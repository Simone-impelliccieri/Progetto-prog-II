package macchinette;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

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
public class Aggregato {

    /**
     * Campo interno dell'aggregato, composto da una mappa tipo di moneta-quantità posseduta
     */
    private final Map<Moneta, Integer> contenuto;


    /*
    * AF:
    * <ul>
    *   <li>La mappa {@code contenuto} associa a ogni {@code Moneta} la sua quantità nell'aggregato,</li>
    * </ul>
    *
    * RI:
    * <ul>
    *   <li>contenuto != null</li>
    *   <li>nessuna chiave è null in {@code contenuto}</li>
    *   <li>tutte le quantità sono interi positivi.</li>
    * </ul>
    */

    /** 
     * 
     * Costruttore, inizializza l'aggregato vuoto.
     * 
     */
    public Aggregato() {
        this.contenuto = new EnumMap<>(Moneta.class);
    }

    /**
     * Aggiunge {@code quantita} monete del tipo {@code m} all'aggregato.
     *
     * @param m, la moneta da aggiungere, non null.
     * @param quantita, numero di monete da aggiungere, deve essere positivo.
     * @throws NullPointerException se {@code m} è null.
     * @throws IllegalArgumentException se {@code quantita} è minore o uguale a 0.
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

    /**
     * Aggiunge all'aggregato  tutte le monete contenute in {@code altro}.
     *
     * Se {@code altro} è null, l'operazione non ha effetto.
     *
     * @param altro, aggregato da aggiungere può essere null.
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


    /**
     * Rimuove dall'aggregato tutte le monete contenute nell'Aggregato {@code daRimuovere}.
     *
     * La rimozione può fallire per due motivi :
     * <ul>
     *   <li>il valore totale di {@code daRimuovere} supera quello dell'aggregato corrente ({@code ValoreInsufficienteException})</li>
     *   <li>il valore totale è sufficiente ma non ci sono abbastanza monete di quello specifico tipo ({@code ComposizioneInsufficienteException})</li>
     * </ul>
     *
     * @param daRimuovere, aggregato da rimuovere, non null.
     * @throws NullPointerException se {@code daRimuovere} è null
     * @throws ValoreInsufficienteException se il valore totale da rimuovere supera il valore totale.
     * @throws ComposizioneInsufficienteException se la composizione non consente la rimozione delle monete.
     */
    public void rimuovi(Aggregato daRimuovere) throws ValoreInsufficienteException, ComposizioneInsufficienteException {
        Objects.requireNonNull(daRimuovere, "Aggregato null");

        if (this.getValoreTotale().compareTo(daRimuovere.getValoreTotale()) < 0) {
            throw new ValoreInsufficienteException();
        }

        for (Map.Entry<Moneta, Integer> e : daRimuovere.contenuto.entrySet()) {
            if (this.getQuantitaMoneta(e.getKey()) < e.getValue()) {
                throw new ComposizioneInsufficienteException();
            }
        }

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


    /**
     * Restituisce la quantità di un tipo specifico di moneta {@code m}.
     * 
     *
     * @param m, la moneta di cui si vuole conoscere la quantità
     * @return la quantità (0 se {@code m} non è presente nell'aggregato).
     */
    int getQuantitaMoneta(Moneta m) {

        if (contenuto.containsKey(m)) {
            return contenuto.get(m);
        } else {
            return 0;
        }

    }


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

}