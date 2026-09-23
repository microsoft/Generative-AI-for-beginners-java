# Основни ћаскање са Azure AI Foundry - пример од краја до краја

Овај пример је једноставна Spring Boot апликација која се повезује на **Azure AI Foundry** модел користећи **аутификацију без кључа** (Microsoft Entra ID) и тестира вашу поставку. Користи Spring AI `ChatClient`, који се ослања на **званични OpenAI Java SDK** и **Azure OpenAI v1** крајњу тачку.

Верзије у [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) су Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** и dotenv-java **3.2.0**. Пример користи `spring-ai-starter-model-openai` и експлицитно декларише `openai-java` и `azure-identity`; Spring AI 2 је уклонио стари Azure OpenAI стартер.

## Садржај

- [Услови](#услови)
- [Брзи почетак](#брзи-почетак)
- [Како аутентификација функционише](#како-аутентификација-функционише)
- [Покретање апликације](#покретање-апликације)
  - [Коришћење Maven-а](#коришћење-maven-а)
  - [Коришћење VS Code-а](#коришћење-vs-code-а)
  - [Очекивани излаз](#очекивани-излаз)
- [Референца конфигурације](#референца-конфигурације)
  - [Променљиве окружења](#променљиве-окружења)
  - [Spring конфигурација](#spring-конфигурација)
- [Решавање проблема](#решавање-проблема)
  - [Уобичајени проблеми](#уобичајени-проблеми)
  - [Дебаг мод](#дебаг-мод)
- [Следећи кораци](#следећи-кораци)
- [Ресурси](#ресурси)

## Услови

Пре покретања овог примера, уверите се да имате:

- Azure AI Foundry ресурс са `gpt-5.6-luna` деплојем - обезбедите га користећи `azd up` или ручно преко [Azure AI Foundry упутства за подешавање](../../getting-started-azure-openai.md)
- Улога **Cognitive Services OpenAI User** на том ресурсу (Bicep шаблони постављају ово за вас)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), пријављен са `az login`
- Java 21+ и Maven 3.9+

> **Без потребе за API кључем** — аутентификација је без кључа преко Microsoft Entra ID.

## Брзи почетак

```bash
# 1. Идите до пројекта
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Пријавите се како би безкључна аутентификација могла добити токен
az login

# 3. Конфигуришите крајњу тачку
#    - Ако сте покренули `azd up`, .env је аутоматски креиран (прескочите ово).
#    - У супротном, копирајте шаблон и подесите AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Покрените апликацију
mvn spring-boot:run
```

## Како аутентификација функционише

Овај пример аутентификује се преко **Microsoft Entra ID** — нема API кључа.

Апликација експлицитно подешава аутентификацију у [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` креира `BearerTokenCredential` користећи `AuthenticationUtil.getBearerTokenSupplier` са `DefaultAzureCredential` и опсегом `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` гради `OpenAIClient` помоћу `OpenAIOkHttpClient.builder()`, резолује крајњу тачку ресурса на `/openai/v1`, и поставља bearer credential са `.credential(...)`.
3. `azureChatModel()` даје тог клијента Spring AI-ју `OpenAiChatModel`, који подржава `ChatClient` из овог примера.

Ови експлицитни bean-ови спречавају да глобални `OPENAI_API_KEY` преустане Azure аутентификацију. Само изостајање API кључа из YAML фајла није потпуна аутентификација. `DefaultAzureCredential` може користити вашу `az login` сесију локално или управљани идентитет у Azure; идентитет који се бира мора имати горе наведене улоге на ресурсу.

## Покретање апликације

### Коришћење Maven-а

```bash
mvn spring-boot:run
```

### Коришћење VS Code-а

1. Отворите пројекат у VS Code-у
2. Притисните `F5` или користите панел "Run and Debug"
3. Изаберите конфигурацију "Spring Boot-BasicChatApplication"

> **Напомена**: Апликација учитава `.env` из радног директоријума, укључујући покретање из VS Code-а.

### Очекивани излаз

Илустративни излаз након успешног покретања (стартуп логови изостављени; формулација одговора варира):

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

## Референца конфигурације

### Променљиве окружења

| Променљива | Опис | Обавезно | Пример |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | URL крајње тачке Foundry (Azure OpenAI) | Да | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Име деплоја чата модела | Не | `gpt-5.6-luna` (подразумевано) |

> Не постоји променљива за API кључ — аутентификација је без кључа (Microsoft Entra ID преко `az login`).

### Spring конфигурација

Подешавања у [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) користе префикс `spring.ai.openai` и простране чат особине (без `options` блока):

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

`model` је **име Azure деплоја**. Аутентификација долази из експлицитних bean-ова описаних горе, а не из подешавања `api-key`. У овом примеру је онемогућено резоновање и ограничен број завршних токена на 500; `temperature` и наслеђени `max-tokens` нису подешени.

Microsoft препоручује [званични OpenAI SDK са Azure OpenAI v1 и Responses API за нове апликације](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions остаје подржан за овај постојећи пример заснован на порукама. За GPT-5.6, захтеви који укључују алате у Chat Completions морају подесити `reasoning_effort` на `none`; за комбиновање резоновања са алатима користите Responses. Погледајте [позив алата са моделима резоновања](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Решавање проблема

### Уобичајени проблеми

<details>
<summary><strong>Грешка: 401 / "PermissionDenied" / грешке са токеном</strong></summary>

- Покрените `az login` — аутентификацији без кључа је потребна активна пријава да добије токен
- Проверите да ваш налог има улогу **Cognitive Services OpenAI User** на ресурсу
- Ако сте управо доделили улогу, сачекајте минут да се промена пропагира
- Потврдите да сте у исправном тенанту/претплати (`az account show`)
</details>

<details>
<summary><strong>Грешка: "Крајња тачка није валидна" / грешке везе</strong></summary>

- Проверите да је `AZURE_OPENAI_ENDPOINT` пуни базични URL (нпр. `https://your-resource.openai.azure.com/`)
- Проверите конзистентност косе црте на крају
- Потврдите да крајња тачка одговара вашем обезбеђеном ресурсу (`azd env get-values`)
</details>

<details>
<summary><strong>Грешка: "Деплој није пронађен"</strong></summary>

- Потврдите да `AZURE_OPENAI_DEPLOYMENT` одговара имену деплоја у Azure
- Проверите да је модел успешно деплојован и активан
- Подразумевано име деплоја је `gpt-5.6-luna`
</details>

<details>
<summary><strong>Грешка: 429 / премашен лимит захтева</strong></summary>

- Подразумевани GPT-5.6 Luna деплој има Global Standard капацитет 10: 10 захтева/минут и 10,000 токена/минут
- Покретајте примере узастопно и сачекајте интервал поновног покушаја сервиса пре поновног покушаја
- Овај основни пример онемогућава аутоматске SDK поновне покушаје, тако да се неуспели захтев директно пријављује
</details>

<details>
<summary><strong>VS Code: Променљиве окружења се не учитавају</strong></summary>

- Уверите се да је ваш `.env` фајл у коренском директоријуму пројекта (на истом нивоу као `pom.xml`)
- Покушајте да покренете `mvn spring-boot:run` у интегрисаном терминалу VS Code-а
- Проверите да је VS Code Java екстензија правилно инсталирана
</details>

### Дебаг мод

Да бисте активирали детаљно логовање, уклоните коментаре са ових линија у [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Следећи кораци

**Подешавање завршено!** Наставите своје учење:

[Поглавље 3: Основне технике генеративне вештачке интелигенције](../../../03-CoreGenerativeAITechniques/README.md)

## Ресурси

- [Преход на Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Званични OpenAI Java SDK са Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Аутентификација без кључа са Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry портал](https://ai.azure.com/)
- [Azure AI Foundry документација](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Изјава о одрицању одговорности**:
Овај документ је преведен коришћењем услуге за аутоматски превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо тачности, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било каква неспоразума или погрешна тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->