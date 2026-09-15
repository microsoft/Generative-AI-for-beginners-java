# Foundry Local Spring Boot-veiledning

Kjør en liten språkmodell på din egen maskin og kall dens OpenAI-kompatible
REST-endepunkt fra en Java-konsollapplikasjon. Ingen Azure-distribusjon, Azure-pålogging,
sky-API-nøkkel eller sky-inferanse brukes. **GPT-5.6 Luna er kun for Azure; ikke
konfigurer den som en Foundry Local-modell.**

## Versjoner og forutsetninger

| Komponent | Versjon |
| --- | --- |
| Java | 21 eller senere |
| Maven | 3.6.3 eller senere |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokal REST-server) | 2.0.1 |
| Node.js (lokal REST-server) | 20 eller senere |
| Foundry Local CLI (valgfritt, separat utgivelse) | 0.10.3 preview |

Spring Boot håndterer Spring Framework, Jackson, JUnit og Maven-plugin versjoner.
Dette eksemplet bruker OpenAI Java SDK direkte, ikke Spring AI. Den gamle ubrukte
Spring AI milepæl-egenskapen og repositoriet er fjernet.

Den anbefalte startmodellen er **Qwen 2.5 0.5B**, CPU-variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (omtrent 822 MB i katalogen).
Den unngår krav om GPU-kjøringsleverandører. Andre støttede, bufrede små modeller
kan velges eksplisitt. Modell- og runtime-installasjon krever nettverkstilgang;
forespørsler og inferanse forblir lokale. Foundry Local kan fortsatt gi minimalt med runtime
diagnostikk selv med ikke-essensiell telemetri deaktivert.

Kjør følgende kommandoer fra denne eksempel-mappen.

## Bygg og test Java

```powershell
mvn clean verify
```

HTTP-kontraktstestene starter en midlertidig loopback-server og tester den faktiske
OpenAI Java SDK. De dekker forespørsel-serialisering, modelloppdagelse, eksplisitt modell-
valg, tvetydige eller feilformede modellister, HTTP-feil, blanke svar,
lokale URL-er, og propagasjon av feil i kommandolinjen. De trenger ingen modell eller
nettverkstilgang utover Maven-avhengighetsinstallasjon. Live-testen er valgfri.

## Start den lokale modellen

### Anbefalt: fiksert SDK-server

Det finnes ingen innfødt Foundry Local Java SDK. Den lille Node.js-hjelperen hoster
den offisielle SDKs REST-server; applikasjon og chat-forespørsel forblir Java.

Installer de fikserte runtime-avhengighetene:

```powershell
npm ci
```

Hvis Windows x64 ikke kan nå NuGet under SDKs native installasjon, bruk den medfølgende
reserveløsningen. Den laster ned den matchende offisielle GitHub-runtime-arkivet, sjekker
SHA-256-digesten for utgivelsen, og legger DLL-er ved siden av native-tillegget. Den
deaktiverer ikke TLS-validering, krever ikke opphøyelse eller endrer SDK-kilde.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

List modeller allerede bufret på denne maskinen:

```powershell
npm run start:foundry -- --list
```

Ved første kjøring, tillat eksplisitt nedlasting av den lille CPU-modellen:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Ved påfølgende kjøringer, utelat `--download` for å kreve en bufret modell:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Hjelperen foretrekker en matchende bufret modell, godtar et alias eller eksakt variant-ID,
og avslår manglende modell med mindre `--download` er oppgitt. Den registrerer kun
valgt modells kjøringsleverandør når det er nødvendig. Bufrede GPU-varianter kan
fortsatt trenge kompatible kjøringsleverandørpakker og drivere.

Hvis port 5273 er opptatt, pass `--port 0` for en ledig port. Hjelperen skriver ut
`FOUNDRY_LOCAL_BASE_URL`, eksakt `FOUNDRY_LOCAL_MODEL` ID, og dens PID når den er klar.
Bruk det oppgitte endepunktet i Java. La denne terminalen være åpen mens Java kjører;
**Ctrl+C** stopper REST-serveren og frigjør modellen.

Standard cachen er `~/.foundry/cache/models`. Sett `FOUNDRY_LOCAL_CACHE_DIR` for en
annen eksisterende cache. Logger og hjelperstatus skrives under denne eksempelets
`target/foundry-local`-mappe. Stopp hjelperen før du kjører `mvn clean`.

### Valgfritt: Foundry Local CLI

CLI og SDK har uavhengige utgivelser: CLI **0.10.3** pakker SDK **1.2.4**;
hjelperen ovenfor bruker SDK **2.0.1**. Å installere siste CLI installerer ikke siste
språk-SDK. Se [CLI utgivelsesnotater](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

På Windows, bruk per-bruker installasjonskommandoen hvis CLI mangler:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Eller oppgrader en eksisterende installasjon:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x erstatter gamle `foundry service`-kommandoer med `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` krever en allerede nedlastet modell. Sjekk `foundry model --help` for
nedlastingskommandoer. Bruk statusutdataenes reelle endepunkt; CLI standardiserer ellers
til en automatisk tildelt port. Ikke start CLI og SDK-hjelper på samme port.
Når ferdig:

```powershell
foundry server stop
```

## Kjør Java-applikasjonen

I et annet terminalvindu, sett endepunkt og eksakt modell-ID som serveren skrev ut:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Eller kjør den pakkede applikasjonen:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Det eneste Java-inngangspunktet er `com.example.Application`. Det skriver ut valgt
endepunkt, faktisk modell-ID, prompt og generert svar, og lukker deretter sin Spring
kontekst og HTTP-klient. Feilet inferanse eller manglende svaretekst gir
feilutgang i stedet for en suksessformet plassholder.

### Konfigurasjon

| Miljøvariabel | Standard | Formål |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP-endepunkt, inkludert `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Tom | Eksakt modell-ID; ellers velg den enkelt annonserte modellen |
| `FOUNDRY_LOCAL_PROMPT` | Et ett-setnings spørsmål om lokale modeller | Prompt sendt av konsollkjøreren |

Ekvivalente Spring-argumenter er `--foundry.local.base-url=...`,
`--foundry.local.model=...`, og `--foundry.local.prompt=...`.
Bare loopback HTTP-endepunkter godtas. Eksterne/sky-endepunkter, innebygde
legitimasjoner, spørringsstrenger og stier uten `/v1` blir avvist.

En tom modellinnstilling fungerer bare når `/v1/models` annonserer akkurat én modell.
En annonsert modell er ikke nødvendigvis lastet. Hvis flere modeller annonseres,
sett den eksakte lastede ID-en i stedet for å stole på katalogrekkefølge.

Forespørsler bruker `temperature=0`, en 150-token utgagsgrense, 120 sekunders timeout, og
ingen automatiske gjentakelser. Forespørselsfeltet `max_tokens` er hensiktsmessig:
det støttes av Foundry Local REST-kontrakten, selv om OpenAI Java avskriver
dette feltet for nyere sky-modeller. Modellidentitet kommer fra konfigurasjon eller
oppdagelse, ikke fra modellens egne påstander.

## Live-validering

Med lokal server kjørende, kjør alle tester inkludert valgfri live-test.
Erstatt endepunktets port med verdien som serveren skriver ut. Siter punkterte
Maven-egenskaper i PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Live-testen kaller `Application.main`, gir faktumet "Hovedstaden i Frankrike er Paris,"
spør om byen, og sjekker at den faktiske genererte teksten er
`Paris`. Den sjekker et semantisk resultat, ikke bare vellykket HTTP-status.

Dette er en integrasjonstest, ikke en nøyaktighetsmålestokk. Under validering svarte
denne 0.5B modellen på en separat "2 + 2"-prompt med `3` gjennom både Java og direkte
REST. Stol ikke på den for aritmetikk eller faktuell nøyaktighet uten uavhengig
verifikasjon; bruk deterministiske verktøy for beregninger.

## Feilsøking

| Symptom | Sjekk |
| --- | --- |
| Tilkobling avslått | Vent på klar-meldingen; bruk den oppgitte porten og `/v1`-stien. |
| Flere modeller annonsert | Sett `FOUNDRY_LOCAL_MODEL` til lastet modells eksakte ID. |
| Modell mangler | Bruk `--list`, eller tillat eksplisitt nedlasting med `--download`. |
| GPU-leverandør feiler eller henger | Bruk den lille CPU-modellen. En bufret GPU-modell trenger fortsatt leverandør. |
| CLI står fast i `initializing` | Les `foundry server logs --lines 80`; stopp daemon og bruk SDK-hjelperen. |
| NuGet TLS/nedlastingsfeil | Fiks nettverkstilgang eller bruk den verifiserte Windows x64 fallback ovenfor. Ikke deaktiver TLS. |
| Port opptatt | Bruk `--port 0` og konfigurer Java med det oppgitte endepunktet. |
| Ingen valg eller tom tekst | Appen feiler bevisst; undersøk modell- og runtime-logger. |

## Kilde og referanser

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): one-shot Spring Boot-kjører.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typet oppdagelse og lokale chat-kompletter.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP-kontrakt, kjører og live-tester.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): offisiell SDK REST-server med bufret modellvalg og opprydding.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): verifisert Windows x64 native-runtime fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), og [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfigurasjon og avhengigheter.
- [Foundry Local REST-integrasjon](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 utgivelse og migrasjonsnotater](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Kapittel 04: Praktiske eksempler](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->