<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T07:18:13+00:00",
  "source_file": "README.md",
  "language_code": "te"
}
-->
# జెనరేటివ్ AI ప్రారంభికులకు - జావా ఎడిషన్
[![Microsoft Foundry డిస్కార్డ్](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![జెనరేటివ్ AI ప్రారంభికులకు - జావా ఎడిషన్](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.te.png)

**సమయ ప్రతిబద్ధత**: మొత్తం వర్క్‌షాప్ స్థానిక సెటప్ లేకుండా ఆన్‌లైన్‌లో పూర్తవుతుంది. వాతావరణాన్ని సెట్ చేయడానికి సుమారు 2 నిమిషాలు పడతాయి, మరియు ఉదాహరణలను అన్వేషించడానికి, అన్వేషణ లోతు ఆధారంగా, 1-3 గంటలు అవసరమవచ్చు.

> **త్వరిత ప్రారంభం** 

1. ఈ రిపోజిటరీని మీ GitHub ఖాతాలో fork చేయండి
2. Click **Code** → **Codespaces** tab → **...** → **New with options...**
3. డిఫాల్ట్ సెట్టింగ్స్‌ను ఉపయోగించండి – ఇది ఈ కోర్సుకు తయారు చేసిన Development container ను ఎంచుకుంటుంది
4. Click **Create codespace**
5. వాతావరణం సిద్ధం కావడానికి సుమారు ~2 నిమిషాలు వేచి ఉండండి
6. నేరుగా [మొదటి ఉదాహరణ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)కి వెళ్లండి

> **లోకల్‌గా క్లోన్ చేయాలనుకుంటున్నారా?**
>
> ఈ రిపోజిటరీలో 50+ భాషా అనువాదాలు ఉన్నాయి, ఇవి డౌన్లోడ్ పరిమాణాన్ని గణనీయంగా పెంచుతాయి. అనువాదాలు లేకుండా క్లోన్ చేయాలంటే, sparse checkout ఉపయోగించండి:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ఇది కోర్సును పూర్తి చేయడానికి అవసరమైన అన్నింటినీ మరింత వేగవంతమైన డౌన్లోడ్‌తో మీకు ఇస్తుంది.


## బహుభాషా మద్దతు

### GitHub Action ద్వారా మద్దతు (స్వయంచాలకీకృతం & ఎప్పుడూ తాజా)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../br/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](./README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## కోర్సు నిర్మాణం మరియు నేర్చుకునే మార్గం

### **అధ్యాయం 1: జెనరేటివ్ AI పరిచయం**
- **ప్రధాన భావాలు**: Large Language Models, టోకెన్లు, ఎంబెడ్డింగ్స్, మరియు AI సామర్థ్యాలను అర్థం చేసుకోవడం
- **జావా AI పరిసరాలు**: Spring AI మరియు OpenAI SDKs యొక్క అవలోకనం
- **మోడల్ కాంటెక్స్ట్ ప్రోటోకాల్**: MCP పరిచయం మరియు AI ఏజెంట్ కమ్యూనికేషన్‌లో దాని పాత్ర
- **ప్రయోగాత్మక అనువర్తనాలు**: చాట్‌బాట్స్ మరియు కంటెంట్ జనరేషన్ వంటి వాస్తవ ప్రపంచ సందర్భాలు
- **[→ అధ్యాయం 1 ప్రారంభించండి](./01-IntroToGenAI/README.md)**

### **అధ్యాయం 2: అభివృద్ధి వాతావరణ సెటప్**
- **బహు-ప్రొవైడర్ కాన్ఫిగరేషన్**: GitHub Models, Azure OpenAI, మరియు OpenAI Java SDK ఇంటిగ్రేషన్లను సెటప్ చేయడం
- **Spring Boot + Spring AI**: ఎంటర్‌ప్రైజ్ AI అనువర్తన అభివృద్ధికి ఉత్తమ ప్రాక్టీసులు
- **GitHub Models**: ప్రోటోటైపింగ్ మరియు నేర్చుకునే ఉద్దేశ్యాలకు ఉచిత AI మోడల్ యాక్సెస్ (క్రెడిట్ కార్డ్ అవసరం లేదు)
- **అభివృద్ధి టూల్స్**: Docker కాన్టెయినర్లు, VS Code, మరియు GitHub Codespaces కాన్ఫిగరేషన్
- **[→ అధ్యాయం 2 ప్రారంభించండి](./02-SetupDevEnvironment/README.md)**

### **అధ్యాయం 3: కోర్ జెనరేటివ్ AI సాంకేతికతలు**
- **ప్రాంప్ట్ ఇంజనీరింగ్**: ఆప్టిమల్ AI మోడల్ ప్రతిస్పందనలు పొందటానికి సాంకేతికలు
- **ఎంబెడ్డింగ్స్ & వెక్టర్ ఆపరేషన్లు**: సెమాంటిక్ సెర్చ్ మరియు సమానత్వ మ్యాచ్‌ను అమలు చేయండి
- **Retrieval-Augmented Generation (RAG)**: AI ను మీ స్వంత డేటా వనరులతో మిళితం చేయండి
- **ఫంక్షన్ కాలింగ్**: కస్టమ్ టూల్స్ మరియు ప్లగిన్లతో AI సామర్థ్యాలను విస్తరించడం
- **[→ అధ్యాయం 3 ప్రారంభించండి](./03-CoreGenerativeAITechniques/README.md)**

### **అధ్యాయం 4: ప్రయోగాత్మక అనువర్తనాలు & ప్రాజెక్టులు**
- **పెట్ స్టోరి జనరేటర్** (`petstory/`): GitHub Models తో సృజనాత్మక కంటెంట్ జనరేషన్
- **Foundry స్థానిక డెమో** (`foundrylocal/`): OpenAI Java SDK తో స్థానిక AI మోడల్ ఇంటిగ్రేషన్
- **MCP క్యాల్క్యులేటర్ సర్వీస్** (`calculator/`): Spring AI తో ప్రాథమిక Model Context Protocol అమలు
- **[→ అధ్యాయం 4 ప్రారంభించండి](./04-PracticalSamples/README.md)**

### **అధ్యాయం 5: బాధ్యతాయుత AI అభివృద్ధి**
- **GitHub Models సేఫ్టీ**: నిర్మింపబడ్డ కంటెంట్ ఫిల్టరింగ్ మరియు సేఫ్టీ మెకానిజమ్‌లను (హార్డ్ బ్లాక్స్ మరియు సాఫ్ట్ రిఫ్యూజల్స్) పరీక్షించండి
- **బాధ్యతాయుత AI డెమో**: ఆధునిక AI సేఫ్టీ వ్యవస్థలు ప్రాక్టికల్‌గా ఎలా పని చేస్తున్నాయో చూపే హ్యాండ్స్ఓన్ ఉదాహరణ
- **ఉత్తమ ప్రాక్టీసులు**: నైతిక AI అభివృద్ధి మరియు డిప్లాయ్‌మెంట్ కోసం ముఖ్యమైన మార్గదర్శకాలు
- **[→ అధ్యాయం 5 ప్రారంభించండి](./05-ResponsibleGenAI/README.md)**

## అదనపు వనరులు

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j ప్రారంభికులకు](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js ప్రారంభికులకు](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / ఏజెంట్స్
[![AZD ప్రారంభికులకు](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI ప్రారంభికులకు](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP ప్రారంభికులకు](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ఏజెంట్స్ ప్రారంభికులకు](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### జెనరేటివ్ AI సిరీస్
[![జెనరేటివ్ AI ప్రారంభికులకు](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![జెనరేటివ్ AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![జెనరేటివ్ AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![జెనరేటివ్ AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ప్రాథమిక విషయాలు
[![మెషిన్ లెర్నింగ్ ప్రారంభికులకు](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![డేటా సైన్స్ ప్రారంభికులకు](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ప్రారంభికులకు](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![సైబర్‌సెక్యూరిటీ ప్రారంభికులకు](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![వెబ్ డెవలప్‌మెంట్ ప్రారంభికులకు](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ప్రారంభుల కోసం IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ప్రారంభుల కోసం XR అభివృద్ధి](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot సిరీస్
[![AI జత ప్రోగ్రామింగ్ కోసం Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET కోసం Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot అడ్వెంచర్](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## సహాయం పొందండి

మీరు చిక్కిపోతే లేదా AI అనువర్తనాలు రూపొందించడం గురించి ఏమైనా ప్రశ్నలు ఉంటే, MCP గురించి చర్చల్లో ఇతర అభ్యసనకారులు మరియు అనుభవజ్ఞులైన డెవలపర్లతో చేరండి. ఇది ప్రశ్నలకు స్వాగతం చెప్పే మరియు జ్ఞానాన్ని స్వేచ్ఛగా పంచుకునే సహాయక సమూహం.

[![Microsoft Foundry డిస్కోర్డ్](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

మీకు ఉత్పత్తి ఫీడ్‌బ్యాక్ లేదా నిర్మాణ సమయంలో పొరపాట్లు ఉంటే, సందర్శించండి:

[![Microsoft Foundry డెవలపర్ ఫోరమ్](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
నిరాకరణ:
ఈ పత్రాన్ని AI అనువాద సేవ Co‑op Translator (https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించారు. మేము ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాల్లో తప్పులు లేదా లోపాలు ఉండొచ్చని దయచేసి గమనించండి. మూల భాషలోని డాక్యుమెంట్నే అధికారిక/ప్రామాణిక మూలంగా పరిగణించాలి. కీలకమైన సమాచారానికి వృత్తిపరమైన మానవ అనువాదాన్ని పొందాలని సూచించబడుతుంది. ఈ అనువాదం ఉపయోగించినందున ఏర్పడే ఏవైనా అవగాహనా లోపాలు లేదా తప్పుదారితలకు మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->