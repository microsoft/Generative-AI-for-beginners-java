# بنیادی تخلیقی AI تکنیکز ٹیوٹوریل

## فہرست مضامین

- [ضروریات](#ضروریات)
- [شروع کریں](#شروع-کریں)
- [ماڈل انتخاب گائیڈ](#ماڈل-انتخاب-گائیڈ)
- [ٹیوٹوریل 1: LLM کمپلیشنز اور چیٹ](#ٹیوٹوریل-1-llm-کمپلیشنز-اور-چیٹ)
- [ٹیوٹوریل 2: فنکشن کالنگ](#ٹیوٹوریل-2-فنکشن-کالنگ)
- [ٹیوٹوریل 3: RAG (ریٹریول-آگمینٹڈ جنریشن)](#ٹیوٹوریل-3-rag-ریٹریول-آگمینٹڈ-جنریشن)
- [ٹیوٹوریل 4: ذمہ دار AI](#ٹیوٹوریل-4-ذمہ-دار-ai)
- [مثالوں میں عام پیٹرنز](#مثالوں-میں-عام-پیٹرنز)
- [یونٹ ٹیسٹس](#یونٹ-ٹیسٹس)
- [تسلسل وار لائیو ویریفیکیشن](#تسلسل-وار-لائیو-ویریفیکیشن)
- [مسائل کا حل](#مسائل-کا-حل)
- [اگلے اقدامات](#اگلے-اقدامات)

## جائزہ

چار آزاد جاوا پروگرامز چیٹ، گفتگو کی تاریخ، فنکشن کالنگ، مکمل دستاویز ریٹریول-آگمینٹڈ جنریشن (RAG)، اور ذمہ دار AI ردعمل کی ہینڈلنگ کو ظاہر کرتے ہیں۔ تمام چیٹ درخواستیں ڈیفالٹ طور پر **GPT-5.6 Luna کے ساتھ reasoning effort `none`** کو ہدف بناتی ہیں۔

یہ مثالیں آفیشل OpenAI جاوا SDK کو Azure OpenAI کے v1 اینڈپوائنٹ کے ساتھ استعمال کرتی ہیں، [Microsoft کے SDK رہنمائی](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) کی پیروی کرتے ہوئے۔ پرانا `azure-ai-openai` پیکیج اب انحصار میں نہیں ہے۔ چیٹ کمپلیشنز کو موجودہ پیغام پر مبنی ورک فلو سکھانے کے لئے رکھا گیا ہے؛ دیگر API اختیارات کے لیے [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) دیکھیں۔

## ضروریات

- جاوا 21 یا اس سے جدید اور Maven 3.6.3 یا اس سے بعد کا ورژن۔
- ایک Azure OpenAI چیٹ ڈیپلائمنٹ جس کا نام `gpt-5.6-luna` ہو، یا یکساں چیٹ کمپلیشنز سیٹنگز کے ساتھ اوور رائیڈ۔
- ایک Azure شناخت جس میں resource پر **Cognitive Services OpenAI User** رول موجود ہو۔ لوکل ڈویلپمنٹ آپ کے Azure CLI سائن ان کا استعمال کرتا ہے؛ ہوسٹڈ ایپلی کیشنز منیجڈ شناخت استعمال کر سکتی ہیں۔
- وسائل سیٹ اپ اور سائن ان ہدایات کے لیے [باب 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) دیکھیں۔

[Maven کنفیگریشن](../../../03-CoreGenerativeAITechniques/examples/pom.xml) ان ورژنز کو پک کرتی ہے، 2026-09-14 کو چیک کی گئی:

| کمپونینٹ | ورژن | مقصد |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | آفیشل Azure v1-مطابق کلائنٹ |
| `com.azure:azure-identity` | 1.18.6 | بغیر کلید کی تصدیق اور ٹوکن ریفریش |
| `net.objecthunter:exp4j` | 0.4.8 | کوڈ کے بغیر ریاضی کے اظہار کی پارسنگ |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | آف لائن جیوپیٹر یونٹ ٹیسٹس |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | جاوا 21 کمپائلیشن، ٹیسٹس، چلنے والی مثالیں |

کمپائلر `--release 21` استعمال کرتا ہے۔ ان آزاد مثالوں کو چلانے کے لیے سپرنگ بوٹ، سپرنگ AI، یا LangChain4j کی ضرورت نہیں ہے۔

## شروع کریں

ذخیرہ جاتی جگہ کی روٹ سے، اپنی شیل میں resource اینڈپوائنٹ اور اختیاری ڈیپلائمنٹ اوور رائیڈ سیٹ کریں۔

**ونڈوز پاور شیل:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**لینکس/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

ٹیسٹس کو نہ Azure کی اسناد کی ضرورت ہے نہ اینڈپوائنٹ کی۔ Maven خود بخود ماحول کی فائل نہیں پڑھتا؛ وہ متغیرات شیل میں سیٹ کریں جو لائیو مثالیں چلانے کے لیے استعمال ہو۔ IDE کے لاؤنچز کے لیے، لاؤنچ کنفیگریشن کی طرف سے فراہم کردہ ماحول کی تصدیق کریں۔

## ماڈل انتخاب گائیڈ

| ماحول کی متغیر | مطلب | ڈیفالٹ |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure resource root یا پہلے سے نارملائزڈ `/openai/v1` URL | لائیو رنز کے لیے ضروری |
| `AZURE_OPENAI_DEPLOYMENT` | چیٹ ڈیپلائمنٹ کا نام، ماڈل ورژن نہیں | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | علیحدہ ایمبیڈنگ ڈیپلائمنٹ کنفیگریشن، ان چار پروگرامز میں استعمال نہیں ہوتا | `text-embedding-3-small` |

خالی اوور رائیڈ ڈیفالٹ استعمال کرتے ہیں۔ کنفیگریشن صحیح طور پر `/openai/v1` شامل کرتا ہے اور اینڈپوائنٹ میں اسناد، کوئری سٹرنگز، اور پرانے ڈیپلائمنٹ راستے رد کر دیتا ہے۔

ہر چیٹ درخواست واضح طور پر `reasoningEffort(ReasoningEffort.NONE)` اور `maxCompletionTokens(...)` سیٹ کرتی ہے۔ کوئی درخواست `temperature`, `top_p`, یا پرانے کمپلیشن ٹوکن کا آپشن نہیں دیتی۔ اس میں ٹول سلیکشن اور ٹول نتیجہ فالو اپ شامل ہیں۔ GPT-5.6 چیٹ کمپلیشنز فنکشن ٹولز کو reasoning effort `none` چاہیے؛ دیکھیں [Microsoft کی چیٹ رہنمائی](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)۔

**اس باب میں کوئی اسٹریمنگ یا ایمبیڈنگ راستہ نہیں ہے۔** قاری پورا دستاویز حاصل کرتا ہے، ویکٹرز نہیں۔ اگر آپ اسے ایمبیڈنگ کے ساتھ بڑھاتے ہیں تو علیحدہ ایمبیڈنگ ڈیپلائمنٹ استعمال کریں جیسے `text-embedding-3-small`، کبھی Luna نہیں۔

## ٹیوٹوریل 1: LLM کمپلیشنز اور چیٹ

ماخذ: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)۔

پروگرام ایک سادہ جاوا اسٹریمز کی وضاحت، دو مرحلوں کی HashMap/TreeMap گفتگو، اور انٹرایکٹو چیٹ چلاتا ہے۔ دوسرا مرحلہ پہلے اسسٹنٹ کے جواب کو شامل کرتا ہے؛ ہر انٹرایکٹو مرحلہ پچھلی گفتگو بھی بھیجتا ہے۔

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` ڈیپلائمنٹ اور واضح reasoning سیٹنگ فراہم کرتا ہے۔ انٹرایکٹو چیٹ خالی لائنوں کو چھوڑ دیتا ہے، `exit` یا EOF پر ختم ہوتا ہے، اور سسٹم میسج کے ساتھ نو مکمل یوزر/اسسٹنٹ مراحل کو محفوظ کرتا ہے۔ مرحلہ شمار کاٹنا تعلیمی حد ہے، ٹوکن بجٹ کی ضمانت نہیں۔

مثالوں کی ڈائریکٹری سے:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

تین ابتدائی جوابات کی توقع رکھیں، پھر `You:` پرامپٹ۔ ہر غیر خالی انٹرایکٹو سوال ایک درخواست بڑھاتا ہے۔ کمپلیشن کی حدود 200، 300، 400، پھر 500 ٹوکن فی انٹرایکٹو مرحلہ ہیں۔

## ٹیوٹوریل 2: فنکشن کالنگ

ماخذ: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)۔

SDK JSON اسکیمات کو annotated `WeatherArguments` اور `CalculationArguments` ریکارڈز سے اخذ کرتا ہے۔ ضروری ٹول انتخاب ہر مثال کو ماڈل کے بغیر جواب دینے کے بجائے ٹول پروٹوکول استعمال کرواتا ہے۔

1. سوال بھیجیں جس میں اجازت شدہ ٹول، reasoning effort `none`، اور 300 ٹوکن کی حد ہو۔
2. `tool_calls` ختم ہونے کی وجہ درکار کریں، فنکشن کا نام اور کال IDs کی تصدیق کریں، اور ٹائپ شدہ JSON دلائل کو پارس کریں۔
3. لوکل فنکشن کو چلائیں۔ ماڈل جاوا یا کسی بھی کوڈ کو نہیں چلتا۔
4. اسسٹنٹ کا ٹول کال میسج ایک بار شامل کریں، پھر ہر نتیجہ اس کا مماثل `tool_call_id` کے ساتھ بھیجیں۔
5. ایک آخری 300 ٹوکن کی درخواست بغیر ٹولز کے بھیجیں اور مکمل، غیرخالی جواب درکار کریں۔

`get_weather` **فرضی**، حقیقی نہیں، موسم دیتا ہے۔ یہ شہر کا احترام کرتا ہے اور نمونہ 22 ڈگری سیلسیس کو فارنہائٹ میں بدلتا ہے جب پوچھا جائے۔ `calculate` exp4j کے ذریعے فراہم کردہ اظہار کی جانچ کرتا ہے، `15% of 240` اور `2 + 3 * 4` جیسے فارم کی حمایت کرتا ہے، اور خالی، زیادہ بڑا، غلط یا لا متناہی حسابات مسترد کرتا ہے۔ یہ فلورٹنگ پوائنٹ ریاضی استعمال کرتا ہے، مالی اعشاریہ کی درستگی نہیں۔

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

`Function: get_weather`، فرضی سیئیٹل موسم، `Function: calculate`، `Function result: 36`، اور دو آخری جوابات کی توقع کریں۔ کوئی stdin یا بیرونی موسم کی اسناد درکار نہیں۔ کامیاب رن میں بالکل چار چیٹ درخواستیں استعمال ہوتی ہیں۔

## ٹیوٹوریل 3: RAG (ریٹریول-آگمینٹڈ جنریشن)

ماخذ: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)۔ ان پٹ: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)۔

یہ تعارفی RAG مثال ایک پورا UTF-8 دستاویز حاصل کرتی ہے اور اسے صارف کے پیغام میں سوال کے ساتھ شامل کرتی ہے۔ ایک الگ سسٹم میسج ماڈل کو ہدایت دیتا ہے کہ دستاویز کے مواد کو غیر معتبر ڈیٹا سمجھ کر صرف اسی سیاق و سباق سے جواب دے۔ اگر دستاویز میں جواب نہ ہو تو درخواست کردہ جواب ہوگا: `I cannot find that information in the provided document.`

گراؤنڈنگ وہم کو کم کر سکتی ہے، لیکن نہ تو ڈیلمیٹرز اور نہ ہی سسٹم ہدایات مکمل درستگی کی ضمانت دیتے ہیں یا ہر پرامپٹ انجیکشن سے بچاتے ہیں۔ لائیو جوابات کا جائزہ لیں۔ پروڈکشن RAG عام طور پر بخشے جانے، حوالہ جات، رسائی کنٹرول، اور تشخیص شامل کرتا ہے۔

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

ایک سوال درج کریں، مثلاً `Which authentication method does the document describe?`۔ متوقع جواب میں Microsoft Entra ID کا ذکر ہو گا۔ پروگرام ایک چیٹ درخواست کے بعد 500 ٹوکن کی حد کے ساتھ باہر نکلتا ہے۔

ڈیفالٹ فائل تلاش ذخیرہ روٹ، باب کی ڈائریکٹری، یا مثالوں کی ڈائریکٹری سے ہوتا ہے۔ ایک واضح راستہ بھی معاون ہے:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

ان پٹ غیر خالی ہونا چاہیے: زیادہ سے زیادہ 32 KiB UTF-8 دستاویز ڈیٹا اور 2,000 حرف سوال۔ غائب فائلیں، خالی/EOF سوالات، اور زیادہ بڑے ان پٹ inference سے پہلے ناکام ہوجاتے ہیں۔

## ٹیوٹوریل 4: ذمہ دار AI

ماخذ: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)۔

چھ پرابز ہنر مند ہدایات، نفرت انگیز تقریر، پرائیویسی، طبی غلط معلومات، غیر قانونی مواد، اور ایک عام ذمہ دار AI سوال کا احاطہ کرتے ہیں۔ پروگرام ردعمل کا مشاہدہ کرتا ہے بجائے اس کے کہ ہر پراب کو فلٹر متحرک کرنا ضروری سمجھے۔

| نتیجہ | ثبوت |
| --- | --- |
| `FILTERED` | ایک واضح `content_filter` / `ResponsibleAIPolicyViolation` ایرر کوڈ، یا کمپلیشن `content_filter` ختم ہونے کی وجہ |
| `REFUSED` | ایک غیر خالی منظم `message.refusal` فیلڈ |
| `POSSIBLE_REFUSAL` | عام متن میں شروع ہونے والا انکار جملہ؛ جائزہ کے لئے ایک ہوراسٹک |
| `GENERATED` | مکمل شدہ غیر خالی جواب؛ اس کا مطلب نہیں کہ مواد محفوظ ہے |

ایک عام HTTP 400 **فلٹرنگ کا ثبوت نہیں** ہے۔ غلط پیرامیٹرز، توثیق کی ناکامیاں، رفتار کی حدود، سرور کی غلطیاں، خراب ردعمل، اور محدود آؤٹ پٹ ناکامیاں ہیں جو غلط حفاظتی کامیابی نہیں دیتیں۔ عام الفاظ جیسے "نقصان دہ مواد" ایک معقول وضاحت میں انکار شمار نہیں ہوتے۔

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

چھ زمرہ کے نتائج اور ایک خلاصہ توقع کریں جو کہتا ہے کہ مشاہدات حفاظتی تصدیق نہیں ہیں۔ ہر پراب کے لیے 300 ٹوکن کی حد ہے۔ غیر متوقع جوابات اور ممکنہ انکار کو دستی طور پر دیکھیں؛ ایک معقول موازنہ ایک معنی خیز ذمہ دار AI وضاحت پیدا کرنا چاہیے۔ کوئی stdin درکار نہیں۔

## مثالوں میں عام پیٹرنز

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) اینڈپوائنٹ نارملائزیشن، ڈیپلائمنٹ اوور رائیڈز، بغیر کلید کی تصدیق، اور چیٹ آپشنز کو مرکزیت دیتا ہے:

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

ٹوکن سپلائر ضرورت کے مطابق رسائی ٹوکنز کو ریفریش کرتا ہے۔ ٹوکنز کو لاگ نہ کریں یا اسے API کلید سے تبدیل نہ کریں۔ ہر پروگرام اپنا کلائنٹ دوبارہ استعمال کرتا ہے اور اسے `finally` یا اپنے `AutoCloseable` ریپر کے ذریعے بند کرتا ہے؛ SDK کا `OpenAIClient` خود `AutoCloseable` نہیں ہے۔

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) مکمل، غیر خالی متنی جواب کا مطالبہ کرتا ہے۔ خالی انتخاب، انکار، فلٹرز، اور محدود جوابات خاموشی سے کامیابی کے طور پر پرنٹ نہیں ہوتے۔ ذمہ دار AI مثال متوقع فلٹر/انکار نتائج کو واضح طور پر ہینڈل کرتی ہے۔ غیر ہینڈل شدہ ناکامیاں جاوا/میوین پراسیس کو نان زیرو اخراجی کوڈ دیتی ہیں۔

**خودکار SDK ریٹریاں غیر فعال ہیں** تاکہ مشترکہ کم آر پی ایم ڈیپلائمنٹس پر درخواست کی تعداد قابل پیش گوئی رہے۔ ہر inference درخواست کی 60 سیکنڈ کی ٹائم آؤٹ ہے۔ ٹوکن حصول میں اضافی وقت لگ سکتا ہے۔ ایپلی کیشن لیول شیڈولنگ کو کوٹس کا احترام کرنا ہوگا؛ ناکام شدہ معاوضہ شدہ درخواست کو اندھا دھند دوبارہ نہ چلائیں۔

## یونٹ ٹیسٹس

مثالوں کی ڈائریکٹری سے:

```powershell
mvn -B -ntp clean test
```

ٹیسٹ ٹرانسپورٹ SDK HTTP لیئر کو مکمل طور پر تبدیل کرتا ہے، اصل سلسلہ وار درخواست کے جسم کو پکڑتا ہے، اور قطار شدہ جواب فراہم کرتا ہے۔ یہ کوئی ساکٹ نہیں کھولتا، Azure ٹوکنز حاصل نہیں کرتا، اور غیر متوقع درخواستوں پر ناکام ہوتا ہے۔ یہ ٹیسٹس ایپلی کیشن رویے اور SDK پروٹوکول کی تصدیق کرتے ہیں، ماڈل کے معیار یا ڈیپلائمنٹ کی دستیابی نہیں۔

| ٹیسٹ سوئٹ | کوریج |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | اینڈپوائنٹ نارملائزیشن/رد، ڈیپلائمنٹ اوور رائیڈ، reasoning اور ٹوکن آپشنز |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | ہر کمپلیشن ورک فلو، پیغام کی تاریخ، مکمل مرحلے کی کٹنگ، EOF، ناکامیاں |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | ٹول اسکیمات، ٹائپ دلائل، ریاضی، IDs، متعدد ٹول نتائج، ناکام فالو اپس |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | فائل تلاش، UTF-8، سائز کی حدود، گراؤنڈنگ پیلوڈ، ان پٹ اور API کی غلطیاں |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | تمام چھ پرابز، واضح فلٹرز، انکار کی درجہ بندی، عام 400 اور دیگر ناکامیاں |

ایک سوئٹ کے لیے، استعمال کریں `mvn -B -ntp test "-Dtest=FunctionsAppTest"`۔ مشترکہ فکسچرز [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) میں پائے جاتے ہیں۔

## تسلسل وار لائیو ویریفیکیشن

لائیو کالز یونٹ ٹیسٹس سے الگ ہیں۔ مندرجہ ذیل کمانڈز کو **ایک ایک کر کے** ذخیرہ جائی جگہ سے چلائیں، صرف اس کے بعد جب اسناد اور ڈیپلائمنٹ کی رسائی دستیاب ہو۔ کوئی سروسز یا مسلسل عمل درکار نہیں۔

ایک مشترکہ **10 درخواستیں/منٹ** والے ڈیپلائمنٹ کے لیے، اگلے پورے پروگرام سے پہلے کافی کوٹا رکھیں: 5، 4، 1، پھر 6 درخواستیں۔ تسلسل وار عمل کی وجہ سے ریٹ-لمٹ کی پابندی کی ضمانت نہیں ہوتی۔ تمام کالرز کے ساتھ رولا منٹ کو ہم آہنگ کریں؛ چار کالز کو بغیر وقفے کے بیچ کے طور پر نہ چلائیں۔

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. کمپلیشنز، ملٹی ٹرن، اور دو انٹرایکٹو مرحلے:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

تمام تین سیکشن کے عنوانات، پانچ جوابات، ایک آخری تعاملی جواب جس میں آدا کا ذکر ہو، `Goodbye!`، اور خروجی کوڈ 0 چیک کریں۔ بجٹ: **5 درخواستیں، زیادہ سے زیادہ 1,900 تکمیل ٹوکنز**۔ کم رن کے لیے صرف `exit` پائپ کریں: 3 درخواستیں / 900 ٹوکنز، لیکن اس سے تعاملی استنتاج کا استعمال نہیں ہوتا۔

**2. دونوں فنکشن کالنگ ورک فلو:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

دونوں فنکشن نام، مشابہ سیئٹل موسم، حساب شدہ نتیجہ 36، دو آخری جوابات، اور خروجی کوڈ 0 چیک کریں۔ بجٹ: **4 درخواستیں، زیادہ سے زیادہ 1,200 تکمیل ٹوکنز**۔

**3. دستاویز پر مبنی جواب:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

دستاویز کے راستے، مائیکروسافٹ اینٹرا آئی ڈی کا ذکر کرنے والا جواب، اور خروجی کوڈ 0 چیک کریں۔ بجٹ: **1 درخواست، زیادہ سے زیادہ 500 تکمیل ٹوکنز**۔ موجودہ [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) واحد مطلوبہ ان پٹ فائل ہے۔ ایک اختیاری دوسرا رن، جو غائب موضوع کے بارے میں پوچھتا ہے، پرہیز کرنا چاہئے اور اس میں ایک درخواست / 500 ٹوکنز کا اضافہ ہوتا ہے۔

**4. ذمہ دار AI مشاہدات:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

چھ زمروں اور مشاہداتی خلاصہ چیک کریں، تیار شدہ مواد کا جائزہ لیں، اور تکنیکی تکمیل کے لیے خروجی کوڈ 0 ضروری ہے۔ کامیاب پراسیس ایگزٹ ماڈل کی حفاظت کی تصدیق نہیں کرتا۔ بجٹ: **6 درخواستیں، زیادہ سے زیادہ 1,800 تکمیل ٹوکنز**۔

**چار کمانڈز کے لیے مجموعی: 16 چیٹ درخواستیں اور زیادہ سے زیادہ 5,400 تکمیل ٹوکنز،** ساتھ میں ان پٹ ٹوکنز (جس میں دہرائی گئی گفتگو اور ٹول اسکیمہ/تاریخ شامل ہیں)۔ ایمبیڈنگ کی کوئی درخواست نہیں ہے۔ حقیقی ٹوکن استعمال ماڈل پر مبنی ہے اور کم ہو سکتا ہے، خاص طور پر فلٹر کیے گئے پرامپٹس کے لیے۔ ڈالر کی قیمت تعیناتی کی قیمت پر منحصر ہے؛ کوئی مقررہ مالی تخمینہ نہیں دیا گیا۔ تمام درخواست کی حدیں دستی دوبارہ رنز کے بغیر فرض کی گئی ہیں۔ ہر کمانڈ کے فوراً بعد `$LASTEXITCODE` چیک کریں؛ غیرصفر کا مطلب ہے کہ رن کامیابی سے مکمل نہیں ہوا۔

## مسائل کا حل

- **اینڈ پوائنٹ غائب / 401 / 403:** لانچ کرنے کے عمل میں اینڈ پوائنٹ سیٹ کریں، اپنے مقامی Azure سائن ان اور وسائل پر مبنی کردار کی تصدیق کریں، اور غیر ارادی شناخت ماحول کی تبدیلیاں چیک کریں۔
- **400 / 404:** تصدیق کریں کہ تعیناتی موجود ہے اور استدلال کی کوشش `none` کے ساتھ چیٹ کمپلیشنز کی حمایت کرتی ہے۔ HTTPS ریسورس روٹ یا `/openai/v1` یو آر ایل استعمال کریں، پرانے تعیناتی یو آر ایل نہیں۔ عام 400 غلطیاں تکنیکی ناکامیاں ہیں، حفاظتی رکاوٹیں نہیں۔
- **429:** مشترکہ RPM اور ٹوکن کوٹا کو ہم آہنگ کریں پھر دوبارہ کوشش کریں۔ مثالیں جان بوجھ کر خودکار دوبارہ کوشش نہیں کرتیں۔
- **`Incomplete chat response: length`:** آؤٹ پٹ نے تکمیل کی حد کو پہنچا لیا۔ بڑھانے سے پہلے جواب اور پرامپٹ کا جائزہ لیں اور اس کی دستاویزی بجٹ؛ کٹا ہوا رن کامیاب کے طور پر ریکارڈ نہ کریں۔
- **فائل یا اسٹنڈ ان غلطیاں:** سپورٹڈ ڈائریکٹری سے لانچ کریں یا واضح دستاویز کا راستہ دیں۔ غیر خالی ریڈر سوال فراہم کریں۔ کمپلیشنز معمول کے مطابق EOF یا `exit` پر ختم ہو سکتے ہیں۔
- **کمپائلیشن کی غلطیاں:** جاوا 21 یا اس کے بعد کی تصدیق کریں، پھر `mvn -B -ntp clean test` چلائیں۔ پاورشیل میں، نقطہ دار پراپرٹی والے Maven آرگیومنٹ کو مکمل اقتباس میں رکھیں، مثال کے طور پر `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"۔

## اگلے اقدامات

جاری رکھیں [باب 4: عملی مثالیں](../04-PracticalSamples/README.md)۔

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ڈس کلیمر**:
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ جبکہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا عدم درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنے مادری زبان میں مستند ماخذ سمجھی جائے گی۔ حساس معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم قبول نہیں کرتے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->