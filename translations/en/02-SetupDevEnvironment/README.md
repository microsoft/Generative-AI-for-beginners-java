# Setting Up the Development Environment for Generative AI for Java

> **Quick Start:** Provision your AI models on **Azure AI Foundry** as code with Bicep + `azd` in a few minutes — see the [Azure AI Foundry Setup Guide](getting-started-azure-openai.md). Authentication is **keyless** (Microsoft Entra ID), so there are no API keys to manage.

## What You'll Learn

- Set up a Java development environment for AI applications
- Choose and configure your preferred development environment (cloud-first with Codespaces, local dev container, or full local setup)
- Test your setup by connecting to an Azure AI Foundry model

## Table of Contents

- [What You'll Learn](#what-youll-learn)
- [Introduction](#introduction)
- [Step 1: Set Up Your Development Environment](#step-1-set-up-your-development-environment)
  - [Option A: GitHub Codespaces (Recommended)](#option-a-github-codespaces-recommended)
  - [Option B: Local Dev Container](#option-b-local-dev-container)
  - [Option C: Use Your Existing Local Installation](#option-c-use-your-existing-local-installation)
- [Step 2: Provision Azure AI Foundry](#step-2-provision-azure-ai-foundry)
- [Step 3: Test Your Setup](#step-3-test-your-setup)
- [Troubleshooting](#troubleshooting)
- [Summary](#summary)
- [Next Steps](#next-steps)

## Introduction

This chapter will guide you through setting up a development environment. We'll use **Azure AI Foundry** for the models throughout this course. You provision the models as code with Bicep and the Azure Developer CLI (`azd`), then connect with **keyless authentication** (Microsoft Entra ID) — no API keys to copy or leak.

**No local setup required!** You can use GitHub Codespaces, which provides a full development environment in your browser, and provision Foundry from there.

We use **Azure AI Foundry** for this course because it's:
- **Provisioned as code** — one `azd up` deploys the account and model deployments
- **Keyless** — authenticate with your Azure sign-in or a managed identity
- **Production-ready** — the same code runs locally and in Azure
- **Flexible** — swap models by changing a deployment name, not your code

> **Note**: Azure AI Foundry deployments are billed per token (pay-as-you-go). See the [Azure AI Foundry setup guide](getting-started-azure-openai.md) for provisioning, region, and cost details.


## Step 1: Set Up Your Development Environment

<a name="quick-start-cloud"></a>

We've created a preconfigured development container to minimize setup time and ensure you have all the necessary tools for this Generative AI for Java course. Choose your preferred development approach:

### Environment Setup Options:

#### Option A: GitHub Codespaces (Recommended)

**Start coding in 2 minutes - no local setup required!**

1. Fork this repository to your GitHub account
   > **Note**: If you want to edit the basic config please have a look at the [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Click **Code** → **Codespaces** tab → **...** → **New with options...**
3. Use the defaults – this will select the **Dev container configuration**: **Generative AI Java Development Environment** custom devcontainer created for this course
4. Click **Create codespace**
5. Wait ~2 minutes for the environment to be ready
6. Proceed to [Step 2: Provision Azure AI Foundry](#step-2-provision-azure-ai-foundry)

<img src="../../../translated_images/en/codespaces.9945ded8ceb431a5.webp" alt="Screenshot: Codespaces submenu" width="50%">

<img src="../../../translated_images/en/image.833552b62eee7766.webp" alt="Screenshot: New with options" width="50%">

<img src="../../../translated_images/en/codespaces-create.b44a36f728660ab7.webp" alt="Screenshot: Create codespace options" width="50%">


> **Benefits of Codespaces**:
> - No local installation required
> - Works on any device with a browser
> - Pre-configured with all tools and dependencies
> - Free 60 hours per month for personal accounts
> - Consistent environment for all learners

#### Option B: Local Dev Container

**For developers who prefer local development with Docker**

1. Fork and clone this repository to your local machine
   > **Note**: If you want to edit the basic config please have a look at the [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Install [Docker Desktop](https://www.docker.com/products/docker-desktop/) and [VS Code](https://code.visualstudio.com/)
3. Install the [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) in VS Code
4. Open the repository folder in VS Code
5. When prompted, click **Reopen in Container** (or use `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Wait for the container to build and start
7. Proceed to [Step 2: Provision Azure AI Foundry](#step-2-provision-azure-ai-foundry)

<img src="../../../translated_images/en/devcontainer.21126c9d6de64494.webp" alt="Screenshot: Dev container setup" width="50%">

<img src="../../../translated_images/en/image-3.bf93d533bbc84268.webp" alt="Screenshot: Dev container build complete" width="50%">

#### Option C: Use Your Existing Local Installation

**For developers with existing Java environments**

Prerequisites:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) or your preferred IDE

Steps:
1. Clone this repository to your local machine
2. Open the project in your IDE
3. Proceed to [Step 2: Provision Azure AI Foundry](#step-2-provision-azure-ai-foundry)

> **Pro Tip**: If you have a low-spec machine but want VS Code locally, use GitHub Codespaces! You can connect your local VS Code to a cloud-hosted Codespace for the best of both worlds.

<img src="../../../translated_images/en/image-2.fc0da29a6e4d2aff.webp" alt="Screenshot: created local devcontainer instance" width="50%">


## Step 2: Provision Azure AI Foundry

Deploy the course's AI models to Azure AI Foundry as code. From the repository root:

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` prompts for an environment name, subscription, and region, provisions an Azure AI Foundry account with `gpt-5.6-luna` and `text-embedding-3-small` deployments, and writes the endpoint into the example's `.env` - all with **keyless** authentication (no API keys).

> **Full walkthrough:** See the [Azure AI Foundry Setup Guide](getting-started-azure-openai.md) for prerequisites, a manual (portal) alternative, region guidance, and cost/cleanup notes.

## Step 3: Test Your Setup

Once your Foundry models are provisioned, test the connection with the example app in [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure).

1. Open the terminal in your development environment.
2. Navigate to the example:
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. Make sure you're signed in (keyless auth needs a token):
   ```bash
   az login
   ```
   > If you ran `azd up`, the `.env` file with your endpoint was already written for you.
4. Run the application:
   ```bash
   mvn clean spring-boot:run
   ```

You should see a response from the `gpt-5.6-luna` model.

### Understanding the Example Code

The [basic-chat example](./examples/basic-chat-azure/README.md) uses **Spring Boot 4.1.1** and **Spring AI 2.0.1**. Spring AI's `ChatClient` is backed by the official OpenAI Java SDK, connecting to the Azure OpenAI **v1** endpoint with keyless authentication.

**What this code does:**
- **Connects** to Azure AI Foundry using your Azure sign-in (Microsoft Entra ID) — no API key
- **Sends** a prompt to the `gpt-5.6-luna` model
- **Receives** and displays the AI's response
- **Validates** your setup is working correctly

**Key Dependencies** (excerpt from [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)):
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
</dependency>
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>${azure-identity.version}</version>
</dependency>
```

The POM manages OpenAI Java **4.63.1** and sets Azure Identity **1.18.6** explicitly. Spring AI 2 removed the Azure-specific starter; Azure Identity is still needed for the credential bean.

**Configuration** ([application.yml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)):
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

Keyless auth is configured explicitly in [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java), not inferred from an absent API key. Its bearer credential uses `DefaultAzureCredential` with the `https://ai.azure.com/.default` scope, and its `OpenAIClient` targets `/openai/v1`. The app supplies that client to Spring AI's chat model, so a global `OPENAI_API_KEY` cannot override Azure authentication.

Chat settings are directly under `spring.ai.openai.chat`, without an `options` block. The lesson retains Chat Completions with `reasoning-effort: none` and a 500-token completion cap; it does not set `temperature` or `max-tokens`. See the [example's configuration reference](./examples/basic-chat-azure/README.md#spring-configuration) for the API choice and tool-calling guidance.

## Summary

After completing the steps above, you will have:

- Provisioned Azure AI Foundry models as code with Bicep + `azd`
- Got your Java development environment running (whether that's Codespaces, dev containers, or local)
- Connected to Azure AI Foundry with keyless authentication (Microsoft Entra ID) — no API keys
- Tested it all works with a simple example that talks to your model

## Next Steps

[Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md)

## Troubleshooting

Having issues? Here are common problems and solutions:

- **Authentication failing (401/403)?** 
  - Run `az login` — authentication is keyless, so you must be signed in
  - Verify your account has the **Cognitive Services OpenAI User** role on the resource
  - If you just provisioned, wait a minute for the role assignment to propagate

- **Maven not found?** 
  - If using dev containers/Codespaces, Maven should be pre-installed
  - For local setup, ensure Java 21+ and Maven 3.9+ are installed
  - Try `mvn --version` to verify installation

- **`azd` not found or provisioning fails?** 
  - Install the [Azure Developer CLI](https://aka.ms/azure-dev/install) and run `azd auth login`
  - Pick a region where `gpt-5.6-luna` and `text-embedding-3-small` are available (e.g. `eastus2`), with sufficient quota in your selected subscription
  - See the [Azure AI Foundry setup guide](getting-started-azure-openai.md) for details

- **Dev container not starting?** 
  - Ensure Docker Desktop is running (for local development)
  - Try rebuilding the container: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Application compilation errors?**
  - Ensure you're in the correct directory: `02-SetupDevEnvironment/examples/basic-chat-azure`
  - Try cleaning and rebuilding: `mvn clean compile`

> **Need help?**: Still having issues? Open an issue in the repository and we'll help you out.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
This document has been translated using AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please be aware that automated translations may contain errors or inaccuracies. The original document in its native language should be considered the authoritative source. For critical information, professional human translation is recommended. We are not liable for any misunderstandings or misinterpretations arising from the use of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->