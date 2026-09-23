# അസ്യൂർ എഐ ഫൗണ്ട്രീയുമായി അടിസ്ഥാനചാറ്റ് - അവസാനം മുതൽ അവസാനം വരെ ഉദാഹരണം

ഈ ഉദാഹരണം ഒരു ലളിതമായ Spring Boot അപേക്ഷയാണ്, ഇത് **അസ്യൂർ എഐ ഫൗണ്ട്രീ** മോഡലുമായി **കീയ്ലെസ് ഓതന്റിക്കേഷൻ** (Microsoft Entra ID) ഉപയോഗിച്ച് ബന്ധിപ്പിച്ച് നിങ്ങളുടെ സെറ്റപ്പിന് പരിശോധന നടത്തുന്നു. ഇത് Spring AIയുടെ `ChatClient` നെ സംരക്ഷിക്കുന്നു, ഇത് **അധികൃത OpenAI Java SDK**യും **അസ്യൂർ OpenAI v1** എന്റ്പോയിന്റും പിന്തുണയ്ക്കുന്നു.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)ൽപയോഗിക്കുന്ന പതിപ്പുകൾ ആണ് Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, dotenv-java **3.2.0**. ഇവിടെ `spring-ai-starter-model-openai` ഉപയോഗിക്കുന്നു, കൂടാതെ ശരിയായി `openai-java`യും `azure-identity`യും പ്രഖ്യാപിച്ചിരിക്കുന്നു; Spring AI 2 പഴയ അസ്യൂർ OpenAI സ്റ്റാർട്ടർ നീക്കം ചെയ്തു.

## ഉള്ളടക്ക പട്ടിക

- [ആവശ്യകതകൾ](#ആവശ്യകതകൾ)
- [ശീഘ്രാരംഭം](#ഷീഘ്രാരംഭം)
- [ഓതന്റിക്കേഷൻ എങ്ങനെ പ്രവർത്തിക്കുന്നു](#ഓതന്റിക്കേഷൻ-എങ്ങനെ-പ്രവർത്തിക്കുന്നു)
- [അപേക്ഷ പ്രവർത്തിപ്പിക്കൽ](#അപേക്ഷ-പ്രവർത്തിപ്പിക്കൽ)
  - [Maven ഉപയോഗിച്ച്](#maven-ഉപയോഗിച്ച്)
  - [VS കോഡ് ഉപയോഗിച്ച്](#vs-കോഡ്-ഉപയോഗിച്ച്)
  - [പ്രതീക്ഷിക്കാവുന്ന ഫലങ്ങൾ](#പ്രതീക്ഷിക്കാവുന്ന-ഫലങ്ങൾ)
- [ക്രമീകരണ സൂചിക](#ക്രമീകരണ-സൂചിക)
  - [പരിസ്ഥിതി മാറ്റികൾ](#പരിസ്ഥിതി-മാറ്റികൾ)
  - [Spring ക്രമീകരണം](#spring-ക്രമീകരണം)
- [പ്രശ്‌നം പരിഹാരം](#പ്രശ്‌നം-പരിഹാരം)
  - [പൊതുവായ പ്രശ്‌നങ്ങൾ](#പൊതുവായ-പ്രശ്‌നങ്ങൾ)
  - [ഡീബഗ്മോഡ്](#ഡീബഗ്-മോഡ്)
- [അടുത്ത ഘട്ടങ്ങൾ](#അടുത്തഘട്ടങ്ങൾ)
- [സ്രോതസുകൾ](#വിഭവങ്ങൾ)

## ആവശ്യകതകൾ

ഈ ഉദാഹരണം പ്രവർത്തിപ്പിക്കുന്നതിന് മുൻപ്, നിങ്ങൾക്കുണ്ടാവേണ്ടത്:

- `gpt-5.6-luna` ഡീപ്ലോയ്‌മെന്റ് ഉള്ള അസ്യൂർ എഐ ഫൗണ്ട്രീ വിഭവം - `azd up` ഉപയോഗിച്ച് പ്രൊവിഷൻ ചെയ്യുക അല്ലെങ്കിൽ [അസ്യൂർ എഐ ഫൗണ്ട്രീ സജ്ജീകരണ മാർഗ്ഗനിർദ്ദേശം](../../getting-started-azure-openai.md) വഴി മാനുവലായി ചെയ്യുക
- ആ വിഭവത്തിൽ **കോഗ്നിറ്റീവ് സർവീസസ് OpenAI ഉപയോക്തൃ** പദവി (Bicep ടെംപ്ലേറ്റുകൾ നിങ്ങൾക്കായി ഇത് വിനിയോഗിക്കും)
- [അസ്യൂർ CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login` ഉപയോഗിച്ച് സൈൻ ഇൻ ചെയ്തു
- Java 21+ மற்றும் Maven 3.9+

> **API കി ആവശ്യമില്ല** — ഓതന്റിക്കേഷൻ കീയ്ലെസ് ആണ് Microsoft Entra ID വഴി.

## ഷീഘ്രാരംഭം

```bash
# 1. പ്രോജക്റ്റിലേക്ക് നാവിഗേറ്റ് ചെയ്യുക
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. കീലെസ്സ് ഓതിനെ ടോക്കൺ ലഭിക്കാനായി സൈൻ ഇൻ ചെയ്യുക
az login

# 3. എൻഡ്‌പോയിന്റ് കോൺഫിഗർ ചെയ്യുക
#    - `azd up` റൺ ചെയ്തവയർക്ക്, .env നിങ്ങൾക്കായി എഴുതിയിട്ടുണ്ട് (ഇത് ഒഴിവാക്കുക).
#    - അല്ലെങ്കിൽ ടെംപ്ലേറ്റ് കാപ്പി ചെയ്ത് AZURE_OPENAI_ENDPOINT സെറ്റ് ചെയ്യുക:
cp .env.example .env

# 4. അപേക്ഷ നടത്തുക
mvn spring-boot:run
```

## ഓതന്റിക്കേഷൻ എങ്ങനെ പ്രവർത്തിക്കുന്നു

ഈ ഉദാഹരണം **Microsoft Entra ID** ഉപയോഗിച്ച് ഓതന്റിക്കേറ്റ് ചെയ്യുന്നു — API കീ ഇല്ല.

അപേക്ഷ [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) ൽ ഓതന്റിക്കേഷൻ വ്യക്തമാക്കുന്നു:

1. `azureCredential()` `AuthenticationUtil.getBearerTokenSupplier` ഉപയോഗിച്ച് `BearerTokenCredential` സൃഷ്ടിക്കുന്നു, `DefaultAzureCredential` ഒപ്പം `https://ai.azure.com/.default` സ്കോപ്പിൽ.
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()` ഉപയോഗിച്ച് ഒരു `OpenAIClient` നിർമ്മിക്കുന്നു, വിഭവം എന്റ്പോയിന്റ് `/openai/v1` ആയി പരിഹരിക്കുന്നു, ശേഷം ബെയറർ ക്രെഡൻഷ്യൽ നൽകി `.credential(...)`.
3. `azureChatModel()` ആ ക്ലയന്റിന് Spring AIയുടെ `OpenAiChatModel` നൽകുന്നു, ഇത് പാഠത്തിലെ `ChatClient` നെ പിന്തുണയ്ക്കുന്നു.

ഈ വ്യക്തമായ ബീൻസുകൾ ഒരു ആഗോള `OPENAI_API_KEY` അസ്യൂർ ഓതന്റിക്കേഷൻ മറയ്ക്കുന്നത് തടയുന്നു. YAML ൽനിന്ന് API കീ ഒഴിവാക്കുന്നതു മാത്രമല്ല ഓതന്റിക്കേഷൻ ക്രമീകരണം. `DefaultAzureCredential` നിങ്ങളുടെ പ്രാദേശിക `az login` സെഷൻ അല്ലെങ്കിൽ അസ്യൂറിൽ മാനേജ്ഡ് ഐഡന്റിറ്റി ഉപയോഗിച്ചേക്കാം; തിരഞ്ഞെടുക്കപ്പെട്ട ഐഡന്റിറ്റിക്ക് മുകളിൽ നൽകിയ വിഭവ പദവി ഉണ്ടായിരിക്കണം.

## അപേക്ഷ പ്രവർത്തിപ്പിക്കൽ

### Maven ഉപയോഗിച്ച്

```bash
mvn spring-boot:run
```

### VS കോഡ് ഉപയോഗിച്ച്

1. პროექტ് VS കോഡ്-ൽ തുറക്കുക
2. `F5` അമർത്തുക അല്ലെങ്കിൽ "Run and Debug" പാനൽ ഉപയോഗിക്കുക
3. "Spring Boot-BasicChatApplication" കോൺഫിഗറേഷൻ തിരഞ്ഞെടുക്കുക

> **കുറിപ്പ്**: അപേക്ഷയ്ക്ക് പ്രവർത്തന ഡയറക്ടറിയിൽ നിന്ന് `.env` ലോഡ് ചെയ്യും, VS കോഡ് മുതൽക്കൂട്ടുമ്പോഴും.

### പ്രതീക്ഷിക്കാവുന്ന ഫലങ്ങൾ

വിജയകരമായി പ്രവർത്തിച്ച ശേഷം ഉദാഹരണസഹിത ഫലം (സ്റ്റാർട്ട്-അപ്പ് ലോഗുകൾ ഒഴിവാക്കി; പ്രതികരണ വാക്കുകൾ വ്യത്യാസപ്പെടാം):

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

## ക്രമീകരണ സൂചിക

### പരിസ്ഥിതി മാറ്റികൾ

| മാറ്റി | വിവരണം | ആവശ്യമുള്ളത് | ഉദാഹരണം |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | ഫൗണ്ട്രീ (അസ്യൂർ OpenAI) എന്റ്പോയിൻറ് URL | അതെ | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | ചാറ്റ് മോഡൽ ഡീപ്ലോയ്‌മെന്റ് പേര് | ഇല്ല | `gpt-5.6-luna` (ഉപാധിയാനുസരിച്ചു) |

> **API കീ പ്രമാണം ഇല്ല** — ഓതന്റിക്കേഷൻ കീയ്ലെസ് ആണ് (Microsoft Entra ID `az login` വഴി).

### Spring ക്രമീകരണം

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ക്രമീകരണങ്ങൾ `spring.ai.openai` പ്രിഫിക്സ് ഉപയോഗിക്കുന്നു, ഫ്ലാറ്റൻ ചെയ്ത ചാറ്റ് പ്രോപ്പർട്ടികൾ ഉപയോഗിക്കുന്നു (`options` ബ്ലോക്ക് ഇല്ല):

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

`model` ആണ് **അസ്യൂർ ഡീപ്ലോയ്‌മെന്റ് പേര്**. ഓതന്റിക്കേഷൻ മുകളിൽ വിവരിച്ച വ്യക്തമായ ബീൻസുകളിൽ നിന്നാണ്; `api-key` ക്രമീകരണം ഇല്ല. പാഠം റീസണിംഗ് നിർത്തുകയും പൂർത്തീകരണ ടോക്കൺ പരിധി 500-ൽ കമിറ്റ് ചെയ്യുകയും ചെയ്യുന്നു; `temperature`യും പഴയ `max-tokens`-ഉം ഒഴിവാക്കിയിരിക്കുന്നു.

Microsoft പുതിയ അപേക്ഷകൾക്ക് [അധികൃത OpenAI SDK അസ്യൂർ OpenAI v1 ഒപ്പം Responses API ഉപയോഗിച്ച്](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java) ശുപാർശ ചെയ്യുന്നു. ഈ നിലവിലുള്ള സന്ദേശ-അധിഷ്ഠിത പാഠത്തിന് Chat Completions പിന്തുണ തുടരുന്നു. GPT-5.6-നുള്ളിൽ ടൂളുകൾ ഉൾപ്പെടുന്ന നിര്‍ബന്ധങ്ങള്‍ക്ക് `reasoning_effort` `none` ആക്കണം; റീസണിംഗ് ടൂളുകളുമായി സംയോജിപ്പിക്കുമ്പോൾ Responses ഉപയോഗിക്കുക. [ടൂൾ കോലിങ് റീസണിംഗ് മോഡലുകളുമായി][https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models] കാണുക.

## പ്രശ്‌നം പരിഹാരം

### പൊതുവായ പ്രശ്‌നങ്ങൾ

<details>
<summary><strong> പിഴവ്: 401 / "PermissionDenied" / ടോക്കൺ പിഴവുകൾ</strong></summary>

- `az login` ഓടിക്കുക — കീയ്ലെസ് ഓതന്റ്‌റ്റിക്കേഷനുള്ള ഒരു സജീവ സൈൻ-ഇൻ ടോക്കൺ ലഭിക്കുന്നതിന്
- നിങ്ങളുടെ അക്കൗണ്ടിന് ആ വിഭവത്തിൽ **കോഗ്നിറ്റീവ് സർവീസസ് OpenAI ഉപയോക്തൃ** പദവി ഉണ്ടെന്ന് ഉറപ്പാക്കുക
- നിങ്ങൾ ഇപ്പോൾ പദവി അനുവദിച്ചെങ്കിൽ, അത് പരത്താൻ ഒരു മിനിറ്റ് കാത്തിരിക്കുക
- നിങ്ങൾ ശരിയായ ടെനന്റ്/സബ്സ്ക്ക്രിപ്‌ഷനിൽ ഉണ്ടെന്ന് ഉറപ്പിക്കൂ (`az account show`)
</details>

<details>
<summary><strong> പിഴവ്: "എന്റ്പോയിന്റ് സാധുവല്ല" / കണക്ഷൻ പിഴവുകൾ</strong></summary>

- `AZURE_OPENAI_ENDPOINT` പൂർണ്ണ അടിസ്ഥാന URL ആണെന്ന് ഉറപ്പാക്കുക (ഉദാഹരണം, `https://your-resource.openai.azure.com/`)
- ഫയൽ മാറുന്നതിൽ സ്ലാഷ് ചേർക്കലിന്റെ സ്ഥിരത പരിശോധിക്കുക
- എൻപോയിന്റ് നിങ്ങൾ നൽകിയ വിഭവത്തിനൊപ്പം പൊരുത്തപ്പെടുന്നതായി ഉറപ്പ് വരുത്തുക (`azd env get-values`)
</details>

<details>
<summary><strong> പിഴവ്: "ഡീപ്ലോയ്‌മെന്റ് കണ്ടെത്താനായില്ല"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` അസ്യൂറിലുള്ള ഡീപ്ലോയ്‌മെന്റ് പേരുമായി പൊരുത്തപ്പെടുന്നു എന്ന് പരിശോധിക്കുക
- മോഡൽ വിജയകരമായി ഡീപ്ലോയുചെയ്തിട്ടും സജീവമാണെന്ന് ഉറപ്പാക്കുക
- പൂർവ്വനിർണയമായ വിനിയോഗ നാമം `gpt-5.6-luna` ആണ്
</details>

<details>
<summary><strong>പിഴവ്: 429 / നിരക്ക് പരിധി മറികടന്നു</strong></summary>

- പൂർവ്വനിർണയമായ GPT-5.6 Luna വിനിയോഗത്തിന് ഗ്ലോബൽ സ്റ്റാൻഡേർഡ് ശേഷി 10 ഉണ്ട്: 10 അഭ്യർത്ഥനകൾ/മിനിറ്റ്, 10,000 ടോക്കണുകൾ/മിനിറ്റ്
- ഉദാഹരണങ്ങൾ ക്രമաբար പ്രവർത്തിപ്പിക്കുക, പിന്നീട് സർവീസിന്റെ പുനരുപയോഗ അവധി പ്രമാണിച്ച് വീണ്ടും ശ്രമിക്കുക
- ഈ അടിസ്ഥാന ഉദാഹരണം സ്വയമേവ SDK പുനരുപയോഗം غیرസജ്ജമാക്കുന്നു, അതിനാൽ പരാജയപ്പെട്ട അഭ്യർത്ഥന നേരിട്ട് റിപ്പോർട്ട് ചെയ്യപ്പെടും
</details>

<details>
<summary><strong>VS കോഡ്: പരിസ്ഥിതി ചാരികകൾ ലോഡ് ആവുന്നില്ല</strong></summary>

- നിങ്ങളുടെ `.env` ഫയൽ പ്രോജക്ട് റൂട് ഡയറക്ടറിയിലാണ് (pom.xml നെ സമാനമായ നിലയിലുള്ളത്)
- `mvn spring-boot:run` VS കോഡിന്റെ സമന്വയിപ്പിച്ച ടർമിനലിൽ പ്രവർത്തിപ്പിക്കാൻ ശ്രമിക്കുക
- VS കോഡ് ജാവാ വിപുലീകരണം ശരിയായി ഇൻസ്റ്റാൾ ചെയ്തിട്ടുണ്ടോ എന്നത് പരിശോധിക്കുക
</details>

### ഡീബഗ് മോഡ്

വിശദമായ ലോഗിംഗ് എനേബിൾ ചെയ്യാൻ, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) യിലെ ഈ വരികൾ അൺകോമന്റ് ചെയ്യുക:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## അടുത്തഘട്ടങ്ങൾ

**സജ്ജീകരണം പൂർത്തിയായി!** നിങ്ങളുടെ പഠന യാത്ര തുടരാം:

[അദ്ധ്യായം 3: കോർ ജനറേറ്റീവ് AI സാങ്കേതികതകൾ](../../../03-CoreGenerativeAITechniques/README.md)

## വിഭവങ്ങൾ

- [Spring AI 2 OpenAI ജാവാ SDK മാറൽ](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [ഓഫീഷ്യൽ OpenAI ജാവാ SDK Azure OpenAI v1-ഉം](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID ഉപയോഗിച്ച് കീലെസ് അത്താഴം](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry പോർട്ടൽ](https://ai.azure.com/)
- [Azure AI Foundry ഡോക്യുമെൻറ്റേഷൻ](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**അറിയിപ്പ്**:
ഈ രേഖ AI പരിഭാഷാ സേവനം [Co-op Translator](https://github.com/Azure/co-op-translator) ഉപയോഗിച്ച് പരിഭാഷപ്പെടുത്തിയതാണ്. ഞങ്ങൾ കൃത്യതയ്ക്കായി ശ്രമിക്കുന്നുവെങ്കിലും, ഓട്ടോമേറ്റഡ് പരിഭാഷകളിൽ പിഴവുകൾ അല്ലെങ്കിൽ തെറ്റായ വിവരങ്ങൾ ഉണ്ടാകാൻ സാധ്യതയുണ്ട്. അതിന്റെ സ്വാഭാവിക ഭാഷയിലുള്ള അസൽ രേഖയാണ് പ്രാമാണികമായ ഉറവിടമായി പരിഗണിക്കേണ്ടത്. നിർണായകമായ വിവരങ്ങൾക്ക്, പ്രൊഫഷണൽ മനുഷ്യ പരിഭാഷ ശുപാർശ ചെയ്യുന്നു. ഈ പരിഭാഷ ഉപയോഗിച്ച് ഉണ്ടാകുന്ന തെറ്റിദ്ധാരണകൾ അല്ലെങ്കിൽ തെറ്റായ വ്യാഖ്യാനങ്ങൾക്കായി ഞങ്ങൾ ഉത്തരവാദികളല്ല.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->