# Mafunzo ya Kikokotozi cha MCP kwa Waanzilishi

## Jedwali la Yaliyomo

- [Utajifunza Nini](#utajifunza-nini)
- [Vifaa Vinavyohitajika](#vifaa-vinavyohitajika)
- [Matoleo ya Vitegemezi](#matoleo-ya-vitegemezi)
- [Kuelewa Muundo wa Mradi](#kuelewa-muundo-wa-mradi)
- [Vipengele Muhimu Vilivyoelezwa](#vipengele-muhimu-vilivyoelezwa)
  - [1. Programu Kuu](#1-programu-kuu)
  - [2. Huduma ya Kikokotozi](#2-huduma-ya-kikokotozi)
  - [3. Mteja wa MCP Moja kwa Moja](#3-mteja-wa-mcp-moja-kwa-moja)
  - [4. Mteja Mwengezaji wa AI](#4-mteja-mwengezaji-wa-ai)
- [Kukimbia Mifano](#kukimbia-mifano)
- [Majaribio ya Nje ya Mtandao](#majaribio-ya-nje-ya-mtandao)
- [Jinsi Kila Kitu Kinavyofanya Kazi Pamoja](#jinsi-kila-kitu-kinavyofanya-kazi-pamoja)
- [Hatua Zinazofuata](#hatua-zinazofuata)

## Utajifunza Nini

Mafunzo haya yanaelezea jinsi ya kujenga huduma ya kikokotozi kutumia Itifaki ya Muktadha wa Mfano (MCP). Utakuwa na ufahamu wa:

- Jinsi ya kuunda huduma ambayo AI inaweza kutumia kama chombo
- Jinsi ya kuweka mawasiliano ya moja kwa moja na huduma za MCP
- Jinsi mifano ya AI inaweza kuchagua kwa moja kwa moja ni zana gani zitumiwe
- Tofauti kati ya simu za itifaki moja kwa moja na mwingiliano unaosaidiwa na AI

## Vifaa Vinavyohitajika

Kabla ya kuanza, hakikisha una:
- Java 21 au toleo jipya zaidi limewekwa
- Maven kwa usimamizi wa utegemezi
- Uelewa wa msingi wa Java na Spring Boot

Ni wateja wa AI pekee wanahitaji usambazaji wa Azure OpenAI na `DefaultAzureCredential` iliyoidhinishwa,
kama kuingia kwenye Azure CLI kwa ndani au kitambulisho kilichosimamiwa Azure. Kitambulisho kinahitaji
jukumu la Mtumiaji wa Cognitive Services OpenAI kwenye rasilimali. Angalia [Sura ya 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Server, mteja wa moja kwa moja wa SDK, na majaribio yote yaliyoendeshwa kwa moja hayahitaji akaunti ya Azure wala upatikanaji wa mfano.

## Matoleo ya Vitegemezi

Vitegemezi vya toleo vilivyohakikishiwa tarehe 2026-09-14:

| Tegemezi | Toleo |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Inasimamiwa na Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Kirekebisha Rasmi cha OpenAI cha LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Utambulisho wa Azure | 1.18.6 |
| JUnit Jupiter (Inasimamiwa na Boot) | 6.0.3 |

Kirekebisha MCP na kirekebisha rasmi cha OpenAI ni toleo za beta zilizochapishwa katika Maven Central, si picha za muda mfupi.
Matoleo yao yanatofautiana na LangChain4j core. Hakuna mahitaji ya hifadhi za snapshot au milestone.
Vitegemezi vya mteja pekee vina upeo wa mtihani kwa sababu mifano inayoweza kuendeshwa iko chini ya `src/test/java`.

## Kuelewa Muundo wa Mradi

Mradi wa kikokotozi una faili kadhaa muhimu:

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

## Vipengele Muhimu Vilivyoelezwa

### 1. Programu Kuu

**Faili:** `McpServerApplication.java`

Huu ndio mlango wa kuingia kwenye huduma yetu ya kikokotozi. Ni programu ya kawaida ya Spring Boot yenye nyongeza moja maalum:

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

**Hii hufanya nini:**
- Inaanzisha server ya wavuti ya Spring Boot kwenye bandari 8080
- Inaunda `ToolCallbackProvider` inayofanya njia zetu za kikokotozi zipatikane kama zana za MCP
- Alama ya `@Bean` inamwambia Spring kusimamia hii kama sehemu ambayo sehemu zingine zinaweza kutumia

### 2. Huduma ya Kikokotozi

**Faili:** `CalculatorService.java`

Hapa ndipo hesabu zote zinapofanyika. Kila njia ina alama `@Tool` ili ifikike kupitia MCP:

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
    
    // Operesheni zaidi za kalkuleta...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Sifa kuu:**

1. **Alama ya `@Tool`**: Hii inaambia MCP kuwa njia hii inaweza kuitwa na wateja wa nje
2. **Maelezo Yenye Uwazi**: Kila chombo kina maelezo yanayosaidia mifano ya AI kuelewa lini ya kuitumia
3. **Muundo Mkubaliano wa Kurudisha Matokeo**: Mifumo yote hurudisha mistari inayosaswa na binadamu kama "5.00 + 3.00 = 8.00"
4. **Udhibiti wa Makosa**: Kugawanya kwa sifuri na mizizi hasi hurudisha ujumbe wa makosa

**Matendo Yanayopatikana:**
- `add(a, b)` - Huongeza nambari mbili
- `subtract(a, b)` - Huanza na kutoa nambari ya pili kutoka ya kwanza
- `multiply(a, b)` - Hunazidisha nambari mbili
- `divide(a, b)` - Hugawanya ya kwanza kwa ya pili (ikiwa na ukaguzi wa sifuri)
- `power(base, exponent)` - Huinua msingi kwa nguvu ya kielezi
- `squareRoot(number)` - Huhesabu mzizi wa mraba (ikiwa na ukaguzi wa hasi)
- `modulus(a, b)` - Hurudisha mabaki ya mgawanyo
- `absolute(number)` - Hurudisha thamani kamili
- `help()` - Hurudisha habari kuhusu kazi zote

### 3. Mteja wa MCP Moja kwa Moja

Angalia [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Mteja huyu hutumia `HttpClientStreamableHttpTransport` kwenye `/mcp`, huanzisha muunganisho,
hufuata server, na huangalia kurasa za orodha ya zana. Hukagua kuwa zana tisa zinazotarajiwa
zipo na huzitumia zote, zikiwemo `modulus` na `help`, bila mfano wa AI.

Mjenzi wa ombi wa sasa unaonekana kama hii:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Makosa ya itifaki huathiri mteja badala ya kuchapisha mafanikio yasiyo sahihi. Mteja wa MCP
hufungwa kwa kutumia jaribio-na-rasilimali (try-with-resources), ikiwa ni pamoja na pale ugunduzi au simu ya chombo inapotofaulu.

### 4. Mteja Mwengezaji wa AI

Angalia [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
na [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` inatekeleza API ya LangChain4j `ChatModel` ya sasa.
`StreamableHttpMcpTransport` inaiunganisha kwenye kitovu kile kile cha `/mcp` kama mteja wa SDK.
`AiServices` hugundua zana na kusimamia mazungumzo ya simu/majibu ya zana.

Usambazaji wa msingi ni **GPT-5.6 Luna**, na sababu imezimwa wazi:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Mipangilio hii ndio inayotumika kwa kila ukamilisho, ikijumuisha majibu yanayofuata baada ya matumizi ya zana.
Mteja hutumia `BearerTokenCredential` inayoweza kusasishwa inayotegemea `DefaultAzureCredential`
na upeo wa `https://ai.azure.com/.default`, si tokeni ya mara moja inayopitishwa kama ufunguo wa API.
URL za rasilimali na URL zinazomalizika na `/openai/v1` zinakubaliwa zote.

Bot huweka kumbukumbu ya mazungumzo yenye kikomo, huchapisha `Tool executed: ...` na matokeo halisi ya
MCP, na huanguka ikiwa jibu halijatumia zana. Mzunguko wa zana umewekwa mpaka mizunguko minne.
Udhibitisho, model, MCP, na makosa ya zana huenezwa; jaribio la moja kwa moja la mfano limezimwa.
Wote usafirishaji/mteja wa MCP na mteja rasmi wa OpenAI hufungwa kwa mafanikio au kushindwa.

## Kukimbia Mifano

### Hatua ya 1: Anzisha Server ya Kikokotozi

Hakuna usanidi wa Azure unaohitajika kwa server. Amri zifuatazo zinamiliki kutoka kwenye saraka ya mfano huu.
Mfano hutumia bandari **18081** kuepuka mgongano na mfano mwingine; chaguo-msingi bado ni 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Kitovu cha MCP ni `http://localhost:18081/mcp`. Taarifa za afya na ugunduzi ziko kwenye
`http://localhost:18081/health` na `http://localhost:18081/info`.
Streamable HTTP inachukua usafirishaji wa zamani wa SSE pekee; `/sse` na `/v1/tools` si vituo tena.

### Hatua ya 2: Jaribu na Mteja wa Moja kwa Moja

Katika terminal nyingine ya PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Hakuna pembejeo inayohitajika. Zana zote tisa hudumishwa. Matokeo yaliyotegemewa ya hesabu ni pamoja na
8, 6, 42, 5, 256, 4, 2, na 5.5, ikifuatiwa na maandishi ya msaada.

### Hatua ya 3: Jaribu na Mteja wa AI

Baada ya kuingia kama ilivyoelezwa katika vifaa vinavyohitajika, sanidi mteja wa AI katika terminal ile ile:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Tarajia mstari wa `Tool executed: add` wenye `41.80`, ikifuatiwa na jibu la mfano.
Mode ya ombi moja huondoka mara moja bila kusubiri pembejeo. Ili kuendesha mafunzo ya ombi nne ya awali:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Mafunzo huita `add`, `squareRoot`, `help`, na operesheni ya mfuatano `power` kisha `divide`.
Majibu ya nambari yanayotarajiwa ni 41.8, 12, na 64. Kukosa hoja pia huendesha mafunzo haya.

### Hatua ya 4: Endesha Bot Anayeingiliana

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Ingiza `Multiply 6 by 7 using the calculator service`, kisha `exit` au `quit`.
Tarajia matokeo halisi ya chombo `multiply` wa 42. Mistari tupu haisikilizwe; EOF pia huisha kikao.
Kwa jaribio la moshi lisiloingiliana la mlango huu:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Milango yote ya AI inakubali `--prompt "swali"`, `--demo`, na `--interactive`.
Viteuzi batili huvunjika kabla ya kufungua muunganisho. Kila hoja ya Maven `-D...` inahitaji kuwekewa alama kikamilifu
kwa PowerShell. Katika Bash, tumia `export NAME=value` badala ya `$env:NAME = "value"`.

**Kipaumbele:** Endesha mifano ya AI kwa mfuatano. Ombi rahisi kawaida huhitaji maombi mawili ya mfano;
mafunzo kamili kawaida huhitaji tisa, ikijumuisha majibu ya matokeo ya zana. Kwa usambazaji wa RPM 10
unaogawana, ruhusu dirisha jipya la kipaumbele kabla ya mzunguko mwingine wa AI. 429 huacha kwa ufanisi bila
jaribio la kiotomatiki; fuata miongozo ya huduma ya retry-after. Idadi halisi ya maombi hutegemea mfano.
Majaribio ya nje ya mtandao hayatumii kipaumbele chochote na hayaangalii upatikanaji wa Luna wa moja kwa moja au ubora wa jibu.

### Usanidi na Kufunga

| Mipangilio | Chaguo-msingi / tabia |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; URL ya msingi, bila `/mcp` |
| `-Dmcp.server.url=...` | Inazidi `MCP_SERVER_URL` kwa wateja wote |
| `AZURE_OPENAI_ENDPOINT` | Inahitajika kwa wateja wa AI pekee; URL ya rasilimali au URL ya `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; jina la usambazaji la Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; nambari chanya |
| Juhudi za sababu | Daima `hakuna`, ikijumuisha majibu ya mzunguko wa zana |

Usambazaji uliobadilishwa lazima usaidie `reasoning_effort=none` na `max_completion_tokens`.
Wateja hawasomi faili la `.env` moja kwa moja. Zima server kwa `Ctrl+C` baada ya majaribio.
Wateja hurudisha kawaida bila `System.exit` au usingizi wa kufunga.

## Majaribio ya Nje ya Mtandao

```powershell
mvn -B -ntp clean verify
```

Majaribio yote ni nje ya mtandao kwa Azure: seti ya itifaki huanzisha server ya Spring na
stub inayolingana na OpenAI kwenye bandari za nasibu za loopback, kisha huzifunga. Maven bado inaweza kuhitaji
kupakua utegemezi. Hakuna vibali, usambazaji wa moja kwa moja, au server ya MCP ya awali hutumika.

- Majaribio ya vipande vya kikokotozi yanashughulikia shughuli zote za hesabu, matokeo ya desimali, msaada, na makosa ya eneo.
- Majaribio ya MCP yanashughulikia uanzishaji, ugunduzi, simu zote tisa za zana, kushindwa kwa zana, na afya/taarifa.
- Majaribio ya itifaki ya AI hufanya mafunzo kamili na Bot wa mwingiliano dhidi ya kikokotozi halisi,
  kuthibitisha matokeo ya zana hutoa ukamilisho unaofuata, na kuchunguza kila mwili wa HTTP kwa Luna,
  `reasoning_effort: "none"`, na `max_completion_tokens` bila `max_tokens` ya kale.
- Majaribio ya usanidi/pembejeo yanashughulikia usambazaji na kupita vituo, mistari tupu, EOF, exit/quit,
  mode ya ombi moja, chaguo batili, na kusambaza makosa. Majaribio ya kipaumbele yanaonyesha 429 haijaribii tena.

## Jinsi Kila Kitu Kinavyofanya Kazi Pamoja

Huu hapa mtiririko kamili unapoambia AI "Je, 5 + 3 ni nini?":

1. **Wewe** unauliza AI kwa lugha ya kawaida
2. **AI** inachambua ombi lako na kutambua unataka kuongeza
3. **AI** inaita server ya MCP: `add(5.0, 3.0)`
4. **Huduma ya Kikokotozi** hufanya: `5.0 + 3.0 = 8.0`
5. **Huduma ya Kikokotozi** hurudisha: `"5.00 + 3.00 = 8.00"`
6. **AI** hupokea matokeo na kuandaa jibu la asili
7. **Wewe** unapata: "Jumla ya 5 na 3 ni 8"

## Hatua Zinazofuata

Kwa mifano zaidi, angalia [Sura 04: Mifano ya vitendo](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kionyozo**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati ya asili katika lugha yake halisi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatutojibu kwa kuelewa vibaya au tafsiri potofu zinazotokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->