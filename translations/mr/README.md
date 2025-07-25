<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:10:19+00:00",
  "source_file": "README.md",
  "language_code": "mr"
}
-->
# जनरेटिव AI सुरुवातीसाठी - जावा एडिशन
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![जनरेटिव AI सुरुवातीसाठी - जावा एडिशन](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.mr.png)

**वेळेची गरज**: संपूर्ण कार्यशाळा स्थानिक सेटअपशिवाय ऑनलाइन पूर्ण केली जाऊ शकते. नमुने चालवायचे असल्यास, वातावरण सेटअपसाठी 2 मिनिटे लागतात, आणि नमुने एक्सप्लोर करण्यासाठी 1-3 तास लागतात, एक्सप्लोरेशनच्या खोलीवर अवलंबून.

> **त्वरित सुरुवात**

1. या रिपॉझिटरीला तुमच्या GitHub खात्यावर फोर्क करा
2. **Code** → **Codespaces** टॅब → **...** → **New with options...** वर क्लिक करा
3. डीफॉल्ट पर्याय वापरा – हे या कोर्ससाठी तयार केलेल्या डेव्हलपमेंट कंटेनरची निवड करेल
4. **Create codespace** वर क्लिक करा
5. वातावरण तयार होण्यासाठी ~2 मिनिटे प्रतीक्षा करा
6. [तुमचा GitHub Models Token तयार करणे](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) येथे थेट जा

## बहुभाषिक समर्थन

### GitHub Action द्वारे समर्थित (स्वयंचलित आणि नेहमी अद्ययावत)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](./README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## कोर्सची रचना आणि शिकण्याचा मार्ग

### **प्रकरण 1: जनरेटिव AI ची ओळख**
- **मूलभूत संकल्पना**: मोठ्या भाषा मॉडेल्स, टोकन्स, एम्बेडिंग्स, आणि AI क्षमता समजून घेणे
- **जावा AI इकोसिस्टम**: Spring AI आणि OpenAI SDKs चा आढावा
- **मॉडेल कॉन्टेक्स्ट प्रोटोकॉल**: MCP आणि AI एजंट कम्युनिकेशनमधील त्याची भूमिका
- **व्यावहारिक उपयोग**: चॅटबॉट्स आणि कंटेंट जनरेशनसारख्या वास्तविक जगातील उदाहरणे
- **[→ प्रकरण 1 सुरू करा](./01-IntroToGenAI/README.md)**

### **प्रकरण 2: विकास वातावरण सेटअप**
- **मल्टी-प्रोव्हायडर कॉन्फिगरेशन**: GitHub Models, Azure OpenAI, आणि OpenAI Java SDK इंटिग्रेशन सेटअप करा
- **Spring Boot + Spring AI**: एंटरप्राइझ AI अॅप्लिकेशन डेव्हलपमेंटसाठी सर्वोत्तम पद्धती
- **GitHub Models**: प्रोटोटायपिंग आणि शिकण्यासाठी मोफत AI मॉडेल प्रवेश (क्रेडिट कार्ड आवश्यक नाही)
- **विकास साधने**: Docker कंटेनर्स, VS Code, आणि GitHub Codespaces कॉन्फिगरेशन
- **[→ प्रकरण 2 सुरू करा](./02-SetupDevEnvironment/README.md)**

### **प्रकरण 3: जनरेटिव AI तंत्रज्ञानाचे मुख्य तत्त्व**
- **प्रॉम्प्ट इंजिनिअरिंग**: AI मॉडेल्सकडून उत्तम प्रतिसाद मिळवण्यासाठी तंत्र
- **एम्बेडिंग्स आणि व्हेक्टर ऑपरेशन्स**: सिमॅंटिक सर्च आणि साम्य जुळविणे अंमलात आणा
- **रिट्रीव्हल-अगमेंटेड जनरेशन (RAG)**: AI ला तुमच्या स्वतःच्या डेटा स्रोतांसह एकत्र करा
- **फंक्शन कॉलिंग**: कस्टम टूल्स आणि प्लगइन्ससह AI क्षमता वाढवा
- **[→ प्रकरण 3 सुरू करा](./03-CoreGenerativeAITechniques/README.md)**

### **प्रकरण 4: व्यावहारिक उपयोग आणि प्रकल्प**
- **पेट स्टोरी जनरेटर** (`petstory/`): GitHub Models सह सर्जनशील कंटेंट जनरेशन
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK सह स्थानिक AI मॉडेल इंटिग्रेशन
- **MCP कॅल्क्युलेटर सर्व्हिस** (`mcp/calculator/`): Spring AI सह मूलभूत मॉडेल कॉन्टेक्स्ट प्रोटोकॉल अंमलबजावणी
- **[→ प्रकरण 4 सुरू करा](./04-PracticalSamples/README.md)**

### **प्रकरण 5: जबाबदार AI विकास**
- **GitHub Models सुरक्षा**: अंगभूत कंटेंट फिल्टरिंग आणि सुरक्षा यंत्रणा तपासा
- **जबाबदार AI डेमो**: AI सुरक्षा फिल्टर्स प्रत्यक्षात कसे कार्य करतात हे दर्शवणारे हँड्स-ऑन उदाहरण
- **सर्वोत्तम पद्धती**: नैतिक AI विकास आणि तैनातीसाठी आवश्यक मार्गदर्शक तत्त्वे
- **[→ प्रकरण 5 सुरू करा](./05-ResponsibleGenAI/README.md)**

## अतिरिक्त संसाधने 

- [AI Agents For Beginners](https://github.com/microsoft/ai-agents-for-beginners)
- [Generative AI for Beginners using .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generative AI for Beginners using JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generative AI for Beginners](https://github.com/microsoft/generative-ai-for-beginners)
- [ML for Beginners](https://aka.ms/ml-beginners)
- [Data Science for Beginners](https://aka.ms/datascience-beginners)
- [AI for Beginners](https://aka.ms/ai-beginners)
- [Cybersecurity for Beginners](https://github.com/microsoft/Security-101)
- [Web Dev for Beginners](https://aka.ms/webdev-beginners)
- [IoT for Beginners](https://aka.ms/iot-beginners)
- [XR Development for Beginners](https://github.com/microsoft/xr-development-for-beginners)
- [Mastering GitHub Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI)
- [Mastering GitHub Copilot for C#/.NET Developers](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Choose Your Own Copilot Adventure](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App with Azure AI Services](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**अस्वीकरण**:  
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरे त्रुटी किंवा अचूकतेच्या अभावाने युक्त असू शकतात. मूळ भाषेतील दस्तऐवज हा अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराचा वापर करून उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थासाठी आम्ही जबाबदार राहणार नाही.