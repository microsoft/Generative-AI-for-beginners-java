# नवीनहरूका लागि पेट कथा जेनेरेटर ट्यूटोरियल

एउटा पेटको फोटो अपलोड गर्नुहोस्, यसलाई GPT-5.6 Luna सँग विश्लेषण गर्नुहोस्, र प्राप्त विवरणबाट कथा जेनेरेट गर्नुहोस्। दुबै मोडेल अनुरोधहरूले `reasoning_effort: none` प्रयोग गर्छन्।

| कम्पोनेन्ट | संस्करण |
| --- | --- |
| Java | 21 वा माथि |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## सामग्री तालिका

- [पूर्व आवश्यकताहरू](#पूर्व-आवश्यकताहरू)
- [परियोजना संरचनाको बुझाइ](#परियोजना-संरचनाको-बुझाइ)
- [मुख्य कम्पोनेन्टहरू व्याख्या](#मुख्य-कम्पोनेन्टहरू-व्याख्या)
  - [1. मुख्य अनुप्रयोग](#१-मुख्य-अनुप्रयोग)
  - [2. वेब कन्ट्रोलर](#२-वेब-कन्ट्रोलर)
  - [3. कथा सेवा](#३-कथा-सेवा)
  - [4. वेब टेम्पलेटहरू](#४-वेब-टेम्प्लेटहरू)
  - [5. कन्फिगरेसन](#५-कन्फिगरेसन)
- [एप्लिकेशन चलाउने](#एप्लिकेशन-चलाउने)
- [अफलाइन टेस्टहरू](#अफलाइन-टेस्टहरू)
- [सबै कसरी सँगै काम गर्छ](#सबै-कुरा-कसरी-सँगै-काम-गर्छ)
- [AI एकीकरण बुझाइ](#ai-एकीकरण-बुझाइ)
- [अर्का कदमहरू](#अर्को-कदमहरू)

## पूर्व आवश्यकताहरू

सुरु गर्नु अघि, पक्का गर्नुहोस् तपाईंले:
- Java 21 वा माथिको संस्करण इन्स्टल गरेका हुनुहुन्छ
- Maven को प्रयोग गरी निर्भरता व्यवस्थापन
- GPT-5.6 Luna नामको Azure AI Foundry डिप्लोयमेन्ट, वा त्यो डिप्लोयमेन्टतर्फ देखाउने `AZURE_OPENAI_DEPLOYMENT` ओभरराइड। कृपया [अध्याय २](../../02-SetupDevEnvironment/getting-started-azure-openai.md) हेर्नुहोस्, `az login` द्वारा साइन इन गर्नुहोस् कुञ्जीरहित प्रमाणीकरणको लागि। डिप्लोयमेन्टले चित्र इनपुट र `reasoning_effort: none` समर्थन गर्नुपर्छ।
- Java, Spring Boot, र वेब विकासको आधारभूत बुझाइ

## परियोजना संरचनाको बुझाइ

पेट कथा परियोजनाको केही महत्त्वपूर्ण फाइलहरू छन्:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## मुख्य कम्पोनेन्टहरू व्याख्या

### १. मुख्य अनुप्रयोग

**फाइल:** `PetStoryApplication.java`

हाम्रो Spring Boot अनुप्रयोगको प्रवेश बिन्दु हो:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**यसले के गर्छ:**
- `@SpringBootApplication` एनोटेशनले अटो-कन्फिगरेसन र कम्पोनेन्ट स्क्यान सक्षम बनाउँछ
- पोर्ट ८०८० मा एम्बेडेड वेब सर्भर (Tomcat) सुरु गर्दछ
- आवश्यक सबै Spring बीन्स र सेवाहरू स्वचालित रूपमा बनाउँछ

### २. वेब कन्ट्रोलर

**फाइल:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| अन्तिम बिन्दु | अनुरोध | सफल प्रतिक्रिया |
| --- | --- | --- |
| `GET /` | कुनै बडी छैन | CSRF टोकन सहित HTML अपलोड फारम |
| `POST /analyze-image` | `multipart/form-data`, फाइल फिल्ड `image` | JSON: `{"description":"एक खेल्ने पेट..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, फिल्ड `description` | HTML नतिजा पृष्ठ जसमा विवरण र जेनेरेट गरिएको कथा छ |

दुबै POST अन्तिम बिन्दुहरूले `GET /` बाट प्राप्त सेसन कुकी र CSRF टोकन आवश्यक पार्छन्। अपलोड स्क्रिप्टले लुकेको `_csrf` मान `X-CSRF-TOKEN` हेडरमा पठाउँछ; कथा सबमिशनले यो `_csrf` फारम फिल्डको रूपमा पठाउँछ। API क्लाएन्टहरूले अनुरोधहरूको बीच कुकी जोगाउनु पर्छ। यी फारम अन्तिम बिन्दुहरू हुन्, JSON अनुरोध अन्तिम बिन्दुहरू होइनन्।

विवरणहरू खाली नहोस् र १००० क्यारेक्टरभन्दा लामो नहोस्। कन्ट्रोलरले विवरण ट्रिम गरेर `<`, `>`, दोहोरो उद्धरण, अपोस्ट्रोफी, र `&` हटाउँछ र त्यसपछि सेवा लाई पठाउँछ। परिणाम टेम्प्लेटले पनि मोडेल आउटपुटलाई `th:text` सँग एक्सकेप गर्दछ।

छवि मान्यता असफलताहरूले HTTP ४०० `error` फिल्डसहित फर्काउँछ; मोडेल असफलताहरू HTTP ५०२ `error` फिल्डसहित र `description` बिना फर्काउँछन्। अमान्य कथा विवरण वा मोडेल असफलताले `/` मा पुनःनिर्देशन गराउँछ जहाँ त्रुटि देखाइन्छ। आवश्यक फिल्डहरू छुटेको अवस्थामा HTTP ४००, र CSRF टोकनहरू छुटेको वा अमान्य हुँदा HTTP ४०३ फर्काइन्छ। कुनै फ्यालब्याक विवरण वा कथाहरू सफल AI नतिजाको रूपमा प्रस्तुत हुँदैनन्।

### ३. कथा सेवा

**फाइल:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

औपचारिक OpenAI Java SDK 4.63.1 ले Azure AI Foundry को OpenAI संग कम्प्याटिबल Chat Completions API कल गर्छ। Azure Identity 1.18.6 ले Microsoft Entra बेयर टोकन `DefaultAzureCredential` मार्फत आपूर्ति गर्छ; API कुञ्जी आवश्यक छैन।

| अपरेशन | इनपुट | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | अपलोड गरिएको MIME प्रकारसहितको बेज 64 डाटा URL रूपमा कन्भर्ट गरिएको छवि बाइटहरू | ३०० |
| `generateStory` | प्रयोगकर्ता सन्देशमा पेट विवरण | ८०० |

दुबै अनुरोधहरूले कन्फिगर गरिएको डिप्लोयमेन्ट प्रयोग गर्छन्, पूर्वनिर्धारित `gpt-5.6-luna`, र स्पष्ट रूपमा `ReasoningEffort.NONE` (`reasoning_effort: none`) सेट गर्छन्। दुवै अनुरोधले `temperature` वा पुरानो `max_tokens` प्यारामिटर पठाउँदैनन्।

छवि विश्लेषण JPEG, PNG, GIF, र WebP स्वीकार्छ, खाली छविहरू र १०MB भन्दा माथिका फाइलहरू अस्वीकृत गर्छ, र प्राप्त विवरणलाई १००० क्यारेक्टरमा सीमित गर्छ। कथा प्रॉम्प्टले पारिवारिक अनुकूल छोटो कथा माग्छ। खाली विकल्प वा खाली मोडेल सामग्री त्रुटिहरू हुन्, र असफलताहरूमा मूल कारण सर्वर-पक्ष डाइग्नोस्टिक्सको लागि सुरक्षित गरिन्छ। एप्लिकेशन बन्द हुँदा SDK क्लाएन्ट बन्द हुन्छ।

### ४. वेब टेम्प्लेटहरू

**फाइल:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (अपलोड फारम)

पृष्ठ फोटो पिकरबाट सुरु हुन्छ, विवरण टेक्स्ट क्षेत्र बाट होइन। **Analyze Image** ले चयनित फोटो पूर्वावलोकन गर्छ र यसलाई `/analyze-image` मा पठाउँछ। सफल प्रतिक्रियाले विवरण देखाउँछ, छिपाइएको `description` फिल्ड भर्छ, र **Generate Story** देखाउँछ। त्यो बटनले हालको फारम `/generate-story` मा सबमिट गर्छ।

कुनै ब्राउजर मोडेल डाउनलोड वा CDN निर्भरता छैन। छवि विश्लेषण सर्भरमा Azure डिप्लोयमेन्टमार्फत चल्छ। असफलताहरू देखाइन्छ र कथाको जेनेरेशन गलत विवरणसहित सक्षम हुँदैन। अर्को फाइल चयन गर्दा अघिल्लो विश्लेषण मेटिन्छ।

**फाइल:** `result.html` (कथा प्रदर्शन)

जेनेरेट गरिएको कथा देखाउँछ:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**टेम्प्लेट विशेषताहरू:**

1. **Thymeleaf एकीकरण**: गतिशील सामग्रीका लागि `th:` एट्रिब्युटहरूको प्रयोग
2. **उत्तरदायी डिजाइन**: मोबाइल र डेस्कटपका लागि CSS स्टाइलिङ
3. **त्रुटि ह्यान्डलिङ**: प्रयोगकर्ताहरूलाई मान्यता त्रुटिहरू देखाउने
4. **अपलोड ह्यान्डलिङ**: JavaScript ले फोटो पूर्वावलोकन गर्छ, CSRF-संरक्षित multipart अनुरोध पठाउँछ, र फर्काइएको विवरण देखाउँछ

### ५. कन्फिगरेसन

**फाइल:** `application.properties`

अनुप्रयोगका लागि कन्फिगरेसन सेटिङहरू:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**कन्फिगरेसन व्याख्या:**

1. **फाइल अपलोड**: फाइल र सम्पूर्ण multipart अनुरोध दुवैलाई १०MB मा सीमित; multipart हेडरहरूको लागि फोटोहरू सो सीमाभन्दा कम राख्नुहोस्
2. **लगिङ**: कार्यसम्पादनको समयमा के जानकारी लग गरिन्छ नियन्त्रण गर्छ
3. **Azure AI Foundry**: प्रयोग गर्नुपर्ने अन्तिम बिन्दु र मोडेल डिप्लोयमेन्ट निर्दिष्ट गर्छ (कुञ्जीरहित प्रमाणीकरण)
4. **सुरक्षा**: CSRF सुरक्षा सक्षम रहन्छ; मोडेल डाइग्नोस्टिक्स सर्भरमा लग गरिन्छ, जबकि कन्ट्रोलरले साधारण मोडेल असफलता सन्देश देखाउँछ

## एप्लिकेशन चलाउने

### चरण १: साइन इन र आफ्नो अन्तिम बिन्दु सेट गर्नुहोस्

प्रमाणीकरण कुञ्जीरहित (Microsoft Entra ID) हो, त्यसैले API कुञ्जी छैन। साइन इन गरी Foundry अन्तिम बिन्दु सेट गर्नुहोस्:

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**किन आवश्यक छ:**
- Azure AI Foundry ले Microsoft Entra ID सँग इनफरेन्स अनुरोध प्रमाणीकरण गर्छ
- कुञ्जीरहित प्रमाणीकरणले स्रोत कोड वा वातावरणमा कुनै गोप्य कुरा हुँदैन
- तपाईंको खातामा स्रोतमा **Cognitive Services OpenAI User** भूमिका हुनुपर्छ

पूर्वनिर्धारित डिप्लोयमेन्ट नाम `gpt-5.6-luna` हो। यदि तपाईंको GPT-5.6 Luna डिप्लोयमेन्टको अर्को नाम छ भने, एप्लिकेशन सुरु गर्नु अघि उस्तै टर्मिनलमा `AZURE_OPENAI_DEPLOYMENT` सेट गर्नुहोस्। दुबै छवि विश्लेषण र कथा जेनेरेशनले यो सेटिङ प्रयोग गर्छन्।

### चरण २: निर्माण र चलाउने

परियोजना निर्देशिकामा जानुहोस्:
```bash
cd 04-PracticalSamples/petstory
```

स्वतन्त्र चल्न सक्ने JAR निर्माण गरेर सबै अफलाइन परीक्षणहरू चलाउनुहोस्:
```bash
mvn clean package
```

सर्भर सुरु गर्नुहोस्:
```bash
mvn spring-boot:run
```

अनुप्रयोग `http://localhost:8080` मा सुरु हुनेछ।

विकल्पको रूपमा, निःशुल्क पोर्टमा प्याकेज्ड JAR सुरु गर्नुहोस्, उदाहरणका लागि:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

त्यो आदेशमा, `http://localhost:8083/` खोलेर प्रयोग गर्नुहोस्। सोही `/analyze-image` र `/generate-story` मार्गहरू चयन गरिएको पोर्टमा उपलब्ध छन्।

### चरण ३: एप्लिकेशन परीक्षण गर्नुहोस्

१. **खोल्नुहोस्** `http://localhost:8080` आफ्नो ब्राउजरमा
२. **छान्नुहोस्** स्पष्ट पेट फोटो JPEG, PNG, GIF, वा WebP रूपमै, १०MB भन्दा कम
३. **क्लिक गर्नुहोस्** "Analyze Image" र पेट विवरणको लागि पर्खनुहोस्
४. **क्लिक गर्नुहोस्** सफल विश्लेषणपछि "Generate Story"
५. **हेर्नुहोस्** कथा र नतिजा पृष्ठको लिंक प्रयोग गरी अपलोड फारममा फिर्ता जानुहोस्

सफल फोटो-देखि-कथा प्रवाहले दुई मोडेल कल गर्छ, एउटा प्रति बटन। लाइभ इनफरेन्सले तपाईंको डिप्लोयमेन्टको कोटा खपत गर्छ र शुल्क लाग्न सक्छ; सीमित दर भएको डिप्लोयमेन्ट सँग सेयर गर्दा स्मोक टेस्टहरू शृङ्खलाबद्ध रूपमा चलाउनुहोस्। होम पृष्ठ लोड गर्दा मोडेल कल हुँदैन।

## अफलाइन टेस्टहरू

नमुना निर्देशिकाबाट, चलाउनुहोस्:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) ले वास्तविक OpenAI SDK अनुरोधहरूलाई लूपब्याक HTTP फिक्स्चर सँग समात्छ। यो दुबै अनुरोधको डिप्लोयमेन्ट, `reasoning_effort: none`, टोकन सीमा, छवि प्यालोड, इनपुट मान्यता, खाली प्रतिक्रियाहरू, र अपस्ट्रीम त्रुटिहरू जांच गर्छ।

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) ले MockMvc सँग नकली मोडेल सेवा प्रयोग गरी Thymeleaf पृष्ठहरू, अपलोड अनुबंध, CSRF, मान्यता, आउटपुट एक्सकेपिंग, र दृश्य असफलताहरू परीक्षण गर्छ। यी परीक्षणहरूलाई Azure प्रमाणपत्र आवश्यक पर्दैन र कहिल्यै पेड Azure इनफरेन्स कल गर्दैन। Maven ले Surefire रिपोर्टहरू `target/surefire-reports` भित्र लेख्छ।

## सबै कुरा कसरी सँगै काम गर्छ

यहाँ पेट कथा जेनेरेट गर्दा पूर्ण प्रवाह छ:

१. **फोटो चयन**: तपाईंले अपलोड फारममा पेटको छवि रोज्नुहुन्छ
२. **छवि अपलोड**: "Analyze Image" ले CSRF हेडरसहित multipart POST पठाउँछ `/analyze-image` मा
३. **छवि विश्लेषण**: `StoryService` ले छविलाई reasoning `none` सेट गरेर GPT-5.6 Luna मा पठाउँछ
४. **विवरण प्रदर्शन**: ब्राउजरले फर्किएको विवरण देखाउँछ र फारममा भण्डारण गर्छ
५. **कथा सबमिशन**: "Generate Story" ले `description` र `_csrf` लाई `/generate-story` मा पोस्ट गर्छ
६. **कथा जेनेरेशन**: कन्ट्रोलरले विवरण मान्य पार्छ र सोही डिप्लोयमेन्टलाई reasoning `none` सँग कल गर्छ
७. **टेम्प्लेट रेंडरिङ**: Thymeleaf ले विवरण र कथालाई निकालेर नतिजा पृष्ठमा देखाउँछ

**त्रुटि ह्यान्डलिङ प्रवाह:**
यदि मोडेल असफल हुन्छ भने, सर्भर कारण लग गर्दछ। छवि विश्लेषणले HTTP ५०२ फर्काउँछ र ब्राउजरले त्रुटि देखाउँछ तर "Generate Story" देखाउँदैन। कथा जेनेरेशनले फारममा त्रुटि सन्देशसहित पुनर्निर्देशन गर्छ। दुवै मार्गले पूर्वलेखित नतिजा चुपचाप प्रतिस्थापन हुँदैन।

## AI एकीकरण बुझाइ

### Azure AI Foundry (कुञ्जीरहित)
सेवालाई तपाईंको स्रोतको `/openai/v1/` अन्तिम बिन्दु संग SDK कन्फिगर गरिएको छ। `DefaultAzureCredential` र `AuthenticationUtil.getBearerTokenSupplier` ले Microsoft Entra टोकन प्रदान गर्छन् `https://ai.azure.com/.default` को लागि। स्थानीय विकासले Azure CLI साइन-इन प्रयोग गर्न सक्छ; Azure होस्ट गरिएको अनुप्रयोगले आवश्यक स्रोत अनुमति भएका प्रबन्धित पहिचान प्रयोग गर्न सक्छ।

### प्रॉम्प्ट इन्जिनियरिङ
छवि विश्लेषणले देखिने पेट विशेषताहरू छोटो अनुच्छेदमा माग्छ र मोडेललाई छविमा टेक्स्टलाई निर्देशन होइन डेटा सम्झन भन्छ। कथा जेनेरेशनले फर्किएको विवरणलाई अलग पारिवारिक अनुकूल लेखन अनुरोधमा प्रयोग गर्छ। दुवै कलले reasoning सक्षम पार्दैन वा तापक्रम ओभरराइड सेट गर्दैन।

### प्रतिक्रिया प्रशोधन
साझा प्रतिक्रिया ह्यान्डलरले विकल्पहरू छुटेको र खाली वा खाली ठाउँ मात्र सामग्री अस्वीकृत गर्छ, मान्य सामग्री ट्रिम गर्छ, र अपस्ट्रीम असफलताहरू सुरक्षित राख्छ। छवि विवरणलाई १००० क्यारेक्टरमा सीमित गरिएको छ जसले पछि कथा फारममा फिट हुन्छ। मूल मोडेल असफलता डाइग्नोस्टिक्सका लागि राखिन्छ तर प्रयोगकर्तालाई देखाइँदैन।

## अर्को कदमहरू

थप उदाहरणहरूका लागि, हेर्नुहोस् [अध्याय ०४: व्यावहारिक नमूना](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी सही हुन प्रयास गर्छौं, तर कृपया जानकार हुनुस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छन्। मूल दस्तावेज़ यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा त्रुटिको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->