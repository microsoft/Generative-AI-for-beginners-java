<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "4bdff5070d182c64143dfe5a581d0ec7",
  "translation_date": "2025-12-01T09:35:20+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "kn"
}
-->
# ಜಾವಾ ಜನರೇಟಿವ್ AI ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಸೆಟ್ ಅಪ್ ಮಾಡುವುದು

> **ತ್ವರಿತ ಪ್ರಾರಂಭ**: 2 ನಿಮಿಷಗಳಲ್ಲಿ ಕ್ಲೌಡ್‌ನಲ್ಲಿ ಕೋಡ್ ಬರೆಯಿರಿ - [GitHub Codespaces ಸೆಟ್ ಅಪ್](../../../02-SetupDevEnvironment) ಗೆ ಹೋಗಿ - ಸ್ಥಳೀಯ ಇನ್‌ಸ್ಟಾಲೇಶನ್ ಅಗತ್ಯವಿಲ್ಲ ಮತ್ತು GitHub ಮಾದರಿಗಳನ್ನು ಬಳಸುತ್ತದೆ!

> **Azure OpenAI ಬಗ್ಗೆ ಆಸಕ್ತಿ ಇದೆಯೇ?**, ಹೊಸ Azure OpenAI ಸಂಪತ್ತನ್ನು ರಚಿಸಲು ನಮ್ಮ [Azure OpenAI ಸೆಟ್ ಅಪ್ ಗೈಡ್](getting-started-azure-openai.md) ನೋಡಿ.

## ನೀವು ಏನು ಕಲಿಯುತ್ತೀರಿ

- AI ಅಪ್ಲಿಕೇಶನ್‌ಗಳಿಗೆ ಜಾವಾ ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಸೆಟ್ ಅಪ್ ಮಾಡುವುದು
- ನಿಮ್ಮ ಇಷ್ಟದ ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಆಯ್ಕೆ ಮಾಡಿ ಮತ್ತು ಕಾನ್ಫಿಗರ್ ಮಾಡುವುದು (Codespaces ಮೂಲಕ ಕ್ಲೌಡ್-ಫಸ್ಟ್, ಸ್ಥಳೀಯ ಡೆವ್ ಕಂಟೈನರ್, ಅಥವಾ ಸಂಪೂರ್ಣ ಸ್ಥಳೀಯ ಸೆಟ್ ಅಪ್)
- GitHub ಮಾದರಿಗಳಿಗೆ ಸಂಪರ್ಕಿಸುವ ಮೂಲಕ ನಿಮ್ಮ ಸೆಟ್ ಅಪ್ ಅನ್ನು ಪರೀಕ್ಷಿಸಿ

## ವಿಷಯಗಳ ಪಟ್ಟಿಯು

- [ನೀವು ಏನು ಕಲಿಯುತ್ತೀರಿ](../../../02-SetupDevEnvironment)
- [ಪರಿಚಯ](../../../02-SetupDevEnvironment)
- [ಹಂತ 1: ನಿಮ್ಮ ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಸೆಟ್ ಅಪ್ ಮಾಡುವುದು](../../../02-SetupDevEnvironment)
  - [ಆಯ್ಕೆ A: GitHub Codespaces (ಶಿಫಾರಸು)](../../../02-SetupDevEnvironment)
  - [ಆಯ್ಕೆ B: ಸ್ಥಳೀಯ ಡೆವ್ ಕಂಟೈನರ್](../../../02-SetupDevEnvironment)
  - [ಆಯ್ಕೆ C: ನಿಮ್ಮ ಇತ್ತೀಚಿನ ಸ್ಥಳೀಯ ಇನ್‌ಸ್ಟಾಲೇಶನ್ ಅನ್ನು ಬಳಸುವುದು](../../../02-SetupDevEnvironment)
- [ಹಂತ 2: GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ರಚಿಸಿ](../../../02-SetupDevEnvironment)
- [ಹಂತ 3: ನಿಮ್ಮ ಸೆಟ್ ಅಪ್ ಅನ್ನು ಪರೀಕ್ಷಿಸಿ](../../../02-SetupDevEnvironment)
- [ತೊಂದರೆ ಪರಿಹಾರ](../../../02-SetupDevEnvironment)
- [ಸಾರಾಂಶ](../../../02-SetupDevEnvironment)
- [ಮುಂದಿನ ಹಂತಗಳು](../../../02-SetupDevEnvironment)

## ಪರಿಚಯ

ಈ ಅಧ್ಯಾಯವು ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಸೆಟ್ ಅಪ್ ಮಾಡುವ ಮೂಲಕ ನಿಮಗೆ ಮಾರ್ಗದರ್ಶನ ನೀಡುತ್ತದೆ. **GitHub ಮಾದರಿಗಳು** ನಮ್ಮ ಪ್ರಾಥಮಿಕ ಉದಾಹರಣೆಯಾಗಿ ಬಳಸಲಾಗುತ್ತದೆ ಏಕೆಂದರೆ ಇದು ಉಚಿತ, GitHub ಖಾತೆಯೊಂದಿಗೆ ಸುಲಭವಾಗಿ ಸೆಟ್ ಅಪ್ ಮಾಡಬಹುದು, ಕ್ರೆಡಿಟ್ ಕಾರ್ಡ್ ಅಗತ್ಯವಿಲ್ಲ, ಮತ್ತು ಪ್ರಯೋಗಕ್ಕಾಗಿ ಅನೇಕ ಮಾದರಿಗಳನ್ನು ಒದಗಿಸುತ್ತದೆ.

**ಸ್ಥಳೀಯ ಸೆಟ್ ಅಪ್ ಅಗತ್ಯವಿಲ್ಲ!** GitHub Codespaces ಬಳಸಿ ತಕ್ಷಣವೇ ಕೋಡಿಂಗ್ ಪ್ರಾರಂಭಿಸಬಹುದು, ಇದು ನಿಮ್ಮ ಬ್ರೌಸರ್‌ನಲ್ಲಿ ಸಂಪೂರ್ಣ ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಒದಗಿಸುತ್ತದೆ.

<img src="./images/models.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: GitHub ಮಾದರಿಗಳು" width="50%">

ಈ ಕೋರ್ಸ್‌ಗೆ ನಾವು [**GitHub ಮಾದರಿಗಳನ್ನು**](https://github.com/marketplace?type=models) ಶಿಫಾರಸು ಮಾಡುತ್ತೇವೆ ಏಕೆಂದರೆ ಇದು:
- **ಉಚಿತ**ವಾಗಿ ಪ್ರಾರಂಭಿಸಲು
- **ಸುಲಭ**ವಾಗಿ GitHub ಖಾತೆಯೊಂದಿಗೆ ಸೆಟ್ ಅಪ್ ಮಾಡಬಹುದು
- **ಕ್ರೆಡಿಟ್ ಕಾರ್ಡ್** ಅಗತ್ಯವಿಲ್ಲ
- **ಅನೇಕ ಮಾದರಿಗಳು** ಪ್ರಯೋಗಕ್ಕಾಗಿ ಲಭ್ಯವಿದೆ

> **ಗಮನಿಸಿ**: ಈ ತರಬೇತಿಯಲ್ಲಿ ಬಳಸುವ GitHub ಮಾದರಿಗಳಿಗೆ ಈ ಉಚಿತ ಮಿತಿಗಳು ಇವೆ:
> - 15 ವಿನಂತಿಗಳು ಪ್ರತಿ ನಿಮಿಷ (150 ಪ್ರತಿ ದಿನ)
> - ~8,000 ಪದಗಳು ಒಳಗೆ, ~4,000 ಪದಗಳು ಹೊರಗೆ ಪ್ರತಿ ವಿನಂತಿಗೆ
> - 5 ಸಮಕಾಲೀನ ವಿನಂತಿಗಳು
> 
> ಉತ್ಪಾದನಾ ಬಳಕೆಗಾಗಿ, ನಿಮ್ಮ Azure ಖಾತೆಯೊಂದಿಗೆ Azure AI Foundry ಮಾದರಿಗಳಿಗೆ ಅಪ್‌ಗ್ರೇಡ್ ಮಾಡಿ. ನಿಮ್ಮ ಕೋಡ್ ಬದಲಾಯಿಸಲು ಅಗತ್ಯವಿಲ್ಲ. [Azure AI Foundry ಡಾಕ್ಯುಮೆಂಟೇಶನ್](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) ನೋಡಿ.

## ಹಂತ 1: ನಿಮ್ಮ ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಸೆಟ್ ಅಪ್ ಮಾಡುವುದು

<a name="quick-start-cloud"></a>

ನಾವು ಈ ಜನರೇಟಿವ್ AI ಜಾವಾ ಕೋರ್ಸ್‌ಗಾಗಿ ಅಗತ್ಯವಿರುವ ಎಲ್ಲಾ ಸಾಧನಗಳನ್ನು ಹೊಂದಿರುವ ಪೂರ್ವ-ಕಾನ್ಫಿಗರ್ ಮಾಡಿದ ಅಭಿವೃದ್ಧಿ ಕಂಟೈನರ್ ಅನ್ನು ರಚಿಸಿದ್ದೇವೆ. ನಿಮ್ಮ ಇಷ್ಟದ ಅಭಿವೃದ್ಧಿ ವಿಧಾನವನ್ನು ಆಯ್ಕೆಮಾಡಿ:

### ಪರಿಸರ ಸೆಟ್ ಅಪ್ ಆಯ್ಕೆಗಳು:

#### ಆಯ್ಕೆ A: GitHub Codespaces (ಶಿಫಾರಸು)

**2 ನಿಮಿಷಗಳಲ್ಲಿ ಕೋಡಿಂಗ್ ಪ್ರಾರಂಭಿಸಿ - ಸ್ಥಳೀಯ ಸೆಟ್ ಅಪ್ ಅಗತ್ಯವಿಲ್ಲ!**

1. ಈ ರೆಪೊಸಿಟರಿಯನ್ನು ನಿಮ್ಮ GitHub ಖಾತೆಗೆ ಫೋರ್ಕ್ ಮಾಡಿ
   > **ಗಮನಿಸಿ**: ನೀವು ಮೂಲಭೂತ ಕಾನ್ಫಿಗ್ ಅನ್ನು ಸಂಪಾದಿಸಲು ಬಯಸಿದರೆ [Dev Container Configuration](../../../.devcontainer/devcontainer.json) ನೋಡಿ
2. **Code** → **Codespaces** ಟ್ಯಾಬ್ → **...** → **New with options...** ಕ್ಲಿಕ್ ಮಾಡಿ
3. ಡೀಫಾಲ್ಟ್‌ಗಳನ್ನು ಬಳಸಿರಿ – ಇದು ಈ ಕೋರ್ಸ್‌ಗಾಗಿ ರಚಿಸಲಾದ **Generative AI Java Development Environment** ಕಸ್ಟಮ್ ಡೆವ್‌ಕಂಟೈನರ್ ಅನ್ನು ಆಯ್ಕೆಮಾಡುತ್ತದೆ
4. **Create codespace** ಕ್ಲಿಕ್ ಮಾಡಿ
5. ಪರಿಸರ ಸಿದ್ಧವಾಗಲು ~2 ನಿಮಿಷ ಕಾಯಿರಿ
6. [ಹಂತ 2: GitHub ಟೋಕನ್ ರಚಿಸಿ](../../../02-SetupDevEnvironment) ಗೆ ಮುಂದುವರಿಯಿರಿ

<img src="../../../translated_images/kn/codespaces.9945ded8ceb431a5.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: Codespaces ಉಪಮೆನು" width="50%">

<img src="../../../translated_images/kn/image.833552b62eee7766.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: New with options" width="50%">

<img src="../../../translated_images/kn/codespaces-create.b44a36f728660ab7.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: Create codespace options" width="50%">

> **Codespaces ನ ಲಾಭಗಳು**:
> - ಸ್ಥಳೀಯ ಇನ್‌ಸ್ಟಾಲೇಶನ್ ಅಗತ್ಯವಿಲ್ಲ
> - ಯಾವುದೇ ಬ್ರೌಸರ್‌ನೊಂದಿಗೆ ಯಾವುದೇ ಸಾಧನದಲ್ಲಿ ಕೆಲಸ ಮಾಡುತ್ತದೆ
> - ಎಲ್ಲಾ ಸಾಧನಗಳು ಮತ್ತು ಅವಲಂಬನೆಗಳೊಂದಿಗೆ ಪೂರ್ವ-ಕಾನ್ಫಿಗರ್ ಮಾಡಲಾಗಿದೆ
> - ವೈಯಕ್ತಿಕ ಖಾತೆಗಳಿಗೆ ತಿಂಗಳಿಗೆ ಉಚಿತ 60 ಗಂಟೆಗಳು
> - ಎಲ್ಲಾ ಕಲಿಯುವವರಿಗೆ ಸತತ ಪರಿಸರ

#### ಆಯ್ಕೆ B: ಸ್ಥಳೀಯ ಡೆವ್ ಕಂಟೈನರ್

**ಡಾಕರ್‌ನೊಂದಿಗೆ ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿಯನ್ನು ಇಷ್ಟಪಡುವ ಡೆವಲಪರ್‌ಗಳಿಗೆ**

1. ಈ ರೆಪೊಸಿಟರಿಯನ್ನು ನಿಮ್ಮ ಸ್ಥಳೀಯ ಯಂತ್ರಕ್ಕೆ ಫೋರ್ಕ್ ಮಾಡಿ ಮತ್ತು ಕ್ಲೋನ್ ಮಾಡಿ
   > **ಗಮನಿಸಿ**: ನೀವು ಮೂಲಭೂತ ಕಾನ್ಫಿಗ್ ಅನ್ನು ಸಂಪಾದಿಸಲು ಬಯಸಿದರೆ [Dev Container Configuration](../../../.devcontainer/devcontainer.json) ನೋಡಿ
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) ಮತ್ತು [VS Code](https://code.visualstudio.com/) ಅನ್ನು ಇನ್‌ಸ್ಟಾಲ್ ಮಾಡಿ
3. VS Code ನಲ್ಲಿ [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ಅನ್ನು ಇನ್‌ಸ್ಟಾಲ್ ಮಾಡಿ
4. ರೆಪೊಸಿಟರಿ ಫೋಲ್ಡರ್ ಅನ್ನು VS Code ನಲ್ಲಿ ತೆರೆಯಿರಿ
5. ಸೂಚನೆ ನೀಡಿದಾಗ, **Reopen in Container** ಕ್ಲಿಕ್ ಮಾಡಿ (ಅಥವಾ `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" ಬಳಸಿ)
6. ಕಂಟೈನರ್ ನಿರ್ಮಿಸಲು ಮತ್ತು ಪ್ರಾರಂಭಿಸಲು ಕಾಯಿರಿ
7. [ಹಂತ 2: GitHub ಟೋಕನ್ ರಚಿಸಿ](../../../02-SetupDevEnvironment) ಗೆ ಮುಂದುವರಿಯಿರಿ

<img src="../../../translated_images/kn/devcontainer.21126c9d6de64494.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: Dev container setup" width="50%">

<img src="../../../translated_images/kn/image-3.bf93d533bbc84268.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: Dev container build complete" width="50%">

#### ಆಯ್ಕೆ C: ನಿಮ್ಮ ಇತ್ತೀಚಿನ ಸ್ಥಳೀಯ ಇನ್‌ಸ್ಟಾಲೇಶನ್ ಅನ್ನು ಬಳಸುವುದು

**ಇತ್ತೀಚಿನ ಜಾವಾ ಪರಿಸರಗಳನ್ನು ಹೊಂದಿರುವ ಡೆವಲಪರ್‌ಗಳಿಗೆ**

ಪೂರ್ವಾಪೇಕ್ಷಿತಗಳು:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ಅಥವಾ ನಿಮ್ಮ ಇಷ್ಟದ IDE

ಹಂತಗಳು:
1. ಈ ರೆಪೊಸಿಟರಿಯನ್ನು ನಿಮ್ಮ ಸ್ಥಳೀಯ ಯಂತ್ರಕ್ಕೆ ಕ್ಲೋನ್ ಮಾಡಿ
2. ನಿಮ್ಮ IDE ನಲ್ಲಿ ಪ್ರಾಜೆಕ್ಟ್ ಅನ್ನು ತೆರೆಯಿರಿ
3. [ಹಂತ 2: GitHub ಟೋಕನ್ ರಚಿಸಿ](../../../02-SetupDevEnvironment) ಗೆ ಮುಂದುವರಿಯಿರಿ

> **ಪ್ರೊ ಟಿಪ್**: ನೀವು ಕಡಿಮೆ-ವಿಶೇಷಣಾ ಯಂತ್ರವನ್ನು ಹೊಂದಿದ್ದರೆ ಆದರೆ ಸ್ಥಳೀಯವಾಗಿ VS Code ಅನ್ನು ಬಯಸಿದರೆ, GitHub Codespaces ಅನ್ನು ಬಳಸಿ! ಕ್ಲೌಡ್-ಹೋಸ್ಟೆಡ್ Codespace ಗೆ ನಿಮ್ಮ ಸ್ಥಳೀಯ VS Code ಅನ್ನು ಸಂಪರ್ಕಿಸಿ.

<img src="../../../translated_images/kn/image-2.fc0da29a6e4d2aff.webp" alt="ಸ್ಕ್ರೀನ್‌ಶಾಟ್: created local devcontainer instance" width="50%">

## ಹಂತ 2: GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ರಚಿಸಿ

1. [GitHub ಸೆಟ್ಟಿಂಗ್‌ಗಳು](https://github.com/settings/profile) ಗೆ ಹೋಗಿ ಮತ್ತು ನಿಮ್ಮ ಪ್ರೊಫೈಲ್ ಮೆನುದಿಂದ **Settings** ಆಯ್ಕೆಮಾಡಿ.
2. ಎಡ ಪಾರ್ಶ್ವದ ಬಾರ್‌ನಲ್ಲಿ, **Developer settings** ಕ್ಲಿಕ್ ಮಾಡಿ (ಸಾಮಾನ್ಯವಾಗಿ ಕೆಳಭಾಗದಲ್ಲಿ).
3. **Personal access tokens** ಅಡಿಯಲ್ಲಿ, **Fine-grained tokens** ಕ್ಲಿಕ್ ಮಾಡಿ (ಅಥವಾ ಈ ನೇರ [ಲಿಂಕ್](https://github.com/settings/personal-access-tokens) ಅನುಸರಿಸಿ).
4. **Generate new token** ಕ್ಲಿಕ್ ಮಾಡಿ.
5. "Token name" ಅಡಿಯಲ್ಲಿ, ವಿವರಣಾತ್ಮಕ ಹೆಸರನ್ನು ನೀಡಿ (ಉದಾ., `GenAI-Java-Course-Token`).
6. ಅವಧಿ ಮುಗಿಯುವ ದಿನಾಂಕವನ್ನು ಹೊಂದಿಸಿ (ಶಿಫಾರಸು: ಭದ್ರತಾ ಉತ್ತಮ ಅಭ್ಯಾಸಗಳಿಗಾಗಿ 7 ದಿನಗಳು).
7. "Resource owner" ಅಡಿಯಲ್ಲಿ, ನಿಮ್ಮ ಬಳಕೆದಾರ ಖಾತೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ.
8. "Repository access" ಅಡಿಯಲ್ಲಿ, GitHub ಮಾದರಿಗಳೊಂದಿಗೆ ಬಳಸಲು ಬಯಸುವ ರೆಪೊಸಿಟರಿಗಳನ್ನು ಆಯ್ಕೆಮಾಡಿ (ಅಥವಾ ಅಗತ್ಯವಿದ್ದರೆ "All repositories").
9. "Account permissions" ಅಡಿಯಲ್ಲಿ, **Models** ಅನ್ನು ಹುಡುಕಿ ಮತ್ತು ಅದನ್ನು **Read-only** ಗೆ ಹೊಂದಿಸಿ.
10. **Generate token** ಕ್ಲಿಕ್ ಮಾಡಿ.
11. **ನಿಮ್ಮ ಟೋಕನ್ ಅನ್ನು ಈಗ ಕಾಪಿ ಮಾಡಿ ಮತ್ತು ಉಳಿಸಿ** – ನೀವು ಇದನ್ನು ಮತ್ತೆ ನೋಡಲು ಸಾಧ್ಯವಿಲ್ಲ!

> **ಭದ್ರತಾ ಟಿಪ್**: ನಿಮ್ಮ ಪ್ರವೇಶ ಟೋಕನ್‌ಗಳಿಗೆ ಕನಿಷ್ಠ ಅಗತ್ಯವಿರುವ ವ್ಯಾಪ್ತಿಯನ್ನು ಮತ್ತು ಅಲ್ಪಾವಧಿಯ ಅವಧಿಯನ್ನು ಬಳಸಿರಿ.

## ಹಂತ 3: GitHub ಮಾದರಿಗಳ ಉದಾಹರಣೆಯೊಂದಿಗೆ ನಿಮ್ಮ ಸೆಟ್ ಅಪ್ ಅನ್ನು ಪರೀಕ್ಷಿಸಿ

ನಿಮ್ಮ ಅಭಿವೃದ್ಧಿ ಪರಿಸರ ಸಿದ್ಧವಾದ ನಂತರ, [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) ನಲ್ಲಿ ನಮ್ಮ ಉದಾಹರಣೆ ಅಪ್ಲಿಕೇಶನ್‌ನೊಂದಿಗೆ GitHub ಮಾದರಿಗಳ ಏಕೀಕರಣವನ್ನು ಪರೀಕ್ಷಿಸೋಣ.

1. ನಿಮ್ಮ ಅಭಿವೃದ್ಧಿ ಪರಿಸರದಲ್ಲಿ ಟರ್ಮಿನಲ್ ಅನ್ನು ತೆರೆಯಿರಿ.
2. GitHub ಮಾದರಿಗಳ ಉದಾಹರಣೆಗೆ ಹೋಗಿ:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. ನಿಮ್ಮ GitHub ಟೋಕನ್ ಅನ್ನು ಪರಿಸರ ವ್ಯತ್ಯಯವಾಗಿ ಸೆಟ್ ಮಾಡಿ:
   ```bash
   # ಮ್ಯಾಕ್‌ಒಎಸ್/ಲಿನಕ್ಸ್
   export GITHUB_TOKEN=your_token_here
   
   # ವಿಂಡೋಸ್ (ಕಮಾಂಡ್ ಪ್ರಾಂಪ್ಟ್)
   set GITHUB_TOKEN=your_token_here
   
   # ವಿಂಡೋಸ್ (ಪವರ್‌ಶೆಲ್)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. ಅಪ್ಲಿಕೇಶನ್ ಅನ್ನು ಚಲಾಯಿಸಿ:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

ನೀವು ಈ ರೀತಿಯ ಔಟ್‌ಪುಟ್ ಅನ್ನು ನೋಡಬಹುದು:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### ಉದಾಹರಣೆ ಕೋಡ್ ಅನ್ನು ಅರ್ಥಮಾಡಿಕೊಳ್ಳುವುದು

ಮೊದಲು, ನಾವು ಏನು ಚಲಾಯಿಸಿದ್ದೇವೆ ಎಂಬುದನ್ನು ಅರ್ಥಮಾಡಿಕೊಳ್ಳೋಣ. `examples/github-models` ಅಡಿಯಲ್ಲಿ ಇರುವ ಉದಾಹರಣೆ OpenAI Java SDK ಅನ್ನು ಬಳಸಿಕೊಂಡು GitHub ಮಾದರಿಗಳಿಗೆ ಸಂಪರ್ಕಿಸುತ್ತದೆ:

**ಈ ಕೋಡ್ ಏನು ಮಾಡುತ್ತದೆ:**
- ನಿಮ್ಮ ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ಬಳಸಿ GitHub ಮಾದರಿಗಳಿಗೆ **ಸಂಪರ್ಕಿಸುತ್ತದೆ**
- AI ಮಾದರಿಗೆ ಸರಳ "Say Hello World!" ಸಂದೇಶವನ್ನು **ಕಳುಹಿಸುತ್ತದೆ**
- AI ನ ಪ್ರತಿಕ್ರಿಯೆಯನ್ನು **ಸ್ವೀಕರಿಸುತ್ತದೆ** ಮತ್ತು ಪ್ರದರ್ಶಿಸುತ್ತದೆ
- ನಿಮ್ಮ ಸೆಟ್ ಅಪ್ ಸರಿಯಾಗಿ ಕೆಲಸ ಮಾಡುತ್ತಿದೆ ಎಂಬುದನ್ನು **ಪರಿಶೀಲಿಸುತ್ತದೆ**

**ಮುಖ್ಯ ಅವಲಂಬನೆ** (`pom.xml` ನಲ್ಲಿ):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**ಮುಖ್ಯ ಕೋಡ್** (`App.java`):
```java
// OpenAI Java SDK ಬಳಸಿ GitHub ಮಾದರಿಗಳೊಂದಿಗೆ ಸಂಪರ್ಕಿಸಿ
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// ಚಾಟ್ ಪೂರ್ಣಗೊಳಿಸುವ ವಿನಂತಿಯನ್ನು ರಚಿಸಿ
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// AI ಪ್ರತಿಕ್ರಿಯೆಯನ್ನು ಪಡೆಯಿರಿ
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## ಸಾರಾಂಶ

ಅದ್ಭುತ! ನೀವು ಈಗ ಎಲ್ಲವನ್ನೂ ಸೆಟ್ ಅಪ್ ಮಾಡಿದ್ದೀರಿ:

- AI ಮಾದರಿಗಳ ಪ್ರವೇಶಕ್ಕಾಗಿ ಸರಿಯಾದ ಅನುಮತಿಗಳೊಂದಿಗೆ GitHub ವೈಯಕ್ತಿಕ ಪ್ರವೇಶ ಟೋಕನ್ ರಚಿಸಲಾಗಿದೆ
- ನಿಮ್ಮ ಜಾವಾ ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು (Codespaces, ಡೆವ್ ಕಂಟೈನರ್‌ಗಳು, ಅಥವಾ ಸ್ಥಳೀಯ) ಚಲಾಯಿಸಲಾಗಿದೆ
- ಉಚಿತ AI ಅಭಿವೃದ್ಧಿಗಾಗಿ OpenAI Java SDK ಬಳಸಿ GitHub ಮಾದರಿಗಳಿಗೆ ಸಂಪರ್ಕಿಸಲಾಗಿದೆ
- AI ಮಾದರಿಗಳೊಂದಿಗೆ ಮಾತನಾಡುವ ಸರಳ ಉದಾಹರಣೆಯೊಂದಿಗೆ ಎಲ್ಲವೂ ಕೆಲಸ ಮಾಡುತ್ತಿದೆ ಎಂದು ಪರೀಕ್ಷಿಸಲಾಗಿದೆ

## ಮುಂದಿನ ಹಂತಗಳು

[ಅಧ್ಯಾಯ 3: ಮೂಲ ಜನರೇಟಿವ್ AI ತಂತ್ರಗಳು](../03-CoreGenerativeAITechniques/README.md)

## ತೊಂದರೆ ಪರಿಹಾರ

ಸಮಸ್ಯೆಗಳನ್ನು ಎದುರಿಸುತ್ತಿದ್ದೀರಾ? ಇಲ್ಲಿ ಸಾಮಾನ್ಯ ಸಮಸ್ಯೆಗಳು ಮತ್ತು ಪರಿಹಾರಗಳಿವೆ:

- **ಟೋಕನ್ ಕೆಲಸ ಮಾಡುತ್ತಿಲ್ಲವೇ?** 
  - ನೀವು ಸಂಪೂರ್ಣ ಟೋಕನ್ ಅನ್ನು ಯಾವುದೇ ಹೆಚ್ಚುವರಿ ಖಾಲಿ ಸ್ಥಳಗಳಿಲ್ಲದೆ ಕಾಪಿ ಮಾಡಿದ್ದೀರಾ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ
  - ಟೋಕನ್ ಅನ್ನು ಪರಿಸರ ವ್ಯತ್ಯಯವಾಗಿ ಸರಿಯಾಗಿ ಸೆಟ್ ಮಾಡಲಾಗಿದೆ ಎಂದು ಪರಿಶೀಲಿಸಿ
  - ನಿಮ್ಮ ಟೋಕನ್ ಸರಿಯಾದ ಅನುಮತಿಗಳನ್ನು ಹೊಂದಿದೆಯೇ (Models: Read and write) ಎಂದು ಪರಿಶೀಲಿಸಿ

- **Maven ಕಂಡುಬರುತ್ತಿಲ್ಲವೇ?** 
  - ಡೆವ್ ಕಂಟೈನರ್‌ಗಳು/Codespaces ಬಳಸಿದರೆ, Maven ಪೂರ್ವ-ಇನ್‌ಸ್ಟಾಲ್ಡ್ ಆಗಿರುತ್ತದೆ
  - ಸ್ಥಳೀಯ ಸೆಟ್ ಅಪ್‌ಗಾಗಿ, Java 21+ ಮತ್ತು Maven 3.9+ ಇನ್‌ಸ್ಟಾಲ್ಡ್ ಆಗಿವೆ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ
  - `mvn --version` ಪ್ರಯತ್ನಿಸಿ ಇನ್‌ಸ್ಟಾಲೇಶನ್ ಪರಿಶೀಲಿಸಲು

- **ಸಂಪರ್ಕ ಸಮಸ್ಯೆಗಳೇ?** 
  - ನಿಮ್ಮ ಇಂಟರ್ನೆಟ್ ಸಂಪರ್ಕವನ್ನು ಪರಿಶೀಲಿಸಿ
  - GitHub ನಿಮ್ಮ ನೆಟ್‌ವರ್ಕ್‌ನಿಂದ ಪ್ರವೇಶಿಸಬಹುದೇ ಎಂದು ಪರಿಶೀಲಿಸಿ
  - GitHub ಮಾದರಿಗಳ ಎಂಡ್‌ಪಾಯಿಂಟ್ ಅನ್ನು ತಡೆಗಟ್ಟುವ ಫೈರ್‌ವಾಲ್‌ನ ಹಿಂದೆ ನೀವು ಇಲ್ಲವೇ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ

- **ಡೆವ್ ಕಂಟೈನರ್ ಪ್ರಾರಂಭವಾಗುತ್ತಿಲ್ಲವೇ?** 
  - ಡಾಕರ್ ಡೆಸ್ಕ್‌ಟಾಪ್ ಚಲಿಸುತ್ತಿದೆಯೇ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ (ಸ್ಥಳೀಯ ಅಭಿವೃದ್ಧಿಗಾಗಿ)
  - ಕಂಟೈನರ್ ಅನ್ನು ಪುನಃ ನಿರ್ಮಿಸಲು ಪ್ರಯತ್ನಿಸಿ: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **ಅಪ್ಲಿಕೇಶನ್ ಸಂಗ್ರಹಣಾ ದೋಷಗಳೇ?**
  - ನೀವು ಸರಿಯಾದ ಡೈರೆಕ್ಟರಿಯಲ್ಲಿದ್ದೀರಾ ಎಂದು ಖಚಿತಪಡಿಸಿಕೊಳ್ಳಿ: `02-SetupDevEnvironment/examples/github-models`
  - `mvn clean compile` ಪ್ರಯತ್ನಿಸಿ ಶುದ್ಧೀಕರಿಸಿ ಮತ್ತು ಪುನಃ ಸಂಗ್ರಹಿಸಲು

> **ಸಹಾಯ ಬೇಕೇ?**: ಇನ್ನೂ ಸಮಸ್ಯೆಗಳನ್ನು ಎದುರಿಸುತ್ತಿದ್ದೀರಾ? ರೆಪೊಸಿಟರಿಯಲ್ಲಿ ಒಂದು ಸಮಸ್ಯೆಯನ್ನು ತೆರೆಯಿರಿ, ನಾವು ನಿಮಗೆ ಸಹಾಯ ಮಾಡುತ್ತೇವೆ.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಅಸಮಾಕ್ಷ್ಯತೆ**:  
ಈ ದಸ್ತಾವೇಜನ್ನು AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವಾದಿಸಲಾಗಿದೆ. ನಾವು ನಿಖರತೆಯನ್ನು ಸಾಧಿಸಲು ಪ್ರಯತ್ನಿಸುತ್ತಿದ್ದರೂ, ದಯವಿಟ್ಟು ಗಮನಿಸಿ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ತಪ್ಪುಗಳು ಅಥವಾ ಅಸಡ್ಡೆಗಳು ಇರಬಹುದು. ಮೂಲ ಭಾಷೆಯಲ್ಲಿರುವ ಮೂಲ ದಸ್ತಾವೇಜು ಪ್ರಾಮಾಣಿಕ ಮೂಲವೆಂದು ಪರಿಗಣಿಸಬೇಕು. ಮಹತ್ವದ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದವನ್ನು ಬಳಸುವ ಮೂಲಕ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪು ಅರ್ಥಗಳ ಅಥವಾ ತಪ್ಪು ವ್ಯಾಖ್ಯಾನಗಳ ಬಗ್ಗೆ ನಾವು ಹೊಣೆಗಾರರಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->