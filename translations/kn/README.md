<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T07:26:37+00:00",
  "source_file": "README.md",
  "language_code": "kn"
}
-->
# Generative AI for Beginners - Java Edition
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.kn.png)

**Time Commitment**: ಸಂಪೂರ್ಣ ವರ್ಕ್‌ಶಾಪ್ ಸ್ಥಳೀಯ ಸജ್ಜುಗೊಂಡಿಲ್ಲದೆ ಆನ್ಲೈನಲ್ಲೇ ಪೂರ್ಣಗೊಳ್ಳಬಹುದು. ಪರಿಸರವನ್ನು ಸಿದ್ಧಪಡಿಸಲು ಸುಮಾರು 2 ನಿಮಿಷಗಳು ಬೇಕಾಗುತ್ತವೆ, ಮತ್ತು ಮಾದರಿಗಳನ್ನು ಪರಿಶೀಲಿಸಲು ಅವಲೋಕನದ ಆಳೈತೆಯಲ್ಲಿ ಆಧರಿಸಿ 1–3 ಗಂಟೆಗಳು ಬೇಕಾಗಬಹುದು.

> **Quick Start** 

1. ಈ ರೆಪೊಸಿಟರಿಯನ್ನು ನಿಮ್ಮ GitHub ಖಾತೆಗೆ Fork ಮಾಡಿ
2. ಕ್ಲಿಕ್ ಮಾಡಿ **Code** → **Codespaces** ಟ್ಯಾಬ್ → **...** → **New with options...**
3. ಡೀಫಾಲ್ಟ್‌ಗಳನ್ನು ಬಳಸಿ – ಇದು ಈ ಕೋರ್ಸ್‌ಗಾಗಿ ರಚಿಸಲಾದ Development container ಅನ್ನು ಆಯ್ಕೆಮಾಡುತ್ತದೆ
4. ಕ್ಲಿಕ್ ಮಾಡಿ **Create codespace**
5. ಪರಿಸರ ಸಿದ್ಧವಾಗಲು ~2 ನಿಮಿಷಗಳವರೆಗೆ ಕಾಯಿರಿ
6. ನೇರವಾಗಿ [ಪ್ರಥಮ ಉದಾಹರಣೆಗೆ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) ಹೋಗಿ

> **Prefer to Clone Locally?**
>
> ಈ ರೆಪೊಸಿಟರಿ 50+ ಭಾಷಾ અનુವಾದಗಳನ್ನು ಒಳಗೊಂಡಿದೆ, ಇದು ಡೌನ್‌ಲೋಡ್ ಗಾತ್ರವನ್ನು ಬಹಳ ಪ್ರಮಾಣದಲ್ಲಿ ಹೆಚ್ಚಿಸುತ್ತದೆ. ಅನುوادಗಳಿಲ್ಲದೆ ಕ್ಲೋನ್ ಮಾಡಲು sparse checkout ಬಳಸಿ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ಇದು ಕೋರ್ಸ್ ಪೂರ್ಣಗೊಳ್ಳಿಸಲು ನಿಮಗೆ ಬೇಕಾದ ಎಲ್ಲಾ ವಿಷಯಗಳನ್ನು ಒದಗಿಸುತ್ತದೆ ಮತ್ತು ಡೌನ್‌ಲೋಡ್ ಅನ್ನು ಬಹಳ ವೇಗವಾಗಿ ಮಾಡುತ್ತದೆ.


## Multi-Language Support

### Supported via GitHub Action (Automated & Always Up-to-Date)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ಅರೆಬಿಕ್](../ar/README.md) | [ಬಂಗಾಲಿ](../bn/README.md) | [ಬುಲ್ಗೇರಿಯನ್](../bg/README.md) | [ಬರ್ಮೀಸ್ (ಮ್ಯಾನ್‌ಮಾರ್)](../my/README.md) | [ಚೈನೀಸ್ (ಸರಳೀಕೃತ)](../zh/README.md) | [ಚೈನೀಸ್ (ಸಂಪ್ರದಾಯಿಕ, ಹಾಂಗ್ ಕಾಂಗ್)](../hk/README.md) | [ಚೈನೀಸ್ (ಸಂಪ್ರದಾಯಿಕ, ಮಾಕಾವ್)](../mo/README.md) | [ಚೈನೀಸ್ (ಸಂಪ್ರದಾಯಿಕ, ತೈವಾನ್)](../tw/README.md) | [ಕ್ರೊಯೇಷಿಯನ್](../hr/README.md) | [ಚೆಕ್](../cs/README.md) | [ಡ್ಯಾನಿಶ್](../da/README.md) | [ಡಚ್](../nl/README.md) | [ಎಸ್ಟೋನಿಯನ್](../et/README.md) | [ಫಿನ್ನಿಶ್](../fi/README.md) | [ಫ್ರೆಂಚ್](../fr/README.md) | [ಜರ್ಮನ್](../de/README.md) | [ಗ್ರೀಕ್](../el/README.md) | [ಹೀಬ್ರೂ](../he/README.md) | [ಹಿಂದಿ](../hi/README.md) | [ಹಂಗೇರಿಯನ್](../hu/README.md) | [ಇಂಡೋನೇಶಿಯನ್](../id/README.md) | [ಇಟಾಲಿಯನ್](../it/README.md) | [জಪಾನ್‌ese](../ja/README.md) | [ಕನ್ನಡ](./README.md) | [ಕೊರಿಯನ್](../ko/README.md) | [ಲಿಥುವೇನಿಯನ್](../lt/README.md) | [ಮಲಯ್](../ms/README.md) | [ಮಲಯಾಳಂ](../ml/README.md) | [ಮರಾಠಿ](../mr/README.md) | [ನೇಪಾಳಿ](../ne/README.md) | [ನೈಜೀರಿಯನ್ ಪിഡ್ಗಿನ್](../pcm/README.md) | [ನಾರ್ವೇಶಿಯನ್](../no/README.md) | [ಪರ್ಶಿಯನ್ (ಫಾರ್ಸಿ)](../fa/README.md) | [ಪೋಲಿಷ್](../pl/README.md) | [ಪೋರ್ಚುಗೀಸ್ (ಬ್ರೆಜಿಲ್)](../br/README.md) | [ಪೋರ್ಚುಗೀಸ್ (ಪೋರ್ಟುಗಲ್)](../pt/README.md) | [ಪುಂಜಾಬಿ (ಗುರ್ಮುಖಿ)](../pa/README.md) | [ರೊಮೇನಿಯನ್](../ro/README.md) | [ರಷ್ಯನ್](../ru/README.md) | [ಸರ್ಬಿಯನ್ (ಸಿರಿಲಿಕ್)](../sr/README.md) | [ಸ್ಲೋವಾಕ್](../sk/README.md) | [ಸ್ಲೋವೇನಿಯನ್](../sl/README.md) | [ಸ್ಪ್ಯಾನಿಷ್](../es/README.md) | [ಸ್ವಾಹಿಲಿ](../sw/README.md) | [ಸ್ವೀಡಿಷ್](../sv/README.md) | [ಟಾಗಾಲೋಗ್ (ಫಿಲಿಪಿನೋ)](../tl/README.md) | [ತಮಿಳು](../ta/README.md) | [ತೆಲುಗು](../te/README.md) | [ಥಾಯ್](../th/README.md) | [ಟರ್ಕಿಶ್](../tr/README.md) | [ಉಕ್ರೇನಿಯನ್](../uk/README.md) | [ಉರ್ದು](../ur/README.md) | [ವಿಯೆಟ್ನಾಮೀಸ್](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Course Structure & Learning Path

### **ಅಧ್ಯಾಯ 1: ಜನರೇಟಿವ್ ಎಐಗೆ ಪರಿಚಯ**
- **ಪ್ರಮುಖ ಸಂकल्पನೆಗಳು**: ದೊಡ್ಡ ಭಾಷಾ ಮಾದರಿಗಳು, ಟೋಕನ್‌ಗಳು, ಎಂಬೆಡ್ಡಿಂಗ್‌ಗಳು ಮತ್ತು ಎಐ ಸಾಮರ್ಥ್ಯಗಳನ್ನು ಅರ್ಥಮಾಡಿಕೊಳ್ಳುವುದು
- **ಜಾವಾ ಎಐ ಪರಿಸರ**: Spring AI ಮತ್ತು OpenAI SDKs ಕುರಿತು ಅವಲೋಕನ
- **Model Context Protocol**: MCP ಗೆ ಪರಿಚಯ ಮತ್ತು ಎಐ ಏಜೆಂಟ್ ಸಂವಹನದಲ್ಲಿ ಅದರ ಪಾತ್ರ
- **ಪ್ರಾಯೋಗಿಕ ಅನ್ವಯಗಳು**: ಚಾಟ್‌ಬಾಟ್‌ಗಳು ಮತ್ತು ವಿಷಯ ರಚನೆ ಸೇರಿದಂತೆ ವಾಸ್ತವಿಕ ದೃಶ್ಯಗಳು
- **[→ ಪ್ರಾರಂಭಿಸಿ ಅಧ್ಯಾಯ 1](./01-IntroToGenAI/README.md)**

### **ಅಧ್ಯಾಯ 2: ಅಭಿವೃದ್ಧಿ ಪರಿಸರವನ್ನು ಹಾಕಿಕೊಳ್ಳುವುದು**
- **ಬಹು-ಪ್ರೊವೈಡರ್ ಸಂರಚನೆ**: GitHub Models, Azure OpenAI, ಮತ್ತು OpenAI Java SDK ಇಂಟಿಗ್ರೇಶನ್‌ಗಳನ್ನು ಸೆಟ್‌ಅಪ್ ಮಾಡುವುದು
- **Spring Boot + Spring AI**: ಎಂಟರ್‌ಪ್ರೈಸ್ ಎಐ ಅಪ್ಲಿಕೇಶನ್ ಅಭಿವೃದ್ಧಿಗಾಗಿ ಉತ್ತಮ ಅಭ್ಯಾಸಗಳು
- **GitHub Models**: ಪ್ರೋಟೋಟೈಪಿಂಗ್ ಮತ್ತು ಕಲಿಕೆಗೆ ಉಚಿತ ಎಐ ಮಾದರಿ ಪ್ರವೇಶ (ಕ್ರೆಡಿಟ್ ಕಾರ್ಡ್ ಅಗತ್ಯವಿಲ್ಲ)
- **ಅಭಿವೃದ್ಧಿ ಸಾಧನಗಳು**: Docker ಕಂಟೈನರ್‌ಗಳು, VS Code, ಮತ್ತು GitHub Codespaces ಸಂರಚನೆ
- **[→ ಪ್ರಾರಂಭಿಸಿ ಅಧ್ಯಾಯ 2](./02-SetupDevEnvironment/README.md)**

### **ಅಧ್ಯಾಯ 3: ಕೋರ್ ಜನರೇಟಿವ್ ಎಐ ತಂತ್ರಗಳು**
- **ಪ್ರಾಂಪ್ಟ್ ಎಂಜಿನಿಯರಿಂಗ್**: ಅತ್ಯುತ್ತಮ ಎಐ ಮಾದರಿ ಪ್ರತಿಕ್ರಿಯೆಗಾಗಿ ತಂತ್ರಗಳು
- **ಎಂಬೆಡ್ಡಿಂಗ್‌ಗಳು & ವೆಕ್ಟರ್ ಕಾರ್ಯಾಚರಣೆಗಳು**: ಸೆಮ್ಯಾಂಟಿಕ್ ಹುಡುಕಾಟ ಮತ್ತು ಸಾದೃಶ್ಯ ಹೊಂದಾಣಿಕೆಯನ್ನು ಅನುಷ್ಠಾನಗೊಳಿಸಿ
- **Retrieval-Augmented Generation (RAG)**: ನಿಮ್ಮದೇ ಡೇಟಾ ಮೂಲಗಳೊಂದಿಗೆ ಎಐ ಅನ್ನು ಸಂಯೋಜಿಸಿ
- **ಫಂಕ್ಷನ್ ಕಾಲಿಂಗ್**: ಕಸ್ಟಮ್ ಉಪಕರಣಗಳು ಮತ್ತು ಪ್ಲಗಿನ್‌ಗಳೊಂದಿಗೆ ಎಐ ಸಾಮರ್ಥ್ಯಗಳನ್ನು ವಿಸ್ತರಿಸಿ
- **[→ ಪ್ರಾರಂಭಿಸಿ ಅಧ್ಯಾಯ 3](./03-CoreGenerativeAITechniques/README.md)**

### **ಅಧ್ಯಾಯ 4: ಪ್ರಾಯೋಗಿಕ ಅನ್ವಯಗಳು & ಪ್ರಾಜೆಕ್ಟ್ಗಳು**
- **Pet Story Generator** (`petstory/`): GitHub Models ಬಳಸಿ ಸೃಜನಾತ್ಮಕ ವಿಷಯ ರಚನೆ
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK ಮೂಲಕ ಸ್ಥಳೀಯ ಎಐ ಮಾದರಿ ಇಂಟಿಗ್ರೇಶನ್
- **MCP Calculator Service** (`calculator/`): Spring AI ಜೊತೆ ಮೂಲ Model Context Protocol ಅನುಷ್ಠಾನ
- **[→ ಪ್ರಾರಂಭಿಸಿ ಅಧ್ಯಾಯ 4](./04-PracticalSamples/README.md)**

### **ಅಧ್ಯಾಯ 5: ಜವಾಬ್ದಾರಿಯುತ ಎಐ ಅಭಿವೃದ್ಧಿ**
- **GitHub Models ಸುರಕ್ಷತೆ**: ಒಳಗೆ ನಿರ್ಮಿತ ವಿಷಯ ಫಿಲ್ಟರಿಂಗ್ ಮತ್ತು ಸುರಕ್ಷತಾ ಸಂಯಂತ್ರಗಳನ್ನು (ಕಠಿಣ ತಡೆಗಳು ಮತ್ತು ಮೃದು ನಿರಾಕರಣೆಗಳು) ಪರೀಕ್ಷಿಸಿ
- **ಜವಾಬ್ದಾರಿಯುತ ಎಐ ಡೆಮೊ**: ಆಧುನಿಕ ಎಐ ಸುರಕ್ಷತಾ ವ್ಯವಸ್ಥೆಗಳು ಪ್ರಾಯೋಗಿಕವಾಗಿ ಹೇಗೆ ಕೆಲಸ ಮಾಡುತ್ತವೆ ಎಂಬುದರ ಕೈಯಲ್ಲಿ ಉದಾಹರಣೆ
- **ಉತ್ತಮ ಅಭ್ಯಾಸಗಳು**: ನೈತಿಕ ಎಐ ಅಭಿವೃದ್ಧಿ ಮತ್ತು ನಿಯೋಜನೆಗಾಗಿ ಅಗತ್ಯ ಮಾರ್ಗದರ್ಶಿಗಳು
- **[→ ಪ್ರಾರಂಭಿಸಿ ಅಧ್ಯಾಯ 5](./05-ResponsibleGenAI/README.md)**

## Additional Resources

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative AI Series
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Core Learning
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![ಆರಂಭಿಕರಿಗಾಗಿ IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ಆರಂಭಿಕರಿಗಾಗಿ XR ಅಭಿವೃದ್ಧಿ](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot ಸರಣಿಗಳು
[![AI ಜೊತೆ ಜೋಡಿ ಪ್ರೋಗ್ರಾಮಿಂಗ್‌ಗಾಗಿ Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NETಗಾಗಿ Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot ಸಾಹಸ](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ಸಹಾಯ ಪಡೆಯುವುದು

ನೀವು ಅಡಚಣೆಗೆ ಸಿಕ್ಕಿದ್ದರೆ ಅಥವಾ AI ಅಪ್ಲಿಕೇಶನ್‌ಗಳನ್ನು ನಿರ್ಮಿಸುವ ಬಗ್ಗೆ ಯಾವುದೇ ಪ್ರಶ್ನೆಗಳಿದ್ದರೆ, MCP ಕುರಿತು ಚರ್ಚೆಗಳಲ್ಲಿ ಇತರ ಅಧ್ಯಯನಕರ್ತರು ಮತ್ತು ಅನುಭವಜ್ಞ ಅಭಿವೃದ್ಧಿಕಾರರ ಜೊತೆ ಸೇರಿ. ಇದೊಂದು ಪ್ರಶ್ನೆಗಳನ್ನು ಸ್ವಾಗತಿಸುವ ಮತ್ತು ಜ್ಞಾನವನ್ನು ಮುಕ್ತವಾಗಿ ಹಂಚಿಕೊಳ್ಳುವ ಬೆಂಬಲದ ಸಮುದಾಯವಾಗಿದೆ.

[![Microsoft Foundry ಡಿಸ್ಕೋರ್ಡ್](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ನೀವು ಉತ್ಪನ್ನ ಪ್ರತಿಕ್ರಿಯೆ ಅಥವಾ ನಿರ್ಮಿಸುವಾಗ ದೋಷಗಳು ಕಂಡುಬಂದರೆ ಭೇಟಿ ಮಾಡಿ:

[![Microsoft Foundry ಡೆವಲಪರ್ ಫೋರಮ್](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
ಜವಾಬ್ದಾರಿ ನಿರಾಕರಣೆ:
ಈ ದಸ್ತಾವೇಜನ್ನು AI ಅನುವಾದ ಸೇವೆ [Co-op Translator](https://github.com/Azure/co-op-translator) ಬಳಸಿ ಅನುವದಿಸಲಾಗಿದೆ. ನಾವು ನಿಖರತೆಯನ್ನು ಸಾಧಿಸಲು ಪ್ರಯತ್ನಿಸುತ್ತೇವೆ ಎಂದು ಖಚಿತಪಡಿಸಿದರೂ, ಸ್ವಯಂಚಾಲಿತ ಅನುವಾದಗಳಲ್ಲಿ ತಪ್ಪುಗಳು ಅಥವಾ ಅನಿಖರತೆಗಳು ಇರಬಹುದೆಂದು ದಯವಿಟ್ಟು ಗಮನದಲ್ಲಿರಿಸಿ. ಮೂಲ ದಸ್ತಾವೇಜನ್ನು ಅದರ ಸ್ಥಳೀಯ/ಸ್ವದೇಶಿ ಭಾಷೆಯಲ್ಲಿ ಅಧಿಕೃತ ಮೂಲವಾಗಿ ಪರಿಗಣಿಸಬೇಕು. ಗಂಭೀರ ಮಾಹಿತಿಗೆ ವೃತ್ತಿಪರ ಮಾನವ ಅನುವಾದವನ್ನು ಶಿಫಾರಸು ಮಾಡಲಾಗುತ್ತದೆ. ಈ ಅನುವಾದದ ಬಳಕೆಯಿಂದ ಉಂಟಾಗುವ ಯಾವುದೇ ತಪ್ಪು ಗ್ರಹಿಕೆಗಳು ಅಥವಾ ತಪ್ಪಾಗಿ ವ್ಯಾಖ್ಯಾನಗಳಿಗೆ ನಾವು ಜವಾಬ್ದಾರರಾಗುವುದಿಲ್ಲ.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->