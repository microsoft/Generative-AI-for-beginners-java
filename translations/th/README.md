<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:55:57+00:00",
  "source_file": "README.md",
  "language_code": "th"
}
-->
# ปัญญาประดิษฐ์เชิงสร้างสรรค์สำหรับผู้เริ่มต้น - ฉบับ Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ปัญญาประดิษฐ์เชิงสร้างสรรค์สำหรับผู้เริ่มต้น - ฉบับ Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.th.png)

**เวลาที่ต้องใช้**: เวิร์กชอปทั้งหมดสามารถทำออนไลน์ได้โดยไม่ต้องตั้งค่าในเครื่องท้องถิ่น การตั้งค่าสิ่งแวดล้อมใช้เวลา 2 นาที โดยการสำรวจตัวอย่างต้องใช้เวลา 1-3 ชั่วโมง ขึ้นกับความลึกของการสำรวจ

> **เริ่มต้นอย่างรวดเร็ว** 

1. Fork repository นี้ไปยังบัญชี GitHub ของคุณ
2. คลิก **Code** → แท็บ **Codespaces** → **...** → **New with options...**
3. ใช้ค่าเริ่มต้น – สิ่งนี้จะเลือก Development container ที่สร้างขึ้นสำหรับคอร์สนี้
4. คลิก **Create codespace**
5. รอ ~2 นาทีเพื่อให้สภาพแวดล้อมพร้อมใช้งาน
6. ข้ามไปที่ [→ เริ่มตัวอย่างแรก](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **ต้องการโคลนลงเครื่องไหม?**
>
> Repository นี้มีการแปลเป็นภาษาต่างๆ มากกว่า 50 ภาษา ซึ่งจะเพิ่มขนาดการดาวน์โหลดอย่างมีนัยสำคัญ หากต้องการโคลนโดยไม่เอาการแปลมาใช้ ให้ใช้ sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> นี่จะให้ทุกอย่างที่คุณต้องการเพื่อจบหลักสูตรด้วยการดาวน์โหลดที่เร็วกว่ามาก


## รองรับหลายภาษา

### รองรับผ่าน GitHub Action (อัตโนมัติ & อัปเดตตลอดเวลา)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[อาหรับ](../ar/README.md) | [เบงาลี](../bn/README.md) | [บัลแกเรียน](../bg/README.md) | [พม่า (เมียนมา)](../my/README.md) | [จีน (ตัวย่อ)](../zh/README.md) | [จีน (ตัวเต็ม, ฮ่องกง)](../hk/README.md) | [จีน (ตัวเต็ม, มาเก๊า)](../mo/README.md) | [จีน (ตัวเต็ม, ไต้หวัน)](../tw/README.md) | [โครเอเชีย](../hr/README.md) | [เช็ก](../cs/README.md) | [เดนมาร์ก](../da/README.md) | [ดัตช์](../nl/README.md) | [เอสโตเนีย](../et/README.md) | [ฟินแลนด์](../fi/README.md) | [ฝรั่งเศส](../fr/README.md) | [เยอรมัน](../de/README.md) | [กรีก](../el/README.md) | [ฮีบรู](../he/README.md) | [ฮินดี](../hi/README.md) | [ฮังการี](../hu/README.md) | [อินโดนีเซีย](../id/README.md) | [อิตาลี](../it/README.md) | [ญี่ปุ่น](../ja/README.md) | [กันนาดา](../kn/README.md) | [เกาหลี](../ko/README.md) | [ลิทัวเนีย](../lt/README.md) | [มลายู](../ms/README.md) | [มาลายาลัม](../ml/README.md) | [มราฐี](../mr/README.md) | [เนปาลี](../ne/README.md) | [ไนจีเรีย พิดจิน](../pcm/README.md) | [นอร์เวย์](../no/README.md) | [เปอร์เซีย (ฟาร์ซี)](../fa/README.md) | [โปแลนด์](../pl/README.md) | [โปรตุเกส (บราซิล)](../br/README.md) | [โปรตุเกส (โปรตุเกส)](../pt/README.md) | [ปัญจาบ (กุรมุขี)](../pa/README.md) | [โรมาเนีย](../ro/README.md) | [รัสเซีย](../ru/README.md) | [เซอร์เบีย (คิริลลิก)](../sr/README.md) | [สโลวาเกีย](../sk/README.md) | [สโลวีเนีย](../sl/README.md) | [สเปน](../es/README.md) | [สวาฮีลี](../sw/README.md) | [สวีเดน](../sv/README.md) | [ทากาล็อก (ฟิลิปปินส์)](../tl/README.md) | [ทมิฬ](../ta/README.md) | [เตลูกู](../te/README.md) | [ไทย](./README.md) | [ตุรกี](../tr/README.md) | [ยูเครน](../uk/README.md) | [อูรดู](../ur/README.md) | [เวียดนาม](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## โครงสร้างหลักสูตร & เส้นทางการเรียนรู้

### **บทที่ 1: บทนำสู่ปัญญาประดิษฐ์เชิงสร้างสรรค์**
- **แนวคิดหลัก**: ทำความเข้าใจโมเดลภาษาใหญ่, โทเค็น, embeddings, และความสามารถของ AI
- **ระบบนิเวศ AI ใน Java**: ภาพรวมของ Spring AI และ OpenAI SDKs
- **Model Context Protocol**: บทนำสู่ MCP และบทบาทของมันในการสื่อสารของเอเจนต์ AI
- **การประยุกต์ใช้จริง**: สถานการณ์ในโลกจริงรวมถึงแชทบอทและการสร้างเนื้อหา
- **[→ เริ่มบทที่ 1](./01-IntroToGenAI/README.md)**

### **บทที่ 2: การตั้งค่าสภาพแวดล้อมการพัฒนา**
- **การกำหนดค่าหลายผู้ให้บริการ**: ตั้งค่า GitHub Models, Azure OpenAI, และการรวม OpenAI Java SDK
- **Spring Boot + Spring AI**: แนวปฏิบัติที่ดีที่สุดสำหรับการพัฒนาแอปพลิเคชัน AI ระดับองค์กร
- **GitHub Models**: การเข้าถึงโมเดล AI ฟรีสำหรับการพัฒนาโปรโตไทป์และการเรียนรู้ (ไม่ต้องใช้บัตรเครดิต)
- **เครื่องมือพัฒนา**: คอนเทนเนอร์ Docker, VS Code และการกำหนดค่า GitHub Codespaces
- **[→ เริ่มบทที่ 2](./02-SetupDevEnvironment/README.md)**

### **บทที่ 3: เทคนิคหลักของปัญญาประดิษฐ์เชิงสร้างสรรค์**
- **Prompt Engineering**: เทคนิคสำหรับการได้คำตอบที่ดีที่สุดจากโมเดล AI
- **Embeddings & การดำเนินการเวกเตอร์**: นำไปใช้การค้นหาเชิงความหมายและการจับคู่ความคล้ายคลึง
- **Retrieval-Augmented Generation (RAG)**: ผสาน AI กับแหล่งข้อมูลของคุณเอง
- **Function Calling**: ขยายความสามารถของ AI ด้วยเครื่องมือและปลั๊กอินที่กำหนดเอง
- **[→ เริ่มบทที่ 3](./03-CoreGenerativeAITechniques/README.md)**

### **บทที่ 4: การประยุกต์ใช้จริง & โครงการ**
- **Pet Story Generator** (`petstory/`): การสร้างเนื้อหาเชิงสร้างสรรค์ด้วย GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): การรวมโมเดล AI ท้องถิ่นกับ OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): การใช้งาน Model Context Protocol เบื้องต้นกับ Spring AI
- **[→ เริ่มบทที่ 4](./04-PracticalSamples/README.md)**

### **บทที่ 5: การพัฒนา AI อย่างรับผิดชอบ**
- **ความปลอดภัยของ GitHub Models**: ทดสอบการกรองเนื้อหาที่มีมาในตัวและกลไกการรักษาความปลอดภัย (การบล็อกแบบเข้มงวดและการปฏิเสธแบบอ่อน)
- **ตัวอย่างการพัฒนา AI อย่างรับผิดชอบ**: ตัวอย่างเชิงปฏิบัติที่แสดงให้เห็นว่าระบบความปลอดภัยของ AI สมัยใหม่ทำงานอย่างไร
- **แนวปฏิบัติที่ดีที่สุด**: แนวทางสำคัญสำหรับการพัฒนาและปรับใช้ AI อย่างมีจริยธรรม
- **[→ เริ่มบทที่ 5](./05-ResponsibleGenAI/README.md)**

## แหล่งข้อมูลเพิ่มเติม

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / เอเจนต์
[![AZD สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP สำหรับผู้เริ่มต้น](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ชุด Generative AI
[![ปัญญาประดิษฐ์เชิงสร้างสรรค์สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ปัญญาประดิษฐ์เชิงสร้างสรรค์ (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![ปัญญาประดิษฐ์เชิงสร้างสรรค์ (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![ปัญญาประดิษฐ์เชิงสร้างสรรค์ (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### การเรียนรู้พื้นฐาน
[![ML สำหรับผู้เริ่มต้น](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![วิทยาศาสตร์ข้อมูลสำหรับผู้เริ่มต้น](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![ความมั่นคงปลอดภัยไซเบอร์สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![การพัฒนาเว็บสำหรับผู้เริ่มต้น](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![IoT สำหรับผู้เริ่มต้น](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![การพัฒนา XR สำหรับผู้เริ่มต้น](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ซีรีส์ Copilot
[![Copilot สำหรับการเขียนโปรแกรมจับคู่ด้วย AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot สำหรับ C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## รับความช่วยเหลือ

หากคุณติดขัดหรือมีคำถามใด ๆ เกี่ยวกับการสร้างแอป AI ให้เข้าร่วมกับผู้เรียนคนอื่น ๆ และนักพัฒนาที่มีประสบการณ์ในการสนทนาเกี่ยวกับ MCP ชุมชนนี้เป็นชุมชนที่ให้การสนับสนุนซึ่งยินดีต้อนรับคำถามและแบ่งปันความรู้กันอย่างเสรี

[![Discord ของ Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

หากคุณมีความคิดเห็นเกี่ยวกับผลิตภัณฑ์หรือพบข้อผิดพลาดขณะพัฒนา โปรดเยี่ยมชม:

[![ฟอรัมนักพัฒนา Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:
เอกสารฉบับนี้ได้รับการแปลโดยใช้บริการแปลด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้การแปลมีความถูกต้อง โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่แม่นยำได้ ควรถือว่าเอกสารต้นฉบับในภาษาต้นทางเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่มีความสำคัญ แนะนำให้ใช้การแปลโดยนักแปลมืออาชีพ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดใด ๆ ที่เกิดจากการใช้การแปลฉบับนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->