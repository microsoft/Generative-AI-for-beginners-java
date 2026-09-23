# Foundry Local Spring Boot Tutorial

Run a small language model on your own machine and call its OpenAI-compatible
REST endpoint from a Java console application. No Azure deployment, Azure sign-in,
cloud API key, or cloud inference is used. **GPT-5.6 Luna is Azure-only; do not
configure it as a Foundry Local model.**

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

Spring Boot manages Spring Framework, Jackson, JUnit, and Maven plugin versions.
This example uses the OpenAI Java SDK directly, not Spring AI. The old unused
Spring AI milestone property and repository have been removed.

The recommended starter model is **Qwen 2.5 0.5B**, CPU variant
`qwen2.5-0.5b-instruct-generic-cpu:4` (approximately 822 MB in the catalog).
It avoids requiring GPU execution providers. Other supported, cached small models
can be selected explicitly. Model and runtime installation require network access;
prompts and inference stay local. Foundry Local may still emit minimal runtime
diagnostics even with nonessential telemetry disabled.

Run the following commands from this sample directory.

## Build and test Java

```powershell
mvn clean verify
```

The HTTP contract tests start an ephemeral loopback server and exercise the actual
OpenAI Java SDK. They cover request serialization, model discovery, explicit model
selection, ambiguous or malformed model lists, HTTP failures, blank responses,
local-only URLs, and command-line failure propagation. They need no model or
network access beyond Maven dependency installation. The live test is opt-in.

## Start the local model

### Recommended: pinned SDK server

There is no native Foundry Local Java SDK. The small Node.js helper hosts the
official SDK's REST server; the application and chat request remain Java.

Install the pinned runtime dependencies:

```powershell
npm ci
```

If Windows x64 cannot reach NuGet during the SDK's native install, use the supplied
fallback. It downloads the matching official GitHub runtime archive, checks the
release's SHA-256 digest, and stages its DLLs beside the native addon. It does not
disable TLS validation, require elevation, or modify SDK source.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

List models already cached on this machine:

```powershell
npm run start:foundry -- --list
```

On the first run, explicitly allow the small CPU model download:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

On subsequent runs, omit `--download` to require a cached model:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

The helper prefers a matching cached model, accepts an alias or exact variant ID,
and refuses a missing model unless `--download` is supplied. It registers only the
selected model's execution provider when one is required. Cached GPU variants can
still need compatible execution-provider packages and drivers.

If port 5273 is occupied, pass `--port 0` for an available port. The helper prints
`FOUNDRY_LOCAL_BASE_URL`, the exact `FOUNDRY_LOCAL_MODEL` ID, and its PID when ready.
Use the printed endpoint in Java. Leave this terminal open while running Java;
**Ctrl+C** stops the REST server and releases the model.

The default cache is `~/.foundry/cache/models`. Set `FOUNDRY_LOCAL_CACHE_DIR` for a
different existing cache. Logs and helper state are written under this sample's
`target/foundry-local` directory. Stop the helper before running `mvn clean`.

### Optional: Foundry Local CLI

The CLI and SDK have independent releases: CLI **0.10.3** bundles SDK **1.2.4**;
the helper above uses SDK **2.0.1**. Installing the latest CLI does not install the
latest language SDK. See the [CLI release notes](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

On Windows, use the per-user install command if the CLI is absent:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Or upgrade an existing installation:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x replaces old `foundry service` commands with `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` needs an already downloaded model. Check `foundry model --help` for
download commands. Use the status output's actual endpoint; the CLI otherwise
defaults to an automatically assigned port. Do not start the CLI and SDK helper
on the same port. When finished:

```powershell
foundry server stop
```

## Run the Java application

In a second terminal, set the endpoint and exact model ID printed by your server:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Or run the packaged application:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

The single Java entrypoint is `com.example.Application`. It prints the selected
endpoint, actual model ID, prompt, and generated response, then closes its Spring
context and HTTP client. Failed inference or missing response text produces a
failure exit instead of a success-shaped placeholder.

### Configuration

| Environment variable | Default | Purpose |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Loopback HTTP endpoint, including `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Empty | Exact model ID; otherwise select the single advertised model |
| `FOUNDRY_LOCAL_PROMPT` | A one-sentence question about local models | Prompt sent by the console runner |

Equivalent Spring arguments are `--foundry.local.base-url=...`,
`--foundry.local.model=...`, and `--foundry.local.prompt=...`.
Only loopback HTTP endpoints are accepted. Remote/cloud endpoints, embedded
credentials, query strings, and paths without `/v1` are rejected.

An empty model setting works only when `/v1/models` advertises exactly one model.
An advertised model is not necessarily loaded. If multiple models are advertised,
set the exact loaded ID rather than relying on catalog ordering.

Requests use `temperature=0`, a 150-token output limit, a 120-second timeout, and
no automatic retries. The `max_tokens` request field is intentional: it is
supported by the Foundry Local REST contract, although OpenAI Java deprecates
that field for newer cloud models. Model identity comes from configuration or
discovery, not from the model's claims about itself.

## Live validation

With the local server running, run all tests including the opt-in live test.
Replace the endpoint's port with the value printed by your server. Quote dotted
Maven properties in PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

The live test invokes `Application.main`, supplies the fact "The capital of
France is Paris," asks for the city, and asserts the actual generated text is
`Paris`. It checks a semantic result, not just a successful HTTP status.

This is an integration check, not an accuracy benchmark. During validation, this
0.5B model answered a separate "2 + 2" prompt with `3` through both Java and direct
REST. Do not rely on it for arithmetic or factual accuracy without independent
verification; use deterministic tools for calculations.

## Troubleshooting

| Symptom | Check |
| --- | --- |
| Connection refused | Wait for the ready message; use the printed port and `/v1` path. |
| Multiple models advertised | Set `FOUNDRY_LOCAL_MODEL` to the loaded model's exact ID. |
| Model missing | Use `--list`, or explicitly allow a download with `--download`. |
| GPU provider fails or stalls | Use the small CPU model. A cached GPU model still needs its provider. |
| CLI remains `initializing` | Read `foundry server logs --lines 80`; stop the daemon and use the SDK helper. |
| NuGet TLS/download failure | Fix network access or use the verified Windows x64 fallback above. Do not disable TLS. |
| Port occupied | Use `--port 0` and configure Java with the printed endpoint. |
| No choices or blank text | The app fails deliberately; inspect the model and runtime logs. |

## Source and references

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): one-shot Spring Boot runner.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typed discovery and local chat completions.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP contract, runner, and live tests.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): official SDK REST server with cached-model selection and cleanup.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): verified Windows x64 native-runtime fallback.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), and [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): configuration and dependencies.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Chapter 04: Practical samples](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
This document has been translated using AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). While we strive for accuracy, please be aware that automated translations may contain errors or inaccuracies. The original document in its native language should be considered the authoritative source. For critical information, professional human translation is recommended. We are not liable for any misunderstandings or misinterpretations arising from the use of this translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->