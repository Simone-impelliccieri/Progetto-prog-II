package macchinette.eccezioni;

/**
 * Eccezione che indica un tentativo di dispensare da un binario vuoto
 */
public class BinarioVuotoException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione  */
    public BinarioVuotoException() {
        super();
    }

}
