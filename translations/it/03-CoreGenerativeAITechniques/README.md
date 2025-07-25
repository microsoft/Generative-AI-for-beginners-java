<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:24:11+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "it"
}
-->
# Tutorial sulle Tecniche Fondamentali di Generative AI

## Indice

- [Prerequisiti](../../../03-CoreGenerativeAITechniques)
- [Introduzione](../../../03-CoreGenerativeAITechniques)
  - [Passo 1: Imposta la Variabile d'Ambiente](../../../03-CoreGenerativeAITechniques)
  - [Passo 2: Naviga nella Directory degli Esempi](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: Completamenti e Chat con LLM](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Chiamata di Funzioni](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Generazione con Recupero di Informazioni)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: AI Responsabile](../../../03-CoreGenerativeAITechniques)
- [Schemi Comuni negli Esempi](../../../03-CoreGenerativeAITechniques)
- [Prossimi Passi](../../../03-CoreGenerativeAITechniques)
- [Risoluzione dei Problemi](../../../03-CoreGenerativeAITechniques)
  - [Problemi Comuni](../../../03-CoreGenerativeAITechniques)

## Panoramica

Questo tutorial offre esempi pratici sulle tecniche fondamentali di Generative AI utilizzando Java e i Modelli di GitHub. Imparerai a interagire con i Large Language Models (LLM), implementare chiamate di funzioni, utilizzare la generazione con recupero di informazioni (RAG) e applicare pratiche di AI responsabile.

## Prerequisiti

Prima di iniziare, assicurati di avere:
- Java 21 o superiore installato
- Maven per la gestione delle dipendenze
- Un account GitHub con un personal access token (PAT)

## Introduzione

### Passo 1: Imposta la Variabile d'Ambiente

Per prima cosa, devi impostare il tuo token GitHub come variabile d'ambiente. Questo token ti consente di accedere gratuitamente ai Modelli di GitHub.

**Windows (Prompt dei Comandi):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Passo 2: Naviga nella Directory degli Esempi

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: Completamenti e Chat con LLM

**File:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Cosa Insegna Questo Esempio

Questo esempio dimostra le meccaniche fondamentali dell'interazione con i Large Language Models (LLM) tramite l'API di OpenAI, inclusa l'inizializzazione del client con i Modelli di GitHub, i modelli di struttura dei messaggi per i prompt di sistema e utente, la gestione dello stato della conversazione tramite l'accumulo della cronologia dei messaggi e la regolazione dei parametri per controllare la lunghezza delle risposte e i livelli di creatività.

### Concetti Chiave del Codice

#### 1. Configurazione del Client
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Questo crea una connessione ai Modelli di GitHub utilizzando il tuo token.

#### 2. Completamento Semplice
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Memoria della Conversazione
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

L'AI ricorda i messaggi precedenti solo se li includi nelle richieste successive.

### Esegui l'Esempio
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Cosa Succede Quando Lo Esegui

1. **Completamento Semplice**: L'AI risponde a una domanda su Java con l'aiuto di un prompt di sistema
2. **Chat Multi-turno**: L'AI mantiene il contesto tra più domande
3. **Chat Interattiva**: Puoi avere una vera conversazione con l'AI

## Tutorial 2: Chiamata di Funzioni

**File:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Cosa Insegna Questo Esempio

La chiamata di funzioni consente ai modelli AI di richiedere l'esecuzione di strumenti e API esterni tramite un protocollo strutturato in cui il modello analizza richieste in linguaggio naturale, determina le chiamate di funzione necessarie con i parametri appropriati utilizzando definizioni JSON Schema e processa i risultati restituiti per generare risposte contestuali, mentre l'esecuzione effettiva delle funzioni rimane sotto il controllo dello sviluppatore per garantire sicurezza e affidabilità.

### Concetti Chiave del Codice

#### 1. Definizione della Funzione
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Questo indica all'AI quali funzioni sono disponibili e come utilizzarle.

#### 2. Flusso di Esecuzione della Funzione
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Implementazione della Funzione
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Esegui l'Esempio
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Cosa Succede Quando Lo Esegui

1. **Funzione Meteo**: L'AI richiede dati meteo per Seattle, tu li fornisci, l'AI formatta una risposta
2. **Funzione Calcolatrice**: L'AI richiede un calcolo (15% di 240), tu lo esegui, l'AI spiega il risultato

## Tutorial 3: RAG (Generazione con Recupero di Informazioni)

**File:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Cosa Insegna Questo Esempio

La Generazione con Recupero di Informazioni (RAG) combina il recupero di informazioni con la generazione di linguaggio iniettando il contesto di documenti esterni nei prompt dell'AI, consentendo ai modelli di fornire risposte accurate basate su fonti di conoscenza specifiche piuttosto che su dati di addestramento potenzialmente obsoleti o inaccurati, mantenendo chiari confini tra le domande degli utenti e le fonti di informazioni autorevoli tramite un'accurata ingegneria dei prompt.

### Concetti Chiave del Codice

#### 1. Caricamento dei Documenti
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Iniezione del Contesto
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Le triple virgolette aiutano l'AI a distinguere tra contesto e domanda.

#### 3. Gestione Sicura delle Risposte
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Valida sempre le risposte dell'API per evitare crash.

### Esegui l'Esempio
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Cosa Succede Quando Lo Esegui

1. Il programma carica `document.txt` (contiene informazioni sui Modelli di GitHub)
2. Fai una domanda sul documento
3. L'AI risponde basandosi solo sul contenuto del documento, non sulla sua conoscenza generale

Prova a chiedere: "Cosa sono i Modelli di GitHub?" rispetto a "Che tempo fa?"

## Tutorial 4: AI Responsabile

**File:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Cosa Insegna Questo Esempio

L'esempio di AI Responsabile mostra l'importanza di implementare misure di sicurezza nelle applicazioni AI. Dimostra filtri di sicurezza che rilevano categorie di contenuti dannosi, tra cui discorsi di odio, molestie, autolesionismo, contenuti sessuali e violenza, mostrando come le applicazioni AI in produzione dovrebbero gestire con grazia le violazioni delle politiche sui contenuti tramite una corretta gestione delle eccezioni, meccanismi di feedback per gli utenti e strategie di risposta alternative.

### Concetti Chiave del Codice

#### 1. Framework di Test della Sicurezza
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Categorie di Sicurezza Testate
- Istruzioni su violenza/danni
- Discorsi di odio
- Violazioni della privacy
- Disinformazione medica
- Attività illegali

### Esegui l'Esempio
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Cosa Succede Quando Lo Esegui

Il programma testa vari prompt dannosi e mostra come il sistema di sicurezza dell'AI:
1. **Blocca richieste pericolose** con errori HTTP 400
2. **Permette contenuti sicuri** di essere generati normalmente
3. **Protegge gli utenti** da output dannosi dell'AI

## Schemi Comuni negli Esempi

### Schema di Autenticazione
Tutti gli esempi utilizzano questo schema per autenticarsi con i Modelli di GitHub:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Schema di Gestione degli Errori
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Schema di Struttura dei Messaggi
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Prossimi Passi

[Capitolo 04: Esempi pratici](../04-PracticalSamples/README.md)

## Risoluzione dei Problemi

### Problemi Comuni

**"GITHUB_TOKEN non impostato"**
- Assicurati di aver impostato la variabile d'ambiente
- Verifica che il tuo token abbia l'ambito `models:read`

**"Nessuna risposta dall'API"**
- Controlla la tua connessione a Internet
- Verifica che il tuo token sia valido
- Controlla se hai superato i limiti di utilizzo

**Errori di compilazione Maven**
- Assicurati di avere Java 21 o superiore
- Esegui `mvn clean compile` per aggiornare le dipendenze

**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l'accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa dovrebbe essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un esperto umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall'uso di questa traduzione.