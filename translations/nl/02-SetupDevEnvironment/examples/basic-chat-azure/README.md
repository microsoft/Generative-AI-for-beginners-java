# Basis Chat met Azure AI Foundry - End-to-End Voorbeeld

Dit voorbeeld is een eenvoudige Spring Boot-applicatie die verbinding maakt met een **Azure AI Foundry**-model met behulp van **keyless authenticatie** (Microsoft Entra ID) en test uw setup. Het gebruikt Spring AI's `ChatClient`, ondersteund door de **officiële OpenAI Java SDK** en het **Azure OpenAI v1**-eindpunt.

De versies in [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) zijn Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, en dotenv-java **3.2.0**. Het voorbeeld gebruikt `spring-ai-starter-model-openai` en verklaart expliciet `openai-java` en `azure-identity`; Spring AI 2 heeft de oude Azure OpenAI starter verwijderd.

## Inhoudsopgave

- [Vereisten](#vereisten)
- [Snel Beginnen](#snel-beginnen)
- [Hoe Authenticatie Werkt](#hoe-authenticatie-werkt)
- [De Applicatie Uitvoeren](#de-applicatie-uitvoeren)
  - [Gebruik van Maven](#gebruik-van-maven)
  - [Gebruik van VS Code](#gebruik-van-vs-code)
  - [Verwachte Output](#verwachte-output)
- [Configuratie Referentie](#configuratie-referentie)
  - [Omgevingsvariabelen](#omgevingsvariabelen)
  - [Spring Configuratie](#spring-configuratie)
- [Probleemoplossing](#probleemoplossing)
  - [Veelvoorkomende Problemen](#veelvoorkomende-problemen)
  - [Debug Modus](#debug-modus)
- [Volgende Stappen](#volgende-stappen)
- [Bronnen](#bronnen)

## Vereisten

Zorg ervoor dat u het volgende hebt voordat u dit voorbeeld uitvoert:

- Een Azure AI Foundry-resource met een `gpt-5.6-luna` deployment - provisioneer het met `azd up` of handmatig via de [Azure AI Foundry setup gids](../../getting-started-azure-openai.md)
- De **Cognitive Services OpenAI User** rol op die resource (de Bicep-templates wijzen dit voor u toe)
- De [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), aangemeld met `az login`
- Java 21+ en Maven 3.9+

> **Geen API-sleutel nodig** — authenticatie is keyless via Microsoft Entra ID.

## Snel Beginnen

```bash
# 1. Navigeer naar het project
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Meld je aan zodat keyless authenticatie een token kan krijgen
az login

# 3. Configureer het eindpunt
#    - Als je `azd up` hebt uitgevoerd, is .env voor je geschreven (sla dit over).
#    - Anders kopieer de template en stel AZURE_OPENAI_ENDPOINT in:
cp .env.example .env

# 4. Start de applicatie
mvn spring-boot:run
```

## Hoe Authenticatie Werkt

Dit voorbeeld authenticeert met **Microsoft Entra ID** — er is geen API-sleutel.

De applicatie configureert authenticatie expliciet in [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` creëert een `BearerTokenCredential` met behulp van `AuthenticationUtil.getBearerTokenSupplier` met `DefaultAzureCredential` en de scope `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` bouwt een `OpenAIClient` met `OpenAIOkHttpClient.builder()`, lost het resource-eindpunt op naar `/openai/v1`, en levert de bearer credential met `.credential(...)`.
3. `azureChatModel()` levert die client aan Spring AI's `OpenAiChatModel`, welke de `ChatClient` van deze les ondersteunt.

Deze expliciete beans voorkomen dat een globale `OPENAI_API_KEY` Azure-authenticatie overschrijft. Alleen het weglaten van een API-sleutel in YAML is niet de juiste authenticatieopzet. `DefaultAzureCredential` kan uw `az login` sessie lokaal gebruiken of een managed identity in Azure; welke identiteit ook wordt geselecteerd, die moet de bovenstaande rol op de resource hebben.

## De Applicatie Uitvoeren

### Gebruik van Maven

```bash
mvn spring-boot:run
```

### Gebruik van VS Code

1. Open het project in VS Code
2. Druk op `F5` of gebruik het "Run and Debug" paneel
3. Selecteer de configuratie "Spring Boot-BasicChatApplication"

> **Opmerking**: De applicatie laadt `.env` uit de werkdirectory, ook wanneer gestart vanuit VS Code.

### Verwachte Output

Voorbeeldoutput na een succesvolle uitvoering (opstartlogs weggelaten; formulering van antwoorden kan variëren):

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

## Configuratie Referentie

### Omgevingsvariabelen

| Variabele | Beschrijving | Verplicht | Voorbeeld |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) eindpunt URL | Ja | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Chat model deployment naam | Nee | `gpt-5.6-luna` (standaard) |

> Er is **geen** API-sleutel variabele — authenticatie is keyless (Microsoft Entra ID via `az login`).

### Spring Configuratie

De [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) instellingen gebruiken de prefix `spring.ai.openai` en afgeplatte chat-eigenschappen (geen `options` blok):

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

`model` is de **Azure deployment naam**. Authenticatie komt van de expliciete beans hierboven beschreven, niet van een `api-key` instelling. De les schakelt redeneren uit en beperkt completion tokens tot 500; `temperature` en de verouderde `max-tokens` worden niet ingesteld.

Microsoft beveelt de [officiële OpenAI SDK met Azure OpenAI v1 en de Responses API voor nieuwe toepassingen](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) aan. Chat Completions blijft ondersteund voor deze bestaande op bericht gebaseerde les. Voor GPT-5.6 moeten verzoeken met tools op Chat Completions `reasoning_effort` op `none` zetten; gebruik Responses voor het combineren van redeneren met tools. Zie [tool calling met reasoning modellen](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Probleemoplossing

### Veelvoorkomende Problemen

<details>
<summary><strong>Fout: 401 / "PermissionDenied" / token fouten</strong></summary>

- Voer `az login` uit — keyless authenticatie vereist een actieve aanmelding om een token te krijgen
- Controleer of uw account de rol **Cognitive Services OpenAI User** op de resource heeft
- Als u zojuist de rol heeft toegewezen, wacht een minuut voor propagatie
- Bevestig dat u zich in het juiste tenant/abonnement bevindt (`az account show`)
</details>

<details>
<summary><strong>Fout: "Het eindpunt is niet geldig" / verbindingsfouten</strong></summary>

- Zorg dat `AZURE_OPENAI_ENDPOINT` de volledige basis-URL is (bijv. `https://your-resource.openai.azure.com/`)
- Let op consistente trailing slash
- Controleer of het eindpunt overeenkomt met uw geprovisioneerde resource (`azd env get-values`)
</details>

<details>
<summary><strong>Fout: "De deployment is niet gevonden"</strong></summary>

- Controleer of `AZURE_OPENAI_DEPLOYMENT` overeenkomt met een deployment-naam in Azure
- Kijk of het model succesvol is uitgerold en actief is
- De standaard deployment-naam is `gpt-5.6-luna`
</details>

<details>
<summary><strong>Fout: 429 / tarieflimiet overschreden</strong></summary>

- De standaard GPT-5.6 Luna deployment heeft Global Standard capaciteit 10: 10 verzoeken/minuut en 10.000 tokens/minuut
- Voer voorbeelden na elkaar uit en wacht op het retry-interval van de service voordat u opnieuw probeert
- Dit basisvoorbeeld schakelt automatische SDK retries uit, dus een mislukte aanvraag wordt direct gerapporteerd
</details>

<details>
<summary><strong>VS Code: Omgevingsvariabelen laden niet</strong></summary>

- Zorg dat uw `.env` bestand in de hoofdmap van het project staat (op hetzelfde niveau als `pom.xml`)
- Probeer `mvn spring-boot:run` in de geïntegreerde terminal van VS Code uit te voeren
- Controleer of de Java-extensie van VS Code correct is geïnstalleerd
</details>

### Debug Modus

Om gedetailleerde logging in te schakelen, haal deze regels uit de commentaarstand in [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Volgende Stappen

**Setup Voltooid!** Ga door met uw leertraject:

[Hoofdstuk 3: Kerntechnieken van Generatieve AI](../../../03-CoreGenerativeAITechniques/README.md)

## Bronnen

- [Spring AI 2 OpenAI Java SDK overgang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Officiële OpenAI Java SDK met Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Keyless authenticatie met Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Azure AI Foundry Documentatie](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->