# MCP Calculator Tutorial voor Beginners

## Inhoudsopgave

- [Wat Je Zal Leren](#wat-je-zal-leren)
- [Vereisten](#vereisten)
- [Versies van Afhankelijkheden](#versies-van-afhankelijkheden)
- [Begrijpen van de Projectstructuur](#begrijpen-van-de-projectstructuur)
- [Uitleg van Kerncomponenten](#uitleg-van-kerncomponenten)
  - [1. Hoofdapplicatie](#1-hoofdapplicatie)
  - [2. Calculator Service](#2-calculator-service)
  - [3. Directe MCP Client](#3-directe-mcp-client)
  - [4. AI-aangedreven Client](#4-ai-aangedreven-client)
- [De Voorbeelden Uitvoeren](#de-voorbeelden-uitvoeren)
- [Offline Tests](#offline-tests)
- [Hoe Het Alles Samenwerkt](#hoe-het-alles-samenwerkt)
- [Volgende Stappen](#volgende-stappen)

## Wat Je Zal Leren

Deze tutorial legt uit hoe je een calculator service bouwt met het Model Context Protocol (MCP). Je zult begrijpen:

- Hoe je een service maakt die AI kan gebruiken als hulpmiddel
- Hoe je directe communicatie met MCP-diensten opzet
- Hoe AI-modellen automatisch kunnen kiezen welke hulpmiddelen te gebruiken
- Het verschil tussen directe protocoloproepen en AI-geassisteerde interacties

## Vereisten

Voordat je begint, zorg ervoor dat je hebt:
- Java 21 of hoger geïnstalleerd
- Maven voor afhankelijkheidsbeheer
- Basiskennis van Java en Spring Boot

Alleen de AI-clients vereisen een Azure OpenAI-implementatie en een geauthenticeerde `DefaultAzureCredential`,
zoals een bestaande Azure CLI aanmelding lokaal of een beheerde identiteit in Azure. De identiteit heeft
de rol Cognitive Services OpenAI User nodig op de resource. Zie [Hoofdstuk 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
De server, directe SDK-client en alle geautomatiseerde tests hebben geen Azure-account of modeltoegang nodig.

## Versies van Afhankelijkheden

Gepubliceerde afhankelijkheden geverifieerd op 2026-09-14:

| Afhankelijkheid | Versie |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-beheerd) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j officiële OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-beheerd) | 6.0.3 |

De MCP en officiële OpenAI adapters zijn gepubliceerde beta releases in Maven Central, geen snapshots.
Hun versies verschillen van LangChain4j core. Er zijn geen snapshot- of milestone-repository's nodig.
Client-only afhankelijkheden hebben test scope omdat de uitvoerbare voorbeelden onder `src/test/java` staan.

## Begrijpen van de Projectstructuur

Het calculator-project heeft verschillende belangrijke bestanden:

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

## Uitleg van Kerncomponenten

### 1. Hoofdapplicatie

**Bestand:** `McpServerApplication.java`

Dit is het startpunt van onze calculator service. Het is een standaard Spring Boot applicatie met een speciale toevoeging:

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

**Wat dit doet:**
- Start een Spring Boot webserver op poort 8080
- Maakt een `ToolCallbackProvider` aan die onze calculator-methoden beschikbaar maakt als MCP-hulpmiddelen
- De `@Bean` annotatie vertelt Spring om dit te beheren als component die andere delen kunnen gebruiken

### 2. Calculator Service

**Bestand:** `CalculatorService.java`

Hier vindt alle wiskunde plaats. Elke methode is gemarkeerd met `@Tool` om deze via MCP beschikbaar te maken:

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
    
    // Meer rekenmachinebewerkingen...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Belangrijkste kenmerken:**

1. **`@Tool` Annotatie**: Dit vertelt MCP dat deze methode door externe klanten kan worden aangeroepen
2. **Duidelijke Beschrijvingen**: Elk hulpmiddel heeft een beschrijving die AI-modellen helpt te begrijpen wanneer het te gebruiken
3. **Consistente Return-structuur**: Alle bewerkingen geven menselijk leesbare strings terug zoals "5.00 + 3.00 = 8.00"
4. **Foutafhandeling**: Delen door nul en negatieve vierkantswortels geven foutmeldingen terug

**Beschikbare bewerkingen:**
- `add(a, b)` - Voegt twee getallen samen
- `subtract(a, b)` - Trekt het tweede getal af van het eerste
- `multiply(a, b)` - Vermenigvuldigt twee getallen
- `divide(a, b)` - Deelt het eerste getal door het tweede (controleert op nul)
- `power(base, exponent)` - Verheft de basis tot de macht van de exponent
- `squareRoot(number)` - Berekent de vierkantswortel (controleert op negatieve waarden)
- `modulus(a, b)` - Geeft de rest van de deling terug
- `absolute(number)` - Geeft de absolute waarde terug
- `help()` - Geeft informatie over alle bewerkingen

### 3. Directe MCP Client

Zie [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Deze client gebruikt `HttpClientStreamableHttpTransport` op `/mcp`, initialiseert de verbinding,
pingt de server, en volgt paginering van de lijst met hulpmiddelen. Hij controleert of alle negen verwachte hulpmiddelen
bestaan en roept elk van hen aan, inclusief `modulus` en `help`, zonder een AI-model.

De huidige request builder ziet er zo uit:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protocolfouten laten de client falen in plaats van een misleidend succes te tonen. De MCP client
wordt gesloten met try-with-resources, ook wanneer ontdekking of een hulpmiddel-aanroep mislukt.

### 4. AI-aangedreven Client

Zie [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
en [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementeert de huidige LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` verbindt deze met dezelfde `/mcp` endpoint als de SDK client.
`AiServices` ontdekt de hulpmiddelen en beheert het gesprek over hulpmiddel-aanroep/antwoord.

De standaardimplementatie is **GPT-5.6 Luna**, waarbij redeneren expliciet is uitgeschakeld:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Deze standaarden gelden voor elke voltooiing, inclusief vervolgvragen na het gebruiken van hulpmiddelen.
De client gebruikt een verfrisbare `BearerTokenCredential` ondersteund door `DefaultAzureCredential`
en de scope `https://ai.azure.com/.default`, niet een eenmalige token die als API key wordt doorgegeven.
Zowel resource-URL's als URL's die al eindigen op `/openai/v1` worden geaccepteerd.

De bot houdt een begrensde gesprekken geschiedenis bij, print `Tool executed: ...` met het daadwerkelijke
MCP resultaat, en faalt indien een antwoord hulpmiddelen overslaat. Hulpmiddel-lussen zijn beperkt tot vier rondes.
Authenticatie-, model-, MCP-, en hulpmiddelfouten worden doorgegeven; automatische modelherhalingen zijn uitgeschakeld.
Zowel de MCP transport/client als de officiële OpenAI client worden gesloten bij succes of falen.

## De Voorbeelden Uitvoeren

### Stap 1: Start de Calculator Server

Geen Azure-configuratie is nodig voor de server. De onderstaande commando's worden uitgevoerd vanuit de map van dit voorbeeld.
Het voorbeeld gebruikt poort **18081** om conflicten met een ander voorbeeld te vermijden; standaard blijft 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

De MCP endpoint is `http://localhost:18081/mcp`. Gezondheids- en ontdekkingsinformatie is beschikbaar op
`http://localhost:18081/health` en `http://localhost:18081/info`.
Streamable HTTP vervangt de oude SSE-only transport; `/sse` en `/v1/tools` zijn geen endpoints meer.

### Stap 2: Test met Directe Client

In een andere PowerShell-terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Er is geen invoer nodig. Alle negen hulpmiddelen worden getest. Verwachte rekenkundige resultaten zijn
8, 6, 42, 5, 256, 4, 2, en 5.5, gevolgd door de help-tekst.

### Stap 3: Test met AI Client

Na authenticatie zoals beschreven bij de vereisten, configureer de AI client in dezelfde terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Verwacht een regel `Tool executed: add` met `41.80`, gevolgd door het antwoord van het model.
De single-prompt modus sluit af zonder op invoer te wachten. Om de originele vier-prompt demo te draaien:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

De demo roept `add`, `squareRoot`, `help`, en de gekoppelde `power` dan `divide` operatie aan.
Verwachte numerieke antwoorden zijn 41.8, 12, en 64. Argumenten weglaten voert deze demo ook uit.

### Stap 4: Run de Interactieve Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Typ `Multiply 6 by 7 using the calculator service`, vervolgens `exit` of `quit`.
Verwacht een daadwerkelijk `multiply` hulpmiddel resultaat van 42. Lege regels worden genegeerd; EOF beëindigt de sessie ook.
Voor een niet-interactieve rooktest van dit toegangspunt:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Beide AI toegangspunten accepteren `--prompt "question"`, `--demo`, en `--interactive`.
Ongeldige opties falen vóór het openen van een verbinding. Elk Maven `-D...` argument is volledig gequoteerd
voor PowerShell. Gebruik onder Bash `export NAME=value` in plaats van `$env:NAME = "value"`.

**Quota:** Voer AI voorbeelden aaneengesloten uit. Een simpele prompt vereist normaal twee modelaanvragen;
de complete demo meestal negen, inclusief vervolgvragen na hulpmiddelresultaten. Met een gedeelde 10 RPM
implementatie, wacht een nieuw quotumvenster voordat je opnieuw AI draait. Een 429 faalt zichtbaar zonder
automatische herhalingen; volg de retry-after aanwijzingen van de dienst. Het daadwerkelijke aantal aanvragen hangt af van het model.
Offline tests verbruiken geen quotum en testen geen live Luna beschikbaarheid of antwoordkwaliteit.

### Configuratie en Afsluiting

| Instelling | Standaard / gedrag |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; basis-URL, zonder `/mcp` |
| `-Dmcp.server.url=...` | Overschrijft `MCP_SERVER_URL` voor alle clients |
| `AZURE_OPENAI_ENDPOINT` | Alleen verplicht voor AI clients; resource URL of `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; een Azure implementatienaam |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positief geheel getal |
| Redeneringsinspanning | Altijd `none`, inclusief vervolgvragen na hulpmiddellussen |

Een overschreven implementatie moet `reasoning_effort=none` en `max_completion_tokens` ondersteunen.
De clients lezen niet automatisch een `.env` bestand in. Stop de server met `Ctrl+C` na het testen.
Clients keren normaal terug zonder `System.exit` of vertragingen bij afsluiten.

## Offline Tests

```powershell
mvn -B -ntp clean verify
```

Alle tests zijn offline ten opzichte van Azure: de protocolsuite start een Spring-server en
een OpenAI-compatibele stub op willekeurige loopback-poorten, en sluit ze daarna weer. Maven kan nog afhankelijkheden
moeten downloaden. Er worden geen credentials, live-implementatie of bestaande MCP-server gebruikt.

- Calculator unittests dekken alle rekenkundige bewerkingen, decimale resultaten, hulp, en domeinfouten.
- MCP tests dekken initialisatie, ontdekking, alle negen hulpmiddel-aanroepen, hulpmiddel-fouten, en gezondheids/info-checks.
- De AI protocoltests voeren de volledige demo en interactieve Bot uit tegen de echte calculator,
  verifiëren dat hulpmiddelresultaten de volgende voltooiing aansturen, en inspecteren elke HTTP-body voor Luna,
  `reasoning_effort: "none"`, en `max_completion_tokens` zonder legacy `max_tokens`.
- Configuratie/invoer tests dekken overschrijvingen voor implementatie en endpoint, lege regels, EOF, exit/quit,
  single-prompt modus, ongeldige opties, en foutdoorvoer. Quota tests bewijzen dat 429 niet wordt herhaald.

## Hoe Het Alles Samenwerkt

Dit is de volledige stroom wanneer je de AI vraagt "Wat is 5 + 3?":

1. **Jij** stelt de AI een vraag in natuurlijke taal
2. **AI** analyseert je verzoek en realiseert zich dat je optellen wilt
3. **AI** roept de MCP server aan: `add(5.0, 3.0)`
4. **Calculator Service** voert uit: `5.0 + 3.0 = 8.0`
5. **Calculator Service** geeft terug: `"5.00 + 3.00 = 8.00"`
6. **AI** ontvangt het resultaat en formuleert een natuurlijk antwoord
7. **Jij** krijgt: "De som van 5 en 3 is 8"

## Volgende Stappen

Voor meer voorbeelden, zie [Hoofdstuk 04: Praktische voorbeelden](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->