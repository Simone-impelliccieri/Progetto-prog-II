package macchinette;

import java.util.Objects;

import macchinette.eccezioni.BinarioVuotoException;
import macchinette.eccezioni.CapacitaSuperataException;
import macchinette.eccezioni.ProdottoDiversoException;
import macchinette.eccezioni.TagliaNonCompatibileException;

/**
 * Un Binario rappresenta un contenitore di prodotti all'interno di un distributore automatico.
 * 
 * Un Binario è un'entità mutabile perchè le operazione carica e dispensa modificano lo stato dell'istanza.
 * 
 *
 * Ogni Binario è caratterizzato da:
 * <ul>
 *   <li>una taglia massima {@code taglia}</li>
 *   <li>una capacità massima {@code maxCapacita} (intero positivo)</li>
 *   <li>un eventuale tipo di prodotto {@code tipoProdotto} e un numero di prodotti {@code numeroProdotti}</li>
 * </ul>
 * 
 * 
 *  Operazioni implementate:
 * <ul>
 *   <li>verificare se un binario è vuoto</li>
 *   <li>caricare un prodotto</li>
 *   <li>dispensare un prodotto</li>
 * </ul>
 *
 * Un binario può essere vuoto oppure contenere fino a {@code maxCapacita} prodotti.
 * 
 * Se non è vuoto, contiene prodotti tutti dello stesso tipo e con taglia non superiore alla taglia del binario.
 *
 */
public final class Binario {

    /**
     * Taglia massima dei prodotti che il binario può contenere.
     */
    private final Taglia taglia;

    /**
     * Capacità massima del binario.
     */
    private final int maxCapacita;

    /**
     * Eventuale tipo di prodotto contenuto nel binario .
     */
    private Prodotto tipoProdotto;

    /**
     * Numero di prodotti attualmente presenti nel binario.
     */
    private int numeroProdotti;

    /*
     * AF:
     * <ul>
     *   <li>l'istanza rappresenta un binario di taglia {@code taglia} e capacità {@code maxCapacita}</li>
     *   <li>se {@code numeroProdotti == 0} allora il binario è vuoto, altrimenti contiene {@code numeroProdotti}
     *       copie del prodotto {@code tipoProdotto}.</li>
     * </ul>
     *
     * RI:
     * <ul>
     *   <li>{@code taglia} non è nulla</li>
     *   <li>{@code maxCapacita > 0}</li>
     *   <li>{@code 0 <= numeroProdotti <= maxCapacita}</li>
     *   <li>{@code numeroProdotti == 0} se {@code tipoProdotto} non è impostato</li>
     * </ul>
    */

    /**
     * Costruttore, crea un binario vuoto di taglia {@code taglia} e capacità massima {@code maxCapacita}.
     *
     * @param taglia, taglia massima del binario.
     * @param maxCapacita, capacità massima del binario, deve essere positiva.
     * @throws NullPointerException se {@code taglia} è null.
     * @throws IllegalArgumentException se {@code maxCapacita <= 0}.
     */
    public Binario(Taglia taglia, int maxCapacita) {

        Objects.requireNonNull(taglia, "taglia non può essere null");

        if (maxCapacita <= 0) {
            throw new IllegalArgumentException("la capacità massima deve essere positiva");
        }

        this.taglia = taglia;
        this.maxCapacita = maxCapacita;
        this.tipoProdotto = null;
        this.numeroProdotti = 0;
    }

    /**
     * Costruttore di copia, costruisce un nuovo binario copiando lo stato di {@code altro}.
     *
     * La copia duplica taglia, capacità, tipo di prodotto e numero di prodotti.
     *
     * Serve per ottenere un binario indipendente in modo che lo stato copiato non viene modificato da operazioni
     * eseguite sull'istanza originale.
     *
     * @param altro, binario da copiare.
     * @throws NullPointerException se {@code altro} è null.
     * @throws IllegalStateException se lo stato di {@code altro} non rispetta i vincoli interni.
     */
    Binario(Binario altro) {
        Objects.requireNonNull(altro, "binario non può essere null");
        this.taglia = altro.taglia;
        this.maxCapacita = altro.maxCapacita;

        if (altro.numeroProdotti < 0 || altro.numeroProdotti > altro.maxCapacita) {
            throw new IllegalStateException("binario non valido");
        }
        if (altro.numeroProdotti == 0) {
            this.tipoProdotto = null;
            this.numeroProdotti = 0;
            return;
        }
        if (altro.tipoProdotto == null) {
            throw new IllegalStateException("binario non correttamente inizializzato");
        }

        this.tipoProdotto = altro.tipoProdotto;
        this.numeroProdotti = altro.numeroProdotti;
    }

    /**
     * Restituisce la taglia del binario.
     *
     * @return la taglia del binario.
     */
    Taglia getTaglia() {
        return taglia;
    }

    /**
     * Restituisce lo spazio ancora disponibile nel binario.
     *
     * @return il numero di prodotti ancora caricabili.
     */
    int getSpazioRimanente() {
        return maxCapacita - numeroProdotti;
    }

    /**
     * Indica se il binario è vuoto.
     *
     * @return {@code true} se non contiene prodotti, {@code false} altrimenti.
     */
    public boolean eVuoto() {

        return numeroProdotti == 0;

    }

    /**
     * Restituisce il tipo di prodotto contenuto nel binario.
     *
     * @return il prodotto contenuto, oppure null se il binario è vuoto.
     */
    Prodotto getTipoProdotto() {
        return tipoProdotto;
    }

    /**
     * Carica {@code quantita} prodotti di tipo {@code prodotto} nel binario.
     *
     * Il caricamento fallisce se:
     * <ul>
     *   <li>la taglia del prodotto è maggiore della taglia del binario</li>
     *   <li>il binario è vuoto ma {@code quantita} è superiore della capacità massima</li>
     *   <li>il binario non è vuoto e il prodotto è diverso da quello già nel binario</li>
     *   <li>il numero totale di prodotti supera la capacità massima</li>
     * </ul>
     *
     * @param prodotto, prodotto da caricare.
     * @param quantita, numero di prodotti da caricare, deve essere positivo.
     * @throws NullPointerException se {@code prodotto} è null.
     * @throws IllegalArgumentException se {@code quantita <= 0}.
     * @throws TagliaNonCompatibileException se la taglia di {@code prodotto} eccede la taglia del binario.
     * @throws CapacitaSuperataException se il caricamento supera {@code maxCapacita}.
     * @throws ProdottoDiversoException se il binario non è vuoto e {@code prodotto} è diverso dal tipo presente.
     */
    public void carica(Prodotto prodotto, int quantita)
            throws TagliaNonCompatibileException, CapacitaSuperataException, ProdottoDiversoException {

        Objects.requireNonNull(prodotto, "prodotto non può essere null");

        if (quantita <= 0) {
            throw new IllegalArgumentException("quantità deve essere positiva");
        }

        if (!(prodotto.getTaglia().eMinoreUguale(this.taglia))) {
            throw new TagliaNonCompatibileException();
        }

        if (eVuoto()) {
            if (quantita > maxCapacita) {
                throw new CapacitaSuperataException();
            }
            this.tipoProdotto = prodotto;
            this.numeroProdotti = quantita;
            return;
        }

        if (!tipoProdotto.equals(prodotto)) {
            throw new ProdottoDiversoException();
        }

        if (numeroProdotti + quantita > maxCapacita) {
            throw new CapacitaSuperataException();
        }

        this.numeroProdotti += quantita;
        return;
    }

    /**
     * Dispensa un prodotto dal binario.
     *
     * Se il binario contiene almeno un prodotto, sottrae un unità a {@code numeroProdotti}
     * e restituisce il prodotto.
     *
     * @return il prodotto dispensato.
     * @throws BinarioVuotoException se il binario è vuoto.
     */
    public Prodotto dispensa() throws BinarioVuotoException {
        if (eVuoto()) {

            throw new BinarioVuotoException();
        }

        Prodotto prodottoErogato = tipoProdotto;
        numeroProdotti--;

        if (numeroProdotti == 0) {
            tipoProdotto = null;
        }

        return prodottoErogato;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<");

        if (this.eVuoto()) {
            sb.append("-");
        } else {
            sb.append(this.tipoProdotto);
        }

        sb.append(", ");
        sb.append(this.taglia);

        sb.append(", ");
        sb.append(this.numeroProdotti);

        sb.append(", ");
        sb.append(this.maxCapacita);

        sb.append(">");

        return sb.toString();
    }

}
