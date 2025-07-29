<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:58:06+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "ms"
}
-->
# Menyediakan Persekitaran Pembangunan untuk Generative AI dengan Java

> **Permulaan Pantas**: Kod di Awan dalam 2 minit - Lompat ke [Persediaan GitHub Codespaces](../../../02-SetupDevEnvironment) - tiada pemasangan tempatan diperlukan dan menggunakan model GitHub!

> **Berminat dengan Azure OpenAI?**, lihat [Panduan Persediaan Azure OpenAI](getting-started-azure-openai.md) kami dengan langkah-langkah untuk mencipta sumber Azure OpenAI baharu.

## Apa yang Anda Akan Pelajari

- Menyediakan persekitaran pembangunan Java untuk aplikasi AI
- Memilih dan mengkonfigurasi persekitaran pembangunan pilihan anda (berorientasikan awan dengan Codespaces, kontena pembangunan tempatan, atau persediaan tempatan penuh)
- Menguji persediaan anda dengan menyambung ke Model GitHub

## Kandungan

- [Apa yang Anda Akan Pelajari](../../../02-SetupDevEnvironment)
- [Pengenalan](../../../02-SetupDevEnvironment)
- [Langkah 1: Sediakan Persekitaran Pembangunan Anda](../../../02-SetupDevEnvironment)
  - [Pilihan A: GitHub Codespaces (Disyorkan)](../../../02-SetupDevEnvironment)
  - [Pilihan B: Kontena Pembangunan Tempatan](../../../02-SetupDevEnvironment)
  - [Pilihan C: Gunakan Pemasangan Tempatan Sedia Ada Anda](../../../02-SetupDevEnvironment)
- [Langkah 2: Cipta Token Akses Peribadi GitHub](../../../02-SetupDevEnvironment)
- [Langkah 3: Uji Persediaan Anda](../../../02-SetupDevEnvironment)
- [Penyelesaian Masalah](../../../02-SetupDevEnvironment)
- [Ringkasan](../../../02-SetupDevEnvironment)
- [Langkah Seterusnya](../../../02-SetupDevEnvironment)

## Pengenalan

Bab ini akan membimbing anda melalui proses menyediakan persekitaran pembangunan. Kami akan menggunakan **Model GitHub** sebagai contoh utama kerana ia percuma, mudah disediakan hanya dengan akaun GitHub, tidak memerlukan kad kredit, dan menyediakan akses kepada pelbagai model untuk eksperimen.

**Tiada persediaan tempatan diperlukan!** Anda boleh mula menulis kod dengan segera menggunakan GitHub Codespaces, yang menyediakan persekitaran pembangunan penuh dalam pelayar anda.

<img src="./images/models.webp" alt="Tangkapan skrin: Model GitHub" width="50%">

Kami mengesyorkan menggunakan [**Model GitHub**](https://github.com/marketplace?type=models) untuk kursus ini kerana:
- **Percuma** untuk bermula
- **Mudah** disediakan hanya dengan akaun GitHub
- **Tiada kad kredit** diperlukan
- **Pelbagai model** tersedia untuk eksperimen

> **Nota**: Model GitHub yang digunakan dalam latihan ini mempunyai had percuma berikut:
> - 15 permintaan seminit (150 sehari)
> - ~8,000 perkataan masuk, ~4,000 perkataan keluar setiap permintaan
> - 5 permintaan serentak
> 
> Untuk penggunaan produksi, naik taraf ke Model Azure AI Foundry dengan akaun Azure anda. Kod anda tidak perlu diubah. Lihat [dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Langkah 1: Sediakan Persekitaran Pembangunan Anda

<a name="quick-start-cloud"></a>

Kami telah mencipta kontena pembangunan yang telah dikonfigurasi untuk meminimumkan masa persediaan dan memastikan anda mempunyai semua alat yang diperlukan untuk kursus Generative AI dengan Java ini. Pilih pendekatan pembangunan pilihan anda:

### Pilihan Persediaan Persekitaran:

#### Pilihan A: GitHub Codespaces (Disyorkan)

**Mula menulis kod dalam 2 minit - tiada persediaan tempatan diperlukan!**

1. Fork repositori ini ke akaun GitHub anda
   > **Nota**: Jika anda ingin mengedit konfigurasi asas, sila lihat [Konfigurasi Kontena Pembangunan](../../../.devcontainer/devcontainer.json)
2. Klik **Code** → tab **Codespaces** → **...** → **New with options...**
3. Gunakan tetapan lalai – ini akan memilih **Konfigurasi Kontena Pembangunan**: **Persekitaran Pembangunan Generative AI Java** kontena pembangunan khusus yang dicipta untuk kursus ini
4. Klik **Create codespace**
5. Tunggu ~2 minit untuk persekitaran bersedia
6. Teruskan ke [Langkah 2: Cipta Token GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Tangkapan skrin: Submenu Codespaces" width="50%">

<img src="./images/image.png" alt="Tangkapan skrin: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Tangkapan skrin: Pilihan create codespace" width="50%">

> **Kelebihan Codespaces**:
> - Tiada pemasangan tempatan diperlukan
> - Berfungsi pada mana-mana peranti dengan pelayar
> - Telah dikonfigurasi dengan semua alat dan kebergantungan
> - Percuma 60 jam sebulan untuk akaun peribadi
> - Persekitaran konsisten untuk semua pelajar

#### Pilihan B: Kontena Pembangunan Tempatan

**Untuk pembangun yang lebih suka pembangunan tempatan dengan Docker**

1. Fork dan klon repositori ini ke mesin tempatan anda
   > **Nota**: Jika anda ingin mengedit konfigurasi asas, sila lihat [Konfigurasi Kontena Pembangunan](../../../.devcontainer/devcontainer.json)
2. Pasang [Docker Desktop](https://www.docker.com/products/docker-desktop/) dan [VS Code](https://code.visualstudio.com/)
3. Pasang [sambungan Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) dalam VS Code
4. Buka folder repositori dalam VS Code
5. Apabila diminta, klik **Reopen in Container** (atau gunakan `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Tunggu kontena dibina dan dimulakan
7. Teruskan ke [Langkah 2: Cipta Token GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Tangkapan skrin: Persediaan kontena pembangunan" width="50%">

<img src="./images/image-3.png" alt="Tangkapan skrin: Pembinaan kontena pembangunan selesai" width="50%">

#### Pilihan C: Gunakan Pemasangan Tempatan Sedia Ada Anda

**Untuk pembangun dengan persekitaran Java sedia ada**

Prasyarat:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) atau IDE pilihan anda

Langkah:
1. Klon repositori ini ke mesin tempatan anda
2. Buka projek dalam IDE anda
3. Teruskan ke [Langkah 2: Cipta Token GitHub](../../../02-SetupDevEnvironment)

> **Petua Pro**: Jika anda mempunyai mesin berspesifikasi rendah tetapi ingin menggunakan VS Code secara tempatan, gunakan GitHub Codespaces! Anda boleh menyambungkan VS Code tempatan anda ke Codespace yang dihoskan di awan untuk mendapatkan yang terbaik dari kedua-dua dunia.

<img src="./images/image-2.png" alt="Tangkapan skrin: Instans kontena pembangunan tempatan yang dicipta" width="50%">

## Langkah 2: Cipta Token Akses Peribadi GitHub

1. Navigasi ke [Tetapan GitHub](https://github.com/settings/profile) dan pilih **Settings** dari menu profil anda.
2. Di bar sisi kiri, klik **Developer settings** (biasanya di bahagian bawah).
3. Di bawah **Personal access tokens**, klik **Fine-grained tokens** (atau ikuti pautan langsung ini [link](https://github.com/settings/personal-access-tokens)).
4. Klik **Generate new token**.
5. Di bawah "Token name", berikan nama deskriptif (contoh: `GenAI-Java-Course-Token`).
6. Tetapkan tarikh luput (disyorkan: 7 hari untuk amalan keselamatan terbaik).
7. Di bawah "Resource owner", pilih akaun pengguna anda.
8. Di bawah "Repository access", pilih repositori yang anda ingin gunakan dengan Model GitHub (atau "All repositories" jika diperlukan).
9. Di bawah "Repository permissions", cari **Models** dan tetapkan kepada **Read and write**.
10. Klik **Generate token**.
11. **Salin dan simpan token anda sekarang** – anda tidak akan dapat melihatnya lagi!

> **Petua Keselamatan**: Gunakan skop minimum yang diperlukan dan tempoh luput terpendek yang praktikal untuk token akses anda.

## Langkah 3: Uji Persediaan Anda dengan Contoh Model GitHub

Setelah persekitaran pembangunan anda bersedia, mari uji integrasi Model GitHub dengan aplikasi contoh kami di [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Buka terminal dalam persekitaran pembangunan anda.
2. Navigasi ke contoh Model GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Tetapkan token GitHub anda sebagai pembolehubah persekitaran:
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

Anda sepatutnya melihat output yang serupa dengan:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Memahami Kod Contoh

Pertama, mari fahami apa yang baru sahaja dijalankan. Contoh di bawah `examples/github-models` menggunakan OpenAI Java SDK untuk menyambung ke Model GitHub:

**Apa yang kod ini lakukan:**
- **Menyambung** ke Model GitHub menggunakan token akses peribadi anda
- **Menghantar** mesej ringkas "Say Hello World!" kepada model AI
- **Menerima** dan memaparkan respons AI
- **Mengesahkan** persediaan anda berfungsi dengan betul

**Kebergantungan Utama** (dalam `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Kod Utama** (`App.java`):
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

Hebat! Anda kini telah menyediakan semuanya:

- Mencipta Token Akses Peribadi GitHub dengan kebenaran yang betul untuk akses model AI
- Menjalankan persekitaran pembangunan Java anda (sama ada Codespaces, kontena pembangunan, atau tempatan)
- Menyambung ke Model GitHub menggunakan OpenAI Java SDK untuk pembangunan AI percuma
- Menguji semuanya berfungsi dengan contoh mudah yang berinteraksi dengan model AI

## Langkah Seterusnya

[Bab 3: Teknik Teras Generative AI](../03-CoreGenerativeAITechniques/README.md)

## Penyelesaian Masalah

Menghadapi masalah? Berikut adalah masalah biasa dan penyelesaiannya:

- **Token tidak berfungsi?** 
  - Pastikan anda menyalin keseluruhan token tanpa ruang tambahan
  - Sahkan token ditetapkan dengan betul sebagai pembolehubah persekitaran
  - Periksa bahawa token anda mempunyai kebenaran yang betul (Models: Read and write)

- **Maven tidak dijumpai?** 
  - Jika menggunakan kontena pembangunan/Codespaces, Maven sepatutnya telah dipasang
  - Untuk persediaan tempatan, pastikan Java 21+ dan Maven 3.9+ dipasang
  - Cuba `mvn --version` untuk mengesahkan pemasangan

- **Masalah sambungan?** 
  - Periksa sambungan internet anda
  - Sahkan GitHub boleh diakses dari rangkaian anda
  - Pastikan anda tidak berada di belakang firewall yang menyekat titik akhir Model GitHub

- **Kontena pembangunan tidak bermula?** 
  - Pastikan Docker Desktop sedang berjalan (untuk pembangunan tempatan)
  - Cuba bina semula kontena: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Ralat pengkompilasi aplikasi?**
  - Pastikan anda berada di direktori yang betul: `02-SetupDevEnvironment/examples/github-models`
  - Cuba bersihkan dan bina semula: `mvn clean compile`

> **Perlu bantuan?**: Masih menghadapi masalah? Buka isu dalam repositori dan kami akan membantu anda.

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang berwibawa. Untuk maklumat yang kritikal, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.