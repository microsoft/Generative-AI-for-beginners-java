<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-21T15:18:28+00:00",
  "source_file": "03-CoreGenerativeAITechniques/TUTORIAL.md",
  "language_code": "de"
}
-->
# Kerntechniken der Generativen KI – Tutorial

## Inhaltsverzeichnis

- [Voraussetzungen](../../../03-CoreGenerativeAITechniques)
- [Erste Schritte](../../../03-CoreGenerativeAITechniques)
  - [Schritt 1: Setzen Sie Ihre Umgebungsvariable](../../../03-CoreGenerativeAITechniques)
  - [Schritt 2: Navigieren Sie zum Beispiele-Verzeichnis](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: LLM-Vervollständigungen und Chat](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Funktionsaufrufe](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: Verantwortungsvolle KI](../../../03-CoreGenerativeAITechniques)
- [Gemeinsame Muster in den Beispielen](../../../03-CoreGenerativeAITechniques)
- [Nächste Schritte](../../../03-CoreGenerativeAITechniques)
- [Fehlerbehebung](../../../03-CoreGenerativeAITechniques)
  - [Häufige Probleme](../../../03-CoreGenerativeAITechniques)

## Überblick

Dieses Tutorial bietet praktische Beispiele zu den Kerntechniken der generativen KI mit Java und GitHub Models. Sie lernen, wie Sie mit Large Language Models (LLMs) interagieren, Funktionsaufrufe implementieren, Retrieval-Augmented Generation (RAG) nutzen und verantwortungsvolle KI-Praktiken anwenden.

## Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass Sie Folgendes haben:
- Java 21 oder höher installiert
- Maven für das Abhängigkeitsmanagement
- Ein GitHub-Konto mit einem persönlichen Zugriffstoken (PAT)

## Erste Schritte

### Schritt 1: Setzen Sie Ihre Umgebungsvariable

Zuerst müssen Sie Ihr GitHub-Token als Umgebungsvariable setzen. Dieses Token ermöglicht Ihnen den kostenlosen Zugriff auf GitHub Models.

**Windows (Eingabeaufforderung):**
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

### Schritt 2: Navigieren Sie zum Beispiele-Verzeichnis

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: LLM-Vervollständigungen und Chat

**Datei:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Was dieses Beispiel vermittelt

Dieses Beispiel zeigt die grundlegenden Mechanismen der Interaktion mit Large Language Models (LLMs) über die OpenAI-API, einschließlich der Initialisierung des Clients mit GitHub Models, der Strukturierung von Nachrichten für System- und Benutzeraufforderungen, der Verwaltung des Gesprächsverlaufs durch die Akkumulation von Nachrichten und der Feinabstimmung von Parametern zur Steuerung der Antwortlänge und Kreativität.

### Wichtige Codekonzepte

#### 1. Client-Setup
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Dies erstellt eine Verbindung zu GitHub Models mit Ihrem Token.

#### 2. Einfache Vervollständigung
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

#### 3. Gesprächsspeicher
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Die KI erinnert sich nur an vorherige Nachrichten, wenn Sie diese in nachfolgenden Anfragen einfügen.

### Beispiel ausführen
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Was passiert, wenn Sie es ausführen

1. **Einfache Vervollständigung**: Die KI beantwortet eine Java-Frage mit Hilfe einer Systemaufforderung.
2. **Mehrstufiger Chat**: Die KI behält den Kontext über mehrere Fragen hinweg bei.
3. **Interaktiver Chat**: Sie können ein echtes Gespräch mit der KI führen.

## Tutorial 2: Funktionsaufrufe

**Datei:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Was dieses Beispiel vermittelt

Funktionsaufrufe ermöglichen es KI-Modellen, externe Tools und APIs über ein strukturiertes Protokoll anzufordern. Das Modell analysiert natürliche Sprachaufforderungen, bestimmt erforderliche Funktionsaufrufe mit passenden Parametern anhand von JSON-Schema-Definitionen und verarbeitet die zurückgegebenen Ergebnisse, um kontextbezogene Antworten zu generieren. Die tatsächliche Funktionsausführung bleibt dabei unter der Kontrolle des Entwicklers, um Sicherheit und Zuverlässigkeit zu gewährleisten.

### Wichtige Codekonzepte

#### 1. Funktionsdefinition
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

Dies teilt der KI mit, welche Funktionen verfügbar sind und wie sie verwendet werden können.

#### 2. Ablauf der Funktionsausführung
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

#### 3. Funktionsimplementierung
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

### Beispiel ausführen
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Was passiert, wenn Sie es ausführen

1. **Wetterfunktion**: Die KI fordert Wetterdaten für Seattle an, Sie liefern diese, und die KI formatiert eine Antwort.
2. **Rechnerfunktion**: Die KI fordert eine Berechnung an (15 % von 240), Sie führen diese aus, und die KI erklärt das Ergebnis.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**Datei:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Was dieses Beispiel vermittelt

Retrieval-Augmented Generation (RAG) kombiniert Informationsabruf mit Sprachgenerierung, indem externe Dokumentenkontexte in KI-Aufforderungen eingebettet werden. Dadurch können Modelle präzise Antworten basierend auf spezifischen Wissensquellen liefern, anstatt auf potenziell veraltete oder ungenaue Trainingsdaten zurückzugreifen. Gleichzeitig werden klare Grenzen zwischen Benutzeranfragen und autoritativen Informationsquellen durch strategisches Prompt-Engineering gewahrt.

### Wichtige Codekonzepte

#### 1. Dokumentenladen
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Kontextinjektion
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

Die dreifachen Anführungszeichen helfen der KI, zwischen Kontext und Frage zu unterscheiden.

#### 3. Sichere Antwortverarbeitung
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API-Antworten sollten immer validiert werden, um Abstürze zu vermeiden.

### Beispiel ausführen
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Was passiert, wenn Sie es ausführen

1. Das Programm lädt `document.txt` (enthält Informationen über GitHub Models).
2. Sie stellen eine Frage zum Dokument.
3. Die KI antwortet ausschließlich basierend auf dem Dokumentinhalt, nicht auf ihrem allgemeinen Wissen.

Versuchen Sie: "Was sind GitHub Models?" vs. "Wie ist das Wetter?"

## Tutorial 4: Verantwortungsvolle KI

**Datei:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Was dieses Beispiel vermittelt

Das Beispiel zur verantwortungsvollen KI zeigt die Bedeutung von Sicherheitsmaßnahmen in KI-Anwendungen. Es demonstriert Sicherheitsfilter, die schädliche Inhaltskategorien wie Hassrede, Belästigung, Selbstverletzung, sexuelle Inhalte und Gewalt erkennen. Es wird gezeigt, wie produktionsreife KI-Anwendungen Verstöße gegen Inhaltsrichtlinien durch ordnungsgemäßes Ausnahmehandling, Benutzerfeedback-Mechanismen und Fallback-Strategien elegant handhaben können.

### Wichtige Codekonzepte

#### 1. Sicherheitsprüfungs-Framework
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

#### 2. Getestete Sicherheitskategorien
- Gewalt/Schadensanweisungen
- Hassrede
- Datenschutzverletzungen
- Medizinische Fehlinformationen
- Illegale Aktivitäten

### Beispiel ausführen
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Was passiert, wenn Sie es ausführen

Das Programm testet verschiedene schädliche Eingaben und zeigt, wie das KI-Sicherheitssystem:
1. **Gefährliche Anfragen blockiert** mit HTTP-400-Fehlern.
2. **Sichere Inhalte** normal generieren lässt.
3. **Benutzer schützt** vor schädlichen KI-Ausgaben.

## Gemeinsame Muster in den Beispielen

### Authentifizierungsmuster
Alle Beispiele verwenden dieses Muster, um sich bei GitHub Models zu authentifizieren:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Fehlerbehandlungsmuster
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Nachrichtenstrukturmuster
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Nächste Schritte

[Kapitel 04: Praktische Beispiele](../04-PracticalSamples/README.md)

## Fehlerbehebung

### Häufige Probleme

**"GITHUB_TOKEN nicht gesetzt"**
- Stellen Sie sicher, dass Sie die Umgebungsvariable gesetzt haben.
- Überprüfen Sie, ob Ihr Token den `models:read`-Bereich hat.

**"Keine Antwort von der API"**
- Überprüfen Sie Ihre Internetverbindung.
- Vergewissern Sie sich, dass Ihr Token gültig ist.
- Prüfen Sie, ob Sie die Ratenlimits erreicht haben.

**Maven-Kompilierungsfehler**
- Stellen Sie sicher, dass Sie Java 21 oder höher verwenden.
- Führen Sie `mvn clean compile` aus, um Abhängigkeiten zu aktualisieren.

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.