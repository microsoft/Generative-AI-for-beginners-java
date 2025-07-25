<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:06:20+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "hi"
}
-->
# Foundry Local Spring Boot ट्यूटोरियल

## सामग्री तालिका

- [पूर्व आवश्यकताएँ](../../../../04-PracticalSamples/foundrylocal)
- [प्रोजेक्ट का अवलोकन](../../../../04-PracticalSamples/foundrylocal)
- [कोड को समझना](../../../../04-PracticalSamples/foundrylocal)
  - [1. एप्लिकेशन कॉन्फ़िगरेशन (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. मुख्य एप्लिकेशन क्लास (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. एआई सर्विस लेयर (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. प्रोजेक्ट डिपेंडेंसीज़ (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [सभी चीज़ें कैसे साथ काम करती हैं](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local सेटअप करना](../../../../04-PracticalSamples/foundrylocal)
- [एप्लिकेशन चलाना](../../../../04-PracticalSamples/foundrylocal)
- [अपेक्षित आउटपुट](../../../../04-PracticalSamples/foundrylocal)
- [अगले कदम](../../../../04-PracticalSamples/foundrylocal)
- [समस्या निवारण](../../../../04-PracticalSamples/foundrylocal)

## पूर्व आवश्यकताएँ

इस ट्यूटोरियल को शुरू करने से पहले, सुनिश्चित करें कि आपके पास निम्नलिखित हैं:

- **Java 21 या उससे अधिक** आपके सिस्टम पर इंस्टॉल हो
- **Maven 3.6+** प्रोजेक्ट को बनाने के लिए
- **Foundry Local** इंस्टॉल और चल रहा हो

### **Foundry Local इंस्टॉल करें:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## प्रोजेक्ट का अवलोकन

यह प्रोजेक्ट चार मुख्य घटकों से बना है:

1. **Application.java** - मुख्य Spring Boot एप्लिकेशन एंट्री पॉइंट
2. **FoundryLocalService.java** - सर्विस लेयर जो एआई कम्युनिकेशन को संभालती है
3. **application.properties** - Foundry Local कनेक्शन के लिए कॉन्फ़िगरेशन
4. **pom.xml** - Maven डिपेंडेंसीज़ और प्रोजेक्ट कॉन्फ़िगरेशन

## कोड को समझना

### 1. एप्लिकेशन कॉन्फ़िगरेशन (application.properties)

**फ़ाइल:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**यह क्या करता है:**
- **base-url**: यह बताता है कि Foundry Local कहाँ चल रहा है (डिफ़ॉल्ट पोर्ट 5273)
- **model**: टेक्स्ट जनरेशन के लिए उपयोग किए जाने वाले एआई मॉडल का नाम

**मुख्य अवधारणा:** Spring Boot इन प्रॉपर्टीज़ को स्वचालित रूप से लोड करता है और `@Value` एनोटेशन का उपयोग करके आपके एप्लिकेशन में उपलब्ध कराता है।

### 2. मुख्य एप्लिकेशन क्लास (Application.java)

**फ़ाइल:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**यह क्या करता है:**
- `@SpringBootApplication` Spring Boot ऑटो-कॉन्फ़िगरेशन को सक्षम करता है
- `WebApplicationType.NONE` Spring को बताता है कि यह एक कमांड-लाइन ऐप है, वेब सर्वर नहीं
- मुख्य मेथड Spring एप्लिकेशन को शुरू करता है

**डेमो रनर:**
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

**यह क्या करता है:**
- `@Bean` एक घटक बनाता है जिसे Spring प्रबंधित करता है
- `CommandLineRunner` Spring Boot शुरू होने के बाद कोड चलाता है
- `foundryLocalService` Spring द्वारा स्वचालित रूप से इंजेक्ट किया जाता है (डिपेंडेंसी इंजेक्शन)
- एआई को एक टेस्ट संदेश भेजता है और प्रतिक्रिया प्रदर्शित करता है

### 3. एआई सर्विस लेयर (FoundryLocalService.java)

**फ़ाइल:** `src/main/java/com/example/FoundryLocalService.java`

#### कॉन्फ़िगरेशन इंजेक्शन:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**यह क्या करता है:**
- `@Service` Spring को बताता है कि यह क्लास बिज़नेस लॉजिक प्रदान करती है
- `@Value` application.properties से कॉन्फ़िगरेशन वैल्यू इंजेक्ट करता है
- `:default-value` सिंटैक्स प्रॉपर्टीज़ सेट न होने पर बैकअप वैल्यू प्रदान करता है

#### क्लाइंट इनिशियलाइज़ेशन:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**यह क्या करता है:**
- `@PostConstruct` Spring द्वारा सर्विस बनाने के बाद इस मेथड को चलाता है
- एक OpenAI क्लाइंट बनाता है जो आपके स्थानीय Foundry Local इंस्टेंस को इंगित करता है
- `/v1` पथ OpenAI API संगतता के लिए आवश्यक है
- API कुंजी "unused" है क्योंकि स्थानीय विकास में प्रमाणीकरण की आवश्यकता नहीं होती

#### चैट मेथड:
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

**यह क्या करता है:**
- **ChatCompletionCreateParams**: एआई अनुरोध को कॉन्फ़िगर करता है
  - `model`: उपयोग किए जाने वाले एआई मॉडल को निर्दिष्ट करता है
  - `addUserMessage`: आपके संदेश को बातचीत में जोड़ता है
  - `maxCompletionTokens`: प्रतिक्रिया की लंबाई को सीमित करता है (संसाधनों को बचाने के लिए)
  - `temperature`: रैंडमनेस को नियंत्रित करता है (0.0 = निश्चित, 1.0 = रचनात्मक)
- **API कॉल**: अनुरोध को Foundry Local पर भेजता है
- **प्रतिक्रिया हैंडलिंग**: एआई की टेक्स्ट प्रतिक्रिया को सुरक्षित रूप से निकालता है
- **त्रुटि हैंडलिंग**: सहायक त्रुटि संदेशों के साथ अपवादों को लपेटता है

### 4. प्रोजेक्ट डिपेंडेंसीज़ (pom.xml)

**मुख्य डिपेंडेंसीज़:**

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

**यह क्या करते हैं:**
- **spring-boot-starter**: मुख्य Spring Boot कार्यक्षमता प्रदान करता है
- **openai-java**: API कम्युनिकेशन के लिए आधिकारिक OpenAI Java SDK
- **jackson-databind**: API कॉल के लिए JSON सीरियलाइज़ेशन/डिसीरियलाइज़ेशन को संभालता है

## सभी चीज़ें कैसे साथ काम करती हैं

जब आप एप्लिकेशन चलाते हैं, तो पूरा फ्लो इस प्रकार होता है:

1. **स्टार्टअप**: Spring Boot शुरू होता है और `application.properties` पढ़ता है
2. **सर्विस निर्माण**: Spring `FoundryLocalService` बनाता है और कॉन्फ़िगरेशन वैल्यू इंजेक्ट करता है
3. **क्लाइंट सेटअप**: `@PostConstruct` OpenAI क्लाइंट को Foundry Local से कनेक्ट करने के लिए इनिशियलाइज़ करता है
4. **डेमो निष्पादन**: स्टार्टअप के बाद `CommandLineRunner` कोड चलाता है
5. **एआई कॉल**: डेमो `foundryLocalService.chat()` को एक टेस्ट संदेश के साथ कॉल करता है
6. **API अनुरोध**: सर्विस OpenAI-संगत अनुरोध को Foundry Local पर भेजती है
7. **प्रतिक्रिया प्रोसेसिंग**: सर्विस एआई की प्रतिक्रिया निकालती है और लौटाती है
8. **प्रदर्शन**: एप्लिकेशन प्रतिक्रिया को प्रिंट करता है और बाहर निकलता है

## Foundry Local सेटअप करना

Foundry Local सेटअप करने के लिए, निम्नलिखित चरणों का पालन करें:

1. **Foundry Local इंस्टॉल करें** [पूर्व आवश्यकताएँ](../../../../04-PracticalSamples/foundrylocal) सेक्शन में दिए गए निर्देशों का उपयोग करके।
2. **एआई मॉडल डाउनलोड करें** जिसे आप उपयोग करना चाहते हैं, उदाहरण के लिए, `phi-3.5-mini`, निम्नलिखित कमांड के साथ:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties फ़ाइल को कॉन्फ़िगर करें** ताकि यह आपके Foundry Local सेटिंग्स से मेल खाए, खासकर यदि आप अलग पोर्ट या मॉडल का उपयोग कर रहे हैं।

## एप्लिकेशन चलाना

### चरण 1: Foundry Local शुरू करें
```bash
foundry model run phi-3.5-mini
```

### चरण 2: एप्लिकेशन को बनाएं और चलाएं
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## अपेक्षित आउटपुट

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

## अगले कदम

अधिक उदाहरणों के लिए, देखें [Chapter 04: Practical samples](../README.md)

## समस्या निवारण

### सामान्य समस्याएँ

**"Connection refused" या "Service unavailable"**
- सुनिश्चित करें कि Foundry Local चल रहा है: `foundry model list`
- सेवा पोर्ट 5273 पर है: `application.properties` की जाँच करें
- Foundry Local को पुनः शुरू करने का प्रयास करें: `foundry model run phi-3.5-mini`

**"Model not found" त्रुटियाँ**
- उपलब्ध मॉडल की जाँच करें: `foundry model list`
- `application.properties` में मॉडल नाम को सही से अपडेट करें
- यदि आवश्यक हो तो मॉडल डाउनलोड करें: `foundry model run phi-3.5-mini`

**Maven संकलन त्रुटियाँ**
- सुनिश्चित करें कि Java 21 या उससे अधिक है: `java -version`
- साफ करें और पुनः बनाएं: `mvn clean compile`
- डिपेंडेंसी डाउनलोड के लिए इंटरनेट कनेक्शन की जाँच करें

**एप्लिकेशन शुरू होता है लेकिन कोई आउटपुट नहीं**
- सुनिश्चित करें कि Foundry Local प्रतिक्रिया दे रहा है: ब्राउज़र में `http://localhost:5273` खोलें
- विशिष्ट त्रुटि संदेशों के लिए एप्लिकेशन लॉग की जाँच करें
- सुनिश्चित करें कि मॉडल पूरी तरह से लोड हो चुका है और तैयार है

**अस्वीकरण**:  
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता सुनिश्चित करने का प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल भाषा में उपलब्ध मूल दस्तावेज़ को प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।