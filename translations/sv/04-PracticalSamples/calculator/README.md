# MCP-kalkylatorhandledning för nybörjare

## Innehållsförteckning

- [Vad du kommer att lära dig](#vad-du-kommer-att-lära-dig)
- [Förutsättningar](#förutsättningar)
- [Versionsberoenden](#versionsberoenden)
- [Förstå projektstrukturen](#förstå-projektstrukturen)
- [Förklaring av kärnkomponenter](#förklaring-av-kärnkomponenter)
  - [1. Huvudapplikation](#1-huvudapplikation)
  - [2. Kalkylatortjänst](#2-kalkylatortjänst)
  - [3. Direkt MCP-klient](#3-direkt-mcp-klient)
  - [4. AI-driven klient](#4-ai-driven-klient)
- [Köra exemplen](#köra-exemplen)
- [Offline-tester](#offline-tester)
- [Hur allt fungerar tillsammans](#hur-allt-fungerar-tillsammans)
- [Nästa steg](#nästa-steg)

## Vad du kommer att lära dig

Den här handledningen förklarar hur man bygger en kalkylatortjänst med Model Context Protocol (MCP). Du kommer att förstå:

- Hur man skapar en tjänst som AI kan använda som ett verktyg
- Hur man ställer in direkt kommunikation med MCP-tjänster
- Hur AI-modeller automatiskt kan välja vilka verktyg som ska användas
- Skillnaden mellan direkta protokollanrop och AI-assisterade interaktioner

## Förutsättningar

Innan du börjar, säkerställ att du har:
- Java 21 eller högre installerat
- Maven för beroendehantering
- Grundläggande förståelse för Java och Spring Boot

Endast AI-klienterna kräver en Azure OpenAI-distribution och en autentiserad `DefaultAzureCredential`,
till exempel en befintlig Azure CLI-inloggning lokalt eller en hanterad identitet i Azure. Identiteten behöver
rollen Cognitive Services OpenAI User på resursen. Se [Kapitel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Servern, direkt SDK-klient och alla automatiska tester kräver inget Azure-konto eller modellåtkomst.

## Versionsberoenden

Releasedependenser verifierade 2026-09-14:

| Beroende | Version |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (hanterad av Spring AI) | 2.0.0 |
| LangChain4j / kärna | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j officiell OpenAI-adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-hanterad) | 6.0.3 |

MCP- och officiella OpenAI-adaptrar är publicerade beta-releaser i Maven Central, inte snapshots.
Deras versioner skiljer sig från LangChain4j-kärnan. Inga snapshot- eller milstolpsrepository krävs.
Klientberoenden har testscope eftersom körbara exempel ligger under `src/test/java`.

## Förstå projektstrukturen

Kalkylatorprojektet har flera viktiga filer:

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

## Förklaring av kärnkomponenter

### 1. Huvudapplikation

**Fil:** `McpServerApplication.java`

Detta är inträdespunkten för vår kalkylatortjänst. Det är en standard Spring Boot-applikation med en speciell tillägg:

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

**Vad detta gör:**
- Startar en Spring Boot-webbserver på port 8080
- Skapar en `ToolCallbackProvider` som gör våra kalkylatormetoder tillgängliga som MCP-verktyg
- `@Bean`-annoteringen talar om för Spring att hantera detta som en komponent som andra delar kan använda

### 2. Kalkylatortjänst

**Fil:** `CalculatorService.java`

Här sker all matematik. Varje metod är märkt med `@Tool` för att göra den tillgänglig via MCP:

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
    
    // Fler kalkylatoroperationer...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Nyckelfunktioner:**

1. **`@Tool`-annotering**: Detta talar om för MCP att den här metoden kan anropas av externa klienter
2. **Tydliga beskrivningar**: Varje verktyg har en beskrivning som hjälper AI-modeller att förstå när det ska användas
3. **Konsekvent returformat**: Alla operationer returnerar människoläsbara strängar som "5.00 + 3.00 = 8.00"
4. **Felhanteirng**: Division med noll och negativa kvadratrötter returnerar felmeddelanden

**Tillgängliga operationer:**
- `add(a, b)` - Lägger till två siffror
- `subtract(a, b)` - Subtraherar andra från första
- `multiply(a, b)` - Multiplicerar två siffror
- `divide(a, b)` - Dividerar första med andra (med noll-kontroll)
- `power(base, exponent)` - Höjer basen till exponentens potens
- `squareRoot(number)` - Beräknar kvadratroten (med kontroll för negativa tal)
- `modulus(a, b)` - Returnerar resten vid division
- `absolute(number)` - Returnerar absolutvärdet
- `help()` - Returnerar information om alla operationer

### 3. Direkt MCP-klient

Se [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Denna klient använder `HttpClientStreamableHttpTransport` på `/mcp`, initierar kopplingen,
pingar servern och följer pagineringen av verktygslista. Den kontrollerar att alla nio förväntade verktyg
finns och anropar var och en av dem, inklusive `modulus` och `help`, utan en AI-modell.

Den nuvarande begäransbyggaren ser ut så här:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokollfel gör att klienten misslyckas i stället för att visa en vilseledande framgång. MCP-klienten
stängs med try-with-resources, även när upptäckt eller verktygsanrop misslyckas.

### 4. AI-driven klient

Se [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
och [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementerar nuvarande LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` kopplar den till samma `/mcp`-endpoint som SDK-klienten.
`AiServices` upptäcker verktygen och hanterar samtalet mellan verktygsanrop/resultat.

Standard-distributionen är **GPT-5.6 Luna**, med resonerande uttryckligen avstängt:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Dessa standardinställningar gäller för varje fullbordan, inklusive uppföljningar efter verktygskörning.
Klienten använder ett uppdateringsbart `BearerTokenCredential` stödd av `DefaultAzureCredential`
och scopet `https://ai.azure.com/.default`, inte en engångstoken som skickas som en API-nyckel.
Resurs-URL:er och URL:er som redan slutar med `/openai/v1` accepteras båda.

Botten håller en begränsad konversationshistorik, skriver ut `Tool executed: ...` med det faktiska
MCP-resultatet och misslyckas om ett svar hoppar över verktyg. Verktygsloppen är begränsade till fyra rundresor.
Autentisering, modell, MCP- och verktygsfel fortplantas; automatiska modellretry är avstängda.
Både MCP-transport/klient och den officiella OpenAI-klienten stängs vid framgång eller fel.

## Köra exemplen

### Steg 1: Starta kalkylatorservern

Ingen Azure-konfiguration behövs för servern. Kommandon nedan körs från detta exempels katalog.
Exemplet använder port **18081** för att undvika konflikt med ett annat exempel; standard är 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP-endpoint är `http://localhost:18081/mcp`. Hälsokontroll och upptäcktsinformation finns på
`http://localhost:18081/health` och `http://localhost:18081/info`.
Streamable HTTP ersätter den gamla enda SSE-transporten; `/sse` och `/v1/tools` är inte endpoints.

### Steg 2: Testa med direkt klient

I en annan PowerShell-terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Ingen inmatning behövs. Alla nio verktyg testas. Förväntade aritmetiska resultat inkluderar
8, 6, 42, 5, 256, 4, 2 och 5,5, följt av hjälptxten.

### Steg 3: Testa med AI-klient

Efter att ha autentiserat som beskrivs i förutsättningarna, konfigurera AI-klienten i samma terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Förvänta dig en rad `Tool executed: add` med `41.80`, följt av modellens svar.
Enkel prompt-läge avslutas utan att vänta på inmatning. För att köra det ursprungliga fyrpromptdemot:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demon anropar `add`, `squareRoot`, `help` och sedan den kedjade `power` och `divide`-operationen.
Förväntade numeriska svar är 41.8, 12 och 64. Att utelämna argument kör också detta demo.

### Steg 4: Kör den interaktiva boten

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Ange `Multiply 6 by 7 using the calculator service`, sedan `exit` eller `quit`.
Förvänta dig ett faktiskt `multiply` verktygsresultat av 42. Tomma rader ignoreras; EOF avslutar också sessionen.
För ett icke-interaktivt smygtest av denna inträdespunkt:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Båda AI-inträdespunkterna accepterar `--prompt "question"`, `--demo` och `--interactive`.
Ogiltiga alternativ leder till fel innan en koppling öppnas. Varje Maven `-D...`-argument är helt citerat
för PowerShell. På Bash, använd `export NAME=value` istället för `$env:NAME = "value"`.

**Kvot:** Kör AI-exempel i följd. En enkel prompt behöver normalt två modellförfrågningar;
det fullständiga demon behöver normalt nio, inklusive uppföljningar efter verktygsresultat. Med en delad 10 RPM
distribution, vänta på ett nytt kvotfönster innan nästa AI-körning. En 429-felkod misslyckas tydligt utan
automatiska retries; följ tjänstens retry-after-riktlinjer. Faktiska förfrågningsantal beror på modellen.
Offline-tester förbrukar ingen kvot och etablerar inte live Luna-tillgänglighet eller svarskvalitet.

### Konfiguration och avstängning

| Inställning | Standard / beteende |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; grund-URL, utan `/mcp` |
| `-Dmcp.server.url=...` | Åsidosätter `MCP_SERVER_URL` för alla klienter |
| `AZURE_OPENAI_ENDPOINT` | Krävs endast för AI-klienter; resurs-URL eller `/openai/v1`-URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; ett Azure-deploynamn |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positivt heltal |
| Ansträngning i resonemang | Alltid `none`, även för verktygsloppsuppföljningar |

En åsidosatt distribution måste stödja `reasoning_effort=none` och `max_completion_tokens`.
Klienterna läser inte automatiskt en `.env`-fil. Stoppa servern med `Ctrl+C` efter tester.
Klienter returnerar normalt utan `System.exit` eller avstängningsfördröjningar.

## Offline-tester

```powershell
mvn -B -ntp clean verify
```

Alla tester är offline med avseende på Azure: protokollsviten startar en Spring-server och
en OpenAI-kompatibel stub på slumpmässiga loopback-portar, och stänger sedan dem. Maven kan fortfarande behöva
ladda ned beroenden. Inga autentiseringsuppgifter, live-distribution eller förhandsinstallerad MCP-server används.

- Kalkylatortester omfattar alla aritmetiska operationer, decimala resultat, hjälp och domänfel.
- MCP-tester omfattar initiering, upptäckt, alla nio verktygsanrop, verktygsfel samt hälsa/info.
- AI-protokolltester kör fullständigt demo och interaktiv bot mot den riktiga kalkylatorn,
  verifierar att verktygsresultat matar den nästa fullbordningen och inspekterar varje HTTP-body för Luna,
  `reasoning_effort: "none"`, och `max_completion_tokens` utan legacy `max_tokens`.
- Konfigurations-/inmatningstester täcker distribution och endpoint-överskrivningar, tomma rader, EOF, exit/quit,
  enkel prompt-läge, ogiltiga alternativ och felhantering. Kvottester visar att 429 inte retryas.

## Hur allt fungerar tillsammans

Här är den kompletta flödet när du frågar AI: "Vad är 5 + 3?":

1. **Du** frågar AI på naturligt språk
2. **AI** analyserar din förfrågan och förstår att du vill göra en addition
3. **AI** anropar MCP-servern: `add(5.0, 3.0)`
4. **Kalkylatortjänsten** utför: `5.0 + 3.0 = 8.0`
5. **Kalkylatortjänsten** returnerar: `"5.00 + 3.00 = 8.00"`
6. **AI** tar emot resultatet och formulerar ett naturligt svar
7. **Du** får: "Summan av 5 och 3 är 8"

## Nästa steg

För fler exempel, se [Kapitel 04: Praktiska exempel](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->