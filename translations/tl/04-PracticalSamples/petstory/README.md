<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:59:46+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "tl"
}
-->
# Tutorial sa Pet Story Generator para sa mga Baguhan

## Nilalaman

- [Mga Kinakailangan](../../../../04-PracticalSamples/petstory)
- [Pag-unawa sa Estruktura ng Proyekto](../../../../04-PracticalSamples/petstory)
- [Paliwanag sa Mga Pangunahing Komponent](../../../../04-PracticalSamples/petstory)
  - [1. Pangunahing Aplikasyon](../../../../04-PracticalSamples/petstory)
  - [2. Web Controller](../../../../04-PracticalSamples/petstory)
  - [3. Story Service](../../../../04-PracticalSamples/petstory)
  - [4. Web Templates](../../../../04-PracticalSamples/petstory)
  - [5. Konpigurasyon](../../../../04-PracticalSamples/petstory)
- [Pagpapatakbo ng Aplikasyon](../../../../04-PracticalSamples/petstory)
- [Paano Nagtutulungan ang Lahat](../../../../04-PracticalSamples/petstory)
- [Pag-unawa sa AI Integration](../../../../04-PracticalSamples/petstory)
- [Mga Susunod na Hakbang](../../../../04-PracticalSamples/petstory)

## Mga Kinakailangan

Bago magsimula, siguraduhing mayroon ka ng mga sumusunod:
- Nakainstall na Java 21 o mas mataas
- Maven para sa pamamahala ng dependencies
- Isang GitHub account na may personal access token (PAT) na may `models:read` scope
- Pangunahing kaalaman sa Java, Spring Boot, at web development

## Pag-unawa sa Estruktura ng Proyekto

Ang proyekto ng pet story ay may ilang mahahalagang file:

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

## Paliwanag sa Mga Pangunahing Komponent

### 1. Pangunahing Aplikasyon

**File:** `PetStoryApplication.java`

Ito ang entry point para sa ating Spring Boot application:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ano ang ginagawa nito:**
- Ang `@SpringBootApplication` annotation ay nagbibigay-daan sa auto-configuration at component scanning
- Sinisimulan ang embedded web server (Tomcat) sa port 8080
- Awtomatikong nililikha ang lahat ng kinakailangang Spring beans at services

### 2. Web Controller

**File:** `PetController.java`

Ito ang humahawak sa lahat ng web requests at interaksyon ng user:

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

**Mga Pangunahing Tampok:**

1. **Paghawak ng Ruta**: Ang `@GetMapping("/")` ay nagpapakita ng upload form, habang ang `@PostMapping("/generate-story")` ay nagpoproseso ng submissions
2. **Pag-validate ng Input**: Tinitiyak na walang laman ang mga description at sinusuri ang haba nito
3. **Seguridad**: Nililinis ang input ng user upang maiwasan ang XSS attacks
4. **Paghawak ng Error**: Nagbibigay ng fallback stories kapag hindi gumana ang AI service
5. **Model Binding**: Ipinapasa ang data sa HTML templates gamit ang Spring's `Model`

**Fallback System:**
Ang controller ay may kasamang pre-written story templates na ginagamit kapag hindi available ang AI service:

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

### 3. Story Service

**File:** `StoryService.java`

Ang service na ito ang nakikipag-ugnayan sa GitHub Models para makabuo ng mga kwento:

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

**Mga Pangunahing Komponent:**

1. **OpenAI Client**: Ginagamit ang opisyal na OpenAI Java SDK na naka-configure para sa GitHub Models
2. **System Prompt**: Itinatakda ang ugali ng AI upang magsulat ng family-friendly na pet stories
3. **User Prompt**: Sinasabi sa AI kung anong kwento ang isusulat base sa description
4. **Mga Parameter**: Kinokontrol ang haba ng kwento at antas ng creativity
5. **Paghawak ng Error**: Nagbubukas ng exceptions na hinuhuli at hinahawakan ng controller

### 4. Web Templates

**File:** `index.html` (Upload Form)

Ang pangunahing pahina kung saan inilalarawan ng mga user ang kanilang mga alagang hayop:

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

**File:** `result.html` (Story Display)

Ipinapakita ang nabuo na kwento:

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

**Mga Tampok ng Template:**

1. **Thymeleaf Integration**: Gumagamit ng `th:` attributes para sa dynamic na content
2. **Responsive Design**: CSS styling para sa mobile at desktop
3. **Paghawak ng Error**: Ipinapakita ang validation errors sa mga user
4. **Client-side Processing**: JavaScript para sa image analysis (gamit ang Transformers.js)

### 5. Konpigurasyon

**File:** `application.properties`

Mga setting ng konpigurasyon para sa aplikasyon:

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

**Paliwanag sa Konpigurasyon:**

1. **File Upload**: Pinapayagan ang mga imahe hanggang 10MB
2. **Logging**: Kinokontrol kung anong impormasyon ang ilalagay sa logs habang tumatakbo
3. **GitHub Models**: Tinutukoy kung aling AI model at endpoint ang gagamitin
4. **Seguridad**: Konpigurasyon sa paghawak ng error upang maiwasan ang paglalantad ng sensitibong impormasyon

## Pagpapatakbo ng Aplikasyon

### Hakbang 1: Itakda ang Iyong GitHub Token

Una, kailangan mong itakda ang iyong GitHub token bilang environment variable:

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

**Bakit ito kailangan:**
- Ang GitHub Models ay nangangailangan ng authentication para ma-access ang AI models
- Ang paggamit ng environment variables ay nagtatago ng sensitibong tokens mula sa source code
- Ang `models:read` scope ay nagbibigay ng access sa AI inference

### Hakbang 2: I-build at Patakbuhin

Pumunta sa project directory:
```bash
cd 04-PracticalSamples/petstory
```

I-build ang application:
```bash
mvn clean compile
```

Simulan ang server:
```bash
mvn spring-boot:run
```

Ang application ay magsisimula sa `http://localhost:8080`.

### Hakbang 3: Subukan ang Aplikasyon

1. **Buksan** ang `http://localhost:8080` sa iyong browser
2. **Ilarawan** ang iyong alagang hayop sa text area (hal., "Isang masayahing golden retriever na mahilig mag-fetch")
3. **I-click** ang "Generate Story" para makakuha ng AI-generated na kwento
4. **Bilang alternatibo**, mag-upload ng larawan ng alagang hayop para awtomatikong makabuo ng description
5. **Tingnan** ang malikhaing kwento base sa description ng iyong alagang hayop

## Paano Nagtutulungan ang Lahat

Narito ang kumpletong daloy kapag nagbuo ka ng pet story:

1. **Input ng User**: Inilalarawan mo ang iyong alagang hayop sa web form
2. **Pag-submit ng Form**: Ang browser ay nagpapadala ng POST request sa `/generate-story`
3. **Pagproseso ng Controller**: Ang `PetController` ay nagva-validate at nililinis ang input
4. **Tawag sa AI Service**: Ang `StoryService` ay nagpapadala ng request sa GitHub Models API
5. **Pagbuo ng Kwento**: Ang AI ay bumubuo ng malikhaing kwento base sa description
6. **Paghawak ng Response**: Tinatanggap ng controller ang kwento at idinadagdag ito sa model
7. **Pag-render ng Template**: Ang Thymeleaf ay nagre-render ng `result.html` kasama ang kwento
8. **Pag-display**: Nakikita ng user ang nabuo na kwento sa kanilang browser

**Daloy ng Paghawak ng Error:**
Kapag hindi gumana ang AI service:
1. Hinuhuli ng controller ang exception
2. Bumubuo ng fallback story gamit ang pre-written templates
3. Ipinapakita ang fallback story na may paalala tungkol sa hindi availability ng AI
4. Nakakakuha pa rin ng kwento ang user, na nagbibigay ng magandang karanasan

## Pag-unawa sa AI Integration

### GitHub Models API
Ginagamit ng application ang GitHub Models, na nagbibigay ng libreng access sa iba't ibang AI models:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
Gumagamit ang service ng maingat na dinisenyong prompts para makakuha ng magagandang resulta:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Pagproseso ng Response
Ang AI response ay kinukuha at sinusuri:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Mga Susunod na Hakbang

Para sa higit pang mga halimbawa, tingnan ang [Chapter 04: Practical samples](../README.md)

**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't sinisikap naming maging tumpak, pakitandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa kanyang katutubong wika ang dapat ituring na opisyal na pinagmulan. Para sa mahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na dulot ng paggamit ng pagsasaling ito.