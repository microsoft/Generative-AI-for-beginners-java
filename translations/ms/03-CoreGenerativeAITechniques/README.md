# Tutorial Teknik AI Generatif Teras

## Jadual Kandungan

- [Prasyarat](#prasyarat)
- [Memulakan](#memulakan)
- [Panduan Pemilihan Model](#panduan-pemilihan-model)
- [Tutorial 1: Lengkapkan dan Chat LLM](#tutorial-1-lengkapkan-dan-chat-llm)
- [Tutorial 2: Panggilan Fungsi](#tutorial-2-panggilan-fungsi)
- [Tutorial 3: RAG (Generasi Dipertingkatkan Pengambilan)](#tutorial-3-rag-generasi-dipertingkatkan-pengambilan)
- [Tutorial 4: AI Bertanggungjawab](#tutorial-4-ai-bertanggungjawab)
- [Corak Biasa Merentasi Contoh](#corak-biasa-merentasi-contoh)
- [Ujian Unit](#ujian-unit)
- [Pengesahan Langsung Berturutan](#pengesahan-langsung-berturutan)
- [Penyelesaian Masalah](#penyelesaian-masalah)
- [Langkah Seterusnya](#langkah-seterusnya)

## Gambaran Keseluruhan

Empat program Java berdiri sendiri mempamerkan perbualan, sejarah perbualan, panggilan fungsi, generasi dipertingkatkan pengambilan dokumen penuh (RAG), dan pengendalian respons AI bertanggungjawab. Semua permintaan chat mensasarkan **GPT-5.6 Luna dengan usaha penalaran `none`** secara lalai.

Contoh ini menggunakan SDK Java rasmi OpenAI dengan titik hujung Azure OpenAI v1, mengikuti [panduan SDK Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Pakej lama `azure-ai-openai` tidak lagi bergantung. Lengkapkan Chat dikekalkan untuk mengajar aliran kerja berasaskan mesej yang sedia ada; lihat [SDK Java OpenAI](https://github.com/openai/openai-java#microsoft-azure) untuk pilihan API lain.

## Prasyarat

- Java 21 atau lebih baru dan Maven 3.6.3 atau lebih baru.
- Penggunaan chat Azure OpenAI dinamakan `gpt-5.6-luna`, atau lebihkan dengan tetapan Lengkapkan Chat yang serasi.
- Identiti Azure yang telah mendaftar masuk dengan peranan **Pengguna OpenAI Perkhidmatan Kognitif** pada sumber tersebut. Pembangunan tempatan menggunakan log masuk CLI Azure anda; aplikasi dihos menggunakan identiti terurus.
- Lihat [Bab 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) untuk arahan penyediaan sumber dan log masuk.

[Konfigurasi Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) menyematkan versi berikut, diperiksa pada 2026-09-14:

| Komponen | Versi | Tujuan |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Klien rasmi yang serasi Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Pengesahan tanpa kunci dan penyegaran token |
| `net.objecthunter:exp4j` | 0.4.8 | Penguraian ekspresi aritmetik tanpa penilaian kod |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Ujian unit Jupiter tanpa sambungan internet |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Kompilasi Java 21, ujian, contoh boleh jalan |

Penyusun menggunakan `--release 21`. Tiada kebergantungan Spring Boot, Spring AI, atau LangChain4j diperlukan untuk contoh berdiri sendiri ini.

## Memulakan

Dari akar repositori, tetapkan titik hujung sumber dan ganti penempatan opsyenal di shell anda.

**Windows PowerShell:**

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

Ujian tidak memerlukan kelayakan Azure atau titik hujung. Maven tidak secara automatik membaca fail persekitaran; tetapkan pembolehubah dalam shell yang digunakan untuk melancarkan contoh langsung. Untuk pelancaran IDE, sahkan persekitaran yang dibekalkan oleh konfigurasi pelancaran anda.

## Panduan Pemilihan Model

| Pembolehubah persekitaran | Maksud | Lalai |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Akar sumber Azure HTTPS atau URL yang sudah dinormalisasi `/openai/v1` | Diperlukan untuk larian langsung |
| `AZURE_OPENAI_DEPLOYMENT` | Nama penempatan chat, bukan versi model | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Konfigurasi penempatan embedding berasingan, tidak digunakan oleh keempat-empat program ini | `text-embedding-3-small` |

Ganti penempatan kosong menggunakan lalai. Konfigurasi menambah `/openai/v1` tepat satu kali dan menolak kelayakan, rentetan pertanyaan, dan laluan penempatan lama dalam titik hujung.

Setiap permintaan chat secara eksplisit menetapkan `reasoningEffort(ReasoningEffort.NONE)` dan `maxCompletionTokens(...)`. Tiada permintaan menetapkan `temperature`, `top_p`, atau pilihan token lengkap lama. Ini termasuk pilihan alat dan tindak balas hasil alat. Alat fungsi Lengkapkan Chat GPT-5.6 memerlukan usaha penalaran `none`; lihat [panduan chat Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Tiada pintu masuk penstriman atau embedding dalam bab ini.** Pembaca mengambil keseluruhan dokumen, bukan vektor. Jika anda meluaskan dengan embedding, gunakan penempatan embedding berasingan seperti `text-embedding-3-small`, bukan Luna.

## Tutorial 1: Lengkapkan dan Chat LLM

Sumber: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program menjalankan penjelasan aliran Java yang mudah, perbualan HashMap/TreeMap dua giliran, dan chat interaktif. Giliran kedua termasuk respons pembantu pertama; setiap giliran interaktif juga menghantar perbualan sebelumnya.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` membekalkan penempatan dan tetapan penalaran eksplisit. Chat interaktif melangkau baris kosong, tamat pada `exit` atau EOF, dan mengekalkan mesej sistem serta sembilan giliran lengkap pengguna/pembantu. Pemangkasan kiraan giliran adalah batas pendidikan, bukan jaminan bajet token tepat.

Dari direktori contoh:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Jangka tiga jawapan awal, kemudian pemberi prompt `You:`. Setiap soalan interaktif tidak kosong menambah satu permintaan. Had lengkapkan adalah 200, 300, 400, kemudian 500 token setiap giliran interaktif.

## Tutorial 2: Panggilan Fungsi

Sumber: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK memperoleh skema JSON dari rekod beranotasi `WeatherArguments` dan `CalculationArguments`. Pilihan alat wajib menjadikan setiap contoh melatih protokol alat bukan menerima jawapan tanpa bantuan model.

1. Hantar soalan dengan alat dibenarkan, usaha penalaran `none`, dan had lengkapkan 300 token.
2. Perlukan alasan tamat `tool_calls`, sahkan nama fungsi dan ID panggilan, dan analisis argumen JSON yang ditaip.
3. Jalankan fungsi tempatan. Model tidak melaksanakan kod Java atau kod sewenang-wenangnya.
4. Tambah mesej panggilan alat pembantu sekali, diikuti dengan setiap hasil dengan `tool_call_id` padanannya.
5. Hantar satu permintaan akhir 300 token tanpa alat dan perlukan jawapan selesai, tidak kosong.

`get_weather` memulangkan cuaca **bersimulasi**, bukan langsung. Ia menghormati bandar dan menukar sampel 22 darjah Celsius ke Fahrenheit bila diminta. `calculate` menilai ekspresi yang dibekalkan melalui exp4j, menyokong bentuk seperti `15% of 240` dan `2 + 3 * 4`, dan menolak pengiraan kosong, terlalu besar, tidak sah, atau bukan terhingga. Ia menggunakan aritmetik titik terapung, bukan ketepatan perpuluhan kewangan.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Jangka `Function: get_weather`, cuaca Seattle bersimulasi, `Function: calculate`, `Function result: 36`, dan dua jawapan akhir. Tiada stdin atau kelayakan cuaca luar diperlukan. Larian berjaya menggunakan tepat empat permintaan chat.

## Tutorial 3: RAG (Generasi Dipertingkatkan Pengambilan)

Sumber: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Input: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Contoh RAG pengenalan ini mengambil satu dokumen UTF-8 penuh dan memasukkannya dalam mesej pengguna bersama soalan. Mesej sistem berasingan mengarahkan model untuk menganggap kandungan dokumen sebagai data yang tidak dipercayai dan menjawab hanya dari konteks itu. Jika dokumen tidak mengandungi jawapan, respons yang diminta ialah: `I cannot find that information in the provided document.`

Pembumian boleh mengurangkan halusinasi, tetapi sama ada delimiter atau arahan sistem tidak menjamin ketepatan atau mencegah setiap suntikan prompt. Semak jawapan langsung. RAG produksi biasanya menambah pemecahan, pengambilan, sitasi, kawalan akses, dan penilaian.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Masukkan satu soalan, contohnya `Which authentication method does the document describe?`. Jangka jawapan menyebut Microsoft Entra ID. Program keluar selepas satu permintaan chat dengan had lengkapkan 500 token.

Pencarian fail lalai berfungsi dari akar repositori, direktori bab, atau direktori contoh. Laluan jelas juga disokong:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Input mesti tidak kosong: paling banyak 32 KiB data dokumen UTF-8 dan 2,000 aksara soalan. Fail yang hilang, soalan kosong/EOF, dan input terlalu besar gagal sebelum inferens.

## Tutorial 4: AI Bertanggungjawab

Sumber: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Enam probe merangkumi arahan berbahaya, ucapan kebencian, privasi, maklumat perubatan salah, kandungan haram, dan soalan AI bertanggungjawab yang benign. Program memerhati respons dan tidak menganggap setiap probe mesti mencetuskan penapis.

| Hasil | Bukti |
| --- | --- |
| `FILTERED` | Kod ralat `content_filter` / `ResponsibleAIPolicyViolation` yang jelas, atau alasan tamat `content_filter` dari lengkapkan |
| `REFUSED` | Ruang `message.refusal` terstruktur, tidak kosong |
| `POSSIBLE_REFUSAL` | Frasa penolakan pembuka dalam teks biasa; heuristik memerlukan semakan |
| `GENERATED` | Respons lengkap tidak kosong; bukan bukti kandungan selamat |

HTTP 400 biasa **bukan** bukti penapisan. Parameter tidak sah, kegagalan pengesahan, had kadar, ralat pelayan, respons cacat, dan output terpotong gagal larian dan bukan menghasilkan kejayaan keselamatan palsu. Perkataan umum seperti "kandungan berbahaya" dalam penjelasan benign tidak dikira sebagai penolakan.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Jangka enam hasil kategori dan ringkasan menyatakan pemerhatian bukan pensijilan keselamatan. Setiap probe had lengkapkan 300 token. Semak penjanaan tidak dijangka dan penolakan mungkin secara manual; perbandingan benign harus menghasilkan penjelasan AI bertanggungjawab yang substantif. Tiada stdin diperlukan.

## Corak Biasa Merentasi Contoh

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) memusatkan normalisasi titik hujung, ganti penempatan, pengesahan tanpa kunci, dan opsyen chat:

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

Pembekal token menyegarkan token akses mengikut keperluan. Jangan log token atau ganti dengan kunci API. Setiap program menggunakan semula pelanggannya dan menutupnya dalam `finally` atau melalui pembungkus `AutoCloseable` sendiri; `OpenAIClient` SDK itu sendiri bukan `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) memerlukan jawapan teks yang lengkap dan tidak kosong. Pilihan kosong, penolakan, penapis, dan jawapan terpotong tidak dicetak senyap sebagai kejayaan. Contoh AI bertanggungjawab mengendalikan hasil penapis/penolakan yang dijangka secara eksplisit. Kegagalan yang tidak dikendalikan memberi kod keluar tidak sifar kepada proses Java/Maven.

**Cuba semula SDK automatik dimatikan** untuk memastikan kiraan permintaan boleh diramal pada penempatan RPM rendah berkongsi. Setiap permintaan inferens mempunyai had masa 60 saat. Pemerolehan token mungkin mengambil masa tambahan. Penjadualan peringkat aplikasi mesti menghormati kuota; jangan jalankan semula permintaan berbayar yang gagal tanpa semakan.

## Ujian Unit

Dari direktori contoh:

```powershell
mvn -B -ntp clean test
```

Pengangkutan ujian menggantikan lapisan HTTP SDK sepenuhnya, menangkap badan permintaan bersiri sebenar, dan membekalkan respons beratur. Ia tidak membuka soket, tidak memperoleh token Azure, dan gagal pada permintaan tidak dijangka. Ujian ini mengesahkan tingkah laku aplikasi dan protokol SDK, bukan kualiti model langsung atau ketersediaan penempatan.

| Suite ujian | Liputan |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalisasi/penolakan titik hujung, ganti penempatan, pilihan penalaran dan token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Setiap aliran kerja lengkapkan, sejarah mesej, pemangkasan giliran penuh, EOF, kegagalan |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Skema alat, argumen ditaip, aritmetik, ID, pelbagai hasil alat, susulan gagal |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Pencarian fail, UTF-8, had saiz, palet pembumian, input dan ralat API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Semua enam probe, penapis eksplisit, klasifikasi penolakan, 400 biasa dan kegagalan lain |

Untuk satu suite, gunakan `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Perlengkapan dikongsi hidup dalam [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Pengesahan Langsung Berturutan

Panggilan langsung berasingan dari ujian unit. Gunakan arahan berikut **secara berasingan**, dari akar repositori, hanya selepas kelayakan dan akses penempatan siap. Tiada perkhidmatan atau proses berterusan diperlukan.

Untuk penempatan berkongsi **10 permintaan/minit**, tempah kuota mencukupi untuk seluruh program seterusnya sebelum melancarkannya: 5, 4, 1, kemudian 6 permintaan. Proses berturutan sahaja tidak menjamin pematuhan had kadar. Selaras minit bergulir dengan semua pemanggil lain; jangan tampal keempat-empat panggilan sebagai kelompok tanpa kawalan.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Lengkapkan, pelbagai giliran, dan dua giliran interaktif:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Semak ketiga-tiga tajuk bahagian, lima jawapan, satu jawapan interaktif akhir yang mengingati Ada, `Selamat tinggal!`, dan kod keluar 0. Bajet: **5 permintaan, maksimum 1,900 token penyempurnaan**. Untuk larian lebih kecil, hanya paip `exit`: 3 permintaan / 900 token, tetapi itu tidak melibatkan inferens interaktif.

**2. Kedua-dua aliran kerja pemanggilan fungsi:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Semak kedua-dua nama fungsi, cuaca simulasi Seattle, hasil kiraan 36, dua jawapan akhir, dan kod keluar 0. Bajet: **4 permintaan, maksimum 1,200 token penyempurnaan**.

**3. Jawapan berasaskan dokumen:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Semak laluan dokumen, jawapan yang menyebut Microsoft Entra ID, dan kod keluar 0. Bajet: **1 permintaan, maksimum 500 token penyempurnaan**. Fail input wajib yang sedia ada ialah [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt). Larian kedua pilihan yang bertanya mengenai topik tiada harus menolak dan menambah satu permintaan / 500 token.

**4. Pemerhatian AI Bertanggungjawab:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Semak enam kategori dan ringkasan pemerhatian, semak kandungan yang dijana, dan memerlukan kod keluar 0 untuk penyempurnaan teknikal. Keluar proses yang berjaya tidak mengesahkan keselamatan model. Bajet: **6 permintaan, maksimum 1,800 token penyempurnaan**.

**Jumlah untuk keempat-empat arahan: 16 permintaan sembang dan paling banyak 5,400 token penyempurnaan**, dan token input (termasuk perbualan berulang dan skim/ sejarah alat). Tiada permintaan imbangan. Penggunaan token sebenar bergantung pada model dan mungkin lebih rendah, terutama untuk prompt yang ditapis. Kos dolar bergantung pada harga penghantaran; tiada anggaran wang tetap disiratkan. Semua had permintaan mengandaikan tiada larian semula manual. Periksa `$LASTEXITCODE` segera selepas setiap arahan; bukan sifar bermakna larian tidak selesai dengan jayanya.

## Penyelesaian Masalah

- **Tiada titik akhir / 401 / 403:** Tetapkan titik akhir dalam proses pelancaran, sahkan log masuk Azure tempatan dan peranan sumber beranndaskan, dan periksa sebarang ganti identiti persekitaran yang tidak disengajakan.
- **400 / 404:** Sahkan bahawa penghantaran wujud dan menyokong Sempurnaan Sembang dengan usaha penaakulan `none`. Gunakan akar sumber HTTPS atau URL `/openai/v1`, bukan URL penghantaran warisan. Ralat 400 biasa ialah kegagalan teknikal, bukan blok keselamatan.
- **429:** Koordinasi RPM kongsi dan kuota token sebelum cuba semula. Contoh tidak melakukan cuba semula automatik.
- **`Respons sembang tidak lengkap: panjang`:** Output mencapai had penyempurnaan. Semak respons dan prompt sebelum meningkatkan had dan bajet yang didokumenkan; jangan rekod larian terpotong sebagai berjaya.
- **Ralat fail atau stdin:** Lancarkan dari direktori sokongan atau berikan laluan dokumen yang jelas. Berikan soalan pembaca tidak kosong. Sempurnaan boleh berakhir normal pada EOF atau `exit`.
- **Ralat penyusunan:** Sahkan Java 21 atau lebih baru, kemudian jalankan `mvn -B -ntp clean test`. Dalam PowerShell, kutip semua argumen Maven yang mengandungi sifat bertitik, contohnya `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Langkah Seterusnya

Teruskan ke [Bab 4: Contoh Praktikal](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan oleh manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->