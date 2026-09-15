# Configurare l'ambiente di sviluppo per Azure AI Foundry

> Questa guida configura i modelli di **Azure AI Foundry** per le app AI Java in questo corso, utilizzando l'autenticazione **senza chiave** (Microsoft Entra ID) — nessuna chiave API da gestire. Sei nuovo agli strumenti? Inizia con la [guida all'ambiente di sviluppo](./README.md).

Questa guida configura i modelli di **Azure AI Foundry** per le app AI Java in questo corso. Hai due percorsi:

- **Opzione A — Provision tramite `azd` + Bicep (consigliato):** un solo comando distribuisce l'account Foundry e i modelli come codice. Nessun clic sul portale.
- **Opzione B — Crea le risorse manualmente** nel portale Azure AI Foundry.

Entrambi i percorsi utilizzano l'**autenticazione senza chiave** (Microsoft Entra ID) — non ci sono chiavi API da copiare o divulgare.

## Sommario

- [Cosa viene creato](#cosa-viene-creato)
- [Prerequisiti](#prerequisiti)
- [Opzione A: Provision con azd + Bicep (Consigliato)](#option-a-provision-with-azd--bicep-recommended)
- [Opzione B: Creazione risorse manuale](#opzione-b-creazione-risorse-manuale)
- [Configura il tuo ambiente](#configura-il-tuo-ambiente)
- [Testa la tua configurazione](#testa-la-tua-configurazione)
- [Cosa fare dopo?](#cosa-fare-dopo)
- [Risorse](#risorse)
- [Risorse aggiuntive](#risorse-aggiuntive)

## Cosa viene creato

I template Bicep in [`infra/`](../../../02-SetupDevEnvironment/infra) provvedono a:

- Un account **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, tipo `AIServices`) con un progetto
- Una distribuzione di **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), versione `2026-07-09`, con capacità `GlobalStandard` `10` (10 richieste/minuto e 10.000 token/minuto per questo modello)
- Una distribuzione di **embedding** - `text-embedding-3-small`, versione `1` (usata nei capitoli successivi)
- Un **assegnamento di ruolo senza chiave** (`Cognitive Services OpenAI User`) per accedere con `az login` invece di gestire chiavi

## Prerequisiti

- Un [abbonamento Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) e [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opzione A: Provision con azd + Bicep (Consigliato)

Dalla cartella `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Accedi (entrambi gli strumenti)
azd auth login
az login

# Fornire l'account Foundry + distribuzioni del modello
azd up
```

`azd` richiede un **nome ambiente** (per esempio `genai-java`), **abbonamento** e **regione**. Scegli il tuo abbonamento e una regione dove `gpt-5.6-luna` e `text-embedding-3-small` sono disponibili, per esempio `eastus2`. Conferma che l'abbonamento abbia quota sufficiente per il modello e tipo di distribuzione in quella regione; disponibilità e quota variano in base all'abbonamento.

Al termine del provisioning, azd:

1. Distribuisce tutto ciò che è definito in [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Esegue uno script post-provisioning che scrive [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) con i nomi del tuo endpoint e distribuzioni (nessun segreto).

> **Suggerimento:** Esegui di nuovo `azd up` in qualsiasi momento per applicare modifiche. Esegui `azd down` per eliminare tutto e interrompere i costi.

Per vedere le impostazioni generate:

```bash
azd env get-values
```

Ora vai a [Testa la tua configurazione](#testa-la-tua-configurazione).

## Opzione B: Creazione risorse manuale

Preferisci il portale? Crea le risorse manualmente:

1. Vai al [portale Azure AI Foundry](https://ai.azure.com/) e accedi.
2. **Crea un progetto** (che crea anche una risorsa AI Foundry). Dagli un nome come `GenAIJava`.
3. Nel progetto, vai su **Modelli + endpoint** → **Distribuisci modello** → **Distribuisci modello base**.
4. Distribuisci **GPT-5.6 Luna** (nome modello e distribuzione `gpt-5.6-luna`, versione `2026-07-09`) con capacità **Global Standard** `10`. Ripeti per **text-embedding-3-small**, versione `1`, se vuoi gli esempi embedding.
5. Da **Panoramica**, copia l'**endpoint** (per esempio `https://<resource>.openai.azure.com/`).
6. Concediti accesso senza chiave: sulla risorsa, apri **Controllo accessi (IAM)** → **Aggiungi assegnazione ruolo** → assegna **Cognitive Services OpenAI User** al tuo account.

> **Ancora problemi?** Vedi la [documentazione Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configura il tuo ambiente

**Se hai usato l'Opzione A (`azd up`)**, il file di impostazioni è già scritto — non c'è nulla da configurare. Passa a [Testa la tua configurazione](#testa-la-tua-configurazione).

**Se hai usato l'Opzione B (manuale)**, crea tu il file `.env` dell'esempio:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Modifica `.env` con il tuo endpoint (senza chiave — l'autenticazione è senza chiave):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Usa l'endpoint Azure OpenAI della risorsa, non un URL di progetto. L'app basic-chat lo risolve a `/openai/v1` e configura un client con token bearer esplicito; non serve una chiave API.

> **Nota di sicurezza:** Non c'è nessuna chiave API da conservare. Ti autentichi con Microsoft Entra ID tramite `az login` (localmente) o un'identità gestita (in Azure). Il file `.env` contiene solo impostazioni non segrete ed è già incluso in `.gitignore`.

## Testa la tua configurazione

Assicurati di essere autenticato così che l'autenticazione senza chiave possa ottenere un token, quindi esegui l'esempio:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # se non hai già effettuato l'accesso
mvn clean spring-boot:run
```

Dovresti vedere una risposta dal modello `gpt-5.6-luna`. Esegui gli esempi in sequenza per restare entro la piccola quota predefinita; se ricevi HTTP 429, aspetta l'intervallo di retry prima di riprovare.

> **Utenti VS Code:** premi `F5` per eseguire. L'app carica automaticamente il tuo `.env`.

> **Esempio completo:** Vedi il [Basic Chat con Azure AI Foundry esempio](./examples/basic-chat-azure/README.md) per dettagli e risoluzione problemi.

## Cosa fare dopo?

Dopo il provisioning e aver eseguito con successo l'esempio, avrai:
- Azure AI Foundry con `gpt-5.6-luna` e `text-embedding-3-small` distribuiti
- Autenticazione senza chiave (Microsoft Entra ID) — nessuna chiave da gestire
- Un `.env` locale con il tuo endpoint e nomi delle distribuzioni
- Un ambiente di sviluppo Java pronto all'uso

**Continua con** [Capitolo 3: Tecniche core di Generative AI](../03-CoreGenerativeAITechniques/README.md) per iniziare a costruire applicazioni AI!

## Risorse

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autenticazione senza chiave con Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Documentazione Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Transizione Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK Java ufficiale OpenAI con Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Risorse aggiuntive

- [Scarica VS Code](https://code.visualstudio.com/Download)
- [Ottieni Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Configurazione del container di sviluppo](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire la precisione, si prega di notare che le traduzioni automatizzate possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->