<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-12-01T09:33:02+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "ml"
}
-->
# Azure OpenAI വികസന പരിസ്ഥിതി സജ്ജമാക്കൽ

> **Quick Start**: ഈ ഗൈഡ് Azure OpenAI സജ്ജീകരണത്തിനാണ്. സൗജന്യ മോഡലുകൾ ഉപയോഗിച്ച് ഉടൻ ആരംഭിക്കാൻ, [GitHub Models with Codespaces](./README.md#quick-start-cloud) ഉപയോഗിക്കുക.

ഈ ഗൈഡ് നിങ്ങളുടെ Java AI ആപ്പുകൾക്കായി Azure AI Foundry മോഡലുകൾ സജ്ജമാക്കാൻ നിങ്ങളെ സഹായിക്കും.

## ഉള്ളടക്ക പട്ടിക

- [സർവസരളമായ സജ്ജീകരണ അവലോകനം](../../../02-SetupDevEnvironment)
- [പടി 1: Azure AI Foundry റിസോഴ്സുകൾ സൃഷ്ടിക്കുക](../../../02-SetupDevEnvironment)
  - [ഹബ്, പ്രോജക്റ്റ് സൃഷ്ടിക്കുക](../../../02-SetupDevEnvironment)
  - [GPT-4o-mini മോഡൽ ഡിപ്ലോയ് ചെയ്യുക](../../../02-SetupDevEnvironment)
- [പടി 2: നിങ്ങളുടെ Codespace സൃഷ്ടിക്കുക](../../../02-SetupDevEnvironment)
- [പടി 3: നിങ്ങളുടെ പരിസ്ഥിതി ക്രമീകരിക്കുക](../../../02-SetupDevEnvironment)
- [പടി 4: നിങ്ങളുടെ സജ്ജീകരണം പരിശോധിക്കുക](../../../02-SetupDevEnvironment)
- [അടുത്തത് എന്ത്?](../../../02-SetupDevEnvironment)
- [വിശദമായ റിസോഴ്സുകൾ](../../../02-SetupDevEnvironment)
- [അധിക റിസോഴ്സുകൾ](../../../02-SetupDevEnvironment)

## സർവസരളമായ സജ്ജീകരണ അവലോകനം

1. Azure AI Foundry റിസോഴ്സുകൾ സൃഷ്ടിക്കുക (ഹബ്, പ്രോജക്റ്റ്, മോഡൽ)
2. Java ഡെവലപ്മെന്റ് കണ്ടെയ്‌നർ ഉപയോഗിച്ച് Codespace സൃഷ്ടിക്കുക
3. Azure OpenAI ക്രെഡൻഷ്യലുകൾ ഉപയോഗിച്ച് നിങ്ങളുടെ .env ഫയൽ ക്രമീകരിക്കുക
4. ഉദാഹരണ പ്രോജക്റ്റ് ഉപയോഗിച്ച് നിങ്ങളുടെ സജ്ജീകരണം പരിശോധിക്കുക

## പടി 1: Azure AI Foundry റിസോഴ്സുകൾ സൃഷ്ടിക്കുക

### ഹബ്, പ്രോജക്റ്റ് സൃഷ്ടിക്കുക

1. [Azure AI Foundry Portal](https://ai.azure.com/) സന്ദർശിച്ച് സൈൻ ഇൻ ചെയ്യുക
2. **+ Create** → **New hub** ക്ലിക്കുചെയ്യുക (അല്ലെങ്കിൽ **Management** → **All hubs** → **+ New hub** വഴി നാവിഗേറ്റ് ചെയ്യുക)
3. നിങ്ങളുടെ ഹബ് ക്രമീകരിക്കുക:
   - **Hub name**: ഉദാ., "MyAIHub"
   - **Subscription**: നിങ്ങളുടെ Azure സബ്സ്ക്രിപ്ഷൻ തിരഞ്ഞെടുക്കുക
   - **Resource group**: പുതിയതോ നിലവിലുള്ളതോ തിരഞ്ഞെടുക്കുക
   - **Location**: നിങ്ങളുടെ അടുത്തുള്ളത് തിരഞ്ഞെടുക്കുക
   - **Storage account**: ഡിഫോൾട്ട് ഉപയോഗിക്കുക അല്ലെങ്കിൽ കസ്റ്റം ക്രമീകരിക്കുക
   - **Key vault**: ഡിഫോൾട്ട് ഉപയോഗിക്കുക അല്ലെങ്കിൽ കസ്റ്റം ക്രമീകരിക്കുക
   - **Next** → **Review + create** → **Create** ക്ലിക്കുചെയ്യുക
4. സൃഷ്ടിച്ചതിന് ശേഷം, **+ New project** (അല്ലെങ്കിൽ **Create project** ഹബ് ഓവർവ്യൂയിൽ നിന്ന്) ക്ലിക്കുചെയ്യുക
   - **Project name**: ഉദാ., "GenAIJava"
   - **Create** ക്ലിക്കുചെയ്യുക

### GPT-4o-mini മോഡൽ ഡിപ്ലോയ് ചെയ്യുക

1. നിങ്ങളുടെ പ്രോജക്റ്റിൽ **Model catalog**-ലേക്ക് പോകുക, **gpt-4o-mini** തിരയുക
   - *വൈകൽപികം: **Deployments** → **+ Create deployment**-ലേക്ക് പോകുക*
2. gpt-4o-mini മോഡൽ കാർഡിൽ **Deploy** ക്ലിക്കുചെയ്യുക
3. ഡിപ്ലോയ്‌മെന്റ് ക്രമീകരിക്കുക:
   - **Deployment name**: "gpt-4o-mini"
   - **Model version**: ഏറ്റവും പുതിയത് തിരഞ്ഞെടുക്കുക
   - **Deployment type**: Standard
4. **Deploy** ക്ലിക്കുചെയ്യുക
5. ഡിപ്ലോയ് ചെയ്ത ശേഷം, **Deployments** ടാബിലേക്ക് പോയി ഈ മൂല്യങ്ങൾ കോപ്പി ചെയ്യുക:
   - **Deployment name** (ഉദാ., "gpt-4o-mini")
   - **Target URI** (ഉദാ., `https://your-hub-name.openai.azure.com/`) 
      > **പ്രധാനമായും ശ്രദ്ധിക്കുക**: പൂർണ്ണ എൻഡ്പോയിന്റ് പാത്ത് അല്ല, അടിസ്ഥാന URL മാത്രം കോപ്പി ചെയ്യുക (ഉദാ., `https://myhub.openai.azure.com/`).
   - **Key** (Keys and Endpoint വിഭാഗത്തിൽ നിന്ന്)

> **ഇനിയും പ്രശ്നമുണ്ടോ?** ഔദ്യോഗിക [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project) സന്ദർശിക്കുക

## പടി 2: നിങ്ങളുടെ Codespace സൃഷ്ടിക്കുക

1. ഈ റിപോസിറ്ററി നിങ്ങളുടെ GitHub അക്കൗണ്ടിലേക്ക് ഫോർക്ക് ചെയ്യുക
   > **ശ്രദ്ധിക്കുക**: അടിസ്ഥാന കോൺഫിഗറേഷൻ എഡിറ്റ് ചെയ്യണമെങ്കിൽ, [Dev Container Configuration](../../../.devcontainer/devcontainer.json) കാണുക
2. നിങ്ങളുടെ ഫോർക്ക് ചെയ്ത റിപോസിറ്ററിയിൽ **Code** → **Codespaces** ടാബ് ക്ലിക്കുചെയ്യുക
3. **...** → **New with options...** ക്ലിക്കുചെയ്യുക
![creating a codespace with options](../../../translated_images/ml/codespaces.9945ded8ceb431a5.webp)
4. **Dev container configuration** തിരഞ്ഞെടുക്കുക: 
   - **Generative AI Java Development Environment**
5. **Create codespace** ക്ലിക്കുചെയ്യുക

## പടി 3: നിങ്ങളുടെ പരിസ്ഥിതി ക്രമീകരിക്കുക

നിങ്ങളുടെ Codespace സജ്ജമായ ശേഷം, Azure OpenAI ക്രെഡൻഷ്യലുകൾ ക്രമീകരിക്കുക:

1. **റിപോസിറ്ററി റൂട്ട് നിന്ന് ഉദാഹരണ പ്രോജക്റ്റിലേക്ക് നാവിഗേറ്റ് ചെയ്യുക:**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **നിങ്ങളുടെ .env ഫയൽ സൃഷ്ടിക്കുക:**
   ```bash
   cp .env.example .env
   ```

3. **Azure OpenAI ക്രെഡൻഷ്യലുകൾ ഉപയോഗിച്ച് .env ഫയൽ എഡിറ്റ് ചെയ്യുക:**
   ```bash
   # നിങ്ങളുടെ Azure OpenAI API കീ (Azure AI Foundry പോർട്ടലിൽ നിന്ന്)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # നിങ്ങളുടെ Azure OpenAI എൻഡ്പോയിന്റ് URL (ഉദാ., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **സുരക്ഷാ കുറിപ്പ്**: 
   > - നിങ്ങളുടെ `.env` ഫയൽ വേർഷൻ കൺട്രോളിലേക്ക് ഒരിക്കലും കമ്മിറ്റ് ചെയ്യരുത്
   > - `.env` ഫയൽ ഇതിനകം `.gitignore`-ൽ ഉൾപ്പെടുത്തിയിട്ടുണ്ട്
   > - നിങ്ങളുടെ API കീകൾ സുരക്ഷിതമായി സൂക്ഷിക്കുക, അവയെ സ്ഥിരമായി പുതുക്കുക

## പടി 4: നിങ്ങളുടെ സജ്ജീകരണം പരിശോധിക്കുക

Azure OpenAI കണക്ഷൻ പരിശോധിക്കാൻ ഉദാഹരണ ആപ്ലിക്കേഷൻ പ്രവർത്തിപ്പിക്കുക:

```bash
mvn clean spring-boot:run
```

നിങ്ങൾ gpt-4o-mini മോഡലിൽ നിന്ന് ഒരു പ്രതികരണം കാണും!

> **VS Code ഉപയോക്താക്കൾ**: ആപ്ലിക്കേഷൻ പ്രവർത്തിപ്പിക്കാൻ VS Code-ൽ `F5` അമർത്താം. നിങ്ങളുടെ `.env` ഫയൽ സ്വയമേവ ലോഡ് ചെയ്യാൻ ലാഞ്ച് കോൺഫിഗറേഷൻ ഇതിനകം സജ്ജീകരിച്ചിരിക്കുന്നു.

> **പൂർണ്ണ ഉദാഹരണം**: വിശദമായ നിർദ്ദേശങ്ങൾക്കും പ്രശ്നപരിഹാരത്തിനും [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) കാണുക.

## അടുത്തത് എന്ത്?

**സജ്ജീകരണം പൂർത്തിയായി!** നിങ്ങൾക്ക് ഇപ്പോൾ:
- gpt-4o-mini ഡിപ്ലോയ് ചെയ്ത Azure OpenAI
- പ്രാദേശിക .env ഫയൽ ക്രമീകരണം
- Java ഡെവലപ്മെന്റ് പരിസ്ഥിതി സജ്ജമാണ്

**തുടരുക** [Chapter 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md) കാണാൻ, AI ആപ്ലിക്കേഷനുകൾ നിർമ്മിക്കാൻ ആരംഭിക്കുക!

## റിസോഴ്സുകൾ

- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI Documentation](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## അധിക റിസോഴ്സുകൾ

- [VS Code ഡൗൺലോഡ് ചെയ്യുക](https://code.visualstudio.com/Download)
- [Docker Desktop നേടുക](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**അറിയിപ്പ്**:  
ഈ പ്രമാണം AI വിവർത്തന സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് വിവർത്തനം ചെയ്തതാണ്. ഞങ്ങൾ കൃത്യതയ്ക്കായി ശ്രമിക്കുന്നുവെങ്കിലും, ഓട്ടോമേറ്റഡ് വിവർത്തനങ്ങളിൽ പിഴവുകൾ അല്ലെങ്കിൽ തെറ്റായ വിവരങ്ങൾ ഉണ്ടാകാൻ സാധ്യതയുണ്ട്. അതിന്റെ സ്വാഭാവിക ഭാഷയിലുള്ള യഥാർത്ഥ പ്രമാണം പ്രാമാണികമായ ഉറവിടമായി പരിഗണിക്കണം. നിർണായകമായ വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ വിവർത്തനം ശുപാർശ ചെയ്യുന്നു. ഈ വിവർത്തനം ഉപയോഗിച്ച് ഉണ്ടാകുന്ന തെറ്റിദ്ധാരണകൾക്കോ തെറ്റായ വ്യാഖ്യാനങ്ങൾക്കോ ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->