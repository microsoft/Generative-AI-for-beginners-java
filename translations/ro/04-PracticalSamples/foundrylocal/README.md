# Tutorial Foundry Local Spring Boot

Rulează un model mic de limbaj pe propria ta mașină și apelează punctul său final REST compatibil OpenAI
dintr-o aplicație Java de consolă. Nu se folosește niciun deployment Azure, autentificare Azure,
cheie API cloud sau inferență în cloud. **GPT-5.6 Luna este doar pentru Azure; nu
îl configura ca model Foundry Local.**

## Versiuni și prerechizite

| Componentă | Versiune |
| --- | --- |
| Java | 21 sau ulterior |
| Maven | 3.6.3 sau ulterior |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (server REST local) | 2.0.1 |
| Node.js (server REST local) | 20 sau ulterior |
| Foundry Local CLI (opțional, release separat) | 0.10.3 preview |

Spring Boot gestionează versiunile Spring Framework, Jackson, JUnit și pluginurile Maven.
Acest exemplu folosește direct OpenAI Java SDK, nu Spring AI. Proprietatea și repository-ul vechi,
neutilizate, milestone Spring AI au fost eliminate.

Modelul recomandat inițial este **Qwen 2.5 0.5B**, varianta CPU
`qwen2.5-0.5b-instruct-generic-cpu:4` (aproximativ 822 MB în catalog).
Evită necesitatea unui furnizor de execuție GPU. Alte modele mici suportate și memorate în cache
pot fi selectate explicit. Instalarea modelului și rularea necesită acces la rețea;
prompturile și inferența rămân locale. Foundry Local poate totuși să emită diagnostice minime
de runtime chiar și cu telemetria neesențială dezactivată.

Rulează comenzile următoare din acest director de exemplu.

## Construiește și testează Java

```powershell
mvn clean verify
```

Testele HTTP contract pornesc un server loopback efemer și exercită efectiv
OpenAI Java SDK. Acoperă serializarea cererilor, descoperirea modelului, selecția explicită a modelului,
listele ambigue sau invalid formate de modele, eșecurile HTTP, răspunsurile goale,
URL-urile doar locale și propagarea eșecurilor din linia de comandă. Nu au nevoie de model sau
acces la rețea dincolo de instalarea dependențelor Maven. Testul live este opțional.

## Pornește modelul local

### Recomandat: server SDK fixat

Nu există un SDK Java nativ Foundry Local. Helper-ul mic Node.js găzduiește
serverul REST oficial SDK; aplicația și cererea chat rămân Java.

Instalează dependențele runtime fixate:

```powershell
npm ci
```

Dacă Windows x64 nu poate accesa NuGet în timpul instalării native a SDK-ului, folosește fallback-ul furnizat.
Acesta descarcă arhiva runtime oficială corespunzătoare de pe GitHub, verifică
digestul SHA-256 al release-ului și poziționează DLL-urile lângă addon-ul nativ. Nu
dezactivează validarea TLS, nu cere drepturi de utilizator elevate și nu modifică sursele SDK.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Listează modelele deja memorate în cache pe această mașină:

```powershell
npm run start:foundry -- --list
```

La prima rulare, permite explicit descărcarea modelului mic CPU:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

La rulările ulterioare, omite `--download` pentru a cere un model memorat în cache:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Helper-ul preferă un model memorat în cache corespunzător, acceptă un alias sau ID variantă exactă,
și refuză un model lipsă decât dacă este specificat `--download`. Înregistrează doar
furnizorul de execuție al modelului selectat când este necesar. Variantele GPU memorate în cache pot
avea totuși nevoie de pachete și drivere furnizor compatibile.

Dacă portul 5273 este ocupat, transmite `--port 0` pentru un port disponibil. Helper-ul afișează
`FOUNDRY_LOCAL_BASE_URL`, ID-ul exact `FOUNDRY_LOCAL_MODEL` și PID-ul său când este gata.
Utilizează punctul final afișat în Java. Lasă acest terminal deschis în timp ce rulezi Java;
**Ctrl+C** oprește serverul REST și eliberează modelul.

Cache-ul implicit este `~/.foundry/cache/models`. Setează `FOUNDRY_LOCAL_CACHE_DIR` pentru un
cache diferit existent. Jurnalele și starea helper-ului sunt scrise în directorul
`target/foundry-local` al acestui exemplu. Oprește helper-ul înainte de a rula `mvn clean`.

### Opțional: Foundry Local CLI

CLI-ul și SDK-ul au lansări independente: CLI **0.10.3** include SDK **1.2.4**;
helper-ul de mai sus utilizează SDK **2.0.1**. Instalarea celei mai recente CLI nu instalează
cea mai recentă versiune a SDK-ului de limbaj. Consultă [notițele de lansare CLI](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Pe Windows, folosește comanda de instalare per utilizator dacă CLI-ul lipsește:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Sau actualizează o instalare existentă:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x înlocuiește vechile comenzi `foundry service` cu `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` necesită un model deja descărcat. Verifică `foundry model --help` pentru
comenzile de descărcare. Folosește în mod efectiv punctul final din ieșirea statusului; CLI-ul
altfel utilizează implicit un port atribuit automat. Nu porni CLI-ul și helper-ul SDK
pe același port. Când termini:

```powershell
foundry server stop
```

## Rulează aplicația Java

Într-un al doilea terminal, setează punctul final și ID-ul exact al modelului afișat de server:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Sau rulează aplicația împachetată:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Punctul unic de intrare Java este `com.example.Application`. Acesta afișează punctul final selectat,
ID-ul actual al modelului, promptul și răspunsul generat, apoi închide contextul Spring
și clientul HTTP. O inferență eșuată sau lipsa textului de răspuns generează o
ieșire cu eșec în loc de un placeholder în formă de succes.

### Configurare

| Variabilă de mediu | Implicit | Scop |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Endpoint HTTP loopback, inclusiv `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Gol | ID-ul exact al modelului; altfel selectează singurul model anunțat |
| `FOUNDRY_LOCAL_PROMPT` | O întrebare de o propoziție despre modelele locale | Prompt trimis de consola runner |

Argumetele echivalente în Spring sunt `--foundry.local.base-url=...`,
`--foundry.local.model=...` și `--foundry.local.prompt=...`.
Sunt acceptate doar endpoint-urile HTTP loopback. Endpoint-urile de la distanță/cloud, 
acreditările încorporate, șirurile de interogare și căile fără `/v1` sunt refuzate.

O setare de model goală funcționează numai atunci când `/v1/models` anunță exact un model.
Un model anunțat nu este neapărat încărcat. Dacă sunt anunțate mai multe modele,
setează ID-ul exact încărcat în loc să te bazezi pe ordinea din catalog.

Cererile folosesc `temperature=0`, o limită de ieșire de 150 de tokeni, un timeout de 120 secunde și
fără reîncercări automate. Câmpul cererii `max_tokens` este intenționat: el este
acceptat de contractul Foundry Local REST, deși OpenAI Java marchează ca învechit
acel câmp pentru modelele cloud mai noi. Identitatea modelului provine din configurație sau
descoperire, nu din afirmațiile modelului despre sine.

## Validare în timp real

Cu serverul local rulând, executați toate testele inclusiv testul live opțional.
Înlocuiți portul endpointului cu valoarea afișată de serverul dvs. Puneți între ghilimele
proprietățile Maven cu punct în PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Testul live apelează `Application.main`, furnizează faptul "Capitala Franței este Paris,"
cere orașul și verifică că textul generat este efectiv
`Paris`. Verifică un rezultat semantic, nu doar un cod de stare HTTP reușit.

Aceasta este o verificare de integrare, nu un reper de acuratețe. În timpul validării,
modelul 0.5B a răspuns la un prompt separat „2 + 2” cu `3` atât prin Java, cât și prin REST direct.
Nu vă bazați pe el pentru acuratețe aritmetică sau factuală fără o verificare independentă;
folosiți unelte deterministe pentru calcule.

## Depanare

| Simptom | Verificare |
| --- | --- |
| Conexiune respinsă | Așteptați mesajul ready; folosiți portul afișat și calea `/v1`. |
| Mai multe modele afișate | Setează `FOUNDRY_LOCAL_MODEL` la ID-ul exact al modelului încărcat. |
| Modelul lipsește | Folosiți `--list` sau permiteți descărcarea explicit cu `--download`. |
| Providerul GPU eșuează sau se blochează | Folosiți modelul CPU mic. Modelul GPU din cache încă are nevoie de provider. |
| CLI rămâne `initializing` | Citiți `foundry server logs --lines 80`; opriți daemon-ul și folosiți ajutorul SDK. |
| Eșec TLS/descărcare NuGet | Corectați accesul la rețea sau folosiți fallback-ul verificat Windows x64 de mai sus. Nu dezactivați TLS. |
| Port ocupat | Folosiți `--port 0` și configurați Java cu endpointul afișat. |
| Fără alegeri sau text gol | Aplicația eșuează intenționat; verificați modelul și jurnalele runtime. |

## Sursă și referințe

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): runner Spring Boot one-shot.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): descoperire tipizată și completări de chat locale.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): contract HTTP, runner și teste live.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): server REST oficial SDK cu selecție și curățare de modele cache.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): fallback runtime nativ verificat Windows x64.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) și [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): configurație și dependențe.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->