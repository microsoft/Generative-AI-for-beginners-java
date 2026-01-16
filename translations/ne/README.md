<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:08:31+00:00",
  "source_file": "README.md",
  "language_code": "ne"
}
-->
# शूरुआतीहरूका लागि जनरेटिव एआई - जाभा संस्करण
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![शूरुआतीहरूका लागि जनरेटिव एआई - जाभा संस्करण](../../translated_images/ne/beg-genai-series.8b48be9951cc574c.webp)

**समय प्रतिबद्धता**: सम्पूर्ण कार्यशाला अनलाइन कुनै स्थानीय सेटअप बिना पूरा गर्न सकिन्छ। वातावरण सेटअप गर्न २ मिनेट लाग्छ, नमूनाहरू अन्वेषण गर्न १-३ घण्टा लाग्छ खोजी गहिराइ अनुसार।

> **छिटो सुरुवात**

1. यो रिपोजिटरी आफ्नो GitHub खातामा फोर्क गर्नुहोस्
2. क्लिक गर्नुहोस् **Code** → **Codespaces** ट्याब → **...** → **New with options...**
3. पूर्वनिर्धारितहरू प्रयोग गर्नुहोस् – यसले यस कोर्सको लागि सिर्जना गरिएको विकास कन्टेनर चयन गर्दछ
4. क्लिक गर्नुहोस् **Create codespace**
5. वातावरण तयार हुन करिब ~२ मिनेट पर्खनुहोस्
6. सिधै जानुहोस् [पहिलो उदाहरण](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **स्थानीय क्लोन गर्न रुचाउनुहुन्छ?**
>
> यो रिपोजिटरीमा ५०+ भाषा अनुवादहरू समावेश छन् जसले डाउनलोड साइज बढाउँछ। अनुवादहरू बिना क्लोन गर्न, स्पार्स चेकआउट प्रयोग गर्नुहोस्:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> यसले तपाईंलाई कोर्स पूरा गर्न आवश्यक सबै कुरा छिटो डाउनलोडसँग दिन्छ।

## बहु-भाषा समर्थन

### GitHub Action मार्फत समर्थित (स्वचालित र सधैं अद्यावधिक)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[अरेबिक](../ar/README.md) | [बङ्गाली](../bn/README.md) | [बुल्गेरियन](../bg/README.md) | [बर्मिज (म्यानमार)](../my/README.md) | [चिनी (सरलीकृत)](../zh/README.md) | [चिनी (पारम्परिक, होङकङ)](../hk/README.md) | [चिनी (पारम्परिक, मकाउ)](../mo/README.md) | [चिनी (पारम्परिक, ताइवान)](../tw/README.md) | [क्रोएशियन्](../hr/README.md) | [चेक](../cs/README.md) | [ड्यानीश](../da/README.md) | [डच](../nl/README.md) | [एस्टोनियन](../et/README.md) | [फिन्निश](../fi/README.md) | [फ्रेन्च](../fr/README.md) | [जर्मन](../de/README.md) | [ग्रीक](../el/README.md) | [हेब्रू](../he/README.md) | [हिन्दी](../hi/README.md) | [हंगेरियन](../hu/README.md) | [इन्डोनेसियन](../id/README.md) | [इटालियन](../it/README.md) | [जापानी](../ja/README.md) | [कन्नड](../kn/README.md) | [कोरियन](../ko/README.md) | [लिथुनियन](../lt/README.md) | [मलय](../ms/README.md) | [मलयालम](../ml/README.md) | [मराठी](../mr/README.md) | [नेपाली](./README.md) | [नाइजेरियन पिडगिन](../pcm/README.md) | [नर्वेजियन](../no/README.md) | [फारसी (पर्सियन)](../fa/README.md) | [पोलीश](../pl/README.md) | [पोर्चुगिज (ब्राजिल)](../br/README.md) | [पोर्चुगिज (पुर्तगाल)](../pt/README.md) | [पंजाबी (गुरमुखी)](../pa/README.md) | [रोमानियन](../ro/README.md) | [रशियन](../ru/README.md) | [सर्बियन् (सिरिलिक)](../sr/README.md) | [स्लोभाक](../sk/README.md) | [स्लोभेनियन्](../sl/README.md) | [स्पेनी](../es/README.md) | [स्वाहिली](../sw/README.md) | [स्विडिश](../sv/README.md) | [टागालग (फिलिपिनो)](../tl/README.md) | [तमिल](../ta/README.md) | [तेलुगु](../te/README.md) | [थाई](../th/README.md) | [टर्किश](../tr/README.md) | [युक्रेनीयन](../uk/README.md) | [उर्दु](../ur/README.md) | [भियतनामी](../vi/README.md)

> **स्थानीय क्लोन गर्न रुचाउनुहुन्छ?**

> यो रिपोजिटरीमा ५०+ भाषा अनुवादहरू समावेश छन् जसले डाउनलोड साइज बढाउँछ। अनुवादहरू बिना क्लोन गर्न, स्पार्स चेकआउट प्रयोग गर्नुहोस्:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> यसले तपाईंलाई कोर्स पूरा गर्न आवश्यक सबै कुरा छिटो डाउनलोडसँग दिन्छ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## कोर्स संरचना र सिकाइ मार्ग

### **अध्याय १: जनरेटिव एआई परिचय**
- **मुख्य अवधारणाहरू**: ठूलो भाषा मोडेलहरू, टोकनहरू, इम्बेडिङहरू, र एआई क्षमता बुझ्न
- **जाभा एआई इकोसिस्टम**: स्प्रिङ एआई र OpenAI SDKs को अवलोकन
- **मोडेल कन्टेक्स्ट प्रोटोकल**: MCP परिचय र यसको एआई एजेन्ट संचारमा भूमिका
- **व्यावहारिक प्रयोगहरू**: वास्तविक संसारका परिदृश्यहरू जस्तै च्याटबोट र सामग्री उत्पादन
- **[→ अध्याय १ सुरु गर्नुहोस्](./01-IntroToGenAI/README.md)**

### **अध्याय २: विकास वातावरण सेटअप**
- **बहु-प्रदाता कन्फिगरेसन**: GitHub मोडेलहरू, Azure OpenAI, र OpenAI जाभा SDK संयोजन सेटअप
- **स्प्रिङ बूट + स्प्रिङ एआई**: उद्यम एआई एप विकासका लागि राम्रो अभ्यासहरू
- **GitHub मोडेलहरू**: प्रोटोटाइप तथा सिकाइका लागि निःशुल्क AI मोडेल पहुँच (क्रेडिट कार्ड आवश्यक छैन)
- **विकास उपकरणहरू**: डोकर कन्टेनर, VS Code, र GitHub Codespaces कन्फिगरेसन
- **[→ अध्याय २ सुरु गर्नुहोस्](./02-SetupDevEnvironment/README.md)**

### **अध्याय ३: मुख्य जनरेटिव एआई प्रविधिहरू**
- **प्रॉम्प्ट इन्जिनियरिङ**: उत्तम AI मोडेल प्रतिक्रियाहरूका विधिहरू
- **इम्बेडिङ र भेक्टर अपरेसनहरू**: सिमेन्टिक खोज र समानता मिलान कार्यान्वयन
- **रिट्रिभल-अग्मेन्टेड जेनेरेसन (RAG)**: आफ्नो डाटासँग एआई जोड्ने
- **फङ्क्शन कलिङ**: कस्टम उपकरण र प्लगइनहरूसँग एआई क्षमता विस्तार गर्ने
- **[→ अध्याय ३ सुरु गर्नुहोस्](./03-CoreGenerativeAITechniques/README.md)**

### **अध्याय ४: व्यावहारिक प्रयोगहरू र प्रोजेक्टहरू**
- **पेट स्टोरी जेनेरेटर** (`petstory/`): GitHub मोडेलहरूसँग रचनात्मक सामग्री उत्पादन
- **फाउन्ड्री लोकल डेमो** (`foundrylocal/`): OpenAI जाभा SDK का साथ स्थानीय AI मोडेल संयोजन
- **MCP क्याल्कुलेटर सेवा** (`calculator/`): स्प्रिङ एआईसँग आधारभूत मोडेल कन्टेक्स्ट प्रोटोकल कार्यान्वयन
- **[→ अध्याय ४ सुरु गर्नुहोस्](./04-PracticalSamples/README.md)**

### **अध्याय ५: उत्तरदायी एआई विकास**
- **GitHub मोडेलहरू सुरक्षा**: निर्मित सामग्री फिल्टरिङ र सुरक्षा मेकानिजमहरू परीक्षण (कडा ब्लक र मन्द अस्वीकृति)
- **उत्तरदायी एआई डेमो**: आधुनिक एआई सुरक्षा प्रणाली कसरी काम गर्छ भन्ने व्यवहारिक उदाहरण
- **सर्वोत्तम अभ्यासहरू**: नैतिक एआई विकास र परिनियोजनका लागि आवश्यक निर्देशनहरू
- **[→ अध्याय ५ सुरु गर्नुहोस्](./05-ResponsibleGenAI/README.md)**

## अतिरिक्त स्रोतहरू

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![शूरुआतीहरूका लागि LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![शूरुआतीहरूका लागि LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![शूरुआतीहरूका लागि AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![शूरुआतीहरूका लागि Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![शूरुआतीहरूका लागि MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![शूरुआतीहरूका लागि AI एजेन्टहरू](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### जनरेटिव एआई सिरिज
[![शूरुआतीहरूका लागि जनरेटिव एआई](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव एआई (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव एआई (जाभा)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![जनरेटिव एआई (जाभास्क्रिप्ट)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### मुख्य सिकाइ
[![शूरुआतीहरूका लागि ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![शूरुआतीहरूका लागि डाटा साइन्स](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![शूरुआतीहरूका लागि एआई](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![शूरुआतीहरूका लागि साइबरसुरक्षा](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![शूरुआतीहरूका लागि वेब विकास](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot श्रृंखला
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## मद्दत पाउनुहोस्

यदि तपाईं अल्झनुहुन्छ वा AI अनुप्रयोगहरू निर्माण गर्दा कुनै पनि प्रश्नहरू छन् भने। MCP सम्बन्धी छलफलहरूमा सहभागी हुनुहोस् जहाँ अन्य सिक्नेहरु र अनुभवी विकासकर्ताहरू छन्। यो एक सहयोगी समुदाय हो जहाँ प्रश्नहरू स्वागतयोग्य छन् र ज्ञान स्वतन्त्र रूपमा साझा गरिन्छ।

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

यदि तपाईंलाई उत्पादन प्रतिक्रियाहरू वा त्रुटिहरू छन् भने निर्माण गर्दा यहाँ जानुहोस्:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:  
यो दस्तावेज एआई अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी सटीकता को लागी प्रयास गर्छौ, तर कृपया बुझ्नुहोस् कि स्वचालित अनुवादमा त्रुटि वा अशुद्धि हुन सक्छ। मूल दस्तावेज यसको मूल भाषामा आधिकारिक स्रोत मानिनु पर्छ। महत्त्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवाद प्रयोगबाट हुने कुनै पनि गलतफहमी वा गलत व्याख्याको जिम्मेवारी हामी लिँदैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->