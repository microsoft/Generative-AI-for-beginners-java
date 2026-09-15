# Руководство по калькулятору MCP для начинающих

## Содержание

- [Что вы узнаете](#что-вы-узнаете)
- [Требования](#требования)
- [Версии зависимостей](#версии-зависимостей)
- [Понимание структуры проекта](#понимание-структуры-проекта)
- [Объяснение основных компонентов](#объяснение-основных-компонентов)
  - [1. Основное приложение](#1-основное-приложение)
  - [2. Сервис калькулятора](#2-сервис-калькулятора)
  - [3. Прямой клиент MCP](#3-прямой-клиент-mcp)
  - [4. Клиент с поддержкой ИИ](#4-клиент-с-поддержкой-ии)
- [Запуск примеров](#запуск-примеров)
- [Оффлайн тесты](#оффлайн-тесты)
- [Как всё работает вместе](#как-всё-работает-вместе)
- [Следующие шаги](#следующие-шаги)

## Что вы узнаете

В этом руководстве объясняется, как создать сервис калькулятора с использованием Протокола Контекста Модели (MCP). Вы поймёте:

- Как создать сервис, который ИИ может использовать как инструмент
- Как настроить прямую связь с сервисами MCP
- Как модели ИИ могут автоматически выбирать инструменты для использования
- Разницу между прямыми вызовами протокола и взаимодействиями с помощью ИИ

## Требования

Прежде чем начать, убедитесь, что у вас установлено:
- Java 21 или выше
- Maven для управления зависимостями
- Базовые знания Java и Spring Boot

Только клиентам ИИ требуется развертывание Azure OpenAI и аутентифицированный `DefaultAzureCredential`,
например, локальная аутентификация через Azure CLI или управляемая идентичность в Azure. Эта идентичность должна иметь
роль пользователя Cognitive Services OpenAI на ресурсе. См. [Глава 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Сервер, прямой клиент SDK и все автоматизированные тесты не требуют учётной записи Azure или доступа к модели.

## Версии зависимостей

Зависимости версии, проверенные на 14.09.2026:

| Зависимость | Версия |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (управляется Spring AI) | 2.0.0 |
| LangChain4j / ядро | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Официальный адаптер OpenAI LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (управляется Boot) | 6.0.3 |

MCP и официальные адаптеры OpenAI — это опубликованные бета-релизы в Maven Central, а не сниппеты.
Их версии отличаются от ядра LangChain4j. Не требуются репозитории сниппетов или milestone.
Зависимости только для клиентов имеют область тестирования, так как запускаемые примеры находятся в `src/test/java`.

## Понимание структуры проекта

В проекте калькулятора есть несколько важных файлов:

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

## Объяснение основных компонентов

### 1. Основное приложение

**Файл:** `McpServerApplication.java`

Это точка входа нашего сервиса калькулятора. Это стандартное приложение Spring Boot с одним особенным дополнением:

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
- Создаёт `ToolCallbackProvider`, который делает методы калькулятора доступными как инструменты MCP
- Аннотация `@Bean` сообщает Spring, что это компонент, которым могут пользоваться другие части

### 2. Сервис калькулятора

**Файл:** `CalculatorService.java`

Здесь происходит всё математическое вычисление. Каждый метод помечен `@Tool` для доступности через MCP:

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
    
    // Больше операций калькулятора...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Ключевые особенности:**

1. **Аннотация `@Tool`**: Сообщает MCP, что этот метод может вызываться внешними клиентами
2. **Понятные описания**: Каждый инструмент имеет описание, помогающее ИИ понять, когда его использовать
3. **Единый формат возврата**: Все операции возвращают удобочитаемые строки, например "5.00 + 3.00 = 8.00"
4. **Обработка ошибок**: Деление на ноль и отрицательный квадратный корень возвращают сообщения об ошибке

**Доступные операции:**
- `add(a, b)` - Сложение двух чисел
- `subtract(a, b)` - Вычитание второго из первого
- `multiply(a, b)` - Умножение двух чисел
- `divide(a, b)` - Деление первого на второе (с проверкой на ноль)
- `power(base, exponent)` - Возведение основания в степень
- `squareRoot(number)` - Вычисление квадратного корня (с проверкой на отрицательное число)
- `modulus(a, b)` - Остаток от деления
- `absolute(number)` - Модуль числа
- `help()` - Информация обо всех операциях

### 3. Прямой клиент MCP

См. [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Этот клиент использует `HttpClientStreamableHttpTransport` на `/mcp`, инициализирует соединение,
пингует сервер и обрабатывает постраничный список инструментов. Он проверяет наличие всех девяти ожидаемых инструментов
и вызывает каждый из них, включая `modulus` и `help`, без использования модели ИИ.

Текущий билдера запроса выглядит так:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Протокольные ошибки приводят к сбою клиента, а не к выводу ложного успеха. Клиент MCP
закрывается с помощью try-with-resources, включая случаи сбоев при обнаружении или вызове инструмента.

### 4. Клиент с поддержкой ИИ

См. [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
и [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` реализует текущий API LangChain4j `ChatModel`.
`StreamableHttpMcpTransport` подключает его к тому же эндпоинту `/mcp`, что и SDK клиент.
`AiServices` обнаруживает инструменты и управляет разговором вызова-инструмента и результата.

Развертывание по умолчанию — **GPT-5.6 Luna**, с явно отключённым рассуждением:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Эти параметры применяются ко всем завершениям, включая последующие после выполнения инструментов.
Клиент использует обновляемый `BearerTokenCredential`, основанный на `DefaultAzureCredential`
и области `https://ai.azure.com/.default`, а не одноразовый токен API.
Принимаются как URL ресурсов, так и URL, уже оканчивающиеся на `/openai/v1`.

Бот хранит ограниченную историю беседы, выводит `Tool executed: ...` с реальным
результатом MCP и завершает работу, если ответ пропускает инструменты. Циклы инструмента ограничены четырьмя проходами.
Ошибки аутентификации, модели, MCP и инструментов распространяются; автоматические повторы модели отключены.
И MCP транспорт/клиент, и официальный клиент OpenAI закрываются при успехе или ошибке.

## Запуск примеров

### Шаг 1: Запуск сервера калькулятора

Для сервера не требуется конфигурация Azure. Команды ниже выполняются из каталога этого примера.
Пример использует порт **18081**, чтобы избежать конфликта с другим примером; по умолчанию остаётся 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Точка доступа MCP — `http://localhost:18081/mcp`. Информация о состоянии и обнаружении доступна по адресам
`http://localhost:18081/health` и `http://localhost:18081/info`.
Потоковый HTTP заменяет старый транспорт только с SSE; `/sse` и `/v1/tools` не являются эндпоинтами.

### Шаг 2: Тест с прямым клиентом

В другом терминале PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Ввод данных не требуется. Проверяются все девять инструментов. Ожидаемые арифметические результаты — 
8, 6, 42, 5, 256, 4, 2 и 5.5, после чего выводится справочная информация.

### Шаг 3: Тест с клиентом ИИ

После аутентификации, описанной в требованиях, настройте клиент ИИ в том же терминале:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Ожидайте строку `Tool executed: add` с результатом `41.80`, затем ответ модели.
Режим с одним запросом выходит без ожидания ввода. Для запуска оригинального демо с четырьмя запросами:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Демо вызывает `add`, `squareRoot`, `help` и цепочку `power` с последующим `divide`.
Ожидаемые числовые ответы — 41.8, 12 и 64. Запуск демо возможен и без аргументов.

### Шаг 4: Запуск интерактивного бота

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Введите `Multiply 6 by 7 using the calculator service`, затем `exit` или `quit`.
Ожидайте реальный результат `multiply` равный 42. Пустые строки игнорируются; EOF также завершает сессию.
Для неинтерактивного "дымового" теста этой точки входа:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Оба ИИ точки входа принимают `--prompt "question"`, `--demo` и `--interactive`.
Некорректные опции приводят к ошибке ещё до открытия соединения. Каждый Maven аргумент `-D...` требуется полностью обернуть
для PowerShell. В Bash используйте `export NAME=value` вместо `$env:NAME = "value"`.

**Квота:** Запускайте ИИ-примеры последовательно. Простой запрос обычно требует двух обращений к модели;
полное демо — около девяти, включая продолжения после вызова инструментов. При совместном использовании развертывания с 10 RPM
оставляйте интервал перед следующим запуском ИИ. Код 429 вызывает явный сбой без
автоматических повторов; следуйте рекомендациям по retry-after. Фактическое количество запросов зависит от модели.
Оффлайн тесты не расходуют квоты и не проверяют живую доступность Luna или качество ответов.

### Конфигурация и завершение работы

| Параметр | Значение по умолчанию / поведение |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; базовый URL без `/mcp` |
| `-Dmcp.server.url=...` | Переопределяет `MCP_SERVER_URL` для всех клиентов |
| `AZURE_OPENAI_ENDPOINT` | Обязательно только для клиентов ИИ; URL ресурса или URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; имя развертывания Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; положительное целое число |
| Усилия рассуждения | Всегда `none`, включая продолжения циклов инструментов |

Переопределённое развертывание должно поддерживать `reasoning_effort=none` и `max_completion_tokens`.
Клиенты не читают файл `.env` автоматически. Остановите сервер с помощью `Ctrl+C` после тестирования.
Клиенты завершают работу нормально, без `System.exit` или задержек при выключении.

## Оффлайн тесты

```powershell
mvn -B -ntp clean verify
```

Все тесты оффлайн относительно Azure: протокольный набор запускает сервер Spring и
OpenAI-совместимый заглушку на случайных портах loopback, затем закрывает их. Maven может потребовать
загрузить зависимости. Учётные данные, живое развертывание или предварительно существующий MCP сервер не используются.

- Юнит-тесты калькулятора покрывают все арифметические операции, десятичные результаты, помощь и ошибки домена.
- MCP тесты покрывают инициализацию, обнаружение, все девять вызовов инструментов, ошибки инструментов и состояние/информацию.
- Тесты протокола ИИ выполняют полное демо и интерактивного бота с реальным калькулятором,
  проверяя, что результаты инструментов подают следующие завершения, и проверяют каждое тело HTTP на Luna,
  `reasoning_effort: "none"` и `max_completion_tokens` без устаревшего `max_tokens`.
- Конфигурационные/входные тесты охватывают переопределения развертывания и эндпоинта, пустые строки, EOF, exit/quit,
  режим с одним запросом, некорректные опции и распространение ошибок. Тесты квоты подтверждают, что 429 не повторяется.

## Как всё работает вместе

Вот полный процесс, когда вы спрашиваете ИИ "Сколько будет 5 + 3?":

1. **Вы** задаёте ИИ вопрос на естественном языке
2. **ИИ** анализирует запрос и понимает, что требуется сложение
3. **ИИ** вызывает MCP сервер: `add(5.0, 3.0)`
4. **Сервис калькулятора** выполняет: `5.0 + 3.0 = 8.0`
5. **Сервис калькулятора** возвращает: `"5.00 + 3.00 = 8.00"`
6. **ИИ** получает результат и формирует ответ на естественном языке
7. **Вы** получаете: "Сумма 5 и 3 равна 8"

## Следующие шаги

Для дополнительных примеров смотрите [Глава 04: Практические примеры](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:
Этот документ был переведен с использованием сервиса машинного перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия по обеспечению точности, имейте в виду, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на его исходном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется обратиться к профессиональному человеческому переводу. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования этого перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->