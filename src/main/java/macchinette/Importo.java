package macchinette;

import java.util.Objects;

/**
 * Un Importo è un'entità immutabile che rappresenta un valore
 * composto da una parte unità e da una parte in centesimi.
 * 
 * L'ordinamento naturale degli importi è dato dal loro valore.
 *
 * Operazioni implementate:
 * <ul>
 *   <li>somma tra importi</li>
 *   <li>sottrazione tra importi</li>
 *   <li>moltiplicazione ad un intero</li>
 *   <li>divisione intera tra importi, pari al più grande n tale che {@code J * n <= I}</li>
 * </ul>
 * 
 * 
 * L'uguaglianza tra importi dipende dal valore.
 */
public class Importo implements Comparable<Importo> {

    /**
     * Valore dell'importo in centesimi
     */
    private final int totaleCentesimi;

    /**
     * Restituisce la parte delle unità dell'importo
     * @return il numero di unità (intero non negativo).
     */
    public int unita() {
        return totaleCentesimi / 100;
    }

    /**
     * Restituisce la parte in centesimi dell'importo.
     *
     * @return il numero di centesimi, compreso tra 0 e 99.
     */
    public int centesimi() {
        return totaleCentesimi % 100;
    }

    /*
     * AF:
     * <ul>
     *   <li>l'istanza costruita rappresenta l'importo di valore {@code unita} unità e {@code centesimi} centesimi</li>
     * </ul>
     *
     * RI:
     * <ul>
     *   <li> totaleCentesimi >= 0,</li>
     * </ul>
     */

    /**
     * Costruttore, crea un'istanza di Importo a partire da unita e centesimi.
     *
     * @param unita, deve essere non negativa.
     * @param centesimi, deve essere compresa tra 0 e 99.
     * @throws IllegalArgumentException se {@code unita < 0}, se {@code centesimi} non è nel range
     *         consentito, oppure se unità + centesimi supera il valore massimo.
     */
    public Importo(int unita, int centesimi) {
        if (unita < 0) {
            throw new IllegalArgumentException("unita negative");
        }
        if (centesimi < 0 || centesimi > 99) {
            throw new IllegalArgumentException("centesimi fuori range");
        }
        int cents = (unita * 100) + centesimi;
        this.totaleCentesimi = cents;
    }

    /**
     * Metodo factory statico, restituisce un importo a partire dal totale dei centesimi.
     *
     * @param totaleCentesimi, totale in centesimi, deve essere non negativo.
     * @return un importo corrispondente a {@code totaleCentesimi}.
     * @throws IllegalArgumentException se {@code totaleCentesimi < 0}.
     */
    public static Importo daCentesimi(int totaleCentesimi) {
        if (totaleCentesimi < 0) {
            throw new IllegalArgumentException("importo negativo");
        }
        int unita = totaleCentesimi / 100;
        int centesimi = totaleCentesimi % 100;
        return new Importo(unita, centesimi);
    }

    /**
     * Restituisce la somma tra due importi
     *
     * @param altro, l'altro importo (non null).
     * @return un nuovo importo pari a  this + altro.
     * @throws NullPointerException se  altro è null.
     * @throws IllegalArgumentException se la somma è superiore di MAX_VALUE.
     */
    public Importo somma(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        int sum = this.totaleCentesimi + altro.totaleCentesimi;
        return daCentesimi(sum);
    }

    /**
     * Restituisce la differenza tra due importi
     *
     * @param altro, l'altro importo (non  null).
     * @return un nuovo importo pari a this - altro.
     * @throws NullPointerException se  altro è  null.
     * @throws IllegalArgumentException se il risultato sarebbe negativo.
     */
    public Importo sottrai(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        int diff = this.totaleCentesimi - altro.totaleCentesimi;
        if (diff < 0) {
            throw new IllegalArgumentException("risultato negativo");
        }
        return daCentesimi(diff);
    }

    /**
     * Restituisce il risultato della moltiplicazione tra l'importo e un intero non negativo.
     *
     * @param n, intero che moltiplica; deve essere non negativo.
     * @return un nuovo importo pari a  this * n.
     * @throws IllegalArgumentException se {@code n} è negativo o se il risultato è superiore di MAX_VALUE
     */
    public Importo moltiplica(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("moltiplicatore negativo");
        }
        int prod = this.totaleCentesimi * n;
        return daCentesimi(prod);
    }

    /**
     * Restituisce il risultato della divisione tra due importi.
     *
     * Il risultato è il più grande intero {@code k} tale che {@code k * altro <= this}.
     *
     * @param altro, il divisore (non  null).
     * @return il risultato della divisione intera this / altro.
     * @throws NullPointerException se altro è null.
     * @throws IllegalArgumentException se  l'altro è pari a zero
     */
    public int divIntera(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        if (altro.totaleCentesimi == 0) {
            throw new IllegalArgumentException("divisione per zero non ammessa");
        }
        return this.totaleCentesimi / altro.totaleCentesimi;
    }

    @Override
    public int compareTo(Importo o) {
        Objects.requireNonNull(o, "importo non può essere null");
        return Integer.compare(this.totaleCentesimi, o.totaleCentesimi);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Importo altro)) {
            return false;
        }
        return this.totaleCentesimi == altro.totaleCentesimi;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(totaleCentesimi);
    }

    @Override
    public String toString() {
        int u = unita();
        int c = centesimi();

        StringBuilder sb = new StringBuilder();
        if (u != 0 || totaleCentesimi == 0) {
            if (u == 1) {
                sb.append(u).append(' ').append("unit");
            } else {
                sb.append(u).append(' ').append("units");
            }
        }
        if (c != 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(c).append(' ');
            if (c == 1) {
                sb.append("cent");
            } else {
                sb.append("cents");
            }
        }
        return sb.toString();
    }
}
