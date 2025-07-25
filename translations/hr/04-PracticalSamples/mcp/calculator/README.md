<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:20:30+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "hr"
}
-->
# MCP Kalkulator Vodič za Početnike

## Sadržaj

- [Što ćete naučiti](../../../../../04-PracticalSamples/mcp/calculator)
- [Preduvjeti](../../../../../04-PracticalSamples/mcp/calculator)
- [Razumijevanje strukture projekta](../../../../../04-PracticalSamples/mcp/calculator)
- [Objašnjenje ključnih komponenti](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Glavna aplikacija](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Servis kalkulatora](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Direktni MCP klijent](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Klijent s AI podrškom](../../../../../04-PracticalSamples/mcp/calculator)
- [Pokretanje primjera](../../../../../04-PracticalSamples/mcp/calculator)
- [Kako sve funkcionira zajedno](../../../../../04-PracticalSamples/mcp/calculator)
- [Sljedeći koraci](../../../../../04-PracticalSamples/mcp/calculator)

## Što ćete naučiti

Ovaj vodič objašnjava kako izgraditi servis kalkulatora koristeći Model Context Protocol (MCP). Naučit ćete:

- Kako kreirati servis koji AI može koristiti kao alat
- Kako postaviti direktnu komunikaciju s MCP servisima
- Kako AI modeli automatski biraju koje alate koristiti
- Razliku između direktnih poziva protokola i interakcija uz pomoć AI-a

## Preduvjeti

Prije početka, osigurajte da imate:
- Instaliran Java 21 ili noviji
- Maven za upravljanje ovisnostima
- GitHub račun s osobnim pristupnim tokenom (PAT)
- Osnovno razumijevanje Jave i Spring Boot-a

## Razumijevanje strukture projekta

Projekt kalkulatora sadrži nekoliko važnih datoteka:

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

## Objašnjenje ključnih komponenti

### 1. Glavna aplikacija

**Datoteka:** `McpServerApplication.java`

Ovo je ulazna točka našeg servisa kalkulatora. To je standardna Spring Boot aplikacija s jednom posebnom dodatkom:

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

**Što ovo radi:**
- Pokreće Spring Boot web server na portu 8080
- Kreira `ToolCallbackProvider` koji omogućuje da naše metode kalkulatora budu dostupne kao MCP alati
- `@Bean` anotacija govori Springu da upravlja ovim kao komponentom koju mogu koristiti drugi dijelovi

### 2. Servis kalkulatora

**Datoteka:** `CalculatorService.java`

Ovdje se odvijaju sve matematičke operacije. Svaka metoda označena je s `@Tool` kako bi bila dostupna putem MCP-a:

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

**Ključne značajke:**

1. **`@Tool` anotacija**: Ova oznaka MCP-u govori da se metoda može pozvati od strane vanjskih klijenata
2. **Jasni opisi**: Svaki alat ima opis koji pomaže AI modelima da razumiju kada ga koristiti
3. **Dosljedan format povratka**: Sve operacije vraćaju lako čitljive stringove poput "5.00 + 3.00 = 8.00"
4. **Rukovanje greškama**: Dijeljenje s nulom i negativni korijeni vraćaju poruke o grešci

**Dostupne operacije:**
- `add(a, b)` - Zbraja dva broja
- `subtract(a, b)` - Oduzima drugi broj od prvog
- `multiply(a, b)` - Množi dva broja
- `divide(a, b)` - Dijeli prvi broj s drugim (provjera nule)
- `power(base, exponent)` - Podiže osnovu na potenciju eksponenta
- `squareRoot(number)` - Izračunava kvadratni korijen (provjera negativnih brojeva)
- `modulus(a, b)` - Vraća ostatak dijeljenja
- `absolute(number)` - Vraća apsolutnu vrijednost
- `help()` - Vraća informacije o svim operacijama

### 3. Direktni MCP klijent

**Datoteka:** `SDKClient.java`

Ovaj klijent direktno komunicira s MCP serverom bez korištenja AI-a. Ručno poziva specifične funkcije kalkulatora:

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

**Što ovo radi:**
1. **Povezuje se** s kalkulator serverom na `http://localhost:8080`
2. **Prikazuje** sve dostupne alate (naše funkcije kalkulatora)
3. **Poziva** specifične funkcije s točnim parametrima
4. **Ispisuje** rezultate direktno

**Kada koristiti ovo:** Kada točno znate koju kalkulaciju želite izvršiti i želite je pozvati programatski.

### 4. Klijent s AI podrškom

**Datoteka:** `LangChain4jClient.java`

Ovaj klijent koristi AI model (GPT-4o-mini) koji automatski odlučuje koje alate kalkulatora koristiti:

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

**Što ovo radi:**
1. **Stvara** vezu s AI modelom koristeći vaš GitHub token
2. **Povezuje** AI s našim MCP serverom kalkulatora
3. **Omogućuje** prirodne jezične zahtjeve poput "Izračunaj zbroj 24.5 i 17.3"

**AI automatski:**
- Razumije da želite zbrojiti brojeve
- Odabire alat `add`
- Poziva `add(24.5, 17.3)`
- Vraća rezultat u prirodnom odgovoru

## Pokretanje primjera

### Korak 1: Pokrenite server kalkulatora

Prvo postavite svoj GitHub token (potreban za AI klijent):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Pokrenite server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Server će se pokrenuti na `http://localhost:8080`. Trebali biste vidjeti:
```
Started McpServerApplication in X.XXX seconds
```

### Korak 2: Testirajte s direktnim klijentom

U novom terminalu:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Vidjet ćete izlaz poput:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Korak 3: Testirajte s AI klijentom

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Vidjet ćete kako AI automatski koristi alate:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Kako sve funkcionira zajedno

Evo kompletnog toka kada pitate AI "Koliko je 5 + 3?":

1. **Vi** pitate AI na prirodnom jeziku
2. **AI** analizira vaš zahtjev i shvaća da želite zbrajanje
3. **AI** poziva MCP server: `add(5.0, 3.0)`
4. **Servis kalkulatora** izvršava: `5.0 + 3.0 = 8.0`
5. **Servis kalkulatora** vraća: `"5.00 + 3.00 = 8.00"`
6. **AI** prima rezultat i formatira prirodni odgovor
7. **Vi** dobivate: "Zbroj 5 i 3 je 8"

## Sljedeći koraci

Za više primjera, pogledajte [Poglavlje 04: Praktični primjeri](../../README.md)

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni prijevod od strane stručnjaka. Ne preuzimamo odgovornost za nesporazume ili pogrešna tumačenja koja mogu proizaći iz korištenja ovog prijevoda.