# MCP Kalkulator Vodič za Početnike

## Sadržaj

- [Što ćete naučiti](#što-ćete-naučiti)
- [Preduvjeti](#preduvjeti)
- [Verzije ovisnosti](#verzije-ovisnosti)
- [Razumijevanje strukture projekta](#razumijevanje-strukture-projekta)
- [Objašnjenje glavnih komponenti](#objašnjenje-glavnih-komponenti)
  - [1. Glavna aplikacija](#1-glavna-aplikacija)
  - [2. Calculator Service](#2-calculator-service)
  - [3. Direktni MCP klijent](#3-direktni-mcp-klijent)
  - [4. Klijent s podrškom AI](#4-klijent-s-podrškom-ai)
- [Pokretanje primjera](#pokretanje-primjera)
- [Offline testovi](#offline-testovi)
- [Kako sve to funkcionira zajedno](#kako-sve-to-funkcionira-zajedno)
- [Sljedeći koraci](#sljedeći-koraci)

## Što ćete naučiti

Ovaj vodič objašnjava kako izgraditi uslugu kalkulatora koristeći Model Context Protocol (MCP). Naučit ćete:

- Kako stvoriti uslugu koju AI može koristiti kao alat
- Kako uspostaviti direktnu komunikaciju s MCP uslugama
- Kako AI modeli mogu automatski odabrati koje alate koristiti
- Razliku između direktnih protokol poziva i AI-pomoću interakcija

## Preduvjeti

Prije početka, osigurajte da imate:
- Instaliran Java 21 ili noviji
- Maven za upravljanje ovisnostima
- Osnovno razumijevanje Jave i Spring Boota

Samo AI klijenti zahtijevaju Azure OpenAI implementaciju i autentificirani `DefaultAzureCredential`,
kao što je postojeći lokalni Azure CLI prijava ili upravljani identitet u Azureu. Identitet treba
ulogu Cognitive Services OpenAI User na resursu. Pogledajte [Poglavlje 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Server, direktni SDK klijent i svi automatizirani testovi ne trebaju Azure račun niti pristup modelu.

## Verzije ovisnosti

Verzije ovisnosti potvrđene 2026-09-14:

| Ovisnost | Verzija |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (pod upravljanjem Spring AI) | 2.0.0 |
| LangChain4j / osnovno | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j službeni OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (pod upravljanjem Boota) | 6.0.3 |

MCP i službeni OpenAI adapteri su objavljene beta verzije u Maven Centralu, ne snapshot verzije.
Njihove verzije razlikuju se od LangChain4j core-a. Nisu potrebni snapshot ili milestone repozitoriji.
Ovisnosti klijenata su s test scopeom jer se pokretni primjeri nalaze u `src/test/java`.

## Razumijevanje strukture projekta

Kalkulator projekt sadrži nekoliko važnih datoteka:

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

## Objašnjenje glavnih komponenti

### 1. Glavna aplikacija

**Datoteka:** `McpServerApplication.java`

Ovo je ulazna točka naše usluge kalkulatora. Standardna je Spring Boot aplikacija s jednim posebnim dodatkom:

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

**Što ovo radi:**
- Pokreće Spring Boot web server na portu 8080
- Stvara `ToolCallbackProvider` koji čini naše metode kalkulatora dostupnima kao MCP alate
- `@Bean` anotacija govori Springu da ovo upravlja kao komponentom koju drugi dijelovi mogu koristiti

### 2. Calculator Service

**Datoteka:** `CalculatorService.java`

Ovdje se odvijaju sve matematičke operacije. Svaka metoda je označena s `@Tool` kako bi bila dostupna putem MCP-a:

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
    
    // Više operacija kalkulatora...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Ključne značajke:**

1. **`@Tool` anotacija**: Ovo govori MCP-u da metoda može biti pozvana od strane vanjskih klijenata
2. **Jasni opisi**: Svaki alat ima opis koji pomaže AI modelima razumjeti kada ga koristiti
3. **Konzistentan format povratka**: Sve operacije vraćaju čitljive stringove poput "5.00 + 3.00 = 8.00"
4. **Rukovanje greškama**: Dijeljenje s nulom i negativni korijen vraćaju poruke o pogrešci

**Dostupne operacije:**
- `add(a, b)` - Zbraja dva broja
- `subtract(a, b)` - Oduzima drugi od prvog
- `multiply(a, b)` - Množi dva broja
- `divide(a, b)` - Dijeli prvi s drugim (s provjerom na nulu)
- `power(base, exponent)` - Podigne bazu na eksponent
- `squareRoot(number)` - Izračunava kvadratni korijen (s provjerom negativnog broja)
- `modulus(a, b)` - Vraća ostatak dijeljenja
- `absolute(number)` - Vraća apsolutnu vrijednost
- `help()` - Vraća informacije o svim operacijama

### 3. Direktni MCP klijent

Pogledajte [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Ovaj klijent koristi `HttpClientStreamableHttpTransport` na `/mcp`, inicijalizira konekciju,
ping-a server i prati paginaciju popisa alata. Provjerava da svih devet očekivanih alata
postoji i poziva svaki od njih, uključujući `modulus` i `help`, bez AI modela.

Trenutni builder zahtjeva izgleda ovako:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokolne greške uzrokuju neuspjeh klijenta umjesto praćenja zavaravajućeg uspjeha. MCP klijent
se zatvara s try-with-resources, uključujući kad otkrivanje ili poziv alata ne uspiju.

### 4. Klijent s podrškom AI

Pogledajte [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
i [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementira trenutni LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` povezuje ga s istim `/mcp` endpointom kao SDK klijent.
`AiServices` otkriva alate i upravlja konverzacijom poziva/alata i rezultata.

Zadana implementacija je **GPT-5.6 Luna**, s eksplicitno onemogućenim zaključivanjem:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ova zadana podešavanja vrijede za svaki završetak, uključujući naknadne pozive nakon izvođenja alata.
Klijent koristi osvježivi `BearerTokenCredential` podržan od `DefaultAzureCredential`
i `https://ai.azure.com/.default` scope, ne token proslijeđen kao API ključ.
Prihvaćaju se URL-ovi resursa i URL-ovi koji već završavaju na `/openai/v1`.

Bot održava ograničenu povijest konverzacije, ispisuje `Tool executed: ...` s pravim
MCP rezultatom, i neuspijeva ako odgovor preskače alate. Petlje alata ograničene su na četiri kruga.
Autentifikacijske, modelne, MCP i alatne greške se prenose; automatski ponovni pokušaji modela su onemogućeni.
MCP transport/klijent i službeni OpenAI klijent zatvaraju se na uspjeh ili neuspjeh.

## Pokretanje primjera

### Korak 1: Pokrenite Kalkulator Server

Nije potrebna Azure konfiguracija za server. Komande u nastavku pokreću se iz direktorija ovog uzorka.
Primjer koristi port **18081** kako bi se izbjegao sukob s drugim uzorkom; zadani ostaje 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP endpoint je `http://localhost:18081/mcp`. Informacije o zdravlju i otkrivanju su na
`http://localhost:18081/health` i `http://localhost:18081/info`.
Streamable HTTP zamjenjuje stari transport samo s SSE; `/sse` i `/v1/tools` nisu endpointi.

### Korak 2: Testirajte s Direktnim Klijentom

U drugom PowerShell terminalu:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Nije potreban unos. Isprobavaju se svih devet alata. Očekivani aritmetički rezultati uključuju
8, 6, 42, 5, 256, 4, 2 i 5.5, a zatim i tekst pomoći.

### Korak 3: Testirajte s AI Klijentom

Nakon autentikacije kako je opisano u preduvjetima, konfigurirajte AI klijent u istom terminalu:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Očekujte redak `Tool executed: add` s `41.80`, a zatim modelov odgovor.
Jednomodni mod izlazi bez čekanja na unos. Za pokretanje izvornog demonstracijskog primjera sa četiri upita:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo poziva `add`, `squareRoot`, `help` i lančane `power` pa `divide` operacije.
Očekivani brojčani odgovori su 41.8, 12 i 64. Izostavljanje argumenata također pokreće ovaj demo.

### Korak 4: Pokrenite Interaktivni Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Unesite `Pomnoži 6 sa 7 koristeći uslugu kalkulatora`, zatim `exit` ili `quit`.
Očekujte stvarni rezultat `multiply` alata od 42. Prazni redovi se zanemaruju; EOF također završava sesiju.
Za neinteraktivni smoke test ove ulazne točke:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Oba AI ulaza prihvaćaju `--prompt "question"`, `--demo` i `--interactive`.
Nevažeće opcije izazivaju neuspjeh prije otvaranja veze. Svaki Maven `-D...` argument je potpuno naveden
za PowerShell. Na Bashu koristite `export NAME=value` umjesto `$env:NAME = "value"`.

**Kota:** Pokrenite AI primjere sekvencijalno. Jednostavan upit obično treba dva zahtjeva modelu;
potpuni demo obično treba devet, uključujući naknadne pozive alatu. S dijeljenim 10 RPM
implementacijom, pričekajte novo vremensko razdoblje prije sljedećeg AI pokretanja. 429 se vidi kao greška bez
automatskih ponovnih pokušaja; slijedite upute servisa o retry-after. Stvarni broj zahtjeva ovisi o modelu.
Offline testovi ne troše nikakvu kvotu i ne utvrđuju dostupnost Lun-e uživo niti kvalitetu odgovora.

### Konfiguracija i gašenje

| Postavka | Zadano / ponašanje |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; osnovni URL, bez `/mcp` |
| `-Dmcp.server.url=...` | Nadjačava `MCP_SERVER_URL` za sve klijente |
| `AZURE_OPENAI_ENDPOINT` | Potrebno samo za AI klijente; resursni URL ili `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; naziv Azure implementacije |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; pozitivan cijeli broj |
| Napor zaključivanja | Uvijek `none`, uključujući naknadne pozive petlji alata |

Nadjačana implementacija mora podržavati `reasoning_effort=none` i `max_completion_tokens`.
Klijenti ne čitaju automatski `.env` datoteku. Zaustavite server s `Ctrl+C` nakon testiranja.
Klijenti normalno izlaze bez `System.exit` ili pauza za gašenje.

## Offline testovi

```powershell
mvn -B -ntp clean verify
```

Svi testovi su offline u odnosu na Azure: protokol pokreće Spring server i
stub kompatibilan s OpenAI na slučajnim loopback portovima, zatim ih zatvara. Maven može i dalje trebati
preuzeti ovisnosti. Nije potrebna autentikacija, živa implementacija niti postojeći MCP server.

- Jedinični testovi kalkulatora pokrivaju sve aritmetičke operacije, decimalne rezultate, pomoć i domenske pogreške.
- MCP testovi pokrivaju inicijalizaciju, otkrivanje, svih devet poziva alata, neuspjehe alata te zdravlje/info.
- AI protokol testovi izvode cijeli demo i interaktivni Bot protiv stvarnog kalkulatora,
  provjeravaju da rezultati alata hrane sljedeći završetak, te ispitaju svako HTTP tijelo za Lunu,
  `reasoning_effort: "none"` i `max_completion_tokens` bez zastarjelog `max_tokens`.
- Testovi konfiguracije/unosa pokrivaju nadjačavanja implementacije i endpointa, prazne linije, EOF, izlaz/quit,
  jednokratni mod, nevažeće opcije i propagaciju pogrešaka. Testovi kvota dokazuju da 429 nije ponovno pokušavan.

## Kako sve to funkcionira zajedno

Evo cjelokupnog toka kada pitate AI "Koliko je 5 + 3?":

1. **Vi** postavljate AI pitanje na prirodnom jeziku
2. **AI** analizira vaš zahtjev i shvaća da želite zbrajanje
3. **AI** poziva MCP server: `add(5.0, 3.0)`
4. **Usluga kalkulatora** izvršava: `5.0 + 3.0 = 8.0`
5. **Usluga kalkulatora** vraća: `"5.00 + 3.00 = 8.00"`
6. **AI** prima rezultat i formira prirodan odgovor
7. **Vi** dobijete: "Zbroj od 5 i 3 je 8"

## Sljedeći koraci

Za više primjera, pogledajte [Poglavlje 04: Praktični primjeri](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati greške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->