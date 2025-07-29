<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T16:12:03+00:00",
  "source_file": "README.md",
  "language_code": "sk"
}
-->
# Generatívna AI pre začiatočníkov - Java edícia
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generatívna AI pre začiatočníkov - Java edícia](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.sk.png)

**Časová náročnosť**: Celý workshop je možné dokončiť online bez lokálneho nastavenia. Nastavenie prostredia trvá 2 minúty, pričom preskúmanie príkladov vyžaduje 1-3 hodiny v závislosti od hĺbky skúmania.

> **Rýchly štart**

1. Forknite tento repozitár do svojho GitHub účtu
2. Kliknite na **Code** → záložka **Codespaces** → **...** → **New with options...**
3. Použite predvolené nastavenia – vyberie sa vývojový kontajner vytvorený pre tento kurz
4. Kliknite na **Create codespace**
5. Počkajte ~2 minúty, kým bude prostredie pripravené
6. Prejdite priamo na [Prvý príklad](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Podpora viacerých jazykov

### Podporované cez GitHub Action (automatizované a vždy aktuálne)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](./README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## Štruktúra kurzu a učebná cesta

### **Kapitola 1: Úvod do generatívnej AI**
- **Základné koncepty**: Porozumenie veľkým jazykovým modelom, tokenom, embeddingom a schopnostiam AI
- **Java AI ekosystém**: Prehľad Spring AI a OpenAI SDK
- **Protokol kontextu modelu**: Úvod do MCP a jeho úlohy v komunikácii AI agentov
- **Praktické aplikácie**: Scenáre z reálneho sveta vrátane chatbotov a generovania obsahu
- **[→ Začať kapitolu 1](./01-IntroToGenAI/README.md)**

### **Kapitola 2: Nastavenie vývojového prostredia**
- **Konfigurácia viacerých poskytovateľov**: Nastavenie GitHub modelov, Azure OpenAI a OpenAI Java SDK integrácií
- **Spring Boot + Spring AI**: Najlepšie postupy pre vývoj AI aplikácií v podnikovej sfére
- **GitHub modely**: Bezplatný prístup k AI modelom na prototypovanie a učenie (bez potreby kreditnej karty)
- **Vývojové nástroje**: Konfigurácia Docker kontajnerov, VS Code a GitHub Codespaces
- **[→ Začať kapitolu 2](./02-SetupDevEnvironment/README.md)**

### **Kapitola 3: Základné techniky generatívnej AI**
- **Prompt Engineering**: Techniky na dosiahnutie optimálnych odpovedí AI modelov
- **Embeddingy a vektorové operácie**: Implementácia semantického vyhľadávania a porovnávania podobnosti
- **Generovanie s podporou vyhľadávania (RAG)**: Kombinácia AI s vlastnými dátovými zdrojmi
- **Volanie funkcií**: Rozšírenie schopností AI pomocou vlastných nástrojov a pluginov
- **[→ Začať kapitolu 3](./03-CoreGenerativeAITechniques/README.md)**

### **Kapitola 4: Praktické aplikácie a projekty**
- **Generátor príbehov o domácich zvieratách** (`petstory/`): Kreatívne generovanie obsahu pomocou GitHub modelov
- **Foundry Local Demo** (`foundrylocal/`): Integrácia lokálnych AI modelov s OpenAI Java SDK
- **MCP kalkulačná služba** (`calculator/`): Základná implementácia protokolu kontextu modelu so Spring AI
- **[→ Začať kapitolu 4](./04-PracticalSamples/README.md)**

### **Kapitola 5: Zodpovedný vývoj AI**
- **Bezpečnosť GitHub modelov**: Testovanie zabudovaného filtrovania obsahu a bezpečnostných mechanizmov (tvrdé bloky a mäkké odmietnutia)
- **Demo zodpovednej AI**: Praktický príklad ukazujúci, ako moderné bezpečnostné systémy AI fungujú v praxi
- **Najlepšie postupy**: Základné pokyny pre etický vývoj a nasadenie AI
- **[→ Začať kapitolu 5](./05-ResponsibleGenAI/README.md)**

## Ďalšie zdroje

- [AI Agents For Beginners](https://github.com/microsoft/ai-agents-for-beginners)
- [Generative AI for Beginners using .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generative AI for Beginners using JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generative AI for Beginners](https://github.com/microsoft/generative-ai-for-beginners)
- [ML for Beginners](https://aka.ms/ml-beginners)
- [Data Science for Beginners](https://aka.ms/datascience-beginners)
- [AI for Beginners](https://aka.ms/ai-beginners)
- [Cybersecurity for Beginners](https://github.com/microsoft/Security-101)
- [Web Dev for Beginners](https://aka.ms/webdev-beginners)
- [IoT for Beginners](https://aka.ms/iot-beginners)
- [XR Development for Beginners](https://github.com/microsoft/xr-development-for-beginners)
- [Mastering GitHub Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI)
- [Mastering GitHub Copilot pre C#/.NET vývojárov](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Choose Your Own Copilot Adventure](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App s Azure AI službami](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Zrieknutie sa zodpovednosti**:  
Tento dokument bol preložený pomocou služby AI prekladu [Co-op Translator](https://github.com/Azure/co-op-translator). Aj keď sa snažíme o presnosť, prosím, berte na vedomie, že automatizované preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho rodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nenesieme zodpovednosť za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.