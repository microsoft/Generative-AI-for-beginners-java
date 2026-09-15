# آموزش محاسبه‌گر MCP برای مبتدیان

## فهرست مطالب

- [آنچه یاد خواهید گرفت](#آنچه-یاد-خواهید-گرفت)
- [پیش‌نیازها](#پیش‌نیازها)
- [نسخه‌های وابستگی](#نسخه‌های-وابستگی)
- [درک ساختار پروژه](#درک-ساختار-پروژه)
- [توضیح اجزای اصلی](#توضیح-اجزای-اصلی)
  - [1. برنامه اصلی](#1-برنامه-اصلی)
  - [2. سرویس محاسبه‌گر](#2-سرویس-محاسبه‌گر)
  - [3. کلاینت مستقیم MCP](#3-کلاینت-مستقیم-mcp)
  - [4. کلاینت مبتنی بر هوش مصنوعی](#4-کلاینت-مبتنی-بر-هوش-مصنوعی)
- [اجرای نمونه‌ها](#اجرای-نمونه‌ها)
- [آزمایش‌های آفلاین](#آزمایش‌های-آفلاین)
- [چگونگی کارکرد همه با هم](#نحوه-کارکرد-کل-فرایند)
- [گام‌های بعدی](#مراحل-بعدی)

## آنچه یاد خواهید گرفت

این آموزش نحوه ساخت یک سرویس ماشین‌حساب با استفاده از پروتکل مدل متن (MCP) را توضیح می‌دهد. شما خواهید فهمید:

- چگونه سرویسی بسازید که هوش مصنوعی بتواند به‌عنوان یک ابزار از آن استفاده کند
- چگونه ارتباط مستقیم با سرویس‌های MCP را برقرار کنید
- چگونه مدل‌های هوش مصنوعی می‌توانند به طور خودکار انتخاب کنند که کدام ابزارها را استفاده کنند
- تفاوت بین فراخوانی‌های مستقیم پروتکل و تعاملات با کمک هوش مصنوعی

## پیش‌نیازها

قبل از شروع، اطمینان حاصل کنید که دارید:
- جاوا نسخه ۲۱ یا بالاتر نصب شده باشد
- Maven برای مدیریت وابستگی‌ها
- درک ابتدایی از جاوا و Spring Boot

تنها کلاینت‌های هوش مصنوعی نیاز به استقرار Azure OpenAI و یک `DefaultAzureCredential` احراز هویت شده دارند،
مانند ورود به سیستم Azure CLI به صورت محلی یا یک هویت مدیریت‌شده در Azure. هویت باید
نقش کاربر OpenAI خدمات شناختی را روی منبع داشته باشد. به [فصل ۲](../../02-SetupDevEnvironment/getting-started-azure-openai.md) مراجعه کنید.
سرور، کلاینت SDK مستقیم و تمامی تست‌های خودکار به حساب Azure یا دسترسی به مدل نیاز ندارند.

## نسخه‌های وابستگی

وابستگی‌های منتشر شده بررسی شده در ۱۴-۰۹-۲۰۲۶:

| وابستگی | نسخه |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (مدیریت شده توسط Spring AI) | 2.0.0 |
| LangChain4j / هسته | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| آداپتور رسمی OpenAI LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (مدیریت شده توسط Boot) | 6.0.3 |

آداپتورهای MCP و رسمی OpenAI نسخه‌های بتای منتشر شده در Maven Central هستند، نه نسخه‌های snapshot.
نسخه‌های آنها با هسته LangChain4j متفاوت است. نیازی به مخازن snapshot یا milestone نیست.
وابستگی‌های فقط کلاینت دارای دامنه تست هستند چون نمونه‌های اجرایی زیر `src/test/java` قرار دارند.

## درک ساختار پروژه

پروژه ماشین‌حساب چند فایل مهم دارد:

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

## توضیح اجزای اصلی

### 1. برنامه اصلی

**فایل:** `McpServerApplication.java`

این نقطه ورودی سرویس ماشین‌حساب ما است. این یک برنامه استاندارد Spring Boot با یک افزودنی ویژه است:

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

**این چه کاری انجام می‌دهد:**
- یک سرور وب Spring Boot روی پورت 8080 راه‌اندازی می‌کند
- یک `ToolCallbackProvider` ایجاد می‌کند که متدهای ماشین‌حساب ما را به‌عنوان ابزارهای MCP در دسترس قرار می‌دهد
- نشانه‌گذاری `@Bean` به Spring می‌گوید این را به عنوان یک کامپوننت مدیریت کند که بخش‌های دیگر بتوانند استفاده کنند

### 2. سرویس محاسبه‌گر

**فایل:** `CalculatorService.java`

اینجا جایی است که تمام محاسبات انجام می‌شود. هر متد با `@Tool` علامت‌گذاری شده تا از طریق MCP قابل دسترس باشد:

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
    
    // عملیات‌های بیشتر ماشین‌حساب...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**ویژگی‌های کلیدی:**

۱. **نشانه‌گذاری `@Tool`**: این به MCP می‌گوید که این متد می‌تواند توسط کلاینت‌های خارجی فراخوانی شود
۲. **توصیف‌های واضح**: هر ابزار یک توضیح دارد که به مدل‌های هوش مصنوعی کمک می‌کند تا زمان استفاده از آن را بفهمند
۳. **فرمت بازگشتی یکنواخت**: تمام عملیات رشته‌های قابل خواندن توسط انسان مانند "5.00 + 3.00 = 8.00" برمی‌گردانند
۴. **مدیریت خطا**: تقسیم بر صفر و جذر اعداد منفی پیام خطا برمی‌گردانند

**عملیات در دسترس:**
- `add(a, b)` - دو عدد را جمع می‌کند
- `subtract(a, b)` - عدد دوم را از اولی کم می‌کند
- `multiply(a, b)` - دو عدد را ضرب می‌کند
- `divide(a, b)` - عدد اول را بر دوم تقسیم می‌کند (با بررسی صفر بودن)
- `power(base, exponent)` - پایه را به توان رسانده
- `squareRoot(number)` - جذر عدد را محاسبه می‌کند (با بررسی منفی بودن)
- `modulus(a, b)` - باقی‌مانده تقسیم را برمی‌گرداند
- `absolute(number)` - قدر مطلق عدد را برمی‌گرداند
- `help()` - اطلاعاتی درباره همه عملیات می‌دهد

### 3. کلاینت مستقیم MCP

به [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java) مراجعه کنید.

این کلاینت از `HttpClientStreamableHttpTransport` در `/mcp` استفاده می‌کند، اتصال را مقداردهی اولیه می‌کند،
به سرور پینگ می‌زند و صفحه‌بندی لیست ابزار را دنبال می‌کند. بررسی می‌کند که همه نه ابزار مورد انتظار
موجود باشند و هر کدام از آنها را بدون مدل هوش مصنوعی فراخوانی می‌کند، از جمله `modulus` و `help`.

سازنده درخواست فعلی به این شکل است:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

خطاهای پروتکل باعث شکست کلاینت می‌شوند به جای چاپ پیام موفقیت گمراه‌کننده. کلاینت MCP
با try-with-resources بسته می‌شود، حتی وقتی کشف یا فراخوانی ابزار شکست می‌خورد.

### 4. کلاینت مبتنی بر هوش مصنوعی

به [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
و [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java) مراجعه کنید.

`OpenAiOfficialChatModel` پیاده‌سازی API فعلی LangChain4j `ChatModel` است.
`StreamableHttpMcpTransport` آن را به همان نقطه انتهایی `/mcp` مانند کلاینت SDK متصل می‌کند.
`AiServices` ابزارها را کشف و مدیریت گفتگوی فراخوانی ابزار / نتیجه را بر عهده دارد.

استقرار پیش‌فرض، **GPT-5.6 لونا** است که استدلال به‌طور صریح غیرفعال شده است:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

این تنظیمات پیش‌فرض برای هر تکمیل، از جمله دنبال کردن پس از اجرای ابزار، اعمال می‌شوند.
کلاینت از یک `BearerTokenCredential` قابل تازه‌سازی که توسط `DefaultAzureCredential`
و دامنه `https://ai.azure.com/.default` پشتیبانی می‌شود استفاده می‌کند، نه توکن یک‌باره به عنوان کلید API.
URL منابع و URLهایی که قبلاً با `/openai/v1` پایان یافته‌اند هر دو پذیرفته می‌شوند.

ربات تاریخچه گفتگو را محدود نگه می‌دارد، پیام `ابزار اجرا شد: ...` را همراه با نتیجه واقعی
MCP چاپ می‌کند و در صورت رد ابزارها پاسخ را رد می‌کند. حلقه‌های ابزار محدود به چهار بازگشت هستند.
خطاهای احراز هویت، مدل، MCP و ابزار منتشر می‌شوند؛ تلاش خودکار مجدد مدل غیرفعال شده است.
هر دو انتقال/کلاینت MCP و کلاینت رسمی OpenAI در موفقیت یا شکست بسته می‌شوند.

## اجرای نمونه‌ها

### مرحله 1: راه‌اندازی سرور ماشین حساب

هیچ پیکربندی Azure برای سرور لازم نیست. دستورات زیر از دایرکتوری نمونه فعلی اجرا می‌شوند.
این مثال از پورت **18081** استفاده می‌کند تا با نمونه دیگری تداخل نداشته باشد؛ پیش‌فرض همچنان 8080 است.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

نقطه پایانی MCP در `http://localhost:18081/mcp` قرار دارد. اطلاعات سلامت و کشف در
`http://localhost:18081/health` و `http://localhost:18081/info` هستند.
HTTP استریم‌پذیر جایگزین حمل‌ونقل قدیمی فقط SSE شده است؛ `/sse` و `/v1/tools` نقاط پایانی نیستند.

### مرحله 2: آزمایش با کلاینت مستقیم

در یک ترمینال PowerShell دیگر:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

ورودی لازم نیست. همه نه ابزار استفاده می‌شوند. نتایج حسابی مورد انتظار شامل
8، 6، 42، 5، 256، 4، 2 و 5.5 است، به دنبال آن متن راهنما.

### مرحله 3: آزمایش با کلاینت هوش مصنوعی

پس از احراز هویت مطابق پیش‌نیازها، کلاینت هوش مصنوعی را در همان ترمینال تنظیم کنید:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

انتظار یک خط `Tool executed: add` همراه با `41.80`، به دنبال آن پاسخ مدل را داشته باشید.
حالت یک درخواست بدون انتظار برای ورودی خارج می‌شود. برای اجرای دموی چهار درخواستی اصلی:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

دمو `add`، `squareRoot`، `help` و عملیات زنجیره‌ای `power` سپس `divide` را فراخوانی می‌کند.
پاسخ‌های عددی مورد انتظار 41.8، 12 و 64 هستند. حذف آرگومان‌ها نیز این دمو را اجرا می‌کند.

### مرحله 4: اجرای ربات تعاملی

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

بنویسید `Multiply 6 by 7 using the calculator service`، سپس `exit` یا `quit`.
انتظار نتیجه واقعی ابزار `multiply` برابر با 42 را داشته باشید. خطوط خالی نادیده گرفته می‌شوند؛ EOF نیز جلسه را پایان می‌دهد.
برای آزمایش غیرتعاملی این نقطه ورود:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

هر دو نقطه ورود هوش مصنوعی گزینه‌های `--prompt "question"`، `--demo` و `--interactive` را می‌پذیرند.
گزینه‌های نامعتبر قبل از بازکردن اتصال شکست می‌خورند. هر آرگومان Maven `-D...` به طور کامل برای PowerShell کوته‌نویسی شده است.
در Bash، به جای `$env:NAME = "value"`, از `export NAME=value` استفاده کنید.

**سهمیه:** نمونه‌های هوش مصنوعی را به ترتیب اجرا کنید. معمولاً یک درخواست ساده دو درخواست مدل نیاز دارد؛
دمو کامل معمولاً نه درخواست، شامل دنبال کردن نتیجه ابزار. با استقرار مشترک 10 درخواست در دقیقه،
قبل از اجرای بعدی AI، یک پنجره سهمیه جدید را در نظر بگیرید. خطای 429 بدون تلاش مجدد اتوماتیک به صورت واضح رخ می‌دهد؛ دستورالعمل retry-after سرویس را دنبال کنید.
تعداد واقعی درخواست‌ها به مدل بستگی دارد.
آزمایش‌های آفلاین هیچ سهمیه‌ای مصرف نمی‌کنند و در دسترس بودن Luna یا کیفیت پاسخ زنده را برقرار نمی‌کنند.

### پیکربندی و خاموش‌کردن

| تنظیم | مقدار پیش‌فرض / رفتار |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; آدرس پایه، بدون `/mcp` |
| `-Dmcp.server.url=...` | جایگزین `MCP_SERVER_URL` برای همه کلاینت‌ها |
| `AZURE_OPENAI_ENDPOINT` | فقط برای کلاینت‌های هوش مصنوعی لازم است؛ آدرس منبع یا آدرس `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; نام یک استقرار Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; عدد صحیح مثبت |
| تلاش استدلال | همیشه `none`، شامل دنبال کردن حلقه ابزار |

یک استقرار جایگزین باید `reasoning_effort=none` و `max_completion_tokens` را پشتیبانی کند.
کلاینت‌ها به طور خودکار فایل `.env` را نمی‌خوانند. پس از آزمایش سرور را با `Ctrl+C` متوقف کنید.
کلاینت‌ها به‌طور معمول بدون `System.exit` یا خواب خاموش‌شدن برمی‌گردند.

## آزمایش‌های آفلاین

```powershell
mvn -B -ntp clean verify
```

همه آزمایش‌ها به صورت آفلاین نسبت به Azure هستند: مجموعه پروتکل سرور Spring و
یک سیم‌کارت سازگار با OpenAI را روی پورت‌های لوپ‌بک تصادفی راه‌اندازی می‌کند، سپس آنها را می‌بندد. Maven ممکن است
هنوز به دانلود وابستگی‌ها نیاز داشته باشد. هیچ اطلاعات هویتی، استقرار زنده یا سرور MCP پیش‌فرض استفاده نمی‌شود.

- آزمایش‌های واحد ماشین حساب تمام عملیات حسابی، نتایج اعشاری، راهنما و خطاهای دامنه را پوشش می‌دهد.
- آزمایش‌های MCP شروع، کشف، همه نه فراخوانی ابزار، شکست ابزار و سلامت/اطلاعات را آزمایش می‌کنند.
- آزمایش‌های پروتکل AI دمو کامل و ربات تعاملی را در برابر ماشین حساب واقعی اجرا می‌کنند،
  اطمینان می‌دهند که نتایج ابزار زیرمجموعه تکمیل بعدی را تغذیه می‌کنند، و هر بدنه HTTP را برای Luna،
  `reasoning_effort: "none"` و `max_completion_tokens` بدون `max_tokens` قدیمی بررسی می‌کنند.
- پیکربندی/ورودی آزمایش‌ها، جایگزینی استقرار و نقطه پایانی، خطوط خالی، EOF، خروج، حالت تک درخواست،
  گزینه‌های نامعتبر و پراکندگی خطا را پوشش می‌دهند. آزمایش سهمیه نشان می‌دهد 429 دوباره تلاش نمی‌شود.

## نحوه کارکرد کل فرایند

جریان کامل هنگام پرسیدن از AI با عبارت "5 + 3 چیست؟" به شرح زیر است:

1. **شما** در زبان طبیعی از AI سوال می‌کنید
2. **AI** درخواست شما را تحلیل و متوجه می‌شود که می‌خواهید جمع انجام دهید
3. **AI** سرور MCP را فراخوانی می‌کند: `add(5.0, 3.0)`
4. **خدمات ماشین حساب** عملیات را انجام می‌دهد: `5.0 + 3.0 = 8.0`
5. **خدمات ماشین حساب** بازمی‌گرداند: `"5.00 + 3.00 = 8.00"`
6. **AI** نتیجه را دریافت کرده و پاسخ طبیعی را قالب‌بندی می‌کند
7. **شما** دریافت می‌کنید: "مجموع ۵ و ۳ برابر ۸ است"

## مراحل بعدی

برای نمونه‌های بیشتر، به [فصل ۰۴: نمونه‌های کاربردی](../README.md) مراجعه کنید

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوء تفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->