<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:45:11+00:00",
  "source_file": "README.md",
  "language_code": "cs"
}
-->
# Generativní AI pro začátečníky - edice Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generativní AI pro začátečníky - edice Java](../../translated_images/cs/beg-genai-series.8b48be9951cc574c.webp)

**Časová náročnost**: Celý workshop lze dokončit online bez lokální instalace. Nastavení prostředí zabere 2 minuty, prozkoumání ukázek vyžaduje 1–3 hodiny podle hloubky průzkumu.

> **Rychlý start**

1. Forkněte si tento repozitář na svůj GitHub účet
2. Klikněte na **Code** → záložka **Codespaces** → **...** → **Nový s možnostmi...**
3. Použijte výchozí nastavení – vybere se vývojový kontejner vytvořený pro tento kurz
4. Klikněte na **Vytvořit codespace**
5. Počkejte ~2 minuty na připravení prostředí
6. Přejděte rovnou na [První příklad](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Preferujete klonovat lokálně?**

> Tento repozitář obsahuje více než 50 jazykových překladů, které výrazně zvětšují velikost stahování. Pro klonování bez překladů použijte sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tím získáte vše potřebné k dokončení kurzu s výrazně rychlejším stažením.

## Podpora vícejazyčnosti

### Podporováno přes GitHub Action (automatické a vždy aktuální)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabština](../ar/README.md) | [Bengálština](../bn/README.md) | [Bulharština](../bg/README.md) | [Barmština (Myanmar)](../my/README.md) | [Čínština (zjednodušená)](../zh/README.md) | [Čínština (tradiční, Hongkong)](../hk/README.md) | [Čínština (tradiční, Macao)](../mo/README.md) | [Čínština (tradiční, Tchajwan)](../tw/README.md) | [Chorvatština](../hr/README.md) | [Čeština](./README.md) | [Dánština](../da/README.md) | [Nizozemština](../nl/README.md) | [Estonština](../et/README.md) | [Finština](../fi/README.md) | [Francouzština](../fr/README.md) | [Němčina](../de/README.md) | [Řečtina](../el/README.md) | [Hebrejština](../he/README.md) | [Hindština](../hi/README.md) | [Maďarština](../hu/README.md) | [Indonéština](../id/README.md) | [Italština](../it/README.md) | [Japonština](../ja/README.md) | [Kannadština](../kn/README.md) | [Korejština](../ko/README.md) | [Litevština](../lt/README.md) | [Malajština](../ms/README.md) | [Malayalam](../ml/README.md) | [Maráthština](../mr/README.md) | [Nepálština](../ne/README.md) | [Nigérijský pidžin](../pcm/README.md) | [Norština](../no/README.md) | [Perština (Fársí)](../fa/README.md) | [Polština](../pl/README.md) | [Portugalština (Brazílie)](../br/README.md) | [Portugalština (Portugalsko)](../pt/README.md) | [Paňdžábština (Gurmukhí)](../pa/README.md) | [Rumunština](../ro/README.md) | [Ruština](../ru/README.md) | [Srbština (cyrilice)](../sr/README.md) | [Slovenština](../sk/README.md) | [Slovinština](../sl/README.md) | [Španělština](../es/README.md) | [Svahilština](../sw/README.md) | [Švédština](../sv/README.md) | [Tagalog (filipínština)](../tl/README.md) | [Tamilština](../ta/README.md) | [Telugština](../te/README.md) | [Thajština](../th/README.md) | [Turečtina](../tr/README.md) | [Ukrajinština](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamština](../vi/README.md)

> **Preferujete klonovat lokálně?**

> Tento repozitář obsahuje více než 50 jazykových překladů, které výrazně zvětšují velikost stahování. Pro klonování bez překladů použijte sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tím získáte vše potřebné k dokončení kurzu s výrazně rychlejším stažením.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Struktura kurzu a učební cesta

### **Kapitola 1: Úvod do generativní AI**
- **Základní pojmy**: Pochopení velkých jazykových modelů, tokenů, embeddingů a schopností AI
- **Ekosystém Java AI**: Přehled Spring AI a OpenAI SDK
- **Protokol pro kontext modelu**: Úvod do MCP a jeho role v komunikaci AI agentů
- **Praktické aplikace**: Reálné scénáře včetně chatbotů a generování obsahu
- **[→ Zahájit kapitolu 1](./01-IntroToGenAI/README.md)**

### **Kapitola 2: Nastavení vývojového prostředí**
- **Konfigurace více poskytovatelů**: Nastavení GitHub Models, Azure OpenAI a OpenAI Java SDK integrací
- **Spring Boot + Spring AI**: Nejlepší postupy pro vývoj podnikových AI aplikací
- **GitHub Models**: Bezplatný přístup k AI modelům pro prototypování a učení (není potřeba kreditní karta)
- **Vývojové nástroje**: Kontejnery Docker, VS Code a konfigurace GitHub Codespaces
- **[→ Zahájit kapitolu 2](./02-SetupDevEnvironment/README.md)**

### **Kapitola 3: Základní techniky generativní AI**
- **Prompt Engineering**: Techniky pro optimální odpovědi AI modelů
- **Embeddingy a vektorové operace**: Implementace sémantického vyhledávání a porovnávání podobnosti
- **Retrieval-Augmented Generation (RAG)**: Kombinace AI s vašimi vlastními datovými zdroji
- **Volání funkcí**: Rozšíření schopností AI pomocí vlastních nástrojů a pluginů
- **[→ Zahájit kapitolu 3](./03-CoreGenerativeAITechniques/README.md)**

### **Kapitola 4: Praktické aplikace a projekty**
- **Generátor příběhů o mazlíčcích** (`petstory/`): Kreativní generování obsahu s GitHub Models
- **Demo Foundry lokálně** (`foundrylocal/`): Lokální integrace AI modelu s OpenAI Java SDK
- **Služba kalkulačky MCP** (`calculator/`): Základní implementace Model Context Protocol pomocí Spring AI
- **[→ Zahájit kapitolu 4](./04-PracticalSamples/README.md)**

### **Kapitola 5: Odpovědný vývoj AI**
- **Bezpečnost GitHub Models**: Testování vestavěného filtrování obsahu a bezpečnostních mechanismů (tvrdé blokace a měkké odmítnutí)
- **Demo odpovědné AI**: Praktický příklad ukazující, jak fungují moderní bezpečnostní systémy AI
- **Nejlepší postupy**: Zásadní pokyny pro etický vývoj a nasazení AI
- **[→ Zahájit kapitolu 5](./05-ResponsibleGenAI/README.md)**

## Další zdroje

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j pro začátečníky](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js pro začátečníky](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agent
[![AZD pro začátečníky](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI pro začátečníky](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP pro začátečníky](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI agenti pro začátečníky](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Série Generativní AI
[![Generativní AI pro začátečníky](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generativní AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generativní AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generativní AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Klíčové učení
[![Strojové učení pro začátečníky](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Datová věda pro začátečníky](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI pro začátečníky](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kybernetická bezpečnost pro začátečníky](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Webový vývoj pro začátečníky](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT pro začátečníky](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Vývoj XR pro začátečníky](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Série Copilot
[![Copilot pro AI párové programování](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot pro C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Dobrodružství Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Získání pomoci

Pokud se zaseknete nebo máte jakékoli otázky ohledně tvorby AI aplikací, připojte se ke komunitě spolužáků a zkušených vývojářů v diskuzích o MCP. Je to podpůrná komunita, kde jsou otázky vítány a znalosti se volně sdílejí.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Pokud máte zpětnou vazbu k produktu nebo narazíte na chyby při vývoji, navštivte:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o vyloučení odpovědnosti**:  
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože se snažíme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby či nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za závazný zdroj. Pro zásadní informace se doporučuje využít profesionální lidský překlad. Nejsme odpovědní za jakékoliv nedorozumění či nesprávné interpretace vyplývající z použití tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->