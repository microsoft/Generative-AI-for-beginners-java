<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:49:42+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "he"
}
-->
# הגדרת סביבת פיתוח עבור AI גנרטיבי ב-Java

> **התחלה מהירה**: כתיבת קוד בענן תוך 2 דקות - עברו ל-[הגדרת GitHub Codespaces](../../../02-SetupDevEnvironment) - אין צורך בהתקנה מקומית ומשתמש במודלים של GitHub!

> **מתעניינים ב-Azure OpenAI?**, ראו את [מדריך ההגדרה של Azure OpenAI](getting-started-azure-openai.md) עם שלבים ליצירת משאב חדש ב-Azure OpenAI.

## מה תלמדו

- להגדיר סביבת פיתוח Java עבור יישומי AI
- לבחור ולהגדיר את סביבת הפיתוח המועדפת עליכם (קודם כל בענן עם Codespaces, מכולת פיתוח מקומית, או התקנה מקומית מלאה)
- לבדוק את ההגדרה שלכם על ידי התחברות למודלים של GitHub

## תוכן עניינים

- [מה תלמדו](../../../02-SetupDevEnvironment)
- [מבוא](../../../02-SetupDevEnvironment)
- [שלב 1: הגדרת סביבת הפיתוח שלכם](../../../02-SetupDevEnvironment)
  - [אפשרות A: GitHub Codespaces (מומלץ)](../../../02-SetupDevEnvironment)
  - [אפשרות B: מכולת פיתוח מקומית](../../../02-SetupDevEnvironment)
  - [אפשרות C: שימוש בהתקנה המקומית הקיימת שלכם](../../../02-SetupDevEnvironment)
- [שלב 2: יצירת אסימון גישה אישי ל-GitHub](../../../02-SetupDevEnvironment)
- [שלב 3: בדיקת ההגדרה שלכם](../../../02-SetupDevEnvironment)
- [פתרון בעיות](../../../02-SetupDevEnvironment)
- [סיכום](../../../02-SetupDevEnvironment)
- [השלבים הבאים](../../../02-SetupDevEnvironment)

## מבוא

פרק זה ידריך אתכם בהגדרת סביבת פיתוח. נשתמש ב-**מודלים של GitHub** כדוגמה ראשית מכיוון שזה חינמי, קל להגדרה עם חשבון GitHub בלבד, אינו דורש כרטיס אשראי, ומספק גישה למספר מודלים לניסויים.

**אין צורך בהגדרה מקומית!** ניתן להתחיל לכתוב קוד מיד באמצעות GitHub Codespaces, המספק סביבת פיתוח מלאה בדפדפן שלכם.

<img src="./images/models.webp" alt="צילום מסך: מודלים של GitHub" width="50%">

אנו ממליצים להשתמש ב-[**מודלים של GitHub**](https://github.com/marketplace?type=models) עבור קורס זה מכיוון ש:
- **חינמי** להתחלה
- **קל** להגדרה עם חשבון GitHub בלבד
- **אין צורך בכרטיס אשראי**
- **מספר מודלים** זמינים לניסויים

> **הערה**: למודלים של GitHub המשמשים באימון זה יש מגבלות חינמיות:
> - 15 בקשות לדקה (150 ביום)
> - ~8,000 מילים נכנסות, ~4,000 מילים יוצאות לכל בקשה
> - 5 בקשות בו-זמניות
> 
> לשימוש בייצור, שדרגו למודלים של Azure AI Foundry עם חשבון Azure שלכם. אין צורך לשנות את הקוד שלכם. ראו את [התיעוד של Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## שלב 1: הגדרת סביבת הפיתוח שלכם

<a name="quick-start-cloud"></a>

יצרנו מכולת פיתוח מוגדרת מראש כדי למזער את זמן ההגדרה ולהבטיח שיש לכם את כל הכלים הדרושים לקורס AI גנרטיבי ב-Java. בחרו את גישת הפיתוח המועדפת עליכם:

### אפשרויות הגדרת הסביבה:

#### אפשרות A: GitHub Codespaces (מומלץ)

**התחילו לכתוב קוד תוך 2 דקות - אין צורך בהגדרה מקומית!**

1. עשו Fork למאגר זה לחשבון GitHub שלכם  
   > **הערה**: אם ברצונכם לערוך את ההגדרה הבסיסית, עיינו ב-[תצורת מכולת הפיתוח](../../../.devcontainer/devcontainer.json)
2. לחצו על **Code** → לשונית **Codespaces** → **...** → **New with options...**
3. השתמשו בברירות המחדל – זה יבחר את **תצורת מכולת הפיתוח**: **Generative AI Java Development Environment** מכולת פיתוח מותאמת אישית שנוצרה עבור קורס זה
4. לחצו על **Create codespace**
5. המתינו ~2 דקות עד שהסביבה תהיה מוכנה
6. המשיכו ל-[שלב 2: יצירת אסימון GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="צילום מסך: תפריט משנה של Codespaces" width="50%">

<img src="./images/image.png" alt="צילום מסך: חדש עם אפשרויות" width="50%">

<img src="./images/codespaces-create.png" alt="צילום מסך: אפשרויות יצירת Codespace" width="50%">

> **יתרונות של Codespaces**:
> - אין צורך בהתקנה מקומית
> - עובד על כל מכשיר עם דפדפן
> - מוגדר מראש עם כל הכלים והתלויות
> - חינם ל-60 שעות בחודש עבור חשבונות אישיים
> - סביבה עקבית לכל הלומדים

#### אפשרות B: מכולת פיתוח מקומית

**עבור מפתחים שמעדיפים פיתוח מקומי עם Docker**

1. עשו Fork ו-clone למאגר זה למחשב המקומי שלכם  
   > **הערה**: אם ברצונכם לערוך את ההגדרה הבסיסית, עיינו ב-[תצורת מכולת הפיתוח](../../../.devcontainer/devcontainer.json)
2. התקינו [Docker Desktop](https://www.docker.com/products/docker-desktop/) ו-[VS Code](https://code.visualstudio.com/)
3. התקינו את [הרחבת Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ב-VS Code
4. פתחו את תיקיית המאגר ב-VS Code
5. כאשר תתבקשו, לחצו על **Reopen in Container** (או השתמשו ב-`Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. המתינו לבניית המכולה ולהפעלתה
7. המשיכו ל-[שלב 2: יצירת אסימון GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="צילום מסך: הגדרת מכולת פיתוח" width="50%">

<img src="./images/image-3.png" alt="צילום מסך: בניית מכולת פיתוח הושלמה" width="50%">

#### אפשרות C: שימוש בהתקנה המקומית הקיימת שלכם

**עבור מפתחים עם סביבות Java קיימות**

דרישות מוקדמות:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) או IDE מועדף

שלבים:
1. עשו Clone למאגר זה למחשב המקומי שלכם
2. פתחו את הפרויקט ב-IDE שלכם
3. המשיכו ל-[שלב 2: יצירת אסימון GitHub](../../../02-SetupDevEnvironment)

> **טיפ מקצועי**: אם יש לכם מחשב עם מפרט נמוך אך רוצים להשתמש ב-VS Code מקומי, השתמשו ב-GitHub Codespaces! ניתן לחבר את VS Code המקומי שלכם ל-Codespace בענן כדי ליהנות משני העולמות.

<img src="./images/image-2.png" alt="צילום מסך: יצירת מופע מכולת פיתוח מקומי" width="50%">

## שלב 2: יצירת אסימון גישה אישי ל-GitHub

1. עברו ל-[הגדרות GitHub](https://github.com/settings/profile) ובחרו **Settings** מתפריט הפרופיל שלכם.
2. בסרגל הצד השמאלי, לחצו על **Developer settings** (בדרך כלל בתחתית).
3. תחת **Personal access tokens**, לחצו על **Fine-grained tokens** (או עקבו אחר [קישור ישיר](https://github.com/settings/personal-access-tokens)).
4. לחצו על **Generate new token**.
5. תחת "Token name", ספקו שם תיאורי (לדוגמה, `GenAI-Java-Course-Token`).
6. הגדירו תאריך תפוגה (מומלץ: 7 ימים לשמירה על אבטחה).
7. תחת "Resource owner", בחרו את חשבון המשתמש שלכם.
8. תחת "Repository access", בחרו את המאגר שברצונכם להשתמש בו עם מודלים של GitHub (או "All repositories" אם נדרש).
9. תחת "Repository permissions", מצאו **Models** והגדירו אותו ל-**Read and write**.
10. לחצו על **Generate token**.
11. **העתיקו ושמרו את האסימון שלכם עכשיו** – לא תוכלו לראות אותו שוב!

> **טיפ אבטחה**: השתמשו בהיקף המינימלי הנדרש ובזמן תפוגה הקצר ביותר האפשרי עבור אסימוני הגישה שלכם.

## שלב 3: בדיקת ההגדרה שלכם עם דוגמת מודלים של GitHub

לאחר שסביבת הפיתוח שלכם מוכנה, בואו נבדוק את האינטגרציה של מודלים של GitHub עם יישום הדוגמה שלנו ב-[`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. פתחו את הטרמינל בסביבת הפיתוח שלכם.
2. עברו לדוגמת מודלים של GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. הגדירו את אסימון GitHub שלכם כמשתנה סביבה:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. הריצו את היישום:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

אתם אמורים לראות פלט דומה ל:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### הבנת קוד הדוגמה

ראשית, בואו נבין מה הרגע הרצנו. הדוגמה תחת `examples/github-models` משתמשת ב-SDK של OpenAI Java כדי להתחבר למודלים של GitHub:

**מה הקוד הזה עושה:**
- **מתחבר** למודלים של GitHub באמצעות אסימון הגישה האישי שלכם
- **שולח** הודעת "Say Hello World!" פשוטה למודל ה-AI
- **מקבל** ומציג את תגובת ה-AI
- **מאמת** שההגדרה שלכם פועלת כראוי

**תלות מרכזית** (ב-`pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**קוד ראשי** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## סיכום

מעולה! עכשיו יש לכם הכל מוגדר:

- יצרתם אסימון גישה אישי ל-GitHub עם ההרשאות הנכונות לגישה למודל AI
- הפעלתם את סביבת הפיתוח שלכם ב-Java (בין אם זה Codespaces, מכולות פיתוח, או מקומי)
- התחברתם למודלים של GitHub באמצעות SDK של OpenAI Java לפיתוח AI חינמי
- בדקתם שהכל עובד עם דוגמה פשוטה שמתקשרת עם מודלים של AI

## השלבים הבאים

[פרק 3: טכניקות ליבה ב-AI גנרטיבי](../03-CoreGenerativeAITechniques/README.md)

## פתרון בעיות

נתקלתם בבעיות? הנה בעיות נפוצות ופתרונות:

- **האסימון לא עובד?**  
  - ודאו שהעתקתם את האסימון כולו ללא רווחים נוספים
  - בדקו שהאסימון מוגדר כראוי כמשתנה סביבה
  - וודאו שלאסימון יש את ההרשאות הנכונות (Models: Read and write)

- **Maven לא נמצא?**  
  - אם אתם משתמשים במכולות פיתוח/Codespaces, Maven אמור להיות מותקן מראש
  - עבור הגדרה מקומית, ודאו ש-Java 21+ ו-Maven 3.9+ מותקנים
  - נסו `mvn --version` כדי לוודא את ההתקנה

- **בעיות חיבור?**  
  - בדקו את חיבור האינטרנט שלכם
  - וודאו ש-GitHub נגיש מהרשת שלכם
  - ודאו שאינכם מאחורי חומת אש שחוסמת את נקודת הקצה של מודלים של GitHub

- **מכולת פיתוח לא מתחילה?**  
  - ודאו ש-Docker Desktop פועל (עבור פיתוח מקומי)
  - נסו לבנות מחדש את המכולה: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **שגיאות קומפילציה ביישום?**
  - ודאו שאתם בתיקייה הנכונה: `02-SetupDevEnvironment/examples/github-models`
  - נסו לנקות ולבנות מחדש: `mvn clean compile`

> **זקוקים לעזרה?**: עדיין יש בעיות? פתחו בעיה במאגר ואנו נעזור לכם.

**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עשויים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית צריך להיחשב כמקור סמכותי. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי על ידי אדם. איננו נושאים באחריות לאי הבנות או לפרשנויות שגויות הנובעות משימוש בתרגום זה.