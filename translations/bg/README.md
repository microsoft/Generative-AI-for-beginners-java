<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:44:33+00:00",
  "source_file": "README.md",
  "language_code": "bg"
}
-->
# Генеративен AI за начинаещи - Java издание
[![Discord на Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Генеративен AI за начинаещи - Java издание](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.bg.png)

**Необходимо време**: Целият уъркшоп може да бъде завършен онлайн без локална настройка. Настройката на средата отнема 2 минути, като разглеждането на примерите изисква между 1 и 3 часа в зависимост от дълбочината на изследване.

> **Бърз старт** 

1. Форкнете това хранилище в своя GitHub акаунт
2. Кликнете **Code** → **Codespaces** таб → **...** → **New with options...**
3. Използвайте подразбиращите се настройки – това ще избере Development container, създаден за този курс
4. Кликнете **Create codespace**
5. Изчакайте ~2 минути средата да бъде готова
6. Преминете директно към [Първия пример](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Предпочитате да клонирате локално?**
>
> Това хранилище съдържа над 50 превода на езици, което значително увеличава размера за изтегляне. За да клонирате без преводите, използвайте sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Това ви дава всичко необходимо за завършване на курса с много по-бързо изтегляне.


## Поддръжка на много езици

### Поддържа се чрез GitHub Action (Автоматизирано и винаги актуално)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Арабски](../ar/README.md) | [Бенгалски](../bn/README.md) | [Български](./README.md) | [Бирмански (Мианмар)](../my/README.md) | [Китайски (опростен)](../zh/README.md) | [Китайски (традиционен, Хонконг)](../hk/README.md) | [Китайски (традиционен, Макао)](../mo/README.md) | [Китайски (традиционен, Тайван)](../tw/README.md) | [Хърватски](../hr/README.md) | [Чешки](../cs/README.md) | [Датски](../da/README.md) | [Холандски](../nl/README.md) | [Естонски](../et/README.md) | [Финландски](../fi/README.md) | [Френски](../fr/README.md) | [Немски](../de/README.md) | [Гръцки](../el/README.md) | [Иврит](../he/README.md) | [Хинди](../hi/README.md) | [Унгарски](../hu/README.md) | [Индонезийски](../id/README.md) | [Италиански](../it/README.md) | [Японски](../ja/README.md) | [Каннада](../kn/README.md) | [Корейски](../ko/README.md) | [Литовски](../lt/README.md) | [Малайски](../ms/README.md) | [Малаялам](../ml/README.md) | [Маратхи](../mr/README.md) | [Непалски](../ne/README.md) | [Нигерийски Пиджин](../pcm/README.md) | [Норвежки](../no/README.md) | [Персийски (Фарси)](../fa/README.md) | [Полски](../pl/README.md) | [Португалски (Бразилия)](../br/README.md) | [Португалски (Португалия)](../pt/README.md) | [Пенджаби (Гурмухи)](../pa/README.md) | [Румънски](../ro/README.md) | [Руски](../ru/README.md) | [Сърбски (кирилица)](../sr/README.md) | [Словашки](../sk/README.md) | [Словенски](../sl/README.md) | [Испански](../es/README.md) | [Свахили](../sw/README.md) | [Шведски](../sv/README.md) | [Тагалог (Филипински)](../tl/README.md) | [Тамилски](../ta/README.md) | [Телугу](../te/README.md) | [Тайски](../th/README.md) | [Турски](../tr/README.md) | [Украински](../uk/README.md) | [Урду](../ur/README.md) | [Виетнамски](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Структура на курса и учебен път

### **Глава 1: Въведение в генеративния AI**
- **Основни понятия**: Разбиране на големи езикови модели, токени, вграждания и възможностите на AI
- **Java AI екосистема**: Преглед на Spring AI и OpenAI SDKs
- **Model Context Protocol**: Въведение в MCP и неговата роля в комуникацията между AI агенти
- **Практически приложения**: Реални сценарии като чатботове и генериране на съдържание
- **[→ Започнете Глава 1](./01-IntroToGenAI/README.md)**

### **Глава 2: Настройка на средата за разработка**
- **Конфигурация с няколко доставчика**: Настройка на GitHub Models, Azure OpenAI и интеграции с OpenAI Java SDK
- **Spring Boot + Spring AI**: Най-добри практики за разработка на корпоративни AI приложения
- **GitHub Models**: Безплатен достъп до AI модели за прототипиране и обучение (не е необходима кредитна карта)
- **Инструменти за разработка**: Docker контейнери, VS Code и конфигурация на GitHub Codespaces
- **[→ Започнете Глава 2](./02-SetupDevEnvironment/README.md)**

### **Глава 3: Основни техники за генеративен AI**
- **Проектиране на подсказки**: Техники за оптимални отговори от AI моделите
- **Вграждания и векторни операции**: Имплементиране на семантично търсене и съвпадение по сходство
- **Извличане-подпомогнато генериране (RAG)**: Комбиниране на AI с вашите собствени източници на данни
- **Извикване на функции**: Разширяване на възможностите на AI с персонализирани инструменти и плъгини
- **[→ Започнете Глава 3](./03-CoreGenerativeAITechniques/README.md)**

### **Глава 4: Практически приложения и проекти**
- **Генератор на истории за домашни любимци** (`petstory/`): Креативно генериране на съдържание с GitHub Models
- **Foundry локално демо** (`foundrylocal/`): Локална интеграция на AI модел с OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): Базова имплементация на Model Context Protocol със Spring AI
- **[→ Започнете Глава 4](./04-PracticalSamples/README.md)**

### **Глава 5: Отговорно разработване на AI**
- **Сигурност на GitHub Models**: Тествайте вграденото филтриране на съдържание и механизми за безопасност (строги блокирания и меки откази)
- **Демо за отговорен AI**: Практически пример, показващ как съвременните системи за безопасност на AI работят на практика
- **Най-добри практики**: Основни насоки за етично разработване и внедряване на AI
- **[→ Започнете Глава 5](./05-ResponsibleGenAI/README.md)**

## Допълнителни ресурси

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j за начинаещи](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js за начинаещи](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Агенти
[![AZD за начинаещи](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI за начинаещи](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP за начинаещи](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents за начинаещи](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серия за генеративен AI
[![Генеративен AI за начинаещи](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Генеративен AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Генеративен AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Генеративен AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Основно обучение
[![ML за начинаещи](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Наука за данни за начинаещи](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI за начинаещи](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Киберсигурност за начинаещи](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Уеб разработка за начинаещи](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT за начинаещи](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR разработка за начинаещи](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серия Copilot
[![Copilot за съвместно програмиране с AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot за C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Приключение с Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Получаване на помощ

Ако се затрудните или имате въпроси относно изграждането на AI приложения. Присъединете се към други учащи се и опитни разработчици в дискусиите за MCP. Това е подкрепяща общност, където въпросите са добре дошли и знанието се споделя свободно.

[![Discord на Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Ако имате обратна връзка за продукта или срещнете грешки по време на разработката, посетете:

[![Форум на разработчиците на Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Отказ от отговорност:
Този документ е преведен с помощта на услуга за превод, използваща изкуствен интелект [Co-op Translator](https://github.com/Azure/co-op-translator). Въпреки че се стремим към точност, имайте предвид, че автоматичните преводи могат да съдържат грешки или неточности. Оригиналният документ на съответния език трябва да се счита за авторитетен източник. За критична информация се препоръчва професионален човешки превод. Не носим отговорност за каквито и да е недоразумения или погрешни тълкувания, произтичащи от използването на този превод.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->