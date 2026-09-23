# Kisállat Történet Generátor Bemutató Kezdőknek

Tölts fel egy kisállat fotót, elemeztesd a GPT-5.6 Lunával, és generálj történetet a kapott leírás alapján. Mindkét modell kérésnél `reasoning_effort: none` értéket használunk.

| Komponens | Verzió |
| --- | --- |
| Java | 21 vagy újabb |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Tartalomjegyzék

- [Előfeltételek](#előfeltételek)
- [A projekt struktúrájának megértése](#a-projekt-struktúrájának-megértése)
- [Az alapvető komponensek magyarázata](#az-alapvető-komponensek-magyarázata)
  - [1. Fő alkalmazás](#1-fő-alkalmazás)
  - [2. Web vezérlő](#2-web-vezérlő)
  - [3. Történet szolgáltatás](#3-történet-szolgáltatás)
  - [4. Web sablonok](#4-web-sablonok)
  - [5. Konfiguráció](#5-konfiguráció)
- [Az alkalmazás futtatása](#az-alkalmazás-futtatása)
- [Offline tesztek](#offline-tesztek)
- [Hogyan működik együtt](#hogyan-működik-együtt-az-egész)
- [Az AI integráció megértése](#az-ai-integráció-megértése)
- [Következő lépések](#következő-lépések)

## Előfeltételek

A kezdés előtt győződj meg arról, hogy rendelkezel:
- Java 21 vagy újabb telepítve
- Maven a függőségkezeléshez
- Egy Azure AI Foundry GPT-5.6 Luna telepítés, `gpt-5.6-luna` néven, vagy egy `AZURE_OPENAI_DEPLOYMENT` felülírás, amely erre mutat. Lásd a [2. fejezetet](../../02-SetupDevEnvironment/getting-started-azure-openai.md) a telepítéshez, és jelentkezz be `az login` parancssal kulcs nélküli hitelesítéshez. A telepítésnek támogatnia kell a kép bemenetet és `reasoning_effort: none` beállítást.
- Az alapvető Java, Spring Boot és webfejlesztési ismeretek

## A projekt struktúrájának megértése

A kisállat történet projekthez tartozik néhány fontos fájl:

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

## Az alapvető komponensek magyarázata

### 1. Fő alkalmazás

**Fájl:** `PetStoryApplication.java`

Ez a Spring Boot alkalmazásunk belépési pontja:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Mit csinál ez:**
- Az `@SpringBootApplication` annotáció engedélyezi az automatikus konfigurációt és komponensfelismerést
- Beindít egy beágyazott web szervert (Tomcat) a 8080-as porton
- Automatikusan létrehozza az összes szükséges Spring bean-t és szolgáltatást

### 2. Web vezérlő

**Fájl:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Végpont | Kérés | Sikeres válasz |
| --- | --- | --- |
| `GET /` | Nincs tartalom | HTML feltöltő űrlap CSRF tokennel |
| `POST /analyze-image` | `multipart/form-data`, fájl mező `image` | JSON: `{"description":"Egy játékos kisállat..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, mező `description` | HTML eredmény oldal a leírással és az elkészült történettel |

Mindkét POST végpont igényli a `GET /` által megszerzett session sütit és CSRF tokent. A feltöltő szkript az elrejtett `_csrf` értéket az `X-CSRF-TOKEN` fejlécben küldi; a történet beküldése a `_csrf` űrlapmezőként. Az API klienseknek meg kell őrizniük a sütit a kérések között. Ezek űrlap végpontok, nem JSON kérés végpontok.

A leírások nem lehetnek üresek és legfeljebb 1000 karakter hosszúak. A vezérlő levágja a leírást és eltávolítja a `<`, `>`, dupla idézőjeleket, aposztrófokat és `&` jeleket, mielőtt átadná a szolgáltatásnak. Az eredmény sablon a modell kimenetet szintén escape-eli `th:text` segítségével.

Képvalidáció sikertelensége HTTP 400 hibát és `error` mezőt ad vissza; modell hibák HTTP 502-t `error` mezővel, de `description` nélkül. Érvénytelen történet leírások vagy modell hibák átirányítanak a `/` oldalra látható hibával. Hiányzó kötelező mezők HTTP 400-t, hiányzó vagy érvénytelen CSRF tokenek HTTP 403-at eredményeznek. Nem jelennek meg helyettesítő leírások vagy történetek sikeres AI eredményként.

### 3. Történet szolgáltatás

**Fájl:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Az OpenAI Java SDK 4.63.1 hivatalosan az Azure AI Foundry OpenAI-kompatibilis Chat Completions API-ját hívja meg. Az Azure Identity 1.18.6 a Microsoft Entra bearer tokent adja `DefaultAzureCredential` segítségével; API kulcs nem szükséges.

| Művelet | Bemenet | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Képpé bájtok base64 adat URL formátumban a feltöltött MIME típussal | 300 |
| `generateStory` | Egy kisállat leírása egy felhasználói üzenetben | 800 |

Mindkét kérés a konfigurált telepítést használja, alapértelmezett `gpt-5.6-luna`, és explicit módon beállítja `ReasoningEffort.NONE` (`reasoning_effort: none`). Egyik kérés sem küld `temperature` vagy a régi `max_tokens` paramétert.

A kép elemzés JPEG, PNG, GIF és WebP formátumot fogad el, elutasítja az üres képeket és 10MB-nál nagyobb fájlokat, és a kapott leírást 1000 karakterre korlátozza. A történet prompt családbarát rövid történetet kér. Üres opciók vagy üres modell tartalom hibák, és a hibák megőrzik az eredeti okot a szerver oldali diagnosztikához. Az SDK kliens bezárul az alkalmazás leállásakor.

### 4. Web sablonok

**Fájl:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Feltöltő űrlap)

Az oldal egy fotóválasztóval indul, nem egy leírás szöveg mezővel. Az **Analyze Image** megjeleníti a kiválasztott képet és elküldi a `/analyze-image` végpont felé. Egy sikeres válasz megmutatja a leírást, kitölti az elrejtett `description` mezőt, és megjeleníti a **Generate Story** gombot. Ez az űrlapot elküldi a `/generate-story`-ra.

Nincs böngészőbeli modell letöltés vagy CDN függőség. A kép elemzés a konfigurált Azure telepítésen a szerveren fut. Hibák láthatók maradnak, és nem engedik, hogy a történet generálás hamisított leírással történjen. Egy másik fájl kiválasztása törli az előző elemzést.

**Fájl:** `result.html` (Történet megjelenítés)

Megjeleníti a generált történetet:

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

**A sablon jellemzői:**

1. **Thymeleaf integráció**: `th:` attribútumokat használ a dinamikus tartalomhoz
2. **Reszponzív dizájn**: CSS stílus mobilra és asztalira
3. **Hibakezelés**: A felhasználók számára megjeleníti az érvényesítési hibákat
4. **Feltöltés kezelése**: JavaScript előnézetet készít a fényképről, CSRF-védett multipart kérést küld, és megjeleníti a visszakapott leírást

### 5. Konfiguráció

**Fájl:** `application.properties`

Az alkalmazás konfigurációs beállításai:

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

**Konfiguráció magyarázata:**

1. **Fájl feltöltés**: Minden fájl és a teljes multipart kérés max 10MB; a képeket tartsd ezen a méreten belül a multipart fejlécek helyének megtartására
2. **Naplózás**: Szabályozza, mi kerül naplózásra futás közben
3. **Azure AI Foundry**: Meghatározza a használni kívánt végpontot és modell telepítést (kulcs nélküli hitelesítés)
4. **Biztonság**: A CSRF védelem engedélyezett marad; a modell diagnosztikák a szerveren naplózódnak, míg a vezérlő általános modell-hiba üzeneteket mutat

## Az alkalmazás futtatása

### 1. lépés: Bejelentkezés és a végpont beállítása

A hitelesítés kulcs nélküli (Microsoft Entra ID), azaz nincs API kulcs. Jelentkezz be és állítsd be a Foundry végpontodat:

**Windows (Parancssor):**
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

**Miért szükséges ez:**
- Az Azure AI Foundry Microsoft Entra ID-vel azonosítja a lekérdezéseket
- Kulcs nélküli hitelesítés azt jelenti, hogy nincs titok forráskódban vagy környezetben
- A fiókodnak rendelkeznie kell a **Cognitive Services OpenAI User** szerepkörrel az erőforráson

Az alapértelmezett telepítés neve `gpt-5.6-luna`. Ha a GPT-5.6 Luna telepítésed más néven fut, állítsd be az `AZURE_OPENAI_DEPLOYMENT` környezeti változót ugyanabban a terminálban az alkalmazás indítása előtt. Mindkét funkció az Image elemzés és a történet generálás ezt a beállítást használja.

### 2. lépés: Fordítás és futtatás

Navigálj a projekt könyvtárába:
```bash
cd 04-PracticalSamples/petstory
```

Fordítsd le a futtatható JAR-t és futtasd le az összes offline tesztet:
```bash
mvn clean package
```

Indítsd el a szervert:
```bash
mvn spring-boot:run
```

Az alkalmazás a `http://localhost:8080` címen indul el.

Vagy indítsd el a csomagolt JAR-t egy szabad porton, például:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Ehhez a parancshoz nyisd meg a `http://localhost:8083/` címet. Ugyanazok a `/analyze-image` és `/generate-story` útvonalak elérhetők a kiválasztott porton.

### 3. lépés: Teszteld az alkalmazást

1. **Nyisd meg** a `http://localhost:8080` címet a böngésződben
2. **Válassz** egy tiszta kisállat fotót JPEG, PNG, GIF vagy WebP formátumban, 10MB alatt
3. **Kattints** az "Analyze Image" gombra és várd meg a kisállat leírását
4. **Kattints** a "Generate Story" gombra a sikeres elemzés után
5. **Nézd meg** a történetet, és használd az eredmény oldalán a linket a feltöltő űrlaphoz való visszatéréshez

A sikeres képtől történetig folyamat két modell hívást tesz meg, egyet gombonként. Az élő lekérdezések használják a telepítésed kvótáját és díjkötelesek lehetnek; ossz meg rate-limited telepítéseket sorosan történő smoke tesztekhez. A kezdő oldal betöltése nem hívja a modellt.

## Offline tesztek

A sample könyvtárból futtasd:

```bash
mvn test
```

A [StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) valós OpenAI SDK kéréseket fog el egy loopback HTTP fixture-rel. Ellenőrzi mindkét kérés telepítését, `reasoning_effort: none`-t, token limiteket, kép payloadot, bemeneti validációt, üres válaszokat és upstream hibákat.

A [PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) MockMvc-t használ egy mockolt modell szolgáltatással, hogy tesztelje a Thymeleaf oldalak renderelését, feltöltési szerződést, CSRF-t, validációt, output escape-elést és látható hibákat. Ezek a tesztek nem igényelnek Azure hitelesítést és soha nem hívnak fizetős Azure inferenciát. A Maven Surefire riportokat ír a `target/surefire-reports` alá.

## Hogyan működik együtt az egész

Íme a teljes folyamat, amikor kisállat történetet generálsz:

1. **Fotó kiválasztása**: Kiválasztasz egy kisállat képet a feltöltő űrlapon
2. **Kép feltöltése**: Az "Analyze Image" multipart POST-ot küld a `/analyze-image` végpontnak a CSRF fejlécet tartalmazva
3. **Kép elemzése**: A `StoryService` elküldi a képet a GPT-5.6 Lunának, a reasoning értéke `none`
4. **Leírás megjelenítése**: A böngésző megjeleníti a visszakapott leírást és eltárolja azt az űrlapban
5. **Történet beküldése**: A "Generate Story" elküldi a `description` és `_csrf` mezőket a `/generate-story` végpontnak
6. **Történet generálás**: A vezérlő ellenőrzi a leírást és ugyanahhoz a telepítéshez hívja a modellt reasoning értékkel `none`
7. **Sablon renderelése**: A Thymeleaf escape-eli és megjeleníti a leírást és a történetet az eredmény oldalon

**Hibakezelési folyamat:**
Ha a modell hibázik, a szerver naplózza az okot. A kép elemzés HTTP 502-t ad vissza, és a böngésző megjeleníti a hibát anélkül, hogy megjelenítené a "Generate Story" gombot. A történet generálás átirányít az űrlapra hibával. Egyik út sem helyettesít csendben előre megírt eredményt.

## Az AI integráció megértése

### Azure AI Foundry (kulcs nélküli)
A szolgáltatás az SDK-t az erőforrásod `/openai/v1/` végpontjával állítja be. A `DefaultAzureCredential` és az `AuthenticationUtil.getBearerTokenSupplier` Microsoft Entra tokeneket szolgáltat a `https://ai.azure.com/.default` számára. Helyi fejlesztésnél használhatod az Azure CLI bejelentkezésed; Azure-hostolt alkalmazásnál menedzselt identitás használható a szükséges erőforrás engedélyekkel.

### Prompt tervezés
A kép elemzési kérés rövid bekezdésben kéri a megfigyelhető kisállat jegyeket, és megmondja a modellnek, hogy a képen lévő szöveget adatként kezelje, ne utasításként. A történet generálás a visszakapott leírást külön, családbarát íráskérésben használja. Egyik hívás sem engedélyezi a reasoning-et vagy állít be hőmérséklet felülírást.

### Válasz feldolgozás
A közös válaszkezelő elutasítja a hiányzó választásokat és az üres vagy csak szóközöket tartalmazó tartalmat, levágja az érvényes tartalmat, és megőrzi az upstream hibákat. A kép leírásokat 1000 karakterre korlátozza, hogy a következő történet űrlapba beleférjen. Az eredeti modell hiba megmarad diagnosztikára, de nem jelenik meg a felhasználónak.

## Következő lépések

További példákért lásd a [4. fejezet: Gyakorlati példák](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->