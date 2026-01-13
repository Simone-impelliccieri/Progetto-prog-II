package macchinette;

import java.util.Objects;
import java.util.Optional;

//liskov approved

/**
 * Una Taglia è un'entità immutabile che rappresenta la taglia di un prodotto
 * 
 *  Ogni Taglia:
 * <ul>
 *   <li>è uno dei seguenti tipi:S, M o L</li>
 *   <li>possiede un ordinamento naturale dato dall'ordine S M L.</li>
 * </ul>
 *
 *
 * Operazioni implementate:
 * <ul>
 *   <li>riconoscere una taglia a partire da una stringa </li>
 *   <li>verificare se una taglia è minore o uguale di un'altra</li>
 * </ul>
 *
 * AF:
 * <ul>
 *   <li>{@code S} rappresenta la taglia small</li>
 *   <li>{@code M} rappresenta la taglia medium</li>
 *   <li>{@code L} rappresenta la taglia large</li>
 * </ul>
 *
 * RI:
 * <ul>
 *   <li>lo stato di una {@code Taglia} è determinato dalla costante enum</li>
 * </ul>
 */
public enum Taglia {

    /** Taglia small. */
    S,
    /** Taglia medium. */
    M,
    /** Taglia large. */
    L;

    //serve per ottenere la taglia a partire da stringa. Utile solo per client
    // magari cambiare nome e magari togliere Optional e trovare un altro modo(no exception)

    //serve per forza perchè nei client arriva input STRING e non è possibile inizializzare direttamente.

    // potrei togliere l'optional VOLENDO, ma con optional obbligo il programmatore a gestire nel caso sia null
    /**
    * Restituisce la taglia corrispondente alla stringa in ingresso.
    *
    * La stringa può contenere spazi bianchi iniziali e finali, che vengono ignorati.
    * È riconosciuta una singola lettera tra "S", "M" e "L" .
    * In tutti gli altri casi il risultato è Optional.Empty.
    *
    * @param stringa che rappresenta una taglia, può essere {@code null}.
    * @return un Optional contenente la taglia riconosciuta oppure Optional.empty() in caso non sia stata trovata.
    */
    public static Optional<Taglia> daStringa(String stringa) {
        if (stringa == null) {
            return Optional.empty();
        }

        String testo = stringa.trim();

        if (testo.length() != 1) {
            return Optional.empty();
        }

        switch (testo.charAt(0)) {
            case 'S':
                return Optional.of(S);
            case 'M':
                return Optional.of(M);
            case 'L':
                return Optional.of(L);
            default:
                return Optional.empty();
        }
    }

    // se superiore a un altra taglia (o minoreuguale)
    /**
    * Verifica se questa taglia è minore o uguale della taglia {@code altra} rispetto
    * all'ordinamento naturale S poi M poi L.
    *
    * @param altra la taglia di confronto, non null.
    * @return true se questa taglia è minore o uguale ad {@code altra}, false in caso contrario.
    * @throws NullPointerException se {@code altra} è null.
    */
    public boolean eMinoreUguale(Taglia altra) {
        Objects.requireNonNull(altra, "taglia nulla");
        return this.compareTo(altra) <= 0;
    }

    @Override
    public String toString() {
        return String.valueOf(name().charAt(0));
    }
}
