<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:10:13+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "sk"
}
-->
# Návod na generátor príbehov o domácich miláčikoch pre začiatočníkov

## Obsah

- [Predpoklady](../../../../04-PracticalSamples/petstory)
- [Pochopenie štruktúry projektu](../../../../04-PracticalSamples/petstory)
- [Vysvetlenie hlavných komponentov](../../../../04-PracticalSamples/petstory)
  - [1. Hlavná aplikácia](../../../../04-PracticalSamples/petstory)
  - [2. Webový kontrolér](../../../../04-PracticalSamples/petstory)
  - [3. Služba pre príbehy](../../../../04-PracticalSamples/petstory)
  - [4. Webové šablóny](../../../../04-PracticalSamples/petstory)
  - [5. Konfigurácia](../../../../04-PracticalSamples/petstory)
- [Spustenie aplikácie](../../../../04-PracticalSamples/petstory)
- [Ako to všetko spolu funguje](../../../../04-PracticalSamples/petstory)
- [Pochopenie integrácie AI](../../../../04-PracticalSamples/petstory)
- [Ďalšie kroky](../../../../04-PracticalSamples/petstory)

## Predpoklady

Pred začiatkom sa uistite, že máte:
- Nainštalovanú Javu 21 alebo novšiu
- Maven na správu závislostí
- GitHub účet s osobným prístupovým tokenom (PAT) s oprávnením `models:read`
- Základné znalosti Javy, Spring Boot a webového vývoja

## Pochopenie štruktúry projektu

Projekt na generovanie príbehov o domácich miláčikoch obsahuje niekoľko dôležitých súborov:

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

## Vysvetlenie hlavných komponentov

### 1. Hlavná aplikácia

**Súbor:** `PetStoryApplication.java`

Toto je vstupný bod pre našu Spring Boot aplikáciu:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Čo to robí:**
- Anotácia `@SpringBootApplication` umožňuje automatickú konfiguráciu a skenovanie komponentov
- Spúšťa zabudovaný webový server (Tomcat) na porte 8080
- Automaticky vytvára všetky potrebné Spring beany a služby

### 2. Webový kontrolér

**Súbor:** `PetController.java`

Tento komponent spracováva všetky webové požiadavky a interakcie používateľov:

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

**Kľúčové vlastnosti:**

1. **Spracovanie trás:** `@GetMapping("/")` zobrazuje formulár na nahrávanie, `@PostMapping("/generate-story")` spracováva odoslania
2. **Validácia vstupu:** Kontroluje prázdne popisy a obmedzenia dĺžky
3. **Bezpečnosť:** Sanitizuje vstupy používateľov na prevenciu XSS útokov
4. **Spracovanie chýb:** Poskytuje záložné príbehy, keď AI služba zlyhá
5. **Väzba modelu:** Prenáša dáta do HTML šablón pomocou Spring `Model`

**Záložný systém:**
Kontrolér obsahuje predpripravené šablóny príbehov, ktoré sa použijú, keď AI služba nie je dostupná:

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

### 3. Služba pre príbehy

**Súbor:** `StoryService.java`

Táto služba komunikuje s GitHub Models na generovanie príbehov:

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

**Kľúčové komponenty:**

1. **OpenAI klient:** Používa oficiálne OpenAI Java SDK nakonfigurované pre GitHub Models
2. **Systémový prompt:** Nastavuje správanie AI na písanie rodinných príbehov o domácich miláčikoch
3. **Používateľský prompt:** Určuje AI, aký príbeh má napísať na základe popisu
4. **Parametre:** Riadi dĺžku príbehu a úroveň kreativity
5. **Spracovanie chýb:** Vyhadzuje výnimky, ktoré kontrolér zachytáva a spracováva

### 4. Webové šablóny

**Súbor:** `index.html` (Formulár na nahrávanie)

Hlavná stránka, kde používatelia popisujú svojich domácich miláčikov:

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

**Súbor:** `result.html` (Zobrazenie príbehu)

Zobrazuje vygenerovaný príbeh:

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

**Vlastnosti šablón:**

1. **Integrácia Thymeleaf:** Používa `th:` atribúty na dynamický obsah
2. **Responzívny dizajn:** CSS štýly pre mobilné a desktopové zariadenia
3. **Spracovanie chýb:** Zobrazuje používateľom validačné chyby
4. **Klientske spracovanie:** JavaScript na analýzu obrázkov (pomocou Transformers.js)

### 5. Konfigurácia

**Súbor:** `application.properties`

Nastavenia konfigurácie pre aplikáciu:

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

**Vysvetlenie konfigurácie:**

1. **Nahrávanie súborov:** Povolené obrázky do veľkosti 10 MB
2. **Logovanie:** Riadi, aké informácie sa zaznamenávajú počas vykonávania
3. **GitHub Models:** Špecifikuje, ktorý AI model a endpoint sa použije
4. **Bezpečnosť:** Konfigurácia spracovania chýb na zabránenie odhalenia citlivých informácií

## Spustenie aplikácie

### Krok 1: Nastavte svoj GitHub token

Najprv nastavte svoj GitHub token ako premennú prostredia:

**Windows (Príkazový riadok):**
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

**Prečo je to potrebné:**
- GitHub Models vyžaduje autentifikáciu na prístup k AI modelom
- Použitie premenných prostredia udržiava citlivé tokeny mimo zdrojového kódu
- Oprávnenie `models:read` poskytuje prístup k AI inferencii

### Krok 2: Zostavte a spustite

Prejdite do adresára projektu:
```bash
cd 04-PracticalSamples/petstory
```

Zostavte aplikáciu:
```bash
mvn clean compile
```

Spustite server:
```bash
mvn spring-boot:run
```

Aplikácia sa spustí na `http://localhost:8080`.

### Krok 3: Otestujte aplikáciu

1. **Otvorte** `http://localhost:8080` vo svojom prehliadači
2. **Popíšte** svojho domáceho miláčika v textovom poli (napr. "Hravý zlatý retríver, ktorý rád aportuje")
3. **Kliknite** na "Generovať príbeh" a získajte AI-generovaný príbeh
4. **Alternatívne**, nahrajte obrázok domáceho miláčika na automatické generovanie popisu
5. **Zobrazte** kreatívny príbeh na základe popisu vášho domáceho miláčika

## Ako to všetko spolu funguje

Tu je kompletný tok, keď generujete príbeh o domácom miláčikovi:

1. **Vstup používateľa:** Popíšete svojho domáceho miláčika vo webovom formulári
2. **Odoslanie formulára:** Prehliadač pošle POST požiadavku na `/generate-story`
3. **Spracovanie kontrolérom:** `PetController` validuje a sanitizuje vstup
4. **Volanie AI služby:** `StoryService` pošle požiadavku na GitHub Models API
5. **Generovanie príbehu:** AI vygeneruje kreatívny príbeh na základe popisu
6. **Spracovanie odpovede:** Kontrolér prijme príbeh a pridá ho do modelu
7. **Renderovanie šablóny:** Thymeleaf vykreslí `result.html` s príbehom
8. **Zobrazenie:** Používateľ vidí vygenerovaný príbeh vo svojom prehliadači

**Tok spracovania chýb:**
Ak AI služba zlyhá:
1. Kontrolér zachytí výnimku
2. Vygeneruje záložný príbeh pomocou predpripravených šablón
3. Zobrazí záložný príbeh s poznámkou o nedostupnosti AI
4. Používateľ stále dostane príbeh, čím sa zabezpečí dobrá používateľská skúsenosť

## Pochopenie integrácie AI

### GitHub Models API
Aplikácia používa GitHub Models, ktoré poskytujú bezplatný prístup k rôznym AI modelom:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Inžinierstvo promptov
Služba používa starostlivo navrhnuté prompty na dosiahnutie dobrých výsledkov:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Spracovanie odpovedí
Odpoveď AI je extrahovaná a validovaná:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Ďalšie kroky

Pre viac príkladov si pozrite [Kapitolu 04: Praktické ukážky](../README.md)

**Upozornenie**:  
Tento dokument bol preložený pomocou služby AI prekladu [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, prosím, berte na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.