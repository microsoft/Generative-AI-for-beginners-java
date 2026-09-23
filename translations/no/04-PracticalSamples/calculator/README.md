# MCP Kalkulatorveiledning for nybegynnere

## Innholdsfortegnelse

- [Hva du vil lære](#hva-du-vil-lære)
- [Forutsetninger](#forutsetninger)
- [Avhengighetsversjoner](#avhengighetsversjoner)
- [Forstå prosjektstrukturen](#forstå-prosjektstrukturen)
- [Kjernekomponenter forklart](#kjernekomponenter-forklart)
  - [1. Hovedapplikasjonen](#1-hovedapplikasjonen)
  - [2. Kalkulatortjenesten](#2-kalkulatortjenesten)
  - [3. Direkte MCP-klient](#3-direkte-mcp-klient)
  - [4. AI-drevet klient](#4-ai-drevet-klient)
- [Kjøre eksemplene](#kjøre-eksemplene)
- [Offline tester](#offline-tester)
- [Hvordan alt fungerer sammen](#hvordan-alt-fungerer-sammen)
- [Neste steg](#neste-steg)

## Hva du vil lære

Denne veiledningen forklarer hvordan du bygger en kalkulatortjeneste ved bruk av Model Context Protocol (MCP). Du vil forstå:

- Hvordan lage en tjeneste som AI kan bruke som et verktøy
- Hvordan sette opp direkte kommunikasjon med MCP-tjenester
- Hvordan AI-modeller automatisk kan velge hvilke verktøy som skal brukes
- Forskjellen mellom direkte protokollkall og AI-assisterte interaksjoner

## Forutsetninger

Før du starter, sørg for at du har:
- Java 21 eller høyere installert
- Maven for avhengighetsstyring
- Grunnleggende forståelse av Java og Spring Boot

Kun AI-klientene krever en Azure OpenAI-distribusjon og en autentisert `DefaultAzureCredential`,
som for eksempel en eksisterende Azure CLI-pålogging lokalt eller en administrert identitet i Azure. Identiteten trenger
rollen Cognitive Services OpenAI User på ressursen. Se [Kapittel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Serveren, direkte SDK-klient, og alle automatiserte tester trenger ikke Azure-konto eller modelltilgang.

## Avhengighetsversjoner

Frigivelsesavhengigheter verifisert 2026-09-14:

| Avhengighet | Versjon |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-styrt) | 2.0.0 |
| LangChain4j / kjerne | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j offisiell OpenAI-adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-styrt) | 6.0.3 |

MCP og offisielle OpenAI-adaptere er publiserte beta-utgaver i Maven Central, ikke snapshots.
Deres versjoner avviker fra LangChain4j-kjernen. Ingen snapshot- eller milepæls-repositorier er nødvendig.
Klient-spesifikke avhengigheter har test-scope fordi kjørbare eksempler ligger under `src/test/java`.

## Forstå prosjektstrukturen

Kalkulatorprosjektet har flere viktige filer:

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

## Kjernekomponenter forklart

### 1. Hovedapplikasjonen

**Fil:** `McpServerApplication.java`

Dette er inngangspunktet for vår kalkulatortjeneste. Det er en standard Spring Boot-applikasjon med en spesiell tillegg:

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

**Hva dette gjør:**
- Starter en Spring Boot webserver på port 8080
- Lager en `ToolCallbackProvider` som gjør kalkulatormetodene våre tilgjengelige som MCP-verktøy
- `@Bean`-annotasjonen forteller Spring å administrere dette som en komponent som andre deler kan bruke

### 2. Kalkulatortjenesten

**Fil:** `CalculatorService.java`

Her skjer all matematikken. Hver metode er merket med `@Tool` for å gjøre den tilgjengelig via MCP:

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
    
    // Flere kalkulatoroperasjoner...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Nøkkelfunksjoner:**

1. **`@Tool`-annotasjon**: Dette forteller MCP at denne metoden kan kalles av eksterne klienter
2. **Klar beskrivelse**: Hvert verktøy har en beskrivelse som hjelper AI-modeller å forstå når det skal brukes
3. **Konsistent returformat**: Alle operasjoner returnerer menneskelesbare strenger som "5.00 + 3.00 = 8.00"
4. **Feilhåndtering**: Divisjon med null og negative kvadratrøtter gir feilmeldinger

**Tilgjengelige operasjoner:**
- `add(a, b)` - Legger sammen to tall
- `subtract(a, b)` - Trekker det andre fra det første
- `multiply(a, b)` - Multipliserer to tall
- `divide(a, b)` - Dividerer det første med det andre (med null-sjekk)
- `power(base, exponent)` - Hever base til eksponenten
- `squareRoot(number)` - Beregner kvadratroten (med negativ-sjekk)
- `modulus(a, b)` - Returnerer resten av divisjonen
- `absolute(number)` - Returnerer absoluttverdien
- `help()` - Returnerer informasjon om alle operasjoner

### 3. Direkte MCP-klient

Se [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Denne klienten bruker `HttpClientStreamableHttpTransport` på `/mcp`, initialiserer forbindelsen,
pinger serveren, og følger verktøyliste-paginering. Den sjekker at alle de ni forventede verktøyene
finnes og kaller hver av dem, inkludert `modulus` og `help`, uten en AI-modell.

Den nåværende forespørselsbyggeren ser slik ut:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokollfeil feiler klienten i stedet for å vise en misvisende suksess. MCP-klienten
lukkes med try-with-resources, også når oppdagelse eller et verktøykall feiler.

### 4. AI-drevet klient

Se [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
og [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementerer den nåværende LangChain4j `ChatModel` API-en.
`StreamableHttpMcpTransport` kobler den til samme `/mcp` endepunkt som SDK-klienten.
`AiServices` oppdager verktøyene og håndterer samtalen om verktøykall/resultat.

Standarddistribusjonen er **GPT-5.6 Luna**, med resonnement eksplisitt deaktivert:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Disse standardene gjelder for alle utfyllinger, inkludert oppfølgingsspørsmål etter verktøykjøring.
Klienten bruker en oppfriskbar `BearerTokenCredential` støttet av `DefaultAzureCredential`
og `https://ai.azure.com/.default`-omfanget, ikke et engangstoken sendt som API-nøkkel.
URL-er til ressurs og URL-er som allerede slutter på `/openai/v1` aksepteres begge.

Boten holder en avgrenset samtalehistorikk, skriver ut `Tool executed: ...` med det faktiske
MCP-resultatet, og feiler hvis et svar hopper over verktøy. Verktøysløyfer er begrenset til fire gjennomføringer.
Autentiserings-, modell-, MCP-, og verktøyfeil forplanter seg; automatiske modellforsøk er deaktivert.
Både MCP-transport/klient og den offisielle OpenAI-klienten lukkes ved suksess eller feil.

## Kjøre eksemplene

### Trinn 1: Start kalkulatorserveren

Ingen Azure-konfigurasjon er nødvendig for serveren. Kommandoer nedenfor kjøres fra denne eksemplarmappen.
Eksemplet bruker port **18081** for å unngå konflikt med et annet eksempel; standard er fortsatt 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP-endepunktet er `http://localhost:18081/mcp`. Helse- og oppdagelsesinformasjon finnes på
`http://localhost:18081/health` og `http://localhost:18081/info`.
Strømmbar HTTP erstatter den gamle SSE-only transporten; `/sse` og `/v1/tools` er ikke endepunkter.

### Trinn 2: Test med direkte klient

I et annet PowerShell-terminalvindu:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Ingen input er nødvendig. Alle ni verktøyene testes. Forventede aritmetiske resultater inkluderer
8, 6, 42, 5, 256, 4, 2, og 5.5, etterfulgt av hjelpeteksten.

### Trinn 3: Test med AI-klient

Etter å ha autentisert som beskrevet under forutsetninger, konfigurer AI-klienten i samme terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Forvent en `Tool executed: add` linje med `41.80`, etterfulgt av modellens svar.
Enkeltpromptmodus avslutter uten å vente på input. For å kjøre det originale fire-prompt demonstrasjonen:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demoen kaller `add`, `squareRoot`, `help`, og den kjedede `power` så `divide`-operasjonen.
Forventede numeriske svar er 41.8, 12, og 64. Uten argumenter kjøres også denne demoen.

### Trinn 4: Kjør den interaktive boten

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Skriv inn `Multiply 6 by 7 using the calculator service`, deretter `exit` eller `quit`.
Forvent et faktisk `multiply` verktøyresultat på 42. Tomme linjer ignoreres; EOF avslutter også økten.
For en ikke-interaktiv røyketest av dette inngangspunktet:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Begge AI-inngangspunktene aksepterer `--prompt "question"`, `--demo`, og `--interactive`.
Ugyldige valg feiler før tilkobling opprettes. Hver Maven `-D...` argument må være fullstendig sitert
for PowerShell. På Bash, bruk `export NAME=value` i stedet for `$env:NAME = "value"`.

**Kvoter:** Kjør AI-eksempler sekvensielt. En enkel prompt krever vanligvis to modellforespørsler;
den komplette demoen krever vanligvis ni, inkludert oppfølgingsspørsmål etter verktøyresultater. Med en delt 10 RPM
distribusjon, vent et nytt kvotevindu før neste AI-kjøring. En 429 feiler tydelig uten
automatiske gjentakelser; følg tjenestens retry-after-anvisninger. Faktiske forespørselsantall avhenger av modellen.
Offline-tester bruker ingen kvote og etablerer ikke live Luna-tilgjengelighet eller svar-kvalitet.

### Konfigurasjon og avslutning

| Innstilling | Standard / oppførsel |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; grunn-URL, uten `/mcp` |
| `-Dmcp.server.url=...` | Overstyrer `MCP_SERVER_URL` for alle klienter |
| `AZURE_OPENAI_ENDPOINT` | Kun nødvendig for AI-klienter; ressurs-URL eller `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; et Azure-distribusjonsnavn |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positivt heltall |
| Resonneringsinnsats | Alltid `none`, inkludert verktøysløyfe oppfølgingsspørsmål |

En overstyrt distribusjon må støtte `reasoning_effort=none` og `max_completion_tokens`.
Klientene leser ikke `.env`-fil automatisk. Stopp serveren med `Ctrl+C` etter testing.
Klienter returnerer normalt uten `System.exit` eller avslutningsforsinkelser.

## Offline tester

```powershell
mvn -B -ntp clean verify
```

Alle tester er offline med hensyn til Azure: protokollpakken starter en Spring-server og
en OpenAI-kompatibel stub på tilfeldige loopback-porter, og lukker dem deretter. Maven kan fortsatt trenge
å laste ned avhengigheter. Ingen legitimasjon, live distribusjon, eller forhåndseksisterende MCP-server brukes.

- Enhetstester for kalkulator dekker alle aritmetiske operasjoner, desimalresultater, hjelp, og domene-feil.
- MCP-tester dekker initialisering, oppdagelse, alle ni verktøykall, verktøyfeil, og helse/info.
- AI-protokolltester kjører full demo og interaktiv bot mot den ekte kalkulatoren,
  verifiserer at verktøyresultater brukes i neste utfylling, og inspiserer hver HTTP-body for Luna,
  `reasoning_effort: "none"`, og `max_completion_tokens` uten gammelt `max_tokens`.
- Konfigurering/input-tester dekker distribusjon og endepunkt-overstyringer, tomme linjer, EOF, exit/quit,
  enkeltpromptmodus, ugyldige valg, og feilsopp-håndtering. Kvote-tester viser at 429 ikke gjentas.

## Hvordan alt fungerer sammen

Her er hele flyten når du spør AI: "Hva er 5 + 3?":

1. **Du** spør AI med naturlig språk
2. **AI** analyserer forespørselen din og forstår at du vil ha addisjon
3. **AI** kaller MCP-serveren: `add(5.0, 3.0)`
4. **Kalkulatortjenesten** utfører: `5.0 + 3.0 = 8.0`
5. **Kalkulatortjenesten** returnerer: `"5.00 + 3.00 = 8.00"`
6. **AI** mottar resultatet og formaterer et naturlig svar
7. **Du** får: "Summen av 5 og 3 er 8"

## Neste steg

For flere eksempler, se [Kapittel 04: Praktiske prøver](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->