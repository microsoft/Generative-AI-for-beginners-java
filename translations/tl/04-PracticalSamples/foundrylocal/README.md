# Foundry Lokal na Tutorial ng Spring Boot

Patakbuhin ang isang maliit na language model sa sarili mong makina at tawagan ang OpenAI-compatible na
REST endpoint nito mula sa isang Java console application. Walang Azure deployment, Azure sign-in,
cloud API key, o cloud inference ang ginagamit. **Ang GPT-5.6 Luna ay para lamang sa Azure; huwag
i-configure ito bilang isang Foundry Local model.**

## Mga Bersyon at mga kinakailangan

| Component | Bersyon |
| --- | --- |
| Java | 21 o mas bago |
| Maven | 3.6.3 o mas bago |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (local REST server) | 2.0.1 |
| Node.js (local REST server) | 20 o mas bago |
| Foundry Local CLI (opsyonal, hiwalay na release) | 0.10.3 preview |

Pinamamahalaan ng Spring Boot ang mga bersyon ng Spring Framework, Jackson, JUnit, at Maven plugin.
Ginagamit ng halimbawa na ito ang OpenAI Java SDK nang direkta, hindi Spring AI. Tinanggal na ang
lumang hindi nagamit na Spring AI milestone property at repository.

Inirerekomenda ang starter model na **Qwen 2.5 0.5B**, CPU variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (mga 822 MB sa catalog).
Ipinapawi nito ang pangangailangang gumamit ng GPU execution providers. Maaaring piliin ang iba pang suportadong, naka-cache na maliit na mga modelo nang eksakto.
Nangangailangan ng network access para sa pag-install ng modelo at runtime;
nananatiling lokal ang prompts at inference. Maaring maglabas pa rin ang Foundry Local ng minimal na runtime
diagnostics kahit naka-disable ang di-kinakailangang telemetry.

Patakbuhin ang mga sumusunod na command mula sa directory ng sample na ito.

## I-build at subukan ang Java

```powershell
mvn clean verify
```

Nagsisimula ang mga HTTP contract tests ng pansamantalang loopback server at sinusuri ang aktwal na
OpenAI Java SDK. Saklaw nito ang request serialization, model discovery, eksaktong pagpili ng modelo,
malabong o mali ang format ng listahan ng modelo, mga pagkabigo sa HTTP, walang laman na sagot,
mga lokal na URL lamang, at propagation ng pagkabigo sa command-line. Hindi kailangan ng modelo o
network access maliban sa pag-install ng Maven dependency. Opsyonal ang live test.

## Simulan ang lokal na modelo

### Inirerekomenda: naka-pin na SDK server

Walang native Foundry Local Java SDK. Ang maliit na Node.js helper ang nagho-host ng
opisyal na SDK REST server; ang application at chat request ay nananatiling Java.

I-install ang naka-pin na runtime dependencies:

```powershell
npm ci
```

Kung ang Windows x64 ay hindi maabot ang NuGet sa panahon ng native install ng SDK, gamitin ang ibinigay
fallback. Idi-download nito ang tumutugmang opisyal na GitHub runtime archive, iche-check ang
SHA-256 na digest ng release, at ilalagay ang mga DLL nito sa tabi ng native addon. Hindi nito
i-disable ang TLS validation, nangangailangan ng elevation, o binabago ang SDK source.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Ilista ang mga models na naka-cache na sa makinang ito:

```powershell
npm run start:foundry -- --list
```

Sa unang pagpapatakbo, hayagang payagan ang pag-download ng maliit na CPU model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Sa mga susunod na pagpapatakbo, huwag isama ang `--download` upang mangailangan ng naka-cache na modelo:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Pinipili ng helper ang tumutugmang naka-cache na modelo, tinatanggap ang alias o eksaktong variant ID,
at tinatanggihan ang nawawalang modelo maliban kung may `--download`. Nire-rehistro lamang nito ang
execution provider ng napiling modelo kapag kinakailangan. Ang naka-cache na GPU variants ay
maaring kailanganin pa rin ng mga katugmang execution-provider packages at drivers.

Kung occupied ang port 5273, gamitin ang `--port 0` para sa available na port. Ipi-print ng helper ang
`FOUNDRY_LOCAL_BASE_URL`, ang eksaktong `FOUNDRY_LOCAL_MODEL` ID, at ang PID nito kapag handa na.
Gamitin ang na-print na endpoint sa Java. Panatilihing bukas ang terminal habang pinapatakbo ang Java;
**Ctrl+C** ay humihinto sa REST server at nire-release ang modelo.

Ang default na cache ay `~/.foundry/cache/models`. Itakda ang `FOUNDRY_LOCAL_CACHE_DIR` para sa
ibang umiiral na cache. Ang mga logs at estado ng helper ay isinusulat sa ilalim ng sample na ito sa
`target/foundry-local` na direktoryo. Ihinto ang helper bago patakbuhin ang `mvn clean`.

### Opsyonal: Foundry Local CLI

Ang CLI at SDK ay may hiwalay na mga releases: ang CLI **0.10.3** ay may SDK **1.2.4**;
ang helper sa itaas ay gumagamit ng SDK **2.0.1**. Ang pag-install ng pinakabagong CLI ay hindi nag-iinstall ng
pinakabagong language SDK. Tingnan ang [CLI release notes](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Sa Windows, gamitin ang per-user install command kung wala pa ang CLI:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

O i-upgrade ang umiiral na installation:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

Pinalitan ng CLI 0.10.x ang lumang `foundry service` commands ng `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

Kailangan ng `model load` na naka-download na ang modelo. Tingnan ang `foundry model --help` para sa
mga download commands. Gamitin ang totoong endpoint mula sa status output; kung hindi, ang CLI ay
gumagamit ng awtomatikong naka-assign na port. Huwag simulan ang CLI at SDK helper
sa parehong port. Kapag tapos na:

```powershell
foundry server stop
```

## Patakbuhin ang Java application

Sa pangalawang terminal, itakda ang endpoint at eksaktong modelo ID na na-print ng iyong server:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

O patakbuhin ang packaged application:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Ang isang Java entrypoint ay `com.example.Application`. Ipi-print nito ang napiling
endpoint, aktwal na modelo ID, prompt, at ang generated na sagot, pagkatapos isasara ang Spring
context at HTTP client. Ang nabigong inference o nawawalang sagot sa teksto ay magreresulta sa
pagkabigong exit sa halip na placeholder na mukhang success.

### Konfigurasyon

| Environment variable | Default | Layunin |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP endpoint, kasama ang `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Walang laman | Eksaktong modelo ID; kung wala pipiliin ang nag-iisang iniarok na modelo |
| `FOUNDRY_LOCAL_PROMPT` | Isang pangungusap na tanong tungkol sa mga lokal na modelo | Prompt na ipinapadala ng console runner |

Katumbas na mga argumento sa Spring ay `--foundry.local.base-url=...`,
`--foundry.local.model=...`, at `--foundry.local.prompt=...`.
Tinatanggap lamang ang loopback HTTP endpoints. Tinanggihan ang remote/cloud endpoints,
embedded credentials, query strings, at mga path na walang `/v1`.

Gumagana lamang ang walang laman na setting ng modelo kapag ang `/v1/models` ay nag-aanunsyo ng eksaktong isang modelo.
Hindi laging naka-load ang inianunsyong modelo. Kung maraming inianunsyo,
itakda ang eksaktong loaded ID sa halip na umasa sa pagkakasunod ng catalog.

Gumagamit ang mga request ng `temperature=0`, 150-token limit para sa output, 120-segundong timeout, at
walang automatic retries. Ang `max_tokens` na request field ay sinasadya: suportado ito ng Foundry Local REST contract,
kahit na deprecate ito ng OpenAI Java.
na patlang para sa mga mas bagong cloud na modelo. Ang pagkakakilanlan ng modelo ay nagmumula sa pagsasaayos o
pagtuklas, hindi mula sa mga pahayag ng modelo tungkol sa sarili nito.

## Live na pag-validate

Sa tumatakbong lokal na server, patakbuhin ang lahat ng mga pagsubok kabilang ang opt-in na live na pagsubok.
Palitan ang port ng endpoint ng halagang ipinakita ng server mo. I-quote ang mga dotted
Maven properties sa PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Ang live na pagsubok ay nag-iinvoke ng `Application.main`, nagbibigay ng katotohanang "Ang kabisera ng
France ay Paris," tinatanong ang lungsod, at sinisiguro na ang aktwal na nilikhang teksto ay
`Paris`. Sinusuri nito ang isang semantikong resulta, hindi lamang matagumpay na HTTP status.

Ito ay isang pagsubok ng integrasyon, hindi isang benchmark ng katumpakan. Sa panahon ng pag-validate, ang
0.5B na modelo ay sumagot sa hiwalay na prompt na "2 + 2" ng `3` sa pamamagitan ng parehong Java at direktang
REST. Huwag umasa dito para sa aritmetika o katotohanang katumpakan nang walang independiyenteng
beripikasyon; gumamit ng mga deterministic na kasangkapan para sa mga kalkulasyon.

## Pag-troubleshoot

| Sintomas | Suriin |
| --- | --- |
| Tinanggihan ang koneksyon | Maghintay ng mensahe ng kahandaan; gamitin ang ipinakitang port at landas na `/v1`. |
| Maraming modelo ang na-advertise | Itakda ang `FOUNDRY_LOCAL_MODEL` sa eksaktong ID ng na-load na modelo. |
| Nawawala ang modelo | Gamitin ang `--list`, o tahasang payagan ang pag-download gamit ang `--download`. |
| Nabigong o naantala ang provider ng GPU | Gamitin ang maliit na CPU na modelo. Ang naka-cache na GPU na modelo ay nangangailangan pa rin ng provider nito. |
| Nanatiling `initializing` ang CLI | Basahin ang `foundry server logs --lines 80`; itigil ang daemon at gamitin ang SDK helper. |
| Nabigong NuGet TLS/download | Ayusin ang network access o gamitin ang napatunayang Windows x64 fallback sa itaas. Huwag i-disable ang TLS. |
| Naka-okupa ang port | Gamitin ang `--port 0` at i-configure ang Java sa ipinakitang endpoint. |
| Walang mga pagpipilian o blankong teksto | Ang app ay sinadyang nabigo; siyasatin ang mga log ng modelo at runtime. |

## Pinagmulan at mga sanggunian

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): one-shot Spring Boot runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typed discovery at lokal na chat completions.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP contract, runner, at live na mga pagsubok.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): opisyal na SDK REST server na may cached-model selection at paglilinis.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): napatunayang Windows x64 native-runtime fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), at [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): pagsasaayos at mga dependencies.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang maling pagkakaintindi o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->