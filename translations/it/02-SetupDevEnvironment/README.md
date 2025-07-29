<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:16:36+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "it"
}
-->
# Configurare l'Ambiente di Sviluppo per Generative AI con Java

> **Avvio Rapido**: Codifica nel Cloud in 2 minuti - Vai a [Configurazione di GitHub Codespaces](../../../02-SetupDevEnvironment) - nessuna installazione locale richiesta e utilizza i modelli di GitHub!

> **Interessato ad Azure OpenAI?**, consulta la nostra [Guida alla Configurazione di Azure OpenAI](getting-started-azure-openai.md) con i passaggi per creare una nuova risorsa Azure OpenAI.

## Cosa Imparerai

- Configurare un ambiente di sviluppo Java per applicazioni AI
- Scegliere e configurare il tuo ambiente di sviluppo preferito (cloud-first con Codespaces, contenitore di sviluppo locale o configurazione locale completa)
- Testare la configurazione collegandoti ai Modelli di GitHub

## Indice

- [Cosa Imparerai](../../../02-SetupDevEnvironment)
- [Introduzione](../../../02-SetupDevEnvironment)
- [Passo 1: Configura il Tuo Ambiente di Sviluppo](../../../02-SetupDevEnvironment)
  - [Opzione A: GitHub Codespaces (Consigliato)](../../../02-SetupDevEnvironment)
  - [Opzione B: Contenitore di Sviluppo Locale](../../../02-SetupDevEnvironment)
  - [Opzione C: Usa la Tua Installazione Locale Esistente](../../../02-SetupDevEnvironment)
- [Passo 2: Crea un Token di Accesso Personale GitHub](../../../02-SetupDevEnvironment)
- [Passo 3: Testa la Configurazione](../../../02-SetupDevEnvironment)
- [Risoluzione dei Problemi](../../../02-SetupDevEnvironment)
- [Riepilogo](../../../02-SetupDevEnvironment)
- [Prossimi Passi](../../../02-SetupDevEnvironment)

## Introduzione

Questo capitolo ti guiderà nella configurazione di un ambiente di sviluppo. Utilizzeremo **GitHub Models** come esempio principale perché è gratuito, facile da configurare con un account GitHub, non richiede una carta di credito e offre accesso a diversi modelli per sperimentare.

**Nessuna configurazione locale richiesta!** Puoi iniziare a programmare immediatamente utilizzando GitHub Codespaces, che fornisce un ambiente di sviluppo completo direttamente nel tuo browser.

<img src="./images/models.webp" alt="Screenshot: GitHub Models" width="50%">

Consigliamo di utilizzare [**GitHub Models**](https://github.com/marketplace?type=models) per questo corso perché:
- È **gratuito** per iniziare
- È **facile** da configurare con un account GitHub
- **Non richiede carta di credito**
- Offre **molteplici modelli** per sperimentare

> **Nota**: I Modelli di GitHub utilizzati in questa formazione hanno i seguenti limiti gratuiti:
> - 15 richieste al minuto (150 al giorno)
> - ~8.000 parole in ingresso, ~4.000 parole in uscita per richiesta
> - 5 richieste simultanee
> 
> Per l'uso in produzione, esegui l'upgrade ai Modelli di Azure AI Foundry con il tuo account Azure. Il tuo codice non necessita di modifiche. Consulta la [documentazione di Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Passo 1: Configura il Tuo Ambiente di Sviluppo

<a name="quick-start-cloud"></a>

Abbiamo creato un contenitore di sviluppo preconfigurato per ridurre al minimo i tempi di configurazione e garantire che tu abbia tutti gli strumenti necessari per questo corso su Generative AI con Java. Scegli il tuo approccio di sviluppo preferito:

### Opzioni di Configurazione dell'Ambiente:

#### Opzione A: GitHub Codespaces (Consigliato)

**Inizia a programmare in 2 minuti - nessuna configurazione locale richiesta!**

1. Fai un fork di questo repository sul tuo account GitHub
   > **Nota**: Se desideri modificare la configurazione di base, consulta la [Configurazione del Contenitore di Sviluppo](../../../.devcontainer/devcontainer.json)
2. Clicca su **Code** → scheda **Codespaces** → **...** → **New with options...**
3. Usa le impostazioni predefinite – questo selezionerà la **Configurazione del Contenitore di Sviluppo**: **Generative AI Java Development Environment**, un contenitore personalizzato creato per questo corso
4. Clicca su **Create codespace**
5. Attendi circa 2 minuti affinché l'ambiente sia pronto
6. Procedi a [Passo 2: Crea il Token GitHub](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Screenshot: Sottomenu Codespaces" width="50%">

<img src="./images/image.png" alt="Screenshot: Nuovo con opzioni" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Opzioni per creare un codespace" width="50%">

> **Vantaggi di Codespaces**:
> - Nessuna installazione locale richiesta
> - Funziona su qualsiasi dispositivo con un browser
> - Preconfigurato con tutti gli strumenti e le dipendenze
> - 60 ore gratuite al mese per account personali
> - Ambiente coerente per tutti i partecipanti

#### Opzione B: Contenitore di Sviluppo Locale

**Per sviluppatori che preferiscono lo sviluppo locale con Docker**

1. Fai un fork e clona questo repository sulla tua macchina locale
   > **Nota**: Se desideri modificare la configurazione di base, consulta la [Configurazione del Contenitore di Sviluppo](../../../.devcontainer/devcontainer.json)
2. Installa [Docker Desktop](https://www.docker.com/products/docker-desktop/) e [VS Code](https://code.visualstudio.com/)
3. Installa l'[estensione Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) in VS Code
4. Apri la cartella del repository in VS Code
5. Quando richiesto, clicca su **Reopen in Container** (o usa `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Attendi che il contenitore venga costruito e avviato
7. Procedi a [Passo 2: Crea il Token GitHub](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Screenshot: Configurazione del contenitore di sviluppo" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Costruzione del contenitore completata" width="50%">

#### Opzione C: Usa la Tua Installazione Locale Esistente

**Per sviluppatori con ambienti Java già configurati**

Prerequisiti:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) o il tuo IDE preferito

Passaggi:
1. Clona questo repository sulla tua macchina locale
2. Apri il progetto nel tuo IDE
3. Procedi a [Passo 2: Crea il Token GitHub](../../../02-SetupDevEnvironment)

> **Suggerimento Pro**: Se hai un computer con specifiche basse ma vuoi usare VS Code localmente, utilizza GitHub Codespaces! Puoi connettere il tuo VS Code locale a un Codespace ospitato nel cloud per ottenere il meglio di entrambi i mondi.

<img src="./images/image-2.png" alt="Screenshot: Istanza locale del contenitore di sviluppo creata" width="50%">

## Passo 2: Crea un Token di Accesso Personale GitHub

1. Vai su [Impostazioni GitHub](https://github.com/settings/profile) e seleziona **Settings** dal menu del tuo profilo.
2. Nella barra laterale sinistra, clicca su **Developer settings** (di solito in fondo).
3. Sotto **Personal access tokens**, clicca su **Fine-grained tokens** (o segui questo [link diretto](https://github.com/settings/personal-access-tokens)).
4. Clicca su **Generate new token**.
5. Sotto "Token name", fornisci un nome descrittivo (es. `GenAI-Java-Course-Token`).
6. Imposta una data di scadenza (consigliato: 7 giorni per motivi di sicurezza).
7. Sotto "Resource owner", seleziona il tuo account utente.
8. Sotto "Repository access", seleziona i repository che vuoi utilizzare con i Modelli di GitHub (o "All repositories" se necessario).
9. Sotto "Repository permissions", trova **Models** e impostalo su **Read and write**.
10. Clicca su **Generate token**.
11. **Copia e salva il tuo token ora** – non potrai più visualizzarlo!

> **Consiglio di Sicurezza**: Usa l'ambito minimo richiesto e il tempo di scadenza più breve possibile per i tuoi token di accesso.

## Passo 3: Testa la Configurazione con l'Esempio GitHub Models

Una volta che il tuo ambiente di sviluppo è pronto, testiamo l'integrazione con i Modelli di GitHub utilizzando la nostra applicazione di esempio in [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Apri il terminale nel tuo ambiente di sviluppo.
2. Vai all'esempio GitHub Models:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Imposta il tuo token GitHub come variabile d'ambiente:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Esegui l'applicazione:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Dovresti vedere un output simile a:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Comprendere il Codice d'Esempio

Prima, capiamo cosa abbiamo appena eseguito. L'esempio sotto `examples/github-models` utilizza l'SDK Java di OpenAI per connettersi ai Modelli di GitHub:

**Cosa fa questo codice:**
- **Si connette** ai Modelli di GitHub utilizzando il tuo token di accesso personale
- **Invia** un semplice messaggio "Say Hello World!" al modello AI
- **Riceve** e visualizza la risposta dell'AI
- **Convalida** che la configurazione funzioni correttamente

**Dipendenza Chiave** (in `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Codice Principale** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Riepilogo

Ottimo! Ora hai tutto configurato:

- Creato un Token di Accesso Personale GitHub con le autorizzazioni corrette per l'accesso ai modelli AI
- Avviato il tuo ambiente di sviluppo Java (sia con Codespaces, contenitori di sviluppo o locale)
- Connesso ai Modelli di GitHub utilizzando l'SDK Java di OpenAI per lo sviluppo AI gratuito
- Testato che tutto funzioni con un semplice esempio che comunica con i modelli AI

## Prossimi Passi

[Capitolo 3: Tecniche Fondamentali di Generative AI](../03-CoreGenerativeAITechniques/README.md)

## Risoluzione dei Problemi

Hai problemi? Ecco i problemi comuni e le soluzioni:

- **Il token non funziona?** 
  - Assicurati di aver copiato l'intero token senza spazi extra
  - Verifica che il token sia impostato correttamente come variabile d'ambiente
  - Controlla che il tuo token abbia le autorizzazioni corrette (Models: Read and write)

- **Maven non trovato?** 
  - Se utilizzi contenitori di sviluppo/Codespaces, Maven dovrebbe essere preinstallato
  - Per configurazioni locali, assicurati che Java 21+ e Maven 3.9+ siano installati
  - Prova `mvn --version` per verificare l'installazione

- **Problemi di connessione?** 
  - Controlla la tua connessione internet
  - Verifica che GitHub sia accessibile dalla tua rete
  - Assicurati di non essere dietro un firewall che blocca l'endpoint dei Modelli di GitHub

- **Il contenitore di sviluppo non si avvia?** 
  - Assicurati che Docker Desktop sia in esecuzione (per lo sviluppo locale)
  - Prova a ricostruire il contenitore: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Errori di compilazione dell'applicazione?**
  - Assicurati di essere nella directory corretta: `02-SetupDevEnvironment/examples/github-models`
  - Prova a pulire e ricostruire: `mvn clean compile`

> **Hai bisogno di aiuto?**: Hai ancora problemi? Apri un problema nel repository e ti aiuteremo.

**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l'accuratezza, si prega di notare che le traduzioni automatiche potrebbero contenere errori o imprecisioni. Il documento originale nella sua lingua nativa dovrebbe essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un esperto umano. Non siamo responsabili per eventuali fraintendimenti o interpretazioni errate derivanti dall'uso di questa traduzione.