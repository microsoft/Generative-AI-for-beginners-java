# Einrichten der Entwicklungsumgebung für Azure AI Foundry

> Diese Anleitung richtet **Azure AI Foundry**-Modelle für die Java KI-Apps in diesem Kurs mit **schlüsselloser** Authentifizierung (Microsoft Entra ID) ein — keine API-Schlüssel zum Verwalten. Neu bei den Tools? Beginnen Sie mit der [Anleitung zur Entwicklungsumgebung](./README.md).

Diese Anleitung richtet **Azure AI Foundry**-Modelle für die Java KI-Apps in diesem Kurs ein. Sie haben zwei Optionen:

- **Option A — Bereitstellung mit `azd` + Bicep (empfohlen):** ein Befehl stellt das Foundry-Konto und Modelle als Code bereit. Kein Klicken im Portal nötig.
- **Option B — Ressourcen manuell erstellen** im Azure AI Foundry-Portal.

Beide Wege nutzen **schlüssellose Authentifizierung** (Microsoft Entra ID) — es gibt keine API-Schlüssel, die kopiert oder offengelegt werden müssen.

## Inhaltsverzeichnis

- [Was erstellt wird](#was-erstellt-wird)
- [Voraussetzungen](#voraussetzungen)
- [Option A: Bereitstellung mit azd + Bicep (Empfohlen)](#option-a-provision-with-azd--bicep-recommended)
- [Option B: Ressourcen manuell erstellen](#option-b-ressourcen-manuell-erstellen)
- [Konfigurieren Sie Ihre Umgebung](#konfigurieren-sie-ihre-umgebung)
- [Testen Sie Ihre Einrichtung](#testen-sie-ihre-einrichtung)
- [Was kommt als Nächstes?](#was-kommt-als-nächstes)
- [Ressourcen](#ressourcen)
- [Zusätzliche Ressourcen](#zusätzliche-ressourcen)

## Was erstellt wird

Die Bicep-Vorlagen in [`infra/`](../../../02-SetupDevEnvironment/infra) stellen bereit:

- Ein **Azure AI Foundry**-Konto (`Microsoft.CognitiveServices/accounts`, Typ `AIServices`) mit einem Projekt
- Eine **Chat**-Bereitstellung - GPT-5.6 Luna (`gpt-5.6-luna`), Version `2026-07-09`, mit Kapazität `GlobalStandard` `10` (10 Anfragen/Minute und 10.000 Tokens/Minute für dieses Modell)
- Eine **Embedding**-Bereitstellung - `text-embedding-3-small`, Version `1` (wird in späteren Kapiteln verwendet)
- Eine **schlüssellose Rollenvergabe** (`Cognitive Services OpenAI User`), sodass Sie sich mit `az login` anmelden statt Schlüssel verwalten zu müssen

## Voraussetzungen

- Ein [Azure-Abonnement](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) und [Maven 3.9+](https://maven.apache.org/download.cgi)

## Option A: Bereitstellung mit azd + Bicep (Empfohlen)

Aus dem Ordner `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Anmelden (beide Tools)
azd auth login
az login

# Bereitstellen des Foundry-Kontos + Modellbereitstellungen
azd up
```

`azd` fragt nach einem **Umgebungsnamen** (zum Beispiel `genai-java`), **Abonnement** und **Region**. Wählen Sie Ihr eigenes Abonnement und eine Region, in der `gpt-5.6-luna` und `text-embedding-3-small` verfügbar sind, z.B. `eastus2`. Stellen Sie sicher, dass für Ihr Abonnement in dieser Region genügend Kontingente für Modell und Bereitstellungstyp vorhanden sind; Verfügbarkeit und Kontingente variieren je nach Abonnement.

Wenn die Bereitstellung abgeschlossen ist, führt azd aus:

1. Setzt alles um, was in [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) definiert ist.
2. Führt einen Post-Provisioning-Hook aus, der [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) mit Ihrem Endpunkt und den Bereitstellungsnamen schreibt (keine Geheimnisse).

> **Tipp:** Führen Sie `azd up` jederzeit erneut aus, um Änderungen anzuwenden. Mit `azd down` löschen Sie alles und vermeiden weitere Kosten.

Um die generierten Einstellungen einzusehen:

```bash
azd env get-values
```

Überspringen Sie jetzt zu [Testen Sie Ihre Einrichtung](#testen-sie-ihre-einrichtung).

## Option B: Ressourcen manuell erstellen

Möchten Sie lieber das Portal verwenden? Erstellen Sie die Ressourcen manuell:

1. Gehen Sie zum [Azure AI Foundry-Portal](https://ai.azure.com/) und melden Sie sich an.
2. **Erstellen Sie ein Projekt** (dadurch wird auch eine AI Foundry-Ressource erstellt). Geben Sie ihm einen Namen wie `GenAIJava`.
3. Öffnen Sie in Ihrem Projekt **Modelle + Endpunkte** → **Modell bereitstellen** → **Basis-Modell bereitstellen**.
4. Stellen Sie **GPT-5.6 Luna** bereit (Modell- und Bereitstellungsname `gpt-5.6-luna`, Version `2026-07-09`) mit Kapazität **Global Standard** `10`. Wiederholen Sie dies für **text-embedding-3-small**, Version `1`, wenn Sie die Embedding-Beispiele möchten.
5. Kopieren Sie aus **Übersicht** den **Endpunkt** (zum Beispiel `https://<resource>.openai.azure.com/`).
6. Gewähren Sie sich selbst schlüssellosen Zugriff: Öffnen Sie bei der Ressource **Zugriffssteuerung (IAM)** → **Rollen zuweisen** → weisen Sie Ihrem Konto die Rolle **Cognitive Services OpenAI User** zu.

> **Immer noch Probleme?** Schauen Sie in der [Azure AI Foundry-Dokumentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) nach.

## Konfigurieren Sie Ihre Umgebung

**Wenn Sie Option A (`azd up`) verwendet haben**, ist Ihre Einstellungsdatei bereits geschrieben — nichts weiter zu konfigurieren. Überspringen Sie zu [Testen Sie Ihre Einrichtung](#testen-sie-ihre-einrichtung).

**Wenn Sie Option B (manuell) verwendet haben**, erstellen Sie die `.env`-Datei des Beispiels selbst:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Bearbeiten Sie `.env` mit Ihrem Endpunkt (kein Schlüssel — Authentifizierung ist schlüssellos):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Verwenden Sie den Azure OpenAI-Endpunkt der Ressource, nicht eine Projekt-URL. Die Basic-Chat-App löst diesen auf `/openai/v1` auf und konfiguriert einen expliziten Bearer-Token-Client; ein API-Schlüssel wird nicht benötigt.

> **Sicherheitshinweis:** Es gibt keinen API-Schlüssel zu speichern. Die Authentifizierung erfolgt über Microsoft Entra ID mittels `az login` (lokal) oder einer verwalteten Identität (in Azure). Die `.env`-Datei enthält nur nicht geheime Einstellungen und ist bereits in `.gitignore` eingeschlossen.

## Testen Sie Ihre Einrichtung

Stellen Sie sicher, dass Sie angemeldet sind, damit die schlüssellose Authentifizierung ein Token abrufen kann, und führen Sie dann das Beispiel aus:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # wenn Sie nicht bereits angemeldet sind
mvn clean spring-boot:run
```

Sie sollten eine Antwort vom Modell `gpt-5.6-luna` sehen. Führen Sie die Beispiele nacheinander aus, um das kleine Standardkontingent nicht zu überschreiten; bei HTTP 429 warten Sie den angegebenen Retry-Intervall ab, bevor Sie es erneut versuchen.

> **VS Code-Nutzer:** Drücken Sie `F5`, um auszuführen. Die App lädt Ihre `.env` automatisch.

> **Vollständiges Beispiel:** Details und Fehlerbehebungen finden Sie im [Basic Chat mit Azure AI Foundry Beispiel](./examples/basic-chat-azure/README.md).

## Was kommt als Nächstes?

Nach der Bereitstellung und erfolgreichem Ausführen des Beispiels verfügen Sie über:
- Azure AI Foundry mit `gpt-5.6-luna` und `text-embedding-3-small` bereitgestellt
- Schlüssellose Authentifizierung (Microsoft Entra ID) — keine Schlüsselverwaltung
- Eine lokale `.env` mit Ihrem Endpunkt und den Bereitstellungsnamen
- Eine einsatzbereite Java-Entwicklungsumgebung

**Fahren Sie fort mit** [Kapitel 3: Kerntechniken der generativen KI](../03-CoreGenerativeAITechniques/README.md), um mit dem Erstellen von KI-Anwendungen zu beginnen!

## Ressourcen

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Schlüssellose Authentifizierung mit Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Dokumentation](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK Übergang](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Offizielles OpenAI Java SDK mit Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Zusätzliche Ressourcen

- [VS Code herunterladen](https://code.visualstudio.com/Download)
- [Docker Desktop herunterladen](https://www.docker.com/products/docker-desktop)
- [Dev Container Konfiguration](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->