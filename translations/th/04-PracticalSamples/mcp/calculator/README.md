<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:36:26+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "th"
}
-->
# MCP Calculator Tutorial สำหรับผู้เริ่มต้น

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../../../04-PracticalSamples/mcp/calculator)
- [ข้อกำหนดเบื้องต้น](../../../../../04-PracticalSamples/mcp/calculator)
- [ทำความเข้าใจโครงสร้างโปรเจกต์](../../../../../04-PracticalSamples/mcp/calculator)
- [อธิบายส่วนประกอบหลัก](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. แอปพลิเคชันหลัก](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. บริการเครื่องคิดเลข](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. ลูกค้า MCP โดยตรง](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. ลูกค้าที่ขับเคลื่อนด้วย AI](../../../../../04-PracticalSamples/mcp/calculator)
- [การรันตัวอย่าง](../../../../../04-PracticalSamples/mcp/calculator)
- [วิธีการทำงานร่วมกันทั้งหมด](../../../../../04-PracticalSamples/mcp/calculator)
- [ขั้นตอนถัดไป](../../../../../04-PracticalSamples/mcp/calculator)

## สิ่งที่คุณจะได้เรียนรู้

บทแนะนำนี้อธิบายวิธีสร้างบริการเครื่องคิดเลขโดยใช้ Model Context Protocol (MCP) คุณจะเข้าใจ:

- วิธีสร้างบริการที่ AI สามารถใช้เป็นเครื่องมือได้
- วิธีตั้งค่าการสื่อสารโดยตรงกับบริการ MCP
- วิธีที่โมเดล AI สามารถเลือกเครื่องมือที่จะใช้โดยอัตโนมัติ
- ความแตกต่างระหว่างการเรียกโปรโตคอลโดยตรงและการโต้ตอบที่ช่วยด้วย AI

## ข้อกำหนดเบื้องต้น

ก่อนเริ่มต้น ตรวจสอบให้แน่ใจว่าคุณมี:
- Java 21 หรือสูงกว่า
- Maven สำหรับการจัดการ dependency
- บัญชี GitHub พร้อม personal access token (PAT)
- ความเข้าใจพื้นฐานเกี่ยวกับ Java และ Spring Boot

## ทำความเข้าใจโครงสร้างโปรเจกต์

โปรเจกต์เครื่องคิดเลขมีไฟล์สำคัญหลายไฟล์:

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

## อธิบายส่วนประกอบหลัก

### 1. แอปพลิเคชันหลัก

**ไฟล์:** `McpServerApplication.java`

นี่คือจุดเริ่มต้นของบริการเครื่องคิดเลขของเรา เป็นแอปพลิเคชัน Spring Boot มาตรฐานที่มีการเพิ่มพิเศษหนึ่งอย่าง:

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

**สิ่งที่มันทำ:**
- เริ่มต้น Spring Boot web server บนพอร์ต 8080
- สร้าง `ToolCallbackProvider` ที่ทำให้วิธีการของเครื่องคิดเลขของเราพร้อมใช้งานเป็นเครื่องมือ MCP
- การใช้ `@Bean` บอก Spring ให้จัดการสิ่งนี้เป็น component ที่ส่วนอื่นสามารถใช้ได้

### 2. บริการเครื่องคิดเลข

**ไฟล์:** `CalculatorService.java`

นี่คือที่ที่การคำนวณทั้งหมดเกิดขึ้น แต่ละวิธีถูกทำเครื่องหมายด้วย `@Tool` เพื่อให้สามารถเรียกใช้ผ่าน MCP:

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

**คุณสมบัติสำคัญ:**

1. **`@Tool` Annotation**: บอก MCP ว่าวิธีนี้สามารถเรียกใช้โดยลูกค้าภายนอกได้
2. **คำอธิบายที่ชัดเจน**: แต่ละเครื่องมือมีคำอธิบายที่ช่วยให้โมเดล AI เข้าใจว่าจะใช้เมื่อใด
3. **รูปแบบการคืนค่าที่สม่ำเสมอ**: การดำเนินการทั้งหมดคืนค่าข้อความที่มนุษย์อ่านได้ เช่น "5.00 + 3.00 = 8.00"
4. **การจัดการข้อผิดพลาด**: การหารด้วยศูนย์และรากที่สองของค่าลบจะคืนข้อความแสดงข้อผิดพลาด

**การดำเนินการที่มีอยู่:**
- `add(a, b)` - บวกตัวเลขสองตัว
- `subtract(a, b)` - ลบตัวที่สองออกจากตัวแรก
- `multiply(a, b)` - คูณตัวเลขสองตัว
- `divide(a, b)` - หารตัวแรกด้วยตัวที่สอง (พร้อมตรวจสอบศูนย์)
- `power(base, exponent)` - ยกฐานเป็นกำลังของเลขชี้กำลัง
- `squareRoot(number)` - คำนวณรากที่สอง (พร้อมตรวจสอบค่าลบ)
- `modulus(a, b)` - คืนค่าผลหารที่เหลือ
- `absolute(number)` - คืนค่าค่าสัมบูรณ์
- `help()` - คืนข้อมูลเกี่ยวกับการดำเนินการทั้งหมด

### 3. ลูกค้า MCP โดยตรง

**ไฟล์:** `SDKClient.java`

ลูกค้ารายนี้พูดคุยโดยตรงกับเซิร์ฟเวอร์ MCP โดยไม่ใช้ AI มันเรียกใช้ฟังก์ชันเครื่องคิดเลขเฉพาะด้วยตนเอง:

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

**สิ่งที่มันทำ:**
1. **เชื่อมต่อ** กับเซิร์ฟเวอร์เครื่องคิดเลขที่ `http://localhost:8080`
2. **แสดงรายการ** เครื่องมือทั้งหมดที่มี (ฟังก์ชันเครื่องคิดเลขของเรา)
3. **เรียกใช้** ฟังก์ชันเฉพาะพร้อมพารามิเตอร์ที่แน่นอน
4. **พิมพ์** ผลลัพธ์โดยตรง

**เมื่อใดควรใช้:** เมื่อคุณรู้แน่ชัดว่าต้องการคำนวณอะไรและต้องการเรียกใช้โปรแกรมโดยตรง

### 4. ลูกค้าที่ขับเคลื่อนด้วย AI

**ไฟล์:** `LangChain4jClient.java`

ลูกค้ารายนี้ใช้โมเดล AI (GPT-4o-mini) ที่สามารถตัดสินใจโดยอัตโนมัติว่าจะใช้เครื่องมือเครื่องคิดเลขใด:

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

**สิ่งที่มันทำ:**
1. **สร้าง** การเชื่อมต่อโมเดล AI โดยใช้ GitHub token ของคุณ
2. **เชื่อมต่อ** AI กับเซิร์ฟเวอร์ MCP เครื่องคิดเลขของเรา
3. **ให้** AI เข้าถึงเครื่องมือเครื่องคิดเลขทั้งหมดของเรา
4. **อนุญาต** คำขอในภาษาธรรมชาติ เช่น "คำนวณผลรวมของ 24.5 และ 17.3"

**AI ทำงานโดยอัตโนมัติ:**
- เข้าใจว่าคุณต้องการบวกตัวเลข
- เลือกเครื่องมือ `add`
- เรียกใช้ `add(24.5, 17.3)`
- คืนผลลัพธ์ในรูปแบบการตอบสนองธรรมชาติ

## การรันตัวอย่าง

### ขั้นตอนที่ 1: เริ่มเซิร์ฟเวอร์เครื่องคิดเลข

ก่อนอื่น ตั้งค่า GitHub token ของคุณ (จำเป็นสำหรับลูกค้า AI):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

เริ่มเซิร์ฟเวอร์:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

เซิร์ฟเวอร์จะเริ่มต้นที่ `http://localhost:8080` คุณควรเห็น:
```
Started McpServerApplication in X.XXX seconds
```

### ขั้นตอนที่ 2: ทดสอบด้วยลูกค้าโดยตรง

ใน terminal ใหม่:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

คุณจะเห็นผลลัพธ์เช่น:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### ขั้นตอนที่ 3: ทดสอบด้วยลูกค้า AI

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

คุณจะเห็น AI ใช้เครื่องมือโดยอัตโนมัติ:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## วิธีการทำงานร่วมกันทั้งหมด

นี่คือกระบวนการทั้งหมดเมื่อคุณถาม AI ว่า "5 + 3 เท่ากับเท่าไหร่?":

1. **คุณ** ถาม AI ในภาษาธรรมชาติ
2. **AI** วิเคราะห์คำขอของคุณและเข้าใจว่าคุณต้องการบวก
3. **AI** เรียกเซิร์ฟเวอร์ MCP: `add(5.0, 3.0)`
4. **บริการเครื่องคิดเลข** ดำเนินการ: `5.0 + 3.0 = 8.0`
5. **บริการเครื่องคิดเลข** คืนค่า: `"5.00 + 3.00 = 8.00"`
6. **AI** รับผลลัพธ์และจัดรูปแบบการตอบสนองธรรมชาติ
7. **คุณ** ได้รับ: "ผลรวมของ 5 และ 3 คือ 8"

## ขั้นตอนถัดไป

สำหรับตัวอย่างเพิ่มเติม ดู [Chapter 04: Practical samples](../../README.md)

**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้การแปลมีความถูกต้อง แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลภาษามืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดซึ่งเกิดจากการใช้การแปลนี้