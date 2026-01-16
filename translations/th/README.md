<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:22:05+00:00",
  "source_file": "README.md",
  "language_code": "th"
}
-->
# Generative AI สำหรับผู้เริ่มต้น - ฉบับ Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI สำหรับผู้เริ่มต้น - ฉบับ Java](../../translated_images/th/beg-genai-series.8b48be9951cc574c.webp)

**เวลาที่ใช้**: เวิร์กช็อปทั้งหมดสามารถทำได้ออนไลน์โดยไม่ต้องตั้งค่าท้องถิ่น การตั้งค่าสภาพแวดล้อมใช้เวลา 2 นาที โดยการสำรวจตัวอย่างใช้เวลาประมาณ 1-3 ชั่วโมงขึ้นอยู่กับความลึกของการสำรวจ

> **เริ่มต้นอย่างรวดเร็ว**

1. Fork รีโพสitory นี้ไปยังบัญชี GitHub ของคุณ
2. คลิก **Code** → แถบ **Codespaces** → **...** → **New with options...**
3. ใช้ค่าเริ่มต้น – ระบบจะเลือกคอนเทนเนอร์สำหรับการพัฒนาที่สร้างไว้สำหรับคอร์สนี้
4. คลิก **Create codespace**
5. รอประมาณ 2 นาทีจนกว่าสภาพแวดล้อมจะพร้อมใช้งาน
6. ไปที่ [ตัวอย่างแรก](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) ได้เลย

> **ต้องการโคลนแบบโลคัลไหม?**
>
> รีโพสitory นี้มีการแปลภาษา 50+ ภาษา ซึ่งทำให้ขนาดดาวน์โหลดใหญ่ขึ้นมาก หากต้องการโคลนโดยไม่รวมการแปลภาษา ใช้ sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> วิธีนี้จะให้ทุกอย่างที่คุณต้องใช้เพื่อทำคอร์สให้เสร็จอย่างรวดเร็วมากขึ้น


## รองรับหลายภาษา

### รองรับผ่าน GitHub Action (อัตโนมัติ & อัปเดตตลอดเวลา)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[อาหรับ](../ar/README.md) | [เบงกาลี](../bn/README.md) | [บัลแกเรีย](../bg/README.md) | [พม่า (เมียนมา)](../my/README.md) | [จีน (ตัวย่อ)](../zh/README.md) | [จีน (ตัวเต็ม, ฮ่องกง)](../hk/README.md) | [จีน (ตัวเต็ม, มาเก๊า)](../mo/README.md) | [จีน (ตัวเต็ม, ไต้หวัน)](../tw/README.md) | [โครเอเชีย](../hr/README.md) | [เช็ก](../cs/README.md) | [เดนมาร์ก](../da/README.md) | [ดัตช์](../nl/README.md) | [เอสโตเนีย](../et/README.md) | [ฟินแลนด์](../fi/README.md) | [ฝรั่งเศส](../fr/README.md) | [เยอรมัน](../de/README.md) | [กรีก](../el/README.md) | [ฮิบรู](../he/README.md) | [ฮินดี](../hi/README.md) | [ฮังการี](../hu/README.md) | [อินโดนีเซีย](../id/README.md) | [อิตาลี](../it/README.md) | [ญี่ปุ่น](../ja/README.md) | [กันนาดา](../kn/README.md) | [เกาหลี](../ko/README.md) | [ลิทัวเนีย](../lt/README.md) | [มาเลย์](../ms/README.md) | [มาลายาลัม](../ml/README.md) | [มราษฏร์](../mr/README.md) | [เนปาล](../ne/README.md) | [ไนจีเรีย พิดจิน](../pcm/README.md) | [นอร์เวย์](../no/README.md) | [เปอร์เซีย (ฟาร์ซี)](../fa/README.md) | [โปแลนด์](../pl/README.md) | [โปรตุเกส (บราซิล)](../br/README.md) | [โปรตุเกส (โปรตุเกส)](../pt/README.md) | [ปัญจาบี (กูรมุขี)](../pa/README.md) | [โรมาเนีย](../ro/README.md) | [รัสเซีย](../ru/README.md) | [เซอร์เบีย (ซีริลลิก)](../sr/README.md) | [สโลวัก](../sk/README.md) | [สโลวีเนีย](../sl/README.md) | [สเปน](../es/README.md) | [สวาฮีลี](../sw/README.md) | [สวีเดน](../sv/README.md) | [ตากาล็อก (ฟิลิปปินส์)](../tl/README.md) | [ทมิฬ](../ta/README.md) | [เทลูกู](../te/README.md) | [ไทย](./README.md) | [ตุรกี](../tr/README.md) | [ยูเครน](../uk/README.md) | [อูรดู](../ur/README.md) | [เวียดนาม](../vi/README.md)

> **ต้องการโคลนแบบโลคัลไหม?**

> รีโพสitory นี้มีการแปลภาษา 50+ ภาษา ซึ่งทำให้ขนาดดาวน์โหลดใหญ่ขึ้นมาก หากต้องการโคลนโดยไม่รวมการแปลภาษา ใช้ sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> วิธีนี้จะให้ทุกอย่างที่คุณต้องใช้เพื่อทำคอร์สให้เสร็จอย่างรวดเร็วมากขึ้น
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## โครงสร้างคอร์ส & เส้นทางการเรียนรู้

### **บทที่ 1: บทนำสู่ Generative AI**
- **แนวคิดหลัก**: ทำความเข้าใจโมเดลภาษาใหญ่ tokens, embeddings และความสามารถของ AI
- **ระบบนิเวศ AI ของ Java**: ภาพรวม Spring AI และ OpenAI SDKs
- **โปรโตคอลบริบทของโมเดล**: แนะนำ MCP และบทบาทในการสื่อสารของตัวแทน AI
- **การใช้งานที่เป็นประโยชน์**: สถานการณ์จริงรวมถึงแชทบอทและการสร้างเนื้อหา
- **[→ เริ่มบทที่ 1](./01-IntroToGenAI/README.md)**

### **บทที่ 2: การตั้งค่าสภาพแวดล้อมการพัฒนา**
- **การตั้งค่าหลายผู้ให้บริการ**: ตั้งค่า GitHub Models, Azure OpenAI และ OpenAI Java SDK integrations
- **Spring Boot + Spring AI**: แนวทางปฏิบัติที่ดีที่สุดสำหรับการพัฒนาแอป AI สำหรับองค์กร
- **GitHub Models**: โมเดล AI ฟรีสำหรับการสร้างต้นแบบและการเรียนรู้ (ไม่ต้องใช้บัตรเครดิต)
- **เครื่องมือพัฒนา**: คอนเทนเนอร์ Docker, VS Code, และการตั้งค่า GitHub Codespaces
- **[→ เริ่มบทที่ 2](./02-SetupDevEnvironment/README.md)**

### **บทที่ 3: เทคนิค Generative AI หลัก**
- **Prompt Engineering**: เทคนิคสำหรับคำตอบโมเดล AI ที่ดีที่สุด
- **Embeddings & การดำเนินการเวกเตอร์**: นำไปใช้การค้นหาเชิงความหมายและการจับคู่ความคล้ายคลึง
- **Retrieval-Augmented Generation (RAG)**: รวม AI กับแหล่งข้อมูลของคุณเอง
- **Function Calling**: ขยายความสามารถ AI ด้วยเครื่องมือและปลั๊กอินที่กำหนดเอง
- **[→ เริ่มบทที่ 3](./03-CoreGenerativeAITechniques/README.md)**

### **บทที่ 4: การประยุกต์ใช้งานและโปรเจกต์**
- **Pet Story Generator** (`petstory/`): สร้างเนื้อหาเชิงสร้างสรรค์ด้วย GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): การรวมโมเดล AI แบบโลคัลด้วย OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): การใช้งาน Model Context Protocol เบื้องต้นด้วย Spring AI
- **[→ เริ่มบทที่ 4](./04-PracticalSamples/README.md)**

### **บทที่ 5: การพัฒนา AI อย่างรับผิดชอบ**
- **ความปลอดภัย GitHub Models**: ทดสอบฟิลเตอร์เนื้อหาและกลไกความปลอดภัยในตัว (บล็อกเข้มงวดและการปฏิเสธนุ่มนวล)
- **เดโม AI อย่างรับผิดชอบ**: ตัวอย่างใช้งานจริงแสดงให้เห็นว่าระบบความปลอดภัยของ AI สมัยใหม่ทำงานอย่างไร
- **แนวทางปฏิบัติที่ดีที่สุด**: แนวทางสำคัญสำหรับการพัฒนาและเผยแพร่ AI อย่างมีจริยธรรม
- **[→ เริ่มบทที่ 5](./05-ResponsibleGenAI/README.md)**

## แหล่งข้อมูลเพิ่มเติม

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js สำหรับผู้เริ่มต้น](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP สำหรับผู้เริ่มต้น](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ชุด Generative AI
[![Generative AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### การเรียนรู้หลัก
[![ML สำหรับผู้เริ่มต้น](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI สำหรับผู้เริ่มต้น](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev สำหรับผู้เริ่มต้น](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Series
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## การขอความช่วยเหลือ

หากคุณติดขัดหรือต้องการสอบถามเกี่ยวกับการสร้างแอป AI ร่วมกับนักเรียนและนักพัฒนาที่มีประสบการณ์ในการอภิปรายเกี่ยวกับ MCP นี่คือชุมชนที่สนับสนุนซึ่งเปิดรับคำถามและแบ่งปันความรู้กันอย่างอิสระ

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

หากคุณมีคำติชมเกี่ยวกับผลิตภัณฑ์หรือพบข้อผิดพลาดระหว่างการสร้างโปรดเยี่ยมชม:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ข้อจำกัดความรับผิดชอบ**:  
เอกสารฉบับนี้ได้รับการแปลโดยใช้บริการแปลภาษาด้วย AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้เราจะพยายามให้ความถูกต้องสูงสุด แต่กรุณาทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความคลาดเคลื่อนได้ เอกสารต้นฉบับในภาษาต้นทางถือเป็นแหล่งข้อมูลที่เชื่อถือได้สำหรับข้อมูลที่ถูกต้องที่สุด สำหรับข้อมูลที่มีความสำคัญ ขอแนะนำให้ใช้บริการแปลโดยมืออาชีพที่เป็นมนุษย์ เราไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความผิดที่เกิดขึ้นจากการใช้การแปลฉบับนี้
<!-- CO-OP TRANSLATOR DISCLAIMER END -->