<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:30:42+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "th"
}
-->
# การตั้งค่าสภาพแวดล้อมการพัฒนาสำหรับ Generative AI ด้วย Java

> **เริ่มต้นอย่างรวดเร็ว**: เขียนโค้ดในคลาวด์ภายใน 2 นาที - ไปที่ [การตั้งค่า GitHub Codespaces](../../../02-SetupDevEnvironment) - ไม่ต้องติดตั้งในเครื่องและใช้โมเดลของ GitHub!

> **สนใจ Azure OpenAI?** ดู [คู่มือการตั้งค่า Azure OpenAI](getting-started-azure-openai.md) พร้อมขั้นตอนการสร้างทรัพยากร Azure OpenAI ใหม่

## สิ่งที่คุณจะได้เรียนรู้

- ตั้งค่าสภาพแวดล้อมการพัฒนาสำหรับแอปพลิเคชัน AI ด้วย Java
- เลือกและกำหนดค่าพื้นที่การพัฒนาที่คุณต้องการ (เน้นคลาวด์ด้วย Codespaces, dev container ในเครื่อง, หรือการตั้งค่าในเครื่องเต็มรูปแบบ)
- ทดสอบการตั้งค่าของคุณโดยการเชื่อมต่อกับ GitHub Models

## สารบัญ

- [สิ่งที่คุณจะได้เรียนรู้](../../../02-SetupDevEnvironment)
- [บทนำ](../../../02-SetupDevEnvironment)
- [ขั้นตอนที่ 1: ตั้งค่าสภาพแวดล้อมการพัฒนา](../../../02-SetupDevEnvironment)
  - [ตัวเลือก A: GitHub Codespaces (แนะนำ)](../../../02-SetupDevEnvironment)
  - [ตัวเลือก B: Local Dev Container](../../../02-SetupDevEnvironment)
  - [ตัวเลือก C: ใช้การติดตั้งในเครื่องที่มีอยู่แล้ว](../../../02-SetupDevEnvironment)
- [ขั้นตอนที่ 2: สร้าง GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [ขั้นตอนที่ 3: ทดสอบการตั้งค่าของคุณ](../../../02-SetupDevEnvironment)
- [การแก้ไขปัญหา](../../../02-SetupDevEnvironment)
- [สรุป](../../../02-SetupDevEnvironment)
- [ขั้นตอนถัดไป](../../../02-SetupDevEnvironment)

## บทนำ

บทนี้จะช่วยแนะนำคุณในการตั้งค่าสภาพแวดล้อมการพัฒนา เราจะใช้ **GitHub Models** เป็นตัวอย่างหลัก เนื่องจากใช้งานฟรี ตั้งค่าได้ง่ายเพียงแค่มีบัญชี GitHub ไม่ต้องใช้บัตรเครดิต และให้การเข้าถึงโมเดลหลายตัวสำหรับการทดลอง

**ไม่ต้องตั้งค่าในเครื่อง!** คุณสามารถเริ่มเขียนโค้ดได้ทันทีโดยใช้ GitHub Codespaces ซึ่งให้สภาพแวดล้อมการพัฒนาเต็มรูปแบบในเบราว์เซอร์ของคุณ

<img src="./images/models.webp" alt="ภาพหน้าจอ: GitHub Models" width="50%">

เราแนะนำให้ใช้ [**GitHub Models**](https://github.com/marketplace?type=models) สำหรับคอร์สนี้เพราะว่า:
- **ฟรี** สำหรับการเริ่มต้น
- **ง่าย** ในการตั้งค่าเพียงแค่มีบัญชี GitHub
- **ไม่ต้องใช้บัตรเครดิต**
- **มีโมเดลหลายตัว** สำหรับการทดลอง

> **หมายเหตุ**: GitHub Models ที่ใช้ในการฝึกอบรมนี้มีข้อจำกัดฟรีดังนี้:
> - 15 คำขอต่อหนึ่งนาที (150 ต่อวัน)
> - ~8,000 คำเข้า, ~4,000 คำออกต่อคำขอ
> - 5 คำขอพร้อมกัน
> 
> สำหรับการใช้งานในระดับผลิตภัณฑ์ อัปเกรดเป็น Azure AI Foundry Models ด้วยบัญชี Azure ของคุณ โค้ดของคุณไม่จำเป็นต้องเปลี่ยน ดู [เอกสาร Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models)

## ขั้นตอนที่ 1: ตั้งค่าสภาพแวดล้อมการพัฒนา

<a name="quick-start-cloud"></a>

เราได้สร้าง dev container ที่กำหนดค่าไว้ล่วงหน้าเพื่อลดเวลาในการตั้งค่าและรับรองว่าคุณมีเครื่องมือที่จำเป็นทั้งหมดสำหรับคอร์ส Generative AI ด้วย Java เลือกวิธีการพัฒนาที่คุณต้องการ:

### ตัวเลือกการตั้งค่าสภาพแวดล้อม:

#### ตัวเลือก A: GitHub Codespaces (แนะนำ)

**เริ่มเขียนโค้ดภายใน 2 นาที - ไม่ต้องตั้งค่าในเครื่อง!**

1. Fork repository นี้ไปยังบัญชี GitHub ของคุณ
   > **หมายเหตุ**: หากคุณต้องการแก้ไขการตั้งค่าพื้นฐาน โปรดดูที่ [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. คลิก **Code** → แท็บ **Codespaces** → **...** → **New with options...**
3. ใช้ค่าตั้งต้น – จะเลือก **Dev container configuration**: **Generative AI Java Development Environment** devcontainer ที่สร้างขึ้นสำหรับคอร์สนี้
4. คลิก **Create codespace**
5. รอประมาณ ~2 นาทีเพื่อให้สภาพแวดล้อมพร้อมใช้งาน
6. ดำเนินการต่อไปที่ [ขั้นตอนที่ 2: สร้าง GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="ภาพหน้าจอ: เมนูย่อย Codespaces" width="50%">

<img src="./images/image.png" alt="ภาพหน้าจอ: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="ภาพหน้าจอ: ตัวเลือกการสร้าง Codespace" width="50%">

> **ข้อดีของ Codespaces**:
> - ไม่ต้องติดตั้งในเครื่อง
> - ใช้งานได้บนอุปกรณ์ใดก็ได้ที่มีเบราว์เซอร์
> - กำหนดค่าล่วงหน้าด้วยเครื่องมือและ dependencies ทั้งหมด
> - ฟรี 60 ชั่วโมงต่อเดือนสำหรับบัญชีส่วนตัว
> - สภาพแวดล้อมที่สม่ำเสมอสำหรับผู้เรียนทุกคน

#### ตัวเลือก B: Local Dev Container

**สำหรับนักพัฒนาที่ชอบการพัฒนาในเครื่องด้วย Docker**

1. Fork และ clone repository นี้ไปยังเครื่องของคุณ
   > **หมายเหตุ**: หากคุณต้องการแก้ไขการตั้งค่าพื้นฐาน โปรดดูที่ [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. ติดตั้ง [Docker Desktop](https://www.docker.com/products/docker-desktop/) และ [VS Code](https://code.visualstudio.com/)
3. ติดตั้ง [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ใน VS Code
4. เปิดโฟลเดอร์ repository ใน VS Code
5. เมื่อได้รับการแจ้งเตือน คลิก **Reopen in Container** (หรือใช้ `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. รอให้ container สร้างและเริ่มต้น
7. ดำเนินการต่อไปที่ [ขั้นตอนที่ 2: สร้าง GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="ภาพหน้าจอ: การตั้งค่า Dev container" width="50%">

<img src="./images/image-3.png" alt="ภาพหน้าจอ: การสร้าง Dev container เสร็จสมบูรณ์" width="50%">

#### ตัวเลือก C: ใช้การติดตั้งในเครื่องที่มีอยู่แล้ว

**สำหรับนักพัฒนาที่มีสภาพแวดล้อม Java อยู่แล้ว**

ข้อกำหนดเบื้องต้น:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) หรือ IDE ที่คุณชอบ

ขั้นตอน:
1. Clone repository นี้ไปยังเครื่องของคุณ
2. เปิดโปรเจกต์ใน IDE ของคุณ
3. ดำเนินการต่อไปที่ [ขั้นตอนที่ 2: สร้าง GitHub Token](../../../02-SetupDevEnvironment)

> **เคล็ดลับ**: หากคุณมีเครื่องที่มีสเปคต่ำแต่ต้องการใช้ VS Code ในเครื่อง ให้ใช้ GitHub Codespaces! คุณสามารถเชื่อมต่อ VS Code ในเครื่องของคุณกับ Codespace ที่โฮสต์ในคลาวด์เพื่อให้ได้ประสบการณ์ที่ดีที่สุด

<img src="./images/image-2.png" alt="ภาพหน้าจอ: สร้าง instance devcontainer ในเครื่อง" width="50%">

## ขั้นตอนที่ 2: สร้าง GitHub Personal Access Token

1. ไปที่ [GitHub Settings](https://github.com/settings/profile) และเลือก **Settings** จากเมนูโปรไฟล์ของคุณ
2. ในแถบด้านซ้าย คลิก **Developer settings** (มักจะอยู่ด้านล่าง)
3. ภายใต้ **Personal access tokens** คลิก **Fine-grained tokens** (หรือใช้ [ลิงก์นี้](https://github.com/settings/personal-access-tokens))
4. คลิก **Generate new token**
5. ใน "Token name" ให้ตั้งชื่อที่อธิบายได้ (เช่น `GenAI-Java-Course-Token`)
6. ตั้งวันหมดอายุ (แนะนำ: 7 วันเพื่อความปลอดภัย)
7. ใน "Resource owner" เลือกบัญชีผู้ใช้ของคุณ
8. ใน "Repository access" เลือก repository ที่คุณต้องการใช้กับ GitHub Models (หรือ "All repositories" หากจำเป็น)
9. ใน "Repository permissions" หา **Models** และตั้งค่าเป็น **Read and write**
10. คลิก **Generate token**
11. **คัดลอกและบันทึก token ของคุณทันที** – คุณจะไม่สามารถดูได้อีก!

> **เคล็ดลับด้านความปลอดภัย**: ใช้ scope ที่จำเป็นขั้นต่ำและระยะเวลาหมดอายุที่สั้นที่สุดเท่าที่จะเป็นไปได้สำหรับ access tokens ของคุณ

## ขั้นตอนที่ 3: ทดสอบการตั้งค่าของคุณด้วยตัวอย่าง GitHub Models

เมื่อสภาพแวดล้อมการพัฒนาของคุณพร้อมแล้ว มาทดสอบการเชื่อมต่อ GitHub Models ด้วยแอปพลิเคชันตัวอย่างใน [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models)

1. เปิด terminal ในสภาพแวดล้อมการพัฒนาของคุณ
2. ไปที่ตัวอย่าง GitHub Models:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. ตั้งค่า GitHub token ของคุณเป็น environment variable:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. รันแอปพลิเคชัน:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

คุณควรเห็นผลลัพธ์ที่คล้ายกับ:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### ทำความเข้าใจโค้ดตัวอย่าง

ก่อนอื่น มาทำความเข้าใจสิ่งที่เราเพิ่งรัน ตัวอย่างใน `examples/github-models` ใช้ OpenAI Java SDK เพื่อเชื่อมต่อกับ GitHub Models:

**สิ่งที่โค้ดนี้ทำ:**
- **เชื่อมต่อ** กับ GitHub Models โดยใช้ personal access token ของคุณ
- **ส่ง** ข้อความง่ายๆ "Say Hello World!" ไปยังโมเดล AI
- **รับ** และแสดงผลการตอบกลับของ AI
- **ตรวจสอบ** ว่าการตั้งค่าของคุณทำงานได้ถูกต้อง

**Dependency หลัก** (ใน `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**โค้ดหลัก** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## สรุป

เยี่ยม! ตอนนี้คุณได้ตั้งค่าทุกอย่างเรียบร้อยแล้ว:

- สร้าง GitHub Personal Access Token พร้อมสิทธิ์ที่เหมาะสมสำหรับการเข้าถึงโมเดล AI
- ตั้งค่าสภาพแวดล้อมการพัฒนา Java ของคุณ (ไม่ว่าจะเป็น Codespaces, dev containers, หรือในเครื่อง)
- เชื่อมต่อกับ GitHub Models โดยใช้ OpenAI Java SDK สำหรับการพัฒนา AI ฟรี
- ทดสอบว่าทุกอย่างทำงานได้ด้วยตัวอย่างง่ายๆ ที่พูดคุยกับโมเดล AI

## ขั้นตอนถัดไป

[บทที่ 3: เทคนิคหลักของ Generative AI](../03-CoreGenerativeAITechniques/README.md)

## การแก้ไขปัญหา

มีปัญหา? นี่คือปัญหาทั่วไปและวิธีแก้ไข:

- **Token ใช้งานไม่ได้?** 
  - ตรวจสอบว่าคุณคัดลอก token ทั้งหมดโดยไม่มีช่องว่างเพิ่มเติม
  - ตรวจสอบว่า token ถูกตั้งค่าเป็น environment variable อย่างถูกต้อง
  - ตรวจสอบว่า token ของคุณมีสิทธิ์ที่ถูกต้อง (Models: Read and write)

- **Maven ไม่พบ?** 
  - หากใช้ dev containers/Codespaces Maven ควรติดตั้งไว้ล่วงหน้า
  - สำหรับการตั้งค่าในเครื่อง ตรวจสอบว่า Java 21+ และ Maven 3.9+ ถูกติดตั้ง
  - ลองใช้ `mvn --version` เพื่อตรวจสอบการติดตั้ง

- **ปัญหาการเชื่อมต่อ?** 
  - ตรวจสอบการเชื่อมต่ออินเทอร์เน็ตของคุณ
  - ตรวจสอบว่า GitHub สามารถเข้าถึงได้จากเครือข่ายของคุณ
  - ตรวจสอบว่าไม่มีไฟร์วอลล์ที่บล็อก endpoint ของ GitHub Models

- **Dev container ไม่เริ่มต้น?** 
  - ตรวจสอบว่า Docker Desktop กำลังทำงาน (สำหรับการพัฒนาในเครื่อง)
  - ลองสร้าง container ใหม่: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **ข้อผิดพลาดการคอมไพล์แอปพลิเคชัน?**
  - ตรวจสอบว่าคุณอยู่ในไดเรกทอรีที่ถูกต้อง: `02-SetupDevEnvironment/examples/github-models`
  - ลองทำความสะอาดและสร้างใหม่: `mvn clean compile`

> **ต้องการความช่วยเหลือ?**: ยังมีปัญหาอยู่? เปิด issue ใน repository แล้วเราจะช่วยคุณ

**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้การแปลมีความถูกต้องมากที่สุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลภาษามนุษย์ที่มีความเชี่ยวชาญ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดซึ่งเกิดจากการใช้การแปลนี้