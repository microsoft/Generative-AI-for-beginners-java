<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T16:22:03+00:00",
  "source_file": "README.md",
  "language_code": "sr"
}
-->
# Генеративна вештачка интелигенција за почетнике - Јава издање
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Генеративна вештачка интелигенција за почетнике - Јава издање](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.sr.png)

**Време потребно**: Целокупна радионица може се завршити онлајн без локалног подешавања. Постављање окружења траје 2 минута, док истраживање примера захтева 1-3 сата, у зависности од дубине истраживања.

> **Брзи почетак**

1. Форкујте овај репозиторијум на свој GitHub налог
2. Кликните **Code** → **Codespaces** таб → **...** → **New with options...**
3. Користите подразумеване опције – ово ће изабрати развојни контејнер креиран за овај курс
4. Кликните **Create codespace**
5. Сачекајте ~2 минута да окружење буде спремно
6. Пређите директно на [Први пример](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Подршка за више језика

### Подржано преко GitHub Action (аутоматски и увек ажурирано)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](./README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## Структура курса и пут учења

### **Поглавље 1: Увод у генеративну вештачку интелигенцију**
- **Основни концепти**: Разумевање великих језичких модела, токена, уградњи и могућности вештачке интелигенције
- **Јава AI екосистем**: Преглед Spring AI и OpenAI SDK-ова
- **Протокол контекста модела**: Увод у MCP и његову улогу у комуникацији AI агената
- **Практичне примене**: Сценарији из стварног света, укључујући четботове и генерисање садржаја
- **[→ Почните Поглавље 1](./01-IntroToGenAI/README.md)**

### **Поглавље 2: Постављање развојног окружења**
- **Конфигурација за више провајдера**: Поставите GitHub моделе, Azure OpenAI и OpenAI Java SDK интеграције
- **Spring Boot + Spring AI**: Најбоље праксе за развој AI апликација за предузећа
- **GitHub модели**: Бесплатан приступ AI моделима за прототипе и учење (без потребе за кредитном картицом)
- **Развојни алати**: Docker контејнери, VS Code и GitHub Codespaces конфигурација
- **[→ Почните Поглавље 2](./02-SetupDevEnvironment/README.md)**

### **Поглавље 3: Основне технике генеративне вештачке интелигенције**
- **Инжењеринг упита**: Технике за оптималне одговоре AI модела
- **Уградње и векторске операције**: Имплементација семантичке претраге и упоређивања сличности
- **Генерисање уз помоћ претраживања (RAG)**: Комбинујте AI са сопственим изворима података
- **Позивање функција**: Проширите могућности AI уз прилагођене алате и додатке
- **[→ Почните Поглавље 3](./03-CoreGenerativeAITechniques/README.md)**

### **Поглавље 4: Практичне примене и пројекти**
- **Генератор прича о кућним љубимцима** (`petstory/`): Креативно генерисање садржаја уз GitHub моделе
- **Foundry локална демонстрација** (`foundrylocal/`): Локална интеграција AI модела уз OpenAI Java SDK
- **MCP услуга калкулатора** (`calculator/`): Основна имплементација протокола контекста модела уз Spring AI
- **[→ Почните Поглавље 4](./04-PracticalSamples/README.md)**

### **Поглавље 5: Одговорни развој вештачке интелигенције**
- **Безбедност GitHub модела**: Тестирајте уграђено филтрирање садржаја и механизме безбедности (тврде блокаде и меко одбијање)
- **Демонстрација одговорне AI**: Практичан пример који показује како савремени AI системи безбедности функционишу
- **Најбоље праксе**: Основне смернице за етички развој и примену AI
- **[→ Почните Поглавље 5](./05-ResponsibleGenAI/README.md)**

## Додатни ресурси

- [AI агенти за почетнике](https://github.com/microsoft/ai-agents-for-beginners)
- [Генеративна вештачка интелигенција за почетнике уз .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Генеративна вештачка интелигенција за почетнике уз JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Генеративна вештачка интелигенција за почетнике](https://github.com/microsoft/generative-ai-for-beginners)
- [Машинско учење за почетнике](https://aka.ms/ml-beginners)
- [Наука о подацима за почетнике](https://aka.ms/datascience-beginners)
- [Вештачка интелигенција за почетнике](https://aka.ms/ai-beginners)
- [Сајбер безбедност за почетнике](https://github.com/microsoft/Security-101)
- [Веб развој за почетнике](https://aka.ms/webdev-beginners)
- [IoT за почетнике](https://aka.ms/iot-beginners)
- [XR развој за почетнике](https://github.com/microsoft/xr-development-for-beginners)
- [Мастеринг GitHub Copilot за AI парно програмирање](https://aka.ms/GitHubCopilotAI)
- [Мастеринг GitHub Copilot за C#/.NET програмере](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Изаберите своју Copilot авантуру](https://github.com/microsoft/CopilotAdventures)
- [RAG чет апликација са Azure AI услугама](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Одрицање од одговорности**:  
Овај документ је преведен коришћењем услуге за превођење помоћу вештачке интелигенције [Co-op Translator](https://github.com/Azure/co-op-translator). Иако се трудимо да обезбедимо тачност, молимо вас да имате у виду да аутоматски преводи могу садржати грешке или нетачности. Оригинални документ на изворном језику треба сматрати ауторитативним извором. За критичне информације препоручује се професионални превод од стране људи. Не сносимо одговорност за било каква погрешна тумачења или неспоразуме који могу произаћи из коришћења овог превода.