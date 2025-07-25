<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:49:57+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "he"
}
-->
# מדריך Foundry Local עם Spring Boot

## תוכן עניינים

- [דרישות מקדימות](../../../../04-PracticalSamples/foundrylocal)
- [סקירת הפרויקט](../../../../04-PracticalSamples/foundrylocal)
- [הבנת הקוד](../../../../04-PracticalSamples/foundrylocal)
  - [1. הגדרות האפליקציה (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. מחלקת האפליקציה הראשית (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. שכבת השירות AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. תלות הפרויקט (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [איך הכל עובד יחד](../../../../04-PracticalSamples/foundrylocal)
- [הגדרת Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [הרצת האפליקציה](../../../../04-PracticalSamples/foundrylocal)
- [פלט צפוי](../../../../04-PracticalSamples/foundrylocal)
- [צעדים הבאים](../../../../04-PracticalSamples/foundrylocal)
- [פתרון תקלות](../../../../04-PracticalSamples/foundrylocal)

## דרישות מקדימות

לפני שמתחילים את המדריך, ודאו שיש לכם:

- **Java 21 או גרסה גבוהה יותר** מותקנת במערכת שלכם
- **Maven 3.6+** לבניית הפרויקט
- **Foundry Local** מותקן ופועל

### **התקנת Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## סקירת הפרויקט

הפרויקט מורכב מארבעה רכיבים עיקריים:

1. **Application.java** - נקודת הכניסה הראשית של אפליקציית Spring Boot
2. **FoundryLocalService.java** - שכבת השירות שמטפלת בתקשורת עם AI
3. **application.properties** - הגדרות חיבור ל-Foundry Local
4. **pom.xml** - תלות Maven והגדרות הפרויקט

## הבנת הקוד

### 1. הגדרות האפליקציה (application.properties)

**קובץ:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**מה זה עושה:**
- **base-url**: מציין היכן Foundry Local פועל (ברירת מחדל: פורט 5273)
- **model**: שם מודל ה-AI לשימוש ביצירת טקסט

**רעיון מרכזי:** Spring Boot טוען את ההגדרות הללו באופן אוטומטי ומאפשר גישה אליהן באמצעות האנוטציה `@Value`.

### 2. מחלקת האפליקציה הראשית (Application.java)

**קובץ:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**מה זה עושה:**
- `@SpringBootApplication` מאפשרת קונפיגורציה אוטומטית של Spring Boot
- `WebApplicationType.NONE` מציין שזו אפליקציה מבוססת שורת פקודה ולא שרת אינטרנט
- המתודה הראשית מפעילה את האפליקציה של Spring

**הרצת הדמו:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**מה זה עושה:**
- `@Bean` יוצרת רכיב שמנוהל על ידי Spring
- `CommandLineRunner` מריץ קוד לאחר ש-Spring Boot מופעל
- `foundryLocalService` מוזרק אוטומטית על ידי Spring (הזרקת תלות)
- שולח הודעת בדיקה ל-AI ומציג את התגובה

### 3. שכבת השירות AI (FoundryLocalService.java)

**קובץ:** `src/main/java/com/example/FoundryLocalService.java`

#### הזרקת קונפיגורציה:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**מה זה עושה:**
- `@Service` מציין ל-Spring שהמחלקה מספקת לוגיקה עסקית
- `@Value` מזריק ערכי קונפיגורציה מתוך application.properties
- התחביר `:default-value` מספק ערכי ברירת מחדל אם ההגדרות לא מוגדרות

#### אתחול לקוח:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**מה זה עושה:**
- `@PostConstruct` מריץ את המתודה הזו לאחר ש-Spring יוצר את השירות
- יוצר לקוח OpenAI שמצביע על המופע המקומי של Foundry Local
- הנתיב `/v1` נדרש לצורך תאימות ל-API של OpenAI
- מפתח ה-API הוא "unused" כי פיתוח מקומי לא דורש אימות

#### מתודת הצ'אט:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**מה זה עושה:**
- **ChatCompletionCreateParams**: מגדיר את בקשת ה-AI
  - `model`: מציין איזה מודל AI להשתמש
  - `addUserMessage`: מוסיף את ההודעה שלכם לשיחה
  - `maxCompletionTokens`: מגביל את אורך התגובה (חוסך משאבים)
  - `temperature`: שולט ברנדומליות (0.0 = דטרמיניסטי, 1.0 = יצירתי)
- **קריאת API**: שולח את הבקשה ל-Foundry Local
- **טיפול בתגובה**: שולף את תגובת הטקסט של ה-AI בצורה בטוחה
- **טיפול בשגיאות**: עוטף חריגים עם הודעות שגיאה מועילות

### 4. תלות הפרויקט (pom.xml)

**תלויות מרכזיות:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**מה הן עושות:**
- **spring-boot-starter**: מספקת פונקציונליות בסיסית של Spring Boot
- **openai-java**: SDK רשמי של OpenAI לתקשורת עם API
- **jackson-databind**: מטפל בסריאליזציה/דסיריאליזציה של JSON עבור קריאות API

## איך הכל עובד יחד

כך מתבצע הזרימה המלאה כאשר מריצים את האפליקציה:

1. **הפעלה**: Spring Boot מופעל וטוען את `application.properties`
2. **יצירת שירות**: Spring יוצר את `FoundryLocalService` ומזריק ערכי קונפיגורציה
3. **הגדרת לקוח**: `@PostConstruct` מאתחל את לקוח OpenAI להתחבר ל-Foundry Local
4. **הרצת דמו**: `CommandLineRunner` מבוצע לאחר ההפעלה
5. **קריאת AI**: הדמו קורא ל-`foundryLocalService.chat()` עם הודעת בדיקה
6. **בקשת API**: השירות בונה ושולח בקשה תואמת OpenAI ל-Foundry Local
7. **עיבוד תגובה**: השירות שולף ומחזיר את תגובת ה-AI
8. **הצגה**: האפליקציה מציגה את התגובה ויוצאת

## הגדרת Foundry Local

כדי להגדיר את Foundry Local, בצעו את השלבים הבאים:

1. **התקנת Foundry Local** באמצעות ההוראות בסעיף [דרישות מקדימות](../../../../04-PracticalSamples/foundrylocal).
2. **הורדת מודל AI** שברצונכם להשתמש בו, לדוגמה, `phi-3.5-mini`, עם הפקודה הבאה:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **הגדרת קובץ application.properties** להתאמה להגדרות Foundry Local שלכם, במיוחד אם אתם משתמשים בפורט או מודל שונים.

## הרצת האפליקציה

### שלב 1: הפעלת Foundry Local
```bash
foundry model run phi-3.5-mini
```

### שלב 2: בנייה והרצת האפליקציה
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## פלט צפוי

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## צעדים הבאים

לדוגמאות נוספות, ראו [פרק 04: דוגמאות מעשיות](../README.md)

## פתרון תקלות

### בעיות נפוצות

**"Connection refused" או "Service unavailable"**
- ודאו ש-Foundry Local פועל: `foundry model list`
- בדקו שהשירות נמצא בפורט 5273: בדקו את `application.properties`
- נסו להפעיל מחדש את Foundry Local: `foundry model run phi-3.5-mini`

**שגיאות "Model not found"**
- בדקו אילו מודלים זמינים: `foundry model list`
- עדכנו את שם המודל ב-`application.properties` כך שיתאים בדיוק
- הורידו את המודל אם נדרש: `foundry model run phi-3.5-mini`

**שגיאות קומפילציה ב-Maven**
- ודאו ש-Java 21 או גרסה גבוהה יותר מותקנת: `java -version`
- נקו ובנו מחדש: `mvn clean compile`
- בדקו חיבור לאינטרנט להורדת תלויות

**האפליקציה מופעלת אך אין פלט**
- ודאו ש-Foundry Local מגיב: פתחו דפדפן לכתובת `http://localhost:5273`
- בדקו את לוגי האפליקציה להודעות שגיאה ספציפיות
- ודאו שהמודל נטען במלואו ומוכן

**כתב ויתור**:  
מסמך זה תורגם באמצעות שירות תרגום מבוסס בינה מלאכותית [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש להיות מודעים לכך שתרגומים אוטומטיים עשויים להכיל שגיאות או אי דיוקים. המסמך המקורי בשפתו המקורית צריך להיחשב כמקור סמכותי. עבור מידע קריטי, מומלץ להשתמש בתרגום מקצועי אנושי. איננו נושאים באחריות לאי הבנות או לפרשנויות שגויות הנובעות משימוש בתרגום זה.