# ప్రారంభీకుల కోసం జనరేటివ్ AI - జావా సంచిక  
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)  

![Generative AI for Beginners - Java Edition](../../translated_images/te/beg-genai-series.8b48be9951cc574c.webp)  

**సమయ వ్యయము**: మొత్తం వర్క్‌షాప్ స్థానిక సెటప్ లేకుండా ఆన్‌లైన్‌లో పూర్తిచేయవచ్చు. పరిసరాలు సెట్ చేయడానికి 2 నిమిషాలు పడతాయి, నమూనాలను పరిశీలించడానికి అన్వేషణ లోతుకు అనుగుణంగా 1-3 గంటలు అవసరం అవుతుంది.  

> **త్వరిత ప్రారంభం**  

1. ఈ రిపోజిటరీని మీ గిట్హబ్ ఖాతాకు ఫోర్క్ చేయండి  
2. క్లిక్ చేయండి **Code** → **Codespaces** ట్యాబ్ → **...** → **New with options...**  
3. డిఫాల్టులను ఉపయోగించండి – ఇది ఈ కోర్సుకు రూపొందించిన డెవలప్‌మెంట్ కంటైనర్‌ను ఎంచుకుంటుంది  
4. క్లిక్ చేయండి **Create codespace**  
5. పరిసరాలు సిద్ధం అయ్యే వరకు సుమారు ~2 నిమిషాలు వేచి ఉండండి  
6. నేరుగా [మొదటి ఉదాహరణకు](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) జంప్ అవ్వండి  

## బహుభాషా సపోర్టు  

### గిట్‌హబ్ అక్టియాన్ ద్వారా మద్దతు (ఆటోమేటెడ్ & ఎల్లప్పుడూ నూతనీకరించబడుతుంది)  

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->  
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](./README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)  

> **స్థానికంగా క్లోన్ చేయాలనుకుంటున్నారా?**  
>  
> ఈ రిపోజిటరీలో 50కి పైగా భాషా అనువాదాలు ఉన్నాయి, ఇవి డౌన్‌లోడ్ పరిమాణాన్ని గణనీయంగా పెంచుతాయి. అనువాదాల లేకుండా క్లోన్ చేయడానికి, స్పార్స్ చెకౌట్ ఉపయోగించండి:  
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
> ఇది మీరు కోర్సును పూర్తి చేయడానికి అవసరమైన అన్ని ఫైల్స్‌ను వేగంగా డౌన్‌లోడ్ చేయడానికి సహాయపడుతుంది.  
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->  

## కోర్సు నిర్మాణం & అభ్యాస మార్గం  

### **అధ్యాయం 1: జనరేటివ్ AIకు పరిచయం**  
- **ప్రధాన భావనల**: పెద్ద భాషా నమూనాలు, టోకెన్లు, ఎంబెడ్డింగ్స్, మరియు AI సామర్థ్యాల అవగాహన  
- **జావా AI పరిసరాలు**: Spring AI మరియు OpenAI SDKల ఓవerview  
- **మోడల్ కాంటెక్స్ట్ ప్రోటోకాల్**: MCP పరిచయం మరియు AI ఏజెంట్ కమ్యూనికేషన్‌లో దాని పాత్ర  
- **ప్రయత్నాత్మక వినియోగాలు**: చాట్‌బాట్లు మరియు కంటెంట్ జనరేషన్ సహా నిజజీవిత సందర్భాలు  
- **[→ అధ్యాయం 1 ప్రారంభించండి](./01-IntroToGenAI/README.md)**  

### **అధ్యాయం 2: డెవలప్‌మెంట్ పరిసరాల సెటప్**  
- **బహు-ప్రొవైడర్ కాన్ఫిగరేషన్**: GitHub Models, Azure OpenAI, మరియు OpenAI Java SDK సమీక్ష  
- **Spring Boot + Spring AI**: ఎంటర్‌ప్రైజ్ AI యాప్ అభివృద్ధి ఉత్తమ సాధనాలు  
- **GitHub Models**: ప్రోటోటైపింగ్ మరియు అభ్యాసానికి ఉచిత AI మోడల్ యాక్సెస్ (క్రెడిట్ కార్డు అవసరం లేదు)  
- **డెవలప్‌మెంట్ టూల్స్**: Docker కంటైనర్లు, VS Code, మరియు GitHub Codespaces సెటప్  
- **[→ అధ్యాయం 2 ప్రారంభించండి](./02-SetupDevEnvironment/README.md)**  

### **అధ్యాయం 3: ప్రధాన జనరేటివ్ AI సాంకేతికతలు**  
- **ప్రాంప్ట్ ఇంజనీئرింగ్**: ఉత్తమ AI మోడల్ స్పందనల కోసం విధానాలు  
- **ఎంబెడ్డింగ్స్ & వెక్టార్ ఆపరేషన్‌లు**: సేమాంటిక్ సెర్చ్ మరియు సమానత్వ మ్యాచ్ చేయడం  
- **రిట్రీవల్-ఆగ్మెంటెడ్ జనరేషన్ (RAG)**: మీ స్వంత డేటా మూలాల తో AI కలయిక  
- **ఫంక్షన్ కాలింగ్**: కస్టమ్ టూల్స్ మరియు ప్లగిన్లతో AI సామర్ధ్యాలు విస్తరించడం  
- **[→ అధ్యాయం 3 ప్రారంభించండి](./03-CoreGenerativeAITechniques/README.md)**  

### **అధ్యాయం 4: ప్రయత్నాత్మక వినియోగాలు & ప్రాజెక్టులు**  
- **పెట్ కథా రూపొందింపు** (`petstory/`): GitHub Models‌తో సృజనాత్మక కంటెంట్ జనరేషన్  
- **ఫౌండ్రీ స్థానిక డెమో** (`foundrylocal/`): OpenAI Java SDKతో స్థానిక AI మోడల్ ఇంటిగ్రేషన్  
- **MCP క్యాల్క్యులేటర్ సర్వీస్** (`calculator/`): Spring AIతో ప్రాథమిక మోడల్ కాంటెక్స్ట్ ప్రోటోకాల్ అమలు  
- **[→ అధ్యాయం 4 ప్రారంభించండి](./04-PracticalSamples/README.md)**  

### **అಧ್ಯాయం 5: బాధ్యతాయుత AI అభివృద్ధి**  
- **GitHub Models భద్రత**: బిల్ట్-ఇన్ కంటెంట్ ఫిల్టరింగ్ మరియు భద్రతా యంత్రాంగాలు పరీక్ష (కఠిన నిరోధాలు మరియు సౌమ్యఅంగీకారాలు)  
- **బాధ్యతాయుత AI డెమో**: ఆధునిక AI భద్రతా వ్యవస్థలు ఎలా పనిచేసినాయో చూపించే హ్యాండ్స్-ఆన్ ఉదాహరణ  
- **ఉత్తమ అభ్యాసాలు**: నైతిక AI అభివృద్ధి మరియు అమలుకు ముఖ్యమైన మార్గదర్శకాలు  
- **[→ అధ్యాయం 5 ప్రారంభించండి](./05-ResponsibleGenAI/README.md)**  

## అదనపు వనరులు  

<!-- CO-OP TRANSLATOR OTHER COURSES START -->  
### లాంగ్‌చైన్  
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)  
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)  
[![LangChain for Beginners](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)  
---  

### Azure / Edge / MCP / ఏజెంట్స్  
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)  

---  
 
### జనరేటివ్ AI సిరీస్  
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)  
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)  
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)  

---  
 
### ప్రధాన అభ్యాసం  
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)  
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)  
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)  
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### కాపిల్ సిరీస్
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## సహాయం పొందడం

మీరు ఆగిపోయినా లేదా AI యాప్స్ నిర్మించడంపై ఏవైనా ప్రశ్నలు ఉంటే. MCP గురించి చర్చల్లో అనుభవజ్ఞులైన డెవలపర్లు మరియు ఇతర అభ్యసిస్తున్న సభ్యులతో చేరండి. ఇది ప్రశ్నలు స్వాగతించబడే మరియు జ్ఞానం స్వేచ్ఛగా పంచుకునే మద్దతొందించే సమూహం.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

మీకు ఉత్పత్తి అభిప్రాయం లేదా నిర్మాణంలో తప్పుడు సమాచారం ఉంటే సందర్శించండి:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**డిస్క్లెయిమర్**:
ఈ డాక్యుమెంట్‌ను AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము దృఢంగా ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, స్వయంచాలక అనువాదాల్లో తప్పులు లేదా లోపాలు ఉండవచ్చు అని దయచేసి గమనించండి. మూల డాక్యుమెంట్ తన స్థానిక భాషలోనే అధికారిక మూలంగా పరిగణించబడాలి. కీలక సమాచారం కోసం, నిపుణుల చేత మనుషుల అనువాదాన్ని సూచిస్తాం. ఈ అనువాదం ఉపయోగంతో జరిగిన ఏ ప‌రార్ధాలు లేదా తప్పుదూఢలకూ మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->