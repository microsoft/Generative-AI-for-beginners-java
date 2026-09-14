# Nastavení vývojového prostředí pro Azure AI Foundry

> Tento průvodce nastavuje modely **Azure AI Foundry** pro Java AI aplikace v tomto kurzu pomocí **keyless** autentizace (Microsoft Entra ID) — není třeba spravovat API klíče. Jste noví v nástrojích? Začněte s [průvodcem vývojového prostředí](./README.md).

Tento průvodce nastavuje modely **Azure AI Foundry** pro Java AI aplikace v tomto kurzu. Máte dvě možnosti:

- **Možnost A — Provisionování pomocí `azd` + Bicep (doporučeno):** jedním příkazem nasadíte Foundry účet a modely jako kód. Žádné klikání v portálu.
- **Možnost B — Vytvoření prostředků ručně** v portálu Azure AI Foundry.

Obě cesty používají **keyless autentizaci** (Microsoft Entra ID) — nejsou potřeba žádné API klíče k použití nebo úniku.

## Obsah

- [Co se vytvoří](#co-se-vytvoří)
- [Požadavky](#požadavky)
- [Možnost A: Provisionování pomocí azd + Bicep (Doporučeno)](#option-a-provision-with-azd--bicep-recommended)
- [Možnost B: Vytvoření prostředků ručně](#možnost-b-vytvoření-prostředků-ručně)
- [Konfigurace vašeho prostředí](#konfigurace-vašeho-prostředí)
- [Otestujte své nastavení](#otestujte-své-nastavení)
- [Co dál?](#co-dál)
- [Zdroje](#zdroje)
- [Další zdroje](#další-zdroje)

## Co se vytvoří

Šablony Bicep v [`infra/`](../../../02-SetupDevEnvironment/infra) vytvoří:

- **Azure AI Foundry** účet (`Microsoft.CognitiveServices/accounts`, typ `AIServices`) s projektem
- Nasazení **chatu** - GPT-5.6 Luna (`gpt-5.6-luna`), verze `2026-07-09`, s kapacitou `GlobalStandard` `10` (10 požadavků/minutu a 10 000 tokenů/minutu pro tento model)
- Nasazení **embeddingu** - `text-embedding-3-small`, verze `1` (použito v pozdějších kapitolách)
- **Keyless přiřazení role** (`Cognitive Services OpenAI User`), takže se přihlásíte pomocí `az login` místo správy klíčů

## Požadavky

- [Azure předplatné](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) a [Maven 3.9+](https://maven.apache.org/download.cgi)

## Možnost A: Provisionování pomocí azd + Bicep (Doporučeno)

Ze složky `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Přihlásit se (oba nástroje)
azd auth login
az login

# Zajistit účet Foundry + nasazení modelů
azd up
```

`azd` vyžaduje **název prostředí** (například `genai-java`), **předplatné** a **region**. Vyberte své předplatné a region, kde jsou dostupné `gpt-5.6-luna` a `text-embedding-3-small`, například `eastus2`. Potvrďte, že předplatné má dostatečnou kvótu pro model a typ nasazení v daném regionu; dostupnost a kvóty se liší podle předplatného.

Po dokončení provisionování azd:

1. Nasadí vše definované v [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Spustí postprovision hook, který zapíše [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) s vaším endpointem a názvy nasazení (žádná tajemství).

> **Tip:** Kdykoli znovu spusťte `azd up` pro uplatnění změn. Použijte `azd down` k odstranění všeho a zastavení nákladů.

Pro zobrazení vygenerovaných nastavení:

```bash
azd env get-values
```

Nyní přejděte na [Otestujte své nastavení](#otestujte-své-nastavení).

## Možnost B: Vytvoření prostředků ručně

Upřednostňujete portál? Vytvořte zdroje ručně:

1. Přejděte do [Azure AI Foundry portálu](https://ai.azure.com/) a přihlaste se.
2. **Vytvořte projekt** (tím se také vytvoří zdroj AI Foundry). Pojmenujte jej například `GenAIJava`.
3. Ve svém projektu otevřete **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Nasadíte **GPT-5.6 Luna** (název modelu a nasazení `gpt-5.6-luna`, verze `2026-07-09`) s kapacitou **Global Standard** `10`. Stejně tak nasadíte **text-embedding-3-small**, verze `1`, pokud chcete příklady embeddingu.
5. Z **Přehledu** zkopírujte **endpoint** (například `https://<resource>.openai.azure.com/`).
6. Udělte si přístup bez klíče: u zdroje otevřete **Řízení přístupu (IAM)** → **Přidat přiřazení role** → přidělte roli **Cognitive Services OpenAI User** svému účtu.

> **Máte stále potíže?** Podívejte se do [dokumentace Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurace vašeho prostředí

**Pokud jste použili Možnost A (`azd up`)**, soubor nastavení už je vytvořen — není potřeba nic konfigurovat. Přejděte na [Otestujte své nastavení](#otestujte-své-nastavení).

**Pokud jste použili Možnost B (ruční)**, vytvořte si soubor `.env` ukázky sami:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Upravte `.env` s vaším endpointem (bez klíče – autentizace je bez klíče):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Použijte Azure OpenAI endpoint zdroje, nikoliv URL projektu. Aplikace basic-chat jej přeloží na `/openai/v1` a nastaví explicitního klienta s bearer-tokenem; API klíč není potřeba.

> **Bezpečnostní poznámka:** Nemáte žádný API klíč k uložení. Autentizujete se přes Microsoft Entra ID pomocí `az login` (lokálně) nebo spravované identity (v Azure). Soubor `.env` obsahuje pouze méně citlivá nastavení a je již zahrnut v `.gitignore`.

## Otestujte své nastavení

Ujistěte se, že jste přihlášeni, aby keyless autentizace mohla získat token, a spusťte ukázku:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # pokud ještě nejste přihlášeni
mvn clean spring-boot:run
```

Měli byste vidět odpověď od modelu `gpt-5.6-luna`. Spouštějte příklady postupně, abyste zůstali v malé základní kvótě; pokud obdržíte HTTP 429, počkejte před dalším pokusem podle intervalu opětovného pokusu.

> **Uživatelé VS Code:** Stiskněte `F5` pro spuštění. Aplikace automaticky načte váš `.env`.

> **Úplný příklad:** Podívejte se na [Příklad základního chatu s Azure AI Foundry](./examples/basic-chat-azure/README.md) pro detaily a řešení potíží.

## Co dál?

Po provisionování a úspěšném spuštění příkladu budete mít:
- Azure AI Foundry s nasazenými modely `gpt-5.6-luna` a `text-embedding-3-small`
- Keyless autentizaci (Microsoft Entra ID) — bez klíčů k správě
- Lokální `.env` s vaším endpointem a názvy nasazení
- Připravené Java vývojové prostředí

**Pokračujte do** [Kapitoly 3: Základní techniky generativní AI](../03-CoreGenerativeAITechniques/README.md) a začněte vytvářet AI aplikace!

## Zdroje

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Keyless autentizace s Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentace Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Přechod Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficiální OpenAI Java SDK s Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Další zdroje

- [Stáhnout VS Code](https://code.visualstudio.com/Download)
- [Získat Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfigurace Dev kontejneru](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o co největší přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->