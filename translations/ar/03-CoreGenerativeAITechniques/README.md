# دروس تقنيات الذكاء الاصطناعي التوليدي الأساسية

## جدول المحتويات

- [المتطلبات الأساسية](#المتطلبات-الأساسية)
- [البدء](#البدء)
- [دليل اختيار النموذج](#دليل-اختيار-النموذج)
- [درس 1: إكمالات ونماذج دردشة LLM](#الدرس-1-إكمالات-ونماذج-دردشة-llm)
- [درس 2: استدعاء الدوال](#الدرس-2-استدعاء-الدوال)
- [درس 3: RAG (توليد مدعوم بالاسترجاع)](#الدرس-3-rag-التوليد-المعزز-بالاسترجاع)
- [درس 4: الذكاء الاصطناعي المسؤول](#الدرس-4-الذكاء-الاصطناعي-المسؤول)
- [أنماط شائعة عبر الأمثلة](#أنماط-شائعة-عبر-الأمثلة)
- [اختبارات الوحدة](#اختبارات-الوحدة)
- [التحقق المباشر المتسلسل](#التحقق-المباشر-المتسلسل)
- [استكشاف الأخطاء وإصلاحها](#استكشاف-الأخطاء-وإصلاحها)
- [الخطوات القادمة](#الخطوات-التالية)

## نظرة عامة

تُظهر أربعة برامج جافا مستقلة الدردشة، سجل المحادثة، استدعاء الدوال، توليد معزز باسترجاع كامل الوثيقة (RAG)، وتعاملات الذكاء الاصطناعي المسؤول. جميع طلبات الدردشة تستهدف **GPT-5.6 Luna مع جهد استدلال `none`** بشكل افتراضي.

تستخدم هذه الأمثلة SDK جافا الرسمي من OpenAI مع نقطة نهاية Azure OpenAI v1، وفقًا لـ [إرشادات SDK من مايكروسوفت](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). الحزمة القديمة `azure-ai-openai` لم تعد تعتمد. يتم الاحتفاظ بـ Chat Completions لتعليم سير العمل القائم على الرسائل؛ راجع [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) لخيارات API أخرى.

## المتطلبات الأساسية

- جافا 21 أو أحدث وMaven 3.6.3 أو أحدث.
- نشر دردشة Azure OpenAI باسم `gpt-5.6-luna`، أو تجاوز مع إعدادات Chat Completions المتوافقة.
- هوية Azure مسجلة دخولها مع دور **مستخدم خدمات الذكاء الاصطناعي الإدراكي OpenAI** على المورد. يستخدم التطوير المحلي تسجيل الدخول الخاص بـ Azure CLI؛ يمكن للتطبيقات المستضافة استخدام الهوية المُدارة.
- راجع [الفصل 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) لإعداد المورد وتعليمات تسجيل الدخول.

يثبت [تكوين Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) هذه الإصدارات، تم التحقق منها في 2026-09-14:

| المكون | الإصدار | الغرض |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | العميل الرسمي المتوافق مع Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | المصادقة بدون مفتاح وتحديث الرموز |
| `net.objecthunter:exp4j` | 0.4.8 | تحليل التعبيرات الحسابية بدون تقييم الكود |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | اختبارات وحدة Jupiter بدون اتصال بالإنترنت |
| مترجم Maven / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | تجميع جافا 21، اختبارات، أمثلة تشغيل |

يستخدم المترجم `--release 21`. لا حاجة لـ Spring Boot، Spring AI، أو LangChain4j في هذه الأمثلة المستقلة.

## البدء

من جذر المستودع، قم بتعيين نقطة نهاية المورد وتجاوز النشر الاختياري في الصدفة.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

لا تتطلب الاختبارات بيانات اعتماد Azure أو نقطة نهاية. Maven لا يقرأ ملف بيئة تلقائيًا؛ عيّن المتغيرات في الصدفة المستخدمة لتشغيل الأمثلة الحية. لاختبارات IDE، تحقق من البيئة التي توفرها تكوين الإطلاق.

## دليل اختيار النموذج

| متغير البيئة | المعنى | الافتراضي |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | جذر مورد Azure عبر HTTPS أو URL `/openai/v1` المُطبع مسبقًا | مطلوب للتشغيل الحي |
| `AZURE_OPENAI_DEPLOYMENT` | اسم نشر الدردشة، ليس إصدار النموذج | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | تكوين نشر التضمين المنفصل، غير مستخدم من هذه البرامج الأربعة | `text-embedding-3-small` |

تستخدم تجاوزات النشر الفارغة الإعدادات الافتراضية. تضيف التكوين `/openai/v1` مرة واحدة بدقة وترفض بيانات الاعتماد، وسلاسل الاستعلام، ومسارات النشر القديمة في نقطة النهاية.

كل طلب دردشة يحدد صراحة `reasoningEffort(ReasoningEffort.NONE)` و `maxCompletionTokens(...)`. لا يحدد أي طلب `temperature` أو `top_p` أو خيار رموز الإكمال القديم. يشمل هذا اختيار الأداة ومتابعات نتائج الأدوات. تتطلب أدوات Chat Completions في GPT-5.6 جهد استدلال `none`; راجع [إرشادات الدردشة من مايكروسوفت](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**لا توجد نقطة دخول للبث أو التضمين في هذا الفصل.** يسترجع القارئ المستند بأكمله، لا المتجهات. إذا أضفته مع التضمينات، فاستخدم نشر تضمين منفصل مثل `text-embedding-3-small`، ولا تستخدم Luna أبدًا.

## الدرس 1: إكمالات ونماذج دردشة LLM

المصدر: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

يشغل البرنامج شرحًا بسيطًا لجافا ستريمز، محادثة من دورين باستخدام HashMap/TreeMap، ودردشة تفاعلية. يشمل الدور الثاني أول استجابة مساعد؛ كل دور تفاعلي يرسل أيضًا المحادثة السابقة.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

تزود `config.chatOptions(...)` التكوين ونطاق جهد الاستدلال الصريح. تتجنب الدردشة التفاعلية الأسطر الفارغة، وتنهي عند `exit` أو نهاية الملف EOF، وتحافظ على رسالة النظام بالإضافة إلى تسعة أدوار مكتملة من المستخدم والمساعد. قص عدد الأدوار هو حد تعليمي، وليس ضمانًا دقيقًا لميزانية الرموز.

من دليل الأمثلة:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

توقع ثلاث إجابات أولية، ثم موجه `أنت:`. يضيف كل سؤال تفاعلي غير فارغ طلبًا واحدًا. حدود الإكمال هي 200، 300، 400، ثم 500 رمز لكل دور تفاعلي.

## الدرس 2: استدعاء الدوال

المصدر: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

يستخلص SDK مخططات JSON من السجلات المشروحة `WeatherArguments` و `CalculationArguments`. اختيار أداة مطلوب يجعل كل مثال يستخدم بروتوكول الأداة بدلًا من قبول إجابة النموذج بدون مساعدة.

1. أرسل سؤالًا باستخدام الأداة المسموح بها، مع جهد استدلال `none`، وحد إكمال 300 رمز.
2. اطلب سبب إنتهاء `tool_calls`، تحقق من اسم الدالة ومعرفات الاستدعاء، وفك JSON المطبوع للوسائط.
3. نفذ الدالة المحلية. النموذج لا ينفذ جافا أو كودًا عشوائيًا.
4. أضف رسالة استدعاء الأداة من المساعد مرة واحدة، تليها كل نتيجة مع معرف `tool_call_id` المطابق.
5. أرسل طلبًا نهائيًا واحدًا بـ 300 رمز بدون أدوات واطلب إجابة مكتملة وغير فارغة.

`get_weather` تُرجع طقسًا **محاكى**، ليس مباشرًا. تحترم المدينة وتحول العينة 22 درجة مئوية إلى فهرنهايت عند الطلب. `calculate` يقيّم التعبير المقدم عبر exp4j، ويدعم أشكالًا مثل `15% من 240` و `2 + 3 * 4` ويرفض الحسابات الفارغة، الكبيرة جدًا، غير الصالحة أو غير المنتهية. يستخدم حسابًا عائمًا، وليس دقة عشرية مالية.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

توقع `Function: get_weather`, طقس سياتل المحاكى, `Function: calculate`, `Function result: 36`, والإجابات النهائية الاثنين. لا تتطلب إدخال قياسي أو بيانات اعتماد الطقس الخارجية. يعمل التشغيل الناجح بأربعة طلبات دردشة بالضبط.

## الدرس 3: RAG (التوليد المعزز بالاسترجاع)

المصدر: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). الإدخال: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

يستعرض مثال RAG التمهيدي هذا مستند UTF-8 كامل واحد ويشمله في رسالة المستخدم مع السؤال. توجه رسالة نظام منفصلة النموذج للتعامل مع محتوى المستند كبيانات غير موثوقة والإجابة فقط من ذلك السياق. إذا لم يكن المستند يحتوي على الإجابة، يكون الرد المطلوب: `لا أستطيع العثور على هذه المعلومة في المستند المقدم.`

يمكن للتأسيس تقليل الهلوسات، لكن الحدود أو تعليمات النظام لا تضمن الدقة أو تمنع كل حقن مطالبات. راجع الإجابات المباشرة. عادة ما يضيف RAG في الإنتاج التجزئة، الاسترجاع، الاستشهادات، التحكم بالوصول، والتقييم.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

اطرح سؤالًا واحدًا، مثلاً `ما طريقة المصادقة التي يصفها المستند؟`. توقع إجابة تذكر Microsoft Entra ID. يخرج البرنامج بعد طلب دردشة واحد بحد إكمال 500 رمز.

يعمل البحث في الملفات الافتراضي من جذر المستودع، دليل الفصل، أو دليل الأمثلة. كما يدعم مسارًا صريحًا:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

يجب أن تكون المدخلات غير فارغة: كحد أقصى 32 كيلوبايت من بيانات مستند UTF-8 و2000 حرف سؤال. يفشل البحث عن الملفات المفقودة، والأسئلة الفارغة/EOF، والمدخلات الكبيرة جدًا قبل الاستدلال.

## الدرس 4: الذكاء الاصطناعي المسؤول

المصدر: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

تغطي الاختبارات الست تعليمات ضارة، خطاب كراهية، الخصوصية، معلومات طبية خاطئة، محتوى غير قانوني، وسؤال مسؤول للذكاء الاصطناعي. يراقب البرنامج الاستجابة بدلًا من افتراض وجوب تفعيل كل اختبار فلترة.

| النتيجة | الدليل |
| --- | --- |
| `FILTERED` | رمز خطأ صريح `content_filter` / `ResponsibleAIPolicyViolation`، أو سبب إكمال `content_filter` |
| `REFUSED` | حقل `message.refusal` منظم غير فارغ |
| `POSSIBLE_REFUSAL` | عبارة رفض افتتاحية في نص عادي؛ قاعدة تقديرية تتطلب مراجعة |
| `GENERATED` | إجابة مكتملة غير فارغة؛ ليست دليلاً على أمان المحتوى |

400 HTTP عادي **ليس** دليلاً على الفلترة. تفشل المعلمات غير الصالحة، فشل المصادقة، حدود المعدل، أخطاء الخادم، الردود غير الصحيحة، والإخراج المقتطع في التشغيل بدلاً من إنتاج نجاح أمان كاذب. لا تعتبر الكلمات الشاملة مثل "محتوى ضار" في شرح حميد رفضًا.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

توقع ست نتائج فئوية وملخصًا يوضح أن الملاحظات ليست شهادة أمان. لكل اختبار حد إكمال 300 رمز. راجع الأجوبة غير المتوقعة والرفض المحتمل يدويًا؛ ينبغي أن ينتج المقارنة الحميدة شرحًا مسؤولًا جوهريًا. لا تحتاج إلى إدخال قياسي.

## أنماط شائعة عبر الأمثلة

يقوم [AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) بتوحيد تطبيع نقطة النهاية، تجاوزات النشر، المصادقة بدون مفتاح، وخيارات الدردشة:

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

يقوم مزود الرموز بتحديث رموز الوصول حسب الحاجة. لا تقوم بتسجيل الرموز أو استبدال هذا بمفتاح API. يعيد كل برنامج استخدام العميل الخاص به ويغلقه في `finally` أو من خلال الغلاف `AutoCloseable` الخاص به؛ SDK `OpenAIClient` نفسه ليس `AutoCloseable`.

يتطلب [ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) إجابة نصية مكتملة وغير فارغة. لا تُطبع الخيارات الفارغة، الرفض، الفلاتر، والإجابات المقتطعة كنجاح صامت. يتعامل مثال الذكاء الاصطناعي المسؤول مع نتائج الفلترة/الرفض المتوقعة صراحة. تعطي الإخفاقات غير المعالجة عملية جافا/Maven رمز خروج غير صفري.

**تم تعطيل المحاولات التلقائية لـ SDK** للحفاظ على قابلية توقع طلبات على عمليات النشر المشتركة منخفضة RPM. كل طلب استدلال له مهلة 60 ثانية. قد يستغرق الحصول على الرموز وقتًا إضافيًا. يجب أن يحترم الجدول على مستوى التطبيق الحصص؛ لا تعيد تشغيل طلب مدفوع فشل بسذاجة.

## اختبارات الوحدة

من دليل الأمثلة:

```powershell
mvn -B -ntp clean test
```

يستبدل النقل الاختباري طبقة HTTP في SDK بالكامل، يلتقط أجسام الطلب المتسلسلة الفعلية، ويقدم الاستجابات المجدولة. لا يفتح أي مقابس، ولا يحصل على رموز Azure، ويفشل في الطلبات غير المتوقعة. تتحقق هذه الاختبارات من سلوك التطبيق وبروتوكول SDK، وليست جودة النموذج الحية أو توفر النشر.

| مجموعة الاختبارات | التغطية |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | تطبيع/رفض نقطة النهاية، تجاوزات النشر، خيارات الاستدلال والرموز |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | كل سير عمل الإكمال، سجل الرسائل، تقليم الأدوار المكتملة، EOF، الإخفاقات |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | مخططات الأدوات، الوسائط المطبعة، الحساب، المعرفات، نتائج أدات متعددة، المتابعات الفاشلة |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | البحث في الملفات، UTF-8، حدود الحجم، حمولة التأسيس، الأخطاء الإدخال وAPI |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | كل الاختبارات الست، الفلاتر الصريحة، تصنيف الرفض، 400 عادي وإخفاقات أخرى |

لمجموعة واحدة، استخدم `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. تعيش الأدوات المشتركة في [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## التحقق المباشر المتسلسل

المكالمات الحية منفصلة عن اختبارات الوحدة. استخدم الأوامر التالية **بشكل فردي**، من جذر المستودع، فقط بعد أن تكون بيانات الاعتماد وإمكانية الوصول للنشر جاهزة. لا تحتاج إلى خدمات أو عمليات مستمرة.

لمشاركة نشر **10 طلبات/دقيقة**، احجز حصة كافية للبرنامج التالي بأكمله قبل تشغيله: 5، 4، 1، ثم 6 طلبات. العمليات المتسلسلة وحدها لا تضمن الالتزام بسرعة الحد. نسق الدقيقة المتدحرجة مع كل المتصلين الآخرين؛ لا تلصق الأربعة استدعاءات على دفعة غير موقوتة.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. إكمالات متعددة الأدوار، ودورتان تفاعليتان:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

تحقق من جميع عناوين الأقسام الثلاثة، وخمس إجابات، وإجابة تفاعلية نهائية تسترجع أدا، و`وداعًا!`، ورمز الخروج 0. الميزانية: **5 طلبات، على الأكثر 1,900 رمز إكمال**. لتشغيل أصغر، قم بتمرير فقط `exit`: 3 طلبات / 900 رمز، لكن ذلك لا يختبر الاستدلال التفاعلي.

**2. كلا سير عملي استدعاء الدالة:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

تحقق من اسمي الدالتين، الطقس المحاكى في سياتل، النتيجة المحسوبة 36، إجابتين نهائيتين، ورمز الخروج 0. الميزانية: **4 طلبات، على الأكثر 1,200 رمز إكمال**.

**3. إجابة مستندة إلى الوثيقة:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

تحقق من مسار الوثيقة، وإجابة تذكر Microsoft Entra ID، ورمز الخروج 0. الميزانية: **طلب واحد، على الأكثر 500 رمز إكمال**. الملف الموجود [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) هو الملف الإدخالي الوحيد المطلوب. يجب على التشغيل الاختياري الثاني المتعلق بموضوع غائب الامتناع ويضيف طلباً واحداً / 500 رمز.

**4. ملاحظات الذكاء الاصطناعي المسؤول:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

تحقق من ست فئات والملخص الرصدي، راجع المحتوى المُنشأ، واشتَرط رمز الخروج 0 لإتمام فني. خروج العملية الناجح لا يضمن سلامة النموذج. الميزانية: **6 طلبات، على الأكثر 1,800 رمز إكمال**.

**الإجمالي للأوامر الأربعة: 16 طلب محادثة وعلى الأكثر 5,400 رمز إكمال**، بالإضافة إلى رموز الإدخال (بما في ذلك المحادثة المكررة ومخطط / سجل الأداة). لا توجد طلبات تضمين. يعتمد استخدام الرموز الفعلي على النموذج وقد يكون أقل، خصوصاً بالنسبة للمطالبات المفلترة. تعتمد التكلفة بالدولار على تسعير النشر؛ لا يُقصد بها تقدير مالي ثابت. تفترض جميع حدود الطلبات عدم وجود عمليات تشغيل يدوية متكررة. افحص `$LASTEXITCODE` فورًا بعد كل أمر؛ غير الصفر يعني أن التشغيل لم يكتمل بنجاح.

## استكشاف الأخطاء وإصلاحها

- **نقطة نهاية مفقودة / 401 / 403:** عين نقطة النهاية في عملية الإطلاق، تحقق من تسجيل دخول Azure المحلي ودور المورد المخصص، وافحص وجود تجاوزات بيئية غير مقصودة للهوية.
- **400 / 404:** تأكد من وجود النشر ودعمه لإنهاءات الدردشة مع جهد التفكير `none`. استخدم جذر المورد HTTPS أو عنوان `/openai/v1`، وليس عنوان نشر قديم. الأخطاء الاعتيادية 400 هي إخفاقات تقنية، وليست حواجز أمان.
- **429:** نسق الحصة المشتركة للدورات في الدقيقة ورمز التوكن قبل إعادة المحاولة. الأمثلة متعمدة لعدم إعادة المحاولة تلقائيًا.
- **`Incomplete chat response: length`:** تجاوز الإخراج حد الإكمال. راجع الرد والمطالبة قبل زيادة الحد والميزانية الموثقة؛ لا تسجل تشغيلاً مقطوعًا على أنه ناجح.
- **أخطاء ملف أو إدخال قياسي:** أطلق من دليل مدعوم أو قدّم مسار وثيقة صريح. قدم سؤال قارئ غير فارغ. يمكن لإنهاءات الدردشة أن تنتهي طبيعيًا بنهاية الملف أو `exit`.
- **أخطاء الترجمة:** تحقق من وجود Java 21 أو أحدث، ثم نفذ `mvn -B -ntp clean test`. في PowerShell، اقتبس الوسيطة كاملة التي تحتوي على خاصية منقطة، مثل `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## الخطوات التالية

تابع إلى [الفصل 4: عينات عملية](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:
تمت ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى للدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الرسمي والمعتمد. للمعلومات الهامة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->