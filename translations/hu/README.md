<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:43:15+00:00",
  "source_file": "README.md",
  "language_code": "hu"
}
-->
# Generatív AI kezdőknek - Java kiadás
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatív AI kezdőknek - Java kiadás](../../translated_images/hu/beg-genai-series.8b48be9951cc574c.webp)

**Időigény**: Az egész workshop online, helyi telepítés nélkül elvégezhető. A környezet beállítása 2 percet vesz igénybe, a minták felfedezése pedig az elmélyülés mértékétől függően 1-3 órát igényel.

> **Gyors kezdés**

1. Forkold ezt a tárolót a GitHub fiókodba
2. Kattints a **Code** → **Codespaces** fülre → **...** → **Új lehetőségekkel...**
3. Használd az alapértelmezetteket – ez kiválasztja a kurzushoz létrehozott fejlesztői konténert
4. Kattints a **Codespace létrehozása** gombra
5. Várj kb. 2 percet, amíg a környezet készen áll
6. Ugrás közvetlenül az [Első példához](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Inkább helyileg klónoznál?**
>
> Ez a tároló több mint 50 nyelvű fordítást tartalmaz, ami jelentősen megnöveli a letöltési méretet. Ha fordítások nélkül szeretnéd klónozni, használd a sparse checkout-ot:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ez mindent tartalmaz, amire a kurzus befejezéséhez szükséged van, sokkal gyorsabb letöltéssel.

## Többnyelvű támogatás

### GitHub Action segítségével támogatott (automatikus és mindig naprakész)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arab](../ar/README.md) | [Bengáli](../bn/README.md) | [Bolgár](../bg/README.md) | [Burmai (Myanmar)](../my/README.md) | [Kínai (egyszerűsített)](../zh/README.md) | [Kínai (hagyományos, Hong Kong)](../hk/README.md) | [Kínai (hagyományos, Makaó)](../mo/README.md) | [Kínai (hagyományos, Tajvan)](../tw/README.md) | [Horvát](../hr/README.md) | [Cseh](../cs/README.md) | [Dán](../da/README.md) | [Holland](../nl/README.md) | [Észt](../et/README.md) | [Finn](../fi/README.md) | [Francia](../fr/README.md) | [Német](../de/README.md) | [Görög](../el/README.md) | [Héber](../he/README.md) | [Hindi](../hi/README.md) | [Magyar](./README.md) | [Indonéz](../id/README.md) | [Olasz](../it/README.md) | [Japán](../ja/README.md) | [Kannada](../kn/README.md) | [Koreai](../ko/README.md) | [Litván](../lt/README.md) | [Maláj](../ms/README.md) | [Malajálam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepáli](../ne/README.md) | [Nigériai pidgin](../pcm/README.md) | [Norvég](../no/README.md) | [Perzsa (Fárszi)](../fa/README.md) | [Lengyel](../pl/README.md) | [Portugál (Brazília)](../br/README.md) | [Portugál (Portugália)](../pt/README.md) | [Pandzsábi (Gurmukhi)](../pa/README.md) | [Román](../ro/README.md) | [Orosz](../ru/README.md) | [Szerb (cirill)](../sr/README.md) | [Szlovák](../sk/README.md) | [Szlovén](../sl/README.md) | [Spanyol](../es/README.md) | [Szuahéli](../sw/README.md) | [Svéd](../sv/README.md) | [Tagalog (filippínó)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Török](../tr/README.md) | [Ukrán](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnami](../vi/README.md)

> **Inkább helyileg klónoznál?**

> Ez a tároló több mint 50 nyelvű fordítást tartalmaz, ami jelentősen megnöveli a letöltési méretet. Ha fordítások nélkül szeretnéd klónozni, használd a sparse checkout-ot:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ez mindent tartalmaz, amire a kurzus befejezéséhez szükséged van, sokkal gyorsabb letöltéssel.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kurzus felépítése és tanulási útvonal

### **1. fejezet: Bevezetés a generatív AI-ba**
- **Alapfogalmak**: Nagy nyelvi modellek, tokenek, beágyazások és az AI képességeinek megértése
- **Java AI ökoszisztéma**: Spring AI és OpenAI SDK-k áttekintése
- **Model Context Protocol (MCP)**: Bevezető az MCP-be és szerepe az AI ügynökök közti kommunikációban
- **Gyakorlati alkalmazások**: Valós életbeli forgatókönyvek, beleértve chatbotokat és tartalomgenerálást
- **[→ 1. fejezet kezdése](./01-IntroToGenAI/README.md)**

### **2. fejezet: Fejlesztői környezet beállítása**
- **Több szolgáltató konfigurálása**: GitHub Models, Azure OpenAI és OpenAI Java SDK integrációk beállítása
- **Spring Boot + Spring AI**: Legjobb gyakorlatok vállalati AI alkalmazások fejlesztéséhez
- **GitHub Models**: Ingyenes AI modellhozzáférés prototípus készítéshez és tanuláshoz (bankkártya nem szükséges)
- **Fejlesztői eszközök**: Docker konténerek, VS Code és GitHub Codespaces konfiguráció
- **[→ 2. fejezet kezdése](./02-SetupDevEnvironment/README.md)**

### **3. fejezet: Alapvető generatív AI technikák**
- **Prompt mérnökség**: Az AI modell optimális válaszainak technikái
- **Beágyazások és vektorműveletek**: Szemantikus keresés és hasonlóságkeresés megvalósítása
- **Retrieval-Augmented Generation (RAG)**: AI kombinálása saját adatforrásaiddal
- **Funkcióhívás**: Az AI képességek bővítése egyedi eszközökkel és bővítményekkel
- **[→ 3. fejezet kezdése](./03-CoreGenerativeAITechniques/README.md)**

### **4. fejezet: Gyakorlati alkalmazások és projektek**
- **Háziállat-történet generátor** (`petstory/`): Kreatív tartalomgenerálás GitHub Models segítségével
- **Foundry helyi demó** (`foundrylocal/`): Helyi AI modell integráció OpenAI Java SDK-val
- **MCP kalkulátor szolgáltatás** (`calculator/`): Alapvető Model Context Protocol megvalósítás Spring AI-val
- **[→ 4. fejezet kezdése](./04-PracticalSamples/README.md)**

### **5. fejezet: Felelős AI fejlesztés**
- **GitHub Models biztonság**: Beépített tartalomszűrés és biztonsági mechanizmusok tesztelése (kemény blokkok és lágy visszautasítások)
- **Felelős AI bemutató**: Kézzel fogható példa arra, hogyan működnek a modern AI biztonsági rendszerek a gyakorlatban
- **Legjobb gyakorlatok**: Lényeges irányelvek az etikus AI fejlesztéshez és bevezetéshez
- **[→ 5. fejezet kezdése](./05-ResponsibleGenAI/README.md)**

## További források

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j kezdőknek](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js kezdőknek](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Ügynökök
[![AZD kezdőknek](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI kezdőknek](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP kezdőknek](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Ügynökök kezdőknek](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatív AI sorozat
[![Generatív AI kezdőknek](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatív AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatív AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatív AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Alapvető tanulás
[![Gépi tanulás kezdőknek](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Adattudomány kezdőknek](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI kezdőknek](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kiberbiztonság kezdőknek](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Webfejlesztés kezdőknek](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Sorozat
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Segítség kérése

Ha elakad vagy kérdése van az AI alkalmazások fejlesztésével kapcsolatban. Csatlakozzon a tanulók és tapasztalt fejlesztők közösségéhez, akik az MCP-ről folytatnak beszélgetéseket. Ez egy támogató közösség, ahol a kérdések megengedettek és a tudás szabadon megosztott.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Ha termék-visszajelzése vagy hibák merülnek fel a fejlesztés során, látogasson el ide:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Nyilatkozat**:
Ezt a dokumentumot az [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordító szolgáltatásával fordították le. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javaslunk. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->