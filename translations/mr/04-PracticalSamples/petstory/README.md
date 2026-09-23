# सुरुवातीसाठी पाळीव प्राणी कथा जनरेटर ट्युटोरियल

एक पाळीव प्राणी फोटो अपलोड करा, याला GPT-5.6 Luna सह विश्लेषित करा, आणि परिणामी वर्णनातून कथा तयार करा. दोन्ही मॉडेल विनंत्या `reasoning_effort: none` वापरतात.

| घटक | आवृत्ती |
| --- | --- |
| Java | 21 किंवा उच्च |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## विभाग सूचक

- [पूर्वअटी](#पूर्वअटी)
- [प्रकल्प रचनांचे समज](#प्रकल्प-रचनांचे-समज)
- [मूळ घटकांचे स्पष्टीकरण](#मूळ-घटकांचे-स्पष्टीकरण)
  - [1. मुख्य अनुप्रयोग](#1-मुख्य-अनुप्रयोग)
  - [2. वेब कंट्रोलर](#2-वेब-कंट्रोलर)
  - [3. कथा सेवा](#3-कथा-सेवा)
  - [4. वेब टेम्पलेट्स](#4-वेब-टेम्पलेट्स)
  - [5. कॉन्फिगरेशन](#5-कॉन्फिगरेशन)
- [अॅप्लिकेशन चालविणे](#अनुप्रयोग-चालविणे)
- [ऑफलाइन चाचण्या](#ऑफलाइन-चाचण्या)
- [हे सगळं कसं काम करतं](#हे-सगळं-कसं-काम-करतं)
- [एआय इंटिग्रेशन समजून घेणे](#एआय-इंटिग्रेशन-समजून-घेणे)
- [पुढील पाऊले](#पुढील-पाऊले)

## पूर्वअटी

सुरू करण्यापूर्वी, खात्री करा की तुमच्याकडे आहे:
- Java 21 किंवा त्याहून अधिक स्थापित आहे
- अवलंबित्व व्यवस्थापनासाठी Maven
- GPT-5.6 Luna नावाच्या Azure AI Foundry तैनाती किंवा `AZURE_OPENAI_DEPLOYMENT` पुनर्लेखन जे त्या तैनातीकडे निर्देश करते. provisioning साठी [प्रकरण 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) पहा आणि कीलेस प्रमाणीकरणासाठी `az login` वापरा. तैनातीने इमेज इनपुट आणि `reasoning_effort: none` समर्थन करणे आवश्यक आहे.
- Java, Spring Boot, आणि वेब विकासाची मूलभूत समज

## प्रकल्प रचनांचे समज

पाळीव प्राणी कथा प्रकल्पात काही महत्त्वाचे फायली आहेत:

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

## मूळ घटकांचे स्पष्टीकरण

### 1. मुख्य अनुप्रयोग

**फाईल:** `PetStoryApplication.java`

हा आपल्या Spring Boot अनुप्रयोगाचा प्रवेश बिंदू आहे:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**हे काय करते:**
- `@SpringBootApplication` अ‍ॅनोटेशन ऑटो-कॉन्फिगरेशन आणि कॉम्पोनंट स्कॅनिंग सक्षम करते
- पोर्ट 8080 वर एम्बेडेड वेब सर्व्हर (Tomcat) सुरू करते
- आवश्यक सर्व Spring बीन्स आणि सेवा आपोआप तयार करते

### 2. वेब कंट्रोलर

**फाईल:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| एंडपॉइंट | विनंती | यशस्वी प्रतिसाद |
| --- | --- | --- |
| `GET /` | कोणतेही बॉडी नाही | CSRF टोकनसह HTML अपलोड फॉर्म |
| `POST /analyze-image` | `multipart/form-data`, फायल फील्ड `image` | JSON: `{"description":"एक खेळकर प्राणी..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, फील्ड `description` | HTML निकाल पृष्ठ वर्णन आणि तयार केलेली कथा सह |

दोन्ही POST एंडपॉइंटसाठी `GET /` कडून मिळालेला सत्र कुकी आणि CSRF टोकन आवश्यक आहे. अपलोड स्क्रिप्ट लपविलेला `_csrf` मूल्य `X-CSRF-TOKEN` हेडरमध्ये पाठवते; कथा सादर करताना ते `_csrf` फॉर्म फील्ड म्हणून पाठवले जाते. API क्लायंट्सना विनंत्यांदरम्यान कुकी जपणे आवश्यक आहे. हे फॉर्म एंडपॉइंट आहेत, JSON विनंती एंडपॉइंट नाहीत.

वर्णन रिक्त नसावे आणि 1000 अक्षरांपेक्षा जास्त नसावे. कंट्रोलर वर्णन ट्रिम करते आणि सेवा कडे पाठविण्यापूर्वी `<`, `>`, डबल उद्धरण, ऍपोस्ट्रॉफी, आणि `&` काढून टाकते. निकाल टेम्पलेट देखील मॉडेल आउटपुट `th:text` सह एस्केप करते.

इमेज व्हॅलिडेशन अपयश HTTP 400 सह `error` फील्ड परत करते; मॉडेल अपयश HTTP 502 सह `error` फील्डसह आणते पण `description` नाही. अवैध कथा वर्णने किंवा मॉडेल अपयशे `/` कडे दृश्यमान चुका संदेशासह पुनर्निर्देशित करतात. आवश्यक फील्ड्स गहाळ असल्यास HTTP 400 आणि CSRF टोकन गहाळ किंवा अयोग्य असल्यास HTTP 403 परत करते. कोणतेही फॉलबॅक वर्णन किंवा कथा यशस्वी AI निकालांप्रमाणे प्रस्तुत केली जात नाहीत.

### 3. कथा सेवा

**फाईल:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

अधिकृत OpenAI Java SDK 4.63.1 Azure AI Foundry च्या OpenAI-संगत Chat Completions API कॉल करते. Azure Identity 1.18.6 Microsoft Entra बीयर टोकन `DefaultAzureCredential` मार्फत पुरवते; कोणतीही API की आवश्यक नाही.

| ऑपरेशन | इनपुट | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | अपलोड केलेल्या MIME प्रकारासह बेस64 डेटा URL म्हणून एनकोड केलेले प्रतिमा बाइट्स | 300 |
| `generateStory` | वापरकर्त्याच्या मेसेजमध्ये पाळीव प्राणी वर्णन | 800 |

दोन्ही विनंत्यांमध्ये कॉन्फिगर केलेली तैनाती वापरली जाते, डीफॉल्ट `gpt-5.6-luna`, आणि स्पष्टपणे `ReasoningEffort.NONE` (`reasoning_effort: none`) सेट केले आहे. कोणतीही विनंती `temperature` किंवा पारंपरिक `max_tokens` पॅरामीटर पाठवत नाही.

प्रतिमा विश्लेषण JPEG, PNG, GIF, आणि WebP स्वीकारते, रिक्त प्रतिमा आणि 10MB पेक्षा जास्त फाइल नाकारते, आणि परिणामी वर्णन 1000 अक्षरांपुरते मर्यादित करते. कथा प्रॉम्प्ट कुटुंब-मैत्रीपूर्ण लघुकथा मागणी करतो. रिक्त पर्याय किंवा रिक्त मॉडेल सामग्री ही चूक आहे, आणि अपयश मूळ कारण सर्व्हर-साइड निदानासाठी जपले जाते. अनुप्रयोग बंद करताना SDK क्लायंट बंद होतो.

### 4. वेब टेम्पलेट्स

**फाईल:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (अपलोड फॉर्म)

पृष्ठ फोटो निवडकर्त्यासह सुरू होते, वर्णन पाठ क्षेत्र नव्हे. **Analyze Image** निवडलेला फोटो पूर्वावलोकन करते आणि `/analyze-image` ला पोस्ट करते. यशस्वी प्रतिसाद वर्णन दर्शवितो, लपविलेले `description` फील्ड भरतो, आणि **Generate Story** उघडतो. ते बटण विद्यमान फॉर्म `/generate-story` ला सबमिट करते.

कोणतेही ब्राउझर मॉडेल डाउनलोड किंवा CDN अवलंबित्व नाही. प्रतिमा विश्लेषण सर्व्हरवर कॉन्फिगर केलेल्या Azure तैनातीद्वारे चालते. अपयश दृश्यमान राहतात आणि तयार केलेल्या कथेसाठी फेकड वर्णनास अनुमती देत नाहीत. वेगळे फाइल निवडणे मागील विश्लेषण साफ करते.

**फाईल:** `result.html` (कथा प्रदर्शन)

तयार केलेली कथा दाखवते:

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

**टेम्पलेट वैशिष्ट्ये:**

1. **Thymeleaf समाकलन**: गतिशील सामग्रीसाठी `th:` अ‍ॅट्रिब्यूट्स वापरते
2. **प्रतिसाद डिझाईन**: मोबाइल आणि डेस्कटॉपसाठी CSS स्टाइलिंग
3. **त्रुटी हाताळणी**: वापरकर्त्यांसाठी व्हॅलिडेशन त्रुटी दर्शविते
4. **अपलोड हँडलिंग**: JavaScript फोटो पूर्वावलोकन करते, CSRF-संरक्षित मल्टिपार्ट विनंती पाठवते, आणि परत आलेले वर्णन दर्शविते

### 5. कॉन्फिगरेशन

**फाईल:** `application.properties`

अनुप्रयोगासाठी कॉन्फिगरेशन सेटिंग्ज:

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

**कॉन्फिगरेशनचे स्पष्टीकरण:**

1. **फाइल अपलोड**: फाइल आणि संपूर्ण मल्टिपार्ट विनंती दोन्ही 10MB ने मर्यादित; मल्टिपार्ट हेडरसाठी जागा ठेवण्यासाठी फोटो 10MB खाली ठेवा
2. **लॉगिंग**: अंमलबजावणी दरम्यान काय माहिती नोंदवायची नियंत्रित करते
3. **Azure AI Foundry**: वापरण्यासाठी एंडपॉइंट आणि मॉडेल तैनाती निर्दिष्ट करते (कीलेस प्रमाणीकरण)
4. **सुरक्षा**: CSRF संरक्षण सक्षम राहते; मॉडेल निदान सर्व्हरवर लॉग केले जाते, कंट्रोलर सामान्य मॉडेल-अपयश संदेश दर्शवितो

## अनुप्रयोग चालविणे

### चरण 1: साइन इन करा आणि तुमचा एंडपॉइंट सेट करा

प्रमाणीकरण कीलेस (Microsoft Entra ID) आहे, त्यामुळे API की नाही. साइन इन करा आणि तुमचा Foundry एंडपॉइंट सेट करा:

**विंडोज (कमांड प्रॉम्प्ट):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**विंडोज (पॉवरशेल):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**हे का आवश्यक आहे:**
- Azure AI Foundry Microsoft Entra ID वापरून इन्फरन्स विनंत्या प्रमाणीकरण करते
- कीलेस प्रमाणीकरण म्हणजे तुमच्या स्रोत कोड किंवा पर्यावरणात गुपिते नाहीत
- तुमच्या खात्याला संसाधनावर **Cognitive Services OpenAI User** भूमिका असणे आवश्यक आहे

डीफॉल्ट तैनाती नाव `gpt-5.6-luna` आहे. जर तुमच्या GPT-5.6 Luna तैनातीचे दुसरे नाव असेल, तर अनुप्रयोग सुरू करण्यापूर्वी त्याच टर्मिनलमध्ये `AZURE_OPENAI_DEPLOYMENT` सेट करा. प्रतिमा विश्लेषण आणि कथा निर्मिती या दोन्ही या सेटिंगचा वापर करतात.

### चरण 2: बिल्ड आणि चालवा

प्रकल्प निर्देशिका मध्ये जा:
```bash
cd 04-PracticalSamples/petstory
```

स्वतंत्र एक्झिक्यूटेबल JAR तयार करा आणि सर्व ऑफलाइन चाचण्या चालवा:
```bash
mvn clean package
```

सर्व्हर सुरू करा:
```bash
mvn spring-boot:run
```

अनुप्रयोग `http://localhost:8080` वर चालू होईल.

पर्यायी, एखाद्या मोकळ्या पोर्टवर पॅकेज्ड JAR सुरू करा, उदाहरणार्थ:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

त्या आदेशासाठी, `http://localhost:8083/` उघडा. समान `/analyze-image` आणि `/generate-story` मार्ग निवडलेल्या पोर्टवर उपलब्ध आहेत.

### चरण 3: अनुप्रयोग चाचणी करा

1. **उघडा** `http://localhost:8080` तुमच्या ब्राउझरमध्ये
2. **निवडा** स्पष्ट पाळीव प्राणी फोटो JPEG, PNG, GIF, किंवा WebP फॉर्मॅटमध्ये, 10MB खाली
3. **क्लिक करा** "Analyze Image" वर आणि प्राणी वर्णनाची वाट पाहा
4. **क्लिक करा** "Generate Story" यशस्वी विश्लेषणानंतर
5. **पहा** कथा आणि निकाल पृष्ठाचा दुवा वापरून अपलोड फॉर्मवर परत या

यशस्वी फोटो-ते-कथा प्रवाहासाठी दोन मॉडेल कॉल होतात, प्रत्येक बटणासाठी एक. थेट इन्फरन्स तुमच्या तैनातीच्या कोटा वापरते आणि शुल्क लागू होऊ शकतात; मर्यादित दराने तैनाती सामायिक करताना स्मोक चाचण्या क्रमवारीने चालवा. होम पेज लोड केल्यास मॉडेल कॉल होत नाही.

## ऑफलाइन चाचण्या

सॅम्पल निर्देशिकेतून चालवा:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) रिअल OpenAI SDK विनंत्या लूपबॅक HTTP फिक्स्चरसह कब्जा करतो. हे दोन्ही विनंत्यांचे तैनाती, `reasoning_effort: none`, टोकन मर्यादा, प्रतिमा पेलोड, इनपुट व्हॅलिडेशन, रिक्त प्रतिसाद, आणि अपस्ट्रीम त्रुटी तपासतो.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) MockMvc वापरून मॉक केलेल्या मॉडेल सेवेने Thymeleaf पृष्ठे, अपलोड करार, CSRF, व्हॅलिडेशन, आउटपुट एस्केपिंग, आणि दृश्यमान अपयश तपासतो. या चाचण्यांना Azure प्रमाणपत्रांची आवश्यकता नाही आणि कधीही पेड Azure इन्फरन्स कॉल करत नाहीत. Maven Surefire रिपोर्ट `target/surefire-reports` अंतर्गत लिहितो.

## हे सगळं कसं काम करतं

जेव्हा आपण पाळीव प्राणी कथा तयार करता तेव्हा पूर्ण प्रवाह असा आहे:

1. **फोटो निवड**: आपण अपलोड फॉर्ममध्ये पाळीव प्राणी प्रतिमा निवडता
2. **प्रतिमा अपलोड**: "Analyze Image" CSRF हेडरसह `/analyze-image` ला मल्टिपार्ट POST पाठवते
3. **प्रतима विश्लेषण**: `StoryService` प्रतिमा GPT-5.6 Luna कडे reasoning `none` सह पाठवतो
4. **वर्णन प्रदर्शन**: ब्राउझर परत आलेले वर्णन दर्शवतो आणि ते फॉर्ममध्ये साठवतो
5. **कथा सादर**: "Generate Story" `description` आणि `_csrf` सह `/generate-story` ला पोस्ट करते
6. **कथा निर्मिती**: कंट्रोलर वर्णनाची पडताळणी करतो आणि त्याच तैनातीला reasoning `none` सह कॉल करतो
7. **टेम्पलेट रेंडरिंग**: Thymeleaf वर्णन आणि कथा परिणाम पृष्ठावर एस्केप आणि दर्शवितो

**त्रुटी हाताळणी प्रवाह:**
जर मॉडेल अयशस्वी झाले, तर सर्व्हर कारण लॉग करतो. प्रतिमा विश्लेषण HTTP 502 परत करते आणि ब्राउझर त्रुटी दाखवतो परंतु "Generate Story" उघडत नाही. कथा निर्मिती फॉर्मकडे त्रुटी संदेशासह पुनर्निर्देशित करते. कोणताही मार्ग गुपचूप पूर्वनिर्धारित निकाल बदलत नाही.

## एआय इंटिग्रेशन समजून घेणे

### Azure AI Foundry (कीलेस)
सेवा तुमच्या संसाधनाच्या `/openai/v1/` एंडपॉइंट सह SDK कॉन्फिगर करते. `DefaultAzureCredential` आणि `AuthenticationUtil.getBearerTokenSupplier` Microsoft Entra टोकन्स `https://ai.azure.com/.default` साठी पुरवतात. स्थानिक विकासासाठी तुमचा Azure CLI साइन-इन वापरू शकतो; Azure-होस्ट केलेल्या अनुप्रयोगासाठी व्यवस्थापित ओळख वापरू शकतो ज्याला आवश्यक संसाधन परवानग्या आहेत.

### प्रॉम्प्ट अभियांत्रिकी
प्रतिमा विश्लेषण लघु परिच्छेदात पाहता येणाऱ्या पाळीव प्राण्याच्या वैशिष्ट्यांची विनंती करते आणि मॉडेलला प्रतिमेतील मजकूर डेटा म्हणून वागवण्यास सांगते, निर्देश म्हणून नाही. कथा निर्मिती परत आलेले वर्णन वेगळ्या, कुटुंब-मैत्रीपूर्ण लेखन विनंतीत वापरते. कोणतीही कॉल reasoning सक्षम करत नाही किंवा तापमान ओव्हरराईड सेट करत नाही.

### प्रतिसाद प्रक्रिया
सामायिक प्रतिसाद हाताळणी गहाळ पर्याय आणि रिक्त किंवा फक्त रिकाम्या जागेची सामग्री नाकारते, वैध सामग्री ट्रिम करते, आणि अपस्ट्रीम अयशस्वी जपते. प्रतिमा वर्णने 1000 अक्षरांपर्यंत मर्यादित आहेत, ज्यामुळे पुढील कथा फॉर्म फिट होते. मूळ मॉडेल अयशस्वी निदानासाठी जपत आहे पण वापरकर्त्यास दाखवत नाही.

## पुढील पाऊले

अधिक उदाहरणे पाहण्यासाठी, बघा [प्रकरण 04: व्यावहारिक नमुने](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. जरी आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थलावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->