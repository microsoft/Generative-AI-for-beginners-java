# MCP Kalkulačka Návod pre Začiatočníkov

## Obsah

- [Čo sa Naučíte](#čo-sa-naučíte)
- [Predpoklady](#predpoklady)
- [Verzie Závislostí](#verzie-závislostí)
- [Pochopenie Štruktúry Projektu](#pochopenie-štruktúry-projektu)
- [Vysvetlenie Hlavných Komponentov](#vysvetlenie-hlavných-komponentov)
  - [1. Hlavná Aplikácia](#1-hlavná-aplikácia)
  - [2. Kalkulačný Servis](#2-kalkulačný-servis)
  - [3. Priamy MCP Klient](#3-priamy-mcp-klient)
  - [4. AI-Poháňaný Klient](#4-ai-poháňaný-klient)
- [Spustenie Príkladov](#spustenie-príkladov)
- [Offline Testy](#offline-testy)
- [Ako To Všetko Spolu Funguje](#ako-to-všetko-spolu-funguje)
- [Ďalšie Kroky](#ďalšie-kroky)

## Čo sa Naučíte

Tento návod vysvetľuje, ako vybudovať kalkulačný servis pomocou Model Context Protocol (MCP). Porozumiete:

- Ako vytvoriť servis, ktorý môže AI používať ako nástroj
- Ako nastaviť priamu komunikáciu so službami MCP
- Ako môžu AI modely automaticky vybrať, ktoré nástroje používať
- Rozdiel medzi priamymi protokolárnymi volaniami a AI-podporovanými interakciami

## Predpoklady

Pred začatím sa uistite, že máte:
- Nainštalovanú Javu 21 alebo novšiu
- Maven na správu závislostí
- Základné znalosti Javy a Spring Boot

Iba AI klienti vyžadujú Azure OpenAI nasadenie a autentifikovaný `DefaultAzureCredential`,
ako napríklad existujúce lokálne prihlásenie v Azure CLI alebo spravovanú identitu v Azure. Táto identita potrebuje
rolu používateľa Cognitive Services OpenAI na zdroji. Pozri [Kapitolu 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Server, priamy SDK klient a všetky automatizované testy nepotrebujú Azure účet ani prístup k modelu.

## Verzie Závislostí

Overené verzie závislostí k dátumu 2026-09-14:

| Závislosť | Verzia |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (spravované Spring AI) | 2.0.0 |
| LangChain4j / jadro | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j oficiálny OpenAI adaptér | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (spravovaný Boot) | 6.0.3 |

MCP a oficiálne OpenAI adaptéry sú publikované beta verzie v Maven Centrálne, nie snapshoty.
Ich verzie sa líšia od LangChain4j jadra. Nie sú potrebné snapshot alebo milestone repozitáre.
Klientské závislosti majú testovací rozsah, pretože spustiteľné príklady sú pod `src/test/java`.

## Pochopenie Štruktúry Projektu

Projekt kalkulačky má niekoľko dôležitých súborov:

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

## Vysvetlenie Hlavných Komponentov

### 1. Hlavná Aplikácia

**Súbor:** `McpServerApplication.java`

Toto je vstupný bod nášho kalkulačného servisu. Je to štandardná Spring Boot aplikácia so špeciálnym doplnkom:

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

**Čo to robí:**
- Spustí Spring Boot webový server na porte 8080
- Vytvorí `ToolCallbackProvider`, ktorý sprístupní naše kalkulačné metódy ako MCP nástroje
- Anotácia `@Bean` hovorí Springu, aby to spravoval ako komponent, ktorý môžu používať iné časti

### 2. Kalkulačný Servis

**Súbor:** `CalculatorService.java`

Tu sa deje všetka matematika. Každá metóda je označená `@Tool`, aby bola dostupná cez MCP:

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
    
    // Viac operácií kalkulačky...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Kľúčové vlastnosti:**

1. **Anotácia `@Tool`**: Toto hovorí MCP, že túto metódu môžu volať externí klienti
2. **Jasné Popisy**: Každý nástroj má popis, ktorý pomáha AI modelom pochopiť, kedy ho použiť
3. **Konzistentný Formát Návratu**: Všetky operácie vracajú ľahko čitateľné reťazce ako "5.00 + 3.00 = 8.00"
4. **Ošetrenie Chýb**: Delenie nulou a záporné odmocniny vracajú chybové správy

**Dostupné Operácie:**
- `add(a, b)` - Sčítanie dvoch čísel
- `subtract(a, b)` - Odčítanie druhého od prvého
- `multiply(a, b)` - Násobenie dvoch čísel
- `divide(a, b)` - Delenie prvého druhým (s kontrolou na nulu)
- `power(base, exponent)` - Mocnenie základu na exponent
- `squareRoot(number)` - Výpočet druhé odmocniny (s kontrolou záporných hodnôt)
- `modulus(a, b)` - Zostáva z delenia
- `absolute(number)` - Absolútna hodnota
- `help()` - Informácie o všetkých operáciách

### 3. Priamy MCP Klient

Pozri [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Tento klient používa `HttpClientStreamableHttpTransport` na `/mcp`, inicializuje spojenie,
pingne server a sleduje stránkovanie zoznamu nástrojov. Kontroluje, či existuje všetkých deväť očakávaných nástrojov
a volá každý z nich, vrátane `modulus` a `help`, bez AI modelu.

Aktuálny tvorca požiadaviek vyzerá takto:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokolové chyby spôsobia zlyhanie klienta namiesto vypísania mätúceho úspechu. MCP klient
sa zatvára pomocou try-with-resources, vrátane prípadov, keď objavovanie alebo volanie nástroja zlyhá.

### 4. AI-Poháňaný Klient

Pozri [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
a [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementuje aktuálne API LangChain4j `ChatModel`.
`StreamableHttpMcpTransport` ho pripája k rovnakému `/mcp` endpointu ako SDK klient.
`AiServices` objavuje nástroje a spravuje rozhovor o volaní nástroja/výsledku.

Predvolené nasadenie je **GPT-5.6 Luna**, s explixitne zakázaným uvažovaním:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Tieto predvolené hodnoty platia pre každé dokončenie, vrátane následných požiadaviek po vykonaní nástroja.
Klient používa obnoviteľný `BearerTokenCredential` podporovaný `DefaultAzureCredential`
a rozsah `https://ai.azure.com/.default`, nie jednorazový token odovzdaný ako API kľúč.
Akceptujú sa URL zdrojov a URL, ktoré už končia na `/openai/v1`.

Bot udržiava obmedzenú históriu konverzácie, vypisuje `Tool executed: ...` s reálnym
MCP výsledkom a zlyhá, ak odpoveď vynecháva nástroje. Nástrojové slučky sú limitované na štyri kolá.
Autentifikácia, model, MCP a chyby nástrojov sa propagujú; automatické opakovanie modelu je vypnuté.
MCP transport/klient a oficiálny OpenAI klient sa zatvárajú pri úspechu alebo zlyhaní.

## Spustenie Príkladov

### Krok 1: Spustite Kalkulačný Server

Pre server nie je potrebná žiadna konfigurácia Azure. Príkazy nižšie sa spúšťajú z adresára tohto príkladu.
Príklad používa port **18081**, aby sa predišlo konfliktu s iným príkladom; predvolený port zostáva 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP endpoint je `http://localhost:18081/mcp`. Informácie o zdraví a objavovaní sú na
`http://localhost:18081/health` a `http://localhost:18081/info`.
Streamable HTTP nahrádza starý transport založený len na SSE; `/sse` a `/v1/tools` nie sú endpointy.

### Krok 2: Testovanie s Priamym Klientom

V inom PowerShell termináli:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Nie je potrebný žiaden vstup. Všetkých deväť nástrojov sa použije. Očakávané aritmetické výsledky zahŕňajú
8, 6, 42, 5, 256, 4, 2 a 5.5, nasledované pomocným textom.

### Krok 3: Testovanie s AI Klientom

Po autentifikácii podľa pokynov v predpokladoch nakonfigurujte AI klienta v rovnakom termináli:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Očakávajte riadok `Tool executed: add` s hodnotou `41.80`, nasledovaný odpoveďou modelu.
Režim s jedným promptom končí bez čakania na vstup. Ak chcete spustiť pôvodnú demo so štyrmi promptmi:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo volá `add`, `squareRoot`, `help` a reťazenú operáciu `power` a potom `divide`.
Očakávané číselné odpovede sú 41.8, 12 a 64. Vynechanie argumentov tiež spustí toto demo.

### Krok 4: Spustite Interaktívneho Bota

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Zadajte `Vynásob 6 krát 7 pomocou kalkulačného servisu`, potom `exit` alebo `quit`.
Očakávajte skutočný výsledok nástroja `multiply` rovný 42. Prázdne riadky sú ignorované; EOF tiež ukončí reláciu.
Pre neinteraktívny smoke test tohto vstupného bodu:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Oba AI vstupné body akceptujú `--prompt "question"`, `--demo` a `--interactive`.
Neplatné možnosti zlyhajú pred otvorením spojenia. Každý Maven `-D...` argument je plne uvedený v úvodzovkách
pre PowerShell. Na Bash používajte `export NAME=value` namiesto `$env:NAME = "value"`.

**Kvóta:** Spúšťajte AI príklady jeden po druhom. Jednoduchý prompt zvyčajne potrebuje dve požiadavky na model;
kompletné demo zvyčajne deväť, vrátane následných správ po vykonaní nástrojov. Pri zdieľanom 10 RPM
nasadení počkajte na obnovenie kvóty pred ďalším spustením AI. Chyba 429 zlyhá viditeľne bez
automatických opakovaní; riaďte sa odporúčaniami služby na retry-after. Skutočný počet požiadaviek závisí od modelu.
Offline testy neprekračujú žiadnu kvótu a neoverujú živú dostupnosť ani kvalitu odpovedí Luna.

### Konfigurácia a Ukončenie

| Nastavenie | Predvolené / správanie |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; základná URL, bez `/mcp` |
| `-Dmcp.server.url=...` | Prepisuje `MCP_SERVER_URL` pre všetkých klientov |
| `AZURE_OPENAI_ENDPOINT` | Vyžaduje sa iba pre AI klientov; URL zdroja alebo `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; názov Azure nasadenia |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; kladné celé číslo |
| Úsilie o uvažovanie | Vždy `none`, vrátane následných správ po cykloch nástroja |

Prepisané nasadenie musí podporovať `reasoning_effort=none` a `max_completion_tokens`.
Klienti automaticky nečítajú `.env` súbor. Server zastavte pomocou `Ctrl+C` po testovaní.
Klienti končia normálne, bez `System.exit` alebo uspávania pri ukončení.

## Offline Testy

```powershell
mvn -B -ntp clean verify
```

Všetky testy sú offline vo vzťahu k Azure: protokolová sada spustí Spring server a
OpenAI-kompatibilný stub na náhodných loopback portoch, potom ich zatvorí. Maven môže stále potrebovať
stiahnuť závislosti. Nepoužívajú sa žiadne poverenia, živé nasadenie ani existujúci MCP server.

- Jednotkové testy kalkulačky pokrývajú všetky aritmetické operácie, desatinné výsledky, pomoc a doménové chyby.
- MCP testy pokrývajú inicializáciu, objavovanie, všetkých deväť volaní nástrojov, chyby nástrojov, a zdravie/info.
- AI protokolové testy vykonávajú kompletné demo a interaktívneho Bota na reálnej kalkulačke,
  overujú, že výsledky nástrojov sa prenášajú do ďalších dokončení, a kontrolujú každé HTTP telo pre Luna,
  `reasoning_effort: "none"` a `max_completion_tokens` bez zastaralého `max_tokens`.
- Testy konfigurácie/vstupu pokrývajú prepísania nasadenia a endpointov, prázdne riadky, EOF, exit/quit,
  režim s jedným promptom, neplatné možnosti a propagáciu chýb. Kvótové testy dokazujú, že 429 sa neopakujú.

## Ako To Všetko Spolu Funguje

Tu je kompletný tok, keď sa AI opýtate "Koľko je 5 + 3?":

1. **Vy** ste AI položili otázku v prirodzenom jazyku
2. **AI** analyzuje vašu požiadavku a zistí, že chcete sčítanie
3. **AI** zavolá MCP server: `add(5.0, 3.0)`
4. **Kalkulačný Servis** vykoná: `5.0 + 3.0 = 8.0`
5. **Kalkulačný Servis** vráti: `"5.00 + 3.00 = 8.00"`
6. **AI** príjme výsledok a vytvorí prirodzenú odpoveď
7. **Vy** dostanete: "Súčet 5 a 3 je 8"

## Ďalšie Kroky

Pre viac príkladov pozri [Kapitolu 04: Praktické príklady](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->