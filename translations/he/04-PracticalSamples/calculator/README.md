# מדריך מחשבון MCP למתחילים

## תוכן עניינים

- [מה תלמדו](#מה-תלמדו)
- [דרישות מוקדמות](#דרישות-מוקדמות)
- [גרסאות תלות](#גרסאות-תלות)
- [הבנת מבנה הפרויקט](#הבנת-מבנה-הפרויקט)
- [הסבר על רכיבים מרכזיים](#הסבר-על-רכיבים-מרכזיים)
  - [1. האפליקציה הראשית](#1-האפליקציה-הראשית)
  - [2. שירות המחשבון](#2-שירות-המחשבון)
  - [3. לקוח ישיר ל-MCP](#3-לקוח-ישיר-ל-mcp)
  - [4. לקוח מונע בינה מלאכותית](#4-לקוח-מונע-בינה-מלאכותית)
- [הרצת הדוגמאות](#הרצת-הדוגמאות)
- [בדיקות אופליין](#בדיקות-אופליין)
- [איך הכל עובד ביחד](#איך-הכל-עובד-ביחד)
- [שלבים הבאים](#שלבים-הבאים)

## מה תלמדו

מדריך זה מסביר כיצד לבנות שירות מחשבון באמצעות פרוטוקול הקשר מודל (MCP). תלמדו:

- כיצד ליצור שירות שבו הבינה המלאכותית יכולה להשתמש ככלי
- כיצד להגדיר תקשורת ישירה עם שירותי MCP
- כיצד דגמי בינה מלאכותית יכולים לבחור אוטומטית אילו כלים להשתמש
- ההבדל בין קריאות פרוטוקול ישירות לאינטראקציות בסיוע בינה מלאכותית

## דרישות מוקדמות

לפני שמתחילים, יש לוודא שיש לכם:
- Java 21 או גרסה גבוהה יותר מותקנת
- Maven לניהול תלות
- הבנה בסיסית של Java ו-Spring Boot

רק לקוחות הבינה המלאכותית דורשים פריסת Azure OpenAI ואימות עם `DefaultAzureCredential`,
למשל חיבור Azure CLI קיים במחשב המקומי או זהות מנוהלת ב-Azure. זהות זו צריכה
את תפקיד Cognitive Services OpenAI User במשאב. עיינו ב-[פרק 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
השרת, לקוח ה-SDK הישיר וכל הבדיקות האוטומטיות אינם דורשים חשבון Azure או גישה לדגם.

## גרסאות תלות

תלויות גרסה מאומתות בתאריך 2026-09-14:

| תלות | גרסה |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (מנוהל על ידי Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| מתאם OpenAI רשמי של LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (מנוהל על ידי Boot) | 6.0.3 |

מתאמי MCP ו-OpenAI הרשמיים הם מהדורות בטא פורסמו במאבן מרכזי, לא תצוגות מהירות.
גרסאותיהם שונות מהליבה של LangChain4j. אין צורך במאגרי snapshot או milestone.
תלויות שמיועדות רק ללקוח מוגדרות עם תחום בדיקה כי הדוגמאות הרצות נמצאות תחת `src/test/java`.

## הבנת מבנה הפרויקט

לפרויקט המחשבון יש כמה קבצים חשובים:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## הסבר על רכיבים מרכזיים

### 1. האפליקציה הראשית

**קובץ:** `McpServerApplication.java`

זהו נקודת הכניסה לשירות המחשבון שלנו. זוהי אפליקציית Spring Boot סטנדרטית עם תוספת מיוחדת אחת:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**מה שזה עושה:**
- מפעיל שרת אינטרנט Spring Boot על פורט 8080
- יוצר `ToolCallbackProvider` שהופך את שיטות המחשבון שלנו לזמינות ככלים ב-MCP
- התווית `@Bean` מודיעה ל-Spring לנהל את זה כרכיב שיכולים להשתמש בו חלקים אחרים

### 2. שירות המחשבון

**קובץ:** `CalculatorService.java`

כאן מתבצעת כל המתמטיקה. כל שיטה מסומנת עם `@Tool` כדי להפוך אותה זמינה דרך MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // עוד פעולות מחשבון...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**תכונות עיקריות:**

1. **תווית `@Tool`**: מסבירה ל-MCP כי שיטה זו ניתנת לקריאה על ידי לקוחות חיצוניים
2. **תיאורים ברורים**: לכל כלי יש תיאור שמסייע לדגמי AI להבין מתי להשתמש בו
3. **פורמט החזרה עקבי**: כל הפעולות מחזירות מחרוזות קריאות לבני אדם כמו "5.00 + 3.00 = 8.00"
4. **טיפול בשגיאות**: חלוקה באפס ושורשים ריבועים שליליים מחזירים הודעות שגיאה

**פעולות זמינות:**
- `add(a, b)` - מחבר שני מספרים
- `subtract(a, b)` - מחסר את השני מהראשון
- `multiply(a, b)` - מכפיל שני מספרים
- `divide(a, b)` - מחלק את הראשון בשני (עם בדיקת אפס)
- `power(base, exponent)` - מעלה את הבסיס בחזקה של האקספוננט
- `squareRoot(number)` - מחשב שורש ריבועי (עם בדיקת שליליות)
- `modulus(a, b)` - מחזיר שארית מחלוקה
- `absolute(number)` - מחזיר ערך מוחלט
- `help()` - מחזיר מידע על כל הפעולות

### 3. לקוח ישיר ל-MCP

עיין ב-[SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

לקוח זה משתמש ב-`HttpClientStreamableHttpTransport` בכתובת `/mcp`, מאתחל את החיבור,
מבצע פינג לשרת ועוקב אחרי גלילת רשימת הכלים. הוא בודק שכל תשע הכלים הצפויים
קיימים ומבצע קריאה לכל אחד מהם, כולל `modulus` ו-`help`, ללא שימוש בדגם AI.

בונה הבקשה הנוכחי נראה כך:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

שגיאות בפרוטוקול גורמות לכישלון הלקוח במקום להדפיס הצלחה מטעית. לקוח MCP
נסגר בשימוש ב-try-with-resources, גם במקרה של כשל בגילוי או קריאת כלי.

### 4. לקוח מונע בינה מלאכותית

עיין ב-[LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
ו-[Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` מממש את ממשק `ChatModel` הנוכחי של LangChain4j.
`StreamableHttpMcpTransport` מחבר אותו לאותה נקודת קצה `/mcp` כמו לקוח ה-SDK.
`AiServices` מגלה את הכלים ומנהל את השיחה בקשר לשיחות כלי ותוצאותיהם.

הפריסה המוגדרת היא **GPT-5.6 Luna**, עם ניתוק מפורש של ההיגיון:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

הגדרות ברירת המחדל חלות על כל השלמות, כולל המשכים אחרי ביצוע כלי.
הלקוח משתמש באסמכתא `BearerTokenCredential` מתחדשת הנתמכת ב-`DefaultAzureCredential`
ותחום `https://ai.azure.com/.default`, לא בטוקן חד-פעמי כמפתח API.
כתובות משאבים וכתובות שכבר מסתיימות ב-`/openai/v1` מתקבלות שתיהן.

הבוט שומר היסטוריית שיחה מוגבלת, מדפיס `Tool executed: ...` עם התוצאה האמיתית מ-MCP,
וכושל אם תגובה מדלגת על כלים. לולאות כלי מוגבלות לארבעה סבבים.
שגיאות אימות, דגם, MCP וכלי מתפשטות; ניסיונות דגם אוטומטיים מנוטרלים.
גם תחבורת MCP/לקוח וגם לקוח OpenAI הרשמי נסגרים בהצלחה או כישלון.

## הרצת הדוגמאות

### שלב 1: הפעלת שרת המחשבון

לא נדרש תצורת Azure עבור השרת. הפקודות למטה מריצות מתוך תיקיית הדוגמה הזו.
הדוגמה משתמשת בפורט **18081** כדי להימנע מקונפליקט עם דוגמה אחרת; ברירת המחדל נשארת 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

נקודת הקצה של MCP היא `http://localhost:18081/mcp`. מידע על בריאות וגילוי נמצא ב-
`http://localhost:18081/health` ו-`http://localhost:18081/info`.
HTTP ניתן להזרים מחליף את תעבורת SSE הישנה בלבד; `/sse` ו-`/v1/tools` אינם נקודות קצה.

### שלב 2: בדיקה עם לקוח ישיר

בטרמינל PowerShell נוסף:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

אין צורך בהזנה. כל תשע הכלים מופעלים. תוצאות חשבוניות צפויות כוללות
8, 6, 42, 5, 256, 4, 2, ו-5.5, ואחריהן טקסט העזרה.

### שלב 3: בדיקה עם לקוח AI

לאחר אימות כמפורט בדרישות המוקדמות, הגדר את לקוח הבינה המלאכותית באותו טרמינל:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

צפו לשורה `Tool executed: add` עם התוצאה `41.80`, ואחריה תשובת הדגם.
מצב הבקשה בודדת יוצא מבלי להמתין להזנה. להריץ את ההדגמה המקורית עם ארבע בקשות:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

ההדגמה קוראת ל-`add`, `squareRoot`, `help`, ולאחר מכן לפעולת השרשרת של `power` ואז `divide`.
תשובות מספריות צפויות הן 41.8, 12 ו-64. השמטת ארגומנטים מפעילה גם את ההדגמה.

### שלב 4: הפעלת הבוט האינטראקטיבי

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

הזינו `Multiply 6 by 7 using the calculator service`, ואז `exit` או `quit`.
צפו לתוצאת כלי `multiply` אמיתית של 42. שורות ריקות מוזנחות; EOF גם מסיים את המפגש.
לבדיקה מהירה לא אינטראקטיבית של נקודת הכניסה הזו:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

נקודות כניסה של AI מקבלות `--prompt "question"`, `--demo`, ו-`--interactive`.
אפשרויות לא תקינות נכשלות לפני פתיחת חיבור. כל ארגומנט Maven `-D...` מצוטט במלואו
עבור PowerShell. בבאש משתמשים ב-`export NAME=value` במקום ב-`$env:NAME = "value"`.

**מכסה:** הריצו דוגמאות AI בזה אחר זה. בקשה פשוטה בדרך כלל דורשת שתי בקשות לדגם;
ההדגמה המלאה בדרך כלל דורשת תשע, כולל המשכים לפי תוצאות הכלים. בפריסת 10 RPM משותפת,
המתינו לחלון מכסה רענן לפני הריצה הבאה. שגיאה 429 נכשלת בקבלת התראה ללא
ניסיונות חוזרים אוטומטיים; עקבו אחר הוראות ההמתנה של השירות. ספירת הבקשות המדויקת תלויה בדגם.
בדיקות אופליין אינן צורכות מכסה ואינן מוודאות זמינות או איכות מענה של לונה.

### תצורה וכיבוי

| הגדרה | ברירת מחדל / התנהגות |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; כתובת בסיסית, ללא `/mcp` |
| `-Dmcp.server.url=...` | מחליף את `MCP_SERVER_URL` עבור כל הלקוחות |
| `AZURE_OPENAI_ENDPOINT` | דרוש רק ללקוחות AI; כתובת משאב או כתובת `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; שם פריסת Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; מספר חיובי שלם |
| מאמץ הסקה | תמיד `none`, כולל המשכי לולאות כלי |

פריסה מוחלפת חייבת לתמוך ב-`reasoning_effort=none` ו-`max_completion_tokens`.
הלקוחות אינם קוראים אוטומטית קובץ `.env`. עצרו את השרת עם `Ctrl+C` לאחר הבדיקה.
לקוחות מסיימים בצורה רגילה ללא `System.exit` או השהיית כיבוי.

## בדיקות אופליין

```powershell
mvn -B -ntp clean verify
```

כל הבדיקות הן אופליין יחסית ל-Azure: חבילת הפרוטוקול מפעילה שרת Spring ו-
דמה תואם OpenAI על פורטים אקראיים, ואז סוגרת אותם. Maven עשוי עדיין להזדקק
להורדת תלותים. לא נעשה שימוש באסמכתות, פריסה חיה, או שרת MCP קיים.

- בדיקות יחידה של המחשבון כוללות את כל הפעולות החשבוניות, תוצאות עשרוניות, עזרה, וטעויות תחום.
- בדיקות MCP כוללות אתחול, גילוי, כל תשע קריאות הכלים, כשלים בכלים, ונתוני בריאות/מידע.
- בדיקות פרוטוקול AI מבצעות את ההדגמה המלאה והבוט האינטראקטיבי מול מחשבון אמיתי,
  בודקות שתוצאות הכלי מזינות את ההשלמה הבאה, ומפענחות כל גוף HTTP עבור לונה,
  `reasoning_effort: "none"`, ו-`max_completion_tokens` ללא `max_tokens` ישן.
- בדיקות תצורה/קלט כוללות החלפות פריסה ונקודות קצה, שורות ריקות, EOF, יציאה/סיום,
  מצב בקשה יחידה, אפשרויות לא תקינות, והתפשטות שגיאות. בדיקות מכסה מוכיחות ששגיאה 429 לא מתנסה מחדש.

## איך הכל עובד ביחד

הנה הזרימה המלאה כשאתה שואל את ה-AI "מה זה 5 + 3?":

1. **אתה** שואל את ה-AI בשפה טבעית
2. **ה-AI** מנתח את בקשתך ומבין שאתה רוצה חיבור
3. **ה-AI** קורא לשרת MCP: `add(5.0, 3.0)`
4. **שירות המחשבון** מבצע: `5.0 + 3.0 = 8.0`
5. **שירות המחשבון** מחזיר: `"5.00 + 3.00 = 8.00"`
6. **ה-AI** מקבל את התוצאה ומנסח תגובה טבעית
7. **אתה** מקבל: "סכום 5 ו-3 הוא 8"

## שלבים הבאים

לעוד דוגמאות, ראה [פרק 04: דוגמאות מעשיות](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום אוטומטי [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו הטבעית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אדם. אנו לא אחראים לכל אי-הבנה או פירוש שגוי הנובע מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->