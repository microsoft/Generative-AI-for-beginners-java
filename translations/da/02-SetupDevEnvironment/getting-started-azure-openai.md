# Opsætning af udviklingsmiljøet for Azure AI Foundry

> Denne vejledning opsætter **Azure AI Foundry** modeller for Java AI-apps i dette kursus med **nøglefri** godkendelse (Microsoft Entra ID) — ingen API-nøgler at administrere. Ny til værktøjet? Start med [udviklingsmiljø-vejledningen](./README.md).

Denne vejledning opsætter **Azure AI Foundry** modeller for Java AI-apps i dette kursus. Du har to muligheder:

- **Mulighed A — Provisioner med `azd` + Bicep (anbefalet):** én kommando implementerer Foundry-kontoen og modeller som kode. Ingen portalklik.
- **Mulighed B — Opret ressourcer manuelt** i Azure AI Foundry-portalen.

Begge muligheder bruger **nøglefri godkendelse** (Microsoft Entra ID) — der er ingen API-nøgler at kopiere eller lække.

## Indholdsfortegnelse

- [Hvad oprettes](#hvad-oprettes)
- [Forudsætninger](#forudsætninger)
- [Mulighed A: Provision med azd + Bicep (anbefalet)](#option-a-provision-with-azd--bicep-recommended)
- [Mulighed B: Opret ressourcer manuelt](#mulighed-b-opret-ressourcer-manuelt)
- [Konfigurer dit miljø](#konfigurer-dit-miljø)
- [Test din opsætning](#test-din-opsætning)
- [Hvad er det næste?](#hvad-er-det-næste)
- [Ressourcer](#ressourcer)
- [Yderligere ressourcer](#yderligere-ressourcer)

## Hvad oprettes

Bicep-skabelonerne i [`infra/`](../../../02-SetupDevEnvironment/infra) provisionerer:

- En **Azure AI Foundry** konto (`Microsoft.CognitiveServices/accounts`, type `AIServices`) med et projekt
- En **chat** implementering - GPT-5.6 Luna (`gpt-5.6-luna`), version `2026-07-09`, med `GlobalStandard` kapacitet `10` (10 anmodninger/minut og 10.000 tokens/minut for denne model)
- En **embedding** implementering - `text-embedding-3-small`, version `1` (bruges i senere kapitler)
- En **nøglefri rolle tildeling** (`Cognitive Services OpenAI User`), så du logger ind med `az login` i stedet for at administrere nøgler

## Forudsætninger

- Et [Azure-abonnement](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) og [Maven 3.9+](https://maven.apache.org/download.cgi)

## Mulighed A: Provision med azd + Bicep (anbefalet)

Fra mappen `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Log ind (begge værktøjer)
azd auth login
az login

# Opret Foundry-kontoen + modelimplementeringer
azd up
```

`azd` spørger efter et **miljønavn** (for eksempel `genai-java`), **abonnement** og **region**. Vælg dit eget abonnement og en region, hvor `gpt-5.6-luna` og `text-embedding-3-small` er tilgængelige, for eksempel `eastus2`. Bekræft at abonnementet har tilstrækkelig kvote til modellen og implementeringstypen i den valgte region; tilgængelighed og kvoter varierer efter abonnement.

Når provisioning er færdig, gør azd:

1. Udvikler alt, der er defineret i [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Kører et post-provisioning hook, der skriver [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) med dine endepunkts- og implementeringsnavne (ingen hemmeligheder).

> **Tip:** Kør `azd up` igen når som helst for at anvende ændringer. Kør `azd down` for at slette alt og stoppe udgifter.

For at se de genererede indstillinger:

```bash
azd env get-values
```

Spring nu videre til [Test din opsætning](#test-din-opsætning).

## Mulighed B: Opret ressourcer manuelt

Foretrækker du portalen? Opret ressourcerne manuelt:

1. Gå til [Azure AI Foundry-portalen](https://ai.azure.com/) og log ind.
2. **Opret et projekt** (det opretter også en AI Foundry-ressource). Giv det et navn som `GenAIJava`.
3. I dit projekt, åbn **Modeller + endepunkter** → **Implementer model** → **Implementer basismodel**.
4. Implementer **GPT-5.6 Luna** (model- og implementeringsnavn `gpt-5.6-luna`, version `2026-07-09`) med **Global Standard** kapacitet `10`. Gentag for **text-embedding-3-small**, version `1`, hvis du ønsker embedding-eksemplerne.
5. Fra **Oversigt**, kopier **endepunktet** (for eksempel `https://<resource>.openai.azure.com/`).
6. Tildel dig selv nøglefri adgang: på ressourcen, åbn **Adgangskontrol (IAM)** → **Tilføj rolle tildeling** → tildel **Cognitive Services OpenAI User** til din konto.

> **Har du stadig problemer?** Se [Azure AI Foundry dokumentationen](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurer dit miljø

**Hvis du brugte Mulighed A (`azd up`)**, er din indstillingsfil allerede skrevet — der er intet at konfigurere. Spring til [Test din opsætning](#test-din-opsætning).

**Hvis du brugte Mulighed B (manuel)**, opret eksemplets `.env` fil selv:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Rediger `.env` med dit endepunkt (ingen nøgle — godkendelse er nøglefri):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Brug ressourcens Azure OpenAI-endepunkt, ikke en projekt-URL. Basic-chat app’en omsætter det til `/openai/v1` og konfigurerer en eksplicit bearer-token klient; en API-nøgle er ikke nødvendigt.

> **Sikkerheds note:** Der er ingen API-nøgle at gemme. Du godkender med Microsoft Entra ID via `az login` (lokalt) eller en managed identity (i Azure). `.env` filen indeholder kun ikke-hemmelige indstillinger og er allerede dækket af `.gitignore`.

## Test din opsætning

Sørg for, at du er logget ind, så nøglefri godkendelse kan få et token, og kør derefter eksemplet:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # hvis du ikke allerede er logget ind
mvn clean spring-boot:run
```

Du bør se et svar fra `gpt-5.6-luna` modellen. Kør eksempler sekventielt for at holde dig inden for den lille standardkvote; hvis du får HTTP 429, vent på gentagelsesintervallet før du prøver igen.

> **VS Code brugere:** Tryk `F5` for at køre. App’en indlæser automatisk din `.env`.

> **Fuld eksempel:** Se [Basic Chat med Azure AI Foundry eksemplet](./examples/basic-chat-azure/README.md) for detaljer og fejlfinding.

## Hvad er det næste?

Efter provisioning og succesfuld kørsel af eksemplet, vil du have:
- Azure AI Foundry med `gpt-5.6-luna` og `text-embedding-3-small` implementeret
- Nøglefri godkendelse (Microsoft Entra ID) — ingen nøgler at administrere
- En lokal `.env` med dit endepunkt og implementeringsnavne
- Et Java udviklingsmiljø klar til brug

**Fortsæt til** [Kapitel 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) for at begynde at bygge AI-applikationer!

## Ressourcer

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Nøglefri godkendelse med Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Dokumentation](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK overgang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Officiel OpenAI Java SDK med Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Yderligere ressourcer

- [Download VS Code](https://code.visualstudio.com/Download)
- [Hent Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Konfiguration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->