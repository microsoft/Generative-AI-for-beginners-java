<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:56:54+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "ms"
}
-->
# Tutorial Spring Boot Foundry Local

## Kandungan

- [Keperluan Awal](../../../../04-PracticalSamples/foundrylocal)
- [Gambaran Projek](../../../../04-PracticalSamples/foundrylocal)
- [Memahami Kod](../../../../04-PracticalSamples/foundrylocal)
  - [1. Konfigurasi Aplikasi (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Kelas Aplikasi Utama (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Lapisan Perkhidmatan AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Kebergantungan Projek (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Bagaimana Semua Ini Berfungsi Bersama](../../../../04-PracticalSamples/foundrylocal)
- [Menyiapkan Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Menjalankan Aplikasi](../../../../04-PracticalSamples/foundrylocal)
- [Output Dijangka](../../../../04-PracticalSamples/foundrylocal)
- [Langkah Seterusnya](../../../../04-PracticalSamples/foundrylocal)
- [Penyelesaian Masalah](../../../../04-PracticalSamples/foundrylocal)

## Keperluan Awal

Sebelum memulakan tutorial ini, pastikan anda mempunyai:

- **Java 21 atau lebih tinggi** dipasang pada sistem anda
- **Maven 3.6+** untuk membina projek
- **Foundry Local** dipasang dan berjalan

### **Pasang Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Gambaran Projek

Projek ini terdiri daripada empat komponen utama:

1. **Application.java** - Titik masuk utama aplikasi Spring Boot
2. **FoundryLocalService.java** - Lapisan perkhidmatan yang mengendalikan komunikasi AI
3. **application.properties** - Konfigurasi untuk sambungan Foundry Local
4. **pom.xml** - Kebergantungan Maven dan konfigurasi projek

## Memahami Kod

### 1. Konfigurasi Aplikasi (application.properties)

**Fail:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Apa yang dilakukan:**
- **base-url**: Menentukan lokasi Foundry Local berjalan (port lalai 5273)
- **model**: Menamakan model AI yang akan digunakan untuk penjanaan teks

**Konsep utama:** Spring Boot secara automatik memuatkan sifat-sifat ini dan menjadikannya tersedia untuk aplikasi anda menggunakan anotasi `@Value`.

### 2. Kelas Aplikasi Utama (Application.java)

**Fail:** `src/main/java/com/example/Application.java`

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
- `@SpringBootApplication` mengaktifkan konfigurasi automatik Spring Boot
- `WebApplicationType.NONE` memberitahu Spring bahawa ini adalah aplikasi baris perintah, bukan pelayan web
- Kaedah utama memulakan aplikasi Spring

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
- `@Bean` mencipta komponen yang diuruskan oleh Spring
- `CommandLineRunner` menjalankan kod selepas Spring Boot dimulakan
- `foundryLocalService` disuntik secara automatik oleh Spring (suntikan kebergantungan)
- Menghantar mesej ujian kepada AI dan memaparkan respons

### 3. Lapisan Perkhidmatan AI (FoundryLocalService.java)

**Fail:** `src/main/java/com/example/FoundryLocalService.java`

#### Suntikan Konfigurasi:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Apa yang dilakukan:**
- `@Service` memberitahu Spring bahawa kelas ini menyediakan logik perniagaan
- `@Value` menyuntik nilai konfigurasi daripada application.properties
- Sintaks `:default-value` menyediakan nilai sandaran jika sifat tidak ditetapkan

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
- `@PostConstruct` menjalankan kaedah ini selepas Spring mencipta perkhidmatan
- Mencipta klien OpenAI yang menunjuk kepada instance Foundry Local anda
- Laluan `/v1` diperlukan untuk keserasian API OpenAI
- Kunci API adalah "tidak digunakan" kerana pembangunan tempatan tidak memerlukan pengesahan

#### Kaedah Chat:
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
- **ChatCompletionCreateParams**: Mengkonfigurasi permintaan AI
  - `model`: Menentukan model AI yang akan digunakan
  - `addUserMessage`: Menambah mesej anda ke perbualan
  - `maxCompletionTokens`: Mengehadkan panjang respons (menjimatkan sumber)
  - `temperature`: Mengawal kebarangkalian (0.0 = deterministik, 1.0 = kreatif)
- **Panggilan API**: Menghantar permintaan kepada Foundry Local
- **Pengendalian Respons**: Mengekstrak respons teks AI dengan selamat
- **Pengendalian Ralat**: Membungkus pengecualian dengan mesej ralat yang berguna

### 4. Kebergantungan Projek (pom.xml)

**Kebergantungan Utama:**

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
- **spring-boot-starter**: Menyediakan fungsi teras Spring Boot
- **openai-java**: SDK Java rasmi OpenAI untuk komunikasi API
- **jackson-databind**: Mengendalikan serialisasi/deserialisasi JSON untuk panggilan API

## Bagaimana Semua Ini Berfungsi Bersama

Berikut adalah aliran lengkap apabila anda menjalankan aplikasi:

1. **Permulaan**: Spring Boot dimulakan dan membaca `application.properties`
2. **Penciptaan Perkhidmatan**: Spring mencipta `FoundryLocalService` dan menyuntik nilai konfigurasi
3. **Persediaan Klien**: `@PostConstruct` menginisialisasi klien OpenAI untuk menyambung ke Foundry Local
4. **Pelaksanaan Demo**: `CommandLineRunner` dilaksanakan selepas permulaan
5. **Panggilan AI**: Demo memanggil `foundryLocalService.chat()` dengan mesej ujian
6. **Permintaan API**: Perkhidmatan membina dan menghantar permintaan yang serasi dengan OpenAI kepada Foundry Local
7. **Pemprosesan Respons**: Perkhidmatan mengekstrak dan mengembalikan respons AI
8. **Paparan**: Aplikasi mencetak respons dan keluar

## Menyiapkan Foundry Local

Untuk menyiapkan Foundry Local, ikuti langkah-langkah berikut:

1. **Pasang Foundry Local** menggunakan arahan dalam bahagian [Keperluan Awal](../../../../04-PracticalSamples/foundrylocal).
2. **Muat turun model AI** yang ingin anda gunakan, contohnya, `phi-3.5-mini`, dengan arahan berikut:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfigurasi fail application.properties** untuk sepadan dengan tetapan Foundry Local anda, terutamanya jika anda menggunakan port atau model yang berbeza.

## Menjalankan Aplikasi

### Langkah 1: Mulakan Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Langkah 2: Bina dan Jalankan Aplikasi
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Output Dijangka

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

## Langkah Seterusnya

Untuk lebih banyak contoh, lihat [Bab 04: Contoh praktikal](../README.md)

## Penyelesaian Masalah

### Masalah Biasa

**"Connection refused" atau "Service unavailable"**
- Pastikan Foundry Local sedang berjalan: `foundry model list`
- Sahkan perkhidmatan berada pada port 5273: Periksa `application.properties`
- Cuba mulakan semula Foundry Local: `foundry model run phi-3.5-mini`

**Ralat "Model not found"**
- Periksa model yang tersedia: `foundry model list`
- Kemas kini nama model dalam `application.properties` agar sepadan dengan tepat
- Muat turun model jika diperlukan: `foundry model run phi-3.5-mini`

**Ralat kompilasi Maven**
- Pastikan Java 21 atau lebih tinggi: `java -version`
- Bersihkan dan bina semula: `mvn clean compile`
- Periksa sambungan internet untuk muat turun kebergantungan

**Aplikasi bermula tetapi tiada output**
- Sahkan Foundry Local memberi respons: Buka pelayar ke `http://localhost:5273`
- Periksa log aplikasi untuk mesej ralat tertentu
- Pastikan model dimuat sepenuhnya dan sedia

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang berwibawa. Untuk maklumat yang kritikal, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.