# Chat de bază cu Azure AI Foundry - Exemplu End-to-End

Acest exemplu este o aplicație simplă Spring Boot care se conectează la un model **Azure AI Foundry** folosind **autentificare fără cheie** (Microsoft Entra ID) și testează configurația dvs. Menține `ChatClient` din Spring AI, susținut de **SDK-ul oficial OpenAI Java** și endpoint-ul **Azure OpenAI v1**.

Versiunile din [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) sunt Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, și dotenv-java **3.2.0**. Exemplul folosește `spring-ai-starter-model-openai` și declară explicit `openai-java` și `azure-identity`; Spring AI 2 a eliminat vechiul starter Azure OpenAI.

## Cuprins

- [Prerechizite](#prerechizite)
- [Pornire rapidă](#pornire-rapidă)
- [Cum funcționează autentificarea](#cum-funcționează-autentificarea)
- [Rularea aplicației](#rularea-aplicației)
  - [Folosind Maven](#folosind-maven)
  - [Folosind VS Code](#folosind-vs-code)
  - [Rezultatul așteptat](#rezultatul-așteptat)
- [Referință configurație](#referință-configurație)
  - [Variabile de mediu](#variabile-de-mediu)
  - [Configurare Spring](#configurare-spring)
- [Depanare](#depanare)
  - [Probleme comune](#probleme-comune)
  - [Mod debug](#mod-debug)
- [Pași următori](#pași-următori)
- [Resurse](#resurse)

## Prerechizite

Înainte de a rula acest exemplu, asigurați-vă că aveți:

- O resursă Azure AI Foundry cu o implementare `gpt-5.6-luna` - provisionați-o cu `azd up` sau manual prin [ghidul de configurare Azure AI Foundry](../../getting-started-azure-openai.md)
- Rolul **Cognitive Services OpenAI User** pe acea resursă (șabloanele Bicep îl atribuie automat)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), autentificat cu `az login`
- Java 21+ și Maven 3.9+

> **Nu este necesară o cheie API** — autentificarea este fără cheie prin Microsoft Entra ID.

## Pornire rapidă

```bash
# 1. Navigați la proiect
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Autentificați-vă pentru ca autentificarea fără cheie să poată obține un token
az login

# 3. Configurați punctul final
#    - Dacă ați rulat `azd up`, .env a fost creat pentru dvs. (săriți peste acest pas).
#    - Altfel, copiați șablonul și setați AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Rulați aplicația
mvn spring-boot:run
```

## Cum funcționează autentificarea

Acest exemplu se autentifică folosind **Microsoft Entra ID** — nu există o cheie API.

Aplicația configurează autentificarea explicit în [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` creează un `BearerTokenCredential` folosind `AuthenticationUtil.getBearerTokenSupplier` cu `DefaultAzureCredential` și domeniul `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` construiește un `OpenAIClient` cu `OpenAIOkHttpClient.builder()`, rezolvă endpoint-ul resursei la `/openai/v1`, și furnizează credentialul bearer cu `.credential(...)`.
3. `azureChatModel()` furnizează clientul respectiv lui `OpenAiChatModel` din Spring AI, care susține `ChatClient` din lecție.

Aceste bean-uri explicite împiedică o cheie globală `OPENAI_API_KEY` să suprascrie autentificarea Azure. Omiterea cheii API doar din YAML nu configurează autentificarea. `DefaultAzureCredential` poate folosi sesiunea dvs. `az login` local sau o identitate gestionată în Azure; orice identitate selectată trebuie să aibă rolul resursei menționat mai sus.

## Rularea aplicației

### Folosind Maven

```bash
mvn spring-boot:run
```

### Folosind VS Code

1. Deschideți proiectul în VS Code
2. Apăsați `F5` sau utilizați panoul "Run and Debug"
3. Selectați configurația "Spring Boot-BasicChatApplication"

> **Notă**: Aplicația încarcă `.env` din directorul său de lucru, inclusiv când este lansată din VS Code.

### Rezultatul așteptat

Output ilustrativ după o rulare reușită (jurnalele de pornire sunt omise; formularea răspunsului poate varia):

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

## Referință configurație

### Variabile de mediu

| Variabilă | Descriere | Obligatoriu | Exemplu |
|----------|-------------|------------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL endpoint Foundry (Azure OpenAI) | Da | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Numele implementării modelului chat | Nu | `gpt-5.6-luna` (implicit) |

> Nu există variabilă pentru cheia API — autentificarea este fără cheie (Microsoft Entra ID prin `az login`).

### Configurare Spring

Setările din [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) folosesc prefixul `spring.ai.openai` și proprietăți chat simplificate (fără blocul `options`):

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

`model` este **numele implementării Azure**. Autentificarea provine din bean-urile explicite descrise mai sus, nu dintr-o setare `api-key`. Lecția dezactivează raționamentul și limitează tokenii de completare la 500; păstrează `temperature` și vechiul `max-tokens` neschimbate.

Microsoft recomandă [SDK-ul oficial OpenAI cu Azure OpenAI v1 și API-ul Responses pentru aplicații noi](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions rămâne suportat pentru această lecție veche bazată pe mesaje. Pentru GPT-5.6, cererile care includ instrumente pe Chat Completions trebuie să seteze `reasoning_effort` la `none`; folosiți Responses când combinați raționamentul cu instrumentele. Vezi [apelarea instrumentelor cu modele de raționament](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Depanare

### Probleme comune

<details>
<summary><strong>Eroare: 401 / "PermissionDenied" / erori token</strong></summary>

- Executați `az login` — autentificarea fără cheie necesită o autentificare activă pentru a obține un token
- Verificați că contul dvs. are rolul **Cognitive Services OpenAI User** pe resursă
- Dacă tocmai ați atribuit rolul, așteptați un minut pentru propagare
- Confirmați că sunteți în chiria/suscripția corectă (`az account show`)
</details>

<details>
<summary><strong>Eroare: "Endpoint-ul nu este valid" / erori de conexiune</strong></summary>

- Asigurați-vă că `AZURE_OPENAI_ENDPOINT` este URL-ul complet de bază (de ex., `https://your-resource.openai.azure.com/`)
- Verificați consistența slash-urilor finale
- Verificați dacă endpoint-ul corespunde resursei provisionate (`azd env get-values`)
</details>

<details>
<summary><strong>Eroare: "Implementarea nu a fost găsită"</strong></summary>

- Verificați că `AZURE_OPENAI_DEPLOYMENT` corespunde unui nume de implementare în Azure
- Verificați că modelul este implementat cu succes și activ
- Numele implicit al implementării este `gpt-5.6-luna`
</details>

<details>
<summary><strong>Eroare: 429 / depășirea limitei de rată</strong></summary>

- Implementarea implicită GPT-5.6 Luna are Capacitate Standard Globală 10: 10 cereri/minut și 10.000 tokeni/minut
- Rulați exemplele secvențial și așteptați intervalul de retry al serviciului înainte de a reîncerca
- Acest exemplu simplu dezactivează retry-urile automate SDK, deci o cerere eșuată este raportată direct
</details>

<details>
<summary><strong>VS Code: Variabilele de mediu nu se încarcă</strong></summary>

- Asigurați-vă că fișierul `.env` este în directorul rădăcină al proiectului (la același nivel cu `pom.xml`)
- Încercați să rulați `mvn spring-boot:run` în terminalul integrat VS Code
- Verificați dacă extensia Java pentru VS Code este instalată corect
</details>

### Mod debug

Pentru a activa jurnalizarea detaliată, decomentați aceste linii în [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Pași următori

**Configurare completă!** Continuați-vă călătoria de învățare:

[Capitolul 3: Tehnici principale de AI Generativ](../../../03-CoreGenerativeAITechniques/README.md)

## Resurse

- [Trecerea la OpenAI Java SDK în Spring AI 2](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK oficial OpenAI Java cu Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentificare fără cheie cu Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Documentație Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->