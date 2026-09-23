# Foundry Local Spring Boot Tutorial

Führen Sie ein kleines Sprachmodell auf Ihrem eigenen Rechner aus und rufen Sie dessen OpenAI-kompatiblen
REST-Endpunkt von einer Java-Konsolenanwendung aus auf. Keine Azure-Bereitstellung, kein Azure-Anmelden,
kein Cloud-API-Schlüssel oder Cloud-Inferenz wird verwendet. **GPT-5.6 Luna ist nur für Azure; konfigurieren Sie es nicht
als Foundry Local Modell.**

## Versionen und Voraussetzungen

| Komponente | Version |
| --- | --- |
| Java | 21 oder später |
| Maven | 3.6.3 oder später |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokaler REST-Server) | 2.0.1 |
| Node.js (lokaler REST-Server) | 20 oder später |
| Foundry Local CLI (optional, separate Version) | 0.10.3 Vorschau |

Spring Boot verwaltet Spring Framework, Jackson, JUnit und Maven-Plugin-Versionen.
Dieses Beispiel verwendet direkt das OpenAI Java SDK, nicht Spring AI. Die alte ungenutzte
Spring AI Milestone-Eigenschaft und das Repository wurden entfernt.

Das empfohlene Startermodell ist **Qwen 2.5 0.5B**, CPU-Variante
`qwen2.5-0.5b-instruct-generic-cpu:4` (ca. 822 MB im Katalog).
Es vermeidet die Notwendigkeit für GPU-Ausführungsanbieter. Andere unterstützte, zwischengespeicherte kleine Modelle
können explizit ausgewählt werden. Modell- und Laufzeitinstallation erfordern Netzwerkzugang;
Prompts und Inferenz bleiben lokal. Foundry Local kann trotz deaktivierter nicht wesentlicher Telemetrie minimale Laufzeit-
Diagnosen ausgeben.

Führen Sie die folgenden Befehle aus diesem Beispielverzeichnis aus.

## Java bauen und testen

```powershell
mvn clean verify
```

Die HTTP-Vertragstests starten einen temporären Loopback-Server und testen das tatsächliche
OpenAI Java SDK. Sie prüfen Anfrage-Serialisierung, Modellentdeckung, explizite Modellauswahl,
mehrdeutige oder fehlerhafte Modelllisten, HTTP-Fehler, leere Antworten,
lokal-einzige URLs und Fehlerweiterleitung in der Befehlszeile. Sie benötigen kein Modell oder
Netzwerkzugang außer der Maven-Abhängigkeitsinstallation. Der Live-Test ist optional.

## Starten Sie das lokale Modell

### Empfohlen: festgelegter SDK-Server

Es gibt kein natives Foundry Local Java SDK. Der kleine Node.js-Helfer hostet den
offiziellen SDK REST-Server; die Anwendung und Chat-Anforderung bleiben in Java.

Installieren Sie die festen Laufzeitabhängigkeiten:

```powershell
npm ci
```

Falls Windows x64 während der nativen SDK-Installation NuGet nicht erreichen kann, verwenden Sie die bereitgestellte
Rückfalllösung. Sie lädt das passende offizielle GitHub-Laufzeit-Archiv herunter, überprüft den
SHA-256-Digest der Veröffentlichung und legt dessen DLLs neben dem nativen Addon ab. Sie deaktiviert
keine TLS-Validierung, erfordert keine Rechteerhöhung oder modifiziert den SDK-Quellcode.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Listen Sie Modelle auf, die bereits auf diesem Rechner zwischengespeichert sind:

```powershell
npm run start:foundry -- --list
```

Erlauben Sie beim ersten Lauf explizit den Download des kleinen CPU-Modells:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Bei nachfolgenden Läufen lassen Sie `--download` weg, um ein zwischengespeichertes Modell zu verlangen:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Der Helfer bevorzugt ein passendes zwischengespeichertes Modell, akzeptiert einen Alias oder exakte Varianten-ID
und lehnt ein fehlendes Modell ab, sofern nicht `--download` angegeben wurde. Er registriert nur den
Ausführungsanbieter des ausgewählten Modells, wenn einer benötigt wird. Zwischengespeicherte GPU-Varianten können
dennoch kompatible Ausführungsanbieter-Pakete und Treiber benötigen.

Wenn Port 5273 belegt ist, geben Sie `--port 0` für einen verfügbaren Port an. Der Helfer gibt
`FOUNDRY_LOCAL_BASE_URL`, die exakte `FOUNDRY_LOCAL_MODEL` ID und seine PID aus, wenn er bereit ist.
Verwenden Sie den ausgegebenen Endpunkt in Java. Lassen Sie dieses Terminal geöffnet, während Java läuft;
**Strg+C** stoppt den REST-Server und gibt das Modell frei.

Der Standardspeicherort ist `~/.foundry/cache/models`. Setzen Sie `FOUNDRY_LOCAL_CACHE_DIR` für
einen anderen vorhandenen Cache. Logs und Helferstatus werden im Verzeichnis `target/foundry-local` dieses Beispiels abgelegt.
Stoppen Sie den Helfer, bevor Sie `mvn clean` ausführen.

### Optional: Foundry Local CLI

CLI und SDK haben unabhängige Releases: CLI **0.10.3** bündelt SDK **1.2.4**;
der oben erwähnte Helfer verwendet SDK **2.0.1**. Die Installation der neuesten CLI installiert nicht das
neueste Sprach-SDK. Siehe die [CLI-Release Notes](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Unter Windows verwenden Sie den Benutzerinstallationsbefehl, falls die CLI fehlt:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Oder aktualisieren Sie eine bestehende Installation:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x ersetzt alte `foundry service` Befehle durch `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` benötigt ein bereits heruntergeladenes Modell. Prüfen Sie `foundry model --help` für
Download-Befehle. Verwenden Sie den Statusausgabe-Endpunkt; die CLI nutzt sonst
standardmäßig einen automatisch zugewiesenen Port. Starten Sie CLI und SDK-Helfer
nicht auf demselben Port. Wenn Sie fertig sind:

```powershell
foundry server stop
```

## Führen Sie die Java-Anwendung aus

In einem zweiten Terminal setzen Sie den vom Server ausgegebenen Endpunkt und die exakte Modell-ID:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Oder führen Sie die gepackte Anwendung aus:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Der einzige Java-Einstiegspunkt ist `com.example.Application`. Er gibt den ausgewählten
Endpunkt, die tatsächliche Modell-ID, Prompt und generierte Antwort aus, schließt dann seinen Spring
Kontext und HTTP-Client. Fehlgeschlagene Inferenz oder fehlender Antworttext erzeugt einen
Fehlerabbruch anstelle eines platzhalterartigen Erfolgs.

### Konfiguration

| Umgebungsvariable | Standard | Zweck |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback-HTTP-Endpunkt, inklusive `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Leer | Exakte Modell-ID; sonst Auswahl des einzigen beworbenen Modells |
| `FOUNDRY_LOCAL_PROMPT` | Eine Ein-Satz-Frage zu lokalen Modellen | Prompt, gesendet vom Konsolenläufer |

Entsprechende Spring-Argumente sind `--foundry.local.base-url=...`,
`--foundry.local.model=...` und `--foundry.local.prompt=...`.
Nur Loopback-HTTP-Endpunkte sind akzeptiert. Remote-/Cloud-Endpunkte, eingebettete
Zugangsdaten, Abfragezeichenfolgen und Pfade ohne `/v1` werden abgelehnt.

Eine leere Modellauswahl funktioniert nur, wenn `/v1/models` genau ein Modell bewirbt.
Ein beworbenes Modell ist nicht zwingend geladen. Wenn mehrere Modelle beworben werden,
setzen Sie die exakt geladene ID statt auf Katalogreihenfolge zu vertrauen.

Anfragen verwenden `temperature=0`, ein Ausgabe-Limit von 150 Token, eine Timeout von 120 Sekunden und
keine automatische Wiederholungen. Das `max_tokens` Anfragefeld ist beabsichtigt: Es wird
vom Foundry Local REST-Vertrag unterstützt, obwohl OpenAI Java es depräziert.
dieses Feld für neuere Cloud-Modelle. Die Modellidentität stammt aus der Konfiguration oder
der Erkennung, nicht aus den Eigenbehauptungen des Modells.

## Live-Validierung

Mit laufendem lokalem Server führen Sie alle Tests einschließlich des Opt-in-Live-Tests aus.
Ersetzen Sie den Port des Endpunkts durch den von Ihrem Server ausgegebenen Wert. Setzen Sie in PowerShell
gepunktete Maven-Eigenschaften in Anführungszeichen:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Der Live-Test ruft `Application.main` auf, liefert den Fakt „Die Hauptstadt von
Frankreich ist Paris“, fragt die Stadt ab und überprüft, dass der tatsächlich generierte Text
`Paris` ist. Es wird ein semantisches Ergebnis geprüft, nicht nur ein erfolgreicher HTTP-Status.

Dies ist eine Integrationsprüfung, kein Genauigkeitsmaßstab. Während der Validierung
antwortete dieses 0,5B-Modell auf eine separate „2 + 2“-Eingabe sowohl über Java als auch direkte
REST mit `3`. Verlassen Sie sich ohne unabhängige
Verifikation nicht auf seine Rechen- oder Faktengenauigkeit; verwenden Sie für Berechnungen deterministische Werkzeuge.

## Fehlerbehebung

| Symptom | Überprüfung |
| --- | --- |
| Verbindung abgelehnt | Warten Sie auf die Bereitmeldung; verwenden Sie den ausgegebenen Port und den Pfad `/v1`. |
| Mehrere Modelle beworben | Setzen Sie `FOUNDRY_LOCAL_MODEL` auf die exakte ID des geladenen Modells. |
| Modell fehlt | Verwenden Sie `--list` oder erlauben Sie explizit einen Download mit `--download`. |
| GPU-Anbieter schlägt fehl oder hängt | Verwenden Sie das kleine CPU-Modell. Ein zwischengespeichertes GPU-Modell benötigt dennoch seinen Anbieter. |
| CLI bleibt `initializing` | Lesen Sie `foundry server logs --lines 80`; stoppen Sie den Daemon und nutzen Sie den SDK-Helfer. |
| NuGet TLS-/Download-Fehler | Beheben Sie den Netzwerkzugang oder verwenden Sie den oben verifizierten Windows-x64-Fallback. Deaktivieren Sie TLS nicht. |
| Port belegt | Verwenden Sie `--port 0` und konfigurieren Sie Java mit dem ausgegebenen Endpunkt. |
| Keine Auswahl oder leerer Text | Die App schlägt absichtlich fehl; überprüfen Sie das Modell und die Laufzeitprotokolle. |

## Quellcode und Verweise

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): einmaliger Spring-Boot-Runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typ-basierte Entdeckung und lokale Chat-Vervollständigungen.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP-Vertrag, Runner und Live-Tests.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): offizieller SDK-REST-Server mit Auswahl zwischengespeicherter Modelle und Bereinigung.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): verifizierter nativer Runtime-Fallback für Windows x64.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), und [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): Konfiguration und Abhängigkeiten.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Kapitel 04: Praktische Beispiele](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->