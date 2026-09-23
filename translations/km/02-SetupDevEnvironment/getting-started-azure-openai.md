# ការតម្លើងបរិយាកាសអភិវឌ្ឍសម្រាប់ Azure AI Foundry

> មគ្គុទេសក៍នេះតំឡើងម៉ូឌែល **Azure AI Foundry** សម្រាប់កម្មវិធី AI ជា Java ក្នុងវគ្គបណ្តុះបណ្តាលនេះ ដោយប្រើការផ្ទៀងផ្ទាត់សម្គាល់ **គ្មានគន្លឹះ** (Microsoft Entra ID) — មិនមានសោ API ត្រូវគ្រប់គ្រងទេ។ ថ្មីចំពោះឧបករណ៍នេះឬ? ចាប់ផ្តើមជាមួយ [មគ្គុទេសក៍បរិយាកាសអភិវឌ្ឍ](./README.md)។

មគ្គុទេសក៍នេះតំឡើងម៉ូឌែល **Azure AI Foundry** សម្រាប់កម្មវិធី AI ជា Java ក្នុងវគ្គនេះ។ អ្នកមានពីរផ្លូវ។

- **ជម្រើស A — ការផ្ដល់ជូនជាមួយ `azd` + Bicep (ណែនាំ):** ពាក្យបញ្ជាដែលតែប៉ុណ្ណោះបញ្ចេញគណនី Foundry និងម៉ូឌែលជាកូដ។ មិនចាំបាច់ចុចក្រឡាចុចបណ្តោះអាសន្ននោះទេ។
- **ជម្រើស B — បង្កើតធនធានដោយដៃ** នៅក្នុងផ្ទាំងគ្រប់គ្រង Azure AI Foundry។

ទាំងពីរផ្លូវប្រើ **ការផ្ទៀងផ្ទាត់គ្មានគន្លឹះ** (Microsoft Entra ID) — មិនមានសោ API ត្រូវចម្លងឬបំពានឡើយ។

## តារាងមាតិកា

- [អ្វីខ្លះដែលត្រូវបានបង្កើត](#អ្វីខ្លះដែលត្រូវបានបង្កើត)
- [លក្ខខ័ណ្ឌមុន](#លក្ខខ័ណ្ឌមុន)
- [ជម្រើស A: ការផ្ដល់ជូនជាមួយ azd + Bicep (ណែនាំ)](#option-a-provision-with-azd--bicep-recommended)
- [ជម្រើស B: បង្កើតធនធានដោយដៃ](#ជម្រើស-b-បង្កើតធនធានដោយដៃ)
- [កំណត់បរិយាកាសរបស់អ្នក](#កំណត់បរិយាកាសរបស់អ្នក)
- [សាកល្បងការតំឡើងរបស់អ្នក](#សាកល្បងការតំឡើងរបស់អ្នក)
- [តើបន្ទាប់មានអ្វី?](#តើបន្ទាប់មានអ្វី)
- [ធនធាន](#ធនធាន)
- [ធនធានបន្ថែម](#ធនធានបន្ថែម)

## អ្វីខ្លះដែលត្រូវបានបង្កើត

គំរូ Bicep នៅក្នុង [`infra/`](../../../02-SetupDevEnvironment/infra) ផ្ដល់ជូន:

- គណនី **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, ប្រភេទ `AIServices`) មានគម្រោងមួយ
- ការបង្ហោះប្រព័ន្ធ **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), កំណែ `2026-07-09`, មានសមត្ថភាព `GlobalStandard` `10` (10 សំណើ/នាទី និង 10,000 ទូម/នាទីសម្រាប់ម៉ូឌែលនេះ)
- ការបង្ហោះប្រព័ន្ធ **embedding** - `text-embedding-3-small`, កំណែ `1` (ប្រើនៅជំពូកក្រោយ)
- ការតែងតាំងតួនាទី **គ្មានគន្លឹះ** (`Cognitive Services OpenAI User`) ដូច្នេះអ្នកអាចចូលប្រើដោយ `az login` ជំនួសការគ្រប់គ្រងកូនសោ

## លក្ខខ័ណ្ឌមុន

- [ជាវ Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) និង [Maven 3.9+](https://maven.apache.org/download.cgi)

## ជម្រើស A: ការផ្ដល់ជូនជាមួយ azd + Bicep (ណែនាំ)

ពីថត `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# ចូល (ឧបករណ៍ទាំងពីរ)
azd auth login
az login

# ផ្តល់គណនី Foundry + ការដាក់ម៉ូដែល
azd up
```

`azd` នឹងស្នើឲ្យបញ្ចូល **ឈ្មោះបរិយាកាស** (ឧទាហរណ៍ `genai-java`), **ជាវ**, និង **តំបន់**។ ជ្រើសរើសជាវរបស់អ្នក និងតំបន់មួយដែលមាន `gpt-5.6-luna` និង `text-embedding-3-small` ដោយគំរូ `eastus2`។ បញ្ជាក់ Mojអំពីការជាសមរម្យនៃគោលដៅនិងបញ្ជូនម៉ូឌែលក្នុងតំបន់នោះ; ការចូលដំណើរការនិងគោលដៅប្រែប្រួលដោយជាវ។

នៅពេលការផ្ដល់ជូនបានបញ្ចប់ `azd`:

1. បង្ហោះអ្វីដែលបានកំណត់ក្នុង [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep)។
2. ប្រតិបត្តិការ hook បន្ទាប់ពីផ្ដល់ជូន ដែលសរសេរ [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) ជាមួយចំណុចបញ្ចប់ និងឈ្មោះការបង្ហោះរបស់អ្នក (មិនមានអាថ៌កំបាំង)។

> **គន្លឹះ៖** វែកវិញ `azd up` ក៏បានគ្រប់ពេល ដើម្បីអនុវត្តបម្លែង។ ប្រតិបត្តិ `azd down` ដើម្បីលុបអ្វីគ្រប់យ៉ាង និងបញ្ឈប់ការចំណាយ។

ដើម្បីមើលការកំណត់ដែលបានបង្កើត:

```bash
azd env get-values
```

ឥឡូវរំលងទៅ [សាកល្បងការតំឡើងរបស់អ្នក](#សាកល្បងការតំឡើងរបស់អ្នក)។

## ជម្រើស B: បង្កើតធនធានដោយដៃ

ចូលចិត្តផ្ទាំងគ្រប់គ្រងឬ? បង្កើតធនធានដោយដៃ៖

1. ទៅកាន់ [ផ្ទាំងគ្រប់គ្រង Azure AI Foundry](https://ai.azure.com/) ហើយចូលប្រើ។
2. **បង្កើតគម្រោង** (នេះក៏បង្កើតធនធាន AI Foundry ផងដែរ) ។ ផ្ដល់ឈ្មោះដូចជា `GenAIJava`។
3. ក្នុងគម្រោងរបស់អ្នក បើក **Models + endpoints** → **Deploy model** → **Deploy base model**។
4. បង្ហោះ **GPT-5.6 Luna** (ឈ្មោះម៉ូឌែល និងការបង្ហោះ `gpt-5.6-luna`, កំណែ `2026-07-09`) ជាមួយសមត្ថភាព **Global Standard** `10`។ ចម្លងសម្រាប់ **text-embedding-3-small**, កំណែ `1`, ប្រសិនបើអ្នកចង់មានឧទាហរណ៍ embedding។
5. ពី **ទិដ្ឋភាពទូទៅ**, ចម្លង **ចំណុចបញ្ចប់** (ឧទាហរណ៍ `https://<resource>.openai.azure.com/`)។
6. ផ្ដល់សិទ្ធិឲ្យខ្លួនឯងចូលដោយគ្មានគន្លឹះ៖ នៅលើធនធាន បើក **Access control (IAM)** → **Add role assignment** → បញ្ជាក់ **Cognitive Services OpenAI User** ទៅគណនីរបស់អ្នក។

> **នៅតែមានបញ្ហា?** មើល [ឯកសារ Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)។

## កំណត់បរិយាកាសរបស់អ្នក

**ប្រសិនបើអ្នកប្រើជម្រើស A (`azd up`)**, ឯកសារកំណត់របស់អ្នកបានសរសេរហើយ — មិនចាំបាច់កំណត់បន្ថែមទេ។ ផ្ទៀងផ្ទាត់ទៅ [សាកល្បងការតំឡើងរបស់អ្នក](#សាកល្បងការតំឡើងរបស់អ្នក)។

**ប្រសិនបើអ្នកប្រើជម្រើស B (ដោយដៃ)**, បង្កើតឯកសារ `.env` របស់ឧទាហរណ៍ដោយខ្លួនឯង៖

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

កែប្រែ `.env` ជាមួយចំណុចបញ្ចប់របស់អ្នក (គ្មានសោ — ការផ្ទៀងផ្ទាត់គ្មានគន្លឹះ)៖

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

ប្រើចំណុចបញ្ចប់ Azure OpenAI របស់ធនធាន មិនមែន URL គម្រោងទេ។ កម្មវិធី basic-chat បម្រុងបំភ្លឺទៅ `/openai/v1` ហើយកំណត់ client bearer-token ខាងច្បាស់; នឹងមិនទាមទារកូនសោ API ទេ។

> **ចំណាំសុវត្ថិភាព:** មិនមានសោ API ត្រូវរក្សា។ អ្នកផ្ទៀងផ្ទាត់តាម Microsoft Entra ID តាម `az login` (នៅលើកុំព្យូទ័រផ្ទាល់) ឬតាម managed identity (នៅក្នុង Azure)។ ឯកសារ `.env` រក្សាទុកតែការកំណត់មិនមែនអាថ៌កំបាំង និងបានគ្របដណ្តប់រួចដោយ `.gitignore`។

## សាកល្បងការតំឡើងរបស់អ្នក

ធានាថាអ្នកបានចូលប្រើ ដោយសារការផ្ទៀងផ្ទាត់គ្មានគន្លឹះអាចទទួលបាន Token បន្ទាប់មករត់ឧទាហរណ៍:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ប្រសិនបើអ្នកមិនទាន់ចូលឆ្មាសនៅឡើយទេ
mvn clean spring-boot:run
```

អ្នកគួរមើលឃើញការឆ្លើយតបពីម៉ូឌែល `gpt-5.6-luna`។ រត់ឧទាហរណ៍ជាលំដាប់ដើម្បីនៅក្នុងគោលដៅតូចលំនាំដើម; ប្រសិនបើទទួលបាន HTTP 429, រង់ចាំរយៈពេល retry មុនព្យាយាមម្តងទៀត។

> **អ្នកប្រើ VS Code:** ចុច `F5` ដើម្បីរត់។ កម្មវិធីទាញយក `.env` របស់អ្នកដោយស្វ័យប្រវត្តិ។

> **ឧទាហរណ៍ពេញលេញ:** មើល [ឧទាហរណ៍ Basic Chat ជាមួយ Azure AI Foundry](./examples/basic-chat-azure/README.md) សម្រាប់ព័ត៌មានលម្អិត និងជំនួយ។

## តើបន្ទាប់មានអ្វី?

បន្ទាប់ពីបានផ្ដល់ជូន និងរត់ឧទាហរណ៍ដោយជោគជ័យ អ្នកនឹងមាន៖
- Azure AI Foundry ដែលបានបង្ហោះ `gpt-5.6-luna` និង `text-embedding-3-small`
- ការផ្ទៀងផ្ទាត់គ្មានគន្លឹះ (Microsoft Entra ID) — មិនមានសោដែលត្រូវគ្រប់គ្រង
- ឯកសារ `.env` ក្នុងគណនីរបស់អ្នកជាមួយចំណុចបញ្ចប់ និងឈ្មោះការបង្ហោះ
- បរិយាកាសអភិវឌ្ឍ Java ដែលបានរួចរាល់

**បន្តទៅ** [ជំពូក 3៖ နរូលជាតិភាសាជាកណ្តាលនៃ AI](../03-CoreGenerativeAITechniques/README.md) ដើម្បីចាប់ផ្តើមបង្កើតកម្មវិធី AI!

## ធនធាន

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [ការផ្ទៀងផ្ទាត់គ្មានគន្លឹះជាមួយ Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [ឯកសារ Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [ការផ្លាស់ប្តូរ Spring AI 2 ទៅ OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [OpenAI Java SDK ផ្លូវការជាមួយ Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## ធនធានបន្ថែម

- [ទាញយក VS Code](https://code.visualstudio.com/Download)
- [ទទួលបាន Docker Desktop](https://www.docker.com/products/docker-desktop)
- [ការកំណត់ Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->