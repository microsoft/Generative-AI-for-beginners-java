# Посібник для початківців з MCP Calculator

## Зміст

- [Чого ви навчитесь](#чого-ви-навчитесь)
- [Вимоги](#вимоги)
- [Версії залежностей](#версії-залежностей)
- [Розуміння структури проєкту](#розуміння-структури-проєкту)
- [Пояснення основних компонентів](#пояснення-основних-компонентів)
  - [1. Основна програма](#1-основна-програма)
  - [2. Сервіс калькулятора](#2-сервіс-калькулятора)
  - [3. Прямий MCP клієнт](#3-прямий-mcp-клієнт)
  - [4. Клієнт з AI](#4-клієнт-з-ai)
- [Запуск прикладів](#запуск-прикладів)
- [Офлайн тести](#офлайн-тести)
- [Як це працює разом](#як-це-працює-разом)
- [Наступні кроки](#наступні-кроки)

## Чого ви навчитесь

У цьому посібнику пояснюється, як створити сервіс калькулятора за допомогою Model Context Protocol (MCP). Ви зрозумієте:

- Як створити сервіс, який штучний інтелект може використовувати як інструмент
- Як налаштувати прямий зв’язок з MCP сервісами
- Як AI-моделі можуть автоматично вибирати, які інструменти використовувати
- Різницю між прямими викликами протоколу та взаємодією з підтримкою AI

## Вимоги

Перед початком переконайтеся, що у вас є:
- Встановлена Java 21 або новіша
- Maven для керування залежностями
- Базові знання Java та Spring Boot

Лише AI клієнти потребують розгортання Azure OpenAI та автентифікованого `DefaultAzureCredential`,
наприклад, існуючого входу в Azure CLI локально або керованої автентичності в Azure. Ідентичність має
роль користувача Cognitive Services OpenAI на ресурсі. Дивіться [Розділ 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Сервер, прямий SDK клієнт і всі автоматизовані тести не потребують облікового запису Azure або доступу до моделі.

## Версії залежностей

Версії релізів перевірені станом на 2026-09-14:

| Залежність | Версія |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (керується Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Офіційний адаптер LangChain4j OpenAI | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (керується Boot) | 6.0.3 |

MCP та офіційні OpenAI адаптери — це публічні beta-релізи в Maven Central, не снапшоти.
Їх версії відрізняються від LangChain4j core. Не потрібні снапшоти або milestone репозиторії.
Залежності лише клієнтів мають тестову область, бо приклади запускаються з `src/test/java`.

## Розуміння структури проєкту

Проєкт калькулятора містить кілька важливих файлів:

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

## Пояснення основних компонентів

### 1. Основна програма

**Файл:** `McpServerApplication.java`

Це вхідна точка нашого сервісу калькулятора. Це стандартний Spring Boot додаток із одним спеціальним доповненням:

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
- Запускає Spring Boot веб-сервер на порті 8080
- Створює `ToolCallbackProvider`, який робить методи калькулятора доступними як інструменти MCP
- Анотація `@Bean` каже Spring керувати цим компонентом, щоб інші частини могли його використовувати

### 2. Сервіс калькулятора

**Файл:** `CalculatorService.java`

Тут відбуваються всі математичні операції. Кожен метод позначено `@Tool`, щоб зробити його доступним через MCP:

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
    
    // Більше операцій калькулятора...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Ключові особливості:**

1. **Анотація `@Tool`**: Каже MCP, що цей метод можуть викликати зовнішні клієнти
2. **Зрозумілі описи**: Кожен інструмент має опис, який допомагає AI-моделям зрозуміти, коли його використовувати
3. **Послідовний формат повернення**: Всі операції повертають зрозумілі рядки, як "5.00 + 3.00 = 8.00"
4. **Обробка помилок**: Ділення на нуль та від’ємні квадратні корені повертають повідомлення про помилку

**Доступні операції:**
- `add(a, b)` - Додає два числа
- `subtract(a, b)` - Віднімає друге від першого
- `multiply(a, b)` - Множить два числа
- `divide(a, b)` - Ділить перше на друге (з перевіркою на нуль)
- `power(base, exponent)` - Підносить основу до степеня
- `squareRoot(number)` - Обчислює квадратний корінь (з перевіркою на від’ємне число)
- `modulus(a, b)` - Повертає остачу від ділення
- `absolute(number)` - Повертає абсолютне значення
- `help()` - Повертає інформацію про всі операції

### 3. Прямий MCP клієнт

Дивіться [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Цей клієнт використовує `HttpClientStreamableHttpTransport` за `/mcp`, ініціалізує з’єднання,
перевіряє сервер та пагінацію списку інструментів. Він перевіряє наявність усіх дев’яти очікуваних інструментів
і викликає кожен з них, включно з `modulus` і `help`, без AI-моделі.

Поточний конструктор запитів виглядає так:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Помилки протоколу призводять до збою клієнта замість введення в оману повідомлення про успіх. MCP клієнт
закривається через try-with-resources, навіть якщо не вдалося виявлення або виклик інструменту.

### 4. Клієнт з AI

Дивіться [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
та [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` реалізує теперішній LangChain4j `ChatModel` API.
`StreamableHttpMcpTransport` підключає його до того самого кінцевого пункту `/mcp`, що і SDK клієнт.
`AiServices` знаходить інструменти і керує послідовністю викликів інструментів та результатів.

За замовчуванням використовується **GPT-5.6 Luna**, з явно відключеним розмірковуванням:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Ці налаштування застосовуються до кожного завдання, включно з подальшими запитами після використання інструментів.
Клієнт використовує оновлюваний `BearerTokenCredential`, забезпечений `DefaultAzureCredential`
та областю `https://ai.azure.com/.default`, а не одноразовий токен API.
URL ресурсів та URL, що вже закінчуються на `/openai/v1`, приймаються.

Бот зберігає обмежену історію розмов, друкує `Tool executed: ...` із фактичним
результатом MCP і припиняє роботу, якщо відповідь пропускає інструменти. Цикли інструментів обмежені до чотирьох раундів.
Помилки автентифікації, моделі, MCP та інструментів поширюються; автоматичні повтори моделі відключені.
І MCP транспорт/клієнт, і офіційний OpenAI клієнт закриваються при успіху чи помилці.

## Запуск прикладів

### Крок 1: Запустіть сервер калькулятора

Для сервера Azure конфігурація не потрібна. Команди нижче виконуються зі каталогу цього прикладу.
Приклад використовує порт **18081**, щоб уникнути конфлікту з іншим прикладом; за замовчуванням залишається 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Кінцева точка MCP — `http://localhost:18081/mcp`. Інформація про стан і виявлення на
`http://localhost:18081/health` та `http://localhost:18081/info`.
Потік HTTP замінює старий транспорт лише з SSE; `/sse` і `/v1/tools` не є кінцевими точками.

### Крок 2: Тест з прямим клієнтом

В іншому терміналі PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Ввід не потрібен. Виконуються всі дев'ять інструментів. Очікувані арифметичні результати включають
8, 6, 42, 5, 256, 4, 2 і 5.5, за якими йде текст допомоги.

### Крок 3: Тест з AI клієнтом

Після автентифікації за описом у вимогах, налаштуйте AI клієнт у тому ж терміналі:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Очікуйте рядок `Tool executed: add` з `41.80`, за яким слідує відповідь моделі.
Режим одного повідомлення виходить без очікування вводу. Щоб запустити оригінальний демо з чотирма запитами:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Демонстрація виконує `add`, `squareRoot`, `help` і послідовну операцію `power`, потім `divide`.
Очікувані числові відповіді — 41.8, 12 і 64. Пропуск аргументів також запускає цю демонстрацію.

### Крок 4: Запустіть інтерактивного бота

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Введіть `Multiply 6 by 7 using the calculator service`, потім `exit` або `quit`.
Очікуйте реальний результат інструменту `multiply` — 42. Порожні рядки ігноруються; EOF також завершує сесію.
Для неінтерактивного базового тесту цієї вхідної точки:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Обидві вхідні точки AI приймають `--prompt "question"`, `--demo` і `--interactive`.
Неправильні опції призводять до збою до відкриття з'єднання. Кожен аргумент Maven `-D...` повністю укладається в лапки
для PowerShell. В Bash використовуйте `export NAME=value` замість `$env:NAME = "value"`.

**Квота:** Запускайте AI приклади послідовно. Простий запит зазвичай потребує двох звернень до моделі;
повний демонтаж зазвичай потребує дев’яти, включно з подальшими викликами інструментів. Із спільним розгортанням 10 RPM
дайте свіжий квотний вікно перед наступним запуском AI. 429 помилка відображається без
автоматичних повторів; дотримуйтесь рекомендацій сервісу retry-after. Кількість запитів залежить від моделі.
Офлайн тести не споживають квоту і не визначають живу доступність Luna або якість відповіді.

### Налаштування і зупинка

| Налаштування | Значення за замовчуванням / поведінка |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; базовий URL, без `/mcp` |
| `-Dmcp.server.url=...` | Перевизначає `MCP_SERVER_URL` для всіх клієнтів |
| `AZURE_OPENAI_ENDPOINT` | Потрібен лише для AI клієнтів; URL ресурсу або `/openai/v1` URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; назва розгортання Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; додатне ціле число |
| Зусилля розмірковування | Завжди `none`, включно з подальшими викликами цикл інструментів |

Замінене розгортання має підтримувати `reasoning_effort=none` та `max_completion_tokens`.
Клієнти не зчитують файл `.env` автоматично. Зупиніть сервер за допомогою `Ctrl+C` після тестування.
Клієнти повертаються нормально без `System.exit` або сну перед завершенням.

## Офлайн тести

```powershell
mvn -B -ntp clean verify
```

Всі тести офлайн щодо Azure: протокольний пакет запускає Spring сервер та
OpenAI-сумісний заглушку на випадкових loopback портах, а потім закриває їх. Maven може все ще
завантажувати залежності. Не використовуються облікові дані, живе розгортання або попередній сервер MCP.

- Юніт-тести калькулятора охоплюють всі арифметичні операції, десяткові результати, допомогу та помилки домену.
- MCP тести охоплюють ініціалізацію, виявлення, усі дев’ять викликів інструментів, збої інструментів, стан/інфо.
- AI протокольні тести виконують повний демонтаж і інтерактивного бота проти справжнього калькулятора,
  перевіряють, що результати інструментів передаються до наступного завершення, і перевіряють кожне HTTP тіло для Luna,
  `reasoning_effort: "none"` і `max_completion_tokens` без застарілого `max_tokens`.
- Тести конфігурації/введення охоплюють перевизначення розгортання і кінцевих точок, порожні рядки, EOF, exit/quit,
  режим одного запиту, неправильні опції і поширення помилок. Тести квоти доказують, що 429 не повторюється.

## Як це працює разом

Ось повний процес, коли ви питаєте AI: "Скільки буде 5 + 3?":

1. **Ви** задаєте AI природною мовою
2. **AI** аналізує ваше запитання і розуміє, що потрібно додати
3. **AI** викликає MCP сервер: `add(5.0, 3.0)`
4. **Сервіс калькулятора** виконує: `5.0 + 3.0 = 8.0`
5. **Сервіс калькулятора** повертає: `"5.00 + 3.00 = 8.00"`
6. **AI** отримує результат і формує природну відповідь
7. **Ви** отримуєте: "Сума 5 і 3 — це 8"

## Наступні кроки

Для більше прикладів дивіться [Розділ 04: Практичні приклади](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу штучного інтелекту для перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->