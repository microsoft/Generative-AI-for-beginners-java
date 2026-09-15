# Foundry Local Spring Boot útmutató

Futtass egy kis nyelvi modellt a saját gépeden, és hívd annak OpenAI-kompatibilis
REST végpontját Java konzolalkalmazásból. Nincs Azure telepítés, Azure bejelentkezés,
felhő API kulcs vagy felhőbeli következtetés. **A GPT-5.6 Luna kizárólag Azure-hoz készült; ne
konfiguráld Foundry Local modellként.**

## Verziók és előfeltételek

| Komponens | Verzió |
| --- | --- |
| Java | 21 vagy újabb |
| Maven | 3.6.3 vagy újabb |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (helyi REST szerver) | 2.0.1 |
| Node.js (helyi REST szerver) | 20 vagy újabb |
| Foundry Local CLI (opcionális, külön kiadás) | 0.10.3 előnézet |

A Spring Boot kezeli a Spring Framework, Jackson, JUnit és Maven plugin verziókat.
Ez a példa közvetlenül az OpenAI Java SDK-t használja, nem a Spring AI-t. A régi, nem használt
Spring AI mérföldkő tulajdonság és tárhely törölve lett.

Az ajánlott induló modell a **Qwen 2.5 0.5B**, CPU változat
`qwen2.5-0.5b-instruct-generic-cpu:4` (körülbelül 822 MB a katalógusban).
Ez elkerüli a GPU végrehajtó szolgáltatók szükségességét. Más támogatott, gyorsítótárazott kis modellek
is választhatók kifejezetten. A modell és a futtatókörnyezet telepítése hálózati hozzáférést igényel;
a promptok és a következtetés helyi marad. A Foundry Local még akkor is kibocsáthat minimális futtatókörnyezet
diagnosztikát, ha a lényegtelen telemetria le van tiltva.

Futtasd az alábbi parancsokat ebből a mintakönyvtárból.

## Java építése és tesztelése

```powershell
mvn clean verify
```

A HTTP szerződés tesztek egy ideiglenes loopback szervert indítanak el, és az OpenAI Java SDK-t gyakorlatban is tesztelik.
Lefedik a kérés sorosítását, modellfelfedezést, expliciten kiválasztott modellt,
kétértelmű vagy hibás modellijegyzékeket, HTTP hibákat, üres válaszokat,
kizárólag helyi URL-eket és a parancssori hiba átvitelét. Ehhez nincs szükség modellre vagy
hálózati hozzáférésre a Maven függőségek telepítésén kívül. Az élő teszt opcionális.

## Indítsd el a helyi modellt

### Ajánlott: rögzített (pinned) SDK szerver

Nincs natív Foundry Local Java SDK. A kis Node.js segéd futtatja az
hivatalos SDK REST szerverét; az alkalmazás és a chat kérés Java marad.

Telepítsd a rögzített futtatókörnyezet-függőségeket:

```powershell
npm ci
```

Ha Windows x64 alatt nem érhető el a NuGet az SDK natív telepítése közben, használd a mellékelt
tartalék megoldást. Letölti a megfelelő hivatalos GitHub futtatókörnyezet archívumot, ellenőrzi a
kiadás SHA-256 ellenőrző összegét, és a DLL-eket a natív addon mellé helyezi. Nem
kapcsolja ki a TLS érvényesítést, nem igényel adminisztrátori jogosultságot, és nem módosítja az SDK forrását.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Listázd az ezen a gépen már gyorsítótárazott modelleket:

```powershell
npm run start:foundry -- --list
```

Első futtatáskor engedélyezd kifejezetten a kis CPU modell letöltését:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Az utána következő futásokon hagyd el a `--download` opciót, ez esetben gyorsítótárazott modell kell:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

A segéd előnyben részesíti a megfelelő gyorsítótárazott modellt, elfogad aliasokat vagy pontos változat ID-kat,
és megtagadja a hiányzó modellt, hacsak nem adsz meg `--download` lehetőséget. Csak a
kiválasztott modell végrehajtó szolgáltatóját regisztrálja, ha szükséges. A gyorsítótárazott GPU változatok
továbbra is igényelhetik a kompatibilis végrehajtó csomagokat és illesztőprogramokat.

Ha a 5273-as port foglalt, add meg a `--port 0` opciót egy szabad portért. A segéd kiírja a
`FOUNDRY_LOCAL_BASE_URL`-t, a pontos `FOUNDRY_LOCAL_MODEL` ID-t és a PID-jét indításkor.
Használd a kiírt végpontot Java-ban. Hagyd nyitva ezt a terminált Java futtatásához;
a **Ctrl+C** leállítja a REST szervert és felszabadítja a modellt.

Az alapértelmezett gyorsítótár `~/.foundry/cache/models`. Állítsd be a `FOUNDRY_LOCAL_CACHE_DIR` változót
egy másik, létező gyorsítótár helyre. A naplók és a segéd állapota ebben a minta `target/foundry-local`
könyvtárban íródnak. Állítsd le a segédet, mielőtt `mvn clean` parancsot futtatnál.

### Opcionális: Foundry Local CLI

A CLI és az SDK külön kiadásokban jelenik meg: a CLI **0.10.3** az SDK **1.2.4**-et tartalmazza;
a fenti segéd SDK verziója **2.0.1**. A legújabb CLI telepítése nem jelenti a
legújabb nyelvi SDK telepítését. Lásd a [CLI kiadási megjegyzések](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Windows rendszeren használd a felhasználónkénti telepítési parancsot, ha a CLI hiányzik:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Vagy frissíts egy meglévő telepítést:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

A CLI 0.10.x lecseréli a régi `foundry service` parancsokat `foundry server`-re:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

A `model load` parancs már letöltött modellt igényel. Nézd meg a `foundry model --help`-et a
letöltési parancsokért. Használd a státusz kimenetén megjelenő pontos végpontot; a CLI máskülönben
automatikusan kiosztott portot használ. Ne indítsd egyszerre a CLI-t és az SDK segédet
ugyanazon a porton. Befejezéskor:

```powershell
foundry server stop
```

## A Java alkalmazás futtatása

Egy másik terminálban állítsd be a szervered által kiírt végpontot és pontos modell ID-t:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Vagy futtasd a csomagolt alkalmazást:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Egyetlen Java belépési pont a `com.example.Application`. Kiírja a kiválasztott
végpontot, a tényleges modell ID-t, a promptot és a generált választ, majd lezárja a Spring
környezetet és az HTTP klienst. Sikertelen következtetés vagy hiányzó válasz szöveg
hiba kilépést eredményez, nem siker alakú helyőrzőt.

### Konfiguráció

| Környezeti változó | Alapértelmezett | Célja |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP végpont, beleértve a `/v1`-et |
| `FOUNDRY_LOCAL_MODEL` | Üres | Pontos modell ID; egyébként az egyetlen hirdetett modell választódik |
| `FOUNDRY_LOCAL_PROMPT` | Egy mondatos kérdés a helyi modellekről | A konzolos futtató által küldött prompt |

Egyenértékű Spring argumentumok: `--foundry.local.base-url=...`,
`--foundry.local.model=...`, és `--foundry.local.prompt=...`.
Csak loopback HTTP végpontok fogadhatók el. Távoli/felhő végpontok, beágyazott
hitelesítő adatok, lekérdezési láncok és `/v1` nélküli útvonalak elutasításra kerülnek.

Az üres modell beállítás csak akkor működik, ha `/v1/models` pontosan egy modellt hirdet.
Egy hirdetett modell nem feltétlenül van betöltve. Ha több modell hirdetésre kerül,
állítsd be a pontosan betöltött ID-t ahelyett, hogy a katalógus sorrendjére hagyatkoznál.

A kérések `temperature=0`, 150 tokenes kimenetlimit, 120 másodperces időkorlát és
automatikus újrapróbálkozás nélküliek. A `max_tokens` kérés mező szándékos: támogatja a
Foundry Local REST szerződés, bár az OpenAI Java ezt a mezőt elavultnak jelöli
újabb felhő modellek esetében. A modellazonosság a konfigurációból vagy
felfedezésből ered, nem a modell önmagáról szóló állításaiból.

## Élő validáció

A helyi szerver futtatása mellett, futtass minden tesztet beleértve az opcionális élő tesztet is.
Cseréld ki a végpont portját a szerver által kiírt értékre. Powershell-ben idézd a pontokkal tagolt
Maven tulajdonságokat:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Az élő teszt meghívja az `Application.main`-t, megadja a tényt „Franciaország fővárosa Párizs,”,
megkérdezi a várost, és az aktuálisan generált szöveg értékét ellenőrzi, amely legyen
`Párizs`. Szemantikus eredményt vizsgál, nem csak sikeres HTTP státuszt.

Ez egy integrációs ellenőrzés, nem pontossági benchmark. Érvényesítés alatt ez
0.5B modell külön „2 + 2” promptot a `3` válasszal adott mind Java, mind közvetlen
REST használatával. Nem szabad arra támaszkodni, hogy pontosan számol vagy tényeket mond helyesen független
ellenőrzés nélkül; számításokra használj determinisztikus eszközöket.

## Hibakeresés

| Tünet | Ellenőrizd |
| --- | --- |
| Kapcsolat elutasítva | Várd meg a kész üzenetet; használd a kiírt portot és a `/v1` útvonalat. |
| Több modell hirdetve | Állítsd be a `FOUNDRY_LOCAL_MODEL`-t a betöltött modell pontos ID-jára. |
| Modell hiányzik | Használd a `--list`-et, vagy engedélyezd a letöltést `--download`-dal. |
| GPU szolgáltató hibádzik vagy akad | Használd a kis CPU modellt. A gyorsítótárazott GPU modell még mindig igényli szolgáltatóját. |
| CLI állapota `initializing` marad | Olvasd a `foundry server logs --lines 80`-at; állítsd le a daemont és használd az SDK segédet. |
| NuGet TLS/letöltés hiba | Javítsd a hálózati hozzáférést vagy használd a fenti ellenőrzött Windows x64 tartalék megoldást. Ne tiltsd le a TLS-t. |
| Port foglalt | Használd a `--port 0` opciót és konfiguráld Java-ban a kiírt végpontot. |
| Nincsenek válaszok vagy üres szöveg | Az app szándékosan hibát jelez; nézd meg a modell és futtatókörnyezet naplóit. |

## Forrás és hivatkozások

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): egylövetű Spring Boot futtató.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): tipizált felfedezés és helyi chat befejezések.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP szerződés, futtató és élő tesztek.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): hivatalos SDK REST szerver gyorsítótárazott modell választással és tisztítással.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): ellenőrzött Windows x64 natív futtatókörnyezet tartalék.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), és [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfigurációk és függőségek.
- [Foundry Local REST integráció](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 kiadás és migrációs megjegyzések](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [04. fejezet: Gyakorlati példák](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->