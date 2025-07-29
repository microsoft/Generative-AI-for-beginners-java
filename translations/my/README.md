<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T16:31:43+00:00",
  "source_file": "README.md",
  "language_code": "my"
}
-->
# Java အတွက် စတင်သူများအတွက် Generative AI  
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generative AI for Beginners - Java Edition](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.my.png)

**အချိန်လိုအပ်ချက်**: အလုပ်ရုံဆွေးနွေးပွဲတစ်ခုလုံးကို ဒေသတွင်းတွင် အဆင့်ဆင့်တပ်ဆင်ရန်မလိုဘဲ အွန်လိုင်းတွင် ပြီးစီးနိုင်ပါသည်။ ပတ်ဝန်းကျင်ကို တပ်ဆင်ရန် ၂ မိနစ်သာလိုအပ်ပြီး၊ နမူနာများကို စူးစမ်းရန် ၁-၃ နာရီကြာနိုင်ပါသည် (စူးစမ်းမှုအနက်အနားပေါ်မူတည်၍)။

> **အမြန်စတင်ရန်**

1. ဤ repository ကို သင်၏ GitHub အကောင့်သို့ Fork လုပ်ပါ  
2. **Code** → **Codespaces** tab → **...** → **New with options...** ကိုနှိပ်ပါ  
3. ပုံမှန်အတိုင်းထားပါ – ဤသင်တန်းအတွက် ဖန်တီးထားသော Development container ကို ရွေးချယ်ပါမည်  
4. **Create codespace** ကိုနှိပ်ပါ  
5. ပတ်ဝန်းကျင်အဆင်သင့်ဖြစ်ရန် ~2 မိနစ်စောင့်ပါ  
6. [ပထမဦးဆုံး နမူနာ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) သို့ တိုက်ရိုက်သွားပါ  

## ဘာသာစကားများအထောက်အပံ့

### GitHub Action မှတဆင့် ထောက်ပံ့ထားသည် (အလိုအလျောက် & အမြဲနောက်ဆုံးပေါ်)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](./README.md)

## သင်တန်းဖွဲ့စည်းမှုနှင့် သင်ယူလမ်းကြောင်း

### **အခန်း ၁: Generative AI ကိုမိတ်ဆက်ခြင်း**
- **အဓိကအကြောင်းအရာများ**: Large Language Models, tokens, embeddings, နှင့် AI စွမ်းရည်များကို နားလည်ခြင်း  
- **Java AI Ecosystem**: Spring AI နှင့် OpenAI SDKs ၏ အကျဉ်းချုပ်  
- **Model Context Protocol**: MCP နှင့် AI agent ဆက်သွယ်မှုတွင် ၎င်း၏ အခန်းကဏ္ဍ  
- **လက်တွေ့အသုံးချမှုများ**: Chatbots နှင့် အကြောင်းအရာဖန်တီးခြင်းအပါအဝင် အမှန်တကယ်အခြေအနေများ  
- **[→ အခန်း ၁ စတင်ရန်](./01-IntroToGenAI/README.md)**  

### **အခန်း ၂: ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင် တပ်ဆင်ခြင်း**
- **Multi-Provider Configuration**: GitHub Models, Azure OpenAI, နှင့် OpenAI Java SDK အစီအစဉ်များကို တပ်ဆင်ခြင်း  
- **Spring Boot + Spring AI**: စီးပွားရေးလုပ်ငန်း AI အပလီကေးရှင်းဖွံ့ဖြိုးတိုးတက်မှုအတွက် အကောင်းဆုံးလက်တွေ့ကျကျနည်းလမ်းများ  
- **GitHub Models**: အခမဲ့ AI မော်ဒယ်များကို စမ်းသပ်ခြင်းနှင့် သင်ယူခြင်းအတွက် အသုံးပြုနိုင်မှု (အကြွေးဝယ်ကတ်မလိုအပ်ပါ)  
- **ဖွံ့ဖြိုးရေးကိရိယာများ**: Docker containers, VS Code, နှင့် GitHub Codespaces ကို တပ်ဆင်ခြင်း  
- **[→ အခန်း ၂ စတင်ရန်](./02-SetupDevEnvironment/README.md)**  

### **အခန်း ၃: Generative AI နည်းလမ်းများ၏ အဓိကအချက်များ**
- **Prompt Engineering**: AI မော်ဒယ်မှ အကောင်းဆုံးဖြေရှင်းချက်များရရှိရန် နည်းလမ်းများ  
- **Embeddings & Vector Operations**: Semantic search နှင့် ဆင်တူမှုကို အကောင်အထည်ဖော်ခြင်း  
- **Retrieval-Augmented Generation (RAG)**: AI ကို သင်၏ကိုယ်ပိုင်ဒေတာရင်းမြစ်များနှင့် ပေါင်းစပ်ခြင်း  
- **Function Calling**: စိတ်ကြိုက်ကိရိယာများနှင့် plugins များဖြင့် AI စွမ်းရည်များကို တိုးချဲ့ခြင်း  
- **[→ အခန်း ၃ စတင်ရန်](./03-CoreGenerativeAITechniques/README.md)**  

### **အခန်း ၄: လက်တွေ့အသုံးချမှုများနှင့် ပရောဂျက်များ**
- **Pet Story Generator** (`petstory/`): GitHub Models ဖြင့် ဖန်တီးမှုဆိုင်ရာ အကြောင်းအရာဖန်တီးခြင်း  
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK ဖြင့် ဒေသတွင်း AI မော်ဒယ်ပေါင်းစပ်ခြင်း  
- **MCP Calculator Service** (`calculator/`): Spring AI ဖြင့် အခြေခံ Model Context Protocol ကို အကောင်အထည်ဖော်ခြင်း  
- **[→ အခန်း ၄ စတင်ရန်](./04-PracticalSamples/README.md)**  

### **အခန်း ၅: တာဝန်ရှိသော AI ဖွံ့ဖြိုးတိုးတက်မှု**
- **GitHub Models Safety**: Built-in content filtering နှင့် safety mechanisms (hard blocks နှင့် soft refusals) ကို စမ်းသပ်ခြင်း  
- **Responsible AI Demo**: ခေတ်မီ AI safety systems များကို လက်တွေ့အသုံးပြုနည်း  
- **အကောင်းဆုံးလက်တွေ့ကျကျနည်းလမ်းများ**: တာဝန်ရှိသော AI ဖွံ့ဖြိုးတိုးတက်မှုနှင့် တင်သွင်းမှုအတွက် အရေးကြီးသော လမ်းညွှန်ချက်များ  
- **[→ အခန်း ၅ စတင်ရန်](./05-ResponsibleGenAI/README.md)**  

## အပိုဆောင်းအရင်းအမြစ်များ  

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

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မတိကျမှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို ၎င်း၏ မူလဘာသာစကားဖြင့် အာဏာတရားရှိသော အရင်းအမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူ့ဘာသာပြန်ပညာရှင်များမှ ပရော်ဖက်ရှင်နယ် ဘာသာပြန်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲအလွတ်များ သို့မဟုတ် အနားလွဲမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။