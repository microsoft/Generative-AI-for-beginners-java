<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:56:35+00:00",
  "source_file": "README.md",
  "language_code": "my"
}
-->
# Generative AI for Beginners - Java Edition
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/my/beg-genai-series.8b48be9951cc574c.webp)

**အချိန်စွန့်မှု**: အလုပ်ရုံသင်တန်းတစ်ခုလုံးကို ဒေသဆိုင်ရာပြင်ဆင်မှုမလိုဘဲ အွန်လိုင်းတွင် ပြီးမြောက်နိုင်သည်။ ပတ်ဝန်းကျင်ရဲ့ပြင်ဆင်ခြင်းကို ၂ မိနစ်ယူပြီး နမူနာတွေကို လေ့လာဖို့မှာ ၁ မှ ၃ နာရီကြား ရှိနိုင်သည်။

> **လျင်မြန်စတင်ခြင်း**

1. ဤဂစ်ဟပ် လောကည်ကို သင့် GitHub အကောင့်သို့ Fork လုပ်ပါ။
2. **Code** ကိုနှိပ်ပြီး → **Codespaces** tab → **...** → **New with options...** ကိုနှိပ်ပါ။
3. ပုံမှန် များကိုအသုံးပြုပါ – ဤသည်မှာ ဒီသင်တန်းအတွက် ဖန်တီးထားသော Development container ကို ရွေးချယ်မည်ဖြစ်သည်။
4. **Create codespace** ကိုနှိပ်ပါ။
5. ပတ်ဝန်းကျင် အသင့်ဖြစ်ရန် ~၂ မိနစ် စောင့်ပါ။
6. တိုက်ရိုက် [ပထမဥပမာ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) သို့ အက်​​ဇ်ပါ။

> **ဒေသဆိုင်ရာ ကို Clone လုပ်ချင်ပါသလား?**
>
> ဤဂစ်ဟပ် လောကည်တွင် ဘာသာစကား ၅၀ ကျော်ကို ထည့်သွင်းထားခြင်းကြောင့် ဒေါင်းလုဒ် အရွယ်အစား စုစုပေါင်း တိုးပွားသွားပါသည်။ ဘာသာပြန်ချက် မပါပါက ဆော့ပါက တိုတောင်းသော sparse checkout ကို အသုံးပြုပါ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> သင်သင်တန်းကို အားလုံးပြီးမြောက်ရန်လိုအပ်သည့် အရာအားလုံးကောင်းမွန်စွာ ရရှိမည်ဖြစ်ပြီး ဒေါင်းလုဒ် လျင်မြန်ပါသည်။

## ဘာသာစကား မျိုးစုံ အထောက်အပံ့

### GitHub Action မှတဆင့် ထောက်ပံ့သည် (အလိုအလျောက် နှင့် အမြဲတမ်း အပ်ဒိတ်ဖြစ်နေသည်)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](./README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../br/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)

> **ဒေသဆိုင်ရာ ကို Clone လုပ်ချင်ပါသလား?**

> ဤဂစ်ဟပ် လောကည်တွင် ဘာသာစကား ၅၀ ကျော်ကို ထည့်သွင်းထားခြင်းကြောင့် ဒေါင်းလုဒ် အရွယ်အစား စုစုပေါင်း တိုးပွားသွားပါသည်။ ဘာသာပြန်ချက် မပါပါက ဆော့ပါက တိုတောင်းသော sparse checkout ကို အသုံးပြုပါ:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> သင်သင်တန်းကို အားလုံးပြီးမြောက်ရန်လိုအပ်သည့် အရာအားလုံးကောင်းမွန်စွာ ရရှိမည်ဖြစ်ပြီး ဒေါင်းလုဒ် လျင်မြန်ပါသည်။
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## သင်တန်းဖွဲ့စည်းမှု & သင်ယူနည်းလမ်းကြောင်း

### **အပိုင်း ၁: Generative AI အစိတ်အပိုင်း မိတ်ဆက်ခြင်း**
- **အခြေခံအယူအဆများ**: ကြီးမားသောဘာသာစကားမော်ဒယ်များ၊ tokens, embeddings နှင့် AI စွမ်းရည်များကို နားလည်ခြင်း
- **Java AI ပတ်ဝန်းကျင်**: Spring AI နှင့် OpenAI SDK များအကြောင်း အခြေခံ သိရှိခြင်း
- **Model Context Protocol**: MCP မိတ်ဆက်ခြင်းနှင့် AI အေးဂျင့်စာဆက်ဆံမှုတွင် အလုပ်လုပ်ပုံ
- **လက်တွေ့အသုံးချမှုများ**: စကားပြောခြင်းစက်များနှင့် တိုက်ရိုက် အကြောင်းအရာဖန်တီးခြင်းကဲ့သို့သော ကိစ္စရပ်များ
- **[→ အပိုင်း ၁ စတင်ရန်](./01-IntroToGenAI/README.md)**

### **အပိုင်း ၂: ဖန်တီးရေးပတ်ဝန်းကျင် တည်ဆောက်ခြင်း**
- **မူလကျပါသော Provider များ ပေါင်းစပ်ခြင်း**: GitHub Models, Azure OpenAI နှင့် OpenAI Java SDK တို့ကို တပ်ဆင်ခြင်း
- **Spring Boot + Spring AI**: စီးပွားရေး AI အက်ပလီကေးရှင်း ဖန်တီးရာတွင် အကောင်းဆုံးလမ်းညွှန်ချက်များ
- **GitHub Models**: prototype များနှင့် သင်ယူရန်အတွက် အခမဲ့ AI မော်ဒယ် အသုံးပြုခွင့် (credit card မလိုအပ်ပါ)
- **ဖန်တီးရေးကိရိယာများ**: Docker containers, VS Code နှင့် GitHub Codespaces တပ်ဆင်မှုများ
- **[→ အပိုင်း ၂ စတင်ရန်](./02-SetupDevEnvironment/README.md)**

### **အပိုင်း ၃: Core Generative AI နည်းပညာများ**
- **Prompt Engineering**: AI မော်ဒယ်တုံ့ပြန်မှုအတွက် ထိထိမိမိနည်းပညာများ
- **Embeddings & Vector လုပ်ဆောင်ခြင်းများ**: စာအဓိပ္ပာယ်ရှာဖွေရေးနှင့် ဆင်တူမှုကို တိုက်ဆိုင်စွာ လုပ်ဆောင်ခြင်း
- **Retrieval-Augmented Generation (RAG)**: သင့်ဒေတာအရင်းအမြစ်များဖြင့် AI ပေါင်းစပ်ခြင်း
- **Function Calling**: AI စွမ်းရည်များကို စိတ်ကြိုက်ကိရိယာများနှင့် plugin များဖြင့် ဖြည့်စည်းခြင်း
- **[→ အပိုင်း ၃ စတင်ရန်](./03-CoreGenerativeAITechniques/README.md)**

### **အပိုင်း ၄: လက်တွေ့အသုံးချခြင်းများနှင့် ပြုလုပ်မှုများ**
- **ခွေးမွေးပုံပြောဆိုခြင်း ဖန်တီးစက်** (`petstory/`): GitHub Models ဖြင့် ဖန်တီးမှုအကြောင်းအရာများထုတ်ပေးခြင်း
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK နှင့် ဒေသတွင်း AI မော်ဒယ် ပေါင်းစပ်ခြင်း
- **MCP ကိန်းဂဏန်းတွက်စက် စီမံခန့်ခွဲခြင်း** (`calculator/`): Model Context Protocol ရိုးရှင်းစွာ Spring AI ဖြင့် တပ်ဆင်ခြင်း
- **[→ အပိုင်း ၄ စတင်ရန်](./04-PracticalSamples/README.md)**

### **အပိုင်း ၅: တာဝန်ယူရမည့် AI ဖန်တီးခြင်း**
- **GitHub Models ဘေးကင်းခြင်း**: အတွင်းတပ်ထားသော အကြောင်းအရာစစ်ထုတ်မှုနှင့် ဘေးကင်းမှုမှုစနစ်များ စမ်းသပ်ခြင်း (ခဲတံခွန်နှင့် နူးညံ့သောစနစ်များ)
- **တာဝန်ယူမှု AI အသုံးပြုမှု စမ်းသပ်မှု**: မှီဝဲဆက်ဆံမှု အမှုဖေါ်ပြချက် များ
- **အကောင်းဆုံး လမ်းညွှန်ချက်များ**: တာဝန်ယူမှု AI ဖန်တီးခြင်းနှင့် ထုတ်လွှင့်ခြင်းအတွက် မရှိမဖြစ်လိုအပ်သော ညွှန်ကြားချက်များ
- **[→ အပိုင်း ၅ စတင်ရန်](./05-ResponsibleGenAI/README.md)**

## အပိုဆောင်း ရင်းမြစ်များ

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
 
### အခြေခံ သင်ယူမှု
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![စက်ပစ္စည်းများအတွက် လူသစ်များ](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR ဖွံ့ဖြိုးတိုးတက်မှု လူသစ်များအတွက်](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Series
[![AI တွဲဖက်ပရိုဂရမ်မင်းအတွက် Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET အတွက် Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot စွန့်စားမှု](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## အကူအညီရယူခြင်း

AI အက်ပ်များ ဖန်တီးရာတွင် အဆင်မပြေမဖြစ်ပါက သို့မဟုတ် မေးခွန်းများရှိပါက MCP အကြောင်း ဆွေးနွေးရာတွင် ပညာရှင်များနှင့် အတွေ့အကြုံရှိသော ဖွံ့ဖြိုးသူများနှင့် တွဲဖက်ပါ။ မေးခွန်းများကို ကြိုဆိုသော နှင့် အသိပညာများကို အခမဲ့ဝေမျှသော ပံ့ပိုးကူညီမှု အဖွဲ့အစည်းတစ်ခု ဖြစ်သည်။

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

ထုတ်ကုန်ရဲ့ တုံ့ပြန်ချက်များ သို့မဟုတ် ဖန်တီးခြင်း ကြားတွင် အမှားများရှိပါက အောက်ပါလိပ်စာကို သွားရောက်ကြည့်ပါ။

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**အငြင်းပွားချက် မပါသောကြေညာချက်**  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှုဖြစ်သော [Co-op Translator](https://github.com/Azure/co-op-translator) ကိုအသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးစားပေမယ့် အလိုအလျောက် ဘာသာပြန်မှုတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များဖြစ်ပေါ်နိုင်သည်ကို လေးစားစွာအသိပေးပါသည်။ မူလစာရွက်စာတမ်းကို သက်ဆိုင်ရာ မူရင်းဘာသာဖြင့်သာ မှန်ကန်မှုအာမခံချက် အဖြစ် ယူဆသင့်ပါသည်။ အရေးကြီးသည့် သတင်းအချက်အလက်များအတွက် ကျွမ်းကျင် လက်တွေ့ ဘာသာပြန်ခြင်းကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက် အသုံးပြုမှုမှ ဆက်စပ်ပြီး ဖြစ်ပေါ်နိုင်သည့် နားလည်မှုမှားမှုများ သို့မဟုတ် မမှန်ကန်မှု၏ တာဝန်ကို ကျွန်ုပ်တို့ မယူဆောင်ပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->