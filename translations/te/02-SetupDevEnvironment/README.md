<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "4bdff5070d182c64143dfe5a581d0ec7",
  "translation_date": "2025-12-01T09:33:45+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "te"
}
-->
# జెనరేటివ్ AI కోసం Java డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సెటప్ చేయడం

> **త్వరిత ప్రారంభం**: క్లౌడ్‌లో 2 నిమిషాల్లో కోడ్ చేయండి - [GitHub Codespaces సెటప్](../../../02-SetupDevEnvironment)కి వెళ్లండి - స్థానిక ఇన్‌స్టాలేషన్ అవసరం లేదు మరియు GitHub మోడల్స్ ఉపయోగిస్తుంది!

> **Azure OpenAI గురించి ఆసక్తి ఉందా?**, కొత్త Azure OpenAI రిసోర్స్ సృష్టించడానికి దశలతో మా [Azure OpenAI సెటప్ గైడ్](getting-started-azure-openai.md) చూడండి.

## మీరు నేర్చుకునేది

- AI అప్లికేషన్ల కోసం Java డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సెటప్ చేయడం
- మీకు ఇష్టమైన డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ (క్లౌడ్-ఫస్ట్ Codespacesతో, స్థానిక డెవ్ కంటైనర్, లేదా పూర్తి స్థానిక సెటప్) ఎంచుకోవడం మరియు కాన్ఫిగర్ చేయడం
- GitHub మోడల్స్‌కు కనెక్ట్ చేయడం ద్వారా మీ సెటప్‌ను పరీక్షించండి

## విషయ సూచిక

- [మీరు నేర్చుకునేది](../../../02-SetupDevEnvironment)
- [పరిచయం](../../../02-SetupDevEnvironment)
- [దశ 1: మీ డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సెటప్ చేయండి](../../../02-SetupDevEnvironment)
  - [ఆప్షన్ A: GitHub Codespaces (సిఫార్సు చేయబడింది)](../../../02-SetupDevEnvironment)
  - [ఆప్షన్ B: స్థానిక డెవ్ కంటైనర్](../../../02-SetupDevEnvironment)
  - [ఆప్షన్ C: మీ ప్రస్తుత స్థానిక ఇన్‌స్టాలేషన్ ఉపయోగించండి](../../../02-SetupDevEnvironment)
- [దశ 2: GitHub వ్యక్తిగత యాక్సెస్ టోకెన్ సృష్టించండి](../../../02-SetupDevEnvironment)
- [దశ 3: మీ సెటప్‌ను పరీక్షించండి](../../../02-SetupDevEnvironment)
- [ట్రబుల్‌షూటింగ్](../../../02-SetupDevEnvironment)
- [సారాంశం](../../../02-SetupDevEnvironment)
- [తదుపరి దశలు](../../../02-SetupDevEnvironment)

## పరిచయం

ఈ అధ్యాయం మీకు డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సెటప్ చేయడంలో సహాయపడుతుంది. **GitHub మోడల్స్**ను మా ప్రధాన ఉదాహరణగా ఉపయోగిస్తాము ఎందుకంటే ఇది ఉచితం, GitHub ఖాతాతో సులభంగా సెటప్ చేయవచ్చు, క్రెడిట్ కార్డ్ అవసరం లేదు, మరియు అన్వేషణ కోసం అనేక మోడల్స్ అందిస్తుంది.

**స్థానిక సెటప్ అవసరం లేదు!** మీరు GitHub Codespaces ఉపయోగించి వెంటనే కోడింగ్ ప్రారంభించవచ్చు, ఇది మీ బ్రౌజర్‌లో పూర్తి డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ అందిస్తుంది.

<img src="./images/models.webp" alt="స్క్రీన్‌షాట్: GitHub మోడల్స్" width="50%">

ఈ కోర్సు కోసం [**GitHub మోడల్స్**](https://github.com/marketplace?type=models)ను ఉపయోగించమని మేము సిఫార్సు చేస్తున్నాము ఎందుకంటే:
- **ఉచితం** ప్రారంభించడానికి
- **సులభం** GitHub ఖాతాతో సెటప్ చేయడం
- **క్రెడిట్ కార్డ్ అవసరం లేదు**
- **అన్వేషణ కోసం అనేక మోడల్స్**

> **గమనిక**: ఈ శిక్షణలో ఉపయోగించిన GitHub మోడల్స్ ఈ ఉచిత పరిమితులను కలిగి ఉంటాయి:
> - నిమిషానికి 15 అభ్యర్థనలు (రోజుకు 150)
> - ~8,000 పదాలు ఇన్‌పుట్, ~4,000 పదాలు అవుట్‌పుట్ ప్రతి అభ్యర్థనకు
> - 5 సమకాలీన అభ్యర్థనలు
> 
> ఉత్పత్తి ఉపయోగం కోసం, మీ Azure ఖాతాతో Azure AI Foundry మోడల్స్‌కు అప్‌గ్రేడ్ చేయండి. మీ కోడ్ మార్చాల్సిన అవసరం లేదు. [Azure AI Foundry డాక్యుమెంటేషన్](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) చూడండి.

## దశ 1: మీ డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సెటప్ చేయండి

<a name="quick-start-cloud"></a>

ఈ Generative AI for Java కోర్సు కోసం అవసరమైన అన్ని టూల్స్‌ను కలిగి ఉండే ప్రీకాన్ఫిగర్డ్ డెవలప్‌మెంట్ కంటైనర్‌ను మేము సృష్టించాము. మీకు ఇష్టమైన డెవలప్‌మెంట్ విధానాన్ని ఎంచుకోండి:

### ఎన్విరాన్‌మెంట్ సెటప్ ఆప్షన్లు:

#### ఆప్షన్ A: GitHub Codespaces (సిఫార్సు చేయబడింది)

**2 నిమిషాల్లో కోడింగ్ ప్రారంభించండి - స్థానిక సెటప్ అవసరం లేదు!**

1. ఈ రిపోజిటరీని మీ GitHub ఖాతాకు ఫోర్క్ చేయండి
   > **గమనిక**: మీరు ప్రాథమిక కాన్ఫిగరేషన్‌ను సవరించాలనుకుంటే [Dev Container Configuration](../../../.devcontainer/devcontainer.json) చూడండి
2. **Code** → **Codespaces** ట్యాబ్ → **...** → **New with options...** క్లిక్ చేయండి
3. డిఫాల్ట్స్ ఉపయోగించండి – ఇది **Generative AI Java Development Environment** కస్టమ్ డెవ్ కంటైనర్‌ను ఎంచుకుంటుంది
4. **Create codespace** క్లిక్ చేయండి
5. ఎన్విరాన్‌మెంట్ సిద్ధం కావడానికి ~2 నిమిషాలు వేచి ఉండండి
6. [దశ 2: GitHub టోకెన్ సృష్టించండి](../../../02-SetupDevEnvironment)కి కొనసాగండి

<img src="../../../translated_images/te/codespaces.9945ded8ceb431a5.webp" alt="స్క్రీన్‌షాట్: Codespaces ఉపమెను" width="50%">

<img src="../../../translated_images/te/image.833552b62eee7766.webp" alt="స్క్రీన్‌షాట్: New with options" width="50%">

<img src="../../../translated_images/te/codespaces-create.b44a36f728660ab7.webp" alt="స్క్రీన్‌షాట్: Create codespace options" width="50%">


> **Codespaces ప్రయోజనాలు**:
> - స్థానిక ఇన్‌స్టాలేషన్ అవసరం లేదు
> - బ్రౌజర్ ఉన్న ఏ డివైస్‌లోనైనా పనిచేస్తుంది
> - అన్ని టూల్స్ మరియు డిపెండెన్సీలతో ప్రీ-కాన్ఫిగర్డ్
> - వ్యక్తిగత ఖాతాల కోసం నెలకు 60 గంటలు ఉచితం
> - అన్ని నేర్చుకునే వారికి స్థిరమైన ఎన్విరాన్‌మెంట్

#### ఆప్షన్ B: స్థానిక డెవ్ కంటైనర్

**Dockerతో స్థానిక డెవలప్‌మెంట్ ఇష్టపడే డెవలపర్ల కోసం**

1. ఈ రిపోజిటరీని మీ స్థానిక యంత్రానికి ఫోర్క్ చేసి క్లోన్ చేయండి
   > **గమనిక**: మీరు ప్రాథమిక కాన్ఫిగరేషన్‌ను సవరించాలనుకుంటే [Dev Container Configuration](../../../.devcontainer/devcontainer.json) చూడండి
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) మరియు [VS Code](https://code.visualstudio.com/) ఇన్‌స్టాల్ చేయండి
3. VS Codeలో [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ఇన్‌స్టాల్ చేయండి
4. రిపోజిటరీ ఫోల్డర్‌ను VS Codeలో ఓపెన్ చేయండి
5. ప్రాంప్ట్ వచ్చినప్పుడు, **Reopen in Container** క్లిక్ చేయండి (లేదా `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" ఉపయోగించండి)
6. కంటైనర్ నిర్మించడానికి మరియు ప్రారంభించడానికి వేచి ఉండండి
7. [దశ 2: GitHub టోకెన్ సృష్టించండి](../../../02-SetupDevEnvironment)కి కొనసాగండి

<img src="../../../translated_images/te/devcontainer.21126c9d6de64494.webp" alt="స్క్రీన్‌షాట్: Dev container సెటప్" width="50%">

<img src="../../../translated_images/te/image-3.bf93d533bbc84268.webp" alt="స్క్రీన్‌షాట్: Dev container నిర్మాణం పూర్తయింది" width="50%">

#### ఆప్షన్ C: మీ ప్రస్తుత స్థానిక ఇన్‌స్టాలేషన్ ఉపయోగించండి

**ఇప్పటికే ఉన్న Java ఎన్విరాన్‌మెంట్ ఉన్న డెవలపర్ల కోసం**

అవసరమైనవి:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) లేదా మీకు ఇష్టమైన IDE

దశలు:
1. ఈ రిపోజిటరీని మీ స్థానిక యంత్రానికి క్లోన్ చేయండి
2. ప్రాజెక్ట్‌ను మీ IDEలో ఓపెన్ చేయండి
3. [దశ 2: GitHub టోకెన్ సృష్టించండి](../../../02-SetupDevEnvironment)కి కొనసాగండి

> **ప్రో టిప్**: మీకు తక్కువ స్పెక్స్ ఉన్న యంత్రం ఉంటే కానీ స్థానికంగా VS Code కావాలనుకుంటే, GitHub Codespaces ఉపయోగించండి! మీరు మీ స్థానిక VS Codeను క్లౌడ్-హోస్టెడ్ Codespaceకి కనెక్ట్ చేయవచ్చు.

<img src="../../../translated_images/te/image-2.fc0da29a6e4d2aff.webp" alt="స్క్రీన్‌షాట్: స్థానిక డెవ్ కంటైనర్ ఉదాహరణ సృష్టించబడింది" width="50%">


## దశ 2: GitHub వ్యక్తిగత యాక్సెస్ టోకెన్ సృష్టించండి

1. [GitHub సెట్టింగ్స్](https://github.com/settings/profile)కి వెళ్లి మీ ప్రొఫైల్ మెను నుండి **Settings** ఎంచుకోండి.
2. ఎడమ సైడ్‌బార్‌లో, **Developer settings** క్లిక్ చేయండి (సాధారణంగా దిగువన ఉంటుంది).
3. **Personal access tokens** కింద, **Fine-grained tokens** క్లిక్ చేయండి (లేదా ఈ [లింక్](https://github.com/settings/personal-access-tokens)ను అనుసరించండి).
4. **Generate new token** క్లిక్ చేయండి.
5. "Token name" కింద, వివరణాత్మక పేరు ఇవ్వండి (ఉదా., `GenAI-Java-Course-Token`).
6. గడువు తేదీని సెట్ చేయండి (భద్రతా ఉత్తమ పద్ధతుల కోసం: 7 రోజులు సిఫార్సు చేయబడింది).
7. "Resource owner" కింద, మీ యూజర్ ఖాతాను ఎంచుకోండి.
8. "Repository access" కింద, GitHub మోడల్స్‌తో ఉపయోగించాలనుకుంటున్న రిపోజిటరీలను ఎంచుకోండి (లేదా అవసరమైతే "All repositories").
9. "Account permissions" కింద, **Models**ను కనుగొని **Read-only**గా సెట్ చేయండి.
10. **Generate token** క్లిక్ చేయండి.
11. **మీ టోకెన్‌ను ఇప్పుడు కాపీ చేసి సేవ్ చేయండి** – మీరు దీన్ని మళ్లీ చూడలేరు!

> **భద్రతా సూచన**: మీ యాక్సెస్ టోకెన్ల కోసం అవసరమైన కనీస స్కోప్ మరియు అత్యల్ప గడువు సమయాన్ని ఉపయోగించండి.

## దశ 3: GitHub మోడల్స్ ఉదాహరణతో మీ సెటప్‌ను పరీక్షించండి

మీ డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సిద్ధంగా ఉన్న తర్వాత, [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models)లో మా ఉదాహరణ అప్లికేషన్‌తో GitHub మోడల్స్ ఇంటిగ్రేషన్‌ను పరీక్షిద్దాం.

1. మీ డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్‌లో టెర్మినల్‌ను ఓపెన్ చేయండి.
2. GitHub మోడల్స్ ఉదాహరణకు వెళ్లండి:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. మీ GitHub టోకెన్‌ను ఎన్విరాన్‌మెంట్ వేరియబుల్‌గా సెట్ చేయండి:
   ```bash
   # మాక్‌ఓఎస్/లినక్స్
   export GITHUB_TOKEN=your_token_here
   
   # విండోస్ (కమాండ్ ప్రాంప్ట్)
   set GITHUB_TOKEN=your_token_here
   
   # విండోస్ (పవర్‌షెల్)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. అప్లికేషన్‌ను రన్ చేయండి:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

మీకు ఇలాంటి అవుట్‌పుట్ కనిపించాలి:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### ఉదాహరణ కోడ్‌ను అర్థం చేసుకోవడం

ముందుగా, మేము ఏమి రన్ చేశామో అర్థం చేసుకుందాం. `examples/github-models` కింద ఉన్న ఉదాహరణ OpenAI Java SDKను ఉపయోగించి GitHub మోడల్స్‌కు కనెక్ట్ అవుతుంది:

**ఈ కోడ్ ఏమి చేస్తుంది:**
- **GitHub మోడల్స్**కు మీ వ్యక్తిగత యాక్సెస్ టోకెన్ ఉపయోగించి కనెక్ట్ అవుతుంది
- AI మోడల్‌కు "Say Hello World!" అనే సింపుల్ మెసేజ్ పంపుతుంది
- AI యొక్క ప్రతిస్పందనను స్వీకరించి ప్రదర్శిస్తుంది
- మీ సెటప్ సరిగ్గా పనిచేస్తుందో ధృవీకరిస్తుంది

**ముఖ్యమైన డిపెండెన్సీ** (`pom.xml`లో):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**ప్రధాన కోడ్** (`App.java`):
```java
// OpenAI Java SDK ఉపయోగించి GitHub మోడల్స్‌కు కనెక్ట్ అవ్వండి
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// చాట్ పూర్తి అభ్యర్థనను సృష్టించండి
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// AI ప్రతిస్పందనను పొందండి
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## సారాంశం

అద్భుతం! మీరు ఇప్పుడు అన్ని సెటప్ పూర్తి చేసారు:

- AI మోడల్ యాక్సెస్ కోసం సరైన అనుమతులతో GitHub వ్యక్తిగత యాక్సెస్ టోకెన్ సృష్టించారు
- మీ Java డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ (Codespaces, dev కంటైనర్లు, లేదా స్థానిక) రన్ చేయబడింది
- OpenAI Java SDK ఉపయోగించి GitHub మోడల్స్‌కు కనెక్ట్ అయ్యారు
- AI మోడల్స్‌తో మాట్లాడే సింపుల్ ఉదాహరణతో అన్ని పనిచేస్తున్నాయని పరీక్షించారు

## తదుపరి దశలు

[అధ్యాయం 3: కోర్ జెనరేటివ్ AI టెక్నిక్స్](../03-CoreGenerativeAITechniques/README.md)

## ట్రబుల్‌షూటింగ్

సమస్యలు ఎదుర్కొంటున్నారా? ఇక్కడ సాధారణ సమస్యలు మరియు పరిష్కారాలు ఉన్నాయి:

- **టోకెన్ పనిచేయడం లేదా?** 
  - మీరు మొత్తం టోకెన్‌ను ఎటువంటి అదనపు స్పేస్‌లతో కాపీ చేయలేదని నిర్ధారించండి
  - టోకెన్ సరైనంగా ఎన్విరాన్‌మెంట్ వేరియబుల్‌గా సెట్ చేయబడిందని ధృవీకరించండి
  - మీ టోకెన్ సరైన అనుమతులు కలిగి ఉందని (Models: Read and write) తనిఖీ చేయండి

- **Maven కనుగొనబడలేదు?** 
  - dev కంటైనర్లు/Codespaces ఉపయోగిస్తే, Maven ముందే ఇన్‌స్టాల్ చేయబడుతుంది
  - స్థానిక సెటప్ కోసం, Java 21+ మరియు Maven 3.9+ ఇన్‌స్టాల్ చేయబడిందని నిర్ధారించండి
  - ఇన్‌స్టాలేషన్ ధృవీకరించడానికి `mvn --version` ప్రయత్నించండి

- **కనెక్షన్ సమస్యలు?** 
  - మీ ఇంటర్నెట్ కనెక్షన్ తనిఖీ చేయండి
  - GitHub మీ నెట్‌వర్క్ నుండి యాక్సెసిబుల్‌గా ఉందని ధృవీకరించండి
  - GitHub మోడల్స్ ఎండ్‌పాయింట్‌ను బ్లాక్ చేసే ఫైర్‌వాల్ వెనుక మీరు లేరని నిర్ధారించండి

- **Dev కంటైనర్ ప్రారంభం కావడం లేదు?** 
  - Docker Desktop రన్ అవుతుందని నిర్ధారించండి (స్థానిక డెవలప్‌మెంట్ కోసం)
  - కంటైనర్‌ను మళ్లీ నిర్మించడానికి ప్రయత్నించండి: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **అప్లికేషన్ కంపైలేషన్ లోపాలు?**
  - మీరు సరైన డైరెక్టరీలో ఉన్నారని నిర్ధారించండి: `02-SetupDevEnvironment/examples/github-models`
  - క్లీనింగ్ మరియు రీబిల్డింగ్ ప్రయత్నించండి: `mvn clean compile`

> **సహాయం కావాలా?**: ఇంకా సమస్యలు ఎదుర్కొంటున్నారా? రిపోజిటరీలో ఒక ఇష్యూ ఓపెన్ చేయండి, మేము మీకు సహాయం చేస్తాము.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్వీకరణ**:  
ఈ పత్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలు తప్పులు లేదా అసమగ్రతలను కలిగి ఉండవచ్చు. దాని స్వస్థల భాషలో ఉన్న అసలు పత్రాన్ని అధికారం కలిగిన మూలంగా పరిగణించాలి. కీలకమైన సమాచారం కోసం, ప్రొఫెషనల్ మానవ అనువాదాన్ని సిఫారసు చేస్తాము. ఈ అనువాదాన్ని ఉపయోగించడం వల్ల కలిగే ఏవైనా అపార్థాలు లేదా తప్పుదారులు కోసం మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->