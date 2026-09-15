# How to Set Up Development Environment for Azure AI Foundry

> Dis guide dey set up **Azure AI Foundry** models for di Java AI apps wey dey dis course, wey dey use **keyless** authentication (Microsoft Entra ID) — no API keys to manage. You be new for di tooling? Start with di [development environment guide](./README.md).

Dis guide go set up **Azure AI Foundry** models for di Java AI apps wey dey dis course. You get two ways:

- **Option A — Provision with `azd` + Bicep (we recommend am):** one command dey deploy di Foundry account and models as code. No need for portal clicking.
- **Option B — Make resources yourself** for Azure AI Foundry portal.

Both ways dey use **keyless authentication** (Microsoft Entra ID) — no API keys to copy or leak.

## Table of Contents

- [Wetyn Dem Go Create](#wetyn-dem-go-create)
- [Prerequisites](#prerequisites)
- [Option A: Provision with azd + Bicep (We Recommend)](#option-a-provision-with-azd--bicep-recommended)
- [Option B: Make Resources Yourself](#option-b-make-resources-yourself)
- [Configure Your Environment](#configure-your-environment)
- [Test Your Setup](#test-your-setup)
- [Wetin Next?](#wetin-next)
- [Resources](#resources)
- [Additional Resources](#additional-resources)

## Wetyn Dem Go Create

Di Bicep templates for [`infra/`](../../../02-SetupDevEnvironment/infra) dem go make:

- One **Azure AI Foundry** account (`Microsoft.CognitiveServices/accounts`, kind `AIServices`) wey get project
- One **chat** deployment - GPT-5.6 Luna (`gpt-5.6-luna`), version `2026-07-09`, wey get `GlobalStandard` capacity `10` (10 requests/minute and 10,000 tokens/minute for dis model)
- One **embedding** deployment - `text-embedding-3-small`, version `1` (we go use am later for chapters)
- One **keyless role assignment** (`Cognitive Services OpenAI User`) so you fit sign in with `az login` no need manage keys

## Prerequisites

- One [Azure subscription](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) and [Maven 3.9+](https://maven.apache.org/download.cgi)

## Option A: Provision with azd + Bicep (We Recommend)

From di `02-SetupDevEnvironment` folder:

```bash
cd 02-SetupDevEnvironment

# Sign in (both tools)
azd auth login
az login

# Set up di Foundry account plus di model deployments
azd up
```

`azd` go ask for **environment name** (example `genai-java`), **subscription**, and **region**. Choose your own subscription and region where `gpt-5.6-luna` and `text-embedding-3-small` dey, example `eastus2`. Confirm say di subscription get enough quota for di model and deployment type for dat region; availability and quota fit different for each subscription.

When di provisioning don finish, azd:

1. Go deploy everything wey dey for [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Go run postprovision hook wey go write [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) with your endpoint and deployment names (no secrets).

> **Tip:** You fit run `azd up` anytime you want apply changes. Run `azd down` to delete everything and stop to dey cost you money.

To see di settings wey dem generate:

```bash
azd env get-values
```

Now you fit continue go [Test Your Setup](#test-your-setup).

## Option B: Make Resources Yourself

You like to use portal? Make the resources by yourself:

1. Go [Azure AI Foundry portal](https://ai.azure.com/) and sign in.
2. **Make a project** (dis one go also make AI Foundry resource). Give am name like `GenAIJava`.
3. For your project, open **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Deploy **GPT-5.6 Luna** (model and deployment name `gpt-5.6-luna`, version `2026-07-09`) with **Global Standard** capacity `10`. Do am again for **text-embedding-3-small**, version `1`, if you want di embedding examples.
5. From **Overview**, copy di **endpoint** (example `https://<resource>.openai.azure.com/`).
6. Give yourself keyless access: for di resource, open **Access control (IAM)** → **Add role assignment** → assign **Cognitive Services OpenAI User** to your account.

> **Still get wahala?** Check [Azure AI Foundry documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configure Your Environment

**If you use Option A (`azd up`)**, your settings file don already write — no need configure anything. You fit skip go [Test Your Setup](#test-your-setup).

**If you use Option B (manual)**, make di example `.env` file yourself:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Change `.env` with your endpoint (no key — auth na keyless):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Use di resource Azure OpenAI endpoint, no be project URL. Di basic-chat app dey resolve am to `/openai/v1` and e configure explicit bearer-token client; no need API key.

> **Security note:** No API key to save. You go authenticate with Microsoft Entra ID via `az login` (local) or managed identity (for Azure). Di `.env` file get only non-secret settings, e don already dey cover by `.gitignore`.

## Test Your Setup

Make sure say you don sign in so that keyless auth fit get token, then run di example:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # if you no don signin before na
mvn clean spring-boot:run
```

You suppose see response from di `gpt-5.6-luna` model. Run examples one by one so that you no go pass di small default quota; if you get HTTP 429, wait before you try again.

> **For VS Code users:** Press `F5` to run. Di app dey load your `.env` automatically.

> **Full example:** Check [Basic Chat with Azure AI Foundry example](./examples/basic-chat-azure/README.md) for details and how to solve problems.

## Wetin Next?

After you don provision and run di example well, you go get:
- Azure AI Foundry with `gpt-5.6-luna` and `text-embedding-3-small` deployed
- Keyless authentication (Microsoft Entra ID) — no keys to manage
- Local `.env` with your endpoint and deployment names
- Java development environment ready to use

**Continue to** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) to start to build AI applications!

## Resources

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Keyless authentication with Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK transition](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Official OpenAI Java SDK with Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Additional Resources

- [Download VS Code](https://code.visualstudio.com/Download)
- [Get Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg make you know say automated translation fit get errors or mistakes. Di original document for dia own language na im be di correct source. For important info, make person wey sabi human translation do am. We no go responsible for any misunderstanding or wrong understanding wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->