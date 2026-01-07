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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import macchinette.Importo;

public class OperazioniImporti {

    private static final Pattern operazione = Pattern.compile("^\\s*(.+?)\\s*([+\\-*/])\\s*(.+?)\\s*$");

    public static void main(String[] args) {

        try (Scanner sca = new Scanner(System.in)) {

            while (sca.hasNextLine()) {

                String riga = sca.nextLine();
                if (riga == null) {
                    continue;
                }
                String rigaPulita = riga.trim();
                if (rigaPulita.isEmpty()) {
                    continue;
                }

                Matcher matcher = operazione.matcher(rigaPulita);
                if (!matcher.matches()) {
                    System.out.println("invalid");
                    continue;
                }

                String sinistra = matcher.group(1).trim();
                String operatore = matcher.group(2);
                String destra = matcher.group(3).trim();

                Importo importoSinistro;
                try {
                    importoSinistro = Importo.daStringa(sinistra);
                } catch (IllegalArgumentException e) {
                    System.out.println("invalid");
                    continue;
                }

                switch (operatore) {
                    case "+": {
                        try {
                            Importo importoDestro = Importo.daStringa(destra);
                            System.out.println(importoSinistro.somma(importoDestro));
                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid");
                        }
                        break;
                    }
                    case "-": {
                        try {
                            Importo importoDestro = Importo.daStringa(destra);
                            if (importoSinistro.compareTo(importoDestro) < 0) {
                                System.out.println("negative");
                            } else {
                                System.out.println(importoSinistro.sottrai(importoDestro));
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid");
                        }
                        break;
                    }
                    case "*": {
                        int moltiplicatore;
                        try {
                            moltiplicatore = Integer.parseInt(destra);
                        } catch (NumberFormatException e) {
                            System.out.println("invalid");
                            continue;
                        }
                        if (moltiplicatore < 0) {
                            System.out.println("negative");
                            continue;
                        }
                        try {
                            System.out.println(importoSinistro.moltiplica(moltiplicatore));
                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid");
                        }
                        break;
                    }
                    case "/": {
                        try {
                            Importo importoDestro = Importo.daStringa(destra);
                            System.out.println(importoSinistro.divIntera(importoDestro));
                        } catch (IllegalArgumentException e) {
                            System.out.println("invalid");
                        }
                        break;
                    }
                    default: {
                        System.out.println("invalid");
                        break;
                    }
                }
            }

        } catch (Exception e) {
        }

    }

}
