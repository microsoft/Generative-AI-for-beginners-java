# Menyediakan Persekitaran Pembangunan untuk Azure AI Foundry

> Panduan ini menyediakan model **Azure AI Foundry** untuk aplikasi AI Java dalam kursus ini, menggunakan pengesahan **tanpa kekunci** (Microsoft Entra ID) — tiada kunci API untuk diurus. Baru dengan alat ini? Mula dengan [panduan persekitaran pembangunan](./README.md).

Panduan ini menyediakan model **Azure AI Foundry** untuk aplikasi AI Java dalam kursus ini. Anda mempunyai dua pilihan:

- **Pilihan A — Sediakan dengan `azd` + Bicep (disyorkan):** satu arahan memasang akaun Foundry dan model sebagai kod. Tiada klik dalam portal.
- **Pilihan B — Buat sumber secara manual** di portal Azure AI Foundry.

Kedua-dua pilihan menggunakan **pengesahan tanpa kekunci** (Microsoft Entra ID) — tiada kunci API untuk disalin atau bocor.

## Jadual Kandungan

- [Apa Yang Dicipta](#apa-yang-dicipta)
- [Prasyarat](#prasyarat)
- [Pilihan A: Sediakan dengan azd + Bicep (Disyorkan)](#option-a-provision-with-azd--bicep-recommended)
- [Pilihan B: Buat Sumber Secara Manual](#pilihan-b-buat-sumber-secara-manual)
- [Konfigurasikan Persekitaran Anda](#konfigurasikan-persekitaran-anda)
- [Uji Persediaan Anda](#uji-persediaan-anda)
- [Apa Seterusnya?](#apa-seterusnya)
- [Sumber](#sumber)
- [Sumber Tambahan](#sumber-tambahan)

## Apa Yang Dicipta

Templat Bicep dalam [`infra/`](../../../02-SetupDevEnvironment/infra) memasang:

- Akaun **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, jenis `AIServices`) dengan projek
- Pelantikan **bual** - GPT-5.6 Luna (`gpt-5.6-luna`), versi `2026-07-09`, dengan kapasiti `GlobalStandard` `10` (10 permintaan/minit dan 10,000 token/minit untuk model ini)
- Pelantikan **penyisipan** - `text-embedding-3-small`, versi `1` (digunakan dalam bab-bab berikutnya)
- Penetapan **peranan tanpa kekunci** (`Cognitive Services OpenAI User`) supaya anda log masuk dengan `az login` dan tidak perlu mengurus kunci

## Prasyarat

- [Langganan Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) dan [Maven 3.9+](https://maven.apache.org/download.cgi)

## Pilihan A: Sediakan dengan azd + Bicep (Disyorkan)

Dari folder `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Log masuk (kedua-dua alat)
azd auth login
az login

# Menyediakan akaun Foundry + pelaksanaan model
azd up
```

`azd` akan meminta **nama persekitaran** (contohnya `genai-java`), **langganan**, dan **rantau**. Pilih langganan anda sendiri dan rantau di mana `gpt-5.6-luna` dan `text-embedding-3-small` tersedia, contohnya `eastus2`. Sahkan bahawa langganan mempunyai kuota yang mencukupi untuk model dan jenis pelantikan di rantau tersebut; ketersediaan dan kuota berbeza mengikut langganan.

Apabila penyediaan selesai, azd:

1. Melaksanakan semua yang ditakrifkan dalam [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Menjalankan hook selepas penyediaan yang menulis [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) dengan nama titik akhir dan pelantikan anda (tiada rahsia).

> **Petua:** Jalankan semula `azd up` bila-bila masa untuk memohon perubahan. Jalankan `azd down` untuk memadam semua dan hentikan kos.

Untuk melihat tetapan yang dijana:

```bash
azd env get-values
```

Sekarang teruskan ke [Uji Persediaan Anda](#uji-persediaan-anda).

## Pilihan B: Buat Sumber Secara Manual

Lebih suka portal? Buat sumber secara manual:

1. Pergi ke [portal Azure AI Foundry](https://ai.azure.com/) dan log masuk.
2. **Buat projek** (ini juga mencipta sumber AI Foundry). Beri nama seperti `GenAIJava`.
3. Dalam projek anda, buka **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Lancarkan **GPT-5.6 Luna** (nama model dan pelantikan `gpt-5.6-luna`, versi `2026-07-09`) dengan kapasiti **Global Standard** `10`. Ulang untuk **text-embedding-3-small**, versi `1`, jika anda mahu contoh penyisipan.
5. Dari **Overview**, salin **titik akhir** (contohnya `https://<resource>.openai.azure.com/`).
6. Berikan akses tanpa kekunci kepada diri sendiri: pada sumber, buka **Access control (IAM)** → **Add role assignment** → tetapkan **Cognitive Services OpenAI User** kepada akaun anda.

> **Masih mengalami masalah?** Lihat [dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurasikan Persekitaran Anda

**Jika anda menggunakan Pilihan A (`azd up`)**, fail tetapan anda sudah ditulis — tiada apa yang perlu dikonfigurasi. Teruskan ke [Uji Persediaan Anda](#uji-persediaan-anda).

**Jika menggunakan Pilihan B (manual)**, buat fail `.env` contoh sendiri:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Edit `.env` dengan titik akhir anda (tiada kunci — pengesahan tanpa kekunci):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Gunakan titik akhir Azure OpenAI sumber, bukan URL projek. Aplikasi basic-chat menyelesaikannya ke `/openai/v1` dan mengkonfigurasi pelanggan token Bearer secara eksplisit; kunci API tidak diperlukan.

> **Nota keselamatan:** Tiada kunci API untuk disimpan. Anda mengesahkan dengan Microsoft Entra ID melalui `az login` (secara tempatan) atau identiti terurus (dalam Azure). Fail `.env` hanya memegang tetapan bukan rahsia dan sudah dilindungi oleh `.gitignore`.

## Uji Persediaan Anda

Pastikan anda sudah log masuk supaya pengesahan tanpa kekunci boleh dapatkan token, kemudian jalankan contoh:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # jika anda belum log masuk
mvn clean spring-boot:run
```

Anda sepatutnya melihat respons dari model `gpt-5.6-luna`. Jalankan contoh secara berurutan untuk kekal dalam kuota kecil lalai; jika menerima HTTP 429, tunggu tempoh percubaan semula sebelum cuba lagi.

> **Pengguna VS Code:** Tekan `F5` untuk jalankan. Aplikasi memuatkan `.env` anda secara automatik.

> **Contoh lengkap:** Lihat [Contoh Basic Chat dengan Azure AI Foundry](./examples/basic-chat-azure/README.md) untuk butiran dan penyelesaian masalah.

## Apa Seterusnya?

Selepas penyediaan dan berjaya menjalankan contoh, anda akan mempunyai:
- Azure AI Foundry dengan `gpt-5.6-luna` dan `text-embedding-3-small` dipasang
- Pengesahan tanpa kekunci (Microsoft Entra ID) — tiada kunci untuk diurus
- Fail `.env` tempatan dengan nama titik akhir dan pelantikan anda
- Persekitaran pembangunan Java bersedia untuk digunakan

**Teruskan ke** [Bab 3: Teknik AI Generatif Teras](../03-CoreGenerativeAITechniques/README.md) untuk mula membina aplikasi AI!

## Sumber

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Pengesahan tanpa kekunci dengan Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Peralihan Spring AI 2 ke OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK OpenAI Java Rasmi dengan Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Sumber Tambahan

- [Muat Turun VS Code](https://code.visualstudio.com/Download)
- [Dapatkan Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfigurasi Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan oleh manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->