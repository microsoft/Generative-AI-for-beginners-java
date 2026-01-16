<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T10:05:27+00:00",
  "source_file": "README.md",
  "language_code": "ml"
}
-->
# ജെനറേറ്റീവ് AI ആരംഭക്കാർക്ക് - ജावा പതിപ്പ്
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ജെനറേറ്റീവ് AI ആരംഭക്കാർക്ക് - ജावा പതിപ്പ്](../../translated_images/ml/beg-genai-series.8b48be9951cc574c.webp)

**സമയം:** മുഴുവൻ വർക്ക്ഷോപ്പ് ഓൺലൈനിൽ സജ്ജീകരണമില്ലാതെ പൂർത്തിയാക്കാം. പരിസ്ഥിതി സജ്ജീകരണം 2 മിനിറ്റ് വേണ്ടി വരും, സാമ്പിളുകൾ അന്വേഷിക്കാനുള്ള സമയം 1-3 മണിക്കൂർ വരെ കൂടുതല്‍ ആഴത്തിൽ നോക്കുന്നതിന് അനുസരിച്ചായി.

> **വേഗത്തിലുള്ള തുടക്കം**

1. ഈ റിപ്പോസിറ്ററി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്ക് ഫോർക്കുചെയ്യുക
2. **Code** → **Codespaces** ടാബ് → **...** → **New with options...** ക്ലിക്ക് ചെയ്യുക
3. ഡിഫോൾട്ടുകൾ ഉപയോഗിക്കുക – ഇത് ഈ കോഴ്‌സിനായി സൃഷ്ടിച്ച ഡെവലപ്പ്മെന്റ് കണ്ടെയ്നറാണ് തിരഞ്ഞെടുക്കുക
4. **Create codespace** ക്ലിക്ക് ചെയ്യുക
5. പരിസ്ഥിതി സജ്ജമാകാൻ ~2 മിനിറ്റ് കാത്തിരിക്കുക
6. നേരിട്ട് [മുതൽ ഉദാഹരണം](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) കാണുക

> **പ്രാദേശികമായി ക്ലോൺ ചെയ്യാൻ ആഗ്രഹിക്കുന്നുണ്ടോ?**
>
> ഈ റിപ്പോസിറ്ററിയിൽ 50+ ഭാഷാ പരിഭാഷകൾ ഉൾപ്പെടുന്നു, ഇത് ഡൗൺലോഡ് വലുതാക്കും. പരിഭാഷകൾ ഇല്ലാതെ ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ഇതോടെ കോഴ്സ് പൂർത്തിയാക്കാൻ വേണ്ടതെല്ലാം വളരെ വേഗത്തിൽ ലഭിക്കും.


## ബഹुभാഷാ പിന്തുണ

### GitHub ആക്ഷൻ വഴി പിന്തുണ (സ്വയം പ്രവർത്തനക്ഷമവും എപ്പൊഴും അപ്പ്‌ടുഡേറ്റും)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[അറബി](../ar/README.md) | [ബംഗാളി](../bn/README.md) | [ബൾഗേറിയൻ](../bg/README.md) | [ബർമീസ് (മ്യാൻമാർ)](../my/README.md) | [ചൈനീസ് (ലഘുകൃതം)](../zh/README.md) | [ചൈനീസ് (പരമ്പരാഗതം, ഹോങ്കോംഗ്)](../hk/README.md) | [ചൈനീസ് (പരമ്പരാഗതം, മക്കാവ്)](../mo/README.md) | [ചൈനീസ് (പരമ്പരാഗതം, തായ്‌വാൻ)](../tw/README.md) | [ക്രൊയേഷ്യൻ](../hr/README.md) | [ചെക്ക്](../cs/README.md) | [ഡാനിഷ്](../da/README.md) | [ഡച്ച്](../nl/README.md) | [എസ്റ്റോണിയൻ](../et/README.md) | [ഫിന്നിഷ്](../fi/README.md) | [ഫ്രഞ്ച്](../fr/README.md) | [ജർമ്മൻ](../de/README.md) | [ഗ്രീക്ക്](../el/README.md) | [ഹീബ്രു](../he/README.md) | [ഹിന്ദി](../hi/README.md) | [ഹംഗേറിയൻ](../hu/README.md) | [ഇന്തോനേഷ്യൻ](../id/README.md) | [ഇറ്റാലിയൻ](../it/README.md) | [ജാപ്പനീസ്](../ja/README.md) | [കന്നഡ](../kn/README.md) | [കൊറിയൻ](../ko/README.md) | [ലിതൗനിയൻ](../lt/README.md) | [മലായ്](../ms/README.md) | [മലയാളം](./README.md) | [മറാത്തി](../mr/README.md) | [നെപ്പാളി](../ne/README.md) | [നൈജീരിയൻ പിഡ്‌ജിൻ](../pcm/README.md) | [നോർവീജിയൻ](../no/README.md) | [പേർഷ്യൻ (ഫാർസി)](../fa/README.md) | [പോളിഷ്](../pl/README.md) | [പോർചുഗീസ് (ബ്രസീൽ)](../br/README.md) | [പോർചുഗീസ് (പോർച്ചുഗൽ)](../pt/README.md) | [പഞ്ചാബി (ഗുരുമുഖി)](../pa/README.md) | [റൊമാനിയൻ](../ro/README.md) | [റഷ്യൻ](../ru/README.md) | [സെർബിയൻ (സിറിലിക്)](../sr/README.md) | [സ്ലോവാക്](../sk/README.md) | [സ്ലോവേനിയൻ](../sl/README.md) | [സ്‌പാനിഷ്](../es/README.md) | [സ്വാഹിലി](../sw/README.md) | [സ്വീഡിഷ്](../sv/README.md) | [ടാഗാലോഗ് (ഫിലിപ്പീൻ)](../tl/README.md) | [തമിഴ്](../ta/README.md) | [तेलुगു](../te/README.md) | [തായ്](../th/README.md) | [ടർക്കിഷ്](../tr/README.md) | [ഉക്രൈനിൻ](../uk/README.md) | [ഉർദു](../ur/README.md) | [വിയറ്റ്നാമീസ്](../vi/README.md)

> **പ്രാദേശികമായി ക്ലോൺ ചെയ്യാൻ ആഗ്രഹിക്കുന്നുണ്ടോ?**

> ഈ റിപ്പോസിറ്ററിയിൽ 50+ ഭാഷാ പരിഭാഷകൾ ഉൾപ്പെടുന്നു, ഇത് ഡൗൺലോഡ് വലുതാക്കും. പരിഭാഷകൾ ഇല്ലാതെ ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ഇതോടെ കോഴ്സ് പൂർത്തിയാക്കാൻ വേണ്ടതെല്ലാം വളരെ വേഗത്തിൽ ലഭിക്കും.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## കോഴ്‌സ് ഘടനയും പഠനപാതയും

### **അദ്ധ്യായം 1: ജെനറേറ്റീവ് AI-യുടെ പരിചയം**
- **കോർ ധാരണകൾ**: വലിയ ഭാഷ മോഡലുകൾ, ടോക്കണുകൾ, എംബെഡിംഗുകൾ, AI കഴിവുകൾ മനസിലാക്കൽ
- **ജാവ AI പരിസ്ഥിതി**: Spring AI-യും OpenAI SDK-കളും അവലോകനം
- **മോഡൽ കോൺറ്റെക്സ് പ്രോട്ടോക്കോൾ**: MCP-യുടെ പരിചയം, AI ഏജന്റുകളുടെ ബന്ധപ്പെട്ട ആവിശ്യ
- **പ്രായോഗിക ഉപയോഗങ്ങൾ**: ചാറ്റ്ബോട്ടുകൾ, ഉള്ളടക്കം സൃഷ്ടിക്കൽ തുടങ്ങിയ യഥാർത്ഥ ലോക സാഹചര്യങ്ങൾ
- **[→ അദ്ധ്യായം 1 ആരംഭിക്കുക](./01-IntroToGenAI/README.md)**

### **അദ്ധ്യായം 2: ഡെവലപ്പ്മെന്റ് പരിസ്ഥിതി സജ്ജീകരണം**
- **ബഹുസ്ഥാപക കോൺഫിഗറേഷൻ**: GitHub മോഡലുകൾ, Azure OpenAI, OpenAI ജावा SDK ഇന്റഗ്രേഷൻ സജ്ജീകരിക്കുക
- **Spring Boot + Spring AI**: എന്റർപ്രൈസ് AI അപ്ലിക്കേഷൻ വികസനത്തിനായുള്ള മികച്ച രീതികൾ
- **GitHub മോഡലുകൾ**: പ്രോട്ടോടെപിംഗ്, പഠനത്തിനായി സൗജന്യ AI മോഡൽ ആക്‌സസ് (ക്രെഡിറ്റ് കാർഡ് ആവശ്യമില്ല)
- **ഡെവലപ്പ്മെന്റ് ടൂളുകൾ**: Docker കണ്ടെയ്നറുകൾ, VS Code, GitHub Codespaces കോൺഫിഗറേഷൻ
- **[→ അദ്ധ്യായം 2 ആരംഭിക്കുക](./02-SetupDevEnvironment/README.md)**

### **അദ്ധ്യായം 3: പ്രധാന ജെനറേറ്റീവ് AI സാങ്കേതിക വിദ്യകൾ**
- **പ്രോമ്പ്റ്റ് എഞ്ചിനീയറിംഗ്**: AI മോഡലുകളെ മികച്ച മറുപടി നൽകാൻ സാങ്കേതിക വിദ്യകൾ
- **എംബെഡിംഗുകളും വെക്ടർ പ്രവർത്തനങ്ങളും**: സെമാന്റിക് തിരയലും സാമ്യമുള്ള പൊരുത്തം കണ്ടെത്തലും നടപ്പാക്കുക
- **റിട്രീവൽ-ഓഗ്മെന്റഡ് ജനറേഷൻ (RAG)**: നിങ്ങളുടെ സ്വന്തം വിവര സംഭരണികളുമായി AI സംയോജിപ്പിക്കുക
- **ഫങ്ക്ഷൻ കോളിംഗ്**: കസ്റ്റം ടൂളുകളുമായി AI കഴിവുകൾ വർദ്ധിപ്പിക്കുക
- **[→ അദ്ധ്യായം 3 ആരംഭിക്കുക](./03-CoreGenerativeAITechniques/README.md)**

### **അദ്ധ്യായം 4: പ്രായോഗിക ഉപയോഗങ്ങളും പ്രോജക്ടുകളും**
- **പെറ്റ് സ്റ്റോറി ജനറേറ്റർ** (`petstory/`): GitHub മോഡലുകളുമായി സൃഷ്ടിമാന ഉള്ളടക്കം
- **ഫൗണ്ട്രി ലോക്കൽ ഡെമോ** (`foundrylocal/`): OpenAI ജावा SDK ഉപയോഗിച്ച് പ്രാദേശിക AI മോഡൽ സംയോജനം
- **MCP കാൽക്കുലേറ്റർ സർവീസ്** (`calculator/`): Spring AI ഉപയോഗിച്ച് അടിസ്ഥാന മോഡൽ കോൺറ്റെക്സ് പ്രോട്ടോക്കോൾ നടപ്പാക്കൽ
- **[→ അദ്ധ്യായം 4 ആരംഭിക്കുക](./04-PracticalSamples/README.md)**

### **അദ്ധ്യായം 5: ഉത്തരവാദിത്വമുള്ള AI വികസനം**
- **GitHub മോഡലുകളുടെ സുരക്ഷ**: കോണ്ടന്റ് ഫിൽട്ടറിംഗ്, ഹാർഡ് ബ്ലോക്കുകളും സോഫ്റ്റ് നിരാകരണങ്ങളും പരീക്ഷിക്കുക
- **ഉത്തരവാദിത്വമുള്ള AI ഡെമോ**: ആധുനിക AI സുരക്ഷാ സംവിധാനങ്ങൾ പ്രായോഗികമായി കാണിക്കുന്ന ഹാൻഡ്‌സ്-ഓൺ ഉദാഹരണം
- **മികച്ച പ്രാക്ടീസുകൾ**: നയപരമായ AI വികസനത്തിനും വിനിയോഗത്തിനും ആവശ്യമായ മാർഗ്ഗനിർദ്ദേശങ്ങൾ
- **[→ അദ്ധ്യായം 5 ആരംഭിക്കുക](./05-ResponsibleGenAI/README.md)**

## അധിക വിവരസ്രോതസ്സുകൾ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![മുതിർന്നവർക്കുള്ള LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![മുതിർന്നവർക്കുള്ള LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / ഏജന്റുകൾ
[![മുതിർന്നവർക്കുള്ള AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![മുതിർന്നവർക്കുള്ള Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![മുതിർന്നവർക്കുള്ള MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![മുതിർന്നവർക്കുള്ള AI ഏജന്റുകൾ](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ജെനറേറ്റീവ് AI സീരീസ്
[![ജെനറേറ്റീവ് AI ആരംഭക്കാർക്ക്](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ജെനറേറ്റീവ് AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![ജെനറേറ്റീവ് AI (ജാവ)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![ജെനറേറ്റീവ് AI (ജാവസ്‌ക്രിപ്റ്റ്)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### കോർ പഠനം
[![മുതിർന്നവർക്കുള്ള ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![മുതിർന്നവർക്കുള്ള ഡാറ്റാ സയൻസ്](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![മുതിർന്നവർക്കുള്ള AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![മുതിർന്നവർക്കുള്ള സൈബർസെക്യൂരിറ്റി](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![മുതിർന്നവർക്കുള്ള വെബ് ഡെവ്](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### കോപിലട്ട് ശ്രേണി
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## സഹായം നേടൽ

നിങ്ങൾ കുടുങ്ങിയാൽ അല്ലെങ്കിൽ AI ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കുന്നതിനെപ്പറ്റി ഏതൊന്നെങ്കിലും ചോദ്യങ്ങൾ ഉണ്ടെങ്കിൽ. MCP-യെക്കുറിച്ച് fellow learners-ഉം പരിചയസമ്പന്നരായ ഡെവലപ്പർമാരും ചേർന്നുള്ള ചർച്ചകളിൽ പങ്കെടുക്കുക. ഇത് ചോദ്യങ്ങൾ സ്വാഗതം ചെയ്യപ്പെടുന്ന ഒരു പിന്തുണയുള്ള സമുദായമാണ്, അവിടെ അറിവ് സ്വതന്ത്രമായി തരുൽപ്പെടുന്നു.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

നിങ്ങൾക്ക് ഉൽപ്പന്ന ഫീഡ്ബാക്ക് അല്ലെങ്കിൽ പിഴവുകൾ ഉണ്ടെങ്കിൽ നിർമ്മിക്കുന്ന സമയത്ത് സന്ദർശിക്കുക:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ഡിസ്ക്ലെയിമർ**:
ഈ രേഖ AI വിവർത്തന సేవയായ [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്തതാണ്. ഞങ്ങൾ ശരിയായ വിവർത്തനത്തിനായി പരിശ്രമിക്കുന്നിട്ടും, സ്വയമേറ്റം വിവർത്തനങ്ങളിൽ പിഴവുകൾ അല്ലെങ്കിൽ അപൂർവതകൾ ഉണ്ടായേക്കാമെന്ന് ദയവായി ശ്രദ്ധിക്കണം. അടിസ്ഥാനഭാഷയിലുള്ള സാഹചര്യം പരിഗണിച്ച് ആ യഥാർത്ഥ രേഖയാണ് പ്രമുഖമായ സ്രോതസം. महत्वपूर्णവും സാരമാണാത്ത വിവരങ്ങൾക്കായി പ്രൊഫഷണൽ മനുഷ്യവിവർത്തനം ശിപാർശ ചെയ്യപ്പെടുന്നു. ഈ വിവർത്തനം ഉപയോഗിച്ചതിൽ നിന്നുണ്ടാകുന്ന എന്തെങ്കിലും അർത്ഥവ്യത്യാസങ്ങളോ തെറ്റിദ്ധാരണങ്ങളോ സംബന്ധിച്ചുള്ള ബാധ്യത ഞങ്ങൾ ഏറ്റെടുക്കുന്നില്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->