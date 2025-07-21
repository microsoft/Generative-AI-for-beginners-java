<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0b563ac59362fb83f0f49dcfc442dd97",
  "translation_date": "2025-07-21T15:32:25+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "de"
}
-->
# Einrichten der Entwicklungsumgebung für Generative KI mit Java

> **Schnellstart**: Code in der Cloud in 2 Minuten – Springe zu [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) – keine lokale Installation erforderlich und nutzt GitHub-Modelle!

> **Interessiert an Azure OpenAI?** Siehe unseren [Azure OpenAI Setup Guide](getting-started-azure-openai.md) mit Schritten zur Erstellung einer neuen Azure OpenAI-Ressource.

## Was Sie lernen werden

- Einrichtung einer Java-Entwicklungsumgebung für KI-Anwendungen
- Auswahl und Konfiguration Ihrer bevorzugten Entwicklungsumgebung (Cloud-basiert mit Codespaces, lokaler Dev-Container oder vollständige lokale Installation)
- Testen Ihrer Einrichtung durch Verbindung zu GitHub-Modellen

## Inhaltsverzeichnis

- [Was Sie lernen werden](../../../02-SetupDevEnvironment)
- [Einleitung](../../../02-SetupDevEnvironment)
- [Schritt 1: Einrichten Ihrer Entwicklungsumgebung](../../../02-SetupDevEnvironment)
  - [Option A: GitHub Codespaces (Empfohlen)](../../../02-SetupDevEnvironment)
  - [Option B: Lokaler Dev-Container](../../../02-SetupDevEnvironment)
  - [Option C: Nutzung Ihrer bestehenden lokalen Installation](../../../02-SetupDevEnvironment)
- [Schritt 2: Erstellen eines GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Schritt 3: Testen Ihrer Einrichtung](../../../02-SetupDevEnvironment)
- [Fehlerbehebung](../../../02-SetupDevEnvironment)
- [Zusammenfassung](../../../02-SetupDevEnvironment)
- [Nächste Schritte](../../../02-SetupDevEnvironment)

## Einleitung

Dieses Kapitel führt Sie durch die Einrichtung einer Entwicklungsumgebung. Wir verwenden **GitHub-Modelle** als primäres Beispiel, da sie kostenlos sind, einfach mit einem GitHub-Konto eingerichtet werden können, keine Kreditkarte erfordern und Zugang zu mehreren Modellen für Experimente bieten.

**Keine lokale Einrichtung erforderlich!** Sie können sofort mit dem Programmieren beginnen, indem Sie GitHub Codespaces verwenden, das eine vollständige Entwicklungsumgebung in Ihrem Browser bereitstellt.

<img src="./images/models.webp" alt="Screenshot: GitHub-Modelle" width="50%">

Wir empfehlen die Verwendung von [**GitHub-Modelle**](https://github.com/marketplace?type=models) für diesen Kurs, da sie:
- **Kostenlos** für den Einstieg sind
- **Einfach** mit nur einem GitHub-Konto eingerichtet werden können
- **Keine Kreditkarte** erfordern
- **Mehrere Modelle** für Experimente bieten

> **Hinweis**: Die in diesem Training verwendeten GitHub-Modelle haben folgende kostenlose Limits:
> - 15 Anfragen pro Minute (150 pro Tag)
> - ~8.000 Wörter Eingabe, ~4.000 Wörter Ausgabe pro Anfrage
> - 5 gleichzeitige Anfragen
> 
> Für den produktiven Einsatz können Sie auf Azure AI Foundry-Modelle mit Ihrem Azure-Konto upgraden. Ihr Code muss nicht geändert werden. Siehe die [Azure AI Foundry-Dokumentation](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Schritt 1: Einrichten Ihrer Entwicklungsumgebung

<a name="quick-start-cloud"></a>

Wir haben einen vorkonfigurierten Entwicklungscontainer erstellt, um die Einrichtungszeit zu minimieren und sicherzustellen, dass Sie alle notwendigen Tools für diesen Generative KI mit Java-Kurs haben. Wählen Sie Ihren bevorzugten Entwicklungsansatz:

### Optionen für die Einrichtung der Umgebung:

#### Option A: GitHub Codespaces (Empfohlen)

**Beginnen Sie in 2 Minuten mit dem Programmieren – keine lokale Einrichtung erforderlich!**

1. Forken Sie dieses Repository in Ihr GitHub-Konto
   > **Hinweis**: Wenn Sie die grundlegende Konfiguration bearbeiten möchten, werfen Sie einen Blick auf die [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Klicken Sie auf **Code** → **Codespaces**-Tab → **...** → **Neu mit Optionen...**
3. Verwenden Sie die Standardwerte – dies wählt die **Dev-Container-Konfiguration**: **Generative AI Java Development Environment**, ein benutzerdefinierter Devcontainer, der für diesen Kurs erstellt wurde
4. Klicken Sie auf **Codespace erstellen**
5. Warten Sie ~2 Minuten, bis die Umgebung bereit ist
6. Fahren Sie fort mit [Schritt 2: GitHub-Token erstellen](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Screenshot: Codespaces-Untermenü" width="50%">

<img src="./images/image.png" alt="Screenshot: Neu mit Optionen" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Codespace-Optionen erstellen" width="50%">


> **Vorteile von Codespaces**:
> - Keine lokale Installation erforderlich
> - Funktioniert auf jedem Gerät mit einem Browser
> - Vorkonfiguriert mit allen Tools und Abhängigkeiten
> - Kostenlos 60 Stunden pro Monat für persönliche Konten
> - Konsistente Umgebung für alle Lernenden

#### Option B: Lokaler Dev-Container

**Für Entwickler, die lokale Entwicklung mit Docker bevorzugen**

1. Forken und klonen Sie dieses Repository auf Ihren lokalen Rechner
   > **Hinweis**: Wenn Sie die grundlegende Konfiguration bearbeiten möchten, werfen Sie einen Blick auf die [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Installieren Sie [Docker Desktop](https://www.docker.com/products/docker-desktop/) und [VS Code](https://code.visualstudio.com/)
3. Installieren Sie die [Dev Containers-Erweiterung](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) in VS Code
4. Öffnen Sie den Repository-Ordner in VS Code
5. Wenn Sie dazu aufgefordert werden, klicken Sie auf **Im Container erneut öffnen** (oder verwenden Sie `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Warten Sie, bis der Container erstellt und gestartet ist
7. Fahren Sie fort mit [Schritt 2: GitHub-Token erstellen](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Screenshot: Dev-Container-Einrichtung" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev-Container-Erstellung abgeschlossen" width="50%">

#### Option C: Nutzung Ihrer bestehenden lokalen Installation

**Für Entwickler mit bestehenden Java-Umgebungen**

Voraussetzungen:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) oder Ihre bevorzugte IDE

Schritte:
1. Klonen Sie dieses Repository auf Ihren lokalen Rechner
2. Öffnen Sie das Projekt in Ihrer IDE
3. Fahren Sie fort mit [Schritt 2: GitHub-Token erstellen](../../../02-SetupDevEnvironment)

> **Profi-Tipp**: Wenn Sie einen leistungsschwachen Rechner haben, aber VS Code lokal nutzen möchten, verwenden Sie GitHub Codespaces! Sie können Ihren lokalen VS Code mit einem cloudbasierten Codespace verbinden und so das Beste aus beiden Welten nutzen.

<img src="./images/image-2.png" alt="Screenshot: Lokale Devcontainer-Instanz erstellt" width="50%">


## Schritt 2: Erstellen eines GitHub Personal Access Token

1. Navigieren Sie zu [GitHub Einstellungen](https://github.com/settings/profile) und wählen Sie **Einstellungen** aus Ihrem Profilmenü.
2. Klicken Sie in der linken Seitenleiste auf **Entwicklereinstellungen** (normalerweise ganz unten).
3. Unter **Personal access tokens** klicken Sie auf **Fine-grained tokens** (oder folgen Sie diesem direkten [Link](https://github.com/settings/personal-access-tokens)).
4. Klicken Sie auf **Neues Token generieren**.
5. Geben Sie unter "Token-Name" einen beschreibenden Namen ein (z. B. `GenAI-Java-Course-Token`).
6. Legen Sie ein Ablaufdatum fest (empfohlen: 7 Tage für Sicherheitsbest-Practices).
7. Wählen Sie unter "Ressourcenbesitzer" Ihr Benutzerkonto aus.
8. Wählen Sie unter "Repository-Zugriff" die Repositorys aus, die Sie mit GitHub-Modellen verwenden möchten (oder "Alle Repositorys", falls erforderlich).
9. Unter "Repository-Berechtigungen" finden Sie **Modelle** und setzen Sie diese auf **Lesen und Schreiben**.
10. Klicken Sie auf **Token generieren**.
11. **Kopieren und speichern Sie Ihr Token jetzt** – Sie werden es später nicht mehr sehen!

> **Sicherheitstipp**: Verwenden Sie den minimal erforderlichen Umfang und die kürzest mögliche Ablaufzeit für Ihre Zugriffstokens.

## Schritt 3: Testen Ihrer Einrichtung mit dem GitHub Models Beispiel

Sobald Ihre Entwicklungsumgebung bereit ist, testen wir die GitHub Models-Integration mit unserer Beispielanwendung in [`02-SetupDevEnvironment/src/github-models`](../../../02-SetupDevEnvironment/src/github-models).

1. Öffnen Sie das Terminal in Ihrer Entwicklungsumgebung.
2. Navigieren Sie zum GitHub Models-Beispiel:
   ```bash
   cd 02-SetupDevEnvironment/src/github-models
   ```
3. Setzen Sie Ihr GitHub-Token als Umgebungsvariable:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Führen Sie die Anwendung aus:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Sie sollten eine Ausgabe ähnlich wie diese sehen:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Das Beispielcode verstehen

Lassen Sie uns zunächst verstehen, was wir ausführen werden. Das Beispiel verwendet das OpenAI Java SDK, um eine Verbindung zu GitHub-Modellen herzustellen:

**Was dieser Code macht:**
- **Verbindet** sich mit GitHub-Modellen unter Verwendung Ihres Personal Access Token
- **Sendet** eine einfache Nachricht "Say Hello World!" an das KI-Modell
- **Empfängt** und zeigt die Antwort der KI an
- **Validiert**, dass Ihre Einrichtung korrekt funktioniert

**Wichtige Abhängigkeit** (in `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Hauptcode** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Zusammenfassung

**Herzlichen Glückwunsch!** Sie haben erfolgreich:

- **Ein GitHub Personal Access Token erstellt** mit den richtigen Berechtigungen für den Zugriff auf KI-Modelle
- **Ihre Java-Entwicklungsumgebung eingerichtet** mit Codespaces, Dev-Containern oder lokaler Installation
- **Eine Verbindung zu GitHub-Modellen hergestellt** unter Verwendung des OpenAI Java SDK für kostenlosen Zugang zur KI-Entwicklung
- **Die Integration getestet** mit einer funktionierenden Beispielanwendung, die mit KI-Modellen kommuniziert

## Nächste Schritte

[Kapitel 3: Kerntechniken der Generativen KI](../03-CoreGenerativeAITechniques/README.md)

## Fehlerbehebung

Haben Sie Probleme? Hier sind häufige Probleme und Lösungen:

- **Token funktioniert nicht?** 
  - Stellen Sie sicher, dass Sie das gesamte Token ohne zusätzliche Leerzeichen kopiert haben
  - Überprüfen Sie, ob das Token korrekt als Umgebungsvariable gesetzt ist
  - Vergewissern Sie sich, dass Ihr Token die richtigen Berechtigungen hat (Modelle: Lesen und Schreiben)

- **Maven nicht gefunden?** 
  - Wenn Sie Dev-Container/Codespaces verwenden, sollte Maven vorinstalliert sein
  - Für die lokale Einrichtung stellen Sie sicher, dass Java 21+ und Maven 3.9+ installiert sind
  - Versuchen Sie `mvn --version`, um die Installation zu überprüfen

- **Verbindungsprobleme?** 
  - Überprüfen Sie Ihre Internetverbindung
  - Vergewissern Sie sich, dass GitHub von Ihrem Netzwerk aus zugänglich ist
  - Stellen Sie sicher, dass Sie sich nicht hinter einer Firewall befinden, die den GitHub Models-Endpunkt blockiert

- **Dev-Container startet nicht?** 
  - Stellen Sie sicher, dass Docker Desktop läuft (für lokale Entwicklung)
  - Versuchen Sie, den Container neu zu erstellen: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Anwendungskompilierungsfehler?**
  - Stellen Sie sicher, dass Sie sich im richtigen Verzeichnis befinden: `02-SetupDevEnvironment/src/github-models`
  - Versuchen Sie, das Projekt zu bereinigen und neu zu erstellen: `mvn clean compile`

> **Brauchen Sie Hilfe?**: Haben Sie weiterhin Probleme? Öffnen Sie ein Issue im Repository und wir helfen Ihnen weiter.

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.