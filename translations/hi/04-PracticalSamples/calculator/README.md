# शुरुआती लोगों के लिए MCP कैलकुलेटर ट्यूटोरियल

## सामग्री तालिका

- [आप क्या सीखेंगे](#आप-क्या-सीखेंगे)
- [पूर्वापेक्षाएँ](#पूर्वापेक्षाएँ)
- [निर्भरता संस्करण](#निर्भरता-संस्करण)
- [परियोजना संरचना को समझना](#परियोजना-संरचना-को-समझना)
- [कोर घटकों की व्याख्या](#कोर-घटकों-की-व्याख्या)
  - [1. मुख्य एप्लिकेशन](#1-मुख्य-एप्लिकेशन)
  - [2. कैलकुलेटर सेवा](#2-कैलकुलेटर-सेवा)
  - [3. डायरेक्ट MCP क्लाइंट](#3-डायरेक्ट-mcp-क्लाइंट)
  - [4. AI-संचालित क्लाइंट](#4-ai-संचालित-क्लाइंट)
- [उदाहरण चलाना](#उदाहरण-चलाना)
- [ऑफ़लाइन परीक्षण](#ऑफ़लाइन-परीक्षण)
- [यह सब कैसे काम करता है](#यह-सब-कैसे-काम-करता-है)
- [अगले कदम](#अगले-कदम)

## आप क्या सीखेंगे

यह ट्यूटोरियल मॉडल कंटेक्स्ट प्रोटोकॉल (MCP) का उपयोग करके कैलकुलेटर सेवा बनाने का तरीका समझाता है। आप जानेंगे:

- एक ऐसी सेवा कैसे बनाएं जिसे AI एक उपकरण के रूप में उपयोग कर सके
- MCP सेवाओं के साथ सीधे संचार सेटअप कैसे करें
- AI मॉडल स्वचालित रूप से किस उपकरण का उपयोग करना है यह कैसे चुन सकते हैं
- सीधे प्रोटोकॉल कॉल और AI-सहायक इंटरैक्शन के बीच का अंतर

## पूर्वापेक्षाएँ

शुरू करने से पहले, सुनिश्चित करें कि आपके पास है:
- Java 21 या उससे ऊपर स्थापित
- निर्भरता प्रबंधन के लिए Maven
- Java और Spring Boot की बुनियादी समझ

केवल AI क्लाइंट्स को Azure OpenAI तैनाती और प्रमाणीकृत `DefaultAzureCredential` की आवश्यकता है,
जैसे स्थानीय रूप से मौजूद Azure CLI साइन-इन या Azure में एक प्रबंधित आईडेंटिटी। यह आईडेंटिटी
संसाधन पर Cognitive Services OpenAI यूज़र भूमिका आवश्यक है। देखें [अध्याय 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md)।
सर्वर, डायरेक्ट SDK क्लाइंट, और सभी स्वचालित परीक्षणों को किसी Azure खाते या मॉडल एक्सेस की आवश्यकता नहीं है।

## निर्भरता संस्करण

2026-09-14 को सत्यापन की गई रिलीज निर्भरताएँ:

| निर्भरता | संस्करण |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-प्रबंधित) | 2.0.0 |
| LangChain4j / कोर | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j आधिकारिक OpenAI एडाप्टर | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-प्रबंधित) | 6.0.3 |

MCP और आधिकारिक OpenAI एडाप्टर Maven Central में प्रकाशित बीटा रिलीज़ हैं, स्नैपशॉट नहीं।
इनके संस्करण LangChain4j कोर से भिन्न हैं। किसी स्नैपशॉट या माइलस्टोन रिपोजिटरी की आवश्यकता नहीं है।
क्लाइंट-केवल निर्भरता का टेस्ट स्कोप है क्योंकि रन करने योग्य उदाहरण `src/test/java` में हैं।

## परियोजना संरचना को समझना

कैलकुलेटर प्रोजेक्ट में कई महत्वपूर्ण फाइलें हैं:

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

## कोर घटकों की व्याख्या

### 1. मुख्य एप्लिकेशन

**फ़ाइल:** `McpServerApplication.java`

यह हमारे कैलकुलेटर सेवा का प्रवेश बिंदु है। यह एक मानक Spring Boot एप्लिकेशन है जिसमें एक विशेष जोड़ है:

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

**यह क्या करता है:**
- पोर्ट 8080 पर एक Spring Boot वेब सर्वर शुरू करता है
- एक `ToolCallbackProvider` बनाता है जो हमारे कैलकुलेटर मेथड्स को MCP उपकरणों के रूप में उपलब्ध कराता है
- `@Bean` एनोटेशन Spring को बताता है कि इसे एक ऐसा घटक माना जाए जिसे अन्य भाग उपयोग कर सकते हैं

### 2. कैलकुलेटर सेवा

**फ़ाइल:** `CalculatorService.java`

यहाँ सभी गणितीय गणनाएँ होती हैं। प्रत्येक मेथड को `@Tool` से चिह्नित किया गया है ताकि इसे MCP के माध्यम से उपलब्ध कराया जा सके:

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
    
    // और गणक ऑपरेशन...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**मुख्य विशेषताएँ:**

1. **`@Tool` एनोटेशन**: यह MCP को बताता है कि यह मेथड बाहरी क्लाइंट्स द्वारा कॉल किया जा सकता है
2. **स्पष्ट विवरण**: प्रत्येक उपकरण का एक विवरण होता है जो AI मॉडल को समझने में मदद करता है कि इसे कब उपयोग करना है
3. **संगत रिटर्न प्रारूप**: सभी ऑपरेशन मानव-पठनीय स्ट्रिंग्स लौटाते हैं जैसे "5.00 + 3.00 = 8.00"
4. **त्रुटि हैंडलिंग**: शून्य द्वारा विभाजन और नकारात्मक वर्गमूल त्रुटि संदेश लौटाते हैं

**उपलब्ध ऑपरेशन:**
- `add(a, b)` - दो संख्याओं का जोड़ करता है
- `subtract(a, b)` - दूसरी संख्या को पहली से घटाता है
- `multiply(a, b)` - दो संख्याओं को गुणा करता है
- `divide(a, b)` - पहली संख्या को दूसरी से विभाजित करता है (शून्य जांच के साथ)
- `power(base, exponent)` - बेस को एक्सपोनेंट की शक्ति तक उठाता है
- `squareRoot(number)` - वर्गमूल निकालता है (नकारात्मक जांच के साथ)
- `modulus(a, b)` - भागफल का शेषफल लौटाता है
- `absolute(number)` - पूर्णांक मान लौटाता है
- `help()` - सभी ऑपरेशन की जानकारी लौटाता है

### 3. डायरेक्ट MCP क्लाइंट

देखें [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)।

यह क्लाइंट `/mcp` पर `HttpClientStreamableHttpTransport` का उपयोग करता है, कनेक्शन प्रारंभ करता है,
सर्वर को पिंग करता है, और टूल-सूची पेजिनेशन का पालन करता है। यह जांचता है कि सभी अपेक्षित नौ उपकरण मौजूद हैं,
और प्रत्येक को कॉल करता है, जिसमें `modulus` और `help` शामिल हैं, बिना AI मॉडल के।

वर्तमान अनुरोध बिल्डर इस प्रकार दिखता है:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

प्रोटोकॉल त्रुटियाँ क्लाइंट को असफल बनाती हैं बजाय भ्रामक सफलता संदेश के। MCP क्लाइंट को
try-with-resources के साथ बंद किया जाता है, चाहे डिस्कवरी विफल हो या कोई टूल कॉल असफल हो।

### 4. AI-संचालित क्लाइंट

देखें [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
और [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)।

`OpenAiOfficialChatModel` वर्तमान LangChain4j `ChatModel` API को लागू करता है।
`StreamableHttpMcpTransport` इसे SDK क्लाइंट के समान `/mcp` एन्डपॉइंट से जोड़ता है।
`AiServices` टूल्स की खोज करता है और टूल-कॉल/रिजल्ट वार्तालाप का प्रबंधन करता है।

डिफ़ॉल्ट तैनाती है **GPT-5.6 Luna**, जिसमें कारण देने का प्रयास स्पष्ट रूप से अक्षम है:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

ये डिफ़ॉल्ट प्रत्येक पूर्णता पर लागू होते हैं, जिसमें टूल निष्पादन के बाद की फॉलो-अप भी शामिल हैं।
क्लाइंट एक रिफ्रेश योग्य `BearerTokenCredential` का उपयोग करता है जो `DefaultAzureCredential` पर आधारित है
और `https://ai.azure.com/.default` स्कोप का उपयोग करता है, न कि एक बार उपयोग होने वाला टोकन जो API की के रूप में पास किया जाता है।
संसाधन URL और `/openai/v1` पर समाप्त होने वाले URL दोनों स्वीकार किए जाते हैं।

बोट एक सीमित वार्तालाप इतिहास रखता है, `Tool executed: ...` प्रिंट करता है वास्तविक
MCP परिणाम के साथ, और यदि कोई प्रतिक्रिया उपकरणों को छोड़ती है तो विफल हो जाता है। टूल लूप चार राउंड ट्रिप तक सीमित हैं।
प्रमाणीकरण, मॉडल, MCP, और टूल त्रुटियाँ फैलती हैं; स्वचालित मॉडल पुनः प्रयास अक्षम हैं।
MCP ट्रांसपोर्ट/क्लाइंट और आधिकारिक OpenAI क्लाइंट दोनों सफलता या विफलता पर बंद किए जाते हैं।

## उदाहरण चलाना

### चरण 1: कैलकुलेटर सर्वर शुरू करें

सर्वर के लिए कोई Azure कॉन्फ़िगरेशन आवश्यक नहीं है। नीचे दिए गए कमांड इस नमूने की निर्देशिका से चलाएं।
उदाहरण टकराव से बचने के लिए पोर्ट **18081** का उपयोग करता है; डिफ़ॉल्ट अभी भी 8080 है।

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP एन्डपॉइंट है `http://localhost:18081/mcp`। स्वास्थ्य और डिस्कवरी जानकारी हैं
`http://localhost:18081/health` और `http://localhost:18081/info` पर।
Streamable HTTP पुराने SSE-केवल ट्रांसपोर्ट की जगह लेता है; `/sse` और `/v1/tools` एन्डपॉइंट नहीं हैं।

### चरण 2: सीधे क्लाइंट के साथ परीक्षण करें

एक अन्य PowerShell टर्मिनल में:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

कोई इनपुट आवश्यक नहीं है। सभी नौ उपकरणों का परीक्षण होता है। अपेक्षित अंकगणितीय परिणामों में शामिल हैं
8, 6, 42, 5, 256, 4, 2, और 5.5, उसके बाद सहायता पाठ।

### चरण 3: AI क्लाइंट के साथ परीक्षण करें

पूर्वापेक्षाओं के अनुसार प्रमाणीकरण के बाद, AI क्लाइंट को उसी टर्मिनल में कॉन्फ़िगर करें:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

अपेक्षा करें `Tool executed: add` लाइन के साथ `41.80`, उसके बाद मॉडल का उत्तर।
सिंगल प्रॉम्प्ट मोड बिना इनपुट की प्रतीक्षा के बाहर निकल जाता है। मूल चार-प्रॉम्प्ट डेमो चलाने के लिए:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

डेमो `add`, `squareRoot`, `help`, और चेन किए गए `power` फिर `divide` ऑपरेशन को कॉल करता है।
अपेक्षित संख्यात्मक उत्तर हैं 41.8, 12, और 64। तर्क छोड़ने पर भी यह डेमो चलाया जाता है।

### चरण 4: इंटरैक्टिव बॉट चलाएँ

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

दर्ज करें `Multiply 6 by 7 using the calculator service`, फिर `exit` या `quit`।
एक वास्तविक `multiply` टूल परिणाम 42 की उम्मीद करें। खाली पंक्तियाँ अनदेखी की जाती हैं; EOF सेशन भी समाप्त करता है।
इस प्रवेश बिंदु के नॉनइंटरेक्टिव स्मोक टेस्ट के लिए:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

दोनों AI प्रवेश बिंदु `--prompt "question"`, `--demo`, और `--interactive` स्वीकार करते हैं।
अमान्य विकल्प कनेक्शन खोलने से पहले विफल हो जाते हैं। प्रत्येक Maven `-D...` तर्क के लिए पावरशेल में पूर्णविराम किया जाता है।
Bash में, `$env:NAME = "value"` की जगह `export NAME=value` का उपयोग करें।

**कोटा:** AI उदाहरणों को क्रमशः चलाएँ। एक साधारण प्रॉम्प्ट को सामान्यतः दो मॉडल अनुरोध चाहिए होते हैं;
पूरा डेमो सामान्यतः नौ आवश्यक होते हैं, जिसमें टूल-परिणाम फॉलो-अप शामिल हैं। साझा 10 RPM
तैनाती के साथ, अगले AI रन से पहले नया कोटा विंडो अनुमति दें। 429 त्रुटि दिखाई देती है बिना
स्वचालित पुनः प्रयास के; सेवा के पुनः प्रयास-पछला निर्देशन का पालन करें। वास्तविक अनुरोध संख्या मॉडल पर निर्भर करती है।
ऑफ़लाइन परीक्षण कोई कोटा नहीं उपयोग करते और लाइव Luna उपलब्धता या उत्तर गुणवत्ता स्थापित नहीं करते।

### कॉन्फ़िगरेशन और शटडाउन

| सेटिंग | डिफ़ॉल्ट / व्यवहार |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; बेस URL, `/mcp` के बिना |
| `-Dmcp.server.url=...` | सभी क्लाइंट्स के लिए `MCP_SERVER_URL` को ओवरराइड करता है |
| `AZURE_OPENAI_ENDPOINT` | केवल AI क्लाइंट्स के लिए आवश्यक; संसाधन URL या `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; एक Azure तैनाती नाम |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; धनात्मक पूर्णांक |
| कारण देने का प्रयास | हमेशा `none`, टूल-लूप फॉलो-अप सहित |

ओवरराइड तैनाती को `reasoning_effort=none` और `max_completion_tokens` का समर्थन करना चाहिए।
क्लाइंट अपने आप `.env` फाइल पढ़ते नहीं हैं। परीक्षण के बाद सर्वर को `Ctrl+C` से बंद करें।
क्लाइंट सामान्यतः बिना `System.exit` या शटडाउन स्लीप के लौटते हैं।

## ऑफ़लाइन परीक्षण

```powershell
mvn -B -ntp clean verify
```

सभी परीक्षण Azure के संदर्भ में ऑफ़लाइन हैं: प्रोटोकॉल सूट एक Spring सर्वर और
OpenAI-संगत स्टब को रैंडम लूपबैक पोर्ट्स पर शुरू करता है, फिर उन्हें बंद कर देता है। Maven को
依然依赖下载 करना पड़ सकता है। कोई क्रेडेंशियल, लाइव तैनाती, या पूर्व-मौजूद MCP सर्वर उपयोग नहीं किया जाता है।

- कैलकुलेटर यूनिट टेस्ट में सभी अंकगणितीय ऑपरेशन, दशमलव परिणाम, सहायता और डोमेन त्रुटियाँ शामिल हैं।
- MCP टेस्ट में इनिशियलाइजेशन, डिस्कवरी, सभी नौ टूल कॉल, टूल फेल्योर, और स्वास्थ्य/जानकारी शामिल हैं।
- AI प्रोटोकॉल टेस्ट वास्तविक कैलकुलेटर के खिलाफ पूर्ण डेमो और इंटरैक्टिव बॉट चलाते हैं,
  पुष्टि करते हैं कि टूल परिणाम अगले पूर्णता को फीड करते हैं, और हर HTTP बॉडी में Luna,
  `reasoning_effort: "none"`, और `max_completion_tokens` के साथ बिना `max_tokens` के निरीक्षण करते हैं।
- कॉन्फ़िगरेशन/इनपुट टेस्ट तैनाती और एन्डपॉइंट ओवरराइड, खाली पंक्तियाँ, EOF, exit/quit,
  सिंगल-प्रॉम्प्ट मोड, अमान्य विकल्प, और त्रुटि प्रसार कवर करते हैं। कोटा टेस्ट यह साबित करते हैं कि 429 पुनः प्रयास नहीं किया जाता।

## यह सब कैसे काम करता है

जब आप AI से पूछते हैं "5 + 3 क्या है?", तो पूरी प्रक्रिया इस प्रकार है:

1. **आप** प्राकृतिक भाषा में AI से पूछते हैं
2. **AI** आपके अनुरोध का विश्लेषण करता है और समझता है कि आप जोड़ चाहते हैं
3. **AI** MCP सर्वर को कॉल करता है: `add(5.0, 3.0)`
4. **कैलकुलेटर सेवा** प्रदर्शन करता है: `5.0 + 3.0 = 8.0`
5. **कैलकुलेटर सेवा** लौटाती है: `"5.00 + 3.00 = 8.00"`
6. **AI** परिणाम प्राप्त करता है और प्राकृतिक प्रतिक्रिया बनाता है
7. **आपको** यह मिलता है: "5 और 3 का योग 8 है"

## अगले कदम

अधिक उदाहरणों के लिए, देखें [अध्याय 04: व्यावहारिक नमूने](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में ही प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->