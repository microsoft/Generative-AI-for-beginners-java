# Generative AI for Beginners - Java Edition
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/my/beg-genai-series.8b48be9951cc574c.webp)

**အချိန်ပမာဏ**: အားလုံးအွန်လိုင်းပဲပြီးစီးနိုင်ပြီး ဒေသဒေတာမလိုအပ်ပါ။ ပတ်ဝန်းကျင် ပြင်ဆင်ရတာ ၂ မိနစ်တောင်သာယူပြီး နမူနာတွေကို လေ့လာဖို့ ၁-၃ နာရီထိ လိုအပ်နိုင်ပါသည်။

> **လျင်မြန်စွာ စတင်ရန်** 

1. ဒီ repository ကို သင့် GitHub အကောင့်သို့ fork လုပ်ပါ
2. **Code** → **Codespaces** tab → **...** → **New with options...** ကိုနှိပ်ပါ
3. ရေပမာဏမပြောင်းဘဲထားပါ– ဒီသင်တန်းအတွက် ဖန်တီးထားသော Development container ကို ရွေးမည်
4. **Create codespace** ကိုနှိပ်ပါ
5. ပတ်ဝန်းကျင်ပြင်ဆင်ထားဖို့ ~၂ မိနစ်စောင့်ပါ
6. တိုက်ရိုက် [ပထမဥပမာ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) သို့跳

## ဘာသာစကားများစွာထောက်ပံ့မှု

### GitHub Action နည်းလမ်းဖြင့် ထောက်ပံ့သည် (အလိုအလျောက်နဲ့ အမြဲတမ်းအချိန်နောက်ဆုံး)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](./README.md) | [Chinese (Simplified)](../zh-CN/README.md) | [Chinese (Traditional, Hong Kong)](../zh-HK/README.md) | [Chinese (Traditional, Macau)](../zh-MO/README.md) | [Chinese (Traditional, Taiwan)](../zh-TW/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../pt-BR/README.md) | [Portuguese (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ဒေသခံသို့ clone လုပ်လိုပါသလား?**
>
> ဒီ repository မှာ ဘာသာစကား ၅၀ ကျော်ဘာသာပြန်ထားသောကြောင့် ဒေါင်းလုပ်အရွယ်အစား ပြင်းပြမှုရှိပါသည်။ ဘာသာပြန်ချက် မပါဘဲ clone လုပ်ချင်ရင် sparse checkout ကိုအသုံးပြုပါ:
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
> သင်အတန်းကို ပိုမြန်ဆန်စွာပြီးစီးနိုင်ရန် လိုအပ်သော အရာအားလုံးရရှိပါသည်။
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## သင်တန်းဖွဲ့စည်းမှုနှင့် သင်ယူမှုလမ်းကြောင်း

### **အခန်း ၁: Generative AI အကြောင်းအရာမိတ်ဆက်**
- **အဓိက အယူအဆများ**: ဘယ်လိုမျိုး အကြီးစားဘာသာစကားမော်ဒယ်များ၊ token များ၊ embedding များနဲ့ AI ၏ စွမ်းဆောင်ရည်များ ကိုနားလည်ခြင်း
- **Java AI ပတ်ဝန်းကျင်**: Spring AI နဲ့ OpenAI SDK များအကြောင်းအနှစ်ချုပ်
- **Model Context Protocol**: MCP နှင့် AI agent တွေရဲ့ ဆက်သွယ်မှုအတွက် အခန်းကဏ္ဍ
- **လက်တွေ့အသုံးချမှုများ**: စကားပြောရသူများနှင့် အသုံးစရိတ်ဖန်တီးခြင်းနမူနာများ
- **[→ အခန်း ၁ စတင်ရန်](./01-IntroToGenAI/README.md)**

### **အခန်း ၂: ဖန်တီးမှု ပတ်ဝန်းကျင် ပြင်ဆင်ခြင်း**
- **အမျိုးမျိုးသော ပံ့ပိုးသူများ ဖန်တီးခြင်း**: GitHub Models, Azure OpenAI, OpenAI Java SDK တို့ကို တွဲဖက်စနစ်တကျပြင်ဆင်ခြင်း
- **Spring Boot + Spring AI**: စက်မှုဇုန်အဆင့် AI အသုံးချမှုအတွက် ထိရောက်ဆုံးနည်းလမ်းများ
- **GitHub Models**: prototype ဖန်တီးခြင်းနှင့် သင်ယူရန် အခမဲ့ AI မော်ဒယ် အသုံးပြုခွင့် (ကရက်ဒစ်ကတ် မလိုအပ်)
- **ဖန်တီးခြင်းကိရိယာများ**: Docker containers, VS Code နှင့် GitHub Codespaces တို့၏ ဖွဲ့စည်းမှု
- **[→ အခန်း ၂ စတင်ရန်](./02-SetupDevEnvironment/README.md)**

### **အခန်း ၃: Core Generative AI နည်းပညာများ**
- **Prompt Engineering**: AI မော်ဒယ်အတွက် အကောင်းဆုံးဖြေကြားမှုနည်းလမ်းများ
- **Embeddings နှင့် Vector လုပ်ဆောင်ချက်များ**: semantic ရှာဖွေမှု နှင့် ဆန်မြိုက်မှုကို တိုးမြှင့်ခြင်း
- **Retrieval-Augmented Generation (RAG)**: သင်၏ဒေတာအရင်းအမြစ်များနှင့် AI တွဲဖက်ခြင်း
- **Function Calling**: AI စွမ်းဆောင်ရည်တို့ကို ထပ်မံတိုးချဲ့ဖို့ ကိရိယာ အသစ်များနှင့် plugins များဖြင့်
- **[→ အခန်း ၃ စတင်ရန်](./03-CoreGenerativeAITechniques/README.md)**

### **အခန်း ၄: လက်တွေ့ အသုံးချမှုနဲ့ ပရောဂျက်များ**
- **ကလေးတိရစ္ဆာန် ပုံပြင်ဖန်တီးသူ** (`petstory/`): GitHub Models ဖြင့် ဖန်တီးမှုနည်းလမ်း
- **Foundry Local လူကြိုက်များ Demo** (`foundrylocal/`): OpenAI Java SDK နဲ့ ဒေသခံ AI မော်ဒယ် ပေါင်းစည်းခြင်း
- **MCP ကိန်းဂဏန်းတွက် စနစ်** (`calculator/`): Spring AI နဲ့ အခြေခံ Model Context Protocol တည်ဆောက်မှု
- **[→ အခန်း ၄ စတင်ရန်](./04-PracticalSamples/README.md)**

### **အခန်း ၅: တာဝန်ယူနိုင်သော AI ဖန်တီးခြင်း**
- **GitHub Models လုံခြုံရေး**: အတွင်းသို့ပါဝင်သည့် အကြောင်းအရာ ဖိလ်တာခြင်းနဲ့ လုံခြုံရေး စနစ်များ (တင်းကြပ်သည့် ကန့်သတ်ချက်များနှင့် နူးညံ့သော ငြင်းပယ်မှုများ) ကို စမ်းသပ်ခြင်း
- **တာဝန်ရှိသော AI Demo**: ခေတ်မီ AI လုံခြုံရေးစနစ်များ လက်တွေ့ လုပ်ဆောင်ပုံမှန် ပြသမှု
- **အကောင်းဆုံး ကျင့်ဝတ်များ**: ကိုယ့်ရဲ့ AI ဖန်တီးမှုတွေအတွက် သင်ယူသင့်သော လမ်းညွှန်ချက်များ
- **[→ အခန်း ၅ စတင်ရန်](./05-ResponsibleGenAI/README.md)**

## အပိုဆောင်း အရင်းအမြစ်များ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain for Beginners](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
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
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot စီးရီးများ
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## ကူညီမှု ရယူရန်

AI application များတည်ဆောက်ရာ၌ မရောက်ပါက သို့မဟုတ် မေးခွန်းများရှိပါက MCP အကြောင်းစကားပြောဆိုရန် အတူတူပညာသင်သည့်သူများနှင့် အတွေ့အကြုံရှိသူ ဖွံ့ဖြိုးရေးရှင်များနှင့် တွဲဖက်ဆွေးနွေးပါ။ ၎င်းသည် မေးခွန်းများကို ကြိုဆိုပြီး ဆောင်ရွက်ပေးသည့်လူမှုအသိုင်းအဝိုင်း တစ်ခုဖြစ်သည်။

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ထုတ်ကုန်တုံ့ပြန်ချက်များ သို့မဟုတ် တည်ဆောက်ရာတွင် အမှားများ ရှိပါက အောက်ဖော်ပြပါနေရာသို့ သွားရောက်ပါ။

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အသိပေးချက်**  
ဤစာတမ်းကို AI ဘာသာပြန်ခြင်း ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် မှန်ကန်မှုအတွက် ကြိုးစားဆောင်ရွက်ပါသော်လည်း၊ စက်ရုပ်ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မှန်ကန်မှုမပြည့်စုံမှုများ ရှိနိုင်ကြောင်း သိရှိထားပေးပါရန် အသိပေးအပ်ပါသည်။ မူရင်းစာရွက်စာတမ်းကို မိမိဘာသာစကားဖြင့်ရှိသည့် ပုံစံဆိုသည်မှာ တရားဝင်အရင်းအမြစ်အဖြစ် ဦးစားပေးစဉ်းစားသင့်ပါသည်။ အရေးကြီးသည့် အချက်အလက်များအတွက်တော့ လူ့ဘာသာပြန်ခြင်းကို သတ်မှတ်အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းကြောင့် ဖြစ်ပေါ်နိုင်သည့် မမှန်ကန်ခြင်းများ သို့မဟုတ် သဘောထား မတူညီမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မယူပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->