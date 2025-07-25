<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:33:12+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "en"
}
-->
# Pet Story Generator Tutorial for Beginners

## Table of Contents

- [Prerequisites](../../../../04-PracticalSamples/petstory)
- [Understanding the Project Structure](../../../../04-PracticalSamples/petstory)
- [Core Components Explained](../../../../04-PracticalSamples/petstory)
  - [1. Main Application](../../../../04-PracticalSamples/petstory)
  - [2. Web Controller](../../../../04-PracticalSamples/petstory)
  - [3. Story Service](../../../../04-PracticalSamples/petstory)
  - [4. Web Templates](../../../../04-PracticalSamples/petstory)
  - [5. Configuration](../../../../04-PracticalSamples/petstory)
- [Running the Application](../../../../04-PracticalSamples/petstory)
- [How It All Works Together](../../../../04-PracticalSamples/petstory)
- [Understanding the AI Integration](../../../../04-PracticalSamples/petstory)
- [Next Steps](../../../../04-PracticalSamples/petstory)

## Prerequisites

Before you begin, ensure you have the following:
- Java 21 or later installed
- Maven for managing dependencies
- A GitHub account with a personal access token (PAT) that has the `models:read` scope
- Basic knowledge of Java, Spring Boot, and web development

## Understanding the Project Structure

The pet story project includes several key files:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI story generation service
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Core Components Explained

### 1. Main Application

**File:** `PetStoryApplication.java`

This is the starting point of the Spring Boot application:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**What it does:**
- The `@SpringBootApplication` annotation enables auto-configuration and component scanning
- Launches an embedded web server (Tomcat) on port 8080
- Automatically creates all necessary Spring beans and services

### 2. Web Controller

**File:** `PetController.java`

This component manages all web requests and user interactions:

```java
@Controller
public class PetController {
    
    private final StoryService storyService;
    
    public PetController(StoryService storyService) {
        this.storyService = storyService;
    }
    
    @GetMapping("/")
    public String index() {
        return "index";  // Returns index.html template
    }
    
    @PostMapping("/generate-story")
    public String generateStory(@RequestParam("description") String description, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        
        // Input validation
        if (description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please provide a description.");
            return "redirect:/";
        }
        
        // Sanitize input for security
        String sanitizedDescription = sanitizeInput(description);
        
        // Generate story with error handling
        try {
            String story = storyService.generateStory(sanitizedDescription);
            model.addAttribute("caption", sanitizedDescription);
            model.addAttribute("story", story);
            return "result";  // Returns result.html template
            
        } catch (Exception e) {
            // Use fallback story if AI fails
            String fallbackStory = generateFallbackStory(sanitizedDescription);
            model.addAttribute("story", fallbackStory);
            return "result";
        }
    }
    
    private String sanitizeInput(String input) {
        return input.replaceAll("[<>\"'&]", "")  // Remove dangerous characters
                   .trim()
                   .substring(0, Math.min(input.length(), 500));  // Limit length
    }
}
```

**Key features:**

1. **Route Handling**: `@GetMapping("/")` displays the upload form, while `@PostMapping("/generate-story")` processes submissions
2. **Input Validation**: Ensures descriptions are not empty and meet length requirements
3. **Security**: Sanitizes user input to prevent XSS attacks
4. **Error Handling**: Provides fallback stories if the AI service is unavailable
5. **Model Binding**: Passes data to HTML templates using Spring's `Model`

**Fallback System:**
The controller includes pre-written story templates that are used when the AI service is not accessible:

```java
private String generateFallbackStory(String description) {
    String[] storyTemplates = {
        "Meet the most wonderful pet in the world – a furry ball of energy...",
        "Once upon a time, there lived a remarkable pet whose heart was as big...",
        "In a cozy home filled with love, there lived an extraordinary pet..."
    };
    
    // Use description hash for consistent responses
    int index = Math.abs(description.hashCode() % storyTemplates.length);
    return storyTemplates[index];
}
```

### 3. Story Service

**File:** `StoryService.java`

This service interacts with GitHub Models to generate stories:

```java
@Service
public class StoryService {
    
    private final OpenAIClient openAIClient;
    private final String modelName;
    
    public StoryService(@Value("${github.models.endpoint}") String endpoint,
                       @Value("${github.models.model}") String modelName) {
        
        String githubToken = System.getenv("GITHUB_TOKEN");
        if (githubToken == null || githubToken.isBlank()) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable must be set");
        }
        
        // Create OpenAI client configured for GitHub Models
        this.openAIClient = OpenAIOkHttpClient.builder()
                .baseUrl(endpoint)
                .apiKey(githubToken)
                .build();
    }
    
    public String generateStory(String description) {
        String systemPrompt = "You are a creative storyteller who writes fun, " +
                             "family-friendly short stories about pets. " +
                             "Keep stories under 500 words and appropriate for all ages.";
        
        String userPrompt = "Write a fun short story about a pet described as: " + description;
        
        // Configure the AI request
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(modelName)
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(500)  // Limit response length
                .temperature(0.8)          // Control creativity (0.0-1.0)
                .build();
        
        // Send request and get response
        ChatCompletion response = openAIClient.chat().completions().create(params);
        
        return response.choices().get(0).message().content().orElse("");
    }
}
```

**Key components:**

1. **OpenAI Client**: Utilizes the official OpenAI Java SDK configured for GitHub Models
2. **System Prompt**: Sets the AI's behavior to create family-friendly pet stories
3. **User Prompt**: Provides the AI with specific instructions based on the pet description
4. **Parameters**: Controls the story's length and creativity level
5. **Error Handling**: Throws exceptions that the controller manages

### 4. Web Templates

**File:** `index.html` (Upload Form)

The main page where users describe their pets:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Generator</title>
    <!-- CSS styling -->
</head>
<body>
    <div class="container">
        <h1>Pet Story Generator</h1>
        <p>Describe your pet and we'll create a fun story about them!</p>
        
        <!-- Error message display -->
        <div th:if="${error}" class="error" th:text="${error}"></div>
        
        <!-- Story generation form -->
        <form action="/generate-story" method="post">
            <div class="form-group">
                <label for="description">Describe your pet:</label>
                <textarea id="description" name="description" 
                         placeholder="Tell us about your pet - what they look like, their personality, favorite activities..."
                         maxlength="1000" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Generate Story</button>
        </form>
        
        <!-- Image upload section with client-side processing -->
        <div class="upload-section">
            <h2>Or Upload a Photo</h2>
            <input type="file" id="imageInput" accept="image/*" />
            <button onclick="analyzeImage()" class="upload-btn">Analyze Image</button>
        </div>
        
        <script>
            // Client-side image analysis using Transformers.js
            async function analyzeImage() {
                // Image processing code here
                // Generates description automatically from uploaded image
            }
        </script>
    </div>
</body>
</html>
```

**File:** `result.html` (Story Display)

Displays the generated story:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Template features:**

1. **Thymeleaf Integration**: Uses `th:` attributes for dynamic content
2. **Responsive Design**: CSS styling for both mobile and desktop
3. **Error Handling**: Shows validation errors to users
4. **Client-side Processing**: JavaScript for image analysis (using Transformers.js)

### 5. Configuration

**File:** `application.properties`

Contains the application's configuration settings:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# GitHub Models configuration
github.models.endpoint=https://models.github.ai/inference
github.models.model=openai/gpt-4.1-nano
```

**Configuration explained:**

1. **File Upload**: Allows images up to 10MB
2. **Logging**: Manages what information is logged during execution
3. **GitHub Models**: Specifies the AI model and endpoint to use
4. **Security**: Configures error handling to avoid exposing sensitive information

## Running the Application

### Step 1: Set Your GitHub Token

First, set your GitHub token as an environment variable:

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

**Why this is needed:**
- GitHub Models requires authentication to access AI models
- Using environment variables keeps sensitive tokens out of the source code
- The `models:read` scope grants access to AI inference

### Step 2: Build and Run

Navigate to the project directory:
```bash
cd 04-PracticalSamples/petstory
```

Build the application:
```bash
mvn clean compile
```

Start the server:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.

### Step 3: Test the Application

1. **Open** `http://localhost:8080` in your browser
2. **Describe** your pet in the text area (e.g., "A playful golden retriever who loves to fetch")
3. **Click** "Generate Story" to receive an AI-generated story
4. **Alternatively**, upload a pet image to automatically generate a description
5. **View** the creative story based on your pet's description

## How It All Works Together

Here’s the complete process for generating a pet story:

1. **User Input**: You describe your pet on the web form
2. **Form Submission**: The browser sends a POST request to `/generate-story`
3. **Controller Processing**: `PetController` validates and sanitizes the input
4. **AI Service Call**: `StoryService` sends a request to the GitHub Models API
5. **Story Generation**: The AI creates a story based on the description
6. **Response Handling**: The controller receives the story and adds it to the model
7. **Template Rendering**: Thymeleaf renders `result.html` with the story
8. **Display**: The user sees the generated story in their browser

**Error Handling Flow:**
If the AI service fails:
1. The controller catches the exception
2. A fallback story is generated using pre-written templates
3. The fallback story is displayed with a note about AI unavailability
4. The user still receives a story, ensuring a positive experience

## Understanding the AI Integration

### GitHub Models API
The application uses GitHub Models, which provides free access to various AI models:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Prompt Engineering
The service uses carefully designed prompts to achieve high-quality results:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Response Processing
The AI response is extracted and validated:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Next Steps

For more examples, refer to [Chapter 04: Practical samples](../README.md)

**Disclaimer**:  
This document has been translated using the AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we aim for accuracy, please note that automated translations may include errors or inaccuracies. The original document in its native language should be regarded as the authoritative source. For critical information, professional human translation is advised. We are not responsible for any misunderstandings or misinterpretations resulting from the use of this translation.