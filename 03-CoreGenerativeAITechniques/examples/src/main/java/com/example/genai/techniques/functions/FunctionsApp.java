package com.example.genai.techniques.functions;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import com.azure.core.util.BinaryData;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GitHub Models Function Calling Example using Azure OpenAI SDK
 * 
 * This example demonstrates how to use function calling with GitHub Models
 * using the Azure OpenAI SDK to create an AI assistant that can call external 
 * functions for weather information and mathematical calculations.
 * 
 * Features:
 * - Function definition and registration
 * - Function calling with structured outputs
 * - Multi-turn conversation with function results
 * - Error handling
 */
public class FunctionsApp {
    // Using a model that supports function calling - not all AI models can call functions
    // gpt-4o-mini is one of the models that supports this advanced feature
    private static final String MODEL = "gpt-4o-mini";
    
    public static void main(String[] args) {
        // GitHub Models endpoint - provides free access to various AI models for learning
        String endpoint = "https://models.inference.ai.azure.com";
        // PAT = Personal Access Token - this authenticates us with GitHub Models
        String pat = System.getenv("GITHUB_TOKEN");
        
        if (pat == null || pat.isBlank()) {
            System.err.println("Please set GITHUB_TOKEN environment variable");
            System.err.println("Get your token from: https://github.com/settings/tokens");
            System.exit(1);
        }

        try {
            // Create the OpenAI client using Azure's SDK
            // This client will handle all our communication with the AI model
            OpenAIClient client = new OpenAIClientBuilder()
                    .endpoint(endpoint)
                    .credential(new StaticTokenCredential(pat)) // Custom credential class (defined below)
                    .buildClient();

            // Example 1: Simple weather function
            System.out.println("=== Weather Function Example ===");
            weatherFunctionExample(client);
            
            System.out.println("\n" + "=".repeat(60) + "\n");
            
            // Example 2: Calculator function
            System.out.println("=== Calculator Function Example ===");
            calculatorFunctionExample(client);
            
            // Force proper cleanup to avoid thread lingering warnings
            System.exit(0);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Example demonstrating weather function calling.
     * Function calling allows the AI to "call" external functions when it needs specific data.
     * The AI doesn't actually execute the function - it just tells us what function to call and with what parameters.
     */
    private static void weatherFunctionExample(OpenAIClient client) {
        // Step 1: Define what the function does and what parameters it accepts
        // This is like creating a "function signature" that the AI can understand
        ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
            new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
        weatherFunction.setDescription("Get current weather information for a city");
        
        // Define the function parameters using JSON Schema format
        // This tells the AI exactly what data it can request from our function
        weatherFunction.setParameters(BinaryData.fromString("""
            {
                "type": "object",
                "properties": {
                    "city": {
                        "type": "string",
                        "description": "The city name"
                    },
                    "unit": {
                        "type": "string",
                        "enum": ["celsius", "fahrenheit"],
                        "description": "Temperature unit",
                        "default": "celsius"
                    }
                },
                "required": ["city"]
            }
            """));

        // Wrap the function definition in a tool definition
        // Tools are the mechanism by which AI models can call functions
        ChatCompletionsFunctionToolDefinition weatherTool = new ChatCompletionsFunctionToolDefinition(weatherFunction);
        List<ChatCompletionsToolDefinition> tools = List.of(weatherTool);

        // Step 2: Set up the conversation like normal, but tell the AI about available functions
        List<ChatRequestMessage> messages = new ArrayList<>();
        messages.add(new ChatRequestSystemMessage(
            "You are a helpful weather assistant. Use the get_weather function to provide current weather information."
        ));
        messages.add(new ChatRequestUserMessage("What's the weather like in Seattle?"));

        // IMPORTANT: Add the tools to the options so the AI knows what functions it can call
        ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
            .setModel(MODEL)
            .setTools(tools);   // This tells the AI about available functions

        try {
            ChatCompletions response = client.getChatCompletions(MODEL, options);
            
            // Step 3: Check if AI wants to call a function instead of giving a direct answer
            // When AI needs external data, it will request a function call instead of guessing
            ChatChoice choice = response.getChoices().get(0);
            if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
                // AI wants to call one or more functions
                List<ChatCompletionsToolCall> toolCalls = choice.getMessage().getToolCalls();
                
                for (ChatCompletionsToolCall toolCall : toolCalls) {
                    if (toolCall instanceof ChatCompletionsFunctionToolCall functionCall) {
                        System.out.println("AI wants to call function: " + functionCall.getFunction().getName());
                        System.out.println("Arguments: " + functionCall.getFunction().getArguments());
                        
                        // Step 4: Execute the function (this is where YOU run your actual code)
                        // The AI doesn't execute anything - it just tells you what to run
                        String functionResult = simulateWeatherFunction(functionCall.getFunction().getArguments());
                        System.out.println("Function result: " + functionResult);
                        
                        // Step 5: Give the function result back to the AI so it can formulate a response
                        // This is a 3-step process: User asks -> AI requests function -> You run function -> AI responds
                        messages.add(new ChatRequestAssistantMessage(choice.getMessage().getContent()).setToolCalls(toolCalls));
                        messages.add(new ChatRequestToolMessage(functionResult, toolCall.getId()));
                        
                        // Step 6: Get the AI's final response now that it has the function result
                        ChatCompletionsOptions finalOptions = new ChatCompletionsOptions(messages).setModel(MODEL);
                        ChatCompletions finalResponse = client.getChatCompletions(MODEL, finalOptions);
                        System.out.println("AI: " + finalResponse.getChoices().get(0).getMessage().getContent());
                    }
                }
            } else {
                // AI gave a direct answer without needing to call any functions
                System.out.println("AI: " + choice.getMessage().getContent());
            }
            
        } catch (Exception e) {
            System.err.println("Weather function example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Example demonstrating calculator function calling.
     * This shows how AI can delegate mathematical operations to external functions.
     */
    private static void calculatorFunctionExample(OpenAIClient client) {
        // Define calculator function - similar to weather function but for math operations
        ChatCompletionsFunctionToolDefinitionFunction calcFunction = 
            new ChatCompletionsFunctionToolDefinitionFunction("calculate");
        calcFunction.setDescription("Perform mathematical calculations");
        calcFunction.setParameters(BinaryData.fromString("""
            {
                "type": "object",
                "properties": {
                    "expression": {
                        "type": "string",
                        "description": "Mathematical expression to evaluate (e.g., '15% of 240' or '2 + 3 * 4')"
                    }
                },
                "required": ["expression"]
            }
            """));

        ChatCompletionsFunctionToolDefinition calcTool = new ChatCompletionsFunctionToolDefinition(calcFunction);

        List<ChatCompletionsToolDefinition> tools = List.of(calcTool);

        List<ChatRequestMessage> messages = new ArrayList<>();
        messages.add(new ChatRequestSystemMessage(
            "You are a helpful math assistant. Use the calculate function for mathematical operations."
        ));
        messages.add(new ChatRequestUserMessage("What's 15% of 240?"));

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
            .setModel(MODEL)
            .setTools(tools);

        try {
            ChatCompletions response = client.getChatCompletions(MODEL, options);
            
            ChatChoice choice = response.getChoices().get(0);
            if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
                List<ChatCompletionsToolCall> toolCalls = choice.getMessage().getToolCalls();
                
                for (ChatCompletionsToolCall toolCall : toolCalls) {
                    if (toolCall instanceof ChatCompletionsFunctionToolCall functionCall) {
                        System.out.println("AI wants to calculate: " + functionCall.getFunction().getArguments());
                        
                        // Execute our calculator function with the AI's requested parameters
                        String calculationResult = simulateCalculatorFunction(functionCall.getFunction().getArguments());
                        System.out.println("Calculation result: " + calculationResult);
                        
                        // Same pattern: Add function result back to conversation for AI to process
                        messages.add(new ChatRequestAssistantMessage(choice.getMessage().getContent()).setToolCalls(toolCalls));
                        messages.add(new ChatRequestToolMessage(calculationResult, toolCall.getId()));
                        
                        // Get final response with the calculation result
                        ChatCompletionsOptions finalOptions = new ChatCompletionsOptions(messages).setModel(MODEL);
                        ChatCompletions finalResponse = client.getChatCompletions(MODEL, finalOptions);
                        System.out.println("AI: " + finalResponse.getChoices().get(0).getMessage().getContent());
                    }
                }
            } else {
                System.out.println("AI: " + choice.getMessage().getContent());
            }
            
        } catch (Exception e) {
            System.err.println("Calculator function example failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Simulate weather function (in real app, this would call a weather API).
     * 
     * IMPORTANT: The AI doesn't execute this function - it just tells us what function to call.
     * We are responsible for parsing the arguments and actually executing the logic.
     * This is a security feature - the AI can't run arbitrary code on your system.
     */
    private static String simulateWeatherFunction(String arguments) {
        // In a real application, you would:
        // 1. Parse the JSON arguments to extract city name and unit
        // 2. Call a real weather API (like OpenWeatherMap)
        // 3. Return the actual weather data
        System.out.println("Calling weather API with arguments: " + arguments);
        
        // For this demo, we return mock weather data
        return """
            {
                "city": "Seattle",
                "temperature": "22",
                "unit": "celsius",
                "condition": "partly cloudy",
                "humidity": "65%",
                "wind": "light breeze from the west"
            }
            """;
    }
    
    /**
     * Simulate calculator function.
     * In a real app, you'd parse the expression and perform actual calculations.
     */
    private static String simulateCalculatorFunction(String arguments) {
        // In a real application, you would:
        // 1. Parse the JSON arguments to extract the mathematical expression
        // 2. Safely evaluate the expression (using a math library, not eval())
        // 3. Return the calculated result
        System.out.println("Performing calculation with arguments: " + arguments);
        
        // For this example, we'll return a simple result for "15% of 240"
        return "36";
    }
    
    /**
     * Custom implementation of TokenCredential for GitHub Models authentication.
     * This is a simplified version that just wraps our GitHub token.
     * In production, you'd use more sophisticated credential management.
     */
    private static final class StaticTokenCredential implements TokenCredential {
        private final String token;
        
        StaticTokenCredential(String token) {
            this.token = token;
        }

        @Override
        public Mono<AccessToken> getToken(com.azure.core.credential.TokenRequestContext tokenRequestContext) {
            // Return our token wrapped in an AccessToken object
            // Setting expiration to 1 year from now (for demo purposes)
            return Mono.just(new AccessToken(token, OffsetDateTime.now().plusYears(1)));
        }
    }
}
