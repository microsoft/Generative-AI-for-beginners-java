# Tutorial sulle Tecniche Fondamentali di AI Generativa

## Indice

- [Prerequisiti](#prerequisiti)
- [Per Iniziare](#per-iniziare)
- [Guida alla Selezione del Modello](#guida-alla-selezione-del-modello)
- [Tutorial 1: Completamenti LLM e Chat](#tutorial-1-completamenti-llm-e-chat)
- [Tutorial 2: Chiamata di Funzione](#tutorial-2-chiamata-di-funzione)
- [Tutorial 3: RAG (Generazione Arricchita da Recupero)](#tutorial-3-rag-generazione-arricchita-da-recupero)
- [Tutorial 4: AI Responsabile](#tutorial-4-ai-responsabile)
- [Modelli Comuni Attraverso gli Esempi](#modelli-comuni-attraverso-gli-esempi)
- [Test Unitari](#test-unitari)
- [Verifica Live Sequenziale](#verifica-live-sequenziale)
- [Risoluzione Problemi](#risoluzione-dei-problemi)
- [Prossimi Passi](#prossimi-passi)

## Panoramica

Quattro programmi Java indipendenti dimostrano chat, cronologia delle conversazioni, chiamata di funzione, generazione arricchita dal recupero dell’intero documento (RAG), e gestione della risposta AI responsabile. Tutte le richieste chat puntano per impostazione predefinita a **GPT-5.6 Luna con sforzo di ragionamento `none`**.

Questi esempi usano l’SDK Java ufficiale di OpenAI con l’endpoint v1 di Azure OpenAI, seguendo la [guida SDK di Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Il pacchetto `azure-ai-openai` più vecchio non è più una dipendenza. Chat Completions è mantenuto per insegnare i flussi basati su messaggi esistenti; vedere [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) per altre opzioni API.

## Prerequisiti

- Java 21 o superiore e Maven 3.6.3 o superiore.
- Una distribuzione chat Azure OpenAI chiamata `gpt-5.6-luna`, o un override con impostazioni Chat Completions compatibili.
- Un’identità Azure autenticata con il ruolo **Utente OpenAI Servizi Cognitivi** sulla risorsa. Lo sviluppo locale utilizza l’accesso Azure CLI; le applicazioni ospitate possono usare l’identità gestita.
- Vedere il [Capitolo 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) per configurazione della risorsa e istruzioni di accesso.

La [configurazione Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) blocca queste versioni, verificate il 14-09-2026:

| Componente | Versione | Scopo |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Client ufficiale compatibile con Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Autenticazione senza chiavi e aggiornamento token |
| `net.objecthunter:exp4j` | 0.4.8 | Parsing espressioni aritmetiche senza esecuzione codice |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Test unitari Jupiter offline |
| Compilatore/Surefire/Exec Maven | 3.16.0 / 3.6.0 / 3.6.4 | Compilazione Java 21, test, esempi eseguibili |

Il compilatore usa `--release 21`. Nessuna dipendenza Spring Boot, Spring AI o LangChain4j è richiesta per questi esempi autonomi.

## Per Iniziare

Dalla radice del repository, imposta l’endpoint della risorsa e l’override di distribuzione opzionale nella tua shell.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

I test non richiedono credenziali Azure né endpoint. Maven non legge automaticamente un file ambiente; imposta le variabili nella shell usata per avviare gli esempi live. Per esecuzioni da IDE, verifica l’ambiente fornito dalla configurazione di lancio.

## Guida alla Selezione del Modello

| Variabile ambiente | Significato | Predefinito |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Radice risorsa HTTPS Azure o URL `/openai/v1` già normalizzato | Richiesto per esecuzioni live |
| `AZURE_OPENAI_DEPLOYMENT` | Nome distribuzione chat, non versione modello | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configurazione distribuzione embedding separata, non usata da questi quattro programmi | `text-embedding-3-small` |

Override di distribuzione vuoti usano i valori predefiniti. La configurazione aggiunge `/openai/v1` esattamente una volta e rifiuta credenziali, stringhe di query e percorsi di distribuzione legacy nell’endpoint.

Ogni richiesta chat imposta esplicitamente `reasoningEffort(ReasoningEffort.NONE)` e `maxCompletionTokens(...)`. Nessuna richiesta imposta `temperature`, `top_p` o l’opzione token di completamento legacy. Questo include la selezione strumenti e i follow-up con risultati strumenti. Gli strumenti chat di GPT-5.6 richiedono sforzo di ragionamento `none`; vedere la [guida chat Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Non esiste un punto di ingresso per streaming o embedding in questo capitolo.** Il lettore ottiene l’intero documento, non i vettori. Se lo estendi con embedding, usa una distribuzione embedding separata come `text-embedding-3-small`, mai Luna.

## Tutorial 1: Completamenti LLM e Chat

Fonte: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Il programma esegue una semplice spiegazione Java stream, una conversazione a due turni HashMap/TreeMap, e chat interattiva. Il secondo turno include la prima risposta assistente; ogni turno interattivo invia anche la conversazione precedente.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` fornisce la distribuzione e l’impostazione esplicita del ragionamento. La chat interattiva salta le righe vuote, termina con `exit` o EOF, e mantiene il messaggio di sistema più nove turni utente/assistente completati. Il taglio del conteggio dei turni è un limite educativo, non una garanzia esatta del budget token.

Dalla directory esempi:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Aspettati tre risposte iniziali, poi un prompt `You:`. Ogni domanda interattiva non vuota aggiunge una richiesta. I limiti di completamento sono 200, 300, 400, poi 500 token per turno interattivo.

## Tutorial 2: Chiamata di Funzione

Fonte: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

L’SDK deriva schemi JSON dai record annotati `WeatherArguments` e `CalculationArguments`. Una scelta strumento obbligatoria fa esercitare il protocollo strumento per ogni esempio invece di accettare una risposta non assistita del modello.

1. Invia una domanda con lo strumento permesso, sforzo di ragionamento `none`, e limite di completamento 300 token.
2. Richiedi un motivo di fine `tool_calls`, valida nome funzione e ID chiamate, e analizza argomenti JSON tipizzati.
3. Esegui la funzione locale. Il modello non esegue codice Java o arbitrario.
4. Aggiungi il messaggio chiamata strumento assistente una volta, seguito da ogni risultato con il suo `tool_call_id` corrispondente.
5. Invia una richiesta finale da 300 token senza strumenti e richiedi una risposta completa e non vuota.

`get_weather` restituisce il meteo **simulato**, non live. Rispetta la città e converte i 22 gradi Celsius di esempio in Fahrenheit se richiesto. `calculate` valuta l’espressione fornita tramite exp4j, supporta forme come `15% di 240` e `2 + 3 * 4`, e rifiuta calcoli vuoti, sovradimensionati, invalidi o non finiti. Usa aritmetica in virgola mobile, non precisione decimale finanziaria.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Aspettati `Function: get_weather`, meteo simulato di Seattle, `Function: calculate`, `Function result: 36`, e le due risposte finali. Non sono richiesti stdin o credenziali meteo esterne. Una corsa riuscita usa esattamente quattro richieste chat.

## Tutorial 3: RAG (Generazione Arricchita da Recupero)

Fonte: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Input: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Questo esempio introduttivo RAG recupera un intero documento UTF-8 e lo include nel messaggio utente con la domanda. Un messaggio sistema separato istruisce il modello a trattare il contenuto documento come dati non affidabili e rispondere solo da quel contesto. Se il documento non contiene la risposta, la risposta richiesta è: `Non riesco a trovare questa informazione nel documento fornito.`

La contestualizzazione può ridurre le allucinazioni, ma né delimitatori né istruzioni di sistema garantiscono accuratezza o prevengono ogni injection di prompt. Rivedi risposte live. RAG di produzione normalmente aggiunge suddivisione, recupero, citazioni, controllo accesso e valutazione.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Inserisci una domanda, ad esempio `Quale metodo di autenticazione descrive il documento?`. Aspettati una risposta che menzioni Microsoft Entra ID. Il programma termina dopo una richiesta chat con limite completamento 500 token.

La ricerca file predefinita funziona dalla radice repository, directory capitolo, o directory esempi. È supportato anche un percorso esplicito:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

I dati di input devono non essere vuoti: massimo 32 KiB di documento UTF-8 e 2.000 caratteri di domanda. File mancanti, domande vuote/EOF, e input sovradimensionati falliscono prima dell'inferenza.

## Tutorial 4: AI Responsabile

Fonte: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Le sei sonde coprono istruzioni dannose, discorsi d’odio, privacy, disinformazione medica, contenuti illegali, e una domanda AI responsabile benigno. Il programma osserva la risposta invece di presumere che ogni sonda debba attivare un filtro.

| Esito | Evidenza |
| --- | --- |
| `FILTRATO` | Un codice errore esplicito `content_filter` / `ResponsibleAIPolicyViolation`, o un motivo fine completamento `content_filter` |
| `RIFIUTATO` | Un campo strutturato `message.refusal` non vuoto |
| `POSSIBILE_RIFIUTO` | Una frase d’apertura di rifiuto nel testo ordinario; un euristico che richiede revisione |
| `GENERATO` | Una risposta completata e non vuota; non prova che il contenuto sia sicuro |

Un ordinario HTTP 400 **non** è evidenza di filtraggio. Parametri invalidi, errori autenticazione, limiti di frequenza, errori server, risposte malformate, e output troncato causano fallimento dell’esecuzione invece di un falso successo sulla sicurezza. Parole ampie come "contenuto dannoso" in una spiegazione benigno non contano come rifiuto.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Aspettati sei risultati categoria e un riepilogo che dichiara che le osservazioni non sono una certificazione di sicurezza. Ogni sonda ha un limite completamento di 300 token. Rivedi manualmente generazioni inattese e possibili rifiuti; il confronto benigno dovrebbe produrre una spiegazione AI responsabile sostanziale. Nessun stdin richiesto.

## Modelli Comuni Attraverso gli Esempi

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizza la normalizzazione endpoint, override di distribuzione, autenticazione senza chiavi, e opzioni chat:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Il fornitore token aggiorna i token di accesso quando serve. Non registrare token né sostituirlo con chiave API. Ogni programma riusa il client e lo chiude in `finally` o con proprio wrapper `AutoCloseable`; l’SDK `OpenAIClient` stesso non è `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) richiede risposta testuale completata e non vuota. Scelte vuote, rifiuti, filtri e risposte tronche non sono stampate silenziosamente come successo. L’esempio AI responsabile gestisce esplicitamente filtri/rifiuti attesi. Fallimenti non gestiti danno codice uscita Java/Maven non zero.

**I tentativi automatici SDK sono disabilitati** per mantenere prevedibile il conteggio richieste su distribuzioni a bassa RPM condivise. Ogni richiesta inferenza ha timeout di 60 secondi. L’acquisizione token può richiedere tempo aggiuntivo. Il scheduling a livello applicazione deve rispettare le quote; non rilanciare ciecamente richieste pagate fallite.

## Test Unitari

Dalla directory esempi:

```powershell
mvn -B -ntp clean test
```

Il trasporto di test sostituisce completamente il livello HTTP SDK, cattura i body serializzati reali delle richieste, e fornisce risposte in coda. Non apre socket, non acquisisce token Azure, e fallisce su richieste inattese. Questi test convalidano comportamento applicazione e protocollo SDK, non qualità modello live o disponibilità distribuzione.

| Suite test | Copertura |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizzazione/rifiuto endpoint, override distribuzione, ragionamento e opzioni token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Ogni workflow completamento, storia messaggi, taglio turni completo, EOF, fallimenti |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schemi strumenti, argomenti tipizzati, aritmetica, ID, risultati moltipli strumenti, follow-up falliti |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Lookup file, UTF-8, limiti dimensioni, payload contestualizzazione, errori input e API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Tutte le sei sonde, filtri espliciti, classificazione rifiuti, 400 ordinario ed altri fallimenti |

Per una suite, usa `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Fixture condivise vivono in [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Verifica Live Sequenziale

Le chiamate live sono separate dai test unitari. Usa i seguenti comandi **individualmente**, dalla radice repository, solo dopo che le credenziali e l’accesso distribuzione sono pronti. Non sono necessari servizi o processi persistenti.

Per una distribuzione condivisa **10 richieste/minuto**, riserva quota sufficiente per tutto il programma prossimo prima di avviarlo: 5, 4, 1, poi 6 richieste. Processi sequenziali da soli non garantiscono il rispetto del limite di frequenza. Coordina il minuto mobile con tutti gli altri chiamanti; non incollare le quattro invocazioni come batch senza ritmo.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completamenti, multi-turn, e due turni interattivi:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Controlla tutti e tre i titoli delle sezioni, cinque risposte, una risposta finale interattiva che richiama Ada, `Goodbye!`, e il codice di uscita 0. Budget: **5 richieste, al massimo 1.900 token di completamento**. Per un'esecuzione più piccola, esegui solo `exit`: 3 richieste / 900 token, ma ciò non esercita l'inferenza interattiva.

**2. Entrambi i flussi di lavoro di chiamata funzione:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Controlla entrambi i nomi delle funzioni, il meteo simulato di Seattle, il risultato calcolato 36, due risposte finali, e il codice di uscita 0. Budget: **4 richieste, al massimo 1.200 token di completamento**.

**3. Risposta basata sul documento:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Controlla il percorso del documento, una risposta che menzioni Microsoft Entra ID, e il codice di uscita 0. Budget: **1 richiesta, al massimo 500 token di completamento**. Il file [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) esistente è l'unico file di input richiesto. Un secondo esecuzione opzionale che domanda su un argomento assente dovrebbe astenersi e aggiunge una richiesta / 500 token.

**4. Osservazioni su AI responsabile:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Controlla sei categorie e il riepilogo delle osservazioni, revisiona il contenuto generato, e richiedi codice di uscita 0 per il completamento tecnico. L'uscita di processo riuscita non certifica la sicurezza del modello. Budget: **6 richieste, al massimo 1.800 token di completamento**.

**Totale per i quattro comandi: 16 richieste chat e al massimo 5.400 token di completamento**, più token di input (inclusa la conversazione ripetuta e lo schema/storia dello strumento). Non ci sono richieste di embedding. L'effettivo uso di token dipende dal modello e può essere inferiore, specialmente per prompt filtrati. Il costo in dollari dipende dal prezzo del deployment; non è implicata alcuna stima monetaria fissa. Tutti i limiti di richieste presumono nessuna riesecuzione manuale. Controlla `$LASTEXITCODE` immediatamente dopo ogni comando; un valore diverso da zero significa che l'esecuzione non è completata con successo.

## Risoluzione dei problemi

- **Endpoint mancante / 401 / 403:** Imposta l'endpoint nel processo di avvio, verifica il tuo accesso locale ad Azure e il ruolo con ambito risorsa, e controlla che non ci siano override di identità involontari.
- **400 / 404:** Conferma che il deployment esista e supporti le Chat Completions con sforzo di ragionamento `none`. Usa la risorsa HTTPS root o URL `/openai/v1`, non un URL di deployment legacy. Errori 400 ordinari sono fallimenti tecnici, non blocchi di sicurezza.
- **429:** Coordina il RPM condiviso e la quota di token prima di riprovare. Gli esempi deliberatamente non effettuano un nuovo tentativo automatico.
- **`Incomplete chat response: length`:** L'output ha raggiunto il limite di completamento. Rivedi la risposta e il prompt prima di aumentare il limite e il relativo budget documentato; non registrare un'esecuzione troncata come riuscita.
- **Errori di file o stdin:** Avvia da una directory supportata o passa un percorso documento esplicito. Fornisci una domanda di lettore non vuota. Le completions possono terminare normalmente su EOF o `exit`.
- **Errori di compilazione:** Verifica Java 21 o successivo, quindi esegui `mvn -B -ntp clean test`. In PowerShell, cita l'intero argomento Maven contenente una proprietà con punto, per esempio `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Prossimi passi

Continua con [Capitolo 4: Esempi pratici](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire la precisione, si prega di notare che le traduzioni automatizzate possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->