<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:07:22+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "cs"
}
-->
# Návod na MCP Kalkulačku pro Začátečníky

## Obsah

- [Co se Naučíte](../../../../../04-PracticalSamples/mcp/calculator)
- [Předpoklady](../../../../../04-PracticalSamples/mcp/calculator)
- [Pochopení Struktury Projektu](../../../../../04-PracticalSamples/mcp/calculator)
- [Vysvětlení Klíčových Komponent](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Hlavní Aplikace](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Kalkulační Služba](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Přímý MCP Klient](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Klient s Umělou Inteligencí](../../../../../04-PracticalSamples/mcp/calculator)
- [Spuštění Příkladů](../../../../../04-PracticalSamples/mcp/calculator)
- [Jak to Všechno Funguje Dohromady](../../../../../04-PracticalSamples/mcp/calculator)
- [Další Kroky](../../../../../04-PracticalSamples/mcp/calculator)

## Co se Naučíte

Tento návod vysvětluje, jak vytvořit kalkulační službu pomocí Model Context Protocol (MCP). Naučíte se:

- Jak vytvořit službu, kterou může AI používat jako nástroj
- Jak nastavit přímou komunikaci s MCP službami
- Jak mohou AI modely automaticky vybírat, které nástroje použít
- Rozdíl mezi přímými voláními protokolu a interakcemi asistovanými AI

## Předpoklady

Než začnete, ujistěte se, že máte:
- Nainstalovaný Java 21 nebo vyšší
- Maven pro správu závislostí
- GitHub účet s osobním přístupovým tokenem (PAT)
- Základní znalosti Javy a Spring Boot

## Pochopení Struktury Projektu

Projekt kalkulačky obsahuje několik důležitých souborů:

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

## Vysvětlení Klíčových Komponent

### 1. Hlavní Aplikace

**Soubor:** `McpServerApplication.java`

Toto je vstupní bod naší kalkulační služby. Jedná se o standardní Spring Boot aplikaci s jedním speciálním doplňkem:

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
- Anotace `@Bean` říká Springu, aby tuto komponentu spravoval a umožnil její použití v jiných částech

### 2. Kalkulační Služba

**Soubor:** `CalculatorService.java`

Zde probíhají všechny matematické operace. Každá metoda je označena anotací `@Tool`, aby byla dostupná přes MCP:

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

**Klíčové vlastnosti:**

1. **Anotace `@Tool`**: Označuje, že tuto metodu mohou volat externí klienti
2. **Jasné Popisy**: Každý nástroj má popis, který pomáhá AI modelům pochopit, kdy jej použít
3. **Konzistentní Formát Výstupu**: Všechny operace vrací čitelný text, např. "5.00 + 3.00 = 8.00"
4. **Ošetření Chyb**: Dělení nulou a záporné odmocniny vrací chybové zprávy

**Dostupné Operace:**
- `add(a, b)` - Sčítá dvě čísla
- `subtract(a, b)` - Odečítá druhé číslo od prvního
- `multiply(a, b)` - Násobí dvě čísla
- `divide(a, b)` - Dělí první číslo druhým (s kontrolou nuly)
- `power(base, exponent)` - Umocňuje základ na exponent
- `squareRoot(number)` - Počítá odmocninu (s kontrolou záporných čísel)
- `modulus(a, b)` - Vrací zbytek po dělení
- `absolute(number)` - Vrací absolutní hodnotu
- `help()` - Vrací informace o všech operacích

### 3. Přímý MCP Klient

**Soubor:** `SDKClient.java`

Tento klient komunikuje přímo s MCP serverem bez použití AI. Ručně volá konkrétní kalkulační funkce:

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

**Co to dělá:**
1. **Připojí se** k serveru kalkulačky na `http://localhost:8080`
2. **Vypíše** všechny dostupné nástroje (naše kalkulační funkce)
3. **Volá** konkrétní funkce s přesnými parametry
4. **Tiskne** výsledky přímo

**Kdy to použít:** Když přesně víte, jaký výpočet chcete provést, a chcete jej volat programově.

### 4. Klient s Umělou Inteligencí

**Soubor:** `LangChain4jClient.java`

Tento klient používá AI model (GPT-4o-mini), který dokáže automaticky rozhodnout, které nástroje kalkulačky použít:

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

**Co to dělá:**
1. **Vytvoří** připojení k AI modelu pomocí vašeho GitHub tokenu
2. **Připojí** AI k našemu MCP serveru kalkulačky
3. **Zpřístupní** AI všechny naše kalkulační nástroje
4. **Umožní** přirozené jazykové požadavky, např. "Spočítej součet 24.5 a 17.3"

**AI automaticky:**
- Pochopí, že chcete sčítat čísla
- Vybere nástroj `add`
- Zavolá `add(24.5, 17.3)`
- Vrátí výsledek v přirozené odpovědi

## Spuštění Příkladů

### Krok 1: Spusťte Server Kalkulačky

Nejprve nastavte svůj GitHub token (potřebný pro AI klienta):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Spusťte server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Server se spustí na `http://localhost:8080`. Měli byste vidět:
```
Started McpServerApplication in X.XXX seconds
```

### Krok 2: Otestujte Přímého Klienta

V novém terminálu:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Uvidíte výstup jako:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Krok 3: Otestujte Klienta s AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Uvidíte, jak AI automaticky používá nástroje:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Jak to Všechno Funguje Dohromady

Zde je kompletní proces, když se AI zeptáte "Kolik je 5 + 3?":

1. **Vy** se zeptáte AI přirozeným jazykem
2. **AI** analyzuje váš požadavek a zjistí, že chcete sčítání
3. **AI** zavolá MCP server: `add(5.0, 3.0)`
4. **Kalkulační Služba** provede: `5.0 + 3.0 = 8.0`
5. **Kalkulační Služba** vrátí: `"5.00 + 3.00 = 8.00"`
6. **AI** obdrží výsledek a formátuje přirozenou odpověď
7. **Vy** dostanete: "Součet 5 a 3 je 8"

## Další Kroky

Pro více příkladů si přečtěte [Kapitolu 04: Praktické příklady](../../README.md)

**Prohlášení:**  
Tento dokument byl přeložen pomocí služby pro automatický překlad [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli se snažíme o přesnost, mějte na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace doporučujeme profesionální lidský překlad. Neodpovídáme za žádná nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.