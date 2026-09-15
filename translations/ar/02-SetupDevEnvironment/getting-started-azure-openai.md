# إعداد بيئة التطوير لـ Azure AI Foundry

> يوضح هذا الدليل كيفية إعداد نماذج **Azure AI Foundry** لتطبيقات Java AI في هذه الدورة التدريبية، باستخدام المصادقة **بدون مفاتيح** (Microsoft Entra ID) — بدون الحاجة لإدارة مفاتيح API. جديد في الأدوات؟ ابدأ بـ [دليل بيئة التطوير](./README.md).

يوضح هذا الدليل كيفية إعداد نماذج **Azure AI Foundry** لتطبيقات Java AI في هذه الدورة. لديك مساران:

- **الخيار أ — الإعداد باستخدام `azd` + Bicep (مُوصى به):** أمر واحد ينشر حساب Foundry والنماذج كرمز. لا حاجة للنقر في البوابة.
- **الخيار ب — إنشاء الموارد يدويًا** في بوابة Azure AI Foundry.

كلا المسارين يستخدمان **المصادقة بدون مفاتيح** (Microsoft Entra ID) — لا يوجد مفاتيح API للنسخ أو التسريب.

## جدول المحتويات

- [ما الذي يتم إنشاؤه](#ما-الذي-يتم-إنشاؤه)
- [المتطلبات المسبقة](#المتطلبات-المسبقة)
- [الخيار أ: الإعداد باستخدام azd + Bicep (مُوصى به)](#option-a-provision-with-azd--bicep-recommended)
- [الخيار ب: إنشاء الموارد يدويًا](#الخيار-ب-إنشاء-الموارد-يدويًا)
- [تكوين بيئتك](#تكوين-بيئتك)
- [اختبر إعدادك](#اختبار-إعدادك)
- [ما الخطوة التالية؟](#ما-الخطوة-التالية؟)
- [المصادر](#المصادر)
- [الموارد الإضافية](#موارد-إضافية)

## ما الذي يتم إنشاؤه

تقوم قوالب Bicep في [`infra/`](../../../02-SetupDevEnvironment/infra) بإعداد:

- حساب **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, النوع `AIServices`) مع مشروع
- نشر **الدردشة** - GPT-5.6 Luna (`gpt-5.6-luna`)، الإصدار `2026-07-09`، بسعة `GlobalStandard` تبلغ `10` (10 طلبات/دقيقة و10,000 توكن/دقيقة لهذا النموذج)
- نشر **التضمين** - `text-embedding-3-small`، الإصدار `1` (يستخدم في الفصول اللاحقة)
- تعيين دور **بدون مفاتيح** (`Cognitive Services OpenAI User`) لكي تسجل الدخول باستخدام `az login` بدلاً من إدارة المفاتيح

## المتطلبات المسبقة

- [اشتراك Azure](https://azure.microsoft.com/free/)
- [CLI مطور Azure (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) و [Maven 3.9+](https://maven.apache.org/download.cgi)

## الخيار أ: الإعداد باستخدام azd + Bicep (مُوصى به)

من مجلد `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# تسجيل الدخول (كلا الأداتين)
azd auth login
az login

# توفير حساب Foundry + نشر النماذج
azd up
```

تطلب `azd` **اسم البيئة** (مثلاً `genai-java`)، و**الاشتراك**، و**المنطقة**. اختر الاشتراك الخاص بك ومنطقة حيث تتوفر نماذج `gpt-5.6-luna` و`text-embedding-3-small`، مثلاً `eastus2`. تأكد من أن الاشتراك لديه الحصة الكافية للنموذج ونوع النشر في تلك المنطقة؛ تختلف التوفر والحصة حسب الاشتراك.

عند الانتهاء من الإعداد، يقوم azd بـ:

1. نشر كل ما هو معرف في [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. تشغيل برنامج نصي بعد الإعداد يكتب [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) مع أسماء نقطة النهاية والنشر الخاصة بك (بدون أسرار).

> **نصيحة:** أعد تشغيل `azd up` في أي وقت لتطبيق التغييرات. شغّل `azd down` لحذف كل شيء وإيقاف التكلفة.

لرؤية الإعدادات التي تم إنشاؤها:

```bash
azd env get-values
```

الآن انتقل إلى [اختبر إعدادك](#اختبار-إعدادك).

## الخيار ب: إنشاء الموارد يدويًا

تفضل استخدام البوابة؟ أنشئ الموارد يدويًا:

1. انتقل إلى [بوابة Azure AI Foundry](https://ai.azure.com/) وسجّل الدخول.
2. **أنشئ مشروعًا** (وهذا ينشئ أيضًا مورد AI Foundry). أطلق عليه اسمًا مثل `GenAIJava`.
3. في مشروعك، افتح **النماذج + نقاط النهاية** → **نشر نموذج** → **نشر نموذج أساسي**.
4. انشر **GPT-5.6 Luna** (اسم النموذج والنشر `gpt-5.6-luna`، الإصدار `2026-07-09`) بسعة **Global Standard** بمقدار `10`. كرر مع **text-embedding-3-small**، الإصدار `1`، إذا أردت أمثلة التضمين.
5. من **نظرة عامة**، انسخ **نقطة النهاية** (مثلاً `https://<resource>.openai.azure.com/`).
6. امنح نفسك وصولًا بدون مفاتيح: على المورد، افتح **التحكم في الوصول (IAM)** → **إضافة تعيين دور** → عيّن دور **Cognitive Services OpenAI User** لحسابك.

> **ما زلت تواجه مشكلة؟** راجع [توثيق Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## تكوين بيئتك

**إذا استخدمت الخيار أ (`azd up`)**، فملف الإعدادات مكتوب بالفعل — لا حاجة لتكوين. انتقل إلى [اختبر إعدادك](#اختبار-إعدادك).

**إذا استخدمت الخيار ب (يدويًا)**، أنشئ ملف `.env` الخاص بالمثال بنفسك:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

عدّل `.env` مع نقطة النهاية الخاصة بك (بدون مفتاح — المصادقة بدون مفاتيح):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

استخدم نقطة النهاية Azure OpenAI الخاصة بالمورد، وليس عنوان URL الخاص بالمشروع. تطبيق basic-chat يحلها إلى `/openai/v1` ويهيئ عميل رمز حامل صريح؛ لا يتطلب مفتاح API.

> **ملاحظة أمان:** لا يوجد مفتاح API للتخزين. تقوم بالمصادقة باستخدام Microsoft Entra ID عبر `az login` (محليًا) أو هوية مُدارة (في Azure). ملف `.env` يحتوي فقط على إعدادات غير سرية وهو مغطى بالفعل بواسطة `.gitignore`.

## اختبار إعدادك

تأكد من تسجيل الدخول حتى تتمكن المصادقة بدون مفاتيح من الحصول على رمز، ثم شغّل المثال:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # إذا لم تكن قد سجلت الدخول بالفعل
mvn clean spring-boot:run
```

يجب أن ترى استجابة من نموذج `gpt-5.6-luna`. شغّل الأمثلة بالتسلسل للبقاء ضمن الحصة الصغيرة الافتراضية؛ إذا استلمت HTTP 429، انتظر فترة إعادة المحاولة قبل المحاولة مرة أخرى.

> **لمستخدمي VS Code:** اضغط `F5` للتشغيل. التطبيق يحمل `.env` تلقائيًا.

> **مثال كامل:** راجع [مثال الدردشة الأساسية مع Azure AI Foundry](./examples/basic-chat-azure/README.md) للتفاصيل واستكشاف الأخطاء.

## ما الخطوة التالية؟

بعد الإعداد وتشغيل المثال بنجاح، سيكون لديك:
- Azure AI Foundry مع نماذج `gpt-5.6-luna` و`text-embedding-3-small` منشورة
- مصادقة بدون مفاتيح (Microsoft Entra ID) — لا مفاتيح للإدارة
- ملف `.env` محلي يحتوي على نقطة النهاية وأسماء النشر الخاصة بك
- بيئة تطوير Java جاهزة للانطلاق

**تابع إلى** [الفصل 3: تقنيات الذكاء الاصطناعي التوليدي الأساسية](../03-CoreGenerativeAITechniques/README.md) لبدء بناء تطبيقات الذكاء الاصطناعي!

## المصادر

- [CLI مطور Azure (azd)](https://aka.ms/azure-dev/install)
- [المصادقة بدون مفاتيح مع Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [توثيق Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [الانتقال إلى Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK الرسمي مع Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## موارد إضافية

- [تحميل VS Code](https://code.visualstudio.com/Download)
- [الحصول على Docker Desktop](https://www.docker.com/products/docker-desktop)
- [تكوين حاوية التطوير](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->