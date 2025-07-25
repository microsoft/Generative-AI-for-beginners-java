<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:27:58+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "uk"
}
-->
# Локальний підручник Foundry Spring Boot

## Зміст

- [Попередні вимоги](../../../../04-PracticalSamples/foundrylocal)
- [Огляд проекту](../../../../04-PracticalSamples/foundrylocal)
- [Розуміння коду](../../../../04-PracticalSamples/foundrylocal)
  - [1. Конфігурація додатку (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Головний клас додатку (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Сервісний шар AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Залежності проекту (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Як усе працює разом](../../../../04-PracticalSamples/foundrylocal)
- [Налаштування Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Запуск додатку](../../../../04-PracticalSamples/foundrylocal)
- [Очікуваний результат](../../../../04-PracticalSamples/foundrylocal)
- [Наступні кроки](../../../../04-PracticalSamples/foundrylocal)
- [Вирішення проблем](../../../../04-PracticalSamples/foundrylocal)

## Попередні вимоги

Перед початком цього підручника переконайтеся, що у вас є:

- **Java 21 або новіша версія**, встановлена на вашій системі
- **Maven 3.6+** для збірки проекту
- **Foundry Local**, встановлений і запущений

### **Встановлення Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Огляд проекту

Цей проект складається з чотирьох основних компонентів:

1. **Application.java** - Головна точка входу Spring Boot додатку
2. **FoundryLocalService.java** - Сервісний шар, який обробляє взаємодію з AI
3. **application.properties** - Конфігурація для підключення до Foundry Local
4. **pom.xml** - Залежності Maven і конфігурація проекту

## Розуміння коду

### 1. Конфігурація додатку (application.properties)

**Файл:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Що це робить:**
- **base-url**: Вказує, де запущений Foundry Local (стандартний порт 5273)
- **model**: Назва AI моделі для генерації тексту

**Основна концепція:** Spring Boot автоматично завантажує ці властивості і робить їх доступними для вашого додатку за допомогою анотації `@Value`.

### 2. Головний клас додатку (Application.java)

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

**Що це робить:**
- `@SpringBootApplication` активує автоматичну конфігурацію Spring Boot
- `WebApplicationType.NONE` вказує Spring, що це консольний додаток, а не веб-сервер
- Головний метод запускає Spring додаток

**Демонстраційний запуск:**
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

**Що це робить:**
- `@Bean` створює компонент, який управляється Spring
- `CommandLineRunner` виконує код після запуску Spring Boot
- `foundryLocalService` автоматично впроваджується Spring (ін'єкція залежностей)
- Надсилає тестове повідомлення до AI і відображає відповідь

### 3. Сервісний шар AI (FoundryLocalService.java)

**Файл:** `src/main/java/com/example/FoundryLocalService.java`

#### Ін'єкція конфігурації:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Що це робить:**
- `@Service` вказує Spring, що цей клас забезпечує бізнес-логіку
- `@Value` впроваджує значення конфігурації з application.properties
- Синтаксис `:default-value` забезпечує резервні значення, якщо властивості не задані

#### Ініціалізація клієнта:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Що це робить:**
- `@PostConstruct` виконує цей метод після створення сервісу Spring
- Створює клієнт OpenAI, який підключається до вашого локального Foundry Local
- Шлях `/v1` потрібен для сумісності з API OpenAI
- API ключ "unused", оскільки локальна розробка не потребує аутентифікації

#### Метод чату:
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

**Що це робить:**
- **ChatCompletionCreateParams**: Налаштовує запит до AI
  - `model`: Вказує, яку AI модель використовувати
  - `addUserMessage`: Додає ваше повідомлення до розмови
  - `maxCompletionTokens`: Обмежує довжину відповіді (економить ресурси)
  - `temperature`: Контролює випадковість (0.0 = детермінованість, 1.0 = креативність)
- **API виклик**: Надсилає запит до Foundry Local
- **Обробка відповіді**: Безпечно витягує текстову відповідь AI
- **Обробка помилок**: Обгортає виключення з корисними повідомленнями про помилки

### 4. Залежності проекту (pom.xml)

**Основні залежності:**

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

**Що вони роблять:**
- **spring-boot-starter**: Забезпечує основну функціональність Spring Boot
- **openai-java**: Офіційний Java SDK OpenAI для взаємодії з API
- **jackson-databind**: Обробляє JSON серіалізацію/десеріалізацію для API викликів

## Як усе працює разом

Ось повний процес, коли ви запускаєте додаток:

1. **Запуск**: Spring Boot запускається і читає `application.properties`
2. **Створення сервісу**: Spring створює `FoundryLocalService` і впроваджує значення конфігурації
3. **Налаштування клієнта**: `@PostConstruct` ініціалізує клієнт OpenAI для підключення до Foundry Local
4. **Виконання демо**: `CommandLineRunner` виконується після запуску
5. **Виклик AI**: Демонстрація викликає `foundryLocalService.chat()` з тестовим повідомленням
6. **Запит API**: Сервіс створює і надсилає запит, сумісний з OpenAI, до Foundry Local
7. **Обробка відповіді**: Сервіс витягує і повертає відповідь AI
8. **Відображення**: Додаток друкує відповідь і завершує роботу

## Налаштування Foundry Local

Щоб налаштувати Foundry Local, виконайте наступні кроки:

1. **Встановіть Foundry Local** за інструкціями в розділі [Попередні вимоги](../../../../04-PracticalSamples/foundrylocal).
2. **Завантажте AI модель**, яку ви хочете використовувати, наприклад, `phi-3.5-mini`, за допомогою наступної команди:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Налаштуйте файл application.properties** відповідно до ваших налаштувань Foundry Local, особливо якщо ви використовуєте інший порт або модель.

## Запуск додатку

### Крок 1: Запустіть Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Крок 2: Зберіть і запустіть додаток
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Очікуваний результат

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

## Наступні кроки

Для отримання додаткових прикладів дивіться [Розділ 04: Практичні приклади](../README.md)

## Вирішення проблем

### Поширені проблеми

**"Connection refused" або "Service unavailable"**
- Переконайтеся, що Foundry Local запущений: `foundry model list`
- Перевірте, чи сервіс працює на порту 5273: Перевірте `application.properties`
- Спробуйте перезапустити Foundry Local: `foundry model run phi-3.5-mini`

**Помилки "Model not found"**
- Перевірте доступні моделі: `foundry model list`
- Оновіть назву моделі в `application.properties`, щоб вона точно збігалася
- Завантажте модель, якщо це необхідно: `foundry model run phi-3.5-mini`

**Помилки компіляції Maven**
- Переконайтеся, що використовується Java 21 або новіша: `java -version`
- Очистіть і зберіть проект заново: `mvn clean compile`
- Перевірте інтернет-з'єднання для завантаження залежностей

**Додаток запускається, але немає результату**
- Перевірте, чи Foundry Local відповідає: Відкрийте браузер на `http://localhost:5273`
- Перевірте журнали додатку на наявність конкретних повідомлень про помилки
- Переконайтеся, що модель повністю завантажена і готова

**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критичної інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникають внаслідок використання цього перекладу.