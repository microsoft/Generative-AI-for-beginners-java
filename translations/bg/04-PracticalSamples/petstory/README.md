<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:15:28+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "bg"
}
-->
# Ръководство за начинаещи: Генератор на истории за домашни любимци

## Съдържание

- [Предварителни изисквания](../../../../04-PracticalSamples/petstory)
- [Разбиране на структурата на проекта](../../../../04-PracticalSamples/petstory)
- [Обяснение на основните компоненти](../../../../04-PracticalSamples/petstory)
  - [1. Основно приложение](../../../../04-PracticalSamples/petstory)
  - [2. Уеб контролер](../../../../04-PracticalSamples/petstory)
  - [3. Услуга за истории](../../../../04-PracticalSamples/petstory)
  - [4. Уеб шаблони](../../../../04-PracticalSamples/petstory)
  - [5. Конфигурация](../../../../04-PracticalSamples/petstory)
- [Стартиране на приложението](../../../../04-PracticalSamples/petstory)
- [Как всичко работи заедно](../../../../04-PracticalSamples/petstory)
- [Разбиране на интеграцията с AI](../../../../04-PracticalSamples/petstory)
- [Следващи стъпки](../../../../04-PracticalSamples/petstory)

## Предварителни изисквания

Преди да започнете, уверете се, че разполагате със следното:
- Инсталирана Java 21 или по-нова версия
- Maven за управление на зависимости
- GitHub акаунт с персонален токен за достъп (PAT) с обхват `models:read`
- Основни познания по Java, Spring Boot и уеб разработка

## Разбиране на структурата на проекта

Проектът за истории за домашни любимци съдържа няколко важни файла:

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

## Обяснение на основните компоненти

### 1. Основно приложение

**Файл:** `PetStoryApplication.java`

Това е началната точка на нашето Spring Boot приложение:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Какво прави това:**
- Анотацията `@SpringBootApplication` активира автоматична конфигурация и сканиране на компоненти
- Стартира вграден уеб сървър (Tomcat) на порт 8080
- Автоматично създава всички необходими Spring bean-ове и услуги

### 2. Уеб контролер

**Файл:** `PetController.java`

Този компонент обработва всички уеб заявки и взаимодействия с потребителя:

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

**Основни функции:**

1. **Обработка на маршрути:** `@GetMapping("/")` показва формата за качване, а `@PostMapping("/generate-story")` обработва подадените данни
2. **Валидиране на входа:** Проверява за празни описания и ограничения в дължината
3. **Сигурност:** Санитизира входните данни, за да предотврати XSS атаки
4. **Обработка на грешки:** Осигурява резервни истории, когато AI услугата не е налична
5. **Свързване на модели:** Предава данни към HTML шаблони, използвайки Spring `Model`

**Резервна система:**
Контролерът включва предварително написани шаблони за истории, които се използват, когато AI услугата не е налична:

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

### 3. Услуга за истории

**Файл:** `StoryService.java`

Тази услуга комуникира с GitHub Models за генериране на истории:

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

**Основни компоненти:**

1. **OpenAI клиент:** Използва официалния OpenAI Java SDK, конфигуриран за GitHub Models
2. **Системен промпт:** Определя поведението на AI за писане на семейно-приятелски истории за домашни любимци
3. **Потребителски промпт:** Указва на AI каква точно история да напише въз основа на описанието
4. **Параметри:** Контролира дължината на историята и нивото на креативност
5. **Обработка на грешки:** Хвърля изключения, които контролерът улавя и обработва

### 4. Уеб шаблони

**Файл:** `index.html` (Форма за качване)

Основната страница, където потребителите описват своите домашни любимци:

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

**Файл:** `result.html` (Показване на историята)

Показва генерираната история:

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

**Характеристики на шаблоните:**

1. **Интеграция с Thymeleaf:** Използва `th:` атрибути за динамично съдържание
2. **Респонсив дизайн:** CSS стилове за мобилни устройства и десктоп
3. **Обработка на грешки:** Показва съобщения за грешки на потребителите
4. **Клиентска обработка:** JavaScript за анализ на изображения (с Transformers.js)

### 5. Конфигурация

**Файл:** `application.properties`

Настройки за конфигурация на приложението:

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

**Обяснение на конфигурацията:**

1. **Качване на файлове:** Позволява изображения до 10MB
2. **Логване:** Контролира каква информация се записва по време на изпълнение
3. **GitHub Models:** Определя кой AI модел и крайна точка да се използват
4. **Сигурност:** Конфигурация за обработка на грешки, за да се избегне излагане на чувствителна информация

## Стартиране на приложението

### Стъпка 1: Настройте своя GitHub токен

Първо, трябва да зададете своя GitHub токен като променлива на средата:

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

**Защо е необходимо това:**
- GitHub Models изисква удостоверяване за достъп до AI модели
- Използването на променливи на средата предпазва чувствителните токени от изтичане в изходния код
- Обхватът `models:read` осигурява достъп до AI инференция

### Стъпка 2: Създайте и стартирайте

Навигирайте до директорията на проекта:
```bash
cd 04-PracticalSamples/petstory
```

Създайте приложението:
```bash
mvn clean compile
```

Стартирайте сървъра:
```bash
mvn spring-boot:run
```

Приложението ще стартира на `http://localhost:8080`.

### Стъпка 3: Тествайте приложението

1. **Отворете** `http://localhost:8080` във вашия браузър
2. **Опишете** своя домашен любимец в текстовото поле (например: "Игрив златен ретривър, който обича да носи топки")
3. **Кликнете** върху "Генерирай история", за да получите AI-генерирана история
4. **Алтернативно**, качете изображение на домашния си любимец, за да генерирате автоматично описание
5. **Прегледайте** креативната история, базирана на описанието на вашия домашен любимец

## Как всичко работи заедно

Ето как протича целият процес на генериране на история за домашен любимец:

1. **Вход от потребителя:** Описвате своя домашен любимец във формата на уеб страницата
2. **Подаване на формата:** Браузърът изпраща POST заявка към `/generate-story`
3. **Обработка от контролера:** `PetController` валидира и санитизира входните данни
4. **Извикване на AI услугата:** `StoryService` изпраща заявка към GitHub Models API
5. **Генериране на история:** AI генерира креативна история въз основа на описанието
6. **Обработка на отговора:** Контролерът получава историята и я добавя към модела
7. **Рендиране на шаблона:** Thymeleaf рендира `result.html` с историята
8. **Показване:** Потребителят вижда генерираната история в своя браузър

**Поток за обработка на грешки:**
Ако AI услугата се провали:
1. Контролерът улавя изключението
2. Генерира резервна история, използвайки предварително написани шаблони
3. Показва резервната история с бележка за недостъпност на AI
4. Потребителят все пак получава история, осигурявайки добро потребителско изживяване

## Разбиране на интеграцията с AI

### GitHub Models API
Приложението използва GitHub Models, които предоставят безплатен достъп до различни AI модели:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Инженеринг на промптове
Услугата използва внимателно създадени промптове за постигане на добри резултати:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Обработка на отговорите
Отговорът от AI се извлича и валидира:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Следващи стъпки

За повече примери вижте [Глава 04: Практически примери](../README.md)

**Отказ от отговорност**:  
Този документ е преведен с помощта на AI услуга за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Не носим отговорност за недоразумения или погрешни интерпретации, произтичащи от използването на този превод.