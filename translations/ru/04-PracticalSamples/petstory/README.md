<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:42:39+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ru"
}
-->
# Учебник по генератору историй о питомцах для начинающих

## Содержание

- [Предварительные требования](../../../../04-PracticalSamples/petstory)
- [Понимание структуры проекта](../../../../04-PracticalSamples/petstory)
- [Объяснение основных компонентов](../../../../04-PracticalSamples/petstory)
  - [1. Основное приложение](../../../../04-PracticalSamples/petstory)
  - [2. Веб-контроллер](../../../../04-PracticalSamples/petstory)
  - [3. Сервис историй](../../../../04-PracticalSamples/petstory)
  - [4. Веб-шаблоны](../../../../04-PracticalSamples/petstory)
  - [5. Конфигурация](../../../../04-PracticalSamples/petstory)
- [Запуск приложения](../../../../04-PracticalSamples/petstory)
- [Как все работает вместе](../../../../04-PracticalSamples/petstory)
- [Понимание интеграции с ИИ](../../../../04-PracticalSamples/petstory)
- [Следующие шаги](../../../../04-PracticalSamples/petstory)

## Предварительные требования

Перед началом убедитесь, что у вас есть:
- Установленный Java 21 или выше
- Maven для управления зависимостями
- Аккаунт GitHub с персональным токеном доступа (PAT) с правами `models:read`
- Базовые знания Java, Spring Boot и веб-разработки

## Понимание структуры проекта

Проект генератора историй о питомцах включает несколько важных файлов:

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

## Объяснение основных компонентов

### 1. Основное приложение

**Файл:** `PetStoryApplication.java`

Это точка входа для нашего приложения на Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Что делает этот файл:**
- Аннотация `@SpringBootApplication` включает автонастройку и сканирование компонентов
- Запускает встроенный веб-сервер (Tomcat) на порту 8080
- Автоматически создает все необходимые Spring-бины и сервисы

### 2. Веб-контроллер

**Файл:** `PetController.java`

Этот компонент обрабатывает все веб-запросы и взаимодействие с пользователем:

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

**Основные функции:**

1. **Обработка маршрутов**: `@GetMapping("/")` отображает форму загрузки, `@PostMapping("/generate-story")` обрабатывает отправленные данные
2. **Валидация ввода**: Проверяет пустые описания и ограничения по длине
3. **Безопасность**: Очищает пользовательский ввод для предотвращения XSS-атак
4. **Обработка ошибок**: Предоставляет запасные истории, если сервис ИИ недоступен
5. **Привязка модели**: Передает данные в HTML-шаблоны с использованием Spring `Model`

**Система запасных вариантов:**
Контроллер включает заранее написанные шаблоны историй, которые используются, если сервис ИИ недоступен:

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

### 3. Сервис историй

**Файл:** `StoryService.java`

Этот сервис взаимодействует с GitHub Models для генерации историй:

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

**Основные компоненты:**

1. **Клиент OpenAI**: Использует официальный Java SDK OpenAI, настроенный для GitHub Models
2. **Системный запрос**: Устанавливает поведение ИИ для написания семейных историй о питомцах
3. **Пользовательский запрос**: Указывает ИИ, какую историю написать на основе описания
4. **Параметры**: Управляет длиной истории и уровнем креативности
5. **Обработка ошибок**: Вызывает исключения, которые контроллер перехватывает и обрабатывает

### 4. Веб-шаблоны

**Файл:** `index.html` (Форма загрузки)

Главная страница, где пользователи описывают своих питомцев:

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

**Файл:** `result.html` (Отображение истории)

Показывает сгенерированную историю:

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

**Особенности шаблонов:**

1. **Интеграция Thymeleaf**: Использует атрибуты `th:` для динамического контента
2. **Адаптивный дизайн**: CSS-стили для мобильных и настольных устройств
3. **Обработка ошибок**: Отображает ошибки валидации пользователям
4. **Клиентская обработка**: JavaScript для анализа изображений (с использованием Transformers.js)

### 5. Конфигурация

**Файл:** `application.properties`

Настройки конфигурации для приложения:

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

**Объяснение конфигурации:**

1. **Загрузка файлов**: Разрешает изображения размером до 10 МБ
2. **Логирование**: Управляет информацией, которая записывается в процессе выполнения
3. **GitHub Models**: Указывает, какую модель ИИ и конечную точку использовать
4. **Безопасность**: Настройки обработки ошибок для предотвращения утечки конфиденциальной информации

## Запуск приложения

### Шаг 1: Установите токен GitHub

Сначала необходимо установить токен GitHub как переменную окружения:

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

**Почему это необходимо:**
- GitHub Models требует аутентификации для доступа к моделям ИИ
- Использование переменных окружения позволяет избежать хранения токенов в исходном коде
- Права `models:read` предоставляют доступ к выполнению запросов ИИ

### Шаг 2: Сборка и запуск

Перейдите в директорию проекта:
```bash
cd 04-PracticalSamples/petstory
```

Соберите приложение:
```bash
mvn clean compile
```

Запустите сервер:
```bash
mvn spring-boot:run
```

Приложение запустится на `http://localhost:8080`.

### Шаг 3: Тестирование приложения

1. **Откройте** `http://localhost:8080` в браузере
2. **Опишите** своего питомца в текстовом поле (например, "Игривый золотистый ретривер, который любит приносить предметы")
3. **Нажмите** "Сгенерировать историю", чтобы получить историю, созданную ИИ
4. **Или** загрузите изображение питомца для автоматического создания описания
5. **Просмотрите** креативную историю, основанную на описании вашего питомца

## Как все работает вместе

Вот полный процесс генерации истории о питомце:

1. **Ввод пользователя**: Вы описываете своего питомца в веб-форме
2. **Отправка формы**: Браузер отправляет POST-запрос на `/generate-story`
3. **Обработка контроллером**: `PetController` проверяет и очищает ввод
4. **Вызов сервиса ИИ**: `StoryService` отправляет запрос к API GitHub Models
5. **Генерация истории**: ИИ создает креативную историю на основе описания
6. **Обработка ответа**: Контроллер получает историю и добавляет ее в модель
7. **Рендеринг шаблона**: Thymeleaf отображает `result.html` с историей
8. **Отображение**: Пользователь видит сгенерированную историю в браузере

**Процесс обработки ошибок:**
Если сервис ИИ недоступен:
1. Контроллер перехватывает исключение
2. Генерирует запасную историю с использованием заранее написанных шаблонов
3. Отображает запасную историю с уведомлением о недоступности ИИ
4. Пользователь все равно получает историю, обеспечивая хороший пользовательский опыт

## Понимание интеграции с ИИ

### API GitHub Models
Приложение использует GitHub Models, который предоставляет бесплатный доступ к различным моделям ИИ:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Инженерия запросов
Сервис использует тщательно составленные запросы для получения качественных результатов:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Обработка ответа
Ответ ИИ извлекается и проверяется:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Следующие шаги

Для получения дополнительных примеров смотрите [Глава 04: Практические примеры](../README.md)

**Отказ от ответственности**:  
Этот документ был переведен с использованием сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия обеспечить точность, автоматические переводы могут содержать ошибки или неточности. Оригинальный документ на его родном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные интерпретации, возникающие в результате использования данного перевода.