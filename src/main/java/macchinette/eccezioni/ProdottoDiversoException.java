package macchinette.eccezioni;

/**
 * Eccezione che indica un tentativo di caricare un prodotto diverso da quello del binario
 */
public class ProdottoDiversoException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione. */
    public ProdottoDiversoException() {
        super();
    }
}
