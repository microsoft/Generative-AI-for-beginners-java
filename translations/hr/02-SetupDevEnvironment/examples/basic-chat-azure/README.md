# Osnovni chat s Azure AI Foundry - Primjer od početka do kraja

Ovaj primjer je jednostavna Spring Boot aplikacija koja se povezuje na **Azure AI Foundry** model koristeći **autentifikaciju bez ključa** (Microsoft Entra ID) i testira vašu konfiguraciju. Koristi Spring AI `ChatClient`, podržan od strane **službenog OpenAI Java SDK** i **Azure OpenAI v1** krajnje točke.

Verzije u [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) su Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** i dotenv-java **3.2.0**. Primjer koristi `spring-ai-starter-model-openai` i eksplicitno deklarira `openai-java` i `azure-identity`; Spring AI 2 je uklonio stari Azure OpenAI starter.

## Sadržaj

- [Preduvjeti](#preduvjeti)
- [Brzi početak](#brzi-početak)
- [Kako funkcionira autentifikacija](#kako-funkcionira-autentifikacija)
- [Pokretanje aplikacije](#pokretanje-aplikacije)
  - [Korištenje Mavena](#korištenje-mavena)
  - [Korištenje VS Code-a](#korištenje-vs-code-a)
  - [Očekivani izlaz](#očekivani-izlaz)
- [Referenca konfiguracije](#referenca-konfiguracije)
  - [Varijable okoline](#varijable-okoline)
  - [Spring konfiguracija](#spring-konfiguracija)
- [Rješavanje problema](#rješavanje-problema)
  - [Česti problemi](#česti-problemi)
  - [Način otklanjanja pogrešaka](#način-otklanjanja-pogrešaka)
- [Sljedeći koraci](#sljedeći-koraci)
- [Resursi](#resursi)

## Preduvjeti

Prije pokretanja ovog primjera, osigurajte da imate:

- Azure AI Foundry resurs s `gpt-5.6-luna` implementacijom - postavite ga pomoću `azd up` ili ručno preko [Azure AI Foundry vodiča za postavljanje](../../getting-started-azure-openai.md)
- Ulogu **Cognitive Services OpenAI User** na tom resursu (Bicep predlošci to dodjeljuju za vas)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), prijavljen s `az login`
- Java 21+ i Maven 3.9+

> **Nije potreban API ključ** — autentifikacija je bez ključa putem Microsoft Entra ID-a.

## Brzi početak

```bash
# 1. Navigirajte do projekta
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Prijavite se kako bi keyless autentikacija mogla dobiti token
az login

# 3. Konfigurirajte krajnju točku
#    - Ako ste pokrenuli `azd up`, .env je već napisan za vas (preskočite ovo).
#    - U suprotnom kopirajte predložak i postavite AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Pokrenite aplikaciju
mvn spring-boot:run
```

## Kako funkcionira autentifikacija

Ovaj primjer koristi autentifikaciju s **Microsoft Entra ID-om** — nema API ključa.

Aplikacija eksplicitno konfigurira autentifikaciju u [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` stvara `BearerTokenCredential` koristeći `AuthenticationUtil.getBearerTokenSupplier` s `DefaultAzureCredential` i `https://ai.azure.com/.default` područjem.
2. `azureOpenAiClient()` gradi `OpenAIClient` s `OpenAIOkHttpClient.builder()`, rješava krajnju točku resursa na `/openai/v1`, i pruža bearer credential s `.credential(...)`.
3. `azureChatModel()` pruža taj klijent Spring AI-jevom `OpenAiChatModel`, koji podržava `ChatClient` u ovom primjeru.

Ovi eksplicitni bean-ovi sprječavaju globalni `OPENAI_API_KEY` da nadjača Azure autentifikaciju. Izostavljanje API ključa samo iz YAML datoteke nije konfiguracija autentifikacije. `DefaultAzureCredential` može koristiti vašu lokalnu `az login` sesiju ili upravljani identitet u Azure-u; identitet koji se koristi mora imati gore navedenu ulogu na resursu.

## Pokretanje aplikacije

### Korištenje Mavena

```bash
mvn spring-boot:run
```

### Korištenje VS Code-a

1. Otvorite projekt u VS Code-u
2. Pritisnite `F5` ili koristite panel "Run and Debug"
3. Odaberite konfiguraciju "Spring Boot-BasicChatApplication"

> **Napomena**: Aplikacija učitava `.env` iz radnog direktorija, uključujući i pokretanje iz VS Code-a.

### Očekivani izlaz

Primjer izlaza nakon uspješnog pokretanja (logovi pokretanja izostavljeni; tekstualni odgovor može varirati):

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

## Referenca konfiguracije

### Varijable okoline

| Varijabla | Opis | Obavezno | Primjer |
|----------|--------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) URL krajnje točke | Da | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Naziv implementacije chat modela | Ne | `gpt-5.6-luna` (zadano) |

> Ne postoji varijabla za API ključ — autentifikacija je bez ključa (Microsoft Entra ID putem `az login`).

### Spring konfiguracija

Postavke u [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) koriste prefiks `spring.ai.openai` i spljoštene chat postavke (bez `options` bloka):

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

`model` je **naziv Azure implementacije**. Autentifikacija dolazi iz eksplicitnih bean-ova opisanih iznad, ne iz postavke `api-key`. Lekcija onemogućuje rezoniranje i ograničava tokene dovršetka na 500; ostavlja `temperature` i legacy `max-tokens` nepostavljeno.

Microsoft preporučuje [službeni OpenAI SDK s Azure OpenAI v1 i Responses API za nove aplikacije](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions ostaje podržan za ovu postojeću lekciju temeljenu na porukama. Za GPT-5.6, zahtjevi koji uključuju alate u Chat Completions moraju postaviti `reasoning_effort` na `none`; koristite Responses za kombiniranje rezoniranja s alatima. Pogledajte [pozivanje alata s modelima za rezoniranje](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Rješavanje problema

### Česti problemi

<details>
<summary><strong>Greška: 401 / "PermissionDenied" / pogreške tokena</strong></summary>

- Pokrenite `az login` — autentifikacija bez ključa zahtijeva aktivnu prijavu za dobivanje tokena
- Provjerite ima li vaš račun ulogu **Cognitive Services OpenAI User** na resursu
- Ako ste upravo dodijelili ulogu, pričekajte minutu da se propagira
- Potvrdite da ste na pravom tenant/subscription (`az account show`)
</details>

<details>
<summary><strong>Greška: "The endpoint is not valid" / problemi s povezivanjem</strong></summary>

- Provjerite `AZURE_OPENAI_ENDPOINT` da bude cijeli osnovni URL (npr. `https://your-resource.openai.azure.com/`)
- Provjerite dosljednost završnog kosa crta
- Potvrdite da krajnja točka odgovara vašem postavljenom resursu (`azd env get-values`)
</details>

<details>
<summary><strong>Greška: "The deployment was not found"</strong></summary>

- Provjerite da `AZURE_OPENAI_DEPLOYMENT` odgovara imenu implementacije u Azure-u
- Provjerite da je model uspješno implementiran i aktivan
- Zadano ime implementacije je `gpt-5.6-luna`
</details>

<details>
<summary><strong>Greška: 429 / prekoračenje ograničenja brzine</strong></summary>

- Zadana GPT-5.6 Luna implementacija ima Global Standard kapacitet 10: 10 zahtjeva/minuta i 10,000 tokena/minuta
- Pokrenite primjere sekvencijalno i pričekajte interval ponovne provjere prije ponovnog pokušaja
- Ovaj osnovni primjer onemogućava automatske SDK ponovne pokušaje, pa se neuspjeli zahtjev dohvaća odmah kao greška
</details>

<details>
<summary><strong>VS Code: Varijable okoline se ne učitavaju</strong></summary>

- Provjerite da je vaša `.env` datoteka u korijenu projekta (na istoj razini kao `pom.xml`)
- Pokušajte pokrenuti `mvn spring-boot:run` u integriranom terminalu VS Code-a
- Provjerite je li VS Code Java ekstenzija ispravno instalirana
</details>

### Način otklanjanja pogrešaka

Da biste omogućili detaljno zapisivanje, otkomentirajte ove retke u [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Sljedeći koraci

**Postavljanje dovršeno!** Nastavite svoje učiteljstvo:

[Poglavlje 3: Osnovne tehnike generativne umjetne inteligencije](../../../03-CoreGenerativeAITechniques/README.md)

## Resursi

- [Prijelaz Spring AI 2 na OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Službeni OpenAI Java SDK s Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentifikacija bez ključa s Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Dokumentacija za Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati greške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->