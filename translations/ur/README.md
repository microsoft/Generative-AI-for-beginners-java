<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T04:59:42+00:00",
  "source_file": "README.md",
  "language_code": "ur"
}
-->
# جنریٹو AI ابتدائی افراد کے لیے - جاوا ایڈیشن
[![Microsoft Foundry ڈسکارڈ](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![جنریٹو AI ابتدائی افراد کے لیے - جاوا ایڈیشن](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ur.png)

**وقت کی پابندی**: پورا ورکشاپ آن لائن بغیر لوکل سیٹ اپ کے مکمل کیا جا سکتا ہے۔ ماحول کو ترتیب دینے میں 2 منٹ لگتے ہیں، نمونوں کو جانچنے میں 1-3 گھنٹے درکار ہو سکتے ہیں جس کا انحصار تلاش کی گہرائی پر ہے۔

> **جلدی آغاز** 

1. اس ریپوزیٹری کو اپنے GitHub اکاؤنٹ میں فورک کریں
2. پر کلک کریں **Code** → **Codespaces** ٹیب → **...** → **New with options...**
3. ڈیفالٹس استعمال کریں – یہ اس کورس کے لیے بنائے گئے ڈیولپمنٹ کنٹینر کو منتخب کرے گا
4. پر کلک کریں **Create codespace**
5. ماحول کے تیار ہونے تک تقریباً 2 منٹ انتظار کریں
6. براہِ راست [پہلا نمونہ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) پر جائیں

> **کیا آپ مقامی طور پر کلون کرنا پسند کریں گے؟**
>
> یہ ریپوزیٹری 50+ زبانوں کے تراجم شامل کرتی ہے جو ڈاؤن لوڈ کے حجم کو نمایاں طور پر بڑھا دیتی ہیں۔ بغیر تراجم کے کلون کرنے کے لیے sparse checkout استعمال کریں:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> یہ آپ کو کورس مکمل کرنے کے لیے ہر وہ چیز فراہم کرتا ہے جس کی آپ کو تیز تر ڈاؤن لوڈ کے ساتھ ضرورت ہے۔

## کثیر لسانی حمایت

### GitHub Action کے ذریعے معاونت (خودکار اور ہمیشہ تازہ کاری رہنے والا)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[عربی](../ar/README.md) | [بنگالی](../bn/README.md) | [بلغاریائی](../bg/README.md) | [برمی (میانمار)](../my/README.md) | [چینی (سادہ)](../zh/README.md) | [چینی (روایتی، ہانگ کانگ)](../hk/README.md) | [چینی (روایتی، مکاو)](../mo/README.md) | [چینی (روایتی، تائیوان)](../tw/README.md) | [کروشین](../hr/README.md) | [چیک](../cs/README.md) | [ڈینش](../da/README.md) | [ڈچ](../nl/README.md) | [ایسٹونین](../et/README.md) | [فنلندی](../fi/README.md) | [فرانسیسی](../fr/README.md) | [جرمن](../de/README.md) | [یونانی](../el/README.md) | [عبرانی](../he/README.md) | [ہندی](../hi/README.md) | [ہنگیرین](../hu/README.md) | [انڈونیشین](../id/README.md) | [اطالوی](../it/README.md) | [جاپانی](../ja/README.md) | [کنڑا](../kn/README.md) | [کوریائی](../ko/README.md) | [لتھوانیائی](../lt/README.md) | [ملائی](../ms/README.md) | [مالایالم](../ml/README.md) | [مراٹھی](../mr/README.md) | [نیپالی](../ne/README.md) | [نائجیریائی پجن](../pcm/README.md) | [ناروے](../no/README.md) | [فارسی (فارس)](../fa/README.md) | [پولش](../pl/README.md) | [پرتگالی (برازیل)](../br/README.md) | [پرتگالی (پرتگال)](../pt/README.md) | [پنجابی (گرمکھی)](../pa/README.md) | [رومانیائی](../ro/README.md) | [روسی](../ru/README.md) | [سربیائی (سریلیک)](../sr/README.md) | [سلوواکیائی](../sk/README.md) | [سلووینیائی](../sl/README.md) | [ہسپانوی](../es/README.md) | [سواحلی](../sw/README.md) | [سوئیڈش](../sv/README.md) | [ٹگالوگ (فلپائنی)](../tl/README.md) | [تمل](../ta/README.md) | [تیلگو](../te/README.md) | [تھائی](../th/README.md) | [ترکی](../tr/README.md) | [یوکرینیائی](../uk/README.md) | [اردو](./README.md) | [ویتنامی](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## کورس کا ڈھانچہ اور سیکھنے کا راستہ

### **باب 1: جنریٹو AI کا تعارف**
- **بنیادی تصورات**: بڑے لینگویج ماڈلز، ٹوکنز، ایمبیڈنگز، اور AI صلاحیتوں کی سمجھ
- **جاوا AI ایکو سسٹم**: Spring AI اور OpenAI SDKs کا جائزہ
- **ماڈل کانٹیکسٹ پروٹوکول**: MCP کا تعارف اور AI ایجنٹ مواصلات میں اس کا کردار
- **عملی اطلاقات**: حقیقی دنیا کے منظرنامے بشمول چیٹ بوٹس اور مواد کی تخلیق
- **[→ باب 1 شروع کریں](./01-IntroToGenAI/README.md)**

### **باب 2: ترقیاتی ماحول کی ترتیب**
- **ملٹی-پرووائیڈر کنفیگریشن**: GitHub Models، Azure OpenAI، اور OpenAI Java SDK انٹیگریشنز سیٹ کریں
- **Spring Boot + Spring AI**: انٹرپرائز AI ایپلیکیشن ڈویلپمنٹ کے بہترین طریقے
- **GitHub Models**: پروٹوٹائپنگ اور سیکھنے کے لیے مفت AI ماڈل تک رسائی (کریڈٹ کارڈ ضروری نہیں)
- **ڈویلپمنٹ ٹولز**: Docker کنٹینرز، VS Code، اور GitHub Codespaces کی ترتیب
- **[→ باب 2 شروع کریں](./02-SetupDevEnvironment/README.md)**

### **باب 3: بنیادی جنریٹو AI تکنیکیں**
- **پرومپٹ انجینئرنگ**: AI ماڈل کے بہترین ردِ عمل کے لیے تکنیکیں
- **ایمبیڈنگز اور ویکٹر آپریشنز**: سیمنٹک تلاش اور مماثلت میچنگ نافذ کریں
- **Retrieval-Augmented Generation (RAG)**: AI کو اپنے ڈیٹا ذرائع کے ساتھ ملائیں
- **فنکشن کالنگ**: کسٹم ٹولز اور پلگ انز کے ساتھ AI صلاحیتوں کو بڑھائیں
- **[→ باب 3 شروع کریں](./03-CoreGenerativeAITechniques/README.md)**

### **باب 4: عملی اطلاقات اور پروجیکٹس**
- **Pet Story Generator** (`petstory/`): GitHub Models کے ساتھ تخلیقی مواد کی تخلیق
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK کے ساتھ مقامی AI ماڈل انضمام
- **MCP Calculator Service** (`calculator/`): Spring AI کے ساتھ بنیادی Model Context Protocol کا نفاذ
- **[→ باب 4 شروع کریں](./04-PracticalSamples/README.md)**

### **باب 5: ذمہ دار AI ڈیولپمنٹ**
- **GitHub Models سیفٹی**: بلٹ ان مواد فلٹرنگ اور حفاظتی میکنزم (سخت بلاکس اور نرم انکار) کا ٹیسٹ کریں
- **Responsible AI Demo**: ایک عملی مثال جو دکھاتی ہے کہ جدید AI سیفٹی سسٹمز عملی طور پر کیسے کام کرتے ہیں
- **بہترین طریقے**: اخلاقی AI ڈویلپمنٹ اور تعیناتی کے لیے بنیادی رہنما اصول
- **[→ باب 5 شروع کریں](./05-ResponsibleGenAI/README.md)**

## مزید وسائل

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j ابتدائی افراد کے لیے](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js ابتدائی افراد کے لیے](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### ایزور / ایج / MCP / ایجنٹس
[![AZD ابتدائی افراد کے لیے](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI ابتدائی افراد کے لیے](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP ابتدائی افراد کے لیے](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ایجنٹس ابتدائی افراد کے لیے](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### جنریٹو AI سیریز
[![جنریٹو AI ابتدائی افراد کے لیے](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![جنریٹو AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![جنریٹو AI (جاوا)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![جنریٹو AI (جاوا اسکرپٹ)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### بنیادی سیکھنے
[![ML ابتدائی افراد کے لیے](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![ڈیٹا سائنس ابتدائی افراد کے لیے](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ابتدائی افراد کے لیے](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![سائبرسیکیوریٹی ابتدائی افراد کے لیے](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![ویب ڈویلپمنٹ ابتدائی افراد کے لیے](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT ابتدائیوں کے لیے](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR ڈیویلپمنٹ ابتدائیوں کے لیے](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot سیریز
[![Copilot برائے AI جوڑی پروگرامنگ](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot برائے C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot ایڈونچر](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## مدد حاصل کریں

اگر آپ پھنس جائیں یا AI ایپس بنانے کے بارے میں کوئی سوال ہو۔ MCP کے بارے میں مباحثوں میں ساتھی سیکھنے والوں اور تجربہ کار ڈویلپرز کے ساتھ شامل ہوں۔ یہ ایک معاون کمیونٹی ہے جہاں سوالات خوش آمدید ہیں اور علم آزادانہ طور پر بانٹا جاتا ہے۔

[![Microsoft Foundry ڈسکارڈ](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

اگر آپ کے پاس پروڈکٹ کا فیڈبیک ہو یا بنانے کے دوران غلطیاں پیش آئیں تو ملاحظہ کریں:

[![Microsoft Foundry ڈویلپر فورم](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
دستبرداری:
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کے ذریعے ترجمہ کی گئی ہے۔ اگرچہ ہم درستگی کے لیے کوشاں رہتے ہیں، براہِ کرم نوٹ کریں کہ خودکار تراجم میں غلطیاں یا بے ضابطگیاں ہو سکتی ہیں۔ اصل دستاویز اس کی مادری زبان میں ہی مستند ماخذ سمجھی جانی چاہیے۔ اہم معلومات کے لیے پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کے لیے ہم ذمہ دار نہیں ہیں۔
<!-- CO-OP TRANSLATOR DISCLAIMER END -->