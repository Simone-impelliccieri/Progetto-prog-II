package macchinette;

import java.util.Objects;
import java.util.Optional;

public enum Taglia {
    S,
    M,
    L;

    //da carattere
    public static Optional<Taglia> daCarattere(char carattere) {
        return switch (carattere) {
            case 'S' -> Optional.of(S);
            case 'M' -> Optional.of(M);
            case 'L' -> Optional.of(L);
            default -> Optional.empty();
        };
    }

    //da stringa
    public static Optional<Taglia> daStringa(String stringa) {
        if (stringa == null) {
            return Optional.empty();
        }
        String testo = stringa.trim();
        if (testo.length() != 1) {
            return Optional.empty();
        }
        return daCarattere(testo.charAt(0));
    }

    // se superiore a un altra taglia
    public boolean nonSuperioreA(Taglia altra) {
        Objects.requireNonNull(altra, "taglia nulla");
        return this.compareTo(altra) <= 0;
    }

    @Override
    public String toString() {
        return String.valueOf(name().charAt(0));
    }
}
