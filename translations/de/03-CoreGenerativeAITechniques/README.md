<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0a27b17f64f598a80b72d93b98b7ed04",
  "translation_date": "2025-07-21T15:31:04+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "de"
}
-->
# Kerntechniken der Generativen KI

>**Hinweis**: Dieses Kapitel enthält ein ausführliches [**Tutorial**](./TUTORIAL.md), das Sie durch die Ausführung der fertigen Beispiele führt.

## Was Sie lernen werden
In diesem Kapitel betrachten wir anhand praktischer Beispiele vier grundlegende Techniken der generativen KI:
- LLM-Vervollständigungen und Chat-Flows
- Funktionsaufrufe
- Retrieval-Augmented Generation (RAG)
- Sicherheitsmaßnahmen für verantwortungsvolle KI

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../03-CoreGenerativeAITechniques)
- [Voraussetzungen](../../../03-CoreGenerativeAITechniques)
- [Erste Schritte](../../../03-CoreGenerativeAITechniques)
- [Übersicht der Beispiele](../../../03-CoreGenerativeAITechniques)
  - [1. LLM-Vervollständigungen und Chat-Flows](../../../03-CoreGenerativeAITechniques)
  - [2. Funktionen & Plugins mit LLMs](../../../03-CoreGenerativeAITechniques)
  - [3. Retrieval-Augmented Generation (RAG)](../../../03-CoreGenerativeAITechniques)
  - [4. Demonstration von Sicherheitsmaßnahmen für verantwortungsvolle KI](../../../03-CoreGenerativeAITechniques)
- [Zusammenfassung](../../../03-CoreGenerativeAITechniques)
- [Nächste Schritte](../../../03-CoreGenerativeAITechniques)

## Voraussetzungen

- Abgeschlossene Einrichtung aus [Kapitel 2](../../../02-SetupDevEnvironment)

## Erste Schritte

1. **Zu den Beispielen navigieren**: 
```bash
cd 03-CoreGenerativeAITechniques/examples/
```
2. **Umgebung einrichten**: 
```bash
export GITHUB_TOKEN=your_token_here
```
3. **Beispiele kompilieren und ausführen**:
```bash
   # Run completions example
   mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
   
   # Run functions example  
   mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
   
   # Run RAG example
   mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
   
   # Run responsible AI demo
   mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
   ```

## Übersicht der Beispiele

Die Beispiele sind im Ordner `examples/` mit der folgenden Struktur organisiert:

```
examples/
├── src/main/java/com/example/genai/techniques/
│   ├── completions/
│   │   └── LLMCompletionsApp.java        # Basic completions 
│   ├── functions/
│   │   └── FunctionsApp.java             # Function calling examples
│   ├── rag/
│   │   └── SimpleReaderDemo.java         # Retrieval-Augmented Generation
│   └── responsibleai/
│       └── ResponsibleGithubModels.java  # Responsible AI safety demonstration
├── document.txt                          # Sample document for RAG example
└── pom.xml                               # Maven configuration
```

### 1. LLM-Vervollständigungen und Chat-Flows
**Datei**: `examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

Lernen Sie, wie man KI-gestützte Konversationen mit Streaming-Antworten und Chat-Verlaufsverwaltung erstellt.

Dieses Beispiel zeigt:
- Einfache Textvervollständigung mit System-Prompts
- Mehrstufige Konversationen mit Verlaufsverwaltung
- Interaktive Chat-Sitzungen
- Parameterkonfiguration (Temperatur, maximale Tokenanzahl)

### 2. Funktionen & Plugins mit LLMs
**Datei**: `examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

Erweitern Sie die Fähigkeiten der KI, indem Sie Modellen Zugriff auf benutzerdefinierte Funktionen und externe APIs geben.

Dieses Beispiel zeigt:
- Integration einer Wetterfunktion
- Implementierung eines Taschenrechners
- Mehrere Funktionsaufrufe in einer Konversation
- Funktionsdefinition mit JSON-Schemata

### 3. Retrieval-Augmented Generation (RAG)
**Datei**: `examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

Erfahren Sie, wie Sie KI mit Ihren eigenen Dokumenten und Datenquellen kombinieren, um präzise, kontextbezogene Antworten zu erhalten.

Dieses Beispiel zeigt:
- Dokumentenbasierte Frage-Antwort-Funktion mit dem Azure OpenAI SDK
- Implementierung des RAG-Musters mit GitHub-Modellen

**Anwendung**: Stellen Sie Fragen zum Inhalt von `document.txt` und erhalten Sie KI-Antworten, die ausschließlich auf diesem Kontext basieren.

### 4. Demonstration von Sicherheitsmaßnahmen für verantwortungsvolle KI
**Datei**: `examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

Erhalten Sie einen Einblick, wie Sicherheitsmaßnahmen in der KI funktionieren, indem Sie die Inhaltsfilterfunktionen der GitHub-Modelle testen.

Dieses Beispiel zeigt:
- Inhaltsfilterung für potenziell schädliche Eingaben
- Umgang mit Sicherheitsantworten in Anwendungen
- Verschiedene Kategorien blockierter Inhalte (Gewalt, Hassrede, Fehlinformationen)
- Korrekte Fehlerbehandlung bei Sicherheitsverstößen

> **Erfahren Sie mehr**: Dies ist nur eine Einführung in die Konzepte der verantwortungsvollen KI. Weitere Informationen zu Ethik, Vorurteilsvermeidung, Datenschutz und Rahmenwerken für verantwortungsvolle KI finden Sie in [Kapitel 5: Verantwortungsvolle Generative KI](../05-ResponsibleGenAI/README.md).

## Zusammenfassung

In diesem Kapitel haben wir LLM-Vervollständigungen und Chat-Flows untersucht, Funktionsaufrufe zur Erweiterung der KI-Fähigkeiten implementiert, ein Retrieval-Augmented Generation (RAG)-System erstellt und Sicherheitsmaßnahmen für verantwortungsvolle KI demonstriert.

> **HINWEIS**: Vertiefen Sie Ihr Wissen mit dem bereitgestellten [**Tutorial**](./TUTORIAL.md)

## Nächste Schritte

[Kapitel 4: Praktische Anwendungen & Projekte](../04-PracticalSamples/README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.