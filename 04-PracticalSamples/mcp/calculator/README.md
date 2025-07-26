# MCP Calculator Tutorial for Beginners

## Table of Contents

- [What You Will Learn](#what-you-will-learn)
- [Prerequisites](#prerequisites)
- [Understanding the Project Structure](#understanding-the-project-structure)
- [Core Components Explained](#core-components-explained)
  - [1. Main Application](#1-main-application)
  - [2. Calculator Service](#2-calculator-service)
  - [3. Direct MCP Client](#3-direct-mcp-client)
  - [4. AI-Powered Client](#4-ai-powered-client)
- [Running the Examples](#running-the-examples)
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
- A GitHub account with a personal access token (PAT)
- Basic understanding of Java and Spring Boot

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
    └── Bot.java                          # Simple chat interface
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
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
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

**File:** `SDKClient.java`

This client talks directly to the MCP server without using AI. It manually calls specific calculator functions:

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
2. **Lists** all available tools (our calculator functions)
3. **Calls** specific functions with exact parameters
4. **Prints** the results directly

**When to use this:** When you know exactly which calculation you want to perform and want to call it programmatically.

### 4. AI-Powered Client

**File:** `LangChain4jClient.java`

This client uses an AI model (GPT-4o-mini) that can automatically decide which calculator tools to use:

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
1. **Creates** an AI model connection using your GitHub token
2. **Connects** the AI to our calculator MCP server
3. **Gives** the AI access to all our calculator tools
4. **Allows** natural language requests like "Calculate the sum of 24.5 and 17.3"

**The AI automatically:**
- Understands you want to add numbers
- Chooses the `add` tool
- Calls `add(24.5, 17.3)`
- Returns the result in a natural response

## Running the Examples

### Step 1: Start the Calculator Server

First, set your GitHub token (needed for the AI client):

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

The server will start on `http://localhost:8080`. You should see:
```
Started McpServerApplication in X.XXX seconds
```

### Step 2: Test with Direct Client

In a new terminal:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient" -Dexec.classpathScope=test
```

You'll see output like:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Step 3: Test with AI Client

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient" -Dexec.classpathScope=test
```

You'll see the AI automatically using tools:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

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

For more examples, see [Chapter 04: Practical samples](../../README.md)


