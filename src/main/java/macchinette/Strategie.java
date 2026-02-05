package macchinette;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

/**
 * Classe che fornisce strategie per il calcolo del resto:
 * <ul>
 *   <li> massimo: usa per prime le monete di valore maggiore.</li>
 *   <li> minimo: usa per prime le monete di valore minore.</li>
 *   <li> personale: preserva le monete da 1 e 2 centesimi; se il resto
 *       non è componibile senza queste monete, riprova consentendole.</li>
 * </ul>
 */
public class Strategie {

    /**
     * Costruttore privato
     */
    private Strategie() {
    }

    /** Strategia che usa prima le monete di valore maggiore. */
    public static final StrategiaResto massimo = new StrategiaResto() {
        @Override
        public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
                throws ValoreInsufficienteException, ComposizioneInsufficienteException {
            return calcolaRestoInterno(restoDaDare, disponibilita, true, false);
        }
    };

    /** Strategia che usa prima le monete di valore minore. */
    public static final StrategiaResto minimo = new StrategiaResto() {
        @Override
        public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
                throws ValoreInsufficienteException, ComposizioneInsufficienteException {
            return calcolaRestoInterno(restoDaDare, disponibilita, false, false);
        }
    };

    /** Strategia che evita 1 e 2 centesimi quando possibile. */
    public static final StrategiaResto personale = new StrategiaResto() {
        @Override
        public Aggregato calcolaResto(Importo restoDaDare, Aggregato disponibilita)
                throws ValoreInsufficienteException, ComposizioneInsufficienteException {
            try {
                return calcolaRestoInterno(restoDaDare, disponibilita, true, true);
            } catch (ComposizioneInsufficienteException e) {
                return calcolaRestoInterno(restoDaDare, disponibilita, true, false);
            }
        }
    };

    /**
     * Calcola il resto usando l'ordine e i vincoli specificati.
     *
     * @param restoDaDare, importo del resto da restituire, non nullo.
     * @param disponibilita, aggregato di monete disponibili per comporre il resto, non nullo.
     * @param ordineDecrescente {@code true} per usare prima monete di valore maggiore, {@code false} per minore.
     * @param evitaMonetePiccole {@code true} per evitare 1 e 2 centesimi.
     * @return un Aggregato contenente le monete che compongono  il resto .
     * @throws ValoreInsufficienteException se il resto è maggiore di quanto disponibile.
     * @throws ComposizioneInsufficienteException se il resto non può essere composto.
     */
    private static Aggregato calcolaRestoInterno(Importo restoDaDare, Aggregato disponibilita,
            boolean ordineDecrescente, boolean evitaMonetePiccole)
            throws ValoreInsufficienteException, ComposizioneInsufficienteException {

        if (restoDaDare.compareTo(disponibilita.getValoreTotale()) > 0) {
            throw new ValoreInsufficienteException();
        }

        Aggregato risultato = new Aggregato();
        Importo restoRimanente = restoDaDare;
        Importo zero = Importo.daCentesimi(0);

        Moneta[] monete = Moneta.values();
        int start;
        int end;
        int step;

        if (ordineDecrescente) {
            start = monete.length - 1;
            end = -1;
            step = -1;
        } else {
            start = 0;
            end = monete.length;
            step = 1;
        }

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
