# ജെനറേറ്റീവ് AI ആരംഭക്കാർക്ക് - ജावा പതിപ്പ്
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/ml/beg-genai-series.8b48be9951cc574c.webp)

**സമയം വെച്ചുള്ള പ്രതിബദ്ധത**: ഈ മുഴുവൻ വർക്‌ഷോപ്പ് ഓൺലൈനായി, പ്രാദേശിക സജ്ജീകരണം വേണ്ടാതെ പൂർത്തിയാക്കാവുന്നതാണ്. പരിസ്ഥിതി സജ്ജീകരണം 2 മിനിറ്റ് വേണ്ടി വരും, സാമ്പിളുകൾ പരിശോധിക്കുന്നതിന് 1-3 മണിക്കൂർ വേണമാകും, പരിശോധിക്കൽ അളവിനു അനുസരിച്ച്.

> **വേഗം തുടങ്ങാം**

1. ഈ റിപോസിറ്ററി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്ക് ഫോർക്കുചെയ്യുക
2. **Code** → **Codespaces** ടാബ് → **...** → **New with options...** ക്ലിക്കുചെയ്യുക
3. ഡിഫോൾറൂട്ടുകൾ ഉപയോഗിക്കുക – ഇതു കോഴ്‌സിന് സൃഷ്ടിച്ച ഡവലപ്മെന്റ് കണ്ടെയ്‌നർ തിരഞ്ഞെടുക്കും
4. **Create codespace** ക്ലിക്ക് ചെയ്യുക
5. പരിസ്ഥിതി റെഡിയാകാൻ ഏകദേശം 2 മിനിറ്റ് കാത്തിരിക്കുക
6. നേരിട്ട് [The first example](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) തുറക്കുക

## ബഹുഭാഷാ പിന്തുണ

### GitHub Action വഴിയായി പിന്തുണ (സ്വയംമാറ്റം & എപ്പോഴും പുതുക്കപ്പെട്ടത്)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](./README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **പ്രാദേശികമായി ക്ലോൺ ചെയ്യണമെന്ന് ആഗ്രഹിക്കുന്നുവോ?**
>
> ഈ റിപോസിറ്ററിയിൽ 50-ത്തിലധികം ഭാഷാ വിവർത്തനങ്ങൾ ഉൾപ്പെടുന്നു, ഇത് ഡൗൺലോഡ് വലിപ്പം ക്രമീകരിക്കുന്നു. വിവർത്തനങ്ങൾ അല്ലാതെ ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
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
> ഇത് കോഴ്സ് പൂർത്തിയാക്കാൻ ആവശ്യമായ എല്ലാ വസ്തുക്കളും വളരെ വേഗത്തിൽ ഡൗൺലോഡ് ചെയ്യുന്നതിന് സഹായിക്കും.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## കോഴ്‌സ് ഘടന & പഠന പാത

### **അധ്യായം 1: ജെനറേറ്റീവ് AI-ന്റെ вступление**
- **അടിസ്ഥാന ആശയങ്ങൾ**: വലിയ ഭാഷാ മാതൃകകൾ, ടോക്കണുകൾ, എംബെഡ്ഡിംഗുകൾ, AI കഴിവുകൾ മനസ്സിലാക്കൽ
- **ജാവ AI പരിസ്ഥിതിവ്യവസ്ഥ**: സ്പ്രിംഗ് AI & OpenAI SDKകൾക്ക് അവലോകനം
- **മോഡൽ കോൺടെക്സ് പ്രോട്ടോക്കോളിന്റെ പരിചയം**: MCP-ന്റെ പരിചയം, AI ഏജന്റുകളുടെ സംവാദത്തിലുള്ള പങ്ക്
- **പ്രായോഗിക ഉപയോഗങ്ങൾ**: ചാറ്റ്‌ബോട്ടുകളും ഉള്ളടക്കം സൃഷ്ടിയും ഉൾപ്പെടുന്നു
- **[→ Chapter 1 തുടങ്ങുക](./01-IntroToGenAI/README.md)**

### **അധ്യായം 2: ഡവലപ്മെന്റ് പരിസ്ഥിതി സജ്ജീകരണം**
- **ബഹു-പ്രൊവൈഡർ കോൺഫിഗറേഷൻ**: GitHub Models, Azure OpenAI, OpenAI Java SDK സംയോജനം സജ്ജീകരണം
- **സ്പ്രിംഗ് ബൂട്ട് + സ്പ്രിംഗ് AI**: എന്റർപ്രൈസ് AI ആപ്ലിക്കേഷൻ നിർമ്മാണത്തിന് മികച്ച രീതികൾ
- **GitHub Models**: പ്രോട്ടോടൈപ്പിംഗും പഠനത്തിനുമായ സൗജന്യ AI മോഡൽ ആക്സസ് (ക്രീഡിറ്റ് കാർഡ് ആവശ്യമില്ല)
- **ഡവലപ്മെന്റ് ഉപകരണങ്ങൾ**: Docker കണ്ടെയ്‌നറുകൾ, VS കോഡ്, GitHub Codespaces കോൺഫിഗറേഷൻ
- **[→ Chapter 2 തുടങ്ങുക](./02-SetupDevEnvironment/README.md)**

### **അധ്യായം 3: കോർ ജെനറേറ്റീവ് AI സാങ്കേതികവിദ്യകൾ**
- **പ്രോപ്റ്റ് എൻജിനീയറിംഗ്**: മികച്ച AI മാതൃക പ്രതികരണങ്ങൾക്കുള്ള സാങ്കേതിക തന്ത്രങ്ങൾ
- **എംബെഡ്ഡിംഗുകളും വെക്റ്റർ ഓപ്പറേഷനുകളും**: സെമാന്റിക് തിരയലും സാമ്യത അനുബന്ധവും നടപ്പിലാക്കൽ
- **Retrieval-Augmented Generation (RAG)**: നിങ്ങളുടെ ഡേറ്റാ സ്രോതസ്സുകളുമായി AI സംയോജിപ്പിക്കൽ
- **ഫങ്‌ഷൻ കോളിംഗ്**: സ്വന്തം ഉപകരണങ്ങളും പ്ലഗിനുകളുമായി AI കഴിവുകൾ വിപുലീകരിക്കൽ
- **[→ Chapter 3 തുടങ്ങുക](./03-CoreGenerativeAITechniques/README.md)**

### **അധ്യായം 4: പ്രായോഗിക അപ്ലിക്കേഷനുകളും പ്രോജക്ടുകളും**
- **പെറ്റ് സ്റ്റോറി ജനറേറ്റർ** (`petstory/`): GitHub Models ഉപയോഗിച്ച് സൃഷ്ടിപരമായ ഉള്ളടക്കം സൃഷ്ടിക്കൽ
- **ഫൗണ്ടറി ലോക്കൽ ഡെമോ** (`foundrylocal/`): OpenAI ജავა SDK-ഉപയോഗിച്ച് പ്രാദേശിക AI മോഡൽ സംയോജനം
- **MCP കാൽക്കുലേറ്റർ സർവീസ്** (`calculator/`): സ്പ്രിംഗിൽ MCP അടിസ്ഥാന പ്രയോഗം
- **[→ Chapter 4 തുടങ്ങുക](./04-PracticalSamples/README.md)**

### **അധ്യായം 5: ഉത്തരവാദിത്വമുള്ള AI വികസനം**
- **GitHub Models സുരക്ഷ**: ഉൾനിർമ്മിത ഉള്ളടക്ക ഫിൽറ്ററിംഗ്, സുരക്ഷാ സംവിധാനങ്ങൾ (ഹാർഡ് ബ്ലോക്കുകളും സോഫ്റ്റ് നിരസിക്കലുകളും) പരീക്ഷണം
- **ഉത്തരവാദിത്വമുള്ള AI ഡെമോ**: ആധുനിക AI സുരക്ഷാ സംവിധാനങ്ങളുടെ പ്രായോഗിക ഉദാഹരണം
- **മികച്ച രീതികൾ**: നയപരമായ AI ഡെവലപ്പ്മെന്റ്‌ക്കും വിന്യാസത്തിനും ആവശ്യമായ മാർഗനിർദ്ദേശങ്ങൾ
- **[→ Chapter 5 തുടങ്ങുക](./05-ResponsibleGenAI/README.md)**

## അധിക സ്രോതസ്സുകൾ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### ലാങ്ചെയിൻ
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain for Beginners](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### ആസിയൂർ / എഡ്‌ജ് / MCP / ഏജന്റുകൾ
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ജെനറേറ്റീവ് AI പരമ്പര
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### കോർ പഠനമാർഗങ്ങൾ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)

[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### കോപൈലറ്റ് സീരീസ്
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## സഹായം ലഭിക്കുക

എഐ ആപ്പുകൾ നിർമ്മിക്കുമ്പോൾ നിങ്ങൾക്ക് തടസമുണ്ടായാൽ അല്ലെങ്കിൽ ഏതെങ്കിലും ചോദ്യങ്ങളുണ്ടെങ്കിൽ, MCP-നെ കുറിച്ചുള്ള സംഭാഷണങ്ങളിൽ മറ്റുള്ള പഠിക്കുന്നവരുമായും പരിചയസമ്പന്നരായ ഡെവലപ്പർമാരുമായും പങ്കുചേരുക. ചോദ്യങ്ങൾക്ക് സ്വീകാര്യതയുള്ളതും അറിവ് സ്വതന്ത്രമായി പങ്കുവെക്കപ്പെടുന്നതുമായ ഒരു പിന്തുണയ്‌ക്കുള്ള സമൂഹമാണ് ഇവിടം.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ഉൽപ്പന്നത്തിന് പ്രതികരണം നൽകാനോ നിർമ്മാണത്തിനിടെ പിഴവുകൾ അനുഭവപ്പെടുന്നുവെങ്കിൽ സന്ദർശിക്കുക:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ഡിസ്ക്ലെയ്ിമർ**:  
ഈ ഡോക്യൂമെന്റ് AI വിവർത്തന സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്‌തു. ഞങ്ങൾ യഥാർത്ഥതയ്ക്കായി പരിശ്രമിക്കുന്നുവെങ്കിലും, സ്വയം നിയന്ത്രിത വിവർത്തനങ്ങളിൽ തെറ്റുകൾ അല്ലെങ്കിൽ അശുദ്ധികൾ ഉണ്ടാകാനുള്ള സാധ്യതയുള്ളതാണെന്നത് ദയവായി മനസ്സിലാക്കുക. മാതൃഭാഷയിൽ ഉള്ള മൗലിക ഡോക്യൂമെന്റിനെ അത്യന്തം വിശ്വസനീയമായ ഉറവിടമായി കണക്കാക്കണം. നിർണ്ണായക വിവരങ്ങൾക്കായി പ്രൊഫഷണൽ മനുഷ്യൻ്റെ വിവർത്തനം ശിപാർശ ചെയ്യപ്പെടുന്നു. ഈ വിവർത്തനം ഉപയോഗിച്ചതിൽ നിന്നുണ്ടാകുന്ന എന്തെങ്കിലും തെറ്റിദ്ധാരണകൾക്കും അപേക്ഷാദോഷങ്ങൾക്കും ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->