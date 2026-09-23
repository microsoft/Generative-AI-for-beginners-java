# Генеративний ШІ для Початківців – Java Версія
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Генеративний ШІ для Початківців – Java Версія](../../translated_images/uk/beg-genai-series.8b48be9951cc574c.webp)

**Часові Витрати**: Весь воркшоп можна пройти онлайн без локального налаштування. Налаштування середовища займає 2 хвилини, а дослідження прикладів – від 1 до 3 годин залежно від глибини вивчення.

> **Швидкий Старт** 

1. Зробіть форк цього репозиторію у своєму обліковому записі GitHub
2. Натисніть **Code** → вкладка **Codespaces** → **...** → **New with options...**
3. Використовуйте налаштування за замовчуванням – це вибере контейнер для розробки, створений для цього курсу
4. Натисніть **Create codespace**
5. Дочекайтеся приблизно 2 хвилин для готовності середовища
6. Перейдіть безпосередньо до [Розділу 2: Provision Azure AI Foundry](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## Підтримка Багатьох Мов

### Підтримка через GitHub Action (Автоматично та Завжди Оновлено)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Арабська](../ar/README.md) | [Бенгалі](../bn/README.md) | [Болгарська](../bg/README.md) | [Бірманська (М’янма)](../my/README.md) | [Китайська (спрощена)](../zh-CN/README.md) | [Китайська (традиційна, Гонконг)](../zh-HK/README.md) | [Китайська (традиційна, Макао)](../zh-MO/README.md) | [Китайська (традиційна, Тайвань)](../zh-TW/README.md) | [Хорватська](../hr/README.md) | [Чеська](../cs/README.md) | [Данська](../da/README.md) | [Голландська](../nl/README.md) | [Естонська](../et/README.md) | [Фінська](../fi/README.md) | [Французька](../fr/README.md) | [Німецька](../de/README.md) | [Грецька](../el/README.md) | [Іврит](../he/README.md) | [Гінді](../hi/README.md) | [Угорська](../hu/README.md) | [Індонезійська](../id/README.md) | [Італійська](../it/README.md) | [Японська](../ja/README.md) | [Каннада](../kn/README.md) | [Кхмер](../km/README.md) | [Корейська](../ko/README.md) | [Литовська](../lt/README.md) | [Малайська](../ms/README.md) | [Малаялам](../ml/README.md) | [Маратхі](../mr/README.md) | [Непальська](../ne/README.md) | [Нігерійський Піджин](../pcm/README.md) | [Норвезька](../no/README.md) | [Перська (Фарсі)](../fa/README.md) | [Польська](../pl/README.md) | [Португальська (Бразилія)](../pt-BR/README.md) | [Португальська (Португалія)](../pt-PT/README.md) | [Пенджабі (Гурмукхі)](../pa/README.md) | [Румунська](../ro/README.md) | [Російська](../ru/README.md) | [Сербська (кирилиця)](../sr/README.md) | [Словацька](../sk/README.md) | [Словенська](../sl/README.md) | [Іспанська](../es/README.md) | [Свахілі](../sw/README.md) | [Шведська](../sv/README.md) | [Тагалог (Філіппінська)](../tl/README.md) | [Тамільська](../ta/README.md) | [Телугу](../te/README.md) | [Тайська](../th/README.md) | [Турецька](../tr/README.md) | [Українська](./README.md) | [Урду](../ur/README.md) | [В’єтнамська](../vi/README.md)

> **Віддаєте перевагу клонувати локально?**
>
> Цей репозиторій містить понад 50 мовних перекладів, що значно збільшує розмір завантаження. Щоб клонувати без перекладів, використовуйте sparse checkout:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> Це дасть вам усе необхідне для проходження курсу з набагато швидшим завантаженням.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Структура Курсу та Навчальний Шлях

### **Розділ 1: Вступ до Генеративного ШІ**
- **Основні Концепти**: Розуміння великих мовних моделей, токенів, ембеддингів та можливостей ШІ
- **Екосистема Java AI**: Огляд Spring AI та OpenAI SDK
- **Протокол Контексту Моделі**: Вступ до MCP та його роль у спілкуванні агентів ШІ
- **Практичне Застосування**: Реальні сценарії, включаючи чатботів і генерацію контенту
- **[→ Почати Розділ 1](./01-IntroToGenAI/README.md)**

### **Розділ 2: Налаштування Середовища Розробки**
- **Azure AI Foundry**: Підготовка чат-бота GPT-5.6 Luna і ембеддингів text-embedding-3-small за допомогою Bicep та Azure Developer CLI (azd)
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: Навчання з `ChatClient`, підтримуваним офіційним OpenAI Java SDK та Azure OpenAI v1
- **Аутентифікація без Ключа**: Безпечне підключення через Microsoft Entra ID — без необхідності керувати API ключами
- **Інструменти Розробки**: Docker контейнери, VS Code, та конфігурація GitHub Codespaces
- **[→ Почати Розділ 2](./02-SetupDevEnvironment/README.md)**

### **Розділ 3: Основні Техніки Генеративного ШІ**
- **Офіційний OpenAI Java SDK**: Виклик Azure OpenAI v1 безпосередньо з аутентифікацією без ключа
- **Інженерія Запитів**: Техніки для оптимальних відповідей моделі ШІ
- **Ембеддинги та Векторні Операції**: Реалізація семантичного пошуку та пошуку схожості
- **Retrieval-Augmented Generation (RAG)**: Поєднання ШІ з вашими джерелами даних
- **Виклики Функцій**: Розширення можливостей ШІ за допомогою власних інструментів і плагінів
- **[→ Почати Розділ 3](./03-CoreGenerativeAITechniques/README.md)**

### **Розділ 4: Практичні Застосування та Проєкти**
- **Генератор Історій про Тварин** (`petstory/`): Креативне генерування контенту з Azure AI Foundry
- **Foundry Local Demo** (`foundrylocal/`): Локальна інтеграція ШІ-моделі з OpenAI Java SDK
- **Сервіс Калькулятора MCP** (`calculator/`): Базова реалізація Протоколу Контексту Моделі з Spring AI
- **[→ Почати Розділ 4](./04-PracticalSamples/README.md)**

### **Розділ 5: Відповідальна Розробка ШІ**
- **Безпека Контенту в Azure AI Foundry**: Тестування вбудованих механізмів фільтрації і безпеки контенту (жорсткий блок та м’які відмови)
- **Демонстрація Відповідальної Розробки ШІ**: Практичний приклад роботи сучасних систем безпеки ШІ
- **Найкращі Практики**: Основні рекомендації для етичної розробки та впровадження ШІ
- **[→ Почати Розділ 5](./05-ResponsibleGenAI/README.md)**

## Додаткові Ресурси

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j для Початківців](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js для Початківців](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain для Початківців](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Агенти
[![AZD для Початківців](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI для Початківців](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP для Початківців](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Агенти для Початківців](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серія про Генеративний ШІ
[![Генеративний ШІ для Початківців](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Генеративний ШІ (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Генеративний ШІ (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Генеративний ШІ (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Основне Навчання
[![Машинне Навчання для Початківців](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Наука про Дані для Початківців](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![ШІ для Початківців](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Кібербезпека для Початківців](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Веб-Розробка для Початківців](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT для Початківців](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Розробка для Початківців](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серія Copilot
[![Copilot для Парного Програмування з ШІ](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot для C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Пригоди Copilot](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Отримання Допомоги

Якщо ви застрягли або маєте питання щодо створення AI-додатків, приєднуйтесь до спільноти учнів і досвідчених розробників для обговорень MCP. Це підтримуюча спільнота, де питання вітаються, а знання вільно обмінюються.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Якщо у вас є відгуки про продукт або ви зіткнулися з помилками під час розробки, відвідайте:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Відмова від відповідальності**:
Цей документ було перекладено за допомогою сервісу штучного інтелекту для перекладу [Co-op Translator](https://github.com/Azure/co-op-translator). Хоча ми прагнемо до точності, будь ласка, майте на увазі, що автоматичні переклади можуть містити помилки або неточності. Оригінальний документ рідною мовою слід вважати авторитетним джерелом. Для критично важливої інформації рекомендується професійний людський переклад. Ми не несемо відповідальності за будь-які непорозуміння або неправильні тлумачення, що виникли внаслідок використання цього перекладу.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->