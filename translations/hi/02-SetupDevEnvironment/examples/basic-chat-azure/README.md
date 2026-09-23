# Azure AI Foundry के साथ बुनियादी चैट - एंड-टू-एंड उदाहरण

यह उदाहरण एक साधारण Spring Boot एप्लिकेशन है जो **Azure AI Foundry** मॉडल से **keyless authentication** (Microsoft Entra ID) का उपयोग करके कनेक्ट होता है और आपकी सेटअप का परीक्षण करता है। यह Spring AI के `ChatClient` को रखता है, जो **official OpenAI Java SDK** और **Azure OpenAI v1** endpoint द्वारा समर्थित है।

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) में संस्करण Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, और dotenv-java **3.2.0** हैं। यह नमूना `spring-ai-starter-model-openai` का उपयोग करता है और स्पष्ट रूप से `openai-java` और `azure-identity` घोषित करता है; Spring AI 2 ने पुराना Azure OpenAI स्टार्टर हटा दिया।

## विषय सूची

- [पूर्वापेक्षाएँ](#पूर्वापेक्षाएँ)
- [त्वरित आरंभ](#त्वरित-आरंभ)
- [प्रमाणीकरण कैसे काम करता है](#प्रमाणीकरण-कैसे-काम-करता-है)
- [एप्लिकेशन चलाना](#एप्लिकेशन-चलाना)
  - [Maven का उपयोग](#maven-का-उपयोग-करते-हुए)
  - [VS Code का उपयोग](#vs-code-का-उपयोग-करते-हुए)
  - [अपेक्षित आउटपुट](#अपेक्षित-आउटपुट)
- [कॉन्फ़िगरेशन संदर्भ](#कॉन्फ़िगरेशन-संदर्भ)
  - [पर्यावरण चर](#पर्यावरण-चर)
  - [Spring कॉन्फ़िगरेशन](#spring-कॉन्फ़िगरेशन)
- [समस्या निवारण](#समस्या-निवारण)
  - [सामान्य समस्याएं](#सामान्य-समस्याएँ)
  - [डिबग मोड](#डिबग-मोड)
- [अगले कदम](#अगले-कदम)
- [संसाधन](#संसाधन)

## पूर्वापेक्षाएँ

इस उदाहरण को चलाने से पहले सुनिश्चित करें कि आपके पास है:

- एक Azure AI Foundry संसाधन जिसमें एक `gpt-5.6-luna` तैनाती हो - इसे `azd up` के साथ या मैन्युअली [Azure AI Foundry सेटअप गाइड](../../getting-started-azure-openai.md) के माध्यम से तैयार करें
- उस संसाधन पर **Cognitive Services OpenAI User** भूमिका (Bicep टेम्प्लेट आपके लिए यह सौंपते हैं)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), जिसमें `az login` के साथ साइन इन किया गया हो
- Java 21+ और Maven 3.9+

> **कोई API कुंजी आवश्यक नहीं** — प्रमाणीकरण keyless है Microsoft Entra ID के माध्यम से।

## त्वरित आरंभ

```bash
# 1. प्रोजेक्ट पर जाएं
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. साइन इन करें ताकि कीलेस ऑथ टोकन प्राप्त कर सके
az login

# 3. एंडपॉइंट कॉन्फ़िगर करें
#    - यदि आपने `azd up` चलाया है, तो .env आपके लिए लिखा गया है (इस चरण को छोड़ें)।
#    - अन्यथा टेम्पलेट कॉपी करें और AZURE_OPENAI_ENDPOINT सेट करें:
cp .env.example .env

# 4. एप्लिकेशन चलाएं
mvn spring-boot:run
```

## प्रमाणीकरण कैसे काम करता है

यह उदाहरण **Microsoft Entra ID** के साथ प्रमाणीकरण करता है — कोई API कुंजी नहीं है।

एप्लिकेशन स्पष्ट रूप से [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) में प्रमाणीकरण कॉन्फ़िगर करता है:

1. `azureCredential()` `AuthenticationUtil.getBearerTokenSupplier` के साथ `DefaultAzureCredential` और `https://ai.azure.com/.default` स्कोप का उपयोग करके `BearerTokenCredential` बनाता है।
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()` से एक `OpenAIClient` बनाता है, संसाधन endpoint को `/openai/v1` पर हल करता है, और `.credential(...)` के साथ बेयरर क्रेडेंशियल प्रदान करता है।
3. `azureChatModel()` उस क्लाइंट को Spring AI के `OpenAiChatModel` को देता है, जो इस पाठ का `ChatClient` समर्थित करता है।

ये स्पष्ट बीन्स एक वैश्विक `OPENAI_API_KEY` को Azure प्रमाणीकरण को ओवरराइड करने से रोकते हैं। YAML में केवल API कुंजी का अभाव प्रमाणीकरण सेटअप नहीं है। `DefaultAzureCredential` आपके स्थानीय `az login` सेशन या Azure में एक प्रबंधित पहचान का उपयोग कर सकता है; चुनी गई पहचान के पास उपरोक्त संसाधन भूमिका होना अनिवार्य है।

## एप्लिकेशन चलाना

### Maven का उपयोग करते हुए

```bash
mvn spring-boot:run
```

### VS Code का उपयोग करते हुए

1. प्रोजेक्ट को VS Code में खोलें
2. `F5` दबाएँ या "Run and Debug" पैनल का उपयोग करें
3. "Spring Boot-BasicChatApplication" कॉन्फ़िगरेशन चुनें

> **नोट**: एप्लिकेशन अपने वर्किंग डायरेक्टरी से `.env` लोड करता है, जिसमें VS Code से लॉन्च करते समय भी शामिल है।

### अपेक्षित आउटपुट

सफल रन के बाद उदाहरण स्वरूप आउटपुट (स्टार्टअप लॉग्स शामिल नहीं; प्रतिक्रिया शब्दावली भिन्न हो सकती है):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## कॉन्फ़िगरेशन संदर्भ

### पर्यावरण चर

| चर | विवरण | आवश्यक | उदाहरण |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endpoint URL | हाँ | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | चैट मॉडल तैनाती नाम | नहीं | `gpt-5.6-luna` (डिफ़ॉल्ट) |

> कोई API कुंजी चर **नहीं** है — प्रमाणीकरण बिना कुंजी के (Microsoft Entra ID के माध्यम से `az login`) है।

### Spring कॉन्फ़िगरेशन

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) सेटिंग्स `spring.ai.openai` उपसर्ग और सपाट चैट गुण (कोई `options` ब्लॉक नहीं) का उपयोग करती हैं:

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` **Azure तैनाती नाम** है। प्रमाणीकरण उपरोक्त स्पष्ट बीन्स से आता है, न कि `api-key` सेटिंग से। यह पाठ तर्क (reasoning) को अक्षम करता है और completion टोकन 500 तक सीमित करता है; `temperature` और पुराना `max-tokens` सेट नहीं करता।

Microsoft नए एप्लिकेशन के लिए [Azure OpenAI v1 और Responses API के साथ आधिकारिक OpenAI SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) की सिफारिश करता है। चैट पूर्णताएं इस मौजूदा संदेश-आधारित पाठ के लिए समर्थित बनी हैं। GPT-5.6 के लिए, चैट पूर्णताएं पर टूल शामिल करने के लिए `reasoning_effort` को `none` सेट करना चाहिए; तर्क के साथ टूल संयोजन करने के लिए Responses का उपयोग करें। देखें [तर्क मॉडल के साथ टूल कॉलिंग](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)।

## समस्या निवारण

### सामान्य समस्याएँ

<details>
<summary><strong>त्रुटि: 401 / "PermissionDenied" / टोकन त्रुटियाँ</strong></summary>

- `az login` चलाएँ — keyless प्रमाणीकरण के लिए एक सक्रिय साइन-इन टोकन की आवश्यकता होती है
- सुनिश्चित करें कि आपके खाते के पास संसाधन पर **Cognitive Services OpenAI User** भूमिका है
- यदि आपने अभी-अभी यह भूमिका सौंपा है, तो इसे लागू होने में एक मिनट प्रतीक्षा करें
- पुष्टि करें कि आप सही किरायेदार/सदस्यता में हैं (`az account show`)
</details>

<details>
<summary><strong>त्रुटि: "एंडपॉइंट मान्य नहीं है" / कनेक्शन त्रुटियाँ</strong></summary>

- सुनिश्चित करें कि `AZURE_OPENAI_ENDPOINT` पूर्ण आधार URL है (जैसे, `https://your-resource.openai.azure.com/`)
- अंतिम स्लैश की सुसंगतता जांचें
- पुष्टि करें कि एंडपॉइंट आपके प्राविधिक संसाधन से मेल खाता है (`azd env get-values`)
</details>

<details>
<summary><strong>त्रुटि: "तैनाती नहीं मिली"</strong></summary>

- सत्यापित करें कि `AZURE_OPENAI_DEPLOYMENT` Azure में एक तैनाती नाम से मेल खाता है
- जांचें कि मॉडल सफलतापूर्वक तैनात और सक्रिय है
- डिफ़ॉल्ट तैनाती नाम `gpt-5.6-luna` है
</details>

<details>
<summary><strong>त्रुटि: 429 / दर सीमा पार हो गई</strong></summary>

- डिफ़ॉल्ट GPT-5.6 Luna तैनाती में Global Standard क्षमता 10 है: 10 अनुरोध/मिनट और 10,000 टोकन/मिनट
- उदाहरणों को क्रमिक रूप से चलाएँ और पुन: प्रयास करने से पहले सेवा के पुन: प्रयास अंतराल का सम्मान करें
- यह बुनियादी उदाहरण स्वचालित SDK पुन: प्रयास को अक्षम करता है, इसलिए विफल अनुरोध सीधे रिपोर्ट होता है
</details>

<details>
<summary><strong>VS Code: पर्यावरण चर लोड नहीं हो रहे हैं</strong></summary>

- सुनिश्चित करें कि आपकी `.env` फ़ाइल प्रोजेक्ट रूट डायरेक्टरी में है (`pom.xml` के समान स्तर पर)
- VS Code के अंतर्निहित टर्मिनल में `mvn spring-boot:run` चलाने का प्रयास करें
- जांचें कि VS Code Java एक्सटेंशन ठीक से इंस्टॉल है
</details>

### डिबग मोड

विस्तृत लॉगिंग सक्षम करने के लिए [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) में इन पंक्तियों की टिप्पणी हटा दें:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## अगले कदम

**सेटअप पूरा!** अपनी सीखने की यात्रा जारी रखें:

[अध्याय 3: कोर जनरेटिव AI तकनीकें](../../../03-CoreGenerativeAITechniques/README.md)

## संसाधन

- [Spring AI 2 OpenAI Java SDK संक्रमण](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 के साथ आधिकारिक OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID के साथ keyless प्रमाणीकरण](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry पोर्टल](https://ai.azure.com/)
- [Azure AI Foundry दस्तावेज़](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में ही प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->