# Grunnleggende chat med Azure AI Foundry - ende-til-ende eksempel

Dette eksempelet er en enkel Spring Boot-applikasjon som kobler til en **Azure AI Foundry**-modell ved bruk av **nøkkelfri autentisering** (Microsoft Entra ID) og tester oppsettet ditt. Den benytter Spring AI sin `ChatClient`, støttet av den **offisielle OpenAI Java SDK** og **Azure OpenAI v1** endepunktet.

Versjonene i [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) er Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, og dotenv-java **3.2.0**. Eksempelet bruker `spring-ai-starter-model-openai` og deklarerer eksplisitt `openai-java` og `azure-identity`; Spring AI 2 fjernet den gamle Azure OpenAI starteren.

## Innholdsfortegnelse

- [Forutsetninger](#forutsetninger)
- [Rask start](#rask-start)
- [Hvordan autentisering fungerer](#hvordan-autentisering-fungerer)
- [Kjøring av applikasjonen](#kjøre-applikasjonen)
  - [Bruke Maven](#bruke-maven)
  - [Bruke VS Code](#bruke-vs-code)
  - [Forventet resultat](#forventet-resultat)
- [Konfigurasjonsreferanse](#konfigurasjonsreferanse)
  - [Miljøvariabler](#miljøvariabler)
  - [Spring-konfigurasjon](#spring-konfigurasjon)
- [Feilsøking](#feilsøking)
  - [Vanlige problemer](#vanlige-problemer)
  - [Feilsøkingsmodus](#feilsøkingsmodus)
- [Neste steg](#neste-steg)
- [Ressurser](#ressurser)

## Forutsetninger

Før du kjører dette eksempelet, sørg for at du har:

- En Azure AI Foundry-ressurs med en `gpt-5.6-luna` distribusjon - opprett den med `azd up` eller manuelt via [Azure AI Foundry oppsettsveiledning](../../getting-started-azure-openai.md)
- Rollen **Cognitive Services OpenAI User** på denne ressursen (Bicep-malene tildeler denne for deg)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), innlogget med `az login`
- Java 21+ og Maven 3.9+

> **Ingen API-nøkkel kreves** — autentisering er nøkkelfri via Microsoft Entra ID.

## Rask start

```bash
# 1. Naviger til prosjektet
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Logg inn slik at keyless autentisering kan få en token
az login

# 3. Konfigurer endepunktet
#    - Hvis du kjørte `azd up`, ble .env skrevet for deg (hopp over dette).
#    - Ellers kopier malen og sett AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Kjør applikasjonen
mvn spring-boot:run
```

## Hvordan autentisering fungerer

Dette eksempelet autentiserer med **Microsoft Entra ID** — det er ingen API-nøkkel.

Applikasjonen konfigurerer autentisering eksplisitt i [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` lager et `BearerTokenCredential` ved å bruke `AuthenticationUtil.getBearerTokenSupplier` med `DefaultAzureCredential` og `https://ai.azure.com/.default`-omfanget.
2. `azureOpenAiClient()` bygger en `OpenAIClient` med `OpenAIOkHttpClient.builder()`, løser ressursendepunktet til `/openai/v1`, og leverer bærebjelken med `.credential(...)`.
3. `azureChatModel()` gir denne klienten til Spring AI sin `OpenAiChatModel`, som støtter leksjonens `ChatClient`.

Disse eksplisitte beanene forhindrer at en global `OPENAI_API_KEY` overskriver Azure-autentiseringen. Det å utelate en API-nøkkel i YAML alene er ikke autentiseringsoppsettet. `DefaultAzureCredential` kan bruke din `az login`-økt lokalt eller en managed identity i Azure; hvilken som helst identitet som velges må ha ressursrollen som er nevnt ovenfor.

## Kjøre applikasjonen

### Bruke Maven

```bash
mvn spring-boot:run
```

### Bruke VS Code

1. Åpne prosjektet i VS Code
2. Trykk `F5` eller bruk panelet "Kjør og feilsøk"
3. Velg konfigurasjonen "Spring Boot-BasicChatApplication"

> **Merk**: Applikasjonen laster `.env` fra sin arbeidsmappe, også når den startes fra VS Code.

### Forventet resultat

Illustrerende utskrift etter vellykket kjøring (startlogger er utelatt; svarordlyden kan variere):

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

## Konfigurasjonsreferanse

### Miljøvariabler

| Variabel | Beskrivelse | Nødvendig | Eksempel |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endepunkt-URL | Ja | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Navnet på chatmodellens distribusjon | Nei | `gpt-5.6-luna` (standard) |

> Det finnes **ingen** API-nøkkelvariabel — autentisering er nøkkelfri (Microsoft Entra ID via `az login`).

### Spring-konfigurasjon

Innstillingene i [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) bruker prefikset `spring.ai.openai` og flat struktur for chat-egenskaper (ingen `options`-blokk):

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

`model` er **Azure-distribusjonsnavnet**. Autentiseringen kommer fra de eksplisitte beanene som beskrevet over, ikke en `api-key`-innstilling. Leksjonen deaktiverer resonnement og setter grense for fullførings-tokens til 500; den lar `temperature` og den gamle `max-tokens` være ubestemt.

Microsoft anbefaler [den offisielle OpenAI SDK med Azure OpenAI v1 og Responses API for nye applikasjoner](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions støttes fortsatt for denne eksisterende meldingsbaserte leksjonen. For GPT-5.6 må forespørsler som inkluderer verktøy på Chat Completions sette `reasoning_effort` til `none`; bruk Responses når resonnement kombineres med verktøy. Se [verktøysanrop med resonnementmodeller](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Feilsøking

### Vanlige problemer

<details>
<summary><strong>Feil: 401 / "PermissionDenied" / token-feil</strong></summary>

- Kjør `az login` — nøkkelfri autentisering trenger aktiv innlogging for å hente token
- Bekreft at kontoen din har rollen **Cognitive Services OpenAI User** på ressursen
- Hvis du nettopp har tildelt rollen, vent et minutt for at det skal slå igjennom
- Kontroller at du er i riktig tenant/abonnement (`az account show`)
</details>

<details>
<summary><strong>Feil: "The endpoint is not valid" / tilkoblingsfeil</strong></summary>

- Sørg for at `AZURE_OPENAI_ENDPOINT` er full base-URL (f.eks. `https://your-resource.openai.azure.com/`)
- Sjekk for konsekvent skråstrek på slutten
- Bekreft at endepunktet stemmer overens med ressursen du opprettet (`azd env get-values`)
</details>

<details>
<summary><strong>Feil: "The deployment was not found"</strong></summary>

- Kontroller at `AZURE_OPENAI_DEPLOYMENT` stemmer med et distribusjonsnavn i Azure
- Sjekk at modellen er distribuert og aktiv
- Standard distribusjonsnavn er `gpt-5.6-luna`
</details>

<details>
<summary><strong>Feil: 429 / grense for forespørsler overskredet</strong></summary>

- Standard GPT-5.6 Luna-distribusjon har Global Standard kapasitet 10: 10 forespørsler/minutt og 10,000 tokens/minutt
- Kjør eksempler sekvensielt og vent på tjenestens retry-intervall før ny forsøk
- Dette enkle eksemplet deaktiverer automatiske SDK-forsøk, så en mislykket forespørsel rapporteres direkte
</details>

<details>
<summary><strong>VS Code: Miljøvariabler lastes ikke</strong></summary>

- Sørg for at `.env`-filen din er i prosjektets rotmappe (samme nivå som `pom.xml`)
- Prøv å kjøre `mvn spring-boot:run` i VS Codes integrerte terminal
- Sjekk at VS Code Java-utvidelsen er riktig installert
</details>

### Feilsøkingsmodus

For å aktivere detaljert logging, fjern kommentaren fra disse linjene i [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Neste steg

**Oppsett fullført!** Fortsett din læringsreise:

[Kapittel 3: Kjerne teknikker for generativ AI](../../../03-CoreGenerativeAITechniques/README.md)

## Ressurser

- [Spring AI 2 OpenAI Java SDK overgang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Offisiell OpenAI Java SDK med Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Nøkkelfri autentisering med Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry-portalen](https://ai.azure.com/)
- [Azure AI Foundry dokumentasjon](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->