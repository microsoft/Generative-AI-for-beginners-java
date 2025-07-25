<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:36:01+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "th"
}
-->
# คู่มือการใช้งาน Foundry Local Spring Boot

## สารบัญ

- [ข้อกำหนดเบื้องต้น](../../../../04-PracticalSamples/foundrylocal)
- [ภาพรวมของโปรเจกต์](../../../../04-PracticalSamples/foundrylocal)
- [การทำความเข้าใจโค้ด](../../../../04-PracticalSamples/foundrylocal)
  - [1. การตั้งค่าของแอปพลิเคชัน (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. คลาสแอปพลิเคชันหลัก (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. ชั้นบริการ AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. การพึ่งพาโปรเจกต์ (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [การทำงานร่วมกันของระบบ](../../../../04-PracticalSamples/foundrylocal)
- [การตั้งค่า Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [การรันแอปพลิเคชัน](../../../../04-PracticalSamples/foundrylocal)
- [ผลลัพธ์ที่คาดหวัง](../../../../04-PracticalSamples/foundrylocal)
- [ขั้นตอนถัดไป](../../../../04-PracticalSamples/foundrylocal)
- [การแก้ไขปัญหา](../../../../04-PracticalSamples/foundrylocal)

## ข้อกำหนดเบื้องต้น

ก่อนเริ่มต้นคู่มือนี้ คุณต้องมี:

- **Java 21 หรือสูงกว่า** ติดตั้งในระบบของคุณ
- **Maven 3.6+** สำหรับการสร้างโปรเจกต์
- **Foundry Local** ติดตั้งและกำลังทำงานอยู่

### **การติดตั้ง Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## ภาพรวมของโปรเจกต์

โปรเจกต์นี้ประกอบด้วย 4 ส่วนหลัก:

1. **Application.java** - จุดเริ่มต้นของแอปพลิเคชัน Spring Boot
2. **FoundryLocalService.java** - ชั้นบริการที่จัดการการสื่อสารกับ AI
3. **application.properties** - การตั้งค่าสำหรับการเชื่อมต่อ Foundry Local
4. **pom.xml** - การพึ่งพา Maven และการตั้งค่าโปรเจกต์

## การทำความเข้าใจโค้ด

### 1. การตั้งค่าของแอปพลิเคชัน (application.properties)

**ไฟล์:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**สิ่งที่ไฟล์นี้ทำ:**
- **base-url**: ระบุที่อยู่ที่ Foundry Local กำลังทำงาน (พอร์ตเริ่มต้นคือ 5273)
- **model**: ชื่อโมเดล AI ที่จะใช้สำหรับการสร้างข้อความ

**แนวคิดสำคัญ:** Spring Boot จะโหลดค่าการตั้งค่าเหล่านี้โดยอัตโนมัติและทำให้พร้อมใช้งานในแอปพลิเคชันผ่าน `@Value` annotation

### 2. คลาสแอปพลิเคชันหลัก (Application.java)

**ไฟล์:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**สิ่งที่ไฟล์นี้ทำ:**
- `@SpringBootApplication` เปิดใช้งานการตั้งค่าอัตโนมัติของ Spring Boot
- `WebApplicationType.NONE` บอก Spring ว่านี่คือแอปพลิเคชันแบบ command-line ไม่ใช่เว็บเซิร์ฟเวอร์
- เมธอดหลักเริ่มต้นแอปพลิเคชัน Spring

**ตัวอย่างการทำงาน:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**สิ่งที่ไฟล์นี้ทำ:**
- `@Bean` สร้างคอมโพเนนต์ที่ Spring จัดการ
- `CommandLineRunner` รันโค้ดหลังจาก Spring Boot เริ่มต้น
- `foundryLocalService` ถูก inject โดย Spring (dependency injection)
- ส่งข้อความทดสอบไปยัง AI และแสดงผลลัพธ์ที่ได้รับ

### 3. ชั้นบริการ AI (FoundryLocalService.java)

**ไฟล์:** `src/main/java/com/example/FoundryLocalService.java`

#### การ inject การตั้งค่า:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**สิ่งที่ไฟล์นี้ทำ:**
- `@Service` บอก Spring ว่าคลาสนี้ให้บริการด้าน business logic
- `@Value` inject ค่าการตั้งค่าจาก application.properties
- ไวยากรณ์ `:default-value` ให้ค่าตั้งต้นหากไม่มีการตั้งค่า

#### การตั้งค่าคลายเอนต์:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**สิ่งที่ไฟล์นี้ทำ:**
- `@PostConstruct` รันเมธอดนี้หลังจาก Spring สร้างบริการ
- สร้าง OpenAI client ที่ชี้ไปยัง Foundry Local
- เส้นทาง `/v1` จำเป็นสำหรับความเข้ากันได้กับ OpenAI API
- API key เป็น "unused" เพราะการพัฒนาในเครื่องไม่ต้องการการยืนยันตัวตน

#### เมธอด Chat:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**สิ่งที่ไฟล์นี้ทำ:**
- **ChatCompletionCreateParams**: ตั้งค่าการร้องขอ AI
  - `model`: ระบุโมเดล AI ที่จะใช้
  - `addUserMessage`: เพิ่มข้อความของผู้ใช้ในบทสนทนา
  - `maxCompletionTokens`: จำกัดความยาวของการตอบกลับ (ประหยัดทรัพยากร)
  - `temperature`: ควบคุมความสุ่ม (0.0 = กำหนดแน่นอน, 1.0 = สร้างสรรค์)
- **API Call**: ส่งคำร้องขอไปยัง Foundry Local
- **Response Handling**: ดึงข้อความตอบกลับของ AI อย่างปลอดภัย
- **Error Handling**: จัดการข้อผิดพลาดพร้อมข้อความช่วยเหลือ

### 4. การพึ่งพาโปรเจกต์ (pom.xml)

**การพึ่งพาหลัก:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**สิ่งที่ไฟล์นี้ทำ:**
- **spring-boot-starter**: ให้ฟังก์ชันการทำงานหลักของ Spring Boot
- **openai-java**: OpenAI Java SDK อย่างเป็นทางการสำหรับการสื่อสาร API
- **jackson-databind**: จัดการการ serialize/deserialization JSON สำหรับการเรียก API

## การทำงานร่วมกันของระบบ

นี่คือกระบวนการทั้งหมดเมื่อคุณรันแอปพลิเคชัน:

1. **เริ่มต้นระบบ**: Spring Boot เริ่มต้นและอ่าน `application.properties`
2. **สร้างบริการ**: Spring สร้าง `FoundryLocalService` และ inject ค่าการตั้งค่า
3. **ตั้งค่าคลายเอนต์**: `@PostConstruct` เริ่มต้น OpenAI client เพื่อเชื่อมต่อกับ Foundry Local
4. **การทำงานตัวอย่าง**: `CommandLineRunner` ทำงานหลังจากเริ่มต้นระบบ
5. **เรียก AI**: ตัวอย่างเรียก `foundryLocalService.chat()` พร้อมข้อความทดสอบ
6. **คำร้องขอ API**: บริการสร้างและส่งคำร้องขอที่เข้ากันได้กับ OpenAI ไปยัง Foundry Local
7. **การประมวลผลตอบกลับ**: บริการดึงและส่งคืนข้อความตอบกลับของ AI
8. **แสดงผล**: แอปพลิเคชันพิมพ์ผลลัพธ์และออกจากระบบ

## การตั้งค่า Foundry Local

เพื่อการตั้งค่า Foundry Local ให้ทำตามขั้นตอนเหล่านี้:

1. **ติดตั้ง Foundry Local** โดยใช้คำแนะนำในส่วน [ข้อกำหนดเบื้องต้น](../../../../04-PracticalSamples/foundrylocal)
2. **ดาวน์โหลดโมเดล AI** ที่คุณต้องการใช้ เช่น `phi-3.5-mini` ด้วยคำสั่ง:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **ตั้งค่าไฟล์ application.properties** ให้ตรงกับการตั้งค่า Foundry Local ของคุณ โดยเฉพาะหากคุณใช้พอร์ตหรือโมเดลที่แตกต่างกัน

## การรันแอปพลิเคชัน

### ขั้นตอนที่ 1: เริ่มต้น Foundry Local
```bash
foundry model run phi-3.5-mini
```

### ขั้นตอนที่ 2: สร้างและรันแอปพลิเคชัน
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## ผลลัพธ์ที่คาดหวัง

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## ขั้นตอนถัดไป

สำหรับตัวอย่างเพิ่มเติม ดู [Chapter 04: Practical samples](../README.md)

## การแก้ไขปัญหา

### ปัญหาทั่วไป

**"Connection refused" หรือ "Service unavailable"**
- ตรวจสอบว่า Foundry Local กำลังทำงานอยู่: `foundry model list`
- ยืนยันว่าบริการอยู่บนพอร์ต 5273: ตรวจสอบ `application.properties`
- ลองรีสตาร์ท Foundry Local: `foundry model run phi-3.5-mini`

**"Model not found" errors**
- ตรวจสอบโมเดลที่มีอยู่: `foundry model list`
- อัปเดตชื่อโมเดลใน `application.properties` ให้ตรงกัน
- ดาวน์โหลดโมเดลหากจำเป็น: `foundry model run phi-3.5-mini`

**ข้อผิดพลาดในการคอมไพล์ Maven**
- ตรวจสอบว่าใช้ Java 21 หรือสูงกว่า: `java -version`
- ล้างและสร้างใหม่: `mvn clean compile`
- ตรวจสอบการเชื่อมต่ออินเทอร์เน็ตสำหรับการดาวน์โหลด dependency

**แอปพลิเคชันเริ่มต้นแต่ไม่มีผลลัพธ์**
- ยืนยันว่า Foundry Local ตอบสนอง: เปิดเบราว์เซอร์ไปที่ `http://localhost:5273`
- ตรวจสอบ log ของแอปพลิเคชันสำหรับข้อความข้อผิดพลาดเฉพาะ
- ตรวจสอบว่าโมเดลโหลดเสร็จและพร้อมใช้งาน

**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้การแปลมีความถูกต้อง แต่โปรดทราบว่าการแปลอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลภาษามนุษย์ที่มีความเชี่ยวชาญ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดที่เกิดจากการใช้การแปลนี้