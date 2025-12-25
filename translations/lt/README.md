<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T07:04:59+00:00",
  "source_file": "README.md",
  "language_code": "lt"
}
-->
# Generatyvinis DI pradedantiesiems - Java leidimas
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatyvinis DI pradedantiesiems - Java leidimas](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.lt.png)

**Laiko sąnaudos**: Visą dirbtuvę galima atlikti internetu be vietinės aplinkos įrengimo. Aplinkos paruošimas užtrunka apie 2 minutes, o pavyzdžių nagrinėjimui reikia 1–3 valandų, priklausomai nuo tyrinėjimo gylio.

> **Greita pradžia** 

1. Padarykite šio saugyklos fork'ą į savo GitHub paskyrą
2. Spustelėkite **Code** → **Codespaces** skirtuką → **...** → **New with options...**
3. Naudokite numatytuosius nustatymus – tai pasirinks šio kurso Development konteinerį
4. Spustelėkite **Create codespace**
5. Palaukite ~2 minutes, kol aplinka bus paruošta
6. Pereikite tiesiai prie [Pirmojo pavyzdžio](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Ar norite klonuoti vietoje?**
>
> Ši saugykla apima daugiau nei 50 kalbų vertimų, kas žymiai padidina atsisiuntimo dydį. Norėdami klonuoti be vertimų, naudokite sparse checkout:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tai suteikia viską, ko reikia kursui baigti, ir žymiai greitesnį atsisiuntimą.


## Daugiakalbė palaikymas

### Palaikoma per GitHub Action (automatinis ir visada atnaujinamas)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabų](../ar/README.md) | [Bengalų](../bn/README.md) | [Bulgarų](../bg/README.md) | [Birmos (Myanmar)](../my/README.md) | [Kinų (supaprastinta)](../zh/README.md) | [Kinų (tradicinė, Honkongas)](../hk/README.md) | [Kinų (tradicinė, Makao)](../mo/README.md) | [Kinų (tradicinė, Taivanas)](../tw/README.md) | [Kroatų](../hr/README.md) | [Čekų](../cs/README.md) | [Danų](../da/README.md) | [Olandų](../nl/README.md) | [Estų](../et/README.md) | [Suomių](../fi/README.md) | [Prancūzų](../fr/README.md) | [Vokiečių](../de/README.md) | [Graikų](../el/README.md) | [Hebrajų](../he/README.md) | [Hindi](../hi/README.md) | [Vengrų](../hu/README.md) | [Indoneziečių](../id/README.md) | [Italų](../it/README.md) | [Japonų](../ja/README.md) | [Kanadų (Kannada)](../kn/README.md) | [Korėjiečių](../ko/README.md) | [Lietuvių](./README.md) | [Malajiečių](../ms/README.md) | [Malajalamų](../ml/README.md) | [Maratų](../mr/README.md) | [Nepaliečių](../ne/README.md) | [Nigerijos pidžino](../pcm/README.md) | [Norvegų](../no/README.md) | [Persų (Farsi)](../fa/README.md) | [Lenkų](../pl/README.md) | [Portugalų (Brazilija)](../br/README.md) | [Portugalų (Portugalija)](../pt/README.md) | [Pandžabi (Gurmukhi)](../pa/README.md) | [Rumunų](../ro/README.md) | [Rusų](../ru/README.md) | [Serbų (kirilica)](../sr/README.md) | [Slovakų](../sk/README.md) | [Slovėnų](../sl/README.md) | [Ispanų](../es/README.md) | [Svahili](../sw/README.md) | [Švedų](../sv/README.md) | [Tagalog (Filipinų)](../tl/README.md) | [Tamilių](../ta/README.md) | [Telugų](../te/README.md) | [Tajų](../th/README.md) | [Turkų](../tr/README.md) | [Ukrainiečių](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamiečių](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kurso struktūra ir mokymosi kelias

### **1 skyrius: Įvadas į generatyvinį DI**
- **Pagrindinės sąvokos**: didžiųjų kalbos modelių, žetonų, įterpimų ir DI galimybių supratimas
- **Java DI ekosistema**: Spring AI ir OpenAI SDK apžvalga
- **Modelio konteksto protokolas**: MCP pristatymas ir jo vaidmuo DI agentų komunikacijoje
- **Praktinės taikymo sritys**: realaus pasaulio scenarijai, įskaitant pokalbių robotus ir turinio generavimą
- **[→ Pradėti 1 skyrių](./01-IntroToGenAI/README.md)**

### **2 skyrius: Vystymo aplinkos paruošimas**
- **Daugelio tiekėjų konfigūracija**: GitHub Models, Azure OpenAI ir OpenAI Java SDK integracijų nustatymas
- **Spring Boot + Spring AI**: geriausios praktikos įmonių DI taikomųjų programų kūrimui
- **GitHub Models**: nemokamas DI modelio prieigos variantas prototipų kūrimui ir mokymuisi (kreditinė kortelė nereikalinga)
- **Vystymo įrankiai**: Docker konteineriai, VS Code ir GitHub Codespaces konfigūracija
- **[→ Pradėti 2 skyrių](./02-SetupDevEnvironment/README.md)**

### **3 skyrius: Pagrindinės generatyvinio DI technikos**
- **Užklausų inžinerija**: technikos, skirtos optimaliems DI modelio atsakymams
- **Įterpiniai & vektorinės operacijos**: semantinei paieškai ir panašumo atitikčiai įgyvendinti
- **Retrieval-Augmented Generation (RAG)**: derinkite DI su savo duomenų šaltiniais
- **Funkcijų kvietimas**: išplėskite DI galimybes su pritaikytais įrankiais ir papildiniais
- **[→ Pradėti 3 skyrių](./03-CoreGenerativeAITechniques/README.md)**

### **4 skyrius: Praktinės taikymo sritys ir projektai**
- **Pet Story Generator** (`petstory/`): kūrybinis turinio generavimas su GitHub Models
- **Foundry Local Demo** (`foundrylocal/`): vietinė DI modelio integracija su OpenAI Java SDK
- **MCP Calculator Service** (`calculator/`): pagrindinis Modelio konteksto protokolo įgyvendinimas su Spring AI
- **[→ Pradėti 4 skyrių](./04-PracticalSamples/README.md)**

### **5 skyrius: Atsakingas DI vystymas**
- **GitHub Models saugumas**: išbandykite įmontuotas turinio filtravimo ir saugumo mechanikas (griežti blokuojantys filtrai ir minkšti atsisakymai)
- **Atsakingo DI demonstracija**: praktinis pavyzdys, rodantis, kaip šiuolaikinės DI saugumo sistemos veikia praktiškai
- **Geriausios praktikos**: esminės gairės etiškam DI kūrimui ir diegimui
- **[→ Pradėti 5 skyrių](./05-ResponsibleGenAI/README.md)**

## Papildomi ištekliai

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j pradedantiesiems](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js pradedantiesiems](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agentai
[![AZD pradedantiesiems](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI pradedantiesiems](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP pradedantiesiems](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![DI agentai pradedantiesiems](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatyvinis DI serija
[![Generatyvinis DI pradedantiesiems](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatyvinis DI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatyvinis DI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatyvinis DI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Pagrindinis mokymasis
[![Mašinų mokymasis pradedantiesiems](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Duomenų mokslas pradedantiesiems](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![DI pradedantiesiems](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kibernetinis saugumas pradedantiesiems](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Žiniatinklio vystymas pradedantiesiems](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)

[![IoT pradedantiesiems](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR kūrimas pradedantiesiems](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot serijos
[![Copilot poriniam programavimui su AI](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot skirtas C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot nuotykiai](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Pagalbos gavimas

Jei įstrigote arba turite klausimų apie AI programėlių kūrimą. Prisijunkite prie kitų besimokančiųjų ir patyrusių kūrėjų diskusijų apie MCP. Tai palaikanti bendruomenė, kurioje klausimai laukiami ir žinios dalijamos laisvai.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Jei turite atsiliepimų apie produktą arba pastebite klaidų kūrimo metu, apsilankykite:

[![Microsoft Foundry kūrėjų forumas](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
Atsakomybės apribojimas:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors stengiamės užtikrinti tikslumą, atkreipkite dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba turėtų būti laikomas autoritetingu šaltiniu. Kritinei informacijai rekomenduojama pasitelkti profesionalų žmogaus vertimą. Mes neprisiimame atsakomybės už jokius nesusipratimus ar neteisingas interpretacijas, kylančias dėl šio vertimo naudojimo.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->