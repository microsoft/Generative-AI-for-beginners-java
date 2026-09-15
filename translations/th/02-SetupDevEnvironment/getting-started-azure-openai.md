# การตั้งค่าสภาพแวดล้อมการพัฒนาสำหรับ Azure AI Foundry

> ไกด์นี้จะตั้งค่าโมเดล **Azure AI Foundry** สำหรับแอป AI ภาษา Java ในหลักสูตรนี้ โดยใช้การตรวจสอบสิทธิ์แบบ **ไม่มีคีย์** (Microsoft Entra ID) — ไม่มีคีย์ API ที่ต้องจัดการ หากคุณยังใหม่กับเครื่องมือนี้ เริ่มจาก [คู่มือสภาพแวดล้อมการพัฒนา](./README.md)

ไกด์นี้จะตั้งค่าโมเดล **Azure AI Foundry** สำหรับแอป AI ภาษา Java ในหลักสูตรนี้ คุณมี 2 ทางเลือก:

- **ตัวเลือก A — จัดเตรียมด้วย `azd` + Bicep (แนะนำ):** คำสั่งเดียวสำหรับการปรับใช้บัญชี Foundry และโมเดลเป็นโค้ด ไม่ต้องคลิกในพอร์ทัล
- **ตัวเลือก B — สร้างทรัพยากรด้วยตนเอง** ในพอร์ทัล Azure AI Foundry

ทั้งสองทางเลือกใช้ **การตรวจสอบสิทธิ์แบบไม่มีคีย์** (Microsoft Entra ID) — ไม่มีคีย์ API ให้คัดลอกหรือรั่วไหล

## สารบัญ

- [สิ่งที่จะถูกสร้างขึ้น](#สิ่งที่จะถูกสร้างขึ้น)
- [สิ่งที่ต้องเตรียม](#สิ่งที่ต้องเตรียม)
- [ตัวเลือก A: จัดเตรียมด้วย azd + Bicep (แนะนำ)](#option-a-provision-with-azd--bicep-recommended)
- [ตัวเลือก B: สร้างทรัพยากรด้วยตนเอง](#ตัวเลือก-b-สร้างทรัพยากรด้วยตนเอง)
- [ตั้งค่าสภาพแวดล้อมของคุณ](#ตั้งค่าสภาพแวดล้อมของคุณ)
- [ทดสอบการตั้งค่าของคุณ](#ทดสอบการตั้งค่าของคุณ)
- [ขั้นตอนต่อไป?](#ขั้นตอนต่อไป)
- [ทรัพยากร](#ทรัพยากร)
- [ทรัพยากรเพิ่มเติม](#ทรัพยากรเพิ่มเติม)

## สิ่งที่จะถูกสร้างขึ้น

แบบฟอร์ม Bicep ใน [`infra/`](../../../02-SetupDevEnvironment/infra) จะจัดเตรียม:

- บัญชี **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, ประเภท `AIServices`) พร้อมโปรเจกต์
- การปรับใช้ **แชท** - GPT-5.6 Luna (`gpt-5.6-luna`), เวอร์ชัน `2026-07-09`, กับความจุ `GlobalStandard` `10` (10 คำขอต่อวินาทีและ 10,000 โทเคนต่อนาทีสำหรับโมเดลนี้)
- การปรับใช้ **embedding** - `text-embedding-3-small`, เวอร์ชัน `1` (ใช้ในบทต่อไป)
- การกำหนดบทบาทแบบไม่ใช้คีย์ (`Cognitive Services OpenAI User`) เพื่อที่คุณจะได้ลงชื่อเข้าใช้ด้วย `az login` แทนการจัดการคีย์

## สิ่งที่ต้องเตรียม

- [สมัครสมาชิก Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) และ [Maven 3.9+](https://maven.apache.org/download.cgi)

## ตัวเลือก A: จัดเตรียมด้วย azd + Bicep (แนะนำ)

จากโฟลเดอร์ `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# ลงชื่อเข้าใช้ (ทั้งสองเครื่องมือ)
azd auth login
az login

# จัดเตรียมบัญชี Foundry + การปรับใช้โมเดล
azd up
```

`azd` จะถามหา **ชื่่อสภาพแวดล้อม** (เช่น `genai-java`), **การสมัครสมาชิก**, และ **ภูมิภาค** เลือกการสมัครสมาชิกของคุณและภูมิภาคที่ `gpt-5.6-luna` และ `text-embedding-3-small` มีให้บริการ เช่น `eastus2` ยืนยันว่าการสมัครสมาชิกมีโควต้าที่เพียงพอสำหรับโมเดลและประเภทการปรับใช้ในภูมิภาคนั้น; ความพร้อมใช้งานและโควต้าแตกต่างกันตามการสมัครสมาชิก

เมื่อการจัดเตรียมเสร็จสิ้นแล้ว azd จะ:

1. ปรับใช้ทุกอย่างที่กำหนดใน [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep)
2. รัน postprovision hook ที่เขียน [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) ด้วยชื่อ endpoint และการปรับใช้ของคุณ (ไม่มีข้อมูลลับ)

> **เคล็ดลับ:** รัน `azd up` ใหม่เมื่อใดก็ได้เพื่อใช้การเปลี่ยนแปลง รัน `azd down` เพื่อลบทุกอย่างและหยุดค่าใช้จ่าย

เพื่อดูการตั้งค่าที่สร้างขึ้น:

```bash
azd env get-values
```

ข้ามไปที่ [ทดสอบการตั้งค่าของคุณ](#ทดสอบการตั้งค่าของคุณ)

## ตัวเลือก B: สร้างทรัพยากรด้วยตนเอง

ชอบใช้พอร์ทัลไหม? สร้างทรัพยากรด้วยตนเอง:

1. ไปที่ [พอร์ทัล Azure AI Foundry](https://ai.azure.com/) และลงชื่อเข้าใช้
2. **สร้างโปรเจกต์** (ซึ่งจะสร้างทรัพยากร AI Foundry ด้วย) ตั้งชื่อเช่น `GenAIJava`
3. ในโปรเจกต์ของคุณ เปิด **Models + endpoints** → **Deploy model** → **Deploy base model**
4. ปรับใช้ **GPT-5.6 Luna** (ชื่อโมเดลและการปรับใช้ `gpt-5.6-luna`, เวอร์ชัน `2026-07-09`) ด้วยความจุ **Global Standard** `10` ทำซ้ำสำหรับ **text-embedding-3-small**, เวอร์ชัน `1` ถ้าคุณต้องการตัวอย่าง embedding
5. จาก **ภาพรวม** คัดลอก **endpoint** (เช่น `https://<resource>.openai.azure.com/`)
6. ให้สิทธิ์เข้าถึงแบบไม่มีคีย์: บนทรัพยากร เปิด **การควบคุมการเข้าถึง (IAM)** → **เพิ่มการกำหนดบทบาท** → กำหนดบทบาท **Cognitive Services OpenAI User** ให้กับบัญชีของคุณ

> **ยังมีปัญหาอยู่ไหม?** ดู [เอกสาร Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)

## ตั้งค่าสภาพแวดล้อมของคุณ

**ถ้าคุณใช้ตัวเลือก A (`azd up`)** ไฟล์การตั้งค่าของคุณถูกเขียนไว้แล้ว — ไม่มีอะไรต้องตั้งค่า ข้ามไปที่ [ทดสอบการตั้งค่าของคุณ](#ทดสอบการตั้งค่าของคุณ)

**ถ้าคุณใช้ตัวเลือก B (ด้วยตนเอง)** สร้างไฟล์ `.env` ของตัวอย่างด้วยตัวเอง:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

แก้ไข `.env` ด้วย endpoint ของคุณ (ไม่มีคีย์ — การตรวจสอบสิทธิ์แบบไม่มีคีย์):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

ใช้ endpoint ของ Azure OpenAI ของทรัพยากร ไม่ใช่ URL ของโปรเจกต์ แอป basic-chat จะแปลงเป็น `/openai/v1` และตั้งค่าไคลเอนต์ bearer-token ที่ชัดเจน; ไม่ต้องใช้คีย์ API

> **หมายเหตุด้านความปลอดภัย:** ไม่มีคีย์ API ให้เก็บ คุณตรวจสอบสิทธิ์ด้วย Microsoft Entra ID ผ่าน `az login` (ที่เครื่องคุณ) หรือ managed identity (ใน Azure) ไฟล์ `.env` มีเฉพาะการตั้งค่าไม่ลับและถูกละเว้นโดย `.gitignore` แล้ว

## ทดสอบการตั้งค่าของคุณ

ตรวจสอบให้แน่ใจว่าคุณได้ลงชื่อเข้าใช้เพื่อให้การตรวจสอบสิทธิ์แบบไม่มีคีย์สามารถรับโทเคนได้ จากนั้นรันตัวอย่าง:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ถ้าคุณยังไม่ได้เข้าสู่ระบบ
mvn clean spring-boot:run
```

คุณควรเห็นการตอบกลับจากโมเดล `gpt-5.6-luna` รันตัวอย่างต่อเนื่องกันเพื่อให้อยู่ในโควต้าเริ่มต้นขนาดเล็ก หากได้รับ HTTP 429 ให้รอช่วงเวลาที่จะลองใหม่ก่อนลองอีกครั้ง

> **ผู้ใช้ VS Code:** กด `F5` เพื่อรัน แอปรองรับการโหลด `.env` อัตโนมัติ

> **ตัวอย่างเต็ม:** ดู [ตัวอย่าง Basic Chat กับ Azure AI Foundry](./examples/basic-chat-azure/README.md) สำหรับรายละเอียดและวิธีแก้ไขปัญหา

## ขั้นตอนต่อไป?

หลังจากจัดเตรียมและรันตัวอย่างได้สำเร็จ คุณจะมี:
- Azure AI Foundry พร้อมปรับใช้ `gpt-5.6-luna` และ `text-embedding-3-small`
- การตรวจสอบสิทธิ์แบบไม่มีคีย์ (Microsoft Entra ID) — ไม่มีคีย์ให้จัดการ
- ไฟล์ `.env` ในเครื่องพร้อม endpoint และชื่อการปรับใช้ของคุณ
- สภาพแวดล้อมการพัฒนาภาษา Java ที่พร้อมใช้งาน

**ดำเนินการต่อไปยัง** [บทที่ 3: เทคนิค Generative AI หลัก](../03-CoreGenerativeAITechniques/README.md) เพื่อเริ่มสร้างแอป AI!

## ทรัพยากร

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [การตรวจสอบสิทธิ์แบบไม่มีคีย์กับ Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [เอกสาร Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [การเปลี่ยนผ่าน Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK อย่างเป็นทางการกับ Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## ทรัพยากรเพิ่มเติม

- [ดาวน์โหลด VS Code](https://code.visualstudio.com/Download)
- [รับ Docker Desktop](https://www.docker.com/products/docker-desktop)
- [การกำหนดค่า Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ปฏิเสธความรับผิดชอบ**:
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) ขณะที่เราพยายามให้ความถูกต้อง โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาต้นทางควรถูกพิจารณาเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ แนะนำให้ใช้การแปลโดยมนุษย์มืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดที่เกิดขึ้นจากการใช้การแปลนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->