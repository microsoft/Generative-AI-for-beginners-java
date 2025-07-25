<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T12:28:25+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "uk"
}
-->
# Підручник MCP Calculator для початківців

## Зміст

- [Що ви дізнаєтесь](../../../../../04-PracticalSamples/mcp/calculator)
- [Передумови](../../../../../04-PracticalSamples/mcp/calculator)
- [Розуміння структури проєкту](../../../../../04-PracticalSamples/mcp/calculator)
- [Пояснення основних компонентів](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Головний додаток](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Сервіс калькулятора](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Прямий MCP клієнт](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Клієнт на основі AI](../../../../../04-PracticalSamples/mcp/calculator)
- [Запуск прикладів](../../../../../04-PracticalSamples/mcp/calculator)
- [Як усе працює разом](../../../../../04-PracticalSamples/mcp/calculator)
- [Наступні кроки](../../../../../04-PracticalSamples/mcp/calculator)

## Що ви дізнаєтесь

Цей підручник пояснює, як створити сервіс калькулятора, використовуючи Model Context Protocol (MCP). Ви зрозумієте:

- Як створити сервіс, який AI може використовувати як інструмент
- Як налаштувати пряме спілкування з MCP сервісами
- Як AI моделі можуть автоматично вибирати, які інструменти використовувати
- Різницю між прямими викликами протоколу та взаємодією за допомогою AI

## Передумови

Перед початком переконайтеся, що у вас є:
- Встановлений Java 21 або новіший
- Maven для управління залежностями
- Обліковий запис GitHub із персональним токеном доступу (PAT)
- Базове розуміння Java та Spring Boot

## Розуміння структури проєкту

Проєкт калькулятора має кілька важливих файлів:

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

## Пояснення основних компонентів

### 1. Головний додаток

**Файл:** `McpServerApplication.java`

Це точка входу для нашого сервісу калькулятора. Це стандартний Spring Boot додаток із одним особливим доповненням:

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

**Що це робить:**
- Запускає вебсервер Spring Boot на порту 8080
- Створює `ToolCallbackProvider`, який робить методи калькулятора доступними як MCP інструменти
- Анотація `@Bean` вказує Spring управляти цим як компонентом, який можуть використовувати інші частини

### 2. Сервіс калькулятора

**Файл:** `CalculatorService.java`

Тут відбуваються всі математичні операції. Кожен метод позначений `@Tool`, щоб зробити його доступним через MCP:

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

**Основні особливості:**

1. **Анотація `@Tool`**: Вказує MCP, що цей метод може викликатися зовнішніми клієнтами
2. **Чіткі описи**: Кожен інструмент має опис, який допомагає AI моделям зрозуміти, коли його використовувати
3. **Послідовний формат повернення**: Усі операції повертають зрозумілі рядки, наприклад "5.00 + 3.00 = 8.00"
4. **Обробка помилок**: Ділення на нуль і негативні квадратні корені повертають повідомлення про помилку

**Доступні операції:**
- `add(a, b)` - Додає два числа
- `subtract(a, b)` - Віднімає друге число від першого
- `multiply(a, b)` - Множить два числа
- `divide(a, b)` - Ділить перше число на друге (з перевіркою на нуль)
- `power(base, exponent)` - Підносить основу до степеня
- `squareRoot(number)` - Обчислює квадратний корінь (з перевіркою на негативні значення)
- `modulus(a, b)` - Повертає залишок від ділення
- `absolute(number)` - Повертає абсолютне значення
- `help()` - Повертає інформацію про всі операції

### 3. Прямий MCP клієнт

**Файл:** `SDKClient.java`

Цей клієнт напряму спілкується з MCP сервером без використання AI. Він вручну викликає конкретні функції калькулятора:

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

**Що це робить:**
1. **Підключається** до сервера калькулятора за адресою `http://localhost:8080`
2. **Перелічує** всі доступні інструменти (функції калькулятора)
3. **Викликає** конкретні функції з точними параметрами
4. **Друкує** результати напряму

**Коли це використовувати:** Коли ви точно знаєте, яку операцію хочете виконати, і хочете викликати її програмно.

### 4. Клієнт на основі AI

**Файл:** `LangChain4jClient.java`

Цей клієнт використовує AI модель (GPT-4o-mini), яка може автоматично вирішувати, які інструменти калькулятора використовувати:

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

**Що це робить:**
1. **Створює** підключення до AI моделі, використовуючи ваш GitHub токен
2. **Підключає** AI до нашого MCP сервера калькулятора
3. **Надає** AI доступ до всіх інструментів калькулятора
4. **Дозволяє** запити природною мовою, наприклад "Обчисли суму 24.5 і 17.3"

**AI автоматично:**
- Розуміє, що ви хочете додати числа
- Вибирає інструмент `add`
- Викликає `add(24.5, 17.3)`
- Повертає результат у природній відповіді

## Запуск прикладів

### Крок 1: Запустіть сервер калькулятора

Спочатку встановіть ваш GitHub токен (потрібний для AI клієнта):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Запустіть сервер:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Сервер запуститься за адресою `http://localhost:8080`. Ви побачите:
```
Started McpServerApplication in X.XXX seconds
```

### Крок 2: Тестування з прямим клієнтом

У новому терміналі:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Ви побачите вихідні дані, наприклад:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Крок 3: Тестування з AI клієнтом

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Ви побачите, як AI автоматично використовує інструменти:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Як усе працює разом

Ось повний процес, коли ви запитуєте AI "Скільки буде 5 + 3?":

1. **Ви** запитуєте AI природною мовою
2. **AI** аналізує ваш запит і розуміє, що ви хочете додати числа
3. **AI** викликає MCP сервер: `add(5.0, 3.0)`
4. **Сервіс калькулятора** виконує: `5.0 + 3.0 = 8.0`
5. **Сервіс калькулятора** повертає: `"5.00 + 3.00 = 8.00"`
6. **AI** отримує результат і форматує природну відповідь
7. **Ви** отримуєте: "Сума 5 і 3 дорівнює 8"

## Наступні кроки

Для більше прикладів дивіться [Розділ 04: Практичні зразки](../../README.md)

**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критичної інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.