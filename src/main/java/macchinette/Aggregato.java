package macchinette;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

//liskov approved

/**
 * Un Aggregato è un multi-insieme di monete.
 * È utile per descrivere pagamenti, resti e fondo cassa del distributore.
 *
 * Un aggregato non ha un ordinamento naturale.
 * 
 * Un aggregato è un entità mutabile perchè i metodi aggiungi(Moneta, int),
 *  aggiungi(Aggregato) e  rimuovi(Aggregato) modificano lo stato dell'istanza.
 *
 * Operazioni implementate:
 * <ul>
 *   <li>aggiungere monete (di un dato tipo o a partire da un altro aggregato)</li>
 *   <li>rimuovere un aggregato</li>
 *   <li>calcolare il valore totale come Importo</li>
 *   <li>restituisce valore totale Aggregato </li>
 * </ul>
 *
 */
public final class Aggregato {

    //L'aggregato è rappresentato come mappa moneta-intero. è final in modo che il riferimento alla mappa 
    // non può essere modificato
    /**
     * Campo interno dell'aggregato, composto da una mappa tipo di moneta-quantità posseduta
     */
    private final Map<Moneta, Integer> contenuto;

    //costruttore che inizializza la struttura interna
    /**
     * Costruttore, inizializza l'aggregato vuoto.
     *
     * AF:
     * <ul>
    *   <li>La mappa {@code contentuto} associa a ogni {@code Moneta} la sua quantità nell'aggregato,</li>
     *   <li>una moneta non presente in {@code contenuto} ha quantità 0.</li>
     * </ul>
     *
     * RI:
     * <ul>
     *   <li>contenuto != null</li>
     *   <li>nessuna chiave è null in {@code contenuto}</li>
     *   <li>tutte le quantità sono interi positivi.</li>
     * </ul>
     */
    public Aggregato() {
        this.contenuto = new EnumMap<>(Moneta.class); //dice che monete possono essere solo di tipo moneta
    }

    // aggiunge all'aggregato una moneta quantità volte
    /**
     * Aggiunge {@code quantita} monete del tipo {@code m} all'aggregato.
     *
     * @param m,  la moneta da aggiungere ,non null.
     * @param quantita numero di monete da aggiungere, deve essere positivo.
     * @throws NullPointerException se {@code m} è null.
     * @throws IllegalArgumentException se  quantita <= 0.
     */
    public void aggiungi(Moneta m, int quantita) {
        Objects.requireNonNull(m, "Moneta non può essere null");

        if (quantita <= 0) {
            throw new IllegalArgumentException("Quantità non positiva");
        }

        if (contenuto.containsKey(m)) {
            int vecchie = contenuto.get(m);
            contenuto.put(m, vecchie + quantita);
        } else {
            contenuto.put(m, quantita);
        }
    }

    // aggiungi all'aggregato un'altro aggregato(scorrendo tutti i suoi contenuti)
    /**
     * Aggiunge all'aggregato  tutte le monete contenute in {@code altro}.
     *
     * Se {@code altro} è null, l'operazione non ha effetto.
     *
     * @param altro aggregato da aggiungere può essere null.
     */
    public void aggiungi(Aggregato altro) {
        if (altro != null) {

            for (Map.Entry<Moneta, Integer> coppia : altro.contenuto.entrySet()) {

                Moneta monetaDaAggiungere = coppia.getKey();
                int quantitaDaAggiungere = coppia.getValue();

                this.aggiungi(monetaDaAggiungere, quantitaDaAggiungere);
            }
        }
    }

    //rimuove un aggregato (eccezioni esterne, 2)
    /**
     * Rimuove dall'aggregato tutte le monete contenute nell'Aggregato {@code daRimuovere}.
     *
     * La rimozione può fallire per due motivi :
     * <ul>
     *   <li>il valore totale di {@code daRimuovere} supera quello dell'aggregato corrente ({@code ValoreInsufficienteException})</li>
     *   <li>il valore totale è sufficiente ma non ci sono abbastanza monete di quello specifico tipo ({@code ComposizioneInsufficienteException})</li>
     * </ul>
     *
     * @param daRimuovere aggregato da rimuovere, non null.
     * @throws NullPointerException se {@code daRimuovere} è null
     * @throws ValoreInsufficienteException se il valore totale da rimuovere supera il valore totale.
     * @throws ComposizioneInsufficienteException se la composizione non consente la rimozione delle monete.
     */
    public void rimuovi(Aggregato daRimuovere) throws ValoreInsufficienteException, ComposizioneInsufficienteException {
        Objects.requireNonNull(daRimuovere, "Aggregato null");

        // 1. Check Valore posseduto > togliere 
        if (this.getValoreTotale().compareTo(daRimuovere.getValoreTotale()) < 0) {
            throw new ValoreInsufficienteException();
        }
        // 2. Verifica  che ogni specifico taglio di moneta da rimuovere sia fisicamente sufficiente, 
        for (Map.Entry<Moneta, Integer> e : daRimuovere.contenuto.entrySet()) {
            if (this.getQuantitaMoneta(e.getKey()) < e.getValue()) {
                throw new ComposizioneInsufficienteException();
            }
        }

        // 3. esegui
        for (Map.Entry<Moneta, Integer> entry : daRimuovere.contenuto.entrySet()) {

            Moneta moneta = entry.getKey();
            int qtaDaTogliere = entry.getValue();

            Integer qtaPresente = this.contenuto.get(moneta);

            int residuo = qtaPresente - qtaDaTogliere;

            if (residuo == 0) {
                this.contenuto.remove(moneta);
            } else {
                this.contenuto.put(moneta, residuo);
            }
        }
    }

    // getter valore totale serve in distributore e client
    /**
     * Restituisce il valore totale dell'aggregato.
     *
     * @return il valore totale come Importo
     */
    public Importo getValoreTotale() {
        Importo tot = Importo.daCentesimi(0);
        for (Map.Entry<Moneta, Integer> e : contenuto.entrySet()) {
            tot = tot.somma(e.getKey().getValore().moltiplica(e.getValue()));
        }
        return tot;
    }

    // getter quantità per moneta serve in strategia
    /**
     * Restituisce la quantità di un tipo specifico di moneta {@code m}.
     * 
     *
     * @param m la moneta di cui si vuole conoscere la quantità
     * @return la quantità (0 se {@code m} non è presente nell'aggregato).
     */
    int getQuantitaMoneta(Moneta m) {

        if (contenuto.containsKey(m)) {
            return contenuto.get(m);
        } else {
            return 0;
        }

    }

    // per fare da stringa a aggregato. metodo factory statico
    /**
     * Metodo factory statico, legge una stringa e restituisce l'istanza di aggregato corrispondente
     *
     *
     * Se {@code descrizione} è null o vuota viene restituito l'aggregato vuoto.
     *
     * @param descrizione ,descrizione testuale dell'aggregato.
     * @return l'aggregato.
     * @throws IllegalArgumentException se il formato non è valido o se una moneta non è riconosciuta.
     */
    public static Aggregato daStringa(String descrizione) {
        Aggregato agg = new Aggregato();
        if (descrizione == null || descrizione.isBlank()) {
            return agg;
        }

        // espressione regolare (10 x .20 --> 10 monete da 20c) per client(forse è meglio spostarla(?))
        Pattern espressione = Pattern.compile("\\s*(\\d+)\\s*x\\s*(.+)\\s*");

        for (String pezzo : descrizione.split(",")) {
            if (pezzo.isBlank()) {
                continue;
            }

            Matcher m = espressione.matcher(pezzo);
            if (!m.matches()) {
                throw new IllegalArgumentException("Formato non valido: " + pezzo);
            }

            String testoMoneta = m.group(2).trim();

            Optional<Moneta> boxMoneta = Moneta.monetaGiusta(testoMoneta);

            if (boxMoneta.isEmpty()) {
                throw new IllegalArgumentException("Moneta non valida");
            }

            Moneta moneta = boxMoneta.get();

            agg.aggiungi(moneta, Integer.parseInt(m.group(1)));
        }
        return agg;
    }

    //toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<");

        boolean primoElemento = true;

        for (Map.Entry<Moneta, Integer> entry : contenuto.entrySet()) {
            Moneta m = entry.getKey();
            int q = entry.getValue();

            if (!primoElemento) {
                sb.append(", ");
            }

            sb.append(q).append(" x ").append(m.toString());

            primoElemento = false;
        }

        sb.append(">");

        return sb.toString();
    }

    // NOTA (per il prof): Aggregato è MUTABILE (aggiungi/rimuovi cambiano lo stato).
    // Seguendo Liskov, per oggetti mutabili l'uguaglianza "per contenuto" è pericolosa:
    // due istanze potrebbero essere uguali in un momento e diverse dopo una mutazione.
    // Inoltre equals/hashCode basati sul contenuto rendono l'oggetto insicuro in HashSet/HashMap
    // (dopo una modifica, l'hash cambia e l'elemento può diventare "irraggiungibile").
    // Per questo Aggregato NON ridefinisce equals/hashCode e usa l'uguaglianza per identità (==).

}