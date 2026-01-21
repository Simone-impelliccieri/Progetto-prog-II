package macchinette;

import java.util.Optional;

/**
 * Una Moneta è un'entità immutabile che rappresenta un tipo di moneta ammesso dal distributore automatico
 *
 * Ogni Moneta:
 * <ul>
 *   <li>ha un valore rappresentato da un Importo non negativo</li>
 *   <li>è uno dei seguenti tipi: 1, 2, 5, 10, 20 o 50 centesimi, oppure 1 o 2 unità</li>
 *   <li>possiede un ordinamento naturale dato dal valore</li>
 * </ul>
 *
 * Operazioni implementate :
 * <ul>
 *   <li>determinare la moneta il cui valore corrisponde ad una stringa fornita</li>
 * </ul>
 * 
 * 
 */
public enum Moneta {

    /** Moneta da 1 centesimo */
    C01(1),
    /** Moneta da 2 centesimi */
    C02(2),
    /** Moneta da 5 centesimi */
    C05(5),
    /** Moneta da 10 centesimi */
    C10(10),
    /** Moneta da 20 centesimi */
    C20(20),
    /** Moneta da 50 centesimi */
    C50(50),
    /** Moneta da 1 unità */
    E1(100),
    /** Moneta da 2 unità */
    E2(200);

    /**
     * Valore della moneta.
     */
    private final Importo valore;

    /*
     * AF:
     * <ul>
     * <li>ogni costante rappresenta la moneta con valore {@code valore}.</li>
     * </ul>
     *
     * RI:
     * <ul>
     * <li>{@code valore} != null</li>
     * <li>{@code valore} rappresenta un importo non negativo</li>
     * <li>{@code valore} è uno dei tipi di moneta definiti</li>
     * </ul>
     */

    /**
     * 
     * 
     * Costruttore dell’enum , costruisce una moneta assegnandole il valore indicato in centesimi
     *
     * @param centesimi, valore della moneta in centesimi; deve essere non negativo.
     * @throws IllegalArgumentException se centesimi minore di 0.
     */
    private Moneta(int centesimi) {
        
        this.valore = Importo.daCentesimi(centesimi);
    }

    /**
     * Restituisce il valore della moneta.
     *
     * @return il valore della moneta come Importo .
     */
    Importo getValore() {
        return valore;
    }

    /**
     * Cerca la moneta il cui valore è uguale alla stringa in input.
     *
     * La stringa viene interpretata tramite Importo.daStringa(String)
     * Se la stringa non rappresenta un importo valido o se il valore
     * ottenuto non corrisponde a nessun tipo di moneta il risultato è optional.empty .
     *
     * @param stringa, rappresentazione testuale dell'importo; se {@code null} il risultato è vuoto.
     * @return un Optional contenente la moneta corrispondente, oppure Optional.empty().
     */
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

    @Override
    public String toString() {
        return valore.toString();
    }
}