# MCP skaičiuoklės vadovas pradedantiesiems

## Turinys

- [Ką išmoksite](#ką-išmoksite)
- [Reikalavimai](#reikalavimai)
- [Priklausomybių versijos](#priklausomybių-versijos)
- [Projekto struktūros supratimas](#projekto-struktūros-supratimas)
- [Pagrindinių komponentų paaiškinimas](#pagrindinių-komponentų-paaiškinimas)
  - [1. Pagrindinė programa](#1-pagrindinė-programa)
  - [2. Skaičiuoklės paslauga](#2-skaičiuoklės-paslauga)
  - [3. Tiesioginis MCP klientas](#3-tiesioginis-mcp-klientas)
  - [4. Dirbtiniu intelektu pagrįstas klientas](#4-dirbtiniu-intelektu-pagrįstas-klientas)
- [Pavyzdžių paleidimas](#pavyzdžių-paleidimas)
- [Offline testai](#offline-testai)
- [Kaip viskas veikia kartu](#kaip-viskas-veikia-kartu)
- [Kiti žingsniai](#kiti-žingsniai)

## Ką išmoksite

Šis vadovas paaiškina, kaip sukurti skaičiuoklės paslaugą naudojant Model Context Protocol (MCP). Suprasite:

- Kaip sukurti paslaugą, kurią AI gali naudoti kaip įrankį
- Kaip nustatyti tiesioginį ryšį su MCP paslaugomis
- Kaip AI modeliai gali automatiškai pasirinkti, kokius įrankius naudoti
- Kokie skirtumai tarp tiesioginių protokolo kvietimų ir AI pagalbinės sąveikos

## Reikalavimai

Prieš pradėdami, įsitikinkite, kad turite:
- Įdiegtą Java 21 ar naujesnę versiją
- Maven skirtą priklausomybių valdymui
- Pagrindines Java ir Spring Boot žinias

Tik AI klientams reikalinga Azure OpenAI diegimas ir autentifikuotas `DefaultAzureCredential`,
pavyzdžiui, vietinė esama prisijungimo per Azure CLI arba valdoma tapatybė Azure. Tapatybė turi
turėti Cognitive Services OpenAI vartotojo vaidmenį resurse. Žr. [2 skyrių](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Serveriui, tiesioginiam SDK klientui ir visiems automatiniams testams Azure paskyra ar modeliui prieiga nereikalinga.

## Priklausomybių versijos

Išleistas priklausomybes patikrinta 2026-09-14:

| Priklausomybė | Versija |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI valdomas) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j oficialus OpenAI adapteris | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (valdomas Boot) | 6.0.3 |

MCP ir oficialūs OpenAI adapteriai yra paskelbti beta versijomis Maven Central, ne snapshotai.
Jų versijos skiriasi nuo LangChain4j core. Snapshootų ar milestone kodų saugyklos nereikalingos.
Klientų priklausomybės turi testų apimtį, nes paleidžiami pavyzdžiai yra `src/test/java` direktorijoje.

## Projekto struktūros supratimas

Skaičiuoklės projekte yra keli svarbūs failai:

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

## Pagrindinių komponentų paaiškinimas

### 1. Pagrindinė programa

**Failas:** `McpServerApplication.java`

Tai įėjimo taškas mūsų skaičiuoklės paslaugai. Standartinė Spring Boot programa su viena ypatinga papildoma dalimi:

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

**Ką tai daro:**
- Paleidžia Spring Boot tinklo serverį 8080 prievade
- Sukuria `ToolCallbackProvider`, kuris leidžia naudotis mūsų skaičiuoklės metodais kaip MCP įrankiais
- `@Bean` anotacija leidžia Spring tvarkyti šį komponentą, kurį gali naudoti kitos dalys

### 2. Skaičiuoklės paslauga

**Failas:** `CalculatorService.java`

Čia vyksta visa matematika. Kiekvienas metodas pažymėtas `@Tool`, kad jis būtų prieinamas per MCP:

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
    
    // Daugiau kalkuliatoriaus operacijų...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Pagrindinės savybės:**

1. **`@Tool` anotacija**: Informuoja MCP, kad šį metodą gali iškviesti išoriniai klientai
2. **Aiškūs aprašymai**: Kiekvienas įrankis turi aprašymą, padedantį AI modeliams suprasti, kada jį naudoti
3. **Nuoseklus gražinimo formatas**: Visos operacijos gražina žmonėms suprantamus tekstus, pvz., "5.00 + 3.00 = 8.00"
4. **Klaidų tvarkymas**: Dalijimas iš nulio ir neigiamo šaknies ėmimas gražina klaidų pranešimus

**Galimos operacijos:**
- `add(a, b)` - Sudeda du skaičius
- `subtract(a, b)` - Atima antrą iš pirmo
- `multiply(a, b)` - Dauginama du skaičius
- `divide(a, b)` - Dalina pirmą iš antro (su patikra dėl nulio)
- `power(base, exponent)` - Pakelia bazę laipsniu
- `squareRoot(number)` - Skaičiuoja kvadratinę šaknį (su neigiamo patikra)
- `modulus(a, b)` - Gražina dalybos liekaną
- `absolute(number)` - Gražina absoliučią vertę
- `help()` - Gražina informaciją apie visas operacijas

### 3. Tiesioginis MCP klientas

Žr. [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Šis klientas naudoja `HttpClientStreamableHttpTransport` adresu `/mcp`, inicializuoja ryšį,
atlieka serverio pinginimą ir seka įrankių sąrašo puslapiavimą. Tikrina, ar egzistuoja visi devyni laukiamieji įrankiai
ir iškviečia kiekvieną iš jų, įskaitant `modulus` ir `help`, be AI modelio.

Dabartinis užklausos konstruktorius atrodo taip:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokolo klaidos priverčia klientą sugesti, neužrašant klaidingos sėkmės. MCP klientas
uždaromas naudojant try-with-resources, įskaitant ir kai aptikimas ar įrankio kvietimas nepavyksta.

### 4. Dirbtiniu intelektu pagrįstas klientas

Žr. [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
ir [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` įgyvendina dabartinį LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` jungia jį prie to paties `/mcp` galinio taško kaip SDK klientą.
`AiServices` atranda įrankius ir valdo pokalbį apie įrankių kvietimus ir rezultatus.

Numatytoji diegimo versija yra **GPT-5.6 Luna**, su aiškiai išjungtu samprotavimu:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Šie numatytieji nustatymai taikomi kiekvienam užbaigimui, įskaitant ir tolimesnius veiksmus po įrankio vykdymo.
Klientas naudoja atnaujinamą `BearerTokenCredential`, pagrįstą `DefaultAzureCredential`
ir `https://ai.azure.com/.default` sritį, o ne vienkartinį žetoną kaip API raktą.
Priimami tiek resursų URL, tiek URL, kurie jau baigiasi `/openai/v1`.

Botas saugo ribotą pokalbio istoriją, išveda `Tool executed: ...` su faktiniu
MCP rezultatu ir sugesta, jei atsakyme praleidžiami įrankiai. Įrankių ciklai ribojami iki keturių turų.
Autentifikacijos, modelio, MCP ir įrankių klaidos perduodamos; automatiniai modelio bandymai iš naujo išjungti.
Tiek MCP transportas/klientas, tiek oficialus OpenAI klientas uždaromi sėkmės ar nesėkmės atveju.

## Pavyzdžių paleidimas

### 1 žingsnis: paleisti skaičiuoklės serverį

Serveriui nereikia konfiguruoti Azure. Žemiau pateiktos komandos paleidžiamos iš šio pavyzdžio aplanko.
Pavyzdyje naudojamas prievadas **18081**, kad nesikirstų su kitu pavyzdžiu; numatytasis liko 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP galinio taško adresas yra `http://localhost:18081/mcp`. Sveikatingumo ir aptikimo informacija prieinama adresuose
`http://localhost:18081/health` ir `http://localhost:18081/info`.
Streamable HTTP pakeičia seną tik SSE transportą; `/sse` ir `/v1/tools` nėra galiniai taškai.

### 2 žingsnis: testuoti su tiesioginiu klientu

Kitame PowerShell terminale:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Vartotojo įvestis nereikalinga. Išbandomi visi devyni įrankiai. Laukiami aritmetiniai rezultatai yra
8, 6, 42, 5, 256, 4, 2 ir 5.5, po to pateikiamas pagalbos tekstas.

### 3 žingsnis: testuoti su AI klientu

Po autentifikacijos, kaip aprašyta reikalavimuose, sukonfigūruokite AI klientą tame pačiame terminale:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Laukite eilutės `Tool executed: add` su `41.80`, po to modelio atsakymo.
Vieno užklausos režimas išeina nebesulaukdamas įvesties. Norint paleisti pirminį keturių užklausų demonstravimą:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demonstracija iškviečia `add`, `squareRoot`, `help` ir surištą `power` bei `divide` operacijas.
Laukiami skaitiniai atsakymai yra 41.8, 12 ir 64. Argumentų nenurodymas taip pat paleidžia šią demonstraciją.

### 4 žingsnis: paleisti interaktyvų botą

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Įveskite `Multiply 6 by 7 using the calculator service`, tada `exit` arba `quit`.
Laukite faktinio `multiply` įrankio rezultato 42. Tuščios eilutės yra ignoruojamos; EOF taip pat baigia sesiją.
Neinteraktyviam patikrinimui šiam įėjimo taškui:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Abu AI įėjimo taškai priima `--prompt "question"`, `--demo` ir `--interactive`.
Netinkamos parinktys priverčia sugesti prieš atidarant ryšį. Kiekvienas Maven `-D...` argumentas yra pilnai cituojamas
PowerShell aplinkoje. Bash naudokite `export NAME=value` vietoje `$env:NAME = "value"`.

**Kvota:** Vykdykite AI pavyzdžius paeiliui. Paprasta užklausa paprastai reikalauja dviejų modelio užklausų;
pilna demonstracija paprastai reikalauja devynių, įskaitant įrankių rezultatų tęsinį. Dalinamoje 10 RPM
diegimo kvotoje palaukite naujo lango prieš kitą AI vykdymą. 429 klaidos iš karto neveikia pakartotinai;
vykdykite paslaugos nurodymus retry-after. Faktiniai užklausų skaičiai priklauso nuo modelio.
Offline testai nekonsumuoja jokios kvotos ir nenustato tiesioginės Luna prieinamumo ar atsakymų kokybės.

### Konfigūracija ir išjungimas

| Nustatymas | Numatytoji reikšmė / elgsena |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; bazinis adresas, be `/mcp` |
| `-Dmcp.server.url=...` | Pakeičia `MCP_SERVER_URL` visiems klientams |
| `AZURE_OPENAI_ENDPOINT` | Reikia tik AI klientams; resurso URL arba `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure diegimo pavadinimas |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; teigiamas sveikasis skaičius |
| Samprotavimo pastangos | Visada `none`, įskaitant įrankių ciklo tęsinį |

Pakeistas diegimas turi palaikyti `reasoning_effort=none` ir `max_completion_tokens`.
Klientai automatiškai neskaito `.env` failo. Baigę testavimą sustabdykite serverį su `Ctrl+C`.
Klientai grąžina kontrolę normaliai, be `System.exit` ar miego laikų prieš išjungimą.

## Offline testai

```powershell
mvn -B -ntp clean verify
```

Visi testai vykdomi offline atžvilgiu Azure: protokolo rinkinys paleidžia Spring serverį ir
OpenAI suderinamą imitaciją atsitiktiniuose loopback prievaduose, tada uždaro juos. Maven gali vis tiek reikalauti
parsisiųsti priklausomybes. Naudojami neprieinami kredencialai, tiesioginis diegimas ar esamas MCP serveris.

- Skaičiuoklės vienetų testai apima visas aritmetines operacijas, dešimtainius rezultatus, pagalbą ir domeno klaidas.
- MCP testai apima inicijavimą, aptikimą, visus devynis įrankių kvietimus, įrankių klaidas, sveikatą ir informaciją.
- AI protokolo testai vykdo pilną demonstraciją ir interaktyvų botą prieš tikrą skaičiuoklę,
  patvirtina, kad įrankių rezultatai yra perduodami kitam užbaigimui, ir tikrina kiekvieną HTTP turinį dėl Luna,
  `reasoning_effort: "none"` ir `max_completion_tokens` be senovinio `max_tokens`.
- Konfigūracijos/įvesties testai apima diegimo ir galinio taško perrašymus, tuščias eilutes, EOF, exit/quit,
  vieno užklausimo režimą, klaidingas parinktis ir klaidų sklaidą. Kvotos testai įrodo, kad 429 neperbandomas.

## Kaip viskas veikia kartu

Štai pilnas srautas, kai paklausiate AI „Kiek yra 5 + 3?“:

1. **Jūs** užduodate klausimą AI natūralia kalba
2. **AI** analizuoja jūsų užklausą ir supranta, kad norite sudėti
3. **AI** iškviečia MCP serverį: `add(5.0, 3.0)`
4. **Skaičiuoklės paslauga** atlieka: `5.0 + 3.0 = 8.0`
5. **Skaičiuoklės paslauga** grąžina: `"5.00 + 3.00 = 8.00"`
6. **AI** gauna rezultatą ir formuoja natūralų atsakymą
7. **Jūs** gaunate: „5 ir 3 suma yra 8“

## Kiti žingsniai

Daugiau pavyzdžių žr. [4 skyrių: Praktiniai pavyzdžiai](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kilusią naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->