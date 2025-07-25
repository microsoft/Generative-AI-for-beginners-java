<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:23:20+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "br"
}
-->
# Tutorial do Gerador de Histórias de Pets para Iniciantes

## Índice

- [Pré-requisitos](../../../../04-PracticalSamples/petstory)
- [Entendendo a Estrutura do Projeto](../../../../04-PracticalSamples/petstory)
- [Componentes Principais Explicados](../../../../04-PracticalSamples/petstory)
  - [1. Aplicação Principal](../../../../04-PracticalSamples/petstory)
  - [2. Controlador Web](../../../../04-PracticalSamples/petstory)
  - [3. Serviço de Histórias](../../../../04-PracticalSamples/petstory)
  - [4. Templates Web](../../../../04-PracticalSamples/petstory)
  - [5. Configuração](../../../../04-PracticalSamples/petstory)
- [Executando a Aplicação](../../../../04-PracticalSamples/petstory)
- [Como Tudo Funciona Junto](../../../../04-PracticalSamples/petstory)
- [Entendendo a Integração com IA](../../../../04-PracticalSamples/petstory)
- [Próximos Passos](../../../../04-PracticalSamples/petstory)

## Pré-requisitos

Antes de começar, certifique-se de ter:
- Java 21 ou superior instalado
- Maven para gerenciamento de dependências
- Uma conta no GitHub com um token de acesso pessoal (PAT) com escopo `models:read`
- Conhecimento básico de Java, Spring Boot e desenvolvimento web

## Entendendo a Estrutura do Projeto

O projeto de histórias de pets possui vários arquivos importantes:

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

## Componentes Principais Explicados

### 1. Aplicação Principal

**Arquivo:** `PetStoryApplication.java`

Este é o ponto de entrada para nossa aplicação Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**O que isso faz:**
- A anotação `@SpringBootApplication` habilita a configuração automática e a varredura de componentes
- Inicia um servidor web embutido (Tomcat) na porta 8080
- Cria automaticamente todos os beans e serviços necessários do Spring

### 2. Controlador Web

**Arquivo:** `PetController.java`

Este componente gerencia todas as requisições web e interações com o usuário:

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

**Principais características:**

1. **Manipulação de Rotas**: `@GetMapping("/")` exibe o formulário de upload, `@PostMapping("/generate-story")` processa as submissões
2. **Validação de Entrada**: Verifica descrições vazias e limites de tamanho
3. **Segurança**: Sanitiza entradas do usuário para prevenir ataques XSS
4. **Tratamento de Erros**: Fornece histórias alternativas quando o serviço de IA falha
5. **Vinculação de Modelo**: Passa dados para templates HTML usando o `Model` do Spring

**Sistema de Contingência:**
O controlador inclui templates de histórias pré-escritas que são usados quando o serviço de IA está indisponível:

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

### 3. Serviço de Histórias

**Arquivo:** `StoryService.java`

Este serviço se comunica com os Modelos do GitHub para gerar histórias:

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

**Componentes principais:**

1. **Cliente OpenAI**: Usa o SDK oficial do OpenAI configurado para os Modelos do GitHub
2. **Prompt do Sistema**: Define o comportamento da IA para escrever histórias de pets apropriadas para a família
3. **Prompt do Usuário**: Instrui a IA sobre qual história escrever com base na descrição
4. **Parâmetros**: Controla o tamanho da história e o nível de criatividade
5. **Tratamento de Erros**: Lança exceções que o controlador captura e gerencia

### 4. Templates Web

**Arquivo:** `index.html` (Formulário de Upload)

A página principal onde os usuários descrevem seus pets:

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

**Arquivo:** `result.html` (Exibição da História)

Exibe a história gerada:

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

**Características dos templates:**

1. **Integração com Thymeleaf**: Usa atributos `th:` para conteúdo dinâmico
2. **Design Responsivo**: Estilização CSS para dispositivos móveis e desktop
3. **Tratamento de Erros**: Exibe erros de validação para os usuários
4. **Processamento no Cliente**: JavaScript para análise de imagens (usando Transformers.js)

### 5. Configuração

**Arquivo:** `application.properties`

Configurações da aplicação:

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

**Configuração explicada:**

1. **Upload de Arquivos**: Permite imagens de até 10MB
2. **Log**: Controla quais informações são registradas durante a execução
3. **Modelos do GitHub**: Especifica qual modelo de IA e endpoint usar
4. **Segurança**: Configuração de tratamento de erros para evitar exposição de informações sensíveis

## Executando a Aplicação

### Passo 1: Configure Seu Token do GitHub

Primeiro, você precisa configurar seu token do GitHub como uma variável de ambiente:

**Windows (Prompt de Comando):**
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

**Por que isso é necessário:**
- Os Modelos do GitHub exigem autenticação para acessar modelos de IA
- Usar variáveis de ambiente mantém tokens sensíveis fora do código-fonte
- O escopo `models:read` fornece acesso à inferência de IA

### Passo 2: Compile e Execute

Navegue até o diretório do projeto:
```bash
cd 04-PracticalSamples/petstory
```

Compile a aplicação:
```bash
mvn clean compile
```

Inicie o servidor:
```bash
mvn spring-boot:run
```

A aplicação será iniciada em `http://localhost:8080`.

### Passo 3: Teste a Aplicação

1. **Abra** `http://localhost:8080` no seu navegador
2. **Descreva** seu pet na área de texto (ex.: "Um golden retriever brincalhão que adora buscar objetos")
3. **Clique** em "Generate Story" para obter uma história gerada pela IA
4. **Alternativamente**, envie uma imagem do pet para gerar automaticamente uma descrição
5. **Veja** a história criativa baseada na descrição do seu pet

## Como Tudo Funciona Junto

Aqui está o fluxo completo ao gerar uma história de pet:

1. **Entrada do Usuário**: Você descreve seu pet no formulário web
2. **Submissão do Formulário**: O navegador envia uma requisição POST para `/generate-story`
3. **Processamento no Controlador**: `PetController` valida e sanitiza a entrada
4. **Chamada ao Serviço de IA**: `StoryService` envia uma requisição para a API de Modelos do GitHub
5. **Geração da História**: A IA cria uma história criativa com base na descrição
6. **Tratamento da Resposta**: O controlador recebe a história e a adiciona ao modelo
7. **Renderização do Template**: O Thymeleaf renderiza `result.html` com a história
8. **Exibição**: O usuário vê a história gerada no navegador

**Fluxo de Tratamento de Erros:**
Se o serviço de IA falhar:
1. O controlador captura a exceção
2. Gera uma história alternativa usando templates pré-escritos
3. Exibe a história alternativa com uma nota sobre a indisponibilidade da IA
4. O usuário ainda recebe uma história, garantindo uma boa experiência

## Entendendo a Integração com IA

### API de Modelos do GitHub
A aplicação usa os Modelos do GitHub, que fornecem acesso gratuito a diversos modelos de IA:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Engenharia de Prompts
O serviço utiliza prompts cuidadosamente elaborados para obter bons resultados:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Processamento de Respostas
A resposta da IA é extraída e validada:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Próximos Passos

Para mais exemplos, veja [Capítulo 04: Exemplos práticos](../README.md)

**Aviso Legal**:  
Este documento foi traduzido utilizando o serviço de tradução por IA [Co-op Translator](https://github.com/Azure/co-op-translator). Embora nos esforcemos para garantir a precisão, esteja ciente de que traduções automatizadas podem conter erros ou imprecisões. O documento original em seu idioma nativo deve ser considerado a fonte autoritativa. Para informações críticas, recomenda-se a tradução profissional realizada por humanos. Não nos responsabilizamos por quaisquer mal-entendidos ou interpretações equivocadas decorrentes do uso desta tradução.