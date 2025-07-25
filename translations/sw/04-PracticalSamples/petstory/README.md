<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:02:11+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "sw"
}
-->
# Mafunzo ya Jenereta ya Hadithi za Wanyama kwa Kompyuta Mpya

## Jedwali la Maudhui

- [Mahitaji ya Awali](../../../../04-PracticalSamples/petstory)
- [Kuelewa Muundo wa Mradi](../../../../04-PracticalSamples/petstory)
- [Vipengele Muhimu Vilivyoelezwa](../../../../04-PracticalSamples/petstory)
  - [1. Programu Kuu](../../../../04-PracticalSamples/petstory)
  - [2. Kidhibiti cha Wavuti](../../../../04-PracticalSamples/petstory)
  - [3. Huduma ya Hadithi](../../../../04-PracticalSamples/petstory)
  - [4. Violezo vya Wavuti](../../../../04-PracticalSamples/petstory)
  - [5. Usanidi](../../../../04-PracticalSamples/petstory)
- [Kuendesha Programu](../../../../04-PracticalSamples/petstory)
- [Jinsi Vyote Vinavyofanya Kazi Pamoja](../../../../04-PracticalSamples/petstory)
- [Kuelewa Muunganisho wa AI](../../../../04-PracticalSamples/petstory)
- [Hatua Zifuatazo](../../../../04-PracticalSamples/petstory)

## Mahitaji ya Awali

Kabla ya kuanza, hakikisha una:
- Java 21 au toleo la juu zaidi limewekwa
- Maven kwa usimamizi wa utegemezi
- Akaunti ya GitHub yenye tokeni ya ufikiaji wa kibinafsi (PAT) yenye wigo wa `models:read`
- Uelewa wa msingi wa Java, Spring Boot, na maendeleo ya wavuti

## Kuelewa Muundo wa Mradi

Mradi wa hadithi za wanyama una faili kadhaa muhimu:

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

## Vipengele Muhimu Vilivyoelezwa

### 1. Programu Kuu

**Faili:** `PetStoryApplication.java`

Hii ni sehemu ya kuanzia kwa programu yetu ya Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Kazi ya hii:**
- Anotesheni ya `@SpringBootApplication` inawezesha usanidi wa kiotomatiki na skani ya vipengele
- Inaendesha seva ya wavuti iliyojengwa ndani (Tomcat) kwenye bandari 8080
- Inaunda maharagwe na huduma zote muhimu za Spring kiotomatiki

### 2. Kidhibiti cha Wavuti

**Faili:** `PetController.java`

Hii inashughulikia maombi yote ya wavuti na mwingiliano wa watumiaji:

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

**Vipengele Muhimu:**

1. **Ushughulikiaji wa Njia**: `@GetMapping("/")` inaonyesha fomu ya kupakia, `@PostMapping("/generate-story")` inashughulikia mawasilisho
2. **Uthibitishaji wa Ingizo**: Hukagua maelezo tupu na mipaka ya urefu
3. **Usalama**: Husafisha ingizo la mtumiaji ili kuzuia mashambulizi ya XSS
4. **Ushughulikiaji wa Makosa**: Hutoa hadithi za akiba wakati huduma ya AI inashindwa
5. **Ufungaji wa Mfano**: Inapitisha data kwa violezo vya HTML kwa kutumia `Model` ya Spring

**Mfumo wa Akiba:**
Kidhibiti kinajumuisha violezo vya hadithi vilivyoandikwa awali ambavyo hutumika wakati huduma ya AI haipatikani:

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

### 3. Huduma ya Hadithi

**Faili:** `StoryService.java`

Huduma hii inaunganishwa na GitHub Models ili kuzalisha hadithi:

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

**Vipengele Muhimu:**

1. **Mteja wa OpenAI**: Hutumia SDK rasmi ya Java ya OpenAI iliyosanidiwa kwa GitHub Models
2. **Mwongozo wa Mfumo**: Huanzisha tabia ya AI kuandika hadithi za wanyama zinazofaa kwa familia
3. **Mwongozo wa Mtumiaji**: Huambia AI hasa ni hadithi gani ya kuandika kulingana na maelezo
4. **Vigezo**: Hudhibiti urefu wa hadithi na kiwango cha ubunifu
5. **Ushughulikiaji wa Makosa**: Hutupa makosa ambayo kidhibiti kinashika na kushughulikia

### 4. Violezo vya Wavuti

**Faili:** `index.html` (Fomu ya Kupakia)

Ukurasa kuu ambapo watumiaji huelezea wanyama wao:

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

**Faili:** `result.html` (Onyesho la Hadithi)

Inaonyesha hadithi iliyozalishwa:

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

**Vipengele vya Kiolezo:**

1. **Muunganisho wa Thymeleaf**: Hutumia sifa za `th:` kwa maudhui ya nguvu
2. **Muundo wa Kujibika**: Usanifu wa CSS kwa simu na kompyuta
3. **Ushughulikiaji wa Makosa**: Huonyesha makosa ya uthibitishaji kwa watumiaji
4. **Usindikaji wa Upande wa Mteja**: JavaScript kwa uchambuzi wa picha (kutumia Transformers.js)

### 5. Usanidi

**Faili:** `application.properties`

Mipangilio ya usanidi kwa programu:

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

**Usanidi Umeelezwa:**

1. **Upakiaji wa Faili**: Inaruhusu picha hadi 10MB
2. **Kumbukumbu**: Hudhibiti ni taarifa gani zinakumbukwa wakati wa utekelezaji
3. **GitHub Models**: Hufafanua ni modeli gani ya AI na mwisho wa API ya kutumia
4. **Usalama**: Usanidi wa ushughulikiaji wa makosa ili kuepuka kufichua taarifa nyeti

## Kuendesha Programu

### Hatua ya 1: Weka Tokeni Yako ya GitHub

Kwanza, unahitaji kuweka tokeni yako ya GitHub kama kigezo cha mazingira:

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

**Kwa nini hii inahitajika:**
- GitHub Models inahitaji uthibitishaji ili kufikia modeli za AI
- Kutumia vigezo vya mazingira kunahifadhi tokeni nyeti nje ya msimbo wa chanzo
- Wigo wa `models:read` unatoa ufikiaji wa inferensi ya AI

### Hatua ya 2: Jenga na Endesha

Nenda kwenye saraka ya mradi:
```bash
cd 04-PracticalSamples/petstory
```

Jenga programu:
```bash
mvn clean compile
```

Anzisha seva:
```bash
mvn spring-boot:run
```

Programu itaanza kwenye `http://localhost:8080`.

### Hatua ya 3: Jaribu Programu

1. **Fungua** `http://localhost:8080` kwenye kivinjari chako
2. **Elezea** mnyama wako kwenye eneo la maandishi (mfano, "Retriever wa dhahabu mchangamfu anayependa kuleta vitu")
3. **Bonyeza** "Generate Story" ili kupata hadithi iliyozalishwa na AI
4. **Vinginevyo**, pakia picha ya mnyama ili kuzalisha maelezo kiotomatiki
5. **Tazama** hadithi ya ubunifu kulingana na maelezo ya mnyama wako

## Jinsi Vyote Vinavyofanya Kazi Pamoja

Hapa kuna mtiririko kamili wakati unazalisha hadithi ya mnyama:

1. **Ingizo la Mtumiaji**: Unaelezea mnyama wako kwenye fomu ya wavuti
2. **Mwasilisho wa Fomu**: Kivinjari kinatuma ombi la POST kwa `/generate-story`
3. **Usindikaji wa Kidhibiti**: `PetController` inathibitisha na kusafisha ingizo
4. **Mwito wa Huduma ya AI**: `StoryService` inatuma ombi kwa API ya GitHub Models
5. **Uzalishaji wa Hadithi**: AI inazalisha hadithi ya ubunifu kulingana na maelezo
6. **Ushughulikiaji wa Majibu**: Kidhibiti kinapokea hadithi na kuiongeza kwenye mfano
7. **Utoaji wa Kiolezo**: Thymeleaf inatoa `result.html` na hadithi
8. **Onyesho**: Mtumiaji anaona hadithi iliyozalishwa kwenye kivinjari chao

**Mtiririko wa Ushughulikiaji wa Makosa:**
Ikiwa huduma ya AI inashindwa:
1. Kidhibiti kinashika kosa
2. Kinazalisha hadithi ya akiba kwa kutumia violezo vilivyoandikwa awali
3. Kinaonyesha hadithi ya akiba na maelezo kuhusu kutopatikana kwa AI
4. Mtumiaji bado anapata hadithi, kuhakikisha uzoefu mzuri wa mtumiaji

## Kuelewa Muunganisho wa AI

### API ya GitHub Models
Programu hutumia GitHub Models, ambayo hutoa ufikiaji wa bure kwa modeli mbalimbali za AI:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Uhandisi wa Mwongozo
Huduma hutumia miongozo iliyoundwa kwa uangalifu ili kupata matokeo mazuri:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Usindikaji wa Majibu
Majibu ya AI yanatolewa na kuthibitishwa:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Hatua Zifuatazo

Kwa mifano zaidi, angalia [Sura ya 04: Sampuli za vitendo](../README.md)

**Kanusho**:  
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafsiri za kiotomatiki zinaweza kuwa na makosa au kutokuwa sahihi. Hati ya asili katika lugha yake ya awali inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatutawajibika kwa kutoelewana au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.