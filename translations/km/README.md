# បច្ចេកវិទ្យាហ្វឹកហាត់ AI សម្រាប់អ្នកចាប់ផ្តើម - កំណែ Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/km/beg-genai-series.8b48be9951cc574c.webp)

**ការប្តេជ្ញាពេលវេលា**: វគ្គបណ្ដុះបណ្ដាលទាំងមូលអាចបញ្ចប់តាមអនឡាញដោយមិនចាំបាច់រៀបចំស្ថាន that នៅក្នុងរូបិយវត្ថុ។ ការរៀបចំបរិស្ថានចំណាយពេល 2 នាទី ហើយការស្វែងរកគំរូតម្រូវឱ្យចំណាយ 1-3 ម៉ោងអាស្រ័យលើជម្រៅស្វែងរក។

> **ចាប់ផ្តើមរហ័ស** 

1. តម្លើង仓repository នេះទៅគណនី GitHub របស់អ្នក
2. ចុច **Code** → ប៊ូតុង **Codespaces** → **...** → **New with options...**
3. ប្រើការកំណត់លំនាំដើម – នេះនឹងជ្រើសរើស container ការអភិវឌ្ឍដែលបានបង្កើតសម្រាប់វគ្គនេះ
4. ចុច **Create codespace**
5. រង់ចាំ ~2 នាទីសម្រាប់បរិស្ថានឱ្យរួចរាល់
6. ឆ្ពោះទៅផ្ទាល់ទៅ [ជំពូក 2: ការផ្ដល់ Azure AI Foundry](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## ការគាំទ្រភាសាចម្រុះ

### គាំទ្រតាមរយៈ GitHub Action (ស្វ័យប្រវត្តិ និងទើបទាន់សម័យជានិច្ច)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[អារ៉ាប់](../ar/README.md) | [បង់គោលី](../bn/README.md) | [ប៊ុលហ្គារី](../bg/README.md) | [ភាសាប៊ឺម៉ា (មីយ៉ាន់ម៉ា)](../my/README.md) | [ចិន (ត្រង់រៀប)](../zh-CN/README.md) | [ចិន (បែបប្រពៃណី, ហុងកុង)](../zh-HK/README.md) | [ចិន (បែបប្រពៃណី, ម៉ាកាវ)](../zh-MO/README.md) | [ចិន (បែបប្រពៃណី, តៃវ៉ាន់)](../zh-TW/README.md) | [ក្រូអាត](../hr/README.md) | [ឆែក](../cs/README.md) | [ដាណីស](../da/README.md) | [ដutch](../nl/README.md) | [អេសតូនី](../et/README.md) | [ហ្វិនស្ហ៊ី](../fi/README.md) | [បារាំង](../fr/README.md) | [អាល្លឺម៉ង់](../de/README.md) | [ក្រិក](../el/README.md) | [ហេប្រ៊ូ](../he/README.md) | [ហិណ្ឌី](../hi/README.md) | [ហុងគ្រី](../hu/README.md) | [ឥណ្ឌូណេស៊ី](../id/README.md) | [អ៊ីតាលី](../it/README.md) | [ជប៉ុន](../ja/README.md) | [កណាដា](../kn/README.md) | [ខ្មែរ](./README.md) | [កូរ៉េ](../ko/README.md) | [លិទួហ្វានី](../lt/README.md) | [ម៉ាឡេស៊ី](../ms/README.md) | [ម៉ាឡាឡាម](../ml/README.md) | [ម៉ារាធី](../mr/README.md) | [នេប៉ាល់](../ne/README.md) | [ភាគីន នីហ្ស៊ែរី](../pcm/README.md) | [ណ័រវ៉េ](../no/README.md) | [ភាសាឥស្លាម (ហ្វាស៊ី)](../fa/README.md) | [ប៉ូឡូញ](../pl/README.md) | [ប៉៊ូតុហ្គាល់ (ប្រេស៊ីល)](../pt-BR/README.md) | [ប៉៊ូតុហ្គាល់ (ប្រទេសប៉ូរទុយហ្គាល់)](../pt-PT/README.md) | [ផុនជាភាសា (ហ្គារម៉ាគី)](../pa/README.md) | [រ៉ូម៉ានី](../ro/README.md) | [រុស្ស៊ី](../ru/README.md) | [ស៊ែរ​ប៊ី (ស៊ីរីលិក)](../sr/README.md) | [ស្លូវាខ](../sk/README.md) | [ស្លូវេនី](../sl/README.md) | [អេស្បាញ](../es/README.md) | [ស្វាហ៊ីលី](../sw/README.md) | [ស្វែត](../sv/README.md) | [តាហ្គាឡុក (ហ្វីលីពីន)](../tl/README.md) | [តាមីល](../ta/README.md) | [តេលូប៊ូ](../te/README.md) | [ថៃ](../th/README.md) | [ទួរកី](../tr/README.md) | [អ៊ុយក្រែន](../uk/README.md) | [អ៊ួរឌូ](../ur/README.md) | [វៀតណាម](../vi/README.md)

> **ចង់ចម្លងនៅក្នុងម៉ាស៊ីនផ្ទាល់?**
>
> ប្រ៊ូហ្សាស់នេះរួមបញ្ចូលការបកប្រែភាសាជាង 50 ដែលបង្កើនទំហំការទាញយកយ៉ាងច្រើន។ ដើម្បីចម្លងដោយគ្មានការបកប្រែ សូមប្រើ sparse checkout៖
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> វាផ្តល់ឱ្យអ្នកនូវអ្វីដែលអ្នកត្រូវការពេញលេញសម្រាប់បញ្ចប់វគ្គនេះជាមួយនឹងការទាញយករហ័សជាងមុន។
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## រចនាសម្ព័ន្ធវគ្គ និងផ្លូវកំណត់ការរៀន

### **ជំពូក 1: សេចក្តីប្ដេជ្ញាចំពោះ AI សំរាប់បង្កើត**
- **គំនិតមូលដ្ឋាន**: ការយល់ដឹងពីម៉ូដែលភាសាធំៗ, tokens, embeddings និងសមត្ថភាព AI
- **ប្រព័ន្ធភាសា Java AI**: ទិដ្ឋភាពទូទៅនៃ Spring AI និង OpenAI SDKs
- **ពិធីសាស្ត្រម៉ូដែល Context Protocol**: ប្រាប់បង្ហាញអំពី MCP និងតួនាទីរបស់វាក្នុងការទំនាក់ទំនងតំណាង AI
- **ការអនុវត្តជាក់ស្តែង**: ស្ថានភាពពិភពជាប្រព័ន្ធជាមួយ chatbot និងការបង្កើតមាតិកា
- **[→ ចាប់ផ្តើមជំពូក 1](./01-IntroToGenAI/README.md)**

### **ជំពូក 2: ការរៀបចំបរិស្ថានអភិវឌ្ឍន៍**
- **Azure AI Foundry**: បង្កើត GPT-5.6 Luna chat និង text-embedding-3-small embeddings ជាមួយ Bicep និង Azure Developer CLI (azd)
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: រៀនជាមួយ `ChatClient`, គាំទ្រដោយ OpenAI Java SDK ផ្លូវការនិង Azure OpenAI v1
- **Keyless Authentication**: បានភ្ជាប់យ៉ាងសុវត្ថិភាពជាមួយ Microsoft Entra ID — អត់មានកូនសោ API ដើម្បីគ្រប់គ្រង
- **ឧបករណ៍អភិវឌ្ឍន៍**: ហុង Docker containers, VS Code, និង ការកំណត់ GitHub Codespaces
- **[→ ចាប់ផ្តើមជំពូក 2](./02-SetupDevEnvironment/README.md)**

### **ជំពូក 3: បច្ចេកទេសសំខាន់នៃ AI បង្កើត**
- **OpenAI Java SDK ផ្លូវការ**: ហៅ Azure OpenAI v1 ដោយផ្ទាល់ជាមួយការផ្ទៀង Keyless
- **Prompt Engineering**: បច្ចេកទេសសម្រាប់ចម្លើយម៉ូដែល AI ល្អបំផុត
- **Embeddings និងប្រតិបត្តិការវិចទ័រ**: បញ្ចូលការស្វែងរក semantic និងការផ្គូរផ្គងស្រដៀងគ្នា
- **Retrieval-Augmented Generation (RAG)**: បញ្ចូល AI ជាមួយប្រភពទិន្នន័យផ្ទាល់ខ្លួន
- **Function Calling**: ពង្រីកសមត្ថភាព AI ជាមួយឧបករណ៍និងផ្លាស៊ីនផ្ទាល់ខ្លួន
- **[→ ចាប់ផ្តើមជំពូក 3](./03-CoreGenerativeAITechniques/README.md)**

### **ជំពូក 4: ការអនុវត្តជាក់ស្តែង និងគម្រោង**
- **ម៉ូដែលបង្កើតរឿងសត្វ pel Story Generator** (`petstory/`): ការបង្កើតមាតិកាជាមួយ Azure AI Foundry
- **Foundry Local Demo** (`foundrylocal/`): បញ្ចូលម៉ូដែល AI នៅក្នុងម៉ាស៊ីនបច្ចុប្បន្នជាមួយ OpenAI Java SDK
- **សេវាកម្មគណនាគម្រប់ Model Context Protocol** (`calculator/`): ការអនុវត្ត MCP មូលដ្ឋានជាមួយ Spring AI
- **[→ ចាប់ផ្តើមជំពូក 4](./04-PracticalSamples/README.md)**

### **ជំពូក 5: ការអភិវឌ្ឍ AI យ៉ាងទំនួលខុសត្រូវ**
- **Azure AI Foundry Content Safety**: សាកល្បងមេកានិចសុវត្ថិភាពនិងការត្រួតពិនិត្យមាតិកា (ការរារាំងតឹង និងការផ្តាច់បន្តបន្សល់)
- **សាកល្បង AI យ៉ាងទំនួលខុសត្រូវ**: ឧទាហរណ៍ដៃគូនៅពេលដែលបង្ហាញពីរបៀបប្រព័ន្ធសុវត្ថិភាព AI ទាន់សម័យធ្វើការជាក់ស្តែង
- **អនុវត្តល្អបំផុត**: ទ្រឹស្តីចំប៉ះសំរាប់ការអភិវឌ្ឍន៍និងដាក់ឱ្យដំណើរការ AI យ៉ាងមានចេតនា
- **[→ ចាប់ផ្តើមជំពូក 5](./05-ResponsibleGenAI/README.md)**

## ធនធានបន្ថែម

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain for Beginners](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ស៊េរី Generative AI
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ការរៀនគោល
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ស៊េរី Copilot
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## របៀបទទួលបានជំនួយ

ប្រសិនបើអ្នកជួបបញ្ហារឺមានសំណួរអំពីការសង់កម្មវិធី AI សូមចូលរួមជាមួយអ្នករៀន និងអ្នកអភិវឌ្ឍដែលមានបទពិសោធន៏ក្នុងការពិភាក្សា អំពី MCP។ វាជាសហគមន៍គាំទ្រ ដែលស្វាគមន៍សំណួរនិងចែករំលែកចំណេះដឹងដោយសេរី។

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ប្រសិនបើអ្នកមានមតិយោបល់ផលិតផល ឬកំហុស ខណៈពេលសង់ សូមទៅកាន់៖

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->