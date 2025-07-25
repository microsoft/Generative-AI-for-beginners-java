<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:20:52+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "hr"
}
-->
# Vodič za početnike: Generator priča o kućnim ljubimcima

## Sadržaj

- [Preduvjeti](../../../../04-PracticalSamples/petstory)
- [Razumijevanje strukture projekta](../../../../04-PracticalSamples/petstory)
- [Objašnjenje ključnih komponenti](../../../../04-PracticalSamples/petstory)
  - [1. Glavna aplikacija](../../../../04-PracticalSamples/petstory)
  - [2. Web kontroler](../../../../04-PracticalSamples/petstory)
  - [3. Servis za priče](../../../../04-PracticalSamples/petstory)
  - [4. Web predlošci](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguracija](../../../../04-PracticalSamples/petstory)
- [Pokretanje aplikacije](../../../../04-PracticalSamples/petstory)
- [Kako sve funkcionira zajedno](../../../../04-PracticalSamples/petstory)
- [Razumijevanje AI integracije](../../../../04-PracticalSamples/petstory)
- [Sljedeći koraci](../../../../04-PracticalSamples/petstory)

## Preduvjeti

Prije početka, osigurajte da imate:
- Instaliran Java 21 ili noviji
- Maven za upravljanje ovisnostima
- GitHub račun s osobnim pristupnim tokenom (PAT) s dozvolom `models:read`
- Osnovno razumijevanje Jave, Spring Boota i web razvoja

## Razumijevanje strukture projekta

Projekt za priče o kućnim ljubimcima sadrži nekoliko važnih datoteka:

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

## Objašnjenje ključnih komponenti

### 1. Glavna aplikacija

**Datoteka:** `PetStoryApplication.java`

Ovo je ulazna točka za našu Spring Boot aplikaciju:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Što radi:**
- Anotacija `@SpringBootApplication` omogućuje automatsku konfiguraciju i skeniranje komponenti
- Pokreće ugrađeni web poslužitelj (Tomcat) na portu 8080
- Automatski kreira sve potrebne Spring beanove i servise

### 2. Web kontroler

**Datoteka:** `PetController.java`

Ovo upravlja svim web zahtjevima i interakcijama korisnika:

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

**Ključne značajke:**

1. **Upravljanje rutama**: `@GetMapping("/")` prikazuje obrazac za učitavanje, `@PostMapping("/generate-story")` obrađuje unose
2. **Validacija unosa**: Provjerava prazne opise i ograničenja duljine
3. **Sigurnost**: Sanitizira korisnički unos kako bi spriječio XSS napade
4. **Upravljanje greškama**: Pruža rezervne priče kada AI servis ne uspije
5. **Povezivanje modela**: Prosljeđuje podatke HTML predlošcima koristeći Springov `Model`

**Sustav za rezervne priče:**
Kontroler uključuje unaprijed napisane predloške priča koji se koriste kada AI servis nije dostupan:

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

### 3. Servis za priče

**Datoteka:** `StoryService.java`

Ovaj servis komunicira s GitHub Models kako bi generirao priče:

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

**Ključne komponente:**

1. **OpenAI klijent**: Koristi službeni OpenAI Java SDK konfiguriran za GitHub Models
2. **Sistemski prompt**: Postavlja ponašanje AI-a za pisanje obiteljskih priča o kućnim ljubimcima
3. **Korisnički prompt**: Govori AI-u točno kakvu priču treba napisati na temelju opisa
4. **Parametri**: Kontrolira duljinu priče i razinu kreativnosti
5. **Upravljanje greškama**: Generira iznimke koje kontroler hvata i obrađuje

### 4. Web predlošci

**Datoteka:** `index.html` (Obrazac za učitavanje)

Glavna stranica gdje korisnici opisuju svoje ljubimce:

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

**Datoteka:** `result.html` (Prikaz priče)

Prikazuje generiranu priču:

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

**Značajke predloška:**

1. **Integracija s Thymeleafom**: Koristi `th:` atribute za dinamički sadržaj
2. **Responzivni dizajn**: CSS stilovi za mobilne uređaje i desktop
3. **Upravljanje greškama**: Prikazuje greške validacije korisnicima
4. **Obrada na strani klijenta**: JavaScript za analizu slika (koristeći Transformers.js)

### 5. Konfiguracija

**Datoteka:** `application.properties`

Postavke konfiguracije za aplikaciju:

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

**Objašnjenje konfiguracije:**

1. **Učitavanje datoteka**: Omogućuje slike do 10MB
2. **Logiranje**: Kontrolira koje informacije se bilježe tijekom izvođenja
3. **GitHub Models**: Navodi koji AI model i endpoint se koriste
4. **Sigurnost**: Konfiguracija za upravljanje greškama kako bi se izbjeglo otkrivanje osjetljivih informacija

## Pokretanje aplikacije

### Korak 1: Postavite svoj GitHub token

Prvo, trebate postaviti svoj GitHub token kao varijablu okruženja:

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

**Zašto je ovo potrebno:**
- GitHub Models zahtijeva autentifikaciju za pristup AI modelima
- Korištenje varijabli okruženja čuva osjetljive tokene izvan izvornog koda
- Dozvola `models:read` omogućuje pristup AI inferenciji

### Korak 2: Izgradnja i pokretanje

Idite u direktorij projekta:
```bash
cd 04-PracticalSamples/petstory
```

Izgradite aplikaciju:
```bash
mvn clean compile
```

Pokrenite poslužitelj:
```bash
mvn spring-boot:run
```

Aplikacija će se pokrenuti na `http://localhost:8080`.

### Korak 3: Testiranje aplikacije

1. **Otvorite** `http://localhost:8080` u svom pregledniku
2. **Opišite** svog ljubimca u tekstualnom polju (npr. "Razigrani zlatni retriver koji voli donositi loptice")
3. **Kliknite** "Generiraj priču" za dobivanje AI-generirane priče
4. **Alternativno**, učitajte sliku ljubimca za automatsko generiranje opisa
5. **Pregledajte** kreativnu priču temeljenu na opisu vašeg ljubimca

## Kako sve funkcionira zajedno

Evo kompletnog toka kada generirate priču o kućnom ljubimcu:

1. **Korisnički unos**: Opišete svog ljubimca na web obrascu
2. **Slanje obrasca**: Preglednik šalje POST zahtjev na `/generate-story`
3. **Obrada kontrolera**: `PetController` validira i sanitizira unos
4. **Poziv AI servisa**: `StoryService` šalje zahtjev GitHub Models API-ju
5. **Generiranje priče**: AI generira kreativnu priču na temelju opisa
6. **Obrada odgovora**: Kontroler prima priču i dodaje je u model
7. **Renderiranje predloška**: Thymeleaf renderira `result.html` s pričom
8. **Prikaz**: Korisnik vidi generiranu priču u svom pregledniku

**Tok upravljanja greškama:**
Ako AI servis ne uspije:
1. Kontroler hvata iznimku
2. Generira rezervnu priču koristeći unaprijed napisane predloške
3. Prikazuje rezervnu priču s napomenom o nedostupnosti AI-a
4. Korisnik i dalje dobiva priču, osiguravajući dobro korisničko iskustvo

## Razumijevanje AI integracije

### GitHub Models API
Aplikacija koristi GitHub Models, koji pruža besplatan pristup raznim AI modelima:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Inženjering prompta
Servis koristi pažljivo osmišljene promte za postizanje dobrih rezultata:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Obrada odgovora
AI odgovor se izvlači i validira:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Sljedeći koraci

Za više primjera, pogledajte [Poglavlje 04: Praktični primjeri](../README.md)

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni prijevod od strane čovjeka. Ne preuzimamo odgovornost za nesporazume ili pogrešna tumačenja koja mogu proizaći iz korištenja ovog prijevoda.