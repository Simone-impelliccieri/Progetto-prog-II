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

import macchinette.Aggregato;
import macchinette.eccezioni.ComposizioneInsufficienteException;
import macchinette.eccezioni.ValoreInsufficienteException;

import java.util.Scanner;

public class OperazioniAggregati {

    public static void main(String[] args) {

        try (Scanner sca = new Scanner(System.in)) {

            Aggregato aggregatoCorrente = new Aggregato();

            while (sca.hasNextLine()) {

                String riga = sca.nextLine();
                if (riga == null) {
                    continue;
                }
                String rigaPulita = riga.trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                char segno = rigaPulita.charAt(0);
                if (segno != '+' && segno != '-') {
                    System.out.println("invalid");
                    continue;
                }

                String descrizione = rigaPulita.substring(1).trim();

                Aggregato operando;
                try {
                    operando = Aggregato.daStringa(descrizione);
                } catch (IllegalArgumentException e) {
                    System.out.println("invalid");
                    continue;
                }

                if (segno == '+') {
                    aggregatoCorrente.aggiungi(operando);
                    System.out.println(aggregatoCorrente);
                } else {
                    try {
                        aggregatoCorrente.rimuovi(operando);
                        System.out.println(aggregatoCorrente);
                    } catch (ValoreInsufficienteException e) {
                        System.out.println("value");
                    } catch (ComposizioneInsufficienteException e) {
                        System.out.println("coins");
                    }
                }

            }

        } catch (Exception e) {
            // non emettere output extra: i test confrontano l'output esatto
        }

    }

}
