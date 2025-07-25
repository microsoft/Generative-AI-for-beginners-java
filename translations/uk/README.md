<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T12:26:42+00:00",
  "source_file": "README.md",
  "language_code": "uk"
}
-->
# Генеративний ШІ для початківців - Java Edition
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Генеративний ШІ для початківців - Java Edition](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.uk.png)

**Часові витрати**: Весь воркшоп можна пройти онлайн без локального налаштування. Якщо ви хочете запустити приклади, налаштування середовища займає 2 хвилини, а дослідження прикладів потребує 1-3 години залежно від глибини вивчення.

> **Швидкий старт**

1. Форкніть цей репозиторій у свій обліковий запис GitHub
2. Натисніть **Code** → вкладка **Codespaces** → **...** → **New with options...**
3. Використовуйте налаштування за замовчуванням – це вибере контейнер розробки, створений для цього курсу
4. Натисніть **Create codespace**
5. Зачекайте ~2 хвилини, поки середовище буде готове
6. Перейдіть одразу до [Створення токена GitHub Models](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Підтримка кількох мов

### Підтримується через GitHub Action (автоматично та завжди актуально)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](./README.md) | [Burmese (Myanmar)](../my/README.md)

## Структура курсу та навчальний шлях

### **Розділ 1: Вступ до генеративного ШІ**
- **Основні концепції**: Розуміння великих мовних моделей, токенів, ембедингів та можливостей ШІ
- **Екосистема Java для ШІ**: Огляд Spring AI та OpenAI SDK
- **Протокол контексту моделі**: Вступ до MCP та його ролі у комунікації агентів ШІ
- **Практичні застосування**: Реальні сценарії, включаючи чат-ботів та генерацію контенту
- **[→ Почати розділ 1](./01-IntroToGenAI/README.md)**

### **Розділ 2: Налаштування середовища розробки**
- **Конфігурація для кількох провайдерів**: Налаштування GitHub Models, Azure OpenAI та інтеграцій OpenAI Java SDK
- **Spring Boot + Spring AI**: Найкращі практики для розробки корпоративних застосунків ШІ
- **GitHub Models**: Безкоштовний доступ до моделей ШІ для прототипування та навчання (без необхідності кредитної картки)
- **Інструменти розробки**: Налаштування Docker-контейнерів, VS Code та GitHub Codespaces
- **[→ Почати розділ 2](./02-SetupDevEnvironment/README.md)**

### **Розділ 3: Основні техніки генеративного ШІ**
- **Інженерія запитів**: Техніки для отримання оптимальних відповідей від моделей ШІ
- **Ембединги та векторні операції**: Реалізація семантичного пошуку та порівняння схожості
- **Генерація з доповненням пошуку (RAG)**: Поєднання ШІ з вашими власними джерелами даних
- **Виклик функцій**: Розширення можливостей ШІ за допомогою кастомних інструментів та плагінів
- **[→ Почати розділ 3](./03-CoreGenerativeAITechniques/README.md)**

### **Розділ 4: Практичні застосування та проєкти**
- **Генератор історій про домашніх улюбленців** (`petstory/`): Творча генерація контенту з використанням GitHub Models
- **Локальна демонстрація Foundry** (`foundrylocal/`): Інтеграція локальної моделі ШІ з OpenAI Java SDK
- **Сервіс калькулятора MCP** (`mcp/calculator/`): Базова реалізація протоколу контексту моделі з Spring AI
- **[→ Почати розділ 4](./04-PracticalSamples/README.md)**

### **Розділ 5: Відповідальна розробка ШІ**
- **Безпека GitHub Models**: Тестування вбудованих фільтрів контенту та механізмів безпеки
- **Демонстрація відповідального ШІ**: Практичний приклад роботи фільтрів безпеки ШІ
- **Найкращі практики**: Основні рекомендації для етичної розробки та впровадження ШІ
- **[→ Почати розділ 5](./05-ResponsibleGenAI/README.md)**

## Додаткові ресурси 

- [AI Agents For Beginners](https://github.com/microsoft/ai-agents-for-beginners)
- [Generative AI for Beginners using .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generative AI for Beginners using JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generative AI for Beginners](https://github.com/microsoft/generative-ai-for-beginners)
- [ML for Beginners](https://aka.ms/ml-beginners)
- [Data Science for Beginners](https://aka.ms/datascience-beginners)
- [AI for Beginners](https://aka.ms/ai-beginners)
- [Cybersecurity for Beginners](https://github.com/microsoft/Security-101)
- [Web Dev for Beginners](https://aka.ms/webdev-beginners)
- [IoT for Beginners](https://aka.ms/iot-beginners)
- [XR Development for Beginners](https://github.com/microsoft/xr-development-for-beginners)
- [Mastering GitHub Copilot for AI Paired Programming](https://aka.ms/GitHubCopilotAI)
- [Mastering GitHub Copilot for C#/.NET Developers](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Choose Your Own Copilot Adventure](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App with Azure AI Services](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Відмова від відповідальності**:  
Цей документ був перекладений за допомогою сервісу автоматичного перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ на його рідній мові слід вважати авторитетним джерелом. Для критичної інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникають внаслідок використання цього перекладу.