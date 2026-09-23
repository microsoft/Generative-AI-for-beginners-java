# Core Generative AI Techniques Tutorial

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Model Selection Guide](#model-selection-guide)
- [Tutorial 1: LLM Completions and Chat](#tutorial-1-llm-completions-and-chat)
- [Tutorial 2: Function Calling](#tutorial-2-function-calling)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: Responsible AI](#tutorial-4-responsible-ai)
- [Common Patterns Across Examples](#common-patterns-across-examples)
- [Unit Tests](#unit-tests)
- [Sequential Live Verification](#sequential-live-verification)
- [Troubleshooting](#troubleshooting)
- [Next Steps](#next-steps)

## Overview

Four standalone Java programs demonstrate chat, conversation history, function calling, whole-document retrieval-augmented generation (RAG), and responsible-AI response handling. All chat requests target **GPT-5.6 Luna with reasoning effort `none`** by default.

These examples use the official OpenAI Java SDK with Azure OpenAI's v1 endpoint, following [Microsoft's SDK guidance](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). The older `azure-ai-openai` package is no longer a dependency. Chat Completions is retained to teach the existing message-based workflows; see the [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) for other API options.

## Prerequisites

- Java 21 or later and Maven 3.6.3 or later.
- An Azure OpenAI chat deployment named `gpt-5.6-luna`, or an override with compatible Chat Completions settings.
- A signed-in Azure identity with the **Cognitive Services OpenAI User** role on the resource. Local development uses your Azure CLI sign-in; hosted applications can use managed identity.
- See [Chapter 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) for resource setup and sign-in instructions.

The [Maven configuration](../../../03-CoreGenerativeAITechniques/examples/pom.xml) pins these versions, checked on 2026-09-14:

| Component | Version | Purpose |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Official Azure v1-compatible client |
| `com.azure:azure-identity` | 1.18.6 | Keyless authentication and token refresh |
| `net.objecthunter:exp4j` | 0.4.8 | Arithmetic expression parsing without code evaluation |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter unit tests |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 compilation, tests, runnable examples |

The compiler uses `--release 21`. No Spring Boot, Spring AI, or LangChain4j dependency is needed by these standalone examples.

## Getting Started

From the repository root, set the resource endpoint and optional deployment override in your shell.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Tests require neither Azure credentials nor an endpoint. Maven does not automatically read an environment file; set variables in the shell used to launch live examples. For IDE launches, verify the environment supplied by your launch configuration.

## Model Selection Guide

| Environment variable | Meaning | Default |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure resource root or already-normalized `/openai/v1` URL | Required for live runs |
| `AZURE_OPENAI_DEPLOYMENT` | Chat deployment name, not a model version | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Separate embedding deployment configuration, unused by these four programs | `text-embedding-3-small` |

Blank deployment overrides use the defaults. The configuration appends `/openai/v1` exactly once and rejects credentials, query strings, and legacy deployment paths in the endpoint.

Every chat request explicitly sets `reasoningEffort(ReasoningEffort.NONE)` and `maxCompletionTokens(...)`. No request sets `temperature`, `top_p`, or the legacy completion-token option. This includes tool-selection and tool-result follow-ups. GPT-5.6 Chat Completions function tools require reasoning effort `none`; see [Microsoft's chat guidance](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**There is no streaming or embedding entrypoint in this chapter.** The reader retrieves its entire document, not vectors. If you extend it with embeddings, use a separate embedding deployment such as `text-embedding-3-small`, never Luna.

## Tutorial 1: LLM Completions and Chat

Source: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

The program runs a simple Java streams explanation, a two-turn HashMap/TreeMap conversation, and interactive chat. The second turn includes the first assistant response; each interactive turn also sends its prior conversation.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` supplies the deployment and explicit reasoning setting. Interactive chat skips blank lines, ends on `exit` or EOF, and retains the system message plus nine completed user/assistant turns. Turn-count trimming is an educational bound, not an exact token-budget guarantee.

From the examples directory:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Expect three initial answers, then a `You:` prompt. Each nonblank interactive question adds one request. Completion limits are 200, 300, 400, then 500 tokens per interactive turn.

## Tutorial 2: Function Calling

Source: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

The SDK derives JSON schemas from the annotated `WeatherArguments` and `CalculationArguments` records. A required tool choice makes each example exercise the tool protocol instead of accepting a model's unaided answer.

1. Send a question with the allowed tool, reasoning effort `none`, and a 300-token completion limit.
2. Require a `tool_calls` finish reason, validate the function name and call IDs, and parse typed JSON arguments.
3. Execute the local function. The model does not execute Java or arbitrary code.
4. Add the assistant tool-call message once, followed by every result with its matching `tool_call_id`.
5. Send one final 300-token request without tools and require a completed, nonempty answer.

`get_weather` returns **simulated**, not live, weather. It respects the city and converts the sample 22 degrees Celsius to Fahrenheit when requested. `calculate` evaluates the supplied expression through exp4j, supports forms such as `15% of 240` and `2 + 3 * 4`, and rejects blank, oversized, invalid, or nonfinite calculations. It uses floating-point arithmetic, not financial decimal precision.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Expect `Function: get_weather`, simulated Seattle weather, `Function: calculate`, `Function result: 36`, and the two final answers. No stdin or external weather credentials are required. A successful run uses exactly four chat requests.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Source: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Input: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

This introductory RAG example retrieves one whole UTF-8 document and includes it in the user message with the question. A separate system message instructs the model to treat document content as untrusted data and answer only from that context. If the document does not contain the answer, the requested response is: `I cannot find that information in the provided document.`

Grounding can reduce hallucinations, but neither delimiters nor system instructions guarantee accuracy or prevent every prompt injection. Review live answers. Production RAG normally adds chunking, retrieval, citations, access control, and evaluation.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Enter one question, for example `Which authentication method does the document describe?`. Expect an answer mentioning Microsoft Entra ID. The program exits after one chat request with a 500-token completion limit.

Default file lookup works from the repository root, chapter directory, or examples directory. An explicit path is also supported:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Inputs must be nonblank: at most 32 KiB of UTF-8 document data and 2,000 question characters. Missing files, blank/EOF questions, and oversized inputs fail before inference.

## Tutorial 4: Responsible AI

Source: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

The six probes cover harmful instructions, hate speech, privacy, medical misinformation, illegal content, and a benign responsible-AI question. The program observes the response rather than assuming every probe must trigger a filter.

| Outcome | Evidence |
| --- | --- |
| `FILTERED` | An explicit `content_filter` / `ResponsibleAIPolicyViolation` error code, or a completion `content_filter` finish reason |
| `REFUSED` | A nonblank structured `message.refusal` field |
| `POSSIBLE_REFUSAL` | An opening refusal phrase in ordinary text; a heuristic requiring review |
| `GENERATED` | A completed nonempty response; not proof that its content is safe |

An ordinary HTTP 400 is **not** evidence of filtering. Invalid parameters, authentication failures, rate limits, server errors, malformed responses, and truncated output fail the run instead of producing a false safety success. Broad words such as "harmful content" in a benign explanation do not count as a refusal.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Expect six category results and a summary stating that the observations are not a safety certification. Each probe has a 300-token completion limit. Review unexpected generations and possible refusals manually; the benign comparison should produce a substantive responsible-AI explanation. No stdin is required.

## Common Patterns Across Examples

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizes endpoint normalization, deployment overrides, keyless authentication, and chat options:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

The token supplier refreshes access tokens as needed. Do not log tokens or replace this with an API key. Each program reuses its client and closes it in `finally` or through its own `AutoCloseable` wrapper; the SDK's `OpenAIClient` itself is not `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) requires a completed, nonempty textual answer. Empty choices, refusals, filters, and truncated answers are not silently printed as success. The responsible-AI example handles expected filter/refusal outcomes explicitly. Unhandled failures give the Java/Maven process a nonzero exit code.

**Automatic SDK retries are disabled** to keep request counts predictable on shared low-RPM deployments. Each inference request has a 60-second timeout. Token acquisition may take additional time. Application-level scheduling must respect quotas; do not blindly rerun a failed paid request.

## Unit Tests

From the examples directory:

```powershell
mvn -B -ntp clean test
```

The test transport replaces the SDK HTTP layer entirely, captures actual serialized request bodies, and supplies queued responses. It opens no sockets, acquires no Azure tokens, and fails on unexpected requests. These tests validate application behavior and the SDK protocol, not live model quality or deployment availability.

| Test suite | Coverage |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Endpoint normalization/rejection, deployment overrides, reasoning and token options |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Every completion workflow, message history, complete-turn trimming, EOF, failures |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Tool schemas, typed arguments, arithmetic, IDs, multiple tool results, failed follow-ups |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | File lookup, UTF-8, size limits, grounding payload, input and API errors |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | All six probes, explicit filters, refusal classification, ordinary 400 and other failures |

For one suite, use `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Shared fixtures live in [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sequential Live Verification

Live calls are separate from unit tests. Use the following commands **individually**, from the repository root, only after credentials and deployment access are ready. No services or persistent processes are needed.

For a shared **10 requests/minute** deployment, reserve enough quota for the entire next program before launching it: 5, 4, 1, then 6 requests. Sequential processes alone do not guarantee rate-limit compliance. Coordinate the rolling minute with all other callers; do not paste the four invocations as an unpaced batch.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completions, multi-turn, and two interactive turns:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Check all di tri section headings, five answers, one last interactive answer wey remember Ada, `Goodbye!`, plus exit code 0. Budget: **5 requests, no pass 1,900 completion tokens**. If you wan small run, pipe only `exit`: 3 requests / 900 tokens, but dat one no dey exercise interactive inference.

**2. Both function-calling workflows:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Check both function names, simulated Seattle weather, calculated result 36, two last answers, plus exit code 0. Budget: **4 requests, no pass 1,200 completion tokens**.

**3. Document-grounded answer:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Check di document path, one answer wey mention Microsoft Entra ID, plus exit code 0. Budget: **1 request, no pass 500 completion tokens**. Di existing [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) na di only required input file. One optional second run wey ask about absent topic suppose no run, and e go add one request / 500 tokens.

**4. Responsible-AI observations:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Check six categories plus di observational summary, review di generated content, and require exit code 0 for technical completion. One successful process exit no mean say di model correct. Budget: **6 requests, no pass 1,800 completion tokens**.

**Total for di four commands: 16 chat requests plus no pass 5,400 completion tokens**, plus input tokens (we include repeated conversation and tool schema/history). No embedding requests dey. Actual token use dey depend on di model and fit be smaller, especially for filtered prompts. Dollar cost depend on deployment pricing; no fixed money estimate dey. All request limits dey assume no manual reruns. Check `$LASTEXITCODE` quick after each command; if e no be zero, dat run no complete well.

## Troubleshooting

- **Missing endpoint / 401 / 403:** Set di endpoint for di launching process, verify your local Azure sign-in plus resource-scoped role, plus check for any unintended identity environment overrides.
- **400 / 404:** Confirm say di deployment dey and e dey support Chat Completions with reasoning effort `none`. Use di HTTPS resource root or `/openai/v1` URL, no use legacy deployment URL. Ordinary 400 errors na technical problems, no be safety blocks.
- **429:** Coordinate how di shared RPM and token quota dey before you try again. Di examples no dey auto-retry on purpose.
- **`Incomplete chat response: length`:** Di output reach di completion limit. Review di response and prompt before you increase di limit plus im documented budget; no record truncated run as success.
- **File or stdin errors:** Launch from supported directory or pass clear document path. Make sure to provide reader question wey no blank. Completions fit end normally on EOF or `exit`.
- **Compilation errors:** Verify say Java 21 or later dey, then run `mvn -B -ntp clean test`. For PowerShell, put quotes for whole Maven argument wey get dotted property, example `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Next Steps

Continue to [Chapter 4: Practical Samples](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dis document don translate wit AI translation service [Co-op Translator](https://github.com/Azure/co-op-translator). Even tho we dey try make am correct, abeg make you know say automated translation fit get errors or mistakes. Di original document for dia own language na im be di correct source. For important info, make person wey sabi human translation do am. We no go responsible for any misunderstanding or wrong understanding wey fit happen because of dis translation.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->