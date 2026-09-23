# بنیادی چیٹ ازور AI فاؤنڈری کے ساتھ - انتہا تا انتہا مثال

یہ مثال ایک سادہ اسپرنگ بوٹ ایپلیکیشن ہے جو **Azure AI Foundry** ماڈل سے **کلید کے بغیر توثیق** (Microsoft Entra ID) کے ذریعے جڑتی ہے اور آپ کے سیٹ اپ کو ٹیسٹ کرتی ہے۔ یہ Spring AI کے `ChatClient` کو رکھتی ہے، جس کے پیچھے **عصری OpenAI Java SDK** اور **Azure OpenAI v1** اینڈ پوائنٹ ہے۔

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) میں ورژنز Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, اور dotenv-java **3.2.0** ہیں۔ یہ سیمپل `spring-ai-starter-model-openai` استعمال کرتا ہے اور واضح طور پر `openai-java` اور `azure-identity` کا اعلان کرتا ہے؛ Spring AI 2 نے پرانا Azure OpenAI اسٹارٹر ہٹا دیا ہے۔

## فہرست مضامین

- [ضروریات](#ضروریات)
- [جلدی آغاز](#جلدی-آغاز)
- [توثیق کیسے کام کرتی ہے](#توثیق-کیسے-کام-کرتی-ہے)
- [ایپلیکیشن چلانا](#ایپلیکیشن-چلانا)
  - [Maven کا استعمال](#maven-کا-استعمال-کرتے-ہوئے)
  - [VS Code کا استعمال](#vs-code-کا-استعمال-کرتے-ہوئے)
  - [متوقع نتیجہ](#متوقع-نتیجہ)
- [ترتیب کا حوالہ](#ترتیب-کا-حوالہ)
  - [ماحولیاتی متغیرات](#ماحولیاتی-متغیرات)
  - [اسپرنگ ترتیب](#اسپرنگ-ترتیب)
- [مسائل کا حل](#مسائل-کا-حل)
  - [عام مسائل](#عام-مسائل)
  - [ڈی بگ موڈ](#ڈی-بگ-موڈ)
- [اگلے اقدامات](#اگلے-اقدامات)
- [وسائل](#وسائل)

## ضروریات

اس مثال کو چلانے سے پہلے یقینی بنائیں کہ آپ کے پاس:

- ایک Azure AI Foundry ریسورس جس میں `gpt-5.6-luna` تعیناتی ہو - اسے `azd up` کے ذریعے فراہم کریں یا دستی طور پر [Azure AI Foundry سیٹ اپ گائیڈ](../../getting-started-azure-openai.md) کے ذریعے
- اس ریسورس پر **Cognitive Services OpenAI User** کا کردار (Bicep ٹیمپلیٹس آپ کے لیے یہ سیٹ کرتے ہیں)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)، جس میں `az login` کے ذریعے سائن ان ہو
- Java 21+ اور Maven 3.9+

> **کوئی API کلید درکار نہیں** — توثیق Microsoft Entra ID کے ذریعے بغیر کلید کے ہے۔

## جلدی آغاز

```bash
# 1. منصوبے پر جائیں
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. سائن ان کریں تاکہ کی لیس آتھ ٹوکن حاصل کر سکے
az login

# 3. اینڈپوائنٹ ترتیب دیں
#    - اگر آپ نے `azd up` چلایا ہے، تو .env آپ کے لیے لکھا گیا ہے (اسے چھوڑ دیں)۔
#    - بصورت دیگر ٹیمپلیٹ کو کاپی کریں اور AZURE_OPENAI_ENDPOINT سیٹ کریں:
cp .env.example .env

# 4. ایپلیکیشن چلائیں
mvn spring-boot:run
```

## توثیق کیسے کام کرتی ہے

یہ مثال **Microsoft Entra ID** کے ساتھ توثیق کرتی ہے — کوئی API کلید نہیں۔

ایپلیکیشن [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) میں توثیق کو واضح طور پر کنفیگر کرتی ہے:

1. `azureCredential()` `BearerTokenCredential` بناتی ہے جو `AuthenticationUtil.getBearerTokenSupplier` کے ساتھ `DefaultAzureCredential` اور `https://ai.azure.com/.default` اسکوپ استعمال کرتی ہے۔
2. `azureOpenAiClient()` `OpenAIClient` بناتا ہے `OpenAIOkHttpClient.builder()` سے، ریسورس اینڈ پوائنٹ کو `/openai/v1` پر حل کرتا ہے، اور `.credential(...)` کے ساتھ بیئر کریڈینشل فراہم کرتا ہے۔
3. `azureChatModel()` اس کلائنٹ کو Spring AI کے `OpenAiChatModel` کو فراہم کرتا ہے، جو سبق کے `ChatClient` کی پشت پناہی کرتا ہے۔

یہ واضح بینز ایک عالمی `OPENAI_API_KEY` کو Azure توثیق کو اوور رائڈ کرنے سے روکتے ہیں۔ YAML سے صرف API کلید کو چھوڑنا توثیق سیٹ اپ نہیں ہے۔ `DefaultAzureCredential` آپ کی لوکل `az login` سیشن یا Azure میں مینیجڈ شناخت استعمال کر سکتا ہے؛ منتخب شدہ شناخت کے پاس اوپر دی گئی ریسورس رول ہونا لازمی ہے۔

## ایپلیکیشن چلانا

### Maven کا استعمال کرتے ہوئے

```bash
mvn spring-boot:run
```

### VS Code کا استعمال کرتے ہوئے

1. VS Code میں پروجیکٹ کھولیں
2. `F5` دبائیں یا "Run and Debug" پینل استعمال کریں
3. "Spring Boot-BasicChatApplication" کنفیگریشن منتخب کریں

> **نوٹ**: ایپلیکیشن اپنے ورکنگ ڈائریکٹری سے `.env` لوڈ کرتی ہے، بشمول جب VS Code سے چلائی جائے۔

### متوقع نتیجہ

کامیاب چلانے کے بعد مثالی آؤٹ پٹ (اسٹارٹ اپ لاگز شامل نہیں؛ جواب کے الفاظ مختلف ہو سکتے ہیں):

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

## ترتیب کا حوالہ

### ماحولیاتی متغیرات

| متغیر | وضاحت | ضروری | مثال |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) اینڈ پوائنٹ URL | ہاں | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | چیٹ ماڈل کی تعیناتی کا نام | نہیں | `gpt-5.6-luna` (ڈیفالٹ) |

> کوئی API کلید متغیر نہیں ہے — توثیق کلید کے بغیر ہے (Microsoft Entra ID کے ذریعے `az login`).

### اسپرنگ ترتیب

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) کے سیٹنگز `spring.ai.openai` پریفکس اور سیدھی چیٹ پراپرٹیز استعمال کرتے ہیں (کوئی `options` بلاک نہیں):

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

`model` **Azure تعیناتی کا نام** ہے۔ توثیق اوپر بیان شدہ واضح بینز سے آتی ہے، نہ کہ `api-key` سیٹنگ سے۔ سبق میں استدلال کو غیر فعال کیا گیا ہے اور تکمیل کے ٹوکنز کی حد 500 مقرر کی گئی ہے؛ `temperature` اور پرانے `max-tokens` کو چھوڑ دیا گیا ہے۔

Microsoft تجویز کرتا ہے کہ [نئی ایپلیکیشنز کے لیے Azure OpenAI v1 اور Responses API کے ساتھ official OpenAI SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) استعمال کریں۔ چیٹ کمپلیشنز اس پیغام پر مبنی سبق کے لیے اب بھی مدد یافتہ ہے۔ GPT-5.6 کے لیے، درخواستیں جو ٹولز کے ساتھ چیٹ کمپلیشنز استعمال کرتی ہیں انہیں `reasoning_effort` کو `none` پر سیٹ کرنا ہوگا؛ جب ٹولز کے ساتھ استدلال ملایا جائے تو Responses استعمال کریں۔ دیکھیں [reasoning ماڈلز کے ساتھ ٹول کالنگ](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)۔

## مسائل کا حل

### عام مسائل

<details>
<summary><strong>غلطی: 401 / "PermissionDenied" / ٹوکن کی غلطیاں</strong></summary>

- `az login` چلائیں — کلید کے بغیر توثیق کے لیے ایک فعال سائن ان ضروری ہے
- تصدیق کریں کہ آپ کے اکاؤنٹ پر ریسورس کے لیے **Cognitive Services OpenAI User** کا کردار ہے
- اگر آپ نے ابھی کردار تفویض کیا ہے تو اسے نافذ ہونے کے لیے ایک منٹ انتظار کریں
- تصدیق کریں کہ آپ صحیح ٹیننٹ/سبسکرپشن میں ہیں (`az account show`)
</details>

<details>
<summary><strong>غلطی: "اینڈ پوائنٹ درست نہیں ہے" / کنکشن کی غلطیاں</strong></summary>

- یقینی بنائیں کہ `AZURE_OPENAI_ENDPOINT` مکمل بنیادی URL ہے (مثلاً `https://your-resource.openai.azure.com/`)
- جھکی ہوئی سلیش کی مطابقت چیک کریں
- تصدیق کریں کہ اینڈ پوائنٹ فراہم کردہ ریسورس سے میل کھاتا ہے (`azd env get-values`)
</details>

<details>
<summary><strong>غلطی: "تعیناتی نہیں ملی"</strong></summary>

- تصدیق کریں کہ `AZURE_OPENAI_DEPLOYMENT` Azure میں کسی تعیناتی کے نام سے میل کھاتا ہے
- چیک کریں کہ ماڈل کامیابی سے تعینات اور فعال ہے
- ڈیفالٹ تعیناتی کا نام `gpt-5.6-luna` ہے
</details>

<details>
<summary><strong>غلطی: 429 / حد سے زیادہ درخواستیں</strong></summary>

- ڈیفالٹ GPT-5.6 Luna تعیناتی میں Global Standard صلاحیت 10 ہے: 10 درخواستیں فی منٹ اور 10,000 ٹوکنز فی منٹ
- مثالوں کو ترتیب وار چلائیں اور دوبارہ کوشش کرنے سے پہلے سروس کے انتظار کے وقفے کا انتظار کریں
- یہ بنیادی مثال خود کار SDK ریٹریز کو غیر فعال کرتی ہے، اس لیے ناکام درخواست فوراً رپورٹ ہوتی ہے
</details>

<details>
<summary><strong>VS Code: ماحولیاتی متغیرات لوڈ نہیں ہو رہے</strong></summary>

- یقینی بنائیں کہ آپ کی `.env` فائل پروجیکٹ کی روٹ ڈائریکٹری میں ہے (جہاں `pom.xml` ہے)
- VS Code کے انٹیگریٹڈ ٹرمینل میں `mvn spring-boot:run` چلانے کی کوشش کریں
- چیک کریں کہ VS Code Java ایکسٹینشن صحیح طریقے سے انسٹال ہے
</details>

### ڈی بگ موڈ

تفصیلی لاگنگ کو فعال کرنے کے لیے [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) میں ان لائنز کو ان کمنٹ کریں:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## اگلے اقدامات

**سیٹ اپ مکمل!** اپنی سیکھنے کی راہ جاری رکھیں:

[باب 3: بنیادی جنریٹو AI تکنیکیں](../../../03-CoreGenerativeAITechniques/README.md)

## وسائل

- [Spring AI 2 OpenAI Java SDK منتقلی](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [عصری OpenAI Java SDK Azure OpenAI v1 کے ساتھ](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID کے ساتھ کلید کے بغیر توثیق](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry پورٹل](https://ai.azure.com/)
- [Azure AI Foundry دستاویزات](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ڈس کلیمر**:
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ جبکہ ہم درستگی کے لیے کوشاں ہیں، براہ کرم اس بات سے آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا عدم درستیاں ہو سکتی ہیں۔ اصل دستاویز اپنے مادری زبان میں مستند ماخذ سمجھی جائے گی۔ حساس معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کی ذمہ داری ہم قبول نہیں کرتے۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->