<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:12:21+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "ro"
}
-->
# Tutorial MCP Calculator pentru Începători

## Cuprins

- [Ce Vei Învăța](../../../../../04-PracticalSamples/mcp/calculator)
- [Cerințe Prealabile](../../../../../04-PracticalSamples/mcp/calculator)
- [Înțelegerea Structurii Proiectului](../../../../../04-PracticalSamples/mcp/calculator)
- [Componentele de Bază Explicate](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Aplicația Principală](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Serviciul Calculator](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Client MCP Direct](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Client Bazat pe AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Rularea Exemplului](../../../../../04-PracticalSamples/mcp/calculator)
- [Cum Funcționează Împreună](../../../../../04-PracticalSamples/mcp/calculator)
- [Pașii Următori](../../../../../04-PracticalSamples/mcp/calculator)

## Ce Vei Învăța

Acest tutorial explică cum să construiești un serviciu de calculator folosind Model Context Protocol (MCP). Vei înțelege:

- Cum să creezi un serviciu pe care AI îl poate folosi ca unealtă
- Cum să configurezi comunicarea directă cu serviciile MCP
- Cum modelele AI pot alege automat ce unelte să folosească
- Diferența dintre apelurile directe de protocol și interacțiunile asistate de AI

## Cerințe Prealabile

Înainte de a începe, asigură-te că ai:
- Java 21 sau o versiune mai nouă instalată
- Maven pentru gestionarea dependențelor
- Un cont GitHub cu un token de acces personal (PAT)
- Cunoștințe de bază despre Java și Spring Boot

## Înțelegerea Structurii Proiectului

Proiectul calculatorului conține mai multe fișiere importante:

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

## Componentele de Bază Explicate

### 1. Aplicația Principală

**Fișier:** `McpServerApplication.java`

Acesta este punctul de intrare al serviciului nostru de calculator. Este o aplicație standard Spring Boot cu o adăugire specială:

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

**Ce face:**
- Pornește un server web Spring Boot pe portul 8080
- Creează un `ToolCallbackProvider` care face metodele calculatorului disponibile ca unelte MCP
- Anotarea `@Bean` spune Spring să gestioneze acest lucru ca un component pe care alte părți îl pot folosi

### 2. Serviciul Calculator

**Fișier:** `CalculatorService.java`

Aici se realizează toate calculele. Fiecare metodă este marcată cu `@Tool` pentru a fi disponibilă prin MCP:

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

**Caracteristici cheie:**

1. **Anotarea `@Tool`**: Indică MCP că această metodă poate fi apelată de clienți externi
2. **Descrieri Clare**: Fiecare unealtă are o descriere care ajută modelele AI să înțeleagă când să o folosească
3. **Format Consistent al Rezultatelor**: Toate operațiile returnează șiruri de caractere ușor de citit, precum "5.00 + 3.00 = 8.00"
4. **Gestionarea Erorilor**: Împărțirea la zero și rădăcinile pătrate negative returnează mesaje de eroare

**Operații Disponibile:**
- `add(a, b)` - Adună două numere
- `subtract(a, b)` - Scade al doilea număr din primul
- `multiply(a, b)` - Înmulțește două numere
- `divide(a, b)` - Împarte primul număr la al doilea (cu verificare pentru zero)
- `power(base, exponent)` - Ridică baza la puterea exponentului
- `squareRoot(number)` - Calculează rădăcina pătrată (cu verificare pentru numere negative)
- `modulus(a, b)` - Returnează restul împărțirii
- `absolute(number)` - Returnează valoarea absolută
- `help()` - Returnează informații despre toate operațiile

### 3. Client MCP Direct

**Fișier:** `SDKClient.java`

Acest client comunică direct cu serverul MCP fără a folosi AI. Apelează manual funcțiile specifice ale calculatorului:

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

**Ce face:**
1. **Se conectează** la serverul calculatorului la `http://localhost:8080`
2. **Listează** toate uneltele disponibile (funcțiile calculatorului nostru)
3. **Apelează** funcții specifice cu parametri exacți
4. **Afișează** rezultatele direct

**Când să-l folosești:** Când știi exact ce calcul vrei să efectuezi și dorești să-l apelezi programatic.

### 4. Client Bazat pe AI

**Fișier:** `LangChain4jClient.java`

Acest client folosește un model AI (GPT-4o-mini) care poate decide automat ce unelte ale calculatorului să folosească:

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

**Ce face:**
1. **Creează** o conexiune cu modelul AI folosind token-ul tău GitHub
2. **Conectează** AI-ul la serverul MCP al calculatorului nostru
3. **Oferă** AI-ului acces la toate uneltele calculatorului
4. **Permite** cereri în limbaj natural, precum "Calculează suma dintre 24.5 și 17.3"

**AI-ul automat:**
- Înțelege că vrei să aduni numere
- Alege unealta `add`
- Apelează `add(24.5, 17.3)`
- Returnează rezultatul într-un răspuns natural

## Rularea Exemplului

### Pasul 1: Pornește Serverul Calculatorului

Mai întâi, setează token-ul GitHub (necesar pentru clientul AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Pornește serverul:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Serverul va porni la `http://localhost:8080`. Ar trebui să vezi:
```
Started McpServerApplication in X.XXX seconds
```

### Pasul 2: Testează cu Clientul Direct

Într-un terminal nou:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Vei vedea un output precum:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Pasul 3: Testează cu Clientul AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Vei vedea AI-ul folosind automat uneltele:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Cum Funcționează Împreună

Iată fluxul complet când întrebi AI-ul "Cât fac 5 + 3?":

1. **Tu** întrebi AI-ul în limbaj natural
2. **AI-ul** analizează cererea ta și își dă seama că vrei să aduni
3. **AI-ul** apelează serverul MCP: `add(5.0, 3.0)`
4. **Serviciul Calculator** efectuează: `5.0 + 3.0 = 8.0`
5. **Serviciul Calculator** returnează: `"5.00 + 3.00 = 8.00"`
6. **AI-ul** primește rezultatul și îl formatează într-un răspuns natural
7. **Tu** primești: "Suma dintre 5 și 3 este 8"

## Pașii Următori

Pentru mai multe exemple, vezi [Capitolul 04: Exemple practice](../../README.md)

**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să fiți conștienți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa natală ar trebui considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea din utilizarea acestei traduceri.