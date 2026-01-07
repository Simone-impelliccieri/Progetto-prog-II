package macchinette;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

public interface StrategiaResto {

	Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
			throws ValoreInsufficienteException, ComposizioneInsufficienteException;

}
