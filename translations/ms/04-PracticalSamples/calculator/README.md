# Tutorial Kalkulator MCP untuk Pemula

## Jadual Kandungan

- [Apa yang Anda Akan Pelajari](#apa-yang-anda-akan-pelajari)
- [Prasyarat](#prasyarat)
- [Versi Pergantungan](#versi-pergantungan)
- [Memahami Struktur Projek](#memahami-struktur-projek)
- [Komponen Utama Dijelaskan](#komponen-utama-dijelaskan)
  - [1. Aplikasi Utama](#1-aplikasi-utama)
  - [2. Perkhidmatan Kalkulator](#2-perkhidmatan-kalkulator)
  - [3. Klien MCP Langsung](#3-klien-mcp-langsung)
  - [4. Klien Berkuasa AI](#4-klien-berkuasa-ai)
- [Menjalankan Contoh](#menjalankan-contoh)
- [Ujian Luar Talian](#ujian-luar-talian)
- [Bagaimana Ia Bekerja Bersama](#bagaimana-ia-bekerja-bersama)
- [Langkah Seterusnya](#langkah-seterusnya)

## Apa yang Anda Akan Pelajari

Tutorial ini menerangkan bagaimana membina perkhidmatan kalkulator menggunakan Protokol Konteks Model (MCP). Anda akan memahami:

- Cara membuat perkhidmatan yang boleh digunakan AI sebagai alat
- Cara menyediakan komunikasi langsung dengan perkhidmatan MCP
- Bagaimana model AI boleh secara automatik memilih alat yang digunakan
- Perbezaan antara panggilan protokol langsung dan interaksi bantu AI

## Prasyarat

Sebelum bermula, pastikan anda mempunyai:
- Java 21 atau lebih tinggi dipasang
- Maven untuk pengurusan pergantungan
- Pemahaman asas tentang Java dan Spring Boot

Hanya klien AI memerlukan penempatan Azure OpenAI dan `DefaultAzureCredential` yang diautentikasi,
seperti log masuk Azure CLI sedia ada secara tempatan atau identiti terurus di Azure. Identiti tersebut perlu
peranan Pengguna Cognitive Services OpenAI pada sumber tersebut. Lihat [Bab 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Pelayan, klien SDK langsung, dan semua ujian automatik tidak memerlukan akaun Azure atau akses model.

## Versi Pergantungan

Pergantungan keluaran yang disahkan pada 2026-09-14:

| Pergantungan | Versi |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (diuruskan oleh Spring AI) | 2.0.0 |
| LangChain4j / teras | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Penyesuai OpenAI rasmi LangChain4j | 1.20.0-beta30 |
| SDK Java OpenAI | 4.63.1 |
| Identiti Azure | 1.18.6 |
| JUnit Jupiter (Diuruskan Boot) | 6.0.3 |

Penyesuai MCP dan OpenAI rasmi adalah keluaran beta yang diterbitkan di Maven Central, bukan snapshot.
Versi mereka berbeza dari LangChain4j teras. Tiada repositori snapshot atau mileston diperlukan.
Pergantungan hanya klien mempunyai skop ujian kerana contoh yang boleh dijalankan berada di bawah `src/test/java`.

## Memahami Struktur Projek

Projek kalkulator mempunyai beberapa fail penting:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Komponen Utama Dijelaskan

### 1. Aplikasi Utama

**Fail:** `McpServerApplication.java`

Ini adalah titik masuk perkhidmatan kalkulator kita. Ia adalah aplikasi Spring Boot biasa dengan satu tambahan istimewa:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Apa yang dilakukan ini:**
- Memulakan pelayan web Spring Boot pada port 8080
- Membuat `ToolCallbackProvider` yang menjadikan kaedah kalkulator tersedia sebagai alat MCP
- Anotasi `@Bean` memberitahu Spring untuk menguruskan ini sebagai komponen yang bahagian lain boleh gunakan

### 2. Perkhidmatan Kalkulator

**Fail:** `CalculatorService.java`

Di sini semua pengiraan berlaku. Setiap kaedah ditandai dengan `@Tool` untuk menjadikannya tersedia melalui MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Lebih banyak operasi kalkulator...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Ciri utama:**

1. **Anotasi `@Tool`**: Ini memberitahu MCP bahawa kaedah ini boleh dipanggil oleh klien luaran
2. **Penerangan Jelas**: Setiap alat mempunyai penerangan yang membantu model AI memahami bila menggunakannya
3. **Format Pulangan Konsisten**: Semua operasi memulangkan rentetan yang mudah dibaca seperti "5.00 + 3.00 = 8.00"
4. **Pengendalian Ralat**: Pembahagian dengan sifar dan punca kuasa negatif memulangkan mesej ralat

**Operasi Tersedia:**
- `add(a, b)` - Menambah dua nombor
- `subtract(a, b)` - Menolak nombor kedua dari pertama
- `multiply(a, b)` - Mendarab dua nombor
- `divide(a, b)` - Membahagi nombor pertama dengan kedua (dengan pemeriksaan sifar)
- `power(base, exponent)` - Menaikkan asas kepada kuasa punca
- `squareRoot(number)` - Mengira punca kuasa dua (dengan pemeriksaan negatif)
- `modulus(a, b)` - Memulangkan baki pembahagian
- `absolute(number)` - Memulangkan nilai mutlak
- `help()` - Memulangkan maklumat mengenai semua operasi

### 3. Klien MCP Langsung

Lihat [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Klien ini menggunakan `HttpClientStreamableHttpTransport` di `/mcp`, memulakan sambungan,
menghantar ping ke pelayan, dan mengikuti paginasi senarai alat. Ia memeriksa bahawa kesemua sembilan alat yang dijangkakan
wujud dan memanggil setiap satu daripada mereka, termasuk `modulus` dan `help`, tanpa model AI.

Pembina permintaan semasa adalah seperti berikut:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Ralat protokol menyebabkan klien gagal dan bukannya mencetak kejayaan yang mengelirukan. Klien MCP
ditutup dengan try-with-resources, termasuk apabila penemuan atau panggilan alat gagal.

### 4. Klien Berkuasa AI

Lihat [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
dan [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` melaksanakan API LangChain4j `ChatModel` semasa.
`StreamableHttpMcpTransport` menyambungkannya ke titik akhir `/mcp` yang sama seperti klien SDK.
`AiServices` menemui alat dan mengurus perbualan panggilan alat/hasil.

Penempatan lalai ialah **GPT-5.6 Luna**, dengan penaakulan dilumpuhkan secara eksplisit:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Lalai ini terpakai pada setiap pelengkap, termasuk susulan selepas pelaksanaan alat.
Klien menggunakan `BearerTokenCredential` yang boleh disegarkan disokong oleh `DefaultAzureCredential`
dan skop `https://ai.azure.com/.default`, bukan token sekali lalu yang disampaikan sebagai kunci API.
URL sumber dan URL yang sudah berakhir dengan `/openai/v1` diterima.

Bot menyimpan sejarah perbualan terbatas, mencetak `Tool executed: ...` dengan hasil
MCP sebenar, dan gagal jika respons melangkau alat. Gelung alat hadkan kepada empat pusingan.
Ralat pengesahan, model, MCP, dan alat disebarkan; percubaan model automatik dilumpuhkan.
Kedua-dua pengangkutan/klien MCP dan klien OpenAI rasmi ditutup pada kejayaan atau kegagalan.

## Menjalankan Contoh

### Langkah 1: Mulakan Pelayan Kalkulator

Tiada konfigurasi Azure diperlukan untuk pelayan. Arahan di bawah dijalankan dari direktori sampel ini.
Contoh menggunakan port **18081** untuk mengelakkan pertembungan dengan sampel lain; lalai tetap 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Titik akhir MCP ialah `http://localhost:18081/mcp`. Maklumat kesihatan dan penemuan terdapat di
`http://localhost:18081/health` dan `http://localhost:18081/info`.
HTTP Streamable menggantikan pengangkutan lama hanya SSE; `/sse` dan `/v1/tools` bukan titik akhir.

### Langkah 2: Uji dengan Klien Langsung

Dalam terminal PowerShell lain:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Tiada input diperlukan. Kesemua sembilan alat diuji. Keputusan aritmetik dijangka termasuk
8, 6, 42, 5, 256, 4, 2, dan 5.5, diikuti dengan teks bantuan.

### Langkah 3: Uji dengan Klien AI

Selepas mengautentikasi seperti yang diterangkan dalam prasyarat, konfigurasi klien AI dalam terminal yang sama:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Jangka baris `Tool executed: add` dengan `41.80`, diikuti dengan jawapan model.
Mod mod permintaan tunggal keluar tanpa menunggu input. Untuk menjalankan demo empat permintaan asal:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo memanggil `add`, `squareRoot`, `help`, dan operasi `power` kemudian `divide` berantai.
Jawapan berangka dijangka adalah 41.8, 12, dan 64. Mengabaikan argumen juga menjalankan demo ini.

### Langkah 4: Jalankan Bot Interaktif

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Masukkan `Multiply 6 by 7 using the calculator service`, kemudian `exit` atau `quit`.
Jangka keputusan alat `multiply` sebenar adalah 42. Baris kosong diabaikan; EOF juga menamatkan sesi.
Untuk ujian asap tidak interaktif bagi titik masuk ini:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Kedua-dua titik masuk AI menerima `--prompt "question"`, `--demo`, dan `--interactive`.
Pilihan tidak sah gagal sebelum membuka sambungan. Setiap argumen Maven `-D...` disitara penuh
untuk PowerShell. Pada Bash, gunakan `export NAME=value` menggantikan `$env:NAME = "value"`.

**Kuota:** Jalankan sampel AI secara berurutan. Satu permintaan ringkas biasanya memerlukan dua permintaan model;
demo lengkap biasanya memerlukan sembilan, termasuk susulan hasil alat. Dengan penempatan 10 RPM dikongsi,
benarkan tetingkap kuota segar sebelum larian AI seterusnya. 429 gagal jelas tanpa
percubaan semula automatik; ikut panduan retry-after perkhidmatan. Bilangan permintaan sebenar bergantung pada model.
Ujian luar talian tidak menggunakan kuota dan tidak mengesahkan ketersediaan Luna atau kualiti jawapan secara langsung.

### Konfigurasi dan Penutupan

| Tetapan | Lalai / kelakuan |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL asas, tanpa `/mcp` |
| `-Dmcp.server.url=...` | Gantikan `MCP_SERVER_URL` untuk semua klien |
| `AZURE_OPENAI_ENDPOINT` | Diperlukan hanya untuk klien AI; URL sumber atau URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nama penempatan Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; integer positif |
| Usaha penaakulan | Sentiasa `none`, termasuk susulan gelung alat |

Penempatan yang digantikan mesti menyokong `reasoning_effort=none` dan `max_completion_tokens`.
Klien tidak membaca fail `.env` secara automatik. Hentikan pelayan dengan `Ctrl+C` selepas ujian.
Klien kembali secara normal tanpa `System.exit` atau tidur penutupan.

## Ujian Luar Talian

```powershell
mvn -B -ntp clean verify
```

Semua ujian adalah luar talian terhadap Azure: suite protokol bermula pelayan Spring dan
stub serasi OpenAI pada port loopback rawak, kemudian menutupnya. Maven mungkin masih perlu
memuat turun pergantungan. Tiada kelayakan, penempatan langsung, atau pelayan MCP sedia ada digunakan.

- Ujian unit kalkulator meliputi semua operasi aritmetik, hasil perpuluhan, bantuan, dan ralat domain.
- Ujian MCP meliputi inisialisasi, penemuan, sembilan panggilan alat, kegagalan alat, dan kesihatan/maklumat.
- Ujian protokol AI melaksanakan demo penuh dan Bot interaktif terhadap kalkulator sebenar,
  mengesahkan hasil alat memberi makan pelengkap seterusnya, dan memeriksa setiap badan HTTP untuk Luna,
  `reasoning_effort: "none"`, dan `max_completion_tokens` tanpa `max_tokens` lama.
- Ujian konfigurasi/input meliputi penggantian penempatan dan titik akhir, baris kosong, EOF, exit/quit,
  mod permintaan tunggal, pilihan tidak sah, dan penyebaran ralat. Ujian kuota membuktikan 429 tidak dicuba semula.

## Bagaimana Ia Bekerja Bersama

Berikut aliran lengkap apabila anda bertanya kepada AI "Berapakah 5 + 3?":

1. **Anda** bertanya kepada AI dalam bahasa semula jadi
2. **AI** menganalisis permintaan anda dan menyedari anda mahu penambahan
3. **AI** memanggil pelayan MCP: `add(5.0, 3.0)`
4. **Perkhidmatan Kalkulator** melaksanakan: `5.0 + 3.0 = 8.0`
5. **Perkhidmatan Kalkulator** memulangkan: `"5.00 + 3.00 = 8.00"`
6. **AI** menerima hasil dan memformat respons semula jadi
7. **Anda** mendapat: "Jumlah 5 dan 3 adalah 8"

## Langkah Seterusnya

Untuk lebih banyak contoh, lihat [Bab 04: Contoh praktikal](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil maklum bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang sahih. Untuk maklumat penting, terjemahan oleh manusia profesional adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->