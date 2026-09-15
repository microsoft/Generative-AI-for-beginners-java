# Pagrindinis pokalbis su Azure AI Foundry – pilnas pavyzdys nuo pradžios iki pabaigos

Šis pavyzdys yra paprasta Spring Boot programa, kuri jungiasi prie **Azure AI Foundry** modelio, naudodama **autentifikaciją be rakto** (Microsoft Entra ID), ir tikrina jūsų nustatymus. Ji naudoja Spring AI `ChatClient`, pagrįstą **oficialiu OpenAI Java SDK** ir **Azure OpenAI v1** galutiniu tašku.

Versijos [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) faile yra Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** ir dotenv-java **3.2.0**. Pavyzdyje naudojamas `spring-ai-starter-model-openai` ir aiškiai deklaruojami `openai-java` bei `azure-identity`; Spring AI 2 pašalino seną Azure OpenAI starterį.

## Turinys

- [Prieš sąlygos](#prieš-sąlygos)
- [Greitas startas](#greitas-startas)
- [Kaip veikia autentifikacija](#kaip-veikia-autentifikacija)
- [Programos vykdymas](#programos-paleidimas)
  - [Naudojant Maven](#naudojant-maven)
  - [Naudojant VS Code](#naudojant-vs-code)
  - [Laukiamas išėjimas](#laukiamas-išėjimas)
- [Konfigūracijos nuoroda](#konfigūracijos-nuoroda)
  - [Aplinkos kintamieji](#aplinkos-kintamieji)
  - [Spring konfigūracija](#spring-konfigūracija)
- [Trikčių šalinimas](#trikčių-šalinimas)
  - [Dažnos problemos](#dažnos-problemos)
  - [Derinimo režimas](#derinimo-režimas)
- [Kiti žingsniai](#kiti-žingsniai)
- [Ištekliai](#ištekliai)

## Prieš sąlygos

Prieš paleisdami šį pavyzdį, įsitikinkite, kad turite:

- Azure AI Foundry išteklių su `gpt-5.6-luna` diegimu - paruoškite jį naudodami `azd up` arba rankiniu būdu pagal [Azure AI Foundry diegimo vadovą](../../getting-started-azure-openai.md)
- **Cognitive Services OpenAI User** rolė tame išteklyje (Bicep šablonai tai priskiria automatiškai)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), prisijungęs per `az login`
- Java 21+ ir Maven 3.9+

> **API rakto nereikia** — autentifikacija vykdoma be rakto per Microsoft Entra ID.

## Greitas startas

```bash
# 1. Eikite į projektą
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Prisijunkite, kad keyless autentifikacija galėtų gauti žetoną
az login

# 3. Konfigūruokite galinį tašką
#    - Jei vykdėte `azd up`, .env failas jau buvo sukurtas jums (praleiskite šį žingsnį).
#    - Priešingu atveju nukopijuokite šabloną ir nustatykite AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Paleiskite programą
mvn spring-boot:run
```

## Kaip veikia autentifikacija

Šis pavyzdys naudoja autentifikaciją su **Microsoft Entra ID** — API rakto nėra.

Programa aiškiai konfigūruoja autentifikaciją faile [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` sukuria `BearerTokenCredential` naudojant `AuthenticationUtil.getBearerTokenSupplier` su `DefaultAzureCredential` ir sritimi `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` sukuria `OpenAIClient` naudojant `OpenAIOkHttpClient.builder()`, nurodo išteklių galinį tašką `/openai/v1` ir prideda prieigos tokeną su `.credential(...)`.
3. `azureChatModel()` perduoda tą klientą Spring AI `OpenAiChatModel`, kuris aptarnauja pamokos `ChatClient`.

Šie aiškūs bean'ai neleidžia globaliam `OPENAI_API_KEY` perrašyti Azure autentifikacijos. Vien tik API rakto nedeklaravimas YAML faile nėra autentifikacijos nustatymas. `DefaultAzureCredential` gali naudoti jūsų vietinę `az login` sesiją arba valdytą identitetą Azure; pasirinktai identitetui turi būti priskirta aukščiau nurodyta išteklių rolė.

## Programos paleidimas

### Naudojant Maven

```bash
mvn spring-boot:run
```

### Naudojant VS Code

1. Atidarykite projektą VS Code
2. Paspauskite `F5` arba naudokite "Run and Debug" panelę
3. Pasirinkite "Spring Boot-BasicChatApplication" konfigūraciją

> **Pastaba**: programa krauna `.env` failą iš savo darbo katalogo, įskaitant paleidžiant iš VS Code.

### Laukiamas išėjimas

Iliustracinis išėjimas po sėkmingo paleidimo (paleidimo žurnalai nepateikiami; atsakymo tekstas gali skirtis):

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

## Konfigūracijos nuoroda

### Aplinkos kintamieji

| Kintamasis | Aprašymas | Privalomas | Pavyzdys |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) galutinio taško URL | Taip | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Pokalbių modelio diegimo pavadinimas | Ne | `gpt-5.6-luna` (numatytasis) |

> Nėra **API rakto** kintamojo — autentifikacija vykdoma be rakto (Microsoft Entra ID per `az login`).

### Spring konfigūracija

Nustatymai faile [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) naudoja prefiksą `spring.ai.openai` ir išskleistus pokalbių parametrus (nėra `options` bloko):

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

`model` yra **Azure diegimo pavadinimas**. Autentifikacija gaunama iš aukščiau aprašytų aiškių bean'ų, o ne iš `api-key` parametro. Pamokoje išjungiamos loginės dedukcijos ir ribojami užbaigimo tokenai iki 500; `temperature` ir senasis `max-tokens` nenurodyti.

Microsoft rekomenduoja [oficialų OpenAI SDK su Azure OpenAI v1 ir Responses API naujoms programoms](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Pokalbių užbaigimai vis dar palaikomi šioje esamoje žinutėms skirtai pamokoje. GPT-5.6 atveju užklausos, kuriose naudojami įrankiai su Pokalbių užbaigimais, turi nustatyti `reasoning_effort` į `none`; derinant su įrankiais naudokite Responses API. Daugiau žr. [įrankių kvietimą su loginiais modeliais](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Trikčių šalinimas

### Dažnos problemos

<details>
<summary><strong>Klaida: 401 / "PermissionDenied" / tokeno klaidos</strong></summary>

- Paleiskite `az login` — autentifikacijai be rakto reikia aktyvios prisijungimo sesijos norint gauti tokeną
- Patikrinkite, ar jūsų paskyrai priskirta **Cognitive Services OpenAI User** rolė ištekliui
- Jei ką tik priskyrėte rolę, palaukite minutę, kol ji įsigalios
- Patikrinkite, ar esate teisingame nuomotojo/prenumeratos kontekste (`az account show`)
</details>

<details>
<summary><strong>Klaida: "The endpoint is not valid" / ryšio klaidos</strong></summary>

- Įsitikinkite, kad `AZURE_OPENAI_ENDPOINT` yra pilnas bazinis URL (pvz., `https://your-resource.openai.azure.com/`)
- Patikrinkite, ar galinio taško URL tvarkingas ir turi tinkamą kylančią brūkšnį
- Patikrinkite, ar galutinis taškas sutampa su jūsų diegiamu ištekliumi (`azd env get-values`)
</details>

<details>
<summary><strong>Klaida: "The deployment was not found"</strong></summary>

- Patikrinkite, ar `AZURE_OPENAI_DEPLOYMENT` atitinka diegimo pavadinimą Azure
- Patikrinkite, ar modelis sėkmingai įdiegtas ir aktyvus
- Numatytoji diegimo pavadinimas yra `gpt-5.6-luna`
</details>

<details>
<summary><strong>Klaida: 429 / viršytas užklausų dažnio limitas</strong></summary>

- Numatytoji GPT-5.6 Luna diegimo talpa Global Standard 10: 10 užklausų/min ir 10 000 tokenų/min
- Vykdykite pavyzdžius paeiliui ir laukite paslaugos pakartojimo intervalo prieš bandydami dar kartą
- Šis paprastas pavyzdys išjungia automatinį SDK pakartojimą, tad nepavykęs užklausimas pranešamas tiesiogiai
</details>

<details>
<summary><strong>VS Code: Aplinkos kintamieji nekraunami</strong></summary>

- Įsitikinkite, kad `.env` failas yra projekto šakniniame kataloge (ta pačia vieta kaip `pom.xml`)
- Pabandykite paleisti `mvn spring-boot:run` VS Code integruotoje terminalo aplinkoje
- Patikrinkite, ar VS Code Java papildinys tinkamai įdiegtas
</details>

### Derinimo režimas

Norėdami įjungti išsamius žurnalus, atkomentuokite šias eilutes faile [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Kiti žingsniai

**Nustatymas baigtas!** Tęskite mokymosi kelią:

[3 skyrius: Pagrindinės generatyvios dirbtinio intelekto technikos](../../../03-CoreGenerativeAITechniques/README.md)

## Ištekliai

- [Spring AI 2 OpenAI Java SDK perėjimas](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficialus OpenAI Java SDK su Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentifikacija be rakto su Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry portalas](https://ai.azure.com/)
- [Azure AI Foundry dokumentacija](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kilusią naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->