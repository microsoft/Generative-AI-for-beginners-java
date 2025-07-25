<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:32:53+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "en"
}
-->
# MCP Calculator Tutorial for Beginners

## Table of Contents

- [What You Will Learn](../../../../../04-PracticalSamples/mcp/calculator)
- [Prerequisites](../../../../../04-PracticalSamples/mcp/calculator)
- [Understanding the Project Structure](../../../../../04-PracticalSamples/mcp/calculator)
- [Core Components Explained](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Main Application](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Calculator Service](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Direct MCP Client](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-Powered Client](../../../../../04-PracticalSamples/mcp/calculator)
- [Running the Examples](../../../../../04-PracticalSamples/mcp/calculator)
- [How It All Works Together](../../../../../04-PracticalSamples/mcp/calculator)
- [Next Steps](../../../../../04-PracticalSamples/mcp/calculator)

## What You Will Learn

This tutorial walks you through building a calculator service using the Model Context Protocol (MCP). You'll learn:

- How to create a service that AI can use as a tool
- How to establish direct communication with MCP services
- How AI models can automatically decide which tools to use
- The difference between direct protocol calls and AI-assisted interactions

## Prerequisites

Before you begin, ensure you have:
- Java 21 or later installed
- Maven for managing dependencies
- A GitHub account with a personal access token (PAT)
- Basic knowledge of Java and Spring Boot

## Understanding the Project Structure

The calculator project includes several key files:

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

## Core Components Explained

### 1. Main Application

**File:** `McpServerApplication.java`

This is the starting point of the calculator service. It's a typical Spring Boot application with one unique feature:

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
- Launches a Spring Boot web server on port 8080
- Creates a `ToolCallbackProvider` to make the calculator methods accessible as MCP tools
- The `@Bean` annotation ensures Spring manages this as a component that other parts of the application can use

### 2. Calculator Service

**File:** `CalculatorService.java`

This is where all the calculations are performed. Each method is annotated with `@Tool` to make it accessible via MCP:

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

**Key features:**

1. **`@Tool` Annotation**: Marks methods as callable by external clients through MCP
2. **Clear Descriptions**: Each tool includes a description to help AI models understand its purpose
3. **Consistent Return Format**: All operations return human-readable strings like "5.00 + 3.00 = 8.00"
4. **Error Handling**: Handles cases like division by zero and negative square roots with appropriate error messages

**Available Operations:**
- `add(a, b)` - Adds two numbers
- `subtract(a, b)` - Subtracts the second number from the first
- `multiply(a, b)` - Multiplies two numbers
- `divide(a, b)` - Divides the first number by the second (with a check for division by zero)
- `power(base, exponent)` - Raises the base to the power of the exponent
- `squareRoot(number)` - Calculates the square root (with a check for negative numbers)
- `modulus(a, b)` - Returns the remainder of division
- `absolute(number)` - Returns the absolute value
- `help()` - Provides information about all available operations

### 3. Direct MCP Client

**File:** `SDKClient.java`

This client communicates directly with the MCP server without involving AI. It manually invokes specific calculator functions:

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

**What this does:**
1. **Connects** to the calculator server at `http://localhost:8080`
2. **Lists** all available tools (the calculator functions)
3. **Calls** specific functions with precise parameters
4. **Prints** the results directly

**When to use this:** When you know exactly which calculation you need and want to execute it programmatically.

### 4. AI-Powered Client

**File:** `LangChain4jClient.java`

This client uses an AI model (GPT-4o-mini) that can automatically determine which calculator tools to use:

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

**What this does:**
1. **Establishes** a connection to the AI model using your GitHub token
2. **Connects** the AI to the calculator MCP server
3. **Grants** the AI access to all calculator tools
4. **Allows** natural language queries like "Calculate the sum of 24.5 and 17.3"

**The AI automatically:**
- Understands the request involves addition
- Selects the `add` tool
- Calls `add(24.5, 17.3)`
- Returns the result in a natural response

## Running the Examples

### Step 1: Start the Calculator Server

First, set your GitHub token (required for the AI client):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Start the server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

The server will launch at `http://localhost:8080`. You should see:
```
Started McpServerApplication in X.XXX seconds
```

### Step 2: Test with Direct Client

Open a new terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

You should see output similar to:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Step 3: Test with AI Client

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

You should see the AI automatically using the tools:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## How It All Works Together

Here’s the complete process when you ask the AI "What's 5 + 3?":

1. **You** ask the AI in plain language
2. **AI** interprets your request and identifies it as an addition operation
3. **AI** calls the MCP server: `add(5.0, 3.0)`
4. **Calculator Service** performs the calculation: `5.0 + 3.0 = 8.0`
5. **Calculator Service** returns: `"5.00 + 3.00 = 8.00"`
6. **AI** receives the result and formats a natural response
7. **You** receive: "The sum of 5 and 3 is 8"

## Next Steps

For more examples, refer to [Chapter 04: Practical samples](../../README.md)

**Disclaimer**:  
This document has been translated using the AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we aim for accuracy, please note that automated translations may include errors or inaccuracies. The original document in its native language should be regarded as the authoritative source. For critical information, professional human translation is advised. We are not responsible for any misunderstandings or misinterpretations resulting from the use of this translation.