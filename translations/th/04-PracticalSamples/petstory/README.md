<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:36:46+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "th"
}
-->
# คู่มือการใช้งาน Pet Story Generator สำหรับผู้เริ่มต้น

## สารบัญ

- [ข้อกำหนดเบื้องต้น](../../../../04-PracticalSamples/petstory)
- [ทำความเข้าใจกับโครงสร้างโปรเจกต์](../../../../04-PracticalSamples/petstory)
- [อธิบายส่วนประกอบหลัก](../../../../04-PracticalSamples/petstory)
  - [1. แอปพลิเคชันหลัก](../../../../04-PracticalSamples/petstory)
  - [2. ตัวควบคุมเว็บ](../../../../04-PracticalSamples/petstory)
  - [3. บริการสร้างเรื่องราว](../../../../04-PracticalSamples/petstory)
  - [4. เทมเพลตเว็บ](../../../../04-PracticalSamples/petstory)
  - [5. การตั้งค่า](../../../../04-PracticalSamples/petstory)
- [การรันแอปพลิเคชัน](../../../../04-PracticalSamples/petstory)
- [การทำงานร่วมกันของระบบ](../../../../04-PracticalSamples/petstory)
- [ทำความเข้าใจกับการผสาน AI](../../../../04-PracticalSamples/petstory)
- [ขั้นตอนถัดไป](../../../../04-PracticalSamples/petstory)

## ข้อกำหนดเบื้องต้น

ก่อนเริ่มต้น โปรดตรวจสอบว่าคุณมี:
- ติดตั้ง Java 21 หรือเวอร์ชันที่สูงกว่า
- Maven สำหรับจัดการ dependencies
- บัญชี GitHub พร้อม personal access token (PAT) ที่มีสิทธิ์ `models:read`
- ความเข้าใจพื้นฐานเกี่ยวกับ Java, Spring Boot และการพัฒนาเว็บ

## ทำความเข้าใจกับโครงสร้างโปรเจกต์

โปรเจกต์ Pet Story มีไฟล์สำคัญดังนี้:

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

## อธิบายส่วนประกอบหลัก

### 1. แอปพลิเคชันหลัก

**ไฟล์:** `PetStoryApplication.java`

นี่คือจุดเริ่มต้นของแอปพลิเคชัน Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**สิ่งที่ไฟล์นี้ทำ:**
- คำสั่ง `@SpringBootApplication` เปิดใช้งานการตั้งค่าอัตโนมัติและการสแกนคอมโพเนนต์
- เริ่มต้นเว็บเซิร์ฟเวอร์ในตัว (Tomcat) บนพอร์ต 8080
- สร้าง Spring beans และบริการที่จำเป็นทั้งหมดโดยอัตโนมัติ

### 2. ตัวควบคุมเว็บ

**ไฟล์:** `PetController.java`

จัดการคำขอเว็บและการโต้ตอบกับผู้ใช้:

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

**คุณสมบัติสำคัญ:**

1. **การจัดการเส้นทาง:** `@GetMapping("/")` แสดงฟอร์มอัปโหลด, `@PostMapping("/generate-story")` ประมวลผลข้อมูลที่ส่งมา
2. **การตรวจสอบข้อมูล:** ตรวจสอบคำอธิบายที่ว่างเปล่าและข้อจำกัดความยาว
3. **ความปลอดภัย:** ทำความสะอาดข้อมูลผู้ใช้เพื่อป้องกันการโจมตี XSS
4. **การจัดการข้อผิดพลาด:** มีเรื่องราวสำรองเมื่อบริการ AI ล้มเหลว
5. **การผูกโมเดล:** ส่งข้อมูลไปยังเทมเพลต HTML โดยใช้ `Model` ของ Spring

**ระบบสำรอง:**
ตัวควบคุมมีเทมเพลตเรื่องราวที่เขียนไว้ล่วงหน้า ซึ่งจะใช้เมื่อบริการ AI ไม่พร้อมใช้งาน:

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

### 3. บริการสร้างเรื่องราว

**ไฟล์:** `StoryService.java`

บริการนี้สื่อสารกับ GitHub Models เพื่อสร้างเรื่องราว:

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

**ส่วนประกอบสำคัญ:**

1. **OpenAI Client:** ใช้ OpenAI Java SDK ที่กำหนดค่าไว้สำหรับ GitHub Models
2. **System Prompt:** กำหนดพฤติกรรมของ AI ให้เขียนเรื่องราวเกี่ยวกับสัตว์เลี้ยงที่เหมาะสำหรับครอบครัว
3. **User Prompt:** บอก AI ว่าต้องเขียนเรื่องราวแบบใดตามคำอธิบาย
4. **พารามิเตอร์:** ควบคุมความยาวและระดับความคิดสร้างสรรค์ของเรื่องราว
5. **การจัดการข้อผิดพลาด:** โยนข้อยกเว้นที่ตัวควบคุมจะจับและจัดการ

### 4. เทมเพลตเว็บ

**ไฟล์:** `index.html` (ฟอร์มอัปโหลด)

หน้าแรกที่ผู้ใช้สามารถอธิบายสัตว์เลี้ยงของพวกเขา:

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

**ไฟล์:** `result.html` (แสดงเรื่องราว)

แสดงเรื่องราวที่สร้างขึ้น:

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

**คุณสมบัติของเทมเพลต:**

1. **การผสาน Thymeleaf:** ใช้ `th:` สำหรับเนื้อหาแบบไดนามิก
2. **การออกแบบที่ตอบสนอง:** การจัดแต่ง CSS สำหรับมือถือและเดสก์ท็อป
3. **การจัดการข้อผิดพลาด:** แสดงข้อผิดพลาดการตรวจสอบให้ผู้ใช้เห็น
4. **การประมวลผลฝั่งไคลเอนต์:** ใช้ JavaScript สำหรับการวิเคราะห์ภาพ (ด้วย Transformers.js)

### 5. การตั้งค่า

**ไฟล์:** `application.properties`

การตั้งค่าคอนฟิกสำหรับแอปพลิเคชัน:

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

**คำอธิบายการตั้งค่า:**

1. **การอัปโหลดไฟล์:** อนุญาตให้ส่งภาพขนาดสูงสุด 10MB
2. **การบันทึก:** ควบคุมข้อมูลที่บันทึกระหว่างการทำงาน
3. **GitHub Models:** ระบุโมเดล AI และ endpoint ที่จะใช้
4. **ความปลอดภัย:** การตั้งค่าการจัดการข้อผิดพลาดเพื่อหลีกเลี่ยงการเปิดเผยข้อมูลที่ละเอียดอ่อน

## การรันแอปพลิเคชัน

### ขั้นตอนที่ 1: ตั้งค่า GitHub Token

ก่อนอื่น คุณต้องตั้งค่า GitHub token เป็นตัวแปรสภาพแวดล้อม:

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

**เหตุผลที่ต้องทำ:**
- GitHub Models ต้องการการยืนยันตัวตนเพื่อเข้าถึงโมเดล AI
- การใช้ตัวแปรสภาพแวดล้อมช่วยป้องกันไม่ให้ token ที่ละเอียดอ่อนอยู่ในซอร์สโค้ด
- สิทธิ์ `models:read` ให้สิทธิ์เข้าถึงการใช้งาน AI inference

### ขั้นตอนที่ 2: สร้างและรัน

ไปที่ไดเรกทอรีโปรเจกต์:
```bash
cd 04-PracticalSamples/petstory
```

สร้างแอปพลิเคชัน:
```bash
mvn clean compile
```

เริ่มเซิร์ฟเวอร์:
```bash
mvn spring-boot:run
```

แอปพลิเคชันจะเริ่มต้นที่ `http://localhost:8080`

### ขั้นตอนที่ 3: ทดสอบแอปพลิเคชัน

1. **เปิด** `http://localhost:8080` ในเบราว์เซอร์ของคุณ
2. **อธิบาย** สัตว์เลี้ยงของคุณในช่องข้อความ (เช่น "โกลเด้นรีทรีฟเวอร์ที่ชอบเล่นโยนของ")
3. **คลิก** "Generate Story" เพื่อรับเรื่องราวที่สร้างโดย AI
4. **หรือ** อัปโหลดภาพสัตว์เลี้ยงเพื่อสร้างคำอธิบายอัตโนมัติ
5. **ดู** เรื่องราวสร้างสรรค์ที่อิงจากคำอธิบายของสัตว์เลี้ยงของคุณ

## การทำงานร่วมกันของระบบ

นี่คือกระบวนการทั้งหมดเมื่อคุณสร้างเรื่องราวสัตว์เลี้ยง:

1. **ข้อมูลจากผู้ใช้:** คุณอธิบายสัตว์เลี้ยงของคุณในฟอร์มเว็บ
2. **การส่งฟอร์ม:** เบราว์เซอร์ส่งคำขอ POST ไปยัง `/generate-story`
3. **การประมวลผลของตัวควบคุม:** `PetController` ตรวจสอบและทำความสะอาดข้อมูล
4. **การเรียกบริการ AI:** `StoryService` ส่งคำขอไปยัง GitHub Models API
5. **การสร้างเรื่องราว:** AI สร้างเรื่องราวสร้างสรรค์ตามคำอธิบาย
6. **การจัดการคำตอบ:** ตัวควบคุมรับเรื่องราวและเพิ่มลงในโมเดล
7. **การเรนเดอร์เทมเพลต:** Thymeleaf เรนเดอร์ `result.html` พร้อมเรื่องราว
8. **การแสดงผล:** ผู้ใช้เห็นเรื่องราวที่สร้างขึ้นในเบราว์เซอร์

**กระบวนการจัดการข้อผิดพลาด:**
หากบริการ AI ล้มเหลว:
1. ตัวควบคุมจับข้อยกเว้น
2. สร้างเรื่องราวสำรองโดยใช้เทมเพลตที่เขียนไว้ล่วงหน้า
3. แสดงเรื่องราวสำรองพร้อมข้อความแจ้งเกี่ยวกับการไม่พร้อมใช้งานของ AI
4. ผู้ใช้ยังคงได้รับเรื่องราว เพื่อประสบการณ์การใช้งานที่ดี

## ทำความเข้าใจกับการผสาน AI

### GitHub Models API
แอปพลิเคชันใช้ GitHub Models ซึ่งให้การเข้าถึงโมเดล AI ต่างๆ ฟรี:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### การออกแบบ Prompt
บริการใช้ prompt ที่ออกแบบมาอย่างดีเพื่อให้ได้ผลลัพธ์ที่ดี:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### การประมวลผลคำตอบ
คำตอบจาก AI จะถูกดึงและตรวจสอบ:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## ขั้นตอนถัดไป

สำหรับตัวอย่างเพิ่มเติม ดู [Chapter 04: Practical samples](../README.md)

**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้การแปลมีความถูกต้องมากที่สุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลภาษามืออาชีพ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดซึ่งเกิดจากการใช้การแปลนี้