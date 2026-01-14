package macchinette;

import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

/**
 * Una StrategiaPersonale è una strategia per il calcolo del resto che preserva
 * le monete più utili, 1 e 2 centesimi. 
 *
 * Se il resto non è componibile senza quel tipo di monete, riprova consentendoli.
 *
 */
public final class StrategiaPersonale implements StrategiaResto {

    /**
     * Costruisce Strategia personale
     */
    public StrategiaPersonale() {
    }

    /**
     * Calcola il resto preservando le monete da 1 e 2 centesimi quando possibile.
     *
     *
     * @param restoDaDare, importo del resto da restituire, non nullo.
     * @param disponibilita, aggregato di monete disponibili per comporre il resto, non nullo.
     * @return un Aggregato contenente le monete che compongono  il resto richiesto.
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

        try {
            return strategiaMassimo(restoDaDare, disponibilita, true);
        } catch (ComposizioneInsufficienteException e) {
            return strategiaMassimo(restoDaDare, disponibilita, false);
        }
    }

    /**
     * Metodo di aiuto che calcola il resto utilizzando monete in ordine decrescente.
     *
     * @param restoDaDare, importo del resto da restituire.
     * @param disponibilita, aggregato di monete disponibili.
    * @param evitaMonetePiccole, se true esclude le monete da 1 e 2 centesimi.
     * @return un Aggregato contenente le monete che compongono il resto.
     * @throws ComposizioneInsufficienteException se non è possibile comporre il resto esatto.
     */
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
