# Základní chat s Azure AI Foundry - příklad od začátku do konce

Tento příklad je jednoduchá aplikace Spring Boot, která se připojuje k modelu **Azure AI Foundry** pomocí **autentizace bez klíče** (Microsoft Entra ID) a testuje vaše nastavení. Používá `ChatClient` Spring AI, podporovaný **oficiálním OpenAI Java SDK** a koncovým bodem **Azure OpenAI v1**.

Verze v [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) jsou Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** a dotenv-java **3.2.0**. Ukázka používá `spring-ai-starter-model-openai` a explicitně deklaruje `openai-java` a `azure-identity`; Spring AI 2 odstranil starý Azure OpenAI starter.

## Obsah

- [Požadavky](#požadavky)
- [Rychlý start](#rychlý-start)
- [Jak funguje autentizace](#jak-funguje-autentizace)
- [Spuštění aplikace](#spuštění-aplikace)
  - [Použití Maven](#použití-maven)
  - [Použití VS Code](#použití-vs-code)
  - [Očekávaný výstup](#očekávaný-výstup)
- [Reference konfigurace](#reference-konfigurace)
  - [Proměnné prostředí](#proměnné-prostředí)
  - [Konfigurace Spring](#konfigurace-spring)
- [Řešení problémů](#řešení-problémů)
  - [Běžné problémy](#běžné-problémy)
  - [Debug režim](#debug-režim)
- [Další kroky](#další-kroky)
- [Zdroje](#zdroje)

## Požadavky

Před spuštěním tohoto příkladu si zajistěte:

- Zdroj Azure AI Foundry s nasazením `gpt-5.6-luna` - zprovozněte jej pomocí `azd up` nebo ručně podle [průvodce nastavením Azure AI Foundry](../../getting-started-azure-openai.md)
- Role **Cognitive Services OpenAI User** na tomto zdroji (v Bicep šablonách je tato role přidělena automaticky)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), přihlášený pomocí `az login`
- Java 21+ a Maven 3.9+

> **Žádný API klíč není potřeba** — autentizace je bezklíčová přes Microsoft Entra ID.

## Rychlý start

```bash
# 1. Přejděte do projektu
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Přihlaste se, aby keyless autentizace mohla získat token
az login

# 3. Nakonfigurujte koncový bod
#    - Pokud jste spustili `azd up`, soubor .env byl vytvořen automaticky (přeskočte tuto část).
#    - Jinak zkopírujte šablonu a nastavte AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Spusťte aplikaci
mvn spring-boot:run
```

## Jak funguje autentizace

Tento příklad používá autentizaci s **Microsoft Entra ID** — není zde žádný API klíč.

Aplikace explicitně nastavuje autentizaci v [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` vytváří `BearerTokenCredential` pomocí `AuthenticationUtil.getBearerTokenSupplier` s `DefaultAzureCredential` a rozsahem `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` sestavuje `OpenAIClient` s `OpenAIOkHttpClient.builder()`, nastaví koncový bod zdroje na `/openai/v1` a předává pověření Bearer pomocí `.credential(...)`.
3. `azureChatModel()` předává tohoto klienta Spring AI `OpenAiChatModel`, který podporuje `ChatClient` tohoto příkladu.

Tyto explicitní definice beanů zabrání, aby globální `OPENAI_API_KEY` přepsal Azure autentizaci. Jen vynechání API klíče v YAML není autentizační nastavení. `DefaultAzureCredential` může použit vaši `az login` relaci lokálně nebo spravovanou identitu v Azure; jakákoli vybraná identita musí mít výše uvedenou roli ke zdroji.

## Spuštění aplikace

### Použití Maven

```bash
mvn spring-boot:run
```

### Použití VS Code

1. Otevřete projekt ve VS Code
2. Stiskněte `F5` nebo použijte panel "Run and Debug"
3. Vyberte konfiguraci "Spring Boot-BasicChatApplication"

> **Poznámka**: Aplikace načítá `.env` ze svého pracovního adresáře, i když je spuštěna z VS Code.

### Očekávaný výstup

Ilustrační výstup po úspěšném spuštění (startovací logy vynechány; formulace odpovědí se může lišit):

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

## Reference konfigurace

### Proměnné prostředí

| Proměnná | Popis | Vyžadováno | Příklad |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL koncového bodu Foundry (Azure OpenAI) | Ano | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Název nasazení chatovacího modelu | Ne | `gpt-5.6-luna` (výchozí) |

> Neexistuje žádná proměnná API klíče — autentizace probíhá bezklíčově (Microsoft Entra ID přes `az login`).

### Konfigurace Spring

Nastavení v [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) používá předponu `spring.ai.openai` a zploštěné vlastnosti chatu (bez bloku `options`):

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

`model` je **název nasazení Azure**. Autentizace vychází z výše popsaných explicitních beanů, ne z nastavení `api-key`. Lekce zakazuje reasoning a omezuje počet tokenů dokončení na 500; nechává `temperature` a starý `max-tokens` nenastavené.

Microsoft doporučuje [oficiální OpenAI SDK s Azure OpenAI v1 a API Responses pro nové aplikace](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions zůstává podporováno pro tuto existující lekci založenou na zprávách. Pro GPT-5.6 žádosti obsahující nástroje na Chat Completions musí nastavit `reasoning_effort` na `none`; při kombinaci reasoning s nástroji používejte Responses. Viz [volání nástrojů s reasoning modely](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Řešení problémů

### Běžné problémy

<details>
<summary><strong>Chyba: 401 / "PermissionDenied" / chyby tokenu</strong></summary>

- Spusťte `az login` — autentizace bez klíče potřebuje aktivní přihlášení pro získání tokenu
- Ověřte, že váš účet má roli **Cognitive Services OpenAI User** na zdroji
- Pokud jste právě přiřadili roli, počkejte chvíli na její propagaci
- Potvrďte, že jste ve správném tenantovi/předplatném (`az account show`)
</details>

<details>
<summary><strong>Chyba: "The endpoint is not valid" / chyby připojení</strong></summary>

- Ujistěte se, že `AZURE_OPENAI_ENDPOINT` je úplná základní URL (např. `https://your-resource.openai.azure.com/`)
- Zkontrolujte konzistenci závěrečného lomítka
- Ověřte, že koncový bod odpovídá vašemu nasazenému zdroji (`azd env get-values`)
</details>

<details>
<summary><strong>Chyba: "The deployment was not found"</strong></summary>

- Ověřte, že `AZURE_OPENAI_DEPLOYMENT` odpovídá názvu nasazení v Azure
- Zkontrolujte, že je model úspěšně nasazen a aktivní
- Výchozí název nasazení je `gpt-5.6-luna`
</details>

<details>
<summary><strong>Chyba: 429 / překročena rychlostní hranice</strong></summary>

- Výchozí nasazení GPT-5.6 Luna má Globální Standard kapacitu 10: 10 požadavků/minutu a 10 000 tokenů/minutu
- Spouštějte příklady sekvenčně a počkejte na interval opakování služby před dalším pokusem
- Tento základní příklad zakazuje automatické opakování SDK, takže neúspěšný požadavek je hlášen přímo
</details>

<details>
<summary><strong>VS Code: Proměnné prostředí se nenačítají</strong></summary>

- Ujistěte se, že soubor `.env` je v kořenovém adresáři projektu (na stejné úrovni jako `pom.xml`)
- Zkuste spustit `mvn spring-boot:run` v integrovaném terminálu VS Code
- Zkontrolujte, zda je správně nainstalováno rozšíření Java pro VS Code
</details>

### Debug režim

Pro podrobné logování odkomentujte tyto řádky v [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Další kroky

**Nastavení dokončeno!** Pokračujte ve svém vzdělávání:

[Kapitola 3: Základní techniky generativní AI](../../../03-CoreGenerativeAITechniques/README.md)

## Zdroje

- [Přechod Spring AI 2 na OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficiální OpenAI Java SDK s Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentizace bez klíče s Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portál Azure AI Foundry](https://ai.azure.com/)
- [Dokumentace Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o co největší přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->