# Kuweka Mazingira ya Maendeleo kwa Azure AI Foundry

> Mwongozo huu unaweka mifano ya **Azure AI Foundry** kwa programu za AI za Java katika kozi hii, ukitumia uthibitishaji wa **keyless** (Microsoft Entra ID) — hakuna funguo za API za kusimamia. Mpya kwenye zana? Anza na [mwongozo wa mazingira ya maendeleo](./README.md).

Mwongozo huu unaweka mifano ya **Azure AI Foundry** kwa programu za AI za Java katika kozi hii. Una njia mbili:

- **Chaguo A — Tayarisha na `azd` + Bicep (inapendekezwa):** amri moja hupeleka akaunti ya Foundry na mifano kama msimbo. Hakuna kubofya kwenye portal.
- **Chaguo B — Unda rasilimali kwa mkono** katika portal ya Azure AI Foundry.

Njia zote mbili hutumia **uthibitishaji wa keyless** (Microsoft Entra ID) — hakuna funguo za API za nakili au kuvuja.

## Jedwali la Maudhui

- [Kinachoumbwa](#kinachoumbwa)
- [Mahitaji ya Awali](#mahitaji-ya-awali)
- [Chaguo A: Tayarisha kwa azd + Bicep (Inapendekezwa)](#option-a-provision-with-azd--bicep-recommended)
- [Chaguo B: Unda Rasilimali kwa Mkono](#chaguo-b-unda-rasilimali-kwa-mkono)
- [Sanidi Mazingira Yako](#sanidi-mazingira-yako)
- [Jaribu Mipangilio Yako](#jaribu-mipangilio-yako)
- [Nini Kifuatayo?](#nini-kifuatayo)
- [Rasilimali](#rasilimali)
- [Rasilimali Zaidi](#rasilimali-zaidi)

## Kinachoumbwa

Violezo vya Bicep katika [`infra/`](../../../02-SetupDevEnvironment/infra) hutoa:

- Akaunti ya **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, aina `AIServices`) yenye mradi
- Usambazaji wa **mazungumzo** - GPT-5.6 Luna (`gpt-5.6-luna`), toleo `2026-07-09`, na uwezo wa `GlobalStandard` `10` (maombi 10 kwa dakika na tokeni 10,000 kwa dakika kwa mfano huu)
- Usambazaji wa **embedding** - `text-embedding-3-small`, toleo `1` (hutumika katika sura zinazofuata)
- Ugawaji wa jukumu la **keyless** (`Mtumiaji wa Cognitive Services OpenAI`) ili ujiingize kwa `az login` badala ya kusimamia funguo

## Mahitaji ya Awali

- [Usajili wa Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) na [Maven 3.9+](https://maven.apache.org/download.cgi)

## Chaguo A: Tayarisha kwa azd + Bicep (Inapendekezwa)

Kutoka kwenye jalada la `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Ingia (zana zote mbili)
azd auth login
az login

# Toa akaunti ya Foundry + uenezaji wa mifano
azd up
```

`azd` hutafta kwa **jina la mazingira** (kwa mfano `genai-java`), **usajili**, na **eneo**. Chagua usajili wako mwenyewe na eneo ambapo `gpt-5.6-luna` na `text-embedding-3-small` zinapatikana, kwa mfano `eastus2`. Thibitisha kuwa usajili una quota ya kutosha kwa mfano na aina ya usambazaji katika eneo hilo; upatikanaji na quota hubadilika kwa usajili.

Wakati maandalizi yanapokamilika, azd:

1. Hupeleka kila kitu kilichoainishwa katika [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Inaendesha hook ya baada ya maandalizi ambayo huandika [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) na jina la mwisho na majina ya usambazaji (hakuna siri).

> **Kipendekezo:** Endelea kurudia `azd up` wakati wowote kutekeleza mabadiliko. Endesha `azd down` kufuta kila kitu na kuacha gharama.

Ili kuona mipangilio iliyoandaliwa:

```bash
azd env get-values
```

Sasa ruka hadi [Jaribu Mipangilio Yako](#jaribu-mipangilio-yako).

## Chaguo B: Unda Rasilimali kwa Mkono

Unapendelea portal? Unda rasilimali kwa mkono:

1. Nenda kwenye [portal ya Azure AI Foundry](https://ai.azure.com/) na ingia.
2. **Tengeneza mradi** (hii pia huunda rasilimali ya AI Foundry). Mpe jina kama `GenAIJava`.
3. Katika mradi wako, fungua **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Sambaza **GPT-5.6 Luna** (jina la mfano na usambazaji `gpt-5.6-luna`, toleo `2026-07-09`) na uwezo wa **Global Standard** `10`. Rudia kwa **text-embedding-3-small**, toleo `1`, ikiwa unataka mifano ya embedding.
5. Kutoka **Overview**, nakili **endpoint** (kwa mfano `https://<resource>.openai.azure.com/`).
6. Jipe upatikanaji wa keyless: kwenye rasilimali, fungua **Access control (IAM)** → **Add role assignment** → paka **Cognitive Services OpenAI User** kwa akaunti yako.

> **Bado unapata shida?** Tazama [nyaraka za Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Sanidi Mazingira Yako

**Ukimalizia kwa Chaguo A (`azd up`)**, faili yako ya mipangilio tayari imeandikwa — hakuna cha kusanidi. Ruka hadi [Jaribu Mipangilio Yako](#jaribu-mipangilio-yako).

**Ukimalizia kwa Chaguo B (kwa mkono)**, tengeneza faili `.env` ya mfano mwenyewe:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Hariri `.env` na endpoint yako (hakuna ufunguo — uthibitishaji ni wa keyless):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Tumia endpoint ya Azure OpenAI ya rasilimali, si URL ya mradi. Programu ya basic-chat huibadilisha hadi `/openai/v1` na kusanidi wateja kwa tokeni ya wazi; funguo ya API haihitajiki.

> **Kumbusho la usalama:** Hakuna ufunguo wa API wa kuhifadhi. Unathibitisha kwa Microsoft Entra ID kupitia `az login` (eneo la kazi) au kitambulisho kinachosimamiwa (Azure). Faili `.env` lina mipangilio isiyo ya siri na tayari linafunikwa na `.gitignore`.

## Jaribu Mipangilio Yako

Hakikisha umeingia ili uthibitishaji wa keyless upate tokeni, kisha endesha mfano:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # kama bado hujajiandikisha
mvn clean spring-boot:run
```

Unapaswa kuona jibu kutoka kwa mfano `gpt-5.6-luna`. Endesha mifano mfululizo ili kubaki ndani ya quota ndogo ya msingi; ikiwa unapata HTTP 429, subiri muda wa jaribio tena kabla ya kujaribu tena.

> **Watumiaji wa VS Code:** Bonyeza `F5` kuendesha. Programu inaingiza `.env` yako moja kwa moja.

> **Mfano kamili:** Tazama [Mfano wa Mazungumzo ya Msingi na Azure AI Foundry](./examples/basic-chat-azure/README.md) kwa maelezo na utatuzi wa matatizo.

## Nini Kifuatayo?

Baada ya maandalizi na kuendesha mfano kwa mafanikio, utakuwa na:
- Azure AI Foundry yenye `gpt-5.6-luna` na `text-embedding-3-small` iliyosambazwa
- Uthibitishaji wa keyless (Microsoft Entra ID) — hakuna funguo za kusimamia
- `.env` ya ndani yenye endpoint na majina ya usambazaji
- Mazingira ya maendeleo ya Java yakiwa tayari

**Endelea kwenye** [Sura ya 3: Mbinu Kuu za AI za Kizazi](../03-CoreGenerativeAITechniques/README.md) kuanza kujenga programu za AI!

## Rasilimali

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Uthibitishaji wa keyless na Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Nyaraka za Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Mpito wa Spring AI 2 kwenda OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Rasmi ya OpenAI Java na Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Rasilimali Zaidi

- [Pakua VS Code](https://code.visualstudio.com/Download)
- [Pata Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Usanidi wa Kontena la Maendeleo](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kionyozo**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati ya asili katika lugha yake halisi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatutojibu kwa kuelewa vibaya au tafsiri potofu zinazotokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->