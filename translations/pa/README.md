<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:34:35+00:00",
  "source_file": "README.md",
  "language_code": "pa"
}
-->
# ਜੈਨੇਰੇਟਿਵ AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ - Java ਐਡੀਸ਼ਨ
[![ਮਾਈਕ੍ਰੋਸੌਫਟ ਫਾਊਂਡਰੀ ਡਿਸਕੋਰਡ](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![ਜੈਨੇਰੇਟਿਵ AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ - Java ਐਡੀਸ਼ਨ](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.pa.png)

**ਸਮਾਂ ਦੀ ਲੋੜ**: ਸਾਰੇ ਵਰਕਸ਼ਾਪ ਨੂੰ ਬਿਨਾਂ ਕਿਸੇ ਲੋਕਲ ਸੈਟਅੱਪ ਦੇ ਔਨਲਾਈਨ ਪੂਰਾ ਕੀਤਾ ਜਾ ਸਕਦਾ ਹੈ। ਮਾਹੌਲ ਸੈਟਅੱਪ ਨੂੰ 2 ਮਿੰਟ ਲੱਗਦੇ ਹਨ, ਅਤੇ ਨਮੂਨੇ ਦੀ ਜਾਂਚ ਕਰਨ ਲਈ ਖੋਜ ਦੀ ਗਹਿਰਾਈ ਦੇ ਅਨੁਸਾਰ 1-3 ਘੰਟੇ ਲੱਗ ਸਕਦੇ ਹਨ।

> **ਤੁਰੰਤ ਸ਼ੁਰੂ ਕਰੋ** 

1. ਇਸ ਰੀਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ GitHub ਖਾਤੇ 'ਤੇ Fork ਕਰੋ
2. **Code** → **Codespaces** ਟੈਬ → **...** → **New with options...** 'ਤੇ ਕਲਿੱਕ ਕਰੋ
3. ਡਿਫਾਲਟ ਵਰਤੋ – ਇਹ ਇਸ ਕੋਰਸ ਲਈ ਬਣਾਏ Development container ਨੂੰ ਚੁਣੇਗਾ
4. **Create codespace** 'ਤੇ ਕਲਿੱਕ ਕਰੋ
5. ਮਾਹੌਲ ਦੇ ਤਿਆਰ ਹੋਣ ਲਈ ਲਗਭਗ 2 ਮਿੰਟ ਇੰਤਜ਼ਾਰ ਕਰੋ
6. ਸਿੱਧਾ ਜਾਓ [ਪਹਿਲਾ ਉਦਾਹਰਨ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **ਕੀ ਤੁਸੀਂ ਲੋਕਲ ਤੌਰ 'ਤੇ ਕਲੋਨ ਕਰਨਾ ਪਸੰਦ ਕਰਦੇ ਹੋ?**
>
> ਇਸ ਰੀਪੋਜ਼ਟਰੀ ਵਿੱਚ 50+ ਭਾਸ਼ਾਈ ਅਨੁਵਾਦ ਸ਼ਾਮਿਲ ਹਨ ਜੋ ਡਾਊਨਲੋਡ ਆਕਾਰ ਨੂੰ ਕਾਫੀ ਵਧਾ ਦਿੰਦੇ ਹਨ। ਅਨੁਵਾਦਾਂ ਦੇ ਬਿਨਾਂ ਕਲੋਨ ਕਰਨ ਲਈ, sparse checkout ਵਰਤੋਂ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ਇਹ ਤੁਹਾਨੂੰ ਕੋਰਸ ਪੂਰਾ ਕਰਨ ਲਈ ਸਭ ਕੁਝ ਦਿੰਦਾ ਹੈ, ਬਹੁਤ ਤੇਜ਼ ਡਾਊਨਲੋਡ ਦੇ ਨਾਲ।


## ਬਹੁ-ਭਾਸ਼ਾਈ ਸਮਰਥਨ

### GitHub Action ਰਾਹੀਂ ਸਮਰਥਿਤ (ਆਟੋਮੇਟਿਕ & ਹਮੇਸ਼ਾਂ ਅੱਪ-ਟੂ-ਡੇਟ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ਅਰਬੀ](../ar/README.md) | [ਬੰਗਾਲੀ](../bn/README.md) | [ਬੁਲਗਾਰੀਆਈ](../bg/README.md) | [ਬਰਮੀ (ਮਿਆਂਮਾਰ)](../my/README.md) | [ਚੀਨੀ (ਸਰਲ)](../zh/README.md) | [ਚੀਨੀ (ਰਿਵਾਇਤੀ, ਹਾਂਗ ਕਾਂਗ)](../hk/README.md) | [ਚੀਨੀ (ਰਿਵਾਇਤੀ, ਮਕਾਉ)](../mo/README.md) | [ਚੀਨੀ (ਰਿਵਾਇਤੀ, ਤਾਈਵਾਨ)](../tw/README.md) | [ਕ੍ਰੋਏਸ਼ੀਆਈ](../hr/README.md) | [ਚੈੱਕ](../cs/README.md) | [ਡੈਨਿਸ਼](../da/README.md) | [ਡੱਚ](../nl/README.md) | [ਇਸਤੋਨੀਅਨ](../et/README.md) | [ਫਿਨਿਸ਼](../fi/README.md) | [ਫ਼ਰਾਂਸੀਸੀ](../fr/README.md) | [ਜਰਮਨ](../de/README.md) | [ਗ੍ਰੀਕ](../el/README.md) | [ਹੀਬਰੂ](../he/README.md) | [ਹਿੰਦੀ](../hi/README.md) | [ਹੁੰਗੇਰੀ](../hu/README.md) | [ਇੰਡੋਨੇਸ਼ੀਆਈ](../id/README.md) | [ਇਟਾਲੀਅਨ](../it/README.md) | [ਜਪਾਨੀ](../ja/README.md) | [ਕੰਨੜ](../kn/README.md) | [ਕੋਰੀਅਨ](../ko/README.md) | [ਲਿਥੂਆਨੀਆਈ](../lt/README.md) | [ਮਲਏ (Malay)](../ms/README.md) | [ਮਲਯਾਲਮ](../ml/README.md) | [ਮਰਾਠੀ](../mr/README.md) | [ਨੇਪਾਲੀ](../ne/README.md) | [ਨਾਈਜੀਰੀਆਈ ਪਿਜ਼ਿਨ](../pcm/README.md) | [ਨਾਰਵੇਜੀਅਨ](../no/README.md) | [ਫਾਰਸੀ (Farsi)](../fa/README.md) | [ਪੋਲਿਸ਼](../pl/README.md) | [ਪੁਰਤਗਾਲੀ (Brazil)](../br/README.md) | [ਪੁਰਤਗਾਲੀ (Portugal)](../pt/README.md) | [ਪੰਜਾਬੀ (ਗੁਰਮੁਖੀ)](./README.md) | [ਰੋਮਾਨੀਆਈ](../ro/README.md) | [ਰੂਸੀ](../ru/README.md) | [ਸਰਬੀਆਈ (ਸਿਰਿਲਿਕ)](../sr/README.md) | [ਸਲੋਵਾਕ](../sk/README.md) | [ਸਲੋਵੇਨੀਆਈ](../sl/README.md) | [ਸਪੇਨੀ](../es/README.md) | [ਸਵਾਹਿਲੀ](../sw/README.md) | [ਸਵੀਡਿਸ਼](../sv/README.md) | [ਟੈਗਾਲੋਗ (ਫਿਲੀਪੀਨੀ)](../tl/README.md) | [ਤਮਿਲ](../ta/README.md) | [ਤੇਲੁਗੂ](../te/README.md) | [ਥਾਈ](../th/README.md) | [ਤੁਰਕੀ](../tr/README.md) | [ਯੂਕਰੇਨੀ](../uk/README.md) | [ਉਰਦੂ](../ur/README.md) | [ਵਿਯਤਨਾਮੀ](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ਕੋਰਸ ਦੀ ਢਾਂਚਾ ਅਤੇ ਸیکھਣ ਦਾ ਰਸਤਾ

### **ਅਧਿਆਇ 1: ਜੈਨੇਰੇਟਿਵ AI ਦਾ ਪਰਿਚਯ**
- **ਮੁੱਖ ਧਾਰਣਾਵਾਂ**: ਵੱਡੇ ਭਾਸ਼ਾ ਮਾਡਲ, ਟੋਕਨ, embeddings, ਅਤੇ AI ਸਮਰੱਥਾਵਾਂ ਨੂੰ ਸਮਝਣਾ
- **Java AI ਇਕੋਸਿਸਟਮ**: Spring AI ਅਤੇ OpenAI SDKs ਦਾ ਝਲਕ
- **Model Context Protocol**: MCP ਦਾ ਪਰਿਚਯ ਅਤੇ AI ਏਜੰਟ ਸੰਚਾਰ ਵਿੱਚ ਇਸ ਦੀ ਭੂਮਿਕਾ
- **ਅਮਲੀ ਲਾਗੂਤਾਂ**: ਚੈਟਬੌਟਸ ਅਤੇ ਸਮੱਗਰੀ ਬਣਾਉਣ ਸਮੇਤ ਹਕੀਕਤੀ ਸਥਿਤੀਆਂ
- **[→ ਅਧਿਆਇ 1 ਸ਼ੁਰੂ ਕਰੋ](./01-IntroToGenAI/README.md)**

### **ਅਧਿਆਇ 2: ਵਿਕਾਸ ਮਾਹੌਲ ਸੈਟਅੱਪ**
- **ਬਹੁ-ਪ੍ਰਦਾਤਾ ਸੰਰਚਨਾ**: GitHub Models, Azure OpenAI, ਅਤੇ OpenAI Java SDK ਇੰਟਿਗ੍ਰੇਸ਼ਨਾਂ ਨੂੰ ਸੈਟਅੱਪ ਕਰੋ
- **Spring Boot + Spring AI**: ਐਂਟਰਪਰਾਈਜ਼ AI ਐਪਲੀਕੇਸ਼ਨ ਵਿਕਾਸ ਲਈ ਸਰਵੋਤਮ ਅਭਿਆਸ
- **GitHub Models**: ਪ੍ਰੋਟੋਟਾਈਪਿੰਗ ਅਤੇ ਸਿੱਖਣ ਲਈ ਮੁਫ਼ਤ AI ਮਾਡਲ ਪੁਹੰਚ (ਕੋਈ ਕਰੈਡਿਟ ਕਾਰਡ ਲੋੜ ਨਹੀਂ)
- **ਡਿਵੈਲਪਮੈਂਟ ਟੂਲਸ**: Docker containers, VS Code, ਅਤੇ GitHub Codespaces ਸੰਰਚਨਾ
- **[→ ਅਧਿਆਇ 2 ਸ਼ੁਰੂ ਕਰੋ](./02-SetupDevEnvironment/README.md)**

### **ਅਧਿਆਇ 3: ਮੁੱਖ ਜੈਨੇਰੇਟਿਵ AI ਤਕਨੀਕਾਂ**
- **Prompt Engineering**: ਸਰਵੋਤਮ AI ਮਾਡਲ ਜਵਾਬਾਂ ਲਈ ਤਕਨੀਕਾਂ
- **Embeddings & Vector Operations**: ਸੈਮੈਂਟਿਕ ਖੋਜ ਅਤੇ ਸਮਾਨਤਾ ਮਿਲਾਉਣ ਨੂੰ ਲਾਗੂ ਕਰੋ
- **Retrieval-Augmented Generation (RAG)**: AI ਨੂੰ ਆਪਣੇ ਡਾਟਾ ਸ੍ਰੋਤਾਂ ਨਾਲ ਜੋੜਨਾ
- **Function Calling**: ਕਸਟਮ ਟੂਲਾਂ ਅਤੇ ਪਲੱਗਇਨਾਂ ਨਾਲ AI ਸਮਰੱਥਾਵਾਂ ਵਧਾਉਣਾ
- **[→ ਅਧਿਆਇ 3 ਸ਼ੁਰੂ ਕਰੋ](./03-CoreGenerativeAITechniques/README.md)**

### **ਅਧਿਆਇ 4: ਅਮਲੀ ਐਪਲੀਕੇਸ਼ਨਜ਼ ਅਤੇ ਪ੍ਰੋਜੈਕਟ**
- **ਪੈਟ ਸਟੋਰੀ ਜਨਰੇਟਰ** (`petstory/`): GitHub Models ਨਾਲ ਰਚਨਾਤਮਕ ਸਮੱਗਰੀ ਬਣਾਉਣਾ
- **Foundry ਲੋਕਲ ਡੈਮੋ** (`foundrylocal/`): OpenAI Java SDK ਨਾਲ ਲੋਕਲ AI ਮਾਡਲ ਇੰਟਿਗ੍ਰੇਸ਼ਨ
- **MCP ਕੈਲਕੁਲੇਟਰ ਸਰਵਿਸ** (`calculator/`): Spring AI ਨਾਲ ਬੁਨਿਆਦੀ Model Context Protocol ਲਾਗੂਕਰਨ
- **[→ ਅਧਿਆਇ 4 ਸ਼ੁਰੂ ਕਰੋ](./04-PracticalSamples/README.md)**

### **ਅਧਿਆਇ 5: ਜਵਾਬਦੇਹ AI ਵਿਕਾਸ**
- **GitHub Models ਸੁਰੱਖਿਆ**: ਬਿਲਟ-ਇਨ ਸਮੱਗਰੀ ਫਿਲਟਰਿੰਗ ਅਤੇ ਸੁਰੱਖਿਆ ਮਕੈਨਿਜ਼ਮਾਂ (ਕਠੋਰ ਰੋਕ ਅਤੇ ਨਰਮ ਇਨਕਾਰ) ਦੀ ਜਾਂਚ
- **ਜਵਾਬਦੇਹ AI ਡੈਮੋ**: ਹੱਥ-ਅਨੁਭਵ ਉਦਾਹਰਨ ਜੋ ਦਿਖਾਉਂਦੀ ਹੈ ਕਿ ਆਧੁਨਿਕ AI ਸੁਰੱਖਿਆ ਪ੍ਰਣਾਲੀਆਂ ਪ੍ਰਯੋਗ ਵਿੱਚ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀਆਂ ਹਨ
- **ਸਰਵੋਤਮ ਅਭਿਆਸ**: ਨੈਤਿਕ AI ਵਿਕਾਸ ਅਤੇ ਡਿਪਲੋਇਮੈਂਟ ਲਈ ਜਰੂਰੀ ਨਿਰਦੇਸ਼
- **[→ ਅਧਿਆਇ 5 ਸ਼ੁਰੂ ਕਰੋ](./05-ResponsibleGenAI/README.md)**

## ਵਾਧੂ ਸਰੋਤ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ਜੈਨੇਰੇਟਿਵ AI ਸੀਰੀਜ਼
[![ਜੈਨੇਰੇਟਿਵ AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![ਜੈਨੇਰੇਟਿਵ AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![ਜੈਨੇਰੇਟਿਵ AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![ਜੈਨੇਰੇਟਿਵ AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ਮੁੱਖ ਸਿਖਿਆ
[![ML ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![ਡੇਟਾ ਸਾਇੰਸ ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![ਸਾਇਬਰਸੁਰੱਖਿਆ ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![ਵੇੱਬ ਡਿਵੈਲਪਮੈਂਟ ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ਆਰੰਭਿਕਾਂ ਲਈ IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ਆਰੰਭਿਕਾਂ ਲਈ XR ਵਿਕਾਸ](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot ਸੀਰੀਜ਼
[![AI ਜੋੜੀ ਪ੍ਰੋਗ੍ਰਾਮਿੰਗ ਲਈ Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET ਲਈ Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot ਐਡਵੈਂਚਰ](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ਸਹਾਇਤਾ ਪ੍ਰਾਪਤ ਕਰੋ

ਜੇ ਤੁਸੀਂ ਫਸ ਜਾਂਦੇ ਹੋ ਜਾਂ AI ਐਪਸ ਬਣਾਉਣ ਬਾਰੇ ਕੋਈ ਸਵਾਲ ਹੈ, ਤਾਂ MCP ਬਾਰੇ ਚਰਚਾਵਾਂ ਵਿੱਚ ਹੋਰ ਸਿੱਖਣ ਵਾਲਿਆਂ ਅਤੇ ਅਨੁਭਵੀ ਡਿਵੈਲਪਰਾਂ ਨਾਲ ਸ਼ਾਮਲ ਹੋਵੋ। ਇਹ ਇੱਕ ਸਹਾਇਕ ਕਮਿਊਨਿਟੀ ਹੈ ਜਿੱਥੇ ਸਵਾਲਾਂ ਦਾ ਸਵਾਗਤ ਹੈ ਅਤੇ ਗਿਆਨ ਖੁੱਲ੍ਹੇ ਤੌਰ 'ਤੇ ਸਾਂਝਾ ਕੀਤਾ ਜਾਂਦਾ ਹੈ।

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਉਤਪਾਦ ਬਾਰੇ ਫੀਡਬੈਕ ਜਾਂ ਬਣਾਉਣ ਦੌਰਾਨ ਕੋਈ ਤਰੁੱਟੀਆਂ ਹਨ, ਤਾਂ ਜਾਓ:

[![Microsoft Foundry ਡਿਵੈਲਪਰ ਫੋਰਮ](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
ਅਸਵੀਕਾਰ:
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਪਰ ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਰੱਖੋ ਕਿ ਆਟੋਮੇਟਿਕ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਣਸਹੀਤਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਿਕ ਸਰੋਤ ਵਜੋਂ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਗੰਭੀਰ ਜਾਣਕਾਰੀ ਲਈ ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਅਸੀਂ ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਉਤਪੰਨ ਹੋਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->