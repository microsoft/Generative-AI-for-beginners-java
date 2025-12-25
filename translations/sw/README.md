<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:27:00+00:00",
  "source_file": "README.md",
  "language_code": "sw"
}
-->
# AI Inayozalisha kwa Waanzilishi - Toleo la Java
[![Discord ya Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![AI Inayozalisha kwa Waanzilishi - Toleo la Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.sw.png)

**Muda unaohitajika**: Warsha yote inaweza kukamilika mtandaoni bila usanidi wa eneo la mteja. Usanidi wa mazingira unachukua dakika 2, na kuchunguza mifano kutahitaji masaa 1-3 kulingana na kina cha uchunguzi.

> **Anza Haraka** 

1. Fork this repository to your GitHub account
2. Click **Code** → **Codespaces** tab → **...** → **New with options...**
3. Use the defaults – this will select the Development container created for this course
4. Click **Create codespace**
5. Wait ~2 minutes for the environment to be ready
6. Ruka moja kwa moja kwenye [Mfano wa kwanza](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Unapendelea Ku-clone Kwenye Kompyuta Yako?**
>
> Ghala hili lina tafsiri za lugha 50+ ambayo inaweza kuongeza sana ukubwa wa kupakua. Ili kukopa bila tafsiri, tumia sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Hii inakupa kila unachohitaji kukamilisha kozi kwa upakuaji wa haraka zaidi.


## Msaada wa Lugha Nyingi

### Inasaidiwa kupitia GitHub Action (Imeotomatishwa na Daima Inafuata Mabadiliko)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../br/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](./README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Muundo wa Kozi & Njia ya Kujifunza

### **Sura 1: Utangulizi kwa AI Inayozalisha**
- **Dhana za Msingi**: Kuelewa Modeli Kubwa za Lugha, tokeni, embeddings, na uwezo wa AI
- **Eneo la AI la Java**: Muhtasari wa Spring AI na OpenAI SDKs
- **Itifaki ya Muktadha wa Modeli**: Utangulizi wa MCP na nafasi yake katika mawasiliano ya mawakala wa AI
- **Matumizi ya Vitendo**: Mifano ya ulimwengu halisi ikijumuisha chatbots na uzalishaji wa maudhui
- **[→ Anza Sura 1](./01-IntroToGenAI/README.md)**

### **Sura 2: Usanidi wa Mazingira ya Maendeleo**
- **Usanidi wa Watoa Huduma Wengi**: Set up GitHub Models, Azure OpenAI, and OpenAI Java SDK integrations
- **Spring Boot + Spring AI**: Mbinu bora za maendeleo ya programu za AI za shirika
- **GitHub Models**: Upatikanaji wa modeli za AI bila gharama kwa prototyping na kujifunza (hakuna kadi ya mkopo inahitajika)
- **Vifaa vya Maendeleo**: Docker containers, VS Code, and GitHub Codespaces configuration
- **[→ Anza Sura 2](./02-SetupDevEnvironment/README.md)**

### **Sura 3: Mbinu za Msingi za AI Inayozalisha**
- **Prompt Engineering**: Mbinu za majibu bora kutoka kwa modeli za AI
- **Embeddings & Vector Operations**: Tekeleza utafutaji wa semantiki na upatanisho wa ufananisho
- **Retrieval-Augmented Generation (RAG)**: Combine AI with your own data sources
- **Function Calling**: Panua uwezo wa AI kwa zana maalum na plugins
- **[→ Anza Sura 3](./03-CoreGenerativeAITechniques/README.md)**

### **Sura 4: Matumizi ya Vitendo & Miradi**
- **Pet Story Generator** (`petstory/`): Uzalishaji wa maudhui ya ubunifu kwa GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): Uunganishaji wa modeli ya AI ya ndani kwa OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): Utekelezaji wa msingi wa Model Context Protocol kwa kutumia Spring AI
- **[→ Anza Sura 4](./04-PracticalSamples/README.md)**

### **Sura 5: Maendeleo ya AI yenye Uwajibikaji**
- **GitHub Models Safety**: Jaribu kuchuja maudhui kilichojengwa na mifumo ya usalama (vizuizi ngumu na kukataa kwa upole)
- **Responsible AI Demo**: Mfano wa vitendo unaoonyesha jinsi mifumo ya usalama ya AI ya kisasa inavyofanya kazi
- **Mbinu Bora**: Miongozo muhimu kwa maendeleo na uanzishaji wa AI kwa maadili
- **[→ Anza Sura 5](./05-ResponsibleGenAI/README.md)**

## Rasilimali za Ziada

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kwa Waanzilishi](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kwa Waanzilishi](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Mawakala
[![AZD kwa Waanzilishi](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI kwa Waanzilishi](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kwa Waanzilishi](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Mawakala wa AI kwa Waanzilishi](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa AI Inayozalisha
[![Generative AI kwa Waanzilishi](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Mafunzo Msingi
[![ML kwa Waanzilishi](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science kwa Waanzilishi](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI kwa Waanzilishi](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity kwa Waanzilishi](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev kwa Waanzilishi](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT kwa Waanzilishi](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Uendelezaji wa XR kwa Waanzilishi](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa Copilot
[![Copilot kwa Kuprogramu kwa Pamoja na AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot kwa C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Uzoefu wa Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Kupata Msaada

Ikiwa utakwama au una maswali yoyote kuhusu uundaji wa programu za AI. Jiunge na wanafunzi wenzako na waendelezaji wenye uzoefu katika mijadala kuhusu MCP. Ni jamii yenye msaada ambapo maswali yanakaribishwa na maarifa yanashirikiwa kwa hiari.

[![Discord ya Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Kama una maoni kuhusu bidhaa au unakutana na makosa wakati wa kujenga, tembelea:

[![Jukwaa la Waendelezaji la Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Tamko la kutokuwa na dhamana:
Hati hii imefasiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au kutokuwa sahihi. Nyaraka asili katika lugha yake ya asili inapaswa kuzingatiwa kama chanzo rasmi. Kwa taarifa muhimu, inashauriwa kutumia tafsiri ya kitaalamu iliyofanywa na mtaalamu wa binadamu. Hatuwajibiki kwa kutoelewana au makosa ya tafsiri yanayotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->