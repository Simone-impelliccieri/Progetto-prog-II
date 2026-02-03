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
along with this file.  If not, see <https://www.gnu.org/licenses/>..
*/
package clients;

import macchinette.Aggregato;
import macchinette.Importo;
import macchinette.Parser;
import macchinette.StrategiaResto;
import macchinette.Strategie;
import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

import java.util.Scanner;

public class CalcolaResti {

    public static void main(String[] args) {

        if (args == null || args.length < 2 || args[0].length() != 1) {
            return;
        }

        StrategiaResto strategia;
        if (args[0].equals("H")) {
            strategia = new Strategie('H');
        } else if (args[0].equals("L")) {
            strategia = new Strategie('L');
        } else {
            return;
        }

        Importo restoDaDare;
        try {
            restoDaDare = Parser.daStringaImporto(args[1]);
        } catch (IllegalArgumentException e) {
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String rigaPulita = scanner.nextLine().trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                Aggregato disponibilita;
                try {
                    disponibilita = Parser.daStringaAggregato(rigaPulita);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                try {
                    System.out.println(strategia.calcolaResto(restoDaDare, disponibilita));
                } catch (ValoreInsufficienteException e) {
                    System.out.println("insufficient-value");
                } catch (ComposizioneInsufficienteException e) {
                    System.out.println("change-not-possible");
                }
            }
        }
    }
}