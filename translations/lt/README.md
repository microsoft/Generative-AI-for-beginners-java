<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:59:22+00:00",
  "source_file": "README.md",
  "language_code": "lt"
}
-->
# Generatyvinis DI pradedantiesiems – Java leidimas
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatyvinis DI pradedantiesiems – Java leidimas](../../translated_images/lt/beg-genai-series.8b48be9951cc574c.webp)

**Laiko sąnaudų kiekis**: Visa dirbtuvė gali būti atlikta internetu be vietinės aplinkos konfigūracijos. Aplinkos paruošimas užtrunka 2 minutes, o pavyzdžių tyrinėjimas – nuo 1 iki 3 valandų, priklausomai nuo tyrinėjimo gylio.

> **Greitas pradėjimas**

1. Padarykite šio saugyklos šaką (fork) į savo GitHub paskyrą  
2. Paspauskite **Code** → **Codespaces** skirtuką → **...** → **New with options...**  
3. Naudokite numatytuosius nustatymus – tai pasirinkti šiam kursui sukurtą kūrimo konteinerį  
4. Paspauskite **Create codespace**  
5. Palaukite ~2 minutes, kol aplinka bus paruošta  
6. Eikite tiesiai į [Pirmąjį pavyzdį](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Norite klonuoti vietoje?**

> Ši saugykla apima daugiau nei 50 kalbų vertimų, todėl reikšmingai padidėja atsisiuntimo dydis. Kad galėtumėte klonuoti be vertimų, naudokite sparse checkout:  
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tai suteikia viską, ko reikia kursui įveikti, ir atsisiuntimas vyksta žymiai greičiau.

## Daugiakalbė palaikymas

### Palaikoma per GitHub Action (automatizuota ir visada atnaujinama)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabų](../ar/README.md) | [Bengalų](../bn/README.md) | [Bulgarų](../bg/README.md) | [Birmos (Mjanmaras)](../my/README.md) | [Kinų (supaprastinta)](../zh/README.md) | [Kinų (tradicinė, Honkongas)](../hk/README.md) | [Kinų (tradicinė, Makao)](../mo/README.md) | [Kinų (tradicinė, Taivanas)](../tw/README.md) | [Kroatų](../hr/README.md) | [Čekų](../cs/README.md) | [Danų](../da/README.md) | [Olandų](../nl/README.md) | [Estų](../et/README.md) | [Suomių](../fi/README.md) | [Prancūzų](../fr/README.md) | [Vokiečių](../de/README.md) | [Graikų](../el/README.md) | [Hebrajų](../he/README.md) | [Hindi](../hi/README.md) | [Vengrų](../hu/README.md) | [Indoneziečių](../id/README.md) | [Italų](../it/README.md) | [Japonų](../ja/README.md) | [Kannadų](../kn/README.md) | [Korėjiečių](../ko/README.md) | [Lietuvių](./README.md) | [Malajų](../ms/README.md) | [Malajalamų](../ml/README.md) | [Marati](../mr/README.md) | [Nepaliečių](../ne/README.md) | [Nigerijos pidžino](../pcm/README.md) | [Norvegų](../no/README.md) | [Persų (farsų)](../fa/README.md) | [Lenkų](../pl/README.md) | [Portugalų (Brazilija)](../br/README.md) | [Portugalų (Portugalija)](../pt/README.md) | [Pandžabų (Gurmukhi)](../pa/README.md) | [Rumunų](../ro/README.md) | [Rusų](../ru/README.md) | [Serbų (kirilica)](../sr/README.md) | [Slovakų](../sk/README.md) | [Slovėnų](../sl/README.md) | [Ispanų](../es/README.md) | [Suahelių](../sw/README.md) | [Švedų](../sv/README.md) | [Tagalogų (filipiniečių)](../tl/README.md) | [Tamilų](../ta/README.md) | [Telugų](../te/README.md) | [Tailando](../th/README.md) | [Turkų](../tr/README.md) | [Ukrainiečių](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamiečių](../vi/README.md)

> **Norite klonuoti vietoje?**

> Ši saugykla apima daugiau nei 50 kalbų vertimų, todėl reikšmingai padidėja atsisiuntimo dydis. Kad galėtumėte klonuoti be vertimų, naudokite sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tai suteikia viską, ko reikia kursui įveikti, ir atsisiuntimas vyksta žymiai greičiau.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kurso struktūra ir mokymosi kelias

### **1 skyrius: Įvadas į generatyvinį DI**
- **Pagrindinės sąvokos**: Didelių kalbos modelių, žetonų, įterpimų ir DI galimybių supratimas  
- **Java DI ekosistema**: Apžvalga apie Spring AI ir OpenAI SDK  
- **Modelio konteksto protokolas**: MCP pristatymas ir jo vaidmuo DI agentų komunikacijoje  
- **Praktinės taikymo sritys**: Tikri scenarijai, įskaitant pokalbių robotus ir turinio generavimą  
- **[→ Pradėti 1 skyrių](./01-IntroToGenAI/README.md)**

### **2 skyrius: Kūrimo aplinkos paruošimas**
- **Daugiaproveidinių paslaugų konfiguracija**: GitHub Modelių, Azure OpenAI ir OpenAI Java SDK integracijos  
- **Spring Boot + Spring AI**: Geriausios praktikos įmonių DI programų kūrimui  
- **GitHub Modeliai**: Nemokamas DI modelių prieinamumas prototipams ir mokymuisi (be kreditinės kortelės)  
- **Kūrimo įrankiai**: Docker konteineriai, VS Code ir GitHub Codespaces konfigūracija  
- **[→ Pradėti 2 skyrių](./02-SetupDevEnvironment/README.md)**

### **3 skyrius: Pagrindinės generatyvinio DI technikos**
- **Promptų inžinerija**: Optimizuoti DI modelių atsakymai  
- **Įterpimai ir vektoriniai veiksmai**: Semantinė paieška ir panašumo atitikimas  
- **Retrieval-Augmented Generation (RAG)**: Derinti DI su savo duomenų šaltiniais  
- **Funkcijų kvietimas**: Išplėsti DI galimybes su individualiais įrankiais ir įskiepių palaikymu  
- **[→ Pradėti 3 skyrių](./03-CoreGenerativeAITechniques/README.md)**

### **4 skyrius: Praktinės taikymo sritys ir projektai**
- **Augintinių pasakų generatorius** (`petstory/`): Kūrybinis turinio generavimas su GitHub Modeliais  
- **Foundry vietinė demonstracija** (`foundrylocal/`): Vietinė DI modelio integracija su OpenAI Java SDK  
- **MCP skaičiuoklio paslauga** (`calculator/`): Bazinė Modelio konteksto protokolo įgyvendinimas su Spring AI  
- **[→ Pradėti 4 skyrių](./04-PracticalSamples/README.md)**

### **5 skyrius: Atsakingas DI kūrimas**
- **GitHub Modelių saugumas**: Išbandyti įmontuotus turinio filtrus ir saugumo mechanizmus (griežti blokavimai ir minkšti atsisakymai)  
- **Atsakingo DI demonstracija**: Praktinis pavyzdys, kaip veikia šiuolaikinės DI saugumo sistemos  
- **Geriausios praktikos**: Esminės gairės etiškam DI kūrimui ir diegimui  
- **[→ Pradėti 5 skyrių](./05-ResponsibleGenAI/README.md)**

## Papildomi ištekliai

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain  
[![LangChain4j pradedantiesiems](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)  
[![LangChain.js pradedantiesiems](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agentai  
[![AZD pradedantiesiems](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![Edge DI pradedantiesiems](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![MCP pradedantiesiems](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![DI agentai pradedantiesiems](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### Generatyvinio DI serija  
[![Generatyvinis DI pradedantiesiems](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![Generatyvinis DI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)  
[![Generatyvinis DI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)  
[![Generatyvinis DI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---

### Pagrindinis mokymasis  
[![ML pradedantiesiems](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)  
[![Duomenų mokslas pradedantiesiems](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)  
[![DI pradedantiesiems](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)  
[![Kibernetinis saugumas pradedantiesiems](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)  
[![Tinklapių kūrimas pradedantiesiems](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT pradedantiesiems](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR plėtra pradedantiesiems](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Serija
[![Copilot AI poriniam programavimui](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Nuotykiai](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Pagalbos gavimas

Jei užstringate arba turite klausimų apie AI programėlių kūrimą. Prisijunkite prie kitų mokinių ir patyrusių programuotojų diskusijų MCP. Tai palaikanti bendruomenė, kur klausimai yra laukiami, o žinios dalijamasi laisvai.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Jei turite produktų atsiliepimų ar klaidų kūrimo metu, apsilankykite:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant AI vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatizuoti vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas pagrindiniu šaltiniu. Svarbiai informacijai rekomenduojama naudotis profesionalaus žmogaus vertimu. Mes neatsakome už bet kokius nesusipratimus ar klaidingas interpretacijas, atsiradusias naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->