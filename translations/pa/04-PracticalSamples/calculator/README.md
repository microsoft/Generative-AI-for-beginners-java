# ਸ਼ੁਰੂਆਤੀ ਲੋਕਾਂ ਲਈ MCP ਕੈਲਕੂਲੇਟਰ ਟਿਊਟੋਰਿਯਲ

## ਸਮੱਗਰੀ ਸੂਚੀ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](#ਤੁਸੀਂ-ਕੀ-ਸਿੱਖੋਗੇ)
- [ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ](#ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ)
- [ਡਿਪੈਂਡੈਂਸੀ ਵਰਜਨ](#ਡਿਪੈਂਡੈਂਸੀ-ਵਰਜਨ)
- [ਪ੍ਰੋਜੈਕਟ ਸੰਰਚਨਾ ਦੀ ਸਮਝ](#ਪ੍ਰੋਜੈਕਟ-ਸੰਰਚਨਾ-ਦੀ-ਸਮਝ)
- [ਮੁੱਖ ਘਟਕਾਂ ਦੀ ਵਿਆਖਿਆ](#ਮੁੱਖ-ਘਟਕਾਂ-ਦੀ-ਵਿਆਖਿਆ)
  - [1. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ](#1-ਮੁੱਖ-ਐਪਲੀਕੇਸ਼ਨ)
  - [2. ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ](#2-ਕੈਲਕੂਲੇਟਰ-ਸੇਵਾ)
  - [3. ਡਾਇਰੈਕਟ MCP ਕਲਾਇੰਟ](#3-ਡਾਇਰੈਕਟ-mcp-ਕਲਾਇੰਟ)
  - [4. AI-ਸਮਰੱਥ ਕਲਾਇੰਟ](#4-ai-ਸਮਰੱਥ-ਕਲਾਇੰਟ)
- [ਉਦਾਹਰਨਾਂ ਚਲਾਉਣਾ](#ਉਦਾਹਰਨਾਂ-ਚਲਾਉਣਾ)
- [ਆਫਲਾਈਨ ਟੈਸਟ](#ਆਫਲਾਈਨ-ਟੈਸਟ)
- [ਇੱਕਠੇ ਕੰਮ ਕਰਨ ਦਾ ਢੰਗ](#ਇਹ-ਸਾਰਾ-ਇਕੱਠੇ-ਕਿਵੇਂ-ਕੰਮ-ਕਰਦਾ-ਹੈ)
- [ਅਗਲੇ ਕਦਮ](#ਅਗਲੇ-ਕਦਮ)

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

ਇਹ ਟਿਊਟੋਰਿਯਲ ਦੱਸਦਾ ਹੈ ਕਿ ਕਿਵੇਂ ਮਾਡਲ ਸੰਦਰਭ ਪ੍ਰੋਟੋਕਾਲ (MCP) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ ਬਣਾਈ ਜਾਵੇ. ਤੁਸੀਂ ਸਮਝੋਗੇ:

- ਕਿਵੇਂ ਐਸੀ ਸੇਵਾ ਬਣਾਈ ਜਾਵੇ ਜਿਸ ਨੂੰ AI ਇੱਕ ਸੰਦ ਵਜੋਂ ਵਰਤ ਸਕੇ
- ਕਿਵੇਂ MCP ਸੇਵਾਵਾਂ ਨਾਲ ਸਿੱਧੀ ਸੰਚਾਰ ਸੈਟਅਪ ਕੀਤਾ ਜਾਵੇ
- ਕਿਵੇਂ AI ਮਾਡਲ ਆਪਣੇ ਆਪ ਤੈਅ ਕਰਦੇ ਹਨ ਕਿ ਕਿਹੜੇ ਸੰਦ ਵਰਤਣੇ ਹਨ
- ਸਿੱਧੇ ਪ੍ਰੋਟੋਕੋਲ ਕਾਲਾਂ ਅਤੇ AI-ਸਹਾਇਤਿਆਤਮਕ ਇੰਟਰਐਕਸ਼ਨਾਂ ਵਿਚਕਾਰ ਫਰਕ

## ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ

ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਇਹ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ ਹੈ:
- ਜਾਵਾ 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ ਇੰਸਟਾਲ ਕੀਤਾ ਹੋਇਆ
- Maven ਡਿਪੈਂਡੈਂਸੀ ਪ੍ਰਬੰਧਨ ਲਈ
- ਜਾਵਾ ਅਤੇ ਸਪ੍ਰਿੰਗ ਬੂਟ ਦੀ ਮੂਲ ਸਮਝ

ਸਿਰਫ AI ਕਲਾਇੰਟਾਂ ਨੂੰ ਹੀ ਏਜ਼ੂਰ OpenAI ਤੈਅਕਰਨ ਅਤੇ ਪ੍ਰਮਾਣਿਤ `DefaultAzureCredential` ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ,
ਜਿਵੇਂ ਕਿ ਲੋਕੇਲ ਤੇ ਮੌਜੂਦ ਏਜ਼ੂਰ CLI ਸਾਈਨ-ਇਨ ਜਾਂ ਏਜ਼ੂਰ ਵਿੱਚ ਪ੍ਰਬੰਧਿਤ ਪਹਚਾਣ. ਇਸ ਪਹਚਾਣ ਨੂੰ
ਕੋਗਨਿਟਿਵ ਸਰਵਿਸਿਜ਼ OpenAI ਯੂਜ਼ਰ ਭੂਮਿਕਾ ਚਾਹੀਦੀ ਹੈ ਸਰੋਤ ਤੇ. ਵੇਖੋ [ਅਧਿਆਇ 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
ਸਰਵਰ, ਸਿੱਧਾ SDK ਕਲਾਇੰਟ, ਅਤੇ ਸਾਰੇ ਆਟੋਮੇਟਿਕ ਟੈਸਟਾਂ ਨੂੰ ਕੋਈ ਏਜ਼ੂਰ ਖਾਤਾ ਜਾਂ ਮਾਡਲ ਐਕਸੈੱਸ ਨਹੀਂ ਚਾਹੀਦਾ.

## ਡਿਪੈਂਡੈਂਸੀ ਵਰਜਨ

2026-09-14 ਨੂੰ ਪਰਖੇ ਗਏ ਰਿਲੀਜ਼ ਡਿਪੈਂਡੈਂਸੀ:

| ਡਿਪੈਂਡੈਂਸੀ | ਵਰਜਨ |
| --- | --- |
| ਸਪ੍ਰਿੰਗ ਬੂਟ | 4.1.1 |
| ਸਪ੍ਰਿੰਗ AI | 2.0.1 |
| MCP ਜਾਵਾ SDK (ਸਪ੍ਰਿੰਗ AI-ਪ੍ਰਬੰਧਤ) | 2.0.0 |
| LangChain4j / ਕੋਰ | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j ਅਧਿਕਾਰਿਕ OpenAI ਐਡਾਪਟਰ | 1.20.0-beta30 |
| OpenAI ਜਾਵਾ SDK | 4.63.1 |
| ਏਜ਼ੂਰ ਆਇਡੈਂਟਿਟੀ | 1.18.6 |
| JUnit ਜੂਪੀਟਰ (ਬੂਟ-ਪ੍ਰਬੰਧਿਤ) | 6.0.3 |

MCP ਅਤੇ ਅਧਿਕਾਰਿਕ OpenAI ਐਡਾਪਟਰ Maven Central ਵਿਚ ਪ੍ਰਕਾਸ਼ਿਤ ਬੀਟਾ ਰਿਲੀਜ਼ ਹਨ, ਸਨੇਪਸ਼ਾਟ ਨਹੀਂ.
ਉਹਨਾਂ ਦੇ ਵਰਜਨ LangChain4j ਕੋਰ ਤੋਂ ਵੱਖਰੇ ਹਨ. ਕੋਈ ਸਨੇਪਸ਼ਾਟ ਜਾਂ ਮਾਇਲਸਟੋਨ ਰਿਪੋਜਿਟਰੀਜ਼ ਦੀ ਲੋੜ ਨਹੀਂ.
ਸਿਰਫ ਕਲਾਇੰਟ ਡਿਪੈਂਡੈਂਸੀਜ਼ ਦੀ ਟੈਸਟ ਸਕੋਪ ਹੈ ਕਿਉਂਕਿ ਚਲਾਉਣਯੋਗ ਉਦਾਹਰਨਾਂ `src/test/java` ਹੇਠਾਂ ਹਨ.

## ਪ੍ਰੋਜੈਕਟ ਸੰਰਚਨਾ ਦੀ ਸਮਝ

ਕੈਲਕੂਲੇਟਰ ਪ੍ਰੋਜੈਕਟ ਵਿਚ ਕਈ ਮਹੱਤਵਪੂਰਨ ਫਾਈਲਾਂ ਹਨ:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## ਮੁੱਖ ਘਟਕਾਂ ਦੀ ਵਿਆਖਿਆ

### 1. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ

**ਫਾਈਲ:** `McpServerApplication.java`

ਇਹ ਸਾਡੀ ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ ਦਾ ਪ੍ਰਵੇਸ਼ ਬਿੰਦੂ ਹੈ। ਇਹ ਇੱਕ ਸਟੈਂਡਰਡ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨ ਹੈ ਜਿਸ ਵਿੱਚ ਇੱਕ ਵਿਸ਼ੇਸ਼ ਸ਼ਾਮਿਲ ਹੈ:

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
- ਪੋਰਟ 8080 'ਤੇ ਇੱਕ ਸਪ੍ਰਿੰਗ ਬੂਟ ਵੈੱਬ ਸਰਵਰ ਸ਼ੁਰੂ ਕਰਦਾ ਹੈ
- `ToolCallbackProvider` ਬਣਾਉਂਦਾ ਹੈ ਜੋ ਸਾਡੇ ਕੈਲਕੂਲੇਟਰ ਮੈਥਡ MCP ਸੰਦਾਂ ਵਜੋਂ ਉਪਲਬਧ ਕਰਵਾਉਂਦਾ ਹੈ
- `@Bean` ਐਨੋਟੇਸ਼ਨ ਸਪ੍ਰਿੰਗ ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਇਹ ਇੱਕ ਕੰਪੋਨੈਂਟ ਵਜੋਂ ਪ੍ਰਬੰਧਿਤ ਕੀਤਾ ਜਾਵੇ ਜੋ ਹੋਰ ਹਿੱਸੇ ਵਰਤ ਸਕਦੇ ਹਨ

### 2. ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ

**ਫਾਈਲ:** `CalculatorService.java`

ਇੱਥੇ ਸਾਰਾ ਗਣਿਤ ਹੁੰਦਾ ਹੈ। ਹਰ ਮੈਥਡ ਨੂੰ `@Tool` ਨਾਲ ਨਿਸ਼ਾਨਿਤ ਕੀਤਾ ਗਿਆ ਹੈ ਤਾ ਕਿ ਇਹ MCP ਰਾਹੀਂ ਉਪਲਬਧ ਹੋ ਸਕੇ:

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
    
    // ਹੋਰ ਕੈਲਕੁਲੇਟਰ ਕਾਰਜ...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**ਮੁੱਖ ਵਿਸ਼ੇਸ਼ਤਾਵਾਂ:**

1. **`@Tool` ਐਨੋਟੇਸ਼ਨ**: ਇਹ MCP ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਇਹ ਮੈਥਡ ਬਾਹਰੀ ਕਲਾਇੰਟਾਂ ਵੱਲੋਂ ਕਾਲ ਕੀਤਾ ਜਾ ਸਕਦਾ ਹੈ
2. **ਸਪੱਸ਼ਟ ਵਰਨਣਾਂ**: ਹਰ ਸੰਦ ਦੇ ਕੋਲ ਇੱਕ ਵਰਨਣ ਹੁੰਦੀ ਹੈ ਜੋ AI ਮਾਡਲ ਨੂੰ ਸਮਝਾਉਂਦਾ ਹੈ ਕਿ ਕਦੋਂ ਇਸ ਦੀ ਵਰਤੋਂ ਕਰਨੀ ਹੈ
3. **ਸਥਿਰ ਰਿਟਰਨ ਫਾਰਮੈਟ**: ਸਾਰੇ ਕੰਮ ਇਨਸਾਨ-ਪੜ੍ਹ ਸਕਣ ਵਾਲੀਆਂ ਸਤਰਾਂ ਵਾਪਸ ਕਰਦੇ ਹਨ, ਜਿਵੇਂ "5.00 + 3.00 = 8.00"
4. **ਗਲਤੀ ਸੰਭਾਲਣਾ**: ਜ਼ੀਰੋ ਨਾਲ ਭਾਗ ਕਰਨ ਜਾਂ ਨਕਾਰਾਤਮਕ ਵਰਗਮੂਲ ਲਈ ਗਲਤੀ ਸੁਨੇਹੇ ਦਿੱਤੇ ਜਾਂਦੇ ਹਨ

**ਉਪਲਬਧ কর্মকਲਾਪ:**
- `add(a, b)` - ਦੋ ਨੰਬਰ ਜੋੜਦਾ ਹੈ
- `subtract(a, b)` - ਦੂਜੇ ਨੂੰ ਪਹਿਲੇ ਤੋਂ ਘਟਾਉਂਦਾ ਹੈ
- `multiply(a, b)` - ਦੋ ਨੰਬਰ ਗੁਣਾ ਕਰਦਾ ਹੈ
- `divide(a, b)` - ਪਹਿਲੇ ਨੂੰ ਦੂਜੇ ਨਾਲ ਭਾਗ ਕਰਦਾ ਹੈ (ਜ਼ੀਰੋ-ਚੈੱਕ ਸਮੇਤ)
- `power(base, exponent)` - ਬੇਸ ਨੂੰ ਘਾਤ ਵਿੱਚ ਵਧਾਉਂਦਾ ਹੈ
- `squareRoot(number)` - ਵਰਗਮੂਲ ਕੱਡਦਾ ਹੈ (ਨਕਾਰਾਤਮਕ ਚੈੱਕ ਸਮੇਤ)
- `modulus(a, b)` - ਭਾਗ ਦਾ ਬਾਕੀ ਮੁੜਦਾ ਹੈ
- `absolute(number)` - ਮੁੱਲ ਦਾ ਪਰਮਾਣ (absolute value) ਵਾਪਸ ਕਰਦਾ ਹੈ
- `help()` - ਸਾਰੇ কর্মকਲਾਪਾਂ ਬਾਰੇ ਜਾਣਕਾਰੀ ਵਾਪਸ ਕਰਦਾ ਹੈ

### 3. ਡਾਇਰੈਕਟ MCP ਕਲਾਇੰਟ

ਦੇਖੋ [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

ਇਹ ਕਲਾਇੰਟ `/mcp` ਤੇ `HttpClientStreamableHttpTransport` ਦੀ ਵਰਤੋਂ ਕਰਦਾ ਹੈ, ਕਨੈਕਸ਼ਨ ਸ਼ੁਰੂ ਕਰਦਾ ਹੈ,
ਸਰਵਰ ਨੂੰ ਪਿੰਗ ਕਰਦਾ ਹੈ ਤੇ ਸੰਦ-ਸੂਚੀ ਪੇਜਨੇਸ਼ਨ ਨੂੰ ਫਾਲੋ ਕਰਦਾ ਹੈ। ਇਹ ਯਕੀਨੀ ਬਣਾਉਂਦਾ ਹੈ ਕਿ ਸਾਰੇ ਨੌਂ ਉਮੀਦ ਕੀਤੇ ਸੰਦ ਮੌਜੂਦ ਹਨ
ਅਤੇ ਹਰ ਇੱਕ ਕਾਲ ਕਰਦਾ ਹੈ, ਜਿਨ੍ਹਾਂ ਵਿੱਚ `modulus` ਅਤੇ `help` ਵੀ ਸ਼ਾਮਿਲ ਹਨ, ਬਿਨਾਂ ਕਿਸੇ AI ਮਾਡਲ ਦੇ.

ਮੌਜੂਦਾ ਕਾਲ ਜੋਬਿਲਡਰ ਇਸ ਤਰ੍ਹਾਂ ਦਿੱਸਦਾ ਹੈ:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

ਪ੍ਰੋਟੋਕੋਲ ਗਲਤੀਆਂ ਕਲਾਇੰਟ ਨੂੰ ਫੇਲਡ ਕਰਦੀਆਂ ਹਨ ਨਾ ਕਿ ਗਲਤ ਸਫਲਤਾ ਪ੍ਰਦਰਸ਼ਿਤ ਕਰਦੀਆਂ ਹਨ. MCP ਕਲਾਇੰਟ
ਟ੍ਰਾਈ-ਵਿਥ-ਰਿਸੋਰਸਿਜ਼ ਨਾਲ ਬੰਦ ਕੀਤਾ ਜਾਂਦਾ ਹੈ, ਜਦੋਂ ਡਿਸਕਵਰੀ ਜਾਂ ਸੰਦ ਕਾਲ ਫੇਲ ਹੁੰਦੀ ਹੈ ਤਦ ਵੀ.

### 4. AI-ਸਮਰੱਥ ਕਲਾਇੰਟ

ਦੇਖੋ [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
ਅਤੇ [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` ਮੌਜੂਦਾ LangChain4j `ChatModel` API ਨੂੰ ਲਾਗੂ ਕਰਦਾ ਹੈ.
`StreamableHttpMcpTransport` ਇਸ ਨੂੰ ਉਹੀ `/mcp` ਐਂਡਪੌਇੰਟ ਨਾਲ ਜੋੜਦਾ ਹੈ ਜਿਹੜਾ SDK ਕਲਾਇੰਟ ਵਰਤਦਾ ਹੈ.
`AiServices` ਸੰਦਾਂ ਦੀ ਢੂੰਢ ਅਤੇ ਸੰਦ-ਕਾਲ/ਨਤੀਜਾ ਗੱਲਬਾਤ ਨੂੰ ਪ੍ਰਬੰਧਿਤ ਕਰਦਾ ਹੈ.

ਡਿਫੌਲਟ ਤੈਨਾਤੀ **GPT-5.6 Luna** ਹੈ, ਜਿਸ ਵਿੱਚ ਵਜਹ ਬੰਦ ਹੈ:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

ਇਹ ਡਿਫੌਲਟ ਹਰ ਪੂਰਨਤਾ ਲਈ ਲਾਗੂ ਹੁੰਦੇ ਹਨ, ਸੰਦਾਂ ਨੂੰ ਚਲਾਉਣ ਬਾਅਦ ਦੇ ਫਾਲੋਅੱਪ ਸਮੇਤ.
ਕਲਾਇੰਟ ਇੱਕ ਰੀਫਰੇਸ਼ ਕਰਨ ਯੋਗ `BearerTokenCredential` ਵਰਤਦਾ ਹੈ ਜੋ `DefaultAzureCredential` ਦੁਆਰਾ ਸਮਰਥਿਤ ਹੈ
ਅਤੇ `https://ai.azure.com/.default` ਸਕੋਪ ਦਾ, ਨਾ ਕਿ ਏਪੀ ਕੀ ਵਜੋਂ ਦੇ ਇੱਕ ਵਾਰੀ ਟੋਕਨ.
ਸਰੋਤ URLs ਅਤੇ ਜਿਹੜੇ URLs `/openai/v1` ਨਾਲ ਖਤਮ ਹੁੰਦੇ ਹਨ ਦੋਹਾਂ ਸਵੀਕਾਰਯੋਗ ਹਨ.

ਬੋਟ ਇੱਕ ਸੀਮਿਤ ਗੱਲਬਾਤ ਇਤਿਹਾਸ ਰੱਖਦਾ ਹੈ, `Tool executed: ...` ਪ੍ਰਿੰਟ ਕਰਦਾ ਹੈ ਅਸਲੀ
MCP ਨਤੀਜੇ ਨਾਲ, ਅਤੇ ਜੇ ਜਵਾਬ ਸੰਦ ਛੱਡ ਦਿੰਦਾ ਹੈ ਤਾਂ ਫੇਲ ਹੁੰਦਾ ਹੈ. ਸੰਦ ਲੂਪ ਚਾਰ ਰਾਉਂਡ ਟ੍ਰਿਪਸ ਤੱਕ ਸੀਮਿਤ ਹਨ.
ਪ੍ਰਮਾਣੀਕਰਨ, ਮਾਡਲ, MCP ਅਤੇ ਸੰਦ ਗਲਤੀਆਂ ਫੈਲਦੀਆਂ ਹਨ; ਆਪੇ-ਆਪ ਰੀਟ੍ਰਾਈਜ਼ ਬੰਦ ਹਨ.
ਦੋਹਾਂ MCP ਟ੍ਰਾਂਸਪੋਰਟ/ਕਲਾਇੰਟ ਅਤੇ ਅਧਿਕਾਰਿਕ OpenAI ਕਲਾਇੰਟ ਕਾਮਯਾਬੀ ਜਾਂ ਅਸਫਲਤਾ ਤੇ ਬੰਦ ਹੁੰਦੇ ਹਨ.

## ਉਦਾਹਰਨਾਂ ਚਲਾਉਣਾ

### ਕਦਮ 1: ਕੈਲਕੂਲੇਟਰ ਸਰਵਰ ਸ਼ੁਰੂ ਕਰੋ

ਸਰਵਰ ਲਈ ਕੋਈ ਏਜ਼ੂਰ ਸੈੱਟਅਪ ਦੀ ਲੋੜ ਨਹੀਂ. ਹੇਠਾਂ ਦਿੱਤੇ ਕਮਾਂਡ ਇਸ ਸੈਮਪਲ ਦੀ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚੋਂ ਚਲਾਓ.
ਉਦਾਹਰਨ ਪੋਰਟ **18081** ਵਰਤਦੀ ਹੈ ਦੂਜੇ ਸੈਮਪਲ ਨਾਲ ਟਕਰਾਅ ਤੋਂ ਬਚਣ ਲਈ; ਡਿਫੌਲਟ 8080 ਹੀ ਰਹਿੰਦਾ ਹੈ.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP ਐਂਡਪੌਇੰਟ ਹੈ `http://localhost:18081/mcp`. ਸਿਹਤ ਅਤੇ ਖੋਜ ਜਾਣਕਾਰੀ ਮੌਜੂਦ ਹੈ
`http://localhost:18081/health` ਅਤੇ `http://localhost:18081/info` 'ਤੇ.
Streamable HTTP ਪੁਰਾਣੇ SSE-ਸਿਰਫ ਟ੍ਰਾਂਸਪੋਰਟ ਨੂੰ ਬਦਲਦਾ ਹੈ; `/sse` ਅਤੇ `/v1/tools` ਐਂਡਪੌਇੰਟ ਨਹੀਂ ਹਨ.

### ਕਦਮ 2: ਡਾਇਰੈਕਟ ਕਲਾਇੰਟ ਨਾਲ ਟੈਸਟ ਕਰੋ

ਹੋਰ ਇੱਕ PowerShell ਟਰਮੀਨਲ ਵਿੱਚ:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

ਕੋਈ ਇਨਪੁੱਟ ਲੋੜ ਨਹੀਂ. ਸਾਰੇ ਨੌਂ ਸੰਦ ਚਲਾਏ ਜਾਂਦੇ ਹਨ. ਉਮੀਦ ਕੀਤੇ ਗਣਿਤ ਨਤੀਜੇ ਹਨ
8, 6, 42, 5, 256, 4, 2, ਅਤੇ 5.5, ਉਸ ਤੋਂ ਬਾਅਦ ਸਹਾਇਤਾ ਲਿਖਤ ਹੈ.

### ਕਦਮ 3: AI ਕਲਾਇੰਟ ਨਾਲ ਟੈਸਟ ਕਰੋ

ਪੂਰਵ-ਆਵਸ਼ਕਤਾਵਾਂ ਵਿੱਚ ਦਿੱਤੇ ਗਏ ਪ੍ਰਮਾਣੀਕਰਨ ਤੋਂ ਬਾਅਦ, ਉਸੇ ਟਰਮੀਨਲ ਵਿੱਚ AI ਕਲਾਇੰਟ ਸੈੱਟ ਕਰੋ:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

ਉਮੀਦ ਕਰੋ `Tool executed: add` ਦੀ ਲਾਈਨ ਨਾਲ `41.80`, ਉਸ ਤੋਂ ਬਾਅਦ ਮਾਡਲ ਦਾ ਜਵਾਬ.
ਸਿੰਗਲ-ਪ੍ਰਾਂਪਟ ਮੋਡ ਇਨਪੁੱਟ ਦੀ ਉਡੀਕ ਕੀਤੇ ਬਿਨਾਂ ਬਾਹਰ ਨਿਕਲ ਜਾਂਦਾ ਹੈ. ਮੂਲ ਚਾਰ-ਪ੍ਰਾਂਪਟ ਡੈਮੋ ਚਲਾਉਣ ਲਈ:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

ਡੈਮੋ ਕਾਲ ਕਰਦਾ ਹੈ `add`, `squareRoot`, `help`, ਅਤੇ ਫਿਰ ਜੁੜੀ ਹੋਈ `power` ਅਤੇ ਬਾਅਦ ਵਿੱਚ `divide`.
ਉਮੀਦ ਕੀਤੇ ਗਿਣਤੀ ਦੇ ਜਵਾਬ ਹਨ 41.8, 12, ਅਤੇ 64. ਦਲੀਲ ਨਾ ਦਿੰਦੇ ਹੋਏ ਵੀ ਇਹ ਡੈਮੋ ਚਲਦਾ ਹੈ.

### ਕਦਮ 4: ਇੰਟਰਐਕਟਿਵ ਬੋਟ ਚਲਾਓ

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

ਲਿਖੋ `Multiply 6 by 7 using the calculator service`, ਫਿਰ `exit` ਜਾਂ `quit`.
ਅਸਲੀ `multiply` ਸੰਦ ਦਾ ਨਤੀਜਾ 42 ਦੀ ਉਮੀਦ ਕਰੋ. ਖਾਲੀ ਲਾਈਨਾਂ ਨੂੰ ਅਣਡਿੱਠਾ ਕੀਤਾ ਜਾਂਦਾ ਹੈ; EOF ਵੀ ਸੈਸ਼ਨ ਖਤਮ ਕਰਦਾ ਹੈ.
ਇਸ ਐਂਟ੍ਰੀਪੁਆਇੰਟ ਦਾ ਗੈਰ-ਇੰਟਰਐਕਟਿਵ ਸਗੰਧ ਟੈਸਟ ਕਰਨ ਲਈ:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

ਦੋਹਾਂ AI ਐਂਟ੍ਰੀਪੁਆਇੰਟ `--prompt "question"`, `--demo`, ਅਤੇ `--interactive` ਸਵੀਕਾਰਦੇ ਹਨ.
ਗਲਤ ਵਿਕਲਪ ਕਨੈਕਸ਼ਨ ਖੋਲ੍ਹਣ ਤੋਂ ਪਹਿਲਾਂ ਫੇਲ ਹੁੰਦੇ ਹਨ. ਹਰ Maven `-D...` ਆਰਗੂਮੈਂਟ ਪਾਵਰਸ਼ੈੱਲ ਲਈ ਪੂਰੀ ਤਰ੍ਹਾਂ ਕੋਟ ਕੀਤਾ ਗਿਆ ਹੈ.
ਬੈਸ਼ ਤੇ, `$env:NAME = "value"` ਦੀ ਬਜਾਏ `export NAME=value` ਵਰਤੋਂ.

**ਕੋਟਾ:** AI ਉਦਾਹਰਨਾਂ ਨੂੰ ਲੜੀਵਾਰ ਚਲਾਓ. ਇੱਕ ਸਧਾਰਣ ਪ੍ਰਾਂਪਟ ਨੂੰ ਆਮ ਤੌਰ ਤੇ ਦੋ ਮਾਡਲ ਬੇਨਤੀਆਂ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ;
ਪੂਰਾ ਡੈਮੋ ਆਮ ਤੌਰ ਤੇ ਨੌ ਕਾਲਾਂ ਦੀ ਲੋੜ ਹੁੰਦੀ ਹੈ, ਸੰਦ-ਨਤੀਜਾ ਫਾਲੋਅੱਪ ਸਮੇਤ. ਸਾਂਝੀ 10 RPM
ਤੈਅਕਰਨ ਨਾਲ, ਅਗਲੇ AI ਚਲਾਓ ਲਈ ਤਾਜ਼ਾ ਕੋਟਾ ਵਿੰਡੋ ਦੀ ਆਗਿਆ ਦਿਓ. 429 ਸਫਲ ਕਿਸੇ ਵੀ ਆਟੋਮੈਟਿਕ ਰੀਟ੍ਰਾਈਜ਼ ਬਿਨਾਂ ਅਸਫਲ ਹੁੰਦਾ ਹੈ;
ਸੇਵਾ ਦੀ ਰੀਟ੍ਰਾਈ-ਆਫਟਰ ਸਲਾਹ ਦੇ ਪਿੱਛਾ ਕਰੋ. ਅਸਲੀ ਕਾਲ ਦੀ ਗਿਣਤੀ ਮਾਡਲ 'ਤੇ ਨਿਰਭਰ ਕਰਦੀ ਹੈ.
ਆਫਲਾਈਨ ਟੈਸਟ ਕਿਸੇ ਵੀ ਕੋਟਾ ਨਹੀਂ ਖਾਣਦੇ ਅਤੇ ਲਾਈਵ Luna ਉਪਲਬਧਤਾ ਜਾਂ ਜਵਾਬ ਦੀ ਗੁਣਵੱਤਾ ਦੀ ਪੁਸ਼ਟੀ ਨਹੀਂ ਕਰਦੇ.

### ਸੰਰਚਨਾ ਅਤੇ ਬੰਦ ਕਰਨਾ

| ਸੈਟਿੰਗ | ਡਿਫੌਲਟ / ਵਿਹਾਰ |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; ਬੇਸ URL, `/mcp` ਦੇ ਬਿਨਾਂ |
| `-Dmcp.server.url=...` | ਸਾਰੇ ਕਲਾਇੰਟਾਂ ਲਈ `MCP_SERVER_URL` ਨੂੰ ਓਵਰਰਾਈਡ ਕਰਦਾ ਹੈ |
| `AZURE_OPENAI_ENDPOINT` | ਸਿਰਫ AI ਕਲਾਇੰਟਾਂ ਲਈ ਜ਼ਰੂਰੀ; ਸਰੋਤ URL ਜਾਂ `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; ਏਜ਼ੂਰ ਤੈਅਕਰਨ ਨਾਮ |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; ਸਕਾਰਾਤਮਕ ਪੂਰਾ ਸੰਖਿਆ |
| ਵਿਚਾਰ ਕਰਨ ਦੀ ਕੋਸ਼ਿਸ਼ | ਹਮੇਸ਼ਾ `none`, ਸੰਦ-ਲੂਪ ਫਾਲੋਅੱਪ ਸਮੇਤ |

ਇੱਕ ਓਵਰਰਾਈਡ ਕੀਤਾ ਗਿਆ ਤੈਅਕਰਨ `reasoning_effort=none` ਅਤੇ `max_completion_tokens` ਨੂੰ ਸਮਰਥਿਤ ਕਰਨਾ ਚਾਹੀਦਾ ਹੈ.
ਕਲਾਇੰਟ ਆਪਣੇ ਆਪ `.env` ਫਾਈਲ ਨਹੀਂ ਪੜ੍ਹਦੇ. ਟੈਸਟ ਮੁਕੰਮਲ ਹੋਣ ਤੇ ਸਰਵਰ ਨੂੰ `Ctrl+C` ਨਾਲ ਬੰਦ ਕਰੋ.
ਕਲਾਇੰਟ ਸਧਾਰਣ ਤੌਰ ਤੇ ਵਾਪਸ ਆਉਂਦੇ ਹਨ ਬਿਨਾਂ `System.exit` ਜਾਂ ਬੰਦ ਕਰਨ ਵਾਲੀਆਂ ਨੀਂਦਾਂ ਦੇ.

## ਆਫਲਾਈਨ ਟੈਸਟ

```powershell
mvn -B -ntp clean verify
```

ਸਾਰੇ ਟੈਸਟ ਏਜ਼ੂਰ ਤੋਂ ਆਫਲਾਈਨ ਹਨ: ਪ੍ਰੋਟੋਕੋਲ ਸੂਟ ਇੱਕ ਸਪ੍ਰਿੰਗ ਸਰਵਰ ਅਤੇ
ਇੱਕ OpenAI-ਸੰਗਤ ਸਟੱਬ ਨੂੰ ਰੈਂਡਮ ਲੂਪਬैक ਪੋਰਟਾਂ 'ਤੇ ਸ਼ੁਰੂ ਕਰਦਾ ਹੈ, ਫਿਰ ਉਹਨਾਂ ਨੂੰ ਬੰਦ ਕਰਦਾ ਹੈ। Maven ਨੂੰ ਹੁਣ ਵੀ
ਡਿਪੈਂਡੈਂਸੀਆਂ ਡਾਊਨਲੋਡ ਕਰਨ ਦੀ ਲੋੜ ਹੋ ਸਕਦੀ ਹੈ। ਕੋਈ ਕ੍ਰਿਡੈਂਸ਼ੀਅਲ, ਲਾਈਵ ਤੈਅਕਰਨ, ਜਾਂ ਮੌਜੂਦਾ MCP ਸਰਵਰ ਵਰਤਿਆ ਨਹੀਂ ਜਾਂਦਾ।

- ਕੈਲਕੂਲੇਟਰ ਯੂਨਿਟ ਟੈਸਟ ਸਾਰੇ ਗਣਿਤ ਕੰਮ, ਦਸ਼ਮਲਵ ਨਤੀਜੇ, ਸਹਾਇਤਾ, ਅਤੇ ਖੇਤਰ ਗਲਤੀਆਂ ਕਵਰ ਕਰਦੇ ਹਨ.
- MCP ਟੈਸਟ ਸ਼ੁਰੂਆਤ, ਖੋਜ, ਸਾਰੇ ਨੌ ਟੂਲ ਕਾਲਾਂ, ਟੂਲ ਫੇਲੀਆਂ, ਅਤੇ ਸਿਹਤ/ਜਾਣਕਾਰੀ ਕਵਰ ਕਰਦੇ ਹਨ.
- AI ਪ੍ਰੋਟੋਕੋਲ ਟੈਸਟ ਪੂਰਾ ਡੈਮੋ ਅਤੇ ਇੰਟਰਐਕਟਿਵ ਬੋਟ ਕੈਲਕੂਲੇਟਰ ਵਿਰੁੱਧ ਚਲਾਉਂਦੇ ਹਨ,
  ਯਕੀਨੀ ਬਣਾਂਦੇ ਹਨ ਕਿ ਟੂਲ ਨਤੀਜੇ ਅਗਲੇ ਪੂਰਨਤਾ ਨੂੰ ਫੀਡ ਕਰਦੇ ਹਨ, ਅਤੇ ਹਰ HTTP ਬੋਡੀ ਨੂੰ ਲੂਣਾ ਲਈ ਜਾਂਚਦੇ ਹਨ,
  `reasoning_effort: "none"`, ਅਤੇ `max_completion_tokens` ਬਿਨਾਂ ਪੁਰਾਣੀ `max_tokens` ਦੇ.
- ਸੰਰਚਨਾ/ਇਨਪੁੱਟ ਟੈਸਟ ਤੈਅਕਰਨ ਅਤੇ ਐਂਡਪੌਇੰਟ ਓਵਰਰਾਈਡ, ਖਾਲੀ ਲਾਈਨਾਂ, EOF, ਬਾਹਰ ਨਿਕਲਣਾ/ਬੰਦ ਕਰਨਾ,
  ਸਿੰਗਲ-ਪ੍ਰਾਂਪਟ ਮੋਡ, ਗਲਤ ਵਿਕਲਪ, ਅਤੇ ਗਲਤੀ ਪ੍ਰਸਾਰਿਤ ਨੂੰ ਕਵਰ ਕਰਦੇ ਹਨ. ਕੋਟਾ ਟੈਸਟ ਸਾਬਤ ਕਰਦੇ ਹਨ ਕਿ 429 ਮੁੜ ਨਹੀਂ ਕੀਤਾ ਜਾਂਦਾ.

## ਇਹ ਸਾਰਾ ਇਕੱਠੇ ਕਿਵੇਂ ਕੰਮ ਕਰਦਾ ਹੈ

ਜਦੋਂ ਤੁਸੀਂ AI ਨੂੰ ਪੁੱਛਦੇ ਹੋ "5 + 3 ਕੀ ਹੈ?" ਤਾਂ ਪੂਰਾ ਪ੍ਰਵਾਹ ਇਹ ਹੈ:

1. **ਤੁਸੀਂ** ਕੁਦਰਤੀ ਭਾਸ਼ਾ ਵਿੱਚ AI ਨੂੰ ਪੁੱਖਦੇ ਹੋ
2. **AI** ਤੁਹਾਡੇ ਬੇਨਤੀ ਦਾ ਵਿਸ਼ਲੇਸ਼ਣ ਕਰਦਾ ਹੈ ਅਤੇ ਸਮਝਦਾ ਹੈ ਕਿ ਤੁਸੀਂ ਜੋੜ ਚਾਹੁੰਦੇ ਹੋ
3. **AI** MCP ਸਰਵਰ ਨੂੰ ਕਾਲ ਕਰਦਾ ਹੈ: `add(5.0, 3.0)`
4. **ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ** ਕਿਰਿਆ ਕਰਦੀ ਹੈ: `5.0 + 3.0 = 8.0`
5. **ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ** ਵਾਪਸ ਕਰਦੀ ਹੈ: `"5.00 + 3.00 = 8.00"`
6. **AI** ਨਤੀਜਾ ਪ੍ਰਾਪਤ ਕਰਦਾ ਹੈ ਅਤੇ ਇਕ ਕੁਦਰਤੀ ਜਵਾਬ ਤਿਆਰ ਕਰਦਾ ਹੈ
7. **ਤੁਸੀਂ** ਪ੍ਰਾਪਤ ਕਰਦੇ ਹੋ: "5 ਅਤੇ 3 ਦਾ ਜੋੜ 8 ਹੈ"

## ਅਗਲੇ ਕਦਮ

ਹੋਰ ਉਦਾਹਰਨਾਂ ਲਈ, ਦੇਖੋ [ਅਧਿਆਇ 04: ਵਿਹਾਰਕ ਨਮੂਨੇ](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰੋਪਣ**:
ਇਸ ਦਸਤਾਵੇਜ਼ ਦਾ ਅਨੁਵਾਦ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾਵਾਂ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਮੱਤਿਆਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੇ ਉਪਯੋਗ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਜਵਾਬਦੇਹ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->