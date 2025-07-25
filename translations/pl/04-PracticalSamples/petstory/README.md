<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:28:17+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "pl"
}
-->
# Samouczek Generatora Opowieści o Zwierzętach dla Początkujących

## Spis Treści

- [Wymagania wstępne](../../../../04-PracticalSamples/petstory)
- [Zrozumienie struktury projektu](../../../../04-PracticalSamples/petstory)
- [Wyjaśnienie kluczowych komponentów](../../../../04-PracticalSamples/petstory)
  - [1. Główna aplikacja](../../../../04-PracticalSamples/petstory)
  - [2. Kontroler webowy](../../../../04-PracticalSamples/petstory)
  - [3. Usługa generowania opowieści](../../../../04-PracticalSamples/petstory)
  - [4. Szablony webowe](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguracja](../../../../04-PracticalSamples/petstory)
- [Uruchamianie aplikacji](../../../../04-PracticalSamples/petstory)
- [Jak to wszystko działa razem](../../../../04-PracticalSamples/petstory)
- [Zrozumienie integracji z AI](../../../../04-PracticalSamples/petstory)
- [Kolejne kroki](../../../../04-PracticalSamples/petstory)

## Wymagania wstępne

Przed rozpoczęciem upewnij się, że masz:
- Zainstalowaną Javę 21 lub nowszą
- Maven do zarządzania zależnościami
- Konto GitHub z tokenem dostępu osobistego (PAT) z zakresem `models:read`
- Podstawową wiedzę o Javie, Spring Boot i tworzeniu aplikacji webowych

## Zrozumienie struktury projektu

Projekt generatora opowieści o zwierzętach zawiera kilka ważnych plików:

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

## Wyjaśnienie kluczowych komponentów

### 1. Główna aplikacja

**Plik:** `PetStoryApplication.java`

To punkt wejścia do naszej aplikacji Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Co robi ten plik:**
- Adnotacja `@SpringBootApplication` włącza automatyczną konfigurację i skanowanie komponentów
- Uruchamia wbudowany serwer webowy (Tomcat) na porcie 8080
- Automatycznie tworzy wszystkie niezbędne beany i usługi Springa

### 2. Kontroler webowy

**Plik:** `PetController.java`

Obsługuje wszystkie żądania webowe i interakcje użytkownika:

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

**Kluczowe funkcje:**

1. **Obsługa tras:** `@GetMapping("/")` wyświetla formularz przesyłania, `@PostMapping("/generate-story")` przetwarza zgłoszenia
2. **Walidacja danych wejściowych:** Sprawdza, czy opisy nie są puste i czy mieszczą się w limitach długości
3. **Bezpieczeństwo:** Oczyszcza dane wejściowe użytkownika, aby zapobiec atakom XSS
4. **Obsługa błędów:** Zapewnia opowieści zapasowe, gdy usługa AI zawiedzie
5. **Powiązanie modelu:** Przekazuje dane do szablonów HTML za pomocą `Model` Springa

**System zapasowy:**
Kontroler zawiera wstępnie napisane szablony opowieści, które są używane, gdy usługa AI jest niedostępna:

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

### 3. Usługa generowania opowieści

**Plik:** `StoryService.java`

Ta usługa komunikuje się z GitHub Models, aby generować opowieści:

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

**Kluczowe komponenty:**

1. **Klient OpenAI:** Korzysta z oficjalnego SDK OpenAI skonfigurowanego dla GitHub Models
2. **System Prompt:** Ustawia zachowanie AI na pisanie przyjaznych rodzinie opowieści o zwierzętach
3. **User Prompt:** Informuje AI, jaką opowieść napisać na podstawie opisu
4. **Parametry:** Kontroluje długość opowieści i poziom kreatywności
5. **Obsługa błędów:** Rzuca wyjątki, które są przechwytywane i obsługiwane przez kontroler

### 4. Szablony webowe

**Plik:** `index.html` (Formularz przesyłania)

Główna strona, na której użytkownicy opisują swoje zwierzęta:

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

**Plik:** `result.html` (Wyświetlanie opowieści)

Pokazuje wygenerowaną opowieść:

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

**Funkcje szablonów:**

1. **Integracja Thymeleaf:** Używa atrybutów `th:` do dynamicznej zawartości
2. **Responsywny design:** Stylizacja CSS dla urządzeń mobilnych i komputerów
3. **Obsługa błędów:** Wyświetla błędy walidacji użytkownikom
4. **Przetwarzanie po stronie klienta:** JavaScript do analizy obrazów (z użyciem Transformers.js)

### 5. Konfiguracja

**Plik:** `application.properties`

Ustawienia konfiguracyjne aplikacji:

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

**Wyjaśnienie konfiguracji:**

1. **Przesyłanie plików:** Pozwala na przesyłanie obrazów do 10 MB
2. **Logowanie:** Kontroluje, jakie informacje są logowane podczas działania aplikacji
3. **GitHub Models:** Określa, który model AI i punkt końcowy są używane
4. **Bezpieczeństwo:** Konfiguracja obsługi błędów, aby uniknąć ujawniania wrażliwych informacji

## Uruchamianie aplikacji

### Krok 1: Ustaw swój token GitHub

Najpierw ustaw swój token GitHub jako zmienną środowiskową:

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

**Dlaczego to jest potrzebne:**
- GitHub Models wymaga uwierzytelnienia, aby uzyskać dostęp do modeli AI
- Używanie zmiennych środowiskowych chroni wrażliwe tokeny przed ujawnieniem w kodzie źródłowym
- Zakres `models:read` zapewnia dostęp do inferencji AI

### Krok 2: Budowanie i uruchamianie

Przejdź do katalogu projektu:
```bash
cd 04-PracticalSamples/petstory
```

Zbuduj aplikację:
```bash
mvn clean compile
```

Uruchom serwer:
```bash
mvn spring-boot:run
```

Aplikacja uruchomi się pod adresem `http://localhost:8080`.

### Krok 3: Testowanie aplikacji

1. **Otwórz** `http://localhost:8080` w przeglądarce
2. **Opisz** swoje zwierzę w polu tekstowym (np. "Wesoły golden retriever, który uwielbia aportować")
3. **Kliknij** "Generate Story", aby otrzymać opowieść wygenerowaną przez AI
4. **Alternatywnie**, prześlij obraz zwierzęcia, aby automatycznie wygenerować opis
5. **Zobacz** kreatywną opowieść na podstawie opisu swojego zwierzęcia

## Jak to wszystko działa razem

Oto pełny przepływ, gdy generujesz opowieść o zwierzęciu:

1. **Dane wejściowe użytkownika:** Opisujesz swoje zwierzę w formularzu webowym
2. **Przesłanie formularza:** Przeglądarka wysyła żądanie POST do `/generate-story`
3. **Przetwarzanie w kontrolerze:** `PetController` sprawdza i oczyszcza dane wejściowe
4. **Wywołanie usługi AI:** `StoryService` wysyła żądanie do API GitHub Models
5. **Generowanie opowieści:** AI tworzy kreatywną opowieść na podstawie opisu
6. **Obsługa odpowiedzi:** Kontroler odbiera opowieść i dodaje ją do modelu
7. **Renderowanie szablonu:** Thymeleaf renderuje `result.html` z opowieścią
8. **Wyświetlanie:** Użytkownik widzi wygenerowaną opowieść w przeglądarce

**Przepływ obsługi błędów:**
Jeśli usługa AI zawiedzie:
1. Kontroler przechwytuje wyjątek
2. Generuje opowieść zapasową z użyciem wstępnie napisanych szablonów
3. Wyświetla opowieść zapasową z informacją o niedostępności AI
4. Użytkownik nadal otrzymuje opowieść, co zapewnia dobrą jakość doświadczenia użytkownika

## Zrozumienie integracji z AI

### API GitHub Models
Aplikacja korzysta z GitHub Models, które zapewnia darmowy dostęp do różnych modeli AI:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Inżynieria promptów
Usługa korzysta z precyzyjnie skonstruowanych promptów, aby uzyskać dobre wyniki:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Przetwarzanie odpowiedzi
Odpowiedź AI jest wyodrębniana i weryfikowana:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Kolejne kroki

Więcej przykładów znajdziesz w [Rozdziale 04: Praktyczne przykłady](../README.md)

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby tłumaczenie było precyzyjne, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego rodzimym języku powinien być uznawany za wiarygodne źródło. W przypadku informacji o kluczowym znaczeniu zaleca się skorzystanie z profesjonalnego tłumaczenia przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.