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

import java.util.Scanner;

import macchinette.Binario;
import macchinette.Prodotto;
import macchinette.Taglia;
import macchinette.eccezioni.CapacitaSuperataException;
import macchinette.eccezioni.ProdottoDiversoException;
import macchinette.eccezioni.TagliaNonCompatibileException;
import macchinette.util.GestioneClient;

public class CaricaBinario {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            if (args == null || args.length < 2) {
                return;
            }

            Integer capacita = GestioneClient.intero(args[0]);
            if (capacita == null) {
                return;
            }

            Taglia taglia = GestioneClient.taglia(args[1]);
            if (taglia == null) {
                return;
            }

            Binario binario = new Binario(taglia, capacita);
            System.out.println(binario);

            while (scanner.hasNextLine()) {
                String riga = scanner.nextLine();
                if (riga == null) {
                    continue;
                }
                String rigaPulita = riga.trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                int indiceVirgola = rigaPulita.indexOf(',');
                if (indiceVirgola < 0) {
                    System.out.println("invalid");
                    continue;
                }

                String quantitaTesto = rigaPulita.substring(0, indiceVirgola).trim();
                String descrizioneProdotto = rigaPulita.substring(indiceVirgola + 1).trim();

                Integer quantita = GestioneClient.intero(quantitaTesto);
                if (quantita == null) {
                    System.out.println("invalid");
                    continue;
                }

                Prodotto prodotto = GestioneClient.prodotto(descrizioneProdotto);
                if (prodotto == null) {
                    System.out.println("invalid");
                    continue;
                }

                try {
                    binario.carica(prodotto, quantita);
                    System.out.println(binario);
                } catch (ProdottoDiversoException e) {
                    System.out.println("item");
                } catch (CapacitaSuperataException e) {
                    System.out.println("capacity");
                } catch (TagliaNonCompatibileException e) {
                    System.out.println("size");
                }
            }

        } catch (Exception e) {
        }
    }
}
