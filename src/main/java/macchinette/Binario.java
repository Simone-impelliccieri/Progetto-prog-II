package macchinette;

import java.util.Objects;
import java.util.Optional;

import macchinette.eccezioni.BinarioVuotoException;
import macchinette.eccezioni.CapacitaSuperataException;
import macchinette.eccezioni.ProdottoDiversoException;
import macchinette.eccezioni.TagliaNonCompatibileException;

//più o meno liskov approved

public final class Binario {

    //CAMPI

    private final Taglia taglia;

    private final int maxCapacita;

    private Prodotto tipoProdotto;
    private int numeroProdotti;

    //costruttore
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

    //costruttore di copia (deep copy dello stato del binario), devo approfondire il signficato di questo
    public Binario(Binario altro) {
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

    //getter forse da togliere, sicuro da mettere package private
    public Taglia getTaglia() {
        return taglia;
    }

    public int getSpazioRimanente() {
        return maxCapacita - numeroProdotti;
    }

    //vedere se è vuoto
    public boolean eVuoto() {

        return numeroProdotti == 0;

    }

    // ti da il tipo prodotto OPTIONAL
    public Optional<Prodotto> getTipoProdotto() {
        if (tipoProdotto == null) {
            return Optional.empty();
        }
        return Optional.of(tipoProdotto);
    }

    // carica un prodotto sul binario, OBBLIGATO AD AVERE QUELLE ECCEZIONI PER VIA CLIENT
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

    // Restituisce  Prodotto solleva eccezione personalizzata IO FAREI PUBLIC perchè è un comportamento
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
