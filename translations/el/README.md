<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:31:36+00:00",
  "source_file": "README.md",
  "language_code": "el"
}
-->
# Γενετική Τεχνητή Νοημοσύνη για Αρχάριους - Έκδοση Java
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Γενετική Τεχνητή Νοημοσύνη για Αρχάριους - Έκδοση Java](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.el.png)

**Χρόνος Δέσμευσης**: Το εργαστήριο μπορεί να ολοκληρωθεί εξ ολοκλήρου online χωρίς τοπική εγκατάσταση. Εάν θέλετε να εκτελέσετε τα παραδείγματα, η ρύθμιση του περιβάλλοντος απαιτεί 2 λεπτά, ενώ η εξερεύνηση των παραδειγμάτων απαιτεί 1-3 ώρες, ανάλογα με το βάθος της εξερεύνησης.

> **Γρήγορη Έναρξη**

1. Κάντε fork αυτό το αποθετήριο στον λογαριασμό σας στο GitHub
2. Κάντε κλικ στο **Code** → καρτέλα **Codespaces** → **...** → **New with options...**
3. Χρησιμοποιήστε τις προεπιλογές – αυτό θα επιλέξει το Development container που δημιουργήθηκε για αυτό το μάθημα
4. Κάντε κλικ στο **Create codespace**
5. Περιμένετε ~2 λεπτά για να είναι έτοιμο το περιβάλλον
6. Μεταβείτε απευθείας στο [Δημιουργία του Token Μοντέλων GitHub](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Υποστήριξη Πολλαπλών Γλωσσών

### Υποστηρίζεται μέσω GitHub Action (Αυτοματοποιημένο & Πάντα Ενημερωμένο)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](./README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## Δομή Μαθήματος & Διαδρομή Μάθησης

### **Κεφάλαιο 1: Εισαγωγή στη Γενετική Τεχνητή Νοημοσύνη**
- **Βασικές Έννοιες**: Κατανόηση Μεγάλων Γλωσσικών Μοντέλων, tokens, embeddings και δυνατοτήτων AI
- **Οικοσύστημα AI για Java**: Επισκόπηση των Spring AI και OpenAI SDKs
- **Πρωτόκολλο Context Μοντέλου**: Εισαγωγή στο MCP και τον ρόλο του στην επικοινωνία των AI agents
- **Πρακτικές Εφαρμογές**: Σενάρια πραγματικού κόσμου, όπως chatbots και δημιουργία περιεχομένου
- **[→ Έναρξη Κεφαλαίου 1](./01-IntroToGenAI/README.md)**

### **Κεφάλαιο 2: Ρύθμιση Περιβάλλοντος Ανάπτυξης**
- **Διαμόρφωση Πολλαπλών Παρόχων**: Ρύθμιση ενσωματώσεων GitHub Models, Azure OpenAI και OpenAI Java SDK
- **Spring Boot + Spring AI**: Βέλτιστες πρακτικές για ανάπτυξη εφαρμογών AI σε επιχειρησιακό επίπεδο
- **GitHub Models**: Δωρεάν πρόσβαση σε μοντέλα AI για πρωτότυπα και μάθηση (χωρίς πιστωτική κάρτα)
- **Εργαλεία Ανάπτυξης**: Διαμόρφωση Docker containers, VS Code και GitHub Codespaces
- **[→ Έναρξη Κεφαλαίου 2](./02-SetupDevEnvironment/README.md)**

### **Κεφάλαιο 3: Βασικές Τεχνικές Γενετικής Τεχνητής Νοημοσύνης**
- **Μηχανική Προτροπών**: Τεχνικές για βέλτιστες απαντήσεις μοντέλων AI
- **Embeddings & Λειτουργίες Διανυσμάτων**: Υλοποίηση σημασιολογικής αναζήτησης και αντιστοίχισης ομοιότητας
- **Ανάκτηση-Εμπλουτισμένη Γενετική (RAG)**: Συνδυασμός AI με δικές σας πηγές δεδομένων
- **Κλήση Λειτουργιών**: Επέκταση δυνατοτήτων AI με προσαρμοσμένα εργαλεία και plugins
- **[→ Έναρξη Κεφαλαίου 3](./03-CoreGenerativeAITechniques/README.md)**

### **Κεφάλαιο 4: Πρακτικές Εφαρμογές & Έργα**
- **Γεννήτρια Ιστοριών Κατοικίδιων** (`petstory/`): Δημιουργία δημιουργικού περιεχομένου με GitHub Models
- **Τοπική Επίδειξη Foundry** (`foundrylocal/`): Ενσωμάτωση τοπικών μοντέλων AI με OpenAI Java SDK
- **Υπηρεσία Υπολογιστή MCP** (`mcp/calculator/`): Βασική υλοποίηση Πρωτοκόλλου Context Μοντέλου με Spring AI
- **[→ Έναρξη Κεφαλαίου 4](./04-PracticalSamples/README.md)**

### **Κεφάλαιο 5: Υπεύθυνη Ανάπτυξη AI**
- **Ασφάλεια GitHub Models**: Δοκιμή ενσωματωμένων μηχανισμών φιλτραρίσματος περιεχομένου και ασφάλειας
- **Επίδειξη Υπεύθυνης AI**: Πρακτικό παράδειγμα που δείχνει πώς λειτουργούν τα φίλτρα ασφάλειας AI
- **Βέλτιστες Πρακτικές**: Ουσιαστικές οδηγίες για ηθική ανάπτυξη και υλοποίηση AI
- **[→ Έναρξη Κεφαλαίου 5](./05-ResponsibleGenAI/README.md)**

## Πρόσθετοι Πόροι 

- [AI Agents For Beginners](https://github.com/microsoft/ai-agents-for-beginners)
- [Generative AI for Beginners using .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generative AI for Beginners using JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generative AI for Beginners](https://github.com/microsoft/generative-ai-for-beginners)
- [ML for Beginners](https://aka.ms/ml-beginners)
- [Data Science for Beginners](https://aka.ms/datascience-beginners)
- [AI for Beginners](https://aka.ms/ai-beginners)
- [Cybersecurity for Beginners](https://github.com/microsoft/Security-101)
- [Web Dev for Beginners](https://aka.ms/webdev-beginners)
- [IoT for Beginners](https://aka.ms/iot-beginners)
- [XR Development for Beginners](https://github.com/microsoft/xr-development-for-beginners)
- [Mastering GitHub Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI)
- [Mastering GitHub Copilot for C#/.NET Developers](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Choose Your Own Copilot Adventure](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App with Azure AI Services](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Αποποίηση ευθύνης**:  
Αυτό το έγγραφο έχει μεταφραστεί χρησιμοποιώντας την υπηρεσία αυτόματης μετάφρασης [Co-op Translator](https://github.com/Azure/co-op-translator). Παρόλο που καταβάλλουμε προσπάθειες για ακρίβεια, παρακαλούμε να έχετε υπόψη ότι οι αυτόματες μεταφράσεις ενδέχεται να περιέχουν λάθη ή ανακρίβειες. Το πρωτότυπο έγγραφο στη μητρική του γλώσσα θα πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες, συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν φέρουμε ευθύνη για τυχόν παρεξηγήσεις ή εσφαλμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.