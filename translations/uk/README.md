<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T07:01:19+00:00",
  "source_file": "README.md",
  "language_code": "uk"
}
-->
# Генеративний ШІ для початківців — видання Java
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Генеративний ШІ для початківців — видання Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.uk.png)

**Часові витрати**: Весь семінар можна пройти онлайн без локального налаштування. Налаштування середовища займає 2 хвилини, а ознайомлення з прикладами вимагає 1–3 години залежно від глибини вивчення.

> **Швидкий старт** 
> 
> 1. Відфоркніть цей репозиторій у свій обліковий запис GitHub
> 2. Натисніть **Code** → вкладку **Codespaces** → **...** → **New with options...**
> 3. Використовуйте значення за замовчуванням – це вибере Development container, створений для цього курсу
> 4. Натисніть **Create codespace**
> 5. Зачекайте приблизно 2 хвилини, доки середовище буде готове
> 6. Перейдіть безпосередньо до [першого прикладу](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Віддаєте перевагу клонуванню локально?**
>
> Цей репозиторій містить понад 50 перекладів мовами, що значно збільшує розмір завантаження. Щоб клонувати без перекладів, використовуйте sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Це дає вам усе необхідне для проходження курсу з набагато швидшим завантаженням.


## Підтримка кількох мов

### Підтримується через GitHub Action (автоматично та завжди актуально)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Арабська](../ar/README.md) | [Бенгальська](../bn/README.md) | [Болгарська](../bg/README.md) | [Бірманська (М'янма)](../my/README.md) | [Китайська (спрощена)](../zh/README.md) | [Китайська (традиційна, Гонконг)](../hk/README.md) | [Китайська (традиційна, Макао)](../mo/README.md) | [Китайська (традиційна, Тайвань)](../tw/README.md) | [Хорватська](../hr/README.md) | [Чеська](../cs/README.md) | [Данська](../da/README.md) | [Нідерландська](../nl/README.md) | [Естонська](../et/README.md) | [Фінська](../fi/README.md) | [Французька](../fr/README.md) | [Німецька](../de/README.md) | [Грецька](../el/README.md) | [Іврит](../he/README.md) | [Гінді](../hi/README.md) | [Угорська](../hu/README.md) | [Індонезійська](../id/README.md) | [Італійська](../it/README.md) | [Японська](../ja/README.md) | [Каннада](../kn/README.md) | [Корейська](../ko/README.md) | [Литовська](../lt/README.md) | [Малайська](../ms/README.md) | [Малаялам](../ml/README.md) | [Маратхі](../mr/README.md) | [Непальська](../ne/README.md) | [Нігерійський піджин](../pcm/README.md) | [Норвезька](../no/README.md) | [Перська (фарсі)](../fa/README.md) | [Польська](../pl/README.md) | [Португальська (Бразилія)](../br/README.md) | [Португальська (Португалія)](../pt/README.md) | [Пенджабі (гурмукхі)](../pa/README.md) | [Румунська](../ro/README.md) | [Російська](../ru/README.md) | [Сербська (кирилиця)](../sr/README.md) | [Словацька](../sk/README.md) | [Словенська](../sl/README.md) | [Іспанська](../es/README.md) | [Свахілі](../sw/README.md) | [Шведська](../sv/README.md) | [Тагальська (філіппінська)](../tl/README.md) | [Тамільська](../ta/README.md) | [Телугу](../te/README.md) | [Тайська](../th/README.md) | [Турецька](../tr/README.md) | [Українська](./README.md) | [Урду](../ur/README.md) | [В’єтнамська](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Структура курсу та шлях навчання

### **Розділ 1: Вступ до генеративного ШІ**
- **Ключові поняття**: Розуміння великих мовних моделей, токенів, ембедингів та можливостей ШІ
- **Екосистема ШІ для Java**: Огляд Spring AI та OpenAI SDK
- **Протокол контексту моделі**: Вступ до MCP та його роль у взаємодії агентів ШІ
- **Практичні застосування**: Сценарії з реального життя, включаючи чат-ботів та генерацію контенту
- **[→ Почати Розділ 1](./01-IntroToGenAI/README.md)**

### **Розділ 2: Налаштування середовища розробки**
- **Конфігурація з кількома постачальниками**: Налаштуйте інтеграції GitHub Models, Azure OpenAI та OpenAI Java SDK
- **Spring Boot + Spring AI**: Кращі практики для розробки корпоративних застосунків ШІ
- **GitHub Models**: Безкоштовний доступ до моделей ШІ для прототипування та навчання (не потрібна кредитна картка)
- **Інструменти розробки**: Docker-контейнери, VS Code та налаштування GitHub Codespaces
- **[→ Почати Розділ 2](./02-SetupDevEnvironment/README.md)**

### **Розділ 3: Основні методи генеративного ШІ**
- **Проєктування підказок (Prompt Engineering)**: Техніки для отримання оптимальних відповідей від моделей ШІ
- **Ембедінги та векторні операції**: Реалізуйте семантичний пошук і порівняння схожості
- **Retrieval-Augmented Generation (RAG)**: Поєднання ШІ з вашими власними джерелами даних
- **Виклик функцій**: Розширте можливості ШІ за допомогою власних інструментів і плагінів
- **[→ Почати Розділ 3](./03-CoreGenerativeAITechniques/README.md)**

### **Розділ 4: Практичні застосування та проекти**
- **Генератор історій про домашніх улюбленців** (`petstory/`): Креативна генерація контенту з GitHub Models
- **Демо локального Foundry** (`foundrylocal/`): Інтеграція локальної моделі ШІ з OpenAI Java SDK
- **Сервіс калькулятора MCP** (`calculator/`): Базова реалізація Model Context Protocol з Spring AI
- **[→ Почати Розділ 4](./04-PracticalSamples/README.md)**

### **Розділ 5: Відповідальна розробка ШІ**
- **Безпека GitHub Models**: Перевірка вбудованих механізмів фільтрації контенту та заходів безпеки (жорсткі блокування та м’які відмови)
- **Демо відповідального ШІ**: Практичний приклад, який демонструє, як працюють сучасні системи безпеки ШІ
- **Найкращі практики**: Основні рекомендації для етичної розробки та впровадження ШІ
- **[→ Почати Розділ 5](./05-ResponsibleGenAI/README.md)**

## Додаткові ресурси

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j для початківців](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js для початківців](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Агенти
[![AZD для початківців](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI для початківців](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP для початківців](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Агенти ШІ для початківців](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серія Генеративного ШІ
[![Генеративний ШІ для початківців](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Генеративний ШІ (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Генеративний ШІ (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Генеративний ШІ (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Основні курси
[![Машинне навчання для початківців](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Наука про дані для початківців](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![ШІ для початківців](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Кібербезпека для початківців](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Веб-розробка для початківців](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT для початківців](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Розробка XR для початківців](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серія Copilot
[![Copilot для парного програмування зі ШІ](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot для C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Пригоди Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Отримання допомоги

Якщо ви застрягли або маєте будь-які питання щодо створення додатків зі ШІ, приєднуйтеся до інших учнів та досвідчених розробників у обговореннях про MCP. Це підтримуюча спільнота, де вітаються питання, а знання вільно передаються.

[![Discord Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Якщо у вас є відгуки щодо продукту або ви виявили помилки під час розробки, відвідайте:

[![Форум розробників Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ був перекладений із використанням сервісу перекладу штучного інтелекту [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, зверніть увагу, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ його рідною мовою слід вважати авторитетним джерелом. Для критичної інформації рекомендовано скористатися послугами професійного перекладача. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->