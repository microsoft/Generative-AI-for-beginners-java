<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:31:42+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "ur"
}
-->
# جاوا کے لیے جنریٹو اے آئی کا ڈیولپمنٹ ماحول ترتیب دینا

> **جلدی شروع کریں**: کلاؤڈ میں کوڈنگ صرف 2 منٹ میں - [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) پر جائیں - کوئی مقامی انسٹالیشن کی ضرورت نہیں اور GitHub ماڈلز استعمال کریں!

> **Azure OpenAI میں دلچسپی رکھتے ہیں؟**، ہمارے [Azure OpenAI Setup Guide](getting-started-azure-openai.md) دیکھیں جس میں نیا Azure OpenAI ریسورس بنانے کے مراحل شامل ہیں۔

## آپ کیا سیکھیں گے

- اے آئی ایپلیکیشنز کے لیے جاوا ڈیولپمنٹ ماحول ترتیب دینا
- اپنی پسندیدہ ڈیولپمنٹ ماحول کا انتخاب اور ترتیب دینا (کلاؤڈ-فرسٹ Codespaces کے ساتھ، مقامی ڈیو کنٹینر، یا مکمل مقامی سیٹ اپ)
- GitHub ماڈلز سے جڑ کر اپنے سیٹ اپ کو ٹیسٹ کرنا

## مواد کی فہرست

- [آپ کیا سیکھیں گے](../../../02-SetupDevEnvironment)
- [تعارف](../../../02-SetupDevEnvironment)
- [مرحلہ 1: اپنا ڈیولپمنٹ ماحول ترتیب دیں](../../../02-SetupDevEnvironment)
  - [آپشن A: GitHub Codespaces (تجویز کردہ)](../../../02-SetupDevEnvironment)
  - [آپشن B: مقامی ڈیو کنٹینر](../../../02-SetupDevEnvironment)
  - [آپشن C: اپنی موجودہ مقامی انسٹالیشن استعمال کریں](../../../02-SetupDevEnvironment)
- [مرحلہ 2: GitHub پرسنل ایکسیس ٹوکن بنائیں](../../../02-SetupDevEnvironment)
- [مرحلہ 3: اپنے سیٹ اپ کو ٹیسٹ کریں](../../../02-SetupDevEnvironment)
- [مسائل کا حل](../../../02-SetupDevEnvironment)
- [خلاصہ](../../../02-SetupDevEnvironment)
- [اگلے مراحل](../../../02-SetupDevEnvironment)

## تعارف

یہ باب آپ کو ڈیولپمنٹ ماحول ترتیب دینے کے مراحل سے گزرے گا۔ ہم **GitHub ماڈلز** کو اپنی بنیادی مثال کے طور پر استعمال کریں گے کیونکہ یہ مفت ہے، صرف ایک GitHub اکاؤنٹ کے ساتھ آسانی سے ترتیب دیا جا سکتا ہے، کریڈٹ کارڈ کی ضرورت نہیں ہے، اور تجربات کے لیے متعدد ماڈلز تک رسائی فراہم کرتا ہے۔

**کوئی مقامی سیٹ اپ کی ضرورت نہیں!** آپ فوراً کوڈنگ شروع کر سکتے ہیں GitHub Codespaces استعمال کرتے ہوئے، جو آپ کے براؤزر میں مکمل ڈیولپمنٹ ماحول فراہم کرتا ہے۔

<img src="./images/models.webp" alt="اسکرین شاٹ: GitHub ماڈلز" width="50%">

ہم تجویز کرتے ہیں کہ اس کورس کے لیے [**GitHub ماڈلز**](https://github.com/marketplace?type=models) استعمال کریں کیونکہ یہ:
- **مفت** ہے شروع کرنے کے لیے
- **آسان** ہے صرف ایک GitHub اکاؤنٹ کے ساتھ ترتیب دینے کے لیے
- **کریڈٹ کارڈ** کی ضرورت نہیں
- **متعدد ماڈلز** تجربات کے لیے دستیاب ہیں

> **نوٹ**: اس تربیت میں استعمال ہونے والے GitHub ماڈلز کے مفت حدود یہ ہیں:
> - 15 درخواستیں فی منٹ (150 فی دن)
> - ~8,000 الفاظ اندر، ~4,000 الفاظ باہر فی درخواست
> - 5 بیک وقت درخواستیں
> 
> پروڈکشن استعمال کے لیے، Azure AI Foundry ماڈلز پر اپ گریڈ کریں اپنے Azure اکاؤنٹ کے ساتھ۔ آپ کے کوڈ کو تبدیل کرنے کی ضرورت نہیں۔ [Azure AI Foundry دستاویزات](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) دیکھیں۔

## مرحلہ 1: اپنا ڈیولپمنٹ ماحول ترتیب دیں

<a name="quick-start-cloud"></a>

ہم نے ایک پہلے سے ترتیب شدہ ڈیولپمنٹ کنٹینر بنایا ہے تاکہ سیٹ اپ کے وقت کو کم کیا جا سکے اور اس جنریٹو اے آئی فار جاوا کورس کے لیے تمام ضروری ٹولز فراہم کیے جا سکیں۔ اپنی پسندیدہ ڈیولپمنٹ اپروچ کا انتخاب کریں:

### ماحول ترتیب دینے کے اختیارات:

#### آپشن A: GitHub Codespaces (تجویز کردہ)

**2 منٹ میں کوڈنگ شروع کریں - کوئی مقامی سیٹ اپ کی ضرورت نہیں!**

1. اس ریپوزٹری کو اپنے GitHub اکاؤنٹ میں فورک کریں
   > **نوٹ**: اگر آپ بنیادی کنفیگریشن میں ترمیم کرنا چاہتے ہیں تو [Dev Container Configuration](../../../.devcontainer/devcontainer.json) دیکھیں
2. **Code** پر کلک کریں → **Codespaces** ٹیب → **...** → **New with options...**
3. ڈیفالٹس استعمال کریں – یہ **Dev container configuration** منتخب کرے گا: **Generative AI Java Development Environment** اس کورس کے لیے بنایا گیا کسٹم ڈیو کنٹینر
4. **Create codespace** پر کلک کریں
5. ماحول تیار ہونے کے لیے ~2 منٹ انتظار کریں
6. [مرحلہ 2: GitHub ٹوکن بنائیں](../../../02-SetupDevEnvironment) پر جائیں

<img src="./images/codespaces.png" alt="اسکرین شاٹ: Codespaces سب مینو" width="50%">

<img src="./images/image.png" alt="اسکرین شاٹ: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="اسکرین شاٹ: Create codespace options" width="50%">


> **Codespaces کے فوائد**:
> - کوئی مقامی انسٹالیشن کی ضرورت نہیں
> - کسی بھی ڈیوائس پر کام کرتا ہے جس میں براؤزر ہو
> - تمام ٹولز اور ڈیپینڈنسیز کے ساتھ پہلے سے ترتیب شدہ
> - ذاتی اکاؤنٹس کے لیے مفت 60 گھنٹے فی مہینہ
> - تمام سیکھنے والوں کے لیے مستقل ماحول

#### آپشن B: مقامی ڈیو کنٹینر

**ان ڈویلپرز کے لیے جو Docker کے ساتھ مقامی ڈیولپمنٹ کو ترجیح دیتے ہیں**

1. اس ریپوزٹری کو اپنے مقامی مشین پر فورک اور کلون کریں
   > **نوٹ**: اگر آپ بنیادی کنفیگریشن میں ترمیم کرنا چاہتے ہیں تو [Dev Container Configuration](../../../.devcontainer/devcontainer.json) دیکھیں
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) اور [VS Code](https://code.visualstudio.com/) انسٹال کریں
3. [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) کو VS Code میں انسٹال کریں
4. ریپوزٹری فولڈر کو VS Code میں کھولیں
5. جب پوچھا جائے، **Reopen in Container** پر کلک کریں (یا `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" استعمال کریں)
6. کنٹینر کے بننے اور شروع ہونے کا انتظار کریں
7. [مرحلہ 2: GitHub ٹوکن بنائیں](../../../02-SetupDevEnvironment) پر جائیں

<img src="./images/devcontainer.png" alt="اسکرین شاٹ: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="اسکرین شاٹ: Dev container build complete" width="50%">

#### آپشن C: اپنی موجودہ مقامی انسٹالیشن استعمال کریں

**ان ڈویلپرز کے لیے جن کے پاس پہلے سے موجود جاوا ماحول ہے**

ضروریات:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) یا آپ کا پسندیدہ IDE

مراحل:
1. اس ریپوزٹری کو اپنے مقامی مشین پر کلون کریں
2. پروجیکٹ کو اپنے IDE میں کھولیں
3. [مرحلہ 2: GitHub ٹوکن بنائیں](../../../02-SetupDevEnvironment) پر جائیں

> **پرو ٹپ**: اگر آپ کے پاس کم اسپیک مشین ہے لیکن آپ VS Code مقامی طور پر چاہتے ہیں، GitHub Codespaces استعمال کریں! آپ اپنے مقامی VS Code کو کلاؤڈ-ہوسٹڈ Codespace سے جوڑ سکتے ہیں بہترین تجربے کے لیے۔

<img src="./images/image-2.png" alt="اسکرین شاٹ: created local devcontainer instance" width="50%">


## مرحلہ 2: GitHub پرسنل ایکسیس ٹوکن بنائیں

1. [GitHub Settings](https://github.com/settings/profile) پر جائیں اور اپنے پروفائل مینو سے **Settings** منتخب کریں۔
2. بائیں سائڈبار میں، **Developer settings** پر کلک کریں (عام طور پر نیچے ہوتا ہے)۔
3. **Personal access tokens** کے تحت، **Fine-grained tokens** پر کلک کریں (یا اس [لنک](https://github.com/settings/personal-access-tokens) کو فالو کریں)۔
4. **Generate new token** پر کلک کریں۔
5. "Token name" کے تحت، ایک وضاحتی نام دیں (جیسے، `GenAI-Java-Course-Token`)۔
6. ایکسپائریشن تاریخ مقرر کریں (تجویز کردہ: سیکیورٹی کے بہترین اصولوں کے لیے 7 دن)۔
7. "Resource owner" کے تحت، اپنا یوزر اکاؤنٹ منتخب کریں۔
8. "Repository access" کے تحت، وہ ریپوزٹریز منتخب کریں جنہیں آپ GitHub ماڈلز کے ساتھ استعمال کرنا چاہتے ہیں (یا "All repositories" اگر ضرورت ہو)۔
9. "Repository permissions" کے تحت، **Models** تلاش کریں اور اسے **Read and write** پر سیٹ کریں۔
10. **Generate token** پر کلک کریں۔
11. **اپنا ٹوکن ابھی کاپی اور محفوظ کریں** – آپ اسے دوبارہ نہیں دیکھ سکیں گے!

> **سیکیورٹی ٹپ**: اپنے ایکسیس ٹوکنز کے لیے کم سے کم مطلوبہ اسکوپ اور سب سے مختصر عملی ایکسپائریشن وقت استعمال کریں۔

## مرحلہ 3: GitHub ماڈلز مثال کے ساتھ اپنے سیٹ اپ کو ٹیسٹ کریں

جب آپ کا ڈیولپمنٹ ماحول تیار ہو جائے، تو آئیے GitHub ماڈلز انٹیگریشن کو ہماری مثال ایپلیکیشن کے ساتھ ٹیسٹ کریں [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models)۔

1. اپنے ڈیولپمنٹ ماحول میں ٹرمینل کھولیں۔
2. GitHub ماڈلز مثال پر جائیں:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. اپنے GitHub ٹوکن کو ایک ماحول متغیر کے طور پر سیٹ کریں:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. ایپلیکیشن چلائیں:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

آپ کو اس طرح کا آؤٹ پٹ نظر آنا چاہیے:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### مثال کوڈ کو سمجھنا

پہلے، آئیے سمجھتے ہیں کہ ہم نے کیا چلایا۔ `examples/github-models` کے تحت مثال OpenAI Java SDK استعمال کرتی ہے GitHub ماڈلز سے جڑنے کے لیے:

**یہ کوڈ کیا کرتا ہے:**
- **جڑتا ہے** GitHub ماڈلز سے آپ کے پرسنل ایکسیس ٹوکن کا استعمال کرتے ہوئے
- **بھیجتا ہے** ایک سادہ "Say Hello World!" پیغام اے آئی ماڈل کو
- **وصول کرتا ہے** اور اے آئی کا جواب دکھاتا ہے
- **تصدیق کرتا ہے** کہ آپ کا سیٹ اپ صحیح کام کر رہا ہے

**اہم ڈیپینڈنسی** (`pom.xml` میں):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**مین کوڈ** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## خلاصہ

زبردست! آپ نے سب کچھ ترتیب دے لیا:

- GitHub پرسنل ایکسیس ٹوکن بنایا صحیح اجازتوں کے ساتھ اے آئی ماڈل تک رسائی کے لیے
- اپنا جاوا ڈیولپمنٹ ماحول چلایا (چاہے وہ Codespaces ہو، ڈیو کنٹینرز ہو، یا مقامی)
- GitHub ماڈلز سے جڑنے کے لیے OpenAI Java SDK استعمال کیا مفت اے آئی ڈیولپمنٹ کے لیے
- ایک سادہ مثال کے ساتھ ٹیسٹ کیا جو اے آئی ماڈلز سے بات کرتا ہے

## اگلے مراحل

[باب 3: بنیادی جنریٹو اے آئی تکنیکیں](../03-CoreGenerativeAITechniques/README.md)

## مسائل کا حل

مسائل ہو رہے ہیں؟ یہاں عام مسائل اور ان کے حل ہیں:

- **ٹوکن کام نہیں کر رہا؟** 
  - یقینی بنائیں کہ آپ نے پورا ٹوکن بغیر کسی اضافی اسپیس کے کاپی کیا ہے
  - تصدیق کریں کہ ٹوکن صحیح طور پر ماحول متغیر کے طور پر سیٹ کیا گیا ہے
  - چیک کریں کہ آپ کے ٹوکن کے پاس صحیح اجازتیں ہیں (Models: Read and write)

- **Maven نہیں ملا؟** 
  - اگر آپ ڈیو کنٹینرز/Codespaces استعمال کر رہے ہیں، تو Maven پہلے سے انسٹال ہونا چاہیے
  - مقامی سیٹ اپ کے لیے، یقینی بنائیں کہ Java 21+ اور Maven 3.9+ انسٹال ہیں
  - انسٹالیشن کی تصدیق کے لیے `mvn --version` آزمائیں

- **کنکشن مسائل؟** 
  - اپنا انٹرنیٹ کنکشن چیک کریں
  - تصدیق کریں کہ GitHub آپ کے نیٹ ورک سے قابل رسائی ہے
  - یقینی بنائیں کہ آپ کسی فائر وال کے پیچھے نہیں ہیں جو GitHub ماڈلز اینڈ پوائنٹ کو بلاک کر رہا ہو

- **ڈیو کنٹینر شروع نہیں ہو رہا؟** 
  - یقینی بنائیں کہ Docker Desktop چل رہا ہے (مقامی ڈیولپمنٹ کے لیے)
  - کنٹینر کو دوبارہ بنانے کی کوشش کریں: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **ایپلیکیشن کمپائل کرنے میں غلطیاں؟**
  - یقینی بنائیں کہ آپ صحیح ڈائریکٹری میں ہیں: `02-SetupDevEnvironment/examples/github-models`
  - صاف کرنے اور دوبارہ بنانے کی کوشش کریں: `mvn clean compile`

> **مدد چاہیے؟**: اب بھی مسائل ہو رہے ہیں؟ ریپوزٹری میں ایک مسئلہ کھولیں اور ہم آپ کی مدد کریں گے۔

**ڈسکلیمر**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کا استعمال کرتے ہوئے ترجمہ کی گئی ہے۔ ہم درستگی کے لیے کوشش کرتے ہیں، لیکن براہ کرم آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا غیر درستیاں ہو سکتی ہیں۔ اصل دستاویز کو اس کی اصل زبان میں مستند ذریعہ سمجھا جانا چاہیے۔ اہم معلومات کے لیے، پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ ہم اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کے ذمہ دار نہیں ہیں۔