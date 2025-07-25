<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:53:25+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "zh"
}
-->
# 宠物故事生成器初学者教程

## 目录

- [前置条件](../../../../04-PracticalSamples/petstory)
- [理解项目结构](../../../../04-PracticalSamples/petstory)
- [核心组件解析](../../../../04-PracticalSamples/petstory)
  - [1. 主应用程序](../../../../04-PracticalSamples/petstory)
  - [2. Web控制器](../../../../04-PracticalSamples/petstory)
  - [3. 故事服务](../../../../04-PracticalSamples/petstory)
  - [4. Web模板](../../../../04-PracticalSamples/petstory)
  - [5. 配置](../../../../04-PracticalSamples/petstory)
- [运行应用程序](../../../../04-PracticalSamples/petstory)
- [整体工作流程](../../../../04-PracticalSamples/petstory)
- [理解AI集成](../../../../04-PracticalSamples/petstory)
- [下一步](../../../../04-PracticalSamples/petstory)

## 前置条件

开始之前，请确保您已准备好以下内容：
- 安装了Java 21或更高版本
- 用于依赖管理的Maven
- 一个拥有`models:read`权限的GitHub账户及个人访问令牌（PAT）
- 对Java、Spring Boot和Web开发有基本了解

## 理解项目结构

宠物故事项目包含几个重要文件：

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

## 核心组件解析

### 1. 主应用程序

**文件:** `PetStoryApplication.java`

这是我们Spring Boot应用程序的入口点：

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**功能说明:**
- `@SpringBootApplication`注解启用自动配置和组件扫描
- 在端口8080上启动嵌入式Web服务器（Tomcat）
- 自动创建所有必要的Spring Bean和服务

### 2. Web控制器

**文件:** `PetController.java`

此组件处理所有Web请求和用户交互：

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

**关键功能:**

1. **路由处理**: `@GetMapping("/")`显示上传表单，`@PostMapping("/generate-story")`处理提交
2. **输入验证**: 检查描述是否为空以及长度限制
3. **安全性**: 清理用户输入以防止XSS攻击
4. **错误处理**: 在AI服务失败时提供备用故事
5. **模型绑定**: 使用Spring的`Model`将数据传递给HTML模板

**备用系统:**
控制器包含预先编写的故事模板，当AI服务不可用时会使用这些模板：

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

### 3. 故事服务

**文件:** `StoryService.java`

此服务与GitHub Models通信以生成故事：

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

**关键组件:**

1. **OpenAI客户端**: 使用配置为GitHub Models的官方OpenAI Java SDK
2. **系统提示**: 设置AI的行为以编写适合家庭的宠物故事
3. **用户提示**: 根据描述告诉AI具体要写什么故事
4. **参数**: 控制故事长度和创意水平
5. **错误处理**: 抛出异常，由控制器捕获并处理

### 4. Web模板

**文件:** `index.html` (上传表单)

用户描述宠物的主页面：

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

**文件:** `result.html` (故事展示)

显示生成的故事：

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

**模板功能:**

1. **Thymeleaf集成**: 使用`th:`属性实现动态内容
2. **响应式设计**: CSS样式适配移动端和桌面端
3. **错误处理**: 向用户显示验证错误
4. **客户端处理**: 使用JavaScript进行图像分析（基于Transformers.js）

### 5. 配置

**文件:** `application.properties`

应用程序的配置设置：

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

**配置说明:**

1. **文件上传**: 允许上传最大10MB的图片
2. **日志记录**: 控制执行期间记录的信息
3. **GitHub Models**: 指定使用的AI模型和端点
4. **安全性**: 错误处理配置以避免暴露敏感信息

## 运行应用程序

### 第一步: 设置GitHub令牌

首先，您需要将GitHub令牌设置为环境变量：

**Windows (命令提示符):**
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

**为什么需要这样做:**
- GitHub Models需要身份验证才能访问AI模型
- 使用环境变量可以避免将敏感令牌暴露在源代码中
- `models:read`权限提供AI推理访问

### 第二步: 构建并运行

进入项目目录：
```bash
cd 04-PracticalSamples/petstory
```

构建应用程序：
```bash
mvn clean compile
```

启动服务器：
```bash
mvn spring-boot:run
```

应用程序将在`http://localhost:8080`上启动。

### 第三步: 测试应用程序

1. **打开**浏览器中的`http://localhost:8080`
2. **描述**您的宠物（例如：“一只喜欢捡球的活泼金毛”）
3. **点击**“生成故事”以获取AI生成的故事
4. **或者**上传宠物图片以自动生成描述
5. **查看**基于宠物描述生成的创意故事

## 整体工作流程

以下是生成宠物故事的完整流程：

1. **用户输入**: 您在Web表单中描述您的宠物
2. **表单提交**: 浏览器发送POST请求到`/generate-story`
3. **控制器处理**: `PetController`验证并清理输入
4. **AI服务调用**: `StoryService`向GitHub Models API发送请求
5. **故事生成**: AI根据描述生成创意故事
6. **响应处理**: 控制器接收故事并将其添加到模型中
7. **模板渲染**: Thymeleaf渲染`result.html`并显示故事
8. **展示**: 用户在浏览器中看到生成的故事

**错误处理流程:**
如果AI服务失败：
1. 控制器捕获异常
2. 使用预编写的模板生成备用故事
3. 显示关于AI不可用的提示及备用故事
4. 确保用户仍然能获得故事，提供良好的用户体验

## 理解AI集成

### GitHub Models API
应用程序使用GitHub Models，提供免费访问各种AI模型的功能：

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### 提示工程
服务使用精心设计的提示以获得良好的结果：

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### 响应处理
AI响应被提取并验证：

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## 下一步

有关更多示例，请参阅[第4章：实用样例](../README.md)

**免责声明**：  
本文档使用AI翻译服务 [Co-op Translator](https://github.com/Azure/co-op-translator) 进行翻译。尽管我们努力确保翻译的准确性，但请注意，自动翻译可能包含错误或不准确之处。应以原始语言的文档作为权威来源。对于关键信息，建议使用专业人工翻译。我们不对因使用此翻译而产生的任何误解或误读承担责任。