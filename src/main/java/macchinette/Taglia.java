package macchinette;

import java.util.Objects;

/**
 * Una Taglia è un'entità immutabile che rappresenta la taglia di un prodotto
 * 
 *  Ogni Taglia:
 * <ul>
 *   <li>è uno dei seguenti tipi: S, M, L o XL</li>
 *   <li>possiede un ordinamento naturale dato dall'ordine S M L XL.</li>
 * </ul>
 *
 *
 * Operazioni implementate:
 * <ul>
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
    */

    /**
    * Verifica se questa taglia è minore o uguale della taglia {@code altra} rispetto
    * all'ordinamento naturale S poi M poi L poi XL.
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
        return name();
    }
}
