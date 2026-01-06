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

import macchinette.Moneta;

import java.util.Optional;
import java.util.Scanner;

public class RiconosciMonete {

    public static void main(String[] args) {

        try (Scanner sca = new Scanner(System.in)) {

            while (sca.hasNextLine()) {

                Optional<Moneta> moneta = Moneta.daStringa(sca.nextLine());
                if (moneta.isPresent()) {
                    System.out.println(moneta.get());
                } else {
                    System.out.println("invalid");
                }

            }

        } catch (Exception e) {
            // non emettere output extra: i test confrontano l'output esatto
        }

    }

}
