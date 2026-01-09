package macchinette;

import java.util.Objects;

import macchinette.eccezioni.BinarioVuotoException;
import macchinette.eccezioni.CapacitaSuperataException;
import macchinette.eccezioni.ProdottoDiversoException;
import macchinette.eccezioni.TagliaNonCompatibileException;

public final class Binario {

    //CAMPI

    private final Taglia taglia;

    private final int maxCapacita;

    private Prodotto tipoProdotto;
    private int numeroProdotti;

    //costruttore
    public Binario(Taglia taglia, int maxCapacita) {

        this.taglia = Objects.requireNonNull(taglia, "taglia non può essere null");
        if (maxCapacita <= 0) {
            throw new IllegalArgumentException("la capacità massima deve essere positiva");
        }
        this.maxCapacita = maxCapacita;
        this.tipoProdotto = null;
        this.numeroProdotti = 0;
    }

    //costruttore di copia (deep copy dello stato del binario)
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

    //getter forse da togliere
    public Taglia getTaglia() {
        return taglia;
    }

    public int getMaxCapacita() {
        return maxCapacita;
    }

    public int getNumeroProdotti() {
        return numeroProdotti;
    }

    public int getSpazioRimanente() {
        return maxCapacita - numeroProdotti;
    }

    //vedere se è vuoto
    public boolean èVuoto() {

        if (numeroProdotti == 0) {
            return true;
        }
        return false;

    }

    // ti da il tipo prodotto FORSE QUA METTERE OPTIONAL PERCHE TIPO POTREBBE ESSERE NULL
    public Prodotto getTipoProdotto() {
        return tipoProdotto;
    }

    public void carica(Prodotto prodotto, int quantita)
            throws TagliaNonCompatibileException, CapacitaSuperataException, ProdottoDiversoException {

        Objects.requireNonNull(prodotto, "prodotto non può essere null");
        if (quantita <= 0) {
            throw new IllegalArgumentException("quantità deve essere positiva");
        }

        if (!(prodotto.getTaglia().eMinoreUguale(this.taglia))) {
            throw new TagliaNonCompatibileException();
        }

        if (èVuoto()) {
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

    // Restituisce  Prodotto solleva eccezione personalizzata
    protected Prodotto dispensa() throws BinarioVuotoException {
        if (èVuoto()) {

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

        if (this.èVuoto()) {
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
