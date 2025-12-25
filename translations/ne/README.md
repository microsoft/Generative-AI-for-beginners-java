<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:30:33+00:00",
  "source_file": "README.md",
  "language_code": "ne"
}
-->
# जेनेरेटिभ एआई शुरुवातकर्ताका लागि - Java संस्करण
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![जेनेरेटिभ एआई शुरुवातकर्ताका लागि - Java संस्करण](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ne.png)

**समय प्रतिबद्धता**: सम्पूर्ण कार्यशाला स्थानीय सेटअप बिना अनलाइनमा पूरा गर्न सकिन्छ। वातावरण सेटअपमा 2 मिनेट लाग्छ, र नमूनाहरू अन्वेषण गर्नमा अन्वेषणको गहिराइअनुसार 1-3 घण्टा लाग्न सक्छ।

> **छिटो सुरु** 

1. यो रिपोजिटरीलाई आफ्नो GitHub खातामा Fork गर्नुहोस्
2. क्लिक गर्नुहोस् **Code** → **Codespaces** tab → **...** → **New with options...**
3. पूर्वनिर्धारित विकल्पहरू प्रयोग गर्नुहोस् – यसले यस कोर्सको लागि सिर्जना गरिएको Development container चयन गर्दछ
4. क्लिक गर्नुहोस् **Create codespace**
5. करिब ~2 मिनेट पर्खनुहोस् जबसम्म वातावरण तयार हुँदैन
6. सिधै जानुहोस् [→ पहिलो उदाहरण](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **स्थानीय रूपमा क्लोन गर्न चाहनुहुन्छ?**
>
> यस रिपोजिटरीमा 50+ भाषा अनुवादहरू समावेश छन् जसले डाउनलोड साइजलाई उल्लेखनीय रूपमा बढाउँछ। अनुवादहरू बिना क्लोन गर्नका लागि, sparse checkout प्रयोग गर्नुहोस्:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> यो तपाइँलाई कोर्स पूरा गर्न आवश्यक सबै कुरा दिन्छ र डाउनलोड धेरै छिटो हुन्छ।


## बहु-भाषा समर्थन

### GitHub Action मार्फत समर्थन (स्वचालित र सधैं अद्यावधिक)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[अरबी](../ar/README.md) | [बंगाली](../bn/README.md) | [बुल्गेरियन](../bg/README.md) | [बर्मी (म्यान्मार)](../my/README.md) | [चिनी (सरलीकृत)](../zh/README.md) | [चिनी (परम्परागत, हङकङ)](../hk/README.md) | [चिनी (परम्परागत, मकाऊ)](../mo/README.md) | [चिनी (परम्परागत, ताइवान)](../tw/README.md) | [क्रोएशियन](../hr/README.md) | [चेक](../cs/README.md) | [डेन्िश](../da/README.md) | [डच](../nl/README.md) | [एस्टोनियन](../et/README.md) | [फिनिश](../fi/README.md) | [फ्रान्सेली](../fr/README.md) | [जर्मन](../de/README.md) | [ग्रीक](../el/README.md) | [हिब्रू](../he/README.md) | [हिन्दी](../hi/README.md) | [हंगेरीयन](../hu/README.md) | [इन्डोनेसियन](../id/README.md) | [इटालियन](../it/README.md) | [जापानी](../ja/README.md) | [कन्नडा](../kn/README.md) | [कोरियन](../ko/README.md) | [लिथुआनियाली](../lt/README.md) | [मलय](../ms/README.md) | [मलयालम](../ml/README.md) | [मराठी](../mr/README.md) | [नेपाली](./README.md) | [नाइजेरियन पिडगिन](../pcm/README.md) | [नर्वेजियन](../no/README.md) | [फारसी (पर्शियन)](../fa/README.md) | [पोलिश](../pl/README.md) | [पोर्चुगिज (ब्राजिल)](../br/README.md) | [पोर्चुगिज (पोर्चुगल)](../pt/README.md) | [पञ्जाबी (गुरमुखी)](../pa/README.md) | [रोमानियन](../ro/README.md) | [रूसी](../ru/README.md) | [सर्बियन (सिरिलिक)](../sr/README.md) | [स्लोभाक](../sk/README.md) | [स्लोभेनियन](../sl/README.md) | [स्पेनिश](../es/README.md) | [स्वाहिली](../sw/README.md) | [स्विडिश](../sv/README.md) | [ट्यागालोग (फिलिपिनो)](../tl/README.md) | [तामिल](../ta/README.md) | [तेलुगु](../te/README.md) | [थाई](../th/README.md) | [तुर्की](../tr/README.md) | [युक्रेनी](../uk/README.md) | [उर्दू](../ur/README.md) | [भियतनामी](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## कोर्स संरचना र सिकाइ मार्ग

### **अध्याय 1: जेनेरेटिभ एआई मा परिचय**
- **मुख्य अवधारणाहरू**: ठूलो भाषा मोडेलहरू, टोकनहरू, एम्बेडिङहरू, र एआई क्षमताहरू बुझ्नु
- **Java AI पारिस्थितिकी**: Spring AI र OpenAI SDKs को अवलोकन
- **Model Context Protocol**: MCP र AI एजेन्ट सञ्चारमा यसको भूमिका परिचय
- **व्यावहारिक अनुप्रयोगहरू**: च्याटबोटहरू र सामग्री सिर्जना लगायत वास्तविक संसारका परिदृश्यहरू
- **[→ अध्याय 1 सुरु गर्नुहोस्](./01-IntroToGenAI/README.md)**

### **अध्याय 2: विकास वातावरण सेटअप**
- **बहु-प्रदायक कन्फिगरेसन**: GitHub Models, Azure OpenAI, र OpenAI Java SDK एकीकरणहरू सेटअप गर्नुहोस्
- **Spring Boot + Spring AI**: उद्यम एआई अनुप्रयोग विकासका लागि उत्तम अभ्यासहरू
- **GitHub Models**: प्रोटोटाइप र सिकाइका लागि निःशुल्क AI मोडल पहुँच (क्रेडिट कार्ड आवश्यक छैन)
- **विकास उपकरणहरू**: Docker containers, VS Code, र GitHub Codespaces कन्फिगरेसन
- **[→ अध्याय 2 सुरु गर्नुहोस्](./02-SetupDevEnvironment/README.md)**

### **अध्याय 3: प्रमुख जेनेरेटिभ एआई प्रविधिहरू**
- **प्रम्प्ट इन्जिनियरिङ**: AI मोडलको उत्तम प्रतिक्रियाका लागि तकनीकहरू
- **एम्बेडिङहरू र भेक्टर अपरेसनहरू**: सिमेन्टिक सर्च र समानता मिलान लागू गर्नुहोस्
- **Retrieval-Augmented Generation (RAG)**: AI लाई आफ्नै डेटा स्रोतहरूसँग मिलाउनुहोस्
- **फंक्शन कलिङ**: कस्टम टुलहरू र प्लगइनहरूसँग AI क्षमताहरू विस्तार गर्नुहोस्
- **[→ अध्याय 3 सुरु गर्नुहोस्](./03-CoreGenerativeAITechniques/README.md)**

### **अध्याय 4: व्यावहारिक अनुप्रयोगहरू र परियोजनाहरू**
- **Pet Story Generator** (`petstory/`): GitHub Models सँग सिर्जनात्मक सामग्री सिर्जना
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK सँग स्थानीय AI मोडेल एकीकरण
- **MCP Calculator Service** (`calculator/`): Spring AI सँग आधारभूत Model Context Protocol कार्यान्वयन
- **[→ अध्याय 4 सुरु गर्नुहोस्](./04-PracticalSamples/README.md)**

### **अध्याय 5: जिम्मेवार एआई विकास**
- **GitHub Models सुरक्षा**: निर्मित सामग्री फिल्टरिङ र सुरक्षा मेकानिज्महरू (हार्ड ब्लक र सफ्ट इन्कारहरू) परीक्षण गर्नुहोस्
- **जिम्मेवार एआई डेमो**: आधुनिक AI सुरक्षा प्रणालीहरू कसरी व्यवहारमा काम गर्छन् भन्ने हातेमालो उदाहरण
- **उत्तम अभ्यासहरू**: नैतिक एआई विकास र डिप्लोयमेन्टका लागि आवश्यक मार्गदर्शनहरू
- **[→ अध्याय 5 सुरु गर्नुहोस्](./05-ResponsibleGenAI/README.md)**

## थप स्रोतहरू

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j शुरुवातकर्ताका लागि](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js शुरुवातकर्ताका लागि](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / एजेन्टहरू
[![AZD शुरुवातकर्ताका लागि](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI शुरुवातकर्ताका लागि](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP शुरुवातकर्ताका लागि](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI एजेन्टहरू शुरुवातकर्ताका लागि](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### जेनेरेटिभ एआई शृंखला
[![जेनेरेटिभ एआई शुरुवातकर्ताका लागि](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### मुख्य सिकाइ
[![ML शुरुवातकर्ताका लागि](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![डेटा विज्ञान शुरुवातकर्ताका लागि](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI शुरुवातकर्ताका लागि](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![साइबरसुरक्षा शुरुवातकर्ताका लागि](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![वेब विकास शुरुवातकर्ताका लागि](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुवातकर्ताहरूका लागि IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![शुरुवातकर्ताहरूका लागि XR विकास](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot श्रृंखला
[![AI जोडी प्रोग्रामिङका लागि Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET का लागि Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot साहसिक](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## सहायता प्राप्त गर्नुहोस्

यदि तपाईं अड्किनुहुन्छ वा AI अनुप्रयोगहरू बनाउने बारे कुनै प्रश्नहरू छन् भने। MCP सम्बन्धी छलफलहरूमा अन्य सिक्नेहरू र अनुभवी विकासकर्ताहरूमा सामेल हुनुहोस्। यो एक सहयोगी समुदाय हो जहाँ प्रश्नहरू स्वागत गरिन्छ र ज्ञान स्वतन्त्र रूपमा साझा गरिन्छ।

[![Microsoft Foundry डिस्कोर्ड](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

यदि तपाईंंसँग उत्पादन सम्बन्धी प्रतिक्रिया वा बनाउँदा त्रुटिहरू भएमा भ्रमण गर्नुहोस्:

[![Microsoft Foundry विकासकर्ता फोरम](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
अस्वीकरण:
यो दस्तावेज कृत्रिम बुद्धिमत्ता अनुवाद सेवा Co-op Translator (https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी शुद्धताका लागि प्रयासरत भए तापनि, कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटि वा अशुद्धता हुनसक्छ। मूल भाषामा रहेको दस्तावेजलाई अधिकृत स्रोतको रूपमा मानिनु पर्छ। महत्त्वपूर्ण जानकारीका लागि पेशेवर मानवीय अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलत बुझाइ वा गलत व्याख्याका लागि हामी उत्तरदायी छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->