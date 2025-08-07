<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:20:10+00:00",
  "source_file": "README.md",
  "language_code": "sk"
}
-->
# Generatívna AI pre začiatočníkov - Java edícia
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generatívna AI pre začiatočníkov - Java edícia](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.sk.png)

**Časová náročnosť**: Celý workshop je možné dokončiť online bez lokálneho nastavenia. Nastavenie prostredia trvá 2 minúty, pričom skúmanie príkladov si vyžaduje 1-3 hodiny v závislosti od hĺbky skúmania.

> **Rýchly štart** 

1. Forknite toto úložisko do svojho GitHub účtu
2. Kliknite na **Code** → záložka **Codespaces** → **...** → **New with options...**
3. Použite predvolené nastavenia – vyberie sa vývojové prostredie vytvorené pre tento kurz
4. Kliknite na **Create codespace**
5. Počkajte ~2 minúty, kým bude prostredie pripravené
6. Prejdite priamo na [Prvý príklad](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Podpora viacerých jazykov

### Podporované cez GitHub Action (automatizované a vždy aktuálne)

[Francúzština](../fr/README.md) | [Španielčina](../es/README.md) | [Nemčina](../de/README.md) | [Ruština](../ru/README.md) | [Arabčina](../ar/README.md) | [Perzština (Farsí)](../fa/README.md) | [Urdu](../ur/README.md) | [Čínština (zjednodušená)](../zh/README.md) | [Čínština (tradičná, Macao)](../mo/README.md) | [Čínština (tradičná, Hongkong)](../hk/README.md) | [Čínština (tradičná, Taiwan)](../tw/README.md) | [Japončina](../ja/README.md) | [Kórejčina](../ko/README.md) | [Hindčina](../hi/README.md) | [Bengálčina](../bn/README.md) | [Maráthčina](../mr/README.md) | [Nepálčina](../ne/README.md) | [Pandžábčina (Gurmukhi)](../pa/README.md) | [Portugalčina (Portugalsko)](../pt/README.md) | [Portugalčina (Brazília)](../br/README.md) | [Taliančina](../it/README.md) | [Poľština](../pl/README.md) | [Turečtina](../tr/README.md) | [Gréčtina](../el/README.md) | [Thajčina](../th/README.md) | [Švédčina](../sv/README.md) | [Dánčina](../da/README.md) | [Nórčina](../no/README.md) | [Fínčina](../fi/README.md) | [Holandčina](../nl/README.md) | [Hebrejčina](../he/README.md) | [Vietnamčina](../vi/README.md) | [Indonézština](../id/README.md) | [Malajčina](../ms/README.md) | [Tagalčina (Filipíny)](../tl/README.md) | [Swahilčina](../sw/README.md) | [Maďarčina](../hu/README.md) | [Čeština](../cs/README.md) | [Slovenčina](./README.md) | [Rumunčina](../ro/README.md) | [Bulharčina](../bg/README.md) | [Srbčina (cyrilika)](../sr/README.md) | [Chorvátčina](../hr/README.md) | [Slovinčina](../sl/README.md) | [Ukrajinčina](../uk/README.md) | [Barmčina (Mjanmarsko)](../my/README.md)

## Štruktúra kurzu a učebná cesta

### **Kapitola 1: Úvod do generatívnej AI**
- **Základné koncepty**: Porozumenie veľkým jazykovým modelom, tokenom, embeddingom a schopnostiam AI
- **Java AI ekosystém**: Prehľad Spring AI a OpenAI SDK
- **Model Context Protocol**: Úvod do MCP a jeho úlohy v komunikácii AI agentov
- **Praktické aplikácie**: Reálne scenáre vrátane chatbotov a generovania obsahu
- **[→ Začať kapitolu 1](./01-IntroToGenAI/README.md)**

### **Kapitola 2: Nastavenie vývojového prostredia**
- **Konfigurácia viacerých poskytovateľov**: Nastavenie GitHub Models, Azure OpenAI a OpenAI Java SDK integrácií
- **Spring Boot + Spring AI**: Najlepšie postupy pre vývoj podnikových AI aplikácií
- **GitHub Models**: Bezplatný prístup k AI modelom na prototypovanie a učenie (bez potreby kreditnej karty)
- **Vývojové nástroje**: Konfigurácia Docker kontajnerov, VS Code a GitHub Codespaces
- **[→ Začať kapitolu 2](./02-SetupDevEnvironment/README.md)**

### **Kapitola 3: Základné techniky generatívnej AI**
- **Prompt Engineering**: Techniky pre optimálne odpovede AI modelov
- **Embeddingy a vektorové operácie**: Implementácia sémantického vyhľadávania a porovnávania podobnosti
- **Retrieval-Augmented Generation (RAG)**: Kombinácia AI s vlastnými dátovými zdrojmi
- **Volanie funkcií**: Rozšírenie schopností AI pomocou vlastných nástrojov a pluginov
- **[→ Začať kapitolu 3](./03-CoreGenerativeAITechniques/README.md)**

### **Kapitola 4: Praktické aplikácie a projekty**
- **Generátor príbehov o domácich miláčikoch** (`petstory/`): Kreatívne generovanie obsahu s GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): Integrácia lokálnych AI modelov s OpenAI Java SDK
- **MCP Kalkulačná služba** (`calculator/`): Základná implementácia Model Context Protocol so Spring AI
- **[→ Začať kapitolu 4](./04-PracticalSamples/README.md)**

### **Kapitola 5: Zodpovedný vývoj AI**
- **Bezpečnosť GitHub Models**: Testovanie vstavaného filtrovania obsahu a bezpečnostných mechanizmov (tvrdé bloky a mäkké odmietnutia)
- **Demo zodpovednej AI**: Praktický príklad ukazujúci, ako moderné bezpečnostné systémy AI fungujú v praxi
- **Najlepšie postupy**: Základné pokyny pre etický vývoj a nasadenie AI
- **[→ Začať kapitolu 5](./05-ResponsibleGenAI/README.md)**

## Ďalšie zdroje 

- [MCP pre začiatočníkov](https://github.com/microsoft/mcp-for-beginners)
- [AI agenti pre začiatočníkov](https://github.com/microsoft/ai-agents-for-beginners)
- [Generatívna AI pre začiatočníkov s .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generatívna AI pre začiatočníkov s JavaScriptom](https://github.com/microsoft/generative-ai-with-javascript)
- [Generatívna AI pre začiatočníkov](https://github.com/microsoft/generative-ai-for-beginners)
- [ML pre začiatočníkov](https://aka.ms/ml-beginners)
- [Dátová veda pre začiatočníkov](https://aka.ms/datascience-beginners)
- [AI pre začiatočníkov](https://aka.ms/ai-beginners)
- [Kybernetická bezpečnosť pre začiatočníkov](https://github.com/microsoft/Security-101)
- [Webový vývoj pre začiatočníkov](https://aka.ms/webdev-beginners)
- [IoT pre začiatočníkov](https://aka.ms/iot-beginners)
- [XR vývoj pre začiatočníkov](https://github.com/microsoft/xr-development-for-beginners)
- [Ovládnutie GitHub Copilot pre AI párové programovanie](https://aka.ms/GitHubCopilotAI)
- [Ovládnutie GitHub Copilot pre C#/.NET vývojárov](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Vyberte si vlastné dobrodružstvo s Copilotom](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App s Azure AI službami](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Zrieknutie sa zodpovednosti**:  
Tento dokument bol preložený pomocou služby AI prekladu [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, prosím, berte na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nenesieme zodpovednosť za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.