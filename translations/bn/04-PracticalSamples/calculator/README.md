# নবাগতদের জন্য MCP ক্যালকুলেটর টিউটোরিয়াল

## বিষয়বস্তু সূচি

- [আপনি কী শিখবেন](#আপনি-কী-শিখবেন)
- [প্রয়োজনীয়তাসমূহ](#প্রয়োজনীয়তাসমূহ)
- [ডিপেন্ডেন্সি সংস্করণসমূহ](#ডিপেন্ডেন্সি-সংস্করণসমূহ)
- [প্রকল্প কাঠামো বোঝা](#প্রকল্প-কাঠামো-বোঝা)
- [কোর উপাদান ব্যাখ্যা](#কোর-উপাদান-ব্যাখ্যা)
  - [১. প্রধান অ্যাপ্লিকেশন](#১-প্রধান-অ্যাপ্লিকেশন)
  - [২. ক্যালকুলেটর সার্ভিস](#২-ক্যালকুলেটর-সার্ভিস)
  - [৩. সরাসরি MCP ক্লায়েন্ট](#৩-সরাসরি-mcp-ক্লায়েন্ট)
  - [৪. AI সক্ষম ক্লায়েন্ট](#৪-ai-সক্ষম-ক্লায়েন্ট)
- [উদাহরণগুলো চালানো](#উদাহরণগুলি-চালানো)
- [অফলাইন পরীক্ষাসমূহ](#অফলাইন-পরীক্ষাসমূহ)
- [সবকিছু মিলে কিভাবে কাজ করে](#সবকিছু-মিলে-কিভাবে-কাজ-করে)
- [পরবর্তী ধাপসমূহ](#পরবর্তী-ধাপসমূহ)

## আপনি কী শিখবেন

এই টিউটোরিয়ালে MCP (মডেল কনটেক্সট প্রোটোকল) ব্যবহার করে ক্যালকুলেটর সার্ভিস তৈরির পদ্ধতি ব্যাখ্যা করা হয়েছে। আপনি বুঝতে পারবেন:

- AI একটি টুল হিসেবে ব্যবহার করার জন্য কিভাবে সার্ভিস তৈরি করবেন
- MCP সার্ভিসের সাথে সরাসরি সংযোগ স্থাপন কিভাবে করবেন
- AI মডেলগুলি স্বয়ংক্রিয়ভাবে কোন টুলগুলি ব্যবহার করবে তা কিভাবে বেছে নেয়
- সরাসরি প্রোটোকল কল এবং AI সহায়িত ইন্টারঅ্যাকশনের মধ্যে পার্থক্য

## প্রয়োজনীয়তাসমূহ

শুরু করার আগে নিশ্চিত করুন যে আপনার কাছে রয়েছে:
- জাভা ২১ বা তার উপরের সংস্করণ ইনস্টল করা
- ডিপেন্ডেন্সি ব্যবস্থাপনার জন্য মেভেন
- জাভা এবং স্প্রিং বুটের মৌলিক জ্ঞান

শুধুমাত্র AI ক্লায়েন্টদের জন্য একটি আজুর ওপেনএআই ডিপ্লয়মেন্ট ও প্রমাণীকৃত `DefaultAzureCredential` প্রয়োজন,
যেমন স্থানীয়ভাবে বিদ্যমান আজুর CLI সাইন-ইন বা আজুরে পরিচালিত আইডেন্টিটি। আইডেন্টিটিটির জন্য কোগনিটিভ সার্ভিসেস ওপেনএআই ইউজার রোল থাকা আবশ্যক।
দেখুন [অধ্যায় ২](../../02-SetupDevEnvironment/getting-started-azure-openai.md)।
সার্ভার, সরাসরি SDK ক্লায়েন্ট এবং সব স্বয়ংক্রিয় পরীক্ষাগুলোর জন্য কোন আজুর অ্যাকাউন্ট বা মডেল অ্যাক্সেসের প্রয়োজন নেই।

## ডিপেন্ডেন্সি সংস্করণসমূহ

রিলিজ ডিপেন্ডেন্সি যাচাই করা হয়েছে ২০২৬-০৯-১৪ তারিখে:

| ডিপেন্ডেন্সি | সংস্করণ |
| --- | --- |
| স্প্রিং বুট | ৪.১.১ |
| স্প্রিং AI | ২.০.১ |
| MCP জাভা SDK (স্প্রিং AI-পরিচালিত) | ২.০.০ |
| LangChain4j / মুল | ১.২০.০ |
| LangChain4j MCP | ১.২০.০-বেটা৩০ |
| LangChain4j অফিসিয়াল OpenAI অ্যাডাপ্টার | ১.২০.০-বেটা৩০ |
| OpenAI জাভা SDK | ৪.৬৩.১ |
| আজুর আইডেন্টিটি | ১.১৮.৬ |
| JUnit জুপিটার (বুট-পরিচালিত) | ৬.০.৩ |

MCP এবং অফিসিয়াল OpenAI অ্যাডাপ্টারগুলি মেভেন সেন্ট্রালে প্রকাশিত বেটা রিলিজ, স্ন্যাপশট নয়।
তাদের সংস্করণ LangChain4j মুল থেকে আলাদা। কোন স্ন্যাপশট বা মাইলস্টোন রিপোজিটরি প্রয়োজন নেই।
শুধুমাত্র ক্লায়েন্টের ডিপেন্ডেন্সিগুলো টেস্ট স্কোপে থাকে কারণ রানযোগ্য উদাহরণগুলো `src/test/java` এর মধ্যে থাকে।

## প্রকল্প কাঠামো বোঝা

ক্যালকুলেটর প্রকল্পে বেশ কিছু গুরুত্বপূর্ণ ফাইল আছে:

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

## কোর উপাদান ব্যাখ্যা

### ১. প্রধান অ্যাপ্লিকেশন

**ফাইল:** `McpServerApplication.java`

এটি আমাদের ক্যালকুলেটর সার্ভিসের এন্ট্রি পয়েন্ট। এটি একটি স্ট্যান্ডার্ড স্প্রিং বুট অ্যাপ্লিকেশন একটি বিশেষ সংযোজনসহ:

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

**এর কাজ:**
- ৮০৮০ পোর্টে একটি স্প্রিং বুট ওয়েব সার্ভার শুরু করে
- একটি `ToolCallbackProvider` তৈরি করে যা আমাদের ক্যালকুলেটর মেথডগুলো MCP টুল হিসেবে উপলব্ধ করে
- `@Bean` অ্যানোটেশন স্প্রিংকে বলেছে এটি একটি কম্পোনেন্ট হিসেবে পরিচালনা করতে যা অন্যান্য অংশ ব্যবহার করতে পারে

### ২. ক্যালকুলেটর সার্ভিস

**ফাইল:** `CalculatorService.java`

এখানেই সব গণনা হয়। প্রতিটি মেথড `@Tool` দিয়ে চিহ্নিত যা MCP মাধ্যমে উপলব্ধ করায়:

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
    
    // আরো ক্যালকুলেটর অপারেশন...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**মূল ফিচার:**

১. **`@Tool` অ্যানোটেশন**: এটা MCP কে বলে যে এই মেথডটি বাইরের ক্লায়েন্ট দ্বারা কল করা যাবে
২. **পরিষ্কার বিবরণ**: প্রতিটি টুলের একটি বর্ণনা থাকে যা AI মডেলকে কখন তা ব্যবহার করতে হয় বুঝতে সাহায্য করে
৩. **সংগত রিটার্ন ফরম্যাট**: সব অপারেশন মানুষ পড়তে পারে এমন স্ট্রিং আকারে রিটার্ন করে যেমন "5.00 + 3.00 = 8.00"
৪. **ত্রুটি হ্যান্ডলিং**: শূন্য দিয়ে ভাগ এবং নেতিবাচক বর্গমূল ত্রুটি বার্তা দেয়

**উপলব্ধ অপারেশনসমূহ:**
- `add(a, b)` - দুইটি সংখ্যা যোগ করে
- `subtract(a, b)` - দ্বিতীয় সংখ্যাটি প্রথম থেকে বিয়োগ করে
- `multiply(a, b)` - দুইটি সংখ্যা গুণ করে
- `divide(a, b)` - প্রথম সংখ্যাটি দ্বিতীয় দ্বারা ভাগ করে (শূন্য পরীক্ষা সহ)
- `power(base, exponent)` - বেসকে উর্ধ্বগামী ঘাত নির্ণয় করে
- `squareRoot(number)` - বর্গমূল নির্ণয় করে (নেতিবাচক পরীক্ষা সহ)
- `modulus(a, b)` - ভাগশেষ প্রদান করে
- `absolute(number)` - মৌলিক মান প্রদান করে
- `help()` - সব অপারেশন সম্পর্কে তথ্য দেয়

### ৩. সরাসরি MCP ক্লায়েন্ট

দেখুন [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)।

এই ক্লায়েন্ট `HttpClientStreamableHttpTransport` ব্যবহার করে `/mcp` এ সংযোগ শুরু করে,
সার্ভারে পিং দেয় এবং টুল তালিকার পেজিনেশন অনুসরণ করে। এটি নিশ্চিত করে যে প্রত্যাশিত নয়টি টুল
আছে এবং প্রত্যেকটি কল চালায়, যার মধ্যে `modulus` ও `help` রয়েছে, AI মডেলের সাহায্য ছাড়াই।

বর্তমান রিকোয়েস্ট বিল্ডারটি দেখতে এ রকম:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

প্রোটোকল ত্রুটি ক্লায়েন্টকে ব্যর্থ করে দেয়, ভুল সফলতার বার্তা না দেখিয়ে। MCP ক্লায়েন্ট try-with-resources দিয়ে বন্ধ হয়,
এমনকি যেকোনো ডিসকভারি বা টুল কল ব্যর্থ হোক।

### ৪. AI সক্ষম ক্লায়েন্ট

দেখুন [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
এবং [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)।

`OpenAiOfficialChatModel` বর্তমান LangChain4j `ChatModel` API বাস্তবায়ন করে।
`StreamableHttpMcpTransport` এটি একই `/mcp` এন্ডপয়েন্টে SDK ক্লায়েন্টের সাথে সংযুক্ত করে।
`AiServices` টুলগুলি আবিষ্কার করে ও টুল কল/রেজাল্ট কথোপকথন পরিচালনা করে।

ডিফল্ট ডিপ্লয়মেন্ট হল **GPT-5.6 Luna**, যেখানে যুক্তি স্পষ্টতই নিষ্ক্রিয় রাখা হয়েছে:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

এই ডিফল্টগুলো প্রতিটি কমপ্লিশনে প্রযোজ্য, টুল এক্সিকিউশনের পর ফলো-আপ সহ।
ক্লায়েন্ট একটি রিফ্রেশযোগ্য `BearerTokenCredential` ব্যবহার করে, যা `DefaultAzureCredential` দ্বারা ব্যাকড এবং
`https://ai.azure.com/.default` স্কোপ ব্যবহার করে, একটি একবারের টোকেন API কী না।
রিসোর্স ইউআরএল এবং যেগুলো `/openai/v1` এ শেষ হয় তাও গ্রহণ করা হয়।

বট সসীমিত কথোপকথন ইতিহাস রাখে, প্রকৃত MCP ফলাফল সহ `Tool executed: ...` মুদ্রণ করে,
এবং যদি কোন রেসপন্স টুল বাদ দেয় তাহলে ব্যর্থ হয়। টুল লুপ সীমাবদ্ধ চারটি রাউন্ড ট্রিপে।
প্রমাণীকরণ, মডেল, MCP এবং টুল ত্রুটি ছড়ায়; স্বয়ংক্রিয় মডেল পুনরায় চেষ্টা বন্ধ।
MCP ট্রান্সপোর্ট/ক্লায়েন্ট ও অফিসিয়াল OpenAI ক্লায়েন্ট সফলতা বা ব্যর্থতায় বন্ধ হয়।

## উদাহরণগুলি চালানো

### ধাপ ১: ক্যালকুলেটর সার্ভার চালু করুন

সার্ভারের জন্য কোন আজুর কনফিগারেশন প্রয়োজন নেই। নিচের কমান্ডগুলো এই স্যাম্পলের ডিরেক্টরি থেকে চালান।
উদাহরণটি পোর্ট **18081** ব্যবহার করে যাতে অন্য স্যাম্পলের সাথে সংঘর্ষ না হয়; ডিফল্ট ৮০৮০ থাকে।

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP এন্ডপয়েন্ট হল `http://localhost:18081/mcp`। হেলথ ও ডিসকভারি তথ্য পাওয়া যায়
`http://localhost:18081/health` এবং `http://localhost:18081/info` এ।
Streamable HTTP পুরানো SSE-শুধুমাত্র ট্রান্সপোর্ট প্রতিস্থাপন করেছে; `/sse` ও `/v1/tools` এন্ডপয়েন্ট নয়।

### ধাপ ২: সরাসরি ক্লায়েন্ট দিয়ে পরীক্ষা করুন

আরেকটি পাওয়ারশেল টার্মিনালে:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

কোন ইনপুট প্রয়োজন নেই। সব নয়টি টুল পরীক্ষিত হয়। প্রত্যাশিত গাণিতিক ফলাফলগুলোর মধ্যে রয়েছে
৮, ৬, ৪২, ৫, ২৫৬, ৪, ২, এবং ৫.৫, তারপরে হেল্প টেক্সট।

### ধাপ ৩: AI ক্লায়েন্ট দিয়ে পরীক্ষা করুন

প্রয়োজনীয়তাসমূহের মত প্রমাণীকরণ করার পর, একই টার্মিনালে AI ক্লায়েন্ট কনফিগার করুন:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

`Tool executed: add` লাইন আশা করুন যার সাথে আছে `41.80`, এবং মডেলের উত্তর।
সিঙ্গল-প্রম্পট মোড ইনপুটের জন্য অপেক্ষা না করেই বের হয়ে যায়। মূল চার-প্রম্পট ডেমো চালাতে:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

ডেমো `add`, `squareRoot`, `help`, এবং চেইনড `power` তারপর `divide` কল করে।
প্রত্যাশিত সংখ্যা উত্তরগুলো ৪১.৮, ১২, ও ৬৪। আর্গুমেন্ট ছাড়লেও এই ডেমো চলে।

### ধাপ ৪: ইন্টারেক্টিভ বট চালান

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

লিখুন `Multiply 6 by 7 using the calculator service`, তারপর `exit` বা `quit` লিখুন।
প্রকৃত `multiply` টুল রেজাল্ট ৪২ আশা করুন। খালি লাইন উপেক্ষিত; EOF সেশন শেষ করে।
এই এন্ট্রি পয়েন্টের নন-ইন্টারেক্টিভ স্মোক টেস্টের জন্য:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

উভয় AI এন্ট্রি পয়েন্ট `--prompt "question"`, `--demo`, ও `--interactive` গ্রহণ করে।
অবৈধ অপশন কানেকশন খোলার আগে ব্যর্থ করে। প্রতিটি মেভেন `-D...` আর্গুমেন্ট পাওয়ারশেলে সম্পূর্ণ কোটেড।
বসে `$env:NAME = "value"` এর পরিবর্তে Bash এ `export NAME=value` ব্যবহার করুন।

**কোটা:** AI স্যাম্পলগুলি ক্রমান্বয়ে চালান। একটি সাধারণ প্রম্পট সাধারণত দুইবার মডেল অনুরোধ করে;
সম্পূর্ণ ডেমো সাধারণত নয়বার, টুল-রেজাল্ট ফলো-আপসহ। একটি ভাগ হত্তয়া ১০ RPM ডিপ্লয়মেন্টে,
পরবর্তী AI রান আগে নতুন কোটা উইন্ডো দিন। ৪২৯ ত্রুটি স্পষ্টভাবে ব্যর্থ হয়, স্বয়ংক্রিয় পুনরায় চেষ্টা ছাড়া;
সার্ভিসের retry-after নির্দেশিকা অনুসরণ করুন। প্রকৃত অনুরোধ সংখ্যা মডেলের ওপর নির্ভর করে।
অফলাইন পরীক্ষা কোন কোটা খরচ করে না এবং লাইভ লুনার প্রাপ্যতা বা উত্তর মান যাচাই করে না।

### কনফিগারেশন এবং শাটডাউন

| সেটিং | ডিফল্ট / আচরণ |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; বেস URL, `/mcp` ছাড়া |
| `-Dmcp.server.url=...` | সকল ক্লায়েন্টের জন্য `MCP_SERVER_URL` ওভাররাইড করে |
| `AZURE_OPENAI_ENDPOINT` | শুধুমাত্র AI ক্লায়েন্টের জন্য প্রয়োজনীয়; রিসোর্স URL বা `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; একটি আজুর ডিপ্লয়মেন্ট নাম |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; ধনাত্মক পূর্ণসংখ্যা |
| যুক্তির প্রচেষ্টা | সর্বদা `none`, টুল-লুপ ফলো-আপসহ |

ওভাররাইড করা ডিপ্লয়মেন্টকে `reasoning_effort=none` এবং `max_completion_tokens` সাপোর্ট করতে হবে।
ক্লায়েন্টরা স্বয়ংক্রিয়ভাবে `.env` ফাইল পড়ে না। পরীক্ষার পর `Ctrl+C` দিয়ে সার্ভার বন্ধ করুন।
ক্লায়েন্টগুলো সাধারণত রিটার্ন করে, `System.exit` অথবা শাটডাউন স্লিপ ছাড়া।

## অফলাইন পরীক্ষাসমূহ

```powershell
mvn -B -ntp clean verify
```

সকল পরীক্ষা আজুরের দৃষ্টিতে অফলাইন: প্রোটোকল স্যুট একটি স্প্রিং সার্ভার শুরু করে এবং
OpenAI-সঙ্গত স্টাব র‍্যান্ডম লুপব্যাক পোর্টে, তারপর সেগুলো বন্ধ করে দেয়। মেভেন হয়তো ডিপেন্ডেন্সি ডাউনলোড করতে হবে।
কোন ক্রেডেনশিয়াল, লাইভ ডিপ্লয়মেন্ট, বা পূর্ব-বিদ্যমান MCP সার্ভার ব্যবহার হয় না।

- ক্যালকুলেটর ইউনিট টেস্ট সব গাণিতিক অপারেশন, দশমিক ফলাফল, সাহায্য এবং ডোমেইন ত্রুটিগুলি কভার করে।
- MCP পরীক্ষাগুলো ইনিশিয়ালাইজেশন, ডিসকভারি, নয়টি টুল কল, টুল ব্যর্থতা, এবং হেলথ/ইনফো কভার করে।
- AI প্রোটোকল পরীক্ষাগুলো পুরো ডেমো এবং ইন্টারেক্টিভ বট বাস্তব ক্যালকুলেটরের বিরুদ্ধে চালায়,
  নিশ্চিত করে টুল ফলাফল পরবর্তী কমপ্লিশনে প্রবাহিত হয়, এবং প্রতিটি HTTP বডি দেখে লুনা,
  `reasoning_effort: "none"`, এবং `max_completion_tokens` কোনও লেগ্যাসি `max_tokens` ছাড়াই।
- কনফিগারেশন/ইনপুট পরীক্ষা ডিপ্লয়মেন্ট এবং এন্ডপয়েন্ট ওভাররাইড, খালি লাইন, EOF, exit/quit,
  সিঙ্গল-প্রম্পট মোড, অবৈধ অপশন, এবং ত্রুটি প্রবাহ কভার করে। কোটা পরীক্ষাগুলো প্রমাণ করে ৪২৯ পুনরায় চেষ্টা হয় না।

## সবকিছু মিলে কিভাবে কাজ করে

যখন আপনি AI কে জিজ্ঞেস করেন "৫ + ৩ কত?":

১. **আপনি** প্রকৃত ভাষায় AI কে প্রশ্ন করেন
২. **AI** আপনার রিকোয়েস্ট বিশ্লেষণ করে এবং বুঝতে পারে আপনি যোগ চান
৩. **AI** MCP সার্ভারে কল করে: `add(5.0, 3.0)`
৪. **ক্যালকুলেটর সার্ভিস** গণনা করে: `5.0 + 3.0 = 8.0`
৫. **ক্যালকুলেটর সার্ভিস** রিটার্ন করে: `"5.00 + 3.00 = 8.00"`
৬. **AI** ফলাফল পেয়ে একটি স্বাভাবিক উত্তরের আকার দেয়
৭. **আপনি** পান: "৫ এবং ৩ এর যোগফল ৮"

## পরবর্তী ধাপসমূহ

আরও উদাহরণের জন্য দেখুন [অধ্যায় ০৪: ব্যবহারিক নমুনা](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা শুদ্ধতার জন্য চেষ্টা করি, অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার স্বভাষায় কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে প্রয়োজনীয় ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->