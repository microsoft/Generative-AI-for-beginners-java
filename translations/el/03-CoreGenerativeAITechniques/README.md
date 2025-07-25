<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:32:06+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "el"
}
-->
# Βασικές Τεχνικές Γενετικής Τεχνητής Νοημοσύνης - Εκπαιδευτικό Υλικό

## Πίνακας Περιεχομένων

- [Προαπαιτούμενα](../../../03-CoreGenerativeAITechniques)
- [Ξεκινώντας](../../../03-CoreGenerativeAITechniques)
  - [Βήμα 1: Ορισμός Μεταβλητής Περιβάλλοντος](../../../03-CoreGenerativeAITechniques)
  - [Βήμα 2: Μεταβείτε στον Φάκελο Παραδειγμάτων](../../../03-CoreGenerativeAITechniques)
- [Εκπαιδευτικό Υλικό 1: Συμπληρώσεις και Συνομιλία με LLM](../../../03-CoreGenerativeAITechniques)
- [Εκπαιδευτικό Υλικό 2: Κλήση Συναρτήσεων](../../../03-CoreGenerativeAITechniques)
- [Εκπαιδευτικό Υλικό 3: RAG (Ανάκτηση-Ενισχυμένη Γενετική)](../../../03-CoreGenerativeAITechniques)
- [Εκπαιδευτικό Υλικό 4: Υπεύθυνη Τεχνητή Νοημοσύνη](../../../03-CoreGenerativeAITechniques)
- [Κοινά Μοτίβα στα Παραδείγματα](../../../03-CoreGenerativeAITechniques)
- [Επόμενα Βήματα](../../../03-CoreGenerativeAITechniques)
- [Αντιμετώπιση Προβλημάτων](../../../03-CoreGenerativeAITechniques)
  - [Συνηθισμένα Προβλήματα](../../../03-CoreGenerativeAITechniques)

## Επισκόπηση

Αυτό το εκπαιδευτικό υλικό παρέχει πρακτικά παραδείγματα βασικών τεχνικών γενετικής τεχνητής νοημοσύνης χρησιμοποιώντας Java και GitHub Models. Θα μάθετε πώς να αλληλεπιδράτε με Μεγάλα Γλωσσικά Μοντέλα (LLMs), να υλοποιείτε κλήσεις συναρτήσεων, να χρησιμοποιείτε την ανάκτηση-ενισχυμένη γενετική (RAG) και να εφαρμόζετε πρακτικές υπεύθυνης τεχνητής νοημοσύνης.

## Προαπαιτούμενα

Πριν ξεκινήσετε, βεβαιωθείτε ότι έχετε:
- Εγκατεστημένη την έκδοση Java 21 ή νεότερη
- Το Maven για τη διαχείριση εξαρτήσεων
- Έναν λογαριασμό GitHub με προσωπικό διακριτικό πρόσβασης (PAT)

## Ξεκινώντας

### Βήμα 1: Ορισμός Μεταβλητής Περιβάλλοντος

Αρχικά, πρέπει να ορίσετε το διακριτικό GitHub ως μεταβλητή περιβάλλοντος. Αυτό το διακριτικό σας επιτρέπει να έχετε πρόσβαση στα GitHub Models δωρεάν.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Βήμα 2: Μεταβείτε στον Φάκελο Παραδειγμάτων

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Εκπαιδευτικό Υλικό 1: Συμπληρώσεις και Συνομιλία με LLM

**Αρχείο:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Τι Διδάσκει Αυτό το Παράδειγμα

Αυτό το παράδειγμα δείχνει τους βασικούς μηχανισμούς αλληλεπίδρασης με Μεγάλα Γλωσσικά Μοντέλα (LLM) μέσω του OpenAI API, συμπεριλαμβανομένης της αρχικοποίησης πελάτη με GitHub Models, των μοτίβων δομής μηνυμάτων για συστημικές και χρήσιμες προτροπές, της διαχείρισης κατάστασης συνομιλίας μέσω συσσώρευσης ιστορικού μηνυμάτων και της ρύθμισης παραμέτρων για τον έλεγχο του μήκους και της δημιουργικότητας των απαντήσεων.

### Βασικές Έννοιες Κώδικα

#### 1. Ρύθμιση Πελάτη
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Αυτό δημιουργεί σύνδεση με τα GitHub Models χρησιμοποιώντας το διακριτικό σας.

#### 2. Απλή Συμπλήρωση
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Μνήμη Συνομιλίας
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Η τεχνητή νοημοσύνη θυμάται προηγούμενα μηνύματα μόνο αν τα συμπεριλάβετε σε επόμενα αιτήματα.

### Εκτέλεση του Παραδείγματος
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Τι Συμβαίνει Όταν Το Εκτελείτε

1. **Απλή Συμπλήρωση**: Η τεχνητή νοημοσύνη απαντά σε μια ερώτηση Java με καθοδήγηση από συστημική προτροπή.
2. **Συνομιλία Πολλαπλών Στροφών**: Η τεχνητή νοημοσύνη διατηρεί το πλαίσιο σε πολλαπλές ερωτήσεις.
3. **Διαδραστική Συνομιλία**: Μπορείτε να έχετε μια πραγματική συνομιλία με την τεχνητή νοημοσύνη.

## Εκπαιδευτικό Υλικό 2: Κλήση Συναρτήσεων

**Αρχείο:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Τι Διδάσκει Αυτό το Παράδειγμα

Η κλήση συναρτήσεων επιτρέπει στα μοντέλα τεχνητής νοημοσύνης να ζητούν την εκτέλεση εξωτερικών εργαλείων και APIs μέσω ενός δομημένου πρωτοκόλλου, όπου το μοντέλο αναλύει αιτήματα φυσικής γλώσσας, καθορίζει τις απαιτούμενες κλήσεις συναρτήσεων με κατάλληλες παραμέτρους χρησιμοποιώντας ορισμούς JSON Schema και επεξεργάζεται τα αποτελέσματα για να δημιουργήσει συμφραζόμενες απαντήσεις, ενώ η πραγματική εκτέλεση συναρτήσεων παραμένει υπό τον έλεγχο του προγραμματιστή για λόγους ασφάλειας και αξιοπιστίας.

### Βασικές Έννοιες Κώδικα

#### 1. Ορισμός Συνάρτησης
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Αυτό ενημερώνει την τεχνητή νοημοσύνη ποιες συναρτήσεις είναι διαθέσιμες και πώς να τις χρησιμοποιεί.

#### 2. Ροή Εκτέλεσης Συνάρτησης
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Υλοποίηση Συνάρτησης
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Εκτέλεση του Παραδείγματος
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Τι Συμβαίνει Όταν Το Εκτελείτε

1. **Συνάρτηση Καιρού**: Η τεχνητή νοημοσύνη ζητά δεδομένα καιρού για το Σιάτλ, εσείς τα παρέχετε, και η τεχνητή νοημοσύνη διαμορφώνει μια απάντηση.
2. **Συνάρτηση Υπολογιστή**: Η τεχνητή νοημοσύνη ζητά έναν υπολογισμό (15% του 240), εσείς τον εκτελείτε, και η τεχνητή νοημοσύνη εξηγεί το αποτέλεσμα.

## Εκπαιδευτικό Υλικό 3: RAG (Ανάκτηση-Ενισχυμένη Γενετική)

**Αρχείο:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Τι Διδάσκει Αυτό το Παράδειγμα

Η Ανάκτηση-Ενισχυμένη Γενετική (RAG) συνδυάζει την ανάκτηση πληροφοριών με τη γλωσσική γενετική, εισάγοντας εξωτερικό περιεχόμενο εγγράφων στις προτροπές της τεχνητής νοημοσύνης. Αυτό επιτρέπει στα μοντέλα να παρέχουν ακριβείς απαντήσεις βασισμένες σε συγκεκριμένες πηγές γνώσης, αντί για πιθανώς ξεπερασμένα ή ανακριβή δεδομένα εκπαίδευσης, διατηρώντας σαφή όρια μεταξύ ερωτήσεων χρηστών και έγκυρων πηγών πληροφοριών μέσω στρατηγικής μηχανικής προτροπών.

### Βασικές Έννοιες Κώδικα

#### 1. Φόρτωση Εγγράφου
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Εισαγωγή Πλαισίου
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Τα τριπλά εισαγωγικά βοηθούν την τεχνητή νοημοσύνη να διακρίνει μεταξύ πλαισίου και ερώτησης.

#### 3. Ασφαλής Διαχείριση Απαντήσεων
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Πάντα να επικυρώνετε τις απαντήσεις API για να αποτρέψετε σφάλματα.

### Εκτέλεση του Παραδείγματος
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Τι Συμβαίνει Όταν Το Εκτελείτε

1. Το πρόγραμμα φορτώνει το `document.txt` (περιέχει πληροφορίες για τα GitHub Models).
2. Κάνετε μια ερώτηση σχετικά με το έγγραφο.
3. Η τεχνητή νοημοσύνη απαντά μόνο με βάση το περιεχόμενο του εγγράφου, όχι τη γενική της γνώση.

Δοκιμάστε να ρωτήσετε: "Τι είναι τα GitHub Models;" σε σύγκριση με "Πώς είναι ο καιρός;"

## Εκπαιδευτικό Υλικό 4: Υπεύθυνη Τεχνητή Νοημοσύνη

**Αρχείο:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Τι Διδάσκει Αυτό το Παράδειγμα

Το παράδειγμα Υπεύθυνης Τεχνητής Νοημοσύνης αναδεικνύει τη σημασία της εφαρμογής μέτρων ασφαλείας στις εφαρμογές τεχνητής νοημοσύνης. Δείχνει φίλτρα ασφαλείας που ανιχνεύουν κατηγορίες επιβλαβούς περιεχομένου, όπως ρητορική μίσους, παρενόχληση, αυτοτραυματισμό, σεξουαλικό περιεχόμενο και βία, δείχνοντας πώς οι εφαρμογές τεχνητής νοημοσύνης παραγωγής πρέπει να διαχειρίζονται με χάρη παραβιάσεις πολιτικής περιεχομένου μέσω κατάλληλου χειρισμού εξαιρέσεων, μηχανισμών ανατροφοδότησης χρηστών και στρατηγικών εναλλακτικών απαντήσεων.

### Βασικές Έννοιες Κώδικα

#### 1. Πλαίσιο Δοκιμών Ασφαλείας
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Κατηγορίες Ασφαλείας που Δοκιμάζονται
- Οδηγίες για βία/βλάβη
- Ρητορική μίσους
- Παραβιάσεις ιδιωτικότητας
- Ιατρική παραπληροφόρηση
- Παράνομες δραστηριότητες

### Εκτέλεση του Παραδείγματος
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Τι Συμβαίνει Όταν Το Εκτελείτε

Το πρόγραμμα δοκιμάζει διάφορες επιβλαβείς προτροπές και δείχνει πώς το σύστημα ασφαλείας της τεχνητής νοημοσύνης:
1. **Αποκλείει επικίνδυνα αιτήματα** με σφάλματα HTTP 400.
2. **Επιτρέπει ασφαλές περιεχόμενο** να δημιουργείται κανονικά.
3. **Προστατεύει τους χρήστες** από επιβλαβείς εξόδους τεχνητής νοημοσύνης.

## Κοινά Μοτίβα στα Παραδείγματα

### Μοτίβο Επαλήθευσης
Όλα τα παραδείγματα χρησιμοποιούν αυτό το μοτίβο για επαλήθευση με τα GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Μοτίβο Διαχείρισης Σφαλμάτων
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Μοτίβο Δομής Μηνυμάτων
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Επόμενα Βήματα

[Κεφάλαιο 04: Πρακτικά Παραδείγματα](../04-PracticalSamples/README.md)

## Αντιμετώπιση Προβλημάτων

### Συνηθισμένα Προβλήματα

**"GITHUB_TOKEN not set"**
- Βεβαιωθείτε ότι έχετε ορίσει τη μεταβλητή περιβάλλοντος.
- Επαληθεύστε ότι το διακριτικό σας έχει το πεδίο `models:read`.

**"No response from API"**
- Ελέγξτε τη σύνδεση στο διαδίκτυο.
- Επαληθεύστε ότι το διακριτικό σας είναι έγκυρο.
- Ελέγξτε αν έχετε φτάσει τα όρια χρήσης.

**Σφάλματα μεταγλώττισης Maven**
- Βεβαιωθείτε ότι έχετε την έκδοση Java 21 ή νεότερη.
- Εκτελέστε `mvn clean compile` για να ανανεώσετε τις εξαρτήσεις.

**Αποποίηση ευθύνης**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που καταβάλλουμε προσπάθειες για ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα θα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή εσφαλμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.