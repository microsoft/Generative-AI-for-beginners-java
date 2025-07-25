<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:57:36+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "ms"
}
-->
# Tutorial Penjana Cerita Haiwan Peliharaan untuk Pemula

## Kandungan

- [Keperluan Asas](../../../../04-PracticalSamples/petstory)
- [Memahami Struktur Projek](../../../../04-PracticalSamples/petstory)
- [Komponen Utama Dijelaskan](../../../../04-PracticalSamples/petstory)
  - [1. Aplikasi Utama](../../../../04-PracticalSamples/petstory)
  - [2. Pengawal Web](../../../../04-PracticalSamples/petstory)
  - [3. Perkhidmatan Cerita](../../../../04-PracticalSamples/petstory)
  - [4. Templat Web](../../../../04-PracticalSamples/petstory)
  - [5. Konfigurasi](../../../../04-PracticalSamples/petstory)
- [Menjalankan Aplikasi](../../../../04-PracticalSamples/petstory)
- [Bagaimana Semua Ini Berfungsi Bersama](../../../../04-PracticalSamples/petstory)
- [Memahami Integrasi AI](../../../../04-PracticalSamples/petstory)
- [Langkah Seterusnya](../../../../04-PracticalSamples/petstory)

## Keperluan Asas

Sebelum memulakan, pastikan anda mempunyai:
- Java 21 atau lebih tinggi dipasang
- Maven untuk pengurusan kebergantungan
- Akaun GitHub dengan token akses peribadi (PAT) dengan skop `models:read`
- Pemahaman asas tentang Java, Spring Boot, dan pembangunan web

## Memahami Struktur Projek

Projek cerita haiwan peliharaan ini mempunyai beberapa fail penting:

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

## Komponen Utama Dijelaskan

### 1. Aplikasi Utama

**Fail:** `PetStoryApplication.java`

Ini adalah titik permulaan untuk aplikasi Spring Boot kami:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Apa yang dilakukan:**
- Anotasi `@SpringBootApplication` mengaktifkan konfigurasi automatik dan pengimbasan komponen
- Memulakan pelayan web terbenam (Tomcat) pada port 8080
- Secara automatik mencipta semua bean dan perkhidmatan Spring yang diperlukan

### 2. Pengawal Web

**Fail:** `PetController.java`

Ini mengendalikan semua permintaan web dan interaksi pengguna:

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

**Ciri utama:**

1. **Pengendalian Laluan**: `@GetMapping("/")` memaparkan borang muat naik, `@PostMapping("/generate-story")` memproses penghantaran
2. **Pengesahan Input**: Memeriksa keterangan kosong dan had panjang
3. **Keselamatan**: Membersihkan input pengguna untuk mencegah serangan XSS
4. **Pengendalian Ralat**: Menyediakan cerita sandaran apabila perkhidmatan AI gagal
5. **Pengikatan Model**: Menyampaikan data kepada templat HTML menggunakan `Model` Spring

**Sistem Sandaran:**
Pengawal ini termasuk templat cerita yang telah ditulis terlebih dahulu yang digunakan apabila perkhidmatan AI tidak tersedia:

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

### 3. Perkhidmatan Cerita

**Fail:** `StoryService.java`

Perkhidmatan ini berkomunikasi dengan GitHub Models untuk menjana cerita:

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

**Komponen utama:**

1. **Klien OpenAI**: Menggunakan SDK Java rasmi OpenAI yang dikonfigurasi untuk GitHub Models
2. **Prompt Sistem**: Menetapkan tingkah laku AI untuk menulis cerita haiwan peliharaan yang mesra keluarga
3. **Prompt Pengguna**: Memberitahu AI dengan tepat cerita apa yang perlu ditulis berdasarkan keterangan
4. **Parameter**: Mengawal panjang cerita dan tahap kreativiti
5. **Pengendalian Ralat**: Melemparkan pengecualian yang ditangkap dan dikendalikan oleh pengawal

### 4. Templat Web

**Fail:** `index.html` (Borang Muat Naik)

Halaman utama di mana pengguna menerangkan haiwan peliharaan mereka:

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

**Fail:** `result.html` (Paparan Cerita)

Memaparkan cerita yang dijana:

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

**Ciri Templat:**

1. **Integrasi Thymeleaf**: Menggunakan atribut `th:` untuk kandungan dinamik
2. **Reka Bentuk Responsif**: Gaya CSS untuk mudah alih dan desktop
3. **Pengendalian Ralat**: Memaparkan ralat pengesahan kepada pengguna
4. **Pemprosesan Pelanggan**: JavaScript untuk analisis imej (menggunakan Transformers.js)

### 5. Konfigurasi

**Fail:** `application.properties`

Tetapan konfigurasi untuk aplikasi:

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

**Konfigurasi dijelaskan:**

1. **Muat Naik Fail**: Membenarkan imej sehingga 10MB
2. **Log**: Mengawal maklumat apa yang direkodkan semasa pelaksanaan
3. **GitHub Models**: Menentukan model AI dan titik akhir yang digunakan
4. **Keselamatan**: Konfigurasi pengendalian ralat untuk mengelakkan pendedahan maklumat sensitif

## Menjalankan Aplikasi

### Langkah 1: Tetapkan Token GitHub Anda

Pertama, anda perlu menetapkan token GitHub anda sebagai pembolehubah persekitaran:

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

**Mengapa ini diperlukan:**
- GitHub Models memerlukan pengesahan untuk mengakses model AI
- Menggunakan pembolehubah persekitaran memastikan token sensitif tidak berada dalam kod sumber
- Skop `models:read` memberikan akses kepada inferens AI

### Langkah 2: Bina dan Jalankan

Navigasi ke direktori projek:
```bash
cd 04-PracticalSamples/petstory
```

Bina aplikasi:
```bash
mvn clean compile
```

Mulakan pelayan:
```bash
mvn spring-boot:run
```

Aplikasi akan bermula pada `http://localhost:8080`.

### Langkah 3: Uji Aplikasi

1. **Buka** `http://localhost:8080` dalam pelayar anda
2. **Terangkan** haiwan peliharaan anda dalam kawasan teks (contoh: "Seekor golden retriever yang suka bermain dan mengambil barang")
3. **Klik** "Generate Story" untuk mendapatkan cerita yang dijana AI
4. **Sebagai alternatif**, muat naik imej haiwan peliharaan untuk menjana keterangan secara automatik
5. **Lihat** cerita kreatif berdasarkan keterangan haiwan peliharaan anda

## Bagaimana Semua Ini Berfungsi Bersama

Berikut adalah aliran lengkap apabila anda menjana cerita haiwan peliharaan:

1. **Input Pengguna**: Anda menerangkan haiwan peliharaan anda pada borang web
2. **Penghantaran Borang**: Pelayar menghantar permintaan POST ke `/generate-story`
3. **Pemprosesan Pengawal**: `PetController` mengesahkan dan membersihkan input
4. **Panggilan Perkhidmatan AI**: `StoryService` menghantar permintaan ke API GitHub Models
5. **Penjanaan Cerita**: AI menjana cerita kreatif berdasarkan keterangan
6. **Pengendalian Respons**: Pengawal menerima cerita dan menambahnya ke model
7. **Rendering Templat**: Thymeleaf merender `result.html` dengan cerita
8. **Paparan**: Pengguna melihat cerita yang dijana dalam pelayar mereka

**Aliran Pengendalian Ralat:**
Jika perkhidmatan AI gagal:
1. Pengawal menangkap pengecualian
2. Menjana cerita sandaran menggunakan templat yang telah ditulis terlebih dahulu
3. Memaparkan cerita sandaran dengan nota tentang ketidaktersediaan AI
4. Pengguna tetap mendapat cerita, memastikan pengalaman pengguna yang baik

## Memahami Integrasi AI

### API GitHub Models
Aplikasi ini menggunakan GitHub Models, yang menyediakan akses percuma kepada pelbagai model AI:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Kejuruteraan Prompt
Perkhidmatan ini menggunakan prompt yang direka dengan teliti untuk mendapatkan hasil yang baik:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Pemprosesan Respons
Respons AI diekstrak dan disahkan:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Langkah Seterusnya

Untuk lebih banyak contoh, lihat [Bab 04: Contoh Praktikal](../README.md)

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang berwibawa. Untuk maklumat yang kritikal, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.