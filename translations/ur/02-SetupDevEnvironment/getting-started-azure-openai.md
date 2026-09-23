# Azure AI Foundry کے لیے ڈیولپمنٹ ماحول سیٹ اپ کرنا

> اس گائیڈ میں **Azure AI Foundry** ماڈلز اس کورس کے جاوا AI ایپس کے لیے **keyless** تصدیق (Microsoft Entra ID) کا استعمال کرتے ہوئے سیٹ اپ کیے گئے ہیں — کوئی API کیز مینیج کرنے کی ضرورت نہیں۔ ٹولنگ میں نئے ہیں؟ [ڈیولپمنٹ ماحول گائیڈ](./README.md) سے شروع کریں۔

اس گائیڈ میں اس کورس کے جاوا AI ایپس کے لیے **Azure AI Foundry** ماڈلز سیٹ اپ کیے گئے ہیں۔ آپ کے پاس دو راستے ہیں:

- **اختیار A — `azd` + Bicep کے ساتھ پروویژن کرنا (تجویز کردہ):** ایک کمانڈ کے ذریعے Foundry اکاؤنٹ اور ماڈلز کو بطور کوڈ تعینات کرنا۔ پورٹل کے ذریعے کلک کرنے کی ضرورت نہیں۔
- **اختیار B — Azure AI Foundry پورٹل میں دستی طور پر وسائل تخلیق کریں۔**

دونوں راستے **keyless authentication** (Microsoft Entra ID) استعمال کرتے ہیں — کوئی API کیز نقل یا لیک نہیں ہوں گی۔

## فہرست مضامین

- [کیا تخلیق کیا جاتا ہے](#کیا-تخلیق-کیا-جاتا-ہے)
- [ضروریات](#ضروریات)
- [اختیار A: azd + Bicep کے ساتھ پروویژن کرنا (تجویز کردہ)](#option-a-provision-with-azd--bicep-recommended)
- [اختیار B: دستی طور پر وسائل تخلیق کرنا](#اختیار-b-وسائل-دستی-طور-پر-تخلیق-کریں)
- [اپنا ماحول ترتیب دیں](#اپنا-ماحول-ترتیب-دیں)
- [اپنے سیٹ اپ کا تجربہ کریں](#اپنے-سیٹ-اپ-کا-تجربہ-کریں)
- [اگلا کیا ہے؟](#اگلا-کیا-ہے؟)
- [وسائل](#وسائل)
- [اضافی وسائل](#اضافی-وسائل)

## کیا تخلیق کیا جاتا ہے

[`infra/`](../../../02-SetupDevEnvironment/infra) میں بیسیپ ٹیمپلیٹس مندرجہ ذیل پروویژن کرتے ہیں:

- ایک **Azure AI Foundry** اکاؤنٹ (`Microsoft.CognitiveServices/accounts`، قسم `AIServices`) جس میں ایک پراجیکٹ شامل ہے
- ایک **چیٹ** ڈپلائمنٹ - GPT-5.6 Luna (`gpt-5.6-luna`)، ورژن `2026-07-09`، `GlobalStandard` صلاحیت `10` کے ساتھ (اس ماڈل کے لیے 10 درخواستیں/منٹ اور 10,000 ٹوکنز/منٹ)
- ایک **ایمبیڈنگ** ڈپلائمنٹ - `text-embedding-3-small`، ورژن `1` (بعد کے ابواب میں استعمال ہوا)
- ایک **keyless رول اسائنمنٹ** (`Cognitive Services OpenAI User`) تاکہ آپ `az login` کے ساتھ سائن ان کر سکیں بجائے کلیدیں مینیج کرنے کے

## ضروریات

- ایک [Azure سبسکرپشن](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) اور [Maven 3.9+](https://maven.apache.org/download.cgi)

## اختیار A: azd + Bicep کے ساتھ پروویژن کرنا (تجویز کردہ)

`02-SetupDevEnvironment` فولڈر سے:

```bash
cd 02-SetupDevEnvironment

# سائن ان کریں (دونوں آلات)
azd auth login
az login

# فاؤنڈری اکاؤنٹ + ماڈل کی تعیناتی کی فراہمی کریں
azd up
```

`azd` آپ سے **ماحول کا نام** (مثلاً `genai-java`)، **سبسکرپشن**، اور **علاقہ** پوچھتا ہے۔ اپنی سبسکرپشن اور ایک ایسا علاقہ منتخب کریں جہاں `gpt-5.6-luna` اور `text-embedding-3-small` دستیاب ہوں، مثلاً `eastus2`۔ تصدیق کریں کہ سبسکرپشن کے پاس ماڈل اور ڈپلائمنٹ کی قسم کے لیے اس علاقے میں کافی کوٹہ موجود ہے؛ دستیابی اور کوٹہ سبسکرپشن کے لحاظ سے مختلف ہوتے ہیں۔

جب پروویژننگ مکمل ہو جائے تو azd:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) میں تعریف شدہ ہر چیز کو ڈپلائے کرتا ہے۔
2. ایک پوسٹ پروویژن ہک چلاتا ہے جو [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) فائل میں آپ کے اینڈپوائنٹ اور ڈپلائمنٹ کے نام لکھتا ہے (کوئی سیکرٹ نہیں)۔

> **مشورہ:** کسی بھی وقت `azd up` دوبارہ چلائیں تاکہ تبدیلیاں لاگو ہوں۔ `azd down` چلا کر سب کچھ حذف کریں اور لاگت سے بچیں۔

تیار شدہ ترتیبات دیکھنے کے لیے:

```bash
azd env get-values
```

اب [اپنے سیٹ اپ کا تجربہ کریں](#اپنے-سیٹ-اپ-کا-تجربہ-کریں) پر جائیں۔

## اختیار B: وسائل دستی طور پر تخلیق کریں

پورٹل کو ترجیح دیتے ہیں؟ وسائل کو اپنے ہاتھ سے بنائیں:

1. [Azure AI Foundry پورٹل](https://ai.azure.com/) پر جائیں اور سائن ان کریں۔
2. **پروجیکٹ تخلیق کریں** (اس سے ایک AI Foundry ریسورس بھی تخلیق ہوتا ہے)۔ نام ایسا دیں جیسے `GenAIJava`۔
3. اپنے پروجیکٹ میں، **Models + endpoints** → **Deploy model** → **Deploy base model** کھولیں۔
4. **GPT-5.6 Luna** تعینات کریں (ماڈل اور ڈپلائمنٹ کا نام `gpt-5.6-luna`، ورژن `2026-07-09`) کے ساتھ **Global Standard** صلاحیت `10`۔ اگر ایمبیڈنگ مثالیں چاہیے تو **text-embedding-3-small**، ورژن `1` بھی تعینات کریں۔
5. **Overview** سے **endpoint** کاپی کریں (مثلاً `https://<resource>.openai.azure.com/`)۔
6. خود کو keyless رسائی دیں: ریسورس پر، **Access control (IAM)** → **Add role assignment** → **Cognitive Services OpenAI User** کو اپنے اکاؤنٹ پر اسائن کریں۔

> **اب بھی مشکل ہے؟** [Azure AI Foundry دستاویزات](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) دیکھیں۔

## اپنا ماحول ترتیب دیں

**اگر آپ نے اختیار A (`azd up`) استعمال کیا ہے**، تو آپ کی ترتیبات کی فائل پہلے سے لکھی ہوئی ہے — کنفیگر کرنے کی ضرورت نہیں۔ [اپنے سیٹ اپ کا تجربہ کریں](#اپنے-سیٹ-اپ-کا-تجربہ-کریں) پر جائیں۔

**اگر آپ نے اختیار B (دستی) استعمال کیا ہے**، تو مثال کی `.env` فائل خود تخلیق کریں:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` کو اپنے اینڈپوائنٹ کے ساتھ ایڈٹ کریں (کوئی کی نہیں — تصدیق keyless ہے):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

ریسورس کا Azure OpenAI اینڈپوائنٹ استعمال کریں، پراجیکٹ کا URL نہیں۔ basic-chat ایپ اسے `/openai/v1` پر حل کرتی ہے اور مخصوص بیئرر ٹوکن کلائنٹ کنفیگر کرتی ہے؛ API کی کی ضرورت نہیں۔

> **سیکیورٹی نوٹ:** کوئی API کی اسٹور کرنے کی ضرورت نہیں۔ آپ Microsoft Entra ID کے ذریعے `az login` (لوکل) یا منیجڈ شناخت (Azure میں) سے تصدیق کرتے ہیں۔ `.env` فائل میں صرف غیر خفیہ ترتیبات ہوتی ہیں اور یہ پہلے ہی `.gitignore` میں شامل ہے۔

## اپنے سیٹ اپ کا تجربہ کریں

تصدیق کریں کہ آپ سائن ان ہیں تاکہ keyless تصدیق ٹوکن حاصل کر سکے، پھر مثال چلائیں:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # اگر آپ پہلے سے لاگ ان نہیں ہیں
mvn clean spring-boot:run
```

آپ کو `gpt-5.6-luna` ماڈل سے جواب نظر آئے گا۔ مثالیں ترتیب سے چلائیں تاکہ معمولی ڈیفالٹ کوٹہ کے اندر رہ سکیں؛ اگر آپ کو HTTP 429 موصول ہوتا ہے، تو دوبارہ کوشش کرنے سے پہلے انتظار کریں۔

> **VS Code صارفین:** چلانے کے لیے `F5` دبائیں۔ ایپ آپ کی `.env` خود بخود لوڈ کر لیتی ہے۔

> **مکمل مثال:** تفصیلات اور مسئلہ حل کے لیے [Basic Chat with Azure AI Foundry کی مثال](./examples/basic-chat-azure/README.md) دیکھیں۔

## اگلا کیا ہے؟

پروویژننگ اور کامیابی سے مثال چلانے کے بعد، آپ کے پاس ہوگا:
- Azure AI Foundry جس میں `gpt-5.6-luna` اور `text-embedding-3-small` تعینات ہوں
- Keyless authentication (Microsoft Entra ID) — کوئی کلید مینیج کرنے کی ضرورت نہیں
- ایک مقامی `.env` فولڈر جس میں آپ کا اینڈپوائنٹ اور ڈپلائمنٹ نام ہوں
- جاوا ڈیولپمنٹ ماحول تیار

**جاری رکھیں** [باب 3: بنیادی جنریٹو AI تکنیکیں](../03-CoreGenerativeAITechniques/README.md) پر جا کر AI ایپلیکیشنز بنانا شروع کریں!

## وسائل

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID کے ساتھ keyless authentication](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry دستاویزات](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 سے OpenAI Java SDK کی منتقلی](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 کے ساتھ سرکاری OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## اضافی وسائل

- [VS Code ڈاؤن لوڈ کریں](https://code.visualstudio.com/Download)
- [Docker Desktop حاصل کریں](https://www.docker.com/products/docker-desktop)
- [Dev Container کنفیگریشن](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ڈس کلیمر**:
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ جبکہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا عدم درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنے مادری زبان میں مستند ماخذ سمجھی جائے گی۔ حساس معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم قبول نہیں کرتے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->