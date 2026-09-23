# Setting Up the Development Environment for Azure AI Foundry

> This guide sets up **Azure AI Foundry** models for the Java AI apps in this course, using **keyless** authentication (Microsoft Entra ID) — no API keys to manage. New to the tooling? Start with the [development environment guide](./README.md).

This guide sets up **Azure AI Foundry** models for the Java AI apps in this course. You have two paths:

- **Option A — Provision with `azd` + Bicep (recommended):** one command deploys the Foundry account and models as code. No portal clicking.
- **Option B — Create resources manually** in the Azure AI Foundry portal.

Both paths use **keyless authentication** (Microsoft Entra ID) — there are no API keys to copy or leak.

## Table of Contents

- [What Gets Created](#what-gets-created)
- [Prerequisites](#prerequisites)
- [Option A: Provision with azd + Bicep (Recommended)](#option-a-provision-with-azd--bicep-recommended)
- [Option B: Create Resources Manually](#option-b-create-resources-manually)
- [Configure Your Environment](#configure-your-environment)
- [Test Your Setup](#test-your-setup)
- [What's Next?](#whats-next)
- [Resources](#resources)
- [Additional Resources](#additional-resources)

## What Gets Created

The Bicep templates in [`infra/`](../../../02-SetupDevEnvironment/infra) provision:

- An **Azure AI Foundry** account (`Microsoft.CognitiveServices/accounts`, kind `AIServices`) with a project
- A **chat** deployment - GPT-5.6 Luna (`gpt-5.6-luna`), version `2026-07-09`, with `GlobalStandard` capacity `10` (10 requests/minute and 10,000 tokens/minute for this model)
- An **embedding** deployment - `text-embedding-3-small`, version `1` (used in later chapters)
- A **keyless role assignment** (`Cognitive Services OpenAI User`) so you sign in with `az login` instead of managing keys

## Prerequisites

- An [Azure subscription](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) and [Maven 3.9+](https://maven.apache.org/download.cgi)

## Option A: Provision with azd + Bicep (Recommended)

From the `02-SetupDevEnvironment` folder:

```bash
cd 02-SetupDevEnvironment

# Sign in (both tools)
azd auth login
az login

# Provision the Foundry account + model deployments
azd up
```

`azd` prompts for an **environment name** (for example `genai-java`), **subscription**, and **region**. Choose your own subscription and a region where `gpt-5.6-luna` and `text-embedding-3-small` are available, for example `eastus2`. Confirm that the subscription has sufficient quota for the model and deployment type in that region; availability and quota vary by subscription.

When provisioning finishes, azd:

1. Deploys everything defined in [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Runs a postprovision hook that writes [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) with your endpoint and deployment names (no secrets).

> **Tip:** Re-run `azd up` any time to apply changes. Run `azd down` to delete everything and stop incurring cost.

To see the generated settings:

```bash
azd env get-values
```

Now skip to [Test Your Setup](#test-your-setup).

## Option B: Create Resources Manually

Prefer the portal? Create the resources by hand:

1. Go to the [Azure AI Foundry portal](https://ai.azure.com/) and sign in.
2. **Create a project** (this also creates an AI Foundry resource). Give it a name like `GenAIJava`.
3. In your project, open **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Deploy **GPT-5.6 Luna** (model and deployment name `gpt-5.6-luna`, version `2026-07-09`) with **Global Standard** capacity `10`. Repeat for **text-embedding-3-small**, version `1`, if you want the embedding examples.
5. From **Overview**, copy the **endpoint** (for example `https://<resource>.openai.azure.com/`).
6. Grant yourself keyless access: on the resource, open **Access control (IAM)** → **Add role assignment** → assign **Cognitive Services OpenAI User** to your account.

> **Still having trouble?** See the [Azure AI Foundry documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configure Your Environment

**If you used Option A (`azd up`)**, your settings file is already written — there's nothing to configure. Skip to [Test Your Setup](#test-your-setup).

**If you used Option B (manual)**, create the example's `.env` file yourself:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Edit `.env` with your endpoint (no key — auth is keyless):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Use the resource's Azure OpenAI endpoint, not a project URL. The basic-chat app resolves it to `/openai/v1` and configures an explicit bearer-token client; an API key is not required.

> **Security note:** There is no API key to store. You authenticate with Microsoft Entra ID via `az login` (locally) or a managed identity (in Azure). The `.env` file holds only non-secret settings and is already covered by `.gitignore`.

## Test Your Setup

Make sure you're signed in so keyless auth can get a token, then run the example:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # if you aren't already signed in
mvn clean spring-boot:run
```

You should see a response from the `gpt-5.6-luna` model. Run examples sequentially to stay within the small default quota; if you receive HTTP 429, wait for the retry interval before trying again.

> **VS Code users:** Press `F5` to run. The app loads your `.env` automatically.

> **Full example:** See the [Basic Chat with Azure AI Foundry example](./examples/basic-chat-azure/README.md) for details and troubleshooting.

## What's Next?

After provisioning and successfully running the example, you will have:
- Azure AI Foundry with `gpt-5.6-luna` and `text-embedding-3-small` deployed
- Keyless authentication (Microsoft Entra ID) — no keys to manage
- A local `.env` with your endpoint and deployment names
- A Java development environment ready to go

**Continue to** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) to start building AI applications!

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
This document has been translated using AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please be aware that automated translations may contain errors or inaccuracies. The original document in its native language should be considered the authoritative source. For critical information, professional human translation is recommended. We are not liable for any misunderstandings or misinterpretations arising from the use of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->