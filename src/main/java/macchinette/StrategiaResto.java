package macchinette;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

/**
 * Una StrategiaResto definisce un metodo per calcolare il resto da erogare.
 *
 *
 * Non è detto che sia sempre possibile dare il resto: non solo perché il valore totale del fondo cassa 
 * potrebbe essere insufficiente, ma anche perché pur essendolo, 
 * a seconda della strategia scelta, potrebbe risultare impossibile determinare 
 * un aggregato di monete che consenta di raggiungere esattamente l'importo del resto.
 * 
 */
public interface StrategiaResto {

	/**
	 * Calcola il resto da erogare a partire dall'importo richiesto e dalla disponibilità di monete.
	 *
	 * L'implementazione non modifica {@code disponibilita}.
	 * 
	 * @param restoDaDare, importo del resto da restituire, non nullo.
	 * @param disponibilita, aggregato di monete disponibili per comporre il resto, non nullo.
	 * @return un Aggregato contenente le monete che compongono  il resto .
	 * @throws NullPointerException se {@code restoDaDare} o {@code disponibilita} sono nulli.
	 * @throws ValoreInsufficienteException se il valore totale di {@code disponibilita} è inferiore a {@code restoDaDare}.
	 * @throws ComposizioneInsufficienteException se pur avendo valore sufficiente non è possibile comporre il resto esatto.
	 */
	Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
			throws ValoreInsufficienteException, ComposizioneInsufficienteException;

}
