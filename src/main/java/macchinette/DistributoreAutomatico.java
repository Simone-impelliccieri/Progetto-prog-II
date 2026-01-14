package macchinette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import macchinette.eccezioni.*;

/**
 * Un Distributore automatico rappresenta un sistema di vendita automatizzato a moneta.

 * Un Distributore automatico è mutabile perché le operazioni {@code svuotaFondoCassa},
 * {@code aggiungiAlFondoCassa}, {@code carica} e {@code eroga} modificano lo stato dell'istanza.
 *
 * Ogni Distributore automatico è caratterizzato da:
 * <ul>
 *   <li>un elenco di binari {@code binari} numerati da 0</li>
 *   <li>un fondo cassa {@code fondoCassa} (un aggregato di monete)</li>
 *   <li>una strategia per il calcolo del resto {@code strategiaResto}</li>
 * </ul>
 *
 * Operazioni implementate:
 * <ul>
 *   <li>svuotare integralmente il fondo cassa</li>
 *   <li>aggiungere monete al fondo cassa</li>
 *   <li>ottenere il valore totale del fondo cassa</li>
 *   <li>mostrare l'elenco dei prodotti disponibili nei binari non vuoti</li>
 *   <li>caricare una quantità di prodotti nel distributore</li>
 *   <li>erogare un prodotto dato il numero del binario e un pagamento</li>
 * </ul>
 *
 */
public final class DistributoreAutomatico {

    /**
     * Lista dei binari del distributore.
     */
    private final List<Binario> binari;

    /**
     * Strategia utilizzata per calcolare il resto durante l'erogazione.
     */
    private final StrategiaResto strategiaResto;

    /**
     * Fondo cassa del distributore.
     */
    private Aggregato fondoCassa;

    /*
     * AF:
     * <ul>
     *   <li>l'istanza rappresenta un distributore con {@code binari.size()} binari numerati da 0</li>
     *   <li>il fondo cassa contiene le monete di {@code fondoCassa}</li>
     *   <li>il resto viene calcolato secondo {@code strategiaResto}</li>
     * </ul>
     *
     * RI:
     * <ul>
     *   <li>{@code binari} non è nullo e non contiene elementi nulli</li>
     *   <li>{@code fondoCassa} non è nullo</li>
     *   <li>{@code strategiaResto} non è nulla</li>
     * </ul>
    */

    /**
     * Costruttore, crea un distributore automatico con i binari, il fondo cassa
     * e la strategia per il resto specificati.
     *
     * I binari e il fondocassa vengono copiati per garantire l'indipendenza dallo stato esterno.
     * 
     *
     * @param binari, lista dei binari del distributore, non nulla e senza elementi nulli.
     * @param fondoCassa,  aggregato iniziale del fondo cassa, non nullo.
     * @param strategiaResto, strategia per il calcolo del resto, non nulla.
     * @throws NullPointerException se {@code binari}, {@code fondoCassa}, {@code strategiaResto}
     *         o un elemento di {@code binari} è nullo.
     */
    public DistributoreAutomatico(List<Binario> binari, Aggregato fondoCassa, StrategiaResto strategiaResto) {

        Objects.requireNonNull(binari, "lista binari non può essere null");
        Objects.requireNonNull(fondoCassa, "fondo cassa non può essere null");
        Objects.requireNonNull(strategiaResto, "strategia resto non può essere null");

        this.strategiaResto = strategiaResto;

        List<Binario> copiaBinari = new ArrayList<>(binari.size());
        for (Binario binario : binari) {
            Objects.requireNonNull(binario, "binario non può essere null");
            copiaBinari.add(new Binario(binario));
        }
        this.binari = Collections.unmodifiableList(copiaBinari);
        this.fondoCassa = copiaAggregato(fondoCassa);
    }

    /**
     * Restituisce il valore totale del fondo cassa.
     *
     * @return l'importo totale contenuto nel fondo cassa.
     */
    public Importo getValoreTotaleFondoCassa() {
        return fondoCassa.getValoreTotale();
    }

    /**
     * Svuota integralmente il fondo cassa e restituisce le monete rimosse.
     *
     * Dopo l'operazione il fondo cassa è vuoto.
     *
     * @return un aggregato contenente tutte le monete precedentemente nel fondo cassa.
     */
    public Aggregato svuotaFondoCassa() {
        Aggregato svuotato = copiaAggregato(this.fondoCassa);
        this.fondoCassa = new Aggregato();
        return svuotato;
    }

    /**
     * Crea una copia indipendente di un aggregato.
     *
     * Serve a garantire il disaccoppiamento tra lo stato interno del distributore e quello esterno.
     *
     * @param origine, aggregato da copiare, non nullo.
     * @return un nuovo aggregato con le stesse monete di {@code origine}.
     * @throws NullPointerException se {@code origine} è nullo.
     */
    private static Aggregato copiaAggregato(Aggregato origine) {
        Objects.requireNonNull(origine, "aggregato null");
        Aggregato copia = new Aggregato();
        copia.aggiungi(origine);
        return copia;
    }

    /**
     * Aggiunge le monete di un aggregato al fondo cassa.
     *
     * @param daAggiungere, aggregato di monete da aggiungere, non nullo.
     * @throws NullPointerException se {@code daAggiungere} è nullo.
     */
    public void aggiungiAlFondoCassa(Aggregato daAggiungere) {
        Objects.requireNonNull(daAggiungere, "aggregato non può essere null");
        this.fondoCassa.aggiungi(daAggiungere);
    }

    /**
     * Restituisce un iteratore sulle descrizioni dei prodotti disponibili.
     *
     * Per ciascun binario non vuoto viene prodotta una stringa nel formato
     * {@code "? indice | nome | prezzo"}.
     *
     * @return un iteratore, sulle descrizioni dei binari non vuoti.
     * @throws IllegalStateException se un binario non vuoto non possiede un tipo di prodotto.
     */
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

    /**
     * Carica una quantità di prodotti nel distributore.
     *
     * Il caricamento avviene selezionando il primo binario che possa contenere il prodotto, 
     * passando se necessario ai successivi fino ad esaurire la quantità da caricare o lo spazio disponibile.
     *
     * @param prodotto,  prodotto da caricare, non nullo.
     * @param quantita, numero di prodotti da caricare, deve essere positivo.
     * @return il numero di prodotti non caricati per mancanza di spazio.
     * @throws NullPointerException se {@code prodotto} è nullo.
     * @throws IllegalArgumentException se {@code quantita <= 0}.
     */
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
                continue;
            }
        }

        return quantitaRimanente;
    }

    /**
     * Eroga un prodotto dal binario specificato dato un pagamento.
     *
     * L'erogazione è possibile se:
     * <ul>
     *   <li>il binario specificato esiste e non è vuoto</li>
     *   <li>il prodotto ha un prezzo non superiore all'importo pagato</li>
     *   <li>il fondo cassa  è in grado di erogare il resto, se necessario</li>
     * </ul>
     *
     * Se l'erogazione ha successo, il prodotto viene rimosso dal binario,
     * il pagamento viene aggiunto al fondo cassa e il resto viene restituito.
     *
     * @param numeroBinario, indice del binario da cui erogare (a partire da 0).
     * @param pagamento, aggregato di monete usato per il pagamento, non nullo.
     * @return l'aggregato corrispondente al resto (può essere vuoto se non dovuto).
     * @throws NullPointerException se {@code pagamento} è nullo.
     * @throws SlotInesistenteException se {@code numeroBinario} non corrisponde ad alcun binario.
     * @throws BinarioVuotoException se il binario indicato è vuoto.
     * @throws PagamentoInsufficienteException se il pagamento è inferiore al prezzo del prodotto.
     * @throws RestoNonDisponibileException se non è possibile calcolare il resto.
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
