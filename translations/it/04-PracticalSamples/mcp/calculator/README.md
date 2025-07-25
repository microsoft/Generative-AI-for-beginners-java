<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:25:15+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "it"
}
-->
# Tutorial del Calcolatore MCP per Principianti

## Indice

- [Cosa Imparerai](../../../../../04-PracticalSamples/mcp/calculator)
- [Prerequisiti](../../../../../04-PracticalSamples/mcp/calculator)
- [Comprendere la Struttura del Progetto](../../../../../04-PracticalSamples/mcp/calculator)
- [Componenti Principali Spiegati](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Applicazione Principale](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Servizio Calcolatore](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Client MCP Diretto](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Client Basato su AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Esecuzione degli Esempi](../../../../../04-PracticalSamples/mcp/calculator)
- [Come Funziona Tutto Insieme](../../../../../04-PracticalSamples/mcp/calculator)
- [Prossimi Passi](../../../../../04-PracticalSamples/mcp/calculator)

## Cosa Imparerai

Questo tutorial spiega come costruire un servizio calcolatore utilizzando il Model Context Protocol (MCP). Capirai:

- Come creare un servizio che l'AI può utilizzare come strumento
- Come configurare una comunicazione diretta con i servizi MCP
- Come i modelli AI possono scegliere automaticamente quali strumenti utilizzare
- La differenza tra chiamate dirette al protocollo e interazioni assistite dall'AI

## Prerequisiti

Prima di iniziare, assicurati di avere:
- Java 21 o superiore installato
- Maven per la gestione delle dipendenze
- Un account GitHub con un personal access token (PAT)
- Conoscenze di base di Java e Spring Boot

## Comprendere la Struttura del Progetto

Il progetto del calcolatore contiene diversi file importanti:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## Componenti Principali Spiegati

### 1. Applicazione Principale

**File:** `McpServerApplication.java`

Questo è il punto di ingresso del nostro servizio calcolatore. È un'applicazione Spring Boot standard con un'aggiunta speciale:

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
- Crea un `ToolCallbackProvider` che rende i metodi del calcolatore disponibili come strumenti MCP
- L'annotazione `@Bean` dice a Spring di gestirlo come un componente utilizzabile da altre parti

### 2. Servizio Calcolatore

**File:** `CalculatorService.java`

Qui avvengono tutti i calcoli. Ogni metodo è contrassegnato con `@Tool` per renderlo disponibile tramite MCP:

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Caratteristiche principali:**

1. **Annotazione `@Tool`**: Indica a MCP che questo metodo può essere chiamato da client esterni
2. **Descrizioni Chiare**: Ogni strumento ha una descrizione che aiuta i modelli AI a capire quando usarlo
3. **Formato di Ritorno Consistente**: Tutte le operazioni restituiscono stringhe leggibili come "5.00 + 3.00 = 8.00"
4. **Gestione degli Errori**: Divisioni per zero e radici quadrate di numeri negativi restituiscono messaggi di errore

**Operazioni Disponibili:**
- `add(a, b)` - Somma due numeri
- `subtract(a, b)` - Sottrae il secondo dal primo
- `multiply(a, b)` - Moltiplica due numeri
- `divide(a, b)` - Divide il primo per il secondo (con controllo dello zero)
- `power(base, exponent)` - Eleva la base alla potenza dell'esponente
- `squareRoot(number)` - Calcola la radice quadrata (con controllo dei numeri negativi)
- `modulus(a, b)` - Restituisce il resto della divisione
- `absolute(number)` - Restituisce il valore assoluto
- `help()` - Restituisce informazioni su tutte le operazioni

### 3. Client MCP Diretto

**File:** `SDKClient.java`

Questo client comunica direttamente con il server MCP senza utilizzare l'AI. Chiama manualmente funzioni specifiche del calcolatore:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Cosa fa:**
1. **Si connette** al server del calcolatore su `http://localhost:8080`
2. **Elenca** tutti gli strumenti disponibili (le funzioni del calcolatore)
3. **Chiama** funzioni specifiche con parametri esatti
4. **Stampa** direttamente i risultati

**Quando usarlo:** Quando sai esattamente quale calcolo vuoi eseguire e desideri chiamarlo programmaticamente.

### 4. Client Basato su AI

**File:** `LangChain4jClient.java`

Questo client utilizza un modello AI (GPT-4o-mini) che può decidere automaticamente quali strumenti del calcolatore utilizzare:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Cosa fa:**
1. **Crea** una connessione al modello AI utilizzando il tuo token GitHub
2. **Si connette** al nostro server MCP del calcolatore
3. **Dà** all'AI accesso a tutti gli strumenti del calcolatore
4. **Permette** richieste in linguaggio naturale come "Calcola la somma di 24.5 e 17.3"

**L'AI automaticamente:**
- Capisce che vuoi sommare i numeri
- Sceglie lo strumento `add`
- Chiama `add(24.5, 17.3)`
- Restituisce il risultato in una risposta naturale

## Esecuzione degli Esempi

### Passo 1: Avviare il Server del Calcolatore

Per prima cosa, imposta il tuo token GitHub (necessario per il client AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Avvia il server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Il server si avvierà su `http://localhost:8080`. Dovresti vedere:
```
Started McpServerApplication in X.XXX seconds
```

### Passo 2: Testare con il Client Diretto

In un nuovo terminale:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Vedrai un output simile a:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Passo 3: Testare con il Client AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Vedrai l'AI utilizzare automaticamente gli strumenti:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Come Funziona Tutto Insieme

Ecco il flusso completo quando chiedi all'AI "Quanto fa 5 + 3?":

1. **Tu** chiedi all'AI in linguaggio naturale
2. **L'AI** analizza la tua richiesta e capisce che vuoi sommare
3. **L'AI** chiama il server MCP: `add(5.0, 3.0)`
4. **Il Servizio Calcolatore** esegue: `5.0 + 3.0 = 8.0`
5. **Il Servizio Calcolatore** restituisce: `"5.00 + 3.00 = 8.00"`
6. **L'AI** riceve il risultato e lo formatta in una risposta naturale
7. **Tu** ottieni: "La somma di 5 e 3 è 8"

## Prossimi Passi

Per ulteriori esempi, consulta [Capitolo 04: Esempi pratici](../../README.md)

**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l'accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa dovrebbe essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un traduttore umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall'uso di questa traduzione.