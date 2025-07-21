# Java Generative AI Developer's Cheat Sheet

## 🎯 Core Patterns You've Mastered

### 1. **Basic AI Client Setup**
```java
// GitHub Models Authentication Pattern
String apiKey = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(apiKey);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### 2. **Text Completion Pattern**
```java
// Simple Text Generation
List<ChatRequestMessage> messages = List.of(
    new ChatRequestUserMessage("Your prompt here")
);
ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini");
ChatCompletions response = client.getChatCompletions("gpt-4o-mini", options);
String result = response.getChoices().get(0).getMessage().getContent();
```

### 3. **Function Calling Pattern**
```java
// Define tools the AI can use
List<ChatCompletionsToolDefinition> tools = List.of(
    new ChatCompletionsToolDefinition(new ChatCompletionsFunctionToolDefinition(
        new FunctionDefinition("getCurrentWeather")
            .setDescription("Get current weather for a location")
            .setParameters(BinaryData.fromObject(Map.of(
                "type", "object",
                "properties", Map.of(
                    "location", Map.of("type", "string", "description", "City name")
                ),
                "required", List.of("location")
            )))
    ))
);

// Use tools in conversation
ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setTools(tools);
```

### 4. **RAG (Retrieval-Augmented Generation) Pattern**
```java
// Load and provide context
String context = Files.readString(Paths.get("knowledge-base.txt"));
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Use only the CONTEXT to answer. If not found, say 'I cannot find that information.'"),
    new ChatRequestUserMessage("CONTEXT:\n\"\"\"" + context + "\"\"\"\n\nQUESTION:\n" + question)
);
```

### 5. **Error Handling Pattern**
```java
try {
    ChatCompletions response = client.getChatCompletions(modelId, options);
    // Handle success
} catch (Exception e) {
    if (e.getMessage().contains("content_filter")) {
        System.out.println("Content was filtered for safety");
    } else {
        System.err.println("API Error: " + e.getMessage());
    }
}
```

### 6. **Spring Boot Integration Pattern**
```java
@Service
public class AIService {
    @Value("${github.models.endpoint}")
    private String endpoint;
    
    @Value("${github.models.model}")
    private String model;
    
    private OpenAIClient client;
    
    @PostConstruct
    public void init() {
        // Initialize client
    }
    
    public String generateStory(String prompt) {
        // AI logic here
    }
}
```

### 7. **Thread Safety Pattern**
```java
public static void main(String[] args) {
    try {
        // Your AI application logic
    } finally {
        // Force clean exit to avoid thread warnings
        System.exit(0);
    }
}
```

## 🛠️ Maven Configuration Templates

### Core Dependencies
```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-ai-openai</artifactId>
    <version>1.0.0-beta.12</version>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.18.2</version>
</dependency>
```

### Spring Boot Dependencies
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.5.3</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
    <version>3.5.3</version>
</dependency>
```

## 🔧 Environment Setup

### Required Environment Variables
```bash
export GITHUB_TOKEN="your_token_here"  # For GitHub Models
export JAVA_HOME="/usr/lib/jvm/msopenjdk-current"
export MAVEN_OPTS="-Xmx2g"
```

### Maven Settings (for Spring Boot plugin)
```xml
<!-- ~/.m2/settings.xml -->
<settings>
    <pluginGroups>
        <pluginGroup>org.springframework.boot</pluginGroup>
    </pluginGroups>
</settings>
```

## 🚀 Quick Start Commands

### Run AI Examples
```bash
# Basic completion
mvn exec:java -Dexec.mainClass="com.example.genai.techniques.completions.CompletionsApp"

# Function calling
mvn exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"

# RAG demo
mvn exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"

# Responsible AI
mvn exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Run Web Applications
```bash
# Pet Story Generator (port 3000)
cd 04-PracticalSamples/petstory
mvn spring-boot:run

# MCP Calculator (port 8080)
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

## 📊 AI Model Capabilities

### GitHub Models Available
- **gpt-4o-mini**: Fast, cost-effective, good for most tasks
- **gpt-4o**: More capable, better reasoning
- **Meta-Llama models**: Open source alternatives

### Model Selection Guide
- **Simple text generation**: gpt-4o-mini
- **Complex reasoning**: gpt-4o
- **Function calling**: gpt-4o-mini or gpt-4o
- **RAG applications**: gpt-4o-mini (sufficient for most cases)

## 🛡️ Responsible AI Checklist

### Content Safety
- ✅ Handle 400 status codes for filtered content
- ✅ Parse content filter responses
- ✅ Implement graceful fallbacks
- ✅ Log safety events for monitoring

### Error Handling
- ✅ Network timeouts and retries
- ✅ Rate limiting (429 status codes)
- ✅ Invalid input validation
- ✅ Model availability checks

### User Experience
- ✅ Loading indicators for slow requests
- ✅ Clear error messages for users
- ✅ Fallback content when AI unavailable
- ✅ Input validation and sanitization

## 🔍 Debugging Tips

### Common Issues and Solutions
1. **Thread warnings**: Add `System.exit(0)` in main methods
2. **Maven plugin not found**: Create `~/.m2/settings.xml` with pluginGroups
3. **Port conflicts**: Use different ports or kill existing processes
4. **Content filtering**: Check for 400 status codes and filter responses
5. **Authentication**: Verify GITHUB_TOKEN environment variable

### Logging Configuration
```properties
logging.level.com.example=INFO
logging.level.org.springframework.security=DEBUG
```

## 🎯 Next Learning Projects

### Beginner Projects
1. **AI-Powered CLI Tool**: Create a command-line assistant
2. **Smart Note Taker**: Auto-summarize and categorize notes
3. **Code Reviewer**: AI that explains code functionality

### Intermediate Projects
1. **Multi-Agent System**: Multiple AI agents working together
2. **Vector Database Integration**: Implement semantic search
3. **Custom Function Library**: Build reusable AI tools

### Advanced Projects
1. **Enterprise RAG System**: Scale to large document collections
2. **AI Workflow Orchestrator**: Chain multiple AI operations
3. **Real-time AI Dashboard**: Live AI-powered analytics

## 📚 Additional Resources

### Documentation
- [Azure OpenAI Java SDK](https://learn.microsoft.com/en-us/java/api/overview/azure/ai-openai-readme)
- [GitHub Models Documentation](https://docs.github.com/en/github-models)
- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)

### Best Practices
- Always validate user inputs
- Implement proper error handling
- Use environment variables for secrets
- Monitor API usage and costs
- Test edge cases and error scenarios
- Follow responsible AI guidelines

---

**Congratulations!** You now have a comprehensive reference for building production-ready Java AI applications! 🎉
