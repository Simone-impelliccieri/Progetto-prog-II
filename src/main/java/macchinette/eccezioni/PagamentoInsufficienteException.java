package macchinette.eccezioni;

/**
 * Eccezione che indica che il pagamento è insufficiente
 */
public class PagamentoInsufficienteException extends Exception {
    /** Versione di serializzazione. */
    private static final long serialVersionUID = 1L;

    /** Costruisce l'eccezione. */
    public PagamentoInsufficienteException() {
        super();
    }
}
