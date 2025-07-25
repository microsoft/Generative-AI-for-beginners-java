<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:43:26+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "no"
}
-->
# MCP Kalkulatorveiledning for Nybegynnere

## Innholdsfortegnelse

- [Hva Du Vil Lære](../../../../../04-PracticalSamples/mcp/calculator)
- [Forutsetninger](../../../../../04-PracticalSamples/mcp/calculator)
- [Forstå Prosjektstrukturen](../../../../../04-PracticalSamples/mcp/calculator)
- [Forklaring av Kjernekomponenter](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Hovedapplikasjon](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Kalkulatortjeneste](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Direkte MCP-klient](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-drevet Klient](../../../../../04-PracticalSamples/mcp/calculator)
- [Kjøre Eksemplene](../../../../../04-PracticalSamples/mcp/calculator)
- [Hvordan Alt Fungerer Sammen](../../../../../04-PracticalSamples/mcp/calculator)
- [Neste Steg](../../../../../04-PracticalSamples/mcp/calculator)

## Hva Du Vil Lære

Denne veiledningen forklarer hvordan du bygger en kalkulatortjeneste ved hjelp av Model Context Protocol (MCP). Du vil forstå:

- Hvordan lage en tjeneste som AI kan bruke som et verktøy
- Hvordan sette opp direkte kommunikasjon med MCP-tjenester
- Hvordan AI-modeller automatisk kan velge hvilke verktøy de skal bruke
- Forskjellen mellom direkte protokollkall og AI-assisterte interaksjoner

## Forutsetninger

Før du starter, sørg for at du har:
- Java 21 eller nyere installert
- Maven for avhengighetsstyring
- En GitHub-konto med en personlig tilgangstoken (PAT)
- Grunnleggende forståelse av Java og Spring Boot

## Forstå Prosjektstrukturen

Kalkulatorprosjektet har flere viktige filer:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## Forklaring av Kjernekomponenter

### 1. Hovedapplikasjon

**Fil:** `McpServerApplication.java`

Dette er startpunktet for kalkulatortjenesten vår. Det er en standard Spring Boot-applikasjon med én spesiell tillegg:

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
- Starter en Spring Boot-webserver på port 8080
- Oppretter en `ToolCallbackProvider` som gjør kalkulatormetodene våre tilgjengelige som MCP-verktøy
- `@Bean`-annotasjonen forteller Spring at dette skal administreres som en komponent som andre deler kan bruke

### 2. Kalkulatortjeneste

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Nøkkelfunksjoner:**

1. **`@Tool`-annotasjon**: Forteller MCP at denne metoden kan kalles av eksterne klienter
2. **Klare beskrivelser**: Hvert verktøy har en beskrivelse som hjelper AI-modeller med å forstå når de skal bruke det
3. **Konsekvent returformat**: Alle operasjoner returnerer lesbare strenger som "5.00 + 3.00 = 8.00"
4. **Feilhåndtering**: Divisjon med null og negative kvadratrøtter returnerer feilmeldinger

**Tilgjengelige Operasjoner:**
- `add(a, b)` - Legger sammen to tall
- `subtract(a, b)` - Trekker det andre fra det første
- `multiply(a, b)` - Multipliserer to tall
- `divide(a, b)` - Dividerer det første med det andre (med null-sjekk)
- `power(base, exponent)` - Opphøyer base til eksponent
- `squareRoot(number)` - Beregner kvadratroten (med negativ sjekk)
- `modulus(a, b)` - Returnerer resten av divisjonen
- `absolute(number)` - Returnerer absoluttverdi
- `help()` - Returnerer informasjon om alle operasjoner

### 3. Direkte MCP-klient

**Fil:** `SDKClient.java`

Denne klienten kommuniserer direkte med MCP-serveren uten å bruke AI. Den kaller spesifikke kalkulatorfunksjoner manuelt:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Hva dette gjør:**
1. **Kobler til** kalkulatorserveren på `http://localhost:8080`
2. **Lister opp** alle tilgjengelige verktøy (kalkulatorfunksjonene våre)
3. **Kaller** spesifikke funksjoner med eksakte parametere
4. **Skriver ut** resultatene direkte

**Når du skal bruke dette:** Når du vet nøyaktig hvilken beregning du vil utføre og ønsker å kalle den programmessig.

### 4. AI-drevet Klient

**Fil:** `LangChain4jClient.java`

Denne klienten bruker en AI-modell (GPT-4o-mini) som automatisk kan bestemme hvilke kalkulatorverktøy som skal brukes:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Hva dette gjør:**
1. **Oppretter** en AI-modelltilkobling ved hjelp av GitHub-tokenet ditt
2. **Kobler** AI til kalkulatorens MCP-server
3. **Gir** AI tilgang til alle kalkulatorverktøyene våre
4. **Tillater** naturlige språkforespørsler som "Beregn summen av 24.5 og 17.3"

**AI gjør automatisk:**
- Forstår at du vil legge sammen tall
- Velger `add`-verktøyet
- Kaller `add(24.5, 17.3)`
- Returnerer resultatet i en naturlig respons

## Kjøre Eksemplene

### Steg 1: Start Kalkulatorserveren

Først, sett GitHub-tokenet ditt (nødvendig for AI-klienten):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Start serveren:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Serveren starter på `http://localhost:8080`. Du bør se:
```
Started McpServerApplication in X.XXX seconds
```

### Steg 2: Test med Direkte Klient

I et nytt terminalvindu:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Du vil se utdata som:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Steg 3: Test med AI Klient

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Du vil se at AI automatisk bruker verktøy:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Hvordan Alt Fungerer Sammen

Her er den komplette flyten når du spør AI "Hva er 5 + 3?":

1. **Du** spør AI på naturlig språk
2. **AI** analyserer forespørselen din og innser at du vil ha en addisjon
3. **AI** kaller MCP-serveren: `add(5.0, 3.0)`
4. **Kalkulatortjenesten** utfører: `5.0 + 3.0 = 8.0`
5. **Kalkulatortjenesten** returnerer: `"5.00 + 3.00 = 8.00"`
6. **AI** mottar resultatet og formaterer en naturlig respons
7. **Du** får: "Summen av 5 og 3 er 8"

## Neste Steg

For flere eksempler, se [Kapittel 04: Praktiske eksempler](../../README.md)

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.