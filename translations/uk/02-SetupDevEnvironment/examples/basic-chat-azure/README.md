# Базовий чат з Azure AI Foundry — приклад від початку до кінця

Цей приклад — це проста програма Spring Boot, яка підключається до моделі **Azure AI Foundry** за допомогою **аутентифікації без ключа** (Microsoft Entra ID) та перевіряє вашу конфігурацію. Вона використовує `ChatClient` із Spring AI, який працює на базі **офіційного OpenAI Java SDK** та кінцевої точки **Azure OpenAI v1**.

Версії в [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) такі: Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** та dotenv-java **3.2.0**. У прикладі використовується `spring-ai-starter-model-openai` і явно оголошуються `openai-java` та `azure-identity`; у Spring AI 2 був видалений старий Azure OpenAI стартер.

## Зміст

- [Вимоги](#вимоги)
- [Швидкий старт](#швидкий-старт)
- [Як працює аутентифікація](#як-працює-аутентифікація)
- [Запуск програми](#запуск-програми)
  - [За допомогою Maven](#за-допомогою-maven)
  - [У VS Code](#у-vs-code)
  - [Очікуваний результат](#очікуваний-результат)
- [Довідка з конфігурації](#довідка-з-конфігурації)
  - [Змінні середовища](#змінні-середовища)
  - [Конфігурація Spring](#конфігурація-spring)
- [Усунення несправностей](#усунення-несправностей)
  - [Поширені проблеми](#поширені-проблеми)
  - [Режим налагодження](#режим-налагодження)
- [Наступні кроки](#наступні-кроки)
- [Ресурси](#ресурси)

## Вимоги

Перед запуском цього прикладу переконайтесь, що у вас є:

- Ресурс Azure AI Foundry із розгортанням `gpt-5.6-luna` — створіть його за допомогою `azd up` або вручну, дотримуючись [інструкції з налаштування Azure AI Foundry](../../getting-started-azure-openai.md)
- Роль **Cognitive Services OpenAI User** на цьому ресурсі (в шаблонах Bicep це призначається автоматично)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), увійдена через `az login`
- Java 21+ та Maven 3.9+

> **API-ключ не потрібен** — аутентифікація без ключа за допомогою Microsoft Entra ID.

## Швидкий старт

```bash
# 1. Перейдіть до проекту
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Увійдіть, щоб ключова автентифікація могла отримати токен
az login

# 3. Налаштуйте кінцеву точку
#    - Якщо ви виконали `azd up`, файл .env уже створено (пропустіть цей крок).
#    - В іншому випадку скопіюйте шаблон і встановіть AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Запустіть додаток
mvn spring-boot:run
```

## Як працює аутентифікація

Цей приклад аутентифікується через **Microsoft Entra ID** — API-ключ відсутній.

Програма явно налаштовує аутентифікацію в [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` створює `BearerTokenCredential` за допомогою `AuthenticationUtil.getBearerTokenSupplier` із `DefaultAzureCredential` і областю `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` створює `OpenAIClient` за допомогою `OpenAIOkHttpClient.builder()`, вирішує кінцеву точку ресурсу `/openai/v1` та надає токен за допомогою `.credential(...)`.
3. `azureChatModel()` передає цього клієнта до `OpenAiChatModel` з Spring AI, який стоїть за `ChatClient` уроку.

Ці явні біні запобігають тому, щоб глобальна змінна `OPENAI_API_KEY` перевизначила аутентифікацію Azure. Лише відсутність API-ключа в YAML не є належною конфігурацією аутентифікації. `DefaultAzureCredential` може використовувати локальну сесію `az login` або керовану ідентичність в Azure; вибрана ідентичність має мати роль ресурсу, вказану вище.

## Запуск програми

### За допомогою Maven

```bash
mvn spring-boot:run
```

### У VS Code

1. Відкрийте проект у VS Code
2. Натисніть `F5` або використайте панель "Run and Debug"
3. Оберіть конфігурацію "Spring Boot-BasicChatApplication"

> **Примітка**: Програма завантажує `.env` з робочої директорії, у тому числі при запуску з VS Code.

### Очікуваний результат

Ілюстративний вивід після успішного запуску (логи старту опущені; формулювання відповіді може відрізнятись):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Довідка з конфігурації

### Змінні середовища

| Змінна | Опис | Обов’язково | Приклад |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL кінцевої точки Foundry (Azure OpenAI) | Так | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Назва розгортання чат-моделі | Ні | `gpt-5.6-luna` (за замовчуванням) |

> Змінної для API-ключа **немає** — аутентифікація без ключа (Microsoft Entra ID через `az login`).

### Конфігурація Spring

Налаштування в [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) використовують префікс `spring.ai.openai` та спрощені властивості чату (без блоку `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` — це **назва розгортання Azure**. Аутентифікація береться з явних бінів, описаних вище, а не з параметра `api-key`. У цьому уроці відключено reasoning і обмежено число токенів у завершенні до 500; `temperature` і спадкова `max-tokens` залишені без налаштувань.

Microsoft рекомендує [офіційний OpenAI SDK з Azure OpenAI v1 та API Responses для нових додатків](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions залишається підтримуваним для цього уроку, що базується на повідомленнях. Для GPT-5.6 запити, що включають інструменти у Chat Completions, мають встановлювати `reasoning_effort` у `none`; використовуйте Responses для поєднання reasoning з інструментами. Докладніше — [виклик інструментів з моделями reasoning](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Усунення несправностей

### Поширені проблеми

<details>
<summary><strong>Помилка: 401 / "PermissionDenied" / проблеми з токеном</strong></summary>

- Виконайте `az login` — аутентифікація без ключа вимагає активного входу для отримання токена
- Перевірте, що ваша облікова запис має роль **Cognitive Services OpenAI User** для ресурсу
- Якщо роль щойно призначена, зачекайте хвилину для її поширення
- Переконайтесь, що ви у вірному орендарі/підписці (`az account show`)
</details>

<details>
<summary><strong>Помилка: "The endpoint is not valid" / проблеми з підключенням</strong></summary>

- Переконайтесь, що `AZURE_OPENAI_ENDPOINT` містить повний базовий URL (наприклад, `https://your-resource.openai.azure.com/`)
- Перевірте узгодженість завершаючого слеша
- Переконайтесь, що кінцева точка відповідає вашому створеному ресурсу (`azd env get-values`)
</details>

<details>
<summary><strong>Помилка: "The deployment was not found"</strong></summary>

- Переконайтесь, що `AZURE_OPENAI_DEPLOYMENT` співпадає з назвою розгортання в Azure
- Перевірте, що модель успішно розгорнута і активна
- Назва розгортання за замовчуванням — `gpt-5.6-luna`
</details>

<details>
<summary><strong>Помилка: 429 / перевищено ліміт швидкості</strong></summary>

- За замовчуванням розгортання GPT-5.6 Luna має Global Standard capacity 10: 10 запитів на хвилину та 10 000 токенів на хвилину
- Запускайте приклади послідовно та чекайте інтервал повторної спроби сервісу перед новою спробою
- Цей базовий приклад вимикає автоматичні повторні запити SDK, тож про помилку повідомляється безпосередньо
</details>

<details>
<summary><strong>VS Code: змінні середовища не завантажуються</strong></summary>

- Переконайтеся, що файл `.env` знаходиться в корінній папці проекту (на одному рівні з `pom.xml`)
- Спробуйте запускати `mvn spring-boot:run` у вбудованому терминалі VS Code
- Перевірте, чи встановлене розширення Java для VS Code
</details>

### Режим налагодження

Щоб увімкнути докладне логування, раскоментуйте ці рядки в [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Наступні кроки

**Налаштування завершено!** Продовжуйте навчання:

[Розділ 3: Основні техніки генеративного ШІ](../../../03-CoreGenerativeAITechniques/README.md)

## Ресурси

- [Перехід Spring AI 2 на OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Офіційний OpenAI Java SDK з Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Аутентифікація без ключа з Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Портал Azure AI Foundry](https://ai.azure.com/)
- [Документація Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу штучного інтелекту для перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->