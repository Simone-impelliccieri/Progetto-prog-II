package macchinette.eccezioni;

/**
 * Eccezione che indica che il valore totale da rimuovere è maggiore di quello disponibile
 */
public class ValoreInsufficienteException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione. */
    public ValoreInsufficienteException() {
        super();
    }
}
