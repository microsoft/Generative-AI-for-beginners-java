<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:54:09+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "id"
}
-->
# Tutorial Teknik Inti Generative AI

## Daftar Isi

- [Prasyarat](../../../03-CoreGenerativeAITechniques)
- [Memulai](../../../03-CoreGenerativeAITechniques)
  - [Langkah 1: Atur Variabel Lingkungan Anda](../../../03-CoreGenerativeAITechniques)
  - [Langkah 2: Arahkan ke Direktori Contoh](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: Penyelesaian LLM dan Obrolan](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Pemanggilan Fungsi](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: AI yang Bertanggung Jawab](../../../03-CoreGenerativeAITechniques)
- [Pola Umum di Seluruh Contoh](../../../03-CoreGenerativeAITechniques)
- [Langkah Selanjutnya](../../../03-CoreGenerativeAITechniques)
- [Pemecahan Masalah](../../../03-CoreGenerativeAITechniques)
  - [Masalah Umum](../../../03-CoreGenerativeAITechniques)

## Gambaran Umum

Tutorial ini menyediakan contoh praktis tentang teknik inti generative AI menggunakan Java dan GitHub Models. Anda akan belajar cara berinteraksi dengan Large Language Models (LLMs), mengimplementasikan pemanggilan fungsi, menggunakan retrieval-augmented generation (RAG), dan menerapkan praktik AI yang bertanggung jawab.

## Prasyarat

Sebelum memulai, pastikan Anda memiliki:
- Java 21 atau versi lebih tinggi terinstal
- Maven untuk manajemen dependensi
- Akun GitHub dengan personal access token (PAT)

## Memulai

### Langkah 1: Atur Variabel Lingkungan Anda

Pertama, Anda perlu mengatur token GitHub Anda sebagai variabel lingkungan. Token ini memungkinkan Anda mengakses GitHub Models secara gratis.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Langkah 2: Arahkan ke Direktori Contoh

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: Penyelesaian LLM dan Obrolan

**File:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Apa yang Diajarkan Contoh Ini

Contoh ini menunjukkan mekanisme inti interaksi dengan Large Language Model (LLM) melalui OpenAI API, termasuk inisialisasi klien dengan GitHub Models, pola struktur pesan untuk prompt sistem dan pengguna, manajemen status percakapan melalui akumulasi riwayat pesan, dan penyesuaian parameter untuk mengontrol panjang respons dan tingkat kreativitas.

### Konsep Kode Utama

#### 1. Pengaturan Klien
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Ini membuat koneksi ke GitHub Models menggunakan token Anda.

#### 2. Penyelesaian Sederhana
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Memori Percakapan
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI hanya mengingat pesan sebelumnya jika Anda menyertakannya dalam permintaan berikutnya.

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Apa yang Terjadi Saat Anda Menjalankannya

1. **Penyelesaian Sederhana**: AI menjawab pertanyaan Java dengan panduan prompt sistem
2. **Obrolan Multi-putaran**: AI mempertahankan konteks di beberapa pertanyaan
3. **Obrolan Interaktif**: Anda dapat melakukan percakapan nyata dengan AI

## Tutorial 2: Pemanggilan Fungsi

**File:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Apa yang Diajarkan Contoh Ini

Pemanggilan fungsi memungkinkan model AI meminta eksekusi alat dan API eksternal melalui protokol terstruktur di mana model menganalisis permintaan bahasa alami, menentukan pemanggilan fungsi yang diperlukan dengan parameter yang sesuai menggunakan definisi JSON Schema, dan memproses hasil yang dikembalikan untuk menghasilkan respons kontekstual, sementara eksekusi fungsi sebenarnya tetap berada di bawah kendali pengembang untuk keamanan dan keandalan.

### Konsep Kode Utama

#### 1. Definisi Fungsi
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Ini memberi tahu AI fungsi apa yang tersedia dan cara menggunakannya.

#### 2. Alur Eksekusi Fungsi
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Implementasi Fungsi
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Apa yang Terjadi Saat Anda Menjalankannya

1. **Fungsi Cuaca**: AI meminta data cuaca untuk Seattle, Anda memberikannya, AI memformat respons
2. **Fungsi Kalkulator**: AI meminta perhitungan (15% dari 240), Anda menghitungnya, AI menjelaskan hasilnya

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**File:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Apa yang Diajarkan Contoh Ini

Retrieval-Augmented Generation (RAG) menggabungkan pengambilan informasi dengan generasi bahasa dengan menyuntikkan konteks dokumen eksternal ke dalam prompt AI, memungkinkan model memberikan jawaban yang akurat berdasarkan sumber pengetahuan tertentu daripada data pelatihan yang mungkin sudah usang atau tidak akurat, sambil menjaga batasan yang jelas antara pertanyaan pengguna dan sumber informasi otoritatif melalui rekayasa prompt strategis.

### Konsep Kode Utama

#### 1. Memuat Dokumen
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Penyuntikan Konteks
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Tanda kutip tiga membantu AI membedakan antara konteks dan pertanyaan.

#### 3. Penanganan Respons Aman
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Selalu validasi respons API untuk mencegah crash.

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Apa yang Terjadi Saat Anda Menjalankannya

1. Program memuat `document.txt` (berisi info tentang GitHub Models)
2. Anda mengajukan pertanyaan tentang dokumen tersebut
3. AI menjawab hanya berdasarkan konten dokumen, bukan pengetahuan umumnya

Coba tanyakan: "Apa itu GitHub Models?" vs "Bagaimana cuaca hari ini?"

## Tutorial 4: AI yang Bertanggung Jawab

**File:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Apa yang Diajarkan Contoh Ini

Contoh AI yang Bertanggung Jawab menunjukkan pentingnya menerapkan langkah-langkah keamanan dalam aplikasi AI. Ini menunjukkan filter keamanan yang mendeteksi kategori konten berbahaya termasuk ujaran kebencian, pelecehan, menyakiti diri sendiri, konten seksual, dan kekerasan, menunjukkan bagaimana aplikasi AI produksi harus menangani pelanggaran kebijakan konten dengan baik melalui penanganan pengecualian yang tepat, mekanisme umpan balik pengguna, dan strategi respons cadangan.

### Konsep Kode Utama

#### 1. Kerangka Pengujian Keamanan
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Kategori Keamanan yang Diuji
- Instruksi Kekerasan/Menyakiti
- Ujaran kebencian
- Pelanggaran privasi
- Informasi medis yang salah
- Aktivitas ilegal

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Apa yang Terjadi Saat Anda Menjalankannya

Program menguji berbagai prompt berbahaya dan menunjukkan bagaimana sistem keamanan AI:
1. **Memblokir permintaan berbahaya** dengan kesalahan HTTP 400
2. **Mengizinkan konten aman** untuk dihasilkan secara normal
3. **Melindungi pengguna** dari output AI yang berbahaya

## Pola Umum di Seluruh Contoh

### Pola Autentikasi
Semua contoh menggunakan pola ini untuk autentikasi dengan GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Pola Penanganan Kesalahan
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Pola Struktur Pesan
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Langkah Selanjutnya

[Chapter 04: Contoh Praktis](../04-PracticalSamples/README.md)

## Pemecahan Masalah

### Masalah Umum

**"GITHUB_TOKEN not set"**
- Pastikan Anda telah mengatur variabel lingkungan
- Verifikasi token Anda memiliki cakupan `models:read`

**"No response from API"**
- Periksa koneksi internet Anda
- Verifikasi token Anda valid
- Periksa apakah Anda telah mencapai batas kuota

**Kesalahan kompilasi Maven**
- Pastikan Anda memiliki Java 21 atau lebih tinggi
- Jalankan `mvn clean compile` untuk menyegarkan dependensi

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan hasil yang akurat, harap diingat bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi yang bersifat kritis, disarankan menggunakan jasa penerjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.