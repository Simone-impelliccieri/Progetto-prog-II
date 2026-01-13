package macchinette.eccezioni;

/**
 * Eccezione che indica l'impossibilità di erogare il resto
 */
public class RestoNonDisponibileException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione. */
    public RestoNonDisponibileException() {
        super();
    }
}
