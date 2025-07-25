<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:43:48+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "no"
}
-->
# Veiledning for nybegynnere: Generering av dyrehistorier

## Innholdsfortegnelse

- [Forutsetninger](../../../../04-PracticalSamples/petstory)
- [Forstå prosjektstrukturen](../../../../04-PracticalSamples/petstory)
- [Forklaring av kjernekomponenter](../../../../04-PracticalSamples/petstory)
  - [1. Hovedapplikasjon](../../../../04-PracticalSamples/petstory)
  - [2. Webkontroller](../../../../04-PracticalSamples/petstory)
  - [3. Historietjeneste](../../../../04-PracticalSamples/petstory)
  - [4. Webmaler](../../../../04-PracticalSamples/petstory)
  - [5. Konfigurasjon](../../../../04-PracticalSamples/petstory)
- [Kjøre applikasjonen](../../../../04-PracticalSamples/petstory)
- [Hvordan alt fungerer sammen](../../../../04-PracticalSamples/petstory)
- [Forstå AI-integrasjonen](../../../../04-PracticalSamples/petstory)
- [Neste steg](../../../../04-PracticalSamples/petstory)

## Forutsetninger

Før du starter, sørg for at du har:
- Java 21 eller nyere installert
- Maven for avhengighetsstyring
- En GitHub-konto med en personlig tilgangstoken (PAT) med `models:read`-tillatelse
- Grunnleggende forståelse av Java, Spring Boot og webutvikling

## Forstå prosjektstrukturen

Dyrehistorieprosjektet har flere viktige filer:

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

## Forklaring av kjernekomponenter

### 1. Hovedapplikasjon

**Fil:** `PetStoryApplication.java`

Dette er startpunktet for vår Spring Boot-applikasjon:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Hva dette gjør:**
- `@SpringBootApplication`-annotasjonen aktiverer autokonfigurasjon og komponentskanning
- Starter en innebygd webserver (Tomcat) på port 8080
- Oppretter alle nødvendige Spring-beans og tjenester automatisk

### 2. Webkontroller

**Fil:** `PetController.java`

Denne håndterer alle webforespørsler og brukerinteraksjoner:

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

**Nøkkelfunksjoner:**

1. **Rutehåndtering**: `@GetMapping("/")` viser opplastingsskjemaet, `@PostMapping("/generate-story")` behandler innsendinger
2. **Inputvalidering**: Sjekker for tomme beskrivelser og lengdebegrensninger
3. **Sikkerhet**: Rensker brukerinput for å forhindre XSS-angrep
4. **Feilhåndtering**: Tilbyr fallback-historier når AI-tjenesten feiler
5. **Modellbinding**: Sender data til HTML-maler ved hjelp av Spring's `Model`

**Fallback-system:**
Kontrolleren inkluderer forhåndsskrevne historiemaler som brukes når AI-tjenesten ikke er tilgjengelig:

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

Denne tjenesten kommuniserer med GitHub Models for å generere historier:

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

**Nøkkelkomponenter:**

1. **OpenAI-klient**: Bruker den offisielle OpenAI Java SDK konfigurert for GitHub Models
2. **Systemprompt**: Setter AI-ens oppførsel til å skrive familievennlige dyrehistorier
3. **Brukerprompt**: Forteller AI nøyaktig hvilken historie som skal skrives basert på beskrivelsen
4. **Parametere**: Kontrollerer historielengde og kreativitet
5. **Feilhåndtering**: Kaster unntak som kontrolleren fanger opp og håndterer

### 4. Webmaler

**Fil:** `index.html` (Opplastingsskjema)

Hovedsiden hvor brukere beskriver sine kjæledyr:

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

**Fil:** `result.html` (Visning av historie)

Viser den genererte historien:

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

**Mal-funksjoner:**

1. **Thymeleaf-integrasjon**: Bruker `th:`-attributter for dynamisk innhold
2. **Responsivt design**: CSS-styling for mobil og desktop
3. **Feilhåndtering**: Viser valideringsfeil til brukere
4. **Klientbehandling**: JavaScript for bildeanalyse (ved bruk av Transformers.js)

### 5. Konfigurasjon

**Fil:** `application.properties`

Konfigurasjonsinnstillinger for applikasjonen:

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

**Konfigurasjon forklart:**

1. **Filopplasting**: Tillater bilder opptil 10MB
2. **Logging**: Kontrollerer hvilken informasjon som logges under kjøring
3. **GitHub Models**: Spesifiserer hvilken AI-modell og endepunkt som skal brukes
4. **Sikkerhet**: Konfigurasjon for feilhåndtering for å unngå eksponering av sensitiv informasjon

## Kjøre applikasjonen

### Steg 1: Sett GitHub-tokenet ditt

Først må du sette GitHub-tokenet som en miljøvariabel:

**Windows (Kommandolinje):**
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

**Hvorfor dette er nødvendig:**
- GitHub Models krever autentisering for å få tilgang til AI-modeller
- Bruk av miljøvariabler holder sensitive token utenfor kildekoden
- `models:read`-tillatelsen gir tilgang til AI-inferens

### Steg 2: Bygg og kjør

Naviger til prosjektmappen:
```bash
cd 04-PracticalSamples/petstory
```

Bygg applikasjonen:
```bash
mvn clean compile
```

Start serveren:
```bash
mvn spring-boot:run
```

Applikasjonen starter på `http://localhost:8080`.

### Steg 3: Test applikasjonen

1. **Åpne** `http://localhost:8080` i nettleseren din
2. **Beskriv** kjæledyret ditt i tekstfeltet (f.eks. "En leken golden retriever som elsker å hente")
3. **Klikk** "Generer historie" for å få en AI-generert historie
4. **Alternativt**, last opp et bilde av kjæledyret ditt for automatisk å generere en beskrivelse
5. **Se** den kreative historien basert på beskrivelsen av kjæledyret ditt

## Hvordan alt fungerer sammen

Her er den komplette flyten når du genererer en dyrehistorie:

1. **Brukerinput**: Du beskriver kjæledyret ditt i webskjemaet
2. **Skjemainnsending**: Nettleseren sender en POST-forespørsel til `/generate-story`
3. **Kontrollerbehandling**: `PetController` validerer og renser input
4. **AI-tjenestekall**: `StoryService` sender forespørsel til GitHub Models API
5. **Historiegenerering**: AI genererer en kreativ historie basert på beskrivelsen
6. **Responsbehandling**: Kontrolleren mottar historien og legger den til modellen
7. **Malrendering**: Thymeleaf renderer `result.html` med historien
8. **Visning**: Brukeren ser den genererte historien i nettleseren

**Feilhåndteringsflyt:**
Hvis AI-tjenesten feiler:
1. Kontrolleren fanger opp unntaket
2. Genererer en fallback-historie ved hjelp av forhåndsskrevne maler
3. Viser fallback-historien med en beskjed om AI-utilgjengelighet
4. Brukeren får fortsatt en historie, noe som sikrer en god brukeropplevelse

## Forstå AI-integrasjonen

### GitHub Models API
Applikasjonen bruker GitHub Models, som gir gratis tilgang til ulike AI-modeller:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
Tjenesten bruker nøye utformede prompts for å få gode resultater:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Responsbehandling
AI-responsen blir hentet ut og validert:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Neste steg

For flere eksempler, se [Kapittel 04: Praktiske eksempler](../README.md)

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.