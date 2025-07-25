<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:49:54+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "ur"
}
-->
# ایم سی پی کیلکولیٹر ٹیوٹوریل برائے ابتدائی افراد

## فہرست مضامین

- [آپ کیا سیکھیں گے](../../../../../04-PracticalSamples/mcp/calculator)
- [ضروری شرائط](../../../../../04-PracticalSamples/mcp/calculator)
- [پروجیکٹ کی ساخت کو سمجھنا](../../../../../04-PracticalSamples/mcp/calculator)
- [اہم اجزاء کی وضاحت](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. مرکزی ایپلیکیشن](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. کیلکولیٹر سروس](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. ڈائریکٹ ایم سی پی کلائنٹ](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. اے آئی سے چلنے والا کلائنٹ](../../../../../04-PracticalSamples/mcp/calculator)
- [مثالیں چلانا](../../../../../04-PracticalSamples/mcp/calculator)
- [یہ سب کیسے ایک ساتھ کام کرتا ہے](../../../../../04-PracticalSamples/mcp/calculator)
- [اگلے مراحل](../../../../../04-PracticalSamples/mcp/calculator)

## آپ کیا سیکھیں گے

یہ ٹیوٹوریل وضاحت کرتا ہے کہ ماڈل کانٹیکسٹ پروٹوکول (ایم سی پی) کا استعمال کرتے ہوئے کیلکولیٹر سروس کیسے بنائی جائے۔ آپ سمجھیں گے:

- ایسی سروس کیسے بنائیں جسے اے آئی ایک ٹول کے طور پر استعمال کر سکے
- ایم سی پی سروسز کے ساتھ براہ راست رابطہ کیسے قائم کریں
- اے آئی ماڈلز خودکار طور پر کون سے ٹولز استعمال کریں، یہ کیسے منتخب کرتے ہیں
- براہ راست پروٹوکول کالز اور اے آئی کی مدد سے تعاملات میں فرق

## ضروری شرائط

شروع کرنے سے پہلے، یقینی بنائیں کہ آپ کے پاس درج ذیل موجود ہیں:
- جاوا 21 یا اس سے زیادہ انسٹال ہو
- ڈیپینڈنسی مینجمنٹ کے لیے میون
- ایک گٹ ہب اکاؤنٹ اور پرسنل ایکسس ٹوکن (PAT)
- جاوا اور اسپرنگ بوٹ کی بنیادی سمجھ

## پروجیکٹ کی ساخت کو سمجھنا

کیلکولیٹر پروجیکٹ میں کئی اہم فائلیں شامل ہیں:

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

## اہم اجزاء کی وضاحت

### 1. مرکزی ایپلیکیشن

**فائل:** `McpServerApplication.java`

یہ ہماری کیلکولیٹر سروس کا انٹری پوائنٹ ہے۔ یہ ایک معیاری اسپرنگ بوٹ ایپلیکیشن ہے جس میں ایک خاص اضافہ شامل ہے:

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
- اسپرنگ بوٹ ویب سرور کو پورٹ 8080 پر شروع کرتا ہے
- ایک `ToolCallbackProvider` بناتا ہے جو ہماری کیلکولیٹر میتھڈز کو ایم سی پی ٹولز کے طور پر دستیاب کرتا ہے
- `@Bean` اینوٹیشن اسپرنگ کو بتاتا ہے کہ اسے ایک کمپوننٹ کے طور پر مینیج کرے جسے دیگر حصے استعمال کر سکیں

### 2. کیلکولیٹر سروس

**فائل:** `CalculatorService.java`

یہ وہ جگہ ہے جہاں تمام حساب کتاب ہوتا ہے۔ ہر میتھڈ کو `@Tool` کے ساتھ نشان زد کیا گیا ہے تاکہ اسے ایم سی پی کے ذریعے دستیاب کیا جا سکے:

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

**اہم خصوصیات:**

1. **`@Tool` اینوٹیشن**: یہ ایم سی پی کو بتاتا ہے کہ یہ میتھڈ بیرونی کلائنٹس کے ذریعے کال کیا جا سکتا ہے
2. **واضح وضاحتیں**: ہر ٹول کی وضاحت ہوتی ہے جو اے آئی ماڈلز کو سمجھنے میں مدد دیتی ہے کہ کب اسے استعمال کرنا ہے
3. **مستقل ریٹرن فارمیٹ**: تمام آپریشنز انسانی قابل فہم اسٹرنگز واپس کرتے ہیں جیسے "5.00 + 3.00 = 8.00"
4. **غلطیوں کا انتظام**: صفر سے تقسیم اور منفی اسکوائر روٹس غلطی کے پیغامات واپس کرتے ہیں

**دستیاب آپریشنز:**
- `add(a, b)` - دو نمبروں کو جمع کرتا ہے
- `subtract(a, b)` - دوسرے نمبر کو پہلے سے منفی کرتا ہے
- `multiply(a, b)` - دو نمبروں کو ضرب دیتا ہے
- `divide(a, b)` - پہلے نمبر کو دوسرے سے تقسیم کرتا ہے (صفر چیک کے ساتھ)
- `power(base, exponent)` - بیس کو ایکسپونینٹ کی طاقت پر لے جاتا ہے
- `squareRoot(number)` - اسکوائر روٹ نکالتا ہے (منفی چیک کے ساتھ)
- `modulus(a, b)` - تقسیم کا باقی حصہ واپس کرتا ہے
- `absolute(number)` - مطلق قدر واپس کرتا ہے
- `help()` - تمام آپریشنز کے بارے میں معلومات واپس کرتا ہے

### 3. ڈائریکٹ ایم سی پی کلائنٹ

**فائل:** `SDKClient.java`

یہ کلائنٹ ایم سی پی سرور سے براہ راست بات کرتا ہے بغیر اے آئی کے استعمال کے۔ یہ مخصوص کیلکولیٹر فنکشنز کو دستی طور پر کال کرتا ہے:

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

**یہ کیا کرتا ہے:**
1. **کنیکٹ** کرتا ہے کیلکولیٹر سرور سے `http://localhost:8080`
2. **لسٹ** کرتا ہے تمام دستیاب ٹولز (ہمارے کیلکولیٹر فنکشنز)
3. **کال** کرتا ہے مخصوص فنکشنز کو درست پیرامیٹرز کے ساتھ
4. **پرنٹ** کرتا ہے نتائج کو براہ راست

**کب استعمال کریں:** جب آپ کو بالکل معلوم ہو کہ کون سا حساب کتاب کرنا ہے اور اسے پروگرام کے ذریعے کال کرنا چاہتے ہیں۔

### 4. اے آئی سے چلنے والا کلائنٹ

**فائل:** `LangChain4jClient.java`

یہ کلائنٹ ایک اے آئی ماڈل (GPT-4o-mini) استعمال کرتا ہے جو خودکار طور پر فیصلہ کر سکتا ہے کہ کون سے کیلکولیٹر ٹولز استعمال کرنے ہیں:

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

**یہ کیا کرتا ہے:**
1. **ایک اے آئی ماڈل کنکشن بناتا ہے** آپ کے گٹ ہب ٹوکن کا استعمال کرتے ہوئے
2. **اے آئی کو کنیکٹ کرتا ہے** ہمارے کیلکولیٹر ایم سی پی سرور سے
3. **اے آئی کو رسائی دیتا ہے** ہمارے تمام کیلکولیٹر ٹولز تک
4. **قدرتی زبان کی درخواستوں کو اجازت دیتا ہے** جیسے "24.5 اور 17.3 کا مجموعہ نکالو"

**اے آئی خودکار طور پر:**
- سمجھتا ہے کہ آپ نمبروں کو جمع کرنا چاہتے ہیں
- `add` ٹول منتخب کرتا ہے
- `add(24.5, 17.3)` کال کرتا ہے
- نتیجہ قدرتی جواب میں واپس کرتا ہے

## مثالیں چلانا

### مرحلہ 1: کیلکولیٹر سرور شروع کریں

پہلے، اپنا گٹ ہب ٹوکن سیٹ کریں (اے آئی کلائنٹ کے لیے ضروری):

**ونڈوز:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**لینکس/میک او ایس:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

سرور شروع کریں:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

سرور `http://localhost:8080` پر شروع ہو جائے گا۔ آپ کو یہ نظر آئے گا:
```
Started McpServerApplication in X.XXX seconds
```

### مرحلہ 2: ڈائریکٹ کلائنٹ کے ساتھ ٹیسٹ کریں

ایک نئے ٹرمینل میں:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

آپ کو ایسا آؤٹ پٹ نظر آئے گا:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### مرحلہ 3: اے آئی کلائنٹ کے ساتھ ٹیسٹ کریں

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

آپ دیکھیں گے کہ اے آئی خودکار طور پر ٹولز استعمال کر رہا ہے:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## یہ سب کیسے ایک ساتھ کام کرتا ہے

جب آپ اے آئی سے پوچھتے ہیں "5 + 3 کیا ہے؟"، تو مکمل عمل یہ ہوتا ہے:

1. **آپ** اے آئی سے قدرتی زبان میں سوال کرتے ہیں
2. **اے آئی** آپ کی درخواست کا تجزیہ کرتا ہے اور سمجھتا ہے کہ آپ جمع کرنا چاہتے ہیں
3. **اے آئی** ایم سی پی سرور کو کال کرتا ہے: `add(5.0, 3.0)`
4. **کیلکولیٹر سروس** انجام دیتی ہے: `5.0 + 3.0 = 8.0`
5. **کیلکولیٹر سروس** واپس کرتی ہے: `"5.00 + 3.00 = 8.00"`
6. **اے آئی** نتیجہ وصول کرتا ہے اور قدرتی جواب میں فارمیٹ کرتا ہے
7. **آپ** کو جواب ملتا ہے: "5 اور 3 کا مجموعہ 8 ہے"

## اگلے مراحل

مزید مثالوں کے لیے، دیکھیں [باب 04: عملی نمونے](../../README.md)

**ڈسکلیمر**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کا استعمال کرتے ہوئے ترجمہ کی گئی ہے۔ ہم درستگی کے لیے کوشش کرتے ہیں، لیکن براہ کرم آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا غیر درستیاں ہو سکتی ہیں۔ اصل دستاویز کو اس کی اصل زبان میں مستند ذریعہ سمجھا جانا چاہیے۔ اہم معلومات کے لیے، پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ ہم اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کے ذمہ دار نہیں ہیں۔