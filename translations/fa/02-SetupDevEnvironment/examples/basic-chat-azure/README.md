# چت پایه با Azure AI Foundry - نمونه انتها به انتها

این مثال یک برنامه ساده Spring Boot است که به مدل **Azure AI Foundry** با استفاده از **احراز هویت بدون کلید** (Microsoft Entra ID) متصل می‌شود و تنظیمات شما را آزمایش می‌کند. این برنامه از `ChatClient` در Spring AI استفاده می‌کند که توسط **کتابخانه OpenAI Java رسمی** و نقطه پایانی **Azure OpenAI v1** پشتیبانی می‌شود.

نسخه‌های موجود در [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) عبارتند از Spring Boot **4.1.1**، Spring AI **2.0.1**، OpenAI Java **4.63.1**، Azure Identity **1.18.6**، و dotenv-java **3.2.0**. نمونه از `spring-ai-starter-model-openai` استفاده می‌کند و به طور صریح `openai-java` و `azure-identity` را اعلام می‌کند؛ در Spring AI 2، استارتر قدیمی Azure OpenAI حذف شده است.

## فهرست مطالب

- [پیش‌نیازها](#پیش‌نیازها)
- [شروع سریع](#شروع-سریع)
- [نحوه کار احراز هویت](#نحوه-کار-احراز-هویت)
- [اجرای برنامه](#اجرای-برنامه)
  - [استفاده از Maven](#استفاده-از-maven)
  - [استفاده از VS Code](#استفاده-از-vs-code)
  - [خروجی مورد انتظار](#خروجی-مورد-انتظار)
- [رفرنس پیکربندی](#رفرنس-پیکربندی)
  - [متغیرهای محیطی](#متغیرهای-محیطی)
  - [پیکربندی Spring](#پیکربندی-spring)
- [عیب‌یابی](#عیب‌یابی)
  - [مشکلات متداول](#مشکلات-متداول)
  - [حالت دیباگ](#حالت-دیباگ)
- [گام‌های بعدی](#گام‌های-بعدی)
- [منابع](#منابع)

## پیش‌نیازها

قبل از اجرای این مثال، اطمینان حاصل کنید که:

- یک منبع Azure AI Foundry با یک استقرار `gpt-5.6-luna` دارید - آن را با `azd up` فراهم کنید یا به صورت دستی از طریق [راهنمای راه‌اندازی Azure AI Foundry](../../getting-started-azure-openai.md)
- نقش **کاربر سرویس‌های شناختی OpenAI** روی آن منبع را دارید (قالب‌های Bicep این نقش را برای شما تخصیص می‌دهند)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli) را دارید و با `az login` وارد شده‌اید
- Java 21+ و Maven 3.9+ نصب شده

> **نیازی به کلید API نیست** — احراز هویت بدون کلید از طریق Microsoft Entra ID است.

## شروع سریع

```bash
# ۱. به پروژه بروید
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# ۲. وارد شوید تا احراز هویت بدون کلید بتواند توکن دریافت کند
az login

# ۳. نقطه پایانی را پیکربندی کنید
#    - اگر دستور `azd up` را اجرا کرده‌اید، فایل .env برای شما نوشته شده است (این مرحله را رد کنید).
#    - در غیر این صورت، قالب را کپی کرده و AZURE_OPENAI_ENDPOINT را تنظیم کنید:
cp .env.example .env

# ۴. برنامه را اجرا کنید
mvn spring-boot:run
```

## نحوه کار احراز هویت

این مثال با **Microsoft Entra ID** احراز هویت می‌کند — کلید API وجود ندارد.

برنامه به صورت صریح احراز هویت را در [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) پیکربندی می‌کند:

1. `azureCredential()` یک `BearerTokenCredential` ایجاد می‌کند که با استفاده از `AuthenticationUtil.getBearerTokenSupplier` همراه با `DefaultAzureCredential` و دامنه `https://ai.azure.com/.default` ساخته شده است.
2. `azureOpenAiClient()` یک `OpenAIClient` با `OpenAIOkHttpClient.builder()` می‌سازد، نقطه پایانی منبع را به `/openai/v1` حل می‌کند و اعتبارنامه توکن را با `.credential(...)` تامین می‌کند.
3. `azureChatModel()` آن کلاینت را به `OpenAiChatModel` در Spring AI تامین می‌کند که پشت `ChatClient` این درس قرار دارد.

این بن‌های صریح مانع از آن می‌شوند که یک `OPENAI_API_KEY` جهانی احراز هویت Azure را بازنویسی کند. کنار گذاشتن کلید API فقط از YAML به تنهایی، تنظیم احراز هویت نیست. `DefaultAzureCredential` می‌تواند از جلسه `az login` شما به صورت محلی یا شناسه مدیریت شده در Azure استفاده کند؛ هر هویتی که انتخاب می‌شود باید نقش منبع ذکر شده را داشته باشد.

## اجرای برنامه

### استفاده از Maven

```bash
mvn spring-boot:run
```

### استفاده از VS Code

1. پروژه را در VS Code باز کنید
2. کلید `F5` را فشار دهید یا از پنل "Run and Debug" استفاده کنید
3. پیکربندی "Spring Boot-BasicChatApplication" را انتخاب کنید

> **توجه**: برنامه فایل `.env` را از دایرکتوری کاری خود بارگیری می‌کند، حتی زمانی که از VS Code اجرا می‌شود.

### خروجی مورد انتظار

خروجی نمونه پس از اجرای موفق (لاگ‌های راه‌اندازی حذف شده‌اند؛ عبارت پاسخ متغیر است):

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

## رفرنس پیکربندی

### متغیرهای محیطی

| متغیر | توضیح | لازم است | مثال |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | آدرس نقطه پایانی Foundry (Azure OpenAI) | بله | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | نام استقرار مدل چت | خیر | `gpt-5.6-luna` (پیش‌فرض) |

> متغیر کلید API **وجود ندارد** — احراز هویت بدون کلید است (Microsoft Entra ID از طریق `az login`).

### پیکربندی Spring

تنظیمات در [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) از پیشوند `spring.ai.openai` و ویژگی‌های چت تخت استفاده می‌کنند (هیچ بلوک `options` نیست):

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

`model` نام **استقرار Azure** است. احراز هویت از بن‌های صریح شرح داده شده در بالا می‌آید، نه از تنظیم `api-key`. این درس استدلال را غیرفعال کرده و توکن‌های تکمیل را در ۵۰۰ محدود می‌کند؛ `temperature` و `max-tokens` قدیمی تنظیم نشده‌اند.

مایکروسافت استفاده از [کتابخانه OpenAI رسمی با Azure OpenAI v1 و API پاسخ‌ها را برای برنامه‌های جدید توصیه می‌کند](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). چت کامپلیشن‌ها همچنان برای این درس مبتنی بر پیام پشتیبانی می‌شوند. برای GPT-5.6، درخواست‌هایی که شامل ابزارها روی چت کامپلیشن‌ها هستند باید `reasoning_effort` را روی `none` تنظیم کنند؛ هنگام ترکیب استدلال با ابزارها از پاسخ‌ها استفاده کنید. به [فراخوانی ابزار با مدل‌های استدلالی](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models) مراجعه کنید.

## عیب‌یابی

### مشکلات متداول

<details>
<summary><strong>خطا: 401 / "PermissionDenied" / خطاهای توکن</strong></summary>

- اجرای `az login` — احراز هویت بدون کلید نیاز به ورود فعال برای دریافت توکن دارد
- اطمینان حاصل کنید حساب کاربری شما نقش **کاربر سرویس‌های شناختی OpenAI** روی منبع دارد
- اگر به تازگی نقش را تخصیص داده‌اید، چند دقیقه صبر کنید تا اعمال شود
- تایید کنید در اجاره‌دار/اشتراک مناسب هستید (`az account show`)
</details>

<details>
<summary><strong>خطا: "نقطه پایانی معتبر نیست" / خطاهای اتصال</strong></summary>

- اطمینان حاصل کنید `AZURE_OPENAI_ENDPOINT` یک URL پایه کامل است (مثلاً `https://your-resource.openai.azure.com/`)
- بررسی سازگاری اسلش انتهایی
- تایید کنید نقطه پایانی با منبع فراهم شده شما مطابقت دارد (`azd env get-values`)
</details>

<details>
<summary><strong>خطا: "استقرار یافت نشد"</strong></summary>

- بررسی کنید که `AZURE_OPENAI_DEPLOYMENT` با نام استقرار در Azure مطابقت دارد
- اطمینان حاصل کنید مدل با موفقیت مستقر و فعال است
- نام پیش‌فرض استقرار `gpt-5.6-luna` است
</details>

<details>
<summary><strong>خطا: 429 / محدودیت نرخ عبور شده است</strong></summary>

- استقرار پیش‌فرض GPT-5.6 Luna دارای ظرفیت Global Standard 10 است: ۱۰ درخواست در دقیقه و ۱۰,۰۰۰ توکن در دقیقه
- نمونه‌ها را به صورت متوالی اجرا کنید و قبل از تلاش مجدد منتظر فاصله دوباره‌گذاری سرویس باشید
- این مثال پایه دوباره تلاش خودکار SDK را غیرفعال کرده، بنابراین درخواست ناموفق به طور مستقیم گزارش می‌شود
</details>

<details>
<summary><strong>VS Code: بارگذاری نشدن متغیرهای محیطی</strong></summary>

- اطمینان حاصل کنید فایل `.env` در شاخه ریشه پروژه قرار دارد (همسطح با `pom.xml`)
- تلاش کنید `mvn spring-boot:run` را در ترمینال یکپارچه VS Code اجرا کنید
- بررسی کنید افزونه جاوا در VS Code به درستی نصب شده باشد
</details>

### حالت دیباگ

برای فعال کردن لاگ‌گیری دقیق، این خط‌ها را در [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) از حالت کامنت خارج کنید:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## گام‌های بعدی

**تنظیمات کامل شد!** سفر یادگیری خود را ادامه دهید:

[فصل ۳: تکنیک‌های اصلی هوش مصنوعی مولد](../../../03-CoreGenerativeAITechniques/README.md)

## منابع

- [انتقال Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [کتابخانه رسمی OpenAI Java با Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [احراز هویت بدون کلید با Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [پرتال Azure AI Foundry](https://ai.azure.com/)
- [مستندات Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوء تفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->