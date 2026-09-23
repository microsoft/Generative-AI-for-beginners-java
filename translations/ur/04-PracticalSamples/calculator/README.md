# MCP کیلکولیٹر ٹیوٹوریل برائے ابتدائی افراد

## مواد کا جدول

- [آپ کیا سیکھیں گے](#آپ-کیا-سیکھیں-گے)
- [پیشگی ضروریات](#پیشگی-ضروریات)
- [انحصار کی ورژنز](#انحصار-کی-ورژنز)
- [پروجیکٹ کے ساخت کی سمجھ](#پروجیکٹ-کی-ساخت-کی-سمجھ)
- [مرکزی اجزاء کی وضاحت](#مرکزی-اجزاء-کی-وضاحت)
  - [1. مرکزی درخواست](#1-مرکزی-درخواست)
  - [2. کیلکولیٹر سروس](#2-کیلکولیٹر-سروس)
  - [3. براہ راست MCP کلائنٹ](#3-براہ-راست-mcp-کلائنٹ)
  - [4. AI سے لیس کلائنٹ](#4-ai-سے-لیس-کلائنٹ)
- [مثالیں چلانا](#مثالیں-چلانا)
- [آف لائن ٹیسٹ](#آف-لائن-ٹیسٹ)
- [یہ سب کیسے مل کر کام کرتا ہے](#یہ-سب-کیسے-مل-کر-کام-کرتا-ہے)
- [اگلے اقدامات](#اگلے-اقدامات)

## آپ کیا سیکھیں گے

یہ ٹیوٹوریل بتاتا ہے کہ ماڈل کانٹیکسٹ پروٹوکول (MCP) استعمال کرتے ہوئے کیلکولیٹر سروس کیسے بنائیں۔ آپ سمجھیں گے:

- AI کو ایک ٹول کے طور پر استعمال کرنے کے لیے سروس کیسے تخلیق کی جائے
- MCP سروسز سے براہ راست رابطہ کیسے قائم کیا جائے
- AI ماڈلز خودکار طریقے سے ٹولز کا انتخاب کیسے کرسکتے ہیں
- براہ راست پروٹوکول کالز اور AI کی مدد سے تعاملات میں کیا فرق ہے

## پیشگی ضروریات

شروع کرنے سے پہلے، یہ بات یقینی بنائیں کہ آپ کے پاس ہے:
- جاوا 21 یا اس سے اوپر انسٹال شدہ ہو
- میون برائے انحصار مینجمنٹ
- جاوا اور اسپرنگ بوٹ کی بنیادی سمجھ

صرف AI کلائنٹس کو Azure OpenAI کی تنصیب اور ایک تصدیق شدہ `DefaultAzureCredential` کی ضرورت ہوتی ہے،
جیسے کہ لوکل موجود Azure CLI سائن ان یا Azure میں ایک منیجڈ شناخت۔ اس شناخت کو
Cognitive Services OpenAI User رول چاہیے۔ دیکھیں [باب 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md)۔
سرور، براہ راست SDK کلائنٹ، اور تمام خودکار ٹیسٹ کو Azure اکاؤنٹ یا ماڈل تک رسائی کی ضرورت نہیں ہے۔

## انحصار کی ورژنز

2026-09-14 کو تصدیق شدہ ریلیز انحصارات:

| انحصار | ورژن |
| --- | --- |
| اسپرنگ بوٹ | 4.1.1 |
| اسپرنگ AI | 2.0.1 |
| MCP جاوا SDK (اسپرنگ AI کے زیر انتظام) | 2.0.0 |
| LangChain4j / کور | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j سرکاری OpenAI اڈیپٹر | 1.20.0-beta30 |
| OpenAI جاوا SDK | 4.63.1 |
| Azure شناخت | 1.18.6 |
| JUnit Jupiter (بوٹ مینیجد) | 6.0.3 |

MCP اور سرکاری OpenAI اڈیپٹرز Maven Central میں شائع شدہ بیٹا ریلیزز ہیں، snapshots نہیں۔
ان کا ورژن LangChain4j کور سے مختلف ہے۔ کسی snapshot یا milestone ذخیرے کی ضرورت نہیں۔
صرف کلائنٹ کی انحصارات کا دائرہ کار ٹیسٹ کا ہے کیونکہ چلائی جانے والی مثالیں `src/test/java` میں ہوتی ہیں۔

## پروجیکٹ کی ساخت کی سمجھ

کیلکولیٹر پروجیکٹ میں متعدد اہم فائلز ہیں:

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

## مرکزی اجزاء کی وضاحت

### 1. مرکزی درخواست

**فائل:** `McpServerApplication.java`

یہ ہمارے کیلکولیٹر سروس کا داخلہ نقطہ ہے۔ یہ ایک معیاری اسپرنگ بوٹ ایپلیکیشن ہے جس میں ایک خاص اضافہ ہے:

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

**یہ کیا کرتا ہے:**
- پورٹ 8080 پر اسپرنگ بوٹ ویب سرور شروع کرتا ہے
- ایک `ToolCallbackProvider` بناتا ہے جو ہمارے کیلکولیٹر طریقوں کو MCP ٹولز کے طور پر دستیاب کر دیتا ہے
- `@Bean` تشریحے بتاتی ہے کہ اسکو اسپرنگ ایک جزو کے طور پر منظم کرے جو دوسرے حصے استعمال کر سکیں

### 2. کیلکولیٹر سروس

**فائل:** `CalculatorService.java`

یہاں سب ریاضیاتی کام ہوتے ہیں۔ ہر طریقہ `@Tool` سے نشان زد ہے تاکہ MCP کے ذریعے دستیاب ہو:

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
    
    // مزید کیلکولیٹر کے عملیات...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**اہم خصوصیات:**

1. **`@Tool` تشریح:** یہ MCP کو بتاتی ہے کہ یہ طریقہ بیرونی کلائنٹس کی طرف سے کال کیا جا سکتا ہے
2. **واضح وضاحتیں:** ہر ٹول کی وضاحت ہوتی ہے جو AI ماڈلز کو سمجھنے میں مدد دیتی ہے کہ اسے کب استعمال کرنا ہے
3. **یکساں واپسی فارمیٹ:** تمام آپریشنز انسانی قابل فہم سٹرنگز واپس کرتے ہیں جیسے "5.00 + 3.00 = 8.00"
4. **غلطی کا انتظام:** صفر پر تقسیم اور منفی جذر دوم غلطی کے پیغامات واپس کرتے ہیں

**دستیاب آپریشنز:**
- `add(a, b)` - دو اعداد کا جمع کرتا ہے
- `subtract(a, b)` - دوسرے نمبر کو پہلے سے منہا کرتا ہے
- `multiply(a, b)` - دو اعداد کا ضرب لگاتا ہے
- `divide(a, b)` - پہلے کو دوسرے سے تقسیم کرتا ہے (صفر چیک کے ساتھ)
- `power(base, exponent)` - بیس کو اکسپننٹ کی طاقت پر اٹھاتا ہے
- `squareRoot(number)` - جذر دوم نکالتا ہے (منفی چیک کے ساتھ)
- `modulus(a, b)` - تقسیم کا باقی حصہ واپس کرتا ہے
- `absolute(number)` - مطلق قدر واپس کرتا ہے
- `help()` - تمام آپریشنز کی معلومات دیتا ہے

### 3. براہ راست MCP کلائنٹ

دیکھیں [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)۔

یہ کلائنٹ `HttpClientStreamableHttpTransport` کو `/mcp` پر استعمال کرتا ہے، کنکشن کو شروع کرتا ہے،
سرور کو پنگ کرتا ہے، اور ٹول لسٹ کی صفحہ بندی کی پیروی کرتا ہے۔ یہ چیک کرتا ہے کہ تمام نو متوقع ٹولز
موجود ہیں اور ہر ایک کو بغیر AI ماڈل کے کال کرتا ہے، بشمول `modulus` اور `help` کے۔

موجودہ درخواست بنانے والا اس طرح دکھتا ہے:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

پروٹوکول کی غلطیاں کلائنٹ کو ناکام کر دیتی ہیں بجائے اس کے کہ گمراہ کن کامیابی دکھائیں۔ MCP کلائنٹ
try-with-resources کے ساتھ بند کیا جاتا ہے، بشمول جب دریافت یا ٹول کال ناکام ہو۔

### 4. AI سے لیس کلائنٹ

دیکھیں [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
اور [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)۔

`OpenAiOfficialChatModel` موجودہ LangChain4j `ChatModel` API کو نافذ کرتا ہے۔
`StreamableHttpMcpTransport` اسے SDK کلائنٹ کے اسی `/mcp` اینڈپوائنٹ سے جوڑتا ہے۔
`AiServices` ٹولز کی دریافت کرتا ہے اور ٹول کال/نتیجہ کی گفتگو کا انتظام کرتا ہے۔

ڈیفالٹ تعیناتی **GPT-5.6 Luna** ہے، جس میں وضاحتاً reasoning غیر فعال ہے:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

یہ ڈیفالٹس ہر تکمیل پر لاگو ہوتے ہیں، بشمول ٹول کے نفاذ کے بعد کے مراحل۔
کلائنٹ ایک تجدید پذیر `BearerTokenCredential` استعمال کرتا ہے جو `DefaultAzureCredential`
اور `https://ai.azure.com/.default` اسکوپ پر مبنی ہوتا ہے، ایک وقت کے API کلید کے بجائے۔
ریسورس URLs اور جو URLs پہلے سے `/openai/v1` پر ختم ہوتے ہیں دونوں قبول کیے جاتے ہیں۔

بوٹ ایک محدود گفتگو کی تاریخ رکھتا ہے، `Tool executed: ...` اصل MCP نتیجہ کے ساتھ پرنٹ کرتا ہے،
اور اگر جواب ٹولز کو چھوڑ دے تو ناکام ہو جاتا ہے۔ ٹول لوپس کو چار دوروں تک محدود کیا گیا ہے۔
تصدیق، ماڈل، MCP، اور ٹول کی غلطیاں منتقل ہوتی رہتی ہیں؛ خودکار ماڈل ریٹریز غیر فعال ہیں۔
MCP ٹرانسپورٹ/کلائنٹ اور سرکاری OpenAI کلائنٹ کامیابی یا ناکامی پر بند ہو جاتے ہیں۔

## مثالیں چلانا

### مرحلہ 1: کیلکولیٹر سرور شروع کریں

سرور کے لیے Azure کی کوئی ترتیب درکار نہیں۔ نیچے کمانڈز اس نمونے کے ڈائریکٹری سے چلائی جائیں۔
یہ مثال پورٹ **18081** استعمال کرتی ہے تاکہ کسی اور نمونے کے ساتھ تصادم نہ ہو؛ ڈیفالٹ 8080 رہتا ہے۔

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP اینڈپوائنٹ `http://localhost:18081/mcp` ہے۔ صحت اور دریافت کی معلومات پر ہیں
`http://localhost:18081/health` اور `http://localhost:18081/info`۔
اسٹریم ایبل HTTP پرانے SSE-صرف ٹرانسپورٹ کی جگہ لے لیتا ہے؛ `/sse` اور `/v1/tools` اینڈپوائنٹس نہیں ہیں۔

### مرحلہ 2: براہ راست کلائنٹ کے ساتھ ٹیسٹ کریں

دوسرے PowerShell ٹرمینل میں:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

کوئی ان پٹ درکار نہیں۔ تمام نو ٹولز استعمال کیے جاتے ہیں۔ متوقع حسابی نتائج میں شامل ہیں
8، 6، 42، 5، 256، 4، 2، اور 5.5، اس کے بعد مدد کا متن۔

### مرحلہ 3: AI کلائنٹ کے ساتھ ٹیسٹ کریں

جیسا کہ پیشگی ضروریات میں بیان کیا گیا ہے، AI کلائنٹ اسی ٹرمینل میں ترتیب دیں:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

توقع کریں کہ `Tool executed: add` کی لائن 41.80 کے ساتھ آئے، اس کے بعد ماڈل کا جواب۔
یکجہتی وضع بغیر ان پٹ کے باہر نکلتی ہے۔ اصل چار پرامپٹ ڈیمو چلانے کے لیے:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

ڈیمو `add`, `squareRoot`, `help` اور پھر `power` کے بعد `divide` آپریشن کال کرتا ہے۔
متوقع عددی جوابات 41.8، 12، اور 64 ہیں۔ دلائل چھوڑنے پر بھی یہ ڈیمو چلتا ہے۔

### مرحلہ 4: انٹرایکٹو بوٹ چلائیں

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

درج کریں `Multiply 6 by 7 using the calculator service`، پھر `exit` یا `quit`۔
حقیقی `multiply` ٹول کا نتیجہ 42 کی توقع کریں۔ خالی لائنیں نظر انداز کی جاتی ہیں؛ EOF بھی سیشن ختم کرتا ہے۔
اس داخلی نقطہ کے غیر انٹرایکٹو سمoke ٹیسٹ کے لیے:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

دونوں AI داخلی نقاط `--prompt "question"`, `--demo`, اور `--interactive` قبول کرتے ہیں۔
غلط آپشنز کنکشن کھولنے سے پہلے ناکام ہو جاتے ہیں۔ ہر میون `-D...` دلیل پوری طرح PowerShell کے لیے کوٹ کی گئی ہے۔
باش میں، `$env:NAME = "value"` کے بجائے `export NAME=value` استعمال کریں۔

**کوٹا:** AI نمونوں کو باری باری چلائیں۔ ایک سادہ پرامپٹ عام طور پر دو ماڈل درخواستیں لیتا ہے؛
مکمل ڈیمو عام طور پر نو لیتا ہے، بشمول ٹول-نتیجہ کے مراحل۔ 10 RPM مشترکہ تعیناتی کے ساتھ،
اگلی AI رن سے پہلے تازہ کوٹا ونڈو ممکن بنائیں۔ 429 خطا واضح طور پر ناکام ہو جاتی ہے بغیر
خودکار ریٹری کے؛ سروس کی Retry-After رہنمائی پر عمل کریں۔ اصل درخواست کی گنتی ماڈل پر منحصر ہے۔
آف لائن ٹیسٹ کسی کوٹا کا استعمال نہیں کرتے اور لائیو Luna دستیابی یا جواب کی معیار کا تعین نہیں کرتے۔

### ترتیب اور بند کرنا

| سیٹنگ | ڈیفالٹ / رویہ |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; بنیادی URL، بغیر `/mcp` کے |
| `-Dmcp.server.url=...` | تمام کلائنٹس کے لیے `MCP_SERVER_URL` کو اوور رائڈ کرتا ہے |
| `AZURE_OPENAI_ENDPOINT` | صرف AI کلائنٹس کے لیے ضروری؛ ریسورس URL یا `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure تعیناتی کا نام |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; مثبت عدد |
| Reasoning کوشش | ہمیشہ `none`، بشمول ٹول-لوپ کے بعد کے مراحل |

ایک اوور رائڈ شدہ تعیناتی کو `reasoning_effort=none` اور `max_completion_tokens` کی حمایت کرنی چاہیے۔
کلائنٹس خود بخود `.env` فائل نہیں پڑھتے۔ ٹیسٹنگ کے بعد سرور کو `Ctrl+C` سے بند کریں۔
کلائنٹس معمول کے مطابق واپس آتے ہیں بغیر `System.exit` یا بند کرنے کی نیند کے۔

## آف لائن ٹیسٹ

```powershell
mvn -B -ntp clean verify
```

تمام ٹیسٹ Azure کے لحاظ سے آف لائن ہیں: پروٹوکول کا مجموعہ اسپرنگ سرور اور
OpenAI-مطابق اسٹب کو تصادفی لوپ بیک پورٹس پر شروع کرتا ہے، پھر انہیں بند کر دیتا ہے۔ میون اب بھی
انحصارات ڈاؤن لوڈ کر سکتا ہے۔ کوئی اسناد، لائیو تعیناتی، یا پہلے سے موجود MCP سرور استعمال نہیں ہوتا۔

- کیلکولیٹر یونٹ ٹیسٹ تمام حسابی آپریشنز، عشری نتائج، مدد، اور ڈومین کی غلطیوں کا احاطہ کرتے ہیں۔
- MCP ٹیسٹ ابتدائیہ، دریافت، تمام نو ٹول کالز، ٹول کی ناکامیاں، اور صحت/معلومات کا احاطہ کرتے ہیں۔
- AI پروٹوکول ٹیسٹ مکمل ڈیمو اور انٹرایکٹو بوٹ کو اصلی کیلکولیٹر کے خلاف چلاتے ہیں،
  تصدیق کرتے ہیں کہ ٹول نتائج اگلی تکمیل کو فراہم کرتے ہیں، اور ہر HTTP باڈی میں Luna،
  `reasoning_effort: "none"`, اور `max_completion_tokens` بغیر پرانے `max_tokens` کو معائنہ کرتے ہیں۔
- ترتیب/ان پٹ ٹیسٹ تعیناتی اور اینڈپوائنٹ اوور رائڈز، خالی لائنیں، EOF، بند یا چھوڑنا،
  یکجہتی وضع، غلط آپشنز، اور غلطی کی منتقلی کا احاطہ کرتے ہیں۔ کوٹا ٹیسٹ ثابت کرتے ہیں کہ 429 کو دوبارہ کوشش نہیں کی جاتی۔

## یہ سب کیسے مل کر کام کرتا ہے

جب آپ AI سے پوچھتے ہیں "5 + 3 کیا ہے؟" تو مکمل عمل کچھ یوں ہوتا ہے:

1. **آپ** AI سے قدرتی زبان میں سوال کرتے ہیں
2. **AI** آپ کی درخواست کا تجزیہ کرتا ہے اور سمجھتا ہے کہ آپ جمع چاہتے ہیں
3. **AI** MCP سرور کو کال کرتا ہے: `add(5.0, 3.0)`
4. **کیلکولیٹر سروس** انجام دیتا ہے: `5.0 + 3.0 = 8.0`
5. **کیلکولیٹر سروس** واپس کرتا ہے: `"5.00 + 3.00 = 8.00"`
6. **AI** نتیجہ وصول کرتا ہے اور قدرتی جواب بناتا ہے
7. **آپ کو** ملتا ہے: "5 اور 3 کا مجموعہ 8 ہے"

## اگلے اقدامات

مزید مثالوں کے لیے دیکھیں [باب 04: عملی مثالیں](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ڈس کلیمر**:
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ جبکہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا عدم درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنے مادری زبان میں مستند ماخذ سمجھی جائے گی۔ حساس معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم قبول نہیں کرتے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->