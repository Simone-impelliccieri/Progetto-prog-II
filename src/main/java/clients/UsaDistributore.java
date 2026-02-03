/*

Copyright 2024 Massimo Santini

This file is part of "Programmazione 2 @ UniMI" teaching material.

This is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This material is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this file.  If not, see <https://www.gnu.org/licenses/>.

*/
package clients;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import macchinette.*;
import macchinette.eccezioni.*;

public class UsaDistributore {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            if (!scanner.hasNextLine()) {
                return;
            }
            String rigaBinari = scanner.nextLine();
            if (rigaBinari == null || rigaBinari.trim().isEmpty()) {
                return;
            }

            List<Binario> binari = new ArrayList<>();
            for (String descrizione : rigaBinari.split(",")) {
                String[] parti = descrizione.trim().split("\\|");
                if (parti.length != 2) {
                    return;
                }
                try {
                    int capacita = Integer.parseInt(parti[0].trim());
                    Taglia taglia = parser.daStringaTaglia(parti[1].trim());
                    binari.add(new Binario(taglia, capacita));
                } catch (IllegalArgumentException e) {
                    return;
                }
            }

            if (!scanner.hasNextLine()) {
                return;
            }

            Aggregato fondoCassa = new Aggregato();

            String riga = scanner.nextLine().trim();

            while (!riga.equals(".")) {
                String[] parti = riga.split("\\s+");

                if (parti.length > 1) {
                    try {
                        int quantita = Integer.parseInt(parti[0]);
                        Moneta moneta = parser.daStringaMoneta(parti[1]);
                        fondoCassa.aggiungi(moneta, quantita);
                    } catch (Exception e) {
                    }
                }
                riga = scanner.nextLine().trim();
            }

            if (!scanner.hasNextLine())
                return;
            String rigaStrategia = scanner.nextLine().trim();
            if (rigaStrategia.length() != 1) {
                return;
            }

            StrategiaResto strategiaResto;
            char tipo = rigaStrategia.charAt(0);
            if (tipo == 'H') {
                strategiaResto = new Strategie('H');
            } else if (tipo == 'L') {
                strategiaResto = new Strategie('L');
            } else if (tipo == 'P') {
                strategiaResto = new Strategie('P');
            } else {
                return;
            }

            DistributoreAutomatico distributore = new DistributoreAutomatico(binari, fondoCassa, strategiaResto);

            while (scanner.hasNextLine()) {
                String rigaPulita = scanner.nextLine().trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                char comando = rigaPulita.charAt(0);

                if (comando == '?') {
                    if (rigaPulita.length() == 1) {
                        Iterator<String> it = distributore.statoProdotti();
                        while (it.hasNext())
                            System.out.println(it.next());
                    }
                    continue;
                }

                if (comando != '+' && comando != '-') {
                    continue;
                }

                String argomenti = rigaPulita.substring(1).trim();
                int sep = argomenti.indexOf('!');
                if (sep < 0) {
                    continue;
                }
                String primo = argomenti.substring(0, sep).trim();
                String secondo = argomenti.substring(sep + 1).trim();

                if (comando == '+') {
                    int quantita;
                    Prodotto prodotto;
                    try {
                        quantita = Integer.parseInt(primo);
                        prodotto = parser.daStringaProdotto(secondo);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }
                    System.out.println("+ " + distributore.carica(prodotto, quantita));

                } else {
                    int indice;
                    Aggregato pagamento;
                    try {
                        indice = Integer.parseInt(primo);
                        pagamento = parser.daStringaAggregato(secondo);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                    try {
                        System.out.println("- " + distributore.eroga(indice, pagamento));
                    } catch (SlotInesistenteException e) {
                        System.out.println("- slot");
                    } catch (PagamentoInsufficienteException e) {
                        System.out.println("- value");
                    } catch (BinarioVuotoException e) {
                        System.out.println("- empty");
                    } catch (RestoNonDisponibileException e) {
                        System.out.println("- change");
                    }
                }
            }
        }
    }
}