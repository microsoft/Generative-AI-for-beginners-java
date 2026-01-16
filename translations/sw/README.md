<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:40:30+00:00",
  "source_file": "README.md",
  "language_code": "sw"
}
-->
# AI Inazalisha kwa Waanzilishi - Toleo la Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/sw/beg-genai-series.8b48be9951cc574c.webp)

**Muda wa Kujitolea**: Warsha yote inaweza kukamilika mtandaoni bila usanidi wa eneo lako. Usanidi wa mazingira unachukua dakika 2, na kuchunguza sampuli kunahitaji masaa 1-3 kulingana na kina cha utafiti.

> **Anza Haraka** 

1. Tenga hifadhidata hii kwenye akaunti yako ya GitHub
2. Bonyeza **Code** → kichupo cha **Codespaces** → **...** → **New with options...**
3. Tumia mipangilio ya msingi – hii itachagua kontena la Maendeleo lililoundwa kwa kozi hii
4. Bonyeza **Create codespace**
5. Subiri ~dakika 2 ili mazingira yawe tayari
6. Nenda moja kwa moja kwenye [Mfano wa kwanza](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Unapendelea Kukopa Kwenye Kifaa Chako?**
>
> Hifadhidata hii ina tafsiri za lugha 50+ ambazo huongeza ukubwa wa kupakua kwa kiasi kikubwa. Ili kukopa bila tafsiri, tumia sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Hii inakupa kila kitu unachohitaji kukamilisha kozi hii kwa upakuaji wa haraka zaidi.


## Msaada wa Lugha Nyingi

### Inasaidiwa kupitia Kitendo cha GitHub (Otomatiki & Kila wakati ni cha Kisasa)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Kiarabu](../ar/README.md) | [Kibengali](../bn/README.md) | [Kibulgaria](../bg/README.md) | [Kiburma (Myanmar)](../my/README.md) | [Kichina (Rahisi)](../zh/README.md) | [Kichina (Mila, Hong Kong)](../hk/README.md) | [Kichina (Mila, Macau)](../mo/README.md) | [Kichina (Mila, Taiwan)](../tw/README.md) | [Kikroeshia](../hr/README.md) | [Kicheki](../cs/README.md) | [Kidenmaki](../da/README.md) | [Kiholanzi](../nl/README.md) | [Kiestonia](../et/README.md) | [Kifini](../fi/README.md) | [Kifaransa](../fr/README.md) | [Kijerumani](../de/README.md) | [Kigiriki](../el/README.md) | [Kiebrania](../he/README.md) | [Kihindi](../hi/README.md) | [Kihungari](../hu/README.md) | [Kiindonesia](../id/README.md) | [Kiitaliano](../it/README.md) | [Kijapani](../ja/README.md) | [Kikannada](../kn/README.md) | [Kikorea](../ko/README.md) | [Kilithuania](../lt/README.md) | [Kimalay](../ms/README.md) | [Kimalayalamu](../ml/README.md) | [Kimarathi](../mr/README.md) | [Kinepali](../ne/README.md) | [Kipidgin cha Nigeria](../pcm/README.md) | [Kinorwe](../no/README.md) | [Kifarsi (Persia)](../fa/README.md) | [Kilahisia](../pl/README.md) | [Kireno (Brazil)](../br/README.md) | [Kireno (Portugal)](../pt/README.md) | [Kipunajabi (Gurmukhi)](../pa/README.md) | [Kiromania](../ro/README.md) | [Kirusi](../ru/README.md) | [Kiserbia (Cyrillic)](../sr/README.md) | [Kislovak](../sk/README.md) | [Kislovenia](../sl/README.md) | [Kihispania](../es/README.md) | [Kiswahili](./README.md) | [Kiswidi](../sv/README.md) | [Kitagalogo (Filipino)](../tl/README.md) | [Kitamili](../ta/README.md) | [Kitelugu](../te/README.md) | [Kithai](../th/README.md) | [Kituruki](../tr/README.md) | [Kiukraini](../uk/README.md) | [Kiurdu](../ur/README.md) | [Kivietinamu](../vi/README.md)

> **Unapendelea Kukopa Kwenye Kifaa Chako?**

> Hifadhidata hii ina tafsiri za lugha 50+ ambazo huongeza ukubwa wa kupakua kwa kiasi kikubwa. Ili kukopa bila tafsiri, tumia sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Hii inakupa kila kitu unachohitaji kukamilisha kozi hii kwa upakuaji wa haraka zaidi.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Muundo wa Kozi & Njia ya Kujifunza

### **Sura ya 1: Utangulizi wa AI Inazalisha**
- **Dhana Muhimu**: Kuelewa Modeli Kubwa za Lugha, tokeni, embeddings, na uwezo wa AI
- **Mfumo wa AI wa Java**: Muhtasari wa Spring AI na SDK za OpenAI
- **Itifaki ya Muktadha wa Modeli**: Utangulizi wa MCP na jukumu lake katika mawasiliano ya mawakala wa AI
- **Matumizi ya Kivitendo**: Mipangilio halisi ikiwa ni pamoja na chatbots na uzalishaji wa maudhui
- **[→ Anza Sura ya 1](./01-IntroToGenAI/README.md)**

### **Sura ya 2: Usanidi wa Mazingira ya Maendeleo**
- **Usanidi wa Watoaji Wengi**: Tayarisha GitHub Models, Azure OpenAI, na ushirikiano wa OpenAI Java SDK
- **Spring Boot + Spring AI**: Mbinu bora za maendeleo ya programu za AI za biashara
- **GitHub Models**: Ufikiaji wa modeli za AI bure kwa ajili ya majaribio na kujifunza (hakuna kadi ya mkopo inahitajika)
- **Zana za Maendeleo**: Kontena za Docker, VS Code, na usanidi wa GitHub Codespaces
- **[→ Anza Sura ya 2](./02-SetupDevEnvironment/README.md)**

### **Sura ya 3: Mbinu Muhimu za AI Inazalisha**
- **Uhandisi wa Prompt**: Mbinu bora za majibu ya modeli za AI
- **Embeddings & Uendeshaji wa Vector**: Tekeleza utafutaji wa maana na kulinganisha kufanana
- **Uzalishaji Ulioendeshwa na Urejeshaji (RAG)**: Changanya AI na vyanzo vyako vya data
- **Kupiga Simu za Kazi**: Panua uwezo wa AI kwa zana na plugins za kawaida
- **[→ Anza Sura ya 3](./03-CoreGenerativeAITechniques/README.md)**

### **Sura ya 4: Matumizi na Miradi ya Kivitendo**
- **Mtengenezaji wa Hadithi za Wanyama** (`petstory/`): Uzalishaji wa maudhui ya ubunifu kwa GitHub Models
- **Demo ya Foundry Local** (`foundrylocal/`): Ushirikiano wa modeli ya AI ya eneo la OpenAI Java SDK
- **Huduma ya Kalkuleta MCP** (`calculator/`): Utekelezaji wa msingi wa Itifaki ya Muktadha wa Modeli kwa Spring AI
- **[→ Anza Sura ya 4](./04-PracticalSamples/README.md)**

### **Sura ya 5: Maendeleo ya AI kwa Uwajibikaji**
- **Usalama wa GitHub Models**: Jaribu vichujio vilivyojengwa na mifumo ya usalama (vizuizi ngumu na kukataa kwa upole)
- **Demo ya AI Inayohusika kwa Uwajibikaji**: Mfano wa vitendo unaoonesha jinsi mifumo ya kisasa ya usalama ya AI inavyofanya kazi
- **Mbinu Bora**: Miongozo muhimu kwa maendeleo na uanzishaji wa AI wa maadili
- **[→ Anza Sura ya 5](./05-ResponsibleGenAI/README.md)**

## Rasilimali Zaidi

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kwa Waanzilishi](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kwa Waanzilishi](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Wakala
[![AZD kwa Waanzilishi](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI kwa Waanzilishi](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kwa Waanzilishi](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Wakala wa AI kwa Waanzilishi](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa AI Inazalisha
[![AI Inazalisha kwa Waanzilishi](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Inazalisha (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![AI Inazalisha (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![AI Inazalisha (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Kujifunza Msingi
[![ML kwa Waanzilishi](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Sayansi ya Data kwa Waanzilishi](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI kwa Waanzilishi](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Usalama wa Mtandao kwa Waanzilishi](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Uendelezaji wa Mtandao kwa Waanzilishi](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT kwa Waanzilishi](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Maendeleo ya XR kwa Waanzilishi](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Mfululizo wa Copilot
[![Copilot kwa Kuprogramu Pamoja ya AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot kwa C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Kupata Msaada

Iwapo utakumbwa na shida au una maswali yoyote kuhusu jinsi ya kujenga programu za AI. Jiunge na wajifunzaji wenzako na waendelezaji wenye uzoefu katika mijadala kuhusu MCP. Ni jamii yenye msaada ambapo maswali yanakaribishwa na maarifa hugawanywa kwa huru.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Ikiwa una maoni kuhusu bidhaa au hitilafu wakati wa kujenga tembelea:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kengele ya Hukumu**:
Hati hii imetafsiriwa kwa kutumia huduma ya utafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Wakati tunajitahidi kwa usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au kasoro. Hati asili katika lugha yake ya asili inapaswa kuchukuliwa kama chanzo halali. Kwa taarifa muhimu, tafsiri ya mtaalamu wa binadamu inashauriwa. Hatubeba uwajibikaji wowote kwa kutoelewana au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->