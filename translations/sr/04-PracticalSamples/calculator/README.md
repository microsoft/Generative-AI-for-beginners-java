# Почетни водич за MCP калкулатор

## Садржај

- [Шта ћете научити](#шта-ћете-научити)
- [Претходни услови](#претходни-услови)
- [Верзије зависности](#верзије-зависности)
- [Разумевање структуре пројекта](#разумевање-структуре-пројекта)
- [Објашњење основних компоненти](#објашњење-основних-компоненти)
  - [1. Главна апликација](#1-главна-апликација)
  - [2. Калкулатор сервис](#2-калкулатор-сервис)
  - [3. Директни MCP клијент](#3-директни-mcp-клијент)
  - [4. Клијент са вештачком интелигенцијом](#4-клијент-са-вештачком-интелигенцијом)
- [Покретање примера](#покретање-примера)
- [Оффлине тестови](#оффлине-тестови)
- [Како све функционише заједно](#како-све-функционише-заједно)
- [Следећи кораци](#следећи-кораци)

## Шта ћете научити

Овај водич објашњава како израдити калкулатор сервис користећи Model Context Protocol (MCP). Укуцаћете:

- Како створити сервис који вештачка интелигенција може користити као алат
- Како поставити директну комуникацију са MCP сервисима
- Како модели вештачке интелигенције аутоматски одабирају које алате да користе
- Разлику између директних позива протокола и интеракција уз помоћ вештачке интелигенције

## Претходни услови

Пре него што почнете, уверите се да имате:
- Јава 21 или новију верзију инсталирану
- Maven за управљање зависностима
- Основно разумевање Јаве и Spring Boot

Само AI клијенти захтевају развојну Azure OpenAI инстанцу и аутентификовани `DefaultAzureCredential`,
као што је постојећа Azure CLI пријава локално или управљани идентитет у Azure-у. Идентитет треба
Cognitive Services OpenAI User улогу на ресурсу. Погледајте [Поглавље 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Сервер, директни SDK клијент и сви аутоматизовани тестови не захтевају Azure налог или приступ моделу.

## Верзије зависности

Потврђене зависности од 14.09.2026:

| Зависност | Верзија |
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

MCP и службени OpenAI адаптери су објављене бета верзије у Maven Central, а не snapshot верзије.
Њихове верзије се разликују од LangChain4j core. Нема потребе за snapshot или milestone репозиторијумима.
Зависности само за клијенте имају опсег тестирања јер се покретљиви примери налазе у `src/test/java`.

## Разумевање структуре пројекта

Калкулатор пројекат има неколико важних фајлова:

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

## Објашњење основних компоненти

### 1. Главна апликација

**Фајл:** `McpServerApplication.java`

Ово је улазна тачка нашег калкулатор сервиса. Стандардна је Spring Boot апликација са једним посебним додатком:

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

**Шта ово ради:**
- Покреће Spring Boot веб сервер на порту 8080
- Креира `ToolCallbackProvider` који чини наше калкулатор методе доступним као MCP алате
- `@Bean` анотација каже Spring-у да ово управља као компонентом коју други делови могу користити

### 2. Калкулатор сервис

**Фајл:** `CalculatorService.java`

Овде се обавља сва математика. Свакa метода је означена са `@Tool` да би била доступна кроз MCP:

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
    
    // Још операција калкулатора...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Кључне карактеристике:**

1. **`@Tool` анотација**: Ово каже MCP-у да ова метода може бити позвана од стране спољашњих клијената
2. **Јасни описи**: Сваки алат има опис који помаже AI моделима да разумеју када да га користе
3. **Конзистентан формат повратне вредности**: Све операције враћају људски читљиве стрингове као "5.00 + 3.00 = 8.00"
4. **Руковање грешкама**: Дељење са нулом и негативни корени враћају поруке о грешци

**Доступне операције:**
- `add(a, b)` - Сабаја два броја
- `subtract(a, b)` - Одузима други од првог
- `multiply(a, b)` - Множи два броја
- `divide(a, b)` - Делује први са другим (са провером за нулу)
- `power(base, exponent)` - Подиже основу на степен експонента
- `squareRoot(number)` - Израчунава квадратни корен (са провером за негативни број)
- `modulus(a, b)` - Враћа остатак при дељењу
- `absolute(number)` - Враћа апсолутну вредност
- `help()` - Враћа информације о свим операцијама

### 3. Директни MCP клијент

Погледајте [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Овај клијент користи `HttpClientStreamableHttpTransport` на `/mcp`, иницијализује везу,
шаље пинг серверу и прати пагинацију листе алата. Проверава да свих девет очекиваних алата
постоји и позива сваки од њих, укључујући `modulus` и `help`, без AI модела.

Тренутни конструктор захтева изгледа овако:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Протоколске грешке узрокују неуспех клијента уместо приказивања заблудног успеха. MCP клијент
се затвара коришћењем try-with-resources, укључујући ситуације када откривање или позив алата не успе.

### 4. Клијент са вештачком интелигенцијом

Погледајте [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
и [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` имплементира тренутни LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` га повезује са истом `/mcp` крајњом тачком као и SDK клијент.
`AiServices` открива алате и управља разговором позива и резултата алата.

Подразумевана имплементација је **GPT-5.6 Luna**, са изричито онемогућеним разумевањем (reasoning):

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ове подразумеване вредности важе за сваки захтев, укључујући праћење након извршења алата.
Клијент користи освеживи `BearerTokenCredential` подржан од `DefaultAzureCredential`
и опсег `https://ai.azure.com/.default`, а не једнократни токен прослеђен као API кључ.
Прихватају се URL-ови ресурса и URL-ови који већ завршавају са `/openai/v1`.

Бот чува ограничену историју разговора, штампа `Tool executed: ...` са стварним
MCP резултатом и неуспева ако одговор прескочи алате. Лупови са алатима су ограничени на четири пута.
Аутентификација, модели, MCP и грешке алата се пропагирају; аутоматски покушаји поновног покретања модела су онемогућени.
Како MCP транспорт/клијент, тако и службени OpenAI клијент се затварају у случају успеха или неуспеха.

## Покретање примера

### Корак 1: Покрените Калкулатор сервер

За сервер није потребна Azure конфигурација. Команде испод се покрећу из овог директоријума примера.
Пример користи порт **18081** да избегне конфликт са другим примером; подразумевани је 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP крајња тачка је `http://localhost:18081/mcp`. Информације о здрављу и откривању су на
`http://localhost:18081/health` и `http://localhost:18081/info`.
Streamable HTTP замењује стари SSE-only транспорт; `/sse` и `/v1/tools` нису крајње тачке.

### Корак 2: Тестирајте са директним клијентом

У другом PowerShell терминалу:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Улаз није потребан. Сви девет алата се испробавају. Очекују се аритметички резултати укључују
8, 6, 42, 5, 256, 4, 2 и 5.5, праћени помоћним текстом.

### Корак 3: Тестирајте са AI клијентом

Након аутентификације како је описано у претходним условима, конфигуришите AI клијента у истом терминалу:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Очекује се линија `Tool executed: add` са `41.80`, праћена одговором модела.
Режим са једним упитом излази без чекања на улаз. Да бисте покренули оригинални демонстрациони пример са четири питања:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Демо позива `add`, `squareRoot`, `help` и ланчане операције `power` па `divide`.
Очекују се нумерички одговори 41.8, 12 и 64. Овaј демо такође ради и ако се аргументи прескоче.

### Корак 4: Покрените интерактивног бота

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Унесите `Multiply 6 by 7 using the calculator service`, затим `exit` или `quit`.
Очекује се стварни резултат `multiply` алата 42. Празни редови се игноришу; EOF такође завршава сесију.
За неинтерактивни тест ове улазне тачке:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Обе AI улазне тачке прихватају `--prompt "question"`, `--demo` и `--interactive`.
Неважеће опције не успевају пре успостављања везе. Свакa Maven `-D...` аргумент је потпуно цитирана
за PowerShell. На Bash-у користите `export NAME=value` уместо `$env:NAME = "value"`.

**Квота:** Покрећите AI примере секвенцијално. Једноставан упит обично захтева два захтева модели;
комплетан демо углавном треба девет, укључујући и праћења резултата алата. При дељењу 10 RPM
имплементације, сачекајте нови прозор квоте пре следећег AI покретања. Код 429 изазива видљиви неуспех без
аутоматских покушаја; пратите смернице о поновном покушају сервиса. Стварни број захтева зависи од модела.
Оффлине тестови не троше квоту и не проверавају стварну доступност Луне или квалитет одговора.

### Конфигурација и заустављање

| Поставка | Подразумевано / понашање |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; основни URL, без `/mcp` |
| `-Dmcp.server.url=...` | Превазилази `MCP_SERVER_URL` за све клијенте |
| `AZURE_OPENAI_ENDPOINT` | Захтева се само за AI клијенте; URL ресурса или `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; име Azure имплементације |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; позитиван цео број |
| Напор размишљања | Увек `none`, укључујући праћење у луповима алата |

Превлашћена имплементација мора подржавати `reasoning_effort=none` и `max_completion_tokens`.
Клијенти аутоматски не читају `.env` фајл. Зауставите сервер са `Ctrl+C` након тестирања.
Клијенти се нормално враћају без `System.exit` или заустављања са спавањем.

## Оффлине тестови

```powershell
mvn -B -ntp clean verify
```

Сви тестови су оффлине у односу на Azure: протокол покреће Spring сервер и
OpenAI- компатибилни stub на насумичним локалним портовима, који се потом затварају. Maven може ипак
морати да преузме зависности. Није неопходно имати креденцијале, живу имплементацију или постојећи MCP сервер.

- Јединични тестови калкулатора обухватају све аритметичке операције, децималне резултате, помоћ и грешке домена.
- MCP тестови обухватају иницијализацију, откривање, све девет позива алата, неуспехе алата и здравље/информације.
- AI протоколски тестови извршавају цео демо и интерактивног бота против правог калкулатора,
  проверавају да резултати алата утичу на следећи захтев и анализирају сваки HTTP тело за Луну,
  `reasoning_effort: "none"` и `max_completion_tokens` без застарелог `max_tokens`.
- Тестови конфигурације/улаза покривају имплементације и замену крајњих тачака, празне редове, EOF, exit/quit,
  режим једног упита, неважеће опције и пропагацију грешака. Тестови квота доказују да 429 није поново покушан.

## Како све функционише заједно

Ево целокупног тока када питате AI "Колико је 5 + 3?":

1. **Ви** питате AI на природном језику
2. **AI** анализира ваш захтев и схвата да желите сабирање
3. **AI** позива MCP сервер: `add(5.0, 3.0)`
4. **Калкулатор сервис** извршава: `5.0 + 3.0 = 8.0`
5. **Калкулатор сервис** враћа: `"5.00 + 3.00 = 8.00"`
6. **AI** прима резултат и форматира природан одговор
7. **Ви** добијате: "Збир бројева 5 и 3 је 8"

## Следећи кораци

За више примера погледајте [Поглавље 04: Практични примерци](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Изјава о одрицању одговорности**:
Овај документ је преведен коришћењем услуге за аутоматски превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо тачности, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било каква неспоразума или погрешна тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->