package macchinette;

import java.util.Objects;

//liksov approved

public final class Prodotto implements Comparable<Prodotto> {

    private final String nome;
    private final Importo prezzo;
    private final Taglia taglia;

    //costruttore normale
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

    //forse da togliere questo getter e restituire OGGETTO e non String
    public String getNome() {
        return nome;
    }

    // questi due get vanno bene perchè restituiscono OGGETTO
    public Importo getPrezzo() {
        return prezzo;
    }

    //viene usato da binario carica , rendere package private
    public Taglia getTaglia() {
        return taglia;
    }

    // Metodo factory statico per il parsing da file (formato: nome|prezzo|taglia) per client
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

    // ordinamento taglia, nome prezzo
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

    //uguali se hanno nome prezzo e taglia uguali
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

    //hashcode
    @Override
    public int hashCode() {
        return Objects.hash(nome, prezzo, taglia);
    }

    //tostring
    @Override
    public String toString() {
        return "<" + nome + ", " + prezzo + ", " + taglia + ">";
    }
}
