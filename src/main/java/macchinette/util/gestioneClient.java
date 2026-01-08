package macchinette.util;

import java.util.ArrayList;
import java.util.List;

import macchinette.Aggregato;
import macchinette.Binario;
import macchinette.Prodotto;
import macchinette.Taglia;

public final class GestioneClient {

    private GestioneClient() {
    }

    public static String pulisci(String testo) {
        return testo == null ? "" : testo.trim();
    }

    public static Integer intero(String testo) {
        try {
            return Integer.parseInt(pulisci(testo));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Taglia taglia(String testo) {
        return Taglia.daStringa(pulisci(testo)).orElse(null);
    }

    public static Prodotto prodotto(String testo) {
        try {
            return Prodotto.daStringa(pulisci(testo));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static Aggregato aggregato(String testo) {
        try {
            return Aggregato.daStringa(pulisci(testo));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static List<Binario> binariDaRiga(String riga) {
        if (riga == null || riga.trim().isEmpty()) {
            return null;
        }

        List<Binario> binari = new ArrayList<>();

        for (String descrizioneBinario : riga.split(",")) {
            if (descrizioneBinario == null) {
                continue;
            }
            String descrizionePulita = descrizioneBinario.trim();
            if (descrizionePulita.isEmpty()) {
                continue;
            }

            String[] parti = descrizionePulita.split("\\|", -1);
            if (parti.length != 2) {
                return null;
            }

            Integer capacita = intero(parti[0]);
            if (capacita == null) {
                return null;
            }

            Taglia taglia = taglia(parti[1]);
            if (taglia == null) {
                return null;
            }

            binari.add(new Binario(taglia, capacita));
        }

        return binari;
    }
}
