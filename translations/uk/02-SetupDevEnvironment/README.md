<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:37:20+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "uk"
}
-->
# Налаштування середовища розробки для Generative AI на Java

> **Швидкий старт**: Код у хмарі за 2 хвилини - перейдіть до [Налаштування GitHub Codespaces](../../../02-SetupDevEnvironment) - не потрібна локальна установка, використовуються моделі GitHub!

> **Цікавитесь Azure OpenAI?** Ознайомтеся з нашим [Посібником з налаштування Azure OpenAI](getting-started-azure-openai.md), де описані кроки для створення нового ресурсу Azure OpenAI.

## Що ви дізнаєтесь

- Налаштування середовища розробки Java для AI-додатків
- Вибір і конфігурація вашого улюбленого середовища розробки (хмарне з Codespaces, локальний контейнер розробки або повна локальна установка)
- Тестування налаштування шляхом підключення до моделей GitHub

## Зміст

- [Що ви дізнаєтесь](../../../02-SetupDevEnvironment)
- [Вступ](../../../02-SetupDevEnvironment)
- [Крок 1: Налаштуйте середовище розробки](../../../02-SetupDevEnvironment)
  - [Варіант A: GitHub Codespaces (Рекомендовано)](../../../02-SetupDevEnvironment)
  - [Варіант B: Локальний контейнер розробки](../../../02-SetupDevEnvironment)
  - [Варіант C: Використання вашої існуючої локальної установки](../../../02-SetupDevEnvironment)
- [Крок 2: Створіть персональний токен доступу GitHub](../../../02-SetupDevEnvironment)
- [Крок 3: Тестування налаштування](../../../02-SetupDevEnvironment)
- [Усунення несправностей](../../../02-SetupDevEnvironment)
- [Підсумок](../../../02-SetupDevEnvironment)
- [Наступні кроки](../../../02-SetupDevEnvironment)

## Вступ

Цей розділ допоможе вам налаштувати середовище розробки. Ми будемо використовувати **GitHub Models** як основний приклад, оскільки це безкоштовно, легко налаштувати лише за допомогою облікового запису GitHub, не потребує кредитної картки і надає доступ до кількох моделей для експериментів.

**Локальне налаштування не потрібне!** Ви можете почати писати код негайно, використовуючи GitHub Codespaces, який забезпечує повне середовище розробки у вашому браузері.

<img src="./images/models.webp" alt="Скріншот: GitHub Models" width="50%">

Ми рекомендуємо використовувати [**GitHub Models**](https://github.com/marketplace?type=models) для цього курсу, оскільки це:
- **Безкоштовно** для початку
- **Легко** налаштувати лише за допомогою облікового запису GitHub
- **Не потрібна кредитна картка**
- **Доступні кілька моделей** для експериментів

> **Примітка**: Моделі GitHub, які використовуються в цьому навчанні, мають такі безкоштовні обмеження:
> - 15 запитів на хвилину (150 на день)
> - ~8,000 слів на вхід, ~4,000 слів на вихід за запит
> - 5 одночасних запитів
> 
> Для використання в продакшені оновіть до Azure AI Foundry Models за допомогою вашого облікового запису Azure. Ваш код не потребує змін. Дивіться [документацію Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Крок 1: Налаштуйте середовище розробки

<a name="quick-start-cloud"></a>

Ми створили попередньо налаштований контейнер розробки, щоб мінімізувати час налаштування і забезпечити наявність усіх необхідних інструментів для цього курсу Generative AI на Java. Виберіть ваш улюблений підхід до розробки:

### Варіанти налаштування середовища:

#### Варіант A: GitHub Codespaces (Рекомендовано)

**Почніть писати код за 2 хвилини - локальне налаштування не потрібне!**

1. Форкніть цей репозиторій у ваш обліковий запис GitHub
   > **Примітка**: Якщо ви хочете змінити базову конфігурацію, перегляньте [Конфігурацію контейнера розробки](../../../.devcontainer/devcontainer.json)
2. Натисніть **Code** → вкладка **Codespaces** → **...** → **New with options...**
3. Використовуйте налаштування за замовчуванням – це вибере **Конфігурацію контейнера розробки**: **Generative AI Java Development Environment**, спеціальний devcontainer, створений для цього курсу
4. Натисніть **Create codespace**
5. Зачекайте ~2 хвилини, поки середовище буде готове
6. Перейдіть до [Кроку 2: Створіть GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Скріншот: підменю Codespaces" width="50%">

<img src="./images/image.png" alt="Скріншот: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Скріншот: опції створення Codespace" width="50%">


> **Переваги Codespaces**:
> - Локальна установка не потрібна
> - Працює на будь-якому пристрої з браузером
> - Попередньо налаштований з усіма інструментами та залежностями
> - Безкоштовно 60 годин на місяць для особистих облікових записів
> - Стабільне середовище для всіх учасників

#### Варіант B: Локальний контейнер розробки

**Для розробників, які віддають перевагу локальній розробці з Docker**

1. Форкніть і клонуйте цей репозиторій на ваш локальний комп'ютер
   > **Примітка**: Якщо ви хочете змінити базову конфігурацію, перегляньте [Конфігурацію контейнера розробки](../../../.devcontainer/devcontainer.json)
2. Встановіть [Docker Desktop](https://www.docker.com/products/docker-desktop/) і [VS Code](https://code.visualstudio.com/)
3. Встановіть розширення [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) у VS Code
4. Відкрийте папку репозиторію у VS Code
5. Коли з'явиться запит, натисніть **Reopen in Container** (або використовуйте `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Зачекайте, поки контейнер буде побудований і запущений
7. Перейдіть до [Кроку 2: Створіть GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Скріншот: налаштування контейнера розробки" width="50%">

<img src="./images/image-3.png" alt="Скріншот: завершення побудови контейнера розробки" width="50%">

#### Варіант C: Використання вашої існуючої локальної установки

**Для розробників з існуючим середовищем Java**

Попередні вимоги:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) або ваш улюблений IDE

Кроки:
1. Клонуйте цей репозиторій на ваш локальний комп'ютер
2. Відкрийте проект у вашому IDE
3. Перейдіть до [Кроку 2: Створіть GitHub Token](../../../02-SetupDevEnvironment)

> **Порада**: Якщо у вас малопотужний комп'ютер, але ви хочете використовувати VS Code локально, скористайтеся GitHub Codespaces! Ви можете підключити ваш локальний VS Code до хмарного Codespace для найкращого досвіду.

<img src="./images/image-2.png" alt="Скріншот: створений локальний контейнер розробки" width="50%">


## Крок 2: Створіть персональний токен доступу GitHub

1. Перейдіть до [Налаштувань GitHub](https://github.com/settings/profile) і виберіть **Settings** у меню вашого профілю.
2. У лівій бічній панелі натисніть **Developer settings** (зазвичай внизу).
3. У розділі **Personal access tokens** натисніть **Fine-grained tokens** (або скористайтеся прямим [посиланням](https://github.com/settings/personal-access-tokens)).
4. Натисніть **Generate new token**.
5. У полі "Token name" вкажіть описову назву (наприклад, `GenAI-Java-Course-Token`).
6. Встановіть дату закінчення терміну дії (рекомендується: 7 днів для забезпечення безпеки).
7. У полі "Resource owner" виберіть ваш обліковий запис користувача.
8. У полі "Repository access" виберіть репозиторії, які ви хочете використовувати з моделями GitHub (або "All repositories", якщо потрібно).
9. У полі "Repository permissions" знайдіть **Models** і встановіть значення **Read and write**.
10. Натисніть **Generate token**.
11. **Скопіюйте і збережіть ваш токен зараз** – ви більше не побачите його!

> **Порада з безпеки**: Використовуйте мінімально необхідний обсяг і найкоротший практичний термін дії для ваших токенів доступу.

## Крок 3: Тестування налаштування з прикладом GitHub Models

Коли ваше середовище розробки готове, протестуйте інтеграцію GitHub Models з нашим прикладом додатку у [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Відкрийте термінал у вашому середовищі розробки.
2. Перейдіть до прикладу GitHub Models:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Встановіть ваш GitHub токен як змінну середовища:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Запустіть додаток:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Ви повинні побачити вихідні дані, схожі на:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Розуміння прикладного коду

Спочатку давайте зрозуміємо, що ми щойно запустили. Приклад у `examples/github-models` використовує OpenAI Java SDK для підключення до моделей GitHub:

**Що робить цей код:**
- **Підключається** до моделей GitHub за допомогою вашого персонального токена доступу
- **Надсилає** просте повідомлення "Say Hello World!" до AI моделі
- **Отримує** і відображає відповідь AI
- **Перевіряє**, що ваше налаштування працює правильно

**Ключова залежність** (у `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Основний код** (`App.java`):
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

## Підсумок

Чудово! Тепер у вас все налаштовано:

- Створено персональний токен доступу GitHub з правильними дозволами для доступу до AI моделей
- Запущено середовище розробки Java (будь то Codespaces, контейнери розробки або локальне)
- Підключено до моделей GitHub за допомогою OpenAI Java SDK для безкоштовної розробки AI
- Перевірено, що все працює за допомогою простого прикладу, який взаємодіє з AI моделями

## Наступні кроки

[Розділ 3: Основні техніки Generative AI](../03-CoreGenerativeAITechniques/README.md)

## Усунення несправностей

Виникли проблеми? Ось поширені проблеми та їх вирішення:

- **Токен не працює?** 
  - Переконайтеся, що ви скопіювали весь токен без зайвих пробілів
  - Перевірте, що токен правильно встановлений як змінна середовища
  - Переконайтеся, що ваш токен має правильні дозволи (Models: Read and write)

- **Maven не знайдено?** 
  - Якщо ви використовуєте контейнери розробки/Codespaces, Maven має бути попередньо встановлений
  - Для локального налаштування переконайтеся, що встановлено Java 21+ і Maven 3.9+
  - Спробуйте `mvn --version`, щоб перевірити установку

- **Проблеми з підключенням?** 
  - Перевірте ваше інтернет-з'єднання
  - Переконайтеся, що GitHub доступний з вашої мережі
  - Перевірте, чи немає брандмауера, який блокує кінцеву точку моделей GitHub

- **Контейнер розробки не запускається?** 
  - Переконайтеся, що Docker Desktop запущений (для локальної розробки)
  - Спробуйте перебудувати контейнер: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Помилки компіляції додатку?**
  - Переконайтеся, що ви знаходитесь у правильній директорії: `02-SetupDevEnvironment/examples/github-models`
  - Спробуйте очистити і перебудувати: `mvn clean compile`

> **Потрібна допомога?**: Все ще виникають проблеми? Відкрийте issue у репозиторії, і ми вам допоможемо.

**Відмова від відповідальності**:  
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критичної інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.