<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:04:43+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ko"
}
-->
# 초보자를 위한 반려동물 이야기 생성기 튜토리얼

## 목차

- [사전 준비](../../../../04-PracticalSamples/petstory)
- [프로젝트 구조 이해하기](../../../../04-PracticalSamples/petstory)
- [핵심 구성 요소 설명](../../../../04-PracticalSamples/petstory)
  - [1. 메인 애플리케이션](../../../../04-PracticalSamples/petstory)
  - [2. 웹 컨트롤러](../../../../04-PracticalSamples/petstory)
  - [3. 이야기 서비스](../../../../04-PracticalSamples/petstory)
  - [4. 웹 템플릿](../../../../04-PracticalSamples/petstory)
  - [5. 설정](../../../../04-PracticalSamples/petstory)
- [애플리케이션 실행하기](../../../../04-PracticalSamples/petstory)
- [전체 작동 방식](../../../../04-PracticalSamples/petstory)
- [AI 통합 이해하기](../../../../04-PracticalSamples/petstory)
- [다음 단계](../../../../04-PracticalSamples/petstory)

## 사전 준비

시작하기 전에 다음을 준비하세요:
- Java 21 이상 설치
- 의존성 관리를 위한 Maven
- `models:read` 범위가 포함된 개인 액세스 토큰(PAT)을 가진 GitHub 계정
- Java, Spring Boot, 웹 개발에 대한 기본적인 이해

## 프로젝트 구조 이해하기

반려동물 이야기 프로젝트에는 몇 가지 중요한 파일이 포함되어 있습니다:

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

## 핵심 구성 요소 설명

### 1. 메인 애플리케이션

**파일:** `PetStoryApplication.java`

Spring Boot 애플리케이션의 진입점입니다:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**이 파일의 역할:**
- `@SpringBootApplication` 애노테이션은 자동 설정 및 컴포넌트 스캔을 활성화합니다
- 포트 8080에서 내장 웹 서버(Tomcat)를 시작합니다
- 필요한 모든 Spring 빈과 서비스를 자동으로 생성합니다

### 2. 웹 컨트롤러

**파일:** `PetController.java`

웹 요청과 사용자 상호작용을 처리합니다:

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

**주요 기능:**

1. **라우트 처리**: `@GetMapping("/")`은 업로드 폼을 표시하고, `@PostMapping("/generate-story")`는 제출된 데이터를 처리합니다
2. **입력 검증**: 빈 설명과 길이 제한을 확인합니다
3. **보안**: 사용자 입력을 정리하여 XSS 공격을 방지합니다
4. **오류 처리**: AI 서비스가 실패할 경우 대체 이야기를 제공합니다
5. **모델 바인딩**: Spring의 `Model`을 사용하여 데이터를 HTML 템플릿에 전달합니다

**대체 시스템:**
컨트롤러는 AI 서비스가 사용할 수 없을 때 사용할 사전 작성된 이야기 템플릿을 포함합니다:

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

### 3. 이야기 서비스

**파일:** `StoryService.java`

GitHub Models와 통신하여 이야기를 생성합니다:

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

**핵심 구성 요소:**

1. **OpenAI 클라이언트**: GitHub Models에 맞게 구성된 공식 OpenAI Java SDK를 사용합니다
2. **시스템 프롬프트**: 가족 친화적인 반려동물 이야기를 작성하도록 AI의 행동을 설정합니다
3. **사용자 프롬프트**: 설명에 따라 AI가 정확히 어떤 이야기를 작성해야 하는지 지시합니다
4. **파라미터**: 이야기 길이와 창의성 수준을 제어합니다
5. **오류 처리**: 컨트롤러가 잡아 처리할 예외를 발생시킵니다

### 4. 웹 템플릿

**파일:** `index.html` (업로드 폼)

사용자가 반려동물을 설명하는 메인 페이지입니다:

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

**파일:** `result.html` (이야기 표시)

생성된 이야기를 보여줍니다:

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

**템플릿 기능:**

1. **Thymeleaf 통합**: 동적 콘텐츠를 위한 `th:` 속성을 사용합니다
2. **반응형 디자인**: 모바일 및 데스크톱용 CSS 스타일링
3. **오류 처리**: 사용자에게 검증 오류를 표시합니다
4. **클라이언트 측 처리**: 이미지 분석을 위한 JavaScript 사용 (Transformers.js 활용)

### 5. 설정

**파일:** `application.properties`

애플리케이션 설정 파일입니다:

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

**설정 설명:**

1. **파일 업로드**: 최대 10MB의 이미지를 허용합니다
2. **로깅**: 실행 중 기록되는 정보를 제어합니다
3. **GitHub Models**: 사용할 AI 모델과 엔드포인트를 지정합니다
4. **보안**: 민감한 정보 노출을 방지하기 위한 오류 처리 설정

## 애플리케이션 실행하기

### 1단계: GitHub 토큰 설정

먼저 GitHub 토큰을 환경 변수로 설정해야 합니다:

**Windows (명령 프롬프트):**
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

**이 설정이 필요한 이유:**
- GitHub Models는 AI 모델에 접근하기 위해 인증이 필요합니다
- 환경 변수를 사용하면 민감한 토큰이 소스 코드에 포함되지 않습니다
- `models:read` 범위는 AI 추론에 접근할 수 있도록 합니다

### 2단계: 빌드 및 실행

프로젝트 디렉토리로 이동합니다:
```bash
cd 04-PracticalSamples/petstory
```

애플리케이션을 빌드합니다:
```bash
mvn clean compile
```

서버를 시작합니다:
```bash
mvn spring-boot:run
```

애플리케이션은 `http://localhost:8080`에서 시작됩니다.

### 3단계: 애플리케이션 테스트

1. **브라우저에서** `http://localhost:8080`을 엽니다
2. **반려동물 설명**을 텍스트 영역에 입력합니다 (예: "공을 가져오는 것을 좋아하는 활발한 골든 리트리버")
3. **"Generate Story" 클릭**하여 AI 생성 이야기를 확인합니다
4. **또는**, 반려동물 이미지를 업로드하여 자동으로 설명을 생성합니다
5. **창의적인 이야기**를 반려동물 설명에 기반하여 확인합니다

## 전체 작동 방식

반려동물 이야기를 생성할 때의 전체 흐름은 다음과 같습니다:

1. **사용자 입력**: 웹 폼에서 반려동물을 설명합니다
2. **폼 제출**: 브라우저가 `/generate-story`로 POST 요청을 보냅니다
3. **컨트롤러 처리**: `PetController`가 입력을 검증하고 정리합니다
4. **AI 서비스 호출**: `StoryService`가 GitHub Models API에 요청을 보냅니다
5. **이야기 생성**: AI가 설명에 기반하여 창의적인 이야기를 생성합니다
6. **응답 처리**: 컨트롤러가 이야기를 받아 모델에 추가합니다
7. **템플릿 렌더링**: Thymeleaf가 `result.html`을 이야기와 함께 렌더링합니다
8. **표시**: 사용자가 브라우저에서 생성된 이야기를 확인합니다

**오류 처리 흐름:**
AI 서비스가 실패하면:
1. 컨트롤러가 예외를 잡습니다
2. 사전 작성된 템플릿을 사용하여 대체 이야기를 생성합니다
3. AI 서비스 이용 불가에 대한 알림과 함께 대체 이야기를 표시합니다
4. 사용자는 여전히 이야기를 받을 수 있어 좋은 사용자 경험을 제공합니다

## AI 통합 이해하기

### GitHub Models API
애플리케이션은 GitHub Models를 사용하여 다양한 AI 모델에 무료로 접근합니다:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### 프롬프트 엔지니어링
서비스는 좋은 결과를 얻기 위해 신중하게 작성된 프롬프트를 사용합니다:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### 응답 처리
AI 응답은 추출되고 검증됩니다:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## 다음 단계

더 많은 예제를 보려면 [Chapter 04: Practical samples](../README.md)를 참조하세요.

**면책 조항**:  
이 문서는 AI 번역 서비스 [Co-op Translator](https://github.com/Azure/co-op-translator)를 사용하여 번역되었습니다. 정확성을 위해 최선을 다하고 있지만, 자동 번역에는 오류나 부정확성이 포함될 수 있습니다. 원본 문서의 원어 버전을 권위 있는 출처로 간주해야 합니다. 중요한 정보의 경우, 전문적인 인간 번역을 권장합니다. 이 번역 사용으로 인해 발생하는 오해나 잘못된 해석에 대해 책임을 지지 않습니다.