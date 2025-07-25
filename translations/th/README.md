<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:34:41+00:00",
  "source_file": "README.md",
  "language_code": "th"
}
-->
# Generative AI สำหรับผู้เริ่มต้น - Java Edition
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generative AI สำหรับผู้เริ่มต้น - Java Edition](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.th.png)

**ระยะเวลา**: เวิร์กชอปทั้งหมดสามารถทำออนไลน์ได้โดยไม่ต้องตั้งค่าบนเครื่อง หากต้องการทดลองตัวอย่าง การตั้งค่าสภาพแวดล้อมใช้เวลา 2 นาที และการสำรวจตัวอย่างใช้เวลา 1-3 ชั่วโมง ขึ้นอยู่กับความลึกของการสำรวจ

> **เริ่มต้นอย่างรวดเร็ว**

1. Fork repository นี้ไปยังบัญชี GitHub ของคุณ
2. คลิก **Code** → **Codespaces** tab → **...** → **New with options...**
3. ใช้ค่าตั้งต้น – ระบบจะเลือก Development container ที่สร้างขึ้นสำหรับคอร์สนี้
4. คลิก **Create codespace**
5. รอประมาณ ~2 นาทีเพื่อให้สภาพแวดล้อมพร้อมใช้งาน
6. ไปที่ [การสร้าง GitHub Models Token ของคุณ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## รองรับหลายภาษา

### รองรับผ่าน GitHub Action (อัตโนมัติและอัปเดตเสมอ)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](./README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## โครงสร้างคอร์สและเส้นทางการเรียนรู้

### **บทที่ 1: แนะนำ Generative AI**
- **แนวคิดหลัก**: ทำความเข้าใจ Large Language Models, tokens, embeddings และความสามารถของ AI
- **ระบบนิเวศ AI ใน Java**: ภาพรวมของ Spring AI และ OpenAI SDKs
- **Model Context Protocol**: แนะนำ MCP และบทบาทของมันใน AI agent communication
- **การใช้งานจริง**: ตัวอย่างในโลกจริง เช่น chatbot และการสร้างเนื้อหา
- **[→ เริ่มบทที่ 1](./01-IntroToGenAI/README.md)**

### **บทที่ 2: การตั้งค่าสภาพแวดล้อมการพัฒนา**
- **การตั้งค่าหลายผู้ให้บริการ**: ตั้งค่า GitHub Models, Azure OpenAI และ OpenAI Java SDK integrations
- **Spring Boot + Spring AI**: แนวทางปฏิบัติที่ดีที่สุดสำหรับการพัฒนาแอปพลิเคชัน AI ในองค์กร
- **GitHub Models**: เข้าถึงโมเดล AI ฟรีสำหรับการสร้างต้นแบบและการเรียนรู้ (ไม่ต้องใช้บัตรเครดิต)
- **เครื่องมือพัฒนา**: การตั้งค่า Docker containers, VS Code และ GitHub Codespaces
- **[→ เริ่มบทที่ 2](./02-SetupDevEnvironment/README.md)**

### **บทที่ 3: เทคนิค Generative AI หลัก**
- **Prompt Engineering**: เทคนิคสำหรับการตอบสนองของโมเดล AI ที่เหมาะสมที่สุด
- **Embeddings & Vector Operations**: การใช้งาน semantic search และ similarity matching
- **Retrieval-Augmented Generation (RAG)**: ผสาน AI กับแหล่งข้อมูลของคุณเอง
- **Function Calling**: ขยายความสามารถของ AI ด้วยเครื่องมือและปลั๊กอินที่กำหนดเอง
- **[→ เริ่มบทที่ 3](./03-CoreGenerativeAITechniques/README.md)**

### **บทที่ 4: การใช้งานจริงและโปรเจกต์**
- **Pet Story Generator** (`petstory/`): การสร้างเนื้อหาเชิงสร้างสรรค์ด้วย GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): การผสานโมเดล AI ในเครื่องกับ OpenAI Java SDK
- **MCP Calculator Service** (`mcp/calculator/`): การใช้งาน Model Context Protocol เบื้องต้นกับ Spring AI
- **[→ เริ่มบทที่ 4](./04-PracticalSamples/README.md)**

### **บทที่ 5: การพัฒนา AI อย่างรับผิดชอบ**
- **GitHub Models Safety**: ทดสอบการกรองเนื้อหาและกลไกความปลอดภัยในตัว
- **Responsible AI Demo**: ตัวอย่างการใช้งานจริงที่แสดงการทำงานของตัวกรองความปลอดภัย AI
- **แนวทางปฏิบัติที่ดีที่สุด**: แนวทางสำคัญสำหรับการพัฒนาและการใช้งาน AI อย่างมีจริยธรรม
- **[→ เริ่มบทที่ 5](./05-ResponsibleGenAI/README.md)**

## แหล่งข้อมูลเพิ่มเติม 

- [AI Agents สำหรับผู้เริ่มต้น](https://github.com/microsoft/ai-agents-for-beginners)
- [Generative AI สำหรับผู้เริ่มต้นโดยใช้ .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generative AI สำหรับผู้เริ่มต้นโดยใช้ JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generative AI สำหรับผู้เริ่มต้น](https://github.com/microsoft/generative-ai-for-beginners)
- [ML สำหรับผู้เริ่มต้น](https://aka.ms/ml-beginners)
- [Data Science สำหรับผู้เริ่มต้น](https://aka.ms/datascience-beginners)
- [AI สำหรับผู้เริ่มต้น](https://aka.ms/ai-beginners)
- [Cybersecurity สำหรับผู้เริ่มต้น](https://github.com/microsoft/Security-101)
- [Web Dev สำหรับผู้เริ่มต้น](https://aka.ms/webdev-beginners)
- [IoT สำหรับผู้เริ่มต้น](https://aka.ms/iot-beginners)
- [XR Development สำหรับผู้เริ่มต้น](https://github.com/microsoft/xr-development-for-beginners)
- [การใช้งาน GitHub Copilot สำหรับการเขียนโปรแกรม AI คู่](https://aka.ms/GitHubCopilotAI)
- [การใช้งาน GitHub Copilot สำหรับนักพัฒนา C#/.NET](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [เลือกการผจญภัย Copilot ของคุณเอง](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App กับ Azure AI Services](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**ข้อจำกัดความรับผิดชอบ**:  
เอกสารนี้ได้รับการแปลโดยใช้บริการแปลภาษา AI [Co-op Translator](https://github.com/Azure/co-op-translator) แม้ว่าเราจะพยายามให้การแปลมีความถูกต้องมากที่สุด แต่โปรดทราบว่าการแปลโดยอัตโนมัติอาจมีข้อผิดพลาดหรือความไม่ถูกต้อง เอกสารต้นฉบับในภาษาดั้งเดิมควรถือเป็นแหล่งข้อมูลที่เชื่อถือได้ สำหรับข้อมูลที่สำคัญ ขอแนะนำให้ใช้บริการแปลภาษามืออาชีพ เราจะไม่รับผิดชอบต่อความเข้าใจผิดหรือการตีความที่ผิดพลาดซึ่งเกิดจากการใช้การแปลนี้