<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:23:06+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "sl"
}
-->
# Vadnica za začetnike: Generator zgodb o hišnih ljubljenčkih

## Kazalo

- [Predpogoji](../../../../04-PracticalSamples/petstory)
- [Razumevanje strukture projekta](../../../../04-PracticalSamples/petstory)
- [Razlaga ključnih komponent](../../../../04-PracticalSamples/petstory)
  - [1. Glavna aplikacija](../../../../04-PracticalSamples/petstory)
  - [2. Spletni krmilnik](../../../../04-PracticalSamples/petstory)
  - [3. Storitev za zgodbe](../../../../04-PracticalSamples/petstory)
  - [4. Spletne predloge](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguracija](../../../../04-PracticalSamples/petstory)
- [Zagon aplikacije](../../../../04-PracticalSamples/petstory)
- [Kako vse deluje skupaj](../../../../04-PracticalSamples/petstory)
- [Razumevanje AI integracije](../../../../04-PracticalSamples/petstory)
- [Naslednji koraki](../../../../04-PracticalSamples/petstory)

## Predpogoji

Pred začetkom se prepričajte, da imate:
- Nameščen Java 21 ali novejši
- Maven za upravljanje odvisnosti
- GitHub račun z osebnim dostopnim žetonom (PAT) z dovoljenjem `models:read`
- Osnovno razumevanje Jave, Spring Boota in spletnega razvoja

## Razumevanje strukture projekta

Projekt za zgodbe o hišnih ljubljenčkih vsebuje več pomembnih datotek:

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

## Razlaga ključnih komponent

### 1. Glavna aplikacija

**Datoteka:** `PetStoryApplication.java`

To je vstopna točka za našo Spring Boot aplikacijo:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Kaj počne:**
- Anotacija `@SpringBootApplication` omogoča samodejno konfiguracijo in skeniranje komponent
- Zažene vgrajeni spletni strežnik (Tomcat) na vratih 8080
- Samodejno ustvari vse potrebne Spring bean-e in storitve

### 2. Spletni krmilnik

**Datoteka:** `PetController.java`

Upravlja vse spletne zahteve in interakcije z uporabniki:

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

**Ključne funkcije:**

1. **Upravljanje poti**: `@GetMapping("/")` prikaže obrazec za nalaganje, `@PostMapping("/generate-story")` obdeluje oddaje
2. **Validacija vnosa**: Preverja prazne opise in omejitve dolžine
3. **Varnost**: Čisti uporabniške vnose za preprečevanje XSS napadov
4. **Upravljanje napak**: Zagotavlja rezervne zgodbe, ko storitev AI ne deluje
5. **Povezovanje modelov**: Posreduje podatke v HTML predloge z uporabo Springovega `Model`

**Sistem rezervnih zgodb:**
Krmilnik vključuje vnaprej napisane predloge zgodb, ki se uporabljajo, ko storitev AI ni na voljo:

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

### 3. Storitev za zgodbe

**Datoteka:** `StoryService.java`

Ta storitev komunicira z GitHub Models za generiranje zgodb:

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

1. **OpenAI odjemalec**: Uporablja uradni OpenAI Java SDK, konfiguriran za GitHub Models
2. **Sistemski poziv**: Nastavi vedenje AI za pisanje družinam prijaznih zgodb o hišnih ljubljenčkih
3. **Uporabniški poziv**: Pove AI, kakšno zgodbo naj napiše glede na opis
4. **Parametri**: Nadzoruje dolžino zgodbe in raven ustvarjalnosti
5. **Upravljanje napak**: Vrača izjeme, ki jih krmilnik ujame in obravnava

### 4. Spletne predloge

**Datoteka:** `index.html` (Obrazec za nalaganje)

Glavna stran, kjer uporabniki opisujejo svoje hišne ljubljenčke:

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

**Datoteka:** `result.html` (Prikaz zgodbe)

Prikaže generirano zgodbo:

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

**Funkcije predloge:**

1. **Integracija Thymeleaf**: Uporablja `th:` atribute za dinamično vsebino
2. **Prilagodljiv dizajn**: CSS oblikovanje za mobilne naprave in namizje
3. **Upravljanje napak**: Prikazuje uporabnikom validacijske napake
4. **Obdelava na strani odjemalca**: JavaScript za analizo slik (z uporabo Transformers.js)

### 5. Konfiguracija

**Datoteka:** `application.properties`

Nastavitve konfiguracije za aplikacijo:

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

**Razlaga konfiguracije:**

1. **Nalaganje datotek**: Omogoča slike do velikosti 10 MB
2. **Beleženje**: Nadzoruje, katere informacije se beležijo med izvajanjem
3. **GitHub Models**: Določa, kateri AI model in končno točko uporabiti
4. **Varnost**: Konfiguracija upravljanja napak za preprečevanje razkrivanja občutljivih informacij

## Zagon aplikacije

### Korak 1: Nastavite GitHub žeton

Najprej morate nastaviti GitHub žeton kot okoljsko spremenljivko:

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

**Zakaj je to potrebno:**
- GitHub Models zahteva avtentikacijo za dostop do AI modelov
- Uporaba okoljskih spremenljivk ohranja občutljive žetone zunaj izvorne kode
- Dovoljenje `models:read` omogoča dostop do AI inferenc

### Korak 2: Zgradite in zaženite

Pomaknite se v mapo projekta:
```bash
cd 04-PracticalSamples/petstory
```

Zgradite aplikacijo:
```bash
mvn clean compile
```

Zaženite strežnik:
```bash
mvn spring-boot:run
```

Aplikacija se bo zagnala na `http://localhost:8080`.

### Korak 3: Preizkusite aplikacijo

1. **Odprite** `http://localhost:8080` v brskalniku
2. **Opišite** svojega hišnega ljubljenčka v besedilnem polju (npr. "Igriv zlati prinašalec, ki rad prinaša predmete")
3. **Kliknite** "Generiraj zgodbo" za pridobitev AI-generirane zgodbe
4. **Alternativno**, naložite sliko hišnega ljubljenčka za samodejno generiranje opisa
5. **Oglejte si** ustvarjalno zgodbo, ki temelji na opisu vašega hišnega ljubljenčka

## Kako vse deluje skupaj

Tukaj je celoten potek, ko generirate zgodbo o hišnem ljubljenčku:

1. **Uporabniški vnos**: Na spletnem obrazcu opišete svojega hišnega ljubljenčka
2. **Oddaja obrazca**: Brskalnik pošlje POST zahtevo na `/generate-story`
3. **Obdelava krmilnika**: `PetController` validira in očisti vnos
4. **Klic storitve AI**: `StoryService` pošlje zahtevo na GitHub Models API
5. **Generiranje zgodbe**: AI ustvari ustvarjalno zgodbo na podlagi opisa
6. **Obdelava odgovora**: Krmilnik prejme zgodbo in jo doda v model
7. **Upodabljanje predloge**: Thymeleaf upodobi `result.html` z zgodbo
8. **Prikaz**: Uporabnik vidi generirano zgodbo v brskalniku

**Potek upravljanja napak:**
Če storitev AI ne deluje:
1. Krmilnik ujame izjemo
2. Generira rezervno zgodbo z uporabo vnaprej napisanih predlog
3. Prikaže rezervno zgodbo z opombo o nedostopnosti AI
4. Uporabnik še vedno prejme zgodbo, kar zagotavlja dobro uporabniško izkušnjo

## Razumevanje AI integracije

### GitHub Models API
Aplikacija uporablja GitHub Models, ki omogoča brezplačen dostop do različnih AI modelov:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Oblikovanje pozivov
Storitev uporablja skrbno oblikovane pozive za doseganje dobrih rezultatov:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Obdelava odgovorov
Odgovor AI se izlušči in validira:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Naslednji koraki

Za več primerov si oglejte [Poglavje 04: Praktični primeri](../README.md)

**Omejitev odgovornosti**:  
Ta dokument je bil preveden z uporabo storitve AI za prevajanje [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas opozarjamo, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem maternem jeziku je treba obravnavati kot avtoritativni vir. Za ključne informacije priporočamo profesionalni človeški prevod. Ne prevzemamo odgovornosti za morebitna nesporazumevanja ali napačne razlage, ki bi nastale zaradi uporabe tega prevoda.