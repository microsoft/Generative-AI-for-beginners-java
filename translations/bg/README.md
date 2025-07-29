<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T16:18:23+00:00",
  "source_file": "README.md",
  "language_code": "bg"
}
-->
# Генеративен AI за начинаещи - Java издание
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Генеративен AI за начинаещи - Java издание](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.bg.png)

**Време за изпълнение**: Целият уъркшоп може да бъде завършен онлайн без локална настройка. Настройката на средата отнема 2 минути, а изследването на примерите изисква 1-3 часа в зависимост от дълбочината на проучването.

> **Бърз старт**

1. Направете fork на това хранилище във вашия GitHub акаунт
2. Кликнете **Code** → **Codespaces** таб → **...** → **New with options...**
3. Използвайте настройките по подразбиране – това ще избере контейнера за разработка, създаден за този курс
4. Кликнете **Create codespace**
5. Изчакайте ~2 минути, докато средата бъде готова
6. Започнете директно с [Първия пример](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Поддръжка на много езици

### Поддържани чрез GitHub Action (Автоматизирано и винаги актуално)

[Френски](../fr/README.md) | [Испански](../es/README.md) | [Немски](../de/README.md) | [Руски](../ru/README.md) | [Арабски](../ar/README.md) | [Персийски (Фарси)](../fa/README.md) | [Урду](../ur/README.md) | [Китайски (опростен)](../zh/README.md) | [Китайски (традиционен, Макао)](../mo/README.md) | [Китайски (традиционен, Хонг Конг)](../hk/README.md) | [Китайски (традиционен, Тайван)](../tw/README.md) | [Японски](../ja/README.md) | [Корейски](../ko/README.md) | [Хинди](../hi/README.md) | [Бенгалски](../bn/README.md) | [Марати](../mr/README.md) | [Непалски](../ne/README.md) | [Пенджабски (Гурмукхи)](../pa/README.md) | [Португалски (Португалия)](../pt/README.md) | [Португалски (Бразилия)](../br/README.md) | [Италиански](../it/README.md) | [Полски](../pl/README.md) | [Турски](../tr/README.md) | [Гръцки](../el/README.md) | [Тайландски](../th/README.md) | [Шведски](../sv/README.md) | [Датски](../da/README.md) | [Норвежки](../no/README.md) | [Фински](../fi/README.md) | [Холандски](../nl/README.md) | [Иврит](../he/README.md) | [Виетнамски](../vi/README.md) | [Индонезийски](../id/README.md) | [Малайски](../ms/README.md) | [Тагалог (Филипински)](../tl/README.md) | [Суахили](../sw/README.md) | [Унгарски](../hu/README.md) | [Чешки](../cs/README.md) | [Словашки](../sk/README.md) | [Румънски](../ro/README.md) | [Български](./README.md) | [Сръбски (Кирилица)](../sr/README.md) | [Хърватски](../hr/README.md) | [Словенски](../sl/README.md) | [Украински](../uk/README.md) | [Бирмански (Мианмар)](../my/README.md)

## Структура на курса и учебен път

### **Глава 1: Въведение в Генеративния AI**
- **Основни концепции**: Разбиране на големите езикови модели, токени, вграждания и AI възможности
- **Java AI екосистема**: Преглед на Spring AI и OpenAI SDKs
- **Протокол за контекст на модела**: Въведение в MCP и неговата роля в комуникацията на AI агентите
- **Практически приложения**: Реални сценарии, включително чатботове и генериране на съдържание
- **[→ Започнете Глава 1](./01-IntroToGenAI/README.md)**

### **Глава 2: Настройка на среда за разработка**
- **Конфигурация за множество доставчици**: Настройка на GitHub Models, Azure OpenAI и OpenAI Java SDK интеграции
- **Spring Boot + Spring AI**: Най-добри практики за разработка на корпоративни AI приложения
- **GitHub Models**: Безплатен достъп до AI модели за прототипиране и обучение (без необходимост от кредитна карта)
- **Инструменти за разработка**: Docker контейнери, VS Code и GitHub Codespaces конфигурация
- **[→ Започнете Глава 2](./02-SetupDevEnvironment/README.md)**

### **Глава 3: Основни техники за Генеративен AI**
- **Инженеринг на подканите**: Техники за оптимални отговори от AI моделите
- **Вграждания и векторни операции**: Имплементация на семантично търсене и съвпадение на сходства
- **Генерация, обогатена с извличане (RAG)**: Комбиниране на AI с ваши собствени източници на данни
- **Извикване на функции**: Разширяване на AI възможностите с персонализирани инструменти и плъгини
- **[→ Започнете Глава 3](./03-CoreGenerativeAITechniques/README.md)**

### **Глава 4: Практически приложения и проекти**
- **Генератор на истории за домашни любимци** (`petstory/`): Генериране на креативно съдържание с GitHub Models
- **Локална демонстрация на Foundry** (`foundrylocal/`): Локална интеграция на AI модели с OpenAI Java SDK
- **Калкулатор на MCP** (`calculator/`): Основна имплементация на Протокол за контекст на модела със Spring AI
- **[→ Започнете Глава 4](./04-PracticalSamples/README.md)**

### **Глава 5: Отговорна разработка на AI**
- **Безопасност на GitHub Models**: Тестване на вградени механизми за филтриране на съдържание и безопасност (твърди блокировки и меки откази)
- **Демонстрация на отговорен AI**: Практически пример за работа на съвременни AI системи за безопасност
- **Най-добри практики**: Основни насоки за етично разработване и внедряване на AI
- **[→ Започнете Глава 5](./05-ResponsibleGenAI/README.md)**

## Допълнителни ресурси

- [AI агенти за начинаещи](https://github.com/microsoft/ai-agents-for-beginners)
- [Генеративен AI за начинаещи с .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Генеративен AI за начинаещи с JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Генеративен AI за начинаещи](https://github.com/microsoft/generative-ai-for-beginners)
- [Машинно обучение за начинаещи](https://aka.ms/ml-beginners)
- [Наука за данни за начинаещи](https://aka.ms/datascience-beginners)
- [AI за начинаещи](https://aka.ms/ai-beginners)
- [Киберсигурност за начинаещи](https://github.com/microsoft/Security-101)
- [Уеб разработка за начинаещи](https://aka.ms/webdev-beginners)
- [IoT за начинаещи](https://aka.ms/iot-beginners)
- [XR разработка за начинаещи](https://github.com/microsoft/xr-development-for-beginners)
- [Майсторство на GitHub Copilot за AI програмиране в екип](https://aka.ms/GitHubCopilotAI)
- [Майсторство на GitHub Copilot за C#/.NET разработчици](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Изберете своето приключение с Copilot](https://github.com/microsoft/CopilotAdventures)
- [RAG чат приложение с Azure AI услуги](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Отказ от отговорност**:  
Този документ е преведен с помощта на AI услуга за превод [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, моля, имайте предвид, че автоматизираните преводи може да съдържат грешки или неточности. Оригиналният документ на неговия роден език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Не носим отговорност за недоразумения или погрешни интерпретации, произтичащи от използването на този превод.