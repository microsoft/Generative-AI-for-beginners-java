<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:55:14+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "id"
}
-->
# Tutorial MCP Calculator untuk Pemula

## Daftar Isi

- [Apa yang Akan Anda Pelajari](../../../../../04-PracticalSamples/mcp/calculator)
- [Prasyarat](../../../../../04-PracticalSamples/mcp/calculator)
- [Memahami Struktur Proyek](../../../../../04-PracticalSamples/mcp/calculator)
- [Penjelasan Komponen Inti](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Aplikasi Utama](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Layanan Kalkulator](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Klien MCP Langsung](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Klien Berbasis AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Menjalankan Contoh](../../../../../04-PracticalSamples/mcp/calculator)
- [Bagaimana Semua Ini Bekerja Bersama](../../../../../04-PracticalSamples/mcp/calculator)
- [Langkah Selanjutnya](../../../../../04-PracticalSamples/mcp/calculator)

## Apa yang Akan Anda Pelajari

Tutorial ini menjelaskan cara membangun layanan kalkulator menggunakan Model Context Protocol (MCP). Anda akan memahami:

- Cara membuat layanan yang dapat digunakan AI sebagai alat
- Cara mengatur komunikasi langsung dengan layanan MCP
- Bagaimana model AI dapat secara otomatis memilih alat yang akan digunakan
- Perbedaan antara panggilan protokol langsung dan interaksi yang dibantu AI

## Prasyarat

Sebelum memulai, pastikan Anda memiliki:
- Java 21 atau versi lebih tinggi terinstal
- Maven untuk manajemen dependensi
- Akun GitHub dengan personal access token (PAT)
- Pemahaman dasar tentang Java dan Spring Boot

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
    └── Bot.java                          # Simple chat interface
```

## Penjelasan Komponen Inti

### 1. Aplikasi Utama

**File:** `McpServerApplication.java`

Ini adalah titik masuk untuk layanan kalkulator kita. Ini adalah aplikasi Spring Boot standar dengan satu tambahan khusus:

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

**Apa yang dilakukan:**
- Memulai server web Spring Boot pada port 8080
- Membuat `ToolCallbackProvider` yang membuat metode kalkulator kita tersedia sebagai alat MCP
- Anotasi `@Bean` memberi tahu Spring untuk mengelola ini sebagai komponen yang dapat digunakan oleh bagian lain

### 2. Layanan Kalkulator

**File:** `CalculatorService.java`

Di sinilah semua perhitungan matematika dilakukan. Setiap metode diberi tanda `@Tool` agar tersedia melalui MCP:

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Fitur utama:**

1. **Anotasi `@Tool`**: Memberi tahu MCP bahwa metode ini dapat dipanggil oleh klien eksternal
2. **Deskripsi Jelas**: Setiap alat memiliki deskripsi yang membantu model AI memahami kapan harus menggunakannya
3. **Format Pengembalian Konsisten**: Semua operasi mengembalikan string yang mudah dibaca seperti "5.00 + 3.00 = 8.00"
4. **Penanganan Kesalahan**: Pembagian dengan nol dan akar kuadrat negatif mengembalikan pesan kesalahan

**Operasi yang Tersedia:**
- `add(a, b)` - Menjumlahkan dua angka
- `subtract(a, b)` - Mengurangi angka kedua dari angka pertama
- `multiply(a, b)` - Mengalikan dua angka
- `divide(a, b)` - Membagi angka pertama dengan angka kedua (dengan pengecekan nol)
- `power(base, exponent)` - Menghitung pangkat dari basis
- `squareRoot(number)` - Menghitung akar kuadrat (dengan pengecekan negatif)
- `modulus(a, b)` - Mengembalikan sisa pembagian
- `absolute(number)` - Mengembalikan nilai absolut
- `help()` - Mengembalikan informasi tentang semua operasi

### 3. Klien MCP Langsung

**File:** `SDKClient.java`

Klien ini berkomunikasi langsung dengan server MCP tanpa menggunakan AI. Klien ini secara manual memanggil fungsi kalkulator tertentu:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Apa yang dilakukan:**
1. **Terhubung** ke server kalkulator di `http://localhost:8080`
2. **Mendaftar** semua alat yang tersedia (fungsi kalkulator kita)
3. **Memanggil** fungsi tertentu dengan parameter yang tepat
4. **Mencetak** hasil secara langsung

**Kapan digunakan:** Ketika Anda tahu persis perhitungan yang ingin dilakukan dan ingin memanggilnya secara programatis.

### 4. Klien Berbasis AI

**File:** `LangChain4jClient.java`

Klien ini menggunakan model AI (GPT-4o-mini) yang dapat secara otomatis memutuskan alat kalkulator mana yang akan digunakan:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Apa yang dilakukan:**
1. **Membuat** koneksi model AI menggunakan token GitHub Anda
2. **Terhubung** AI ke server MCP kalkulator kita
3. **Memberikan** akses AI ke semua alat kalkulator kita
4. **Memungkinkan** permintaan bahasa alami seperti "Hitung jumlah 24.5 dan 17.3"

**AI secara otomatis:**
- Memahami bahwa Anda ingin menjumlahkan angka
- Memilih alat `add`
- Memanggil `add(24.5, 17.3)`
- Mengembalikan hasil dalam respons alami

## Menjalankan Contoh

### Langkah 1: Memulai Server Kalkulator

Pertama, atur token GitHub Anda (diperlukan untuk klien AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Mulai server:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Server akan dimulai di `http://localhost:8080`. Anda akan melihat:
```
Started McpServerApplication in X.XXX seconds
```

### Langkah 2: Uji dengan Klien Langsung

Di terminal baru:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Anda akan melihat output seperti:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Langkah 3: Uji dengan Klien AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Anda akan melihat AI secara otomatis menggunakan alat:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Bagaimana Semua Ini Bekerja Bersama

Berikut alur lengkap ketika Anda bertanya kepada AI "Berapa 5 + 3?":

1. **Anda** bertanya kepada AI dalam bahasa alami
2. **AI** menganalisis permintaan Anda dan menyadari Anda ingin melakukan penjumlahan
3. **AI** memanggil server MCP: `add(5.0, 3.0)`
4. **Layanan Kalkulator** melakukan: `5.0 + 3.0 = 8.0`
5. **Layanan Kalkulator** mengembalikan: `"5.00 + 3.00 = 8.00"`
6. **AI** menerima hasil dan memformat respons alami
7. **Anda** mendapatkan: "Jumlah dari 5 dan 3 adalah 8"

## Langkah Selanjutnya

Untuk lebih banyak contoh, lihat [Bab 04: Contoh Praktis](../../README.md)

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berupaya untuk memberikan hasil yang akurat, harap diperhatikan bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang berwenang. Untuk informasi yang bersifat kritis, disarankan menggunakan jasa penerjemahan manusia profesional. Kami tidak bertanggung jawab atas kesalahpahaman atau interpretasi yang keliru yang timbul dari penggunaan terjemahan ini.