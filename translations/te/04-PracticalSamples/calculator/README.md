# ప్రారంభ దశలకు MCP క్యాల్క్యులేటర్ మార్గదర్శి

## విషయ సూచీ

- [మీరు ఏమి నేర్చుకుంటారు](#మీరు-ఏమి-నేర్చుకుంటారు)
- [ముందస్తు అవసరాలు](#ముందస్తు-అవసరాలు)
- [ఆశ్రిత సంస్కరణలు](#ఆశ్రిత-సంస్కరణలు)
- [ప్రాజెక్ట్ నిర్మాణం అవగాహన](#ప్రాజెక్ట్-నిర్మాణం-అవగాహన)
- [ముఖ్య భాగాల వివరణ](#ముఖ్య-భాగాల-వివరణ)
  - [1. প্রধান అనువర్తనం](#1-ప్రధాన-అనువర్తనం)
  - [2. క్యాల్క్యులేటర్ సేవ](#2-క్యాల్క్యులేటర్-సేవ)
  - [3. ప్రత్యక్ష MCP క్లయెంట్](#3-ప్రత్యక్ష-mcp-క్లయెంట్)
  - [4. AI-సమర్థించబడిన క్లయెంట్](#4-ai-సమర్థించబడిన-క్లయెంట్)
- [ఉదాహరణలను నడపడం](#ఉదాహరణలను-నడపడం)
- [ఆఫ్‌లైన్ పరీక్షలు](#ఆఫ్‌లైన్-పరీక్షలు)
- [ఇది ఎలా కలిసి పని చేస్తుంది](#ఇది-ఎలా-కలిసి-పని-చేస్తుంది)
- [తరువాతి దశలు](#తదుపరి-దశలు)

## మీరు ఏమి నేర్చుకుంటారు

ఈ మార్గదర్శి Model Context Protocol (MCP) ఉపయోగించి ఒక క్యాల్క్యులేటర్ సేవను ఎలా నిర్మించాలో వివరిస్తుంది. మీరు అర్థం చేసుకుంటారు:

- AI ఉపయోగించే సాధనం గా సర్వీస్ ఎలా సృష్టించాలి
- MCP సేవలతో ప్రత్యక్ష కమ్యూనికేషన్ ఎలా సెటప్ చేయాలి
- AI మోడల్లు ఏ సాధనాలను ఉపయోగించాలో స్వయంచాలకంగా ఎలా ఎంచుకుంటాయో
- ప్రత్యక్ష ప్రోటోకాల్ కాల్స్ మరియు AI సహాయంతో ఇంటరాక్షన్ మధ్య తేడా

## ముందస్తు అవసరాలు

ప్రారంభించడానికి ముందు, మీ వద్ద ఈవన్ తెలాలసినట్టు చూసుకోండి:
- జావా 21 లేదా అంతకంటే అధిక సంస్కరణ ఇన్స్టాల్ చేయబడింది
- డిపెండెన్సీ నిర్వహణ కోసం Maven
- జావా మరియు Spring Boot పై ప్రాథమిక అవగాహన

కేవలం AI క్లయెంట్లకే Azure OpenAI డిప్లాయ్‌మెంట్ మరియు ధృవీకృత `DefaultAzureCredential` అవసరం,
ఉదాహరణకు స్థానికంగా ఇప్పటికే ఉన్న Azure CLI సైన్-ఇన్ లేదా Azure లలో నిర్వహించే ఐడెంటిటీ. ఆ ఐడెంటిటీకి
Cognitive Services OpenAI User పాత్ర వనరుపై అవసరం. [అధ్యాయం 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) చూడండి.
సర్వర్, ప్రత్యక్ష SDK క్లయెంట్, మరియు అన్ని ఆటోమేటెడ్ పరీక్షలకు Azure ఖాతా లేదా మోడల్ యాక్సెస్ అవసరం లేదు.

## ఆశ్రిత సంస్కరణలు

2026-09-14 న నిర్ధారించిన విడుదల డిపెండెన్సీలు:

| డిపెండెన్సీ | సంస్కరణ |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-చే నిర్వహించబడుతుంది) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j అధికారిక OpenAI అడాప్టర్ | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-చే నిర్వహించబడుతుంది) | 6.0.3 |

MCP మరియు అధికారిక OpenAI అడాప్టర్లు Maven సెంట్రల్‌లో ప్రచురిత బీటా రిలీజ్‌లు, స్నాప్షాట్లు కాదు.
డిపెండెన్సీలు LangChain4j కోర్ నుండి వేరుగా ఉంటాయి. స్నాప్షాట్ లేదా మైలురాయి రిపాజిటరీలు అవసరం లేదు.
కస్టమర్-కే డిపెండెన్సీలు టెస్ట్ స్కోప్‌లో ఉంటాయి ఎందుకంటే నడపగలిగే ఉదాహరణలు `src/test/java` కింద ఉంటాయి.

## ప్రాజెక్ట్ నిర్మాణం అవగాహన

క్యాల్క్యులేటర్ ప్రాజెక్ట్‌లో కొన్ని ముఖ్యమైన ఫైళ్ళు ఉంటాయి:

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

## ముఖ్య భాగాల వివరణ

### 1. ప్రధాన అనువర్తనం

**ఫైల్:** `McpServerApplication.java`

ఇది మా క్యాల్క్యులేటర్ సేవకి ప్రవేశ పాయింట్. ఇది ఒక సాధారణ Spring Boot అనువర్తనం ఒక్క ప్రత్యేకమైన అదనంతో:

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

**ఇది ఏమి చేస్తుంది:**
- పోర్ట్ 8080 లో Spring Boot వెబ్ సర్వర్ ని ప్రారంభిస్తుంది
- మా క్యాల్క్యులేటర్ పద్ధతులు MCP టూల్స్ గా అందుబాటులో ఉంచే `ToolCallbackProvider` సృష్టిస్తుంది
- `@Bean` అనోటేషన్ Spring కి ఇది ఒక భాగంగా నిర్వహించమని వెల్లడిస్తుంది, ఇతర భాగాలు ఉపయోగించగలిగేలా

### 2. క్యాల్క్యులేటర్ సేవ

**ఫైల్:** `CalculatorService.java`

ఇక్కడ ప్రతి గణిత చర్య జరగుతుంది. ప్రతి పద్ధతి MCP ద్వారా అందుబాటులో ఉండేందుకు `@Tool` తో గుర్తించబడింది:

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
    
    // మరిన్ని క్యాల్క్యులేటర్ ఆపరేషన్లు...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**ప్రధాన లక్షణాలు:**

1. **`@Tool` అనోటేషన్**: ఇది MCPకి ఈ పద్ధతిని బయటి క్లయెంట్లు పిలవగలిగినట్లు తెలియజేస్తుంది
2. **స్పష్ట వివరణలు**: ప్రతి టూల్ కి AI మోడల్స్ ఉపయోగించేటప్పుడు అర్థం చేసుకునే వివరణ ఉంటుంది
3. **సారూప్యమైన రిటర్న్ ఫార్మాట్**: అన్ని కార్యకలాపాలు "5.00 + 3.00 = 8.00" వంటి మానవ-readable స్ట్రింగ్స్ ని రిటర్న్ చేస్తాయి
4. **లోప నిర్వహణ**: పునాదులు 0తో భాగించడం మరియు నెగటివ్ స్క్వేర్ రూట్ మూడు లోప సందేశాలు ఇస్తాయి

**అందుబాటులో ఉన్న కార్యకలాపాలు:**
- `add(a, b)` - రెండు సంఖ్యలు జోడిస్తుంది
- `subtract(a, b)` - రెండవ నంబరు మొదటి నుండి తీసివేస్తుంది
- `multiply(a, b)` - రెండు సంఖ్యలు గుణిస్తుంది
- `divide(a, b)` - మొదటి సంఖ్యని రెండవదాని తో భాగిస్తుంది (శూన్యం తనిఖీతో)
- `power(base, exponent)` - మూలం దశను దత్తాంశంలో పెంచుతుంది
- `squareRoot(number)` - స్క్వేర్ రూట్ లెక్కిస్తుంది (నెగటివ్ తనిఖీతో)
- `modulus(a, b)` - భాగించిన తర్వాత మిగిలిన రిమైండర్ ఇస్తుంది
- `absolute(number)` - ఆసక్తికర విలువను ఇస్తుంది
- `help()` - అన్ని కార్యకలాపాల గురించి సమాచారం ఇస్తుంది

### 3. ప్రత్యక్ష MCP క్లయెంట్

చూడండి [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

ఈ క్లయెంట్ `HttpClientStreamableHttpTransport` ను `/mcp` వద్ద ఉపయోగిస్తుంది, కనెక్షన్ ప్రారంభిస్తుంది,
సర్వర్ ని పింగ్స్ చేస్తుంది, మరియు టూల్-లిస్ట్ పేజినేషన్ తనిఖీ చేస్తుంది. అందులో అన్ని తొమ్మిది ఆశించిన టూల్స్
ఉన్నాయో చూసి పిలుస్తుంది, అంతేకాకుండా `modulus` మరియు `help` తెచ్చిపరుగులే AI మోడల్ లేకుండా.

ప్రస్తుత అభ్యర్థన బిల్డర్ ఈ విధంగా ఉంది:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

ప్రోటోకాల్ లోపాలు క్లయెంట్‌ను తప్పుగా విజయంగా చూపకుండా విఫలమవుతుంది. సహజీవనం విఫలమైతే లేదా టూల్ కాల్ విఫలమైతే
try-with-resources తో MCP క్లయెంట్ మూసివేయబడుతుంది.

### 4. AI-సమర్థించబడిన క్లయెంట్

చూడండి [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
మరియు [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` ప్రస్తుత LangChain4j `ChatModel` API ని అమలు చేస్తుంది.
`StreamableHttpMcpTransport` దీనిని SDK క్లయెంట్ వైపు `/mcp` ఎండ్పాయింట్ తో కలుపుతుంది.
`AiServices` టూల్స్ ని కనుగొంటుంది మరియు టూల్-కాల్స్ / ఫలిత సంభాషణని నిర్వహిస్తుంది.

డిఫాల్ట్ డిప్లాయ్‌మెంట్ **GPT-5.6 Luna**, కాని తర్కం స్పష్టంగా నిలిపివేయబడింది:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

ఈ డిఫాల్ట్ ప్రతి పూర్తి పనికి వర్తిస్తాయి, టూల్ నిర్వహణ తర్వాత కూడా.
క్లయెంట్ రిఫ్రెష్ చేయగల `BearerTokenCredential` ను `DefaultAzureCredential`తో బ్యాకప్ చేస్తుంది
మరియు `https://ai.azure.com/.default` స్కోప్‌ను ఉపయోగిస్తుంది, ఒకసారి API కీ తరహాలో టోకెన్ కాదు.
వనరు URLs మరియు `/openai/v1` తో ముగిసే URLs రెండూ అందుబాటులో ఉంటాయి.

బోట్ పరిమిత సంభాషణ చరిత్రను పాటు నిలుస్తుంది, నిజమైన MCP ఫలితంతో `Tool executed: ...` ను ముద్రిస్తుంది,
మరియు టూల్స్ తప్పించుకుంటే విఫలమవుతుంది. టూల్ లూపులు నాలుగు రౌండ్ ట్రిప్స్ కు పరిమితమవుతాయి.
ధృవీకరణ, మోడల్, MCP మరియు టూల్ లోపాలు వ్యాపిస్తాయి; ఆటోమేటిక్ మోడల్ పునఃప్రయత్నాలు నిలిపివేయబడ్డాయి.
MCP ట్రాన్స్‌పోర్ట్/క్లయెంట్ మరియు అధికారిక OpenAI క్లయెంట్ విజయమో విఫలమో అయినప్పుడల్లా మూసివేయబడతాయి.

## ఉదాహరణలను నడపడం

### పెదవి 1: క్యాల్క్యులేటర్ సర్వర్ ప్రారంభించండి

సర్వర్ కోసం Azure అమరిక అవసరం లేదు. కింది కమాండ్లు ఈ ఉదాహరణ ఫోల్డర్ లోనుండి నడవండి.
ఉదాహరణ మరొక సాంపిల్‌తో ఢంకింపును నివారించడానికి **18081** పోర్ట్ ఉపయోగిస్తుంది; డిఫాల్ట్ 8080 ఇంతే.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP ఎండ్పాయింట్ `http://localhost:18081/mcp`. హెల్త్ మరియు అన్వేషణ సమాచారం ఇందు వద్ద ఉంటుంది
`http://localhost:18081/health` మరియు `http://localhost:18081/info`.
Streamable HTTP పాత SSE-పేరుగల ట్రాన్స్‌పోర్ట్‌ను మార్చింది; `/sse` మరియు `/v1/tools` ఎండ్పాయింట్లు కాదగ్గ.

### పెదవి 2: ప్రత్యక్ష క్లయెంట్ తో పరీక్ష చేయండి

మరో PowerShell టెర్మినల్లలో:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

ఏ ఇన్‌పుట్ అవసరం లేదు. తొమ్మిది టూల్స్ అందరూ పరీక్షించబడతాయి. ఆశించిన గణిత ఫలితాలు
8, 6, 42, 5, 256, 4, 2, మరియు 5.5; తర్వాత సహాయం టెక్స్ట్ కనిపిస్తుంది.

### పెదవి 3: AI క్లయెంట్ తో పరీక్ష చేయండి

ముందస్తు అవసరాల ప్రకారం ధృవీకరణ చేసి తరువాత, ఇదే టెర్మినల్లో AI క్లయెంట్ సెటప్ చేయండి:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

`Tool executed: add` అనే పంక్తి 41.80 తో వచ్చే అవకాశముంది, తరువాత మోడల్ యొక్క సమాధానం.
సింగిల్-ప్రాంప్ట్ మోడ్ ఎక్కడా వేచి ఉండకుండా వదిలేస్తుంది. అసలు నాలుగు ప్రాంప్ట్ డెమో నడపడానికి:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

డెమో `add`, `squareRoot`, `help`, మరియు తరువాత `power` మరియు `divide` శ్రేణి పిలుస్తుంది.
ఆశించిన సంఖ్యాత్మక జవాబులు 41.8, 12, మరియు 64. ఆర్గ్యూమెంట్లను మాకిచ్చకూడదు కూడా ఈ డెమో నడపబడుతుంది.

### పెదవి 4: ఇంటరాక్టివ్ బోట్ నడపండి

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

`Multiply 6 by 7 using the calculator service` అనిపించి, తరువాత `exit` లేదా `quit` టైప్ చేయండి.
`multiply` టూల్ నిజమైన ఫలితంగా 42 వస్తుంది. ఖాళీ లైన్లు విస్మరించబడతాయి; EOF కూడా సెషన్ ముగుస్తుంది.
ఈ ఎంట్రీపాయింట్ కు నాన్‌ఇంటరాక్టివ్ స్మోక్ పరీక్ష కోసం:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

రెండు AI ఎంట్రీ పాయింట్లూ `--prompt "question"`, `--demo`, మరియు `--interactive` అంగీకరిస్తాయి.
తప్పు ఆప్షన్లు కనెక్షన్ ప్రారంభించే ముందు విఫలమవుతాయి. ప్రతి Maven `-D...` ఆర్గ్యుమెంట్ PowerShell కోసం పూర్తిగా కొట్స్ చేయబడాలి.
Bash లో `$env:NAME = "value"` కాకుండా `export NAME=value` వాడాలి.

**క్వోటా:** AI నమూనాలు వరసగా నడపండి. సాధారణ ప్రాంప్ట్ సాధారణంగా రెండు మోడల్ అభ్యర్థనలు అవసరం;
పూర్తి డెమో తొమ్మిది పడతాయి, టూల్ ఫలితాల ఫాలో-అప్‌లు సహా. 10 RPMDeployment భాగం ఉంటే,
తదుపరి AI పరుగుకు ముందే తాజా క్వోటా విండో అనుమతించండి. 429 ఒక దృశ్యంగా విఫలమవుతుంది, ఆటోమేటిక్ పునఃప్రయత్నాలు లేవు; సేవ యొక్క retry-after మార్గదర్శకాన్ని అనుసరించండి.
నిజమైన అభ్యర్థన సంఖ్యలు మోడల్ పై ఆధారపడి ఉంటాయి.
ఆఫ్‌లైన్ పరీక్షలు ఏ క్వోటా వాడకపోవు, Luna లైవ్ అందుబాటు లేదా సమాధాన నాణ్యతను నిర్ధారించవు.

### అమరిక మరియు ఆపు

| సెట్టింగ్ | డిఫాల్ట్ / ప్రవర్తన |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; బేస్ URL, `/mcp` లేకుండా |
| `-Dmcp.server.url=...` | ఉన్నతambled `MCP_SERVER_URL` సర్వర్ కోసం లేక అన్ని క్లయెంట్లకూ మార్పిడి |
| `AZURE_OPENAI_ENDPOINT` | కేవలం AI క్లయెంట్లకు అవసరం; వనరు URL లేదా `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure డిప్లాయ్‌మెంట్ పేరు |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; సానుకూల పూర్ణాంకం |
| తర్కం ప్రయత్నం | ఎప్పుడూ `none`, టూల్-లూప్ ఫాలో-అప్‌లను కూడా కలిగి |

ఓవర్‌రైడ్ చేయబడిన డిప్లాయ్‌మెంట్ `reasoning_effort=none` మరియు `max_completion_tokens` ను మద్దతు ఇవ్వాలి.
క్లయెంట్లు ఆటోమేటిగ `.env` ఫైల్ చదవవు. పరీక్ష ముగిసిన తరువాత `Ctrl+C` తో సర్వర్ ఆపు.
క్లయెంట్లు సాధారణంగా `System.exit` లేకపోవడం లేదా ఆపు నిద్రలు లేకుండా తిరిగి వస్తాయి.

## ఆఫ్‌లైన్ పరీక్షలు

```powershell
mvn -B -ntp clean verify
```

పాటు పరీక్షలు Azure కు సంబంధించి ఆఫ్‌లైన్లో ఉంటాయి: ప్రోటోకాల్ సూట్ ఒక Spring సర్వర్ ని ప్రారంభిస్తుంది మరియు
ఓ OpenAI అనుకూల స్టబ్ ని రాండమ్ లూప్‌బ్యాక్ పోర్ట్స్ లో, తర్వాత వాటిని మూసేస్తుంది. Maven ఇంకా డిపెండెన్సీలు డౌన్లోడ్ కావాల్సి ఉండవచ్చు.
ఎటువంటి క్రెడెన్షియల్స్, లైవ్ డిప్లాయ్‌మెంట్ లేదా MCP సర్వర్ ప్ర_EXISTING_ ఉపయోగించబడటం లేదు.

- క్యాల్క్యులేటర్ యూనిట్ పరీక్షలు అన్ని గణిత కార్యకలాపాలు, దశాంశ ఫలితాలు, సహాయం మరియు డొమైన్ లోపాలను కవర్ చేస్తాయి.
- MCP పరీక్షలు ప్రారంభం, అన్వేషణ, అన్ని తొమ్మిది టూల్ కాల్స్, టూల్ విఫలములు, మరియు హెల్త్/ఇన్‍ఫో కవర్ చేస్తాయి.
- AI ప్రోటోకాల్ పరీక్షలు పూర్తి డెమో మరియు ఇంటరాక్టివ్ బోట్ రెగ్యులర్ క్యాల్క్యులేటర్ పై నడిపిస్తాయి,
  టూల్ ఫలితాలు తదుపరి పూర్తి లోకి పంపబడుతున్నాయని ధృవీకరిస్తాయి, మరియు ప్రతి HTTP బాడీని Luna,
  `reasoning_effort: "none"`, మరియు `max_completion_tokens` అందచేస్తున్నాయి, పాత విద్యుత్ `max_tokens` లేదని చూస్తాయి.
- అమరిక / ఇన్‌పుట్ పరీక్షలు డిప్లాయ్‌మెంట్ మరియు ఎండ్పాయింట్ ఓవర్‌రైడ్లను, ఖాళీ సహా, EOF, exit/quit,
  సింగిల్ ప్రాంప్ట్ మోడ్, చెల్లని ఆప్షన్లు, మరియు లోప వ్యాప్తిని కవర్ చేస్తాయి. క్వోటా పరీక్షలు 429 తిరిగివచ్చే లేదు అని నిరూపిస్తాయి.

## ఇది ఎలా కలిసి పని చేస్తుంది

మీరు AIని "5 + 3 ఎంత?" అని అడిగినప్పుడు పూర్తి ప్రవాహం ఇలానే ఉంటుంది:

1. **మీరు** సహజ భాషలో AI కు అడుగుతారు
2. **AI** మీ అభ్యర్థనను విశ్లేషించి మీరు జోడింపు కోరుతున్నారని గ్రహిస్తుంది
3. **AI** MCP సర్వర్ ను పిలుస్తుంది: `add(5.0, 3.0)`
4. **క్యాల్క్యులేటర్ సేవ** లెక్కలు చేస్తుంది: `5.0 + 3.0 = 8.0`
5. **క్యాల్క్యులేటర్ సేవ** ఫలితాన్ని ఇస్తుంది: `"5.00 + 3.00 = 8.00"`
6. **AI** ఫలితాన్ని అందుకుని సహజ జవాబును రూపొందిస్తుంది
7. **మీకు** వస్తుంది: "5 మరియు 3 యొక్క మొత్తం 8"

## తదుపరి దశలు

మరిన్ని ఉదాహరణల కోసం [అధ్యాయం 04: ప్రాయోగిక నమూనాలు](../README.md) చూడండి

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్వీకరణ**:
ఈ పత్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలు తప్పులు లేదా అసమగ్రతలను కలిగి ఉండవచ్చు. దాని స్వదేశ భాషలో ఉన్న అసలు పత్రాన్ని అధికారం కలిగిన మూలంగా పరిగణించాలి. కీలకమైన సమాచారం కోసం, ప్రొఫెషనల్ మానవ అనువాదాన్ని సిఫారసు చేస్తాము. ఈ అనువాదం ఉపయోగం వల్ల కలిగే ఏవైనా అపార్థాలు లేదా తప్పుదారులు కోసం మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->