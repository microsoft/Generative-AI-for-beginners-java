package com.example.ai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Template for building AI applications with Java and GitHub Models.
 * 
 * This template provides:
 * - GitHub Models authentication
 * - Basic AI completion
 * - Error handling
 * - Clean shutdown
 */
public class Application {
    
    private static final String ENDPOINT = "https://models.inference.ai.azure.com";
    private static final String MODEL = "gpt-4o-mini";
    
    public static void main(String[] args) {
        try {
            new Application().run();
        } finally {
            // Ensure clean shutdown
            System.exit(0);
        }
    }
    
    public void run() {
        // Validate environment
        String apiKey = System.getenv("GITHUB_TOKEN");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("Error: GITHUB_TOKEN environment variable is required");
            return;
        }
        
        // Initialize AI client
        OpenAIClient client = createClient(apiKey);
        AIService aiService = new AIService(client);
        
        // Your application logic here
        System.out.println("=== AI Application Started ===");
        
        // Example: Simple completion
        String response = aiService.complete("Hello, how are you today?");
        System.out.println("AI Response: " + response);
        
        // Example: Function calling (uncomment to use)
        // String result = aiService.callFunction("getCurrentWeather", Map.of("location", "Seattle"));
        // System.out.println("Function Result: " + result);
        
        System.out.println("=== Application Complete ===");
    }
    
    private OpenAIClient createClient(String apiKey) {
        TokenCredential credential = new StaticTokenCredential(apiKey);
        return new OpenAIClientBuilder()
                .endpoint(ENDPOINT)
                .credential(credential)
                .buildClient();
    }
    
    /**
     * AI Service wrapper for common operations
     */
    public static class AIService {
        private final OpenAIClient client;
        
        public AIService(OpenAIClient client) {
            this.client = client;
        }
        
        /**
         * Simple text completion
         */
        public String complete(String prompt) {
            try {
                List<ChatRequestMessage> messages = List.of(
                    new ChatRequestUserMessage(prompt)
                );
                
                ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
                    .setModel(MODEL)
                    .setMaxTokens(500)
                    .setTemperature(0.7);
                
                ChatCompletions response = client.getChatCompletions(MODEL, options);
                
                if (response != null && !response.getChoices().isEmpty()) {
                    return response.getChoices().get(0).getMessage().getContent();
                }
                return "No response generated";
                
            } catch (Exception e) {
                if (e.getMessage().contains("content_filter")) {
                    return "[Content filtered for safety]";
                } else {
                    System.err.println("AI Error: " + e.getMessage());
                    return "[Error generating response]";
                }
            }
        }
        
        /**
         * Completion with system message
         */
        public String completeWithSystem(String systemMessage, String userMessage) {
            try {
                List<ChatRequestMessage> messages = List.of(
                    new ChatRequestSystemMessage(systemMessage),
                    new ChatRequestUserMessage(userMessage)
                );
                
                ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
                    .setModel(MODEL);
                
                ChatCompletions response = client.getChatCompletions(MODEL, options);
                
                if (response != null && !response.getChoices().isEmpty()) {
                    return response.getChoices().get(0).getMessage().getContent();
                }
                return "No response generated";
                
            } catch (Exception e) {
                System.err.println("AI Error: " + e.getMessage());
                return "[Error generating response]";
            }
        }
        
        // Add more methods as needed:
        // - Function calling
        // - Streaming responses
        // - RAG operations
        // - Multi-turn conversations
    }
    
    /**
     * Simple token credential implementation for GitHub Models
     */
    private static class StaticTokenCredential implements TokenCredential {
        private final String token;
        
        public StaticTokenCredential(String token) {
            this.token = token;
        }
        
        @Override
        public Mono<AccessToken> getToken(com.azure.core.credential.TokenRequestContext request) {
            return Mono.just(new AccessToken(token, OffsetDateTime.now().plusYears(1)));
        }
    }
}
