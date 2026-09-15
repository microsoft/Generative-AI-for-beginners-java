# Basis-Chat mit Azure AI Foundry – End-to-End-Beispiel

Dieses Beispiel ist eine einfache Spring Boot-Anwendung, die sich mit einem **Azure AI Foundry**-Modell über **schlüssellose Authentifizierung** (Microsoft Entra ID) verbindet und Ihre Einrichtung testet. Sie verwendet Spring AIs `ChatClient`, unterstützt durch das **offizielle OpenAI Java SDK** und den **Azure OpenAI v1** Endpunkt.

Die Versionen in [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) sind Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** und dotenv-java **3.2.0**. Das Beispiel nutzt `spring-ai-starter-model-openai` und deklariert explizit `openai-java` und `azure-identity`; Spring AI 2 hat den alten Azure OpenAI-Starter entfernt.

## Inhaltsverzeichnis

- [Voraussetzungen](#voraussetzungen)
- [Schnellstart](#schnellstart)
- [Wie Authentifizierung funktioniert](#wie-authentifizierung-funktioniert)
- [Anwendung ausführen](#anwendung-ausführen)
  - [Mit Maven](#mit-maven)
  - [Mit VS Code](#mit-vs-code)
  - [Erwartete Ausgabe](#erwartete-ausgabe)
- [Konfigurationsreferenz](#konfigurationsreferenz)
  - [Umgebungsvariablen](#umgebungsvariablen)
  - [Spring-Konfiguration](#spring-konfiguration)
- [Fehlerbehebung](#fehlerbehebung)
  - [Häufige Probleme](#häufige-probleme)
  - [Debug-Modus](#debug-modus)
- [Nächste Schritte](#nächste-schritte)
- [Ressourcen](#ressourcen)

## Voraussetzungen

Stellen Sie vor dem Ausführen dieses Beispiels sicher, dass Sie Folgendes haben:

- Eine Azure AI Foundry-Ressource mit einer `gpt-5.6-luna` Bereitstellung – stellen Sie diese mit `azd up` bereit oder manuell über die [Azure AI Foundry Einrichtungsanleitung](../../getting-started-azure-openai.md)
- Die Rolle **Cognitive Services OpenAI User** auf dieser Ressource (wird in den Bicep-Vorlagen für Sie zugewiesen)
- Die [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), angemeldet mit `az login`
- Java 21+ und Maven 3.9+

> **Kein API-Schlüssel erforderlich** — Authentifizierung ist schlüssellos über Microsoft Entra ID.

## Schnellstart

```bash
# 1. Zum Projekt navigieren
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Melden Sie sich an, damit die schlüssellose Authentifizierung ein Token erhalten kann
az login

# 3. Konfigurieren Sie den Endpunkt
#    - Wenn Sie `azd up` ausgeführt haben, wurde die .env für Sie geschrieben (überspringen).
#    - Andernfalls kopieren Sie die Vorlage und setzen Sie AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Führen Sie die Anwendung aus
mvn spring-boot:run
```

## Wie Authentifizierung funktioniert

Dieses Beispiel authentifiziert sich mit **Microsoft Entra ID** — es wird kein API-Schlüssel verwendet.

Die Anwendung konfiguriert die Authentifizierung explizit in [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` erstellt ein `BearerTokenCredential` mithilfe von `AuthenticationUtil.getBearerTokenSupplier` mit `DefaultAzureCredential` und dem Bereich `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` baut einen `OpenAIClient` mit `OpenAIOkHttpClient.builder()`, ermittelt den Ressourcenendpunkt zu `/openai/v1` und stellt das Bearer-Credential mit `.credential(...)` bereit.
3. `azureChatModel()` stellt diesen Client dem `OpenAiChatModel` von Spring AI zur Verfügung, der die `ChatClient`-Klasse dieser Anleitung unterstützt.

Diese expliziten Beans verhindern, dass ein globaler `OPENAI_API_KEY` die Azure-Authentifizierung überschreibt. Das Weglassen eines API-Schlüssels nur im YAML ist nicht die Authentifizierungskonfiguration. `DefaultAzureCredential` kann Ihre lokale `az login`-Sitzung oder eine verwaltete Identität in Azure nutzen; welche Identität ausgewählt wird, muss die oben aufgeführte Ressourcenrolle besitzen.

## Anwendung ausführen

### Mit Maven

```bash
mvn spring-boot:run
```

### Mit VS Code

1. Öffnen Sie das Projekt in VS Code
2. Drücken Sie `F5` oder nutzen Sie das "Ausführen und Debuggen"-Paneel
3. Wählen Sie die Konfiguration "Spring Boot-BasicChatApplication"

> **Hinweis**: Die Anwendung lädt `.env` aus dem Arbeitsverzeichnis, auch wenn sie von VS Code gestartet wird.

### Erwartete Ausgabe

Beispielhafte Ausgabe nach einem erfolgreichen Start (Startprotokolle ausgelassen; Formulierungen für Antwort können variieren):

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

## Konfigurationsreferenz

### Umgebungsvariablen

| Variable | Beschreibung | Erforderlich | Beispiel |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) Endpunkt-URL | Ja | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Name der Chatmodell-Bereitstellung | Nein | `gpt-5.6-luna` (Standard) |

> Es gibt **keine** API-Schlüsselvariable – die Authentifizierung erfolgt schlüssellos (Microsoft Entra ID via `az login`).

### Spring-Konfiguration

Die Einstellungen in [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) verwenden das Präfix `spring.ai.openai` und flache Chat-Eigenschaften (kein `options`-Block):

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

`model` ist der **Azure-Bereitstellungsname**. Die Authentifizierung erfolgt über die oben beschriebenen expliziten Beans, nicht über eine `api-key`-Einstellung. Das Beispiel deaktiviert Reasoning und begrenzt Completion Tokens auf 500; `temperature` und der legacy `max-tokens` sind nicht gesetzt.

Microsoft empfiehlt für neue Anwendungen das [offizielle OpenAI SDK mit Azure OpenAI v1 und der Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions bleibt für diese bestehende, berichtsbasierte Anleitung weiterhin unterstützt. Für GPT-5.6 müssen Anfragen, die Tools bei Chat Completions einschließen, `reasoning_effort` auf `none` setzen; bei Kombination von Reasoning und Tools verwenden Sie Responses. Siehe [Tool-Aufrufe mit Reasoning-Modellen](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Fehlerbehebung

### Häufige Probleme

<details>
<summary><strong>Fehler: 401 / "PermissionDenied" / Token-Fehler</strong></summary>

- Führen Sie `az login` aus – schlüssellose Authentifizierung benötigt eine aktive Anmeldung, um ein Token zu erhalten
- Stellen Sie sicher, dass Ihr Konto die Rolle **Cognitive Services OpenAI User** auf der Ressource besitzt
- Falls Sie die Rolle gerade zugewiesen haben, warten Sie eine Minute bis zur Übernahme
- Prüfen Sie, ob Sie sich im richtigen Mandanten/Abonnement befinden (`az account show`)
</details>

<details>
<summary><strong>Fehler: "The endpoint is not valid" / Verbindungsfehler</strong></summary>

- Stellen Sie sicher, dass `AZURE_OPENAI_ENDPOINT` die vollständige Basis-URL ist (z.B. `https://your-resource.openai.azure.com/`)
- Achten Sie auf einheitliche Verwendung eines abschließenden Schrägstrichs
- Überprüfen Sie, ob der Endpunkt mit Ihrer bereitgestellten Ressource übereinstimmt (`azd env get-values`)
</details>

<details>
<summary><strong>Fehler: "The deployment was not found"</strong></summary>

- Prüfen Sie, ob `AZURE_OPENAI_DEPLOYMENT` einem Bereitstellungsnamen in Azure entspricht
- Stellen Sie sicher, dass das Modell erfolgreich bereitgestellt und aktiv ist
- Der Standard-Bereitstellungsname ist `gpt-5.6-luna`
</details>

<details>
<summary><strong>Fehler: 429 / Ratenlimit überschritten</strong></summary>

- Die Standard-GPT-5.6 Luna-Bereitstellung hat Global Standard Kapazität 10: 10 Anfragen/Minute und 10.000 Tokens/Minute
- Führen Sie Beispiele sequenziell aus und warten Sie das Wiederholungsintervall des Dienstes ab, bevor Sie es erneut versuchen
- Dieses Basisbeispiel deaktiviert automatische SDK-Wiederholungen, sodass fehlgeschlagene Anfragen direkt gemeldet werden
</details>

<details>
<summary><strong>VS Code: Umgebungsvariablen werden nicht geladen</strong></summary>

- Stellen Sie sicher, dass Ihre `.env`-Datei im Projekt-Stammverzeichnis liegt (auf derselben Ebene wie `pom.xml`)
- Versuchen Sie, `mvn spring-boot:run` im integrierten Terminal von VS Code auszuführen
- Überprüfen Sie, ob die Java-Erweiterung in VS Code korrekt installiert ist
</details>

### Debug-Modus

Um detaillierte Protokolle zu aktivieren, stellen Sie diese Zeilen in [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) frei:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Nächste Schritte

**Einrichtung abgeschlossen!** Setzen Sie Ihre Lernreise fort:

[Kapitel 3: Kerntechniken der generativen KI](../../../03-CoreGenerativeAITechniques/README.md)

## Ressourcen

- [Spring AI 2 Übergang zum OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Offizielles OpenAI Java SDK mit Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Schlüssellose Authentifizierung mit Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portal](https://ai.azure.com/)
- [Azure AI Foundry Dokumentation](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->