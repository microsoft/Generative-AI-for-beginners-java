<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T10:06:26+00:00",
  "source_file": "README.md",
  "language_code": "kn"
}
-->
# ಆರಂಭಿಕರಿಗೆ ಜನರೇಟಿವ್ AI - ಜಾವಾ ಆವೃತ್ತಿ
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/kn/beg-genai-series.8b48be9951cc574c.webp)

**ಸಮಯ ಬದ್ಧತೆ**: ಸಂಪೂರ್ಣ ಕಾರ್ಯಾಗಾರವನ್ನು ಸ್ಥಳೀಯ ವ್ಯವಸ್ಥಾಪನೆ ಇಲ್ಲದೆ ಆನ್‌ಲೈನ್‌ನಲ್ಲಿ ಪೂರ್ಣಗೊಳಿಸಬಹುದು. ಪರಿಸರ ವ್ಯವಸ್ಥೆಯನ್ನು ಸ್ಥಾಪಿಸಲು 2 ನಿಮಿಷಗಳು ಬೇಕಾಗಿದ್ದು, ಉದಾಹರಣೆಗಳನ್ನು ಪರಿಶೀಲಿಸಲು ಅನುಸರಿಸುವ ಆಳತೆಗೆ ಅವಲಂಬಿಸಿದಂತೆ 1-3 ಗಂಟೆಗಳು ಬೇಕಾಗಬಹುದು.

> **ತ್ವರಿತ ಪ್ರಾರಂಭ**

1. ಈ ರೆಪೊಸಿಟರಿಯನ್ನು ನಿಮ್ಮ GitHub ಖಾತೆಗೆ ಫೋರ್ಕ್ ಮಾಡಿ
2. ಕ್ಲಿಕ್ ಮಾಡಿ **Code** → **Codespaces** ಟ್ಯಾಬ್ → **...** → **New with options...**
3. ಡೀಫಾಲ್ಟ್ ಬಳಸಿ – ಇದು ಈ ಕೋರ್ಸ್‌ಗಾಗಿ ರಚಿಸಲಾದ ಡೆವಲಪ್‌ಮೆಂಟ್ ಕಂಟೇನರ್ ಆಯ್ಕೆಮಾಡುತ್ತದೆ
4. ಕ್ಲಿಕ್ ಮಾಡಿ **Create codespace**
5. ಪರಿಸರ ಸಿದ್ಧವಾಗಲು ~2 ನಿಮಿಷಗಳ ಕಾಲ ಕಾಯಿರಿ
6. ನೇರವಾಗಿ [ಮೊದಲ ಉದಾಹರಣೆಗೆ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) ಸರಿಸಿ

> **ಸ್ಥಳೀಯವಾಗಿ ಕ್ಲೋನ್ ಮಾಡಲು ಇಚ್ಛಿಸುವಿರಾ?**
>
> ಈ ರೆಪೊಸಿಟರಿಯಲ್ಲಿ 50+ ಭಾಷಾ ಅನುವಾದಗಳು ಇವೆ, ಇದು ಡೌನ್‌ಲೋಡ್ ಗಾತ್ರವನ್ನು ಬಹಳ ಹೆಚ್ಚಿಸುತ್ತದೆ. ಅನುವಾದಗಳಿಲ್ಲದೆ ಕ್ಲೋನ್ ಮಾಡಲು ಸ್ಪಾರ್ಸ್ ಔಟ್‌ಚೆಕ್ ಬಳಸಿ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ಇದರಿಂದ ಕೋರ್ಸ್‌ ಪೂರ್ಣಗೊಳಿಸಲು ಬೇಕಾದ ಎಲ್ಲಾ ವಿಷಯಗಳನ್ನು ಮತ್ತಷ್ಟು ವೇಗವಾಗಿ ಡೌನ್‌ಲೋಡ್ ಮಾಡಬಹುದು.

## ಬಹು-ಭಾಷಾ ಬೆಂಬಲ

### GitHub Action ಮೂಲಕ ಬೆಂಬಲಿತ (ಸ್ವಯಂಚಾಲಿತ & ಸದಾ ನವೀಕರಿಸಿದ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ಅರೇಬಿಕ್](../ar/README.md) | [ಬಂಗಾಳಿ](../bn/README.md) | [ಬಲ್ಗೇರಿಯನ್](../bg/README.md) | [ಬರ್ಮೀಸ್ (ಮಯಾನ್ಮಾರ್)](../my/README.md) | [ಚೀನೀಸ್ (ಸರಳೀಕೃತ)](../zh/README.md) | [ಚೀನೀಸ್ (ಪಾರಂಪರಿಕ, ಹಾಂಗ್ ಕಾಂಗ್)](../hk/README.md) | [ಚೀನೀಸ್ (ಪಾರಂಪರಿಕ, ಮಾರಾಕ್ಯು)](../mo/README.md) | [ಚೀನೀಸ್ (ಪಾರಂಪರಿಕ, ತೈವಾನ್)](../tw/README.md) | [ಕ್ರೋಯೇಷಿಯನ್](../hr/README.md) | [ಸೆಕ್](../cs/README.md) | [ಡ್ಯಾನಿಶ್](../da/README.md) | [ಡಚ್](../nl/README.md) | [ಎಸ್ಟೋನಿಯನ್](../et/README.md) | [ಫಿನ್ನಿಶ್](../fi/README.md) | [ಫ್ರೆಂಚ್](../fr/README.md) | [ಜರ್ಮನ್](../de/README.md) | [ಗ್ರೀಕ್](../el/README.md) | [ಹೀಬ್ರೂ](../he/README.md) | [ಹಿಂದಿ](../hi/README.md) | [ಹಂಗೇರಿಯನ್](../hu/README.md) | [ಇಂಡೋನೇಶಿಯನ್](../id/README.md) | [ಇಟಾಲಿಯನ್](../it/README.md) | [ಜಪಾನೀಸ್](../ja/README.md) | [ಕನ್ನಡ](./README.md) | [ಕೊರಿಯನ್](../ko/README.md) | [ಲಿಥೋನಿಯನ್](../lt/README.md) | [ಮಲೇ](../ms/README.md) | [ಮಲಯಾಳಂ](../ml/README.md) | [ಮರಾಠಿ](../mr/README.md) | [ನೆಪಾಳಿ](../ne/README.md) | [ನಿಜೀರಿಯನ್ ಪಿಡ್ಗಿನ್](../pcm/README.md) | [ನಾರ್ವೇಜಿಯನ್](../no/README.md) | [ಪರ್ಷಿಯನ್ (ಫಾರ್ಸಿ)](../fa/README.md) | [ಪೋಲಿಷ್](../pl/README.md) | [ಪೋರ್ಚುಗೀಸ್ (ಬ್ರೆಜಿಲ್)](../br/README.md) | [ಪೋರ್ಚುಗೀಸ್ (ಪೋರ್ಚುಗಲ್)](../pt/README.md) | [ಪಂಜಾಬಿ (ಗುರ್ಮುಖಿ)](../pa/README.md) | [ರೊಮಾನಿಯನ್](../ro/README.md) | [ರಷ್ಯನ್](../ru/README.md) | [ಸರ್ಬಿಯನ್ (ಸಿರಿಲಿಕ್)](../sr/README.md) | [ಸ್ಲೋವಾಕ್](../sk/README.md) | [ಸ್ಲೋವೇನಿಯನ್](../sl/README.md) | [ಸ್ಪ್ಯಾನಿಷ್](../es/README.md) | [ಸ್ವಾಹಿಲಿ](../sw/README.md) | [ಸ್ವೀಡಿಷ್](../sv/README.md) | [ಟಾಗಾಲೋಗ್ (ಫಿಲಿಪಿನೋ)](../tl/README.md) | [ತಮಿಳು](../ta/README.md) | [ತೆಲುಗು](../te/README.md) | [ಥಾಯ್](../th/README.md) | [ಟರ್ಕಿಶ್](../tr/README.md) | [ಉಕ್ರೇನಿಯನ್](../uk/README.md) | [ಉರ್ದು](../ur/README.md) | [ವಿಯೆಟ್ನಾಮೀಸ್](../vi/README.md)

> **ಸ್ಥಳೀಯವಾಗಿ ಕ್ಲೋನ್ ಮಾಡಲು ಇಚ್ಛಿಸುವಿರಾ?**

> ಈ ರೆಪೊಸಿಟರಿಯಲ್ಲಿ 50+ ಭಾಷಾ ಅನುವಾದಗಳು ಇವೆ, ಇದು ಡೌನ್‌ಲೋಡ್ ಗಾತ್ರವನ್ನು ಬಹಳ ಹೆಚ್ಚಿಸುತ್ತದೆ. ಅನುವಾದಗಳಿಲ್ಲದೆ ಕ್ಲೋನ್ ಮಾಡಲು ಸ್ಪಾರ್ಸ್ ಔಟ್‌ಚೆಕ್ ಬಳಸಿ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ಇದರಿಂದ ಕೋರ್ಸ್‌ ಪೂರ್ಣಗೊಳಿಸಲು ಬೇಕಾದ ಎಲ್ಲಾ ವಿಷಯಗಳನ್ನು ಮತ್ತಷ್ಟು ವೇಗವಾಗಿ ಡೌನ್‌ಲೋಡ್ ಮಾಡಬಹುದು.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ಕೋರ್ಸ್ ರಚನೆ ಮತ್ತು ಕಲಿಕೋದ್ದೇಶ

### **ಅಧ್ಯಾಯ 1: ಜನರೇಟಿವ್ AI ಗೆ ಪರಿಚಯ**
- **ಮૂಲೋತ್ಪತ್ತಿ ತತ್ವಗಳು**: ದೊಡ್ಡ ಭಾಷಾ ಮಾದರಿಗಳ, ಟೋಕನ್ಸ್, ಎಂಬರ್‍ಡಿಂಗ್‌ಗಳು ಮತ್ತು AI ಸಾಮರ್ಥ್ಯಗಳ ಅರ್ಥ
- **ಜಾವಾ AI ಪರಿಸರ**: ಸ್ಪ್ರಿಂಗ್ AI ಮತ್ತು OpenAI SDK ಗಳ ಪರಿಚಯ
- **ಮಾದರಿ ಸಾಂದರ್ಭಿಕ ಪ್ರೋಟೋಕಾಲ್**: MCP ಮತ್ತು AI ಏಜೆಂಟ್ ಸಂವಹನದಲ್ಲಿ ಅದರ ಪಾತ್ರ
- **ಪ್ರಾಯೋಗಿಕ ಅನ್ವಯಗಳು**: ಚಾಟ್‌ಬಾಟ್‌ಗಳು ಮತ್ತು ವಿಷಯ ಉತ್ಪಾದನೆহಾಗಾದ ವಾಸ್ತವಿಕ ಪರಿಸ್ಥಿತಿಗಳು
- **[→ ಅಧ್ಯಾಯ 1 ಪ್ರಾರಂಭಿಸಿ](./01-IntroToGenAI/README.md)**

### **ಅಧ್ಯಾಯ 2: ಡೆವಲಪ್‌ಮೆಂಟ್ ಪರಿಸರ ಸ್ಥಾಪನೆ**
- **ಬಹು-ಪ್ರದಾತೃ ಸಂರಚನೆ**: GitHub Models, Azure OpenAI, ಮತ್ತು OpenAI Java SDK ಸಂಯೋಜನೆ
- **ಸ್ಪ್ರಿಂಗ್ ಬೂಟ್ + ಸ್ಪ್ರಿಂಗ್ AI**: ಎಂಟರ್ಪ್ರೈಸ್ AI ಅನ್ವಯದ ಉತ್ತಮ ಅಭ್ಯಾಸಗಳು
- **GitHub Models**: ಪ್ರೋಟೋಟೈಪಿಂಗ್ ಮತ್ತು ಕಲಿಕೆಗೆ ಉಚಿತ AI ಮಾದರಿ ಪ್ರವೇಶ (ಕ್ರೆಡಿಟ್ ಕಾರ್ಡ್ ಅಗತ್ಯವಿಲ್ಲ)
- **ಡೆವಲಪ್‌ಮೆಂಟ್ ಸಾಧನಗಳು**: ಡಾಕರ್ ಕಂಟೇನರ್‌ಗಳು, VS ಕೋಡ್ ಮತ್ತು GitHub Codespaces ಸಂರಚನೆ
- **[→ ಅಧ್ಯಾಯ 2 ಪ್ರಾರಂಭಿಸಿ](./02-SetupDevEnvironment/README.md)**

### **ಅಧ್ಯಾಯ 3: ಮುಖ್ಯ ಜನರೇಟಿವ್ AI ತಂತ್ರಗಳು**
- **ಪ್ರಾಂಪ್ಟ್ ಎಂಜಿನಿಯರಿಂಗ್**: ಪರಿಪೂರ್ಣ AI ಮಾದರಿ ಪ್ರತಿಕ್ರಿಯೆಗಾಗಿ ತಂತ್ರಗಳು
- **ಎಂಬರ್‍ಡಿಂಗ್‌ಗಳು ಮತ್ತು ವೆಕ್ಟರ್ ಕಾರ್ಯಾಚರಣೆಗಳು**: ಅರ್ಥಪೂರ್ಣ ಹುಡುಕಾಟ ಮತ್ತು ಸ್ಪಷ್ಟತಾ ಹೊಂದಾಣಿಕೆ
- **ಪುನರುದ್ಧಾರ-ಸಂಬಂಧಿತ ಉತ್ಪಾದನೆ (RAG)**: ನಿಮ್ಮ ಸ್ವಂತ ಡೇಟಾ ಸಂಪನ್ಮೂಲಗಳೊಂದಿಗೆ AI ಸಂಯೋಜನೆ
- **ಫಂಕ್ಷನ್ ಕರೆ**: ಕಸ್ಟಮ್ ಸಾಧನಗಳು ಮತ್ತು ಪ್ಲಗ್ ಇನ್‌ಗಳೊಂದಿಗೆ AI ಸಾಮರ್ಥ್ಯ ವಿಸ್ತರಣೆ
- **[→ ಅಧ್ಯಾಯ 3 ಪ್ರಾರಂಭಿಸಿ](./03-CoreGenerativeAITechniques/README.md)**

### **ಅಧ್ಯಾಯ 4: ಪ್ರಾಯೋಗಿಕ ಅನ್ವಯಗಳು ಮತ್ತು ಪ್ರಾಜೆಕ್ಟ್ಗಳು**
- **ಪೆಟ್ ಕಥೆ ಜನರೇಟರ್** (`petstory/`): GitHub Models ಬಳಸಿ ಸೃಜನಾತ್ಮಕ ವಿಷಯ ರಚನೆ
- **ಫೌಂಡರಿ ಸ್ಥಳೀಯ ಪ್ರದರ್ಶನ** (`foundrylocal/`): OpenAI Java SDK ಬಳಸಿ ಸ್ಥಳೀಯ AI ಮಾದರಿ ಸಂಯೋಜನೆ
- **MCP ಕ್ಯಾಲ್ಕ್ಯುಲೇಟರ್ ಸೇವೆ** (`calculator/`): ಸ್ಪ್ರಿಂಗ್ AI ಸಹಿತ ಮೂಲ Model Context ಪ್ರೋಟೋಕಾಲ್ ಅನ್ವಯ
- **[→ ಅಧ್ಯಾಯ 4 ಪ್ರಾರಂಭಿಸಿ](./04-PracticalSamples/README.md)**

### **ಅಧ್ಯಾಯ 5: ಜವಾಬ್ದಾರಿಯುತ AI ಅಭಿವೃದ್ಧಿ**
- **GitHub Models ಸುರಕ್ಷತೆ**: ನಿರ್ಮಿತ ವಿಷಯ ಪರಿಸರೀಕರಣ ಮತ್ತು ಸುರಕ್ಷತಾ ಯಂತ್ರಗಳನ್ನು ಪರೀಕ್ಷೆ ಮಾಡಲಾಗುತ್ತದೆ (ಕಠಿಣ ತಡೆಗಳು ಮತ್ತು ಮೃದು ನಿರಾಕರಣೆಗಳು)
- **ಜವಾಬ್ದಾರಿಯುತ AI ಪ್ರದರ್ಶನ**: ಆಧುನಿಕ AI ಸುರಕ್ಷತಾ ವ್ಯವಸ್ಥೆಗಳು ಪ್ರಾಯೋಗಿಕವಾಗಿ ಹೇಗೆ ಕೆಲಸಮಾಡುತ್ತವೆ ಎಂಬ ಉದಾಹರಣೆ
- **ಉತ್ತಮ ಅಭ್ಯಾಸಗಳು**: ನೈತಿಕ AI ಅಭಿವೃದ್ಧಿ ಮತ್ತು ನಿಯೋಜನೆಗಾಗಿ ಅಗತ್ಯ ಮಾರ್ಗಸೂಚಿಗಳು
- **[→ ಅಧ್ಯಾಯ 5 ಪ್ರಾರಂಭಿಸಿ](./05-ResponsibleGenAI/README.md)**

## ಹೆಚ್ಚುವರಿ ಸಂಪನ್ಮೂಲಗಳು

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### ಲಾಂಗ್‌ಚೈನ್
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### ಅಜೂರ್ / ಎಡ್ಜ್ / MCP / ಏಜೆಂಟ್‌ಗಳು
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ಜನರೇಟಿವ್ AI ಸರಣಿ
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ಮೂಲ ಕಲಿಕೆ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ಆರಂಭಿಕರಿಗೆ ಐಒಟಿ](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ಆರಂಭಿಕರಿಗೆ XR ಅಭಿವೃದ್ಧಿ](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ಕೋಪೈಲಟ್ ಸರಣಿ
[![AI ಜೋಡಣೆ ಕಾರ್ಯಕ್ರಮಕ್ಕಾಗಿ ಕೋಪೈಲಟ್](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NETಗಾಗಿ ಕೋಪೈಲಟ್](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![ಕೋಪೈಲಟ್ ಸಾಹಸ](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ಸಹಾಯ ಪಡೆಯುವುದು

ನೀವು ಅಡಗಿದಿದ್ದರೆ ಅಥವಾ AI ಅಪ್ಲಿಕೇಷನ್ಸ್ ನಿರ್ಮಿಸುವ ಬಗ್ಗೆ ಯಾವುದೇ ಪ್ರಶ್ನೆಗಳಿದ್ದರೆ, MCP ಕುರಿತು ಸಹರಸಿ ಕಲಿಯುವವರು ಮತ್ತು ಅನುಭವೀ ಡೆವಲಪರ್‌ಗಳ ચર્ચೆಗಳಲ್ಲಿ ಸೇರಿ. ಇದು ಪ್ರೊತ್ಸಾಹಕಾರಿ ಸಮುದಾಯವಾಗಿದ್ದು, ಇಲ್ಲಿ ಪ್ರಶ್ನೆಗಳನ್ನು ಸ್ವಾಗತಿಸಲಾಗುತ್ತದೆ ಮತ್ತು ಜ್ಞಾನವನ್ನು ಮುಕ್ತವಾಗಿ ಹಂಚಿಕೊಳ್ಳಲಾಗುತ್ತದೆ.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ನಿಮ್ಮ ಲೇಖನ ಅಭಿಪ್ರಾಯ ಅಥವಾ ನಿರ್ಮಾಣ ಸಂದರ್ಭದಲ್ಲಿ ದೋಷಗಳಿದ್ದರೆ ಸಂಗ್ರಹಿಸಿ ಭೇಟಿ ನೀಡಿ:

[![Microsoft Foundry ಡೆವಲಪರ್ ಫೋರಂ](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ಒಪ್ಪಿಕಾ**:  
ಈ ದಾಖಲೆ AI ಭಾಷಾಂತರ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವಾದಿಸಲಾಯಿತು. ನಾವು ಶುದ್ದತೆಯಿಗಾಗಿ ಪ್ರಯತ್ನಿಸಿದ್ದರೂ, ಸ್ವಯಂಕ್ರಿಯ ಭಾಷಾಂತರಗಳಲ್ಲಿ ದೋಷಗಳ ಅಥವಾ ತಪ್ಪುಗಳಿದ್ದುಕಡೆಗೂ ಮನಸ್ಸಿನಲ್ಲಿ ಇಟ್ಟುಕೊಳ್ಳಿ. ಮೂಲ ದಾಖಲೆ ತನ್ನ ಮೂಲ ಭಾಷೆಯಲ್ಲಿ ಪ್ರಾಮಾಣಿಕ ಮೂಲವಾಗಿ ಪರಿಗಣಿಸುವುದು ಉತ್ಕೃಷ್ಟ. ಪ್ರಮುಖ ಮಾಹಿತಿಗಾಗಿ, ವೃತ್ತಿಪರ ಮಾನವ ಭಾಷಾಂತರವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗಿದೆ. ಈ ಭಾಷಾಂತರ ಬಳಕೆಯಿಂದ ಉಂಟಾಗುವ ಯಾವುದೇ ಅರ್ಥಗುಂಜಲಿಕೆ ಅಥವಾ ತಪ್ಪು ಅರ್ಥಗಳನ್ನು ನಾವು ಹೊಣೆ ಹೊಲಿಸಾಮಾಡುವುದಿಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->