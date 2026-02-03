package macchinette.eccezioni;

/**
 * Eccezione che indica che la quantità da caricare è superiore alla capacità del binario
 */
public class CapacitaSuperataException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione */
    public CapacitaSuperataException() {
        super();
    }
}
