# MCP Lommeregner Vejledning for Begyndere

## Indholdsfortegnelse

- [Hvad Du Vil Lære](#hvad-du-vil-lære)
- [Forudsætninger](#forudsætninger)
- [Afhængighedsversioner](#afhængighedsversioner)
- [Forståelse af Projektstrukturen](#forståelse-af-projektstrukturen)
- [Kernekomponenter Forklaret](#kernekomponenter-forklaret)
  - [1. Hovedapplikationen](#1-hovedapplikationen)
  - [2. Lommeregner Service](#2-lommeregner-service)
  - [3. Direkte MCP Klient](#3-direkte-mcp-klient)
  - [4. AI-drevet Klient](#4-ai-drevet-klient)
- [Kørsel af Eksemplerne](#kørsel-af-eksemplerne)
- [Offline Tests](#offline-tests)
- [Sådan Arbejder Det Hele Sammen](#sådan-arbejder-det-hele-sammen)
- [Næste Skridt](#næste-skridt)

## Hvad Du Vil Lære

Denne vejledning forklarer, hvordan man bygger en lommeregner service ved hjælp af Model Context Protocol (MCP). Du vil forstå:

- Hvordan man opretter en service, som AI kan bruge som et værktøj
- Hvordan man opsætter direkte kommunikation med MCP-services
- Hvordan AI-modeller automatisk kan vælge, hvilke værktøjer der skal bruges
- Forskellen mellem direkte protokolopkald og AI-assisterede interaktioner

## Forudsætninger

Før du begynder, skal du sikre dig, at du har:
- Java 21 eller højere installeret
- Maven til håndtering af afhængigheder
- Grundlæggende forståelse af Java og Spring Boot

Kun AI-klienterne kræver en Azure OpenAI-udrulning og en autentificeret `DefaultAzureCredential`,
som for eksempel en eksisterende Azure CLI login lokalt eller en managed identity i Azure. Identiteten skal
have Cognitive Services OpenAI brugerrrollen på ressourcen. Se [Kapitel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Serveren, direkte SDK-klient og alle automatiserede tests behøver ikke Azure konto eller modeladgang.

## Afhængighedsversioner

Udgivelsesafhængigheder verificeret den 14-09-2026:

| Afhængighed | Version |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-styret) | 2.0.0 |
| LangChain4j / kerne | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j officiel OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-styret) | 6.0.3 |

MCP og de officielle OpenAI-adaptere er udgivne beta-versioner på Maven Central, ikke snapshots.
Deres versioner adskiller sig fra LangChain4j kerne. Intet snapshot- eller milestone-arkiv er nødvendigt.
Klient-only afhængigheder har testscope, fordi kørbare eksempler ligger under `src/test/java`.

## Forståelse af Projektstrukturen

Lommeregnerprojektet har flere vigtige filer:

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

## Kernekomponenter Forklaret

### 1. Hovedapplikationen

**Fil:** `McpServerApplication.java`

Dette er indgangspunktet for vores lommeregner service. Det er en standard Spring Boot-applikation med én særlig tilføjelse:

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

**Dette gør:**
- Starter en Spring Boot webserver på port 8080
- Opretter en `ToolCallbackProvider` som gør vores lommeregnerfunktioner tilgængelige som MCP-værktøjer
- `@Bean`-annotationen fortæller Spring at håndtere dette som en komponent, som andre dele kan bruge

### 2. Lommeregner Service

**Fil:** `CalculatorService.java`

Her foregår al matematikken. Hver metode er markeret med `@Tool` for at gøre den tilgængelig via MCP:

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
    
    // Flere regnearksfunktioner...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Nøglefunktioner:**

1. **`@Tool`-annotation**: Fortæller MCP, at denne metode kan kaldes af eksterne klienter
2. **Klar Beskrivelse**: Hvert værktøj har en beskrivelse, der hjælper AI-modeller med at forstå, hvornår det skal bruges
3. **Konsistent Returformat**: Alle opgaver returnerer menneskeligt læsbare strenge som "5.00 + 3.00 = 8.00"
4. **Fejlhåndtering**: Division med nul og negative kvadratrødder returnerer fejlbeskeder

**Tilgængelige Operationer:**
- `add(a, b)` - Lægger to tal sammen
- `subtract(a, b)` - Trækker det andet tal fra det første
- `multiply(a, b)` - Ganger to tal
- `divide(a, b)` - Dividerer første tal med andet (med nul-tjek)
- `power(base, exponent)` - Ganger base op til eksponenten
- `squareRoot(number)` - Beregner kvadratroden (med negativ-tjek)
- `modulus(a, b)` - Returnerer resten ved division
- `absolute(number)` - Returnerer den absolutte værdi
- `help()` - Returnerer information om alle operationer

### 3. Direkte MCP Klient

Se [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Denne klient bruger `HttpClientStreamableHttpTransport` på `/mcp`, initialiserer forbindelsen,
pinger serveren, og følger værktøjslistens pagination. Den tjekker, at alle ni forventede værktøjer
findes og kalder hver af dem, inklusiv `modulus` og `help`, uden en AI-model.

Den aktuelle request builder ser således ud:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokolfejl fejler klienten i stedet for at printe en misvisende succes. MCP-klienten
lukkes med try-with-resources, også hvis discovery eller et værktøjskald mislykkes.

### 4. AI-drevet Klient

Se [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
og [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementerer den nuværende LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` forbinder den til den samme `/mcp` endpoint som SDK-klienten.
`AiServices` opdager værktøjerne og styrer værktøjskald/ -resultat samtalen.

Standardudrulningen er **GPT-5.6 Luna**, med ræsonnering eksplicit deaktiveret:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Disse standarder gælder for hver completion, inklusive opfølgninger efter værktøjsudførelse.
Klienten bruger en fornyelig `BearerTokenCredential` baseret på `DefaultAzureCredential`
og `https://ai.azure.com/.default` scope, ikke et engangstoken som API-nøgle.
Ressource-URL'er og URL'er, der allerede ender på `/openai/v1`, accepteres begge.

Botten holder en begrænset samtalehistorik, printer `Tool executed: ...` med det faktiske
MCP-resultat, og fejler, hvis et svar springer værktøjer over. Værktøjsloops begrænses til fire ture.
Autentificering, model-, MCP- og værktøjsfejl propagere; automatiske model-omforsøg er deaktiveret.
Både MCP-transport/klient og den officielle OpenAI-klient lukkes ved succes eller fejl.

## Kørsel af Eksemplerne

### Trin 1: Start Lommeregner Serveren

Ingen Azure-konfiguration er nødvendig for serveren. Kommandoerne nedenfor køres fra denne eksempelkatalog.
Eksemplet bruger port **18081** for at undgå sammenstød med et andet eksempel; standard er stadig 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP endpoint er `http://localhost:18081/mcp`. Sundheds- og discovery-information findes på
`http://localhost:18081/health` og `http://localhost:18081/info`.
Streamable HTTP erstatter den gamle SSE-only transport; `/sse` og `/v1/tools` er ikke endpoints.

### Trin 2: Test med Direkte Klient

I et andet PowerShell-terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Ingen input er nødvendig. Alle ni værktøjer testes. Forventede aritmetiske resultater inkluderer
8, 6, 42, 5, 256, 4, 2, og 5,5 efterfulgt af hjælpe teksten.

### Trin 3: Test med AI Klient

Efter autentificering som beskrevet i forudsætninger, konfigurer AI-klienten i samme terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Forvent en `Tool executed: add` linje med `41.80`, efterfulgt af modellens svar.
Single-prompt-mode afslutter uden at vente på input. For at køre originalt fire-prompt demo:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demoen kalder `add`, `squareRoot`, `help`, og den kædede `power` så `divide` operation.
Forventede numeriske svar er 41,8, 12, og 64. Udeladelse af argumenter kører også denne demo.

### Trin 4: Kør den Interaktive Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Indtast `Multiply 6 by 7 using the calculator service`, så `exit` eller `quit`.
Forvent et faktisk `multiply` værktøjsresultat på 42. Tomme linjer ignoreres; EOF afslutter også sessionen.
For en noninteractive smoke test af dette indgangspunkt:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Begge AI-indgange accepterer `--prompt "question"`, `--demo`, og `--interactive`.
Ugyldige valgmuligheder fejler før åbning af forbindelse. Hver Maven `-D...` argument er fuldt citeret
for PowerShell. På Bash brug `export NAME=value` i stedet for `$env:NAME = "value"`.

**Kvotering:** Kør AI-eksempler sekventielt. Et enkelt prompt kræver normalt to modelanmodninger;
hele demoen kræver normalt ni, inklusiv opfølgninger efter værktøjsresultater. Med en delt 10 RPM
udrulning, vent et ny kvoteflade før næste AI-kørsel. En 429 fejler synligt uden
automatiske omforsøg; følg tjenestens retry-after vejledning. Faktiske anmodningsantal afhænger af modellen.
Offline tests bruger ikke nogen kvote og etablerer ikke live Luna tilgængelighed eller svar kvalitet.

### Konfiguration og Nedlukning

| Indstilling | Standard / adfærd |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; base URL, uden `/mcp` |
| `-Dmcp.server.url=...` | Overskriver `MCP_SERVER_URL` for alle klienter |
| `AZURE_OPENAI_ENDPOINT` | Kræves kun for AI-klienter; ressourcelink eller `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; et Azure udrulningsnavn |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positivt heltal |
| Ræsonneringsindsats | Altid `none`, inklusive værktøjs-loop-opfølgninger |

En overskrevet udrulning skal understøtte `reasoning_effort=none` og `max_completion_tokens`.
Klienterne læser ikke en `.env` fil automatisk. Stop serveren med `Ctrl+C` efter test.
Klienter returnerer normalt uden `System.exit` eller nedlukningsforsinkelser.

## Offline Tests

```powershell
mvn -B -ntp clean verify
```

Alle tests er offline med hensyn til Azure: protokolsuiten starter en Spring-server og
en OpenAI-kompatibel stub på tilfældige loopback-porte, og lukker dem derefter. Maven kan stadig have brug for
at downloade afhængigheder. Ingen legitimationsoplysninger, live-udrulning eller eksisterende MCP-server benyttes.

- Lommeregnerens enhedstest dækker alle aritmetiske operationer, decimale resultater, hjælp og domænefejl.
- MCP-tests dækker initialisering, discovery, alle ni værktøjskald, fejlsituationer og sundheds/info-check.
- AI-protokoltests udfører hele demoen og interaktive Bot mod den ægte lommeregner,
  verificerer at værktøjsresultater fodrer næste completion, og inspicerer hver HTTP-body for Luna,
  `reasoning_effort: "none"`, og `max_completion_tokens` uden legacy `max_tokens`.
- Konfigurations-/input-tests dækker udrulning og endpoint-overskridelser, blanke linjer, EOF, exit/quit,
  single-prompt mode, ugyldige valgmuligheder, og fejlpropagering. Kvotatests beviser at 429 ikke omforsøges.

## Sådan Arbejder Det Hele Sammen

Her er det komplette flow når du spørger AI'en "Hvad er 5 + 3?":

1. **Du** spørger AI'en på naturligt sprog
2. **AI** analyserer din forespørgsel og forstår, at du ønsker addition
3. **AI** kalder MCP-serveren: `add(5.0, 3.0)`
4. **Lommeregner Service** udfører: `5.0 + 3.0 = 8.0`
5. **Lommeregner Service** returnerer: `"5.00 + 3.00 = 8.00"`
6. **AI** modtager resultatet og formaterer et naturligt svar
7. **Du** får: "Summen af 5 og 3 er 8"

## Næste Skridt

For flere eksempler, se [Kapitel 04: Praktiske eksempler](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->