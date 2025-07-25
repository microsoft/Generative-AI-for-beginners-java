<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:25:37+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "it"
}
-->
# Tutorial per Principianti: Generatore di Storie sui Pet

## Indice

- [Prerequisiti](../../../../04-PracticalSamples/petstory)
- [Comprendere la Struttura del Progetto](../../../../04-PracticalSamples/petstory)
- [Componenti Principali Spiegati](../../../../04-PracticalSamples/petstory)
  - [1. Applicazione Principale](../../../../04-PracticalSamples/petstory)
  - [2. Controller Web](../../../../04-PracticalSamples/petstory)
  - [3. Servizio Storie](../../../../04-PracticalSamples/petstory)
  - [4. Template Web](../../../../04-PracticalSamples/petstory)
  - [5. Configurazione](../../../../04-PracticalSamples/petstory)
- [Esecuzione dell'Applicazione](../../../../04-PracticalSamples/petstory)
- [Come Funziona Tutto Insieme](../../../../04-PracticalSamples/petstory)
- [Comprendere l'Integrazione con l'AI](../../../../04-PracticalSamples/petstory)
- [Prossimi Passi](../../../../04-PracticalSamples/petstory)

## Prerequisiti

Prima di iniziare, assicurati di avere:
- Java 21 o superiore installato
- Maven per la gestione delle dipendenze
- Un account GitHub con un token di accesso personale (PAT) con ambito `models:read`
- Conoscenze di base di Java, Spring Boot e sviluppo web

## Comprendere la Struttura del Progetto

Il progetto del generatore di storie sui pet include diversi file importanti:

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

## Componenti Principali Spiegati

### 1. Applicazione Principale

**File:** `PetStoryApplication.java`

Questo è il punto di ingresso per la nostra applicazione Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Cosa fa:**
- L'annotazione `@SpringBootApplication` abilita la configurazione automatica e la scansione dei componenti
- Avvia un server web embedded (Tomcat) sulla porta 8080
- Crea automaticamente tutti i bean e i servizi Spring necessari

### 2. Controller Web

**File:** `PetController.java`

Gestisce tutte le richieste web e le interazioni con l'utente:

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

**Caratteristiche principali:**

1. **Gestione delle Rotte**: `@GetMapping("/")` mostra il modulo di caricamento, `@PostMapping("/generate-story")` elabora le richieste
2. **Validazione Input**: Controlla che le descrizioni non siano vuote e rispettino i limiti di lunghezza
3. **Sicurezza**: Sanifica l'input dell'utente per prevenire attacchi XSS
4. **Gestione degli Errori**: Fornisce storie di fallback quando il servizio AI non è disponibile
5. **Binding del Modello**: Passa i dati ai template HTML utilizzando il `Model` di Spring

**Sistema di Fallback:**
Il controller include modelli di storie pre-scritte che vengono utilizzati quando il servizio AI non è disponibile:

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

### 3. Servizio Storie

**File:** `StoryService.java`

Questo servizio comunica con i Modelli GitHub per generare storie:

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

**Componenti principali:**

1. **Client OpenAI**: Utilizza l'SDK Java ufficiale di OpenAI configurato per i Modelli GitHub
2. **Prompt di Sistema**: Imposta il comportamento dell'AI per scrivere storie sui pet adatte alle famiglie
3. **Prompt Utente**: Indica all'AI esattamente quale storia scrivere in base alla descrizione
4. **Parametri**: Controlla la lunghezza della storia e il livello di creatività
5. **Gestione degli Errori**: Lancia eccezioni che il controller intercetta e gestisce

### 4. Template Web

**File:** `index.html` (Modulo di Caricamento)

La pagina principale dove gli utenti descrivono i loro pet:

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

**File:** `result.html` (Visualizzazione della Storia)

Mostra la storia generata:

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

**Caratteristiche dei Template:**

1. **Integrazione Thymeleaf**: Utilizza attributi `th:` per contenuti dinamici
2. **Design Responsivo**: Stile CSS per dispositivi mobili e desktop
3. **Gestione degli Errori**: Mostra errori di validazione agli utenti
4. **Elaborazione lato Client**: JavaScript per l'analisi delle immagini (usando Transformers.js)

### 5. Configurazione

**File:** `application.properties`

Impostazioni di configurazione per l'applicazione:

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

**Configurazione spiegata:**

1. **Caricamento File**: Consente immagini fino a 10MB
2. **Logging**: Controlla quali informazioni vengono registrate durante l'esecuzione
3. **Modelli GitHub**: Specifica quale modello AI e endpoint utilizzare
4. **Sicurezza**: Configurazione per la gestione degli errori per evitare di esporre informazioni sensibili

## Esecuzione dell'Applicazione

### Passo 1: Imposta il tuo Token GitHub

Per prima cosa, devi impostare il tuo token GitHub come variabile d'ambiente:

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

**Perché è necessario:**
- I Modelli GitHub richiedono autenticazione per accedere ai modelli AI
- Utilizzare variabili d'ambiente mantiene i token sensibili fuori dal codice sorgente
- L'ambito `models:read` fornisce accesso all'inferenza AI

### Passo 2: Compila ed Esegui

Vai nella directory del progetto:
```bash
cd 04-PracticalSamples/petstory
```

Compila l'applicazione:
```bash
mvn clean compile
```

Avvia il server:
```bash
mvn spring-boot:run
```

L'applicazione sarà disponibile su `http://localhost:8080`.

### Passo 3: Testa l'Applicazione

1. **Apri** `http://localhost:8080` nel tuo browser
2. **Descrivi** il tuo pet nell'area di testo (es. "Un golden retriever giocherellone che ama riportare oggetti")
3. **Clicca** su "Genera Storia" per ottenere una storia generata dall'AI
4. **In alternativa**, carica un'immagine del tuo pet per generare automaticamente una descrizione
5. **Visualizza** la storia creativa basata sulla descrizione del tuo pet

## Come Funziona Tutto Insieme

Ecco il flusso completo quando generi una storia sui pet:

1. **Input Utente**: Descrivi il tuo pet nel modulo web
2. **Invio del Modulo**: Il browser invia una richiesta POST a `/generate-story`
3. **Elaborazione del Controller**: `PetController` valida e sanifica l'input
4. **Chiamata al Servizio AI**: `StoryService` invia una richiesta all'API dei Modelli GitHub
5. **Generazione della Storia**: L'AI genera una storia creativa basata sulla descrizione
6. **Gestione della Risposta**: Il controller riceve la storia e la aggiunge al modello
7. **Rendering del Template**: Thymeleaf rende `result.html` con la storia
8. **Visualizzazione**: L'utente vede la storia generata nel browser

**Flusso di Gestione degli Errori:**
Se il servizio AI fallisce:
1. Il controller intercetta l'eccezione
2. Genera una storia di fallback utilizzando modelli pre-scritti
3. Mostra la storia di fallback con una nota sull'indisponibilità dell'AI
4. L'utente riceve comunque una storia, garantendo una buona esperienza utente

## Comprendere l'Integrazione con l'AI

### API Modelli GitHub
L'applicazione utilizza i Modelli GitHub, che forniscono accesso gratuito a vari modelli AI:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
Il servizio utilizza prompt attentamente progettati per ottenere buoni risultati:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Elaborazione della Risposta
La risposta dell'AI viene estratta e validata:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Prossimi Passi

Per ulteriori esempi, consulta [Capitolo 04: Esempi pratici](../README.md)

**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l'accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa dovrebbe essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un traduttore umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall'uso di questa traduzione.