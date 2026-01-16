<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-07-27T13:24:54+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "sk"
}
-->
# Nastavenie vývojového prostredia pre Azure OpenAI

> **Rýchly štart**: Táto príručka je určená na nastavenie Azure OpenAI. Ak chcete okamžite začať s bezplatnými modelmi, použite [GitHub Models with Codespaces](./README.md#quick-start-cloud).

Táto príručka vám pomôže nastaviť modely Azure AI Foundry pre vaše Java AI aplikácie v tomto kurze.

## Obsah

- [Rýchly prehľad nastavenia](../../../02-SetupDevEnvironment)
- [Krok 1: Vytvorenie zdrojov Azure AI Foundry](../../../02-SetupDevEnvironment)
  - [Vytvorenie Hubu a Projektu](../../../02-SetupDevEnvironment)
  - [Nasadenie modelu GPT-4o-mini](../../../02-SetupDevEnvironment)
- [Krok 2: Vytvorenie Codespace](../../../02-SetupDevEnvironment)
- [Krok 3: Konfigurácia prostredia](../../../02-SetupDevEnvironment)
- [Krok 4: Testovanie nastavenia](../../../02-SetupDevEnvironment)
- [Čo ďalej?](../../../02-SetupDevEnvironment)
- [Zdroje](../../../02-SetupDevEnvironment)
- [Ďalšie zdroje](../../../02-SetupDevEnvironment)

## Rýchly prehľad nastavenia

1. Vytvorte zdroje Azure AI Foundry (Hub, Projekt, Model)
2. Vytvorte Codespace s Java vývojovým kontajnerom
3. Nakonfigurujte svoj súbor .env s povereniami Azure OpenAI
4. Otestujte svoje nastavenie s ukážkovým projektom

## Krok 1: Vytvorenie zdrojov Azure AI Foundry

### Vytvorenie Hubu a Projektu

1. Prejdite na [Azure AI Foundry Portal](https://ai.azure.com/) a prihláste sa
2. Kliknite na **+ Create** → **New hub** (alebo prejdite na **Management** → **All hubs** → **+ New hub**)
3. Nakonfigurujte svoj hub:
   - **Názov hubu**: napr. "MyAIHub"
   - **Predplatné**: Vyberte svoje Azure predplatné
   - **Skupina zdrojov**: Vytvorte novú alebo vyberte existujúcu
   - **Lokalita**: Vyberte najbližšiu k vám
   - **Účet úložiska**: Použite predvolený alebo nakonfigurujte vlastný
   - **Key vault**: Použite predvolený alebo nakonfigurujte vlastný
   - Kliknite na **Next** → **Review + create** → **Create**
4. Po vytvorení kliknite na **+ New project** (alebo **Create project** z prehľadu hubu)
   - **Názov projektu**: napr. "GenAIJava"
   - Kliknite na **Create**

### Nasadenie modelu GPT-4o-mini

1. Vo svojom projekte prejdite na **Model catalog** a vyhľadajte **gpt-4o-mini**
   - *Alternatíva: Prejdite na **Deployments** → **+ Create deployment***
2. Kliknite na **Deploy** na karte modelu gpt-4o-mini
3. Nakonfigurujte nasadenie:
   - **Názov nasadenia**: "gpt-4o-mini"
   - **Verzia modelu**: Použite najnovšiu
   - **Typ nasadenia**: Štandardný
4. Kliknite na **Deploy**
5. Po nasadení prejdite na kartu **Deployments** a skopírujte tieto hodnoty:
   - **Názov nasadenia** (napr. "gpt-4o-mini")
   - **Cieľová URI** (napr. `https://your-hub-name.openai.azure.com/`) 
      > **Dôležité**: Skopírujte iba základnú URL (napr. `https://myhub.openai.azure.com/`), nie celú cestu k endpointu.
   - **Kľúč** (z časti Keys and Endpoint)

> **Stále máte problémy?** Navštívte oficiálnu [dokumentáciu Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)

## Krok 2: Vytvorenie Codespace

1. Forknite toto úložisko do svojho GitHub účtu
   > **Poznámka**: Ak chcete upraviť základnú konfiguráciu, pozrite si [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Vo svojom forknutom úložisku kliknite na **Code** → kartu **Codespaces**
3. Kliknite na **...** → **New with options...**
![vytváranie codespace s možnosťami](../../../translated_images/sk/codespaces.9945ded8ceb431a5.webp)
4. Vyberte **Dev container configuration**: 
   - **Generative AI Java Development Environment**
5. Kliknite na **Create codespace**

## Krok 3: Konfigurácia prostredia

Keď je váš Codespace pripravený, nastavte svoje poverenia Azure OpenAI:

1. **Prejdite na ukážkový projekt z koreňa úložiska:**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **Vytvorte svoj súbor .env:**
   ```bash
   cp .env.example .env
   ```

3. **Upravte súbor .env so svojimi povereniami Azure OpenAI:**
   ```bash
   # Your Azure OpenAI API key (from Azure AI Foundry portal)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # Your Azure OpenAI endpoint URL (e.g., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **Bezpečnostná poznámka**: 
   > - Nikdy neukladajte svoj `.env` súbor do verzionovacieho systému
   > - Súbor `.env` je už zahrnutý v `.gitignore`
   > - Uchovávajte svoje API kľúče v bezpečí a pravidelne ich rotujte

## Krok 4: Testovanie nastavenia

Spustite ukážkovú aplikáciu na otestovanie pripojenia Azure OpenAI:

```bash
mvn clean spring-boot:run
```

Mali by ste vidieť odpoveď od modelu GPT-4o-mini!

> **Používatelia VS Code**: Môžete tiež stlačiť `F5` vo VS Code na spustenie aplikácie. Konfigurácia spustenia je už nastavená na automatické načítanie vášho súboru `.env`.

> **Kompletný príklad**: Pozrite si [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) pre podrobné pokyny a riešenie problémov.

## Čo ďalej?

**Nastavenie dokončené!** Teraz máte:
- Azure OpenAI s nasadeným gpt-4o-mini
- Lokálnu konfiguráciu súboru .env
- Pripravené vývojové prostredie pre Javu

**Pokračujte na** [Kapitolu 3: Základné techniky generatívnej AI](../03-CoreGenerativeAITechniques/README.md) a začnite budovať AI aplikácie!

## Zdroje

- [Dokumentácia Azure AI Foundry](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI Dokumentácia](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## Ďalšie zdroje

- [Stiahnite si VS Code](https://code.visualstudio.com/Download)
- [Získajte Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

**Upozornenie**:  
Tento dokument bol preložený pomocou služby na automatický preklad [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, upozorňujeme, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nenesieme zodpovednosť za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.