# Sette opp utviklingsmiljøet for Azure AI Foundry

> Denne guiden setter opp **Azure AI Foundry**-modeller for Java AI-appene i dette kurset, ved å bruke **nøkkelfri** autentisering (Microsoft Entra ID) — ingen API-nøkler å administrere. Ny med verktøyene? Start med [utviklingsmiljøguiden](./README.md).

Denne guiden setter opp **Azure AI Foundry**-modeller for Java AI-appene i dette kurset. Du har to valg:

- **Alternativ A — Provisionering med `azd` + Bicep (anbefalt):** én kommando deployerer Foundry-kontoen og modeller som kode. Ingen portal-klikk.
- **Alternativ B — Opprett ressurser manuelt** i Azure AI Foundry-portalen.

Begge valg bruker **nøkkelfri autentisering** (Microsoft Entra ID) — det finnes ingen API-nøkler å kopiere eller lekke.

## Innholdsfortegnelse

- [Hva blir opprettet](#hva-blir-opprettet)
- [Forutsetninger](#forutsetninger)
- [Alternativ A: Provisionering med azd + Bicep (Anbefalt)](#option-a-provision-with-azd--bicep-recommended)
- [Alternativ B: Opprett ressurser manuelt](#alternativ-b-opprett-ressurser-manuelt)
- [Konfigurer miljøet ditt](#konfigurer-miljøet-ditt)
- [Test oppsettet ditt](#test-oppsettet-ditt)
- [Hva er det neste?](#hva-er-det-neste)
- [Ressurser](#ressurser)
- [Ekstra ressurser](#ekstra-ressurser)

## Hva blir opprettet

Bicep-malene i [`infra/`](../../../02-SetupDevEnvironment/infra) provisionerer:

- En **Azure AI Foundry**-konto (`Microsoft.CognitiveServices/accounts`, type `AIServices`) med et prosjekt
- En **chat**-utplassering - GPT-5.6 Luna (`gpt-5.6-luna`), versjon `2026-07-09`, med `GlobalStandard` kapasitet `10` (10 forespørsler/minutt og 10 000 tokens/minutt for denne modellen)
- En **embedding**-utplassering - `text-embedding-3-small`, versjon `1` (brukes i senere kapitler)
- En **nøkkelfri rolle-tilordning** (`Cognitive Services OpenAI User`) så du logger inn med `az login` i stedet for å administrere nøkler

## Forutsetninger

- Et [Azure-abonnement](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) og [Maven 3.9+](https://maven.apache.org/download.cgi)

## Alternativ A: Provisionering med azd + Bicep (Anbefalt)

Fra mappen `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Logg inn (begge verktøy)
azd auth login
az login

# Opprett Foundry-konto + modellutrullinger
azd up
```

`azd` spør om et **miljønavn** (for eksempel `genai-java`), **abonnement** og **region**. Velg ditt eget abonnement og en region hvor `gpt-5.6-luna` og `text-embedding-3-small` er tilgjengelig, for eksempel `eastus2`. Bekreft at abonnementet har tilstrekkelig kvote for modellen og utplasseringstypen i den regionen; tilgjengelighet og kvoter varierer per abonnement.

Når provisjoneringen er ferdig, gjør azd:

1. Deployerer alt som er definert i [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Kjører en postprovision-hook som skriver [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) med din endpoint og utplasseringsnavn (ingen hemmeligheter).

> **Tips:** Kjør `azd up` når som helst for å bruke endringer. Kjør `azd down` for å slette alt og stoppe kostnader.

For å se de genererte innstillingene:

```bash
azd env get-values
```

Nå kan du gå direkte til [Test oppsettet ditt](#test-oppsettet-ditt).

## Alternativ B: Opprett ressurser manuelt

Foretrekker du portalen? Opprett ressursene manuelt:

1. Gå til [Azure AI Foundry-portalen](https://ai.azure.com/) og logg inn.
2. **Opprett et prosjekt** (dette oppretter også en AI Foundry-ressurs). Gi det et navn som `GenAIJava`.
3. I prosjektet ditt, åpne **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Deploy **GPT-5.6 Luna** (modell og utplasseringsnavn `gpt-5.6-luna`, versjon `2026-07-09`) med **Global Standard** kapasitet `10`. Gjenta for **text-embedding-3-small**, versjon `1`, om du vil ha embedding-eksemplene.
5. Fra **Oversikt**, kopier **endpoint** (for eksempel `https://<resource>.openai.azure.com/`).
6. Gi deg selv nøkkelfri tilgang: på ressursen, åpne **Access control (IAM)** → **Legg til rolle-tilordning** → tilordne **Cognitive Services OpenAI User** til kontoen din.

> **Fortsatt problemer?** Se [Azure AI Foundry-dokumentasjonen](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurer miljøet ditt

**Hvis du brukte Alternativ A (`azd up`)**, er innstillingsfilen din allerede skrevet — ingenting å konfigurere. Hopp til [Test oppsettet ditt](#test-oppsettet-ditt).

**Hvis du brukte Alternativ B (manuelt)**, opprett eksempel-filen `.env` selv:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Rediger `.env` med din endpoint (ingen nøkkel — autentisering er nøkkelfri):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Bruk ressursens Azure OpenAI endpoint, ikke en prosjekt-URL. basic-chat-appen løser den til `/openai/v1` og konfigurerer en eksplisitt bearer-token-klient; API-nøkkel er ikke nødvendig.

> **Sikkerhetsnotat:** Det finnes ingen API-nøkkel å lagre. Du autentiserer deg med Microsoft Entra ID via `az login` (lokalt) eller en administrert identitet (i Azure). `.env`-filen holder kun ikke-hemmelige innstillinger og er allerede dekket av `.gitignore`.

## Test oppsettet ditt

Sørg for at du er pålogget slik at nøkkelfri autentisering kan hente en token, deretter kjør eksemplet:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # hvis du ikke allerede er logget inn
mvn clean spring-boot:run
```

Du skal se et svar fra `gpt-5.6-luna`-modellen. Kjør eksemplene sekvensielt for å holde deg innenfor den lille standardkvoten; hvis du får HTTP 429, vent på retry-intervallet før du prøver igjen.

> **VS Code-brukere:** Trykk `F5` for å kjøre. Appen laster din `.env` automatisk.

> **Fullt eksempel:** Se [Basic Chat med Azure AI Foundry-eksempelet](./examples/basic-chat-azure/README.md) for detaljer og feilsøking.

## Hva er det neste?

Etter provisjonering og vellykket kjøring av eksemplet vil du ha:
- Azure AI Foundry med `gpt-5.6-luna` og `text-embedding-3-small` utplassert
- Nøkkelfri autentisering (Microsoft Entra ID) — ingen nøkler å administrere
- En lokal `.env` med din endpoint og utplasseringsnavn
- Et Java utviklingsmiljø klart til bruk

**Fortsett til** [Kapittel 3: Kjerne teknikker for generativ AI](../03-CoreGenerativeAITechniques/README.md) for å begynne å bygge AI-applikasjoner!

## Ressurser

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Nøkkelfri autentisering med Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry-dokumentasjon](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK overgang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Offisielt OpenAI Java SDK med Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Ekstra ressurser

- [Last ned VS Code](https://code.visualstudio.com/Download)
- [Last ned Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container-konfigurasjon](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->