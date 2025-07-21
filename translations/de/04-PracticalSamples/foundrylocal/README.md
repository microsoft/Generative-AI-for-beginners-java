<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a66dad62cdb2e141f05086feaf1a4a39",
  "translation_date": "2025-07-21T15:33:54+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "de"
}
-->
# Foundry Local Befehlszeilenanwendung

>**Hinweis**: Dieses Kapitel enthält ein [**Tutorial**](./TUTORIAL.md), das Sie durch das Ausführen der fertigen Beispiele führt.

Eine einfache Spring-Boot-Befehlszeilenanwendung, die zeigt, wie man sich mit Foundry Local über das OpenAI Java SDK verbindet.

## Was Sie lernen werden

- Wie man Foundry Local mit Spring-Boot-Anwendungen unter Verwendung des OpenAI Java SDK integriert
- Best Practices für lokale KI-Entwicklung und -Tests

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../../04-PracticalSamples/foundrylocal)
- [Voraussetzungen](../../../../04-PracticalSamples/foundrylocal)
  - [Installation von Foundry Local](../../../../04-PracticalSamples/foundrylocal)
  - [Verifizierung](../../../../04-PracticalSamples/foundrylocal)
- [Konfiguration](../../../../04-PracticalSamples/foundrylocal)
- [Schnellstart](../../../../04-PracticalSamples/foundrylocal)
- [Was die Anwendung macht](../../../../04-PracticalSamples/foundrylocal)
- [Beispielausgabe](../../../../04-PracticalSamples/foundrylocal)
- [Architektur](../../../../04-PracticalSamples/foundrylocal)
- [Code-Highlights](../../../../04-PracticalSamples/foundrylocal)
  - [Integration des OpenAI Java SDK](../../../../04-PracticalSamples/foundrylocal)
  - [Chat Completion API](../../../../04-PracticalSamples/foundrylocal)
- [Fehlerbehebung](../../../../04-PracticalSamples/foundrylocal)

## Voraussetzungen

> **⚠️ Hinweis**: Diese Anwendung **funktioniert nicht im mitgelieferten Devcontainer**, da Foundry Local auf dem Hostsystem installiert und ausgeführt werden muss.

### Installation von Foundry Local

Bevor Sie diese Anwendung ausführen, müssen Sie Foundry Local installieren und starten. Gehen Sie wie folgt vor:

1. **Stellen Sie sicher, dass Ihr System die Anforderungen erfüllt**:
   - **Betriebssystem**: Windows 10 (x64), Windows 11 (x64/ARM), Windows Server 2025 oder macOS
   - **Hardware**: 
     - Minimum: 8 GB RAM, 3 GB freier Festplattenspeicher
     - Empfohlen: 16 GB RAM, 15 GB freier Festplattenspeicher
   - **Netzwerk**: Internetverbindung für den initialen Modell-Download (optional für Offline-Nutzung)
   - **Beschleunigung (optional)**: NVIDIA GPU (2000er-Serie oder neuer), AMD GPU (6000er-Serie oder neuer), Qualcomm Snapdragon X Elite (8 GB oder mehr Speicher) oder Apple Silicon
   - **Berechtigungen**: Administratorrechte, um Software auf Ihrem Gerät zu installieren

2. **Installieren Sie Foundry Local**:
   
   **Für Windows:**
   ```bash
   winget install Microsoft.FoundryLocal
   ```
   
   **Für macOS:**
   ```bash
   brew tap microsoft/foundrylocal
   brew install foundrylocal
   ```
   
   Alternativ können Sie den Installer aus dem [Foundry Local GitHub-Repository](https://github.com/microsoft/Foundry-Local) herunterladen.

3. **Starten Sie Ihr erstes Modell**:

   ```bash
   foundry model run phi-3.5-mini
   ```

   Das Modell wird heruntergeladen (dies kann je nach Internetgeschwindigkeit einige Minuten dauern) und anschließend ausgeführt. Foundry Local wählt automatisch die beste Modellvariante für Ihr System aus (CUDA für NVIDIA-GPUs, CPU-Version andernfalls).

4. **Testen Sie das Modell**, indem Sie im selben Terminal eine Frage stellen:

   ```bash
   Why is the sky blue?
   ```

   Sie sollten eine Antwort vom Phi-Modell erhalten, die erklärt, warum der Himmel blau erscheint.

### Verifizierung

Sie können überprüfen, ob alles ordnungsgemäß funktioniert, indem Sie diese Befehle ausführen:

```bash
# List all available models
foundry model list

# Check the service status via REST API
curl http://localhost:5273/v1/models
```

Sie können auch `http://localhost:5273` in Ihrem Browser aufrufen, um die Weboberfläche von Foundry Local zu sehen.

## Konfiguration

Die Anwendung kann über `application.properties` konfiguriert werden:

- `foundry.local.base-url` - Basis-URL für Foundry Local (Standard: http://localhost:5273)
- `foundry.local.model` - Zu verwendendes KI-Modell (Standard: Phi-3.5-mini-instruct-cuda-gpu)

> **Hinweis**: Der Modellname in der Konfiguration sollte mit der spezifischen Variante übereinstimmen, die Foundry Local für Ihr System heruntergeladen hat. Wenn Sie `foundry model run phi-3.5-mini` ausführen, wählt Foundry Local automatisch die beste Variante aus und lädt sie herunter (CUDA für NVIDIA-GPUs, CPU-Version andernfalls). Verwenden Sie `foundry model list`, um den genauen Modellnamen in Ihrer lokalen Instanz anzuzeigen.

## Schnellstart

### 1. Wechseln Sie in das Verzeichnis der Foundry Local-Anwendung
```bash
cd Generative-AI-for-beginners-java/04-PracticalSamples/foundrylocal
```

### 2. Starten Sie die Anwendung

```bash
mvn spring-boot:run
```

Oder bauen und starten Sie die JAR-Datei:

```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

### Abhängigkeiten

Diese Anwendung verwendet das OpenAI Java SDK, um mit Foundry Local zu kommunizieren. Die wichtigste Abhängigkeit ist:

```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

Die Anwendung ist vorkonfiguriert, um sich mit Foundry Local zu verbinden, das auf dem Standardport läuft.

## Was die Anwendung macht

Wenn Sie die Anwendung ausführen:

1. **Startet** sie als Befehlszeilenanwendung (kein Webserver)
2. **Sendet automatisch** eine Testnachricht: "Hallo! Können Sie mir sagen, was Sie sind und welches Modell Sie ausführen?"
3. **Zeigt die Antwort** von Foundry Local in der Konsole an
4. **Beendet sich sauber** nach der Demo

## Beispielausgabe

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi, an AI language model created by Microsoft. I don't have a physical form or a specific hardware model like a smartphone or a computer. I exist purely in software, and I operate on Microsoft's infrastructure...
=========================
```

## Architektur

- **Application.java** - Haupt-Spring-Boot-Anwendung mit CommandLineRunner
- **FoundryLocalService.java** - Service, der das OpenAI Java SDK verwendet, um mit Foundry Local zu kommunizieren
- Verwendet **OpenAI Java SDK** für typsichere API-Aufrufe
- Automatische JSON-Serialisierung/Deserialisierung wird vom SDK übernommen
- Saubere Konfiguration mit Springs `@Value`- und `@PostConstruct`-Annotationen

## Code-Highlights

### Integration des OpenAI Java SDK

Die Anwendung verwendet das OpenAI Java SDK, um einen Client zu erstellen, der für Foundry Local konfiguriert ist:

```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")
            .apiKey("unused") // Local server doesn't require real API key
            .build();
}
```

### Chat Completion API

Das Erstellen von Chat Completion-Anfragen ist einfach und typsicher:

```java
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
        .model(model)
        .addUserMessage(message)
        .maxCompletionTokens(150)
        .temperature(0.7)
        .build();

ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
```

## Fehlerbehebung

Falls Verbindungsfehler auftreten:
1. Überprüfen Sie, ob Foundry Local unter `http://localhost:5273` läuft
2. Stellen Sie sicher, dass eine Phi-3.5-mini-Modellvariante mit `foundry model list` verfügbar ist
3. Vergewissern Sie sich, dass der Modellname in `application.properties` mit dem exakten Modellnamen in der Liste übereinstimmt
4. Stellen Sie sicher, dass keine Firewall die Verbindung blockiert

Häufige Probleme:
- **Modell nicht gefunden**: Führen Sie `foundry model run phi-3.5-mini` aus, um das Modell herunterzuladen und zu starten
- **Dienst läuft nicht**: Der Foundry Local-Dienst wurde möglicherweise gestoppt; starten Sie ihn mit dem Modell-Startbefehl neu
- **Falscher Modellname**: Verwenden Sie `foundry model list`, um verfügbare Modelle anzuzeigen, und aktualisieren Sie Ihre Konfiguration entsprechend

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.