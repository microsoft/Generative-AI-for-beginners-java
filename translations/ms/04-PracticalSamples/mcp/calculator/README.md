<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:57:14+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "ms"
}
-->
# Tutorial Kalkulator MCP untuk Pemula

## Kandungan

- [Apa yang Anda Akan Pelajari](../../../../../04-PracticalSamples/mcp/calculator)
- [Prasyarat](../../../../../04-PracticalSamples/mcp/calculator)
- [Memahami Struktur Projek](../../../../../04-PracticalSamples/mcp/calculator)
- [Komponen Teras Dijelaskan](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Aplikasi Utama](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Perkhidmatan Kalkulator](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Klien MCP Langsung](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Klien Berkuasa AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Menjalankan Contoh](../../../../../04-PracticalSamples/mcp/calculator)
- [Bagaimana Semua Ini Berfungsi Bersama](../../../../../04-PracticalSamples/mcp/calculator)
- [Langkah Seterusnya](../../../../../04-PracticalSamples/mcp/calculator)

## Apa yang Anda Akan Pelajari

Tutorial ini menerangkan cara membina perkhidmatan kalkulator menggunakan Model Context Protocol (MCP). Anda akan memahami:

- Cara mencipta perkhidmatan yang boleh digunakan AI sebagai alat
- Cara menyediakan komunikasi langsung dengan perkhidmatan MCP
- Bagaimana model AI boleh memilih alat yang sesuai secara automatik
- Perbezaan antara panggilan protokol langsung dan interaksi yang dibantu AI

## Prasyarat

Sebelum memulakan, pastikan anda mempunyai:
- Java 21 atau lebih tinggi dipasang
- Maven untuk pengurusan kebergantungan
- Akaun GitHub dengan token akses peribadi (PAT)
- Pemahaman asas tentang Java dan Spring Boot

## Memahami Struktur Projek

Projek kalkulator ini mempunyai beberapa fail penting:

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

## Komponen Teras Dijelaskan

### 1. Aplikasi Utama

**Fail:** `McpServerApplication.java`

Ini adalah titik permulaan perkhidmatan kalkulator kita. Ia adalah aplikasi Spring Boot standard dengan satu tambahan istimewa:

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
- Memulakan pelayan web Spring Boot pada port 8080
- Mewujudkan `ToolCallbackProvider` yang menjadikan kaedah kalkulator kita tersedia sebagai alat MCP
- Anotasi `@Bean` memberitahu Spring untuk menguruskan ini sebagai komponen yang boleh digunakan oleh bahagian lain

### 2. Perkhidmatan Kalkulator

**Fail:** `CalculatorService.java`

Di sinilah semua pengiraan berlaku. Setiap kaedah ditandai dengan `@Tool` untuk menjadikannya tersedia melalui MCP:

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

**Ciri utama:**

1. **Anotasi `@Tool`**: Ini memberitahu MCP bahawa kaedah ini boleh dipanggil oleh klien luaran
2. **Penerangan Jelas**: Setiap alat mempunyai penerangan yang membantu model AI memahami bila untuk menggunakannya
3. **Format Pulangan Konsisten**: Semua operasi mengembalikan rentetan yang mudah dibaca seperti "5.00 + 3.00 = 8.00"
4. **Pengendalian Ralat**: Pembahagian dengan sifar dan punca kuasa dua nombor negatif mengembalikan mesej ralat

**Operasi Tersedia:**
- `add(a, b)` - Menambah dua nombor
- `subtract(a, b)` - Menolak nombor kedua daripada nombor pertama
- `multiply(a, b)` - Mendarab dua nombor
- `divide(a, b)` - Membahagi nombor pertama dengan nombor kedua (dengan semakan sifar)
- `power(base, exponent)` - Menaikkan asas kepada kuasa eksponen
- `squareRoot(number)` - Mengira punca kuasa dua (dengan semakan negatif)
- `modulus(a, b)` - Mengembalikan baki pembahagian
- `absolute(number)` - Mengembalikan nilai mutlak
- `help()` - Mengembalikan maklumat tentang semua operasi

### 3. Klien MCP Langsung

**Fail:** `SDKClient.java`

Klien ini berkomunikasi secara langsung dengan pelayan MCP tanpa menggunakan AI. Ia memanggil fungsi kalkulator tertentu secara manual:

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
1. **Menyambung** ke pelayan kalkulator di `http://localhost:8080`
2. **Menyenaraikan** semua alat yang tersedia (fungsi kalkulator kita)
3. **Memanggil** fungsi tertentu dengan parameter yang tepat
4. **Mencetak** hasil secara langsung

**Bila untuk menggunakannya:** Apabila anda tahu dengan tepat pengiraan yang ingin dilakukan dan mahu memanggilnya secara programatik.

### 4. Klien Berkuasa AI

**Fail:** `LangChain4jClient.java`

Klien ini menggunakan model AI (GPT-4o-mini) yang boleh memutuskan secara automatik alat kalkulator mana yang hendak digunakan:

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
1. **Mewujudkan** sambungan model AI menggunakan token GitHub anda
2. **Menyambung** AI ke pelayan MCP kalkulator kita
3. **Memberikan** AI akses kepada semua alat kalkulator kita
4. **Membolehkan** permintaan bahasa semula jadi seperti "Kira jumlah 24.5 dan 17.3"

**AI secara automatik:**
- Memahami anda ingin menambah nombor
- Memilih alat `add`
- Memanggil `add(24.5, 17.3)`
- Mengembalikan hasil dalam respons semula jadi

## Menjalankan Contoh

### Langkah 1: Mulakan Pelayan Kalkulator

Mula-mula, tetapkan token GitHub anda (diperlukan untuk klien AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Mulakan pelayan:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Pelayan akan bermula di `http://localhost:8080`. Anda sepatutnya melihat:
```
Started McpServerApplication in X.XXX seconds
```

### Langkah 2: Uji dengan Klien Langsung

Dalam terminal baru:
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

Anda akan melihat AI menggunakan alat secara automatik:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Bagaimana Semua Ini Berfungsi Bersama

Berikut adalah aliran lengkap apabila anda bertanya kepada AI "Berapakah 5 + 3?":

1. **Anda** bertanya kepada AI dalam bahasa semula jadi
2. **AI** menganalisis permintaan anda dan menyedari anda ingin menambah
3. **AI** memanggil pelayan MCP: `add(5.0, 3.0)`
4. **Perkhidmatan Kalkulator** melaksanakan: `5.0 + 3.0 = 8.0`
5. **Perkhidmatan Kalkulator** mengembalikan: `"5.00 + 3.00 = 8.00"`
6. **AI** menerima hasil dan memformat respons semula jadi
7. **Anda** mendapat: "Jumlah 5 dan 3 adalah 8"

## Langkah Seterusnya

Untuk lebih banyak contoh, lihat [Bab 04: Contoh Praktikal](../../README.md)

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk memastikan ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi kesilapan atau ketidaktepatan. Dokumen asal dalam bahasa asalnya harus dianggap sebagai sumber yang berwibawa. Untuk maklumat yang kritikal, terjemahan manusia profesional adalah disyorkan. Kami tidak bertanggungjawab atas sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.