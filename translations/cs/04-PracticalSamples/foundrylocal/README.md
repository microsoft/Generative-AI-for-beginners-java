# Foundry Local Spring Boot Tutoriál

Spusťte malý jazykový model na svém vlastním stroji a volejte jeho OpenAI-kompatibilní
REST endpoint z Java konzolové aplikace. Není použito žádné Azure nasazení, přihlášení do Azure,
klíč cloudového API ani cloudové inferenční služby. **GPT-5.6 Luna je pouze pro Azure; nepokoušejte
se jej konfigurovat jako Foundry Local model.**

## Verze a požadavky

| Komponenta | Verze |
| --- | --- |
| Java | 21 nebo novější |
| Maven | 3.6.3 nebo novější |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokální REST server) | 2.0.1 |
| Node.js (lokální REST server) | 20 nebo novější |
| Foundry Local CLI (volitelně, samostatné vydání) | 0.10.3 preview |

Spring Boot spravuje verze Spring Frameworku, Jacksonu, JUnit a Maven pluginů.
Tento příklad používá přímo OpenAI Java SDK, nikoliv Spring AI. Staré nepoužívané
vlastnosti a repositorium Spring AI milestone byly odstraněny.

Doporučený startovací model je **Qwen 2.5 0.5B**, varianta pro CPU
`qwen2.5-0.5b-instruct-generic-cpu:4` (přibližně 822 MB v katalogu).
Vyhýbá se požadavkům na GPU providery. Jiné podporované, kešované malé modely
lze explicitně vybrat. Instalace modelu a runtime vyžaduje síťový přístup;
promptování a inference zůstávají lokální. Foundry Local může i tak emitovat minimální diagnostiku runtime
i při zakázané nepovinné telemetrii.

Spusťte následující příkazy z tohoto ukázkového adresáře.

## Sestavení a testování Java

```powershell
mvn clean verify
```

Testy HTTP kontraktů spustí dočasný loopback server a otestují reálné
OpenAI Java SDK. Pokrývají serializaci dotazů, objevování modelů, explicitní výběr modelu,
nejednoznačné nebo chybné seznamy modelů, HTTP chyby, prázdné odpovědi,
lokální URL, a propagaci chyb přes příkazovou řádku. Nepotřebují model ani
síťový přístup kromě instalace Maven závislostí. Live test je volitelný.

## Spuštění lokálního modelu

### Doporučeno: připnutý SDK server

Neexistuje nativní Foundry Local Java SDK. Malý Node.js pomocník hostuje
oficiální SDK REST server; aplikace a chatové požadavky zůstávají v Javě.

Nainstalujte připnuté runtime závislosti:

```powershell
npm ci
```

Pokud Windows x64 nemůže při nativní instalaci SDK dosáhnout NuGet,
použijte dodanou záložní cestu. Ta stáhne odpovídající oficiální GitHub runtime archiv, zkontroluje
SHA-256 digest vydání a uloží jeho DLL soubory vedle nativního dodatku. Ne
zakáže TLS validaci, nevyžaduje zvýšení oprávnění ani nemění zdroj SDK.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Vypsat modely již kešované na tomto stroji:

```powershell
npm run start:foundry -- --list
```

Při prvním spuštění explicitně povolte stažení malého CPU modelu:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Při následujících spuštěních vynechejte `--download` a vyžadujte kešovaný model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Pomocník preferuje odpovídající kešovaný model, přijímá alias nebo přesné ID varianty,
a odmítá chybějící model, pokud není zadáno `--download`. Registruje pouze
prováděcího providera vybraného modelu, pokud je vyžadován. Kešované GPU varianty mohou
stále potřebovat kompatibilní balíčky poskytovatele a ovladače.

Pokud je port 5273 obsazen, použijte `--port 0` pro dostupný port. Pomocník vytiskne
`FOUNDRY_LOCAL_BASE_URL`, přesné `FOUNDRY_LOCAL_MODEL` ID a jeho PID, až bude připraven.
Použijte vytištěný endpoint v Javě. Nechte tento terminál otevřený během běhu Javy;
**Ctrl+C** zastaví REST server a uvolní model.

Výchozí cache je `~/.foundry/cache/models`. Nastavte `FOUNDRY_LOCAL_CACHE_DIR` pro
jinou existující cache. Logy a stav pomocníka se zapisují pod adresář tohoto příkladu
`target/foundry-local`. Ukončete pomocníka před spuštěním `mvn clean`.

### Volitelně: Foundry Local CLI

CLI a SDK mají nezávislá vydání: CLI **0.10.3** obsahuje SDK **1.2.4**;
výše uvedený pomocník používá SDK **2.0.1**. Instalace nejnovějšího CLI neinstaluje
nejnovější jazykové SDK. Viz [poznámky k vydání CLI](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Na Windows použijte instalační příkaz pro uživatele, pokud CLI chybí:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Nebo aktualizujte existující instalaci:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x nahrazuje staré příkazy `foundry service` příkazy `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` vyžaduje již stažený model. Zkontrolujte `foundry model --help` pro
příkazy ke stažení. Použijte skutečný endpoint výstupu stavu; CLI jinak
používá automaticky přidělený port. Nespouštějte CLI a SDK pomocníka
na stejném portu. Po dokončení:

```powershell
foundry server stop
```

## Spuštění Java aplikace

V druhém terminálu nastavte endpoint a přesné modelové ID vytištěné vaším serverem:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Nebo spusťte zabalenou aplikaci:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Jediný vstupní bod Javy je `com.example.Application`. Vytiskne vybraný
endpoint, skutečné modelové ID, prompt a vygenerovanou odpověď, poté zavře svůj Spring
kontext a HTTP klienta. Selhání inference nebo chybějící odpověď produkuje
chybový ukončovací stav místo úspěšného zástupného výsledku.

### Konfigurace

| Proměnná prostředí | Výchozí | Účel |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP endpoint, včetně `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Prázdné | Přesné modelové ID; jinak vybere jediný oznamovaný model |
| `FOUNDRY_LOCAL_PROMPT` | Jedna věta otázky o lokálních modelech | Prompt poslaný konzolovým runnerem |

Ekvivalentní Spring argumenty jsou `--foundry.local.base-url=...`,
`--foundry.local.model=...` a `--foundry.local.prompt=...`.
Přijímány jsou pouze loopback HTTP endpointy. Vzdálené/cloudové endpointy, vložené
přihlašovací údaje, query stringy a cesty bez `/v1` jsou odmítnuty.

Prázdné nastavení modelu funguje pouze pokud `/v1/models` oznamuje přesně jeden model.
Oznamovaný model není nutně načtený. Pokud je oznamováno více modelů,
nastavte přesné načtené ID namísto spoléhání se na pořadí v katalogu.

Požadavky používají `temperature=0`, limit výstupu 150 tokenů, timeout 120 sekund a
žádné automatické opakování. Pole požadavku `max_tokens` je záměrné: je
podporováno Foundry Local REST kontraktem, ačkoli OpenAI Java toto pole deprecatuje
u novějších cloudových modelů. Identita modelu pochází z konfigurace nebo
z objevování, ne z tvrzení modelu o sobě.

## Živá validace

Se spuštěným lokálním serverem spusťte všechny testy včetně volitelného živého testu.
Nahraďte port endpointu hodnotou vytištěnou vaším serverem. V PowerShellu uvozujte tečkované
Maven vlastnosti:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Živý test vyvolá `Application.main`, předá informaci "Hlavní město
Francie je Paříž," zeptá se na město a ověří, že skutečný generovaný text je
`Paříž`. Kontroluje sémantický výsledek, ne jen úspěšný HTTP stav.

Toto je integrační kontrola, ne test přesnosti. Během validace tato
0.5B model odpověděl na samostatný prompt "2 + 2" číslem `3` přes jak Javu, tak přímo
REST. Nespoléhejte se na něj pro aritmetiku či faktickou přesnost bez nezávislé
verifikace; pro výpočty používejte deterministické nástroje.

## Řešení problémů

| Příznak | Kontrola |
| --- | --- |
| Připojení odmítnuto | Počkejte na zprávu o připravenosti; použijte vytištěný port a `/v1` cestu. |
| Více oznamovaných modelů | Nastavte `FOUNDRY_LOCAL_MODEL` na přesné ID načteného modelu. |
| Model chybí | Použijte `--list`, nebo explicitně povolte stažení s `--download`. |
| GPU provider selhává nebo zamrzá | Použijte malý CPU model. Kešovaný GPU model stále potřebuje svého providera. |
| CLI zůstává ve stavu `initializing` | Přečtěte `foundry server logs --lines 80`; zastavte démona a použijte SDK pomocníka. |
| Chyba NuGet TLS/stahování | Opravte síťový přístup nebo použijte ověřenou záložní cestu Windows x64 výše. Nezakazujte TLS. |
| Port obsazen | Použijte `--port 0` a nakonfigurujte Javu s vytištěným endpointem. |
| Žádné volby nebo prázdný text | Aplikace selže úmyslně; prohlédněte modelové a runtime logy. |

## Zdrojové kódy a odkazy

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): jednorázový Spring Boot běžec.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typová objevování a lokální chatové dokončování.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP kontrakty, běžec a živé testy.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): oficiální SDK REST server s výběrem kešovaného modelu a úklidem.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): ověřená Windows x64 nativní fallback runtime.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) a [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfigurace a závislosti.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o co největší přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->