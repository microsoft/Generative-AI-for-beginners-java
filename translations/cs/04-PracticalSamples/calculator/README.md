# MCP Kalkulační návod pro začátečníky

## Obsah

- [Co se naučíte](#co-se-naučíte)
- [Požadavky](#požadavky)
- [Verze závislostí](#verze-závislostí)
- [Porozumění struktuře projektu](#porozumění-struktuře-projektu)
- [Vysvětlení základních komponent](#vysvětlení-základních-komponent)
  - [1. Hlavní aplikace](#1-hlavní-aplikace)
  - [2. Kalkulační služba](#2-kalkulační-služba)
  - [3. Přímý MCP klient](#3-přímý-mcp-klient)
  - [4. Klient s AI](#4-klient-s-ai)
- [Spuštění příkladů](#spuštění-příkladů)
- [Offline testy](#offline-testy)
- [Jak to vše funguje dohromady](#jak-to-vše-funguje-dohromady)
- [Další kroky](#další-kroky)

## Co se naučíte

Tento návod vysvětluje, jak vytvořit kalkulační službu pomocí Model Context Protocol (MCP). Naučíte se:

- Jak vytvořit službu, kterou může AI používat jako nástroj
- Jak nastavit přímou komunikaci se službami MCP
- Jak mohou AI modely automaticky vybrat, které nástroje použít
- Rozdíl mezi přímými protokolovými voláními a interakcemi asistovanými AI

## Požadavky

Před začátkem se ujistěte, že máte:
- Nainstalovanou Javu 21 nebo vyšší
- Maven pro správu závislostí
- Základní znalost Javy a Spring Boot

Pouze AI klienti vyžadují Azure OpenAI nasazení a ověřený `DefaultAzureCredential`,
například přihlášení přes Azure CLI lokálně nebo spravovanou identitu v Azure. Identita musí mít
roli Cognitive Services OpenAI User na příslušném zdroji. Viz [Kapitola 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Server, přímý SDK klient a všechny automatizované testy žádný Azure účet ani přístup k modelu nevyžadují.

## Verze závislostí

Ověřené závislosti ke dni 2026-09-14:

| Závislost | Verze |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (spravovaný Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j oficiální OpenAI adaptér | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (spravovaný Bootem) | 6.0.3 |

MCP a oficiální OpenAI adaptéry jsou publikované beta verze v Maven Central, nikoli snapshoty.
Jejich verze se liší od LangChain4j core. Není potřeba žádné snapshot nebo milestone repozitáře.
Závislosti pouze pro klienty mají rozsah testování, protože spustitelné příklady jsou v `src/test/java`.

## Porozumění struktuře projektu

Projekt kalkulačky obsahuje několik důležitých souborů:

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

## Vysvětlení základních komponent

### 1. Hlavní aplikace

**Soubor:** `McpServerApplication.java`

Toto je vstupní bod naší kalkulační služby. Jedná se o standardní Spring Boot aplikaci se speciálním doplňkem:

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

**Co to dělá:**
- Spustí Spring Boot webový server na portu 8080
- Vytvoří `ToolCallbackProvider`, který zpřístupní naše kalkulační metody jako MCP nástroje
- Anotace `@Bean` říká Springu, aby to spravoval jako komponentu, kterou mohou používat ostatní části

### 2. Kalkulační služba

**Soubor:** `CalculatorService.java`

Zde probíhají veškeré matematické výpočty. Každá metoda je označena `@Tool`, aby byla dostupná přes MCP:

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
    
    // Další operace kalkulačky...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Klíčové vlastnosti:**

1. **Anotace `@Tool`**: Říká MCP, že tuto metodu mohou volat externí klienti
2. **Jasné popisy**: Každý nástroj má popis, který pomáhá AI modelům pochopit, kdy použít daný nástroj
3. **Konzistentní formát návratu**: Všechny operace vrací čitelné řetězce, například "5.00 + 3.00 = 8.00"
4. **Zpracování chyb**: Dělení nulou a odmocnina záporného čísla vrací chybová hlášení

**Dostupné operace:**
- `add(a, b)` - Sčítá dvě čísla
- `subtract(a, b)` - Odečítá druhé číslo od prvního
- `multiply(a, b)` - Násobí dvě čísla
- `divide(a, b)` - Dělí první číslo druhým (s kontrolou nuly)
- `power(base, exponent)` - Umocňuje základ na exponent
- `squareRoot(number)` - Vypočítá druhou odmocninu (kontrola záporného čísla)
- `modulus(a, b)` - Vrací zbytek po dělení
- `absolute(number)` - Vrací absolutní hodnotu
- `help()` - Vrací informace o všech operacích

### 3. Přímý MCP klient

Viz [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Tento klient používá `HttpClientStreamableHttpTransport` na `/mcp`, inicializuje spojení,
pingne server a zvládá stránkování seznamu nástrojů. Kontroluje, zda všech devět očekávaných nástrojů
existuje, a volá každý z nich, včetně `modulus` a `help`, bez AI modelu.

Současný stavěč požadavků vypadá takto:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Chyby protokolu zákazníka nezahodí, místo toho končí chybou místo falešného úspěchu. MCP klient
je uzavřen pomocí try-with-resources, i když selže objevování nebo volání nástroje.

### 4. Klient s AI

Viz [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
a [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementuje aktuální LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` ho propojuje se stejným `/mcp` endpointem jako SDK klienta.
`AiServices` objevuje nástroje a řídí konverzaci volání nástroje / výsledku.

Výchozí nasazení je **GPT-5.6 Luna**, s explicitním vypnutím uvažování:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Tyto výchozí hodnoty platí pro každé dokončení, včetně následných dotazů po provedení nástroje.
Klient používá obnovitelný `BearerTokenCredential`, podporovaný `DefaultAzureCredential`
a rozsahem `https://ai.azure.com/.default`, nikoli jednorázový token předaný jako API klíč.
Jsou akceptovány jak URL zdroje, tak URL již končící na `/openai/v1`.

Bot udržuje omezenou historii konverzace, tiskne `Tool executed: ...` s aktuálním
výsledkem MCP a ukončí se, pokud odpověď vynechá nástroje. Smyčky nástrojů jsou omezeny na čtyři kola.
Autentifikace, model, MCP a chyby nástrojů se propagují; automatické opakování modelu je vypnuto.
MCP transport/klient i oficiální OpenAI klient jsou uzavřeni při úspěchu i neúspěchu.

## Spuštění příkladů

### Krok 1: Spusťte server kalkulačky

Pro server není potřeba žádná konfigurace Azure. Níže uvedené příkazy běží z adresáře tohoto vzoru.
Příklad používá port **18081**, aby se vyhnul konfliktu s jiným příkladem; výchozí port zůstává 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP endpoint je `http://localhost:18081/mcp`. Stav zdraví a informace o objevování jsou na
`http://localhost:18081/health` a `http://localhost:18081/info`.
Streamable HTTP nahrazuje starý pouze SSE transport; `/sse` a `/v1/tools` nejsou endpointy.

### Krok 2: Otestujte s přímým klientem

V jiném PowerShell terminálu:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Není potřeba žádný vstup. Jsou vyzkoušeny všechny devět nástrojů. Očekávané aritmetické výsledky zahrnují
8, 6, 42, 5, 256, 4, 2 a 5,5, následované textem nápovědy.

### Krok 3: Otestujte s AI klientem

Po autentizaci podle požadavků nastavte AI klienta ve stejném terminálu:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Očekávejte řádek `Tool executed: add` s hodnotou `41.80`, následovaný odpovědí modelu.
Režim jednoho promptu skončí bez čekání na vstup. Pro spuštění původní demo verze se čtyřmi prompty:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo volá `add`, `squareRoot`, `help` a řetězenou operaci `power` a poté `divide`.
Očekávané číselné odpovědi jsou 41,8, 12 a 64. Vynechání argumentů také spustí toto demo.

### Krok 4: Spusťte interaktivního bota

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Zadejte `Násob 6 krát 7 pomocí kalkulační služby`, poté `exit` nebo `quit`.
Očekávejte skutečný výsledek nástroje `multiply` s hodnotou 42. Prázdné řádky jsou ignorovány; EOF také ukončí relaci.
Pro neinteraktivní smoke test tohoto vstupního bodu:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Oba AI vstupní body přijímají `--prompt "otázka"`, `--demo` a `--interactive`.
Neplatné volby selžou ještě před otevřením spojení. Každý Maven `-D...` argument je v PowerShellu plně v uvozovkách.
V Bashi použijte `export NAME=value` místo `$env:NAME = "value"`.

**Kvóta:** Spouštějte AI příklady postupně. Jednoduchý prompt obvykle potřebuje dvě žádosti modelu;
kompletní demo normálně potřebuje devět, včetně následných dotazů na výsledky nástrojů. U sdíleného 10 RPM
nasazení počkejte na novou kvótovou periodu před dalším během AI. 429 chyba se nezopakují automaticky a zobrazí chybu;
řiďte se pokyny služby k době čekání (retry-after). Počet skutečných požadavků závisí na modelu.
Offline testy nevyužívají kvótu ani nezjišťují dostupnost či kvalitu odpovědí Luna.

### Konfigurace a vypnutí

| Nastavení | Výchozí / chování |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; základní URL, bez `/mcp` |
| `-Dmcp.server.url=...` | Přepíše `MCP_SERVER_URL` ve všech klientech |
| `AZURE_OPENAI_ENDPOINT` | Vyžadováno pouze pro AI klienty; URL zdroje nebo `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; jméno Azure nasazení |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; kladné celé číslo |
| Úsilí o uvažování | Vždy `none`, včetně následných dotazů nástrojových smyček |

Přepsané nasazení musí podporovat `reasoning_effort=none` a `max_completion_tokens`.
Klienti automaticky nečtou `.env` soubor. Po testování zastavte server pomocí `Ctrl+C`.
Klienti běžně vrací návratovou hodnotu bez `System.exit` či zpoždění ukončení.

## Offline testy

```powershell
mvn -B -ntp clean verify
```

Všechny testy jsou offline vzhledem k Azure: protokol spustí Spring server a
OpenAI-kompatibilní stub na náhodných loopback portech, které pak zavře. Maven může stále potřebovat
stáhnout závislosti. Nejsou použity žádné přihlašovací údaje, živé nasazení ani existující MCP server.

- Jednotkové testy kalkulačky pokrývají všechny aritmetické operace, desetinné výsledky, nápovědu a doménové chyby.
- MCP testy zahrnují inicializaci, objevování, všech devět volání nástrojů, selhání nástrojů a stav zdraví/informace.
- AI protokol testy provádějí celé demo a interaktivního bota proti skutečné kalkulačce,
  ověřují, zda výsledky nástrojů krmí následující dokončení, a kontrolují každý HTTP obsah pro Luna,
  `reasoning_effort: "none"` a `max_completion_tokens` bez starého `max_tokens`.
- Konfigurační/vstupní testy ověřují přepnutí nasazení a endpointu, prázdné řádky, EOF, exit/quit,
  režim jednoho promptu, neplatné volby a propagaci chyb. Testy kvóty potvrzují, že chyba 429 není opakována.

## Jak to vše funguje dohromady

Zde je celý tok, když se zeptáte AI "Kolik je 5 + 3?":

1. **Vy** požádáte AI přirozeným jazykem
2. **AI** analyzuje váš požadavek a zjistí, že chcete sčítání
3. **AI** zavolá MCP server: `add(5.0, 3.0)`
4. **Kalkulační služba** provede: `5.0 + 3.0 = 8.0`
5. **Kalkulační služba** vrátí: `"5.00 + 3.00 = 8.00"`
6. **AI** přijme výsledek a vytvoří přirozenou odpověď
7. **Vy** dostanete: "Součet 5 a 3 je 8"

## Další kroky

Pro více příkladů viz [Kapitola 04: Praktické ukázky](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o co největší přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->