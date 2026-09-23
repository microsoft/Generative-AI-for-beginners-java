# Tutorial per Principianti Generatore di Storie di Animali Domestici

Carica una foto di un animale domestico, analizzala con GPT-5.6 Luna e genera una storia dalla descrizione risultante. Entrambe le richieste al modello usano `reasoning_effort: none`.

| Componente | Versione |
| --- | --- |
| Java | 21 o superiore |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Indice

- [Prerequisiti](#prerequisiti)
- [Comprendere la Struttura del Progetto](#comprendere-la-struttura-del-progetto)
- [Spiegazione dei Componenti Principali](#spiegazione-dei-componenti-principali)
  - [1. Applicazione Principale](#1-applicazione-principale)
  - [2. Controller Web](#2-controller-web)
  - [3. Servizio Storie](#3-servizio-storie)
  - [4. Template Web](#4-template-web)
  - [5. Configurazione](#5-configurazione)
- [Esecuzione dell'Applicazione](#esecuzione-dellapplicazione)
- [Test Offline](#test-offline)
- [Come Funziona Tutto Insieme](#come-funziona-tutto-insieme)
- [Comprendere l'Integrazione AI](#comprendere-lintegrazione-ai)
- [Passi Successivi](#passi-successivi)

## Prerequisiti

Prima di iniziare, assicurati di avere:
- Java 21 o superiore installato
- Maven per la gestione delle dipendenze
- Un deployment Azure AI Foundry di GPT-5.6 Luna chiamato `gpt-5.6-luna`, o un override `AZURE_OPENAI_DEPLOYMENT` che punta a quel deployment. Vedi [Capitolo 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) per il provisioning e fai login con `az login` per l'autenticazione senza chiave. Il deployment deve supportare l'input delle immagini e `reasoning_effort: none`.
- Conoscenze di base di Java, Spring Boot e sviluppo web

## Comprendere la Struttura del Progetto

Il progetto storia di animali domestici contiene diversi file importanti:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Spiegazione dei Componenti Principali

### 1. Applicazione Principale

**File:** `PetStoryApplication.java`

Questo è il punto di ingresso della nostra applicazione Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Cosa fa:**
- L'annotazione `@SpringBootApplication` abilita la configurazione automatica e la scansione dei componenti
- Avvia un server web embedded (Tomcat) sulla porta 8080
- Crea automaticamente tutti i bean e i servizi Spring necessari

### 2. Controller Web

**File:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Richiesta | Risposta di successo |
| --- | --- | --- |
| `GET /` | Nessun corpo | Modulo HTML di caricamento con token CSRF |
| `POST /analyze-image` | `multipart/form-data`, campo file `image` | JSON: `{"description":"Un animale domestico giocherellone..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, campo `description` | Pagina HTML con il risultato contenente la descrizione e la storia generata |

Entrambi gli endpoint POST richiedono il cookie di sessione e il token CSRF ottenuti da `GET /`. Lo script di caricamento invia il valore nascosto `_csrf` nell'intestazione `X-CSRF-TOKEN`; l'invio della storia lo invia come campo del form `_csrf`. I client API devono preservare il cookie tra le richieste. Questi sono endpoint per form, non endpoint per richieste JSON.

Le descrizioni devono essere non vuote e non più lunghe di 1000 caratteri. Il controller tronca la descrizione e rimuove `<`, `>`, virgolette doppie, apostrofi e `&` prima di passarla al servizio. Il template del risultato esegue anche l'escape dell'output del modello con `th:text`.

I fallimenti di validazione delle immagini ritornano HTTP 400 con un campo `error`; i fallimenti del modello ritornano HTTP 502 con un campo `error` e nessuna `description`. Descrizioni di storie invalide o fallimenti del modello reindirizzano a `/` con un errore visibile. I campi richiesti mancanti ritornano HTTP 400, e i token CSRF mancanti o invalidi ritornano HTTP 403. Non sono presentate descrizioni o storie di fallback come risultati AI di successo.

### 3. Servizio Storie

**File:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

L'SDK ufficiale OpenAI Java 4.63.1 chiama l'API Chat Completions compatibile OpenAI di Azure AI Foundry. Azure Identity 1.18.6 fornisce un bearer token Microsoft Entra tramite `DefaultAzureCredential`; non è richiesta alcuna chiave API.

| Operazione | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Byte dell'immagine codificati come URL dati base64 con il tipo MIME caricato | 300 |
| `generateStory` | Una descrizione del pet in un messaggio utente | 800 |

Entrambe le richieste usano il deployment configurato, predefinito a `gpt-5.6-luna`, e impostano esplicitamente `ReasoningEffort.NONE` (`reasoning_effort: none`). Nessuna richiesta invia `temperature` o il vecchio parametro `max_tokens`.

L'analisi delle immagini accetta JPEG, PNG, GIF e WebP, rifiuta immagini vuote e file oltre 10MB, e limita la descrizione risultante a 1000 caratteri. Il prompt per la storia richiede una breve storia adatta a famiglie. Scelte vuote o contenuto del modello vuoto sono errori, e i fallimenti mantengono la causa originale per la diagnostica lato server. Il client SDK viene chiuso allo spegnimento dell'applicazione.

### 4. Template Web

**File:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Modulo di Caricamento)

La pagina inizia con un selettore di foto, non un'area di testo per la descrizione. **Analizza Immagine** mostra l'anteprima della foto selezionata e la invia a `/analyze-image`. Una risposta di successo mostra la descrizione, compila il campo nascosto `description` e rivela **Genera Storia**. Quel pulsante invia il modulo esistente a `/generate-story`.

Non c'è alcun modello browser da scaricare o dipendenza CDN. L'analisi delle immagini avviene sul server tramite il deployment Azure configurato. I fallimenti restano visibili e non abilitano la generazione di storie con una descrizione fabbricata. La selezione di un file diverso cancella l'analisi precedente.

**File:** `result.html` (Visualizzazione Storia)

Mostra la storia generata:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Caratteristiche del template:**

1. **Integrazione Thymeleaf**: Usa attributi `th:` per contenuti dinamici
2. **Design Responsive**: Styling CSS per mobile e desktop
3. **Gestione Errori**: Visualizza errori di validazione agli utenti
4. **Gestione Caricamento**: JavaScript mostra l’anteprima della foto, invia una richiesta multipart protetta CSRF e visualizza la descrizione restituita

### 5. Configurazione

**File:** `application.properties`

Impostazioni di configurazione per l'applicazione:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Spiegazione della configurazione:**

1. **Caricamento File**: Sia il file che la richiesta multipart completa sono limitati a 10MB; mantieni le foto sotto questo limite per lasciare spazio alle intestazioni multipart
2. **Logging**: Controlla quali informazioni vengono registrate durante l'esecuzione
3. **Azure AI Foundry**: Specifica l'endpoint e il deployment modello da usare (autenticazione senza chiave)
4. **Sicurezza**: La protezione CSRF rimane abilitata; le diagnostiche del modello vengono registrate sul server, mentre il controller mostra messaggi generici di errore modello

## Esecuzione dell'Applicazione

### Passo 1: Accedi e Imposta il Tuo Endpoint

L'autenticazione è senza chiave (Microsoft Entra ID), quindi non c'è una chiave API. Accedi e imposta il tuo endpoint Foundry:

**Windows (Prompt dei comandi):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Perché è necessario:**
- Azure AI Foundry usa Microsoft Entra ID per autenticare le richieste di inferenza
- L'autenticazione senza chiave significa nessun segreto nel codice sorgente o nell'ambiente
- Il tuo account deve avere il ruolo **Cognitive Services OpenAI User** sulla risorsa

Il nome del deployment predefinito è `gpt-5.6-luna`. Se il tuo deployment GPT-5.6 Luna ha un altro nome, imposta `AZURE_OPENAI_DEPLOYMENT` nello stesso terminale prima di avviare l'applicazione. Sia l'analisi delle immagini che la generazione di storie usano questa impostazione.

### Passo 2: Compila e Avvia

Naviga nella directory del progetto:
```bash
cd 04-PracticalSamples/petstory
```

Compila il JAR eseguibile standalone e avvia tutti i test offline:
```bash
mvn clean package
```

Avvia il server:
```bash
mvn spring-boot:run
```

L'applicazione partirà su `http://localhost:8080`.

In alternativa, avvia il JAR impacchettato su una porta libera, per esempio:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Per quel comando, apri `http://localhost:8083/`. Le stesse rotte `/analyze-image` e `/generate-story` sono disponibili sulla porta selezionata.

### Passo 3: Testa l'Applicazione

1. **Apri** `http://localhost:8080` nel browser
2. **Seleziona** una foto chiara di un animale domestico in formato JPEG, PNG, GIF o WebP, sotto i 10MB
3. **Clicca** su "Analizza Immagine" e aspetta la descrizione dell'animale domestico
4. **Clicca** su "Genera Storia" dopo l'analisi riuscita
5. **Visualizza** la storia e usa il link nella pagina dei risultati per tornare al modulo di caricamento

Il flusso riuscito da foto a storia effettua due chiamate al modello, una per pulsante. L'inferenza live consuma la tua quota di deployment e può comportare costi; esegui test a caldo in serie se condividi un deployment con limiti di velocità. Il caricamento della home page non chiama il modello.

## Test Offline

Dalla directory sample, esegui:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) cattura vere richieste OpenAI SDK con un fixture HTTP in loopback. Controlla deployment su entrambe le richieste, `reasoning_effort: none`, limiti di token, payload dell'immagine, convalida input, risposte vuote e errori a monte.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) usa MockMvc con un servizio modello simulato per testare le pagine Thymeleaf renderizzate, il contratto di caricamento, CSRF, validazione, escape output e fallimenti visibili. Questi test non necessitano credenziali Azure e non chiamano mai inferenza Azure a pagamento. Maven scrive rapporti Surefire sotto `target/surefire-reports`.

## Come Funziona Tutto Insieme

Ecco il flusso completo quando generi una storia di animali domestici:

1. **Selezione Foto**: Scegli un'immagine di un animale domestico nel modulo di caricamento
2. **Caricamento Immagine**: "Analizza Immagine" invia un POST multipart a `/analyze-image` con l'intestazione CSRF
3. **Analisi Immagine**: `StoryService` invia l'immagine a GPT-5.6 Luna con il ragionamento impostato su `none`
4. **Visualizzazione Descrizione**: Il browser mostra la descrizione restituita e la memorizza nel modulo
5. **Invio Storia**: "Genera Storia" invia `description` e `_csrf` a `/generate-story`
6. **Generazione Storia**: Il controller valida la descrizione e chiama lo stesso deployment con ragionamento impostato su `none`
7. **Rendering Template**: Thymeleaf effettua l'escape e visualizza descrizione e storia nella pagina dei risultati

**Flusso di Gestione Errori:**
Se il modello fallisce, il server registra la causa. L'analisi delle immagini ritorna HTTP 502 e il browser mostra l'errore senza rivelare "Genera Storia". La generazione di storie reindirizza al modulo con un messaggio di errore. Nessun percorso sostituisce silenziosamente un risultato pre-scritto.

## Comprendere l'Integrazione AI

### Azure AI Foundry (senza chiave)
Il servizio configura l'SDK con l'endpoint `/openai/v1/` della tua risorsa. `DefaultAzureCredential` e `AuthenticationUtil.getBearerTokenSupplier` forniscono token Microsoft Entra per `https://ai.azure.com/.default`. Lo sviluppo locale può usare il login Azure CLI; un'app ospitata in Azure può usare un'identità gestita con i permessi necessari sulla risorsa.

### Ingegneria del Prompt
L'analisi delle immagini richiede caratteristiche osservabili dell'animale domestico in un breve paragrafo e dice al modello di trattare il testo nell'immagine come dati, non istruzioni. La generazione della storia usa la descrizione restituita in una richiesta di scrittura separata, adatta a famiglie. Nessuna chiamata abilita il ragionamento o imposta una sovrascrittura della temperatura.

### Elaborazione della Risposta
Il gestore di risposta condiviso rifiuta scelte mancanti e contenuti vuoti o solo spazi, tronca i contenuti validi e preserva i fallimenti a monte. Le descrizioni delle immagini sono limitate a 1000 caratteri per adattarsi al modulo della storia successivo. Il fallimento originale del modello è mantenuto per la diagnostica ma non mostrato all'utente.

## Passi Successivi

Per più esempi, vedi [Capitolo 04: Esempi pratici](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire la precisione, si prega di notare che le traduzioni automatizzate possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->