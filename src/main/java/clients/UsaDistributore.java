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
                    Taglia taglia = Taglia.daStringa(parti[1].trim()).orElse(null);
                    if (taglia == null) {
                        return;
                    }
                    binari.add(new Binario(taglia, capacita));
                } catch (Exception e) {
                    return;
                }
            }

            if (!scanner.hasNextLine()) {
                return;
            }
            Aggregato fondoCassa;
            try {
                fondoCassa = Aggregato.daStringa(scanner.nextLine().trim());
            } catch (Exception e) {
                return;
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
                strategiaResto = new StrategiaMassimo();
            } else if (tipo == 'L') {
                strategiaResto = new StrategiaMinimo();
            } else if (tipo == 'P') {
                strategiaResto = new StrategiaPersonale();
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
                int virgola = argomenti.indexOf(',');
                if (virgola < 0) {
                    continue;
                }

                String primo = argomenti.substring(0, virgola).trim();
                String secondo = argomenti.substring(virgola + 1).trim();

                try {
                    if (comando == '+') {
                        int quantita = Integer.parseInt(primo);
                        Prodotto prodotto = Prodotto.daStringa(secondo);
                        System.out.println("+ " + distributore.carica(prodotto, quantita));
                    } else {
                        int indice = Integer.parseInt(primo);
                        Aggregato pagamento = Aggregato.daStringa(secondo);
                        System.out.println("- " + distributore.eroga(indice, pagamento));
                    }
                } catch (SlotInesistenteException e) {
                    System.out.println("- slot");
                } catch (PagamentoInsufficienteException e) {
                    System.out.println("- value");
                } catch (BinarioVuotoException e) {
                    System.out.println("- empty");
                } catch (RestoNonDisponibileException e) {
                    System.out.println("- change");
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());
        }
    }
}