<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:15:17+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ne"
}
-->
# पेट स्टोरी जनरेटर ट्युटोरियल शुरुवातका लागि

## सामग्रीको सूची

- [पूर्व आवश्यकताहरू](../../../../04-PracticalSamples/petstory)
- [प्रोजेक्ट संरचना बुझ्ने](../../../../04-PracticalSamples/petstory)
- [मुख्य कम्पोनेन्टहरूको व्याख्या](../../../../04-PracticalSamples/petstory)
  - [१. मुख्य एप्लिकेसन](../../../../04-PracticalSamples/petstory)
  - [२. वेब कन्ट्रोलर](../../../../04-PracticalSamples/petstory)
  - [३. स्टोरी सेवा](../../../../04-PracticalSamples/petstory)
  - [४. वेब टेम्प्लेटहरू](../../../../04-PracticalSamples/petstory)
  - [५. कन्फिगरेसन](../../../../04-PracticalSamples/petstory)
- [एप्लिकेसन चलाउने](../../../../04-PracticalSamples/petstory)
- [सबै कम्पोनेन्टहरू कसरी सँगै काम गर्छन्](../../../../04-PracticalSamples/petstory)
- [एआई एकीकरण बुझ्ने](../../../../04-PracticalSamples/petstory)
- [अगाडि के गर्ने](../../../../04-PracticalSamples/petstory)

## पूर्व आवश्यकताहरू

सुरु गर्नुअघि, सुनिश्चित गर्नुहोस् कि तपाईंले:
- Java 21 वा उच्च संस्करण स्थापना गर्नुभएको छ
- Maven निर्भरता व्यवस्थापनको लागि
- GitHub खाता र `models:read` स्कोप भएको व्यक्तिगत पहुँच टोकन (PAT)
- Java, Spring Boot, र वेब विकासको आधारभूत ज्ञान

## प्रोजेक्ट संरचना बुझ्ने

पेट स्टोरी प्रोजेक्टमा केही महत्त्वपूर्ण फाइलहरू छन्:

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

## मुख्य कम्पोनेन्टहरूको व्याख्या

### १. मुख्य एप्लिकेसन

**फाइल:** `PetStoryApplication.java`

यो हाम्रो Spring Boot एप्लिकेसनको प्रवेश बिन्दु हो:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**यसले के गर्छ:**
- `@SpringBootApplication` एनोटेसनले स्वतः कन्फिगरेसन र कम्पोनेन्ट स्क्यानिंग सक्षम बनाउँछ
- पोर्ट 8080 मा एम्बेडेड वेब सर्भर (Tomcat) सुरु गर्छ
- आवश्यक सबै Spring बीनहरू र सेवाहरू स्वतः सिर्जना गर्छ

### २. वेब कन्ट्रोलर

**फाइल:** `PetController.java`

यो सबै वेब अनुरोधहरू र प्रयोगकर्ता अन्तर्क्रियाहरूलाई ह्यान्डल गर्छ:

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

**मुख्य विशेषताहरू:**

1. **रूट ह्यान्डलिंग**: `@GetMapping("/")` अपलोड फारम देखाउँछ, `@PostMapping("/generate-story")` सबमिशनहरू प्रक्रिया गर्छ
2. **इनपुट मान्यता**: खाली विवरणहरू र लम्बाइ सीमाहरू जाँच गर्छ
3. **सुरक्षा**: प्रयोगकर्ता इनपुटलाई XSS आक्रमणबाट बचाउन सफा गर्छ
4. **त्रुटि ह्यान्डलिंग**: एआई सेवा असफल हुँदा फलब्याक स्टोरीहरू प्रदान गर्छ
5. **मोडेल बाइन्डिङ**: Spring को `Model` प्रयोग गरेर HTML टेम्प्लेटहरूमा डाटा पास गर्छ

**फलब्याक प्रणाली:**
कन्ट्रोलरमा पूर्व-लेखिएको स्टोरी टेम्प्लेटहरू समावेश छन्, जुन एआई सेवा अनुपलब्ध हुँदा प्रयोग गरिन्छ:

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

### ३. स्टोरी सेवा

**फाइल:** `StoryService.java`

यो सेवा GitHub Models सँग संवाद गरेर स्टोरीहरू सिर्जना गर्छ:

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

**मुख्य कम्पोनेन्टहरू:**

1. **OpenAI क्लाइन्ट**: GitHub Models को लागि आधिकारिक OpenAI Java SDK प्रयोग गर्छ
2. **सिस्टम प्रम्प्ट**: एआईलाई परिवारमैत्री पेट स्टोरीहरू लेख्न व्यवहार सेट गर्छ
3. **प्रयोगकर्ता प्रम्प्ट**: विवरणको आधारमा एआईलाई ठ्याक्कै के स्टोरी लेख्ने बताउँछ
4. **प्यारामिटरहरू**: स्टोरीको लम्बाइ र सिर्जनात्मकता स्तर नियन्त्रण गर्छ
5. **त्रुटि ह्यान्डलिंग**: कन्ट्रोलरले समात्ने र ह्यान्डल गर्ने अपवादहरू फ्याँक्छ

### ४. वेब टेम्प्लेटहरू

**फाइल:** `index.html` (अपलोड फारम)

मुख्य पृष्ठ जहाँ प्रयोगकर्ताहरू आफ्नो पेटको विवरण दिन्छन्:

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

**फाइल:** `result.html` (स्टोरी प्रदर्शन)

सिर्जित स्टोरी देखाउँछ:

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

**टेम्प्लेट विशेषताहरू:**

1. **Thymeleaf एकीकरण**: गतिशील सामग्रीको लागि `th:` एट्रिब्युटहरू प्रयोग गर्छ
2. **उत्तरदायी डिजाइन**: मोबाइल र डेस्कटपको लागि CSS स्टाइलिङ
3. **त्रुटि ह्यान्डलिंग**: प्रयोगकर्ताहरूलाई मान्यता त्रुटिहरू देखाउँछ
4. **क्लाइन्ट-साइड प्रक्रिया**: छवि विश्लेषणको लागि JavaScript (Transformers.js प्रयोग गरेर)

### ५. कन्फिगरेसन

**फाइल:** `application.properties`

एप्लिकेसनको लागि कन्फिगरेसन सेटिङहरू:

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

**कन्फिगरेसन व्याख्या:**

1. **फाइल अपलोड**: 10MB सम्मका छविहरू अनुमति दिन्छ
2. **लगिङ**: कार्यान्वयनको क्रममा के जानकारी लग गरिन्छ नियन्त्रण गर्छ
3. **GitHub Models**: कुन एआई मोडेल र अन्त बिन्दु प्रयोग गर्ने निर्दिष्ट गर्छ
4. **सुरक्षा**: संवेदनशील जानकारी नदेखाउन त्रुटि ह्यान्डलिंग कन्फिगरेसन

## एप्लिकेसन चलाउने

### चरण १: आफ्नो GitHub टोकन सेट गर्नुहोस्

पहिले, तपाईंले आफ्नो GitHub टोकनलाई वातावरण चरको रूपमा सेट गर्नुपर्छ:

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

**किन यो आवश्यक छ:**
- GitHub Models ले एआई मोडेलहरूमा पहुँच गर्न प्रमाणिकरण आवश्यक छ
- वातावरण चरहरू प्रयोग गर्दा संवेदनशील टोकनहरू स्रोत कोडबाट बाहिर राखिन्छ
- `models:read` स्कोपले एआई इनफरेन्समा पहुँच प्रदान गर्छ

### चरण २: निर्माण र चलाउनुहोस्

प्रोजेक्ट डाइरेक्टरीमा जानुहोस्:
```bash
cd 04-PracticalSamples/petstory
```

एप्लिकेसन निर्माण गर्नुहोस्:
```bash
mvn clean compile
```

सर्भर सुरु गर्नुहोस्:
```bash
mvn spring-boot:run
```

एप्लिकेसन `http://localhost:8080` मा सुरु हुनेछ।

### चरण ३: एप्लिकेसन परीक्षण गर्नुहोस्

1. **खोल्नुहोस्** `http://localhost:8080` आफ्नो ब्राउजरमा
2. **वर्णन गर्नुहोस्** आफ्नो पेटलाई टेक्स्ट क्षेत्रमा (जस्तै, "एक खेलप्रेमी गोल्डन रिट्रिभर जसलाई फेच खेल्न मन पर्छ")
3. **क्लिक गर्नुहोस्** "Generate Story" एआई-जनित स्टोरी प्राप्त गर्न
4. **वैकल्पिक रूपमा**, पेटको छवि अपलोड गरेर स्वतः विवरण सिर्जना गर्नुहोस्
5. **हेर्नुहोस्** आफ्नो पेटको विवरणको आधारमा सिर्जनात्मक स्टोरी

## सबै कम्पोनेन्टहरू कसरी सँगै काम गर्छन्

जब तपाईं पेट स्टोरी सिर्जना गर्नुहुन्छ, यहाँ सम्पूर्ण प्रक्रिया छ:

1. **प्रयोगकर्ता इनपुट**: तपाईं वेब फारममा आफ्नो पेटको वर्णन गर्नुहुन्छ
2. **फारम सबमिशन**: ब्राउजरले `/generate-story` मा POST अनुरोध पठाउँछ
3. **कन्ट्रोलर प्रक्रिया**: `PetController` इनपुटलाई मान्यता दिन्छ र सफा गर्छ
4. **एआई सेवा कल**: `StoryService` GitHub Models API मा अनुरोध पठाउँछ
5. **स्टोरी सिर्जना**: एआईले विवरणको आधारमा सिर्जनात्मक स्टोरी सिर्जना गर्छ
6. **प्रतिक्रिया ह्यान्डलिंग**: कन्ट्रोलरले स्टोरी प्राप्त गर्छ र मोडेलमा थप्छ
7. **टेम्प्लेट रेंडरिंग**: Thymeleaf `result.html` लाई स्टोरीसहित रेंडर गर्छ
8. **प्रदर्शन**: प्रयोगकर्ताले आफ्नो ब्राउजरमा सिर्जित स्टोरी देख्छ

**त्रुटि ह्यान्डलिंग प्रक्रिया:**
यदि एआई सेवा असफल भयो भने:
1. कन्ट्रोलरले अपवाद समात्छ
2. पूर्व-लेखिएको टेम्प्लेट प्रयोग गरेर फलब्याक स्टोरी सिर्जना गर्छ
3. एआई अनुपलब्धताको बारेमा नोटसहित फलब्याक स्टोरी देखाउँछ
4. प्रयोगकर्ताले अझै पनि स्टोरी प्राप्त गर्छ, राम्रो प्रयोगकर्ता अनुभव सुनिश्चित गर्दै

## एआई एकीकरण बुझ्ने

### GitHub Models API
एप्लिकेसनले GitHub Models प्रयोग गर्छ, जसले विभिन्न एआई मोडेलहरूमा निःशुल्क पहुँच प्रदान गर्छ:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### प्रम्प्ट इन्जिनियरिङ
सेवाले राम्रो परिणाम प्राप्त गर्न सावधानीपूर्वक तयार गरिएको प्रम्प्टहरू प्रयोग गर्छ:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### प्रतिक्रिया प्रक्रिया
एआई प्रतिक्रिया निकालिन्छ र मान्यता दिइन्छ:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## अगाडि के गर्ने

थप उदाहरणहरूको लागि, [Chapter 04: Practical samples](../README.md) हेर्नुहोस्।

**अस्वीकरण**:  
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको छ। हामी शुद्धताको लागि प्रयास गर्छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छ। यसको मूल भाषा मा रहेको मूल दस्तावेज़लाई आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याको लागि हामी जिम्मेवार हुने छैनौं।