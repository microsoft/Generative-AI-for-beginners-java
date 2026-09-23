# สนทนาพื้นฐานกับ Azure AI Foundry - ตัวอย่างแบบครบวงจร

ตัวอย่างนี้เป็นแอปพลิเคชัน Spring Boot ง่าย ๆ ที่เชื่อมต่อกับโมเดล **Azure AI Foundry** โดยใช้ **การตรวจสอบสิทธิ์แบบไม่ใช้คีย์ (keyless authentication)** (Microsoft Entra ID) และทดสอบการตั้งค่าของคุณ มันเก็บ `ChatClient` ของ Spring AI ซึ่งรองรับโดย **OpenAI Java SDK อย่างเป็นทางการ** และจุดสิ้นสุด **Azure OpenAI v1**

เวอร์ชันใน [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) คือ Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, และ dotenv-java **3.2.0** ตัวอย่างใช้ `spring-ai-starter-model-openai` และระบุ `openai-java` กับ `azure-identity` อย่างชัดเจน; Spring AI 2 ได้ลบตัวสตาร์ท Azure OpenAI แบบเก่าออกไป

## สารบัญ

- [ข้อกำหนดเบื้องต้น](#ข้อกำหนดเบื้องต้น)
- [เริ่มต้นอย่างรวดเร็ว](#เริ่มต้นอย่างรวดเร็ว)
- [การทำงานของการตรวจสอบสิทธิ์](#การทำงานของการตรวจสอบสิทธิ์)
- [การรันแอปพลิเคชัน](#การรันแอปพลิเคชัน)
  - [การใช้ Maven](#การใช้-maven)
  - [การใช้ VS Code](#การใช้-vs-code)
  - [ผลลัพธ์ที่คาดหวัง](#ผลลัพธ์ที่คาดหวัง)
- [การอ้างอิงการตั้งค่า](#การอ้างอิงการตั้งค่า)
  - [ตัวแปรสภาพแวดล้อม](#ตัวแปรสภาพแวดล้อม)
  - [การตั้งค่า Spring](#การตั้งค่า-spring)
- [การแก้ปัญหา](#การแก้ปัญหา)
  - [ปัญหาทั่วไป](#ปัญหาทั่วไป)
  - [โหมดดีบัก](#โหมดดีบัก)
- [ขั้นตอนถัดไป](#ขั้นตอนถัดไป)
- [ทรัพยากร](#ทรัพยากร)

## ข้อกำหนดเบื้องต้น

ก่อนรันตัวอย่างนี้ ให้แน่ใจว่าคุณมี:

- แหล่งข้อมูล Azure AI Foundry ที่มีการปรับใช้ `gpt-5.6-luna` - จัดเตรียมด้วย `azd up` หรือด้วยตนเองผ่าน [คู่มือการตั้งค่า Azure AI Foundry](../../getting-started-azure-openai.md)
- บทบาท **Cognitive Services OpenAI User** บนแหล่งข้อมูลนั้น (เทมเพลต Bicep กำหนดให้โดยอัตโนมัติ)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), ลงชื่อเข้าใช้ด้วย `az login`
- Java 21+ และ Maven 3.9+

> **ไม่ต้องใช้คีย์ API** — การตรวจสอบสิทธิ์เป็นแบบไม่มีคีย์ผ่าน Microsoft Entra ID

## เริ่มต้นอย่างรวดเร็ว

```bash
# 1. ไปที่โครงการ
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. ลงชื่อเข้าใช้เพื่อให้ระบบแยกยืนยันตัวตนแบบไม่ต้องใช้คีย์สามารถรับโทเค็นได้
az login

# 3. กำหนดค่าจุดเชื่อมต่อ
#    - หากคุณรัน `azd up` ไฟล์ .env จะถูกเขียนให้คุณ (ข้ามขั้นตอนนี้)
#    - หากไม่ใช่ ให้คัดลอกเทมเพลตและตั้งค่า AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. รันแอปพลิเคชัน
mvn spring-boot:run
```

## การทำงานของการตรวจสอบสิทธิ์

ตัวอย่างนี้ตรวจสอบสิทธิ์ด้วย **Microsoft Entra ID** — ไม่มีคีย์ API

แอปพลิเคชันกำหนดการตรวจสอบสิทธิ์อย่างชัดเจนใน [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` สร้าง `BearerTokenCredential` โดยใช้ `AuthenticationUtil.getBearerTokenSupplier` พร้อม `DefaultAzureCredential` และช่วง `https://ai.azure.com/.default`
2. `azureOpenAiClient()` สร้าง `OpenAIClient` ด้วย `OpenAIOkHttpClient.builder()`, แก้ไขปลายทางของแหล่งข้อมูลเป็น `/openai/v1` และจัดส่งข้อมูลประจำตัวแบบ bearer ด้วย `.credential(...)`
3. `azureChatModel()` จัดส่งไคลเอนต์นั้นไปยัง `OpenAiChatModel` ของ Spring AI ซึ่งสนับสนุน `ChatClient` ของบทเรียนนี้

beans เหล่านี้ที่กำหนดอย่างชัดเจนป้องกันไม่ให้ `OPENAI_API_KEY` ทั่วโลกแทรกแซงการตรวจสอบสิทธิ์ของ Azure การละเว้นคีย์ API จาก YAML เพียงอย่างเดียวไม่ใช่การตั้งค่าการตรวจสอบสิทธิ์ `DefaultAzureCredential` สามารถใช้เซสชัน `az login` ของคุณในเครื่องหรือ managed identity ใน Azure; ตัวตนที่เลือกต้องมีบทบาทในแหล่งข้อมูลตามที่ระบุไว้ข้างต้น

## การรันแอปพลิเคชัน

### การใช้ Maven

```bash
mvn spring-boot:run
```

### การใช้ VS Code

1. เปิดโปรเจกต์ใน VS Code
2. กด `F5` หรือใช้แผง "Run and Debug"
3. เลือกการตั้งค่า "Spring Boot-BasicChatApplication"

> **หมายเหตุ**: แอปโหลดไฟล์ `.env` จากไดเรกทอรีการทำงาน รวมถึงเมื่อเปิดจาก VS Code

### ผลลัพธ์ที่คาดหวัง

ผลลัพธ์ตัวอย่างหลังการรันสำเร็จ (ละบันทึกการเริ่มทำงาน; ข้อความตอบกลับอาจแตกต่างกัน):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## การอ้างอิงการตั้งค่า

### ตัวแปรสภาพแวดล้อม

| ตัวแปร | คำอธิบาย | จำเป็น | ตัวอย่าง |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL จุดสิ้นสุด Foundry (Azure OpenAI) | ใช่ | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | ชื่อการปรับใช้โมเดลแชท | ไม่ | `gpt-5.6-luna` (ค่าเริ่มต้น) |

> ไม่มีตัวแปรคีย์ API — การตรวจสอบสิทธิ์เป็นแบบไม่มีคีย์ (Microsoft Entra ID ผ่าน `az login`)

### การตั้งค่า Spring

การตั้งค่าใน [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ใช้คำนำหน้า `spring.ai.openai` และคุณสมบัติแชทแบบเรียบ (ไม่มีบล็อก `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` คือ **ชื่อการปรับใช้ของ Azure** การตรวจสอบสิทธิ์มาจาก beans ที่อธิบายไว้ข้างต้น ไม่ใช่การตั้งค่า `api-key` บทเรียนปิดการใช้งาน reasoning และจำกัดโทเค็นการเติมที่ 500; ปล่อยให้ `temperature` และ `max-tokens` แบบเก่าไม่ได้ตั้งค่า

Microsoft แนะนำ [OpenAI SDK อย่างเป็นทางการกับ Azure OpenAI v1 และ Responses API สำหรับแอปใหม่](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) Chat Completions ยังคงรองรับบทเรียนที่ใช้ข้อความนี้ สำหรับ GPT-5.6 คำขอที่รวมเครื่องมือใน Chat Completions ต้องตั้งค่า `reasoning_effort` เป็น `none`; ใช้ Responses เมื่อรวม reasoning กับเครื่องมือ ดู [การเรียกเครื่องมือกับโมเดล reasoning](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)

## การแก้ปัญหา

### ปัญหาทั่วไป

<details>
<summary><strong>ข้อผิดพลาด: 401 / "PermissionDenied" / token errors</strong></summary>

- รัน `az login` — การตรวจสอบสิทธิ์แบบไม่มีคีย์ต้องการการลงชื่อเข้าใช้งานที่ใช้งานอยู่เพื่อนำโทเค็น
- ตรวจสอบบัญชีของคุณว่ามีบทบาท **Cognitive Services OpenAI User** บนแหล่งข้อมูล
- หากเพิ่งกำหนดบทบาท รอหน่อยเพื่อให้แผ่กระจาย
- ยืนยันว่าคุณอยู่ใน tenant/การสมัครสมาชิกที่ถูกต้อง (`az account show`)
</details>

<details>
<summary><strong>ข้อผิดพลาด: "The endpoint is not valid" / ความผิดพลาดในการเชื่อมต่อ</strong></summary>

- ตรวจสอบว่า `AZURE_OPENAI_ENDPOINT` คือ URL ฐานเต็ม (เช่น `https://your-resource.openai.azure.com/`)
- ตรวจสอบความสอดคล้องของเครื่องหมายทับหลัง
- ยืนยันว่าสิ้นสุดตรงกับแหล่งข้อมูลที่จัดเตรียมไว้ (`azd env get-values`)
</details>

<details>
<summary><strong>ข้อผิดพลาด: "The deployment was not found"</strong></summary>

- ยืนยันว่าสำหรับ `AZURE_OPENAI_DEPLOYMENT` ตรงกับชื่อการปรับใช้ใน Azure
- ตรวจสอบว่าโมเดลถูกปรับใช้อย่างสำเร็จและใช้งานได้
- ชื่อปรับใช้เริ่มต้นคือ `gpt-5.6-luna`
</details>

<details>
<summary><strong>ข้อผิดพลาด: 429 / เกินขีดจำกัดอัตรา</strong></summary>

- การปรับใช้ GPT-5.6 Luna เริ่มต้นมีความจุมาตรฐานทั่วโลก 10: 10 คำขอต่อนาที และ 10,000 โทเค็นต่อนาที
- รันตัวอย่างทีละรายการและรอช่วงเวลาที่บริการให้ลองใหม่ก่อนลองอีกครั้ง
- ตัวอย่างพื้นฐานนี้ปิดการลองใหม่โดยอัตโนมัติของ SDK ดังนั้นคำขอที่ล้มเหลวจะแจ้งผลลัพธ์ทันที
</details>

<details>
<summary><strong>VS Code: ตัวแปรสภาพแวดล้อมไม่โหลด</strong></summary>

- ตรวจสอบให้แน่ใจว่าไฟล์ `.env` อยู่ในไดเรกทอรีรากของโปรเจกต์ (ระดับเดียวกับ `pom.xml`)
- ลองรัน `mvn spring-boot:run` ในเทอร์มินัลแบบบูรณาการของ VS Code
- ตรวจสอบว่า extension Java ของ VS Code ติดตั้งเรียบร้อยแล้ว
</details>

### โหมดดีบัก

เพื่อเปิดใช้งานการบันทึกรายละเอียด ให้ยกเลิกการคอมเมนต์บรรทัดเหล่านี้ใน [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## ขั้นตอนถัดไป

**การตั้งค่าสมบูรณ์!** ดำเนินการเดินทางเรียนรู้ของคุณต่อไป:

[บทที่ 3: เทคนิคแกนหลักของ Generative AI](../../../03-CoreGenerativeAITechniques/README.md)

## ทรัพยากร

- [การเปลี่ยนผ่าน Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK อย่างเป็นทางการกับ Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [การตรวจสอบสิทธิ์แบบไม่มีคีย์กับ Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [พอร์ทัล Azure AI Foundry](https://ai.azure.com/)
- [เอกสาร Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ปฏิเสธความรับผิดชอบ**:
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) ขณะที่เราพยายามให้ความถูกต้อง โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ แนะนำให้ใช้การแปลโดยมนุษย์มืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->