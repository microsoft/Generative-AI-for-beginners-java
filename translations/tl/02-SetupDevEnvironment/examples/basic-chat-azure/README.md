# Pangunahing Chat gamit ang Azure AI Foundry - End-to-End na Halimbawa

Ang halimbawang ito ay isang simpleng Spring Boot application na kumokonekta sa isang **Azure AI Foundry** na modelo gamit ang **keyless authentication** (Microsoft Entra ID) at sinusubukan ang iyong setup. Ginagamit nito ang Spring AI `ChatClient`, na suportado ng **opisyal na OpenAI Java SDK** at ang **Azure OpenAI v1** endpoint.

Ang mga bersyon sa [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) ay Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, at dotenv-java **3.2.0**. Ang sample ay gumagamit ng `spring-ai-starter-model-openai` at tahasang dinideklara ang `openai-java` at `azure-identity`; inalis ng Spring AI 2 ang lumang Azure OpenAI starter.

## Talaan ng Nilalaman

- [Mga Kinakailangan](#mga-kinakailangan)
- [Mabilis na Simula](#mabilis-na-simula)
- [Paano Gumagana ang Authentication](#paano-gumagana-ang-authentication)
- [Pagpapatakbo ng Application](#pagpapatakbo-ng-application)
  - [Paggamit ng Maven](#paggamit-ng-maven)
  - [Paggamit ng VS Code](#paggamit-ng-vs-code)
  - [Inaasahang Output](#inaasahang-output)
- [Sanggunian sa Configuration](#sanggunian-sa-configuration)
  - [Mga Environment Variable](#mga-environment-variable)
  - [Spring Configuration](#spring-configuration)
- [Pag-troubleshoot](#pag-troubleshoot)
  - [Karaniwang Isyu](#karaniwang-isyu)
  - [Debug Mode](#debug-mode)
- [Mga Susunod na Hakbang](#mga-susunod-na-hakbang)
- [Mga Resources](#mga-resources)

## Mga Kinakailangan

Bago patakbuhin ang halimbawang ito, tiyakin na mayroon kang:

- Isang Azure AI Foundry resource na may `gpt-5.6-luna` deployment - iprovide ito gamit ang `azd up` o manu-mano sa pamamagitan ng [Azure AI Foundry setup guide](../../getting-started-azure-openai.md)
- Ang **Cognitive Services OpenAI User** na papel sa resource na iyon (itinakda ng Bicep templates para sa iyo)
- Ang [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), naka-sign in gamit ang `az login`
- Java 21+ at Maven 3.9+

> **Hindi Kailangan ng API key** — keyless ang authentication sa pamamagitan ng Microsoft Entra ID.

## Mabilis na Simula

```bash
# 1. Pumunta sa proyekto
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Mag-sign in upang makakuha ng token ang keyless auth
az login

# 3. I-configure ang endpoint
#    - Kung nagpatakbo ka ng `azd up`, naisulat na ang .env para sa iyo (laktawan ito).
#    - Kung hindi, kopyahin ang template at itakda ang AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Patakbuhin ang aplikasyon
mvn spring-boot:run
```

## Paano Gumagana ang Authentication

Ang halimbawang ito ay nag-a-authenticate gamit ang **Microsoft Entra ID** — walang API key.

Ang application ay tahasang nagse-set up ng authentication sa [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. Gumagawa ang `azureCredential()` ng `BearerTokenCredential` gamit ang `AuthenticationUtil.getBearerTokenSupplier` gamit ang `DefaultAzureCredential` at ang `https://ai.azure.com/.default` na saklaw.
2. Ang `azureOpenAiClient()` ay bumubuo ng isang `OpenAIClient` gamit ang `OpenAIOkHttpClient.builder()`, tinutukoy ang resource endpoint sa `/openai/v1`, at nagbibigay ng bearer credential gamit ang `.credential(...)`.
3. Ang `azureChatModel()` ay nagbibigay ng kliyenteng iyon sa Spring AI `OpenAiChatModel`, na sumusuporta sa `ChatClient` ng aralin.

Pinipigilan ng mga tahasang mga bean na ito ang global na `OPENAI_API_KEY` mula sa pag-ooverride ng Azure authentication. Ang pag-alis ng API key mula sa YAML lamang ay hindi setup ng authentication. Ang `DefaultAzureCredential` ay maaaring gumamit ng iyong `az login` session nang lokal o ng managed identity sa Azure; alin man ang mapili ay dapat may papel na nakalista sa resource.

## Pagpapatakbo ng Application

### Paggamit ng Maven

```bash
mvn spring-boot:run
```

### Paggamit ng VS Code

1. Buksan ang proyekto sa VS Code
2. Pindutin ang `F5` o gamitin ang "Run and Debug" panel
3. Piliin ang "Spring Boot-BasicChatApplication" na configuration

> **Tandaan**: Naglo-load ang application ng `.env` mula sa working directory nito, kabilang kapag inilunsad mula sa VS Code.

### Inaasahang Output

Ilustratibong output pagkatapos ng matagumpay na pagpapatakbo (inalis ang startup logs; nagiiba ang mga salita ng sagot):

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

## Sanggunian sa Configuration

### Mga Environment Variable

| Variable | Paglalarawan | Kinakailangan | Halimbawa |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endpoint URL | Oo | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Pangalan ng chat model deployment | Hindi | `gpt-5.6-luna` (default) |

> Walang API key variable — keyless ang authentication (Microsoft Entra ID gamit ang `az login`).

### Spring Configuration

Ang mga setting sa [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ay gumagamit ng `spring.ai.openai` prefix at tinapyas na mga chat property (walang `options` block):

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

Ang `model` ay ang **Azure deployment name**. Nagmumula ang authentication sa tahasang mga bean na inilalarawan sa itaas, hindi sa isang `api-key` na setting. Pinapahinto ng aralin ang reasoning at nililimitahan ang completion tokens sa 500; hindi sinise-set ang `temperature` at ang legacy na `max-tokens`.

Inirerekomenda ng Microsoft ang [opisyal na OpenAI SDK gamit ang Azure OpenAI v1 at ang Responses API para sa mga bagong aplikasyon](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Ang Chat Completions ay nananatiling suportado para sa umiiral na lesson na batay sa mga mensahe. Para sa GPT-5.6, ang mga request na may kasamang tools sa Chat Completions ay dapat magtakda ng `reasoning_effort` sa `none`; gamitin ang Responses kapag pinagsasama ang reasoning at tools. Tingnan ang [tool calling gamit ang reasoning models](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Pag-troubleshoot

### Karaniwang Isyu

<details>
<summary><strong>Error: 401 / "PermissionDenied" / mga token error</strong></summary>

- Patakbuhin ang `az login` — kailangan ng keyless auth ng aktibong pag-sign in para makakuha ng token
- Siguraduhing may papel na **Cognitive Services OpenAI User** ang iyong account sa resource
- Kung kakatalaga mo lang ng papel, maghintay ng isang minuto upang maipamahagi ito
- Kumpirmahin na nasa tamang tenant/subscription ka (`az account show`)
</details>

<details>
<summary><strong>Error: "The endpoint is not valid" / mga problema sa koneksyon</strong></summary>

- Tiyakin na ang `AZURE_OPENAI_ENDPOINT` ay buong base URL (hal., `https://your-resource.openai.azure.com/`)
- Suriin ang consistency ng trailing slash
- Siguraduhing ang endpoint ay tumutugma sa iyong provisioned resource (`azd env get-values`)
</details>

<details>
<summary><strong>Error: "The deployment was not found"</strong></summary>

- Siguraduhing ang `AZURE_OPENAI_DEPLOYMENT` ay tumutugma sa pangalan ng deployment sa Azure
- Tiyakin na ang modelo ay matagumpay na na-deploy at aktibo
- Ang default na deployment name ay `gpt-5.6-luna`
</details>

<details>
<summary><strong>Error: 429 / nalampasan ang rate limit</strong></summary>

- Ang default GPT-5.6 Luna deployment ay may Global Standard capacity 10: 10 requests/bawat minuto at 10,000 tokens/bawat minuto
- Patakbuhin ang mga halimbawa nang sunud-sunod at maghintay para sa retry interval ng serbisyo bago ulitin
- Hindi pinapagana ng simpleng halimbawa na ito ang awtomatikong SDK retries, kaya direktang iniulat ang mga nabigong request
</details>

<details>
<summary><strong>VS Code: Hindi naglo-load ang environment variables</strong></summary>

- Siguraduhing ang `.env` file mo ay nasa root directory ng proyekto (kaparehong level ng `pom.xml`)
- Subukang patakbuhin ang `mvn spring-boot:run` sa integrated terminal ng VS Code
- Siguraduhing tama ang pag-install ng VS Code Java extension
</details>

### Debug Mode

Para paganahin ang detalyadong logging, i-uncomment ang mga linyang ito sa [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Mga Susunod na Hakbang

**Kumpleto na ang Setup!** Ipagpatuloy ang iyong pag-aaral:

[Kabanata 3: Mga Pangunahing Teknik sa Generative AI](../../../03-CoreGenerativeAITechniques/README.md)

## Mga Resources

- [Spring AI 2 OpenAI Java SDK transition](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Opisyal na OpenAI Java SDK gamit ang Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Keyless authentication gamit ang Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Dokumentasyon ng Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang maling pagkakaintindi o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->