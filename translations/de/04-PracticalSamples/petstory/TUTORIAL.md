<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-21T15:35:39+00:00",
  "source_file": "04-PracticalSamples/petstory/TUTORIAL.md",
  "language_code": "de"
}
-->
# Anleitung für den Pet Story Generator für Anfänger

## Inhaltsverzeichnis

- [Voraussetzungen](../../../../04-PracticalSamples/petstory)
- [Projektstruktur verstehen](../../../../04-PracticalSamples/petstory)
- [Erläuterung der Kernkomponenten](../../../../04-PracticalSamples/petstory)
  - [1. Hauptanwendung](../../../../04-PracticalSamples/petstory)
  - [2. Web-Controller](../../../../04-PracticalSamples/petstory)
  - [3. Story-Service](../../../../04-PracticalSamples/petstory)
  - [4. Web-Templates](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguration](../../../../04-PracticalSamples/petstory)
- [Anwendung ausführen](../../../../04-PracticalSamples/petstory)
- [Wie alles zusammenarbeitet](../../../../04-PracticalSamples/petstory)
- [Die KI-Integration verstehen](../../../../04-PracticalSamples/petstory)
- [Nächste Schritte](../../../../04-PracticalSamples/petstory)

## Voraussetzungen

Bevor Sie beginnen, stellen Sie sicher, dass Sie Folgendes haben:
- Java 21 oder höher installiert
- Maven für das Abhängigkeitsmanagement
- Ein GitHub-Konto mit einem persönlichen Zugriffstoken (PAT) mit dem `models:read`-Bereich
- Grundlegendes Verständnis von Java, Spring Boot und Webentwicklung

## Projektstruktur verstehen

Das Pet-Story-Projekt enthält mehrere wichtige Dateien:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI story generation service
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Erläuterung der Kernkomponenten

### 1. Hauptanwendung

**Datei:** `PetStoryApplication.java`

Dies ist der Einstiegspunkt für unsere Spring-Boot-Anwendung:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Was das macht:**
- Die Annotation `@SpringBootApplication` aktiviert die automatische Konfiguration und Komponentensuche
- Startet einen eingebetteten Webserver (Tomcat) auf Port 8080
- Erstellt automatisch alle notwendigen Spring-Beans und -Dienste

### 2. Web-Controller

**Datei:** `PetController.java`

Dieser Controller verarbeitet alle Webanfragen und Benutzerinteraktionen:

```java
@Controller
public class PetController {
    
    private final StoryService storyService;
    
    public PetController(StoryService storyService) {
        this.storyService = storyService;
    }
    
    @GetMapping("/")
    public String index() {
        return "index";  // Returns index.html template
    }
    
    @PostMapping("/generate-story")
    public String generateStory(@RequestParam("description") String description, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        
        // Input validation
        if (description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please provide a description.");
            return "redirect:/";
        }
        
        // Sanitize input for security
        String sanitizedDescription = sanitizeInput(description);
        
        // Generate story with error handling
        try {
            String story = storyService.generateStory(sanitizedDescription);
            model.addAttribute("caption", sanitizedDescription);
            model.addAttribute("story", story);
            return "result";  // Returns result.html template
            
        } catch (Exception e) {
            // Use fallback story if AI fails
            String fallbackStory = generateFallbackStory(sanitizedDescription);
            model.addAttribute("story", fallbackStory);
            return "result";
        }
    }
    
    private String sanitizeInput(String input) {
        return input.replaceAll("[<>\"'&]", "")  // Remove dangerous characters
                   .trim()
                   .substring(0, Math.min(input.length(), 500));  // Limit length
    }
}
```

**Wichtige Funktionen:**

1. **Routenverwaltung**: `@GetMapping("/")` zeigt das Upload-Formular, `@PostMapping("/generate-story")` verarbeitet die Eingaben
2. **Eingabevalidierung**: Überprüft auf leere Beschreibungen und Längenbeschränkungen
3. **Sicherheit**: Bereinigt Benutzereingaben, um XSS-Angriffe zu verhindern
4. **Fehlerbehandlung**: Bietet Ersatzgeschichten, wenn der KI-Dienst ausfällt
5. **Modellbindung**: Übergibt Daten an HTML-Templates mit Spring's `Model`

**Fallback-System:**
Der Controller enthält vorgefertigte Story-Vorlagen, die verwendet werden, wenn der KI-Dienst nicht verfügbar ist:

```java
private String generateFallbackStory(String description) {
    String[] storyTemplates = {
        "Meet the most wonderful pet in the world – a furry ball of energy...",
        "Once upon a time, there lived a remarkable pet whose heart was as big...",
        "In a cozy home filled with love, there lived an extraordinary pet..."
    };
    
    // Use description hash for consistent responses
    int index = Math.abs(description.hashCode() % storyTemplates.length);
    return storyTemplates[index];
}
```

### 3. Story-Service

**Datei:** `StoryService.java`

Dieser Service kommuniziert mit GitHub Models, um Geschichten zu generieren:

```java
@Service
public class StoryService {
    
    private final OpenAIClient openAIClient;
    private final String modelName;
    
    public StoryService(@Value("${github.models.endpoint}") String endpoint,
                       @Value("${github.models.model}") String modelName) {
        
        String githubToken = System.getenv("GITHUB_TOKEN");
        if (githubToken == null || githubToken.isBlank()) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable must be set");
        }
        
        // Create OpenAI client configured for GitHub Models
        this.openAIClient = OpenAIOkHttpClient.builder()
                .baseUrl(endpoint)
                .apiKey(githubToken)
                .build();
    }
    
    public String generateStory(String description) {
        String systemPrompt = "You are a creative storyteller who writes fun, " +
                             "family-friendly short stories about pets. " +
                             "Keep stories under 500 words and appropriate for all ages.";
        
        String userPrompt = "Write a fun short story about a pet described as: " + description;
        
        // Configure the AI request
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(modelName)
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(500)  // Limit response length
                .temperature(0.8)          // Control creativity (0.0-1.0)
                .build();
        
        // Send request and get response
        ChatCompletion response = openAIClient.chat().completions().create(params);
        
        return response.choices().get(0).message().content().orElse("");
    }
}
```

**Wichtige Komponenten:**

1. **OpenAI-Client**: Verwendet das offizielle OpenAI-Java-SDK, das für GitHub Models konfiguriert ist
2. **System-Prompt**: Legt das Verhalten der KI fest, um familienfreundliche Haustiergeschichten zu schreiben
3. **Benutzer-Prompt**: Gibt der KI genau vor, welche Geschichte basierend auf der Beschreibung geschrieben werden soll
4. **Parameter**: Steuert die Länge der Geschichte und das Kreativitätsniveau
5. **Fehlerbehandlung**: Wirft Ausnahmen, die vom Controller abgefangen und verarbeitet werden

### 4. Web-Templates

**Datei:** `index.html` (Upload-Formular)

Die Hauptseite, auf der Benutzer ihre Haustiere beschreiben:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Generator</title>
    <!-- CSS styling -->
</head>
<body>
    <div class="container">
        <h1>Pet Story Generator</h1>
        <p>Describe your pet and we'll create a fun story about them!</p>
        
        <!-- Error message display -->
        <div th:if="${error}" class="error" th:text="${error}"></div>
        
        <!-- Story generation form -->
        <form action="/generate-story" method="post">
            <div class="form-group">
                <label for="description">Describe your pet:</label>
                <textarea id="description" name="description" 
                         placeholder="Tell us about your pet - what they look like, their personality, favorite activities..."
                         maxlength="1000" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Generate Story</button>
        </form>
        
        <!-- Image upload section with client-side processing -->
        <div class="upload-section">
            <h2>Or Upload a Photo</h2>
            <input type="file" id="imageInput" accept="image/*" />
            <button onclick="analyzeImage()" class="upload-btn">Analyze Image</button>
        </div>
        
        <script>
            // Client-side image analysis using Transformers.js
            async function analyzeImage() {
                // Image processing code here
                // Generates description automatically from uploaded image
            }
        </script>
    </div>
</body>
</html>
```

**Datei:** `result.html` (Story-Anzeige)

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

**Template-Funktionen:**

1. **Thymeleaf-Integration**: Verwendet `th:`-Attribute für dynamische Inhalte
2. **Responsive Design**: CSS-Styling für Mobilgeräte und Desktops
3. **Fehlerbehandlung**: Zeigt Validierungsfehler für Benutzer an
4. **Clientseitige Verarbeitung**: JavaScript für Bildanalyse (mit Transformers.js)

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

# GitHub Models configuration
github.models.endpoint=https://models.github.ai/inference
github.models.model=openai/gpt-4.1-nano
```

**Erläuterung der Konfiguration:**

1. **Datei-Upload**: Erlaubt Bilder bis zu 10 MB
2. **Logging**: Steuert, welche Informationen während der Ausführung protokolliert werden
3. **GitHub Models**: Gibt an, welches KI-Modell und welchen Endpunkt verwendet werden soll
4. **Sicherheit**: Konfiguration der Fehlerbehandlung, um sensible Informationen nicht preiszugeben

## Anwendung ausführen

### Schritt 1: GitHub-Token setzen

Zuerst müssen Sie Ihr GitHub-Token als Umgebungsvariable setzen:

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

**Warum das notwendig ist:**
- GitHub Models erfordert eine Authentifizierung, um auf KI-Modelle zuzugreifen
- Die Verwendung von Umgebungsvariablen hält sensible Tokens aus dem Quellcode heraus
- Der `models:read`-Bereich ermöglicht den Zugriff auf KI-Inferenzen

### Schritt 2: Build und Start

Wechseln Sie in das Projektverzeichnis:
```bash
cd 04-PracticalSamples/petstory
```

Bauen Sie die Anwendung:
```bash
mvn clean compile
```

Starten Sie den Server:
```bash
mvn spring-boot:run
```

Die Anwendung wird unter `http://localhost:8080` gestartet.

### Schritt 3: Anwendung testen

1. **Öffnen** Sie `http://localhost:8080` in Ihrem Browser
2. **Beschreiben** Sie Ihr Haustier im Textfeld (z. B. "Ein verspielter Golden Retriever, der gerne apportiert")
3. **Klicken** Sie auf "Generate Story", um eine KI-generierte Geschichte zu erhalten
4. **Alternativ** können Sie ein Haustierbild hochladen, um automatisch eine Beschreibung zu generieren
5. **Anzeigen** der kreativen Geschichte basierend auf der Beschreibung Ihres Haustiers

## Wie alles zusammenarbeitet

So funktioniert der gesamte Ablauf, wenn Sie eine Haustiergeschichte generieren:

1. **Benutzereingabe**: Sie beschreiben Ihr Haustier im Webformular
2. **Formularübermittlung**: Der Browser sendet eine POST-Anfrage an `/generate-story`
3. **Controller-Verarbeitung**: `PetController` validiert und bereinigt die Eingabe
4. **KI-Dienstaufruf**: `StoryService` sendet eine Anfrage an die GitHub Models API
5. **Geschichtengenerierung**: Die KI erstellt eine kreative Geschichte basierend auf der Beschreibung
6. **Antwortverarbeitung**: Der Controller erhält die Geschichte und fügt sie dem Modell hinzu
7. **Template-Rendering**: Thymeleaf rendert `result.html` mit der Geschichte
8. **Anzeige**: Der Benutzer sieht die generierte Geschichte in seinem Browser

**Fehlerbehandlungsablauf:**
Falls der KI-Dienst ausfällt:
1. Der Controller fängt die Ausnahme ab
2. Generiert eine Ersatzgeschichte mit vorgefertigten Vorlagen
3. Zeigt die Ersatzgeschichte mit einem Hinweis auf die Nichtverfügbarkeit der KI an
4. Der Benutzer erhält trotzdem eine Geschichte, was eine gute Benutzererfahrung sicherstellt

## Die KI-Integration verstehen

### GitHub Models API
Die Anwendung verwendet GitHub Models, die kostenlosen Zugriff auf verschiedene KI-Modelle bieten:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt-Engineering
Der Service verwendet sorgfältig gestaltete Prompts, um gute Ergebnisse zu erzielen:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Antwortverarbeitung
Die KI-Antwort wird extrahiert und validiert:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Nächste Schritte

Für weitere Beispiele siehe [Kapitel 04: Praktische Beispiele](../README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.