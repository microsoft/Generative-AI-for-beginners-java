<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:20:18+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "bg"
}
-->
# Настройване на среда за разработка за Генеративен AI за Java

> **Бърз старт**: Кодирайте в облака за 2 минути - преминете към [Настройка на GitHub Codespaces](../../../02-SetupDevEnvironment) - без нужда от локална инсталация и използва модели на GitHub!

> **Интересувате се от Azure OpenAI?**, вижте нашето [Ръководство за настройка на Azure OpenAI](getting-started-azure-openai.md) със стъпки за създаване на нов ресурс Azure OpenAI.

## Какво ще научите

- Настройване на Java среда за разработка за AI приложения
- Избор и конфигуриране на предпочитаната среда за разработка (облачно с Codespaces, локален dev контейнер или пълна локална настройка)
- Тестване на настройката чрез свързване към модели на GitHub

## Съдържание

- [Какво ще научите](../../../02-SetupDevEnvironment)
- [Въведение](../../../02-SetupDevEnvironment)
- [Стъпка 1: Настройте вашата среда за разработка](../../../02-SetupDevEnvironment)
  - [Опция A: GitHub Codespaces (Препоръчително)](../../../02-SetupDevEnvironment)
  - [Опция B: Локален dev контейнер](../../../02-SetupDevEnvironment)
  - [Опция C: Използвайте вашата съществуваща локална инсталация](../../../02-SetupDevEnvironment)
- [Стъпка 2: Създайте GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Стъпка 3: Тествайте вашата настройка](../../../02-SetupDevEnvironment)
- [Отстраняване на проблеми](../../../02-SetupDevEnvironment)
- [Обобщение](../../../02-SetupDevEnvironment)
- [Следващи стъпки](../../../02-SetupDevEnvironment)

## Въведение

Тази глава ще ви насочи как да настроите среда за разработка. Ще използваме **GitHub Models** като основен пример, защото е безплатен, лесен за настройка само с GitHub акаунт, не изисква кредитна карта и предоставя достъп до множество модели за експериментиране.

**Без нужда от локална настройка!** Можете да започнете да кодирате веднага, използвайки GitHub Codespaces, който предоставя пълна среда за разработка директно в браузъра.

<img src="./images/models.webp" alt="Снимка: GitHub Models" width="50%">

Препоръчваме използването на [**GitHub Models**](https://github.com/marketplace?type=models) за този курс, защото:
- **Безплатен** за започване
- **Лесен** за настройка само с GitHub акаунт
- **Без нужда от кредитна карта**
- **Множество модели** за експериментиране

> **Забележка**: Моделите на GitHub, използвани в това обучение, имат следните безплатни ограничения:
> - 15 заявки на минута (150 на ден)
> - ~8,000 думи вход, ~4,000 думи изход на заявка
> - 5 едновременни заявки
> 
> За производствено използване, преминете към Azure AI Foundry Models с вашия Azure акаунт. Вашият код няма нужда от промяна. Вижте [документацията за Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Стъпка 1: Настройте вашата среда за разработка

<a name="quick-start-cloud"></a>

Създадохме предварително конфигуриран контейнер за разработка, за да минимизираме времето за настройка и да гарантираме, че разполагате с всички необходими инструменти за този курс за Генеративен AI за Java. Изберете предпочитания подход за разработка:

### Опции за настройка на средата:

#### Опция A: GitHub Codespaces (Препоръчително)

**Започнете да кодирате за 2 минути - без нужда от локална настройка!**

1. Форкнете това хранилище към вашия GitHub акаунт
   > **Забележка**: Ако искате да редактирате основната конфигурация, вижте [Конфигурация на dev контейнера](../../../.devcontainer/devcontainer.json)
2. Кликнете **Code** → **Codespaces** таб → **...** → **New with options...**
3. Използвайте настройките по подразбиране – това ще избере **Конфигурацията на dev контейнера**: **Generative AI Java Development Environment**, специално създаден за този курс
4. Кликнете **Create codespace**
5. Изчакайте ~2 минути, докато средата бъде готова
6. Продължете към [Стъпка 2: Създайте GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Снимка: Codespaces подменю" width="50%">

<img src="./images/image.png" alt="Снимка: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Снимка: Create codespace options" width="50%">


> **Предимства на Codespaces**:
> - Без нужда от локална инсталация
> - Работи на всяко устройство с браузър
> - Предварително конфигуриран с всички инструменти и зависимости
> - Безплатни 60 часа на месец за лични акаунти
> - Консистентна среда за всички обучаващи се

#### Опция B: Локален dev контейнер

**За разработчици, които предпочитат локална разработка с Docker**

1. Форкнете и клонирайте това хранилище на вашата локална машина
   > **Забележка**: Ако искате да редактирате основната конфигурация, вижте [Конфигурация на dev контейнера](../../../.devcontainer/devcontainer.json)
2. Инсталирайте [Docker Desktop](https://www.docker.com/products/docker-desktop/) и [VS Code](https://code.visualstudio.com/)
3. Инсталирайте [Dev Containers разширението](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) в VS Code
4. Отворете папката на хранилището в VS Code
5. Когато бъдете подканени, кликнете **Reopen in Container** (или използвайте `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Изчакайте контейнерът да се изгради и стартира
7. Продължете към [Стъпка 2: Създайте GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Снимка: Настройка на dev контейнер" width="50%">

<img src="./images/image-3.png" alt="Снимка: Завършено изграждане на dev контейнер" width="50%">

#### Опция C: Използвайте вашата съществуваща локална инсталация

**За разработчици със съществуващи Java среди**

Предпоставки:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) или предпочитан IDE

Стъпки:
1. Клонирайте това хранилище на вашата локална машина
2. Отворете проекта във вашия IDE
3. Продължете към [Стъпка 2: Създайте GitHub Token](../../../02-SetupDevEnvironment)

> **Професионален съвет**: Ако имате машина с ниски спецификации, но искате VS Code локално, използвайте GitHub Codespaces! Можете да свържете вашия локален VS Code към облачно хостван Codespace за най-доброто от двата свята.

<img src="./images/image-2.png" alt="Снимка: Създаден локален dev контейнер" width="50%">


## Стъпка 2: Създайте GitHub Personal Access Token

1. Отидете на [GitHub Settings](https://github.com/settings/profile) и изберете **Settings** от менюто на вашия профил.
2. В лявата странична лента кликнете **Developer settings** (обикновено в долната част).
3. Под **Personal access tokens**, кликнете **Fine-grained tokens** (или следвайте този директен [линк](https://github.com/settings/personal-access-tokens)).
4. Кликнете **Generate new token**.
5. Под "Token name", предоставете описателно име (например `GenAI-Java-Course-Token`).
6. Задайте дата на изтичане (препоръчително: 7 дни за най-добри практики за сигурност).
7. Под "Resource owner", изберете вашия потребителски акаунт.
8. Под "Repository access", изберете хранилищата, които искате да използвате с GitHub Models (или "All repositories", ако е необходимо).
9. Под "Repository permissions", намерете **Models** и го задайте на **Read and write**.
10. Кликнете **Generate token**.
11. **Копирайте и запазете вашия токен сега** – няма да го видите отново!

> **Съвет за сигурност**: Използвайте минималния необходим обхват и най-краткия практически срок на валидност за вашите токени за достъп.

## Стъпка 3: Тествайте вашата настройка с примера за модели на GitHub

След като вашата среда за разработка е готова, нека тестваме интеграцията с GitHub Models с нашето примерно приложение в [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Отворете терминала във вашата среда за разработка.
2. Навигирайте до примера за модели на GitHub:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Задайте вашия GitHub токен като променлива на средата:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Стартирайте приложението:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Трябва да видите изход, подобен на:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Разбиране на примерния код

Първо, нека разберем какво току-що стартирахме. Примерът под `examples/github-models` използва OpenAI Java SDK за свързване към GitHub Models:

**Какво прави този код:**
- **Свързва се** към GitHub Models, използвайки вашия токен за достъп
- **Изпраща** просто съобщение "Say Hello World!" към AI модела
- **Получава** и показва отговора на AI
- **Проверява** дали вашата настройка работи правилно

**Основна зависимост** (в `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Основен код** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Обобщение

Чудесно! Вече имате всичко настроено:

- Създадохте GitHub Personal Access Token с правилните разрешения за достъп до AI модели
- Настроихте вашата Java среда за разработка (независимо дали е Codespaces, dev контейнери или локална)
- Свързахте се към GitHub Models, използвайки OpenAI Java SDK за безплатна AI разработка
- Тествахте, че всичко работи с прост пример, който комуникира с AI модели

## Следващи стъпки

[Глава 3: Основни техники за Генеративен AI](../03-CoreGenerativeAITechniques/README.md)

## Отстраняване на проблеми

Имате проблеми? Ето често срещани проблеми и решения:

- **Токенът не работи?** 
  - Уверете се, че сте копирали целия токен без допълнителни интервали
  - Проверете дали токенът е зададен правилно като променлива на средата
  - Уверете се, че вашият токен има правилните разрешения (Models: Read and write)

- **Maven не е намерен?** 
  - Ако използвате dev контейнери/Codespaces, Maven трябва да е предварително инсталиран
  - За локална настройка, уверете се, че Java 21+ и Maven 3.9+ са инсталирани
  - Опитайте `mvn --version`, за да проверите инсталацията

- **Проблеми със свързването?** 
  - Проверете вашата интернет връзка
  - Уверете се, че GitHub е достъпен от вашата мрежа
  - Уверете се, че не сте зад защитна стена, блокираща GitHub Models endpoint

- **Dev контейнерът не стартира?** 
  - Уверете се, че Docker Desktop работи (за локална разработка)
  - Опитайте да изградите контейнера отново: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Грешки при компилация на приложението?**
  - Уверете се, че сте в правилната директория: `02-SetupDevEnvironment/examples/github-models`
  - Опитайте да почистите и изградите отново: `mvn clean compile`

> **Нуждаете се от помощ?**: Все още имате проблеми? Отворете проблем в хранилището и ще ви помогнем.

**Отказ от отговорност**:  
Този документ е преведен с помощта на AI услуга за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматичните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия изходен език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален превод от човек. Ние не носим отговорност за каквито и да било недоразумения или погрешни интерпретации, произтичащи от използването на този превод.