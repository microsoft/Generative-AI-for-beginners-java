# מדריך ליצירת סיפור על חיית מחמד למתחילים

העלה תמונת חיית מחמד, נתח אותה עם GPT-5.6 Luna, ויצר סיפור מתוך התיאור המתקבל. שני בקשות המודל משתמשות ב-`reasoning_effort: none`.

| רכיב | גרסה |
| --- | --- |
| Java | 21 ומעלה |
| Spring Boot | 4.1.1 |
| ערכת כלים Java של OpenAI | 4.63.1 |
| Azure Identity | 1.18.6 |

## תוכן עניינים

- [דרישות מוקדמות](#דרישות-מוקדמות)
- [הבנת מבנה הפרויקט](#הבנת-מבנה-הפרויקט)
- [הסבר רכיבי הליבה](#הסבר-רכיבי-הליבה)
  - [1. האפליקציה הראשית](#1-אפליקציה-ראשית)
  - [2. בותר האינטרנט](#2-בותר-האינטרנט)
  - [3. שירות הסיפור](#3-שירות-הסיפור)
  - [4. תבניות רשת](#4-תבניות-רשת)
  - [5. תצורה](#5-תצורה)
- [הרצת האפליקציה](#הרצת-האפליקציה)
- [בדיקות לא מקוונות](#בדיקות-לא-מקוונות)
- [כיצד הכל עובד יחד](#כיצד-הכל-עובד-יחד)
- [הבנת האינטגרציה של ה-AI](#הבנת-האינטגרציה-של-ה-ai)
- [השלבים הבאים](#השלבים-הבאים)

## דרישות מוקדמות

לפני שתתחיל, ודא שיש לך:
- Java 21 או גרסה גבוהה יותר מותקנת
- Maven לניהול תלותים
- פריסת Azure AI Foundry של GPT-5.6 Luna בשם `gpt-5.6-luna`, או הגדרת `AZURE_OPENAI_DEPLOYMENT` המצביעה על פריסה זו. ראה [פרק 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) לפריסת המשאבים והתחברות באמצעות `az login` לאימות ללא מפתח. הפריסה חייבת לתמוך בקלט תמונה וב-`reasoning_effort: none`.
- הבנה בסיסית של Java, Spring Boot ופיתוח אינטרנט

## הבנת מבנה הפרויקט

לפרויקט סיפור חיית המחמד יש מספר קבצים חשובים:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## הסבר רכיבי הליבה

### 1. אפליקציה ראשית

**קובץ:** `PetStoryApplication.java`

זהו נקודת הכניסה לאפליקציית Spring Boot שלנו:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**מה זה עושה:**
- ההערת `@SpringBootApplication` מאפשרת קונפיגורציה אוטומטית וסריקת רכיבים
- מתחיל שרת אינטרנט משולב (Tomcat) על פורט 8080
- יוצר את כל Beens ושירותי ה-Spring הנחוצים אוטומטית

### 2. בותר האינטרנט

**קובץ:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| נקודת קצה | בקשה | תגובה מוצלחת |
| --- | --- | --- |
| `GET /` | אין גוף | טופס HTML להעלאת קובץ עם אסימון CSRF |
| `POST /analyze-image` | `multipart/form-data`, שדה קובץ `image` | JSON: `{"description":"חיית מחמד שובבה..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, שדה `description` | דף HTML עם התיאור והסיפור שנוצר |

שני נקודות הקצה POST דורשות את עוגיית הסשן ואסימון ה-CSRF שהתקבלו מ-`GET /`. סקריפט ההעלאה שולח את הערך הנסתר `_csrf` בכותרת `X-CSRF-TOKEN`; שליחת הסיפור שולחת זאת כשדה `_csrf` בטופס. לקוחות API חייבים לשמור על העוגייה בין הבקשות. אלה נקודות קצה של טפסים, לא נקודות JSON.

התיאורים חייבים להיות לא ריקים ולא ליותר מ-1000 תווים. הבורר קוצר את התיאור ומסיר `<`, `>`, מרכאות כפולות, גרשיים, ו-`&` לפני העברתו לשירות. התבנית לתוצאה גם מבצעת בריחת תווים על הפלט עם `th:text`.

כשיש כישלונות באימות תמונה מחזירים HTTP 400 עם שדה `error`; כשיש כישלונות במודל מחזירים HTTP 502 עם שדה `error` וללא `description`. תיאורי סיפור לא תקינים או כישלונות במודל מנותבים מחדש ל-`/` עם שגיאה גלויה. שדות חובה חסרים מחזירים HTTP 400, ואסימוני CSRF חסרים או לא תקינים מחזירים HTTP 403. לא מוצגים תיאורי גיבוי או סיפורים כהצלחות AI.

### 3. שירות הסיפור

**קובץ:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

ערכת הכלים הרשמית של OpenAI Java 4.63.1 קוראת ל-API של Azure AI Foundry התואם ל-OpenAI Chat Completions. Azure Identity 1.18.6 מספקת אסימון Microsoft Entra דרך `DefaultAzureCredential`; אין צורך במפתח API.

| פעולה | קלט | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | נתוני תמונה מקודדים ככתובת URL בסיס64 עם סוג MIME שהועלה | 300 |
| `generateStory` | תיאור חיית מחמד בהודעת משתמש | 800 |

שתי הבקשות משתמשות בפריסת המודל המוגדרת, כברירת מחדל `gpt-5.6-luna`, וקובעות במפורש `ReasoningEffort.NONE` (`reasoning_effort: none`). אף בקשה אינה שולחת `temperature` או את הפרמטר הישן `max_tokens`.

ניתוח תמונה מקבל JPEG, PNG, GIF, ו-WebP, דוחה תמונות ריקות וקבצים מעל 10MB, ומגביל את התיאור המתקבל ל-1000 תווים. בקשת הסיפור מבקשת סיפור קצר ידידותי למשפחה. בחירות ריקות או תוכן ריק במודל הן שגיאות, וכישלונות שומרים על סיבת הכישלון המקורית לאבחון צד השרת. לקוח ה-SDK נסגר כאשר האפליקציה נסגרת.

### 4. תבניות רשת

**קובץ:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (טופס העלאה)

הדף מתחיל עם בורר תמונות, לא אזור טקסט לתיאור. **נתח תמונה** מציג מראש את התמונה שנבחרה ושולח אותה ל-`/analyze-image`. תגובה מוצלחת מציגה את התיאור, ממלאת את שדה `description` הנסתר, ומגלה את כפתור **צור סיפור**. כפתור זה שולח את הטופס הקיים ל-`/generate-story`.

אין הורדת מודל בדפדפן או תלות ב-CDN. ניתוח התמונה מתבצע בשרת דרך הפריסה של Azure שהוגדרה. כישלונות נשארים גלויים ואינם מאפשרים יצירת סיפור עם תיאור מזויף. בחירת קובץ שונה מוחקת את הניתוח הקודם.

**קובץ:** `result.html` (תצוגת הסיפור)

מציג את הסיפור שנוצר:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**מאפייני התבנית:**

1. **אינטגרציה עם Thymeleaf**: משתמש בתכונות `th:` לתוכן דינמי
2. **עיצוב רספונסיבי**: עיצוב CSS למובייל ולשולחן עבודה
3. **טיפול בשגיאות**: מציג שגיאות אימות למשתמשים
4. **טיפול בהעלאה**: JavaScript מציג מראש את התמונה, שולח בקשת multipart עם הגנת CSRF, ומציג את התיאור המוחזר

### 5. תצורה

**קובץ:** `application.properties`

הגדרות תצורה לאפליקציה:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**הסבר התצורה:**

1. **העלאת קבצים**: מוגבלים עד לגודל 10MB גם הקובץ עצמו וגם בקשת ה-multipart; שמור על תמונות בגודל נמוך מהגבול כדי להשאיר מקום לכותרות ה-multipart
2. **רישום (לוגינג)**: שולט אילו מידע נרשם במהלך הריצה
3. **Azure AI Foundry**: מגדיר את נקודת הקצה ופריסת המודל לשימוש (אימות ללא מפתח)
4. **אבטחה**: הגנת CSRF נשארת מופעלת; אבחון המודל נרשם בשרת, בעוד שהבורר מציג הודעות שגיאה כלליות בעת כישלונות מודל

## הרצת האפליקציה

### שלב 1: התחבר וקבע את נקודת הקצה שלך

האימות הוא ללא מפתח (Microsoft Entra ID), כך שאין מפתח API. התחבר וקבע את נקודת הקצה Foundry שלך:

**Windows (שורת הפקודה):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**מדוע זה נדרש:**
- Azure AI Foundry משתמש ב-Microsoft Entra ID לאימות בקשות אינפרנציה
- אימות ללא מפתח אומר שאין סודות בקוד המקור או בסביבה
- החשבון שלך צריך את התפקיד **Cognitive Services OpenAI User** במשאב

שם הפריסה ברירת המחדל הוא `gpt-5.6-luna`. אם לפריסת GPT-5.6 Luna שלך יש שם אחר, הגדר את `AZURE_OPENAI_DEPLOYMENT` באותו מסוף לפני הפעלת האפליקציה. גם ניתוח התמונה וגם יצירת הסיפור משתמשים בהגדרה זו.

### שלב 2: בניה והרצה

נווט לספריית הפרויקט:
```bash
cd 04-PracticalSamples/petstory
```

בניה של קובץ JAR עצמאי והרצת כל הבדיקות הלא מקוונות:
```bash
mvn clean package
```

הפעל את השרת:
```bash
mvn spring-boot:run
```

האפליקציה תתחיל בכתובת `http://localhost:8080`.

לחלופין, הפעל את קובץ ה-JAR הארוז על פורט פנוי, לדוגמה:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

עבור פקודה זו, פתח את `http://localhost:8083/`. אותם נתיבי `/analyze-image` ו- `/generate-story` זמינים על הפורט הנבחר.

### שלב 3: בדוק את האפליקציה

1. **פתח** את `http://localhost:8080` בדפדפן שלך
2. **בחר** תמונת חיית מחמד ברורה בפורמט JPEG, PNG, GIF, או WebP, מתחת ל-10MB
3. **לחץ** על "נתח תמונה" והמתן לתיאור חיית המחמד
4. **לחץ** על "צור סיפור" לאחר ניתוח מוצלח
5. **הצג** את הסיפור והשתמש בקישור בדף התוצאה כדי לחזור לטופס ההעלאה

הזרימה המוצלחת של תמונה לסיפור מבצעת שתי קריאות למודל, אחת לכל כפתור. אינפרנציה חיה צורכת את המינון של הפריסה שלך ועלולה לגרור חיובים; הרץ בדיקות מעשנות אחת אחרי השנייה כשמשתפים פריסה עם הגבלת קצב. טעינת דף הבית לא מפעילה את המודל.

## בדיקות לא מקוונות

מספריית הדוגמאות, הרץ:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) תופס בקשות אמיתיות ל-SDK של OpenAI עם מתקין HTTP חזרה לפנים. הבדיקה בודקת את פריסת שניהם, `reasoning_effort: none`, מגבלות טוקנים, מטען תמונה, אימות קלט, תגובות ריקות, ושגיאות מקוריות.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) משתמש ב-MockMvc עם שירות מודל מדומה לבדיקת דפי Thymeleaf מוצגים, חוזה העלאה, CSRF, אימות, בריחת פלט, וכישלונות גלויים. בדיקות אלה אינן זקוקות לאישורי Azure ואינן קוראות לאינפרנציה בתשלום של Azure. Maven כותב דוחות Surefire תחת `target/surefire-reports`.

## כיצד הכל עובד יחד

כך נראית זרימת העבודה המלאה בעת יצירת סיפור על חיית מחמד:

1. **בחירת תמונה**: אתה בוחר תמונת חיית מחמד בטופס ההעלאה
2. **העלאת תמונה**: "נתח תמונה" שולח בקשת POST מרובת חלקים ל-/analyze-image עם כותרת CSRF
3. **ניתוח תמונה**: `StoryService` שולח את התמונה ל-GPT-5.6 Luna עם הגדרת reasoning ל-none
4. **הצגת תיאור**: הדפדפן מציג את התיאור שהוחזר ושומר אותו בטופס
5. **שליחת סיפור**: "צור סיפור" שולח את `description` ו- `_csrf` ל-/generate-story
6. **יצירת הסיפור**: הבורר מאמת את התיאור וקורא לאותה פריסה עם reasoning מוגדר ל-none
7. **הצגת התבנית**: Thymeleaf מבצע בריחה ומציג את התיאור והסיפור בדף התוצאה

**זרימת טיפול בשגיאות:**
אם המודל נכשל, השרת רושם את הסיבה. ניתוח תמונה מחזיר HTTP 502 והדפדפן מציג את השגיאה מבלי לחשוף את "צור סיפור". יצירת הסיפור מנותבת לטופס עם הודעת שגיאה. אף נתיב אינו מחליף בשקט תוצאה שנכתבה מראש.

## הבנת האינטגרציה של ה-AI

### Azure AI Foundry (ללא מפתח)
השירות מגדיר את ה-SDK עם נקודת הקצה `/openai/v1/` של המשאב שלך. `DefaultAzureCredential` ו-`AuthenticationUtil.getBearerTokenSupplier` מספקים אסימוני Microsoft Entra עבור `https://ai.azure.com/.default`. פיתוח מקומי יכול להשתמש בהתחברות Azure CLI שלך; אפליקציה המופעלת ב-Azure יכולה להשתמש בזיהוי מנוהל עם ההרשאות הנחוצות למשאב.

### הנדסת פרומפטים
ניתוח תמונה מבקש תכונות נראות לעין של חיית המחמד בפסקה קצרה ואומר למודל להתייחס לטקסט בתמונה כנתונים, לא כהוראות. יצירת סיפור משתמשת בתיאור שהוחזר בבקשת כתיבה קצרה, ידידותית למשפחה. אף אחד מהקריאות לא מאפשר הסקה או מגדיר טמפרטורה.

### עיבוד תגובת המודל
מנהל התגובות המשותף דוחה בחירות חסרות ותוכן ריק או ריק מרווחים, מקצר תוכן תקין ושומר על כישלונות מקוריים לאבחון. תיאורי תמונה מוגבלים ל-1000 תווים כדי להתאים לטופס הסיפור הבא. כישלון המודל המקורי נשמר לאבחון אך לא מוצג למשתמש.

## השלבים הבאים

לדוגמאות נוספות ראו [פרק 04: דוגמאות מעשיות](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום אוטומטי [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו הטבעית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אדם. אנו לא אחראים לכל אי-הבנה או פירוש שגוי הנובע מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->