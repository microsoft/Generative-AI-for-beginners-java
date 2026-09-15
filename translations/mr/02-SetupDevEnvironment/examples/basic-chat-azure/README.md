# Azure AI Foundry सोबत मूलभूत चॅट - एंड-टू-एंड उदाहरण

हे उदाहरण एक सोपी Spring Boot अनुप्रयोग आहे जे **Azure AI Foundry** मॉडेलशी **कुञ्जीशिवाय प्रमाणीकरण** (Microsoft Entra ID) वापरून कनेक्ट होते आणि तुमची सेटअप तपासते. हे Spring AI चा `ChatClient` वापरते, जो **अधिकृत OpenAI Java SDK** आणि **Azure OpenAI v1** एंडपॉइंटद्वारे समर्थित आहे.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) मधील आवृत्त्या Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, आणि dotenv-java **3.2.0** आहेत. या नमुन्यात `spring-ai-starter-model-openai` वापरले आहे आणि `openai-java` आणि `azure-identity` स्पष्टपणे घोषित केले आहेत; Spring AI 2 मध्ये जुना Azure OpenAI स्टार्टर काढला गेला आहे.

## अनुक्रमणिका

- [पूर्वअटी](#पूर्वअटी)
- [त्वरित प्रारंभ](#त्वरित-प्रारंभ)
- [प्रमाणीकरण कसे कार्य करते](#प्रमाणीकरण-कसे-कार्य-करते)
- [अनुप्रयोग चालविणे](#अनुप्रयोग-चालविणे)
  - [Maven वापरून](#maven-वापरून)
  - [VS Code वापरून](#vs-code-वापरून)
  - [अपेक्षित आउटपुट](#अपेक्षित-आउटपुट)
- [कॉन्फिगरेशन संदर्भ](#कॉन्फिगरेशन-संदर्भ)
  - [पर्यावरण चल](#पर्यावरण-चल)
  - [Spring कॉन्फिगरेशन](#spring-कॉन्फिगरेशन)
- [समस्या निवारण](#समस्या-निवारण)
  - [सामान्य समस्या](#सामान्य-समस्या)
  - [डिबग मोड](#डिबग-मोड)
- [पुढील पावले](#पुढील-पावले)
- [साधने](#साधने)

## पूर्वअटी

या उदाहरणाला चालवण्यापूर्वी, खात्री करा की तुमच्याकडे खालील आहेत:

- `gpt-5.6-luna` डिप्लॉयमेंटसह Azure AI Foundry रिसोर्स - ते `azd up` वापरून किंवा मॅन्युअली [Azure AI Foundry सेटअप मार्गदर्शक](../../getting-started-azure-openai.md) द्वारे तयार करा
- त्या रिसोर्सवर **Cognitive Services OpenAI User** भूमिका (Bicep टेम्पलेट्स या भूमिका आपल्यासाठी सोपवतात)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login` वापरून साइन इन केलेले
- Java 21+ आणि Maven 3.9+

> **कोणतीही API की आवश्यक नाही** — प्रमाणीकरण Microsoft Entra ID द्वारे कुञ्जीशिवाय होते.

## त्वरित प्रारंभ

```bash
# 1. प्रकल्पाकडे जा
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. साइन इन करा जेणेकरुन कीलेस प्रमाणीकरण टोकन मिळवू शकेल
az login

# 3. एंडपॉइंट कॉन्फिगर करा
#    - जर तुम्ही `azd up` चालवले असेल, तर .env तुमच्यासाठी लिहिले गेले आहे (हे वगळा).
#    - अन्यथा टेम्पलेट कॉपी करा आणि AZURE_OPENAI_ENDPOINT सेट करा:
cp .env.example .env

# 4. अॅप्लिकेशन चालवा
mvn spring-boot:run
```

## प्रमाणीकरण कसे कार्य करते

या उदाहरणात प्रमाणीकरण **Microsoft Entra ID** वापरून होते — कोणतीही API की नाही.

अनुप्रयोग [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) मध्ये प्रमाणीकरण स्पष्टपणे कॉन्फिगर करते:

1. `azureCredential()` `AuthenticationUtil.getBearerTokenSupplier` वापरून `DefaultAzureCredential` आणि `https://ai.azure.com/.default` स्कोपसह `BearerTokenCredential` बनवते.
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()` वापरून `OpenAIClient` तयार करते, रिसोर्स एंडपॉइंट `/openai/v1` पर्यंत अखेरचा ठरविते, आणि `.credential(...)` वापरून बीयरर क्रेडेंशियल पुरवते.
3. `azureChatModel()` तो क्लायंट Spring AI च्या `OpenAiChatModel` ला पुरवते, जो या टप्प्याचा `ChatClient` समर्थित करतो.

हे स्पष्ट बीन्स एक जागतिक `OPENAI_API_KEY` Azure प्रमाणीकरणावर अधिरोपण होण्यापासून प्रतिबंधित करतात. YAML मध्ये फक्त API कीचा अभाव असल्याने प्रमाणीकरण सेटअप होत नाही. `DefaultAzureCredential` स्थानिकपणे तुमच्या `az login` सत्राचा वापर करू शकते किंवा Azure मधील एक व्यवस्थापित ओळख; कोणतीही निवडलेली ओळख वरील रिसोर्स भूमिकेची सदस्य असावी.

## अनुप्रयोग चालविणे

### Maven वापरून

```bash
mvn spring-boot:run
```

### VS Code वापरून

1. प्रोजेक्ट VS Code मध्ये उघडा
2. `F5` दाबा किंवा "Run and Debug" पॅनेल वापरा
3. "Spring Boot-BasicChatApplication" कॉन्फिगरेशन निवडा

> **टीप**: अनुप्रयोग त्याच्या कामकाजाच्या निर्देशिकेतून `.env` लोड करतो, VS Code मधून सुरु केल्यावर ही प्रक्रिया चालू राहते.

### अपेक्षित आउटपुट

यशस्वी चालविल्यानंतरचे उदाहरणात्मक आउटपुट (स्टार्टअप लॉग वगळले; प्रतिसादातील शब्द थोडे वेगळे असू शकतात):

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

## कॉन्फिगरेशन संदर्भ

### पर्यावरण चल

| चल | वर्णन | आवश्यक आहे | उदाहरण |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) एंडपॉइंट URL | होय | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | चॅट मॉडेल डिप्लॉयमेंटचे नाव | नाही | `gpt-5.6-luna` (डीफॉल्ट) |

> कोणतीही API की चल नाही — प्रमाणीकरण कुञ्जीशिवाय आहे (Microsoft Entra ID द्वारे `az login` वापरून).

### Spring कॉन्फिगरेशन

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) सेटिंग्ज `spring.ai.openai` प्रीफिक्स वापरतात आणि सपाट चॅट प्रॉपर्टीज (कोणतेही `options` ब्लॉक नाही) आहेत:

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

`model` म्हणजे **Azure डिप्लॉयमेंट नाव**. प्रमाणीकरण वर दिलेल्या स्पष्ट बीन्सकडून येते, `api-key` सेटिंगमधून नाही. हे धडा कारणशक्ती (reasoning) आणि पूर्णता टोकन ५०० वर मर्यादित करतो; `temperature` आणि जुने `max-tokens` सेट करत नाही.

Microsoft नवीन अनुप्रयोगांसाठी [अधिकृत OpenAI SDK सह Azure OpenAI v1 आणि Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) शिफारस करते. चॅट पूर्णता या संदेश-आधारित धड्यासाठी अजूनही समर्थित आहे. GPT-5.6 साठी, चॅट पूर्णता मध्ये साधने वापरल्यास `reasoning_effort` `none` सेट करणे आवश्यक आहे; साधने आणि कारणशक्ती एकत्रित करण्यासाठी Responses वापरा. पाहा [कारणशक्ती मॉडेल्ससह साधन कॉल](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## समस्या निवारण

### सामान्य समस्या

<details>
<summary><strong>त्रुटी: 401 / "PermissionDenied" / टोकन त्रुटी</strong></summary>

- `az login` चालवा — कुञ्जीशिवाय प्रमाणीकरणासाठी सक्रिय साइन-इन आवश्यक आहे
- खात्री करा की तुमच्या खात्यावर त्या रिसोर्ससाठी **Cognitive Services OpenAI User** भूमिका आहे
- भूमिका दिल्यानंतर ती प्रचारित होण्यासाठी काही वेळ थांबा
- योग्य ग्राहक/सदस्यता मध्ये आहात का ते तपासा (`az account show`)
</details>

<details>
<summary><strong>त्रुटी: "एंडपॉइंट वैध नाही" / कनेक्शन त्रुटी</strong></summary>

- `AZURE_OPENAI_ENDPOINT` हे पूर्ण बेस URL आहे याची खात्री करा (उदा., `https://your-resource.openai.azure.com/`)
- शेवटचा स्लॅश सुसंगत आहे का ते तपासा
- एंडपॉइंट तुमच्या उपलब्ध रिसोर्सशी जुळतो का ते तपासा (`azd env get-values`)
</details>

<details>
<summary><strong>त्रुटी: "डिप्लॉयमेंट सापडले नाही"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` हा Azure मधील डिप्लॉयमेंट नावाशी जुळतो का ते तपासा
- मॉडेल यशस्वी डिप्लॉय आणि सक्रिय आहे का ते तपासा
- डीफॉल्ट डिप्लॉयमेंट नाव `gpt-5.6-luna` आहे
</details>

<details>
<summary><strong>त्रुटी: 429 / दर मर्यादा ओलांडली</strong></summary>

- डीफॉल्ट GPT-5.6 Luna डिप्लॉयमेंटमध्ये Global Standard क्षमता 10 आहे: १० विनंत्या/मिनिट आणि १०,००० टोकन्स/मिनिट
- उदाहरणे साखळीने चालवा आणि पुन्हा प्रयत्न करण्यापूर्वी सर्व्हिसचे पुनर्प्रयत्न अंतर वाट पाहा
- हा मूलभूत उदाहरण आपोआप SDK पुनर्प्रयत्न अक्षम करतो, त्यामुळे अपयशी विनंती थेट कळवली जाते
</details>

<details>
<summary><strong>VS Code: पर्यावरण चल लोड होत नाहीत</strong></summary>

- तुमची `.env` फाइल प्रोजेक्टच्या मूळ निर्देशिकेत आहे का ते तपासा (`pom.xml` शी एकसारखा स्तर)
- VS Codeच्या अखंड टर्मिनलमध्ये `mvn spring-boot:run` चालवून बघा
- VS Code Java विस्तार योग्य प्रकारे प्रतिष्ठापित आहे का तपासा
</details>

### डिबग मोड

तपशीलवार लॉगिंग सक्षम करण्यासाठी, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) मधील हे ओळी अनकमेंट करा:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## पुढील पावले

**सेटअप पूर्ण!** तुमच्या शिक्षण प्रवासाला पुढे चालू ठेवा:

[अध्याय 3: कोर जनरेटिव्ह AI तंत्रे](../../../03-CoreGenerativeAITechniques/README.md)

## साधने

- [Spring AI 2 OpenAI Java SDK संक्रमण](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [अधिकृत OpenAI Java SDK सह Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID सह कुञ्जीशिवाय प्रमाणीकरण](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry पोर्टल](https://ai.azure.com/)
- [Azure AI Foundry दस्तऐवज](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. जरी आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थलावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->