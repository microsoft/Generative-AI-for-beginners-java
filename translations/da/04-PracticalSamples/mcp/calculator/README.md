<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:41:12+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "da"
}
-->
# MCP Calculator Tutorial for Begyndere

## Indholdsfortegnelse

- [Hvad Du Vil Lære](../../../../../04-PracticalSamples/mcp/calculator)
- [Forudsætninger](../../../../../04-PracticalSamples/mcp/calculator)
- [Forstå Projektstrukturen](../../../../../04-PracticalSamples/mcp/calculator)
- [Forklaring af Kernekomponenter](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Hovedapplikation](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Calculator Service](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Direkte MCP-klient](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-drevet Klient](../../../../../04-PracticalSamples/mcp/calculator)
- [Kør Eksemplerne](../../../../../04-PracticalSamples/mcp/calculator)
- [Hvordan Det Hele Fungerer Sammen](../../../../../04-PracticalSamples/mcp/calculator)
- [Næste Skridt](../../../../../04-PracticalSamples/mcp/calculator)

## Hvad Du Vil Lære

Denne tutorial forklarer, hvordan man bygger en calculator service ved hjælp af Model Context Protocol (MCP). Du vil lære:

- Hvordan man opretter en service, som AI kan bruge som et værktøj
- Hvordan man opsætter direkte kommunikation med MCP-services
- Hvordan AI-modeller automatisk kan vælge, hvilke værktøjer der skal bruges
- Forskellen mellem direkte protokolopkald og AI-assisterede interaktioner

## Forudsætninger

Før du starter, skal du sikre dig, at du har:
- Java 21 eller nyere installeret
- Maven til håndtering af afhængigheder
- En GitHub-konto med en personlig adgangstoken (PAT)
- Grundlæggende forståelse af Java og Spring Boot

## Forstå Projektstrukturen

Calculator-projektet indeholder flere vigtige filer:

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

## Forklaring af Kernekomponenter

### 1. Hovedapplikation

**Fil:** `McpServerApplication.java`

Dette er indgangspunktet for vores calculator service. Det er en standard Spring Boot-applikation med en særlig tilføjelse:

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

**Hvad dette gør:**
- Starter en Spring Boot-webserver på port 8080
- Opretter en `ToolCallbackProvider`, der gør vores calculator-metoder tilgængelige som MCP-værktøjer
- `@Bean`-annoteringen fortæller Spring, at dette skal administreres som en komponent, som andre dele kan bruge

### 2. Calculator Service

**Fil:** `CalculatorService.java`

Her sker alle beregningerne. Hver metode er markeret med `@Tool` for at gøre den tilgængelig via MCP:

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

**Nøglefunktioner:**

1. **`@Tool`-annotering**: Dette fortæller MCP, at denne metode kan kaldes af eksterne klienter
2. **Klare beskrivelser**: Hvert værktøj har en beskrivelse, der hjælper AI-modeller med at forstå, hvornår det skal bruges
3. **Konsistent returformat**: Alle operationer returnerer læsbare strenge som "5.00 + 3.00 = 8.00"
4. **Fejlhåndtering**: Division med nul og negative kvadratrødder returnerer fejlmeddelelser

**Tilgængelige operationer:**
- `add(a, b)` - Lægger to tal sammen
- `subtract(a, b)` - Trækker det andet tal fra det første
- `multiply(a, b)` - Ganger to tal
- `divide(a, b)` - Dividerer det første tal med det andet (med nul-tjek)
- `power(base, exponent)` - Løfter base til eksponentens potens
- `squareRoot(number)` - Beregner kvadratroden (med negativt tjek)
- `modulus(a, b)` - Returnerer resten af divisionen
- `absolute(number)` - Returnerer den absolutte værdi
- `help()` - Returnerer information om alle operationer

### 3. Direkte MCP-klient

**Fil:** `SDKClient.java`

Denne klient kommunikerer direkte med MCP-serveren uden at bruge AI. Den kalder manuelt specifikke calculator-funktioner:

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

**Hvad dette gør:**
1. **Forbinder** til calculator-serveren på `http://localhost:8080`
2. **Lister** alle tilgængelige værktøjer (vores calculator-funktioner)
3. **Kalder** specifikke funktioner med præcise parametre
4. **Printer** resultaterne direkte

**Hvornår man skal bruge dette:** Når du præcist ved, hvilken beregning du vil udføre, og ønsker at kalde den programmæssigt.

### 4. AI-drevet Klient

**Fil:** `LangChain4jClient.java`

Denne klient bruger en AI-model (GPT-4o-mini), der automatisk kan beslutte, hvilke calculator-værktøjer der skal bruges:

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

**Hvad dette gør:**
1. **Opretter** en AI-modelforbindelse ved hjælp af din GitHub-token
2. **Forbinder** AI til vores calculator MCP-server
3. **Giver** AI adgang til alle vores calculator-værktøjer
4. **Tillader** naturlige sprogforespørgsler som "Beregn summen af 24.5 og 17.3"

**AI gør automatisk følgende:**
- Forstår, at du vil lægge tal sammen
- Vælger værktøjet `add`
- Kalder `add(24.5, 17.3)`
- Returnerer resultatet i en naturlig respons

## Kør Eksemplerne

### Trin 1: Start Calculator-serveren

Først skal du indstille din GitHub-token (nødvendig for AI-klienten):

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

### Trin 2: Test med Direkte Klient

I en ny terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Du vil se output som:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Trin 3: Test med AI Klient

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Du vil se, at AI automatisk bruger værktøjer:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Hvordan Det Hele Fungerer Sammen

Her er den komplette proces, når du spørger AI "Hvad er 5 + 3?":

1. **Du** spørger AI på naturligt sprog
2. **AI** analyserer din forespørgsel og indser, at du vil lægge tal sammen
3. **AI** kalder MCP-serveren: `add(5.0, 3.0)`
4. **Calculator Service** udfører: `5.0 + 3.0 = 8.0`
5. **Calculator Service** returnerer: `"5.00 + 3.00 = 8.00"`
6. **AI** modtager resultatet og formaterer en naturlig respons
7. **Du** får: "Summen af 5 og 3 er 8"

## Næste Skridt

For flere eksempler, se [Kapitel 04: Praktiske eksempler](../../README.md)

**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på at sikre nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi er ikke ansvarlige for eventuelle misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.