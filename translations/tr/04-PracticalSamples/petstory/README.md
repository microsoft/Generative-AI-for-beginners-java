<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:31:04+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "tr"
}
-->
# Evcil Hayvan Hikaye Üretici Eğitimi için Başlangıç Kılavuzu

## İçindekiler

- [Gereksinimler](../../../../04-PracticalSamples/petstory)
- [Proje Yapısını Anlamak](../../../../04-PracticalSamples/petstory)
- [Temel Bileşenlerin Açıklaması](../../../../04-PracticalSamples/petstory)
  - [1. Ana Uygulama](../../../../04-PracticalSamples/petstory)
  - [2. Web Denetleyicisi](../../../../04-PracticalSamples/petstory)
  - [3. Hikaye Servisi](../../../../04-PracticalSamples/petstory)
  - [4. Web Şablonları](../../../../04-PracticalSamples/petstory)
  - [5. Yapılandırma](../../../../04-PracticalSamples/petstory)
- [Uygulamayı Çalıştırma](../../../../04-PracticalSamples/petstory)
- [Tüm Bileşenlerin Birlikte Çalışması](../../../../04-PracticalSamples/petstory)
- [Yapay Zeka Entegrasyonunu Anlamak](../../../../04-PracticalSamples/petstory)
- [Sonraki Adımlar](../../../../04-PracticalSamples/petstory)

## Gereksinimler

Başlamadan önce, aşağıdakilere sahip olduğunuzdan emin olun:
- Java 21 veya daha üstü yüklü
- Bağımlılık yönetimi için Maven
- `models:read` kapsamına sahip bir kişisel erişim belirteci (PAT) içeren bir GitHub hesabı
- Java, Spring Boot ve web geliştirme hakkında temel bilgi

## Proje Yapısını Anlamak

Evcil hayvan hikaye projesi birkaç önemli dosyadan oluşur:

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

## Temel Bileşenlerin Açıklaması

### 1. Ana Uygulama

**Dosya:** `PetStoryApplication.java`

Bu, Spring Boot uygulamamızın giriş noktasıdır:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Bu ne yapar:**
- `@SpringBootApplication` anotasyonu otomatik yapılandırmayı ve bileşen taramayı etkinleştirir
- 8080 portunda gömülü bir web sunucusu (Tomcat) başlatır
- Gerekli tüm Spring bean'lerini ve servislerini otomatik olarak oluşturur

### 2. Web Denetleyicisi

**Dosya:** `PetController.java`

Bu, tüm web isteklerini ve kullanıcı etkileşimlerini yönetir:

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

**Ana özellikler:**

1. **Rota Yönetimi**: `@GetMapping("/")` yükleme formunu gösterir, `@PostMapping("/generate-story")` gönderimleri işler
2. **Girdi Doğrulama**: Boş açıklamaları ve uzunluk sınırlarını kontrol eder
3. **Güvenlik**: Kullanıcı girdilerini XSS saldırılarını önlemek için temizler
4. **Hata Yönetimi**: Yapay zeka servisi başarısız olduğunda yedek hikayeler sağlar
5. **Model Bağlama**: Verileri Spring'in `Model` sınıfını kullanarak HTML şablonlarına aktarır

**Yedekleme Sistemi:**
Denetleyici, yapay zeka servisi kullanılamadığında kullanılan önceden yazılmış hikaye şablonlarını içerir:

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

### 3. Hikaye Servisi

**Dosya:** `StoryService.java`

Bu servis, hikayeler oluşturmak için GitHub Modelleri ile iletişim kurar:

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

**Ana bileşenler:**

1. **OpenAI İstemcisi**: GitHub Modelleri için yapılandırılmış resmi OpenAI Java SDK'sını kullanır
2. **Sistem Komutu**: Yapay zekanın aile dostu evcil hayvan hikayeleri yazmasını sağlar
3. **Kullanıcı Komutu**: Yapay zekaya açıklamaya dayalı olarak tam olarak ne tür bir hikaye yazması gerektiğini söyler
4. **Parametreler**: Hikaye uzunluğunu ve yaratıcılık seviyesini kontrol eder
5. **Hata Yönetimi**: Denetleyicinin yakalayıp işleyeceği istisnalar oluşturur

### 4. Web Şablonları

**Dosya:** `index.html` (Yükleme Formu)

Kullanıcıların evcil hayvanlarını tanımladığı ana sayfa:

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

**Dosya:** `result.html` (Hikaye Gösterimi)

Oluşturulan hikayeyi gösterir:

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

**Şablon özellikleri:**

1. **Thymeleaf Entegrasyonu**: Dinamik içerik için `th:` özniteliklerini kullanır
2. **Duyarlı Tasarım**: Mobil ve masaüstü için CSS stilleri
3. **Hata Yönetimi**: Kullanıcılara doğrulama hatalarını gösterir
4. **İstemci Tarafı İşleme**: Görüntü analizi için JavaScript (Transformers.js kullanarak)

### 5. Yapılandırma

**Dosya:** `application.properties`

Uygulama için yapılandırma ayarları:

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

**Yapılandırma açıklaması:**

1. **Dosya Yükleme**: 10MB'a kadar görüntülere izin verir
2. **Günlükleme**: Çalışma sırasında hangi bilgilerin kaydedileceğini kontrol eder
3. **GitHub Modelleri**: Hangi yapay zeka modeli ve uç noktanın kullanılacağını belirtir
4. **Güvenlik**: Hassas bilgilerin açığa çıkmasını önlemek için hata yönetimi yapılandırması

## Uygulamayı Çalıştırma

### Adım 1: GitHub Belirtecinizi Ayarlayın

Öncelikle, GitHub belirtecinizi bir ortam değişkeni olarak ayarlamanız gerekir:

**Windows (Komut İstemi):**
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

**Neden gerekli:**
- GitHub Modelleri, yapay zeka modellerine erişim için kimlik doğrulama gerektirir
- Ortam değişkenlerini kullanmak, hassas belirteçlerin kaynak kodunda yer almasını önler
- `models:read` kapsamı, yapay zeka çıkarımına erişim sağlar

### Adım 2: Derleme ve Çalıştırma

Proje dizinine gidin:
```bash
cd 04-PracticalSamples/petstory
```

Uygulamayı derleyin:
```bash
mvn clean compile
```

Sunucuyu başlatın:
```bash
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde başlayacaktır.

### Adım 3: Uygulamayı Test Edin

1. **Açın** `http://localhost:8080` tarayıcınızda
2. **Tanımlayın** evcil hayvanınızı metin alanında (ör. "Top oynamayı seven neşeli bir golden retriever")
3. **Tıklayın** "Hikaye Oluştur" butonuna, yapay zeka tarafından oluşturulan bir hikaye alın
4. **Alternatif olarak**, bir evcil hayvan resmi yükleyerek otomatik bir açıklama oluşturabilirsiniz
5. **Görün** evcil hayvanınızın açıklamasına dayalı yaratıcı hikayeyi

## Tüm Bileşenlerin Birlikte Çalışması

Bir evcil hayvan hikayesi oluşturduğunuzda tüm süreç şu şekilde işler:

1. **Kullanıcı Girdisi**: Web formunda evcil hayvanınızı tanımlarsınız
2. **Form Gönderimi**: Tarayıcı, `/generate-story` adresine POST isteği gönderir
3. **Denetleyici İşlemi**: `PetController` girdiyi doğrular ve temizler
4. **Yapay Zeka Servis Çağrısı**: `StoryService`, GitHub Modelleri API'sine istek gönderir
5. **Hikaye Oluşturma**: Yapay zeka, açıklamaya dayalı yaratıcı bir hikaye oluşturur
6. **Yanıt İşleme**: Denetleyici hikayeyi alır ve modele ekler
7. **Şablon İşleme**: Thymeleaf, hikaye ile `result.html` dosyasını işler
8. **Gösterim**: Kullanıcı, tarayıcısında oluşturulan hikayeyi görür

**Hata Yönetimi Akışı:**
Eğer yapay zeka servisi başarısız olursa:
1. Denetleyici istisnayı yakalar
2. Önceden yazılmış şablonları kullanarak bir yedek hikaye oluşturur
3. Yapay zeka kullanılabilirliği hakkında bir not ile yedek hikayeyi gösterir
4. Kullanıcı yine de bir hikaye alır, böylece iyi bir kullanıcı deneyimi sağlanır

## Yapay Zeka Entegrasyonunu Anlamak

### GitHub Modelleri API
Uygulama, çeşitli yapay zeka modellerine ücretsiz erişim sağlayan GitHub Modelleri'ni kullanır:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Komut Mühendisliği
Servis, iyi sonuçlar almak için dikkatlice hazırlanmış komutlar kullanır:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Yanıt İşleme
Yapay zeka yanıtı çıkarılır ve doğrulanır:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Sonraki Adımlar

Daha fazla örnek için [Bölüm 04: Pratik örnekler](../README.md) kısmına bakın.

**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlıklar içerebileceğini lütfen unutmayın. Belgenin orijinal dili, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yanlış yorumlamalar için sorumluluk kabul etmiyoruz.