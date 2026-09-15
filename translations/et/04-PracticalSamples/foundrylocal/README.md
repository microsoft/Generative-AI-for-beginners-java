# Foundry lokaalse Spring Booti juhend

Käivitage väike keelemudel oma masinas ja kutsuge selle OpenAI-ga ühilduvat
REST lõpp-punkti Java konsoolirakendusest. Azure'i juurutust, Azure'i sisselogimist,
pilve API võtit ega pilve põhist tuletamist ei kasutata. **GPT-5.6 Luna on ainult Azure’i jaoks; ärge
seadistage seda Foundry Local mudelina.**

## Versioonid ja nõuded

| Komponent | Versioon |
| --- | --- |
| Java | 21 või uuem |
| Maven | 3.6.3 või uuem |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokaalne REST server) | 2.0.1 |
| Node.js (lokaalne REST server) | 20 või uuem |
| Foundry Local CLI (valikuline, eraldi väljaanne) | 0.10.3 eelvaade |

Spring Boot haldab Spring Frameworki, Jacksonit, JUniti ja Maveni pluginversioone.
See näide kasutab OpenAI Java SDK-d otse, mitte Spring AI-d. Vana kasutamata
Spring AI verstapost-omadus ja hoidla on eemaldatud.

Soovitatav algusmudel on **Qwen 2.5 0.5B**, CPU variandina
`qwen2.5-0.5b-instruct-generic-cpu:4` (ligikaudu 822 MB kataloogis).
See väldib GPU täitmispakkujate nõudeid. Teisi toetatud, vahemällu salvestatud väikseid mudeleid
saab valida ekspliciitselt. Mudeli ja jooksuaegse paigalduse jaoks on vajalik võrguühendus;
käsud ja tuletamine jäävad lokaalseks. Foundry Local võib siiski väljastada minimaalset
jooksuaegset diagnostikat isegi mittevajaliku telemeetria keelamisel.

Käivitage järgmised käsud sellest näitedirektoriumist.

## Koosta ja testi Java

```powershell
mvn clean verify
```

HTTP lepingutestid käivitavad hägustava loopback serveri ja katsetavad tegelikku
OpenAI Java SDK-d. Need katavad päringu serialiseerimist, mudeli avastamist, ekspliciitse mudeli
valikut, kahtlasi või vigaseid mudeliloendeid, HTTP rikkeid, tühje vastuseid,
ainult lokaalseid URL-e ja käsurea tõrketeadete levikut. Testid ei vaja mudelit ega
võrguühendust peale Maveni sõltuvuste paigalduse. Live-test on vabatahtlik.

## Käivita kohalik mudel

### Soovituslik: fikseeritud SDK server

Puudub natiivne Foundry Local Java SDK. Väike Node.js abiline majutab ametliku
SDK REST serveri; rakendus ja vestluspäring jäävad Java-põhiseks.

Paigaldage fikseeritud jooksuaegsed sõltuvused:

```powershell
npm ci
```

Kui Windows x64 ei pääse SDK natiivse paigaldamise ajal NuGeti juurde, kasutage kaasasolevat
varuplaani. See laadib alla ametliku GitHubi jooksuaegse arhivaadi, kontrollib
väljaande SHA-256 suumlause ja paigutab selle DLL-failid natiivaddoni kõrvale. See ei
keela TLS valideerimist, nõua kõrgendatud õigusi ega muuda SDK lähtekoodi.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Loetle selle masina juba vahemällu salvestatud mudelid:

```powershell
npm run start:foundry -- --list
```

Esimesel kasutamisel lubage ekspliciitselt väikese CPU mudeli allalaadimine:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Järgnevatel kordadel jäta `--download` välja, et nõuda vahemälus olevat mudelit:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Abi eelistab sobivat vahemällu salvestatud mudelit, aktsepteerib aliasi või täpset variandit,
ja keelab puuduvad mudelid, kui ei ole määratud `--download`. Registreerib täitmisprotsessori
ainult valitud mudelile, kui seda nõutakse. Vahemällu jäävad GPU variandid võivad siiski
vajada sobivaid täitmisprotsessori pakette ja draivereid.

Kui pordi 5273 on hõivatud, edastage `--port 0` vabale pordile. Abi prindib
`FOUNDRY_LOCAL_BASE_URL`, täpse `FOUNDRY_LOCAL_MODEL` ID ja selle protsessi ID kui valmis.
Kasutage Java-s prinditud lõpp-punkti. Jätke see terminal avatud, kuni Java töötab;
**Ctrl+C** peatab REST serveri ja vabastab mudeli.

Vaikimisi vahemälu on asukohas `~/.foundry/cache/models`. Määrake `FOUNDRY_LOCAL_CACHE_DIR` kui
soovite kasutada erinevat olemasolevat vahemälu. Logid ja abi olek kirjutatakse selle näite
kausta `target/foundry-local` alla. Peatage abi enne `mvn clean` käivitamist.

### Valikuline: Foundry Local CLI

CLI ja SDK on iseseisvad väljaanded: CLI **0.10.3** sisaldab SDK **1.2.4**;
ülaltoodud abi kasutab SDK **2.0.1**. Viimase CLI paigaldamine ei paigalda
uusimat keele SDK-d. Vaadake [CLI väljaannete märkmeid](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Windowsis kasutage kasutajapõhist paigalduskäsku, kui CLI pole olemas:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Või uuendage olemasolevat installatsiooni:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x asendab vanad `foundry service` käsud käsuga `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` vajab eelnevalt allalaaditud mudelit. Kontrollige `foundry model --help`
allalaadimiskäske. Kasutage oleku väljundist kuvatud lõpp-punkti; CLI valib
muidu automaatselt määratud pordi. Ära käivita CLI-d ja SDK abi korraga samal pordil.
Kui lõpetatud:

```powershell
foundry server stop
```

## Käivita Java rakendus

Teises terminalis määrake oma serveri poolt prinditud lõpp-punkt ja täpne mudeli ID:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Või käivitage pakitud rakendus:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Üksainus Java sisenemispunkt on `com.example.Application`. See kuvab valitud
lõpp-punkti, tegeliku mudeli ID, käsu ja genereeritud vastuse, seejärel sulgeb oma Spring
konteksti ja HTTP kliendi. Ebaõnnestunud tuletamine või puuduva vastuseteksti puhul
tagastatakse tõrke väljumiskood, mitte edukujulise kohatäitja.

### Seadistamine

| Keskkonnamuutuja | Vaikimisi | Eesmärk |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP lõpp-punkt, kaasates `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Tühi | Täpne mudeli ID; muidu valitakse üks reklaamitud mudel |
| `FOUNDRY_LOCAL_PROMPT` | Üksiklauseline küsimus kohalike mudelite kohta | Käsus konsooli käitaja poolt saadetav kiri |

Võrdväärsed Spring argumendid on `--foundry.local.base-url=...`,
`--foundry.local.model=...` ja `--foundry.local.prompt=...`.
Ainult loopback HTTP lõpp-punktid on aktsepteeritavad. Kaug- või pilve lõpp-punktid,
manustatud mandaadid, päringujärjestused ja ilma `/v1` path-ta ei ole lubatud.

Tühi mudeli seadistus töötab ainult siis, kui `/v1/models` reklaamib täpselt üht mudelit.
Reklaamitud mudel ei pruugi olla laaditud. Kui mitu mudelit on reklaamitud,
määrake täpne laetud ID asemel, et tugineda kataloogi järjestusele.

Päringud kasutavad `temperature=0`, 150-sõnalist väljundpiirangut, 120-sekundilist time-out’i ja
automaatseid uuesti katseid ei ole. `max_tokens` päringuväli on sihilik:
seda toetab Foundry Local REST leping, kuigi OpenAI Java hakkab seda välja vananenuks tunnistama
uuemate pilve mudelite puhul. Mudeli identiteet tuleb konfiguratsioonist või
avastamisest, mitte mudeli enda ütlustest.

## Otsetestimine

Kui kohalik server töötab, käivitage kõik testid, kaasa arvatud vabatahtlik otsetest.
Asendage lõpp-punkti port oma serveri poolt prinditud väärtusega. Quote'ige punktidega
Maveni omadused PowerShellis:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Otsetest käivitab `Application.main`, esitab fakti "Prantsusmaa pealinn on Pariis," küsib linna nime,
ja kontrollib, et tegelik genereeritud tekst oleks `Paris`. See kontrollib semantilist tulemust,
mitte ainult õnnestunud HTTP staatust.

See on integreerimistest, mitte täpsusvõrdlus. Valideerimise ajal
vastas see 0.5B mudel eraldi "2 + 2" küsimusele vastusega `3` nii Java kui ka otsese RESTi puhul.
Ärge tuginege sellele aritmeetilise või faktitõe kontrolliks ilma iseseisva
kinnitamiseta; kasutage deterministlikke vahendeid arvutusteks.

## Tõrkeotsing

| Sümptom | Kontroll |
| --- | --- |
| Ühendus keelatud | Oodake valmisoleku teadet; kasutage prinditud porti ja `/v1` path-i. |
| Mitmed mudelid reklaamitud | Määrake `FOUNDRY_LOCAL_MODEL` laetud mudeli täpseks ID-ks. |
| Mudel puudub | Kasutage `--list` või lubage allalaadimine käsuga `--download`. |
| GPU pakkuja ebaõnnestub või hangub | Kasutage väikest CPU mudelit. Vahemälus oleva GPU mudeli puhul on vajalik selle pakkuja. |
| CLI püsib staatuses `initializing` | Looge käsk `foundry server logs --lines 80`; peatage daemon ja kasutage SDK abi. |
| NuGet TLS/alla laadimise tõrge | Parandage võrgukonfiguratsioon või kasutage ülal toodud Windows x64 varuplaani. Ärge keelake TLS-i. |
| Port on hõivatud | Kasutage `--port 0` ja seadistage Java prinditud lõpp-punktiga. |
| Puuduvad valikud või tühjad tekstid | Rakendus annab tahtlikult tõrke; kontrollige mudeli ja jooksuaja logisid. |

## Allikad ja viited

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): ühekordne Spring Booti käivitaja.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): tüpitud avastamine ja lokaalsed vestluste täitmised.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP lepingutestid, käivitaja ja otsetestid.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): ametlik SDK REST server koos vahemälumudeli valiku ja puhastusega.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): kinnitatud Windows x64 natiivne jooksuaegne varuplaan.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) ja [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfiguratsioon ja sõltuvused.
- [Foundry Local REST integratsioon](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 väljaanne ja migratsioonimärkmikud](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Praktikad näited](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades AI tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun pange tähele, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega seotud eksimustest või valesti mõistmistest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->