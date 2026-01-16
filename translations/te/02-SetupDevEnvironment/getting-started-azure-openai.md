<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-12-01T09:32:40+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "te"
}
-->
# Azure OpenAI కోసం డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సెట్ అప్ చేయడం

> **త్వరిత ప్రారంభం**: ఇది Azure OpenAI సెటప్ కోసం గైడ్. ఉచిత మోడల్స్‌తో వెంటనే ప్రారంభించాలంటే, [GitHub Models with Codespaces](./README.md#quick-start-cloud) ఉపయోగించండి.

ఈ గైడ్ మీ Java AI యాప్‌ల కోసం Azure AI Foundry మోడల్స్‌ను సెట్ అప్ చేయడంలో మీకు సహాయపడుతుంది.

## విషయ సూచిక

- [త్వరిత సెటప్ అవలోకనం](../../../02-SetupDevEnvironment)
- [దశ 1: Azure AI Foundry వనరులను సృష్టించండి](../../../02-SetupDevEnvironment)
  - [హబ్ మరియు ప్రాజెక్ట్ సృష్టించండి](../../../02-SetupDevEnvironment)
  - [GPT-4o-mini మోడల్‌ను డిప్లాయ్ చేయండి](../../../02-SetupDevEnvironment)
- [దశ 2: మీ కోడ్‌స్పేస్‌ను సృష్టించండి](../../../02-SetupDevEnvironment)
- [దశ 3: మీ ఎన్విరాన్‌మెంట్‌ను కాన్ఫిగర్ చేయండి](../../../02-SetupDevEnvironment)
- [దశ 4: మీ సెటప్‌ను పరీక్షించండి](../../../02-SetupDevEnvironment)
- [తరువాత ఏమిటి?](../../../02-SetupDevEnvironment)
- [వనరులు](../../../02-SetupDevEnvironment)
- [అదనపు వనరులు](../../../02-SetupDevEnvironment)

## త్వరిత సెటప్ అవలోకనం

1. Azure AI Foundry వనరులను సృష్టించండి (హబ్, ప్రాజెక్ట్, మోడల్)
2. Java డెవలప్‌మెంట్ కంటైనర్‌తో కోడ్‌స్పేస్ సృష్టించండి
3. Azure OpenAI క్రెడెన్షియల్స్‌తో మీ .env ఫైల్‌ను కాన్ఫిగర్ చేయండి
4. ఉదాహరణ ప్రాజెక్ట్‌తో మీ సెటప్‌ను పరీక్షించండి

## దశ 1: Azure AI Foundry వనరులను సృష్టించండి

### హబ్ మరియు ప్రాజెక్ట్ సృష్టించండి

1. [Azure AI Foundry Portal](https://ai.azure.com/)కి వెళ్లి సైన్ ఇన్ చేయండి
2. **+ Create** → **New hub** క్లిక్ చేయండి (లేదా **Management** → **All hubs** → **+ New hub**కి వెళ్లండి)
3. మీ హబ్‌ను కాన్ఫిగర్ చేయండి:
   - **Hub name**: ఉదా., "MyAIHub"
   - **Subscription**: మీ Azure సబ్‌స్క్రిప్షన్‌ను ఎంచుకోండి
   - **Resource group**: కొత్తది సృష్టించండి లేదా ఉన్నదాన్ని ఎంచుకోండి
   - **Location**: మీకు దగ్గరగా ఉన్నదాన్ని ఎంచుకోండి
   - **Storage account**: డిఫాల్ట్‌ను ఉపయోగించండి లేదా కస్టమ్‌గా కాన్ఫిగర్ చేయండి
   - **Key vault**: డిఫాల్ట్‌ను ఉపయోగించండి లేదా కస్టమ్‌గా కాన్ఫిగర్ చేయండి
   - **Next** → **Review + create** → **Create** క్లిక్ చేయండి
4. సృష్టించిన తర్వాత, **+ New project** క్లిక్ చేయండి (లేదా హబ్ అవలోకనం నుండి **Create project**)
   - **Project name**: ఉదా., "GenAIJava"
   - **Create** క్లిక్ చేయండి

### GPT-4o-mini మోడల్‌ను డిప్లాయ్ చేయండి

1. మీ ప్రాజెక్ట్‌లో **Model catalog**కి వెళ్లి **gpt-4o-mini** కోసం శోధించండి
   - *ప్రత్యామ్నాయం: **Deployments** → **+ Create deployment**కి వెళ్లండి*
2. gpt-4o-mini మోడల్ కార్డ్‌పై **Deploy** క్లిక్ చేయండి
3. డిప్లాయ్‌మెంట్‌ను కాన్ఫిగర్ చేయండి:
   - **Deployment name**: "gpt-4o-mini"
   - **Model version**: తాజాదాన్ని ఉపయోగించండి
   - **Deployment type**: స్టాండర్డ్
4. **Deploy** క్లిక్ చేయండి
5. డిప్లాయ్ అయిన తర్వాత, **Deployments** ట్యాబ్‌కి వెళ్లి ఈ విలువలను కాపీ చేయండి:
   - **Deployment name** (ఉదా., "gpt-4o-mini")
   - **Target URI** (ఉదా., `https://your-hub-name.openai.azure.com/`) 
      > **ముఖ్యం**: పూర్తి ఎండ్‌పాయింట్ పాత్ కాకుండా బేస్ URL (ఉదా., `https://myhub.openai.azure.com/`) మాత్రమే కాపీ చేయండి.
   - **Key** (Keys and Endpoint విభాగం నుండి)

> **ఇంకా సమస్యలు ఉన్నాయా?** అధికారిక [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)ను సందర్శించండి

## దశ 2: మీ కోడ్‌స్పేస్‌ను సృష్టించండి

1. ఈ రిపోజిటరీని మీ GitHub ఖాతాకు ఫోర్క్ చేయండి
   > **గమనిక**: మీరు ప్రాథమిక కాన్ఫిగరేషన్‌ను మార్చాలనుకుంటే, [Dev Container Configuration](../../../.devcontainer/devcontainer.json)ని చూడండి
2. మీ ఫోర్క్ చేసిన రిపోలో, **Code** → **Codespaces** ట్యాబ్ క్లిక్ చేయండి
3. **...** → **New with options...** క్లిక్ చేయండి  
![creating a codespace with options](../../../translated_images/te/codespaces.9945ded8ceb431a5.webp)
4. **Dev container configuration** ఎంచుకోండి: 
   - **Generative AI Java Development Environment**
5. **Create codespace** క్లిక్ చేయండి

## దశ 3: మీ ఎన్విరాన్‌మెంట్‌ను కాన్ఫిగర్ చేయండి

మీ కోడ్‌స్పేస్ సిద్ధమైన తర్వాత, మీ Azure OpenAI క్రెడెన్షియల్స్‌ను సెట్ చేయండి:

1. **రిపోజిటరీ రూట్ నుండి ఉదాహరణ ప్రాజెక్ట్‌కి వెళ్లండి:**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **మీ .env ఫైల్‌ను సృష్టించండి:**
   ```bash
   cp .env.example .env
   ```

3. **మీ Azure OpenAI క్రెడెన్షియల్స్‌తో .env ఫైల్‌ను ఎడిట్ చేయండి:**
   ```bash
   # మీ Azure OpenAI API కీ (Azure AI Foundry పోర్టల్ నుండి)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # మీ Azure OpenAI ఎండ్‌పాయింట్ URL (ఉదా., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **భద్రతా గమనిక**: 
   > - మీ `.env` ఫైల్‌ను వెర్షన్ కంట్రోల్‌కు కమిట్ చేయవద్దు
   > - `.env` ఫైల్ ఇప్పటికే `.gitignore`లో చేర్చబడింది
   > - మీ API కీలు సురక్షితంగా ఉంచండి మరియు వాటిని తరచుగా రొటేట్ చేయండి

## దశ 4: మీ సెటప్‌ను పరీక్షించండి

Azure OpenAI కనెక్షన్‌ను పరీక్షించడానికి ఉదాహరణ అప్లికేషన్‌ను రన్ చేయండి:

```bash
mvn clean spring-boot:run
```

మీకు GPT-4o-mini మోడల్ నుండి స్పందన కనిపించాలి!

> **VS కోడ్ వినియోగదారులు**: అప్లికేషన్‌ను రన్ చేయడానికి మీరు VS కోడ్‌లో `F5` నొక్కవచ్చు. లాంచ్ కాన్ఫిగరేషన్ ఇప్పటికే మీ `.env` ఫైల్‌ను ఆటోమేటిక్‌గా లోడ్ చేయడానికి సెట్ చేయబడింది.

> **పూర్తి ఉదాహరణ**: వివరమైన సూచనలు మరియు సమస్యల పరిష్కారానికి [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) చూడండి.

## తరువాత ఏమిటి?

**సెటప్ పూర్తయింది!** మీరు ఇప్పుడు:
- gpt-4o-miniతో Azure OpenAIని డిప్లాయ్ చేశారు
- స్థానిక .env ఫైల్ కాన్ఫిగరేషన్
- Java డెవలప్‌మెంట్ ఎన్విరాన్‌మెంట్ సిద్ధంగా ఉంది

**తరువాత కొనసాగండి** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md)కి, AI అప్లికేషన్లను నిర్మించడం ప్రారంభించండి!

## వనరులు

- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI Documentation](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## అదనపు వనరులు

- [VS కోడ్ డౌన్‌లోడ్ చేయండి](https://code.visualstudio.com/Download)
- [డాకర్ డెస్క్‌టాప్ పొందండి](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**అస్వీకరణ**:  
ఈ పత్రం AI అనువాద సేవ [Co-op Translator](https://github.com/Azure/co-op-translator) ఉపయోగించి అనువదించబడింది. మేము ఖచ్చితత్వానికి ప్రయత్నిస్తున్నప్పటికీ, ఆటోమేటెడ్ అనువాదాలు తప్పులు లేదా అసమగ్రతలను కలిగి ఉండవచ్చు. దాని స్వదేశ భాషలో ఉన్న అసలు పత్రాన్ని అధికారం కలిగిన మూలంగా పరిగణించాలి. కీలకమైన సమాచారం కోసం, ప్రొఫెషనల్ మానవ అనువాదం సిఫారసు చేయబడుతుంది. ఈ అనువాదం ఉపయోగం వల్ల కలిగే ఏదైనా అపార్థాలు లేదా తప్పుదారులు కోసం మేము బాధ్యత వహించము.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->