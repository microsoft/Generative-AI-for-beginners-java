<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:13:24+00:00",
  "source_file": "README.md",
  "language_code": "he"
}
-->
# בינה גנרטיבית למתחילים - מהדורת Java
[![Discord של Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![בינה גנרטיבית למתחילים - מהדורת Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.he.png)

**משך זמן משוער**: כל הסדנה ניתנת להשלמה באינטרנט ללא צורך בהתקנה מקומית. הגדרת הסביבה לוקחת כ-2 דקות, וחקר הדוגמאות ידרוש בין 1-3 שעות בהתאם לעומק החקירה.

> **התחלה מהירה** 

1. בצע Fork של מאגר זה אל חשבון ה-GitHub שלך
2. לחץ על **Code** → לשונית **Codespaces** → **...** → **New with options...**
3. השתמש בערכי ברירת המחדל – זה יבחר את מכולת הפיתוח שנוצרה לקורס זה
4. לחץ על **Create codespace**
5. המתן כ-2 דקות עד שהסביבה תהיה מוכנה
6. עבור ישירות אל [הדוגמה הראשונה](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **מעדיף לשכפל מקומית?**
>
> מאגר זה כולל מעל 50 תרגומים לשפות שונות, מה שמגדיל משמעותית את גודל ההורדה. כדי לשכפל בלי התרגומים, השתמש ב-sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> זה נותן לך את כל מה שאתה צריך כדי להשלים את הקורס עם הורדה הרבה יותר מהירה.


## תמיכה ברב-שפות

### נתמך באמצעות GitHub Action (אוטומטי ותמיד מעודכן)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ערבית](../ar/README.md) | [בנגלית](../bn/README.md) | [בולגרית](../bg/README.md) | [בורמזית (מיאנמר)](../my/README.md) | [סינית (מפושטת)](../zh/README.md) | [סינית (מסורתית, הונג קונג)](../hk/README.md) | [סינית (מסורתית, מקאו)](../mo/README.md) | [סינית (מסורתית, טייוואן)](../tw/README.md) | [קרואטית](../hr/README.md) | [צ'כית](../cs/README.md) | [דנית](../da/README.md) | [הולנדית](../nl/README.md) | [אסטונית](../et/README.md) | [פינית](../fi/README.md) | [צרפתית](../fr/README.md) | [גרמנית](../de/README.md) | [יוונית](../el/README.md) | [עברית](./README.md) | [הינדי](../hi/README.md) | [הונגרית](../hu/README.md) | [אינדונזית](../id/README.md) | [איטלקית](../it/README.md) | [יפנית](../ja/README.md) | [קאנאדה](../kn/README.md) | [קוריאנית](../ko/README.md) | [ליטאית](../lt/README.md) | [מלאית](../ms/README.md) | [מאליאלאם](../ml/README.md) | [מראטי](../mr/README.md) | [נפאלית](../ne/README.md) | [נייג'ריאן פיג'ין](../pcm/README.md) | [נורווגית](../no/README.md) | [פרסית (פארסי)](../fa/README.md) | [פולנית](../pl/README.md) | [פורטוגזית (ברזיל)](../br/README.md) | [פורטוגזית (פורטוגל)](../pt/README.md) | [פנג'אבי (גורמוקי)](../pa/README.md) | [רומנית](../ro/README.md) | [רוסית](../ru/README.md) | [סרבית (קירילית)](../sr/README.md) | [סלובקית](../sk/README.md) | [סלובנית](../sl/README.md) | [ספרדית](../es/README.md) | [סווהילי](../sw/README.md) | [שוודית](../sv/README.md) | [טגלוג (פיליפינית)](../tl/README.md) | [טמילית](../ta/README.md) | [טלוגו](../te/README.md) | [תאית](../th/README.md) | [טורקית](../tr/README.md) | [אוקראינית](../uk/README.md) | [אורדו](../ur/README.md) | [וייטנאמית](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## מבנה הקורס ונתיב הלמידה

### **פרק 1: מבוא לבינה גנרטיבית**
- **מושגי ליבה**: הבנת מודלים לשוניים גדולים, טוקנים, הטמעות, ויכולות של בינה מלאכותית
- **אקוסיסטם ה-AI ב-Java**: סקירה של Spring AI ו-SDKs של OpenAI
- **פרוטוקול הקשר של המודל**: מבוא ל-MCP ותפקידו בתקשורת סוכני AI
- **יישומים מעשיים**: תרחישים במציאות, כולל צ'אטבוטים ויצירת תוכן
- **[→ התחל פרק 1](./01-IntroToGenAI/README.md)**

### **פרק 2: הגדרת סביבת הפיתוח**
- **תצורה רב-ספקית**: הגדרת GitHub Models, Azure OpenAI, ושילובים עם OpenAI Java SDK
- **Spring Boot + Spring AI**: שיטות מומלצות לפיתוח אפליקציות AI ארגוניות
- **GitHub Models**: גישה חינמית לדגמי AI לפרוטוטייפינג ולמידה (אין צורך בכרטיס אשראי)
- **כלי פיתוח**: מכולות Docker, VS Code, ותצורת GitHub Codespaces
- **[→ התחל פרק 2](./02-SetupDevEnvironment/README.md)**

### **פרק 3: טכניקות מרכזיות בבינה גנרטיבית**
- **הנדסת פרומפטים**: טכניקות לקבלת תגובות מיטביות ממודל ה-AI
- **הטמעות & פעולות וקטוריות**: יישום חיפוש סמנטי והתאמת דמיון
- **Retrieval-Augmented Generation (RAG)**: שילוב AI עם מקורות הנתונים שלך
- **קריאת פונקציות**: הרחבת יכולות ה-AI עם כלים ותוספים מותאמים אישית
- **[→ התחל פרק 3](./03-CoreGenerativeAITechniques/README.md)**

### **פרק 4: יישומים מעשיים ופרויקטים**
- **מחולל סיפורי חיות מחמד** (`petstory/`): יצירת תוכן יצירתי עם GitHub Models
- **הדגמת Foundry מקומית** (`foundrylocal/`): אינטגרציה של מודל AI מקומי עם OpenAI Java SDK
- **שירות מחשבון MCP** (`calculator/`): יישום בסיסי של Model Context Protocol עם Spring AI
- **[→ התחל פרק 4](./04-PracticalSamples/README.md)**

### **פרק 5: פיתוח אחראי של בינה מלאכותית**
- **בטיחות ב-GitHub Models**: בדיקת סינון תוכן ומנגנוני בטיחות מובנים (חסימות מוחלטות וסירובים רכים)
- **הדגמת פיתוח אחראי של AI**: דוגמה מעשית שמדגימה כיצד מערכות בטיחות מודרניות של AI עובדות בפועל
- **שיטות עבודה מומלצות**: הנחיות הכרחיות לפיתוח והטמעה אתית של AI
- **[→ התחל פרק 5](./05-ResponsibleGenAI/README.md)**

## משאבים נוספים

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j למתחילים](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js למתחילים](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / סוכנים
[![AZD למתחילים](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI למתחילים](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP למתחילים](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![סוכני AI למתחילים](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### סדרת בינה גנרטיבית
[![בינה גנרטיבית למתחילים](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![בינה גנרטיבית (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![בינה גנרטיבית (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![בינה גנרטיבית (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### למידה עיקרית
[![למידת מכונה למתחילים](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![מדעי הנתונים למתחילים](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![בינה מלאכותית למתחילים](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![אבטחת סייבר למתחילים](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![פיתוח ווב למתחילים](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![IoT למתחילים](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![פיתוח XR למתחילים](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### סדרת Copilot
[![Copilot לתכנות זוגי עם בינה מלאכותית](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot ל־C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![הרפתקאות Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## קבלת עזרה

אם תיתקעו או יהיו לכם שאלות לגבי בניית יישומי בינה מלאכותית. הצטרפו ללומדים נוספים ולמפתחים מנוסים בדיונים על MCP. זו קהילה תומכת שבה שאלות מתקבלות בברכה והידע משותף בחופשיות.

[![Discord של Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

אם יש לכם משוב על המוצר או שגיאות בזמן הפיתוח, בקרו ב:

[![פורום המפתחים של Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
הצהרת אחריות:
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). אף שאנו שואפים לדייק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו כמקור הסמכות. עבור מידע קריטי מומלץ תרגום מקצועי על ידי מתרגם אנושי. איננו אחראים לכל אי-הבנות או פרשנויות שגויות הנובעות משימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->