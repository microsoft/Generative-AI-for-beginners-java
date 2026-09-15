# Základný chat s Azure AI Foundry - príklad end-to-end

Tento príklad je jednoduchá aplikácia Spring Boot, ktorá sa pripája k modelu **Azure AI Foundry** pomocou **autentifikácie bez kľúča** (Microsoft Entra ID) a testuje vaše nastavenie. Používa Spring AI `ChatClient`, podporovaný **oficiálnym OpenAI Java SDK** a koncovým bodom **Azure OpenAI v1**.

Verzie v [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) sú Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** a dotenv-java **3.2.0**. Ukážka používa `spring-ai-starter-model-openai` a explicitne deklaruje `openai-java` a `azure-identity`; Spring AI 2 odstránil starý Azure OpenAI starter.

## Obsah

- [Požiadavky](#požiadavky)
- [Rýchly štart](#rýchly-štart)
- [Ako funguje autentifikácia](#ako-funguje-autentifikácia)
- [Spustenie aplikácie](#spustenie-aplikácie)
  - [Použitie Maven](#použitie-maven)
  - [Použitie VS Code](#použitie-vs-code)
  - [Očakávaný výstup](#očakávaný-výstup)
- [Referenčná konfigurácia](#referencia-konfigurácie)
  - [Premenné prostredia](#premenné-prostredia)
  - [Spring konfigurácia](#spring-konfigurácia)
- [Riešenie problémov](#riešenie-problémov)
  - [Bežné problémy](#bežné-problémy)
  - [Ladenie](#režim-ladenia)
- [Ďalšie kroky](#ďalšie-kroky)
- [Zdroje](#zdroje)

## Požiadavky

Pred spustením tohto príkladu sa uistite, že máte:

- Prostriedok Azure AI Foundry s nasadením `gpt-5.6-luna` - zriaďte ho pomocou `azd up` alebo manuálne podľa [návodu na nastavenie Azure AI Foundry](../../getting-started-azure-openai.md)
- Rolu **Cognitive Services OpenAI User** na tomto prostriedku (Bicep šablóny to priraďujú za vás)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), prihlásený cez `az login`
- Java 21+ a Maven 3.9+

> **Nie je potrebný žiadny API kľúč** — autentifikácia je bez kľúča cez Microsoft Entra ID.

## Rýchly štart

```bash
# 1. Prejdite do projektu
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Prihláste sa, aby keyless autentifikácia mohla získať token
az login

# 3. Nakonfigurujte koncový bod
#    - Ak ste spustili `azd up`, .env bol pre vás vytvorený (preskočte toto).
#    - Inak skopírujte šablónu a nastavte AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Spustite aplikáciu
mvn spring-boot:run
```

## Ako funguje autentifikácia

Tento príklad autentifikuje pomocou **Microsoft Entra ID** — nie je potrebný žiadny API kľúč.

Aplikácia explicitne konfiguruje autentifikáciu v [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` vytvára `BearerTokenCredential` pomocou `AuthenticationUtil.getBearerTokenSupplier` s `DefaultAzureCredential` a rozsahom `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` vytvára `OpenAIClient` cez `OpenAIOkHttpClient.builder()`, upravuje koncový bod zdroja na `/openai/v1` a dodáva bearer credential cez `.credential(...)`.
3. `azureChatModel()` poskytuje klienta do Spring AI `OpenAiChatModel`, ktorý podporuje `ChatClient` v tomto príklade.

Tieto explicitné beany zabraňujú globálnemu prepisu autentifikácie pomocou `OPENAI_API_KEY`. Vynechanie API kľúča v YAML samotnom nie je nastavenie autentifikácie. `DefaultAzureCredential` môže použiť lokálnu reláciu `az login` alebo spravovanú identitu v Azure; vybraná identita musí mať vyššie uvedenú rolu.

## Spustenie aplikácie

### Použitie Maven

```bash
mvn spring-boot:run
```

### Použitie VS Code

1. Otvorte projekt vo VS Code
2. Stlačte `F5` alebo použite panel „Run and Debug“
3. Vyberte konfiguráciu „Spring Boot-BasicChatApplication“

> **Poznámka**: Aplikácia načítava `.env` z pracovného adresára, vrátane spustenia z VS Code.

### Očakávaný výstup

Ilustratívny výstup po úspešnom spustení (záznamy spustenia sú vynechané; formulácia odpovede sa môže líšiť):

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

## Referencia konfigurácie

### Premenné prostredia

| Premenná | Popis | Povinné | Príklad |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Koncový bod Foundry (Azure OpenAI) | Áno | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Názov nasadenia chat modelu | Nie | `gpt-5.6-luna` (predvolené) |

> Premenná s API kľúčom **neexistuje** — autentifikácia je bez kľúča (Microsoft Entra ID cez `az login`).

### Spring konfigurácia

Nastavenia v [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) používajú prefix `spring.ai.openai` a sploštené vlastnosti chatu (bez bloku `options`):

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

`model` je **názov nasadenia v Azure**. Autentifikácia pochádza z explicitných beanov popísaných vyššie, nie z nastavenia `api-key`. Tento príklad deaktivuje reasoning a limituje tokeny odpovede na 500; necháva `temperature` a starý parameter `max-tokens` nenastavené.

Microsoft odporúča [oficiálne OpenAI SDK s Azure OpenAI v1 a Responses API pre nové aplikácie](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions zostáva podporované pre túto existujúcu lekciu založenú na správach. Pre GPT-5.6 musia požiadavky so zapnutými nástrojmi na Chat Completions nastaviť `reasoning_effort` na `none`; pre kombináciu reasoning a nástrojov používajte Responses. Viď [volanie nástrojov s reasoning modelmi](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Riešenie problémov

### Bežné problémy

<details>
<summary><strong>Chyba: 401 / „PermissionDenied“ / chyby tokenu</strong></summary>

- Spustite `az login` — autentifikácia bez kľúča vyžaduje aktívne prihlásenie pre získanie tokenu
- Skontrolujte, či má vaše konto rolu **Cognitive Services OpenAI User** na prostriedku
- Ak ste práve priradili rolu, počkajte minútu na jej propagáciu
- Uistite sa, že ste v správnom tenante/predplatnom (`az account show`)
</details>

<details>
<summary><strong>Chyba: „Koncový bod nie je platný“ / chyby pripojenia</strong></summary>

- Uistite sa, že `AZURE_OPENAI_ENDPOINT` je úplná základná URL (napr. `https://your-resource.openai.azure.com/`)
- Skontrolujte konzistenciu so záverečným lomítkom
- Overte, že koncový bod zodpovedá vášmu provisionovanému prostriedku (`azd env get-values`)
</details>

<details>
<summary><strong>Chyba: „Nasadenie nenájdené“</strong></summary>

- Overte, či `AZURE_OPENAI_DEPLOYMENT` zodpovedá názvu nasadenia v Azure
- Skontrolujte, že model je úspešne nasadený a aktívny
- Predvolený názov nasadenia je `gpt-5.6-luna`
</details>

<details>
<summary><strong>Chyba: 429 / prekročený limit</strong></summary>

- Predvolené nasadenie GPT-5.6 Luna má globálnu štandardnú kapacitu 10: 10 požiadaviek/minútu a 10 000 tokenov/minútu
- Spúšťajte príklady postupne a počkajte na interval opakovania služby pred ďalším pokusom
- Tento základný príklad vypína automatické opakovania SDK, preto sa zlyhanie hneď signalizuje
</details>

<details>
<summary><strong>VS Code: premenné prostredia sa nenahrávajú</strong></summary>

- Uistite sa, že súbor `.env` je v koreňovom adresári projektu (na rovnakej úrovni ako `pom.xml`)
- Skúste spustiť `mvn spring-boot:run` v integrovanom termináli VS Code
- Skontrolujte, či je správne nainštalované rozšírenie Java pre VS Code
</details>

### Režim ladenia

Pre detailné logovanie odkomentujte tieto riadky v [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Ďalšie kroky

**Nastavenie dokončené!** Pokračujte v učení:

[Kapitola 3: Základné techniky generatívnej AI](../../../03-CoreGenerativeAITechniques/README.md)

## Zdroje

- [Prechod na Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficiálne OpenAI Java SDK s Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autentifikácia bez kľúča s Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry portál](https://ai.azure.com/)
- [Dokumentácia Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->