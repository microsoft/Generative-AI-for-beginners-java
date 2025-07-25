<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:44:58+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ar"
}
-->
# دليل إنشاء قصص الحيوانات الأليفة للمبتدئين

## جدول المحتويات

- [المتطلبات الأساسية](../../../../04-PracticalSamples/petstory)
- [فهم هيكل المشروع](../../../../04-PracticalSamples/petstory)
- [شرح المكونات الأساسية](../../../../04-PracticalSamples/petstory)
  - [1. التطبيق الرئيسي](../../../../04-PracticalSamples/petstory)
  - [2. وحدة التحكم في الويب](../../../../04-PracticalSamples/petstory)
  - [3. خدمة القصص](../../../../04-PracticalSamples/petstory)
  - [4. قوالب الويب](../../../../04-PracticalSamples/petstory)
  - [5. الإعدادات](../../../../04-PracticalSamples/petstory)
- [تشغيل التطبيق](../../../../04-PracticalSamples/petstory)
- [كيف تعمل جميع الأجزاء معًا](../../../../04-PracticalSamples/petstory)
- [فهم تكامل الذكاء الاصطناعي](../../../../04-PracticalSamples/petstory)
- [الخطوات التالية](../../../../04-PracticalSamples/petstory)

## المتطلبات الأساسية

قبل البدء، تأكد من توفر ما يلي:
- تثبيت Java 21 أو إصدار أعلى
- Maven لإدارة التبعيات
- حساب GitHub مع رمز وصول شخصي (PAT) يحتوي على نطاق `models:read`
- فهم أساسي لـ Java وSpring Boot وتطوير الويب

## فهم هيكل المشروع

يحتوي مشروع قصص الحيوانات الأليفة على عدة ملفات مهمة:

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

## شرح المكونات الأساسية

### 1. التطبيق الرئيسي

**الملف:** `PetStoryApplication.java`

هذه هي نقطة البداية لتطبيق Spring Boot الخاص بنا:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**ما الذي يفعله هذا:**
- التعليمة `@SpringBootApplication` تُمكّن التهيئة التلقائية ومسح المكونات
- تشغيل خادم ويب مدمج (Tomcat) على المنفذ 8080
- إنشاء جميع الكائنات والخدمات اللازمة تلقائيًا

### 2. وحدة التحكم في الويب

**الملف:** `PetController.java`

تتعامل هذه الوحدة مع جميع طلبات الويب وتفاعلات المستخدم:

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

**الميزات الرئيسية:**

1. **معالجة المسارات**: `@GetMapping("/")` يعرض نموذج التحميل، و`@PostMapping("/generate-story")` يعالج الإرسالات
2. **التحقق من المدخلات**: التحقق من الوصف الفارغ وحدود الطول
3. **الأمان**: تنقية مدخلات المستخدم لمنع هجمات XSS
4. **معالجة الأخطاء**: توفير قصص بديلة عند فشل خدمة الذكاء الاصطناعي
5. **ربط النماذج**: تمرير البيانات إلى قوالب HTML باستخدام `Model` الخاص بـ Spring

**نظام الطوارئ:**
تتضمن وحدة التحكم قوالب قصص مكتوبة مسبقًا تُستخدم عند عدم توفر خدمة الذكاء الاصطناعي:

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

### 3. خدمة القصص

**الملف:** `StoryService.java`

تتواصل هذه الخدمة مع GitHub Models لإنشاء القصص:

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

**المكونات الرئيسية:**

1. **عميل OpenAI**: يستخدم حزمة OpenAI Java SDK الرسمية المهيأة لنماذج GitHub
2. **التوجيه النظامي**: يحدد سلوك الذكاء الاصطناعي لكتابة قصص صديقة للعائلة
3. **توجيه المستخدم**: يوضح للذكاء الاصطناعي القصة التي يجب كتابتها بناءً على الوصف
4. **المعلمات**: تتحكم في طول القصة ومستوى الإبداع
5. **معالجة الأخطاء**: يرمي استثناءات تتعامل معها وحدة التحكم

### 4. قوالب الويب

**الملف:** `index.html` (نموذج التحميل)

الصفحة الرئيسية حيث يصف المستخدمون حيواناتهم الأليفة:

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

**الملف:** `result.html` (عرض القصة)

يعرض القصة التي تم إنشاؤها:

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

**ميزات القالب:**

1. **تكامل Thymeleaf**: يستخدم سمات `th:` للمحتوى الديناميكي
2. **تصميم متجاوب**: تنسيق CSS للهواتف المحمولة وأجهزة سطح المكتب
3. **معالجة الأخطاء**: عرض أخطاء التحقق للمستخدمين
4. **المعالجة على جانب العميل**: JavaScript لتحليل الصور (باستخدام Transformers.js)

### 5. الإعدادات

**الملف:** `application.properties`

إعدادات التهيئة للتطبيق:

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

**شرح الإعدادات:**

1. **تحميل الملفات**: يسمح بالصور حتى 10 ميجابايت
2. **التسجيل**: يتحكم في المعلومات التي يتم تسجيلها أثناء التنفيذ
3. **نماذج GitHub**: يحدد النموذج ونقطة النهاية التي سيتم استخدامها
4. **الأمان**: إعدادات معالجة الأخطاء لتجنب كشف المعلومات الحساسة

## تشغيل التطبيق

### الخطوة 1: إعداد رمز GitHub الخاص بك

أولاً، تحتاج إلى إعداد رمز GitHub الخاص بك كمتغير بيئة:

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

**لماذا هذا ضروري:**
- تتطلب نماذج GitHub المصادقة للوصول إلى نماذج الذكاء الاصطناعي
- استخدام متغيرات البيئة يحافظ على الرموز الحساسة خارج الكود المصدري
- يوفر النطاق `models:read` الوصول إلى استدلال الذكاء الاصطناعي

### الخطوة 2: البناء والتشغيل

انتقل إلى دليل المشروع:
```bash
cd 04-PracticalSamples/petstory
```

قم ببناء التطبيق:
```bash
mvn clean compile
```

ابدأ الخادم:
```bash
mvn spring-boot:run
```

سيبدأ التطبيق على `http://localhost:8080`.

### الخطوة 3: اختبار التطبيق

1. **افتح** `http://localhost:8080` في متصفحك
2. **صف** حيوانك الأليف في مربع النص (مثل: "جرو ذهبي مرح يحب الجلب")
3. **انقر** على "إنشاء قصة" للحصول على قصة تم إنشاؤها بواسطة الذكاء الاصطناعي
4. **بدلاً من ذلك**، قم بتحميل صورة لحيوانك الأليف لإنشاء وصف تلقائيًا
5. **شاهد** القصة الإبداعية بناءً على وصف حيوانك الأليف

## كيف تعمل جميع الأجزاء معًا

إليك التدفق الكامل عند إنشاء قصة لحيوان أليف:

1. **مدخلات المستخدم**: تصف حيوانك الأليف في نموذج الويب
2. **إرسال النموذج**: يرسل المتصفح طلب POST إلى `/generate-story`
3. **معالجة وحدة التحكم**: يتحقق `PetController` من المدخلات وينقيها
4. **استدعاء خدمة الذكاء الاصطناعي**: ترسل `StoryService` طلبًا إلى واجهة GitHub Models API
5. **إنشاء القصة**: يقوم الذكاء الاصطناعي بإنشاء قصة إبداعية بناءً على الوصف
6. **معالجة الاستجابة**: تتلقى وحدة التحكم القصة وتضيفها إلى النموذج
7. **عرض القالب**: يقوم Thymeleaf بعرض `result.html` مع القصة
8. **العرض**: يرى المستخدم القصة التي تم إنشاؤها في متصفحه

**تدفق معالجة الأخطاء:**
إذا فشلت خدمة الذكاء الاصطناعي:
1. تلتقط وحدة التحكم الاستثناء
2. تنشئ قصة بديلة باستخدام القوالب المكتوبة مسبقًا
3. تعرض القصة البديلة مع ملاحظة حول عدم توفر الذكاء الاصطناعي
4. يحصل المستخدم على قصة، مما يضمن تجربة مستخدم جيدة

## فهم تكامل الذكاء الاصطناعي

### واجهة GitHub Models API
يستخدم التطبيق GitHub Models، التي توفر وصولاً مجانيًا إلى نماذج الذكاء الاصطناعي المختلفة:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### هندسة التوجيه
تستخدم الخدمة توجيهات مصممة بعناية للحصول على نتائج جيدة:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### معالجة الاستجابة
يتم استخراج استجابة الذكاء الاصطناعي والتحقق منها:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## الخطوات التالية

للحصول على المزيد من الأمثلة، راجع [الفصل 04: أمثلة عملية](../README.md)

**إخلاء المسؤولية**:  
تم ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو معلومات غير دقيقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي. للحصول على معلومات حاسمة، يُوصى بالاستعانة بترجمة بشرية احترافية. نحن غير مسؤولين عن أي سوء فهم أو تفسيرات خاطئة تنشأ عن استخدام هذه الترجمة.