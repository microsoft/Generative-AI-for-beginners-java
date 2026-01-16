<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-07-27T13:31:56+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "uk"
}
-->
# Налаштування середовища розробки для Azure OpenAI

> **Швидкий старт**: Цей посібник призначений для налаштування Azure OpenAI. Для швидкого старту з безкоштовними моделями скористайтеся [GitHub Models with Codespaces](./README.md#quick-start-cloud).

Цей посібник допоможе вам налаштувати моделі Azure AI Foundry для ваших Java AI додатків у цьому курсі.

## Зміст

- [Огляд швидкого налаштування](../../../02-SetupDevEnvironment)
- [Крок 1: Створення ресурсів Azure AI Foundry](../../../02-SetupDevEnvironment)
  - [Створення хабу та проєкту](../../../02-SetupDevEnvironment)
  - [Розгортання моделі GPT-4o-mini](../../../02-SetupDevEnvironment)
- [Крок 2: Створення Codespace](../../../02-SetupDevEnvironment)
- [Крок 3: Налаштування середовища](../../../02-SetupDevEnvironment)
- [Крок 4: Тестування налаштування](../../../02-SetupDevEnvironment)
- [Що далі?](../../../02-SetupDevEnvironment)
- [Ресурси](../../../02-SetupDevEnvironment)
- [Додаткові ресурси](../../../02-SetupDevEnvironment)

## Огляд швидкого налаштування

1. Створіть ресурси Azure AI Foundry (хаб, проєкт, модель)
2. Створіть Codespace з контейнером для розробки на Java
3. Налаштуйте файл .env з обліковими даними Azure OpenAI
4. Перевірте налаштування за допомогою прикладного проєкту

## Крок 1: Створення ресурсів Azure AI Foundry

### Створення хабу та проєкту

1. Перейдіть на [портал Azure AI Foundry](https://ai.azure.com/) і увійдіть у систему
2. Натисніть **+ Create** → **New hub** (або перейдіть до **Management** → **All hubs** → **+ New hub**)
3. Налаштуйте ваш хаб:
   - **Назва хабу**: наприклад, "MyAIHub"
   - **Підписка**: виберіть вашу підписку Azure
   - **Група ресурсів**: створіть нову або виберіть існуючу
   - **Розташування**: виберіть найближче до вас
   - **Обліковий запис сховища**: використовуйте стандартний або налаштуйте власний
   - **Key vault**: використовуйте стандартний або налаштуйте власний
   - Натисніть **Next** → **Review + create** → **Create**
4. Після створення натисніть **+ New project** (або **Create project** з огляду хабу)
   - **Назва проєкту**: наприклад, "GenAIJava"
   - Натисніть **Create**

### Розгортання моделі GPT-4o-mini

1. У вашому проєкті перейдіть до **Model catalog** і знайдіть **gpt-4o-mini**
   - *Альтернатива: Перейдіть до **Deployments** → **+ Create deployment***
2. Натисніть **Deploy** на картці моделі gpt-4o-mini
3. Налаштуйте розгортання:
   - **Назва розгортання**: "gpt-4o-mini"
   - **Версія моделі**: використовуйте останню
   - **Тип розгортання**: стандартний
4. Натисніть **Deploy**
5. Після розгортання перейдіть до вкладки **Deployments** і скопіюйте ці значення:
   - **Назва розгортання** (наприклад, "gpt-4o-mini")
   - **Цільовий URI** (наприклад, `https://your-hub-name.openai.azure.com/`) 
      > **Важливо**: Скопіюйте лише базовий URL (наприклад, `https://myhub.openai.azure.com/`), а не повний шлях до кінцевої точки.
   - **Ключ** (з розділу Keys and Endpoint)

> **Все ще виникають проблеми?** Відвідайте офіційну [документацію Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)

## Крок 2: Створення Codespace

1. Форкніть цей репозиторій у ваш обліковий запис GitHub
   > **Примітка**: Якщо ви хочете змінити базову конфігурацію, перегляньте [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. У вашому форкнутому репозиторії натисніть **Code** → вкладка **Codespaces**
3. Натисніть **...** → **New with options...**
![створення Codespace з опціями](../../../translated_images/uk/codespaces.9945ded8ceb431a5.webp)
4. Виберіть **Конфігурацію контейнера для розробки**: 
   - **Середовище розробки Generative AI Java**
5. Натисніть **Create codespace**

## Крок 3: Налаштування середовища

Коли ваш Codespace буде готовий, налаштуйте облікові дані Azure OpenAI:

1. **Перейдіть до прикладного проєкту з кореня репозиторію:**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **Створіть файл .env:**
   ```bash
   cp .env.example .env
   ```

3. **Редагуйте файл .env, додавши облікові дані Azure OpenAI:**
   ```bash
   # Your Azure OpenAI API key (from Azure AI Foundry portal)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # Your Azure OpenAI endpoint URL (e.g., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **Примітка щодо безпеки**: 
   > - Ніколи не додавайте файл `.env` до системи контролю версій
   > - Файл `.env` вже включений до `.gitignore`
   > - Зберігайте ваші API-ключі в безпеці та регулярно змінюйте їх

## Крок 4: Тестування налаштування

Запустіть прикладний додаток, щоб перевірити підключення до Azure OpenAI:

```bash
mvn clean spring-boot:run
```

Ви повинні побачити відповідь від моделі GPT-4o-mini!

> **Користувачі VS Code**: Ви також можете натиснути `F5` у VS Code, щоб запустити додаток. Конфігурація запуску вже налаштована для автоматичного завантаження вашого файлу `.env`.

> **Повний приклад**: Перегляньте [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) для детальних інструкцій та усунення несправностей.

## Що далі?

**Налаштування завершено!** Тепер у вас є:
- Azure OpenAI з розгорнутим gpt-4o-mini
- Локальна конфігурація файлу .env
- Середовище розробки на Java готове

**Продовжуйте до** [Розділу 3: Основні техніки генеративного AI](../03-CoreGenerativeAITechniques/README.md), щоб почати створювати AI-додатки!

## Ресурси

- [Документація Azure AI Foundry](https://learn.microsoft.com/azure/ai-services/)
- [Документація Spring AI Azure OpenAI](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Java SDK для Azure OpenAI](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## Додаткові ресурси

- [Завантажити VS Code](https://code.visualstudio.com/Download)
- [Отримати Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Конфігурація контейнера для розробки](../../../.devcontainer/devcontainer.json)

**Відмова від відповідальності**:  
Цей документ було перекладено за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критичної інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникають внаслідок використання цього перекладу.