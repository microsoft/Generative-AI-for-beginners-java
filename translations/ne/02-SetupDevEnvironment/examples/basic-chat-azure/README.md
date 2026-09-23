# Azure AI Foundry सँग आधारभूत च्याट - अन्त्यदेखि अन्त्यसम्म उदाहरण

यो उदाहरण एउटा सरल Spring Boot अनुप्रयोग हो जुन **Azure AI Foundry** मोडेलमा **keyless authentication** (Microsoft Entra ID) प्रयोग गरेर जडान गर्छ र तपाईंको सेटअप परीक्षण गर्छ। यसले Spring AI को `ChatClient` प्रयोग गर्छ, जुन **आधिकारिक OpenAI Java SDK** र **Azure OpenAI v1** एन्डपोइन्टले समर्थित छ।

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) मा संस्करणहरू Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, र dotenv-java **3.2.0** हुन्। नमूनाले `spring-ai-starter-model-openai` प्रयोग गर्छ र स्पष्ट रूपमा `openai-java` र `azure-identity` घोषणा गर्छ; Spring AI 2 ले पुरानो Azure OpenAI स्टार्टर हटायो।

## सामग्री तालिका

- [पूर्वआवश्यकताहरू](#पूर्वआवश्यकताहरू)
- [चाँडो सुरु गर्ने तरिका](#चाँडो-सुरु-गर्ने-तरिका)
- [प्रमाणीकरण कसरी काम गर्छ](#प्रमाणीकरण-कसरी-काम-गर्छ)
- [अनुप्रयोग चलाउने](#अनुप्रयोग-चलाउने)
  - [Maven प्रयोग गर्दै](#maven-प्रयोग-गर्दै)
  - [VS Code प्रयोग गर्दै](#vs-code-प्रयोग-गर्दै)
  - [अपेक्षित आउटपुट](#अपेक्षित-आउटपुट)
- [कन्फिगरेसन सन्दर्भ](#कन्फिगरेसन-सन्दर्भ)
  - [पर्यावरण चरहरू](#वातावरण-चरहरू)
  - [Spring कन्फिगरेसन](#spring-कन्फिगरेसन)
- [समस्या समाधान](#समस्या-समाधान)
  - [सामान्य समस्याहरू](#सामान्य-समस्याहरू)
  - [डिबग मोड](#डिबग-मोड)
- [अगाडि के गर्ने](#अगाडिका-चरणहरू)
- [स्रोतहरू](#स्रोतहरू)

## पूर्वआवश्यकताहरू

यो उदाहरण चलाउनु अघि, सुनिश्चित गर्नुहोस् कि तपाईंसँग छ:

- `gpt-5.6-luna` तैनाथी भएको Azure AI Foundry स्रोत - यो `azd up` वा म्यानुअली [Azure AI Foundry सेटअप मार्गनिर्देशन](../../getting-started-azure-openai.md) मार्फत प्रावधान गर्न सकिन्छ
- सो स्रोतमा **Cognitive Services OpenAI User** भूमिका (Bicep टेम्प्लेटहरूले तपाईंका लागि यो सेट गर्छन्)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login` द्वारा साइन इन गरिएको
- Java 21+ र Maven 3.9+

> **कुनै API कुञ्जी आवश्यक छैन** — प्रमाणीकरण Microsoft Entra ID मार्फत keyless छ।

## चाँडो सुरु गर्ने तरिका

```bash
# 1. प्रोजेक्टमा जानुहोस्
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. साइन इन गर्नुहोस् ताकि keyless प्रमाणीकरणले टोकन प्राप्त गर्न सकोस्
az login

# 3. अन्त बिन्दु कन्फिगर गर्नुहोस्
#    - यदि तपाईंले `azd up` चलाउनुभयो भने, तपाईंको लागि .env लेखिएको थियो (यो छोड्नुहोस्)।
#    - अन्यथा ढाँचा प्रतिलिपि गर्नुहोस् र AZURE_OPENAI_ENDPOINT सेट गर्नुहोस्:
cp .env.example .env

# 4. एप्लिकेशन चलाउनुहोस्
mvn spring-boot:run
```

## प्रमाणीकरण कसरी काम गर्छ

यो उदाहरण **Microsoft Entra ID** सँग प्रमाणीकरण गर्दछ — कुनै API कुञ्जी छैन।

अनुप्रयोगले [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) मा स्पष्ट रूपमा प्रमाणीकरण कन्फिगर गर्छ:

1. `azureCredential()` ले `AuthenticationUtil.getBearerTokenSupplier` प्रयोग गरेर `DefaultAzureCredential` र `https://ai.azure.com/.default` स्कोपका साथ `BearerTokenCredential` सिर्जना गर्छ।
2. `azureOpenAiClient()` ले `OpenAIOkHttpClient.builder()` प्रयोग गरी `OpenAIClient` बनाउँछ, स्रोत एन्डपोइन्टलाई `/openai/v1` मा समाधान गर्छ, र `.credential(...)` बाट bearer क्रेडेन्सियल दिन्छ।
3. `azureChatModel()` ले त्यो क्लाइन्ट Spring AI को `OpenAiChatModel` लाई दिन्छ, जुन पाठको `ChatClient` को समर्थन गर्छ।

यी स्पष्ट बीनहरूले ग्लोबल `OPENAI_API_KEY` लाई Azure प्रमाणीकरणबाट अधिलेखन हुनबाट रोक्छन्। YAML बाट केवल API कुञ्जी हटाउनु भनेको प्रमाणीकरण सेटअप होइन। `DefaultAzureCredential` ले तपाईको स्थानीय `az login` सत्र वा Azure मा म्यानेज्ड आइडेन्टिटी प्रयोग गर्न सक्छ; जस्तोसुकै आइडेन्टिटी चयन गरिएको छ त्यसले माथि सूचीबद्ध स्रोत भूमिका पाउनुपर्छ।

## अनुप्रयोग चलाउने

### Maven प्रयोग गर्दै

```bash
mvn spring-boot:run
```

### VS Code प्रयोग गर्दै

1. परियोजना VS Code मा खोल्नुहोस्
2. `F5` थिच्नुहोस् वा "Run and Debug" प्यानल प्रयोग गर्नुहोस्
3. "Spring Boot-BasicChatApplication" कन्फिगरेसन चयन गर्नुहोस्

> **सूचना**: अनुप्रयोगले `.env` आफ्नो कार्य निर्देशिकाबाट लोड गर्छ, VS Code बाट सुरु गर्दा समेत।

### अपेक्षित आउटपुट

सफल चलाएको पछिको उदाहरणात्मक आउटपुट (सुरु हुने लगहरू समावेश गरिएको छैन; प्रतिक्रिया शब्दावली फरक पर्न सक्छ):

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

## कन्फिगरेसन सन्दर्भ

### वातावरण चरहरू

| चर | विवरण | आवश्यक | उदाहरण |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) एन्डपोइन्ट URL | हो | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | च्याट मोडेल तैनाथी नाम | होइन | `gpt-5.6-luna` (पूर्वनिर्धारित) |

> कुनै API कुञ्जी चर छैन — प्रमाणीकरण keyless (Microsoft Entra ID द्वारा `az login`) हो।

### Spring कन्फिगरेसन

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) सेटिङहरूले `spring.ai.openai` पूर्वसर्ग र सपाट च्याट गुणहरू प्रयोग गर्छन् (कुनै `options` ब्लक छैन):

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

`model` भनेको **Azure तैनाथी नाम** हो। प्रमाणीकरण माथि वर्णन गरिएका स्पष्ट बीनहरूबाट आउँछ, `api-key` सेटिङबाट होइन। पाठले reasoning लाई अक्षम गरेको छ र ५०० मा completion tokens को सीमा लगाएको छ; यसले `temperature` र पुरानो `max-tokens` सेट नगरेको छ।

Microsoft ले नयाँ अनुप्रयोगहरूका लागि [आधिकारिक OpenAI SDK संग Azure OpenAI v1 र Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) सिफारिश गर्छ। Chat Completions यस पहिलेको सन्देश-आधारित पाठका लागि समर्थित छ। GPT-5.6 का लागि, टुलहरू समावेश गर्दै Chat Completions अनुरोधहरूले `reasoning_effort` लाई `none` मा सेट गर्नुपर्छ; reasoning र टुल संयोजन गर्दा Responses प्रयोग गर्नुहोस्। हेर्नुहोस् [reasoning मोडेलहरूसँग टुल कलिङ](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)।

## समस्या समाधान

### सामान्य समस्याहरू

<details>
<summary><strong>त्रुटि: 401 / "PermissionDenied" / टोकन त्रुटिहरू</strong></summary>

- `az login` चलाउनुहोस् — keyless प्रमाणीकरणले सक्रिय साइन-इनले टोकन पाउन आवश्यक छ
- खातामा स्रोतमा **Cognitive Services OpenAI User** भूमिका छ कि छैन जाँच्नुहोस्
- यदि तपाईंले भर्खर भूमिका दिनुभएको छ भने, यसको फैलावटको लागि केही समय पर्खनुहोस्
- तपाईं सही tenant/subscription मा हुनुहुन्छ कि छैन पुष्टि गर्नुहोस् (`az account show`)
</details>

<details>
<summary><strong>त्रुटि: "एन्डपोइन्ट मान्य छैन" / जडान त्रुटिहरू</strong></summary>

- `AZURE_OPENAI_ENDPOINT` पूर्ण आधार URL हो कि छैन सुनिश्चित गर्नुहोस् (जस्तै, `https://your-resource.openai.azure.com/`)
- पछाडि स्ल्यासको स्थिरता जाँच गर्नुहोस्
- एन्डपोइन्ट तपाईंले प्रावधान गरेको स्रोतसँग मेल खाँदो छ कि छैन जांच गर्नुहोस् (`azd env get-values`)
</details>

<details>
<summary><strong>त्रुटि: "तैनाथी भेटिएन"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` Azure मा कुनै तैनाथी नामसंग मेल खान्छ कि छैन सुनिश्चित गर्नुहोस्
- मोडेल सफलतापूर्वक तैनाथी गरिएको र सक्रिय छ कि छैन जाँच गर्नुहोस्
- पूर्वनिर्धारित तैनाथी नाम `gpt-5.6-luna` हो
</details>

<details>
<summary><strong>त्रुटि: 429 / दर सीमा उल्लंघन गरियो</strong></summary>

- पूर्वनिर्धारित GPT-5.6 Luna तैनाथीमा Global Standard क्षमता 10 छ: १० अनुरोध/मिनेट र १०,००० टोकन/मिनेट
- उदाहरणहरू क्रमशः चलाउनुहोस् र पुनः प्रयास अघि सेवाको पुनः प्रयास अवधि कुर्नुहोस्
- यो आधारभूत उदाहरणले स्वत: SDK पुनः प्रयासहरू अक्षम गरेको छ, त्यसैले असफल अनुरोध सिधै रिपोर्ट गरिन्छ
</details>

<details>
<summary><strong>VS Code: वातावरण चरहरू लोड हुँदैनन्</strong></summary>

- सुनिश्चित गर्नुहोस् कि तपाईंको `.env` फाइल परियोजना रुट निर्देशिकामा छ (`pom.xml` को स्तरसँग समान)
- VS Code को एकीकृत टर्मिनलमा `mvn spring-boot:run` चलाएर प्रयास गर्नुहोस्
- VS Code Java एक्सटेन्सन ठीकसँग स्थापना भएको छ कि छैन जाँच्नुहोस्
</details>

### डिबग मोड

विस्तृत लगिंग सक्षम गर्न, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) मा यी पङ्क्तिहरू अनकमेंट गर्नुहोस्:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## अगाडिका चरणहरू

**सेटअप पूरा भयो!** आफ्नो सिकाइ यात्रा जारी राख्नुहोस्:

[अध्याय ३: मुख्य जेनेरेटिभ AI प्रविधिहरू](../../../03-CoreGenerativeAITechniques/README.md)

## स्रोतहरू

- [Spring AI 2 OpenAI Java SDK संक्रमण](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [आधिकारिक OpenAI Java SDK Azure OpenAI v1 संग](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID सँग keyless प्रमाणीकरण](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry पोर्टल](https://ai.azure.com/)
- [Azure AI Foundry कागजात](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी सही हुन प्रयास गर्छौं, तर कृपया जानकार हुनुस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छन्। मूल दस्तावेज़ यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा त्रुटिको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->