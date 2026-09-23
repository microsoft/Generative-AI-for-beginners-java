# ಅಝುರ್ AI ಫೌಂಡ್ರಿಯೊಂದಿಗೆ ಮೂಲಭೂತ ಚಾಟ್ - ಎಂಡ್-ಟು-ಎಂಡ್ ಉದಾಹರಣೆ

ಈ ಉದಾಹರಣೆ ಒಂದು ಸರಳ ಸ್ಪ್ರಿಂಗ್ ಬೂಟ್ ಅಪ್ಲಿಕೇಶನ್ ಆಗಿದ್ದು, ಅದು **ಅಝುರ್ AI ಫೌಂಡ್ರಿ** ಮಾದರಿಯನ್ನು **ಕೀಲೆಸ್ ಪ್ರಮಾಣೀಕರಣ** (Microsoft Entra ID) ಬಳಸಿ ಸಂಪರ್ಕಿಸುತ್ತದೆ ಮತ್ತು ನಿಮ್ಮ ಸೆಟಪ್ ಅನ್ನು ತಪಾಸಣೆ ಮಾಡುತ್ತದೆ. ಇದು ಸ್ಪ್ರಿಂಗ್ AI ನ `ChatClient` ಅನ್ನು ಬಳಸುತ್ತದೆ, ಇದು **ಅಧಿಕೃತ OpenAI ಜಾವಾ SDK** ಮತ್ತು **ಅಝುರ್ OpenAI v1** ಎಂಡ್ಪಾಯಿಂಟ್ ನೊಂದಿಗೆ ಬೆಕಪ್ ಆಗಿದೆ.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) ನಲ್ಲಿ ಅವುಗಳ ವರ್ಜನ್‌ಗಳು ಸ್ಪ್ರಿಂಗ್ ಬೂಟ್ **4.1.1**, ಸ್ಪ್ರಿಂಗ್ AI **2.0.1**, OpenAI ಜಾವಾ **4.63.1**, ಅಝುರ್ ಐಡೆಂಟಿಟಿ **1.18.6**, ಮತ್ತು dotenv-javase **3.2.0** ಆಗಿವೆ. ಈ ಮಾದರಿಯಲ್ಲಿ `spring-ai-starter-model-openai` ಬಳಸಲ್ಪಟ್ಟಿದ್ದು ಸ್ಪಷ್ಟವಾಗಿ `openai-java` ಮತ್ತು `azure-identity` ಅನ್ನು ಘೋಷಿಸಲಾಗಿದೆ; ಸ್ಪ್ರಿಂಗ್ AI 2 ಹಳೆಯ ಅಝುರ್ OpenAI ಸ್ಟಾರ್ಟರ್ ಅನ್ನು ತೆಗೆದುಹಾಕಿದೆ.

## ವಿಷಯಪಟ್ಟಿ

- [ಆವಶ್ಯಕತೆಗಳು](#ಆವಶ್ಯಕತೆಗಳು)
- [ವೇಗವಾಗಿ ಪ್ರಾರಂಭಿಸುವುದು](#ವೇಗವಾಗಿ-ಪ್ರಾರಂಭಿಸುವುದು)
- [ಪ್ರಮಾಣೀಕರಣ ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ](#ಪ್ರಮಾಣೀಕರಣ-ಹೇಗೆ-ಕೆಲಸ-ಮಾಡುತ್ತದೆ)
- [ಅಪ್ಲಿಕೇಶನ್ ಚಾಲನೆ](#ಅಪ್ಲಿಕೇಶನ್-ಚಾಲನೆ)
  - [ಮೇವನ್ ಬಳಸಿ](#ಮೇವನ್-ಬಳಸಿ)
  - [VS ಕೋಡ್ ಬಳಸಿ](#vs-ಕೋಡ್-ಬಳಸಿ)
  - [ನಿರೀಕ್ಷಿತ ಔಟ್‍ಪುಟ್](#ನಿರೀಕ್ಷಿತ-ಔಟ್‍ಪುಟ್)
- [ಕಾನ್ಫಿಗರೇಶನ್ ರೆಫರೆನ್ಸ್](#ಕಾನ್ಫಿಗರೇಶನ್-ರೆಫರೆನ್ಸ್)
  - [ಪರಿಸರ ಚಾಲಕ変量ಗಳು](#ಪರಿಸರ-ಚಾಲಕ変量ಗಳು)
  - [ಸ್ಪ್ರಿಂಗ್ ಕಾನ್ಫಿಗರೇಶನ್](#ಸ್ಪ್ರಿಂಗ್-ಕಾನ್ಫಿಗರೇಶನ್)
- [ದೋಷ ಪರಿಹಾರ](#ದೋಷ-ಪರಿಹಾರ)
  - [ಸಾಮಾನ್ಯ ಸಮಸ್ಯೆಗಳು](#ಸಾಮಾನ್ಯ-ಸಮಸ್ಯೆಗಳು)
  - [ಡೆಬಗ್ಗಿಂಗ್ ಮೋಡ್](#ಡಿಬੱਗಿಂಗ್-ಮೋಡ್)
- [ಮುಂದಿನ ಹಂತಗಳು](#ಮುಂದಿನ-ಹಂತಗಳು)
- [ಸಂಪನ್ಮೂಲಗಳು](#ಸಂಪನ್ಮೂಲಗಳು)

## ಆವಶ್ಯಕತೆಗಳು

ಈ ಉದಾಹರಣೆ ಚಲಾಯಿಸಲು ಮುನ್ನ, ನೀವು ಇವುಗಳನ್ನು ಹೊಂದಿರಬೇಕು:

- `gpt-5.6-luna` ನಿಯೋಜನೆಯೊಂದಿಗೆ ಅಝುರ್ AI ಫೌಂಡ್ರಿ ಸಂಪನ್ಮೂಲ - ಇದನ್ನು `azd up` ಮೂಲಕ ಅಥವಾ ಕೈತಡೆದಂತೆ [ಅಝುರ್ AI ಫೌಂಡ್ರಿ ಸೆಟಪ್ ಮಾರ್ಗದರ್ಶಿ](../../getting-started-azure-openai.md) ಬಳಸಿ ಪ್ರಾರಂಭಿಸಿ
- ಆ ಸಂಪನ್ಮೂಲದಲ್ಲಿ **Cognitive Services OpenAI User** ಪಾತ್ರ (ಬೈಸಿಪ್ ಟೆಂಪ್ಲೇಟುಗಳು ಇದನ್ನು ನಿಮಗಾಗಿ ನಿಯೋಜಿಸುತ್ತವೆ)
- [ಅಝುರ್ CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login` ಮೂಲಕ ಸೈನ್ ಇನ್ ಆಗಿರಬೇಕು
- ಜಾವಾ 21+ ಮತ್ತು ಮೇವನ್ 3.9+

> **ಯಾವುದೇ API ಕೀ ಅಗತ್ಯವಿಲ್ಲ** — ಪ್ರಮಾಣೀಕರಣ Microsoft Entra ID ಮೂಲಕ ಕೀಲೆಸ್ ಆಗಿದೆ.

## ವೇಗವಾಗಿ ಪ್ರಾರಂಭಿಸುವುದು

```bash
# 1. ಪ್ರಾಜೆಕ್ಟ್ ಗೆ ನ್ಯಾವಿಗೇಟ್ ಮಾಡಿ
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. ಕೀಲಿಸಹಿತ ಅನುಮತಿ ಟೋಕನ್ ಪಡೆಯಲು ಸೈನ್ ಇನ್ ಆಗಿ
az login

# 3. ಎಂಡ್‌ಪಾಯಿಂಟ್ ಅನ್ನು ಸಂರಚಿಸಿ
#    - ನೀವು `azd up` ನಡಿಸಿದ್ದರೆ, .env ನಿಮ್ಮಗಾಗಿ ಬರೆಯಲಾಗಿದೆ (ಇದನ್ನು ದಾಟಿ ಹೋಗಿ).
#    - ಇಲ್ಲದಿದ್ದರೆ ಟೆಂಪ್ಲೇಟ್ ನಕಲಿಸಿ ಮತ್ತು AZURE_OPENAI_ENDPOINT ಅನ್ನು ಹೊಂದಿಸಿ:
cp .env.example .env

# 4. ಅಪ್ಲಿಕೇಶನ್ ಓಡಿಸಿ
mvn spring-boot:run
```

## ಪ್ರಮಾಣೀಕರಣ ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತದೆ

ಈ ಉದಾಹರಣೆ **Microsoft Entra ID** ಜೊತೆ ಪ್ರಮಾಣೀಕೃತವಾಗಿದೆ — ಯಾವುದೇ API ಕೀ ಇಲ್ಲ.

ಅಪ್ಲಿಕೇಶನ್ ಸ್ಪಷ್ಟವಾಗಿ [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) ನಲ್ಲಿ ಪ್ರಮಾಣೀಕರಣವನ್ನು ಕಾನ್ಫಿಗರ್ ಮಾಡುತ್ತದೆ:

1. `azureCredential()` ಒಂದು `BearerTokenCredential` ಅನ್ನು `AuthenticationUtil.getBearerTokenSupplier` ಬಳಸಿ `DefaultAzureCredential` ಮತ್ತು `https://ai.azure.com/.default` ವಿಸ್ತಾರದಿಂದ ರಚಿಸುತ್ತದೆ.
2. `azureOpenAiClient()` `OpenAIClient` ಅನ್ನು `OpenAIOkHttpClient.builder()` ಬಳಸಿ ತಯಾರಿಸಿ, ಸಂಪನ್ಮೂಲ ಎಂಡ್ಪಾಯಿಂಟ್ ಅನ್ನು `/openai/v1` ಗೆ ಪರಿಹರಿಸುತ್ತದೆ ಮತ್ತು `.credential(...)` ಮೂಲಕ ಬೇರರ್ ಕ್ರೆಡenciais್ ಒದಗಿಸುತ್ತದೆ.
3. `azureChatModel()` ಆ ಕ್ಲೈಂಟ್ ಅನ್ನು ಸ್ಪ್ರಿಂಗ್ AI ನ `OpenAiChatModel` ಗೆ ಒದಗಿಸುತ್ತದೆ, ಇದು ಪಾಠದ `ChatClient` ನನ್ನು ಬೆಂಬಲಿಸುತ್ತದೆ.

ಈ ಸ್ಪಷ್ಟವಾದ ಬೀನ್ಸ್ `OPENAI_API_KEY` ಯನ್ನು ಅಝುರ್ ಪ್ರಮಾಣೀಕರಣದಿಂದ ಮೀರಿಸುವುದನ್ನು ತಡೆಯುತ್ತವೆ. YAML ನಲ್ಲಿ ನುಡಿದ API ಕೀ ಮೇಲ್ವಿಚಾರಣೆ, ಪ್ರಮಾಣೀಕರಣ ಸೆಟಪ್ ಅಲ್ಲ. `DefaultAzureCredential` ನಿಮ್ಮ `az login` ಸೆಷನ್ ಅನ್ನು ಸ್ಥಳೀಯವಾಗಿ ಅಥವಾ ಅಝುರ್ ನಲ್ಲಿ ನಿರ್ವಹಣಾ ಪರಿಚಯವನ್ನು ಬಳಸಬಹುದು; ಆಯ್ದ ಯಾವುದೇ ಪರಿಚಯವು ಮೇಲ್ಕಂಡ ಸಂಪನ್ಮೂಲ ಪಾತ್ರವನ್ನು ಹೊಂದಿರಬೇಕು.

## ಅಪ್ಲಿಕೇಶನ್ ಚಾಲನೆ

### ಮೇವನ್ ಬಳಸಿ

```bash
mvn spring-boot:run
```

### VS ಕೋಡ್ ಬಳಸಿ

1. ಪ್ರಾಜೆಕ್ಟ್ ಅನ್ನು VS ಕೋಡ್ ನಲ್ಲಿ ತೆರೆಯಿರಿ
2. `F5` ಒತ್ತಿ ಅಥವಾ "ಚಲಾಯಿಸಿ ಮತ್ತು ಡಿಬಗ್ ಮಾಡಿ" ಫಲಕವನ್ನು ಬಳಸಿ
3. "Spring Boot-BasicChatApplication" ಕಾನ್ಫಿಗರೇಶನ್ ಆಯ್ಕೆಮಾಡಿ

> **ಉಪದೇಶ**: ಅಪ್ಲಿಕೇಶನ್ ತನ್ನ ಕೆಲಸದ ಡೈರೆಕ್ಟರಿ ನಿಂದ `.env` ಅನ್ನು ಲೋಡ್ ಮಾಡುತ್ತದೆ, VS ಕೋಡ್ ನಿಂದ ಚಾಲನೆಯಾಗುವಾಗ ಸಹ.

### ನಿರೀಕ್ಷಿತ ಔಟ್‍ಪುಟ್

ಯಶಸ್ವಿಯಾಗಿ ಚಾಲನೆಯಾದ ನಂತರದ ಚಿತ್ರಣಾತ್ಮಕ ಔಟ್‍ಪುಟ್ (ಸ್ಟಾರ್ಟ್ ಅಪ್ ಲಾಗ್‌ಗಳನ್ನು ಹೊರತುಪಡಿಸಿ; ಪ್ರತಿಕ್ರಿಯೆಯ ಪದಬಳಕೆ ಬದಲಾಗಬಹುದು):

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

## ಕಾನ್ಫಿಗರೇಶನ್ ರೆಫರೆನ್ಸ್

### ಪರಿಸರ ಚಾಲಕ変量ಗಳು

| ಚರ | ವಿವರಣೆ | ಅಗತ್ಯವಿದೆ | ಉದಾಹರಣೆ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | ಫೌಂಡ್ರಿ (ಅಝುರ್ OpenAI) ಎಂಡ್ಪಾಯಿಂಟ್ URL | ಹೌದು | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | ಚಾಟ್ ಮಾದರಿ ನಿಯೋಜನೆ ಹೆಸರು | ಇಲ್ಲ | `gpt-5.6-luna` (ಡಿಫಾಲ್ಟ್) |

> ಯಾವುದೇ API ಕೀ ಚಾಲಕ変量 ಇಲ್ಲ — ಪ್ರಮಾಣೀಕರಣವು ಕೀಲೆಸ್ (Microsoft Entra ID ಮೂಲಕ `az login`).

### ಸ್ಪ್ರಿಂಗ್ ಕಾನ್ಫಿಗರೇಶನ್

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ಸೆಟ್ಟಿಂಗ್‌ಗಳು `spring.ai.openai` ಪೂರ್ವವರ್ನನೆಯೊಂದಿಗೆ ಮತ್ತು ಸಮತಲಗೊಳಿಸಿದ ಚಾಟ್ ಗುಣಲಕ್ಷಣಗಳೊಂದಿಗೆ ಬಳಸಲ್ಪಟ್ಟಿವೆ (ಯಾವುದೇ `options` ಬ್ಲಾಕ್ ಇಲ್ಲ):

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

`model` ಅಂಶವು **ಅಝುರ್ ನಿಯೋಜನೆ ಹೆಸರು** ಆಗಿದೆ. ಪ್ರಮಾಣೀಕರಣ ಮೇಲ್ಕಂಡ ಸ್ಪಷ್ಟವಾದ ಬೀನ್ಸ್ ಮೂಲಕ ಬರುತ್ತದೆ, `api-key` ಸೆಟ್ಟಿಂಗ್ ಮೂಲಕ ಅಲ್ಲ. ಪಾಠದಲ್ಲಿ ಯುಕ್ತಿವಾದವನ್ನು ನಿಷ್ಕ್ರಿಯಗೊಳಿಸಲಾಗಿದ್ದು, ಪೂರ್ಣಗೊಳಿಸುವ ಟೋಕನ್ ಗಳು 500 ವರೆಗೆ ಮಿತಿ ಹೊಂದಿವೆ; `temperature` ಮತ್ತು ಪಾಂಪರಿಕ `max-tokens` ಮೌಲ್ಯಗಳನ್ನು ಬಿಟ್ಟುಬಿಟ್ಟಿದೆ.

Microsoft ಹೊಸ ಅಪ್ಲಿಕೇಶನ್ಗಳಿಗಾಗಿ [ಅಧಿಕೃತ OpenAI SDK ನೊಂದಿಗೆ ಅಝುರ್ OpenAI v1 ಮತ್ತು Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) ಅನ್ನು ಶಿಫಾರಸು ಮಾಡುತ್ತದೆ. ಈ ಸಂದೇಶ ಆಧಾರಿತ ಪಾಠಕ್ಕೆ ಚಾಟ್ ಪೂರ್ಣಗೊಳಿಕೆಗಳನ್ನು ಇನ್ನೂ ಬೆಂಬಲಿಸುತ್ತದೆ. GPT-5.6 ಗಾಗಿ, ಚಾಟ್ ಪೂರ್ಣಗೊಳಿಕೆಗಳಲ್ಲಿ ಉಪಕರಣಗಳು ಸೇರಿದ್ದರೆ `reasoning_effort` ಅನ್ನು `none` ಗೆ ಹೊಂದಿಸಬೇಕು; ಯುಕ್ತಿವಾದ ಮತ್ತು ಉಪಕರಣಗಳನ್ನು ಸಂಯೋಜಿಸಲು Responses ಬಳಸಿ. [ಯುಕ್ತಿವಾದ ಮಾದರಿಗಳೊಂದಿಗೆ ಉಪಕರಣ ಕರೆಗೆ](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models) ನೋಡಿ.

## ದೋಷ ಪರಿಹಾರ

### ಸಾಮಾನ್ಯ ಸಮಸ್ಯೆಗಳು

<details>
<summary><strong>ದೋಷ: 401 / "PermissionDenied" / ಟೋಕನ್ ದೋಷಗಳು</strong></summary>

- `az login` ಚಾಲನೆ ಮಾಡಿ — ಕೀಲೆಸ್ ಪ್ರಮಾಣೀಕರಣಕ್ಕೆ ಟೋಕನ್ ಪಡೆಯಲು ಸಕ್ರಿಯ ಸೈನ್ ಇನ್ ಅಗತ್ಯ
- ನಿಮ್ಮ ಖಾತೆಗೆ ಆ ಸಂಪನ್ಮೂಲದಲ್ಲಿ **Cognitive Services OpenAI User** ಪಾತ್ರ ಇದೆ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ
- ನೀವು ಇತ್ತೀಚಿಗೆ ಪಾತ್ರವನ್ನು ನಿಯೋಜಿಸಿದ್ದರೆ, ಅದು ಹರಡಲು ಒಂದು ನಿಮಿಷ ಕಾಯಿರಿ
- ನೀವು ಸರಿಯಾದ ಟ್ರೆನেন্ট್/ಚಂದಾದಾರಿಕೆಯಲ್ಲಿ ಇರುವುದನ್ನು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ (`az account show`)
</details>

<details>
<summary><strong>ದೋಷ: "ಎಂಡ್ಪಾಯಿಂಟ್ ಮಾನ್ಯವಿಲ್ಲ" / ಸಂಪರ್ಕದ ದೋಷಗಳು</strong></summary>

- `AZURE_OPENAI_ENDPOINT` ಸಂಪೂರ್ಣ ಮೂಲ URL ಆಗಿರಬೇಕು (ಉದಾ: `https://your-resource.openai.azure.com/`)
- ಕೊನೆಗಿನ ಸ್ಲಾಶ್ ಸुसಂಗತತೆಯನ್ನು ಪರಿಶೀಲಿಸಿ
- ಎಂಡ್ಪಾಯಿಂಟ್ ನಿಮ್ಮ ನಿಯೋಜಿಸಿದ ಸಂಪನ್ಮೂಲಕ್ಕೆ ಹೊಂದಿಕೆಯಾಗುತ್ತದೆಯೇ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ (`azd env get-values`)
</details>

<details>
<summary><strong>ದೋಷ: "ನಿಯೋಜನೆ ದೊರಕಲಿಲ್ಲ"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` ಅಝುರ್ ನಲ್ಲಿ ನಿಯೋಜನೆಯ ಹೆಸರಿಗೆ ಹೊಂದಿಕೊಳ್ಳಬೇಕು
- ಮಾದರಿಗೆ ಯಶಸ್ವಿಯಾಗಿ ನಿಯೋಜನೆ ಮತ್ತು ಸಕ್ರಿಯತೆ ಬಂದಿದೆ ಎಂಬುದನ್ನು ಪರಿಶೀಲಿಸಿ
- ಸ್ಥಿರ ನಿಯೋಜನೆಯ ಹೆಸರು `gpt-5.6-luna`
</details>

<details>
<summary><strong>ದೋಷ: 429 / ದರ ಮಿತಿಯನ್ನು ಮೀರುವುದು</strong></summary>

- ಡಿಫಾಲ್ಟ್ GPT-5.6 Luna ನಿಯೋಜನೆಗೆ ಜಾಗತಿಕ ಸ್ಟ್ಯಾಂಡರ್ಡ್ ಸಾಮರ್ಥ್ಯ 10: 10 ವಿನಂತಿಗಳು/ನಿಮಿಷ ಮತ್ತು 10,000 ಟೋಕನ್ಸ್/ನಿಮಿಷ
- ಉದಾಹರಣೆಗಳನ್ನು ಕ್ರಮವಾಗಿ ಚಲಾಯಿಸಿ ಮತ್ತು ಸೇವೆಯ ಮರುಪ್ರಯತ್ನ ಅಂತರವು ಮುಗಿಯುವವರೆಗೆ ಕಾಯಿರಿ
- ಈ ಮೂಲ ಉದಾಹರಣೆ ಸ್ವಯಂಚಾಲಿತ SDK ಮರುಪ್ರಯತ್ನಗಳನ್ನು ನಿಷೇಧಿಸುತ್ತದೆ ಹಾಗಾಗಿ ವಿಫಲವಾದ ವಿನಂತಿಯನ್ನು ನೇರವಾಗಿ ವರದಿ ಮಾಡುತ್ತದೆ
</details>

<details>
<summary><strong>VS ಕೋಡ್: ಪರಿಸರ ಚರಗಳು ಲೋಡಾಗುತ್ತಿಲ್ಲ</strong></summary>

- ನಿಮ್ಮ `.env` ಫೈಲ್ ಪ್ರಾಜೆಕ್ಟ್ ರೂಟ್ ಡೈರೆಕ್ಟರಿಯಲ್ಲಿ (pom.xml ಜೊತೆ ಸಮಮಟ್ಟದಲ್ಲಿದ್ದಾರೆ) ಇರಬೇಕು ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ
- VS ಕೋಡ್ ಒಂದುಗೂಡಿಸಲಾದ ಟರ್ಮಿನಲ್ ನಲ್ಲಿ `mvn spring-boot:run` ಅನ್ನು ಚಲಾಯಿಸಲು ಪ್ರಯತ್ನಿಸಿ
- VS ಕೋಡ್ ಜಾವಾ ವಿಸ್ತರಣೆ ಸರಿಯಾಗಿ ಸ್ಥಾಪಿತವಾಗಿದೆಯೇ ಎಂದು ಪರಿಶೀಲಿಸಿ
</details>

### ಡಿಬੱਗಿಂಗ್ ಮೋಡ್

ವಿವರವಾದ ಲಾಗಿಂಗ್ ಸಕ್ರಿಯಗೊಳಿಸಲು, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ನಲ್ಲಿ ಈ ಸಾಲುಗಳನ್ನು ಕಾಮೆಂಟ್ ತೆಗೆಯಿರಿ:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## ಮುಂದಿನ ಹಂತಗಳು

**ಸೆಟಪ್ ಸಂಪೂರ್ಣ!** ನಿಮ್ಮ ಅಧ್ಯಯನ ಪ್ರಯಾಣವನ್ನು ಮುಂದುವರೆಸಿರಿ:

[ಅಧ್ಯಾಯ 3: ಕೋರ್ ಜನರೇಟಿವ್ AI ತಂತ್ರಗಳು](../../../03-CoreGenerativeAITechniques/README.md)

## ಸಂಪನ್ಮೂಲಗಳು

- [ಸ್ಪ್ರಿಂಗ್ AI 2 OpenAI ಜಾವಾ SDK ಪರಿವರ್ತನೆ](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [ಅಧಿಕೃತ OpenAI ಜಾವಾ SDK ಅಝುರ್ OpenAI v1 ನೊಂದಿಗೆ](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID ಮೂಲಕ ಕೀಲೆಸ್ ಪ್ರಮಾಣೀಕರಣ](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [ಅಝುರ್ AI ಫೌಂಡ್ರಿ ಪೋರ್ಟಲ್](https://ai.azure.com/)
- [ಅಝುರ್ AI ಫೌಂಡ್ರಿ ಡಾಕ್ಯುಮೆಂಟೇಶನ್](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಅಸ್ವೀಕಾರ**:
ಈ ದಸ್ತಾವೇಜು AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವಾದಿಸಲಾಗಿದೆ. ನಾವು ನಿಖರತೆಯನ್ನು ಸಾಧಿಸಲು ಪ್ರಯತ್ನಿಸುತ್ತಿದ್ದರೂ, ದಯವಿಟ್ಟು ಗಮನಿಸಿ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ದೋಷಗಳು ಅಥವಾ ಅಸಡ್ಡೆಗಳು ಇರಬಹುದು. ಮೂಲ ಭಾಷೆಯಲ್ಲಿರುವ ಮೂಲ ದಸ್ತಾವೇಜು ಪ್ರಾಮಾಣಿಕ ಮೂಲವೆಂದು ಪರಿಗಣಿಸಬೇಕು. ಪ್ರಮುಖ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದವನ್ನು ಬಳಸುವ ಮೂಲಕ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪು ಅರ್ಥಗಳ ಅಥವಾ ತಪ್ಪು ವ್ಯಾಖ್ಯಾನಗಳ ಬಗ್ಗೆ ನಾವು ಹೊಣೆಗಾರರಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->