<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T08:54:57+00:00",
  "source_file": "README.md",
  "language_code": "ur"
}
-->
# ابتدائیوں کے لیے جنریٹو AI - جاوا ایڈیشن
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ابتدائیوں کے لیے جنریٹو AI - جاوا ایڈیشن](../../translated_images/ur/beg-genai-series.8b48be9951cc574c.webp)

**وقت کی وابستگی**: پورا ورکشاپ آن لائن مکمل کیا جا سکتا ہے بغیر لوکل سیٹ اپ کے۔ ماحول کی ترتیب میں 2 منٹ لگتے ہیں، نمونوں کو دریافت کرنے میں 1-3 گھنٹے لگ سکتے ہیں، دریافت کی گہرائی پر منحصر ہے۔

> **جلدی آغاز**

1. اس ریپوزٹری کو اپنے GitHub اکاؤنٹ میں فورک کریں
2. **Code** → **Codespaces** ٹیب → **...** → **New with options...** پر کلک کریں
3. ڈیفالٹس استعمال کریں – اس سے اس کورس کے لیے تیار کردہ Development container منتخب ہو جائے گا
4. **Create codespace** پر کلک کریں
5. ماحول کے تیار ہونے کے لیے تقریباً 2 منٹ انتظار کریں
6. سیدھا [پہلے مثال](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) پر جائیں

> **لوکل کلوننگ کو ترجیح دیتے ہیں؟**
>
> اس ریپوزٹری میں 50+ زبانوں کے تراجم شامل ہیں جو ڈاؤن لوڈ سائز کو خاصا بڑھاتے ہیں۔ بغیر تراجم کے کلون کرنے کے لیے sparse checkout استعمال کریں:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> یہ آپ کو کورس مکمل کرنے کے لیے تمام ضروری چیزیں فراہم کرتا ہے اور ڈاؤن لوڈ بہت تیز ہوتا ہے۔

## کثیر لسانی حمایت

### GitHub Action کے ذریعے سپورٹ (خودکار اور ہمیشہ تازہ ترین)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[عربی](../ar/README.md) | [بنگالی](../bn/README.md) | [بلغاریائی](../bg/README.md) | [برمی (میانمار)](../my/README.md) | [چینی (سادہ)](../zh/README.md) | [چینی (روایتی، ہانگ کانگ)](../hk/README.md) | [چینی (روایتی، ماؤ)](../mo/README.md) | [چینی (روایتی، تائیوان)](../tw/README.md) | [کروشیائی](../hr/README.md) | [چیک](../cs/README.md) | [ڈینش](../da/README.md) | [ڈچ](../nl/README.md) | [ایسٹونین](../et/README.md) | [فنلش](../fi/README.md) | [فرانسیسی](../fr/README.md) | [جرمن](../de/README.md) | [یونانی](../el/README.md) | [عبرانی](../he/README.md) | [ہندی](../hi/README.md) | [ہنگیرین](../hu/README.md) | [انڈونیشین](../id/README.md) | [اطالوی](../it/README.md) | [جاپانی](../ja/README.md) | [کنڑا](../kn/README.md) | [کوریائی](../ko/README.md) | [لتھوانین](../lt/README.md) | [ملائی](../ms/README.md) | [مالایالم](../ml/README.md) | [مراٹھی](../mr/README.md) | [نیپالی](../ne/README.md) | [نائجیریائی پیدگن](../pcm/README.md) | [ناروے](../no/README.md) | [فارسی (فارس)](../fa/README.md) | [پولش](../pl/README.md) | [پرتگالی (برازیل)](../br/README.md) | [پرتگالی (پورٹوگال)](../pt/README.md) | [پنجابی (گرمکھی)](../pa/README.md) | [رومنین](../ro/README.md) | [روسی](../ru/README.md) | [سربیائی (سریلی)]](../sr/README.md) | [سلوواک](../sk/README.md) | [سلووینین](../sl/README.md) | [ہسپانوی](../es/README.md) | [سواحلی](../sw/README.md) | [سویڈش](../sv/README.md) | [ٹگالگ (فلپائنی)](../tl/README.md) | [تمل](../ta/README.md) | [تیلگو](../te/README.md) | [تھائی](../th/README.md) | [ترکی](../tr/README.md) | [یوکرینیائی](../uk/README.md) | [اردو](./README.md) | [ویتنامی](../vi/README.md)

> **لوکل کلوننگ کو ترجیح دیتے ہیں؟**

> اس ریپوزٹری میں 50+ زبانوں کے تراجم شامل ہیں جو ڈاؤن لوڈ سائز کو خاصا بڑھاتے ہیں۔ بغیر تراجم کے کلون کرنے کے لیے sparse checkout استعمال کریں:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> یہ آپ کو کورس مکمل کرنے کے لیے تمام ضروری چیزیں فراہم کرتا ہے اور ڈاؤن لوڈ بہت تیز ہوتا ہے۔
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## کورس کی ساخت اور سیکھنے کا راستہ

### **باب 1: جنریٹو AI کا تعارف**
- **بنیادی تصورات**: بڑی زبان کے ماڈلز، ٹوکنز، ایمبیڈنگز، اور AI کی صلاحیتوں کو سمجھنا
- **جاوا AI ماحولیاتی نظام**: Spring AI اور OpenAI SDKs کا جائزہ
- **ماڈل کانٹیکسٹ پروٹوکول**: MCP کا تعارف اور AI ایجنٹ کمیونیکیشن میں اس کا کردار
- **عملی اطلاقات**: حقیقی دنیا کے منظرنامے، بشمول چیٹ بوٹس اور مواد کی تخلیق
- **[→ باب 1 شروع کریں](./01-IntroToGenAI/README.md)**

### **باب 2: ترقیاتی ماحول کی ترتیب**
- **کثیر فراہم کنندہ کنفیگریشن**: GitHub ماڈلز، Azure OpenAI، اور OpenAI Java SDK انضمام کی سیٹ اپ
- **Spring Boot + Spring AI**: انٹرپرائز AI ایپلیکیشن ڈویلپمنٹ کے بہترین طریقے
- **GitHub ماڈلز**: پروٹو ٹائپنگ اور سیکھنے کے لیے مفت AI ماڈل تک رسائی (کریڈٹ کارڈ کی ضرورت نہیں)
- **ترقیاتی ٹولز**: Docker کنٹینرز، VS Code، اور GitHub Codespaces کی ترتیب
- **[→ باب 2 شروع کریں](./02-SetupDevEnvironment/README.md)**

### **باب 3: بنیادی جنریٹو AI تکنیکیں**
- **پرامپٹ انجینئرنگ**: AI ماڈل کے جوابات کے لیے بہترین تکنیکیں
- **ایمبیڈنگز اور ویکٹر آپریشنز**: سیمنٹک سرچ اور مماثلت کا نفاذ
- **ریٹریول آگمینٹڈ جنریشن (RAG)**: AI کو اپنی ڈیٹا سورسز کے ساتھ ملائیں
- **فنکشن کالنگ**: کسٹم ٹولز اور پلگ انز کے ساتھ AI صلاحیتوں کو بڑھائیں
- **[→ باب 3 شروع کریں](./03-CoreGenerativeAITechniques/README.md)**

### **باب 4: عملی اطلاقات اور منصوبے**
- **پالتو جانوروں کی کہانی بنانے والا** (`petstory/`): GitHub ماڈلز کے ساتھ تخلیقی مواد کی تخلیق
- **Foundry لوکل ڈیمو** (`foundrylocal/`): OpenAI Java SDK کے ساتھ مقامی AI ماڈل انضمام
- **MCP کیلکولیٹر سروس** (`calculator/`): Spring AI کے ساتھ بنیادی ماڈل کانٹیکسٹ پروٹوکول کا نفاذ
- **[→ باب 4 شروع کریں](./04-PracticalSamples/README.md)**

### **باب 5: ذمہ دار AI کی ترقی**
- **GitHub ماڈلز کی حفاظت**: بلٹ ان مواد کی فلٹرنگ اور سیفٹی مکینزم (ہارڈ بلاکس اور سوفٹ انکار) کی جانچ
- **ذمہ دار AI ڈیمو**: ایک عملی مثال دکھاتی ہے کہ جدید AI سیفٹی سسٹمز کس طرح کام کرتے ہیں
- **بہترین طریقے**: اخلاقی AI ترقی اور تعیناتی کے لیے ضروری رہنما اصول
- **[→ باب 5 شروع کریں](./05-ResponsibleGenAI/README.md)**

## اضافی وسائل

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j ابتدائیوں کے لیے](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js ابتدائیوں کے لیے](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / ایجنٹس
[![AZD ابتدائیوں کے لیے](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI ابتدائیوں کے لیے](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP ابتدائیوں کے لیے](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ایجنٹس ابتدائیوں کے لیے](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### جنریٹو AI سیریز
[![ابتدائیوں کے لیے جنریٹو AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![جنریٹو AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![جنریٹو AI (جاوا)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![جنریٹو AI (جاوااسکرپٹ)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---

### بنیادی تعلیم
[![ابتدائیوں کے لیے ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![ابتدائیوں کے لیے ڈیٹا سائنس](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![ابتدائیوں کے لیے AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![ابتدائیوں کے لیے سائبرسیکیورٹی](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![ابتدائیوں کے لیے ویب ڈویلپمنٹ](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### کاپائلٹ سیریز
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## مدد حاصل کرنا

اگر آپ پھنس جاتے ہیں یا AI ایپس بنانے کے بارے میں کوئی سوال ہے۔ دوسرے سیکھنے والوں اور تجربہ کار ڈویلپرز کے ساتھ MCP پر مباحثہ میں شامل ہوں۔ یہ ایک معاون کمیونٹی ہے جہاں سوالات کا خیرمقدم کیا جاتا ہے اور علم آزادانہ طور پر شیئر کیا جاتا ہے۔

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

اگر آپ کے پاس مصنوعات کے بارے میں تاثرات یا تعمیر کے دوران خامیاں ہیں تو یہاں جائیں:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ڈسکلیمر**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لئے کوشش کرتے ہیں، براہ کرم نوٹ کریں کہ خودکار ترجمے میں غلطیاں یا عدم مطابقت ہو سکتی ہے۔ اصل دستاویز اپنی مادری زبان میں مجاز ماخذ سمجھا جائے گا۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے ہونے والی کسی بھی غلط فہمی یا غلط تشریحات کی ذمہ داری ہم پر عائد نہیں ہوتی۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->