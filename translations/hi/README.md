<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:21:49+00:00",
  "source_file": "README.md",
  "language_code": "hi"
}
-->
# जनरेटिव AI शुरुआती के लिए - Java संस्करण
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![जनरेटिव AI शुरुआती के लिए - Java संस्करण](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.hi.png)

**समय प्रतिबद्धता**: पूरा वर्कशॉप बिना लोकल सेटअप के ऑनलाइन पूरा किया जा सकता है। वातावरण सेटअप में 2 मिनट लगते हैं, और सैंपल्स का अन्वेषण करने में अन्वेषण की गहराई के आधार पर 1-3 घंटे लग सकते हैं।

> **त्वरित शुरुआत** 

1. इस रिपॉज़िटरी को अपने GitHub अकाउंट पर fork करें
2. क्लिक करें **Code** → **Codespaces** टैब → **...** → **New with options...**
3. डिफ़ॉल्ट का उपयोग करें – यह इस कोर्स के लिए बनाए गए Development container का चयन करेगा
4. क्लिक करें **Create codespace**
5. वातावरण तैयार होने के लिए ~2 मिनट प्रतीक्षा करें
6. सीधे कूदें [पहला उदाहरण](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **क्या आप लोकल पर क्लोन करना पसंद करते हैं?**
>
> यह रिपॉज़िटरी 50+ भाषा अनुवादों को शामिल करती है जो डाउनलोड आकार को काफी बढ़ा देती है। अनुवादों के बिना क्लोन करने के लिए sparse checkout का उपयोग करें:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> यह आपको कोर्स पूरा करने के लिए आवश्यक सबकुछ देता है और डाउनलोड को बहुत तेज़ कर देता है।


## बहु-भाषा समर्थन

### GitHub Action के माध्यम से समर्थित (स्वचालित और हमेशा अद्यतित)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[अरबी](../ar/README.md) | [बांग्ला](../bn/README.md) | [बुल्गारियाई](../bg/README.md) | [बर्मी (म्यांमार)](../my/README.md) | [चीनी (सरलीकृत)](../zh/README.md) | [चीनी (पारंपरिक, हॉन्ग कॉन्ग)](../hk/README.md) | [चीनी (पारंपरिक, मकाउ)](../mo/README.md) | [चीनी (पारंपरिक, ताइवान)](../tw/README.md) | [क्रोएशियाई](../hr/README.md) | [चेक](../cs/README.md) | [डेनिश](../da/README.md) | [डच](../nl/README.md) | [एस्टोनियाई](../et/README.md) | [फिन्निश](../fi/README.md) | [फ्रेंच](../fr/README.md) | [जर्मन](../de/README.md) | [ग्रीक](../el/README.md) | [हिब्रू](../he/README.md) | [हिंदी](./README.md) | [हंगेरियन](../hu/README.md) | [इंडोनेशियाई](../id/README.md) | [इटालियन](../it/README.md) | [जापानी](../ja/README.md) | [कन्नड़](../kn/README.md) | [कोरियाई](../ko/README.md) | [लिथुआनियाई](../lt/README.md) | [मलय](../ms/README.md) | [मलयालम](../ml/README.md) | [मराठी](../mr/README.md) | [नेपाली](../ne/README.md) | [नाइजीरियाई पाजन](../pcm/README.md) | [नॉर्वेजियन](../no/README.md) | [फ़ारसी (Farsi)](../fa/README.md) | [पोलिश](../pl/README.md) | [पुर्तगाली (ब्राज़ील)](../br/README.md) | [पुर्तगाली (पुर्तगाल)](../pt/README.md) | [पंजाबी (गुरमुखी)](../pa/README.md) | [रोमानियाई](../ro/README.md) | [रूसी](../ru/README.md) | [सर्बियाई (सिरिलिक)](../sr/README.md) | [स्लोवाक](../sk/README.md) | [स्लोवेनियाई](../sl/README.md) | [स्पेनिश](../es/README.md) | [स्वाहिली](../sw/README.md) | [स्वीडिश](../sv/README.md) | [तागालोग (फ़िलिपिनो)](../tl/README.md) | [तमिल](../ta/README.md) | [तेलुगु](../te/README.md) | [थाई](../th/README.md) | [तुर्की](../tr/README.md) | [यूक्रेनी](../uk/README.md) | [उर्दू](../ur/README.md) | [वियतनामी](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## कोर्स संरचना और सीखने का मार्ग

### **अध्याय 1: जनरेटिव AI का परिचय**
- **मुख्य अवधारणाएँ**: Large Language Models, tokens, embeddings, और AI क्षमताओं की समझ
- **Java AI पारिस्थितिकी तंत्र**: Spring AI और OpenAI SDKs का अवलोकन
- **Model Context Protocol**: MCP का परिचय और AI एजेंट संचार में इसकी भूमिका
- **व्यावहारिक अनुप्रयोग**: चैटबॉट्स और कंटेंट जेनरेशन सहित वास्तविक दुनिया के परिदृश्य
- **[→ अध्याय 1 शुरू करें](./01-IntroToGenAI/README.md)**

### **अध्याय 2: विकास पर्यावरण सेटअप**
- **मल्टी-प्रोवाइडर कॉन्फ़िगरेशन**: GitHub Models, Azure OpenAI, और OpenAI Java SDK इंटीग्रेशन सेटअप करें
- **Spring Boot + Spring AI**: एंटरप्राइज़ AI एप्लिकेशन विकास के लिए सर्वोत्तम प्रथाएँ
- **GitHub Models**: प्रोटोटाइपिंग और सीखने के लिए मुफ्त AI मॉडल पहुँच (क्रेडिट कार्ड आवश्यक नहीं)
- **विकास उपकरण**: Docker containers, VS Code, और GitHub Codespaces कॉन्फ़िगरेशन
- **[→ अध्याय 2 शुरू करें](./02-SetupDevEnvironment/README.md)**

### **अध्याय 3: मुख्य जनरेटिव AI तकनीकें**
- **प्रॉम्प्ट इंजीनियरिंग**: AI मॉडल की उत्तम प्रतिक्रियाओं के लिए तकनीकें
- **Embeddings और वेक्टर ऑपरेशन्स**: सेमन्टिक सर्च और समानता मिलान को लागू करें
- **Retrieval-Augmented Generation (RAG)**: अपने डेटा स्रोतों के साथ AI को संयोजित करें
- **Function Calling**: कस्टम टूल्स और प्लगइन्स के साथ AI क्षमताओं को बढ़ाएँ
- **[→ अध्याय 3 शुरू करें](./03-CoreGenerativeAITechniques/README.md)**

### **अध्याय 4: व्यावहारिक अनुप्रयोग और परियोजनाएँ**
- **Pet Story Generator** (`petstory/`): GitHub Models के साथ रचनात्मक सामग्री जेनरेशन
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK के साथ लोकल AI मॉडल इंटीग्रेशन
- **MCP Calculator Service** (`calculator/`): Spring AI के साथ बेसिक Model Context Protocol कार्यान्वयन
- **[→ अध्याय 4 शुरू करें](./04-PracticalSamples/README.md)**

### **अध्याय 5: ज़िम्मेदार AI विकास**
- **GitHub Models सुरक्षा**: बिल्ट-इन कंटेंट फिल्टरिंग और सुरक्षा तंत्रों (कठोर ब्लॉक्स और सॉफ्ट रिजेक्शन्स) का परीक्षण करें
- **ज़िम्मेदार AI डेमो**: मॉडर्न AI सुरक्षा प्रणालियों के काम करने का व्यावहारिक उदाहरण
- **सर्वोत्तम प्रथाएँ**: नैतिक AI विकास और परिनियोजन के लिए आवश्यक मार्गदर्शिकाएँ
- **[→ अध्याय 5 शुरू करें](./05-ResponsibleGenAI/README.md)**

## अतिरिक्त संसाधन

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j शुरुआती के लिए](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js शुरुआती के लिए](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD शुरुआती के लिए](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI शुरुआती के लिए](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP शुरुआती के लिए](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI एजेंट्स शुरुआती के लिए](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### जनरेटिव AI श्रृंखला
[![जनरेटिव AI शुरुआती के लिए](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### मुख्य अध्ययन
[![ML शुरुआती के लिए](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![डेटा साइंस शुरुआती के लिए](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI शुरुआती के लिए](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![साइबरसिक्योरिटी शुरुआती के लिए](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![वेब डेवलपमेंट शुरुआती के लिए](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT के लिए शुरुआती](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR विकास के लिए शुरुआती](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot श्रृंखला
[![AI सह-प्रोग्रामिंग के लिए Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET के लिए Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot एडवेंचर](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## सहायता प्राप्त करें

यदि आप अटक जाते हैं या AI ऐप्स बनाने के बारे में कोई प्रश्न हैं, तो सहकर्मी शिक्षार्थियों और अनुभवी डेवलपर्स में शामिल हों और MCP के बारे में चर्चा करें। यह एक सहयोगी समुदाय है जहाँ प्रश्नों का स्वागत है और ज्ञान स्वतंत्र रूप से साझा किया जाता है।

[![Microsoft Foundry डिस्कॉर्ड](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

यदि आपके पास उत्पाद संबंधी प्रतिक्रिया है या निर्माण के दौरान त्रुटियाँ आ रही हैं, तो जाएँ:

[![Microsoft Foundry डेवलपर फ़ोरम](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
अस्वीकरण:
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। हालांकि हम सटीकता के लिए प्रयत्नशील हैं, कृपया ध्यान रखें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल भाषा में मौज़ूद मूल दस्तावेज़ को अधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए पेशेवर मानव अनुवाद की सलाह दी जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफ़हमी या गलत व्याख्या के लिए हम जिम्मेदार नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->