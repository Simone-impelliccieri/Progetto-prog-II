package macchinette;

import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

public final class StrategiaMinimo implements StrategiaResto {

	@Override
	public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
			throws ValoreInsufficienteException, ComposizioneInsufficienteException {
		Objects.requireNonNull(restoDaDare, "il resto da dare non può essere nullo");
		Objects.requireNonNull(disponibilita, "disponibilita non può essere nulla");

		if (disponibilita.getValoreTotale().compareTo(restoDaDare) < 0) {
			throw new ValoreInsufficienteException();
		}

		Importo zero = Importo.daCentesimi(0);
		Importo restoRimanente = restoDaDare;
		Aggregato risultato = new Aggregato();

		// cicla tra i valori di moneta nell'ordine in cui li ho scritti
		for (Moneta moneta : Moneta.values()) {
			if (restoRimanente.compareTo(zero) == 0) {
				break;
			}

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
