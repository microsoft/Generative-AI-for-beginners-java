# Βασική Συνομιλία με το Azure AI Foundry - Παράδειγμα από Αρχή ως Τέλος

Αυτό το παράδειγμα είναι μια απλή εφαρμογή Spring Boot που συνδέεται με ένα μοντέλο **Azure AI Foundry** χρησιμοποιώντας **αυθεντικοποίηση χωρίς κλειδί** (Microsoft Entra ID) και δοκιμάζει τη ρύθμισή σας. Χρησιμοποιεί το `ChatClient` του Spring AI, υποστηριζόμενο από το **επίσημο OpenAI Java SDK** και το τελικό σημείο **Azure OpenAI v1**.

Οι εκδόσεις στο [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) είναι Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, και dotenv-java **3.2.0**. Το δείγμα χρησιμοποιεί το `spring-ai-starter-model-openai` και δηλώνει ρητά τα `openai-java` και `azure-identity`; Το Spring AI 2 αφαίρεσε τον παλιό Azure OpenAI starter.

## Περιεχόμενα

- [Προαπαιτούμενα](#προαπαιτούμενα)
- [Γρήγορη Εκκίνηση](#γρήγορη-εκκίνηση)
- [Πώς Λειτουργεί η Αυθεντικοποίηση](#πώς-λειτουργεί-η-αυθεντικοποίηση)
- [Εκτέλεση της Εφαρμογής](#εκτέλεση-της-εφαρμογής)
  - [Με Maven](#με-maven)
  - [Με VS Code](#με-vs-code)
  - [Αναμενόμενη Έξοδος](#αναμενόμενη-έξοδος)
- [Αναφορά Ρυθμίσεων](#αναφορά-ρυθμίσεων)
  - [Μεταβλητές Περιβάλλοντος](#μεταβλητές-περιβάλλοντος)
  - [Ρύθμιση Spring](#ρύθμιση-spring)
- [Επίλυση Προβλημάτων](#επίλυση-προβλημάτων)
  - [Συνηθισμένα Προβλήματα](#συνηθισμένα-προβλήματα)
  - [Λειτουργία Εντοπισμού Σφαλμάτων](#λειτουργία-εντοπισμού-σφαλμάτων)
- [Επόμενα Βήματα](#επόμενα-βήματα)
- [Πόροι](#πόροι)

## Προαπαιτούμενα

Πριν εκτελέσετε αυτό το παράδειγμα, βεβαιωθείτε ότι έχετε:

- Πόρο Azure AI Foundry με ανάπτυξη `gpt-5.6-luna` - δημιουργήστε τον με `azd up` ή χειροκίνητα μέσω του [οδηγού ρύθμισης Azure AI Foundry](../../getting-started-azure-openai.md)
- Το ρόλο **Cognitive Services OpenAI User** σε αυτόν τον πόρο (τα πρότυπα Bicep το αναθέτουν για εσάς)
- Το [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), συνδεδεμένο με `az login`
- Java 21+ και Maven 3.9+

> **Δεν απαιτείται κλειδί API** — η αυθεντικοποίηση γίνεται χωρίς κλειδί μέσω Microsoft Entra ID.

## Γρήγορη Εκκίνηση

```bash
# 1. Πλοηγηθείτε στο έργο
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Συνδεθείτε ώστε η είσοδος χωρίς κλειδί να μπορεί να πάρει ένα διακριτικό
az login

# 3. Ρυθμίστε το τελικό σημείο
#    - Αν εκτελέσατε `azd up`, το αρχείο .env δημιουργήθηκε για εσάς (παραλείψτε αυτό).
#    - Διαφορετικά, αντιγράψτε το πρότυπο και ορίστε το AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Εκτελέστε την εφαρμογή
mvn spring-boot:run
```

## Πώς Λειτουργεί η Αυθεντικοποίηση

Αυτό το παράδειγμα αυθεντικοποιείται με **Microsoft Entra ID** — δεν υπάρχει κλειδί API.

Η εφαρμογή ρυθμίζει ρητά την αυθεντικοποίηση στο [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. Η `azureCredential()` δημιουργεί ένα `BearerTokenCredential` χρησιμοποιώντας το `AuthenticationUtil.getBearerTokenSupplier` με `DefaultAzureCredential` και το scope `https://ai.azure.com/.default`.
2. Η `azureOpenAiClient()` δημιουργεί ένα `OpenAIClient` με `OpenAIOkHttpClient.builder()`, καθορίζει το endpoint του πόρου σε `/openai/v1`, και παρέχει το διακριτικό με `.credential(...)`.
3. Η `azureChatModel()` παρέχει αυτόν τον client στο `OpenAiChatModel` του Spring AI, που υποστηρίζει το `ChatClient` του μαθήματος.

Αυτά τα ρητά bean αποτρέπουν μια παγκόσμια `OPENAI_API_KEY` από την αντικατάσταση της αυθεντικοποίησης Azure. Η παράλειψη ενός κλειδιού API από το YAML μόνο δεν αποτελεί ρύθμιση αυθεντικοποίησης. Το `DefaultAzureCredential` μπορεί να χρησιμοποιήσει την τοπική συνεδρία `az login` ή μια διαχειριζόμενη ταυτότητα στο Azure· όποια ταυτότητα επιλεχθεί πρέπει να έχει τον αναφερόμενο ρόλο πόρου.

## Εκτέλεση της Εφαρμογής

### Με Maven

```bash
mvn spring-boot:run
```

### Με VS Code

1. Ανοίξτε το έργο στο VS Code
2. Πατήστε `F5` ή χρησιμοποιήστε τον πίνακα "Εκτέλεση και Εντοπισμός Σφαλμάτων"
3. Επιλέξτε τη ρύθμιση "Spring Boot-BasicChatApplication"

> **Σημείωση**: Η εφαρμογή φορτώνει το `.env` από τον κατάλογο εργασίας, συμπεριλαμβανομένης της εκκίνησης από το VS Code.

### Αναμενόμενη Έξοδος

Παραδειγματική έξοδος μετά από επιτυχή εκτέλεση (οι καταγραφές εκκίνησης παραλείπονται· η διατύπωση της απάντησης μπορεί να διαφέρει):

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

## Αναφορά Ρυθμίσεων

### Μεταβλητές Περιβάλλοντος

| Μεταβλητή | Περιγραφή | Απαιτείται | Παράδειγμα |
|----------|-----------|------------|-----------|
| `AZURE_OPENAI_ENDPOINT` | Η διεύθυνση τελικού σημείου Foundry (Azure OpenAI) | Ναι | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Όνομα ανάπτυξης μοντέλου συνομιλίας | Όχι | `gpt-5.6-luna` (προεπιλογή) |

> Δεν υπάρχει μεταβλητή κλειδιού API — η αυθεντικοποίηση είναι χωρίς κλειδί (Microsoft Entra ID μέσω `az login`).

### Ρύθμιση Spring

Οι ρυθμίσεις στο [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) χρησιμοποιούν το πρόθεμα `spring.ai.openai` και απλοποιημένες ιδιότητες συνομιλίας (χωρίς μπλοκ `options`):

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

Το `model` είναι το **όνομα ανάπτυξης Azure**. Η αυθεντικοποίηση προέρχεται από τα ρητά bean που περιγράφονται παραπάνω, όχι από ρύθμιση `api-key`. Το μάθημα απενεργοποιεί τη λογική και περιορίζει τους χαρακτήρες ολοκλήρωσης σε 500· αφήνει `temperature` και το παλαιό `max-tokens` μη ορισμένα.

Η Microsoft συνιστά το [επίσημο OpenAI SDK με Azure OpenAI v1 και το API Responses για νέες εφαρμογές](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Το Chat Completions παραμένει υποστηριζόμενο για αυτό το υπάρχον μάθημα βασισμένο σε μηνύματα. Για το GPT-5.6, αιτήματα που περιλαμβάνουν εργαλεία στο Chat Completions πρέπει να θέτουν το `reasoning_effort` σε `none`; χρησιμοποιήστε το Responses όταν συνδυάζετε λογική με εργαλεία. Δείτε το [κλήση εργαλείων με μοντέλα λογικής](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Επίλυση Προβλημάτων

### Συνηθισμένα Προβλήματα

<details>
<summary><strong>Σφάλμα: 401 / "PermissionDenied" / σφάλματα διακριτικού</strong></summary>

- Εκτελέστε `az login` — η αυθεντικοποίηση χωρίς κλειδί απαιτεί ενεργή σύνδεση για λήψη διακριτικού
- Επαληθεύστε ότι ο λογαριασμός σας έχει το ρόλο **Cognitive Services OpenAI User** στον πόρο
- Αν μόλις αναθέσατε το ρόλο, περιμένετε ένα λεπτό για να ολοκληρωθεί η εξάπλωση
- Επιβεβαιώστε ότι είστε στο σωστό tenant/συνδρομή (`az account show`)
</details>

<details>
<summary><strong>Σφάλμα: "Το τελικό σημείο δεν είναι έγκυρο" / σφάλματα σύνδεσης</strong></summary>

- Βεβαιωθείτε ότι η `AZURE_OPENAI_ENDPOINT` είναι η πλήρης βασική URL (π.χ. `https://your-resource.openai.azure.com/`)
- Ελέγξτε για συνέπεια στο τελικό / κατάληξη
- Επαληθεύστε ότι το τελικό σημείο ταιριάζει με τον παρεχόμενο πόρο σας (`azd env get-values`)
</details>

<details>
<summary><strong>Σφάλμα: "Η ανάπτυξη δεν βρέθηκε"</strong></summary>

- Επαληθεύστε ότι το `AZURE_OPENAI_DEPLOYMENT` ταιριάζει με όνομα ανάπτυξης στο Azure
- Ελέγξτε ότι το μοντέλο έχει αναπτυχθεί με επιτυχία και είναι ενεργό
- Το προεπιλεγμένο όνομα ανάπτυξης είναι `gpt-5.6-luna`
</details>

<details>
<summary><strong>Σφάλμα: 429 / υπέρβαση ορίου ρυθμού</strong></summary>

- Η προεπιλεγμένη ανάπτυξη GPT-5.6 Luna διαθέτει Global Standard capacity 10: 10 αιτήματα/λεπτό και 10,000 χαρακτήρες/λεπτό
- Εκτελέστε παραδείγματα διαδοχικά και περιμένετε το διάστημα επανεκτέλεσης της υπηρεσίας προτού ξαναδοκιμάσετε
- Αυτό το βασικό παράδειγμα απενεργοποιεί αυτόματες επανεκτελέσεις SDK, οπότε ένα αποτυχημένο αίτημα αναφέρεται απευθείας
</details>

<details>
<summary><strong>VS Code: Οι μεταβλητές περιβάλλοντος δεν φορτώνονται</strong></summary>

- Βεβαιωθείτε ότι το αρχείο `.env` είναι στον ριζικό κατάλογο του έργου (στο ίδιο επίπεδο με το `pom.xml`)
- Δοκιμάστε να εκτελέσετε `mvn spring-boot:run` στο εσωτερικό τερματικό του VS Code
- Ελέγξτε ότι η επέκταση Java για VS Code είναι σωστά εγκατεστημένη
</details>

### Λειτουργία Εντοπισμού Σφαλμάτων

Για ενεργοποίηση λεπτομερούς καταγραφής, ξεσχολιάστε αυτές τις γραμμές στο [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Επόμενα Βήματα

**Η Ρύθμιση Ολοκληρώθηκε!** Συνεχίστε το ταξίδι μάθησής σας:

[Κεφάλαιο 3: Βασικές Τεχνικές Γενετικής Τεχνητής Νοημοσύνης](../../../03-CoreGenerativeAITechniques/README.md)

## Πόροι

- [Μετάβαση στο Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Επίσημο OpenAI Java SDK με Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Αυθεντικοποίηση χωρίς κλειδί με Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Πύλη Azure AI Foundry](https://ai.azure.com/)
- [Τεκμηρίωση Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθυνών**:
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία μετάφρασης με τεχνητή νοημοσύνη [Co-op Translator](https://github.com/Azure/co-op-translator). Ενώ επιδιώκουμε την ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτοματοποιημένες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->