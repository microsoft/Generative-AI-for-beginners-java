<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:39:17+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "sv"
}
-->
# Handledning för nybörjare: Pet Story Generator

## Innehållsförteckning

- [Förutsättningar](../../../../04-PracticalSamples/petstory)
- [Förstå projektstrukturen](../../../../04-PracticalSamples/petstory)
- [Förklaring av kärnkomponenter](../../../../04-PracticalSamples/petstory)
  - [1. Huvudapplikation](../../../../04-PracticalSamples/petstory)
  - [2. Webbkontroller](../../../../04-PracticalSamples/petstory)
  - [3. Storytjänst](../../../../04-PracticalSamples/petstory)
  - [4. Webbmallar](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguration](../../../../04-PracticalSamples/petstory)
- [Köra applikationen](../../../../04-PracticalSamples/petstory)
- [Hur allt fungerar tillsammans](../../../../04-PracticalSamples/petstory)
- [Förstå AI-integrationen](../../../../04-PracticalSamples/petstory)
- [Nästa steg](../../../../04-PracticalSamples/petstory)

## Förutsättningar

Innan du börjar, se till att du har:
- Java 21 eller senare installerat
- Maven för hantering av beroenden
- Ett GitHub-konto med en personlig åtkomsttoken (PAT) med `models:read`-behörighet
- Grundläggande förståelse för Java, Spring Boot och webbutveckling

## Förstå projektstrukturen

Pet Story-projektet innehåller flera viktiga filer:

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

## Förklaring av kärnkomponenter

### 1. Huvudapplikation

**Fil:** `PetStoryApplication.java`

Detta är startpunkten för vår Spring Boot-applikation:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Vad den gör:**
- `@SpringBootApplication`-annoteringen aktiverar autokonfiguration och komponentgenomsökning
- Startar en inbäddad webbserver (Tomcat) på port 8080
- Skapar automatiskt alla nödvändiga Spring-beans och tjänster

### 2. Webbkontroller

**Fil:** `PetController.java`

Hantera alla webbförfrågningar och användarinteraktioner:

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

**Viktiga funktioner:**

1. **Routhantering**: `@GetMapping("/")` visar uppladdningsformuläret, `@PostMapping("/generate-story")` bearbetar inskickningar
2. **Validering av indata**: Kontrollerar tomma beskrivningar och längdbegränsningar
3. **Säkerhet**: Rensar användarinmatning för att förhindra XSS-attacker
4. **Felkorrigering**: Tillhandahåller reservberättelser när AI-tjänsten misslyckas
5. **Modelbindning**: Skickar data till HTML-mallar med hjälp av Springs `Model`

**Reservsystem:**
Kontrollern innehåller förskrivna berättelsemallar som används när AI-tjänsten inte är tillgänglig:

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

### 3. Storytjänst

**Fil:** `StoryService.java`

Denna tjänst kommunicerar med GitHub Models för att generera berättelser:

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

**Viktiga komponenter:**

1. **OpenAI-klient**: Använder den officiella OpenAI Java SDK konfigurerad för GitHub Models
2. **Systemprompt**: Ställer in AI:s beteende för att skriva familjevänliga berättelser om husdjur
3. **Användarprompt**: Ber AI att skriva en specifik berättelse baserat på beskrivningen
4. **Parametrar**: Styr berättelsens längd och kreativitet
5. **Felkorrigering**: Kastar undantag som kontrollern fångar och hanterar

### 4. Webbmallar

**Fil:** `index.html` (Uppladdningsformulär)

Huvudsidan där användare beskriver sina husdjur:

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

**Fil:** `result.html` (Visning av berättelse)

Visar den genererade berättelsen:

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

**Mallfunktioner:**

1. **Thymeleaf-integration**: Använder `th:`-attribut för dynamiskt innehåll
2. **Responsiv design**: CSS-styling för mobil och desktop
3. **Felkorrigering**: Visar valideringsfel för användare
4. **Klientbearbetning**: JavaScript för bildanalys (med Transformers.js)

### 5. Konfiguration

**Fil:** `application.properties`

Konfigurationsinställningar för applikationen:

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

**Förklaring av konfiguration:**

1. **Filuppladdning**: Tillåter bilder upp till 10 MB
2. **Loggning**: Styr vilken information som loggas under körning
3. **GitHub Models**: Anger vilken AI-modell och endpoint som ska användas
4. **Säkerhet**: Konfiguration för felhantering för att undvika att exponera känslig information

## Köra applikationen

### Steg 1: Ställ in din GitHub-token

Först måste du ställa in din GitHub-token som en miljövariabel:

**Windows (Kommandotolken):**
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

**Varför detta behövs:**
- GitHub Models kräver autentisering för att få åtkomst till AI-modeller
- Att använda miljövariabler håller känsliga tokens utanför källkoden
- `models:read`-behörigheten ger åtkomst till AI-inferens

### Steg 2: Bygg och kör

Navigera till projektkatalogen:
```bash
cd 04-PracticalSamples/petstory
```

Bygg applikationen:
```bash
mvn clean compile
```

Starta servern:
```bash
mvn spring-boot:run
```

Applikationen startar på `http://localhost:8080`.

### Steg 3: Testa applikationen

1. **Öppna** `http://localhost:8080` i din webbläsare
2. **Beskriv** ditt husdjur i textområdet (t.ex. "En lekfull golden retriever som älskar att hämta")
3. **Klicka** på "Generate Story" för att få en AI-genererad berättelse
4. **Alternativt**, ladda upp en bild på ditt husdjur för att automatiskt generera en beskrivning
5. **Visa** den kreativa berättelsen baserat på beskrivningen av ditt husdjur

## Hur allt fungerar tillsammans

Så här går det till när du genererar en berättelse om ditt husdjur:

1. **Användarinmatning**: Du beskriver ditt husdjur i webbformuläret
2. **Formulärinskickning**: Webbläsaren skickar en POST-förfrågan till `/generate-story`
3. **Kontrollerbearbetning**: `PetController` validerar och rensar inmatningen
4. **AI-tjänstanrop**: `StoryService` skickar en förfrågan till GitHub Models API
5. **Berättelsegenerering**: AI skapar en kreativ berättelse baserat på beskrivningen
6. **Svarshantering**: Kontrollern tar emot berättelsen och lägger till den i modellen
7. **Mallrendering**: Thymeleaf renderar `result.html` med berättelsen
8. **Visning**: Användaren ser den genererade berättelsen i sin webbläsare

**Felkorrigeringsflöde:**
Om AI-tjänsten misslyckas:
1. Kontrollern fångar undantaget
2. Genererar en reservberättelse med förskrivna mallar
3. Visar reservberättelsen med en notis om att AI inte är tillgänglig
4. Användaren får fortfarande en berättelse, vilket säkerställer en bra användarupplevelse

## Förstå AI-integrationen

### GitHub Models API
Applikationen använder GitHub Models, som erbjuder gratis åtkomst till olika AI-modeller:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Promptteknik
Tjänsten använder noggrant utformade prompts för att få bra resultat:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Bearbetning av svar
AI-svaret extraheras och valideras:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Nästa steg

För fler exempel, se [Kapitel 04: Praktiska exempel](../README.md)

**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiserade översättningar kan innehålla fel eller felaktigheter. Det ursprungliga dokumentet på dess originalspråk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.