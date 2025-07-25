<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:11:28+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "mr"
}
-->
# Foundry Local Spring Boot ट्यूटोरियल

## अनुक्रमणिका

- [पूर्वतयारी](../../../../04-PracticalSamples/foundrylocal)
- [प्रकल्पाचा आढावा](../../../../04-PracticalSamples/foundrylocal)
- [कोड समजून घेणे](../../../../04-PracticalSamples/foundrylocal)
  - [1. अनुप्रयोग कॉन्फिगरेशन (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. मुख्य अनुप्रयोग वर्ग (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI सेवा स्तर (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. प्रकल्प अवलंबित्व (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [सर्व काही कसे एकत्र कार्य करते](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local सेट करणे](../../../../04-PracticalSamples/foundrylocal)
- [अनुप्रयोग चालवणे](../../../../04-PracticalSamples/foundrylocal)
- [अपेक्षित आउटपुट](../../../../04-PracticalSamples/foundrylocal)
- [पुढील पावले](../../../../04-PracticalSamples/foundrylocal)
- [समस्या निवारण](../../../../04-PracticalSamples/foundrylocal)

## पूर्वतयारी

या ट्यूटोरियलला सुरुवात करण्यापूर्वी, खात्री करा की तुमच्याकडे खालील गोष्टी आहेत:

- **Java 21 किंवा त्याहून अधिक** तुमच्या प्रणालीवर स्थापित आहे
- **Maven 3.6+** प्रकल्प तयार करण्यासाठी
- **Foundry Local** स्थापित आणि चालू आहे

### **Foundry Local स्थापित करा:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## प्रकल्पाचा आढावा

या प्रकल्पामध्ये चार मुख्य घटक आहेत:

1. **Application.java** - मुख्य Spring Boot अनुप्रयोग प्रवेश बिंदू
2. **FoundryLocalService.java** - सेवा स्तर जो AI संवाद हाताळतो
3. **application.properties** - Foundry Local कनेक्शनसाठी कॉन्फिगरेशन
4. **pom.xml** - Maven अवलंबित्व आणि प्रकल्प कॉन्फिगरेशन

## कोड समजून घेणे

### 1. अनुप्रयोग कॉन्फिगरेशन (application.properties)

**फाइल:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**याचा उपयोग:**
- **base-url**: Foundry Local कुठे चालू आहे हे निर्दिष्ट करते (डिफॉल्ट पोर्ट 5273)
- **model**: टेक्स्ट जनरेशनसाठी वापरायचा AI मॉडेलचे नाव

**महत्त्वाची संकल्पना:** Spring Boot आपोआप या गुणधर्मांना लोड करते आणि `@Value` अ‍ॅनोटेशन वापरून तुमच्या अनुप्रयोगासाठी उपलब्ध करते.

### 2. मुख्य अनुप्रयोग वर्ग (Application.java)

**फाइल:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**याचा उपयोग:**
- `@SpringBootApplication` Spring Boot ऑटो-कॉन्फिगरेशन सक्षम करते
- `WebApplicationType.NONE` Spring ला सांगते की हा एक कमांड-लाइन अनुप्रयोग आहे, वेब सर्व्हर नाही
- मुख्य पद्धत Spring अनुप्रयोग सुरू करते

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

**याचा उपयोग:**
- `@Bean` एक घटक तयार करते ज्याचे व्यवस्थापन Spring करते
- `CommandLineRunner` Spring Boot सुरू झाल्यानंतर कोड चालवते
- `foundryLocalService` Spring द्वारे आपोआप इंजेक्ट केले जाते (डिपेंडन्सी इंजेक्शन)
- AI ला एक चाचणी संदेश पाठवते आणि प्रतिसाद प्रदर्शित करते

### 3. AI सेवा स्तर (FoundryLocalService.java)

**फाइल:** `src/main/java/com/example/FoundryLocalService.java`

#### कॉन्फिगरेशन इंजेक्शन:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**याचा उपयोग:**
- `@Service` Spring ला सांगते की हा वर्ग व्यवसाय तर्क प्रदान करतो
- `@Value` application.properties मधून कॉन्फिगरेशन मूल्ये इंजेक्ट करते
- `:default-value` सिंटॅक्स गुणधर्म सेट नसल्यास फॉलबॅक मूल्ये प्रदान करते

#### क्लायंट प्रारंभ:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**याचा उपयोग:**
- `@PostConstruct` Spring सेवा तयार केल्यानंतर ही पद्धत चालवते
- OpenAI क्लायंट तयार करते जो तुमच्या स्थानिक Foundry Local उदाहरणाकडे निर्देशित करतो
- OpenAI API सुसंगततेसाठी `/v1` पथ आवश्यक आहे
- API की "unused" आहे कारण स्थानिक विकासासाठी प्रमाणीकरण आवश्यक नाही

#### चॅट पद्धत:
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

**याचा उपयोग:**
- **ChatCompletionCreateParams**: AI विनंती कॉन्फिगर करते
  - `model`: कोणता AI मॉडेल वापरायचा ते निर्दिष्ट करते
  - `addUserMessage`: संभाषणात तुमचा संदेश जोडतो
  - `maxCompletionTokens`: प्रतिसाद किती लांब असू शकतो याची मर्यादा घालते (संसाधने वाचवते)
  - `temperature`: यादृच्छिकता नियंत्रित करते (0.0 = निश्चित, 1.0 = सर्जनशील)
- **API कॉल**: विनंती Foundry Local ला पाठवते
- **प्रतिसाद हाताळणी**: AI चा टेक्स्ट प्रतिसाद सुरक्षितपणे काढते
- **त्रुटी हाताळणी**: उपयुक्त त्रुटी संदेशांसह अपवाद लपेटते

### 4. प्रकल्प अवलंबित्व (pom.xml)

**महत्त्वाची अवलंबित्वे:**

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

**याचा उपयोग:**
- **spring-boot-starter**: मुख्य Spring Boot कार्यक्षमता प्रदान करते
- **openai-java**: API संवादासाठी OpenAI Java SDK
- **jackson-databind**: API कॉलसाठी JSON सिरियलायझेशन/डिसिरियलायझेशन हाताळते

## सर्व काही कसे एकत्र कार्य करते

जेव्हा तुम्ही अनुप्रयोग चालवता तेव्हा संपूर्ण प्रवाह असा आहे:

1. **स्टार्टअप**: Spring Boot सुरू होते आणि `application.properties` वाचते
2. **सेवा निर्मिती**: Spring `FoundryLocalService` तयार करते आणि कॉन्फिगरेशन मूल्ये इंजेक्ट करते
3. **क्लायंट सेटअप**: `@PostConstruct` OpenAI क्लायंट प्रारंभ करते जो Foundry Local शी कनेक्ट होतो
4. **डेमो अंमलबजावणी**: स्टार्टअपनंतर `CommandLineRunner` अंमलात आणते
5. **AI कॉल**: डेमो `foundryLocalService.chat()` चाचणी संदेशासह कॉल करते
6. **API विनंती**: सेवा OpenAI-सुसंगत विनंती तयार करते आणि Foundry Local ला पाठवते
7. **प्रतिसाद प्रक्रिया**: सेवा प्रतिसाद काढते आणि परत करते
8. **प्रदर्शन**: अनुप्रयोग प्रतिसाद मुद्रित करतो आणि बाहेर पडतो

## Foundry Local सेट करणे

Foundry Local सेट करण्यासाठी, खालील चरणांचे अनुसरण करा:

1. **Foundry Local स्थापित करा** [पूर्वतयारी](../../../../04-PracticalSamples/foundrylocal) विभागातील सूचनांचा वापर करून.
2. **AI मॉडेल डाउनलोड करा** जे तुम्हाला वापरायचे आहे, उदाहरणार्थ, `phi-3.5-mini`, खालील आदेशासह:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties फाइल कॉन्फिगर करा** तुमच्या Foundry Local सेटिंग्जशी जुळण्यासाठी, विशेषतः जर तुम्ही वेगळा पोर्ट किंवा मॉडेल वापरत असाल.

## अनुप्रयोग चालवणे

### चरण 1: Foundry Local सुरू करा
```bash
foundry model run phi-3.5-mini
```

### चरण 2: अनुप्रयोग तयार करा आणि चालवा
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

## पुढील पावले

अधिक उदाहरणांसाठी, [Chapter 04: Practical samples](../README.md) पहा

## समस्या निवारण

### सामान्य समस्या

**"Connection refused" किंवा "Service unavailable"**
- खात्री करा की Foundry Local चालू आहे: `foundry model list`
- सेवा पोर्ट 5273 वर आहे याची खात्री करा: `application.properties` तपासा
- Foundry Local पुन्हा सुरू करण्याचा प्रयत्न करा: `foundry model run phi-3.5-mini`

**"Model not found" त्रुटी**
- उपलब्ध मॉडेल तपासा: `foundry model list`
- मॉडेलचे नाव application.properties मध्ये अचूकपणे अपडेट करा
- आवश्यक असल्यास मॉडेल डाउनलोड करा: `foundry model run phi-3.5-mini`

**Maven संकलन त्रुटी**
- Java 21 किंवा त्याहून अधिक खात्री करा: `java -version`
- स्वच्छ करा आणि पुन्हा तयार करा: `mvn clean compile`
- अवलंबित्व डाउनलोडसाठी इंटरनेट कनेक्शन तपासा

**अनुप्रयोग सुरू होतो पण आउटपुट नाही**
- Foundry Local प्रतिसाद देत आहे याची खात्री करा: ब्राउझर उघडा `http://localhost:5273`
- विशिष्ट त्रुटी संदेशांसाठी अनुप्रयोग लॉग तपासा
- मॉडेल पूर्णपणे लोड झाले आहे आणि तयार आहे याची खात्री करा

**अस्वीकरण**:  
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरे त्रुटी किंवा अचूकतेच्या अभावाने युक्त असू शकतात. मूळ भाषेतील दस्तऐवज हा अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराचा वापर करून उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थासाठी आम्ही जबाबदार नाही.