# ភ្លុកជជែកមូលដ្ឋានជាមួយ Azure AI Foundry - ឧទាហរណ៍ចុងបញ្ចប់ទៅចុងបញ្ចប់

ឧទាហរណ៍នេះគឺជាកម្មវិធី Spring Boot សាមញ្ញមួយដែលភ្ជាប់ទៅនឹងម៉ូដែល **Azure AI Foundry** ដោយប្រើ **ការផ្ទៀងផ្ទាត់អត្តសញ្ញាណគ្មានកូនសោ** (Microsoft Entra ID) ហើយសាកល្បងការតំឡើងរបស់អ្នក។ វារក្សាទុក Spring AI's `ChatClient` ដែលគាំទ្រដោយ **official OpenAI Java SDK** និងចុងបញ្ចប់ **Azure OpenAI v1**។

កំណែដែលមាននៅក្នុង [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) គឺ Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, និង dotenv-java **3.2.0**។ ឧទាហរណ៍នេះប្រើ `spring-ai-starter-model-openai` និងប្រកាសដាច់ខាត `openai-java` និង `azure-identity`; Spring AI 2 បានដកចេញ starter ថ្មី Azure OpenAI ដូចពីមុន។

## តារាងមាតិកា

- [លក្ខខ័ណ្ឌមុនប្រើប្រាស់](#លក្ខខ័ណ្ឌមុនប្រើប្រាស់)
- [ចាប់ផ្តើមយ៉ាងរហ័ស](#ចាប់ផ្តើមយ៉ាងរហ័ស)
- [ការធ្វើការផ្ទៀងផ្ទាត់អត្តសញ្ញាណ](#របៀបការផ្ទៀងផ្ទាត់អត្តសញ្ញាណ)
- [ការរត់កម្មវិធី](#រត់កម្មវិធី)
  - [ប្រើ Maven](#ប្រើ-maven)
  - [ប្រើ VS Code](#ប្រើ-vs-code)
  - [លទ្ធផលដែលរំពឹងទុក](#លទ្ធផលដែលរំពឹងទុក)
- [យោងការកំណត់រចនា](#យោងការកំណត់រចនា)
  - [អថេរបរិយាកាស](#អថេរបរិយាកាស)
  - [កំណត់រចនាសម្ព័ន្ធ Spring](#កំណត់រចនាសម្ព័ន្ធ-spring)
- [ដោះស្រាយបញ្ហា](#ដោះស្រាយបញ្ហា)
  - [បញ្ហាមួយទូទៅ](#បញ្ហាមានទូទៅ)
  - [ម៉ូដដ្បិតកំហុស](#ម៉ូដដ្បិតកំហុស)
- [ជំហានបន្ទាប់](#ជំហានបន្ទាប់)
- [ធនធាន](#ធនធាន)

## លក្ខខ័ណ្ឌមុនប្រើប្រាស់

មុនពេលរត់ឧទាហរណ៍នេះ សូមធានាថាអ្នកមាន៖

- ប្រភព Azure AI Foundry មួយមាន `gpt-5.6-luna` deployment - បង្កើតវាដោយ `azd up` ឬដៃគូតាម [Azure AI Foundry setup guide](../../getting-started-azure-openai.md)
- តួនាទី **Cognitive Services OpenAI User** លើធនធាននោះ (Bicep templates បានចាត់តួនាទីនេះសម្រាប់អ្នក)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), បានចុះឈ្មោះដោយ `az login`
- Java 21+ និង Maven 3.9+

> **មិន​ត្រូវ​ការ​កូន​សោ API** — ការផ្ទៀងផ្ទាត់គ្មានកូនសោតាម Microsoft Entra ID។

## ចាប់ផ្តើមយ៉ាងរហ័ស

```bash
# 1. នាវិហ្គេតទៅគម្រោង
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. ចូលប្រើ ដូច្នេះការផ្តល់សិទ្ធិគ្មានកូនសោអាចទទួលបានសញ្ញាសម្គាល់
az login

# 3. កំណត់ពាណិជ្ជកម្ម
#    - ប្រសិនបើអ្នកបានបញ្ជា `azd up`, .env ត្រូវបានសរសេរដោយស្វ័យប្រវត្តិសម្រាប់អ្នក (រំលងនេះ)។
#    - ប្រសិនបើមិនដូចនេះ សូមចម្លងទំព័រគំរូ ហើយកំណត់ AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. ដំណើរការកម្មវិធី
mvn spring-boot:run
```

## របៀបការផ្ទៀងផ្ទាត់អត្តសញ្ញាណ

ឧទាហរណ៍នេះផ្ទៀងផ្ទាត់ជាមួយ **Microsoft Entra ID** — មិនមានកូនសោ API ទេ។

កម្មវិធីកំណត់ការផ្ទៀងផ្ទាត់ទ្រង់ទ្រាយច្បាស់លាស់នៅក្នុង [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` បង្កើត `BearerTokenCredential` ដោយប្រើ `AuthenticationUtil.getBearerTokenSupplier` ជាមួយ `DefaultAzureCredential` និងវិសាលភាព `https://ai.azure.com/.default`។
2. `azureOpenAiClient()` បង្កើត `OpenAIClient` ជាមួយ `OpenAIOkHttpClient.builder()`, ដំណោះស្រាយចុងបញ្ចប់ធនធានទៅ `/openai/v1`, ហើយផ្គត់ផ្គង់កុនប៊ែរ​ដោយ `.credential(...)`។
3. `azureChatModel()` ផ្គត់ផ្គង់ client នោះទៅ Spring AI's `OpenAiChatModel` ដែលគាំទ្រកម្មវិធី `ChatClient` នៅក្នុងមេរៀននេះ។

ធាតុនេះគឺរក្សាទុកឲ្យ `OPENAI_API_KEY` របស់លោកអ្នកមិនប៉ះពាល់ការផ្ទៀងផ្ទាត់ Azure បានទេ។ ការខកខានជាមួយសោ API ពី YAML តែមួយគឺមិនមែនការតំឡើងការផ្ទៀងផ្ទាត់ទេ។ `DefaultAzureCredential` អាចប្រើសម័យ `az login` របស់អ្នកនៅក្នុងមshinកុំព្យូទ័រផ្ទាល់ ឬ managed identity នៅក្នុង Azure; អត្តសញ្ញាណដែលជ្រើសរើសត្រូវតែនៅមានតួនាទីធនធានដូចបានរាយការណ៍ខាងលើ។

## រត់កម្មវិធី

### ប្រើ Maven

```bash
mvn spring-boot:run
```

### ប្រើ VS Code

1. បើកគម្រោងនៅ VS Code
2. ចុច `F5` ឬប្រើផ្ទាំង "Run and Debug"
3. ជ្រើសរើសការកំណត់ "Spring Boot-BasicChatApplication"

> **សម្គាល់**: កម្មវិធីរុករក `.env` ពីថតការងាររបស់វា រួមទាំងពេលចាប់ផ្តើមពី VS Code។

### លទ្ធផលដែលរំពឹងទុក

លទ្ធផលបង្ហាញបន្ទាប់ពីរត់បានជោគជ័យ (ឯកសារប្រសាសន៍ចាប់ផ្តើមមិនបង្ហាញ; ពាក្យឆ្លើយនឹងខុសគ្នា):

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

## យោងការកំណត់រចនា

### អថេរបរិយាកាស

| អថេរ | ការពិពណ៌នា | តម្រូវការ | ឧទាហរណ៍ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL ចុងបញ្ចប់ Foundry (Azure OpenAI) | បាទ/ចាស | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | ឈ្មោះការចេញផ្សាយម៉ូដែលជជែក | ទេ | `gpt-5.6-luna` (លំនាំដើម) |

> គ្មានអថេរ API key — ការផ្ទៀងផ្ទាត់គឺគ្មានកូនសោ (Microsoft Entra ID តាម `az login`)។

### កំណត់រចនាសម្ព័ន្ធ Spring

ការកំណត់នៅក្នុង [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ប្រើ prefix `spring.ai.openai` និងលំនាំឡើងជាតម្លៃស្រួល (flat chat properties) ដោយគ្មានប្លុក `options`៖

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

`model` គឺជាឈ្មោះ **deployment Azure**។ ការផ្ទៀងផ្ទាត់មានពីធាតុច្បាស់លាស់ខាងលើ មិនមែនពីការ​កំណត់ `api-key` ។ មេរៀនបិទការគិតលទ្ធផល (reasoning) ហើយកំណត់កាប់មុខToken completion ទៅ ៥០០; វាបោះបង់ការកំណត់ `temperature` និង `max-tokens` បែបចាស់។

Microsoft ផ្តល់អនុសាសន៍ឲ្យប្រើ [official OpenAI SDK ជាមួយ Azure OpenAI v1 និង API Responses សម្រាប់កម្មវិធីថ្មីៗ](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)។ Chat Completions នៅតែគាំទ្រសម្រាប់មេរៀនផ្អែកលើសារ។ សម្រាប់ GPT-5.6, ទំព័រដែលមានឧបករណ៍នៅលើ Chat Completions ត្រូវកំណត់ `reasoning_effort` ទៅ `none`; ប្រើ Responses ពេលបញ្ចូលការគិតលទ្ធផលជាមួយឧបករណ៍។ មើល [ការហៅឧបករណ៍ជាមួយម៉ូដែល reasoning](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)។

## ដោះស្រាយបញ្ហា

### បញ្ហាមានទូទៅ

<details>
<summary><strong>កំហុស: 401 / "PermissionDenied" / កំហុស token</strong></summary>

- រត់ `az login` — auth គ្មានកូនសោត្រូវការចូលប្រើដែលទាន់សម័យដើម្បីទទួលបាន token
- ពិនិត្យមើលថាអ្នកមានតួនាទី **Cognitive Services OpenAI User** លើធនធាន
- ប្រសិនបើយោងតំណែងថ្មីទើបតែងត្រូវរង់ចាំមួយនាទីដើម្បីឲ្យវាលាតត្រដាង
- បញ្ជាក់ថាអ្នកនៅក្នុង tenant/subscription ត្រឹមត្រូវ (`az account show`)
</details>

<details>
<summary><strong>កំហុស: "The endpoint is not valid" / កំហុសផ្ដោតភ្ជាប់</strong></summary>

- ប្រាកដថា `AZURE_OPENAI_ENDPOINT` ជា URL មូលដ្ឋានពេញលេញ (ឧ. `https://your-resource.openai.azure.com/`)
- ពិនិត្យសញ្ញាចុងបញ្ចប់ជាមួយសញ្ញា `/` ត្រូវគ្នា
- ប្រាកដថាចុងបញ្ចប់ត្រូវនឹងធនធានដែលបានបង្កើត (`azd env get-values`)
</details>

<details>
<summary><strong>កំហុស: "The deployment was not found"</strong></summary>

- ពិនិត្យមើលថា `AZURE_OPENAI_DEPLOYMENT` ត្រូវនឹងឈ្មោះ deployment នៅក្នុង Azure
- ពិនិត្យមើលមូឌែលបានចេញផ្សាយ ហើយវាមានសកម្មភាព
- ឈ្មោះ deployment លំនាំដើមគឺ `gpt-5.6-luna`
</details>

<details>
<summary><strong>កំហុស: 429 / លើសកំណត់អត្រា</strong></summary>

- ការចេញផ្សាយ GPT-5.6 Luna លំនាំដើមមានសមត្ថភាពនេះ Global Standard capacity 10: 10 សំណើ/នាទី និង 10,000 token/នាទី
- រត់ឧទាហរណ៍តាមជួរ និងរង់ចាំរយៈពេល retry របស់សេវាកម្មមុនព្យាយាមម្តងទៀត
- ឧទាហរណ៍មូលដ្ឋាននេះបានបិទ retry SDK ដោយស្វ័យប្រវត្តិ ដូច្នេះសំណើដែលបរាជ័យនឹងត្រូវរាយការណ៍ផ្ទាល់
</details>

<details>
<summary><strong>VS Code៖ អថេរបរិយាកាសមិនបានផលិតឡើង</strong></summary>

- ប្រាកដថាឯកសារ `.env` របស់អ្នកនៅក្នុងថតឫសគម្រោង (មានកម្រិតដូចជា `pom.xml`)
- ព្យាយាមរត់ `mvn spring-boot:run` នៅក្នុង terminal រួមបញ្ចូលVS Code
- ពិនិត្យមើលថា ផ្នែកបន្ថែម Java នៅក្នុង VS Code ត្រូវបានដំឡើងត្រឹមត្រូវ
</details>

### ម៉ូដដ្បិតកំហុស

ដើម្បីបើក logging ពីរបៀបលម្អិត សូមដក comment បន្ទាត់ខាងក្រោមនៅក្នុង [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## ជំហានបន្ទាប់

**ការតំឡើងបានបញ្ចប់ហើយ!** សូមបន្តការសិក្សារបស់អ្នក៖

[ជំពូក 3៖ បច្ចេកទេស AI សំខាន់ៗ](../../../03-CoreGenerativeAITechniques/README.md)

## ធនធាន

- [ការប្តូរពី Spring AI 2 ទៅ OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Official OpenAI Java SDK ជាមួយ Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [ការផ្ទៀងផ្ទាត់អត្តសញ្ញាណគ្មានកូនសោ Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [ឯកសារប្រើប្រាស់ Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->