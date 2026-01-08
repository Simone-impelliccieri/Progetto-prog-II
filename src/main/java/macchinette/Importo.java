package macchinette;

import java.math.BigDecimal;
import java.util.Objects;

//liksov approvata

// il comparable è ciò che permette che 
//"L'ordinamento naturale delle monete e degli importi è dato dal loro valore"

public final class Importo implements Comparable<Importo> {

    //CAMPO UNICO
    private final int totaleCentesimi;

    //unità
    private int unita() {
        return totaleCentesimi / 100;
    }

    //centesimi
    private int centesimi() {
        return totaleCentesimi % 100;
    }

    // da unità centesimi a importo COSTRUTTORE
    public Importo(int unita, int centesimi) {
        if (unita < 0) {
            throw new IllegalArgumentException("unita negative");
        }
        if (centesimi < 0 || centesimi > 99) {
            throw new IllegalArgumentException("centesimi fuori range");
        }
        long cents = (long) unita * 100L + (long) centesimi; // magari studiarsi questa parte
        if (cents > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("importo troppo grande");
        }
        this.totaleCentesimi = (int) cents;
    }

    // da totale centesimi a importo metodo FACTORY statico
    //non è costruttore SOLO per poterlo chiamare "daCentesimi"
    public static Importo daCentesimi(int totaleCentesimi) {
        if (totaleCentesimi < 0) {
            throw new IllegalArgumentException("importo negativo");
        }
        int unita = totaleCentesimi / 100;
        int centesimi = totaleCentesimi % 100;
        return new Importo(unita, centesimi);
    }

    // per alcuni clients, da stringa a importo. metodo FACTORY statico
    public static Importo daStringa(String str) {
        if (str == null) {
            throw new IllegalArgumentException("stringa nulla");
        }
        String s = str.trim();
        if (s.isEmpty()) {
            throw new IllegalArgumentException("stringa vuota");
        }
        try {
            int totalCents = new BigDecimal(s).multiply(BigDecimal.valueOf(100)).intValueExact();
            if (totalCents < 0) {
                throw new IllegalArgumentException("importo negativo");
            }
            return daCentesimi(totalCents);
        } catch (ArithmeticException | NumberFormatException e) {
            throw new IllegalArgumentException("formato importo non valido", e);
        }
    }

    // metodo per somma
    public Importo somma(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        long sum = (long) this.totaleCentesimi + (long) altro.totaleCentesimi;
        if (sum > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("somma troppo grande");
        }
        return daCentesimi((int) sum);
    }

    // metodo per differenza
    public Importo sottrai(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        int diff = this.totaleCentesimi - altro.totaleCentesimi;
        if (diff < 0) {
            throw new IllegalArgumentException("risultato negativo");
        }
        return daCentesimi(diff);
    }

    // metodo per moltiplicare
    public Importo moltiplica(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("moltiplicatore negativo");
        }
        long prod = (long) this.totaleCentesimi * (long) n;
        if (prod > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("prodotto troppo grande");
        }
        return daCentesimi((int) prod);
    }

    // metodo per dividere
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
