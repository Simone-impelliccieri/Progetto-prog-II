package macchinette;

import java.util.Objects;
import java.util.Optional;

public enum Taglia {
    S,
    M,
    L;

    //serve per ottenere la taglia a partire da stringa. Utile solo per client
    // magari cambiare nome e magari togliere Optional e trovare un altro modo(no exception)

    //serve per forza perchè nei client arriva input STRING e non è possibile inizializzare direttamente.

    // potrei togliere l'optional VOLENDO
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
    public boolean eMinoreUguale(Taglia altra) {
        Objects.requireNonNull(altra, "taglia nulla");
        return this.compareTo(altra) <= 0;
    }

    @Override
    public String toString() {
        return String.valueOf(name().charAt(0));
    }
}
