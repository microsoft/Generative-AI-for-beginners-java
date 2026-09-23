# שיחה בסיסית עם Azure AI Foundry - דוגמה מקצה לקצה

דוגמה זו היא אפליקציית Spring Boot פשוטה שמתחברת לדגם של **Azure AI Foundry** באמצעות **אימות ללא מפתח** (Microsoft Entra ID) ובודקת את ההגדרה שלך. היא שומרת על `ChatClient` של Spring AI, הנתמך על ידי **ה-SDK הרשמי של OpenAI ל-Java** וכתובת ה-API **Azure OpenAI v1**.

הגרסאות ב-[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) הן Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, ו-dotenv-java **3.2.0**. הדוגמה משתמשת ב-`spring-ai-starter-model-openai` ומצהירה במפורש על `openai-java` ו-`azure-identity`; ב-Spring AI 2 הוסר הסטארטר הישן של Azure OpenAI.

## תוכן העניינים

- [דרישות מקדימות](#דרישות-מקדימות)
- [התחלה מהירה](#התחלה-מהירה)
- [כיצד האימות עובד](#כיצד-האימות-עובד)
- [הרצת האפליקציה](#הרצת-האפליקציה)
  - [שימוש ב-Maven](#שימוש-ב-maven)
  - [שימוש ב-VS Code](#שימוש-ב-vs-code)
  - [פלט צפוי](#פלט-צפוי)
- [הפניות להגדרות](#הפניות-להגדרות)
  - [משתני סביבה](#משתני-סביבה)
  - [הגדרות Spring](#הגדרות-spring)
- [פתרון בעיות](#פתרון-בעיות)
  - [בעיות נפוצות](#בעיות-נפוצות)
  - [מצב דיבוג](#מצב-דיבוג)
- [שלבים הבאים](#שלבים-הבאים)
- [משאבים](#משאבים)

## דרישות מקדימות

לפני הפעלת הדוגמה הזו, ודא שיש לך:

- משאב Azure AI Foundry עם פריסה של `gpt-5.6-luna` - ספק אותו באמצעות `azd up` או ידנית דרך [מדריך ההגדרה של Azure AI Foundry](../../getting-started-azure-openai.md)
- תפקיד **Cognitive Services OpenAI User** על המשאב (תבניות Bicep מקצות את זה עבורך)
- את [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), מחובר עם `az login`
- Java 21+ ו-Maven 3.9+

> **אין צורך במפתח API** — האימות הוא ללא מפתח באמצעות Microsoft Entra ID.

## התחלה מהירה

```bash
# 1. נווט לפרויקט
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. התחבר כדי שאימות ללא מפתח יוכל לקבל אסימון
az login

# 3. הגדר את נקודת הקצה
#    - אם הרצת את `azd up`, הקובץ .env נכתב עבורך (דלג על כך).
#    - אחרת העתק את התבנית והגדר את AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. הפעל את היישום
mvn spring-boot:run
```

## כיצד האימות עובד

דוגמה זו מאמתת עם **Microsoft Entra ID** — אין מפתח API.

האפליקציה מגדירה את האימות במפורש ב-[BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` יוצר `BearerTokenCredential` באמצעות `AuthenticationUtil.getBearerTokenSupplier` עם `DefaultAzureCredential` והסמכה של `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` בונה `OpenAIClient` עם `OpenAIOkHttpClient.builder()`, פותר את נקודת הקצה של המשאב ל-`/openai/v1`, ומספק את אישור הנשיאה עם `.credential(...)`.
3. `azureChatModel()` מספק את הלקוח הזה ל-`OpenAiChatModel` של Spring AI, התומך ב-`ChatClient` של השיעור.

פיני הביניים המפורשים האלה מונעים מ-`OPENAI_API_KEY` גלובלי להחליף את האימות של Azure. השמטה של מפתח API ב-YAML בלבד אינה ההגדרה לאימות. `DefaultAzureCredential` יכול להשתמש במפגש `az login` מקומי או זהות מנוהלת ב-Azure; כל זהות שנבחרה חייבת להחזיק בתפקיד המשאב שפורט לעיל.

## הרצת האפליקציה

### שימוש ב-Maven

```bash
mvn spring-boot:run
```

### שימוש ב-VS Code

1. פתח את הפרויקט ב-VS Code
2. לחץ על `F5` או השתמש בפאנל "Run and Debug"
3. בחר בקונפיגורציית "Spring Boot-BasicChatApplication"

> **הערה**: האפליקציה טוענת `.env` מספריית העבודה שלה, גם כאשר מופעלת מ-VS Code.

### פלט צפוי

פלט מאויר לאחר הרצה מוצלחת (יומני הפעלה לא כלולים; ניסוח התגובה משתנה):

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

## הפניות להגדרות

### משתני סביבה

| משתנה | תיאור | דרוש | דוגמה |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | כתובת נקודת הקצה Foundry (Azure OpenAI) | כן | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | שם פריסת דגם השיחה | לא | `gpt-5.6-luna` (בררת מחדל) |

> אין משתנה מפתח API — האימות הוא ללא מפתח (Microsoft Entra ID דרך `az login`).

### הגדרות Spring

ההגדרות ב-[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) משתמשות בקידומת `spring.ai.openai` ופרופרטיז של שיחה שטוחים (ללא בלוק `options`):

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

`model` הוא **שם הפריסה של Azure**. האימות מגיע מפיני הביניים המפורשים שתוארו לעיל, לא מהגדרת `api-key`. השיעור מבטל הסקה ומגביל את סך הטוקנים להשלמה ל-500; הוא משאיר את `temperature` ואת `max-tokens` הישן ללא הגדרה.

מיקרוסופט ממליצה על [ה-SDK הרשמי של OpenAI עם Azure OpenAI v1 ו-Responses API לאפליקציות חדשות](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions נשאר נתמך לשיעור זה המבוסס על הודעות. עבור GPT-5.6, בקשות הכוללות כלים ב-Chat Completions חייבות להגדיר את `reasoning_effort` ל-`none`; השתמש ב-Responses כאשר משלבים הסקה עם כלים. ראה [קריאת כלים עם דגמי הסקה](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## פתרון בעיות

### בעיות נפוצות

<details>
<summary><strong>שגיאה: 401 / "PermissionDenied" / שגיאות טוקן</strong></summary>

- הפעל `az login` — אימות ללא מפתח דורש התחברות פעילה לקבלת טוקן
- אמת שחשבונך מחזיק בתפקיד **Cognitive Services OpenAI User** על המשאב
- אם רק מינת את התפקיד, המתן דקה להתפשטות השינוי
- אשר שאתה ב-tenant/מנוי הנכון (`az account show`)
</details>

<details>
<summary><strong>שגיאה: "נקודת הקצה אינה תקינה" / שגיאות חיבור</strong></summary>

- ודא ש-`AZURE_OPENAI_ENDPOINT` הוא כתובת בסיס מלאה (לדוגמה, `https://your-resource.openai.azure.com/`)
- בדוק עקביות של סלאש סופי
- אמת שנקודת הקצה מתאימה למשאב שהקצת (`azd env get-values`)
</details>

<details>
<summary><strong>שגיאה: "הפריסה לא נמצאה"</strong></summary>

- אמת ש-`AZURE_OPENAI_DEPLOYMENT` תואם לשם פריסה ב-Azure
- בדוק שהדגם מופעל ומאושר לפעולה
- שם הפריסה בררת המחדל הוא `gpt-5.6-luna`
</details>

<details>
<summary><strong>שגיאה: 429 / חריגה ממגבלת קצב</strong></summary>

- לפריסת GPT-5.6 Luna ברירת המחדל יש קיבולת סטנדרטית גלובלית 10: 10 בקשות לדקה ו-10,000 טוקנים לדקה
- הרץ דוגמאות ברצף והמתן לפני ניסיון חוזר לפי מרווח הניסיון של השירות
- דוגמה בסיסית זו מבטלת ניסיונות אוטומטיים של ה-SDK, כך שהבקשה שנכשלה מדווחת ישירות
</details>

<details>
<summary><strong>VS Code: משתני סביבה לא נטענים</strong></summary>

- ודא שקובץ `.env` נמצא בתיקיית השורש של הפרויקט (בהיהוי עם `pom.xml`)
- נסה להפעיל `mvn spring-boot:run` במסוף המשולב של VS Code
- בדוק שההרחבה של Java ב-VS Code מותקנת כראוי
</details>

### מצב דיבוג

כדי לאפשר רישום מפורט, הסר את ההערה מהשורות הבאות ב-[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## שלבים הבאים

**ההגדרה הושלמה!** המשך במסע הלמידה שלך:

[פרק 3: טכניקות AI גנרטיביות בסיסיות](../../../03-CoreGenerativeAITechniques/README.md)

## משאבים

- [מעבר ל-Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [ה-SDK הרשמי של OpenAI Java עם Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [אימות ללא מפתח עם Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [פורטאל Azure AI Foundry](https://ai.azure.com/)
- [תיעוד Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום אוטומטי [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו הטבעית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אדם. אנו לא אחראים לכל אי-הבנה או פירוש שגוי הנובע מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->