# Grundläggande chatt med Azure AI Foundry - Exemplifiering från början till slut

Detta exempel är en enkel Spring Boot-applikation som ansluter till en **Azure AI Foundry**-modell med hjälp av **autentisering utan nyckel** (Microsoft Entra ID) och testar din installation. Det använder Spring AI:s `ChatClient`, som stöds av den **officiella OpenAI Java SDK** och **Azure OpenAI v1**-endpointen.

Versionerna i [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) är Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** och dotenv-java **3.2.0**. Exemplet använder `spring-ai-starter-model-openai` och deklarerar uttryckligen `openai-java` och `azure-identity`; Spring AI 2 tog bort den gamla Azure OpenAI-startaren.

## Innehållsförteckning

- [Förutsättningar](#förutsättningar)
- [Snabbstart](#snabbstart)
- [Hur autentisering fungerar](#hur-autentisering-fungerar)
- [Köra applikationen](#köra-applikationen)
  - [Använda Maven](#använda-maven)
  - [Använda VS Code](#använda-vs-code)
  - [Förväntad utdata](#förväntad-utdata)
- [Konfigurationsreferens](#konfigurationsreferens)
  - [Miljövariabler](#miljövariabler)
  - [Spring-konfiguration](#spring-konfiguration)
- [Felsökning](#felsökning)
  - [Vanliga problem](#vanliga-problem)
  - [Felsökningsläge](#felsökningsläge)
- [Nästa steg](#nästa-steg)
- [Resurser](#resurser)

## Förutsättningar

Innan du kör detta exempel, säkerställ att du har:

- En Azure AI Foundry-resurs med en `gpt-5.6-luna`-distribution - förse den med `azd up` eller manuellt via [Azure AI Foundry installationsguide](../../getting-started-azure-openai.md)
- Rollen **Cognitive Services OpenAI User** på den resursen (Bicep-mallarna tilldelar detta åt dig)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), inloggad med `az login`
- Java 21+ och Maven 3.9+

> **Ingen API-nyckel krävs** — autentisering är utan nyckel via Microsoft Entra ID.

## Snabbstart

```bash
# 1. Navigera till projektet
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Logga in så att keyless autentisering kan få en token
az login

# 3. Konfigurera slutpunkten
#    - Om du körde `azd up`, skrevs .env för dig (hoppa över detta).
#    - Annars kopiera mallen och ange AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Kör applikationen
mvn spring-boot:run
```

## Hur autentisering fungerar

Detta exempel autentiserar med **Microsoft Entra ID** — det finns ingen API-nyckel.

Applikationen konfigurerar autentisering uttryckligen i [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` skapar en `BearerTokenCredential` med `AuthenticationUtil.getBearerTokenSupplier` med `DefaultAzureCredential` och omfånget `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` bygger en `OpenAIClient` med `OpenAIOkHttpClient.builder()`, löser resursens endpoint till `/openai/v1` och tillhandahåller bearertoken-uppgifterna med `.credential(...)`.
3. `azureChatModel()` levererar den klienten till Spring AI:s `OpenAiChatModel`, som backar lektionens `ChatClient`.

Dessa uttryckliga beans förhindrar att en global `OPENAI_API_KEY` åsidosätter Azure-autentisering. Att utelämna en API-nyckel från YAML ensam är inte konfigurationen för autentisering. `DefaultAzureCredential` kan använda din `az login`-session lokalt eller en hanterad identitet i Azure; vilken identitet som än väljs måste ha den resursrollen som nämns ovan.

## Köra applikationen

### Använda Maven

```bash
mvn spring-boot:run
```

### Använda VS Code

1. Öppna projektet i VS Code
2. Tryck på `F5` eller använd panelen "Kör och felsök"
3. Välj "Spring Boot-BasicChatApplication"-konfigurationen

> **Notera**: Applikationen laddar `.env` från sin arbetskatalog, även vid start från VS Code.

### Förväntad utdata

Illustrativ utdata efter en lyckad körning (uppstartsloggar utelämnas; formulering i svar kan variera):

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

## Konfigurationsreferens

### Miljövariabler

| Variabel | Beskrivning | Obligatorisk | Exempel |
|----------|-------------|-------------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endpoint-URL | Ja | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Namn på chatmodell-distribution | Nej | `gpt-5.6-luna` (standard) |

> Det finns **ingen** API-nyckel-variabel — autentisering är utan nyckel (Microsoft Entra ID via `az login`).

### Spring-konfiguration

Inställningarna i [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) använder prefixet `spring.ai.openai` och platta chattegenskaper (ingen `options`-block):

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

`model` är **Azure-distributionsnamnet**. Autentisering kommer från de uttryckliga beans som beskrivits ovan, inte från en `api-key`-inställning. Lektionen inaktiverar resonemang och sätter tak för slutförandetokens till 500; lämnar `temperature` och den äldre `max-tokens` oinställda.

Microsoft rekommenderar [officiella OpenAI SDK med Azure OpenAI v1 och Responses API för nya applikationer](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions stöds fortfarande för denna existerande lektions modell med meddelandebaserade samtal. För GPT-5.6 måste förfrågningar med verktyg på Chat Completions sätta `reasoning_effort` till `none`; använd Responses när resonemang kombineras med verktyg. Se [verktygsanrop med resonemangsmodeller](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Felsökning

### Vanliga problem

<details>
<summary><strong>Fel: 401 / "PermissionDenied" / tokenfel</strong></summary>

- Kör `az login` — autentisering utan nyckel kräver en aktiv inloggning för att få en token
- Kontrollera att ditt konto har rollen **Cognitive Services OpenAI User** på resursen
- Om du just tilldelade rollen, vänta en minut för att ändringen ska slå igenom
- Bekräfta att du är i rätt tenant/prenumeration (`az account show`)
</details>

<details>
<summary><strong>Fel: "The endpoint is not valid" / anslutningsfel</strong></summary>

- Kontrollera att `AZURE_OPENAI_ENDPOINT` är hela bas-URL:en (t.ex. `https://your-resource.openai.azure.com/`)
- Kontrollera att slashes i slutet är konsekventa
- Säkerställ att endpointen motsvarar din tillhandahållna resurs (`azd env get-values`)
</details>

<details>
<summary><strong>Fel: "The deployment was not found"</strong></summary>

- Kontrollera att `AZURE_OPENAI_DEPLOYMENT` matchar ett distributionsnamn i Azure
- Kontrollera att modellen är lyckosamt distribuerad och aktiv
- Standardsnamnet för distribution är `gpt-5.6-luna`
</details>

<details>
<summary><strong>Fel: 429 / hastighetsgräns överskriden</strong></summary>

- Standarddistributionen GPT-5.6 Luna har Global Standard kapacitet 10: 10 förfrågningar/minut och 10 000 tokens/minut
- Kör exempel sekventiellt och vänta tjänstens retry-intervall innan du försöker igen
- Detta enkla exempel inaktiverar automatiska SDK-försök, så en misslyckad förfrågan rapporteras direkt
</details>

<details>
<summary><strong>VS Code: Miljövariabler laddas inte</strong></summary>

- Säkerställ att din `.env`-fil finns i projektets rotkatalog (på samma nivå som `pom.xml`)
- Försök köra `mvn spring-boot:run` i VS Codes integrerade terminal
- Kontrollera att VS Code:s Java-tillägg är korrekt installerat
</details>

### Felsökningsläge

För att aktivera detaljerad loggning, avkommentera dessa rader i [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Nästa steg

**Installation slutförd!** Fortsätt din läranderesa:

[Kapitel 3: Kärntekniker för generativ AI](../../../03-CoreGenerativeAITechniques/README.md)

## Resurser

- [Spring AI 2 OpenAI Java SDK-övergång](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Officiella OpenAI Java SDK med Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentisering utan nyckel med Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry-portal](https://ai.azure.com/)
- [Azure AI Foundry-dokumentation](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->