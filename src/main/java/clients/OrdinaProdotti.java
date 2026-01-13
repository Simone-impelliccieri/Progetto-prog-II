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
import java.util.ArrayList;
import java.util.Collections;

import macchinette.Prodotto;

public class OrdinaProdotti {

    public static void main(String[] args) {

        try (Scanner sca = new Scanner(System.in)) {

            ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();

            while (sca.hasNextLine()) {

                String riga = sca.nextLine();
                String rigaPulita = riga.trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                prodotti.add(Prodotto.daStringa(rigaPulita));

            }

            Collections.sort(prodotti);

            for (Prodotto prodotto : prodotti) {

                System.out.println(prodotto);

            }

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());

        }

    }

}
