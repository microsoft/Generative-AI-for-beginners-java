# ការតំឡើងបរិស្ថានអភិវឌ្ឍសម្រាប់ Generative AI សម្រាប់ Java

> **ចាប់ផ្តើមយ៉ាងរហ័ស៖** គ្រប់គ្រងម៉ូដែល AI របស់អ្នកនៅលើ **Azure AI Foundry** ជា code ជាមួយ Bicep + `azd` ក្នុងរយៈពេលតែប៉ុន្មាននាទី — សូមមើល [Azure AI Foundry Setup Guide](getting-started-azure-openai.md)។ ការផ្ទៀងផ្ទាត់គឺ **មិនប្រើ key** (Microsoft Entra ID) ដូច្នេះមិនមាន API key ត្រូវគ្រប់គ្រងទេ។

## អ្វីដែលអ្នកនឹងរៀន

- តំឡើងបរិស្ថានអភិវឌ្ឍ Java សម្រាប់កម្មវិធី AI
- ជ្រើសរើស និងកំណត់តម្លៃបរិស្ថានអភិវឌ្ឍដែលអ្នកចូលចិត្ត (cloud-first ជាមួយ Codespaces, dev container លើកុំព្យូទ័រផ្ទាល់, ឬការតំឡើងពេញលេញនៅលើមូលដ្ឋាន)
- សាកល្បងការតំឡើងដោយភ្ជាប់ទៅម៉ូដែល Azure AI Foundry

## មាតិកាទំព័រ

- [អ្វីដែលអ្នកនឹងរៀន](#អ្វីដែលអ្នកនឹងរៀន)
- [សេចក្តីផ្តើម](#សេចក្តីផ្តើម)
- [ជំហាន​ 1៖ តំឡើងបរិស្ថានអភិវឌ្ឍរបស់អ្នក](#ជំហាន-1៖-តំឡើងបរិស្ថានអភិវឌ្ឍរបស់អ្នក)
  - [ជម្រើស A៖ GitHub Codespaces (ផ្ដល់អនុសាសន៍)](#ជម្រើស-a៖-github-codespaces-ផ្ដល់អនុសាសន៍)
  - [ជម្រើស B៖ Local Dev Container](#ជម្រើស-b៖-local-dev-container)
  - [ជម្រើស C៖ ប្រើការតំឡើងមូលដ្ឋានដែលមានរបស់អ្នក](#ជម្រើស-c៖-ប្រើការតំឡើងនៅលើកុំព្យូទ័រដែលមានរបស់អ្នក)
- [ជំហាន 2៖ ការផ្គត់ផ្គង់ Azure AI Foundry](#ជំហាន-2៖-ការផ្គត់ផ្គង់-azure-ai-foundry)
- [ជំហាន 3៖ សាកល្បងការតំឡើងរបស់អ្នក](#ជំហាន-3៖-សាកល្បងការតំឡើងរបស់អ្នក)
- [ការជួសជុលបញ្ហា](#ការរកមើលបញ្ហា)
- [សេចក្ដីសន្និដ្ឋាន](#សេចក្ដីសន្និដ្ឋាន)
- [ជំហានបន្ទាប់](#ជំហានបន្ទាប់)

## សេចក្តីផ្តើម

ជំពូកនេះនឹងណែនាំអ្នកអំពីការតំឡើងបរិស្ថានអភិវឌ្ឍ។ យើងនឹងប្រើ **Azure AI Foundry** សម្រាប់ម៉ូដែលទាំងអស់នៅក្នុងវគ្គនេះ។ អ្នកផ្គត់ផ្គង់ម៉ូដែលជា code ជាមួយ Bicep និង Azure Developer CLI (`azd`), បន្ទាប់មកភ្ជាប់ដោយប្រើ **keyless authentication** (Microsoft Entra ID) — មិនចាំបាច់បញ្ចូល ឬ បែក API key ទេ។

**មិនចាំបាច់តំឡើងក្នុងកុំព្យូទ័រផ្ទាល់!** អ្នកអាចប្រើ GitHub Codespaces ដែលផ្តល់បរិស្ថានអភិវឌ្ឍពេញលេញនៅក្នុងកម្មវិធីរកមើលរបស់អ្នក ហើយផ្គត់ផ្គង់ Foundry ពីទីនោះ។

យើងប្រើ **Azure AI Foundry** សម្រាប់វគ្គនេះ ព្រោះវា:
- **ផ្គត់ផ្គង់ជា code** — តែ `azd up` តែមួយបញ្ចេញគណនី និងការផ្គត់ផ្គង់ម៉ូដែល
- **មិនប្រើ key** — ផ្ទៀងផ្ទាត់ដោយការចូលប្រើ Azure របស់អ្នក ឬ managed identity
- **រួចរាល់សម្រាប់ផលិតកម្ម** — code តែមួយដំណើរការបានទាំងក្នុងកុំព្យូទ័រផ្ទាល់ និងនៅ Azure
- **បត់បែនបាន** — ប្ដូរម៉ូដែលដោយផ្លាស់ប្តូរឈ្មោះ deployment មិនមែនកូដរបស់អ្នកទេ

> **ចំណាំ**: ការផ្គត់ផ្គង់ Azure AI Foundry ត្រូវបានគិតប្រាក់លើមួយ token (បង់តាមការប្រើប្រាស់)។ សូមមើល [Azure AI Foundry setup guide](getting-started-azure-openai.md) សម្រាប់ព័ត៌មានអំពីការផ្គត់ផ្គង់, តំបន់, និងការចំណាយ។


## ជំហាន 1៖ តំឡើងបរិស្ថានអភិវឌ្ឍរបស់អ្នក

<a name="quick-start-cloud"></a>

យើងបានបង្កើត dev container ដែលបានកំណត់រួចដើម្បីបន្ថយពេលវេលាតំឡើង និងធានាថាអ្នកមានឧបករណ៍ទាំងអស់ដែលចាំបាច់សម្រាប់វគ្គ Generative AI សម្រាប់ Java។ ជ្រើសរើសវិធីសាស្រ្តអភិវឌ្ឍដែលអ្នកចូលចិត្ត៖

### ជម្រើសសម្រាប់ការតំឡើងបរិស្ថាន:

#### ជម្រើស A៖ GitHub Codespaces (ផ្ដល់អនុសាសន៍)

**ចាប់ផ្តើមកូដនៅក្នុងរយៈពេល 2 នាទី - មិនចាំបាច់តំឡើងក្នុងកុំព្យូទ័រផ្ទាល់!**

1. Fork repository នេះទៅគណនី GitHub របស់អ្នក
   > **ចំណាំ**: ប្រសិនបើអ្នកចង់កែប្រែកំណត់តម្លៃមូលដ្ឋាន សូមមើល [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. ចុច **Code** → ផ្ទាំង **Codespaces** → **...** → **New with options...**
3. ប្រើការកំណត់លំនាំដើម - វានឹងជ្រើសរើស **Dev container configuration**: **Generative AI Java Development Environment** devcontainer ដែលបង្កើតសម្រាប់វគ្គនេះ
4. ចុច **Create codespace**
5. រង់ចាំប្រមាណ ~2 នាទីរហូតដល់បរិស្ថានត្រៀមរួច
6. បន្តទៅ [ជំហាន 2៖ ការផ្គត់ផ្គង់ Azure AI Foundry](#ជំហាន-2៖-ការផ្គត់ផ្គង់-azure-ai-foundry)

<img src="../../../translated_images/km/codespaces.9945ded8ceb431a5.webp" alt="Screenshot: Codespaces submenu" width="50%">

<img src="../../../translated_images/km/image.833552b62eee7766.webp" alt="Screenshot: New with options" width="50%">

<img src="../../../translated_images/km/codespaces-create.b44a36f728660ab7.webp" alt="Screenshot: Create codespace options" width="50%">


> **អត្ថប្រយោជន៍របស់ Codespaces**:
> - មិនចាំបាច់ដំឡើងក្នុងកុំព្យូទ័រផ្ទាល់
> - ប្រើបានលើឧបករណ៍ណាមួយដែលមានកម្មវិធីរកមើល
> - តំឡើងរួចជាមួយឧបករណ៍ និងចំណុចពាក់ព័ន្ធទាំងអស់
> - មាន 60 ម៉ោងមិនគិតថ្លៃក្នុងមួយខែសម្រាប់គណនីផ្ទាល់ខ្លួន
> - បរិស្ថានជាកម្រិតលម្អិតសម្រាប់អ្នករៀនទាំងអស់

#### ជម្រើស B៖ Local Dev Container

**សម្រាប់អ្នកអភិវឌ្ឍដែលចូលចិត្តអភិវឌ្ឍនៅលើកុំព្យូទ័រផ្ទាល់ជាមួយ Docker**

1. Fork និង clone repository នេះទៅកុំព្យូទ័រផ្ទាល់អ្នក
   > **ចំណាំ**: ប្រសិនបើអ្នកចង់កែប្រែកំណត់តម្លៃមូលដ្ឋាន សូមមើល [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. ដំឡើង [Docker Desktop](https://www.docker.com/products/docker-desktop/) និង [VS Code](https://code.visualstudio.com/)
3. ដំឡើងបន្ថែម [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) នៅក្នុង VS Code
4. បើកថត repository នៅក្នុង VS Code
5. ពេលមានការជំរើស ចុច **Reopen in Container** (ឬប្រើ `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. រង់ចាំ container ត្រូវបានសាង និងចាប់ផ្តើម
7. បន្តទៅ [ជំហាន 2៖ ការផ្គត់ផ្គង់ Azure AI Foundry](#ជំហាន-2៖-ការផ្គត់ផ្គង់-azure-ai-foundry)

<img src="../../../translated_images/km/devcontainer.21126c9d6de64494.webp" alt="Screenshot: Dev container setup" width="50%">

<img src="../../../translated_images/km/image-3.bf93d533bbc84268.webp" alt="Screenshot: Dev container build complete" width="50%">

#### ជម្រើស C៖ ប្រើការតំឡើងនៅលើកុំព្យូទ័រដែលមានរបស់អ្នក

**សម្រាប់អ្នកអភិវឌ្ឍដែលមានបរិស្ថាន Java មានស្រាប់**

លក្ខខណ្ឌជាមុន:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ឬ IDE ដែលអ្នកចូលចិត្ត

ជំហាន៖
1. Clone repository នេះទៅកុំព្យូទ័រផ្ទាល់អ្នក
2. បើកគម្រោងនៅក្នុង IDE របស់អ្នក
3. បន្តទៅ [ជំហាន 2៖ ការផ្គត់ផ្គង់ Azure AI Foundry](#ជំហាន-2៖-ការផ្គត់ផ្គង់-azure-ai-foundry)

> **ប្រយោជន៍ឯកទេស**: ប្រសិនបើម៉ាស៊ីនរបស់អ្នកមានសមត្ថភាពទាប ប៉ុន្តាចង់បាន VS Code នៅលើកុំព្យូទ័រផ្ទាល់។ សូមប្រើ GitHub Codespaces! អ្នកអាចភ្ជាប់ VS Code ផ្ទាល់របស់អ្នកទៅ Codespace ដែលផ្អែកលើពពកសម្រាប់ភាពល្អបំផុតទាំងពីរ។

<img src="../../../translated_images/km/image-2.fc0da29a6e4d2aff.webp" alt="Screenshot: created local devcontainer instance" width="50%">


## ជំហាន 2៖ ការផ្គត់ផ្គង់ Azure AI Foundry

ផ្គត់ផ្គង់ម៉ូដែល AI របស់វគ្គទៅ Azure AI Foundry ជា code។ ចាប់ផ្ដើមពី root repository៖

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` នឹងសួរឈ្មោះបរិស្ថាន, subscription, និងតំបន់, ផ្គត់ផ្គង់គណនី Azure AI Foundry ជាមួយ deployment `gpt-5.6-luna` និង `text-embedding-3-small`, ហើយសរសេរទីតាំង endpoint ចូលទៅក្នុងឯកសារ .env របស់ឧទាហរណ៍ - ទាំងអស់នេះជាមួយ **keyless** authentication (គ្មាន API key)។

> **មើលការណែនាំពេញលេញ:** សូមមើល [Azure AI Foundry Setup Guide](getting-started-azure-openai.md) សម្រាប់លក្ខខណ្ឌមុនផ្គត់ផ្គង់, ជម្រើសដៃគូ (portal), មូលដ្ឋានតំបន់ និងកំណត់ចំណាយ/ការសម្អាត។

## ជំហាន 3៖ សាកល្បងការតំឡើងរបស់អ្នក

ពេលម៉ូដែល Foundry របស់អ្នកត្រូវបានផ្គត់ផ្គង់រួច សាកល្បងការតភ្ជាប់ជាមួយកម្មវិធីឧទាហរណ៍នៅក្នុង [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure)។

1. បើក terminal ក្នុងបរិស្ថានអភិវឌ្ឍរបស់អ្នក
2. ទៅកាន់ឧទាហរណ៍៖
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. ​ប្រាកដថាអ្នកបានចូល (keyless auth តម្រូវ token):
   ```bash
   az login
   ```
   > ប្រសិនបើអ្នកបានដំណើរការ `azd up` អ្នកបានសរសេរឯកសារ `.env` ជាមួយ endpoint រួចហើយ។
4. រត់កម្មវិធី៖
   ```bash
   mvn clean spring-boot:run
   ```

អ្នកគួរតែឃើញការឆ្លើយតបពីម៉ូដែល `gpt-5.6-luna`។

### យល់ដឹងអំពីកូដឧទាហរណ៍

[ឧទាហរណ៍ basic-chat](./examples/basic-chat-azure/README.md) ប្រើ **Spring Boot 4.1.1** និង **Spring AI 2.0.1**។ Spring AI របស់ `ChatClient` គាំទ្រដោយ official OpenAI Java SDK, ភ្ជាប់ទៅ Azure OpenAI **v1** endpoint ជាមួយ keyless authentication។

**កូដនេះបញ្ចេញអ្វីខ្លះ៖**
- **ភ្ជាប់** ទៅ Azure AI Foundry ដោយប្រើ Azure sign-in របស់អ្នក (Microsoft Entra ID) — គ្មាន API key
- **ផ្ញើ** prompt ទៅម៉ូដែល `gpt-5.6-luna`
- **ទទួល** និងបង្ហាញចម្លើយពី AI
- **ផ្ទៀងផ្ទាត់** ថាដំណើរការរបស់អ្នកបានត្រឹមត្រូវ

**ការពឹងផ្អែកសំខាន់ៗ** (ចម្រាញ់ពី [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)):
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
</dependency>
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>${azure-identity.version}</version>
</dependency>
```

POM គ្រប់គ្រង OpenAI Java **4.63.1** និងកំណត់ Azure Identity **1.18.6** ជាក់លាក់។ Spring AI 2 បានដកស្តាតថ៍ Azure ដាច់; តែនៅតែត្រូវការដើម្បី credential bean។

**កំណត់បញ្ជា** ([application.yml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)):
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

Keyless auth ត្រូវបានកំណត់យ៉ាងច្បាស់នៅ [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java), មិនបានដកស្រង់ពី API key ដែលអស់សុពលភាព។ ដំណើរការចុះបញ្ជីរបស់វាប្រើ `DefaultAzureCredential` ជាមួយទំនង `https://ai.azure.com/.default`, ហើយ `OpenAIClient` ចេញបញ្ជានៅ `/openai/v1`។ កម្មវិធីផ្គត់ផ្គង់ client នោះទៅម៉ូដែល chat របស់ Spring AI, ដូច្នេះ `OPENAI_API_KEY` ទូទាំងប្រព័ន្ធមិនអាចបម្លែង authentication របស់ Azure បាន។

ការកំណត់ chat ស្ថិតក្រោម `spring.ai.openai.chat` ដោយផ្ទាល់, គ្មាន ប្លុក `options`។ មេរៀនរក្សា Chat Completions ជាមួយ `reasoning-effort: none` និងកំណត់កំណត់ទស្សន៍ 500-token completion; មិនកំណត់ `temperature` ឬ `max-tokens` ទេ។ សូមមើល [ឧទាហរណ៍នៃការកំណត់ configuration reference](./examples/basic-chat-azure/README.md#spring-configuration) សម្រាប់ជម្រេច API និងការណែនាំអំពីការហៅឧបករណ៍។

## សេចក្ដីសន្និដ្ឋាន

បន្ទាប់ពីបញ្ចប់ជំហានខាងលើ អ្នកនឹងមាន ៖

- ផ្គត់ផ្គង់ម៉ូដែល Azure AI Foundry ជា code ជាមួយ Bicep + `azd`
- មានបរិស្ថានអភិវឌ្ឍ Java របស់អ្នកដំណើរការ (គWhether្រប់ Codespaces, dev containers, ឬលើកុំព្យូទ័រផ្ទាល់)
- ភ្ជាប់ទៅ Azure AI Foundry ជាមួយ keyless authentication (Microsoft Entra ID) — មិនមាន API keys
- សាកល្បងថាអ្វីកំពុងដំណើរការ​បានជាមួយឧទាហរណ៍សាមញ្ញដែលនិយាយទៅម៉ូដែលរបស់អ្នក

## ជំហានបន្ទាប់

[ជំពូក 3៖ បច្ចេកទេស Generative AI ស្នូល](../03-CoreGenerativeAITechniques/README.md)

## ការរកមើលបញ្ហា

មានបញ្ហាទេ? នេះគឺជាបញ្ហាទូទៅ និងដំណោះស្រាយ៖

- **ការផ្ទៀងផ្ទាត់បរាជ័យ (401/403)?** 
  - រត់ `az login` — authentication គ្មាន key ដូច្នេះត្រូវតែចូលគណនី
  - ពិនិត្យមើលថាគណនីរបស់អ្នកមានតួនាទី **Cognitive Services OpenAI User** លើ resource នោះ
  - ប្រសិនបើអ្នកទើបតែផ្គត់ផ្គង់ ចាំមួយនាទីដើម្បីឱ្យតួនាទីផ្ទេរទៅ

- **រកមិនឃើញ Maven?** 
  - ប្រើ dev containers/Codespaces, Maven គួរត្រូវបានដំឡើងរួច
  - សម្រាប់ការតំឡើងក្នុងកុំព្យូទ័រផ្ទាល់ សូមធានាថា Java 21+ និង Maven 3.9+ ត្រូវបានដំឡើង
  - ព្យាយាម `mvn --version` ដើម្បីផ្ទៀងផ្ទាត់កំណត់តម្លៃ

- **រកមិនឃើញ `azd` ឬ ពិសោធន៍ការផ្គត់ផ្គង់បរាជ័យ?** 
  - ដំឡើង [Azure Developer CLI](https://aka.ms/azure-dev/install) ហើយរត់ `azd auth login`
  - ជ្រើសតំបន់ដែលមាន `gpt-5.6-luna` និង `text-embedding-3-small` (ឧ. `eastus2`), មាន quota គ្រប់គ្រាន់សម្រាប់ subscription របស់អ្នក
  - សូមមើល [Azure AI Foundry setup guide](getting-started-azure-openai.md) សម្រាប់ព័ត៌មានលម្អិត

- **Dev container មិនចាប់ផ្តើម?** 
  - ធានាថា Docker Desktop កំពុងដំណើរការ (សម្រាប់អភិវឌ្ឍនៅលើកុំព្យូទ័រផ្ទាល់)
  - សាកល្បងសាង container ឡើងវិញ: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **កំហុសចំរាស់ application?**
  - ថតការងារត្រឹមត្រូវ៖ `02-SetupDevEnvironment/examples/basic-chat-azure`
  - ព្យាយាមសម្អាត និងសាងថ្មី: `mvn clean compile`

> **តម្រូវការជំនួយ?**: តើនៅសល់បញ្ហាដែរ? បើក issue នៅក្នុង repository ហើយយើងនឹងជួយអ្នក។

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->