# MCP क्यालकुलेटर ट्यूटोरियल प्रारम्भिकहरूको लागि

## सामग्री तालिका

- [तपाईंले के सिक्नुहुनेछ](#तपाईंले-के-सिक्नुहुनेछ)
- [पहिले जान्नुपर्ने कुरा](#पहिले-जान्नुपर्ने-कुरा)
- [निर्भरता संस्करणहरू](#निर्भरता-संस्करणहरू)
- [प्रोजेक्ट संरचना बुझ्नुहोस्](#प्रोजेक्ट-संरचना-बुझ्नुहोस्)
- [मुख्य कम्पोनेन्टहरू बुझ्नुहोस्](#मुख्य-कम्पोनेन्टहरू-बुझाउनुहोस्)
  - [1. मुख्य एप्लिकेशन](#1-मुख्य-एप्लिकेशन)
  - [2. क्यालकुलेटर सेवा](#2-क्यालकुलेटर-सेवा)
  - [3. प्रत्यक्ष MCP क्लाइन्ट](#3-प्रत्यक्ष-mcp-क्लाइन्ट)
  - [4. एआई-संचालित क्लाइन्ट](#4-एआई-सञ्चालित-क्लाइन्ट)
- [उदाहरणहरू चलाउनुहोस्](#उदाहरणहरू-चलाउन)
- [अफलाइन परीक्षणहरू](#अफलाइन-परीक्षणहरू)
- [सबै कुरा कसरी काम गर्छ](#सबै-कुरा-कसरी-संगै-काम-गर्छ)
- [अर्को चरणहरू](#अर्को-चरणहरू)

## तपाईंले के सिक्नुहुनेछ

यस ट्यूटोरियलले मोडेल कन्टेक्स्ट प्रोटोकल (MCP) प्रयोग गरेर क्यालकुलेटर सेवा कसरी बनाउने बताउँछ। तपाईं बुझ्नुहुनेछ:

- एआईले उपकरणको रुपमा प्रयोग गर्न सक्ने सेवा कसरी बनाउने
- MCP सेवाहरुसँग प्रत्यक्ष संचार कसरी सेटअप गर्ने
- एआई मोडेलहरूले स्वचालित रूपमा कुन उपकरणहरू प्रयोग गर्ने चयन कसरी गर्ने
- प्रत्यक्ष प्रोटोकल कलहरू र एआई-सहायता प्राप्त अन्तरक्रियाहरू बीचको फरक के हो

## पहिले जान्नुपर्ने कुरा

सुरु गर्नु अघि, सुनिश्चित गर्नुहोस् कि तपाईं सँग:
- Java 21 वा माथि स्थापना छ
- Maven निर्भरता प्रबन्धनको लागि
- Java र Spring Boot को आधारभूत बुझाइ छ

केवल एआई क्लाइन्टहरूले Azure OpenAI डिप्लोइमेन्ट र प्रमाणीकरण गरिएको `DefaultAzureCredential` आवश्यक पर्छ,
जस्तै कि स्थानीय रूपमा पहिले नै Azure CLI सँग साइन-इन वा Azure मा प्रबन्धित पहिचान। पहिचानले
Cognitive Services OpenAI प्रयोगकर्ता भूमिका स्रोतमा आवश्यक पर्छ। हेर्नुहोस् [अध्याय 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md)।
सर्भर, प्रत्यक्ष SDK क्लाइन्ट, र सबै स्वचालित परीक्षणहरूले कुनै Azure खाता वा मोडेल पहुँच आवश्यक पर्दैन।

## निर्भरता संस्करणहरू

२०२६-०९-१४ मा प्रमाणित रिलिज निर्भरता:

| निर्भरता | संस्करण |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-प्रबन्धित) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j आधिकारिक OpenAI एडाप्टर | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-प्रबन्धित) | 6.0.3 |

MCP र आधिकारिक OpenAI एडाप्टरहरू Maven सेंट्रलमा प्रकाशित बीटा रिलिजहरू हुन्, स्न्यापशटहरू होइनन्।
तिनीहरूको संस्करण LangChain4j core बाट फरक छ। कुनै स्न्यापशट वा माइलस्टोन रिपोजिटरी आवश्यक छैन।
केवल क्लाइन्ट निर्भरता परीक्षण स्कोपमा छन् किनकि चलाउन मिल्ने उदाहरणहरू `src/test/java` अन्तर्गत छन्।

## प्रोजेक्ट संरचना बुझ्नुहोस्

क्यालकुलेटर प्रोजेक्टमा केहि महत्वपूर्ण फाइलहरू छन्:

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

## मुख्य कम्पोनेन्टहरू बुझाउनुहोस्

### 1. मुख्य एप्लिकेशन

**फाइल:** `McpServerApplication.java`

यो हाम्रो क्यालकुलेटर सेवाको प्रवेश बिन्दु हो। यो एक मानक Spring Boot एप्लिकेशन हो जुन एउटा विशेष थपिएको कुरा छ:

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

**यो के गर्छ:**
- पोर्ट ८०८० मा Spring Boot वेब सर्भर सुरु गर्छ
- `ToolCallbackProvider` बनाउँछ जुन हाम्रो क्यालकुलेटर विधिहरूलाई MCP उपकरणहरूका रूपमा उपलब्ध गराउँछ
- `@Bean` एनोटेशनले Spring लाई यो कम्पोनेन्टको रूपमा व्यवस्थापन गर्न बताउँछ जुन अरु भागहरूले प्रयोग गर्न सक्छन्

### 2. क्यालकुलेटर सेवा

**फाइल:** `CalculatorService.java`

यहाँ सबै गणित हुन्छ। प्रत्येक विधि `@Tool` ले चिन्हित गरिएको छ ताकि यो MCP मार्फत उपलब्ध होस्:

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
    
    // थप क्यालकुलेटर अपरेसनहरू...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**मुख्य विशेषताहरू:**

1. **`@Tool` एनोटेशन**: यसले MCP लाई बताउँछ कि यो विधि बाह्य क्लाइन्टहरूले कल गर्न सक्छन्
2. **स्पष्ट वर्णनहरू**: प्रत्येक उपकरणसँग एआई मोडेलहरूले कहिले प्रयोग गर्ने बुझ्न मद्दत गर्ने वर्णन छ
3. **सामञ्जस्यपूर्ण फिर्ता ढाँचा**: सबै अपरेसनहरूले मानव-पठनीय स्ट्रिङहरू फिर्ता गर्छन् जस्तै "5.00 + 3.00 = 8.00"
4. **त्रुटि ह्यान्डलिंग**: शून्यले भाग गर्दा र नकारात्मक वर्गमूलले त्रुटि सन्देश फिर्ता गर्छ

**उपलब्ध अपरेसनहरू:**
- `add(a, b)` - दुई संख्याहरू जोड्छ
- `subtract(a, b)` - दोस्रोमा पहिलोबाट घटाउँछ
- `multiply(a, b)` - दुई संख्याहरू गुणा गर्छ
- `divide(a, b)` - पहिलोलाई दोस्रोले भाग गर्छ (शून्य जाँचसहित)
- `power(base, exponent)` - आधारलाई घातांकमा उठाउँछ
- `squareRoot(number)` - वर्गमूल गणना गर्छ (नकारात्मक जाँचसहित)
- `modulus(a, b)` - भाग बाँकी फिर्ता गर्छ
- `absolute(number)` - पूर्ण मान फिर्ता गर्छ
- `help()` - सबै अपरेसनहरूको समाचार फिर्ता गर्छ

### 3. प्रत्यक्ष MCP क्लाइन्ट

हेर्नुहोस् [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)

यो क्लाइन्ट `HttpClientStreamableHttpTransport` लाई `/mcp` मा प्रयोग गर्छ, जडान सुरु गर्छ,
सर्भरलाई पिङ्ग गर्छ, र उपकरण सूची पेजिनेशन अनुसरण गर्छ। यो सबै नौ अपेक्षित उपकरणहरू छन् कि छैनन् जांच्छ
र प्रत्येकलाई कल गर्छ, `modulus` र `help` सहित, एआई मोडेल बिना।

हालको अनुरोध बिल्डर यसरी देखिन्छ:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

प्रोटोकल त्रुटिहरूले गलत सफलता देखाउने सट्टा क्लाइन्ट असफल बनाउँछन्। MCP क्लाइन्ट
प्रयत्न-का-संसाधनहरूसँग बन्द गरिन्छ, पत्ता लगाउने वा उपकरण कल असफल हुँदा पनि।

### 4. एआई-सञ्चालित क्लाइन्ट

हेर्नुहोस् [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
र [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)।

`OpenAiOfficialChatModel` हालको LangChain4j `ChatModel` API लागू गर्छ।
`StreamableHttpMcpTransport` यसलाई SDK क्लाइन्ट जस्तै `/mcp` अन्तमा जडान गर्छ।
`AiServices` उपकरणहरू खोज्छ र उपकरण कल/परिणाम संवाद व्यवस्थापन गर्दछ।

डिफल्ट डिप्लोइमेन्ट **GPT-5.6 Luna** हो, जसमा कारण बताउने प्रयास स्पष्ट रूपमा अक्षम गरिएको छ:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

यी डिफल्टहरू प्रत्येक पूर्णतामा लागू हुन्छन्, उपकरण कार्यान्वयनपछि फलो-अपमहरू सहित।
क्लाइन्टले `DefaultAzureCredential` मा समर्थित रिफ्रेश योग्य `BearerTokenCredential` प्रयोग गर्छ
र `https://ai.azure.com/.default` स्कोप, एकपटक मात्र टोकन नभई।
स्रोत URLहरू र `/openai/v1` मा समाप्त हुने URLहरू दुबै स्वीकार्य छन्।

बोटले सीमित संवाद इतिहास राख्छ, `Tool executed: ...` मुद्रण गर्छ वास्तविक
MCP परिणामसहित, र यदि प्रतिक्रिया उपकरणहरू छोड्छ भने असफल हुन्छ। उपकरण चक्रहरू चार राउन्ड ट्रिपमा सीमित छन्।
प्रमाणीकरण, मोडेल, MCP, र उपकरण त्रुटिहरू फैलन्छन्; स्वचालित मोडेल पुन: प्रयासहरू अक्षम छन्।
MCP ट्रान्सपोर्ट/क्लाइन्ट र आधिकारिक OpenAI क्लाइन्ट दुबै सफल वा असफलतामा बन्द हुन्छन्।

## उदाहरणहरू चलाउन

### चरण १: क्यालकुलेटर सर्भर सुरु गर्नुहोस्

सर्भरका लागि कुनै Azure कन्फिगरेसन आवश्यक छैन। तलका आदेशहरू यस नमुना डाइरेक्टरीबाट चलाउनुहोस्।
उदाहरणले अर्को नमुनासँग टकराव टार्न **१८०८१** पोर्ट प्रयोग गर्छ; डिफल्ट 8080 नै छ।

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP अन्त बिन्दु `http://localhost:18081/mcp` हो। स्वास्थ्य र पत्ता लगाउने जानकारीहरू छन्
`http://localhost:18081/health` र `http://localhost:18081/info` मा।
Streamable HTTP पुरानो SSE मात्र यातायातलाई प्रतिस्थापन गर्छ; `/sse` र `/v1/tools` अन्त बिन्दुहरू होइनन्।

### चरण २: प्रत्यक्ष क्लाइन्टसँग परीक्षण गर्नुहोस्

अर्को PowerShell टर्मिनलमा:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

कुनै इनपुट आवश्यक छैन। सबै नौ उपकरणहरू व्यायाम गरिन्छ। अपेक्षित अंकगणित परिणामहरूमा
8, 6, 42, 5, 256, 4, 2, र 5.5 छन्, त्यसपछि सहायता पाठ आउँछ।

### चरण ३: एआई क्लाइन्टसँग परीक्षण गर्नुहोस्

पूर्व आवश्यकताहरू अनुसार प्रमाणीकरण गरेपछि, सोही टर्मिनलमा एआई क्लाइन्ट कन्फिगर गर्नुहोस्:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

अपेक्षा गर्नुहोस् `Tool executed: add` लाइन `41.80` सहित, त्यसपछि मोडेलको उत्तर।
एकल-प्राम्प्ट मोड इनपुट कुर्न नपरी बन्द हुन्छ। मूल चार-प्राम्प्ट डेमो चलाउन:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

डेमोले `add`, `squareRoot`, `help`, र श्रृंखलाबद्ध `power` अनि `divide` अपरेसन कल गर्छ।
अपेक्षित अंकगणित उत्तरहरू 41.8, 12, र 64 छन्। तर्कहरू छोड्दा पनि यो डेमो चल्छ।

### चरण ४: अन्तरक्रियात्मक बोट चलाउनुहोस्

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

`Multiply 6 by 7 using the calculator service` टाइप गर्नुहोस्, त्यसपछि `exit` वा `quit` गर्नुहोस्।
वास्तविक `multiply` उपकरणको परिणाम 42 अपेक्षित छ। खाली लाइनहरू बेवास्ता गरिन्छ; EOF ले पनि सत्र समाप्त गर्छ।
यो प्रवेश बिन्दुको गैर-अन्तरक्रियात्मक स्मोक टेस्टका लागि:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

दुबै एआई प्रवेश बिन्दुले `--prompt "question"`, `--demo`, र `--interactive` स्वीकार्छन्।
अवैध विकल्पहरूले जडान खोलेभन्दा अघि असफल हुन्छन्। प्रत्येक Maven `-D...` तर्क राम्रोसँग उद्धृत हुनु पर्छ
PowerShell का लागि। Bash मा, `$env:NAME = "value"` सट्टा `export NAME=value` प्रयोग गर्नुहोस्।

**कोटा:** एआई नमूनाहरूलाई क्रमिक रूपमा चलाउनुहोस्। सरल प्राम्प्टले सामान्यतया दुई मोडेल अनुरोध आवश्यक पर्छ;
पूर्ण डेमोलाई सामान्यतया नौ आवश्यक पर्छ, उपकरण-परिणाम फलोअप सहित। साझा १० RPM
डिप्लोइमेन्टसँग, अर्को AI रन अघि नयाँ कोटा विन्डो अनुमति दिनुहोस्। 429 स्पष्ट असफल हुन्छ स्वतः पुन: प्रयास बिना;
सेवाको पुन: प्रयास-पछि निर्देशक पालना गर्नुहोस्। वास्तविक अनुरोध संख्या मोडेलमा निर्भर गर्दछ।
अफलाइन परीक्षणहरूले कुनै कोटा खपत गर्दैनन् र जीवित Luna उपलब्धता वा उत्तर गुणस्तर पुष्टि गर्दैनन्।

### कन्फिगरेसन र बन्द गर्नुहोस्

| सेटिङ | डिफल्ट / व्यवहार |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; आधार URL, `/mcp` बिना |
| `-Dmcp.server.url=...` | सबै क्लाइन्टहरूको लागि `MCP_SERVER_URL` ओभरराइड गर्छ |
| `AZURE_OPENAI_ENDPOINT` | केवल AI क्लाइन्टहरूको लागि आवश्यक; स्रोत URL वा `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure डिप्लोइमेन्ट नाम |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; सकारात्मक पूर्णांक |
| कारण बताउने प्रयास | सधैं `none`, उपकरण चक्र फलोअप सहित |

ओभरराइड गरिएको डिप्लोइमेन्टले `reasoning_effort=none` र `max_completion_tokens` समर्थन गर्नुपर्छ।
क्लाइन्टहरूले `.env` फाइल स्वचालित रूपमा पढ्दैनन्। परीक्षण पछि सर्भरलाई `Ctrl+C` दबाएर रोक्नुहोस्।
क्लाइन्टहरूले सामान्यतया `System.exit` वा बन्द सुताइहरू बिना फिर्ता गर्छन्।

## अफलाइन परीक्षणहरू

```powershell
mvn -B -ntp clean verify
```

सबै परीक्षणहरू Azure सन्दर्भमा अफलाइन छन्: प्रोटोकल सुइटले Spring सर्भर सुरु गर्छ र
OpenAI-संगत स्टब र्यान्डम लूपब्याक पोर्टहरूमा, त्यसपछि तिनीहरू बन्द गर्छ। Maven ले अझै निर्भरता डाउनलोड गर्न सक्छ।
कुनै प्रमाणपत्र, जीवित डिप्लोइमेन्ट, वा पहिलेको MCP सर्भर प्रयोग गरिंदैन।

- क्यालकुलेटर युनिट परीक्षणहरूले सबै अंकगणित अपरेसनहरू, दशमलव परिणामहरू, सहायता, र क्षेत्रमा दोषहरू समेट्छ।
- MCP परीक्षणहरूले आरम्भ, पत्ता लगाउने, सबै नौ उपकरण कलहरू, उपकरण असफलताहरू, र स्वास्थ्य/जानकारी समेट्छ।
- AI प्रोटोकल परीक्षणहरूले वास्तविक क्यालकुलेटर विरुद्ध पूर्ण डेमो र अन्तरक्रियात्मक बोट चलाउँछन्,
  उपकरण परिणामहरूले अर्को पूर्णता खुवाउँछन् भनी सुनिश्चित गर्दछन्, र प्रत्येक HTTP बडी जाँच गर्छन् Luna,
  `reasoning_effort: "none"`, र `max_completion_tokens` कुनै पुरानो `max_tokens` नभएको।
- कन्फिगरेसन/इनपुट परीक्षणहरूले डिप्लोइमेन्ट र अन्त बिन्दु ओभरराइड, खाली लाइनहरू, EOF, निकास/छुट,
  एकल-प्राम्प्ट मोड, अवैध विकल्पहरू, र त्रुटि फैलावट समेट्छन्। कोटा परीक्षणहरूले देखाउँछन् 429 पुन: प्रयास हुँदैन।

## सबै कुरा कसरी संगै काम गर्छ

जब तपाईंले AI लाई "5 + 3 कति हुन्छ?" सोध्नुहुन्छ, यहाँ पूर्ण प्रवाह छ:

1. **तपाईं** प्राकृतिक भाषामा AI लाई सोध्नुहुन्छ
2. **AI** तपाईंको अनुरोध विश्लेषण गर्छ र थाहा पाउँछ तपाईंले जोड चाहनुहुन्छ
3. **AI** MCP सर्भरलाई कल गर्छ: `add(5.0, 3.0)`
4. **क्यालकुलेटर सेवा** गर्दछ: `5.0 + 3.0 = 8.0`
5. **क्यालकुलेटर सेवा** फिर्ता गर्छ: `"5.00 + 3.00 = 8.00"`
6. **AI** परिणाम पाउँछ र प्राकृतिक प्रतिक्रिया ढाँचा तयार गर्छ
7. **तपाईं** पाउनुहुन्छ: "5 र 3 को योगफल 8 हो"

## अर्को चरणहरू

थप उदाहरणहरूको लागि, हेर्नुहोस् [अध्याय ०४: व्यावहारिक नमूनाहरू](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी सही हुन प्रयास गर्छौं, तर कृपया जानकार हुनुस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छन्। मूल दस्तावेज़ यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा त्रुटिको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->