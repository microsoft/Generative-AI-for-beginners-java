<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:05:31+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "hi"
}
-->
# कोर जनरेटिव एआई तकनीकों का ट्यूटोरियल

## सामग्री सूची

- [पूर्व आवश्यकताएँ](../../../03-CoreGenerativeAITechniques)
- [शुरुआत करना](../../../03-CoreGenerativeAITechniques)
  - [चरण 1: अपना एनवायरनमेंट वेरिएबल सेट करें](../../../03-CoreGenerativeAITechniques)
  - [चरण 2: उदाहरण डायरेक्टरी पर जाएं](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 1: LLM पूर्णता और चैट](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 2: फंक्शन कॉलिंग](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 3: RAG (रिट्रीवल-ऑगमेंटेड जनरेशन)](../../../03-CoreGenerativeAITechniques)
- [ट्यूटोरियल 4: जिम्मेदार एआई](../../../03-CoreGenerativeAITechniques)
- [सामान्य पैटर्न उदाहरणों में](../../../03-CoreGenerativeAITechniques)
- [अगले कदम](../../../03-CoreGenerativeAITechniques)
- [समस्या निवारण](../../../03-CoreGenerativeAITechniques)
  - [सामान्य समस्याएँ](../../../03-CoreGenerativeAITechniques)

## अवलोकन

यह ट्यूटोरियल Java और GitHub Models का उपयोग करके कोर जनरेटिव एआई तकनीकों के व्यावहारिक उदाहरण प्रदान करता है। आप सीखेंगे कि बड़े भाषा मॉडल (LLMs) के साथ कैसे इंटरैक्ट करें, फंक्शन कॉलिंग को लागू करें, रिट्रीवल-ऑगमेंटेड जनरेशन (RAG) का उपयोग करें, और जिम्मेदार एआई प्रथाओं को लागू करें।

## पूर्व आवश्यकताएँ

शुरू करने से पहले, सुनिश्चित करें कि आपके पास निम्नलिखित हैं:
- Java 21 या उच्चतर संस्करण इंस्टॉल किया हुआ
- Maven डिपेंडेंसी प्रबंधन के लिए
- एक GitHub खाता और व्यक्तिगत एक्सेस टोकन (PAT)

## शुरुआत करना

### चरण 1: अपना एनवायरनमेंट वेरिएबल सेट करें

सबसे पहले, आपको अपना GitHub टोकन एक एनवायरनमेंट वेरिएबल के रूप में सेट करना होगा। यह टोकन आपको GitHub Models तक मुफ्त में पहुंचने की अनुमति देता है।

**Windows (कमांड प्रॉम्प्ट):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (पावरशेल):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### चरण 2: उदाहरण डायरेक्टरी पर जाएं

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## ट्यूटोरियल 1: LLM पूर्णता और चैट

**फ़ाइल:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### यह उदाहरण क्या सिखाता है

यह उदाहरण OpenAI API के माध्यम से बड़े भाषा मॉडल (LLM) के साथ इंटरैक्शन के मुख्य यांत्रिकी को प्रदर्शित करता है, जिसमें GitHub Models के साथ क्लाइंट इनिशियलाइज़ेशन, सिस्टम और उपयोगकर्ता प्रॉम्प्ट के लिए संदेश संरचना पैटर्न, संदेश इतिहास संचय के माध्यम से बातचीत की स्थिति प्रबंधन, और प्रतिक्रिया लंबाई और रचनात्मकता स्तरों को नियंत्रित करने के लिए पैरामीटर ट्यूनिंग शामिल है।

### मुख्य कोड अवधारणाएँ

#### 1. क्लाइंट सेटअप
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

यह आपके टोकन का उपयोग करके GitHub Models से कनेक्शन बनाता है।

#### 2. सरल पूर्णता
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

#### 3. बातचीत की मेमोरी
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

एआई केवल पिछले संदेशों को याद करता है यदि आप उन्हें बाद के अनुरोधों में शामिल करते हैं।

### उदाहरण चलाएँ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### जब आप इसे चलाते हैं तो क्या होता है

1. **सरल पूर्णता**: एआई सिस्टम प्रॉम्प्ट मार्गदर्शन के साथ एक Java प्रश्न का उत्तर देता है
2. **मल्टी-टर्न चैट**: एआई कई प्रश्नों के दौरान संदर्भ बनाए रखता है
3. **इंटरएक्टिव चैट**: आप एआई के साथ वास्तविक बातचीत कर सकते हैं

## ट्यूटोरियल 2: फंक्शन कॉलिंग

**फ़ाइल:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### यह उदाहरण क्या सिखाता है

फंक्शन कॉलिंग एआई मॉडल को बाहरी टूल और एपीआई के निष्पादन का अनुरोध करने की अनुमति देती है। यह एक संरचित प्रोटोकॉल के माध्यम से काम करता है, जहां मॉडल प्राकृतिक भाषा अनुरोधों का विश्लेषण करता है, JSON Schema परिभाषाओं का उपयोग करके आवश्यक फंक्शन कॉल्स और पैरामीटर निर्धारित करता है, और संदर्भात्मक प्रतिक्रियाएँ उत्पन्न करने के लिए लौटाए गए परिणामों को संसाधित करता है, जबकि वास्तविक फंक्शन निष्पादन सुरक्षा और विश्वसनीयता के लिए डेवलपर नियंत्रण में रहता है।

### मुख्य कोड अवधारणाएँ

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

यह एआई को बताता है कि कौन से फंक्शन उपलब्ध हैं और उनका उपयोग कैसे करना है।

#### 2. फंक्शन निष्पादन प्रवाह
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

#### 3. फंक्शन कार्यान्वयन
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

### उदाहरण चलाएँ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### जब आप इसे चलाते हैं तो क्या होता है

1. **मौसम फंक्शन**: एआई सिएटल के मौसम डेटा का अनुरोध करता है, आप इसे प्रदान करते हैं, एआई प्रतिक्रिया को प्रारूपित करता है
2. **कैलकुलेटर फंक्शन**: एआई एक गणना (240 का 15%) का अनुरोध करता है, आप इसे गणना करते हैं, एआई परिणाम की व्याख्या करता है

## ट्यूटोरियल 3: RAG (रिट्रीवल-ऑगमेंटेड जनरेशन)

**फ़ाइल:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### यह उदाहरण क्या सिखाता है

रिट्रीवल-ऑगमेंटेड जनरेशन (RAG) जानकारी पुनर्प्राप्ति को भाषा जनरेशन के साथ जोड़ता है। यह बाहरी दस्तावेज़ संदर्भ को एआई प्रॉम्प्ट में इंजेक्ट करता है, जिससे मॉडल विशिष्ट ज्ञान स्रोतों के आधार पर सटीक उत्तर प्रदान कर सकते हैं, जबकि रणनीतिक प्रॉम्प्ट इंजीनियरिंग के माध्यम से उपयोगकर्ता प्रश्नों और प्राधिकृत जानकारी स्रोतों के बीच स्पष्ट सीमाएँ बनाए रखते हैं।

### मुख्य कोड अवधारणाएँ

#### 1. दस्तावेज़ लोडिंग
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. संदर्भ इंजेक्शन
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

ट्रिपल कोट्स एआई को संदर्भ और प्रश्न के बीच अंतर करने में मदद करते हैं।

#### 3. सुरक्षित प्रतिक्रिया प्रबंधन
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

एपीआई प्रतिक्रियाओं को हमेशा मान्य करें ताकि क्रैश से बचा जा सके।

### उदाहरण चलाएँ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### जब आप इसे चलाते हैं तो क्या होता है

1. प्रोग्राम `document.txt` लोड करता है (जिसमें GitHub Models के बारे में जानकारी होती है)
2. आप दस्तावेज़ के बारे में एक प्रश्न पूछते हैं
3. एआई केवल दस्तावेज़ सामग्री के आधार पर उत्तर देता है, अपने सामान्य ज्ञान के आधार पर नहीं

आजमाएँ: "GitHub Models क्या है?" बनाम "मौसम कैसा है?"

## ट्यूटोरियल 4: जिम्मेदार एआई

**फ़ाइल:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### यह उदाहरण क्या सिखाता है

जिम्मेदार एआई उदाहरण एआई अनुप्रयोगों में सुरक्षा उपायों को लागू करने के महत्व को प्रदर्शित करता है। यह सुरक्षा फ़िल्टर दिखाता है जो हानिकारक सामग्री श्रेणियों का पता लगाते हैं, जैसे कि घृणा भाषण, उत्पीड़न, आत्म-हानि, यौन सामग्री, और हिंसा। यह दिखाता है कि उत्पादन एआई अनुप्रयोगों को सामग्री नीति उल्लंघनों को उचित अपवाद प्रबंधन, उपयोगकर्ता प्रतिक्रिया तंत्र, और फॉलबैक प्रतिक्रिया रणनीतियों के माध्यम से कैसे संभालना चाहिए।

### मुख्य कोड अवधारणाएँ

#### 1. सुरक्षा परीक्षण फ्रेमवर्क
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

#### 2. परीक्षण की गई सुरक्षा श्रेणियाँ
- हिंसा/हानि निर्देश
- घृणा भाषण
- गोपनीयता उल्लंघन
- चिकित्सा गलत जानकारी
- अवैध गतिविधियाँ

### उदाहरण चलाएँ
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### जब आप इसे चलाते हैं तो क्या होता है

प्रोग्राम विभिन्न हानिकारक प्रॉम्प्ट का परीक्षण करता है और दिखाता है कि एआई सुरक्षा प्रणाली:
1. **खतरनाक अनुरोधों को ब्लॉक करती है** HTTP 400 त्रुटियों के साथ
2. **सुरक्षित सामग्री को सामान्य रूप से उत्पन्न करने की अनुमति देती है**
3. **उपयोगकर्ताओं की रक्षा करती है** हानिकारक एआई आउटपुट से

## सामान्य पैटर्न उदाहरणों में

### प्रमाणीकरण पैटर्न
सभी उदाहरण इस पैटर्न का उपयोग GitHub Models के साथ प्रमाणीकरण के लिए करते हैं:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### त्रुटि प्रबंधन पैटर्न
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### संदेश संरचना पैटर्न
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## अगले कदम

[अध्याय 04: व्यावहारिक नमूने](../04-PracticalSamples/README.md)

## समस्या निवारण

### सामान्य समस्याएँ

**"GITHUB_TOKEN सेट नहीं है"**
- सुनिश्चित करें कि आपने एनवायरनमेंट वेरिएबल सेट किया है
- सत्यापित करें कि आपके टोकन में `models:read` स्कोप है

**"एपीआई से कोई प्रतिक्रिया नहीं"**
- अपना इंटरनेट कनेक्शन जांचें
- सत्यापित करें कि आपका टोकन मान्य है
- जांचें कि क्या आपने दर सीमाएँ पार कर ली हैं

**Maven संकलन त्रुटियाँ**
- सुनिश्चित करें कि आपके पास Java 21 या उच्चतर संस्करण है
- डिपेंडेंसी को ताज़ा करने के लिए `mvn clean compile` चलाएँ

**अस्वीकरण**:  
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता सुनिश्चित करने का प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल भाषा में उपलब्ध मूल दस्तावेज़ को प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।