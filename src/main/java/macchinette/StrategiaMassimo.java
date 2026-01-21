package macchinette;

import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

/**
 * Una StrategiaMassimo è una strategia per il calcolo del resto che utilizza
 * per prime le monete di valore maggiore.
 *
 */
public class StrategiaMassimo implements StrategiaResto {

	/**
	 * Costruisce Strategia massimo
	 */
	public StrategiaMassimo() {
	}

	/**
	 * Calcola il resto utilizzando per prime le monete di valore maggiore.
	
	 *
	 * @param restoDaDare, importo del resto da restituire, non nullo.
	 * @param disponibilita, aggregato di monete disponibili per comporre il resto, non nullo.
	 * @return un Aggregato contenente le monete che compongono il resto richiesto.
	 * @throws NullPointerException se {@code restoDaDare} o {@code disponibilita} sono nulli.
	 * @throws ValoreInsufficienteException se il valore totale di {@code disponibilita} è inferiore a {@code restoDaDare}.
	 * @throws ComposizioneInsufficienteException se pur avendo valore sufficiente non è possibile comporre il resto esatto.
	 */
	@Override
	public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
			throws ValoreInsufficienteException, ComposizioneInsufficienteException {
		Objects.requireNonNull(restoDaDare, "restoDaDare nullo");
		Objects.requireNonNull(disponibilita, "disponibilita nulla");

		if (disponibilita.getValoreTotale().compareTo(restoDaDare) < 0) {
			throw new ValoreInsufficienteException();
		}

		Importo zero = Importo.daCentesimi(0);
		Importo restoRimanente = restoDaDare;
		Aggregato risultato = new Aggregato();

		Moneta[] monete = Moneta.values();
		for (int indice = monete.length - 1; indice >= 0; indice--) {
			if (restoRimanente.compareTo(zero) == 0) {
				break;
			}

			Moneta moneta = monete[indice];
			int massimoPerValore = restoRimanente.divIntera(moneta.getValore());
			int disponibili = disponibilita.getQuantitaMoneta(moneta);
			int daUsare = Math.min(massimoPerValore, disponibili);

			if (daUsare > 0) {
				risultato.aggiungi(moneta, daUsare);
				restoRimanente = restoRimanente.sottrai(moneta.getValore().moltiplica(daUsare));
			}
		}

		if (restoRimanente.compareTo(zero) != 0) {
			throw new ComposizioneInsufficienteException();
		}

		return risultato;
	}

}
