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
import macchinette.StrategiaMassimo;
import macchinette.StrategiaMinimo;
import macchinette.StrategiaResto;
import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

import java.util.Scanner;

public class CalcolaResti {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            if (args == null || args.length < 2) {
                return;
            }

            if (args[0] == null) {
                return;
            }

            String lettera = args[0].trim();

            if (lettera.length() != 1) {
                return;
            }

            char tipo = lettera.charAt(0);
            StrategiaResto strategia;

            if (tipo == 'H') {
                strategia = new StrategiaMassimo();
            } else if (tipo == 'L') {
                strategia = new StrategiaMinimo();
            } else {
                return;
            }

            Importo restoDaDare;
            try {
                restoDaDare = Importo.daStringa(args[1]);
            } catch (IllegalArgumentException e) {
                return;
            }

            while (scanner.hasNextLine()) {
                String riga = scanner.nextLine();
                if (riga == null) {
                    continue;
                }
                String rigaPulita = riga.trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                Aggregato disponibilita;
                try {
                    disponibilita = Aggregato.daStringa(rigaPulita);
                } catch (IllegalArgumentException e) {
                    disponibilita = null;
                }
                if (disponibilita == null) {
                    System.out.println("invalid");
                    continue;
                }

                if (disponibilita.getValoreTotale().compareTo(restoDaDare) < 0) {
                    System.out.println("value");
                    continue;
                }

                try {
                    Aggregato resto = strategia.calcolaResto(restoDaDare, disponibilita);
                    System.out.println(resto);
                } catch (ValoreInsufficienteException e) {
                    System.out.println("value");
                } catch (ComposizioneInsufficienteException e) {
                    System.out.println("change");
                }
            }

        } catch (Exception e) {
            System.err.println("Errore: " + e.getMessage());

        }
    }

}
