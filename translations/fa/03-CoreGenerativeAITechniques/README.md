# آموزش تکنیک‌های پایه‌ای هوش مصنوعی مولد

## فهرست مطالب

- [پیش‌نیازها](#پیش‌نیازها)
- [شروع کار](#شروع-کار)
- [راهنمای انتخاب مدل](#راهنمای-انتخاب-مدل)
- [آموزش ۱: تکمیل‌های LLM و چت](#آموزش-1-تکمیل‌های-llm-و-چت)
- [آموزش ۲: فراخوانی تابع](#آموزش-۲-فراخوانی-تابع)
- [آموزش ۳: RAG (تولید تقویت‌شده با بازیابی)](#آموزش-۳-rag-تولید-تقویت‌شده-با-بازیابی)
- [آموزش ۴: هوش مصنوعی مسئولانه](#آموزش-۴-هوش-مصنوعی-مسئولانه)
- [الگوهای رایج در مثال‌ها](#الگوهای-رایج-در-مثال‌ها)
- [تست‌های واحد](#تست‌های-واحد)
- [تأیید زنده‌ی متوالی](#تأیید-زنده-متوالی)
- [رفع اشکال](#رفع-اشکال)
- [گام‌های بعدی](#مراحل-بعدی)

## مرور کلی

چهار برنامه جداگانه جاوا چت، تاریخچه مکالمه، فراخوانی توابع، تولید تقویت‌شده با بازیابی کل سند (RAG) و مدیریت پاسخ هوش مصنوعی مسئولانه را نشان می‌دهند. به طور پیش‌فرض تمام درخواست‌های چت به هدف **GPT-5.6 Luna با تلاش استدلالی `none`** ارسال می‌شوند.

این مثال‌ها از SDK رسمی جاوای OpenAI با نقطه پایانی v1 Azure OpenAI، مطابق با [راهنمای SDK مایکروسافت](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) استفاده می‌کنند. بسته قدیمی `azure-ai-openai` دیگر یک وابستگی نیست. چت کامپلشنز حفظ شده تا فرایندهای مبتنی بر پیام موجود آموزش داده شود؛ برای گزینه‌های دیگر API به [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) مراجعه کنید.

## پیش‌نیازها

- جاوا نسخه ۲۱ یا بالاتر و Maven نسخه 3.6.3 یا بالاتر.
- یک استقرار چت Azure OpenAI به نام `gpt-5.6-luna` یا جایگزین با تنظیمات چت کامپلشنز سازگار.
- یک هویت Azure وارد شده که نقش **کاربر خدمات شناختی OpenAI** را روی منبع دارد. توسعه محلی از ورود به سیستم Azure CLI شما استفاده می‌کند؛ برنامه‌های میزبانی شده می‌توانند از هویت مدیریت شده بهره‌مند شوند.
- برای دستورالعمل‌های راه‌اندازی منبع و ورود به سیستم به [فصل ۲](../02-SetupDevEnvironment/getting-started-azure-openai.md) مراجعه کنید.

[پیکربندی Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) این نسخه‌ها را که در تاریخ ۲۰۲۶-۰۹-۱۴ بررسی شده، قفل می‌کند:

| مؤلفه | نسخه | کاربرد |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | کلاینت رسمی سازگار با Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | احراز هویت بدون کلید و تازه‌سازی توکن |
| `net.objecthunter:exp4j` | 0.4.8 | تجزیه عبارات ریاضی بدون اجرای کد |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | تست‌های واحد Jupiter آفلاین |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | کامپایل جاوا ۲۱، تست‌ها، مثال‌های قابل اجرای |

کامپایلر از گزینه `--release 21` استفاده می‌کند. هیچ وابستگی به Spring Boot، Spring AI یا LangChain4j توسط این مثال‌های مستقل نیاز نیست.

## شروع کار

از ریشه مخزن، نقطه پایانی منبع و جایگزین استقرار اختیاری را در شل خود تنظیم کنید.

**PowerShell ویندوز:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**لینوکس/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

تست‌ها به احراز هویت Azure یا نقطه پایانی نیاز ندارند. Maven به طور خودکار فایل محیطی را نمی‌خواند؛ متغیرها را در شلی که برای اجرای مثال‌های زنده استفاده می‌کنید تنظیم کنید. برای اجرای IDE، محیط ارائه شده توسط پیکربندی راه‌انداز خود را بررسی کنید.

## راهنمای انتخاب مدل

| متغیر محیطی | معنی | پیش‌فرض |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | ریشه منبع Azure از نوع HTTPS یا URL `/openai/v1` که قبلاً نرمال‌سازی شده | برای اجراهای زنده الزامی است |
| `AZURE_OPENAI_DEPLOYMENT` | نام استقرار چت، نه نسخه مدل | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | پیکربندی استقرار جداگانه برای جاسازی، توسط این چهار برنامه استفاده نمی‌شود | `text-embedding-3-small` |

جایگزینی‌های استقرار خالی از پیش‌فرض‌ها استفاده می‌کنند. پیکربندی دقیقاً یک بار `/openai/v1` را می‌افزاید و اعتبارنامه‌ها، رشته‌های پرس‌وجو و مسیرهای استقرار قدیمی را در نقطه پایانی رد می‌کند.

هر درخواست چت به صراحت `reasoningEffort(ReasoningEffort.NONE)` و `maxCompletionTokens(...)` را تنظیم می‌کند. هیچ درخواستی `temperature`، `top_p` یا گزینه توکن تکمیل قدیمی را تعیین نمی‌کند. این شامل انتخاب ابزار و پیگیری‌های نتایج ابزار می‌شود. ابزارهای توابع چت کامپلشنز GPT-5.6 نیاز به تلاش استدلالی `none` دارند؛ به [راهنمای چت مایکروسافت](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt) مراجعه کنید.

**در این فصل هیچ نقطه ورودی استریمینگ یا جاسازی وجود ندارد.** خواننده کل سند را بازیابی می‌کند، نه بردارها را. اگر بخواهید با جاسازی‌ها توسعه دهید، از استقرار جاسازی جداگانه مانند `text-embedding-3-small` استفاده کنید، هرگز Luna را.

## آموزش 1: تکمیل‌های LLM و چت

منبع: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

برنامه یک توضیح ساده با جاوا استریم، مکالمه دو دور HashMap/TreeMap و چت تعاملی اجرا می‌کند. دور دوم شامل پاسخ اولین دستیار است؛ هر نوبت تعاملی همچنین مکالمه قبلی خود را ارسال می‌کند.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` استقرار و تنظیم استدلال صریح را تأمین می‌کند. چت تعاملی خطوط خالی را رد می‌کند، با ورودی `exit` یا EOF خاتمه می‌یابد و پیام سیستم به همراه نه دور کامل کاربر/دستیار را نگه می‌دارد. محدودیت شمارش نوبت یک حد آموزشی است، نه تضمینی بر بودجه توکن دقیق.

از دایرکتوری examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

انتظار سه پاسخ اولیه را داشته باشید، سپس نشانگر `You:` ظاهر می‌شود. هر سؤال تعاملی غیرخالی، یک درخواست اضافه می‌کند. محدودیت‌های تکمیل به ترتیب ۲۰۰، ۳۰۰، ۴۰۰ و سپس در هر نوبت تعاملی ۵۰۰ توکن است.

## آموزش ۲: فراخوانی تابع

منبع: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK اسکیمای JSON را از رکوردهای نشانه‌دار `WeatherArguments` و `CalculationArguments` استخراج می‌کند. انتخاب اجباری ابزار باعث می‌شود هر مثال پروتکل ابزار را تمرین کند به جای پذیرش پاسخ بدون کمک مدل.

۱. سوالی را با ابزار مجاز، تلاش استدلالی `none` و محدودیت تکمیل ۳۰۰ توکن ارسال کنید.
۲. دلیل پایان `tool_calls` را درخواست کنید، نام تابع و شناسه‌های تماس را اعتبارسنجی کرده و آرگومان‌های JSON تایپ‌شده را تجزیه نمایید.
۳. تابع محلی را اجرا کنید. مدل کد جاوا یا دلخواهی را اجرا نمی‌کند.
۴. پیام فراخوانی ابزار دستیار را یک بار اضافه کنید، سپس هر نتیجه‌ای با شناسه `tool_call_id` مربوطه دنبال شود.
۵. یک درخواست نهایی ۳۰۰ توکن بدون ابزار ارسال کرده و پاسخ کامل و غیرخالی را درخواست کنید.

`get_weather` وضعیت آب‌وهوای **شبیه‌سازی‌شده** را برمی‌گرداند، نه زنده. به شهر احترام می‌گذارد و دمای نمونه ۲۲ درجه سانتی‌گراد را وقتی خواسته شود به فارنهایت تبدیل می‌کند. `calculate` عبارت داده شده را با exp4j ارزیابی می‌کند، ساختارهایی مانند `15% of 240` و `2 + 3 * 4` را پشتیبانی می‌کند و محاسبات خالی، بیش‌حد، نامعتبر یا نامتناهی را رد می‌کند. این محاسبات از حساب شناور استفاده می‌کند، نه دقت اعشاری مالی.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

انتظار داشته باشید `Function: get_weather`، آب‌وهوای شبیه‌سازی‌شده سیاتل، `Function: calculate`، `Function result: 36` و دو پاسخ نهایی را ببینید. به ورودی استاندارد یا اعتبارنامه آب‌وهوا نیازی نیست. اجرای موفق دقیقاً از چهار درخواست چت استفاده می‌کند.

## آموزش ۳: RAG (تولید تقویت‌شده با بازیابی)

منبع: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). ورودی: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

این مثال مقدماتی RAG یک سند UTF-8 کامل را بازیابی می‌کند و آن را همراه با سوال در پیام کاربر می‌فرستد. یک پیام سیستم جداگانه به مدل دستور می‌دهد محتوای سند را داده غیرقابل‌اعتماد تلقی کرده و فقط از همان متن پاسخ دهد. اگر سند جواب را نداشته باشد، پاسخ درخواستی این است: `I cannot find that information in the provided document.`

بنیادگذاری می‌تواند توهمات را کاهش دهد، ولی نه مرزبندی‌ها و نه دستورالعمل‌های سیستم دقت را تضمین یا از هر تزریق درخواست جلوگیری نمی‌کنند. پاسخ‌های زنده را مرور کنید. RAG تولید در تولید عادی معمولاً شامل تقسیم‌بندی، بازیابی، استنادات، کنترل دسترسی و ارزیابی می‌شود.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

یک سوال وارد کنید، مثلاً `Which authentication method does the document describe?`. انتظار داشته باشید جوابی با نام Microsoft Entra ID دریافت کنید. برنامه پس از یک درخواست چت با محدودیت تکمیل ۵۰۰ توکن خارج می‌شود.

جستجوی فایل پیش‌فرض از ریشه مخزن، دایرکتوری فصل یا دایرکتوری examples کار می‌کند. مسیر صریح نیز پشتیبانی می‌شود:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

ورودی‌ها باید غیرخالی باشند: حداکثر ۳۲ کیلوبایت داده سند UTF-8 و ۲۰۰۰ کاراکتر سوال. فایل‌های گمشده، سوالات خالی/EOF و ورودی‌های بیش‌حد قبل از استنتاج شکست می‌خورند.

## آموزش ۴: هوش مصنوعی مسئولانه

منبع: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

شش تست پوشش‌دهنده دستورهای مضر، سخنان نفرت، حریم خصوصی، اطلاعات نادرست پزشکی، محتوای غیرقانونی و یک سوال مسئولانه محافظه‌کار هستند. برنامه پاسخ را مشاهده می‌کند به جای این‌که فرض کند هر تست باید یک فیلتر فعال کند.

| نتیجه | مدرک |
| --- | --- |
| `FILTERED` | کد خطای صریح `content_filter` / `ResponsibleAIPolicyViolation`، یا دلیل پایان تکمیل `content_filter` |
| `REFUSED` | فیلد ساخت‌یافته غیرخالی `message.refusal` |
| `POSSIBLE_REFUSAL` | عبارت آغازین امتناع در متن عادی؛ یک قاعده تجربی نیازمند بررسی |
| `GENERATED` | پاسخ کامل و غیرخالی؛ نه دلیل برای ایمنی محتوا |

یک خطای HTTP ۴۰۰ عادی **دلیل فیلتر شدن نیست**. پارامترهای نامعتبر، شکست‌های احراز هویت، محدودیت نرخ، خطاهای سرور، پاسخ‌های نادرست قالب و خروجی قطع‌شده باعث شکست اجرا می‌شوند نه موفقیت کاذب ایمنی. کلمات کلی مانند «محتوای مضر» در توضیح محافظه‌کارانه به عنوان امتناع حساب نمی‌شوند.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

انتظار شش نتیجه دسته‌بندی و خلاصه‌ای که می‌گوید این مشاهدات تایید ایمنی نیستند را داشته باشید. هر تست محدودیت تکمیل ۳۰۰ توکن دارد. تولیدات غیرمنتظره و امتناع‌های ممکن را دستی بررسی کنید؛ مقایسه محافظه‌کارانه باید توضیح مسئولانه واقعی ارائه دهد. ورودی استاندارد لازم نیست.

## الگوهای رایج در مثال‌ها

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) مرکزیت‌بخش نرمال‌سازی نقطه پایانی، جایگزین‌های استقرار، احراز هویت بدون کلید و گزینه‌های چت است:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

تأمین‌کننده توکن دسترسی‌های لازم را به‌روزرسانی می‌کند. توکن‌ها را لاگ نکنید یا آن را با کلید API جایگزین نکنید. هر برنامه کلاینت خود را مجدداً استفاده کرده و در `finally` یا از طریق بسته‌بندی `AutoCloseable` خودش می‌بندد؛ `OpenAIClient` در SDK خود `AutoCloseable` نیست.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) به پاسخ متنی کامل و غیرخالی نیاز دارد. انتخاب‌های خالی، امتناع‌ها، فیلترها و پاسخ‌های کوتاه به عنوان موفقیت با سکوت چاپ نمی‌شوند. مثال هوش مصنوعی مسئولانه به طور صریح نتایج فیلتر/امتناع مورد انتظار را مدیریت می‌کند. شکست‌های بدون کنترل، فرآیند Java/Maven را با کد خروجی غیرصفر خاتمه می‌دهند.

**تلاش‌های خودکار SDK غیرفعال شده‌اند** تا تعداد درخواست‌ها در استقرارهای مشترک با RPM پایین پیش‌بینی‌پذیر بماند. هر درخواست استنتاج دارای تایم‌اوت ۶۰ ثانیه است. دریافت توکن ممکن است زمان بیشتری ببرد. برنامه‌ریز سطح برنامه باید سهمیه‌ها را رعایت کند؛ درخواست پولی ناموفق را کورکورانه دوباره اجرا نکنید.

## تست‌های واحد

از دایرکتوری examples:

```powershell
mvn -B -ntp clean test
```

ترنسپورت تست لایه HTTP SDK را به طور کامل جایگزین می‌کند، بدنه‌های واقعی سریال شده درخواست‌ها را ضبط می‌کند و پاسخ‌های صف‌بندی شده را تأمین می‌کند. این ترنسپورت هیچ ساکتی باز نمی‌کند، توکن Azure نمی‌گیرد و روی درخواست‌های غیرمنتظره شکست می‌خورد. این تست‌ها رفتار برنامه و پروتکل SDK را اعتبارسنجی می‌کنند، نه کیفیت مدل زنده یا در دسترس‌بودن استقرار.

| مجموعه تست | پوشش |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | نرمال‌سازی/رد نقطه پایانی، جایگزینی استقرار، استدلال و گزینه‌های توکن |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | همه جریان‌های تکمیل، تاریخچه پیام، کوتاه‌سازی نوبت کامل، EOF، شکست‌ها |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | اسکیمای ابزار، آرگومان‌های تایپ‌شده، حساب، شناسه‌ها، چند نتیجه ابزار، پیگیری‌های شکست‌خورده |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | جستجوی فایل، UTF-8، محدودیت اندازه، اطلاعات زمینه، خطاهای ورودی و API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | همه شش تست، فیلترهای صریح، طبقه‌بندی امتناع، HTTP 400 عادی و سایر شکست‌ها |

برای اجرای یک مجموعه، از `mvn -B -ntp test "-Dtest=FunctionsAppTest"` استفاده کنید. وسایل مشترک در [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) قرار دارند.

## تأیید زنده متوالی

تماس‌های زنده جدا از تست‌های واحد هستند. از دستورات زیر **به صورت جداگانه**، از ریشه مخزن، فقط بعد از آماده شدن دسترسی‌ها و اعتبارنامه‌ها استفاده کنید. نیازی به سرویس‌ها یا پردازش‌های پایدار نیست.

برای استقرار مشترک **۱۰ درخواست در دقیقه**، قبل از راه‌اندازی برنامه بعدی سهمیه کافی رزرو کنید: ۵، ۴، ۱، سپس ۶ درخواست. روندهای متوالی به تنهایی تضمین‌کننده رعایت محدودیت نرخ نیستند. دقیقه گردشی را با همه تماس‌گیرندگان دیگر هماهنگ کنید؛ چهار فراخوانی را به صورت یک دسته بدون فاصله نگذارید.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**۱. تکمیل‌ها، چند نوبتی، و دو نوبت تعاملی:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

تمام سه عنوان بخش‌ها، پنج پاسخ، یک پاسخ تعاملی نهایی که آدا را به یاد می‌آورد، `Goodbye!` و کد خروج ۰ را بررسی کنید. بودجه: **۵ درخواست، حداکثر ۱۹۰۰ توکن تکمیل**. برای اجرای کوچک‌تر، فقط `exit` را عبور دهید: ۳ درخواست / ۹۰۰ توکن، اما این اجرا استنتاج تعاملی را آزمایش نمی‌کند.

**۲. هر دو جریان کاری فراخوانی تابع:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

هر دو نام تابع، آب و هوای شبیه‌سازی شده سیاتل، نتیجه محاسبه شده ۳۶، دو پاسخ نهایی و کد خروج ۰ را بررسی کنید. بودجه: **۴ درخواست، حداکثر ۱۲۰۰ توکن تکمیل**.

**۳. پاسخ مبتنی بر سند:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

مسیر سند، پاسخی که به Microsoft Entra ID اشاره دارد، و کد خروج ۰ را بررسی کنید. بودجه: **۱ درخواست، حداکثر ۵۰۰ توکن تکمیل**. فایل ورودی مورد نیاز تنها [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) موجود است. اجرای اختیاری دوم که درباره موضوعی غایب سوال می‌پرسد باید خودداری کند و یک درخواست / ۵۰۰ توکن اضافه می‌کند.

**۴. مشاهدات مربوط به هوش مصنوعی مسئولانه:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

شش دسته و خلاصه مشاهدات را بررسی کنید، محتوای تولید شده را مرور کنید، و کد خروج ۰ را برای اتمام فنی الزامی بدانید. خروج موفقیت‌آمیز فرآیند مدل را تضمین نمی‌کند. بودجه: **۶ درخواست، حداکثر ۱۸۰۰ توکن تکمیل**.

**مجموع برای چهار دستور: ۱۶ درخواست چت و حداکثر ۵۴۰۰ توکن تکمیل**، به‌علاوه توکن‌های ورودی (شامل تکرار گفتگو و تاریخچه / طرح‌واره ابزارها). هیچ درخواست جاسازی وجود ندارد. استفاده واقعی از توکن به مدل بستگی دارد و ممکن است کمتر باشد، به ویژه برای پرامپت‌های فیلتر شده. هزینه دلاری به قیمت‌گذاری استقرار بستگی دارد؛ هیچ برآورد مالی قطعی ارائه نمی‌شود. همه محدودیت‌های درخواست فرض می‌کنند هیچ اجرای دستی مجددی انجام نشده است. بلافاصله پس از هر دستور `$LASTEXITCODE` را بررسی کنید؛ مقدار غیر صفر به معنی عدم اتمام موفقیت‌آمیز اجرا است.

## رفع اشکال

- **عدم وجود نقطه پایان / ۴۰۱ / ۴۰۳:** نقطه پایان را در فرایند راه‌اندازی تنظیم کنید، ورود به حساب محلی آزور و نقش منابع را بررسی کنید، و از نبود بازنویسی‌های ناخواسته‌ محیط هویتی اطمینان حاصل کنید.
- **۴۰۰ / ۴۰۴:** اطمینان حاصل کنید که استقرار وجود دارد و از تکمیل‌های چت با تلاش استنتاج `none` پشتیبانی می‌کند. از ریشه منبع HTTPS یا URL `/openai/v1` استفاده کنید، نه URL استقرار قدیمی. خطاهای عادی ۴۰۰ شکست‌های فنی هستند، نه مانع‌های ایمنی.
- **۴۲۹:** سهمیه RPM و توکن اشتراکی را قبل از تلاش مجدد هماهنگ کنید. نمونه‌ها عمداً خودکار تلاش مجدد نمی‌کنند.
- **`Incomplete chat response: length`:** خروجی به حد تکمیل رسید. پاسخ و پرامپت را مرور کنید قبل از افزایش حد و بودجه مستند شده آن؛ اجرای کوتاه شده را موفق ندانید.
- **خطاهای فایل یا stdin:** از دایرکتوری پشتیبانی شده اجرا کنید یا مسیر سند صریح بدهید. یک سوال خواننده غیرخالی ارائه دهید. تکمیل‌ها می‌توانند در EOF یا `exit` به صورت نرمال پایان یابند.
- **خطاهای کامپایل:** جاوا ۲۱ یا بالاتر را تأیید کنید، سپس `mvn -B -ntp clean test` را اجرا کنید. در پاورشل، کل آرگومان Maven شامل خاصیت نقطه‌دار را نقل قول کنید، مثلاً `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## مراحل بعدی

ادامه به [فصل ۴: نمونه‌های عملی](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوء تفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->