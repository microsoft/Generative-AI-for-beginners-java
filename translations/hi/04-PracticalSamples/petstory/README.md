<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:07:10+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "hi"
}
-->
# पालतू कहानी जनरेटर ट्यूटोरियल शुरुआती लोगों के लिए

## विषय सूची

- [पूर्व आवश्यकताएँ](../../../../04-PracticalSamples/petstory)
- [प्रोजेक्ट संरचना को समझना](../../../../04-PracticalSamples/petstory)
- [मुख्य घटकों की व्याख्या](../../../../04-PracticalSamples/petstory)
  - [1. मुख्य एप्लिकेशन](../../../../04-PracticalSamples/petstory)
  - [2. वेब कंट्रोलर](../../../../04-PracticalSamples/petstory)
  - [3. कहानी सेवा](../../../../04-PracticalSamples/petstory)
  - [4. वेब टेम्पलेट्स](../../../../04-PracticalSamples/petstory)
  - [5. कॉन्फ़िगरेशन](../../../../04-PracticalSamples/petstory)
- [एप्लिकेशन चलाना](../../../../04-PracticalSamples/petstory)
- [यह सब कैसे एक साथ काम करता है](../../../../04-PracticalSamples/petstory)
- [एआई इंटीग्रेशन को समझना](../../../../04-PracticalSamples/petstory)
- [अगले कदम](../../../../04-PracticalSamples/petstory)

## पूर्व आवश्यकताएँ

शुरू करने से पहले, सुनिश्चित करें कि आपके पास निम्नलिखित हैं:
- Java 21 या उससे उच्च संस्करण इंस्टॉल हो
- Maven डिपेंडेंसी प्रबंधन के लिए
- एक GitHub खाता जिसमें `models:read` स्कोप के साथ व्यक्तिगत एक्सेस टोकन (PAT) हो
- Java, Spring Boot, और वेब विकास की बुनियादी समझ

## प्रोजेक्ट संरचना को समझना

पालतू कहानी प्रोजेक्ट में कई महत्वपूर्ण फाइलें हैं:

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

## मुख्य घटकों की व्याख्या

### 1. मुख्य एप्लिकेशन

**फाइल:** `PetStoryApplication.java`

यह हमारी Spring Boot एप्लिकेशन का प्रवेश बिंदु है:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**यह क्या करता है:**
- `@SpringBootApplication` एनोटेशन ऑटो-कॉन्फ़िगरेशन और घटक स्कैनिंग को सक्षम करता है
- पोर्ट 8080 पर एक एम्बेडेड वेब सर्वर (Tomcat) शुरू करता है
- स्वचालित रूप से सभी आवश्यक Spring बीन्स और सेवाएँ बनाता है

### 2. वेब कंट्रोलर

**फाइल:** `PetController.java`

यह सभी वेब अनुरोधों और उपयोगकर्ता इंटरैक्शन को संभालता है:

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

**मुख्य विशेषताएँ:**

1. **रूट हैंडलिंग:** `@GetMapping("/")` अपलोड फॉर्म दिखाता है, `@PostMapping("/generate-story")` सबमिशन को प्रोसेस करता है
2. **इनपुट वैलिडेशन:** खाली विवरण और लंबाई सीमा की जाँच करता है
3. **सुरक्षा:** उपयोगकर्ता इनपुट को XSS हमलों से बचाने के लिए सैनिटाइज़ करता है
4. **त्रुटि प्रबंधन:** एआई सेवा विफल होने पर बैकअप कहानियाँ प्रदान करता है
5. **मॉडल बाइंडिंग:** Spring के `Model` का उपयोग करके डेटा HTML टेम्पलेट्स में पास करता है

**बैकअप सिस्टम:**
कंट्रोलर में पहले से लिखी गई कहानी टेम्पलेट्स शामिल हैं, जो एआई सेवा अनुपलब्ध होने पर उपयोग की जाती हैं:

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

### 3. कहानी सेवा

**फाइल:** `StoryService.java`

यह सेवा GitHub Models के साथ संवाद करके कहानियाँ उत्पन्न करती है:

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

**मुख्य घटक:**

1. **OpenAI क्लाइंट:** GitHub Models के लिए कॉन्फ़िगर किया गया आधिकारिक OpenAI Java SDK का उपयोग करता है
2. **सिस्टम प्रॉम्प्ट:** एआई के व्यवहार को पारिवारिक-अनुकूल पालतू कहानियाँ लिखने के लिए सेट करता है
3. **उपयोगकर्ता प्रॉम्प्ट:** एआई को बताता है कि विवरण के आधार पर कौन सी कहानी लिखनी है
4. **पैरामीटर्स:** कहानी की लंबाई और रचनात्मकता स्तर को नियंत्रित करता है
5. **त्रुटि प्रबंधन:** अपवाद फेंकता है जिसे कंट्रोलर पकड़ता और संभालता है

### 4. वेब टेम्पलेट्स

**फाइल:** `index.html` (अपलोड फॉर्म)

मुख्य पृष्ठ जहाँ उपयोगकर्ता अपने पालतू जानवरों का विवरण देते हैं:

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

**फाइल:** `result.html` (कहानी प्रदर्शन)

उत्पन्न कहानी दिखाता है:

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

**टेम्पलेट विशेषताएँ:**

1. **Thymeleaf इंटीग्रेशन:** डायनामिक सामग्री के लिए `th:` एट्रिब्यूट्स का उपयोग करता है
2. **उत्तरदायी डिज़ाइन:** मोबाइल और डेस्कटॉप के लिए CSS स्टाइलिंग
3. **त्रुटि प्रबंधन:** उपयोगकर्ताओं को वैलिडेशन त्रुटियाँ दिखाता है
4. **क्लाइंट-साइड प्रोसेसिंग:** छवि विश्लेषण के लिए JavaScript (Transformers.js का उपयोग करके)

### 5. कॉन्फ़िगरेशन

**फाइल:** `application.properties`

एप्लिकेशन के लिए कॉन्फ़िगरेशन सेटिंग्स:

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

**कॉन्फ़िगरेशन की व्याख्या:**

1. **फाइल अपलोड:** 10MB तक की छवियों की अनुमति देता है
2. **लॉगिंग:** निष्पादन के दौरान कौन सी जानकारी लॉग की जाती है, इसे नियंत्रित करता है
3. **GitHub Models:** उपयोग किए जाने वाले एआई मॉडल और एंडपॉइंट को निर्दिष्ट करता है
4. **सुरक्षा:** संवेदनशील जानकारी को उजागर करने से बचने के लिए त्रुटि प्रबंधन कॉन्फ़िगरेशन

## एप्लिकेशन चलाना

### चरण 1: अपना GitHub टोकन सेट करें

सबसे पहले, आपको अपने GitHub टोकन को एक पर्यावरण चर के रूप में सेट करना होगा:

**Windows (कमांड प्रॉम्प्ट):**
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

**यह क्यों आवश्यक है:**
- GitHub Models को एआई मॉडल तक पहुँचने के लिए प्रमाणीकरण की आवश्यकता होती है
- पर्यावरण चर का उपयोग संवेदनशील टोकन को स्रोत कोड से बाहर रखता है
- `models:read` स्कोप एआई इनफेरेंस तक पहुँच प्रदान करता है

### चरण 2: बिल्ड और रन करें

प्रोजेक्ट डायरेक्टरी पर जाएँ:
```bash
cd 04-PracticalSamples/petstory
```

एप्लिकेशन को बिल्ड करें:
```bash
mvn clean compile
```

सर्वर शुरू करें:
```bash
mvn spring-boot:run
```

एप्लिकेशन `http://localhost:8080` पर शुरू होगा।

### चरण 3: एप्लिकेशन का परीक्षण करें

1. **खोलें** `http://localhost:8080` अपने ब्राउज़र में
2. **विवरण दें** अपने पालतू जानवर का टेक्स्ट एरिया में (जैसे, "एक चंचल गोल्डन रिट्रीवर जो गेंद लाना पसंद करता है")
3. **क्लिक करें** "Generate Story" एआई-जनित कहानी प्राप्त करने के लिए
4. **वैकल्पिक रूप से**, एक पालतू छवि अपलोड करें ताकि स्वचालित रूप से विवरण उत्पन्न हो सके
5. **देखें** अपने पालतू जानवर के विवरण के आधार पर बनाई गई रचनात्मक कहानी

## यह सब कैसे एक साथ काम करता है

जब आप एक पालतू कहानी उत्पन्न करते हैं, तो यहाँ पूरा प्रवाह है:

1. **उपयोगकर्ता इनपुट:** आप वेब फॉर्म पर अपने पालतू जानवर का विवरण देते हैं
2. **फॉर्म सबमिशन:** ब्राउज़र `/generate-story` पर POST अनुरोध भेजता है
3. **कंट्रोलर प्रोसेसिंग:** `PetController` इनपुट को मान्य और सैनिटाइज़ करता है
4. **एआई सेवा कॉल:** `StoryService` GitHub Models API को अनुरोध भेजता है
5. **कहानी निर्माण:** एआई विवरण के आधार पर एक रचनात्मक कहानी उत्पन्न करता है
6. **प्रतिक्रिया प्रबंधन:** कंट्रोलर कहानी प्राप्त करता है और इसे मॉडल में जोड़ता है
7. **टेम्पलेट रेंडरिंग:** Thymeleaf `result.html` को कहानी के साथ रेंडर करता है
8. **प्रदर्शन:** उपयोगकर्ता अपने ब्राउज़र में उत्पन्न कहानी देखते हैं

**त्रुटि प्रबंधन प्रवाह:**
यदि एआई सेवा विफल होती है:
1. कंट्रोलर अपवाद को पकड़ता है
2. पहले से लिखी गई टेम्पलेट्स का उपयोग करके एक बैकअप कहानी उत्पन्न करता है
3. एआई अनुपलब्धता के बारे में एक नोट के साथ बैकअप कहानी प्रदर्शित करता है
4. उपयोगकर्ता को फिर भी एक कहानी मिलती है, जिससे अच्छा उपयोगकर्ता अनुभव सुनिश्चित होता है

## एआई इंटीग्रेशन को समझना

### GitHub Models API
एप्लिकेशन GitHub Models का उपयोग करता है, जो विभिन्न एआई मॉडलों तक मुफ्त पहुँच प्रदान करता है:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### प्रॉम्प्ट इंजीनियरिंग
सेवा अच्छे परिणाम प्राप्त करने के लिए सावधानीपूर्वक तैयार किए गए प्रॉम्प्ट का उपयोग करती है:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### प्रतिक्रिया प्रसंस्करण
एआई प्रतिक्रिया को निकाला और मान्य किया जाता है:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## अगले कदम

अधिक उदाहरणों के लिए, देखें [अध्याय 04: व्यावहारिक नमूने](../README.md)

**अस्वीकरण**:  
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता सुनिश्चित करने का प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल भाषा में उपलब्ध मूल दस्तावेज़ को प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।