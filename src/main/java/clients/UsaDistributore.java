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

import java.util.List;
import java.util.Iterator;
import java.util.Scanner;

import macchinette.Aggregato;
import macchinette.Binario;
import macchinette.DistributoreAutomatico;
import macchinette.Prodotto;
import macchinette.StrategiaMassimo;
import macchinette.StrategiaMinimo;
import macchinette.StrategiaResto;
import macchinette.eccezioni.BinarioVuotoException;
import macchinette.eccezioni.PagamentoInsufficienteException;
import macchinette.eccezioni.RestoNonDisponibileException;
import macchinette.eccezioni.SlotInesistenteException;
import macchinette.util.GestioneClient;

public class UsaDistributore {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            if (!scanner.hasNextLine()) {
                return;
            }

            String rigaBinari = scanner.nextLine();
            List<Binario> binari = GestioneClient.binariDaRiga(rigaBinari);
            if (binari == null) {
                return;
            }

            if (!scanner.hasNextLine()) {
                return;
            }

            String rigaFondoCassa = scanner.nextLine();
            Aggregato fondoCassa = GestioneClient.aggregato(rigaFondoCassa);
            if (fondoCassa == null) {
                return;
            }

            if (!scanner.hasNextLine()) {
                return;
            }

            String rigaStrategia = scanner.nextLine();
            if (rigaStrategia == null) {
                return;
            }
            String strategiaPulita = rigaStrategia.trim();
            if (strategiaPulita.length() != 1) {
                return;
            }

            char tipoStrategia = strategiaPulita.charAt(0);
            StrategiaResto strategiaResto;
            if (tipoStrategia == 'H') {
                strategiaResto = new StrategiaMassimo();
            } else if (tipoStrategia == 'L') {
                strategiaResto = new StrategiaMinimo();
            } else {
                return;
            }
            DistributoreAutomatico distributore = new DistributoreAutomatico(binari, fondoCassa, strategiaResto);

            while (scanner.hasNextLine()) {
                String riga = scanner.nextLine();
                if (riga == null) {
                    continue;
                }
                String rigaPulita = riga.trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                char comando = rigaPulita.charAt(0);

                if (comando == '?') {
                    if (rigaPulita.length() != 1) {
                        continue;
                    }
                    Iterator<String> iteratoreStato = distributore.statoProdotti();
                    while (iteratoreStato.hasNext()) {
                        System.out.println(iteratoreStato.next());
                    }
                    continue;
                }

                if (comando != '+' && comando != '-') {
                    continue;
                }

                String argomenti = rigaPulita.substring(1).trim();
                int indiceVirgola = argomenti.indexOf(',');
                if (indiceVirgola < 0) {
                    continue;
                }

                String primo = argomenti.substring(0, indiceVirgola).trim();
                String secondo = argomenti.substring(indiceVirgola + 1).trim();

                if (comando == '+') {
                    Integer quantita = GestioneClient.intero(primo);
                    if (quantita == null) {
                        continue;
                    }

                    Prodotto prodotto = GestioneClient.prodotto(secondo);
                    if (prodotto == null) {
                        continue;
                    }

                    try {
                        int avanzati = distributore.carica(prodotto, quantita);
                        System.out.println("+ " + avanzati);
                    } catch (IllegalArgumentException e) {
                        continue;
                    }

                } else {
                    Integer indice = GestioneClient.intero(primo);
                    if (indice == null) {
                        continue;
                    }

                    Aggregato pagamento = GestioneClient.aggregato(secondo);
                    if (pagamento == null) {
                        continue;
                    }

                    try {
                        Aggregato resto = distributore.eroga(indice, pagamento);
                        System.out.println("- " + resto);
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

        } catch (Exception e) {
        }
    }
}
