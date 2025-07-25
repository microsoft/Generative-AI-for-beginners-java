<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:18:23+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "pa"
}
-->
# ਪਾਲਤੂ ਕਹਾਣੀ ਜਨਰੇਟਰ ਟਿਊਟੋਰਿਯਲ ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ

## ਸੂਚੀ

- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../../04-PracticalSamples/petstory)
- [ਪਰਿਯੋਜਨਾ ਦੀ ਬਣਾਵਟ ਨੂੰ ਸਮਝਣਾ](../../../../04-PracticalSamples/petstory)
- [ਮੁੱਖ ਹਿੱਸਿਆਂ ਦੀ ਵਿਆਖਿਆ](../../../../04-PracticalSamples/petstory)
  - [1. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ](../../../../04-PracticalSamples/petstory)
  - [2. ਵੈੱਬ ਕੰਟਰੋਲਰ](../../../../04-PracticalSamples/petstory)
  - [3. ਕਹਾਣੀ ਸੇਵਾ](../../../../04-PracticalSamples/petstory)
  - [4. ਵੈੱਬ ਟੈਂਪਲੇਟ](../../../../04-PracticalSamples/petstory)
  - [5. ਸੰਰਚਨਾ](../../../../04-PracticalSamples/petstory)
- [ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣਾ](../../../../04-PracticalSamples/petstory)
- [ਇਹ ਸਾਰਾ ਕਿਵੇਂ ਕੰਮ ਕਰਦਾ ਹੈ](../../../../04-PracticalSamples/petstory)
- [AI ਇੰਟੀਗ੍ਰੇਸ਼ਨ ਨੂੰ ਸਮਝਣਾ](../../../../04-PracticalSamples/petstory)
- [ਅਗਲੇ ਕਦਮ](../../../../04-PracticalSamples/petstory)

## ਪੂਰਵ ਸ਼ਰਤਾਂ

ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਇਹ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ ਇਹ ਹਨ:
- ਜਾਵਾ 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ ਵਰਜਨ ਇੰਸਟਾਲ ਹੈ
- Maven ਡਿਪੈਂਡੈਂਸੀ ਮੈਨੇਜਮੈਂਟ ਲਈ
- ਇੱਕ GitHub ਖਾਤਾ ਜਿਸ ਵਿੱਚ `models:read` ਸਕੋਪ ਵਾਲਾ ਪੈਰਸਨਲ ਐਕਸੈਸ ਟੋਕਨ (PAT) ਹੈ
- ਜਾਵਾ, ਸਪ੍ਰਿੰਗ ਬੂਟ, ਅਤੇ ਵੈੱਬ ਡਿਵੈਲਪਮੈਂਟ ਦੀ ਬੁਨਿਆਦੀ ਸਮਝ

## ਪਰਿਯੋਜਨਾ ਦੀ ਬਣਾਵਟ ਨੂੰ ਸਮਝਣਾ

ਪਾਲਤੂ ਕਹਾਣੀ ਪ੍ਰੋਜੈਕਟ ਵਿੱਚ ਕੁਝ ਮਹੱਤਵਪੂਰਨ ਫਾਈਲਾਂ ਹਨ:

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

## ਮੁੱਖ ਹਿੱਸਿਆਂ ਦੀ ਵਿਆਖਿਆ

### 1. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ

**ਫਾਈਲ:** `PetStoryApplication.java`

ਇਹ ਸਾਡੀ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨ ਦਾ ਐਂਟਰੀ ਪੌਇੰਟ ਹੈ:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- `@SpringBootApplication` ਐਨੋਟੇਸ਼ਨ ਆਟੋ-ਕੰਫਿਗਰੇਸ਼ਨ ਅਤੇ ਕੰਪੋਨੈਂਟ ਸਕੈਨਿੰਗ ਨੂੰ ਯਕੀਨੀ ਬਣਾਉਂਦਾ ਹੈ
- ਪੋਰਟ 8080 'ਤੇ ਇੱਕ ਐਂਬੈਡਡ ਵੈੱਬ ਸਰਵਰ (Tomcat) ਸ਼ੁਰੂ ਕਰਦਾ ਹੈ
- ਸਾਰੇ ਲੋੜੀਂਦੇ ਸਪ੍ਰਿੰਗ ਬੀਨ ਅਤੇ ਸੇਵਾਵਾਂ ਆਟੋਮੈਟਿਕ ਤੌਰ 'ਤੇ ਬਣਾਉਂਦਾ ਹੈ

### 2. ਵੈੱਬ ਕੰਟਰੋਲਰ

**ਫਾਈਲ:** `PetController.java`

ਇਹ ਸਾਰੇ ਵੈੱਬ ਰਿਕਵੈਸਟਾਂ ਅਤੇ ਯੂਜ਼ਰ ਇੰਟਰੈਕਸ਼ਨ ਨੂੰ ਹੈਂਡਲ ਕਰਦਾ ਹੈ:

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

**ਮੁੱਖ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ:**

1. **ਰੂਟ ਹੈਂਡਲਿੰਗ**: `@GetMapping("/")` ਅਪਲੋਡ ਫਾਰਮ ਦਿਖਾਉਂਦਾ ਹੈ, `@PostMapping("/generate-story")` ਸਬਮਿਸ਼ਨ ਪ੍ਰੋਸੈਸ ਕਰਦਾ ਹੈ
2. **ਇਨਪੁਟ ਵੈਲੀਡੇਸ਼ਨ**: ਖਾਲੀ ਵੇਰਵੇ ਅਤੇ ਲੰਬਾਈ ਦੀ ਸੀਮਾ ਦੀ ਜਾਂਚ ਕਰਦਾ ਹੈ
3. **ਸੁਰੱਖਿਆ**: ਯੂਜ਼ਰ ਇਨਪੁਟ ਨੂੰ ਸੈਨੀਟਾਈਜ਼ ਕਰਦਾ ਹੈ ਤਾਂ ਜੋ XSS ਹਮਲਿਆਂ ਤੋਂ ਬਚਿਆ ਜਾ ਸਕੇ
4. **ਗਲਤੀ ਸੰਭਾਲ**: AI ਸੇਵਾ ਫੇਲ੍ਹ ਹੋਣ 'ਤੇ ਬੈਕਅੱਪ ਕਹਾਣੀਆਂ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ
5. **ਮਾਡਲ ਬਾਈਂਡਿੰਗ**: Spring ਦੇ `Model` ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਡਾਟਾ HTML ਟੈਂਪਲੇਟਾਂ ਨੂੰ ਪਾਸ ਕਰਦਾ ਹੈ

**ਬੈਕਅੱਪ ਸਿਸਟਮ:**
ਕੰਟਰੋਲਰ ਵਿੱਚ ਪਹਿਲਾਂ ਤੋਂ ਲਿਖੀਆਂ ਕਹਾਣੀ ਟੈਂਪਲੇਟਾਂ ਸ਼ਾਮਲ ਹਨ, ਜਿਨ੍ਹਾਂ ਨੂੰ AI ਸੇਵਾ ਉਪਲਬਧ ਨਾ ਹੋਣ 'ਤੇ ਵਰਤਿਆ ਜਾਂਦਾ ਹੈ:

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

### 3. ਕਹਾਣੀ ਸੇਵਾ

**ਫਾਈਲ:** `StoryService.java`

ਇਹ ਸੇਵਾ GitHub Models ਨਾਲ ਸੰਚਾਰ ਕਰਕੇ ਕਹਾਣੀਆਂ ਬਣਾਉਂਦੀ ਹੈ:

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

**ਮੁੱਖ ਹਿੱਸੇ:**

1. **OpenAI ਕਲਾਇੰਟ**: GitHub Models ਲਈ ਕਨਫਿਗਰ ਕੀਤੇ ਗਏ OpenAI ਜਾਵਾ SDK ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ
2. **ਸਿਸਟਮ ਪ੍ਰੌਮਪਟ**: AI ਦੇ ਵਿਹਾਰ ਨੂੰ ਪਰਿਵਾਰ-ਅਨੁਕੂਲ ਪਾਲਤੂ ਕਹਾਣੀਆਂ ਲਿਖਣ ਲਈ ਸੈਟ ਕਰਦਾ ਹੈ
3. **ਯੂਜ਼ਰ ਪ੍ਰੌਮਪਟ**: AI ਨੂੰ ਸਪਸ਼ਟ ਦਿਸ਼ਾ ਦਿੰਦਾ ਹੈ ਕਿ ਕਿਹੜੀ ਕਹਾਣੀ ਲਿਖਣੀ ਹੈ
4. **ਪੈਰਾਮੀਟਰ**: ਕਹਾਣੀ ਦੀ ਲੰਬਾਈ ਅਤੇ ਕ੍ਰੀਏਟਿਵਿਟੀ ਲੈਵਲ ਨੂੰ ਨਿਯੰਤਰਿਤ ਕਰਦਾ ਹੈ
5. **ਗਲਤੀ ਸੰਭਾਲ**: ਐਕਸਪਸ਼ਨ ਸੁੱਟਦਾ ਹੈ, ਜਿਨ੍ਹਾਂ ਨੂੰ ਕੰਟਰੋਲਰ ਕੈਚ ਕਰਦਾ ਹੈ

### 4. ਵੈੱਬ ਟੈਂਪਲੇਟ

**ਫਾਈਲ:** `index.html` (ਅਪਲੋਡ ਫਾਰਮ)

ਮੁੱਖ ਪੰਨਾ ਜਿੱਥੇ ਯੂਜ਼ਰ ਆਪਣੇ ਪਾਲਤੂ ਜਾਨਵਰਾਂ ਦਾ ਵੇਰਵਾ ਦਿੰਦੇ ਹਨ:

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

**ਫਾਈਲ:** `result.html` (ਕਹਾਣੀ ਪ੍ਰਦਰਸ਼ਨ)

ਜਨਰੇਟ ਕੀਤੀ ਕਹਾਣੀ ਦਿਖਾਉਂਦਾ ਹੈ:

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

**ਟੈਂਪਲੇਟ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ:**

1. **Thymeleaf ਇੰਟੀਗ੍ਰੇਸ਼ਨ**: ਗਤੀਸ਼ੀਲ ਸਮੱਗਰੀ ਲਈ `th:` ਐਟ੍ਰਿਬਿਊਟ ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ
2. **ਰਿਸਪਾਂਸਿਵ ਡਿਜ਼ਾਈਨ**: ਮੋਬਾਈਲ ਅਤੇ ਡੈਸਕਟਾਪ ਲਈ CSS ਸਟਾਈਲਿੰਗ
3. **ਗਲਤੀ ਸੰਭਾਲ**: ਯੂਜ਼ਰ ਨੂੰ ਵੈਲੀਡੇਸ਼ਨ ਗਲਤੀਆਂ ਦਿਖਾਉਂਦਾ ਹੈ
4. **ਕਲਾਇੰਟ-ਸਾਈਡ ਪ੍ਰੋਸੈਸਿੰਗ**: Transformers.js ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਚਿੱਤਰ ਵਿਸ਼ਲੇਸ਼ਣ ਲਈ ਜਾਵਾਸਕ੍ਰਿਪਟ

### 5. ਸੰਰਚਨਾ

**ਫਾਈਲ:** `application.properties`

ਐਪਲੀਕੇਸ਼ਨ ਲਈ ਸੰਰਚਨਾ ਸੈਟਿੰਗਾਂ:

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

**ਸੰਰਚਨਾ ਦੀ ਵਿਆਖਿਆ:**

1. **ਫਾਈਲ ਅਪਲੋਡ**: 10MB ਤੱਕ ਚਿੱਤਰਾਂ ਦੀ ਆਗਿਆ ਦਿੰਦਾ ਹੈ
2. **ਲੌਗਿੰਗ**: ਚਲਾਉਣ ਦੌਰਾਨ ਕੀ ਜਾਣਕਾਰੀ ਲੌਗ ਕੀਤੀ ਜਾਂਦੀ ਹੈ, ਇਸ ਨੂੰ ਨਿਯੰਤਰਿਤ ਕਰਦਾ ਹੈ
3. **GitHub Models**: ਕਿਹੜੇ AI ਮਾਡਲ ਅਤੇ ਐਂਡਪੌਇੰਟ ਦੀ ਵਰਤੋਂ ਕੀਤੀ ਜਾ ਰਹੀ ਹੈ, ਇਹ ਦਰਸਾਉਂਦਾ ਹੈ
4. **ਸੁਰੱਖਿਆ**: ਸੰਵੇਦਨਸ਼ੀਲ ਜਾਣਕਾਰੀ ਨੂੰ ਬਚਾਉਣ ਲਈ ਗਲਤੀ ਸੰਭਾਲ ਸੰਰਚਨਾ

## ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣਾ

### ਕਦਮ 1: ਆਪਣਾ GitHub ਟੋਕਨ ਸੈਟ ਕਰੋ

ਸਭ ਤੋਂ ਪਹਿਲਾਂ, ਆਪਣਾ GitHub ਟੋਕਨ ਇੱਕ ਵਾਤਾਵਰਣ ਵੈਰੀਏਬਲ ਵਜੋਂ ਸੈਟ ਕਰੋ:

**Windows (ਕਮਾਂਡ ਪ੍ਰਾਂਪਟ):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (ਪਾਵਰਸ਼ੈੱਲ):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

**ਇਹ ਕਿਉਂ ਲੋੜੀਂਦਾ ਹੈ:**
- GitHub Models ਨੂੰ AI ਮਾਡਲਾਂ ਤੱਕ ਪਹੁੰਚ ਲਈ ਪ੍ਰਮਾਣਿਕਤਾ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ
- ਵਾਤਾਵਰਣ ਵੈਰੀਏਬਲਾਂ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਸੰਵੇਦਨਸ਼ੀਲ ਟੋਕਨ ਨੂੰ ਸੋਰਸ ਕੋਡ ਤੋਂ ਬਾਹਰ ਰੱਖਿਆ ਜਾਂਦਾ ਹੈ
- `models:read` ਸਕੋਪ AI ਇੰਫਰੈਂਸ ਤੱਕ ਪਹੁੰਚ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ

### ਕਦਮ 2: ਬਿਲਡ ਅਤੇ ਚਲਾਓ

ਪ੍ਰੋਜੈਕਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ ਜਾਓ:
```bash
cd 04-PracticalSamples/petstory
```

ਐਪਲੀਕੇਸ਼ਨ ਬਿਲਡ ਕਰੋ:
```bash
mvn clean compile
```

ਸਰਵਰ ਸ਼ੁਰੂ ਕਰੋ:
```bash
mvn spring-boot:run
```

ਐਪਲੀਕੇਸ਼ਨ `http://localhost:8080` 'ਤੇ ਸ਼ੁਰੂ ਹੋਵੇਗਾ।

### ਕਦਮ 3: ਐਪਲੀਕੇਸ਼ਨ ਦੀ ਜਾਂਚ ਕਰੋ

1. **ਖੋਲ੍ਹੋ** `http://localhost:8080` ਆਪਣੇ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ
2. **ਵੇਰਵਾ ਦਿਓ** ਆਪਣੇ ਪਾਲਤੂ ਜਾਨਵਰ ਦਾ ਟੈਕਸਟ ਖੇਤਰ ਵਿੱਚ (ਜਿਵੇਂ, "ਇੱਕ ਖੇਡਣ ਵਾਲਾ ਗੋਲਡਨ ਰਿਟਰੀਵਰ ਜੋ ਫੈਚ ਖੇਡਣਾ ਪਸੰਦ ਕਰਦਾ ਹੈ")
3. **ਕਲਿੱਕ ਕਰੋ** "ਕਹਾਣੀ ਬਣਾਓ" AI ਦੁਆਰਾ ਬਣਾਈ ਕਹਾਣੀ ਪ੍ਰਾਪਤ ਕਰਨ ਲਈ
4. **ਵਿਕਲਪਕ ਤੌਰ 'ਤੇ**, ਇੱਕ ਪਾਲਤੂ ਜਾਨਵਰ ਦੀ ਤਸਵੀਰ ਅਪਲੋਡ ਕਰੋ ਤਾਂ ਜੋ ਵੇਰਵਾ ਆਟੋਮੈਟਿਕ ਤੌਰ 'ਤੇ ਬਣ ਸਕੇ
5. **ਦੇਖੋ** ਆਪਣੇ ਪਾਲਤੂ ਜਾਨਵਰ ਦੇ ਵੇਰਵੇ 'ਤੇ ਆਧਾਰਿਤ ਰਚਨਾਤਮਕ ਕਹਾਣੀ

## ਇਹ ਸਾਰਾ ਕਿਵੇਂ ਕੰਮ ਕਰਦਾ ਹੈ

ਜਦੋਂ ਤੁਸੀਂ ਇੱਕ ਪਾਲਤੂ ਕਹਾਣੀ ਬਣਾਉਂਦੇ ਹੋ, ਤਾਂ ਪੂਰਾ ਪ੍ਰਕਿਰਿਆ ਇਹ ਹੈ:

1. **ਯੂਜ਼ਰ ਇਨਪੁਟ**: ਤੁਸੀਂ ਵੈੱਬ ਫਾਰਮ 'ਤੇ ਆਪਣੇ ਪਾਲਤੂ ਜਾਨਵਰ ਦਾ ਵੇਰਵਾ ਦਿੰਦੇ ਹੋ
2. **ਫਾਰਮ ਸਬਮਿਸ਼ਨ**: ਬ੍ਰਾਊਜ਼ਰ `/generate-story` ਨੂੰ POST ਰਿਕਵੈਸਟ ਭੇਜਦਾ ਹੈ
3. **ਕੰਟਰੋਲਰ ਪ੍ਰੋਸੈਸਿੰਗ**: `PetController` ਇਨਪੁਟ ਦੀ ਵੈਲੀਡੇਸ਼ਨ ਅਤੇ ਸੈਨੀਟਾਈਜ਼ ਕਰਦਾ ਹੈ
4. **AI ਸੇਵਾ ਕਾਲ**: `StoryService` GitHub Models API ਨੂੰ ਰਿਕਵੈਸਟ ਭੇਜਦਾ ਹੈ
5. **ਕਹਾਣੀ ਜਨਰੇਸ਼ਨ**: AI ਵੇਰਵੇ ਦੇ ਆਧਾਰ 'ਤੇ ਰਚਨਾਤਮਕ ਕਹਾਣੀ ਬਣਾਉਂਦਾ ਹੈ
6. **ਰਿਸਪਾਂਸ ਹੈਂਡਲਿੰਗ**: ਕੰਟਰੋਲਰ ਕਹਾਣੀ ਪ੍ਰਾਪਤ ਕਰਦਾ ਹੈ ਅਤੇ ਮਾਡਲ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ
7. **ਟੈਂਪਲੇਟ ਰੈਂਡਰਿੰਗ**: Thymeleaf `result.html` ਨੂੰ ਕਹਾਣੀ ਨਾਲ ਰੈਂਡਰ ਕਰਦਾ ਹੈ
8. **ਪ੍ਰਦਰਸ਼ਨ**: ਯੂਜ਼ਰ ਆਪਣੇ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ ਜਨਰੇਟ ਕੀਤੀ ਕਹਾਣੀ ਦੇਖਦਾ ਹੈ

**ਗਲਤੀ ਸੰਭਾਲਣ ਦਾ ਪ੍ਰਕਿਰਿਆ:**
ਜੇਕਰ AI ਸੇਵਾ ਫੇਲ੍ਹ ਹੋ ਜਾਂਦੀ ਹੈ:
1. ਕੰਟਰੋਲਰ ਐਕਸਪਸ਼ਨ ਕੈਚ ਕਰਦਾ ਹੈ
2. ਪਹਿਲਾਂ ਤੋਂ ਲਿਖੀਆਂ ਕਹਾਣੀ ਟੈਂਪਲੇਟਾਂ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਬੈਕਅੱਪ ਕਹਾਣੀ ਬਣਾਉਂਦਾ ਹੈ
3. AI ਦੀ ਉਪਲਬਧਤਾ ਬਾਰੇ ਨੋਟ ਦੇ ਨਾਲ ਬੈਕਅੱਪ ਕਹਾਣੀ ਦਿਖਾਉਂਦਾ ਹੈ
4. ਯੂਜ਼ਰ ਨੂੰ ਫਿਰ ਵੀ ਇੱਕ ਕਹਾਣੀ ਮਿਲਦੀ ਹੈ, ਜੋ ਚੰਗੇ ਯੂਜ਼ਰ ਅਨੁਭਵ ਨੂੰ ਯਕੀਨੀ ਬਣਾਉਂਦਾ ਹੈ

## AI ਇੰਟੀਗ੍ਰੇਸ਼ਨ ਨੂੰ ਸਮਝਣਾ

### GitHub Models API
ਐਪਲੀਕੇਸ਼ਨ GitHub Models ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ, ਜੋ ਵੱਖ-ਵੱਖ AI ਮਾਡਲਾਂ ਤੱਕ ਮੁਫ਼ਤ ਪਹੁੰਚ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### ਪ੍ਰੌਮਪਟ ਇੰਜੀਨੀਅਰਿੰਗ
ਸੇਵਾ ਵਧੀਆ ਨਤੀਜੇ ਪ੍ਰਾਪਤ ਕਰਨ ਲਈ ਧਿਆਨ ਨਾਲ ਤਿਆਰ ਕੀਤੇ ਪ੍ਰੌਮਪਟ ਦੀ ਵਰਤੋਂ ਕਰਦੀ ਹੈ:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### ਰਿਸਪਾਂਸ ਪ੍ਰੋਸੈਸਿੰਗ
AI ਰਿਸਪਾਂਸ ਨੂੰ ਕੱਢਿਆ ਅਤੇ ਵੈਲੀਡੇਟ ਕੀਤਾ ਜਾਂਦਾ ਹੈ:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## ਅਗਲੇ ਕਦਮ

ਹੋਰ ਉਦਾਹਰਣਾਂ ਲਈ, ਵੇਖੋ [Chapter 04: Practical samples](../README.md)

**ਅਸਵੀਕਾਰਨਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀ ਹੋਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਆਟੋਮੈਟਿਕ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁਚੱਜੇਪਣ ਹੋ ਸਕਦੇ ਹਨ। ਇਸ ਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਮੌਜੂਦ ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਅਧਿਕਾਰਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੇ ਪ੍ਰਯੋਗ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।