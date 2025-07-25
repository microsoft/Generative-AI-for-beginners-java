<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:18:17+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "sr"
}
-->
# Туторијал за Генератор Причa о Кућним Љубимцима за Почетнике

## Садржај

- [Предуслови](../../../../04-PracticalSamples/petstory)
- [Разумевање Структуре Пројекта](../../../../04-PracticalSamples/petstory)
- [Објашњење Основних Компоненти](../../../../04-PracticalSamples/petstory)
  - [1. Главна Апликација](../../../../04-PracticalSamples/petstory)
  - [2. Веб Контролер](../../../../04-PracticalSamples/petstory)
  - [3. Сервис за Приче](../../../../04-PracticalSamples/petstory)
  - [4. Веб Шаблони](../../../../04-PracticalSamples/petstory)
  - [5. Конфигурација](../../../../04-PracticalSamples/petstory)
- [Покретање Апликације](../../../../04-PracticalSamples/petstory)
- [Како Све Ради Заједно](../../../../04-PracticalSamples/petstory)
- [Разумевање Интеграције са Вештачком Интелигенцијом](../../../../04-PracticalSamples/petstory)
- [Следећи Кораци](../../../../04-PracticalSamples/petstory)

## Предуслови

Пре него што почнете, уверите се да имате:
- Инсталиран Java 21 или новији
- Maven за управљање зависностима
- GitHub налог са персоналним приступним токеном (PAT) са `models:read` дозволом
- Основно разумевање Java, Spring Boot-а и веб развоја

## Разумевање Структуре Пројекта

Пројекат за приче о кућним љубимцима садржи неколико важних фајлова:

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

## Објашњење Основних Компоненти

### 1. Главна Апликација

**Фајл:** `PetStoryApplication.java`

Ово је улазна тачка за нашу Spring Boot апликацију:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Шта ово ради:**
- Анотација `@SpringBootApplication` омогућава аутоматску конфигурацију и скенирање компоненти
- Покреће уграђени веб сервер (Tomcat) на порту 8080
- Аутоматски креира све неопходне Spring бинове и сервисе

### 2. Веб Контролер

**Фајл:** `PetController.java`

Ово обрађује све веб захтеве и интеракције са корисницима:

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

**Кључне карактеристике:**

1. **Обрада Рута:** `@GetMapping("/")` приказује форму за отпремање, `@PostMapping("/generate-story")` обрађује поднете податке
2. **Валидација Уноса:** Проверава празне описе и ограничења дужине
3. **Безбедност:** Чисти кориснички унос како би спречио XSS нападе
4. **Обрада Грешака:** Нуди резервне приче када AI сервис не успе
5. **Повезивање Модела:** Прослеђује податке HTML шаблонима користећи Spring-ов `Model`

**Систем Резервних Причa:**
Контролер укључује унапред написане шаблоне прича који се користе када AI сервис није доступан:

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

### 3. Сервис за Приче

**Фајл:** `StoryService.java`

Овај сервис комуницира са GitHub Models ради генерисања прича:

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

**Кључне компоненте:**

1. **OpenAI Клијент:** Користи званични OpenAI Java SDK конфигурисан за GitHub Models
2. **Системски Подстицај:** Поставља понашање AI-а да пише породично прилагођене приче о кућним љубимцима
3. **Кориснички Подстицај:** Прецизира AI-у какву причу да напише на основу описа
4. **Параметри:** Контролише дужину приче и ниво креативности
5. **Обрада Грешака:** Баца изузетке које контролер хвата и обрађује

### 4. Веб Шаблони

**Фајл:** `index.html` (Форма за Отпремање)

Главна страница где корисници описују своје љубимце:

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

**Фајл:** `result.html` (Приказ Приче)

Приказује генерисану причу:

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

**Карактеристике Шаблона:**

1. **Интеграција са Thymeleaf-ом:** Користи `th:` атрибуте за динамички садржај
2. **Респонзивни Дизајн:** CSS стилизовање за мобилне уређаје и десктоп
3. **Обрада Грешака:** Приказује грешке валидације корисницима
4. **Клијентска Обрада:** JavaScript за анализу слика (користећи Transformers.js)

### 5. Конфигурација

**Фајл:** `application.properties`

Подешавања конфигурације за апликацију:

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

**Објашњење Конфигурације:**

1. **Отпремање Фајлова:** Дозвољава слике до 10MB
2. **Логовање:** Контролише које информације се бележе током извршавања
3. **GitHub Models:** Специфицира који AI модел и крајњу тачку користити
4. **Безбедност:** Конфигурација обраде грешака како би се избегло излагање осетљивих информација

## Покретање Апликације

### Корак 1: Поставите Ваш GitHub Токен

Прво, потребно је да поставите свој GitHub токен као променљиву окружења:

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

**Зашто је ово потребно:**
- GitHub Models захтева аутентификацију за приступ AI моделима
- Коришћење променљивих окружења чува осетљиве токене ван изворног кода
- Дозвола `models:read` омогућава приступ AI инференцији

### Корак 2: Изградите и Покрените

Идите у директоријум пројекта:
```bash
cd 04-PracticalSamples/petstory
```

Изградите апликацију:
```bash
mvn clean compile
```

Покрените сервер:
```bash
mvn spring-boot:run
```

Апликација ће се покренути на `http://localhost:8080`.

### Корак 3: Тестирајте Апликацију

1. **Отворите** `http://localhost:8080` у вашем претраживачу
2. **Опишите** свог љубимца у текстуалном пољу (нпр. "Разиграни златни ретривер који воли да доноси лоптице")
3. **Кликните** на "Генериши Причу" да бисте добили причу генерисану помоћу AI-а
4. **Алтернативно**, отпремите слику љубимца за аутоматско генерисање описа
5. **Погледајте** креативну причу засновану на опису вашег љубимца

## Како Све Ради Заједно

Ево комплетног тока када генеришете причу о кућном љубимцу:

1. **Кориснички Унос:** Описујете свог љубимца у веб форми
2. **Подношење Форме:** Претраживач шаље POST захтев на `/generate-story`
3. **Обрада у Контролеру:** `PetController` валида и чисти унос
4. **Позив AI Сервиса:** `StoryService` шаље захтев GitHub Models API-ју
5. **Генерисање Приче:** AI генерише креативну причу на основу описа
6. **Обрада Одговора:** Контролер прима причу и додаје је у модел
7. **Рендеровање Шаблона:** Thymeleaf рендерује `result.html` са причом
8. **Приказ:** Корисник види генерисану причу у свом претраживачу

**Ток Обраде Грешака:**
Ако AI сервис не успе:
1. Контролер хвата изузетак
2. Генерише резервну причу користећи унапред написане шаблоне
3. Приказује резервну причу са напоменом о недоступности AI-а
4. Корисник ипак добија причу, што осигурава добро корисничко искуство

## Разумевање Интеграције са Вештачком Интелигенцијом

### GitHub Models API
Апликација користи GitHub Models, који пружа бесплатан приступ разним AI моделима:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Инжењеринг Подстицаја
Сервис користи пажљиво осмишљене подстицаје за добијање добрих резултата:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Обрада Одговора
Одговор AI-а се извлачи и валида:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Следећи Кораци

За више примера, погледајте [Поглавље 04: Практични примери](../README.md)

**Одрицање од одговорности**:  
Овај документ је преведен коришћењем услуге за превођење помоћу вештачке интелигенције [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да обезбедимо тачност, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални превод од стране људи. Не сносимо одговорност за било каква погрешна тумачења или неспоразуме који могу произаћи из коришћења овог превода.