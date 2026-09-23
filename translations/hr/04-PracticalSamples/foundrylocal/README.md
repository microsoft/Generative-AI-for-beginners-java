# Foundry Lokalni Spring Boot Tutorial

Pokrenite mali jezični model na vlastitom računalu i pozovite njegov OpenAI-kompatibilni
REST endpoint iz Java konzolne aplikacije. Nema Azure distribucije, Azure prijave,
cloud API ključa ili cloud izvođenja. **GPT-5.6 Luna je samo za Azure; ne konfigurirajte
ga kao Foundry Local model.**

## Verzije i preduvjeti

| Komponenta | Verzija |
| --- | --- |
| Java | 21 ili novija |
| Maven | 3.6.3 ili noviji |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokalni REST server) | 2.0.1 |
| Node.js (lokalni REST server) | 20 ili noviji |
| Foundry Local CLI (opcionalno, zasebno izdanje) | 0.10.3 preview |

Spring Boot upravlja verzijama Spring Frameworka, Jacksona, JUnita i Maven plugina.
Ovaj primjer koristi OpenAI Java SDK direktno, ne Spring AI. Stari neiskorišteni
Spring AI milestone property i repozitorij uklonjeni su.

Preporučeni početni model je **Qwen 2.5 0.5B**, CPU varijanta
`qwen2.5-0.5b-instruct-generic-cpu:4` (otprilike 822 MB u katalogu).
On izbjegava zahtijevanje GPU izvršne pružatelje. Drugi podržani, keširani mali modeli
mogu se odabrati eksplicitno. Instalacija modela i runtimea zahtijeva mrežni pristup;
upiti i izvođenje ostaju lokalni. Foundry Local i dalje može emitirati minimalnu runtime
dijagnostiku čak i s onemogućenim nevažnim telemetrijskim podacima.

Pokrenite sljedeće naredbe iz ovog direktorija uzoraka.

## Sastavite i testirajte Javu

```powershell
mvn clean verify
```

HTTP ugovorni testovi pokreću privremeni loopback server i vježbaju stvarni
OpenAI Java SDK. Pokrivaju serializaciju zahtjeva, otkrivanje modela, eksplicitni odabir modela,
nejasne ili neispravne liste modela, HTTP pogreške, prazne odgovore,
lokalne URL-ove i propagaciju pogrešaka iz naredbenog retka. Nisu potrebni modeli ili
mrežni pristup osim instalacije Maven ovisnosti. Živi test je prema izboru.

## Pokrenite lokalni model

### Preporučeno: fiksni SDK server

Ne postoji izvorni Foundry Local Java SDK. Mali Node.js pomoćnik hostira
službeni SDK REST server; aplikacija i chat zahtjev ostaju u Javi.

Instalirajte fiksne runtime ovisnosti:

```powershell
npm ci
```

Ako Windows x64 ne može dohvatiti NuGet tijekom nativne instalacije SDK-a, upotrijebite ponuđeni
rezervni način. On preuzima odgovarajući službeni GitHub runtime arhiv, provjerava
SHA-256 digest izdanja i postavlja njegove DLL-ove uz nativni dodatak. Ne
onemogućuje TLS validaciju, ne zahtijeva povišene privilegije niti mijenja SDK izvor.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Prikažite modele već keširane na ovom računalu:

```powershell
npm run start:foundry -- --list
```

Prvi put dopustite preuzimanje malog CPU modela eksplicitno:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Pri sljedećim pokretanjima izostavite `--download` da zahtijevate keširani model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Pomoćnik preferira odgovarajući keširani model, prihvaća alias ili točan ID varijante,
i odbija model ako nedostaje osim ako nije dan `--download`. Registrira samo
izvršnog pružatelja odabranog modela kad je potreban. Keširane GPU varijante i dalje mogu
zahtijevati kompatibilne pakete i upravljačke programe izvršnog pružatelja.

Ako je port 5273 zauzet, proslijedite `--port 0` za slobodan port. Pomoćnik ispisuje
`FOUNDRY_LOCAL_BASE_URL`, točan `FOUNDRY_LOCAL_MODEL` ID i njegov PID kad je spreman.
Koristite ispisanu točku u Javi. Ostavite ovaj terminal otvoren tijekom pokretanja Jave;
**Ctrl+C** zaustavlja REST server i oslobađa model.

Zadani cache je `~/.foundry/cache/models`. Postavite `FOUNDRY_LOCAL_CACHE_DIR` za
drugi postojeći cache. Dnevnici i stanje pomoćnika zapisani su u ovom uzorku
pod direktorijem `target/foundry-local`. Zaustavite pomoćnika prije pokretanja `mvn clean`.

### Opcionalno: Foundry Local CLI

CLI i SDK imaju neovisna izdanja: CLI **0.10.3** uključuje SDK **1.2.4**;
pomoćnik gore koristi SDK **2.0.1**. Instaliranje najnovijeg CLI ne instalira
najnoviji jezični SDK. Pogledajte [CLI release notes](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Na Windowsu, upotrijebite naredbu za instalaciju po korisniku ako CLI nedostaje:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Ili nadogradite postojeću instalaciju:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x zamjenjuje stare `foundry service` naredbe s `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` zahtijeva već preuzeti model. Provjerite `foundry model --help` za
naredbe za preuzimanje. Koristite stvarnu točku iz statusnog izlaza; CLI inače
koristi automatski dodijeljeni port. Nemojte pokretati CLI i SDK pomoćnika
na istom portu. Kad završite:

```powershell
foundry server stop
```

## Pokrenite Java aplikaciju

U drugom terminalu postavite točku i točan ID modela ispisane od vašeg servera:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Ili pokrenite zapakirani program:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Jedina Java ulazna točka je `com.example.Application`. Ispisuje odabranu
točku, stvarni ID modela, upit i generirani odgovor, zatim zatvara svoj Spring
kontekst i HTTP klijent. Neuspjela inferencija ili nedostajući tekst odgovora rezultira
izlazom s greškom umjesto zamjenskim ishodom za uspjeh.

### Konfiguracija

| Varijabla okoline | Zadano | Svrha |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP endpoint, uključujući `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Prazno | Točan ID modela; inače odabire jedini reklamirani model |
| `FOUNDRY_LOCAL_PROMPT` | Jedna rečenica o lokalnim modelima | Upit koji šalje konzolni pokretač |

Ekvivalentni Spring argumenti su `--foundry.local.base-url=...`,
`--foundry.local.model=...` i `--foundry.local.prompt=...`.
Prihvaćaju se samo loopback HTTP točke. Vanjske/cloud točke, ugrađene
vjerodajnice, query stringovi i putanje bez `/v1` odbacuju se.

Prazna postavka modela radi samo kad `/v1/models` reklamira točno jedan model.
Reklamirani model nije nužno učitan. Ako je više modela reklamirano,
postavite točan učitani ID umjesto oslanjanja na redoslijed u katalogu.

Zahtjevi koriste `temperature=0`, limit izlaza od 150 tokena, timeout od 120 sekundi i
nikakve automatske ponovne pokušaje. Polje `max_tokens` u zahtjevu je namjerno:
podržava ga Foundry Local REST ugovor, iako OpenAI Java zastarijeva
to polje za novije cloud modele. Identitet modela dolazi iz konfiguracije ili
otkrivanja, a ne iz tvrdnji modela o samome sebi.

## Živa validacija

Dok lokalni server radi, pokrenite sve testove uključujući i testowa uživo prema izboru.
Zamijenite port endpointa s vrijednošću koju je ispisao vaš server. Navodite točkaste
Maven varijable u PowerShellu:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Živi test poziva `Application.main`, dostavlja činjenicu "Glavni grad
Francuske je Pariz," traži grad i provjerava je li stvarni generirani tekst
`Paris`. Provjerava semantički rezultat, ne samo uspješan HTTP status.

Ovo je integracijska provjera, ne benchmark točnosti. Tijekom validacije, ovaj
0.5B model je odgovorio na poseban upit "2 + 2" s `3` kroz obje Java i direktni
REST. Nemojte se oslanjati na njega za aritmetiku ili faktografsku točnost bez neovisne
provjere; koristite determinističke alate za proračune.

## Rješavanje problema

| Simptom | Provjera |
| --- | --- |
| Veza odbijena | Pričekajte poruku o spremnosti; koristite ispisani port i `/v1` put. |
| Više modela reklamirano | Postavite `FOUNDRY_LOCAL_MODEL` na točan ID učitanog modela. |
| Model nedostaje | Koristite `--list` ili eksplicitno dopustite preuzimanje `--download`. |
| GPU pružatelj ne radi ili zastane | Koristite mali CPU model. Keširani GPU model i dalje treba svog pružatelja. |
| CLI ostaje `initializing` | Pročitajte `foundry server logs --lines 80`; zaustavite servis i koristite SDK pomoćnika. |
| NuGet TLS/preuzimanje neuspjeh | Popravite mrežni pristup ili koristite provjereni Windows x64 rezervni način gore. Ne onemogućujte TLS. |
| Port zauzet | Koristite `--port 0` i konfigurirajte Javu s ispisanom točkom. |
| Nema izbora ili prazan tekst | Aplikacija se namjerno ne uspije; pregledajte dnevnike modela i runtimea. |

## Izvor i reference

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): jednokratni Spring Boot pokretač.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): tipizirano otkrivanje i lokalne chat dovršetke.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP ugovor, pokretač i živi testovi.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): službeni SDK REST server s odabirom keširanog modela i čišćenjem.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): provjereni Windows x64 nativni runtime rezervni način.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) i [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfiguracija i ovisnosti.
- [Foundry Local REST integracija](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 izdanje i bilješke o migraciji](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Poglavlje 04: Praktični primjeri](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati greške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->