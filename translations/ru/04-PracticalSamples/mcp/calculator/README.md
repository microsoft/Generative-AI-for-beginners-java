<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T10:42:11+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "ru"
}
-->
# Учебник по MCP-калькулятору для начинающих

## Содержание

- [Чему вы научитесь](../../../../../04-PracticalSamples/mcp/calculator)
- [Предварительные требования](../../../../../04-PracticalSamples/mcp/calculator)
- [Понимание структуры проекта](../../../../../04-PracticalSamples/mcp/calculator)
- [Объяснение основных компонентов](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Основное приложение](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Сервис калькулятора](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Клиент MCP с прямым доступом](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. Клиент с поддержкой ИИ](../../../../../04-PracticalSamples/mcp/calculator)
- [Запуск примеров](../../../../../04-PracticalSamples/mcp/calculator)
- [Как все работает вместе](../../../../../04-PracticalSamples/mcp/calculator)
- [Следующие шаги](../../../../../04-PracticalSamples/mcp/calculator)

## Чему вы научитесь

Этот учебник объясняет, как создать сервис калькулятора с использованием Model Context Protocol (MCP). Вы узнаете:

- Как создать сервис, который ИИ может использовать как инструмент
- Как настроить прямую связь с MCP-сервисами
- Как модели ИИ автоматически выбирают, какие инструменты использовать
- Разницу между прямыми вызовами протокола и взаимодействиями с поддержкой ИИ

## Предварительные требования

Перед началом убедитесь, что у вас есть:
- Установленный Java версии 21 или выше
- Maven для управления зависимостями
- Аккаунт на GitHub с персональным токеном доступа (PAT)
- Базовые знания Java и Spring Boot

## Понимание структуры проекта

Проект калькулятора содержит несколько важных файлов:

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

## Объяснение основных компонентов

### 1. Основное приложение

**Файл:** `McpServerApplication.java`

Это точка входа для нашего сервиса калькулятора. Это стандартное приложение Spring Boot с одним особым дополнением:

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

**Что это делает:**
- Запускает веб-сервер Spring Boot на порту 8080
- Создает `ToolCallbackProvider`, который делает методы калькулятора доступными как инструменты MCP
- Аннотация `@Bean` указывает Spring управлять этим компонентом, чтобы другие части могли его использовать

### 2. Сервис калькулятора

**Файл:** `CalculatorService.java`

Здесь выполняются все математические операции. Каждый метод помечен аннотацией `@Tool`, чтобы сделать его доступным через MCP:

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

**Основные особенности:**

1. **Аннотация `@Tool`**: Указывает MCP, что этот метод может быть вызван внешними клиентами
2. **Четкие описания**: Каждый инструмент имеет описание, которое помогает моделям ИИ понять, когда его использовать
3. **Единый формат возврата**: Все операции возвращают читаемые строки, например "5.00 + 3.00 = 8.00"
4. **Обработка ошибок**: Деление на ноль и отрицательные квадратные корни возвращают сообщения об ошибках

**Доступные операции:**
- `add(a, b)` - Сложение двух чисел
- `subtract(a, b)` - Вычитание второго числа из первого
- `multiply(a, b)` - Умножение двух чисел
- `divide(a, b)` - Деление первого числа на второе (с проверкой на ноль)
- `power(base, exponent)` - Возведение основания в степень
- `squareRoot(number)` - Вычисление квадратного корня (с проверкой на отрицательные значения)
- `modulus(a, b)` - Остаток от деления
- `absolute(number)` - Абсолютное значение
- `help()` - Информация обо всех операциях

### 3. Клиент MCP с прямым доступом

**Файл:** `SDKClient.java`

Этот клиент напрямую взаимодействует с сервером MCP без использования ИИ. Он вручную вызывает конкретные функции калькулятора:

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

**Что это делает:**
1. **Подключается** к серверу калькулятора по адресу `http://localhost:8080`
2. **Список** всех доступных инструментов (функций калькулятора)
3. **Вызывает** конкретные функции с точными параметрами
4. **Выводит** результаты напрямую

**Когда использовать:** Когда вы точно знаете, какую операцию хотите выполнить, и хотите вызвать ее программно.

### 4. Клиент с поддержкой ИИ

**Файл:** `LangChain4jClient.java`

Этот клиент использует модель ИИ (GPT-4o-mini), которая может автоматически решать, какие инструменты калькулятора использовать:

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

**Что это делает:**
1. **Создает** подключение к модели ИИ с использованием вашего токена GitHub
2. **Подключает** ИИ к серверу MCP калькулятора
3. **Предоставляет** ИИ доступ ко всем инструментам калькулятора
4. **Позволяет** выполнять запросы на естественном языке, например "Вычисли сумму 24.5 и 17.3"

**ИИ автоматически:**
- Понимает, что нужно сложить числа
- Выбирает инструмент `add`
- Вызывает `add(24.5, 17.3)`
- Возвращает результат в естественном ответе

## Запуск примеров

### Шаг 1: Запустите сервер калькулятора

Сначала установите токен GitHub (необходим для клиента ИИ):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Запустите сервер:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Сервер запустится по адресу `http://localhost:8080`. Вы должны увидеть:
```
Started McpServerApplication in X.XXX seconds
```

### Шаг 2: Тестирование с прямым клиентом

В новом терминале:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Вы увидите вывод, похожий на:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Шаг 3: Тестирование с клиентом ИИ

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Вы увидите, как ИИ автоматически использует инструменты:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Как все работает вместе

Вот полный процесс, когда вы спрашиваете ИИ "Сколько будет 5 + 3?":

1. **Вы** задаете вопрос ИИ на естественном языке
2. **ИИ** анализирует ваш запрос и понимает, что нужно сложить числа
3. **ИИ** вызывает сервер MCP: `add(5.0, 3.0)`
4. **Сервис калькулятора** выполняет: `5.0 + 3.0 = 8.0`
5. **Сервис калькулятора** возвращает: `"5.00 + 3.00 = 8.00"`
6. **ИИ** получает результат и формирует естественный ответ
7. **Вы** получаете: "Сумма 5 и 3 равна 8"

## Следующие шаги

Для получения дополнительных примеров смотрите [Глава 04: Практические примеры](../../README.md)

**Отказ от ответственности**:  
Этот документ был переведен с использованием сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Хотя мы стремимся к точности, пожалуйста, имейте в виду, что автоматические переводы могут содержать ошибки или неточности. Оригинальный документ на его исходном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные интерпретации, возникшие в результате использования данного перевода.