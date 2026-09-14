# Mazungumzo ya Msingi na Azure AI Foundry - Mfano Kamili

Mfano huu ni programu rahisi ya Spring Boot inayounganisha na mfano wa **Azure AI Foundry** kwa kutumia **uthibitishaji bila funguo** (Microsoft Entra ID) na kujaribu usanidi wako. Inatumia `ChatClient` ya Spring AI, ikiwa na usaidizi wa **SDK rasmi ya OpenAI ya Java** na kiungo cha **Azure OpenAI v1**.

Toleo kwenye [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) ni Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, na dotenv-java **3.2.0**. Mfano huu hutumia `spring-ai-starter-model-openai` na hujaza wazi `openai-java` na `azure-identity`; Spring AI 2 iliondoa starter ya zamani ya Azure OpenAI.

## Jedwali la Maudhui

- [Mahitaji ya Awali](#mahitaji-ya-awali)
- [Anza Haraka](#anza-haraka)
- [Jinsi Uthibitishaji Unavyofanya Kazi](#jinsi-uthibitishaji-unavyofanya-kazi)
- [Kuendesha Programu](#kuendesha-programu)
  - [Kutumia Maven](#kutumia-maven)
  - [Kutumia VS Code](#kutumia-vs-code)
  - [Matokeo Yanayotarajiwa](#matokeo-yanayotarajiwa)
- [Marejeleo ya Usanidi](#marejeleo-ya-usanidi)
  - [Mazingira ya Mabadiliko](#mazingira-ya-mabadiliko)
  - [Usanidi wa Spring](#usanidi-wa-spring)
- [Kutatua Matatizo](#kutatua-matatizo)
  - [Matatizo Yanayojirudia Mara kwa Mara](#matatizo-yanayojirudiwa-mara-kwa-mara)
  - [Hali ya Ujibu wa Hitilafu](#hali-ya-ujibu-wa-hitilafu)
- [Hatua Zifuatazo](#hatua-zifuatazo)
- [Rasilimali](#rasilimali)

## Mahitaji ya Awali

Kabla ya kuendesha mfano huu, hakikisha una:

- Rasilimali ya Azure AI Foundry yenye matumizi ya `gpt-5.6-luna` - kupelekwa kwa kutumia `azd up` au kwa mkono kupitia [mwongozo wa usanidi wa Azure AI Foundry](../../getting-started-azure-openai.md)
- Nafasi ya mtumiaji wa **Cognitive Services OpenAI User** kwenye rasilimali hiyo (templates za Bicep hujaza hii kwaautomatically)
- [CLI ya Azure (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), umeingia kwa kutumia `az login`
- Java 21+ na Maven 3.9+

> **Hapana ufunguo wa API unaohitajika** — uthibitishaji ni bila funguo kupitia Microsoft Entra ID.

## Anza Haraka

```bash
# 1. Elekea kwenye mradi
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Ingia ili uthibitishaji usiotumia funguo upate tokeni
az login

# 3. Sanidi kiungo cha mwisho
#    - Ikiwa ulitumia `azd up`, .env imeandikwa kwa ajili yako (ruka hii).
#    - Vinginevyo nakili kiolezo na weka AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Endesha programu
mvn spring-boot:run
```

## Jinsi Uthibitishaji Unavyofanya Kazi

Mfano huu unathibitisha kwa kutumia **Microsoft Entra ID** — hakuna ufunguo wa API.

Programu huweka wazi uthibitisho katika [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` huunda `BearerTokenCredential` kwa kutumia `AuthenticationUtil.getBearerTokenSupplier` na `DefaultAzureCredential` pamoja na upeo wa `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` hujenga `OpenAIClient` kwa kutumia `OpenAIOkHttpClient.builder()`, inatafsiri kiungo cha rasilimali hadi `/openai/v1`, na hutoa uthibitisho wa bearer kwa `.credential(...)`.
3. `azureChatModel()` hutoa mteja huyo kwa `OpenAiChatModel` ya Spring AI, inayosaidia `ChatClient` ya somo.

Viungo hivi wazi vinaweka `OPENAI_API_KEY` ya jumla isizuie uthibitishaji wa Azure. Kukosa ufunguo wa API kwenye YAML siyo usanidi wa uthibitishaji. `DefaultAzureCredential` inaweza kutumia kikao chako cha `az login` mahali ulipo au utambulisho wa kuendeshwa ndani ya Azure; utambulisho wowote unaochaguliwa lazima uwe na nafasi ya rasilimali iliyotajwa hapo juu.

## Kuendesha Programu

### Kutumia Maven

```bash
mvn spring-boot:run
```

### Kutumia VS Code

1. Fungua mradi katika VS Code
2. Bonyeza `F5` au tumia jopo la "Run and Debug"
3. Chagua usanidi wa "Spring Boot-BasicChatApplication"

> **Kumbuka**: Programu husoma `.env` kutoka saraka yake ya kazi, ikiwa ni pamoja na wakati wa kuzinduliwa kutoka VS Code.

### Matokeo Yanayotarajiwa

Matokeo yanayoonyesha baada ya kuendesha kwa mafanikio (kumbukumbu za kuanzisha zimetolewa; maneno ya majibu yanatofautiana):

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

## Marejeleo ya Usanidi

### Mazingira ya Mabadiliko

| Kigezo | Maelezo | Kinahitajika | Mfano |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Kiungo cha Foundry (Azure OpenAI) | Ndiyo | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Jina la usambazaji wa mfano wa mazungumzo | Hapana | `gpt-5.6-luna` (chaguo-msingi) |

> Hakuna kigezo cha ufunguo wa API — uthibitishaji ni bila funguo (Microsoft Entra ID kupitia `az login`).

### Usanidi wa Spring

Mipangilio ya [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) hutumia kiambishi `spring.ai.openai` na sifa za mazungumzo zisizo na ufananishi wa kikundi (`options`):

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

`model` ni jina la **usambazaji wa Azure**. Uthibitishaji unatoka kwenye viungo wazi vilivyotajwa hapo juu, si kwenye usanidi wa `api-key`. Somo linazima ufikivu wa hoja na linaweka viashiria vya kukamilika hadi 500; linaacha `temperature` na `max-tokens` za zamani kutosanifishwa.

Microsoft inapendekeza [SDK rasmi ya OpenAI na Azure OpenAI v1 pamoja na API ya Majibu kwa programu mpya](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions bado zinasaidiwa kwa somo hili linalotumia ujumbe. Kwa GPT-5.6, maombi yanayojumuisha zana kwenye Chat Completions lazima yaweke `reasoning_effort` kuwa `none`; tumia Majibu unapochanganya hoja na zana. Angalia [kuitisha zana na mfano wa hoja](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Kutatua Matatizo

### Matatizo Yanayojirudiwa Mara kwa Mara

<details>
<summary><strong>Kosa: 401 / "PermissionDenied" / makosa ya token</strong></summary>

- Endesha `az login` — uthibitishaji bila funguo unahitaji kuingia kuleta tokeni
- Thibitisha kuwa akaunti yako ina nafasi ya mtumiaji wa Cognitive Services OpenAI kwenye rasilimali
- Ikiwa umejitolea nafasi hiyo, subiri dakika moja hadi isambazwe
- Thibitisha uko kwenye mtenat/udhamini unaofaa (`az account show`)
</details>

<details>
<summary><strong>Kosa: "Kiungo si sahihi" / makosa ya muunganisho</strong></summary>

- Hakikisha `AZURE_OPENAI_ENDPOINT` ni URL kamili ya msingi (kwa mfano, `https://your-resource.openai.azure.com/`)
- Angalia usawa wa slash ya mwisho
- Thibitisha kiungo kinapatana na rasilimali uliyopewa (`azd env get-values`)
</details>

<details>
<summary><strong>Kosa: "Usambazaji haukupatikana"</strong></summary>

- Thibitisha `AZURE_OPENAI_DEPLOYMENT` la jina la usambazaji katika Azure
- Angalia kuwa mfano umewekwa na una nguvu
- Jina la usambazaji wa chaguo-msingi ni `gpt-5.6-luna`
</details>

<details>
<summary><strong>Kosa: 429 / kiwango cha maombi kimezidiwa</strong></summary>

- Usambazaji wa chaguo-msingi wa GPT-5.6 Luna una uwezo wa Global Standard 10: maombi 10 kwa dakika na tokeni 10,000 kwa dakika
- Endesha mifano kwa mpangilio na subiri kipindi cha huduma kurudia kabla ya jaribio jingine
- Mfano huu wa msingi unazima jaribio la SDK kiotomatiki, hivyo ombi lililoanguka linaripotiwa moja kwa moja
</details>

<details>
<summary><strong>VS Code: Mabadiliko ya mazingira hayapaki</strong></summary>

- Hakikisha faili yako `.env` iko katika saraka kuu ya mradi (ngazi sawa na `pom.xml`)
- Jaribu kuendesha `mvn spring-boot:run` katika terminal iliyojumuishwa ya VS Code
- Thibitisha ugani wa Java wa VS Code umewekwa vizuri
</details>

### Hali ya Ujibu wa Hitilafu

Ili kuwezesha kumbukumbu za kina, toa maoni kwenye mistari hii katika [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Hatua Zifuatazo

**Usanidi Umefanyika!** Endelea na safari yako ya kujifunza:

[Sura ya 3: Mbinu Msingi za Ufundi wa AI wa Kizazi](../../../03-CoreGenerativeAITechniques/README.md)

## Rasilimali

- [Mabadiliko ya Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Rasmi ya OpenAI ya Java na Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Uthibitishaji bila funguo na Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Nyaraka za Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kionyozo**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati ya asili katika lugha yake halisi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatutojibu kwa kuelewa vibaya au tafsiri potofu zinazotokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->