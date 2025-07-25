<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:16:20+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "pa"
}
-->
# ਕੋਰ ਜਨਰੇਟਿਵ AI ਤਕਨੀਕਾਂ ਟਿਊਟੋਰਿਅਲ

## ਸੂਚੀ

- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../03-CoreGenerativeAITechniques)
- [ਸ਼ੁਰੂਆਤ ਕਰਨਾ](../../../03-CoreGenerativeAITechniques)
  - [ਪਹਿਲਾ ਕਦਮ: ਆਪਣਾ ਐਨਵਾਇਰਮੈਂਟ ਵੈਰੀਏਬਲ ਸੈਟ ਕਰੋ](../../../03-CoreGenerativeAITechniques)
  - [ਦੂਜਾ ਕਦਮ: ਉਦਾਹਰਣਾਂ ਵਾਲੇ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ ਜਾਓ](../../../03-CoreGenerativeAITechniques)
- [ਟਿਊਟੋਰਿਅਲ 1: LLM ਕੰਪਲੀਸ਼ਨ ਅਤੇ ਚੈਟ](../../../03-CoreGenerativeAITechniques)
- [ਟਿਊਟੋਰਿਅਲ 2: ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ](../../../03-CoreGenerativeAITechniques)
- [ਟਿਊਟੋਰਿਅਲ 3: RAG (ਰੀਟਰੀਵਲ-ਅਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ)](../../../03-CoreGenerativeAITechniques)
- [ਟਿਊਟੋਰਿਅਲ 4: ਜ਼ਿੰਮੇਵਾਰ AI](../../../03-CoreGenerativeAITechniques)
- [ਉਦਾਹਰਣਾਂ ਵਿੱਚ ਆਮ ਪੈਟਰਨ](../../../03-CoreGenerativeAITechniques)
- [ਅਗਲੇ ਕਦਮ](../../../03-CoreGenerativeAITechniques)
- [ਟ੍ਰਬਲਸ਼ੂਟਿੰਗ](../../../03-CoreGenerativeAITechniques)
  - [ਆਮ ਸਮੱਸਿਆਵਾਂ](../../../03-CoreGenerativeAITechniques)

## ਝਲਕ

ਇਹ ਟਿਊਟੋਰਿਅਲ ਜਾਵਾ ਅਤੇ GitHub ਮਾਡਲਾਂ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੋਰ ਜਨਰੇਟਿਵ AI ਤਕਨੀਕਾਂ ਦੇ ਹੱਥ-ਅਨੁਭਵ ਵਾਲੇ ਉਦਾਹਰਣ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ। ਤੁਸੀਂ ਸਿੱਖੋਗੇ ਕਿ ਵੱਡੇ ਭਾਸ਼ਾ ਮਾਡਲਾਂ (LLMs) ਨਾਲ ਕਿਵੇਂ ਇੰਟਰੈਕਟ ਕਰਨਾ ਹੈ, ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ ਨੂੰ ਕਿਵੇਂ ਲਾਗੂ ਕਰਨਾ ਹੈ, ਰੀਟਰੀਵਲ-ਅਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ (RAG) ਦੀ ਵਰਤੋਂ ਕਰਨੀ ਹੈ, ਅਤੇ ਜ਼ਿੰਮੇਵਾਰ AI ਅਭਿਆਸਾਂ ਨੂੰ ਕਿਵੇਂ ਲਾਗੂ ਕਰਨਾ ਹੈ।

## ਪੂਰਵ ਸ਼ਰਤਾਂ

ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਇਹ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ ਹੈ:
- ਜਾਵਾ 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ ਵਰਜਨ ਇੰਸਟਾਲ ਹੈ
- Maven ਡਿਪੈਂਡੈਂਸੀ ਮੈਨੇਜਮੈਂਟ ਲਈ
- ਇੱਕ GitHub ਖਾਤਾ ਜਿਸ ਵਿੱਚ ਪర్సਨਲ ਐਕਸੈਸ ਟੋਕਨ (PAT) ਹੈ

## ਸ਼ੁਰੂਆਤ ਕਰਨਾ

### ਪਹਿਲਾ ਕਦਮ: ਆਪਣਾ ਐਨਵਾਇਰਮੈਂਟ ਵੈਰੀਏਬਲ ਸੈਟ ਕਰੋ

ਸਭ ਤੋਂ ਪਹਿਲਾਂ, ਤੁਹਾਨੂੰ ਆਪਣਾ GitHub ਟੋਕਨ ਇੱਕ ਐਨਵਾਇਰਮੈਂਟ ਵੈਰੀਏਬਲ ਵਜੋਂ ਸੈਟ ਕਰਨਾ ਹੋਵੇਗਾ। ਇਹ ਟੋਕਨ ਤੁਹਾਨੂੰ ਮੁਫ਼ਤ ਵਿੱਚ GitHub ਮਾਡਲਾਂ ਤੱਕ ਪਹੁੰਚ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ।

**Windows (ਕਮਾਂਡ ਪ੍ਰਾਂਪਟ):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (ਪਾਵਰਸ਼ੈਲ):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### ਦੂਜਾ ਕਦਮ: ਉਦਾਹਰਣਾਂ ਵਾਲੇ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ ਜਾਓ

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## ਟਿਊਟੋਰਿਅਲ 1: LLM ਕੰਪਲੀਸ਼ਨ ਅਤੇ ਚੈਟ

**ਫਾਈਲ:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### ਇਹ ਉਦਾਹਰਣ ਕੀ ਸਿਖਾਉਂਦਾ ਹੈ

ਇਹ ਉਦਾਹਰਣ OpenAI API ਰਾਹੀਂ ਵੱਡੇ ਭਾਸ਼ਾ ਮਾਡਲ (LLM) ਨਾਲ ਇੰਟਰੈਕਸ਼ਨ ਦੇ ਮੁੱਖ ਮਕੈਨਿਕਸ ਨੂੰ ਦਰਸਾਉਂਦਾ ਹੈ, ਜਿਸ ਵਿੱਚ GitHub ਮਾਡਲਾਂ ਨਾਲ ਕਲਾਇੰਟ ਇਨੀਸ਼ੀਅਲਾਈਜ਼ੇਸ਼ਨ, ਸਿਸਟਮ ਅਤੇ ਯੂਜ਼ਰ ਪ੍ਰਾਂਪਟਾਂ ਲਈ ਸੁਨੇਹਾ ਸਟ੍ਰਕਚਰ ਪੈਟਰਨ, ਸੁਨੇਹਾ ਇਤਿਹਾਸ ਸੰਭਾਲਣ ਲਈ ਗੱਲਬਾਤ ਦੀ ਸਥਿਤੀ ਪ੍ਰਬੰਧਨ, ਅਤੇ ਜਵਾਬ ਦੀ ਲੰਬਾਈ ਅਤੇ ਕ੍ਰੀਏਟਿਵਿਟੀ ਪੱਧਰ ਨੂੰ ਨਿਯੰਤਰਿਤ ਕਰਨ ਲਈ ਪੈਰਾਮੀਟਰ ਟਿਊਨਿੰਗ ਸ਼ਾਮਲ ਹੈ।

### ਮੁੱਖ ਕੋਡ ਸੰਕਲਪ

#### 1. ਕਲਾਇੰਟ ਸੈਟਅੱਪ
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

ਇਹ ਤੁਹਾਡੇ ਟੋਕਨ ਦੀ ਵਰਤੋਂ ਕਰਕੇ GitHub ਮਾਡਲਾਂ ਨਾਲ ਕਨੈਕਸ਼ਨ ਬਣਾਉਂਦਾ ਹੈ।

#### 2. ਸਧਾਰਨ ਕੰਪਲੀਸ਼ਨ
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

#### 3. ਗੱਲਬਾਤ ਮੈਮੋਰੀ
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI ਪਿਛਲੇ ਸੁਨੇਹਿਆਂ ਨੂੰ ਸਿਰਫ਼ ਉਸ ਸਮੇਂ ਯਾਦ ਰੱਖਦਾ ਹੈ ਜਦੋਂ ਤੁਸੀਂ ਉਹਨਾਂ ਨੂੰ ਅਗਲੇ ਰਿਕਵੈਸਟ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰਦੇ ਹੋ।

### ਉਦਾਹਰਣ ਚਲਾਓ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### ਜਦੋਂ ਤੁਸੀਂ ਇਸਨੂੰ ਚਲਾਉਂਦੇ ਹੋ ਤਾਂ ਕੀ ਹੁੰਦਾ ਹੈ

1. **ਸਧਾਰਨ ਕੰਪਲੀਸ਼ਨ**: AI ਸਿਸਟਮ ਪ੍ਰਾਂਪਟ ਗਾਈਡੈਂਸ ਨਾਲ ਜਾਵਾ ਸਵਾਲ ਦਾ ਜਵਾਬ ਦਿੰਦਾ ਹੈ
2. **ਮਲਟੀ-ਟਰਨ ਚੈਟ**: AI ਕਈ ਸਵਾਲਾਂ ਦੇ ਦੌਰਾਨ ਸੰਦਰਭ ਨੂੰ ਬਰਕਰਾਰ ਰੱਖਦਾ ਹੈ
3. **ਇੰਟਰੈਕਟਿਵ ਚੈਟ**: ਤੁਸੀਂ AI ਨਾਲ ਅਸਲ ਗੱਲਬਾਤ ਕਰ ਸਕਦੇ ਹੋ

## ਟਿਊਟੋਰਿਅਲ 2: ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ

**ਫਾਈਲ:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### ਇਹ ਉਦਾਹਰਣ ਕੀ ਸਿਖਾਉਂਦਾ ਹੈ

ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ AI ਮਾਡਲਾਂ ਨੂੰ ਬਾਹਰੀ ਟੂਲਾਂ ਅਤੇ APIs ਦੇ ਕਾਰਜਨਵੈਨ ਲਈ ਬੇਨਤੀ ਕਰਨ ਯੋਗ ਬਣਾਉਂਦਾ ਹੈ। ਇਹ ਇੱਕ ਸਟ੍ਰਕਚਰਡ ਪ੍ਰੋਟੋਕੋਲ ਰਾਹੀਂ ਕੰਮ ਕਰਦਾ ਹੈ, ਜਿੱਥੇ ਮਾਡਲ ਕੁਦਰਤੀ ਭਾਸ਼ਾ ਬੇਨਤੀਆਂ ਦਾ ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਦਾ ਹੈ, JSON Schema ਡਿਫਿਨੀਸ਼ਨਾਂ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਲੋੜੀਂਦੇ ਫੰਕਸ਼ਨ ਕਾਲਾਂ ਅਤੇ ਪੈਰਾਮੀਟਰਾਂ ਨੂੰ ਨਿਰਧਾਰਤ ਕਰਦਾ ਹੈ, ਅਤੇ ਪ੍ਰਾਪਤ ਨਤੀਜਿਆਂ ਨੂੰ ਪ੍ਰਸੰਗਿਕ ਜਵਾਬ ਬਣਾਉਣ ਲਈ ਪ੍ਰਕਿਰਿਆ ਕਰਦਾ ਹੈ। ਫੰਕਸ਼ਨ ਦੀ ਅਸਲ ਕਾਰਜਨਵੈਤੀ ਡਿਵੈਲਪਰ ਦੇ ਨਿਯੰਤਰਣ ਹੇਠ ਰਹਿੰਦੀ ਹੈ ਤਾਂ ਜੋ ਸੁਰੱਖਿਆ ਅਤੇ ਭਰੋਸੇਯੋਗਤਾ ਨੂੰ ਯਕੀਨੀ ਬਣਾਇਆ ਜਾ ਸਕੇ।

### ਮੁੱਖ ਕੋਡ ਸੰਕਲਪ

#### 1. ਫੰਕਸ਼ਨ ਡਿਫਿਨੀਸ਼ਨ
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

ਇਹ AI ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਕਿਹੜੇ ਫੰਕਸ਼ਨ ਉਪਲਬਧ ਹਨ ਅਤੇ ਉਹਨਾਂ ਨੂੰ ਕਿਵੇਂ ਵਰਤਣਾ ਹੈ।

#### 2. ਫੰਕਸ਼ਨ ਕਾਰਜਨਵੈਤੀ ਦਾ ਪ੍ਰਵਾਹ
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

#### 3. ਫੰਕਸ਼ਨ ਇੰਪਲੀਮੈਂਟੇਸ਼ਨ
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

### ਉਦਾਹਰਣ ਚਲਾਓ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### ਜਦੋਂ ਤੁਸੀਂ ਇਸਨੂੰ ਚਲਾਉਂਦੇ ਹੋ ਤਾਂ ਕੀ ਹੁੰਦਾ ਹੈ

1. **ਮੌਸਮ ਫੰਕਸ਼ਨ**: AI ਸਿਆਟਲ ਲਈ ਮੌਸਮ ਡਾਟਾ ਮੰਗਦਾ ਹੈ, ਤੁਸੀਂ ਪ੍ਰਦਾਨ ਕਰਦੇ ਹੋ, AI ਜਵਾਬ ਫਾਰਮੈਟ ਕਰਦਾ ਹੈ
2. **ਕੈਲਕੂਲੇਟਰ ਫੰਕਸ਼ਨ**: AI ਇੱਕ ਗਣਨਾ (240 ਦਾ 15%) ਮੰਗਦਾ ਹੈ, ਤੁਸੀਂ ਇਸਨੂੰ ਗਣਨਾ ਕਰਦੇ ਹੋ, AI ਨਤੀਜੇ ਨੂੰ ਸਮਝਾਉਂਦਾ ਹੈ

## ਟਿਊਟੋਰਿਅਲ 3: RAG (ਰੀਟਰੀਵਲ-ਅਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ)

**ਫਾਈਲ:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### ਇਹ ਉਦਾਹਰਣ ਕੀ ਸਿਖਾਉਂਦਾ ਹੈ

ਰੀਟਰੀਵਲ-ਅਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ (RAG) ਜਾਣਕਾਰੀ ਰੀਟਰੀਵਲ ਨੂੰ ਭਾਸ਼ਾ ਜਨਰੇਸ਼ਨ ਨਾਲ ਜੋੜਦਾ ਹੈ। ਇਹ ਬਾਹਰੀ ਦਸਤਾਵੇਜ਼ ਸੰਦਰਭ ਨੂੰ AI ਪ੍ਰਾਂਪਟਾਂ ਵਿੱਚ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ, ਮਾਡਲਾਂ ਨੂੰ ਖਾਸ ਗਿਆਨ ਸਰੋਤਾਂ ਦੇ ਆਧਾਰ 'ਤੇ ਸਹੀ ਜਵਾਬ ਪ੍ਰਦਾਨ ਕਰਨ ਯੋਗ ਬਣਾਉਂਦਾ ਹੈ। ਇਹ ਸਟ੍ਰੈਟਜਿਕ ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ ਰਾਹੀਂ ਯੂਜ਼ਰ ਦੀਆਂ ਪੁੱਛਤਾਛਾਂ ਅਤੇ ਅਧਿਕਾਰਤ ਜਾਣਕਾਰੀ ਸਰੋਤਾਂ ਦੇ ਵਿਚਕਾਰ ਸਪਸ਼ਟ ਹੱਦਾਂ ਬਣਾਉਂਦਾ ਹੈ।

### ਮੁੱਖ ਕੋਡ ਸੰਕਲਪ

#### 1. ਦਸਤਾਵੇਜ਼ ਲੋਡ ਕਰਨਾ
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. ਸੰਦਰਭ ਸ਼ਾਮਲ ਕਰਨਾ
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

ਟ੍ਰਿਪਲ ਕੋਟ AI ਨੂੰ ਸੰਦਰਭ ਅਤੇ ਸਵਾਲ ਵਿੱਚ ਅੰਤਰ ਕਰਨ ਵਿੱਚ ਮਦਦ ਕਰਦੇ ਹਨ।

#### 3. ਸੁਰੱਖਿਅਤ ਜਵਾਬ ਸੰਭਾਲਣਾ
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API ਜਵਾਬਾਂ ਨੂੰ ਹਮੇਸ਼ਾ ਵੈਧ ਕਰੋ ਤਾਂ ਜੋ ਕ੍ਰੈਸ਼ ਤੋਂ ਬਚਿਆ ਜਾ ਸਕੇ।

### ਉਦਾਹਰਣ ਚਲਾਓ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### ਜਦੋਂ ਤੁਸੀਂ ਇਸਨੂੰ ਚਲਾਉਂਦੇ ਹੋ ਤਾਂ ਕੀ ਹੁੰਦਾ ਹੈ

1. ਪ੍ਰੋਗਰਾਮ `document.txt` ਲੋਡ ਕਰਦਾ ਹੈ (ਜਿਸ ਵਿੱਚ GitHub ਮਾਡਲਾਂ ਬਾਰੇ ਜਾਣਕਾਰੀ ਹੈ)
2. ਤੁਸੀਂ ਦਸਤਾਵੇਜ਼ ਬਾਰੇ ਸਵਾਲ ਪੁੱਛਦੇ ਹੋ
3. AI ਸਿਰਫ਼ ਦਸਤਾਵੇਜ਼ ਦੀ ਸਮੱਗਰੀ ਦੇ ਆਧਾਰ 'ਤੇ ਜਵਾਬ ਦਿੰਦਾ ਹੈ, ਆਪਣੇ ਆਮ ਗਿਆਨ ਦੇ ਆਧਾਰ 'ਤੇ ਨਹੀਂ

ਇਹ ਪੁੱਛਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ: "GitHub ਮਾਡਲ ਕੀ ਹਨ?" ਬਨਾਮ "ਮੌਸਮ ਕਿਵੇਂ ਹੈ?"

## ਟਿਊਟੋਰਿਅਲ 4: ਜ਼ਿੰਮੇਵਾਰ AI

**ਫਾਈਲ:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### ਇਹ ਉਦਾਹਰਣ ਕੀ ਸਿਖਾਉਂਦਾ ਹੈ

ਜ਼ਿੰਮੇਵਾਰ AI ਦਾ ਉਦਾਹਰਣ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਵਿੱਚ ਸੁਰੱਖਿਆ ਉਪਾਅ ਲਾਗੂ ਕਰਨ ਦੀ ਮਹੱਤਤਾ ਨੂੰ ਦਰਸਾਉਂਦਾ ਹੈ। ਇਹ ਸੁਰੱਖਿਆ ਫਿਲਟਰਾਂ ਨੂੰ ਦਰਸਾਉਂਦਾ ਹੈ ਜੋ ਹੇਟ ਸਪੀਚ, ਹਰੇਸਮੈਂਟ, ਸਵੈ-ਨੁਕਸਾਨ, ਯੌਨ ਸਮੱਗਰੀ, ਅਤੇ ਹਿੰਸਾ ਸਮੇਤ ਹਾਨਿਕਾਰਕ ਸਮੱਗਰੀ ਸ਼੍ਰੇਣੀਆਂ ਦੀ ਪਛਾਣ ਕਰਦੇ ਹਨ। ਇਹ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ ਉਤਪਾਦਨ AI ਐਪਲੀਕੇਸ਼ਨਾਂ ਨੂੰ ਸਮੱਗਰੀ ਨੀਤੀ ਦੇ ਉਲੰਘਨਾਂ ਨੂੰ ਕਿਵੇਂ ਸੁਚੱਜੇ ਢੰਗ ਨਾਲ ਸੰਭਾਲਣਾ ਚਾਹੀਦਾ ਹੈ, ਜਿਸ ਵਿੱਚ ਸਹੀ ਐਕਸਪਸ਼ਨ ਹੈਂਡਲਿੰਗ, ਯੂਜ਼ਰ ਫੀਡਬੈਕ ਮਕੈਨਿਜ਼ਮ, ਅਤੇ ਫਾਲਬੈਕ ਜਵਾਬ ਰਣਨੀਤੀਆਂ ਸ਼ਾਮਲ ਹਨ।

### ਮੁੱਖ ਕੋਡ ਸੰਕਲਪ

#### 1. ਸੁਰੱਖਿਆ ਟੈਸਟਿੰਗ ਫਰੇਮਵਰਕ
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

#### 2. ਟੈਸਟ ਕੀਤੀਆਂ ਸੁਰੱਖਿਆ ਸ਼੍ਰੇਣੀਆਂ
- ਹਿੰਸਾ/ਨੁਕਸਾਨ ਦੇ ਨਿਰਦੇਸ਼
- ਹੇਟ ਸਪੀਚ
- ਗੋਪਨੀਯਤਾ ਦੇ ਉਲੰਘਨ
- ਮੈਡੀਕਲ ਗਲਤ ਜਾਣਕਾਰੀ
- ਗੈਰਕਾਨੂੰਨੀ ਗਤੀਵਿਧੀਆਂ

### ਉਦਾਹਰਣ ਚਲਾਓ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### ਜਦੋਂ ਤੁਸੀਂ ਇਸਨੂੰ ਚਲਾਉਂਦੇ ਹੋ ਤਾਂ ਕੀ ਹੁੰਦਾ ਹੈ

ਪ੍ਰੋਗਰਾਮ ਵੱਖ-ਵੱਖ ਹਾਨਿਕਾਰਕ ਪ੍ਰਾਂਪਟਾਂ ਦੀ ਜਾਂਚ ਕਰਦਾ ਹੈ ਅਤੇ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ AI ਸੁਰੱਖਿਆ ਸਿਸਟਮ ਕਿਵੇਂ:
1. **ਖਤਰਨਾਕ ਬੇਨਤੀਆਂ ਨੂੰ ਰੋਕਦਾ ਹੈ** HTTP 400 ਗਲਤੀਆਂ ਨਾਲ
2. **ਸੁਰੱਖਿਅਤ ਸਮੱਗਰੀ ਨੂੰ ਆਮ ਤੌਰ 'ਤੇ ਜਨਰੇਟ ਕਰਨ ਦਿੰਦਾ ਹੈ**
3. **ਯੂਜ਼ਰਾਂ ਦੀ ਰੱਖਿਆ ਕਰਦਾ ਹੈ** ਹਾਨਿਕਾਰਕ AI ਆਉਟਪੁੱਟ ਤੋਂ

## ਉਦਾਹਰਣਾਂ ਵਿੱਚ ਆਮ ਪੈਟਰਨ

### ਪ੍ਰਮਾਣਿਕਤਾ ਪੈਟਰਨ
ਸਭ ਉਦਾਹਰਣ ਇਸ ਪੈਟਰਨ ਦੀ ਵਰਤੋਂ ਕਰਦੇ ਹਨ GitHub ਮਾਡਲਾਂ ਨਾਲ ਪ੍ਰਮਾਣਿਕਤਾ ਲਈ:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### ਗਲਤੀ ਸੰਭਾਲਣ ਪੈਟਰਨ
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### ਸੁਨੇਹਾ ਸਟ੍ਰਕਚਰ ਪੈਟਰਨ
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## ਅਗਲੇ ਕਦਮ

[ਅਧਿਆਇ 04: ਪ੍ਰੈਕਟਿਕਲ ਉਦਾਹਰਣ](../04-PracticalSamples/README.md)

## ਟ੍ਰਬਲਸ਼ੂਟਿੰਗ

### ਆਮ ਸਮੱਸਿਆਵਾਂ

**"GITHUB_TOKEN ਸੈਟ ਨਹੀਂ ਕੀਤਾ"**
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਸੀਂ ਐਨਵਾਇਰਮੈਂਟ ਵੈਰੀਏਬਲ ਸੈਟ ਕੀਤਾ ਹੈ
- ਆਪਣੇ ਟੋਕਨ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ ਕਿ ਇਸ ਵਿੱਚ `models:read` ਸਕੋਪ ਹੈ

**"API ਤੋਂ ਕੋਈ ਜਵਾਬ ਨਹੀਂ"**
- ਆਪਣੀ ਇੰਟਰਨੈਟ ਕਨੈਕਸ਼ਨ ਦੀ ਜਾਂਚ ਕਰੋ
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡਾ ਟੋਕਨ ਵੈਧ ਹੈ
- ਜਾਂਚੋ ਕਿ ਕੀ ਤੁਸੀਂ ਰੇਟ ਲਿਮਿਟਾਂ ਨੂੰ ਪਾਰ ਕੀਤਾ ਹੈ

**Maven ਕੰਪਾਈਲੇਸ਼ਨ ਗਲਤੀਆਂ**
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ ਜਾਵਾ 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ ਵਰਜਨ ਹੈ
- ਡਿਪੈਂਡੈਂਸੀਜ਼ ਨੂੰ ਰਿਫ੍ਰੈਸ਼ ਕਰਨ ਲਈ `mvn clean compile` ਚਲਾਓ

**ਅਸਵੀਕਾਰਨਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਹਾਲਾਂਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁਚਨਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।