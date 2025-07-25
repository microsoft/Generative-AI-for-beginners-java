<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:47:41+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "fa"
}
-->
# آموزش تولید داستان حیوانات خانگی برای مبتدیان

## فهرست مطالب

- [پیش‌نیازها](../../../../04-PracticalSamples/petstory)
- [درک ساختار پروژه](../../../../04-PracticalSamples/petstory)
- [توضیح اجزای اصلی](../../../../04-PracticalSamples/petstory)
  - [1. برنامه اصلی](../../../../04-PracticalSamples/petstory)
  - [2. کنترل‌کننده وب](../../../../04-PracticalSamples/petstory)
  - [3. سرویس داستان](../../../../04-PracticalSamples/petstory)
  - [4. قالب‌های وب](../../../../04-PracticalSamples/petstory)
  - [5. تنظیمات](../../../../04-PracticalSamples/petstory)
- [اجرای برنامه](../../../../04-PracticalSamples/petstory)
- [چگونگی همکاری اجزا با یکدیگر](../../../../04-PracticalSamples/petstory)
- [درک یکپارچه‌سازی هوش مصنوعی](../../../../04-PracticalSamples/petstory)
- [گام‌های بعدی](../../../../04-PracticalSamples/petstory)

## پیش‌نیازها

قبل از شروع، مطمئن شوید که موارد زیر را دارید:
- نصب Java نسخه 21 یا بالاتر
- Maven برای مدیریت وابستگی‌ها
- حساب GitHub با یک توکن دسترسی شخصی (PAT) با دسترسی `models:read`
- آشنایی اولیه با Java، Spring Boot و توسعه وب

## درک ساختار پروژه

پروژه داستان حیوانات خانگی شامل چندین فایل مهم است:

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

## توضیح اجزای اصلی

### 1. برنامه اصلی

**فایل:** `PetStoryApplication.java`

این نقطه ورود برای برنامه Spring Boot ما است:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**کارهایی که انجام می‌دهد:**
- استفاده از annotation `@SpringBootApplication` برای فعال‌سازی پیکربندی خودکار و اسکن اجزا
- راه‌اندازی یک سرور وب داخلی (Tomcat) روی پورت 8080
- ایجاد خودکار تمام Bean‌ها و سرویس‌های مورد نیاز Spring

### 2. کنترل‌کننده وب

**فایل:** `PetController.java`

این بخش مسئول مدیریت درخواست‌های وب و تعاملات کاربر است:

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

**ویژگی‌های کلیدی:**

1. **مدیریت مسیرها**: `@GetMapping("/")` فرم آپلود را نمایش می‌دهد، `@PostMapping("/generate-story")` ارسال‌ها را پردازش می‌کند
2. **اعتبارسنجی ورودی**: بررسی توضیحات خالی و محدودیت‌های طول
3. **امنیت**: پاکسازی ورودی‌های کاربر برای جلوگیری از حملات XSS
4. **مدیریت خطا**: ارائه داستان‌های جایگزین در صورت شکست سرویس هوش مصنوعی
5. **مدل‌سازی داده‌ها**: انتقال داده‌ها به قالب‌های HTML با استفاده از `Model` در Spring

**سیستم جایگزین:**
کنترل‌کننده شامل قالب‌های داستان از پیش نوشته شده است که در صورت عدم دسترسی به سرویس هوش مصنوعی استفاده می‌شوند:

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

### 3. سرویس داستان

**فایل:** `StoryService.java`

این سرویس با مدل‌های GitHub ارتباط برقرار می‌کند تا داستان‌ها را تولید کند:

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

**اجزای کلیدی:**

1. **کلاینت OpenAI**: استفاده از SDK رسمی OpenAI برای مدل‌های GitHub
2. **سیستم Prompt**: تنظیم رفتار هوش مصنوعی برای نوشتن داستان‌های خانوادگی و دوستانه
3. **Prompt کاربر**: مشخص کردن دقیق داستانی که باید بر اساس توضیحات نوشته شود
4. **پارامترها**: کنترل طول داستان و سطح خلاقیت
5. **مدیریت خطا**: پرتاب استثناهایی که توسط کنترل‌کننده گرفته و مدیریت می‌شوند

### 4. قالب‌های وب

**فایل:** `index.html` (فرم آپلود)

صفحه اصلی که کاربران حیوانات خانگی خود را توصیف می‌کنند:

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

**فایل:** `result.html` (نمایش داستان)

داستان تولید شده را نمایش می‌دهد:

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

**ویژگی‌های قالب:**

1. **یکپارچه‌سازی Thymeleaf**: استفاده از ویژگی‌های `th:` برای محتوای پویا
2. **طراحی واکنش‌گرا**: استایل CSS برای موبایل و دسکتاپ
3. **مدیریت خطا**: نمایش خطاهای اعتبارسنجی به کاربران
4. **پردازش سمت کاربر**: استفاده از JavaScript برای تحلیل تصویر (با Transformers.js)

### 5. تنظیمات

**فایل:** `application.properties`

تنظیمات پیکربندی برای برنامه:

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

**توضیح تنظیمات:**

1. **آپلود فایل**: اجازه آپلود تصاویر تا حجم 10MB
2. **ثبت وقایع**: کنترل اطلاعاتی که در طول اجرا ثبت می‌شوند
3. **مدل‌های GitHub**: مشخص کردن مدل هوش مصنوعی و نقطه پایانی مورد استفاده
4. **امنیت**: پیکربندی مدیریت خطا برای جلوگیری از افشای اطلاعات حساس

## اجرای برنامه

### مرحله 1: تنظیم توکن GitHub

ابتدا باید توکن GitHub خود را به عنوان یک متغیر محیطی تنظیم کنید:

**ویندوز (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**ویندوز (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**لینوکس/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

**چرا این مورد لازم است:**
- مدل‌های GitHub برای دسترسی به مدل‌های هوش مصنوعی نیاز به احراز هویت دارند
- استفاده از متغیرهای محیطی باعث می‌شود توکن‌های حساس در کد منبع ذخیره نشوند
- دسترسی `models:read` امکان استفاده از هوش مصنوعی را فراهم می‌کند

### مرحله 2: ساخت و اجرا

به دایرکتوری پروژه بروید:
```bash
cd 04-PracticalSamples/petstory
```

برنامه را بسازید:
```bash
mvn clean compile
```

سرور را راه‌اندازی کنید:
```bash
mvn spring-boot:run
```

برنامه روی `http://localhost:8080` اجرا خواهد شد.

### مرحله 3: تست برنامه

1. **باز کردن** `http://localhost:8080` در مرورگر
2. **توصیف** حیوان خانگی خود در قسمت متن (مثلاً "یک سگ بازیگوش طلایی که عاشق بازی کردن است")
3. **کلیک کردن** روی "تولید داستان" برای دریافت داستان تولید شده توسط هوش مصنوعی
4. **یا** آپلود تصویر حیوان خانگی برای تولید خودکار توضیحات
5. **مشاهده** داستان خلاقانه بر اساس توضیحات حیوان خانگی شما

## چگونگی همکاری اجزا با یکدیگر

در اینجا جریان کامل زمانی که یک داستان حیوان خانگی تولید می‌کنید آمده است:

1. **ورودی کاربر**: شما حیوان خانگی خود را در فرم وب توصیف می‌کنید
2. **ارسال فرم**: مرورگر درخواست POST به `/generate-story` ارسال می‌کند
3. **پردازش کنترل‌کننده**: `PetController` ورودی را اعتبارسنجی و پاکسازی می‌کند
4. **تماس سرویس هوش مصنوعی**: `StoryService` درخواست را به API مدل‌های GitHub ارسال می‌کند
5. **تولید داستان**: هوش مصنوعی داستانی خلاقانه بر اساس توضیحات تولید می‌کند
6. **مدیریت پاسخ**: کنترل‌کننده داستان را دریافت کرده و به مدل اضافه می‌کند
7. **رندر قالب**: Thymeleaf قالب `result.html` را با داستان رندر می‌کند
8. **نمایش**: کاربر داستان تولید شده را در مرورگر خود مشاهده می‌کند

**جریان مدیریت خطا:**
اگر سرویس هوش مصنوعی شکست بخورد:
1. کنترل‌کننده استثنا را می‌گیرد
2. داستان جایگزین با استفاده از قالب‌های از پیش نوشته شده تولید می‌کند
3. داستان جایگزین با یادداشتی درباره عدم دسترسی هوش مصنوعی نمایش داده می‌شود
4. کاربر همچنان داستانی دریافت می‌کند، که تجربه کاربری خوبی را تضمین می‌کند

## درک یکپارچه‌سازی هوش مصنوعی

### API مدل‌های GitHub
برنامه از مدل‌های GitHub استفاده می‌کند که دسترسی رایگان به مدل‌های مختلف هوش مصنوعی را فراهم می‌کند:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### مهندسی Prompt
سرویس از Prompt‌های دقیق طراحی شده برای دریافت نتایج خوب استفاده می‌کند:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### پردازش پاسخ
پاسخ هوش مصنوعی استخراج و اعتبارسنجی می‌شود:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## گام‌های بعدی

برای مثال‌های بیشتر، به [فصل 04: نمونه‌های عملی](../README.md) مراجعه کنید.

**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما تلاش می‌کنیم دقت را حفظ کنیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌ها باشند. سند اصلی به زبان اصلی آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حساس، توصیه می‌شود از ترجمه حرفه‌ای انسانی استفاده کنید. ما مسئولیتی در قبال سوء تفاهم‌ها یا تفسیرهای نادرست ناشی از استفاده از این ترجمه نداریم.