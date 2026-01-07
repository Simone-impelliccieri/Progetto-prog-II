package macchinette;

import java.util.Objects;

import macchinette.eccezioni.BinarioVuotoException;

public class Binario {

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

    //preferisco come output STRING piuttosto che enum o optional strani. carica un prodotto nel binario e da vari errori
    // se serve
    public String carica(Prodotto prodotto, int quantita) {

        Objects.requireNonNull(prodotto, "prodotto non può essere null");
        if (quantita <= 0) {
            throw new IllegalArgumentException("quantità deve essere positiva");
        }

        if (!(prodotto.getTaglia().nonSuperioreA(this.taglia))) {
            return "taglia-errata";
        }

        if (èVuoto()) {
            if (quantita > maxCapacita) {
                return "capacità-superata";
            }
            this.tipoProdotto = prodotto;
            this.numeroProdotti = quantita;
            return "OK";
        }

        if (!tipoProdotto.equals(prodotto)) {
            return "prodotto-errato";
        }

        if (numeroProdotti + quantita > maxCapacita) {
            return "capacità-superata";
        }

        this.numeroProdotti += quantita;
        return "OK";
    }

    // Restituisce  Prodotto solleva eccezione personalizzata
    public Prodotto dispensa() {
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
