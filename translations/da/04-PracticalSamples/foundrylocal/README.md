# Foundry Local Spring Boot Vejledning

Kør en lille sprogmodel på din egen maskine og kald dens OpenAI-kompatible
REST-endpoint fra en Java-konsolapplikation. Ingen Azure-udrulning, Azure-login,
cloud API-nøgle eller cloud-inferens anvendes. **GPT-5.6 Luna er kun til Azure; konfigurer den ikke
som en Foundry Local model.**

## Versioner og forudsætninger

| Komponent | Version |
| --- | --- |
| Java | 21 eller nyere |
| Maven | 3.6.3 eller nyere |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokal REST-server) | 2.0.1 |
| Node.js (lokal REST-server) | 20 eller nyere |
| Foundry Local CLI (valgfri, separat udgivelse) | 0.10.3 preview |

Spring Boot administrerer versioner af Spring Framework, Jackson, JUnit og Maven-plugin.
Dette eksempel bruger OpenAI Java SDK direkte, ikke Spring AI. Den gamle ubrugte
Spring AI milepæl-ejendom og repository er fjernet.

Den anbefalede startmodel er **Qwen 2.5 0.5B**, CPU-variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (ca. 822 MB i kataloget).
Den undgår krav om GPU-udførelsesudbydere. Andre understøttede, cachede små modeller
kan vælges eksplicit. Model- og runtime-installation kræver netværksadgang;
forespørgsler og inferens forbliver lokale. Foundry Local kan stadig udsende minimal runtime
diagnostik selv med ikke-væsentlig telemetri deaktiveret.

Kør følgende kommandoer fra denne eksempelmappe.

## Byg og test Java

```powershell
mvn clean verify
```

HTTP-kontraktstestene starter en ephemer loopback-server og afprøver den faktiske
OpenAI Java SDK. De dækker anmodningsserialisering, modelopdagelse, eksplicit modelvalg,
tvetydige eller fejlbehæftede modellister, HTTP-fejl, tomme svar,
lokal-only URLs og kommandolinje-fejlpropagering. De behøver ingen model eller
netværksadgang udover Maven afhængighedsinstallation. Den live test er valgfri.

## Start den lokale model

### Anbefalet: fastlåst SDK-server

Der findes ikke en native Foundry Local Java SDK. Den lille Node.js-helper hoster
den officielle SDK's REST-server; applikationen og chat-forespørgslen forbliver Java.

Installer de fastlåste runtime-afhængigheder:

```powershell
npm ci
```

Hvis Windows x64 ikke kan få adgang til NuGet under SDK's native installation, brug den medfølgende
fallback. Den downloader det matchende officielle GitHub runtime-arkiv, tjekker
release's SHA-256 digest og lægger dets DLL'er ved siden af native-tilføjelsen. Den
deaktiverer ikke TLS-validering, kræver ikke forhøjelse eller ændrer SDK-kilde.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

List modeller, der allerede er cachet på denne maskine:

```powershell
npm run start:foundry -- --list
```

Ved første kørsel, tillad eksplicit download af den lille CPU-model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Ved efterfølgende kørsel, udelad `--download` for at kræve en cachet model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Helperen foretrækker en matchende cachet model, accepterer et alias eller præcis variant-ID,
og afviser en manglende model, medmindre `--download` angives. Den registrerer kun den
valgte models execution provider, når en sådan kræves. Cachede GPU-varianter kan
stadig kræve kompatible execution provider-pakker og drivere.

Hvis port 5273 er optaget, angiv `--port 0` for en tilgængelig port. Helperen udskriver
`FOUNDRY_LOCAL_BASE_URL`, det eksakte `FOUNDRY_LOCAL_MODEL` ID og dens PID, når den er klar.
Brug det udskrevne endpoint i Java. Lad denne terminal være åben under kørsel af Java;
**Ctrl+C** stopper REST-serveren og frigiver modellen.

Standardcachen er `~/.foundry/cache/models`. Angiv `FOUNDRY_LOCAL_CACHE_DIR` for en
anden eksisterende cache. Logs og helper-tilstand skrives under denne eksempels
`target/foundry-local` bibliotek. Stop helperen før du kører `mvn clean`.

### Valgfri: Foundry Local CLI

CLI og SDK har uafhængige udgivelser: CLI **0.10.3** indeholder SDK **1.2.4**;
helperen ovenfor bruger SDK **2.0.1**. Installation af nyeste CLI installerer ikke
automatisk nyeste sprog-SDK. Se [CLI udgivelsesnoter](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

På Windows, brug brugerinstallationskommandoen, hvis CLI ikke er til stede:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Eller opgrader en eksisterende installation:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x erstatter gamle `foundry service` kommandoer med `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` kræver en allerede downloadet model. Se `foundry model --help` for
download-kommandoer. Brug statusoutputtets faktiske endpoint; CLI bruger ellers
som standard en automatisk tildelt port. Start ikke CLI og SDK helper
på samme port. Når du er færdig:

```powershell
foundry server stop
```

## Kør Java-applikationen

I en anden terminal, sæt endpoint og eksakt model-ID, der blev udskrevet af din server:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Eller kør den pakkede applikation:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Det eneste Java-entrépunkt er `com.example.Application`. Den udskriver det valgte
endpoint, faktiske model-ID, prompt og genererede svar, hvorefter den lukker sin Spring
kontekst og HTTP-klient. Mislykket inferens eller manglende svartekst resulterer i
en fejlagtig exit i stedet for et succesformet pladsholder.

### Konfiguration

| Miljøvariabel | Standard | Formål |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP-endpoint, inklusive `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Tom | Eksakt model-ID; ellers vælges den enkelt annoncerede model |
| `FOUNDRY_LOCAL_PROMPT` | Et ét-sætnings spørgsmål om lokale modeller | Prompt sendt af konsolløber |

Ækvivalente Spring-argumenter er `--foundry.local.base-url=...`,
`--foundry.local.model=...`, og `--foundry.local.prompt=...`.
Kun loopback HTTP-endpoints accepteres. Fjern/cloud-endpoints, indlejrede
legitimationsoplysninger, forespørgselsstrenge og stier uden `/v1` afvises.

En tom modelindstilling fungerer kun, når `/v1/models` annoncerer præcis én model.
En annonceret model er ikke nødvendigvis indlæst. Hvis flere modeller annonceres,
angiv det præcise indlæste ID i stedet for at stole på katalogrækkefølge.

Forespørgsler bruger `temperature=0`, en 150-token outputgrænse, en 120-sekunders timeout og
ingen automatiske genforsøg. Feltet `max_tokens` i forespørgslen er tilsigtet: det understøttes
af Foundry Local REST-kontrakten, selvom OpenAI Java deprecierer
det felt for nyere cloud-modeller. Modelidentitet kommer fra konfiguration eller
opdagelse, ikke fra modellens egne påstande om sig selv.

## Live validering

Når den lokale server kører, skal du køre alle tests inklusive den frivillige live-test.
Erstat endpointets port med den værdi, som din server har udskrevet. Sæt citationstegn omkring punktum-
Maven-egenskaber i PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Live-testen kalder `Application.main`, leverer faktumet "Hovedstaden i
Frankrig er Paris," spørger efter byen og bekræfter, at den faktiske genererede tekst er
`Paris`. Den tjekker et semantisk resultat, ikke kun en vellykket HTTP-status.

Dette er en integrationskontrol, ikke et præcisionsmål. Under valideringen svarede denne
0,5B-model på en separat "2 + 2"-prompt med `3` gennem både Java og direkte
REST. Stol ikke på den til aritmetik eller faktuel nøjagtighed uden uafhængig
verifikation; brug deterministiske værktøjer til beregninger.

## Fejlfinding

| Symptom | Kontrol |
| --- | --- |
| Forbindelse afvist | Vent på klar-meddelelsen; brug den udskrevne port og `/v1` sti. |
| Flere modeller reklameret | Sæt `FOUNDRY_LOCAL_MODEL` til den indlæste modells eksakte ID. |
| Model mangler | Brug `--list`, eller tillad eksplicit download med `--download`. |
| GPU-udbyder fejler eller stopper | Brug den lille CPU-model. En cachet GPU-model har stadig brug for sin udbyder. |
| CLI forbliver `initializing` | Læs `foundry server logs --lines 80`; stop daemonen og brug SDK-hjælperen. |
| NuGet TLS/download fejl | Ret netværksadgang eller brug den godkendte Windows x64 fallback ovenfor. Deaktiver ikke TLS. |
| Port optaget | Brug `--port 0` og konfigurer Java med den udskrevne endpoint. |
| Ingen valg eller tom tekst | App’en fejler bevidst; undersøg model- og runtime-logs. |

## Kilde og referencer

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): one-shot Spring Boot-runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typet opdagelse og lokale chat-fuldførelser.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP-kontrakt, runner og live tests.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): officiel SDK REST-server med cachet modelvalg og oprydning.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): verificeret Windows x64 native-runtime fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), og [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfiguration og afhængigheder.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release og migrationsnoter](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Kapitel 04: Praktiske eksempler](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->