# คู่มือการใช้ MCP Calculator สำหรับผู้เริ่มต้น

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](#สิ่งที่คุณจะได้เรียนรู้)
- [ความรู้พื้นฐานที่ต้องมี](#ความรู้พื้นฐานที่ต้องมี)
- [เวอร์ชันของไลบรารีที่ใช้](#เวอร์ชันของไลบรารีที่ใช้)
- [เข้าใจโครงสร้างโปรเจกต์](#เข้าใจโครงสร้างโปรเจกต์)
- [อธิบายคอมโพเนนต์หลัก](#อธิบายคอมโพเนนต์หลัก)
  - [1. แอปพลิเคชันหลัก](#1-แอปพลิเคชันหลัก)
  - [2. บริการเครื่องคิดเลข](#2-บริการเครื่องคิดเลข)
  - [3. ลูกค้า MCP แบบตรง](#3-ลูกค้า-mcp-แบบตรง)
  - [4. ลูกค้าที่ใช้ AI](#4-ลูกค้าที่ใช้-ai)
- [การรันตัวอย่าง](#การรันตัวอย่าง)
- [การทดสอบแบบออฟไลน์](#การทดสอบแบบออฟไลน์)
- [การทำงานร่วมกันทั้งหมด](#การทำงานร่วมกันทั้งหมด)
- [ขั้นตอนถัดไป](#ขั้นตอนถัดไป)

## สิ่งที่คุณจะได้เรียนรู้

คู่มือนี้อธิบายวิธีสร้างบริการเครื่องคิดเลขโดยใช้ Model Context Protocol (MCP) คุณจะเข้าใจว่า:

- วิธีสร้างบริการที่ AI สามารถใช้เป็นเครื่องมือ
- วิธีตั้งค่าการสื่อสารตรงกับบริการ MCP
- วิธีที่โมเดล AI เลือกเครื่องมือใช้งานโดยอัตโนมัติ
- ความแตกต่างระหว่างการเรียกโปรโตคอลโดยตรงและการโต้ตอบโดยใช้ AI ช่วย

## ความรู้พื้นฐานที่ต้องมี

ก่อนเริ่มต้น โปรดตรวจสอบว่าคุณมี:
- ติดตั้ง Java 21 หรือสูงกว่า
- Maven สำหรับการจัดการ dependencies
- ความเข้าใจพื้นฐานเกี่ยวกับ Java และ Spring Boot

เฉพาะลูกค้า AI เท่านั้นที่ต้องใช้การปรับใช้ Azure OpenAI และการยืนยันตัวตน `DefaultAzureCredential`
เช่น ลงชื่อเข้าใช้ Azure CLI แบบโลคอล หรือ managed identity ใน Azure ตัวตนนี้ต้องมีบทบาท Cognitive Services OpenAI User บน resource ดูรายละเอียดใน [บทที่ 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md)
เซิร์ฟเวอร์ ลูกค้า SDK แบบตรง และการทดสอบแบบอัตโนมัติทั้งหมดไม่จำเป็นต้องใช้บัญชี Azure หรือการเข้าถึงโมเดล


## เวอร์ชันของไลบรารีที่ใช้

ไลบรารีสำหรับ release ที่ตรวจสอบเมื่อ 14 กันยายน 2026:

| ไลบรารี | เวอร์ชัน |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (จัดการโดย Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| ตัวเชื่อมต่อ OpenAI อย่างเป็นทางการของ LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (จัดการโดย Boot) | 6.0.3 |

MCP และตัวเชื่อมต่อ OpenAI อย่างเป็นทางการเป็น release beta ที่เผยแพร่ใน Maven Central ไม่ใช่ snapshots
เวอร์ชันของพวกมันต่างจาก LangChain4j core ไม่จำเป็นต้องใช้ snapshot หรือ milestone repositories
dependencies ที่ใช้เฉพาะฝั่งไคลเอนต์มี scope เป็น test เนื่องจากตัวอย่างที่รันอยู่ใน `src/test/java`

## เข้าใจโครงสร้างโปรเจกต์

โปรเจกต์เครื่องคิดเลขมีไฟล์สำคัญหลายไฟล์:

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

## อธิบายคอมโพเนนต์หลัก

### 1. แอปพลิเคชันหลัก

**ไฟล์:** `McpServerApplication.java`

นี่คือจุดเริ่มต้นของบริการเครื่องคิดเลขของเรา เป็นแอป Spring Boot ปกติที่มีการเพิ่มพิเศษหนึ่งอย่าง:

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

**ฟังก์ชันที่ทำงาน:**
- เริ่ม Spring Boot เว็บเซิร์ฟเวอร์บนพอร์ต 8080
- สร้าง `ToolCallbackProvider` ที่ทำให้เมธอดเครื่องคิดเลขของเราใช้งานเป็นเครื่องมือ MCP ได้
- ตัวส่วนประกอบ `@Bean` บอกให้ Spring จัดการเป็น component ที่ส่วนอื่นๆ ใช้ได้

### 2. บริการเครื่องคิดเลข

**ไฟล์:** `CalculatorService.java`

ที่นี่คือจุดที่คำนวณเลขเกิดขึ้น แต่ละเมธอดติด `@Tool` เพื่อให้เรียกผ่าน MCP ได้:

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
    
    // การดำเนินการเครื่องคิดเลขเพิ่มเติม...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**คุณสมบัติหลัก:**

1. **การติดประกาศ `@Tool`**: บอก MCP ว่าเมธอดนี้สามารถเรียกผ่านไคลเอนต์ภายนอกได้
2. **คำอธิบายชัดเจน**: ทุกเครื่องมือมีคำอธิบายช่วยให้โมเดล AI เข้าใจว่าจะใช้เมื่อใด
3. **รูปแบบผลลัพธ์ที่เหมือนกัน**: ทุกการคำนวณคืนค่าเป็นสตริงที่อ่านง่าย เช่น "5.00 + 3.00 = 8.00"
4. **การจัดการข้อผิดพลาด**: การหารด้วยศูนย์และรากที่เป็นลบจะคืนข้อความข้อผิดพลาด

**ฟังก์ชันที่พร้อมใช้งาน:**
- `add(a, b)` - บวกเลขสองตัว
- `subtract(a, b)` - ลบตัวที่สองจากตัวแรก
- `multiply(a, b)` - คูณเลขสองตัว
- `divide(a, b)` - หารตัวแรกด้วยตัวที่สอง (ตรวจสอบศูนย์)
- `power(base, exponent)` - ยกกำลัง
- `squareRoot(number)` - คำนวณรากที่สอง (ตรวจสอบค่าติดลบ)
- `modulus(a, b)` - คืนเศษจากการหาร
- `absolute(number)` - คืนค่าสัมบูรณ์
- `help()` - คืนข้อมูลเกี่ยวกับทุกฟังก์ชัน

### 3. ลูกค้า MCP แบบตรง

ดู [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java)

ลูกค้านี้ใช้ `HttpClientStreamableHttpTransport` ที่ `/mcp` เริ่มต้นการเชื่อมต่อ
ส่งสัญญาณ ping เซิร์ฟเวอร์ และติดตามเครื่องมือผ่านการแบ่งหน้า ตรวจสอบว่ามีเครื่องมือเก้าอย่างครบ
และเรียกใช้แต่ละเครื่องมือ รวม `modulus` และ `help` โดยไม่ใช้โมเดล AI

ตัวสร้างคำขอปัจจุบันเป็นดังนี้:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

ข้อผิดพลาดของโปรโตคอลจะทำให้ไคลเอนต์ล้มเหลวแทนที่จะพิมพ์ข้อความสำเร็จผิด ๆ ลูกค้า MCP
ปิดด้วย try-with-resources รวมถึงกรณีพบข้อผิดพลาดเมื่อค้นหาหรือเรียกใช้งานเครื่องมือ

### 4. ลูกค้าที่ใช้ AI

ดู [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
และ [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java)

`OpenAiOfficialChatModel` เป็นการใช้งาน API `ChatModel` ของ LangChain4j รุ่นปัจจุบัน
`StreamableHttpMcpTransport` เชื่อมต่อไปยัง endpoint `/mcp` เดียวกับไคลเอนต์ SDK
`AiServices` ค้นหาเครื่องมือและจัดการการสนทนาเรียกใช้เครื่องมือและผลลัพธ์

การปรับใช้เริ่มต้นคือ **GPT-5.6 Luna** โดยปิดการใช้ reasoning อย่างชัดเจน:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

ค่าปริยายนี้จะใช้กับทุกคำสั่งเสริม รวมถึงการติดตามผลหลังการใช้เครื่องมือ
ลูกค้าใช้ `BearerTokenCredential` ที่รีเฟรชได้โดยมีพื้นฐานจาก `DefaultAzureCredential`
และ scope `https://ai.azure.com/.default` ไม่ใช่โทเค็นแบบครั้งเดียวใช้เป็น API key
URL ของ resource และ URL ที่ลงท้ายด้วย `/openai/v1` ทั้งคู่ถูกยอมรับ

บอทเก็บประวัติการสนทนาไว้จำกัดจำนวน แสดง `Tool executed: ...` พร้อมผลลัพธ์ MCP จริง
และล้มเหลวหากการตอบกลับข้ามเครื่องมือ วงจรเครื่องมือจำกัด 4 รอบเท่านั้น
ข้อผิดพลาดการยืนยันตัวตน โมเดล MCP และเครื่องมือจะถูกส่งผ่าน ไปยังผู้ใช้
และปิดไคลเอนต์ MCP/transport และไคลเอนต์ OpenAI อย่างเป็นทางการทั้งตอนสำเร็จและล้มเหลว

## การรันตัวอย่าง

### ขั้นตอนที่ 1: เริ่มเครื่องคิดเลขเซิร์ฟเวอร์

ไม่ต้องตั้งค่า Azure สำหรับเซิร์ฟเวอร์ คำสั่งด้านล่างรันจากไดเรกทอรีของตัวอย่างนี้
ตัวอย่างใช้พอร์ต **18081** เพื่อหลีกเลี่ยงความขัดแย้งกับตัวอย่างอื่น พอร์ตเริ่มต้นยังคงเป็น 8080

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

จุดเชื่อมต่อ MCP คือ `http://localhost:18081/mcp` ข้อมูลสถานะและการค้นหามีที่
`http://localhost:18081/health` และ `http://localhost:18081/info`
Streamable HTTP แทนที่การส่งข้อมูล SSE แบบเก่า `/sse` และ `/v1/tools` ไม่ใช่ endpoints

### ขั้นตอนที่ 2: ทดสอบด้วยไคลเอนต์แบบตรง

ในเทอร์มินัล PowerShell อีกอัน:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

ไม่ต้องใส่อินพุต เครื่องมือเก้าชิ้นทั้งหมดถูกใช้งาน ผลลัพธ์เลขคณิตคาดว่าจะมี
8, 6, 42, 5, 256, 4, 2, และ 5.5 ตามด้วยข้อความช่วยเหลือ

### ขั้นตอนที่ 3: ทดสอบด้วยลูกค้า AI

หลังยืนยันตัวตนตามที่ได้อธิบายไว้ในพื้นฐาน ตั้งค่าลูกค้า AI ในเทอร์มินัลเดียวกัน:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

คาดว่าจะเห็นบรรทัด `Tool executed: add` พร้อมค่าผลลัพธ์ `41.80` ตามด้วยคำตอบของโมเดล
โหมด prompt เดี่ยวจะออกโดยไม่รออินพุต หากต้องการรันเดโมสี่ prompt เดิม:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

เดโมนี้เรียก `add`, `squareRoot`, `help` และลำดับผูกปม `power` แล้วตามด้วย `divide`
คำตอบตัวเลขที่คาดหวังคือ 41.8, 12, และ 64 การละเว้นอาร์กิวเมนต์ก็รันเดโมนี้ได้เช่นกัน

### ขั้นตอนที่ 4: รันบอทแบบโต้ตอบ

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

พิมพ์ `Multiply 6 by 7 using the calculator service` แล้วตามด้วย `exit` หรือ `quit`
คาดว่าจะได้ผลลัพธ์จริงของเครื่องมือ `multiply` เท่ากับ 42 บรรทัดว่างจะถูกละเว้น EOF ก็จะปิดเซสชัน
สำหรับทดสอบแบบไม่โต้ตอบขั้นต้นของจุดเข้าครั้งนี้:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

จุดเข้า AI ทั้งสองรองรับ `--prompt "question"`, `--demo` และ `--interactive`
ตัวเลือกที่ไม่ถูกต้องจะล้มเหลวก่อนเปิดเชื่อมต่อ ทุกอาร์กิวเมนต์ `-D...` ของ Maven ต้องใส่ในอัญประกาศเต็ม
ใน PowerShell สำหรับ Bash ให้ใช้ `export NAME=value` แทน `$env:NAME = "value"`

**โควต้า:** รันตัวอย่าง AI ต่อเนื่อง โหมด prompt ง่ายมักต้องการคำขอโมเดลสองครั้ง
เดโมเต็มโดยปกติต้องการเก้าครั้ง รวมถึงการติดตามผลการใช้งานเครื่องมือในรายการ ถ้าใช้การปรับใช้ 10 RPM ร่วมกัน
ควรเว้นช่วงหน้าต่างโควต้าก่อนรัน AI ครั้งถัดไป หากพบ 429 จะล้มเหลวทันทีไม่มีการลองใหม่อัตโนมัติ
ให้ปฏิบัติตามคำแนะนำ retry-after จากบริการ จำนวนคำขอขึ้นอยู่กับโมเดล
การทดสอบแบบออฟไลน์ไม่ใช้โควต้าและไม่ยืนยันความพร้อมของ Luna หรือคุณภาพคำตอบ

### การตั้งค่าและการปิดเซิร์ฟเวอร์

| การตั้งค่า | ค่าเริ่มต้น / พฤติกรรม |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL พื้นฐาน, ไม่รวม `/mcp` |
| `-Dmcp.server.url=...` | เขียนทับ `MCP_SERVER_URL` สำหรับไคลเอนต์ทั้งหมด |
| `AZURE_OPENAI_ENDPOINT` | จำเป็นเฉพาะไคลเอนต์ AI; URL ของ resource หรือ URL ที่ลงท้ายด้วย `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; ชื่อ deployment ใน Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; จำนวนเต็มบวก |
| ความพยายาม reasoning | ปิดตลอดเวลา รวมถึงการติดตามการวนเครื่องมือ |

การปรับใช้ที่เขียนทับต้องรองรับ `reasoning_effort=none` และ `max_completion_tokens`
ไคลเอนต์จะไม่อ่านไฟล์ `.env` โดยอัตโนมัติ หยุดเซิร์ฟเวอร์ด้วย `Ctrl+C` หลังทดสอบเสร็จ
ไคลเอนต์จะคืนค่าปกติโดยไม่เรียก `System.exit` หรือหน่วงเวลา shutdown

## การทดสอบแบบออฟไลน์

```powershell
mvn -B -ntp clean verify
```

การทดสอบทั้งหมดเป็นแบบออฟไลน์กับ Azure: โปรโตคอลชุดนี้เริ่มเซิร์ฟเวอร์ Spring และ
stub ที่รองรับ OpenAI บนพอร์ต loopback แบบสุ่ม แล้วปิดเซิร์ฟเวอร์ เหมือนเดิม Maven อาจต้องดาวน์โหลด dependencies
ไม่มีการใช้ credential, deployment จริง หรือ MCP server ที่มีอยู่ก่อนหน้า

- การทดสอบ unit ของเครื่องคิดเลขครอบคลุมการคำนวณทั้งหมด ผลลัพธ์เลขฐานสิบ, help, และข้อผิดพลาดโดเมน
- การทดสอบ MCP ครอบคลุมการเริ่มต้น ค้นหา การเรียกเครื่องมือเก้าอย่าง การล้มเหลวของเครื่องมือ และข้อมูลสถานะ/สุขภาพ
- การทดสอบโปรโตคอล AI รันเดโมเต็มและบอทแบบโต้ตอบกับเครื่องคิดเลขจริง
  ตรวจสอบว่าผลลัพธ์เครื่องมือเป็นอินพุตให้ completion ถัดไป และตรวจสอบ HTTP body ทุกครั้งสำหรับ Luna,
  `reasoning_effort: "none"`, และ `max_completion_tokens` โดยไม่มี `max_tokens` แบบเก่า
- การทดสอบการตั้งค่าและอินพุตครอบคลุมการเขียนทับ deployment และ endpoint, บรรทัดว่าง, EOF, exit/quit,
  โหมด prompt เดี่ยว, ตัวเลือกไม่ถูกต้อง, และการส่งผ่านข้อผิดพลาด ทดสอบโควต้าพิสูจน์ว่า 429 ไม่ได้ถูกลองใหม่

## การทำงานร่วมกันทั้งหมด

นี่คือขั้นตอนทั้งหมดเมื่อคุณถาม AI ว่า "5 + 3 เท่ากับอะไร?":

1. **คุณ** ถาม AI ด้วยภาษาธรรมชาติ
2. **AI** วิเคราะห์คำถามและเข้าใจว่าคุณต้องการบวกเลข
3. **AI** เรียกใช้ MCP server: `add(5.0, 3.0)`
4. **บริการเครื่องคิดเลข** คำนวณ: `5.0 + 3.0 = 8.0`
5. **บริการเครื่องคิดเลข** คืนค่า: `"5.00 + 3.00 = 8.00"`
6. **AI** รับผลลัพธ์และจัดรูปแบบคำตอบเป็นภาษาธรรมชาติ
7. **คุณ** ได้รับคำตอบ: "ผลบวกของ 5 กับ 3 คือ 8"

## ขั้นตอนถัดไป

สำหรับตัวอย่างเพิ่มเติม ดู [บทที่ 04: ตัวอย่างเชิงปฏิบัติ](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ปฏิเสธความรับผิดชอบ**:
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) ขณะที่เราพยายามให้ความถูกต้อง โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ แนะนำให้ใช้การแปลโดยมนุษย์มืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->