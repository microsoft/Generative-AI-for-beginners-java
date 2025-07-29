<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:10:39+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "cs"
}
-->
# Nastavení vývojového prostředí pro Generativní AI v Javě

> **Rychlý start**: Programujte v cloudu za 2 minuty – přejděte na [Nastavení GitHub Codespaces](../../../02-SetupDevEnvironment) – není potřeba žádná lokální instalace a využívají se modely GitHubu!

> **Zajímá vás Azure OpenAI?** Podívejte se na náš [Průvodce nastavením Azure OpenAI](getting-started-azure-openai.md) s kroky pro vytvoření nové Azure OpenAI služby.

## Co se naučíte

- Nastavit vývojové prostředí pro aplikace AI v Javě
- Vybrat a nakonfigurovat preferované vývojové prostředí (cloudové s Codespaces, lokální dev kontejner nebo plně lokální nastavení)
- Otestovat nastavení připojením k modelům GitHubu

## Obsah

- [Co se naučíte](../../../02-SetupDevEnvironment)
- [Úvod](../../../02-SetupDevEnvironment)
- [Krok 1: Nastavení vývojového prostředí](../../../02-SetupDevEnvironment)
  - [Možnost A: GitHub Codespaces (doporučeno)](../../../02-SetupDevEnvironment)
  - [Možnost B: Lokální dev kontejner](../../../02-SetupDevEnvironment)
  - [Možnost C: Použití stávající lokální instalace](../../../02-SetupDevEnvironment)
- [Krok 2: Vytvoření osobního přístupového tokenu GitHub](../../../02-SetupDevEnvironment)
- [Krok 3: Otestování nastavení](../../../02-SetupDevEnvironment)
- [Řešení problémů](../../../02-SetupDevEnvironment)
- [Shrnutí](../../../02-SetupDevEnvironment)
- [Další kroky](../../../02-SetupDevEnvironment)

## Úvod

Tato kapitola vás provede nastavením vývojového prostředí. Jako hlavní příklad použijeme **modely GitHubu**, protože jsou zdarma, snadno nastavitelné pouze s GitHub účtem, nevyžadují kreditní kartu a poskytují přístup k několika modelům pro experimentování.

**Není potřeba žádné lokální nastavení!** Můžete začít programovat okamžitě pomocí GitHub Codespaces, které poskytují plné vývojové prostředí přímo ve vašem prohlížeči.

<img src="./images/models.webp" alt="Snímek obrazovky: Modely GitHubu" width="50%">

Doporučujeme používat [**modely GitHubu**](https://github.com/marketplace?type=models) pro tento kurz, protože:
- Jsou **zdarma** na začátek
- **Snadno** nastavitelné pouze s GitHub účtem
- **Nejsou potřeba** žádné kreditní karty
- **Více modelů** dostupných pro experimentování

> **Poznámka**: Modely GitHubu používané v tomto školení mají tyto bezplatné limity:
> - 15 požadavků za minutu (150 za den)
> - ~8 000 slov vstup, ~4 000 slov výstup na požadavek
> - 5 souběžných požadavků
> 
> Pro produkční použití přejděte na Azure AI Foundry Models s vaším Azure účtem. Váš kód není třeba měnit. Viz [dokumentace Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Krok 1: Nastavení vývojového prostředí

<a name="quick-start-cloud"></a>

Vytvořili jsme předkonfigurovaný vývojový kontejner, abychom minimalizovali čas na nastavení a zajistili, že budete mít všechny potřebné nástroje pro tento kurz Generativní AI v Javě. Vyberte si preferovaný přístup k vývoji:

### Možnosti nastavení prostředí:

#### Možnost A: GitHub Codespaces (doporučeno)

**Začněte programovat za 2 minuty – není potřeba žádné lokální nastavení!**

1. Forkněte toto úložiště do svého GitHub účtu
   > **Poznámka**: Pokud chcete upravit základní konfiguraci, podívejte se na [Konfiguraci dev kontejneru](../../../.devcontainer/devcontainer.json)
2. Klikněte na **Code** → záložka **Codespaces** → **...** → **New with options...**
3. Použijte výchozí nastavení – to vybere **Konfiguraci dev kontejneru**: **Generative AI Java Development Environment**, speciální dev kontejner vytvořený pro tento kurz
4. Klikněte na **Create codespace**
5. Počkejte ~2 minuty, než bude prostředí připraveno
6. Pokračujte na [Krok 2: Vytvoření GitHub tokenu](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Snímek obrazovky: Podnabídka Codespaces" width="50%">

<img src="./images/image.png" alt="Snímek obrazovky: Nové s možnostmi" width="50%">

<img src="./images/codespaces-create.png" alt="Snímek obrazovky: Možnosti vytvoření Codespace" width="50%">

> **Výhody Codespaces**:
> - Není potřeba žádná lokální instalace
> - Funguje na jakémkoli zařízení s prohlížečem
> - Předkonfigurované se všemi nástroji a závislostmi
> - Zdarma 60 hodin měsíčně pro osobní účty
> - Konzistentní prostředí pro všechny účastníky

#### Možnost B: Lokální dev kontejner

**Pro vývojáře, kteří preferují lokální vývoj s Dockerem**

1. Forkněte a naklonujte toto úložiště na svůj lokální počítač
   > **Poznámka**: Pokud chcete upravit základní konfiguraci, podívejte se na [Konfiguraci dev kontejneru](../../../.devcontainer/devcontainer.json)
2. Nainstalujte [Docker Desktop](https://www.docker.com/products/docker-desktop/) a [VS Code](https://code.visualstudio.com/)
3. Nainstalujte rozšíření [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ve VS Code
4. Otevřete složku úložiště ve VS Code
5. Když budete vyzváni, klikněte na **Reopen in Container** (nebo použijte `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Počkejte, než se kontejner sestaví a spustí
7. Pokračujte na [Krok 2: Vytvoření GitHub tokenu](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Snímek obrazovky: Nastavení dev kontejneru" width="50%">

<img src="./images/image-3.png" alt="Snímek obrazovky: Dokončení sestavení dev kontejneru" width="50%">

#### Možnost C: Použití stávající lokální instalace

**Pro vývojáře s existujícími prostředími pro Javu**

Předpoklady:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) nebo váš preferovaný IDE

Kroky:
1. Naklonujte toto úložiště na svůj lokální počítač
2. Otevřete projekt ve svém IDE
3. Pokračujte na [Krok 2: Vytvoření GitHub tokenu](../../../02-SetupDevEnvironment)

> **Tip pro pokročilé**: Máte-li počítač s nízkým výkonem, ale chcete VS Code lokálně, použijte GitHub Codespaces! Můžete připojit svůj lokální VS Code ke cloudovému Codespace a získat to nejlepší z obou světů.

<img src="./images/image-2.png" alt="Snímek obrazovky: Vytvořený lokální dev kontejner" width="50%">

## Krok 2: Vytvoření osobního přístupového tokenu GitHub

1. Přejděte na [Nastavení GitHub](https://github.com/settings/profile) a vyberte **Settings** z nabídky svého profilu.
2. V levém postranním panelu klikněte na **Developer settings** (obvykle dole).
3. Pod **Personal access tokens** klikněte na **Fine-grained tokens** (nebo použijte tento přímý [odkaz](https://github.com/settings/personal-access-tokens)).
4. Klikněte na **Generate new token**.
5. Pod "Token name" zadejte popisný název (např. `GenAI-Java-Course-Token`).
6. Nastavte datum vypršení platnosti (doporučeno: 7 dní pro bezpečnostní osvědčené postupy).
7. Pod "Resource owner" vyberte svůj uživatelský účet.
8. Pod "Repository access" vyberte repozitáře, které chcete používat s modely GitHubu (nebo "All repositories", pokud je to potřeba).
9. Pod "Repository permissions" najděte **Models** a nastavte na **Read and write**.
10. Klikněte na **Generate token**.
11. **Zkopírujte a uložte svůj token nyní** – později jej už neuvidíte!

> **Bezpečnostní tip**: Používejte minimální požadovaný rozsah a nejkratší praktickou dobu platnosti pro své přístupové tokeny.

## Krok 3: Otestování nastavení s příkladem modelů GitHubu

Jakmile je vaše vývojové prostředí připraveno, otestujeme integraci modelů GitHubu s naším ukázkovým programem v [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Otevřete terminál ve svém vývojovém prostředí.
2. Přejděte do složky s příkladem modelů GitHubu:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Nastavte svůj GitHub token jako proměnnou prostředí:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Spusťte aplikaci:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Měli byste vidět výstup podobný:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Pochopení ukázkového kódu

Nejprve si vysvětlíme, co jsme právě spustili. Příklad ve složce `examples/github-models` používá OpenAI Java SDK pro připojení k modelům GitHubu:

**Co tento kód dělá:**
- **Připojuje se** k modelům GitHubu pomocí vašeho osobního přístupového tokenu
- **Odesílá** jednoduchou zprávu "Řekni Ahoj světe!" modelu AI
- **Přijímá** a zobrazuje odpověď AI
- **Ověřuje**, že vaše nastavení funguje správně

**Klíčová závislost** (v `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Hlavní kód** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Shrnutí

Skvělé! Nyní máte vše nastaveno:

- Vytvořili jste osobní přístupový token GitHub s potřebnými oprávněními pro přístup k modelům AI
- Spustili jste své vývojové prostředí pro Javu (ať už Codespaces, dev kontejnery nebo lokálně)
- Připojili jste se k modelům GitHubu pomocí OpenAI Java SDK pro vývoj AI zdarma
- Otestovali jste, že vše funguje, pomocí jednoduchého příkladu, který komunikuje s modely AI

## Další kroky

[Kap. 3: Základní techniky generativní AI](../03-CoreGenerativeAITechniques/README.md)

## Řešení problémů

Máte problémy? Zde jsou běžné problémy a jejich řešení:

- **Token nefunguje?** 
  - Ujistěte se, že jste zkopírovali celý token bez mezer navíc
  - Ověřte, že token je správně nastaven jako proměnná prostředí
  - Zkontrolujte, zda váš token má správná oprávnění (Models: Read and write)

- **Maven nebyl nalezen?** 
  - Pokud používáte dev kontejnery/Codespaces, Maven by měl být předinstalován
  - Pro lokální nastavení se ujistěte, že máte nainstalovanou Javu 21+ a Maven 3.9+
  - Zkuste `mvn --version` pro ověření instalace

- **Problémy s připojením?** 
  - Zkontrolujte své internetové připojení
  - Ověřte, že GitHub je přístupný z vaší sítě
  - Ujistěte se, že nejste za firewallem blokujícím endpoint modelů GitHubu

- **Dev kontejner se nespouští?** 
  - Ujistěte se, že Docker Desktop běží (pro lokální vývoj)
  - Zkuste znovu sestavit kontejner: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Chyby při kompilaci aplikace?**
  - Ujistěte se, že jste ve správném adresáři: `02-SetupDevEnvironment/examples/github-models`
  - Zkuste vyčistit a znovu sestavit: `mvn clean compile`

> **Potřebujete pomoc?**: Stále máte problémy? Otevřete issue v repozitáři a my vám pomůžeme.

**Prohlášení:**  
Tento dokument byl přeložen pomocí služby pro automatický překlad [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli se snažíme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Neodpovídáme za žádné nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.