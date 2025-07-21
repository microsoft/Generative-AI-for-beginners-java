<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-21T15:34:58+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/TUTORIAL.md",
  "language_code": "de"
}
-->
# MCP-Rechner-Tutorial für Anfänger

## Inhaltsverzeichnis

- [Was Sie Lernen Werden](../../../../../04-PracticalSamples/mcp/calculator)
- [Voraussetzungen](../../../../../04-PracticalSamples/mcp/calculator)
- [Verständnis der Projektstruktur](../../../../../04-PracticalSamples/mcp/calculator)
- [Erläuterung der Kernkomponenten](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Hauptanwendung](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Rechnerdienst](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Direkter MCP-Client](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. KI-gestützter Client](../../../../../04-PracticalSamples/mcp/calculator)
- [Ausführen der Beispiele](../../../../../04-PracticalSamples/mcp/calculator)
- [Wie alles zusammenarbeitet](../../../../../04-PracticalSamples/mcp/calculator)
- [Nächste Schritte](../../../../../04-PracticalSamples/mcp/calculator)

## Was Sie Lernen Werden

Dieses Tutorial erklärt, wie man einen Rechnerdienst mit dem Model Context Protocol (MCP) erstellt. Sie werden verstehen:

- Wie man einen Dienst erstellt, den KI als Werkzeug nutzen kann
- Wie man direkte Kommunikation mit MCP-Diensten einrichtet
- Wie KI-Modelle automatisch entscheiden können, welche Werkzeuge sie verwenden
- Den Unterschied zwischen direkten Protokollaufrufen und KI-gestützten Interaktionen

## Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass Sie Folgendes haben:
- Java 21 oder höher installiert
- Maven für die Abhängigkeitsverwaltung
- Ein GitHub-Konto mit einem persönlichen Zugriffstoken (PAT)
- Grundlegendes Verständnis von Java und Spring Boot

## Verständnis der Projektstruktur

Das Rechnerprojekt enthält mehrere wichtige Dateien:

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

## Erläuterung der Kernkomponenten

### 1. Hauptanwendung

**Datei:** `McpServerApplication.java`

Dies ist der Einstiegspunkt unseres Rechnerdienstes. Es handelt sich um eine Standard-Spring-Boot-Anwendung mit einer besonderen Ergänzung:

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

**Was das macht:**
- Startet einen Spring-Boot-Webserver auf Port 8080
- Erstellt einen `ToolCallbackProvider`, der unsere Rechenmethoden als MCP-Werkzeuge verfügbar macht
- Die `@Bean`-Annotation weist Spring an, dies als Komponente zu verwalten, die von anderen Teilen verwendet werden kann

### 2. Rechnerdienst

**Datei:** `CalculatorService.java`

Hier findet die gesamte Mathematik statt. Jede Methode ist mit `@Tool` markiert, um sie über MCP verfügbar zu machen:

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

**Wichtige Merkmale:**

1. **`@Tool`-Annotation**: Diese zeigt MCP an, dass diese Methode von externen Clients aufgerufen werden kann
2. **Klare Beschreibungen**: Jedes Werkzeug hat eine Beschreibung, die KI-Modelle dabei unterstützt, zu verstehen, wann es verwendet werden soll
3. **Konsistentes Rückgabeformat**: Alle Operationen geben menschenlesbare Strings zurück, wie "5.00 + 3.00 = 8.00"
4. **Fehlerbehandlung**: Division durch Null und negative Quadratwurzeln geben Fehlermeldungen zurück

**Verfügbare Operationen:**
- `add(a, b)` - Addiert zwei Zahlen
- `subtract(a, b)` - Subtrahiert die zweite Zahl von der ersten
- `multiply(a, b)` - Multipliziert zwei Zahlen
- `divide(a, b)` - Teilt die erste Zahl durch die zweite (mit Null-Prüfung)
- `power(base, exponent)` - Erhöht die Basis auf die Potenz des Exponenten
- `squareRoot(number)` - Berechnet die Quadratwurzel (mit negativer Prüfung)
- `modulus(a, b)` - Gibt den Rest einer Division zurück
- `absolute(number)` - Gibt den Absolutwert zurück
- `help()` - Gibt Informationen zu allen Operationen zurück

### 3. Direkter MCP-Client

**Datei:** `SDKClient.java`

Dieser Client kommuniziert direkt mit dem MCP-Server, ohne KI zu verwenden. Er ruft manuell spezifische Rechnerfunktionen auf:

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

**Was das macht:**
1. **Verbindet** sich mit dem Rechner-Server unter `http://localhost:8080`
2. **Listet** alle verfügbaren Werkzeuge (unsere Rechnerfunktionen) auf
3. **Ruft** spezifische Funktionen mit genauen Parametern auf
4. **Gibt** die Ergebnisse direkt aus

**Wann das verwendet wird:** Wenn Sie genau wissen, welche Berechnung Sie durchführen möchten, und diese programmatisch aufrufen wollen.

### 4. KI-gestützter Client

**Datei:** `LangChain4jClient.java`

Dieser Client verwendet ein KI-Modell (GPT-4o-mini), das automatisch entscheiden kann, welche Rechnerwerkzeuge verwendet werden sollen:

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

**Was das macht:**
1. **Erstellt** eine Verbindung zum KI-Modell mit Ihrem GitHub-Token
2. **Verbindet** die KI mit unserem MCP-Rechner-Server
3. **Gibt** der KI Zugriff auf alle unsere Rechnerwerkzeuge
4. **Ermöglicht** natürliche Sprachbefehle wie "Berechne die Summe von 24,5 und 17,3"

**Die KI macht automatisch:**
- Versteht, dass Sie Zahlen addieren möchten
- Wählt das Werkzeug `add`
- Ruft `add(24.5, 17.3)` auf
- Gibt das Ergebnis in einer natürlichen Antwort zurück

## Ausführen der Beispiele

### Schritt 1: Starten Sie den Rechner-Server

Zuerst setzen Sie Ihr GitHub-Token (erforderlich für den KI-Client):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Starten Sie den Server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Der Server wird unter `http://localhost:8080` gestartet. Sie sollten Folgendes sehen:
```
Started McpServerApplication in X.XXX seconds
```

### Schritt 2: Testen Sie den direkten Client

In einem neuen Terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Sie werden eine Ausgabe wie diese sehen:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Schritt 3: Testen Sie den KI-Client

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Sie werden sehen, wie die KI automatisch Werkzeuge verwendet:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Wie alles zusammenarbeitet

Hier ist der vollständige Ablauf, wenn Sie die KI fragen: "Was ist 5 + 3?":

1. **Sie** fragen die KI in natürlicher Sprache
2. **Die KI** analysiert Ihre Anfrage und erkennt, dass Sie eine Addition möchten
3. **Die KI** ruft den MCP-Server auf: `add(5.0, 3.0)`
4. **Der Rechnerdienst** führt aus: `5.0 + 3.0 = 8.0`
5. **Der Rechnerdienst** gibt zurück: `"5.00 + 3.00 = 8.00"`
6. **Die KI** erhält das Ergebnis und formatiert eine natürliche Antwort
7. **Sie** erhalten: "Die Summe von 5 und 3 ist 8"

## Nächste Schritte

Für weitere Beispiele siehe [Kapitel 04: Praktische Beispiele](../../README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, weisen wir darauf hin, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Nutzung dieser Übersetzung entstehen.