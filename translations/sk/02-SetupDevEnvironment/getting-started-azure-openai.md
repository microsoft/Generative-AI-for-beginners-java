# Nastavenie vývojového prostredia pre Azure AI Foundry

> Tento sprievodca nastavuje modely **Azure AI Foundry** pre Java AI aplikácie v tomto kurze pomocou **bezklúčovej** autentifikácie (Microsoft Entra ID) — žiadne API kľúče na správu. Ste v nástrojoch noví? Začnite s [príručkou vývojového prostredia](./README.md).

Tento sprievodca nastavuje modely **Azure AI Foundry** pre Java AI aplikácie v tomto kurze. Máte dve možnosti:

- **Možnosť A — Provisionovanie pomocou `azd` + Bicep (odporúčané):** jedným príkazom nasadíte Foundry účet a modely ako kód. Žiadne klikanie v portáli.
- **Možnosť B — Vytvorenie zdrojov manuálne** v portáli Azure AI Foundry.

Obe možnosti používajú **bezklúčovú autentifikáciu** (Microsoft Entra ID) — nie sú žiadne API kľúče na kopírovanie alebo únik.

## Obsah

- [Čo sa vytvorí](#čo-sa-vytvorí)
- [Predpoklady](#predpoklady)
- [Možnosť A: Provisionovanie pomocou azd + Bicep (odporúčané)](#option-a-provision-with-azd--bicep-recommended)
- [Možnosť B: Vytvorenie zdrojov manuálne](#možnosť-b-vytvorenie-zdrojov-manuálne)
- [Nastavenie prostredia](#nastavte-svoje-prostredie)
- [Otestujte nastavenie](#otestujte-nastavenie)
- [Čo ďalej?](#čo-ďalej)
- [Zdroje](#zdroje)
- [Ďalšie zdroje](#ďalšie-zdroje)

## Čo sa vytvorí

Bicep šablóny v [`infra/`](../../../02-SetupDevEnvironment/infra) provisionujú:

- Účet **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, druh `AIServices`) s projektom
- Nasadenie **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), verzia `2026-07-09`, s kapacitou `GlobalStandard` `10` (10 požiadaviek/minútu a 10 000 tokenov/minútu pre tento model)
- Nasadenie **embedding** - `text-embedding-3-small`, verzia `1` (používané v neskorších kapitolách)
- Bezklúčové priradenie roly (`Cognitive Services OpenAI User`), takže sa prihlásite pomocou `az login` namiesto správy kľúčov

## Predpoklady

- [Azure predplatné](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) a [Maven 3.9+](https://maven.apache.org/download.cgi)

## Možnosť A: Provisionovanie pomocou azd + Bicep (odporúčané)

Zo zložky `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Prihlásiť sa (obe nástroje)
azd auth login
az login

# Poskytnúť účet Foundry + nasadenia modelov
azd up
```

`azd` vás vyzve na zadanie **názvu prostredia** (napríklad `genai-java`), **predplatného** a **regiónu**. Zvoľte svoje predplatné a región, kde sú dostupné `gpt-5.6-luna` a `text-embedding-3-small`, napríklad `eastus2`. Overte, že predplatné má dostatočnú kvótu pre model a typ nasadenia v danom regióne; dostupnosť a kvóta sa líšia podľa predplatného.

Po dokončení provisionovania azd:

1. Nasadí všetko definované v [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Spustí postprovision hook, ktorý zapíše [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) s vašimi endpoint a názvami nasadení (žiadne tajomstvá).

> **Tip:** Kedykoľvek spustite `azd up` na aplikovanie zmien. Použite `azd down`, ak chcete všetko odstrániť a prestať generovať náklady.

Ak chcete zobraziť vygenerované nastavenia:

```bash
azd env get-values
```

Teraz preskočte na [Otestujte nastavenie](#otestujte-nastavenie).

## Možnosť B: Vytvorenie zdrojov manuálne

Preferujete portál? Vytvorte zdroje ručne:

1. Navštívte [Azure AI Foundry portál](https://ai.azure.com/) a prihláste sa.
2. **Vytvorte projekt** (tým sa zároveň vytvorí AI Foundry zdroj). Pomenujte ho napríklad `GenAIJava`.
3. Vo vašom projekte otvorte **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Nasadte **GPT-5.6 Luna** (model a názov nasadenia `gpt-5.6-luna`, verzia `2026-07-09`) s kapacitou **Global Standard** `10`. Opakujte pre **text-embedding-3-small**, verzia `1`, ak chcete embedding príklady.
5. Z **Overview** skopírujte **endpoint** (napríklad `https://<resource>.openai.azure.com/`).
6. Pridajte si bezklúčový prístup: na zdroji otvorte **Access control (IAM)** → **Add role assignment** → priraďte **Cognitive Services OpenAI User** k vášmu účtu.

> **Stále máte problém?** Pozrite si [dokumentáciu Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Nastavte svoje prostredie

**Ak ste použili možnosť A (`azd up`)**, váš konfiguračný súbor je už vytvorený — nie je potrebné nič nastavovať. Preskočte na [Otestujte nastavenie](#otestujte-nastavenie).

**Ak ste použili možnosť B (manuálne)**, vytvorte si `.env` súbor sami:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Upracte `.env` s vaším endpointom (bez kľúča — autentifikácia je bezklúčová):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Použite Azure OpenAI endpoint zdroja, nie URL projektu. Aplikácia basic-chat to vyrieši na `/openai/v1` a nakonfiguruje explicitného klienta s bearer tokenom; API kľúč nie je potrebný.

> **Bezpečnostná poznámka:** Nie je potrebné uchovávať API kľúč. Autentifikujete sa pomocou Microsoft Entra ID cez `az login` (lokálne) alebo Managed Identity (v Azure). `.env` súbor obsahuje len ne-tajné nastavenia a je zahrnutý v `.gitignore`.

## Otestujte nastavenie

Uistite sa, že ste prihlásení, aby bezklúčová autentifikácia získala token, potom spustite príklad:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ak ešte nie ste prihlásený
mvn clean spring-boot:run
```

Mali by ste vidieť odpoveď od modelu `gpt-5.6-luna`. Spúšťajte príklady postupne, aby ste zostali v rámci malej predvolenej kvóty; ak dostanete HTTP 429, počkajte pred ďalším pokusom podľa intervalu retry.

> **Používatelia VS Code:** Stlačte `F5` na spustenie. Aplikácia automaticky načíta váš `.env`.

> **Kompletný príklad:** Pozrite si [Basic Chat s Azure AI Foundry príklad](./examples/basic-chat-azure/README.md) pre podrobnosti a riešenie problémov.

## Čo ďalej?

Po provisionovaní a úspešnom spustení príkladu budete mať:
- Azure AI Foundry s nasadenými `gpt-5.6-luna` a `text-embedding-3-small`
- Bezklúčovú autentifikáciu (Microsoft Entra ID) — žiadne kľúče na správu
- Lokálny `.env` so vaším endpoint a názvami nasadení
- Pripravené Java vývojové prostredie

**Pokračujte na** [Kapitolu 3: Základné generatívne AI techniky](../03-CoreGenerativeAITechniques/README.md) a začnite budovať AI aplikácie!

## Zdroje

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Bezklúčová autentifikácia pomocou Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentácia Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Prechod Spring AI 2 na OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficiálne OpenAI Java SDK s Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Ďalšie zdroje

- [Stiahnite si VS Code](https://code.visualstudio.com/Download)
- [Získajte Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfigurácia vývojového kontajnera](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->