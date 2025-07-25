<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:35:06+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "th"
}
-->
# บทเรียนเทคนิคพื้นฐานของ Generative AI

## สารบัญ

- [ข้อกำหนดเบื้องต้น](../../../03-CoreGenerativeAITechniques)
- [เริ่มต้นใช้งาน](../../../03-CoreGenerativeAITechniques)
  - [ขั้นตอนที่ 1: ตั้งค่าตัวแปรสภาพแวดล้อม](../../../03-CoreGenerativeAITechniques)
  - [ขั้นตอนที่ 2: ไปที่ไดเรกทอรีตัวอย่าง](../../../03-CoreGenerativeAITechniques)
- [บทเรียนที่ 1: การเติมข้อความและแชทด้วย LLM](../../../03-CoreGenerativeAITechniques)
- [บทเรียนที่ 2: การเรียกใช้ฟังก์ชัน](../../../03-CoreGenerativeAITechniques)
- [บทเรียนที่ 3: RAG (การสร้างข้อความเสริมด้วยการดึงข้อมูล)](../../../03-CoreGenerativeAITechniques)
- [บทเรียนที่ 4: AI ที่มีความรับผิดชอบ](../../../03-CoreGenerativeAITechniques)
- [รูปแบบทั่วไปในตัวอย่าง](../../../03-CoreGenerativeAITechniques)
- [ขั้นตอนถัดไป](../../../03-CoreGenerativeAITechniques)
- [การแก้ไขปัญหา](../../../03-CoreGenerativeAITechniques)
  - [ปัญหาที่พบบ่อย](../../../03-CoreGenerativeAITechniques)

## ภาพรวม

บทเรียนนี้ให้ตัวอย่างการใช้งานจริงของเทคนิคพื้นฐานใน Generative AI โดยใช้ Java และ GitHub Models คุณจะได้เรียนรู้วิธีการโต้ตอบกับ Large Language Models (LLMs) การเรียกใช้ฟังก์ชัน การใช้การสร้างข้อความเสริมด้วยการดึงข้อมูล (RAG) และการนำแนวปฏิบัติของ AI ที่มีความรับผิดชอบไปใช้

## ข้อกำหนดเบื้องต้น

ก่อนเริ่มต้น ตรวจสอบให้แน่ใจว่าคุณมี:
- ติดตั้ง Java 21 หรือเวอร์ชันที่สูงกว่า
- Maven สำหรับการจัดการ dependencies
- บัญชี GitHub พร้อม Personal Access Token (PAT)

## เริ่มต้นใช้งาน

### ขั้นตอนที่ 1: ตั้งค่าตัวแปรสภาพแวดล้อม

ก่อนอื่น คุณต้องตั้งค่า GitHub token เป็นตัวแปรสภาพแวดล้อม Token นี้จะช่วยให้คุณเข้าถึง GitHub Models ได้ฟรี

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

### ขั้นตอนที่ 2: ไปที่ไดเรกทอรีตัวอย่าง

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## บทเรียนที่ 1: การเติมข้อความและแชทด้วย LLM

**ไฟล์:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### สิ่งที่ตัวอย่างนี้สอน

ตัวอย่างนี้แสดงให้เห็นถึงกลไกพื้นฐานของการโต้ตอบกับ Large Language Model (LLM) ผ่าน OpenAI API รวมถึงการเริ่มต้น client ด้วย GitHub Models รูปแบบโครงสร้างข้อความสำหรับ system และ user prompts การจัดการสถานะแชทผ่านการสะสมประวัติข้อความ และการปรับแต่งพารามิเตอร์เพื่อควบคุมความยาวและระดับความคิดสร้างสรรค์ของคำตอบ

### แนวคิดสำคัญในโค้ด

#### 1. การตั้งค่า Client
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

นี่คือการสร้างการเชื่อมต่อกับ GitHub Models โดยใช้ token ของคุณ

#### 2. การเติมข้อความแบบง่าย
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

#### 3. หน่วยความจำของการสนทนา
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI จะจำข้อความก่อนหน้าได้ก็ต่อเมื่อคุณรวมข้อความเหล่านั้นไว้ในคำขอครั้งถัดไป

### การรันตัวอย่าง
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### สิ่งที่เกิดขึ้นเมื่อคุณรัน

1. **การเติมข้อความแบบง่าย**: AI ตอบคำถามเกี่ยวกับ Java โดยมีคำแนะนำจาก system prompt
2. **การแชทหลายรอบ**: AI รักษาบริบทระหว่างคำถามหลายข้อ
3. **การแชทแบบโต้ตอบ**: คุณสามารถสนทนากับ AI ได้จริง

## บทเรียนที่ 2: การเรียกใช้ฟังก์ชัน

**ไฟล์:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### สิ่งที่ตัวอย่างนี้สอน

การเรียกใช้ฟังก์ชันช่วยให้โมเดล AI สามารถร้องขอการดำเนินการของเครื่องมือและ API ภายนอกผ่านโปรโตคอลที่มีโครงสร้าง โดยโมเดลจะวิเคราะห์คำขอในภาษาธรรมชาติ กำหนดการเรียกใช้ฟังก์ชันที่จำเป็นพร้อมพารามิเตอร์ที่เหมาะสมโดยใช้ JSON Schema และประมวลผลผลลัพธ์ที่ได้เพื่อสร้างคำตอบที่มีบริบท ขณะที่การดำเนินการของฟังก์ชันยังคงอยู่ภายใต้การควบคุมของนักพัฒนาเพื่อความปลอดภัยและความน่าเชื่อถือ

### แนวคิดสำคัญในโค้ด

#### 1. การกำหนดฟังก์ชัน
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

นี่คือการบอก AI ว่ามีฟังก์ชันใดบ้างและใช้งานอย่างไร

#### 2. ลำดับการเรียกใช้ฟังก์ชัน
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

#### 3. การใช้งานฟังก์ชัน
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

### การรันตัวอย่าง
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### สิ่งที่เกิดขึ้นเมื่อคุณรัน

1. **ฟังก์ชันสภาพอากาศ**: AI ขอข้อมูลสภาพอากาศของ Seattle คุณให้ข้อมูลนั้น และ AI จัดรูปแบบคำตอบ
2. **ฟังก์ชันเครื่องคิดเลข**: AI ขอการคำนวณ (15% ของ 240) คุณคำนวณให้ และ AI อธิบายผลลัพธ์

## บทเรียนที่ 3: RAG (การสร้างข้อความเสริมด้วยการดึงข้อมูล)

**ไฟล์:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### สิ่งที่ตัวอย่างนี้สอน

การสร้างข้อความเสริมด้วยการดึงข้อมูล (RAG) ผสมผสานการดึงข้อมูลเข้ากับการสร้างข้อความ โดยการเพิ่มบริบทจากเอกสารภายนอกลงใน prompts ของ AI ทำให้โมเดลสามารถให้คำตอบที่แม่นยำโดยอ้างอิงจากแหล่งข้อมูลเฉพาะ แทนที่จะพึ่งพาข้อมูลการฝึกอบรมที่อาจล้าสมัยหรือไม่ถูกต้อง พร้อมทั้งรักษาขอบเขตที่ชัดเจนระหว่างคำถามของผู้ใช้และแหล่งข้อมูลที่เชื่อถือได้ผ่านการออกแบบ prompts อย่างมีกลยุทธ์

### แนวคิดสำคัญในโค้ด

#### 1. การโหลดเอกสาร
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. การเพิ่มบริบท
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

เครื่องหมาย triple quotes ช่วยให้ AI แยกแยะระหว่างบริบทและคำถาม

#### 3. การจัดการคำตอบอย่างปลอดภัย
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

ตรวจสอบคำตอบจาก API เสมอเพื่อป้องกันข้อผิดพลาด

### การรันตัวอย่าง
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### สิ่งที่เกิดขึ้นเมื่อคุณรัน

1. โปรแกรมโหลด `document.txt` (มีข้อมูลเกี่ยวกับ GitHub Models)
2. คุณถามคำถามเกี่ยวกับเอกสาร
3. AI ตอบโดยอ้างอิงเฉพาะเนื้อหาในเอกสาร ไม่ใช่ความรู้ทั่วไป

ลองถาม: "GitHub Models คืออะไร?" เทียบกับ "สภาพอากาศเป็นอย่างไร?"

## บทเรียนที่ 4: AI ที่มีความรับผิดชอบ

**ไฟล์:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### สิ่งที่ตัวอย่างนี้สอน

ตัวอย่าง AI ที่มีความรับผิดชอบแสดงให้เห็นถึงความสำคัญของการใช้มาตรการความปลอดภัยในแอปพลิเคชัน AI โดยแสดงตัวกรองความปลอดภัยที่ตรวจจับหมวดหมู่เนื้อหาที่เป็นอันตราย เช่น คำพูดแสดงความเกลียดชัง การคุกคาม การทำร้ายตัวเอง เนื้อหาทางเพศ และความรุนแรง พร้อมทั้งแสดงให้เห็นว่าแอปพลิเคชัน AI สำหรับการใช้งานจริงควรจัดการกับการละเมิดนโยบายเนื้อหาอย่างไรด้วยการจัดการข้อยกเว้น การให้ข้อเสนอแนะแก่ผู้ใช้ และกลยุทธ์การตอบกลับสำรอง

### แนวคิดสำคัญในโค้ด

#### 1. กรอบการทดสอบความปลอดภัย
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

#### 2. หมวดหมู่ความปลอดภัยที่ทดสอบ
- คำแนะนำเกี่ยวกับความรุนแรง/การทำร้าย
- คำพูดแสดงความเกลียดชัง
- การละเมิดความเป็นส่วนตัว
- ข้อมูลทางการแพทย์ที่ผิดพลาด
- กิจกรรมที่ผิดกฎหมาย

### การรันตัวอย่าง
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### สิ่งที่เกิดขึ้นเมื่อคุณรัน

โปรแกรมทดสอบ prompts ที่เป็นอันตรายต่างๆ และแสดงให้เห็นว่าระบบความปลอดภัยของ AI:
1. **บล็อกคำขอที่อันตราย** ด้วยข้อผิดพลาด HTTP 400
2. **อนุญาตเนื้อหาที่ปลอดภัย** ให้สร้างได้ตามปกติ
3. **ปกป้องผู้ใช้** จากผลลัพธ์ AI ที่เป็นอันตราย

## รูปแบบทั่วไปในตัวอย่าง

### รูปแบบการตรวจสอบสิทธิ์
ตัวอย่างทั้งหมดใช้รูปแบบนี้เพื่อเชื่อมต่อกับ GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### รูปแบบการจัดการข้อผิดพลาด
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### รูปแบบโครงสร้างข้อความ
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## ขั้นตอนถัดไป

[บทที่ 04: ตัวอย่างการใช้งานจริง](../04-PracticalSamples/README.md)

## การแก้ไขปัญหา

### ปัญหาที่พบบ่อย

**"GITHUB_TOKEN not set"**
- ตรวจสอบให้แน่ใจว่าคุณตั้งค่าตัวแปรสภาพแวดล้อมแล้ว
- ยืนยันว่า token ของคุณมี scope `models:read`

**"No response from API"**
- ตรวจสอบการเชื่อมต่ออินเทอร์เน็ตของคุณ
- ยืนยันว่า token ของคุณยังใช้งานได้
- ตรวจสอบว่าคุณถึงขีดจำกัดการใช้งานหรือไม่

**ข้อผิดพลาดในการคอมไพล์ Maven**
- ตรวจสอบให้แน่ใจว่าคุณมี Java 21 หรือเวอร์ชันที่สูงกว่า
- รัน `mvn clean compile` เพื่อรีเฟรช dependencies

**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้การแปลมีความถูกต้องมากที่สุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลภาษาจากผู้เชี่ยวชาญ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดซึ่งเกิดจากการใช้การแปลนี้