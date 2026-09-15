# Azure AI Foundry को विकास वातावरण सेट अप गर्दै

> यस निर्देशनले यस पाठ्यक्रमका Java AI अनुप्रयोगहरूका लागि **Azure AI Foundry** मोडेलहरू **keyless** प्रमाणीकरण (Microsoft Entra ID) प्रयोग गरेर सेट अप गर्छ — कुनै API कुञ्जीहरू व्यवस्थापन गर्न आवश्यक छैन। उपकरणप्रति नयाँ हुनुहुन्छ? [विकास वातावरण गाइड](./README.md) बाट सुरु गर्नुहोस्।

यस निर्देशनले यस पाठ्यक्रमका Java AI अनुप्रयोगहरूका लागि **Azure AI Foundry** मोडेलहरू सेट अप गर्छ। तपाईंका दुई विकल्पहरू छन्:

- **विकल्प A — `azd` + Bicep सँग प्रविधान गर्नुहोस् (सिफारिस गरिएको):** एकै कमाण्डले Foundry खाता र मोडेलहरू कोडको रूपमा तैनाथ गर्छ। कुनै पोर्टल क्लिक आवश्यक छैन।
- **विकल्प B — Azure AI Foundry पोर्टलमा स्रोतहरू म्यानुअली सिर्जना गर्नुहोस्।**

दुवै विकल्पहरू **keyless प्रमाणीकरण** (Microsoft Entra ID) प्रयोग गर्छन् — कुनै API कुञ्जीहरू नक्कल या लिक हुँदैनन्।

## सामग्री तालिका

- [के बनाइन्छ](#के-बनाइन्छ)
- [पूर्व आवश्यकताहरू](#पूर्व-आवश्यकताहरू)
- [विकल्प A: azd + Bicep सँग प्रविधान (सिफारिस)](#option-a-provision-with-azd--bicep-recommended)
- [विकल्प B: स्रोतहरू म्यानुअली सिर्जना गर्नुहोस्](#विकल्प-b-स्रोतहरू-म्यानुअली-सिर्जना-गर्नुहोस्)
- [आफ्नो वातावरण कन्फिगर गर्नुहोस्](#आफ्नो-वातावरण-कन्फिगर-गर्नुहोस्)
- [आफ्नो सेटअप परीक्षण गर्नुहोस्](#आफ्नो-सेटअप-परीक्षण-गर्नुहोस्)
- [अर्को के छ?](#अर्को-के-छ)
- [स्रोतहरू](#स्रोतहरू)
- [थप स्रोतहरू](#थप-स्रोतहरू)

## के बनाइन्छ

[`infra/`](../../../02-SetupDevEnvironment/infra) मा रहेका Bicep टेम्प्लेटहरूले निम्न लागू गर्छन्:

- एक **Azure AI Foundry** खाता (`Microsoft.CognitiveServices/accounts`, प्रकार `AIServices`) प्रोजेक्टसहित
- एक **च्याट** तैनाथी - GPT-5.6 Luna (`gpt-5.6-luna`), संस्करण `2026-07-09`, `GlobalStandard` क्षमता `10` (यस मोडेलका लागि प्रति मिनेट 10 अनुरोध र 10,000 टोकन)
- एक **इम्बेडिङ** तैनाथी - `text-embedding-3-small`, संस्करण `1` (पछिका अध्यायहरूमा प्रयोग हुने)
- एक **keyless भूमिका असाइनमेन्ट** (`Cognitive Services OpenAI User`) जसले `az login` द्वारा साइन इन गर्न मद्दत गर्छ, कुञ्जी व्यवस्थापन नगरी

## पूर्व आवश्यकताहरू

- एउटा [Azure सदस्यता](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) र [Maven 3.9+](https://maven.apache.org/download.cgi)

## विकल्प A: azd + Bicep सँग प्रविधान (सिफारिस गरिएको)

`02-SetupDevEnvironment` फोल्डरबाट:

```bash
cd 02-SetupDevEnvironment

# साइन इन गर्नुहोस् (दुवै उपकरणहरू)
azd auth login
az login

# फाउन्ड्री खाता + मोडेल परिनियोजनहरू व्यवस्था गर्नुहोस्
azd up
```

`azd` ले तपाईंलाई **वातावरण नाम** (जस्तै `genai-java`), **सदस्यता**, र **क्षेत्र** सोध्छ। आफ्नो सदस्यता र `gpt-5.6-luna` तथा `text-embedding-3-small` उपलब्ध क्षेत्र (जस्तै `eastus2`) चयन गर्नुहोस्। त्यहाँको मोडेल र तैनाथ प्रकारका लागि सदस्यताको उपयुक्त कोटा सुनिश्चित गर्नुहोस्; उपलब्धता र कोटा सदस्यताद्वारा फरक हुन सक्छ।

प्रविधान सम्पन्न भएपछि, azd ले:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) मा परिभाषित सबै तैनाथ गर्छ।
2. पोस्टप्रोभिजन हुक चलाउँछ जुन [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) लेख्छ, तपाईंको एन्डपोइन्ट र तैनाथी नामहरूसँग (कुनै गोप्य सूचना छैन)।

> **टिप:** परिवर्तनहरू लागू गर्न जहिले पनि `azd up` पुनः चलाउनुहोस्। सबै मेटाउन र लागत रोक्न `azd down` चलाउनुहोस्।

उत्पन्न सेटिङहरू हेर्न:

```bash
azd env get-values
```

अब [आफ्नो सेटअप परीक्षण गर्नुहोस्](#आफ्नो-सेटअप-परीक्षण-गर्नुहोस्) मा जानुहोस्।

## विकल्प B: स्रोतहरू म्यानुअली सिर्जना गर्नुहोस्

पोर्टल प्रयोग गर्न चाहनुहुन्छ? स्रोतहरू म्यानुअली सिर्जना गर्नुहोस्:

1. [Azure AI Foundry पोर्टल](https://ai.azure.com/) मा जानुहोस् र साइन इन गर्नुहोस्।
2. **प्रोजेक्ट सिर्जना गर्नुहोस्** (यसले AI Foundry स्रोत पनि सिर्जना गर्छ)। नाम जस्तै `GenAIJava` दिनुहोस्।
3. आफ्नो प्रोजेक्टमा, **Models + endpoints** → **Deploy model** → **Deploy base model** खोल्नुहोस्।
4. **GPT-5.6 Luna** तैनाथ गर्नुहोस् (मोडेल र तैनाथी नाम `gpt-5.6-luna`, संस्करण `2026-07-09`) सँग **Global Standard** क्षमता `10`। इम्बेडिङ उदाहरणका लागि **text-embedding-3-small**, संस्करण `1` पनि तय गर्नुहोस्।
5. **Overview** बाट आफ्नो **एन्डपोइन्ट** (जस्तै `https://<resource>.openai.azure.com/`) कपी गर्नुहोस्।
6. आफूलाई कुञ्जी बिना पहुँच दिनुहोस्: स्रोतमा, **Access control (IAM)** → **Add role assignment** → मा **Cognitive Services OpenAI User** भूमिका आफ्नो खातामा असाइन गर्नुहोस्।

> **अझै समस्या छ?** भने [Azure AI Foundry दस्तावेज](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) हेर्नुहोस्।

## आफ्नो वातावरण कन्फिगर गर्नुहोस्

**यदि तपाईंले विकल्प A (`azd up`) प्रयोग गर्नुभयो भने**, तपाईंको सेटिङ्स फाइल पहिल्यै लेखिएको छ — कन्फिगर गर्न आवश्यक छैन। [आफ्नो सेटअप परीक्षण गर्नुहोस्](#आफ्नो-सेटअप-परीक्षण-गर्नुहोस्) मा जानुहोस्।

**यदि विकल्प B (म्यानुअल) प्रयोग गर्नुभयो भने**, उदाहरणको `.env` फाइल आफैं सिर्जना गर्नुहोस्:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

आफ्नो एन्डपोइन्टसँग `.env` सम्पादन गर्नुहोस् (कुञ्जी छैन — प्रमाणीकरण keyless छ):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

स्रोतको Azure OpenAI एन्डपोइन्ट प्रयोग गर्नुहोस्, प्रोजेक्ट URL होइन। basic-chat एप्ले `/openai/v1` मा यसको रूपमा समाधान गर्छ र स्पष्ट बिरियर-टोकन क्लाइन्ट सेट गर्दछ; API कुञ्जी आवश्यक छैन।

> **सुरक्षा नोट:** कुनै API कुञ्जी भण्डारण छैन। तपाईं Microsoft Entra ID मार्फत `az login` (स्थानिय रूपमा) वा व्यवस्थापन गरिएको पहिचान (Azure मा) द्वारा प्रमाणित गर्नुहुन्छ। `.env` फाइलमा कुनै गोप्य सेटिङ छैन र `.gitignore` मार्फत समावेश गरिएको छ।

## आफ्नो सेटअप परीक्षण गर्नुहोस्

सुनिश्चित गर्नुहोस् कि तपाईं साइन इन हुनुहुन्छ ताकि keyless प्रमाणीकरण टोकन प्राप्त गर्न सकोस्, अनि उदाहरण चलाउनुहोस्:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # यदि तपाईं पहिले नै साइन इन गर्नु भएका छैन भने
mvn clean spring-boot:run
```

तपाईंले `gpt-5.6-luna` मोडेलबाट जवाफ पाउनु पर्नेछ। साना पूर्वनिर्धारित कोटाभित्र रहन उदाहरणहरू क्रमबद्ध रूपमा चलाउनुहोस्; HTTP 429 पाउनुभयो भने, पुन: प्रयास अन्तराल पर्खनुहोस् र फेरि प्रयास गर्नुहोस्।

> **VS Code प्रयोगकर्ताहरू:** चलाउन `F5` थिच्नुहोस्। एपले तपाईंको `.env` स्वचालित लोड गर्छ।

> **पूर्ण उदाहरण:** विवरण र समस्या समाधानका लागि [Azure AI Foundry सँग आधारभूत च्याट उदाहरण](./examples/basic-chat-azure/README.md) हेर्नुहोस्।

## अर्को के छ?

सफलतापूर्वक प्रविधान र उदाहरण चलाएपछि, तपाईंले प्राप्त गर्नुहुन्छ:
- `gpt-5.6-luna` र `text-embedding-3-small` सँग Azure AI Foundry तैनाथ गरिएको
- कुञ्जी रहित प्रमाणीकरण (Microsoft Entra ID) — व्यवस्थापन गर्न कुनै कुञ्जीहरू छैनन्
- तपाईंको endpoint र deployment नामहरूसँग स्थानीय `.env`
- तयार गर्न Java विकास वातावरण

**आगामी अध्याय 3: Core Generative AI Techniques** [मा जारी राख्नुहोस्](../03-CoreGenerativeAITechniques/README.md) AI अनुप्रयोगहरू बनाउन सुरु गर्न!

## स्रोतहरू

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID सँग कुञ्जी रहित प्रमाणीकरण](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry कागजात](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK परिवर्तन](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [अधिकृत OpenAI Java SDK Azure OpenAI v1 सँग](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## थप स्रोतहरू

- [VS Code डाउनलोड गर्नुहोस्](https://code.visualstudio.com/Download)
- [Docker Desktop प्राप्त गर्नुहोस्](https://www.docker.com/products/docker-desktop)
- [Dev Container कन्फिगरेसन](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी सही हुन प्रयास गर्छौं, तर कृपया जानकार हुनुस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छन्। मूल दस्तावेज़ यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा त्रुटिको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->