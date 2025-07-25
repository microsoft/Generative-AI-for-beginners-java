<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:24:54+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "it"
}
-->
# Tutorial di Foundry Local con Spring Boot

## Indice

- [Prerequisiti](../../../../04-PracticalSamples/foundrylocal)
- [Panoramica del Progetto](../../../../04-PracticalSamples/foundrylocal)
- [Comprendere il Codice](../../../../04-PracticalSamples/foundrylocal)
  - [1. Configurazione dell'Applicazione (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Classe Principale dell'Applicazione (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Livello di Servizio AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Dipendenze del Progetto (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Come Funziona Tutto Insieme](../../../../04-PracticalSamples/foundrylocal)
- [Configurare Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Eseguire l'Applicazione](../../../../04-PracticalSamples/foundrylocal)
- [Output Atteso](../../../../04-PracticalSamples/foundrylocal)
- [Prossimi Passi](../../../../04-PracticalSamples/foundrylocal)
- [Risoluzione dei Problemi](../../../../04-PracticalSamples/foundrylocal)

## Prerequisiti

Prima di iniziare questo tutorial, assicurati di avere:

- **Java 21 o superiore** installato sul tuo sistema
- **Maven 3.6+** per costruire il progetto
- **Foundry Local** installato e in esecuzione

### **Installare Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Panoramica del Progetto

Questo progetto è composto da quattro componenti principali:

1. **Application.java** - Il punto di ingresso principale dell'applicazione Spring Boot
2. **FoundryLocalService.java** - Livello di servizio che gestisce la comunicazione con l'AI
3. **application.properties** - Configurazione per la connessione a Foundry Local
4. **pom.xml** - Dipendenze Maven e configurazione del progetto

## Comprendere il Codice

### 1. Configurazione dell'Applicazione (application.properties)

**File:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Cosa fa:**
- **base-url**: Specifica dove Foundry Local è in esecuzione (porta predefinita 5273)
- **model**: Indica il modello AI da utilizzare per la generazione di testo

**Concetto chiave:** Spring Boot carica automaticamente queste proprietà e le rende disponibili all'applicazione utilizzando l'annotazione `@Value`.

### 2. Classe Principale dell'Applicazione (Application.java)

**File:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Cosa fa:**
- `@SpringBootApplication` abilita la configurazione automatica di Spring Boot
- `WebApplicationType.NONE` indica a Spring che si tratta di un'applicazione da riga di comando, non di un server web
- Il metodo principale avvia l'applicazione Spring

**Il Runner Demo:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**Cosa fa:**
- `@Bean` crea un componente gestito da Spring
- `CommandLineRunner` esegue il codice dopo l'avvio di Spring Boot
- `foundryLocalService` viene iniettato automaticamente da Spring (iniezione di dipendenze)
- Invia un messaggio di prova all'AI e visualizza la risposta

### 3. Livello di Servizio AI (FoundryLocalService.java)

**File:** `src/main/java/com/example/FoundryLocalService.java`

#### Iniezione della Configurazione:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Cosa fa:**
- `@Service` indica a Spring che questa classe fornisce logica di business
- `@Value` inietta i valori di configurazione da application.properties
- La sintassi `:default-value` fornisce valori di fallback se le proprietà non sono impostate

#### Inizializzazione del Client:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Cosa fa:**
- `@PostConstruct` esegue questo metodo dopo che Spring ha creato il servizio
- Crea un client OpenAI che punta alla tua istanza locale di Foundry Local
- Il percorso `/v1` è richiesto per la compatibilità con l'API OpenAI
- La chiave API è "unused" perché lo sviluppo locale non richiede autenticazione

#### Metodo Chat:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**Cosa fa:**
- **ChatCompletionCreateParams**: Configura la richiesta AI
  - `model`: Specifica quale modello AI utilizzare
  - `addUserMessage`: Aggiunge il tuo messaggio alla conversazione
  - `maxCompletionTokens`: Limita la lunghezza della risposta (risparmia risorse)
  - `temperature`: Controlla la casualità (0.0 = deterministico, 1.0 = creativo)
- **Chiamata API**: Invia la richiesta a Foundry Local
- **Gestione della Risposta**: Estrae in modo sicuro la risposta testuale dell'AI
- **Gestione degli Errori**: Avvolge le eccezioni con messaggi di errore utili

### 4. Dipendenze del Progetto (pom.xml)

**Dipendenze Chiave:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Cosa fanno:**
- **spring-boot-starter**: Fornisce funzionalità principali di Spring Boot
- **openai-java**: SDK Java ufficiale di OpenAI per la comunicazione con l'API
- **jackson-databind**: Gestisce la serializzazione/deserializzazione JSON per le chiamate API

## Come Funziona Tutto Insieme

Ecco il flusso completo quando esegui l'applicazione:

1. **Avvio**: Spring Boot si avvia e legge `application.properties`
2. **Creazione del Servizio**: Spring crea `FoundryLocalService` e inietta i valori di configurazione
3. **Configurazione del Client**: `@PostConstruct` inizializza il client OpenAI per connettersi a Foundry Local
4. **Esecuzione Demo**: `CommandLineRunner` viene eseguito dopo l'avvio
5. **Chiamata AI**: La demo chiama `foundryLocalService.chat()` con un messaggio di prova
6. **Richiesta API**: Il servizio costruisce e invia una richiesta compatibile con OpenAI a Foundry Local
7. **Elaborazione della Risposta**: Il servizio estrae e restituisce la risposta dell'AI
8. **Visualizzazione**: L'applicazione stampa la risposta e si chiude

## Configurare Foundry Local

Per configurare Foundry Local, segui questi passaggi:

1. **Installa Foundry Local** utilizzando le istruzioni nella sezione [Prerequisiti](../../../../04-PracticalSamples/foundrylocal).
2. **Scarica il modello AI** che desideri utilizzare, ad esempio `phi-3.5-mini`, con il seguente comando:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configura il file application.properties** per adattarlo alle impostazioni di Foundry Local, soprattutto se stai utilizzando una porta o un modello diverso.

## Eseguire l'Applicazione

### Passo 1: Avvia Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Passo 2: Compila ed Esegui l'Applicazione
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Output Atteso

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Prossimi Passi

Per ulteriori esempi, consulta [Capitolo 04: Esempi pratici](../README.md)

## Risoluzione dei Problemi

### Problemi Comuni

**"Connessione rifiutata" o "Servizio non disponibile"**
- Assicurati che Foundry Local sia in esecuzione: `foundry model list`
- Verifica che il servizio sia sulla porta 5273: Controlla `application.properties`
- Prova a riavviare Foundry Local: `foundry model run phi-3.5-mini`

**Errori "Modello non trovato"**
- Controlla i modelli disponibili: `foundry model list`
- Aggiorna il nome del modello in `application.properties` per corrispondere esattamente
- Scarica il modello se necessario: `foundry model run phi-3.5-mini`

**Errori di compilazione Maven**
- Assicurati di avere Java 21 o superiore: `java -version`
- Pulisci e ricompila: `mvn clean compile`
- Controlla la connessione internet per il download delle dipendenze

**L'applicazione si avvia ma non produce output**
- Verifica che Foundry Local stia rispondendo: Apri il browser su `http://localhost:5273`
- Controlla i log dell'applicazione per messaggi di errore specifici
- Assicurati che il modello sia completamente caricato e pronto

**Disclaimer**:  
Questo documento è stato tradotto utilizzando il servizio di traduzione automatica [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire l'accuratezza, si prega di notare che le traduzioni automatiche possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa dovrebbe essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un traduttore umano. Non siamo responsabili per eventuali fraintendimenti o interpretazioni errate derivanti dall'uso di questa traduzione.