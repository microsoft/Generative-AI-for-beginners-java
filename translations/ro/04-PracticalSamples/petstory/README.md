<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T12:12:48+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ro"
}
-->
# Tutorial pentru Generatorul de Povești cu Animale de Companie pentru Începători

## Cuprins

- [Cerințe preliminare](../../../../04-PracticalSamples/petstory)
- [Înțelegerea structurii proiectului](../../../../04-PracticalSamples/petstory)
- [Explicația componentelor de bază](../../../../04-PracticalSamples/petstory)
  - [1. Aplicația principală](../../../../04-PracticalSamples/petstory)
  - [2. Controller web](../../../../04-PracticalSamples/petstory)
  - [3. Serviciul de povești](../../../../04-PracticalSamples/petstory)
  - [4. Șabloane web](../../../../04-PracticalSamples/petstory)
  - [5. Configurare](../../../../04-PracticalSamples/petstory)
- [Rularea aplicației](../../../../04-PracticalSamples/petstory)
- [Cum funcționează totul împreună](../../../../04-PracticalSamples/petstory)
- [Înțelegerea integrării AI](../../../../04-PracticalSamples/petstory)
- [Pașii următori](../../../../04-PracticalSamples/petstory)

## Cerințe preliminare

Înainte de a începe, asigură-te că ai:
- Java 21 sau o versiune mai recentă instalată
- Maven pentru gestionarea dependențelor
- Un cont GitHub cu un token de acces personal (PAT) cu permisiunea `models:read`
- Cunoștințe de bază despre Java, Spring Boot și dezvoltare web

## Înțelegerea structurii proiectului

Proiectul pentru povești cu animale de companie conține mai multe fișiere importante:

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

## Explicația componentelor de bază

### 1. Aplicația principală

**Fișier:** `PetStoryApplication.java`

Acesta este punctul de intrare pentru aplicația noastră Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ce face:**
- Anotarea `@SpringBootApplication` activează auto-configurarea și scanarea componentelor
- Pornește un server web integrat (Tomcat) pe portul 8080
- Creează automat toate bean-urile și serviciile necesare din Spring

### 2. Controller web

**Fișier:** `PetController.java`

Acesta gestionează toate cererile web și interacțiunile utilizatorilor:

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

**Caracteristici cheie:**

1. **Gestionarea rutelor**: `@GetMapping("/")` afișează formularul de încărcare, iar `@PostMapping("/generate-story")` procesează trimiterile
2. **Validarea intrărilor**: Verifică descrierile goale și limitele de lungime
3. **Securitate**: Sanitizează intrările utilizatorilor pentru a preveni atacurile XSS
4. **Gestionarea erorilor**: Oferă povești de rezervă atunci când serviciul AI eșuează
5. **Legarea modelului**: Transmite date către șabloanele HTML folosind `Model` din Spring

**Sistem de rezervă:**
Controller-ul include șabloane de povești pre-scrise care sunt utilizate atunci când serviciul AI nu este disponibil:

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

### 3. Serviciul de povești

**Fișier:** `StoryService.java`

Acest serviciu comunică cu GitHub Models pentru a genera povești:

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

**Componente cheie:**

1. **Client OpenAI**: Utilizează SDK-ul oficial OpenAI pentru Java configurat pentru GitHub Models
2. **Prompt de sistem**: Setează comportamentul AI pentru a scrie povești prietenoase cu familia
3. **Prompt pentru utilizator**: Spune AI-ului exact ce poveste să scrie pe baza descrierii
4. **Parametri**: Controlează lungimea poveștii și nivelul de creativitate
5. **Gestionarea erorilor**: Aruncă excepții pe care controller-ul le prinde și le gestionează

### 4. Șabloane web

**Fișier:** `index.html` (Formular de încărcare)

Pagina principală unde utilizatorii descriu animalele lor de companie:

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

**Fișier:** `result.html` (Afișarea poveștii)

Afișează povestea generată:

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

**Caracteristici ale șabloanelor:**

1. **Integrare Thymeleaf**: Utilizează atribute `th:` pentru conținut dinamic
2. **Design responsiv**: Stilizare CSS pentru mobil și desktop
3. **Gestionarea erorilor**: Afișează erorile de validare utilizatorilor
4. **Procesare pe partea clientului**: JavaScript pentru analiza imaginilor (folosind Transformers.js)

### 5. Configurare

**Fișier:** `application.properties`

Setări de configurare pentru aplicație:

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

**Explicația configurării:**

1. **Încărcare fișiere**: Permite imagini de până la 10MB
2. **Logare**: Controlează ce informații sunt înregistrate în timpul execuției
3. **GitHub Models**: Specifică ce model AI și endpoint să fie utilizate
4. **Securitate**: Configurare pentru gestionarea erorilor, evitând expunerea informațiilor sensibile

## Rularea aplicației

### Pasul 1: Setează token-ul GitHub

Mai întâi, trebuie să setezi token-ul GitHub ca variabilă de mediu:

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

**De ce este necesar:**
- GitHub Models necesită autentificare pentru a accesa modelele AI
- Utilizarea variabilelor de mediu păstrează token-urile sensibile în afara codului sursă
- Permisiunea `models:read` oferă acces la inferența AI

### Pasul 2: Construiește și rulează

Navighează la directorul proiectului:
```bash
cd 04-PracticalSamples/petstory
```

Construiește aplicația:
```bash
mvn clean compile
```

Pornește serverul:
```bash
mvn spring-boot:run
```

Aplicația va porni pe `http://localhost:8080`.

### Pasul 3: Testează aplicația

1. **Deschide** `http://localhost:8080` în browser
2. **Descrie** animalul tău de companie în câmpul text (ex.: "Un golden retriever jucăuș care adoră să aducă mingea")
3. **Apasă** "Generate Story" pentru a obține o poveste generată de AI
4. **Alternativ**, încarcă o imagine a animalului tău pentru a genera automat o descriere
5. **Vizualizează** povestea creativă bazată pe descrierea animalului tău

## Cum funcționează totul împreună

Iată fluxul complet atunci când generezi o poveste cu animale de companie:

1. **Intrare utilizator**: Descrii animalul tău de companie în formularul web
2. **Trimiterea formularului**: Browser-ul trimite o cerere POST către `/generate-story`
3. **Procesarea în controller**: `PetController` validează și sanitizează intrarea
4. **Apel către serviciul AI**: `StoryService` trimite cererea către API-ul GitHub Models
5. **Generarea poveștii**: AI generează o poveste creativă bazată pe descriere
6. **Gestionarea răspunsului**: Controller-ul primește povestea și o adaugă în model
7. **Redarea șablonului**: Thymeleaf redă `result.html` cu povestea
8. **Afișare**: Utilizatorul vede povestea generată în browser

**Fluxul de gestionare a erorilor:**
Dacă serviciul AI eșuează:
1. Controller-ul prinde excepția
2. Generează o poveste de rezervă folosind șabloane pre-scrise
3. Afișează povestea de rezervă cu o notă despre indisponibilitatea AI
4. Utilizatorul primește totuși o poveste, asigurând o experiență bună

## Înțelegerea integrării AI

### API-ul GitHub Models
Aplicația utilizează GitHub Models, care oferă acces gratuit la diverse modele AI:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Ingineria prompturilor
Serviciul folosește prompturi atent concepute pentru a obține rezultate bune:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Procesarea răspunsurilor
Răspunsul AI este extras și validat:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Pașii următori

Pentru mai multe exemple, vezi [Capitolul 04: Exemple practice](../README.md)

**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să fiți conștienți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa natală ar trebui considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea din utilizarea acestei traduceri.