package macchinette;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe di utilità per il parsing di importi, monete, taglie, prodotti e aggregati.
 */
public class Parser {

	/**
	 * Costruttore privato 
	 */
	private Parser() {
	}

	/**
	 * Legge una stringa e restituisce l'istanza di importo corrispondente.
	 *
	 * La stringa può contenere spazi bianchi iniziali o finali, che vengono ignorati.
	 *
	 * @param str, è l'importo in stringa.
	 * @return l'importo corrispondente.
	 * @throws IllegalArgumentException se {@code str} è {@code null} o vuota,
	 *         se il formato non è valido, oppure se l'importo è negativo.
	 */
	public static Importo daStringaImporto(String str) {
		if (str == null) {
			throw new IllegalArgumentException("stringa nulla");
		}
		String s = str.trim();
		if (s.isEmpty()) {
			throw new IllegalArgumentException("stringa vuota");
		}
		try {
			int totalCents = new BigDecimal(s).multiply(BigDecimal.valueOf(100)).intValueExact();
			if (totalCents < 0) {
				throw new IllegalArgumentException("importo negativo");
			}
			return Importo.daCentesimi(totalCents);
		} catch (ArithmeticException | NumberFormatException e) {
			throw new IllegalArgumentException("formato importo non valido", e);
		}
	}

	/**
	 * Cerca la moneta il cui valore è uguale alla stringa in input.
	 *
	 *
	 * @param stringa, rappresentazione testuale dell'importo.
	 * @return la moneta corrispondente.
	 * @throws IllegalArgumentException se la stringa è nulla o non rappresenta una moneta valida.
	 */
	public static Moneta daStringaMoneta(String stringa) {
		if (stringa == null) {
			throw new IllegalArgumentException("stringa nulla");
		}

		Importo importo = daStringaImporto(stringa);
		for (Moneta moneta : Moneta.values()) {
			if (moneta.getValore().equals(importo)) {
				return moneta;
			}
		}

		throw new IllegalArgumentException("moneta non valida");
	}

	/**
	 * Restituisce la taglia corrispondente alla stringa in ingresso.
	 *
	 * La stringa può contenere spazi bianchi iniziali e finali, che vengono ignorati.
	 * È riconosciuta una singola lettera tra "S", "M", "L" o "XL".
	 *
	 * @param stringa, che rappresenta una taglia.
	 * @return la taglia riconosciuta.
	 * @throws IllegalArgumentException se la stringa è nulla o non rappresenta una taglia valida.
	 */
	public static Taglia daStringaTaglia(String stringa) {
		if (stringa == null) {
			throw new IllegalArgumentException("stringa nulla");
		}

		String testo = stringa.trim().toUpperCase();
		try {
			return Taglia.valueOf(testo);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("taglia non valida", e);
		}
	}

	/**
	 * Costruisce un prodotto a partire da una descrizione di tipo string.
	 *
	 * Il formato atteso è {@code nome|prezzo|taglia} (con separatori anche {@code ;} o {@code @}).
	 * La taglia deve essere uno tra i caratteri {@code S}, {@code M}, {@code L} e {@code XL}.
	 *
	 * @param descrizione, descrizione del prodotto.
	 * @return il prodotto, corrispondente alla descrizione.
	 * @throws IllegalArgumentException se {@code descrizione} è null, se il formato non è valido, se il
	 *         prezzo non è un importo valido oppure se la taglia non è valida.
	 */
	public static Prodotto daStringaProdotto(String descrizione) {
		if (descrizione == null) {
			throw new IllegalArgumentException("descrizione nulla");
		}

		String[] parti = descrizione.split("\\s*[|;@]\\s*", -1);

		if (parti.length != 3) {
			throw new IllegalArgumentException("formato prodotto non valido");
		}

		String nome = parti[0].trim();

		Importo prezzo = daStringaImporto(parti[1].trim());

		Taglia taglia = daStringaTaglia(parti[2].trim());

		return new Prodotto(nome, prezzo, taglia);
	}

	/**
	 * Legge una stringa e restituisce l'istanza di aggregato corrispondente.
	 *
	 * Se {@code descrizione} è null o vuota viene restituito l'aggregato vuoto.
	 *
	 * @param descrizione, descrizione testuale dell'aggregato.
	 * @return l'aggregato.
	 * @throws IllegalArgumentException se il formato non è valido o se una moneta non è riconosciuta.
	 */
	public static Aggregato daStringaAggregato(String descrizione) {
		Aggregato agg = new Aggregato();
		if (descrizione == null || descrizione.isBlank()) {
			return agg;
		}

		Pattern espressione = Pattern.compile("\\s*(\\d+)\\s*[x*é]\\s*(.+)\\s*");
		for (String pezzo : descrizione.split("[,;]")) {
			if (pezzo.isBlank()) {
				continue;
			}

			Matcher m = espressione.matcher(pezzo);
			if (!m.matches()) {
				throw new IllegalArgumentException("Formato non valido: " + pezzo);
			}

			String testoMoneta = m.group(2).trim();

			Moneta moneta = daStringaMoneta(testoMoneta);

			agg.aggiungi(moneta, Integer.parseInt(m.group(1)));
		}
		return agg;
	}
}
