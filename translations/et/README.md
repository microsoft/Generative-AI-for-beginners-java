<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T10:01:35+00:00",
  "source_file": "README.md",
  "language_code": "et"
}
-->
# Generatiivne tehisintellekt algajatele - Java väljaanne
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatiivne tehisintellekt algajatele - Java väljaanne](../../translated_images/et/beg-genai-series.8b48be9951cc574c.webp)

**Ajapanus**: Kogu töötuba saab lõpetada veebis ilma kohaliku seadistuseta. Keskkonna seadistamine võtab 2 minutit ja näidiste uurimine 1–3 tundi, sõltuvalt uurimise sügavusest.

> **Kiire algus**

1. Forki see hoidla oma GitHubi kontole
2. Klõpsa **Code** → **Codespaces** vahekaart → **...** → **New with options...**
3. Kasuta vaikeseadeid – see valib selle kursuse jaoks loodud arendusmahuti
4. Klõpsa **Create codespace**
5. Oota ~2 minutit, kuni keskkond on valmis
6. Hüpata otse [Esimese näite juurde](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Eelistad kloonida kohalikult?**
>
> Selles hoidlas on üle 50 keele tõlke, mis suurendab allalaadimise mahtu märkimisväärselt. Ilma tõlgeteta kloonimiseks kasuta sparse checkout'i:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> See annab sulle kõik vajaliku kursuse läbimiseks palju kiiremalt.

## Mitmekeelne tugi

### Toetatud GitHub Actioni kaudu (automaatne ja alati ajakohane)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[araabia](../ar/README.md) | [bengali](../bn/README.md) | [bulgaaria](../bg/README.md) | [burma (Myanmar)](../my/README.md) | [hiina (lihtsustatud)](../zh/README.md) | [hiina (traditsiooniline, Hongkong)](../hk/README.md) | [hiina (traditsiooniline, Macau)](../mo/README.md) | [hiina (traditsiooniline, Taiwan)](../tw/README.md) | [horvaadi](../hr/README.md) | [tšehhi](../cs/README.md) | [taani](../da/README.md) | [hollandi](../nl/README.md) | [eesti](./README.md) | [soome](../fi/README.md) | [prantsuse](../fr/README.md) | [saksa](../de/README.md) | [kreeka](../el/README.md) | [heebrea](../he/README.md) | [hindi](../hi/README.md) | [ungari](../hu/README.md) | [indoneesia](../id/README.md) | [itaalia](../it/README.md) | [jaapani](../ja/README.md) | [kannada](../kn/README.md) | [korea](../ko/README.md) | [leedu](../lt/README.md) | [malai](../ms/README.md) | [malajalami](../ml/README.md) | [marathi](../mr/README.md) | [nepali](../ne/README.md) | [Nigeeria pidgin](../pcm/README.md) | [norra](../no/README.md) | [pärsia (Farsi)](../fa/README.md) | [poola](../pl/README.md) | [portugali (Brasiilia)](../br/README.md) | [portugali (Portugal)](../pt/README.md) | [pandžabi (Gurmukhi)](../pa/README.md) | [rumeenia](../ro/README.md) | [vene](../ru/README.md) | [serbia (kirillitsa)](../sr/README.md) | [slovaki](../sk/README.md) | [sloveeni](../sl/README.md) | [hispaania](../es/README.md) | [suahiili](../sw/README.md) | [rootsi](../sv/README.md) | [tagalogi (filipino)](../tl/README.md) | [tamiili](../ta/README.md) | [telugu](../te/README.md) | [tai](../th/README.md) | [türgi](../tr/README.md) | [ukraina](../uk/README.md) | [urdu](../ur/README.md) | [vietnami](../vi/README.md)

> **Eelistad kloonida kohalikult?**

> Selles hoidlas on üle 50 keele tõlke, mis suurendab allalaadimise mahtu märkimisväärselt. Ilma tõlgeteta kloonimiseks kasuta sparse checkout'i:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> See annab sulle kõik vajaliku kursuse läbimiseks palju kiiremalt.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kursuse struktuur ja õppeteekond

### **1. peatükk: Sissejuhatus generatiivsesse tehisintellekti**
- **Põhikontseptsioonid**: Suurte keelemudelite, tokenite, manuste ja tehisintellekti võimete mõistmine
- **Java AI ökosüsteem**: Ülevaade Spring AI ja OpenAI SDK-dest
- **Mudeli konteksti protokoll**: Sissejuhatus MCP-sse ja selle roll AI agentide suhtluses
- **Praktilised rakendused**: Tõelised stsenaariumid, sealhulgas vestlusrobotid ja sisuloomine
- **[→ Alusta 1. peatükist](./01-IntroToGenAI/README.md)**

### **2. peatükk: Arenduskeskkonna seadistamine**
- **Mitme pakkuja konfiguratsioon**: GitHubi mudelite, Azure OpenAI ja OpenAI Java SDK integratsioonide seadistamine
- **Spring Boot + Spring AI**: Parimad tavad ettevõtte AI rakenduste arendamiseks
- **GitHubi mudelid**: Tasuta AI mudelite ligipääs prototüüpimiseks ja õppimiseks (ilma krediitkaardita)
- **Arendustööriistad**: Docker mahutid, VS Code ja GitHubi Codespaces konfiguratsioon
- **[→ Alusta 2. peatükist](./02-SetupDevEnvironment/README.md)**

### **3. peatükk: Põhilised generatiivse tehisintellekti tehnikad**
- **Promptide inseneriteadus**: Optimaalse AI mudelitele vastamise tehnikad
- **Manused ja vektoritöötlus**: Semantilise otsingu ja sarnasuse sobitamise rakendamine
- **Taasmine täiendatud genereerimine (RAG)**: AI ühendamine oma andmeallikatega
- **Funktsioonide kutsumine**: AI võimete laiendamine kohandatud tööriistade ja pistikprogrammidega
- **[→ Alusta 3. peatükist](./03-CoreGenerativeAITechniques/README.md)**

### **4. peatükk: Praktilised rakendused ja projektid**
- **Lemmiku loo generaator** (`petstory/`): Loov sisuloomine GitHubi mudelitega
- **Foundry kohalik demo** (`foundrylocal/`): Kohalik AI mudeli integratsioon OpenAI Java SDK-ga
- **MCP kalkulaatori teenus** (`calculator/`): Põhiline Mudeli konteksti protokolli rakendus Spring AI-ga
- **[→ Alusta 4. peatükist](./04-PracticalSamples/README.md)**

### **5. peatükk: Vastutustundlik AI arendus**
- **GitHubi mudelite turvalisus**: Sisseehitatud sisufiltri ja turvaprotokollide (rasked plokid ja pehmed keeldumised) testimine
- **Vastutustundliku AI demo**: Praktiline näide tänapäevaste AI turvasüsteemide toimimisest
- **Parimad tavad**: Olulised juhised eetiliseks AI arenduseks ja kasutuselevõtuks
- **[→ Alusta 5. peatükist](./05-ResponsibleGenAI/README.md)**

## Täiendavad ressursid

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j algajatele](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js algajatele](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agentid
[![AZD algajatele](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI algajatele](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP algajatele](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI agentid algajatele](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatiivne AI seeria
[![Generatiivne AI algajatele](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatiivne AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatiivne AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatiivne AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Põhioskused
[![Masinõpe algajatele](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Andmeteadus algajatele](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI algajatele](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Küberjulgeolek algajatele](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Veebiarendus algajatele](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT algajatele](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR arendamine algajatele](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copiloti sari
[![Copilot tehisintellekti paarisprogrammeerimiseks](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot C#/.NET jaoks](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot seiklus](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Abi saamine

Kui te jääte hätta või teil on küsimusi tehisintellekti rakenduste loomise kohta. Liituge teiste õppijate ja kogenud arendajatega aruteludes MCP teemal. See on toetav kogukond, kus küsimused on teretulnud ja teadmisi jagatakse vabalt.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Kui teil on toodet puudutav tagasiside või ehitamisel tekib vigu, külastage:

[![Microsoft Foundry arendajate foorum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastutusest loobumine**:
See dokument on tõlgitud kasutades tehisintellektil põhinevat tõlkimisteenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi püüame täpsust, palun arvestage, et automatiseeritud tõlked võivad sisaldada vigu või ebatäpsusi. Originaaldokument oma emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta mistahes arusaamatuste või valesti mõistmiste eest, mis võivad tekkida selle tõlke kasutamisest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->