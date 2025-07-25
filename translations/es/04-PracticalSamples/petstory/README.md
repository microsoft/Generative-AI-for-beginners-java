<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T10:37:43+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "es"
}
-->
# Tutorial del Generador de Historias de Mascotas para Principiantes

## Tabla de Contenidos

- [Requisitos Previos](../../../../04-PracticalSamples/petstory)
- [Entendiendo la Estructura del Proyecto](../../../../04-PracticalSamples/petstory)
- [Componentes Principales Explicados](../../../../04-PracticalSamples/petstory)
  - [1. Aplicación Principal](../../../../04-PracticalSamples/petstory)
  - [2. Controlador Web](../../../../04-PracticalSamples/petstory)
  - [3. Servicio de Historias](../../../../04-PracticalSamples/petstory)
  - [4. Plantillas Web](../../../../04-PracticalSamples/petstory)
  - [5. Configuración](../../../../04-PracticalSamples/petstory)
- [Ejecutando la Aplicación](../../../../04-PracticalSamples/petstory)
- [Cómo Funciona Todo Junto](../../../../04-PracticalSamples/petstory)
- [Entendiendo la Integración con IA](../../../../04-PracticalSamples/petstory)
- [Próximos Pasos](../../../../04-PracticalSamples/petstory)

## Requisitos Previos

Antes de comenzar, asegúrate de tener:
- Java 21 o superior instalado
- Maven para la gestión de dependencias
- Una cuenta de GitHub con un token de acceso personal (PAT) con el alcance `models:read`
- Conocimientos básicos de Java, Spring Boot y desarrollo web

## Entendiendo la Estructura del Proyecto

El proyecto de historias de mascotas contiene varios archivos importantes:

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

## Componentes Principales Explicados

### 1. Aplicación Principal

**Archivo:** `PetStoryApplication.java`

Este es el punto de entrada para nuestra aplicación Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Qué hace:**
- La anotación `@SpringBootApplication` habilita la configuración automática y el escaneo de componentes
- Inicia un servidor web embebido (Tomcat) en el puerto 8080
- Crea automáticamente todos los beans y servicios necesarios de Spring

### 2. Controlador Web

**Archivo:** `PetController.java`

Este se encarga de todas las solicitudes web e interacciones con el usuario:

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

**Características clave:**

1. **Manejo de Rutas**: `@GetMapping("/")` muestra el formulario de carga, `@PostMapping("/generate-story")` procesa las solicitudes
2. **Validación de Entrada**: Verifica descripciones vacías y límites de longitud
3. **Seguridad**: Sanitiza las entradas del usuario para prevenir ataques XSS
4. **Manejo de Errores**: Proporciona historias de respaldo cuando el servicio de IA falla
5. **Vinculación de Modelo**: Pasa datos a las plantillas HTML usando el `Model` de Spring

**Sistema de Respaldo:**
El controlador incluye plantillas de historias preescritas que se usan cuando el servicio de IA no está disponible:

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

### 3. Servicio de Historias

**Archivo:** `StoryService.java`

Este servicio se comunica con GitHub Models para generar historias:

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

**Componentes clave:**

1. **Cliente OpenAI**: Usa el SDK oficial de OpenAI para Java configurado para GitHub Models
2. **Prompt del Sistema**: Configura el comportamiento de la IA para escribir historias familiares y amigables
3. **Prompt del Usuario**: Indica a la IA exactamente qué historia escribir según la descripción
4. **Parámetros**: Controla la longitud de la historia y el nivel de creatividad
5. **Manejo de Errores**: Lanza excepciones que el controlador captura y maneja

### 4. Plantillas Web

**Archivo:** `index.html` (Formulario de Carga)

La página principal donde los usuarios describen a sus mascotas:

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

**Archivo:** `result.html` (Visualización de la Historia)

Muestra la historia generada:

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

**Características de las plantillas:**

1. **Integración con Thymeleaf**: Usa atributos `th:` para contenido dinámico
2. **Diseño Responsivo**: Estilo CSS para móviles y escritorio
3. **Manejo de Errores**: Muestra errores de validación a los usuarios
4. **Procesamiento en el Cliente**: JavaScript para análisis de imágenes (usando Transformers.js)

### 5. Configuración

**Archivo:** `application.properties`

Configuraciones de la aplicación:

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

**Explicación de la configuración:**

1. **Carga de Archivos**: Permite imágenes de hasta 10MB
2. **Registro**: Controla qué información se registra durante la ejecución
3. **GitHub Models**: Especifica qué modelo de IA y endpoint usar
4. **Seguridad**: Configuración de manejo de errores para evitar exponer información sensible

## Ejecutando la Aplicación

### Paso 1: Configura tu Token de GitHub

Primero, necesitas configurar tu token de GitHub como una variable de entorno:

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

**Por qué es necesario:**
- GitHub Models requiere autenticación para acceder a los modelos de IA
- Usar variables de entorno mantiene los tokens sensibles fuera del código fuente
- El alcance `models:read` proporciona acceso a la inferencia de IA

### Paso 2: Compila y Ejecuta

Navega al directorio del proyecto:
```bash
cd 04-PracticalSamples/petstory
```

Compila la aplicación:
```bash
mvn clean compile
```

Inicia el servidor:
```bash
mvn spring-boot:run
```

La aplicación se iniciará en `http://localhost:8080`.

### Paso 3: Prueba la Aplicación

1. **Abre** `http://localhost:8080` en tu navegador
2. **Describe** a tu mascota en el área de texto (por ejemplo, "Un golden retriever juguetón que ama buscar pelotas")
3. **Haz clic** en "Generar Historia" para obtener una historia generada por IA
4. **Alternativamente**, sube una imagen de tu mascota para generar automáticamente una descripción
5. **Visualiza** la historia creativa basada en la descripción de tu mascota

## Cómo Funciona Todo Junto

Aquí está el flujo completo cuando generas una historia de mascotas:

1. **Entrada del Usuario**: Describes a tu mascota en el formulario web
2. **Envío del Formulario**: El navegador envía una solicitud POST a `/generate-story`
3. **Procesamiento del Controlador**: `PetController` valida y sanitiza la entrada
4. **Llamada al Servicio de IA**: `StoryService` envía una solicitud a la API de GitHub Models
5. **Generación de la Historia**: La IA genera una historia creativa basada en la descripción
6. **Manejo de Respuesta**: El controlador recibe la historia y la agrega al modelo
7. **Renderizado de Plantilla**: Thymeleaf renderiza `result.html` con la historia
8. **Visualización**: El usuario ve la historia generada en su navegador

**Flujo de Manejo de Errores:**
Si el servicio de IA falla:
1. El controlador captura la excepción
2. Genera una historia de respaldo usando plantillas preescritas
3. Muestra la historia de respaldo con una nota sobre la indisponibilidad de la IA
4. El usuario aún recibe una historia, asegurando una buena experiencia de usuario

## Entendiendo la Integración con IA

### API de GitHub Models
La aplicación usa GitHub Models, que proporciona acceso gratuito a varios modelos de IA:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Ingeniería de Prompts
El servicio utiliza prompts cuidadosamente diseñados para obtener buenos resultados:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Procesamiento de Respuestas
La respuesta de la IA se extrae y valida:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Próximos Pasos

Para más ejemplos, consulta [Capítulo 04: Ejemplos prácticos](../README.md)

**Descargo de responsabilidad**:  
Este documento ha sido traducido utilizando el servicio de traducción automática [Co-op Translator](https://github.com/Azure/co-op-translator). Aunque nos esforzamos por garantizar la precisión, tenga en cuenta que las traducciones automatizadas pueden contener errores o imprecisiones. El documento original en su idioma nativo debe considerarse como la fuente autorizada. Para información crítica, se recomienda una traducción profesional realizada por humanos. No nos hacemos responsables de malentendidos o interpretaciones erróneas que puedan surgir del uso de esta traducción.