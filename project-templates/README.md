# AI Project Template

## Quick Start

1. **Copy this template to a new directory:**
   ```bash
   cp -r project-templates/ my-new-ai-project/
   cd my-new-ai-project/
   ```

2. **Set your GitHub token:**
   ```bash
   export GITHUB_TOKEN="your_token_here"
   ```

3. **Customize the project:**
   - Edit `pom.xml` to change group ID, artifact ID, and name
   - Modify `Application.java` to implement your AI logic
   - Add dependencies as needed

4. **Run your application:**
   ```bash
   mvn compile exec:java
   ```

## Template Features

✅ **GitHub Models Integration** - Ready to use Azure OpenAI SDK  
✅ **Error Handling** - Proper exception handling and safety filtering  
✅ **Clean Architecture** - Separated AI service class  
✅ **Maven Configuration** - All necessary dependencies  
✅ **Thread Safety** - Proper shutdown handling  

## Common Customizations

### Add Spring Boot Web Support
Uncomment Spring Boot dependencies in `pom.xml` and create controllers:

```java
@RestController
public class AIController {
    
    @Autowired
    private AIService aiService;
    
    @PostMapping("/complete")
    public String complete(@RequestBody String prompt) {
        return aiService.complete(prompt);
    }
}
```

### Add Function Calling
Extend the `AIService` class:

```java
public String callFunction(String functionName, Map<String, Object> parameters) {
    // Implement function calling logic
}
```

### Add RAG Support
Add document processing capabilities:

```java
public String answerFromDocument(String question, String document) {
    String systemMessage = "Use only the provided document to answer questions.";
    String userMessage = "DOCUMENT:\n" + document + "\n\nQUESTION:\n" + question;
    return completeWithSystem(systemMessage, userMessage);
}
```

## Project Ideas to Try

1. **CLI Assistant** - Command-line AI helper
2. **Document Summarizer** - Upload and summarize files  
3. **Code Explainer** - Analyze and explain source code
4. **Smart FAQ Bot** - RAG-powered question answering
5. **Multi-Language Translator** - Real-time translation service

Happy coding! 🚀
