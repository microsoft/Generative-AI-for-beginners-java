<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T04:58:29+00:00",
  "source_file": "README.md",
  "language_code": "fa"
}
-->
# هوش مولد برای مبتدیان - نسخه جاوا
[![دیسکورد Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![هوش مولد برای مبتدیان - نسخه جاوا](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.fa.png)

**زمان مورد نیاز**: کل کارگاه را می‌توان به‌صورت آنلاین و بدون راه‌اندازی محلی انجام داد. راه‌اندازی محیط ۲ دقیقه طول می‌کشد و بررسی نمونه‌ها بسته به عمق کاوش بین ۱ تا ۳ ساعت زمان می‌برد.

> **شروع سریع** 

1. این مخزن را به حساب GitHub خود فورک کنید
2. روی **Code** → برگه **Codespaces** → **...** → **New with options...** کلیک کنید
3. از پیش‌فرض‌ها استفاده کنید – این کار کانتینری که برای این دوره ساخته شده را انتخاب می‌کند
4. روی **Create codespace** کلیک کنید
5. حدود ۲ دقیقه منتظر بمانید تا محیط آماده شود
6. مستقیماً به [اولین مثال](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) بروید

> **ترجیح می‌دهید محلی کلون کنید؟**
>
> این مخزن شامل بیش از ۵۰ ترجمه زبانی است که به‌طور قابل‌توجهی اندازه دانلود را افزایش می‌دهد. برای کلون کردن بدون ترجمه‌ها، از sparse checkout استفاده کنید:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> این به شما همه چیز مورد نیاز برای تکمیل دوره را با دانلود بسیار سریع‌تر می‌دهد.


## پشتیبانی چندزبانه

### پشتیبانی شده از طریق GitHub Action (خودکار و همیشه به‌روز)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[عربی](../ar/README.md) | [بنگالی](../bn/README.md) | [بلغاری](../bg/README.md) | [برمه‌ای (میانمار)](../my/README.md) | [چینی (ساده‌شده)](../zh/README.md) | [چینی (سنتی، هنگ‌کنگ)](../hk/README.md) | [چینی (سنتی، ماکائو)](../mo/README.md) | [چینی (سنتی، تایوان)](../tw/README.md) | [کرواسی](../hr/README.md) | [چک](../cs/README.md) | [دانمارکی](../da/README.md) | [هلندی](../nl/README.md) | [استونیایی](../et/README.md) | [فنلاندی](../fi/README.md) | [فرانسوی](../fr/README.md) | [آلمانی](../de/README.md) | [یونانی](../el/README.md) | [عبری](../he/README.md) | [هندی](../hi/README.md) | [مجارستانی](../hu/README.md) | [اندونزیایی](../id/README.md) | [ایتالیایی](../it/README.md) | [ژاپنی](../ja/README.md) | [کانّادا](../kn/README.md) | [کره‌ای](../ko/README.md) | [لیتوانیایی](../lt/README.md) | [مالایی](../ms/README.md) | [مالایالام](../ml/README.md) | [مراتی](../mr/README.md) | [نپالی](../ne/README.md) | [پیدج نیجریه‌ای](../pcm/README.md) | [نروژی](../no/README.md) | [فارسی (Farsi)](./README.md) | [لهستانی](../pl/README.md) | [پرتغالی (برزیل)](../br/README.md) | [پرتغالی (پرتغال)](../pt/README.md) | [پنجابی (Gurmukhi)](../pa/README.md) | [رومانیایی](../ro/README.md) | [روسی](../ru/README.md) | [صربی (سیریلیک)](../sr/README.md) | [اسلواکی](../sk/README.md) | [اسلوونیایی](../sl/README.md) | [اسپانیایی](../es/README.md) | [سواحیلی](../sw/README.md) | [سوئدی](../sv/README.md) | [تاگالوگ (فیلیپینی)](../tl/README.md) | [تامیل](../ta/README.md) | [تلوگو](../te/README.md) | [تایلندی](../th/README.md) | [ترکی](../tr/README.md) | [اوکراینی](../uk/README.md) | [اردو](../ur/README.md) | [ویتنامی](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ساختار دوره و مسیر یادگیری

### **فصل 1: مقدمه‌ای بر هوش مولد**
- **مفاهیم اصلی**: درک مدل‌های بزرگ زبانی، توکن‌ها، امبدینگ‌ها و توانایی‌های هوش مصنوعی
- **اکوسیستم هوش مصنوعی جاوا**: مرور کلی بر Spring AI و SDKهای OpenAI
- **پروتکل بافت مدل**: معرفی MCP و نقش آن در ارتباطات عامل‌های هوش مصنوعی
- **کاربردهای عملی**: سناریوهای دنیای واقعی از جمله چت‌بات‌ها و تولید محتوا
- **[→ شروع فصل 1](./01-IntroToGenAI/README.md)**

### **فصل 2: راه‌اندازی محیط توسعه**
- **پیکربندی چند ارائه‌دهنده**: راه‌اندازی GitHub Models، Azure OpenAI و یکپارچه‌سازی‌های OpenAI Java SDK
- **Spring Boot + Spring AI**: بهترین شیوه‌ها برای توسعه برنامه‌های سازمانی مبتنی بر هوش مصنوعی
- **GitHub Models**: دسترسی رایگان به مدل‌های هوش مصنوعی برای نمونه‌سازی و یادگیری (نیاز به کارت اعتباری ندارد)
- **ابزارهای توسعه**: کانتینرهای Docker، VS Code و پیکربندی GitHub Codespaces
- **[→ شروع فصل 2](./02-SetupDevEnvironment/README.md)**

### **فصل 3: تکنیک‌های اصلی هوش مولد**
- **مهندسی پرامپت**: تکنیک‌هایی برای دریافت پاسخ‌های بهینه از مدل‌های هوش مصنوعی
- **امبدینگ‌ها و عملیات برداری**: پیاده‌سازی جستجوی معنایی و تطبیق شباهت
- **تولید تقویت‌شده با بازیابی (RAG)**: ترکیب هوش مصنوعی با منابع داده‌ای خودتان
- **فراخوانی توابع**: گسترش توانایی‌های هوش مصنوعی با ابزارها و پلاگین‌های سفارشی
- **[→ شروع فصل 3](./03-CoreGenerativeAITechniques/README.md)**

### **فصل 4: برنامه‌ها و پروژه‌های عملی**
- **تولیدکننده داستان حیوانات خانگی** (`petstory/`): تولید محتوای خلاقانه با GitHub Models
- **دموی محلی Foundry** (`foundrylocal/`): یکپارچه‌سازی مدل محلی با OpenAI Java SDK
- **سرویس ماشین‌حساب MCP** (`calculator/`): پیاده‌سازی پایه‌ای پروتکل بافت مدل با Spring AI
- **[→ شروع فصل 4](./04-PracticalSamples/README.md)**

### **فصل 5: توسعه مسئولانه هوش مصنوعی**
- **ایمنی GitHub Models**: آزمایش فیلترینگ محتوای داخلی و مکانیزم‌های ایمنی (مسدودسازی سخت و امتناع نرم)
- **دموی هوش مصنوعی مسئولانه**: مثال عملی که نشان می‌دهد سیستم‌های ایمنی مدرن چگونه در عمل کار می‌کنند
- **بهترین شیوه‌ها**: دستورالعمل‌های ضروری برای توسعه و استقرار اخلاقی هوش مصنوعی
- **[→ شروع فصل 5](./05-ResponsibleGenAI/README.md)**

## منابع اضافی

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j برای مبتدیان](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js برای مبتدیان](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / عامل‌ها
[![AZD برای مبتدیان](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI برای مبتدیان](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP برای مبتدیان](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![عامل‌های AI برای مبتدیان](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### مجموعه هوش مولد
[![هوش مولد برای مبتدیان](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![هوش مولد (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![هوش مولد (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![هوش مولد (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### یادگیری پایه
[![یادگیری ماشین برای مبتدیان](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![علم داده برای مبتدیان](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![هوش مصنوعی برای مبتدیان](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![امنیت سایبری برای مبتدیان](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![توسعه وب برای مبتدیان](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![اینترنت اشیاء برای مبتدیان](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![توسعه XR برای مبتدیان](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### مجموعه Copilot
[![Copilot برای برنامه‌نویسی جفتی با هوش مصنوعی](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot برای C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![ماجراجویی Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## دریافت کمک

اگر در ساخت برنامه‌های هوش مصنوعی به مشکل برخوردید یا سوالی داشتید، به بحث‌ها با دیگر یادگیرندگان و توسعه‌دهندگان باتجربه در مورد MCP بپیوندید. این یک جامعهٔ حمایت‌کننده است که در آن سوالات پذیرفته می‌شوند و دانش به‌صورت آزاد به اشتراک گذاشته می‌شود.

[![دیسکورد Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

اگر هنگام ساخت بازخوردی دربارهٔ محصول یا خطا مشاهده کردید، به:

[![انجمن توسعه‌دهندگان Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
سلب مسئولیت:
این سند با استفاده از سرویس ترجمهٔ هوش مصنوعی [Co-op Translator](https://github.com/Azure/co-op-translator) ترجمه شده است. در حالی که ما در تلاش برای دقت هستیم، لطفاً توجه داشته باشید که ترجمه‌های خودکار ممکن است شامل خطاها یا نادرستی‌هایی باشند. سند اصلی به زبان مبدأ باید به‌عنوان منبع معتبر در نظر گرفته شود. برای اطلاعات حیاتی، ترجمهٔ حرفه‌ای توسط مترجم انسانی توصیه می‌شود. ما در قبال هرگونه سوءتفاهم یا تفسیر نادرست ناشی از استفاده از این ترجمه مسئولیتی نداریم.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->