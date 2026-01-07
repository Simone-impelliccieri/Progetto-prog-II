package macchinette;

import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

public class StrategiaMassimo implements StrategiaResto {

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

		//cicla al contrario sui valori moneta
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
