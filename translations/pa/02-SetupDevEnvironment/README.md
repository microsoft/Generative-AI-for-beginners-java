<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:07:32+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "pa"
}
-->
# ਜਾਵਾ ਲਈ ਜਨਰੇਟਿਵ AI ਦੇ ਵਿਕਾਸਕ ਮਾਹੌਲ ਦੀ ਸੈਟਿੰਗ

> **ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ**: ਕਲਾਉਡ ਵਿੱਚ 2 ਮਿੰਟਾਂ ਵਿੱਚ ਕੋਡ ਕਰੋ - [GitHub Codespaces ਸੈਟਅੱਪ](../../../02-SetupDevEnvironment) 'ਤੇ ਜਾਓ - ਕੋਈ ਲੋਕਲ ਇੰਸਟਾਲੇਸ਼ਨ ਦੀ ਲੋੜ ਨਹੀਂ ਅਤੇ ਇਹ GitHub ਮਾਡਲ ਵਰਤਦਾ ਹੈ!

> **Azure OpenAI ਵਿੱਚ ਦਿਲਚਸਪੀ ਹੈ?**, ਸਾਡੇ [Azure OpenAI ਸੈਟਅੱਪ ਗਾਈਡ](getting-started-azure-openai.md) ਨੂੰ ਵੇਖੋ ਜਿਸ ਵਿੱਚ ਨਵਾਂ Azure OpenAI ਸਰੋਤ ਬਣਾਉਣ ਦੇ ਕਦਮ ਦਿੱਤੇ ਗਏ ਹਨ।

## ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ

- AI ਐਪਲੀਕੇਸ਼ਨ ਲਈ ਜਾਵਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਸੈਟਅੱਪ ਕਰੋ
- ਆਪਣਾ ਪਸੰਦੀਦਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਚੁਣੋ ਅਤੇ ਕਨਫਿਗਰ ਕਰੋ (ਕਲਾਉਡ-ਪਹਿਲਾਂ Codespaces ਨਾਲ, ਲੋਕਲ ਡਿਵ ਕੰਟੇਨਰ, ਜਾਂ ਪੂਰੀ ਲੋਕਲ ਸੈਟਅੱਪ)
- GitHub ਮਾਡਲ ਨਾਲ ਕਨੈਕਟ ਕਰਕੇ ਆਪਣਾ ਸੈਟਅੱਪ ਟੈਸਟ ਕਰੋ

## ਸੂਚੀ

- [ਤੁਸੀਂ ਕੀ ਸਿੱਖੋਗੇ](../../../02-SetupDevEnvironment)
- [ਪ੍ਰਸਤਾਵਨਾ](../../../02-SetupDevEnvironment)
- [ਪਹਿਲਾ ਕਦਮ: ਆਪਣਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਸੈਟਅੱਪ ਕਰੋ](../../../02-SetupDevEnvironment)
  - [ਵਿਕਲਪ A: GitHub Codespaces (ਸਿਫਾਰਸ਼ ਕੀਤੀ ਗਈ)](../../../02-SetupDevEnvironment)
  - [ਵਿਕਲਪ B: ਲੋਕਲ ਡਿਵ ਕੰਟੇਨਰ](../../../02-SetupDevEnvironment)
  - [ਵਿਕਲਪ C: ਆਪਣੀ ਮੌਜੂਦਾ ਲੋਕਲ ਇੰਸਟਾਲੇਸ਼ਨ ਵਰਤੋ](../../../02-SetupDevEnvironment)
- [ਦੂਜਾ ਕਦਮ: GitHub ਪੈਰਸਨਲ ਐਕਸੈਸ ਟੋਕਨ ਬਣਾਓ](../../../02-SetupDevEnvironment)
- [ਤੀਜਾ ਕਦਮ: ਆਪਣਾ ਸੈਟਅੱਪ ਟੈਸਟ ਕਰੋ](../../../02-SetupDevEnvironment)
- [ਮਸਲੇ ਹੱਲ ਕਰਨ](../../../02-SetupDevEnvironment)
- [ਸੰਖੇਪ](../../../02-SetupDevEnvironment)
- [ਅਗਲੇ ਕਦਮ](../../../02-SetupDevEnvironment)

## ਪ੍ਰਸਤਾਵਨਾ

ਇਹ ਅਧਿਆਇ ਤੁਹਾਨੂੰ ਵਿਕਾਸਕ ਮਾਹੌਲ ਸੈਟਅੱਪ ਕਰਨ ਵਿੱਚ ਮਦਦ ਕਰੇਗਾ। ਅਸੀਂ **GitHub ਮਾਡਲ** ਨੂੰ ਆਪਣੀ ਮੁੱਖ ਉਦਾਹਰਣ ਵਜੋਂ ਵਰਤਾਂਗੇ ਕਿਉਂਕਿ ਇਹ ਮੁਫ਼ਤ ਹੈ, ਸਿਰਫ਼ GitHub ਖਾਤੇ ਨਾਲ ਸੈਟਅੱਪ ਕਰਨਾ ਆਸਾਨ ਹੈ, ਕਿਸੇ ਕਰੈਡਿਟ ਕਾਰਡ ਦੀ ਲੋੜ ਨਹੀਂ ਹੈ, ਅਤੇ ਪ੍ਰਯੋਗ ਲਈ ਕਈ ਮਾਡਲਾਂ ਤੱਕ ਪਹੁੰਚ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ।

**ਕੋਈ ਲੋਕਲ ਸੈਟਅੱਪ ਦੀ ਲੋੜ ਨਹੀਂ!** ਤੁਸੀਂ ਤੁਰੰਤ GitHub Codespaces ਵਰਤ ਕੇ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ ਪੂਰਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਸ਼ੁਰੂ ਕਰ ਸਕਦੇ ਹੋ।

<img src="./images/models.webp" alt="Screenshot: GitHub Models" width="50%">

ਅਸੀਂ ਇਸ ਕੋਰਸ ਲਈ [**GitHub ਮਾਡਲ**](https://github.com/marketplace?type=models) ਦੀ ਸਿਫਾਰਸ਼ ਕਰਦੇ ਹਾਂ ਕਿਉਂਕਿ ਇਹ:
- **ਮੁਫ਼ਤ** ਹੈ ਸ਼ੁਰੂਆਤ ਕਰਨ ਲਈ
- **ਆਸਾਨ** ਹੈ ਸਿਰਫ਼ GitHub ਖਾਤੇ ਨਾਲ ਸੈਟਅੱਪ ਕਰਨ ਲਈ
- **ਕੋਈ ਕਰੈਡਿਟ ਕਾਰਡ** ਦੀ ਲੋੜ ਨਹੀਂ
- **ਕਈ ਮਾਡਲ** ਪ੍ਰਯੋਗ ਲਈ ਉਪਲਬਧ ਹਨ

> **ਨੋਟ**: ਇਸ ਟ੍ਰੇਨਿੰਗ ਵਿੱਚ ਵਰਤੇ ਗਏ GitHub ਮਾਡਲਾਂ ਦੀਆਂ ਮੁਫ਼ਤ ਸੀਮਾਵਾਂ ਹਨ:
> - 15 ਬੇਨਤੀ ਪ੍ਰਤੀ ਮਿੰਟ (150 ਪ੍ਰਤੀ ਦਿਨ)
> - ~8,000 ਸ਼ਬਦ ਅੰਦਰ, ~4,000 ਸ਼ਬਦ ਬਾਹਰ ਪ੍ਰਤੀ ਬੇਨਤੀ
> - 5 ਸਮਕਾਲੀ ਬੇਨਤੀਆਂ
> 
> ਉਤਪਾਦਨ ਲਈ, ਆਪਣੇ Azure ਖਾਤੇ ਨਾਲ Azure AI Foundry ਮਾਡਲਾਂ 'ਤੇ ਅਪਗ੍ਰੇਡ ਕਰੋ। ਤੁਹਾਡਾ ਕੋਡ ਬਦਲਣ ਦੀ ਲੋੜ ਨਹੀਂ। [Azure AI Foundry ਦਸਤਾਵੇਜ਼](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) ਵੇਖੋ।

## ਪਹਿਲਾ ਕਦਮ: ਆਪਣਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਸੈਟਅੱਪ ਕਰੋ

<a name="quick-start-cloud"></a>

ਅਸੀਂ ਇੱਕ ਪ੍ਰੀ-ਕਨਫਿਗਰਡ ਵਿਕਾਸਕ ਕੰਟੇਨਰ ਬਣਾਇਆ ਹੈ ਤਾਂ ਜੋ ਸੈਟਅੱਪ ਸਮਾਂ ਘਟਾਇਆ ਜਾ ਸਕੇ ਅਤੇ ਇਹ ਯਕੀਨੀ ਬਣਾਇਆ ਜਾ ਸਕੇ ਕਿ ਤੁਹਾਡੇ ਕੋਲ ਇਸ ਜਨਰੇਟਿਵ AI ਜਾਵਾ ਕੋਰਸ ਲਈ ਸਾਰੇ ਜ਼ਰੂਰੀ ਟੂਲ ਹਨ। ਆਪਣਾ ਪਸੰਦੀਦਾ ਵਿਕਾਸਕ ਪਹੁੰਚ ਚੁਣੋ:

### ਮਾਹੌਲ ਸੈਟਅੱਪ ਦੇ ਵਿਕਲਪ:

#### ਵਿਕਲਪ A: GitHub Codespaces (ਸਿਫਾਰਸ਼ ਕੀਤੀ ਗਈ)

**2 ਮਿੰਟਾਂ ਵਿੱਚ ਕੋਡਿੰਗ ਸ਼ੁਰੂ ਕਰੋ - ਕੋਈ ਲੋਕਲ ਸੈਟਅੱਪ ਦੀ ਲੋੜ ਨਹੀਂ!**

1. ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ GitHub ਖਾਤੇ ਵਿੱਚ ਫੋਰਕ ਕਰੋ
   > **ਨੋਟ**: ਜੇ ਤੁਸੀਂ ਬੇਸਿਕ ਕਨਫਿਗਰੇਸ਼ਨ ਸੋਧਣਾ ਚਾਹੁੰਦੇ ਹੋ ਤਾਂ [ਡਿਵ ਕੰਟੇਨਰ ਕਨਫਿਗਰੇਸ਼ਨ](../../../.devcontainer/devcontainer.json) ਵੇਖੋ।
2. **Code** → **Codespaces** ਟੈਬ → **...** → **New with options...** 'ਤੇ ਕਲਿੱਕ ਕਰੋ।
3. ਡਿਫਾਲਟ ਵਰਤੋ – ਇਹ **Generative AI Java Development Environment** ਕਸਟਮ ਡਿਵਕੰਟੇਨਰ ਚੁਣੇਗਾ ਜੋ ਇਸ ਕੋਰਸ ਲਈ ਬਣਾਇਆ ਗਿਆ ਹੈ।
4. **Create codespace** 'ਤੇ ਕਲਿੱਕ ਕਰੋ।
5. ਮਾਹੌਲ ਤਿਆਰ ਹੋਣ ਲਈ ~2 ਮਿੰਟ ਰੁਕੋ।
6. [ਦੂਜਾ ਕਦਮ: GitHub ਟੋਕਨ ਬਣਾਓ](../../../02-SetupDevEnvironment) 'ਤੇ ਜਾਓ।

<img src="./images/codespaces.png" alt="Screenshot: Codespaces submenu" width="50%">

<img src="./images/image.png" alt="Screenshot: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Create codespace options" width="50%">

> **Codespaces ਦੇ ਫਾਇਦੇ**:
> - ਕੋਈ ਲੋਕਲ ਇੰਸਟਾਲੇਸ਼ਨ ਦੀ ਲੋੜ ਨਹੀਂ
> - ਕਿਸੇ ਵੀ ਬ੍ਰਾਊਜ਼ਰ ਵਾਲੇ ਡਿਵਾਈਸ 'ਤੇ ਕੰਮ ਕਰਦਾ ਹੈ
> - ਸਾਰੇ ਟੂਲ ਅਤੇ ਡਿਪੈਂਡੈਂਸੀਜ਼ ਨਾਲ ਪ੍ਰੀ-ਕਨਫਿਗਰਡ
> - ਨਿੱਜੀ ਖਾਤਿਆਂ ਲਈ ਪ੍ਰਤੀ ਮਹੀਨਾ 60 ਘੰਟੇ ਮੁਫ਼ਤ
> - ਸਾਰੇ ਸਿੱਖਣ ਵਾਲਿਆਂ ਲਈ ਇੱਕੋ ਜਿਹਾ ਮਾਹੌਲ

#### ਵਿਕਲਪ B: ਲੋਕਲ ਡਿਵ ਕੰਟੇਨਰ

**ਉਨ੍ਹਾਂ ਵਿਕਾਸਕਾਂ ਲਈ ਜੋ ਡੌਕਰ ਨਾਲ ਲੋਕਲ ਵਿਕਾਸ ਨੂੰ ਤਰਜੀਹ ਦਿੰਦੇ ਹਨ**

1. ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ ਲੋਕਲ ਮਸ਼ੀਨ 'ਤੇ ਫੋਰਕ ਅਤੇ ਕਲੋਨ ਕਰੋ।
   > **ਨੋਟ**: ਜੇ ਤੁਸੀਂ ਬੇਸਿਕ ਕਨਫਿਗਰੇਸ਼ਨ ਸੋਧਣਾ ਚਾਹੁੰਦੇ ਹੋ ਤਾਂ [ਡਿਵ ਕੰਟੇਨਰ ਕਨਫਿਗਰੇਸ਼ਨ](../../../.devcontainer/devcontainer.json) ਵੇਖੋ।
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) ਅਤੇ [VS Code](https://code.visualstudio.com/) ਇੰਸਟਾਲ ਕਰੋ।
3. VS Code ਵਿੱਚ [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ਇੰਸਟਾਲ ਕਰੋ।
4. ਰਿਪੋਜ਼ਟਰੀ ਫੋਲਡਰ ਨੂੰ VS Code ਵਿੱਚ ਖੋਲ੍ਹੋ।
5. ਜਦੋਂ ਪ੍ਰਮਾਣਿਤ ਕੀਤਾ ਜਾਵੇ, **Reopen in Container** 'ਤੇ ਕਲਿੱਕ ਕਰੋ (ਜਾਂ `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" ਵਰਤੋ)।
6. ਕੰਟੇਨਰ ਨੂੰ ਬਣਨ ਅਤੇ ਸ਼ੁਰੂ ਹੋਣ ਲਈ ਰੁਕੋ।
7. [ਦੂਜਾ ਕਦਮ: GitHub ਟੋਕਨ ਬਣਾਓ](../../../02-SetupDevEnvironment) 'ਤੇ ਜਾਓ।

<img src="./images/devcontainer.png" alt="Screenshot: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev container build complete" width="50%">

#### ਵਿਕਲਪ C: ਆਪਣੀ ਮੌਜੂਦਾ ਲੋਕਲ ਇੰਸਟਾਲੇਸ਼ਨ ਵਰਤੋ

**ਉਨ੍ਹਾਂ ਵਿਕਾਸਕਾਂ ਲਈ ਜਿਨ੍ਹਾਂ ਕੋਲ ਮੌਜੂਦਾ ਜਾਵਾ ਮਾਹੌਲ ਹੈ**

ਪੂਰਵ ਸ਼ਰਤਾਂ:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ਜਾਂ ਤੁਹਾਡਾ ਪਸੰਦੀਦਾ IDE

ਕਦਮ:
1. ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ ਲੋਕਲ ਮਸ਼ੀਨ 'ਤੇ ਕਲੋਨ ਕਰੋ।
2. ਪ੍ਰੋਜੈਕਟ ਨੂੰ ਆਪਣੇ IDE ਵਿੱਚ ਖੋਲ੍ਹੋ।
3. [ਦੂਜਾ ਕਦਮ: GitHub ਟੋਕਨ ਬਣਾਓ](../../../02-SetupDevEnvironment) 'ਤੇ ਜਾਓ।

> **ਪ੍ਰੋ ਟਿਪ**: ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਘੱਟ-ਸਪੈਕ ਮਸ਼ੀਨ ਹੈ ਪਰ ਤੁਸੀਂ ਲੋਕਲ VS Code ਚਾਹੁੰਦੇ ਹੋ, ਤਾਂ GitHub Codespaces ਵਰਤੋ! ਤੁਸੀਂ ਆਪਣੇ ਲੋਕਲ VS Code ਨੂੰ ਕਲਾਉਡ-ਹੋਸਟਡ Codespace ਨਾਲ ਕਨੈਕਟ ਕਰ ਸਕਦੇ ਹੋ।

<img src="./images/image-2.png" alt="Screenshot: created local devcontainer instance" width="50%">

## ਦੂਜਾ ਕਦਮ: GitHub ਪੈਰਸਨਲ ਐਕਸੈਸ ਟੋਕਨ ਬਣਾਓ

1. [GitHub Settings](https://github.com/settings/profile) 'ਤੇ ਜਾਓ ਅਤੇ ਆਪਣੇ ਪ੍ਰੋਫਾਈਲ ਮੀਨੂ ਤੋਂ **Settings** ਚੁਣੋ।
2. ਖੱਬੇ ਸਾਈਡਬਾਰ ਵਿੱਚ, **Developer settings** 'ਤੇ ਕਲਿੱਕ ਕਰੋ (ਆਮ ਤੌਰ 'ਤੇ ਹੇਠਾਂ)।
3. **Personal access tokens** ਹੇਠ, **Fine-grained tokens** 'ਤੇ ਕਲਿੱਕ ਕਰੋ (ਜਾਂ ਇਸ ਸਿੱਧੇ [ਲਿੰਕ](https://github.com/settings/personal-access-tokens) ਨੂੰ ਫੋਲੋ ਕਰੋ)।
4. **Generate new token** 'ਤੇ ਕਲਿੱਕ ਕਰੋ।
5. "Token name" ਹੇਠ, ਇੱਕ ਵਰਣਨਾਤਮਕ ਨਾਮ ਦਿਓ (ਜਿਵੇਂ, `GenAI-Java-Course-Token`)।
6. ਮਿਆਦ ਦੀ ਮਿਤੀ ਸੈੱਟ ਕਰੋ (ਸੁਰੱਖਿਆ ਲਈ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਗਈ: 7 ਦਿਨ)।
7. "Resource owner" ਹੇਠ, ਆਪਣਾ ਯੂਜ਼ਰ ਖਾਤਾ ਚੁਣੋ।
8. "Repository access" ਹੇਠ, ਉਹ ਰਿਪੋਜ਼ਟਰੀ ਚੁਣੋ ਜੋ ਤੁਸੀਂ GitHub ਮਾਡਲਾਂ ਨਾਲ ਵਰਤਣਾ ਚਾਹੁੰਦੇ ਹੋ (ਜਾਂ "All repositories" ਜੇ ਲੋੜੀਂਦਾ ਹੋਵੇ)।
9. "Repository permissions" ਹੇਠ, **Models** ਲੱਭੋ ਅਤੇ ਇਸਨੂੰ **Read and write** 'ਤੇ ਸੈੱਟ ਕਰੋ।
10. **Generate token** 'ਤੇ ਕਲਿੱਕ ਕਰੋ।
11. **ਹੁਣੇ ਆਪਣਾ ਟੋਕਨ ਕਾਪੀ ਅਤੇ ਸੇਵ ਕਰੋ** – ਤੁਸੀਂ ਇਸਨੂੰ ਮੁੜ ਨਹੀਂ ਦੇਖੋਗੇ!

> **ਸੁਰੱਖਿਆ ਟਿਪ**: ਆਪਣੀ ਪਹੁੰਚ ਟੋਕਨ ਲਈ ਘੱਟੋ-ਘੱਟ ਲੋੜੀਂਦੇ ਸਕੋਪ ਅਤੇ ਸਭ ਤੋਂ ਛੋਟੀ ਮਿਆਦ ਦੀ ਮਿਆਦ ਵਰਤੋ।

## ਤੀਜਾ ਕਦਮ: GitHub ਮਾਡਲਾਂ ਦੇ ਉਦਾਹਰਣ ਨਾਲ ਆਪਣਾ ਸੈਟਅੱਪ ਟੈਸਟ ਕਰੋ

ਜਦੋਂ ਤੁਹਾਡਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਤਿਆਰ ਹੋ ਜਾਵੇ, ਆਓ GitHub ਮਾਡਲਾਂ ਦੇ ਉਦਾਹਰਣ ਐਪਲੀਕੇਸ਼ਨ ਨਾਲ ਟੈਸਟ ਕਰੀਏ ਜੋ [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) ਵਿੱਚ ਹੈ।

1. ਆਪਣੇ ਵਿਕਾਸਕ ਮਾਹੌਲ ਵਿੱਚ ਟਰਮੀਨਲ ਖੋਲ੍ਹੋ।
2. GitHub ਮਾਡਲਾਂ ਦੇ ਉਦਾਹਰਣ ਵਿੱਚ ਜਾਓ:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. ਆਪਣਾ GitHub ਟੋਕਨ ਇੱਕ ਮਾਹੌਲ ਵੈਰੀਏਬਲ ਵਜੋਂ ਸੈੱਟ ਕਰੋ:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਓ:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

ਤੁਹਾਨੂੰ ਇਸ ਤਰ੍ਹਾਂ ਦਾ ਆਉਟਪੁੱਟ ਵੇਖਣਾ ਚਾਹੀਦਾ ਹੈ:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### ਉਦਾਹਰਣ ਕੋਡ ਨੂੰ ਸਮਝਣਾ

ਸਭ ਤੋਂ ਪਹਿਲਾਂ, ਆਓ ਸਮਝੀਏ ਕਿ ਅਸੀਂ ਕੀ ਚਲਾਇਆ। `examples/github-models` ਹੇਠਲਾ ਉਦਾਹਰਣ OpenAI Java SDK ਵਰਤਦਾ ਹੈ GitHub ਮਾਡਲਾਂ ਨਾਲ ਕਨੈਕਟ ਕਰਨ ਲਈ:

**ਇਹ ਕੋਡ ਕੀ ਕਰਦਾ ਹੈ:**
- **GitHub ਮਾਡਲਾਂ ਨਾਲ ਕਨੈਕਟ ਕਰਦਾ ਹੈ** ਤੁਹਾਡੇ ਪੈਰਸਨਲ ਐਕਸੈਸ ਟੋਕਨ ਦੀ ਵਰਤੋਂ ਕਰਕੇ
- **ਇੱਕ ਸਧਾਰਨ ਸੁਨੇਹਾ ਭੇਜਦਾ ਹੈ** "Say Hello World!" AI ਮਾਡਲ ਨੂੰ
- **ਜਵਾਬ ਪ੍ਰਾਪਤ ਕਰਦਾ ਹੈ** ਅਤੇ ਦਿਖਾਉਂਦਾ ਹੈ
- **ਤਸਦੀਕ ਕਰਦਾ ਹੈ** ਕਿ ਤੁਹਾਡਾ ਸੈਟਅੱਪ ਸਹੀ ਤਰੀਕੇ ਨਾਲ ਕੰਮ ਕਰ ਰਿਹਾ ਹੈ

**ਮੁੱਖ ਡਿਪੈਂਡੈਂਸੀ** (`pom.xml` ਵਿੱਚ):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**ਮੁੱਖ ਕੋਡ** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## ਸੰਖੇਪ

ਵਧੀਆ! ਹੁਣ ਤੁਹਾਡੇ ਕੋਲ ਸਭ ਕੁਝ ਤਿਆਰ ਹੈ:

- GitHub ਪੈਰਸਨਲ ਐਕਸੈਸ ਟੋਕਨ ਬਣਾਇਆ ਜਿਸ ਵਿੱਚ AI ਮਾਡਲ ਪਹੁੰਚ ਲਈ ਸਹੀ ਅਧਿਕਾਰ ਹਨ
- ਆਪਣਾ ਜਾਵਾ ਵਿਕਾਸਕ ਮਾਹੌਲ ਚਲਾਇਆ (ਚਾਹੇ ਉਹ Codespaces, ਡਿਵ ਕੰਟੇਨਰ, ਜਾਂ ਲੋਕਲ ਹੋਵੇ)
- OpenAI Java SDK ਦੀ ਵਰਤੋਂ ਕਰਕੇ GitHub ਮਾਡਲਾਂ ਨਾਲ ਕਨੈਕਟ ਕੀਤਾ
- ਇੱਕ ਸਧਾਰਣ ਉਦਾਹਰਣ ਨਾਲ ਸਾਰਾ ਸੈਟਅੱਪ ਟੈਸਟ ਕੀਤਾ ਜੋ AI ਮਾਡਲਾਂ ਨਾਲ ਗੱਲ ਕਰਦਾ ਹੈ

## ਅਗਲੇ ਕਦਮ

[ਅਧਿਆਇ 3: ਮੁੱਖ ਜਨਰੇਟਿਵ AI ਤਕਨੀਕਾਂ](../03-CoreGenerativeAITechniques/README.md)

## ਮਸਲੇ ਹੱਲ ਕਰਨ

ਮਸਲੇ ਆ ਰਹੇ ਹਨ? ਇੱਥੇ ਆਮ ਸਮੱਸਿਆਵਾਂ ਅਤੇ ਹੱਲ ਹਨ:

- **ਟੋਕਨ ਕੰਮ ਨਹੀਂ ਕਰ ਰਿਹਾ?** 
  - ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਸੀਂ ਪੂਰਾ ਟੋਕਨ ਕਾਪੀ ਕੀਤਾ ਹੈ ਬਿਨਾਂ ਕਿਸੇ ਵਾਧੂ ਖਾਲੀ ਜਗ੍ਹਾ ਦੇ
  - ਟੋਕਨ ਨੂੰ ਸਹੀ ਤਰੀਕੇ ਨਾਲ ਮਾਹੌਲ ਵੈਰੀਏਬਲ ਵਜੋਂ ਸੈੱਟ ਕੀਤਾ ਹੈ ਜਾਂ ਨਹੀਂ ਜਾਂਚੋ
  - ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਟੋਕਨ ਕੋਲ ਸਹੀ ਅਧਿਕਾਰ ਹਨ (Models: Read and write)

- **Maven ਨਹੀਂ ਮਿਲ ਰਿਹਾ?** 
  - ਜੇ ਤੁਸੀਂ ਡਿਵ ਕੰਟੇਨਰ/Codespaces ਵਰਤ ਰਹੇ ਹੋ, Maven ਪਹਿਲਾਂ ਤੋਂ ਇੰਸਟਾਲ ਹੋਣਾ ਚਾਹੀਦਾ ਹੈ
  - ਲੋਕਲ ਸੈਟਅੱਪ ਲਈ, ਯਕੀਨੀ ਬਣਾਓ ਕਿ Java 21+ ਅਤੇ Maven 3.9+ ਇੰਸਟਾਲ ਹਨ
  - `mvn --version` ਚਲਾਕੇ ਇੰਸਟਾਲੇਸ਼ਨ ਦੀ ਪੁਸ਼ਟੀ ਕਰੋ

- **ਕਨੈਕਸ਼ਨ ਸਮੱਸਿਆਵਾਂ?** 
  - ਆਪਣੀ ਇੰਟਰਨੈਟ ਕਨੈਕਸ਼ਨ ਜਾਂ

**ਅਸਵੀਕਾਰਨਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁਚਨਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।