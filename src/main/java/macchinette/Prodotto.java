package macchinette;

import java.util.Objects;

/**
 * Un Prodotto è un'entità immutabile che rappresenta un bene venduto da un distributore.
 *
 * 
 * Ogni Prodotto è caratterizzato da:
 * <ul>
 *   <li>un nome {@code nome}</li>
 *   <li>un prezzo (un importo non negativo){@code prezzo}</li>
 *   <li>una taglia (S, M o L) {@code taglia}</li>
 * </ul>
 * 
 * 
 * Operazioni implementate:
 * <ul>
 *   <li>costruzione di un prodotto a partire da una descrizione testuale</li>
 * </ul>
 *
 * L'ordinamento naturale dei prodotti è dato dall'ordine della tripla: taglia, nome e prezzo.
 *
 * L'uguaglianza di un prodotto è data da nome, prezzo e taglia.
 */
public final class Prodotto implements Comparable<Prodotto> {

    /**
     * Nome del prodotto.
     */
    private final String nome;

    /**
     * Prezzo del prodotto.
     */
    private final Importo prezzo;

    /**
     * Taglia del prodotto.
     */
    private final Taglia taglia;

    /**
     * Costruttore, crea un prodotto a partire da importo taglia e nome.
     *
     * AF:
     * <ul>
     *   <li>l'istanza costruita rappresenta il prodotto di nome {@code nome}, prezzo {@code importo} e taglia {@code taglia}.</li>
     * </ul>
     *
     * RI:
     * <ul>
     *   <li>{@code nome} è una stringa non vuota e senza spazi iniziali o finali</li>
     *   <li>{@code importo} non è nullo</li>
     *   <li>{@code taglia} non è nulla</li>
     * </ul>
     *
     * @param nome, nome del prodotto; non nullo e non vuoto (eventuali spazi iniziali e finali vengono rimossi).
     * @param importo, prezzo del prodotto, non nullo.
     * @param taglia, taglia del prodotto, non nulla.
     * @throws NullPointerException se uno tra {@code nome}, {@code importo} o {@code taglia} è null.
     * @throws IllegalArgumentException se {@code nome.trim()} è vuoto.
     */
    public Prodotto(String nome, Importo importo, Taglia taglia) {
        Objects.requireNonNull(nome, "nome non può essere null");
        Objects.requireNonNull(importo, "prezzo non può essere null");
        Objects.requireNonNull(taglia, "taglia non può essere null");

        String nomePulito = nome.trim();
        if (nomePulito.isEmpty()) {
            throw new IllegalArgumentException("nome non può essere vuoto");
        }

        this.nome = nomePulito;
        this.prezzo = importo;
        this.taglia = taglia;

    }

    /**
     * Restituisce il nome del prodotto.
     *
     * @return il nome del prodotto.
     */
    String getNome() {
        return nome;
    }

    /**
     * Restituisce il prezzo del prodotto.
     *
     * @return il prezzo del prodotto.
     */
    Importo getPrezzo() {
        return prezzo;
    }

    /**
     * Restituisce la taglia del prodotto.
     *
     * @return la taglia del prodotto.
     */
    Taglia getTaglia() {
        return taglia;
    }

    /**
     * Metodo factory statico, costruisce un prodotto a partire da una descrizione di tipo string.
     *
     * Il formato atteso è {@code nome|prezzo|taglia}.
     * La taglia deve essere uno tra i caratteri {@code S}, {@code M} e {@code L}.
     *
     * @param descrizione, descrizione del prodotto.
     * @return il prodotto, corrispondente alla descrizione.
     * @throws IllegalArgumentException se {@code descrizione} è null, se il formato non è valido, se il 
     * prezzo non è un importo valido oppure se la taglia non è valida.
     */
    public static Prodotto daStringa(String descrizione) {

        if (descrizione == null) {
            throw new IllegalArgumentException("descrizione nulla");
        }

        String[] parti = descrizione.split("\\|", -1);

        if (parti.length != 3) {
            throw new IllegalArgumentException("formato prodotto non valido");
        }

        String nome = parti[0].trim();

        Importo prezzo = Importo.daStringa(parti[1].trim());

        Taglia taglia = Taglia.daStringa(parti[2].trim()).orElse(null);

        if (taglia == null) {
            throw new IllegalArgumentException("taglia non valida");
        }

        return new Prodotto(nome, prezzo, taglia);

    }

    @Override
    public int compareTo(Prodotto altro) {
        Objects.requireNonNull(altro, "prodotto non può essere null");

        int confrontoTaglia = this.taglia.compareTo(altro.taglia);
        if (confrontoTaglia != 0) {
            return confrontoTaglia;
        }

        int confrontoNome = this.nome.compareTo(altro.nome);
        if (confrontoNome != 0) {
            return confrontoNome;
        }

        return this.prezzo.compareTo(altro.prezzo);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Prodotto altro)) {
            return false;
        }
        if (nome.equals(altro.nome) && prezzo.equals(altro.prezzo) && taglia == altro.taglia) {
            return true;
        }
        return false;

    }


    @Override
    public int hashCode() {
        return Objects.hash(nome, prezzo, taglia);
    }

    @Override
    public String toString() {
        return "<" + nome + ", " + prezzo + ", " + taglia + ">";
    }
}
