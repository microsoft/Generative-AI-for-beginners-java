<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:44:03+00:00",
  "source_file": "README.md",
  "language_code": "it"
}
-->
# Generative AI per Principianti - Edizione Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI per Principianti - Edizione Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.it.png)

**Impegno di tempo**: L'intero workshop può essere completato online senza configurazione locale. La configurazione dell'ambiente richiede 2 minuti, mentre l'esplorazione degli esempi richiede 1-3 ore a seconda della profondità dell'esplorazione.

> **Avvio rapido** 

1. Fork di questo repository sul tuo account GitHub
2. Clicca **Code** → scheda **Codespaces** → **...** → **New with options...**
3. Usa i valori predefiniti – questo selezionerà il contenitore di sviluppo creato per questo corso
4. Clicca **Create codespace**
5. Attendi ~2 minuti che l'ambiente sia pronto
6. Vai direttamente a [Il primo esempio](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Preferisci clonare localmente?**
>
> Questo repository include oltre 50 traduzioni delle lingue che aumentano notevolmente la dimensione del download. Per clonare senza le traduzioni, utilizza sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Questo ti fornisce tutto il necessario per completare il corso con un download molto più veloce.


## Supporto multilingue

### Supportato tramite GitHub Action (Automatico e sempre aggiornato)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabo](../ar/README.md) | [Bengalese](../bn/README.md) | [Bulgaro](../bg/README.md) | [Birmano (Myanmar)](../my/README.md) | [Cinese (Semplificato)](../zh/README.md) | [Cinese (Tradizionale, Hong Kong)](../hk/README.md) | [Cinese (Tradizionale, Macao)](../mo/README.md) | [Cinese (Tradizionale, Taiwan)](../tw/README.md) | [Croato](../hr/README.md) | [Ceco](../cs/README.md) | [Danese](../da/README.md) | [Olandese](../nl/README.md) | [Estone](../et/README.md) | [Finlandese](../fi/README.md) | [Francese](../fr/README.md) | [Tedesco](../de/README.md) | [Greco](../el/README.md) | [Ebraico](../he/README.md) | [Hindi](../hi/README.md) | [Ungherese](../hu/README.md) | [Indonesiano](../id/README.md) | [Italiano](./README.md) | [Giapponese](../ja/README.md) | [Kannada](../kn/README.md) | [Coreano](../ko/README.md) | [Lituano](../lt/README.md) | [Malese](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepalese](../ne/README.md) | [Pidgin nigeriano](../pcm/README.md) | [Norvegese](../no/README.md) | [Persiano (Farsi)](../fa/README.md) | [Polacco](../pl/README.md) | [Portoghese (Brasile)](../br/README.md) | [Portoghese (Portogallo)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Rumeno](../ro/README.md) | [Russo](../ru/README.md) | [Serbo (Cirillico)](../sr/README.md) | [Slovacco](../sk/README.md) | [Sloveno](../sl/README.md) | [Spagnolo](../es/README.md) | [Swahili](../sw/README.md) | [Svedese](../sv/README.md) | [Tagalog (Filippine)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turco](../tr/README.md) | [Ucraino](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamita](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Struttura del corso e percorso di apprendimento

### **Capitolo 1: Introduzione all'IA generativa**
- **Concetti chiave**: Comprendere i modelli di linguaggio di grandi dimensioni (LLM), i token, gli embedding e le capacità dell'IA
- **Ecosistema AI in Java**: Panoramica di Spring AI e degli SDK OpenAI
- **Protocollo Model Context**: Introduzione al MCP e al suo ruolo nella comunicazione degli agenti AI
- **Applicazioni pratiche**: Scenari reali inclusi chatbot e generazione di contenuti
- **[→ Inizia Capitolo 1](./01-IntroToGenAI/README.md)**

### **Capitolo 2: Configurazione dell'ambiente di sviluppo**
- **Configurazione multi-fornitore**: Configurare GitHub Models, Azure OpenAI e integrazioni con OpenAI Java SDK
- **Spring Boot + Spring AI**: Best practice per lo sviluppo di applicazioni AI enterprise
- **GitHub Models**: Accesso gratuito ai modelli AI per prototipazione e apprendimento (non è richiesta la carta di credito)
- **Strumenti di sviluppo**: container Docker, VS Code e configurazione di GitHub Codespaces
- **[→ Inizia Capitolo 2](./02-SetupDevEnvironment/README.md)**

### **Capitolo 3: Tecniche principali di IA generativa**
- **Prompt Engineering**: Tecniche per ottenere risposte ottimali dai modelli AI
- **Embedding e operazioni vettoriali**: Implementare ricerca semantica e confronto di similarità
- **Retrieval-Augmented Generation (RAG)**: Combinare l'IA con le tue fonti di dati
- **Function Calling**: Estendere le capacità dell'IA con strumenti e plugin personalizzati
- **[→ Inizia Capitolo 3](./03-CoreGenerativeAITechniques/README.md)**

### **Capitolo 4: Applicazioni pratiche e progetti**
- **Pet Story Generator** (`petstory/`): Generazione creativa di contenuti con GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): Integrazione di modelli AI locali con OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): Implementazione base del Model Context Protocol con Spring AI
- **[→ Inizia Capitolo 4](./04-PracticalSamples/README.md)**

### **Capitolo 5: Sviluppo responsabile dell'IA**
- **Sicurezza di GitHub Models**: Testare il filtraggio dei contenuti integrato e i meccanismi di sicurezza (blocchi rigidi e rifiuti morbidi)
- **Demo di IA responsabile**: Esempio pratico che mostra come funzionano nella pratica i moderni sistemi di sicurezza dell'IA
- **Best practice**: Linee guida essenziali per uno sviluppo e un deployment etici dell'IA
- **[→ Inizia Capitolo 5](./05-ResponsibleGenAI/README.md)**

## Risorse aggiuntive

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j per Principianti](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js per Principianti](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agenti
[![AZD per Principianti](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI per Principianti](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP per Principianti](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Agenti AI per Principianti](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serie IA generativa
[![Generative AI per Principianti](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Apprendimento core
[![ML per Principianti](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science per Principianti](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![IA per Principianti](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Sicurezza informatica per Principianti](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Sviluppo Web per Principianti](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT per Principianti](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Sviluppo XR per Principianti](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Serie Copilot
[![Copilot per programmazione affiancata con IA](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot per C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Avventura Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Ottenere aiuto

Se rimani bloccato o hai domande sulla creazione di app con IA. Unisciti ad altri studenti e sviluppatori esperti nelle discussioni su MCP. È una community di supporto dove le domande sono benvenute e le conoscenze vengono condivise liberamente.

[![Discord di Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Se hai feedback sul prodotto o riscontri errori durante lo sviluppo, visita:

[![Forum per sviluppatori Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Esclusione di responsabilità:
Questo documento è stato tradotto utilizzando il servizio di traduzione basato su intelligenza artificiale Co-op Translator (https://github.com/Azure/co-op-translator). Pur facendo il possibile per garantire l’accuratezza, si segnala che le traduzioni automatiche possono contenere errori o inesattezze. Il documento originale nella sua lingua d’origine deve essere considerato la fonte autorevole. Per informazioni critiche si raccomanda di ricorrere a una traduzione professionale effettuata da un traduttore umano. Non ci assumiamo alcuna responsabilità per eventuali incomprensioni o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->