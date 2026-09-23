# Basic Chat wit Azure AI Foundry - End-to-End Example

Dis example na simple Spring Boot app wey connect to **Azure AI Foundry** model use **keyless authentication** (Microsoft Entra ID) and e test your setup. E dey use Spring AI `ChatClient`, wey dey supported by **official OpenAI Java SDK** and **Azure OpenAI v1** endpoint.

Versions wey dey for [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) na Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, and dotenv-java **3.2.0**. Di sample dey use `spring-ai-starter-model-openai` and explicit declare `openai-java` and `azure-identity`; Spring AI 2 comot the old Azure OpenAI starter.

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

Before you run dis example, make sure say you get:

- Azure AI Foundry resource wit `gpt-5.6-luna` deployment - fit create am wit `azd up` or manually wit di [Azure AI Foundry setup guide](../../getting-started-azure-openai.md)
- Di **Cognitive Services OpenAI User** role for dat resource (Bicep templates go assign am for you)
- Di [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), wey you don sign in wit `az login`
- Java 21+ and Maven 3.9+

> **No API key needed** — authentication na keyless via Microsoft Entra ID.

## Quick Start

```bash
# 1. Go waka go project
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Sign in make keyless auth fit get token
az login

# 3. Arrange the endpoint
#    - If you run `azd up`, .env don write for you (no need do am).
#    - If no be so, copy di template and set AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Run di application
mvn spring-boot:run
```

## How Authentication Works

Dis example dey authenticate wit **Microsoft Entra ID** — no API key dey.

Di app dey explicitly configure authentication for [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` dey create `BearerTokenCredential` use `AuthenticationUtil.getBearerTokenSupplier` wit `DefaultAzureCredential` and di `https://ai.azure.com/.default` scope.
2. `azureOpenAiClient()` dey build `OpenAIClient` wit `OpenAIOkHttpClient.builder()`, e come resolve di resource endpoint to `/openai/v1`, and e supply di bearer credential wit `.credential(...)`.
3. `azureChatModel()` dey supply dat client give Spring AI `OpenAiChatModel`, wey dey back di lesson `ChatClient`.

These explicit beans dey prevent global `OPENAI_API_KEY` from override Azure authentication. If you no put API key for YAML alone, e no be authentication setup. `DefaultAzureCredential` fit take your `az login` session locally or managed identity for Azure; whichever identity dem choose must get di resource role wey dem talk for above.

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using VS Code

1. Open di project for VS Code
2. Press `F5` or use di "Run and Debug" panel
3. Select "Spring Boot-BasicChatApplication" configuration

> **Note**: Di app go load `.env` from di work directory, including when you launch am from VS Code.

### Expected Output

Sample output after e run well (startup logs no dey show; response fit vary):

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

| Variable | Explanation | Required | Example |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) endpoint URL | Yes | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Chat model deployment name | No | `gpt-5.6-luna` (default) |

> No API key variable dey — authentication na keyless (Microsoft Entra ID via `az login`).

### Spring Configuration

Di [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) settings use `spring.ai.openai` prefix and flatten chat properties (no `options` block):

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

`model` na **Azure deployment name**. Authentication dey come from explicit beans wey dem describe above, no be `api-key` setting. Di lesson disable reasoning and set completion tokens limit to 500; e leave `temperature` and di old `max-tokens` no set.

Microsoft recommend di [official OpenAI SDK wit Azure OpenAI v1 and di Responses API for new applications](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions still dey supported for dis old message-based lesson. For GPT-5.6, requests wey get tools for Chat Completions must set `reasoning_effort` to `none`; use Responses if you wan combine reasoning with tools. See [tool calling with reasoning models](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Troubleshooting

### Common Issues

<details>
<summary><strong>Error: 401 / "PermissionDenied" / token errors</strong></summary>

- Run `az login` — keyless auth need active sign-in before e fit get token
- Check say your account get **Cognitive Services OpenAI User** role for the resource
- If you just assign di role, wait small time make e propagate
- Confirm say you dey di correct tenant/subscription (`az account show`)
</details>

<details>
<summary><strong>Error: "The endpoint is not valid" / connection errors</strong></summary>

- Make sure say `AZURE_OPENAI_ENDPOINT` na the full base URL (e.g., `https://your-resource.openai.azure.com/`)
- Check for consistent trailing slash
- Verify say di endpoint match your resource wey you provision (`azd env get-values`)
</details>

<details>
<summary><strong>Error: "The deployment was not found"</strong></summary>

- Verify say `AZURE_OPENAI_DEPLOYMENT` match deployment name for Azure
- Confirm say di model deploy finish and e dey active
- Default deployment name na `gpt-5.6-luna`
</details>

<details>
<summary><strong>Error: 429 / rate limit exceeded</strong></summary>

- Default GPT-5.6 Luna deployment get Global Standard capacity 10: 10 requests/min and 10,000 tokens/min
- Run examples one by one and wait make service retry interval pass before you try again
- Dis basic example no enable automatic SDK retries, so if request fail, e go report direct
</details>

<details>
<summary><strong>VS Code: Environment variables no dey load</strong></summary>

- Make sure say your `.env` file dey inside di project root directory (same level wit `pom.xml`)
- Try run `mvn spring-boot:run` inside VS Code integrated terminal
- Confirm say VS Code Java extension dey properly install
</details>

### Debug Mode

To enable detailed logging, comment“These lines for [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Next Steps

**Setup Don Finish!** Continue your learning journey:

[Chapter 3: Core Generative AI Techniques](../../../03-CoreGenerativeAITechniques/README.md)

## Resources

- [Spring AI 2 OpenAI Java SDK transition](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Official OpenAI Java SDK wit Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Keyless authentication wit Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg make you know say automated translation fit get errors or mistakes. Di original document for dia own language na im be di correct source. For important info, make person wey sabi human translation do am. We no go responsible for any misunderstanding or wrong understanding wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->