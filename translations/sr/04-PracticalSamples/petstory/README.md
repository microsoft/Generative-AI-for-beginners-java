# Туторијал за генератор прича о кућним љубимцима за почетнике

Отпремите фотографију кућног љубимца, анализирајте је уз помоћ GPT-5.6 Luna и генеришите причу из добијеног описа. Обa захтева модела користе `reasoning_effort: none`.

| Компонента | Верзија |
| --- | --- |
| Java | 21 или новији |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Садржај

- [Предуслови](#предуслови)
- [Разумевање структуре пројекта](#разумевање-структуре-пројекта)
- [Објашњење основних компоненти](#објашњење-основних-компоненти)
  - [1. Главна апликација](#1-главна-апликација)
  - [2. Веб контролер](#2-веб-контролер)
  - [3. Сервис за приче](#3-сервис-за-приче)
  - [4. Веб шаблони](#4-веб-шаблони)
  - [5. Конфигурација](#5-конфигурација)
- [Покретање апликације](#покретање-апликације)
- [Офлајн тестови](#офлајн-тестови)
- [Како све то ради заједно](#како-све-то-ради-заједно)
- [Разумевање AI интеграције](#разумевање-ai-интеграције)
- [Следећи кораци](#следећи-кораци)

## Предуслови

Пре почетка, уверите се да имате:
- Инсталиран Java 21 или новију верзију
- Maven за управљање зависностима
- Размештање Azure AI Foundry GPT-5.6 Luna под именом `gpt-5.6-luna`, или `AZURE_OPENAI_DEPLOYMENT` променљиву која показује на то размештање. Погледајте [Поглавље 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) за постављање и пријавите се са `az login` за аутентификацију без кључа. Размештање мора да подржава унос слика и `reasoning_effort: none`.
- Основно разумевање Java, Spring Boot и веб развоја

## Разумевање структуре пројекта

Пројекат приче о кућном љубимцу има неколико важних фајлова:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Објашњење основних компоненти

### 1. Главна апликација

**Фајл:** `PetStoryApplication.java`

Ово је улазна тачка наше Spring Boot апликације:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Шта ово ради:**
- `@SpringBootApplication` анотација омогућава аутоматску конфигурацију и скенирање компоненти
- Покреће уграђени веб сервер (Tomcat) на порту 8080
- Аутоматски креира све потребне Spring бинове и сервисе

### 2. Веб контролер

**Фајл:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Ендпоинт | Захтев | Успешан одговор |
| --- | --- | --- |
| `GET /` | Без тела | HTML форма за отпремање са CSRF токеном |
| `POST /analyze-image` | `multipart/form-data`, пољe фајл `image` | JSON: `{"description":"Разиграни љубимац..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, поље `description` | HTML страница резултата са описом и генерисаном причом |

Оба POST ендпоинта захтевају сесијски колачић и CSRF токен добијен са `GET /`. Скрипта за отпремање шаље сакривену вредност `_csrf` у заглављу `X-CSRF-TOKEN`; слање приче шаље је као `_csrf` поље форме. API клијенти морају сачувати колачић између захтева. Ово су ендпоинти форма, а не JSON захтеви.

Описи морају бити непразни и не дужи од 1000 карактера. Контролер уклања размаке и брише `<`, `>`, двоструке наводнике, апострофе и `&` пре прослеђивања сервису. Шаблон резултата такође бежи моделски излаз са `th:text`.

Неуспех валидације слике враћа HTTP 400 са пољем `error`; неуспех модела враћа HTTP 502 са пољем `error` и без `description`. Неважећи описи прича или неуспеси модела преусмеравају на `/` са видљивом грешком. Недостајућа обавезна поља враћају HTTP 400, а недостајући или неважећи CSRF токен враћа HTTP 403. Није приказан резерван опис или прича као успешан AI резултат.

### 3. Сервис за приче

**Фајл:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Званични OpenAI Java SDK 4.63.1 позива Azure AI Foundry OpenAI-компатибилан Chat Completions API. Azure Identity 1.18.6 обезбеђује Microsoft Entra bearer токен преко `DefaultAzureCredential`; није потребан API кључ.

| Операција | Улаз | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Бајтови слике кодирани као base64 DАTA URL са отпремљеним MIME типом | 300 |
| `generateStory` | Опис љубимца у корисничкој поруци | 800 |

Оба захтева користе конфигурисано размештање, подразумевано `gpt-5.6-luna`, и експлицитно постављају `ReasoningEffort.NONE` (`reasoning_effort: none`). Ниједан захтев не шаље `temperature` или стари параметар `max_tokens`.

Анализа слике прихвата JPEG, PNG, GIF и WebP, одбацује празне слике и фајлове веће од 10MB, и ограничава опис на 1000 карактера. Подстицај за причу тражи кратку породично-прилагођену причу. Празни избори или празан моделски садржај су грешке, а неуспеси чувају оригинални узрок за дијагностику на серверу. SDK клијент се затвара при гашењу апликације.

### 4. Веб шаблони

**Фајл:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Форма за отпремање)

Страница почиње избором фотографије, а не тексталним пољем за опис. **Анализирај слику** приказује претпоглед одабране фотографије и шаље је на `/analyze-image`. Успешан одговор приказује опис, попуњава сакривено поље `description` и открива дугме **Генериши причу**. То дугме шаље постојећу форму на `/generate-story`.

Нема преузимања модела у прегледачу нити CDN зависности. Анализа слике се обавља на серверу преко конфигурисаног Azure размештања. Неуспеси остају видљиви и не дозвољавају генерисање приче са лажним описом. Избор другог фајла брише претходну анализу.

**Фајл:** `result.html` (Приказ приче)

Приказује генерисану причу:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Функције шаблона:**

1. **Интеграција с Thymeleaf**: Користи `th:` атрибуте за динамички садржај
2. **Респонзивни дизајн**: CSS стили за мобилне и десктоп уређаје
3. **Обрада грешака**: Приказује корисничке грешке валидације
4. **Обрада отпремања**: JavaScript приказује фотографију, шаље CSRF заштићен multipart захтев и приказује враћени опис

### 5. Конфигурација

**Фајл:** `application.properties`

Конфигурационе поставке за апликацију:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Објашњење конфигурације:**

1. **Отпремање фајлова**: Максимална величина за фајл и читав multipart захтев је 10MB; држите фотографије испод овог ограничења да би било места за multipart заглавља
2. **Логовање**: Контролише које информације се бележе током извршавања
3. **Azure AI Foundry**: Наводи endpoint и размештање модела које се користе (аутентикација без кључа)
4. **Сигурност**: CSRF заштита је укључена; дијагностика модела се бележи на серверу, а контролер приказује опште поруке о неуспеху модела

## Покретање апликације

### Корак 1: Пријавите се и подесите ваш endpoint

Аутентикација је без кључа (Microsoft Entra ID), па нема API кључа. Пријавите се и подесите ваш Foundry endpoint:

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Зашто је ово потребно:**
- Azure AI Foundry користи Microsoft Entra ID за аутентификацију захтева за инференцу
- Аутентикација без кључа значи да нема тајни у вашем изворном коду или окружењу
- Ваш налог треба улогу **Cognitive Services OpenAI User** на ресурсу

Подразумевани назив размештања је `gpt-5.6-luna`. Ако ваше GPT-5.6 Luna размештање има другачије име, подесите `AZURE_OPENAI_DEPLOYMENT` у истом терминалу пре покретања апликације. И анализа слика и генерисање приче користе ову поставку.

### Корак 2: Направите билд и покрените

Идите у директоријум пројекта:
```bash
cd 04-PracticalSamples/petstory
```

Направите самостални извршни JAR и покрените све офлајн тестове:
```bash
mvn clean package
```

Покрените сервер:
```bash
mvn spring-boot:run
```

Апликација ће започети на `http://localhost:8080`.

Алтернативно, покрените паковани JAR на слободном порту, на пример:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

За ту команду отворите `http://localhost:8083/`. Исте руте `/analyze-image` и `/generate-story` доступне су на изабраном порту.

### Корак 3: Тестирајте апликацију

1. **Отворите** `http://localhost:8080` у прегледачу
2. **Одаберите** јасну фотографију кућног љубимца у JPEG, PNG, GIF или WebP формату, мању од 10MB
3. **Кликните** "Analyze Image" и сачекајте опис љубимца
4. **Кликните** "Generate Story" након успешне анализе
5. **Прегледајте** причу и искористите линк на страници резултата да се вратите на форму за отпремање

Успешан ток од фотографије ка причи обавља два позива модела, по један за сваки тастер. Жива инференца трошити ће квоту вашег размештања и може укључити трошкове; изводите тестове постепено када делите размештање са ограничењем брзине. Учитавање почетне странице не позива модел.

## Офлајн тестови

Из директоријума узорка покрените:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) бележи реалне OpenAI SDK захтеве користећи HTTP фикстуру. Проверава размештање оба захтева, `reasoning_effort: none`, границе токена, сликовни улаз, валидацију, празне одговоре и грешке из горњег слоја.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) користи MockMvc са мокованим сервисом модела да тестира Thymeleaf странице, уговор отпремања, CSRF, валидацију, бежање излаза и видљиве неуспехе. Ови тестови не захтевају Azure креденцијале и никада не позивају плаћену Azure инференцу. Maven генерише Surefire извештаје у `target/surefire-reports`.

## Како све то ради заједно

Ево комплетног тока када генеришете причу о љубимцу:

1. **Избор фотографије**: Изаберете слику љубимца у форми за отпремање
2. **Отпремање слике**: "Analyze Image" шаље multipart POST на `/analyze-image` са CSRF заглављем
3. **Анализа слике**: `StoryService` шаље слику GPT-5.6 Luna са поставком reasoning на `none`
4. **Приказ описа**: Прегледач приказује добијени опис и чува га у форми
5. **Слање приче**: "Generate Story" шаље `description` и `_csrf` на `/generate-story`
6. **Генерисање приче**: Контролер верификује опис и позива исто размештање са reasoning подешеним на `none`
7. **Рендеровање шаблона**: Thymeleaf бежи и приказује опис и причу на страници резултата

**Ток обраде грешака:**
Ако модел не успе, сервер бележи узрок. Анализа слике враћа HTTP 502 и прегледач приказује грешку без приказивања "Generate Story". Генерисање приче преусмерава на формум са поруком о грешци. Нити један пут тихо не замењује резултат унапред написаном верзијом.

## Разумевање AI интеграције

### Azure AI Foundry (аутентикација без кључа)
Сервис конфигурише SDK са `/openai/v1/` endpoint-ом вашег ресурса. `DefaultAzureCredential` и `AuthenticationUtil.getBearerTokenSupplier` обезбеђују Microsoft Entra токене за `https://ai.azure.com/.default`. Локални развој може користити ваше Azure CLI пријављивање; Azure хостована апликација може користити управљани идентитет са потребним дозволама ресурса.

### Инжењеринг упита
Анализа слике тражи видљиве карактеристике љубимца у кратком пасусу и каже моделу да третира текст на слици као податке, а не инструкције. Генерисање приче користи враћени опис у засебном, породично-прилагођеном захтеву за писање. Нити један позив не омогућава reasoning или подешава температуру.

### Обрада одговора
Заједнички обрађивач одговора одбија недостајуће изборе и празан или само размакнут садржај, скраћује важећи садржај и чува грешке из горњег слоја. Описи слика се ограничавају на 1000 карактера да се уклопе у следећу форму приче. Изворни неуспех модела се чува за дијагностику али се не приказује кориснику.

## Следећи кораци

За више примера погледајте [Поглавље 04: Практични узорци](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Изјава о одрицању одговорности**:
Овај документ је преведен коришћењем услуге за аутоматски превод [Co-op Translator](https://github.com/Azure/co-op-translator). Иако тежимо тачности, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на његовом изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални људски превод. Нисмо одговорни за било каква неспоразума или погрешна тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->