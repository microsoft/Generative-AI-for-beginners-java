# מדריך Foundry Local Spring Boot

הרץ מודל שפה קטן על המחשב שלך וקרא את נקודת הקצה REST התואמת ל-OpenAI
מתוך אפליקציית קונסול Java. אין שימוש בפריסת Azure, כניסה ל-Azure,
מפתח API לענן או הסקת מסקנות בענן. **GPT-5.6 Luna מיועד רק ל-Azure; אל תגדירו אותו כמודל Foundry Local.**


## גרסאות ותנאים מוקדמים

| רכיב | גרסה |
| --- | --- |
| Java | 21 או יותר חדש |
| Maven | 3.6.3 או יותר חדש |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (שרת REST מקומי) | 2.0.1 |
| Node.js (שרת REST מקומי) | 20 או יותר חדש |
| Foundry Local CLI (אופציונלי, גרסה נפרדת) | 0.10.3 preview |

Spring Boot מנהל את גרסאות Spring Framework, Jackson, JUnit, ותוסף Maven.
דוגמה זו משתמשת ב-OpenAI Java SDK ישירות, לא ב-Spring AI. תכונת Milestone המקורית והמאגר הישן של Spring AI הוסרו.


דגם ההתחלה המומלץ הוא **Qwen 2.5 0.5B**, וריאנט CPU
`qwen2.5-0.5b-instruct-generic-cpu:4` (כ-822 מגה בייט בקטלוג).
הוא נמנע מלהצריך ספקי ביצוע GPU. דגמים קטנים נוספים הנתמכים והמטופלים באפשרותך לבחור במפורש.
ההתקנה של המודל והסביבה דורשת גישה לרשת; הפקודות וההסקה נשארות מקומיות.
Foundry Local עלול להוציא אבחונים מינימליים של זמן הריצה אפילו כאשר טלמטריה לא חיונית מושבתת.


הרץ את הפקודות הבאות מתוך תיקיית הדוגמה הזו.

## בנייה ובדיקה עם Java

```powershell
mvn clean verify
```

בדיקות חוזה HTTP מפעילות שרת לולאה זמני ובודקות את OpenAI Java SDK בפועל.
הן כוללות סריאליזציה של בקשות, גילוי מודל, בחירת מודל מפורשת,
רשימות מודלים לא ברורות או שגויות, כישלונות HTTP, תגובות ריקות,
כתובות URL מקומיות בלבד, והפצת שגיאות בשורת הפקודה. הן לא דורשות מודל או
גישה לרשת מעבר להתקנת תלות Maven. הבדיקה החיה היא מבחן אופציונלי.

## הפעלת המודל המקומי

### מומלץ: שרת SDK מקובע

אין SDK Java מקורית של Foundry Local. העזרן הקטן של Node.js מארח את
שרת ה-REST הרשמי של ה-SDK; האפליקציה ובקשות הצ'אט נשארות ב-Java.

התקן את התלויות של זמן הריצה המקובע:

```powershell
npm ci
```

אם חלונות x64 אינם מצליחים להגיע ל-NuGet במהלך התקנת SDK המקומית, השתמש בגיבוי המסופק.
הגיבוי יוריד את ארכיון זמן הריצה הרשמי המתאים מ-GitHub, יבצע בדיקת SHA-256 על הגרסה,
וימקם את הקובצי DLL לצד התוסף המקומי. הוא אינו
מבטל אימות TLS, אינו דורש הרשאות גבוהות, ואינו משנה את קוד ה-SDK.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

רשום את המודלים שכבר מטופלים על מחשב זה:

```powershell
npm run start:foundry -- --list
```

בהרצה הראשונה, אפשר במפורש להוריד את מודל ה-CPU הקטן:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

בהרצות הבאות, השמט את `--download` כדי לדרוש מודל מטופל:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

העזרן מעדיף מודל מטופל תואם, מקבל כינוי או מזהה וריאנט מדויק,
ומסרב למודל חסר אלא אם כן `--download` מסופק. הוא רשום רק את
ספק הביצוע של המודל הנבחר כאשר נדרש. וריאנטים מטופלים ל-GPU עדיין
עשויים להזדקק לחבילות וספקי מכשירים תואמים.

אם הפורט 5273 תפוס, העבר `--port 0` לפורט פנוי. העוזר מדפיס
את `FOUNDRY_LOCAL_BASE_URL`, את מזהה `FOUNDRY_LOCAL_MODEL` המדויק, ואת PID שלו כשהוא מוכן.
השתמש בנקודת הקצה שהודפסה ב-Java. השאר טרמינל זה פתוח בזמן הרצת Java;
**Ctrl+C** עוצר את שרת REST ומשחרר את המודל.

המטמון הברירת מחדל הוא `~/.foundry/cache/models`. הגדירו `FOUNDRY_LOCAL_CACHE_DIR` למטמון קיים שונה.
יומנים ומצב העוזר נרשמים תחת התיקייה של הדוגמה `target/foundry-local`. עצרו את העוזר לפני הרצת `mvn clean`.


### אופציונלי: Foundry Local CLI

ל-CLI ול-SDK יש שחרורים עצמאיים: CLI בגרסה **0.10.3** כולל SDK בגרסה **1.2.4**;
העוזר שלמעלה משתמש ב-SDK גרסה **2.0.1**. התקנת ה-CLI העדכני ביותר אינה מתקינה את
ה-SDK הכי עדכני לשפה. עיין ב[הערות השחרור של CLI](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

ב-Windows, השתמש בפקודת ההתקנה למשתמש אם ה-CLI חסר:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

או עדכן התקנה קיימת:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x מחליף את פקודות `foundry service` הישנות ב-`foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` דורש מודל שהורד כבר. בדוק ב־`foundry model --help` את פקודות ההורדה.
השתמש בנקודת הקצה בהפעלת הסטטוס; ה-CLI אחרת
מבררת פורט שמוקצה באופן אוטומטי. אל תתחיל את העוזר של CLI ו-SDK
על אותו פורט. בסיום:

```powershell
foundry server stop
```

## הרץ את יישום ה-Java

בטרמינל שני, הגדר את נקודת הקצה ואת מזהה המודל המדויק שהודפסו על ידי השרת שלך:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

או הרץ את היישום הארוז:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

נקודת הכניסה היחידה של Java היא `com.example.Application`. היא מדפיסה את נקודת
הקצה שנבחרה, מזהה המודל המדויק, הפקודה והתגובה שנוצרה, ואז סוגרת את הקונטקסט של Spring ולקוח HTTP.
כישלון בהסקה או חוסר בטקסט תגובה מייצר
יציאה עם שגיאה במקום מציין הצלחה.

### קונפיגורציה

| משתנה סביבה | ברירת מחדל | מטרה |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | נקודת קצה HTTP לולאית, כולל `/v1` |
| `FOUNDRY_LOCAL_MODEL` | ריקה | מזהה מודל מדויק; אחרת בחר את המודל היחיד שמפורסם |
| `FOUNDRY_LOCAL_PROMPT` | שאלה במשפט אחד על מודלים מקומיים | הפקודה שנשלחת על ידי הקונסול |

פרמטרי Spring שקולים הם `--foundry.local.base-url=...`,
`--foundry.local.model=...`, ו־`--foundry.local.prompt=...`.
מתקבלים רק נקודות קצה HTTP לולאיות. נקודות קצה מרוחקות/ענניות, אישורים מוטבעים,
מחרוזות שאילתה, ונתיבים ללא `/v1` נדחים.

הגדרת מודל ריקה עובדת רק כאשר `/v1/models` מפרסם בדיוק מודל אחד.
מודל שמפורסם לא בהכרח טעון. אם מודלים רבים מפורסמים,
הגדר את המזהה המדויק של המודל הטעון במקום להסתמך על סדר הקטלוג.

הבקשות משתמשות ב־`temperature=0`, מגבלת פלט של 150 תווים, טווח זמן של 120 שניות, ו-
ללא נסיונות חוזרים אוטומטיים. שדה `max_tokens` בבקשה הוא מכוון: הוא
נתמך על ידי חוזה ה-REST המקומי של Foundry, אם כי OpenAI Java מתיישן
שדה זה עבור דגמי ענן חדשים יותר. זהות הדגם מגיעה מהגדרה או
גילוי, לא מהתביעות של הדגם לגבי עצמו.

## אימות חי

עם השרת המקומי פועל, הרץ את כל הבדיקות כולל בדיקת חיות אופציונלית.
החלף את הפורט של נקודת הקצה בערך שהודפס על ידי השרת שלך. צטט מאפיינים ב־Maven עם נקודות ב־PowerShell:


```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

בדיקת החיות מפעילה את `Application.main`, מספקת את העובדה "בירת צרפת היא פריז," מבקשת את שם העיר, ומוודאת שטקסט שנוצר הוא
`Paris`. היא בודקת תוצאה סמנטית, לא רק סטטוס HTTP מוצלח.


ענה על נושא נפרד "2 + 2" ב־`3` דרך ג׳אווה וגם REST ישירות.
אל תסתמך עליו לחישובים או דיוק עובדות ללא אימות עצמאי;
השתמש בכלים דטרמיניסטיים עבור חישובים.


## פתרון תקלות

| סימפטום | בדוק |
| --- | --- |
| חיבור נדחה | המתן להודעת מוכן; השתמש בפורט שהודפס ובנתיב `/v1`. |
| מודלים מרובים מתפרסמים | הגדר את `FOUNDRY_LOCAL_MODEL` לזיהוי המדויק של הדגם הטעון. |
| דגם חסר | השתמש ב־`--list`, או אפשר במפורש הורדה עם `--download`. |
| ספק GPU נכשל או תקוע | השתמש בדגם CPU הקטן. דגם GPU שנמצא במטמון עדיין זקוק לספק שלו. |
| CLI נשאר ב־`initializing` | קרא `foundry server logs --lines 80`; עצור את התהליך והשתמש בעוזר SDK. |
| כשל TLS/הורדה של NuGet | תקן גישת רשת או השתמש במנגנון הגיבוי האמין של Windows x64 לעיל. אל תבטל TLS. |
| פורט תפוס | השתמש ב־`--port 0` וקבע את Java עם נקודת הקצה שהודפסה. |
| אין בחירות או טקסט ריק | האפליקציה נכשלת בכוונה; בדוק את לוגי הדגם והריצת זמן. |

## מקור והפניות

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): ריצה חד־פעמית עם Spring Boot.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): גילוי טיפוסי והשלמות שיחה מקומיות.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): חוזה HTTP, רץ, ובדיקות חיות.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): שרת REST של SDK רשמי עם בחירת דגם במטמון וניקוי.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): מנגנון גיבוי נייטיב ל־Windows x64 מאומת.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), ו־[package.json](../../../../04-PracticalSamples/foundrylocal/package.json): הגדרות ותלויות.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**כתב ויתור**:
מסמך זה תורגם באמצעות שירות תרגום אוטומטי [Co-op Translator](https://github.com/Azure/co-op-translator). למרות שאנו שואפים לדיוק, יש לקחת בחשבון שתרגומים אוטומטיים עלולים להכיל שגיאות או אי-דיוקים. יש להחשיב את המסמך המקורי בשפתו הטבעית כמקור הסמכות. למידע קריטי מומלץ להשתמש בתרגום מקצועי על ידי מתרגם אדם. אנו לא אחראים לכל אי-הבנה או פירוש שגוי הנובע מהשימוש בתרגום זה.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->