# Foundry Local Spring Boot Tutorial

Spustite malý jazykový model na vlastnom počítači a volajte jeho OpenAI-kompatibilnú
REST koncovku z konzolovej aplikácie v Jave. Nie je potrebné nasadenie Azure, prihlásenie do Azure,
cloudový API kľúč ani cloudové inferencie. **GPT-5.6 Luna je len pre Azure; nekonfigurujte ho ako Foundry Local model.**


## Verzie a požiadavky

| Komponent | Verzia |
| --- | --- |
| Java | 21 alebo novšia |
| Maven | 3.6.3 alebo novšia |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokálny REST server) | 2.0.1 |
| Node.js (lokálny REST server) | 20 alebo novšia |
| Foundry Local CLI (voliteľné, samostatné vydanie) | 0.10.3 preview |

Spring Boot spravuje verzie Spring Framework, Jackson, JUnit a Maven pluginov.
Tento príklad používa priamo OpenAI Java SDK, nie Spring AI. Staré nepoužívané
vlastnosti a repozitár Spring AI milestone boli odstránené.

Odporúčaný štartovací model je **Qwen 2.5 0.5B**, CPU varianta
`qwen2.5-0.5b-instruct-generic-cpu:4` (približne 822 MB v katalógu).
Vyhýba sa požiadavkám na GPU poskytovateľov vykonávania. Iné podporované, uložené malé modely
je možné zvoliť explicitne. Inštalácia modelov a runtime vyžaduje prístup k sieti;
požiadavky a inferencie zostávajú lokálne. Foundry Local môže stále vydávať minimálnu
diagnostiku runtime aj pri neesenciálnej deaktivácii telemetrie.

Spustite nasledujúce príkazy z tohto vzorového adresára.

## Skompilujte a otestujte Javou

```powershell
mvn clean verify
```

HTTP kontraktné testy spúšťajú dočasný loopback server a testujú samotné
OpenAI Java SDK. Pokrývajú serializáciu požiadaviek, objavovanie modelov, explicitný výber modelu,
nejednoznačné alebo neúplné zoznamy modelov, HTTP zlyhania, prázdne odpovede,
lokálne URL adresy a propagáciu zlyhaní z príkazového riadku. Nepotrebujú model ani
prístup k sieti okrem inštalácie závislostí Mavenom. Live test je opt-in.

## Spustite lokálny model

### Odporúčané: fixovaný SDK server

Neexistuje natívne Foundry Local Java SDK. Malý pomocný Node.js hosťuje
oficiálny REST server SDK; aplikácia a chat požiadavka zostávajú v Jave.

Nainštalujte fixované runtime závislosti:

```powershell
npm ci
```

Ak Windows x64 nedosiahne NuGet počas natívnej inštalácie SDK, použite dodaný
záložný spôsob. Stiahne zodpovedajúci oficiálny archív runtime z GitHubu, overí
SHA-256 kontrolný súčet vydania a pripraví jeho DLL vedľa natívneho addonu. Nevypína
TLS validáciu, nevyžaduje oprávnenia ani nemení zdroj SDK.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Zoznam modelov už uložených v cache na tomto stroji:

```powershell
npm run start:foundry -- --list
```

Pri prvom spustení výslovne povoľte stiahnutie malého CPU modelu:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Pri ďalších spusteniach vynechajte `--download`, aby bol požadovaný model z cache:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Pomocník preferuje zodpovedajúci uložený model, akceptuje alias alebo presné ID variantu
a odmieta chýbajúci model, pokiaľ nie je zadané `--download`. Zaregistruje iba
vykonávacieho poskytovateľa vybraného modelu, keď je požadovaný. Uložené GPU varianty môžu
stále potrebovať kompatibilné balíky a ovládače poskytovateľa vykonávania.

Ak je port 5273 obsadený, zadajte `--port 0` pre dostupný port. Pomocník vypíše
`FOUNDRY_LOCAL_BASE_URL`, presné `FOUNDRY_LOCAL_MODEL` ID a PID po pripravení.
Použite vytlačenú koncovku v Jave. Nezatvárajte tento terminál počas behu Java;
**Ctrl+C** zastaví REST server a uvoľní model.

Predvolená cache je `~/.foundry/cache/models`. Nastavte `FOUNDRY_LOCAL_CACHE_DIR` pre
inú existujúcu cache. Logy a stav pomocníka sa zapisujú pod adresár
`target/foundry-local` tohto príkladu. Pred spustením `mvn clean` ukončite pomocníka.

### Voliteľné: Foundry Local CLI

CLI a SDK majú nezávislé vydania: CLI **0.10.3** obsahuje SDK **1.2.4**;
zahrnutý pomocník používa SDK **2.0.1**. Inštalácia najnovšieho CLI neznamená inštaláciu
najnovšieho jazykového SDK. Viď [poznámky k vydaniu CLI](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Na Windows používajte príkaz inštalácie pre používateľa, ak CLI chýba:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Alebo aktualizujte existujúcu inštaláciu:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x nahrádza staré príkazy `foundry service` príkazom `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` potrebuje už stiahnutý model. Skontrolujte `foundry model --help` pre
príkazy na stiahnutie. Používajte aktuálnu koncovku zo statusového výstupu; CLI inak
používa automaticky priradený port. Nespúšťajte CLI a SDK pomocníka na rovnakom porte.
Po dokončení:

```powershell
foundry server stop
```

## Spustite Java aplikáciu

V druhom termináli nastavte koncovku a presné ID modelu, ktoré vám server vytlačil:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Alebo spustite zabalenú aplikáciu:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Jediným vstupným bodom v Jave je `com.example.Application`. Vytlačí vybranú
koncovku, aktuálne ID modelu, prompt a vygenerovanú odpoveď, potom zatvorí Spring
kontext a HTTP klienta. Neúspešné inferencie alebo chýbajúci text odpovede spôsobí
zlyhanie ukončenia namiesto uspiešne tvarovaného zástupcu.

### Konfigurácia

| Premenná prostredia | Predvolené | Účel |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP koncovka, vrátane `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Prázdne | Presné ID modelu; inak sa vyberie jediný inzerovaný model |
| `FOUNDRY_LOCAL_PROMPT` | Jednosentencová otázka o lokálnych modeloch | Prompt posielaný konzolovým spúšťačom |

Ekvivalentné Spring argumenty sú `--foundry.local.base-url=...`,
`--foundry.local.model=...`, a `--foundry.local.prompt=...`.
Prijímajú sa iba loopback HTTP koncovky. Vzdialené/cloudové koncovky, vložené
poverenia, query stringy a cesty bez `/v1` sa odmietajú.

Prázdna hodnota modelu funguje len ak `/v1/models` inzeruje presne jeden model.
Inzerovaný model nemusí byť nutne načítaný. Ak sa inzeruje viac modelov,
nastavte presné načítané ID namiesto spoliehania sa na poradie v katalógu.

Požiadavky používajú `temperature=0`, limit výstupu 150 tokenov, timeout 120 sekúnd a
žiadne automatické opakovania. Pole požiadavky `max_tokens` je zámerné:
podporuje ho Foundry Local REST kontrakt, aj keď OpenAI Java deprecated
to pole pre novšie cloudové modely. Identita modelu pochádza z konfigurácie alebo
zisťovania, nie z tvrdení samotného modelu o sebe.

## Živá validácia

Pri spustenom lokálnom serveri spustite všetky testy vrátane voliteľného živého testu.
Nahraďte port koncového bodu hodnotou, ktorú vytlačí váš server. V PowerShellu uvádzajte bodky v
vlastnostiach Maven v úvodzovkách:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Živý test zavolá `Application.main`, dodá fakt „Hlavné mesto
Francúzska je Paríž,“ vypýta si mesto a overí, že skutočný vygenerovaný text je
`Paríž`. Kontroluje sémantický výsledok, nie len úspešný HTTP status.

Toto je integračná kontrola, nie benchmark presnosti. Počas validácie
0,5B model odpovedal na samostatný prompt „2 + 2“ hodnotou `3` v Jave aj priamo cez
REST. Nespoliehajte sa naňho pri aritmetike alebo faktickej presnosti bez nezávislej
overy; pre výpočty používajte deterministické nástroje.

## Riešenie problémov

| Príznak | Skontrolujte |
| --- | --- |
| Pripojenie odmietnuté | Počkajte na správu o pripravenosti; použite vytlačený port a cestu `/v1`. |
| Viacero modelov oznámených | Nastavte `FOUNDRY_LOCAL_MODEL` na presné ID načítaného modelu. |
| Model chýba | Použite `--list` alebo explicitne povoľte stiahnutie s `--download`. |
| Poskytovateľ GPU zlyháva alebo zamŕza | Použite malý CPU model. Kešovaný GPU model stále potrebuje svojho poskytovateľa. |
| CLI zostáva v stave `initializing` | Prečítajte si `foundry server logs --lines 80`; zastavte démona a použite SDK pomocníka. |
| NuGet TLS/ťahanie zlyhalo | Opravte sieťový prístup alebo použite overenú Windows x64 náhradnú možnosť vyššie. Neposkytujte TLS. |
| Port obsadený | Použite `--port 0` a nakonfigurujte Javu podľa vytlačeného koncového bodu. |
| Žiadne možnosti alebo prázdny text | Aplikácia zámerne zlyhala; skontrolujte model a logy runtime. |

## Zdroj a odkazy

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): jednorazový Spring Boot runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typová detekcia a lokálne chat doplnenia.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP kontrakt, runner a živé testy.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): oficiálny SDK REST server s výberom kešovaných modelov a čistením.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): overená Windows x64 natívna náhradná možnosť.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) a [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfigurácia a závislosti.
- [Foundry Local REST integrácia](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 vydanie a poznámky k migrácii](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Kapitola 04: Praktické príklady](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->