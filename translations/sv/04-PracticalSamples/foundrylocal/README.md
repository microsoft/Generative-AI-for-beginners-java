# Foundry Local Spring Boot-handledning

Kör en liten språkmodell på din egen maskin och anropa dess OpenAI-kompatibla
REST-endpoint från en Java-konsolapplikation. Ingen Azure-distribution, Azure-inloggning,
cloud API-nyckel eller cloud inferens används. **GPT-5.6 Luna är endast för Azure; konfigurera inte
den som en Foundry Local-modell.**

## Versioner och förutsättningar

| Komponent | Version |
| --- | --- |
| Java | 21 eller senare |
| Maven | 3.6.3 eller senare |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokal REST-server) | 2.0.1 |
| Node.js (lokal REST-server) | 20 eller senare |
| Foundry Local CLI (valfritt, separat version) | 0.10.3 preview |

Spring Boot hanterar versioner för Spring Framework, Jackson, JUnit och Maven-plugin.
Detta exempel använder OpenAI Java SDK direkt, inte Spring AI. Den gamla, oanvända
Spring AI milestone-egenskapen och lagret har tagits bort.

Den rekommenderade startmodellen är **Qwen 2.5 0.5B**, CPU-variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (ungefär 822 MB i katalogen).
Den undviker att kräva GPU-körningsleverantörer. Andra stödda, cachade små modeller
kan väljas explicit. Modell- och runtime-installation kräver nätverksåtkomst;
prompts och inferens förblir lokala. Foundry Local kan ändå emittera minimal runtime-
diagnostik även med icke-essentiell telemetri avstängd.

Kör följande kommandon från den här exempel-katalogen.

## Bygg och testa Java

```powershell
mvn clean verify
```

HTTP-kontraktstesterna startar en flyktig loopback-server och testar den faktiska
OpenAI Java SDK. De täcker begäransserialisering, modellupptäckt, explicit modell-
val, tvetydiga eller felaktiga modellister, HTTP-fel, tomma svar,
endast lokala URL:er och felhantering i kommandoraden. De behöver ingen modell eller
nätverksåtkomst utöver installation av Maven-beroenden. Live-testet är valfritt.

## Starta den lokala modellen

### Rekommenderat: fast SDK-server

Det finns inget inbyggt Foundry Local Java SDK. Den lilla Node.js-hjälparen hostar
SDK:ns officiella REST-server; applikationen och chat-begäran förblir i Java.

Installera de fastlåsta runtime-beroendena:

```powershell
npm ci
```

Om Windows x64 inte kan nå NuGet under SDK:ns inbyggda installation, använd den medföljande
fallbacken. Den laddar ner matchande officiellt GitHub-runtime-arkiv, kontrollerar
release:ns SHA-256-digest och placerar dess DLL-filer bredvid den inbyggda addonen. Den
inaktiverar inte TLS-validering, kräver inte upphöjning eller ändrar SDK-källa.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Lista modeller som redan är cachade på denna maskin:

```powershell
npm run start:foundry -- --list
```

Vid första körning, tillåt explicit nedladdning av den lilla CPU-modellen:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Vid efterföljande körningar, utelämna `--download` för att kräva en cachad modell:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Hjälparen föredrar en matchande cachad modell, accepterar ett alias eller exakt variant-ID,
och vägrar en saknad modell såvida inte `--download` anges. Den registrerar endast den
valda modellens körningsleverantör när en sådan krävs. Cachade GPU-varianter kan
fortfarande behöva kompatibla körnings-leverantörspaket och drivrutiner.

Om port 5273 är upptagen, ange `--port 0` för en tillgänglig port. Hjälparen skriver ut
`FOUNDRY_LOCAL_BASE_URL`, det exakta `FOUNDRY_LOCAL_MODEL`-ID:t och dess PID när den är redo.
Använd den utskrivna endpointen i Java. Lämna detta terminalfönster öppet under körning av Java;
**Ctrl+C** stoppar REST-servern och släpper modellen.

Standard-cachen är `~/.foundry/cache/models`. Ställ in `FOUNDRY_LOCAL_CACHE_DIR` för en
annan befintlig cache. Loggar och hjälparstatus skrivs under denna exempels
`target/foundry-local`-katalog. Stoppa hjälparen före körning av `mvn clean`.

### Valfritt: Foundry Local CLI

CLI och SDK har oberoende versioner: CLI **0.10.3** innehåller SDK **1.2.4**;
hjälparen ovan använder SDK **2.0.1**. Att installera senaste CLI installerar inte
senaste språk-SDK. Se [CLI versionsanteckningar](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

På Windows, använd per-användare-installationskommandot om CLI saknas:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Eller uppgradera en befintlig installation:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x ersätter gamla `foundry service`-kommandon med `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` behöver en redan nedladdad modell. Kontrollera `foundry model --help` för
nedladdningskommandon. Använd statusutsignalens faktiska endpoint; CLI använder annars
en automatiskt tilldelad port. Starta inte CLI och SDK-hjälparen
på samma port. När du är klar:

```powershell
foundry server stop
```

## Kör Java-applikationen

I en andra terminal, ställ in endpoint och exakt modell-ID som skrivits ut av din server:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Eller kör den paketerade applikationen:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Den enda Java-entrépunkten är `com.example.Application`. Den skriver ut den valda
endpointen, faktiska modell-ID, prompt och genererat svar, stänger sedan sitt Spring-
kontext och HTTP-klient. Misslyckad inferens eller saknat svarstext ger
ett felavslut istället för en placeholder med framgångsformat.

### Konfiguration

| Miljövariabel | Standard | Syfte |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP-endpoint, inklusive `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Tom | Exakt modell-ID; välj annars den enda annonserade modellen |
| `FOUNDRY_LOCAL_PROMPT` | En enkel fråga om lokala modeller | Prompt som skickas av konsolprogrammet |

Motsvarande Spring-argument är `--foundry.local.base-url=...`,
`--foundry.local.model=...` och `--foundry.local.prompt=...`.
Endast loopback HTTP-endpoints accepteras. Remote/cloud-endpoints, inbäddade
autentiseringsuppgifter, frågesträngar och sökvägar utan `/v1` avvisas.

En tom modellinställning fungerar bara när `/v1/models` annonserar exakt en modell.
En annonserad modell är inte nödvändigtvis laddad. Om flera modeller annonseras,
ställ in det exakta laddade ID:t snarare än att förlita dig på katalogordning.

Begäranden använder `temperature=0`, en 150-token utmatningsgräns, en timeout på 120 sekunder, och
inga automatiska omförsök. `max_tokens`-fältet i begäran är medvetet: det stöds
av Foundry Local REST-kontraktet, även om OpenAI Java avråder från
detta fält för nyare molnmodeller. Modellidentiteten kommer från konfiguration eller
upptäckt, inte från modellens egna påståenden.

## Live-validering

Med den lokala servern igång, kör alla tester inklusive valfria live-testet.
Byt ut endpoint-porten mot det värde som skrivits ut av din server. Sätta citationstecken
runt punktseparerade Maven-egenskaper i PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Live-testet anropar `Application.main`, tillhandahåller faktumet "Frankrikes huvudstad är Paris,"
frågar efter staden och försäkrar att faktiskt genererad text är
`Paris`. Det kontrollerar ett semantiskt resultat, inte bara en lyckad HTTP-status.

Detta är en integrationskontroll, inte en precisionstest. Under validering svarade denna
0.5B-modell på en separat "2 + 2"-prompt med `3` via både Java och direkt
REST. Förlita dig inte på den för aritmetik eller faktuell noggrannhet utan oberoende
verifiering; använd deterministiska verktyg för beräkningar.

## Felsökning

| Symptom | Kontrollera |
| --- | --- |
| Anslutning nekad | Vänta på redo-meddelandet; använd den utskrivna porten och `/v1`-sökvägen. |
| Flera modeller annonserade | Ställ in `FOUNDRY_LOCAL_MODEL` till det laddade modellens exakta ID. |
| Modell saknas | Använd `--list`, eller tillåt explicit nedladdning med `--download`. |
| GPU-leverantör misslyckas eller fastnar | Använd den lilla CPU-modellen. En cachad GPU-modell behöver fortfarande sin leverantör. |
| CLI förblir `initializing` | Läs `foundry server logs --lines 80`; stoppa daemonen och använd SDK-hjälparen. |
| NuGet TLS-/nedladdningsfel | Åtgärda nätverksåtkomst eller använd den verifierade Windows x64-fallbacken ovan. Inaktivera inte TLS. |
| Port upptagen | Använd `--port 0` och konfigurera Java med den utskrivna endpointen. |
| Inga val eller tom text | Applikationen misslyckas medvetet; inspektera modell- och runtime-loggar. |

## Källkod och referenser

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): engångs Spring Boot-runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typad upptäckt och lokala chatt-svar.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP-kontrakt, runner- och live-test.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): officiell SDK REST-server med cache-modellval och städning.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): verifierad Windows x64 inbyggd runtime-fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), och [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfiguration och beroenden.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release och migrationsanteckningar](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Kapitel 04: Praktiska exempel](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->