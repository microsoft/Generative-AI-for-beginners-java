<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:01:47+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "sw"
}
-->
# Mafunzo ya MCP Calculator kwa Anayeanza

## Jedwali la Yaliyomo

- [Unachojifunza](../../../../../04-PracticalSamples/mcp/calculator)
- [Mahitaji ya Awali](../../../../../04-PracticalSamples/mcp/calculator)
- [Kuelewa Muundo wa Mradi](../../../../../04-PracticalSamples/mcp/calculator)
- [Vipengele Muhimu Vilivyoelezwa](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Programu Kuu](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Huduma ya Calculator](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Mteja wa MCP wa Moja kwa Moja](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Mteja Anayetumia AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Kuendesha Mifano](../../../../../04-PracticalSamples/mcp/calculator)
- [Jinsi Inavyofanya Kazi Pamoja](../../../../../04-PracticalSamples/mcp/calculator)
- [Hatua Zifuatazo](../../../../../04-PracticalSamples/mcp/calculator)

## Unachojifunza

Mafunzo haya yanaelezea jinsi ya kujenga huduma ya calculator kwa kutumia Model Context Protocol (MCP). Utaelewa:

- Jinsi ya kuunda huduma ambayo AI inaweza kutumia kama zana
- Jinsi ya kusanidi mawasiliano ya moja kwa moja na huduma za MCP
- Jinsi mifano ya AI inaweza kuchagua zana za kutumia kiotomatiki
- Tofauti kati ya miito ya moja kwa moja ya itifaki na mwingiliano unaosaidiwa na AI

## Mahitaji ya Awali

Kabla ya kuanza, hakikisha una:
- Java 21 au toleo la juu zaidi limewekwa
- Maven kwa usimamizi wa utegemezi
- Akaunti ya GitHub yenye tokeni ya ufikiaji wa kibinafsi (PAT)
- Uelewa wa msingi wa Java na Spring Boot

## Kuelewa Muundo wa Mradi

Mradi wa calculator una faili kadhaa muhimu:

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

## Vipengele Muhimu Vilivyoelezwa

### 1. Programu Kuu

**Faili:** `McpServerApplication.java`

Hii ni sehemu ya kuanzia ya huduma yetu ya calculator. Ni programu ya kawaida ya Spring Boot yenye nyongeza moja maalum:

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

**Inachofanya:**
- Inaendesha seva ya wavuti ya Spring Boot kwenye bandari 8080
- Inaunda `ToolCallbackProvider` inayofanya mbinu za calculator zetu zipatikane kama zana za MCP
- Anotesheni ya `@Bean` inaiambia Spring kusimamia hii kama sehemu ambayo sehemu nyingine zinaweza kutumia

### 2. Huduma ya Calculator

**Faili:** `CalculatorService.java`

Hapa ndipo hesabu zote hufanyika. Kila mbinu imewekwa alama na `@Tool` ili iweze kupatikana kupitia MCP:

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

**Vipengele Muhimu:**

1. **Anotesheni ya `@Tool`**: Hii inaiambia MCP kwamba mbinu hii inaweza kuitwa na wateja wa nje
2. **Maelezo Wazi**: Kila zana ina maelezo yanayosaidia mifano ya AI kuelewa wakati wa kuitumia
3. **Muundo wa Matokeo Thabiti**: Operesheni zote zinarudisha maandishi yanayosomeka kama "5.00 + 3.00 = 8.00"
4. **Ushughulikiaji wa Makosa**: Mgawanyiko kwa sifuri na mizizi ya mraba hasi hurudisha ujumbe wa makosa

**Operesheni Zinazopatikana:**
- `add(a, b)` - Huongeza namba mbili
- `subtract(a, b)` - Hutoa ya pili kutoka ya kwanza
- `multiply(a, b)` - Huzidisha namba mbili
- `divide(a, b)` - Hugawa ya kwanza kwa ya pili (na ukaguzi wa sifuri)
- `power(base, exponent)` - Huinua msingi kwa nguvu ya exponent
- `squareRoot(number)` - Hukokotoa mzizi wa mraba (na ukaguzi wa hasi)
- `modulus(a, b)` - Hutoa baki ya mgawanyiko
- `absolute(number)` - Hutoa thamani kamili
- `help()` - Hutoa taarifa kuhusu operesheni zote

### 3. Mteja wa MCP wa Moja kwa Moja

**Faili:** `SDKClient.java`

Mteja huyu huzungumza moja kwa moja na seva ya MCP bila kutumia AI. Huita mbinu maalum za calculator kwa mikono:

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

**Inachofanya:**
1. **Inaunganisha** na seva ya calculator kwenye `http://localhost:8080`
2. **Inaorodhesha** zana zote zinazopatikana (mbinu za calculator zetu)
3. **Inaita** mbinu maalum na vigezo sahihi
4. **Inachapisha** matokeo moja kwa moja

**Wakati wa kutumia hii:** Wakati unajua hasa hesabu unayotaka kufanya na unataka kuifanya kwa mpangilio wa programu.

### 4. Mteja Anayetumia AI

**Faili:** `LangChain4jClient.java`

Mteja huyu hutumia mfano wa AI (GPT-4o-mini) ambao unaweza kuamua kiotomatiki zana za calculator za kutumia:

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

**Inachofanya:**
1. **Inaunda** muunganisho wa mfano wa AI kwa kutumia tokeni yako ya GitHub
2. **Inaunganisha** AI na seva yetu ya MCP ya calculator
3. **Inatoa** AI ufikiaji wa zana zote za calculator zetu
4. **Inaruhusu** maombi ya lugha ya asili kama "Hesabu jumla ya 24.5 na 17.3"

**AI kiotomatiki:**
- Inaelewa unataka kuongeza namba
- Inachagua zana ya `add`
- Inaita `add(24.5, 17.3)`
- Inarudisha matokeo kwa majibu ya asili

## Kuendesha Mifano

### Hatua ya 1: Anzisha Seva ya Calculator

Kwanza, weka tokeni yako ya GitHub (inayohitajika kwa mteja wa AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Anzisha seva:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Seva itaanza kwenye `http://localhost:8080`. Unapaswa kuona:
```
Started McpServerApplication in X.XXX seconds
```

### Hatua ya 2: Jaribu na Mteja wa Moja kwa Moja

Katika terminal mpya:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Utaona matokeo kama:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Hatua ya 3: Jaribu na Mteja wa AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Utaona AI ikitumia zana kiotomatiki:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Jinsi Inavyofanya Kazi Pamoja

Hivi ndivyo mtiririko mzima unavyokuwa unapouliza AI "5 + 3 ni ngapi?":

1. **Wewe** unauliza AI kwa lugha ya asili
2. **AI** inachambua ombi lako na kugundua unataka kuongeza
3. **AI** inaita seva ya MCP: `add(5.0, 3.0)`
4. **Huduma ya Calculator** inafanya: `5.0 + 3.0 = 8.0`
5. **Huduma ya Calculator** inarudisha: `"5.00 + 3.00 = 8.00"`
6. **AI** inapokea matokeo na kuunda jibu la asili
7. **Wewe** unapokea: "Jumla ya 5 na 3 ni 8"

## Hatua Zifuatazo

Kwa mifano zaidi, angalia [Sura ya 04: Sampuli za vitendo](../../README.md)

**Kanusho**:  
Hati hii imetafsiriwa kwa kutumia huduma ya kutafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au kutokuwa sahihi. Hati ya asili katika lugha yake ya awali inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatutawajibika kwa kutoelewana au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.