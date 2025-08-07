<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:16:04+00:00",
  "source_file": "README.md",
  "language_code": "he"
}
-->
# בינה מלאכותית גנרטיבית למתחילים - מהדורת Java

[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![בינה מלאכותית גנרטיבית למתחילים - מהדורת Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.he.png)

**זמן נדרש**: ניתן להשלים את הסדנה כולה באופן מקוון ללא צורך בהתקנה מקומית. הגדרת הסביבה אורכת 2 דקות, וההתנסות בדוגמאות דורשת 1-3 שעות בהתאם לעומק החקירה.

> **התחלה מהירה**

1. בצעו Fork למאגר זה לחשבון GitHub שלכם  
2. לחצו **Code** → **Codespaces** → **...** → **New with options...**  
3. השתמשו בהגדרות ברירת המחדל – זה יבחר את מיכל הפיתוח שנוצר עבור הקורס  
4. לחצו **Create codespace**  
5. המתינו כ-2 דקות עד שהסביבה תהיה מוכנה  
6. עברו ישירות ל-[הדוגמה הראשונה](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)  

## תמיכה בריבוי שפות

### נתמך באמצעות GitHub Action (אוטומטי ותמיד מעודכן)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](./README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## מבנה הקורס ונתיב הלמידה

### **פרק 1: מבוא לבינה מלאכותית גנרטיבית**
- **מושגים מרכזיים**: הבנת מודלים שפתיים גדולים, טוקנים, הטמעות ויכולות AI  
- **מערכת AI ב-Java**: סקירה של Spring AI ו-SDKs של OpenAI  
- **פרוטוקול הקשר מודל**: מבוא ל-MCP ותפקידו בתקשורת סוכני AI  
- **יישומים מעשיים**: תרחישים בעולם האמיתי כולל צ'אטבוטים ויצירת תוכן  
- **[→ התחילו את פרק 1](./01-IntroToGenAI/README.md)**  

### **פרק 2: הגדרת סביבת פיתוח**
- **הגדרת ספקים מרובים**: הגדרת מודלים של GitHub, Azure OpenAI ואינטגרציות SDK של OpenAI Java  
- **Spring Boot + Spring AI**: שיטות עבודה מומלצות לפיתוח יישומי AI ארגוניים  
- **מודלים של GitHub**: גישה חינמית למודלי AI לצורך יצירת אב-טיפוס ולמידה (ללא צורך בכרטיס אשראי)  
- **כלי פיתוח**: הגדרת מיכלי Docker, VS Code ו-GitHub Codespaces  
- **[→ התחילו את פרק 2](./02-SetupDevEnvironment/README.md)**  

### **פרק 3: טכניקות ליבה של בינה מלאכותית גנרטיבית**
- **הנדסת הנחיות**: טכניקות לתגובות מיטביות ממודלי AI  
- **הטמעות ופעולות וקטוריות**: יישום חיפוש סמנטי והתאמת דמיון  
- **יצירה מוגברת על ידי אחזור (RAG)**: שילוב AI עם מקורות נתונים משלכם  
- **קריאת פונקציות**: הרחבת יכולות AI עם כלים ותוספים מותאמים אישית  
- **[→ התחילו את פרק 3](./03-CoreGenerativeAITechniques/README.md)**  

### **פרק 4: יישומים מעשיים ופרויקטים**
- **מחולל סיפורי חיות מחמד** (`petstory/`): יצירת תוכן יצירתי עם מודלים של GitHub  
- **דמו מקומי של Foundry** (`foundrylocal/`): אינטגרציה של מודלי AI מקומיים עם OpenAI Java SDK  
- **שירות מחשבון MCP** (`calculator/`): יישום בסיסי של פרוטוקול הקשר מודל עם Spring AI  
- **[→ התחילו את פרק 4](./04-PracticalSamples/README.md)**  

### **פרק 5: פיתוח AI אחראי**
- **בטיחות מודלים של GitHub**: בדיקת סינון תוכן מובנה ומנגנוני בטיחות (חסימות קשות וסירובים רכים)  
- **דמו AI אחראי**: דוגמה מעשית שמראה כיצד מערכות בטיחות AI מודרניות פועלות בפועל  
- **שיטות עבודה מומלצות**: הנחיות חיוניות לפיתוח והטמעה אתית של AI  
- **[→ התחילו את פרק 5](./05-ResponsibleGenAI/README.md)**  

## משאבים נוספים

- [MCP למתחילים](https://github.com/microsoft/mcp-for-beginners)  
- [סוכני AI למתחילים](https://github.com/microsoft/ai-agents-for-beginners)  
- [בינה מלאכותית גנרטיבית למתחילים באמצעות .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)  
- [בינה מלאכותית גנרטיבית למתחילים באמצעות JavaScript](https://github.com/microsoft/generative-ai-with-javascript)  
- [בינה מלאכותית גנרטיבית למתחילים](https://github.com/microsoft/generative-ai-for-beginners)  
- [למידת מכונה למתחילים](https://aka.ms/ml-beginners)  
- [מדע נתונים למתחילים](https://aka.ms/datascience-beginners)  
- [AI למתחילים](https://aka.ms/ai-beginners)  
- [סייבר למתחילים](https://github.com/microsoft/Security-101)  
- [פיתוח אתרים למתחילים](https://aka.ms/webdev-beginners)  
- [IoT למתחילים](https://aka.ms/iot-beginners)  
- [פיתוח XR למתחילים](https://github.com/microsoft/xr-development-for-beginners)  
- [שליטה ב-GitHub Copilot לתכנות זוגי עם AI](https://aka.ms/GitHubCopilotAI)  
- [שליטה ב-GitHub Copilot למפתחי C#/.NET](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)  
- [בחרו את הרפתקת Copilot שלכם](https://github.com/microsoft/CopilotAdventures)  
- [אפליקציית צ'אט RAG עם שירותי Azure AI](https://github.com/Azure-Samples/azure-search-openai-demo-java)  

**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). בעוד שאנו שואפים לדיוק, יש להיות מודעים לכך שתרגומים אוטומטיים עשויים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית צריך להיחשב כמקור סמכותי. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי על ידי אדם. איננו נושאים באחריות לאי הבנות או לפרשנויות שגויות הנובעות משימוש בתרגום זה.