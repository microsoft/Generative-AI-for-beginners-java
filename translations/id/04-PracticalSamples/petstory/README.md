<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:55:35+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "id"
}
-->
# Tutorial Generator Cerita Hewan Peliharaan untuk Pemula

## Daftar Isi

- [Prasyarat](../../../../04-PracticalSamples/petstory)
- [Memahami Struktur Proyek](../../../../04-PracticalSamples/petstory)
- [Penjelasan Komponen Inti](../../../../04-PracticalSamples/petstory)
  - [1. Aplikasi Utama](../../../../04-PracticalSamples/petstory)
  - [2. Pengontrol Web](../../../../04-PracticalSamples/petstory)
  - [3. Layanan Cerita](../../../../04-PracticalSamples/petstory)
  - [4. Template Web](../../../../04-PracticalSamples/petstory)
  - [5. Konfigurasi](../../../../04-PracticalSamples/petstory)
- [Menjalankan Aplikasi](../../../../04-PracticalSamples/petstory)
- [Bagaimana Semua Ini Bekerja Bersama](../../../../04-PracticalSamples/petstory)
- [Memahami Integrasi AI](../../../../04-PracticalSamples/petstory)
- [Langkah Selanjutnya](../../../../04-PracticalSamples/petstory)

## Prasyarat

Sebelum memulai, pastikan Anda memiliki:
- Java 21 atau versi lebih tinggi terinstal
- Maven untuk manajemen dependensi
- Akun GitHub dengan token akses pribadi (PAT) dengan cakupan `models:read`
- Pemahaman dasar tentang Java, Spring Boot, dan pengembangan web

## Memahami Struktur Proyek

Proyek cerita hewan peliharaan memiliki beberapa file penting:

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

## Penjelasan Komponen Inti

### 1. Aplikasi Utama

**File:** `PetStoryApplication.java`

Ini adalah titik masuk untuk aplikasi Spring Boot kita:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Apa yang dilakukan:**
- Anotasi `@SpringBootApplication` mengaktifkan konfigurasi otomatis dan pemindaian komponen
- Memulai server web bawaan (Tomcat) pada port 8080
- Secara otomatis membuat semua bean dan layanan Spring yang diperlukan

### 2. Pengontrol Web

**File:** `PetController.java`

Ini menangani semua permintaan web dan interaksi pengguna:

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

**Fitur utama:**

1. **Penanganan Rute**: `@GetMapping("/")` menampilkan formulir unggah, `@PostMapping("/generate-story")` memproses pengiriman
2. **Validasi Input**: Memeriksa deskripsi kosong dan batas panjang
3. **Keamanan**: Membersihkan input pengguna untuk mencegah serangan XSS
4. **Penanganan Kesalahan**: Menyediakan cerita cadangan saat layanan AI gagal
5. **Pengikatan Model**: Mengirimkan data ke template HTML menggunakan `Model` Spring

**Sistem Cadangan:**
Pengontrol mencakup template cerita yang telah ditulis sebelumnya yang digunakan saat layanan AI tidak tersedia:

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

### 3. Layanan Cerita

**File:** `StoryService.java`

Layanan ini berkomunikasi dengan GitHub Models untuk menghasilkan cerita:

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

1. **Klien OpenAI**: Menggunakan SDK Java resmi OpenAI yang dikonfigurasi untuk GitHub Models
2. **Prompt Sistem**: Mengatur perilaku AI untuk menulis cerita hewan peliharaan yang ramah keluarga
3. **Prompt Pengguna**: Memberitahu AI cerita apa yang harus ditulis berdasarkan deskripsi
4. **Parameter**: Mengontrol panjang cerita dan tingkat kreativitas
5. **Penanganan Kesalahan**: Melemparkan pengecualian yang ditangkap dan ditangani oleh pengontrol

### 4. Template Web

**File:** `index.html` (Formulir Unggah)

Halaman utama tempat pengguna mendeskripsikan hewan peliharaan mereka:

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

**File:** `result.html` (Tampilan Cerita)

Menampilkan cerita yang dihasilkan:

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

**Fitur Template:**

1. **Integrasi Thymeleaf**: Menggunakan atribut `th:` untuk konten dinamis
2. **Desain Responsif**: Styling CSS untuk perangkat seluler dan desktop
3. **Penanganan Kesalahan**: Menampilkan kesalahan validasi kepada pengguna
4. **Pemrosesan Sisi Klien**: JavaScript untuk analisis gambar (menggunakan Transformers.js)

### 5. Konfigurasi

**File:** `application.properties`

Pengaturan konfigurasi untuk aplikasi:

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

**Penjelasan Konfigurasi:**

1. **Unggah File**: Mengizinkan gambar hingga 10MB
2. **Logging**: Mengontrol informasi apa yang dicatat selama eksekusi
3. **GitHub Models**: Menentukan model AI dan endpoint yang digunakan
4. **Keamanan**: Konfigurasi penanganan kesalahan untuk menghindari pengungkapan informasi sensitif

## Menjalankan Aplikasi

### Langkah 1: Atur Token GitHub Anda

Pertama, Anda perlu mengatur token GitHub Anda sebagai variabel lingkungan:

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
- GitHub Models memerlukan autentikasi untuk mengakses model AI
- Menggunakan variabel lingkungan menjaga token sensitif tetap aman dari kode sumber
- Cakupan `models:read` memberikan akses ke inferensi AI

### Langkah 2: Bangun dan Jalankan

Navigasikan ke direktori proyek:
```bash
cd 04-PracticalSamples/petstory
```

Bangun aplikasi:
```bash
mvn clean compile
```

Mulai server:
```bash
mvn spring-boot:run
```

Aplikasi akan berjalan di `http://localhost:8080`.

### Langkah 3: Uji Aplikasi

1. **Buka** `http://localhost:8080` di browser Anda
2. **Deskripsikan** hewan peliharaan Anda di area teks (misalnya, "Golden retriever yang suka bermain dan mengambil bola")
3. **Klik** "Generate Story" untuk mendapatkan cerita yang dihasilkan AI
4. **Sebagai alternatif**, unggah gambar hewan peliharaan untuk secara otomatis menghasilkan deskripsi
5. **Lihat** cerita kreatif berdasarkan deskripsi hewan peliharaan Anda

## Bagaimana Semua Ini Bekerja Bersama

Berikut alur lengkap saat Anda menghasilkan cerita hewan peliharaan:

1. **Input Pengguna**: Anda mendeskripsikan hewan peliharaan Anda di formulir web
2. **Pengiriman Formulir**: Browser mengirimkan permintaan POST ke `/generate-story`
3. **Pemrosesan Pengontrol**: `PetController` memvalidasi dan membersihkan input
4. **Panggilan Layanan AI**: `StoryService` mengirimkan permintaan ke API GitHub Models
5. **Pembuatan Cerita**: AI menghasilkan cerita kreatif berdasarkan deskripsi
6. **Penanganan Respons**: Pengontrol menerima cerita dan menambahkannya ke model
7. **Rendering Template**: Thymeleaf merender `result.html` dengan cerita
8. **Tampilan**: Pengguna melihat cerita yang dihasilkan di browser mereka

**Alur Penanganan Kesalahan:**
Jika layanan AI gagal:
1. Pengontrol menangkap pengecualian
2. Menghasilkan cerita cadangan menggunakan template yang telah ditulis sebelumnya
3. Menampilkan cerita cadangan dengan catatan tentang ketidaktersediaan AI
4. Pengguna tetap mendapatkan cerita, memastikan pengalaman pengguna yang baik

## Memahami Integrasi AI

### API GitHub Models
Aplikasi ini menggunakan GitHub Models, yang menyediakan akses gratis ke berbagai model AI:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Teknik Prompt
Layanan ini menggunakan prompt yang dirancang dengan hati-hati untuk mendapatkan hasil yang baik:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Pemrosesan Respons
Respons AI diekstraksi dan divalidasi:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Langkah Selanjutnya

Untuk contoh lebih lanjut, lihat [Bab 04: Contoh Praktis](../README.md)

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan hasil yang akurat, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi yang bersifat kritis, disarankan menggunakan jasa penerjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.