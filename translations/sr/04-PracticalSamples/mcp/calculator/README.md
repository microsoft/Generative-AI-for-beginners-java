<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:17:48+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "sr"
}
-->
# MCP Туторијал за почетнике

## Садржај

- [Шта ћете научити](../../../../../04-PracticalSamples/mcp/calculator)
- [Предуслови](../../../../../04-PracticalSamples/mcp/calculator)
- [Разумевање структуре пројекта](../../../../../04-PracticalSamples/mcp/calculator)
- [Објашњење основних компоненти](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Главна апликација](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Сервис калкулатора](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Директни MCP клијент](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Клијент са вештачком интелигенцијом](../../../../../04-PracticalSamples/mcp/calculator)
- [Покретање примера](../../../../../04-PracticalSamples/mcp/calculator)
- [Како све функционише заједно](../../../../../04-PracticalSamples/mcp/calculator)
- [Следећи кораци](../../../../../04-PracticalSamples/mcp/calculator)

## Шта ћете научити

Овај туторијал објашњава како да направите сервис калкулатора користећи Model Context Protocol (MCP). Разумећете:

- Како да направите сервис који вештачка интелигенција може да користи као алат
- Како да подесите директну комуникацију са MCP сервисима
- Како модели вештачке интелигенције могу аутоматски да бирају које алате да користе
- Разлику између директних позива протокола и интеракција уз помоћ вештачке интелигенције

## Предуслови

Пре него што почнете, уверите се да имате:
- Инсталиран Java 21 или новији
- Maven за управљање зависностима
- GitHub налог са персоналним приступним токеном (PAT)
- Основно разумевање Java и Spring Boot-а

## Разумевање структуре пројекта

Пројекат калкулатора садржи неколико важних фајлова:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Simple chat interface
```

## Објашњење основних компоненти

### 1. Главна апликација

**Фајл:** `McpServerApplication.java`

Ово је улазна тачка нашег сервиса калкулатора. То је стандардна Spring Boot апликација са једним посебним додатком:

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
- Креира `ToolCallbackProvider` који омогућава нашим методама калкулатора да буду доступне као MCP алати
- Анотација `@Bean` говори Spring-у да управља овим као компонентом коју други делови могу користити

### 2. Сервис калкулатора

**Фајл:** `CalculatorService.java`

Овде се обављају све математичке операције. Свака метода је означена са `@Tool` како би била доступна преко MCP-а:

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Кључне карактеристике:**

1. **Анотација `@Tool`**: Ово MCP-у говори да ову методу могу позивати спољни клијенти
2. **Јасни описи**: Сваки алат има опис који помаже моделима вештачке интелигенције да разумеју када да га користе
3. **Доследан формат повратних вредности**: Све операције враћају резултате у формату читљивом за људе, попут "5.00 + 3.00 = 8.00"
4. **Обрада грешака**: Дељење са нулом и негативни квадратни корени враћају поруке о грешци

**Доступне операције:**
- `add(a, b)` - Сабира два броја
- `subtract(a, b)` - Одузима други број од првог
- `multiply(a, b)` - Множи два броја
- `divide(a, b)` - Делу први број другим (са провером за нулу)
- `power(base, exponent)` - Подиже базу на степен експонента
- `squareRoot(number)` - Израчунава квадратни корен (са провером за негативне вредности)
- `modulus(a, b)` - Враћа остатак дељења
- `absolute(number)` - Враћа апсолутну вредност
- `help()` - Враћа информације о свим операцијама

### 3. Директни MCP клијент

**Фајл:** `SDKClient.java`

Овај клијент директно комуницира са MCP сервером без коришћења вештачке интелигенције. Ручно позива одређене функције калкулатора:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Шта ово ради:**
1. **Повезује се** са сервером калкулатора на `http://localhost:8080`
2. **Листира** све доступне алате (функције нашег калкулатора)
3. **Позива** одређене функције са тачним параметрима
4. **Исписује** резултате директно

**Када користити ово:** Када тачно знате коју операцију желите да извршите и желите да је позовете програмски.

### 4. Клијент са вештачком интелигенцијом

**Фајл:** `LangChain4jClient.java`

Овај клијент користи модел вештачке интелигенције (GPT-4o-mini) који може аутоматски да одлучи које алате калкулатора да користи:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Шта ово ради:**
1. **Креира** везу са моделом вештачке интелигенције користећи ваш GitHub токен
2. **Повезује** вештачку интелигенцију са нашим MCP сервером калкулатора
3. **Омогућава** вештачкој интелигенцији приступ свим алатима калкулатора
4. **Дозвољава** захтеве на природном језику, попут "Израчунај збир 24.5 и 17.3"

**Вештачка интелигенција аутоматски:**
- Разуме да желите да саберете бројеве
- Бира алат `add`
- Позива `add(24.5, 17.3)`
- Враћа резултат у природном одговору

## Покретање примера

### Корак 1: Покрените сервер калкулатора

Прво, подесите свој GitHub токен (потребан за клијент са вештачком интелигенцијом):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Покрените сервер:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Сервер ће се покренути на `http://localhost:8080`. Требало би да видите:
```
Started McpServerApplication in X.XXX seconds
```

### Корак 2: Тестирајте са директним клијентом

У новом терминалу:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Видећете излаз попут:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Корак 3: Тестирајте са клијентом вештачке интелигенције

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Видећете како вештачка интелигенција аутоматски користи алате:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Како све функционише заједно

Ево комплетног тока када питате вештачку интелигенцију "Колико је 5 + 3?":

1. **Ви** питате вештачку интелигенцију на природном језику
2. **Вештачка интелигенција** анализира ваш захтев и схвата да желите сабирање
3. **Вештачка интелигенција** позива MCP сервер: `add(5.0, 3.0)`
4. **Сервис калкулатора** извршава: `5.0 + 3.0 = 8.0`
5. **Сервис калкулатора** враћа: `"5.00 + 3.00 = 8.00"`
6. **Вештачка интелигенција** прима резултат и форматира природан одговор
7. **Ви** добијате: "Збир 5 и 3 је 8"

## Следећи кораци

За више примера, погледајте [Поглавље 04: Практични примери](../../README.md)

**Одрицање од одговорности**:  
Овај документ је преведен коришћењем услуге за превођење помоћу вештачке интелигенције [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да обезбедимо тачност, молимо вас да имате у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални превод од стране људи. Не преузимамо одговорност за било каква погрешна тумачења или неспоразуме који могу настати услед коришћења овог превода.