# Vadnica za začetnike: MCP kalkulator

## Kazalo

- [Kaj se boste naučili](#kaj-se-boste-naučili)
- [Predpogoji](#predpogoji)
- [V različice odvisnosti](#različice-odvisnosti)
- [Razumevanje strukture projekta](#razumevanje-strukture-projekta)
- [Razlaga osnovnih komponent](#razlaga-osnovnih-komponent)
  - [1. Glavna aplikacija](#1-glavna-aplikacija)
  - [2. Storitve kalkulatorja](#2-storitve-kalkulatorja)
  - [3. Neposredni MCP odjemalec](#3-neposredni-mcp-odjemalec)
  - [4. Odjemalec z AI](#4-odjemalec-z-ai)
- [Zagon primerov](#zagon-primerov)
- [Preizkusi brez povezave](#preizkusi-brez-povezave)
- [Kako vse deluje skupaj](#kako-vse-deluje-skupaj)
- [Naslednji koraki](#naslednji-koraki)

## Kaj se boste naučili

Ta vadnica razlaga, kako zgraditi storitev kalkulatorja z uporabo Model Context Protocol (MCP). Spoznali boste:

- Kako ustvariti storitev, ki jo lahko AI uporablja kot orodje
- Kako vzpostaviti neposredno komunikacijo s storitvami MCP
- Kako lahko AI modeli samodejno izbirajo, katera orodja uporabiti
- Razliko med neposrednimi klici protokola in interakcijami z asistenco AI

## Predpogoji

Pred začetkom se prepričajte, da imate:
- nameščen Java 21 ali višje
- Maven za upravljanje odvisnosti
- osnovno razumevanje Java in Spring Boot

Samo AI odjemalci potrebujejo namestitev Azure OpenAI in overjeni `DefaultAzureCredential`,
na primer obstoječo lokalno prijavo v Azure CLI ali upravljano identiteto v Azure. Identiteta mora
imeti vlogo uporabnika Cognitive Services OpenAI na viru. Oglejte si [Poglavje 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Strežnik, neposredni SDK odjemalec in vsi samodejni testi ne potrebujejo Azure računa ali dostopa do modela.

## Različice odvisnosti

Odvisnosti za izdajo, preverjene 2026-09-14:

| Odvisnost | Različica |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (upravljanje Spring AI) | 2.0.0 |
| LangChain4j / jedro | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j uradni OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (upravljanje Boot) | 6.0.3 |

MCP in uradni OpenAI adapterji so objavljene beta različice v Maven Central, ne posnetki.
Njihove različice se razlikujejo od LangChain4j jedra. Ni potrebnih nobenih posnetkov ali mejnih skladišč.
Odvisnosti le za odjemalce imajo obseg testiranja, saj se za zagon primerov uporabljajo `src/test/java`.

## Razumevanje strukture projekta

Projekt kalkulatorja vsebuje več pomembnih datotek:

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

## Razlaga osnovnih komponent

### 1. Glavna aplikacija

**Datoteka:** `McpServerApplication.java`

To je vstopna točka naše storitve kalkulatorja. Gre za standardno Spring Boot aplikacijo z eno posebno dodano funkcionalnostjo:

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

**Kaj ta del naredi:**
- Začne Spring Boot spletni strežnik na vratih 8080
- Ustvari `ToolCallbackProvider`, ki naredi metode našega kalkulatorja dostopne kot MCP orodja
- Oznaka `@Bean` pove Springu, naj to upravlja kot komponento, ki jo lahko uporabljajo druge dele

### 2. Storitve kalkulatorja

**Datoteka:** `CalculatorService.java`

Tu se izvaja vsa matematika. Vsaka metoda je označena z `@Tool`, da je dostopna prek MCP:

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
    
    // Več operacij kalkulatorja...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Ključne značilnosti:**

1. **Oznaka `@Tool`**: Sporoča MCP, da lahko zunanji odjemalci kličejo to metodo
2. **Jasni opisi**: Vsako orodje ima opis, ki pomaga AI modelom razumeti, kdaj ga uporabiti
3. **Dosleden format vrnitve**: Vse operacije vračajo berljive nize, na primer "5.00 + 3.00 = 8.00"
4. **Obravnava napak**: Deljenje z nič in negativne korene vračajo sporočila o napaki

**Na voljo operacije:**
- `add(a, b)` - seštevanje dveh števil
- `subtract(a, b)` - odštevanje drugega od prvega
- `multiply(a, b)` - množenje dveh števil
- `divide(a, b)` - deljenje prvega z drugim (s preverjanjem ničle)
- `power(base, exponent)` - potencialna potenca baze
- `squareRoot(number)` - izračun kvadratnega korena (s preverjanjem negativnosti)
- `modulus(a, b)` - vrne ostanek deljenja
- `absolute(number)` - vrne absolutno vrednost
- `help()` - vrne informacije o vseh operacijah

### 3. Neposredni MCP odjemalec

Glejte [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Ta odjemalec uporablja `HttpClientStreamableHttpTransport` na `/mcp`, inicializira povezavo,
pošilja ping strežniku in sledi paginaciji seznama orodij. Preveri obstoj vseh devetih pričakovanih orodij
in kliče vsako posebej, vključno z `modulus` in `help`, brez uporabe AI modela.

Trenutni gradnik zahtev izgleda takole:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokolarne napake povzročijo neuspeh odjemalca namesto zavajajoče uspešne vrnitve. MCP odjemalec
se zapre s try-with-resources, tudi če ne uspe odkriti ali poklicati orodja.

### 4. Odjemalec z AI

Glejte [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
in [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementira trenutno LangChain4j API za `ChatModel`.
`StreamableHttpMcpTransport` ga poveže na isti `/mcp` konektor kot SDK odjemalec.
`AiServices` odkrije orodja in upravlja pogovor o klicu orodja/reultatu.

Privzeta namestitev je **GPT-5.6 Luna**, z izrecno onemogočenim sklepčanjem:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ti privzeti nastavitvi veljata za vsak dokončan odgovor, tudi nadaljnje po izvedbi orodja.
Odjemalec uporablja osvežljiv `BearerTokenCredential`, podprt z `DefaultAzureCredential`
in obsegom `https://ai.azure.com/.default`, namesto enkratnega žetona posredovanega kot API ključ.
Sprejemljivi so URL-ji virov in URL-ji, ki že končajo z `/openai/v1`.

Bot ohranja omejen zgodovinski pogovor, izpiše `Tool executed: ...` z dejanskim
MCP rezultatom in odpove, če je odgovor brez uporabe orodij. Zanke orodij so omejene na štiri kroge.
Avtentikacija, model, MCP in orodje napake se prenašajo; samodejni ponovni poskusi modela so onemogočeni.
MCP transport/odjemalec in uradni OpenAI odjemalec se zapirata ob uspehu ali neuspehu.

## Zagon primerov

### 1. korak: Zaženite strežnik kalkulatorja

Za strežnik ni potrebna konfiguracija Azure. Ukazi spodaj se izvajajo iz direktorija tega vzorca.
Primer uporablja vrata **18081**, da se izogne konfliktu z drugim vzorcem; privzeto je 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP dostopna točka je `http://localhost:18081/mcp`. Informacije o zdravju in odkritju so na
`http://localhost:18081/health` in `http://localhost:18081/info`.
Streamable HTTP nadomešča staro transportno metodo SSE; `/sse` in `/v1/tools` nista dostopni točki.

### 2. korak: Test z neposrednim odjemalcem

V drugem terminalu PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Vnos ni potreben. Preizkusi se vseh devet orodij. Pričakovani aritmetični rezultati vključujejo
8, 6, 42, 5, 256, 4, 2 in 5,5, sledita pa opis pomoči.

### 3. korak: Test z AI odjemalcem

Po autentikaciji, opisani pod predpogoji, v istem terminalu nastavite AI odjemalca:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Pričakujte vrstico `Tool executed: add` z rezultatom `41.80`, sledil bo odgovor modela.
Enopromptski način izstopi brez čakanja na vnos. Za zagon originalnega demo s štirimi pozivi:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo kliče operacije `add`, `squareRoot`, `help` in zaporedno `power` nato `divide`.
Pričakovani številčni odgovori so 41.8, 12 in 64. Demo se zažene tudi brez podanih argumentov.

### 4. korak: Zaženite interaktivnega bota

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Vnesite `Multiply 6 by 7 using the calculator service`, nato `exit` ali `quit`.
Pričakujte dejanski rezultat orodja `multiply` 42. Prazne vrstice se prezrejo; EOF tudi zaključi sejo.
Za neinterakcijski preizkus te vstopne točke:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Obe AI vstopni točki sprejemata `--prompt "question"`, `--demo` in `--interactive`.
Napačne možnosti povzročijo napake pred vzpostavitvijo povezave. Vsak Maven `-D...` argument je v celoti podan v narekovajih
za PowerShell. V Bash-u raje uporabite `export NAME=value` namesto `$env:NAME = "value"`.

**Kvote:** Zaženite AI primere zaporedno. Enostaven poziv običajno potrebuje dva modelna zahtevka;
celoten demo običajno devet, vključno s nadaljnjimi odzivi po izvedbi orodja. Pri 10 RPM deljeni
namestitvi počakajte na obnovitev kvote pred naslednjim zagonom AI. Koda 429 povzroči vidno neuspešnost brez
samodejnih ponovnih poskusov; upoštevajte navodila o ponovnem poskusu storitve. Dejanska števila zahtevkov so odvisna od modela.
Preizkusi brez povezave ne uporabljajo kvot in ne preverjajo dejanske razpoložljivosti Lune ali kakovosti odgovorov.

### Konfiguracija in zaustavitev

| Nastavitev | Privzeto / vedenje |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; osnovni URL, brez `/mcp` |
| `-Dmcp.server.url=...` | Preglasi `MCP_SERVER_URL` za vse odjemalce |
| `AZURE_OPENAI_ENDPOINT` | Potrebno samo za AI odjemalce; URL vira ali URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; ime Azure namestitve |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; pozitivno celo število |
| Napor sklepanja | Vedno `none`, tudi za nadaljevanja v orodjih |

Prepisana namestitev mora podpirati `reasoning_effort=none` in `max_completion_tokens`.
Odjemalci ne preberejo `.env` datoteke samodejno. Strežnik ustavite s `Ctrl+C` po testiranju.
Odjemalci se običajno vrnejo brez `System.exit` ali z zamudo zaustavitve.

## Preizkusi brez povezave

```powershell
mvn -B -ntp clean verify
```

Vsi testi so brez povezave glede na Azure: paket protokolov zažene Spring strežnik in
OpenAI-kompatibilen stub na naključnih loopback vratih, nato ju zapre. Maven lahko še vedno potrebuje
prenos odvisnosti. Ne uporabljajo se poverilnice, živa namestitev ali obstoječ MCP strežnik.

- Enote za kalkulator preizkušajo vse aritmetične operacije, decimalne rezultate, pomoč in domenska sporočila o napakah.
- MCP testi zajemajo inicializacijo, odkritje, vseh devet klicev orodij, napake orodij in informacije o zdravju.
- AI protokolarni testi izvajajo celoten demo in interaktivnega bota proti pravemu kalkulatorju,
  preverjajo, da rezultati orodij napajajo naslednje dokončanje, in pregledajo vsak HTTP zahtevek za Luno,
  `reasoning_effort: "none"` in `max_completion_tokens` brez starega `max_tokens`.
- Preizkusi konfiguracije/vnosa zajemajo preglasitve namestitve in dostopnih točk, prazne vrstice, EOF, izhod/quit,
  enopromptski način, napačne možnosti in propagacijo napak. Preizkusi kvot dokazujejo, da koda 429 ni samodejno ponovljena.

## Kako vse deluje skupaj

Tukaj je celoten proces, ko vprašate AI: "Kaj je 5 + 3?":

1. **Vi** vprašate AI v naravnem jeziku
2. **AI** analizira vašo zahtevo in sklene, da želite seštevanje
3. **AI** pokliče MCP strežnik: `add(5.0, 3.0)`
4. **Storitve kalkulatorja** izvedejo: `5.0 + 3.0 = 8.0`
5. **Storitve kalkulatorja** vrnejo: `"5.00 + 3.00 = 8.00"`
6. **AI** prejme rezultat in oblikuje naraven odgovor
7. **Vi** prejmete: "Vsota 5 in 3 je 8"

## Naslednji koraki

Za več primerov glejte [Poglavje 04: Praktični primeri](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba obravnavati kot avtoritativni vir. Za kritične informacije je priporočljiv strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->