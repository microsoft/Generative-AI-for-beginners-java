# Basic Chat with Azure AI Foundry - End-to-End Example

This example is a simple Spring Boot application that connects to an **Azure AI Foundry** model using **keyless authentication** (Microsoft Entra ID) and tests your setup. It keeps Spring AI's `ChatClient`, backed by the **official OpenAI Java SDK** and the **Azure OpenAI v1** endpoint.

The versions in [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) are Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, and dotenv-java **3.2.0**. The sample uses `spring-ai-starter-model-openai` and explicitly declares `openai-java` and `azure-identity`; Spring AI 2 removed the old Azure OpenAI starter.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [How Authentication Works](#how-authentication-works)
- [Running the Application](#running-the-application)
  - [Using Maven](#using-maven)
  - [Using VS Code](#using-vs-code)
  - [Expected Output](#expected-output)
- [Configuration Reference](#configuration-reference)
  - [Environment Variables](#environment-variables)
  - [Spring Configuration](#spring-configuration)
- [Troubleshooting](#troubleshooting)
  - [Common Issues](#common-issues)
  - [Debug Mode](#debug-mode)
- [Next Steps](#next-steps)
- [Resources](#resources)

## Prerequisites

Before running this example, ensure you have:

- An Azure AI Foundry resource with a `gpt-5.6-luna` deployment - provision it with `azd up` or manually via the [Azure AI Foundry setup guide](../../getting-started-azure-openai.md)
- The **Cognitive Services OpenAI User** role on that resource (the Bicep templates assign this for you)
- The [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), signed in with `az login`
- Java 21+ and Maven 3.9+

> **No API key required** — authentication is keyless via Microsoft Entra ID.

## Quick Start

```bash
# 1. Navigate to project
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Sign in so keyless auth can get a token
az login

# 3. Configure the endpoint
#    - If you ran `azd up`, .env was written for you (skip this).
#    - Otherwise copy the template and set AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Run the application
mvn spring-boot:run
```

## How Authentication Works

This example authenticates with **Microsoft Entra ID** — there is no API key.

The application configures authentication explicitly in [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` creates a `BearerTokenCredential` using `AuthenticationUtil.getBearerTokenSupplier` with `DefaultAzureCredential` and the `https://ai.azure.com/.default` scope.
2. `azureOpenAiClient()` builds an `OpenAIClient` with `OpenAIOkHttpClient.builder()`, resolves the resource endpoint to `/openai/v1`, and supplies the bearer credential with `.credential(...)`.
3. `azureChatModel()` supplies that client to Spring AI's `OpenAiChatModel`, which backs the lesson's `ChatClient`.

These explicit beans keep a global `OPENAI_API_KEY` from overriding Azure authentication. Omitting an API key from YAML alone is not the authentication setup. `DefaultAzureCredential` can use your `az login` session locally or a managed identity in Azure; whichever identity is selected must have the resource role listed above.

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using VS Code

1. Open the project in VS Code
2. Press `F5` or use the "Run and Debug" panel
3. Select "Spring Boot-BasicChatApplication" configuration

> **Note**: The application loads `.env` from its working directory, including when launched from VS Code.

### Expected Output

Illustrative output after a successful run (startup logs omitted; response wording varies):

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

## Configuration Reference

### Environment Variables

| Variable | Description | Required | Example |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endpoint URL | Yes | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Chat model deployment name | No | `gpt-5.6-luna` (default) |

> There is **no** API key variable — authentication is keyless (Microsoft Entra ID via `az login`).

### Spring Configuration

The [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) settings use the `spring.ai.openai` prefix and flattened chat properties (no `options` block):

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

`model` is the **Azure deployment name**. Authentication comes from the explicit beans described above, not an `api-key` setting. The lesson disables reasoning and caps completion tokens at 500; it leaves `temperature` and the legacy `max-tokens` unset.

Microsoft recommends the [official OpenAI SDK with Azure OpenAI v1 and the Responses API for new applications](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions remains supported for this existing message-based lesson. For GPT-5.6, requests that include tools on Chat Completions must set `reasoning_effort` to `none`; use Responses when combining reasoning with tools. See [tool calling with reasoning models](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Troubleshooting

### Common Issues

<details>
<summary><strong>Error: 401 / "PermissionDenied" / token errors</strong></summary>

- Run `az login` — keyless auth needs an active sign-in to get a token
- Verify your account has the **Cognitive Services OpenAI User** role on the resource
- If you just assigned the role, wait a minute for it to propagate
- Confirm you're in the right tenant/subscription (`az account show`)
</details>

<details>
<summary><strong>Error: "The endpoint is not valid" / connection errors</strong></summary>

- Ensure `AZURE_OPENAI_ENDPOINT` is the full base URL (e.g., `https://your-resource.openai.azure.com/`)
- Check for trailing slash consistency
- Verify the endpoint matches your provisioned resource (`azd env get-values`)
</details>

<details>
<summary><strong>Error: "The deployment was not found"</strong></summary>

- Verify `AZURE_OPENAI_DEPLOYMENT` matches a deployment name in Azure
- Check that the model is successfully deployed and active
- The default deployment name is `gpt-5.6-luna`
</details>

<details>
<summary><strong>Error: 429 / rate limit exceeded</strong></summary>

- The default GPT-5.6 Luna deployment has Global Standard capacity 10: 10 requests/minute and 10,000 tokens/minute
- Run examples sequentially and wait for the service's retry interval before retrying
- This basic example disables automatic SDK retries, so a failed request is reported directly
</details>

<details>
<summary><strong>VS Code: Environment variables not loading</strong></summary>

- Ensure your `.env` file is in the project root directory (same level as `pom.xml`)
- Try running `mvn spring-boot:run` in VS Code's integrated terminal
- Check that the VS Code Java extension is properly installed
</details>

### Debug Mode

To enable detailed logging, uncomment these lines in [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Next Steps

**Setup Complete!** Continue your learning journey:

[Chapter 3: Core Generative AI Techniques](../../../03-CoreGenerativeAITechniques/README.md)

## Resources

- [Spring AI 2 OpenAI Java SDK transition](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Official OpenAI Java SDK with Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Keyless authentication with Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
This document has been translated using AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please be aware that automated translations may contain errors or inaccuracies. The original document in its native language should be considered the authoritative source. For critical information, professional human translation is recommended. We are not liable for any misunderstandings or misinterpretations arising from the use of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->