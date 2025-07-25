<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:12:24+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "mr"
}
-->
# पाळीव प्राणी कथा जनरेटर शिकवणी नवशिक्यांसाठी

## विषय सूची

- [पूर्वतयारी](../../../../04-PracticalSamples/petstory)
- [प्रकल्प संरचना समजून घेणे](../../../../04-PracticalSamples/petstory)
- [मुख्य घटकांचे स्पष्टीकरण](../../../../04-PracticalSamples/petstory)
  - [1. मुख्य अनुप्रयोग](../../../../04-PracticalSamples/petstory)
  - [2. वेब नियंत्रक](../../../../04-PracticalSamples/petstory)
  - [3. कथा सेवा](../../../../04-PracticalSamples/petstory)
  - [4. वेब टेम्पलेट्स](../../../../04-PracticalSamples/petstory)
  - [5. कॉन्फिगरेशन](../../../../04-PracticalSamples/petstory)
- [अनुप्रयोग चालवणे](../../../../04-PracticalSamples/petstory)
- [सर्व घटक कसे एकत्र काम करतात](../../../../04-PracticalSamples/petstory)
- [एआय एकत्रीकरण समजून घेणे](../../../../04-PracticalSamples/petstory)
- [पुढील पायऱ्या](../../../../04-PracticalSamples/petstory)

## पूर्वतयारी

सुरुवात करण्यापूर्वी, तुमच्याकडे खालील गोष्टी असणे आवश्यक आहे:
- Java 21 किंवा त्याहून अधिक आवृत्ती स्थापित असणे
- Maven dependency व्यवस्थापनासाठी
- `models:read` स्कोपसह GitHub खाते आणि वैयक्तिक प्रवेश टोकन (PAT)
- Java, Spring Boot, आणि वेब विकासाची मूलभूत समज

## प्रकल्प संरचना समजून घेणे

पाळीव प्राणी कथा प्रकल्पामध्ये काही महत्त्वाचे फाइल्स आहेत:

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

## मुख्य घटकांचे स्पष्टीकरण

### 1. मुख्य अनुप्रयोग

**फाइल:** `PetStoryApplication.java`

हे आमच्या Spring Boot अनुप्रयोगाचे प्रवेश बिंदू आहे:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**याचा उपयोग:**
- `@SpringBootApplication` अ‍ॅनोटेशन ऑटो-कॉन्फिगरेशन आणि घटक स्कॅनिंग सक्षम करते
- पोर्ट 8080 वर एम्बेडेड वेब सर्व्हर (Tomcat) सुरू करते
- आवश्यक असलेल्या सर्व Spring beans आणि सेवांची स्वयंचलित निर्मिती करते

### 2. वेब नियंत्रक

**फाइल:** `PetController.java`

हे सर्व वेब विनंत्या आणि वापरकर्ता संवाद हाताळते:

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

**मुख्य वैशिष्ट्ये:**

1. **मार्ग हाताळणी**: `@GetMapping("/")` अपलोड फॉर्म दाखवते, `@PostMapping("/generate-story")` सबमिशन प्रक्रिया करते
2. **इनपुट सत्यापन**: रिक्त वर्णन आणि लांबी मर्यादा तपासते
3. **सुरक्षा**: वापरकर्ता इनपुट स्वच्छ करते जेणेकरून XSS हल्ले टाळता येतील
4. **त्रुटी हाताळणी**: एआय सेवा अयशस्वी झाल्यास फॉलबॅक कथा प्रदान करते
5. **मॉडेल बाइंडिंग**: Spring च्या `Model` वापरून HTML टेम्पलेट्समध्ये डेटा पास करते

**फॉलबॅक प्रणाली:**
नियंत्रकामध्ये पूर्व-लिखित कथा टेम्पलेट्स समाविष्ट आहेत, जे एआय सेवा अनुपलब्ध असताना वापरले जातात:

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

### 3. कथा सेवा

**फाइल:** `StoryService.java`

ही सेवा GitHub Models शी संवाद साधून कथा तयार करते:

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

1. **OpenAI क्लायंट**: GitHub Models साठी कॉन्फिगर केलेले अधिकृत OpenAI Java SDK वापरते
2. **सिस्टम प्रॉम्प्ट**: एआयला कौटुंबिक-अनुकूल पाळीव प्राणी कथा लिहिण्यास सांगते
3. **वापरकर्ता प्रॉम्प्ट**: वर्णनावर आधारित एआयला नेमकी कथा लिहिण्यास सांगते
4. **पॅरामीटर्स**: कथा लांबी आणि सर्जनशीलता स्तर नियंत्रित करते
5. **त्रुटी हाताळणी**: अपवाद फेकते जे नियंत्रक पकडतो आणि हाताळतो

### 4. वेब टेम्पलेट्स

**फाइल:** `index.html` (अपलोड फॉर्म)

मुख्य पृष्ठ जिथे वापरकर्ते त्यांच्या पाळीव प्राण्यांचे वर्णन करतात:

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

**फाइल:** `result.html` (कथा प्रदर्शन)

निर्मित कथा दाखवते:

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

**टेम्पलेट वैशिष्ट्ये:**

1. **Thymeleaf एकत्रीकरण**: डायनॅमिक सामग्रीसाठी `th:` गुणधर्म वापरते
2. **प्रतिसादात्मक डिझाइन**: मोबाइल आणि डेस्कटॉपसाठी CSS स्टाइलिंग
3. **त्रुटी हाताळणी**: वापरकर्त्यांना सत्यापन त्रुटी दाखवते
4. **क्लायंट-साइड प्रक्रिया**: प्रतिमा विश्लेषणासाठी JavaScript (Transformers.js वापरून)

### 5. कॉन्फिगरेशन

**फाइल:** `application.properties`

अनुप्रयोगासाठी कॉन्फिगरेशन सेटिंग्ज:

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

**कॉन्फिगरेशन स्पष्टीकरण:**

1. **फाइल अपलोड**: 10MB पर्यंत प्रतिमा परवानगी देते
2. **लॉगिंग**: अंमलबजावणी दरम्यान कोणती माहिती लॉग केली जाते ते नियंत्रित करते
3. **GitHub Models**: कोणता एआय मॉडेल आणि एंडपॉइंट वापरायचा ते निर्दिष्ट करते
4. **सुरक्षा**: संवेदनशील माहिती उघड होऊ नये यासाठी त्रुटी हाताळणी कॉन्फिगरेशन

## अनुप्रयोग चालवणे

### चरण 1: तुमचा GitHub टोकन सेट करा

प्रथम, तुम्हाला तुमचा GitHub टोकन पर्यावरणीय व्हेरिएबल म्हणून सेट करणे आवश्यक आहे:

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

**याची गरज का आहे:**
- GitHub Models एआय मॉडेल्समध्ये प्रवेश करण्यासाठी प्रमाणीकरण आवश्यक आहे
- पर्यावरणीय व्हेरिएबल्स वापरल्याने संवेदनशील टोकन स्रोत कोडपासून दूर राहतात
- `models:read` स्कोप एआय इनफरन्समध्ये प्रवेश प्रदान करतो

### चरण 2: बिल्ड आणि रन करा

प्रकल्प निर्देशिकेत जा:
```bash
cd 04-PracticalSamples/petstory
```

अनुप्रयोग बिल्ड करा:
```bash
mvn clean compile
```

सर्व्हर सुरू करा:
```bash
mvn spring-boot:run
```

अनुप्रयोग `http://localhost:8080` वर सुरू होईल.

### चरण 3: अनुप्रयोगाची चाचणी करा

1. **उघडा** `http://localhost:8080` तुमच्या ब्राउझरमध्ये
2. **वर्णन करा** तुमच्या पाळीव प्राण्याचे मजकूर क्षेत्रात (उदा., "एक खेळकर गोल्डन रिट्रीव्हर जो फेच खेळायला आवडतो")
3. **क्लिक करा** "Generate Story" एआय-निर्मित कथा मिळवण्यासाठी
4. **पर्यायाने**, पाळीव प्राण्याची प्रतिमा अपलोड करा जेणेकरून वर्णन आपोआप तयार होईल
5. **पहा** तुमच्या पाळीव प्राण्याच्या वर्णनावर आधारित सर्जनशील कथा

## सर्व घटक कसे एकत्र काम करतात

जेव्हा तुम्ही पाळीव प्राणी कथा तयार करता तेव्हा संपूर्ण प्रवाह असा आहे:

1. **वापरकर्ता इनपुट**: तुम्ही वेब फॉर्मवर तुमच्या पाळीव प्राण्याचे वर्णन करता
2. **फॉर्म सबमिशन**: ब्राउझर `/generate-story` ला POST विनंती पाठवतो
3. **नियंत्रक प्रक्रिया**: `PetController` इनपुट सत्यापित आणि स्वच्छ करते
4. **एआय सेवा कॉल**: `StoryService` GitHub Models API ला विनंती पाठवते
5. **कथा निर्मिती**: एआय वर्णनावर आधारित सर्जनशील कथा तयार करते
6. **प्रतिसाद हाताळणी**: नियंत्रक कथा प्राप्त करतो आणि ती मॉडेलमध्ये जोडतो
7. **टेम्पलेट रेंडरिंग**: Thymeleaf `result.html` कथा सह रेंडर करते
8. **प्रदर्शन**: वापरकर्त्याला त्यांच्या ब्राउझरमध्ये निर्मित कथा दिसते

**त्रुटी हाताळणी प्रवाह:**
जर एआय सेवा अयशस्वी झाली:
1. नियंत्रक अपवाद पकडतो
2. पूर्व-लिखित टेम्पलेट्स वापरून फॉलबॅक कथा तयार करतो
3. एआय अनुपलब्धतेबद्दल नोटसह फॉलबॅक कथा प्रदर्शित करतो
4. वापरकर्त्याला तरीही कथा मिळते, चांगला वापरकर्ता अनुभव सुनिश्चित करतो

## एआय एकत्रीकरण समजून घेणे

### GitHub Models API
अनुप्रयोग GitHub Models वापरतो, जे विविध एआय मॉडेल्ससाठी विनामूल्य प्रवेश प्रदान करते:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### प्रॉम्प्ट इंजिनिअरिंग
सेवा चांगले परिणाम मिळवण्यासाठी काळजीपूर्वक तयार केलेले प्रॉम्प्ट वापरते:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### प्रतिसाद प्रक्रिया
एआय प्रतिसाद काढला जातो आणि सत्यापित केला जातो:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## पुढील पायऱ्या

अधिक उदाहरणांसाठी, [Chapter 04: Practical samples](../README.md) पहा

**अस्वीकरण**:  
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरे त्रुटी किंवा अचूकतेच्या अभावाने युक्त असू शकतात. मूळ भाषेतील दस्तऐवज हा अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराचा वापर करून उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थासाठी आम्ही जबाबदार राहणार नाही.