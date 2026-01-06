package macchinette;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

public class Aggregato implements Iterable<Moneta> {

    //espressione regolare che serve per daStringa (10 x .20 --> 10 monete da 20c)
    private static final Pattern espressione = Pattern.compile("\\s*(\\d+)\\s*x\\s*(.+)\\s*");

    //L'aggregato è rappresentato come mappa moneta-intero
    private final Map<Moneta, Integer> contenuto;

    //costruttore che inizializza la struttura interna
    public Aggregato() {
        this.contenuto = new EnumMap<>(Moneta.class); //dici che monete possono essere solo di tipo moneta
    }


    // NON SO SE FARLO PRIVATE O PUBLIC
    // aggiunge all'aggregato una moneta quantità volte
    private void aggiungi(Moneta m, int quantita) {
        Objects.requireNonNull(m, "Moneta nulla");
        if (quantita <= 0)
            throw new IllegalArgumentException("Quantità non positiva");

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
    public void rimuovi(Aggregato daRimuovere) {
        Objects.requireNonNull(daRimuovere, "Aggregato null");

        // 1. Check Valore posseduto > togliere 
        if (this.getValoreTotale().compareTo(daRimuovere.getValoreTotale()) < 0) {
            throw new ValoreInsufficienteException();
        }

        // 2. Check Composizione , ovvero se
        for (Map.Entry<Moneta, Integer> e : daRimuovere.contenuto.entrySet()) {
            if (this.getQuantitaMoneta(e.getKey()) < e.getValue()) {
                throw new ComposizioneInsufficienteException();
            }
        }

        // 3. Commit
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

    // getter valore totale
    public Importo getValoreTotale() {
        Importo tot = Importo.daCentesimi(0);
        for (Map.Entry<Moneta, Integer> e : contenuto.entrySet()) {
            tot = tot.somma(e.getKey().getValore().moltiplica(e.getValue()));
        }
        return tot;
    }

    // getter quantità per moneta
    public int getQuantitaMoneta(Moneta m) {

        if (contenuto.containsKey(m)) {
            return contenuto.get(m);
        } else {
            return 0;
        }

    }

    // metodo statico per fare da stringa a aggregato
    public static Aggregato daStringa(String descrizione) {
        Aggregato agg = new Aggregato();
        if (descrizione == null || descrizione.isBlank())
            return agg;

        for (String pezzo : descrizione.split(",")) {
            if (pezzo.isBlank())
                continue;

            Matcher m = espressione.matcher(pezzo);
            if (!m.matches())
                throw new IllegalArgumentException("Formato non valido: " + pezzo);

            Moneta moneta = Moneta.daStringa(m.group(2).trim())
                    .orElseThrow(() -> new IllegalArgumentException("Moneta non valida"));

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

    //equals
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Aggregato a))
            return false;
        return Objects.equals(contenuto, a.contenuto);
    }

    //hashcode
    @Override
    public int hashCode() {
        return Objects.hashCode(contenuto);
    }
}