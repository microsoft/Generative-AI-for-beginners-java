<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:07:48+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "cs"
}
-->
# Generátor příběhů o mazlíčcích - tutoriál pro začátečníky

## Obsah

- [Předpoklady](../../../../04-PracticalSamples/petstory)
- [Porozumění struktuře projektu](../../../../04-PracticalSamples/petstory)
- [Vysvětlení hlavních komponent](../../../../04-PracticalSamples/petstory)
  - [1. Hlavní aplikace](../../../../04-PracticalSamples/petstory)
  - [2. Webový kontroler](../../../../04-PracticalSamples/petstory)
  - [3. Služba pro příběhy](../../../../04-PracticalSamples/petstory)
  - [4. Webové šablony](../../../../04-PracticalSamples/petstory)
  - [5. Konfigurace](../../../../04-PracticalSamples/petstory)
- [Spuštění aplikace](../../../../04-PracticalSamples/petstory)
- [Jak vše spolupracuje](../../../../04-PracticalSamples/petstory)
- [Porozumění integraci AI](../../../../04-PracticalSamples/petstory)
- [Další kroky](../../../../04-PracticalSamples/petstory)

## Předpoklady

Než začnete, ujistěte se, že máte:
- Nainstalovaný Java 21 nebo novější
- Maven pro správu závislostí
- GitHub účet s osobním přístupovým tokenem (PAT) s oprávněním `models:read`
- Základní znalosti Java, Spring Boot a webového vývoje

## Porozumění struktuře projektu

Projekt generátoru příběhů o mazlíčcích obsahuje několik důležitých souborů:

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

## Vysvětlení hlavních komponent

### 1. Hlavní aplikace

**Soubor:** `PetStoryApplication.java`

Toto je vstupní bod naší Spring Boot aplikace:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Co dělá:**
- Anotace `@SpringBootApplication` umožňuje automatickou konfiguraci a skenování komponent
- Spouští vestavěný webový server (Tomcat) na portu 8080
- Automaticky vytváří všechny potřebné Spring bean a služby

### 2. Webový kontroler

**Soubor:** `PetController.java`

Zpracovává všechny webové požadavky a interakce uživatelů:

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

**Klíčové vlastnosti:**

1. **Zpracování tras**: `@GetMapping("/")` zobrazí formulář pro nahrávání, `@PostMapping("/generate-story")` zpracovává odeslání
2. **Validace vstupu**: Kontroluje prázdné popisy a limity délky
3. **Bezpečnost**: Sanitizuje uživatelský vstup, aby zabránil XSS útokům
4. **Zpracování chyb**: Poskytuje záložní příběhy, když služba AI selže
5. **Vazba modelu**: Předává data do HTML šablon pomocí Spring `Model`

**Záložní systém:**
Kontroler obsahuje předem napsané šablony příběhů, které se použijí, když služba AI není dostupná:

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

### 3. Služba pro příběhy

**Soubor:** `StoryService.java`

Tato služba komunikuje s GitHub Models pro generování příběhů:

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

**Klíčové komponenty:**

1. **OpenAI klient**: Používá oficiální OpenAI Java SDK nakonfigurovaný pro GitHub Models
2. **Systémový prompt**: Nastavuje chování AI na psaní rodinných příběhů o mazlíčcích
3. **Uživatelský prompt**: Říká AI přesně, jaký příběh má napsat na základě popisu
4. **Parametry**: Řídí délku příběhu a úroveň kreativity
5. **Zpracování chyb**: Vyvolává výjimky, které kontroler zachytí a zpracuje

### 4. Webové šablony

**Soubor:** `index.html` (Formulář pro nahrávání)

Hlavní stránka, kde uživatelé popisují své mazlíčky:

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

**Soubor:** `result.html` (Zobrazení příběhu)

Zobrazuje vygenerovaný příběh:

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

**Vlastnosti šablon:**

1. **Integrace Thymeleaf**: Používá atributy `th:` pro dynamický obsah
2. **Responzivní design**: CSS stylování pro mobilní a desktopové zařízení
3. **Zpracování chyb**: Zobrazuje validační chyby uživatelům
4. **Zpracování na straně klienta**: JavaScript pro analýzu obrázků (pomocí Transformers.js)

### 5. Konfigurace

**Soubor:** `application.properties`

Konfigurační nastavení aplikace:

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

**Vysvětlení konfigurace:**

1. **Nahrávání souborů**: Umožňuje obrázky do velikosti 10MB
2. **Logování**: Řídí, jaké informace se logují během běhu aplikace
3. **GitHub Models**: Určuje, který AI model a endpoint se použije
4. **Bezpečnost**: Konfigurace zpracování chyb, aby se zabránilo odhalení citlivých informací

## Spuštění aplikace

### Krok 1: Nastavte svůj GitHub token

Nejprve musíte nastavit svůj GitHub token jako proměnnou prostředí:

**Windows (Příkazový řádek):**
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

**Proč je to potřeba:**
- GitHub Models vyžaduje autentizaci pro přístup k AI modelům
- Použití proměnných prostředí udržuje citlivé tokeny mimo zdrojový kód
- Oprávnění `models:read` poskytuje přístup k AI inferenci

### Krok 2: Sestavení a spuštění

Přejděte do adresáře projektu:
```bash
cd 04-PracticalSamples/petstory
```

Sestavte aplikaci:
```bash
mvn clean compile
```

Spusťte server:
```bash
mvn spring-boot:run
```

Aplikace se spustí na `http://localhost:8080`.

### Krok 3: Testování aplikace

1. **Otevřete** `http://localhost:8080` ve svém prohlížeči
2. **Popište** svého mazlíčka v textovém poli (např. "Hravý zlatý retrívr, který rád aportuje")
3. **Klikněte** na "Vygenerovat příběh" pro získání příběhu vytvořeného AI
4. **Alternativně**, nahrajte obrázek mazlíčka pro automatické generování popisu
5. **Zobrazte** kreativní příběh na základě popisu vašeho mazlíčka

## Jak vše spolupracuje

Zde je kompletní tok při generování příběhu o mazlíčkovi:

1. **Vstup uživatele**: Popíšete svého mazlíčka na webovém formuláři
2. **Odeslání formuláře**: Prohlížeč odešle POST požadavek na `/generate-story`
3. **Zpracování kontrolerem**: `PetController` validuje a sanitizuje vstup
4. **Volání služby AI**: `StoryService` odešle požadavek na GitHub Models API
5. **Generování příběhu**: AI vytvoří kreativní příběh na základě popisu
6. **Zpracování odpovědi**: Kontroler obdrží příběh a přidá ho do modelu
7. **Renderování šablony**: Thymeleaf vykreslí `result.html` s příběhem
8. **Zobrazení**: Uživatel vidí vygenerovaný příběh ve svém prohlížeči

**Tok zpracování chyb:**
Pokud služba AI selže:
1. Kontroler zachytí výjimku
2. Vygeneruje záložní příběh pomocí předem napsaných šablon
3. Zobrazí záložní příběh s poznámkou o nedostupnosti AI
4. Uživatel stále obdrží příběh, což zajišťuje dobrou uživatelskou zkušenost

## Porozumění integraci AI

### GitHub Models API
Aplikace používá GitHub Models, které poskytují bezplatný přístup k různým AI modelům:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
Služba používá pečlivě vytvořené prompty pro dosažení dobrých výsledků:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Zpracování odpovědí
Odpověď AI je extrahována a validována:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Další kroky

Pro více příkladů viz [Kapitola 04: Praktické ukázky](../README.md)

**Prohlášení:**  
Tento dokument byl přeložen pomocí služby pro automatizovaný překlad [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli se snažíme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Neodpovídáme za žádná nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.