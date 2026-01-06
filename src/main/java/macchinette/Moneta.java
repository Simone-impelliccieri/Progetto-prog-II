package macchinette;

import java.util.Optional;

public enum Moneta {

    //valori consentiti. MONETA è UN SINGOLO OGGETTO
    C01(1),
    C02(2),
    C05(5),
    C10(10),
    C20(20),
    C50(50),
    E1(100),
    E2(200);

    //campo
    private final Importo valore;

    //costruttore privato
    Moneta(int centesimi) {
        this.valore = Importo.daCentesimi(centesimi);
    }

    //get valore
    public Importo getValore() {
        return valore;
    }

    // converte stringa in importo e poi moneta
    public static Optional<Moneta> daStringa(String stringa) {
        if (stringa == null) {
            return Optional.empty();
        }
        try {
            Importo importo = Importo.stringaToImporto(stringa);
            return daImporto(importo);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    // converte importo in moneta
    public static Optional<Moneta> daImporto(Importo importo) {
        if (importo == null)
            return Optional.empty();

        for (Moneta m : values()) {
            if (m.getValore().equals(importo)) {
                return Optional.of(m);
            }
        }

        return Optional.empty();
    }

    //tostring di importo
    @Override
    public String toString() {
        return valore.toString();
    }
}