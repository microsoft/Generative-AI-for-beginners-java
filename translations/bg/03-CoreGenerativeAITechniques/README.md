<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:13:45+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "bg"
}
-->
# Основни техники за генерираща AI: Ръководство

## Съдържание

- [Предварителни условия](../../../03-CoreGenerativeAITechniques)
- [Първи стъпки](../../../03-CoreGenerativeAITechniques)
  - [Стъпка 1: Настройте променливата на средата](../../../03-CoreGenerativeAITechniques)
  - [Стъпка 2: Навигирайте до директорията с примери](../../../03-CoreGenerativeAITechniques)
- [Ръководство 1: Завършвания и чат с LLM](../../../03-CoreGenerativeAITechniques)
- [Ръководство 2: Извикване на функции](../../../03-CoreGenerativeAITechniques)
- [Ръководство 3: RAG (Генериране с подсилено извличане)](../../../03-CoreGenerativeAITechniques)
- [Ръководство 4: Отговорен AI](../../../03-CoreGenerativeAITechniques)
- [Общи модели в примерите](../../../03-CoreGenerativeAITechniques)
- [Следващи стъпки](../../../03-CoreGenerativeAITechniques)
- [Отстраняване на проблеми](../../../03-CoreGenerativeAITechniques)
  - [Чести проблеми](../../../03-CoreGenerativeAITechniques)

## Преглед

Това ръководство предоставя практически примери за основни техники за генерираща AI, използвайки Java и GitHub Models. Ще научите как да взаимодействате с големи езикови модели (LLMs), да внедрявате извикване на функции, да използвате генериране с подсилено извличане (RAG) и да прилагате практики за отговорен AI.

## Предварителни условия

Преди да започнете, уверете се, че имате:
- Инсталиран Java 21 или по-нов
- Maven за управление на зависимости
- GitHub акаунт с персонален токен за достъп (PAT)

## Първи стъпки

### Стъпка 1: Настройте променливата на средата

Първо, трябва да зададете вашия GitHub токен като променлива на средата. Този токен ви позволява да използвате GitHub Models безплатно.

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

### Стъпка 2: Навигирайте до директорията с примери

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Ръководство 1: Завършвания и чат с LLM

**Файл:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Какво учи този пример

Този пример демонстрира основните механизми на взаимодействие с големи езикови модели (LLM) чрез OpenAI API, включително инициализация на клиент с GitHub Models, модели на структура на съобщения за системни и потребителски подсказки, управление на състоянието на разговора чрез натрупване на история на съобщенията и настройка на параметри за контролиране на дължината на отговорите и нивото на креативност.

### Основни концепции в кода

#### 1. Настройка на клиента
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Това създава връзка с GitHub Models, използвайки вашия токен.

#### 2. Просто завършване
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

#### 3. Памет на разговора
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI запомня предишни съобщения само ако ги включите в следващите заявки.

### Стартирайте примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Какво се случва, когато го стартирате

1. **Просто завършване**: AI отговаря на въпрос за Java с насоки от системната подсказка.
2. **Многократен чат**: AI поддържа контекст през множество въпроси.
3. **Интерактивен чат**: Можете да проведете реален разговор с AI.

## Ръководство 2: Извикване на функции

**Файл:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Какво учи този пример

Извикването на функции позволява на AI моделите да заявяват изпълнение на външни инструменти и API чрез структуриран протокол, при който моделът анализира заявки на естествен език, определя необходимите извиквания на функции с подходящи параметри, използвайки JSON Schema дефиниции, и обработва върнатите резултати, за да генерира контекстуални отговори, докато реалното изпълнение на функциите остава под контрола на разработчика за сигурност и надеждност.

### Основни концепции в кода

#### 1. Дефиниция на функция
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

Това казва на AI какви функции са налични и как да ги използва.

#### 2. Поток на изпълнение на функции
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

#### 3. Имплементация на функции
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

### Стартирайте примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Какво се случва, когато го стартирате

1. **Функция за времето**: AI заявява данни за времето в Сиатъл, вие ги предоставяте, AI форматира отговор.
2. **Функция калкулатор**: AI заявява изчисление (15% от 240), вие го изчислявате, AI обяснява резултата.

## Ръководство 3: RAG (Генериране с подсилено извличане)

**Файл:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Какво учи този пример

Генерирането с подсилено извличане (RAG) комбинира извличане на информация с езиково генериране, като инжектира контекст от външни документи в AI подсказки, позволявайки на моделите да предоставят точни отговори, базирани на специфични източници на знания, вместо на потенциално остарели или неточни данни от обучението, като същевременно поддържа ясни граници между потребителските въпроси и авторитетните източници на информация чрез стратегическо инженерство на подсказки.

### Основни концепции в кода

#### 1. Зареждане на документи
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Инжектиране на контекст
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

Тройните кавички помагат на AI да различи контекста от въпроса.

#### 3. Безопасно обработване на отговори
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Винаги валидирайте API отговорите, за да предотвратите сривове.

### Стартирайте примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Какво се случва, когато го стартирате

1. Програмата зарежда `document.txt` (съдържа информация за GitHub Models).
2. Задавате въпрос за документа.
3. AI отговаря само въз основа на съдържанието на документа, а не на общите си знания.

Опитайте да попитате: "Какво представляват GitHub Models?" срещу "Какво е времето?"

## Ръководство 4: Отговорен AI

**Файл:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Какво учи този пример

Примерът за отговорен AI показва важността на внедряването на мерки за безопасност в AI приложенията. Демонстрира филтри за безопасност, които откриват вредни категории съдържание, включително реч на омраза, тормоз, самонараняване, сексуално съдържание и насилие, показвайки как производствените AI приложения трябва да се справят с нарушения на политиката за съдържание чрез подходящо обработване на изключения, механизми за обратна връзка от потребителите и стратегии за резервни отговори.

### Основни концепции в кода

#### 1. Рамка за тестване на безопасност
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

#### 2. Тествани категории за безопасност
- Инструкции за насилие/вреда
- Реч на омраза
- Нарушения на поверителността
- Медицинска дезинформация
- Незаконни дейности

### Стартирайте примера
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Какво се случва, когато го стартирате

Програмата тества различни вредни подсказки и показва как системата за безопасност на AI:
1. **Блокира опасни заявки** с HTTP 400 грешки.
2. **Позволява безопасно съдържание** да се генерира нормално.
3. **Защитава потребителите** от вредни AI изходи.

## Общи модели в примерите

### Модел за удостоверяване
Всички примери използват този модел за удостоверяване с GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Модел за обработка на грешки
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Модел за структура на съобщенията
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Следващи стъпки

[Глава 04: Практически примери](../04-PracticalSamples/README.md)

## Отстраняване на проблеми

### Чести проблеми

**"GITHUB_TOKEN не е зададен"**
- Уверете се, че сте задали променливата на средата.
- Проверете дали вашият токен има обхват `models:read`.

**"Няма отговор от API"**
- Проверете интернет връзката си.
- Уверете се, че вашият токен е валиден.
- Проверете дали сте достигнали лимитите за заявки.

**Грешки при компилация с Maven**
- Уверете се, че имате Java 21 или по-нов.
- Стартирайте `mvn clean compile`, за да обновите зависимостите.

**Отказ от отговорност**:  
Този документ е преведен с помощта на AI услуга за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Не носим отговорност за недоразумения или погрешни интерпретации, произтичащи от използването на този превод.