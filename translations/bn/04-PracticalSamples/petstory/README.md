<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:09:54+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "bn"
}
-->
# পেট স্টোরি জেনারেটর টিউটোরিয়াল নবাগতদের জন্য

## বিষয়সূচি

- [প্রয়োজনীয়তা](../../../../04-PracticalSamples/petstory)
- [প্রকল্পের কাঠামো বোঝা](../../../../04-PracticalSamples/petstory)
- [মূল উপাদানগুলোর ব্যাখ্যা](../../../../04-PracticalSamples/petstory)
  - [১. প্রধান অ্যাপ্লিকেশন](../../../../04-PracticalSamples/petstory)
  - [২. ওয়েব কন্ট্রোলার](../../../../04-PracticalSamples/petstory)
  - [৩. স্টোরি সার্ভিস](../../../../04-PracticalSamples/petstory)
  - [৪. ওয়েব টেমপ্লেট](../../../../04-PracticalSamples/petstory)
  - [৫. কনফিগারেশন](../../../../04-PracticalSamples/petstory)
- [অ্যাপ্লিকেশন চালানো](../../../../04-PracticalSamples/petstory)
- [সবকিছু কীভাবে একসাথে কাজ করে](../../../../04-PracticalSamples/petstory)
- [এআই ইন্টিগ্রেশন বোঝা](../../../../04-PracticalSamples/petstory)
- [পরবর্তী পদক্ষেপ](../../../../04-PracticalSamples/petstory)

## প্রয়োজনীয়তা

শুরু করার আগে নিশ্চিত করুন যে আপনার কাছে রয়েছে:
- Java 21 বা তার বেশি সংস্করণ ইনস্টল করা
- Maven ডিপেনডেন্সি ম্যানেজমেন্টের জন্য
- একটি GitHub অ্যাকাউন্ট এবং `models:read` স্কোপ সহ একটি ব্যক্তিগত অ্যাক্সেস টোকেন (PAT)
- Java, Spring Boot এবং ওয়েব ডেভেলপমেন্টের মৌলিক ধারণা

## প্রকল্পের কাঠামো বোঝা

পেট স্টোরি প্রকল্পে কয়েকটি গুরুত্বপূর্ণ ফাইল রয়েছে:

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

## মূল উপাদানগুলোর ব্যাখ্যা

### ১. প্রধান অ্যাপ্লিকেশন

**ফাইল:** `PetStoryApplication.java`

এটি আমাদের Spring Boot অ্যাপ্লিকেশনের এন্ট্রি পয়েন্ট:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**এটি কী করে:**
- `@SpringBootApplication` অ্যানোটেশনটি অটো-কনফিগারেশন এবং কম্পোনেন্ট স্ক্যানিং সক্রিয় করে
- পোর্ট 8080-এ একটি এম্বেডেড ওয়েব সার্ভার (Tomcat) চালু করে
- প্রয়োজনীয় Spring বিয়ান এবং সার্ভিসগুলো স্বয়ংক্রিয়ভাবে তৈরি করে

### ২. ওয়েব কন্ট্রোলার

**ফাইল:** `PetController.java`

এটি সমস্ত ওয়েব অনুরোধ এবং ব্যবহারকারীর ইন্টারঅ্যাকশন পরিচালনা করে:

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

**মূল বৈশিষ্ট্য:**

1. **রুট হ্যান্ডলিং**: `@GetMapping("/")` আপলোড ফর্ম দেখায়, `@PostMapping("/generate-story")` জমা দেওয়া প্রক্রিয়া করে
2. **ইনপুট যাচাই**: খালি বিবরণ এবং দৈর্ঘ্যের সীমা পরীক্ষা করে
3. **নিরাপত্তা**: ব্যবহারকারীর ইনপুট স্যানিটাইজ করে XSS আক্রমণ প্রতিরোধ করতে
4. **ত্রুটি পরিচালনা**: AI সার্ভিস ব্যর্থ হলে ব্যাকআপ স্টোরি প্রদান করে
5. **মডেল বাইন্ডিং**: Spring-এর `Model` ব্যবহার করে HTML টেমপ্লেটে ডেটা পাস করে

**ব্যাকআপ সিস্টেম:**
কন্ট্রোলারে প্রি-রাইটেন স্টোরি টেমপ্লেট রয়েছে যা AI সার্ভিস অনুপলব্ধ হলে ব্যবহার করা হয়:

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

### ৩. স্টোরি সার্ভিস

**ফাইল:** `StoryService.java`

এই সার্ভিস GitHub Models-এর সাথে যোগাযোগ করে স্টোরি তৈরি করতে:

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

**মূল উপাদান:**

1. **OpenAI ক্লায়েন্ট**: GitHub Models-এর জন্য কনফিগার করা অফিসিয়াল OpenAI Java SDK ব্যবহার করে
2. **সিস্টেম প্রম্পট**: AI-কে পরিবার-বান্ধব পেট স্টোরি লেখার জন্য সেট করে
3. **ইউজার প্রম্পট**: AI-কে নির্দিষ্ট বিবরণের উপর ভিত্তি করে স্টোরি লেখার নির্দেশ দেয়
4. **প্যারামিটার**: স্টোরির দৈর্ঘ্য এবং সৃজনশীলতার স্তর নিয়ন্ত্রণ করে
5. **ত্রুটি পরিচালনা**: এক্সেপশন ছুঁড়ে দেয় যা কন্ট্রোলার ধরে এবং পরিচালনা করে

### ৪. ওয়েব টেমপ্লেট

**ফাইল:** `index.html` (আপলোড ফর্ম)

প্রধান পৃষ্ঠা যেখানে ব্যবহারকারীরা তাদের পেটের বিবরণ দেন:

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

**ফাইল:** `result.html` (স্টোরি প্রদর্শন)

তৈরি করা স্টোরি দেখায়:

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

**টেমপ্লেট বৈশিষ্ট্য:**

1. **Thymeleaf ইন্টিগ্রেশন**: ডায়নামিক কন্টেন্টের জন্য `th:` অ্যাট্রিবিউট ব্যবহার করে
2. **রেসপন্সিভ ডিজাইন**: মোবাইল এবং ডেস্কটপের জন্য CSS স্টাইলিং
3. **ত্রুটি পরিচালনা**: ব্যবহারকারীদের যাচাইকরণ ত্রুটি দেখায়
4. **ক্লায়েন্ট-সাইড প্রসেসিং**: Transformers.js ব্যবহার করে ইমেজ বিশ্লেষণের জন্য JavaScript

### ৫. কনফিগারেশন

**ফাইল:** `application.properties`

অ্যাপ্লিকেশনের কনফিগারেশন সেটিংস:

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

**কনফিগারেশন ব্যাখ্যা:**

1. **ফাইল আপলোড**: 10MB পর্যন্ত ইমেজ অনুমোদন করে
2. **লগিং**: এক্সিকিউশনের সময় কোন তথ্য লগ হবে তা নিয়ন্ত্রণ করে
3. **GitHub Models**: কোন AI মডেল এবং এন্ডপয়েন্ট ব্যবহার করতে হবে তা নির্দিষ্ট করে
4. **নিরাপত্তা**: সংবেদনশীল তথ্য প্রকাশ এড়াতে ত্রুটি পরিচালনার কনফিগারেশন

## অ্যাপ্লিকেশন চালানো

### ধাপ ১: আপনার GitHub টোকেন সেট করুন

প্রথমে, আপনার GitHub টোকেন একটি পরিবেশ ভেরিয়েবল হিসেবে সেট করুন:

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

**কেন এটি প্রয়োজন:**
- GitHub Models AI মডেল অ্যাক্সেস করতে প্রমাণীকরণ প্রয়োজন
- পরিবেশ ভেরিয়েবল ব্যবহার করে সংবেদনশীল টোকেন সোর্স কোড থেকে দূরে রাখা হয়
- `models:read` স্কোপ AI ইনফারেন্স অ্যাক্সেস প্রদান করে

### ধাপ ২: বিল্ড এবং চালান

প্রকল্প ডিরেক্টরিতে যান:
```bash
cd 04-PracticalSamples/petstory
```

অ্যাপ্লিকেশন বিল্ড করুন:
```bash
mvn clean compile
```

সার্ভার চালু করুন:
```bash
mvn spring-boot:run
```

অ্যাপ্লিকেশন `http://localhost:8080`-এ শুরু হবে।

### ধাপ ৩: অ্যাপ্লিকেশন পরীক্ষা করুন

1. **ওপেন করুন** `http://localhost:8080` আপনার ব্রাউজারে
2. **বর্ণনা করুন** আপনার পেটের বিবরণ টেক্সট এরিয়াতে (যেমন, "একটি খেলাধুলাপ্রিয় গোল্ডেন রিট্রিভার যে ফেচ খেলতে ভালোবাসে")
3. **ক্লিক করুন** "Generate Story" AI-জেনারেটেড স্টোরি পেতে
4. **অন্যভাবে**, একটি পেট ইমেজ আপলোড করুন স্বয়ংক্রিয়ভাবে বিবরণ তৈরি করতে
5. **দেখুন** আপনার পেটের বিবরণের উপর ভিত্তি করে সৃজনশীল স্টোরি

## সবকিছু কীভাবে একসাথে কাজ করে

আপনি যখন একটি পেট স্টোরি তৈরি করেন তখন সম্পূর্ণ প্রবাহটি এখানে:

1. **ব্যবহারকারীর ইনপুট**: আপনি ওয়েব ফর্মে আপনার পেটের বিবরণ দেন
2. **ফর্ম জমা**: ব্রাউজার `/generate-story`-এ POST অনুরোধ পাঠায়
3. **কন্ট্রোলার প্রসেসিং**: `PetController` ইনপুট যাচাই এবং স্যানিটাইজ করে
4. **AI সার্ভিস কল**: `StoryService` GitHub Models API-তে অনুরোধ পাঠায়
5. **স্টোরি তৈরি**: AI বিবরণের উপর ভিত্তি করে একটি সৃজনশীল স্টোরি তৈরি করে
6. **প্রতিক্রিয়া পরিচালনা**: কন্ট্রোলার স্টোরি গ্রহণ করে এবং মডেলে যোগ করে
7. **টেমপ্লেট রেন্ডারিং**: Thymeleaf `result.html` স্টোরি সহ রেন্ডার করে
8. **প্রদর্শন**: ব্যবহারকারী তাদের ব্রাউজারে তৈরি করা স্টোরি দেখতে পান

**ত্রুটি পরিচালনার প্রবাহ:**
যদি AI সার্ভিস ব্যর্থ হয়:
1. কন্ট্রোলার এক্সেপশন ধরে
2. প্রি-রাইটেন টেমপ্লেট ব্যবহার করে একটি ব্যাকআপ স্টোরি তৈরি করে
3. AI অনুপলব্ধতার একটি নোট সহ ব্যাকআপ স্টোরি প্রদর্শন করে
4. ব্যবহারকারী এখনও একটি স্টোরি পান, যা ভালো ব্যবহারকারীর অভিজ্ঞতা নিশ্চিত করে

## এআই ইন্টিগ্রেশন বোঝা

### GitHub Models API
অ্যাপ্লিকেশন GitHub Models ব্যবহার করে, যা বিভিন্ন AI মডেলের বিনামূল্যে অ্যাক্সেস প্রদান করে:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### প্রম্পট ইঞ্জিনিয়ারিং
সার্ভিসটি ভালো ফলাফল পেতে সাবধানে তৈরি করা প্রম্পট ব্যবহার করে:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### প্রতিক্রিয়া প্রসেসিং
AI প্রতিক্রিয়া বের করে এবং যাচাই করা হয়:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## পরবর্তী পদক্ষেপ

আরও উদাহরণের জন্য, দেখুন [Chapter 04: Practical samples](../README.md)

**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসাধ্য সঠিকতার জন্য চেষ্টা করি, তবে অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। এর মূল ভাষায় থাকা নথিটিকে প্রামাণিক উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য, পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদ ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যা হলে আমরা তার জন্য দায়বদ্ধ নই।