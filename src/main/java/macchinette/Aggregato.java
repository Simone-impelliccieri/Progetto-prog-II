package macchinette;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

//liskov approved

public class Aggregato implements Iterable<Moneta> {

    //L'aggregato è rappresentato come mappa moneta-intero. è final in modo che il riferimento alla mappa 
    // non può essere modificato
    private final Map<Moneta, Integer> contenuto;

    //costruttore che inizializza la struttura interna
    public Aggregato() {
        this.contenuto = new EnumMap<>(Moneta.class); //dice che monete possono essere solo di tipo moneta
    }

    // aggiunge all'aggregato una moneta quantità volte
    public void aggiungi(Moneta m, int quantita) {
        Objects.requireNonNull(m, "Moneta non può essere null");
        Objects.requireNonNull(quantita, "Quantità non può essere null");

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

    // getter valore totale serve in distributore
    public Importo getValoreTotale() {
        Importo tot = Importo.daCentesimi(0);
        for (Map.Entry<Moneta, Integer> e : contenuto.entrySet()) {
            tot = tot.somma(e.getKey().getValore().moltiplica(e.getValue()));
        }
        return tot;
    }

    // getter quantità per moneta serve in strategia
    public int getQuantitaMoneta(Moneta m) {

        if (contenuto.containsKey(m)) {
            return contenuto.get(m);
        } else {
            return 0;
        }

    }

    // per fare da stringa a aggregato. metodo factory statico
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

    //iteratore
    @Override
    public Iterator<Moneta> iterator() {
        return contenuto.keySet().iterator();
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