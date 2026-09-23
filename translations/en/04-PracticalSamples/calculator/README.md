# MCP Calculator Tutorial for Beginners

## Table of Contents

- [What You Will Learn](#what-you-will-learn)
- [Prerequisites](#prerequisites)
- [Dependency Versions](#dependency-versions)
- [Understanding the Project Structure](#understanding-the-project-structure)
- [Core Components Explained](#core-components-explained)
  - [1. Main Application](#1-main-application)
  - [2. Calculator Service](#2-calculator-service)
  - [3. Direct MCP Client](#3-direct-mcp-client)
  - [4. AI-Powered Client](#4-ai-powered-client)
- [Running the Examples](#running-the-examples)
- [Offline Tests](#offline-tests)
- [How It All Works Together](#how-it-all-works-together)
- [Next Steps](#next-steps)

## What You Will Learn

This tutorial explains how to build a calculator service using the Model Context Protocol (MCP). You'll understand:

- How to create a service that AI can use as a tool
- How to set up direct communication with MCP services
- How AI models can automatically choose which tools to use
- The difference between direct protocol calls and AI-assisted interactions

## Prerequisites

Before starting, make sure you have:
- Java 21 or higher installed
- Maven for dependency management
- Basic understanding of Java and Spring Boot

Only the AI clients require an Azure OpenAI deployment and an authenticated `DefaultAzureCredential`,
such as an existing Azure CLI sign-in locally or a managed identity in Azure. The identity needs
the Cognitive Services OpenAI User role on the resource. See [Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
The server, direct SDK client, and all automated tests need no Azure account or model access.

## Dependency Versions

Release dependencies verified on 2026-09-14:

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

The MCP and official OpenAI adapters are published beta releases in Maven Central, not snapshots.
Their versions differ from LangChain4j core. No snapshot or milestone repositories are needed.
Client-only dependencies have test scope because the runnable examples live under `src/test/java`.

## Understanding the Project Structure

The calculator project has several important files:

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

## Core Components Explained

### 1. Main Application

**File:** `McpServerApplication.java`

This is the entry point of our calculator service. It's a standard Spring Boot application with one special addition:

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

**What this does:**
- Starts a Spring Boot web server on port 8080
- Creates a `ToolCallbackProvider` that makes our calculator methods available as MCP tools
- The `@Bean` annotation tells Spring to manage this as a component that other parts can use

### 2. Calculator Service

**File:** `CalculatorService.java`

This is where all the math happens. Each method is marked with `@Tool` to make it available through MCP:

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
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Key features:**

1. **`@Tool` Annotation**: This tells MCP that this method can be called by external clients
2. **Clear Descriptions**: Each tool has a description that helps AI models understand when to use it
3. **Consistent Return Format**: All operations return human-readable strings like "5.00 + 3.00 = 8.00"
4. **Error Handling**: Division by zero and negative square roots return error messages

**Available Operations:**
- `add(a, b)` - Adds two numbers
- `subtract(a, b)` - Subtracts second from first
- `multiply(a, b)` - Multiplies two numbers
- `divide(a, b)` - Divides first by second (with zero-check)
- `power(base, exponent)` - Raises base to the power of exponent
- `squareRoot(number)` - Calculates square root (with negative check)
- `modulus(a, b)` - Returns remainder of division
- `absolute(number)` - Returns absolute value
- `help()` - Returns information about all operations

### 3. Direct MCP Client

See [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

This client uses `HttpClientStreamableHttpTransport` at `/mcp`, initializes the connection,
pings the server, and follows tool-list pagination. It checks that all nine expected tools
exist and calls each of them, including `modulus` and `help`, without an AI model.

The current request builder looks like this:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protocol errors fail the client instead of printing a misleading success. The MCP client
is closed with try-with-resources, including when discovery or a tool call fails.

### 4. AI-Powered Client

See [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
and [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implements the current LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` connects it to the same `/mcp` endpoint as the SDK client.
`AiServices` discovers the tools and manages the tool-call/result conversation.

The default deployment is **GPT-5.6 Luna**, with reasoning explicitly disabled:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

These defaults apply to every completion, including follow-ups after tool execution.
The client uses a refreshable `BearerTokenCredential` backed by `DefaultAzureCredential`
and the `https://ai.azure.com/.default` scope, not a one-time token passed as an API key.
Resource URLs and URLs already ending in `/openai/v1` are both accepted.

The bot keeps a bounded conversation history, prints `Tool executed: ...` with the actual
MCP result, and fails if a response skips tools. Tool loops are limited to four round trips.
Authentication, model, MCP, and tool errors propagate; automatic model retries are disabled.
Both the MCP transport/client and the official OpenAI client are closed on success or failure.

## Running the Examples

### Step 1: Start the Calculator Server

No Azure configuration is needed for the server. Commands below run from this sample's directory.
The example uses port **18081** to avoid conflicting with another sample; the default remains 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

The MCP endpoint is `http://localhost:18081/mcp`. Health and discovery information are at
`http://localhost:18081/health` and `http://localhost:18081/info`.
Streamable HTTP replaces the old SSE-only transport; `/sse` and `/v1/tools` are not endpoints.

### Step 2: Test with Direct Client

In another PowerShell terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

No input is needed. All nine tools are exercised. Expected arithmetic results include
8, 6, 42, 5, 256, 4, 2, and 5.5, followed by the help text.

### Step 3: Test with AI Client

After authenticating as described in the prerequisites, configure the AI client in the same terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Expect a `Tool executed: add` line with `41.80`, followed by the model's answer.
The single-prompt mode exits without waiting for input. To run the original four-prompt demo:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

The demo calls `add`, `squareRoot`, `help`, and the chained `power` then `divide` operation.
Expected numeric answers are 41.8, 12, and 64. Omitting arguments also runs this demo.

### Step 4: Run the Interactive Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Enter `Multiply 6 by 7 using the calculator service`, then `exit` or `quit`.
Expect an actual `multiply` tool result of 42. Blank lines are ignored; EOF also ends the session.
For a noninteractive smoke test of this entrypoint:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Both AI entrypoints accept `--prompt "question"`, `--demo`, and `--interactive`.
Invalid options fail before opening a connection. Each Maven `-D...` argument is fully quoted
for PowerShell. On Bash, use `export NAME=value` instead of `$env:NAME = "value"`.

**Quota:** Run AI samples sequentially. A simple prompt normally needs two model requests;
the complete demo normally needs nine, including tool-result follow-ups. With a shared 10 RPM
deployment, allow a fresh quota window before the next AI run. A 429 fails visibly without
automatic retries; follow the service's retry-after guidance. Actual request counts depend on the model.
Offline tests do not consume any quota and do not establish live Luna availability or answer quality.

### Configuration and Shutdown

| Setting | Default / behavior |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; base URL, without `/mcp` |
| `-Dmcp.server.url=...` | Overrides `MCP_SERVER_URL` for all clients |
| `AZURE_OPENAI_ENDPOINT` | Required only for AI clients; resource URL or `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; an Azure deployment name |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positive integer |
| Reasoning effort | Always `none`, including tool-loop follow-ups |

An overridden deployment must support `reasoning_effort=none` and `max_completion_tokens`.
The clients do not read a `.env` file automatically. Stop the server with `Ctrl+C` after testing.
Clients return normally without `System.exit` or shutdown sleeps.

## Offline Tests

```powershell
mvn -B -ntp clean verify
```

All tests are offline with respect to Azure: the protocol suite starts a Spring server and
an OpenAI-compatible stub on random loopback ports, then closes them. Maven may still need
to download dependencies. No credentials, live deployment, or pre-existing MCP server are used.

- Calculator unit tests cover all arithmetic operations, decimal results, help, and domain errors.
- MCP tests cover initialization, discovery, all nine tool calls, tool failures, and health/info.
- The AI protocol tests execute the full demo and interactive Bot against the real calculator,
  verify tool results feed the next completion, and inspect every HTTP body for Luna,
  `reasoning_effort: "none"`, and `max_completion_tokens` with no legacy `max_tokens`.
- Configuration/input tests cover deployment and endpoint overrides, blank lines, EOF, exit/quit,
  single-prompt mode, invalid options, and error propagation. Quota tests prove 429 is not retried.

## How It All Works Together

Here's the complete flow when you ask the AI "What's 5 + 3?":

1. **You** ask the AI in natural language
2. **AI** analyzes your request and realizes you want addition
3. **AI** calls the MCP server: `add(5.0, 3.0)`
4. **Calculator Service** performs: `5.0 + 3.0 = 8.0`
5. **Calculator Service** returns: `"5.00 + 3.00 = 8.00"`
6. **AI** receives the result and formats a natural response
7. **You** get: "The sum of 5 and 3 is 8"

## Next Steps

For more examples, see [Chapter 04: Practical samples](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
This document has been translated using AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please be aware that automated translations may contain errors or inaccuracies. The original document in its native language should be considered the authoritative source. For critical information, professional human translation is recommended. We are not liable for any misunderstandings or misinterpretations arising from the use of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->