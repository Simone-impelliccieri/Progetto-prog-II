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
import macchinette.eccezioni.*;

public class CaricaBinario {

    public static void main(String[] args) {

        if (args == null || args.length < 2) {
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {

            int capacita;
            try {
                capacita = Integer.parseInt(args[0].trim());
            } catch (NumberFormatException e) {
                return;
            }

            Taglia taglia = Taglia.daStringa(args[1].trim()).orElse(null);
            if (taglia == null) {
                return;
            }

            Binario binario = new Binario(taglia, capacita);
            System.out.println(binario);

            while (scanner.hasNextLine()) {
                String rigaPulita = scanner.nextLine().trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                int indiceVirgola = rigaPulita.indexOf(',');
                if (indiceVirgola < 0) {
                    System.out.println("invalid");
                    continue;
                }

                int quantita;
                Prodotto prodotto;

                try {
                    quantita = Integer.parseInt(rigaPulita.substring(0, indiceVirgola).trim());
                    prodotto = Prodotto.daStringa(rigaPulita.substring(indiceVirgola + 1).trim());
                } catch (IllegalArgumentException e) {
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
        }
    }
}