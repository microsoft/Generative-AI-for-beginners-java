<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:27:11+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "uk"
}
-->
# Основні техніки генеративного штучного інтелекту: навчальний посібник

## Зміст

- [Попередні вимоги](../../../03-CoreGenerativeAITechniques)
- [Початок роботи](../../../03-CoreGenerativeAITechniques)
  - [Крок 1: Налаштуйте змінну середовища](../../../03-CoreGenerativeAITechniques)
  - [Крок 2: Перейдіть до каталогу прикладів](../../../03-CoreGenerativeAITechniques)
- [Навчальний посібник 1: Завершення та чат LLM](../../../03-CoreGenerativeAITechniques)
- [Навчальний посібник 2: Виклик функцій](../../../03-CoreGenerativeAITechniques)
- [Навчальний посібник 3: RAG (Генерація з розширеним пошуком)](../../../03-CoreGenerativeAITechniques)
- [Навчальний посібник 4: Відповідальний штучний інтелект](../../../03-CoreGenerativeAITechniques)
- [Загальні шаблони у прикладах](../../../03-CoreGenerativeAITechniques)
- [Наступні кроки](../../../03-CoreGenerativeAITechniques)
- [Вирішення проблем](../../../03-CoreGenerativeAITechniques)
  - [Поширені проблеми](../../../03-CoreGenerativeAITechniques)

## Огляд

Цей навчальний посібник містить практичні приклади основних технік генеративного штучного інтелекту з використанням Java та моделей GitHub. Ви навчитеся взаємодіяти з великими мовними моделями (LLM), реалізовувати виклик функцій, використовувати генерацію з розширеним пошуком (RAG) та застосовувати практики відповідального штучного інтелекту.

## Попередні вимоги

Перед початком переконайтеся, що у вас є:
- Встановлена Java 21 або новіша версія
- Maven для управління залежностями
- Обліковий запис GitHub із персональним токеном доступу (PAT)

## Початок роботи

### Крок 1: Налаштуйте змінну середовища

Спочатку вам потрібно встановити токен GitHub як змінну середовища. Цей токен дозволяє отримувати доступ до моделей GitHub безкоштовно.

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

### Крок 2: Перейдіть до каталогу прикладів

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Навчальний посібник 1: Завершення та чат LLM

**Файл:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Що вчить цей приклад

Цей приклад демонструє основні механізми взаємодії з великими мовними моделями (LLM) через API OpenAI, включаючи ініціалізацію клієнта з моделями GitHub, шаблони структури повідомлень для системних і користувацьких запитів, управління станом розмови через накопичення історії повідомлень та налаштування параметрів для контролю довжини відповіді та рівня креативності.

### Основні концепції коду

#### 1. Налаштування клієнта
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Це створює з'єднання з моделями GitHub за допомогою вашого токена.

#### 2. Просте завершення
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Пам'ять розмови
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Штучний інтелект запам'ятовує попередні повідомлення лише якщо ви включаєте їх у наступні запити.

### Запуск прикладу
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Що відбувається під час запуску

1. **Просте завершення**: ШІ відповідає на питання про Java з використанням системного запиту.
2. **Багатоетапний чат**: ШІ зберігає контекст між кількома питаннями.
3. **Інтерактивний чат**: Ви можете вести реальну розмову з ШІ.

## Навчальний посібник 2: Виклик функцій

**Файл:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Що вчить цей приклад

Виклик функцій дозволяє моделям ШІ запитувати виконання зовнішніх інструментів та API через структурований протокол, де модель аналізує запити природною мовою, визначає необхідні виклики функцій із відповідними параметрами за допомогою визначень JSON Schema та обробляє отримані результати для створення контекстуальних відповідей, при цьому фактичне виконання функцій залишається під контролем розробника для забезпечення безпеки та надійності.

### Основні концепції коду

#### 1. Визначення функції
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Це повідомляє ШІ, які функції доступні та як їх використовувати.

#### 2. Потік виконання функції
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Реалізація функції
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Запуск прикладу
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Що відбувається під час запуску

1. **Функція погоди**: ШІ запитує дані про погоду в Сіетлі, ви надаєте їх, ШІ форматує відповідь.
2. **Функція калькулятора**: ШІ запитує обчислення (15% від 240), ви виконуєте його, ШІ пояснює результат.

## Навчальний посібник 3: RAG (Генерація з розширеним пошуком)

**Файл:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Що вчить цей приклад

Генерація з розширеним пошуком (RAG) поєднує пошук інформації з генерацією тексту, додаючи контекст зовнішніх документів до запитів ШІ, що дозволяє моделям надавати точні відповіді на основі конкретних джерел знань, а не потенційно застарілих або неточних даних навчання, при цьому зберігаючи чіткі межі між запитами користувача та авторитетними джерелами інформації через стратегічну інженерію запитів.

### Основні концепції коду

#### 1. Завантаження документа
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Впровадження контексту
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Три лапки допомагають ШІ розрізняти контекст і питання.

#### 3. Безпечна обробка відповідей
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Завжди перевіряйте відповіді API, щоб уникнути збоїв.

### Запуск прикладу
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Що відбувається під час запуску

1. Програма завантажує `document.txt` (містить інформацію про моделі GitHub).
2. Ви ставите питання про документ.
3. ШІ відповідає лише на основі вмісту документа, а не загальних знань.

Спробуйте запитати: "Що таке моделі GitHub?" проти "Яка погода?"

## Навчальний посібник 4: Відповідальний штучний інтелект

**Файл:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Що вчить цей приклад

Приклад відповідального ШІ демонструє важливість впровадження заходів безпеки в додатках ШІ. Він показує фільтри безпеки, які виявляють шкідливі категорії контенту, включаючи мову ненависті, переслідування, самопошкодження, сексуальний контент і насильство, демонструючи, як виробничі додатки ШІ повинні коректно обробляти порушення політики контенту через належне оброблення винятків, механізми зворотного зв'язку з користувачем і стратегії резервних відповідей.

### Основні концепції коду

#### 1. Фреймворк тестування безпеки
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Перевірені категорії безпеки
- Інструкції щодо насильства/шкоди
- Мова ненависті
- Порушення конфіденційності
- Медична дезінформація
- Незаконна діяльність

### Запуск прикладу
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Що відбувається під час запуску

Програма тестує різні шкідливі запити та показує, як система безпеки ШІ:
1. **Блокує небезпечні запити** з помилками HTTP 400.
2. **Дозволяє безпечний контент** генеруватися нормально.
3. **Захищає користувачів** від шкідливих результатів ШІ.

## Загальні шаблони у прикладах

### Шаблон автентифікації
Усі приклади використовують цей шаблон для автентифікації з моделями GitHub:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Шаблон обробки помилок
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Шаблон структури повідомлень
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Наступні кроки

[Розділ 04: Практичні приклади](../04-PracticalSamples/README.md)

## Вирішення проблем

### Поширені проблеми

**"GITHUB_TOKEN не встановлено"**
- Переконайтеся, що ви встановили змінну середовища.
- Перевірте, чи ваш токен має область `models:read`.

**"Немає відповіді від API"**
- Перевірте ваше інтернет-з'єднання.
- Переконайтеся, що ваш токен дійсний.
- Перевірте, чи не перевищили ви ліміти запитів.

**Помилки компіляції Maven**
- Переконайтеся, що у вас встановлена Java 21 або новіша версія.
- Запустіть `mvn clean compile`, щоб оновити залежності.

**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критичної інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникають внаслідок використання цього перекладу.