# MCP Számológép Oktatóanyag Kezdőknek

## Tartalomjegyzék

- [Mit Tanulhatsz Meg](#mit-tanulhatsz-meg)
- [Előfeltételek](#előfeltételek)
- [Függőség Verziók](#függőség-verziók)
- [A Projekt Felépítésének Megértése](#a-projekt-felépítésének-megértése)
- [Az Alapvető Összetevők Magyarázata](#az-alapvető-összetevők-magyarázata)
  - [1. Fő Alkalmazás](#1-fő-alkalmazás)
  - [2. Számológép Szolgáltatás](#2-számológép-szolgáltatás)
  - [3. Közvetlen MCP Ügyfél](#3-közvetlen-mcp-ügyfél)
  - [4. Mesterséges Intelligenciával Támogatott Ügyfél](#4-mesterséges-intelligenciával-támogatott-ügyfél)
- [Példák Futtatása](#példák-futtatása)
- [Offline Tesztek](#offline-tesztek)
- [Hogyan Működik Együtt Minden](#hogyan-működik-együtt-minden)
- [Következő Lépések](#következő-lépések)

## Mit Tanulhatsz Meg

Ez az oktatóanyag elmagyarázza, hogyan építsünk számológép szolgáltatást Model Context Protocol (MCP) használatával. Megérted:

- Hogyan hozz létre egy szolgáltatást, amelyet az AI eszközként használhat
- Hogyan állítsd be a közvetlen kommunikációt MCP szolgáltatásokkal
- Hogyan választhatnak automatikusan az AI modellek eszközöket
- Mi a különbség a közvetlen protokollhívások és az AI-által támogatott interakciók között

## Előfeltételek

Kezdés előtt győződj meg arról, hogy rendelkezel:
- Telepített Java 21 vagy magasabb verzióval
- Maven-t a függőségkezeléshez
- Alapvető Java és Spring Boot ismeretekkel

Csak az AI ügyfelek igényelnek Azure OpenAI telepítést és hitelesített `DefaultAzureCredential`-t,
például helyi Azure CLI bejelentkezést vagy az Azure által kezelt identitást. Az identitásnak
Cognitive Services OpenAI Felhasználói szerepkörrel kell rendelkeznie az erőforráson. Lásd [2. fejezet](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
A szerver, közvetlen SDK ügyfél és az összes automatizált teszt nem igényel Azure fiókot vagy modellhozzáférést.

## Függőség Verziók

A kiadási függőségek ellenőrizve 2026-09-14:

| Függőség | Verzió |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI által kezelt) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j hivatalos OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot által kezelt) | 6.0.3 |

Az MCP és a hivatalos OpenAI adapterek közzétett béta kiadások a Maven Central-ban, nem snapshotok.
Verzióik eltérnek a LangChain4j core verziójától. Nincs szükség snapshot vagy mérföldkő tárolókra.
Csak az ügyfél oldali függőségek tesztscope-mal rendelkeznek, mert a futtatható példák a `src/test/java` alatt vannak.

## A Projekt Felépítésének Megértése

A számológép projekt tartalmaz több fontos fájlt:

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

## Az Alapvető Összetevők Magyarázata

### 1. Fő Alkalmazás

**Fájl:** `McpServerApplication.java`

Ez a belépési pont a számológép szolgáltatásunkhoz. Egy szabványos Spring Boot alkalmazás egy különleges kiegészítéssel:

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

**Mit csinál ez:**
- Elindít egy Spring Boot web szervert a 8080-as porton
- Létrehoz egy `ToolCallbackProvider`-t, amely elérhetővé teszi számológép metódusainkat MCP eszközként
- Az `@Bean` annotáció azt mondja a Springnek, hogy ezt komponensként kezelje, amit más részek is használhatnak

### 2. Számológép Szolgáltatás

**Fájl:** `CalculatorService.java`

Itt zajlik az összes matek. Minden metódus `@Tool` megjelölést kap, hogy MCP-n keresztül elérhető legyen:

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
    
    // További számológép műveletek...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Fő jellemzők:**

1. **`@Tool` annotáció**: Ez jelzi az MCP-nek, hogy ezt a metódust külső ügyfelek hívhatják
2. **Egyértelmű leírások**: Minden eszköz egy leírással rendelkezik, ami segíti az AI modelleket, mikor használják
3. **Konzisztens visszatérési formátum**: Minden művelet emberi olvasható stringet ad vissza, pl. "5.00 + 3.00 = 8.00"
4. **Hibakezelés**: Nullával való osztás és negatív gyök hibát ad vissza

**Elérhető Műveletek:**
- `add(a, b)` - Két szám összeadása
- `subtract(a, b)` - Második kivonása az elsőből
- `multiply(a, b)` - Két szám szorzása
- `divide(a, b)` - Első osztása a másodikkal (nulla ellenőrzéssel)
- `power(base, exponent)` - Alap hatványra emelése
- `squareRoot(number)` - Négyzetgyök számítása (negatív ellenőrzéssel)
- `modulus(a, b)` - Osztási maradék visszaadása
- `absolute(number)` - Abszolút érték visszaadása
- `help()` - Információk visszaadása az összes műveletről

### 3. Közvetlen MCP Ügyfél

Lásd [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Ez az ügyfél `HttpClientStreamableHttpTransport`-ot használ `/mcp` alatt, inicializálja a kapcsolatot,
pingeli a szervert és követi az eszközlistázás lapozását. Ellenőrzi, hogy mind a kilenc elvárt eszköz
létezik, és mindegyiket hívja, beleértve a `modulus` és `help` hívásokat is, AI modell nélkül.

A jelenlegi kérésépítő így néz ki:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokollhibák ügyfél hibát okoznak, nem téves sikert jeleznek. Az MCP ügyfél a try-with-resources szerkezettel záródik,
még akkor is, ha a felfedezés vagy egy eszköz hívás meghiúsul.

### 4. Mesterséges Intelligenciával Támogatott Ügyfél

Lásd [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
és [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementálja a jelenlegi LangChain4j `ChatModel` API-t.
A `StreamableHttpMcpTransport` ugyanahhoz a `/mcp` végponthoz csatlakozik, mint az SDK ügyfél.
Az `AiServices` felfedezi az eszközöket és kezeli az eszköz-hívás/eredmény beszélgetést.

Az alapértelmezett telepítés a **GPT-5.6 Luna**, ésszerűsítés egyértelmű kikapcsolással:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ezek az alapértelmezések minden befejezésre érvényesek, beleértve az eszköz végrehajtás utáni folytatásokat is.
Az ügyfél egy frissíthető `BearerTokenCredential`-t használ, melyet a `DefaultAzureCredential` támogat,
és a `https://ai.azure.com/.default` jogosultsággal, nem egyszeri tokennel API kulcsként.
Erőforrás URL-eket és `/openai/v1`-re végződő URL-eket egyaránt elfogad.

A bot korlátozott beszélgetési előzményt tart, kiírja az `Eszköz végrehajtva: ...` sort a tényleges
MCP eredménnyel, és hibát jelez, ha a válasz kihagy eszközöket. Az eszköz hurok legfeljebb négy kör.
Hitelesítési, modell, MCP és eszköz hibák továbbterjednek; automatikus modell újrapróbálkozások ki vannak kapcsolva.
Mind az MCP átviteli/ügyfél, mind a hivatalos OpenAI ügyfél sikeren vagy hibán zárul.

## Példák Futtatása

### 1. lépés: Indítsd el a Számológép Szervert

A szerverhez nincs szükség Azure konfigurációra. Az alábbi parancsok a minta könyvtárból futnak.
A példa a **18081** portot használja, hogy ne ütközzön másik példával; az alapértelmezett 8080 marad.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Az MCP végpont `http://localhost:18081/mcp`. Az állapot és felfedezési információk elérhetők a
`http://localhost:18081/health` és `http://localhost:18081/info` címen.
A Streamable HTTP váltotta az régi SSE-only átvitelt; `/sse` és `/v1/tools` nem végpontok.

### 2. lépés: Teszteld Közvetlen Ügyféllel

Egy másik PowerShell terminálban:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Nincs szükség bemenetre. Az összes kilenc eszköz használatban van. Várt eredmények között van
8, 6, 42, 5, 256, 4, 2, és 5.5, amit az help szöveg követ.

### 3. lépés: Teszteld AI Ügyféllel

A hitelesítés után az előfeltételek szerint, konfiguráld az AI ügyfelet ugyanabban a terminálban:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Várható egy `Eszköz végrehajtva: add` sor `41.80` értékkel, majd a modell válasza.
Az egy-prompt mód kilép bemenetre várás nélkül. Az eredeti négy-prompt demó futtatásához:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

A demó hívja az `add`, `squareRoot`, `help`, és a láncolt `power` majd `divide` műveletet.
Várt numerikus válaszok: 41.8, 12, és 64. Argumentumok elhagyása is futtatja ezt a demót.

### 4. lépés: Futtasd az Interaktív Botot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Írd be: `Szorozd meg 6-tal a 7-et a számológép szolgáltatás használatával`, majd `exit` vagy `quit`.
Várj egy tényleges `multiply` eszköz eredményt, ami 42. Az üres sorokat figyelmen kívül hagyjuk; az EOF is véget vet a munkamenetnek.
Nem interaktív smoke tesztjéhez ennek a belépési pontnak:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Mindkét AI belépési pont elfogadja a `--prompt "kérdés"`, `--demo` és `--interactive` opciókat.
Érvénytelen opciók a kapcsolatnyitás előtt hibáznak. Minden Maven `-D...` argumentum teljesen idézett PowerShell-ben.
Bash alatt használd inkább az `export NAME=value` formát a `$env:NAME = "value"` helyett.

**Kvóta:** Futtasd az AI mintákat egymás után. Egy egyszerű prompt általában két modellikérést igényel;
a teljes demó általában kilencet, beleértve az eszköz-eredmény utáni kéréseket. Megosztott 10 RPM
telepítésnél várj egy friss kvótaablakot a következő AI futtatás előtt. A 429 láthatóan hibával tér vissza automatikus
újrapróbálkozás nélkül; kövesd a szolgáltatás utasításait. A tényleges kérésszám a modelltől függ.
Az offline tesztek nem fogynak kvótából és nem állapítanak meg élő Luna hozzáférést vagy válaszminőséget.

### Konfiguráció és Leállítás

| Beállítás | Alapértelmezett / viselkedés |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; alap URL, `/mcp` nélkül |
| `-Dmcp.server.url=...` | Felülírja az `MCP_SERVER_URL`-t minden ügyfél esetében |
| `AZURE_OPENAI_ENDPOINT` | Csak AI ügyfeleknek; erőforrás URL vagy `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; egy Azure telepítés neve |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; pozitív egész szám |
| Ésszerűsítési erőfeszítés | Mindig `none`, beleértve az eszköz-hurok folytatásokat is |

A felülírt telepítésnek támogatnia kell a `reasoning_effort=none` és a `max_completion_tokens` beállításokat.
Az ügyfelek automatikusan nem olvasnak `.env` fájlt. Tesztelés után állítsd le a szervert `Ctrl+C`-vel.
Az ügyfelek normálisan térnek vissza, nem használnak `System.exit`-et vagy leállítási késleltetést.

## Offline Tesztek

```powershell
mvn -B -ntp clean verify
```

Minden teszt offline az Azure-hoz képest: a protokoll csomag elindít egy Spring szervert és
egy OpenAI-kompatibilis sztubot véletlenszerű loopback portokon, majd bezárja azokat. A Maven
még mindig tölthet le függőségeket. Nem használnak hitelesítő adatot, élő telepítést vagy előzetes MCP szervert.

- Számológép egység tesztek lefedik az összes aritmetikai műveletet, tizedes eredményeket, help-et és domain hibákat.
- MCP tesztek lefedik az inicializációt, felfedezést, a kilenc eszköz hívást, eszköz hibákat és health/info-t.
- AI protokoll tesztek a teljes demót és interaktív Botot futtatják a valódi számológéppel,
  ellenőrzik, hogy az eszköz eredmények táplálják a következő befejezést, és HTTP törzset vizsgálnak Luna,
  `reasoning_effort: "none"`, és `max_completion_tokens` értékekkel, örökölt `max_tokens` nélkül.
- Konfiguráció/bemeneti tesztek lefedik a telepítési és végpontra vonatkozó felülírásokat, üres sorokat, EOF-ot, kilépést/lezárást,
  egyprompt módot, érvénytelen opciókat és hiba továbbterjedést. Kvóta tesztek bizonyítják, hogy a 429 nem ismétlődik.

## Hogyan Működik Együtt Minden

Itt van a teljes folyamat, amikor megkérdezed az AI-tól: „Mennyi 5 + 3?”:

1. **Te** megkéred az AI-t természetes nyelven
2. **AI** elemzi a kérésed és rájön, hogy összeadást szeretnél
3. **AI** meghívja az MCP szervert: `add(5.0, 3.0)`
4. **Számológép Szolgáltatás** végrehajtja: `5.0 + 3.0 = 8.0`
5. **Számológép Szolgáltatás** visszaadja: `"5.00 + 3.00 = 8.00"`
6. **AI** megkapja az eredményt és természetes választ formáz
7. **Te** megkapod: "5 és 3 összege 8"

## Következő Lépések

További példákért lásd a [04. fejezet: Gyakorlati példák](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->