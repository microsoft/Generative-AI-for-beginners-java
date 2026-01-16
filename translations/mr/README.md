<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:06:52+00:00",
  "source_file": "README.md",
  "language_code": "mr"
}
-->
# नवीनांसाठी जनरेटिव्ह AI - जावा संस्करण
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/mr/beg-genai-series.8b48be9951cc574c.webp)

**वेळेची बांधणी**: संपूर्ण कार्यशाळा ऑनलाईन पूर्ण करता येते, स्थानिक सेटअपशिवाय. पर्यावरण सेटअपसाठी 2 मिनिटे लागतात, आणि नमुन्यांचा अभ्यास करण्यासाठी 1-3 तासांचा वेगळेपणा आहे जो अभ्यासाच्या खोलीवर अवलंबून आहे.

> **लवकर सुरूवात** 

1. हा रेपॉझिटरी आपल्या GitHub खात्यावर Fork करा
2. क्लिक करा **Code** → **Codespaces** टॅब → **...** → **New with options...**
3. डीफॉल्ट्स वापरा – यामुळे या कोर्ससाठी तयार केलेल्या Development कंटेनरची निवड होईल
4. क्लिक करा **Create codespace**
5. सुमारे 2 मिनिटे प्रतीक्षा करा पर्यावरण तयार होईपर्यंत
6. थेट [पहिला उदाहरण](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) यावर जा

> **स्थानिकरित्या Clone करायचे प्राधान्य द्यायचे?**
>
> या रेपॉझिटरीमध्ये 50+ भाषेतले भाषांतर आहे ज्यामुळे डाउनलोड साईज लक्षणीय वाढते. भाषांतरांशिवाय clone करण्यासाठी sparse checkout वापरा:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> यामुळे आपण कोर्स पूर्ण करण्यासाठी आवश्यक सर्वकाही खूप जलद डाउनलोड मिळेल.


## बहुभाषिक समर्थन

### GitHub Action द्वारे समर्थित (स्वयंचलित आणि सदैव अद्ययावत)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[अरबी](../ar/README.md) | [बंगाली](../bn/README.md) | [बल्गेरियन](../bg/README.md) | [म्यानमार (बर्मीज)](../my/README.md) | [चिनी (सोपे केलेले)](../zh/README.md) | [चिनी (परंपरागत, हॉंगकॉंग)](../hk/README.md) | [चिनी (परंपरागत, मकाऊ)](../mo/README.md) | [चिनी (परंपरागत, तैवान)](../tw/README.md) | [क्रोएशियन](../hr/README.md) | [झेक](../cs/README.md) | [डॅनिश](../da/README.md) | [डच](../nl/README.md) | [एस्टोनियन](../et/README.md) | [फिन्निश](../fi/README.md) | [फ्रेंच](../fr/README.md) | [जर्मन](../de/README.md) | [ग्रीक](../el/README.md) | [हिब्रू](../he/README.md) | [हिंदी](../hi/README.md) | [हंगेरियन](../hu/README.md) | [इंडोनेशियन](../id/README.md) | [इटालियन](../it/README.md) | [जपानी](../ja/README.md) | [कन्नड](../kn/README.md) | [कोंगली](../ko/README.md) | [लिथुआनियन](../lt/README.md) | [मलय](../ms/README.md) | [मलयाळम](../ml/README.md) | [मराठी](./README.md) | [नेपाली](../ne/README.md) | [नायजेरियन पिजिन](../pcm/README.md) | [नॉर्वेजियन](../no/README.md) | [फारशी (पर्शियन)](../fa/README.md) | [पोलिश](../pl/README.md) | [ब्राझीलियन पोर्तुगीज](../br/README.md) | [पोर्तुगीज (पोर्तुगाल)](../pt/README.md) | [पंजाबी (गुरुमुखी)](../pa/README.md) | [रोमानियन](../ro/README.md) | [रशियन](../ru/README.md) | [सर्बियन (सिरिलिक)](../sr/README.md) | [स्लोव्हाक](../sk/README.md) | [स्लोव्हेनियन](../sl/README.md) | [स्पॅनिश](../es/README.md) | [स्वाहिली](../sw/README.md) | [स्वीडिश](../sv/README.md) | [तागालॉग (फिलीपिनो)](../tl/README.md) | [तमिळ](../ta/README.md) | [तेलुगू](../te/README.md) | [थाई](../th/README.md) | [तुर्की](../tr/README.md) | [युक्रेनी](../uk/README.md) | [उर्दू](../ur/README.md) | [विएतनामी](../vi/README.md)

> **स्थानिकरित्या Clone करायचे प्राधान्य द्यायचे?**

> या रेपॉझिटरीमध्ये 50+ भाषांतर आहे ज्यामुळे डाउनलोड साईज लक्षणीय वाढते. भाषांतरांशिवाय clone करण्यासाठी sparse checkout वापरा:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> यामुळे आपण कोर्स पूर्ण करण्यासाठी आवश्यक सर्वकाही खूप जलद डाउनलोड मिळेल.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## कोर्स रचना आणि शिक्षणाचा मार्ग

### **अध्याय 1: जनरेटिव्ह AI ची ओळख**
- **मुळ संकल्पना**: Large Language Models, tokens, embeddings, आणि AI क्षमतांचा समज
- **जावा AI इकोसिस्टम**: Spring AI आणि OpenAI SDK चे विहंगावलोकन
- **मॉडेल संदर्भ प्रोटोकॉल**: MCP ची ओळख आणि AI एजंट संवादामध्ये त्याची भूमिका
- **व्यावहारिक अनुप्रयोग**: चैटबॉट्स आणि कंटेंट जनरेशन यांसारख्या वास्तविक जगातील परिस्थिती
- **[→ पहिला अध्याय सुरू करा](./01-IntroToGenAI/README.md)**

### **अध्याय 2: विकास पर्यावरण सेटअप**
- **मल्टि-प्रोव्हायडर कॉन्फिगरेशन**: GitHub Models, Azure OpenAI, आणि OpenAI Java SDK एकत्रीकरण सेट करा
- **Spring Boot + Spring AI**: एंटरप्राइझ AI अनुप्रयोग विकासासाठी सर्वोत्तम पद्धती
- **GitHub Models**: प्रोटोटायपिंग आणि शिक्षणासाठी मोफत AI मॉडेल वापर (क्रेडिट कार्ड आवश्यक नाही)
- **विकास साधने**: Docker कंटेनर, VS Code, आणि GitHub Codespaces सेटअप
- **[→ दुसरा अध्याय सुरू करा](./02-SetupDevEnvironment/README.md)**

### **अध्याय 3: मुख्य जनरेटिव्ह AI तंत्रे**
- **प्रॉम्प्ट इंजिनीअरिंग**: AI मॉडेल प्रतिसादासाठी उत्कृष्ट तंत्र
- **_embeddings_ आणि वेक्टर ऑपरेशन्स**: सेमॅंटिक सर्च आणि सादृश्यता जुळणी अंमलात आणा
- **रिट्रीव्हल-ऑगमेंटेड जनरेशन (RAG)**: AI ला आपल्या स्वतःच्या डेटा स्रोतांसह जोडा
- **फंक्शन कॉलिंग**: कस्टम टूल्स आणि प्लगइन्ससह AI क्षमतांचा विस्तार करा
- **[→ तिसरा अध्याय सुरू करा](./03-CoreGenerativeAITechniques/README.md)**

### **अध्याय 4: व्यावहारिक अनुप्रयोग आणि प्रकल्प**
- **पेट स्टोरी जनरेटर** (`petstory/`): GitHub Models वापरून सर्जनशील सामग्री निर्मिती
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK सह स्थानिक AI मॉडेल समाकलन
- **MCP कॅल्क्युलेटर सेवा** (`calculator/`): Spring AI सह मूलभूत Model Context Protocol अंमलबजावणी
- **[→ चौथा अध्याय सुरू करा](./04-PracticalSamples/README.md)**

### **अध्याय 5: जबाबदार AI विकास**
- **GitHub Models सुरक्षा**: अंगभूत कंटेंट फिल्टरिंग आणि संरक्षण व्यवस्था तपासा (हार्ड ब्लॉक्स आणि सॉफ्ट नाकार)
- **जबाबदार AI डेमो**: आधुनिक AI सुरक्षा व्यवस्थेवर प्रत्यक्ष उदाहरण
- **सर्वोत्तम पद्धती**: नैतिक AI विकास आणि अंमलबजावणीसाठी आवश्यक मार्गदर्शक तत्त्वे
- **[→ पाचवा अध्याय सुरू करा](./05-ResponsibleGenAI/README.md)**

## अतिरिक्त संसाधने

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
 
### जनरेटिव्ह AI मालिका
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### मुख्य शिक्षण
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![नवशिक्यांसाठी IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![नवशिक्यांसाठी XR विकास](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### कॉपायलट मालिका
[![AI जोडवलेले प्रोग्रामिंगसाठी कॉपायलट](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET साठी कॉपायलट](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![कॉपायलट साहस](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## मदत मिळवा

जर तुम्हाला अडचण येत असेल किंवा AI अॅप्स तयार करताना काही प्रश्न असतील. तर MCP विषयी चर्चेत सहभागी व्हा जिथे सहशिक्षार्थी आणि अनुभवी विकासक असतात. ही एक आधारभूत समुदाय आहे जिथे प्रश्न विचारले जातात आणि ज्ञान मोकळेपणाने शेअर केले जाते.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

तुमच्याकडे उत्पादन संबंधित अभिप्राय किंवा चुका असल्यास येथे भेट द्या:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तावेज AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरुन अनुवादित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी, कृपया लक्षात ठेवा की स्वयंचलित अनुवादांमध्ये चुका किंवा अचूकतेच्या त्रुटी असू शकतात. मूळ दस्तावेज त्याच्या मूळ भाषेत अधिकृत स्रोत म्हणून घ्यावा. महत्त्वाच्या माहिती साठी व्यावसायिक मानवी अनुवादाची शिफारस केली जाते. या अनुवादाच्या वापरामुळे उद्भवलेल्या कोणत्याही गैरसमजात किंवा चुकीच्या समजुतीस आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->