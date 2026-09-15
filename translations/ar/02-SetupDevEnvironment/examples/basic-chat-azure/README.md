# دردشة أساسية مع Azure AI Foundry - مثال شامل من البداية إلى النهاية

هذا المثال هو تطبيق بسيط باستخدام Spring Boot يتصل بنموذج **Azure AI Foundry** باستخدام **المصادقة بدون مفتاح** (Microsoft Entra ID) ويختبر إعدادك. يستخدم `ChatClient` من Spring AI، المدعوم من **official OpenAI Java SDK** ونقطة النهاية **Azure OpenAI v1**.

الإصدارات في [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) هي Spring Boot **4.1.1**، Spring AI **2.0.1**، OpenAI Java **4.63.1**، Azure Identity **1.18.6**، و dotenv-java **3.2.0**. تستخدم العينة `spring-ai-starter-model-openai` وتعلن صراحة عن `openai-java` و `azure-identity`؛ أزال Spring AI 2 مبدئ Azure OpenAI القديم.

## جدول المحتويات

- [المتطلبات المسبقة](#المتطلبات-المسبقة)
- [البدء السريع](#البدء-السريع)
- [كيفية عمل المصادقة](#كيفية-عمل-المصادقة)
- [تشغيل التطبيق](#تشغيل-التطبيق)
  - [باستخدام Maven](#باستخدام-maven)
  - [باستخدام VS Code](#باستخدام-vs-code)
  - [المخرجات المتوقعة](#المخرجات-المتوقعة)
- [مرجع التهيئة](#مرجع-التهيئة)
  - [متغيرات البيئة](#متغيرات-البيئة)
  - [تهيئة Spring](#تهيئة-spring)
- [استكشاف الأخطاء وإصلاحها](#استكشاف-الأخطاء-وإصلاحها)
  - [المشاكل الشائعة](#المشاكل-الشائعة)
  - [وضع التصحيح](#وضع-التصحيح)
- [الخطوات التالية](#الخطوات-التالية)
- [الموارد](#الموارد)

## المتطلبات المسبقة

قبل تشغيل هذا المثال، تأكد من:

- وجود مورد Azure AI Foundry مع نشر `gpt-5.6-luna` - قم بتجهيزه باستخدام `azd up` أو يدويًا من خلال [دليل إعداد Azure AI Foundry](../../getting-started-azure-openai.md)
- دور **Cognitive Services OpenAI User** على ذلك المورد (تعيّن قوالب Bicep هذا الدور لك)
- استخدام [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)، مسجل الدخول عبر `az login`
- جافا 21+ و Maven 3.9+

> **لا حاجة لمفتاح API** — المصادقة بدون مفتاح عبر Microsoft Entra ID.

## البدء السريع

```bash
# 1. انتقل إلى المشروع
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. قم بتسجيل الدخول حتى يتمكن التوثيق بدون مفتاح من الحصول على رمز
az login

# 3. قم بتكوين نقطة النهاية
#    - إذا قمت بتشغيل `azd up`، تم كتابة ملف .env لك (تخطى هذه الخطوة).
#    - خلاف ذلك، انسخ القالب وقم بتعيين AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. شغّل التطبيق
mvn spring-boot:run
```

## كيفية عمل المصادقة

هذا المثال يستخدم المصادقة مع **Microsoft Entra ID** — ولا يوجد مفتاح API.

يهيئ التطبيق المصادقة صراحة في [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` ينشئ `BearerTokenCredential` باستخدام `AuthenticationUtil.getBearerTokenSupplier` مع `DefaultAzureCredential` ونطاق `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` يبني `OpenAIClient` باستخدام `OpenAIOkHttpClient.builder()`، يحل نقطة النهاية إلى `/openai/v1`، ويزود مصداقية Bearer باستخدام `.credential(...)`.
3. `azureChatModel()` يزوِّد ذلك العميل إلى `OpenAiChatModel` في Spring AI، والذي يدعم `ChatClient` في الدرس.

هذه المكونات الصريحة تمنع وجود `OPENAI_API_KEY` عامة من تجاوز مصادقة Azure. مجرد حذف مفتاح API من ملف YAML ليس تكوين مصادقة. `DefaultAzureCredential` يمكنه استخدام جلسة `az login` المحلية أو هوية مُدارة في Azure؛ ويجب أن يكون للهوية المختارة دور المورد المذكور أعلاه.

## تشغيل التطبيق

### باستخدام Maven

```bash
mvn spring-boot:run
```

### باستخدام VS Code

1. افتح المشروع في VS Code
2. اضغط `F5` أو استخدم لوحة "تشغيل وتصحيح"
3. اختر تكوين "Spring Boot-BasicChatApplication"

> **ملاحظة**: التطبيق يحمل `.env` من دليل العمل الخاص به، بما في ذلك عند الإطلاق من VS Code.

### المخرجات المتوقعة

مخرجات توضيحية بعد تشغيل ناجح (تم حذف سجلات بدء التشغيل؛ قد تختلف صياغة الاستجابة):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## مرجع التهيئة

### متغيرات البيئة

| المتغير | الوصف | مطلوب | مثال |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | عنوان نقطة نهاية Foundry (Azure OpenAI) | نعم | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | اسم نشر نموذج الدردشة | لا | `gpt-5.6-luna` (الافتراضي) |

> لا يوجد متغير لمفتاح API — المصادقة بدون مفتاح (Microsoft Entra ID عبر `az login`).

### تهيئة Spring

تستخدم إعدادات [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) بادئة `spring.ai.openai` وخصائص محادثة مسطحة (بدون كتلة `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` هو **اسم نشر Azure**. تأتي المصادقة من المكونات الصريحة الموضحة أعلاه، وليس من إعداد `api-key`. الدرس يعطل الاستنتاج ويحدد الحد الأقصى لرموز الإكمال عند 500؛ يترك `temperature` و `max-tokens` القديمة غير محددة.

توصي Microsoft باستخدام [SDK OpenAI الرسمي مع Azure OpenAI v1 وواجهة Responses API للتطبيقات الجديدة](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). تبقى محادثات الإكمال مدعومة لهذا الدرس القائم على الرسائل. بالنسبة إلى GPT-5.6، يجب أن تحدد الطلبات التي تحتوي على أدوات في محادثات الإكمال `reasoning_effort` إلى `none`؛ استخدم Responses عند الجمع بين الاستنتاج والأدوات. راجع [استدعاء الأدوات مع نماذج الاستنتاج](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## استكشاف الأخطاء وإصلاحها

### المشاكل الشائعة

<details>
<summary><strong>خطأ: 401 / "PermissionDenied" / أخطاء التوكن</strong></summary>

- قم بتشغيل `az login` — المصادقة بدون مفتاح تحتاج إلى تسجيل دخول نشط للحصول على توكن
- تأكد من أن حسابك لديه دور **Cognitive Services OpenAI User** على المورد
- إذا قمت للتو بتعيين الدور، انتظر دقيقة لتنتشر التغييرات
- تأكد من أنك في المستأجر/الاشتراك الصحيح (`az account show`)
</details>

<details>
<summary><strong>خطأ: "نقطة النهاية غير صالحة" / أخطاء الاتصال</strong></summary>

- تأكد أن `AZURE_OPENAI_ENDPOINT` هو عنوان URL الأساسي الكامل (مثلاً `https://your-resource.openai.azure.com/`)
- تحقق من اتساق الشرط النهائي
- تحقق من أن نقطة النهاية تطابق المورد الذي جهزته (`azd env get-values`)
</details>

<details>
<summary><strong>خطأ: "النشر غير موجود"</strong></summary>

- تحقق أن `AZURE_OPENAI_DEPLOYMENT` يطابق اسم نشر في Azure
- تحقق من أن النموذج نشر بنجاح ونشط
- اسم النشر الافتراضي هو `gpt-5.6-luna`
</details>

<details>
<summary><strong>خطأ: 429 / تجاوز حدود المعدل</strong></summary>

- نشر GPT-5.6 Luna الافتراضي لديه طاقة Global Standard 10: 10 طلبات/دقيقة و 10,000 رمز/دقيقة
- شغل الأمثلة بشكل متتالي وانتظر فترة إعادة المحاولة من الخدمة قبل المحاولة مرة أخرى
- هذا المثال الأساسي يعطل إعادة المحاولة التلقائية في SDK، لذا تُبلغ الطلبات الفاشلة مباشرةً
</details>

<details>
<summary><strong>VS Code: لم يتم تحميل متغيرات البيئة</strong></summary>

- تأكد من أن ملف `.env` في جذر المشروع (بنفس مستوى `pom.xml`)
- جرب تشغيل `mvn spring-boot:run` في الطرفية المدمجة في VS Code
- تحقق من تثبيت امتداد جافا لـ VS Code بشكل صحيح
</details>

### وضع التصحيح

لتمكين تسجيل التفاصيل، قم بإلغاء تعليق هذه الأسطر في [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## الخطوات التالية

**تم إكمال الإعداد!** واصل رحلة تعلمك:

[الفصل 3: تقنيات الذكاء الاصطناعي التوليدي الأساسية](../../../03-CoreGenerativeAITechniques/README.md)

## الموارد

- [الانتقال إلى Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK الرسمي مع Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [المصادقة بدون مفتاح مع Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [بوابة Azure AI Foundry](https://ai.azure.com/)
- [توثيق Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->