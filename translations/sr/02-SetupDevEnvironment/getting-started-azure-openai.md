# Постављање развојног окружења за Azure AI Foundry

> Овај водич поставља **Azure AI Foundry** моделе за Java AI апликације у овом курсу, користећи **аутентификацију без кључа** (Microsoft Entra ID) — нема потребе за управљањем API кључевима. Нови сте у алатима? Започните са [водичем за развојно окружење](./README.md).

Овај водич поставља **Azure AI Foundry** моделе за Java AI апликације у овом курсу. Имате два пута:

- **Опција А — Постављање са `azd` + Bicep (препоручено):** једна команда разврстава налог Foundry и моделе као код. Није потребно кликање по порталу.
- **Опција Б — Креирање ресурса ручно** у Azure AI Foundry порталу.

Оба пута користе **аутентификацију без кључа** (Microsoft Entra ID) — нема API кључева које треба копирати или угрозити.

## Садржај

- [Шта се креира](#шта-се-креира)
- [Претпоставке](#претпоставке)
- [Опција А: Постављање са azd + Bicep (Препоручено)](#option-a-provision-with-azd--bicep-recommended)
- [Опција Б: Креирање ресурса ручно](#опција-б-креирање-ресурса-ручно)
- [Конфигурисање вашег окружења](#конфигуришите-своје-окружење)
- [Тестирајте своју поставку](#тестирајте-своју-поставку)
- [Шта даље?](#шта-даље)
- [Ресурси](#ресурси)
- [Додатни ресурси](#додатни-ресурси)

## Шта се креира

Bicep шаблони у [`infra/`](../../../02-SetupDevEnvironment/infra) постављају:

- **Azure AI Foundry** налог (`Microsoft.CognitiveServices/accounts`, тип `AIServices`) са пројектом
- Чат распореда - GPT-5.6 Luna (`gpt-5.6-luna`), верзија `2026-07-09`, са капацитетом `GlobalStandard` 10 (10 захтева/минут и 10,000 токена/минут за овај модел)
- Распоред уграђивања - `text-embedding-3-small`, верзија `1` (користи се у каснијим поглављима)
- Додела улоге без кључа (`Cognitive Services OpenAI User`) тако да се пријављујете помоћу `az login` уместо управљања кључевима

## Претпоставке

- [Azure претплата](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) и [Maven 3.9+](https://maven.apache.org/download.cgi)

## Опција А: Постављање са azd + Bicep (Препоручено)

Из фолдера `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Пријавите се (оба алата)
azd auth login
az login

# Обезбедите налог Foundry + распореде модела
azd up
```

`azd` тражи **назив окружења** (на пример `genai-java`), **претплату** и **регион**. Изаберите своју претплату и регион где су доступни `gpt-5.6-luna` и `text-embedding-3-small`, на пример `eastus2`. Потврдите да претплата има довољну квоту за модел и тип распореда у том региону; доступност и квота варирају по претплати.

Када постављање заврши, azd:

1. Распоређује све дефинисано у [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Покреће постпостављачки hook који уписује [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) са вашим именима крајњег тачке и распореда (без тајни).

> **Савет:** Покрените поново `azd up` у било ком тренутку да примените измене. Покрените `azd down` да избришете све и прекинете настајање трошкова.

Да видите генерисане поставке:

```bash
azd env get-values
```

Сада пређите на [Тестирајте своју поставку](#тестирајте-своју-поставку).

## Опција Б: Креирање ресурса ручно

Више волите портал? Креирајте ресурсе ручно:

1. Идите на [Azure AI Foundry портал](https://ai.azure.com/) и пријавите се.
2. **Креирајте пројекат** (ово такође креира AI Foundry ресурс). Дајте име као `GenAIJava`.
3. У вашем пројекту отворите **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Распоредите **GPT-5.6 Luna** (име модела и распореда `gpt-5.6-luna`, верзија `2026-07-09`) са капацитетом **Global Standard** 10. Поновите за **text-embedding-3-small**, верзија `1`, ако желите примере уграђивања.
5. Са странице **Overview**, копирајте **крајњу тачку** (на пример `https://<resource>.openai.azure.com/`).
6. Дозволите себи приступ без кључа: на ресурсу отворите **Access control (IAM)** → **Add role assignment** → доделите улогу **Cognitive Services OpenAI User** вашем налогу.

> **Још увек имате проблема?** Погледајте [документацију Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Конфигуришите своје окружење

**Ако сте користили опцију А (`azd up`)**, ваш фајл са подешавањима је већ уписан — нема шта да се подешава. Пређите на [Тестирајте своју поставку](#тестирајте-своју-поставку).

**Ако сте користили опцију Б (ручно)**, сами креирајте `.env` фајл примера:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Уредите `.env` са вашом крајњом тачком (без кључа — аутентификација је без кључа):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Користите Azure OpenAI крајњу тачку ресурса, не URL пројекта. апликација basic-chat је пребацује на `/openai/v1` и конфигурише изричитог клијента са bearer токеном; API кључ није потребан.

> **Безбедносна напомена:** Не постоји API кључ за чување. Аутентификујете се помоћу Microsoft Entra ID преко `az login` (локално) или управљеног идентитета (у Azure). Фајл `.env` садржи само непотпунске параметре и већ је обухваћен `.gitignore`.

## Тестирајте своју поставку

Уверите се да сте пријављени како би аутентификација без кључа могла добити токен, затим покрените пример:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ако већ нисте пријављени
mvn clean spring-boot:run
```

Требало би да видите одговор са модела `gpt-5.6-luna`. Покретајте примере узастопно да би сте остали унутар мале подразумеване квоте; ако добијете HTTP 429, сачекајте период поновног покушаја пре него што покушате поново.

> **Корисници VS Code:** Притисните `F5` за покретање. Апликација аутоматски учитава ваш `.env`.

> **Комлетан пример:** Погледајте [Основни чат са Azure AI Foundry примером](./examples/basic-chat-azure/README.md) за детаље и решавање проблема.

## Шта даље?

Након постављања и успешног покретања примера, имаћете:
- Azure AI Foundry са разврстаним `gpt-5.6-luna` и `text-embedding-3-small`
- Аутентификацију без кључа (Microsoft Entra ID) — нема кључева за управљање
- Локални `.env` са вашим крајњим тачкама и именима распореда
- Јава развојно окружење спремно за рад

**Наставите на** [Поглавље 3: Основне технике генеративне вештачке интелигенције](../03-CoreGenerativeAITechniques/README.md) да бисте почели са израдом AI апликација!

## Ресурси

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Аутентификација без кључа са Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Документација Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Преход са Spring AI 2 на OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Званични OpenAI Java SDK са Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Додатни ресурси

- [Преузми VS Code](https://code.visualstudio.com/Download)
- [Преузми Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Конфигурација Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Изјава о одрицању одговорности**:
Овај документ је преведен коришћењем услуге за аутоматски превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо тачности, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било каква неспоразума или погрешна тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->