# Azure AI Foundry kūrimo aplinkos nustatymas

> Šiame vadove nustatomi **Azure AI Foundry** modeliai šio kurso Java AI programėlėms, naudojant **be raktų** autentifikaciją (Microsoft Entra ID) — nereikia tvarkyti API raktų. Naujokas? Pradėkite nuo [kūrimo aplinkos vadovo](./README.md).

Šis vadovas nustato **Azure AI Foundry** modelius šio kurso Java AI programėlėms. Jūs turite du kelius:

- **A variantas — diegimas su `azd` + Bicep (rekomenduojama):** vienas komandos įvykdymas įdiegia Foundry paskyrą ir modelius kaip kodą. Nereikia naudoti portalo.
- **B variantas — ištekliai kuriami rankiniu būdu** Azure AI Foundry portale.

Abu keliai naudoja **be raktų autentifikavimą** (Microsoft Entra ID) — nėra API raktų, kuriuos reikėtų kopijuoti ar nutekinti.

## Turinys

- [Kas bus sukuriama](#kas-bus-sukuriama)
- [Išankstiniai reikalavimai](#išankstiniai-reikalavimai)
- [A variantas: diegimas su azd + Bicep (rekomenduojama)](#option-a-provision-with-azd--bicep-recommended)
- [B variantas: išteklių kūrimas rankiniu būdu](#b-variantas-išteklių-kūrimas-rankiniu-būdu)
- [Aplinkos konfigūravimas](#aplinkos-konfigūravimas)
- [Patikrinkite savo nustatymus](#patikrinkite-savo-nustatymus)
- [Kas toliau?](#kas-toliau)
- [Ištekliai](#ištekliai)
- [Papildomi ištekliai](#papildomi-ištekliai)

## Kas bus sukuriama

Bicep šablonai kataloge [`infra/`](../../../02-SetupDevEnvironment/infra) įdiegia:

- **Azure AI Foundry** paskyrą (`Microsoft.CognitiveServices/accounts`, tipo `AIServices`) su projektu
- **Pokalbių** diegimą - GPT-5.6 Luna (`gpt-5.6-luna`), versija `2026-07-09`, su `GlobalStandard` talpa `10` (10 užklausų/ min. ir 10 000 žetonų/ min. šiam modeliui)
- **Įterpimo** diegimą - `text-embedding-3-small`, versija `1` (naudojama vėlesniuose skyriuose)
- **Be raktų rolės paskyrimą** (`Cognitive Services OpenAI User`), kad prisijungtumėte su `az login` vietoje raktų tvarkymo

## Išankstiniai reikalavimai

- [Azure prenumerata](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) ir [Maven 3.9+](https://maven.apache.org/download.cgi)

## A variantas: diegimas su azd + Bicep (rekomenduojama)

Iš katalogo `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Prisijungti (abu įrankiai)
azd auth login
az login

# Sukonfigūruoti Foundry paskyrą ir modelių diegimus
azd up
```

`azd` paprašo **aplinkos vardo** (pvz., `genai-java`), **prenumeratos** ir **regiono**. Pasirinkite savo prenumeratą ir regioną, kuriame yra `gpt-5.6-luna` ir `text-embedding-3-small`, pavyzdžiui, `eastus2`. Patikrinkite, ar prenumeratoje tame regione yra pakankamai kvotos modeliui ir diegimo tipui; prieinamumas ir kvota priklauso nuo prenumeratos.

Kai diegimas baigtas, azd:

1. Įdiegia viską, kas aprašyta faile [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Vykdo post diegimo veiksmą, kuris sukuria failą [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) su jūsų galiniu tašku ir diegimo pavadinimais (be slaptų duomenų).

> **Patarimas:** bet kada paleiskite `azd up`, kad pritaikytumėte pakeitimus. Paleiskite `azd down`, kad pašalintumėte viską ir nustosite kurti išlaidas.

Norėdami peržiūrėti sugeneruotus nustatymus:

```bash
azd env get-values
```

Dabar pereikite prie [Patikrinkite savo nustatymus](#patikrinkite-savo-nustatymus).

## B variantas: išteklių kūrimas rankiniu būdu

Norite naudoti portalą? Sukurkite išteklius rankiniu būdu:

1. Eikite į [Azure AI Foundry portalą](https://ai.azure.com/) ir prisijunkite.
2. **Sukurkite projektą** (tai taip pat sukuria AI Foundry išteklių). Pavadinkite pvz., `GenAIJava`.
3. Savo projekte atidarykite **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Įdiekite **GPT-5.6 Luna** (modelio ir diegimo pavadinimas `gpt-5.6-luna`, versija `2026-07-09`) su **Global Standard** talpa `10`. Pakartokite su **text-embedding-3-small**, versija `1`, jei norite naudoti įterpimo pavyzdžius.
5. Iš **Overview** nukopijuokite **galo tašką** (pvz., `https://<resource>.openai.azure.com/`).
6. Suteikite sau be raktų prieigą: ištekliaus lange atidarykite **Access control (IAM)** → **Add role assignment** → priskirkite savo paskyrai rolę **Cognitive Services OpenAI User**.

> **Dar kyla problemų?** Žr. [Azure AI Foundry dokumentaciją](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Aplinkos konfigūravimas

**Jei naudojote A variantą (`azd up`)**, jūsų nustatymų failas jau yra sukurtas — nieko papildomai konfigūruoti nereikia. Pereikite prie [Patikrinkite savo nustatymus](#patikrinkite-savo-nustatymus).

**Jei naudojote B variantą (ranka)**, sukurkite pavyzdžio `.env` failą patys:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Redaguokite `.env` su savo galiniu tašku (nereikia rakto — autentifikacija be raktų):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Naudokite Azure OpenAI ištekliaus galinio taško URL, ne projekto URL. Basic-chat programa nukreipia jį į `/openai/v1` ir naudoja aiškų bearer-token klientą; API raktas nėra reikalingas.

> **Saugumo pastaba:** Nėra jokio API rakto, kurį reikėtų saugoti. Autentifikacija vykdoma per Microsoft Entra ID su `az login` (vietoje) arba valdomą tapatybę (Azure). `.env` failas talpina tik neslaptus nustatymus ir jau yra įtrauktas į `.gitignore`.

## Patikrinkite savo nustatymus

Įsitikinkite, kad esate prisijungę, kad be raktų autentifikacija galėtų gauti žetoną, ir paleiskite pavyzdį:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # jei dar nesate prisijungę
mvn clean spring-boot:run
```

Turėtumėte matyti atsakymą iš `gpt-5.6-luna` modelio. Paleiskite pavyzdžius paeiliui, kad neviršytumėte numatytos mažos kvotos; jei gausite HTTP 429, palaukite kol praėjus pakartojimo intervalui pabandysite dar kartą.

> **VS Code naudotojams:** paspauskite `F5`, kad paleistumėte programą. Ji automatiškai įkraus jūsų `.env`.

> **Pilnas pavyzdys:** žr. [Basic Chat su Azure AI Foundry pavyzdį](./examples/basic-chat-azure/README.md) su detalėmis ir trikčių šalinimu.

## Kas toliau?

Po diegimo ir sėkmingo pavyzdžio paleidimo turėsite:
- Azure AI Foundry su įdiegtais `gpt-5.6-luna` ir `text-embedding-3-small`
- Be raktų autentifikaciją (Microsoft Entra ID) — nereikia valdyti rakto
- Vietinį `.env` failą su jūsų galiniu tašku ir diegimo pavadinimais
- Paruoštą Java kūrimo aplinką

**Toliau tęskite** prie [3 skyriaus: Pagrindinės generatyvios AI technikos](../03-CoreGenerativeAITechniques/README.md) ir pradėkite kurti AI programas!

## Ištekliai

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Be raktų autentifikacija su Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry dokumentacija](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK migracija](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficialus OpenAI Java SDK su Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Papildomi ištekliai

- [Atsisiųsti VS Code](https://code.visualstudio.com/Download)
- [Gauti Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev konteinerio konfigūracija](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kilusią naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->