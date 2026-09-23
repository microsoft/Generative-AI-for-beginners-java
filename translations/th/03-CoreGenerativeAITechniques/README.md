# คู่มือเทคนิคหลักของ Generative AI

## สารบัญ

- [ข้อกำหนดเบื้องต้น](#ข้อกำหนดเบื้องต้น)
- [เริ่มต้น](#เริ่มต้น)
- [คำแนะนำการเลือกโมเดล](#คำแนะนำการเลือกโมเดล)
- [บทเรียนที่ 1: การเติมข้อความและแชท LLM](#บทเรียนที่-1-การเติมข้อความและแชท-llm)
- [บทเรียนที่ 2: การเรียกฟังก์ชัน](#บทเรียนที่-2-การเรียกฟังก์ชัน)
- [บทเรียนที่ 3: RAG (การสร้างเสริมด้วยการดึงข้อมูล)](#บทเรียนที่-3-rag-การสร้างเสริมด้วยการดึงข้อมูล)
- [บทเรียนที่ 4: AI ที่รับผิดชอบ](#บทเรียนที่-4-ai-ที่รับผิดชอบ)
- [รูปแบบทั่วไปในตัวอย่างต่าง ๆ](#รูปแบบทั่วไปในตัวอย่างต่าง-ๆ)
- [การทดสอบหน่วย](#การทดสอบหน่วย)
- [การตรวจสอบสดแบบลำดับ](#การตรวจสอบสดแบบลำดับ)
- [การแก้ไขปัญหา](#การแก้ไขปัญหา)
- [ขั้นตอนถัดไป](#ขั้นตอนถัดไป)

## ภาพรวม

โปรแกรม Java สี่โปรแกรมแยกต่างหากสาธิตการแชท, ประวัติการสนทนา, การเรียกฟังก์ชัน, การสร้างเสริมด้วยการดึงข้อมูลทั้งเอกสาร (RAG) และการจัดการการตอบสนอง AI ที่รับผิดชอบ คำขอแชททั้งหมดใช้เป้าหมายเป็น **GPT-5.6 Luna กับความพยายามในการให้เหตุผล `none`** โดยค่าเริ่มต้น

ตัวอย่างเหล่านี้ใช้ OpenAI Java SDK อย่างเป็นทางการพร้อมจุดสิ้นสุด v1 ของ Azure OpenAI ตามคำแนะนำจาก [Microsoft's SDK guidance](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) แพ็กเกจ `azure-ai-openai` รุ่นเก่าเป็นสิ่งที่ไม่จำเป็นอีกต่อไป Chat Completions ยังคงอยู่เพื่อสอนเวิร์กโฟลว์แบบข้อความเดิม; ดูรายละเอียด [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) สำหรับตัวเลือก API อื่น ๆ

## ข้อกำหนดเบื้องต้น

- Java 21 หรือใหม่กว่า และ Maven 3.6.3 หรือใหม่กว่า
- การปรับใช้แชท Azure OpenAI ที่มีชื่อว่า `gpt-5.6-luna` หรือการแทนที่ที่รองรับการตั้งค่า Chat Completions
- บัญชี Azure ที่ลงชื่อเข้าใช้พร้อมบทบาท **Cognitive Services OpenAI User** ในทรัพยากร พัฒนาท้องถิ่นใช้การลงชื่อเข้าใช้ Azure CLI ของคุณ; แอปที่โฮสต์สามารถใช้ managed identity ได้
- ดู [บทที่ 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) สำหรับการตั้งค่าทรัพยากรและคำแนะนำการลงชื่อเข้าใช้

[การกำหนดค่า Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) กำหนดเวอร์ชันเหล่านี้ไว้ ซึ่งตรวจสอบเมื่อวันที่ 2026-09-14:

| ส่วนประกอบ | เวอร์ชัน | วัตถุประสงค์ |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | ไคลเอนต์ Azure v1 อย่างเป็นทางการ |
| `com.azure:azure-identity` | 1.18.6 | การตรวจสอบสิทธิ์โดยไม่ใช้คีย์และรีเฟรชโทเค็น |
| `net.objecthunter:exp4j` | 0.4.8 | การวิเคราะห์นิพจน์คณิตศาสตร์โดยไม่ประเมินโค้ด |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | การทดสอบหน่วย Jupiter แบบออฟไลน์ |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | คอมไพล์ Java 21, การทดสอบ, ตัวอย่างที่รันได้ |

คอมไพล์เลอร์ใช้ `--release 21` ไม่มีการพึ่งพา Spring Boot, Spring AI หรือ LangChain4j ในตัวอย่างแยกส่วนนี้

## เริ่มต้น

จากรากของรีโพสิทอรี ตั้งค่าจุดสิ้นสุดของทรัพยากรและการแทนที่การปรับใช้ที่เลือกได้ในเชลล์ของคุณ

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

การทดสอบไม่ต้องการข้อมูลรับรอง Azure หรือจุดสิ้นสุด Maven ไม่ได้อ่านไฟล์สภาพแวดล้อมโดยอัตโนมัติ ให้ตั้งค่าตัวแปรในเชลล์ที่ใช้เรียกตัวอย่างสด สำหรับการเปิดใน IDE ให้ตรวจสอบว่าสภาพแวดล้อมที่ใช้เปิดนั้นถูกต้อง

## คำแนะนำการเลือกโมเดล

| ตัวแปรสภาพแวดล้อม | ความหมาย | ค่าเริ่มต้น |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | รากของทรัพยากร Azure HTTPS หรือ URL `/openai/v1` ที่ถูกทำให้เป็นมาตรฐานแล้ว | จำเป็นสำหรับการรันสด |
| `AZURE_OPENAI_DEPLOYMENT` | ชื่อการปรับใช้แชท ไม่ใช่รุ่นโมเดล | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | การกำหนดค่าการปรับใช้ embedding แยกต่างหาก ซึ่งไม่ใช้โดยโปรแกรมสี่โปรแกรมนี้ | `text-embedding-3-small` |

การแทนที่การปรับใช้ที่เว้นว่างจะใช้ค่าเริ่มต้น การตั้งค่าจะเพิ่ม `/openai/v1` อย่างแม่นยำหนึ่งครั้งและปฏิเสธข้อมูลรับรอง สตริงคำถาม และเส้นทางการปรับใช้แบบเก่าในจุดสิ้นสุด

คำขอแชทแต่ละรายการจะตั้งค่า `reasoningEffort(ReasoningEffort.NONE)` และ `maxCompletionTokens(...)` อย่างชัดเจน ไม่มีคำขอใดตั้งค่า `temperature`, `top_p` หรือ ตัวเลือก token completion แบบเก่า รวมถึงการเลือกเครื่องมือและการติดตามผลลัพธ์ของเครื่องมือด้วย ฟังก์ชันเครื่องมือ Chat Completions ของ GPT-5.6 ต้องการความพยายามในการให้เหตุผลเป็น `none`; ดู [Microsoft's chat guidance](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)

**ในบทนี้ไม่มีจุดเข้าใช้งานสตรีมหรือ embedding** ผู้อ่านดึงเอกสารทั้งหมด ไม่ใช่เวกเตอร์ ถ้าคุณขยายมันด้วย embedding ให้ใช้การปรับใช้ embedding แยกต่างหาก เช่น `text-embedding-3-small` อย่าใช้ Luna

## บทเรียนที่ 1: การเติมข้อความและแชท LLM

แหล่งที่มา: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)

โปรแกรมรันคำอธิบาย Java streams อย่างง่าย การสนทนา HashMap/TreeMap สองเทิร์น และแชทโต้ตอบ เทิร์นที่สองรวมการตอบกลับผู้ช่วยเทิร์นแรกไว้ด้วย; แต่ละเทิร์นโต้ตอบยังส่งประวัติการสนทนาก่อนหน้าไปด้วย

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` จัดหาการปรับใช้และการตั้งค่าความพยายามในการให้เหตุผลอย่างชัดเจน แชทโต้ตอบข้ามบรรทัดว่าง, จบที่ `exit` หรือ EOF, และเก็บข้อความระบบพร้อมกับเก้าเทิร์นผู้ใช้/ผู้ช่วยที่เสร็จสมบูรณ์ การจำกัดจำนวนเทิร์นเป็นเพียงขอบเขตการเรียนรู้ ไม่ใช่การรับประกันโควต้า token ที่แน่นอน

จากไดเรกทอรี examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

คาดว่าจะได้รับคำตอบเริ่มต้นสามคำตอบ แล้วจะมีพรอมต์ `You:` ทุกคำถามโต้ตอบที่ไม่ว่างจะเพิ่มคำขอหนึ่งรายการ ขีดจำกัดการเติมคำคือ 200, 300, 400 แล้ว 500 token ต่อเทิร์นโต้ตอบ

## บทเรียนที่ 2: การเรียกฟังก์ชัน

แหล่งที่มา: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)

SDK สร้างสคีมา JSON จากเรคอร์ดที่มีคำอธิบายประกอบ `WeatherArguments` และ `CalculationArguments` ตัวเลือกเครื่องมือที่บังคับทำให้แต่ละตัวอย่างใช้โปรโตคอลเครื่องมือแทนการรับคำตอบโดยไม่ช่วยเหลือจากโมเดล

1. ส่งคำถามพร้อมเครื่องมือที่อนุญาต ความพยายามในการให้เหตุผลเป็น `none` และขีดจำกัดการเติม 300 token
2. ต้องการเหตุผลการสิ้นสุด `tool_calls` ตรวจสอบชื่อฟังก์ชันและ ID การเรียก และแยกวิเคราะห์อาร์กิวเมนต์ JSON ที่พิมพ์แล้ว
3. เรียกใช้ฟังก์ชันภายในเครื่อง โมเดลไม่ดำเนินการโค้ด Java หรือโค้ดทั่วไป
4. เพิ่มข้อความเรียกเครื่องมือผู้ช่วยหนึ่งครั้ง ตามด้วยผลลัพธ์ทุกตัวพร้อม `tool_call_id` ที่ตรงกัน
5. ส่งคำขอสุดท้าย 300 token หนึ่งรายการโดยไม่มีเครื่องมือและต้องการคำตอบที่เสร็จสมบูรณ์และไม่ว่าง

`get_weather` ส่งคืนสภาพอากาศ **จำลอง**, ไม่ใช่ข้อมูลสด มันเคารพชื่อเมืองและแปลงตัวอย่าง 22 องศาเซลเซียสเป็นฟาเรนไฮต์เมื่อร้องขอ `calculate` ประเมินนิพจน์ที่ให้ผ่าน exp4j รองรับรูปแบบเช่น `15% of 240` และ `2 + 3 * 4` และปฏิเสธการคำนวณว่าง ใหญ่เกินไป ไม่ถูกต้อง หรือไม่ใช่จำนวนจำกัด ใช้เลขทศนิยมแบบ floating-point ไม่ใช่ความแม่นยำเลขฐานสิบทางการเงิน

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

คาดว่าจะเห็น `Function: get_weather` สภาพอากาศซ้อม Seattle, `Function: calculate`, `Function result: 36` และคำตอบสองคำตอบสุดท้าย ไม่ต้องใช้ stdin หรือข้อมูลรับรองสภาพอากาศภายนอก การรันที่สำเร็จใช้คำขอแชททั้งหมดสี่คำขอ

## บทเรียนที่ 3: RAG (การสร้างเสริมด้วยการดึงข้อมูล)

แหล่งที่มา: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java) อินพุต: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)

ตัวอย่าง RAG เบื้องต้นนี้ดึงเอกสาร UTF-8 ทั้งฉบับและใส่ในข้อความผู้ใช้พร้อมคำถาม ข้อความระบบแยกต่างหากสั่งให้โมเดลถือว่าเนื้อหาเอกสารถูกมองว่าเป็นข้อมูลที่ไม่น่าเชื่อถือและตอบเฉพาะจากบริบทนั้นเท่านั้น ถ้าเอกสารไม่มีคำตอบ คำตอบที่ร้องขอจะเป็น: `I cannot find that information in the provided document.`

การตั้งฐานข้อมูลสามารถลดการหลอกลวงได้ แต่ไม่มีเครื่องหมายขอบเขตหรือคำสั่งระบบใดรับประกันความถูกต้องหรือป้องกันการโจมตี prompt injection ทุกกรณี ตรวจสอบคำตอบสด การใช้งาน RAG ในผลิตภัณฑ์ปกติจะเพิ่มการแบ่งชิ้นงาน, การเรียกคืน, การอ้างอิงแหล่งที่มา, การควบคุมการเข้าถึง และการประเมินผล

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

ป้อนคำถามหนึ่งข้อ เช่น `Which authentication method does the document describe?` คาดว่าจะได้รับคำตอบกล่าวถึง Microsoft Entra ID โปรแกรมจะออกหลังจากคำขอแชทหนึ่งคำขอพร้อมขีดจำกัดการเติม 500 token

การค้นหาไฟล์เริ่มต้นทำงานจากรากรีโพสิทอรี โฟลเดอร์บท หรือโฟลเดอร์ examples รองรับเส้นทางชัดเจนด้วยเช่นกัน:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

ข้อมูลอินพุตต้องไม่ว่าง: มากสุด 32 KiB ของข้อมูลเอกสาร UTF-8 และ 2,000 อักขระคำถาม ไฟล์หาย คำถามว่าง/EOF และอินพุตใหญ่เกินไปจะล้มเหลวก่อนขั้นตอนสืบค้น

## บทเรียนที่ 4: AI ที่รับผิดชอบ

แหล่งที่มา: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)

การทดสอบหกประเภทครอบคลุมคำสั่งที่เป็นอันตราย, คำพูดเกลียดชัง, ความเป็นส่วนตัว, ข้อมูลทางการแพทย์ที่ผิด, เนื้อหาที่ผิดกฎหมาย และคำถาม AI ที่รับผิดชอบที่ไม่เป็นอันตราย โปรแกรมสังเกตการตอบสนองแทนการสมมุติว่าต้องมีการกรองทุกการทดสอบ

| ผลลัพธ์ | หลักฐาน |
| --- | --- |
| `FILTERED` | รหัสข้อผิดพลาด `content_filter` / `ResponsibleAIPolicyViolation` ที่ชัดเจน หรือเหตุผลสิ้นสุด `content_filter` ในการเติมข้อความ |
| `REFUSED` | ฟิลด์ `message.refusal` ที่มีโครงสร้างและไม่ว่าง |
| `POSSIBLE_REFUSAL` | วลีการปฏิเสธเริ่มต้นในข้อความปกติ; การประเมินผลที่ต้องตรวจสอบ |
| `GENERATED` | การตอบข้อความที่เสร็จสมบูรณ์และไม่ว่าง; ไม่ใช่หลักฐานว่าคอนเทนต์ปลอดภัย |

HTTP 400 ทั่วไป **ไม่ใช่** หลักฐานของการกรอง พารามิเตอร์ไม่ถูกต้อง, ล้มเหลวการตรวจสอบสิทธิ์, จำกัดอัตรา, ข้อผิดพลาดเซิร์ฟเวอร์, การตอบกลับผิดรูปแบบ และการตัดคำตอบจะทำให้การรันล้มเหลวแทนที่จะผลิตความปลอดภัยปลอม คำที่กว้างเช่น "harmful content" ในคำอธิบายนุ่มนวลไม่ถือเป็นการปฏิเสธ

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

คาดหกผลลัพธ์และบทสรุปที่กล่าวว่าเป็นเพียงการสังเกต ไม่ใช่การรับรองความปลอดภัย การทดสอบแต่ละข้อมีขีดจำกัดการเติม 300 token ตรวจสอบคำตอบที่ไม่คาดคิดและการปฏิเสธที่อาจเกิดขึ้นด้วยตนเอง; การเปรียบเทียบนุ่มนวลควรให้คำอธิบาย AI ที่รับผิดชอบที่มีสาระสำคัญ ไม่ต้องการ stdin

## รูปแบบทั่วไปในตัวอย่างต่าง ๆ

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) รวมศูนย์การทำให้มาตรฐานของจุดสิ้นสุด, การแทนที่การปรับใช้, การตรวจสอบสิทธิ์โดยไม่ใช้คีย์ และตัวเลือกแชท:

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

ซัพพลายเออร์โทเค็นจะรีเฟรชโทเค็นการเข้าถึงตามต้องการ อย่าบันทึกโทเค็นหรือแทนที่ด้วยคีย์ API แต่ละโปรแกรมใช้ไคลเอนต์ของตัวเองซ้ำและปิดใน `finally` หรือผ่านตัวห่อ `AutoCloseable` ของตัวเอง; SDK `OpenAIClient` เองไม่ใช่ `AutoCloseable`

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) ต้องการคำตอบข้อความที่เสร็จสมบูรณ์และไม่ว่าง ตัวเลือกว่าง ปฏิเสธ กรอง และคำตอบที่ถูกตัดจะไม่พิมพ์ออกมาแบบเงียบ ๆ ว่าเป็นความสำเร็จ ตัวอย่าง AI ที่รับผิดชอบจัดการผลลัพธ์การกรอง/ปฏิเสธที่คาดไว้โดยชัดเจน ความล้มเหลวที่ไม่จัดการจะทำให้กระบวนการ Java/Maven ออกด้วยรหัสไม่เป็นศูนย์

**การลองใหม่อัตโนมัติของ SDK ถูกปิดใช้งาน** เพื่อให้จำนวนคำขอทำนายได้ในสภาพแวดล้อมที่มีอัตราการเรียกต่ำแบบแบ่งปัน คำขอเดาสถานะแต่ละรายการมีเวลาหมดเวลา 60 วินาที การได้มาซึ่งโทเค็นอาจใช้เวลามากขึ้น การจัดตารางในระดับแอปพลิเคชันต้องเคารพโควต้า อย่ารันซ้ำคำขอที่เสียหายแล้วอย่างไร้สติโดยไม่ตั้งใจ

## การทดสอบหน่วย

จากไดเรกทอรี examples:

```powershell
mvn -B -ntp clean test
```

การขนส่งทดแทนจะเปลี่ยนชั้น HTTP ของ SDK ทั้งหมด จับบอดี้คำขอแบบจริงที่ถูกซีเรียลไลซ์ และจัดส่งคำตอบที่คิวไว้ เปิดซ็อกเก็ตไม่ได้ รับโทเค็น Azure ไม่ได้ และล้มเหลวกับคำขอที่ไม่คาดคิด การทดสอบเหล่านี้ตรวจสอบพฤติกรรมแอปพลิเคชันและโปรโตคอล SDK ไม่ใช่คุณภาพโมเดลสดหรือความพร้อมของการปรับใช้

| ชุดการทดสอบ | ครอบคลุม |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | การทำให้มาตรฐาน/ปฏิเสธจุดสิ้นสุด, การแทนที่การปรับใช้, ตัวเลือกเหตุผลและ token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | เวิร์กโฟลว์การเติมคำทุกตัว, ประวัติข้อความ, การตัดเทิร์นที่สมบูรณ์, EOF, ความล้มเหลว |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | สคีมาของเครื่องมือ, อาร์กิวเมนต์ที่พิมพ์, คณิตศาสตร์, ID, ผลลัพธ์หลายเครื่องมือ, การติดตามผลที่ล้มเหลว |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | การค้นหาไฟล์, UTF-8, ขีดจำกัดขนาด, ข้อมูลตั้งฐาน, ข้อผิดพลาดอินพุตและ API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | หกการทดสอบทั้งหมด, กรองที่ชัดเจน, การจัดประเภทปฏิเสธ, HTTP 400 ปกติและความล้มเหลวอื่น ๆ |

สำหรับชุดใดชุดหนึ่ง ใช้คำสั่ง `mvn -B -ntp test "-Dtest=FunctionsAppTest"` ตัว fixture ที่ใช้ร่วมกันอยู่ใน [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)

## การตรวจสอบสดแบบลำดับ

การเรียกใช้งานสดแยกจากการทดสอบหน่วย ใช้คำสั่งต่อไปนี้ **ทีละคำสั่ง** จากรากรีโพสิทอรีหลังจากข้อมูลรับรองและการเข้าถึงการปรับใช้พร้อมใช้งานเท่านั้น ไม่ต้องการบริการหรือกระบวนการถาวร

สำหรับการปรับใช้ที่ใช้ร่วมกัน **10 คำขอต่อนาที** ให้จองโควต้าเพียงพอสำหรับโปรแกรมถัดไปทั้งหมดก่อนเริ่ม: 5, 4, 1 แล้ว 6 คำขอ เท่านั้น กระบวนการแบบลำดับไม่ได้รับประกันการปฏิบัติตามอัตราขีดจำกัด ควบคุมช่วงหนึ่งนาทีหมุนเวียนกับผู้เรียกใช้รายอื่นทั้งหมด; อย่าวางสี่การเรียกเป็นชุดที่ไม่มีช่องว่าง

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. การเติมคำ หลายเทิร์น และสองเทิร์นโต้ตอบ:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

ตรวจสอบหัวข้อทั้งสามส่วน คำตอบห้าข้อ คำตอบโต้ตอบสุดท้ายที่ระลึกถึง Ada, `Goodbye!` และรหัสออก 0 งบประมาณ: **5 คำขอ, สูงสุด 1,900 โทเค็นเสร็จสิ้น** สำหรับการรันขนาดเล็ก ให้ส่งผ่านเฉพาะ `exit`: 3 คำขอ / 900 โทเค็น แต่จะไม่ทำให้เกิดการคาดการณ์โต้ตอบ  

**2. ทั้งสองเวิร์กโฟลว์การเรียกใช้ฟังก์ชัน:**  

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

ตรวจสอบชื่อฟังก์ชันทั้งสอง สภาพอากาศจำลองที่ซีแอตเทิล ผลคำนวณ 36 คำตอบสุดท้ายสองคำตอบ และรหัสออก 0 งบประมาณ: **4 คำขอ, สูงสุด 1,200 โทเค็นเสร็จสิ้น**  

**3. คำตอบอ้างอิงจากเอกสาร:**  

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

ตรวจสอบเส้นทางเอกสาร คำตอบที่กล่าวถึง Microsoft Entra ID และรหัสออก 0 งบประมาณ: **1 คำขอ, สูงสุด 500 โทเค็นเสร็จสิ้น** [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) ที่มีอยู่เป็นไฟล์ป้อนข้อมูลเดียวที่ต้องการ การรันครั้งที่สองเสริมเติมเกี่ยวกับหัวข้อที่ไม่มีควรงดและเพิ่มหนึ่งคำขอ / 500 โทเค็น  

**4. ข้อสังเกตเกี่ยวกับ Responsible-AI:**  

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

ตรวจสอบหกหมวดหมู่และสรุปข้อสังเกต ทบทวนเนื้อหาที่สร้างขึ้น และต้องการรหัสออก 0 สำหรับการเสร็จสิ้นทางเทคนิค การออกจากกระบวนการสำเร็จไม่ได้รับรองความปลอดภัยของโมเดล งบประมาณ: **6 คำขอ, สูงสุด 1,800 โทเค็นเสร็จสิ้น**  

**รวมสี่คำสั่ง: 16 คำขอสนทนาและสูงสุด 5,400 โทเค็นเสร็จสิ้น** รวมถึงโทเค็นป้อนข้อมูล (รวมการสนทนาซ้ำและสคีม่า/ประวัติของเครื่องมือ) ไม่มีคำขอฝังอยู่จริง การใช้โทเค็นจริงขึ้นอยู่กับโมเดลและอาจต่ำกว่าโดยเฉพาะสำหรับพรอมต์ที่ถูกกรอง ค่าใช้จ่ายเป็นดอลลาร์ขึ้นกับราคาการปรับใช้ ไม่มีการประมาณราคาเงินที่ตายตัว สมมติว่าขีดจำกัดคำขอทั้งหมดเป็นไปโดยไม่ต้องรันซ้ำด้วยมือ ตรวจสอบ `$LASTEXITCODE` ทันทีหลังคำสั่งแต่ละคำสั่ง; ค่าที่ไม่ใช่ศูนย์หมายความว่าการรันไม่สำเร็จ  

## การแก้ไขปัญหา  

- **ไม่มี endpoint / 401 / 403:** ตั้งค่า endpoint ในกระบวนการเริ่มต้น ตรวจสอบการลงชื่อเข้าใช้ Azure ท้องถิ่นและบทบาททรัพยากรแบบสโคป และตรวจสอบการแทนที่สภาพแวดล้อมอัตลักษณ์ที่ไม่ตั้งใจ  
- **400 / 404:** ยืนยันว่าการปรับใช้งานมีอยู่และรองรับ Chat Completions ด้วยความพยายามใช้เหตุผล `none` ใช้ HTTPS resource root หรือ URL `/openai/v1` ไม่ใช่ URL การปรับใช้เดิม ข้อผิดพลาด 400 ทั่วไปคือความล้มเหลวทางเทคนิค ไม่ใช่การบล็อกด้านความปลอดภัย  
- **429:** ประสานงาน RPM และโควต้าท็อกเค็นแบบแชร์ก่อนลองใหม่ ตัวอย่างไม่ได้ตั้งค่าให้ลองใหม่โดยอัตโนมัติ  
- **`Incomplete chat response: length`:** เอาต์พุตถึงขีดจำกัดการเสร็จสิ้น ตรวจสอบการตอบสนองและพรอมต์ก่อนเพิ่มขีดจำกัดและงบประมาณที่ระบุไว้ อย่าบันทึกการรันที่ถูกตัดเป็นสำเร็จ  
- **ข้อผิดพลาดไฟล์หรือ stdin:** เรียกใช้จากไดเรกทอรีที่สนับสนุน หรือส่งเส้นทางเอกสารอย่างชัดเจน ให้คำถามที่ไม่ว่างเปล่าจากผู้อ่าน Completions อาจจบปกติโดย EOF หรือ `exit`  
- **ข้อผิดพลาดคอมไพล์:** ตรวจสอบ Java 21 หรือสูงกว่า แล้วรัน `mvn -B -ntp clean test` ใน PowerShell ให้ใส่เครื่องหมายคำพูดรอบอาร์กิวเมนต์ Maven ที่มี property จุด เช่น `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`  

## ขั้นตอนถัดไป  

ดำเนินการต่อไปยัง [บทที่ 4: ตัวอย่างเชิงปฏิบัติ](../04-PracticalSamples/README.md)  

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ปฏิเสธความรับผิดชอบ**:
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) ขณะที่เราพยายามให้ความถูกต้อง โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ แนะนำให้ใช้การแปลโดยมนุษย์มืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->