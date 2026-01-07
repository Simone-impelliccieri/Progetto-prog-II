package macchinette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

public class DistributoreAutomatico {

    //campi
    private final List<Binario> binari;
    private final StrategiaResto strategiaResto;
    private Aggregato fondoCassa;

    //costruttore
    public DistributoreAutomatico(List<Binario> binari, Aggregato fondoCassa, StrategiaResto strategiaResto) {

        Objects.requireNonNull(binari, "lista binari non può essere null");
        Objects.requireNonNull(fondoCassa, "fondo cassa non può essere null");
        Objects.requireNonNull(strategiaResto, "strategia resto non può essere null");

        this.strategiaResto = strategiaResto;
        this.binari = Collections.unmodifiableList(new ArrayList<>(binari));
        this.fondoCassa = copiaAggregato(fondoCassa);
    }

    //probabilmente non serve 
    public int getNumeroBinari() {
        return binari.size();
    }

    //serve per specifiche
    public Importo getValoreTotaleFondoCassa() {
        return fondoCassa.getValoreTotale();
    }

    //sostanzialmente toglie tutti i soldi dalla macchinetta e li mette nel Aggregato restituito. serve per specifiche
    public Aggregato svuotaFondoCassa() {
        Aggregato svuotato = copiaAggregato(this.fondoCassa);
        this.fondoCassa = new Aggregato();
        return svuotato;
    }

    //serve a garantire il Disaccoppiamento tra lo stato interno del distributore e il mondo esterno.
    private static Aggregato copiaAggregato(Aggregato origine) {
        Objects.requireNonNull(origine, "aggregato null");
        Aggregato copia = new Aggregato();
        copia.aggiungi(origine);
        return copia;
    }

    // questo serve per specifiche
    public void aggiungiAlFondoCassa(Aggregato daAggiungere) {
        Objects.requireNonNull(daAggiungere, "aggregato non può essere null");
        this.fondoCassa.aggiungi(daAggiungere);
    }

    //immagino serva per qualche client + serve per specifiche
    public List<String> statoProdotti() {
        List<String> righe = new ArrayList<>();

        for (int indice = 0; indice < binari.size(); indice++) {
            Binario binario = binari.get(indice);

            if (binario.èVuoto()) {
                continue;
            }

            Prodotto prodotto = binario.getTipoProdotto();
            if (prodotto == null) {
                throw new IllegalStateException("Binario non correttamente inizializzato");
            }

            righe.add("? " + indice + " | " + prodotto.getNome() + " | " + prodotto.getPrezzo());
        }

        return righe;
    }

    //metodo per caricare prodotti,
    // deve indicare il numero di prodotti che non sono stati caricati per mancanza di spazio
    //  E LO FA, è l'int restituito
    public int carica(Prodotto prodotto, int quantita) {
        Objects.requireNonNull(prodotto, "prodotto non può essere null");
        if (quantita <= 0) {
            throw new IllegalArgumentException("quantità deve essere positiva");
        }

        int quantitaRimanente = quantita;

        for (Binario binario : binari) {
            if (quantitaRimanente == 0) {
                break;
            }

            if (!prodotto.getTaglia().nonSuperioreA(binario.getTaglia())) {
                continue;
            }

            boolean compatibile = false;

            if (binario.èVuoto()) {

                compatibile = true;

            } else {
                Prodotto prodottoPresente = binario.getTipoProdotto();

                if (prodottoPresente != null && prodottoPresente.equals(prodotto)) {
                    compatibile = true;
                }
            }

            if (!compatibile) {
                continue;
            }

            int spazio = binario.getSpazioRimanente();
            if (spazio <= 0) {
                continue;
            }

            int daCaricare = Math.min(quantitaRimanente, spazio);

            String esito = binario.carica(prodotto, daCaricare);

            if ("OK".equals(esito)) {
                quantitaRimanente -= daCaricare;
            }
        }

        return quantitaRimanente;
    }

    /** metodo bello complicatino
     * 
     * DA CAMBIARE ASSOLUTAMENTE; deve restituire solo il resto e nel caso ECCEZIONI PERSONALIZZATE
     * Ritorna una stringa tra:
     * - "slot", "empty", "value", "change" in caso di errore;
     * - il toString del resto in caso di successo.
     */
    public String eroga(int numeroBinario, Aggregato pagamento) {
        Objects.requireNonNull(pagamento, "pagamento non può essere null");

        if (numeroBinario < 0 || numeroBinario >= binari.size()) {
            return "slot"; //eccezione IndexOutOfBoundsException unchecked
        }

        Binario binario = binari.get(numeroBinario);

        if (binario.èVuoto()) {
            return "empty"; //binario vuoto eccezione
        }

        Prodotto prodotto = binario.getTipoProdotto();
        if (prodotto == null) {
            throw new IllegalStateException("Binario non correttamente inizializzato");
        }
        Importo prezzo = prodotto.getPrezzo();
        Importo valorePagamento = pagamento.getValoreTotale();

        if (valorePagamento.compareTo(prezzo) < 0) {
            return "value"; // FondoInsufficienteException checked
        }

        Aggregato disponibilita = copiaAggregato(this.fondoCassa);
        disponibilita.aggiungi(pagamento);

        Importo restoDaDare;
        try {
            restoDaDare = valorePagamento.sottrai(prezzo);
        } catch (IllegalArgumentException e) {
            return "value"; //  FondoInsufficienteException checked
        }

        Aggregato resto = new Aggregato();
        Importo zero = Importo.daCentesimi(0);
        if (restoDaDare.compareTo(zero) != 0) {
            try {
                resto = strategiaResto.calcolaResto(restoDaDare, disponibilita);
            } catch (ValoreInsufficienteException | ComposizioneInsufficienteException e) {
                return "change"; // RestoNonDisponibileException checked, magari differenziare tra valore o composizione
            }
        }

        Aggregato fondoNuovo = copiaAggregato(this.fondoCassa);
        fondoNuovo.aggiungi(pagamento);
        try {
            fondoNuovo.rimuovi(resto);
        } catch (ValoreInsufficienteException | ComposizioneInsufficienteException e) {
            return "change"; // RestoNonDisponibileException checked, magari differenziare tra valore o composizione
        }

        binario.dispensa();
        this.fondoCassa = fondoNuovo;
        return resto.toString();
    }

}
