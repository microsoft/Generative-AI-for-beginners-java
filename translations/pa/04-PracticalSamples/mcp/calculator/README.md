<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:17:53+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "pa"
}
-->
# MCP ਕੈਲਕੁਲੇਟਰ ਟਿਊਟੋਰਿਅਲ ਸ਼ੁਰੂਆਤੀ ਲਈ

## ਸੂਚੀ

- [ਤੁਹਾਨੂੰ ਕੀ ਸਿੱਖਣ ਨੂੰ ਮਿਲੇਗਾ](../../../../../04-PracticalSamples/mcp/calculator)
- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../../../04-PracticalSamples/mcp/calculator)
- [ਪ੍ਰੋਜੈਕਟ ਦੀ ਬਣਤਰ ਨੂੰ ਸਮਝਣਾ](../../../../../04-PracticalSamples/mcp/calculator)
- [ਮੁੱਖ ਹਿੱਸਿਆਂ ਦੀ ਵਿਆਖਿਆ](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. ਡਾਇਰੈਕਟ MCP ਕਲਾਇੰਟ](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-ਚਲਿਤ ਕਲਾਇੰਟ](../../../../../04-PracticalSamples/mcp/calculator)
- [ਉਦਾਹਰਣ ਚਲਾਉਣਾ](../../../../../04-PracticalSamples/mcp/calculator)
- [ਇਹ ਸਭ ਕੁਝ ਕਿਵੇਂ ਇਕੱਠੇ ਕੰਮ ਕਰਦਾ ਹੈ](../../../../../04-PracticalSamples/mcp/calculator)
- [ਅਗਲੇ ਕਦਮ](../../../../../04-PracticalSamples/mcp/calculator)

## ਤੁਹਾਨੂੰ ਕੀ ਸਿੱਖਣ ਨੂੰ ਮਿਲੇਗਾ

ਇਹ ਟਿਊਟੋਰਿਅਲ ਦੱਸਦਾ ਹੈ ਕਿ ਮਾਡਲ ਕਾਂਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ (MCP) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ ਕਿਵੇਂ ਬਣਾਈ ਜਾ ਸਕਦੀ ਹੈ। ਤੁਸੀਂ ਸਮਝੋਗੇ:

- AI ਨੂੰ ਸੰਦ ਵਜੋਂ ਵਰਤਣ ਲਈ ਸੇਵਾ ਕਿਵੇਂ ਬਣਾਈ ਜਾਵੇ
- MCP ਸੇਵਾਵਾਂ ਨਾਲ ਸਿੱਧੀ ਸੰਚਾਰ ਸੈਟਅੱਪ ਕਿਵੇਂ ਕੀਤਾ ਜਾਵੇ
- AI ਮਾਡਲ ਕਿਵੇਂ ਆਪਣੇ ਆਪ ਫੈਸਲਾ ਕਰਦੇ ਹਨ ਕਿ ਕਿਹੜੇ ਸੰਦ ਵਰਤਣੇ ਹਨ
- ਸਿੱਧੇ ਪ੍ਰੋਟੋਕੋਲ ਕਾਲਾਂ ਅਤੇ AI-ਸਹਾਇਕ ਇੰਟਰੈਕਸ਼ਨ ਵਿੱਚ ਅੰਤਰ

## ਪੂਰਵ ਸ਼ਰਤਾਂ

ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਇਹ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ ਹੈ:
- Java 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ ਵਰਜਨ ਇੰਸਟਾਲ ਹੈ
- Maven ਡਿਪੈਂਡੈਂਸੀ ਮੈਨੇਜਮੈਂਟ ਲਈ
- GitHub ਖਾਤਾ ਅਤੇ ਪੈਰਸਨਲ ਐਕਸੈਸ ਟੋਕਨ (PAT)
- Java ਅਤੇ Spring Boot ਦੀ ਬੁਨਿਆਦੀ ਸਮਝ

## ਪ੍ਰੋਜੈਕਟ ਦੀ ਬਣਤਰ ਨੂੰ ਸਮਝਣਾ

ਕੈਲਕੁਲੇਟਰ ਪ੍ਰੋਜੈਕਟ ਵਿੱਚ ਕੁਝ ਮਹੱਤਵਪੂਰਨ ਫਾਈਲਾਂ ਹਨ:

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

## ਮੁੱਖ ਹਿੱਸਿਆਂ ਦੀ ਵਿਆਖਿਆ

### 1. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ

**ਫਾਈਲ:** `McpServerApplication.java`

ਇਹ ਸਾਡੇ ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ ਦਾ ਐਂਟਰੀ ਪੌਇੰਟ ਹੈ। ਇਹ ਇੱਕ ਸਧਾਰਨ Spring Boot ਐਪਲੀਕੇਸ਼ਨ ਹੈ ਜਿਸ ਵਿੱਚ ਇੱਕ ਵਿਸ਼ੇਸ਼ ਸ਼ਾਮਲ ਹੈ:

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

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- Spring Boot ਵੈੱਬ ਸਰਵਰ ਨੂੰ ਪੋਰਟ 8080 'ਤੇ ਸ਼ੁਰੂ ਕਰਦਾ ਹੈ
- `ToolCallbackProvider` ਬਣਾਉਂਦਾ ਹੈ ਜੋ ਸਾਡੇ ਕੈਲਕੁਲੇਟਰ ਮੈਥਡ ਨੂੰ MCP ਸੰਦ ਵਜੋਂ ਉਪਲਬਧ ਕਰਵਾਉਂਦਾ ਹੈ
- `@Bean` ਐਨੋਟੇਸ਼ਨ Spring ਨੂੰ ਇਸਨੂੰ ਇੱਕ ਕੰਪੋਨੈਂਟ ਵਜੋਂ ਮੈਨੇਜ ਕਰਨ ਲਈ ਦੱਸਦਾ ਹੈ ਜੋ ਹੋਰ ਹਿੱਸੇ ਵਰਤ ਸਕਦੇ ਹਨ

### 2. ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ

**ਫਾਈਲ:** `CalculatorService.java`

ਇਹ ਜਿੱਥੇ ਸਾਰਾ ਗਣਿਤ ਹੁੰਦਾ ਹੈ। ਹਰ ਮੈਥਡ `@Tool` ਨਾਲ ਚਿੰਨ੍ਹਿਤ ਹੈ ਤਾਂ ਜੋ ਇਹ MCP ਰਾਹੀਂ ਉਪਲਬਧ ਹੋ ਸਕੇ:

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

**ਮੁੱਖ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ:**

1. **`@Tool` ਐਨੋਟੇਸ਼ਨ**: ਇਹ MCP ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਇਹ ਮੈਥਡ ਬਾਹਰੀ ਕਲਾਇੰਟਾਂ ਦੁਆਰਾ ਕਾਲ ਕੀਤਾ ਜਾ ਸਕਦਾ ਹੈ
2. **ਸਪਸ਼ਟ ਵਰਣਨ**: ਹਰ ਸੰਦ ਦਾ ਵਰਣਨ ਹੈ ਜੋ AI ਮਾਡਲ ਨੂੰ ਸਮਝਣ ਵਿੱਚ ਮਦਦ ਕਰਦਾ ਹੈ ਕਿ ਕਦੋਂ ਇਸਨੂੰ ਵਰਤਣਾ ਹੈ
3. **ਸਥਿਰ ਰਿਟਰਨ ਫਾਰਮੈਟ**: ਸਾਰੇ ਓਪਰੇਸ਼ਨ ਮਨੁੱਖ-ਪੜ੍ਹਨਯੋਗ ਸਤਰਾਂ ਵਾਪਸ ਕਰਦੇ ਹਨ ਜਿਵੇਂ "5.00 + 3.00 = 8.00"
4. **ਗਲਤੀ ਸੰਭਾਲ**: ਜ਼ੀਰੋ ਨਾਲ ਭਾਗ ਅਤੇ ਨਕਾਰਾਤਮਕ ਵਰਗਮੂਲ ਗਲਤੀ ਸੁਨੇਹੇ ਵਾਪਸ ਕਰਦੇ ਹਨ

**ਉਪਲਬਧ ਓਪਰੇਸ਼ਨ:**
- `add(a, b)` - ਦੋ ਨੰਬਰ ਜੋੜਦਾ ਹੈ
- `subtract(a, b)` - ਦੂਜੇ ਨੂੰ ਪਹਿਲੇ ਤੋਂ ਘਟਾਉਂਦਾ ਹੈ
- `multiply(a, b)` - ਦੋ ਨੰਬਰ ਗੁਣਾ ਕਰਦਾ ਹੈ
- `divide(a, b)` - ਪਹਿਲੇ ਨੂੰ ਦੂਜੇ ਨਾਲ ਭਾਗ ਕਰਦਾ ਹੈ (ਜ਼ੀਰੋ-ਚੈੱਕ ਨਾਲ)
- `power(base, exponent)` - ਬੇਸ ਨੂੰ ਐਕਸਪੋਨੈਂਟ ਦੇ ਪਾਵਰ 'ਤੇ ਲੈ ਜਾਂਦਾ ਹੈ
- `squareRoot(number)` - ਵਰਗਮੂਲ ਕੈਲਕੁਲੇਟ ਕਰਦਾ ਹੈ (ਨਕਾਰਾਤਮਕ ਚੈੱਕ ਨਾਲ)
- `modulus(a, b)` - ਭਾਗ ਦਾ ਬਾਕੀ ਵਾਪਸ ਕਰਦਾ ਹੈ
- `absolute(number)` - ਐਬਸੋਲਿਊਟ ਵੈਲਯੂ ਵਾਪਸ ਕਰਦਾ ਹੈ
- `help()` - ਸਾਰੇ ਓਪਰੇਸ਼ਨ ਬਾਰੇ ਜਾਣਕਾਰੀ ਵਾਪਸ ਕਰਦਾ ਹੈ

### 3. ਡਾਇਰੈਕਟ MCP ਕਲਾਇੰਟ

**ਫਾਈਲ:** `SDKClient.java`

ਇਹ ਕਲਾਇੰਟ AI ਦੀ ਵਰਤੋਂ ਕੀਤੇ ਬਿਨਾਂ MCP ਸਰਵਰ ਨਾਲ ਸਿੱਧਾ ਗੱਲ ਕਰਦਾ ਹੈ। ਇਹ ਖਾਸ ਕੈਲਕੁਲੇਟਰ ਫੰਕਸ਼ਨ ਨੂੰ ਮੈਨੂਅਲੀ ਕਾਲ ਕਰਦਾ ਹੈ:

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

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
1. **ਕਨੈਕਟ** ਕਰਦਾ ਹੈ ਕੈਲਕੁਲੇਟਰ ਸਰਵਰ ਨਾਲ `http://localhost:8080` 'ਤੇ
2. **ਸੂਚੀ** ਕਰਦਾ ਹੈ ਸਾਰੇ ਉਪਲਬਧ ਸੰਦਾਂ ਦੀ (ਸਾਡੇ ਕੈਲਕੁਲੇਟਰ ਫੰਕਸ਼ਨ)
3. **ਕਾਲ** ਕਰਦਾ ਹੈ ਖਾਸ ਫੰਕਸ਼ਨ ਨੂੰ ਸਹੀ ਪੈਰਾਮੀਟਰਾਂ ਨਾਲ
4. **ਨਤੀਜੇ** ਸਿੱਧੇ ਪ੍ਰਿੰਟ ਕਰਦਾ ਹੈ

**ਇਸਨੂੰ ਕਦੋਂ ਵਰਤਣਾ ਹੈ:** ਜਦੋਂ ਤੁਹਾਨੂੰ ਪੂਰੀ ਤਰ੍ਹਾਂ ਪਤਾ ਹੋਵੇ ਕਿ ਕਿਹੜੀ ਗਣਨਾ ਕਰਨੀ ਹੈ ਅਤੇ ਇਸਨੂੰ ਪ੍ਰੋਗਰਾਮਿੰਗ ਤਰੀਕੇ ਨਾਲ ਕਾਲ ਕਰਨਾ ਹੈ।

### 4. AI-ਚਲਿਤ ਕਲਾਇੰਟ

**ਫਾਈਲ:** `LangChain4jClient.java`

ਇਹ ਕਲਾਇੰਟ ਇੱਕ AI ਮਾਡਲ (GPT-4o-mini) ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ ਜੋ ਆਪਣੇ ਆਪ ਫੈਸਲਾ ਕਰ ਸਕਦਾ ਹੈ ਕਿ ਕਿਹੜੇ ਕੈਲਕੁਲੇਟਰ ਸੰਦ ਵਰਤਣੇ ਹਨ:

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

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
1. **AI ਮਾਡਲ** ਕਨੈਕਸ਼ਨ ਬਣਾਉਂਦਾ ਹੈ ਤੁਹਾਡੇ GitHub ਟੋਕਨ ਦੀ ਵਰਤੋਂ ਕਰਕੇ
2. **ਕਨੈਕਟ** ਕਰਦਾ ਹੈ AI ਨੂੰ ਸਾਡੇ ਕੈਲਕੁਲੇਟਰ MCP ਸਰਵਰ ਨਾਲ
3. **AI ਨੂੰ** ਸਾਰੇ ਕੈਲਕੁਲੇਟਰ ਸੰਦਾਂ ਤੱਕ ਪਹੁੰਚ ਦਿੰਦਾ ਹੈ
4. **ਕੁਦਰਤੀ ਭਾਸ਼ਾ** ਵਿੱਚ ਬੇਨਤੀ ਕਰਨ ਦੀ ਆਗਿਆ ਦਿੰਦਾ ਹੈ ਜਿਵੇਂ "24.5 ਅਤੇ 17.3 ਦਾ ਜੋੜ ਕੈਲਕੁਲੇਟ ਕਰੋ"

**AI ਆਪਣੇ ਆਪ:**
- ਸਮਝਦਾ ਹੈ ਕਿ ਤੁਸੀਂ ਨੰਬਰ ਜੋੜਨਾ ਚਾਹੁੰਦੇ ਹੋ
- `add` ਸੰਦ ਚੁਣਦਾ ਹੈ
- `add(24.5, 17.3)` ਕਾਲ ਕਰਦਾ ਹੈ
- ਨਤੀਜਾ ਕੁਦਰਤੀ ਜਵਾਬ ਵਿੱਚ ਵਾਪਸ ਕਰਦਾ ਹੈ

## ਉਦਾਹਰਣ ਚਲਾਉਣਾ

### ਪਹਲਾ ਕਦਮ: ਕੈਲਕੁਲੇਟਰ ਸਰਵਰ ਸ਼ੁਰੂ ਕਰੋ

ਪਹਿਲਾਂ, ਆਪਣਾ GitHub ਟੋਕਨ ਸੈਟ ਕਰੋ (AI ਕਲਾਇੰਟ ਲਈ ਲੋੜੀਂਦਾ):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

ਸਰਵਰ ਸ਼ੁਰੂ ਕਰੋ:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

ਸਰਵਰ `http://localhost:8080` 'ਤੇ ਸ਼ੁਰੂ ਹੋਵੇਗਾ। ਤੁਸੀਂ ਇਹ ਦੇਖੋਗੇ:
```
Started McpServerApplication in X.XXX seconds
```

### ਦੂਜਾ ਕਦਮ: ਡਾਇਰੈਕਟ ਕਲਾਇੰਟ ਨਾਲ ਟੈਸਟ ਕਰੋ

ਨਵੀਂ ਟਰਮੀਨਲ ਵਿੱਚ:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

ਤੁਸੀਂ ਇਹ ਆਉਟਪੁੱਟ ਦੇਖੋਗੇ:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### ਤੀਜਾ ਕਦਮ: AI ਕਲਾਇੰਟ ਨਾਲ ਟੈਸਟ ਕਰੋ

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

ਤੁਸੀਂ ਦੇਖੋਗੇ ਕਿ AI ਆਪਣੇ ਆਪ ਸੰਦ ਵਰਤ ਰਿਹਾ ਹੈ:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## ਇਹ ਸਭ ਕੁਝ ਕਿਵੇਂ ਇਕੱਠੇ ਕੰਮ ਕਰਦਾ ਹੈ

ਜਦੋਂ ਤੁਸੀਂ AI ਨੂੰ "5 + 3 ਕੀ ਹੈ?" ਪੁੱਛਦੇ ਹੋ ਤਾਂ ਪੂਰਾ ਪ੍ਰਕਿਰਿਆ ਇਹ ਹੈ:

1. **ਤੁਸੀਂ** AI ਨੂੰ ਕੁਦਰਤੀ ਭਾਸ਼ਾ ਵਿੱਚ ਪੁੱਛਦੇ ਹੋ
2. **AI** ਤੁਹਾਡੀ ਬੇਨਤੀ ਦਾ ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਦਾ ਹੈ ਅਤੇ ਸਮਝਦਾ ਹੈ ਕਿ ਤੁਸੀਂ ਜੋੜਨਾ ਚਾਹੁੰਦੇ ਹੋ
3. **AI** MCP ਸਰਵਰ ਨੂੰ ਕਾਲ ਕਰਦਾ ਹੈ: `add(5.0, 3.0)`
4. **ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ** ਇਹ ਗਣਨਾ ਕਰਦੀ ਹੈ: `5.0 + 3.0 = 8.0`
5. **ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ** ਵਾਪਸ ਕਰਦੀ ਹੈ: `"5.00 + 3.00 = 8.00"`
6. **AI** ਨਤੀਜਾ ਪ੍ਰਾਪਤ ਕਰਦਾ ਹੈ ਅਤੇ ਕੁਦਰਤੀ ਜਵਾਬ ਵਿੱਚ ਫਾਰਮੈਟ ਕਰਦਾ ਹੈ
7. **ਤੁਹਾਨੂੰ** ਮਿਲਦਾ ਹੈ: "5 ਅਤੇ 3 ਦਾ ਜੋੜ 8 ਹੈ"

## ਅਗਲੇ ਕਦਮ

ਹੋਰ ਉਦਾਹਰਣਾਂ ਲਈ, ਵੇਖੋ [ਚੈਪਟਰ 04: ਪ੍ਰੈਕਟਿਕਲ ਸੈਂਪਲ](../../README.md)

**ਅਸਵੀਕਾਰਨਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁਚਨਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।