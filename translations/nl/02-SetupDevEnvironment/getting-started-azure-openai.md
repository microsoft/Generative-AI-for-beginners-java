# De ontwikkelomgeving instellen voor Azure AI Foundry

> Deze gids stelt **Azure AI Foundry** modellen in voor de Java AI-apps in deze cursus, met gebruik van **keyless** authenticatie (Microsoft Entra ID) — geen API-sleutels om te beheren. Nieuw met de tools? Begin met de [ontwikkelomgevinggids](./README.md).

Deze gids stelt **Azure AI Foundry** modellen in voor de Java AI-apps in deze cursus. Je hebt twee opties:

- **Optie A — Provisioneren met `azd` + Bicep (aanbevolen):** één opdracht zet het Foundry-account en modellen als code uit. Geen portal geklik.
- **Optie B — Maak bronnen handmatig aan** in het Azure AI Foundry-portaal.

Beide opties gebruiken **keyless authenticatie** (Microsoft Entra ID) — er zijn geen API-sleutels om te kopiëren of lekken.

## Inhoudsopgave

- [Wat Wordt Aangemaakt](#wat-wordt-aangemaakt)
- [Vereisten](#vereisten)
- [Optie A: Provisioneren met azd + Bicep (Aanbevolen)](#option-a-provision-with-azd--bicep-recommended)
- [Optie B: Bronnen Handmatig Aanmaken](#optie-b-bronnen-handmatig-aanmaken)
- [Je omgeving configureren](#je-omgeving-configureren)
- [Test je setup](#test-je-setup)
- [Wat nu?](#wat-nu)
- [Bronnen](#bronnen)
- [Aanvullende bronnen](#aanvullende-bronnen)

## Wat Wordt Aangemaakt

De Bicep-templates in [`infra/`](../../../02-SetupDevEnvironment/infra) voorzien in:

- Een **Azure AI Foundry** account (`Microsoft.CognitiveServices/accounts`, soort `AIServices`) met een project
- Een **chat** implementatie - GPT-5.6 Luna (`gpt-5.6-luna`), versie `2026-07-09`, met `GlobalStandard` capaciteit `10` (10 verzoeken/minuut en 10.000 tokens/minuut voor dit model)
- Een **embedding** implementatie - `text-embedding-3-small`, versie `1` (gebruikt in latere hoofdstukken)
- Een **keyless roltoewijzing** (`Cognitive Services OpenAI User`) zodat je inlogt met `az login` in plaats van sleutels te beheren

## Vereisten

- Een [Azure-abonnement](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) en [Maven 3.9+](https://maven.apache.org/download.cgi)

## Optie A: Provisioneren met azd + Bicep (Aanbevolen)

Vanuit de map `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Inloggen (beide tools)
azd auth login
az login

# Voorzie het Foundry-account + modelimplementaties
azd up
```

`azd` vraagt om een **omgevingsnaam** (bijv. `genai-java`), **abonnement**, en **regio**. Kies je eigen abonnement en een regio waar `gpt-5.6-luna` en `text-embedding-3-small` beschikbaar zijn, bijvoorbeeld `eastus2`. Bevestig dat het abonnement voldoende quota heeft voor het model en type implementatie in die regio; beschikbaarheid en quota variëren per abonnement.

Wanneer het provisioneren klaar is, doet azd het volgende:

1. Zet alles uit wat is gedefinieerd in [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Voert een postprovision hook uit die [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) schrijft met jouw endpoint en implementatienamen (zonder geheimen).

> **Tip:** Voer `azd up` op elk moment opnieuw uit om wijzigingen toe te passen. Voer `azd down` uit om alles te verwijderen en kosten te stoppen.

Om de gegenereerde instellingen te zien:

```bash
azd env get-values
```

Ga nu naar [Test je setup](#test-je-setup).

## Optie B: Bronnen Handmatig Aanmaken

Gebruik je liever het portaal? Maak de bronnen handmatig aan:

1. Ga naar het [Azure AI Foundry portaal](https://ai.azure.com/) en log in.
2. **Maak een project aan** (dit maakt ook een AI Foundry resource aan). Geef het een naam zoals `GenAIJava`.
3. Open in je project **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Zet **GPT-5.6 Luna** in productie (model- en implementatienaam `gpt-5.6-luna`, versie `2026-07-09`) met **Global Standard** capaciteit `10`. Herhaal voor **text-embedding-3-small**, versie `1`, als je de embedding voorbeelden wilt.
5. Kopieer vanuit **Overzicht** de **endpoint** (bijv. `https://<resource>.openai.azure.com/`).
6. Verleen jezelf keyless toegang: open op de resource **Toegangsbeheer (IAM)** → **Roltoewijzing toevoegen** → wijs **Cognitive Services OpenAI User** toe aan je account.

> **Heb je nog problemen?** Raadpleeg de [Azure AI Foundry documentatie](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Je omgeving configureren

**Als je Optie A (`azd up`) hebt gebruikt**, is je instellingenbestand al geschreven — er is niets te configureren. Ga door naar [Test je setup](#test-je-setup).

**Als je Optie B (handmatig) hebt gebruikt**, maak dan zelf het `.env` bestand van het voorbeeld aan:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Bewerk `.env` met je endpoint (geen sleutel — authenticatie is keyless):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Gebruik de Azure OpenAI endpoint van de resource, niet de URL van een project. De basic-chat app resolveert deze naar `/openai/v1` en configureert een expliciete bearer-token client; een API sleutel is niet nodig.

> **Beveiligingsnotitie:** Er is geen API sleutel om op te slaan. Je authenticatie gebeurt met Microsoft Entra ID via `az login` (lokaal) of een managed identity (in Azure). Het `.env`-bestand bevat alleen niet-geheime instellingen en is al opgenomen in `.gitignore`.

## Test je setup

Zorg dat je bent ingelogd zodat keyless authenticatie een token kan halen, voer dan het voorbeeld uit:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # als je nog niet bent ingelogd
mvn clean spring-boot:run
```

Je zou een reactie van het `gpt-5.6-luna` model moeten zien. Voer voorbeelden sequentieel uit om binnen de kleine standaardquota te blijven; als je HTTP 429 ontvangt, wacht dan de retry-interval af voordat je het opnieuw probeert.

> **VS Code gebruikers:** Druk op `F5` om te starten. De app laadt je `.env` automatisch.

> **Volledig voorbeeld:** Zie het [Basic Chat met Azure AI Foundry voorbeeld](./examples/basic-chat-azure/README.md) voor details en probleemoplossing.

## Wat nu?

Na het provisioneren en succesvol draaien van het voorbeeld, heb je:
- Azure AI Foundry met `gpt-5.6-luna` en `text-embedding-3-small` uitgerold
- Keyless authenticatie (Microsoft Entra ID) — geen sleutels om te beheren
- Een lokaal `.env` met jouw endpoint en implementatienamen
- Een Java ontwikkelomgeving die klaar is voor gebruik

**Ga verder naar** [Hoofdstuk 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) om te beginnen met het bouwen van AI-applicaties!

## Bronnen

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Keyless authenticatie met Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Documentatie](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK overgang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Officiële OpenAI Java SDK met Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Aanvullende bronnen

- [Download VS Code](https://code.visualstudio.com/Download)
- [Krijg Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuratie](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->