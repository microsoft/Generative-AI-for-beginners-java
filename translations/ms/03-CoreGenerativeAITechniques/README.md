<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:56:21+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "ms"
}
-->
# Tutorial Teknik Teras AI Generatif

## Kandungan

- [Prasyarat](../../../03-CoreGenerativeAITechniques)
- [Memulakan](../../../03-CoreGenerativeAITechniques)
  - [Langkah 1: Tetapkan Pembolehubah Persekitaran Anda](../../../03-CoreGenerativeAITechniques)
  - [Langkah 2: Navigasi ke Direktori Contoh](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: Penyempurnaan dan Chat LLM](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Pemanggilan Fungsi](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: AI Bertanggungjawab](../../../03-CoreGenerativeAITechniques)
- [Corak Biasa Dalam Contoh](../../../03-CoreGenerativeAITechniques)
- [Langkah Seterusnya](../../../03-CoreGenerativeAITechniques)
- [Penyelesaian Masalah](../../../03-CoreGenerativeAITechniques)
  - [Isu Biasa](../../../03-CoreGenerativeAITechniques)

## Gambaran Keseluruhan

Tutorial ini menyediakan contoh praktikal teknik teras AI generatif menggunakan Java dan Model GitHub. Anda akan belajar cara berinteraksi dengan Model Bahasa Besar (LLM), melaksanakan pemanggilan fungsi, menggunakan generasi yang diperkaya dengan pengambilan (RAG), dan menerapkan amalan AI yang bertanggungjawab.

## Prasyarat

Sebelum memulakan, pastikan anda mempunyai:
- Java 21 atau lebih tinggi dipasang
- Maven untuk pengurusan kebergantungan
- Akaun GitHub dengan token akses peribadi (PAT)

## Memulakan

### Langkah 1: Tetapkan Pembolehubah Persekitaran Anda

Pertama, anda perlu menetapkan token GitHub anda sebagai pembolehubah persekitaran. Token ini membolehkan anda mengakses Model GitHub secara percuma.

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

### Langkah 2: Navigasi ke Direktori Contoh

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: Penyempurnaan dan Chat LLM

**Fail:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Apa Yang Diajar oleh Contoh Ini

Contoh ini menunjukkan mekanik teras interaksi Model Bahasa Besar (LLM) melalui API OpenAI, termasuk inisialisasi klien dengan Model GitHub, corak struktur mesej untuk arahan sistem dan pengguna, pengurusan keadaan perbualan melalui pengumpulan sejarah mesej, dan penalaan parameter untuk mengawal panjang respons dan tahap kreativiti.

### Konsep Kod Utama

#### 1. Persediaan Klien
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Ini mencipta sambungan ke Model GitHub menggunakan token anda.

#### 2. Penyempurnaan Mudah
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

#### 3. Memori Perbualan
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI mengingati mesej sebelumnya hanya jika anda menyertakannya dalam permintaan berikutnya.

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Apa Yang Berlaku Apabila Anda Menjalankannya

1. **Penyempurnaan Mudah**: AI menjawab soalan Java dengan panduan arahan sistem
2. **Chat Berbilang Pusingan**: AI mengekalkan konteks merentasi beberapa soalan
3. **Chat Interaktif**: Anda boleh berbual secara langsung dengan AI

## Tutorial 2: Pemanggilan Fungsi

**Fail:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Apa Yang Diajar oleh Contoh Ini

Pemanggilan fungsi membolehkan model AI meminta pelaksanaan alat dan API luaran melalui protokol berstruktur di mana model menganalisis permintaan bahasa semula jadi, menentukan pemanggilan fungsi yang diperlukan dengan parameter yang sesuai menggunakan definisi JSON Schema, dan memproses hasil yang dikembalikan untuk menghasilkan respons kontekstual, sementara pelaksanaan fungsi sebenar kekal di bawah kawalan pembangun untuk keselamatan dan kebolehpercayaan.

### Konsep Kod Utama

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

Ini memberitahu AI fungsi apa yang tersedia dan cara menggunakannya.

#### 2. Aliran Pelaksanaan Fungsi
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

#### 3. Pelaksanaan Fungsi
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

### Apa Yang Berlaku Apabila Anda Menjalankannya

1. **Fungsi Cuaca**: AI meminta data cuaca untuk Seattle, anda memberikannya, AI memformat respons
2. **Fungsi Kalkulator**: AI meminta pengiraan (15% daripada 240), anda mengiranya, AI menerangkan hasilnya

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**Fail:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Apa Yang Diajar oleh Contoh Ini

Retrieval-Augmented Generation (RAG) menggabungkan pengambilan maklumat dengan generasi bahasa dengan menyuntik konteks dokumen luaran ke dalam arahan AI, membolehkan model memberikan jawapan yang tepat berdasarkan sumber pengetahuan tertentu dan bukannya data latihan yang mungkin sudah lapuk atau tidak tepat, sambil mengekalkan sempadan yang jelas antara pertanyaan pengguna dan sumber maklumat yang berwibawa melalui kejuruteraan arahan strategik.

### Konsep Kod Utama

#### 1. Pemuatan Dokumen
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Suntikan Konteks
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

Tanda petikan tiga membantu AI membezakan antara konteks dan soalan.

#### 3. Pengendalian Respons Selamat
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Sentiasa sahkan respons API untuk mengelakkan kerosakan.

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Apa Yang Berlaku Apabila Anda Menjalankannya

1. Program memuatkan `document.txt` (mengandungi maklumat tentang Model GitHub)
2. Anda bertanya soalan tentang dokumen tersebut
3. AI menjawab berdasarkan kandungan dokumen sahaja, bukan pengetahuan amnya

Cuba tanya: "Apa itu Model GitHub?" vs "Bagaimana cuaca hari ini?"

## Tutorial 4: AI Bertanggungjawab

**Fail:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Apa Yang Diajar oleh Contoh Ini

Contoh AI Bertanggungjawab menunjukkan kepentingan melaksanakan langkah keselamatan dalam aplikasi AI. Ia menunjukkan penapis keselamatan yang mengesan kategori kandungan berbahaya termasuk ucapan kebencian, gangguan, bahaya diri, kandungan seksual, dan keganasan, menunjukkan bagaimana aplikasi AI pengeluaran harus menangani pelanggaran dasar kandungan dengan baik melalui pengendalian pengecualian yang betul, mekanisme maklum balas pengguna, dan strategi respons alternatif.

### Konsep Kod Utama

#### 1. Rangka Kerja Ujian Keselamatan
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

#### 2. Kategori Keselamatan Yang Diuji
- Arahan keganasan/bahaya
- Ucapan kebencian
- Pelanggaran privasi
- Maklumat salah perubatan
- Aktiviti haram

### Jalankan Contoh
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Apa Yang Berlaku Apabila Anda Menjalankannya

Program menguji pelbagai arahan berbahaya dan menunjukkan bagaimana sistem keselamatan AI:
1. **Menyekat permintaan berbahaya** dengan ralat HTTP 400
2. **Membenarkan kandungan selamat** dihasilkan seperti biasa
3. **Melindungi pengguna** daripada output AI yang berbahaya

## Corak Biasa Dalam Contoh

### Corak Pengesahan
Semua contoh menggunakan corak ini untuk mengesahkan dengan Model GitHub:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Corak Pengendalian Ralat
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Corak Struktur Mesej
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Langkah Seterusnya

[Bab 04: Contoh praktikal](../04-PracticalSamples/README.md)

## Penyelesaian Masalah

### Isu Biasa

**"GITHUB_TOKEN tidak ditetapkan"**
- Pastikan anda menetapkan pembolehubah persekitaran
- Sahkan token anda mempunyai skop `models:read`

**"Tiada respons daripada API"**
- Periksa sambungan internet anda
- Sahkan token anda sah
- Periksa jika anda telah mencapai had kadar

**Ralat kompilasi Maven**
- Pastikan anda mempunyai Java 21 atau lebih tinggi
- Jalankan `mvn clean compile` untuk menyegarkan kebergantungan

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang berwibawa. Untuk maklumat yang kritikal, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.