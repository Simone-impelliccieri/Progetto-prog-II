package macchinette.eccezioni;

/**
 * Eccezione che indica che il binario richiesto non esiste
 */
public class SlotInesistenteException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione. */
    public SlotInesistenteException() {
        super();
    }
}
