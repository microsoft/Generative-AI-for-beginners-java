# Tutorial Pembuat Cerita Hewan Peliharaan untuk Pemula

Unggah foto hewan peliharaan, analisis dengan GPT-5.6 Luna, dan buat cerita dari deskripsi yang dihasilkan. Kedua permintaan model menggunakan `reasoning_effort: none`.

| Komponen | Versi |
| --- | --- |
| Java | 21 atau lebih tinggi |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Daftar Isi

- [Prasyarat](#prasyarat)
- [Memahami Struktur Proyek](#memahami-struktur-proyek)
- [Penjelasan Komponen Inti](#penjelasan-komponen-inti)
  - [1. Aplikasi Utama](#1-aplikasi-utama)
  - [2. Kontroler Web](#2-kontroler-web)
  - [3. Layanan Cerita](#3-layanan-cerita)
  - [4. Template Web](#4-template-web)
  - [5. Konfigurasi](#5-konfigurasi)
- [Menjalankan Aplikasi](#menjalankan-aplikasi)
- [Tes Offline](#tes-offline)
- [Bagaimana Semua Ini Bekerja Bersama](#bagaimana-semua-ini-bekerja-bersama)
- [Memahami Integrasi AI](#memahami-integrasi-ai)
- [Langkah Selanjutnya](#langkah-selanjutnya)

## Prasyarat

Sebelum memulai, pastikan Anda memiliki:
- Java 21 atau lebih tinggi terpasang
- Maven untuk manajemen dependensi
- Deploy AI Foundry Azure dari GPT-5.6 Luna yang bernama `gpt-5.6-luna`, atau override `AZURE_OPENAI_DEPLOYMENT` yang mengarah ke deployment tersebut. Lihat [Bab 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) untuk provisioning dan masuk dengan `az login` untuk autentikasi tanpa kunci. Deployment harus mendukung input gambar dan `reasoning_effort: none`.
- Pemahaman dasar Java, Spring Boot, dan pengembangan web

## Memahami Struktur Proyek

Proyek cerita hewan peliharaan memiliki beberapa file penting:

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

**Apa yang dilakukan ini:**
- Anotasi `@SpringBootApplication` mengaktifkan auto-konfigurasi dan pemindaian komponen
- Memulai server web tertanam (Tomcat) di port 8080
- Membuat semua bean dan layanan Spring yang diperlukan secara otomatis

### 2. Kontroler Web

**File:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Permintaan | Respon berhasil |
| --- | --- | --- |
| `GET /` | Tanpa isi | Formulir unggah HTML dengan token CSRF |
| `POST /analyze-image` | `multipart/form-data`, bidang file `image` | JSON: `{"description":"Hewan peliharaan yang riang..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, bidang `description` | Halaman hasil HTML dengan deskripsi dan cerita yang dihasilkan |

Kedua endpoint POST memerlukan cookie sesi dan token CSRF yang diperoleh dari `GET /`. Skrip unggah mengirim nilai tersembunyi `_csrf` di header `X-CSRF-TOKEN`; pengiriman cerita mengirimkannya sebagai bidang formulir `_csrf`. Klien API harus mempertahankan cookie antar permintaan. Ini adalah endpoint formulir, bukan endpoint permintaan JSON.

Deskripsi harus tidak kosong dan tidak lebih dari 1000 karakter. Kontroler memangkas deskripsi dan menghapus `<`, `>`, tanda kutip ganda, apostrof, dan `&` sebelum diteruskan ke layanan. Template hasil juga meloloskan output model dengan `th:text`.

Kegagalan validasi gambar mengembalikan HTTP 400 dengan bidang `error`; kegagalan model mengembalikan HTTP 502 dengan bidang `error` dan tanpa `description`. Deskripsi cerita tidak valid atau kegagalan model mengarahkan kembali ke `/` dengan pesan kesalahan yang terlihat. Bidang yang dibutuhkan hilang mengembalikan HTTP 400, dan token CSRF yang hilang atau tidak valid mengembalikan HTTP 403. Tidak ada deskripsi fallback atau cerita yang disajikan sebagai hasil AI yang berhasil.

### 3. Layanan Cerita

**File:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

OpenAI Java SDK resmi 4.63.1 memanggil API Chat Completions kompatibel OpenAI dari Azure AI Foundry. Azure Identity 1.18.6 menyediakan token bearer Microsoft Entra melalui `DefaultAzureCredential`; tidak diperlukan kunci API.

| Operasi | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Byte gambar yang dikodekan sebagai URL data base64 dengan tipe MIME yang diunggah | 300 |
| `generateStory` | Deskripsi hewan peliharaan dalam pesan pengguna | 800 |

Kedua permintaan menggunakan deployment yang dikonfigurasi, default ke `gpt-5.6-luna`, dan secara eksplisit mengatur `ReasoningEffort.NONE` (`reasoning_effort: none`). Tidak ada permintaan yang mengirim `temperature` atau parameter legasi `max_tokens`.

Analisis gambar menerima JPEG, PNG, GIF, dan WebP, menolak gambar kosong dan file di atas 10MB, serta membatasi deskripsi yang dihasilkan hingga 1000 karakter. Prompt cerita meminta cerita pendek ramah keluarga. Pilihan kosong atau konten model kosong adalah kesalahan, dan kegagalan mempertahankan penyebab asli untuk diagnosis sisi server. Klien SDK ditutup saat aplikasi dimatikan.

### 4. Template Web

**File:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Form Unggah)

Halaman dimulai dengan pemilih foto, bukan area teks deskripsi. **Analyze Image** menampilkan pratinjau foto yang dipilih dan mengirimkannya ke `/analyze-image`. Respon berhasil menampilkan deskripsi, mengisi bidang `description` tersembunyi, dan menampilkan **Generate Story**. Tombol itu mengirimkan formulir yang ada ke `/generate-story`.

Tidak ada pengunduhan model di browser atau ketergantungan CDN. Analisis gambar berjalan di server melalui deployment Azure yang dikonfigurasi. Kegagalan tetap terlihat dan tidak memungkinkan pembuatan cerita dengan deskripsi palsu. Pemilihan file berbeda menghapus analisis sebelumnya.

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

**Fitur template:**

1. **Integrasi Thymeleaf**: Menggunakan atribut `th:` untuk konten dinamis
2. **Desain Responsif**: Styling CSS untuk mobile dan desktop
3. **Penanganan Kesalahan**: Menampilkan kesalahan validasi kepada pengguna
4. **Penanganan Unggah**: JavaScript menampilkan pratinjau foto, mengirim permintaan multipart yang terlindungi CSRF, dan menampilkan deskripsi yang dikembalikan

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

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Penjelasan konfigurasi:**

1. **Unggah File**: Baik file maupun permintaan multipart lengkap dibatasi pada 10MB; jaga ukuran foto di bawah batas itu agar masih ada ruang untuk header multipart
2. **Logging**: Mengendalikan informasi yang dicatat selama eksekusi
3. **Azure AI Foundry**: Menentukan endpoint dan deployment model yang digunakan (autentikasi tanpa kunci)
4. **Keamanan**: Perlindungan CSRF tetap diaktifkan; diagnosa model dicatat di server, sementara kontroler menampilkan pesan kegagalan model umum

## Menjalankan Aplikasi

### Langkah 1: Masuk dan Atur Endpoint Anda

Autentikasi tanpa kunci (Microsoft Entra ID), jadi tidak ada kunci API. Masuk dan atur endpoint Foundry Anda:

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
- Azure AI Foundry menggunakan Microsoft Entra ID untuk mengautentikasi permintaan inferensi
- Autentikasi tanpa kunci berarti tidak ada rahasia dalam kode sumber atau lingkungan Anda
- Akun Anda memerlukan peran **Cognitive Services OpenAI User** pada sumber daya

Nama deployment default adalah `gpt-5.6-luna`. Jika deployment GPT-5.6 Luna Anda memiliki nama lain, atur `AZURE_OPENAI_DEPLOYMENT` di terminal yang sama sebelum menjalankan aplikasi. Baik analisis gambar maupun pembuatan cerita menggunakan pengaturan ini.

### Langkah 2: Bangun dan Jalankan

Arahkan ke direktori proyek:
```bash
cd 04-PracticalSamples/petstory
```

Bangun JAR yang dapat dieksekusi mandiri dan jalankan semua tes offline:
```bash
mvn clean package
```

Mulai server:
```bash
mvn spring-boot:run
```

Aplikasi akan berjalan di `http://localhost:8080`.

Sebagai alternatif, jalankan JAR yang dikemas pada port bebas, misalnya:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Untuk perintah itu, buka `http://localhost:8083/`. Rute `/analyze-image` dan `/generate-story` yang sama tersedia pada port yang dipilih.

### Langkah 3: Uji Aplikasi

1. **Buka** `http://localhost:8080` di browser Anda
2. **Pilih** foto hewan peliharaan yang jelas dalam format JPEG, PNG, GIF, atau WebP, di bawah 10MB
3. **Klik** "Analyze Image" dan tunggu deskripsi hewan peliharaan
4. **Klik** "Generate Story" setelah analisis berhasil
5. **Lihat** ceritanya dan gunakan tautan di halaman hasil untuk kembali ke formulir unggah

Alur foto ke cerita yang berhasil melakukan dua panggilan model, satu per tombol. Inferensi langsung mengonsumsi kuota deployment Anda dan dapat menimbulkan biaya; jalankan tes ringan secara serial saat berbagi deployment dengan rate limit. Memuat halaman utama tidak memanggil model.

## Tes Offline

Dari direktori sampel, jalankan:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) menangkap permintaan SDK OpenAI nyata dengan fixture HTTP loopback. Ini memeriksa deployment kedua permintaan, `reasoning_effort: none`, batas token, payload gambar, validasi input, respon kosong, dan kesalahan hulu.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) menggunakan MockMvc dengan layanan model tiruan untuk menguji halaman Thymeleaf yang dirender, kontrak unggah, CSRF, validasi, pelolosan output, dan kegagalan yang terlihat. Tes ini tidak memerlukan kredensial Azure dan tidak pernah memanggil inferensi bayar Azure. Maven menulis laporan Surefire di bawah `target/surefire-reports`.

## Bagaimana Semua Ini Bekerja Bersama

Berikut adalah alur lengkap saat Anda membuat cerita hewan peliharaan:

1. **Pemilihan Foto**: Anda memilih gambar hewan peliharaan di formulir unggah
2. **Unggah Gambar**: "Analyze Image" mengirim POST multipart ke `/analyze-image` dengan header CSRF
3. **Analisis Gambar**: `StoryService` mengirim gambar ke GPT-5.6 Luna dengan reasoning diatur ke `none`
4. **Tampilan Deskripsi**: Browser menampilkan deskripsi yang dikembalikan dan menyimpannya di formulir
5. **Pengiriman Cerita**: "Generate Story" mengirim `description` dan `_csrf` ke `/generate-story`
6. **Pembuatan Cerita**: Kontroler memvalidasi deskripsi dan memanggil deployment yang sama dengan reasoning diatur `none`
7. **Render Template**: Thymeleaf meloloskan dan menampilkan deskripsi dan cerita di halaman hasil

**Alur Penanganan Kesalahan:**
Jika model gagal, server mencatat penyebabnya. Analisis gambar mengembalikan HTTP 502 dan browser menampilkan kesalahan tanpa menampilkan "Generate Story". Pembuatan cerita mengarahkan kembali ke formulir dengan pesan kesalahan. Tidak ada jalur yang secara diam-diam menggantikan hasil yang sudah ditulis sebelumnya.

## Memahami Integrasi AI

### Azure AI Foundry (tanpa kunci)
Layanan mengonfigurasi SDK dengan endpoint `/openai/v1/` sumber daya Anda. `DefaultAzureCredential` dan `AuthenticationUtil.getBearerTokenSupplier` menyediakan token Microsoft Entra untuk `https://ai.azure.com/.default`. Pengembangan lokal dapat menggunakan masuk CLI Azure Anda; aplikasi yang dihosting Azure dapat menggunakan managed identity dengan izin sumber daya yang diperlukan.

### Rekayasa Prompt
Analisis gambar meminta fitur hewan peliharaan yang dapat diamati dalam paragraf singkat dan memberitahu model untuk menganggap teks dalam gambar sebagai data, bukan instruksi. Pembuatan cerita menggunakan deskripsi yang dikembalikan dalam permintaan penulisan ramah keluarga yang terpisah. Kedua panggilan tidak mengaktifkan reasoning atau mengatur override suhu.

### Pemrosesan Respon
Penangan respon bersama menolak pilihan yang hilang dan konten kosong atau hanya spasi, memangkas konten valid, dan mempertahankan kegagalan hulu. Deskripsi gambar dibatasi hingga 1000 karakter agar muat di formulir cerita berikutnya. Kegagalan model asli disimpan untuk diagnosis tetapi tidak dirender ke pengguna.

## Langkah Selanjutnya

Untuk contoh lebih lanjut, lihat [Bab 04: Contoh praktis](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->