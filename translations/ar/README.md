<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T08:51:58+00:00",
  "source_file": "README.md",
  "language_code": "ar"
}
-->
# الذكاء الاصطناعي التوليدي للمبتدئين - إصدار جافا
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![الذكاء الاصطناعي التوليدي للمبتدئين - إصدار جافا](../../translated_images/ar/beg-genai-series.8b48be9951cc574c.webp)

**مدة الالتزام**: يمكن إتمام ورشة العمل بأكملها عبر الإنترنت دون تثبيت محلي. يستغرق إعداد البيئة دقيقتين، مع استكشاف العينات الذي يتطلب من 1 إلى 3 ساعات حسب عمق الاستكشاف.

> **بدء سريع**

1. قم بعمل فورك لهذا المستودع إلى حسابك على GitHub
2. اضغط على **Code** → علامة التبويب **Codespaces** → **...** → **New with options...**
3. استخدم الإعدادات الافتراضية – هذا سيختار حاوية التطوير التي تم إنشاؤها لهذه الدورة
4. اضغط على **Create codespace**
5. انتظر حوالي دقيقتين حتى تكون البيئة جاهزة
6. انتقل مباشرة إلى [المثال الأول](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **هل تفضل الاستنساخ محليًا؟**
>
> يتضمن هذا المستودع أكثر من 50 ترجمة للغات مما يزيد بشكل كبير من حجم التنزيل. للاستنساخ بدون الترجمات، استخدم الاستنساخ الجزئي:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> هذا يمنحك كل ما تحتاجه لإكمال الدورة بتنزيل أسرع بكثير.

## دعم متعدد اللغات

### مدعوم عبر GitHub Action (مؤتمت ودائم التحديث)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[العربية](./README.md) | [البنغالية](../bn/README.md) | [البلغارية](../bg/README.md) | [البورمية (ميانمار)](../my/README.md) | [الصينية (المبسطة)](../zh/README.md) | [الصينية (التقليدية، هونغ كونغ)](../hk/README.md) | [الصينية (التقليدية، ماكاو)](../mo/README.md) | [الصينية (التقليدية، تايوان)](../tw/README.md) | [الكرواتية](../hr/README.md) | [التشيكية](../cs/README.md) | [الدانمركية](../da/README.md) | [الهولندية](../nl/README.md) | [الإستونية](../et/README.md) | [الفنلندية](../fi/README.md) | [الفرنسية](../fr/README.md) | [الألمانية](../de/README.md) | [اليونانية](../el/README.md) | [العبرية](../he/README.md) | [الهندية](../hi/README.md) | [الهنغارية](../hu/README.md) | [الإندونيسية](../id/README.md) | [الإيطالية](../it/README.md) | [اليابانية](../ja/README.md) | [الكنادية](../kn/README.md) | [الكورية](../ko/README.md) | [الليتوانية](../lt/README.md) | [الماليزية](../ms/README.md) | [المالايالامية](../ml/README.md) | [الماراثية](../mr/README.md) | [النيبالية](../ne/README.md) | [بيبغة نيجيرية](../pcm/README.md) | [النرويجية](../no/README.md) | [الفارسية (اللغة الفارسية)](../fa/README.md) | [البولندية](../pl/README.md) | [البرتغالية (البرازيل)](../br/README.md) | [البرتغالية (البرتغال)](../pt/README.md) | [البنجابية (غورموخي)](../pa/README.md) | [الرومانية](../ro/README.md) | [الروسية](../ru/README.md) | [الصربية (السيريلية)](../sr/README.md) | [السلوفاكية](../sk/README.md) | [السلوفينية](../sl/README.md) | [الإسبانية](../es/README.md) | [السواحيلية](../sw/README.md) | [السويدية](../sv/README.md) | [التاغالوغ (الفلبينية)](../tl/README.md) | [التاميلية](../ta/README.md) | [التيلوجو](../te/README.md) | [التايلاندية](../th/README.md) | [التركية](../tr/README.md) | [الأوكرانية](../uk/README.md) | [الأردية](../ur/README.md) | [الفيتنامية](../vi/README.md)

> **هل تفضل الاستنساخ محليًا؟**

> يتضمن هذا المستودع أكثر من 50 ترجمة للغات مما يزيد بشكل كبير من حجم التنزيل. للاستنساخ بدون الترجمات، استخدم الاستنساخ الجزئي:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> هذا يمنحك كل ما تحتاجه لإكمال الدورة بتنزيل أسرع بكثير.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## هيكل الدورة ومسار التعلم

### **الفصل 1: مقدمة في الذكاء الاصطناعي التوليدي**
- **المفاهيم الأساسية**: فهم نماذج اللغات الكبيرة، الرموز المميزة، التضمينات، وقدرات الذكاء الاصطناعي
- **بيئة الذكاء الاصطناعي في جافا**: نظرة عامة على Spring AI و OpenAI SDKs
- **بروتوكول سياق النموذج**: مقدمة عن MCP ودوره في تواصل وكلاء الذكاء الاصطناعي
- **تطبيقات عملية**: سيناريوهات واقعية تشمل الدردشات وتوليد المحتوى
- **[→ بدء الفصل 1](./01-IntroToGenAI/README.md)**

### **الفصل 2: إعداد بيئة التطوير**
- **تكوين مزود متعدد**: إعداد نماذج GitHub، Azure OpenAI، وتكاملات OpenAI Java SDK
- **Spring Boot + Spring AI**: أفضل الممارسات لتطوير تطبيقات الذكاء الاصطناعي للمؤسسات
- **نماذج GitHub**: وصول مجاني لنماذج الذكاء الاصطناعي للنمذجة الأولية والتعلم (لا تحتاج بطاقة ائتمان)
- **أدوات التطوير**: حاويات Docker، VS Code، وتكوين GitHub Codespaces
- **[→ بدء الفصل 2](./02-SetupDevEnvironment/README.md)**

### **الفصل 3: تقنيات الذكاء الاصطناعي التوليدي الأساسية**
- **هندسة المطالبات**: تقنيات لتحقيق أفضل استجابات لنماذج الذكاء الاصطناعي
- **التضمينات وعمليات المتجهات**: تطبيق البحث الدلالي ومطابقة التشابه
- **التوليد المعزز بالاسترجاع (RAG)**: دمج الذكاء الاصطناعي مع مصادر بياناتك الخاصة
- **استدعاء الدوال**: توسيع قدرات الذكاء الاصطناعي بأدوات وإضافات مخصصة
- **[→ بدء الفصل 3](./03-CoreGenerativeAITechniques/README.md)**

### **الفصل 4: التطبيقات العملية والمشاريع**
- **مولد قصص الحيوانات الأليفة** (`petstory/`): توليد محتوى إبداعي باستخدام نماذج GitHub
- **عرض Foundry محلي** (`foundrylocal/`): دمج نموذج الذكاء الاصطناعي المحلي مع OpenAI Java SDK
- **خدمة آلة حاسبة MCP** (`calculator/`): تطبيق أساسي لبروتوكول سياق النموذج مع Spring AI
- **[→ بدء الفصل 4](./04-PracticalSamples/README.md)**

### **الفصل 5: تطوير الذكاء الاصطناعي المسؤول**
- **أمان نماذج GitHub**: اختبار آليات تصفية المحتوى والسلامة المدمجة (الكتل الصارمة والرفض اللين)
- **عرض للذكاء الاصطناعي المسؤول**: مثال عملي يوضح كيف تعمل أنظمة الأمان الحديثة في الذكاء الاصطناعي
- **أفضل الممارسات**: إرشادات أساسية لتطوير ونشر الذكاء الاصطناعي الأخلاقي
- **[→ بدء الفصل 5](./05-ResponsibleGenAI/README.md)**

## موارد إضافية

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j للمبتدئين](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js للمبتدئين](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD للمبتدئين](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI للمبتدئين](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP للمبتدئين](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![وكلاء الذكاء الاصطناعي للمبتدئين](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### سلسلة الذكاء الاصطناعي التوليدي
[![الذكاء الاصطناعي التوليدي للمبتدئين](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي التوليدي (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي التوليدي (جافا)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي التوليدي (جافا سكريبت)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### التعلم الأساسي
[![تعلم الآلة للمبتدئين](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![علم البيانات للمبتدئين](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![الذكاء الاصطناعي للمبتدئين](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![الأمن السيبراني للمبتدئين](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![تطوير الويب للمبتدئين](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![إنترنت الأشياء للمبتدئين](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![تطوير الواقع الممتد للمبتدئين](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### سلسلة كوبايلوت
[![كوبايلوت للبرمجة الزوجية بالذكاء الاصطناعي](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![كوبايلوت لـ C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![مغامرات كوبايلوت](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## الحصول على المساعدة

إذا واجهت صعوبة أو كان لديك أي أسئلة حول بناء تطبيقات الذكاء الاصطناعي. انضم إلى المتعلمين الآخرين والمطورين ذوي الخبرة في مناقشات حول MCP. إنها مجتمع داعم حيث الأسئلة مرحب بها والمعرفة تُشارك بحرية.

[![مايكروسوفت فاوندري ديسكورد](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

إذا كان لديك ملاحظات عن المنتج أو أخطاء أثناء البناء قم بزيارة:

[![منتدى مطوري مايكروسوفت فاوندري](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**تنويه**:  
تمت ترجمة هذا المستند باستخدام خدمة الترجمة الآلية [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو عدم دقة. يجب اعتبار النسخة الأصلية من المستند بلغتها الأصلية المصدر المرجعي والصحيح. بالنسبة للمعلومات الحرجة، يُنصح بالاستعانة بترجمة بشرية محترفة. نحن غير مسؤولين عن أي سوء فهم أو تفسير خاطئ ناتج عن استخدام هذه الترجمة.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->