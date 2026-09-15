# MCP Calculator Tutorial for Beginners

## Table of Contents

- [Wetin You Go Learn](#wetin-you-go-learn)
- [Wetin Dem Need Before](#wetin-dem-need-before)
- [Versions for Dem Dependencies](#versions-for-dependencies)
- [How You Go Sabi Project Structure](#how-you-go-sabi-project-structure)
- [Main Components Wey Dem Explain](#main-components-wey-dem-explain)
  - [1. Main Application](#1-main-application)
  - [2. Calculator Service](#2-calculator-service)
  - [3. Direct MCP Client](#3-direct-mcp-client)
  - [4. AI-Powered Client](#4-ai-powered-client)
- [How to Run Dem Examples](#how-to-run-dem-examples)
- [Offline Tests](#offline-tests)
- [How Everything Dey Work Together](#how-everything-dey-work-together)
- [Wet Next Steps](#wet-next-steps)

## Wetin You Go Learn

Dis tutorial go explain how you fit build calculator service using Model Context Protocol (MCP). You go fit understand:

- How to make service wey AI fit use as tool
- How to set correct direct talk with MCP services
- How AI models fit select tools to use by themselves
- Different between direct protocol calls and AI-assisted talk

## Wetin Dem Need Before

Before you start, make sure say you get:
- Java 21 or higher wey you don install
- Maven to manage dependencies
- Basic sabi Java and Spring Boot

Na only AI clients need Azure OpenAI deployment and authenticated `DefaultAzureCredential`,
like say you don sign in with Azure CLI for your local or managed identity for Azure. Di identity gats
Cognitive Services OpenAI User role for the resource. See [Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Di server, direct SDK client, and all automated tests no need Azure account or access to model.

## Versions for Dependencies

Dependencies wey dem check on 2026-09-14:

| Dependency | Version |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-managed) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j official OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-managed) | 6.0.3 |

MCP and official OpenAI adapters na beta releases wey dem publish for Maven Central, no be snapshots.
Their versions different from LangChain4j core. No need snapshot or milestone repositories.
Client-only dependencies get test scope because di runnable examples dey under `src/test/java`.

## How You Go Sabi Project Structure

Calculator project get some important files:

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

## Main Components Wey Dem Explain

### 1. Main Application

**File:** `McpServerApplication.java`

Na here be the entrance point for our calculator service. Na standard Spring Boot application but e get one special tin:

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

**Wetin this one dey do:**
- E dey start Spring Boot web server for port 8080
- E dey create `ToolCallbackProvider` wey make our calculator methods dey available as MCP tools
- `@Bean` annotation dey tell Spring to manage am as component wey other parts fit use

### 2. Calculator Service

**File:** `CalculatorService.java`

Na here all di maths take happen. Every method get `@Tool` for mark to make am available through MCP:

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
    
    // More calculator operations dem...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Main features:**

1. **`@Tool` Annotation**: Dis dey tell MCP say dis method fit call from outside client
2. **Clear Descriptions**: Each tool get description wey go help AI model sabi when to use am
3. **Consistent Return Format**: All operations go dey return human-readable strings like "5.00 + 3.00 = 8.00"
4. **Error Handling**: Division by zero and negative square root go return error messages

**Available Operations:**
- `add(a, b)` - E dey add two numbers
- `subtract(a, b)` - E dey subtract second from first
- `multiply(a, b)` - E dey multiply two numbers
- `divide(a, b)` - E dey divide first by second (with zero-check)
- `power(base, exponent)` - E dey raise base to the power of exponent
- `squareRoot(number)` - E dey calculate square root (with negative check)
- `modulus(a, b)` - E dey return remainder of division
- `absolute(number)` - E dey return absolute value
- `help()` - E dey return information about all operations

### 3. Direct MCP Client

See [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Dis client dey use `HttpClientStreamableHttpTransport` for `/mcp`, e dey initialize connection,
e dey ping server, and e dey follow tool-list pagination. E dey check say all nine tools wey dem expect
dey exist and e dey call each one, including `modulus` and `help`, without AI model.

Current request builder dey like dis:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protocol errors go fail client instead make e print misleading success. MCP client
dey close with try-with-resources, even when discovery or tool call fail.

### 4. AI-Powered Client

See [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
and [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` na di current LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` connect am to the same `/mcp` endpoint wey SDK client dey use.
`AiServices` dey discover tools and dey manage tool-call/result conversation.

Default deployment na **GPT-5.6 Luna**, with reasoning manually turn off:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Dis defaults dey apply to every completion, including follow-ups after tool run.
Di client dey use refreshable `BearerTokenCredential` wey `DefaultAzureCredential` support
and the `https://ai.azure.com/.default` scope, no be one-time token wey dem pass as API key.
Resource URLs and URLs wey already end for `/openai/v1` na both dey accepted.

Di bot dey keep limited conversation history, e dey print `Tool executed: ...` with actual
MCP result, and e go fail if response no use tools. Tool loops dey limit to four round trips.
Authentication, model, MCP, and tool errors dey propagate; automatic model retries no dey work.
Both MCP transport/client and official OpenAI client dey close on success or failure.

## How to Run Dem Examples

### Step 1: Start the Calculator Server

No Azure config needed for server. Commands below dey run from dis sample directory.
The example dey use port **18081** to avoid conflict with another sample; default still dey 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP endpoint na `http://localhost:18081/mcp`. Health and discovery info dey for
`http://localhost:18081/health` and `http://localhost:18081/info`.
Streamable HTTP don replace old SSE-only transport; `/sse` and `/v1/tools` no be endpoints again.

### Step 2: Test with Direct Client

For another PowerShell terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

No input needed. All nine tools dey exercise. Expected arithmetic results include
8, 6, 42, 5, 256, 4, 2, and 5.5, then help text.

### Step 3: Test with AI Client

After you authenticate like dem talk for prerequisites, configure AI client for same terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Expect `Tool executed: add` line with `41.80`, then model answer.
Single-prompt mode go exit without wait input. To run original four-prompt demo:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Di demo go call `add`, `squareRoot`, `help`, and chained `power` then `divide` operation.
Expected numeric answers na 41.8, 12, and 64. If you no put args, e still run this demo.

### Step 4: Run the Interactive Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Make you enter `Multiply 6 by 7 using the calculator service`, then `exit` or `quit`.
Expect correct `multiply` tool result of 42. Blank lines no dey count; EOF too fit end session.
For noninteractive smoke test of dis entrypoint:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Both AI entrypoints dey accept `--prompt "question"`, `--demo`, and `--interactive`.
Bad options go fail before e open connection. Each Maven `-D...` argument fully quoted
for PowerShell. For Bash use `export NAME=value` instead of `$env:NAME = "value"`.

**Quota:** Run AI samples one after another. Simple prompt normally need two model requests;
complete demo normally need nine, including tool-result follow-ups. With shared 10 RPM
deployment, give fresh quota window before next AI run. 429 go fail visibly without
automatic retries; follow service retry-after explanation. Actual request counts depend on model.
Offline tests no dey use any quota and no dey check live Luna availability or answer quality.

### Configuration and Shutdown

| Setting | Default / behavior |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; base URL, no include `/mcp` |
| `-Dmcp.server.url=...` | Override `MCP_SERVER_URL` for all clients |
| `AZURE_OPENAI_ENDPOINT` | Only needed for AI clients; resource URL or `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azure deployment name |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positive number |
| Reasoning effort | Always `none`, included tool-loop follow-ups |

Override deployment must support `reasoning_effort=none` and `max_completion_tokens`.
Clients no dey automatically read `.env` file. Stop server with `Ctrl+C` after test finish.
Clients go normally return without `System.exit` or shutdown sleep.

## Offline Tests

```powershell
mvn -B -ntp clean verify
```

All tests dey offline re Azure: the protocol suite go start Spring server and
OpenAI-compatible stub on random loopback ports, then close dem. Maven fit still need
to download dependencies. No credentials, live deployment, or existing MCP server dey used.

- Calculator unit tests cover all arithmetic operations, decimal results, help, and domain errors.
- MCP tests cover initialization, discovery, all nine tool calls, tool failures, and health/info.
- AI protocol tests dey run full demo and interactive Bot for real calculator,
  confirm tool results dey feed next completion, and check every HTTP body for Luna,
  `reasoning_effort: "none"`, and `max_completion_tokens` without old `max_tokens`.
- Configuration/input tests cover deployment and endpoint overrides, blank lines, EOF, exit/quit,
  single prompt mode, bad options, and error carry forward. Quota tests show 429 no dey retry.

## How Everything Dey Work Together

Dis na full flow wey you ask AI "Wetin be 5 + 3?":

1. **You** ask AI for normal language
2. **AI** check your request and know say you want addition
3. **AI** call MCP server: `add(5.0, 3.0)`
4. **Calculator Service** do: `5.0 + 3.0 = 8.0`
5. **Calculator Service** return: `"5.00 + 3.00 = 8.00"`
6. **AI** receive result and arrange natural response
7. **You** see: "The sum of 5 and 3 is 8"

## Wet Next Steps

For more samples, see [Chapter 04: Practical samples](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg make you know say automated translation fit get errors or mistakes. Di original document for dia own language na im be di correct source. For important info, make person wey sabi human translation do am. We no go responsible for any misunderstanding or wrong understanding wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->