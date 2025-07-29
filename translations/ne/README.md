<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T15:01:50+00:00",
  "source_file": "README.md",
  "language_code": "ne"
}
-->
# सुरुवातीका लागि जेनेरेटिभ एआई - जाभा संस्करण
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![सुरुवातीका लागि जेनेरेटिभ एआई - जाभा संस्करण](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ne.png)

**समय प्रतिबद्धता**: यो सम्पूर्ण कार्यशाला स्थानीय सेटअप बिना नै अनलाइन पूरा गर्न सकिन्छ। वातावरण सेटअप गर्न २ मिनेट लाग्छ, र नमूनाहरू अन्वेषण गर्न १-३ घण्टा लाग्न सक्छ, अन्वेषणको गहिराइमा निर्भर गर्दै।

> **छिटो सुरु गर्नुहोस्**

1. यो रिपोजिटरी आफ्नो GitHub खातामा फोर्क गर्नुहोस्  
2. **Code** → **Codespaces** ट्याब → **...** → **New with options...** मा क्लिक गर्नुहोस्  
3. डिफल्ट सेटिङहरू प्रयोग गर्नुहोस् – यसले यस कोर्सका लागि बनाइएको Development container चयन गर्नेछ  
4. **Create codespace** मा क्लिक गर्नुहोस्  
5. वातावरण तयार हुन ~२ मिनेट कुर्नुहोस्  
6. [पहिलो उदाहरण](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) मा सिधै जानुहोस्  

## बहुभाषिक समर्थन

### GitHub Action मार्फत समर्थित (स्वचालित र सधैं अद्यावधिक)

[फ्रान्सेली](../fr/README.md) | [स्पेनिश](../es/README.md) | [जर्मन](../de/README.md) | [रुसी](../ru/README.md) | [अरबी](../ar/README.md) | [फारसी (पर्सियन)](../fa/README.md) | [उर्दू](../ur/README.md) | [चिनियाँ (सरलीकृत)](../zh/README.md) | [चिनियाँ (परम्परागत, मकाउ)](../mo/README.md) | [चिनियाँ (परम्परागत, हङकङ)](../hk/README.md) | [चिनियाँ (परम्परागत, ताइवान)](../tw/README.md) | [जापानी](../ja/README.md) | [कोरियन](../ko/README.md) | [हिन्दी](../hi/README.md) | [बंगाली](../bn/README.md) | [मराठी](../mr/README.md) | [नेपाली](./README.md) | [पञ्जाबी (गुरमुखी)](../pa/README.md) | [पोर्चुगिज (पोर्चुगल)](../pt/README.md) | [पोर्चुगिज (ब्राजिल)](../br/README.md) | [इटालियन](../it/README.md) | [पोलिश](../pl/README.md) | [टर्किश](../tr/README.md) | [ग्रीक](../el/README.md) | [थाई](../th/README.md) | [स्विडिश](../sv/README.md) | [डेनिश](../da/README.md) | [नर्वेजियन](../no/README.md) | [फिनिश](../fi/README.md) | [डच](../nl/README.md) | [हिब्रू](../he/README.md) | [भियतनामी](../vi/README.md) | [इन्डोनेसियन](../id/README.md) | [मलय](../ms/README.md) | [टागालोग (फिलिपिनो)](../tl/README.md) | [स्वाहिली](../sw/README.md) | [हंगेरीयन](../hu/README.md) | [चेक](../cs/README.md) | [स्लोभाक](../sk/README.md) | [रोमानियन](../ro/README.md) | [बुल्गेरियन](../bg/README.md) | [सर्बियन (सिरिलिक)](../sr/README.md) | [क्रोएसियन](../hr/README.md) | [स्लोभेनियन](../sl/README.md) | [युक्रेनी](../uk/README.md) | [बर्मेली (म्यानमार)](../my/README.md)

## कोर्स संरचना र सिकाइ मार्ग

### **अध्याय १: जेनेरेटिभ एआईको परिचय**
- **मुख्य अवधारणाहरू**: ठूला भाषा मोडेलहरू, टोकनहरू, एम्बेडिङहरू, र एआई क्षमताहरू बुझ्ने  
- **जाभा एआई इकोसिस्टम**: Spring AI र OpenAI SDKs को अवलोकन  
- **मोडेल कन्टेक्स्ट प्रोटोकल**: MCP र यसको एआई एजेन्ट संचारमा भूमिका  
- **व्यावहारिक अनुप्रयोगहरू**: च्याटबोटहरू र सामग्री निर्माण जस्ता वास्तविक संसारका परिदृश्यहरू  
- **[→ अध्याय १ सुरु गर्नुहोस्](./01-IntroToGenAI/README.md)**  

### **अध्याय २: विकास वातावरण सेटअप**
- **बहु-प्रदायक कन्फिगरेसन**: GitHub Models, Azure OpenAI, र OpenAI Java SDK एकीकरण सेटअप गर्नुहोस्  
- **Spring Boot + Spring AI**: उद्यम एआई अनुप्रयोग विकासका लागि उत्कृष्ट अभ्यासहरू  
- **GitHub Models**: प्रोटोटाइप र सिकाइका लागि निःशुल्क एआई मोडेल पहुँच (क्रेडिट कार्ड आवश्यक छैन)  
- **विकास उपकरणहरू**: Docker कन्टेनरहरू, VS Code, र GitHub Codespaces कन्फिगरेसन  
- **[→ अध्याय २ सुरु गर्नुहोस्](./02-SetupDevEnvironment/README.md)**  

### **अध्याय ३: मुख्य जेनेरेटिभ एआई प्रविधिहरू**
- **प्रम्प्ट इन्जिनियरिङ**: एआई मोडेलको उत्तम प्रतिक्रिया प्राप्त गर्नका लागि प्रविधिहरू  
- **एम्बेडिङ र भेक्टर अपरेसनहरू**: सेम्यान्टिक खोज र समानता मिलान कार्यान्वयन गर्नुहोस्  
- **Retrieval-Augmented Generation (RAG)**: एआईलाई आफ्नै डाटा स्रोतहरूसँग संयोजन गर्नुहोस्  
- **फङ्सन कलिङ**: कस्टम उपकरण र प्लगइनहरूसँग एआई क्षमताहरू विस्तार गर्नुहोस्  
- **[→ अध्याय ३ सुरु गर्नुहोस्](./03-CoreGenerativeAITechniques/README.md)**  

### **अध्याय ४: व्यावहारिक अनुप्रयोगहरू र परियोजनाहरू**
- **पेट स्टोरी जेनेरेटर** (`petstory/`): GitHub Models को साथ रचनात्मक सामग्री निर्माण  
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK को साथ स्थानीय एआई मोडेल एकीकरण  
- **MCP क्याल्कुलेटर सेवा** (`calculator/`): Spring AI को साथ आधारभूत मोडेल कन्टेक्स्ट प्रोटोकल कार्यान्वयन  
- **[→ अध्याय ४ सुरु गर्नुहोस्](./04-PracticalSamples/README.md)**  

### **अध्याय ५: जिम्मेवार एआई विकास**
- **GitHub Models सुरक्षा**: सामग्री फिल्टरिङ र सुरक्षा संयन्त्रहरूको परीक्षण गर्नुहोस् (कडा ब्लकहरू र नरम अस्वीकारहरू)  
- **जिम्मेवार एआई डेमो**: आधुनिक एआई सुरक्षा प्रणालीहरू कसरी काम गर्छन् भन्ने व्यावहारिक उदाहरण  
- **उत्तम अभ्यासहरू**: नैतिक एआई विकास र परिनियोजनका लागि आवश्यक दिशानिर्देशहरू  
- **[→ अध्याय ५ सुरु गर्नुहोस्](./05-ResponsibleGenAI/README.md)**  

## थप स्रोतहरू 

- [सुरुवातीका लागि एआई एजेन्टहरू](https://github.com/microsoft/ai-agents-for-beginners)  
- [सुरुवातीका लागि जेनेरेटिभ एआई (.NET प्रयोग गरेर)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)  
- [सुरुवातीका लागि जेनेरेटिभ एआई (JavaScript प्रयोग गरेर)](https://github.com/microsoft/generative-ai-with-javascript)  
- [सुरुवातीका लागि जेनेरेटिभ एआई](https://github.com/microsoft/generative-ai-for-beginners)  
- [सुरुवातीका लागि मेसिन लर्निङ](https://aka.ms/ml-beginners)  
- [सुरुवातीका लागि डाटा विज्ञान](https://aka.ms/datascience-beginners)  
- [सुरुवातीका लागि एआई](https://aka.ms/ai-beginners)  
- [सुरुवातीका लागि साइबरसुरक्षा](https://github.com/microsoft/Security-101)  
- [सुरुवातीका लागि वेब विकास](https://aka.ms/webdev-beginners)  
- [सुरुवातीका लागि IoT](https://aka.ms/iot-beginners)  
- [सुरुवातीका लागि XR विकास](https://github.com/microsoft/xr-development-for-beginners)  
- [GitHub Copilot को मास्टरी (एआई जोडी प्रोग्रामिङका लागि)](https://aka.ms/GitHubCopilotAI)  
- [C#/.NET विकासकर्ताहरूका लागि GitHub Copilot को मास्टरी](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)  
- [आफ्नै Copilot साहसिक यात्रा चयन गर्नुहोस्](https://github.com/microsoft/CopilotAdventures)  
- [Azure AI सेवाहरूको साथ RAG च्याट एप](https://github.com/Azure-Samples/azure-search-openai-demo-java)  

**अस्वीकरण**:  
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको छ। हामी यथार्थताको लागि प्रयास गर्छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादहरूमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छ। यसको मूल भाषा मा रहेको मूल दस्तावेज़लाई आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीको लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याको लागि हामी जिम्मेवार हुने छैनौं।