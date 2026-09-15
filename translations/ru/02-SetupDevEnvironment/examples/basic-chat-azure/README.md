# Основной чат с Azure AI Foundry — пример сквозного выполнения

Этот пример — простое приложение Spring Boot, которое подключается к модели **Azure AI Foundry** с использованием **аутентификации без ключа** (Microsoft Entra ID) и проверяет вашу настройку. Оно использует `ChatClient` из Spring AI, основанный на **официальном OpenAI Java SDK** и конечной точке **Azure OpenAI v1**.

Версии в [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml): Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** и dotenv-java **3.2.0**. Пример использует `spring-ai-starter-model-openai` и явно объявляет `openai-java` и `azure-identity`; в Spring AI 2 удалён старый стартер Azure OpenAI.

## Содержание

- [Требования](#требования)
- [Быстрый старт](#быстрый-старт)
- [Как работает аутентификация](#как-работает-аутентификация)
- [Запуск приложения](#запуск-приложения)
  - [Использование Maven](#использование-maven)
  - [Использование VS Code](#использование-vs-code)
  - [Ожидаемый результат](#ожидаемый-результат)
- [Справочная информация по конфигурации](#справочная-информация-по-конфигурации)
  - [Переменные окружения](#переменные-окружения)
  - [Конфигурация Spring](#конфигурация-spring)
- [Устранение неполадок](#устранение-неполадок)
  - [Распространённые проблемы](#распространённые-проблемы)
  - [Режим отладки](#режим-отладки)
- [Дальнейшие шаги](#дальнейшие-шаги)
- [Ресурсы](#ресурсы)

## Требования

Перед запуском этого примера убедитесь, что у вас есть:

- Ресурс Azure AI Foundry с развертыванием `gpt-5.6-luna` — создайте его с помощью `azd up` или вручную по [руководству Azure AI Foundry](../../getting-started-azure-openai.md)
- Роль **Cognitive Services OpenAI User** для этого ресурса (шаблоны Bicep назначают её автоматически)
- Установленная и авторизованная [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli) с выполненным входом через `az login`
- Java 21+ и Maven 3.9+

> **API ключ не требуется** — аутентификация без ключа через Microsoft Entra ID.

## Быстрый старт

```bash
# 1. Перейдите в проект
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Войдите в систему, чтобы keyless auth мог получить токен
az login

# 3. Настройте конечную точку
#    - Если вы запускали `azd up`, .env был создан для вас (пропустите этот шаг).
#    - В противном случае скопируйте шаблон и установите AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Запустите приложение
mvn spring-boot:run
```

## Как работает аутентификация

В этом примере используется аутентификация через **Microsoft Entra ID** — API ключ не применяется.

Приложение настраивает аутентификацию явно в [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. Метод `azureCredential()` создаёт `BearerTokenCredential`, используя `AuthenticationUtil.getBearerTokenSupplier` с `DefaultAzureCredential` и областью `https://ai.azure.com/.default`.
2. Метод `azureOpenAiClient()` создаёт `OpenAIClient` через `OpenAIOkHttpClient.builder()`, формирует конечную точку ресурса `/openai/v1` и передаёт токен с `.credential(...)`.
3. Метод `azureChatModel()` передаёт этого клиента в `OpenAiChatModel` Spring AI, который поддерживает `ChatClient` из урока.

Такие явные бины предотвращают переопределение аутентификации Azure глобальным `OPENAI_API_KEY`. Исключение ключа API из YAML само по себе не задаёт аутентификацию. `DefaultAzureCredential` использует либо сессию `az login` локально, либо управляемую идентичность в Azure; выбранная учётная запись должна иметь указанную выше роль на ресурсе.

## Запуск приложения

### Использование Maven

```bash
mvn spring-boot:run
```

### Использование VS Code

1. Откройте проект в VS Code
2. Нажмите `F5` или используйте панель «Run and Debug»
3. Выберите конфигурацию "Spring Boot-BasicChatApplication"

> **Примечание**: приложение загружает `.env` из рабочей директории, в том числе при запуске из VS Code.

### Ожидаемый результат

Примерный вывод после успешного запуска (журналы запуска опущены; формулировка ответа может отличаться):

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

## Справочная информация по конфигурации

### Переменные окружения

| Переменная | Описание | Обязательно | Пример |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL конечной точки Foundry (Azure OpenAI) | Да | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Имя развертывания чат-модели | Нет | `gpt-5.6-luna` (по умолчанию) |

> Переменной для API ключа **нет** — аутентификация ключом не используется (Microsoft Entra ID через `az login`).

### Конфигурация Spring

Настройки в [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) используют префикс `spring.ai.openai` и разглаженные свойства чата (без блока `options`):

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

`model` — это **имя развертывания Azure**. Аутентификация обеспечивается явными бинами выше, а не через параметр `api-key`. В уроке отключено рассуждение и ограничено количество токенов для ответа до 500; параметры `temperature` и устаревший `max-tokens` не заданы.

Microsoft рекомендует [официальный OpenAI SDK с Azure OpenAI v1 и Responses API для новых приложений](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions остаётся поддерживаемым для этого существующего урока с сообщениями. Для GPT-5.6 запросы с инструментами на Chat Completions должны устанавливать `reasoning_effort` в `none`; при сочетании рассуждений с инструментами используйте Responses. Подробнее см. [вызов инструментов с моделями рассуждений](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Устранение неполадок

### Распространённые проблемы

<details>
<summary><strong>Ошибка: 401 / "PermissionDenied" / ошибки токена</strong></summary>

- Выполните `az login` — аутентификация без ключа требует активной авторизации для получения токена
- Проверьте, что у вашей учётной записи есть роль **Cognitive Services OpenAI User** для ресурса
- Если роль назначена недавно, подождите минуту для распространения изменений
- Убедитесь, что вы находитесь в нужном тенанте/подписке (`az account show`)
</details>

<details>
<summary><strong>Ошибка: "The endpoint is not valid" / ошибки подключения</strong></summary>

- Убедитесь, что `AZURE_OPENAI_ENDPOINT` — полный базовый URL (например, `https://your-resource.openai.azure.com/`)
- Проверьте согласованность завершающего слеша
- Уточните, что конечная точка совпадает с вашим созданным ресурсом (`azd env get-values`)
</details>

<details>
<summary><strong>Ошибка: "The deployment was not found"</strong></summary>

- Убедитесь, что `AZURE_OPENAI_DEPLOYMENT` совпадает с именем развертывания в Azure
- Проверьте, что модель успешно развернута и активна
- Значение по умолчанию для имени развертывания — `gpt-5.6-luna`
</details>

<details>
<summary><strong>Ошибка: 429 / превышен лимит запросов</strong></summary>

- Развертывание GPT-5.6 Luna по умолчанию имеет Global Standard capacity 10: 10 запросов в минуту и 10 000 токенов в минуту
- Выполняйте примеры последовательно и ожидайте интервал повторной попытки сервиса перед повторным запросом
- В этом базовом примере отключены автоматические повторные попытки SDK, поэтому ошибка запроса выводится сразу
</details>

<details>
<summary><strong>VS Code: переменные окружения не загружаются</strong></summary>

- Убедитесь, что файл `.env` находится в корневой директории проекта (на том же уровне, что и `pom.xml`)
- Попробуйте запустить `mvn spring-boot:run` в интегрированном терминале VS Code
- Проверьте корректность установки расширения Java для VS Code
</details>

### Режим отладки

Для включения подробного логирования раскомментируйте эти строки в [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Дальнейшие шаги

**Настройка завершена!** Продолжайте обучение:

[Глава 3: Основные техники генеративного ИИ](../../../03-CoreGenerativeAITechniques/README.md)

## Ресурсы

- [Переход Spring AI 2 на OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Официальный OpenAI Java SDK с Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Аутентификация без ключа с Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Портал Azure AI Foundry](https://ai.azure.com/)
- [Документация Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:
Этот документ был переведен с использованием сервиса машинного перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия по обеспечению точности, имейте в виду, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на его исходном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется обратиться к профессиональному человеческому переводу. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования этого перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->