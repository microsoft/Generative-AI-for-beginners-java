# Azure AI Foundry နှင့် မူလစမ်းသပ်ချက် Chat အခြေခံ - အဆုံးမှအဆုံး ဥပမာ

ဤဥပမာသည် **keyless authentication** (Microsoft Entra ID) ကို အသုံးပြု၍ **Azure AI Foundry** ပုံစံနှင့် ဆက်သွယ်သည့် ရိုးရှင်းသည့် Spring Boot ကွန်ရက်လ်တစ်ခုဖြစ်ပြီး သင့်ဆက်တင်ကို စမ်းသပ်သည်။ ၎င်းသည် Spring AI ၏ `ChatClient` ကိုသိမ်းဆည်းပြီး **အတည်ပြု OpenAI Java SDK** နှင့် **Azure OpenAI v1** အဆုံးအမှတ် ပံ့ပိုးထားသည်။

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) တွင် အသုံးပြုထားသော အကွဲအပြားများမှာ Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, နှင့် dotenv-java **3.2.0** ဖြစ်သည်။ ဤဥပမာသည် `spring-ai-starter-model-openai` ကို အသုံးပြုသည့်အပြင် `openai-java` နှင့် `azure-identity` ကို ထုတ်ဖော်ကြေညာထားပြီး Spring AI 2 သည်သင်္ကေတဟောင်း Azure OpenAI starter ကို ဖယ်ရှားထားသည်။

## အကြောင်းအရာဇယား

- [လိုအပ်ချက်များ](#လိုအပ်ချက်များ)
- [လျင်မြန်စွာစတင်ခြင်း](#လျင်မြန်စွာစတင်ခြင်း)
- [အတည်ပြုခြင်းသည် မည်သို့ လုပ်ဆောင်သည်](#အတည်ပြုခြင်းသည်-မည်သို့-လုပ်ဆောင်သည်)
- [အပလီကေးရှင်းကို စမ်းသပ်ခြင်း](#အပလီကေးရှင်းကို-စမ်းသပ်ခြင်း)
  - [Maven ဖြင့် အသုံးပြုခြင်း](#maven-ကို-အသုံးပြုခြင်း)
  - [VS Code ဖြင့် အသုံးပြုခြင်း](#vs-code-ကို-အသုံးပြုခြင်း)
  - [မျှော်မှန်းထားသော ထွက်ရှိမှု](#မျှော်မှန်းထားသော-ထွက်ရှိမှု)
- [ဖွဲ့စည်းမှုကို ရည်ညွှန်းခြင်း](#ဖွဲ့စည်းမှုကို-ရည်ညွှန်းခြင်း)
  - [ပတ်ဝန်းကျင်အပြောင်းအလဲများ](#ပတ်ဝန်းကျင်အပြောင်းအလဲများ)
  - [Spring ဖွဲ့စည်းမှု](#spring-ဖွဲ့စည်းမှု)
- [ပြဿနာများဖြေရှင်းခြင်း](#ပြဿနာများဖြေရှင်းခြင်း)
  - [ရိုးရာပြဿနာများ](#ရိုးရာပြဿနာများ)
  - [ဗမာချက်မုဒ်](#ဗမာချက်မုဒ်)
- [နောက်တစ်ဆင့်များ](#နောက်တစ်ဆင့်များ)
- [အရင်းအမြစ်များ](#အရင်းအမြစ်များ)

## လိုအပ်ချက်များ

ဤဥပမာကို အလည်အပတ်တင်ရန် မတိုင်မီ အောက်ပါအချက်များကို သေချာစစ်ဆေးပါ။

- `gpt-5.6-luna` deployment ရှိသော Azure AI Foundry အရင်းအမြစ် - `azd up` ဖြင့် သို့မဟုတ် [Azure AI Foundry setup guide](../../getting-started-azure-openai.md) တိုက်ရိုက် provision လုပ်ပေးပါ
- အဆိုပါ အရင်းအမြစ်တွင် **Cognitive Services OpenAI User** စတိုက်ကွာ ဖြစ်ရမည် (Bicep templates သည် ဤအခန်းကို မှတ်ပုံတင်ပေးသည်)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli) ကို `az login` ဖြင့် အကောင့်အုံဆိုင်ရာ သွင်းထားရမည်
- Java 21+ နှင့် Maven 3.9+ ကို တပ်ဆင်ထားရမည်

> **API key မလိုအပ်ပါ** — authentication သည် Microsoft Entra ID ဖြင့် keyless ဖြစ်သည်။

## လျင်မြန်စွာစတင်ခြင်း

```bash
# ၁။ ပရောဂျက်သို့ ရောက်ပါ
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# ၂။ keyless အတည်ပြုမှုအတွက် token ရယူနိုင်ရန် ဝင်ပါ
az login

# ၃။ endpoint ကို ပြင်ဆင်ပါ
#    - သင် `azd up` မောင်းထားခဲ့လျှင် .env ကို အလိုအလျောက်ရေးသားပေးထားသည် (ဤပိုင်းကို ကျော်လိုက်ပါ)
#    - မဟုတ်လျှင် ဆွဲထားသော template ကို ကူးယူပြီး AZURE_OPENAI_ENDPOINT ကို သတ်မှတ်ပါ
cp .env.example .env

# ၄။ အက်ပလီကေးရှင်းကို အသုံးပြုပါ
mvn spring-boot:run
```

## အတည်ပြုခြင်းသည် မည်သို့ လုပ်ဆောင်သည်

ဤဥပမာသည် **Microsoft Entra ID** ဖြင့် authentication လုပ်သည် - API key မရှိပါ။

အပလီကေးရှင်းသည် [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) တွင် authentication ကို ထိပ်တိုက်ပြုလုပ်သည်။

1. `azureCredential()` သည် `DefaultAzureCredential` နှင့် `https://ai.azure.com/.default` scope ကို အသုံးပြု၍ `AuthenticationUtil.getBearerTokenSupplier` ဖြင့် `BearerTokenCredential` တည်ဆောက်သည်။
2. `azureOpenAiClient()` သည် `OpenAIOkHttpClient.builder()` ဖြင့် `OpenAIClient` ဖန်တီးပြီး resource အဆုံးအမှတ် `/openai/v1` သို့ သတ်မှတ်ပြီး bearer credential ကို `.credential(...)` ဖြင့်ပေးသည်။
3. `azureChatModel()` သည် ၎င်း client ကို Spring AI ၏ `OpenAiChatModel` သို့ ပေးပြီး ဤသင်ခန်းစာ၏ `ChatClient` ကို ထောက်ပံ့သည်။

ဤ explicit beans များက ခရီးသွားလမ်းညွှန် `OPENAI_API_KEY` ကို Azure authentication အစားတောင် မပြောင်းလဲစေပါ။ YAML မှ API key မပါခြင်း သာ authentication setup မဟုတ်ပါ။ `DefaultAzureCredential` သည် သင်၏ ဒေသတြင်း `az login` အစမ်းကပ်မှု သို့မဟုတ် Azure ၏ managed identity ကို အသုံးပြုနိုင်သည်။ မည်သော identity ကို ရွေးချယ်မဆို အဆိုပါ အရင်းအမြစ် အခန်းကဏ္ဍကို ရှိရပါမည်။

## အပလီကေးရှင်းကို စမ်းသပ်ခြင်း

### Maven ကို အသုံးပြုခြင်း

```bash
mvn spring-boot:run
```

### VS Code ကို အသုံးပြုခြင်း

1. VS Code မှာ ပရောဂျက်ကိုဖွင့်ပါ
2. `F5` ကို နှိပ်ပါ သို့မဟုတ် "Run and Debug" panel ကို သုံးပါ
3. "Spring Boot-BasicChatApplication" configuration ကို ရွေးပါ

> **မှတ်ချက်**: အပလီကေးရှင်းသည် ၎င်း၏ လုပ်ငန်းတာဝန် ဒေါ့ကက်ထဲမှ `.env` ကို ဖတ်ပါသည်၊ VS Code မှစတင်သုံးသည့်အခါပါ။

### မျှော်မှန်းထားသော ထွက်ရှိမှု

အောင်မြင်စွာ စတင်မှုအပြီး ပုံမှန်ထွက်ရှိမှု (စတင်သော လော့ဂ်များ မပါဝင်; တုံ့ပြန်ချက် စကားလုံးများ ကွဲပြားနိုင်သည်) -

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## ဖွဲ့စည်းမှုကို ရည်ညွှန်းခြင်း

### ပတ်ဝန်းကျင်အပြောင်းအလဲများ

| ပြောင်းလဲနိုင်သော ဧရိယာ | ဖော်ပြချက် | လိုအပ်/မလိုအပ် | ဥပမာ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) အဆုံးအမှတ် URL | လိုအပ်သည် | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Chat model deployment နာမည် | မလိုအပ်ပါ | `gpt-5.6-luna` (ပုံမှန်) |

> API key variable မရှိပါ - authentication သည် keyless ဖြစ်သည် (Microsoft Entra ID ကို `az login` နှင့်ဖြင့်)။

### Spring ဖွဲ့စည်းမှု

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ၏ စတင်မှုများသည် `spring.ai.openai` prefix နှင့် တိုတောင်းစွာ chat properties များ (options block မပါ) ကို အသုံးပြုသည်။

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` သည် **Azure deployment နာမည်** ဖြစ်သည်။ authentication သည် အထက်တွင်ဖော်ပြထားသော explicit beans မှ ရရှိပြီး `api-key` သတ်မှတ်ချက်မျိုး မရှိပါ။ သင်ခန်းစာသည် reasoning ကို ပိတ်သိမ်း၍ စကားလုံးတွက်ချက်မှုကို ၅၀၀ အထိ ကန့်သတ်ထားသည်။ `temperature` နှင့် အဟောင်း `max-tokens` မသတ်မှတ်ထားပါ။

Microsoft သည် [အသစ်သော အပလီကေးရှင်းများအတွက် Azure OpenAI v1 နှင့် Responses API ကို ပံ့ပိုးတား OpenAI SDK သုံးရန်](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) ကို အကြံပြုသည်။ Chat Completions သည် ၎င်း ရှိသော စာတိုက်ပို့မှုအခြေခံ သင်ခန်းစာအတွက် ဆက်မခံထားပါ။ GPT-5.6 အတွက် Chat Completions တွင် tools ပါဝင်သော တောင်းဆိုမှုများသည် `reasoning_effort` ကို `none` သတ်မှတ်ရမည်ဖြစ်ပြီး reasoning နှင့် tools များကို ပေါင်းစပ်ရန် Responses ကို သုံးရန် လိုအပ်သည်။ [reasoning models နှင့် tool calling](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models) ကို ကြည့်ပါ။

## ပြဿနာများဖြေရှင်းခြင်း

### ရိုးရာပြဿနာများ

<details>
<summary><strong>အမှား: 401 / "PermissionDenied" / token အမှားများ</strong></summary>

- `az login` ကို လုပ်ပါ - keyless authentication သည် token ရရှိရန် သက်တမ်းရှိသော sign-in လိုအပ်သည်
- သင့်အကောင့်တွင် အဆိုပါ အရင်းအမြစ်အပေါ် **Cognitive Services OpenAI User** အခန်းကဏ္ဍ ရှိရန် သေချာပါစေ
- တတ်နိုင်ပါက စာရင်းပေးသွင်းပြီး စောင့်ဆိုင်းရန် တစ်မိနစ် ခန့်
- သင့်သည် တိကျသော tenant / subscription တွင် ရှိကြောင်း သေချာပါ ( `az account show` ကိရိယာဖြင့်)
</details>

<details>
<summary><strong>အမှား: "The endpoint is not valid" / ချိတ်ဆက်မှု အမှားများ</strong></summary>

- `AZURE_OPENAI_ENDPOINT` မှာ အပြည့်အစုံ base URL ဖြစ်ရန် သေချာပါ (ဥပမာ- `https://your-resource.openai.azure.com/`)
- လိုက်နာရမည့် trailing slash ကွပ်တည်းမှုကို စစ်ဆေးပါ
- အဆုံးအမှတ်သည် သင့် provision လုပ်ထားသော အရင်းအမြစ်နှင့် ကိုက်ညီမှုရှိပြီးစစ်ဆေးပါ (`azd env get-values` သုံး၍)
</details>

<details>
<summary><strong>အမှား: "The deployment was not found"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` သည် Azure တွင် deployment နာမည်နှင့် ကိုက်ညီသေချာစေပါ
- မော်ဒယ်သည် အောင်မြင်စွာ တပ်ဆင်ပြီး လည်ပတ်နေသည်ဟု သေချာစေပါ
- ပုံမှန် deployment နာမည်မှာ `gpt-5.6-luna` ဖြစ်သည်
</details>

<details>
<summary><strong>အမှား: 429 / အမြန်နှုန်း ကန့်သတ်ချက် ကျော်လွန်ခြင်း</strong></summary>

- ပုံမှန် GPT-5.6 Luna deployment သည် Global Standard capacity 10: မိနစ်စဉ် ၁၀ တောင်းဆိုမှု နှင့် မိနစ်စဉ် ၁၀,၀၀၀ tokens ကို ပံ့ပိုးသည်
- ဥပမာများကို တစ်စိတ်တစ်ပိုင်း ဖြတ်သန်းပြီး ဝန်ဆောင်မှုပြန်တောင်းမှုကာလ စောင့်ပါ
- ဤအခြေခံဥပမာသည် အလိုအလျောက် SDK ပြန်တောင်းမှုများ ပိတ်ထားသဖြင့် မအောင်မြင်သော တောင်းဆိုမှုကို တိုက်ရိုက် ဆောင်ရွက်သည်
</details>

<details>
<summary><strong>VS Code: ပတ်ဝန်းကျင်အပြောင်းအလဲ မတင်ထားမှု</strong></summary>

- သင့် `.env` ဖိုင်သည် ပရောဂျက် မူလ ဒါရဲတာရီတွင် ရှိကြောင်း သေချာစေပါ (`pom.xml` နှင့်တန်းတူ)
- VS Code ၏ အတွင်းရေးကောက် terminal တွင် `mvn spring-boot:run` ကို စမ်းသပ်ပါ
- VS Code Java extension သည် မှန်ကန်စွာ တပ်ဆင်ထားကြောင်း သေချာစေပါ
</details>

### ဗမာချက်မုဒ်

အသေးစိတ် logging ကို ဖွင့်ရန် [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) တွင် ဤလိုင်းများကို မှတ်ချက်ဖယ်ပါ။

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## နောက်တစ်ဆင့်များ

**တပ်ဆင်မှု ပြီးစီးပါပြီ!** သင်ယူမှု ခရီးကို ဆက်လုပ်ဆောင်ပါ-

[အခန်း ၃: Core Generative AI နည်းလမ်းများ](../../../03-CoreGenerativeAITechniques/README.md)

## အရင်းအမြစ်များ

- [Spring AI 2 OpenAI Java SDK ပြောင်းလဲမှု](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 နှင့် သက်မှတ် OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID ဖြင့် keyless authentication](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry ပေါ်တယ်](https://ai.azure.com/)
- [Azure AI Foundry စာရွက်စာတမ်း](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ပြောကြားချက်**
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းနေသော်လည်း၊ စက်ကိရိယာဘာသာပြန်ခြင်းများတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် လိုအပ်ပါသည်။ မူလစာတမ်းကို မူရင်းဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော အချက်အလက်အဖြစ် သတ်မှတ်သင့်သည်။ အရေးကြီးသည့် သတင်းအချက်အလက်များအတွက် ပရော်ဖက်ရှင်နယ် လူသားဘာသာပြန်သူဝန်ဆောင်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော နားလည်မှုကွာခြားမှုများ သို့မဟုတ် မမှန်ကန်သော အသုံးပြုမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မခံပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->