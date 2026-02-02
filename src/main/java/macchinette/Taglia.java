package macchinette;

import java.util.Objects;

/**
 * Una Taglia è un'entità immutabile che rappresenta la taglia di un prodotto
 * 
 *  Ogni Taglia:
 * <ul>
 *   <li>è uno dei seguenti tipi:S, M ,L o XL</li>
 *   <li>possiede un ordinamento naturale dato dall'ordine S M L XL.</li>
 * </ul>
 *
 *
 * Operazioni implementate:
 * <ul>
 *   <li>riconoscere una taglia a partire da una stringa </li>
 *   <li>verificare se una taglia è minore o uguale di un'altra</li>
 * </ul>
 *
 * 
 */
public enum Taglia {

    /** Taglia small. */
    S,
    /** Taglia medium. */
    M,
    /** Taglia large. */
    L,
    /** Taglia extra-large. */
    XL;

    /*
    * AF:
    * <ul>
    *   <li>{@code S} rappresenta la taglia small</li>
    *   <li>{@code M} rappresenta la taglia medium</li>
    *   <li>{@code L} rappresenta la taglia large</li>
    *   <li>{@code XL} rappresenta la taglia extra-large</li>
    * 
    * </ul>
    *
    * RI:
    * <ul>
    *   <li>lo stato di una {@code Taglia} è determinato dalla costante enum</li>
    * </ul>
    */

    // HO CAMBIATO DA TESTO.CHARAT(0) a SOLO TESTO

    /**
    *
    * 
    * Restituisce la taglia corrispondente alla stringa in ingresso.
    *
    * La stringa può contenere spazi bianchi iniziali e finali, che vengono ignorati.
    * È riconosciuta una singola lettera tra "S", "M","L" o "XL".
    * In tutti gli altri casi viene sollevata un'eccezione.
    * 
    *
    * @param stringa, che rappresenta una taglia.
    * @return la taglia riconosciuta.
    * @throws IllegalArgumentException se la stringa è nulla o non rappresenta una taglia valida.
    */
    public static Taglia daStringa(String stringa) {
        if (stringa == null) {
            throw new IllegalArgumentException("stringa nulla");
        }

        String testo = stringa.trim();

        if ("XL".equals(testo)) {
            return XL;
        }

        if (testo.length() != 1) {
            throw new IllegalArgumentException("taglia non valida");
        }

        switch (testo.charAt(0)) {
            case 'S':
                return S;
            case 'M':
                return M;
            case 'L':
                return L;
            default:
                throw new IllegalArgumentException("taglia non valida");
        }
    }

    /**
    * Verifica se questa taglia è minore o uguale della taglia {@code altra} rispetto
    * all'ordinamento naturale S poi M poi L po XL.
    *
    * @param altra, la taglia di confronto, non null.
    * @return true se questa taglia è minore o uguale ad {@code altra}, false in caso contrario.
    * @throws NullPointerException se {@code altra} è null.
    */
    public boolean eMinoreUguale(Taglia altra) {
        Objects.requireNonNull(altra, "taglia nulla");
        return this.compareTo(altra) <= 0;
    }

    @Override
    public String toString() {
        if (this == XL) {
            return "XL";
        }
        return String.valueOf(name().charAt(0));
    }
}
