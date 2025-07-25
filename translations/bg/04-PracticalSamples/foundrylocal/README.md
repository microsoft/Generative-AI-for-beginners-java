<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:14:34+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "bg"
}
-->
# Локален Foundry Spring Boot Урок

## Съдържание

- [Предварителни изисквания](../../../../04-PracticalSamples/foundrylocal)
- [Преглед на проекта](../../../../04-PracticalSamples/foundrylocal)
- [Разбиране на кода](../../../../04-PracticalSamples/foundrylocal)
  - [1. Конфигурация на приложението (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Основен клас на приложението (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Слой за AI услуги (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Зависимости на проекта (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Как всичко работи заедно](../../../../04-PracticalSamples/foundrylocal)
- [Настройка на Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Стартиране на приложението](../../../../04-PracticalSamples/foundrylocal)
- [Очакван резултат](../../../../04-PracticalSamples/foundrylocal)
- [Следващи стъпки](../../../../04-PracticalSamples/foundrylocal)
- [Отстраняване на проблеми](../../../../04-PracticalSamples/foundrylocal)

## Предварителни изисквания

Преди да започнете този урок, уверете се, че разполагате със следното:

- **Java 21 или по-нова версия**, инсталирана на вашата система
- **Maven 3.6+** за изграждане на проекта
- **Foundry Local**, инсталиран и работещ

### **Инсталиране на Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Преглед на проекта

Този проект се състои от четири основни компонента:

1. **Application.java** - Основната входна точка на Spring Boot приложението
2. **FoundryLocalService.java** - Слой за услуги, който обработва AI комуникацията
3. **application.properties** - Конфигурация за връзка с Foundry Local
4. **pom.xml** - Maven зависимости и конфигурация на проекта

## Разбиране на кода

### 1. Конфигурация на приложението (application.properties)

**Файл:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Какво прави това:**
- **base-url**: Посочва къде работи Foundry Local (по подразбиране порт 5273)
- **model**: Името на AI модела, който ще се използва за генериране на текст

**Ключова концепция:** Spring Boot автоматично зарежда тези свойства и ги прави достъпни за вашето приложение чрез анотацията `@Value`.

### 2. Основен клас на приложението (Application.java)

**Файл:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Какво прави това:**
- `@SpringBootApplication` активира автоматичната конфигурация на Spring Boot
- `WebApplicationType.NONE` указва, че това е командно приложение, а не уеб сървър
- Основният метод стартира Spring приложението

**Демо изпълнител:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**Какво прави това:**
- `@Bean` създава компонент, който Spring управлява
- `CommandLineRunner` изпълнява код след стартиране на Spring Boot
- `foundryLocalService` се инжектира автоматично от Spring (dependency injection)
- Изпраща тестово съобщение до AI и показва отговора

### 3. Слой за AI услуги (FoundryLocalService.java)

**Файл:** `src/main/java/com/example/FoundryLocalService.java`

#### Инжектиране на конфигурация:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Какво прави това:**
- `@Service` указва на Spring, че този клас предоставя бизнес логика
- `@Value` инжектира стойности от application.properties
- Синтаксисът `:default-value` предоставя резервни стойности, ако свойствата не са зададени

#### Инициализация на клиента:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Какво прави това:**
- `@PostConstruct` изпълнява този метод след създаването на услугата от Spring
- Създава OpenAI клиент, който сочи към вашия локален Foundry Local екземпляр
- Пътят `/v1` е необходим за съвместимост с OpenAI API
- API ключът е "unused", тъй като локалната разработка не изисква удостоверяване

#### Метод за чат:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**Какво прави това:**
- **ChatCompletionCreateParams**: Конфигурира заявката към AI
  - `model`: Посочва кой AI модел да се използва
  - `addUserMessage`: Добавя вашето съобщение към разговора
  - `maxCompletionTokens`: Ограничение за дължината на отговора (пести ресурси)
  - `temperature`: Контролира случайността (0.0 = детерминирано, 1.0 = креативно)
- **API заявка**: Изпраща заявката към Foundry Local
- **Обработка на отговор**: Извлича текстовия отговор на AI безопасно
- **Обработка на грешки**: Обвива изключенията с полезни съобщения за грешки

### 4. Зависимости на проекта (pom.xml)

**Ключови зависимости:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Какво правят те:**
- **spring-boot-starter**: Осигурява основната функционалност на Spring Boot
- **openai-java**: Официален OpenAI Java SDK за API комуникация
- **jackson-databind**: Обработва JSON сериализация/десериализация за API заявки

## Как всичко работи заедно

Ето пълния процес, когато стартирате приложението:

1. **Стартиране**: Spring Boot стартира и чете `application.properties`
2. **Създаване на услуга**: Spring създава `FoundryLocalService` и инжектира конфигурационни стойности
3. **Настройка на клиента**: `@PostConstruct` инициализира OpenAI клиента за връзка с Foundry Local
4. **Изпълнение на демо**: `CommandLineRunner` се изпълнява след стартиране
5. **AI заявка**: Демото извиква `foundryLocalService.chat()` с тестово съобщение
6. **API заявка**: Услугата изгражда и изпраща съвместима с OpenAI заявка към Foundry Local
7. **Обработка на отговор**: Услугата извлича и връща отговора на AI
8. **Показване**: Приложението отпечатва отговора и излиза

## Настройка на Foundry Local

За да настроите Foundry Local, следвайте тези стъпки:

1. **Инсталирайте Foundry Local** според инструкциите в секцията [Предварителни изисквания](../../../../04-PracticalSamples/foundrylocal).
2. **Изтеглете AI модела**, който искате да използвате, например `phi-3.5-mini`, със следната команда:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Конфигурирайте файла application.properties**, за да съответства на настройките на Foundry Local, особено ако използвате различен порт или модел.

## Стартиране на приложението

### Стъпка 1: Стартирайте Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Стъпка 2: Изградете и стартирайте приложението
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Очакван резултат

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Следващи стъпки

За повече примери вижте [Глава 04: Практически примери](../README.md)

## Отстраняване на проблеми

### Чести проблеми

**"Connection refused" или "Service unavailable"**
- Уверете се, че Foundry Local работи: `foundry model list`
- Проверете дали услугата е на порт 5273: Проверете `application.properties`
- Опитайте да рестартирате Foundry Local: `foundry model run phi-3.5-mini`

**Грешки "Model not found"**
- Проверете наличните модели: `foundry model list`
- Актуализирайте името на модела в `application.properties`, за да съвпада точно
- Изтеглете модела, ако е необходимо: `foundry model run phi-3.5-mini`

**Грешки при компилация с Maven**
- Уверете се, че използвате Java 21 или по-нова версия: `java -version`
- Почистете и изградете отново: `mvn clean compile`
- Проверете интернет връзката за изтегляне на зависимости

**Приложението стартира, но няма изход**
- Проверете дали Foundry Local отговаря: Отворете браузър на `http://localhost:5273`
- Проверете логовете на приложението за конкретни съобщения за грешки
- Уверете се, че моделът е напълно зареден и готов

**Отказ от отговорност**:  
Този документ е преведен с помощта на AI услуга за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматичните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия изходен език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален превод от човек. Ние не носим отговорност за каквито и да било недоразумения или погрешни интерпретации, произтичащи от използването на този превод.