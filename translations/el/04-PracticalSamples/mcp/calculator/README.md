<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:33:32+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "el"
}
-->
# Οδηγός Χρήσης MCP Calculator για Αρχάριους

## Πίνακας Περιεχομένων

- [Τι Θα Μάθετε](../../../../../04-PracticalSamples/mcp/calculator)
- [Προαπαιτούμενα](../../../../../04-PracticalSamples/mcp/calculator)
- [Κατανόηση της Δομής του Έργου](../../../../../04-PracticalSamples/mcp/calculator)
- [Επεξήγηση Κύριων Στοιχείων](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Κύρια Εφαρμογή](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Υπηρεσία Υπολογιστή](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Άμεσος MCP Client](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Client με Τεχνητή Νοημοσύνη](../../../../../04-PracticalSamples/mcp/calculator)
- [Εκτέλεση των Παραδειγμάτων](../../../../../04-PracticalSamples/mcp/calculator)
- [Πώς Όλα Λειτουργούν Μαζί](../../../../../04-PracticalSamples/mcp/calculator)
- [Επόμενα Βήματα](../../../../../04-PracticalSamples/mcp/calculator)

## Τι Θα Μάθετε

Αυτός ο οδηγός εξηγεί πώς να δημιουργήσετε μια υπηρεσία υπολογιστή χρησιμοποιώντας το Model Context Protocol (MCP). Θα κατανοήσετε:

- Πώς να δημιουργήσετε μια υπηρεσία που μπορεί να χρησιμοποιηθεί από την Τεχνητή Νοημοσύνη ως εργαλείο
- Πώς να ρυθμίσετε άμεση επικοινωνία με υπηρεσίες MCP
- Πώς τα μοντέλα Τεχνητής Νοημοσύνης μπορούν να επιλέγουν αυτόματα ποια εργαλεία να χρησιμοποιούν
- Τη διαφορά μεταξύ άμεσων κλήσεων πρωτοκόλλου και αλληλεπιδράσεων με τη βοήθεια Τεχνητής Νοημοσύνης

## Προαπαιτούμενα

Πριν ξεκινήσετε, βεβαιωθείτε ότι έχετε:
- Εγκατεστημένο Java 21 ή νεότερο
- Το Maven για διαχείριση εξαρτήσεων
- Έναν λογαριασμό GitHub με προσωπικό access token (PAT)
- Βασική κατανόηση της Java και του Spring Boot

## Κατανόηση της Δομής του Έργου

Το έργο του υπολογιστή περιλαμβάνει αρκετά σημαντικά αρχεία:

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

## Επεξήγηση Κύριων Στοιχείων

### 1. Κύρια Εφαρμογή

**Αρχείο:** `McpServerApplication.java`

Αυτό είναι το σημείο εκκίνησης της υπηρεσίας υπολογιστή μας. Είναι μια τυπική εφαρμογή Spring Boot με μια ειδική προσθήκη:

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

**Τι κάνει:**
- Εκκινεί έναν web server Spring Boot στη θύρα 8080
- Δημιουργεί έναν `ToolCallbackProvider` που καθιστά τις μεθόδους του υπολογιστή μας διαθέσιμες ως εργαλεία MCP
- Το `@Bean` λέει στο Spring να διαχειριστεί αυτό ως ένα component που μπορούν να χρησιμοποιήσουν άλλα μέρη

### 2. Υπηρεσία Υπολογιστή

**Αρχείο:** `CalculatorService.java`

Εδώ πραγματοποιούνται όλοι οι μαθηματικοί υπολογισμοί. Κάθε μέθοδος επισημαίνεται με `@Tool` για να είναι διαθέσιμη μέσω MCP:

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

**Κύρια χαρακτηριστικά:**

1. **Σχολιασμός `@Tool`**: Δηλώνει στο MCP ότι αυτή η μέθοδος μπορεί να κληθεί από εξωτερικούς πελάτες
2. **Σαφείς Περιγραφές**: Κάθε εργαλείο έχει μια περιγραφή που βοηθά τα μοντέλα Τεχνητής Νοημοσύνης να κατανοούν πότε να το χρησιμοποιούν
3. **Συνεπής Μορφή Επιστροφής**: Όλες οι λειτουργίες επιστρέφουν ευανάγνωστες συμβολοσειρές όπως "5.00 + 3.00 = 8.00"
4. **Διαχείριση Σφαλμάτων**: Η διαίρεση με το μηδέν και οι αρνητικές τετραγωνικές ρίζες επιστρέφουν μηνύματα σφάλματος

**Διαθέσιμες Λειτουργίες:**
- `add(a, b)` - Προσθέτει δύο αριθμούς
- `subtract(a, b)` - Αφαιρεί τον δεύτερο από τον πρώτο
- `multiply(a, b)` - Πολλαπλασιάζει δύο αριθμούς
- `divide(a, b)` - Διαιρεί τον πρώτο με τον δεύτερο (με έλεγχο μηδενικού διαιρέτη)
- `power(base, exponent)` - Υψώνει τη βάση στη δύναμη του εκθέτη
- `squareRoot(number)` - Υπολογίζει την τετραγωνική ρίζα (με έλεγχο αρνητικών αριθμών)
- `modulus(a, b)` - Επιστρέφει το υπόλοιπο της διαίρεσης
- `absolute(number)` - Επιστρέφει την απόλυτη τιμή
- `help()` - Επιστρέφει πληροφορίες για όλες τις λειτουργίες

### 3. Άμεσος MCP Client

**Αρχείο:** `SDKClient.java`

Αυτός ο client επικοινωνεί απευθείας με τον MCP server χωρίς τη χρήση Τεχνητής Νοημοσύνης. Καλεί χειροκίνητα συγκεκριμένες λειτουργίες του υπολογιστή:

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

**Τι κάνει:**
1. **Συνδέεται** με τον server του υπολογιστή στο `http://localhost:8080`
2. **Λίστα** όλων των διαθέσιμων εργαλείων (των λειτουργιών του υπολογιστή μας)
3. **Καλεί** συγκεκριμένες λειτουργίες με ακριβείς παραμέτρους
4. **Εκτυπώνει** τα αποτελέσματα απευθείας

**Πότε να το χρησιμοποιήσετε:** Όταν γνωρίζετε ακριβώς ποιον υπολογισμό θέλετε να εκτελέσετε και θέλετε να τον καλέσετε προγραμματιστικά.

### 4. Client με Τεχνητή Νοημοσύνη

**Αρχείο:** `LangChain4jClient.java`

Αυτός ο client χρησιμοποιεί ένα μοντέλο Τεχνητής Νοημοσύνης (GPT-4o-mini) που μπορεί να αποφασίζει αυτόματα ποια εργαλεία του υπολογιστή να χρησιμοποιήσει:

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

**Τι κάνει:**
1. **Δημιουργεί** σύνδεση με μοντέλο Τεχνητής Νοημοσύνης χρησιμοποιώντας το GitHub token σας
2. **Συνδέει** την Τεχνητή Νοημοσύνη με τον MCP server του υπολογιστή μας
3. **Παρέχει** στην Τεχνητή Νοημοσύνη πρόσβαση σε όλα τα εργαλεία του υπολογιστή μας
4. **Επιτρέπει** φυσικές γλωσσικές αιτήσεις όπως "Υπολόγισε το άθροισμα του 24.5 και του 17.3"

**Η Τεχνητή Νοημοσύνη αυτόματα:**
- Κατανοεί ότι θέλετε να προσθέσετε αριθμούς
- Επιλέγει το εργαλείο `add`
- Καλεί το `add(24.5, 17.3)`
- Επιστρέφει το αποτέλεσμα σε φυσική απάντηση

## Εκτέλεση των Παραδειγμάτων

### Βήμα 1: Εκκίνηση του Server του Υπολογιστή

Αρχικά, ορίστε το GitHub token σας (απαραίτητο για τον client Τεχνητής Νοημοσύνης):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Εκκινήστε τον server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Ο server θα ξεκινήσει στο `http://localhost:8080`. Θα δείτε:
```
Started McpServerApplication in X.XXX seconds
```

### Βήμα 2: Δοκιμή με Άμεσο Client

Σε ένα νέο τερματικό:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Θα δείτε έξοδο όπως:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Βήμα 3: Δοκιμή με Client Τεχνητής Νοημοσύνης

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Θα δείτε την Τεχνητή Νοημοσύνη να χρησιμοποιεί αυτόματα τα εργαλεία:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Πώς Όλα Λειτουργούν Μαζί

Ακολουθεί η πλήρης ροή όταν ρωτάτε την Τεχνητή Νοημοσύνη "Πόσο κάνει 5 + 3;":

1. **Εσείς** ρωτάτε την Τεχνητή Νοημοσύνη σε φυσική γλώσσα
2. **Η Τεχνητή Νοημοσύνη** αναλύει το αίτημά σας και καταλαβαίνει ότι θέλετε πρόσθεση
3. **Η Τεχνητή Νοημοσύνη** καλεί τον MCP server: `add(5.0, 3.0)`
4. **Η Υπηρεσία Υπολογιστή** εκτελεί: `5.0 + 3.0 = 8.0`
5. **Η Υπηρεσία Υπολογιστή** επιστρέφει: `"5.00 + 3.00 = 8.00"`
6. **Η Τεχνητή Νοημοσύνη** λαμβάνει το αποτέλεσμα και το διαμορφώνει σε φυσική απάντηση
7. **Εσείς** λαμβάνετε: "Το άθροισμα του 5 και του 3 είναι 8"

## Επόμενα Βήματα

Για περισσότερα παραδείγματα, δείτε [Κεφάλαιο 04: Πρακτικά παραδείγματα](../../README.md)

**Αποποίηση ευθύνης**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που καταβάλλουμε προσπάθειες για ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν σφάλματα ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα θα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή εσφαλμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.