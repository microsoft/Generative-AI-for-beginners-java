# Наръчник за MCP калкулатор за начинаещи

## Съдържание

- [Какво ще научите](#какво-ще-научите)
- [Изисквания](#изисквания)
- [Версии на зависимости](#версии-на-зависимости)
- [Разбиране на структурата на проекта](#разбиране-на-структурата-на-проекта)
- [Обяснение на основните компоненти](#обяснение-на-основните-компоненти)
  - [1. Основно приложение](#1-основно-приложение)
  - [2. Услуга Калкулатор](#2-услуга-калкулатор)
  - [3. Директен MCP клиент](#3-директен-mcp-клиент)
  - [4. Клиент с изкуствен интелект](#4-клиент-с-изкуствен-интелект)
- [Стартиране на примерите](#стартиране-на-примерите)
- [Офлайн тестове](#офлайн-тестове)
- [Как всичко работи заедно](#как-всичко-работи-заедно)
- [Следващи стъпки](#следващи-стъпки)

## Какво ще научите

Този урок обяснява как да създадете услуга калкулатор, използвайки Model Context Protocol (MCP). Ще разберете:

- Как да създадете услуга, която AI може да използва като инструмент
- Как да настроите директна комуникация с MCP услуги
- Как AI моделите автоматично избират кои инструменти да използват
- Разликата между директни протоколни повиквания и AI-подпомагани взаимодействия

## Изисквания

Преди да започнете, уверете се, че имате:
- Инсталиран Java 21 или по-нова версия
- Maven за управление на зависимости
- Основни познания по Java и Spring Boot

Само AI клиентите изискват разгръщане на Azure OpenAI и удостоверен `DefaultAzureCredential`,
като съществуващ вход с Azure CLI локално или управлявана идентичност в Azure. Идентичността се нуждае
от роля "Потребител на Cognitive Services OpenAI" върху ресурса. Вижте [Глава 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Сървърът, директният SDK клиент и всички автоматизирани тестове не изискват Azure акаунт или достъп до модел.

## Версии на зависимости

Потвърдени зависимости към 14.09.2026:

| Зависимост | Версия |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (управляван от Spring AI) | 2.0.0 |
| LangChain4j / ядро | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Официален OpenAI адаптер за LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (управляван от Boot) | 6.0.3 |

MCP и официалните OpenAI адаптери са публикувани бета версии в Maven Central, не снимки.
Техните версии се различават от тези на LangChain4j ядрото. Не са необходими snapshot или milestone хранилища.
Зависимостите само за клиенти са с обхват test, защото изпълнимите примери се намират в `src/test/java`.

## Разбиране на структурата на проекта

Проектът калкулатор съдържа няколко важни файла:

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

## Обяснение на основните компоненти

### 1. Основно приложение

**Файл:** `McpServerApplication.java`

Това е входната точка на нашата услуга калкулатор. Това е стандартно Spring Boot приложение с една специална добавка:

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

**Какво прави това:**
- Стартира Spring Boot уеб сървър на порт 8080
- Създава `ToolCallbackProvider`, който прави методите на калкулатора ни достъпни като MCP инструменти
- Анотацията `@Bean` казва на Spring да управлява този компонент, който може да използват други части

### 2. Услуга Калкулатор

**Файл:** `CalculatorService.java`

Тук се извършват всички математически операции. Всеки метод е обозначен с `@Tool`, за да бъде достъпен чрез MCP:

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
    
    // Още операции на калкулатора...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Основни характеристики:**

1. **Анотация `@Tool`**: Казва на MCP, че този метод може да бъде извикан от външни клиенти
2. **Ясни описания**: Всеки инструмент има описание, което помага на AI моделите да разберат кога да го използват
3. **Консистентен формат на връщане**: Всички операции връщат четими за хора резултати като "5.00 + 3.00 = 8.00"
4. **Обработка на грешки**: Деление на нула и отрицателни квадратни корени връщат съобщения за грешка

**Налични операции:**
- `add(a, b)` - Събира две числа
- `subtract(a, b)` - Изважда второто от първото
- `multiply(a, b)` - Умножава две числа
- `divide(a, b)` - Деление на първото число на второто (с проверка за нула)
- `power(base, exponent)` - Вдига основата на степен
- `squareRoot(number)` - Изчислява квадратен корен (с проверка за отрицателно число)
- `modulus(a, b)` - Връща остатъка от деление
- `absolute(number)` - Връща абсолютна стойност
- `help()` - Връща информация за всички операции

### 3. Директен MCP клиент

Вижте [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Този клиент използва `HttpClientStreamableHttpTransport` на `/mcp`, инициализира връзката,
прави пинг към сървъра и следи страницирането на списъка с инструменти. Проверява, че всички девет очаквани инструмента
съществуват, и извиква всеки от тях, включително `modulus` и `help`, без AI модел.

Текущият конструктор на заявки изглежда така:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Грешките в протокола водят до провал на клиента, вместо да отпечатват подвеждащо успешно съобщение. MCP клиентът
се затваря с try-with-resources, включително при неуспех при откриване или повикване на инструмент.

### 4. Клиент с изкуствен интелект

Вижте [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
и [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` реализира текущото API `ChatModel` на LangChain4j.
`StreamableHttpMcpTransport` го свързва към същата крайна точка `/mcp` като SDK клиента.
`AiServices` открива инструментите и управлява разговора за повикване и резултат от инструмент.

По подразбиране разгръщането е **GPT-5.6 Luna**, с изрично изключено разсъждаване:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Тези настройки важат за всяко допълнение, включително последващи след изпълнение на инструмента.
Клиентът използва подновяем `BearerTokenCredential`, подкрепен от `DefaultAzureCredential`
и обхвата `https://ai.azure.com/.default`, а не еднократен токен, предаден като API ключ.
Приемат се както URL на ресурси, така и URL, завършващи с `/openai/v1`.

Ботът поддържа ограничена история на разговори, отпечатва `Tool executed: ...` с действителния
MCP резултат и се проваля, ако няма използвани инструменти в отговора. Циклите с инструменти са ограничени до четири кръга.
Грешките при удостоверяване, модел, MCP и инструменти се предават; автоматични опити за модел са деактивирани.
И MCP транспорта/клиентът, и официалния OpenAI клиент се затварят при успех или провал.

## Стартиране на примерите

### Стъпка 1: Стартиране на сървъра на калкулатора

За сървъра не е необходима никаква Azure конфигурация. Командите по-долу се изпълняват от директорията на този пример.
Примерът използва порт **18081**, за да избегне конфликт с друг пример; по подразбиране остава 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Крайна точка за MCP е `http://localhost:18081/mcp`. Информация за здравето и откриването е на
`http://localhost:18081/health` и `http://localhost:18081/info`.
Streamable HTTP заменя стария транспорт само с SSE; `/sse` и `/v1/tools` не са крайни точки.

### Стъпка 2: Тест с директен клиент

В друг PowerShell терминал:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Не е необходим вход. Всички девет инструмента се упражняват. Очаквани аритметични резултати включват
8, 6, 42, 5, 256, 4, 2 и 5.5, последвани от текста на помощта.

### Стъпка 3: Тест с AI клиент

След удостоверяване, описано в изискванията, конфигурирайте AI клиента в същия терминал:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Очаквайте ред с `Tool executed: add` с `41.80`, последван от отговора на модела.
Режимът с един подкан излиза без да чака вход. За да стартирате оригиналното демо с четири подканвания:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Демото извиква `add`, `squareRoot`, `help` и последователната операция `power`, след това `divide`.
Очаквани числени отговори са 41.8, 12 и 64. Липсата на аргументи също стартира това демо.

### Стъпка 4: Стартиране на интерактивния бот

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Въведете `Multiply 6 by 7 using the calculator service`, след това `exit` или `quit`.
Очаквайте действителен резултат от инструмента `multiply` с 42. Празни редове се игнорират; EOF също прекратява сесията.
За неинтерактивен тест на тази входна точка:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

И двете AI входни точки приемат `--prompt "question"`, `--demo` и `--interactive`.
Невалидни опции предизвикват грешка преди отваряне на връзка. Всеки Maven `-D...` аргумент е напълно кавичкиран
за PowerShell. В Bash използвайте `export NAME=value` вместо `$env:NAME = "value"`.

**Квота:** Стартирайте AI примерите последователно. Една проста подкан обикновено изисква две заявки към модела;
пълното демо обикновено изисква девет, включително последващи заявки за резултати от инструменти. При споделено разгръщане с 10 RPM
изчакайте нов прозорец за квота преди следващото изпълнение на AI. 429 грешка се проваля видимо без
автоматични опити; следвайте указанията за повторен опит на услугата. Реалният брой заявки зависи от модела.
Офлайн тестовете не използват квота и не осигуряват наличие на Luna или качество на отговорите.

### Конфигурация и спиране

| Настройка | По подразбиране / поведение |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; базов URL, без `/mcp` |
| `-Dmcp.server.url=...` | Презаписва `MCP_SERVER_URL` за всички клиенти |
| `AZURE_OPENAI_ENDPOINT` | Изисква се само за AI клиенти; URL на ресурс или `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; име на Azure разгръщане |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; положително цяло число |
| Усилие за разсъждаване | Винаги `none`, включително последващи след инструментални цикли |

Презаписаното разгръщане трябва да поддържа `reasoning_effort=none` и `max_completion_tokens`.
Клиентите не четат .env файл автоматично. Спирайте сървъра с `Ctrl+C` след тестове.
Клиентите се връщат нормално без `System.exit` или забавяния при спиране.

## Офлайн тестове

```powershell
mvn -B -ntp clean verify
```

Всички тестове са офлайн спрямо Azure: протоколният пакет стартира Spring сървър и
OpenAI-совместим stub на случайни локални портове, след което ги затваря. Maven може да се нуждае
да изтегли зависимости. Не се използват креденшъли, живо разгръщане или съществуващ MCP сървър.

- Модулните тестове на калкулатора покриват всички аритметични операции, десетични резултати, помощ и домейн грешки.
- MCP тестовете покриват инициализация, откриване, всички девет повиквания на инструменти, неизправности и здравословен статус/информация.
- АI протоколните тестове изпълняват пълното демо и интерактивния бот срещу реалния калкулатор,
  проверяват, че резултатите от инструменти захранват следващото допълнение, и инспектират всеки HTTP body за Luna,
  `reasoning_effort: "none"` и `max_completion_tokens` без наследени `max_tokens`.
- Тестовете за конфигурация/вход покриват разгръщане и крайна точка, празни редове, EOF, exit/quit,
  режим с една подкан, невалидни опции и разпространение на грешки. Тестовете за квота доказват, че 429 не се повтаря.

## Как всичко работи заедно

Ето целия поток, когато попитате AI "Колко е 5 + 3?":

1. **Вие** питате AI на естествен език
2. **AI** анализира вашето запитване и осъзнава, че искате събиране
3. **AI** извиква MCP сървъра: `add(5.0, 3.0)`
4. **Услугата Калкулатор** изчислява: `5.0 + 3.0 = 8.0`
5. **Услугата Калкулатор** връща: `"5.00 + 3.00 = 8.00"`
6. **AI** получава резултата и форматира естествен отговор
7. **Вие** получавате: "Сумата на 5 и 3 е 8"

## Следващи стъпки

За повече примери вижте [Глава 04: Практически примери](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводачески услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Ние не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->