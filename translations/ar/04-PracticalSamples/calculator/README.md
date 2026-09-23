# درس آلة حاسبة MCP للمبتدئين

## جدول المحتويات

- [ما ستتعلمه](#ما-ستتعلمه)
- [المتطلبات المسبقة](#المتطلبات-المسبقة)
- [إصدارات التبعيات](#إصدارات-التبعيات)
- [فهم هيكل المشروع](#فهم-هيكل-المشروع)
- [شرح المكونات الأساسية](#شرح-المكونات-الأساسية)
  - [1. التطبيق الرئيسي](#1-التطبيق-الرئيسي)
  - [2. خدمة الآلة الحاسبة](#2-خدمة-الآلة-الحاسبة)
  - [3. عميل MCP مباشرة](#3-عميل-mcp-مباشرة)
  - [4. عميل مدعوم بالذكاء الاصطناعي](#4-عميل-مدعوم-بالذكاء-الاصطناعي)
- [تشغيل الأمثلة](#تشغيل-الأمثلة)
- [اختبارات غير متصلة بالإنترنت](#اختبارات-غير-متصلة-بالإنترنت)
- [كيف يعمل كل شيء معًا](#كيف-يعمل-كل-شيء-معًا)
- [الخطوات التالية](#الخطوات-التالية)

## ما ستتعلمه

يشرح هذا الدرس كيفية بناء خدمة آلة حاسبة باستخدام بروتوكول نموذج السياق (MCP). ستفهم:

- كيفية إنشاء خدمة يمكن للذكاء الاصطناعي استخدامها كأداة
- كيفية إعداد الاتصال المباشر مع خدمات MCP
- كيف يمكن لنماذج الذكاء الاصطناعي اختيار الأدوات التي ستستخدمها تلقائيًا
- الفرق بين المكالمات المباشرة للبروتوكول والتفاعلات المدعومة بالذكاء الاصطناعي

## المتطلبات المسبقة

قبل البدء، تأكد من أن لديك:
- تثبيت Java 21 أو أعلى
- Maven لإدارة التبعيات
- فهم أساسي لـ Java و Spring Boot

فقط عملاء الذكاء الاصطناعي يتطلبون نشر Azure OpenAI و`DefaultAzureCredential` مصادق عليه،
مثل تسجيل دخول Azure CLI المحلي الموجود أو هوية مُدارة في Azure. الهوية تحتاج
إلى دور مستخدم Cognitive Services OpenAI على المورد. انظر [الفصل 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
الخادم وعميل SDK المباشر وجميع الاختبارات الآلية لا تحتاج إلى حساب Azure أو وصول للنموذج.

## إصدارات التبعيات

التبعيات المصدرة التي تم التحقق منها في 2026-09-14:

| التبعية | الإصدار |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (مدار بواسطة Spring AI) | 2.0.0 |
| LangChain4j / الأساسية | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| محول OpenAI الرسمي لـ LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (مدار بواسطة Boot) | 6.0.3 |

MCP ومحولات OpenAI الرسمية هي إصدارات بيتا منشورة في Maven Central، وليست لقطات.
إصداراتها تختلف عن جوهر LangChain4j. لا حاجة لمستودعات لقطات أو مراحل.
التبعيات الخاصة بالعميل فقط لها نطاق اختبار لأن الأمثلة القابلة للتشغيل موجودة تحت `src/test/java`.

## فهم هيكل المشروع

يحتوي مشروع الآلة الحاسبة على عدة ملفات مهمة:

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

## شرح المكونات الأساسية

### 1. التطبيق الرئيسي

**الملف:** `McpServerApplication.java`

هذه هي نقطة الدخول لخدمة الآلة الحاسبة الخاصة بنا. هو تطبيق Spring Boot قياسي مع إضافة خاصة واحدة:

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

**ما يفعله هذا:**
- يبدأ خادم ويب Spring Boot على المنفذ 8080
- ينشئ موفر استدعاء أدوات `ToolCallbackProvider` يجعل طرق الآلة الحاسبة متاحة كأدوات MCP
- التعليق `@Bean` يخبر Spring بإدارة هذا كمكون يمكن لأجزاء أخرى استخدامه

### 2. خدمة الآلة الحاسبة

**الملف:** `CalculatorService.java`

هنا يتم كل الحسابات. كل طريقة معلمة بالتعليق `@Tool` لجعلها متاحة عبر MCP:

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
    
    // المزيد من عمليات الآلة الحاسبة...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**الميزات الرئيسية:**

1. **تعليق `@Tool`**: يخبر MCP أن هذه الطريقة يمكن استدعاؤها من عملاء خارجيين
2. **وصف واضح**: كل أداة لها وصف يساعد نماذج الذكاء الاصطناعي على فهم متى تستخدمها
3. **تنسيق إرجاع متسق**: كل العمليات تعيد سلاسل قابلة للقراءة البشرية مثل "5.00 + 3.00 = 8.00"
4. **معالجة الأخطاء**: القسمة على الصفر والجذور التربيعية السالبة ترجع رسائل خطأ

**العمليات المتاحة:**
- `add(a, b)` - يجمع رقمين
- `subtract(a, b)` - يطرح الثاني من الأول
- `multiply(a, b)` - يضرب رقمين
- `divide(a, b)` - يقسم الأول على الثاني (مع التحقق من الصفر)
- `power(base, exponent)` - يرفع الأساس لقوة الأس
- `squareRoot(number)` - يحسب الجذر التربيعي (مع التحقق من السالب)
- `modulus(a, b)` - يرجع باقي القسمة
- `absolute(number)` - يرجع القيمة المطلقة
- `help()` - يرجع معلومات عن جميع العمليات

### 3. عميل MCP مباشرة

انظر [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

يستخدم هذا العميل `HttpClientStreamableHttpTransport` عند `/mcp`، يهيئ الاتصال،
يرسل اختبار اتصال للخادم، ويتبع ترقيم صفحات قائمة الأدوات. يتأكد من وجود كل الأدوات التسع المتوقعة
ويستدعي كل منها، بما في ذلك `modulus` و`help`، دون نموذج ذكاء اصطناعي.

بناء طلب الاستدعاء الحالي يبدو كالتالي:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

أخطاء البروتوكول تؤدي إلى فشل العميل بدلًا من طباعة نجاح مضلل. يتم إغلاق عميل MCP باستخدام try-with-resources،
حتى عند فشل الاكتشاف أو استدعاء الأداة.

### 4. عميل مدعوم بالذكاء الاصطناعي

انظر [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
و[Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` ينفذ API الحالي لنموذج المحادثة في LangChain4j.
`StreamableHttpMcpTransport` يربطه بنقطة نهاية `/mcp` نفسها مثل عميل SDK.
`AiServices` يكتشف الأدوات ويدير محادثة استدعاء الأداة / النتيجة.

النشر الافتراضي هو **GPT-5.6 Luna**، مع تعطيل التفكير بشكل صريح:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

هذه الإعدادات الافتراضية تنطبق على كل إكمال، بما في ذلك المتابعات بعد تنفيذ الأداة.
يستخدم العميل `BearerTokenCredential` قابل للتحديث بدعم من `DefaultAzureCredential`
ونطاق `https://ai.azure.com/.default`، وليس رمزًا مميزًا لجلسة واحدة يمرر كمفتاح API.
تُقبل كل من عناوين URL الخاصة بالموارد وعناوين URL التي تنتهي بالفعل بـ `/openai/v1`.

يحتفظ البوت بتاريخ محادثة محدود، ويطبع `Tool executed: ...` مع نتيجة MCP الفعلية،
ويفشل إذا تخطى الرد الأدوات. تقتصر حلقات الأدوات على أربع جولات ذهابًا وإيابًا.
تنتقل أخطاء المصادقة والنموذج وMCP والأدوات؛ تم تعطيل إعادة المحاولة التلقائية للنموذج.
يتم إغلاق كل من ناقل ومستخدم MCP والعميل الرسمي لـ OpenAI عند النجاح أو الفشل.

## تشغيل الأمثلة

### الخطوة 1: بدء خادم الآلة الحاسبة

لا حاجة لتكوين Azure للخادم. تنفذ الأوامر أدناه من دليل هذا المثال.
المثال يستخدم المنفذ **18081** لتجنب التعارض مع مثال آخر؛ الافتراضي يظل 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

نقطة نهاية MCP هي `http://localhost:18081/mcp`. معلومات الصحة والاكتشاف موجودة في
`http://localhost:18081/health` و `http://localhost:18081/info`.
HTTP قابلة للبث تحل محل النقل القديم الذي كان يعتمد فقط على SSE؛ `/sse` و `/v1/tools` ليست نقاط نهاية.

### الخطوة 2: اختبار باستخدام العميل المباشر

في نافذة PowerShell أخرى:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

لا حاجة لإدخال. جميع الأدوات التسع يتم اختبارها. النتائج الحسابية المتوقعة تشمل
8، 6، 42، 5، 256، 4، 2، و5.5، تتبعها نص المساعدة.

### الخطوة 3: اختبار باستخدام عميل الذكاء الاصطناعي

بعد المصادقة كما هو موضح في المتطلبات المسبقة، قم بتكوين عميل الذكاء الاصطناعي في نفس النافذة:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

توقع وجود سطر `Tool executed: add` مع النتيجة `41.80`، تليه إجابة النموذج.
وضع الموجه الواحد يخرج دون انتظار إدخال. لتشغيل العرض التوضيحي الأصلي لأربع موجهات:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

ينفذ العرض التوضيحي `add`، `squareRoot`، `help`، ثم سلسلة العمليات `power` و`divide`.
الإجابات العددية المتوقعة هي 41.8، 12، و64. تجاهل الوسائط ينفذ هذا العرض التوضيحي أيضًا.

### الخطوة 4: تشغيل بوت تفاعلي

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

أدخل `Multiply 6 by 7 using the calculator service`، ثم أكتب `exit` أو `quit`.
توقع نتيجة فعلية للأداة `multiply` بقيمة 42. الأسطر الفارغة تُتجاهل؛ نهاية الملف (EOF) تنهي الجلسة أيضًا.
لاختبار غير تفاعلي لهذه نقطة الدخول:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

كلا نقطتي دخول AI تقبلان `--prompt "question"`، `--demo`، و`--interactive`.
الخيارات غير الصالحة تؤدي إلى فشل قبل فتح الاتصال. كل معلمة Maven `-D...` تُقتبس بالكامل لـ PowerShell.
في Bash، استخدم `export NAME=value` بدلاً من `$env:NAME = "value"`.

**الكمية:** شغل عينات AI بالتتابع. عادةً يحتاج الموجه البسيط إلى طلبين للنموذج؛
العرض التوضيحي الكامل يحتاج تسعة، بما في ذلك المتابعات لنتائج الأدوات. مع نشر 10 RPM مشترك،
انتظر نافذة كمية جديدة قبل تشغيل AI مرة أخرى. الفشل برمز 429 واضح دون
إعادة المحاولة التلقائية؛ اتبع إرشادات الخدمة الخاصة بإعادة المحاولة. عدد الطلبات الفعلي يعتمد على النموذج.
لا تستهلك الاختبارات غير المتصلة بالإنترنت أي كمية ولا تضمن توفر Luna أو جودة الإجابة الحقيقية.

### التكوين والإيقاف

| الإعداد | الافتراضي / السلوك |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; عنوان URL الأساسي، بدون `/mcp` |
| `-Dmcp.server.url=...` | يتجاوز `MCP_SERVER_URL` لكل العملاء |
| `AZURE_OPENAI_ENDPOINT` | مطلوب فقط لعملاء الذكاء الاصطناعي؛ عنوان المورد أو عنوان URL من `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; اسم نشر Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; رقم صحيح إيجابي |
| جهد التفكير | دائما `none`، بما في ذلك المتابعات في حلقات الأدوات |

يجب أن يدعم النشر المتجاوز `reasoning_effort=none` و`max_completion_tokens`.
لا يقرأ العملاء ملف `.env` تلقائياً. أوقف الخادم باستخدام `Ctrl+C` بعد الاختبار.
يعود العملاء بشكل طبيعي دون `System.exit` أو تأخيرات إيقاف.

## اختبارات غير متصلة بالإنترنت

```powershell
mvn -B -ntp clean verify
```

جميع الاختبارات غير متصلة بالإنترنت بالنسبة لـ Azure: تبدأ مجموعة البروتوكول خادم Spring و
نموذجًا متوافقًا مع OpenAI على منافذ loopback عشوائية، ثم تغلقها. قد يحتاج Maven بعد إلى
تحميل التبعيات. لا تُستخدم بيانات اعتماد، نشر مباشر، أو خادم MCP موجود مسبقًا.

- تغطي اختبارات وحدة الآلة الحاسبة كل العمليات الحسابية، النتائج العشرية، المساعدة، وأخطاء النطاق.
- تغطي اختبارات MCP التهيئة والاكتشاف وكل المكالمات التسع للأدوات، وأخطاء الأدوات، والصحة والمعلومات.
- تنفذ اختبارات بروتوكول AI العرض التوضيحي الكامل والروبوت التفاعلي ضد الآلة الحاسبة الحقيقية،
  وتتحقق من تغذية نتائج الأدوات للإكمال التالي، وتفحص كل جسم HTTP لـ Luna،
  `reasoning_effort: "none"`, و `max_completion_tokens` بدون `max_tokens` قديم.
- تغطي اختبارات التكوين/الإدخال تجاوزات النشر ونقاط النهاية، الأسطر الفارغة، نهاية الملف، الأمر خروج/quit،
  وضع الموجه الواحد، الخيارات غير الصالحة، وانتشار الأخطاء. تثبت اختبارات الحصة أن 429 لا يُعاد المحاولة.

## كيف يعمل كل شيء معًا

إليك التدفق الكامل عندما تسأل الذكاء الاصطناعي "ما هو 5 + 3؟":

1. **أنت** تسأل الذكاء الاصطناعي بلغة طبيعية
2. **الذكاء الاصطناعي** يحلل طلبك ويدرك أنك تريد الجمع
3. **الذكاء الاصطناعي** يستدعي خادم MCP: `add(5.0, 3.0)`
4. **خدمة الآلة الحاسبة** تنفذ: `5.0 + 3.0 = 8.0`
5. **خدمة الآلة الحاسبة** تعيد: `"5.00 + 3.00 = 8.00"`
6. **الذكاء الاصطناعي** يستلم النتيجة ويصيغ ردًا طبيعيًا
7. **أنت** تحصل على: "مجموع 5 و3 هو 8"

## الخطوات التالية

لمزيد من الأمثلة، انظر [الفصل 04: عينات عملية](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->