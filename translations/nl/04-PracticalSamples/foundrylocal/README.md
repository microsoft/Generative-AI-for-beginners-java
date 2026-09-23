# Foundry Local Spring Boot Tutorial

Draai een klein taalmodel op je eigen machine en roep de OpenAI-compatibele
REST-endpoint aan vanuit een Java-consoleapplicatie. Geen Azure-implementatie, Azure-aanmelding,
cloud-API-sleutel of cloudinferencing wordt gebruikt. **GPT-5.6 Luna is alleen voor Azure; stel deze niet in als een Foundry Local-model.**


## Versies en vereisten

| Component | Versie |
| --- | --- |
| Java | 21 of later |
| Maven | 3.6.3 of later |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokale REST-server) | 2.0.1 |
| Node.js (lokale REST-server) | 20 of later |
| Foundry Local CLI (optioneel, aparte release) | 0.10.3 preview |

Spring Boot beheert Spring Framework-, Jackson-, JUnit- en Maven plugin-versies.
Dit voorbeeld gebruikt de OpenAI Java SDK rechtstreeks, niet Spring AI. De oude ongebruikte
Spring AI mijlpaal-eigenschap en repository zijn verwijderd.

Het aanbevolen startermodel is **Qwen 2.5 0.5B**, CPU-variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (ongeveer 822 MB in de catalogus).
Het vermijdt het vereisen van GPU-uitvoeringsproviders. Andere ondersteunde, gecachte kleine modellen
kunnen expliciet worden geselecteerd. Model- en runtime-installatie vereisen netwerktoegang;
prompts en inferentie blijven lokaal. Foundry Local kan nog steeds minimale runtime-
diagnostiek afgeven, zelfs met niet-essentiële telemetrie uitgeschakeld.

Voer de volgende opdrachten uit vanuit deze voorbeeldmap.

## Bouw en test Java

```powershell
mvn clean verify
```

De HTTP contracttests starten een tijdelijke loopback-server en testen de daadwerkelijke
OpenAI Java SDK. Ze dekken verzoek-serialisatie, modelontdekking, expliciete model-
selectie, ambigue of onjuiste modellenlijsten, HTTP-fouten, lege antwoorden,
alleen-lokale URL's en commandoregelfoutpropagatie. Ze hebben geen model of
netwerktoegang nodig anders dan Maven afhankelijkheidsinstallatie. De live test is opt-in.

## Start het lokale model

### Aanbevolen: vastgezette SDK-server

Er is geen native Foundry Local Java SDK. De kleine Node.js helper host de
officiële SDK's REST-server; de applicatie en chatverzoek blijven Java.

Installeer de vastgezette runtime-afhankelijkheden:

```powershell
npm ci
```

Als Windows x64 tijdens de native SDK-installatie geen toegang heeft tot NuGet, gebruik dan de meegeleverde
fallback. Deze downloadt het bijpassende officiële GitHub runtime-archief, controleert de
SHA-256 digest van de release en plaatst de DLL's naast de native addon. Dit schakelt geen TLS-validatie uit,
vereist geen verhoogde rechten en wijzigt de SDK-bron niet.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Toon modellen die al gecached zijn op deze machine:

```powershell
npm run start:foundry -- --list
```

Sta bij de eerste keer expliciet het downloaden van het kleine CPU-model toe:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Bij volgende keren laat `--download` weg om een gecached model te vereisen:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

De helper geeft de voorkeur aan een overeenkomend gecached model, accepteert een alias of exacte variant-ID
en weigert een ontbrekend model tenzij `--download` wordt meegegeven. Hij registreert alleen de
uitvoeringsprovider van het gekozen model als er een vereist is. Gecachte GPU-varianten kunnen
nog steeds compatibele uitvoerings-provider pakketten en drivers nodig hebben.

Als poort 5273 in gebruik is, geef dan `--port 0` op voor een beschikbare poort. De helper toont
`FOUNDRY_LOCAL_BASE_URL`, de exacte `FOUNDRY_LOCAL_MODEL` ID en zijn PID wanneer klaar.
Gebruik de getoonde endpoint in Java. Laat dit terminalvenster open tijdens het draaien van Java;
**Ctrl+C** stopt de REST-server en geeft het model vrij.

De standaardcache is `~/.foundry/cache/models`. Stel `FOUNDRY_LOCAL_CACHE_DIR` in voor een
andere bestaande cache. Logs en helperstatus worden geschreven in de `target/foundry-local` directory
van dit voorbeeld. Stop de helper voordat je `mvn clean` uitvoert.

### Optioneel: Foundry Local CLI

De CLI en SDK hebben onafhankelijke releases: CLI **0.10.3** bevat SDK **1.2.4**;
de hierboven gebruikte helper gebruikt SDK **2.0.1**. Het installeren van de nieuwste CLI
installeert niet de nieuwste taal-SDK. Zie de [CLI release notes](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Gebruik op Windows de per-gebruiker installatiestatus als de CLI ontbreekt:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Of upgrade een bestaande installatie:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x vervangt oude `foundry service` commando's door `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` vereist een reeds gedownload model. Controleer `foundry model --help` voor
downloadcommando's. Gebruik altijd het daadwerkelijke endpoint uit de statusuitvoer; anders gebruikt de CLI
standaard een automatisch toegewezen poort. Start de CLI en SDK helper niet op dezelfde poort.
Wanneer klaar:

```powershell
foundry server stop
```

## Draai de Java-applicatie

Stel in een tweede terminal het endpoint en de exacte model-ID in die je server heeft uitgeprint:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Of draai de verpakte applicatie:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Het enkele Java-entrypoint is `com.example.Application`. Het print het geselecteerde
endpoint, de daadwerkelijke model-ID, prompt en gegenereerd antwoord, sluit dan zijn Spring
context en HTTP-client. Mislukte inferentie of ontbrekende antwoordtekst resulteert in een
foutcode in plaats van een succesvormige placeholder.

### Configuratie

| Omgevingsvariabele | Standaard | Doel |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP-endpoint, inclusief `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Leeg | Exacte model-ID; anders wordt het enige geadverteerde model gekozen |
| `FOUNDRY_LOCAL_PROMPT` | Een één-zin vraag over lokale modellen | Prompt verzonden door de console-runner |

Equivalente Spring-argumenten zijn `--foundry.local.base-url=...`,
`--foundry.local.model=...` en `--foundry.local.prompt=...`.
Alleen loopback HTTP-endpoints worden geaccepteerd. Remote/cloud endpoints, ingebedde
referenties, query strings en paden zonder `/v1` worden geweigerd.

Een lege modelinstelling werkt alleen als `/v1/models` precies één model adverteert.
Een geadverteerd model is niet per se geladen. Als meerdere modellen geadverteerd worden,
stel dan de exacte geladen ID in in plaats van te vertrouwen op catalogus volgorde.

Verzoeken gebruiken `temperature=0`, een output-limiet van 150 tokens, een timeout van 120 seconden, en
geen automatische retries. Het `max_tokens` aanvraagveld is bewust aanwezig: het wordt
ondersteund door het Foundry Local REST-contract, hoewel OpenAI Java dat veld deprecated voor
nieuwere cloudmodellen. Modelidentiteit komt uit configuratie of ontdekking,
niet uit de claims van het model zelf.

## Live validatie

Met de lokale server draaiend, voer je alle tests uit inclusief de opt-in live test.
Vervang de poort in het endpoint door de waarde die jouw server heeft uitgeprint. Quote scherpe
Maven-eigenschappen in PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

De live test roept `Application.main` aan, geeft de feitelijke "De hoofdstad van Frankrijk is Parijs," als feit,
vraagt om de stad en controleert of de daadwerkelijke gegenereerde tekst
`Paris` is. Het controleert een semantisch resultaat, niet alleen een succesvolle HTTP-status.

Dit is een integratiecontrole, geen nauwkeurigheidsbenchmark. Tijdens validatie gaf dit
0.5B model een antwoord `3` op een aparte "2 + 2" prompt via zowel Java als directe
REST. Vertrouw niet op het model voor rekenkundige of feitelijke nauwkeurigheid zonder onafhankelijke
verificatie; gebruik deterministische tools voor berekeningen.

## Probleemoplossing

| Symptoom | Controleer |
| --- | --- |
| Verbinding geweigerd | Wacht op het gereed bericht; gebruik de opgegeven poort en `/v1` pad. |
| Meerdere modellen geadverteerd | Stel `FOUNDRY_LOCAL_MODEL` in op de exacte ID van het geladen model. |
| Model ontbreekt | Gebruik `--list`, of sta expliciet een download toe met `--download`. |
| GPU-provider faalt of hapert | Gebruik het kleine CPU-model. Een gecached GPU-model heeft nog steeds de provider nodig. |
| CLI blijft `initializing` | Lees `foundry server logs --lines 80`; stop de daemon en gebruik de SDK helper. |
| NuGet TLS/download falen | Herstel netwerktoegang of gebruik de bovengenoemde geverifieerde Windows x64 fallback. Schakel TLS niet uit. |
| Poort in gebruik | Gebruik `--port 0` en configureer Java met het getoonde endpoint. |
| Geen keuzes of lege tekst | De app faalt bewust; inspecteer het model en runtime logs. |

## Bron en referenties

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): one-shot Spring Boot runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): getypeerde ontdekking en lokale chatvoltooiingen.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP contract-, runner- en live tests.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): officiële SDK REST-server met gecachte modelselectie en opruiming.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): geverifieerde Windows x64 native-runtime fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), en [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): configuratie en afhankelijkheden.
- [Foundry Local REST integratie](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release en migratienotities](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Hoofdstuk 04: Praktische voorbeelden](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->