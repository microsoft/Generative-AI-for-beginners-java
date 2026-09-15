# Azure AI Foundry साठी विकास वातावरण सेट करणे

> या मार्गदर्शिकेमध्ये या कोर्समधील Java AI अॅप्ससाठी **Azure AI Foundry** मॉडेल्स सेट केली जातात, जे **keyless** प्रमाणीकरण (Microsoft Entra ID) वापरून — कोणतेही API की नाहीत. टूलिंगमध्ये नवीन आहात? सुरू करा [development environment guide](./README.md) पासून.

या मार्गदर्शिकेमध्ये या कोर्समधील Java AI अॅप्ससाठी **Azure AI Foundry** मॉडेल्स सेट केली जातात. तुमच्याकडे दोन मार्ग आहेत:

- **पर्याय A — `azd` + Bicep सह प्राव्हिजन करा (शिफारस केलेले):** एक कमांड Foundry खाते आणि मॉडेल्स कोड म्हणून तैनात करते. कोणतेही पोर्टल क्लिकिंग नाही.
- **पर्याय B — Azure AI Foundry पोर्टलमध्ये संसाधने मॅन्युअली तयार करा**.

दोन्ही मार्गे **keyless authentication** (Microsoft Entra ID) वापरतात — कॉपी किंवा गळती करू शकणारे कोणतेही API की नाहीत.

## सामग्रीचे अनुक्रमणिका

- [काय तयार केले जाते](#काय-तयार-केले-जाते)
- [पूर्वअट](#पूर्वअट)
- [पर्याय A: azd + Bicep सह प्राव्हिजन करा (शिफारस केलेले)](#option-a-provision-with-azd--bicep-recommended)
- [पर्याय B: मॅन्युअली संसाधने तयार करा](#पर्याय-b-संसाधने-मॅन्युअली-तयार-करा)
- [तुमचे वातावरण कॉन्फिगर करा](#तुमचे-वातावरण-कॉन्फिगर-करा)
- [तुमची सेटअप चाचणी करा](#तुमची-सेटअप-चाचणी-करा)
- [पुढे काय?](#पुढे-काय)
- [संसाधने](#संसाधने)
- [अतिरिक्त संसाधने](#अतिरिक्त-संसाधने)

## काय तयार केले जाते

[`infra/`](../../../02-SetupDevEnvironment/infra) मधील Bicep टेम्प्लेट्स हे प्राव्हिजन करतात:

- एक **Azure AI Foundry** खाते (`Microsoft.CognitiveServices/accounts`, प्रकार `AIServices`) प्रकल्पासह
- एक **चॅट** तैनात - GPT-5.6 Luna (`gpt-5.6-luna`), आवृत्ती `2026-07-09`, जे `GlobalStandard` क्षमता `10` (10 विनंत्या/मिनिट आणि 10,000 टोकन्स/मिनिट या मॉडेलसाठी) सह आहे
- एक **एंबेडिंग** तैनात - `text-embedding-3-small`, आवृत्ती `1` (नंतरच्या章节 मध्ये वापरलेले)
- एक **keyless role assignment** (`Cognitive Services OpenAI User`) जेणेकरून तुम्ही `az login` द्वारे साइन इन करू शकता, की व्यवस्थापित करण्याची गरज नाही

## पूर्वअट

- एक [Azure सदस्यता](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) आणि [Maven 3.9+](https://maven.apache.org/download.cgi)

## पर्याय A: azd + Bicep सह प्राव्हिजन करा (शिफारस केलेले)

`02-SetupDevEnvironment` फोल्डरमधून:

```bash
cd 02-SetupDevEnvironment

# साईन इन करा (दोन्ही साधने)
azd auth login
az login

# फाउंड्री खाते + मॉडेल तैनाती निश्चित करा
azd up
```

`azd` तुम्हाला **पर्यावरण नाव** (उदा. `genai-java`), **सदस्यता**, आणि **प्रदेश** विचारतो. तुमची स्वतःची सदस्यता आणि `gpt-5.6-luna` आणि `text-embedding-3-small` उपलब्ध असलेला प्रदेश निवडा, उदाहरणार्थ `eastus2`. खात्री करा की सदस्यता त्या प्रदेशातील मॉडेल आणि तैनात प्रकारासाठी पुरेशी कोटा आहे; उपलब्धता आणि कोटा सदस्यतेनुसार वेगळी असते.

प्राव्हिजनिंग पूर्ण झाल्यावर, azd:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) मध्ये परिभाषित सगळे तैनात करते.
2. एक पोस्टप्राव्हिजन हुक चालवतो जो [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) मध्ये तुमचे endpoint आणि तैनात करणारे नावे लिहितो (कोणतेही गुपित नाही).

> **टीप:** `azd up` कोणत्याही वेळी पुन्हा चालवा बदल लागू करण्यासाठी. सर्व काही हटवण्यासाठी आणि खर्च थांबवण्यासाठी `azd down` चालवा.

तयार सेटिंग्ज पाहण्यासाठी:

```bash
azd env get-values
```

आता पुढे जा [तुमची सेटअप चाचणी करा](#तुमची-सेटअप-चाचणी-करा) येथे.

## पर्याय B: संसाधने मॅन्युअली तयार करा

पोर्टल प्राधान्य देता? हाताने संसाधने तयार करा:

1. [Azure AI Foundry पोर्टल](https://ai.azure.com/) येथे जा आणि साइन इन करा.
2. **एक प्रकल्प तयार करा** (हे देखील AI Foundry संसाधन तयार करते). त्याला `GenAIJava` सारखे नाव द्या.
3. तुमच्या प्रकल्पात, **Models + endpoints** → **Deploy model** → **Deploy base model** उघडा.
4. **GPT-5.6 Luna** तैनात करा (मॉडेल आणि तैनात नांव `gpt-5.6-luna`, आवृत्ती `2026-07-09`) **Global Standard** क्षमता `10` सह. जर तुम्हाला एंबेडिंग उदाहरणे हवी असतील तर **text-embedding-3-small**, आवृत्ती `1`, वर पुन्हा करा.
5. **Overview** मधून, **endpoint** कॉपी करा (उदा. `https://<resource>.openai.azure.com/`).
6. स्वतःला keyless प्रवेश द्या: संसाधनावर, **Access control (IAM)** → **Add role assignment** → **Cognitive Services OpenAI User** तुमच्या खात्यावर नियुक्त करा.

> **अजून अडचण येतेय?** [Azure AI Foundry दस्तऐवजीकरण](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) पहा.

## तुमचे वातावरण कॉन्फिगर करा

**जर तुम्ही पर्याय A (`azd up`) वापरला असेल**, तुमची सेटिंग्ज फाइल आधीच लिहिलेली आहे — कॉन्फिगर करण्यासाठी काही नाही. पुढे जा [तुमची सेटअप चाचणी करा](#तुमची-सेटअप-चाचणी-करा).

**जर तुम्ही पर्याय B (मॅन्युअल) वापरला असेल**, तर उदाहरणाचा `.env` फाइल स्वतः तयार करा:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` संपादित करा तुमच्या endpoint सह (की नाही — प्रमाणीकरण keyless आहे):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

प्रकल्प URL नाही, संसाधनाचा Azure OpenAI endpoint वापरा. basic-chat अॅप त्याला `/openai/v1` पर्यंत सोडवतो आणि स्पष्ट bearer-token क्लायंट कॉन्फिगर करतो; API कीची गरज नाही.

> **सुरक्षा नोट:** स्टोअर करण्यासाठी कोणतीही API की नाही. तुम्ही Microsoft Entra ID द्वारे `az login` (स्थानिक) किंवा मॅनेज्ड आयडेंटिटी (Azure मध्ये) वापरून प्रमाणीकरण करता. `.env` फाइलमध्ये केवळ गैर-गुपित सेटिंग्ज असतात आणि ती `.gitignore` ने आधीच संरक्षित आहे.

## तुमची सेटअप चाचणी करा

keyless auth टोकन मिळवू शकेल यासाठी साइन इन असलात याची खात्री करा, नंतर उदाहरण चालवा:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # जर आपण आधीच साइन इन केलेले नसाल तर
mvn clean spring-boot:run
```

तुम्हाला `gpt-5.6-luna` मॉडेलकडून प्रतिसाद दिसेल. छोटे डीफॉल्ट कोटा आत राहण्यासाठी उदाहरणे मात्र क्रमाने चालवा; जर HTTP 429 मिळाली, तर पुन्हा प्रयत्न करण्यापूर्वी रिट्री अंतराची वाट पाहा.

> **VS Code वापरकर्ते:** चालवण्यासाठी `F5` दाबा. अॅप तुमचा `.env` आपोआप लोड करतो.

> **पूर्ण उदाहरण:** सविस्तर माहिती आणि समस्या निवारणासाठी [Basic Chat with Azure AI Foundry example](./examples/basic-chat-azure/README.md) पहा.

## पुढे काय?

प्राव्हिजनिंगनंतर आणि उदाहरण यशस्वीरित्या चालवल्यानंतर, तुमच्याकडे खालील गोष्टी असतील:
- Azure AI Foundry मध्ये `gpt-5.6-luna` आणि `text-embedding-3-small` तैनात केलेले
- Keyless प्रमाणीकरण (Microsoft Entra ID) — कोणत्याही की व्यवस्थापित करायची गरज नाही
- तुमच्या endpoint आणि तैनात नावे असलेली स्थानिक `.env` फाइल
- Java विकास वातावरण तयार

**सुरू करा** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) येथे AI अॅप्लिकेशन्स तयार करण्यासाठी!

## संसाधने

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID सह Keyless प्रमाणीकरण](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry दस्तऐवजीकरण](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK संक्रमण](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 सह अधिकृत OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## अतिरिक्त संसाधने

- [VS Code डाउनलोड करा](https://code.visualstudio.com/Download)
- [Docker Desktop मिळवा](https://www.docker.com/products/docker-desktop)
- [Dev Container कॉन्फिगरेशन](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. जरी आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थलावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->