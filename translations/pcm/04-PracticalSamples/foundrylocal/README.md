# Foundry Local Spring Boot Tutorial

Run small language model for your own machine and call im OpenAI-compatible
REST endpoint from Java console application. No Azure deployment, Azure sign-in,
cloud API key, or cloud inference dey used. **GPT-5.6 Luna na only for Azure; no
configure am as Foundry Local model.**

## Versions and prerequisites

| Component | Version |
| --- | --- |
| Java | 21 or later |
| Maven | 3.6.3 or later |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (local REST server) | 2.0.1 |
| Node.js (local REST server) | 20 or later |
| Foundry Local CLI (optional, separate release) | 0.10.3 preview |

Spring Boot dey manage Spring Framework, Jackson, JUnit, and Maven plugin versions.
Dis example dey use OpenAI Java SDK directly, no be Spring AI. Old unused
Spring AI milestone property and repository don commot.

The recommended starter model na **Qwen 2.5 0.5B**, CPU variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (about 822 MB for catalog).
E no need GPU execution providers. Other supported, cached small models fit
be choose explicitly. Model and runtime installation need network access;
prompts and inference dey local. Foundry Local fit still give small runtime
diagnostics even if una don disable nonessential telemetry.

Run the commands wey dey below from dis sample directory.

## Build and test Java

```powershell
mvn clean verify
```

The HTTP contract tests go start ephemeral loopback server and test the real
OpenAI Java SDK. Dem go check request serialization, model discovery, explicit model
selection, confusing or wrong model lists, HTTP failures, blank responses,
local-only URLs, and command-line failure propagation. Dem no need model or
network access pass Maven dependency installation. The live test na opt-in.

## Start the local model

### Recommended: pinned SDK server

No native Foundry Local Java SDK dey. The small Node.js helper dey host the
official SDK REST server; application and chat request still dey Java.

Install the pinned runtime dependencies:

```powershell
npm ci
```

If Windows x64 no fit reach NuGet during SDK native install, make una use the supplied
fallback. E go download matching official GitHub runtime archive, check
release's SHA-256 digest, and place im DLLs beside the native addon. E no
go disable TLS validation, need elevation, or change SDK source.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

List models wey don already cache for this machine:

```powershell
npm run start:foundry -- --list
```

For the first run, explicitly allow the small CPU model download:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

For subsequent runs, no add `--download` so make e require cached model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

The helper prefer matching cached model, accept alias or exact variant ID,
and go reject missing model if `--download` no dey supplied. E only register the
selected model's execution provider when one dey required. Cached GPU variants fit
still require compatible execution-provider packages and drivers.

If port 5273 don busy, pass `--port 0` for available port. The helper go print
`FOUNDRY_LOCAL_BASE_URL`, exact `FOUNDRY_LOCAL_MODEL` ID, and im PID when e ready.
Use the printed endpoint for Java. Keep dis terminal open as you dey run Java;
**Ctrl+C** go stop the REST server and release the model.

Default cache na `~/.foundry/cache/models`. Set `FOUNDRY_LOCAL_CACHE_DIR` for
different existing cache. Logs and helper state dem dey write under this sample's
`target/foundry-local` directory. Stop the helper before you run `mvn clean`.

### Optional: Foundry Local CLI

CLI and SDK get their own separate releases: CLI **0.10.3** get SDK **1.2.4** bundle;
the helper wey dey above dey use SDK **2.0.1**. Installing latest CLI no mean say you
install latest language SDK. See [CLI release notes](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

For Windows, use per-user install command if CLI no dey:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Or upgrade existing installation:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x don change old `foundry service` commands to `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` need model wey don download already. Check `foundry model --help` for
download commands. Use real endpoint from status output; CLI otherwise
go default to automatically assigned port. No start CLI and SDK helper
for same port. When you finish:

```powershell
foundry server stop
```

## Run the Java application

For second terminal, set endpoint and exact model ID wey your server print:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Or run the packaged application:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

The one Java entrypoint na `com.example.Application`. E go print selected
endpoint, real model ID, prompt, and generated response, then e go close Spring
context and HTTP client. Failed inference or missing response text go cause
failure exit instead of success-shaped placeholder.

### Configuration

| Environment variable | Default | Purpose |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP endpoint, including `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Empty | Exact model ID; otherwise choose the single advertised model |
| `FOUNDRY_LOCAL_PROMPT` | One question about local models | Prompt wey console runner go send |

Equivalent Spring arguments na `--foundry.local.base-url=...`,
`--foundry.local.model=...`, and `--foundry.local.prompt=...`.
Only loopback HTTP endpoints dey accepted. Remote/cloud endpoints, embedded
credentials, query strings, and paths without `/v1` no go accepted.

Empty model setting work only if `/v1/models` advertise exactly one model.
Advertised model no mean say e don load. If multiple models dey advertised,
set exact loaded ID no be rely on catalog order.

Requests use `temperature=0`, 150-token output limit, 120-second timeout, and
no automatic retries. The `max_tokens` request field na intentional: e dey
supported by Foundry Local REST contract, even though OpenAI Java don dey deprecated
dat field na for newer cloud models. Model identity come from configuration or
discovery, no be from di model claims about itself.

## Live validation

As di local server dey run, run all tests including di opt-in live test.
Change di endpoint port to di value wey your server print. Quote dotted
Maven properties for PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Di live test dey call `Application.main`, e supply di fact "The capital of
France na Paris," e ask for di city, and e check say di actual generated text na
`Paris`. E dey check semantic result, no be only successful HTTP status.

Dis na integration check, no be accuracy benchmark. During validation, dis
0.5B model answer one separate "2 + 2" prompt wit `3` through both Java and direct
REST. No rely on am for arithmetic or factual accuracy without independent
verification; make you use deterministic tools for calculations.

## Troubleshooting

| Symptom | Check |
| --- | --- |
| Connection refused | Wait for di ready message; use di printed port and `/v1` path. |
| Multiple models advertised | Set `FOUNDRY_LOCAL_MODEL` to di loaded model exact ID. |
| Model missing | Use `--list`, or allow make e download with `--download`. |
| GPU provider fails or stalls | Use di small CPU model. Cached GPU model still need provider. |
| CLI still dey `initializing` | Read `foundry server logs --lines 80`; stop the daemon and use SDK helper. |
| NuGet TLS/download failure | Fix network access or use verified Windows x64 fallback above. No disable TLS. |
| Port occupied | Use `--port 0` and configure Java wit di printed endpoint. |
| No choices or blank text | Di app dey fail purposely; inspect di model and runtime logs. |

## Source and references

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): one-shot Spring Boot runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typed discovery and local chat completions.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP contract, runner, and live tests.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): official SDK REST server wit cached-model selection and cleanup.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): verified Windows x64 native-runtime fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), and [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): configuration and dependencies.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg make you know say automated translation fit get errors or mistakes. Di original document for dia own language na im be di correct source. For important info, make person wey sabi human translation do am. We no go responsible for any misunderstanding or wrong understanding wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->