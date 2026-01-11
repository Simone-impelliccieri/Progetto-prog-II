---
applyTo: '**'
---
Provide project context and coding guidelines that AI should follow when generating code, answering questions, or reviewing changes.

non eseguire ne test ne build ne assemble se non specificamente richiesto.

scrivi il codice in modo che sia pronto per i clients

scrivi le variabili in italiano, ad esempio: totaleParziale, numeroElementi, valoreMassimo, ecc.  

GUARDA SEMPRE I FILE e README e CLIENT prima di rispondere, te li incollo qua sotto
Macchinette
Questo repository contiene il progetto d'esame della sessione invernale per l'insegnamento di "Programmazione II" all'a.a 2025/2026.

Obiettivo del progetto è realizzare un sistema di gestione per distributori automatici di prodotti confezionati a moneta, informalmente denominati "macchinetta".

Per portare a termine il lavoro dovrà decidere se e quali classi (concrete o astratte) e quali interfacce implementare. Per ciascuna di esse dovrà descrivere (in formato Javadoc attraverso commenti presenti nel codice) le scelte relative alla rappresentazione dello stato (con particolare riferimento all'invariante di rappresentazione e alla funzione di astrazione così come definiti nel libro di testo dell'insegnamento e illustrati a lezione) e ai metodi (con particolare riferimento a pre/post-condizioni ed effetti collaterali, soffermandosi ad illustrare le ragioni della correttezza solo per le implementazioni che riterrà più critiche). Osservi che l'esito di questa prova, che le consentirà di accedere o meno all'orale, si baserà tanto su questa documentazione quanto sul codice sorgente.

Può prendere visione dei dettagli tecnici riguardanti la realizzazione del progetto nelle apposite istruzioni. Osservi che la presenza di errori o fallimenti nella compilazione, generazione della documentazione ed esecuzione dei test impedisce il superamento dell'esame.

Nota bene: prenda attentamente visione della checklist presente nelle istruzioni, in modo da assicurarsi di aver completato tutti i punti richiesti. prima di consegnare. Non sarà consentita nessuna eccezione a tale regola.

Descrizione delle entità coinvolte
Le principali entità coinvolte nel progetto sono: i distributori automatici e i loro componenti, i prodotti e le monete e le relative entità con cui realizzare le transazioni. Di seguito ne vengono illustrate le caratteristiche principali (le informazioni che le descrivono e le competenze che possiedono).

Valori monetari
L'importo è composto da unità (un intero non negativo) e centesimi (un intero compreso tra 0 e 99, estremi inclusi). Una moneta è caratterizzato da un valore che può essere esclusivamente uno dei seguenti importi: 1, 2, 5, 10, 20 o 50 centesimi, oppure 1 o 2 unità. L'ordinamento naturale delle monete e degli importi è dato dal loro valore. Sono definite le seguenti operazioni (dall'ovvio significato):

somma: 
;
sottrazione: 
, prestando attenzione al segno;
moltiplicazione: 
;
divisione intera: 
, pari al più grande 
 tale che 
;
dove 
 e 
 sono importi e 
 è un intero non negativo.

Un aggregato è un multi-insieme di monete (ossia insiemi che possono contenere più occorrenze della stessa moneta), l'aggregato è utile ad esempio per rappresentare i pagamenti, i resti e il fondo cassa del distributore. Non è sensato definire un ordinamento tra aggregati di monete. Oltre che partendo da un elenco di monete, si può ottenere un aggregato sia aggiungendo che rimuovendo le monete di un altro aggregato; si osservi che la rimozione potrebbe non essere possibile innanzitutto per una questione di valore (il valore totale dell'aggregato potrebbe essere inferiore a quello da rimuovere), ma anche per una questione di composizione (ad esempio se si volesse rimuovere una moneta da 50 centesimi ma l'aggregato ne contenesse solo da 20 e 10 centesimi).

Prodotto
Un prodotto è caratterizzato da un nome (dato da una stringa non vuota), un prezzo (dato da un importo) e una taglia; per semplicità si assuma che la taglia sia uno dei tre valori: small, medium e large (rappresentati in input e output rispettivamente dai caratteri S, M e L). L'ordinamento naturale dei prodotti è dato dall'ordine della tripla taglia, nome e prezzo.

Distributore automatico
Un distributore automatico è costruito a partire da un elenco di binari (inizialmente vuoti, numerati progressivamente da 0), da un fondo cassa (dato da un aggregato di monete) e da una strategia per dare il resto.

Un binario è caratterizzato da una taglia e da una capacità massima (un intero positivo) e può essere vuoto, oppure contenere un numero di prodotti fino alla capacità massima, che devono avere taglia non superiore a quella del binario e devono essere tutti dello stesso tipo. Il binario consente due operazioni: dispensare un prodotto (se disponibile) e caricare un dato numero di prodotti. Il caricamento fallisce se il binario:

è vuoto e il numero di prodotti eccede la capacità, o la taglia; oppure
non è vuoto e i prodotti da caricare sono diversi da quelli presenti, o il numero complessivo di prodotti eccede la capacità.
A parità di importo e fondo cassa, ci sono diverse strategie per dare il resto (ossia per determinare l'esatto aggregato di monete che corrisponda esattamente alla differenza tra il prezzo pagato e quello del prodotto acquistato). Si può decidere di scegliere per prime le monete di valore minore (usando in questo modo un gran numero di monete), oppure di quelle di valore maggiore (riducendo così il numero di monete usate), così come si può scegliere di agire secondo tanti altri criteri, vari e complessi a piacere. Si osservi che non è detto che sia sempre possibile dare il resto, non solo perché il valore totale del fondo cassa potrebbe essere insufficiente, ma anche perché pur essendolo, a seconda della strategia scelta, potrebbe comunque risultare impossibile determinare un aggregato di monete che consenta di raggiungere esattamente l'importo del resto.

Una volta costruito il distributore automatico esso consente di eseguire le seguenti operazioni:

svuotare (integralmente) e aggiungere monete al fondo cassa, nonché conoscere l'importo totale in esso contenuto,
mostrare l'elenco dei prodotti disponibili, riportando per ciascun binario non vuoto il nome e prezzo del prodotto che contiene,
caricare (eventualmente in parte) una assegnata quantità di un prodotto e erogare, se possibile, un prodotto dato il numero del binario e un pagamento (espresso da un aggregato di monete), calcolando l'eventuale resto.
Il caricamento (parziale) di un prodotto avviene selezionando il primo binario (in ordine di numero) che possa contenerlo, passando se necessario ai successivi binari fino ad esaurire la quantità da caricare o lo spazio disponibile; il caricamento deve indicare il numero di prodotti che non sono stati caricati per mancanza di spazio.

L'erogazione di un prodotto è possibile se:

il binario indicato esiste e non è vuoto,
il prodotto in cima al binario ha un prezzo non superiore all'importo pagato,
il fondo cassa del distributore (una volta aggiunte le monete del pagamento) è in grado di fornire il resto (se necessario).
Cosa è necessario implementare
Dovrà implementare una gerarchia di oggetti utili a:

rappresentare le entità fondamentali moneta, prodotto e distributore automatico oltre alle entità di corredo necessarie per la loro gestione; per ciascuna di esse dovranno essere implementate le opportune operazioni;

definire e realizzare almeno tre diverse strategie per il calcolo del resto (tra cui le due descritte nella sezione precedente e una di sua ideazione);

valutare l'ipotesi di realizzare una o più eccezioni personalizzate per segnalare situazioni di errore particolari (ad esempio il tentativo di erogazione di un prodotto da un binario vuoto);

realizzare tutti i client, ossia le classi di test (come descritto di seguito).

Al fine di evitare confusione con i client (descritti nella prossima sottosezione), è consigliabile che il suo codice sia contenuto in una gerarchia di pacchetti, ad esempio nel pacchetto macchinette, i cui sorgenti dovranno essere nella directory src/main/java/macchinette.

Le classi client
Facendo uso delle classi della sua soluzione, dovrà quindi implementare una serie di classi client secondo quanto illustrato nelle istruzioni e seguendo attentamente le specifiche del docente.

Si ricorda che il progetto non sarà valutato a meno che tutti i test svolti tramite il comando gradle test che esercita tali client diano esito positivo.

Codice di condotta
Dovendo svolgere il progetto a casa non le vengono imposte particolari restrizioni delle quali sarebbe peraltro difficile verificare il rispetto.

Le è pertanto consentito di avvalersi:

di qualunque risorsa disponibile in rete,
di strumenti di supporto basati sull'AI (come GitHub Copilot, o ChatGPT),
del confronto con altri studenti, o professionisti,
sia per la progettazione che per l'implementazione e documentazione del codice. Ogni supporto che la aiuti a apprendere e dominare gli obiettivi culturali dell'insegnamento è benvenuto!

D'altro canto le viene formalmente richiesto di elencare (nella documentazione del codice) in modo chiaro ed esaustivo ogni risorsa di cui si è avvalso al di fuori di quelle esplicitamente indicate come materiale didattico dell'insegnamento. L'omissione di tale elenco può costituire motivo di respingimento del progetto e, in gravi casi di plagio alle sanzioni disciplinari previste.

Si sottolinea che consegnando il progetto lei dichiara di fatto di esserne l'unico autore, assumendosi la piena responsabilità dell'originalità del codice e della documentazione che esso include, nonché della completezza e veridicità del suddetto elenco. Per questa ragione non le è consentito condividere il suo codice con altri studenti.

Durante la discussione orale, eventuali incertezze nell'illustrare, giustificare o modificare il materiale consegnato non potranno che essere a lei esclusivamente addebitate e, come tali, valutate negativamente.

Nota bene: la violazione del presente codice di condotta, qualora ne venga accertato il dolo, può condurre a sanzioni disciplinari come previste dal comma d) dell'art. 52 del Regolamento generale d'Ateneo

Note legali e copyright
Ai sensi della Legge n. 633/1941 e successive modificazioni, l'autore si riserva, in ogni forma e modo nei limiti fissati dalla legge, il diritto esclusivo di pubblicare e di utilizzare il materiale contenuto nel presente repository.

Più specificatamente, è fatto divieto di riprodurre, trascrivere, comunicare al pubblico, distribuire, tradurre, elaborare e modificare il presente materiale (codice sorgente compreso), in tutto o in parte, senza specifica autorizzazione scritta dell'autore.





Specifiche dei client
Questo documento contiene la versione c6cbb34 (del 2025-12-22, ora 11:23:53) delle specifiche dei client da implementare per il progetto. Come illustrato nelle istruzioni dovrà completare le implementazioni delle classi nel pacchetto clients (che si trovano nella directory src/main/java/clients) seguendo con attenzione quanto illustrato di seguito.

Nota bene: i client hanno non a caso questo nome: il loro compito è usare le classi da lei implementate per svolgere il loro compito, devono pertanto contenere poco e semplice codice al loro interno, praticamente solo quello strettamente necessario a:

leggere i dati dal flusso di ingresso e/o dagli argomenti sulla linea di comando,
istanziare le classi da lei implementate che ritiene utili per svolgere il compito assegnato al client,
esercitarne i metodi ai fini di ottenere il comportamento descritto di seguito, quindi
scrivere i risultati nel flusso d'uscita.
Nei client non occorre aggiungere alcuna documentazione; se il loro codice è ricco di logica o supera (indicativamente) una ventina di righe, significa che probabilmente le classi della soluzione non sono state progettate in modo da avere le competenze necessarie.

Di seguito, trova (raccolti per entità) una sezione per ciascun client, il nome della sezione coincide esattamente con il nome che della classe (predisposta dal docente nella directory src/main/java/clients) che dovrà completare e della directory contenente i file di test (predisposta dal docente nella directory tests/clients).

Si ricorda che il progetto non sarà valutato a meno che tutti i test svolti tramite il comando gradle test che esercita i client qui descritti diano esito positivo.

Input e output
Gli importi
Come più volte discusso in classe, un importo monetario non va mai rappresentato mediante un numero in virgola mobile (ad esempio, float o double), questo vale anche riguardo alla lettura e scrittura di importi da e verso i flussi di ingresso e uscita.

Per convertire una stringa str che rappresenta un importo (ad esempio, ".50" o "2.30") nel numero intero totalCents corrispondente (come 50 e 230 nell'esempio precedente), evitando problemi di arrotondamento e con minimo sforzo, si può utilizzare questo frammento di codice

int totalCents = (new BigDecimal(str)).multiply(BigDecimal.valueOf(100)).intValueExact();
a patto di gestire in modo adeguato le eccezioni che possono essere sollevate dal costruttore, o dal metodo intValueExact().
Nella direzione opposta, il formato adoperato in questo tema è dato da:

1 unit, 1 cent e 1 unit 1 cent rispettivamente per gli importi di valore 1, 0.01 e 1.01;
 units per gli importi interi di valore 
;
 cents per gli importi di valore compreso tra 0 e 1 (estremi esclusi);
1 unit 
 cents per gli importi di valore compreso tra 1 e 2 (estremi esclusi);
 units 
 cents per tutti gli altri casi.
Detto più brevemente, la forma è [u unit[s]] [c cent[s]], dove la parte tra parentesi quadre è opzionale a seconda che i valori siano diversi da 0 e 1.

Spazi bianchi
In alcuni casi l'input è composto di parti inframezzate da operatori come ,, x, | o simili. In tali casi, è sempre possibile che vi siano spazi bianchi (uno o più) prima o dopo tali operatori. I client devono essere in grado di gestire tali spazi bianchi senza problemi.

I client
RiconosciMonete
Legge una sequenza di importi dal flusso d'ingresso, uno per linea, ed emette nel flusso d'uscita il valore della moneta corrispondente, oppure la stringa invalid se l'importo non corrisponde ad alcuna moneta.

Ad esempio, su input

.05
.07
.10
1.00
1.50
2.00
il client emette
5 cents
invalid
10 cents
1 unit
invalid
2 units
OperazioniImporti
Legge dal flusso di ingresso una sequenza di operazioni, una per riga, della forma

I + J
I - J
I * n
I / J
dove I e J sono importi monetari (ad esempio, ".50" o "2.30") e n è un intero. Per ciascuna operazione, il client emette nel flusso d'uscita il risultato dell'operazione, oppure la stringa invalid se uno degli operandi non è nel formato corretto, oppure negative se il primo operando della sottrazione è minore del secondo, o l'intero è negativo.
OperazioniAggregati
Legge dal flusso di ingresso una sequenza di linee, ciascuna linea contiene il segno + o - seguito da uno spazio e da un aggregato, descritto da una sequenza separata da , di coppie n x M, dove n è un intero positivo e M è un importo corrispondente ad una moneta.

A partire dall'aggregato vuoto, per ciascuna linea il client procede aggiungendo (se il segno è +) o rimuovendo (se il segno è -) l'aggregato descritto emettendo nel flusso d'uscita il valore dell'aggregato risultante. Dato che non è sempre possibile rimuovere un aggregato da un'altro, il client emette value se il valore totale da rimuovere eccede quello dell'aggregato corrente, oppure coins se pur avendo valore totale maggiore, non ci sono abbastanza monete del taglio indicato da rimuovere.

Ad esempio, su input

+ 10 x .20, 5 x .50, 20 x 1, 10 x 2
- 1 x 2
- 5 x .01
- 30 x 2
il client emette
<10 x 20 cents, 5 x 50 cents, 20 x 1 unit, 10 x 2 units>
<10 x 20 cents, 5 x 50 cents, 20 x 1 unit, 9 x 2 units>
coins
value
la terza riga è coins perché sebbene l'aggregato sulla seconda riga valga più di 5 cents, non contiene monete da 1 cent (che quindi non possono essere rimosse); la terza riga è value perché l'aggregato sulla seconda riga vale meno di 60 units.
CalcolaResti
Riceve come argomento sulla linea di comando un carattere tra H e L e quindi un intero corrispondente all'importo del resto e quindi legge dal flusso di ingresso una sequenza di aggregati (uno per linea, nel formato descritto in precedenza) e per ciascuno di essi emette l'aggregato corrispondente al resto calcolato usando dapprima le monete di taglio più alto (se il carattere è H) o più basso (se il carattere è L). Dato che non sempre è possibile calcolare il resto, il client emette value se il valore totale dell'aggregato è minore dell'importo del resto, oppure change se pur avendo valore totale maggiore la strategia non è in grado di determinare il resto.

Ad esempio, avendo H 13.50 come argomenti sulla linea di comando, su input

10 x .01, 10 x .02, 10 x .05, 10 x .10, 10 x .20, 10 x 1
10 x 2
il client emette
<10 x 5 cents, 10 x 10 cents, 10 x 20 cents, 10 x 1 unit>
change
dove la prima riga è un aggregato contenuto nell'aggregato di input che vale esattamente 13.50, mentre la seconda riga è change perché l'aggregato di input vale 20.00 ma non contiene monete che permettono di calcolare il resto di 13.50.
OrdinaProdotti
Legge dal flusso di ingresso una sequenza di prodotti, uno per linea, ciascuno nella forma nome|prezzo|taglia (dove taglia è uno dei caratteri: S, M o L) ordinata in base all'ordinamento naturale dei prodotti.

Ad esempio, su input

Bibita|2|S
Crostata|1.50|M
Noccioline|3|S
Bibita|1.50|S
il client emette
<Bibita, 1 unit 50 cents, S>
<Bibita, 2 units, S>
<Noccioline, 3 units, S>
<Crostata, 1 unit 50 cents, M>
CaricaBinari
Data la capacità (data da un intero positivo) e la taglia (indicata da uno dei caratteri: S, M o L) di un binario come argomenti sulla linea di comando, costruisce tale binario (inizialmente vuoto) e ne emette nel flusso d'uscita la descrizione.

Quindi legge dal flusso di ingresso una sequenza di coppie (separate da ,) quantità nome|prezzo|taglia e per tenta di caricare la quantità specificata del prodotto indicato nel binario, emettendo nel flusso d'uscita una rappresentazione del binario, se l'operazione ha avuto successo, oppure:

item se si sta cercando di caricare un prodotto diverso da quello già contenuto nel binario,
capacity se la quantità da caricare eccede la capacità del binario,
size se la taglia del prodotto da caricare eccede quella del binario.
Ad esempio, avendo 100 M come argomenti sulla linea di comando, su input

1, Panino|4.50|L
101, Snack|.50|S
10, Acqua|1|M
20, Bibita|1.50|S
80, Acqua|1|M
11, Acqua|1|M
10, Acqua|1|M
il client emette
<-, M, 0, 100>
size
capacity
<<Acqua, 1 unit, M>, M, 10, 100>
item
<<Acqua, 1 unit, M>, M, 90, 100>
capacity
<<Acqua, 1 unit, M>, M, 100, 100>
dove le righe in cui non viene riportato il contenuto del binario sono dovute al fatto che il panino eccede la dimensione, gli snack eccedono la capacità, la bibita non può essere aggiunta ad un binario che contiene già l'acqua e nel binario dove ci sono già 90 unità di acqua non possono essere aggiunte altre 11 unità perché si supererebbe la capacità.
UsaDistributore
Legge dal flusso di ingresso una linea contenente coppie capacità|taglia (separate da ,) che descrivono un elenco di binari, quindi legge una linea che descrive un aggregato di monete e procede col creare il distributore automatico con i binari dell'elenco e il fondo cassa dato dall'aggregato.

Quindi legge dal flusso di ingresso una sequenza di comandi, uno per linea, di una delle seguenti forme:

+ quantità, nome|prezzo|taglia per caricare il prodotto specificato nel distributore;
- indice, aggregato per erogare il prodotto dal binario di indice dato, pagando con l'aggregato specificato;
? per emettere nel flusso d'uscita lo stato corrente del distributore.
A fronte del caricamento, emette nel flusso d'uscita il numero di prodotti avanzati nella forma + avanzati; a fronte dell'erogazione emette nel flusso d'uscita:

l'aggregato corrispondente al resto, nella forma - resto,se l'erogazione ha avuto successo,
- slot se l'indice non corrisponde ad alcun binario,
- value se il valore totale dell'aggregato di pagamento è inferiore al prezzo del prodotto,
- empty se il binario indicato è vuoto,
- change se pur avendo valore totale maggiore, la strategia di calcolo del resto non è in grado di produrre un aggregato corrispondente al resto dovuto.
Ad esempio, su input

30|S, 20|M, 10|L, 30|S, 20|M, 10|L, 30|S, 20|M, 10|L
10 x .50, 10 x 1, 10 x 2
+ 5, Bibita|2|S
+ 5, Panino|3.20|L
- 2, 2 x 2, 2 x 1
+ 5, Patatine|2.50|M
?
- 3, 1 x 1, 2 x .20, 1 x .10
+ 10, Panino|3.20|L
- 5, 3 x 2, 1 x .20
?
- 1, 2 x 1, 5 x .20
+ 10, Extra|10|L
?
+ 2, Extra|10|L
- 1, 1 x 1
?
- 10, 1 x 1
il client emette
+ 0
+ 0
- change
+ 0
? 0 | Bibita | 2 units
? 1 | Patatine | 2 units 50 cents
? 2 | Panino | 3 units 20 cents
- empty
+ 0
- <1 x 1 unit, 1 x 2 units>
? 0 | Bibita | 2 units
? 1 | Patatine | 2 units 50 cents
? 2 | Panino | 3 units 20 cents
? 5 | Panino | 3 units 20 cents
- <1 x 50 cents>
+ 0
? 0 | Bibita | 2 units
? 1 | Patatine | 2 units 50 cents
? 2 | Panino | 3 units 20 cents
? 5 | Panino | 3 units 20 cents
? 8 | Extra | 10 units
+ 2
- value
? 0 | Bibita | 2 units
? 1 | Patatine | 2 units 50 cents
? 2 | Panino | 3 units 20 cents
? 5 | Panino | 3 units 20 cents
? 8 | Extra | 10 units
- slot
Se ci si limita ad esaminare le erogazioni

- 2, 2 x 2, 2 x 1
- 3, 1 x 1, 2 x .20, 1 x .10
- 5, 3 x 2, 1 x .20
- 1, 2 x 1, 5 x .20
- 1, 1 x 1
- 10, 1 x 1
e i relativi esiti
- change
- empty
- <1 x 1 unit, 1 x 2 units>
- <1 x 50 cents>
- value
- slot
si osserva che il primo fallisce perché non è possibile dare il resto (mancano i 20 cent in cassa), il secondo perché il binario è vuoto, il terzo riesce e produce un resto di 1 unità e 2 units (possibile perché i 20 cent sono parte del pagamento), il quarto riesce e produce un resto di 50 cents, il quinto fallisce perché il valore del pagamento è inferiore al prezzo del prodotto, il sesto fallisce perché non esiste alcun binario di indice 10.
