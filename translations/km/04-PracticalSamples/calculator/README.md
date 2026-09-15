# មគ្គុទេសក៍គណនេយ្យ MCP សម្រាប់អ្នកចាប់ផ្តើម

## សារំពីមាតិកា

- [អ្វីដែលអ្នកនឹងរៀន](#អ្វីដែលអ្នកនឹងរៀន)
- [លក្ខខ័ណ្ឌមុន](#លក្ខខ័ណ្ឌមុន)
- [កំណែការស្ទង់ពិសោធន៍](#កំណែការស្ទង់ពិសោធន៍)
- [ការស្វែងយល់អំពីរចនាសម្ព័ន្ធគម្រោង](#ការយល់ដឹងអំពីរចនាសម្ព័ន្ធគម្រោង)
- [បញ្ជាក់អំពីប្លង់គ្រឿងផ្សំនៃគម្រោង](#ប្លង់គ្រឿងផ្សំនៃគម្រោង)
  - [1. កម្មវិធីស្នូល](#1-កម្មវិធីស្នូល)
  - [2. សេវាកម្មគណនេយ្យ](#2-សេវាកម្មគណនេយ្យ)
  - [3. អតិថិជន MCP ដោយផ្ទាល់](#3-អតិថិជន-mcp-ដោយផ្ទាល់)
  - [4. អតិថិជនបច្ចេកវិទ្យា AI](#4-អតិថិជនមានបច្ចេកវិទ្យា-ai-អភិវឌ្ឍ)
- [រត់ឧទាហរណ៍](#រត់ឧទាហរណ៍)
- [ការសាកល្បងក្រៅបណ្តាញ](#ការសាកល្បងមិនអនឡាញ)
- [របៀបដែលវាដំណើរការជាមួយគ្នា](#វិធីដំណើរការមួយទាំងមូល)
- [ជំហានបន្ទាប់](#ជំហានបន្ទាប់)

## អ្វីដែលអ្នកនឹងរៀន

មគ្គុទេសក៍នេះអធិប្បាយពីរបៀបបង្កើតសេវាកម្មគណនេយ្យដោយប្រើ Model Context Protocol (MCP)។ អ្នកនឹងយល់ដឹងអំពីៈ

- របៀបបង្កើតសេវាកម្មដែល AI អាចប្រើប្រាស់ជាមធ្យោបាយ
- របៀបរៀបចំការទំនាក់ទំនងផ្ទាល់ជាមួយសេវាកម្ម MCP
- របៀបដែលម៉ូដែល AI អាចជ្រើសរើសឧបករណ៍ដែលត្រូវប្រើដោយស្វ័យប្រវត្តិ
- ផ្ទាំងខុសគ្នារវាងការហៅប្រព័ន្ធផ្ទាល់និងបាត់បង់ជំនួយដោយ AI

## លក្ខខ័ណ្ឌមុន

មុនចាប់ផ្តើម, សូមធ្វើឲ្យប្រាកដថាអ្នកមាន:
- Java 21 ឬខ្ពស់ជាងត្រូវបានដំឡើង
- Maven សម្រាប់ការគ្រប់គ្រងឧបករណ៍ផ្គួបផ្គង់
- ការយល់ដឹងមូលដ្ឋានអំពី Java និង Spring Boot

អតិថិជន AI តែប៉ុណ្ណោះត្រូវការដំឡើង Azure OpenAI និង `DefaultAzureCredential` ដែលបានអ៊ិនធើម៉ិក,
ដូចជា ការចុះឈ្មោះ Azure CLI ដែលមានរួចហើយនៅក្នុងកុំព្យូទ័រមូលដ្ឋាន ឬអត្តសញ្ញាណគ្រប់គ្រងក្នុង Azure។ អត្តសញ្ញាណគួរត្រូវមាន
តួនាទី Cognitive Services OpenAI User នៅលើធនធាន។ មើល [ជំពូកទី ២](../../02-SetupDevEnvironment/getting-started-azure-openai.md)។
មុឺនម៉ាស៊ីនជួសជុល, អតិថិជន SDK ផ្ទាល់ និងការសាកល្បងទាំងអស់មិនត្រូវការគណនី Azure ឬការចូលប្រើម៉ូដែលឡើយ។

## កំណែការស្ទង់ពិសោធន៍

ការពិនិត្យគំនិតដែលអនុញ្ញាតបាននៅថ្ងៃទី ១៤ ខែកញ្ញា ឆ្នាំ ២០២៦៖

| ឧបករណ៍ផ្គួបផ្គង់ | កំណែ |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (គ្រប់គ្រងដោយ Spring AI) | 2.0.0 |
| LangChain4j / ស្នូល | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| កម្មវិធីផ្សាយ OpenAI ផ្លូវការរបស់ LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| សម្ភារៈអត្តសញ្ញាណ Azure | 1.18.6 |
| JUnit Jupiter (គ្រប់គ្រងដោយ Boot) | 6.0.3 |

MCP និងកម្មវិធីផ្សាយ OpenAI ផ្លូវការមានជាកំណែ beta ដែលបានផ្សាយនៅ Maven Central មិនមែន snapshots ទេ។
កំណែរបស់ពួកវាផ្សេងពី LangChain4j ស្នូល។ មិនចាំបាច់មាន repository snapshot ឬ milestone ទេ។
ឧបករណ៍ផ្គួបផ្គង់សំរាប់អតិថិជនមាន scope សាកល្បងព្រោះឧទាហរណ៍ដែលអាចដំណើរការបានស្ថិតនៅក្រោម `src/test/java`។

## ការយល់ដឹងអំពីរចនាសម្ព័ន្ធគម្រោង

គម្រោងគណនេយ្យមានឯកសារសំខាន់ជាច្រើន៖

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

## ប្លង់គ្រឿងផ្សំនៃគម្រោង

### 1. កម្មវិធីស្នូល

**ឯកសារ៖** `McpServerApplication.java`

នេះជាចំណុចចូលរបស់សេវាកម្មគណនេយ្យរបស់យើង។ វាជាកម្មវិធី Spring Boot ស្តង់ដារដែលមានការបន្ថែមពិសេសមួយ៖

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

**អ្វីដែលវាធ្វើ៖**
- ចាប់ផ្តើមម៉ាស៊ីនបម្រើ Spring Boot នៅកំពង់ផែ 8080
- បង្កើត `ToolCallbackProvider` ដែលធ្វើឲ្យវិធីសាស្រ្តគណនេយ្យរបស់យើងអាចប្រើជាឧបករណ៍ MCP បាន
- ស្លាក `@Bean` ប្រាប់ Spring អោយគ្រប់គ្រងវាជាផ្នែកមួយដែលបណ្តាញផ្សេងអាចប្រើបាន

### 2. សេវាកម្មគណនេយ្យ

**ឯកសារ៖** `CalculatorService.java`

នេះជាទីដែលវិធីសាស្រ្តគ្រប់យ៉ាងធ្វើការគណនា។ មេធុលីទាំងអស់ត្រូវបានសម្គាល់ជាមួយ `@Tool` ដើម្បីអោយវាអាចប្រើតាម MCP បាន៖

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
    
    // ប្រតិបត្តិការ​គណនាពីរប​ណ​ខាងលើ…
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**លក្ខណៈពិសេសសំខាន់៖**

1. **ស្លាក `@Tool`**៖ នេះប្រាប់ MCP ថាវិធីសាស្រ្តនេះអាចហៅដោយអតិថិជនខាងក្រៅបាន
2. **ការពិពណ៌នាឈ្មោះច្បាស់លាស់**៖ ឧបករណ៍នីមួយមានការពណ៌នាដើម្បីជួយម៉ូដែល AI យល់ពេលណាគួរប្រើវា
3. **ទ្រង់ទ្រាយត្រឡប់អាចអានបានដោយមនុស្ស**៖ ប្រតិបត្តិការទាំងអស់ត្រឡប់ខ្សែអក្សរដូចជា "5.00 + 3.00 = 8.00"
4. **ការគ្រប់គ្រងកំហុស**៖ ការចែកដោយសូន្យ និងឫសការ៉េអវិជ្ជមានបង្ហាញសារពីកំហុស

**ប្រតិបត្តិការដែលមាន៖**
- `add(a, b)` - បន្ថែមចំនួនពីរជាមួយគ្នា
- `subtract(a, b)` - ដកចេញពីលេខទីពីរ
- `multiply(a, b)` - គុណចំនួនពីរជាមួយគ្នា
- `divide(a, b)` - ចែកលេខទីមួយដោយលេខទីពីរ (ពិនិត្យការចែកសូន្យ)
- `power(base, exponent)` - កើនកម្លាំងពីគោលដៅទៅកម្លាំងកំណត់
- `squareRoot(number)` - គណនាឫសការ៉េ (ពិនិត្យអវិជ្ជមាន)
- `modulus(a, b)` - ត្រឡប់ باقيសំណល់នៃការចែក
- `absolute(number)` - ត្រឡប់តម្លៃផ្ទៀងផ្ទាត់
- `help()` - ត្រឡប់ព័ត៌មានអំពីប្រតិបត្តិការទាំងអស់

### 3. អតិថិជន MCP ដោយផ្ទាល់

មើល [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)។

អតិថិជននេះប្រើ `HttpClientStreamableHttpTransport` នៅ `/mcp`, ចាប់ផ្តើមការតភ្ជាប់,
ពិនិត្យម៉ាស៊ីនបម្រើ, និងតាមដាន pagination បញ្ជីឧបករណ៍។ វាពិនិត្យថាឧបករណ៍ទាំងប្រាំបួនគឺមានរួចហើយ
ហើយហៅមួយចំនួនរួមទាំង `modulus` និង `help` ទៅដោយមិនប្រើម៉ូដែល AI។

មុខងារ request builder បច្ចុប្បន្នមានរូបរាងដូចខាងក្រោម៖

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

កំហុសពហុប្រព័ន្ធបណ្តាលឲ្យអតិថិជនបរាជ័យមិនមែនបោះពុម្ពជោគជ័យមិនត្រឹមត្រូវ។ អតិថិជន MCP
ត្រូវបានបិទដោយ try-with-resources រួមទាំងពេលរកឧបករណ៍ ឬហៅឧបករណ៍បរាជ័យ។

### 4. អតិថិជនមានបច្ចេកវិទ្យា AI អភិវឌ្ឍ

មើល [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
និង [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)។

`OpenAiOfficialChatModel` អនុវត្ត API ChatModel នៃ LangChain4j បច្ចុប្បន្ន។
`StreamableHttpMcpTransport` តភ្ជាប់វាទៅកាន់ចំណុច `/mcp` ដូចអតិថិជន SDK។
`AiServices` ស្វែងរកឧបករណ៍ និងគ្រប់គ្រងការសន្ទនា ហៅឧបករណ៍/លទ្ធផល។

ការផ្ទុកលំនាំដើមគឺ **GPT-5.6 Luna**, ជាមួយការបិទការសន្មត់យ៉ាងច្បាស់៖

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

លំនាំដើមទាំងនេះអាចអនុវត្តលើការសម្រេចចិត្តទាំងអស់ រួមទាំងការតាមដានបន្ទាប់ពីការប្រើឧបករណ៍។
អតិថិជនប្រើ `BearerTokenCredential` ដែលអាចបញ្ចូលបន្ថែមបាន និង `DefaultAzureCredential`
និងជួរដែនកំណត់ `https://ai.azure.com/.default`, មិនមែនជាទីបម្រើ token មួយដងដែលផ្តល់ជាម្ចាស់ API។
URL ធនធាន និង URL ដែលបញ្ចប់នៅ `/openai/v1` ទាំងពីរត្រូវបានទទួលស្គាល់។

បុត្រសត្វរក្សាប្រវត្តិសន្ទនា ដែលបោះពុម្ព `Tool executed: ...` ជាមួយលទ្ធផល MCP ពិតប្រាកដ
ហើយបរាជ័យបើការឆ្លើយតបរំលងឧបករណ៍។ ការបញ្ជូនឧបករណ៍មានកំណត់ប្រាំពីរជុំ។
កំហុសផ្ទាល់ខ្លួន, ម៉ូដែល, MCP និងឧបករណ៍ត្រូវបានបន្ត; ការឆ្លងវគ្គម៉ូដែលដោយស្វ័យប្រវត្តិបានបិទ។
ទាំងការដឹកជញ្ជូន/អតិថិជន MCP និងអតិថិជន OpenAI ផ្លូវការត្រូវបានបិទនៅពេលជោគជ័យឬបរាជ័យ។

## រត់ឧទាហរណ៍

### ជំហាន ១៖ ចាប់ផ្តើមម៉ាស៊ីនមេគណនាគរបស់ Calculator

មិនចាំបាច់តំរូវការកំណត់ Azure សម្រាប់ម៉ាស៊ីនមេទេ។ ពាក្យបញ្ជាខាងក្រោមដំណើរការពីថតរបស់ឧទាហរណ៍នេះ។
ឧទាហរណ៍នេះប្រើច្រក **18081** ដើម្បីជៀសវាងការប្រកួតប្រជែងជាមួយឧទាហរណ៍ផ្សេងទៀត។ ច្រកលំនាំដើមនៅតែ 8080។

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

ចំណុចចុង MCP គឺ `http://localhost:18081/mcp`។ ព័ត៌មានសុខភាព និងការរកឃើញមាននៅ
`http://localhost:18081/health` និង `http://localhost:18081/info`។
HTTP ដែលអាចច្រកបានជំនួសដល់ការដឹកជញ្ជូន SSE ចាស់; `/sse` និង `/v1/tools` មិនមែនជាចំណុចចុងទេ។

### ជំហាន ២៖ សាកល្បងជាមួយ Client ត្រង់

នៅក្នុង Terminal PowerShell ផ្សេងទៀត៖

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

មិនចាំបាច់បញ្ចូលព័ត៌មានទេ។ ឧបករណ៍ទាំងប្រាំបួនត្រូវបានប្រើ។ លទ្ធផលគណិតវិទ្យារង់ចាំរួមមាន
8, 6, 42, 5, 256, 4, 2, និង 5.5 បន្ទាប់ពី​អត្ថបទ​ជំនួយ។

### ជំហាន ៣៖ សាកល្បងជាមួយ Client AI

បន្ទាប់ពី Authenticate ដូចបានរៀបរាប់ក្នុងលក្ខណៈទាំងមុនៗ តំរូវ Client AI ក្នុងទីបំផុតដដែល៖

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

រំពឹងថាលេខមួយ`Tool executed: add` ជាមួយ `41.80` បន្ទាប់មកពាក្យឆ្លើយពីម៉ូដែល។
លំនាំ single-prompt ចេញដោយមិនរង់ចាំបញ្ចូល។ ដើម្បីដំណើរការឧទាហរណ៍បួន prompt ដើម៖

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

ឧទាហរណ៍ហៅ `add`, `squareRoot`, `help` និង​សកម្មភាព​ច្រក `power` បំផុត `divide` ។
លទ្ធផលលេខ​រំពឹងបានជា 41.8, 12, និង 64។ ការលែងបញ្ចូល arguments ក៏ដំណើរការឧទាហរណ៍នេះ។

### ជំហាន ៤៖ ប្រតិបត្តិ Bot អន្តរកម្ម

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

បញ្ចូល `Multiply 6 by 7 using the calculator service` ហើយបន្ទាប់មក `exit` ឬ `quit`។
រំពឹងលទ្ធផលយ៉ាងត្រឹមត្រូវ `multiply` គឺ 42។ បន្ទាត់ទទេត្រូវចោល; EOF ក៏បញ្ចប់សម័យ។
សម្រាប់ការសាកល្បងមិនអន្តរកម្មនៃចំណុចចូលនេះ៖

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

ចំណុចចូល AI ទាំងពីរទទួល `--prompt "question"`, `--demo`, និង `--interactive`។
ជម្រើសមិនត្រឹមត្រូវនាំឲ្យបរាជ័យមុនការបើកការតភ្ជាប់។ ពាក្យបញ្ជា Maven រាល់ `-D...` ត្រូវបានគេដាក់សញ្ញាដាច់
សម្រាប់ PowerShell។ នៅ Bash ប្រើ `export NAME=value` ជំនួស `$env:NAME = "value"`។

**Quota:** បង្រៀនគំរូ AI ជាលំដាប់លំដោយ។ prompt ងាយស្រួលត្រូវការសំណើម៉ូដែលពីរដង;
ឧទាហរណ៍ពេញលេញត្រូវការចំនួនប្រាំឯង រួមទាំងការតាមដានលទ្ធផលប្រើឧបករណ៍។ ជាមួយការ​បង្ហោះ RPM 10 ចែករំលែក
អនុញ្ញាតឲ្យមានវិនាទី quota ថ្មីមុនការរត់ AI បន្ទាប់។ 429 បរាជ័យបង្ហាញដោយមិនមាន
ការព្យាយាមឡើងវិញដោយស្វ័យប្រវត្តិ; តាម​ដានការណែនាំ retry-after នៃសេវា។ ចំនួនសំណើពិតប្រាកដអាស្រ័យលើម៉ូដែល។
ការសាកល្បងមិនអនឡាញមិនប្រើ quota ហើយមិនបង្ហាញភាពមានប្រសិទ្ធភាព Luna ឬគុណភាពចម្លើយ។

### ការកំណត់រចនាសម្ព័ន្ធ និង បិទបញ្ចប់

| ការកំណត់ | លំនាំដើម / ព្រលឹង |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; លំនាំដើម URL មូលដ្ឋាន ដោយគ្មាន `/mcp` |
| `-Dmcp.server.url=...` | ជំនួស `MCP_SERVER_URL` សម្រាប់អតិថិជនទាំងអស់ |
| `AZURE_OPENAI_ENDPOINT` | ត្រូវការសម្រាប់អតិថិជន AI តែប៉ុណ្ណោះ; URL ឫ URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; ឈ្មោះការចែកចាយ Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; លេខគត់វិជ្ជមាន |
| ការខិតខំយល់ដឹង | នៅតែ `none` រួមបញ្ចូលតាមដានលទ្ធផលឧបករណ៍ |

ការបង្ហោះដែលបានជំនួសត្រូវគាំទ្រ `reasoning_effort=none` និង `max_completion_tokens`។
អតិថិជនមិនអានឯកសារ `.env` ដោយស្វ័យប្រវត្តិទេ។ បិទម៉ាស៊ីនមេដោយចុច `Ctrl+C` បន្ទាប់ពីសាកល្បង។
អតិថិជនត្រឡប់មកធម្មតា ដោយគ្មាន `System.exit` ឬការដេកបិទ។

## ការសាកល្បងមិនអនឡាញ

```powershell
mvn -B -ntp clean verify
```

ការសាកល្បងទាំងអស់មិនអនឡាញសម្រាប់ Azure៖ protocol suite ចាប់ផ្តើមម៉ាស៊ីនមេ Spring និង
stub យ៉ាងស្របគ្នា OpenAI លើច្រក loopback ផ្សេងៗ ប្រើក្រោយ ហើយបិទវា។ Maven នៅតែអាចត្រូវតែ
ទាញយកdependencies។ មិនប្រើជម្រើស ឬការចេញផ្សាយ ជាចាំបាច់ MCP។

- ការសាកល្បងឯកតាគណនាគ្រប់គ្រងលើប្រតិបត្តិការគណិតវិទ្យាទាំងអស់ លទ្ធផលទសភាគ ជំនួយ និងកំហុស domain។
- ការសាកល្បង MCP ក្រចាំលើការចាប់ផ្តើម ការរកឃើញ ចេញសំនួរឧបករណ៍ទាំងប្រាំបួន ការបរាជ័យ ឯកសារសុខភាព និងព័ត៌មាន។
- ការសាកល្បងProtocol AI ជំនួញការបង្ហាញពេញលេញ និង Bot អន្តរកម្មប្រឆាំងគណនាគរពិតប្រាកដ,
  ការត្រួតពិនិត្យលទ្ធផលឧបករណ៍ជាការតបតទៅការបញ្ចប់បន្ទាប់ និងពិនិត្យរាល់ HTTP body សម្រាប់ Luna,
  `reasoning_effort: "none"`, និង `max_completion_tokens` ដោយគ្មាន `max_tokens` បេតិកភណ្ឌ។
- ការកំណត់/បញ្ចូលព័ត៌មានសាកល្បងលើការបង្ហោះ និងការស្តារចំណុចចុង បន្ទាត់ទទេ EOF, ចេញ/បញ្ចប់,
  លំនាំ single-prompt ជម្រើសមិនត្រឹមត្រូវ និង បញ្ហាផ្សេងៗបញ្ជូន។ ការសាកល្បង quota បង្ហាញ 429 មិនបានសាកល្បងឡើងវិញ។

## វិធីដំណើរការមួយទាំងមូល

នេះគឺជាសកម្មភាពពេញលេញនៅពេលអ្នកសួរថា AI "តើ 5 + 3 ជាអ្វី?":

1. **អ្នក** សួរ AI ជាភាសារដ្ឋាភិបាល
2. **AI** វិភាគសំណើរបស់អ្នក ហើយយល់ថាអ្នកចង់បន្ថែម
3. **AI** ហៅម៉ាស៊ីនមេ MCP: `add(5.0, 3.0)`
4. **Calculator Service** បំពេញ: `5.0 + 3.0 = 8.0`
5. **Calculator Service** ត្រលប់: `"5.00 + 3.00 = 8.00"`
6. **AI** ទទួលលទ្ធផល ហើយរៀបចំចម្លើយធម្មជាតិមួយ
7. **អ្នក** ទទួល: "ផលបូកនៃ 5 និង 3 គឺ 8"

## ជំហានបន្ទាប់

សម្រាប់ឧទាហរណ៍បន្ថែម, មើល [ជំពូក 04: ឧទាហរណ៍ប្រើប្រាស់](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->