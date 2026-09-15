# Anleitung für den Haustier-Geschichten-Generator für Einsteiger

Laden Sie ein Haustierfoto hoch, analysieren Sie es mit GPT-5.6 Luna und generieren Sie aus der resultierenden Beschreibung eine Geschichte. Beide Modellaufrufe verwenden `reasoning_effort: none`.

| Komponente | Version |
| --- | --- |
| Java | 21 oder höher |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Inhaltsverzeichnis

- [Voraussetzungen](#voraussetzungen)
- [Projektstruktur verstehen](#projektstruktur-verstehen)
- [Erklärung der Kernkomponenten](#erklärung-der-kernkomponenten)
  - [1. Hauptanwendung](#1-hauptanwendung)
  - [2. Webcontroller](#2-webcontroller)
  - [3. Story-Service](#3-story-service)
  - [4. Webvorlagen](#4-webvorlagen)
  - [5. Konfiguration](#5-konfiguration)
- [Anwendung starten](#anwendung-ausführen)
- [Offline-Tests](#offline-tests)
- [Wie das Ganze zusammenarbeitet](#wie-das-ganze-zusammenarbeitet)
- [Die KI-Integration verstehen](#die-ki-integration-verstehen)
- [Nächste Schritte](#nächste-schritte)

## Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass Sie Folgendes haben:
- Java 21 oder höher installiert
- Maven für die Verwaltung von Abhängigkeiten
- Eine Azure AI Foundry-Bereitstellung von GPT-5.6 Luna namens `gpt-5.6-luna` oder eine `AZURE_OPENAI_DEPLOYMENT`-Überschreibung, die auf diese Bereitstellung zeigt. Siehe [Kapitel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) für das Bereitstellen und melden Sie sich mit `az login` für eine schlüsselose Authentifizierung an. Die Bereitstellung muss Bild-Input und `reasoning_effort: none` unterstützen.
- Grundlegende Kenntnisse in Java, Spring Boot und Webentwicklung

## Projektstruktur verstehen

Das Haustiergeschichtenprojekt enthält mehrere wichtige Dateien:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Erklärung der Kernkomponenten

### 1. Hauptanwendung

**Datei:** `PetStoryApplication.java`

Dies ist der Einstiegspunkt unserer Spring Boot-Anwendung:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Das bewirkt dies:**
- Die Annotation `@SpringBootApplication` ermöglicht Auto-Konfiguration und Komponentenscan
- Startet einen eingebetteten Webserver (Tomcat) auf Port 8080
- Erstellt automatisch alle benötigten Spring Beans und Services

### 2. Webcontroller

**Datei:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpunkt | Anfrage | Erfolgreiche Antwort |
| --- | --- | --- |
| `GET /` | Kein Body | HTML-Uploader-Formular mit CSRF-Token |
| `POST /analyze-image` | `multipart/form-data`, Datei-Feld `image` | JSON: `{"description":"Ein verspieltes Haustier..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, Feld `description` | HTML-Ergebnisseite mit Beschreibung und generierter Geschichte |

Beide POST-Endpunkte erfordern das Sitzungscookie und CSRF-Token, die von `GET /` erhalten wurden. Das Upload-Skript sendet den versteckten `_csrf`-Wert im `X-CSRF-TOKEN`-Header; die Geschichte-Übermittlung sendet ihn als `_csrf`-Formularfeld. API-Clients müssen das Cookie zwischen Anfragen beibehalten. Dies sind Formulardienste, keine JSON-Anfrageendpunkte.

Beschreibungen müssen nicht leer sein und dürfen nicht länger als 1000 Zeichen sein. Der Controller trimmt die Beschreibung und entfernt `<`, `>`, doppelte Anführungszeichen, Apostrophe und `&` bevor er sie an den Service weitergibt. Die Ergebnisvorlage escaped ebenfalls die Modell-Ausgabe mit `th:text`.

Bildvalidierungsfehler führen zu HTTP 400 mit einem `error`-Feld; Modellausfälle führen zu HTTP 502 mit einem `error`-Feld und ohne `description`. Ungültige Geschichtsbeschreibungen oder Modellfehler leiten auf `/` mit einer sichtbaren Fehlermeldung um. Fehlende erforderliche Felder führen zu HTTP 400, fehlende oder ungültige CSRF-Tokens zu HTTP 403. Keine Ersatzbeschreibungen oder Geschichten werden als erfolgreiche KI-Ergebnisse präsentiert.

### 3. Story-Service

**Datei:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Das offizielle OpenAI Java SDK 4.63.1 ruft die OpenAI-kompatible Chat Completions API von Azure AI Foundry auf. Azure Identity 1.18.6 liefert ein Microsoft Entra Bearer-Token über `DefaultAzureCredential`; kein API-Schlüssel ist erforderlich.

| Operation | Eingabe | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bildbytes, kodiert als base64-Daten-URL mit dem hochgeladenen MIME-Typ | 300 |
| `generateStory` | Eine Haustierbeschreibung in einer Benutzer-Nachricht | 800 |

Beide Anfragen verwenden die konfigurierte Bereitstellung, standardmäßig `gpt-5.6-luna`, und setzen explizit `ReasoningEffort.NONE` (`reasoning_effort: none`). Keine der Anfragen sendet `temperature` oder den veralteten Parameter `max_tokens`.

Die Bildanalyse akzeptiert JPEG, PNG, GIF und WebP, lehnt leere Bilder und Dateien über 10 MB ab und begrenzt die resultierende Beschreibung auf 1000 Zeichen. Die Story-Aufforderung fordert eine familienfreundliche Kurzgeschichte an. Leere Auswahlen oder leere Modellinhalte sind Fehler, und Fehler bewahren die ursprüngliche Ursache für diagnostische Zwecke auf Serverseite. Der SDK-Client wird beim Herunterfahren der Anwendung geschlossen.

### 4. Webvorlagen

**Datei:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Upload-Formular)

Die Seite startet mit einem Fotoauswahlelement, nicht mit einem Textbereich für die Beschreibung. **Bild analysieren** zeigt eine Vorschau des ausgewählten Fotos und sendet es an `/analyze-image`. Eine erfolgreiche Antwort zeigt die Beschreibung, füllt das versteckte Feld `description` und zeigt **Geschichte generieren** an. Dieser Button sendet das existierende Formular an `/generate-story`.

Es gibt keinen Modell-Download im Browser oder eine CDN-Abhängigkeit. Die Bildanalyse läuft auf dem Server über die konfigurierte Azure-Bereitstellung. Fehler bleiben sichtbar und erlauben keine Geschichtengenerierung mit einer erfundenen Beschreibung. Das Auswählen einer anderen Datei löscht die vorherige Analyse.

**Datei:** `result.html` (Geschichtendarstellung)

Zeigt die generierte Geschichte an:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Vorlagenfunktionen:**

1. **Thymeleaf-Integration**: Verwendet `th:`-Attribute für dynamischen Inhalt
2. **Responsives Design**: CSS-Stylings für Mobil- und Desktopgeräte
3. **Fehlerbehandlung**: Zeigt Validierungsfehler für Benutzer an
4. **Upload-Verarbeitung**: JavaScript zeigt eine Vorschau des Fotos, sendet eine CSRF-geschützte Multipart-Anfrage und zeigt die zurückgegebene Beschreibung an

### 5. Konfiguration

**Datei:** `application.properties`

Konfigurationseinstellungen für die Anwendung:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Konfiguration erklärt:**

1. **Datei-Upload**: Sowohl die Datei als auch die gesamte Multipart-Anfrage sind mit 10MB begrenzt; halten Sie Fotos unter diesem Limit, um Platz für Multipart-Header zu lassen
2. **Logging**: Steuert, welche Informationen während der Ausführung protokolliert werden
3. **Azure AI Foundry**: Gibt den Endpunkt und die zur Verwendung stehende Modellbereitstellung an (schlüssellose Authentifizierung)
4. **Sicherheit**: CSRF-Schutz bleibt aktiviert; Modelldiagnosen werden auf dem Server protokolliert, während der Controller generische Modellfehler Nachrichten anzeigt

## Anwendung ausführen

### Schritt 1: Anmelden und Endpunkt festlegen

Die Authentifizierung erfolgt schlüssellos (Microsoft Entra ID), es gibt also keinen API-Schlüssel. Melden Sie sich an und setzen Sie Ihren Foundry-Endpunkt:

**Windows (Eingabeaufforderung):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Warum das nötig ist:**
- Azure AI Foundry verwendet Microsoft Entra ID, um Inferenzanfragen zu authentifizieren
- Schlüssellose Authentifizierung bedeutet keine Geheimnisse im Quellcode oder in der Umgebung
- Ihr Konto benötigt die Rolle **Cognitive Services OpenAI User** auf der Ressource

Der Standardname der Bereitstellung ist `gpt-5.6-luna`. Wenn Ihre GPT-5.6 Luna-Bereitstellung einen anderen Namen hat, setzen Sie `AZURE_OPENAI_DEPLOYMENT` im selben Terminal vor dem Start der Anwendung. Sowohl die Bildanalyse als auch die Geschichtengenerierung verwenden diese Einstellung.

### Schritt 2: Bauen und Ausführen

Navigieren Sie zum Projektverzeichnis:
```bash
cd 04-PracticalSamples/petstory
```

Bauen Sie die eigenständige ausführbare JAR und führen Sie alle Offline-Tests aus:
```bash
mvn clean package
```

Starten Sie den Server:
```bash
mvn spring-boot:run
```

Die Anwendung wird unter `http://localhost:8080` gestartet.

Alternativ starten Sie die verpackte JAR auf einem freien Port, zum Beispiel:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Für diesen Befehl öffnen Sie `http://localhost:8083/`. Die gleichen Routen `/analyze-image` und `/generate-story` sind auf dem gewählten Port verfügbar.

### Schritt 3: Anwendung testen

1. **Öffnen** Sie `http://localhost:8080` in Ihrem Browser
2. **Wählen** Sie ein deutliches Haustierfoto im JPEG-, PNG-, GIF- oder WebP-Format unter 10MB
3. **Klicken** Sie auf „Bild analysieren“ und warten Sie auf die Haustierbeschreibung
4. **Klicken** Sie nach erfolgreicher Analyse auf „Geschichte generieren“
5. **Betrachten** Sie die Geschichte und benutzen Sie den Link auf der Ergebnis-Seite, um zum Upload-Formular zurückzukehren

Der erfolgreiche Foto-zu-Geschichte-Fluss macht zwei Modellaufrufe, einen pro Button. Laufende Inferenz verbraucht Ihr Bereitstellungskontingent und kann Kosten verursachen; führen Sie Smoke-Tests seriell aus, wenn Sie eine rate-limitierte Bereitstellung teilen. Das Laden der Startseite ruft das Modell nicht auf.

## Offline-Tests

Im Verzeichnis `sample` führen Sie aus:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) zeichnet echte OpenAI SDK-Anfragen mit einem Loopback-HTTP-Fixture auf. Es prüft die Bereitstellung beider Anfragen, `reasoning_effort: none`, Tokenlimits, Bild-Payload, Eingabevalidierung, leere Antworten und Upstream-Fehler.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) verwendet MockMvc mit einem gemockten Modell-Service zum Testen der gerenderten Thymeleaf-Seiten, Upload-Vertrag, CSRF, Validierung, Ausgabe-Escaping und sichtbare Fehler. Diese Tests benötigen keine Azure-Zugangsdaten und rufen niemals kostenpflichtige Azure-Inferenz auf. Maven schreibt Surefire-Berichte unter `target/surefire-reports`.

## Wie das Ganze zusammenarbeitet

Hier ist der komplette Ablauf, wenn Sie eine Haustiergeschichte generieren:

1. **Fotoauswahl**: Sie wählen ein Haustierbild im Upload-Formular aus
2. **Bild-Upload**: „Bild analysieren“ sendet eine Multipart-POST an `/analyze-image` mit dem CSRF-Header
3. **Bildanalyse**: `StoryService` sendet das Bild an GPT-5.6 Luna mit reasoning auf `none` gesetzt
4. **Beschreibung anzeigen**: Der Browser zeigt die zurückgegebene Beschreibung an und speichert sie im Formular
5. **Geschichte absenden**: „Geschichte generieren“ sendet `description` und `_csrf` an `/generate-story`
6. **Geschichtengenerierung**: Der Controller validiert die Beschreibung und ruft dieselbe Bereitstellung mit reasoning auf `none` gesetzt auf
7. **Vorlagenrendering**: Thymeleaf escaped und zeigt die Beschreibung und die Geschichte auf der Ergebnis-Seite an

**Fehlerbehandlungsablauf:**
Wenn das Modell ausfällt, protokolliert der Server die Ursache. Die Bildanalyse liefert HTTP 502 und der Browser zeigt die Fehlermeldung ohne Anzeige von „Geschichte generieren“. Die Geschichtengenerierung leitet zur Formularseite mit einer Fehlermeldung weiter. Keiner der Pfade ersetzt stillschweigend ein vorgefertigtes Ergebnis.

## Die KI-Integration verstehen

### Azure AI Foundry (schlüssellos)
Der Dienst konfiguriert das SDK mit dem `/openai/v1/` Endpunkt Ihrer Ressource. `DefaultAzureCredential` und `AuthenticationUtil.getBearerTokenSupplier` liefern Microsoft Entra Tokens für `https://ai.azure.com/.default`. Die lokale Entwicklung kann die Azure CLI-Anmeldung verwenden; eine in Azure gehostete App kann eine verwaltete Identität mit den notwendigen Ressourcenberechtigungen verwenden.

### Prompt Engineering
Die Bildanalyse fordert beobachtbare Haustiermerkmale in einem kurzen Absatz an und weist das Modell an, Text im Bild als Daten und nicht als Anweisungen zu behandeln. Die Geschichtenerstellung verwendet die zurückgegebene Beschreibung in einer separaten, familienfreundlichen Schreibaufforderung. Keiner der Aufrufe aktiviert reasoning oder setzt eine Temperatureinstellung.

### Antwortverarbeitung
Der gemeinsame Antwortverarbeiter lehnt fehlende Optionen und leere oder nur aus Leerzeichen bestehende Inhalte ab, trimmt gültige Inhalte und bewahrt Upstream-Fehler. Bildbeschreibungen sind auf 1000 Zeichen begrenzt, um in das nachfolgende Geschichtsformular zu passen. Der ursprüngliche Modellfehler wird für Diagnosen beibehalten, aber nicht dem Benutzer angezeigt.

## Nächste Schritte

Für weitere Beispiele siehe [Kapitel 04: Praktische Beispiele](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->