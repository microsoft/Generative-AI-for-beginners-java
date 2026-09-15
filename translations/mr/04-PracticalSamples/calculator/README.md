# सूरुवातीसाठी MCP कॅल्क्युलेटर ट्यूटोरियल

## सूची

- [आपण काय शिकाल](#आपण-काय-शिकाल)
- [पूर्वज्ञान](#पूर्वज्ञान)
- [अवलंबित्व आवृत्त्या](#अवलंबित्व-आवृत्त्या)
- [प्रकल्प रचना समजून घेणे](#प्रकल्प-रचना-समजून-घेणे)
- [कोर घटकांचे स्पष्टीकरण](#कोर-घटकांचे-स्पष्टीकरण)
  - [1. मुख्य अनुप्रयोग](#1-मुख्य-अनुप्रयोग)
  - [2. कॅल्क्युलेटर सेवा](#2-कॅल्क्युलेटर-सेवा)
  - [3. थेट MCP क्लायंट](#3-थेट-mcp-क्लायंट)
  - [4. AI-शक्तीयुक्त क्लायंट](#4-ai-शक्तीयुक्त-क्लायंट)
- [उदाहरण चालविणे](#उदाहरण-चालविणे)
- [ऑफलाइन चाचण्या](#ऑफलाइन-चाचण्या)
- [ते कसे एकत्र कार्य करतात](#ते-कसे-एकत्र-कार्य-करतात)
- [पुढील पावले](#पुढील-पावले)

## आपण काय शिकाल

हा ट्यूटोरियल मॉडेल कॉन्टेक्स्ट प्रोटोकॉल (MCP) वापरून कॅल्क्युलेटर सेवा कशी तयार करायची ते समजावतो. आपण समजाल:

- AI साधन म्हणून वापरू शकण्यास सेवा कशी तयार करावी
- MCP सेवा सह थेट संवाद कसा स्थापन करावा
- AI मॉडेल्स स्वयंचलितपणे कोणती साधने वापरायची हे कसे निवडतात
- थेट प्रोटोकॉल कॉल्स आणि AI-आधारित संवादातील फरक काय आहे

## पूर्वज्ञान

सुरुवात करण्यापूर्वी, खात्री करा की आपल्याजवळ आहे:
- Java 21 किंवा त्याहून अधिक आवृत्ती स्थापित केलेली आहे
- अवलंबित्व व्यवस्थापनासाठी Maven
- Java आणि Spring Boot ची मूलभूत समज

फक्त AI क्लायंटसाठी Azure OpenAI डिप्लॉयमेंट आणि प्रमाणित `DefaultAzureCredential` आवश्यक आहे,
जसे की स्थानिक Azure CLI साइन-इन किंवा Azure मधील व्यवस्थापित ओळख. ओळखीला 
संसाधनावर Cognitive Services OpenAI User भूमिका आवश्यक आहे. पाहा [अध्याय 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
सर्व्हर, डायरेक्ट SDK क्लायंट, आणि सर्व स्वयंचलित चाचण्या यांना Azure खाते किंवा मॉडेल प्रवेश आवश्यक नाही.

## अवलंबित्व आवृत्त्या

2026-09-14 रोजी पडताळणी केलेल्या रिलीज अवलंबित्व:

| अवलंबित्व | आवृत्ती |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-व्यवस्थापित) | 2.0.0 |
| LangChain4j / मुख्य | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j अधिकृत OpenAI अ‍ॅडॅप्टर | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (बूट-व्यवस्थापित) | 6.0.3 |

MCP आणि अधिकृत OpenAI अ‍ॅडॅप्टर Maven Central मधील प्रसिद्ध बीटा रिलीज आहेत, स्नॅपशॉट्स नाहीत.
त्यांची आवृत्ती LangChain4j मुख्यापासून वेगळी आहे. स्नॅपशॉट किंवा माइलस्टोन रिपॉझिटरीजची गरज नाही.
केवळ क्लायंट अवलंबित्वांना चाचणी क्षेत्र आहे कारण चालणारे उदाहरणे `src/test/java` अंतर्गत आहेत.

## प्रकल्प रचना समजून घेणे

कॅल्क्युलेटर प्रकल्पात काही महत्त्वाचे फायली आहेत:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## कोर घटकांचे स्पष्टीकरण

### 1. मुख्य अनुप्रयोग

**फाईल:** `McpServerApplication.java`

हा आमच्या कॅल्क्युलेटर सेवेचा प्रवेश बिंदू आहे. ही एक मानक Spring Boot अनुप्रयोग आहे ज्यात एक विशेष घटक आहे:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**हे काय करते:**
- पोर्ट 8080 वर Spring Boot वेब सर्व्हर सुरू करते
- `ToolCallbackProvider` तयार करते जो आमच्या कॅल्क्युलेटर मेथड्सना MCP साधनांप्रमाणे उपलब्ध करतो
- `@Bean` अ‍ॅनोटेशन Spring ला सूचित करते की हे एक कॉम्पोनंट आहे ज्याचा वापर अन्य भाग करू शकतात

### 2. कॅल्क्युलेटर सेवा

**फाईल:** `CalculatorService.java`

येथे सर्व गणिते होतात. प्रत्येक पद्धत `@Tool` ने चिन्हांकित आहे ज्यामुळे ती MCP द्वारे उपलब्ध होते:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // अधिक कॅल्क्युलेटर ऑपरेशन्स...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**मुख्य वैशिष्ट्ये:**

1. **`@Tool` अ‍ॅनोटेशन**: हे MCP ला सांगते की ही पद्धत बाह्य क्लायंट्सद्वारे कॉल केली जाऊ शकते
2. **स्पष्ट वर्णने**: प्रत्येक साधनासाठी वर्णन आहे जे AI मॉडेल्सना ते कधी वापरायचे ते समजण्यास मदत करते
3. **सुसंगत परतावा स्वरूप**: सर्व क्रिया मानवी वाचनीय स्ट्रिंग्ज परत करतात जसे "5.00 + 3.00 = 8.00"
4. **त्रुटी हाताळणी**: शून्याने भागाकार आणि नकारात्मक वर्गमूळ शोधणाऱ्या त्रुटी संदेश परत करतात

**उपलब्ध ऑपरेशन्स:**
- `add(a, b)` - दोन संख्या जमा करते
- `subtract(a, b)` - दुसर्‍या संख्येपासून पहिली वजा करते
- `multiply(a, b)` - दोन संख्या गुणाकार करते
- `divide(a, b)` - पहिल्याने दुसर्‍या पासून भाग करते (शून्य तपासणीसह)
- `power(base, exponent)` - बेसवर अपवर्ग करते
- `squareRoot(number)` - वर्गमूळ काढते (नकारात्मक तपासणीसह)
- `modulus(a, b)` - भागाकारांतील शिल्लक देते
- `absolute(number)` - अभाज्य मूल्य देते
- `help()` - सर्व ऑपरेशन्सबद्दल माहिती देते

### 3. थेट MCP क्लायंट

बघा [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

हा क्लायंट `/mcp` वर `HttpClientStreamableHttpTransport` वापरतो, कनेक्शन सुरू करतो,
सर्व्हरला पिंग करतो, आणि साधने-यादी पॅजिनेशनचे पालन करतो. तो सर्व नऊ अपेक्षित साधने आहेत याची तपासणी करतो
आणि त्यांना कॉल करतो, जसे `modulus` आणि `help`, AI मॉडेलशिवाय.

वर्तमान विनंती बिल्डर असे दिसते:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

प्रोटोकॉल त्रुटी क्लायंट अयशस्वी करतात, चुकीच्या यशाच्या ऐवजी. MCP क्लायंट ट्राय-विद-रिसोर्सेसने बंद होतो,
जसे डिस्कव्हरी किंवा साधन कॉल अयशस्वी झाल्यावर.

### 4. AI-शक्तीयुक्त क्लायंट

बघा [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
आणि [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` अद्ययावत LangChain4j `ChatModel` API अंमलात आणते.
`StreamableHttpMcpTransport` त्याला SDK क्लायंटसारख्या `/mcp` एन्डपॉईंटशी जोडतो.
`AiServices` साधने शोधतो आणि साधन कॉल/परिणाम संभाषण व्यवस्थापित करतो.

डीफॉल्ट डिप्लॉयमेंट आहे **GPT-5.6 Luna**, ज्यामध्ये तार्किक प्रयत्न स्पष्टपणे निष्क्रिय आहेत:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

हे डीफॉल्ट्स प्रत्येक पूर्णतेसाठी लागू होतात, ज्यात साधन हाताळण्यानंतरचे फॉलो-अप्सही आहेत.
क्लायंट वापरतो एक रिफ्रेश करण्यायोग्य `BearerTokenCredential` ज्याला `DefaultAzureCredential` आणि
`https://ai.azure.com/.default` स्कोप बॅक करतो, एकवेळचा API की टोकन नाही.
संसाधन URL आणि `/openai/v1` मध्ये समाप्त होणारे URL स्वीकारले जातात.

बॉट राखतो मर्यादित संभाषण इतिहास, प्रिंट करतो `Tool executed: ...` वास्तविक
MCP परिणामासह, आणि अयशस्वी होतो जर प्रतिसादाने साधने सोडली. साधन लूपे चार राउंड ट्रिपपर्यंत मर्यादित आहेत.
प्रमाणीकरण, मॉडेल, MCP, आणि साधन त्रुटी प्रसारित होतात; स्वयंचलित मॉडेल पुनर्प्रयत्न निष्क्रिय आहेत.
दोन्ही MCP ट्रान्सपोर्ट/क्लायंट आणि अधिकृत OpenAI क्लायंट यश किंवा अयशस्वीवर बंद होतात.

## उदाहरण चालविणे

### चरण 1: कॅल्क्युलेटर सर्व्हर सुरू करा

सर्व्हरसाठी कोणतीही Azure संरचना आवश्यक नाही. खालील आदेश या नमुन्याच्या फोल्डरमधून चालवा.
उदाहरण पोर्ट **18081** वापरते ज्यामुळे दुसऱ्या नमुन्याशी संसर्ग टाळता येतो; डीफॉल्ट 8080 राहतो.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP एन्डपॉईंट आहे `http://localhost:18081/mcp`. हेल्थ आणि डिस्कव्हरी माहिती आहे
`http://localhost:18081/health` आणि `http://localhost:18081/info`.
Streamable HTTP जुन्या SSE-फक्त ट्रान्सपोर्ट बदलतो; `/sse` आणि `/v1/tools` हे एन्डपॉईंट नाहीत.

### चरण 2: डायरेक्ट क्लायंटसह चाचणी करा

दुसऱ्या PowerShell टर्मिनलमध्ये:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

कोणत्याही इनपुटची गरज नाही. सर्व नऊ साधने वापरली जातात. अपेक्षित अंकगणित परिणामांचा समावेश आहे
8, 6, 42, 5, 256, 4, 2, आणि 5.5, नंतर मदत मजकूर.

### चरण 3: AI क्लायंटसह चाचणी करा

पूर्वज्ञानात वर्णनानुसार प्रमाणित केल्यावर, त्याच टर्मिनलमध्ये AI क्लायंट कॉन्फिगर करा:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

अपेक्षा करा `Tool executed: add` ओळ `41.80` सह, त्यानंतर मॉडेलचे उत्तर.
सिंगल-प्रॉम्प्ट मोड इनपुटची वाट पाहणार नाही. मूळ चार-प्रॉम्प्ट डेमो चालविण्यासाठी:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

डेमो कॉल्स `add`, `squareRoot`, `help`, आणि साखळीबद्ध `power` नंतर `divide` ऑपरेशन.
अपेक्षित संख्यात्मक उत्तरं 41.8, 12, आणि 64. आर्ग्युमेंट्सशिवाय देखील हे डेमो चालते.

### चरण 4: संवादात्मक बॉट चालवा

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

टाका `Multiply 6 by 7 using the calculator service`, नंतर `exit` किंवा `quit`.
अपेक्षा करा एक खरा `multiply` साधन परिणाम 42. रिकाम्या ओळी दुर्लक्षित केल्या जातात; EOF देखील सत्र संपवते.
या प्रवेश बिंदूची नॉनइंटरएक्टिव्ह स्मोक चाचणीसाठी:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

दोन्ही AI प्रवेशबिंदू स्वीकारतात `--prompt "question"`, `--demo`, आणि `--interactive`.
अवैध पर्याय कनेक्शन उघडण्याआधी अयशस्वी होतात. प्रत्येक Maven `-D...` आर्ग्युमेंट पूर्णपणे कोट केला जातो
PowerShell साठी. Bash वर, `$env:NAME = "value"` च्या ऐवजी `export NAME=value` वापरा.

**कोटा:** AI उदाहरणे अनुक्रमे चालवा. साधे प्रॉम्प्ट सामान्यतः दोन मॉडेल विनंत्या आवश्यक असतात;
संपूर्ण डेमो सामान्यतः नऊ आवश्यक आहेत, ज्यात साधन-परिणाम फॉलो-अप्सही आहेत. एक समान 10 RPM
डिप्लॉयमेंटसह, पुढील AI रनपूर्वी ताजे कोटा विंडो द्या. 429 स्पष्टपणे अयशस्वी होतो पुनर्प्रयत्नांशिवाय;
सेवेच्या पुनर्प्रयत्न-नंतर मार्गदर्शनाचा अवलंब करा. वास्तविक विनंती संख्या मॉडेलवर अवलंबून असते.
ऑफलाइन चाचण्यांमध्ये कोणताही कोटा वापरला जात नाही आणि ते लाइव्ह Luna उपलब्धता किंवा उत्तर गुणवत्ता तपासत नाहीत.

### कॉन्फिगरेशन आणि बंद करणे

| सेटिंग | डीफॉल्ट / वर्तन |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; बेस URL, `/mcp` शिवाय |
| `-Dmcp.server.url=...` | सर्व क्लायंटसाठी `MCP_SERVER_URL` ओव्हरराइड करतो |
| `AZURE_OPENAI_ENDPOINT` | फक्त AI क्लायंटसाठी आवश्यक; रिसोर्स URL किंवा `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; एक Azure डिप्लॉयमेंट नाव |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; सकारात्मक पूर्णांक |
| तार्किक प्रयत्न | नेहमी `none`, साधन-लूप फॉलो-अप्ससह |

ओव्हरराइड केलेल्या डिप्लॉयमेंटला `reasoning_effort=none` आणि `max_completion_tokens` समर्थन करणे आवश्यक आहे.
क्लायंट स्वयंचलितपणे `.env` फाईल वाचत नाहीत. परीक्षणानंतर `Ctrl+C` ने सर्व्हर थांबवा.
क्लायंट सामान्यपणे परततात `System.exit` किंवा शटडाउन स्लीपशिवाय.

## ऑफलाइन चाचण्या

```powershell
mvn -B -ntp clean verify
```

सर्व चाचण्या Azure संदर्भात ऑफलाइन आहेत: प्रोटोकॉल सेट सुरू करतो एक Spring सर्व्हर आणि
OpenAI-सुसंगत स्टब यादृच्छिक लूपबॅक पोर्टवर, नंतर त्यांना बंद करतो. Maven कदाचित अजूनही अवलंबित्वे डाउनलोड करावी लागतील.
कोणतेही क्रेडेन्शियल्स, लाइव्ह डिप्लॉयमेंट किंवा पूर्वस्थित MCP सर्व्हर वापरले जात नाहीत.

- कॅल्क्युलेटर युनिट चाचण्या सर्व अंकगणित ऑपरेशन्स, दशांश परिणाम, मदत, आणि डोमेन त्रुटी कव्हर करतात.
- MCP चाचण्या प्रारंभिकीकरण, डिस्कव्हरी, सर्व नऊ साधन कॉल्स, साधन अपयश, आणि हेल्थ/माहिती कव्हर करतात.
- AI प्रोटोकॉल चाचण्या वास्तविक कॅल्क्युलेटरवर संपूर्ण डेमो आणि संवादात्मक बॉट चालवतात,
  तपासतात की साधन परिणाम पुढील पूर्णतेसाठी पोहोचतात, आणि प्रत्येक HTTP बॉडी तपासतात Luna,
  `reasoning_effort: "none"`, आणि `max_completion_tokens` ज्यात कोणताही जुना `max_tokens` नाही.
- कॉन्फिगरेशन/इनपुट चाचण्या डिप्लॉयमेंट आणि एन्डपॉईंट ओव्हरराइड्स, रिकाम्या ओळी, EOF, इक्झिट/क्विट,
  सिंगल-प्रॉम्प्ट मोड, अवैध पर्याय, आणि त्रुटी प्रसार कव्हर करतात. कोटा चाचण्या सिद्ध करतात की 429 पुनर्प्रयत्न होत नाही.

## ते कसे एकत्र कार्य करतात

येथे पूर्ण प्रक्रिया आहे जेव्हा आपण AI ला विचारता "5 + 3 काय आहे?":

1. **आपण** नैसर्गिक भाषेत AI ला विचारता
2. **AI** आपल्या विनंतीचे विश्लेषण करते आणि समजते की आपण बेरीज करू इच्छिता
3. **AI** MCP सर्व्हरला कॉल करते: `add(5.0, 3.0)`
4. **कॅल्क्युलेटर सेवा** करते: `5.0 + 3.0 = 8.0`
5. **कॅल्क्युलेटर सेवा** परत करते: `"5.00 + 3.00 = 8.00"`
6. **AI** परिणाम प्राप्त करते आणि नैसर्गिक प्रतिसाद तयार करते
7. **आपण** मिळवता: "5 आणि 3 ची बेरीज 8 आहे"

## पुढील पावले

अधिक उदाहरणांसाठी, पाहा [अध्याय 04: व्यावहारिक नमुने](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. जरी आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थलावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->