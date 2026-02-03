package macchinette.eccezioni;

/**
 * Eccezione che indica che la taglia del prodotto non è compatibile con il binario
 */
public class TagliaNonCompatibileException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione. */
    public TagliaNonCompatibileException() {
        super();
    }
}
