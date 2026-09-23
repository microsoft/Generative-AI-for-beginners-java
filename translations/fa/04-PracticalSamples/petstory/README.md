# آموزش ساخت داستان حیوانات خانگی برای مبتدیان

یک عکس حیوان خانگی آپلود کنید، آن را با GPT-5.6 Luna تحلیل کنید، و از شرح به‌دست‌آمده داستانی تولید کنید. هر دو درخواست مدل از `reasoning_effort: none` استفاده می‌کنند.

| مؤلفه | نسخه |
| --- | --- |
| جاوا | نسخه ۲۱ یا بالاتر |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## فهرست مطالب

- [پیش‌نیازها](#پیش‌نیازها)
- [درک ساختار پروژه](#درک-ساختار-پروژه)
- [توضیح مؤلفه‌های اصلی](#توضیح-مؤلفه‌های-اصلی)
  - [1. برنامه اصلی](#1-برنامه-اصلی)
  - [2. کنترلر وب](#2-کنترلر-وب)
  - [3. سرویس داستان](#3-سرویس-داستان)
  - [4. قالب‌های وب](#4-قالب‌های-وب)
  - [5. پیکربندی](#5-پیکربندی)
- [اجرای برنامه](#اجرای-برنامه)
- [تست‌های آفلاین](#تست‌های-آفلاین)
- [چگونگی عملکرد کلی](#چگونگی-عملکرد-کلی)
- [درک ادغام هوش مصنوعی](#درک-ادغام-هوش-مصنوعی)
- [گام‌های بعدی](#گام‌های-بعدی)

## پیش‌نیازها

پیش از شروع مطمئن شوید که:
- جاوای نسخه ۲۱ یا بالاتر نصب‌شده است
- Maven برای مدیریت وابستگی‌ها
- یک استقرار Azure AI Foundry از GPT-5.6 Luna به نام `gpt-5.6-luna`، یا جایگزین `AZURE_OPENAI_DEPLOYMENT` که به آن استقرار اشاره دارد. بخش [فصل ۲](../../02-SetupDevEnvironment/getting-started-azure-openai.md) را برای فراهم‌سازی ببینید و با `az login` برای احراز هویت بدون کلید وارد شوید. استقرار باید ورودی تصویر و `reasoning_effort: none` پشتیبانی کند.
- درک پایه‌ای از جاوا، Spring Boot و توسعه وب

## درک ساختار پروژه

پروژه داستان حیوانات خانگی چندین فایل مهم دارد:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## توضیح مؤلفه‌های اصلی

### 1. برنامه اصلی

**فایل:** `PetStoryApplication.java`

این نقطه ورود برنامه Spring Boot ما است:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**این کارها را انجام می‌دهد:**
- توضیحات `@SpringBootApplication` فعال‌سازی خودکار تنظیمات و اسکن مؤلفه‌ها
- راه‌اندازی یک وب سرور تعبیه‌شده (Tomcat) روی پورت 8080
- ایجاد خودکار تمام beans و سرویس‌های مورد نیاز Spring

### 2. کنترلر وب

**فایل:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| نقطه پایانی | درخواست | پاسخ موفق |
| --- | --- | --- |
| `GET /` | بدون بدنه | فرم آپلود HTML با توکن CSRF |
| `POST /analyze-image` | `multipart/form-data`، فیلد فایل `image` | JSON: `{"description":"حیوان بازیگوش..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`، فیلد `description` | صفحه نتیجه HTML با شرح و داستان تولیدشده |

هر دو نقطه پایانی POST نیاز به کوکی جلسه و توکن CSRF گرفته‌شده از `GET /` دارند. اسکریپت آپلود مقدار مخفی `_csrf` را در هدر `X-CSRF-TOKEN` ارسال می‌کند؛ ارسال داستان آن را به صورت فیلد فرم `_csrf` می‌فرستد. کلاینت‌های API باید کوکی را بین درخواست‌ها حفظ کنند. این‌ها نقاط پایانی فرم هستند، نه درخواست‌های JSON.

شرح‌ها باید غیرخالی و حداکثر ۱۰۰۰ کاراکتر باشند. کنترلر شرح را کوتاه می‌کند و `<`, `>`, کوتیشن دوتایی، آپاستروف و `&` را حذف می‌کند قبل از ارسال به سرویس. قالب نتیجه خروجی مدل را با `th:text` فراردهی می‌کند.

خطاهای اعتبارسنجی تصویر HTTP 400 با فیلد `error` برمی‌گردانند؛ خطاهای مدل HTTP 502 با فیلد `error` و بدون `description` برمی‌گردند. شرح‌های داستان نامعتبر یا خطاهای مدل به `/` و با خطای قابل مشاهده ریدایرکت می‌شوند. فیلدهای ضروری گمشده HTTP 400 و توکن‌های CSRF مفقود یا نامعتبر HTTP 403 می‌دهند. شرح‌ها یا داستان‌های پیش‌فرض به‌عنوان نتایج AI موفق عرضه نمی‌شوند.

### 3. سرویس داستان

**فایل:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

SDK رسمی OpenAI جاوا 4.63.1 درخواست‌های Chat Completions سازگار با OpenAI از Azure AI Foundry را صدا می‌زند. Azure Identity نسخه 1.18.6، توکن مایکروسافت Entra را از طریق `DefaultAzureCredential` فراهم می‌کند؛ نیازی به کلید API نیست.

| عملیات | ورودی | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | بایت‌های تصویر کدگذاری‌شده به عنوان یک URL داده base64 با نوع MIME آپلود شده | 300 |
| `generateStory` | شرح حیوان خانگی در یک پیام کاربر | 800 |

هر دو درخواست از استقرار پیکربندی‌شده استفاده می‌کنند که به‌طور پیش‌فرض `gpt-5.6-luna` است، و به صراحت `ReasoningEffort.NONE` (`reasoning_effort: none`) را تنظیم می‌کنند. هیچکدام پارامتر `temperature` یا `max_tokens` قدیمی را نمی‌فرستند.

تحلیل تصویر JPEG، PNG، GIF و WebP را می‌پذیرد، تصاویر خالی و فایل‌های بیش از ۱۰ مگابایت را رد می‌کند و شرح نهایی را تا ۱۰۰۰ کاراکتر محدود می‌کند. درخواست داستان داستان کوتاه مناسب خانواده می‌خواهد. انتخاب‌های خالی یا محتوای خالی مدل خطا هستند و خطاها دلیل اصلی را برای تشخیص سمت سرور حفظ می‌کنند. کلاینت SDK وقتی برنامه خاموش می‌شود بسته می‌شود.

### 4. قالب‌های وب

**فایل:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (فرم آپلود)

صفحه با یک انتخاب‌کننده عکس شروع می‌شود، نه با ناحیه متنی شرح. **تحلیل تصویر** عکس انتخاب‌شده را پیش‌نمایش می‌کند و آن را به `/analyze-image` ارسال می‌کند. پاسخ موفقیت‌آمیز شرح را نمایش می‌دهد، فیلد مخفی `description` را پر می‌کند و دکمه **تولید داستان** را آشکار می‌کند. این دکمه همان فرم را به `/generate-story` ارسال می‌کند.

هیچ دانلود مدلی در مرورگر یا وابستگی CDN وجود ندارد. تحلیل تصویر در سرور از طریق استقرار Azure پیکربندی‌شده اجرا می‌شود. خطاها قابل مشاهده باقی می‌مانند و داستان با شرح ساختگی فعال نمی‌شود. انتخاب فایل متفاوت تحلیل قبلی را پاک می‌کند.

**فایل:** `result.html` (نمایش داستان)

داستان تولیدشده را نمایش می‌دهد:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**ویژگی‌های قالب:**

1. **ادغام Thymeleaf**: استفاده از ویژگی‌های `th:` برای محتوای داینامیک
2. **طراحی واکنش‌گرا**: استایل CSS برای موبایل و دسکتاپ
3. **مدیریت خطا**: نمایش خطاهای اعتبارسنجی به کاربران
4. **مدیریت آپلود**: جاوااسکریپت عکس را پیش‌نمایش می‌کند، درخواست چندبخشی محافظت‌شده با CSRF می‌فرستد و شرح بازگشتی را نمایش می‌دهد

### 5. پیکربندی

**فایل:** `application.properties`

تنظیمات پیکربندی برای برنامه:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**توضیح پیکربندی:**

1. **آپلود فایل**: هر دو فایل و درخواست چندبخشی کامل به حداکثر ۱۰ مگابایت محدود شده‌اند؛ عکس‌ها را زیر این حد نگه دارید تا فضای هدر multipart باقی بماند
2. **لاگ‌گیری**: کنترل می‌کند چه اطلاعاتی در زمان اجرا ثبت شود
3. **Azure AI Foundry**: نقطه انتهایی و استقرار مدل را مشخص می‌کند (احراز هویت بدون کلید)
4. **امنیت**: محافظت CSRF فعال است؛ تشخیص خطای مدل در سرور لاگ می‌شود، و کنترلر پیام‌های خطای عمومی مدل را نمایش می‌دهد

## اجرای برنامه

### گام ۱: ورود و تنظیم نقطه انتهایی

احراز هویت بدون کلید است (Microsoft Entra ID)، پس کلید API نیست. وارد شوید و نقطه انتهایی Foundry خود را تنظیم کنید:

**ویندوز (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**ویندوز (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**لینوکس/مک‌اواس:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**چرا این لازم است:**
- Azure AI Foundry از Microsoft Entra ID برای احراز هویت درخواست‌های استنتاج استفاده می‌کند
- احراز هویت بدون کلید یعنی رمزعبور یا کلید در کد یا محیط شما نیست
- حساب شما باید نقش **Cognitive Services OpenAI User** را روی منبع داشته باشد

نام استقرار پیش‌فرض `gpt-5.6-luna` است. اگر استقرار GPT-5.6 Luna شما نام دیگری دارد، متغیر `AZURE_OPENAI_DEPLOYMENT` را در همان ترمینال قبل از اجرای برنامه تنظیم کنید. هر دو تحلیل تصویر و تولید داستان از این تنظیم استفاده می‌کنند.

### گام ۲: ساخت و اجرا

به پوشه پروژه بروید:
```bash
cd 04-PracticalSamples/petstory
```

فایل اجرایی JAR مستقل بسازید و همه تست‌های آفلاین را اجرا کنید:
```bash
mvn clean package
```

سرور را اجرا کنید:
```bash
mvn spring-boot:run
```

برنامه روی آدرس `http://localhost:8080` اجرا خواهد شد.

به جای آن، JAR بسته‌بندی‌شده را روی پورت آزاد مثلاً:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

برای آن دستور، `http://localhost:8083/` را باز کنید. همان مسیرهای `/analyze-image` و `/generate-story` روی پورت انتخاب شده در دسترس‌اند.

### گام ۳: تست برنامه

۱. **باز کردن** `http://localhost:8080` در مرورگر
۲. **انتخاب** عکس واضح حیوان خانگی در فرمت JPEG، PNG، GIF یا WebP، زیر ۱۰ مگابایت
۳. **کلیک** روی "تحلیل تصویر" و انتظار برای شرح حیوان
۴. **کلیک** روی "تولید داستان" پس از تحلیل موفق
۵. **مشاهده** داستان و استفاده از لینک صفحه نتیجه برای بازگشت به فرم آپلود

جریان موفق از عکس به داستان دو بار تماس مدل می‌گیرد، یکی برای هر دکمه. استنتاج زنده سهمیه استقرار شما را مصرف می‌کند و ممکن است هزینه داشته باشد؛ تست‌های ابتدایی را وقتی استقرار محدود است پشت سر هم اجرا کنید. بارگذاری صفحه اصلی تماس با مدل ندارد.

## تست‌های آفلاین

از دایرکتوری sample اجرا کنید:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) درخواست‌های واقعی SDK OpenAI را با fixture HTTP حلقه‌برگردان ضبط می‌کند. هر دو درخواست استقرار، `reasoning_effort: none`، محدودیت توکن‌ها، داده تصویر، اعتبارسنجی ورودی، پاسخ‌های خالی و خطاهای بالادستی را بررسی می‌کند.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) با MockMvc و مدل سرویس شبیه‌سازی‌شده صفحات Thymeleaf رندرشده، قرارداد آپلود، CSRF، اعتبارسنجی، فراردهی خروجی و شکست‌های قابل مشاهده را تست می‌کند. این تست‌ها نیازی به کلید Azure ندارند و هیچگاه از استنتاج پولی Azure استفاده نمی‌کنند. Maven گزارش Surefire را زیر `target/surefire-reports` می‌نویسد.

## چگونگی عملکرد کلی

اینجا جریان کامل هنگام تولید داستان حیوان خانگی است:

۱. **انتخاب عکس**: شما یک تصویر حیوان خانگی در فرم آپلود انتخاب می‌کنید
۲. **آپلود تصویر**: "تحلیل تصویر" یک درخواست چندبخشی POST به `/analyze-image` با هدر CSRF می‌فرستد
۳. **تحلیل تصویر**: `StoryService` تصویر را به GPT-5.6 Luna با تنظیم reasoning برابر none می‌فرستد
۴. **نمایش شرح**: مرورگر شرح بازگشتی را نمایش می‌دهد و آن را در فرم ذخیره می‌کند
۵. **ارسال داستان**: "تولید داستان" `description` و `_csrf` را به `/generate-story` ارسال می‌کند
۶. **تولید داستان**: کنترلر شرح را اعتبارسنجی می‌کند و همان استقرار را با reasoning برابر none صدا می‌زند
۷. **رندر قالب**: Thymeleaf شرح و داستان را در صفحه نتیجه فراردهی و نمایش می‌دهد

**جریان مدیریت خطا:**
اگر مدل ناکام بماند، دلیل آن در سرور لاگ می‌شود. تحلیل تصویر HTTP 502 برمی‌گرداند و مرورگر بدون نمایش "تولید داستان" خطا را نشان می‌دهد. تولید داستان به فرم با پیام خطا ریدایرکت می‌شود. هیچ مسیر به صورت خاموش نتیجه از پیش نوشته شده را جایگزین نمی‌کند.

## درک ادغام هوش مصنوعی

### Azure AI Foundry (بدون کلید)
سرویس SDK را با نقطه انتهایی `/openai/v1/` منبع شما پیکربندی می‌کند. `DefaultAzureCredential` و `AuthenticationUtil.getBearerTokenSupplier` توکن‌های Microsoft Entra را برای `https://ai.azure.com/.default` فراهم می‌کنند. توسعه محلی می‌تواند از ورود Azure CLI شما استفاده کند؛ اپ میزبانی‌شده در Azure می‌تواند از شناسه مدیریت شده با دسترسی‌های لازم منبع بهره بگیرد.

### مهندسی دستور
درخواست تحلیل تصویر ویژگی‌های قابل مشاهده حیوان خانگی را در یک پاراگراف کوتاه می‌خواهد و به مدل می‌گوید متن روی تصویر داده است، نه دستورالعمل. تولید داستان از شرح برگشتی در یک درخواست نوشتاری جداگانه، مناسب خانواده استفاده می‌کند. هیچکدام استنتاج یا تنظیم دمای پاسخ را فعال نمی‌کنند.

### پردازش پاسخ
هندلر پاسخ مشترک انتخاب‌های گمشده و محتوای خالی یا فقط فاصله را رد می‌کند، محتوای معتبر را کوتاه می‌کند و خطاهای بالادستی را حفظ می‌کند. شرح تصاویر به ۱۰۰۰ کاراکتر برای تناسب با فرم داستان بعدی محدود شده‌اند. خطای اصلی مدل برای تشخیص حفظ می‌شود اما به کاربر نمایش داده نمی‌شود.

## گام‌های بعدی

برای نمونه‌های بیشتر، فصل [04: نمونه‌های عملی](../README.md) را ببینید

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوء تفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->