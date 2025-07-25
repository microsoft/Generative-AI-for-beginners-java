<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:28:51+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "uk"
}
-->
# Посібник для початківців: Генератор історій про домашніх улюбленців

## Зміст

- [Попередні вимоги](../../../../04-PracticalSamples/petstory)
- [Розуміння структури проєкту](../../../../04-PracticalSamples/petstory)
- [Пояснення основних компонентів](../../../../04-PracticalSamples/petstory)
  - [1. Головний додаток](../../../../04-PracticalSamples/petstory)
  - [2. Веб-контролер](../../../../04-PracticalSamples/petstory)
  - [3. Сервіс історій](../../../../04-PracticalSamples/petstory)
  - [4. Веб-шаблони](../../../../04-PracticalSamples/petstory)
  - [5. Конфігурація](../../../../04-PracticalSamples/petstory)
- [Запуск додатка](../../../../04-PracticalSamples/petstory)
- [Як усе працює разом](../../../../04-PracticalSamples/petstory)
- [Розуміння інтеграції з AI](../../../../04-PracticalSamples/petstory)
- [Наступні кроки](../../../../04-PracticalSamples/petstory)

## Попередні вимоги

Перед початком переконайтеся, що у вас є:
- Встановлений Java 21 або новіший
- Maven для управління залежностями
- Обліковий запис GitHub із персональним токеном доступу (PAT) зі сферою `models:read`
- Базове розуміння Java, Spring Boot та веб-розробки

## Розуміння структури проєкту

Проєкт генератора історій про домашніх улюбленців містить кілька важливих файлів:

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

## Пояснення основних компонентів

### 1. Головний додаток

**Файл:** `PetStoryApplication.java`

Це точка входу для нашого додатка Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Що це робить:**
- Анотація `@SpringBootApplication` активує автоконфігурацію та сканування компонентів
- Запускає вбудований веб-сервер (Tomcat) на порту 8080
- Автоматично створює всі необхідні Spring-біни та сервіси

### 2. Веб-контролер

**Файл:** `PetController.java`

Цей компонент обробляє всі веб-запити та взаємодію з користувачами:

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

**Ключові особливості:**

1. **Обробка маршрутів**: `@GetMapping("/")` відображає форму завантаження, `@PostMapping("/generate-story")` обробляє відправлені дані
2. **Валідація введення**: Перевіряє порожні описи та обмеження довжини
3. **Безпека**: Очищає введені дані користувача для запобігання XSS-атакам
4. **Обробка помилок**: Надає резервні історії, якщо сервіс AI недоступний
5. **Прив’язка моделі**: Передає дані до HTML-шаблонів за допомогою Spring `Model`

**Резервна система:**
Контролер включає заздалегідь написані шаблони історій, які використовуються, коли сервіс AI недоступний:

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

### 3. Сервіс історій

**Файл:** `StoryService.java`

Цей сервіс взаємодіє з GitHub Models для генерації історій:

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

**Ключові компоненти:**

1. **Клієнт OpenAI**: Використовує офіційний Java SDK OpenAI, налаштований для GitHub Models
2. **Системний запит**: Встановлює поведінку AI для написання сімейних історій про домашніх улюбленців
3. **Користувацький запит**: Вказує AI, яку саме історію написати на основі опису
4. **Параметри**: Контролює довжину історії та рівень креативності
5. **Обробка помилок**: Генерує винятки, які обробляє контролер

### 4. Веб-шаблони

**Файл:** `index.html` (Форма завантаження)

Головна сторінка, де користувачі описують своїх улюбленців:

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

**Файл:** `result.html` (Відображення історії)

Відображає згенеровану історію:

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

**Особливості шаблонів:**

1. **Інтеграція Thymeleaf**: Використовує атрибути `th:` для динамічного контенту
2. **Адаптивний дизайн**: CSS-стилі для мобільних пристроїв і настільних комп’ютерів
3. **Обробка помилок**: Відображає помилки валідації користувачам
4. **Клієнтська обробка**: JavaScript для аналізу зображень (з використанням Transformers.js)

### 5. Конфігурація

**Файл:** `application.properties`

Налаштування конфігурації для додатка:

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

**Пояснення конфігурації:**

1. **Завантаження файлів**: Дозволяє завантажувати зображення до 10 МБ
2. **Логування**: Контролює, яка інформація записується під час виконання
3. **GitHub Models**: Вказує, яку AI-модель і кінцеву точку використовувати
4. **Безпека**: Налаштування обробки помилок для уникнення розкриття конфіденційної інформації

## Запуск додатка

### Крок 1: Встановіть GitHub-токен

Спочатку потрібно встановити GitHub-токен як змінну середовища:

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

**Чому це потрібно:**
- GitHub Models вимагає автентифікації для доступу до AI-моделей
- Використання змінних середовища дозволяє зберігати токени поза вихідним кодом
- Сфера `models:read` надає доступ до AI-інференції

### Крок 2: Збірка та запуск

Перейдіть до каталогу проєкту:
```bash
cd 04-PracticalSamples/petstory
```

Зберіть додаток:
```bash
mvn clean compile
```

Запустіть сервер:
```bash
mvn spring-boot:run
```

Додаток запуститься на `http://localhost:8080`.

### Крок 3: Тестування додатка

1. **Відкрийте** `http://localhost:8080` у вашому браузері
2. **Опишіть** свого улюбленця в текстовому полі (наприклад, "Грайливий золотистий ретривер, який любить приносити м’яч")
3. **Натисніть** "Згенерувати історію", щоб отримати історію, створену AI
4. **Або** завантажте зображення улюбленця для автоматичного створення опису
5. **Перегляньте** креативну історію, засновану на описі вашого улюбленця

## Як усе працює разом

Ось повний процес генерації історії про домашнього улюбленця:

1. **Введення користувача**: Ви описуєте свого улюбленця у веб-формі
2. **Відправка форми**: Браузер надсилає POST-запит на `/generate-story`
3. **Обробка контролером**: `PetController` перевіряє та очищає введені дані
4. **Виклик AI-сервісу**: `StoryService` надсилає запит до API GitHub Models
5. **Генерація історії**: AI створює креативну історію на основі опису
6. **Обробка відповіді**: Контролер отримує історію та додає її до моделі
7. **Рендеринг шаблону**: Thymeleaf рендерить `result.html` з історією
8. **Відображення**: Користувач бачить згенеровану історію у своєму браузері

**Процес обробки помилок:**
Якщо сервіс AI недоступний:
1. Контролер перехоплює виняток
2. Генерує резервну історію за допомогою заздалегідь написаних шаблонів
3. Відображає резервну історію з приміткою про недоступність AI
4. Користувач все одно отримує історію, забезпечуючи позитивний досвід

## Розуміння інтеграції з AI

### API GitHub Models
Додаток використовує GitHub Models, який надає безкоштовний доступ до різних AI-моделей:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Інженерія запитів
Сервіс використовує ретельно створені запити для отримання якісних результатів:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Обробка відповіді
Відповідь AI витягується та перевіряється:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Наступні кроки

Для отримання додаткових прикладів дивіться [Розділ 04: Практичні приклади](../README.md)

**Відмова від відповідальності**:  
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, звертаємо вашу увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.