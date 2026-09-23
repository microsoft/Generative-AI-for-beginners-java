# Tutorial Penjana Cerita Haiwan Peliharaan untuk Pemula

Muat naik foto haiwan peliharaan, analisa menggunakan GPT-5.6 Luna, dan jana cerita berdasarkan penerangan yang diperoleh. Kedua-dua permintaan model menggunakan `reasoning_effort: none`.

| Komponen | Versi |
| --- | --- |
| Java | 21 atau lebih tinggi |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Jadual Kandungan

- [Prasyarat](#prasyarat)
- [Memahami Struktur Projek](#memahami-struktur-projek)
- [Penerangan Komponen Teras](#penerangan-komponen-teras)
  - [1. Aplikasi Utama](#1-aplikasi-utama)
  - [2. Pengawal Web](#2-pengawal-web)
  - [3. Perkhidmatan Cerita](#3-perkhidmatan-cerita)
  - [4. Templat Web](#4-templat-web)
  - [5. Konfigurasi](#5-konfigurasi)
- [Menjalankan Aplikasi](#menjalankan-aplikasi)
- [Ujian Offline](#ujian-offline)
- [Bagaimana Ia Berfungsi Bersama](#bagaimana-ia-berfungsi-bersama)
- [Memahami Integrasi AI](#memahami-integrasi-ai)
- [Langkah Seterusnya](#langkah-seterusnya)

## Prasyarat

Sebelum memulakan, pastikan anda mempunyai:
- Java 21 atau lebih tinggi dipasang
- Maven untuk pengurusan pergantungan
- Penempatan Azure AI Foundry GPT-5.6 Luna bernama `gpt-5.6-luna`, atau override `AZURE_OPENAI_DEPLOYMENT` yang menunjuk ke penempatan tersebut. Lihat [Bab 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) untuk penyediaan dan daftar masuk dengan `az login` untuk pengesahan tanpa kunci. Penempatan mestilah menyokong input imej dan `reasoning_effort: none`.
- Pemahaman asas tentang Java, Spring Boot, dan pembangunan web

## Memahami Struktur Projek

Projek cerita haiwan peliharaan mempunyai beberapa fail penting:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Penerangan Komponen Teras

### 1. Aplikasi Utama

**Fail:** `PetStoryApplication.java`

Ini adalah titik masuk untuk aplikasi Spring Boot kami:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Apa yang dilakukan:**
- Anotasi `@SpringBootApplication` mengaktifkan pengkonfigurasian automatik dan pengimbasan komponen
- Memulakan pelayan web terbenam (Tomcat) pada port 8080
- Membuat semua bean dan perkhidmatan Spring yang diperlukan secara automatik

### 2. Pengawal Web

**Fail:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Titik Akhir | Permintaan | Respons Berjaya |
| --- | --- | --- |
| `GET /` | Tiada badan | Borang muat naik HTML dengan token CSRF |
| `POST /analyze-image` | `multipart/form-data`, medan fail `image` | JSON: `{"description":"Haiwan peliharaan yang ceria..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, medan `description` | Halaman hasil HTML dengan penerangan dan cerita yang dijana |

Kedua-dua titik akhir POST memerlukan kuki sesi dan token CSRF yang diperoleh dari `GET /`. Skrip muat naik menghantar nilai tersembunyi `_csrf` dalam pengepala `X-CSRF-TOKEN`; penghantaran cerita menghantarnya sebagai medan borang `_csrf`. Klien API mesti mengekalkan kuki antara permintaan. Ini adalah titik akhir borang, bukan permintaan JSON.

Penerangan mestilah tidak kosong dan tidak lebih panjang daripada 1000 aksara. Pengawal memotong penerangan dan membuang `<`, `>`, petikan berganda, apostrof, dan `&` sebelum menghantarnya ke perkhidmatan. Templat hasil juga mengelakkan output model dengan `th:text`.

Kegagalan pengesahan imej memulangkan HTTP 400 dengan medan `error`; kegagalan model memulangkan HTTP 502 dengan medan `error` tanpa `description`. Penerangan cerita tidak sah atau kegagalan model mengalihkan ke `/` dengan ralat yang kelihatan. Medan diperlukan yang hilang memulangkan HTTP 400, dan token CSRF yang hilang atau tidak sah memulangkan HTTP 403. Tiada penerangan gantian atau cerita dipersembahkan sebagai hasil AI berjaya.

### 3. Perkhidmatan Cerita

**Fail:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

SDK OpenAI Java rasmi 4.63.1 memanggil API Chat Completions yang serasi dengan OpenAI di Azure AI Foundry. Azure Identity 1.18.6 menyediakan token pembawa Microsoft Entra melalui `DefaultAzureCredential`; tiada kunci API diperlukan.

| Operasi | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Byte imej yang disandi sebagai URL data base64 dengan jenis MIME yang dimuat naik | 300 |
| `generateStory` | Penerangan haiwan peliharaan dalam pesanan pengguna | 800 |

Kedua-dua permintaan menggunakan penempatan yang dikonfigurasikan, secara lalai `gpt-5.6-luna`, dan secara eksplisit menetapkan `ReasoningEffort.NONE` (`reasoning_effort: none`). Tiada permintaan yang menghantar `temperature` atau parameter `max_tokens` warisan.

Analisis imej menerima JPEG, PNG, GIF, dan WebP, menolak imej kosong dan fail lebih 10MB, dan mengehadkan penerangan hasil kepada 1000 aksara. Prompt cerita meminta cerita pendek mesra keluarga. Pilihan kosong atau kandungan model kosong adalah ralat, dan kegagalan mengekalkan sebab asal untuk diagnostik sisi pelayan. Klien SDK ditutup apabila aplikasi dimatikan.

### 4. Templat Web

**Fail:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Borang Muat Naik)

Halaman bermula dengan pemilih foto, bukan kawasan teks penerangan. **Analyze Image** menyemak pratonton foto yang dipilih dan menghantarnya ke `/analyze-image`. Respons berjaya memaparkan penerangan, mengisi medan `description` tersembunyi, dan mendedahkan **Generate Story**. Butang itu menghantar borang sedia ada ke `/generate-story`.

Tiada muat turun model penyemak imbas atau kebergantungan CDN. Analisis imej dijalankan di pelayan melalui penempatan Azure yang dikonfigurasikan. Kegagalan kekal kelihatan dan tidak membenarkan penjanaan cerita dengan penerangan yang direka. Memilih fail berlainan mengosongkan analisis sebelum ini.

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

**Ciri-ciri Templat:**

1. **Integrasi Thymeleaf**: Menggunakan atribut `th:` untuk kandungan dinamik
2. **Reka Bentuk Responsif**: Gaya CSS untuk mudah alih dan desktop
3. **Pengendalian Ralat**: Memaparkan ralat pengesahan kepada pengguna
4. **Pengendalian Muat Naik**: JavaScript menyemak pratonton foto, menghantar permintaan multipart dilindungi CSRF, dan memaparkan penerangan yang diterima

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

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Penerangan konfigurasi:**

1. **Muat Naik Fail**: Fail dan keseluruhan permintaan multipart dihadkan kepada 10MB; pastikan foto di bawah had itu untuk memberi ruang bagi pengepala multipart
2. **Log**: Mengawal maklumat apa yang direkod semasa pelaksanaan
3. **Azure AI Foundry**: Menentukan titik akhir dan penempatan model yang digunakan (pengesahan tanpa kunci)
4. **Keselamatan**: Perlindungan CSRF sentiasa diaktifkan; diagnostik model direkod di pelayan, manakala pengawal memaparkan mesej kegagalan model yang umum

## Menjalankan Aplikasi

### Langkah 1: Log Masuk dan Tetapkan Titik Akhir Anda

Pengesahan tanpa kunci (Microsoft Entra ID), jadi tiada kunci API. Log masuk dan tetapkan titik akhir Foundry anda:

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Mengapa ini diperlukan:**
- Azure AI Foundry menggunakan Microsoft Entra ID untuk mengesahkan permintaan inferens
- Pengesahan tanpa kunci bermakna tiada rahsia dalam kod sumber atau persekitaran anda
- Akaun anda memerlukan peranan **Cognitive Services OpenAI User** pada sumber

Nama penempatan lalai ialah `gpt-5.6-luna`. Jika penempatan GPT-5.6 Luna anda mempunyai nama lain, tetapkan `AZURE_OPENAI_DEPLOYMENT` dalam terminal yang sama sebelum memulakan aplikasi. Kedua-dua analisis imej dan penjanaan cerita menggunakan tetapan ini.

### Langkah 2: Bina dan Jalankan

Navigasi ke direktori projek:
```bash
cd 04-PracticalSamples/petstory
```

Bina fail JAR boleh laku berdiri sendiri dan jalankan semua ujian offline:
```bash
mvn clean package
```

Mulakan pelayan:
```bash
mvn spring-boot:run
```

Aplikasi akan bermula di `http://localhost:8080`.

Sebagai alternatif, mulakan JAR yang dipakej pada port bebas, contohnya:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Untuk arahan itu, buka `http://localhost:8083/`. Laluan yang sama `/analyze-image` dan `/generate-story` tersedia pada port yang dipilih.

### Langkah 3: Uji Aplikasi

1. **Buka** `http://localhost:8080` dalam penyemak imbas anda
2. **Pilih** foto haiwan peliharaan yang jelas dalam format JPEG, PNG, GIF, atau WebP, di bawah 10MB
3. **Klik** "Analyze Image" dan tunggu penerangan haiwan peliharaan
4. **Klik** "Generate Story" selepas analisis berjaya
5. **Lihat** cerita dan gunakan pautan halaman hasil untuk kembali ke borang muat naik

Aliran foto-ke-cerita yang berjaya membuat dua panggilan model, satu per butang. Inferens langsung menggunakan kuota penempatan anda dan mungkin mengenakan caj; laksanakan ujian asap secara bersiri apabila berkongsi penempatan yang terhad kadar. Memuatkan halaman utama tidak memanggil model.

## Ujian Offline

Dari direktori contoh, jalankan:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) merakam permintaan SDK OpenAI sebenar dengan kawalan HTTP loopback. Ia memeriksa penempatan kedua-dua permintaan, `reasoning_effort: none`, had token, beban imej, pengesahan input, respons kosong, dan ralat huluan.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) menggunakan MockMvc dengan perkhidmatan model yang dimok untuk menguji halaman Thymeleaf yang dipaparkan, kontrak muat naik, CSRF, pengesahan, pengelakan output, dan kegagalan yang kelihatan. Ujian ini tidak memerlukan kelayakan Azure dan tidak pernah memanggil inferens berbayar Azure. Maven menulis laporan Surefire di bawah `target/surefire-reports`.

## Bagaimana Ia Berfungsi Bersama

Ini adalah aliran lengkap apabila anda menjana cerita haiwan peliharaan:

1. **Pemilihan Foto**: Anda memilih imej haiwan peliharaan dalam borang muat naik
2. **Muat Naik Imej**: "Analyze Image" menghantar POST multipart ke `/analyze-image` dengan pengepala CSRF
3. **Analisis Imej**: `StoryService` menghantar imej ke GPT-5.6 Luna dengan penalaran ditetapkan kepada `none`
4. **Paparan Penerangan**: Penyemak imbas memaparkan penerangan yang diterima dan menyimpannya dalam borang
5. **Penghantaran Cerita**: "Generate Story" menghantar `description` dan `_csrf` ke `/generate-story`
6. **Penjanaan Cerita**: Pengawal mengesahkan penerangan dan memanggil penempatan yang sama dengan penalaran ditetapkan kepada `none`
7. **Penampilan Templat**: Thymeleaf mengelakkan dan memaparkan penerangan serta cerita dalam halaman hasil

**Aliran Pengendalian Ralat:**
Jika model gagal, pelayan merekod sebabnya. Analisis imej memulangkan HTTP 502 dan penyemak imbas memaparkan ralat tanpa menunjukkan "Generate Story". Penjanaan cerita mengalihkan ke borang dengan mesej ralat. Tiada laluan yang menyamar secara senyap dengan hasil pra-tulis.

## Memahami Integrasi AI

### Azure AI Foundry (tanpa kunci)
Perkhidmatan mengkonfigurasikan SDK dengan titik akhir `/openai/v1/` sumber anda. `DefaultAzureCredential` dan `AuthenticationUtil.getBearerTokenSupplier` membekalkan token Microsoft Entra untuk `https://ai.azure.com/.default`. Pembangunan tempatan boleh menggunakan daftar masuk Azure CLI anda; aplikasi yang dihoskan Azure boleh menggunakan identiti terurus dengan kebenaran sumber yang diperlukan.

### Kejuruteraan Prompt
Analisis imej meminta ciri haiwan peliharaan yang boleh diperhatikan dalam perenggan pendek dan memberitahu model untuk menganggap teks dalam imej sebagai data, bukan arahan. Penjanaan cerita menggunakan penerangan yang diterima dalam permintaan penulisan mesra keluarga yang berasingan. Tiada panggilan yang membolehkan penalaran atau menetapkan penggantian suhu.

### Pemprosesan Respons
Pengendali respons bersama menolak pilihan yang hilang dan kandungan kosong atau hanya ruang putih, memotong kandungan sah, dan mengekalkan kegagalan huluan. Penerangan imej dihadkan kepada 1000 aksara untuk sesuai dengan borang cerita seterusnya. Kegagalan model asal disimpan untuk diagnostik tetapi tidak dipaparkan kepada pengguna.

## Langkah Seterusnya

Untuk lebih banyak contoh, lihat [Bab 04: Contoh Praktikal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan oleh manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->