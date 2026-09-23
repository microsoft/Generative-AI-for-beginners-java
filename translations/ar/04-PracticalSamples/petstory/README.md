# درس إنشاء قصة حيوان أليف للمبتدئين

قم بتحميل صورة حيوان أليف، وحللها باستخدام GPT-5.6 لونا، وقم بإنشاء قصة من الوصف الناتج. كلا طلبات النموذج تستخدم `reasoning_effort: none`.

| المكون | النسخة |
| --- | --- |
| جافا | 21 أو أعلى |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## جدول المحتويات

- [المتطلبات الأساسية](#المتطلبات-الأساسية)
- [فهم هيكل المشروع](#فهم-هيكل-المشروع)
- [شرح المكونات الأساسية](#شرح-المكونات-الأساسية)
  - [1. التطبيق الرئيسي](#1-التطبيق-الرئيسي)
  - [2. وحدة التحكم في الويب](#2-وحدة-التحكم-في-الويب)
  - [3. خدمة القصة](#3-خدمة-القصة)
  - [4. قوالب الويب](#4-قوالب-الويب)
  - [5. التكوين](#5-التكوين)
- [تشغيل التطبيق](#تشغيل-التطبيق)
- [اختبارات دون اتصال](#اختبارات-دون-اتصال)
- [كيف يعمل كل شيء معًا](#كيف-يعمل-كل-شيء-معًا)
- [فهم تكامل الذكاء الاصطناعي](#فهم-تكامل-الذكاء-الاصطناعي)
- [الخطوات التالية](#الخطوات-التالية)

## المتطلبات الأساسية

قبل البدء، تأكد من أنك تملك:
- تثبيت جافا 21 أو أعلى
- Maven لإدارة التبعيات
- نشر Azure AI Foundry لـ GPT-5.6 Luna مسمى `gpt-5.6-luna`، أو تجاوز `AZURE_OPENAI_DEPLOYMENT` يشير إلى ذلك النشر. انظر [الفصل 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) للتوفير وسجل الدخول بـ `az login` للمصادقة بدون مفتاح. يجب أن يدعم النشر إدخال الصور و `reasoning_effort: none`.
- فهم أساسي لـ جافا، Spring Boot، وتطوير الويب

## فهم هيكل المشروع

يحتوي مشروع قصة الحيوان الأليف على عدة ملفات مهمة:

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

## شرح المكونات الأساسية

### 1. التطبيق الرئيسي

**الملف:** `PetStoryApplication.java`

هذه هي نقطة الدخول لتطبيق Spring Boot الخاص بنا:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**ما يقوم به هذا:**
- التعليمة `@SpringBootApplication` تمكّن التهيئة التلقائية والمسح التلقائي للمكونات
- يبدأ خادم ويب مدمج (Tomcat) على المنفذ 8080
- ينشئ كل الـ beans والخدمات اللازمة تلقائيًا لـ Spring

### 2. وحدة التحكم في الويب

**الملف:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| نقطة النهاية | الطلب | الاستجابة الناجحة |
| --- | --- | --- |
| `GET /` | لا يوجد جسم | نموذج تحميل HTML مع رمز CSRF |
| `POST /analyze-image` | `multipart/form-data`، حقل ملف `image` | JSON: `{"description":"حيوان أليف لعوب..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`، حقل `description` | صفحة HTML للنتيجة مع الوصف والقصة المولدة |

كلا نقطتي النهاية POST تتطلبان ملف تعريف الارتباط للجلسة ورمز CSRF المُستخلص من `GET /`. ترسل سكريبت التحميل القيمة المخفية `_csrf` في رأس `X-CSRF-TOKEN`؛ إرسال القصة يرسلها كحقل `_csrf` في النموذج. يجب على عملاء API الاحتفاظ بملف تعريف الارتباط بين الطلبات. هذه نقاط نهاية للطلبات النموذجية، وليست طلبات JSON.

يجب أن تكون الأوصاف غير فارغة ولا تزيد عن 1000 حرف. يقوم وحدة التحكم بقص الوصف وإزالة `<`, `>`, علامات الاقتباس المزدوجة، الفواصل العليا، و `&` قبل تمريرها إلى الخدمة. كما تقوم القالب الخاص بالنتيجة بتعقيم مخرجات النموذج باستخدام `th:text`.

تعيد فشلات التحقق من الصورة HTTP 400 مع حقل `error`؛ تفشل فشلات النموذج بـ HTTP 502 مع حقل `error` ودون وجود `description`. تشير أوصاف القصة غير الصالحة أو فشلات النموذج إلى إعادة التوجيه إلى `/` مع رسالة خطأ ظاهرة. تعيد الحقول المفقودة المطلوبة HTTP 400، ورموز CSRF المفقودة أو غير الصالحة تعيد HTTP 403. لا يتم عرض أوصاف بديلة أو قصص كنواتج ذكاء اصطناعي ناجحة.

### 3. خدمة القصة

**الملف:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

يستخدم SDK رسمي لـ OpenAI Java 4.63.1 API الدردشة المتوافقة مع OpenAI من Azure AI Foundry. توفر Azure Identity 1.18.6 رمز حامل Microsoft Entra عبر `DefaultAzureCredential`؛ لا حاجة لمفتاح API.

| العملية | الإدخال | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | بايتات الصورة مشفرة كرابط بيانات base64 مع نوع MIME المرفوع | 300 |
| `generateStory` | وصف الحيوان الأليف في رسالة مستخدم | 800 |

كلا الطلبين يستخدمان النشر المُكوّن، بالافتراضي `gpt-5.6-luna`، ويضبطان صراحة `ReasoningEffort.NONE` (`reasoning_effort: none`). لا يرسلان `temperature` ولا المعامل القديم `max_tokens`.

يقبل تحليل الصورة صيغ JPEG، PNG، GIF، و WebP، ويرفض الصور الفارغة والملفات الأكبر من 10 ميغابايت، ويحد النص الوصفي الناتج بحد اقصى 1000 حرف. يطلب الاستدعاء القصصي قصة قصيرة مناسبة للعائلة. الخيارات الفارغة أو محتوى النموذج الفارغ هي أخطاء، وتخزن الأخطاء الأصلية لأغراض التشخيص على الخادم. يُغلق العميل SDK عند إيقاف التطبيق.

### 4. قوالب الويب

**الملف:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (نموذج التحميل)

تبدأ الصفحة بخيار اختيار صورة، وليس منطقة نص للوصف. **تحليل الصورة** يعرض معاينة الصورة المختارة ويرسلها إلى `/analyze-image`. تعرض الاستجابة الناجحة الوصف، وتملأ الحقل المخفي `description`، وتعطي الظهور لـ **إنشاء قصة**. يرسل هذا الزر النموذج الموجود إلى `/generate-story`.

لا يوجد تحميل نموذج في المتصفح أو اعتماد على CDN. يعمل تحليل الصورة على الخادم من خلال نشر Azure المُكوّن. تبقى الأخطاء ظاهرة ولا تسمح بإنشاء قصة بمع وصف ملفق. اختيار ملف مختلف يمسح التحليل السابق.

**الملف:** `result.html` (عرض القصة)

يعرض القصة المولدة:

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

**ميزات القالب:**

1. **تكامل Thymeleaf**: يستخدم سمات `th:` للمحتوى الديناميكي
2. **تصميم مستجيب**: تنسيق CSS للهواتف المحمولة وسطح المكتب
3. **معالجة الأخطاء**: يعرض أخطاء التحقق للمستخدمين
4. **معالجة التحميل**: جافا سكريبت تعرض معاينة الصورة، ترسل طلب متعدد الأجزاء محمي برمز CSRF، وتعرض الوصف المُستلم

### 5. التكوين

**الملف:** `application.properties`

إعدادات التكوين للتطبيق:

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

**تفسير التكوين:**

1. **تحميل الملفات**: الحد الأقصى للملف والطلب كامل متعدد الأجزاء هو 10 ميغابايت؛ احفظ الصور تحت هذا الحد لتوفير مساحة لرؤوس الطلبات المتعددة
2. **التسجيل**: يتحكم في ما يتم تسجيله أثناء التنفيذ
3. **Azure AI Foundry**: يحدد نقطة النهاية ونشر النموذج المستخدم (مصادقة بدون مفتاح)
4. **الأمان**: تظل حماية CSRF مفعلة؛ تُسجّل تشخيصات النموذج على الخادم، بينما تعرض وحدة التحكم رسائل فشل عامة للنموذج

## تشغيل التطبيق

### الخطوة 1: تسجيل الدخول وضبط نقطة النهاية

المصادقة بدون مفتاح (Microsoft Entra ID)، لذا لا يوجد مفتاح API. سجّل الدخول واضبط نقطة النهاية الخاصة بـ Foundry:

**ويندوز (موجه الأوامر):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**ويندوز (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**لينكس/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**لماذا هذا مطلوب:**
- يستخدم Azure AI Foundry Microsoft Entra ID لمصادقة طلبات الاستدلال
- المصادقة بدون مفتاح تعني عدم وجود أسرار في كود المصدر أو البيئة
- يحتاج حسابك إلى دور **مستخدم Cognitive Services OpenAI** على المورد

اسم النشر الافتراضي هو `gpt-5.6-luna`. إذا كان نشر GPT-5.6 Luna الخاص بك يحمل اسمًا آخر، اضبط `AZURE_OPENAI_DEPLOYMENT` في نفس الطرفية قبل بدء التطبيق. تستخدم كل من تحليل الصورة وتوليد القصة هذا الإعداد.

### الخطوة 2: البناء والتشغيل

انتقل إلى دليل المشروع:
```bash
cd 04-PracticalSamples/petstory
```

ابنِ ملف JAR التنفيذي المستقل وقم بتشغيل كل الاختبارات دون اتصال:
```bash
mvn clean package
```

ابدأ الخادم:
```bash
mvn spring-boot:run
```

سيبدأ التطبيق على `http://localhost:8080`.

بدلاً من ذلك، ابدأ ملف JAR المعبأ على منفذ حر، على سبيل المثال:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

لذلك الأمر، افتح `http://localhost:8083/`. نفس مسارات `/analyze-image` و `/generate-story` متاحة على المنفذ المختار.

### الخطوة 3: اختبار التطبيق

1. **افتح** `http://localhost:8080` في متصفحك
2. **اختر** صورة واضحة لحيوان أليف بصيغ JPEG، PNG، GIF، أو WebP، تكون أقل من 10 ميغابايت
3. **انقر** على "تحليل الصورة" وانتظر وصف الحيوان الأليف
4. **انقر** على "إنشاء القصة" بعد نجاح التحليل
5. **عارض** القصة واستخدم رابط صفحة النتيجة للعودة إلى نموذج التحميل

تدفق الصورة إلى القصة الناجح يقوم بندائين للنموذج، واحد لكل زر. الاستدلال الحي يستهلك الحصة من نشراتك وقد يترتب عليه رسوم؛ شغّل اختبارات التدخين بشكل متتابع عند مشاركة نشر محدود المعدل. فتح الصفحة الرئيسية لا يستدعي النموذج.

## اختبارات دون اتصال

من دليل العينات، شغل:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) يلتقط طلبات SDK الحقيقية لـ OpenAI باستخدام وحدة HTTP loopback. يتحقق من نشر كلا الطلبين، `reasoning_effort: none`، حدود التوكنات، حمولة الصورة، تحقق الإدخال، الردود الفارغة، وأخطاء المصدر.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) يستخدم MockMvc مع خدمة نموذج مزيفة لاختبار صفحات Thymeleaf المعروضة، اتفاقية التحميل، CSRF، التحقق، تعقيم المخرجات، والفشلات الظاهرة. لا تحتاج هذه الاختبارات إلى بيانات اعتماد Azure ولا تستدعي استدلال مدفوع في Azure. تكتب Maven تقارير Surefire تحت `target/surefire-reports`.

## كيف يعمل كل شيء معًا

هذا هو التدفق الكامل عند إنشاء قصة حيوان أليف:

1. **اختيار الصورة**: تختار صورة حيوان أليف في نموذج التحميل
2. **تحميل الصورة**: "تحليل الصورة" يرسل طلب POST متعدد الأجزاء إلى `/analyze-image` مع رأس CSRF
3. **تحليل الصورة**: ترسل `StoryService` الصورة إلى GPT-5.6 Luna مع ضبط الجهد الذهني إلى `none`
4. **عرض الوصف**: يعرض المتصفح الوصف المُعاد ويخزنه في النموذج
5. **إرسال القصة**: "إنشاء القصة" يرسل `description` و `_csrf` إلى `/generate-story`
6. **توليد القصة**: تتحقق وحدة التحكم من الوصف وتتصل بنفس النشر مع ضبط الجهد الذهني إلى `none`
7. **عرض القالب**: تقوم Thymeleaf بتعقيم وعرض الوصف والقصة في صفحة النتيجة

**تدفق معالجة الأخطاء:**
إذا فشل النموذج، يسجل الخادم السبب. يعيد تحليل الصورة HTTP 502 ويعرض المتصفح الخطأ دون إظهار "إنشاء القصة". يعيد توليد القصة التوجيه إلى النموذج مع رسالة خطأ. لا يستبدل أي مسار بهدوء بنتيجة معدة مسبقًا.

## فهم تكامل الذكاء الاصطناعي

### Azure AI Foundry (بدون مفتاح)
تقوم الخدمة بتكوين SDK مع نقطة نهاية المورد `/openai/v1/`. يوفر `DefaultAzureCredential` و `AuthenticationUtil.getBearerTokenSupplier` رموز Microsoft Entra لـ `https://ai.azure.com/.default`. يمكن لتطوير محلي استخدام تسجيل دخول Azure CLI الخاص بك؛ يمكن للتطبيق المستضاف على Azure استخدام هوية مُدارة مع الأذونات اللازمة للمورد.

### هندسة الأمر
تطلب تحليل الصورة ميزات مرئية للحيوان الأليف في فقرة قصيرة وتخبر النموذج بمعاملة النص في الصورة كبيانات، وليس كتعليمات. يستخدم توليد القصة الوصف المرتجع في طلب كتابة منفصل مناسب للعائلة. كلا المكالمتين لا تفعّل الجهد الذهني ولا تضبط درجة الحرارة.

### معالجة الاستجابة
يرفض معالج الاستجابة المشترك الخيارات المفقودة والمحتوى الفارغ أو المحتوى الذي يحتوي على فراغات فقط، ويقص المحتوى الصالح، ويحافظ على فشلات المصدر. يقتصر وصف الصورة على 1000 حرف ليناسب نموذج القصة اللاحق. تُحتفظ بفشل النموذج الأصلي للتشخيص، لكنه لا يُعرض للمستخدم.

## الخطوات التالية

للمزيد من الأمثلة، انظر [الفصل 04: عينات عملية](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->