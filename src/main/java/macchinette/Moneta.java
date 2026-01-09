package macchinette;

import java.util.Optional;

//liskov approved   


public enum Moneta {
    // " L'ordinamento naturale delle monete e degli importi è dato dal loro valore" è implicito in enum,
    // è in base a come li scrivi

    //si inizializza una moneta semplicemente facendo Moneta m = Moneta.E1, il resto è automatico   

    //oggetti consentiti del tipo Moneta.
    C01(1),
    C02(2),
    C05(5),
    C10(10),
    C20(20),
    C50(50),
    E1(100),
    E2(200);

    //campo interno
    private final Importo valore;

    //costruttore privato . ad esempio c01(1)--> moneta.valore = 1
    Moneta(int centesimi) {
        this.valore = Importo.daCentesimi(centesimi);
    }

    //get valore. Essenziale(anche perchè il valore è evidente anche dal nome)
    public Importo getValore() {
        return valore;
    }

    //serve per ottenere la moneta più grande che possa contenere l'importo. Utile solo per client
    // magari cambiare nome e magari togliere Optional e trovare un altro modo(no exception)
    public static Optional<Moneta> monetaGiusta(String stringa) {
        if (stringa == null) {
            return Optional.empty();
        }
        try {
            Importo importo = Importo.daStringa(stringa);

            for (Moneta moneta : values()) {
                if (moneta.getValore().equals(importo)) {
                    return Optional.of(moneta);
                }
            }

            return Optional.empty();
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    //tostring di importo
    @Override
    public String toString() {
        return valore.toString();
    }
}