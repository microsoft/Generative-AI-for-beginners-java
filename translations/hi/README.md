<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:05:05+00:00",
  "source_file": "README.md",
  "language_code": "hi"
}
-->
# शुरुआती लोगों के लिए जनरेटिव AI - जावा संस्करण
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![शुरुआती लोगों के लिए जनरेटिव AI - जावा संस्करण](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.hi.png)

**समय की आवश्यकता**: पूरा वर्कशॉप ऑनलाइन पूरा किया जा सकता है, बिना किसी लोकल सेटअप के। यदि आप सैंपल्स चलाना चाहते हैं, तो पर्यावरण सेटअप में 2 मिनट लगते हैं, और सैंपल्स को एक्सप्लोर करने में 1-3 घंटे लग सकते हैं, यह आपकी गहराई पर निर्भर करता है।

> **त्वरित शुरुआत**

1. इस रिपॉजिटरी को अपने GitHub अकाउंट पर फोर्क करें
2. **Code** → **Codespaces** टैब → **...** → **New with options...** पर क्लिक करें
3. डिफॉल्ट विकल्प चुनें – यह इस कोर्स के लिए बनाए गए डेवलपमेंट कंटेनर को चुनेगा
4. **Create codespace** पर क्लिक करें
5. पर्यावरण तैयार होने के लिए ~2 मिनट प्रतीक्षा करें
6. सीधे [अपना GitHub Models Token बनाना शुरू करें](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## बहुभाषी समर्थन

### GitHub Action के माध्यम से समर्थित (स्वचालित और हमेशा अपडेटेड)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](./README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## कोर्स संरचना और सीखने का मार्ग

### **अध्याय 1: जनरेटिव AI का परिचय**
- **मुख्य अवधारणाएँ**: बड़े भाषा मॉडल, टोकन, एम्बेडिंग, और AI क्षमताओं को समझना
- **जावा AI इकोसिस्टम**: Spring AI और OpenAI SDKs का अवलोकन
- **मॉडल कॉन्टेक्स्ट प्रोटोकॉल**: MCP और AI एजेंट संचार में इसकी भूमिका का परिचय
- **व्यावहारिक अनुप्रयोग**: वास्तविक दुनिया के उदाहरण जैसे चैटबॉट्स और सामग्री निर्माण
- **[→ अध्याय 1 शुरू करें](./01-IntroToGenAI/README.md)**

### **अध्याय 2: विकास पर्यावरण सेटअप**
- **मल्टी-प्रोवाइडर कॉन्फ़िगरेशन**: GitHub Models, Azure OpenAI, और OpenAI Java SDK इंटीग्रेशन सेटअप करें
- **Spring Boot + Spring AI**: एंटरप्राइज़ AI एप्लिकेशन विकास के लिए सर्वोत्तम प्रथाएँ
- **GitHub Models**: प्रोटोटाइप और सीखने के लिए मुफ्त AI मॉडल एक्सेस (क्रेडिट कार्ड की आवश्यकता नहीं)
- **विकास उपकरण**: Docker कंटेनर, VS Code, और GitHub Codespaces कॉन्फ़िगरेशन
- **[→ अध्याय 2 शुरू करें](./02-SetupDevEnvironment/README.md)**

### **अध्याय 3: मुख्य जनरेटिव AI तकनीकें**
- **प्रॉम्प्ट इंजीनियरिंग**: AI मॉडल से सर्वोत्तम प्रतिक्रियाएँ प्राप्त करने की तकनीकें
- **एम्बेडिंग और वेक्टर ऑपरेशन्स**: सिमेंटिक सर्च और समानता मिलान लागू करें
- **रिट्रीवल-ऑगमेंटेड जनरेशन (RAG)**: AI को अपने डेटा स्रोतों के साथ जोड़ें
- **फंक्शन कॉलिंग**: कस्टम टूल्स और प्लगइन्स के साथ AI क्षमताओं को बढ़ाएं
- **[→ अध्याय 3 शुरू करें](./03-CoreGenerativeAITechniques/README.md)**

### **अध्याय 4: व्यावहारिक अनुप्रयोग और प्रोजेक्ट्स**
- **पेट स्टोरी जनरेटर** (`petstory/`): GitHub Models के साथ रचनात्मक सामग्री निर्माण
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK के साथ लोकल AI मॉडल इंटीग्रेशन
- **MCP कैलकुलेटर सर्विस** (`mcp/calculator/`): Spring AI के साथ बेसिक मॉडल कॉन्टेक्स्ट प्रोटोकॉल का कार्यान्वयन
- **[→ अध्याय 4 शुरू करें](./04-PracticalSamples/README.md)**

### **अध्याय 5: जिम्मेदार AI विकास**
- **GitHub Models सुरक्षा**: अंतर्निहित सामग्री फ़िल्टरिंग और सुरक्षा तंत्र का परीक्षण करें
- **जिम्मेदार AI डेमो**: यह दिखाने के लिए व्यावहारिक उदाहरण कि AI सुरक्षा फ़िल्टर कैसे काम करते हैं
- **सर्वोत्तम प्रथाएँ**: नैतिक AI विकास और तैनाती के लिए आवश्यक दिशानिर्देश
- **[→ अध्याय 5 शुरू करें](./05-ResponsibleGenAI/README.md)**

## अतिरिक्त संसाधन 

- [शुरुआती लोगों के लिए AI एजेंट्स](https://github.com/microsoft/ai-agents-for-beginners)
- [शुरुआती लोगों के लिए जनरेटिव AI (.NET का उपयोग करके)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [शुरुआती लोगों के लिए जनरेटिव AI (JavaScript का उपयोग करके)](https://github.com/microsoft/generative-ai-with-javascript)
- [शुरुआती लोगों के लिए जनरेटिव AI](https://github.com/microsoft/generative-ai-for-beginners)
- [शुरुआती लोगों के लिए मशीन लर्निंग](https://aka.ms/ml-beginners)
- [शुरुआती लोगों के लिए डेटा साइंस](https://aka.ms/datascience-beginners)
- [शुरुआती लोगों के लिए AI](https://aka.ms/ai-beginners)
- [शुरुआती लोगों के लिए साइबर सुरक्षा](https://github.com/microsoft/Security-101)
- [शुरुआती लोगों के लिए वेब विकास](https://aka.ms/webdev-beginners)
- [शुरुआती लोगों के लिए IoT](https://aka.ms/iot-beginners)
- [शुरुआती लोगों के लिए XR विकास](https://github.com/microsoft/xr-development-for-beginners)
- [AI पेयर प्रोग्रामिंग के लिए GitHub Copilot में महारत हासिल करें](https://aka.ms/GitHubCopilotAI)
- [C#/.NET डेवलपर्स के लिए GitHub Copilot में महारत हासिल करें](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [अपना खुद का Copilot एडवेंचर चुनें](https://github.com/microsoft/CopilotAdventures)
- [Azure AI सेवाओं के साथ RAG चैट ऐप](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**अस्वीकरण**:  
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता सुनिश्चित करने का प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल भाषा में उपलब्ध मूल दस्तावेज़ को प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम जिम्मेदार नहीं हैं।