# Настройване на средата за разработка за Azure AI Foundry

> Това ръководство настройва **Azure AI Foundry** модели за Java AI приложенията в този курс, използвайки **автентикация без ключове** (Microsoft Entra ID) — няма нужда от управление на API ключове. Нови сте в инструментите? Започнете с [ръководството за среда за разработка](./README.md).

Това ръководство настройва **Azure AI Foundry** модели за Java AI приложенията в този курс. Имате два варианта:

- **Вариант А — Пускане с `azd` + Bicep (препоръчително):** една команда разгръща Foundry акаунта и моделите като код. Без кликване в портала.
- **Вариант Б — Създаване на ресурси ръчно** в Azure AI Foundry портала.

И в двата варианта се използва **автентикация без ключове** (Microsoft Entra ID) — няма API ключове за копиране или изтичане.

## Съдържание

- [Какво се създава](#какво-се-създава)
- [Изисквания](#изисквания)
- [Вариант А: Пускане с azd + Bicep (Препоръчително)](#option-a-provision-with-azd--bicep-recommended)
- [Вариант Б: Създаване на ресурси ръчно](#вариант-б-създаване-на-ресурси-ръчно)
- [Конфигуриране на вашата среда](#конфигуриране-на-вашата-среда)
- [Тестване на настройката](#тестване-на-настройката)
- [Какво следва?](#какво-следва)
- [Ресурси](#ресурси)
- [Допълнителни ресурси](#допълнителни-ресурси)

## Какво се създава

Bicep шаблоните в [`infra/`](../../../02-SetupDevEnvironment/infra) създават:

- **Azure AI Foundry** акаунт (`Microsoft.CognitiveServices/accounts`, вид `AIServices`) с проект
- Разгръщане на **чат** - GPT-5.6 Luna (`gpt-5.6-luna`), версия `2026-07-09`, с капацитет `GlobalStandard` 10 (10 заявки/минута и 10,000 токена/минута за този модел)
- Разгръщане на **embedding** - `text-embedding-3-small`, версия `1` (използван в по-късни глави)
- Присвояване на **роли без ключове** (`Cognitive Services OpenAI User`), така че да се вписвате с `az login` без управление на ключове

## Изисквания

- [Абонамент в Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) и [Maven 3.9+](https://maven.apache.org/download.cgi)

## Вариант А: Пускане с azd + Bicep (Препоръчително)

От папката `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Вход (и за двата инструмента)
azd auth login
az login

# Осигуряване на акаунт във Foundry + разгръщане на модели
azd up
```

`azd` ще поиска **име на среда** (например `genai-java`), **абонамент** и **регион**. Изберете своя абонамент и регион, където са налични `gpt-5.6-luna` и `text-embedding-3-small`, например `eastus2`. Потвърдете, че абонаментът има достатъчен квотен лимит за модела и типа разгръщане в този регион; наличността и квотата варират според абонамента.

Когато разгръщането приключи, azd:

1. Разгръща всичко дефинирано в [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Изпълнява postprovision скрипт, който записва [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) с вашите крайни точки и имена на разгръщане (без тайни).

> **Съвет:** Стартирайте отново `azd up` по всяко време, за да приложите промени. Стартирайте `azd down` за изтриване и спиране на начисляване на разходи.

За да видите генерираните настройки:

```bash
azd env get-values
```

Сега прескочете към [Тестване на настройката](#тестване-на-настройката).

## Вариант Б: Създаване на ресурси ръчно

Предпочитате портала? Създайте ресурсите ръчно:

1. Отидете на [Azure AI Foundry портала](https://ai.azure.com/) и влезте.
2. **Създайте проект** (това също създава ресурс AI Foundry). Дайте му име като `GenAIJava`.
3. В проекта отворете **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Разгърнете **GPT-5.6 Luna** (модел и име на разгръщане `gpt-5.6-luna`, версия `2026-07-09`) с капацитет **Global Standard** 10. Повторете за **text-embedding-3-small**, версия `1`, ако искате примерите с embedding.
5. От **Overview**, копирайте **endpoint** (например `https://<resource>.openai.azure.com/`).
6. Дайте си достъп без ключове: на ресурса отворете **Access control (IAM)** → **Add role assignment** → назначете **Cognitive Services OpenAI User** за своя акаунт.

> **Все още имате проблеми?** Вижте [документацията на Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Конфигуриране на вашата среда

**Ако сте използвали Вариант А (`azd up`)**, вашият файл с настройки вече е записан — няма нищо за конфигуриране. Отидете директно на [Тестване на настройката](#тестване-на-настройката).

**Ако сте използвали Вариант Б (ръчно)**, създайте `.env` файла на примера сами:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Редактирайте `.env` с вашия endpoint (без ключ — автентикацията е без ключове):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Използвайте Azure OpenAI endpoint на ресурса, а не URL на проект. Приложението basic-chat го превръща в `/openai/v1` и конфигурира явен клиент с bearer токен; API ключ не се изисква.

> **Забележка за сигурност:** Няма API ключ за съхранение. Автентикацията е чрез Microsoft Entra ID с `az login` (локално) или управлявана идентичност (в Azure). Файлът `.env` съдържа само не секретни настройки и вече е в `.gitignore`.

## Тестване на настройката

Уверете се, че сте влезли, за да може автентикацията без ключове да получи токен, след което стартирайте примера:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ако все още не сте влезли
mvn clean spring-boot:run
```

Трябва да видите отговор от модела `gpt-5.6-luna`. Стартирайте примерите по ред, за да останете в малката стандартна квота; ако получите HTTP 429, изчакайте интервала за повторен опит преди да пробвате отново.

> **Потребители на VS Code:** Натиснете `F5` за стартиране. Приложението зарежда `.env` автоматично.

> **Пълен пример:** Вижте [Пример за Basic Chat с Azure AI Foundry](./examples/basic-chat-azure/README.md) за детайли и отстраняване на проблеми.

## Какво следва?

След разгръщането и успешно изпълнение на примера ще имате:
- Azure AI Foundry с разгръщани `gpt-5.6-luna` и `text-embedding-3-small`
- Автентикация без ключове (Microsoft Entra ID) — няма ключове за управление
- Локален `.env` с вашия endpoint и имена на разгръщане
- Подготвена Java среда за разработка

**Продължете към** [Глава 3: Основни техники за генеративен AI](../03-CoreGenerativeAITechniques/README.md), за да започнете да изграждате AI приложения!

## Ресурси

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Автентикация без ключове с Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Документация на Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Преход към Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Официален OpenAI Java SDK с Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Допълнителни ресурси

- [Изтеглете VS Code](https://code.visualstudio.com/Download)
- [Вземете Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Конфигурация на Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводачески услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Ние не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->