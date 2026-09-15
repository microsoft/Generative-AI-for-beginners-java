# Menyiapkan Lingkungan Pengembangan untuk Azure AI Foundry

> Panduan ini menyiapkan model **Azure AI Foundry** untuk aplikasi AI Java dalam kursus ini, menggunakan autentikasi **tanpa kunci** (Microsoft Entra ID) — tanpa kunci API yang perlu dikelola. Baru mengenal alat ini? Mulailah dengan [panduan lingkungan pengembangan](./README.md).

Panduan ini menyiapkan model **Azure AI Foundry** untuk aplikasi AI Java dalam kursus ini. Anda memiliki dua pilihan:

- **Opsi A — Menyediakan dengan `azd` + Bicep (direkomendasikan):** satu perintah untuk menyebarkan akun Foundry dan model sebagai kode. Tidak perlu klik portal.
- **Opsi B — Membuat sumber daya secara manual** di portal Azure AI Foundry.

Kedua jalur menggunakan **autentikasi tanpa kunci** (Microsoft Entra ID) — tidak ada kunci API yang perlu disalin atau bocor.

## Daftar Isi

- [Apa yang Dibuat](#apa-yang-dibuat)
- [Prasyarat](#prasyarat)
- [Opsi A: Menyediakan dengan azd + Bicep (Direkomendasikan)](#option-a-provision-with-azd--bicep-recommended)
- [Opsi B: Membuat Sumber Daya Secara Manual](#opsi-b-membuat-sumber-daya-secara-manual)
- [Konfigurasi Lingkungan Anda](#konfigurasi-lingkungan-anda)
- [Uji Pengaturan Anda](#uji-pengaturan-anda)
- [Apa Selanjutnya?](#apa-selanjutnya)
- [Sumber Daya](#sumber-daya)
- [Sumber Daya Tambahan](#sumber-daya-tambahan)

## Apa yang Dibuat

Template Bicep dalam [`infra/`](../../../02-SetupDevEnvironment/infra) menyediakan:

- Sebuah akun **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, jenis `AIServices`) dengan sebuah proyek
- Sebuah penyebaran **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), versi `2026-07-09`, dengan kapasitas `GlobalStandard` `10` (10 permintaan/menit dan 10.000 token/menit untuk model ini)
- Sebuah penyebaran **embedding** - `text-embedding-3-small`, versi `1` (digunakan di bab-bab selanjutnya)
- Sebuah **penugasan peran tanpa kunci** (`Cognitive Services OpenAI User`) sehingga Anda masuk dengan `az login` tanpa mengelola kunci

## Prasyarat

- Sebuah [langganan Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) dan [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opsi A: Menyediakan dengan azd + Bicep (Direkomendasikan)

Dari folder `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Masuk (kedua alat)
azd auth login
az login

# Menyediakan akun Foundry + penyebaran model
azd up
```

`azd` akan meminta **nama lingkungan** (misalnya `genai-java`), **langganan**, dan **wilayah**. Pilih langganan Anda sendiri dan wilayah di mana `gpt-5.6-luna` dan `text-embedding-3-small` tersedia, misalnya `eastus2`. Pastikan langganan memiliki kuota yang cukup untuk model dan tipe penyebaran di wilayah tersebut; ketersediaan dan kuota dapat berbeda per langganan.

Saat penyediaan selesai, azd:

1. Menyebarkan semua yang didefinisikan dalam [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Menjalankan hook pascapenyediaan yang menulis [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) dengan nama endpoint dan penyebaran Anda (tanpa rahasia).

> **Tips:** Jalankan ulang `azd up` kapan pun untuk menerapkan perubahan. Jalankan `azd down` untuk menghapus semuanya dan berhenti menimbulkan biaya.

Untuk melihat pengaturan yang dihasilkan:

```bash
azd env get-values
```

Sekarang lompat ke [Uji Pengaturan Anda](#uji-pengaturan-anda).

## Opsi B: Membuat Sumber Daya Secara Manual

Lebih suka portal? Buat sumber daya secara manual:

1. Pergi ke [portal Azure AI Foundry](https://ai.azure.com/) dan masuk.
2. **Buat proyek** (ini juga membuat sumber daya AI Foundry). Beri nama seperti `GenAIJava`.
3. Dalam proyek Anda, buka **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Sebarkan **GPT-5.6 Luna** (nama model dan penyebaran `gpt-5.6-luna`, versi `2026-07-09`) dengan kapasitas **Global Standard** `10`. Ulangi untuk **text-embedding-3-small**, versi `1`, jika Anda ingin contoh embedding.
5. Dari **Overview**, salin **endpoint** (misalnya `https://<resource>.openai.azure.com/`).
6. Berikan akses tanpa kunci untuk diri Anda: pada sumber daya, buka **Access control (IAM)** → **Add role assignment** → tetapkan **Cognitive Services OpenAI User** ke akun Anda.

> **Masih mengalami kesulitan?** Lihat [dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurasi Lingkungan Anda

**Jika Anda menggunakan Opsi A (`azd up`)**, file pengaturan Anda sudah dibuat — tidak perlu konfigurasi. Langsung ke [Uji Pengaturan Anda](#uji-pengaturan-anda).

**Jika Anda menggunakan Opsi B (manual)**, buat file `.env` contoh sendiri:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Sunting `.env` dengan endpoint Anda (tanpa kunci — autentikasi menggunakan tanpa kunci):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Gunakan endpoint Azure OpenAI dari sumber daya, bukan URL proyek. Aplikasi basic-chat mengarahkannya ke `/openai/v1` dan mengonfigurasi klien token bearer eksplisit; kunci API tidak diperlukan.

> **Catatan keamanan:** Tidak ada kunci API yang perlu disimpan. Anda melakukan autentikasi dengan Microsoft Entra ID melalui `az login` (secara lokal) atau identitas terkelola (di Azure). File `.env` hanya berisi pengaturan non-rahasia dan sudah tercakup oleh `.gitignore`.

## Uji Pengaturan Anda

Pastikan Anda sudah masuk agar autentikasi tanpa kunci bisa mendapatkan token, lalu jalankan contoh:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # jika Anda belum masuk
mvn clean spring-boot:run
```

Anda harus melihat respons dari model `gpt-5.6-luna`. Jalankan contoh secara berurutan agar tetap dalam kuota kecil default; jika menerima HTTP 429, tunggu interval retry sebelum mencoba lagi.

> **Pengguna VS Code:** Tekan `F5` untuk menjalankan. Aplikasi memuat `.env` Anda secara otomatis.

> **Contoh lengkap:** Lihat [Contoh Basic Chat dengan Azure AI Foundry](./examples/basic-chat-azure/README.md) untuk detail dan cara mengatasi masalah.

## Apa Selanjutnya?

Setelah penyediaan dan menjalankan contoh dengan sukses, Anda akan memiliki:
- Azure AI Foundry dengan `gpt-5.6-luna` dan `text-embedding-3-small` yang sudah disebarkan
- Autentikasi tanpa kunci (Microsoft Entra ID) — tanpa kunci yang perlu dikelola
- File `.env` lokal dengan endpoint dan nama penyebaran Anda
- Lingkungan pengembangan Java yang siap digunakan

**Lanjutkan ke** [Bab 3: Teknik AI Generatif Inti](../03-CoreGenerativeAITechniques/README.md) untuk mulai membangun aplikasi AI!

## Sumber Daya

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autentikasi tanpa kunci dengan Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Transisi Spring AI 2 ke OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK resmi dengan Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Sumber Daya Tambahan

- [Unduh VS Code](https://code.visualstudio.com/Download)
- [Dapatkan Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfigurasi Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->