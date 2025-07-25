<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:38:57+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "sv"
}
-->
# MCP-kalkylatorhandledning för nybörjare

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../../../04-PracticalSamples/mcp/calculator)
- [Förkunskaper](../../../../../04-PracticalSamples/mcp/calculator)
- [Förstå projektstrukturen](../../../../../04-PracticalSamples/mcp/calculator)
- [Förklaring av kärnkomponenter](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Huvudapplikation](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Kalkylatorservice](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Direkt MCP-klient](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-driven klient](../../../../../04-PracticalSamples/mcp/calculator)
- [Köra exemplen](../../../../../04-PracticalSamples/mcp/calculator)
- [Hur allt fungerar tillsammans](../../../../../04-PracticalSamples/mcp/calculator)
- [Nästa steg](../../../../../04-PracticalSamples/mcp/calculator)

## Vad du kommer att lära dig

Den här handledningen förklarar hur man bygger en kalkylatorservice med Model Context Protocol (MCP). Du kommer att förstå:

- Hur man skapar en service som AI kan använda som ett verktyg
- Hur man ställer in direkt kommunikation med MCP-tjänster
- Hur AI-modeller automatiskt kan välja vilka verktyg som ska användas
- Skillnaden mellan direkta protokollanrop och AI-assisterade interaktioner

## Förkunskaper

Innan du börjar, se till att du har:
- Java 21 eller högre installerat
- Maven för hantering av beroenden
- Ett GitHub-konto med en personlig åtkomsttoken (PAT)
- Grundläggande förståelse för Java och Spring Boot

## Förstå projektstrukturen

Kalkylatorprojektet innehåller flera viktiga filer:

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

## Förklaring av kärnkomponenter

### 1. Huvudapplikation

**Fil:** `McpServerApplication.java`

Detta är startpunkten för vår kalkylatorservice. Det är en standard Spring Boot-applikation med ett speciellt tillägg:

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
- `@Bean`-annoteringen säger till Spring att hantera detta som en komponent som andra delar kan använda

### 2. Kalkylatorservice

**Fil:** `CalculatorService.java`

Här sker alla beräkningar. Varje metod är markerad med `@Tool` för att göra den tillgänglig via MCP:

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

**Viktiga funktioner:**

1. **`@Tool`-annotering**: Detta talar om för MCP att denna metod kan anropas av externa klienter
2. **Tydliga beskrivningar**: Varje verktyg har en beskrivning som hjälper AI-modeller att förstå när det ska användas
3. **Konsekvent returformat**: Alla operationer returnerar lättlästa strängar som "5.00 + 3.00 = 8.00"
4. **Felhantering**: Division med noll och negativa kvadratrötter returnerar felmeddelanden

**Tillgängliga operationer:**
- `add(a, b)` - Adderar två tal
- `subtract(a, b)` - Subtraherar det andra från det första
- `multiply(a, b)` - Multiplicerar två tal
- `divide(a, b)` - Dividerar det första med det andra (med kontroll för noll)
- `power(base, exponent)` - Höjer basen till exponentens värde
- `squareRoot(number)` - Beräknar kvadratroten (med kontroll för negativa tal)
- `modulus(a, b)` - Returnerar resten av divisionen
- `absolute(number)` - Returnerar absolutvärdet
- `help()` - Returnerar information om alla operationer

### 3. Direkt MCP-klient

**Fil:** `SDKClient.java`

Denna klient kommunicerar direkt med MCP-servern utan att använda AI. Den anropar specifika kalkylatorfunktioner manuellt:

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

**Vad detta gör:**
1. **Ansluter** till kalkylatorservern på `http://localhost:8080`
2. **Listar** alla tillgängliga verktyg (våra kalkylatorfunktioner)
3. **Anropar** specifika funktioner med exakta parametrar
4. **Skriver ut** resultaten direkt

**När detta används:** När du vet exakt vilken beräkning du vill utföra och vill anropa den programmatiskt.

### 4. AI-driven klient

**Fil:** `LangChain4jClient.java`

Denna klient använder en AI-modell (GPT-4o-mini) som automatiskt kan bestämma vilka kalkylatorverktyg som ska användas:

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

**Vad detta gör:**
1. **Skapar** en AI-modellanslutning med din GitHub-token
2. **Ansluter** AI till vår kalkylator-MCP-server
3. **Ger** AI tillgång till alla våra kalkylatorverktyg
4. **Tillåter** naturliga språkförfrågningar som "Beräkna summan av 24.5 och 17.3"

**AI gör automatiskt:**
- Förstår att du vill addera tal
- Väljer verktyget `add`
- Anropar `add(24.5, 17.3)`
- Returnerar resultatet i ett naturligt svar

## Köra exemplen

### Steg 1: Starta kalkylatorservern

Ställ först in din GitHub-token (behövs för AI-klienten):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Starta servern:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Servern startar på `http://localhost:8080`. Du bör se:
```
Started McpServerApplication in X.XXX seconds
```

### Steg 2: Testa med direkt klient

I ett nytt terminalfönster:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Du kommer att se output som:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Steg 3: Testa med AI-klient

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Du kommer att se AI automatiskt använda verktyg:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Hur allt fungerar tillsammans

Så här ser flödet ut när du frågar AI "Vad är 5 + 3?":

1. **Du** frågar AI på naturligt språk
2. **AI** analyserar din fråga och inser att du vill addera
3. **AI** anropar MCP-servern: `add(5.0, 3.0)`
4. **Kalkylatorservice** utför: `5.0 + 3.0 = 8.0`
5. **Kalkylatorservice** returnerar: `"5.00 + 3.00 = 8.00"`
6. **AI** tar emot resultatet och formaterar ett naturligt svar
7. **Du** får: "Summan av 5 och 3 är 8"

## Nästa steg

För fler exempel, se [Kapitel 04: Praktiska exempel](../../README.md)

**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiserade översättningar kan innehålla fel eller felaktigheter. Det ursprungliga dokumentet på dess ursprungliga språk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.