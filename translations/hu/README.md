<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T12:02:43+00:00",
  "source_file": "README.md",
  "language_code": "hu"
}
-->
# Generatív AI kezdőknek - Java kiadás
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generatív AI kezdőknek - Java kiadás](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.hu.png)

**Időráfordítás**: Az egész workshop online elvégezhető helyi környezet beállítása nélkül. Ha futtatni szeretnéd a mintákat, a környezet beállítása 2 percet vesz igénybe, a minták felfedezése pedig 1-3 órát igényelhet a vizsgálat mélységétől függően.

> **Gyors kezdés**

1. Forkold ezt a repót a GitHub fiókodba
2. Kattints a **Code** → **Codespaces** fülre → **...** → **New with options...**
3. Használd az alapértelmezett beállításokat – ez kiválasztja a tanfolyamhoz létrehozott fejlesztői konténert
4. Kattints a **Create codespace** gombra
5. Várj ~2 percet, amíg a környezet készen áll
6. Ugorj egyenesen ide: [GitHub Models Token létrehozása](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Többnyelvű támogatás

### GitHub Action által támogatott (Automatikus és mindig naprakész)

[Francia](../fr/README.md) | [Spanyol](../es/README.md) | [Német](../de/README.md) | [Orosz](../ru/README.md) | [Arab](../ar/README.md) | [Perzsa (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Kínai (Egyszerűsített)](../zh/README.md) | [Kínai (Hagyományos, Makaó)](../mo/README.md) | [Kínai (Hagyományos, Hongkong)](../hk/README.md) | [Kínai (Hagyományos, Tajvan)](../tw/README.md) | [Japán](../ja/README.md) | [Koreai](../ko/README.md) | [Hindi](../hi/README.md) | [Bengáli](../bn/README.md) | [Maráthi](../mr/README.md) | [Nepáli](../ne/README.md) | [Pandzsábi (Gurmukhi)](../pa/README.md) | [Portugál (Portugália)](../pt/README.md) | [Portugál (Brazília)](../br/README.md) | [Olasz](../it/README.md) | [Lengyel](../pl/README.md) | [Török](../tr/README.md) | [Görög](../el/README.md) | [Thai](../th/README.md) | [Svéd](../sv/README.md) | [Dán](../da/README.md) | [Norvég](../no/README.md) | [Finn](../fi/README.md) | [Holland](../nl/README.md) | [Héber](../he/README.md) | [Vietnámi](../vi/README.md) | [Indonéz](../id/README.md) | [Maláj](../ms/README.md) | [Tagalog (Filippínó)](../tl/README.md) | [Szuahéli](../sw/README.md) | [Magyar](./README.md) | [Cseh](../cs/README.md) | [Szlovák](../sk/README.md) | [Román](../ro/README.md) | [Bolgár](../bg/README.md) | [Szerb (Cirill)](../sr/README.md) | [Horvát](../hr/README.md) | [Szlovén](../sl/README.md) | [Ukrán](../uk/README.md) | [Burmai (Mianmar)](../my/README.md)

## Tanfolyam felépítése és tanulási útvonal

### **1. fejezet: Bevezetés a generatív AI-ba**
- **Alapfogalmak**: Nagy nyelvi modellek, tokenek, beágyazások és AI képességek megértése
- **Java AI ökoszisztéma**: Áttekintés a Spring AI és OpenAI SDK-król
- **Model Context Protocol**: Bevezetés az MCP-be és szerepe az AI ügynökök kommunikációjában
- **Gyakorlati alkalmazások**: Valós példák, mint chatbotok és tartalomgenerálás
- **[→ Kezdje az 1. fejezetet](./01-IntroToGenAI/README.md)**

### **2. fejezet: Fejlesztői környezet beállítása**
- **Több szolgáltató konfigurációja**: GitHub Models, Azure OpenAI és OpenAI Java SDK integrációk beállítása
- **Spring Boot + Spring AI**: Legjobb gyakorlatok vállalati AI alkalmazásfejlesztéshez
- **GitHub Models**: Ingyenes AI modell hozzáférés prototípus készítéshez és tanuláshoz (hitelkártya nem szükséges)
- **Fejlesztői eszközök**: Docker konténerek, VS Code és GitHub Codespaces konfiguráció
- **[→ Kezdje a 2. fejezetet](./02-SetupDevEnvironment/README.md)**

### **3. fejezet: Alapvető generatív AI technikák**
- **Prompt Engineering**: Technikák az AI modellek optimális válaszaihoz
- **Beágyazások és vektorműveletek**: Szemantikus keresés és hasonlóság alapú összehasonlítás megvalósítása
- **Retrieval-Augmented Generation (RAG)**: AI kombinálása saját adatforrásokkal
- **Funkcióhívás**: AI képességek bővítése egyedi eszközökkel és bővítményekkel
- **[→ Kezdje a 3. fejezetet](./03-CoreGenerativeAITechniques/README.md)**

### **4. fejezet: Gyakorlati alkalmazások és projektek**
- **Pet Story Generator** (`petstory/`): Kreatív tartalomgenerálás GitHub Models segítségével
- **Foundry Local Demo** (`foundrylocal/`): Helyi AI modell integráció az OpenAI Java SDK-val
- **MCP Calculator Service** (`mcp/calculator/`): Alapvető Model Context Protocol megvalósítás Spring AI-vel
- **[→ Kezdje a 4. fejezetet](./04-PracticalSamples/README.md)**

### **5. fejezet: Felelős AI fejlesztés**
- **GitHub Models biztonság**: Beépített tartalomszűrés és biztonsági mechanizmusok tesztelése
- **Felelős AI demó**: Gyakorlati példa, amely bemutatja, hogyan működnek az AI biztonsági szűrők
- **Legjobb gyakorlatok**: Alapvető irányelvek az etikus AI fejlesztéshez és bevezetéshez
- **[→ Kezdje az 5. fejezetet](./05-ResponsibleGenAI/README.md)**

## További források 

- [AI ügynökök kezdőknek](https://github.com/microsoft/ai-agents-for-beginners)
- [Generatív AI kezdőknek .NET használatával](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generatív AI kezdőknek JavaScript használatával](https://github.com/microsoft/generative-ai-with-javascript)
- [Generatív AI kezdőknek](https://github.com/microsoft/generative-ai-for-beginners)
- [ML kezdőknek](https://aka.ms/ml-beginners)
- [Adattudomány kezdőknek](https://aka.ms/datascience-beginners)
- [AI kezdőknek](https://aka.ms/ai-beginners)
- [Kiberbiztonság kezdőknek](https://github.com/microsoft/Security-101)
- [Webfejlesztés kezdőknek](https://aka.ms/webdev-beginners)
- [IoT kezdőknek](https://aka.ms/iot-beginners)
- [XR fejlesztés kezdőknek](https://github.com/microsoft/xr-development-for-beginners)
- [GitHub Copilot elsajátítása AI páros programozáshoz](https://aka.ms/GitHubCopilotAI)
- [GitHub Copilot elsajátítása C#/.NET fejlesztők számára](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Válassza ki saját Copilot kalandját](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App az Azure AI szolgáltatásokkal](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Felelősségkizárás**:  
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordítási szolgáltatás segítségével készült. Bár törekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelvén tekintendő hiteles forrásnak. Kritikus információk esetén javasolt professzionális, emberi fordítást igénybe venni. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.