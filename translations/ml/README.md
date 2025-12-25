<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T07:22:34+00:00",
  "source_file": "README.md",
  "language_code": "ml"
}
-->
# ജനറേറ്റീവ് എഐ ആരംഭക്കാർക്കായി - ജാവാ പതിപ്പ്
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ജനറേറ്റീവ് എഐ ആരംഭക്കാർക്കായി - ജാവാ പതിപ്പ്](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ml.png)

**സമയം പ്രതിബദ്ധത**: ഇതെല്ലാം പ്രാദേശിക സെറ്റപ് ചെയ്യാതെയും ഓൺലൈനിൽ പൂർത്തിയാക്കാം. പരിസ്ഥിതി സജ്ജീകരണം ഏകദേശം 2 മിനിറ്റ് എടുക്കും; സാമ്പിളുകൾ പരിശോധിക്കാൻ ആവശ്യമായ സമയം അന്വേഷനത്തിന്റെ ആഴത്വത്തെ ആശ്രയിച്ചാണ്, സാധാരണയായി 1-3 മണിക്കൂർ.

> **ത്വരിത ആരംഭം** 

1. ഈ റിപ്പോസിറ്ററി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്ക് Fork ചെയ്യുക
2. **Code** → **Codespaces** ടാബ് → **...** → **New with options...** ക്ലിക്ക് ചെയ്യുക
3. ഡിഫോള്റ്ടുകൾ ഉപയോഗിക്കുക – ഇത് ഈ കോഴ്സിന് സൃഷ്ടിച്ച Development container തിരഞ്ഞെടുക്കും
4. **Create codespace** ക്ലിക്ക് ചെയ്യുക
5. പരിസ്ഥിതി തയ്യാറാകാൻ ~2 മിനിറ്റ് കാത്തിരിക്കൂ
6. നേരിട്ട് [ആദ്യം ഉദാഹരണം](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) കാണുക

> **പ്രാദേശികമായി ക്ലോൺ ചെയ്യാൻ ഇഷ്ടമുണ്ടോ?**
>
> ഈ റിപ്പോസിറ്ററിയിൽ 50+ ഭാഷാ വിവർത്തനങ്ങൾ ഉൾക്കൊള്ളിച്ചിരിക്കുന്നത് ഡൗൺലോഡ് വലുപ്പം ഗണ്യമായി വർദ്ധിപ്പിക്കുന്നു. വിവർത്തനങ്ങൾ ഒഴികെയാക്കി ക്ലോൺ ചെയ്യാൻ sparse checkout ഉപയോഗിക്കുക:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ഇത് കോഴ്‌സ് പൂർത്തിയാക്കാൻ ആവശ്യമായ എല്ലാ കാര്യങ്ങളും നൽകും, ഡൗൺലോഡ് വളരെ വേഗത്തിൽ നടക്കും.


## ബഹുഭാഷാ പിന്തുണ

### GitHub Action വഴി പിന്തുണ (സ്വയംചാലിതവും എല്ലായ്പ്പോഴും പുതുക്കി നിലനിർത്തപ്പെട്ടത്)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](./README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../br/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## കോഴ്‌സ് ഘടനയും പഠന പഥവും

### **അദ്ധ്യായം 1: ജനറേറ്റീവ് എഐ പരിചയം**
- **പ്രധാന ആശയങ്ങൾ**: വലിയ ഭാഷാ മോഡലുകൾ, ടോക്കണുകൾ, എംബഡിങ്ങുകൾ, എഐ ശേഷികൾ എന്നിവയെക്കുറിച്ചുള്ള തിരിച്ചറിവ്
- **ജാവാ എഐ പരിസ്ഥിതി**: Spring AI, OpenAI SDKs എന്നിവയുടെ അവലೋಕനം
- **Model Context Protocol**: MCP പരിചയപ്പെടുത്തൽയും AI ഏജന്റ് ആശയവിനിമയത്തിൽ അതിന്റെ പങ്കും
- **പ്രായോഗിക പ്രയോഗങ്ങൾ**: ചാറ്റ്ബോട്ടുകളും ഉള്ളടക്കം സൃഷ്ടിക്കൽ ഉൾപ്പെടെയുള്ള വൈദ്യുതി അനുഭവങ്ങൾ
- **[→ Start Chapter 1](./01-IntroToGenAI/README.md)**

### **അദ്ധ്യായം 2: വികസന പരിസ്ഥിതി സജ്ജീകരണം**
- **ബഹു-പ്രൊവൈഡർ ക്രമീകരണം**: GitHub Models, Azure OpenAI, OpenAI Java SDK ഇന്റഗ്രേഷനുകൾ സജ്ജമാക്കൽ
- **Spring Boot + Spring AI**: എന്റർപ്രൈസ് എഐ ആപ്ലിക്കേഷൻ വികസനത്തിന് മികച്ച രീതികൾ
- **GitHub Models**: പ്രോട്ടോടൈപ്പിംഗിനും പഠനത്തിനും സൗജന്യമായി മോഡൽ ആക്സസ് (ക്രെഡിറ്റ് കാർഡ് ആവശ്യമില്ല)
- **വികസന ഉപകരണങ്ങൾ**: Docker കണ്ടെയ്‌നറുകൾ, VS Code, GitHub Codespaces ക്രമീകരണം
- **[→ Start Chapter 2](./02-SetupDevEnvironment/README.md)**

### **അദ്ധ്യായം 3: പ്രാഥമിക ജനറേറ്റീവ് എഐ സാങ്കേതികവിദ്യകൾ**
- **Prompt Engineering**: മികച്ച എഐ മോഡൽ പ്രതികരണങ്ങൾ നേടാനുള്ള സാങ്കേതിക വിദ്യകൾ
- **Embeddings & Vector Operations**: സെമാന്റിക് തിരയലും സാമ്യമുള്ള മെച്ചപ്പെട്ട മാച്ചിങ്ങും നടപ്പിലാക്കൽ
- **Retrieval-Augmented Generation (RAG)**: നിങ്ങളുടെ സ്വന്തം ഡാറ്റാ സ്രോതസ്സുകളുമായി എഐ സംയോജിപ്പിക്കൽ
- **Function Calling**: കസ്റ്റം ടൂളുകൾ കൂടാതെ പ്ലഗിനുകൾ വഴി എഐ കഴിവുകൾ വിപുലീകരിക്കുക
- **[→ Start Chapter 3](./03-CoreGenerativeAITechniques/README.md)**

### **അദ്ധ്യായം 4: പ്രായോഗിക പ്രയോഗങ്ങളും പ്രോജക്റ്റുകളും**
- **Pet Story Generator** (`petstory/`): GitHub Models ഉപയോഗിച്ച് സൃഷ്ടിപരമായ ഉള്ളടക്കം നിർമിക്കൽ
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK ഉപയോഗിച്ച് ലോക്കൽ AI മോഡൽ ഇന്റഗ്രേഷൻ
- **MCP Calculator Service** (`calculator/`): Spring AI ഉപയോഗിച്ച് അടിസ്ഥാന Model Context Protocol നടപ്പാക്കൽ
- **[→ Start Chapter 4](./04-PracticalSamples/README.md)**

### **അദ്ധ്യായം 5: ഉത്തരവാദിത്വമുള്ള എഐ വികസനം**
- **GitHub Models Safety**: ബിൽറ്റ്-ഇൻ ഉള്ളടക്ക ഫിൽട്ടറിംഗും സുരക്ഷാ മെക്കാനിസങ്ങളുടെയും (ഹാർഡ് ബ്ലോക്കുകളും സോഫ്റ്റ് നിരാകരണങ്ങളും) പരീക്ഷണം
- **Responsible AI Demo**: ആധുനിക എഐ സുരക്ഷാ സംവിധാനങ്ങൾ പ്രായോഗികമായി എങ്ങനെ പ്രവർത്തിക്കുന്നു എന്നത് കാണിക്കുന്ന ഹാൻഡ്‌സ്-ഓൺ ഉദാഹരണം
- **Best Practices**: നൈതികമായ എഐ വികസനത്തിനും വിന്യാസത്തിനും വേണ്ട അടിസ്ഥാന മാർഗ്ഗനിർദ്ദേശങ്ങൾ
- **[→ Start Chapter 5](./05-ResponsibleGenAI/README.md)**

## അധിക വിഭവങ്ങൾ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative AI Series
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Core Learning
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ആരംഭക്കാർക്കുള്ള IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ആരംഭക്കാർക്കുള്ള XR വികസനം](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot പരമ്പര
[![AI ജോഡി പ്രോഗ്രാമിങ്ങിനുള്ള Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET-യ്ക്കുള്ള Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot അഡ്വഞ്ചർ](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## സഹായം

AI ആപ്പുകൾ നിർമ്മിക്കുന്നതിനെക്കുറിച്ച് നിങ്ങൾക്ക് കുടുങ്ങുകയാണെങ്കിൽ അല്ലെങ്കിൽ എന്തെങ്കിലും ചോദ്യം ഉണ്ടെങ്കിൽ. MCP-യെക്കുറിച്ചുള്ള ചർച്ചകളിൽ മറ്റ് പഠിക്കുന്നവരും പരിചയസമ്പന്നരായ ഡെവലപ്പർങ്ങളും ചേർന്ന് പങ്കെടുക്കൂ. ചോദ്യങ്ങൾക്ക് സ്വാഗതം പറയപ്പെടുന്ന, അറിവ് സ്വതന്ത്രമായി പങ്കിടപ്പെടുന്ന ഒരു പിന്തുണയുള്ള സമൂഹമാണ് ഇത്.

[![Microsoft Foundry ഡിസ്കോർഡ്](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

നിർമ്മിക്കുമ്പോൾ ഉത്പന്നം സംബന്ധിച്ച പ്രതികരണം അല്ലെങ്കിൽ തെറ്റുകൾ ഉണ്ടെങ്കിൽ സന്ദർശിക്കുക:

[![Microsoft Foundry ഡെവലപ്പർ ഫോറം](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
അസ്വീകരണ കുറിപ്പ്:
ഈ രേഖ AI വിവർത്തന സേവനമായ [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്തതാണ്. നാം കൃത്യതയ്ക്കായി ശ്രമിച്ചുവെങ്കിലും, യന്ത്രവൽക്കൃത വിവർത്തനങ്ങളിൽ പിശകുകളും അപൂർണ്ണതകളും ഉണ്ടാകാവുന്നതാണെന്ന് ദയവായി ശ്രദ്ധിക്കുക. മൂല രേഖ അതിന്റെ മാതൃഭാഷയിലുള്ള പതിപ്പാണ് അധികാരപരമായ ഉറവിടമായി പരിഗണിക്കേണ്ടത്. നിർണായകമായ വിവരങ്ങൾക്ക് പ്രൊഫഷണൽ മനുഷ്യവിവർത്തനം ശുപാർശ ചെയ്യുന്നു. ഈ വിവർത്തനം ഉപയോഗിച്ചതിനാൽ ഉണ്ടാകുന്ന ഏതെങ്കിലും തെറ്റിദ്ധാരണകൾക്കും തെറ്റായ വ്യാഖ്യാനങ്ങൾക്കും ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->