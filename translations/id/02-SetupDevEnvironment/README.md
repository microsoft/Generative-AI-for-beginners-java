<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:55:38+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "id"
}
-->
# Menyiapkan Lingkungan Pengembangan untuk Generative AI dengan Java

> **Quick Start**: Kode di Cloud dalam 2 menit - Langsung ke [Pengaturan GitHub Codespaces](../../../02-SetupDevEnvironment) - tidak perlu instalasi lokal dan menggunakan model GitHub!

> **Tertarik dengan Azure OpenAI?**, lihat [Panduan Pengaturan Azure OpenAI](getting-started-azure-openai.md) kami dengan langkah-langkah untuk membuat sumber daya Azure OpenAI baru.

## Apa yang Akan Anda Pelajari

- Menyiapkan lingkungan pengembangan Java untuk aplikasi AI
- Memilih dan mengonfigurasi lingkungan pengembangan pilihan Anda (berbasis cloud dengan Codespaces, kontainer pengembangan lokal, atau pengaturan lokal penuh)
- Menguji pengaturan Anda dengan menghubungkan ke Model GitHub

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../02-SetupDevEnvironment)
- [Pendahuluan](../../../02-SetupDevEnvironment)
- [Langkah 1: Menyiapkan Lingkungan Pengembangan Anda](../../../02-SetupDevEnvironment)
  - [Opsi A: GitHub Codespaces (Direkomendasikan)](../../../02-SetupDevEnvironment)
  - [Opsi B: Kontainer Pengembangan Lokal](../../../02-SetupDevEnvironment)
  - [Opsi C: Menggunakan Instalasi Lokal yang Ada](../../../02-SetupDevEnvironment)
- [Langkah 2: Membuat Token Akses Pribadi GitHub](../../../02-SetupDevEnvironment)
- [Langkah 3: Menguji Pengaturan Anda](../../../02-SetupDevEnvironment)
- [Pemecahan Masalah](../../../02-SetupDevEnvironment)
- [Ringkasan](../../../02-SetupDevEnvironment)
- [Langkah Selanjutnya](../../../02-SetupDevEnvironment)

## Pendahuluan

Bab ini akan memandu Anda melalui proses pengaturan lingkungan pengembangan. Kami akan menggunakan **Model GitHub** sebagai contoh utama karena gratis, mudah diatur hanya dengan akun GitHub, tidak memerlukan kartu kredit, dan menyediakan akses ke beberapa model untuk eksperimen.

**Tidak perlu pengaturan lokal!** Anda dapat langsung mulai coding menggunakan GitHub Codespaces, yang menyediakan lingkungan pengembangan lengkap di browser Anda.

<img src="./images/models.webp" alt="Screenshot: Model GitHub" width="50%">

Kami merekomendasikan menggunakan [**Model GitHub**](https://github.com/marketplace?type=models) untuk kursus ini karena:
- **Gratis** untuk memulai
- **Mudah** diatur hanya dengan akun GitHub
- **Tidak memerlukan kartu kredit**
- **Beberapa model** tersedia untuk eksperimen

> **Catatan**: Model GitHub yang digunakan dalam pelatihan ini memiliki batasan gratis berikut:
> - 15 permintaan per menit (150 per hari)
> - ~8.000 kata masuk, ~4.000 kata keluar per permintaan
> - 5 permintaan bersamaan
> 
> Untuk penggunaan produksi, tingkatkan ke Model Azure AI Foundry dengan akun Azure Anda. Kode Anda tidak perlu diubah. Lihat [dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Langkah 1: Menyiapkan Lingkungan Pengembangan Anda

<a name="quick-start-cloud"></a>

Kami telah membuat kontainer pengembangan yang telah dikonfigurasi sebelumnya untuk meminimalkan waktu pengaturan dan memastikan Anda memiliki semua alat yang diperlukan untuk kursus Generative AI dengan Java ini. Pilih pendekatan pengembangan yang Anda sukai:

### Opsi Pengaturan Lingkungan:

#### Opsi A: GitHub Codespaces (Direkomendasikan)

**Mulai coding dalam 2 menit - tidak perlu pengaturan lokal!**

1. Fork repositori ini ke akun GitHub Anda
   > **Catatan**: Jika Anda ingin mengedit konfigurasi dasar, silakan lihat [Konfigurasi Kontainer Pengembangan](../../../.devcontainer/devcontainer.json)
2. Klik **Code** → tab **Codespaces** → **...** → **New with options...**
3. Gunakan pengaturan default – ini akan memilih **Konfigurasi kontainer pengembangan**: **Generative AI Java Development Environment** kontainer pengembangan khusus yang dibuat untuk kursus ini
4. Klik **Create codespace**
5. Tunggu ~2 menit hingga lingkungan siap
6. Lanjutkan ke [Langkah 2: Membuat Token GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Screenshot: submenu Codespaces" width="50%">

<img src="./images/image.png" alt="Screenshot: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Create codespace options" width="50%">

> **Keuntungan Codespaces**:
> - Tidak memerlukan instalasi lokal
> - Berfungsi di perangkat apa pun dengan browser
> - Dikonfigurasi sebelumnya dengan semua alat dan dependensi
> - Gratis 60 jam per bulan untuk akun pribadi
> - Lingkungan yang konsisten untuk semua peserta

#### Opsi B: Kontainer Pengembangan Lokal

**Untuk pengembang yang lebih suka pengembangan lokal dengan Docker**

1. Fork dan clone repositori ini ke mesin lokal Anda
   > **Catatan**: Jika Anda ingin mengedit konfigurasi dasar, silakan lihat [Konfigurasi Kontainer Pengembangan](../../../.devcontainer/devcontainer.json)
2. Instal [Docker Desktop](https://www.docker.com/products/docker-desktop/) dan [VS Code](https://code.visualstudio.com/)
3. Instal ekstensi [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) di VS Code
4. Buka folder repositori di VS Code
5. Saat diminta, klik **Reopen in Container** (atau gunakan `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Tunggu hingga kontainer selesai dibangun dan dimulai
7. Lanjutkan ke [Langkah 2: Membuat Token GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Screenshot: pengaturan kontainer pengembangan" width="50%">

<img src="./images/image-3.png" alt="Screenshot: kontainer pengembangan selesai dibangun" width="50%">

#### Opsi C: Menggunakan Instalasi Lokal yang Ada

**Untuk pengembang dengan lingkungan Java yang sudah ada**

Prasyarat:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) atau IDE pilihan Anda

Langkah-langkah:
1. Clone repositori ini ke mesin lokal Anda
2. Buka proyek di IDE Anda
3. Lanjutkan ke [Langkah 2: Membuat Token GitHub](../../../02-SetupDevEnvironment)

> **Pro Tip**: Jika Anda memiliki mesin dengan spesifikasi rendah tetapi ingin menggunakan VS Code secara lokal, gunakan GitHub Codespaces! Anda dapat menghubungkan VS Code lokal Anda ke Codespace yang di-host di cloud untuk mendapatkan manfaat terbaik dari keduanya.

<img src="./images/image-2.png" alt="Screenshot: instance kontainer pengembangan lokal yang dibuat" width="50%">

## Langkah 2: Membuat Token Akses Pribadi GitHub

1. Navigasikan ke [Pengaturan GitHub](https://github.com/settings/profile) dan pilih **Settings** dari menu profil Anda.
2. Di sidebar kiri, klik **Developer settings** (biasanya di bagian bawah).
3. Di bawah **Personal access tokens**, klik **Fine-grained tokens** (atau ikuti [tautan langsung](https://github.com/settings/personal-access-tokens)).
4. Klik **Generate new token**.
5. Di bawah "Token name", berikan nama deskriptif (misalnya, `GenAI-Java-Course-Token`).
6. Tetapkan tanggal kedaluwarsa (disarankan: 7 hari untuk praktik keamanan terbaik).
7. Di bawah "Resource owner", pilih akun pengguna Anda.
8. Di bawah "Repository access", pilih repositori yang ingin Anda gunakan dengan Model GitHub (atau "All repositories" jika diperlukan).
9. Di bawah "Repository permissions", temukan **Models** dan atur ke **Read and write**.
10. Klik **Generate token**.
11. **Salin dan simpan token Anda sekarang** – Anda tidak akan melihatnya lagi!

> **Tip Keamanan**: Gunakan cakupan minimum yang diperlukan dan waktu kedaluwarsa terpendek yang praktis untuk token akses Anda.

## Langkah 3: Menguji Pengaturan Anda dengan Contoh Model GitHub

Setelah lingkungan pengembangan Anda siap, mari kita uji integrasi Model GitHub dengan aplikasi contoh kami di [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Buka terminal di lingkungan pengembangan Anda.
2. Navigasikan ke contoh Model GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Tetapkan token GitHub Anda sebagai variabel lingkungan:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Jalankan aplikasi:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Anda akan melihat output yang mirip dengan:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Memahami Kode Contoh

Pertama, mari kita pahami apa yang baru saja dijalankan. Contoh di bawah `examples/github-models` menggunakan OpenAI Java SDK untuk terhubung ke Model GitHub:

**Apa yang dilakukan kode ini:**
- **Menghubungkan** ke Model GitHub menggunakan token akses pribadi Anda
- **Mengirimkan** pesan sederhana "Say Hello World!" ke model AI
- **Menerima** dan menampilkan respons AI
- **Memvalidasi** pengaturan Anda berfungsi dengan benar

**Dependensi Utama** (di `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Kode Utama** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Ringkasan

Hebat! Anda sekarang memiliki semua yang diperlukan:

- Membuat Token Akses Pribadi GitHub dengan izin yang tepat untuk akses model AI
- Menjalankan lingkungan pengembangan Java Anda (baik itu Codespaces, kontainer pengembangan, atau lokal)
- Terhubung ke Model GitHub menggunakan OpenAI Java SDK untuk pengembangan AI gratis
- Menguji semuanya berfungsi dengan contoh sederhana yang berbicara dengan model AI

## Langkah Selanjutnya

[Bab 3: Teknik Inti Generative AI](../03-CoreGenerativeAITechniques/README.md)

## Pemecahan Masalah

Mengalami masalah? Berikut adalah masalah umum dan solusinya:

- **Token tidak berfungsi?** 
  - Pastikan Anda menyalin seluruh token tanpa spasi tambahan
  - Verifikasi token diatur dengan benar sebagai variabel lingkungan
  - Periksa bahwa token Anda memiliki izin yang benar (Models: Read and write)

- **Maven tidak ditemukan?** 
  - Jika menggunakan kontainer pengembangan/Codespaces, Maven harus sudah terinstal
  - Untuk pengaturan lokal, pastikan Java 21+ dan Maven 3.9+ terinstal
  - Coba `mvn --version` untuk memverifikasi instalasi

- **Masalah koneksi?** 
  - Periksa koneksi internet Anda
  - Verifikasi GitHub dapat diakses dari jaringan Anda
  - Pastikan Anda tidak berada di belakang firewall yang memblokir endpoint Model GitHub

- **Kontainer pengembangan tidak dimulai?** 
  - Pastikan Docker Desktop berjalan (untuk pengembangan lokal)
  - Coba bangun ulang kontainer: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Kesalahan kompilasi aplikasi?**
  - Pastikan Anda berada di direktori yang benar: `02-SetupDevEnvironment/examples/github-models`
  - Coba bersihkan dan bangun ulang: `mvn clean compile`

> **Butuh bantuan?**: Masih mengalami masalah? Buka masalah di repositori dan kami akan membantu Anda.

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan hasil yang akurat, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi yang bersifat kritis, disarankan menggunakan jasa penerjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.