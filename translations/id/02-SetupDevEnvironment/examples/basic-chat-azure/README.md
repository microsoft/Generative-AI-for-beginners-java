# Obrolan Dasar dengan Azure AI Foundry - Contoh End-to-End

Contoh ini adalah aplikasi Spring Boot sederhana yang terhubung ke model **Azure AI Foundry** menggunakan **autentikasi tanpa kunci** (Microsoft Entra ID) dan menguji pengaturan Anda. Ini menggunakan `ChatClient` dari Spring AI, didukung oleh **OpenAI Java SDK resmi** dan endpoint **Azure OpenAI v1**.

Versi dalam [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) adalah Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, dan dotenv-java **3.2.0**. Contoh ini menggunakan `spring-ai-starter-model-openai` dan secara eksplisit menyatakan `openai-java` dan `azure-identity`; Spring AI 2 menghapus starter Azure OpenAI yang lama.

## Daftar Isi

- [Persyaratan](#persyaratan)
- [Mulai Cepat](#mulai-cepat)
- [Cara Kerja Autentikasi](#cara-kerja-autentikasi)
- [Menjalankan Aplikasi](#menjalankan-aplikasi)
  - [Menggunakan Maven](#menggunakan-maven)
  - [Menggunakan VS Code](#menggunakan-vs-code)
  - [Output yang Diharapkan](#output-yang-diharapkan)
- [Referensi Konfigurasi](#referensi-konfigurasi)
  - [Variabel Lingkungan](#variabel-lingkungan)
  - [Konfigurasi Spring](#konfigurasi-spring)
- [Pemecahan Masalah](#pemecahan-masalah)
  - [Masalah Umum](#masalah-umum)
  - [Mode Debug](#mode-debug)
- [Langkah Berikutnya](#langkah-berikutnya)
- [Sumber Daya](#sumber-daya)

## Persyaratan

Sebelum menjalankan contoh ini, pastikan Anda memiliki:

- Sumber daya Azure AI Foundry dengan deployment `gpt-5.6-luna` - sediakan dengan `azd up` atau secara manual melalui [panduan setup Azure AI Foundry](../../getting-started-azure-openai.md)
- Peran **Cognitive Services OpenAI User** pada sumber daya tersebut (template Bicep menetapkan ini untuk Anda)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), sudah masuk dengan `az login`
- Java 21+ dan Maven 3.9+

> **Tidak diperlukan kunci API** — autentikasi tanpa kunci menggunakan Microsoft Entra ID.

## Mulai Cepat

```bash
# 1. Navigasi ke proyek
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Masuk agar autentikasi tanpa kunci dapat mendapatkan token
az login

# 3. Konfigurasikan endpoint
#    - Jika Anda menjalankan `azd up`, .env sudah ditulis untuk Anda (lewati ini).
#    - Jika tidak, salin template dan atur AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Jalankan aplikasi
mvn spring-boot:run
```

## Cara Kerja Autentikasi

Contoh ini melakukan autentikasi dengan **Microsoft Entra ID** — tidak ada kunci API.

Aplikasi mengonfigurasi autentikasi secara eksplisit di [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` membuat `BearerTokenCredential` menggunakan `AuthenticationUtil.getBearerTokenSupplier` dengan `DefaultAzureCredential` dan cakupan `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` membangun `OpenAIClient` dengan `OpenAIOkHttpClient.builder()`, mengarahkan endpoint sumber daya ke `/openai/v1`, dan memberikan kredensial bearer dengan `.credential(...)`.
3. `azureChatModel()` memberikan klien tersebut ke Spring AI `OpenAiChatModel`, yang mendukung `ChatClient` pada contoh ini.

Bean eksplisit ini mencegah `OPENAI_API_KEY` global menimpa autentikasi Azure. Menghilangkan kunci API dari YAML saja bukanlah pengaturan autentikasi. `DefaultAzureCredential` dapat menggunakan sesi `az login` Anda secara lokal atau managed identity di Azure; identitas yang dipilih harus memiliki peran sumber daya yang disebutkan di atas.

## Menjalankan Aplikasi

### Menggunakan Maven

```bash
mvn spring-boot:run
```

### Menggunakan VS Code

1. Buka proyek di VS Code
2. Tekan `F5` atau gunakan panel "Run and Debug"
3. Pilih konfigurasi "Spring Boot-BasicChatApplication"

> **Catatan**: Aplikasi memuat `.env` dari direktori kerja, termasuk saat dijalankan dari VS Code.

### Output yang Diharapkan

Contoh output setelah menjalankan dengan sukses (log awal dihilangkan; kata-kata respons bervariasi):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Referensi Konfigurasi

### Variabel Lingkungan

| Variabel | Deskripsi | Wajib | Contoh |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL endpoint Foundry (Azure OpenAI) | Ya | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nama deployment model chat | Tidak | `gpt-5.6-luna` (default) |

> Tidak ada variabel kunci API — autentikasi tanpa kunci (Microsoft Entra ID via `az login`).

### Konfigurasi Spring

Pengaturan di [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) menggunakan prefix `spring.ai.openai` dan properti chat yang diratakan (tanpa blok `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` adalah **nama deployment Azure**. Autentikasi berasal dari bean eksplisit yang dijelaskan di atas, bukan pengaturan `api-key`. Contoh ini menonaktifkan reasoning dan membatasi token penyelesaian hingga 500; `temperature` dan `max-tokens` lama dibiarkan tidak diatur.

Microsoft merekomendasikan [SDK OpenAI resmi dengan Azure OpenAI v1 dan Responses API untuk aplikasi baru](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions masih didukung untuk pelajaran berbasis pesan ini. Untuk GPT-5.6, permintaan yang mencakup alat pada Chat Completions harus mengatur `reasoning_effort` ke `none`; gunakan Responses saat menggabungkan reasoning dengan alat. Lihat [pemanggilan alat dengan model reasoning](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Pemecahan Masalah

### Masalah Umum

<details>
<summary><strong>Error: 401 / "PermissionDenied" / kesalahan token</strong></summary>

- Jalankan `az login` — autentikasi tanpa kunci memerlukan sesi masuk aktif untuk mendapatkan token
- Pastikan akun Anda memiliki peran **Cognitive Services OpenAI User** pada sumber daya tersebut
- Jika Anda baru menetapkan peran, tunggu beberapa menit agar propagasi selesai
- Pastikan Anda berada di tenant/berlangganan yang benar (`az account show`)
</details>

<details>
<summary><strong>Error: "Endpoint tidak valid" / kesalahan koneksi</strong></summary>

- Pastikan `AZURE_OPENAI_ENDPOINT` adalah URL dasar lengkap (misal, `https://your-resource.openai.azure.com/`)
- Periksa konsistensi slash di akhir URL
- Pastikan endpoint sesuai dengan sumber daya yang Anda sediakan (`azd env get-values`)
</details>

<details>
<summary><strong>Error: "Deployment tidak ditemukan"</strong></summary>

- Pastikan `AZURE_OPENAI_DEPLOYMENT` sesuai dengan nama deployment di Azure
- Periksa apakah model sudah berhasil dideploy dan aktif
- Nama deployment default adalah `gpt-5.6-luna`
</details>

<details>
<summary><strong>Error: 429 / batas kuota terlampaui</strong></summary>

- Deployment GPT-5.6 Luna default memiliki Kapasitas Global Standar 10: 10 permintaan/menit dan 10.000 token/menit
- Jalankan contoh secara berurutan dan tunggu interval retry layanan sebelum mencoba lagi
- Contoh dasar ini menonaktifkan retry otomatis SDK, sehingga permintaan gagal langsung dilaporkan
</details>

<details>
<summary><strong>VS Code: Variabel lingkungan tidak dimuat</strong></summary>

- Pastikan file `.env` berada di direktori root proyek (tingkat yang sama dengan `pom.xml`)
- Coba jalankan `mvn spring-boot:run` di terminal terintegrasi VS Code
- Periksa bahwa ekstensi Java di VS Code sudah terpasang dengan benar
</details>

### Mode Debug

Untuk mengaktifkan logging detail, hapus komentar baris-baris ini di [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Langkah Berikutnya

**Pengaturan Selesai!** Lanjutkan perjalanan pembelajaran Anda:

[Bab 3: Teknik Inti Generative AI](../../../03-CoreGenerativeAITechniques/README.md)

## Sumber Daya

- [Transisi Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK resmi dengan Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentikasi tanpa kunci dengan Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->