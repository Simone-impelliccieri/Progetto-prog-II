package macchinette;

import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

/*
 * Strategia "preserva monete rare": tenta di calcolare il resto evitando, se possibile,
 * l'uso delle monete da 1 e 2 centesimi (che considero importanti per resti futuri).
 * Se il resto non è componibile senza quei tagli, riprova consentendoli (fallback).
 */
public final class StrategiaPersonale implements StrategiaResto {

    @Override
    public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
            throws ValoreInsufficienteException, ComposizioneInsufficienteException {
        Objects.requireNonNull(restoDaDare, "restoDaDare nullo");
        Objects.requireNonNull(disponibilita, "disponibilita nulla");

        if (disponibilita.getValoreTotale().compareTo(restoDaDare) < 0) {
            throw new ValoreInsufficienteException();
        }

        // Strategia: prova a non consumare monete "rare" (1c e 2c) se non necessario.
        try {
            return strategiaMassimo(restoDaDare, disponibilita, true);
        } catch (ComposizioneInsufficienteException e) {
            // fallback: se è impossibile senza 1c/2c, allora le permetto.
            return strategiaMassimo(restoDaDare, disponibilita, false);
        }
    }

    private static Aggregato strategiaMassimo(Importo restoDaDare, Aggregato disponibilita, boolean evitaMonetePiccole)
            throws ComposizioneInsufficienteException {
        Importo zero = Importo.daCentesimi(0);
        Importo restoRimanente = restoDaDare;
        Aggregato risultato = new Aggregato();

        Moneta[] monete = Moneta.values();
        for (int indice = monete.length - 1; indice >= 0; indice--) {
            if (restoRimanente.compareTo(zero) == 0) {
                break;
            }

            Moneta moneta = monete[indice];
            if (evitaMonetePiccole && (moneta == Moneta.C01 || moneta == Moneta.C02)) {
                continue;
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
