# Installera utvecklingsmiljön för Azure AI Foundry

> Den här guiden ställer in **Azure AI Foundry**-modeller för Java AI-apparna i den här kursen, med **nyckellös** autentisering (Microsoft Entra ID) – inga API-nycklar att hantera. Ny med verktygen? Börja med [guider för utvecklingsmiljön](./README.md).

Den här guiden ställer in **Azure AI Foundry**-modeller för Java AI-apparna i den här kursen. Du har två vägar:

- **Alternativ A — Provisions med `azd` + Bicep (rekommenderas):** en kommandorad distribuerar Foundry-kontot och modeller som kod. Ingen portalnavigering.
- **Alternativ B — Skapa resurser manuellt** i Azure AI Foundry-portalen.

Båda vägarna använder **nyckellös autentisering** (Microsoft Entra ID) – inga API-nycklar att kopiera eller läcka.

## Innehållsförteckning

- [Vad som skapas](#vad-som-skapas)
- [Förutsättningar](#förutsättningar)
- [Alternativ A: Provision med azd + Bicep (Rekommenderas)](#option-a-provision-with-azd--bicep-recommended)
- [Alternativ B: Skapa resurser manuellt](#alternativ-b-skapa-resurser-manuellt)
- [Konfigurera din miljö](#konfigurera-din-miljö)
- [Testa din installation](#testa-din-installation)
- [Vad händer härnäst?](#vad-händer-härnäst)
- [Resurser](#resurser)
- [Ytterligare resurser](#ytterligare-resurser)

## Vad som skapas

Bicep-mallarna i [`infra/`](../../../02-SetupDevEnvironment/infra) provisionerar:

- Ett **Azure AI Foundry**-konto (`Microsoft.CognitiveServices/accounts`, typ `AIServices`) med ett projekt
- En **chat** implementation – GPT-5.6 Luna (`gpt-5.6-luna`), version `2026-07-09`, med `GlobalStandard` kapacitet `10` (10 förfrågningar/minut och 10 000 tokens/minut för denna modell)
- En **embedding** implementation – `text-embedding-3-small`, version `1` (används i senare kapitel)
- En **nyckellös rolltilldelning** (`Cognitive Services OpenAI User`) så att du kan logga in med `az login` istället för att hantera nycklar

## Förutsättningar

- Ett [Azure-prenumeration](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) och [Maven 3.9+](https://maven.apache.org/download.cgi)

## Alternativ A: Provision med azd + Bicep (Rekommenderas)

Från mappen `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Logga in (båda verktygen)
azd auth login
az login

# Tillhandahåll Foundry-kontot + modellinstallationer
azd up
```

`azd` frågar efter ett **miljönamn** (t.ex. `genai-java`), **prenumeration** och **region**. Välj din prenumeration och en region där `gpt-5.6-luna` och `text-embedding-3-small` är tillgängliga, till exempel `eastus2`. Bekräfta att prenumerationen har tillräcklig kvot för modellen och distributionstypen i regionen; tillgänglighet och kvot varierar per prenumeration.

När provisioneringen är klar gör azd:

1. Distribuerar allt som definieras i [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Kör en efterprovisionerings-hook som skriver [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) med din endpoint och distribueringsnamn (inga hemligheter).

> **Tips:** Kör `azd up` igen när som helst för att tillämpa ändringar. Kör `azd down` för att ta bort allt och stoppa kostnader.

För att se de genererade inställningarna:

```bash
azd env get-values
```

Hoppa nu till [Testa din installation](#testa-din-installation).

## Alternativ B: Skapa resurser manuellt

Föredrar du portalen? Skapa resurserna manuellt:

1. Gå till [Azure AI Foundry-portalen](https://ai.azure.com/) och logga in.
2. **Skapa ett projekt** (det skapar också en AI Foundry-resurs). Ge det ett namn som `GenAIJava`.
3. I projektet, öppna **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Distribuera **GPT-5.6 Luna** (modell och distribueringsnamn `gpt-5.6-luna`, version `2026-07-09`) med **Global Standard** kapacitet `10`. Upprepa för **text-embedding-3-small**, version `1`, om du vill ha embedding-exemplen.
5. På **Översikt**, kopiera **endpoint** (t.ex. `https://<resource>.openai.azure.com/`).
6. Ge dig själv nyckellös åtkomst: öppna på resursen **Access control (IAM)** → **Add role assignment** → tilldela **Cognitive Services OpenAI User** till ditt konto.

> **Fortfarande problem?** Se [Azure AI Foundry-dokumentationen](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurera din miljö

**Om du använde Alternativ A (`azd up`)** är din inställningsfil redan skapad – inget att konfigurera. Hoppa till [Testa din installation](#testa-din-installation).

**Om du använde Alternativ B (manuell)**, skapa exempelns `.env`-fil själv:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Redigera `.env` med din endpoint (ingen nyckel – autentiseringen är nyckellös):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Använd resursens Azure OpenAI-endpoint, inte en projekt-URL. Basic-chat-appen omdirigerar den till `/openai/v1` och konfigurerar en explicit behörighetstokenklient; en API-nyckel krävs inte.

> **Säkerhetsnotering:** Det finns ingen API-nyckel att lagra. Du autentiserar med Microsoft Entra ID via `az login` (lokalt) eller en hanterad identitet (i Azure). `.env`-filen innehåller endast icke-hemliga inställningar och omfattas redan av `.gitignore`.

## Testa din installation

Se till att du är inloggad så att nyckellös autentisering kan hämta en token, och kör sedan exemplet:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # om du inte redan är inloggad
mvn clean spring-boot:run
```

Du bör få ett svar från `gpt-5.6-luna`-modellen. Kör exemplen i följd för att hålla dig inom den lilla standardkvoten; om du får HTTP 429, vänta på retryintervallet innan du försöker igen.

> **VS Code-användare:** Tryck `F5` för att köra. Appen laddar din `.env` automatiskt.

> **Fullständigt exempel:** Se [Basic Chat med Azure AI Foundry-exemplet](./examples/basic-chat-azure/README.md) för detaljer och felsökning.

## Vad händer härnäst?

Efter provisionering och lyckad körning av exemplet kommer du att ha:
- Azure AI Foundry med `gpt-5.6-luna` och `text-embedding-3-small` distribuerade
- Nyckellös autentisering (Microsoft Entra ID) – inga nycklar att hantera
- En lokal `.env` med din endpoint och distribueringsnamn
- En Java-utvecklingsmiljö redo att använda

**Fortsätt till** [Kapitel 3: Kärntekniker för Generativ AI](../03-CoreGenerativeAITechniques/README.md) för att börja bygga AI-applikationer!

## Resurser

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Nyckellös autentisering med Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry-dokumentation](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK-övergång](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Officiellt OpenAI Java SDK med Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Ytterligare resurser

- [Ladda ner VS Code](https://code.visualstudio.com/Download)
- [Hämta Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container-konfiguration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->