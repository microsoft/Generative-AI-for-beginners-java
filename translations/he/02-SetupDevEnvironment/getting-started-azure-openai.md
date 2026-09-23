# הגדרת סביבת הפיתוח עבור Azure AI Foundry

> מדריך זה מגדיר דגמים של **Azure AI Foundry** עבור אפליקציות AI בג'אווה בקורס זה, באמצעות אימות **ללא מפתח** (Microsoft Entra ID) — אין מפתחות API לנהל. חדש בכלי זה? התחל עם [מדריך סביבת הפיתוח](./README.md).

מדריך זה מגדיר דגמים של **Azure AI Foundry** עבור אפליקציות AI בג'אווה בקורס זה. יש לך שתי דרכים:

- **אפשרות א' — פריסת חשבון עם `azd` + Bicep (מומלץ):** פקודה אחת שפורסת את חשבון Foundry והדגמים כקוד. ללא לחיצות בפורטל.
- **אפשרות ב' — יצירת משאבים ידנית** בפורטל Azure AI Foundry.

שתי הדרכים משתמשות ב**אימות ללא מפתח** (Microsoft Entra ID) — אין מפתחות API להעתקה או דליפה.

## תוכן העניינים

- [מה נוצר](#מה-נוצר)
- [דרישות מוקדמות](#דרישות-מוקדמות)
- [אפשרות א': פריסה עם azd + Bicep (מומלץ)](#option-a-provision-with-azd--bicep-recommended)
- [אפשרות ב': יצירת משאבים ידנית](#אפשרות-ב-יצירת-משאבים-ידנית)
- [הגדרת הסביבה שלך](#הגדרת-הסביבה-שלך)
- [בדוק את ההתקנה שלך](#בדוק-את-ההתקנה-שלך)
- [מה הלאה?](#מה-הלאה)
- [משאבים](#משאבים)
- [משאבים נוספים](#משאבים-נוספים)

## מה נוצר

תבניות Bicep בתיקיית [`infra/`](../../../02-SetupDevEnvironment/infra) מפריסות:

- חשבון **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, סוג `AIServices`) עם פרויקט
- פריסה של **שיחה** - GPT-5.6 Luna (`gpt-5.6-luna`), גרסה `2026-07-09`, עם קיבולת `GlobalStandard` בת 10 (10 בקשות לדקה ו-10,000 טוקנים לדקה עבור דגם זה)
- פריסת **הטמעה** - `text-embedding-3-small`, גרסה `1` (משמש בפרקים מאוחרים יותר)
- הקצאת תפקיד **ללא מפתח** (`Cognitive Services OpenAI User`) כדי שתוכל להיכנס עם `az login` במקום לנהל מפתחות

## דרישות מוקדמות

- [מנוי Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) ו-[Maven 3.9+](https://maven.apache.org/download.cgi)

## אפשרות א': פריסה עם azd + Bicep (מומלץ)

מספריית `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# היכנס (בשני הכלים)
azd auth login
az login

# הפעל חשבון Foundry + פריסות מודלים
azd up
```

`azd` יבקש **שם סביבת עבודה** (לדוגמה `genai-java`), **מנוי**, ו**אזור**. בחר מנוי משלך ואזור שבו `gpt-5.6-luna` ו-`text-embedding-3-small` זמינים, לדוגמה `eastus2`. אשר שלמנוי יש מכסת משאבים מספקת לדגם ולסוג הפריסה באזור זה; זמינות ומכסה משתנים בין מנויים.

כשסיום הפריסה, azd:

1. מפריס את כל מה שמוגדר ב-[`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. מריץ הוק אחרי הפריסה שכותב את [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) עם כתובת הקצה ושמות הפריסה שלך (ללא סודות).

> **טיפ:** הרץ שוב `azd up` בכל עת כדי להחיל שינויים. הרץ `azd down` כדי למחוק הכל ולעצור את עלויות התפעול.

כדי לראות את ההגדרות שנוצרו:

```bash
azd env get-values
```

עכשיו דלג אל [בדוק את ההתקנה שלך](#בדוק-את-ההתקנה-שלך).

## אפשרות ב': יצירת משאבים ידנית

מעדיף את הפורטל? צור את המשאבים ידנית:

1. עבור אל [פורטל Azure AI Foundry](https://ai.azure.com/) והתחבר.
2. **צור פרויקט** (זה יוצר גם משאב Foundry). תן לו שם כמו `GenAIJava`.
3. בפרויקט שלך, פתח **Models + endpoints** → **פרוס דגם** → **פרוס דגם בסיסי**.
4. פרוס את **GPT-5.6 Luna** (שם דגם ופריסה `gpt-5.6-luna`, גרסה `2026-07-09`) עם קיבולת **Global Standard** של 10. חזור על הפעולה עבור **text-embedding-3-small**, גרסה `1`, אם ברצונך דוגמאות להטמעה.
5. מ**סקירה כללית**, העתק את **כתובת הקצה** (לדוגמה `https://<resource>.openai.azure.com/`).
6. הענק לעצמך גישה ללא מפתח: במשאב פתח **ניהול גישה (IAM)** → **הוסף הקצאת תפקיד** → הקצה את **Cognitive Services OpenAI User** לחשבונך.

> **עדיין יש בעיות?** ראה את [התיעוד של Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## הגדרת הסביבה שלך

**אם השתמשת באפשרות א' (`azd up`)**, קובץ ההגדרות שלך כבר נכתב — אין צורך להגדיר כלום. דלג ל[בדוק את ההתקנה שלך](#בדוק-את-ההתקנה-שלך).

**אם השתמשת באפשרות ב' (ידנית)**, צור בעצמך את קובץ `.env` של הדוגמא:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

ערוך את `.env` עם כתובת הקצה שלך (ללא מפתח — האימות ללא מפתח):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

השתמש בכתובת הקצה של Azure OpenAI במשאב, לא בכתובת URL של פרויקט. אפליקציית basic-chat מפענחת זאת אל `/openai/v1` ומגדירה לקוח עם אסימון נושא מפורש; מפתח API אינו נדרש.

> **הערת אבטחה:** אין מפתח API לשמירה. אתה מאמת עם Microsoft Entra ID דרך `az login` (מקומי) או זהות מנוהלת (ב-Azure). קובץ `.env` מחזיק רק הגדרות ללא סודות וכבר מתווסף ל-`.gitignore`.

## בדוק את ההתקנה שלך

ודא שאתה מחובר כדי שאימות ללא מפתח יכול לקבל אסימון, ואז הרץ את הדוגמה:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # אם עדיין לא נכנסת למערכת
mvn clean spring-boot:run
```

עליך לראות תגובה מהדגם `gpt-5.6-luna`. הרץ את הדוגמאות ברצף כדי להישאר בתוך המכסה הקטנה לבררת המחדל; אם תקבל HTTP 429, המתן לפני הניסיון הבא.

> **משתמשי VS Code:** לחץ `F5` להרצה. האפליקציה טוענת את `.env` שלך אוטומטית.

> **דוגמה מלאה:** ראה את [דוגמת Basic Chat עם Azure AI Foundry](./examples/basic-chat-azure/README.md) לפרטים ופתרון תקלות.

## מה הלאה?

לאחר הפריסה והרצה מוצלחת של הדוגמה, תהיה לך:
- Azure AI Foundry עם `gpt-5.6-luna` ו-`text-embedding-3-small` פרוסים
- אימות ללא מפתח (Microsoft Entra ID) — ללא מפתחות לנהל
- קובץ `.env` מקומי עם כתובת הקצה ושמות הפריסה שלך
- סביבת פיתוח בג'אווה מוכנה לשימוש

**המשך אל** [פרק 3: טכניקות ליבה של AI יוצרת](../03-CoreGenerativeAITechniques/README.md) כדי להתחיל לבנות אפליקציות AI!

## משאבים

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [אימות ללא מפתח עם Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [תיעוד Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [מעבר Spring AI 2 לספריית OpenAI בג'אווה](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [ספריית OpenAI הרשמית בג'אווה עם Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## משאבים נוספים

- [הורדת VS Code](https://code.visualstudio.com/Download)
- [קבל Docker Desktop](https://www.docker.com/products/docker-desktop)
- [הגדרת Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום אוטומטי [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו הטבעית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אדם. אנו לא אחראים לכל אי-הבנה או פירוש שגוי הנובע מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->