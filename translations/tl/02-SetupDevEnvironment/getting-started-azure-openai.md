# Pag-set Up ng Development Environment para sa Azure AI Foundry

> Itong gabay ay nagaayos ng **Azure AI Foundry** mga modelo para sa Java AI apps sa kursong ito, gamit ang **keyless** na authentication (Microsoft Entra ID) — walang API keys na kailangang pamahalaan. Bago ka sa gamit na ito? Magsimula sa [gabay sa development environment](./README.md).

Itong gabay ay nagaayos ng **Azure AI Foundry** mga modelo para sa Java AI apps sa kursong ito. Mayroon kang dalawang pagpipilian:

- **Option A — I-provision gamit ang `azd` + Bicep (inirerekomenda):** isang command ang nag-deploy ng Foundry account at mga modelo bilang code. Walang kailangang i-click sa portal.
- **Option B — Gumawa ng resources nang manu-mano** sa Azure AI Foundry portal.

Parehong paraan ay gumagamit ng **keyless authentication** (Microsoft Entra ID) — walang API keys na kinakailangang kopyahin o ma-leak.

## Talaan ng mga Nilalaman

- [Ano ang Nalilikha](#ano-ang-nalilikha)
- [Mga Kinakailangan](#mga-kinakailangan)
- [Option A: Pagprovision gamit ang azd + Bicep (Inirerekomenda)](#option-a-provision-with-azd--bicep-recommended)
- [Option B: Manu-manong Paglikha ng Resources](#option-b-manu-manong-paglikha-ng-resources)
- [I-configure ang Iyong Kapaligiran](#i-configure-ang-iyong-kapaligiran)
- [Subukan ang Iyong Setup](#subukan-ang-iyong-setup)
- [Ano ang Susunod?](#ano-ang-susunod)
- [Mga Resources](#mga-resources)
- [Mga Karagdagang Resources](#mga-karagdagang-resources)

## Ano ang Nalilikha

Ang mga Bicep template sa [`infra/`](../../../02-SetupDevEnvironment/infra) ay nag-pro-provision ng:

- Isang **Azure AI Foundry** account (`Microsoft.CognitiveServices/accounts`, kind `AIServices`) na may proyekto
- Isang **chat** deployment - GPT-5.6 Luna (`gpt-5.6-luna`), bersyon `2026-07-09`, na may `GlobalStandard` kapasidad na `10` (10 kahilingan/minuto at 10,000 tokens/minuto para sa modelong ito)
- Isang **embedding** deployment - `text-embedding-3-small`, bersyon `1` (ginagamit sa mga susunod na kabanata)
- Isang **keyless role assignment** (`Cognitive Services OpenAI User`) para makapag-sign in ka gamit ang `az login` imbes na pamahalaan ang mga keys

## Mga Kinakailangan

- Isang [Azure subscription](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) at [Maven 3.9+](https://maven.apache.org/download.cgi)

## Option A: Pagprovision gamit ang azd + Bicep (Inirerekomenda)

Mula sa `02-SetupDevEnvironment` folder:

```bash
cd 02-SetupDevEnvironment

# Mag-sign in (parehong mga tool)
azd auth login
az login

# I-provision ang Foundry account + mga deployment ng modelo
azd up
```

Hihingin ng `azd` ang **pangalan ng environment** (halimbawa `genai-java`), **subscription**, at **rehiyon**. Piliin ang iyong sariling subscription at isang rehiyon kung saan available ang `gpt-5.6-luna` at `text-embedding-3-small`, halimbawa `eastus2`. Siguraduhing may sapat na quota ang subscription para sa modelo at uri ng deployment sa rehiyong iyon; nag-iiba ang availability at quota depende sa subscription.

Kapag natapos ang provisioning, gagawin ng azd:

1. I-de-deploy ang lahat ng nakasaad sa [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Patatakbuhin ang isang postprovision hook na nagsusulat sa [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) gamit ang iyong endpoint at mga pangalan ng deployment (walang mga sikreto).

> **Tip:** Patakbuhin muli ang `azd up` anumang oras upang ilapat ang mga pagbabago. Patakbuhin ang `azd down` upang tanggalin ang lahat at ihinto ang pag-incur ng gastos.

Para makita ang mga nalikhang settings:

```bash
azd env get-values
```

Ngayon, direktang pumunta sa [Subukan ang Iyong Setup](#subukan-ang-iyong-setup).

## Option B: Manu-manong Paglikha ng Resources

Mas gusto mo ba ang portal? Gawin ang mga resources nang manu-mano:

1. Pumunta sa [Azure AI Foundry portal](https://ai.azure.com/) at mag-sign in.
2. **Gumawa ng proyekto** (ito rin ay gumagawa ng isang AI Foundry resource). Bigyan ito ng pangalan tulad ng `GenAIJava`.
3. Sa iyong proyekto, buksan ang **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. I-deploy ang **GPT-5.6 Luna** (pangalan ng modelo at deployment `gpt-5.6-luna`, bersyon `2026-07-09`) na may kapasidad na **Global Standard** `10`. Ulitin para sa **text-embedding-3-small**, bersyon `1`, kung gusto mo ang embedding examples.
5. Mula sa **Overview**, kopyahin ang **endpoint** (halimbawa `https://<resource>.openai.azure.com/`).
6. Bigyan ang sarili mo ng keyless access: sa resource, buksan ang **Access control (IAM)** → **Add role assignment** → i-assign ang **Cognitive Services OpenAI User** sa iyong account.

> **May problema pa ba?** Tingnan ang [Azure AI Foundry documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## I-configure ang Iyong Kapaligiran

**Kung ginamit mo ang Option A (`azd up`)**, ang iyong settings file ay awtomatikong naisulat — walang kailangang isaayos. Dumiretso ka na sa [Subukan ang Iyong Setup](#subukan-ang-iyong-setup).

**Kung ginamit mo ang Option B (manu-mano)**, gumawa ka ng `.env` file ng halimbawa nang sarili:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

I-edit ang `.env` gamit ang iyong endpoint (walang key — keyless ang authentication):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Gamitin ang Azure OpenAI endpoint ng resource, hindi ang URL ng proyekto. Ina-resolve ng basic-chat app ito sa `/openai/v1` at kino-configure ang isang explicit bearer-token client; hindi kailangan ng API key.

> **Tandaan sa seguridad:** Walang API key na itinatago. Nag-authenticate ka gamit ang Microsoft Entra ID sa pamamagitan ng `az login` (lokal) o managed identity (sa Azure). Ang `.env` file ay naglalaman lamang ng mga non-secret settings at sakop na ng `.gitignore`.

## Subukan ang Iyong Setup

Siguraduhing naka-sign in ka para makakuha ng token ang keyless auth, pagkatapos patakbuhin ang halimbawa:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # kung hindi ka pa naka-sign in
mvn clean spring-boot:run
```

Dapat kang makakita ng tugon mula sa `gpt-5.6-luna` na modelo. Patakbuhin ang mga halimbawa nang sunud-sunod upang manatili sa loob ng maliit na default quota; kung makatanggap ng HTTP 429, maghintay sa retry interval bago subukan muli.

> **Para sa mga gumagamit ng VS Code:** Pindutin ang `F5` upang patakbuhin. Awtomatikong niloload ng app ang iyong `.env`.

> **Buong halimbawa:** Tingnan ang [Basic Chat with Azure AI Foundry example](./examples/basic-chat-azure/README.md) para sa mga detalye at pag-aayos ng problema.

## Ano ang Susunod?

Matapos ang provisioning at matagumpay na pagpapatakbo ng halimbawa, magkakaroon ka ng:
- Azure AI Foundry na may `gpt-5.6-luna` at `text-embedding-3-small` na na-deploy
- Keyless authentication (Microsoft Entra ID) — walang mga key na kailangang pamahalaan
- Isang lokal na `.env` na may iyong endpoint at mga pangalan ng deployment
- Isang handang Java development environment

**Magpatuloy sa** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) para magsimulang bumuo ng mga AI application!

## Mga Resources

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Keyless authentication gamit ang Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK transition](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Opisyal na OpenAI Java SDK na may Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Mga Karagdagang Resources

- [I-download ang VS Code](https://code.visualstudio.com/Download)
- [Kumuha ng Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang maling pagkakaintindi o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->