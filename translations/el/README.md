<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:52:20+00:00",
  "source_file": "README.md",
  "language_code": "el"
}
-->
# Γενετικό AI για Αρχάριους - Έκδοση Java
[![Discord του Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Γενετικό AI για Αρχάριους - Έκδοση Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.el.png)

**Δέσμευση Χρόνου**: Το σεμινάριο μπορεί να ολοκληρωθεί εξολοκλήρου διαδικτυακά χωρίς τοπική εγκατάσταση. Η ρύθμιση του περιβάλλοντος διαρκεί 2 λεπτά, με την εξερεύνηση των δειγμάτων να απαιτεί 1-3 ώρες ανάλογα με το βάθος της εξερεύνησης.

> **Γρήγορη Εκκίνηση** 

1. Fork αυτό το αποθετήριο στον λογαριασμό σας στο GitHub
2. Κάντε κλικ **Code** → καρτέλα **Codespaces** → **...** → **New with options...**
3. Χρησιμοποιήστε τις προεπιλογές – αυτό θα επιλέξει το Development container που δημιουργήθηκε για αυτό το μάθημα
4. Κάντε κλικ **Create codespace**
5. Περιμένετε περίπου 2 λεπτά μέχρι να είναι έτοιμο το περιβάλλον
6. Πηγαίνετε απευθείας στο [→ Ξεκινήστε το Κεφάλαιο 1](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Προτιμάτε να Κλωνοποιήσετε Τοπικά;**
>
> Αυτό το αποθετήριο περιλαμβάνει 50+ μεταφράσεις γλωσσών που αυξάνουν σημαντικά το μέγεθος λήψης. Για να κλωνοποιήσετε χωρίς μεταφράσεις, χρησιμοποιήστε sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Αυτό σας δίνει ό,τι χρειάζεστε για να ολοκληρώσετε το μάθημα με πολύ πιο γρήγορη λήψη.


## Υποστήριξη Πολλών Γλωσσών

### Υποστηρίζεται μέσω GitHub Action (Αυτοματοποιημένο & Πάντα Ενημερωμένο)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Αραβικά](../ar/README.md) | [Μπενγκάλι](../bn/README.md) | [Βουλγαρικά](../bg/README.md) | [Βιρμανικά (Μιανμάρ)](../my/README.md) | [Κινέζικα (Απλοποιημένα)](../zh/README.md) | [Κινέζικα (Παραδοσιακά, Χονγκ Κονγκ)](../hk/README.md) | [Κινέζικα (Παραδοσιακά, Μακάο)](../mo/README.md) | [Κινέζικα (Παραδοσιακά, Ταϊβάν)](../tw/README.md) | [Κροατικά](../hr/README.md) | [Τσέχικα](../cs/README.md) | [Δανικά](../da/README.md) | [Ολλανδικά](../nl/README.md) | [Εσθονικά](../et/README.md) | [Φινλανδικά](../fi/README.md) | [Γαλλικά](../fr/README.md) | [Γερμανικά](../de/README.md) | [Ελληνικά](./README.md) | [Εβραϊκά](../he/README.md) | [Χίντι](../hi/README.md) | [Ουγγρικά](../hu/README.md) | [Ινδονησιακά](../id/README.md) | [Ιταλικά](../it/README.md) | [Ιαπωνικά](../ja/README.md) | [Κανάντα](../kn/README.md) | [Κορεατικά](../ko/README.md) | [Λιθουανικά](../lt/README.md) | [Μαλαιικά](../ms/README.md) | [Μαλαγιαλάμ](../ml/README.md) | [Μαράθι](../mr/README.md) | [Νεπαλικά](../ne/README.md) | [Νιγηριανό Πίτγκιν](../pcm/README.md) | [Νορβηγικά](../no/README.md) | [Περσικά (Φαρσί)](../fa/README.md) | [Πολωνικά](../pl/README.md) | [Πορτογαλικά (Βραζιλίας)](../br/README.md) | [Πορτογαλικά (Πορτογαλίας)](../pt/README.md) | [Πουντζαμπί (Gurmukhi)](../pa/README.md) | [Ρουμανικά](../ro/README.md) | [Ρωσικά](../ru/README.md) | [Σερβικά (Κυριλλικά)](../sr/README.md) | [Σλοβάκικα](../sk/README.md) | [Σλοβενικά](../sl/README.md) | [Ισπανικά](../es/README.md) | [Σουαχίλι](../sw/README.md) | [Σουηδικά](../sv/README.md) | [Ταγκάλογκ (Φιλιππινέζικα)](../tl/README.md) | [Ταμίλ](../ta/README.md) | [Τελούγκου](../te/README.md) | [Ταϊλανδικά](../th/README.md) | [Τουρκικά](../tr/README.md) | [Ουκρανικά](../uk/README.md) | [Ουρντού](../ur/README.md) | [Βιετναμέζικα](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Δομή Μαθήματος & Διαδρομή Μάθησης

### **Κεφάλαιο 1: Εισαγωγή στο Γενετικό AI**
- **Κύριες Έννοιες**: Κατανόηση Μεγάλων Γλωσσικών Μοντέλων, tokens, embeddings και δυνατοτήτων AI
- **Οικοσύστημα AI για Java**: Επισκόπηση του Spring AI και των OpenAI SDKs
- **Πρωτόκολλο Πλαισίου Μοντέλου**: Εισαγωγή στο MCP και ο ρόλος του στην επικοινωνία πρακτόρων AI
- **Πρακτικές Εφαρμογές**: Σενάρια πραγματικού κόσμου όπως chatbots και δημιουργία περιεχομένου
- **[→ Ξεκινήστε το Κεφάλαιο 1](./01-IntroToGenAI/README.md)**

### **Κεφάλαιο 2: Ρύθμιση Περιβάλλοντος Ανάπτυξης**
- **Διαμόρφωση Πολλαπλών Παρόχων**: Ρύθμιση GitHub Models, Azure OpenAI και ενσωματώσεων OpenAI Java SDK
- **Spring Boot + Spring AI**: Καλές πρακτικές για ανάπτυξη επιχειρηματικών εφαρμογών AI
- **GitHub Models**: Δωρεάν πρόσβαση σε μοντέλα AI για πρωτοτυποποίηση και μάθηση (δεν απαιτείται πιστωτική κάρτα)
- **Εργαλεία Ανάπτυξης**: Docker containers, VS Code και ρύθμιση GitHub Codespaces
- **[→ Ξεκινήστε το Κεφάλαιο 2](./02-SetupDevEnvironment/README.md)**

### **Κεφάλαιο 3: Βασικές Τεχνικές Γενετικού AI**
- **Σχεδίαση Προτροπών**: Τεχνικές για βέλτιστες απαντήσεις μοντέλων AI
- **Embeddings & Διανυσματικές Λειτουργίες**: Υλοποίηση σημασιολογικής αναζήτησης και αντιστοίχισης ομοιότητας
- **Retrieval-Augmented Generation (RAG)**: Συνδυάστε το AI με τις δικές σας πηγές δεδομένων
- **Κλήση Συναρτήσεων**: Επεκτείνετε τις δυνατότητες του AI με προσαρμοσμένα εργαλεία και πρόσθετα
- **[→ Ξεκινήστε το Κεφάλαιο 3](./03-CoreGenerativeAITechniques/README.md)**

### **Κεφάλαιο 4: Πρακτικές Εφαρμογές & Έργα**
- **Γεννήτρια Ιστοριών Κατοικίδιων** (`petstory/`): Δημιουργική παραγωγή περιεχομένου με GitHub Models
- **Τοπική Επίδειξη Foundry** (`foundrylocal/`): Τοπική ενσωμάτωση μοντέλου AI με OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): Βασική υλοποίηση του Model Context Protocol με Spring AI
- **[→ Ξεκινήστε το Κεφάλαιο 4](./04-PracticalSamples/README.md)**

### **Κεφάλαιο 5: Υπεύθυνη Ανάπτυξη AI**
- **Ασφάλεια GitHub Models**: Δοκιμάστε το ενσωματωμένο φιλτράρισμα περιεχομένου και τους μηχανισμούς ασφάλειας (αυστηρά μπλοκαρίσματα και μαλακές απορρίψεις)
- **Επίδειξη Υπεύθυνου AI**: Πρακτικό παράδειγμα που δείχνει πώς λειτουργούν τα σύγχρονα συστήματα ασφάλειας AI στην πράξη
- **Καλές Πρακτικές**: Βασικές οδηγίες για ηθική ανάπτυξη και υλοποίηση AI
- **[→ Ξεκινήστε το Κεφάλαιο 5](./05-ResponsibleGenAI/README.md)**

## Πρόσθετοι Πόροι

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j για Αρχάριους](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js για Αρχάριους](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Πράκτορες
[![AZD για Αρχάριους](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI για Αρχάριους](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP για Αρχάριους](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Πράκτορες για Αρχάριους](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Σειρά Γενετικού AI
[![Γενετικό AI για Αρχάριους](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Γενετικό AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Γενετικό AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Γενετικό AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Βασικά Μαθήματα
[![ML για Αρχάριους](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Επιστήμη Δεδομένων για Αρχάριους](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI για Αρχάριους](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Κυβερνοασφάλεια για Αρχάριους](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Ανάπτυξη Ιστού για Αρχάριους](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->
[![IoT για Αρχάριους](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Ανάπτυξη XR για Αρχάριους](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Σειρά Copilot
[![Copilot για Συνεργατικό Προγραμματισμό με AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot για C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Περιπέτεια Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Λήψη Βοήθειας

Εάν κολλήσετε ή έχετε οποιεσδήποτε ερωτήσεις σχετικά με τη δημιουργία εφαρμογών AI. Συμμετάσχετε με άλλους μαθητές και έμπειρους προγραμματιστές σε συζητήσεις σχετικά με το MCP. Είναι μια υποστηρικτική κοινότητα όπου οι ερωτήσεις είναι ευπρόσδεκτες και η γνώση μοιράζεται ελεύθερα.

[![Microsoft Foundry στο Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Εάν έχετε σχόλια για το προϊόν ή αντιμετωπίζετε σφάλματα κατά την ανάπτυξη, επισκεφτείτε:

[![Φόρουμ Προγραμματιστών Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Αποποίηση ευθυνών:
Το παρόν έγγραφο έχει μεταφραστεί με τη χρήση της υπηρεσίας αυτόματης μετάφρασης τεχνητής νοημοσύνης Co-op Translator (https://github.com/Azure/co-op-translator). Παρά τις προσπάθειές μας για ακρίβεια, παρακαλούμε να λάβετε υπόψη ότι οι αυτοματοποιημένες μεταφράσεις ενδέχεται να περιέχουν σφάλματα ή ανακρίβειες. Το αρχικό έγγραφο στη γλώσσα προέλευσής του πρέπει να θεωρείται η αυθεντική πηγή. Για κρίσιμες πληροφορίες συνιστάται επαγγελματική ανθρώπινη μετάφραση. Δεν ευθυνόμαστε για τυχόν παρερμηνείες ή λανθασμένες ερμηνείες που προκύπτουν από τη χρήση αυτής της μετάφρασης.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->