# Налаштування середовища розробки для Azure AI Foundry

> Цей посібник налаштовує моделі **Azure AI Foundry** для Java AI додатків у цьому курсі, використовуючи **авторизацію без ключів** (Microsoft Entra ID) — без необхідності керування API-ключами. Новачок у цих інструментах? Почніть із [посібника зі середовища розробки](./README.md).

Цей посібник налаштовує моделі **Azure AI Foundry** для Java AI додатків у цьому курсі. У вас є два варіанти:

- **Варіант A — налаштування за допомогою `azd` + Bicep (рекомендовано):** одна команда розгортає обліковий запис Foundry та моделі як код. Ніяких кліків у порталі.
- **Варіант B — створення ресурсів вручну** у порталі Azure AI Foundry.

Обидва варіанти використовують **авторизацію без ключів** (Microsoft Entra ID) — немає API-ключів, які потрібно копіювати або ризикувати їх витоком.

## Зміст

- [Що створюється](#що-створюється)
- [Передумови](#передумови)
- [Варіант A: налаштування за допомогою azd + Bicep (рекомендовано)](#option-a-provision-with-azd--bicep-recommended)
- [Варіант B: створення ресурсів вручну](#варіант-b-створення-ресурсів-вручну)
- [Налаштуйте своє середовище](#налаштуйте-своє-середовище)
- [Перевірте своє налаштування](#перевірте-своє-налаштування)
- [Що далі?](#що-далі)
- [Ресурси](#ресурси)
- [Додаткові ресурси](#додаткові-ресурси)

## Що створюється

Шаблони Bicep в каталозі [`infra/`](../../../02-SetupDevEnvironment/infra) розгортають:

- Обліковий запис **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, тип `AIServices`) з проектом
- Розгортання **чат-моделі** - GPT-5.6 Luna (`gpt-5.6-luna`), версія `2026-07-09`, з потужністю `GlobalStandard` 10 (10 запитів на хвилину та 10,000 токенів на хвилину для цієї моделі)
- Розгортання **embedding-моделі** - `text-embedding-3-small`, версія `1` (використовується у пізніших розділах)
- Призначення ролі без ключа (`Cognitive Services OpenAI User`), тож ви увійдете через `az login` замість керування ключами

## Передумови

- [Підписка Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) і [Maven 3.9+](https://maven.apache.org/download.cgi)

## Варіант A: Налаштування за допомогою azd + Bicep (рекомендовано)

З папки `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Увійти (обидва інструменти)
azd auth login
az login

# Забезпечення облікового запису Foundry + розгортання моделей
azd up
```

`azd` запитує **назву середовища** (наприклад `genai-java`), **підписку** та **регіон**. Оберіть свою підписку та регіон, де доступні `gpt-5.6-luna` і `text-embedding-3-small`, наприклад `eastus2`. Переконайтеся, що підписка має достатню квоту для моделі та типу розгортання в цьому регіоні; доступність і квота залежать від підписки.

Після завершення налаштування azd:

1. Розгортає все, визначене у [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Виконує postprovision hook, який записує [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) з вашими іменами кінцевої точки та розгортання (без секретів).

> **Порада:** повторно запускайте `azd up` у будь-який час для застосування змін. Запуск `azd down` видалить усе і припинить нарахування вартості.

Щоб переглянути згенеровані налаштування:

```bash
azd env get-values
```

Тепер перейдіть до розділу [Перевірте своє налаштування](#перевірте-своє-налаштування).

## Варіант B: Створення ресурсів вручну

Віддаєте перевагу порталу? Створіть ресурси вручну:

1. Перейдіть до [Azure AI Foundry порталу](https://ai.azure.com/) та увійдіть.
2. **Створіть проект** (це також створює ресурс AI Foundry). Дайте йому ім'я, наприклад `GenAIJava`.
3. У своєму проекті відкрийте **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Розгорніть **GPT-5.6 Luna** (ім'я моделі та розгортання `gpt-5.6-luna`, версія `2026-07-09`) з потужністю **Global Standard** `10`. Повторіть для **text-embedding-3-small**, версія `1`, якщо вам потрібні прикладиembedding.
5. З розділу **Overview** скопіюйте **endpoint** (наприклад, `https://<resource>.openai.azure.com/`).
6. Надаліть собі доступ без ключа: на ресурсі відкрийте **Access control (IAM)** → **Add role assignment** → призначте **Cognitive Services OpenAI User** для вашого акаунту.

> **Ще виникають проблеми?** Дивіться [документацію Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Налаштуйте своє середовище

**Якщо ви використовували Варіант A (`azd up`)**, файл налаштувань вже створено — нічого конфігурувати не потрібно. Перейдіть до [Перевірте своє налаштування](#перевірте-своє-налаштування).

**Якщо ви використовували Варіант B (ручний спосіб)**, створіть файл `.env` прикладу самостійно:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Відредагуйте `.env`, додавши ваш endpoint (без ключа — авторизація без ключа):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Використовуйте Azure OpenAI endpoint ресурсу, а не URL проекту. Програма basic-chat розпізнає його як `/openai/v1` та налаштовує клієнт із експліцитним токеном Bearer; API-ключ не потрібен.

> **Примітка з безпеки:** Немає API-ключа для зберігання. Ви авторизуєтесь через Microsoft Entra ID за допомогою `az login` (локально) або керованої ідентичності (в Azure). Файл `.env` містить лише незахищені налаштування і вже ігнорується `.gitignore`.

## Перевірте своє налаштування

Переконайтесь, що ви увійшли, щоб авторизація без ключів могла отримати токен, і запустіть приклад:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # якщо ви ще не увійшли в систему
mvn clean spring-boot:run
```

Ви повинні побачити відповідь від моделі `gpt-5.6-luna`. Запускайте приклади послідовно, щоб не перевищувати невелику стандартну квоту; якщо отримаєте HTTP 429, зачекайте заданий інтервал між спробами.

> **Користувачі VS Code:** Натисніть `F5` для запуску. Програма автоматично завантажує ваш `.env`.

> **Повний приклад:** Дивіться [Basic Chat з Azure AI Foundry приклад](./examples/basic-chat-azure/README.md) для деталей та налагодження.

## Що далі?

Після налаштування і успішного запуску прикладу у вас буде:
- Azure AI Foundry з розгорнутими `gpt-5.6-luna` і `text-embedding-3-small`
- Авторизація без ключів (Microsoft Entra ID) — ніяких ключів для керування
- Локальний `.env` з вашим endpoint і іменами розгортань
- Готове середовище розробки Java

**Продовжуйте у** [Розділ 3: Основні методи генеративного AI](../03-CoreGenerativeAITechniques/README.md), щоб почати створювати AI-додатки!

## Ресурси

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Авторизація без ключа з Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Документація Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Перехід Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Офіційний OpenAI Java SDK з Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Додаткові ресурси

- [Завантажити VS Code](https://code.visualstudio.com/Download)
- [Отримати Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Конфігурація контейнера розробника](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу штучного інтелекту для перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->