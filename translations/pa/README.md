<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:10:08+00:00",
  "source_file": "README.md",
  "language_code": "pa"
}
-->
# ਸ਼ੁਰੂਆਤੀ ਲਈ ਜਨਰੇਟਿਵ ਏਆਈ - ਜਾਵਾ ਐਡੀਸ਼ਨ
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/pa/beg-genai-series.8b48be9951cc574c.webp)

**ਸਮਾਂ ਸਮਰਪਣ**: ਸਾਰਾ ਵਰਕਸ਼ਾਪ ਆਨਲਾਈਨ ਬਿਨਾਂ ਕਿਸੇ ਲੋਕਲ ਸੈਟਅਪ ਦੇ ਮੁਕੰਮਲ ਕੀਤਾ ਜਾ ਸਕਦਾ ਹੈ। ਵਾਤਾਵਰਨ ਸੈਟਅਪ ਵਿੱਚ 2 ਮਿੰਟ ਲੱਗਦੇ ਹਨ, ਅਤੇ ਨਮੂਨੇ ਖੰਗਾਲਣ ਵਿੱਚ ਵਿਅਕਤੀਗਤ ਗਹਿਰਾਈ ਮੁਤਾਬਕ 1-3 ਘੰਟੇ ਲੱਗ ਸਕਦੇ ਹਨ।

> **ਤੇਜ਼ ਸ਼ੁਰੂਆਤ**

1. ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ GitHub ਖਾਤੇ ਤੇ ਫੋਰਕ ਕਰੋ
2. **Code** → **Codespaces** ਟੈਬ → **...** → **New with options...** 'ਤੇ ਕਲਿਕ ਕਰੋ
3. ਡਿਫਾਲਟ ਦੀ ਵਰਤੋਂ ਕਰੋ – ਇਹ ਕੋਰਸ ਲਈ ਬਣਾਈ ਗਈ ਡਿਵੈਲਪਮੈਂਟ ਕੰਟੇਨਰ ਨੂੰ ਚੁਣੇਗਾ
4. **Create codespace** 'ਤੇ ਕਲਿਕ ਕਰੋ
5. ਵਾਤਾਵਰਨ ਦੀ ਤਿਆਰੀ ਲਈ ਲਗਭਗ 2 ਮਿੰਟ ਉਡੀਕ ਕਰੋ
6. ਸਿੱਧਾ [ਪਹਿਲਾ ਉਦਾਹਰਨ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) ਤੇ ਜਾਓ

> **ਸਥਾਨਕ ਕਲੋਨ ਕਰਨਾ ਪਸੰਦ ਕਰਦੇ ਹੋ?**
>
> ਇਹ ਰਿਪੋਜ਼ਟਰੀ 50+ ਭਾਸ਼ਾਈ ਅਨੁਵਾਦ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ ਜੋ ਡਾਉਨਲੋਡ ਸਾਈਜ਼ ਵਿੱਚ ਕਾਫੀ ਵਾਧਾ ਕਰਦਾ ਹੈ। ਬਿਨਾਂ ਅਨੁਵਾਦਾਂ ਦੇ ਕਲੋਨ ਕਰਨ ਲਈ, sparse checkout ਦੀ ਵਰਤੋਂ ਕਰੋ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ਇਸ ਨਾਲ ਤੁਹਾਨੂੰ ਕੋਰਸ ਪੂਰਾ ਕਰਨ ਲਈ ਸਾਰਾ ਕੁਝ ਮਿਲੇਗਾ ਅਤੇ ਡਾਉਨਲੋਡ ਬਹੁਤ ਤੇਜ਼ ਹੋਵੇਗਾ।

## ਬਹੁ-ਭਾਸ਼ਾ ਸਹਾਇਤਾ

### GitHub Action ਰਾਹੀਂ ਸਹਾਇਤਿਤ (ਆਟੋਮੇਟਿਕ ਅਤੇ ਹਮੇਸ਼ਾਂ ਅੱਪ-ਟੂ-ਡੇਟ)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[ਅਰਬੀ](../ar/README.md) | [ਬੰਗਾਲੀ](../bn/README.md) | [ਬਲਗੇਰੀਆਈ](../bg/README.md) | [ਬੁਰਮੀ (ਮਿਆਨਮਾਰ)](../my/README.md) | [ਚੀਨੀ (ਸਰਲ)](../zh/README.md) | [ਚੀਨੀ (ਰਵਾਇਤੀ, ਹਾਂਗ ਕਾਂਗ)](../hk/README.md) | [ਚੀਨੀ (ਰਵਾਇਤੀ, ਮਕਾਉ)](../mo/README.md) | [ਚੀਨੀ (ਰਵਾਇਤੀ, ਤਾਈਵਾਨ)](../tw/README.md) | [ਕਰੋਏਸ਼ੀਆਈ](../hr/README.md) | [ਚੈਕ](../cs/README.md) | [ਡੈਨਿਸ਼](../da/README.md) | [ਡੱਚ](../nl/README.md) | [ਐਸਟੋਨੀਆਈ](../et/README.md) | [ਫਿਨਿਸ਼](../fi/README.md) | [ਫ੍ਰੈਂਚ](../fr/README.md) | [ਜਰਮਨ](../de/README.md) | [ਗ੍ਰੀਕ](../el/README.md) | [ਹਿਬ੍ਰੂ](../he/README.md) | [ਹਿੰਦੀ](../hi/README.md) | [ਹੰਗੇਰੀਆਈ](../hu/README.md) | [ਇੰਡੋਨੇਸ਼ੀਆਈ](../id/README.md) | [ਇਟਾਲੀਅਨ](../it/README.md) | [ਜਾਪਾਨੀ](../ja/README.md) | [ਕੰਨੜ](../kn/README.md) | [ਕੋਰੀਅਨ](../ko/README.md) | [ਲਿੱਥੂਆਨੀਆਈ](../lt/README.md) | [ਮਲਈ](../ms/README.md) | [ਮਲਯਾਲਮ](../ml/README.md) | [ਮਰਾਠੀ](../mr/README.md) | [ਨੇਪਾਲੀ](../ne/README.md) | [ਨਾਈਜੀਰੀਆਈ ਪਿੱਡਜਿਨ](../pcm/README.md) | [ਨਾਰਵੇਜੀਆਈ](../no/README.md) | [ਫ਼ਾਰਸੀ (ਪਰਸੀਆਈ)](../fa/README.md) | [ਪੋਲਿਸ਼](../pl/README.md) | [ਪੋਰਤੁਗਾਲੀ (ਬ੍ਰਾਜ਼ੀਲ)](../br/README.md) | [ਪੋਰਤੁਗਾਲੀ (ਪੋਰਚਗਾਲ)](../pt/README.md) | [ਪੰਜਾਬੀ (ਗੁਰਮੁਖੀ)](./README.md) | [ਰੋਮਾਨੀਆਈ](../ro/README.md) | [ਰੂਸੀ](../ru/README.md) | [ਸੇਰਬੀਆਈ (ਸਿਰਿਲਿਕ)](../sr/README.md) | [ਸਲੋਵਾਕ](../sk/README.md) | [ਸਲੋਵੇਨੀਆਈ](../sl/README.md) | [ਸਪੈਨਿਸ਼](../es/README.md) | [ਸਵਾਹਿਲੀ](../sw/README.md) | [ਸਵੀਡੀਸ਼](../sv/README.md) | [ਟਾਗਾਲੋਗ (ਫਿਲਿਪੀਨੀ)](../tl/README.md) | [ਤਮਿਲ](../ta/README.md) | [ਤੇਲਗੂ](../te/README.md) | [ਥਾਈ](../th/README.md) | [ਤੁਰਕੀ](../tr/README.md) | [ਯੂਕਰੇਨੀਅਨ](../uk/README.md) | [ਉਰਦੂ](../ur/README.md) | [ਵਿਯਤਨਾਮੀ](../vi/README.md)

> **ਸਥਾਨਕ ਕਲੋਨ ਕਰਨਾ ਪਸੰਦ ਕਰਦੇ ਹੋ?**

> ਇਹ ਰਿਪੋਜ਼ਟਰੀ 50+ ਭਾਸ਼ਾਈ ਅਨੁਵਾਦ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ ਜੋ ਡਾਉਨਲੋਡ ਸਾਈਜ਼ ਵਿੱਚ ਕਾਫੀ ਵਾਧਾ ਕਰਦਾ ਹੈ। ਬਿਨਾਂ ਅਨੁਵਾਦਾਂ ਦੇ ਕਲੋਨ ਕਰਨ ਲਈ, sparse checkout ਦੀ ਵਰਤੋਂ ਕਰੋ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> ਇਸ ਨਾਲ ਤੁਹਾਨੂੰ ਕੋਰਸ ਪੂਰਾ ਕਰਨ ਲਈ ਸਾਰਾ ਕੁਝ ਮਿਲੇਗਾ ਅਤੇ ਡਾਉਨਲੋਡ ਬਹੁਤ ਤੇਜ਼ ਹੋਵੇਗਾ।
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## ਕੋਰਸ ਸਤਰਾਂਚਨਾ ਅਤੇ ਸਿੱਖਣ ਦਾ ਰਾਸ਼ਤਾ

### **ਅਧਿਆਇ 1: ਜਨਰੇਟਿਵ ਏਆਈ ਦਾ ਪਰਿਚਯ**
- **ਮੁੱਖ ਧਾਰਣਾਵਾਂ**: ਵੱਡੇ ਭਾਸ਼ਾ ਮਾਡਲਾਂ, ਟੋਕਨ, ਐਮਬੈੱਡੀੰਗ, ਅਤੇ ਏਆਈ ਸਮਰੱਥਾਵਾਂ ਦੀ ਸਮਝ
- **ਜਾਵਾ ਏਆਈ ਪਰਿਸਰ**: Spring AI ਅਤੇ OpenAI SDKs ਦੀ ਸਮੀਖਿਆ
- **ਮਾਡਲ ਕਾਂਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ**: MCP ਦਾ ਪਰਿਚਯ ਅਤੇ ਏਆਈ ਏਜੰਟ ਸੰਚਾਰ ਵਿੱਚ ਇਸ ਦੀ ਭੂਮਿਕਾ
- **ਅਮਲਾਤਮਕ ਅਰਜ਼ੀ**: ਚੈਟਬੋਟ ਅਤੇ ਸਮੱਗਰੀ ਸਿਰਜਣਾ ਸਮੇਤ ਅਸਲੀ ਦੁਨੀਆ ਦੇ ਸਿਨਾਰਿਓ
- **[→ ਅਧਿਆਇ 1 ਸ਼ੁਰੂ ਕਰੋ](./01-IntroToGenAI/README.md)**

### **ਅਧਿਆਇ 2: ਵਿਕਾਸ ਵਾਤਾਵਰਨ ਸੈਟਅਪ**
- **ਮੁਲਟੀ ਪ੍ਰੋਵਾਇਡਰ ਕਨਫ਼ਿਗਰੇਸ਼ਨ**: GitHub Models, Azure OpenAI, ਅਤੇ OpenAI ਜਾਵਾ SDK ਇੰਟੀਗ੍ਰੇਸ਼ਨ ਸੈਟਅਪ ਕਰੋ
- **Spring Boot + Spring AI**: ਐਨਟਰਪ੍ਰਾਈਜ਼ ਏਆਈ ਐਪਲੀਕੇਸ਼ਨ ਵਿਕਾਸ ਲਈ ਵਧੀਆ ਤਰੀਕੇ
- **GitHub Models**: ਪ੍ਰੋਟੋਟਾਈਪਿੰਗ ਅਤੇ ਸਿੱਖਣ ਲਈ ਮੁਫ਼ਤ ਏਆਈ ਮਾਡਲ ਐਕਸੈਸ (ਕੋਈ ਕਰੈਡਿਟ ਕਾਰਡ ਲੋੜ ਨਹੀਂ)
- **ਵਿਕਾਸ ਸੰਦ**: Docker ਕੰਟੇਨਰ, VS Code, ਅਤੇ GitHub Codespaces ਕਨਫ਼ਿਗਰੇਸ਼ਨ
- **[→ ਅਧਿਆਇ 2 ਸ਼ੁਰੂ ਕਰੋ](./02-SetupDevEnvironment/README.md)**

### **ਅਧਿਆਇ 3: ਮੁੱਖ ਜਨਰੇਟਿਵ ਏਆਈ ਤਕਨੀਕਾਂ**
- **ਪ੍ਰੋਮਪਟ ਇੰਜੀਨੀਅਰਿੰਗ**: ਉੱਤਮ ਏਆਈ ਮਾਡਲ ਜਵਾਬ ਲਈ ਤਕਨੀਕਾਂ
- **ਐਮਬੈੱਡੀੰਗ ਅਤੇ ਵੇਕਟਰ ਓਪਰੇਸ਼ਨ**: ਸੈਮੈਂਟਿਕ ਖੋਜ ਅਤੇ ਸਮਾਨਤਾ ਮੇਲ ਦੀ ਨਿਰਮਾਣ
- **ਰੀਟਰੀਵੱਲ-ਆਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ (RAG)**: ਆਪਣੀ ਡਾਟਾ ਸਰੋਤਾਂ ਨਾਲ ਏਆਈ ਨੂੰ ਜੋੜੋ
- **ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ**: ਕਸਟਮ ਔਜ਼ਾਰ ਅਤੇ ਪਲੱਗਇਨਾਂ ਨਾਲ ਏਆਈ ਸਮਰੱਥਾਵਾਂ ਦਾ ਵਿਸ਼ਤਾਰ
- **[→ ਅਧਿਆਇ 3 ਸ਼ੁਰੂ ਕਰੋ](./03-CoreGenerativeAITechniques/README.md)**

### **ਅਧਿਆਇ 4: ਅਮਲਾਤਮਕ ਅਰਜ਼ੀਆਂ ਅਤੇ ਪ੍ਰੋਜੈਕਟ**
- **ਪਾਲਤੂ ਕਹਾਣੀ ਸਿਰਜਣਹਾਰ** (`petstory/`): GitHub Models ਨਾਲ ਰਚਨਾਤਮਕ ਸਮੱਗਰੀ ਸਿਰਜਣਾ
- **Foundry Local ਡੈਮੋ** (`foundrylocal/`): OpenAI ਜਾਵਾ SDK ਨਾਲ ਸਥਾਨਕ ਏਆਈ ਮਾਡਲ ਇੰਟੀਗ੍ਰੇਸ਼ਨ
- **MCP ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ** (`calculator/`): Spring AI ਨਾਲ ਬੁਨਿਆਦੀ ਮਾਡਲ ਕਾਂਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ ਲਾਗੂ
- **[→ ਅਧਿਆਇ 4 ਸ਼ੁਰੂ ਕਰੋ](./04-PracticalSamples/README.md)**

### **ਅਧਿਆਇ 5: ਜ਼ਿੰਮੇਵਾਰ ਏਆਈ ਵਿਕਾਸ**
- **GitHub Models ਸੁਰੱਖਿਆ**: ਬਣੇ-ਬਣਾਏ ਸਮੱਗਰੀ ਛਾਂਟਣ ਅਤੇ ਸੁਰੱਖਿਆ ਤੰਤਰਾਂ ਦਾ ਟੈਸਟ (ਕਠੋਰ ਰੋਕ ਅਤੇ ਨਰਮ ਇਨਕਾਰ)
- **ਜ਼ਿੰਮੇਵਾਰ ਏਆਈ ਡੈਮੋ**: ਤਤਕਾਲ ਉਦਾਹਰਨ ਜੋ ਦਿਖਾਉਂਦੀ ਹੈ ਕਿ ਆਧੁਨਿਕ ਏਆਈ ਸੁਰੱਖਿਆ ਪ੍ਰਣਾਲੀਆਂ ਕਿਵੇਂ ਕੰਮ ਕਰਦੀਆਂ ਹਨ
- **ਸਰੋਤ ਸੰਭਾਲ**: ਨੈਤਿਕ ਏਆਈ ਵਿਕਾਸ ਅਤੇ ਤਾਇਨਾਤੀ ਲਈ ਜ਼ਰੂਰੀ ਗਾਈਡਲਾਈਨਜ਼
- **[→ ਅਧਿਆਇ 5 ਸ਼ੁਰੂ ਕਰੋ](./05-ResponsibleGenAI/README.md)**

## ਵਾਧੂ ਸਰੋਤ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### ਲੈੰਗਚੇਨ
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### ਐਜ਼ੂਰ / ਐਜ / MCP / ਏਜੰਟ
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ਜਨਰੇਟਿਵ ਏਆਈ ਸੀਰੀਜ਼
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### ਮੁੱਖ ਸਿੱਖਿਆ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### ਕੌਪਾਇਲਟ ਸੀਰੀਜ਼
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ਸਹਾਇਤਾ ਪ੍ਰਾਪਤ ਕਰਨਾ

ਜੇ ਤੁਸੀਂ ਫਸ ਜਾਂਦੇ ਹੋ ਜਾਂ AI ਐਪ ਬਣਾਉਣ ਬਾਰੇ ਕੋਈ ਵੀ ਸਵਾਲ ਹੋਵੇ। MCP ਬਾਰੇ ਚਰਚਾ ਵਿੱਚ ਸਹਿਆੋਗੀ ਸਿੱਖਣ ਵਾਲਿਆਂ ਅਤੇ ਅਨੁਭਵੀ ਵਿਕਾਸਕਾਰਾਂ ਦੇ ਨਾਲ ਜੁੜੋ। ਇਹ ਇੱਕ ਸਹਾਇਕ ਕਮਿਊਨਿਟੀ ਹੈ ਜਿਥੇ ਸਵਾਲ ਸੁਆਗਤਯੋਗ ਹਨ ਅਤੇ ਗਿਆਨ ਖੁੱਲ੍ਹਾ ਸਾਂਝਾ ਕੀਤਾ ਜਾਂਦਾ ਹੈ।

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ਜੇ ਤੁਹਾਡੇ ਕੋਲ ਉਤਪਾਦ ਫੀਡਬੈਕ ਜਾਂ ਗਲਤੀਆਂ ਹਨ ਜੋ ਬਣਾਉਣ ਸਮੇਂ ਆਈਆਂ ਹਨ ਤਾਂ ਦੌਰਾ ਕਰੋ:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ਅਸਪੱਸ਼ਟੀਕਰਨ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਅਤ ਲਈ ਕੋਸ਼ਿਸ਼ ਕਰਦੇ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਜਾਣੋ ਕਿ ਸਵੈਚਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਣਸਹੀਤੀਆਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਆਪਣੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਪ੍ਰਮਾਣਿਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਜਰੂਰੀ ਜਾਣਕਾਰੀ ਲਈ, ਵਿਸ਼ੇਸ਼ਗਿਆਨ ਵਾਲੇ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫ਼ਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੇ ਉਪਯੋਗ ਨਾਲ ਉੱਪਜਣ ਵਾਲੀਆਂ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀਆਂ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆਵਾਂ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->