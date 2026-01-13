package macchinette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import macchinette.eccezioni.*;

// liskov approved

public final class DistributoreAutomatico {

    // NOTA: classe mutabile e NON thread-safe. Se usata in multithread serve sincronizzazione esterna,
    // perché `fondoCassa` e i `Binario` interni possono cambiare durante le operazioni.

    //campi
    private final List<Binario> binari;
    private final StrategiaResto strategiaResto;

    // non è final per poter fare il copiaaggregato
    private Aggregato fondoCassa;

    //costruttore
    public DistributoreAutomatico(List<Binario> binari, Aggregato fondoCassa, StrategiaResto strategiaResto) {

        Objects.requireNonNull(binari, "lista binari non può essere null");
        Objects.requireNonNull(fondoCassa, "fondo cassa non può essere null");
        Objects.requireNonNull(strategiaResto, "strategia resto non può essere null");

        this.strategiaResto = strategiaResto;

        List<Binario> copiaBinari = new ArrayList<>(binari.size());
        for (Binario binario : binari) {
            Objects.requireNonNull(binario, "binario non può essere null");
            copiaBinari.add(new Binario(binario)); // il binario viene copiato QUA con DEEP copy
        }
        this.binari = Collections.unmodifiableList(copiaBinari);
        this.fondoCassa = copiaAggregato(fondoCassa);
    }

    //serve per specifiche quindi immagino debba rimanere public
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

    // questo serve per specifiche quindi immagino public
    public void aggiungiAlFondoCassa(Aggregato daAggiungere) {
        Objects.requireNonNull(daAggiungere, "aggregato non può essere null");
        this.fondoCassa.aggiungi(daAggiungere);
    }

    //serve per specifiche e client: fa una copia e restituisce un iteratore per evitare di esporre direttamente una lista
    public Iterator<String> statoProdotti() {
        List<String> righe = new ArrayList<>();

        for (int indice = 0; indice < binari.size(); indice++) {
            Binario binario = binari.get(indice);

            if (binario.eVuoto()) {
                continue;
            }

            Prodotto prodotto = binario.getTipoProdotto();
            if (prodotto == null) {
                throw new IllegalStateException("Binario non correttamente inizializzato");
            }

            righe.add("? " + indice + " | " + prodotto.getNome() + " | " + prodotto.getPrezzo());
        }

        return Collections.unmodifiableList(righe).iterator();
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

            if (!prodotto.getTaglia().eMinoreUguale(binario.getTaglia())) {
                continue;
            }

            boolean compatibile = false;

            if (binario.eVuoto()) {

                compatibile = true;

            } else {
                Prodotto prodottoPresente = binario.getTipoProdotto();
                if (prodottoPresente != null && prodottoPresente.equals(prodotto)) {
                    compatibile = true;
                } else {
                    compatibile = false;
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

            try {
                binario.carica(prodotto, daCaricare);
                quantitaRimanente -= daCaricare;
            } catch (TagliaNonCompatibileException | ProdottoDiversoException | CapacitaSuperataException e) {
                throw new IllegalStateException("Caricamento fallito");
            }
        }

        return quantitaRimanente;
    }

    /*
     * Deve restituire solo il resto e nel caso lancia eccezioni:
     * - SlotInesistenteException (slot)
     * - BinarioVuotoException (empty)
     * - PagamentoInsufficienteException (value)
     * - RestoNonDisponibileException (change)
     */
    public Aggregato eroga(int numeroBinario, Aggregato pagamento)
            throws SlotInesistenteException, BinarioVuotoException, PagamentoInsufficienteException,
            RestoNonDisponibileException {
        Objects.requireNonNull(pagamento, "pagamento non può essere null");

        if (numeroBinario < 0 || numeroBinario >= binari.size()) {
            throw new SlotInesistenteException();
        }

        Binario binario = binari.get(numeroBinario);

        if (binario.eVuoto()) {
            throw new BinarioVuotoException();
        }

        Prodotto prodotto = binario.getTipoProdotto();
        if (prodotto == null) {
            throw new IllegalStateException("Binario non correttamente inizializzato");
        }
        Importo prezzo = prodotto.getPrezzo();
        Importo valorePagamento = pagamento.getValoreTotale();

        if (valorePagamento.compareTo(prezzo) < 0) {
            throw new PagamentoInsufficienteException();
        }

        Aggregato disponibilita = copiaAggregato(this.fondoCassa);
        disponibilita.aggiungi(pagamento);

        Importo restoDaDare;
        try {
            restoDaDare = valorePagamento.sottrai(prezzo);
        } catch (IllegalArgumentException e) {
            throw new PagamentoInsufficienteException();
        }

        Aggregato resto = new Aggregato();
        Importo zero = Importo.daCentesimi(0);
        if (restoDaDare.compareTo(zero) != 0) {
            try {
                resto = strategiaResto.calcolaResto(restoDaDare, disponibilita);
            } catch (ValoreInsufficienteException | ComposizioneInsufficienteException e) {
                throw new RestoNonDisponibileException();
            }
        }

        Aggregato fondoNuovo = copiaAggregato(this.fondoCassa);
        fondoNuovo.aggiungi(pagamento);
        try {
            fondoNuovo.rimuovi(resto);
        } catch (ValoreInsufficienteException | ComposizioneInsufficienteException e) {
            throw new RestoNonDisponibileException();
        }

        try {
            binario.dispensa();
        } catch (BinarioVuotoException e) {
            throw new IllegalStateException("Binario incoerente: vuoto dopo i controlli");
        }
        this.fondoCassa = fondoNuovo;
        return copiaAggregato(resto);
    }

}
