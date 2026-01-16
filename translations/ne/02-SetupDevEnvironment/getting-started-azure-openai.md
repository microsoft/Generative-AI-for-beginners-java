<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-07-27T13:03:06+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "ne"
}
-->
# Azure OpenAI को विकास वातावरण सेटअप गर्ने

> **छिटो सुरु**: यो मार्गदर्शन Azure OpenAI सेटअपको लागि हो। निःशुल्क मोडेलहरू प्रयोग गरेर तुरुन्त सुरु गर्न [GitHub Models with Codespaces](./README.md#quick-start-cloud) प्रयोग गर्नुहोस्।

यो मार्गदर्शनले तपाईंलाई यस पाठ्यक्रममा Java AI एप्सको लागि Azure AI Foundry मोडेल सेटअप गर्न मद्दत गर्नेछ।

## सामग्री सूची

- [छिटो सेटअपको अवलोकन](../../../02-SetupDevEnvironment)
- [चरण १: Azure AI Foundry स्रोतहरू सिर्जना गर्नुहोस्](../../../02-SetupDevEnvironment)
  - [हब र प्रोजेक्ट सिर्जना गर्नुहोस्](../../../02-SetupDevEnvironment)
  - [GPT-4o-mini मोडेल डिप्लोय गर्नुहोस्](../../../02-SetupDevEnvironment)
- [चरण २: आफ्नो Codespace सिर्जना गर्नुहोस्](../../../02-SetupDevEnvironment)
- [चरण ३: आफ्नो वातावरण कन्फिगर गर्नुहोस्](../../../02-SetupDevEnvironment)
- [चरण ४: आफ्नो सेटअप परीक्षण गर्नुहोस्](../../../02-SetupDevEnvironment)
- [अगाडि के गर्ने?](../../../02-SetupDevEnvironment)
- [स्रोतहरू](../../../02-SetupDevEnvironment)
- [थप स्रोतहरू](../../../02-SetupDevEnvironment)

## छिटो सेटअपको अवलोकन

1. Azure AI Foundry स्रोतहरू सिर्जना गर्नुहोस् (हब, प्रोजेक्ट, मोडेल)
2. Java विकास कन्टेनरको साथ Codespace सिर्जना गर्नुहोस्
3. Azure OpenAI प्रमाणहरू सहित आफ्नो `.env` फाइल कन्फिगर गर्नुहोस्
4. उदाहरण प्रोजेक्टको साथ आफ्नो सेटअप परीक्षण गर्नुहोस्

## चरण १: Azure AI Foundry स्रोतहरू सिर्जना गर्नुहोस्

### हब र प्रोजेक्ट सिर्जना गर्नुहोस्

1. [Azure AI Foundry Portal](https://ai.azure.com/) मा जानुहोस् र साइन इन गर्नुहोस्
2. **+ Create** → **New hub** क्लिक गर्नुहोस् (वा **Management** → **All hubs** → **+ New hub** मा जानुहोस्)
3. आफ्नो हब कन्फिगर गर्नुहोस्:
   - **हब नाम**: जस्तै, "MyAIHub"
   - **सदस्यता**: आफ्नो Azure सदस्यता चयन गर्नुहोस्
   - **स्रोत समूह**: नयाँ सिर्जना गर्नुहोस् वा पहिलेको चयन गर्नुहोस्
   - **स्थान**: आफ्नो नजिकको स्थान चयन गर्नुहोस्
   - **स्टोरेज खाता**: डिफल्ट प्रयोग गर्नुहोस् वा कस्टम कन्फिगर गर्नुहोस्
   - **Key vault**: डिफल्ट प्रयोग गर्नुहोस् वा कस्टम कन्फिगर गर्नुहोस्
   - **Next** → **Review + create** → **Create** क्लिक गर्नुहोस्
4. हब सिर्जना भएपछि, **+ New project** क्लिक गर्नुहोस् (वा हबको अवलोकनबाट **Create project**)
   - **प्रोजेक्ट नाम**: जस्तै, "GenAIJava"
   - **Create** क्लिक गर्नुहोस्

### GPT-4o-mini मोडेल डिप्लोय गर्नुहोस्

1. आफ्नो प्रोजेक्टमा, **Model catalog** मा जानुहोस् र **gpt-4o-mini** खोज्नुहोस्
   - *वैकल्पिक: **Deployments** → **+ Create deployment** मा जानुहोस्*
2. gpt-4o-mini मोडेल कार्डमा **Deploy** क्लिक गर्नुहोस्
3. डिप्लोयमेन्ट कन्फिगर गर्नुहोस्:
   - **डिप्लोयमेन्ट नाम**: "gpt-4o-mini"
   - **मोडेल संस्करण**: पछिल्लो संस्करण प्रयोग गर्नुहोस्
   - **डिप्लोयमेन्ट प्रकार**: Standard
4. **Deploy** क्लिक गर्नुहोस्
5. डिप्लोय भएपछि, **Deployments** ट्याबमा जानुहोस् र यी मानहरू प्रतिलिपि गर्नुहोस्:
   - **डिप्लोयमेन्ट नाम** (जस्तै, "gpt-4o-mini")
   - **Target URI** (जस्तै, `https://your-hub-name.openai.azure.com/`) 
      > **महत्वपूर्ण**: केवल आधार URL प्रतिलिपि गर्नुहोस् (जस्तै, `https://myhub.openai.azure.com/`) पूर्ण अन्त बिन्दु पथ होइन।
   - **Key** (Keys and Endpoint सेक्सनबाट)

> **अझै समस्या छ?** आधिकारिक [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project) हेर्नुहोस्।

## चरण २: आफ्नो Codespace सिर्जना गर्नुहोस्

1. यो रिपोजिटरीलाई आफ्नो GitHub खातामा Fork गर्नुहोस्
   > **नोट**: यदि तपाईं आधारभूत कन्फिग सम्पादन गर्न चाहनुहुन्छ भने [Dev Container Configuration](../../../.devcontainer/devcontainer.json) हेर्नुहोस्।
2. आफ्नो Fork गरिएको रिपोजिटरीमा, **Code** → **Codespaces** ट्याबमा जानुहोस्
3. **...** → **New with options...** क्लिक गर्नुहोस्
![Codespace विकल्पहरू सिर्जना गर्दै](../../../translated_images/ne/codespaces.9945ded8ceb431a5.webp)
4. **Dev container configuration** चयन गर्नुहोस्: 
   - **Generative AI Java Development Environment**
5. **Create codespace** क्लिक गर्नुहोस्

## चरण ३: आफ्नो वातावरण कन्फिगर गर्नुहोस्

तपाईंको Codespace तयार भएपछि, Azure OpenAI प्रमाणहरू सेटअप गर्नुहोस्:

1. **रिपोजिटरीको मूलबाट उदाहरण प्रोजेक्टमा जानुहोस्:**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **आफ्नो `.env` फाइल सिर्जना गर्नुहोस्:**
   ```bash
   cp .env.example .env
   ```

3. **Azure OpenAI प्रमाणहरू सहित `.env` फाइल सम्पादन गर्नुहोस्:**
   ```bash
   # Your Azure OpenAI API key (from Azure AI Foundry portal)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # Your Azure OpenAI endpoint URL (e.g., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **सुरक्षा नोट**: 
   > - कहिल्यै आफ्नो `.env` फाइललाई संस्करण नियन्त्रणमा कमिट नगर्नुहोस्
   > - `.env` फाइल पहिले नै `.gitignore` मा समावेश गरिएको छ
   > - आफ्नो API कुञ्जीहरू सुरक्षित राख्नुहोस् र नियमित रूपमा परिवर्तन गर्नुहोस्

## चरण ४: आफ्नो सेटअप परीक्षण गर्नुहोस्

Azure OpenAI कनेक्शन परीक्षण गर्न उदाहरण एप्लिकेशन चलाउनुहोस्:

```bash
mvn clean spring-boot:run
```

तपाईंले GPT-4o-mini मोडेलबाट प्रतिक्रिया देख्नु पर्नेछ!

> **VS Code प्रयोगकर्ता**: तपाईंले `F5` थिचेर एप्लिकेशन चलाउन सक्नुहुन्छ। `.env` फाइललाई स्वचालित रूपमा लोड गर्न लन्च कन्फिगरेसन पहिले नै सेट गरिएको छ।

> **पूर्ण उदाहरण**: विस्तृत निर्देशन र समस्या समाधानको लागि [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) हेर्नुहोस्।

## अगाडि के गर्ने?

**सेटअप पूरा भयो!** तपाईंले अब:
- gpt-4o-mini सहित Azure OpenAI डिप्लोय गर्नुभयो
- स्थानीय `.env` फाइल कन्फिगरेसन
- Java विकास वातावरण तयार

**अगाडि बढ्नुहोस्** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) मा AI एप्लिकेशन निर्माण सुरु गर्न!

## स्रोतहरू

- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI Documentation](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## थप स्रोतहरू

- [VS Code डाउनलोड गर्नुहोस्](https://code.visualstudio.com/Download)
- [Docker Desktop प्राप्त गर्नुहोस्](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

**अस्वीकरण**:  
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरी अनुवाद गरिएको हो। हामी यथासम्भव शुद्धताको प्रयास गर्छौं, तर कृपया ध्यान दिनुहोस् कि स्वचालित अनुवादमा त्रुटि वा अशुद्धता हुन सक्छ। मूल भाषामा रहेको मूल दस्तावेज़लाई आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि, व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न हुने कुनै पनि गलतफहमी वा गलत व्याख्याका लागि हामी जिम्मेवार हुने छैनौं।