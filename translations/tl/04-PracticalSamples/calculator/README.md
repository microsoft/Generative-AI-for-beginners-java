# MCP Calculator Tutorial para sa mga Baguhan

## Talaan ng mga Nilalaman

- [Ano ang Iyong Matututuhan](#ano-ang-iyong-matututuhan)
- [Mga Kinakailangan](#mga-kinakailangan)
- [Mga Bersyon ng Dependency](#mga-bersyon-ng-dependency)
- [Pag-unawa sa Istruktura ng Proyekto](#pag-unawa-sa-istruktura-ng-proyekto)
- [Paliwanag sa mga Pangunahing Bahagi](#paliwanag-sa-mga-pangunahing-bahagi)
  - [1. Pangunahing Aplikasyon](#1-pangunahing-aplikasyon)
  - [2. Serbisyo ng Calculator](#2-serbisyo-ng-calculator)
  - [3. Direktang MCP Client](#3-direktang-mcp-client)
  - [4. Kliyente na Pinapagana ng AI](#4-kliyente-na-pinapagana-ng-ai)
- [Pagpapatakbo ng mga Halimbawa](#pagpapatakbo-ng-mga-halimbawa)
- [Mga Pagsubok Offline](#mga-pagsubok-offline)
- [Paano Lahat ng Ito Nagtutulungan](#paano-lahat-ng-ito-nagtutulungan)
- [Mga Susunod na Hakbang](#mga-susunod-na-hakbang)

## Ano ang Iyong Matututuhan

Inilalarawan ng tutorial na ito kung paano gumawa ng serbisyo ng calculator gamit ang Model Context Protocol (MCP). Mauunawaan mo:

- Paano gumawa ng serbisyo na maaaring gamitin ng AI bilang kasangkapan
- Paano i-setup ang direktang komunikasyon sa mga MCP na serbisyo
- Paano awtomatikong pipili ang mga modelo ng AI kung anong mga kasangkapan ang gagamitin
- Ang pagkakaiba ng direktang mga tawag sa protocol at mga interaksyon na tinulungan ng AI

## Mga Kinakailangan

Bago magsimula, tiyaking mayroon kang:
- Java 21 o mas mataas pa na naka-install
- Maven para sa pamamahala ng dependency
- Pangunahing kaalaman sa Java at Spring Boot

Tanging ang mga AI client lamang ang nangangailangan ng Azure OpenAI deployment at isang authenticated `DefaultAzureCredential`,
tulad ng isang umiiral na Azure CLI sign-in sa lokal o isang managed identity sa Azure. Kailangan ng identity
ang Cognitive Services OpenAI User role sa resource. Tingnan ang [Chapter 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Ang server, direktang SDK client, at lahat ng automated tests ay hindi kailangan ng Azure account o access sa modelo.

## Mga Bersyon ng Dependency

Mga na-verify na release dependencies noong 2026-09-14:

| Dependency | Bersyon |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI-managed) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j official OpenAI adapter | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-managed) | 6.0.3 |

Ang MCP at opisyal na OpenAI adapters ay mga inilathalang beta releases sa Maven Central, hindi snapshots.
Iba ang kanilang mga bersyon mula sa LangChain4j core. Hindi kailangan ang mga snapshot o milestone na repositories.
Ang client-only dependencies ay may test scope dahil ang mga runnable examples ay nasa ilalim ng `src/test/java`.

## Pag-unawa sa Istruktura ng Proyekto

Ang proyekto ng calculator ay may ilang mahahalagang mga file:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Paliwanag sa mga Pangunahing Bahagi

### 1. Pangunahing Aplikasyon

**File:** `McpServerApplication.java`

Ito ang entry point ng aming serbisyo ng calculator. Isa itong standard na Spring Boot application na may isang espesyal na dagdag:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Ang ginagawa nito:**
- Nagpapasimula ng Spring Boot web server sa port 8080
- Lumilikha ng `ToolCallbackProvider` na nagbibigay-daan upang maging available ang mga metodo ng calculator natin bilang mga MCP tool
- Ang `@Bean` annotation ay nagsasabi sa Spring na pamahalaan ito bilang isang bahagi na maaaring gamitin ng ibang bahagi

### 2. Serbisyo ng Calculator

**File:** `CalculatorService.java`

Dito nangyayari lahat ng matematika. Bawat metodo ay may markang `@Tool` upang maging available sa pamamagitan ng MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Higit pang mga operasyon ng calculator...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Mga pangunahing tampok:**

1. **`@Tool` Annotation**: Sinasabi nito sa MCP na ang methodong ito ay maaari tawagin ng external clients
2. **Malinaw na Deskripsyon**: Bawat tool ay may deskripsyon na tumutulong sa mga modelo ng AI na maintindihan kung kailan ito gagamitin
3. **Consistent na Pormat ng Bunga**: Lahat ng operasyon ay nagbabalik ng mga human-readable na string gaya ng "5.00 + 3.00 = 8.00"
4. **Paghawak ng Error**: Ang paghati sa zero at mga negatibong square root ay nagbabalik ng mga mensahe ng error

**Mga Available na Operasyon:**
- `add(a, b)` - Nagdaragdag ng dalawang numero
- `subtract(a, b)` - Binabawas ang pangalawa mula sa una
- `multiply(a, b)` - Nagmumultiply ng dalawang numero
- `divide(a, b)` - Hinahati ang una sa pangalawa (may check sa zero)
- `power(base, exponent)` - Itinaas ang base sa power ng exponent
- `squareRoot(number)` - Kinakalkula ang square root (may check sa negatibo)
- `modulus(a, b)` - Nagbabalik ng remainder ng paghahati
- `absolute(number)` - Nagbabalik ng absolute value
- `help()` - Nagbabalik ng impormasyon tungkol sa lahat ng operasyon

### 3. Direktang MCP Client

Tingnan ang [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Ginagamit ng client na ito ang `HttpClientStreamableHttpTransport` sa `/mcp`, ini-initialize ang koneksyon,
pini-ping ang server, at sinusunod ang pagination ng listahan ng tool. Tinitiyak na lahat ng siyam na inaasahang tool
ay umiiral at tinatawag ang bawat isa, kasama ang `modulus` at `help`, nang walang AI modelo.

Ganito ang hitsura ng kasalukuyang request builder:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Ang mga error sa protocol ay nagdudulot ng pagkabigo ng client imbes na mag-print ng maling tagumpay. Ang MCP client
ay isinara gamit ang try-with-resources, kabilang kapag nabigo ang discovery o isang tawag sa tool.

### 4. Kliyente na Pinapagana ng AI

Tingnan ang [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
at [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

Ang `OpenAiOfficialChatModel` ay nagpapatupad ng kasalukuyang LangChain4j `ChatModel` API.
Ang `StreamableHttpMcpTransport` ay nag-uugnay dito sa parehong `/mcp` endpoint tulad ng SDK client.
Ang `AiServices` ay nagdidiskubre ng mga tool at pinangangasiwaan ang usapan sa pagitan ng tawag sa tool at resulta.

Ang default deployment ay **GPT-5.6 Luna**, na may reasoning na tahasang naka-disable:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ang mga default na ito ay nalalapat sa bawat completion, kasama na ang mga follow-up pagkatapos ng tool execution.
Ang client ay gumagamit ng refreshable na `BearerTokenCredential` na suportado ng `DefaultAzureCredential`
at ang `https://ai.azure.com/.default` na scope, hindi isang one-time token na ipinapasa bilang API key.
Tinanggap parehong mga resource URL at mga URL na nagtatapos na sa `/openai/v1`.

Pinananatili ng bot ang isang bounded na history ng pag-uusap, nagpaprint ng `Tool executed: ...` na may aktwal na
resulta ng MCP, at bumabagsak kung may tugon na lumaktaw sa mga tool. Limitado ang mga tool loop sa apat na round trip.
Ang authentication, modelo, MCP, at mga tool errors ay naipapasa; naka-disable ang awtomatikong pag-ulit ng modelo.
Parehong MCP transport/client at ang opisyal na OpenAI client ay isinara kapag tagumpay o nabigo.

## Pagpapatakbo ng mga Halimbawa

### Hakbang 1: Simulan ang Calculator Server

Hindi kailangan ng anumang Azure configuration para sa server. Ang mga utos sa ibaba ay pinapatakbo mula sa direktoryo ng sample na ito.
Ginagamit ng halimbawa ang port na **18081** upang maiwasan ang conflict sa ibang sample; ang default ay nananatiling 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Ang MCP endpoint ay `http://localhost:18081/mcp`. Ang health at discovery information ay nasa
`http://localhost:18081/health` at `http://localhost:18081/info`.
Pinalitan ng Streamable HTTP ang lumang transport na SSE-only; hindi endpoints ang `/sse` at `/v1/tools`.

### Hakbang 2: Subukan gamit ang Direktang Client

Sa ibang PowerShell terminal:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Walang input ang kailangan. Lahat ng siyam na tools ay nasusubukan. Inaasahang mga resulta ng aritmetika ay
8, 6, 42, 5, 256, 4, 2, at 5.5, kasunod ang help text.

### Hakbang 3: Subukan gamit ang AI Client

Pagkatapos ma-authenticate gaya ng nakasaad sa prerequisites, i-configure ang AI client sa parehong terminal:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Inaasahan ang isang linya na `Tool executed: add` na may `41.80`, kasunod ang sagot ng modelo.
Ang single-prompt mode ay lalabas agad nang hindi naghihintay ng input. Upang patakbuhin ang orihinal na four-prompt na demo:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Tinatawag ng demo ang `add`, `squareRoot`, `help`, at ang magkakasunod na `power` pagkatapos `divide` na operasyon.
Inaasahang mga numerong sagot ay 41.8, 12, at 64. Ang hindi pagbibigay ng mga argumento ay nagpapatakbo rin ng demo na ito.

### Hakbang 4: Patakbuhin ang Interactive Bot

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

I-type ang `Multiply 6 by 7 using the calculator service`, pagkatapos `exit` o `quit`.
Inaasahan ang aktwal na resulta ng `multiply` tool na 42. Hindi pinapansin ang mga blangkong linya; ang EOF ay nagtatapos din ng session.
Para sa isang noninteractive smoke test ng entrypoint na ito:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Parehong tumatanggap ang mga AI entrypoint ng `--prompt "question"`, `--demo`, at `--interactive`.
Ang mga invalid options ay nagdudulot ng pagkabigo bago magbukas ng koneksyon. Ang bawat Maven `-D...` na argumento ay fully quoted
para sa PowerShell. Sa Bash, gamitin ang `export NAME=value` sa halip na `$env:NAME = "value"`.

**Quota:** Patakbuhin ang mga AI samples nang sunud-sunod. Isang simpleng prompt ay karaniwang nangangailangan ng dalawang model request;
ang buong demo ay karaniwang nangangailangan ng siyam, kabilang ang mga tool-result follow-ups. Sa isang shared 10 RPM
deployment, maghintay ng bago quota window bago ang susunod na AI run. Ang 429 ay talagang nagdudulot ng pagkabigo nang walang
automatic retries; sundin ang retry-after guidance ng serbisyo. Ang aktwal na bilang ng request ay nakadepende sa modelo.
Hindi kumokonsumo ng quota at hindi sumusuri ng live Luna availability o kalidad ng sagot ang mga offline test.

### Configuration at Shutdown

| Setting | Default / behavior |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; base URL, walang `/mcp` |
| `-Dmcp.server.url=...` | Pinapalitan ang `MCP_SERVER_URL` para sa lahat ng client |
| `AZURE_OPENAI_ENDPOINT` | Kinakailangan lamang para sa AI clients; resource URL o `/openai/v1` na URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; isang pangalan ng Azure deployment |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positibong integer |
| Reasoning effort | Laging `none`, kabilang na ang tool-loop follow-ups |

Ang pinapalitang deployment ay dapat sumuporta sa `reasoning_effort=none` at `max_completion_tokens`.
Hindi awtomatikong nagbabasa ang mga client ng `.env` file. Patayin ang server gamit ang `Ctrl+C` pagkatapos ng pagsubok.
Ang mga client ay bumabalik nang normal nang walang `System.exit` o shutdown sleeps.

## Mga Pagsubok Offline

```powershell
mvn -B -ntp clean verify
```

Lahat ng pagsubok ay offline kaugnay sa Azure: nagsisimula ang protocol suite ng Spring server at
stub na compatible sa OpenAI sa mga random na loopback port, pagkatapos ay isinasara ang mga ito. Maaaring kailanganin pa rin ng Maven
na mag-download ng mga dependency. Walang credentials, live deployment, o umiiral na MCP server ang ginagamit.

- Ang mga unit test ng calculator ay sumasaklaw sa lahat ng aritmetikong operasyon, decimal na resulta, help, at mga domain error.
- Ang mga MCP test ay sumasaklaw sa initialization, discovery, lahat ng siyam na tawag sa tool, mga pagkabigo sa tool, at health/info.
- Ang mga AI protocol test ay nagpapatakbo ng buong demo at interactive Bot laban sa totoong calculator,
  nagve-verify na ang mga resulta ng tool ay pinapasukan sa susunod na completion, at sinusuri ang bawat HTTP body para sa Luna,
  `reasoning_effort: "none"`, at `max_completion_tokens` nang walang legacy na `max_tokens`.
- Ang configuration/input tests ay sumasaklaw sa deployment at endpoint overrides, blangkong mga linya, EOF, exit/quit,
  single-prompt mode, invalid options, at propagation ng error. Pinapatunayan ng mga quota tests na hindi ini-retry ang 429.

## Paano Lahat ng Ito Nagtutulungan

Narito ang kumpletong daloy kapag tinanong mo ang AI ng "Ano ang 5 + 3?":

1. **Ikaw** ang nagtatanong sa AI sa natural na wika
2. **AI** ay sinusuri ang iyong kahilingan at napagtatanto na gusto mong mag-add
3. **AI** ay tumatawag sa MCP server: `add(5.0, 3.0)`
4. **Calculator Service** ay nagsasagawa: `5.0 + 3.0 = 8.0`
5. **Calculator Service** ay nagbabalik: `"5.00 + 3.00 = 8.00"`
6. **AI** ay tumatanggap ng resulta at bumubuo ng natural na tugon
7. **Ikaw** ay nakakakuha ng: "Ang kabuuan ng 5 at 3 ay 8"

## Mga Susunod na Hakbang

Para sa higit pang mga halimbawa, tingnan ang [Chapter 04: Practical samples](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Pagtatanggi**:
Ang dokumentong ito ay isinalin gamit ang serbisyo ng AI translation na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't nagsusumikap kami para sa katumpakan, pakatandaan na ang awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa orihinal nitong wika ang dapat ituring na pangunahing sanggunian. Para sa mahahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang maling pagkakaintindi o maling interpretasyon na nagmula sa paggamit ng pagsasaling ito.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->