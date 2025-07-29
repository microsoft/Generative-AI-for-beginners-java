<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T14:19:56+00:00",
  "source_file": "README.md",
  "language_code": "ru"
}
-->
# Генеративный ИИ для начинающих - версия на Java
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generative AI for Beginners - Java Edition](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ru.png)

**Время выполнения**: Весь воркшоп можно пройти онлайн без локальной настройки. Настройка окружения занимает 2 минуты, а изучение примеров требует от 1 до 3 часов в зависимости от глубины изучения.

> **Быстрый старт**

1. Сделайте форк этого репозитория в свой аккаунт GitHub
2. Нажмите **Code** → вкладка **Codespaces** → **...** → **New with options...**
3. Используйте настройки по умолчанию – это выберет контейнер разработки, созданный для этого курса
4. Нажмите **Create codespace**
5. Подождите ~2 минуты, пока окружение будет готово
6. Перейдите сразу к [Первому примеру](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Поддержка нескольких языков

### Поддерживается через GitHub Action (автоматически и всегда актуально)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](./README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## Структура курса и учебный путь

### **Глава 1: Введение в генеративный ИИ**
- **Основные концепции**: Понимание больших языковых моделей, токенов, эмбеддингов и возможностей ИИ
- **Экосистема Java для ИИ**: Обзор Spring AI и OpenAI SDK
- **Протокол контекста модели**: Введение в MCP и его роль в коммуникации агентов ИИ
- **Практическое применение**: Реальные сценарии, включая чат-ботов и генерацию контента
- **[→ Начать главу 1](./01-IntroToGenAI/README.md)**

### **Глава 2: Настройка среды разработки**
- **Конфигурация для нескольких провайдеров**: Настройка GitHub Models, Azure OpenAI и OpenAI Java SDK
- **Spring Boot + Spring AI**: Лучшие практики для разработки корпоративных приложений с ИИ
- **GitHub Models**: Бесплатный доступ к моделям ИИ для прототипирования и обучения (без необходимости кредитной карты)
- **Инструменты разработки**: Конфигурация Docker-контейнеров, VS Code и GitHub Codespaces
- **[→ Начать главу 2](./02-SetupDevEnvironment/README.md)**

### **Глава 3: Основные техники генеративного ИИ**
- **Инженерия запросов**: Техники для получения оптимальных ответов от моделей ИИ
- **Эмбеддинги и операции с векторами**: Реализация семантического поиска и сопоставления по схожести
- **Генерация с дополнением извлечений (RAG)**: Комбинирование ИИ с вашими собственными источниками данных
- **Вызов функций**: Расширение возможностей ИИ с помощью пользовательских инструментов и плагинов
- **[→ Начать главу 3](./03-CoreGenerativeAITechniques/README.md)**

### **Глава 4: Практическое применение и проекты**
- **Генератор историй о питомцах** (`petstory/`): Создание креативного контента с использованием GitHub Models
- **Локальная демонстрация Foundry** (`foundrylocal/`): Интеграция локальных моделей ИИ с OpenAI Java SDK
- **Сервис калькулятора MCP** (`calculator/`): Базовая реализация протокола контекста модели с Spring AI
- **[→ Начать главу 4](./04-PracticalSamples/README.md)**

### **Глава 5: Разработка ответственного ИИ**
- **Безопасность моделей GitHub**: Тестирование встроенной фильтрации контента и механизмов безопасности (жесткие блокировки и мягкие отказы)
- **Демонстрация ответственного ИИ**: Практический пример работы современных систем безопасности ИИ
- **Лучшие практики**: Основные рекомендации для этичной разработки и развертывания ИИ
- **[→ Начать главу 5](./05-ResponsibleGenAI/README.md)**

## Дополнительные ресурсы

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

**Отказ от ответственности**:  
Этот документ был переведен с использованием сервиса автоматического перевода [Co-op Translator](https://github.com/Azure/co-op-translator). Хотя мы стремимся к точности, пожалуйста, имейте в виду, что автоматические переводы могут содержать ошибки или неточности. Оригинальный документ на его исходном языке следует считать авторитетным источником. Для получения критически важной информации рекомендуется профессиональный перевод человеком. Мы не несем ответственности за любые недоразумения или неправильные интерпретации, возникшие в результате использования данного перевода.