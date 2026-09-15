# מדריך לטכניקות ליבה של בינה מלאכותית גנרטיבית  

## תוכן העניינים  

- [דרישות מוקדמות](#דרישות-מוקדמות)  
- [התחלה מהירה](#התחלה-מהירה)  
- [מדריך לבחירת מודל](#מדריך-לבחירת-מודל)  
- [מדריך 1: השלמות ושיחה באמצעות LLM](#מדריך-1-השלמות-ושיחה-ב-llm)  
- [מדריך 2: קריאה לפונקציה](#מדריך-2-קריאה-לפונקציה)  
- [מדריך 3: RAG (הפקה משופרת על ידי אחזור)](#מדריך-3-rag-הפקה-משופרת-באמצעות-אחזור)  
- [מדריך 4: בינה מלאכותית אחראית](#מדריך-4-בינה-מלאכותית-אחראית)  
- [תבניות נפוצות לדוגמאות](#תבניות-נפוצות-בדוגמאות)  
- [בדיקות יחידה](#בדיקות-יחידה)  
- [אישור חי רציף סדרתי](#אישור-חי-סדרתי)  
- [פתרון תקלות](#פתרון-תקלות)  
- [שלבים הבאים](#שלבים-הבאים)  

## סקירה כללית  

ארבע תוכניות Java עצמאיות מדגימות שיחה, היסטוריית שיחות, קריאה לפונקציות, הפקה משופרת באחזור מסמכים (RAG), וטיפול בתגובות בינה מלאכותית אחראית. כל בקשות השיחה פונות בברירת מחדל אל **GPT-5.6 Luna עם מאמץ הנמקה `none`**.  

דוגמאות אלו משתמשות ב-SDK הרשמי של OpenAI ב-Java עם נקודת הקצה v1 של Azure OpenAI, בהתאם להנחיות [Microsoft's SDK guidance](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). חבילת ה-`azure-ai-openai` הישנה כבר לא תלות נדרשת. Chat Completions נשמר ללימוד תזרימי עבודה מבוססי הודעות קיימים; עיינו ב-[OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) לאפשרויות API נוספות.  

## דרישות מוקדמות  

- Java 21 או יותר חדש ו-Maven 3.6.3 או יותר חדש.  
- פריסת שיחה של Azure OpenAI בשם `gpt-5.6-luna`, או החלפה עם הגדרות Chat Completions תואמות.  
- זהות Azure מחוברת עם תפקיד **Cognitive Services OpenAI User** על המשאב. פיתוח מקומי משתמש בכניסה דרך Azure CLI שלך; אפליקציות מתארחות יכולות להשתמש בזהות מנוהלת.  
- ראו [פרק 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) להגדרות המשאב והנחיות כניסה.  

ה[הגדרת Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) מציינת את הגרסאות הללו, נבדקו בתאריך 2026-09-14:  

| רכיב | גרסה | מטרה |  
| --- | --- | --- |  
| `com.openai:openai-java` | 4.63.1 | לקוח רשמי תואם Azure v1 |  
| `com.azure:azure-identity` | 1.18.6 | אימות ללא מפתח וחדשנות אסימון |  
| `net.objecthunter:exp4j` | 0.4.8 | פרסינג ביטויים אריתמטיים ללא הערכת קוד |  
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | בדיקות יחידה אוף-ליין Jupiter |  
| קומפיילר / Surefire / Exec של Maven | 3.16.0 / 3.6.0 / 3.6.4 | הידור Java 21, בדיקות, דוגמאות להרצה |  

הקומפיילר משתמש ב-`--release 21`. לא נדרשת תלות ב-Spring Boot, Spring AI, או LangChain4j בדוגמאות העצמאיות הללו.  

## התחלה מהירה  

משורש המאגר, הגדר את נקודת הקצה של המשאב ואת החלפת הפריסה האופציונלית ב-shell שלך.  

**Windows PowerShell:**  

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```
  
**Linux/macOS:**  

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```
  
בדיקות אינן דורשות אישורי Azure או נקודת קצה. Maven אינו טוען קובץ סביבה אוטומטית; הגדר משתנים ב-shell שבו מפעילים דוגמאות חיות. להפעלות IDE, ודא את הסביבה שמסופקת בהגדרות ההפעלה.  

## מדריך לבחירת מודל  

| משתנה סביבה | משמעות | ברירת מחדל |  
| --- | --- | --- |  
| `AZURE_OPENAI_ENDPOINT` | שורש משאב Azure HTTPS או כתובת URL `/openai/v1` מנורמלת מראש | דרוש להרצות חיות |  
| `AZURE_OPENAI_DEPLOYMENT` | שם פריסת שיחה, לא גרסת מודל | `gpt-5.6-luna` |  
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | תצורת פריסת הטמעה נפרדת, לא בשימוש בתוכניות אלו | `text-embedding-3-small` |  

החלפות פריסה ריקות משתמשות בברירות המחדל. התצורה מצרפת `/openai/v1` בדיוק פעם אחת ודוחה אישורים, מחרוזות שאילתה, ונתיבי פריסה ישנים בנקודת הקצה.  

כל בקשת שיחה מגדירה במפורש `reasoningEffort(ReasoningEffort.NONE)` ו`maxCompletionTokens(...)`. אף בקשה לא מגדירה `temperature`, `top_p`, או את אפשרות הטוקנים הישנה. זה כולל בחירת כלים ומעקב אחרי תוצאותיהם. כלים בפונקציית Chat Completions של GPT-5.6 דורשים מאמץ הנמקה `none`; עיין ב[הנחיות שיחה של Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).  

**אין נקודת כניסה להזנה או לטמעה בפרק זה.** הקורא מושך את המסמך כולו, לא וקטורים. אם להרחיב עם הטמעות, השתמש בפריסת הטמעה נפרדת כגון `text-embedding-3-small`, לעולם לא בלונה.  

## מדריך 1: השלמות ושיחה ב-LLM  

מקור: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).  

התוכנית מריצה הסבר פשוט ב-Java streams, שיחת HashMap/TreeMap בת שתי סבבים, ושיחה אינטראקטיבית. הסבב השני כולל את תגובת העוזר הראשונה; כל סבב אינטראקטיבי גם שולח את השיחה הקודמת.  

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```
  
`config.chatOptions(...)` מספק את הפריסה והגדרת ההנמקה המפורשת. שיחה אינטראקטיבית מדלגת על שורות ריקות, מסתיימת ב-exit או EOF, ושומרת על הודעת המערכת ותשעת סבבי משתמש/עוזר הושלמו. קיצוץ בהתאם למספר הסבבים הוא גבול חינוכי, לא ערבויות תקציב טוקנים מדויק.  

מתיקיית הדוגמאות:  

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```
  
צפה לשלוש תשובות ראשוניות, ואז קריאת `You:`. כל שאלה אינטראקטיבית שאינה ריקה מוסיפה בקשה אחת. מגבלות השלמה הן 200, 300, 400 ואז 500 טוקנים לסבב אינטראקטיבי.  

## מדריך 2: קריאה לפונקציה  

מקור: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).  

ה-SDK מפיק סכימות JSON מתוך רשומות מתויגות `WeatherArguments` ו-`CalculationArguments`. בחירת כלי מחייבת מוודאת שכל דוגמה מתאמנת על פרוטוקול הכלי במקום לקבל תשובה לא מסייעת מהמודל.  

1. שלח שאלה עם הכלי המותר, מאמץ הנמקה `none`, ומגבלת השלמה של 300 טוקנים.  
2. דרוש סיבת סיום `tool_calls`, אשר את שם הפונקציה ומזהי הקריאה, ופרס JSON טיפוסי של הטיעונים.  
3. הפעל את הפונקציה המקומית. המודל אינו מפעיל קוד Java או כל קוד ארבי אחר.  
4. הוסף את הודעת קריאת הכלי של העוזר פעם אחת, ואחריה כל תוצאה עם `tool_call_id` תואם.  
5. שלח בקשה אחרונה של 300 טוקנים ללא כלים ודרוש תשובה שלמה ולא ריקה.  

`get_weather` מחזירה מזג אוויר **מדומה**, לא חי. היא מתחשבת בעיר והופכת את הדוגמה של 22 מעלות צלזיוס לפרנהייט בבקשה. `calculate` מעריכה את הביטוי שסופק דרך exp4j, תומכת בצורות כמו `15% מתוך 240` ו-`2 + 3 * 4`, ודוחה חישובים ריקים, גדולים מדי, לא תקינים או לא סופיים. היא משתמשת באריתמטיקה בנקודה צפה, לא בדיוק פיננסי עשרוני.  

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```
  
צפה ל`Function: get_weather`, מזג האוויר המדומה של סיאטל, `Function: calculate`, `Function result: 36`, ושתי התשובות הסופיות. אין צורך ב־stdin או באישורים חיצוניים למזג אוויר. ריצה מוצלחת משתמשת בדיוק בארבע בקשות שיחה.  

## מדריך 3: RAG (הפקה משופרת באמצעות אחזור)  

מקור: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). קלט: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).  

דוגמה מבואית זו של RAG מושכת מסמך UTF-8 שלם ומכלילה אותו בהודעת המשתמש עם השאלה. הודעת מערכת נפרדת מורה למודל להתייחס לתוכן המסמך כנתונים לא מהימנים ולענות רק מהקשר זה. אם המסמך אינו מכיל את התשובה, התגובה המבוקשת היא: `I cannot find that information in the provided document.`  

עיגון יכול להפחית הזיות, אך לא תוויות או הוראות מערכת מבטיחות דיוק או מונעות כל הזרקת פרומפט. בדוק תשובות חיות. בייצור, RAG מוסיף בדרך כלל חתיכות, אחזור, ציטוטים, בקרת גישה והערכה.  

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```
  
הזן שאלה אחת, למשל `Which authentication method does the document describe?`. צפה לתשובה שמזכירה Microsoft Entra ID. התוכנית יוצאת לאחר בקשת שיחה אחת עם מגבלת 500 טוקנים להשלמה.  

חיפוש קבצים ברירת מחדל עובד משורש המאגר, תיקיית הפרק, או תיקיית הדוגמאות. נתיב מפורש נתמך גם כן:  

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```
  
הקלטים חייבים להיות לא ריקים: עד 32 KiB של נתוני מסמך בפורמט UTF-8 ועד 2,000 תווי שאלה. קבצים חסרים, שאלות ריקות/EOF, וקלטים גדולים מדי נכשלות לפני ההסקה.  

## מדריך 4: בינה מלאכותית אחראית  

מקור: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).  

ששת המבדקים כוללים הוראות מזיקות, דיבור שנאה, פרטיות, מידע רפואי שגוי, תוכן בלתי חוקי, ושאלה חיובית של בינה מלאכותית אחראית. התוכנית מתבוננת בתגובה במקום להניח שכל מבחן חייב להפעיל סינון.  

| תוצאה | הוכחות |  
| --- | --- |  
| `FILTERED` | קוד שגיאה מפורש `content_filter` / `ResponsibleAIPolicyViolation`, או סיבת סיום השלמה `content_filter` |  
| `REFUSED` | שדה `message.refusal` מובנה, לא ריק |  
| `POSSIBLE_REFUSAL` | ביטוי דחייה הפותח בטקסט רגיל; הזרחה הדורשת סקירה |  
| `GENERATED` | תגובה הושלמה ולא ריקה; לא הוכחה שהתוכן בטוח |  

HTTP 400 רגיל **אינו** הוכחה לסינון. פרמטרים שגויים, כשלי אימות, מגבלות קצב, שגיאות שרת, תגובות פגומות ופלט מקוצץ מפסידים את ההרצה במקום לייצר הצלחה בטיחותית שגויה. מילים כלליות כמו "תוכן מזיק" בהסבר חיובי אינן נספרות כסירוב.  

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```
  
צפה לשש תוצאות קטגוריה וסיכום המציין כי התצפיות אינן תעודת בטיחות. לכל מבחן יש מגבלת השלמה של 300 טוקנים. בדוק באופן ידני הפקות בלתי צפויות ודחיות אפשריות; ההשוואה החיובית אמורה לייצר הסבר משמעותי של בינה מלאכותית אחראית. אין צורך ב־stdin.  

## תבניות נפוצות בדוגמאות  

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) מרכז נורמליזציה של נקודות קצה, החלפות פריסה, אימות ללא מפתח, ואפשרויות שיחה:  

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```
  
ספק הטוקנים מרענן אסימוני גישה לפי הצורך. אין לרשום טוקנים או להחליף זאת במפתח API. כל תוכנית משתמשת מחדש בלקוח שלה וסוגרת אותו ב-`finally` או דרך עטיפת `AutoCloseable` משלה; ה-`OpenAIClient` של ה-SDK עצמו אינו `AutoCloseable`.  

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) דורש תשובה טקסטואלית שלמה ולא ריקה. בחירות ריקות, דחיות, סינונים, ותשובות מקוצצות אינן מודפסות בשקט כהצלחה. דוגמת הבינה המלאכותית האחראית מטפלת במפורש בתוצאות צפויות של סינון/דחייה. כישלונות לא מטופלים נותנים לקוד יציאה לא אפס לתהליך Java/Maven.  

**ניסיונות אוטומטיים של ה-SDK כבויים** לשמירה על ספירת בקשות ניתנת לחיזוי בפריסות שיתוף בעלות קצב נמוך. לכל בקשת הסקה יש מגבלת זמן של 60 שניות. רכישת טוקנים עלולה לקחת זמן נוסף. תזמון ברמת היישום חייב לכבד מכסות; אל תריץ שוב עיוור בקשה בתשלום שנכשל.  

## בדיקות יחידה  

מתיקיית הדוגמאות:  

```powershell
mvn -B -ntp clean test
```
  
תקשורת הבדיקה מחליפה לחלוטין את השכבה HTTP של ה-SDK, תופסת גופות בקשות רשומות בפועל, ומספקת תגובות בתור. היא לא פותחת שקעי רשת, לא משיגה אסימוני Azure, ונכשלת בבקשות בלתי צפויות. בדיקות אלו מאמתות התנהגות יישום ופרוטוקול SDK, לא איכות מודל חי או זמינות פריסה.  

| חבילת בדיקות | כיסוי |  
| --- | --- |  
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | נרמול/דחיית נקודות קצה, החלפות פריסה, הגדרות הנמקה וטוקנים |  
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | כל זרימת השלמות, היסטוריית הודעות, קיצוץ סיבובים מלאים, EOF, כישלונות |  
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | סכימות כלים, טיעונים טיפוסיים, אריתמטיקה, מזהים, תוצאות כלים מרובות, מעקבים שנכשלו |  
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | חיפוש קבצים, UTF-8, מגבלות גודל, תכולת עיגון, שגיאות קלט ו-API |  
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | כל ששת המבחנים, סינונים מפורשים, סיווג דחיות, 400 רגיל ושגיאות אחרות |  

עבור חבילה אחת, השתמש ב`mvn -B -ntp test "-Dtest=FunctionsAppTest"`. תשתיות משותפות חיות ב[RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).  

## אישור חי סדרתי  

שיחות חיות נפרדות מבדיקות יחידה. השתמש בפקודות הבאות **ליחידן**, משורש המאגר, רק אחרי שאישורים והגישה לפריסה מוכנים. אין צורך בשירותים או תהליכים מתמשכים.  

עבור פריסת **10 בקשות/דקה** משותפת, השאר מספיק מכסה לכל התוכנית הבאה לפני הפעלתה: 5, 4, 1 ואז 6 בקשות. תהליכים סדרתיים לבד אינם מבטיחים עמידה במגבלת קצב. תיאם את הדקה המתגלגלת עם כל שאר המתקשרים; אל תדביק את ארבע הקריאות במקבץ לא מבוקר.  

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```
  
**1. השלמות, רב-סיבוביות, ושני סבבים אינטראקטיביים:**  

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```
  
בדוק את כל שלושת כותרות הקטעים, חמשת התשובות, תשובה אינטראקטיבית סופית המזכירה את אדה, `להתראות!`, וקוד יציאה 0. תקציב: **5 בקשות, עד 1,900 טוקני השלמה**. להרצה מצומצמת יותר, העבר רק `exit`: 3 בקשות / 900 טוקנים, אך זה לא מתאמן על אינפרנס אינטראקטיבי.

**2. שני זרמי העבודה של קריאת הפונקציה:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

בדוק את שני שמות הפונקציות, את מזג האוויר המדומה בסיאטל, את התוצאה המחושבת 36, שתי התשובות הסופיות, וקוד יציאה 0. תקציב: **4 בקשות, עד 1,200 טוקני השלמה**.

**3. תשובה מבוססת מסמך:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

בדוק את נתיב המסמך, תשובה שמזכירה את Microsoft Entra ID, וקוד יציאה 0. תקציב: **בקשה אחת, עד 500 טוקני השלמה**. קובץ הקלט היחיד הדרוש הוא [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt). הרצה שנייה אופציונלית ששואלת על נושא שאינו קיים צריכה להימנע ומוסיפה בקשה אחת / 500 טוקנים.

**4. תצפיות AI אחראי:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

בדוק שש שש קטגוריות וסיכום התצפיות, סקור את התוכן שנוצר, ודרש קוד יציאה 0 לסיום טכני. יציאה מוצלחת של התהליך אינה מאשרת בטיחות מודל. תקציב: **6 בקשות, עד 1,800 טוקני השלמה**.

**סה"כ עבור ארבעת הפקודות: 16 בקשות שיחה ועד 5,400 טוקני השלמה**, בנוסף לטוקני קלט (כולל שיח משוכפל ותבנית היסטורית/כלי). אין בקשות הטמעות. השימוש בפועל בטוקנים תלוי במודל ועלול להיות נמוך יותר, במיוחד עבור פרומפטים מסוננים. עלות בדולרים תלויה בתמחור הפריסה; אין הערכה כספית קבועה מרומזת. כל מגבלת בקשות מניחה שאין הרצות ידניות חוזרות. בדוק את `$LASTEXITCODE` מיד לאחר כל פקודה; ערך שונה מאפס מציין שההרצה לא הושלמה בהצלחה.

## פתרון תקלות

- **נקודת קצה חסרה / 401 / 403:** הגדר את נקודת הקצה בתהליך ההפעלה, אמת את ההתחברות המקומית ל-Azure ואת התפקיד ממוקד המשאבים, ובדוק האם קיימות ביטולי סביבה של זהויות בלתי מכוונים.
- **400 / 404:** ודא שהפריסה קיימת ותומכת ב-Chat Completions עם מאמץ השקול `none`. השתמש בשורש המשאב HTTPS או בכתובת `/openai/v1`, לא בכתובת פריסה ישנה. שגיאות 400 רגילות הן כשלים טכניים, לא חסימות בטיחות.
- **429:** תאם את RPM המשוחק ואת מכסת הטוקנים לפני ניסיון חוזר. הדוגמאות לא מנסות אוטומטית.
- **`Incomplete chat response: length`:** הפלט הגיע למגבלת ההשלמה. סקור את התגובה והפרומפט לפני שתגביר את המגבלה ותקציבה המתועד; אל תרשום הרצה מקוצרת כהצלחה.
- **שגיאות קובץ או stdin:** הפעל מתיקייה נתמכת או העבר נתיב מסמך מפורש. ספק שאלה קוראת שאינה ריקה. השלמות יכולות להסתיים כרגיל ב-EOF או ב-`exit`.
- **שגיאות קומפילציה:** אמת Java 21 ומעלה, ואז הרץ `mvn -B -ntp clean test`. ב-PowerShell עטוף במרכאות את כל ארגומנט Maven הכולל תכונה עם נקודות, למשל `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## שלבים הבאים

המשך ל-[פרק 4: דוגמאות מעשיות](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום אוטומטי [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו הטבעית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אדם. אנו לא אחראים לכל אי-הבנה או פירוש שגוי הנובע מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->