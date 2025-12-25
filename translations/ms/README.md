<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:22:15+00:00",
  "source_file": "README.md",
  "language_code": "ms"
}
-->
# AI Generatif untuk Pemula - Edisi Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![AI Generatif untuk Pemula - Edisi Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ms.png)

**Komitmen Masa**: Keseluruhan bengkel boleh diselesaikan secara dalam talian tanpa persediaan tempatan. Persediaan persekitaran mengambil masa 2 minit, dengan penerokaan contoh memerlukan 1-3 jam bergantung pada tahap penerokaan.

> **Mula Pantas** 

1. Fork repositori ini ke akaun GitHub anda
2. Klik **Code** → **Codespaces** tab → **...** → **New with options...**
3. Gunakan tetapan lalai – ini akan memilih kontena Pembangunan yang dicipta untuk kursus ini
4. Klik **Create codespace**
5. Tunggu ~2 minit untuk persekitaran bersedia
6. Terus ke [Contoh pertama](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Lebih suka Mengklon Secara Tempatan?**
>
> Repositori ini mengandungi 50+ terjemahan bahasa yang secara ketara meningkatkan saiz muat turun. Untuk mengklon tanpa terjemahan, gunakan sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ini memberi anda segala yang anda perlukan untuk melengkapkan kursus dengan muat turun yang jauh lebih pantas.


## Sokongan Pelbagai Bahasa

### Disokong melalui GitHub Action (Automatik & Sentiasa Terkini)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](./README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../br/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Struktur Kursus & Laluan Pembelajaran

### **Bab 1: Pengenalan kepada AI Generatif**
- **Konsep Teras**: Memahami Large Language Models, token, embeddings, dan kebolehan AI
- **Ekosistem AI Java**: Gambaran keseluruhan Spring AI dan OpenAI SDKs
- **Protokol Konteks Model**: Pengenalan kepada MCP dan perannya dalam komunikasi agen AI
- **Aplikasi Praktikal**: Senario dunia sebenar termasuk chatbot dan penjanaan kandungan
- **[→ Mula Bab 1](./01-IntroToGenAI/README.md)**

### **Bab 2: Persediaan Persekitaran Pembangunan**
- **Konfigurasi Multi-Pembekal**: Sediakan integrasi GitHub Models, Azure OpenAI, dan OpenAI Java SDK
- **Spring Boot + Spring AI**: Amalan terbaik untuk pembangunan aplikasi AI perusahaan
- **GitHub Models**: Akses model AI percuma untuk prototaip dan pembelajaran (no credit card required)
- **Alat Pembangunan**: Docker containers, VS Code, dan konfigurasi GitHub Codespaces
- **[→ Mula Bab 2](./02-SetupDevEnvironment/README.md)**

### **Bab 3: Teknik Teras AI Generatif**
- **Prompt Engineering**: Teknik untuk respons model AI yang optimum
- **Embeddings & Operasi Vektor**: Laksana carian semantik dan padanan kesamaan
- **Retrieval-Augmented Generation (RAG)**: Gabungkan AI dengan sumber data anda sendiri
- **Function Calling**: Luaskan kebolehan AI dengan alat dan pemalam tersuai
- **[→ Mula Bab 3](./03-CoreGenerativeAITechniques/README.md)**

### **Bab 4: Aplikasi Praktikal & Projek**
- **Penjana Cerita Haiwan Peliharaan** (`petstory/`): Penjanaan kandungan kreatif dengan GitHub Models
- **Demo Foundry Tempatan** (`foundrylocal/`): Integrasi model AI tempatan dengan OpenAI Java SDK
- **Perkhidmatan Kalkulator MCP** (`calculator/`): Pelaksanaan asas Model Context Protocol dengan Spring AI
- **[→ Mula Bab 4](./04-PracticalSamples/README.md)**

### **Bab 5: Pembangunan AI Bertanggungjawab**
- **Keselamatan GitHub Models**: Uji penapisan kandungan terbina dan mekanisme keselamatan (hard blocks dan soft refusals)
- **Demo AI Bertanggungjawab**: Contoh praktikal menunjukkan bagaimana sistem keselamatan AI moden berfungsi dalam amalan
- **Amalan Terbaik**: Garis panduan penting untuk pembangunan dan penyebaran AI yang beretika
- **[→ Mula Bab 5](./05-ResponsibleGenAI/README.md)**

## Sumber Tambahan

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j untuk Pemula](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js untuk Pemula](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Ejen
[![AZD untuk Pemula](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI untuk Pemula](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP untuk Pemula](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Ejen AI untuk Pemula](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Siri AI Generatif
[![AI Generatif untuk Pemula](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Generatif (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![AI Generatif (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![AI Generatif (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Pembelajaran Teras
[![ML untuk Pemula](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Sains Data untuk Pemula](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI untuk Pemula](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Keselamatan Siber untuk Pemula](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Pembangunan Web untuk Pemula](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT untuk Pemula](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Pembangunan XR untuk Pemula](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Siri Copilot
[![Copilot untuk Pengaturcaraan Berpasangan AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot untuk C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Pengembaraan Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Mendapatkan Bantuan

Jika anda tersangkut atau mempunyai sebarang pertanyaan mengenai pembinaan aplikasi AI. Sertai rakan pelajar dan pembangun berpengalaman dalam perbincangan mengenai MCP. Ia adalah sebuah komuniti yang menyokong di mana soalan dialu-alukan dan pengetahuan dikongsi dengan bebas.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Jika anda mempunyai maklum balas produk atau ralat semasa membina lawati:

[![Forum Pembangun Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Penafian:
Dokumen ini telah diterjemahkan menggunakan perkhidmatan terjemahan AI Co-op Translator (https://github.com/Azure/co-op-translator). Walaupun kami berusaha untuk ketepatan, sila ambil perhatian bahawa terjemahan automatik mungkin mengandungi ralat atau ketidakakuratan. Dokumen asal dalam bahasa asalnya hendaklah dianggap sebagai sumber rujukan yang sah. Untuk maklumat penting, terjemahan profesional oleh penterjemah manusia adalah disyorkan. Kami tidak bertanggungjawab terhadap sebarang salah faham atau salah tafsir yang timbul daripada penggunaan terjemahan ini.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->