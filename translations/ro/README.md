<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T12:10:42+00:00",
  "source_file": "README.md",
  "language_code": "ro"
}
-->
# Generative AI pentru Începători - Ediția Java
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generative AI pentru Începători - Ediția Java](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.ro.png)

**Timp necesar**: Întregul atelier poate fi finalizat online fără configurare locală. Dacă dorești să rulezi exemplele, configurarea mediului durează 2 minute, iar explorarea exemplelor necesită între 1-3 ore, în funcție de profunzimea explorării.

> **Start Rapid**

1. Clonează acest depozit în contul tău GitHub
2. Apasă pe **Code** → fila **Codespaces** → **...** → **New with options...**
3. Folosește setările implicite – acestea vor selecta containerul de dezvoltare creat pentru acest curs
4. Apasă pe **Create codespace**
5. Așteaptă ~2 minute pentru ca mediul să fie gata
6. Treci direct la [Crearea Token-ului pentru Modelele GitHub](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Suport Multilingv

### Suportat prin GitHub Action (Automat & Întotdeauna Actualizat)

[Franceză](../fr/README.md) | [Spaniolă](../es/README.md) | [Germană](../de/README.md) | [Rusă](../ru/README.md) | [Arabă](../ar/README.md) | [Persană (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chineză (Simplificată)](../zh/README.md) | [Chineză (Tradițională, Macau)](../mo/README.md) | [Chineză (Tradițională, Hong Kong)](../hk/README.md) | [Chineză (Tradițională, Taiwan)](../tw/README.md) | [Japoneză](../ja/README.md) | [Coreeană](../ko/README.md) | [Hindi](../hi/README.md) | [Bengaleză](../bn/README.md) | [Marathi](../mr/README.md) | [Nepaleză](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portugheză (Portugalia)](../pt/README.md) | [Portugheză (Brazilia)](../br/README.md) | [Italiană](../it/README.md) | [Poloneză](../pl/README.md) | [Turcă](../tr/README.md) | [Greacă](../el/README.md) | [Thailandeză](../th/README.md) | [Suedeză](../sv/README.md) | [Daneză](../da/README.md) | [Norvegiană](../no/README.md) | [Finlandeză](../fi/README.md) | [Olandeză](../nl/README.md) | [Ebraică](../he/README.md) | [Vietnameză](../vi/README.md) | [Indoneziană](../id/README.md) | [Malayeză](../ms/README.md) | [Tagalog (Filipineză)](../tl/README.md) | [Swahili](../sw/README.md) | [Maghiară](../hu/README.md) | [Cehă](../cs/README.md) | [Slovacă](../sk/README.md) | [Română](./README.md) | [Bulgară](../bg/README.md) | [Sârbă (Chirilică)](../sr/README.md) | [Croată](../hr/README.md) | [Slovenă](../sl/README.md) | [Ucraineană](../uk/README.md) | [Birmaneză (Myanmar)](../my/README.md)

## Structura Cursului & Parcursul de Învățare

### **Capitolul 1: Introducere în Generative AI**
- **Concepte de bază**: Înțelegerea modelelor lingvistice mari, token-urilor, embedding-urilor și capacităților AI
- **Ecosistemul Java AI**: Prezentare generală a Spring AI și SDK-urilor OpenAI
- **Protocolul Contextului Modelului**: Introducere în MCP și rolul său în comunicarea agenților AI
- **Aplicații practice**: Scenarii reale, inclusiv chatboți și generarea de conținut
- **[→ Începe Capitolul 1](./01-IntroToGenAI/README.md)**

### **Capitolul 2: Configurarea Mediului de Dezvoltare**
- **Configurare Multi-Provider**: Integrarea GitHub Models, Azure OpenAI și OpenAI Java SDK
- **Spring Boot + Spring AI**: Cele mai bune practici pentru dezvoltarea aplicațiilor AI în mediul enterprise
- **GitHub Models**: Acces gratuit la modele AI pentru prototipare și învățare (fără card de credit necesar)
- **Instrumente de Dezvoltare**: Configurarea containerelor Docker, VS Code și GitHub Codespaces
- **[→ Începe Capitolul 2](./02-SetupDevEnvironment/README.md)**

### **Capitolul 3: Tehnici de Bază în Generative AI**
- **Ingineria Prompt-urilor**: Tehnici pentru răspunsuri optime ale modelelor AI
- **Embedding-uri & Operațiuni pe Vectori**: Implementarea căutării semantice și potrivirii similitudinii
- **Generare Augmentată prin Recuperare (RAG)**: Combinarea AI cu sursele proprii de date
- **Apelarea Funcțiilor**: Extinderea capacităților AI cu instrumente și plugin-uri personalizate
- **[→ Începe Capitolul 3](./03-CoreGenerativeAITechniques/README.md)**

### **Capitolul 4: Aplicații Practice & Proiecte**
- **Generator de Povești pentru Animale de Companie** (`petstory/`): Generarea creativă de conținut cu GitHub Models
- **Demo Local Foundry** (`foundrylocal/`): Integrarea modelelor AI locale cu OpenAI Java SDK
- **Serviciu Calculator MCP** (`mcp/calculator/`): Implementare de bază a Protocolului Contextului Modelului cu Spring AI
- **[→ Începe Capitolul 4](./04-PracticalSamples/README.md)**

### **Capitolul 5: Dezvoltare AI Responsabilă**
- **Siguranța GitHub Models**: Testarea mecanismelor de filtrare a conținutului și siguranță integrate
- **Demo AI Responsabil**: Exemplu practic care arată cum funcționează filtrele de siguranță AI
- **Cele mai bune practici**: Ghiduri esențiale pentru dezvoltarea și implementarea etică a AI
- **[→ Începe Capitolul 5](./05-ResponsibleGenAI/README.md)**

## Resurse Suplimentare 

- [AI Agents For Beginners](https://github.com/microsoft/ai-agents-for-beginners)
- [Generative AI pentru Începători folosind .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generative AI pentru Începători folosind JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generative AI pentru Începători](https://github.com/microsoft/generative-ai-for-beginners)
- [ML pentru Începători](https://aka.ms/ml-beginners)
- [Data Science pentru Începători](https://aka.ms/datascience-beginners)
- [AI pentru Începători](https://aka.ms/ai-beginners)
- [Securitate Cibernetică pentru Începători](https://github.com/microsoft/Security-101)
- [Dezvoltare Web pentru Începători](https://aka.ms/webdev-beginners)
- [IoT pentru Începători](https://aka.ms/iot-beginners)
- [Dezvoltare XR pentru Începători](https://github.com/microsoft/xr-development-for-beginners)
- [Stăpânirea GitHub Copilot pentru Programare în Echipa AI](https://aka.ms/GitHubCopilotAI)
- [Stăpânirea GitHub Copilot pentru Dezvoltatori C#/.NET](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Alege-ți Propria Aventură cu Copilot](https://github.com/microsoft/CopilotAdventures)
- [Aplicație Chat RAG cu Servicii Azure AI](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Declinarea responsabilității**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa natală ar trebui considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm răspunderea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea din utilizarea acestei traduceri.