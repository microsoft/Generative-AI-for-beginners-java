# Azure AI Foundry के लिए विकास पर्यावरण सेट करना

> यह गाइड इस पाठ्यक्रम में जावा AI ऐप्स के लिए **Azure AI Foundry** मॉडल सेट करता है, **keyless** प्रमाणीकरण (Microsoft Entra ID) का उपयोग करते हुए — कोई API कुंजी प्रबंधित करने की आवश्यकता नहीं। टूलिंग में नए हैं? [विकास पर्यावरण गाइड](./README.md) से शुरू करें।

यह गाइड इस पाठ्यक्रम में जावा AI ऐप्स के लिए **Azure AI Foundry** मॉडल सेट करता है। आपके पास दो विकल्प हैं:

- **विकल्प A — `azd` + Bicep के साथ प्रावधान (सिफारिश की गई):** एक कमांड Foundry अकाउंट और मॉडल को कोड के रूप में तैनात करता है। कोई पोर्टल क्लिकिंग नहीं।
- **विकल्प B — Azure AI Foundry पोर्टल में मैन्युअल रूप से संसाधन बनाएं।**

दोनों विकल्प **keyless प्रमाणीकरण** (Microsoft Entra ID) का उपयोग करते हैं — कोई API कुंजी कॉपी करने या लीक करने की आवश्यकता नहीं।

## विषय-सूची

- [क्या बनाया जाता है](#क्या-बनाया-जाता-है)
- [पूर्व आवश्यकताएँ](#पूर्व-आवश्यकताएँ)
- [विकल्प A: azd + Bicep के साथ प्रावधान (सिफारिश की गई)](#option-a-provision-with-azd--bicep-recommended)
- [विकल्प B: मैन्युअल रूप से संसाधन बनाएं](#विकल्प-b-मैन्युअल-रूप-से-संसाधन-बनाएं)
- [अपना पर्यावरण कॉन्फ़िगर करें](#अपना-पर्यावरण-कॉन्फ़िगर-करें)
- [अपने सेटअप का परीक्षण करें](#अपने-सेटअप-का-परीक्षण-करें)
- [अगले कदम?](#आगे-क्या)
- [संसाधन](#संसाधन)
- [अतिरिक्त संसाधन](#अतिरिक्त-संसाधन)

## क्या बनाया जाता है

[`infra/`](../../../02-SetupDevEnvironment/infra) में Bicep टेम्पलेट:

- एक **Azure AI Foundry** अकाउंट (`Microsoft.CognitiveServices/accounts`, प्रकार `AIServices`) एक प्रोजेक्ट के साथ
- एक **चैट** डिप्लॉयमेंट - GPT-5.6 Luna (`gpt-5.6-luna`), संस्करण `2026-07-09`, `GlobalStandard` क्षमता `10` (इस मॉडल के लिए 10 अनुरोध/मिनट और 10,000 टोकन/मिनट)
- एक **एम्बेडिंग** डिप्लॉयमेंट - `text-embedding-3-small`, संस्करण `1` (बाद के अध्यायों में उपयोग किया गया)
- एक **keyless भूमिका असाइनमेंट** (`Cognitive Services OpenAI User`) ताकि आप कुंजी प्रबंधन के बिना `az login` से साइन इन कर सकें

## पूर्व आवश्यकताएँ

- एक [Azure सदस्यता](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) और [Maven 3.9+](https://maven.apache.org/download.cgi)

## विकल्प A: azd + Bicep के साथ प्रावधान (सिफारिश की गई)

`02-SetupDevEnvironment` फ़ोल्डर से:

```bash
cd 02-SetupDevEnvironment

# साइन इन करें (दोनों उपकरण)
azd auth login
az login

# Foundry खाता + मॉडल डिप्लॉयमेंट को प्रावधानित करें
azd up
```

`azd` एक **पर्यावरण नाम** (उदाहरण के लिए `genai-java`), **सदस्यता**, और **क्षेत्र** के लिए पूछेगा। अपनी सदस्यता चुनें और उस क्षेत्र को चुनें जहाँ `gpt-5.6-luna` और `text-embedding-3-small` उपलब्ध हैं, उदाहरण के लिए `eastus2`। पुष्टि करें कि मॉडल और डिप्लॉयमेंट प्रकार के लिए क्षेत्र में पर्याप्त कोटा है; उपलब्धता और कोटा सदस्यता के अनुसार भिन्न हो सकते हैं।

प्रावधान पूरा होने पर, azd:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) में परिभाषित सभी चीज़ें तैनात करता है।
2. एक पोस्टप्राविजन हुक चलाता है जो [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) में आपका endpoint और डिप्लॉयमेंट नाम लिखता है (कोई सीक्रेट नहीं)।

> **टिप:** परिवर्तनों को लागू करने के लिए कभी भी `azd up` पुनः चलाएँ। सब कुछ हटाने और लागत रोकने के लिए `azd down` चलाएँ।

उत्पन्न सेटिंग्स देखने के लिए:

```bash
azd env get-values
```

अब [अपने सेटअप का परीक्षण करें](#अपने-सेटअप-का-परीक्षण-करें) पर जाएं।

## विकल्प B: मैन्युअल रूप से संसाधन बनाएं

पोर्टल पसंद है? संसाधन हाथ से बनाएं:

1. [Azure AI Foundry पोर्टल](https://ai.azure.com/) पर जाएं और साइन इन करें।
2. **एक प्रोजेक्ट बनाएं** (यह एक AI Foundry संसाधन भी बनाता है)। इसे `GenAIJava` जैसा कोई नाम दें।
3. अपने प्रोजेक्ट में, **Models + endpoints** → **Deploy model** → **Deploy base model** खोलें।
4. **GPT-5.6 Luna** (मॉडल और डिप्लॉयमेंट नाम `gpt-5.6-luna`, संस्करण `2026-07-09`) को **Global Standard** क्षमता `10` के साथ तैनात करें। यदि आप एम्बेडिंग उदाहरण चाहते हैं तो **text-embedding-3-small**, संस्करण `1`, के लिए भी दोहराएँ।
5. **Overview** से, **endpoint** कॉपी करें (उदाहरण के लिए `https://<resource>.openai.azure.com/`)।
6. खुद को keyless एक्सेस दें: संसाधन पर, **Access control (IAM)** → **Add role assignment** → **Cognitive Services OpenAI User** को अपनी अकाउंट तक असाइन करें।

> **अभी भी समस्या आ रही है?** [Azure AI Foundry दस्तावेज़](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) देखें।

## अपना पर्यावरण कॉन्फ़िगर करें

**यदि आपने विकल्प A (`azd up`) का उपयोग किया है**, तो आपकी सेटिंग्स फ़ाइल पहले से लिखी गई है — कॉन्फ़िगर करने के लिए कुछ नहीं। [अपने सेटअप का परीक्षण करें](#अपने-सेटअप-का-परीक्षण-करें) पर जाएं।

**यदि आपने विकल्प B (मैनुअल) का उपयोग किया है**, उदाहरण का `.env` फ़ाइल खुद बनाएं:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` को अपने endpoint के साथ संपादित करें (कोई कुंजी नहीं — प्रमाणीकरण keyless है):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

संसाधन का Azure OpenAI endpoint उपयोग करें, प्रोजेक्ट URL नहीं। basic-chat ऐप इसे `/openai/v1` में हल करता है और एक स्पष्ट bearer-token क्लाइंट कॉन्फ़िगर करता है; API कुंजी आवश्यक नहीं है।

> **सुरक्षा नोट:** स्टोर करने के लिए कोई API कुंजी नहीं है। आप Microsoft Entra ID के साथ `az login` (स्थानीय) या एक प्रबंधित आइडेंटिटी (Azure में) के माध्यम से प्रमाणीकरण करते हैं। `.env` फ़ाइल में केवल गैर-गुप्त सेटिंग्स होती हैं और इसे पहले से ही `.gitignore` द्वारा कवर किया गया है।

## अपने सेटअप का परीक्षण करें

सुनिश्चित करें कि आप साइन इन हैं ताकि keyless प्रमाणीकरण टोकन प्राप्त कर सके, फिर उदाहरण चलाएं:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # यदि आप पहले से साइन इन नहीं हैं
mvn clean spring-boot:run
```

आपको `gpt-5.6-luna` मॉडल से एक प्रतिक्रिया देखनी चाहिए। छोटे डिफ़ॉल्ट कोटा के भीतर रहने के लिए उदाहरणों को क्रमवार चलाएं; यदि आपको HTTP 429 मिलता है, तो पुनः प्रयास अंतराल के लिए प्रतीक्षा करें और पुनः प्रयास करें।

> **VS कोड उपयोगकर्ता:** चलाने के लिए `F5` दबाएँ। ऐप आपका `.env` स्वचालित रूप से लोड करता है।

> **पूर्ण उदाहरण:** विवरण और समस्या निवारण के लिए [Azure AI Foundry के साथ Basic Chat उदाहरण](./examples/basic-chat-azure/README.md) देखें।

## आगे क्या?

प्रावधान और सफलतापूर्वक उदाहरण चलाने के बाद, आपके पास होगा:
- Azure AI Foundry जिसमें `gpt-5.6-luna` और `text-embedding-3-small` तैनात हैं
- Keyless प्रमाणीकरण (Microsoft Entra ID) — प्रबंधित करने के लिए कोई कुंजी नहीं
- एक स्थानीय `.env` जिसमें आपका endpoint और डिप्लॉयमेंट नाम हैं
- एक जावा विकास पर्यावरण तैयार

**शुरू करने के लिए जारी रखें** [अध्याय 3: मुख्य जनरेटिव AI तकनीकें](../03-CoreGenerativeAITechniques/README.md) में AI एप्लिकेशन बनाने के लिए!

## संसाधन

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID के साथ Keyless प्रमाणीकरण](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry दस्तावेज़](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK संक्रमण](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 के साथ आधिकारिक OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## अतिरिक्त संसाधन

- [VS कोड डाउनलोड करें](https://code.visualstudio.com/Download)
- [Docker Desktop प्राप्त करें](https://www.docker.com/products/docker-desktop)
- [Dev कंटेनर कॉन्फ़िगरेशन](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में ही प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->