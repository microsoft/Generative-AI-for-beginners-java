<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:49:14+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "he"
}
-->
# מדריך לטכניקות ליבה ב-AI גנרטיבי

## תוכן עניינים

- [דרישות מוקדמות](../../../03-CoreGenerativeAITechniques)
- [תחילת עבודה](../../../03-CoreGenerativeAITechniques)
  - [שלב 1: הגדרת משתנה סביבה](../../../03-CoreGenerativeAITechniques)
  - [שלב 2: מעבר לתיקיית הדוגמאות](../../../03-CoreGenerativeAITechniques)
- [מדריך 1: השלמות ושיחה עם LLM](../../../03-CoreGenerativeAITechniques)
- [מדריך 2: קריאה לפונקציות](../../../03-CoreGenerativeAITechniques)
- [מדריך 3: RAG (יצירה מוגברת על ידי שליפה)](../../../03-CoreGenerativeAITechniques)
- [מדריך 4: AI אחראי](../../../03-CoreGenerativeAITechniques)
- [תבניות נפוצות בדוגמאות](../../../03-CoreGenerativeAITechniques)
- [השלבים הבאים](../../../03-CoreGenerativeAITechniques)
- [פתרון בעיות](../../../03-CoreGenerativeAITechniques)
  - [בעיות נפוצות](../../../03-CoreGenerativeAITechniques)

## סקירה כללית

מדריך זה מספק דוגמאות מעשיות לטכניקות ליבה ב-AI גנרטיבי באמצעות Java ומודלים של GitHub. תלמדו כיצד לעבוד עם מודלים של שפה גדולה (LLMs), ליישם קריאה לפונקציות, להשתמש ביצירה מוגברת על ידי שליפה (RAG), וליישם עקרונות של AI אחראי.

## דרישות מוקדמות

לפני שמתחילים, ודאו שיש לכם:
- Java 21 או גרסה מתקדמת יותר מותקנת
- Maven לניהול תלות
- חשבון GitHub עם טוקן גישה אישי (PAT)

## תחילת עבודה

### שלב 1: הגדרת משתנה סביבה

ראשית, עליכם להגדיר את הטוקן של GitHub כמשתנה סביבה. טוקן זה מאפשר לכם גישה למודלים של GitHub בחינם.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### שלב 2: מעבר לתיקיית הדוגמאות

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## מדריך 1: השלמות ושיחה עם LLM

**קובץ:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### מה תלמדו בדוגמה זו

דוגמה זו מדגימה את המכניקה הבסיסית של אינטראקציה עם מודלים של שפה גדולה (LLM) דרך OpenAI API, כולל אתחול לקוח עם מודלים של GitHub, מבנה הודעות עבור הנחיות מערכת ומשתמש, ניהול מצב שיחה על ידי צבירת היסטוריית הודעות, וכיוונון פרמטרים לשליטה באורך התגובה ורמת היצירתיות.

### מושגים מרכזיים בקוד

#### 1. הגדרת לקוח
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

זה יוצר חיבור למודלים של GitHub באמצעות הטוקן שלכם.

#### 2. השלמה פשוטה
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. זיכרון שיחה
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

ה-AI זוכר הודעות קודמות רק אם תכללו אותן בבקשות הבאות.

### הרצת הדוגמה
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### מה קורה כשמריצים את זה

1. **השלמה פשוטה**: ה-AI עונה על שאלה ב-Java עם הנחיות מערכת
2. **שיחה מרובת סבבים**: ה-AI שומר על הקשר בין שאלות
3. **שיחה אינטראקטיבית**: ניתן לנהל שיחה אמיתית עם ה-AI

## מדריך 2: קריאה לפונקציות

**קובץ:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### מה תלמדו בדוגמה זו

קריאה לפונקציות מאפשרת למודלים של AI לבקש ביצוע של כלים חיצוניים ו-APIs דרך פרוטוקול מובנה שבו המודל מנתח בקשות בשפה טבעית, קובע אילו פונקציות נדרשות עם פרמטרים מתאימים באמצעות הגדרות JSON Schema, ומעבד את התוצאות המוחזרות ליצירת תגובות בהקשר מתאים, בעוד הביצוע בפועל נשאר בשליטת המפתח לצורך אבטחה ואמינות.

### מושגים מרכזיים בקוד

#### 1. הגדרת פונקציה
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

זה מגדיר ל-AI אילו פונקציות זמינות וכיצד להשתמש בהן.

#### 2. זרימת ביצוע פונקציה
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. מימוש פונקציה
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### הרצת הדוגמה
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### מה קורה כשמריצים את זה

1. **פונקציית מזג אוויר**: ה-AI מבקש נתוני מזג אוויר עבור סיאטל, אתם מספקים אותם, וה-AI מעצב תגובה
2. **פונקציית מחשבון**: ה-AI מבקש חישוב (15% מתוך 240), אתם מחשבים, וה-AI מסביר את התוצאה

## מדריך 3: RAG (יצירה מוגברת על ידי שליפה)

**קובץ:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### מה תלמדו בדוגמה זו

RAG (יצירה מוגברת על ידי שליפה) משלב שליפת מידע עם יצירת שפה על ידי הזרקת הקשר ממסמכים חיצוניים להנחיות ה-AI, ומאפשר למודלים לספק תשובות מדויקות על בסיס מקורות ידע ספציפיים במקום להסתמך על נתוני אימון שעשויים להיות מיושנים או לא מדויקים, תוך שמירה על גבולות ברורים בין שאלות המשתמש למקורות מידע סמכותיים באמצעות הנדסת הנחיות אסטרטגית.

### מושגים מרכזיים בקוד

#### 1. טעינת מסמכים
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. הזרקת הקשר
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

המרכאות המשולשות עוזרות ל-AI להבחין בין הקשר לשאלה.

#### 3. טיפול בטוח בתגובות
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

תמיד יש לאמת תגובות API כדי למנוע קריסות.

### הרצת הדוגמה
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### מה קורה כשמריצים את זה

1. התוכנית טוענת את `document.txt` (מכיל מידע על מודלים של GitHub)
2. אתם שואלים שאלה על המסמך
3. ה-AI עונה רק על בסיס תוכן המסמך, ולא על ידע כללי

נסו לשאול: "מהם מודלים של GitHub?" לעומת "מה מזג האוויר?"

## מדריך 4: AI אחראי

**קובץ:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### מה תלמדו בדוגמה זו

דוגמת AI אחראי מדגישה את החשיבות של יישום אמצעי בטיחות ביישומי AI. היא מדגימה מסנני בטיחות שמזהים קטגוריות תוכן מזיקות כולל דברי שנאה, הטרדה, פגיעה עצמית, תוכן מיני ואלימות, ומראה כיצד יישומי AI בייצור צריכים להתמודד בצורה חיננית עם הפרות מדיניות תוכן באמצעות טיפול נכון בחריגות, מנגנוני משוב למשתמש, ואסטרטגיות תגובה חלופיות.

### מושגים מרכזיים בקוד

#### 1. מסגרת בדיקות בטיחות
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. קטגוריות בטיחות שנבדקו
- הוראות אלימות/פגיעה
- דברי שנאה
- הפרות פרטיות
- מידע רפואי שגוי
- פעילויות לא חוקיות

### הרצת הדוגמה
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### מה קורה כשמריצים את זה

התוכנית בודקת הנחיות מזיקות שונות ומראה כיצד מערכת הבטיחות של ה-AI:
1. **חוסמת בקשות מסוכנות** עם שגיאות HTTP 400
2. **מאפשרת תוכן בטוח** להיווצר כרגיל
3. **מגנה על משתמשים** מפני פלטים מזיקים של AI

## תבניות נפוצות בדוגמאות

### תבנית אימות
כל הדוגמאות משתמשות בתבנית זו לאימות מול מודלים של GitHub:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### תבנית טיפול בשגיאות
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### תבנית מבנה הודעות
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## השלבים הבאים

[פרק 04: דוגמאות מעשיות](../04-PracticalSamples/README.md)

## פתרון בעיות

### בעיות נפוצות

**"GITHUB_TOKEN not set"**
- ודאו שהגדרתם את משתנה הסביבה
- בדקו שהטוקן שלכם כולל את ההרשאה `models:read`

**"No response from API"**
- בדקו את חיבור האינטרנט שלכם
- ודאו שהטוקן שלכם תקף
- בדקו אם הגעתם למגבלות השימוש

**שגיאות קומפילציה ב-Maven**
- ודאו שיש לכם Java 21 או גרסה מתקדמת יותר
- הריצו `mvn clean compile` כדי לרענן תלות

**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש להיות מודעים לכך שתרגומים אוטומטיים עשויים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית צריך להיחשב כמקור סמכותי. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי על ידי אדם. אנו לא נושאים באחריות לאי הבנות או לפרשנויות שגויות הנובעות משימוש בתרגום זה.