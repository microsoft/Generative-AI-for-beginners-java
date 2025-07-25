<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T10:41:00+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "ru"
}
-->
# Основные техники генеративного ИИ: Учебное пособие

## Содержание

- [Предварительные требования](../../../03-CoreGenerativeAITechniques)
- [Начало работы](../../../03-CoreGenerativeAITechniques)
  - [Шаг 1: Установите переменную окружения](../../../03-CoreGenerativeAITechniques)
  - [Шаг 2: Перейдите в директорию с примерами](../../../03-CoreGenerativeAITechniques)
- [Учебное пособие 1: Завершения и чат с LLM](../../../03-CoreGenerativeAITechniques)
- [Учебное пособие 2: Вызов функций](../../../03-CoreGenerativeAITechniques)
- [Учебное пособие 3: RAG (Генерация с дополнением поиска)](../../../03-CoreGenerativeAITechniques)
- [Учебное пособие 4: Ответственный ИИ](../../../03-CoreGenerativeAITechniques)
- [Общие шаблоны в примерах](../../../03-CoreGenerativeAITechniques)
- [Следующие шаги](../../../03-CoreGenerativeAITechniques)
- [Устранение неполадок](../../../03-CoreGenerativeAITechniques)
  - [Распространенные проблемы](../../../03-CoreGenerativeAITechniques)

## Обзор

Это учебное пособие предоставляет практические примеры основных техник генеративного ИИ с использованием Java и GitHub Models. Вы научитесь взаимодействовать с большими языковыми моделями (LLM), реализовывать вызов функций, использовать генерацию с дополнением поиска (RAG) и применять принципы ответственного ИИ.

## Предварительные требования

Перед началом убедитесь, что у вас есть:
- Установленная Java версии 21 или выше
- Maven для управления зависимостями
- Аккаунт на GitHub с персональным токеном доступа (PAT)

## Начало работы

### Шаг 1: Установите переменную окружения

Сначала необходимо установить ваш GitHub-токен в качестве переменной окружения. Этот токен позволяет вам бесплатно использовать GitHub Models.

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

### Шаг 2: Перейдите в директорию с примерами

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Учебное пособие 1: Завершения и чат с LLM

**Файл:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Чему учит этот пример

Этот пример демонстрирует основные механики взаимодействия с большими языковыми моделями (LLM) через OpenAI API, включая инициализацию клиента с использованием GitHub Models, шаблоны структуры сообщений для системных и пользовательских запросов, управление состоянием диалога через накопление истории сообщений и настройку параметров для контроля длины ответа и уровня креативности.

### Основные концепции кода

#### 1. Настройка клиента
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Это создает подключение к GitHub Models с использованием вашего токена.

#### 2. Простое завершение
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

#### 3. Память диалога
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

ИИ запоминает предыдущие сообщения только в том случае, если вы включаете их в последующие запросы.

### Запуск примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Что происходит при запуске

1. **Простое завершение**: ИИ отвечает на вопрос по Java с учетом системного запроса.
2. **Многоходовой чат**: ИИ сохраняет контекст при нескольких вопросах.
3. **Интерактивный чат**: Вы можете вести реальный диалог с ИИ.

## Учебное пособие 2: Вызов функций

**Файл:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Чему учит этот пример

Вызов функций позволяет моделям ИИ запрашивать выполнение внешних инструментов и API через структурированный протокол, где модель анализирует запросы на естественном языке, определяет необходимые вызовы функций с соответствующими параметрами, используя определения JSON Schema, и обрабатывает возвращенные результаты для генерации контекстуальных ответов, при этом выполнение функций остается под контролем разработчика для обеспечения безопасности и надежности.

### Основные концепции кода

#### 1. Определение функции
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

Это сообщает ИИ, какие функции доступны и как их использовать.

#### 2. Поток выполнения функции
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

#### 3. Реализация функции
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

### Запуск примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Что происходит при запуске

1. **Функция погоды**: ИИ запрашивает данные о погоде в Сиэтле, вы предоставляете их, ИИ форматирует ответ.
2. **Функция калькулятора**: ИИ запрашивает расчет (15% от 240), вы вычисляете, ИИ объясняет результат.

## Учебное пособие 3: RAG (Генерация с дополнением поиска)

**Файл:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Чему учит этот пример

Генерация с дополнением поиска (RAG) сочетает извлечение информации с языковой генерацией, добавляя контекст из внешних документов в запросы ИИ. Это позволяет моделям предоставлять точные ответы на основе конкретных источников знаний, а не устаревших или неточных данных обучения, сохраняя четкие границы между пользовательскими запросами и авторитетной информацией через стратегическое проектирование запросов.

### Основные концепции кода

#### 1. Загрузка документа
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Внедрение контекста
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

Тройные кавычки помогают ИИ различать контекст и вопрос.

#### 3. Безопасная обработка ответов
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Всегда проверяйте ответы API, чтобы избежать сбоев.

### Запуск примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Что происходит при запуске

1. Программа загружает `document.txt` (содержит информацию о GitHub Models).
2. Вы задаете вопрос о документе.
3. ИИ отвечает, основываясь только на содержимом документа, а не на своих общих знаниях.

Попробуйте спросить: "Что такое GitHub Models?" и "Какая погода?"

## Учебное пособие 4: Ответственный ИИ

**Файл:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Чему учит этот пример

Пример ответственного ИИ подчеркивает важность внедрения мер безопасности в приложениях ИИ. Он демонстрирует фильтры безопасности, которые обнаруживают вредоносные категории контента, включая разжигание ненависти, домогательства, самоповреждения, сексуальный контент и насилие, показывая, как производственные приложения ИИ должны корректно обрабатывать нарушения политики контента через обработку исключений, механизмы обратной связи с пользователем и стратегии резервных ответов.

### Основные концепции кода

#### 1. Фреймворк тестирования безопасности
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

#### 2. Тестируемые категории безопасности
- Инструкции по насилию/вреду
- Разжигание ненависти
- Нарушения конфиденциальности
- Медицинская дезинформация
- Незаконные действия

### Запуск примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Что происходит при запуске

Программа тестирует различные вредоносные запросы и показывает, как система безопасности ИИ:
1. **Блокирует опасные запросы** с ошибками HTTP 400.
2. **Разрешает безопасный контент** генерироваться нормально.
3. **Защищает пользователей** от вредоносных выходных данных ИИ.

## Общие шаблоны в примерах

### Шаблон аутентификации
Все примеры используют этот шаблон для аутентификации с GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Шаблон обработки ошибок
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Шаблон структуры сообщений
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Следующие шаги

[Глава 04: Практические примеры](../04-PracticalSamples/README.md)

## Устранение неполадок

### Распространенные проблемы

**"GITHUB_TOKEN не установлен"**
- Убедитесь, что вы установили переменную окружения.
- Проверьте, что у вашего токена есть область `models:read`.

**"Нет ответа от API"**
- Проверьте подключение к интернету.
- Убедитесь, что ваш токен действителен.
- Проверьте, не превысили ли вы лимиты запросов.

**Ошибки компиляции Maven**
- Убедитесь, что у вас установлена Java версии 21 или выше.
- Запустите `mvn clean compile`, чтобы обновить зависимости.

**Отказ от ответственности**:  
Этот документ был переведен с использованием сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия обеспечить точность, автоматические переводы могут содержать ошибки или неточности. Оригинальный документ на его родном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные интерпретации, возникающие в результате использования данного перевода.