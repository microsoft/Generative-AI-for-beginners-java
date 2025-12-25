<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:30:47+00:00",
  "source_file": "README.md",
  "language_code": "hu"
}
-->
# Generatív MI kezdőknek - Java kiadás
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatív MI kezdőknek - Java kiadás](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.hu.png)

**Időráfordítás**: Az egész workshop elvégezhető online, helyi telepítés nélkül. A környezet beállítása körülbelül 2 percet vesz igénybe, a minták felfedezése pedig 1–3 órát igényelhet a mélységtől függően.

> **Gyors kezdés** 
> 
> 1. Forkold ezt a tárolót a GitHub fiókodba
> 2. Kattints a **Code** → **Codespaces** fülre → **...** → **New with options...**
> 3. Használd az alapértelmezetteket – ez kiválasztja a tanfolyamhoz készített Development containt
> 4. Kattints a **Create codespace** gombra
> 5. Várj ~2 percet, amíg a környezet készen lesz
> 6. Ugorj közvetlenül az [Az első példa](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) részhez

> **Inkább helyben klónoznád?**
> 
> Ez a tároló több mint 50 nyelvi fordítást tartalmaz, ami jelentősen megnöveli a letöltési méretet. Ha fordítások nélkül szeretnéd klónozni, használj sparse checkoutot:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ez mindent megad, amire szükséged van a kurzus elvégzéséhez, sokkal gyorsabb letöltéssel.


## Többnyelvű támogatás

### GitHub Action által támogatott (automatikus és mindig naprakész)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arab](../ar/README.md) | [Bengáli](../bn/README.md) | [Bolgár](../bg/README.md) | [Burmai (Mianmar)](../my/README.md) | [Kínai (egyszerűsített)](../zh/README.md) | [Kínai (hagyományos, Hongkong)](../hk/README.md) | [Kínai (hagyományos, Makaó)](../mo/README.md) | [Kínai (hagyományos, Tajvan)](../tw/README.md) | [Horvát](../hr/README.md) | [Cseh](../cs/README.md) | [Dán](../da/README.md) | [Holland](../nl/README.md) | [Észt](../et/README.md) | [Finn](../fi/README.md) | [Francia](../fr/README.md) | [Német](../de/README.md) | [Görög](../el/README.md) | [Héber](../he/README.md) | [Hindi](../hi/README.md) | [Magyar](./README.md) | [Indonéz](../id/README.md) | [Olasz](../it/README.md) | [Japán](../ja/README.md) | [Kannada](../kn/README.md) | [Koreai](../ko/README.md) | [Litván](../lt/README.md) | [Maláj](../ms/README.md) | [Malajálam](../ml/README.md) | [Maráthi](../mr/README.md) | [Nepáli](../ne/README.md) | [Nigériai Pidgin](../pcm/README.md) | [Norvég](../no/README.md) | [Perzsa (Farsi)](../fa/README.md) | [Lengyel](../pl/README.md) | [Portugál (Brazília)](../br/README.md) | [Portugál (Portugália)](../pt/README.md) | [Pandzsábi (Gurmukhi)](../pa/README.md) | [Román](../ro/README.md) | [Orosz](../ru/README.md) | [Szerb (cirill)](../sr/README.md) | [Szlovák](../sk/README.md) | [Szlovén](../sl/README.md) | [Spanyol](../es/README.md) | [Szuahéli](../sw/README.md) | [Svéd](../sv/README.md) | [Tagalog (Filippínó)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Török](../tr/README.md) | [Ukrán](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnami](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Tanfolyam felépítése és tanulási út

### **1. fejezet: Bevezetés a generatív MI-be**
- **Alapfogalmak**: A nagy nyelvi modellek, tokenek, beágyazások és az MI képességeinek megismerése
- **Java MI ökoszisztéma**: Áttekintés a Spring AI és az OpenAI SDK-k használatáról
- **Model Context Protocol**: Bemutatás az MCP-nek és szerepének az MI-ügynökök közötti kommunikációban
- **Gyakorlati alkalmazások**: Valós példák, többek között chatbotok és tartalomgenerálás
- **[→ Indítsd el az 1. fejezetet](./01-IntroToGenAI/README.md)**

### **2. fejezet: Fejlesztőkörnyezet beállítása**
- **Többszolgáltatós konfiguráció**: GitHub Models, Azure OpenAI és OpenAI Java SDK integrációk beállítása
- **Spring Boot + Spring AI**: Legjobb gyakorlatok vállalati MI-alkalmazások fejlesztéséhez
- **GitHub Models**: Ingyenes MI-modell hozzáférés prototípuskészítéshez és tanuláshoz (nem szükséges hitelkártya)
- **Fejlesztői eszközök**: Docker konténerek, VS Code és GitHub Codespaces konfiguráció
- **[→ Indítsd el a 2. fejezetet](./02-SetupDevEnvironment/README.md)**

### **3. fejezet: Alapvető generatív MI technikák**
- **Prompt tervezés**: Technikák az optimális MI-model válaszok eléréséhez
- **Beágyazások & vektorműveletek**: Szemantikus keresés és hasonlóság alapú egyeztetés megvalósítása
- **Retrieval-Augmented Generation (RAG)**: Az MI és a saját adatforrásaid kombinálása
- **Function Calling**: Az MI képességeinek kiterjesztése egyedi eszközökkel és bővítményekkel
- **[→ Indítsd el a 3. fejezetet](./03-CoreGenerativeAITechniques/README.md)**

### **4. fejezet: Gyakorlati alkalmazások és projektek**
- **Pet Story Generator** (`petstory/`): Kreatív tartalomgenerálás a GitHub Models használatával
- **Foundry Local Demo** (`foundrylocal/`): Helyi MI-modell integráció az OpenAI Java SDK-val
- **MCP Calculator Service** (`calculator/`): Alapvető Model Context Protocol megvalósítás Spring AI segítségével
- **[→ Indítsd el a 4. fejezetet](./04-PracticalSamples/README.md)**

### **5. fejezet: Felelős MI-fejlesztés**
- **GitHub Models biztonság**: A beépített tartalomszűrés és biztonsági mechanizmusok tesztelése (szigorú tiltások és lágy elutasítások)
- **Felelős MI bemutató**: Gyakorlati példa arra, hogyan működnek a modern MI-biztonsági rendszerek a gyakorlatban
- **Legjobb gyakorlatok**: Alapvető irányelvek etikus MI-fejlesztéshez és üzembe helyezéshez
- **[→ Indítsd el az 5. fejezetet](./05-ResponsibleGenAI/README.md)**

## További források

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kezdőknek](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kezdőknek](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Ügynökök
[![AZD kezdőknek](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge MI kezdőknek](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kezdőknek](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MI ügynökök kezdőknek](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatív MI sorozat
[![Generatív MI kezdőknek](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatív MI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatív MI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatív MI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Alapozó tanfolyamok
[![Gépi tanulás kezdőknek](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Adattudomány kezdőknek](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![MI kezdőknek](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kiberbiztonság kezdőknek](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Webfejlesztés kezdőknek](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![IoT kezdőknek](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR fejlesztés kezdőknek](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot sorozat
[![Copilot az AI páros programozáshoz](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot C#/.NET-hez](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot kaland](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Segítség

Ha elakadsz, vagy kérdésed van az AI-alkalmazások fejlesztésével kapcsolatban. Csatlakozz más tanulókhoz és tapasztalt fejlesztőkhöz a MCP-ről folytatott beszélgetésekhez. Ez egy támogató közösség, ahol a kérdések szívesen fogadottak, és a tudást szabadon megosztják.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

If you have product feedback or errors while building visit:

[![Microsoft Foundry Fejlesztői Fórum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Felelősségkizárás:
Ezt a dokumentumot a Co‑op Translator (https://github.com/Azure/co-op-translator) nevű mesterséges intelligencián alapuló fordítószolgáltatással fordítottuk. Bár igyekszünk pontos fordítást biztosítani, kérjük, vegye figyelembe, hogy az automatizált fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti, anyanyelvi dokumentum tekintendő mérvadó forrásnak. Kritikus információk esetén professzionális, emberi fordítást javasolunk. Nem vállalunk felelősséget az ezen fordítás használatából eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->