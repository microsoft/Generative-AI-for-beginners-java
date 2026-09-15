# Настройка среды разработки для Azure AI Foundry

> Это руководство описывает настройку моделей **Azure AI Foundry** для Java AI приложений в этом курсе с использованием **аутентификации без ключей** (Microsoft Entra ID) — без необходимости управлять API-ключами. Новичок в инструментах? Начните с [руководства по настройке среды разработки](./README.md).

Это руководство описывает настройку моделей **Azure AI Foundry** для Java AI приложений в этом курсе. Есть два варианта:

- **Вариант A — Развёртывание с помощью `azd` + Bicep (рекомендуется):** одна команда разворачивает аккаунт Foundry и модели как код. Никаких кликов в портале.
- **Вариант B — Создание ресурсов вручную** в портале Azure AI Foundry.

Оба варианта используют **аутентификацию без ключей** (Microsoft Entra ID) — API-ключи не нужно копировать или сохранять.

## Содержание

- [Что создаётся](#что-создаётся)
- [Требования](#требования)
- [Вариант A: Развёртывание с azd + Bicep (рекомендуется)](#option-a-provision-with-azd--bicep-recommended)
- [Вариант B: Создание ресурсов вручную](#вариант-b-создание-ресурсов-вручную)
- [Настройка среды](#настройка-среды)
- [Проверка настройки](#проверка-настройки)
- [Что дальше?](#что-дальше)
- [Ресурсы](#ресурсы)
- [Дополнительные ресурсы](#дополнительные-ресурсы)

## Что создаётся

Bicep шаблоны в [`infra/`](../../../02-SetupDevEnvironment/infra) создают:

- Аккаунт **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, тип `AIServices`) с проектом
- Развёртывание **чата** - GPT-5.6 Luna (`gpt-5.6-luna`), версия `2026-07-09`, с мощностью `GlobalStandard` `10` (10 запросов/мин и 10,000 токенов/мин для этой модели)
- Развёртывание **встраивания** - `text-embedding-3-small`, версия `1` (используется в последующих главах)
- Назначение роли **без ключей** (`Пользователь Cognitive Services OpenAI`), чтобы вход был через `az login` без управления ключами

## Требования

- [Подписка Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) и [Maven 3.9+](https://maven.apache.org/download.cgi)

## Вариант A: Развёртывание с azd + Bicep (рекомендуется)

Из папки `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Войти (обе программы)
azd auth login
az login

# Настроить учетную запись Foundry + развертывание моделей
azd up
```

`azd` запросит **название окружения** (например, `genai-java`), **подписку** и **регион**. Выберите свою подписку и регион, где доступны `gpt-5.6-luna` и `text-embedding-3-small`, например `eastus2`. Убедитесь, что в подписке достаточно квот для модели и типа развертывания в выбранном регионе; доступность и квоты зависят от подписки.

Когда развёртывание завершится, azd:

1. Разворачивает всё, что описано в [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Выполняет postprovision хук, который записывает [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) с вашим endpoint и именами развертываний (без секретов).

> **Подсказка:** Повторно запускайте `azd up` для применения изменений. Выполните `azd down`, чтобы удалить всё и прекратить начисление расходов.

Чтобы просмотреть сгенерированные настройки:

```bash
azd env get-values
```

Теперь перейдите к разделу [Проверка настройки](#проверка-настройки).

## Вариант B: Создание ресурсов вручную

Предпочитаете портал? Создайте ресурсы вручную:

1. Перейдите на [портал Azure AI Foundry](https://ai.azure.com/) и войдите.
2. **Создайте проект** (это также создаст ресурс AI Foundry). Назовите его, например, `GenAIJava`.
3. В проекте откройте **Модели + конечные точки** → **Развернуть модель** → **Развернуть базовую модель**.
4. Разверните **GPT-5.6 Luna** (имя модели и развертывания `gpt-5.6-luna`, версия `2026-07-09`) с мощностью **Global Standard** `10`. Повторите для **text-embedding-3-small**, версия `1`, если хотите использовать примеры встраивания.
5. На панели **Обзор** скопируйте **endpoint** (например, `https://<resource>.openai.azure.com/`).
6. Предоставьте себе доступ без ключей: в ресурсе откройте **Управление доступом (IAM)** → **Добавить назначение роли** → назначьте **Пользователь Cognitive Services OpenAI** своему аккаунту.

> **Все еще возникают проблемы?** Ознакомьтесь с [документацией Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Настройка среды

**Если вы использовали Вариант A (`azd up`),** файл настроек уже создан — ничего дополнительно настраивать не нужно. Перейдите к разделу [Проверка настройки](#проверка-настройки).

**Если вы использовали Вариант B (вручную), создайте файл `.env` для примера сами:**

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Отредактируйте `.env`, указав ваш endpoint (ключ не нужен — аутентификация без ключей):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Используйте Azure OpenAI endpoint ресурса, а не URL проекта. Приложение basic-chat преобразует его в `/openai/v1` и настраивает клиент с явным токеном bearer; API-ключ не требуется.

> **Примечание по безопасности:** API-ключ хранить не нужно. Аутентификация происходит через Microsoft Entra ID с помощью `az login` (локально) или управляемой идентичности (в Azure). Файл `.env` содержит только не секретные настройки и уже добавлен в `.gitignore`.

## Проверка настройки

Убедитесь, что вы вошли в систему, чтобы аутентификация без ключей могла получить токен, затем запустите пример:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # если вы еще не вошли в систему
mvn clean spring-boot:run
```

Вы должны увидеть ответ от модели `gpt-5.6-luna`. Запускайте примеры последовательно, чтобы не превысить небольшой дефолтный лимит; если получите HTTP 429, подождите интервал повторной попытки перед новой попыткой.

> **Пользователи VS Code:** Нажмите `F5`, чтобы запустить. Приложение автоматически загрузит ваш `.env`.

> **Полный пример:** См. [Пример Basic Chat с Azure AI Foundry](./examples/basic-chat-azure/README.md) для подробностей и устранения неполадок.

## Что дальше?

После провиженинга и успешного запуска примера у вас будет:
- Azure AI Foundry с развернутыми `gpt-5.6-luna` и `text-embedding-3-small`
- Аутентификация без ключей (Microsoft Entra ID) — ключи не нужно управлять
- Локальный файл `.env` с вашим endpoint и именами развертываний
- Готовая среда разработки для Java

**Продолжайте к** [Главе 3: Основные техники генеративного ИИ](../03-CoreGenerativeAITechniques/README.md), чтобы начать создавать AI-приложения!

## Ресурсы

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Аутентификация без ключей с Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Документация Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Переход Spring AI 2 на OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Официальный OpenAI Java SDK с Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Дополнительные ресурсы

- [Скачать VS Code](https://code.visualstudio.com/Download)
- [Получить Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Конфигурация Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от ответственности**:
Этот документ был переведен с использованием сервиса машинного перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Несмотря на наши усилия по обеспечению точности, имейте в виду, что автоматический перевод может содержать ошибки или неточности. Оригинальный документ на его исходном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется обратиться к профессиональному человеческому переводу. Мы не несем ответственности за любые недоразумения или неправильные толкования, возникшие в результате использования этого перевода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->