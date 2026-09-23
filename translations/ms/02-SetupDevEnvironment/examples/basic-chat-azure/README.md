# Sembang Asas dengan Azure AI Foundry - Contoh Sepanjang Proses

Contoh ini adalah aplikasi Spring Boot yang mudah yang menyambung ke model **Azure AI Foundry** menggunakan **pengesahan tanpa kunci** (Microsoft Entra ID) dan menguji persediaan anda. Ia menggunakan `ChatClient` Spring AI, disokong oleh **SDK OpenAI Java rasmi** dan titik akhir **Azure OpenAI v1**.

Versi dalam [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) adalah Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, dan dotenv-java **3.2.0**. Contoh ini menggunakan `spring-ai-starter-model-openai` dan dengan jelas menyatakan `openai-java` dan `azure-identity`; Spring AI 2 membuang starter Azure OpenAI lama.

## Jadual Kandungan

- [Prasyarat](#prasyarat)
- [Mula Dengan Cepat](#mula-dengan-cepat)
- [Bagaimana Pengesahan Berfungsi](#bagaimana-pengesahan-berfungsi)
- [Menjalankan Aplikasi](#menjalankan-aplikasi)
  - [Menggunakan Maven](#menggunakan-maven)
  - [Menggunakan VS Code](#menggunakan-vs-code)
  - [Output Dijangka](#output-dijangka)
- [Rujukan Konfigurasi](#rujukan-konfigurasi)
  - [Pemboleh Ubah Persekitaran](#pemboleh-ubah-persekitaran)
  - [Konfigurasi Spring](#konfigurasi-spring)
- [Penyelesaian Masalah](#penyelesaian-masalah)
  - [Isu Biasa](#isu-biasa)
  - [Mod Debug](#mod-debug)
- [Langkah Seterusnya](#langkah-seterusnya)
- [Sumber](#sumber)

## Prasyarat

Sebelum menjalankan contoh ini, pastikan anda mempunyai:

- Sumber Azure AI Foundry dengan penggunaan `gpt-5.6-luna` - sediakan dengan `azd up` atau secara manual melalui [panduan persediaan Azure AI Foundry](../../getting-started-azure-openai.md)
- Peranan **Pengguna Cognitive Services OpenAI** pada sumber tersebut (templat Bicep menetapkan ini untuk anda)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), telah log masuk dengan `az login`
- Java 21+ dan Maven 3.9+

> **Tiada kunci API diperlukan** — pengesahan adalah tanpa kunci melalui Microsoft Entra ID.

## Mula Dengan Cepat

```bash
# 1. Navigasi ke projek
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Log masuk supaya pengesahan tanpa kunci dapat memperoleh token
az login

# 3. Konfigurasikan titik akhir
#    - Jika anda menjalankan `azd up`, .env telah ditulis untuk anda (langkau ini).
#    - Jika tidak, salin templat dan tetapkan AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Jalankan aplikasi
mvn spring-boot:run
```

## Bagaimana Pengesahan Berfungsi

Contoh ini mengesahkan dengan **Microsoft Entra ID** — tiada kunci API.

Aplikasi mengkonfigurasi pengesahan secara jelas dalam [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` mencipta `BearerTokenCredential` menggunakan `AuthenticationUtil.getBearerTokenSupplier` dengan `DefaultAzureCredential` dan skop `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` membina `OpenAIClient` dengan `OpenAIOkHttpClient.builder()`, menyelesaikan titik akhir sumber ke `/openai/v1`, dan membekalkan kelayakan pembawa dengan `.credential(...)`.
3. `azureChatModel()` membekalkan klien itu kepada `OpenAiChatModel` Spring AI, yang menyokong `ChatClient` dalam pelajaran ini.

Beans eksplisit ini mengelakkan `OPENAI_API_KEY` global menimpa pengesahan Azure. Tidak membekalkan kunci API dalam YAML sahaja bukanlah konfigurasi pengesahan. `DefaultAzureCredential` boleh menggunakan sesi `az login` anda secara lokal atau identiti terurus di Azure; identiti yang dipilih mesti mempunyai peranan sumber yang disenaraikan di atas.

## Menjalankan Aplikasi

### Menggunakan Maven

```bash
mvn spring-boot:run
```

### Menggunakan VS Code

1. Buka projek dalam VS Code
2. Tekan `F5` atau gunakan panel "Run and Debug"
3. Pilih konfigurasi "Spring Boot-BasicChatApplication"

> **Nota**: Aplikasi memuat `.env` dari direktori kerja, termasuk apabila dilancarkan dari VS Code.

### Output Dijangka

Output ilustrasi selepas kejayaan menjalankan (log permulaan diabaikan; ayat balasan berbeza):

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

## Rujukan Konfigurasi

### Pemboleh Ubah Persekitaran

| Pemboleh Ubah | Penerangan | Diperlukan | Contoh |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL titik akhir Foundry (Azure OpenAI) | Ya | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nama penggunaan model sembang | Tidak | `gpt-5.6-luna` (lalai) |

> Tiada pemboleh ubah kunci API — pengesahan adalah tanpa kunci (Microsoft Entra ID melalui `az login`).

### Konfigurasi Spring

Tetapan dalam [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) menggunakan awalan `spring.ai.openai` dan sifat sembang rata (tiada blok `options`):

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

`model` adalah **nama penggunaan Azure**. Pengesahan berasal dari beans eksplisit yang diterangkan di atas, bukan tetapan `api-key`. Pelajaran ini melumpuhkan penalaran dan mengehadkan token lengkap kepada 500; ia tidak menetapkan `temperature` dan `max-tokens` lama.

Microsoft mengesyorkan [SDK OpenAI rasmi dengan Azure OpenAI v1 dan API Respon untuk aplikasi baru](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions masih disokong untuk pelajaran berasaskan mesej ini. Untuk GPT-5.6, permintaan yang merangkumi alat pada Chat Completions mesti menetapkan `reasoning_effort` kepada `none`; gunakan Responses ketika menggabungkan penalaran dengan alat. Lihat [pemanggilan alat dengan model penalaran](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Penyelesaian Masalah

### Isu Biasa

<details>
<summary><strong>Ralat: 401 / "PermissionDenied" / ralat token</strong></summary>

- Jalankan `az login` — pengesahan tanpa kunci memerlukan daftar masuk aktif untuk mendapatkan token
- Sahkan akaun anda mempunyai peranan **Pengguna Cognitive Services OpenAI** pada sumber
- Jika anda baru sahaja memberikan peranan, tunggu sebentar untuk ia tersebar
- Sahkan anda berada dalam tenant/subskripsi yang betul (`az account show`)
</details>

<details>
<summary><strong>Ralat: "Titik akhir tidak sah" / ralat sambungan</strong></summary>

- Pastikan `AZURE_OPENAI_ENDPOINT` adalah URL asas penuh (contoh, `https://your-resource.openai.azure.com/`)
- Periksa konsistensi garis miring akhir
- Sahkan titik akhir sepadan dengan sumber yang disediakan (`azd env get-values`)
</details>

<details>
<summary><strong>Ralat: "Penggunaan tidak ditemui"</strong></summary>

- Sahkan `AZURE_OPENAI_DEPLOYMENT` sepadan dengan nama penggunaan dalam Azure
- Periksa model berjaya digunakan dan aktif
- Nama penyebaran lalai ialah `gpt-5.6-luna`
</details>

<details>
<summary><strong>Ralat: 429 / had kadar melebihi</strong></summary>

- Penyebaran GPT-5.6 Luna lalai mempunyai kapasiti Standard Global 10: 10 permintaan/minit dan 10,000 token/minit
- Jalankan contoh secara berurutan dan tunggu selang masa cuba semula perkhidmatan sebelum mencuba semula
- Contoh asas ini mematikan cuba semula SDK automatik, jadi permintaan yang gagal dilaporkan secara terus
</details>

<details>
<summary><strong>VS Code: Pembolehubah persekitaran tidak dimuatkan</strong></summary>

- Pastikan fail `.env` anda berada di direktori akar projek (tahap yang sama seperti `pom.xml`)
- Cuba jalankan `mvn spring-boot:run` di terminal terintegrasi VS Code
- Semak bahawa peluasan Java VS Code dipasang dengan betul
</details>

### Mod Debug

Untuk mengaktifkan log terperinci, nyahkomen baris ini di [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Langkah Seterusnya

**Penyediaan Selesai!** Teruskan perjalanan pembelajaran anda:

[Bab 3: Teknik Teras AI Generatif](../../../03-CoreGenerativeAITechniques/README.md)

## Sumber

- [Peralihan Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java OpenAI Rasmi dengan Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Pengesahan tanpa kunci dengan Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Dokumentasi Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan oleh manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->