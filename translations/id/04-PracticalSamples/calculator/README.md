# Tutorial Kalkulator MCP untuk Pemula

## Daftar Isi

- [Apa yang Akan Anda Pelajari](#apa-yang-akan-anda-pelajari)
- [Prasyarat](#prasyarat)
- [Versi Ketergantungan](#versi-ketergantungan)
- [Memahami Struktur Proyek](#memahami-struktur-proyek)
- [Penjelasan Komponen Inti](#penjelasan-komponen-inti)
  - [1. Aplikasi Utama](#1-aplikasi-utama)
  - [2. Layanan Kalkulator](#2-layanan-kalkulator)
  - [3. Klien MCP Langsung](#3-klien-mcp-langsung)
  - [4. Klien Berbasis AI](#4-klien-berbasis-ai)
- [Menjalankan Contoh](#menjalankan-contoh)
- [Pengujian Offline](#pengujian-offline)
- [Bagaimana Semua Bekerja Bersama](#bagaimana-semua-bekerja-bersama)
- [Langkah Selanjutnya](#langkah-selanjutnya)

## Apa yang Akan Anda Pelajari

Tutorial ini menjelaskan cara membangun layanan kalkulator menggunakan Model Context Protocol (MCP). Anda akan mengerti:

- Cara membuat layanan yang dapat digunakan AI sebagai alat
- Cara mengatur komunikasi langsung dengan layanan MCP
- Bagaimana model AI secara otomatis memilih alat yang akan digunakan
- Perbedaan antara panggilan protokol langsung dan interaksi yang dibantu AI

## Prasyarat

Sebelum memulai, pastikan Anda memiliki:
- Java 21 atau yang lebih tinggi terpasang
- Maven untuk manajemen ketergantungan
- Pemahaman dasar tentang Java dan Spring Boot

Hanya klien AI yang membutuhkan penyebaran Azure OpenAI dan `DefaultAzureCredential` yang terotentikasi,
seperti masuk Azure CLI yang sudah ada secara lokal atau identitas terkelola di Azure. Identitas tersebut membutuhkan
peran Cognitive Services OpenAI User pada sumber daya. Lihat [Bab 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Server, klien SDK langsung, dan semua pengujian otomatis tidak memerlukan akun Azure atau akses model.

## Versi Ketergantungan

Ketergantungan rilis yang diverifikasi pada 14-09-2026:

| Ketergantungan | Versi |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (dikelola Spring AI) | 2.0.0 |
| LangChain4j / inti | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Adaptor OpenAI resmi LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (dikelola Boot) | 6.0.3 |

Adaptor MCP dan OpenAI resmi adalah rilis beta yang diterbitkan di Maven Central, bukan snapshot.
Versi mereka berbeda dari inti LangChain4j. Tidak dibutuhkan repositori snapshot atau milestone.
Ketergantungan hanya klien berskala uji karena contoh yang dapat dijalankan berada di bawah `src/test/java`.

## Memahami Struktur Proyek

Proyek kalkulator memiliki beberapa file penting:

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

## Penjelasan Komponen Inti

### 1. Aplikasi Utama

**File:** `McpServerApplication.java`

Ini adalah titik masuk dari layanan kalkulator kami. Ini adalah aplikasi Spring Boot standar dengan satu tambahan khusus:

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
- Memulai server web Spring Boot pada port 8080
- Membuat `ToolCallbackProvider` yang membuat metode kalkulator kami tersedia sebagai alat MCP
- Anotasi `@Bean` memberitahu Spring untuk mengelola ini sebagai komponen yang dapat digunakan bagian lain

### 2. Layanan Kalkulator

**File:** `CalculatorService.java`

Di sinilah semua matematika terjadi. Setiap metode ditandai dengan `@Tool` agar dapat diakses melalui MCP:

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

**Fitur utama:**

1. **Anotasi `@Tool`**: Ini memberitahu MCP bahwa metode ini dapat dipanggil oleh klien eksternal
2. **Deskripsi yang Jelas**: Setiap alat memiliki deskripsi yang membantu model AI memahami kapan harus menggunakan
3. **Format Kembalian Konsisten**: Semua operasi mengembalikan string yang mudah dibaca manusia seperti "5.00 + 3.00 = 8.00"
4. **Penanganan Kesalahan**: Pembagian dengan nol dan akar kuadrat negatif mengembalikan pesan kesalahan

**Operasi yang Tersedia:**
- `add(a, b)` - Menambahkan dua angka
- `subtract(a, b)` - Mengurangkan angka kedua dari yang pertama
- `multiply(a, b)` - Mengalikan dua angka
- `divide(a, b)` - Membagi angka pertama dengan kedua (dengan pemeriksaan nol)
- `power(base, exponent)` - Menaikkan basis ke pangkat eksponen
- `squareRoot(number)` - Menghitung akar kuadrat (dengan pemeriksaan negatif)
- `modulus(a, b)` - Mengembalikan sisa pembagian
- `absolute(number)` - Mengembalikan nilai mutlak
- `help()` - Mengembalikan informasi tentang semua operasi

### 3. Klien MCP Langsung

Lihat [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Klien ini menggunakan `HttpClientStreamableHttpTransport` pada `/mcp`, menginisialisasi koneksi,
mengirim ping ke server, dan mengikuti paginasi daftar alat. Ia memeriksa bahwa semua sembilan alat yang diharapkan
ada dan memanggil semuanya, termasuk `modulus` dan `help`, tanpa model AI.

Builder permintaan saat ini terlihat seperti ini:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Kesalahan protokol menyebabkan klien gagal daripada mencetak keberhasilan yang menyesatkan. Klien MCP
ditutup dengan try-with-resources, termasuk saat penemuan atau panggilan alat gagal.

### 4. Klien Berbasis AI

Lihat [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
dan [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` mengimplementasikan API `ChatModel` LangChain4j saat ini.
`StreamableHttpMcpTransport` menghubungkannya ke titik akhir `/mcp` yang sama dengan klien SDK.
`AiServices` menemukan alat dan mengelola percakapan panggilan/hasil alat.

Penyebaran default adalah **GPT-5.6 Luna**, dengan pemikiran dinonaktifkan secara eksplisit:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Default ini berlaku untuk setiap penyelesaian, termasuk tindak lanjut setelah eksekusi alat.
Klien menggunakan `BearerTokenCredential` yang dapat diperbarui didukung oleh `DefaultAzureCredential`
dan cakupan `https://ai.azure.com/.default`, bukan token satu kali yang diteruskan sebagai kunci API.
URL sumber daya dan URL yang sudah berakhiran `/openai/v1` keduanya diterima.

Bot menyimpan riwayat percakapan terbatas, mencetak `Tool executed: ...` dengan hasil MCP aktual,
dan gagal jika respons melewati alat. Loop alat dibatasi sampai empat putaran.
Kesalahan otentikasi, model, MCP, dan alat diteruskan; percobaan ulang model otomatis dinonaktifkan.
Baik transport/klien MCP maupun klien OpenAI resmi ditutup saat berhasil atau gagal.

## Menjalankan Contoh

### Langkah 1: Mulai Server Kalkulator

Tidak diperlukan konfigurasi Azure untuk server. Perintah di bawah ini dijalankan dari direktori contoh ini.
Contoh menggunakan port **18081** untuk menghindari bentrok dengan contoh lain; default tetap 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Titik akhir MCP adalah `http://localhost:18081/mcp`. Informasi kesehatan dan penemuan ada di
`http://localhost:18081/health` dan `http://localhost:18081/info`.
HTTP streamable menggantikan transport SSE-only lama; `/sse` dan `/v1/tools` bukan titik akhir.

### Langkah 2: Uji dengan Klien Langsung

Di terminal PowerShell lain:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Tidak diperlukan input. Semua sembilan alat diuji. Hasil aritmatika yang diharapkan termasuk
8, 6, 42, 5, 256, 4, 2, dan 5.5, diikuti oleh teks bantuan.

### Langkah 3: Uji dengan Klien AI

Setelah otentikasi seperti dijelaskan dalam prasyarat, konfigurasikan klien AI di terminal yang sama:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Harapkan baris `Tool executed: add` dengan `41.80`, diikuti jawaban model.
Mode single-prompt keluar tanpa menunggu input. Untuk menjalankan demo original empat-prompt:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo memanggil operasi `add`, `squareRoot`, `help`, dan rantai operasi `power` lalu `divide`.
Jawaban numerik yang diharapkan adalah 41.8, 12, dan 64. Melewatkan argumen juga menjalankan demo ini.

### Langkah 4: Jalankan Bot Interaktif

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Masukkan `Multiply 6 by 7 using the calculator service`, lalu `exit` atau `quit`.
Harapkan hasil alat `multiply` yang sebenarnya yaitu 42. Baris kosong diabaikan; EOF juga mengakhiri sesi.
Untuk tes smoke noninteraktif dari titik masuk ini:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Kedua titik masuk AI menerima `--prompt "question"`, `--demo`, dan `--interactive`.
Opsi tidak valid gagal sebelum membuka koneksi. Setiap argumen Maven `-D...` dikutip penuh
untuk PowerShell. Pada Bash, gunakan `export NAME=value` bukan `$env:NAME = "value"`.

**Kuota:** Jalankan contoh AI secara berurutan. Satu prompt biasanya membutuhkan dua permintaan model;
demo lengkap biasanya membutuhkan sembilan, termasuk tindak lanjut hasil alat. Dengan penyebaran 10 RPM bersama,
berikan jendela kuota baru sebelum menjalankan AI berikutnya. Kode 429 gagal terlihat tanpa
percobaan ulang otomatis; ikuti panduan retry-after layanan. Jumlah permintaan aktual tergantung model.
Pengujian offline tidak menghabiskan kuota dan tidak menetapkan ketersediaan Luna langsung atau kualitas jawaban.

### Konfigurasi dan Shutdown

| Pengaturan | Default / perilaku |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL dasar, tanpa `/mcp` |
| `-Dmcp.server.url=...` | Menimpa `MCP_SERVER_URL` untuk semua klien |
| `AZURE_OPENAI_ENDPOINT` | Hanya diperlukan untuk klien AI; URL sumber daya atau URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nama penyebaran Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; bilangan bulat positif |
| Usaha pemikiran | Selalu `none`, termasuk tindak lanjut loop alat |

Penyebaran yang ditimpa harus mendukung `reasoning_effort=none` dan `max_completion_tokens`.
Klien tidak membaca file `.env` secara otomatis. Hentikan server dengan `Ctrl+C` setelah pengujian.
Klien kembali normal tanpa `System.exit` atau penundaan shutdown.

## Pengujian Offline

```powershell
mvn -B -ntp clean verify
```

Semua pengujian bersifat offline terhadap Azure: suite protokol memulai server Spring dan
stub kompatibel OpenAI pada port loopback acak, lalu menutupnya. Maven mungkin masih perlu
mengunduh ketergantungan. Tidak digunakan kredensial, penyebaran live, atau server MCP yang sudah ada.

- Pengujian unit kalkulator mencakup semua operasi aritmatika, hasil desimal, bantuan, dan kesalahan domain.
- Pengujian MCP mencakup inisialisasi, penemuan, semua sembilan panggilan alat, kegagalan alat, dan kesehatan/info.
- Pengujian protokol AI menjalankan demo lengkap dan Bot interaktif terhadap kalkulator nyata,
  memverifikasi hasil alat memberi makan penyelesaian berikutnya, dan memeriksa setiap badan HTTP untuk Luna,
  `reasoning_effort: "none"`, dan `max_completion_tokens` tanpa `max_tokens` warisan.
- Pengujian konfigurasi/input mencakup penimpa penyebaran dan endpoint, baris kosong, EOF, keluar/quit,
  mode single-prompt, opsi tidak valid, dan propagasi kesalahan. Pengujian kuota membuktikan 429 tidak dicoba ulang.

## Bagaimana Semua Bekerja Bersama

Berikut alur lengkap saat Anda bertanya kepada AI "Berapa 5 + 3?":

1. **Anda** bertanya kepada AI dalam bahasa alami
2. **AI** menganalisis permintaan Anda dan menyadari Anda ingin penjumlahan
3. **AI** memanggil server MCP: `add(5.0, 3.0)`
4. **Layanan Kalkulator** melakukan: `5.0 + 3.0 = 8.0`
5. **Layanan Kalkulator** mengembalikan: `"5.00 + 3.00 = 8.00"`
6. **AI** menerima hasil dan memformat respons alami
7. **Anda** mendapatkan: "Jumlah 5 dan 3 adalah 8"

## Langkah Selanjutnya

Untuk contoh lebih lanjut, lihat [Bab 04: Contoh praktis](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Penafian**:
Dokumen ini telah diterjemahkan menggunakan layanan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk mencapai akurasi, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang sah. Untuk informasi penting, disarankan menggunakan terjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->