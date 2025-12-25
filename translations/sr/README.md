<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T06:48:19+00:00",
  "source_file": "README.md",
  "language_code": "sr"
}
-->
# Генеративна вештачка интелигенција за почетнике - Java издање
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Генеративна вештачка интелигенција за почетнике - Java издање](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.sr.png)

**Време потребно**: Цео курс се може завршити онлајн без локалне конфигурације. Подешавање окружења траје 2 минута, а истраживање примера захтева 1–3 сата у зависности од дубине истраживања.

> **Брзи старт** 

1. Форкујте овај репозиторијум на ваш GitHub налог
2. Кликните **Code** → **Codespaces** таб → **...** → **New with options...**
3. Користите подразумеване поставке – ово ће одабрати Development container креиран за овај курс
4. Кликните **Create codespace**
5. Сачекајте ~2 минута да средина буде спремна
6. Прескочите директно на [Први пример](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Желите да клонирате локално?**
>
> Овај репозиторијум укључује преко 50 превода на језике што значајно повећава величину преузимања. Да бисте клонирали без превода, користите sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Ово вам даје све што вам треба да завршите курс уз знатно брже преузимање.


## Подршка за више језика

### Подржано преко GitHub Action (Аутоматизовано и увек ажурно)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Арапски](../ar/README.md) | [Бенгалски](../bn/README.md) | [Бугарски](../bg/README.md) | [Бирмански (Мјанмар)](../my/README.md) | [Кинески (поједностављени)](../zh/README.md) | [Кинески (традиционални, Хонг Конг)](../hk/README.md) | [Кинески (традиционални, Макао)](../mo/README.md) | [Кинески (традиционални, Тајван)](../tw/README.md) | [Хрватски](../hr/README.md) | [Чешки](../cs/README.md) | [Дански](../da/README.md) | [Холандски](../nl/README.md) | [Естонски](../et/README.md) | [Фински](../fi/README.md) | [Француски](../fr/README.md) | [Немачки](../de/README.md) | [Грчки](../el/README.md) | [Хебрејски](../he/README.md) | [Хинди](../hi/README.md) | [Мађарски](../hu/README.md) | [Индонежански](../id/README.md) | [Италијански](../it/README.md) | [Јапански](../ja/README.md) | [Каннада](../kn/README.md) | [Корејски](../ko/README.md) | [Литвански](../lt/README.md) | [Малајски](../ms/README.md) | [Малајалам](../ml/README.md) | [Маратхи](../mr/README.md) | [Непали](../ne/README.md) | [Нигеријски пидгин](../pcm/README.md) | [Норвешки](../no/README.md) | [Персијски (Фарси)](../fa/README.md) | [Пољски](../pl/README.md) | [Португалски (Бразил)](../br/README.md) | [Португалски (Португал)](../pt/README.md) | [Пенџаби (Гурумухи)](../pa/README.md) | [Румунски](../ro/README.md) | [Руски](../ru/README.md) | [Српски (ћирилица)](./README.md) | [Словачки](../sk/README.md) | [Словеначки](../sl/README.md) | [Шпански](../es/README.md) | [Свахили](../sw/README.md) | [Шведски](../sv/README.md) | [Тагалог (Филипини)](../tl/README.md) | [Тамилски](../ta/README.md) | [Телугу](../te/README.md) | [Тајландски](../th/README.md) | [Турски](../tr/README.md) | [Украјински](../uk/README.md) | [Урду](../ur/README.md) | [Вијетнамски](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Структура курса и план учења

### **Поглавље 1: Увод у генеративну вештачку интелигенцију**
- **Основни концепти**: Разумевање великих језичких модела, токена, ембеддинзија и могућности ВИ
- **Java AI екосистем**: Преглед Spring AI и OpenAI SDK-ова
- **Протокол Model Context**: Увод у MCP и његову улогу у комуникацији AI агената
- **Практичне примене**: Реални сценарији укључујући разговорне ботове и генерисање садржаја
- **[→ Почни Поглавље 1](./01-IntroToGenAI/README.md)**

### **Поглавље 2: Подешавање развојног окружења**
- **Конфигурација са више провајдера**: Подесите GitHub Models, Azure OpenAI и OpenAI Java SDK интеграције
- **Spring Boot + Spring AI**: Најбоље праксе за развој ентерпрајз ВИ апликација
- **GitHub Models**: Бесплатан приступ AI моделима за прототипирање и учење (није потребна кредитна картица)
- **Алатке за развој**: Docker контејнери, VS Code и GitHub Codespaces конфигурација
- **[→ Почни Поглавље 2](./02-SetupDevEnvironment/README.md)**

### **Поглавље 3: Основне технике генеративне ВИ**
- **Инжењеринг упита**: Технике за оптималне одговоре AI модела
- **Ембеддинзи и векторске операције**: Имплементирање семантичке претраге и подударности
- **Генерација појачана претрагом (RAG)**: Комбинујте ВИ са сопственим изворима података
- **Позив функција**: Проширите могућности ВИ са прилагођеним алатима и додацима
- **[→ Почни Поглавље 3](./03-CoreGenerativeAITechniques/README.md)**

### **Поглавље 4: Практичне примене и пројекти**
- **Генератор прича о кућним љубимцима** (`petstory/`): Креативно генерисање садржаја уз GitHub Models
- **Foundry локално демо** (`foundrylocal/`): Локална интеграција ВИ модела са OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): Основна имплементација Model Context Protocol-а са Spring AI
- **[→ Почни Поглавље 4](./04-PracticalSamples/README.md)**

### **Поглавље 5: Одговорни развој ВИ**
- **Безбедност GitHub модела**: Тестирање уграђеног филтрирања садржаја и механизама безбедности (строга блокирања и мека одбијања)
- **Демо одговорне ВИ**: Практичан пример који показује како савремени системи безбедности ВИ раде у пракси
- **Најбоље праксе**: Основна упутства за етички развој и имплементацију ВИ
- **[→ Почни Поглавље 5](./05-ResponsibleGenAI/README.md)**

## Додатни ресурси

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j за почетнике](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js за почетнике](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Агенти
[![AZD за почетнике](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI за почетнике](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP за почетнике](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI агенти за почетнике](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серија генеративне ВИ
[![Генеративна ВИ за почетнике](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Генеративна ВИ (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Генеративна ВИ (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Генеративна ВИ (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Основно учење
[![Машинско учење за почетнике](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Наука о подацима за почетнике](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![ВИ за почетнике](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Кибербезбедност за почетнике](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Веб развој за почетнике](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![Интернет ствари за почетнике](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Развој XR за почетнике](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Серија Copilot
[![Copilot за парно програмирање са вештачком интелигенцијом](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot за C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot авантура](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Добијање помоћи

Ако запнете или имате било каквих питања у вези са прављењем AI апликација. Придружите се другим полазницима и искусним програмерима у дискусијама о MCP. То је подржавајућа заједница где су питања добродошла и знање се дели слободно.

[![Discord за Microsoft Foundry](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Ако имате повратне информације о производу или наиђете на грешке током израде, посетите:

[![Форум програмера Microsoft Foundry](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Искључење одговорности:
Овај документ је преведен помоћу услуге за превод засноване на вештачкој интелигенцији Co-op Translator (https://github.com/Azure/co-op-translator). Иако настојимо да обезбедимо тачност, имајте у виду да аутоматски преводи могу садржати грешке или нетачности. Изворни документ на његовом оригиналном језику треба сматрати меродавним извором. За критичне информације препоручује се професионални људски превод. Не преузимамо одговорност за било какве неспоразуме или погрешне тумачења која произилазе из коришћења овог превода.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->