package com.example.genai.techniques.rag;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenCredential;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * A simple RAG (Retrieval-Augmented Generation) demonstration that reads a document
 * and allows users to ask questions about its content.
 * 
 * RAG combines two approaches:
 * 1. RETRIEVAL: Finding relevant information from a knowledge source (our document)
 * 2. GENERATION: Using AI to generate answers based on that retrieved information
 * 
 * This prevents the AI from "hallucinating" (making up) answers by giving it
 * specific context to work with.
 */
public class SimpleReaderDemo {

    public static void main(String[] args) throws IOException {
        // GitHub Models endpoint - provides free access to various AI models for learning
        String endpoint = "https://models.inference.ai.azure.com";
        // PAT = Personal Access Token - this authenticates us with GitHub Models
        String apiKey = System.getenv("GITHUB_TOKEN");
        
        // Validate API key
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("Error: GITHUB_TOKEN environment variable is not set or is empty.");
            System.exit(1);
        }

        // RETRIEVAL STEP: Load the document that will serve as our knowledge base
        // In a real RAG system, this might be a database, vector store, or document collection
        // Find document.txt by checking multiple possible locations
        String[] possiblePaths = {
            "document.txt",
            "examples/document.txt", 
            "03-CoreGenerativeAITechniques/examples/document.txt"
        };
        
        String doc = null;
        for (String path : possiblePaths) {
            if (Files.exists(Paths.get(path))) {
                doc = Files.readString(Paths.get(path));
                System.out.println("Found document at: " + path);
                break;
            }
        }
        
        if (doc == null) {
            throw new IOException("Could not find document.txt in any expected location");
        }

        // Use try-with-resources to ensure Scanner is properly closed
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Ask a question about the document: ");
            String question = scanner.nextLine();
            
            // Validate question input
            if (question == null || question.trim().isEmpty()) {
                System.err.println("Error: No question provided.");
                return;
            }

            // Create the OpenAI client using Azure's SDK
            TokenCredential credential = new StaticTokenCredential(apiKey);
            OpenAIClient chatClient = new OpenAIClientBuilder()
                    .endpoint(endpoint)
                    .credential(credential)
                    .buildClient();

            // GENERATION STEP: Construct the prompt with both context and question
            // This is the key to RAG - we provide the AI with specific context
            // The triple quotes (""") help the AI clearly separate context from question
            List<ChatRequestMessage> messages = List.of(
                    // System message: Instruct AI to ONLY use provided context, not its training data
                    new ChatRequestSystemMessage("You are a helpful assistant. Use only the CONTEXT to answer. If the answer is not in the context, say 'I cannot find that information in the provided document.'"),
                    // User message: Provide both the document content AND the user's question
                    new ChatRequestUserMessage("CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question)
            );

            String modelId = "gpt-4o-mini";
            ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
                    .setModel(modelId);

            try {
                // Send the RAG request: AI will generate answer based on our document
                ChatCompletions response = chatClient.getChatCompletions(modelId, options);
                
                // Extract and validate the AI's response
                // This defensive programming prevents crashes from malformed API responses
                if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                    ChatChoice choice = response.getChoices().get(0);
                    if (choice != null && choice.getMessage() != null && choice.getMessage().getContent() != null) {
                        System.out.println("Assistant: " + choice.getMessage().getContent());
                    } else {
                        System.err.println("Error: Received invalid response from the API.");
                    }
                } else {
                    System.err.println("Error: No response received from the API.");
                }
            } catch (Exception e) {
                // Handle network errors, API errors, or authentication issues
                System.err.println("Error calling the API: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Force proper cleanup to avoid thread lingering warnings
        System.exit(0);
    }

    /**
     * Custom implementation of TokenCredential for GitHub Models authentication.
     * This is a simplified version that just wraps our GitHub token.
     * In production RAG systems, you'd use more sophisticated credential management.
     */
    private static final class StaticTokenCredential implements TokenCredential {
        private final String token;
        
        StaticTokenCredential(String token) { 
            this.token = token; 
        }
        
        @Override
        public Mono<AccessToken> getToken(com.azure.core.credential.TokenRequestContext request) {
            // Return our token wrapped in an AccessToken object
            // Setting expiration to 1 year from now (for demo purposes)
            return Mono.just(new AccessToken(token, OffsetDateTime.now().plusYears(1)));
        }
    }
}
