<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:51:42+00:00",
  "source_file": "README.md",
  "language_code": "hr"
}
-->
# Generativna AI za početnike - Java izdanje
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generativna AI za početnike - Java izdanje](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.hr.png)

**Vrijeme potrebno**: Cijeli radni seminar može se završiti online bez lokalne instalacije. Postavljanje okoline traje 2 minute, a istraživanje uzoraka zahtijeva 1–3 sata ovisno o dubini istraživanja.

> **Brzi početak** 

1. Forkajte ovaj repozitorij na vaš GitHub račun
2. Kliknite **Code** → **Codespaces** kartica → **...** → **New with options...**
3. Koristite zadane postavke – ovo će odabrati razvojni kontejner napravljen za ovaj tečaj
4. Kliknite **Create codespace**
5. Pričekajte ~2 minute da se okolina pripremi
6. Pređite izravno na [→ Započni Poglavlje 1](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Preferirate klonirati lokalno?**
>
> Ovaj repozitorij uključuje 50+ prijevoda na jezike što značajno povećava veličinu preuzimanja. Za kloniranje bez prijevoda, koristite sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ovo vam daje sve što vam treba za dovršetak tečaja s mnogo bržim preuzimanjem.


## Podrška za više jezika

### Podržano putem GitHub Action (Automatizirano i uvijek ažurno)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arapski](../ar/README.md) | [Bengalski](../bn/README.md) | [Bugarski](../bg/README.md) | [Burmanski (Myanmar)](../my/README.md) | [Kineski (pojednostavljeni)](../zh/README.md) | [Kineski (tradicionalni, Hong Kong)](../hk/README.md) | [Kineski (tradicionalni, Macau)](../mo/README.md) | [Kineski (tradicionalni, Taiwan)](../tw/README.md) | [Hrvatski](./README.md) | [Češki](../cs/README.md) | [Danski](../da/README.md) | [Nizozemski](../nl/README.md) | [Estonski](../et/README.md) | [Finski](../fi/README.md) | [Francuski](../fr/README.md) | [Njemački](../de/README.md) | [Grčki](../el/README.md) | [Hebrejski](../he/README.md) | [Hindi](../hi/README.md) | [Mađarski](../hu/README.md) | [Indonezijski](../id/README.md) | [Talijanski](../it/README.md) | [Japanski](../ja/README.md) | [Kannada](../kn/README.md) | [Korejski](../ko/README.md) | [Litvanski](../lt/README.md) | [Malajski](../ms/README.md) | [Malajalamski](../ml/README.md) | [Marathi](../mr/README.md) | [Nepalski](../ne/README.md) | [Nigerijski pidgin](../pcm/README.md) | [Norveški](../no/README.md) | [Perzijski (Farsi)](../fa/README.md) | [Poljski](../pl/README.md) | [Portugalski (Brazil)](../br/README.md) | [Portugalski (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Rumunjski](../ro/README.md) | [Ruski](../ru/README.md) | [Srpski (ćirilica)](../sr/README.md) | [Slovački](../sk/README.md) | [Slovenski](../sl/README.md) | [Španjolski](../es/README.md) | [Svahili](../sw/README.md) | [Švedski](../sv/README.md) | [Tagalog (filipinski)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Tajlandski](../th/README.md) | [Turski](../tr/README.md) | [Ukrajinski](../uk/README.md) | [Urdu](../ur/README.md) | [Vijetnamski](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Struktura tečaja i put učenja

### **Poglavlje 1: Uvod u generativnu AI**
- **Temeljni pojmovi**: Razumijevanje velikih jezičnih modela, tokena, embeddings i mogućnosti AI
- **Java AI ekosustav**: Pregled Spring AI i OpenAI SDK-ova
- **Model Context Protocol**: Uvod u MCP i njegova uloga u komunikaciji AI agenata
- **Praktične primjene**: Stvarni scenariji uključujući chatbote i generiranje sadržaja
- **[→ Započni Poglavlje 1](./01-IntroToGenAI/README.md)**

### **Poglavlje 2: Postavljanje razvojne okoline**
- **Konfiguracija za više pružatelja**: Postavite GitHub Models, Azure OpenAI i OpenAI Java SDK integracije
- **Spring Boot + Spring AI**: Najbolje prakse za razvoj enterprise AI aplikacija
- **GitHub Models**: Besplatan pristup AI modelima za prototipiranje i učenje (nije potrebna kreditna kartica)
- **Alati za razvoj**: Docker kontejneri, VS Code i konfiguracija GitHub Codespaces
- **[→ Započni Poglavlje 2](./02-SetupDevEnvironment/README.md)**

### **Poglavlje 3: Temeljne tehnike generativne AI**
- **Prompt inženjering**: Tehnike za optimalne odgovore AI modela
- **Embeddings i operacije nad vektorima**: Implementacija semantičkog pretraživanja i podudaranja sličnosti
- **Retrieval-Augmented Generation (RAG)**: Kombinirajte AI s vlastitim izvorima podataka
- **Pozivanje funkcija**: Proširite mogućnosti AI prilagođenim alatima i dodacima
- **[→ Započni Poglavlje 3](./03-CoreGenerativeAITechniques/README.md)**

### **Poglavlje 4: Praktične primjene i projekti**
- **Generator priča o ljubimcima** (`petstory/`): Kreativno generiranje sadržaja s GitHub Models
- **Foundry lokalna demonstracija** (`foundrylocal/`): Lokalna integracija AI modela s OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): Osnovna implementacija Model Context Protocol-a sa Spring AI
- **[→ Započni Poglavlje 4](./04-PracticalSamples/README.md)**

### **Poglavlje 5: Odgovorni razvoj umjetne inteligencije**
- **Sigurnost GitHub Models**: Testirajte ugrađeno filtriranje sadržaja i sigurnosne mehanizme (tvrdih blokada i mekanih odbijanja)
- **Demo odgovornog AI**: Praktični primjer koji pokazuje kako suvremeni AI sigurnosni sustavi djeluju u praksi
- **Najbolje prakse**: Ključne smjernice za etički razvoj i implementaciju AI
- **[→ Započni Poglavlje 5](./05-ResponsibleGenAI/README.md)**

## Dodatni resursi

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j za početnike](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js za početnike](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agenti
[![AZD za početnike](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI za početnike](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP za početnike](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI agenti za početnike](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generativna AI serija
[![Generativna AI za početnike](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generativna AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Temeljno učenje
[![Strojno učenje za početnike](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Znanost o podacima za početnike](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI za početnike](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kibernetička sigurnost za početnike](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web razvoj za početnike](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![IoT za početnike](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Razvoj XR za početnike](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serija Copilot
[![Copilot za AI u programiranju u paru](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot za C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot avantura](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Dobivanje pomoći

Ako zapnete ili imate bilo kakvih pitanja o izradi AI aplikacija, pridružite se drugim učenicima i iskusnim programerima u raspravama o MCP-u. To je podržavajuća zajednica u kojoj su pitanja dobrodošla, a znanje se slobodno dijeli.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Ako imate povratne informacije o proizvodu ili primijetite pogreške tijekom izrade, posjetite:

[![Microsoft Foundry forum za programere](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Odricanje odgovornosti**:
Ovaj dokument je preveden pomoću AI usluge prevođenja [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo postići točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati mjerodavnim izvorom. Za kritične informacije preporučujemo profesionalni ljudski prijevod. Ne snosimo odgovornost za bilo kakve nesporazume ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->