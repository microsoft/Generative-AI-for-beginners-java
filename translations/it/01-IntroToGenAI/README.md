<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "75bfb080ca725e8a9aa9c80cae25fba1",
  "translation_date": "2025-07-29T15:17:28+00:00",
  "source_file": "01-IntroToGenAI/README.md",
  "language_code": "it"
}
-->
# Introduzione all'AI Generativa - Edizione Java

## Cosa Imparerai

- **Fondamenti di AI Generativa**, inclusi LLM, prompt engineering, token, embedding e database vettoriali
- **Confronto tra strumenti di sviluppo AI in Java**, come Azure OpenAI SDK, Spring AI e OpenAI Java SDK
- **Scoperta del Model Context Protocol** e del suo ruolo nella comunicazione tra agenti AI

## Indice

- [Introduzione](../../../01-IntroToGenAI)
- [Un rapido ripasso sui concetti di AI Generativa](../../../01-IntroToGenAI)
- [Revisione del prompt engineering](../../../01-IntroToGenAI)
- [Token, embedding e agenti](../../../01-IntroToGenAI)
- [Strumenti e librerie di sviluppo AI per Java](../../../01-IntroToGenAI)
  - [OpenAI Java SDK](../../../01-IntroToGenAI)
  - [Spring AI](../../../01-IntroToGenAI)
  - [Azure OpenAI Java SDK](../../../01-IntroToGenAI)
- [Riepilogo](../../../01-IntroToGenAI)
- [Prossimi Passi](../../../01-IntroToGenAI)

## Introduzione

Benvenuto al primo capitolo di AI Generativa per Principianti - Edizione Java! Questa lezione introduttiva ti guiderà attraverso i concetti fondamentali dell'AI generativa e su come lavorarci utilizzando Java. Imparerai i blocchi essenziali per costruire applicazioni AI, inclusi i Large Language Models (LLM), i token, gli embedding e gli agenti AI. Esploreremo anche i principali strumenti Java che utilizzerai durante questo corso.

### Un rapido ripasso sui concetti di AI Generativa

L'AI generativa è un tipo di intelligenza artificiale che crea nuovi contenuti, come testo, immagini o codice, basandosi su schemi e relazioni appresi dai dati. I modelli di AI generativa possono generare risposte simili a quelle umane, comprendere il contesto e talvolta creare contenuti che sembrano umani.

Sviluppando applicazioni AI in Java, lavorerai con **modelli di AI generativa** per creare contenuti. Alcune capacità dei modelli di AI generativa includono:

- **Generazione di Testo**: Creazione di testo simile a quello umano per chatbot, contenuti e completamento di testi.
- **Generazione e Analisi di Immagini**: Produzione di immagini realistiche, miglioramento di foto e rilevamento di oggetti.
- **Generazione di Codice**: Scrittura di frammenti di codice o script.

Esistono tipi specifici di modelli ottimizzati per diversi compiti. Ad esempio, sia i **Small Language Models (SLM)** che i **Large Language Models (LLM)** possono gestire la generazione di testo, con gli LLM che generalmente offrono prestazioni migliori per compiti complessi. Per attività legate alle immagini, utilizzeresti modelli di visione specializzati o modelli multimodali.

![Figura: Tipi di modelli di AI generativa e casi d'uso.](../../../translated_images/llms.225ca2b8a0d344738419defc5ae14bba2fd3388b94f09fd4e8be8ce2a720ae51.it.png)

Ovviamente, le risposte di questi modelli non sono sempre perfette. Probabilmente hai sentito parlare di modelli che "allucinano" o generano informazioni errate in modo autorevole. Tuttavia, puoi guidare il modello a generare risposte migliori fornendo istruzioni e contesti chiari. È qui che entra in gioco il **prompt engineering**.

#### Revisione del prompt engineering

Il prompt engineering è la pratica di progettare input efficaci per guidare i modelli AI verso output desiderati. Include:

- **Chiarezza**: Rendere le istruzioni chiare e non ambigue.
- **Contesto**: Fornire le informazioni di base necessarie.
- **Vincoli**: Specificare eventuali limitazioni o formati.

Alcune best practice per il prompt engineering includono la progettazione del prompt, istruzioni chiare, suddivisione dei compiti, apprendimento one-shot e few-shot, e il tuning del prompt. Testare diversi prompt è essenziale per trovare ciò che funziona meglio per il tuo caso d'uso specifico.

Quando sviluppi applicazioni, lavorerai con diversi tipi di prompt:
- **Prompt di sistema**: Impostano le regole di base e il contesto per il comportamento del modello.
- **Prompt utente**: I dati di input forniti dagli utenti della tua applicazione.
- **Prompt assistente**: Le risposte del modello basate sui prompt di sistema e utente.

> **Per saperne di più**: Scopri di più sul prompt engineering nel [capitolo Prompt Engineering del corso GenAI per Principianti](https://github.com/microsoft/generative-ai-for-beginners/tree/main/04-prompt-engineering-fundamentals)

#### Token, embedding e agenti

Lavorando con modelli di AI generativa, incontrerai termini come **token**, **embedding**, **agenti** e **Model Context Protocol (MCP)**. Ecco una panoramica dettagliata di questi concetti:

- **Token**: I token sono la più piccola unità di testo in un modello. Possono essere parole, caratteri o sottoparole. I token vengono utilizzati per rappresentare i dati testuali in un formato comprensibile per il modello. Ad esempio, la frase "The quick brown fox jumped over the lazy dog" potrebbe essere tokenizzata come ["The", " quick", " brown", " fox", " jumped", " over", " the", " lazy", " dog"] o ["The", " qu", "ick", " br", "own", " fox", " jump", "ed", " over", " the", " la", "zy", " dog"] a seconda della strategia di tokenizzazione.

![Figura: Esempio di token di AI generativa che suddivide le parole in token](../../../01-IntroToGenAI/images/tokens.webp)

La tokenizzazione è il processo di suddivisione del testo in queste unità più piccole. Questo è cruciale perché i modelli operano sui token piuttosto che sul testo grezzo. Il numero di token in un prompt influisce sulla lunghezza e qualità della risposta del modello, poiché i modelli hanno limiti di token per la loro finestra di contesto (ad esempio, 128K token per il contesto totale di GPT-4o, inclusi input e output).

  In Java, puoi utilizzare librerie come l'OpenAI SDK per gestire automaticamente la tokenizzazione quando invii richieste ai modelli AI.

- **Embedding**: Gli embedding sono rappresentazioni vettoriali dei token che catturano il significato semantico. Sono rappresentazioni numeriche (tipicamente array di numeri in virgola mobile) che permettono ai modelli di comprendere le relazioni tra le parole e generare risposte contestualmente rilevanti. Parole simili hanno embedding simili, consentendo al modello di comprendere concetti come sinonimi e relazioni semantiche.

![Figura: Embedding](../../../translated_images/embedding.398e50802c0037f931c725fd0113747831ea7776434d2b3ba3eb2e7a1a20ab1f.it.png)

  In Java, puoi generare embedding utilizzando l'OpenAI SDK o altre librerie che supportano la generazione di embedding. Questi embedding sono essenziali per attività come la ricerca semantica, dove vuoi trovare contenuti simili basandoti sul significato piuttosto che su corrispondenze testuali esatte.

- **Database vettoriali**: I database vettoriali sono sistemi di archiviazione specializzati ottimizzati per gli embedding. Consentono una ricerca per similarità efficiente e sono cruciali per i pattern di Retrieval-Augmented Generation (RAG), dove è necessario trovare informazioni rilevanti da grandi dataset basandosi sulla similarità semantica piuttosto che su corrispondenze esatte.

![Figura: Architettura di un database vettoriale che mostra come gli embedding vengono archiviati e recuperati per la ricerca di similarità.](../../../translated_images/vector.f12f114934e223dff971b01ca371e85a41a540f3af2ffdd49fb3acec6c6652f2.it.png)

> **Nota**: In questo corso, non tratteremo i database vettoriali, ma riteniamo che valga la pena menzionarli poiché sono comunemente utilizzati nelle applicazioni reali.

- **Agenti & MCP**: Componenti AI che interagiscono autonomamente con modelli, strumenti e sistemi esterni. Il Model Context Protocol (MCP) fornisce un modo standardizzato per gli agenti di accedere in modo sicuro a fonti di dati esterne e strumenti. Scopri di più nel nostro corso [MCP for Beginners](https://github.com/microsoft/mcp-for-beginners).

Nelle applicazioni AI in Java, utilizzerai i token per l'elaborazione del testo, gli embedding per la ricerca semantica e RAG, i database vettoriali per il recupero dei dati e gli agenti con MCP per costruire sistemi intelligenti che utilizzano strumenti.

![Figura: come un prompt diventa una risposta—token, vettori, eventuale ricerca RAG, elaborazione LLM e un agente MCP in un unico flusso rapido.](../../../translated_images/flow.f4ef62c3052d12a88b1d216eb2cd0e2ea3293c806d0defa7921dd1786dcb8516.it.png)

### Strumenti e librerie di sviluppo AI per Java

Java offre ottimi strumenti per lo sviluppo AI. Esistono tre principali librerie che esploreremo durante questo corso: OpenAI Java SDK, Azure OpenAI SDK e Spring AI.

Ecco una tabella di riferimento rapido che mostra quale SDK viene utilizzato negli esempi di ciascun capitolo:

| Capitolo | Esempio | SDK |
|----------|---------|-----|
| 02-SetupDevEnvironment | github-models | OpenAI Java SDK |
| 02-SetupDevEnvironment | basic-chat-azure | Spring AI Azure OpenAI |
| 03-CoreGenerativeAITechniques | examples | Azure OpenAI SDK |
| 04-PracticalSamples | petstory | OpenAI Java SDK |
| 04-PracticalSamples | foundrylocal | OpenAI Java SDK |
| 04-PracticalSamples | calculator | Spring AI MCP SDK + LangChain4j |

**Link alla documentazione degli SDK:**
- [Azure OpenAI Java SDK](https://github.com/Azure/azure-sdk-for-java/tree/azure-ai-openai_1.0.0-beta.16/sdk/openai/azure-ai-openai)
- [Spring AI](https://docs.spring.io/spring-ai/reference/)
- [OpenAI Java SDK](https://github.com/openai/openai-java)
- [LangChain4j](https://docs.langchain4j.dev/)

#### OpenAI Java SDK

L'OpenAI SDK è la libreria Java ufficiale per l'API di OpenAI. Fornisce un'interfaccia semplice e coerente per interagire con i modelli di OpenAI, rendendo facile integrare le capacità AI nelle applicazioni Java. L'esempio GitHub Models del Capitolo 2, l'applicazione Pet Story e l'esempio Foundry Local del Capitolo 4 dimostrano l'approccio con l'OpenAI SDK.

#### Spring AI

Spring AI è un framework completo che porta le capacità AI nelle applicazioni Spring, fornendo un livello di astrazione coerente tra diversi fornitori di AI. Si integra perfettamente con l'ecosistema Spring, rendendolo la scelta ideale per applicazioni Java aziendali che necessitano di funzionalità AI.

La forza di Spring AI risiede nella sua integrazione senza soluzione di continuità con l'ecosistema Spring, rendendo facile costruire applicazioni AI pronte per la produzione con pattern Spring familiari come l'iniezione di dipendenze, la gestione della configurazione e i framework di test. Utilizzerai Spring AI nei Capitoli 2 e 4 per costruire applicazioni che sfruttano sia OpenAI che le librerie Spring AI del Model Context Protocol (MCP).

##### Model Context Protocol (MCP)

Il [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) è uno standard emergente che consente alle applicazioni AI di interagire in modo sicuro con fonti di dati esterne e strumenti. MCP fornisce un modo standardizzato per i modelli AI di accedere a informazioni contestuali ed eseguire azioni nelle tue applicazioni.

Nel Capitolo 4, costruirai un semplice servizio calcolatrice MCP che dimostra i fondamenti del Model Context Protocol con Spring AI, mostrando come creare integrazioni di strumenti di base e architetture di servizio.

#### Azure OpenAI Java SDK

La libreria client Azure OpenAI per Java è un adattamento delle API REST di OpenAI che fornisce un'interfaccia idiomatica e un'integrazione con il resto dell'ecosistema Azure SDK. Nel Capitolo 3, costruirai applicazioni utilizzando l'Azure OpenAI SDK, inclusi applicazioni di chat, chiamate di funzioni e pattern RAG (Retrieval-Augmented Generation).

> Nota: L'Azure OpenAI SDK è meno avanzato rispetto all'OpenAI Java SDK in termini di funzionalità, quindi per progetti futuri considera l'utilizzo dell'OpenAI Java SDK.

## Riepilogo

Ecco cosa hai imparato:

- I concetti fondamentali dell'AI generativa - dagli LLM al prompt engineering, ai token, embedding e database vettoriali
- Le opzioni di strumenti per lo sviluppo AI in Java: Azure OpenAI SDK, Spring AI e OpenAI Java SDK
- Cos'è il Model Context Protocol e come consente agli agenti AI di lavorare con strumenti esterni

## Prossimi Passi

[Capitolo 2: Configurazione dell'Ambiente di Sviluppo](../02-SetupDevEnvironment/README.md)

**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l'accuratezza, si prega di notare che le traduzioni automatiche potrebbero contenere errori o imprecisioni. Il documento originale nella sua lingua nativa dovrebbe essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un traduttore umano. Non siamo responsabili per eventuali incomprensioni o interpretazioni errate derivanti dall'uso di questa traduzione.