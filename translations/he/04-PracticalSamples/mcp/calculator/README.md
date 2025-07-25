<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:50:25+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "he"
}
-->
# מדריך למתחילים למחשבון MCP

## תוכן עניינים

- [מה תלמדו](../../../../../04-PracticalSamples/mcp/calculator)
- [דרישות מקדימות](../../../../../04-PracticalSamples/mcp/calculator)
- [הבנת מבנה הפרויקט](../../../../../04-PracticalSamples/mcp/calculator)
- [הסבר על הרכיבים המרכזיים](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. אפליקציה ראשית](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. שירות מחשבון](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. לקוח MCP ישיר](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. לקוח מבוסס AI](../../../../../04-PracticalSamples/mcp/calculator)
- [הרצת הדוגמאות](../../../../../04-PracticalSamples/mcp/calculator)
- [איך הכל עובד יחד](../../../../../04-PracticalSamples/mcp/calculator)
- [השלבים הבאים](../../../../../04-PracticalSamples/mcp/calculator)

## מה תלמדו

מדריך זה מסביר כיצד לבנות שירות מחשבון באמצעות פרוטוקול Model Context Protocol (MCP). תלמדו:

- כיצד ליצור שירות שה-AI יכול להשתמש בו ככלי
- כיצד להגדיר תקשורת ישירה עם שירותי MCP
- כיצד מודלים של AI יכולים לבחור באופן אוטומטי אילו כלים להשתמש
- ההבדל בין קריאות פרוטוקול ישירות לבין אינטראקציות בסיוע AI

## דרישות מקדימות

לפני שמתחילים, ודאו שיש לכם:
- Java 21 או גרסה גבוהה יותר מותקנת
- Maven לניהול תלות
- חשבון GitHub עם אסימון גישה אישי (PAT)
- הבנה בסיסית של Java ו-Spring Boot

## הבנת מבנה הפרויקט

לפרויקט המחשבון יש מספר קבצים חשובים:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## הסבר על הרכיבים המרכזיים

### 1. אפליקציה ראשית

**קובץ:** `McpServerApplication.java`

זהו נקודת הכניסה לשירות המחשבון שלנו. זו אפליקציית Spring Boot סטנדרטית עם תוספת מיוחדת:

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

**מה זה עושה:**
- מפעיל שרת אינטרנט של Spring Boot על פורט 8080
- יוצר `ToolCallbackProvider` שמאפשר לשיטות המחשבון שלנו להיות זמינות ככלי MCP
- האנומציה `@Bean` אומרת ל-Spring לנהל את זה כרכיב ששאר החלקים יכולים להשתמש בו

### 2. שירות מחשבון

**קובץ:** `CalculatorService.java`

כאן מתבצעים כל החישובים. כל שיטה מסומנת עם `@Tool` כדי להפוך אותה לזמינה דרך MCP:

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**תכונות מרכזיות:**

1. **אנומציית `@Tool`**: זה אומר ל-MCP שהשיטה הזו יכולה להיקרא על ידי לקוחות חיצוניים
2. **תיאורים ברורים**: לכל כלי יש תיאור שעוזר למודלים של AI להבין מתי להשתמש בו
3. **פורמט החזרה עקבי**: כל הפעולות מחזירות מחרוזות קריאות כמו "5.00 + 3.00 = 8.00"
4. **טיפול בשגיאות**: חלוקה באפס ושורש ריבועי שלילי מחזירים הודעות שגיאה

**פעולות זמינות:**
- `add(a, b)` - חיבור שני מספרים
- `subtract(a, b)` - חיסור השני מהראשון
- `multiply(a, b)` - כפל שני מספרים
- `divide(a, b)` - חלוקת הראשון בשני (עם בדיקת אפס)
- `power(base, exponent)` - העלאת בסיס בחזקה
- `squareRoot(number)` - חישוב שורש ריבועי (עם בדיקת שליליים)
- `modulus(a, b)` - מחזיר את השארית של חלוקה
- `absolute(number)` - מחזיר ערך מוחלט
- `help()` - מחזיר מידע על כל הפעולות

### 3. לקוח MCP ישיר

**קובץ:** `SDKClient.java`

לקוח זה מתקשר ישירות עם שרת MCP ללא שימוש ב-AI. הוא קורא ידנית לפונקציות מחשבון ספציפיות:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**מה זה עושה:**
1. **מתחבר** לשרת המחשבון בכתובת `http://localhost:8080`
2. **מציג** את כל הכלים הזמינים (פונקציות המחשבון שלנו)
3. **קורא** לפונקציות ספציפיות עם פרמטרים מדויקים
4. **מדפיס** את התוצאות ישירות

**מתי להשתמש בזה:** כשאתם יודעים בדיוק איזה חישוב אתם רוצים לבצע ורוצים לקרוא לו בצורה תכנותית.

### 4. לקוח מבוסס AI

**קובץ:** `LangChain4jClient.java`

לקוח זה משתמש במודל AI (GPT-4o-mini) שיכול להחליט באופן אוטומטי אילו כלים של מחשבון להשתמש:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**מה זה עושה:**
1. **יוצר** חיבור למודל AI באמצעות אסימון GitHub שלכם
2. **מתחבר** ל-AI לשרת MCP של המחשבון שלנו
3. **נותן** ל-AI גישה לכל הכלים של המחשבון שלנו
4. **מאפשר** בקשות בשפה טבעית כמו "חשב את הסכום של 24.5 ו-17.3"

**ה-AI באופן אוטומטי:**
- מבין שאתם רוצים לחבר מספרים
- בוחר את הכלי `add`
- קורא ל-`add(24.5, 17.3)`
- מחזיר את התוצאה בתגובה טבעית

## הרצת הדוגמאות

### שלב 1: הפעלת שרת המחשבון

ראשית, הגדירו את אסימון GitHub שלכם (נדרש עבור לקוח ה-AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

הפעילו את השרת:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

השרת יתחיל בכתובת `http://localhost:8080`. אתם אמורים לראות:
```
Started McpServerApplication in X.XXX seconds
```

### שלב 2: בדיקה עם לקוח ישיר

בטרמינל חדש:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

תראו פלט כמו:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### שלב 3: בדיקה עם לקוח AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

תראו שה-AI משתמש בכלים באופן אוטומטי:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## איך הכל עובד יחד

הנה הזרימה המלאה כשאתם שואלים את ה-AI "מה זה 5 + 3?":

1. **אתם** שואלים את ה-AI בשפה טבעית
2. **ה-AI** מנתח את הבקשה שלכם ומבין שאתם רוצים חיבור
3. **ה-AI** קורא לשרת MCP: `add(5.0, 3.0)`
4. **שירות המחשבון** מבצע: `5.0 + 3.0 = 8.0`
5. **שירות המחשבון** מחזיר: `"5.00 + 3.00 = 8.00"`
6. **ה-AI** מקבל את התוצאה ומעצב תגובה טבעית
7. **אתם** מקבלים: "הסכום של 5 ו-3 הוא 8"

## השלבים הבאים

לדוגמאות נוספות, ראו [פרק 04: דוגמאות מעשיות](../../README.md)

**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). בעוד שאנו שואפים לדיוק, יש להיות מודעים לכך שתרגומים אוטומטיים עשויים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית צריך להיחשב כמקור הסמכותי. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי על ידי אדם. איננו נושאים באחריות לאי הבנות או לפרשנויות שגויות הנובעות משימוש בתרגום זה.