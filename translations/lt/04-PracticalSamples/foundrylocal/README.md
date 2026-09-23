# Foundry Local Spring Boot vadovėlis

Paleiskite mažą kalbos modelį savo kompiuteryje ir kvieskite jo OpenAI suderinamą
REST tašką iš Java konsolės programos. Nenaudojama Azure diegimas, Azure prisijungimas,
debesies API raktas ar debesies inferencija. **GPT-5.6 Luna yra tik Azure; ne
konfigūruokite jo kaip Foundry Local modelio.**

## Versijos ir reikalavimai

| Komponentas | Versija |
| --- | --- |
| Java | 21 arba naujesnė |
| Maven | 3.6.3 arba naujesnė |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokalus REST serveris) | 2.0.1 |
| Node.js (lokalus REST serveris) | 20 arba naujesnė |
| Foundry Local CLI (pasirinktinai, atskira versija) | 0.10.3 peržiūra |

Spring Boot valdo Spring Framework, Jackson, JUnit ir Maven įskiepių versijas.
Šis pavyzdys naudoja tiesiogiai OpenAI Java SDK, o ne Spring AI. Senasis nenaudojamas
Spring AI milestone atributas ir saugykla buvo pašalinti.

Rekomenduojamas pradinio modelio pasirinkimas yra **Qwen 2.5 0.5B**, CPU variantas
`qwen2.5-0.5b-instruct-generic-cpu:4` (apie 822 MB kataloge).
Jis nereikalauja GPU vykdymo teikėjų. Kiti palaikomi, talpykloje laikomi maži modeliai
gali būti pasirinkti tiesiogiai. Modelio ir vykdymo aplinkos diegimas reikalauja tinklo prieigos;
užklausos ir inferencija lieka vietinėse sąlygose. Foundry Local vis tiek gali pateikti minimalias vykdymo
diagnostikas net jei nereikšminga telemetrija išjungta.

Vykdykite šias komandas iš šio pavyzdžio katalogo.

## Surinkite ir išbandykite Java

```powershell
mvn clean verify
```

HTTP sutarties testai paleidžia trumpalaikį loopback serverį ir patikrina tikrą OpenAI Java SDK.
Jie apima užklausų serializavimą, modelio paiešką, aiškų modelio
pasirinkimą, dviprasmiškas ar netaisyklingas modelių sąrašas, HTTP klaidas, tuščius atsakymus,
tik vietines URL, ir komandų eilutės klaidų perdavimą. Jie nereikalauja modelio ar
tinklo prieigos, išskyrus Maven priklausomybių įdiegimą. Tiesioginis testas yra pasirenkamas.

## Paleiskite vietinį modelį

### Rekomenduojama: prisegtas SDK serveris

Nėra natūralaus Foundry Local Java SDK. Mažas Node.js pagalbininkas talpina oficialaus SDK REST serverį;
programa ir pokalbio užklausa išlieka Java.

Įdiekite prisegtas vykdymo aplinkos priklausomybes:

```powershell
npm ci
```

Jei Windows x64 negali pasiekti NuGet SDK gimtojo įdiegimo metu, naudokite pateiktą
atsarginę parinktį. Ji atsisiunčia atitinkamą oficialų GitHub vykdymo archyvą,
patikrina leidimo SHA-256 kontrolinį sumą ir įdeda jo DLL šalia gauto papildo. Ji
neišjungia TLS patikros, nereikalauja administratoriaus teisų ar nesikeičia SDK šaltinio kodo.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Parodykite modelius, jau talpykloje esamus šioje mašinoje:

```powershell
npm run start:foundry -- --list
```

Pirmą kartą paleidus, aiškiai leiskite parsisiųsti mažą CPU modelį:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Vėlesnių paleidimų metu praleiskite `--download`, kad reikėtų turimą talpykloje modelį:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Pagalbininkas labiau pageidauja atitinkamą talpykloje esantį modelį, priima slapyvardį arba tikslią varianto ID,
ir atsisako trūkstamo modelio, nebent pateikiamas `--download`. Jis registruoja tik
pasirinkto modelio vykdymo teikėją, kai to reikia. Laikyti GPU variantai vis tiek gali
reikėti suderinamų vykdymo teikėjo paketus ir tvarkykles.

Jei prievadas 5273 užimtas, perduokite `--port 0` kitam laisvam prievadui. Pagalbinė programa atspausdina
`FOUNDRY_LOCAL_BASE_URL`, tikslų `FOUNDRY_LOCAL_MODEL` ID ir jo PID, kai yra paruošta.
Naudokite atspausdintą galinį tašką Java. Palikite šį terminalą atidarytą paleidžiant Java;
**Ctrl+C** sustabdo REST serverį ir atlaisvina modelį.

Numatytoji talpykla yra `~/.foundry/cache/models`. Nustatykite `FOUNDRY_LOCAL_CACHE_DIR` kitai
egzistuojančiai talpyklai. Žurnalai ir pagalbinės būsenos rašomi šio pavyzdžio
`target/foundry-local` kataloge. Sustabdykite pagalbinę programą prieš vykdant `mvn clean`.

### Pasirinktinai: Foundry Local CLI

CLI ir SDK turi nepriklausomus leidimus: CLI **0.10.3** įtraukia SDK **1.2.4**;
aukščiau pateikta pagalbinė programa naudoja SDK **2.0.1**. Įdiegus naujausią CLI neįdiegiama
naujausia kalbos SDK versija. Žr. [CLI leidimo pastabas](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

„Windows“ naudokite vartotojui skirtą diegimo komandą, jei CLI nėra įdiegta:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Arba atnaujinkite esamą diegimą:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x pakeičia senas `foundry service` komandas į `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` reikia jau atsisiųsto modelio. Peržiūrėkite `foundry model --help` dėl
atsisiuntimo komandų. Naudokite būsenos išvesties tikrą galinį tašką; kitaip CLI
naudoja automatiškai priskirtą prievadą. Nepradėkite CLI ir SDK pagalbinės programos
tame pačiame prievade. Baigus:

```powershell
foundry server stop
```

## Paleiskite Java programą

Antrame terminale nustatykite galinį tašką ir tikslų modelio ID, atspausdintus jūsų serveryje:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Arba paleiskite supakuotą programą:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Vienintelis Java įėjimo taškas yra `com.example.Application`. Jis atspausdina pasirinktą
galinį tašką, tikslų modelio ID, komandą ir sugeneruotą atsakymą, tada uždaro savo Spring
kontekstą ir HTTP klientą. Nepavykusi išvada arba trūkstamas atsakymo tekstas sukuria
klaidos išeitį, o ne sėkmės formos vietos rezervavimo ženklą.

### Konfigūracija

| Aplinkos kintamasis | Numatytoji reikšmė | Paskirtis |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP galinis taškas, įskaitant `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Tuščias | Tikslus modelio ID; kitaip pasirinktas vienintelis reklamuojamas modelis |
| `FOUNDRY_LOCAL_PROMPT` | Vienos sakinio klausimas apie vietinius modelius | Komanda, siunčiama per konsolės vykdyklę |

Lygiavertės Spring argumentai yra `--foundry.local.base-url=...`,
`--foundry.local.model=...` ir `--foundry.local.prompt=...`.
Priimami tik loopback HTTP galiniai taškai. Nuotoliniai/debesų galiniai taškai, įterpti
kredencialai, užklausų eilutės ir keliai be `/v1` nepriimami.

Tuščias modelio nustatymas veikia tik tada, kai `/v1/models` reklamuoja tiksliai vieną modelį.
Reklamuojamas modelis nebūtinai yra įkeltas. Jei reklamuojama keli modeliai,
nurodykite tikslų įkeltą ID, o ne pasikliaukite katalogo tvarka.

Užklausos naudoja `temperature=0`, 150 simbolių išvesties limitą, 120 sekundžių timeout ir
neautomatinius pakartojimus. `max_tokens` užklausos laukas yra tyčinis: jis yra
palaikoma Foundry vietinio REST kontrakto, nors OpenAI Java neberekomenduoja
tas laukas naujesniems debesų modeliams. Modelio tapatybė gaunama iš konfigūracijos arba
aptikimo, o ne iš modelio teiginių apie save.

## Gyvas tikrinimas

Paleidus vietinį serverį, vykdykite visus testus, įskaitant pasirenkamą gyvą testą.
Pakeiskite galinio taško prievadą į jūsų serverio išvestą reikšmę. Citavimas taškinių
Maven savybių PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Gyvas testas iškviečia `Application.main`, pateikia faktą „Prancūzijos sostinė yra Paryžius“,
prašo miestą ir tvirtina, kad faktiškai sugeneruotas tekstas yra `Paryžius`. Jis tikrina
semantinį rezultatą, o ne tik sėkmingą HTTP būseną.

Tai integracijos patikra, o ne tikslumo standartas. Patvirtinimo metu šis
0.5B modelis į atskirą „2 + 2“ užklausą per Java ir tiesioginį
REST atsakė `3`. Nesinaudokite juo aritmetikai ar faktiniam tikslumui be nepriklausomo
patikrinimo; naudokite deterministinius įrankius skaičiavimams.

## Problemų sprendimas

| Simptomas | Patikrinkite |
| --- | --- |
| Prijungimas atmestas | Palaukite paruošimo pranešimo; naudokite spausdintą prievadą ir `/v1` kelią. |
| Reklamuojama daugybė modelių | Nustatykite `FOUNDRY_LOCAL_MODEL` į įkelto modelio tikslią ID. |
| Modelis nerastas | Naudokite `--list` arba aiškiai leiskite atsisiuntimą su `--download`. |
| GPU teikėjas nepavyksta arba užstrigo | Naudokite mažą CPU modelį. Talpykloje esantis GPU modelis vis tiek reikalauja teikėjo. |
| CLI lieka „inicializuojamas“ | Perskaitykite `foundry server logs --lines 80`; sustabdykite demoną ir naudokite SDK pagalbą. |
| NuGet TLS/atsisiuntimo klaida | Sutvarkykite tinklo prieigą arba naudokite aukščiau patikrintą Windows x64 atsarginį variantą. Neduokite išjungti TLS. |
| Prievadas užimtas | Naudokite `--port 0` ir konfigūruokite Java su išspausdintu galiniu tašku. |
| Nėra pasirinkimų arba tuščias tekstas | Programa sąmoningai nesėkminga; patikrinkite modelio ir vykdymo žurnalus. |

## Šaltiniai ir nuorodos

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): vienkartinis Spring Boot paleidėjas.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): tipizuotas aptikimas ir vietinės pokalbių užbaigtys.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP sutartis, paleidėjas ir gyvieji testai.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): oficialus SDK REST serveris su talpyklos modelio pasirinkimu ir valymu.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): patikrintas Windows x64 vietinis vykdymo laikotarpio atsarginis variantas.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) ir [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfigūracija ir priklausomybės.
- [Foundry Local REST integracija](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 leidimo ir migracijos pastabos](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [4 skyrius: praktiniai pavyzdžiai](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kilusią naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->