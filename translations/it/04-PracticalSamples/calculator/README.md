# Tutorial del Calcolatore MCP per Principianti

## Indice

- [Cosa Imparerai](#cosa-imparerai)
- [Prerequisiti](#prerequisiti)
- [Versioni delle Dipendenze](#versioni-delle-dipendenze)
- [Comprendere la Struttura del Progetto](#comprendere-la-struttura-del-progetto)
- [Componenti Principali Spiegati](#componenti-principali-spiegati)
  - [1. Applicazione Principale](#1-applicazione-principale)
  - [2. Servizio Calcolatore](#2-servizio-calcolatore)
  - [3. Client MCP Diretto](#3-client-mcp-diretto)
  - [4. Client Alimentato da AI](#4-client-alimentato-da-ai)
- [Esecuzione degli Esempi](#esecuzione-degli-esempi)
- [Test Offline](#test-offline)
- [Come Funziona Tutto Insieme](#come-funziona-tutto-insieme)
- [Passi Successivi](#passi-successivi)

## Cosa Imparerai

Questo tutorial spiega come costruire un servizio di calcolatore utilizzando il Model Context Protocol (MCP). Capirai:

- Come creare un servizio che l'AI può usare come strumento
- Come impostare una comunicazione diretta con i servizi MCP
- Come i modelli AI possono scegliere automaticamente quali strumenti utilizzare
- La differenza tra chiamate dirette al protocollo e interazioni assistite da AI

## Prerequisiti

Prima di iniziare, assicurati di avere:
- Java 21 o superiore installato
- Maven per la gestione delle dipendenze
- Conoscenze di base di Java e Spring Boot

Solo i client AI richiedono un’installazione Azure OpenAI e una `DefaultAzureCredential` autenticata,
come un accesso Azure CLI esistente localmente o un’identità gestita in Azure. L’identità necessita
del ruolo utente Cognitive Services OpenAI sulla risorsa. Vedi [Capitolo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Il server, il client SDK diretto e tutti i test automatizzati non necessitano di un account Azure o accesso a modelli.

## Versioni delle Dipendenze

Dipendenze di rilascio verificate al 14-09-2026:

| Dipendenza | Versione |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (gestito da Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Adattatore ufficiale LangChain4j OpenAI | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (gestito da Boot) | 6.0.3 |

Gli adattatori MCP e OpenAI ufficiali sono rilasci beta pubblicati in Maven Central, non snapshot.
Le loro versioni differiscono dal core LangChain4j. Non sono necessari repository snapshot o milestone.
Le dipendenze solo per client hanno scope test perché gli esempi eseguibili si trovano sotto `src/test/java`.

## Comprendere la Struttura del Progetto

Il progetto calcolatore contiene diversi file importanti:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Componenti Principali Spiegati

### 1. Applicazione Principale

**File:** `McpServerApplication.java`

Questo è il punto d’ingresso del nostro servizio calcolatore. È un’applicazione Spring Boot standard con un’aggiunta speciale:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Cosa fa:**
- Avvia un server web Spring Boot sulla porta 8080
- Crea un `ToolCallbackProvider` che rende i nostri metodi di calcolatore disponibili come strumenti MCP
- L’annotazione `@Bean` dice a Spring di gestirlo come componente utilizzabile da altre parti

### 2. Servizio Calcolatore

**File:** `CalculatorService.java`

Qui avviene tutta la matematica. Ogni metodo è marcato con `@Tool` per renderlo disponibile tramite MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Altre operazioni della calcolatrice...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Caratteristiche principali:**

1. **Annotazione `@Tool`**: Dice a MCP che questo metodo può essere chiamato da client esterni
2. **Descrizioni Chiare**: Ogni strumento ha una descrizione che aiuta i modelli AI a capire quando usarlo
3. **Formato di ritorno coerente**: Tutte le operazioni restituiscono stringhe leggibili come "5.00 + 3.00 = 8.00"
4. **Gestione degli errori**: Divisione per zero e radice quadrata negativa restituiscono messaggi di errore

**Operazioni Disponibili:**
- `add(a, b)` - Somma due numeri
- `subtract(a, b)` - Sottrae il secondo dal primo
- `multiply(a, b)` - Moltiplica due numeri
- `divide(a, b)` - Divide il primo per il secondo (con controllo zero)
- `power(base, exponent)` - Eleva la base alla potenza dell’esponente
- `squareRoot(number)` - Calcola la radice quadrata (con controllo negativo)
- `modulus(a, b)` - Restituisce il resto della divisione
- `absolute(number)` - Restituisce il valore assoluto
- `help()` - Restituisce informazioni su tutte le operazioni

### 3. Client MCP Diretto

Vedi [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Questo client usa `HttpClientStreamableHttpTransport` su `/mcp`, inizializza la connessione,
fa il ping al server e segue la paginazione della lista strumenti. Controlla che esistano tutti e nove gli strumenti previsti
e chiama ognuno di essi, inclusi `modulus` e `help`, senza un modello AI.

Il costruttore di richieste attuale è così:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Gli errori di protocollo fanno fallire il client invece di stampare un successo ingannevole. Il client MCP
viene chiuso con try-with-resources, anche in caso di fallimento di discovery o di chiamata a uno strumento.

### 4. Client Alimentato da AI

Vedi [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
e [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementa l’API corrente LangChain4j `ChatModel`.
`StreamableHttpMcpTransport` lo collega allo stesso endpoint `/mcp` del client SDK.
`AiServices` scopre gli strumenti e gestisce la conversazione di chiamata/risultato dello strumento.

Il deployment predefinito è **GPT-5.6 Luna**, con ragionamento esplicitamente disabilitato:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Questi valori predefiniti si applicano a ogni completamento, incluse le risposte successive dopo l’esecuzione dello strumento.
Il client usa una `BearerTokenCredential` aggiornabile supportata da `DefaultAzureCredential`
e lo scope `https://ai.azure.com/.default`, non un token one-time passato come chiave API.
Sia gli URL risorsa sia gli URL che terminano già con `/openai/v1` sono accettati.

Il bot mantiene una cronologia della conversazione limitata, stampa `Tool executed: ...` con il
risultato MCP reale e fallisce se una risposta salta gli strumenti. I loop degli strumenti sono limitati a quattro round trip.
Gli errori di autenticazione, modello, MCP e strumenti si propagano; i retry automatici dei modelli sono disabilitati.
Sia il trasporto/client MCP che il client OpenAI ufficiale vengono chiusi in caso di successo o fallimento.

## Esecuzione degli Esempi

### Passo 1: Avviare il Server Calcolatore

Non è necessaria alcuna configurazione Azure per il server. I comandi sotto vanno eseguiti dalla directory di questo esempio.
L’esempio usa la porta **18081** per evitare conflitti con un altro esempio; la porta predefinita rimane 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

L’endpoint MCP è `http://localhost:18081/mcp`. Le informazioni di salute e discovery sono a
`http://localhost:18081/health` e `http://localhost:18081/info`.
HTTP Streamable sostituisce il trasporto vecchio solo SSE; `/sse` e `/v1/tools` non sono endpoint.

### Passo 2: Test con il Client Diretto

In un altro terminale PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Non è necessario alcun input. Vengono testati tutti e nove gli strumenti. I risultati aritmetici attesi includono
8, 6, 42, 5, 256, 4, 2 e 5.5, seguiti dal testo di aiuto.

### Passo 3: Test con il Client AI

Dopo l’autenticazione come descritto nei prerequisiti, configura il client AI nello stesso terminale:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Aspettati una riga `Tool executed: add` con `41.80`, seguita dalla risposta del modello.
La modalità single-prompt esce senza aspettare input. Per eseguire la demo originale con quattro prompt:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

La demo chiama `add`, `squareRoot`, `help` e l’operazione concatenata `power` poi `divide`.
Le risposte numeriche attese sono 41.8, 12 e 64. Anche omettere gli argomenti esegue questa demo.

### Passo 4: Eseguire il Bot Interattivo

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Inserisci `Moltiplica 6 per 7 usando il servizio calcolatore`, poi `exit` o `quit`.
Aspettati un vero risultato dello strumento `multiply` pari a 42. Le righe vuote sono ignorate; EOF termina anche la sessione.
Per un test smoke non interattivo di questo entrypoint:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Entrambe le entrypoint AI accettano `--prompt "question"`, `--demo` e `--interactive`.
Le opzioni invalide falliscono prima di aprire una connessione. Ogni argomento Maven `-D...` è completamente quotato
per PowerShell. In Bash, usa `export NAME=value` invece di `$env:NAME = "value"`.

**Quota:** Esegui gli esempi AI in sequenza. Un prompt semplice normalmente richiede due richieste modello;
la demo completa normalmente ne richiede nove, inclusi i follow-up dei risultati degli strumenti. Con un deployment condiviso 10 RPM,
attendi una nuova finestra di quota prima della prossima esecuzione AI. Un errore 429 fallisce visibilmente senza
retry automatici; segui la guida retry-after del servizio. Il numero effettivo di richieste dipende dal modello.
I test offline non consumano quota e non stabiliscono disponibilità live di Luna o qualità delle risposte.

### Configurazione e Arresto

| Impostazione | Default / comportamento |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL base, senza `/mcp` |
| `-Dmcp.server.url=...` | Sovrascrive `MCP_SERVER_URL` per tutti i client |
| `AZURE_OPENAI_ENDPOINT` | Richiesto solo per client AI; URL risorsa o URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nome di deployment Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; intero positivo |
| Sforzo di ragionamento | Sempre `none`, inclusi follow-up ciclo strumenti |

Un deployment sovrascritto deve supportare `reasoning_effort=none` e `max_completion_tokens`.
I client non leggono automaticamente un file `.env`. Ferma il server con `Ctrl+C` dopo il test.
I client tornano normalmente senza `System.exit` o sospensioni di arresto.

## Test Offline

```powershell
mvn -B -ntp clean verify
```

Tutti i test sono offline rispetto ad Azure: la suite di protocollo avvia un server Spring e
uno stub compatibile OpenAI su porte loopback casuali, poi li chiude. Maven potrebbe ancora dover
scaricare dipendenze. Non vengono usate credenziali, deployment live o server MCP preesistenti.

- I test unitari del calcolatore coprono tutte le operazioni aritmetiche, risultati decimali, aiuto ed errori di dominio.
- I test MCP coprono inizializzazione, discovery, tutte e nove le chiamate agli strumenti, fallimenti e health/info.
- I test di protocollo AI eseguono la demo completa e il Bot interattivo contro il calcolatore reale,
  verificano che i risultati degli strumenti alimentino il completamento successivo, e ispezionano ogni corpo HTTP per Luna,
  `reasoning_effort: "none"` e `max_completion_tokens` senza `max_tokens` legacy.
- I test di configurazione/input coprono sovrascritture di deployment e endpoint, righe vuote, EOF, exit/quit,
  modalità single-prompt, opzioni invalide e propagazione errori. I test quota dimostrano che il 429 non viene ritentato.

## Come Funziona Tutto Insieme

Ecco il flusso completo quando chiedi all'AI "Quanto fa 5 + 3?":

1. **Tu** chiedi all’AI in linguaggio naturale
2. **AI** analizza la tua richiesta e capisce che vuoi una somma
3. **AI** chiama il server MCP: `add(5.0, 3.0)`
4. **Servizio Calcolatore** esegue: `5.0 + 3.0 = 8.0`
5. **Servizio Calcolatore** restituisce: `"5.00 + 3.00 = 8.00"`
6. **AI** riceve il risultato e costruisce una risposta naturale
7. **Tu** ricevi: "La somma di 5 e 3 è 8"

## Passi Successivi

Per ulteriori esempi, vedi [Capitolo 04: Esempi pratici](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire la precisione, si prega di notare che le traduzioni automatizzate possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->