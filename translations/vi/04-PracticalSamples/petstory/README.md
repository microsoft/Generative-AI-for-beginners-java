<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:53:16+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "vi"
}
-->
# Hướng Dẫn Sử Dụng Pet Story Generator Dành Cho Người Mới Bắt Đầu

## Mục Lục

- [Yêu Cầu Trước Khi Bắt Đầu](../../../../04-PracticalSamples/petstory)
- [Hiểu Cấu Trúc Dự Án](../../../../04-PracticalSamples/petstory)
- [Giải Thích Các Thành Phần Chính](../../../../04-PracticalSamples/petstory)
  - [1. Ứng Dụng Chính](../../../../04-PracticalSamples/petstory)
  - [2. Bộ Điều Khiển Web](../../../../04-PracticalSamples/petstory)
  - [3. Dịch Vụ Tạo Câu Chuyện](../../../../04-PracticalSamples/petstory)
  - [4. Mẫu Giao Diện Web](../../../../04-PracticalSamples/petstory)
  - [5. Cấu Hình](../../../../04-PracticalSamples/petstory)
- [Chạy Ứng Dụng](../../../../04-PracticalSamples/petstory)
- [Cách Các Thành Phần Hoạt Động Cùng Nhau](../../../../04-PracticalSamples/petstory)
- [Hiểu Tích Hợp AI](../../../../04-PracticalSamples/petstory)
- [Bước Tiếp Theo](../../../../04-PracticalSamples/petstory)

## Yêu Cầu Trước Khi Bắt Đầu

Trước khi bắt đầu, hãy đảm bảo bạn đã có:
- Java 21 hoặc phiên bản cao hơn
- Maven để quản lý phụ thuộc
- Tài khoản GitHub với token truy cập cá nhân (PAT) có quyền `models:read`
- Hiểu biết cơ bản về Java, Spring Boot và phát triển web

## Hiểu Cấu Trúc Dự Án

Dự án Pet Story bao gồm một số tệp quan trọng:

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

## Giải Thích Các Thành Phần Chính

### 1. Ứng Dụng Chính

**Tệp:** `PetStoryApplication.java`

Đây là điểm khởi đầu cho ứng dụng Spring Boot của chúng ta:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Chức năng:**
- Annotation `@SpringBootApplication` kích hoạt tự động cấu hình và quét thành phần
- Khởi động máy chủ web nhúng (Tomcat) trên cổng 8080
- Tự động tạo tất cả các bean và dịch vụ cần thiết của Spring

### 2. Bộ Điều Khiển Web

**Tệp:** `PetController.java`

Xử lý tất cả các yêu cầu web và tương tác người dùng:

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

**Các tính năng chính:**

1. **Xử Lý Đường Dẫn**: `@GetMapping("/")` hiển thị biểu mẫu tải lên, `@PostMapping("/generate-story")` xử lý các yêu cầu gửi
2. **Xác Thực Đầu Vào**: Kiểm tra mô tả trống và giới hạn độ dài
3. **Bảo Mật**: Làm sạch đầu vào của người dùng để ngăn chặn các cuộc tấn công XSS
4. **Xử Lý Lỗi**: Cung cấp các câu chuyện dự phòng khi dịch vụ AI không hoạt động
5. **Liên Kết Mô Hình**: Truyền dữ liệu đến các mẫu HTML bằng cách sử dụng `Model` của Spring

**Hệ Thống Dự Phòng:**
Bộ điều khiển bao gồm các mẫu câu chuyện được viết sẵn được sử dụng khi dịch vụ AI không khả dụng:

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

### 3. Dịch Vụ Tạo Câu Chuyện

**Tệp:** `StoryService.java`

Dịch vụ này giao tiếp với GitHub Models để tạo câu chuyện:

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

**Các thành phần chính:**

1. **OpenAI Client**: Sử dụng SDK Java chính thức của OpenAI được cấu hình cho GitHub Models
2. **System Prompt**: Đặt hành vi của AI để viết các câu chuyện thân thiện với gia đình
3. **User Prompt**: Hướng dẫn AI viết câu chuyện dựa trên mô tả
4. **Tham Số**: Kiểm soát độ dài và mức độ sáng tạo của câu chuyện
5. **Xử Lý Lỗi**: Ném ra các ngoại lệ mà bộ điều khiển sẽ bắt và xử lý

### 4. Mẫu Giao Diện Web

**Tệp:** `index.html` (Biểu Mẫu Tải Lên)

Trang chính nơi người dùng mô tả thú cưng của họ:

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

**Tệp:** `result.html` (Hiển Thị Câu Chuyện)

Hiển thị câu chuyện được tạo:

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

**Tính năng của mẫu:**

1. **Tích Hợp Thymeleaf**: Sử dụng các thuộc tính `th:` để hiển thị nội dung động
2. **Thiết Kế Đáp Ứng**: CSS được tối ưu hóa cho cả thiết bị di động và máy tính để bàn
3. **Xử Lý Lỗi**: Hiển thị lỗi xác thực cho người dùng
4. **Xử Lý Phía Khách Hàng**: JavaScript phân tích hình ảnh (sử dụng Transformers.js)

### 5. Cấu Hình

**Tệp:** `application.properties`

Cài đặt cấu hình cho ứng dụng:

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

**Giải thích cấu hình:**

1. **Tải Lên Tệp**: Cho phép tải lên hình ảnh tối đa 10MB
2. **Ghi Nhật Ký**: Kiểm soát thông tin được ghi lại trong quá trình thực thi
3. **GitHub Models**: Chỉ định mô hình AI và endpoint được sử dụng
4. **Bảo Mật**: Cấu hình xử lý lỗi để tránh lộ thông tin nhạy cảm

## Chạy Ứng Dụng

### Bước 1: Đặt Token GitHub

Đầu tiên, bạn cần đặt token GitHub của mình làm biến môi trường:

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

**Tại sao cần điều này:**
- GitHub Models yêu cầu xác thực để truy cập các mô hình AI
- Sử dụng biến môi trường giúp giữ token nhạy cảm ngoài mã nguồn
- Quyền `models:read` cung cấp quyền truy cập vào AI inference

### Bước 2: Build và Chạy

Đi đến thư mục dự án:
```bash
cd 04-PracticalSamples/petstory
```

Build ứng dụng:
```bash
mvn clean compile
```

Khởi động máy chủ:
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại `http://localhost:8080`.

### Bước 3: Kiểm Tra Ứng Dụng

1. **Mở** `http://localhost:8080` trong trình duyệt
2. **Mô Tả** thú cưng của bạn trong ô văn bản (ví dụ: "Một chú chó golden retriever vui nhộn thích chơi ném bóng")
3. **Nhấn** "Generate Story" để nhận câu chuyện được tạo bởi AI
4. **Hoặc**, tải lên hình ảnh thú cưng để tự động tạo mô tả
5. **Xem** câu chuyện sáng tạo dựa trên mô tả thú cưng của bạn

## Cách Các Thành Phần Hoạt Động Cùng Nhau

Dưới đây là luồng hoạt động hoàn chỉnh khi bạn tạo một câu chuyện về thú cưng:

1. **Người Dùng Nhập Liệu**: Bạn mô tả thú cưng của mình trên biểu mẫu web
2. **Gửi Biểu Mẫu**: Trình duyệt gửi yêu cầu POST đến `/generate-story`
3. **Xử Lý Bởi Bộ Điều Khiển**: `PetController` xác thực và làm sạch đầu vào
4. **Gọi Dịch Vụ AI**: `StoryService` gửi yêu cầu đến API GitHub Models
5. **Tạo Câu Chuyện**: AI tạo một câu chuyện sáng tạo dựa trên mô tả
6. **Xử Lý Phản Hồi**: Bộ điều khiển nhận câu chuyện và thêm vào mô hình
7. **Kết Xuất Mẫu**: Thymeleaf kết xuất `result.html` với câu chuyện
8. **Hiển Thị**: Người dùng xem câu chuyện được tạo trong trình duyệt

**Luồng Xử Lý Lỗi:**
Nếu dịch vụ AI không hoạt động:
1. Bộ điều khiển bắt ngoại lệ
2. Tạo câu chuyện dự phòng bằng các mẫu viết sẵn
3. Hiển thị câu chuyện dự phòng với thông báo về việc AI không khả dụng
4. Người dùng vẫn nhận được câu chuyện, đảm bảo trải nghiệm tốt

## Hiểu Tích Hợp AI

### API GitHub Models
Ứng dụng sử dụng GitHub Models, cung cấp quyền truy cập miễn phí vào các mô hình AI khác nhau:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Kỹ Thuật Prompt
Dịch vụ sử dụng các prompt được thiết kế cẩn thận để đạt kết quả tốt:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Xử Lý Phản Hồi
Phản hồi từ AI được trích xuất và xác thực:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Bước Tiếp Theo

Để xem thêm ví dụ, hãy tham khảo [Chương 04: Các mẫu thực tế](../README.md)

**Tuyên bố miễn trừ trách nhiệm**:  
Tài liệu này đã được dịch bằng dịch vụ dịch thuật AI [Co-op Translator](https://github.com/Azure/co-op-translator). Mặc dù chúng tôi cố gắng đảm bảo độ chính xác, xin lưu ý rằng các bản dịch tự động có thể chứa lỗi hoặc không chính xác. Tài liệu gốc bằng ngôn ngữ bản địa nên được coi là nguồn thông tin chính thức. Đối với các thông tin quan trọng, khuyến nghị sử dụng dịch vụ dịch thuật chuyên nghiệp bởi con người. Chúng tôi không chịu trách nhiệm cho bất kỳ sự hiểu lầm hoặc diễn giải sai nào phát sinh từ việc sử dụng bản dịch này.