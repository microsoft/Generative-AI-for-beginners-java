<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:26:03+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "my"
}
-->
# သင်ကြားမှုအတွက် စတင်သူများအတွက် အိမ်မွေးတိရစ္ဆာန်ပုံပြင် ဖန်တီးရေးရာလမ်းညွှန်

## အကြောင်းအရာများ

- [လိုအပ်ချက်များ](../../../../04-PracticalSamples/petstory)
- [ပရောဂျက်ဖွဲ့စည်းမှုကို နားလည်ခြင်း](../../../../04-PracticalSamples/petstory)
- [အဓိကအစိတ်အပိုင်းများ ရှင်းပြချက်](../../../../04-PracticalSamples/petstory)
  - [1. အဓိကအပလီကေးရှင်း](../../../../04-PracticalSamples/petstory)
  - [2. Web Controller](../../../../04-PracticalSamples/petstory)
  - [3. Story Service](../../../../04-PracticalSamples/petstory)
  - [4. Web Templates](../../../../04-PracticalSamples/petstory)
  - [5. Configuration](../../../../04-PracticalSamples/petstory)
- [အပလီကေးရှင်းကို အလုပ်လုပ်စေခြင်း](../../../../04-PracticalSamples/petstory)
- [အားလုံးပေါင်းစပ်ပြီး ဘယ်လိုအလုပ်လုပ်သလဲ](../../../../04-PracticalSamples/petstory)
- [AI ပေါင်းစပ်မှုကို နားလည်ခြင်း](../../../../04-PracticalSamples/petstory)
- [နောက်ထပ်အဆင့်များ](../../../../04-PracticalSamples/petstory)

## လိုအပ်ချက်များ

စတင်မီ သင်မှာ အောက်ပါအရာများရှိရမည်ဖြစ်သည် -
- Java 21 သို့မဟုတ် အထက်ရှိထားခြင်း
- Maven ကို အခြေခံလိုအပ်ချက်များ စီမံရန်
- `models:read` scope ပါဝင်သော personal access token (PAT) ရှိသော GitHub အကောင့်
- Java, Spring Boot, နှင့် web development အခြေခံကို နားလည်ထားခြင်း

## ပရောဂျက်ဖွဲ့စည်းမှုကို နားလည်ခြင်း

အိမ်မွေးတိရစ္ဆာန်ပုံပြင်ပရောဂျက်တွင် အရေးကြီးသော ဖိုင်အချို့ပါဝင်သည် -

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

## အဓိကအစိတ်အပိုင်းများ ရှင်းပြချက်

### 1. အဓိကအပလီကေးရှင်း

**ဖိုင်:** `PetStoryApplication.java`

ဤသည်မှာ Spring Boot အပလီကေးရှင်း၏ စတင်ဝင်ပေါက်ဖြစ်သည် -

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**ဤအရာက ဘာလုပ်သလဲ:**
- `@SpringBootApplication` annotation သည် auto-configuration နှင့် component scanning ကို ဖွင့်စင်စေသည်
- Port 8080 တွင် embedded web server (Tomcat) ကို စတင်စေသည်
- လိုအပ်သော Spring beans နှင့် services အားလုံးကို အလိုအလျောက် ဖန်တီးပေးသည်

### 2. Web Controller

**ဖိုင်:** `PetController.java`

ဤသည်မှာ web request များနှင့် အသုံးပြုသူအပြန်အလှန်များကို ကိုင်တွယ်ပေးသည် -

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

**အဓိကအင်္ဂါရပ်များ:**

1. **Route Handling**: `@GetMapping("/")` သည် upload form ကို ပြသပြီး၊ `@PostMapping("/generate-story")` သည် တင်သွင်းမှုများကို ကိုင်တွယ်သည်
2. **Input Validation**: ဖော်ပြချက်များအတွက် အလွတ်နှင့် အရှည်ကန့်သတ်များကို စစ်ဆေးသည်
3. **Security**: အသုံးပြုသူ၏ input ကို sanitize လုပ်ပြီး XSS တိုက်ခိုက်မှုများကို ကာကွယ်သည်
4. **Error Handling**: AI service မအောင်မြင်ပါက fallback stories များကို ပေးသည်
5. **Model Binding**: Spring ၏ `Model` ကို အသုံးပြု၍ HTML templates သို့ data ပေးပို့သည်

**Fallback System:**
Controller တွင် AI service မရရှိနိုင်ပါက အသုံးပြုရန် ကြိုတင်ရေးသားထားသော ပုံပြင် template များပါဝင်သည် -

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

**ဖိုင်:** `StoryService.java`

ဤ service သည် GitHub Models နှင့် ဆက်သွယ်ပြီး ပုံပြင်များ ဖန်တီးပေးသည် -

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

**အဓိကအစိတ်အပိုင်းများ:**

1. **OpenAI Client**: GitHub Models အတွက် အတည်ပြုထားသော OpenAI Java SDK ကို အသုံးပြုသည်
2. **System Prompt**: AI ကို မိသားစုနှင့်သင့်တော်သော အိမ်မွေးတိရစ္ဆာန်ပုံပြင်များရေးရန် behavior ကို သတ်မှတ်သည်
3. **User Prompt**: ဖော်ပြချက်အပေါ်မူတည်၍ AI ကို ဘာရေးရမည်ကို ပြောသည်
4. **Parameters**: ပုံပြင်အရှည်နှင့် ဖန်တီးမှုအဆင့်ကို ထိန်းချုပ်သည်
5. **Error Handling**: Controller မှ ဖမ်းယူပြီး ကိုင်တွယ်နိုင်သော အထွက်အမှားများကို ပေးသည်

### 4. Web Templates

**ဖိုင်:** `index.html` (Upload Form)

အသုံးပြုသူများ အိမ်မွေးတိရစ္ဆာန်များကို ဖော်ပြရန် အဓိကစာမျက်နှာ -

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

**ဖိုင်:** `result.html` (Story Display)

ဖန်တီးထားသော ပုံပြင်ကို ပြသသည် -

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

**Template အင်္ဂါရပ်များ:**

1. **Thymeleaf Integration**: `th:` attributes ကို အသုံးပြု၍ dynamic content ကို ထည့်သွင်းသည်
2. **Responsive Design**: CSS ဖြင့် mobile နှင့် desktop အတွက် အဆင်ပြေစေသည်
3. **Error Handling**: အသုံးပြုသူများကို validation error များ ပြသသည်
4. **Client-side Processing**: JavaScript ဖြင့် image analysis (Transformers.js ကို အသုံးပြု၍) ပြုလုပ်သည်

### 5. Configuration

**ဖိုင်:** `application.properties`

အပလီကေးရှင်းအတွက် configuration settings -

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

**Configuration ရှင်းပြချက်:**

1. **File Upload**: 10MB အထိ image များကို ခွင့်ပြုသည်
2. **Logging**: အလုပ်လုပ်စဉ်အတွင်း log ထုတ်လွှင့်မှုကို ထိန်းချုပ်သည်
3. **GitHub Models**: အသုံးပြုမည့် AI model နှင့် endpoint ကို သတ်မှတ်သည်
4. **Security**: အထွက်အမှားများကို ကိုင်တွယ်ပြီး အထူးအချက်အလက်များ မဖော်ပြစေရန်

## အပလီကေးရှင်းကို အလုပ်လုပ်စေခြင်း

### အဆင့် 1: GitHub Token ကို သတ်မှတ်ပါ

ပထမဦးစွာ GitHub token ကို environment variable အဖြစ် သတ်မှတ်ရမည် -

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

**ဤအရာလိုအပ်သောအကြောင်း:**
- GitHub Models သည် AI models များကို အသုံးပြုရန် authentication လိုအပ်သည်
- Environment variables ကို အသုံးပြုခြင်းဖြင့် အထူးအချက်အလက်များကို source code မှ ကာကွယ်ထားနိုင်သည်
- `models:read` scope သည် AI inference ကို အသုံးပြုခွင့်ပေးသည်

### အဆင့် 2: Build နှင့် Run

ပရောဂျက် directory သို့ သွားပါ:
```bash
cd 04-PracticalSamples/petstory
```

အပလီကေးရှင်းကို Build လုပ်ပါ:
```bash
mvn clean compile
```

Server ကို Start လုပ်ပါ:
```bash
mvn spring-boot:run
```

အပလီကေးရှင်းသည် `http://localhost:8080` တွင် စတင်ပါမည်။

### အဆင့် 3: အပလီကေးရှင်းကို စမ်းသပ်ပါ

1. **Browser** တွင် `http://localhost:8080` ကို ဖွင့်ပါ
2. **Text Area** တွင် သင့်အိမ်မွေးတိရစ္ဆာန်ကို ဖော်ပြပါ (ဥပမာ - "A playful golden retriever who loves to fetch")
3. **"Generate Story"** ကို နှိပ်ပြီး AI ဖန်တီးထားသော ပုံပြင်ကို ရယူပါ
4. **အခြားနည်းလမ်းအဖြစ်**, အိမ်မွေးတိရစ္ဆာန်ပုံတစ်ပုံကို upload လုပ်ပြီး ဖော်ပြချက်ကို အလိုအလျောက် ဖန်တီးပါ
5. **ဖန်တီးထားသော ပုံပြင်ကို** သင့် browser တွင် ကြည့်ရှုနိုင်ပါသည်

## အားလုံးပေါင်းစပ်ပြီး ဘယ်လိုအလုပ်လုပ်သလဲ

သင့်အိမ်မွေးတိရစ္ဆာန်ပုံပြင်ကို ဖန်တီးစဉ် အလုပ်လုပ်ပုံ -

1. **အသုံးပြုသူ Input**: သင့်အိမ်မွေးတိရစ္ဆာန်ကို web form တွင် ဖော်ပြပါ
2. **Form တင်သွင်းမှု**: Browser မှ POST request ကို `/generate-story` သို့ ပို့သည်
3. **Controller ကိုင်တွယ်မှု**: `PetController` သည် input ကို စစ်ဆေးပြီး sanitize လုပ်သည်
4. **AI Service Call**: `StoryService` သည် GitHub Models API သို့ request ပို့သည်
5. **ပုံပြင်ဖန်တီးမှု**: AI သည် ဖော်ပြချက်အပေါ်မူတည်၍ ဖန်တီးမှုကို ပြုလုပ်သည်
6. **Response ကိုင်တွယ်မှု**: Controller သည် ပုံပြင်ကို လက်ခံပြီး model သို့ ထည့်သည်
7. **Template Rendering**: Thymeleaf သည် `result.html` ကို ပုံပြင်နှင့်အတူ render လုပ်သည်
8. **ပြသမှု**: အသုံးပြုသူသည် ဖန်တီးထားသော ပုံပြင်ကို browser တွင် ကြည့်ရှုနိုင်သည်

**Error Handling Flow:**
AI service မအောင်မြင်ပါက -
1. Controller သည် အထွက်အမှားကို ဖမ်းယူသည်
2. ကြိုတင်ရေးသားထားသော template များကို အသုံးပြု၍ fallback story ဖန်တီးသည်
3. AI မရရှိနိုင်မှုအကြောင်းကို အသုံးပြုသူသို့ အသိပေးသည်
4. အသုံးပြုသူသည် ပုံပြင်တစ်ခုကို ရရှိနိုင်ပြီး အတွေ့အကြုံကောင်းရရှိစေသည်

## AI ပေါင်းစပ်မှုကို နားလည်ခြင်း

### GitHub Models API
ဤအပလီကေးရှင်းသည် GitHub Models ကို အသုံးပြုပြီး အမျိုးမျိုးသော AI models များကို အခမဲ့ရယူနိုင်သည် -

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
ဤ service သည် ရလဒ်ကောင်းများရရှိစေရန် သေချာစွာရေးသားထားသော prompts များကို အသုံးပြုသည် -

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Response Processing
AI response ကို ထုတ်ယူပြီး စစ်ဆေးသည် -

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## နောက်ထပ်အဆင့်များ

နောက်ထပ်ဥပမာများအတွက် [Chapter 04: Practical samples](../README.md) ကို ကြည့်ပါ

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို ၎င်း၏ မူလဘာသာစကားဖြင့် အာဏာရှိသောအရင်းအမြစ်အဖြစ် ရှုလေ့လာသင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူ့ဘာသာပြန်ပညာရှင်များမှ ပရော်ဖက်ရှင်နယ် ဘာသာပြန်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲအလွတ်များ သို့မဟုတ် အနားလွဲမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။