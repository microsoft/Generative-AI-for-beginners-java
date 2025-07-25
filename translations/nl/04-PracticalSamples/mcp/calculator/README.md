<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:48:08+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "nl"
}
-->
# MCP Calculator Tutorial voor Beginners

## Inhoudsopgave

- [Wat Je Gaat Leren](../../../../../04-PracticalSamples/mcp/calculator)
- [Vereisten](../../../../../04-PracticalSamples/mcp/calculator)
- [De Projectstructuur Begrijpen](../../../../../04-PracticalSamples/mcp/calculator)
- [Uitleg van Kerncomponenten](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Hoofdapplicatie](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Calculator Service](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Directe MCP Client](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-gestuurde Client](../../../../../04-PracticalSamples/mcp/calculator)
- [Voorbeelden Uitvoeren](../../../../../04-PracticalSamples/mcp/calculator)
- [Hoe Alles Samenwerkt](../../../../../04-PracticalSamples/mcp/calculator)
- [Volgende Stappen](../../../../../04-PracticalSamples/mcp/calculator)

## Wat Je Gaat Leren

Deze tutorial legt uit hoe je een calculatorservice bouwt met het Model Context Protocol (MCP). Je leert:

- Hoe je een service maakt die door AI als hulpmiddel kan worden gebruikt
- Hoe je directe communicatie met MCP-services instelt
- Hoe AI-modellen automatisch kunnen kiezen welke tools ze moeten gebruiken
- Het verschil tussen directe protocoloproepen en AI-ondersteunde interacties

## Vereisten

Voordat je begint, zorg ervoor dat je:
- Java 21 of hoger hebt geïnstalleerd
- Maven gebruikt voor dependency management
- Een GitHub-account hebt met een persoonlijke toegangstoken (PAT)
- Basiskennis hebt van Java en Spring Boot

## De Projectstructuur Begrijpen

Het calculatorproject bevat verschillende belangrijke bestanden:

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

## Uitleg van Kerncomponenten

### 1. Hoofdapplicatie

**Bestand:** `McpServerApplication.java`

Dit is het startpunt van onze calculatorservice. Het is een standaard Spring Boot-applicatie met één speciale toevoeging:

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
- Start een Spring Boot-webserver op poort 8080
- Creëert een `ToolCallbackProvider` die onze calculatormethoden beschikbaar maakt als MCP-tools
- De `@Bean`-annotatie vertelt Spring om dit te beheren als een component die door andere onderdelen kan worden gebruikt

### 2. Calculator Service

**Bestand:** `CalculatorService.java`

Hier vindt alle berekening plaats. Elke methode is gemarkeerd met `@Tool` om deze via MCP beschikbaar te maken:

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

**Belangrijke kenmerken:**

1. **`@Tool`-annotatie**: Geeft aan dat deze methode door externe clients kan worden aangeroepen via MCP
2. **Duidelijke beschrijvingen**: Elke tool heeft een beschrijving die AI-modellen helpt te begrijpen wanneer ze deze moeten gebruiken
3. **Consistente retourformaten**: Alle operaties retourneren menselijk leesbare strings zoals "5.00 + 3.00 = 8.00"
4. **Foutafhandeling**: Delen door nul en negatieve vierkantswortels geven foutmeldingen terug

**Beschikbare operaties:**
- `add(a, b)` - Optelt twee getallen
- `subtract(a, b)` - Aftrekt het tweede getal van het eerste
- `multiply(a, b)` - Vermenigvuldigt twee getallen
- `divide(a, b)` - Deelt het eerste getal door het tweede (met controle op nul)
- `power(base, exponent)` - Verheft de basis tot de macht van de exponent
- `squareRoot(number)` - Berekent de vierkantswortel (met controle op negatieve waarden)
- `modulus(a, b)` - Geeft de rest van een deling
- `absolute(number)` - Geeft de absolute waarde
- `help()` - Geeft informatie over alle operaties

### 3. Directe MCP Client

**Bestand:** `SDKClient.java`

Deze client communiceert rechtstreeks met de MCP-server zonder gebruik te maken van AI. Het roept handmatig specifieke calculatorfuncties aan:

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

**Wat dit doet:**
1. **Verbindt** met de calculatorserver op `http://localhost:8080`
2. **Lijst** alle beschikbare tools (onze calculatorfuncties)
3. **Roept** specifieke functies aan met exacte parameters
4. **Print** de resultaten direct

**Wanneer te gebruiken:** Wanneer je precies weet welke berekening je wilt uitvoeren en dit programmatisch wilt aanroepen.

### 4. AI-gestuurde Client

**Bestand:** `LangChain4jClient.java`

Deze client gebruikt een AI-model (GPT-4o-mini) dat automatisch kan beslissen welke calculatortools te gebruiken:

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

**Wat dit doet:**
1. **Creëert** een verbinding met het AI-model met behulp van je GitHub-token
2. **Verbindt** de AI met onze calculator MCP-server
3. **Geeft** de AI toegang tot al onze calculatortools
4. **Maakt** natuurlijke taalverzoeken mogelijk zoals "Bereken de som van 24.5 en 17.3"

**De AI doet automatisch:**
- Begrijpt dat je getallen wilt optellen
- Kiest de `add`-tool
- Roept `add(24.5, 17.3)` aan
- Geeft het resultaat terug in een natuurlijke reactie

## Voorbeelden Uitvoeren

### Stap 1: Start de Calculatorserver

Stel eerst je GitHub-token in (nodig voor de AI-client):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Start de server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

De server start op `http://localhost:8080`. Je zou het volgende moeten zien:
```
Started McpServerApplication in X.XXX seconds
```

### Stap 2: Test met Directe Client

Open een nieuwe terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Je ziet output zoals:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Stap 3: Test met AI Client

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Je ziet dat de AI automatisch tools gebruikt:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Hoe Alles Samenwerkt

Hier is de volledige flow wanneer je de AI vraagt "Wat is 5 + 3?":

1. **Jij** vraagt de AI in natuurlijke taal
2. **AI** analyseert je verzoek en begrijpt dat je optelling wilt
3. **AI** roept de MCP-server aan: `add(5.0, 3.0)`
4. **Calculator Service** voert uit: `5.0 + 3.0 = 8.0`
5. **Calculator Service** retourneert: `"5.00 + 3.00 = 8.00"`
6. **AI** ontvangt het resultaat en formatteert een natuurlijke reactie
7. **Jij** krijgt: "De som van 5 en 3 is 8"

## Volgende Stappen

Voor meer voorbeelden, zie [Hoofdstuk 04: Praktische voorbeelden](../../README.md)

**Disclaimer (Vrijwaring)**:  
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.