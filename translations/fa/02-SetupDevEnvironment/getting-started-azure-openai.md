# راه‌اندازی محیط توسعه برای Azure AI Foundry

> این راهنما مدل‌های **Azure AI Foundry** را برای برنامه‌های هوش مصنوعی جاوا در این دوره با استفاده از احراز هویت **بدون کلید** (Microsoft Entra ID) راه‌اندازی می‌کند — نیازی به مدیریت کلیدهای API ندارید. تازه‌کار با ابزارها؟ با [راهنمای محیط توسعه](./README.md) شروع کنید.

این راهنما مدل‌های **Azure AI Foundry** را برای برنامه‌های هوش مصنوعی جاوا در این دوره راه‌اندازی می‌کند. شما دو مسیر دارید:

- **گزینه A — تهیه با `azd` + Bicep (توصیه شده):** یک دستور حساب Foundry و مدل‌ها را به عنوان کد مستقر می‌کند. بدون کلیک در پورتال.
- **گزینه B — ایجاد منابع به صورت دستی** در پورتال Azure AI Foundry.

هر دو مسیر از **احراز هویت بدون کلید** (Microsoft Entra ID) استفاده می‌کنند — نیازی به کپی یا نشت کلید API نیست.

## فهرست مطالب

- [چه چیزهایی ایجاد می‌شود](#چه-چیزهایی-ایجاد-می‌شود)
- [پیش‌نیازها](#پیش‌نیازها)
- [گزینه A: تهیه با azd + Bicep (توصیه شده)](#option-a-provision-with-azd--bicep-recommended)
- [گزینه B: ایجاد منابع به صورت دستی](#گزینه-b-ایجاد-منابع-به-صورت-دستی)
- [پیکربندی محیط خود](#پیکربندی-محیط-خود)
- [آزمایش تنظیمات خود](#آزمایش-تنظیمات-خود)
- [بعدی چیست؟](#بعدی-چیست؟)
- [منابع](#منابع)
- [منابع اضافی](#منابع-اضافی)

## چه چیزهایی ایجاد می‌شود

قالب‌های Bicep داخل [`infra/`](../../../02-SetupDevEnvironment/infra) موارد زیر را تهیه می‌کنند:

- یک حساب **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`، نوع `AIServices`) با یک پروژه
- یک استقرار **چت** - GPT-5.6 Luna (`gpt-5.6-luna`)، نسخه `2026-07-09`، با ظرفیت `GlobalStandard` برابر با `10` (10 درخواست در دقیقه و 10,000 توکن در دقیقه برای این مدل)
- یک استقرار **embedding** - `text-embedding-3-small`، نسخه `1` (در فصول بعدی استفاده می‌شود)
- یک **اختصاص نقش بدون کلید** (`Cognitive Services OpenAI User`) بنابراین شما با `az login` وارد می‌شوید به جای مدیریت کلیدها

## پیش‌نیازها

- یک [اشتراک Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [جاوا 21+](https://learn.microsoft.com/java/openjdk/download) و [Maven 3.9+](https://maven.apache.org/download.cgi)

## گزینه A: تهیه با azd + Bicep (توصیه شده)

از پوشه `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# ورود به سیستم (هر دو ابزار)
azd auth login
az login

# فراهم‌سازی حساب Foundry + استقرار مدل‌ها
azd up
```

`azd` از شما برای **نام محیط** (مثلاً `genai-java`)، **اشتراک** و **منطقه** درخواست می‌کند. اشتراک خود و منطقه‌ای را انتخاب کنید که مدل‌های `gpt-5.6-luna` و `text-embedding-3-small` در آنجا در دسترس باشند، مثلاً `eastus2`. اطمینان حاصل کنید که اشتراک شما در آن منطقه سهمیه کافی برای مدل و نوع استقرار دارد؛ دسترسی و سهمیه بسته به اشتراک متفاوت است.

هنگام پایان تهیه، azd:

1. هر چیزی را که در [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) تعریف شده مستقر می‌کند.
2. اجرای یک هوک پس از تهیه که فایل [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) را با نام‌های نقطه پایانی و استقرار شما می‌نویسد (بدون اطلاعات محرمانه).

> **نکته:** هر زمان برای اعمال تغییرات دوباره دستور `azd up` را اجرا کنید. برای حذف همه و توقف هزینه‌ها `azd down` را اجرا کنید.

برای مشاهده تنظیمات تولیدشده:

```bash
azd env get-values
```

حال به بخش [آزمایش تنظیمات خود](#آزمایش-تنظیمات-خود) بروید.

## گزینه B: ایجاد منابع به صورت دستی

ترجیح می‌دهید از پورتال استفاده کنید؟ منابع را دستی ایجاد کنید:

1. به [پورتال Azure AI Foundry](https://ai.azure.com/) بروید و وارد شوید.
2. **یک پروژه ایجاد کنید** (این همچنین یک منبع AI Foundry ایجاد می‌کند). به آن نامی مانند `GenAIJava` بدهید.
3. در پروژه خود، به **مدل‌ها + نقاط انتهایی** → **استقرار مدل** → **استقرار مدل پایه** بروید.
4. **GPT-5.6 Luna** را مستقر کنید (نام مدل و استقرار `gpt-5.6-luna`، نسخه `2026-07-09`) با ظرفیت **Global Standard** برابر با `10`. اگر می‌خواهید مثال‌های embedding را داشته باشید، همین کار را برای **text-embedding-3-small**، نسخه `1` تکرار کنید.
5. از **نمای کلی**، **نقطه پایان** را کپی کنید (مثلاً `https://<resource>.openai.azure.com/`).
6. به خودتان دسترسی بدون کلید بدهید: روی منبع، **کنترل دسترسی (IAM)** → **افزودن تخصیص نقش** را باز کنید → نقش **Cognitive Services OpenAI User** را به حساب خود اختصاص دهید.

> **هنوز مشکل دارید؟** به [مستندات Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) مراجعه کنید.

## پیکربندی محیط خود

**اگر از گزینه A (`azd up`) استفاده کرده‌اید**، فایل تنظیمات شما قبلاً نوشته شده است — نیازی به پیکربندی نیست. به [آزمایش تنظیمات خود](#آزمایش-تنظیمات-خود) بروید.

**اگر از گزینه B (دستی) استفاده کرده‌اید**، فایل `.env` مثال را خودتان ایجاد کنید:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

فایل `.env` را با نقطه پایان خود ویرایش کنید (بدون کلید — احراز هویت بدون کلید است):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

از نقطه پایان Azure OpenAI منبع استفاده کنید، نه URL پروژه. برنامه basic-chat آن را به `/openai/v1` تبدیل می‌کند و یک کلاینت توکن دار صریح تنظیم می‌کند؛ کلید API لازم نیست.

> **یادداشت امنیتی:** کلید API برای ذخیره وجود ندارد. شما با Microsoft Entra ID از طریق `az login` (محلی) یا هویت مدیریت شده (در Azure) احراز هویت می‌کنید. فایل `.env` فقط تنظیمات غیر محرمانه را دربر می‌گیرد و توسط `.gitignore` پوشش داده شده است.

## آزمایش تنظیمات خود

اطمینان حاصل کنید که وارد شده‌اید تا احراز هویت بدون کلید بتواند توکن بگیرد، سپس مثال را اجرا کنید:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # اگر قبلاً وارد نشده‌اید
mvn clean spring-boot:run
```

باید پاسخ مدل `gpt-5.6-luna` را ببینید. مثال‌ها را به ترتیب اجرا کنید تا در داخل سهمیه کم پیش‌فرض بمانید؛ اگر با HTTP 429 مواجه شدید، قبل از تلاش مجدد صبر کنید.

> **کاربران VS Code:** برای اجرا `F5` را فشار دهید. برنامه فایل `.env` شما را به‌طور خودکار بارگذاری می‌کند.

> **مثال کامل:** برای جزئیات و عیب‌یابی به [مثال Basic Chat با Azure AI Foundry](./examples/basic-chat-azure/README.md) مراجعه کنید.

## بعدی چیست؟

پس از تهیه و اجرای موفق مثال، شما خواهید داشت:
- Azure AI Foundry با استقرار `gpt-5.6-luna` و `text-embedding-3-small`
- احراز هویت بدون کلید (Microsoft Entra ID) — بدون کلید برای مدیریت
- یک فایل `.env` محلی با نام‌های نقطه پایان و استقرار شما
- یک محیط توسعه جاوا آماده استفاده

**ادامه دهید به** [فصل ۳: تکنیک‌های اصلی AI مولد](../03-CoreGenerativeAITechniques/README.md) برای شروع ساخت برنامه‌های هوش مصنوعی!

## منابع

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [احراز هویت بدون کلید با Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [مستندات Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [انتقال Spring AI 2 به OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK رسمی OpenAI Java با Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## منابع اضافی

- [دانلود VS Code](https://code.visualstudio.com/Download)
- [دریافت Docker Desktop](https://www.docker.com/products/docker-desktop)
- [پیکربندی Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**سلب مسئولیت**:
این سند با استفاده از سرویس ترجمه هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مادری خود باید به عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمه حرفه‌ای انسانی توصیه می‌شود. ما در قبال هرگونه سوء تفاهم یا برداشت نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->