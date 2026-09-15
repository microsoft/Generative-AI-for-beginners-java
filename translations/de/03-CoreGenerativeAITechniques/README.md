# Kerntechniken zur Generativen KI - Tutorial

## Inhaltsverzeichnis

- [Voraussetzungen](#voraussetzungen)
- [Erste Schritte](#erste-schritte)
- [Modellauswahl-Leitfaden](#modellauswahl-leitfaden)
- [Tutorial 1: LLM-Vervollständigungen und Chat](#tutorial-1-llm-vervollständigungen-und-chat)
- [Tutorial 2: Funktionsaufruf](#tutorial-2-funktionsaufruf)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: Verantwortliche KI](#tutorial-4-verantwortliche-ki)
- [Gemeinsame Muster in den Beispielen](#gemeinsame-muster-in-den-beispielen)
- [Unit-Tests](#unit-tests)
- [Sequenzielle Live-Verifikation](#sequenzielle-live-verifikation)
- [Fehlerbehebung](#fehlerbehebung)
- [Nächste Schritte](#nächste-schritte)

## Überblick

Vier eigenständige Java-Programme demonstrieren Chat, Gesprächsverlauf, Funktionsaufrufe, dokumentenbasierte Retrieval-Augmented Generation (RAG) und verantwortungsbewusste KI-Antwortverarbeitung. Alle Chat-Anfragen richten sich standardmäßig an **GPT-5.6 Luna mit der Reasoning-Einstellung `none`**.

Diese Beispiele verwenden das offizielle OpenAI Java SDK mit dem Azure OpenAI v1-Endpunkt, entsprechend der [Microsoft SDK-Anleitung](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Das ältere Paket `azure-ai-openai` ist keine Abhängigkeit mehr. Chat Completions wird beibehalten, um die bisherigen nachrichtenbasierten Arbeitsabläufe zu demonstrieren; siehe das [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) für andere API-Optionen.

## Voraussetzungen

- Java 21 oder höher und Maven 3.6.3 oder höher.
- Eine Azure OpenAI Chat-Bereitstellung namens `gpt-5.6-luna` oder eine Kompatibilitätsüberschreibung mit passenden Chat Completions Einstellungen.
- Eine angemeldete Azure-Identität mit der Rolle **Cognitive Services OpenAI User** für die Ressource. Lokale Entwicklung verwendet Ihre Azure CLI-Anmeldung; gehostete Anwendungen können Managed Identity verwenden.
- Siehe [Kapitel 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) für Ressourcen-Einrichtung und Anmeldeanweisungen.

Die [Maven-Konfiguration](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fixiert diese Versionen, geprüft am 14.09.2026:

| Komponente | Version | Zweck |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Offizieller Azure v1-kompatibler Client |
| `com.azure:azure-identity` | 1.18.6 | Schlüssel-lose Authentifizierung und Token-Erneuerung |
| `net.objecthunter:exp4j` | 0.4.8 | Arithmetische Ausdrucksauswertung ohne Codeausführung |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter Unit-Tests |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 Kompilierung, Tests, ausführbare Beispiele |

Der Compiler verwendet `--release 21`. Keine Abhängigkeit zu Spring Boot, Spring AI oder LangChain4j ist für diese eigenständigen Beispiele erforderlich.

## Erste Schritte

Setzen Sie vom Repository-Stamm aus die Ressourcen-Endpoint-URL und optional eine Bereitstellungsüberschreibung in Ihrer Shell.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Tests benötigen weder Azure-Zugangsdaten noch einen Endpoint. Maven liest nicht automatisch eine Umgebungsdatei ein; setzen Sie Variablen in der Shell, von der aus Sie die Live-Beispiele starten. Bei IDE-Starts prüfen Sie die bereitgestellte Umgebung Ihrer Startkonfiguration.

## Modellauswahl-Leitfaden

| Umgebungsvariable | Bedeutung | Standard |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure-Ressourcenstamm oder bereits normalisierte `/openai/v1` URL | Erforderlich für Live-Ausführungen |
| `AZURE_OPENAI_DEPLOYMENT` | Chat-Bereitstellungsname, kein Modellversionenname | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Separate Embedding-Bereitstellungskonfiguration, wird von diesen vier Programmen nicht verwendet | `text-embedding-3-small` |

Leere Bereitstellungsüberschreibungen verwenden die Standardwerte. Die Konfiguration hängt genau ein Mal `/openai/v1` an und lehnt Credentials, Query-Strings und Legacy-Bereitstellungspfade im Endpoint ab.

Jede Chat-Anfrage setzt explizit `reasoningEffort(ReasoningEffort.NONE)` und `maxCompletionTokens(...)`. Keine Anfrage setzt `temperature`, `top_p` oder die legacy Option für Completion Tokens. Dies gilt auch für Werkzeugauswahl- und Werkzeugergebnis-Follow-ups. GPT-5.6 Chat Completions Funktions-Tools erfordern Reasoning effort `none`; siehe [Microsoft Chat-Anleitung](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**In diesem Kapitel gibt es keinen Streaming- oder Embedding-Einstiegspunkt.** Der Leser ruft das gesamte Dokument ab, nicht Vektoren. Wenn Sie das mit Embeddings erweitern, verwenden Sie eine separate Embedding-Bereitstellung wie `text-embedding-3-small`, niemals Luna.

## Tutorial 1: LLM-Vervollständigungen und Chat

Quelle: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Das Programm führt eine einfache Erklärung zu Java Streams durch, ein zweistufiges HashMap/TreeMap-Gespräch und einen interaktiven Chat. Die zweite Runde enthält die erste Assistenten-Antwort; jede interaktive Runde sendet auch den vorherigen Gesprächsverlauf.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` liefert die Bereitstellungs- und explizite Reasoning-Einstellungen. Interaktiver Chat überspringt leere Zeilen, endet bei `exit` oder EOF und behält die Systemnachricht plus neun abgeschlossene Benutzer-/Assistenten-Turns. Die Begrenzung der Turn-Anzahl dient zu Lehrzwecken, keine exakte Token-Budgetgarantie.

Aus dem examples-Verzeichnis:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Erwarten Sie drei erste Antworten, danach eine Eingabeaufforderung `You:`. Jede nichtleere interaktive Frage erzeugt eine Anfrage. Die Vervollständigungsgrenze liegt bei 200, 300, 400 und dann 500 Token pro interaktivem Turn.

## Tutorial 2: Funktionsaufruf

Quelle: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

Das SDK leitet JSON-Schemas von den annotierten Records `WeatherArguments` und `CalculationArguments` ab. Eine erforderliche Werkzeugauswahl lässt jedes Beispiel das Werkzeugprotokoll üben, anstatt eine unbeeinflusste Antwort des Modells hinzunehmen.

1. Senden Sie eine Frage mit dem erlaubten Tool, Reasoning effort `none` und einer 300-Token-Vervollständigungsgrenze.
2. Fordern Sie einen `tool_calls` Finish-Grund, validieren Sie Funktionsnamen und Aufruf-IDs und parsen Sie typisierte JSON-Argumente.
3. Führen Sie die lokale Funktion aus. Das Modell führt keinen Java- oder beliebigen Code aus.
4. Fügen Sie die Assistenten-Tool-Call-Nachricht einmal hinzu, gefolgt von jedem Ergebnis mit passender `tool_call_id`.
5. Senden Sie eine abschließende 300-Token-Anfrage ohne Werkzeuge und fordern Sie eine vollständige, nichtleere Antwort.

`get_weather` liefert **simuliertes**, kein Live-Wetter. Es beachtet die Stadt und wandelt die Beispieltemperatur 22 Grad Celsius bei Bedarf in Fahrenheit um. `calculate` wertet den Ausdruck über exp4j aus, unterstützt Formen wie `15% von 240` und `2 + 3 * 4` und lehnt leere, zu große, ungültige oder nicht endliche Berechnungen ab. Es verwendet Fließkommaarithmetik, keine finanzielle Dezimalgenauigkeit.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Erwarten Sie `Function: get_weather`, simuliertes Wetter für Seattle, `Function: calculate`, `Function result: 36` und die zwei abschließenden Antworten. Keine stdin oder externe Wetter-Zugangsdaten sind erforderlich. Ein erfolgreicher Lauf verwendet exakt vier Chat-Anfragen.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Quelle: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Eingabe: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Dieses einführende RAG-Beispiel ruft ein gesamtes UTF-8-Dokument ab und bindet es in die Benutzeranfrage mit der Frage ein. Eine separate Systemnachricht weist das Modell an, die Dokumentinhalte als unzuverlässige Daten zu behandeln und nur auf dieser Grundlage zu antworten. Wenn das Dokument die Antwort nicht enthält, lautet die angeforderte Antwort: `Ich kann diese Information im bereitgestellten Dokument nicht finden.`

Grounding kann Halluzinationen reduzieren, aber weder Begrenzungen noch Systemanweisungen garantieren Genauigkeit oder verhindern alle Prompt-Injektionen. Überprüfen Sie Live-Antworten. Produktions-RAG umfasst normalerweise Segmentierung, Abruf, Zitate, Zugriffskontrolle und Evaluation.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Geben Sie eine Frage ein, z.B. `Welche Authentifizierungsmethode beschreibt das Dokument?`. Erwarten Sie eine Antwort mit Microsoft Entra ID. Das Programm beendet sich nach einer Chat-Anfrage mit 500-Token-Vervollständigungsgrenze.

Die Standarddateisuche funktioniert vom Repository-Stamm, Kapitel- oder Beispielverzeichnis aus. Ein expliziter Pfad wird auch unterstützt:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Eingaben müssen nicht leer sein: max. 32 KiB UTF-8-Dokumentdaten und 2.000 Fragezeichen. Fehlende Dateien, leere/EOF-Fragen und zu große Eingaben schlagen vor der Inferenzen fehl.

## Tutorial 4: Verantwortliche KI

Quelle: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Die sechs Prüfungspunkte decken schädliche Anweisungen, Hassrede, Datenschutz, medizinische Fehlinformationen, illegale Inhalte und eine harmlose verantwortungsbewusste KI-Frage ab. Das Programm beobachtet die Reaktion, ohne jede Prüfung als Filterimpuls vorauszusetzen.

| Ergebnis | Nachweis |
| --- | --- |
| `GEFILTERT` | Ein expliziter `content_filter` / `ResponsibleAIPolicyViolation` Fehlercode oder ein Completion-`content_filter` Endgrund |
| `ABGELEHNT` | Ein nichtleeres strukturiertes `message.refusal` Feld |
| `MÖGLICHE_ABLEHNUNG` | Eine einleitende Ablehnungsphrase im normalen Text; eine Heuristik, die Überprüfung erfordert |
| `GENERIERTER` | Eine abgeschlossene nichtleere Antwort; kein Beleg dafür, dass der Inhalt sicher ist |

Ein gewöhnlicher HTTP 400 ist **kein** Beweis für Filterung. Ungültige Parameter, Authentifizierungsfehler, Ratenbegrenzungen, Serverfehler, fehlerhafte Antworten und abgeschnittene Ausgabe führen zum Fehlschlagen des Laufs anstelle eines falschen Sicherheitserfolgs. Breite Begriffe wie „schädlicher Inhalt“ in einer harmlosen Erklärung zählen nicht als Ablehnung.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Erwarten Sie sechs Kategorieresultate und eine Zusammenfassung, dass die Beobachtungen keine Sicherheitszertifizierung sind. Jeder Prüfvorgang hat ein Vervollständigungs-Limit von 300 Token. Überprüfen Sie unerwartete Generierungen und mögliche Ablehnungen manuell; der harmlose Vergleich sollte eine inhaltliche verantwortungsbewusste KI-Erklärung liefern. Kein stdin erforderlich.

## Gemeinsame Muster in den Beispielen

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) zentralisiert Endpoint-Normalisierung, Bereitstellungsüberschreibungen, schlüssel-lose Authentifizierung und Chat-Optionen:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Der Token-Lieferant erneuert Zugriffstoken bei Bedarf. Protokollieren Sie keine Tokens oder ersetzen Sie dies durch einen API-Schlüssel. Jedes Programm nutzt seinen Client wieder und schließt ihn in `finally` oder durch eigenen `AutoCloseable`-Wrapper; Das SDK-`OpenAIClient` selbst ist nicht `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) verlangt eine vollständige, nichtleere textuelle Antwort. Leere Optionen, Ablehnungen, Filter und abgeschnittene Antworten werden nicht stillschweigend als Erfolg ausgegeben. Das verantwortliche KI-Beispiel behandelt erwartete Filter-/Ablehnungsergebnisse explizit. Nicht behandelte Fehler geben dem Java/Maven-Prozess einen Nicht-Null-Exit-Code.

**Automatische SDK-Wiederholungen sind deaktiviert,** um die Anfragenanzahl auf geteilten Low-RPM-Bereitstellungen vorhersehbar zu halten. Jede Inferenz-Anfrage hat ein Timeout von 60 Sekunden. Die Token-Akquise kann zusätzliche Zeit beanspruchen. Die Anwendungsschicht muss Quoten einhalten; führen Sie fehlgeschlagene kostenpflichtige Anfragen nicht blind neu aus.

## Unit-Tests

Aus dem examples-Verzeichnis:

```powershell
mvn -B -ntp clean test
```

Der Test-Transport ersetzt vollständig die SDK-HTTP-Schicht, erfasst tatsächlich serialisierte Anfragetexte und liefert vorgereihte Antworten. Er öffnet keine Sockets, holt keine Azure-Tokens und schlägt bei unerwarteten Anfragen fehl. Diese Tests validieren Anwendungsverhalten und SDK-Protokoll, nicht die Qualität des Live-Modells oder die Verfügbarkeit der Bereitstellung.

| Testsuite | Abdeckung |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Endpoint-Normalisierung/-Ablehnung, Bereitstellungsüberschreibungen, Reasoning- und Token-Optionen |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Jeder Vervollständigungsworkflow, Nachrichtenhistorie, vollständiges Turn-Trimming, EOF, Fehler |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Werkzeug-Schemas, typisierte Argumente, Arithmetik, IDs, mehrere Werkzeug-Ergebnisse, fehlgeschlagene Follow-ups |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Dateisuche, UTF-8, Größenlimits, Grounding Payload, Eingabe- und API-Fehler |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Alle sechs Prüfungen, explizite Filter, Ablehnungsklassifikation, normale 400- und weitere Fehler |

Für eine Suite verwenden Sie `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Gemeinsame Fixtures befinden sich in [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sequenzielle Live-Verifikation

Live-Aufrufe sind getrennt von Unit-Tests. Verwenden Sie die folgenden Befehle **einzeln**, vom Repository-Stamm aus, nur wenn Anmeldedaten und Bereitstellungszugang bereitstehen. Es sind keine Dienste oder persistente Prozesse erforderlich.

Für eine gemeinsam genutzte **10 Anfragen/Minute** Bereitstellung reservieren Sie genügend Kontingent für das komplette nächste Programm vor dem Start: 5, 4, 1, dann 6 Anfragen. Sequenzielle Prozesse allein garantieren keine Ratenlimit-Einhaltung. Koordinieren Sie die Rollierende Minute mit allen anderen Nutzern; fügen Sie die vier Aufrufe nicht als ungeplant gepulstes Batch ein.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Vervollständigungen, mehrere Turns, zwei interaktive Runden:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Überprüfen Sie alle drei Abschnittsüberschriften, fünf Antworten, eine abschließende interaktive Antwort, die Ada erwähnt, `Auf Wiedersehen!` und den Exit-Code 0. Budget: **5 Anfragen, höchstens 1.900 Completion-Token**. Für einen kleineren Durchlauf nur `exit` weiterleiten: 3 Anfragen / 900 Token, aber dies testet keine interaktive Inferenz.

**2. Beide Function-Calling-Workflows:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Überprüfen Sie beide Funktionsnamen, das simulierte Wetter in Seattle, das berechnete Ergebnis 36, zwei abschließende Antworten und den Exit-Code 0. Budget: **4 Anfragen, höchstens 1.200 Completion-Token**.

**3. Dokumentbasierte Antwort:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Überprüfen Sie den Dokumentpfad, eine Antwort, die Microsoft Entra ID erwähnt, und den Exit-Code 0. Budget: **1 Anfrage, höchstens 500 Completion-Token**. Die vorhandene [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) ist die einzige erforderliche Eingabedatei. Ein optionaler zweiter Durchlauf, der ein nicht vorhandenes Thema abfragt, sollte dies unterlassen und fügt eine Anfrage / 500 Token hinzu.

**4. Responsible-AI-Beobachtungen:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Überprüfen Sie sechs Kategorien und die Beobachtungszusammenfassung, bewerten Sie den generierten Inhalt und erfordern Sie den Exit-Code 0 für den technischen Abschluss. Ein erfolgreicher Prozessabbruch bestätigt nicht die Sicherheit des Modells. Budget: **6 Anfragen, höchstens 1.800 Completion-Token**.

**Insgesamt für die vier Befehle: 16 Chat-Anfragen und höchstens 5.400 Completion-Token**, plus Eingabetoken (einschließlich wiederholter Konversation und Tool-Schema/-Verlauf). Es gibt keine Embedding-Anfragen. Die tatsächliche Token-Nutzung hängt vom Modell ab und kann niedriger sein, insbesondere bei gefilterten Eingabeaufforderungen. Die Dollar-Kosten hängen von den Preiseinstellungen der Bereitstellung ab; es wird keine feste monetäre Schätzung angegeben. Alle Anfragelimits gelten ohne manuelle Wiederholungen. Prüfen Sie `$LASTEXITCODE` unmittelbar nach jedem Befehl; ungleich Null bedeutet, dass der Durchlauf nicht erfolgreich beendet wurde.

## Fehlerbehebung

- **Fehlender Endpunkt / 401 / 403:** Legen Sie den Endpunkt im Startprozess fest, überprüfen Sie die lokale Azure-Anmeldung und die ressourcenbezogene Rolle und prüfen Sie unbeabsichtigte Überschreibungen der Identitätsumgebung.
- **400 / 404:** Vergewissern Sie sich, dass die Bereitstellung existiert und Chat Completions mit Reasoning-Einstellung `none` unterstützt. Verwenden Sie die HTTPS-Ressourcenwurzel oder die URL `/openai/v1`, nicht eine ältere Bereitstellungs-URL. Übliche 400-Fehler sind technische Fehler, keine Sicherheitsblockaden.
- **429:** Stimmen Sie das gemeinsame RPM- und Token-Kontingent ab, bevor Sie es erneut versuchen. Die Beispiele retryen absichtlich nicht automatisch.
- **`Unvollständige Chat-Antwort: Länge`:** Die Ausgabe hat das Completion-Limit erreicht. Überprüfen Sie die Antwort und die Eingabeaufforderung, bevor Sie das Limit und das dokumentierte Budget erhöhen; dokumentieren Sie keinen abgeschnittenen Durchlauf als erfolgreich.
- **Datei- oder stdin-Fehler:** Starten Sie aus einem unterstützten Verzeichnis oder geben Sie einen expliziten Dokumentpfad an. Geben Sie eine nicht-leere Leseanfrage ein. Completions können bei EOF oder `exit` normal enden.
- **Kompilierfehler:** Prüfen Sie Java 21 oder neuer, dann führen Sie `mvn -B -ntp clean test` aus. Unter PowerShell setzen Sie das gesamte Maven-Argument mit einem Punkt-Eigenschaftsnamen in Anführungszeichen, z. B. `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Nächste Schritte

Fahren Sie fort mit [Kapitel 4: Praktische Beispiele](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->