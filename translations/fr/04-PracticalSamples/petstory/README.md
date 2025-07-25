<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:35:29+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "fr"
}
-->
# Tutoriel du générateur d'histoires pour animaux de compagnie pour débutants

## Table des matières

- [Prérequis](../../../../04-PracticalSamples/petstory)
- [Comprendre la structure du projet](../../../../04-PracticalSamples/petstory)
- [Explication des composants principaux](../../../../04-PracticalSamples/petstory)
  - [1. Application principale](../../../../04-PracticalSamples/petstory)
  - [2. Contrôleur web](../../../../04-PracticalSamples/petstory)
  - [3. Service d'histoires](../../../../04-PracticalSamples/petstory)
  - [4. Modèles web](../../../../04-PracticalSamples/petstory)
  - [5. Configuration](../../../../04-PracticalSamples/petstory)
- [Exécution de l'application](../../../../04-PracticalSamples/petstory)
- [Comment tout fonctionne ensemble](../../../../04-PracticalSamples/petstory)
- [Comprendre l'intégration de l'IA](../../../../04-PracticalSamples/petstory)
- [Prochaines étapes](../../../../04-PracticalSamples/petstory)

## Prérequis

Avant de commencer, assurez-vous d'avoir :
- Java 21 ou une version supérieure installée
- Maven pour la gestion des dépendances
- Un compte GitHub avec un jeton d'accès personnel (PAT) avec l'autorisation `models:read`
- Une compréhension de base de Java, Spring Boot et du développement web

## Comprendre la structure du projet

Le projet d'histoires pour animaux de compagnie contient plusieurs fichiers importants :

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

## Explication des composants principaux

### 1. Application principale

**Fichier :** `PetStoryApplication.java`

C'est le point d'entrée de notre application Spring Boot :

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Ce que cela fait :**
- L'annotation `@SpringBootApplication` active la configuration automatique et le balayage des composants
- Démarre un serveur web intégré (Tomcat) sur le port 8080
- Crée automatiquement tous les beans et services nécessaires de Spring

### 2. Contrôleur web

**Fichier :** `PetController.java`

Ce composant gère toutes les requêtes web et les interactions utilisateur :

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

**Caractéristiques principales :**

1. **Gestion des routes** : `@GetMapping("/")` affiche le formulaire de téléchargement, `@PostMapping("/generate-story")` traite les soumissions
2. **Validation des entrées** : Vérifie les descriptions vides et les limites de longueur
3. **Sécurité** : Assainit les entrées utilisateur pour éviter les attaques XSS
4. **Gestion des erreurs** : Fournit des histoires de secours lorsque le service d'IA échoue
5. **Liaison de modèle** : Transmet les données aux modèles HTML en utilisant le `Model` de Spring

**Système de secours :**
Le contrôleur inclut des modèles d'histoires pré-écrites qui sont utilisés lorsque le service d'IA est indisponible :

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

### 3. Service d'histoires

**Fichier :** `StoryService.java`

Ce service communique avec GitHub Models pour générer des histoires :

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

**Composants clés :**

1. **Client OpenAI** : Utilise le SDK Java officiel d'OpenAI configuré pour GitHub Models
2. **Prompt système** : Définit le comportement de l'IA pour écrire des histoires adaptées à la famille
3. **Prompt utilisateur** : Indique à l'IA exactement quelle histoire écrire en fonction de la description
4. **Paramètres** : Contrôle la longueur de l'histoire et le niveau de créativité
5. **Gestion des erreurs** : Lève des exceptions que le contrôleur intercepte et gère

### 4. Modèles web

**Fichier :** `index.html` (Formulaire de téléchargement)

La page principale où les utilisateurs décrivent leurs animaux de compagnie :

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

**Fichier :** `result.html` (Affichage de l'histoire)

Affiche l'histoire générée :

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

**Caractéristiques des modèles :**

1. **Intégration Thymeleaf** : Utilise les attributs `th:` pour le contenu dynamique
2. **Design réactif** : Style CSS pour mobile et bureau
3. **Gestion des erreurs** : Affiche les erreurs de validation aux utilisateurs
4. **Traitement côté client** : JavaScript pour l'analyse d'images (avec Transformers.js)

### 5. Configuration

**Fichier :** `application.properties`

Paramètres de configuration pour l'application :

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

**Explication de la configuration :**

1. **Téléchargement de fichiers** : Autorise les images jusqu'à 10 Mo
2. **Journalisation** : Contrôle les informations enregistrées pendant l'exécution
3. **GitHub Models** : Spécifie le modèle d'IA et le point de terminaison à utiliser
4. **Sécurité** : Configuration de la gestion des erreurs pour éviter d'exposer des informations sensibles

## Exécution de l'application

### Étape 1 : Définir votre jeton GitHub

Tout d'abord, vous devez définir votre jeton GitHub comme variable d'environnement :

**Windows (Invite de commandes) :**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell) :**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS :**
```bash
export GITHUB_TOKEN=your_github_token_here
```

**Pourquoi c'est nécessaire :**
- GitHub Models nécessite une authentification pour accéder aux modèles d'IA
- Utiliser des variables d'environnement permet de garder les jetons sensibles hors du code source
- L'autorisation `models:read` donne accès à l'inférence de l'IA

### Étape 2 : Construire et exécuter

Accédez au répertoire du projet :
```bash
cd 04-PracticalSamples/petstory
```

Construisez l'application :
```bash
mvn clean compile
```

Démarrez le serveur :
```bash
mvn spring-boot:run
```

L'application démarrera sur `http://localhost:8080`.

### Étape 3 : Tester l'application

1. **Ouvrez** `http://localhost:8080` dans votre navigateur
2. **Décrivez** votre animal de compagnie dans la zone de texte (par exemple, "Un golden retriever joueur qui adore rapporter des objets")
3. **Cliquez** sur "Générer une histoire" pour obtenir une histoire générée par l'IA
4. **Alternativement**, téléchargez une image de votre animal pour générer automatiquement une description
5. **Visualisez** l'histoire créative basée sur la description de votre animal

## Comment tout fonctionne ensemble

Voici le flux complet lorsque vous générez une histoire pour animal de compagnie :

1. **Entrée utilisateur** : Vous décrivez votre animal sur le formulaire web
2. **Soumission du formulaire** : Le navigateur envoie une requête POST à `/generate-story`
3. **Traitement par le contrôleur** : `PetController` valide et assainit l'entrée
4. **Appel au service d'IA** : `StoryService` envoie une requête à l'API GitHub Models
5. **Génération d'histoire** : L'IA génère une histoire créative basée sur la description
6. **Gestion de la réponse** : Le contrôleur reçoit l'histoire et l'ajoute au modèle
7. **Rendu du modèle** : Thymeleaf rend `result.html` avec l'histoire
8. **Affichage** : L'utilisateur voit l'histoire générée dans son navigateur

**Flux de gestion des erreurs :**
Si le service d'IA échoue :
1. Le contrôleur intercepte l'exception
2. Génère une histoire de secours en utilisant des modèles pré-écrits
3. Affiche l'histoire de secours avec une note sur l'indisponibilité de l'IA
4. L'utilisateur obtient quand même une histoire, garantissant une bonne expérience utilisateur

## Comprendre l'intégration de l'IA

### API GitHub Models
L'application utilise GitHub Models, qui offre un accès gratuit à divers modèles d'IA :

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Conception des prompts
Le service utilise des prompts soigneusement conçus pour obtenir de bons résultats :

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Traitement des réponses
La réponse de l'IA est extraite et validée :

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Prochaines étapes

Pour plus d'exemples, consultez [Chapitre 04 : Exemples pratiques](../README.md)

**Avertissement** :  
Ce document a été traduit à l'aide du service de traduction automatique [Co-op Translator](https://github.com/Azure/co-op-translator). Bien que nous nous efforcions d'assurer l'exactitude, veuillez noter que les traductions automatisées peuvent contenir des erreurs ou des inexactitudes. Le document original dans sa langue d'origine doit être considéré comme la source faisant autorité. Pour des informations critiques, il est recommandé de recourir à une traduction professionnelle réalisée par un humain. Nous déclinons toute responsabilité en cas de malentendus ou d'interprétations erronées résultant de l'utilisation de cette traduction.