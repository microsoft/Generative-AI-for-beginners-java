# அஸ்யூர் AI Foundry உடன் அடிப்படை உரையாடல் - முற்றிலும் எடுத்துக்காட்டு

இந்த எடுத்துக்காட்டு ஒரு எளிய SPRING பூட் பயன்பாடு ஆகும், இது **Azure AI Foundry** மாதிரிக்கு **keyless authentication** (Microsoft Entra ID) மூலம் இணைகிறது மற்றும் உங்கள் அமைப்பை சோதிக்கிறது. இது Spring AI இன் `ChatClient` ஐ தனிச்செயலாக்கி, இது **அதிகாரபூர்வ OpenAI Java SDK** மற்றும் **Azure OpenAI v1** இறுதி புள்ளியால் ஆதரிக்கப்படுகிறது.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) இல் உள்ள பதிப்புகள் Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, மற்றும் dotenv-java **3.2.0** ஆகும். எடுத்துக்காட்டு `spring-ai-starter-model-openai` ஐ பயன்படுத்துகிறது மற்றும் தெளிவாக `openai-java` மற்றும் `azure-identity` ஐ அறிவிக்கிறது; Spring AI 2 பழைய Azure OpenAI ஸ்டார்டரைக் அகற்றியது.

## உள்ளடக்க அட்டவணை

- [முன் தேவைகள்](#முன்-தேவைகள்)
- [விரைவான தொடக்கம்](#விரைவான-தொடக்கம்)
- [அங்கீகாரம் எப்படி வேலை செய்கிறது](#அங்கீகாரம்-எப்படி-வேலை-செய்கிறது)
- [பயன்பாட்டை இயக்குவது](#பயன்பாட்டை-இயக்குதல்)
  - [Maven ஐ பயன்படுத்துதல்](#maven-ஐ-பயன்படுத்துதல்)
  - [VS குறியீட்டை பயன்படுத்துதல்](#vs-குறியீட்டை-பயன்படுத்துதல்)
  - [எதிர்பார்க்கப்பட்ட வெளியீடு](#எதிர்பார்க்கப்படும்-வெளியீடு)
- [கட்டமைப்பு குறிப்பு](#கட்டமைப்பு-குறிப்பு)
  - [சுற்றுப்புற மாறிகள்](#சுற்றுப்புற-மாறிகள்)
  - [Spring கட்டமைப்பு](#spring-கட்டமைப்பு)
- [சிக்கனைகள்](#சிக்கனைகள்)
  - [ஸாதாரண சிக்கல்கள்](#சாதாரண-சிக்கல்கள்)
  - [துணை போக்கு](#துணை-போக்கு)
- [அடுத்த படிகள்](#அடுத்த-படிகள்)
- [வளங்கள்](#வளங்கள்)

## முன் தேவைகள்

இந்த எடுத்துக்காட்டை இயக்குவதற்கு முன், கீழ்வரும் இருப்பதை உறுதிசெய்யவும்:

- `gpt-5.6-luna` அமைப்பு கொண்ட Azure AI Foundry வளம் - இது `azd up` மூலம் அல்லது கையேட்டுப் பயன்படுத்திக் கொள்க [Azure AI Foundry அமைப்பு வழிகாட்டி](../../getting-started-azure-openai.md)
- அந்த வளத்தில் **Cognitive Services OpenAI User** பண்புரிமையுடன் இருப்பது (Bicep வார்ப்புருக்கள் இதை உங்களுக்கு ஒதுக்கி விடும்)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli) இனி `az login` மூலம் உள்நுழைந்தீர்கள்
- Java 21+ மற்றும் Maven 3.9+

> **API விசை தேவையில்லை** — அங்கீகாரம் Microsoft Entra ID மூலம் keyless ஆக உள்ளது.

## விரைவான தொடக்கம்

```bash
# 1. திட்டத்துக்குள் செல்க
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. பூட்டியில்லா அங்கீகாரம் டோக்கன் பெற உள்நுழைக
az login

# 3. முடிவுநிலை சேவையகத்தை அமைக்கவும்
#    - நீங்கள் `azd up` ஓட்டியிருந்தால், .env உங்களுக்காக எழுதப்பட்டது (இதை தவிர்க்கவும்).
#    - இல்லையெனில் மாதிரியை நகலெடுக்கவும் மற்றும் AZURE_OPENAI_ENDPOINT ஐ அமைக்கவும்:
cp .env.example .env

# 4. பயன்பாட்டை இயக்கவும்
mvn spring-boot:run
```

## அங்கீகாரம் எப்படி வேலை செய்கிறது

இந்த எடுத்துக்காட்டு **Microsoft Entra ID** உடன் அங்கீகாரம் செய்கிறது — API விசை இல்லை.

பயன்பாடு அங்கீகாரத்தை தெளிவாக [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) இல் அமைக்கிறது:

1. `azureCredential()` `BearerTokenCredential` ஐ உருவாக்குகிறது `AuthenticationUtil.getBearerTokenSupplier` கொண்டு, `DefaultAzureCredential` மற்றும் `https://ai.azure.com/.default` பரப்புடன்.
2. `azureOpenAiClient()` `OpenAIClient` ஐ கட்டமைக்கிறது `OpenAIOkHttpClient.builder()` மூலம், வளத்தின் இறுதி புள்ளியை `/openai/v1` ஆக தீர்மானித்து, `.credential(...)` மூலம் பேயர் அங்கீகாரத்தை அளிக்கிறது.
3. `azureChatModel()` அந்த கிளையன்ட்டை Spring AI இன் `OpenAiChatModel` இற்கு வழங்குகிறது, இது பாடத்திட்டத்தின் `ChatClient` க்கு ஆதரவாக செயல்படுகிறது.

இந்த தெளிவான பீன்கள் உலகளாவிய `OPENAI_API_KEY` கொண்டு Azure அங்கீகாரத்தை மாற்றாமல் வைக்கின்றன. YAML இல் API விசையை மட்டும் விடுவித்தால் அது அங்கீகார அமைப்பு அல்ல. `DefaultAzureCredential` உங்கள் உள்ளூர் `az login` அமர்வைப் பயன்படுத்தலாம் அல்லது Azure இல் முகாமையாக்கப்பட்ட அடையாளத்தை; எந்த அடையாளம் தேர்ந்தெடுக்கப்பட்டாலும் மேலே பட்டியலிடப்பட்ட வள பண்புரிமையை கொண்டிருக்க வேண்டும்.

## பயன்பாட்டை இயக்குதல்

### Maven ஐ பயன்படுத்துதல்

```bash
mvn spring-boot:run
```

### VS குறியீட்டை பயன்படுத்துதல்

1. திட்டத்தை VS குறியீட்டில் திறக்கவும்
2. `F5` அழுத்தவும் அல்லது "ஏற்றவும் மற்றும் குறியீடு" பலகட்டை பயன்படுத்தவும்
3. "Spring Boot-BasicChatApplication" கட்டமைப்பை தேர்வு செய்யவும்

> **குறிப்பு**: பயன்பாடு தனது பணியின் அடைவிலிருந்து `.env` கோப்பைப் படிக்கிறது, VS குறியீட்டிலிருந்து தொடங்குமானாலும்.

### எதிர்பார்க்கப்படும் வெளியீடு

வெற்றிகரமாக ஓட்டிய பிறகு விளக்கமாக வெளிப்படும் (ஆரம்ப பதிவு உள்ளது; பதிலின் சொற்கள் மாறலாம்):

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

## கட்டமைப்பு குறிப்பு

### சுற்றுப்புற மாறிகள்

| மாறி | விளக்கம் | தேவை | உதாரணம் |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) இறுதி புள்ளி URL | ஆமாம் | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | உரையாடல் மாதிரி அமைப்பு பெயர் | இல்லை | `gpt-5.6-luna` (இயல்புநிலை) |

> **API விசை மாறி** இல்லை — அங்கீகாரம் keyless (Microsoft Entra ID `az login` மூலம்).

### Spring கட்டமைப்பு

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) அமைப்புகள் `spring.ai.openai` முன்னொட்டு மற்றும் சீரமைக்கப்பட்ட உரையாடல் பண்புகளைப் பயன்படுத்துகிறது (`options` பகுதி இல்லை):

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

`model` என்பது **Azure அமைப்பு பெயர்**. அங்கீகாரம் மேலே விவரிக்கப்பட்ட தெளிவான பீன்களில் இருந்து வருகிறது, `api-key` அமைப்பு இல்லை. பாடம் சிந்திப்பை நிறுத்தி பூர்த்தி குறியீடுகளை 500 ஆகக் குறைக்கிறது; `temperature` மற்றும் பழமைவாய்ந்த `max-tokens` அமைக்கப்படவில்லை.

Microsoft புதிய பயன்பாடுகளுக்காக [அதிகாரபூர்வ OpenAI SDK உடன் Azure OpenAI v1 மற்றும் Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) பரிந்துரைக்கிறது. உரையாடல் பூர்த்திகள் இந்த உள்ளடக்க நிகழ்விற்குப் பயன்படுத்துவதாக தொடர்கிறது. GPT-5.6 க்கானதும், Chat Completions இல் கருவிகள் உள்ள விண்ணப்பங்கள் `reasoning_effort` ஐ `none` ஆக அமைக்க வேண்டும்; கருதுகோள் மற்றும் கருவிகளுடன் சேர்ப்பதற்கு Responses ஐ பயன்படுத்தவும். பார்க்கவும் [கருதுகோள் மாதிரிகள் உடன் கருவி அழைப்பு](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## சிக்கனைகள்

### சாதாரண சிக்கல்கள்

<details>
<summary><strong>பிழை: 401 / "PermissionDenied" / டோக்கன் பிழைகள்</strong></summary>

- `az login` இயக்கவும் — keyless அங்கீகாரத்திற்கு செயலில் உள்நுழைவுப் பதிவு தேவை
- உங்கள் கணக்கு அந்த வளத்தில் **Cognitive Services OpenAI User** பண்புரிமையுடன் இருப்பதை உறுதிசெய்க
- பண்புரிமையை ஒதுக்கியபின் ஒரு நிமிடம் காத்திருங்கள் பரவுவது வரை
- நீங்கள் சரியான துணையாளர்/உறுப்பதாரராக உள்ளீர்களா என்பதை உறுதிசெய்க (`az account show`)
</details>

<details>
<summary><strong>பிழை: "இறுதி புள்ளி செல்லாது" / இணைப்பு பிழைகள்</strong></summary>

- `AZURE_OPENAI_ENDPOINT` முழு அடிப்படை URL ஆக இருக்க வேண்டும் (எ.கா., `https://your-resource.openai.azure.com/`)
- கேள்விக்குறி தாண்டியுள்ளதா என்று சரிபார்க்கவும்
- இறுதி புள்ளி உங்கள் ஒதுக்கப்பட்ட வள நிரலுக்கு பொருந்துகிறதா என்றும் சரிபார்க்கவும் (`azd env get-values`)
</details>

<details>
<summary><strong>பிழை: "அமைப்பு காணப்படவில்லை"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` Azure அமைப்பின் ஒரு பெயருடன் பொருந்துகிறது என்பதை சரிபார்க்கவும்
- மாதிரி வெற்றிகரமாக அமைக்கப்பட்டு செயல்படுகிறது என்பதை உறுதிசெய்க
- இயல்புநிலை அமைப்பு பெயர் `gpt-5.6-luna`
</details>

<details>
<summary><strong>பிழை: 429 / வீதக் கட்டுப்பாடு மீறப்பட்டது</strong></summary>

- இயல்புநிலை GPT-5.6 Luna அமைப்புக்கு உலகளாவிய ஸ்டாண்டர்டு திறன் 10: 10 கோரிக்கைகள்/நிமிடம் மற்றும் 10,000 குறியீடுகள்/நிமிடம்
- எடுத்துக்காட்டுகளை வரிசைப்படுத்தி ஓட்டவும் மற்றும் மறுஉரை முயற்சிக்கும்போது சேவையின் மறுஉரை இடைவெளியை காத்திருங்கள்
- இந்த அடிப்படை எடுத்துக்காட்டு தானாக SDK மறுஉரை நடவடிக்கையை முடக்குகிறது, ஆகவே தவறான கோரிக்கை நேரடியாக தகவல் தரப்படும்
</details>

<details>
<summary><strong>VS குறியீடு: சுற்றுப்புற மாறிகள் ஏறவில்லை</strong></summary>

- உங்கள் `.env` கோப்பு திட்டத்தின் அடிப்படைக் கோப்புறையில் இருக்கிறது என்பதை உறுதிசெய்க (போம்எக்ஸ்எம்எல் சம அளவில்)
- VS குறியீட்டின் ஒருங்கிணைக்கப்பட்ட டெர்மினலில் `mvn spring-boot:run` இயக்க முயற்சிக்கவும்
- VS குறியீடு Java விரிவாக்கம் சரியாக நிறுவப்பட்டுள்ளதா என சரிபார்க்கவும்
</details>

### துணை போக்கு

விரிவான பதிவு பயன்பாட்டை இயக்க [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) இல் உள்ள இந்த வரிகளை செயல்படுத்தவும்:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## அடுத்த படிகள்

**அமைப்பு நிறைவடைந்தது!** உங்கள் கற்றல் பயணத்தை தொடரவும்:

[அத்தியாயம் 3: கோர் உருவாக்கும் AI நுணுக்கங்கள்](../../../03-CoreGenerativeAITechniques/README.md)

## வளங்கள்

- [Spring AI 2 OpenAI Java SDK மாற்றம்](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [அதிகாரபூர்வ OpenAI Java SDK Azure OpenAI v1 உடன்](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID உடன் keyless அங்கீகாரம்](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry போர்டல்](https://ai.azure.com/)
- [Azure AI Foundry ஆவணம்](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**மறுப்பு**:
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) பயன்படுத்தி மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்திற்காக முயற்சி செய்துள்ளோம், ஆனால் தானாக செய்யப்படும் மொழிபெயர்ப்புகளில் பிழைகள் அல்லது தவறுகள் இருக்கலாம் என்பதை கவனத்தில் கொள்ளவும். அசல் ஆவணம் அதன் தாய்மொழியில் அதிகாரப்பூர்வ ஆதாரமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்நுட்பமான மனித மொழிபெயர்ப்பு பரிந்துரைக்கப்படுகிறது. இந்த மொழிபெயர்ப்பைப் பயன்படுத்துவதால் ஏற்படும் எந்த தவறான புரிதல்கள் அல்லது தவறான விளக்கத்திற்கும் நாங்கள் பொறுப்பில்வில்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->