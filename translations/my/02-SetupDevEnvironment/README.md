<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:33:44+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "my"
}
-->
# Java အတွက် Generative AI ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို စတင်တပ်ဆင်ခြင်း

> **အမြန်စတင်ရန်**: Cloud မှာ 2 မိနစ်အတွင်း ကုဒ်ရေးရန် - [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) ကို သွားပါ - ဒေသတွင်းမှာ တပ်ဆင်ရန် မလိုအပ်ပါ၊ GitHub Models ကို အသုံးပြုပါ!

> **Azure OpenAI ကို စိတ်ဝင်စားပါသလား?** [Azure OpenAI Setup Guide](getting-started-azure-openai.md) ကို ကြည့်ပါ၊ Azure OpenAI resource အသစ်တစ်ခု ဖန်တီးရန် လိုအပ်သော အဆင့်များပါဝင်သည်။

## သင်လေ့လာမည့်အရာများ

- AI အက်ပလီကေးရှင်းများအတွက် Java ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို တပ်ဆင်ခြင်း
- သင့်အကြိုက်ဆုံး ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို ရွေးချယ်ပြီး ဖွဲ့စည်းခြင်း (Codespaces ဖြင့် cloud-first, ဒေသတွင်း dev container, ဒေသတွင်း setup အပြည့်အစုံ)
- GitHub Models ကို ချိတ်ဆက်ပြီး သင့် setup ကို စမ်းသပ်ခြင်း

## အကြောင်းအရာများ

- [သင်လေ့လာမည့်အရာများ](../../../02-SetupDevEnvironment)
- [နိဒါန်း](../../../02-SetupDevEnvironment)
- [အဆင့် ၁: သင့်ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို တပ်ဆင်ပါ](../../../02-SetupDevEnvironment)
  - [ရွေးချယ်မှု A: GitHub Codespaces (အကြံပြုသည်)](../../../02-SetupDevEnvironment)
  - [ရွေးချယ်မှု B: ဒေသတွင်း Dev Container](../../../02-SetupDevEnvironment)
  - [ရွေးချယ်မှု C: သင့်ရှိပြီးသား ဒေသတွင်းတပ်ဆင်မှုကို အသုံးပြုပါ](../../../02-SetupDevEnvironment)
- [အဆင့် ၂: GitHub Personal Access Token ဖန်တီးပါ](../../../02-SetupDevEnvironment)
- [အဆင့် ၃: သင့် Setup ကို စမ်းသပ်ပါ](../../../02-SetupDevEnvironment)
- [ပြဿနာဖြေရှင်းခြင်း](../../../02-SetupDevEnvironment)
- [အကျဉ်းချုပ်](../../../02-SetupDevEnvironment)
- [နောက်ထပ်အဆင့်များ](../../../02-SetupDevEnvironment)

## နိဒါန်း

ဤအခန်းတွင် ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို စတင်တပ်ဆင်ရန် လမ်းညွှန်ပေးပါမည်။ **GitHub Models** ကို အဓိက ဥပမာအဖြစ် အသုံးပြုမည်ဖြစ်ပြီး၊ GitHub အကောင့်တစ်ခုသာ လိုအပ်ပြီး၊ အခမဲ့၊ အလွယ်တကူ စတင်နိုင်ပြီး၊ ခရက်ဒစ်ကတ်မလိုအပ်ပါ၊ မျိုးစုံသော မော်ဒယ်များကို စမ်းသပ်ရန် အခွင့်အရေးပေးပါသည်။

**ဒေသတွင်း setup မလိုအပ်ပါ!** GitHub Codespaces ကို အသုံးပြု၍ သင့် browser မှာ အပြည့်အစုံ ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို ချက်ချင်း စတင်ကုဒ်ရေးနိုင်ပါသည်။

<img src="./images/models.webp" alt="Screenshot: GitHub Models" width="50%">

ဤသင်တန်းအတွက် [**GitHub Models**](https://github.com/marketplace?type=models) ကို အသုံးပြုရန် အကြံပြုပါသည်၊ အကြောင်းမှာ:
- **အခမဲ့** စတင်နိုင်သည်
- **အလွယ်တကူ** GitHub အကောင့်တစ်ခုသာ လိုအပ်သည်
- **ခရက်ဒစ်ကတ်မလိုအပ်ပါ**
- **မျိုးစုံသော မော်ဒယ်များ** စမ်းသပ်နိုင်သည်

> **မှတ်ချက်**: ဤသင်တန်းတွင် အသုံးပြုသော GitHub Models တွင် အခမဲ့ အကန့်အသတ်များရှိသည်:
> - မိနစ် ၁၅ တစ်ခုလျှင် တောင်းဆိုမှုများ (နေ့စဉ် ၁၅၀)
> - တောင်းဆိုမှုတစ်ခုလျှင် ~8,000 စကားလုံးထည့်, ~4,000 စကားလုံးထုတ်
> - တစ်ချိန်တည်းတွင် ၅ တောင်းဆိုမှုများ
> 
> ထုတ်လုပ်မှုအတွက် Azure AI Foundry Models ကို သင့် Azure အကောင့်ဖြင့် upgrade လုပ်ပါ။ သင့်ကုဒ်ကို ပြောင်းလဲရန် မလိုအပ်ပါ။ [Azure AI Foundry documentation](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) ကို ကြည့်ပါ။

## အဆင့် ၁: သင့်ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို တပ်ဆင်ပါ

<a name="quick-start-cloud"></a>

Generative AI for Java သင်တန်းအတွက် လိုအပ်သော tools များနှင့် dependencies အားလုံးပါဝင်သော preconfigured development container ကို ဖန်တီးထားပါသည်။ သင့်အကြိုက်ဆုံး ဖွံ့ဖြိုးရေးနည်းလမ်းကို ရွေးချယ်ပါ:

### ပတ်ဝန်းကျင်တပ်ဆင်မှု ရွေးချယ်မှုများ:

#### ရွေးချယ်မှု A: GitHub Codespaces (အကြံပြုသည်)

**ဒေသတွင်း setup မလိုအပ်ဘဲ 2 မိနစ်အတွင်း ကုဒ်ရေးရန် စတင်ပါ!**

1. ဤ repository ကို သင့် GitHub အကောင့်သို့ Fork လုပ်ပါ
   > **မှတ်ချက်**: အခြေခံ config ကို ပြင်ဆင်လိုပါက [Dev Container Configuration](../../../.devcontainer/devcontainer.json) ကို ကြည့်ပါ
2. **Code** → **Codespaces** tab → **...** → **New with options...** ကို နှိပ်ပါ
3. Defaults ကို အသုံးပြုပါ – ဤသည်သည် သင်တန်းအတွက် ဖန်တီးထားသော **Generative AI Java Development Environment** custom devcontainer ကို ရွေးချယ်ပါမည်
4. **Create codespace** ကို နှိပ်ပါ
5. ပတ်ဝန်းကျင်အဆင်သင့်ဖြစ်ရန် ~2 မိနစ်စောင့်ပါ
6. [အဆင့် ၂: GitHub Token ဖန်တီးပါ](../../../02-SetupDevEnvironment) သို့ ဆက်လက်လုပ်ဆောင်ပါ

<img src="./images/codespaces.png" alt="Screenshot: Codespaces submenu" width="50%">

<img src="./images/image.png" alt="Screenshot: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Create codespace options" width="50%">

> **Codespaces ၏ အကျိုးကျေးဇူးများ**:
> - ဒေသတွင်းတပ်ဆင်မှု မလိုအပ်ပါ
> - Browser ပါရှိသော စက်တစ်ခုလုံးတွင် အလုပ်လုပ်နိုင်သည်
> - Tools နှင့် dependencies အားလုံးပါဝင်သော pre-configured ဖြစ်သည်
> - ပုဂ္ဂလိကအကောင့်များအတွက် တစ်လလျှင် အခမဲ့ 60 နာရီ
> - သင်တန်းသားများအတွက် ပတ်ဝန်းကျင်တစ်ခုတည်း

#### ရွေးချယ်မှု B: ဒေသတွင်း Dev Container

**Docker ဖြင့် ဒေသတွင်းဖွံ့ဖြိုးရေးကို နှစ်သက်သော developer များအတွက်**

1. ဤ repository ကို သင့်ဒေသတွင်းစက်သို့ Fork နှင့် Clone လုပ်ပါ
   > **မှတ်ချက်**: အခြေခံ config ကို ပြင်ဆင်လိုပါက [Dev Container Configuration](../../../.devcontainer/devcontainer.json) ကို ကြည့်ပါ
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) နှင့် [VS Code](https://code.visualstudio.com/) ကို တပ်ဆင်ပါ
3. VS Code တွင် [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ကို တပ်ဆင်ပါ
4. Repository folder ကို VS Code တွင် ဖွင့်ပါ
5. အကြောင်းကြားချက်ရရှိပါက **Reopen in Container** ကို နှိပ်ပါ (သို့မဟုတ် `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" ကို အသုံးပြုပါ)
6. Container ကို build လုပ်ပြီး စတင်ရန် စောင့်ပါ
7. [အဆင့် ၂: GitHub Token ဖန်တီးပါ](../../../02-SetupDevEnvironment) သို့ ဆက်လက်လုပ်ဆောင်ပါ

<img src="./images/devcontainer.png" alt="Screenshot: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev container build complete" width="50%">

#### ရွေးချယ်မှု C: သင့်ရှိပြီးသား ဒေသတွင်းတပ်ဆင်မှုကို အသုံးပြုပါ

**Java ပတ်ဝန်းကျင်များရှိပြီးသား developer များအတွက်**

လိုအပ်ချက်များ:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) သို့မဟုတ် သင့်နှစ်သက်သော IDE

အဆင့်များ:
1. ဤ repository ကို သင့်ဒေသတွင်းစက်သို့ Clone လုပ်ပါ
2. Project ကို သင့် IDE တွင် ဖွင့်ပါ
3. [အဆင့် ၂: GitHub Token ဖန်တီးပါ](../../../02-SetupDevEnvironment) သို့ ဆက်လက်လုပ်ဆောင်ပါ

> **Pro Tip**: သင့်စက် specs များနည်းပါက ဒေသတွင်း VS Code ကို အသုံးပြုလိုပါက GitHub Codespaces ကို အသုံးပြုပါ! Cloud-hosted Codespace ကို သင့်ဒေသတွင်း VS Code နှင့် ချိတ်ဆက်နိုင်သည်။

<img src="./images/image-2.png" alt="Screenshot: created local devcontainer instance" width="50%">

## အဆင့် ၂: GitHub Personal Access Token ဖန်တီးပါ

1. [GitHub Settings](https://github.com/settings/profile) သို့ သွားပြီး သင့် profile menu မှ **Settings** ကို ရွေးပါ။
2. ဘယ်ဘက် sidebar တွင် **Developer settings** ကို နှိပ်ပါ (အောက်ဆုံးတွင် ရှိလေ့ရှိသည်)။
3. **Personal access tokens** အောက်တွင် **Fine-grained tokens** ကို နှိပ်ပါ (သို့မဟုတ် ဤ [link](https://github.com/settings/personal-access-tokens) ကို တိုက်ရိုက်လိုက်ပါ)။
4. **Generate new token** ကို နှိပ်ပါ။
5. "Token name" အောက်တွင် ဖော်ပြချက်အမည် (ဥပမာ `GenAI-Java-Course-Token`) ထည့်ပါ။
6. Expiration date ကို သတ်မှတ်ပါ (လုံခြုံရေးအတွက် အကြံပြုချက်: 7 ရက်)။
7. "Resource owner" အောက်တွင် သင့် user account ကို ရွေးပါ။
8. "Repository access" အောက်တွင် GitHub Models ကို အသုံးပြုလိုသော repositories (သို့မဟုတ် "All repositories") ကို ရွေးပါ။
9. "Repository permissions" အောက်တွင် **Models** ကို ရှာပြီး **Read and write** သို့ ပြောင်းပါ။
10. **Generate token** ကို နှိပ်ပါ။
11. **သင့် token ကို ယခုကူးပြီး သိမ်းဆည်းပါ** – နောက်တစ်ကြိမ် မမြင်နိုင်တော့ပါ!

> **လုံခြုံရေးအကြံပြုချက်**: လိုအပ်သော scope အနည်းဆုံးနှင့် အလားတူ Expiration time အတိုဆုံးကို အသုံးပြုပါ။

## အဆင့် ၃: GitHub Models Example ဖြင့် သင့် Setup ကို စမ်းသပ်ပါ

သင့်ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်အဆင်သင့်ဖြစ်ပြီးနောက်၊ [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) တွင်ရှိသော ဥပမာအက်ပလီကေးရှင်းဖြင့် GitHub Models integration ကို စမ်းသပ်ပါ။

1. သင့်ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်တွင် terminal ကို ဖွင့်ပါ။
2. GitHub Models ဥပမာသို့ သွားပါ:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. သင့် GitHub token ကို environment variable အဖြစ် သတ်မှတ်ပါ:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. အက်ပလီကေးရှင်းကို run လုပ်ပါ:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

သင့် output သည် အောက်ပါအတိုင်း ဖြစ်သင့်သည်:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### ဤဥပမာကုဒ်ကို နားလည်ခြင်း

ပထမဦးဆုံး၊ သင် run လုပ်ခဲ့သောအရာကို နားလည်ပါ။ `examples/github-models` အောက်ရှိ ဤဥပမာသည် GitHub Models ကို ချိတ်ဆက်ရန် OpenAI Java SDK ကို အသုံးပြုသည်:

**ဤကုဒ်၏လုပ်ဆောင်မှုများ:**
- **GitHub Models** ကို သင့် personal access token ဖြင့် ချိတ်ဆက်သည်
- AI မော်ဒယ်သို့ "Say Hello World!" စာကို **ပို့သည်**
- AI ၏ အဖြေကို **လက်ခံပြီး ပြသသည်**
- သင့် setup မှန်ကန်စွာ အလုပ်လုပ်နေသည်ကို **အတည်ပြုသည်**

**အဓိက Dependency** (`pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Main Code** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## အကျဉ်းချုပ်

အိုကေ! သင်အခုအချိန်တွင် အားလုံးကို တပ်ဆင်ပြီးဖြစ်သည်:

- AI မော်ဒယ်များကို ချိတ်ဆက်ရန် လိုအပ်သော permissions ဖြင့် GitHub Personal Access Token တစ်ခု ဖန်တီးခဲ့သည်
- Java ဖွံ့ဖြိုးရေးပတ်ဝန်းကျင်ကို အလုပ်လုပ်စေခဲ့သည် (Codespaces, dev containers, ဒေသတွင်း setup များဖြင့်)
- OpenAI Java SDK ကို အသုံးပြု၍ GitHub Models ကို ချိတ်ဆက်ခဲ့သည်
- AI မော်ဒယ်များနှင့် ဆက်သွယ်သော ဥပမာတစ်ခုကို run လုပ်ပြီး အားလုံးအလုပ်လုပ်နေသည်ကို စမ်းသပ်ခဲ့သည်

## နောက်ထပ်အဆင့်များ

[Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md)

## ပြဿနာဖြေရှင်းခြင်း

ပြဿနာရှိပါသလား? အောက်ပါ ပြဿနာများနှင့် ဖြေရှင်းနည်းများကို ကြည့်ပါ:

- **Token အလုပ်မလုပ်ပါသလား?** 
  - Token အပြည့်အစုံကို အပိုနေရာများမပါဘဲ ကူးယူထားကြောင်း သေချာပါ
  - Token ကို environment variable အဖြစ် မှန်ကန်စွာ သတ်မှတ်ထားကြောင်း စစ်ဆေးပါ
  - Token ၏ permissions မှန်ကန်ကြောင်း (Models: Read and write) စစ်ဆေးပါ

- **Maven မတွေ့ပါသလား?** 
  - Dev containers/Codespaces ကို အသုံးပြုပါက Maven သည် pre-installed ဖြစ်သင့်သည်
  - ဒေသတွင်း setup အတွက် Java 21+ နှင့် Maven 3.9+ တပ်ဆင်ထားကြောင်း သေချာပါ
  - `mvn --version` ကို run လုပ်၍ တပ်ဆင်မှုကို စစ်ဆေးပါ

- **ချိတ်ဆက်မှုပြဿနာများ?** 
  - သင့်အင်တာနက်ချိတ်ဆက်မှုကို စစ်ဆေးပါ
  - GitHub သည် သင့် network မှ ချိတ်ဆက်နိုင်ကြောင်း သေချာပါ
  - GitHub Models endpoint ကို ပိတ်ထားသော firewall မရှိကြောင်း စစ်ဆေးပါ

- **Dev container မစတင်နိုင်ပါသလား?** 
  - Docker Desktop ကို run လုပ်ထားကြောင်း သေချာပါ (ဒေသတွင်းဖွံ့ဖြိုးရေးအတွက်)
  - Container ကို ပြန်လည် build လုပ်ပါ: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **အက်ပလီကေးရှင်း compile ပြဿနာများ?**
  - သင့် directory မှန်ကန်ကြောင်း သေချာပါ: `02-SetupDevEnvironment/examples/github-models`
  - Clean နှင့် rebuild လုပ်ပါ: `mvn clean compile`

> **အကူအညီလိုပါသလား?**: ပြဿနာရှိနေသေးပါက repository တွင် issue တစ်ခုဖွင့်ပြီး ကျွန်ုပ်တို့ကူညီပေးပါမည်။

**ဝက်ဘ်ဆိုက်မှတ်ချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးစားနေသော်လည်း၊ အလိုအလျောက်ဘာသာပြန်ခြင်းတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်ကြောင်း သတိပြုပါ။ မူလဘာသာစကားဖြင့် ရေးသားထားသော စာရွက်စာတမ်းကို အာဏာတည်သော ရင်းမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူ့ဘာသာပြန်ပညာရှင်များကို အသုံးပြုရန် အကြံပြုပါသည်။ ဤဘာသာပြန်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော နားလည်မှုမှားများ သို့မဟုတ် အဓိပ္ပာယ်မှားများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။ 