# ਮੁੱਢਲੀਆਂ ਲਈ ਜੈਨਰੇਟਿਵ ਏਆਈ - ਜਾਵਾ ਐਡੀਸ਼ਨ
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ਮੁੱਢਲੀਆਂ ਲਈ ਜੈਨਰੇਟਿਵ ਏਆਈ - ਜਾਵਾ ਐਡੀਸ਼ਨ](../../translated_images/pa/beg-genai-series.8b48be9951cc574c.webp)

**ਸਮਾਂ ਦਾਨ**: ਸਾਰਾ ਵਰਕਸ਼ਾਪ ਔਨਲਾਈਨ ਪੂਰਾ ਕੀਤਾ ਜਾ ਸਕਦਾ ਹੈ ਬਿਨਾਂ ਸਥਾਨਕ ਸੈਟਅੱਪ ਦੇ। ਵਾਤਾਵਰਣ ਸੈਟਅੱਪ 2 ਮਿੰਟ ਲੈਂਦਾ ਹੈ, ਸੈਂਪਲ ਖੋਜਣ ਲਈ 1-3 ਘੰਟੇ ਲੱਗ ਸਕਦੇ ਹਨ ਖੋਜ ਦੀ ਗਹਿਰਾਈ ਦੇ ਅਨੁਸਾਰ।

> **ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ**

1. ਇਸ ਭੰਡਾਰ ਨੂੰ ਆਪਣੇ GitHub ਖਾਤੇ ਵਿੱਚ ਫੋਰਕ ਕਰੋ
2. ਕਲਿੱਕ ਕਰੋ **Code** → **Codespaces** ਟੈਬ → **...** → **New with options...**
3. ਡਿਫ਼ੌਲਟ ਵਰਤੋ – ਇਹ ਇਸ ਕੋਰਸ ਲਈ ਬਣਾਇਆ ਗਿਆ ਡਿਵੈਲਪਮੈਂਟ ਕੰਟੇਨਰ ਚੁਣੇਗਾ
4. ਕਲਿੱਕ ਕਰੋ **Create codespace**
5. ਲਗਭਗ 2 ਮਿੰਟ ਇੰਤਜ਼ਾਰ ਕਰੋ ਵਾਤਾਵਰਣ ਤਿਆਰ ਹੋਣ ਲਈ
6. ਸਿੱਧਾ [ਪਹਿਲਾ ਉਦਾਹਰਨ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) ਤੇ ਜਾਓ

## ਬਹੁ-ਭਾਸ਼ਾਈ ਸਹਿਯੋਗ

### GitHub ਐਕਸ਼ਨ ਰਾਹੀਂ ਸਹਿਯੋਗ (ਆਟੋਮੈਟਿਕ ਅਤੇ ਸਦਾ ਅੱਪਟੂਡੇਟ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](./README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ਅਸੀਂ ਸਥਾਨਕ ਕਲੋਨ ਕਰਨਾ ਪਸੰਦ ਕਰਦੇ ਹੋ?**
>
> ਇਸ ਭੰਡਾਰ ਵਿੱਚ 50+ ਭਾਸ਼ਾਈ ਅਨੁਵਾਦ ਸ਼ਾਮਿਲ ਹਨ ਜੋ ਡਾਊਨਲੋਡ ਸਾਈਜ਼ ਵੱਡਾ ਕਰਦੇ ਹਨ। ਬਿਨਾਂ ਅਨੁਵਾਦਾਂ ਦੇ ਕਲੋਨ ਕਰਨ ਲਈ sparse checkout ਵਰਤੋਂ:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> ਇਹ ਤੁਹਾਨੂੰ ਕੋਰਸ ਪੂਰਾ ਕਰਨ ਲਈ ਲੋੜੀਂਦੇ ਸਾਰੇ ਚੀਜ਼ਾਂ ਤੇਜ਼ੀ ਨਾਲ ਡਾਊਨਲੋਡ ਕਰਕੇ ਦਿੰਦਾ ਹੈ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ਕੋਰਸ ਦਾ ਢਾਂਚਾ ਅਤੇ ਸਿੱਖਣ ਦਾ ਰਾਸਤਾ

### **ਅਧਿਆਇ 1: ਜੈਨਰੇਟਿਵ ਏਆਈ ਦਾ ਪਰਿਚਯ**
- **ਮੁੱਖ ਧਾਰਨਾਵਾਂ**: ਵੱਡੇ ਭਾਸ਼ਾਈ ਮਾਡਲਾਂ ਦੀ ਸਮਝ, ਟੋਕਨ, ਐਮਬੈਡਿੰਗਜ਼, ਅਤੇ ਏਆਈ ਸਮਰੱਥਾਵਾਂ
- **ਜਾਵਾ ਏਆਈ ਇਕੋਸਿਸਟਮ**: ਸਪ੍ਰਿੰਗ ਏਆਈ ਅਤੇ ਓਪਨਏਆਈ SDKs ਦਾ ਸਰੋਤ
- **ਮਾਡਲ ਕੁਨਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ**: MCP ਦਾ ਪਰਿਚਯ ਅਤੇ ਏਆਈ ਏਜੰਟ ਸੰਚਾਰ ਵਿੱਚ ਭੂਮਿਕਾ
- **ਵਾਹੀਵਾਹਿਕ ਅਰਜ਼ੀਆਂ**: ਚੈਟਬੋਟ ਅਤੇ ਸਮੱਗਰੀ ਪੈਦਾ ਕਰਨ ਵਰਗੇ ਅਸਲੀ ਜਗ੍ਹਾ ਦੇ ਸੰਦਰਭ
- **[→ ਅਧਿਆਇ 1 ਸਿੱਖਣਾ ਸ਼ੁਰੂ ਕਰੋ](./01-IntroToGenAI/README.md)**

### **ਅਧਿਆਇ 2: ਵਿਕਾਸ ਵਾਤਾਵਰਣ ਸੈਟਅੱਪ**
- **ਬਹੁ-ਪ੍ਰਦਾਤਾ ਸੰਰਚਨਾ**: GitHub ਮਾਡਲ, ਐਜ਼ਰ ਓਪਨਏਆਈ ਅਤੇ ਓਪਨਏਆਈ ਜਾਵਾ SDK ਇੰਟੇਗਰੇਸ਼ਨ ਸੈਟਅੱਪ ਕਰੋ
- **ਸਪ੍ਰਿੰਗ ਬੂਟ + ਸਪ੍ਰਿੰਗ ਏਆਈ**: ਏਂਟਰਪ੍ਰਾਈਜ਼ AI ਐਪਲੀਕੇਸ਼ਨ ਵਿਕਾਸ ਲਈ ਸਰੋਤ ਸਿਖਲਾਈ
- **GitHub ਮਾਡਲ**: ਪ੍ਰੋਟੋਟਾਈਪਿੰਗ ਅਤੇ ਸਿੱਖਣ ਲਈ ਮੁਫ਼ਤ ਏਆਈ ਮਾਡਲ ਐਕਸੈਸ (ਕੋਈ ਕ੍ਰੈਡਿਟ ਕਾਰਡ ਨਹੀਂ ਲੋੜੀਂਦਾ)
- **ਡਿਵੈਲਪਮੈਂਟ ਟੂਲਜ਼**: ਡੋਕਰ ਕੰਟੇਨਰ, VS ਕੋਡ, ਅਤੇ GitHub ਕੋਡਸਪੇਸ ਸੰਰਚਨਾ
- **[→ ਅਧਿਆਇ 2 ਸਿੱਖਣਾ ਸ਼ੁਰੂ ਕਰੋ](./02-SetupDevEnvironment/README.md)**

### **ਅਧਿਆਇ 3: ਕੋਰ ਜੈਨਰੇਟਿਵ ਏਆਈ ਤਕਨੀਕਾਂ**
- **ਪ੍ਰੰਪਟ ਇੰਜੀਨੀਅਰਿੰਗ**: ਵਧੀਆ AI ਮਾਡਲ ਜਵਾਬਾਂ ਲਈ ਤਕਨੀਕਾਂ
- **ਐਮਬੈਡਿੰਗਜ਼ ਅਤੇ ਵੇਕਟਰ ਓਪਰੇਸ਼ਨਜ਼**: ਸੇਮੈਂਟਿਕ ਖੋਜ ਅਤੇ ਸਮਾਨਤਾ ਮੇਲਵਟ ਮੁਕੰਮਲ ਕਰੋ
- **ਰੀਟ੍ਰੀਵਲ-ਅਗਮੇਂਟੇਡ ਜੈਨਰੇਸ਼ਨ (RAG)**: ਆਪਣੇ ਡੇਟਾ ਸਰੋਤਾਂ ਨਾਲ AI ਜੋੜੋ
- **ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ**: ਨਿੱਜੀ ਟੂਲਾਂ ਅਤੇ ਪਲੱਗਇਨਾਂ ਨਾਲ AI ਸਮਰੱਥਾਵਾਂ ਵਧਾਓ
- **[→ ਅਧਿਆਇ 3 ਸਿੱਖਣਾ ਸ਼ੁਰੂ ਕਰੋ](./03-CoreGenerativeAITechniques/README.md)**

### **ਅਧਿਆਇ 4: ਵਾਹੀਵਾਹਿਕ ਅਰਜ਼ੀਆਂ ਅਤੇ ਪ੍ਰੋਜੈਕਟ**
- **ਪਿਆਰੇ ਕਹਾਣੀ ਜੈਨਰੇਟਰ** (`petstory/`): GitHub ਮਾਡਲ ਨਾਲ ਰਚਨਾਤਮਕ ਸਮੱਗਰੀ ਪੈਦਾ ਕਰਨਾ
- **Foundry ਲੋਕਲ ਡੈਮੋ** (`foundrylocal/`): ਓਪਨਏਆਈ ਜਾਵਾ SDK ਨਾਲ ਲੋਕਲ ਏਆਈ ਮਾਡਲ ਇੰਟੇਗਰੇਸ਼ਨ
- **MCP ਕੈਲਕੁਲੇਟਰ ਸੇਵਾ** (`calculator/`): ਸਪ੍ਰਿੰਗ ਏਆਈ ਨਾਲ ਬੁਨਿਆਦੀ ਮਾਡਲ ਕੁਨਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ ਲਾਗੂ ਕਰਨਾ
- **[→ ਅਧਿਆਇ 4 ਸਿੱਖਣਾ ਸ਼ੁਰੂ ਕਰੋ](./04-PracticalSamples/README.md)**

### **ਅਧਿਆਇ 5: ਜ਼ਿੰਮੇਵਾਰ ਏਆਈ ਵਿਕਾਸ**
- **GitHub ਮਾਡਲ ਸੁਰੱਖਿਆ**: ਇੰਬਿਲਟ ਸਮੱਗਰੀ ਫਿਲਟ੍ਰਿੰਗ ਅਤੇ ਸੁਰੱਖਿਆ ਮਕੈਨਿਜ਼ਮ ਟੈਸਟ ਕਰੋ (ਹਾਰਡ ਬਲਾਕ ਅਤੇ ਸੌਫਟ ਰਿਫਿਊਜ਼ਲ)
- **ਜ਼ਿੰਮੇਵਾਰ ਏਆਈ ਡੈਮੋ**: ਹੱਥ-ਨਾਲ ਉਦਾਹਰਨ ਦਿਖਾਉਂਦਾ ਹੈ ਕਿ ਆਧੁਨਿਕ ਏਆਈ ਸੁਰੱਖਿਆ ਪ੍ਰਣਾਲੀਆਂ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀਆਂ ਹਨ
- **ਸਰਵੋਤਮ ਪ੍ਰਥਾਵਾਂ**: ਨੈਤਿਕ ਏਆਈ ਦੀ ਵਿਕਾਸ ਅਤੇ ਤਾਇਨਾਤੀ ਲਈ ਲਾਜ਼ਮੀ ਦਿਸ਼ਾ-ਨਿਰਦੇਸ਼
- **[→ ਅਧਿਆਇ 5 ਸਿੱਖਣਾ ਸ਼ੁਰੂ ਕਰੋ](./05-ResponsibleGenAI/README.md)**

## ਵਾਧੂ ਸਾਧਨ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### ਲੈਂਗਚੇਨ
[![ਮੁੱਢਲੀਆਂ ਲਈ LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![ਮੁੱਢਲੀਆਂ ਲਈ LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![ਮੁੱਢਲੀਆਂ ਲਈ LangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### ਐਜ਼ਰ / ਐੱਜ / MCP / ਏਜੰਟ
[![ਮੁੱਢਲੀਆਂ ਲਈ AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ਮੁੱਢਲੀਆਂ ਲਈ Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ਮੁੱਢਲੀਆਂ ਲਈ MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ਮੁੱਢਲੀਆਂ ਲਈ AI ਏਜੰਟਸ](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ਜੈਨਰੇਟਿਵ ਏਆਈ ਸੀਰੀਜ਼
[![ਮੁੱਢਲੀਆਂ ਲਈ ਜੈਨਰੇਟਿਵ ਏਆਈ](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ਜੈਨਰੇਟਿਵ ਏਆਈ (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![ਜੈਨਰੇਟਿਵ ਏਆਈ (ਜਾਵਾ)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![ਜੈਨਰੇਟਿਵ ਏਆਈ (ਜਾਵਾਸਕ੍ਰਿਪਟ)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ਕੋਰ ਸਿੱਖਿਆ
[![ਮੁੱਢਲੀਆਂ ਲਈ ਐਮਐੱਲ](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![ਮੁੱਢਲੀਆਂ ਲਈ ਡਾਟਾ ਸਾਇੰਸ](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![ਮੁੱਢਲੀਆਂ ਲਈ ਏਆਈ](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![ਮੁੱਢਲੀਆਂ ਲਈ ਸਾਇਬਰਸੁਰੱਖਿਆ](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)

[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ਕੋਪਾਇਲਟ ਸੀਰੀਜ਼
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ਮਦਦ ਪ੍ਰਾਪਤ ਕਰਨਾ

ਜੇ ਤੁਸੀਂ ਫਸ ਜਾਂਦੇ ਹੋ ਜਾਂ AI ਐਪਸ ਬਣਾਉਣ ਬਾਰੇ ਤੁਹਾਡੇ ਕੋਲ ਕੋਈ ਸਵਾਲ ਹਨ। MCP ਬਾਰੇ ਵਿਚਾਰ-ਵਟਾਂਦਰੇ ਵਿੱਚ ਸਾਥੀ ਸਿੱਖਣ ਵਾਲਿਆਂ ਅਤੇ ਅਨੁਭਵੀ ਵਿਕਾਸਕਾਰਾਂ ਨਾਲ ਜੁੜੋ। ਇਹ ਇਕ ਸਹਿਯੋਗੀ ਕਮਿਊਨਿਟੀ ਹੈ ਜਿੱਥੇ ਸਵਾਲਾਂ ਦਾ ਸਵਾਗਤ ਹੈ ਅਤੇ ਗਿਆਨ ਖੁੱਲ੍ਹ ਕੇ ਸਾਂਝਾ ਕੀਤਾ ਜਾਂਦਾ ਹੈ। 

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਉਤਪਾਦ ਫੀਡਬੈਕ ਹੈ ਜਾਂ ਬਣਾਉਣ ਦੌਰਾਨ ਕੋਈ ਗਲਤੀ ਹੋਈ ਹੈ ਤਾਂ ਜਾਓ:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਤੀਅਫ਼ਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ ਏਆਈ ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਸਵੈਚਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸਥਿਰਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਕ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜ਼ਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਮੀਆਂ ਜਾਂ ਭ੍ਰਮਾਂ ਲਈ ਜਵਾਬਦੇਹ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->