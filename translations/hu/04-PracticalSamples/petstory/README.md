<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:05:12+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "hu"
}
-->
# Kisállat Történet Generátor Kezdőknek

## Tartalomjegyzék

- [Előfeltételek](../../../../04-PracticalSamples/petstory)
- [A projekt struktúrájának megértése](../../../../04-PracticalSamples/petstory)
- [Főbb komponensek magyarázata](../../../../04-PracticalSamples/petstory)
  - [1. Fő alkalmazás](../../../../04-PracticalSamples/petstory)
  - [2. Webes vezérlő](../../../../04-PracticalSamples/petstory)
  - [3. Történet szolgáltatás](../../../../04-PracticalSamples/petstory)
  - [4. Webes sablonok](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguráció](../../../../04-PracticalSamples/petstory)
- [Az alkalmazás futtatása](../../../../04-PracticalSamples/petstory)
- [Hogyan működik együtt minden](../../../../04-PracticalSamples/petstory)
- [Az AI integráció megértése](../../../../04-PracticalSamples/petstory)
- [Következő lépések](../../../../04-PracticalSamples/petstory)

## Előfeltételek

Mielőtt elkezdenéd, győződj meg róla, hogy rendelkezel az alábbiakkal:
- Telepített Java 21 vagy újabb verzió
- Maven a függőségek kezeléséhez
- GitHub fiók személyes hozzáférési tokennel (PAT), amely rendelkezik `models:read` jogosultsággal
- Alapvető ismeretek Java, Spring Boot és webfejlesztés terén

## A projekt struktúrájának megértése

A kisállat történet projekt több fontos fájlt tartalmaz:

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

## Főbb komponensek magyarázata

### 1. Fő alkalmazás

**Fájl:** `PetStoryApplication.java`

Ez a Spring Boot alkalmazás belépési pontja:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Mit csinál ez:**
- Az `@SpringBootApplication` annotáció engedélyezi az automatikus konfigurációt és a komponenskeresést
- Elindít egy beágyazott webszervert (Tomcat) a 8080-as porton
- Automatikusan létrehozza a szükséges Spring bean-eket és szolgáltatásokat

### 2. Webes vezérlő

**Fájl:** `PetController.java`

Ez kezeli az összes webes kérést és felhasználói interakciót:

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

**Főbb jellemzők:**

1. **Útvonalkezelés**: Az `@GetMapping("/")` megjeleníti a feltöltési űrlapot, az `@PostMapping("/generate-story")` feldolgozza a beküldéseket
2. **Bemenet validálás**: Ellenőrzi az üres leírásokat és a hosszúsági korlátokat
3. **Biztonság**: Tisztítja a felhasználói bemenetet az XSS támadások elkerülése érdekében
4. **Hibakezelés**: Tartalék történeteket biztosít, ha az AI szolgáltatás nem elérhető
5. **Modellezés**: Adatokat ad át a HTML sablonoknak a Spring `Model` segítségével

**Tartalék rendszer:**
A vezérlő előre megírt történetsablonokat tartalmaz, amelyeket akkor használ, ha az AI szolgáltatás nem elérhető:

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

### 3. Történet szolgáltatás

**Fájl:** `StoryService.java`

Ez a szolgáltatás kommunikál a GitHub Models-szel történetek generálásához:

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

**Főbb elemek:**

1. **OpenAI kliens**: Az OpenAI hivatalos Java SDK-ját használja, amely a GitHub Models-hez van konfigurálva
2. **Rendszer prompt**: Beállítja az AI viselkedését családbarát kisállat történetek írására
3. **Felhasználói prompt**: Pontosan megadja az AI-nak, hogy milyen történetet írjon a leírás alapján
4. **Paraméterek**: Szabályozza a történet hosszát és kreativitási szintjét
5. **Hibakezelés**: Kivételt dob, amelyet a vezérlő elkap és kezel

### 4. Webes sablonok

**Fájl:** `index.html` (Feltöltési űrlap)

A főoldal, ahol a felhasználók leírják kisállataikat:

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

**Fájl:** `result.html` (Történet megjelenítése)

Megjeleníti a generált történetet:

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

**Sablon jellemzők:**

1. **Thymeleaf integráció**: `th:` attribútumokat használ a dinamikus tartalomhoz
2. **Reszponzív dizájn**: CSS stílus mobilra és asztali gépre
3. **Hibakezelés**: Megjeleníti a validációs hibákat a felhasználóknak
4. **Kliensoldali feldolgozás**: JavaScript az képelemzéshez (Transformers.js használatával)

### 5. Konfiguráció

**Fájl:** `application.properties`

Az alkalmazás konfigurációs beállításai:

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

**Konfiguráció magyarázata:**

1. **Fájl feltöltés**: Legfeljebb 10 MB méretű képeket engedélyez
2. **Naplózás**: Szabályozza, hogy milyen információkat naplózzon a futás során
3. **GitHub Models**: Meghatározza, hogy melyik AI modellt és végpontot használja
4. **Biztonság**: Hibakezelési beállítások az érzékeny információk elrejtésére

## Az alkalmazás futtatása

### 1. lépés: Állítsd be a GitHub tokent

Először állítsd be a GitHub tokent környezeti változóként:

**Windows (Parancssor):**
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

**Miért van erre szükség:**
- A GitHub Models hitelesítést igényel az AI modellek eléréséhez
- A környezeti változók használata megakadályozza, hogy az érzékeny tokenek bekerüljenek a forráskódba
- A `models:read` jogosultság hozzáférést biztosít az AI inferenciához

### 2. lépés: Építés és futtatás

Navigálj a projekt könyvtárába:
```bash
cd 04-PracticalSamples/petstory
```

Építsd meg az alkalmazást:
```bash
mvn clean compile
```

Indítsd el a szervert:
```bash
mvn spring-boot:run
```

Az alkalmazás elindul a `http://localhost:8080` címen.

### 3. lépés: Teszteld az alkalmazást

1. **Nyisd meg** a `http://localhost:8080` címet a böngésződben
2. **Írd le** a kisállatodat a szövegmezőben (pl. "Egy játékos golden retriever, aki imád apportírozni")
3. **Kattints** a "Történet generálása" gombra, hogy AI által generált történetet kapj
4. **Alternatívaként**, tölts fel egy kisállat képet, hogy automatikusan generáljon egy leírást
5. **Tekintsd meg** a kreatív történetet a kisállatod leírása alapján

## Hogyan működik együtt minden

Íme a teljes folyamat, amikor egy kisállat történetet generálsz:

1. **Felhasználói bemenet**: Leírod a kisállatodat a webes űrlapon
2. **Űrlap beküldése**: A böngésző POST kérést küld a `/generate-story` végpontra
3. **Vezérlő feldolgozás**: A `PetController` validálja és tisztítja a bemenetet
4. **AI szolgáltatás hívás**: A `StoryService` kérést küld a GitHub Models API-hoz
5. **Történet generálás**: Az AI kreatív történetet generál a leírás alapján
6. **Válasz feldolgozás**: A vezérlő megkapja a történetet, és hozzáadja a modellhez
7. **Sablon renderelés**: A Thymeleaf rendereli a `result.html` fájlt a történettel
8. **Megjelenítés**: A felhasználó látja a generált történetet a böngészőjében

**Hibakezelési folyamat:**
Ha az AI szolgáltatás hibát jelez:
1. A vezérlő elkapja a kivételt
2. Tartalék történetet generál az előre megírt sablonok segítségével
3. Megjeleníti a tartalék történetet egy megjegyzéssel az AI elérhetetlenségéről
4. A felhasználó így is kap egy történetet, biztosítva a jó felhasználói élményt

## Az AI integráció megértése

### GitHub Models API
Az alkalmazás a GitHub Models-t használja, amely ingyenes hozzáférést biztosít különböző AI modellekhez:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt tervezés
A szolgáltatás gondosan megfogalmazott promptokat használ a jó eredmények érdekében:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Válasz feldolgozás
Az AI válaszát kinyeri és validálja:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Következő lépések

További példákért lásd: [4. fejezet: Gyakorlati példák](../README.md)

**Felelősségkizárás**:  
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordítási szolgáltatás segítségével készült. Bár törekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelvén tekintendő hiteles forrásnak. Kritikus információk esetén javasolt professzionális, emberi fordítást igénybe venni. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.