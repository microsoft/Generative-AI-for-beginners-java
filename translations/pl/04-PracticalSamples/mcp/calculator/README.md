<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:27:50+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "pl"
}
-->
# Samouczek Kalkulatora MCP dla Początkujących

## Spis Treści

- [Czego Się Nauczysz](../../../../../04-PracticalSamples/mcp/calculator)
- [Wymagania Wstępne](../../../../../04-PracticalSamples/mcp/calculator)
- [Zrozumienie Struktury Projektu](../../../../../04-PracticalSamples/mcp/calculator)
- [Wyjaśnienie Głównych Komponentów](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Główna Aplikacja](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Serwis Kalkulatora](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Bezpośredni Klient MCP](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Klient Wspierany przez AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Uruchamianie Przykładów](../../../../../04-PracticalSamples/mcp/calculator)
- [Jak Wszystko Razem Działa](../../../../../04-PracticalSamples/mcp/calculator)
- [Kolejne Kroki](../../../../../04-PracticalSamples/mcp/calculator)

## Czego Się Nauczysz

Ten samouczek wyjaśnia, jak zbudować serwis kalkulatora przy użyciu Model Context Protocol (MCP). Dowiesz się:

- Jak stworzyć serwis, który AI może wykorzystać jako narzędzie
- Jak skonfigurować bezpośrednią komunikację z usługami MCP
- Jak modele AI mogą automatycznie wybierać, których narzędzi użyć
- Różnic między bezpośrednimi wywołaniami protokołu a interakcjami wspieranymi przez AI

## Wymagania Wstępne

Przed rozpoczęciem upewnij się, że masz:
- Zainstalowaną Javę 21 lub nowszą
- Maven do zarządzania zależnościami
- Konto GitHub z tokenem dostępu osobistego (PAT)
- Podstawową znajomość Javy i Spring Boot

## Zrozumienie Struktury Projektu

Projekt kalkulatora zawiera kilka ważnych plików:

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

## Wyjaśnienie Głównych Komponentów

### 1. Główna Aplikacja

**Plik:** `McpServerApplication.java`

To punkt wejścia do naszego serwisu kalkulatora. Jest to standardowa aplikacja Spring Boot z jednym specjalnym dodatkiem:

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

**Co to robi:**
- Uruchamia serwer webowy Spring Boot na porcie 8080
- Tworzy `ToolCallbackProvider`, który udostępnia metody kalkulatora jako narzędzia MCP
- Adnotacja `@Bean` informuje Spring, aby zarządzał tym jako komponentem dostępnym dla innych części

### 2. Serwis Kalkulatora

**Plik:** `CalculatorService.java`

Tutaj odbywają się wszystkie obliczenia. Każda metoda jest oznaczona adnotacją `@Tool`, aby była dostępna przez MCP:

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

**Kluczowe cechy:**

1. **Adnotacja `@Tool`**: Informuje MCP, że metoda może być wywoływana przez zewnętrznych klientów
2. **Jasne Opisy**: Każde narzędzie ma opis, który pomaga modelom AI zrozumieć, kiedy go użyć
3. **Spójny Format Zwracanych Danych**: Wszystkie operacje zwracają czytelne dla człowieka ciągi, np. "5.00 + 3.00 = 8.00"
4. **Obsługa Błędów**: Dzielenie przez zero i pierwiastki kwadratowe z liczb ujemnych zwracają komunikaty o błędach

**Dostępne Operacje:**
- `add(a, b)` - Dodaje dwie liczby
- `subtract(a, b)` - Odejmuje drugą liczbę od pierwszej
- `multiply(a, b)` - Mnoży dwie liczby
- `divide(a, b)` - Dzieli pierwszą liczbę przez drugą (z kontrolą dzielenia przez zero)
- `power(base, exponent)` - Podnosi podstawę do potęgi
- `squareRoot(number)` - Oblicza pierwiastek kwadratowy (z kontrolą liczb ujemnych)
- `modulus(a, b)` - Zwraca resztę z dzielenia
- `absolute(number)` - Zwraca wartość bezwzględną
- `help()` - Zwraca informacje o wszystkich operacjach

### 3. Bezpośredni Klient MCP

**Plik:** `SDKClient.java`

Ten klient komunikuje się bezpośrednio z serwerem MCP, bez użycia AI. Ręcznie wywołuje konkretne funkcje kalkulatora:

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

**Co to robi:**
1. **Łączy się** z serwerem kalkulatora pod adresem `http://localhost:8080`
2. **Wypisuje** wszystkie dostępne narzędzia (nasze funkcje kalkulatora)
3. **Wywołuje** konkretne funkcje z dokładnymi parametrami
4. **Wyświetla** wyniki bezpośrednio

**Kiedy tego używać:** Gdy dokładnie wiesz, jakie obliczenie chcesz wykonać i chcesz je wywołać programowo.

### 4. Klient Wspierany przez AI

**Plik:** `LangChain4jClient.java`

Ten klient korzysta z modelu AI (GPT-4o-mini), który automatycznie decyduje, których narzędzi kalkulatora użyć:

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

**Co to robi:**
1. **Tworzy** połączenie z modelem AI, używając Twojego tokena GitHub
2. **Łączy** AI z naszym serwerem MCP kalkulatora
3. **Udostępnia** AI wszystkie narzędzia kalkulatora
4. **Pozwala** na naturalne zapytania, np. "Oblicz sumę 24.5 i 17.3"

**AI automatycznie:**
- Rozumie, że chcesz dodać liczby
- Wybiera narzędzie `add`
- Wywołuje `add(24.5, 17.3)`
- Zwraca wynik w naturalnej odpowiedzi

## Uruchamianie Przykładów

### Krok 1: Uruchom Serwer Kalkulatora

Najpierw ustaw swój token GitHub (potrzebny dla klienta AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Uruchom serwer:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Serwer uruchomi się pod adresem `http://localhost:8080`. Powinieneś zobaczyć:
```
Started McpServerApplication in X.XXX seconds
```

### Krok 2: Test z Bezpośrednim Klientem

W nowym terminalu:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Zobaczysz wynik podobny do:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Krok 3: Test z Klientem AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Zobaczysz, jak AI automatycznie używa narzędzi:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Jak Wszystko Razem Działa

Oto pełny przepływ, gdy pytasz AI "Ile to 5 + 3?":

1. **Ty** pytasz AI w języku naturalnym
2. **AI** analizuje Twoje zapytanie i rozumie, że chcesz dodać liczby
3. **AI** wywołuje serwer MCP: `add(5.0, 3.0)`
4. **Serwis Kalkulatora** wykonuje: `5.0 + 3.0 = 8.0`
5. **Serwis Kalkulatora** zwraca: `"5.00 + 3.00 = 8.00"`
6. **AI** otrzymuje wynik i formatuje naturalną odpowiedź
7. **Ty** otrzymujesz: "Suma 5 i 3 wynosi 8"

## Kolejne Kroki

Więcej przykładów znajdziesz w [Rozdziale 04: Praktyczne przykłady](../../README.md)

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż staramy się zapewnić dokładność, prosimy mieć na uwadze, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego rodzimym języku powinien być uznawany za wiarygodne źródło. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.