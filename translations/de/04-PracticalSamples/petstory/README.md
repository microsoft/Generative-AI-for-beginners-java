<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "69dffd84127360d3f9446b89de471abe",
  "translation_date": "2025-07-21T15:22:35+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "de"
}
-->
# Haustiergeschichten-App

>**Hinweis**: Dieses Kapitel enthält ein [**Tutorial**](./TUTORIAL.md), das Sie durch das Ausführen der fertigen Beispiele führt.

Eine Spring Boot-Webanwendung, die KI-gestützte Beschreibungen und Geschichten für hochgeladene Haustierbilder mithilfe von GitHub-Modellen erstellt.

## Inhaltsverzeichnis

- [Technologie-Stack](../../../../04-PracticalSamples/petstory)
- [Voraussetzungen](../../../../04-PracticalSamples/petstory)
- [Einrichtung & Installation](../../../../04-PracticalSamples/petstory)
- [Verwendung](../../../../04-PracticalSamples/petstory)

## Technologie-Stack

- **Backend**: Spring Boot 3.5.3, Java 21
- **KI-Integration**: OpenAI Java SDK mit GitHub-Modellen
- **Sicherheit**: Spring Security
- **Frontend**: Thymeleaf-Templates mit Bootstrap-Styling
- **Build-Tool**: Maven
- **KI-Modelle**: GitHub-Modelle

## Voraussetzungen

- Java 21 oder höher
- Maven 3.9+
- GitHub Personal Access Token mit `models:read`-Berechtigung

## Einrichtung & Installation

### 1. Wechseln Sie in das Verzeichnis der petstory-Anwendung
```bash
cd Generative-AI-for-beginners-java/04-PracticalSamples/petstory
```

### 2. Setzen Sie die Umgebungsvariable
   ```bash
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_github_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_github_token_here"
   
   # Linux/macOS
   export GITHUB_TOKEN=your_github_token_here
   ```

### 3. Bauen Sie die Anwendung
```bash
mvn clean compile
```

### 4. Starten Sie die Anwendung
```bash
mvn spring-boot:run
```

## Verwendung

1. **Zugriff auf die Anwendung**: Navigieren Sie zu `http://localhost:8080`
2. **Bild hochladen**: Klicken Sie auf "Datei auswählen" und wählen Sie ein Haustierbild aus
3. **Bild analysieren**: Klicken Sie auf "Bild analysieren", um eine KI-Beschreibung zu erhalten
4. **Geschichte generieren**: Klicken Sie auf "Geschichte generieren", um die Geschichte zu erstellen

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.