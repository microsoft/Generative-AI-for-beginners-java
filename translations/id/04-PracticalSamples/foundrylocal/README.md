<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:54:48+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "id"
}
-->
# Tutorial Foundry Local Spring Boot

## Daftar Isi

- [Prasyarat](../../../../04-PracticalSamples/foundrylocal)
- [Ikhtisar Proyek](../../../../04-PracticalSamples/foundrylocal)
- [Memahami Kode](../../../../04-PracticalSamples/foundrylocal)
  - [1. Konfigurasi Aplikasi (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Kelas Aplikasi Utama (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Lapisan Layanan AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Dependensi Proyek (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Bagaimana Semua Ini Bekerja Bersama](../../../../04-PracticalSamples/foundrylocal)
- [Menyiapkan Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Menjalankan Aplikasi](../../../../04-PracticalSamples/foundrylocal)
- [Output yang Diharapkan](../../../../04-PracticalSamples/foundrylocal)
- [Langkah Selanjutnya](../../../../04-PracticalSamples/foundrylocal)
- [Pemecahan Masalah](../../../../04-PracticalSamples/foundrylocal)

## Prasyarat

Sebelum memulai tutorial ini, pastikan Anda memiliki:

- **Java 21 atau lebih tinggi** terinstal di sistem Anda
- **Maven 3.6+** untuk membangun proyek
- **Foundry Local** terinstal dan berjalan

### **Instal Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Ikhtisar Proyek

Proyek ini terdiri dari empat komponen utama:

1. **Application.java** - Titik masuk utama aplikasi Spring Boot
2. **FoundryLocalService.java** - Lapisan layanan yang menangani komunikasi AI
3. **application.properties** - Konfigurasi untuk koneksi Foundry Local
4. **pom.xml** - Dependensi Maven dan konfigurasi proyek

## Memahami Kode

### 1. Konfigurasi Aplikasi (application.properties)

**File:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Apa yang dilakukan:**
- **base-url**: Menentukan lokasi Foundry Local berjalan (port default 5273)
- **model**: Menyebutkan model AI yang akan digunakan untuk menghasilkan teks

**Konsep utama:** Spring Boot secara otomatis memuat properti ini dan membuatnya tersedia untuk aplikasi Anda menggunakan anotasi `@Value`.

### 2. Kelas Aplikasi Utama (Application.java)

**File:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Apa yang dilakukan:**
- `@SpringBootApplication` mengaktifkan konfigurasi otomatis Spring Boot
- `WebApplicationType.NONE` memberi tahu Spring bahwa ini adalah aplikasi command-line, bukan server web
- Metode utama memulai aplikasi Spring

**Demo Runner:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**Apa yang dilakukan:**
- `@Bean` membuat komponen yang dikelola oleh Spring
- `CommandLineRunner` menjalankan kode setelah Spring Boot dimulai
- `foundryLocalService` secara otomatis disuntikkan oleh Spring (dependency injection)
- Mengirim pesan uji ke AI dan menampilkan responsnya

### 3. Lapisan Layanan AI (FoundryLocalService.java)

**File:** `src/main/java/com/example/FoundryLocalService.java`

#### Penyuntikan Konfigurasi:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Apa yang dilakukan:**
- `@Service` memberi tahu Spring bahwa kelas ini menyediakan logika bisnis
- `@Value` menyuntikkan nilai konfigurasi dari application.properties
- Sintaks `:default-value` menyediakan nilai cadangan jika properti tidak diatur

#### Inisialisasi Klien:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Apa yang dilakukan:**
- `@PostConstruct` menjalankan metode ini setelah Spring membuat layanan
- Membuat klien OpenAI yang mengarah ke instance Foundry Local Anda
- Jalur `/v1` diperlukan untuk kompatibilitas API OpenAI
- Kunci API adalah "unused" karena pengembangan lokal tidak memerlukan autentikasi

#### Metode Chat:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**Apa yang dilakukan:**
- **ChatCompletionCreateParams**: Mengonfigurasi permintaan AI
  - `model`: Menentukan model AI yang akan digunakan
  - `addUserMessage`: Menambahkan pesan Anda ke percakapan
  - `maxCompletionTokens`: Membatasi panjang respons (menghemat sumber daya)
  - `temperature`: Mengontrol tingkat keacakan (0.0 = deterministik, 1.0 = kreatif)
- **Panggilan API**: Mengirim permintaan ke Foundry Local
- **Penanganan Respons**: Mengekstrak respons teks AI dengan aman
- **Penanganan Kesalahan**: Membungkus pengecualian dengan pesan kesalahan yang membantu

### 4. Dependensi Proyek (pom.xml)

**Dependensi Utama:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Apa yang dilakukan:**
- **spring-boot-starter**: Menyediakan fungsionalitas inti Spring Boot
- **openai-java**: SDK Java resmi OpenAI untuk komunikasi API
- **jackson-databind**: Menangani serialisasi/deserialisasi JSON untuk panggilan API

## Bagaimana Semua Ini Bekerja Bersama

Berikut alur lengkap saat Anda menjalankan aplikasi:

1. **Startup**: Spring Boot dimulai dan membaca `application.properties`
2. **Pembuatan Layanan**: Spring membuat `FoundryLocalService` dan menyuntikkan nilai konfigurasi
3. **Pengaturan Klien**: `@PostConstruct` menginisialisasi klien OpenAI untuk terhubung ke Foundry Local
4. **Eksekusi Demo**: `CommandLineRunner` dijalankan setelah startup
5. **Panggilan AI**: Demo memanggil `foundryLocalService.chat()` dengan pesan uji
6. **Permintaan API**: Layanan membangun dan mengirim permintaan yang kompatibel dengan OpenAI ke Foundry Local
7. **Pemrosesan Respons**: Layanan mengekstrak dan mengembalikan respons AI
8. **Tampilan**: Aplikasi mencetak respons dan keluar

## Menyiapkan Foundry Local

Untuk menyiapkan Foundry Local, ikuti langkah-langkah berikut:

1. **Instal Foundry Local** menggunakan instruksi di bagian [Prasyarat](../../../../04-PracticalSamples/foundrylocal).
2. **Unduh model AI** yang ingin Anda gunakan, misalnya, `phi-3.5-mini`, dengan perintah berikut:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfigurasikan file application.properties** agar sesuai dengan pengaturan Foundry Local Anda, terutama jika Anda menggunakan port atau model yang berbeda.

## Menjalankan Aplikasi

### Langkah 1: Jalankan Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Langkah 2: Bangun dan Jalankan Aplikasi
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Output yang Diharapkan

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Langkah Selanjutnya

Untuk contoh lainnya, lihat [Bab 04: Contoh Praktis](../README.md)

## Pemecahan Masalah

### Masalah Umum

**"Connection refused" atau "Service unavailable"**
- Pastikan Foundry Local berjalan: `foundry model list`
- Verifikasi layanan berada di port 5273: Periksa `application.properties`
- Coba mulai ulang Foundry Local: `foundry model run phi-3.5-mini`

**Kesalahan "Model not found"**
- Periksa model yang tersedia: `foundry model list`
- Perbarui nama model di `application.properties` agar sesuai dengan tepat
- Unduh model jika diperlukan: `foundry model run phi-3.5-mini`

**Kesalahan kompilasi Maven**
- Pastikan Java 21 atau lebih tinggi: `java -version`
- Bersihkan dan bangun ulang: `mvn clean compile`
- Periksa koneksi internet untuk mengunduh dependensi

**Aplikasi berjalan tetapi tidak ada output**
- Verifikasi Foundry Local merespons: Buka browser ke `http://localhost:5273`
- Periksa log aplikasi untuk pesan kesalahan spesifik
- Pastikan model sepenuhnya dimuat dan siap

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan hasil yang akurat, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi yang bersifat kritis, disarankan menggunakan jasa penerjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.