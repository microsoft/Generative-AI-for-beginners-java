<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:16:26+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "sr"
}
-->
# Основне Технике Генеративне Вештачке Интелигенције - Туторијал

## Садржај

- [Предуслови](../../../03-CoreGenerativeAITechniques)
- [Почетак](../../../03-CoreGenerativeAITechniques)
  - [Корак 1: Поставите променљиву окружења](../../../03-CoreGenerativeAITechniques)
  - [Корак 2: Идите у директоријум са примерима](../../../03-CoreGenerativeAITechniques)
- [Туторијал 1: LLM Завршетци и Ћаскање](../../../03-CoreGenerativeAITechniques)
- [Туторијал 2: Позивање Функција](../../../03-CoreGenerativeAITechniques)
- [Туторијал 3: RAG (Генерација са Додацима за Претраживање)](../../../03-CoreGenerativeAITechniques)
- [Туторијал 4: Одговорна Вештачка Интелигенција](../../../03-CoreGenerativeAITechniques)
- [Заједнички Обрасци у Примерима](../../../03-CoreGenerativeAITechniques)
- [Следећи Кораци](../../../03-CoreGenerativeAITechniques)
- [Решавање Проблема](../../../03-CoreGenerativeAITechniques)
  - [Уобичајени Проблеми](../../../03-CoreGenerativeAITechniques)

## Преглед

Овај туторијал пружа практичне примере основних техника генеративне вештачке интелигенције користећи Java и GitHub моделе. Научићете како да комуницирате са великим језичким моделима (LLMs), имплементирате позивање функција, користите генерацију са додацима за претраживање (RAG) и примените праксе одговорне вештачке интелигенције.

## Предуслови

Пре него што почнете, уверите се да имате:
- Инсталирану Java 21 или новију верзију
- Maven за управљање зависностима
- GitHub налог са личним приступним токеном (PAT)

## Почетак

### Корак 1: Поставите променљиву окружења

Прво, потребно је да поставите свој GitHub токен као променљиву окружења. Овај токен вам омогућава приступ GitHub моделима бесплатно.

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

### Корак 2: Идите у директоријум са примерима

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Туторијал 1: LLM Завршетци и Ћаскање

**Фајл:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Шта овај пример показује

Овај пример демонстрира основне механизме интеракције са великим језичким моделима (LLM) преко OpenAI API-ја, укључујући иницијализацију клијента са GitHub моделима, обрасце структуре порука за системске и корисничке упите, управљање стањем конверзације кроз акумулацију историје порука и подешавање параметара за контролу дужине одговора и нивоа креативности.

### Кључни Концепти Кода

#### 1. Постављање Клијента
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Ово креира везу са GitHub моделима користећи ваш токен.

#### 2. Једноставан Завршетак
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

#### 3. Меморија Конверзације
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Вештачка интелигенција памти претходне поруке само ако их укључите у наредне захтеве.

### Покрените Пример
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Шта се дешава када га покренете

1. **Једноставан Завршетак**: ВИ одговара на питање о Java-и уз смернице системског упита
2. **Вишеструко Ћаскање**: ВИ одржава контекст кроз више питања
3. **Интерактивно Ћаскање**: Можете водити прави разговор са ВИ

## Туторијал 2: Позивање Функција

**Фајл:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Шта овај пример показује

Позивање функција омогућава моделима вештачке интелигенције да захтевају извршавање спољашњих алата и API-ја кроз структуриран протокол где модел анализира захтеве на природном језику, одређује потребне позиве функција са одговарајућим параметрима користећи JSON Schema дефиниције и обрађује враћене резултате за генерисање контекстуалних одговора, док извршавање функција остаје под контролом програмера ради безбедности и поузданости.

### Кључни Концепти Кода

#### 1. Дефиниција Функције
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

Ово говори ВИ које су функције доступне и како да их користи.

#### 2. Ток Извршавања Функције
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

#### 3. Имплементација Функције
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

### Покрените Пример
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Шта се дешава када га покренете

1. **Функција за Време**: ВИ тражи податке о времену за Сијетл, ви их обезбеђујете, ВИ форматира одговор
2. **Функција Калкулатора**: ВИ тражи прорачун (15% од 240), ви га извршавате, ВИ објашњава резултат

## Туторијал 3: RAG (Генерација са Додацима за Претраживање)

**Фајл:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Шта овај пример показује

Генерација са додацима за претраживање (RAG) комбинује претраживање информација са језичком генерацијом убацивањем контекста из спољашњих докумената у упите ВИ, омогућавајући моделима да пруже тачне одговоре засноване на специфичним изворима знања уместо потенцијално застарелих или нетачних података из обуке, уз јасно раздвајање корисничких упита и ауторитативних извора информација кроз стратешко инжењерство упита.

### Кључни Концепти Кода

#### 1. Учитавање Документа
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Убацивање Контекста
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

Троструки наводници помажу ВИ да разликује контекст од питања.

#### 3. Безбедно Руковање Одговорима
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Увек валидајте одговоре API-ја како бисте спречили падове.

### Покрените Пример
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Шта се дешава када га покренете

1. Програм учитава `document.txt` (садржи информације о GitHub моделима)
2. Постављате питање о документу
3. ВИ одговара само на основу садржаја документа, а не свог општег знања

Пробајте да питате: "Шта су GitHub модели?" у односу на "Какво је време?"

## Туторијал 4: Одговорна Вештачка Интелигенција

**Фајл:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Шта овај пример показује

Пример одговорне ВИ истиче важност примене мера безбедности у ВИ апликацијама. Демонстрира безбедносне филтере који откривају категорије штетног садржаја, укључујући говор мржње, узнемиравање, самоповређивање, сексуални садржај и насиље, показујући како производне ВИ апликације треба да се носе са кршењем политике садржаја кроз правилно руковање изузецима, механизме повратних информација корисника и стратегије резервних одговора.

### Кључни Концепти Кода

#### 1. Оквир за Тестирање Безбедности
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

#### 2. Тестиране Категорије Безбедности
- Упутства за насиље/штету
- Говор мржње
- Повреде приватности
- Медицинске дезинформације
- Незаконите активности

### Покрените Пример
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Шта се дешава када га покренете

Програм тестира различите штетне упите и показује како систем безбедности ВИ:
1. **Блокира опасне захтеве** са HTTP 400 грешкама
2. **Дозвољава безбедан садржај** да се генерише нормално
3. **Штити кориснике** од штетних излаза ВИ

## Заједнички Обрасци у Примерима

### Образац Аутентификације
Сви примери користе овај образац за аутентификацију са GitHub моделима:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Образац Руковања Грешкама
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Образац Структуре Порука
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Следећи Кораци

[Поглавље 04: Практични примери](../04-PracticalSamples/README.md)

## Решавање Проблема

### Уобичајени Проблеми

**"GITHUB_TOKEN није постављен"**
- Уверите се да сте поставили променљиву окружења
- Проверите да ли ваш токен има `models:read` дозволу

**"Нема одговора од API-ја"**
- Проверите интернет конекцију
- Уверите се да је ваш токен важећи
- Проверите да ли сте достигли ограничења у коришћењу

**Грешке при компилацији у Maven-у**
- Уверите се да имате Java 21 или новију верзију
- Покрените `mvn clean compile` да освежите зависности

**Одрицање од одговорности**:  
Овај документ је преведен коришћењем услуге за превођење помоћу вештачке интелигенције [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да обезбедимо тачност, молимо вас да имате у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални превод од стране људи. Не сносимо одговорност за било каква погрешна тумачења или неспоразуме који могу произаћи из коришћења овог превода.