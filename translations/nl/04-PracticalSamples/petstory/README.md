<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:48:28+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "nl"
}
-->
# Handleiding voor beginners: Pet Story Generator

## Inhoudsopgave

- [Vereisten](../../../../04-PracticalSamples/petstory)
- [De projectstructuur begrijpen](../../../../04-PracticalSamples/petstory)
- [Uitleg over de kerncomponenten](../../../../04-PracticalSamples/petstory)
  - [1. Hoofdapplicatie](../../../../04-PracticalSamples/petstory)
  - [2. Webcontroller](../../../../04-PracticalSamples/petstory)
  - [3. Verhaalservice](../../../../04-PracticalSamples/petstory)
  - [4. Webtemplates](../../../../04-PracticalSamples/petstory)
  - [5. Configuratie](../../../../04-PracticalSamples/petstory)
- [De applicatie uitvoeren](../../../../04-PracticalSamples/petstory)
- [Hoe alles samenwerkt](../../../../04-PracticalSamples/petstory)
- [De AI-integratie begrijpen](../../../../04-PracticalSamples/petstory)
- [Volgende stappen](../../../../04-PracticalSamples/petstory)

## Vereisten

Voordat je begint, zorg ervoor dat je het volgende hebt:
- Java 21 of hoger geïnstalleerd
- Maven voor afhankelijkheidsbeheer
- Een GitHub-account met een persoonlijke toegangstoken (PAT) met de scope `models:read`
- Basiskennis van Java, Spring Boot en webontwikkeling

## De projectstructuur begrijpen

Het pet story-project bevat verschillende belangrijke bestanden:

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

## Uitleg over de kerncomponenten

### 1. Hoofdapplicatie

**Bestand:** `PetStoryApplication.java`

Dit is het startpunt van onze Spring Boot-applicatie:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Wat dit doet:**
- De annotatie `@SpringBootApplication` schakelt automatische configuratie en componentenscanning in
- Start een ingebouwde webserver (Tomcat) op poort 8080
- Maakt automatisch alle benodigde Spring beans en services aan

### 2. Webcontroller

**Bestand:** `PetController.java`

Dit handelt alle webverzoeken en gebruikersinteracties af:

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

**Belangrijke kenmerken:**

1. **Routeverwerking**: `@GetMapping("/")` toont het uploadformulier, `@PostMapping("/generate-story")` verwerkt inzendingen
2. **Invoercontrole**: Controleert op lege beschrijvingen en lengtebeperkingen
3. **Beveiliging**: Zuivert gebruikersinvoer om XSS-aanvallen te voorkomen
4. **Foutafhandeling**: Biedt alternatieve verhalen wanneer de AI-service faalt
5. **Modelbinding**: Geeft gegevens door aan HTML-templates met behulp van Spring's `Model`

**Fallback-systeem:**
De controller bevat vooraf geschreven verhaaltemplates die worden gebruikt wanneer de AI-service niet beschikbaar is:

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

### 3. Verhaalservice

**Bestand:** `StoryService.java`

Deze service communiceert met GitHub Models om verhalen te genereren:

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

**Belangrijke componenten:**

1. **OpenAI Client**: Gebruikt de officiële OpenAI Java SDK geconfigureerd voor GitHub Models
2. **Systeemprompt**: Stelt het gedrag van de AI in om gezinsvriendelijke verhalen over huisdieren te schrijven
3. **Gebruikersprompt**: Geeft de AI precies aan welk verhaal geschreven moet worden op basis van de beschrijving
4. **Parameters**: Regelt de lengte van het verhaal en het creativiteitsniveau
5. **Foutafhandeling**: Gooit uitzonderingen die door de controller worden opgevangen en afgehandeld

### 4. Webtemplates

**Bestand:** `index.html` (Uploadformulier)

De hoofdpagina waar gebruikers hun huisdieren beschrijven:

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

**Bestand:** `result.html` (Verhaalweergave)

Toont het gegenereerde verhaal:

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

**Templatekenmerken:**

1. **Thymeleaf-integratie**: Gebruikt `th:`-attributen voor dynamische inhoud
2. **Responsief ontwerp**: CSS-styling voor mobiel en desktop
3. **Foutafhandeling**: Toont validatiefouten aan gebruikers
4. **Client-side verwerking**: JavaScript voor beeldanalyse (met Transformers.js)

### 5. Configuratie

**Bestand:** `application.properties`

Configuratie-instellingen voor de applicatie:

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

**Configuratie uitgelegd:**

1. **Bestandsupload**: Staat afbeeldingen toe tot 10MB
2. **Logging**: Regelt welke informatie wordt gelogd tijdens uitvoering
3. **GitHub Models**: Geeft aan welk AI-model en endpoint gebruikt moeten worden
4. **Beveiliging**: Configuratie voor foutafhandeling om te voorkomen dat gevoelige informatie wordt blootgesteld

## De applicatie uitvoeren

### Stap 1: Stel je GitHub-token in

Eerst moet je je GitHub-token instellen als een omgevingsvariabele:

**Windows (Command Prompt):**
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

**Waarom dit nodig is:**
- GitHub Models vereist authenticatie om toegang te krijgen tot AI-modellen
- Het gebruik van omgevingsvariabelen houdt gevoelige tokens buiten de broncode
- De scope `models:read` biedt toegang tot AI-inferentie

### Stap 2: Build en run

Navigeer naar de projectmap:
```bash
cd 04-PracticalSamples/petstory
```

Build de applicatie:
```bash
mvn clean compile
```

Start de server:
```bash
mvn spring-boot:run
```

De applicatie start op `http://localhost:8080`.

### Stap 3: Test de applicatie

1. **Open** `http://localhost:8080` in je browser
2. **Beschrijf** je huisdier in het tekstveld (bijv. "Een speelse golden retriever die graag apporteert")
3. **Klik** op "Generate Story" om een AI-gegenereerd verhaal te krijgen
4. **Of**, upload een afbeelding van je huisdier om automatisch een beschrijving te genereren
5. **Bekijk** het creatieve verhaal gebaseerd op de beschrijving van je huisdier

## Hoe alles samenwerkt

Hier is de volledige flow wanneer je een verhaal over je huisdier genereert:

1. **Gebruikersinvoer**: Je beschrijft je huisdier op het webformulier
2. **Formulierinzending**: Browser stuurt een POST-verzoek naar `/generate-story`
3. **Controllerverwerking**: `PetController` valideert en zuivert de invoer
4. **AI-serviceaanroep**: `StoryService` stuurt een verzoek naar de GitHub Models API
5. **Verhaalgeneratie**: AI genereert een creatief verhaal op basis van de beschrijving
6. **Responsverwerking**: Controller ontvangt het verhaal en voegt het toe aan het model
7. **Template-rendering**: Thymeleaf rendert `result.html` met het verhaal
8. **Weergave**: Gebruiker ziet het gegenereerde verhaal in de browser

**Foutafhandelingsflow:**
Als de AI-service faalt:
1. Controller vangt de uitzondering op
2. Genereert een alternatief verhaal met vooraf geschreven templates
3. Toont het alternatieve verhaal met een melding over de onbeschikbaarheid van de AI
4. Gebruiker krijgt alsnog een verhaal, wat zorgt voor een goede gebruikerservaring

## De AI-integratie begrijpen

### GitHub Models API
De applicatie gebruikt GitHub Models, die gratis toegang biedt tot verschillende AI-modellen:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt-engineering
De service gebruikt zorgvuldig samengestelde prompts om goede resultaten te krijgen:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Responsverwerking
De AI-respons wordt geëxtraheerd en gevalideerd:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Volgende stappen

Voor meer voorbeelden, zie [Hoofdstuk 04: Praktische voorbeelden](../README.md)

**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.