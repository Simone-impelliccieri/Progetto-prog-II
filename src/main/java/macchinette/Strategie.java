package macchinette;

import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

/**
 * Una Strategie è una strategia per il calcolo del resto parametrizzata dal tipo.
 */
public class Strategie implements StrategiaResto {

    private final char tipo;

    /**
     * Costruisce una strategia del tipo indicato.
     *
     * @param tipo tipo di strategia: 'H' (massimo), 'L' (minimo), 'P' (personale).
     * @throws IllegalArgumentException se {@code tipo} non è tra 'H', 'L', 'P'.
     */
    public Strategie(char tipo) {
        this.tipo = Character.toUpperCase(tipo);
        if (this.tipo != 'H' && this.tipo != 'L' && this.tipo != 'P') {
            throw new IllegalArgumentException("tipo strategia non valido");
        }
    }

    @Override
    public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
            throws ValoreInsufficienteException, ComposizioneInsufficienteException {
        switch (tipo) {
            case 'H':
                return calcolaGreedy(restoDaDare, disponibilita, true, false);
            case 'L':
                return calcolaGreedy(restoDaDare, disponibilita, false, false);
            case 'P':
                try {
                    return calcolaGreedy(restoDaDare, disponibilita, true, true);
                } catch (ComposizioneInsufficienteException e) {
                    return calcolaGreedy(restoDaDare, disponibilita, true, false);
                }
            default:
                throw new IllegalStateException("tipo strategia non gestito");
        }
    }

    private static Aggregato calcolaGreedy(Importo restoDaDare, Aggregato disponibilita, boolean ordineDecrescente,
            boolean evitaMonetePiccole) throws ValoreInsufficienteException, ComposizioneInsufficienteException {
        Objects.requireNonNull(restoDaDare, "restoDaDare nullo");
        Objects.requireNonNull(disponibilita, "disponibilita nulla");

        if (disponibilita.getValoreTotale().compareTo(restoDaDare) < 0) {
            throw new ValoreInsufficienteException();
        }

        Importo zero = Importo.daCentesimi(0);
        Importo restoRimanente = restoDaDare;
        Aggregato risultato = new Aggregato();

        Moneta[] monete = Moneta.values();
        int start = ordineDecrescente ? monete.length - 1 : 0;
        int end = ordineDecrescente ? -1 : monete.length;
        int step = ordineDecrescente ? -1 : 1;

        for (int indice = start; indice != end; indice += step) {
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
