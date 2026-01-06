package macchinette;

import java.math.BigDecimal;
import java.util.Objects;

public final class Importo implements Comparable<Importo> {
    //CAMPO UNICO
    private final int totaleCentesimi;

    //unità
    public int unita() {
        return totaleCentesimi / 100;
    }

    //centesimi
    public int centesimi() {
        return totaleCentesimi % 100;
    }

    // da unità centesimi a importo
    public Importo(int unita, int centesimi) {
        if (unita < 0) {
            throw new IllegalArgumentException("unita negative");
        }
        if (centesimi < 0 || centesimi > 99) {
            throw new IllegalArgumentException("centesimi fuori range");
        }
        long cents = (long) unita * 100L + (long) centesimi;
        if (cents > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("importo troppo grande");
        }
        this.totaleCentesimi = (int) cents;
    }


    // da centesimi totali PRIVATE
    private Importo(int totaleCentesimi) {
        if (totaleCentesimi < 0) {
            throw new IllegalArgumentException("importo negativo");
        }
        this.totaleCentesimi = totaleCentesimi;
    }

    // da totale centisimi a importo public usando importo private
    public static Importo daCentesimi(int totaleCentesimi) {
        return new Importo(totaleCentesimi);
    }


    // per alcuni clients, da stringa a importo
    public static Importo stringaToImporto(String str) {
        if (str == null) {
            throw new IllegalArgumentException("stringa nulla");
        }
        String s = str.trim();  
        if (s.isEmpty()) {
            throw new IllegalArgumentException("stringa vuota");
        }
        try {
            int cents = new BigDecimal(s).multiply(BigDecimal.valueOf(100)).intValueExact();
            if (cents < 0) {
                throw new IllegalArgumentException("importo negativo");
            }
            return new Importo(cents);
        } catch (ArithmeticException | NumberFormatException e) {
            throw new IllegalArgumentException("formato importo non valido", e);
        }
    }

    //METODI


    // metodo comodità per avere centisimi totali
    public int inCentesimi() {
        return totaleCentesimi;
    }

    // metodo per somma
    public Importo somma(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        long sum = (long) this.totaleCentesimi + (long) altro.totaleCentesimi;
        if (sum > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("somma troppo grande");
        }
        return new Importo((int) sum);
    }

    // metodo per differenza
    public Importo sottrai(Importo altro) {
        Objects.requireNonNull(altro, "importo non può essere null");
        int diff = this.totaleCentesimi - altro.totaleCentesimi;
        if (diff < 0) {
            throw new IllegalArgumentException("risultato negativo");
        }
        return new Importo(diff);
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
        return new Importo((int) prod);
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
        Objects.requireNonNull(o, "importo non può essere valido");
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
            sb.append(u).append(' ').append(u == 1 ? "unit" : "units");
        }
        if (c != 0) {
            if (sb.length() > 0) {
                sb.append(' ');
            }
            sb.append(c).append(' ').append(c == 1 ? "cent" : "cents");
        }
        return sb.toString();
    }
}
