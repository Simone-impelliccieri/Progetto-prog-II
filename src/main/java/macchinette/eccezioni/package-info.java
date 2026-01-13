/**
 * Pacchetto che contiene le eccezioni personalizzate.
 * 
 * 
 * Eccezioni relative agli Aggregati di monete:
 * <ul>
 *   <li>{@code ValoreInsufficienteException} il valore totale da rimuovere è maggiore di quello disponibile</li>
 *   <li>{@code ComposizioneInsufficienteException} errore di composizione</li>
 * </ul>
 *
 * Eccezioni relative ai Binari:
 * <ul>
 *   <li>{@code BinarioVuotoException} tentativo di dispensare da un binario vuoto</li>
 *   <li>{@code CapacitaSuperataException} la quantità da caricare è superiore della capacità del binario</li>
 *   <li>{@code ProdottoDiversoException} tentativo di caricare un prodotto diverso da quello del binario</li>
 *   <li>{@code TagliaNonCompatibileException} la taglia del prodotto è diversa da quella del binario</li>
 * </ul>
 *
 * Eccezioni relative al Distributore automatico:
 * <ul>
 *   <li>{@code SlotInesistenteException} il binario richiesto non esiste</li>
 *   <li>{@code PagamentoInsufficienteException} il pagamento è insufficiente </li>
 *   <li>{@code RestoNonDisponibileException} non è possibile erogare il resto</li>
 * </ul>
 */
package macchinette.eccezioni;
