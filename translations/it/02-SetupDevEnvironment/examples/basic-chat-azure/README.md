# Chat di base con Azure AI Foundry - Esempio End-to-End

Questo esempio è una semplice applicazione Spring Boot che si connette a un modello **Azure AI Foundry** usando **autenticazione senza chiave** (Microsoft Entra ID) e testa la configurazione. Utilizza il `ChatClient` di Spring AI, supportato dal **SDK ufficiale OpenAI Java** e dall’endpoint **Azure OpenAI v1**.

Le versioni in [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) sono Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, e dotenv-java **3.2.0**. Il campione usa `spring-ai-starter-model-openai` e dichiara esplicitamente `openai-java` e `azure-identity`; Spring AI 2 ha rimosso il vecchio starter Azure OpenAI.

## Indice

- [Prerequisiti](#prerequisiti)
- [Avvio rapido](#avvio-rapido)
- [Come funziona l'autenticazione](#come-funziona-lautenticazione)
- [Esecuzione dell'applicazione](#esecuzione-dellapplicazione)
  - [Usare Maven](#usare-maven)
  - [Usare VS Code](#usare-vs-code)
  - [Output previsto](#output-previsto)
- [Riferimento configurazione](#riferimento-configurazione)
  - [Variabili d'ambiente](#variabili-dambiente)
  - [Configurazione Spring](#configurazione-spring)
- [Risoluzione problemi](#risoluzione-problemi)
  - [Problemi comuni](#problemi-comuni)
  - [Modalità debug](#modalità-debug)
- [Passi successivi](#passi-successivi)
- [Risorse](#risorse)

## Prerequisiti

Prima di eseguire questo esempio, assicurati di avere:

- Una risorsa Azure AI Foundry con un deployment `gpt-5.6-luna` - provala con `azd up` o manualmente tramite la [guida di configurazione Azure AI Foundry](../../getting-started-azure-openai.md)
- Il ruolo **Cognitive Services OpenAI User** su quella risorsa (i template Bicep te lo assegnano)
- Il [CLI Azure (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), con accesso tramite `az login`
- Java 21+ e Maven 3.9+

> **Nessuna chiave API richiesta** — l’autenticazione è senza chiave attraverso Microsoft Entra ID.

## Avvio rapido

```bash
# 1. Naviga al progetto
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Accedi in modo che l'autenticazione senza chiave possa ottenere un token
az login

# 3. Configura l'endpoint
#    - Se hai eseguito `azd up`, .env è stato scritto per te (salta questo passaggio).
#    - Altrimenti copia il modello e imposta AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Esegui l'applicazione
mvn spring-boot:run
```

## Come funziona l'autenticazione

Questo esempio autentica con **Microsoft Entra ID** — non si usa nessuna chiave API.

L’applicazione configura l’autenticazione esplicitamente in [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` crea un `BearerTokenCredential` usando `AuthenticationUtil.getBearerTokenSupplier` con `DefaultAzureCredential` e l’ambito `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` costruisce un `OpenAIClient` con `OpenAIOkHttpClient.builder()`, risolve l’endpoint risorsa in `/openai/v1` e fornisce la credenziale bearer con `.credential(...)`.
3. `azureChatModel()` fornisce quel client all’`OpenAiChatModel` di Spring AI, che supporta il `ChatClient` della lezione.

Questi bean espliciti evitano che una variabile globale `OPENAI_API_KEY` sovrascriva l’autenticazione Azure. Omettere una chiave API da YAML da solo non è la configurazione corretta. `DefaultAzureCredential` può usare la sessione `az login` locale o un’identità gestita in Azure; l’identità scelta deve avere il ruolo risorsa indicato sopra.

## Esecuzione dell'applicazione

### Usare Maven

```bash
mvn spring-boot:run
```

### Usare VS Code

1. Apri il progetto in VS Code
2. Premi `F5` o usa il pannello "Esegui e Debug"
3. Seleziona la configurazione "Spring Boot-BasicChatApplication"

> **Nota**: L'applicazione carica `.env` dalla directory di lavoro, anche se avviata da VS Code.

### Output previsto

Output illustrativo dopo un’esecuzione riuscita (log di avvio omessi; la formulazione della risposta può variare):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Riferimento configurazione

### Variabili d'ambiente

| Variabile | Descrizione | Richiesta | Esempio |
|----------|-------------|-----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL endpoint Foundry (Azure OpenAI) | Sì | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nome del deployment modello chat | No | `gpt-5.6-luna` (predefinito) |

> Non esiste nessuna variabile per chiave API — l’autenticazione è senza chiave (Microsoft Entra ID tramite `az login`).

### Configurazione Spring

Le impostazioni in [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) usano il prefisso `spring.ai.openai` e proprietà chat appiattite (nessun blocco `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` è il **nome del deployment Azure**. L’autenticazione proviene dai bean espliciti descritti sopra, non da una impostazione `api-key`. La lezione disabilita il ragionamento e limita i token di completamento a 500; lascia `temperature` e il vecchio `max-tokens` non impostati.

Microsoft raccomanda l’[SDK ufficiale OpenAI con Azure OpenAI v1 e le API Responses per nuove applicazioni](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions rimane supportato per questa lezione esistente basata su messaggi. Per GPT-5.6, le richieste con strumenti su Chat Completions devono impostare `reasoning_effort` a `none`; usa Responses quando combini ragionamento con strumenti. Vedi [uso degli strumenti con modelli di ragionamento](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Risoluzione problemi

### Problemi comuni

<details>
<summary><strong>Errore: 401 / "PermissionDenied" / errori token</strong></summary>

- Esegui `az login` — l’autenticazione senza chiave necessita una sessione attiva per ottenere il token
- Verifica che il tuo account abbia il ruolo **Cognitive Services OpenAI User** sulla risorsa
- Se hai appena assegnato il ruolo, attendi un minuto per la propagazione
- Conferma di essere nel tenant/sottoscrizione corretta (`az account show`)
</details>

<details>
<summary><strong>Errore: "L’endpoint non è valido" / errori di connessione</strong></summary>

- Assicurati che `AZURE_OPENAI_ENDPOINT` sia l’URL base completo (es., `https://your-resource.openai.azure.com/`)
- Controlla la coerenza della barra finale
- Verifica che l’endpoint corrisponda alla risorsa fornita (`azd env get-values`)
</details>

<details>
<summary><strong>Errore: "Il deployment non è stato trovato"</strong></summary>

- Verifica che `AZURE_OPENAI_DEPLOYMENT` corrisponda a un nome di deployment in Azure
- Controlla che il modello sia distribuito con successo e attivo
- Il nome di deployment predefinito è `gpt-5.6-luna`
</details>

<details>
<summary><strong>Errore: 429 / limite di richieste superato</strong></summary>

- Il deployment predefinito GPT-5.6 Luna ha capacità Global Standard 10: 10 richieste/minuto e 10.000 token/minuto
- Esegui gli esempi in sequenza e attendi l’intervallo di retry del servizio prima di riprovare
- Questo esempio base disabilita i retry automatici dell’SDK, quindi la richiesta fallita viene segnalata direttamente
</details>

<details>
<summary><strong>VS Code: le variabili d’ambiente non si caricano</strong></summary>

- Assicurati che il file `.env` sia nella directory radice del progetto (lo stesso livello di `pom.xml`)
- Prova a eseguire `mvn spring-boot:run` nel terminale integrato di VS Code
- Controlla che l’estensione Java di VS Code sia installata correttamente
</details>

### Modalità debug

Per abilitare il logging dettagliato, decommenta queste righe in [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Passi successivi

**Configurazione completata!** Continua il tuo percorso di apprendimento:

[Capitolo 3: Tecniche di IA generativa di base](../../../03-CoreGenerativeAITechniques/README.md)

## Risorse

- [Transizione Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK ufficiale OpenAI Java con Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Autenticazione senza chiave con Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portale Azure AI Foundry](https://ai.azure.com/)
- [Documentazione Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Questo documento è stato tradotto utilizzando il servizio di traduzione AI [Co-op Translator](https://github.com/Azure/co-op-translator). Sebbene ci impegniamo per garantire la precisione, si prega di notare che le traduzioni automatizzate possono contenere errori o imprecisioni. Il documento originale nella sua lingua nativa deve essere considerato la fonte autorevole. Per informazioni critiche, si raccomanda una traduzione professionale effettuata da un essere umano. Non siamo responsabili per eventuali malintesi o interpretazioni errate derivanti dall’uso di questa traduzione.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->