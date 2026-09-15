# ਐਜ਼ਯੂਰ ਏਆਈ ਫਾਊਂਡਰੀ ਨਾਲ ਬੁਨਿਆਦੀ ਗੱਲਬਾਤ - ਅੰਤ ਤੋਂ ਅੰਤ ਤੱਕ ਉਦਾਹਰਣ

ਇਹ ਉਦਾਹਰਣ ਇੱਕ ਸਾਦਾ ਸਪ੍ਰਿੰਗ ਬੂਟ ਐਪਲੀਕੇਸ਼ਨ ਹੈ ਜੋ **ਮਾਈਕ੍ਰੋਸੋਫਟ ਏਂਟਰਾ ਆਈਡੀ** ਦੀ ਵੱਡੀ ਪ੍ਰਮਾਣਿਕਤਾ ਵਰਤ ਕੇ **ਐਜ਼ਯੂਰ ਏਆਈ ਫਾਊਂਡਰੀ** ਮਾਡਲ ਨਾਲ ਕਨੈਕਟ ਹੁੰਦਾ ਹੈ ਅਤੇ ਤੁਹਾਡੇ ਸੈਟਅਪ ਦੀ ਜਾਂਚ ਕਰਦਾ ਹੈ। ਇਹ ਸਪ੍ਰਿੰਗ ਏਆਈ ਦੇ `ChatClient` ਨੂੰ ਰੱਖਦਾ ਹੈ, ਜੋ **ਆਧਿਕਾਰਕ OpenAI ਜਾਵਾ SDK** ਅਤੇ **ਐਜ਼ਯੂਰ OpenAI v1** ਐਂਡਪੌਇੰਟ ਦੁਆਰਾ ਸਮਰਥਿਤ ਹੈ।

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) ਵਿੱਚ ਵਰਜ਼ਨ ਸਪ੍ਰਿੰਗ ਬੂਟ **4.1.1**, ਸਪ੍ਰਿੰਗ ਏਆਈ **2.0.1**, OpenAI ਜਾਵਾ **4.63.1**, ਐਜ਼ਯੂਰ ਇੰਡੈਂਟੀਟੀ **1.18.6**, ਅਤੇ dotenv-java **3.2.0** ਹਨ। ਨਮੂਨਾ `spring-ai-starter-model-openai` ਵਰਤਦਾ ਹੈ ਅਤੇ ਖੁੱਲ੍ਹ ਕੇ `openai-java` ਅਤੇ `azure-identity` ਦਰਸਾਉਂਦਾ ਹੈ; ਸਪ੍ਰਿੰਗ ਏਆਈ 2 ਨੇ ਪੁਰਾਣਾ ਐਜ਼ਯੂਰ OpenAI ਸਟਾਰਟਰ ਹਟਾ ਦਿੱਤਾ ਹੈ।

## ਸੂਚੀ ਸਿਰਲੇਖ

- [ਜਰੂਰੀਆਂ ਸ਼ਰਤਾਂ](#ਜਰੂਰੀਆਂ-ਸ਼ਰਤਾਂ)
- [ਜਲਦੀ ਸ਼ੁਰੂਆਤ](#ਜਲਦੀ-ਸ਼ੁਰੂਆਤ)
- [ਪ੍ਰਮਾਣਿਕਤਾ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ](#ਪ੍ਰਮਾਣਿਕਤਾ-ਕਿਵੇਂ-ਕੰਮ-ਕਰਦੀ-ਹੈ)
- [ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣਾ](#ਐਪਲੀਕੇਸ਼ਨ-ਚਲਾਉਣਾ)
  - [ਮੇਵਨ ਵਰਤ ਕੇ](#ਮੇਵਨ-ਵਰਤ-ਕੇ)
  - [VS ਕੋਡ ਵਰਤ ਕੇ](#vs-ਕੋਡ-ਵਰਤ-ਕੇ)
  - [ਉਮੀਦ ਕੀਤੀ ਨਤੀਜਾ](#ਉਮੀਦ-ਕੀਤੀ-ਨਤੀਜਾ)
- [ਸੰਰਚਨਾ ਸੰਦਰਭ](#ਸੰਰਚਨਾ-ਸੰਦਰਭ)
  - [ਮਾਹੌਲ ਵਾਲੇ ਬਦਲ](#ਮਾਹੌਲ-ਵਾਲੇ-ਬਦਲ)
  - [ਸਪ੍ਰਿੰਗ ਸੰਰਚਨਾ](#ਸਪ੍ਰਿੰਗ-ਸੰਰਚਨਾ)
- [ਸਮੱਸਿਆ ਸਮਾਧਾਨ](#ਸਮੱਸਿਆ-ਸਮਾਧਾਨ)
  - [ਆਮ ਮੁੱਦੇ](#ਆਮ-ਮੁੱਦੇ)
  - [ਡਿਬੱਗ ਮੋਡ](#ਡਿਬੱਗ-ਮੋਡ)
- [ਅਗਲੇ ਕਦਮ](#ਅਗਲੇ-ਕਦਮ)
- [ਸੰਸਾਧਨ](#ਸੰਸਾਧਨ)

## ਜਰੂਰੀਆਂ ਸ਼ਰਤਾਂ

ਇਸ ਉਦਾਹਰਣ ਨੂੰ ਚਲਾਉਣ ਤੋਂ ਪਹਿਲਾਂ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ:

- ਇੱਕ ਐਜ਼ਯੂਰ ਏਆਈ ਫਾਊਂਡਰੀ ਸਰੋਤ ਜਿਸ ਵਿੱਚ `gpt-5.6-luna` ਨਿਰਵੇਸ਼ਨ ਹੋਵੇ - ਇਸਨੂੰ `azd up` ਨਾਲ ਜਾਂ ਹੱਥਾਂ ਨਾਲ [ਐਜ਼ਯੂਰ ਏਆਈ ਫਾਊਂਡਰੀ ਸੈਟਅਪ ਗਾਈਡ](../../getting-started-azure-openai.md) ਦੁਆਰਾ ਪ੍ਰੋਵਿਜ਼ਨ ਕਰੋ
- ਉਸ ਸਰੋਤ 'ਤੇ **ਕੌਗਨਿਟਿਵ ਸਰਵਿਸਿਜ਼ OpenAI ਯੂਜ਼ਰ** ਭੂਮਿਕਾ (Bicep ਟੈਂਪਲੇਟ ਤੁਹਾਡੇ ਲਈ ਇਹ ਜ਼ਿੰਮੇਵਾਰ ਹੁੰਦੇ ਹਨ)
- [ਐਜ਼ਯੂਰ CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login` ਨਾਲ ਸਾਈਨ-ਇਨ ਕੀਤਾ ਹੋਇਆ
- ਜਾਵਾ 21+ ਅਤੇ ਮੇਵਨ 3.9+

> **ਕੋਈ API ਕੁੰਜੀ ਦੀ ਲੋੜ ਨਹੀਂ** — ਪ੍ਰਮਾਣਿਕਤਾ ਮਾਈਕ੍ਰੋਸੋਫਟ ਏਂਟਰਾ ਆਈਡੀ ਦੁਆਰਾ ਕੁੰਜੀ-ਰਹਿਤ ਹੁੰਦੀ ਹੈ।

## ਜਲਦੀ ਸ਼ੁਰੂਆਤ

```bash
# 1. ਪ੍ਰੋਜੈਕਟ ਵੱਲ ਜਾਓ
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. ਸਾਈਨ ਇਨ ਕਰੋ ਤਾਂ ਜੋ ਕੀਲੈੱਸ ਪਰਮਾਣਿਕਤਾ ਟੋਕਨ ਲੈ ਸਕੇ
az login

# 3. ਐਂਡਪਾਇੰਟ ਸੈੱਟ ਕਰੋ
#    - ਜੇ ਤੁਸੀਂ `azd up` ਚਲਾਇਆ ਸੀ, ਤਾਂ .env ਤੁਹਾਡੇ ਲਈ ਲਿਖ ਦਿੱਤਾ ਗਿਆ ਸੀ (ਇਸਨੂੰ ਛੱਡੋ).
#    - ਨਹੀਂ ਤਾਂ ਟੈਂਪਲੇਟ ਕਾਪੀ ਕਰਕੇ AZURE_OPENAI_ENDPOINT ਸੈੱਟ ਕਰੋ:
cp .env.example .env

# 4. ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਓ
mvn spring-boot:run
```

## ਪ੍ਰਮਾਣਿਕਤਾ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀ ਹੈ

ਇਹ ਉਦਾਹਰਣ **ਮਾਈਕ੍ਰੋਸੋਫਟ ਏਂਟਰਾ ਆਈਡੀ** ਨਾਲ ਪ੍ਰਮਾਣਿਤ ਹੁੰਦੀ ਹੈ — ਕੋਈ API ਕੁੰਜੀ ਨਹੀਂ ਹੈ।

ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਪ੍ਰਮਾਣਿਕਤਾ ਨੂੰ ਖੁੱਲ੍ਹ ਕੇ [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) ਵਿੱਚ ਕਨਫਿਗਰ ਕੀਤਾ ਗਿਆ ਹੈ:

1. `azureCredential()` `AuthenticationUtil.getBearerTokenSupplier` ਨਾਲ `DefaultAzureCredential` ਅਤੇ `https://ai.azure.com/.default` ਸਕੋਪ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਇੱਕ `BearerTokenCredential` ਬਣਾਉਂਦਾ ਹੈ।
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()` ਨਾਲ ਇੱਕ `OpenAIClient` ਤਿਆਰ ਕਰਦਾ ਹੈ, ਸਰੋਤ ਐਂਡਪੌਇੰਟ ਨੂੰ `/openai/v1` 'ਤੇ ਹੱਲ ਕਰਦਾ ਹੈ, ਅਤੇ `.credential(...)` ਨਾਲ ਬੀਅਰਰ ਪ੍ਰਮਾਣ ਪੱਤਰ ਦੇਂਦਾ ਹੈ।
3. `azureChatModel()` ਉਸ ਕਲਾਇੰਟ ਨੂੰ ਸਪ੍ਰਿੰਗ ਏਆਈ ਦੇ `OpenAiChatModel` ਨੂੰ ਦਿੰਦਾ ਹੈ, ਜੋ ਸਬਕ ਦੇ `ChatClient` ਦਾ ਸਹਾਰਾ ਹੈ।

ਇਹ ਖੁੱਲ੍ਹੇ ਬੀਨਜ਼ ਇੱਕ ਗਲੋਬਲ `OPENAI_API_KEY` ਨੂੰ ਐਜ਼ਯੂਰ ਪ੍ਰਮਾਣਿਕਤਾ ਲਈ ਓਵਰਰਾਈਡ ਹੋਣ ਤੋਂ ਰੋਕਦੇ ਹਨ। ਸਿਰਫ YAML ਵਿੱਚੋਂ API ਕੁੰਜੀ ਛੱਡਣ ਨਾਲ ਪ੍ਰਮਾਣਿਕਤਾ ਸੈਟਅਪ ਨਹੀਂ ਹੁੰਦਾ। `DefaultAzureCredential` ਤੁਹਾਡੇ ਸਥਾਨਕ `az login` ਸੈਸ਼ਨ ਜਾਂ ਐਜ਼ਯੂਰ ਵਿੱਚ ਪ੍ਰਬੰਧਿਤ ਪਛਾਣ ਦੀ ਵਰਤੋਂ ਕਰ ਸਕਦਾ ਹੈ; ਚਾਹੇ ਜੋ ਪਛਾਣ ਚੁਣੀ ਜਾਵੇ, ਉਸ ਨੂੰ ਉਪਰ ਦਿੱਤਾ ਸਰੋਤ ਭੂਮਿਕਾ ਹੋਣੀ ਚਾਹੀਦੀ ਹੈ।

## ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣਾ

### ਮੇਵਨ ਵਰਤ ਕੇ

```bash
mvn spring-boot:run
```

### VS ਕੋਡ ਵਰਤ ਕੇ

1. ਪ੍ਰੋਜੈਕਟ ਨੂੰ VS ਕੋਡ ਵਿੱਚ ਖੋਲ੍ਹੋ
2. `F5` ਦਬਾਓ ਜਾਂ "ਰੰਨ ਅਤੇ ਡਿਬੱਗ" ਪੈਨਲ ਵਰਤੋ
3. "Spring Boot-BasicChatApplication" ਸੰਰਚਨਾ ਚੁਣੋ

> **ਨੋਟ**: ਐਪਲੀਕੇਸ਼ਨ ਆਪਣੀ ਕੰਮ ਕਰਨ ਦੀ ਡਾਇਰੈਕਟਰੀ ਤੋਂ `.env` ਲੋਡ ਕਰਦੀ ਹੈ, ਜਿਸ ਵਿੱਚ VS ਕੋਡ ਤੋਂ ਚਾਲੂ ਕਰਨ ਸਮੇਂ ਵੀ ਸ਼ਾਮਲ ਹੈ।

### ਉਮੀਦ ਕੀਤੀ ਨਤੀਜਾ

ਸਫਲ ਚਲਾਉਣ ਦੇ ਬਾਅਦ ਦਾ ਪ੍ਰਤੀਕਾਤਮਕ ਨਤੀਜਾ (ਸਟਾਰਟਅਪ ਲਾਗ ਛੱਡੇ ਗਏ ਹਨ; ਜਵਾਬ ਦੇਸ਼ਵਾਕ ਵੱਖਰੇ ਹੋ ਸਕਦੇ ਹਨ):

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

## ਸੰਰਚਨਾ ਸੰਦਰਭ

### ਮਾਹੌਲ ਵਾਲੇ ਬਦਲ

| ਵੈਰੀਏਬਲ | ਵਰਣਨ | ਜ਼ਰੂਰੀ | ਉਦਾਹਰਣ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | ਫਾਊਂਡਰੀ (ਐਜ਼ਯੂਰ OpenAI) ਐਂਡਪੌਇੰਟ URL | ਹਾਂ | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | ਗੱਲਬਾਤ ਮਾਡਲ ਨਿਰਵੇਸ਼ਨ ਨਾਮ | ਨਾ | `gpt-5.6-luna` (ਮੂਲ) |

> **ਕੋਈ ਵੀ** API ਕੁੰਜੀ ਵੈਰੀਏਬਲ ਨਹੀਂ ਹੈ — ਪ੍ਰਮਾਣਿਕਤਾ ਕੁੰਜੀ-ਰਹਿਤ (ਮਾਈਕ੍ਰੋਸੋਫਟ ਏਂਟਰਾ ਆਈਡੀ ਦੁਆਰਾ `az login`) ਹੈ।

### ਸਪ੍ਰਿੰਗ ਸੰਰਚਨਾ

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ਸੈਟਿੰਗਜ਼ `spring.ai.openai` ਪ੍ਰੀਫਿਕਸ ਅਤੇ ਸਧਾਰਨ ਗੱਲਬਾਤ ਪ੍ਰਾਪਰਟੀਜ਼ ਵਰਤਦੀਆਂ ਹਨ (ਕੋਈ `options` ਬਲਾਕ ਨਹੀਂ):

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

`model` **ਐਜ਼ਯੂਰ ਪ੍ਰੋਵਿਜ਼ਨ ਨਾਮ** ਹੈ। ਪ੍ਰਮਾਣਿਕਤਾ ਉਪਰ ਦਿੱਤੇ ਖੁੱਲ੍ਹੇ ਬੀਨਜ਼ ਤੋਂ ਆਉਂਦੀ ਹੈ, ਨਾ ਕਿ `api-key` ਸੈਟਿੰਗ ਤੋਂ। ਸਬਕ ਵਿਚ ਤਰਕਸ਼ੀਲਤਾ ਬੰਦ ਹੈ ਅਤੇ ਪੂਰਨਤਾ ਟੋਕਨਾਂ ਨੂੰ 500 'ਤੇ ਸੀਮਿਤ ਕੀਤਾ ਗਿਆ ਹੈ; ਇਹ `temperature` ਅਤੇ ਪੁਰਾਣੀ `max-tokens` ਨੂੰ ਅਣਸੈਟ ਛੱਡਦਾ ਹੈ।

ਮਾਈਕ੍ਰੋਸੋਫਟ [ਨਵੇਂ ਐਪਲੀਕੇਸ਼ਨਾਂ ਲਈ ਆਧਿਕਾਰਕ OpenAI SDK ਨਾਲ ਐਜ਼ਯੂਰ OpenAI v1 ਅਤੇ Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) ਦੀ ਸਿਫਾਰਸ਼ ਕਰਦਾ ਹੈ। ਗੱਲਬਾਤ ਪੂਰਨਤਾ ਇਸ ਮੌਜੂਦਾ ਸੁਨੇਹਾ-ਆਧਾਰਤ ਸਬਕ ਲਈ ਲਾਗੂ ਹੈ। GPT-5.6 ਲਈ, ਗੱਲਬਾਤ ਪੂਰਨਤਾਵਾਂ 'ਤੇ ਟੂਲ ਸ਼ਾਮਲ ਕਰਨ ਵਾਲਿਆਂ ਨੂੰ `reasoning_effort` ਨੂੰ `none` ਤੇ ਸੈੱਟ ਕਰਨਾ ਚਾਹੀਦਾ ਹੈ; ਤਰਕਸ਼ੀਲਤਾ ਨੂੰ ਟੂਲਾਂ ਨਾਲ ਮਿਲਾਉਂਦੇ ਸਮੇਂ Responses ਵਰਤੋ। ਦੇਖੋ [ਤਰਕਸ਼ੀਲ ਮਾਡਲਾਂ ਨਾਲ ਟੂਲ ਕਾਲਿੰਗ](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)।

## ਸਮੱਸਿਆ ਸਮਾਧਾਨ

### ਆਮ ਮੁੱਦੇ

<details>
<summary><strong>ਤ੍ਰੁੱਟੀ: 401 / "PermissionDenied" / ਟੋਕਨ ਤ੍ਰੁੱਟੀਆਂ</strong></summary>

- `az login` ਚਲਾਓ — ਕੁੰਜੀ-ਰਹਿਤ ਪ੍ਰਮਾਣਿਕਤਾ ਨੂੰ ਟੋکن ਲੈਣ ਲਈ ਸਾਈਨ-ਇਨ ਕੀਤਾ ਗਿਆ ਹੋਣਾ ਜ਼ਰੂਰੀ ਹੈ
- ਯਕੀਨੀ ਬਣਾਓ ਤੁਹਾਡੇ ਖਾਤੇ ਕੋਲ ਉਸ ਸਰੋਤ 'ਤੇ **ਕੌਗਨਿਟਿਵ ਸਰਵਿਸਿਜ਼ OpenAI ਯੂਜ਼ਰ** ਭੂਮਿਕਾ ਹੈ
- ਜੇ ਤੁਸੀਂ ਭੂਮਿਕਾ ਅਜਿਹਾ ਅਸਾਈਨ ਕੀਤੀ ਹੈ, ਤਾਂ ਇਸਦੇ ਫੈਲਣ ਲਈ ਇੱਕ ਮਿੰਟ ਇੰਤਜ਼ਾਰ ਕਰੋ
- ਪੁਸ਼ਟੀ ਕਰੋ ਕਿ ਤੁਸੀਂ ਸਹੀ ਟੈਨੈਂਟ/ਸਬਸਕ੍ਰਿਪਸ਼ਨ ਵਿੱਚ ਹੋ (`az account show`)
</details>

<details>
<summary><strong>ਤ੍ਰੁੱਟੀ: "ਐਂਡਪੌਇੰਟ ਵੈਧ ਨਹੀਂ ਹੈ" / ਕਨੈਕਸ਼ਨ ਤ੍ਰੁੱਟੀਆਂ</strong></summary>

- ਯਕੀਨੀ ਬਣਾਓ `AZURE_OPENAI_ENDPOINT` ਪੂਰੀ ਬੇਸ URL ਹੈ (ਜਿਵੇਂ ਕਿ `https://your-resource.openai.azure.com/`)
- ਅੰਤ ਵਿੱਚ ਸਲੇਸ਼ ਦੀ ਸੰਗਤੀ ਦੀ ਜਾਂਚ ਕਰੋ
- ਪੁਸ਼ਟੀ ਕਰੋ ਕਿ ਐਂਡਪੌਇੰਟ ਤੁਹਾਡੇ ਪ੍ਰੋਵਿਜ਼ਨ ਕੀਤੇ ਸਰੋਤ ਨਾਲ ਮੇਲ ਖਾਂਦਾ ਹੈ (`azd env get-values`)
</details>

<details>
<summary><strong>ਤ੍ਰੁੱਟੀ: "ਨਿਰਵੇਸ਼ਨ ਨਹੀਂ ਲੱਭਿਆ ਗਿਆ"</strong></summary>

- ਯਕੀਨੀ ਬਣਾਓ `AZURE_OPENAI_DEPLOYMENT` ਐਜ਼ਯੂਰ ਵਿੱਚ ਕਿਸੇ ਨਿਰਵੇਸ਼ਨ ਨਾਮ ਨਾਲ ਮੇਲ ਖਾਂਦਾ ਹੈ
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਮਾਡਲ ਸਫਲਤਾਪੂਰਕ ਨਿਰਵੇਸ਼ਤ ਅਤੇ ਸਰਗਰਮ ਹੈ
- ਮੂਲ ਨਿਰਵੇਸ਼ਨ ਨਾਮ `gpt-5.6-luna` ਹੈ
</details>

<details>
<summary><strong>ਤ੍ਰੁੱਟੀ: 429 / ਦਰ ਸੀਮਾ ਲੰਘ ਗਈ</strong></summary>

- ਮੂਲ GPT-5.6 ਲੂਨਾ ਨਿਰਵੇਸ਼ਨ ਕੋਲ ਗਲੋਬਲ ਮਿਆਰੀ ਸਮਰੱਥਾ 10 ਹੈ: 10 ਬੇਨਤੀਆਂ/ਮਿੰਟ ਅਤੇ 10,000 ਟੋਕਨ/ਮਿੰਟ
- ਉਦਾਹਰਣਾਂ ਨੂੰ ਕ੍ਰਮ ਵਿੱਚ ਚਲਾਓ ਅਤੇ ਸੇਵਾ ਦੇ ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਅੰਤਰਾਲ ਲਈ ਇੰਤਜ਼ਾਰ ਕਰੋ
- ਇਹ ਬੁਨਿਆਦੀ ਉਦਾਹਰਨ ਸਵੈਚਾਲਿਤ SDK ਦੁਬਾਰਾ ਕੋਸ਼ਿਸ਼ ਨੂੰ ਬੰਦ ਕਰਦੀ ਹੈ, ਇਸ ਲਈ ਅਸਫਲ ਬੇਨਤੀ ਸਿੱਧਾ ਦੱਸਾਤੀ ਜਾ ਰਹੀ ਹੈ
</details>

<details>
<summary><strong>VS ਕੋਡ: ਮਾਹੌਲ ਵਾਲੇ ਬਦਲ ਲੋਡ ਨਹੀਂ ਹੋ ਰਹੇ</strong></summary>

- ਯਕੀਨੀ ਬਣਾਓ ਤੁਹਾਡੀ `.env` ਫਾਈਲ ਪ੍ਰੋਜੈਕਟ ਰੂਟ ਡਾਇਰੈਕਟਰੀ ਵਿੱਚ ਹੈ (`pom.xml` ਦੇ ਸਮਾਨ ਸਤਰ)
- VS ਕੋਡ ਦੇ ਇੰਟੀਗਰੇਟਿਡ ਟਰਮੀਨਲ ਵਿੱਚ `mvn spring-boot:run` ਚਲਾਉਣ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ
- ਜਾਂਚੋ ਕਿ VS ਕੋਡ ਜਾਵਾ ਐਕਸਟੈਂਸ਼ਨ ਸਹੀ ਤਰੀਕੇ ਨਾਲ ਇੰਸਟਾਲ ਹੋਇਆ ਹੈ
</details>

### ਡਿਬੱਗ ਮੋਡ

ਵਿਸਥਾਰਿਤ ਲੌਗਿੰਗ ਲਈ, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ਵਿੱਚ ਇਹ ਲਾਈਨਾਂ ਅਣਕਮੈਂਟ ਕਰੋ:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## ਅਗਲੇ ਕਦਮ

**ਸੈਟਅਪ ਪੂਰਾ!** ਆਪਣੀ ਸਿੱਖਣ ਦੀ ਯਾਤਰਾ ਜਾਰੀ ਰੱਖੋ:

[ਅਧਿਆਇ 3: ਕੋਰ ਜਨਰੇਟਿਵ ਏਆਈ ਤਕਨੀਕਾਂ](../../../03-CoreGenerativeAITechniques/README.md)

## ਸੰਸਾਧਨ

- [ਸਪ੍ਰਿੰਗ ਏਆਈ 2 OpenAI ਜਾਵਾ SDK ਤਬਦੀਲੀ](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [ਆਧਿਕਾਰਕ OpenAI ਜਾਵਾ SDK ਨਾਲ ਐਜ਼ਯੂਰ OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [ਮਾਈਕ੍ਰੋਸੋਫਟ ਏਂਟਰਾ ਆਈਡੀ ਨਾਲ ਕੁੰਜੀ-ਰਹਿਤ ਪ੍ਰਮਾਣਿਕਤਾ](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [ਐਜ਼ਯੂਰ ਏਆਈ ਫਾਊਂਡਰੀ ਪੋਰਟਲ](https://ai.azure.com/)
- [ਐਜ਼ਯੂਰ ਏਆਈ ਫਾਊਂਡਰੀ ਡੌਕਯੂਮੇਨਟੇਸ਼ਨ](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਵੀਕਾਰੋਪਣ**:
ਇਸ ਦਸਤਾਵੇਜ਼ ਦਾ ਅਨੁਵਾਦ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾਵਾਂ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਮੱਤਿਆਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੇ ਉਪਯੋਗ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਜਵਾਬਦੇਹ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->