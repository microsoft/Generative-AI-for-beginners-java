<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T10:31:59+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "en"
}
-->
# Core Generative AI Techniques Tutorial

## Table of Contents

- [Prerequisites](../../../03-CoreGenerativeAITechniques)
- [Getting Started](../../../03-CoreGenerativeAITechniques)
  - [Step 1: Set Your Environment Variable](../../../03-CoreGenerativeAITechniques)
  - [Step 2: Navigate to the Examples Directory](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: LLM Completions and Chat](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Function Calling](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: Responsible AI](../../../03-CoreGenerativeAITechniques)
- [Common Patterns Across Examples](../../../03-CoreGenerativeAITechniques)
- [Next Steps](../../../03-CoreGenerativeAITechniques)
- [Troubleshooting](../../../03-CoreGenerativeAITechniques)
  - [Common Issues](../../../03-CoreGenerativeAITechniques)

## Overview

This tutorial offers practical examples of essential generative AI techniques using Java and GitHub Models. You'll learn how to interact with Large Language Models (LLMs), implement function calling, use retrieval-augmented generation (RAG), and apply responsible AI practices.

## Prerequisites

Before you begin, ensure you have:
- Java 21 or later installed
- Maven for managing dependencies
- A GitHub account with a personal access token (PAT)

## Getting Started

### Step 1: Set Your Environment Variable

Start by setting your GitHub token as an environment variable. This token lets you access GitHub Models for free.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Step 2: Navigate to the Examples Directory

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: LLM Completions and Chat

**File:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### What This Example Teaches

This example explains the basics of interacting with Large Language Models (LLMs) via the OpenAI API. It covers client initialization with GitHub Models, structuring system and user prompts, managing conversation history, and adjusting parameters to control response length and creativity.

### Key Code Concepts

#### 1. Client Setup
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

This establishes a connection to GitHub Models using your token.

#### 2. Simple Completion
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Conversation Memory
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

The AI retains context only if you include previous messages in subsequent requests.

### Run the Example
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### What Happens When You Run It

1. **Simple Completion**: The AI answers a Java-related question using system prompt guidance.
2. **Multi-turn Chat**: The AI maintains context across multiple questions.
3. **Interactive Chat**: You can engage in a real conversation with the AI.

## Tutorial 2: Function Calling

**File:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### What This Example Teaches

Function calling allows AI models to request external tools and APIs. The model interprets natural language requests, determines the necessary function calls with parameters using JSON Schema definitions, and processes the results to generate responses. Developers retain control over function execution for security and reliability.

### Key Code Concepts

#### 1. Function Definition
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

This defines the available functions and their usage.

#### 2. Function Execution Flow
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Function Implementation
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Run the Example
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### What Happens When You Run It

1. **Weather Function**: The AI requests weather data for Seattle, you provide it, and the AI formats a response.
2. **Calculator Function**: The AI requests a calculation (15% of 240), you compute it, and the AI explains the result.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**File:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### What This Example Teaches

Retrieval-Augmented Generation (RAG) combines information retrieval with language generation. It injects external document context into AI prompts, enabling the model to provide accurate answers based on specific knowledge sources rather than relying solely on its training data.

### Key Code Concepts

#### 1. Document Loading
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Context Injection
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Triple quotes help the AI distinguish between context and the question.

#### 3. Safe Response Handling
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Always validate API responses to avoid crashes.

### Run the Example
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### What Happens When You Run It

1. The program loads `document.txt` (containing information about GitHub Models).
2. You ask a question about the document.
3. The AI responds based solely on the document's content, not its general knowledge.

Try asking: "What is GitHub Models?" versus "What is the weather like?"

## Tutorial 4: Responsible AI

**File:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### What This Example Teaches

This example highlights the importance of safety measures in AI applications. It demonstrates filters that detect harmful content categories such as hate speech, harassment, self-harm, sexual content, and violence. It shows how production AI applications should handle content policy violations gracefully.

### Key Code Concepts

#### 1. Safety Testing Framework
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Safety Categories Tested
- Violence or harmful instructions
- Hate speech
- Privacy violations
- Medical misinformation
- Illegal activities

### Run the Example
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### What Happens When You Run It

The program tests various harmful prompts and demonstrates how the AI safety system:
1. **Blocks dangerous requests** with HTTP 400 errors.
2. **Allows safe content** to be generated normally.
3. **Protects users** from harmful AI outputs.

## Common Patterns Across Examples

### Authentication Pattern
All examples use this pattern to authenticate with GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Error Handling Pattern
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Message Structure Pattern
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Next Steps

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## Troubleshooting

### Common Issues

**"GITHUB_TOKEN not set"**
- Ensure the environment variable is set.
- Verify your token has the `models:read` scope.

**"No response from API"**
- Check your internet connection.
- Confirm your token is valid.
- Verify you haven't exceeded rate limits.

**Maven compilation errors**
- Ensure Java 21 or later is installed.
- Run `mvn clean compile` to refresh dependencies.

**Disclaimer**:  
This document has been translated using the AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please note that automated translations may contain errors or inaccuracies. The original document in its native language should be regarded as the authoritative source. For critical information, professional human translation is recommended. We are not responsible for any misunderstandings or misinterpretations resulting from the use of this translation.