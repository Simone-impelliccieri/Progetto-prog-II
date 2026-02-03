/**
 *
 * La cartella Macchinette contiene:
 * <ul>
 *   <li>{@code Importo} rappresenta un valore composto da unità e centesimi</li>
 *   <li>{@code Moneta} rappresenta i tipi di moneta ammessi </li>
 *   <li>{@code Aggregato} rappresenta un multi-insieme di monete </li>
 *   <li>{@code Taglia} rappresenta la taglia di un prodotto (S, M, L, XL)</li>
 *   <li>{@code Prodotto} rappresenta un prodotto all'interno del distributore</li>
 *   <li>{@code Binario} rappresenta un contenitore di prodotti all'interno del distributore</li>
 *   <li>{@code DistributoreAutomatico} rappresenta il distributore con binari, fondo cassa e strategia resto</li>
 *   <li>{@code StrategiaResto} interfaccia per le strategie di calcolo del resto</li>
 *   <li>{@code Strategie} strategie raggruppate (massimo/minimo/personale)</li>
 *   <li>{@code Parser} utilità per il parsing di importi, monete, taglie, prodotti e aggregati</li>
 * 
 * </ul>
 *
 * Ho utilizzato l'autocomplete di co-pilot per assistermi nella scrittura di
 * alcuni metodi e della documentazione Javadoc. 
 * Ho utilizzato chatgpt per il chiarimento di concetti e qualche correzione nei client.
 *  
 */
package macchinette;
