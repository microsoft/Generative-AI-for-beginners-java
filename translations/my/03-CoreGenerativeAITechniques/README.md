<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:24:03+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "my"
}
-->
# Core Generative AI Techniques Tutorial

## အကြောင်းအရာများ

- [လိုအပ်ချက်များ](../../../03-CoreGenerativeAITechniques)
- [စတင်ခြင်း](../../../03-CoreGenerativeAITechniques)
  - [အဆင့် ၁: သင့်ပတ်ဝန်းကျင် Variable ကို သတ်မှတ်ပါ](../../../03-CoreGenerativeAITechniques)
  - [အဆင့် ၂: Examples ဖိုင်တွဲသို့ သွားပါ](../../../03-CoreGenerativeAITechniques)
- [သင်ခန်းစာ ၁: LLM Completions နှင့် Chat](../../../03-CoreGenerativeAITechniques)
- [သင်ခန်းစာ ၂: Function Calling](../../../03-CoreGenerativeAITechniques)
- [သင်ခန်းစာ ၃: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [သင်ခန်းစာ ၄: တာဝန်ရှိသော AI](../../../03-CoreGenerativeAITechniques)
- [ဥပမာများအတွက် ပုံမှန် Pattern များ](../../../03-CoreGenerativeAITechniques)
- [နောက်တစ်ဆင့်များ](../../../03-CoreGenerativeAITechniques)
- [ပြဿနာဖြေရှင်းခြင်း](../../../03-CoreGenerativeAITechniques)
  - [ပုံမှန်ပြဿနာများ](../../../03-CoreGenerativeAITechniques)

## အကျဉ်းချုပ်

ဒီသင်ခန်းစာမှာ Java နှင့် GitHub Models ကို အသုံးပြုပြီး အဓိက Generative AI နည်းလမ်းများကို လက်တွေ့လုပ်ဆောင်နိုင်မယ့် ဥပမာများကို ပေးထားပါတယ်။ သင်သည် Large Language Models (LLMs) နှင့် အပြန်အလှန်ဆက်သွယ်နည်း၊ function calling ကို အကောင်အထည်ဖော်နည်း၊ retrieval-augmented generation (RAG) ကို အသုံးပြုနည်း၊ နှင့် တာဝန်ရှိသော AI လုပ်ဆောင်မှုများကို လေ့လာနိုင်ပါမည်။

## လိုအပ်ချက်များ

စတင်မီ သင်မှာ အောက်ပါအရာများရှိရမည်-
- Java 21 သို့မဟုတ် အထက်ရှိရမည်
- Maven ကို dependency များစီမံရန်
- GitHub အကောင့်နှင့် personal access token (PAT)

## စတင်ခြင်း

### အဆင့် ၁: သင့်ပတ်ဝန်းကျင် Variable ကို သတ်မှတ်ပါ

ပထမဦးစွာ သင်၏ GitHub token ကို environment variable အဖြစ် သတ်မှတ်ရမည်။ ဒီ token က GitHub Models ကို အခမဲ့ အသုံးပြုခွင့်ပေးပါသည်။

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

### အဆင့် ၂: Examples ဖိုင်တွဲသို့ သွားပါ

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## သင်ခန်းစာ ၁: LLM Completions နှင့် Chat

**ဖိုင်:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### ဒီဥပမာက သင်ကြားပေးမယ့်အရာ

ဒီဥပမာက OpenAI API ကို အသုံးပြုပြီး Large Language Model (LLM) နှင့် အပြန်အလှန်ဆက်သွယ်နည်းကို ပြသပါသည်။ GitHub Models ဖြင့် client initialization, system နှင့် user prompts အတွက် message structure pattern များ, message history accumulation ဖြင့် conversation state ကို စီမံနည်း, နှင့် response length နှင့် creativity level များကို ထိန်းချုပ်ရန် parameter tuning အကြောင်းကို လေ့လာနိုင်ပါမည်။

### အဓိက Code အကြောင်းအရာများ

#### ၁. Client Setup
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

GitHub Models နှင့် သင့် token ကို အသုံးပြု၍ ချိတ်ဆက်မှုတစ်ခု ဖန်တီးပါသည်။

#### ၂. ရိုးရှင်းသော Completion
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

#### ၃. Conversation Memory
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI သည် ယခင် message များကို သင်ထပ်မံတင်သွင်းပါကသာ မှတ်မိပါသည်။

### ဥပမာကို Run လုပ်ပါ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Run လုပ်ပြီးနောက် ဖြစ်ပေါ်မည့်အရာများ

1. **ရိုးရှင်းသော Completion**: AI သည် Java အကြောင်းမေးခွန်းကို system prompt အညွှန်းဖြင့် ဖြေကြားသည်။
2. **Multi-turn Chat**: AI သည် မေးခွန်းများအကြား context ကို ထိန်းသိမ်းထားသည်။
3. **Interactive Chat**: သင် AI နှင့် တိုက်ရိုက် စကားပြောနိုင်သည်။

## သင်ခန်းစာ ၂: Function Calling

**ဖိုင်:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### ဒီဥပမာက သင်ကြားပေးမယ့်အရာ

Function calling သည် AI model များကို natural language request များကို ခွဲခြမ်းစိတ်ဖြာပြီး JSON Schema အညွှန်းများဖြင့် function call များကို သတ်မှတ်နိုင်စေသည်။ function execution ကို developer ထိန်းချုပ်နိုင်သောကြောင့် လုံခြုံမှုနှင့် ယုံကြည်စိတ်ချရမှုရှိစေပါသည်။

### အဓိက Code အကြောင်းအရာများ

#### ၁. Function Definition
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

AI ကို ဘယ် function များရရှိနိုင်ပြီး ဘယ်လိုအသုံးပြုရမည်ကို ပြောပြပါသည်။

#### ၂. Function Execution Flow
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

#### ၃. Function Implementation
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

### ဥပမာကို Run လုပ်ပါ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Run လုပ်ပြီးနောက် ဖြစ်ပေါ်မည့်အရာများ

1. **Weather Function**: AI သည် Seattle ရဲ့ ရာသီဥတုကို မေးမြန်းပြီး သင်ထည့်သွင်းပေးပါက AI သည် အဖြေကို ဖော်ပြသည်။
2. **Calculator Function**: AI သည် 240 ရဲ့ 15% ကိုတွက်ချက်ရန် မေးမြန်းပြီး သင်တွက်ချက်ပေးပါက AI သည် ရလဒ်ကို ရှင်းပြသည်။

## သင်ခန်းစာ ၃: RAG (Retrieval-Augmented Generation)

**ဖိုင်:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### ဒီဥပမာက သင်ကြားပေးမယ့်အရာ

Retrieval-Augmented Generation (RAG) သည် AI model များကို အပြင်မှ အချက်အလက်များထည့်သွင်းပေးပြီး အတိအကျဖြေကြားနိုင်စေသည်။ AI သည် သတ်မှတ်ထားသော အချက်အလက်များကိုသာ အခြေခံပြီး ဖြေကြားမည်ဖြစ်သည်။

### အဓိက Code အကြောင်းအရာများ

#### ၁. Document Loading
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### ၂. Context Injection
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

Triple quotes သည် context နှင့် မေးခွန်းကို ခွဲခြားစေသည်။

#### ၃. Safe Response Handling
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API response များကို အမြဲ validate လုပ်ပါ။

### ဥပမာကို Run လုပ်ပါ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Run လုပ်ပြီးနောက် ဖြစ်ပေါ်မည့်အရာများ

1. `document.txt` (GitHub Models အကြောင်းပါရှိသည်) ကို load လုပ်ပါသည်။
2. သင် document အကြောင်းမေးပါသည်။
3. AI သည် document အကြောင်းအရာကိုသာ အခြေခံပြီး ဖြေကြားသည်။

"GitHub Models ဆိုတာဘာလဲ?" နှင့် "ရာသီဥတုကဘယ်လိုလဲ?" ဆိုပြီး မေးကြည့်ပါ။

## သင်ခန်းစာ ၄: တာဝန်ရှိသော AI

**ဖိုင်:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### ဒီဥပမာက သင်ကြားပေးမယ့်အရာ

တာဝန်ရှိသော AI သည် AI application များတွင် လုံခြုံရေးအတိုင်းအတာများကို ထည့်သွင်းရန် အရေးကြီးကြောင်း ပြသသည်။ AI output များအတွက် content policy ကို လိုက်နာစေပြီး user များကို အန္တရာယ်ကင်းစေရန် ကာကွယ်ပေးသည်။

### အဓိက Code အကြောင်းအရာများ

#### ၁. Safety Testing Framework
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

#### ၂. စစ်ဆေးသော Safety Categories
- အကြမ်းဖက်မှု/အန္တရာယ်
- မုန်းတီးစကား
- ကိုယ်ရေးကိုယ်တာဖော်ထုတ်မှု
- ဆေးဘက်ဆိုင်ရာမှားယွင်းမှု
- တရားမဝင်လုပ်ဆောင်မှု

### ဥပမာကို Run လုပ်ပါ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Run လုပ်ပြီးနောက် ဖြစ်ပေါ်မည့်အရာများ

1. **အန္တရာယ်ရှိသော request များကို** HTTP 400 error ဖြင့် ပိတ်ပင်သည်။
2. **လုံခြုံသောအကြောင်းအရာများကို** ပုံမှန်ဖန်တီးနိုင်သည်။
3. **User များကို** အန္တရာယ်ရှိသော AI output များမှ ကာကွယ်ပေးသည်။

## ဥပမာများအတွက် ပုံမှန် Pattern များ

### Authentication Pattern
ဥပမာအားလုံးသည် GitHub Models နှင့် authentication ပြုလုပ်ရန် ဒီ pattern ကို အသုံးပြုသည်-

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Error Handling Pattern
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Message Structure Pattern
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## နောက်တစ်ဆင့်များ

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## ပြဿနာဖြေရှင်းခြင်း

### ပုံမှန်ပြဿနာများ

**"GITHUB_TOKEN not set"**
- သင့် environment variable ကို သတ်မှတ်ထားကြောင်း သေချာပါစေ။
- သင့် token တွင် `models:read` scope ရှိကြောင်း စစ်ဆေးပါ။

**"No response from API"**
- သင့်အင်တာနက်ချိတ်ဆက်မှုကို စစ်ဆေးပါ။
- သင့် token သက်တမ်းမကုန်သေးကြောင်း စစ်ဆေးပါ။
- Rate limit မကျော်လွန်ကြောင်း စစ်ဆေးပါ။

**Maven compilation errors**
- Java 21 သို့မဟုတ် အထက်ရှိကြောင်း သေချာပါစေ။
- `mvn clean compile` ကို run လုပ်ပြီး dependencies များကို refresh လုပ်ပါ။

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို ၎င်း၏ မူလဘာသာစကားဖြင့် အာဏာတရ အရင်းအမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူက ဘာသာပြန်မှု ဝန်ဆောင်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲအလွတ်များ သို့မဟုတ် အနားယူမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။