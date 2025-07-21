<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2289320a74aeca1eb844cd7d3a7a9e12",
  "translation_date": "2025-07-21T15:20:22+00:00",
  "source_file": "02-SetupDevEnvironment/src/basic-chat-azure/README.md",
  "language_code": "de"
}
-->
# Grundlegender Chat mit Azure OpenAI - End-to-End-Beispiel

Dieses Beispiel zeigt, wie man eine einfache Spring-Boot-Anwendung erstellt, die sich mit Azure OpenAI verbindet und Ihre Einrichtung testet.

## Inhaltsverzeichnis

- [Voraussetzungen](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Schnellstart](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Konfigurationsoptionen](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Option 1: Umgebungsvariablen (.env-Datei) - Empfohlen](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Option 2: GitHub Codespace Secrets](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Anwendung ausführen](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Mit Maven](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Mit VS Code](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Erwartete Ausgabe](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Konfigurationsreferenz](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Umgebungsvariablen](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Spring-Konfiguration](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Fehlerbehebung](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Häufige Probleme](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
  - [Debug-Modus](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Nächste Schritte](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)
- [Ressourcen](../../../../../02-SetupDevEnvironment/src/basic-chat-azure)

## Voraussetzungen

Bevor Sie dieses Beispiel ausführen, stellen Sie sicher, dass Sie Folgendes erledigt haben:

- Den [Azure OpenAI Einrichtungsleitfaden](../../getting-started-azure-openai.md) abgeschlossen haben  
- Eine Azure OpenAI-Ressource bereitgestellt haben (über das Azure AI Foundry-Portal)  
- Das Modell gpt-4o-mini (oder eine Alternative) bereitgestellt haben  
- Einen API-Schlüssel und die Endpunkt-URL von Azure erhalten haben  

## Schnellstart

```bash
# 1. Navigate to project
cd 02-SetupDevEnvironment/src/basic-chat-azure

# 2. Configure credentials
cp .env.example .env
# Edit .env with your Azure OpenAI credentials

# 3. Run the application
mvn spring-boot:run
```

## Konfigurationsoptionen

### Option 1: Umgebungsvariablen (.env-Datei) - Empfohlen

**Schritt 1: Erstellen Sie Ihre Konfigurationsdatei**  
```bash
cp .env.example .env
```

**Schritt 2: Fügen Sie Ihre Azure OpenAI-Zugangsdaten hinzu**  
```bash
# Your Azure OpenAI API key (from Azure AI Foundry portal)
AZURE_AI_KEY=your-actual-api-key-here

# Your Azure OpenAI endpoint URL (e.g., https://your-hub-name.openai.azure.com/)
AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
```

> **Sicherheitshinweis**:  
> - Committen Sie Ihre `.env`-Datei niemals in die Versionskontrolle  
> - Die `.env`-Datei ist bereits in `.gitignore` enthalten  
> - Halten Sie Ihre API-Schlüssel sicher und rotieren Sie diese regelmäßig  

### Option 2: GitHub Codespace Secrets

Für GitHub Codespaces legen Sie diese Secrets in Ihrem Repository fest:  
- `AZURE_AI_KEY` - Ihr Azure OpenAI API-Schlüssel  
- `AZURE_AI_ENDPOINT` - Ihre Azure OpenAI Endpunkt-URL  

Die Anwendung erkennt und verwendet diese Secrets automatisch.

### Alternative: Direkte Umgebungsvariablen

<details>
<summary>Plattform-spezifische Befehle anzeigen</summary>

**Linux/macOS (bash/zsh):**  
```bash
export AZURE_AI_KEY=your-actual-api-key-here
export AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
```

**Windows (Command Prompt):**  
```cmd
set AZURE_AI_KEY=your-actual-api-key-here
set AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
```

**Windows (PowerShell):**  
```powershell
$env:AZURE_AI_KEY="your-actual-api-key-here"
$env:AZURE_AI_ENDPOINT="https://your-hub-name.openai.azure.com/"
```
</details>

## Anwendung ausführen

### Mit Maven

```bash
mvn spring-boot:run
```

### Mit VS Code

1. Öffnen Sie das Projekt in VS Code  
2. Drücken Sie `F5` oder verwenden Sie das "Run and Debug"-Panel  
3. Wählen Sie die Konfiguration "Spring Boot-BasicChatApplication"  

> **Hinweis**: Die VS Code-Konfiguration lädt automatisch Ihre `.env`-Datei  

### Erwartete Ausgabe

```
Starting Basic Chat with Azure OpenAI...
Environment variables loaded successfully
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Konfigurationsreferenz

### Umgebungsvariablen

| Variable | Beschreibung | Erforderlich | Beispiel |
|----------|--------------|--------------|----------|
| `AZURE_AI_KEY` | Azure OpenAI API-Schlüssel | Ja | `abc123...` |
| `AZURE_AI_ENDPOINT` | Azure OpenAI Endpunkt-URL | Ja | `https://my-hub.openai.azure.com/` |
| `AZURE_AI_MODEL_DEPLOYMENT` | Modellbereitstellungsname | Nein | `gpt-4o-mini` (Standard) |

### Spring-Konfiguration

Die Datei `application.yml` konfiguriert:  
- **API-Schlüssel**: `${AZURE_AI_KEY}` - Aus der Umgebungsvariablen  
- **Endpunkt**: `${AZURE_AI_ENDPOINT}` - Aus der Umgebungsvariablen  
- **Modell**: `${AZURE_AI_MODEL_DEPLOYMENT:gpt-4o-mini}` - Aus der Umgebungsvariablen mit Fallback  
- **Temperatur**: `0.7` - Steuert die Kreativität (0.0 = deterministisch, 1.0 = kreativ)  
- **Maximale Tokens**: `500` - Maximale Antwortlänge  

## Fehlerbehebung

### Häufige Probleme

<details>
<summary><strong>Fehler: "Der API-Schlüssel ist ungültig"</strong></summary>

- Überprüfen Sie, ob Ihr `AZURE_AI_KEY` korrekt in Ihrer `.env`-Datei gesetzt ist  
- Stellen Sie sicher, dass der API-Schlüssel exakt aus dem Azure AI Foundry-Portal kopiert wurde  
- Achten Sie darauf, dass keine zusätzlichen Leerzeichen oder Anführungszeichen um den Schlüssel vorhanden sind  
</details>

<details>
<summary><strong>Fehler: "Der Endpunkt ist ungültig"</strong></summary>

- Stellen Sie sicher, dass Ihre `AZURE_AI_ENDPOINT` die vollständige URL enthält (z. B. `https://your-hub-name.openai.azure.com/`)  
- Überprüfen Sie die Konsistenz des abschließenden Schrägstrichs  
- Vergewissern Sie sich, dass der Endpunkt mit Ihrer Azure-Bereitstellungsregion übereinstimmt  
</details>

<details>
<summary><strong>Fehler: "Die Bereitstellung wurde nicht gefunden"</strong></summary>

- Überprüfen Sie, ob der Modellbereitstellungsname exakt mit dem in Azure bereitgestellten übereinstimmt  
- Stellen Sie sicher, dass das Modell erfolgreich bereitgestellt und aktiv ist  
- Versuchen Sie, den Standardbereitstellungsnamen zu verwenden: `gpt-4o-mini`  
</details>

<details>
<summary><strong>VS Code: Umgebungsvariablen werden nicht geladen</strong></summary>

- Stellen Sie sicher, dass sich Ihre `.env`-Datei im Projektstammverzeichnis befindet (auf derselben Ebene wie `pom.xml`)  
- Versuchen Sie, `mvn spring-boot:run` im integrierten Terminal von VS Code auszuführen  
- Überprüfen Sie, ob die Java-Erweiterung in VS Code ordnungsgemäß installiert ist  
- Vergewissern Sie sich, dass die Startkonfiguration `"envFile": "${workspaceFolder}/.env"` enthält  
</details>

### Debug-Modus

Um detaillierte Protokolle zu aktivieren, kommentieren Sie diese Zeilen in `application.yml` aus:  

```yaml
logging:
  level:
    org.springframework.ai: DEBUG
    com.azure: DEBUG
```

## Nächste Schritte

**Einrichtung abgeschlossen!** Setzen Sie Ihre Lernreise fort:  

[Kapitel 3: Kerntechniken der generativen KI](../../../03-CoreGenerativeAITechniques/README.md)

## Ressourcen

- [Spring AI Azure OpenAI Dokumentation](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)  
- [Azure OpenAI Service Dokumentation](https://learn.microsoft.com/azure/ai-services/openai/)  
- [Azure AI Foundry Portal](https://ai.azure.com/)  
- [Azure AI Foundry Dokumentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)  

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.