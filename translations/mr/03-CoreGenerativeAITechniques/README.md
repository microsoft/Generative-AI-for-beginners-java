<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:10:43+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "mr"
}
-->
# कोर जनरेटिव AI तंत्रज्ञान ट्यूटोरियल

## विषय सूची

- [पूर्वतयारी](../../../03-CoreGenerativeAITechniques)
- [सुरुवात कशी करावी](../../../03-CoreGenerativeAITechniques)
  - [पायरी 1: तुमचे पर्यावरणीय व्हेरिएबल सेट करा](../../../03-CoreGenerativeAITechniques)
  - [पायरी 2: उदाहरणे डिरेक्टरीमध्ये जा](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 1: LLM पूर्णता आणि चॅट](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 2: फंक्शन कॉलिंग](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 3: RAG (रिट्रीव्हल-अगमेंटेड जनरेशन)](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 4: जबाबदार AI](../../../03-CoreGenerativeAITechniques)
- [सर्व उदाहरणांमध्ये सामान्य पॅटर्न](../../../03-CoreGenerativeAITechniques)
- [पुढील पायऱ्या](../../../03-CoreGenerativeAITechniques)
- [समस्या निवारण](../../../03-CoreGenerativeAITechniques)
  - [सामान्य समस्या](../../../03-CoreGenerativeAITechniques)

## आढावा

या ट्यूटोरियलमध्ये Java आणि GitHub Models वापरून कोर जनरेटिव AI तंत्रज्ञानाचे प्रत्यक्ष उदाहरण दिले आहे. तुम्ही Large Language Models (LLMs) सोबत संवाद साधणे, फंक्शन कॉलिंग अंमलात आणणे, रिट्रीव्हल-अगमेंटेड जनरेशन (RAG) वापरणे, आणि जबाबदार AI पद्धती लागू करणे शिकाल.

## पूर्वतयारी

सुरुवात करण्यापूर्वी, खात्री करा की:
- Java 21 किंवा त्याहून अधिक आवृत्ती स्थापित आहे
- Maven dependency व्यवस्थापनासाठी आहे
- GitHub खाते आणि वैयक्तिक प्रवेश टोकन (PAT) आहे

## सुरुवात कशी करावी

### पायरी 1: तुमचे पर्यावरणीय व्हेरिएबल सेट करा

सर्वप्रथम, तुम्हाला तुमचे GitHub टोकन पर्यावरणीय व्हेरिएबल म्हणून सेट करावे लागेल. हे टोकन तुम्हाला GitHub Models विनामूल्य वापरण्यास अनुमती देते.

**Windows (कमांड प्रॉम्प्ट):**
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

### पायरी 2: उदाहरणे डिरेक्टरीमध्ये जा

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## ट्यूटोरियल 1: LLM पूर्णता आणि चॅट

**फाइल:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### या उदाहरणातून काय शिकता येईल

हे उदाहरण OpenAI API वापरून Large Language Model (LLM) सोबत संवाद साधण्याचे मुख्य तंत्र शिकवते. यात GitHub Models सह क्लायंट इनिशियलायझेशन, सिस्टम आणि युजर प्रॉम्प्टसाठी संदेश संरचना पॅटर्न, संदेश इतिहास संचयित करून संभाषण स्थिती व्यवस्थापन, आणि प्रतिसादाची लांबी व सर्जनशीलता पातळी नियंत्रित करण्यासाठी पॅरामीटर ट्यूनिंग यांचा समावेश आहे.

### मुख्य कोड संकल्पना

#### 1. क्लायंट सेटअप
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

हे तुमच्या टोकनचा वापर करून GitHub Models शी कनेक्शन तयार करते.

#### 2. साधी पूर्णता
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

#### 3. संभाषण मेमरी
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI फक्त ते मागील संदेश लक्षात ठेवते जे तुम्ही पुढील विनंत्यांमध्ये समाविष्ट करता.

### उदाहरण चालवा
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### तुम्ही हे चालवल्यावर काय होते

1. **साधी पूर्णता**: AI Java प्रश्नाचे उत्तर देते, सिस्टम प्रॉम्प्ट मार्गदर्शनासह
2. **मल्टी-टर्न चॅट**: AI अनेक प्रश्नांमध्ये संदर्भ टिकवते
3. **इंटरॅक्टिव चॅट**: तुम्ही AI सोबत खऱ्या संभाषणाचा अनुभव घेऊ शकता

## ट्यूटोरियल 2: फंक्शन कॉलिंग

**फाइल:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### या उदाहरणातून काय शिकता येईल

फंक्शन कॉलिंग AI मॉडेल्सना बाह्य टूल्स आणि APIs चालवण्याची विनंती करण्यास सक्षम करते. यात मॉडेल नैसर्गिक भाषेतील विनंत्या विश्लेषित करते, JSON Schema परिभाषांचा वापर करून योग्य पॅरामीटर्ससह आवश्यक फंक्शन कॉल्स ठरवते, आणि परत आलेल्या निकालांवर प्रक्रिया करून संदर्भात्मक प्रतिसाद तयार करते. फंक्शनची प्रत्यक्ष अंमलबजावणी सुरक्षा आणि विश्वसनीयतेसाठी विकसकाच्या नियंत्रणाखाली राहते.

### मुख्य कोड संकल्पना

#### 1. फंक्शन परिभाषा
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

हे AI ला कोणते फंक्शन उपलब्ध आहेत आणि त्यांचा वापर कसा करायचा ते सांगते.

#### 2. फंक्शन अंमलबजावणी प्रवाह
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

#### 3. फंक्शन अंमलबजावणी
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

### उदाहरण चालवा
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### तुम्ही हे चालवल्यावर काय होते

1. **हवामान फंक्शन**: AI सिएटलसाठी हवामान डेटा मागते, तुम्ही तो पुरवता, AI प्रतिसाद स्वरूपित करते
2. **कॅल्क्युलेटर फंक्शन**: AI गणना मागते (240 चा 15%), तुम्ही ती करता, AI निकाल स्पष्ट करते

## ट्यूटोरियल 3: RAG (रिट्रीव्हल-अगमेंटेड जनरेशन)

**फाइल:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### या उदाहरणातून काय शिकता येईल

रिट्रीव्हल-अगमेंटेड जनरेशन (RAG) माहिती पुनर्प्राप्ती आणि भाषा निर्मिती एकत्र करते. बाह्य दस्तऐवज संदर्भ AI प्रॉम्प्टमध्ये समाविष्ट करून, मॉडेल्स विशिष्ट ज्ञान स्रोतांवर आधारित अचूक उत्तरे देऊ शकतात. हे मॉडेल्सच्या प्रशिक्षण डेटामधील संभाव्य जुनी किंवा चुकीची माहिती टाळते आणि वापरकर्ता प्रश्न व अधिकृत माहिती स्रोतांमध्ये स्पष्ट सीमा राखते.

### मुख्य कोड संकल्पना

#### 1. दस्तऐवज लोड करणे
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. संदर्भ समाविष्ट करणे
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

ट्रिपल कोट्स AI ला संदर्भ आणि प्रश्न वेगळे करण्यास मदत करतात.

#### 3. सुरक्षित प्रतिसाद हाताळणी
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API प्रतिसादांची नेहमीच पडताळणी करा जेणेकरून क्रॅश टाळता येतील.

### उदाहरण चालवा
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### तुम्ही हे चालवल्यावर काय होते

1. प्रोग्राम `document.txt` लोड करते (GitHub Models बद्दल माहिती असलेला दस्तऐवज)
2. तुम्ही दस्तऐवजाबद्दल प्रश्न विचारता
3. AI फक्त दस्तऐवजाच्या सामग्रीवर आधारित उत्तर देते, सामान्य ज्ञानावर नाही

प्रयत्न करा: "GitHub Models म्हणजे काय?" वि "हवामान कसे आहे?"

## ट्यूटोरियल 4: जबाबदार AI

**फाइल:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### या उदाहरणातून काय शिकता येईल

जबाबदार AI उदाहरण उत्पादन AI अनुप्रयोगांमध्ये सुरक्षा उपाय अंमलात आणण्याचे महत्त्व दर्शवते. यात हानिकारक सामग्री श्रेणी ओळखण्यासाठी सुरक्षा फिल्टर्स दाखवले आहेत, ज्यात द्वेषपूर्ण भाषण, छळ, आत्महानी, लैंगिक सामग्री, आणि हिंसा यांचा समावेश आहे. हे सामग्री धोरण उल्लंघनांना योग्य अपवाद हाताळणी, वापरकर्ता अभिप्राय यंत्रणा, आणि फॉलबॅक प्रतिसाद धोरणांद्वारे सौम्यपणे हाताळण्याचे प्रदर्शन करते.

### मुख्य कोड संकल्पना

#### 1. सुरक्षा चाचणी फ्रेमवर्क
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

#### 2. चाचणी केलेल्या सुरक्षा श्रेण्या
- हिंसा/हानी सूचना
- द्वेषपूर्ण भाषण
- गोपनीयता उल्लंघन
- वैद्यकीय चुकीची माहिती
- बेकायदेशीर क्रियाकलाप

### उदाहरण चालवा
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### तुम्ही हे चालवल्यावर काय होते

प्रोग्राम विविध हानिकारक प्रॉम्प्ट्सची चाचणी करतो आणि AI सुरक्षा प्रणाली कसे कार्य करते ते दाखवते:
1. **धोकादायक विनंत्या ब्लॉक करते** HTTP 400 त्रुटींसह
2. **सुरक्षित सामग्री** सामान्यपणे तयार होऊ देते
3. **वापरकर्त्यांचे संरक्षण करते** हानिकारक AI आउटपुटपासून

## सर्व उदाहरणांमध्ये सामान्य पॅटर्न

### प्रमाणीकरण पॅटर्न
सर्व उदाहरणे GitHub Models सोबत प्रमाणीकरणासाठी हा पॅटर्न वापरतात:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### त्रुटी हाताळणी पॅटर्न
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### संदेश संरचना पॅटर्न
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## पुढील पायऱ्या

[अध्याय 04: व्यावहारिक नमुने](../04-PracticalSamples/README.md)

## समस्या निवारण

### सामान्य समस्या

**"GITHUB_TOKEN सेट केलेले नाही"**
- पर्यावरणीय व्हेरिएबल सेट केल्याची खात्री करा
- तुमच्या टोकनला `models:read` स्कोप आहे याची खात्री करा

**"API कडून प्रतिसाद नाही"**
- तुमचे इंटरनेट कनेक्शन तपासा
- तुमचे टोकन वैध आहे याची खात्री करा
- तुम्ही दर मर्यादा ओलांडल्या आहेत का ते तपासा

**Maven संकलन त्रुटी**
- Java 21 किंवा त्याहून अधिक आवृत्ती असल्याची खात्री करा
- `mvn clean compile` चालवून डिपेंडन्सी रीफ्रेश करा

**अस्वीकरण**:  
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेचा अभाव असू शकतो. मूळ भाषेतील दस्तऐवज हा अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराचा वापर करून उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थासाठी आम्ही जबाबदार राहणार नाही.