# MCP Rechner Tutorial für Einsteiger

## Inhaltsverzeichnis

- [Was Sie lernen werden](#was-sie-lernen-werden)
- [Voraussetzungen](#voraussetzungen)
- [Abhängigkeitsversionen](#abhängigkeitsversionen)
- [Verstehen der Projektstruktur](#verstehen-der-projektstruktur)
- [Erklärung der Kernkomponenten](#erklärung-der-kernkomponenten)
  - [1. Hauptanwendung](#1-hauptanwendung)
  - [2. Rechner-Service](#2-rechner-service)
  - [3. Direkter MCP-Client](#3-direkter-mcp-client)
  - [4. KI-gestützter Client](#4-ki-gestützter-client)
- [Ausführen der Beispiele](#ausführen-der-beispiele)
- [Offline-Tests](#offline-tests)
- [Wie alles zusammenarbeitet](#wie-alles-zusammenarbeitet)
- [Nächste Schritte](#nächste-schritte)

## Was Sie lernen werden

Dieses Tutorial erklärt, wie man einen Rechner-Service mit dem Model Context Protocol (MCP) erstellt. Sie werden verstehen:

- Wie man einen Service erstellt, den KI als Werkzeug nutzen kann
- Wie man direkte Kommunikation mit MCP-Services einrichtet
- Wie KI-Modelle automatisch entscheiden, welche Werkzeuge sie nutzen
- Der Unterschied zwischen direkten Protokollaufrufen und KI-unterstützten Interaktionen

## Voraussetzungen

Bevor Sie starten, stellen Sie sicher, dass Sie Folgendes haben:
- Java 21 oder höher installiert
- Maven für das Abhängigkeitsmanagement
- Grundkenntnisse in Java und Spring Boot

Nur die KI-Clients benötigen eine Azure OpenAI-Deployment und eine authentifizierte `DefaultAzureCredential`,
wie z.B. ein bestehendes Azure CLI-Login lokal oder eine Managed Identity in Azure. Die Identität benötigt
die Rolle Cognitive Services OpenAI User auf der Ressource. Siehe [Kapitel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Der Server, der direkte SDK-Client und alle automatisierten Tests benötigen kein Azure-Konto oder Modellzugriff.

## Abhängigkeitsversionen

Release-Abhängigkeiten überprüft am 14.09.2026:

| Abhängigkeit | Version |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (von Spring AI verwaltet) | 2.0.0 |
| LangChain4j / Kern | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j offizieller OpenAI-Adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-managed) | 6.0.3 |

Die MCP- und offiziellen OpenAI-Adapter sind veröffentlichte Betaversionen in Maven Central, keine Snapshots.
Ihre Versionen unterscheiden sich vom LangChain4j-Kern. Es werden keine Snapshot- oder Meilenstein-Repositories benötigt.
Client-only Abhängigkeiten haben Testscope, da die ausführbaren Beispiele unter `src/test/java` liegen.

## Verstehen der Projektstruktur

Das Rechner-Projekt hat mehrere wichtige Dateien:

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

## Erklärung der Kernkomponenten

### 1. Hauptanwendung

**Datei:** `McpServerApplication.java`

Dies ist der Einstiegspunkt unseres Rechner-Services. Es ist eine normale Spring Boot Anwendung mit einer besonderen Ergänzung:

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
- Startet einen Spring Boot Webserver auf Port 8080
- Erstellt einen `ToolCallbackProvider`, der unsere Rechner-Methoden als MCP-Werkzeuge verfügbar macht
- Die `@Bean` Annotation teilt Spring mit, diese Komponente zu verwalten, damit andere Teile sie nutzen können

### 2. Rechner-Service

**Datei:** `CalculatorService.java`

Hier finden alle mathematischen Berechnungen statt. Jede Methode ist mit `@Tool` markiert, um sie über MCP verfügbar zu machen:

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
    
    // Weitere Taschenrechneroperationen...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Wichtigste Merkmale:**

1. **`@Tool` Annotation**: Signalisiert MCP, dass diese Methode von externen Clients aufgerufen werden kann
2. **Klare Beschreibungen**: Jedes Werkzeug hat eine Beschreibung, die KI-Modellen hilft zu verstehen, wann es genutzt werden soll
3. **Konsistentes Rückgabeformat**: Alle Operationen geben menschenlesbare Strings zurück wie "5.00 + 3.00 = 8.00"
4. **Fehlerbehandlung**: Division durch Null und negative Quadratwurzeln liefern Fehlermeldungen

**Verfügbare Operationen:**
- `add(a, b)` - Addiert zwei Zahlen
- `subtract(a, b)` - Subtrahiert die zweite von der ersten Zahl
- `multiply(a, b)` - Multipliziert zwei Zahlen
- `divide(a, b)` - Dividiert die erste durch die zweite Zahl (mit Nullprüfung)
- `power(base, exponent)` - Hebt die Basis auf die Potenz des Exponenten
- `squareRoot(number)` - Berechnet die Quadratwurzel (mit Negativprüfung)
- `modulus(a, b)` - Gibt den Rest der Division zurück
- `absolute(number)` - Gibt den Absolutwert zurück
- `help()` - Gibt Informationen zu allen Operationen zurück

### 3. Direkter MCP-Client

Siehe [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Dieser Client verwendet `HttpClientStreamableHttpTransport` unter `/mcp`, initialisiert die Verbindung,
pingt den Server und folgt der Seitenaufteilung der Werkzeugliste. Er prüft, dass alle neun erwarteten Werkzeuge
existieren und ruft jedes auf, inklusive `modulus` und `help`, ohne ein KI-Modell.

Der aktuelle Request-Builder sieht so aus:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokollfehler führen zum Fehlschlag des Clients anstatt einer irreführenden Erfolgsmeldung. Der MCP-Client
wird mit try-with-resources geschlossen, auch wenn die Entdeckung oder ein Werkzeugaufruf fehlschlägt.

### 4. KI-gestützter Client

Siehe [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
und [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementiert die aktuelle LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` verbindet es mit dem gleichen `/mcp` Endpunkt wie der SDK-Client.
`AiServices` entdeckt die Werkzeuge und verwaltet das Gespräch für Werkzeugaufrufe/-ergebnisse.

Die Standard-Deployment ist **GPT-5.6 Luna**, mit explizit deaktivierter Argumentation:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Diese Voreinstellungen gelten für jede Komplettierung, inklusive Folgeanfragen nach Werkzeugausführung.
Der Client verwendet ein aktualisierbares `BearerTokenCredential`, unterstützt von `DefaultAzureCredential`
und dem Scope `https://ai.azure.com/.default`, nicht ein Einmal-Token als API-Schlüssel.
Ressourcen-URLs und URLs, die bereits mit `/openai/v1` enden, werden akzeptiert.

Der Bot behält einen begrenzten Gesprächsverlauf, gibt `Tool executed: ...` mit dem tatsächlichen
MCP-Ergebnis aus und schlägt fehl, wenn eine Antwort Werkzeuge überspringt. Werkzeug-Schleifen sind auf vier Durchläufe begrenzt.
Authentifizierungs-, Modell-, MCP- und Werkzeugfehler werden weitergegeben; automatische Modell-Wiederholungen sind deaktiviert.
Sowohl der MCP Transport/Client als auch der offizielle OpenAI-Client werden bei Erfolg oder Fehler geschlossen.

## Ausführen der Beispiele

### Schritt 1: Starten Sie den Rechner-Server

Azure-Konfiguration ist für den Server nicht erforderlich. Die folgenden Befehle werden aus dem Verzeichnis dieses Beispiels ausgeführt.
Das Beispiel verwendet Port **18081**, um Konflikte mit einem anderen Beispiel zu vermeiden; der Standard bleibt 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Der MCP-Endpunkt ist `http://localhost:18081/mcp`. Gesundheits- und Entdeckungsinformationen sind unter
`http://localhost:18081/health` und `http://localhost:18081/info` zu finden.
Streamable HTTP ersetzt den alten nur SSE-Transport; `/sse` und `/v1/tools` sind keine Endpunkte.

### Schritt 2: Test mit direktem Client

In einem anderen PowerShell-Terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Es sind keine Eingaben erforderlich. Alle neun Werkzeuge werden ausgeführt. Erwartete Rechenergebnisse sind
8, 6, 42, 5, 256, 4, 2 und 5,5, gefolgt vom Hilfetext.

### Schritt 3: Test mit KI-Client

Nach der Authentifizierung wie unter den Voraussetzungen beschrieben, konfigurieren Sie den KI-Client im gleichen Terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Erwarten Sie eine Zeile `Tool executed: add` mit `41.80`, gefolgt von der Antwort des Modells.
Der Einzeleingabe-Modus beendet sich ohne Eingabeaufforderung. Um die ursprüngliche Vier-Eingabe-Demo auszuführen:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Die Demo ruft `add`, `squareRoot`, `help` und die verknüpfte Operation `power` gefolgt von `divide` auf.
Erwartete numerische Antworten sind 41,8, 12 und 64. Das Weglassen von Argumenten führt ebenfalls zu dieser Demo.

### Schritt 4: Führen Sie den interaktiven Bot aus

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Geben Sie `Multipliziere 6 mit 7 unter Verwendung des Rechner-Services` ein, danach `exit` oder `quit`.
Erwarten Sie ein tatsächliches Werkzeugergebnis von 42 für `multiply`. Leere Zeilen werden ignoriert; EOF beendet ebenfalls die Sitzung.
Für einen nicht-interaktiven Schnelltest dieses Einstiegspunkts:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Beide KI-Einstiegspunkte akzeptieren `--prompt "question"`, `--demo` und `--interactive`.
Ungültige Optionen führen zu Fehlern vor Verbindungsaufnahme. Jedes Maven `-D...` Argument ist für PowerShell vollständig zu zitieren.
In Bash verwenden Sie stattdessen `export NAME=value` anstelle von `$env:NAME = "value"`.

**Kontingent:** Führen Sie KI-Beispiele nacheinander aus. Eine einfache Eingabe benötigt normalerweise zwei Modellanfragen;
die vollständige Demo normalerweise neun, inklusive Folgeanfragen mit Werkzeugergebnissen. Bei einer gemeinsamen 10 RPM
Bereitstellung warten Sie ein neues Kontingentfenster vor dem nächsten KI-Durchlauf. Ein 429 Fehler schlägt sichtbar fehl ohne
automatische Wiederholungen; folgen Sie den Retry-After-Anweisungen des Dienstes. Tatsächliche Anfragenzahlen hängen vom Modell ab.
Offline-Tests verbrauchen kein Kontingent und testen nicht die Verfügbarkeit oder Antwortqualität von Luna live.

### Konfiguration und Shutdown

| Einstellung | Standard / Verhalten |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; Basis-URL, ohne `/mcp` |
| `-Dmcp.server.url=...` | Überschreibt `MCP_SERVER_URL` für alle Clients |
| `AZURE_OPENAI_ENDPOINT` | Nur für KI-Clients erforderlich; Ressourcen-URL oder `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Name des Azure-Deployments |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positive ganze Zahl |
| Reasoning effort | Immer `none`, auch bei Werkzeug-Schleifenfolgeanfragen |

Ein überschriebenes Deployment muss `reasoning_effort=none` und `max_completion_tokens` unterstützen.
Die Clients lesen nicht automatisch eine `.env`-Datei ein. Stoppen Sie den Server mit `Ctrl+C` nach den Tests.
Clients beenden sich normal ohne `System.exit` oder Shutdown-Wartezeiten.

## Offline-Tests

```powershell
mvn -B -ntp clean verify
```

Alle Tests sind bezüglich Azure offline: Die Protokoll-Suite startet einen Spring-Server und
einen OpenAI-kompatiblen Stub auf zufälligen Loopback-Ports, schließt diese dann wieder. Maven muss evtl. noch
Abhängigkeiten herunterladen. Keine Zugangsdaten, keine live Bereitstellung oder MCP-Server sind nötig.

- Rechner-Unit-Tests decken alle arithmetischen Operationen, Dezimalergebnisse, Hilfe und Domain-Fehler ab.
- MCP-Tests prüfen Initialisierung, Entdeckung, alle neun Werkzeugaufrufe, Werkzeugfehler und Gesundheits-/Info-Endpunkte.
- Die KI-Protokolltests führen die vollständige Demo und den interaktiven Bot gegen den echten Rechner aus,
  prüfen, dass Werkzeugergebnisse in die nächste Komplettierung einfließen, und inspizieren jeden HTTP-Body auf Luna,
  `reasoning_effort: "none"` und `max_completion_tokens` ohne Legacy-`max_tokens`.
- Konfigurations-/Eingabetests umfassen Deployment- und Endpunkt-Überschreibungen, leere Zeilen, EOF, exit/quit,
  Einzeleingabe-Modus, ungültige Optionen und Fehlerweitergabe. Kontingent-Tests zeigen, dass 429 nicht erneut versucht wird.

## Wie alles zusammenarbeitet

Hier ist der vollständige Ablauf, wenn Sie die KI fragen: „Was ist 5 + 3?“:

1. **Sie** fragen die KI in natürlicher Sprache
2. **KI** analysiert Ihre Anfrage und erkennt, dass Sie Addition wollen
3. **KI** ruft den MCP-Server auf: `add(5.0, 3.0)`
4. **Rechner-Service** führt aus: `5.0 + 3.0 = 8.0`
5. **Rechner-Service** gibt zurück: `"5.00 + 3.00 = 8.00"`
6. **KI** erhält das Ergebnis und formatiert eine natürliche Antwort
7. **Sie** erhalten: „Die Summe von 5 und 3 ist 8“

## Nächste Schritte

Für weitere Beispiele siehe [Kapitel 04: Praktische Beispiele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->