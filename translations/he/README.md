<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:32:26+00:00",
  "source_file": "README.md",
  "language_code": "he"
}
-->
# אינטליגנציה מלאכותית גנרטיבית למתחילים - מהדורת Java
[![Discord של Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![אינטליגנציה מלאכותית גנרטיבית למתחילים - מהדורת Java](../../translated_images/he/beg-genai-series.8b48be9951cc574c.webp)

**זמן התחייבות**: כל הסדנה ניתנת לביצוע באופן מקוון ללא התקנה מקומית. הגדרת הסביבה לוקחת 2 דקות, וחקר הדוגמאות דורש 1-3 שעות בהתאם לעומק החקירה.

> **התחלה מהירה**

1. צור Fork למאגר זה לחשבון GitHub שלך
2. לחץ על **Code** → לשונית **Codespaces** → **...** → **חדש עם אפשרויות...**
3. השתמש בערכי ברירת המחדל – זה יבחר את מכולת הפיתוח שנוצרה לקורס זה
4. לחץ על **Create codespace**
5. המתן כ-2 דקות עד שהסביבה תהיה מוכנה
6. עבור ישירות ל-[הדוגמה הראשונה](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **מעדיף לשכפל מקומית?**

> מאגר זה כולל יותר מ-50 תרגומים לשפות שונות שמגדילים משמעותית את גודל ההורדה. לשכפול ללא תרגומים, השתמש ב-sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> זה נותן לך את כל מה שאתה צריך כדי להשלים את הקורס עם הורדה מהירה בהרבה.


## תמיכה בריבוי שפות

### נתמך באמצעות GitHub Action (מאובטח ותמיד מעודכן)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ערבית](../ar/README.md) | [בנגלית](../bn/README.md) | [בולגרית](../bg/README.md) | [בורמית (מיאנמר)](../my/README.md) | [סינית (מפושטת)](../zh/README.md) | [סינית (מסורתית, הונג קונג)](../hk/README.md) | [סינית (מסורתית, מקאו)](../mo/README.md) | [סינית (מסורתית, טייוואן)](../tw/README.md) | [קרואטית](../hr/README.md) | [צ'כית](../cs/README.md) | [דנית](../da/README.md) | [הולנדית](../nl/README.md) | [אסטונית](../et/README.md) | [פינית](../fi/README.md) | [צרפתית](../fr/README.md) | [גרמנית](../de/README.md) | [יוונית](../el/README.md) | [עברית](./README.md) | [הינדי](../hi/README.md) | [הונגרית](../hu/README.md) | [אינדונזית](../id/README.md) | [איטלקית](../it/README.md) | [יפנית](../ja/README.md) | [קאנדה](../kn/README.md) | [קוריאנית](../ko/README.md) | [ליטאית](../lt/README.md) | [מלאית](../ms/README.md) | [מאליאלאם](../ml/README.md) | [מרטהית](../mr/README.md) | [נפאלית](../ne/README.md) | [ניגרית פידגין](../pcm/README.md) | [נורווגית](../no/README.md) | [פרסית (פארסית)](../fa/README.md) | [פולנית](../pl/README.md) | [פורטוגזית (ברזיל)](../br/README.md) | [פורטוגזית (פורטוגל)](../pt/README.md) | [פנג'אבית (ג'רמוכי)](../pa/README.md) | [רומנית](../ro/README.md) | [רוסית](../ru/README.md) | [סרבית (קירילית)](../sr/README.md) | [סלובקית](../sk/README.md) | [סלובנית](../sl/README.md) | [ספרדית](../es/README.md) | [סווהילית](../sw/README.md) | [שוודית](../sv/README.md) | [טגלוג (פיליפינית)](../tl/README.md) | [טמילית](../ta/README.md) | [טלוגו](../te/README.md) | [תאית](../th/README.md) | [טורקית](../tr/README.md) | [אוקראינית](../uk/README.md) | [אורדו](../ur/README.md) | [וייטנאמית](../vi/README.md)

> **מעדיף לשכפל מקומית?**

> מאגר זה כולל יותר מ-50 תרגומים לשפות שונות שמגדילים משמעותית את גודל ההורדה. לשכפול ללא תרגומים, השתמש ב-sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> זה נותן לך את כל מה שאתה צריך כדי להשלים את הקורס עם הורדה מהירה בהרבה.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## מבנה הקורס ונתיב הלמידה

### **פרק 1: מבוא לאינטליגנציה מלאכותית גנרטיבית**
- **מושגים מרכזיים**: הבנת מודלים לשוניים גדולים, טוקנים, אמבדינגים ויכולות AI
- **אקו-סיסטם AI בג'אווה**: סקירה של Spring AI ו-OpenAI SDKs
- **פרוטוקול הקשר המודל (MCP)**: מבוא ל-MCP ותפקידו בתקשורת סוכני AI
- **יישומים מעשיים**: תרחישים אמיתיים כולל צ'אטבוטים ויצירת תוכן
- **[→ להתחיל פרק 1](./01-IntroToGenAI/README.md)**

### **פרק 2: הגדרת סביבת הפיתוח**
- **קונפיגורציה עם מספר ספקים**: הגדרת GitHub Models, Azure OpenAI ו-OpenAI Java SDK
- **Spring Boot + Spring AI**: שיטות עבודה מומלצות לפיתוח יישומי AI ארגוניים
- **GitHub Models**: גישה חינמית למודלי AI לפרוטוטייפינג וללמידה (ללא צורך בכרטיס אשראי)
- **כלי פיתוח**: מכולות דוקר, VS Code וקונפיגורציית GitHub Codespaces
- **[→ להתחיל פרק 2](./02-SetupDevEnvironment/README.md)**

### **פרק 3: טכניקות מרכזיות ב-AI גנרטיבי**
- **הנדסת פרומפטים**: טכניקות לתגובה מיטבית של מודלי AI
- **אמבדינגים ופעולות וקטור**: יישום חיפוש סמנטי והתאמת דמיון
- **יצירה מוגברת בהחזרה (RAG)**: שילוב AI עם מקורות המידע שלך
- **קריאת פונקציות**: הרחבת יכולות AI עם כלים ותוספים מותאמים אישית
- **[→ להתחיל פרק 3](./03-CoreGenerativeAITechniques/README.md)**

### **פרק 4: יישומים מעשיים ופרויקטים**
- **מחולל סיפור על חיות מחמד** (`petstory/`): יצירת תוכן יצירתי עם GitHub Models
- **הדגמה מקומית של Foundry** (`foundrylocal/`): אינטגרציה מקומית של מודל AI עם OpenAI Java SDK
- **שירות מחשבון MCP** (`calculator/`): יישום בסיסי של פרוטוקול הקשר המודל עם Spring AI
- **[→ להתחיל פרק 4](./04-PracticalSamples/README.md)**

### **פרק 5: פיתוח AI אחראי**
- **בטיחות GitHub Models**: בדיקת סינון תוכן מובנה ומנגנוני בטיחות (חסימות קשוחות וסירובים רכים)
- **הדגמת AI אחראי**: דוגמה מעשית להראות כיצד מערכות בטיחות AI מודרניות פועלות
- **שיטות עבודה מומלצות**: קווים מנחים חיוניים לפיתוח והטמעת AI אתי
- **[→ להתחיל פרק 5](./05-ResponsibleGenAI/README.md)**

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
 
### סדרת אינטליגנציה מלאכותית גנרטיבית
[![אינטליגנציה מלאכותית גנרטיבית למתחילים](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![אינטליגנציה מלאכותית גנרטיבית (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![אינטליגנציה מלאכותית גנרטיבית (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![אינטליגנציה מלאכותית גנרטיבית (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### למידה בסיסית
[![למידת מכונה למתחילים](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![מדעי נתונים למתחילים](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI למתחילים](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![אבטחת מידע למתחילים](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![פיתוח ווב למתחילים](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT למתחילים](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![פיתוח XR למתחילים](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### סדרת Copilot
[![Copilot לתכנות זוגי בינה מלאכותית](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot ל-C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![הרפתקת Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## לקבלת עזרה

אם נתקעת או יש לך שאלות לגבי בניית אפליקציות בינה מלאכותית. הצטרף ללומדים אחרים ומפתחים מנוסים בדיונים בנושא MCP. זו קהילה תומכת שבה שאלות מתקבלות בברכה והידע משותף בחופשיות.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

אם יש לך משוב על מוצר או שגיאות בזמן הבנייה בקר ב:

[![פורום המפתחים Microsoft Foundry על GitHub](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב התראה**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון כי תרגומים אוטומטיים עלולים לכלול שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית יש להיחשב למקור הסמכותי. למידע קריטי מומלץ להשתמש בתרגום מקצועי של בני אדם. אנו לא נושאים באחריות לכל הבנה שגויה או פרשנות מוטעית הנובעת מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->