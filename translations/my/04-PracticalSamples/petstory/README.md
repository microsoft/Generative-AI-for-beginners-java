# ပစ္စည်းဇာတ်လမ်း ထုတ်လုပ်ရေး လမ်းညွန် စတင် လေ့လာသူများအတွက်

ပစ္စည်းဓာတ်ပုံတင်ပြီး၊ GPT-5.6 Luna ဖြင့် စိစစ်၍၊ ရလာသော ဖော်ပြချက်မှ ဇာတ်လမ်းတစ်ပုဒ် ထုတ်လုပ်ပါ။ မော်ဒယ် တောင်းဆိုမှု နှစ်ခုလုံးတွင် `reasoning_effort: none` ကို အသုံးပြုသည်။

| အစိတ်အပိုင်း | ဗားရှင်း |
| --- | --- |
| Java | 21 သို့မဟုတ် အထက် |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## အညွှန်းဇယား

- [လိုအပ်ချက်များ](#လိုအပ်ချက်များ)
- [ပရောဂျက် တည်ဆောက်ပုံ နားလည်ခြင်း](#ပရောဂျက်-တည်ဆောက်ပုံ-နားလည်ခြင်း)
- [အဓိကအစိတ်အပိုင်းများ ရှင်းပြချက်](#အဓိကအစိတ်အပိုင်းများ-ရှင်းပြချက်)
  - [1. အဓိက အက်ပလီကေးရှင်း](#1-အဓိက-အက်ပလီကေးရှင်း)
  - [2. ဝက်ဘ် ထိန်းချုပ်သူ](#2-ဝက်ဘ်-ထိန်းချုပ်သူ)
  - [3. ဇာတ်လမ်း ဝန်ဆောင်မှု](#3-ဇာတ်လမ်း-ဝန်ဆောင်မှု)
  - [4. ဝက်ဘ် ပုံစံများ](#4-ဝက်ဘ်-ပုံစံများ)
  - [5. ပြုပြင်ဆောင်ရွက်မှု](#5-ပြုပြင်ဆောင်ရွက်မှု)
- [အက်ပလီကေးရှင်း လည်ပတ်ခြင်း](#အက်ပလီကေးရှင်း-လည်ပတ်ခြင်း)
- [အော့ဖ်လိုင်း စမ်းသပ်မှုများ](#အော့ဖ်လိုင်း-စမ်းသပ်မှုများ)
- [ဘယ်လိုအတူတကွ လည်ပတ်သလဲ](#ဘယ်လောက်အဆင်ပြေစွာ-လည်ပတ်သလဲ)
- [AI ပေါင်းစည်းမှု နားလည်ခြင်း](#ai-ပေါင်းစည်းမှု-နားလည်ခြင်း)
- [နောက်ထပ် အဆင့်များ](#နောက်ထပ်-အဆင့်များ)

## လိုအပ်ချက်များ

စတင်하기 မတိုင်မီ အောက်ပါအရာများရှိနေပါစေ-
- Java 21 သို့အထက် တပ်ဆင်ထားပြီး
- နည်းပညာ ဘာသာရပ် ရယူဖို့ Maven
- Azure AI Foundry ထဲမှ GPT-5.6 Luna ဟုအမည်ပေးထားသော deployment, သို့မဟုတ် ဤ deployment ကိုညွှန်ပြသည့် `AZURE_OPENAI_DEPLOYMENT` override တစ်ခု။ provisioning အတွက် [ခန်းမ ၂](../../02-SetupDevEnvironment/getting-started-azure-openai.md) ကိုကြည့်ပြီး `az login` ဖြင့် keyless authentication ကို အသုံးပြုပါ။ Deployment သည် ပုံများအား input အဖြစ် ပေးပို့နိုင်ရမည်နှင့် `reasoning_effort: none` ကို ထောက်ပံ့ရမည်။
- Java, Spring Boot, နှင့် ဝက်ဘ် ဖွံ့ဖြိုးတိုးတက်မှု အခြေခံနားလည်မှုရှိထားရန်။

## ပရောဂျက် တည်ဆောက်ပုံ နားလည်ခြင်း

ပစ္စည်းဇာတ်လမ်းပရောဂျက်တွင် အရေးကြီးသော ဖိုင်များ အချို့ရှိသည်-

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## အဓိကအစိတ်အပိုင်းများ ရှင်းပြချက်

### 1. အဓိက အက်ပလီကေးရှင်း

**ဖိုင်:** `PetStoryApplication.java`

၎င်းသည် ကျွန်ုပ်တို့၏ Spring Boot အက်ပလီကေးရှင်း၏ ဝင်ပေါက်ဖြစ်သည် –

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**လုပ်ဆောင်ချက်များ:**
- `@SpringBootApplication` အတန်းအမှတ်အသားသည် auto-configuration နှင့် component scanning ကို ဖွင့်လှစ်သည်
- Port 8080 တွင် embedded web server (Tomcat) တစ်ခုစတင်သည်
- လိုအပ်သည့် Spring beans နှင့် ဝန်ဆောင်မှုများအားလုံးကို အလိုအလျောက် ဖန်တီးသည်

### 2. ဝက်ဘ် ထိန်းချုပ်သူ

**ဖိုင်:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | တောင်းဆိုမှု | အောင်မြင်သော ပြန်ကြားချက် |
| --- | --- | --- |
| `GET /` | အကြောင်းအရာ မပါ | CSRF token ပါသော HTML တင်သွင်းပုံစံ |
| `POST /analyze-image` | `multipart/form-data`, file field `image` | JSON: `{"description":"ကျယ်ပြန့်သော ပစ္စည်းတစ်ခု..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, field `description` | ဖော်ပြချက်နှင့် ထုတ်လုပ်ထားသော ဇာတ်လမ်းပါသော HTML ရလဒ်စာမျက်နှာ |

POST endpoint နှစ်ခုလုံးသည် `GET /` မှ ရရှိသော session cookie နှင့် CSRF token ကို လိုအပ်သည်။ Upload script သည် ပြင်ပ `_csrf` တန်ဖိုးကို `X-CSRF-TOKEN` header တွင် ပို့သည်။ ဇာတ်လမ်းတင်သွင်းမှုမှာ `_csrf` form field အဖြစ် ပို့သည်။ API clients များသည် Cookie ကို တောင်းဆိုမှုများ အကြားထိန်းသိမ်းထားရမည်။ ဤ endpoint များသည် JSON request မဟုတ်၊ form endpoints ဖြစ်သည်။

ဖော်ပြချက်များသည် မလွတ်လပ်ရပါနှင့် ၁၀၀၀ စာလုံးကျော် မဖြစ်ရပါ။ Controller သည် ဖော်ပြချက်ကို စစ်ထုတ်ပြီး `<`, `>`, နှစ်ဆဖြူ၊ `'`, နှင့် `&` ကို ဖယ်ရှား၍ ဝန်ဆောင်မှုသို့ ပေးပို့သည်။ ရလဒ်ပုံစံသည် `th:text` ဖြင့် မော်ဒယ် ထုတ်ဝေမှုကိုလည်း ကျွတ်ကာ ကာကွယ်သည်။

ပုံအတူမမှန်ခြင်းအတွက် HTTP 400 မှာ `error` field ပါထုတ်ပြန်သည်။ မော်ဒယ် မအောင်မြင်ခြင်းသည် HTTP 502 နှင့် `error` field ပါ သို့မဟုတ် `description` မပါသော ပြန်ကြားချက်ဖြင့် ပြန်သွားသည်။ ဖော်ပြချက် မမှန်ကန်ခြင်း သို့မဟုတ် မော်ဒယ် မအောင်မြင်ပြန်ကြားချက်များသည် ပြန်လည် ဦးတည် `/` နှင့် error ပြထားသည်။ လိုအပ်သော အချက်အလက်များ မပါခြင်းသည် HTTP 400 ဖြစ်ကာ CSRF token မရှိ သို့မဟုတ် မမှန်ကန်ခြင်းတွင် HTTP 403 ပြန်လည် ပေးသည်။ AI အောင်မြင်မှုအဖြစ် fallback ဖော်ပြချက် သို့မဟုတ် ဇာတ်လမ်း မတင်ပြပါ။

### 3. ဇာတ်လမ်း ဝန်ဆောင်မှု

**ဖိုင်:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

OpenAI Java SDK 4.63.1 သည် Azure AI Foundry ၏ OpenAI နှင့်ကိုက်ညီသော Chat Completions API ကို ခေါ်ယူသည်။ Azure Identity 1.18.6 သည် `DefaultAzureCredential` ဖြင့် Microsoft Entra bearer token ကို ပေးဆောင်ပြီး API key မလိုအပ်ပါ။

| လုပ်ဆောင်ချက် | input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | တင်သွင်းထားသော MIME type နှင့် base64 data URL အဖြစ် encode လုပ်ထားသော ပုံအချက်အလက် | 300 |
| `generateStory` | အသုံးပြုသူ စာသားမှာ ပစ္စည်းဖော်ပြချက် | 800 |

နှစ်ခုလုံးသည် `gpt-5.6-luna` ကို default deployment အဖြစ် သတ်မှတ်ထားပြီး `ReasoningEffort.NONE` (`reasoning_effort: none`) ကို အသုံးပြုပြီး စီစဉ်ထားသည်။ `temperature` သို့မဟုတ် legacy `max_tokens` parameter မပို့ပါ။

ပုံစစ်ခြင်းသည် JPEG, PNG, GIF, နှင့် WebP များအား လက်ခံကာ empty ပုံများနှင့် ၁၀MB ကျော် များကို ငြင်းဆန်သည်။ ဖော်ပြချက်သည် ၁၀၀၀ စာလုံးထက် မကျော်ရ။ ဇာတ်လမ်း request သည် မိသားစုနှင့် သင့်တော်သော ဇာတ်လမ်း အတိုကောက်တစ်ပုဒ် ဖြစ်သည်။ ရွေးချယ်မှုမရှိခြင်း သို့မဟုတ် မှန်ကန်သောမပါရှိခြင်းသည် အမှား ဖြစ်ပြေနိုင်ပြီး မော်ဒယ် မအောင်မြင်မှုများသည် server side သုံး စစ်ဆေးမှု အတွက် အကြောင်းရင်းကို ထိန်းသိမ်းထားသည်။ SDK client သည် အက်ပလီကေးရှင်း ရပ်တန့်စဉ် ပိတ်သိမ်းသည်။

### 4. ဝက်ဘ် ပုံစံများ

**ဖိုင်:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (တင်သွင်းဖောင်)

စာမျက်နှာတွင် ပုံရွေးစရာရှိပြီး ဖော်ပြချက်ရေးရာနေရာ မရှိပါ။ **Analyze Image** သည် ရွေးချယ်ထားသော ဓာတ်ပုံကို ကြည့်ရှုမည်၊ `/analyze-image` သို့ ဖော်ပြချက်ကို ပို့မည်။ အောင်မြင်သော ပြန်ကြားချက်သည် ဖော်ပြချက်ကို ပြသပြီး ဖော်ပြချက် field ကို ဖြည့်ပြီး **Generate Story** ကို ပြသသည်။ ခလုတ်သည် ရှိပြီးသားဖောင်ကို `/generate-story` သို့ ပို့သည်။

ဘရောက်ဇာတွင် မော်ဒယ်ဒေါင်းလုပ် သို့မဟုတ် CDN တိုက်ရိုက် မလိုအပ်ပါ။ ပုံစစ်ခြင်းသည် ဆာဗာတွင် Azure deployment ဖြင့် လည်ပတ်သည်။ အမှားများ မြင်သာပြီး ဇာတ်လမ်း ဖန်တီးခြင်းကို ဖော်ပြချက်မမှန်ဘဲ ခွင့်ပြုမထားပါ။ မတူညီသောဖိုင်ရွေးချယ်ခြင်းသည် ယခင် ခွဲခြားချက်ကို ဖယ်ရှားသည်။

**ဖိုင်:** `result.html` (ဇာတ်လမ်း ပြသမှု)

ထုတ်လုပ်ထားသောဇာတ်လမ်းကို ပြသသည် –

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**ပုံစံ အင်အားများ:**

1. **Thymeleaf ပေါင်းစည်းမှု**: dynamic အကြောင်းအရာအတွက် `th:` attribute များ အသုံးပြုသည်
2. **တုံ့ပြန်နိုင်မှု ဒီဇိုင်း**: မိုဘိုင်းနှင့် desktop အတွက် CSS ဖန်တီးမှု
3. **အမှား ကိုင်တွယ်မှု**: အသုံးပြုသူများအား စစ်ဆေးမှု အမှားပြသမှု
4. **တင်သွင်းမှု ကိုင်တွယ်မှု**: JavaScript ဖြင့် ဓာတ်ပုံ ကြည့်ရှု၊ CSRF ကာကွယ်ထားသော multipart တောင်းဆိုမှု ပို့ခြင်းနှင့် ပြန်ကြားချက် ဖော်ပြချက် စာသားပြသခြင်း

### 5. ပြုပြင်ဆောင်ရွက်မှု

**ဖိုင်:** `application.properties`

အက်ပလီကေးရှင်းအတွက် ပြုပြင်ဆောင်ရွက်မှု ဆက်တင်များ -

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**ပြုပြင်ဆောင်ရွက်မှု ရှင်းပြချက်:**

1. **ဖိုင် တင်သွင်းမှု**: ဖိုင်နှင့် multipart request တို့သည် နှစ်ခုလုံး 10MB အထိ ကန့်သတ်ထားသည်၊ multipart header များအတွက် နေရာ ချန်ထားရန် ဓာတ်ပုံများကို ထိုအောက်တွင် ထားပါ။
2. **မှတ်တမ်းတင်ခြင်း**: လည်ပတ်စဉ် မှတ်တမ်းတင်မှု ထိန်းချုပ်မှု။
3. **Azure AI Foundry**: ခေါင်းစဉ်နှင့် မော်ဒယ် deployment အသုံးပြုမှုသတ်မှတ်ချက် (keyless auth)
4. **လုံခြုံရေး**: CSRF ကာကွယ်မှု ဖြစ်နေဆဲ၊ မော်ဒယ် ထောက်လှမ်း စစ်ဆေးမှုများကို ဆာဗာတွင် မှတ်တမ်းတင်ပြီး controller တွင် မော်ဒယ် မအောင်မြင်မှု စာသားများ ပြသသည်

## အက်ပလီကေးရှင်း လည်ပတ်ခြင်း

### အဆင့် ၁: အသုံးပြုသူ မှတ်ပုံတင်ခြင်းနှင့် endpoint သတ်မှတ်ခြင်း

authentication သည် keyless (Microsoft Entra ID) ဖြစ်သောကြောင့် API key မလိုအပ်ပါ။ သင်၏ Foundry endpoint ကို သတ်မှတ်ရန် နှင့် အသုံးပြုရန် –

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**ဤအဆင့် လိုအပ်သော အကြောင်းပြချက်:**
- Azure AI Foundry သည် Microsoft Entra ID ကို inference တောင်းဆိုမှုများ အတည်ပြုရန် အသုံးပြုသည်
- Keyless auth ဆိုသည်မှာ သင့် source code သို့မဟုတ် ပတ်ဝန်းကျင်အတွင်းတွင် မှတ်စု မရှိပါ။
- သင်၏ အကောင့်တွင် **Cognitive Services OpenAI User** အခန်းကဏ္ဍ ပါရှိရမည်။

default deployment အမည်မှာ `gpt-5.6-luna` ဖြစ်သည်။ သင်၏ GPT-5.6 Luna deployment ၌ အမည်တစ်ခုခြားလည်း၊ အက်ပလီကေးရှင်း စတင်မတိုင် မီ terminal ထဲတွင် `AZURE_OPENAI_DEPLOYMENT` ကို သတ်မှတ်ပါ။ ပုံစစ်ခြင်းနှင့် ဇာတ်လမ်း ထုတ်လုပ်မှု နှစ်ခုလုံးတွင် ဤဆက်တင်ကို အသုံးပြုသည်။

### အဆင့် ၂: ဆောက်လုပ်၍ လည်ပတ်ခြင်း

project လမ်းကြောင်းသို့ သွားပါ -
```bash
cd 04-PracticalSamples/petstory
```

standalone executable JAR တည်ဆောက်ပြီး အော့ဖ်လိုင်း စမ်းသပ်မှုများအားလုံး လည်ပတ်ပါ-
```bash
mvn clean package
```

ဆာဗာ စတင်ပါ -
```bash
mvn spring-boot:run
```

အက်ပလီကေးရှင်းသည် `http://localhost:8080` တွင် စတင်လည်ပတ်ပါမည်။

အဲဒီအစားပြောင်းလဲ၍ free port တစ်ခုတွင် packaged JAR စတင်လည်ပတ်နိုင်သည်၊ ဥပမာ -

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

ဤကိစ္စအတွက် `http://localhost:8083/` ကို ဖွင့်ပါ။ `/analyze-image` နှင့် `/generate-story` လမ်းကြောင်းများသည် ရွေးချယ်ထားသော port တွင် ရရှိနိုင်သည်။

### အဆင့် ၃: အက်ပလီကေးရှင်း စမ်းသပ်ခြင်း

1. **ဖွင့်ပါ** `http://localhost:8080` ကို ဘရောက်ဇာတွင်
2. **ရွေးချယ်ပါ** ပုံမှန် မှတ်သားရှင်းလင်းသော ပစ္စည်းဓာတ်ပုံကို JPEG, PNG, GIF, သို့မဟုတ် WebP ပုံစံဖြင့် ၁၀MB အောက်တွင်
3. **နှိပ်ပါ** "Analyze Image" ကို နှင့် ပစ္စည်းဖော်ပြချက် စောင့်ဆိုင်းပါ
4. **နှိပ်ပါ** "Generate Story" ကို အောင်မြင်စွာ စစ်ဆေးမှုပြီးနောက်
5. **ကြည့်ပါ** ဇာတ်လမ်းကိုနှင့် ရလဒ် စာမျက်နှာရှိ လင့်ခ်အား အသုံးပြု၍ တင်သွင်းဖောင်သို့ ပြန်သွားပါ

ပုံမှန် ပုံမှတဆင့် ဇာတ်လမ်းဇာတ်ညွှန်း ထုတ်လုပ်မှုသည် ခလုတ်နှစ်ချက်အလိုက် မော်ဒယ်နှစ်ကြိမ်ကို ခေါ်သည်။ Live inference သည် သင်၏ deployment ၏ အရင်းအနှီး ကို စားသုံးပြီး သင်္ကေတနှုန်း ကန့်သတ်ထားသော deployment သုံးစဉ်မှာ smoke tests များကို အဆက်မပြတ် လုပ်ဆောင်ပါ။ စာမျက်နှာ ပိတျခြင်းသည် မော်ဒယ် ခေါ်ဆိုမှု မလုပ်ပါ။

## အော့ဖ်လိုင်း စမ်းသပ်မှုများ

sample ဖိုလ်ဒါမှ ဆောင်ရွက်ပါ-

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) သည် အဖြစ်အပျက် OpenAI SDK ၏ တောင်းဆိုမှုများကို loopback HTTP fixture ဖြင့် ဖမ်းယူသည်။ နှစ်ခုလုံးတောင်းဆိုမှု၏ deployment, `reasoning_effort: none`, token ကန့်သတ်ချက်များ, ပုံအချက်အလက် စစ်ဆေးမှု, ဖျက်ဆီးမှုမရှိစေရန် စစ်ဆေးသည်။

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) သည် MockMvc နှင့် မော်ကွက်ထားသော မော်ဒယ် ဝန်ဆောင်မှုဖြင့် Thymeleaf စာမျက်နှာများကို စစ်ဆေးသည်၊ တင်သွင်းမှု ပြားစီ, CSRF, စစ်ဆေးမှု, ထုတ်လွှတ်ရာ လုံခြုံမှုနည်းလမ်းများနှင့် မြင်သာသော ပျက်စီးမှုများကို ကြည့်သည်။ ဤစမ်းသပ်မှုများသည် Azure လက်မှတ် မလိုအပ်ဘဲ မလုပ်ဖူး Azure inference ကိုခြေရာခံ မလုပ်။ Maven သည် Surefire reports ကို `target/surefire-reports` အတွင်းတွင် ရေးဖြည့်သည်။

## ဘယ်လောက်အဆင်ပြေစွာ လည်ပတ်သလဲ

ပစ္စည်းဇာတ်လမ်း ထုတ်လုပ်ရာ အပြည့်အစုံ လည်ပတ်မှုဖြစ်ရပ် –

1. **ဓာတ်ပုံ ရွေးချယ်ခြင်း**: တင်သွင်းပုံစံတွင် ပစ္စည်းဓာတ်ပုံကို ရွေးချယ်သည်
2. **ဓာတ်ပုံ တင်သွင်းခြင်း**: "Analyze Image" သည် CSRF header ပါနေသည့် multipart POST ကို `/analyze-image` သို့ ပို့သည်
3. **ဓာတ်ပုံ စိစစ်ခြင်း**: `StoryService` သည် ပုံကို GPT-5.6 Luna သို့ reasoning ကို `none` အဖြစ် သတ်မှတ်ပို့သည်
4. **ဖော်ပြချက် ပြသခြင်း**: ဘရောက်ဇာသည် ပြန်လာသော ဖော်ပြချက်ကို ပြသ၍ ဖောင်တွင် သိမ်းဆည်းသည်
5. **ဇာတ်လမ်း တင်သွင်းခြင်း**: "Generate Story" သည် `description` နှင့် `_csrf` ကို `/generate-story` သို့ ပို့သည်
6. **ဇာတ်လမ်း ထုတ်လုပ်ခြင်း**: Controller သည် ဖော်ပြချက်ကို စစ်ဆေးပြီး ထို deployment တူညီဖြင့် reasoning သတ်မှတ်မှု `none` ဖြင့် ခေါ်သည်
7. **ပုံစံ အသုံးပြုမှု**: Thymeleaf သည် ဖော်ပြချက်နှင့် ဇာတ်လမ်းကို escape ပြုလုပ်ပြီး ရလဒ် စာမျက်နှာ၌ ပြသသည်

**အမှား ကိုင်တွယ်မှု လည်ပတ်မှု:**
မော်ဒယ် မအောင်မြင်လျှင် ဆာဗာတွင် အကြောင်းရင်းထောက်လှမ်းမှု ပြုလုပ်သည်။ ပုံစစ်ခြင်းသည် HTTP 502 ပြန်ပေးပြီး ဘရောက်ဇာတွင် အမှားကို "Generate Story" မပြသဘဲ ပြသသည်။ ဇာတ်လမ်း ထုတ်လုပ်မှုသည် ဖောင်သို့ အမှားစာပို့၍ ပြန်လည် ဦးတည်သည်။ တစ်ဘက်လမ်းမရှိဘဲ လောင်းမိုက်ထားသော ရလဒ်တစ်ခုကို မတပ်ဆင်ပါ။

## AI ပေါင်းစည်းမှု နားလည်ခြင်း

### Azure AI Foundry (keyless)
ဝန်ဆောင်မှုသည် သင့်ရင်းမြစ် `/openai/v1/` endpoint ဖြင့် SDK ကို ဖော်ညွှန်းသည်။ `DefaultAzureCredential` နှင့် `AuthenticationUtil.getBearerTokenSupplier` သည် Microsoft Entra tokens ကို `https://ai.azure.com/.default` အတွက် ပေးဆောင်သည်။ ဒေသခံ ဖွံ့ဖြိုးမှုတွင် Azure CLI အကောင့်ဖြင့် မှတ်ပုံတင်နိုင်ပြီး Azure-hosted အက်ပလီကေးရှင်းသည် managed identity နှင့် လိုအပ်သည့် resource ခွင့်ပြုချက်များ ကို အသုံးပြုနိုင်သည်။

### Prompt အင်ဂျင်နီယာ
ပုံစစ်ခြင်းသည် အတ္ထိပစ္စည်း ဆန်းစစ်ဖော်ပြချက် ကောက်နုတ်ရန် အတိုချုံးစာပိုဒ်တစ်ခု တောင်းဆိုပြီး မော်ဒယ်အား ပုံအတွင်း စာသားကို အညွှန်းမဟုတ်ဘဲ ဒေတာအနေနှင့် ကိုင်တွယ်ရန် ပြောသည်။ ဇာတ်လမ်းထုတ်လုပ်မှု သည် ပြန်လာသော ဖော်ပြချက်ကို သီးခြား မိသားစု သင့်တော်သော စာရေးခြင်း တောင်းဆိုမှုငယ်နှင့် အသုံးပြုသည်။ တစ်ဖက်လုံးမှာ reasoning သို့ မတည်ဆောက်ထားဘဲ temperature override မသတ်မှတ်ပါ။

### ပြန်ကြားချက် ကိုင်တွယ်ခြင်း
မျှဝေထားသော ပြန်ကြားချက် ကိုင်တွယ်သူသည် မရွေးချယ်မှု မရှိမှု နှင့် အားလုံး ဖျောက်ထား၊ သို့မဟုတ် ဝါးသော အကြောင်းအရာများကို ငြင်းကြားသည်။ မွန်ကန်သော အကြောင်းအရာကို စစ်ထုတ်၍ အပ်ဒိတ်လုပ်ပြီး တောင်းဆိုမှုပင်မအမှားများကို ထိမ်းသိမ်းသည်။ ပုံဖော်ပြချက်များသည် ၁၀၀၀ စာလုံးထက် မကျော်အောင် ကန့်သတ်ထားပြီး နောက်ထပ် ဇာတ်လမ်းဖောင်သို့ ထည့်သွင်းရန် ဖြစ်သည်။ မော်ဒယ် မအောင်မြင်မှု ကို စစ်ဆေးရန် စတင်အကြောင်းရင်းအား ထိန်းသိမ်းထားသော်လည်း အသုံးပြုသူ မမြင်ဖို့ ဖော်ပြမထားပါ။

## နောက်ထပ် အဆင့်များ

နမူနာပိုများအတွက် [ခန်းမ ၀၄: လက်တွေ့ နမူနာများ](../README.md) ကို ကြည့်ပါ

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ပြောကြားချက်**
ဤစာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှန်ကန်မှုအတွက် ကြိုးပမ်းနေသော်လည်း၊ စက်ကိရိယာဘာသာပြန်ခြင်းများတွင် အမှားများ သို့မဟုတ် မှားယွင်းချက်များ ပါဝင်နိုင်ကြောင်း သတိပြုပါရန် လိုအပ်ပါသည်။ မူလစာတမ်းကို မူရင်းဘာသာဖြင့်သာ ယုံကြည်စိတ်ချရသော အချက်အလက်အဖြစ် သတ်မှတ်သင့်သည်။ အရေးကြီးသည့် သတင်းအချက်အလက်များအတွက် ပရော်ဖက်ရှင်နယ် လူသားဘာသာပြန်သူဝန်ဆောင်မှုကို အကြံပြုပါသည်။ ဤဘာသာပြန်ချက်ကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော နားလည်မှုကွာခြားမှုများ သို့မဟုတ် မမှန်ကန်သော အသုံးပြုမှုများအတွက် ကျွန်ုပ်တို့ တာဝန်မခံပါ။
<!-- CO-OP TRANSLATOR DISCLAIMER END -->