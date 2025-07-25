<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:15:46+00:00",
  "source_file": "README.md",
  "language_code": "pa"
}
-->
# ਜਨਰੇਟਿਵ AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ - ਜਾਵਾ ਐਡੀਸ਼ਨ  
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)  

![ਜਨਰੇਟਿਵ AI ਸ਼ੁਰੂਆਤੀਆਂ ਲਈ - ਜਾਵਾ ਐਡੀਸ਼ਨ](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.pa.png)  

**ਸਮਾਂ ਦੀ ਲੋੜ**: ਪੂਰਾ ਵਰਕਸ਼ਾਪ ਆਨਲਾਈਨ ਬਿਨਾਂ ਕਿਸੇ ਲੋਕਲ ਸੈਟਅੱਪ ਦੇ ਪੂਰਾ ਕੀਤਾ ਜਾ ਸਕਦਾ ਹੈ। ਜੇ ਤੁਸੀਂ ਨਮੂਨਿਆਂ ਨੂੰ ਚਲਾਉਣਾ ਚਾਹੁੰਦੇ ਹੋ, ਤਾਂ ਵਾਤਾਵਰਣ ਸੈਟਅੱਪ ਵਿੱਚ 2 ਮਿੰਟ ਲੱਗਦੇ ਹਨ, ਅਤੇ ਨਮੂਨਿਆਂ ਦੀ ਖੋਜ ਕਰਨ ਵਿੱਚ 1-3 ਘੰਟੇ ਲੱਗ ਸਕਦੇ ਹਨ, ਖੋਜ ਦੀ ਗਹਿਰਾਈ 'ਤੇ ਨਿਰਭਰ ਕਰਦਾ ਹੈ।  

> **ਤੁਰੰਤ ਸ਼ੁਰੂਆਤ**  

1. ਇਸ ਰਿਪੋਜ਼ਟਰੀ ਨੂੰ ਆਪਣੇ GitHub ਅਕਾਊਂਟ ਵਿੱਚ ਫੋਰਕ ਕਰੋ  
2. **Code** → **Codespaces** ਟੈਬ → **...** → **New with options...** 'ਤੇ ਕਲਿੱਕ ਕਰੋ  
3. ਡਿਫਾਲਟ ਸੈਟਿੰਗਾਂ ਵਰਤੋ – ਇਹ ਇਸ ਕੋਰਸ ਲਈ ਬਣਾਏ ਗਏ ਡਿਵੈਲਪਮੈਂਟ ਕੰਟੇਨਰ ਨੂੰ ਚੁਣੇਗਾ  
4. **Create codespace** 'ਤੇ ਕਲਿੱਕ ਕਰੋ  
5. ਵਾਤਾਵਰਣ ਤਿਆਰ ਹੋਣ ਲਈ ~2 ਮਿੰਟ ਰੁਕੋ  
6. ਸਿੱਧੇ [Creating your GitHub Models Token](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) 'ਤੇ ਜਾਓ  

## ਬਹੁ-ਭਾਸ਼ਾਈ ਸਹਾਇਤਾ  

### GitHub Action ਰਾਹੀਂ ਸਹਾਇਕ (ਆਟੋਮੈਟਿਕ ਅਤੇ ਹਮੇਸ਼ਾ ਅਪ-ਟੂ-ਡੇਟ)  

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](./README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)  

## ਕੋਰਸ ਦੀ ਬਣਤਰ ਅਤੇ ਸਿੱਖਣ ਦਾ ਰਸਤਾ  

### **ਅਧਿਆਇ 1: ਜਨਰੇਟਿਵ AI ਦਾ ਪਰਚੇਅ**  
- **ਮੁੱਖ ਧਾਰਨਾਵਾਂ**: ਵੱਡੇ ਭਾਸ਼ਾ ਮਾਡਲਾਂ, ਟੋਕਨ, ਐਮਬੈਡਿੰਗਜ਼, ਅਤੇ AI ਸਮਰੱਥਾਵਾਂ ਨੂੰ ਸਮਝਣਾ  
- **ਜਾਵਾ AI ਪਰਿਸਰ**: Spring AI ਅਤੇ OpenAI SDKs ਦਾ ਜਾਇਜ਼ਾ  
- **ਮਾਡਲ ਕਾਂਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ**: MCP ਅਤੇ AI ਏਜੰਟ ਸੰਚਾਰ ਵਿੱਚ ਇਸ ਦੀ ਭੂਮਿਕਾ ਦਾ ਪਰਚੇਅ  
- **ਵਿਹਾਰਕ ਐਪਲੀਕੇਸ਼ਨ**: ਚੈਟਬੋਟ ਅਤੇ ਸਮੱਗਰੀ ਸਿਰਜਣ ਸਮੇਤ ਅਸਲ ਦੁਨੀਆ ਦੇ ਉਦਾਹਰਨ  
- **[→ ਅਧਿਆਇ 1 ਸ਼ੁਰੂ ਕਰੋ](./01-IntroToGenAI/README.md)**  

### **ਅਧਿਆਇ 2: ਵਿਕਾਸ ਵਾਤਾਵਰਣ ਸੈਟਅੱਪ**  
- **ਮਲਟੀ-ਪ੍ਰੋਵਾਈਡਰ ਕਨਫਿਗਰੇਸ਼ਨ**: GitHub Models, Azure OpenAI, ਅਤੇ OpenAI Java SDK ਇੰਟੀਗ੍ਰੇਸ਼ਨ ਸੈਟਅੱਪ  
- **Spring Boot + Spring AI**: ਉਦਯੋਗਿਕ AI ਐਪਲੀਕੇਸ਼ਨ ਵਿਕਾਸ ਲਈ ਸਭ ਤੋਂ ਵਧੀਆ ਤਰੀਕੇ  
- **GitHub Models**: ਪ੍ਰੋਟੋਟਾਈਪਿੰਗ ਅਤੇ ਸਿੱਖਣ ਲਈ ਮੁਫ਼ਤ AI ਮਾਡਲ ਪਹੁੰਚ (ਕੋਈ ਕਰੈਡਿਟ ਕਾਰਡ ਲੋੜੀਂਦਾ ਨਹੀਂ)  
- **ਵਿਕਾਸ ਦੇ ਸੰਦ**: Docker ਕੰਟੇਨਰ, VS Code, ਅਤੇ GitHub Codespaces ਕਨਫਿਗਰੇਸ਼ਨ  
- **[→ ਅਧਿਆਇ 2 ਸ਼ੁਰੂ ਕਰੋ](./02-SetupDevEnvironment/README.md)**  

### **ਅਧਿਆਇ 3: ਮੁੱਖ ਜਨਰੇਟਿਵ AI ਤਕਨੀਕਾਂ**  
- **ਪ੍ਰਾਂਪਟ ਇੰਜੀਨੀਅਰਿੰਗ**: AI ਮਾਡਲ ਦੇ ਉੱਤਮ ਜਵਾਬਾਂ ਲਈ ਤਕਨੀਕਾਂ  
- **ਐਮਬੈਡਿੰਗਜ਼ ਅਤੇ ਵੇਕਟਰ ਓਪਰੇਸ਼ਨ**: ਸੈਮੈਂਟਿਕ ਖੋਜ ਅਤੇ ਸਮਾਨਤਾ ਮੈਚਿੰਗ ਨੂੰ ਲਾਗੂ ਕਰਨਾ  
- **ਰਿਟਰੀਵਲ-ਅਗਮੈਂਟਡ ਜਨਰੇਸ਼ਨ (RAG)**: ਆਪਣੇ ਡਾਟਾ ਸਰੋਤਾਂ ਨਾਲ AI ਨੂੰ ਜੋੜਨਾ  
- **ਫੰਕਸ਼ਨ ਕਾਲਿੰਗ**: ਕਸਟਮ ਟੂਲ ਅਤੇ ਪਲੱਗਇਨ ਨਾਲ AI ਸਮਰੱਥਾਵਾਂ ਨੂੰ ਵਧਾਉਣਾ  
- **[→ ਅਧਿਆਇ 3 ਸ਼ੁਰੂ ਕਰੋ](./03-CoreGenerativeAITechniques/README.md)**  

### **ਅਧਿਆਇ 4: ਵਿਹਾਰਕ ਐਪਲੀਕੇਸ਼ਨ ਅਤੇ ਪ੍ਰੋਜੈਕਟ**  
- **ਪੈਟ ਸਟੋਰੀ ਜਨਰੇਟਰ** (`petstory/`): GitHub Models ਨਾਲ ਰਚਨਾਤਮਕ ਸਮੱਗਰੀ ਸਿਰਜਣਾ  
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK ਨਾਲ ਸਥਾਨਕ AI ਮਾਡਲ ਇੰਟੀਗ੍ਰੇਸ਼ਨ  
- **MCP ਕੈਲਕੂਲੇਟਰ ਸੇਵਾ** (`mcp/calculator/`): Spring AI ਨਾਲ ਬੁਨਿਆਦੀ ਮਾਡਲ ਕਾਂਟੈਕਸਟ ਪ੍ਰੋਟੋਕੋਲ ਲਾਗੂ ਕਰਨਾ  
- **[→ ਅਧਿਆਇ 4 ਸ਼ੁਰੂ ਕਰੋ](./04-PracticalSamples/README.md)**  

### **ਅਧਿਆਇ 5: ਜ਼ਿੰਮੇਵਾਰ AI ਵਿਕਾਸ**  
- **GitHub Models ਸੁਰੱਖਿਆ**: ਸਮੱਗਰੀ ਫਿਲਟਰੀਕਰਨ ਅਤੇ ਸੁਰੱਖਿਆ ਮਕੈਨਿਜ਼ਮ ਦੀ ਜਾਂਚ ਕਰੋ  
- **ਜ਼ਿੰਮੇਵਾਰ AI ਡੈਮੋ**: ਪ੍ਰੈਕਟਿਸ ਵਿੱਚ AI ਸੁਰੱਖਿਆ ਫਿਲਟਰਾਂ ਦੇ ਕੰਮ ਕਰਨ ਦਾ ਹੱਥ-ਅਨੁਭਵ  
- **ਸਭ ਤੋਂ ਵਧੀਆ ਤਰੀਕੇ**: ਨੈਤਿਕ AI ਵਿਕਾਸ ਅਤੇ ਤੈਨਾਤੀ ਲਈ ਜ਼ਰੂਰੀ ਦਿਸ਼ਾ-ਨਿਰਦੇਸ਼  
- **[→ ਅਧਿਆਇ 5 ਸ਼ੁਰੂ ਕਰੋ](./05-ResponsibleGenAI/README.md)**  

## ਵਾਧੂ ਸਰੋਤ  

- [AI Agents For Beginners](https://github.com/microsoft/ai-agents-for-beginners)  
- [Generative AI for Beginners using .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)  
- [Generative AI for Beginners using JavaScript](https://github.com/microsoft/generative-ai-with-javascript)  
- [Generative AI for Beginners](https://github.com/microsoft/generative-ai-for-beginners)  
- [ML for Beginners](https://aka.ms/ml-beginners)  
- [Data Science for Beginners](https://aka.ms/datascience-beginners)  
- [AI for Beginners](https://aka.ms/ai-beginners)  
- [Cybersecurity for Beginners](https://github.com/microsoft/Security-101)  
- [Web Dev for Beginners](https://aka.ms/webdev-beginners)  
- [IoT for Beginners](https://aka.ms/iot-beginners)  
- [XR Development for Beginners](https://github.com/microsoft/xr-development-for-beginners)  
- [Mastering GitHub Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI)  
- [Mastering GitHub Copilot for C#/.NET Developers](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)  
- [Choose Your Own Copilot Adventure](https://github.com/microsoft/CopilotAdventures)  
- [RAG Chat App with Azure AI Services](https://github.com/Azure-Samples/azure-search-openai-demo-java)  

**ਅਸਵੀਕਾਰਨਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਜਦੋਂ ਕਿ ਅਸੀਂ ਸਹੀਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁਚਨਾਵਾਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।