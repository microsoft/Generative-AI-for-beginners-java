<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:14:18+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "ne"
}
-->
# फाउन्ड्री लोकल स्प्रिंग बुट ट्युटोरियल

## सामग्री सूची

- [पूर्वआवश्यकताहरू](../../../../04-PracticalSamples/foundrylocal)
- [प्रोजेक्टको अवलोकन](../../../../04-PracticalSamples/foundrylocal)
- [कोड बुझ्दै](../../../../04-PracticalSamples/foundrylocal)
  - [१. एप्लिकेसन कन्फिगरेसन (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [२. मुख्य एप्लिकेसन क्लास (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [३. एआई सेवा तह (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [४. प्रोजेक्ट निर्भरताहरू (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [सबै कुरा कसरी सँगै काम गर्छ](../../../../04-PracticalSamples/foundrylocal)
- [फाउन्ड्री लोकल सेटअप गर्दै](../../../../04-PracticalSamples/foundrylocal)
- [एप्लिकेसन चलाउँदै](../../../../04-PracticalSamples/foundrylocal)
- [अपेक्षित आउटपुट](../../../../04-PracticalSamples/foundrylocal)
- [अर्को कदम](../../../../04-PracticalSamples/foundrylocal)
- [समस्या समाधान](../../../../04-PracticalSamples/foundrylocal)

## पूर्वआवश्यकताहरू

यो ट्युटोरियल सुरु गर्नु अघि, सुनिश्चित गर्नुहोस् कि तपाईंसँग यी छन्:

- **Java 21 वा उच्च संस्करण** तपाईंको सिस्टममा इन्स्टल भएको
- **Maven 3.6+** प्रोजेक्ट निर्माणका लागि
- **Foundry Local** इन्स्टल गरिएको र चलिरहेको

### **फाउन्ड्री लोकल इन्स्टल गर्नुहोस्:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## प्रोजेक्टको अवलोकन

यो प्रोजेक्ट चार मुख्य कम्पोनेन्टहरूमा विभाजित छ:

1. **Application.java** - मुख्य स्प्रिंग बुट एप्लिकेसनको प्रवेश बिन्दु
2. **FoundryLocalService.java** - एआईसँगको संवाद व्यवस्थापन गर्ने सेवा तह
3. **application.properties** - फाउन्ड्री लोकल कनेक्शनको कन्फिगरेसन
4. **pom.xml** - माभेन निर्भरताहरू र प्रोजेक्ट कन्फिगरेसन

## कोड बुझ्दै

### १. एप्लिकेसन कन्फिगरेसन (application.properties)

**फाइल:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**यसले के गर्छ:**
- **base-url**: फाउन्ड्री लोकल कहाँ चलिरहेको छ भनेर निर्दिष्ट गर्छ (डिफल्ट पोर्ट ५२७३)
- **model**: टेक्स्ट जेनेरेसनका लागि प्रयोग हुने एआई मोडेलको नाम

**मुख्य अवधारणा:** स्प्रिंग बुटले यी प्रोपर्टीहरू स्वचालित रूपमा लोड गर्छ र `@Value` एनोटेसन प्रयोग गरेर तपाईंको एप्लिकेसनमा उपलब्ध गराउँछ।

### २. मुख्य एप्लिकेसन क्लास (Application.java)

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

**यसले के गर्छ:**
- `@SpringBootApplication` ले स्प्रिंग बुटको स्वचालित कन्फिगरेसन सक्षम गर्छ
- `WebApplicationType.NONE` ले स्प्रिंगलाई यो कमाण्ड-लाइन एप्लिकेसन हो भनेर बताउँछ, वेब सर्भर होइन
- मुख्य मेथडले स्प्रिंग एप्लिकेसन सुरु गर्छ

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

**यसले के गर्छ:**
- `@Bean` ले स्प्रिंगद्वारा व्यवस्थापन गरिने कम्पोनेन्ट सिर्जना गर्छ
- `CommandLineRunner` ले स्प्रिंग बुट सुरु भएपछि कोड चलाउँछ
- `foundryLocalService` स्प्रिंगद्वारा स्वचालित रूपमा इन्जेक्ट गरिन्छ (निर्भरता इन्जेक्सन)
- एआईलाई परीक्षण सन्देश पठाउँछ र प्रतिक्रिया देखाउँछ

### ३. एआई सेवा तह (FoundryLocalService.java)

**फाइल:** `src/main/java/com/example/FoundryLocalService.java`

#### कन्फिगरेसन इन्जेक्सन:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**यसले के गर्छ:**
- `@Service` ले स्प्रिंगलाई यो क्लासले व्यापारिक तर्क प्रदान गर्छ भनेर बताउँछ
- `@Value` ले application.properties बाट कन्फिगरेसन मानहरू इन्जेक्ट गर्छ
- `:default-value` सिन्ट्याक्सले प्रोपर्टी सेट नभएमा फलब्याक मानहरू प्रदान गर्छ

#### क्लाइन्ट इनिसियलाइजेसन:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**यसले के गर्छ:**
- `@PostConstruct` ले स्प्रिंगले सेवा सिर्जना गरेपछि यो मेथड चलाउँछ
- फाउन्ड्री लोकल इन्स्ट्यान्समा संकेत गर्ने ओपनएआई क्लाइन्ट सिर्जना गर्छ
- `/v1` पथ ओपनएआई एपीआई अनुकूलताका लागि आवश्यक छ
- एपीआई कुञ्जी "unused" छ किनभने स्थानीय विकासमा प्रमाणीकरण आवश्यक पर्दैन

#### च्याट मेथड:
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

**यसले के गर्छ:**
- **ChatCompletionCreateParams**: एआई अनुरोध कन्फिगर गर्छ
  - `model`: प्रयोग हुने एआई मोडेल निर्दिष्ट गर्छ
  - `addUserMessage`: तपाईंको सन्देशलाई संवादमा थप्छ
  - `maxCompletionTokens`: प्रतिक्रिया कति लामो हुन सक्छ भनेर सीमित गर्छ (स्रोत बचत गर्छ)
  - `temperature`: अनियमितता नियन्त्रण गर्छ (०.० = निर्धारणात्मक, १.० = रचनात्मक)
- **एपीआई कल**: अनुरोध फाउन्ड्री लोकलमा पठाउँछ
- **प्रतिक्रिया ह्यान्डलिङ**: एआईको टेक्स्ट प्रतिक्रिया सुरक्षित रूपमा निकाल्छ
- **त्रुटि ह्यान्डलिङ**: उपयोगी त्रुटि सन्देशहरू सहित अपवादहरू समेट्छ

### ४. प्रोजेक्ट निर्भरताहरू (pom.xml)

**मुख्य निर्भरताहरू:**

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

**यसले के गर्छ:**
- **spring-boot-starter**: कोर स्प्रिंग बुट कार्यक्षमता प्रदान गर्छ
- **openai-java**: एपीआई संवादका लागि आधिकारिक ओपनएआई जाभा एसडीके
- **jackson-databind**: एपीआई कलहरूको लागि JSON सिरियलाइजेसन/डिसिरियलाइजेसन ह्यान्डल गर्छ

## सबै कुरा कसरी सँगै काम गर्छ

जब तपाईं एप्लिकेसन चलाउनुहुन्छ, यो सम्पूर्ण प्रक्रिया हुन्छ:

1. **सुरुवात**: स्प्रिंग बुट सुरु हुन्छ र `application.properties` पढ्छ
2. **सेवा सिर्जना**: स्प्रिंगले `FoundryLocalService` सिर्जना गर्छ र कन्फिगरेसन मानहरू इन्जेक्ट गर्छ
3. **क्लाइन्ट सेटअप**: `@PostConstruct` ले ओपनएआई क्लाइन्टलाई फाउन्ड्री लोकलसँग जडान गर्न इनिसियलाइज गर्छ
4. **डेमो कार्यान्वयन**: `CommandLineRunner` सुरु भएपछि कार्यान्वित हुन्छ
5. **एआई कल**: डेमोले `foundryLocalService.chat()` लाई परीक्षण सन्देशसहित कल गर्छ
6. **एपीआई अनुरोध**: सेवा ओपनएआई-संगत अनुरोध फाउन्ड्री लोकलमा पठाउँछ
7. **प्रतिक्रिया प्रशोधन**: सेवा प्रतिक्रिया निकाल्छ र फिर्ता गर्छ
8. **प्रदर्शन**: एप्लिकेसनले प्रतिक्रिया प्रिन्ट गर्छ र बन्द हुन्छ

## फाउन्ड्री लोकल सेटअप गर्दै

फाउन्ड्री लोकल सेटअप गर्न, यी चरणहरू पालना गर्नुहोस्:

1. [पूर्वआवश्यकताहरू](../../../../04-PracticalSamples/foundrylocal) सेक्सनमा दिइएको निर्देशनअनुसार फाउन्ड्री लोकल इन्स्टल गर्नुहोस्।
2. तपाईं प्रयोग गर्न चाहनुभएको एआई मोडेल, जस्तै `phi-3.5-mini`, डाउनलोड गर्न निम्न कमाण्ड चलाउनुहोस्:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties** फाइललाई तपाईंको फाउन्ड्री लोकल सेटिङ्ससँग मिलाउन कन्फिगर गर्नुहोस्, विशेष गरी यदि तपाईं फरक पोर्ट वा मोडेल प्रयोग गर्दै हुनुहुन्छ भने।

## एप्लिकेसन चलाउँदै

### चरण १: फाउन्ड्री लोकल सुरु गर्नुहोस्
```bash
foundry model run phi-3.5-mini
```

### चरण २: एप्लिकेसन निर्माण र चलाउनुहोस्
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

## अर्को कदम

थप उदाहरणहरूको लागि, [Chapter 04: Practical samples](../README.md) हेर्नुहोस्।

## समस्या समाधान

### सामान्य समस्याहरू

**"Connection refused" वा "Service unavailable"**
- सुनिश्चित गर्नुहोस् कि फाउन्ड्री लोकल चलिरहेको छ: `foundry model list`
- सेवा पोर्ट ५२७३ मा छ भनेर जाँच गर्नुहोस्: `application.properties` हेर्नुहोस्
- फाउन्ड्री लोकल पुनः सुरु गर्न प्रयास गर्नुहोस्: `foundry model run phi-3.5-mini`

**"Model not found" त्रुटिहरू**
- उपलब्ध मोडेलहरू जाँच गर्नुहोस्: `foundry model list`
- `application.properties` मा मोडेल नाम ठ्याक्कै मिलाएर अपडेट गर्नुहोस्
- आवश्यक भएमा मोडेल डाउनलोड गर्नुहोस्: `foundry model run phi-3.5-mini`

**माभेन कम्पाइल त्रुटिहरू**
- Java 21 वा उच्च संस्करण सुनिश्चित गर्नुहोस्: `java -version`
- सफा गरेर पुनः निर्माण गर्नुहोस्: `mvn clean compile`
- निर्भरताहरू डाउनलोड गर्न इन्टरनेट कनेक्शन जाँच गर्नुहोस्

**एप्लिकेसन सुरु हुन्छ तर कुनै आउटपुट छैन**
- फाउन्ड्री लोकल प्रतिक्रिया दिइरहेको छ भनेर सुनिश्चित गर्नुहोस्: ब्राउजरमा `http://localhost:5273` खोल्नुहोस्
- एप्लिकेसन लगहरूमा विशिष्ट त्रुटि सन्देशहरू जाँच गर्नुहोस्
- मोडेल पूर्ण रूपमा लोड भएको र तयार छ भनेर सुनिश्चित गर्नुहोस्

**अस्वीकरण**:  
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको छ। हामी शुद्धताको लागि प्रयास गर्छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादहरूमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छ। यसको मूल भाषा मा रहेको मूल दस्तावेज़लाई आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याको लागि हामी जिम्मेवार हुने छैनौं।