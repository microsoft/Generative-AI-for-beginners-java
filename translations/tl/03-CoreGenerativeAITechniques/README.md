# Tutorial sa Pangunahing Mga Teknik ng Generative AI

## Talaan ng Nilalaman

- [Mga Kinakailangan](#mga-kinakailangan)
- [Pagsisimula](#pagsisimula)
- [Gabay sa Pagpili ng Modelo](#gabay-sa-pagpili-ng-modelo)
- [Tutorial 1: LLM Completions at Chat](#tutorial-1-llm-completions-at-chat)
- [Tutorial 2: Pagtawag ng Function](#tutorial-2-pagtawag-ng-function)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: Responsable AI](#tutorial-4-responsable-ai)
- [Karaniwang Pattern sa Mga Halimbawa](#karaniwang-pattern-sa-mga-halimbawa)
- [Mga Unit Test](#mga-unit-test)
- [Pagsusuri ng Live na Sunud-sunod](#pagsusuri-ng-live-na-sunud-sunod)
- [Pag-troubleshoot](#pag-aayos-ng-problema)
- [Mga Susunod na Hakbang](#mga-susunod-na-hakbang)

## Pangkalahatang-ideya

Apat na nakahiwalay na Java na programa ang nagpapakita ng chat, kasaysayan ng pag-uusap, pagtawag ng function, retrieval-augmented generation (RAG) sa buong dokumento, at responsable-AI na paghawak ng tugon. Lahat ng mga kahilingan sa chat ay nakatuon sa **GPT-5.6 Luna na may reasoning effort `none`** bilang default.

Ginagamit ng mga halimbawang ito ang opisyal na OpenAI Java SDK kasama ang Azure OpenAI's v1 endpoint, alinsunod sa [gabay ng Microsoft sa SDK](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Hindi na dependency ang mas lumang `azure-ai-openai` package. Pinananatili ang Chat Completions upang ituro ang umiiral na mga workflow na batay sa mga mensahe; tingnan ang [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) para sa iba pang mga opsyon ng API.

## Mga Kinakailangan

- Java 21 o mas bago at Maven 3.6.3 o mas bago.
- Isang Azure OpenAI chat deployment na pinangalanang `gpt-5.6-luna`, o isang override na may compatible na mga setting ng Chat Completions.
- Isang naka-sign in na Azure identity na may **Cognitive Services OpenAI User** na papel sa resource. Gumagamit ang lokal na pag-unlad ng iyong Azure CLI sign-in; maaaring gamitin ng mga hosted na aplikasyon ang managed identity.
- Tingnan ang [Kabanata 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) para sa pagsasaayos ng resource at mga tagubilin sa pag-sign in.

Ang [Maven configuration](../../../03-CoreGenerativeAITechniques/examples/pom.xml) ang nagpi-pin ng mga bersyon na ito, na nasuri noong 2026-09-14:

| Bahagi | Bersyon | Layunin |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Opisyal na Azure v1-compatible na kliyente |
| `com.azure:azure-identity` | 1.18.6 | Pagpapatunay na walang susi at pag-refresh ng token |
| `net.objecthunter:exp4j` | 0.4.8 | Parsing ng arithmetic expression nang walang code evaluation |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter unit tests |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Pag-compile ng Java 21, mga tests, mga runnable na halimbawa |

Ginagamit ng compiler ang `--release 21`. Hindi kailangan ng Spring Boot, Spring AI, o LangChain4j na dependency para sa mga standalone na halimbawang ito.

## Pagsisimula

Mula sa root ng repositoryo, itakda ang resource endpoint at opsyonal na override ng deployment sa iyong shell.

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

Hindi kailangan ng mga tests ang Azure credentials o endpoint. Hindi awtomatikong binabasa ng Maven ang isang environment file; itakda ang mga variable sa shell na gagamitin upang ilunsad ang mga live na halimbawa. Para sa mga paglulunsad gamit ang IDE, suriin ang environment na ibinibigay ng iyong launch configuration.

## Gabay sa Pagpili ng Modelo

| Environment variable | Kahulugan | Default |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure resource root o isang normalisadong `/openai/v1` URL | Kailangan para sa mga live na pagtakbo |
| `AZURE_OPENAI_DEPLOYMENT` | Pangalan ng chat deployment, hindi bersyon ng modelo | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Hiwa-hiwalay na configuration ng embedding deployment, hindi ginagamit ng apat na programang ito | `text-embedding-3-small` |

Ang mga blangkong override ng deployment ay gumagamit ng mga default. Idinadagdag ng configuration ang `/openai/v1` nang eksakto isang beses at tinatanggihan ang mga kredensyal, query strings, at mga legacy na deployment path sa endpoint.

Ang bawat kahilingan sa chat ay tahasang nagse-set ng `reasoningEffort(ReasoningEffort.NONE)` at `maxCompletionTokens(...)`. Walang kahilingan na nagse-set ng `temperature`, `top_p`, o ang legacy na opsyon sa completion-token. Kasama dito ang pagpili ng tool at mga follow-up sa resulta ng tool. Nangangailangan ang GPT-5.6 Chat Completions function tools ng reasoning effort na `none`; tingnan ang [gabayan ng Microsoft sa chat](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Walang streaming o embedding entrypoint sa kabanatang ito.** Kinukuha ng reader ang buong dokumento, hindi mga vector. Kung papalawakin mo ito gamit ang embeddings, gumamit ng hiwalay na embedding deployment tulad ng `text-embedding-3-small`, huwag kailanman ang Luna.

## Tutorial 1: LLM Completions at Chat

Pinagmulan: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Pinapatakbo ng programa ang simpleng paliwanag sa Java streams, isang dalawang-turn na usapan gamit ang HashMap/TreeMap, at interactive chat. Kasama sa pangalawang turn ang unang tugon ng assistant; bawat interactive na turn ay nagpapadala rin ng naunang pag-uusap.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` ang nagbibigay ng deployment at tahasang reasoning setting. Ang interactive chat ay pumapalagpas sa mga blangkong linya, nagtatapos sa `exit` o EOF, at pinapanatili ang system message kasama ang siyam na nagawang mga user/assistant turn. Ang pag-trim ng bilang ng turn ay isang pang-edukasyong hangganan, hindi isang tumpak na token-budget na garantiya.

Mula sa direktoryo ng mga halimbawa:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Asahan ang tatlong panimulang sagot, pagkatapos isang `You:` prompt. Bawat hindi blangkong interactive na tanong ay nagdadagdag ng isang kahilingan. Ang limitasyon sa completion ay 200, 300, 400, pagkatapos ay 500 token bawat interactive turn.

## Tutorial 2: Pagtawag ng Function

Pinagmulan: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

Kinukuha ng SDK ang JSON schemas mula sa annotated na `WeatherArguments` at `CalculationArguments` records. Isang kinakailangang pagpili ng tool ang ginagawa ng bawat halimbawa na gamitin ang protocol ng tool sa halip na tanggapin ang hindi tinulungan na sagot ng modelo.

1. Magpadala ng tanong gamit ang pinapayagang tool, reasoning effort na `none`, at limitasyon sa completion na 300 token.
2. Kailangan ang `tool_calls` finish reason, i-validate ang pangalan ng function at mga call ID, at i-parse ang typed JSON arguments.
3. Patakbuhin ang lokal na function. Hindi nagpapatupad ang modelo ng Java o kahit anong arbitraryong code.
4. Idagdag ang assistant tool-call message nang isang beses, kasunod ang bawat resulta gamit ang tugmang `tool_call_id`.
5. Magpadala ng isang huling kahilingan na 300 token nang walang mga tool at kailangan ang kumpleto at di-blangkong sagot.

Nagbabalik ang `get_weather` ng **pinuputol na simulasyon**, hindi live na panahon. Iginagalang nito ang lungsod at kino-convert ang halimbawa ng 22 degrees Celsius sa Fahrenheit kapag hiniling. Sinusuri naman ng `calculate` ang ibinigay na expression gamit ang exp4j, sinusuportahan ang mga porma tulad ng `15% of 240` at `2 + 3 * 4`, at tinatanggihan ang blangko, sobra ang laki, diwasto, o hindi tapos na mga kalkulasyon. Gumagamit ito ng floating-point arithmetic, hindi financial decimal precision.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Asahan ang `Function: get_weather`, simulated na panahon ng Seattle, `Function: calculate`, `Function result: 36`, at ang dalawang huling sagot. Hindi kailangan ng stdin o panlabas na kredensyal sa panahon. Isang matagumpay na pagtakbo ang gumagamit ng eksaktong apat na kahilingan sa chat.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Pinagmulan: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Input: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Kinukuha ng panimulang halimbawang RAG ang isang buong dokumento na UTF-8 at isinasama ito sa mensahe ng user kasama ng tanong. Ang isang hiwalay na system message ay nag-uutos sa modelo na ituring ang nilalaman ng dokumento bilang hindi mapagkakatiwalaang data at sumagot lamang base sa kontekstong iyon. Kung wala sa dokumento ang sagot, ang hinihinging tugon ay: `I cannot find that information in the provided document.`

Nakakatulong ang grounding na mabawasan ang mga halusinasyon, ngunit hindi ginagarantiyahan ng mga delimiter o mga tagubilin ng system ang katumpakan o napipigilan ang bawat prompt injection. Suriin ang mga live na sagot. Karaniwang idinadagdag ng production RAG ang chunking, retrieval, citation, access control, at evaluation.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Maglagay ng isang tanong, halimbawa `Which authentication method does the document describe?`. Asahan ang sagot na nagsasabi ng Microsoft Entra ID. Lalabas ang programa pagkatapos ng isang chat request na may 500-token completion limit.

Gumagana ang default file lookup mula sa root ng repositoryo, direktoryo ng kabanata, o direktoryo ng mga halimbawa. Sinusuportahan din ang isang tahasang path:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Dapat ay hindi blangko ang mga input: hanggang 32 KiB ng UTF-8 na dokumento at 2,000 na character ng tanong. Ang nawawalang mga file, blangko/EOF na mga tanong, at sobrang laki ng mga input ay nabibigo bago ang paggaya.

## Tutorial 4: Responsable AI

Pinagmulan: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Sinasaklaw ng anim na probe ang mapaminsalang mga tagubilin, hate speech, privacy, maling impormasyon sa medisina, ilegal na nilalaman, at isang benign na tanong ng responsable-AI. Binabantayan ng programa ang tugon imbes na ipagpalagay na kailangang mag-trigger ng filter ang bawat probe.

| Kinalabasan | Katibayan |
| --- | --- |
| `FILTERED` | Isang tahasang `content_filter` / `ResponsibleAIPolicyViolation` na error code, o isang completion na may `content_filter` finish reason |
| `REFUSED` | Isang hindi blangkong structured `message.refusal` na field |
| `POSSIBLE_REFUSAL` | Isang panimulang parirala ng pagtanggi sa ordinaryong teksto; isang heuristik na nangangailangan ng pagsusuri |
| `GENERATED` | Isang kumpleto at di-blangkong tugon; hindi patunay na ligtas ang nilalaman nito |

Ang ordinaryong HTTP 400 ay **hindi** katibayan ng pag-filter. Nabibigo ang pagtakbo dahil sa diwastong mga parametro, kapalpakan sa pagpapatunay, mga limitasyon sa rate, mga error ng server, di-kaayusan na mga tugon, at putol na output imbes na magbigay ng maling tagumpay sa kaligtasan. Hindi binibilang bilang pagtanggi ang mga malawak na salita tulad ng "mapaminsalang nilalaman" sa isang benign na paliwanag.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Asahan ang anim na resulta ng kategorya at isang buod na nagsasaad na ang mga obserbasyon ay hindi sertipikasyon sa kaligtasan. Bawat probe ay may 300-token completion limit. Suriin nang manu-mano ang mga hindi inaasahang generation at posibleng pagtanggi; ang benign na paghahambing ay dapat makagawa ng substansyal na paliwanag ng responsable-AI. Hindi kailangan ng stdin.

## Karaniwang Pattern sa Mga Halimbawa

Pinagsasama-sama ng [AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) ang normalisasyon ng endpoint, override ng deployment, pagpapatunay na walang susi, at mga opsyon sa chat:

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

Ina-refresh ng token supplier ang access tokens kung kinakailangan. Huwag i-log ang mga token o palitan ito ng API key. Paulit-ulit na ginagamit ng bawat programa ang sarili nitong kliyente at isinasara ito sa `finally` o sa pamamagitan ng sarili nitong `AutoCloseable` na wrapper; hindi `AutoCloseable` mismo ang SDK's `OpenAIClient`.

Kinakailangan ng [ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) ang kumpleto at di-blangkong tekstuwal na sagot. Hindi tahimik na ini-print bilang tagumpay ang mga blangkong pagpipilian, pagtanggi, filter, at putol na sagot. Malinaw na hinihawakan ng responsable-AI na halimbawa ang inaasahang mga kinalabasan ng filter/pagtanggi. Nagbibigay ng nonzero exit code ang Java/Maven process sa di-hinakaling pagkabigo.

**Hindi pinapagana ang awtomatikong retry ng SDK** upang panatilihing predictable ang mga bilang ng kahilingan sa mga shared na mababang RPM deployment. Bawat inference request ay may 60-segundong timeout. Maaaring tumagal pa ng karagdagang oras ang pagkuha ng token. Dapat igalang ng scheduling sa antas ng aplikasyon ang mga quota; huwag basta-basta ulitin ang nabigong bayad na kahilingan.

## Mga Unit Test

Mula sa direktoryo ng mga halimbawa:

```powershell
mvn -B -ntp clean test
```

Pinapalitan ng test transport ang SDK HTTP layer nang buo, kinukuha ang aktwal na na-serialize na katawan ng kahilingan, at nagbibigay ng naka-queue na mga tugon. Hindi ito nagbubukas ng mga socket, hindi kumukuha ng Azure tokens, at pumapalpak sa di-inasahang mga kahilingan. Pinapatibay ng mga test na ito ang pagkilos ng aplikasyon at ang protocol ng SDK, hindi ang kalidad ng live na modelo o availability ng deployment.

| Test suite | Saklaw |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalisasyon/pagtanggi sa endpoint, override ng deployment, mga opsyon sa reasoning at token |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Bawat completion workflow, kasaysayan ng mensahe, pag-trim ng kumpletong turn, EOF, mga pagkabigo |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Mga schema ng tool, typed arguments, arithmetic, mga ID, maraming resulta ng tool, nabigong mga follow-up |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Lookup ng file, UTF-8, mga limitasyon sa laki, grounding payload, mga error sa input at API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Lahat ng anim na probe, malinaw na mga filter, klasipikasyon ng pagtanggi, ordinaryong 400 at iba pang mga pagkabigo |

Para sa isang suite, gamitin ang `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Nakatira ang mga shared fixture sa [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Pagsusuri ng Live na Sunud-sunod

Hiwa-hiwalay ang live calls mula sa mga unit test. Gamitin ang mga sumusunod na utos **isa-isa**, mula sa root ng repositoryo, pagkatapos lang na handa na ang mga kredensyal at access sa deployment. Walang mga serbisyo o permanenteng proseso ang kailangan.

Para sa isang shared na deployment na **10 kahilingan/minuto**, magreserba ng sapat na quota para sa buong susunod na programa bago ito ilunsad: 5, 4, 1, pagkatapos 6 na kahilingan. Hindi ginagarantiyahan ng mga sunud-sunod na proseso lamang ang pagsunod sa rate-limit. I-coordinate ang rolling minute kasama ang lahat ng iba pang tumatawag; huwag ipiliit ang apat na tawag bilang isang batch na walang pacing.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completions, multi-turn, at dalawang interactive turn:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Suriin ang lahat ng tatlong mga pamagat ng seksyon, limang sagot, isang huling interaktibong sagot na nagpapaalala kay Ada, `Paalam!`, at exit code 0. Budget: **5 kahilingan, hindi hihigit sa 1,900 completion tokens**. Para sa mas maliit na pagpapatakbo, i-pipe lamang ang `exit`: 3 kahilingan / 900 tokens, ngunit hindi nito nasusubukan ang interaktibong inference.

**2. Parehong mga workflow na tumatawag ng function:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Suriin ang parehong mga pangalan ng function, ginamit na simulated na panahon sa Seattle, kalkulado na resulta na 36, dalawang panghuling sagot, at exit code 0. Budget: **4 kahilingan, hindi hihigit sa 1,200 completion tokens**.

**3. Sagot na nakabatay sa dokumento:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Suriin ang path ng dokumento, isang sagot na binabanggit ang Microsoft Entra ID, at exit code 0. Budget: **1 kahilingan, hindi hihigit sa 500 completion tokens**. Ang umiiral na [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) ang nag-iisang kinakailangang input file. Ang isang optional na pangalawang pagpapatakbo na nagtatanong tungkol sa isang wala sa paksa ay dapat panghinaan ng loob at nagdadagdag ng isang kahilingan / 500 tokens.

**4. Mga obserbasyon sa Responsible-AI:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Suriin ang anim na kategorya at ang observational summary, suriin ang nilikhang nilalaman, at kinakailangang exit code 0 para sa teknikal na pagkumpleto. Ang matagumpay na proseso ng exit ay hindi nagpapatunay ng kaligtasan ng modelo. Budget: **6 kahilingan, hindi hihigit sa 1,800 completion tokens**.

**Kabuuan para sa apat na mga utos: 16 chat na kahilingan at hindi hihigit sa 5,400 completion tokens**, dagdag pa ang input tokens (kabilang ang paulit-ulit na pag-uusap at tool schema/history). Walang embedding na kahilingan. Ang aktwal na paggamit ng token ay nakadepende sa modelo at maaaring mas mababa, lalo na para sa mga filtered prompts. Ang gastusing dolyar ay nakadepende sa presyo ng deployment; walang nakatakdang eksaktong halaga ang ipinapalagay. Lahat ng limitasyon sa kahilingan ay ipinapalagay na walang mano-manong reruns. Suriin ang `$LASTEXITCODE` agad pagkatapos ng bawat utos; ang nonzero ay nangangahulugang hindi matagumpay ang pagpapatakbo.

## Pag-aayos ng Problema

- **Nawawalang endpoint / 401 / 403:** Itakda ang endpoint sa proseso ng paglulunsad, suriin ang iyong lokal na Azure sign-in at resource-scoped role, at tingnan para sa hindi sinasadyang identity environment overrides.
- **400 / 404:** Kumpirmahin na umiiral ang deployment at sinusuportahan ang Chat Completions na may reasoning effort na `none`. Gamitin ang HTTPS resource root o `/openai/v1` na URL, hindi ang legacy deployment URL. Karaniwang mga 400 error ay mga teknikal na pagkabigo, hindi mga seguridad na hadlang.
- **429:** Iayos ang shared RPM at token quota bago muling subukan. Ang mga halimbawa ay sinasadya na hindi mag-auto-retry.
- **`Incomplete chat response: length`:** Naabot ng output ang limitasyon ng completion. Suriin ang sagot at prompt bago dagdagan ang limitasyon at ang dokumentadong budget nito; huwag ituring na matagumpay ang isang pinutol na pagpapatakbo.
- **Mga error sa file o stdin:** Magsimula mula sa suportadong direktoryo o magpasa ng tiyak na path ng dokumento. Magbigay ng hindi blankong tanong sa tagabasa. Maaari magtapos nang normal ang Completions sa EOF o `exit`.
- **Mga error sa compilation:** Siguraduhing Java 21 o mas bago, pagkatapos ay patakbuhin ang `mvn -B -ntp clean test`. Sa PowerShell, i-quotes ang buong Maven argument na naglalaman ng dotted property, halimbawa `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Mga Susunod na Hakbang

Magpatuloy sa [Chapter 4: Practical Samples](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang maling pagkakaintindi o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->