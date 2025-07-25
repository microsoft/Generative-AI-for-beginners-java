<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T10:32:33+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "en"
}
-->
# Foundry Local Spring Boot Tutorial

## Table of Contents

- [Prerequisites](../../../../04-PracticalSamples/foundrylocal)
- [Project Overview](../../../../04-PracticalSamples/foundrylocal)
- [Understanding the Code](../../../../04-PracticalSamples/foundrylocal)
  - [1. Application Configuration (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Main Application Class (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI Service Layer (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Project Dependencies (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [How It All Works Together](../../../../04-PracticalSamples/foundrylocal)
- [Setting Up Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Running the Application](../../../../04-PracticalSamples/foundrylocal)
- [Expected Output](../../../../04-PracticalSamples/foundrylocal)
- [Next Steps](../../../../04-PracticalSamples/foundrylocal)
- [Troubleshooting](../../../../04-PracticalSamples/foundrylocal)


## Prerequisites

Before starting this tutorial, ensure you have:

- **Java 21 or higher** installed on your system
- **Maven 3.6+** for building the project
- **Foundry Local** installed and running

### **Install Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Project Overview

This project is made up of four main components:

1. **Application.java** - The main entry point for the Spring Boot application
2. **FoundryLocalService.java** - Service layer responsible for AI communication
3. **application.properties** - Configuration file for connecting to Foundry Local
4. **pom.xml** - Maven dependencies and project setup

## Understanding the Code

### 1. Application Configuration (application.properties)

**File:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**What this does:**
- **base-url**: Specifies the location where Foundry Local is running (default port 5273)
- **model**: Defines the AI model to use for text generation

**Key concept:** Spring Boot automatically loads these properties and makes them accessible in your application using the `@Value` annotation.

### 2. Main Application Class (Application.java)

**File:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**What this does:**
- `@SpringBootApplication` enables Spring Boot's auto-configuration features
- `WebApplicationType.NONE` indicates that this is a command-line application, not a web server
- The main method starts the Spring application

**The Demo Runner:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**What this does:**
- `@Bean` creates a Spring-managed component
- `CommandLineRunner` executes code after Spring Boot starts
- `foundryLocalService` is injected automatically by Spring (dependency injection)
- Sends a test message to the AI and displays the response

### 3. AI Service Layer (FoundryLocalService.java)

**File:** `src/main/java/com/example/FoundryLocalService.java`

#### Configuration Injection:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**What this does:**
- `@Service` marks this class as a provider of business logic
- `@Value` injects configuration values from application.properties
- The `:default-value` syntax provides fallback values if properties are not set

#### Client Initialization:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**What this does:**
- `@PostConstruct` ensures this method runs after Spring creates the service
- Initializes an OpenAI client pointing to your local Foundry Local instance
- The `/v1` path is required for OpenAI API compatibility
- API key is set to "unused" since local development does not require authentication

#### Chat Method:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**What this does:**
- **ChatCompletionCreateParams**: Configures the AI request
  - `model`: Specifies the AI model to use
  - `addUserMessage`: Adds your message to the conversation
  - `maxCompletionTokens`: Limits the length of the response (to save resources)
  - `temperature`: Controls randomness (0.0 = deterministic, 1.0 = creative)
- **API Call**: Sends the request to Foundry Local
- **Response Handling**: Safely extracts the AI's text response
- **Error Handling**: Wraps exceptions with helpful error messages

### 4. Project Dependencies (pom.xml)

**Key Dependencies:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**What these do:**
- **spring-boot-starter**: Provides essential Spring Boot functionality
- **openai-java**: Official OpenAI Java SDK for API communication
- **jackson-databind**: Handles JSON serialization/deserialization for API calls

## How It All Works Together

Here’s the complete flow when you run the application:

1. **Startup**: Spring Boot starts and loads `application.properties`
2. **Service Creation**: Spring creates `FoundryLocalService` and injects configuration values
3. **Client Setup**: `@PostConstruct` initializes the OpenAI client to connect to Foundry Local
4. **Demo Execution**: `CommandLineRunner` runs after startup
5. **AI Call**: The demo calls `foundryLocalService.chat()` with a test message
6. **API Request**: The service builds and sends an OpenAI-compatible request to Foundry Local
7. **Response Processing**: The service extracts and returns the AI’s response
8. **Display**: The application prints the response and exits

## Setting Up Foundry Local

To set up Foundry Local, follow these steps:

1. **Install Foundry Local** using the instructions in the [Prerequisites](../../../../04-PracticalSamples/foundrylocal) section.
2. **Download the AI model** you want to use, such as `phi-3.5-mini`, with the following command:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configure the application.properties** file to match your Foundry Local settings, especially if you’re using a different port or model.

## Running the Application

### Step 1: Start Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Step 2: Build and Run the Application
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Expected Output

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Next Steps

For more examples, see [Chapter 04: Practical samples](../README.md)

## Troubleshooting

### Common Issues

**"Connection refused" or "Service unavailable"**
- Ensure Foundry Local is running: `foundry model list`
- Verify the service is on port 5273: Check `application.properties`
- Restart Foundry Local: `foundry model run phi-3.5-mini`

**"Model not found" errors**
- Check available models: `foundry model list`
- Update the model name in `application.properties` to match exactly
- Download the model if necessary: `foundry model run phi-3.5-mini`

**Maven compilation errors**
- Confirm Java 21 or higher: `java -version`
- Clean and rebuild: `mvn clean compile`
- Check your internet connection for dependency downloads

**Application starts but no output**
- Verify Foundry Local is responding: Open a browser and navigate to `http://localhost:5273`
- Check application logs for specific error messages
- Ensure the model is fully loaded and ready

**Disclaimer**:  
This document has been translated using the AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please note that automated translations may contain errors or inaccuracies. The original document in its native language should be regarded as the authoritative source. For critical information, professional human translation is recommended. We are not responsible for any misunderstandings or misinterpretations resulting from the use of this translation.