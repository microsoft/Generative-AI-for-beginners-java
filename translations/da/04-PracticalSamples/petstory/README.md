<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:41:31+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "da"
}
-->
# Vejledning til Pet Story Generator for begyndere

## Indholdsfortegnelse

- [Forudsætninger](../../../../04-PracticalSamples/petstory)
- [Forståelse af projektstrukturen](../../../../04-PracticalSamples/petstory)
- [Forklaring af kernekomponenter](../../../../04-PracticalSamples/petstory)
  - [1. Hovedapplikation](../../../../04-PracticalSamples/petstory)
  - [2. Webkontroller](../../../../04-PracticalSamples/petstory)
  - [3. Historietjeneste](../../../../04-PracticalSamples/petstory)
  - [4. Webskabeloner](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguration](../../../../04-PracticalSamples/petstory)
- [Sådan kører du applikationen](../../../../04-PracticalSamples/petstory)
- [Hvordan det hele hænger sammen](../../../../04-PracticalSamples/petstory)
- [Forståelse af AI-integration](../../../../04-PracticalSamples/petstory)
- [Næste skridt](../../../../04-PracticalSamples/petstory)

## Forudsætninger

Før du starter, skal du sikre dig, at du har:
- Java 21 eller nyere installeret
- Maven til afhængighedsstyring
- En GitHub-konto med en personlig adgangstoken (PAT) med `models:read`-tilladelse
- Grundlæggende forståelse af Java, Spring Boot og webudvikling

## Forståelse af projektstrukturen

Pet Story-projektet indeholder flere vigtige filer:

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

## Forklaring af kernekomponenter

### 1. Hovedapplikation

**Fil:** `PetStoryApplication.java`

Dette er startpunktet for vores Spring Boot-applikation:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Hvad denne gør:**
- `@SpringBootApplication`-annoteringen aktiverer auto-konfiguration og komponentscanning
- Starter en indlejret webserver (Tomcat) på port 8080
- Opretter automatisk alle nødvendige Spring-beans og tjenester

### 2. Webkontroller

**Fil:** `PetController.java`

Denne håndterer alle webanmodninger og brugerinteraktioner:

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

**Vigtige funktioner:**

1. **Rutehåndtering**: `@GetMapping("/")` viser uploadformularen, `@PostMapping("/generate-story")` behandler indsendelser
2. **Inputvalidering**: Tjekker for tomme beskrivelser og længdebegrænsninger
3. **Sikkerhed**: Renser brugerinput for at forhindre XSS-angreb
4. **Fejlhåndtering**: Tilbyder fallback-historier, når AI-tjenesten fejler
5. **Modelbinding**: Sender data til HTML-skabeloner ved hjælp af Springs `Model`

**Fallback-system:**
Kontrolleren inkluderer forudskrevne historie-skabeloner, der bruges, når AI-tjenesten ikke er tilgængelig:

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

### 3. Historietjeneste

**Fil:** `StoryService.java`

Denne tjeneste kommunikerer med GitHub Models for at generere historier:

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

**Vigtige komponenter:**

1. **OpenAI-klient**: Bruger den officielle OpenAI Java SDK konfigureret til GitHub Models
2. **Systemprompt**: Indstiller AI'ens adfærd til at skrive familievenlige kæledyrshistorier
3. **Brugerpåmindelse**: Fortæller AI præcis, hvilken historie der skal skrives baseret på beskrivelsen
4. **Parametre**: Styrer historiens længde og kreativitet
5. **Fejlhåndtering**: Kaster undtagelser, som kontrolleren fanger og håndterer

### 4. Webskabeloner

**Fil:** `index.html` (Uploadformular)

Hovedsiden, hvor brugere beskriver deres kæledyr:

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

**Fil:** `result.html` (Visning af historie)

Viser den genererede historie:

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

**Skabelonfunktioner:**

1. **Thymeleaf-integration**: Bruger `th:`-attributter til dynamisk indhold
2. **Responsivt design**: CSS-styling til mobil og desktop
3. **Fejlhåndtering**: Viser valideringsfejl til brugerne
4. **Klient-side behandling**: JavaScript til billedanalyse (ved hjælp af Transformers.js)

### 5. Konfiguration

**Fil:** `application.properties`

Konfigurationsindstillinger for applikationen:

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

**Forklaring af konfiguration:**

1. **Filupload**: Tillader billeder op til 10MB
2. **Logning**: Styrer, hvilke oplysninger der logges under udførelse
3. **GitHub Models**: Angiver, hvilken AI-model og endpoint der skal bruges
4. **Sikkerhed**: Fejlhåndteringskonfiguration for at undgå eksponering af følsomme oplysninger

## Sådan kører du applikationen

### Trin 1: Indstil din GitHub-token

Først skal du indstille din GitHub-token som en miljøvariabel:

**Windows (Kommandoprompt):**
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

**Hvorfor dette er nødvendigt:**
- GitHub Models kræver autentificering for at få adgang til AI-modeller
- Brug af miljøvariabler holder følsomme tokens ude af kildekoden
- `models:read`-tilladelsen giver adgang til AI-inferens

### Trin 2: Byg og kør

Naviger til projektmappen:
```bash
cd 04-PracticalSamples/petstory
```

Byg applikationen:
```bash
mvn clean compile
```

Start serveren:
```bash
mvn spring-boot:run
```

Applikationen starter på `http://localhost:8080`.

### Trin 3: Test applikationen

1. **Åbn** `http://localhost:8080` i din browser
2. **Beskriv** dit kæledyr i tekstfeltet (f.eks. "En legesyg golden retriever, der elsker at hente")
3. **Klik** på "Generate Story" for at få en AI-genereret historie
4. **Alternativt**, upload et billede af dit kæledyr for automatisk at generere en beskrivelse
5. **Se** den kreative historie baseret på beskrivelsen af dit kæledyr

## Hvordan det hele hænger sammen

Her er det komplette flow, når du genererer en kæledyrshistorie:

1. **Brugerinput**: Du beskriver dit kæledyr i webformularen
2. **Formularindsendelse**: Browseren sender en POST-anmodning til `/generate-story`
3. **Kontrollerbehandling**: `PetController` validerer og renser input
4. **AI-tjenesteopkald**: `StoryService` sender en anmodning til GitHub Models API
5. **Historiegenerering**: AI genererer en kreativ historie baseret på beskrivelsen
6. **Responsbehandling**: Kontrolleren modtager historien og tilføjer den til modellen
7. **Skabelonrendering**: Thymeleaf gengiver `result.html` med historien
8. **Visning**: Brugeren ser den genererede historie i deres browser

**Fejlhåndteringsflow:**
Hvis AI-tjenesten fejler:
1. Kontrolleren fanger undtagelsen
2. Genererer en fallback-historie ved hjælp af forudskrevne skabeloner
3. Viser fallback-historien med en note om AI-utilgængelighed
4. Brugeren får stadig en historie, hvilket sikrer en god brugeroplevelse

## Forståelse af AI-integration

### GitHub Models API
Applikationen bruger GitHub Models, som giver gratis adgang til forskellige AI-modeller:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
Tjenesten bruger nøje udformede prompts for at opnå gode resultater:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Responsbehandling
AI-responsen udtrækkes og valideres:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Næste skridt

For flere eksempler, se [Kapitel 04: Praktiske eksempler](../README.md)

**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på at sikre nøjagtighed, skal det bemærkes, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os ikke ansvar for eventuelle misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.