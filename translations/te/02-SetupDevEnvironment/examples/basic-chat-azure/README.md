# ఆస్యూర్ ఎ ఐ ఫౌండ్రీ తో ప్రాథమిక చాట్ - పూర్తిస్థాయి ఉదాహరణ

ఈ ఉదాహరణ ఒక సులభమైన స్పிரింగ్ బూట్ అప్లికేషన్, ఇది **ఆస్యూర్ ఎ ఐ ఫౌండ్రీ** మోడల్ కు **కీఅల్పైన గుర్తింపు లేకుండా** (Microsoft Entra ID) అనుసంధానిస్తుంది మరియు మీ సెట్ అప్ ను పరీక్షిస్తుంది. ఇది స్ప్రింగ్ ఎ ఐ యొక్క `ChatClient` ను ఉపయోగిస్తుంది, ఇది **అధికారిక OpenAI జావా SDK** మరియు **ఆస్యూర్ OpenAI v1** ఎండ్‌పాయింట్ పైన ఆధారపడి ఉంటుంది.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) లో సంస్కరణలు స్ప్రింగ్ బూట్ **4.1.1**, స్ప్రింగ్ ఎ ఐ **2.0.1**, OpenAI జావా **4.63.1**, ఆస్యూర్ ఐడెంటిటీ **1.18.6**, మరియు dotenv-java **3.2.0**. ఈ ఉదాహరణ `spring-ai-starter-model-openai` ఉపయోగిస్తుంది మరియు స్పష్టంగా `openai-java` మరియు `azure-identity` ను ప్రకటిస్తుంది; స్ప్రింగ్ ఎ ఐ 2 పాత ఆస్యూర్ OpenAI స్టార్టర్ ను తొలగించింది.

## విషయ సూచీ

- [అవసరాలు](#అవసరాలు)
- [త్వరిత ప్రారంభం](#త్వరిత-ప్రారంభం)
- [గుర్తింపు ఎలా పనిచేస్తుంది](#గుర్తింపు-ఎలా-పనిచేస్తుంది)
- [అప్లికేషన్ ని నడపడం](#అప్లికేషన్-నడపడం)
  - [మావెన్ ఉపయోగించి](#మావెన్-ఉపయోగించి)
  - [VS కోడ్ ఉపయోగించి](#vs-కోడ్-ఉపయోగించి)
  - [అంచనా ఫలితం](#అంచనా-ఫలితం)
- [సంస్థాపన సూచిక](#సంస్థాపన-సూచిక)
  - [పర్యావరణ చారాలు](#పర్యావరణ-చారాలు)
  - [స్ప్రింగ్ సెట్టింగ్‌లు](#స్ప్రింగ్-సెట్టింగ్‌లు)
- [గొడవలు పరిష్కారం](#గొడవలు-పరిష్కారం)
  - [సాధారణ సమస్యలు](#సాధారణ-సమస్యలు)
  - [డీబగ్ మోడ్](#డీబగ్-మోడ్)
- [తరువాతి దశలు](#తరువాతి-దశలు)
- [వనరులు](#వనరులు)

## అవసరాలు

ఈ ఉదాహరణ నడపడానికి ముందు, మీరు ఉంటే ధృవీకరించండి:

- `gpt-5.6-luna` డిప్లాయ్‌మెంట్‌తో ఆస్యూర్ ఎ ఐ ఫౌండ్రీ వనరు - దీన్ని `azd up` లేదా [ఆస్యూర్ ఎ ఐ ఫౌండ్రీ సెటప్ గైడ్](../../getting-started-azure-openai.md) ద్వారా మానవీయంగా ప్రావిజన్ చేయండి
- ఆ వనరుపై **కాగ్నిటివ్ సర్వీసెస్ OpenAI యూజర్** పాత్ర (Bicep టెంప్లేట్లు మీకోసమే ఇస్తాయి)
- [ఆస్యూర్ CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), దీనిలో `az login` ద్వారా సైన్ ఇన్ అయ్యి ఉండాలి
- జావా 21+ మరియు మావెన్ 3.9+

> **ఏ API కీ అవసరం లేదు** — గుర్తింపు Microsoft Entra ID ద్వారా కీఅల్పైనగా జరుగుతుంది.

## త్వరిత ప్రారంభం

```bash
# 1. ప్రాజెక్ట్‌కు నావిగేట్ చేయండి
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. కీలెస్ ఆథ్ టోకెన్ పొందడానికి సైన్ ఇన్ చేయండి
az login

# 3. ఎండ్పాయింట్‌ను కాన్ఫిగర్ చేయండి
#    - మీరు `azd up` నడిపితే, .env మీకు రాయబడింది (దీనిని దాటవేయండి).
#    - లేకపోతే టెంప్లేట్‌ను కాపీ చేసి AZURE_OPENAI_ENDPOINT ను సెట్ చేయండి:
cp .env.example .env

# 4. యాప్ రన్ చేయండి
mvn spring-boot:run
```

## గుర్తింపు ఎలా పనిచేస్తుంది

ఈ ఉదాహరణ **Microsoft Entra ID** తో గుర్తింపును నిర్వహిస్తుంది — ఏ API కీ లేదు.

అప్లికేషన్ [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) లో స్పష్టంగా గుర్తింపును కాన్ఫిగర్ చేస్తుంది:

1. `azureCredential()` `AuthenticationUtil.getBearerTokenSupplier` ఉపయోగించి `DefaultAzureCredential` మరియు `https://ai.azure.com/.default` స్కోప్ తో `BearerTokenCredential` ను సృష్టిస్తుంది.
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()`తో `OpenAIClient` నిర్మించి, రిసోర్స్ ఎండ్‌పాయింట్ ను `/openai/v1` కు విప్పుతుంది, మరియు `.credential(...)` తో బేరర్ క్రెడెన్షియల్ అందిస్తుంది.
3. `azureChatModel()` ఆ క్లయింట్ ను స్ప్రింగ్ ఎ ఐ `OpenAiChatModel` కు అందిస్తుంది, ఇది పాఠంలో ఉపయోగించిన `ChatClient` ను మద్దతు ఇస్తుంది.

ఈ స్పష్టమైన బీన్లు ఒక గ్లోబల్ `OPENAI_API_KEY`ని ఆస్యూర్ గుర్తింపు పై ఎత్తివేయకుండా ఉంచుతాయి. YAML నుండి API కీ వదలగొట్టటం మాత్రమే గుర్తింపు సెటప్ కాదు. `DefaultAzureCredential` మీ స్థానిక `az login` సెషన్ లేదా ఆస్యూర్ లో ఒక నిర్వహించబడుతున్న గుర్తింపును ఉపయోగించవచ్చు; ఎంచుకున్న గుర్తింపు పైపైన పేర్కొన్న వనరు పాత్ర ఉంటాలి.

## అప్లికేషన్ నడపడం

### మావెన్ ఉపయోగించి

```bash
mvn spring-boot:run
```

### VS కోడ్ ఉపయోగించి

1. ప్రాజెక్ట్ ను VS కోడ్ లో తెరవండి
2. `F5` నొక్కండి లేదా "రన్ మరియు డీబగ్" ప్యానెల్ ఉపయోగించండి
3. "Spring Boot-BasicChatApplication" కాన్ఫిగరేషన్ ను ఎంచుకోండి

> **గమనిక**: అప్లికేషన్ తన వర్కింగ్ డైరెక్టరీ నుండి `.env` ఫైల్ ను లోడ్ చేస్తుంది, దీనిలో VS కోడ్ నుండి ప్రారంభించినప్పుడు కూడా అలాగే ఉంటుంది.

### అంచనా ఫలితం

విజయవంతమైన రన్ తర్వాత ప్రదర్శనాత్మక అవుట్‌పుట్ (స్టార్టప్ లాగ్స్ వదిలివేయబడినవి; ప్రతిస్పందన మాటలు మారవచ్చు):

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

## సంస్థాపన సూచిక

### పర్యావరణ చారాలు

| వేరియబుల్ | వివరణ | అవసరం | ఉదాహరణ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | ఫౌండ్రీ (ఆస్యూర్ OpenAI) ఎండ్‌పాయింట్ URL | అవును | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | చాట్ మోడల్ డ్రిప్లాయ్‌మెంట్ పేరు | కాదు | `gpt-5.6-luna` (డిఫాల్ట్) |

> **ఏ API కీ వేరియబుల్ లేదు** — గుర్తింపు కీఅల్పైన (Microsoft Entra ID ద్వారా `az login`).

### స్ప్రింగ్ సెట్టింగ్‌లు

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) సెట్టింగ్‌లు `spring.ai.openai` ఫ్రీఫిక్స్ మరియు సరళీకృత చాట్ ప్రాపర్టీస్ (ఏ `options` బ్లాక్ లేకుండా) ఉపయోగిస్తాయి:

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

`model` అనేది **ఆస్యూర్ డ్రిప్లాయ్‌మెంట్ పేరు**. గుర్తింపు పై పేర్కొన్న స్పష్టమైన బీన్ల వద్ద నుండే వస్తుంది, `api-key` సెటింగ్ నుంచి కాదు. పాఠం రీజనింగ్ ను ఆపేస్తుంది మరియు పూర్తి టోకెన్లను 500కి పరిమితం చేస్తుంది; `temperature` మరియు పాత `max-tokens` ని ఉన్నట్టుగా ఉంచుతుంది.

Microsoft కొత్త అప్లికేషన్ల కోసం [ఆధికారిక OpenAI SDK ఆస్యూర్ OpenAI v1 మరియు రెస్పాన్సెస్ API తో](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) ఉపయోగించాలని సూచిస్తుంది. చాట్ కంప్లీషన్స్ ఈ ఉన్న సందేశ-ఆధారిత పాఠం కోసం మద్దతు ఉంటుంది. GPT-5.6 కోసం, టూల్స్ ఉన్న రిక్వెస్టులు చాట్ కంప్లీషన్స్ లో `reasoning_effort`ని `none` గా సెట్ చేయాలి; రీజనింగ్ టూల్స్‌తో కలిపి రెస్పాన్సెస్ ఉపయోగించాలి. [రీజనింగ్ మోడల్స్ తో టూల్ కాలింగ్](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models) చూడండి.

## గొడవలు పరిష్కారం

### సాధారణ సమస్యలు

<details>
<summary><strong>లోపం: 401 / "PermissionDenied" / టోకెన్ లోపాలు</strong></summary>

- `az login` నడపండి — కీఅల్పైన గుర్తింపు టోకెన్ పొందడానికి ప్రస్తుత సైన్-ఇన్ అవసరం
- మీ ఖాతాకు ఆ వనరుపై **కాగ్నిటివ్ సర్వీసెస్ OpenAI యూజర్** పాత్ర ఉందని నిర్ధారించుకోండి
- మీరు తాజాగా పాత్రని అప్పగిస్తే, దాని వ్యాప్తికి ఒక నిమిషం వేచి ఉండండి
- మీరు సరైన టెనెంట్/సబ్‌స్క్రిప్షన్‌లో ఉన్నారా అని ధృవీకరించుకోండి (`az account show`)
</details>

<details>
<summary><strong>లోపం: "ఎండ్‌పాయింట్ సరి కాదు" / కనెక్షన్ లోపాలు</strong></summary>

- `AZURE_OPENAI_ENDPOINT` పూర్తిస్థాయి బేస్ URL కాదనుకోండి (ఉదా: `https://your-resource.openai.azure.com/`)
- చివరిరబ్బరు కాంసిస్టెన్సీ (Slash consistency) తనిఖీ చేయండి
- ఎండ్‌పాయింట్ మీరు ప్రావిజన్ చేసిన వనరుకు సరిపోతుందా చూడండి (`azd env get-values`)
</details>

<details>
<summary><strong>లోపం: "డిప్లాయ్‌మెంట్ దొరకలేదు"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` ఆస్యూర్ లోని డిప్లాయ్‌మెంట్ పేరుతో సరిపోతున్నదో లేదో నిర్ధారించుకోండి
- మోడల్ విజయవంతంగా డిప్లాయ్ కాగా, యాక్టివ్ ఉందో చూసుకోండి
- డిఫాల్ట్ డ్రిప్లాయ్‌మెంట్ పేరు `gpt-5.6-luna`
</details>

<details>
<summary><strong>లోపం: 429 / రేట్లు లిమిట్ దాటింది</strong></summary>

- డిఫాల్ట్ GPT-5.6 లూనా డ్రిప్లాయ్‌మెంట్ కి గ్లోబల్ స్టాండర్డ్ సామర్థ్యం 10: నిమిషానికి 10 అభ్యర్థనలు మరియు నిమిషానికి 10,000 టోకెన్లు
- ఉదాహరణలను వరుసగా నడపండి మరియు సర్వీస్ రీట్రై ఇంటర్వెల్ కోసం ఆగండి
- ఈ ప్రాథమిక ఉదాహరణ ఆటోమాటిక్ SDK రీట్రైలను ఆపేస్తుంది, కాబట్టి ఒక విఫల అభ్యర్థన నేరుగా తెలియజేస్తుంది
</details>

<details>
<summary><strong>VS కోడ్: పర్యావరణ వేరియబుల్స్ లోడ్ కావడం లేదు</strong></summary>

- `.env` ఫైల్ ప్రాజెక్టు రూట్ డైరెక్టరీలో ఉందని నిర్ధారించుకోండి (`pom.xml`ని సమాన స్థాయిలో)
- VS కోడ్ యొక్క ఇంటిగ్రేటెడ్ టెర్మినల్ లో `mvn spring-boot:run` నడపండి
- VS కోడ్ జావా ఎక్స్‌టెన్షన్ సరిగా ఇన్‌స్టాల్ అయిందో లేదో చూడండి
</details>

### డీబగ్ మోడ్

డీటైల్డ్ లాగింగ్ కోసం, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) లో ఈ లైన్ల వ్యాఖ్యను తొలగించండి:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## తరువాతి దశలు

**సెటప్ ముగిసింది!** మీ నేర్చుకోవటాన్ని కొనసాగించండి:

[అధ్యాయం 3: ముఖ్యమైన జనరేటివ్ ఎ ఐ సాంకేతికతలు](../../../03-CoreGenerativeAITechniques/README.md)

## వనరులు

- [స్ప్రింగ్ ఎ ఐ 2 OpenAI జావా SDK మార్పిడి](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [అధికారిక OpenAI జావా SDK ఆస్యూర్ OpenAI v1 తో](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID తో కీఅల్పైన గుర్తింపు](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [ఆస్యూర్ ఎ ఐ ఫౌండ్రీ పోర్టల్](https://ai.azure.com/)
- [ఆస్యూర్ ఎ ఐ ఫౌండ్రీ డాక్యుమెంటేషన్](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్వీకరణ**:
ఈ పత్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలు తప్పులు లేదా అసమగ్రతలను కలిగి ఉండవచ్చు. దాని స్వదేశ భాషలో ఉన్న అసలు పత్రాన్ని అధికారం కలిగిన మూలంగా పరిగణించాలి. కీలకమైన సమాచారం కోసం, ప్రొఫెషనల్ మానవ అనువాదాన్ని సిఫారసు చేస్తాము. ఈ అనువాదం ఉపయోగం వల్ల కలిగే ఏవైనా అపార్థాలు లేదా తప్పుదారులు కోసం మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->