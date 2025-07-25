<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:33:03+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "el"
}
-->
# Τοπικό Εγχειρίδιο Foundry Spring Boot

## Πίνακας Περιεχομένων

- [Προαπαιτούμενα](../../../../04-PracticalSamples/foundrylocal)
- [Επισκόπηση Έργου](../../../../04-PracticalSamples/foundrylocal)
- [Κατανόηση του Κώδικα](../../../../04-PracticalSamples/foundrylocal)
  - [1. Ρύθμιση Εφαρμογής (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Κύρια Κλάση Εφαρμογής (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Επίπεδο Υπηρεσίας AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Εξαρτήσεις Έργου (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Πώς Λειτουργούν Όλα Μαζί](../../../../04-PracticalSamples/foundrylocal)
- [Ρύθμιση του Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Εκτέλεση της Εφαρμογής](../../../../04-PracticalSamples/foundrylocal)
- [Αναμενόμενο Αποτέλεσμα](../../../../04-PracticalSamples/foundrylocal)
- [Επόμενα Βήματα](../../../../04-PracticalSamples/foundrylocal)
- [Αντιμετώπιση Προβλημάτων](../../../../04-PracticalSamples/foundrylocal)

## Προαπαιτούμενα

Πριν ξεκινήσετε αυτό το εγχειρίδιο, βεβαιωθείτε ότι έχετε:

- **Java 21 ή νεότερη έκδοση** εγκατεστημένη στο σύστημά σας
- **Maven 3.6+** για τη δημιουργία του έργου
- **Foundry Local** εγκατεστημένο και σε λειτουργία

### **Εγκατάσταση του Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Επισκόπηση Έργου

Αυτό το έργο αποτελείται από τέσσερα κύρια στοιχεία:

1. **Application.java** - Το κύριο σημείο εισόδου της εφαρμογής Spring Boot
2. **FoundryLocalService.java** - Επίπεδο υπηρεσίας που χειρίζεται την επικοινωνία με την AI
3. **application.properties** - Ρυθμίσεις για τη σύνδεση με το Foundry Local
4. **pom.xml** - Εξαρτήσεις Maven και ρυθμίσεις έργου

## Κατανόηση του Κώδικα

### 1. Ρύθμιση Εφαρμογής (application.properties)

**Αρχείο:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Τι κάνει:**
- **base-url**: Καθορίζει πού εκτελείται το Foundry Local (προεπιλεγμένη θύρα 5273)
- **model**: Ορίζει το όνομα του μοντέλου AI που θα χρησιμοποιηθεί για τη δημιουργία κειμένου

**Βασική έννοια:** Το Spring Boot φορτώνει αυτόματα αυτές τις ιδιότητες και τις καθιστά διαθέσιμες στην εφαρμογή σας χρησιμοποιώντας την αναφορά `@Value`.

### 2. Κύρια Κλάση Εφαρμογής (Application.java)

**Αρχείο:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Τι κάνει:**
- `@SpringBootApplication` ενεργοποιεί την αυτόματη ρύθμιση του Spring Boot
- `WebApplicationType.NONE` δηλώνει ότι πρόκειται για εφαρμογή γραμμής εντολών, όχι για διακομιστή ιστού
- Η κύρια μέθοδος εκκινεί την εφαρμογή Spring

**Ο Εκτελεστής Επίδειξης:**
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

**Τι κάνει:**
- `@Bean` δημιουργεί ένα στοιχείο που διαχειρίζεται το Spring
- `CommandLineRunner` εκτελεί κώδικα μετά την εκκίνηση του Spring Boot
- Το `foundryLocalService` εισάγεται αυτόματα από το Spring (dependency injection)
- Στέλνει ένα δοκιμαστικό μήνυμα στην AI και εμφανίζει την απάντηση

### 3. Επίπεδο Υπηρεσίας AI (FoundryLocalService.java)

**Αρχείο:** `src/main/java/com/example/FoundryLocalService.java`

#### Εισαγωγή Ρυθμίσεων:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Τι κάνει:**
- `@Service` δηλώνει ότι αυτή η κλάση παρέχει επιχειρησιακή λογική
- `@Value` εισάγει τιμές ρυθμίσεων από το application.properties
- Η σύνταξη `:default-value` παρέχει εναλλακτικές τιμές αν δεν έχουν οριστεί ιδιότητες

#### Αρχικοποίηση Πελάτη:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Τι κάνει:**
- `@PostConstruct` εκτελεί αυτή τη μέθοδο μετά τη δημιουργία της υπηρεσίας από το Spring
- Δημιουργεί έναν πελάτη OpenAI που συνδέεται με την τοπική σας εγκατάσταση Foundry Local
- Η διαδρομή `/v1` είναι απαραίτητη για συμβατότητα με το API του OpenAI
- Το API key είναι "unused" επειδή η τοπική ανάπτυξη δεν απαιτεί αυθεντικοποίηση

#### Μέθοδος Συνομιλίας:
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

**Τι κάνει:**
- **ChatCompletionCreateParams**: Ρυθμίζει το αίτημα προς την AI
  - `model`: Καθορίζει ποιο μοντέλο AI θα χρησιμοποιηθεί
  - `addUserMessage`: Προσθέτει το μήνυμά σας στη συνομιλία
  - `maxCompletionTokens`: Περιορίζει το μήκος της απάντησης (εξοικονόμηση πόρων)
  - `temperature`: Ελέγχει την τυχαιότητα (0.0 = ντετερμινιστικό, 1.0 = δημιουργικό)
- **Κλήση API**: Στέλνει το αίτημα στο Foundry Local
- **Επεξεργασία Απάντησης**: Εξάγει με ασφάλεια την απάντηση της AI
- **Διαχείριση Σφαλμάτων**: Τυλίγει εξαιρέσεις με χρήσιμα μηνύματα σφάλματος

### 4. Εξαρτήσεις Έργου (pom.xml)

**Κύριες Εξαρτήσεις:**

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

**Τι κάνουν:**
- **spring-boot-starter**: Παρέχει βασική λειτουργικότητα του Spring Boot
- **openai-java**: Επίσημο OpenAI Java SDK για επικοινωνία με το API
- **jackson-databind**: Χειρίζεται τη σειριοποίηση/αποσειριοποίηση JSON για κλήσεις API

## Πώς Λειτουργούν Όλα Μαζί

Ακολουθεί η πλήρης ροή όταν εκτελείτε την εφαρμογή:

1. **Εκκίνηση**: Το Spring Boot ξεκινά και διαβάζει το `application.properties`
2. **Δημιουργία Υπηρεσίας**: Το Spring δημιουργεί το `FoundryLocalService` και εισάγει τιμές ρυθμίσεων
3. **Ρύθμιση Πελάτη**: Το `@PostConstruct` αρχικοποιεί τον πελάτη OpenAI για σύνδεση με το Foundry Local
4. **Εκτέλεση Επίδειξης**: Το `CommandLineRunner` εκτελείται μετά την εκκίνηση
5. **Κλήση AI**: Η επίδειξη καλεί τη μέθοδο `foundryLocalService.chat()` με ένα δοκιμαστικό μήνυμα
6. **Αίτημα API**: Η υπηρεσία δημιουργεί και στέλνει αίτημα συμβατό με το OpenAI στο Foundry Local
7. **Επεξεργασία Απάντησης**: Η υπηρεσία εξάγει και επιστρέφει την απάντηση της AI
8. **Εμφάνιση**: Η εφαρμογή εκτυπώνει την απάντηση και τερματίζει

## Ρύθμιση του Foundry Local

Για να ρυθμίσετε το Foundry Local, ακολουθήστε τα εξής βήματα:

1. **Εγκαταστήστε το Foundry Local** χρησιμοποιώντας τις οδηγίες στην ενότητα [Προαπαιτούμενα](../../../../04-PracticalSamples/foundrylocal).
2. **Κατεβάστε το μοντέλο AI** που θέλετε να χρησιμοποιήσετε, για παράδειγμα, `phi-3.5-mini`, με την ακόλουθη εντολή:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Ρυθμίστε το αρχείο application.properties** ώστε να ταιριάζει με τις ρυθμίσεις του Foundry Local, ειδικά αν χρησιμοποιείτε διαφορετική θύρα ή μοντέλο.

## Εκτέλεση της Εφαρμογής

### Βήμα 1: Εκκινήστε το Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Βήμα 2: Δημιουργήστε και Εκτελέστε την Εφαρμογή
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Αναμενόμενο Αποτέλεσμα

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

## Επόμενα Βήματα

Για περισσότερα παραδείγματα, δείτε [Κεφάλαιο 04: Πρακτικά δείγματα](../README.md)

## Αντιμετώπιση Προβλημάτων

### Συνηθισμένα Προβλήματα

**"Connection refused" ή "Service unavailable"**
- Βεβαιωθείτε ότι το Foundry Local εκτελείται: `foundry model list`
- Επαληθεύστε ότι η υπηρεσία είναι στη θύρα 5273: Ελέγξτε το `application.properties`
- Δοκιμάστε να επανεκκινήσετε το Foundry Local: `foundry model run phi-3.5-mini`

**Σφάλματα "Model not found"**
- Ελέγξτε τα διαθέσιμα μοντέλα: `foundry model list`
- Ενημερώστε το όνομα του μοντέλου στο `application.properties` ώστε να ταιριάζει ακριβώς
- Κατεβάστε το μοντέλο αν χρειάζεται: `foundry model run phi-3.5-mini`

**Σφάλματα κατά τη σύνταξη με Maven**
- Βεβαιωθείτε ότι έχετε Java 21 ή νεότερη έκδοση: `java -version`
- Καθαρίστε και επαναδημιουργήστε: `mvn clean compile`
- Ελέγξτε τη σύνδεση στο διαδίκτυο για λήψη εξαρτήσεων

**Η εφαρμογή ξεκινά αλλά δεν εμφανίζει αποτέλεσμα**
- Επαληθεύστε ότι το Foundry Local ανταποκρίνεται: Ανοίξτε τον περιηγητή στο `http://localhost:5273`
- Ελέγξτε τα αρχεία καταγραφής της εφαρμογής για συγκεκριμένα μηνύματα σφάλματος
- Βεβαιωθείτε ότι το μοντέλο έχει φορτωθεί πλήρως και είναι έτοιμο

**Αποποίηση Ευθύνης**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που καταβάλλουμε προσπάθειες για ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα θα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή εσφαλμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.