<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:47:18+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "fa"
}
-->
# آموزش ماشین حساب MCP برای مبتدیان

## فهرست مطالب

- [آنچه یاد خواهید گرفت](../../../../../04-PracticalSamples/mcp/calculator)
- [پیش‌نیازها](../../../../../04-PracticalSamples/mcp/calculator)
- [درک ساختار پروژه](../../../../../04-PracticalSamples/mcp/calculator)
- [توضیح اجزای اصلی](../../../../../04-PracticalSamples/mcp/calculator)
  - [۱. برنامه اصلی](../../../../../04-PracticalSamples/mcp/calculator)
  - [۲. سرویس ماشین حساب](../../../../../04-PracticalSamples/mcp/calculator)
  - [۳. کلاینت مستقیم MCP](../../../../../04-PracticalSamples/mcp/calculator)
  - [۴. کلاینت مبتنی بر هوش مصنوعی](../../../../../04-PracticalSamples/mcp/calculator)
- [اجرای مثال‌ها](../../../../../04-PracticalSamples/mcp/calculator)
- [چگونگی همکاری اجزا](../../../../../04-PracticalSamples/mcp/calculator)
- [گام‌های بعدی](../../../../../04-PracticalSamples/mcp/calculator)

## آنچه یاد خواهید گرفت

این آموزش نحوه ساخت یک سرویس ماشین حساب با استفاده از پروتکل Model Context Protocol (MCP) را توضیح می‌دهد. شما یاد خواهید گرفت:

- چگونه یک سرویس ایجاد کنید که هوش مصنوعی بتواند از آن به عنوان ابزار استفاده کند
- چگونه ارتباط مستقیم با سرویس‌های MCP را تنظیم کنید
- چگونه مدل‌های هوش مصنوعی می‌توانند به طور خودکار ابزارهای مناسب را انتخاب کنند
- تفاوت بین تماس‌های مستقیم پروتکل و تعاملات مبتنی بر هوش مصنوعی

## پیش‌نیازها

قبل از شروع، مطمئن شوید که موارد زیر را دارید:
- نصب Java 21 یا بالاتر
- Maven برای مدیریت وابستگی‌ها
- یک حساب GitHub با توکن دسترسی شخصی (PAT)
- درک پایه‌ای از Java و Spring Boot

## درک ساختار پروژه

پروژه ماشین حساب شامل چندین فایل مهم است:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## توضیح اجزای اصلی

### ۱. برنامه اصلی

**فایل:** `McpServerApplication.java`

این نقطه ورود سرویس ماشین حساب ما است. این یک برنامه استاندارد Spring Boot با یک ویژگی خاص است:

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

**کارهایی که انجام می‌دهد:**
- یک سرور وب Spring Boot را روی پورت 8080 راه‌اندازی می‌کند
- یک `ToolCallbackProvider` ایجاد می‌کند که متدهای ماشین حساب ما را به عنوان ابزارهای MCP در دسترس قرار می‌دهد
- با استفاده از `@Bean` به Spring می‌گوید که این را به عنوان یک کامپوننت مدیریت کند که سایر بخش‌ها بتوانند از آن استفاده کنند

### ۲. سرویس ماشین حساب

**فایل:** `CalculatorService.java`

اینجا جایی است که تمام محاسبات ریاضی انجام می‌شود. هر متد با `@Tool` علامت‌گذاری شده تا از طریق MCP در دسترس باشد:

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**ویژگی‌های کلیدی:**

1. **Annotation `@Tool`**: این نشان می‌دهد که این متد می‌تواند توسط کلاینت‌های خارجی فراخوانی شود
2. **توضیحات واضح**: هر ابزار توضیحاتی دارد که به مدل‌های هوش مصنوعی کمک می‌کند بفهمند چه زمانی از آن استفاده کنند
3. **فرمت بازگشت سازگار**: تمام عملیات رشته‌های قابل خواندن انسانی مانند "5.00 + 3.00 = 8.00" را بازمی‌گردانند
4. **مدیریت خطا**: تقسیم بر صفر و جذر منفی پیام‌های خطا بازمی‌گردانند

**عملیات موجود:**
- `add(a, b)` - جمع دو عدد
- `subtract(a, b)` - تفریق عدد دوم از عدد اول
- `multiply(a, b)` - ضرب دو عدد
- `divide(a, b)` - تقسیم عدد اول بر عدد دوم (با بررسی صفر)
- `power(base, exponent)` - توان پایه به توان نمای
- `squareRoot(number)` - محاسبه جذر (با بررسی منفی بودن)
- `modulus(a, b)` - بازگشت باقی‌مانده تقسیم
- `absolute(number)` - بازگشت مقدار مطلق
- `help()` - اطلاعات مربوط به تمام عملیات را بازمی‌گرداند

### ۳. کلاینت مستقیم MCP

**فایل:** `SDKClient.java`

این کلاینت به طور مستقیم با سرور MCP ارتباط برقرار می‌کند بدون استفاده از هوش مصنوعی. این به صورت دستی توابع خاص ماشین حساب را فراخوانی می‌کند:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**کارهایی که انجام می‌دهد:**
1. **اتصال** به سرور ماشین حساب در `http://localhost:8080`
2. **لیست کردن** تمام ابزارهای موجود (توابع ماشین حساب ما)
3. **فراخوانی** توابع خاص با پارامترهای دقیق
4. **چاپ** نتایج به صورت مستقیم

**زمان استفاده:** زمانی که دقیقاً می‌دانید کدام محاسبه را می‌خواهید انجام دهید و می‌خواهید آن را به صورت برنامه‌ریزی شده فراخوانی کنید.

### ۴. کلاینت مبتنی بر هوش مصنوعی

**فایل:** `LangChain4jClient.java`

این کلاینت از یک مدل هوش مصنوعی (GPT-4o-mini) استفاده می‌کند که می‌تواند به طور خودکار تصمیم بگیرد از کدام ابزارهای ماشین حساب استفاده کند:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**کارهایی که انجام می‌دهد:**
1. **ایجاد** اتصال مدل هوش مصنوعی با استفاده از توکن GitHub شما
2. **اتصال** هوش مصنوعی به سرور MCP ماشین حساب ما
3. **دسترسی دادن** به هوش مصنوعی به تمام ابزارهای ماشین حساب ما
4. **اجازه دادن** به درخواست‌های زبان طبیعی مانند "مجموع 24.5 و 17.3 را محاسبه کن"

**هوش مصنوعی به طور خودکار:**
- می‌فهمد که شما می‌خواهید اعداد را جمع کنید
- ابزار `add` را انتخاب می‌کند
- `add(24.5, 17.3)` را فراخوانی می‌کند
- نتیجه را در یک پاسخ طبیعی بازمی‌گرداند

## اجرای مثال‌ها

### مرحله ۱: راه‌اندازی سرور ماشین حساب

ابتدا توکن GitHub خود را تنظیم کنید (برای کلاینت هوش مصنوعی لازم است):

**ویندوز:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**لینوکس/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

سرور را راه‌اندازی کنید:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

سرور روی `http://localhost:8080` راه‌اندازی خواهد شد. باید ببینید:
```
Started McpServerApplication in X.XXX seconds
```

### مرحله ۲: آزمایش با کلاینت مستقیم

در یک ترمینال جدید:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

خروجی مشابه زیر را خواهید دید:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### مرحله ۳: آزمایش با کلاینت هوش مصنوعی

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

خواهید دید که هوش مصنوعی به طور خودکار از ابزارها استفاده می‌کند:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## چگونگی همکاری اجزا

در اینجا جریان کامل زمانی که از هوش مصنوعی می‌پرسید "5 + 3 چند می‌شود؟" آورده شده است:

1. **شما** از هوش مصنوعی به زبان طبیعی سؤال می‌کنید
2. **هوش مصنوعی** درخواست شما را تحلیل می‌کند و متوجه می‌شود که شما جمع می‌خواهید
3. **هوش مصنوعی** سرور MCP را فراخوانی می‌کند: `add(5.0, 3.0)`
4. **سرویس ماشین حساب** عملیات را انجام می‌دهد: `5.0 + 3.0 = 8.0`
5. **سرویس ماشین حساب** نتیجه را بازمی‌گرداند: `"5.00 + 3.00 = 8.00"`
6. **هوش مصنوعی** نتیجه را دریافت می‌کند و یک پاسخ طبیعی قالب‌بندی می‌کند
7. **شما** پاسخ را دریافت می‌کنید: "مجموع 5 و 3 برابر با 8 است"

## گام‌های بعدی

برای مثال‌های بیشتر، به [فصل ۰۴: نمونه‌های عملی](../../README.md) مراجعه کنید.

**سلب مسئولیت**:  
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما تلاش می‌کنیم دقت را حفظ کنیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌ها باشند. سند اصلی به زبان اصلی آن باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حساس، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما مسئولیتی در قبال سوء تفاهم‌ها یا تفسیرهای نادرست ناشی از استفاده از این ترجمه نداریم.