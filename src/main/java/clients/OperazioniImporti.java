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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import macchinette.Importo;
import macchinette.parser;

public class OperazioniImporti {

    private static final Pattern operazione = Pattern.compile("^\\s*(.+?)\\s*([+\\-*/])\\s*(.+?)\\s*$");

    public static void main(String[] args) {
        try (Scanner sca = new Scanner(System.in)) {
            while (sca.hasNextLine()) {
                String riga = sca.nextLine().trim();
                if (riga.isEmpty()) {
                    continue;
                }

                Matcher match = operazione.matcher(riga);
                if (!match.matches()) {
                    System.out.println("invalid-result");
                    continue;
                }

                String sinistraStr = match.group(1);
                String operatore = match.group(2);
                String destraStr = match.group(3).trim();

                Importo importoSinistro;
                try {
                    importoSinistro = parser.daStringaImporto(sinistraStr);
                } catch (IllegalArgumentException e) {
                    System.out.println("invalid-result");
                    continue;
                }

                if (operatore.equals("*")) {
                    int fattore;
                    try {
                        fattore = Integer.parseInt(destraStr);
                    } catch (NumberFormatException e) {
                        System.out.println("invalid-result");
                        continue;
                    }

                    if (fattore < 0) {
                        System.out.println("negative");
                    } else {
                        try {
                            System.out.println(importoSinistro.moltiplica(fattore));
                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid-result");
                        }
                    }

                } else {
                    Importo importoDestro;
                    try {
                        importoDestro = parser.daStringaImporto(destraStr);
                    } catch (IllegalArgumentException e) {
                        System.out.println("invalid-result");
                        continue;
                    }

                    if (operatore.equals("-") && importoSinistro.compareTo(importoDestro) < 0) {
                        System.out.println("negative-result");
                    } else {
                        try {
                            switch (operatore) {
                                case "+":
                                    System.out.println(importoSinistro.somma(importoDestro));
                                    break;
                                case "-":
                                    System.out.println(importoSinistro.sottrai(importoDestro));
                                    break;
                                case "/":
                                    System.out.println(importoSinistro.divIntera(importoDestro));
                                    break;
                                default:
                                    System.out.println("invalid-result");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid-result");
                        }
                    }
                }
            }
        }
    }
}