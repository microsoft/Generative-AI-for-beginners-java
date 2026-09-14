# Inteligența Artificială Generativă pentru Începători - Ediția Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Inteligența Artificială Generativă pentru Începători - Ediția Java](../../translated_images/ro/beg-genai-series.8b48be9951cc574c.webp)

**Timp necesar**: Întregul atelier poate fi parcurs online fără configurare locală. Configurarea mediului durează 2 minute, iar explorarea exemplarelor necesită între 1 și 3 ore, în funcție de profunzimea explorării.

> **Pornire rapidă**

1. Fă fork la acest repositoriu pe contul tău de GitHub
2. Click pe **Code** → tab-ul **Codespaces** → **...** → **New with options...**
3. Folosește valorile implicite – acestea vor selecta containerul de dezvoltare creat pentru acest curs
4. Click pe **Create codespace**
5. Așteaptă aproximativ 2 minute pentru ca mediul să fie pregătit
6. Accesează direct [Capitolul 2: Provision Azure AI Foundry](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## Suport Multi-Limbă

### Suportat prin GitHub Action (Automat și Întotdeauna Actualizat)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabă](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgară](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chineză (Simplificată)](../zh-CN/README.md) | [Chineză (Tradițională, Hong Kong)](../zh-HK/README.md) | [Chineză (Tradițională, Macau)](../zh-MO/README.md) | [Chineză (Tradițională, Taiwan)](../zh-TW/README.md) | [Croată](../hr/README.md) | [Cehă](../cs/README.md) | [Daneză](../da/README.md) | [Olandeză](../nl/README.md) | [Estoniană](../et/README.md) | [Finlandeză](../fi/README.md) | [Franceză](../fr/README.md) | [Germană](../de/README.md) | [Greacă](../el/README.md) | [Ebraică](../he/README.md) | [Hindi](../hi/README.md) | [Maghiară](../hu/README.md) | [Indoneziană](../id/README.md) | [Italiană](../it/README.md) | [Japoneză](../ja/README.md) | [Kannada](../kn/README.md) | [Khmeră](../km/README.md) | [Coreeană](../ko/README.md) | [Lituaniană](../lt/README.md) | [Malaysiană](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepaleză](../ne/README.md) | [Pidgin Nigeriana](../pcm/README.md) | [Norvegiană](../no/README.md) | [Persană (Farsi)](../fa/README.md) | [Poloneză](../pl/README.md) | [Portugheză (Brazilia)](../pt-BR/README.md) | [Portugheză (Portugalia)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Română](./README.md) | [Rusă](../ru/README.md) | [Sârbă (Chirilică)](../sr/README.md) | [Slovacă](../sk/README.md) | [Slovenă](../sl/README.md) | [Spaniolă](../es/README.md) | [Swahili](../sw/README.md) | [Suedeză](../sv/README.md) | [Tagalog (Filipineză)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thailandeză](../th/README.md) | [Turcă](../tr/README.md) | [Ucraineană](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnameză](../vi/README.md)

> **Preferi să clonezi local?**
>
> Acest repository include peste 50 de traduceri în limbi diferite, ceea ce mărește semnificativ dimensiunea descărcării. Pentru a clona fără traduceri, folosește sparse checkout:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> Aceasta îți oferă tot ce ai nevoie pentru a finaliza cursul cu o descărcare mult mai rapidă.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Structura Cursului & Parcursul de Învățare

### **Capitolul 1: Introducere în Inteligența Artificială Generativă**
- **Concepte de bază**: Înțelegerea modelelor mari de limbaj, tokeni, embeddings și capabilitățile AI
- **Ecosistemul Java AI**: Prezentare generală a SDK-urilor Spring AI și OpenAI
- **Protocolul Contextului Modelului**: Introducere în MCP și rolul său în comunicarea agenților AI
- **Aplicații practice**: Scenarii reale, inclusiv chatbots și generare de conținut
- **[→ Începe Capitolul 1](./01-IntroToGenAI/README.md)**

### **Capitolul 2: Configurarea Mediului de Dezvoltare**
- **Azure AI Foundry**: Provizionează chat GPT-5.6 Luna și embeddings text-embedding-3-small cu Bicep și Azure Developer CLI (azd)
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: Învață folosind `ChatClient`, susținut de SDK-ul oficial OpenAI Java și Azure OpenAI v1
- **Autentificare fără chei**: Conectare securizată cu Microsoft Entra ID — fără chei API de gestionat
- **Instrumente de dezvoltare**: Containere Docker, VS Code și configurarea GitHub Codespaces
- **[→ Începe Capitolul 2](./02-SetupDevEnvironment/README.md)**

### **Capitolul 3: Tehnici de bază în Inteligența Artificială Generativă**
- **SDK-ul oficial OpenAI Java**: Apelează direct Azure OpenAI v1 cu autentificare fără chei
- **Ingineria Prompturilor**: Tehnici pentru răspunsuri optime ale modelului AI
- **Embeddări și operațiuni vectoriale**: Implementează căutare semantică și potrivire de similaritate
- **Generare augmentată prin recuperare (RAG)**: Combină AI cu propriile tale surse de date
- **Apelarea funcțiilor**: Extinde capabilitățile AI cu unelte și plugin-uri personalizate
- **[→ Începe Capitolul 3](./03-CoreGenerativeAITechniques/README.md)**

### **Capitolul 4: Aplicații practice & Proiecte**
- **Generator de povești cu animale** (`petstory/`): Generare creativă de conținut cu Azure AI Foundry
- **Demo local Foundry** (`foundrylocal/`): Integrare locală a modelului AI cu OpenAI Java SDK
- **Serviciul MCP Calculator** (`calculator/`): Implementare de bază a Protocolului Contextului Modelului cu Spring AI
- **[→ Începe Capitolul 4](./04-PracticalSamples/README.md)**

### **Capitolul 5: Dezvoltare responsabilă AI**
- **Siguranța conținutului în Azure AI Foundry**: Testează filtrele integrate de conținut și mecanismele de siguranță (blocări dure și refuzuri blânde)
- **Demo AI responsabil**: Exemplu practic care arată modul în care funcționează sistemele moderne de siguranță AI
- **Cele mai bune practici**: Ghiduri esențiale pentru dezvoltarea și implementarea etică a AI
- **[→ Începe Capitolul 5](./05-ResponsibleGenAI/README.md)**

## Resurse suplimentare

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j pentru Începători](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js pentru Începători](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain pentru Începători](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agenți
[![AZD pentru Începători](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI pentru Începători](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP pentru Începători](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Agenți AI pentru Începători](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Seria Inteligenței Artificiale Generative
[![Inteligența Artificială Generativă pentru Începători](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Inteligența Artificială Generativă (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Inteligența Artificială Generativă (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Inteligența Artificială Generativă (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Învățare de bază
[![ML pentru Începători](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science pentru Începători](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI pentru Începători](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Securitate cibernetică pentru Începători](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Dezvoltare Web pentru Începători](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT pentru Începători](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Dezvoltare XR pentru Începători](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Seria Copilot
[![Copilot pentru programare asistată AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot pentru C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Aventura Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Obținerea Ajutorului

Dacă întâmpini dificultăți sau ai întrebări despre construirea aplicațiilor AI, alătură-te altor cursanți și dezvoltatori experimentați în discuții despre MCP. Este o comunitate suportivă unde întrebările sunt binevenite și cunoștințele sunt împărtășite liber.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Dacă ai feedback despre produs sau întâmpini erori în timpul dezvoltării, vizitează:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->