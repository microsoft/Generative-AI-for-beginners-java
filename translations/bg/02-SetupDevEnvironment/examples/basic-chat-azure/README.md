# Основен чат с Azure AI Foundry - пример от край до край

Този пример е просто Spring Boot приложение, което се свързва с модел на **Azure AI Foundry** с помощта на **автентикация без ключ** (Microsoft Entra ID) и тества вашата настройка. Използва Spring AI `ChatClient`, подкрепен от **официалния OpenAI Java SDK** и крайна точка **Azure OpenAI v1**.

Версиите в [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) са Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** и dotenv-java **3.2.0**. Примерът използва `spring-ai-starter-model-openai` и изрично декларира `openai-java` и `azure-identity`; Spring AI 2 премахна стария Azure OpenAI стартер.

## Съдържание

- [Изисквания](#изисквания)
- [Бърз старт](#бърз-старт)
- [Как работи автентикацията](#как-работи-автентикацията)
- [Стартиране на приложението](#стартиране-на-приложението)
  - [Използване на Maven](#използване-на-maven)
  - [Използване на VS Code](#използване-на-vs-code)
  - [Очакван резултат](#очакван-резултат)
- [Справочна информация за конфигурацията](#справочна-информация-за-конфигурацията)
  - [Променливи на средата](#променливи-на-средата)
  - [Spring конфигурация](#spring-конфигурация)
- [Отстраняване на проблеми](#отстраняване-на-проблеми)
  - [Чести проблеми](#чести-проблеми)
  - [Режим за отстраняване на грешки](#режим-за-отстраняване-на-грешки)
- [Следващи стъпки](#следващи-стъпки)
- [Ресурси](#ресурси)

## Изисквания

Преди да стартирате този пример, уверете се, че имате:

- Ресурс Azure AI Foundry с разгръщане `gpt-5.6-luna` – създайте го с `azd up` или ръчно чрез [ръководството за настройка на Azure AI Foundry](../../getting-started-azure-openai.md)
- Ролята **Cognitive Services OpenAI User** за този ресурс (Bicep шаблоните я задават автоматично)
- [Инструмент Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), влязъл с `az login`
- Java 21+ и Maven 3.9+

> **Не е необходим API ключ** — автентикацията е без ключ чрез Microsoft Entra ID.

## Бърз старт

```bash
# 1. Отидете до проекта
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Влезте, за да може безключовата автентикация да получи токен
az login

# 3. Конфигурирайте крайния пункт
#    - Ако сте изпълнили `azd up`, .env беше създаден автоматично (пропуснете тази стъпка).
#    - В противен случай копирайте шаблона и задайте AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Стартирайте приложението
mvn spring-boot:run
```

## Как работи автентикацията

Този пример използва автентикация с **Microsoft Entra ID** — няма API ключ.

Приложението конфигурира автентикация изрично в [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` създава `BearerTokenCredential`, използвайки `AuthenticationUtil.getBearerTokenSupplier` с `DefaultAzureCredential` и обхват `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` изгражда `OpenAIClient` с `OpenAIOkHttpClient.builder()`, определя крайна точка `/openai/v1` и задава удостоверяването с `.credential(...)`.
3. `azureChatModel()` подава този клиент на Spring AI `OpenAiChatModel`, който поддържа `ChatClient` на урока.

Тези изрични бъйнове предотвратяват глобален `OPENAI_API_KEY` да замести автентикацията на Azure. Пропускането на API ключ само в YAML не е достатъчно за настройка на автентикацията. `DefaultAzureCredential` може да използва вашата сесия `az login` локално или управлявана идентичност в Azure; избраната идентичност трябва да има описаната по-горе роля.

## Стартиране на приложението

### Използване на Maven

```bash
mvn spring-boot:run
```

### Използване на VS Code

1. Отворете проекта във VS Code
2. Натиснете `F5` или използвайте панела "Run and Debug"
3. Изберете конфигурация "Spring Boot-BasicChatApplication"

> **Забележка**: Приложението зарежда `.env` от работната си директория, включително при стартиране от VS Code.

### Очакван резултат

Илюстративен изход след успешен старт (регистрационните съобщения при стартиране са пропуснати; формулировката на отговора може да варира):

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

## Справочна информация за конфигурацията

### Променливи на средата

| Променлива | Описание | Задължително | Пример |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Крайна точка на Foundry (Azure OpenAI) | Да | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Име на разгръщане на чат модела | Не | `gpt-5.6-luna` (по подразбиране) |

> Няма променлива за API ключ — автентикацията е без ключ (Microsoft Entra ID чрез `az login`).

### Spring конфигурация

Настройките в [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) използват префикс `spring.ai.openai` и изравнени свойства за чат (без блок `options`):

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

`model` е **името на Azure разгръщането**. Автентикацията се осигурява от изричните бъйнове, описани по-горе, а не от настройка `api-key`. Урокът деактивира reasoning и ограничава completion токени до 500; оставя `temperature` и наследения `max-tokens` незададени.

Microsoft препоръчва [официалния OpenAI SDK с Azure OpenAI v1 и Responses API за нови приложения](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions остава поддържан за този съществуващ урок с базирани на съобщения взаимодействия. За GPT-5.6 заявките с инструменти на Chat Completions трябва да зададат `reasoning_effort` на `none`; използвайте Responses при комбиниране на reasoning с инструменти. Вижте [извикване на инструменти с reasoning модели](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Отстраняване на проблеми

### Чести проблеми

<details>
<summary><strong>Грешка: 401 / "PermissionDenied" / проблеми с токен</strong></summary>

- Стартирайте `az login` — автентикация без ключ изисква активен вход за получаване на токен
- Проверете дали вашият акаунт има роля **Cognitive Services OpenAI User** за ресурса
- Ако току-що сте присвоили ролята, изчакайте минута за разпространение
- Потвърдете, че сте в правилния tenant/абонамент (`az account show`)
</details>

<details>
<summary><strong>Грешка: "The endpoint is not valid" / проблеми с връзката</strong></summary>

- Уверете се, че `AZURE_OPENAI_ENDPOINT` е пълният основен URL (например `https://your-resource.openai.azure.com/`)
- Проверете за последователност на наклонена черта накрая
- Потвърдете, че крайната точка съвпада с разположения от вас ресурс (`azd env get-values`)
</details>

<details>
<summary><strong>Грешка: "The deployment was not found"</strong></summary>

- Проверете дали `AZURE_OPENAI_DEPLOYMENT` съвпада с име на разгръщане в Azure
- Уверете се, че моделът е успешно разположен и активен
- По подразбиране името на разгръщането е `gpt-5.6-luna`
</details>

<details>
<summary><strong>Грешка: 429 / надвишен лимит на заявки</strong></summary>

- По подразбиране разгръщането GPT-5.6 Luna има капацитет Global Standard 10: 10 заявки/минута и 10,000 токена/минута
- Стартирайте примерите последователно и изчакайте интервала за повторен опит на услугата преди да опитате отново
- Този основен пример деактивира автоматичните повторни опити на SDK, така че неуспешна заявка се отчита веднага
</details>

<details>
<summary><strong>VS Code: Променливите на средата не се зареждат</strong></summary>

- Уверете се, че вашият `.env` файл е в корена на проекта (на същото ниво като `pom.xml`)
- Опитайте да стартирате `mvn spring-boot:run` в интегрирания терминал на VS Code
- Проверете дали Java разширението за VS Code е правилно инсталирано
</details>

### Режим за отстраняване на грешки

За да активирате подробен лог, премахнете коментара от тези редове в [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Следващи стъпки

**Настройката е завършена!** Продължете вашето обучение:

[Глава 3: Основни техники в генеративния AI](../../../03-CoreGenerativeAITechniques/README.md)

## Ресурси

- [Преход към OpenAI Java SDK в Spring AI 2](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Официалния OpenAI Java SDK с Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Автентикация без ключ с Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Портал Azure AI Foundry](https://ai.azure.com/)
- [Документация на Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Отказ от отговорност**:
Този документ е преведен с помощта на AI преводачески услуга [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля имайте предвид, че автоматизираните преводи могат да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Ние не носим отговорност за каквито и да е недоразумения или неправилни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->