# Tutorial Teknik AI Generatif Inti

## Daftar Isi

- [Prasyarat](#prasyarat)
- [Memulai](#memulai)
- [Panduan Pemilihan Model](#panduan-pemilihan-model)
- [Tutorial 1: Penyelesaian dan Chat LLM](#tutorial-1-penyelesaian-dan-chat-llm)
- [Tutorial 2: Pemanggilan Fungsi](#tutorial-2-pemanggilan-fungsi)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: AI Bertanggung Jawab](#tutorial-4-ai-bertanggung-jawab)
- [Pola Umum di Berbagai Contoh](#pola-umum-di-berbagai-contoh)
- [Unit Test](#unit-test)
- [Verifikasi Langsung Berurutan](#verifikasi-langsung-berurutan)
- [Pemecahan Masalah](#pemecahan-masalah)
- [Langkah Selanjutnya](#langkah-selanjutnya)

## Ikhtisar

Empat program Java mandiri menunjukkan chat, riwayat percakapan, pemanggilan fungsi, retrieval-augmented generation (RAG) dokumen penuh, dan penanganan respons AI bertanggung jawab. Semua permintaan chat menargetkan **GPT-5.6 Luna dengan usaha penalaran `none`** secara default.

Contoh-contoh ini menggunakan SDK Java resmi OpenAI dengan endpoint Azure OpenAI v1, mengikuti [panduan SDK Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Paket lama `azure-ai-openai` tidak lagi menjadi dependensi. Chat Completions tetap digunakan untuk mengajarkan alur kerja berbasis pesan yang sudah ada; lihat [SDK Java OpenAI](https://github.com/openai/openai-java#microsoft-azure) untuk opsi API lain.

## Prasyarat

- Java 21 atau lebih baru dan Maven 3.6.3 atau lebih baru.
- Deployment chat Azure OpenAI bernama `gpt-5.6-luna`, atau override dengan setting Chat Completions yang kompatibel.
- Identitas Azure yang sudah masuk dengan peran **Cognitive Services OpenAI User** pada sumber daya. Pengembangan lokal menggunakan masuk Azure CLI Anda; aplikasi yang dihosting dapat menggunakan managed identity.
- Lihat [Bab 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) untuk pengaturan sumber daya dan petunjuk masuk.

[Konfigurasi Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) menetapkan versi berikut, yang dicek pada 2026-09-14:

| Komponen | Versi | Tujuan |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Klien resmi kompatibel Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Otentikasi tanpa kunci dan penyegaran token |
| `net.objecthunter:exp4j` | 0.4.8 | Parsing ekspresi aritmatik tanpa evaluasi kode |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Unit test Jupiter offline |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Kompilasi Java 21, tes, contoh yang dapat dijalankan |

Compiler menggunakan `--release 21`. Tidak diperlukan dependensi Spring Boot, Spring AI, atau LangChain4j pada contoh mandiri ini.

## Memulai

Dari direktori root repositori, atur endpoint sumber daya dan override deployment opsional di shell Anda.

**PowerShell Windows:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Tes tidak memerlukan kredensial Azure atau endpoint. Maven tidak otomatis membaca file lingkungan; atur variabel di shell yang digunakan untuk menjalankan contoh langsung. Untuk peluncuran IDE, verifikasi lingkungan yang disuplai oleh konfigurasi peluncuran Anda.

## Panduan Pemilihan Model

| Variabel lingkungan | Arti | Default |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Root sumber daya Azure HTTPS atau URL `/openai/v1` yang sudah dinormalisasi | Diperlukan untuk jalankan langsung |
| `AZURE_OPENAI_DEPLOYMENT` | Nama deployment chat, bukan versi model | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Konfigurasi deployment embedding terpisah, tidak digunakan oleh empat program ini | `text-embedding-3-small` |

Override deployment kosong menggunakan default. Konfigurasi melampirkan `/openai/v1` tepat sekali dan menolak kredensial, string kueri, serta path deployment lama pada endpoint.

Setiap permintaan chat secara eksplisit mengatur `reasoningEffort(ReasoningEffort.NONE)` dan `maxCompletionTokens(...)`. Tidak ada permintaan yang menetapkan `temperature`, `top_p`, atau opsi token completion lama. Ini termasuk pemilihan alat dan tindak lanjut hasil alat. Fungsi alat Chat Completions GPT-5.6 memerlukan usaha penalaran `none`; lihat [panduan chat Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Tidak ada titik masuk streaming atau embedding di bab ini.** Pembaca mendapatkan seluruh dokumennya, bukan vektornya. Jika Anda memperluas dengan embedding, gunakan deployment embedding terpisah seperti `text-embedding-3-small`, jangan Luna.

## Tutorial 1: Penyelesaian dan Chat LLM

Sumber: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program menjalankan penjelasan streaming Java sederhana, percakapan dua giliran HashMap/TreeMap, dan chat interaktif. Giliran kedua mencakup respons asisten pertama; setiap giliran interaktif juga mengirim percakapan sebelumnya.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` menyediakan deployment dan pengaturan penalaran eksplisit. Chat interaktif melewati baris kosong, berakhir pada `exit` atau EOF, dan menyimpan pesan sistem plus sembilan giliran pengguna/asisten yang sudah selesai. Pemangkasan jumlah giliran adalah batas pendidikan, bukan jaminan anggaran token pasti.

Dari direktori contoh:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Harapkan tiga jawaban awal, lalu prompt `You:`. Setiap pertanyaan interaktif tidak kosong menambah satu permintaan. Batas penyelesaian 200, 300, 400, kemudian 500 token per giliran interaktif.

## Tutorial 2: Pemanggilan Fungsi

Sumber: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK menurunkan skema JSON dari catatan `WeatherArguments` dan `CalculationArguments` yang diberi anotasi. Pilihan alat wajib membuat setiap contoh melatih protokol alat daripada menerima jawaban tanpa bantuan model.

1. Kirim pertanyaan dengan alat yang diizinkan, usaha penalaran `none`, dan batas penyelesaian 300 token.
2. Memerlukan alasan selesai `tool_calls`, validasi nama fungsi dan ID panggilan, serta parsing argumen JSON bertipe.
3. Jalankan fungsi lokal. Model tidak menjalankan kode Java atau kode sembarang.
4. Tambahkan pesan panggilan alat asisten sekali, diikuti setiap hasil dengan `tool_call_id` yang cocok.
5. Kirim satu permintaan akhir 300 token tanpa alat dan minta jawaban selesai yang tidak kosong.

`get_weather` mengembalikan cuaca **disimulasikan**, bukan langsung. Menghormati kota dan mengonversi contoh 22 derajat Celcius ke Fahrenheit bila diminta. `calculate` mengevaluasi ekspresi yang diberikan melalui exp4j, mendukung bentuk seperti `15% of 240` dan `2 + 3 * 4`, dan menolak perhitungan kosong, terlalu besar, tidak valid, atau tidak terbatas. Menggunakan aritmetika floating-point, bukan presisi desimal keuangan.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Harapkan `Function: get_weather`, cuaca Seattle yang disimulasikan, `Function: calculate`, `Function result: 36`, dan dua jawaban akhir. Tidak diperlukan stdin atau kredensial cuaca eksternal. Jalankan sukses dengan tepat empat permintaan chat.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Sumber: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Input: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Contoh RAG pengantar ini mengambil satu dokumen UTF-8 utuh dan memasukkannya dalam pesan pengguna bersama pertanyaan. Pesan sistem terpisah menginstruksikan model untuk memperlakukan isi dokumen sebagai data tidak terpercaya dan hanya menjawab dari konteks tersebut. Jika dokumen tidak berisi jawaban, respons yang diminta adalah: `Saya tidak dapat menemukan informasi tersebut dalam dokumen yang disediakan.`

Grounding dapat mengurangi halusinasi, tetapi delimiter atau instruksi sistem tidak menjamin akurasi atau mencegah setiap injeksi prompt. Tinjau jawaban langsung. RAG produksi biasanya menambahkan pemotongan, pengambilan, sitasi, pengendalian akses, dan evaluasi.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Masukkan satu pertanyaan, misalnya `Metode autentikasi mana yang dideskripsikan dalam dokumen?`. Harapkan jawaban yang menyebutkan Microsoft Entra ID. Program keluar setelah satu permintaan chat dengan batas penyelesaian 500 token.

Pencarian file default bekerja dari root repositori, direktori bab, atau direktori contoh. Path eksplisit juga didukung:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Input harus tidak kosong: maksimal 32 KiB data dokumen UTF-8 dan 2.000 karakter pertanyaan. File hilang, pertanyaan kosong/EOF, dan input terlalu besar gagal sebelum inferensi.

## Tutorial 4: AI Bertanggung Jawab

Sumber: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Enam probe mencakup instruksi membahayakan, ujaran kebencian, privasi, informasi medis salah, konten ilegal, dan pertanyaan AI bertanggung jawab yang benign (tidak berbahaya). Program mengamati respons daripada mengasumsikan setiap probe harus memicu filter.

| Hasil | Bukti |
| --- | --- |
| `FILTERED` | Kode kesalahan eksplisit `content_filter` / `ResponsibleAIPolicyViolation`, atau alasan selesai `content_filter` pada penyelesaian |
| `REFUSED` | Field `message.refusal` terstruktur yang tidak kosong |
| `POSSIBLE_REFUSAL` | Frasa penolakan pembuka dalam teks biasa; heuristik yang memerlukan peninjauan |
| `GENERATED` | Respons selesai yang tidak kosong; bukan bukti isi aman |

HTTP 400 biasa **bukan** bukti pemfilteran. Parameter tidak valid, kegagalan otentikasi, batas laju, kesalahan server, respons rusak, dan output terpotong gagal menjalankan, bukan menghasilkan keberhasilan keamanan palsu. Kata-kata luas seperti "konten berbahaya" dalam penjelasan benign tidak dihitung sebagai penolakan.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Harapkan enam hasil kategori dan ringkasan yang menyatakan observasi bukan sertifikasi keamanan. Tiap probe memiliki batas penyelesaian 300 token. Tinjau manual generasi tidak terduga dan penolakan kemungkinan; perbandingan benign harus menghasilkan penjelasan AI bertanggung jawab yang substansial. Tidak diperlukan stdin.

## Pola Umum di Berbagai Contoh

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) memusatkan normalisasi endpoint, override deployment, otentikasi tanpa kunci, dan opsi chat:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Pemasok token memperbarui token akses sesuai kebutuhan. Jangan mencatat token atau mengganti ini dengan kunci API. Setiap program menggunakan ulang kliennya dan menutupnya di `finally` atau melalui pembungkus `AutoCloseable` sendiri; SDK `OpenAIClient` sendiri bukan `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) memerlukan jawaban teks selesai yang tidak kosong. Pilihan kosong, penolakan, filter, dan jawaban terpotong tidak dicetak diam-diam sebagai keberhasilan. Contoh AI bertanggung jawab menangani hasil filter/penolakan yang diharapkan secara eksplisit. Kegagalan yang tidak tertangani memberikan kode keluar nonnol pada proses Java/Maven.

**Pengulangan SDK otomatis dinonaktifkan** agar jumlah permintaan tetap dapat diprediksi pada deployment RPM rendah bersama. Tiap permintaan inferensi memiliki batas waktu 60 detik. Akuisisi token mungkin memakan waktu tambahan. Penjadwalan tingkat aplikasi harus menghormati kuota; jangan menjalankan ulang permintaan berbayar yang gagal secara membabi buta.

## Unit Test

Dari direktori contoh:

```powershell
mvn -B -ntp clean test
```

Transport tes menggantikan lapisan HTTP SDK secara keseluruhan, menangkap badan permintaan serialized yang sebenarnya, dan menyediakan respons yang antre. Tidak membuka soket, tidak mengambil token Azure, dan gagal pada permintaan tak terduga. Tes ini memvalidasi perilaku aplikasi dan protokol SDK, bukan kualitas model langsung atau ketersediaan deployment.

| Bundel tes | Cakupan |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalisasi/penolakan endpoint, override deployment, opsi penalaran dan token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Semua alur kerja penyelesaian, riwayat pesan, pemangkasan giliran lengkap, EOF, kegagalan |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Skema alat, argumen bertipe, aritmetika, ID, beberapa hasil alat, tindak lanjut gagal |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Pencarian file, UTF-8, batas ukuran, muatan grounding, kesalahan input dan API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Enam probe, filter eksplisit, klasifikasi penolakan, kegagalan biasa 400 dan lainnya |

Untuk satu bundel, gunakan `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Fixture bersama ada di [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Verifikasi Langsung Berurutan

Panggilan langsung terpisah dari unit test. Gunakan perintah berikut **secara individu**, dari root repositori, hanya setelah kredensial dan akses deployment siap. Tidak diperlukan layanan atau proses persisten.

Untuk deployment bersama **10 permintaan/menit**, sediakan kuota cukup untuk seluruh program berikutnya sebelum menjalankannya: 5, 4, 1, lalu 6 permintaan. Proses berurutan saja tidak menjamin ketaatan batas laju. Koordinasikan menit berjalan dengan semua pemanggil lain; jangan tempel keempat pemanggilan sebagai batch tanpa jeda.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Penyelesaian, multi-giliran, dan dua giliran interaktif:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Periksa semua tiga judul bagian, lima jawaban, jawaban interaktif terakhir yang mengingat Ada, `Goodbye!`, dan kode keluar 0. Anggaran: **5 permintaan, maksimal 1.900 token penyelesaian**. Untuk jalankan yang lebih kecil, jalankan hanya `exit`: 3 permintaan / 900 token, tetapi itu tidak menjalankan inferensi interaktif.

**2. Kedua alur kerja pemanggilan fungsi:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Periksa kedua nama fungsi, cuaca Seattle yang disimulasikan, hasil perhitungan 36, dua jawaban akhir, dan kode keluar 0. Anggaran: **4 permintaan, maksimal 1.200 token penyelesaian**.

**3. Jawaban berbasis dokumen:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Periksa jalur dokumen, jawaban yang menyebutkan Microsoft Entra ID, dan kode keluar 0. Anggaran: **1 permintaan, maksimal 500 token penyelesaian**. [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) yang ada adalah satu-satunya file input yang diperlukan. Jalankan kedua opsional yang bertanya tentang topik yang tidak ada harus menahan diri dan menambahkan satu permintaan / 500 token.

**4. Pengamatan AI Bertanggung Jawab:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Periksa enam kategori dan ringkasan pengamatan, tinjau konten yang dihasilkan, dan pastikan kode keluar 0 untuk penyelesaian teknis. Proses yang berhasil keluar tidak menjamin keamanan model. Anggaran: **6 permintaan, maksimal 1.800 token penyelesaian**.

**Total untuk empat perintah: 16 permintaan chat dan maksimal 5.400 token penyelesaian**, plus token input (termasuk percakapan berulang dan skema/riwayat alat). Tidak ada permintaan embedding. Penggunaan token sesungguhnya tergantung model dan mungkin lebih rendah, terutama untuk prompt yang disaring. Biaya dolar tergantung harga penyebaran; tidak ada estimasi moneter tetap yang dimaksudkan. Semua batas permintaan mengasumsikan tidak ada rerun manual. Periksa `$LASTEXITCODE` segera setelah setiap perintah; nonzero berarti jalankan tidak selesai dengan sukses.

## Pemecahan Masalah

- **Endpoint hilang / 401 / 403:** Atur endpoint dalam proses peluncuran, verifikasi masuk Azure lokal dan peran sumber daya Anda, dan periksa untuk penggantian identitas lingkungan yang tidak sengaja.
- **400 / 404:** Pastikan penyebaran ada dan mendukung Chat Completions dengan usaha penalaran `none`. Gunakan root sumber daya HTTPS atau URL `/openai/v1`, bukan URL penyebaran lama. Kesalahan 400 biasa adalah kegagalan teknis, bukan blok keamanan.
- **429:** Koordinasikan RPM dan kuota token bersama sebelum mencoba lagi. Contoh sengaja tidak melakukan coba ulang otomatis.
- **`Incomplete chat response: length`:** Output mencapai batas penyelesaian. Tinjau respons dan prompt sebelum meningkatkan batas dan anggaran yang didokumentasikan; jangan rekam jalankan yang terpotong sebagai sukses.
- **Kesalahan file atau stdin:** Luncurkan dari direktori yang didukung atau gunakan jalur dokumen eksplisit. Berikan pertanyaan pembaca yang tidak kosong. Penyelesaian bisa berakhir normal pada EOF atau `exit`.
- **Kesalahan kompilasi:** Verifikasi Java 21 atau lebih baru, lalu jalankan `mvn -B -ntp clean test`. Di PowerShell, kutip seluruh argumen Maven yang mengandung properti bertitik, misalnya `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Langkah Selanjutnya

Lanjutkan ke [Bab 4: Contoh Praktis](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->