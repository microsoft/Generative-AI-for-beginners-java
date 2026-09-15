# Διαμόρφωση του Περιβάλλοντος Ανάπτυξης για το Azure AI Foundry

> Αυτός ο οδηγός διαμορφώνει τα μοντέλα **Azure AI Foundry** για τις εφαρμογές Java AI σε αυτό το μάθημα, χρησιμοποιώντας **authentication χωρίς κλειδιά** (Microsoft Entra ID) — χωρίς διαχείριση κλειδιών API. Νέα στα εργαλεία; Ξεκινήστε με τον [οδηγό περιβάλλοντος ανάπτυξης](./README.md).

Αυτός ο οδηγός διαμορφώνει τα μοντέλα **Azure AI Foundry** για τις εφαρμογές Java AI σε αυτό το μάθημα. Έχετε δύο επιλογές:

- **Επιλογή Α — Provision με `azd` + Bicep (συνιστώμενο):** μια εντολή αναπτύσσει τον λογαριασμό Foundry και τα μοντέλα ως κώδικα. Χωρίς κλικ σε portal.
- **Επιλογή Β — Δημιουργία πόρων με το χέρι** στο portal Azure AI Foundry.

Και οι δύο δρόμοι χρησιμοποιούν **authentication χωρίς κλειδιά** (Microsoft Entra ID) — δεν υπάρχουν κλειδιά API για αντιγραφή ή διαρροή.

## Πίνακας Περιεχομένων

- [Τι Δημιουργείται](#τι-δημιουργείται)
- [Απαιτήσεις](#απαιτήσεις)
- [Επιλογή Α: Provision με azd + Bicep (Συνιστώμενο)](#option-a-provision-with-azd--bicep-recommended)
- [Επιλογή Β: Δημιουργία Πόρων με το Χέρι](#επιλογή-β-δημιουργία-πόρων-με-το-χέρι)
- [Διαμόρφωση του Περιβάλλοντος σας](#διαμόρφωση-του-περιβάλλοντος-σας)
- [Δοκιμάστε τη Διαμόρφωση σας](#δοκιμάστε-τη-διαμόρφωση-σας)
- [Τι Ακολουθεί;](#τι-ακολουθεί)
- [Πόροι](#πόροι)
- [Επιπλέον Πόροι](#επιπλέον-πόροι)

## Τι Δημιουργείται

Τα πρότυπα Bicep στον φάκελο [`infra/`](../../../02-SetupDevEnvironment/infra) κάνουν provisioning:

- Έναν λογαριασμό **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, είδος `AIServices`) με ένα έργο
- Μία ανάπτυξη **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), έκδοση `2026-07-09`, με ικανότητα `GlobalStandard` `10` (10 αιτήματα/λεπτό και 10.000 tokens/λεπτό για αυτό το μοντέλο)
- Μία ανάπτυξη **embedding** - `text-embedding-3-small`, έκδοση `1` (χρησιμοποιείται σε επόμενα κεφάλαια)
- Μία **ανάθεση ρόλου χωρίς κλειδί** (`Cognitive Services OpenAI User`) ώστε να κάνετε σύνδεση με `az login` αντί για διαχείριση κλειδιών

## Απαιτήσεις

- Έναν [Azure subscription](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) και [Maven 3.9+](https://maven.apache.org/download.cgi)

## Επιλογή Α: Provision με azd + Bicep (Συνιστώμενο)

Από τον φάκελο `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Σύνδεση (και τα δύο εργαλεία)
azd auth login
az login

# Προετοιμασία του λογαριασμού Foundry + αναπτύξεις μοντέλων
azd up
```

Το `azd` ζητά ένα **όνομα περιβάλλοντος** (π.χ. `genai-java`), **subscription** και **περιοχή**. Επιλέξτε το δικό σας subscription και μια περιοχή όπου είναι διαθέσιμα τα `gpt-5.6-luna` και `text-embedding-3-small`, π.χ. `eastus2`. Επιβεβαιώστε ότι το subscription διαθέτει επαρκές quota για το μοντέλο και τον τύπο ανάπτυξης σε εκείνη την περιοχή· η διαθεσιμότητα και το quota διαφέρουν ανά subscription.

Όταν ολοκληρωθεί το provisioning, το azd:

1. Αναπτύσσει όλα όσα ορίζονται στο [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Εκτελεί ένα postprovision hook που γράφει το αρχείο [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) με το endpoint και τα ονόματα ανάπτυξης (χωρίς μυστικά).

> **Συμβουλή:** Εκτελέστε ξανά `azd up` οποτεδήποτε για να εφαρμόσετε αλλαγές. Εκτελέστε `azd down` για να διαγράψετε τα πάντα και να σταματήσουν τα κόστη.

Για να δείτε τις δημιουργημένες ρυθμίσεις:

```bash
azd env get-values
```

Τώρα πηγαίνετε στο [Δοκιμάστε τη Διαμόρφωση σας](#δοκιμάστε-τη-διαμόρφωση-σας).

## Επιλογή Β: Δημιουργία Πόρων με το Χέρι

Προτιμάτε το portal; Δημιουργήστε τους πόρους χειροκίνητα:

1. Μεταβείτε στο [portal Azure AI Foundry](https://ai.azure.com/) και κάντε σύνδεση.
2. **Δημιουργήστε ένα έργο** (αυτό δημιουργεί και πόρο AI Foundry). Δώστε του ένα όνομα όπως `GenAIJava`.
3. Στο έργο σας, ανοίξτε **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Αναπτύξτε το **GPT-5.6 Luna** (όνομα μοντέλου και ανάπτυξης `gpt-5.6-luna`, έκδοση `2026-07-09`) με ικανότητα **Global Standard** `10`. Επαναλάβετε για το **text-embedding-3-small**, έκδοση `1`, αν θέλετε παραδείγματα embeddings.
5. Από το **Overview**, αντιγράψτε το **endpoint** (π.χ. `https://<resource>.openai.azure.com/`).
6. Χορηγήστε πρόσβαση χωρίς κλειδί: στον πόρο, ανοίξτε **Access control (IAM)** → **Add role assignment** → αναθέστε τον ρόλο **Cognitive Services OpenAI User** στο λογαριασμό σας.

> **Ακόμα δυσκολεύεστε;** Δείτε την [τεκμηρίωση Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Διαμόρφωση του Περιβάλλοντος σας

**Αν χρησιμοποιήσατε την Επιλογή Α (`azd up`)**, το αρχείο ρυθμίσεών σας έχει ήδη γραφτεί — δεν χρειάζεται καμία διαμόρφωση. Πηγαίνετε απευθείας στο [Δοκιμάστε τη Διαμόρφωση σας](#δοκιμάστε-τη-διαμόρφωση-σας).

**Αν χρησιμοποιήσατε την Επιλογή Β (χειροκίνητα)**, δημιουργήστε μόνοι σας το `.env` του παραδείγματος:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Επεξεργαστείτε το `.env` με το endpoint σας (χωρίς κλειδί — η αυθεντικοποίηση είναι χωρίς κλειδί):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Χρησιμοποιήστε το Azure OpenAI endpoint του πόρου, όχι το URL του έργου. Η βασική εφαρμογή chat το μεταφράζει σε `/openai/v1` και διαμορφώνει ρητά έναν πελάτη με bearer-token· δεν απαιτείται κλειδί API.

> **Σημείωση ασφαλείας:** Δεν υπάρχει κλειδί API για αποθήκευση. Αυθεντικοποιείστε με Microsoft Entra ID μέσω `az login` (τοπικά) ή managed identity (στο Azure). Το αρχείο `.env` περιέχει μόνο μη μυστικές ρυθμίσεις και έχει ήδη προστεθεί στο `.gitignore`.

## Δοκιμάστε τη Διαμόρφωση σας

Βεβαιωθείτε ότι έχετε συνδεθεί για να πάρει η αυθεντικοποίηση χωρίς κλειδί ένα token, μετά εκτελέστε το παράδειγμα:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # αν δεν έχετε ήδη συνδεθεί
mvn clean spring-boot:run
```

Θα πρέπει να δείτε μια απάντηση από το μοντέλο `gpt-5.6-luna`. Εκτελέστε τα παραδείγματα διαδοχικά για να παραμείνετε εντός του μικρού προεπιλεγμένου quota· αν λάβετε HTTP 429, περιμένετε το διάστημα επανάληψης πριν ξαναδοκιμάσετε.

> **Χρήστες VS Code:** Πατήστε `F5` για εκτέλεση. Η εφαρμογή φορτώνει το `.env` σας αυτόματα.

> **Πλήρες παράδειγμα:** Δείτε το [Παράδειγμα Basic Chat με Azure AI Foundry](./examples/basic-chat-azure/README.md) για λεπτομέρειες και αντιμετώπιση προβλημάτων.

## Τι Ακολουθεί;

Μετά το provisioning και την επιτυχή εκτέλεση του παραδείγματος, θα έχετε:
- Azure AI Foundry με `gpt-5.6-luna` και `text-embedding-3-small` αναπτυγμένα
- Εξουσιοδότηση χωρίς κλειδί (Microsoft Entra ID) — χωρίς διαχείριση κλειδιών
- Τοπικό `.env` με το endpoint και τα ονόματα ανάπτυξης σας
- Ένα περιβάλλον ανάπτυξης Java έτοιμο για χρήση

**Συνεχίστε στο** [Κεφάλαιο 3: Βασικές Τεχνικές Γενετικής Τεχνητής Νοημοσύνης](../03-CoreGenerativeAITechniques/README.md) για να ξεκινήσετε την ανάπτυξη εφαρμογών AI!

## Πόροι

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Authentication χωρίς κλειδί με Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Τεκμηρίωση Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Μετάβαση Spring AI 2 στο OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Επίσημο OpenAI Java SDK με Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Επιπλέον Πόροι

- [Κατεβάστε το VS Code](https://code.visualstudio.com/Download)
- [Λάβετε το Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Ρύθμιση Περιβάλλοντος Ανάπτυξης (Dev Container)](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Αποποίηση ευθυνών**:
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία μετάφρασης με τεχνητή νοημοσύνη [Co-op Translator](https://github.com/Azure/co-op-translator). Ενώ επιδιώκουμε την ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτοματοποιημένες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->