# Grundlæggende Chat med Azure AI Foundry - End-to-End Eksempel

Dette eksempel er en simpel Spring Boot-applikation, der forbinder til en **Azure AI Foundry**-model ved hjælp af **nøglefri autentificering** (Microsoft Entra ID) og tester din opsætning. Den bruger Spring AI's `ChatClient`, understøttet af den **officielle OpenAI Java SDK** og **Azure OpenAI v1** endpointet.

Versionerne i [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) er Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** og dotenv-java **3.2.0**. Eksemplet bruger `spring-ai-starter-model-openai` og erklærer eksplicit `openai-java` og `azure-identity`; Spring AI 2 fjernede den gamle Azure OpenAI starter.

## Indholdsfortegnelse

- [Forudsætninger](#forudsætninger)
- [Kom hurtigt i gang](#kom-hurtigt-i-gang)
- [Sådan fungerer autentificering](#sådan-fungerer-autentificering)
- [Køre applikationen](#køre-applikationen)
  - [Brug af Maven](#brug-af-maven)
  - [Brug af VS Code](#brug-af-vs-code)
  - [Forventet output](#forventet-output)
- [Konfigurationsreference](#konfigurationsreference)
  - [Miljøvariabler](#miljøvariabler)
  - [Spring-konfiguration](#spring-konfiguration)
- [Fejlfinding](#fejlfinding)
  - [Almindelige problemer](#almindelige-problemer)
  - [Debug-tilstand](#debug-tilstand)
- [Næste skridt](#næste-skridt)
- [Ressourcer](#ressourcer)

## Forudsætninger

Før du kører dette eksempel, skal du sikre dig, at du har:

- En Azure AI Foundry-ressource med en `gpt-5.6-luna` udrulning - deployér den med `azd up` eller manuelt via [Azure AI Foundry opsætningsvejledning](../../getting-started-azure-openai.md)
- Rollen **Cognitive Services OpenAI User** på denne ressource (Bicep-skabelonerne tildeler denne for dig)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), logget ind med `az login`
- Java 21+ og Maven 3.9+

> **Ingen API-nøgle kræves** — autentificering er nøglefri via Microsoft Entra ID.

## Kom hurtigt i gang

```bash
# 1. Naviger til projektet
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Log ind, så keyless auth kan få et token
az login

# 3. Konfigurer endepunktet
#    - Hvis du kørte `azd up`, blev .env skrevet for dig (spring dette over).
#    - Ellers kopier skabelonen og indstil AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Kør applikationen
mvn spring-boot:run
```

## Sådan fungerer autentificering

Dette eksempel autentificerer med **Microsoft Entra ID** — der er ingen API-nøgle.

Applikationen konfigurerer autentificering eksplicit i [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` opretter en `BearerTokenCredential` ved brug af `AuthenticationUtil.getBearerTokenSupplier` med `DefaultAzureCredential` og omfanget `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` opbygger en `OpenAIClient` med `OpenAIOkHttpClient.builder()`, løser resource endpointet til `/openai/v1`, og leverer bearer-credential med `.credential(...)`.
3. `azureChatModel()` leverer den klient til Spring AI's `OpenAiChatModel`, som understøtter lektionens `ChatClient`.

Disse eksplicitte beans forhindrer en global `OPENAI_API_KEY` i at overskrive Azure-autentificering. Det er ikke korrekt at udelade en API-nøgle i YAML alene for at sætte autentificeringen op. `DefaultAzureCredential` kan bruge din `az login` session lokalt eller en managed identity i Azure; den valgte identitet skal have den ovenfor listede ressource-rolle.

## Køre applikationen

### Brug af Maven

```bash
mvn spring-boot:run
```

### Brug af VS Code

1. Åbn projektet i VS Code
2. Tryk på `F5` eller brug panelet "Run and Debug"
3. Vælg "Spring Boot-BasicChatApplication" konfiguration

> **Note**: Applikationen loader `.env` fra sin arbejdsmappe, også når den startes fra VS Code.

### Forventet output

Illustrativt output efter en succesfuld kørsel (start-logfiler er udeladt; formuleringen i svar varierer):

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

## Konfigurationsreference

### Miljøvariabler

| Variabel | Beskrivelse | Påkrævet | Eksempel |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endpoint URL | Ja | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Navn på chat-modellens udrulning | Nej | `gpt-5.6-luna` (standard) |

> Der findes **ingen** API-nøglevariabel — autentificering er nøglefri (Microsoft Entra ID via `az login`).

### Spring-konfiguration

Indstillingerne i [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) bruger præfikset `spring.ai.openai` og fladede chat-egenskaber (uden `options` blok):

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

`model` er **Azure udrulningsnavnet**. Autentificeringen kommer fra de ovenfor beskrevne eksplicitte beans, ikke en `api-key` indstilling. Lektionen deaktiverer reasoning og sætter maks tokens for completion til 500; den lader `temperature` og legacy `max-tokens` være udefineret.

Microsoft anbefaler den [officielle OpenAI SDK med Azure OpenAI v1 og Responses API til nye applikationer](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions understøttes stadig for denne eksisterende beskedbaserede lektion. For GPT-5.6 skal anmodninger, der inkluderer værktøjer på Chat Completions, sætte `reasoning_effort` til `none`; brug Responses når man kombinerer reasoning med værktøjer. Se [tool calling med reasoning modeller](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Fejlfinding

### Almindelige problemer

<details>
<summary><strong>Fejl: 401 / "PermissionDenied" / token fejl</strong></summary>

- Kør `az login` — nøglefri autentificering kræver et aktivt login for at få et token
- Bekræft at din konto har rollen **Cognitive Services OpenAI User** på ressourcen
- Hvis du netop har tildelt rollen, vent et minut for at den træder i kraft
- Bekræft at du er i det rette tenant/subscription (`az account show`)
</details>

<details>
<summary><strong>Fejl: "The endpoint is not valid" / forbindelsesfejl</strong></summary>

- Sørg for at `AZURE_OPENAI_ENDPOINT` er den fulde base-URL (f.eks. `https://your-resource.openai.azure.com/`)
- Tjek om der er konsistens i trailing slash
- Bekræft at endpoint matcher din provisionerede ressource (`azd env get-values`)
</details>

<details>
<summary><strong>Fejl: "The deployment was not found"</strong></summary>

- Bekræft at `AZURE_OPENAI_DEPLOYMENT` matcher et udrulningsnavn i Azure
- Tjek at modellen er succesfuldt udrullet og aktiv
- Standardnavnet på udrulningen er `gpt-5.6-luna`
</details>

<details>
<summary><strong>Fejl: 429 / rate limit overskredet</strong></summary>

- Standard GPT-5.6 Luna udrulningen har Global Standard kapacitet 10: 10 forespørgsler/minut og 10.000 tokens/minut
- Kør eksempler sekventielt og vent på serviceens retry-interval før genforsøg
- Dette grundlæggende eksempel deaktiverer automatiske SDK gentagelser, så et fejlet kald rapporteres med det samme
</details>

<details>
<summary><strong>VS Code: Miljøvariabler loader ikke</strong></summary>

- Sørg for at din `.env` fil ligger i projektets rodmappe (samme niveau som `pom.xml`)
- Prøv at køre `mvn spring-boot:run` i VS Codes integrerede terminal
- Tjek at VS Code Java-udvidelsen er korrekt installeret
</details>

### Debug-tilstand

For at slå detaljeret logging til, fjern kommentarerne på disse linjer i [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Næste skridt

**Opsætning fuldført!** Fortsæt din læringsrejse:

[Kapitel 3: Grundlæggende Teknikker indenfor Generativ AI](../../../03-CoreGenerativeAITechniques/README.md)

## Ressourcer

- [Spring AI 2 OpenAI Java SDK overgang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Officiel OpenAI Java SDK med Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Nøglefri autentificering med Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Azure AI Foundry Dokumentation](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->