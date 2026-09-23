# Azure AI Foundry အတွက် ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို စတင်ပြင်ဆင်ခြင်း

> ဒီလမ်းပြမှာ ဒီသင်ရိုးကိုယ်တိုင် Java AI အက်ပ်များအတွက် **Azure AI Foundry** မော်ဒယ်များကို **keyless** authentication (Microsoft Entra ID) အသုံးပြု၍ တပ်ဆင်ပေးသည် — API key မလိုအပ်ပါ။ သက်ဆိုင်ရာ ကိရိယာအသစ်တွေ သုံးခွင့်မရှိလား? [ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင် လမ်းညွှန်](./README.md) ဖြင့် စတင်ပါ။

ဒီလမ်းပြက ဒီသင်ရိုး Java AI အက်ပ်အတွက် **Azure AI Foundry** မော်ဒယ်များကို တပ်ဆင်ပေးပါသည်။ သင်တွင် နှစ်မျိုးသောရွေးချယ်စရာတွေရှိသည်။

- **ရွေးချယ်မှု A — `azd` + Bicep ဖြင့် Provision လုပ်ခြင်း (အကြံပြု):** တစ်ချက်ဇယားတည်းက လုပ်ဆောင်မှုတစ်ခုဖြင့် Foundry အကောင့်နှင့် မော်ဒယ်များအား ကိုဒ်ပုံစံဖြင့် တပ်ဆင်ပေးသည်။ Portal တွင် နှိပ်ရန်မလို။
- **ရွေးချယ်မှု B — Azure AI Foundry portal တွင် လက်ဖြင့် အရင်းအမြစ်များ ဖန်တီးခြင်း**

နှစ်ခုစလုံးသည် **keyless authentication** (Microsoft Entra ID) ဖြင့် အသုံးပြုသည် — API key များကူးယူခြင်း သို့မဟုတ် ဖော်ပြခြင်း မရှိပါ။

## စာရင်းဇယား

- [ဘာတွေဖန်တီးမလဲ](#ဘာတွေ-ဖန်တီးမလဲ)
- [လိုအပ်ချက်များ](#လိုအပ်ချက်များ)
- [ရွေးချယ်မှု A: azd + Bicep ဖြင့် Provision လုပ်ခြင်း (အကြံပြု)](#option-a-provision-with-azd--bicep-recommended)
- [ရွေးချယ်မှု B: လက်ဖြင့် အရင်းအမြစ်များ ဖန်တီးခြင်း](#ရွေးချယ်မှု-b-လက်ဖြင့်-အရင်းအမြစ်များ-ဖန်တီးခြင်း)
- [ပတ်ဝန်းကျင်ကို ပြင်ဆင်ခြင်း](#ပတ်ဝန်းကျင်ကို-ပြင်ဆင်ခြင်း)
- [ပြင်ဆင်မှု စစ်ဆေးခြင်း](#ပြင်ဆင်မှု-စစ်ဆေးရန်)
- [နောက်တစ်ဆင့်?](#နောက်တစ်ဆင့်)
- [အရင်းအမြစ်များ](#အရင်းအမြစ်များ)
- [ထပ်ဆောင်း အရင်းအမြစ်များ](#ထပ်ဆောင်း-အရင်းအမြစ်များ)

## ဘာတွေ ဖန်တီးမလဲ

[`infra/`](../../../02-SetupDevEnvironment/infra) တွင်ရှိသော Bicep တမ်းပလိတ်များက အောက်ပါအရာများကို provision လုပ်ပေးသည်။

- **Azure AI Foundry** အကောင့် (`Microsoft.CognitiveServices/accounts`, kind `AIServices`) နှင့် project တစ်ခု
- **chat** deployment - GPT-5.6 Luna (`gpt-5.6-luna`), version `2026-07-09`, `GlobalStandard` စွမ်းဆောင်ရည် `10` (ဒီမော်ဒယ်အတွက် တစ်မိနစ်လျှင် မေးခွန်း 10 ခုနှင့် တိုးကင် 10,000 ကျော်)
- **embedding** deployment - `text-embedding-3-small`, version `1` (နောက်ပိုင်းအခန်းများတွင် အသုံးပြုမည့်)
- **keyless role assignment** (`Cognitive Services OpenAI User`) ကသာ သင် `az login` ဖြင့် လက်မှတ်ထိုးဝင်ရန် မျက်နှာစာများကို စီမံခန့်ခွဲရန်မလို

## လိုအပ်ချက်များ

- [Azure subscription](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) နဲ့ [Maven 3.9+](https://maven.apache.org/download.cgi)

## ရွေးချယ်မှု A: azd + Bicep ဖြင့် Provision လုပ်ခြင်း (အကြံပြု)

`02-SetupDevEnvironment` ဖိုလ်ဒါမှ:

```bash
cd 02-SetupDevEnvironment

# အကောင့်ဝင်ပါ (ကိရိယာနှစ်ခု)
azd auth login
az login

# Foundry အကောင့်နှင့် မော်ဒယ် တပ်ဆင်မှုများ ထုတ်ပေးပါ
azd up
```

`azd` က **environment name** (ဥပမာ `genai-java`), **subscription** နဲ့ **ဒေသ** ရွေးဖို့ မေးပါမယ်။ သင့် subscription ကို နှင့် `gpt-5.6-luna` နဲ့ `text-embedding-3-small` တွေ ရနိုင်တဲ့ဒေသတစ်ခု (ဥပမာ `eastus2`) ရွေးချယ်ပါ။ subscription တွင် မော်ဒယ်အမျိုးအစား နဲ့ deployment အမျိုးအစားအတွက် လုံလောက်သော quota ရှိသည်ဟု အတည်ပြုပါ; ရရှိနိုင်မှုနဲ့ quota ကို subscription အလိုက် မတူကြပါ။

Provision ပြီးဆုံးသောအခါ azd သည်

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) တွင် သတ်မှတ်ထားသော အရာအားလုံးကို တပ်ဆင်ပေးသည်။
2. စီမံဆောင်ရွက်ပြီးနောက်  hook တစ်ခုကို Run လုပ်ပြီး [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) တွင် သင်၏ endpoint နဲ့ deployment အမည်များ (လျှို့ဝှက်ချက်မရှိ) ကို ရေးသားပေးသည်။

> **အကြံပြုချက်**: ပြောင်းလဲမှုတွေကို ပြန်လည်အသုံးပြုရန်အတွက် `azd up` ကို မည်သည့်အခါမဆို ပြန်လည်အတည်ပြုနိုင်သည်။ စုစုပေါင်းအရာများကိုဖျက်ရန် နဲ့ ကုန်ကျစရိတ် ရပ်တန့်ရန် `azd down` ကို အသုံးပြုပါ။

ဖန်တီးထားသော တပ်ဆင်မှု အချက်အလက်များကို ကြည့်ရန်:

```bash
azd env get-values
```

ယခု [ပြင်ဆင်မှု စစ်ဆေးရန်](#ပြင်ဆင်မှု-စစ်ဆေးရန်) သို့ လွတ်လပ်စွာ ပြေးပါ။

## ရွေးချယ်မှု B: လက်ဖြင့် အရင်းအမြစ်များ ဖန်တီးခြင်း

Portal ကို ပိုနှစ်သက်တယ်ဆိုရင် အရင်းအမြစ်တွေကို လက်ဖြင့်ဖန်တီးပါ။

1. [Azure AI Foundry portal](https://ai.azure.com/) သို့ သွားပြီး လက်မှတ်ထိုးဝင်ပါ။
2. **Project အသစ်ဖန်တီးပါ** (ဒါကို ဖန်တီးရာမှာ AI Foundry အရင်းအမြစ်တစ်ခုလည်း ဖန်တီးပါသည်)။ `GenAIJava` ဆိုတဲ့နာမည်ပေးပါ။
3. သင့် project တွင် **Models + endpoints** → **Deploy model** → **Deploy base model** ကို ဖွင့်ပါ။
4. **GPT-5.6 Luna** (မော်ဒယ်နဲ့ deployment နာမည် `gpt-5.6-luna`, version `2026-07-09`) ကို **Global Standard** စွမ်းဆောင်ရည် `10` နဲ့ တပ်ဆင်ပါ။ အကယ်၍ embedding နမူနာများလိုတယ်ဆိုရင် **text-embedding-3-small**, version `1` ကိုလည်း ထပ်မံ တပ်ဆင်ပါ။
5. **Overview** မှာရှိတဲ့ **endpoint** ကို ကူးယူပါ (ဥပမာ `https://<resource>.openai.azure.com/`)
6. Keyless access ခွင့်ပြုပါ: အရင်းအမြစ်တွင် **Access control (IAM)** → **Add role assignment** → သင့်အကောင့်အား **Cognitive Services OpenAI User** အဖြစ်စာရင်းသွင်းပါ။

> **အခက်အခဲရှိသေးလား?** [Azure AI Foundry စာတမ်းများ](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) ကို ကြည့်ရှုပါ။

## ပတ်ဝန်းကျင်ကို ပြင်ဆင်ခြင်း

**Option A (`azd up`) ကို သုံးခဲ့ပါက** သင်တို့၏ အချက်အလက်ဖိုင် ပြီးပြည့်စုံပြီးဖြစ်သည် — ပြင်ဆင်ရန် မလိုပါ။ [ပြင်ဆင်မှု စစ်ဆေးရန်](#ပြင်ဆင်မှု-စစ်ဆေးရန်) သို့ တိုက်ရိုက်သွားပါ။

**Option B (လက်ဖြင့်) ကို သုံးခဲ့ပါက** နမူနာ `.env` ဖိုင်ကို ကိုယ့်အရ ချမှတ်ပါ။

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` ကို သင်၏ endpoint ဖြင့် တည်းဖြတ်ပါ (key မပါ — authentication သည် keyless ဖြစ်သည်)။

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Resource ၏ Azure OpenAI endpoint ကို သုံးပါ၊ project URL မဟုတ်ပါ။ basic-chat app သည် `/openai/v1` သို့ အလိုအလျောက် သွားရောက်ပြီး ဂျက်ဆက်ရှင်းပြုလုပ်ထားသော bearer-token client ဖြင့် ဆက်သွယ်သည်; API key မလိုပါ။

> **လုံခြုံရေး အချက်အလက်**: သိမ်းဆည်းရမည့် API key မရှိပါ။ သင့်ရဲ့ authentication ကို Microsoft Entra ID အသုံးပြု၍ `az login` (ကိုယ်တိုင်မှ) သို့မဟုတ် managed identity (Azure တွင်) ဖြင့် ဆောင်ရွက်ပါသည်။ `.env` ဖိုင်တွင် လျှို့ဝှက်ချက်မပါတဲ့ ပြင်ဆင်ချက်များသာ ပါဝင်ပြီး `.gitignore` မှာလည်းကောင်းစွာ ထည့်ထားသည်။

## ပြင်ဆင်မှု စစ်ဆေးရန်

Keyless authentication အတွက် token ရနိုင်ဖို့ သင်ဝင်ထားခြင်းကို သေချာစေပြီး နမူနာကို လုပ်ဆောင်ပါ-

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # သင်မပထမဆုံးကအကောင့်ဝင်ထားခြင်းမရှိသေးပါက
mvn clean spring-boot:run
```

သင်သည် `gpt-5.6-luna` မော်ဒယ်မှ ပြန်ကြားချက်ကို မြင်ရမည်။ နမူနာတွေကို ဆက်တိုက်ပြေးပါက မူရင်း အသေးစား quota ထဲမှာသာ နေပါမယ်။ HTTP 429 တက်လာလျှင် retry အချိန်ကို စောင့်ပြီး ထပ်မံကြိုးစားပါ။

> **VS Code အသုံးပြုသူများ**: `F5` ကိုနှိပ်၍ ဆေးလာပါ။ app သည်သင်၏ `.env` ကို အလိုအလျောက်သက်ဆိုင်ရာဖြည့်ဆည်းပေးပါသည်။

> **လုံးဝနမူနာ**: ပိုမို အသေးစိတ်နဲ့ ပြသနာဖြေရှင်းရေးအတွက် [Azure AI Foundry နှင့် Basic Chat ဥပမာ](./examples/basic-chat-azure/README.md) ကို ကြည့်ပါ။

## နောက်တစ်ဆင့်?

Provision ပြီးနောက် နမူနာကို အောင်မြင်စွာ Run ပြီးသည့်အခါ သင်မှာဖြစ်ပါမယ်-
- `gpt-5.6-luna` နဲ့ `text-embedding-3-small` နဲ့ Azure AI Foundry တပ်ဆင်ပြီး
- Keyless authentication (Microsoft Entra ID) — စီမံခန့်ခွဲရန် key မလို
- သင့် endpoint နဲ့ deployment အမည်များပါရှိသော ဒေသခံ `.env`
- Java ဖွံ့ဖြိုးရေး ပတ်ဝန်းကျင် ပြင်ဆင်ပြီး စတင်အသုံးပြုနိုင်ပြီ

**ဆက်လက်ပြုလုပ်ရန်** [အခန်း 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) သို့ သွားပြီး AI အက်ပ်များ ဖန်တီးရန် စတင်လိုက်ပါ!

## အရင်းအမြစ်များ

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID ဖြင့် keyless authentication](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry စာတမ်းများ](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK ပြောင်းရွှေ့ခြင်း](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 နှင့်အတူ တရားဝင် OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## ထပ်ဆောင်း အရင်းအမြစ်များ

- [VS Code ဒေါင်းလုပ်](https://code.visualstudio.com/Download)
- [Docker Desktop ကို ရယူပါ](https://www.docker.com/products/docker-desktop)
- [Dev Container ဖွဲ့စည်းမှု](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ပြောကြားချက်**
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းနေသော်လည်း၊ စက်ကိရိယာဘာသာပြန်ခြင်းများတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် လိုအပ်ပါသည်။ မူလစာတမ်းကို မူရင်းဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော အချက်အလက်အဖြစ် သတ်မှတ်သင့်သည်။ အရေးကြီးသည့် သတင်းအချက်အလက်များအတွက် ပရော်ဖက်ရှင်နယ် လူသားဘာသာပြန်သူဝန်ဆောင်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော နားလည်မှုကွာခြားမှုများ သို့မဟုတ် မမှန်ကန်သော အသုံးပြုမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မခံပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->