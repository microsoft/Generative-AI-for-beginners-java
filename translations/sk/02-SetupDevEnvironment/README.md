<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:13:33+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "sk"
}
-->
# Nastavenie vývojového prostredia pre Generatívnu AI pre Javu

> **Rýchly štart**: Kódovanie v cloude za 2 minúty - Prejdite na [Nastavenie GitHub Codespaces](../../../02-SetupDevEnvironment) - nie je potrebná žiadna lokálna inštalácia a využíva modely GitHub!

> **Zaujíma vás Azure OpenAI?**, pozrite si náš [Sprievodca nastavením Azure OpenAI](getting-started-azure-openai.md) s krokmi na vytvorenie nového zdroja Azure OpenAI.

## Čo sa naučíte

- Nastaviť vývojové prostredie pre AI aplikácie v Jave
- Vybrať a nakonfigurovať preferované vývojové prostredie (cloudové prostredníctvom Codespaces, lokálny vývojový kontajner alebo plne lokálne nastavenie)
- Otestovať svoje nastavenie pripojením k modelom GitHub

## Obsah

- [Čo sa naučíte](../../../02-SetupDevEnvironment)
- [Úvod](../../../02-SetupDevEnvironment)
- [Krok 1: Nastavenie vývojového prostredia](../../../02-SetupDevEnvironment)
  - [Možnosť A: GitHub Codespaces (Odporúčané)](../../../02-SetupDevEnvironment)
  - [Možnosť B: Lokálny vývojový kontajner](../../../02-SetupDevEnvironment)
  - [Možnosť C: Použitie existujúcej lokálnej inštalácie](../../../02-SetupDevEnvironment)
- [Krok 2: Vytvorenie osobného prístupového tokenu GitHub](../../../02-SetupDevEnvironment)
- [Krok 3: Testovanie nastavenia](../../../02-SetupDevEnvironment)
- [Riešenie problémov](../../../02-SetupDevEnvironment)
- [Zhrnutie](../../../02-SetupDevEnvironment)
- [Ďalšie kroky](../../../02-SetupDevEnvironment)

## Úvod

Táto kapitola vás prevedie nastavením vývojového prostredia. Ako hlavný príklad použijeme **GitHub Models**, pretože je zadarmo, ľahko sa nastavuje len s účtom GitHub, nevyžaduje kreditnú kartu a poskytuje prístup k viacerým modelom na experimentovanie.

**Nie je potrebné lokálne nastavenie!** Môžete začať kódovať okamžite pomocou GitHub Codespaces, ktorý poskytuje plné vývojové prostredie vo vašom prehliadači.

<img src="./images/models.webp" alt="Screenshot: GitHub Models" width="50%">

Odporúčame používať [**GitHub Models**](https://github.com/marketplace?type=models) pre tento kurz, pretože:
- Je **zadarmo** na začiatok
- **Jednoduché** nastavenie len s účtom GitHub
- **Nie je potrebná kreditná karta**
- **Viacero modelov** dostupných na experimentovanie

> **Poznámka**: Modely GitHub používané v tomto tréningu majú tieto bezplatné limity:
> - 15 požiadaviek za minútu (150 za deň)
> - ~8 000 slov na vstup, ~4 000 slov na výstup na požiadavku
> - 5 súbežných požiadaviek
> 
> Pre produkčné použitie upgradujte na Azure AI Foundry Models s vaším účtom Azure. Váš kód nie je potrebné meniť. Pozrite si [dokumentáciu Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Krok 1: Nastavenie vývojového prostredia

<a name="quick-start-cloud"></a>

Vytvorili sme predkonfigurovaný vývojový kontajner, aby sme minimalizovali čas nastavenia a zabezpečili, že budete mať všetky potrebné nástroje pre tento kurz Generatívnej AI pre Javu. Vyberte si preferovaný prístup k vývoju:

### Možnosti nastavenia prostredia:

#### Možnosť A: GitHub Codespaces (Odporúčané)

**Začnite kódovať za 2 minúty - nie je potrebné lokálne nastavenie!**

1. Forknite toto úložisko do svojho účtu GitHub
   > **Poznámka**: Ak chcete upraviť základnú konfiguráciu, pozrite si [Konfiguráciu vývojového kontajnera](../../../.devcontainer/devcontainer.json)
2. Kliknite na **Code** → záložka **Codespaces** → **...** → **New with options...**
3. Použite predvolené nastavenia – to vyberie **Konfiguráciu vývojového kontajnera**: **Generative AI Java Development Environment**, špeciálny devcontainer vytvorený pre tento kurz
4. Kliknite na **Create codespace**
5. Počkajte ~2 minúty, kým bude prostredie pripravené
6. Pokračujte na [Krok 2: Vytvorenie GitHub tokenu](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Screenshot: Codespaces submenu" width="50%">

<img src="./images/image.png" alt="Screenshot: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Create codespace options" width="50%">

> **Výhody Codespaces**:
> - Nie je potrebná lokálna inštalácia
> - Funguje na akomkoľvek zariadení s prehliadačom
> - Predkonfigurované so všetkými nástrojmi a závislosťami
> - 60 hodín mesačne zadarmo pre osobné účty
> - Konzistentné prostredie pre všetkých účastníkov

#### Možnosť B: Lokálny vývojový kontajner

**Pre vývojárov, ktorí preferujú lokálny vývoj s Dockerom**

1. Forknite a naklonujte toto úložisko na svoj lokálny počítač
   > **Poznámka**: Ak chcete upraviť základnú konfiguráciu, pozrite si [Konfiguráciu vývojového kontajnera](../../../.devcontainer/devcontainer.json)
2. Nainštalujte [Docker Desktop](https://www.docker.com/products/docker-desktop/) a [VS Code](https://code.visualstudio.com/)
3. Nainštalujte rozšírenie [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) vo VS Code
4. Otvorte priečinok úložiska vo VS Code
5. Keď budete vyzvaní, kliknite na **Reopen in Container** (alebo použite `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Počkajte, kým sa kontajner zostaví a spustí
7. Pokračujte na [Krok 2: Vytvorenie GitHub tokenu](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Screenshot: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev container build complete" width="50%">

#### Možnosť C: Použitie existujúcej lokálnej inštalácie

**Pre vývojárov s existujúcim prostredím pre Javu**

Predpoklady:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) alebo váš preferovaný IDE

Kroky:
1. Naklonujte toto úložisko na svoj lokálny počítač
2. Otvorte projekt vo svojom IDE
3. Pokračujte na [Krok 2: Vytvorenie GitHub tokenu](../../../02-SetupDevEnvironment)

> **Tip pre profesionálov**: Ak máte počítač s nízkymi špecifikáciami, ale chcete používať VS Code lokálne, použite GitHub Codespaces! Môžete pripojiť svoj lokálny VS Code k cloudovému Codespace pre to najlepšie z oboch svetov.

<img src="./images/image-2.png" alt="Screenshot: created local devcontainer instance" width="50%">

## Krok 2: Vytvorenie osobného prístupového tokenu GitHub

1. Prejdite na [Nastavenia GitHub](https://github.com/settings/profile) a vyberte **Settings** z ponuky svojho profilu.
2. V ľavom bočnom paneli kliknite na **Developer settings** (zvyčajne na spodku).
3. Pod **Personal access tokens** kliknite na **Fine-grained tokens** (alebo použite tento priamy [odkaz](https://github.com/settings/personal-access-tokens)).
4. Kliknite na **Generate new token**.
5. Pod "Token name" zadajte popisný názov (napr. `GenAI-Java-Course-Token`).
6. Nastavte dátum vypršania (odporúčané: 7 dní pre najlepšie bezpečnostné praktiky).
7. Pod "Resource owner" vyberte svoj používateľský účet.
8. Pod "Repository access" vyberte úložiská, ktoré chcete používať s GitHub Models (alebo "All repositories", ak je to potrebné).
9. Pod "Repository permissions" nájdite **Models** a nastavte na **Read and write**.
10. Kliknite na **Generate token**.
11. **Skopírujte a uložte svoj token teraz** – už ho znova neuvidíte!

> **Bezpečnostný tip**: Používajte minimálny potrebný rozsah a najkratší praktický čas vypršania pre svoje prístupové tokeny.

## Krok 3: Testovanie nastavenia s príkladom GitHub Models

Keď je vaše vývojové prostredie pripravené, otestujme integráciu GitHub Models s našou ukážkovou aplikáciou v [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Otvorte terminál vo svojom vývojovom prostredí.
2. Prejdite do priečinka s príkladom GitHub Models:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Nastavte svoj GitHub token ako premennú prostredia:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Spustite aplikáciu:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Mali by ste vidieť výstup podobný tomuto:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Pochopenie ukážkového kódu

Najprv si vysvetlíme, čo sme práve spustili. Príklad v `examples/github-models` používa OpenAI Java SDK na pripojenie k GitHub Models:

**Čo tento kód robí:**
- **Pripája sa** k GitHub Models pomocou vášho osobného prístupového tokenu
- **Odosiela** jednoduchú správu "Say Hello World!" AI modelu
- **Prijíma** a zobrazuje odpoveď AI
- **Overuje**, že vaše nastavenie funguje správne

**Kľúčová závislosť** (v `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Hlavný kód** (`App.java`):
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

## Zhrnutie

Výborne! Teraz máte všetko nastavené:

- Vytvorili ste osobný prístupový token GitHub s potrebnými povoleniami na prístup k AI modelom
- Spustili ste svoje vývojové prostredie pre Javu (či už Codespaces, vývojové kontajnery alebo lokálne)
- Pripojili ste sa k GitHub Models pomocou OpenAI Java SDK pre bezplatný vývoj AI
- Otestovali ste, že všetko funguje s jednoduchým príkladom, ktorý komunikuje s AI modelmi

## Ďalšie kroky

[3. kapitola: Základné techniky generatívnej AI](../03-CoreGenerativeAITechniques/README.md)

## Riešenie problémov

Máte problémy? Tu sú bežné problémy a riešenia:

- **Token nefunguje?** 
  - Uistite sa, že ste skopírovali celý token bez akýchkoľvek medzier
  - Overte, že token je správne nastavený ako premenná prostredia
  - Skontrolujte, či má váš token správne povolenia (Models: Read and write)

- **Maven nebol nájdený?** 
  - Ak používate vývojové kontajnery/Codespaces, Maven by mal byť predinštalovaný
  - Pre lokálne nastavenie sa uistite, že máte nainštalované Java 21+ a Maven 3.9+
  - Skúste `mvn --version` na overenie inštalácie

- **Problémy s pripojením?** 
  - Skontrolujte svoje internetové pripojenie
  - Overte, že GitHub je prístupný z vašej siete
  - Uistite sa, že nie ste za firewallom blokujúcim endpoint GitHub Models

- **Vývojový kontajner sa nespúšťa?** 
  - Uistite sa, že Docker Desktop je spustený (pre lokálny vývoj)
  - Skúste znovu zostaviť kontajner: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Chyby kompilácie aplikácie?**
  - Uistite sa, že ste v správnom priečinku: `02-SetupDevEnvironment/examples/github-models`
  - Skúste vyčistiť a znovu zostaviť: `mvn clean compile`

> **Potrebujete pomoc?**: Stále máte problémy? Otvorte issue v úložisku a pomôžeme vám.

**Upozornenie**:  
Tento dokument bol preložený pomocou služby AI prekladu [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, prosím, berte na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.