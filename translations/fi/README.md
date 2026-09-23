# Generatiivinen tekoäly aloittelijoille - Java-versio
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatiivinen tekoäly aloittelijoille - Java-versio](../../translated_images/fi/beg-genai-series.8b48be9951cc574c.webp)

**Aikavaatimukset**: Koko työpaja voidaan suorittaa verkossa ilman paikallista asennusta. Ympäristön perustaminen vie 2 minuuttia, ja esimerkkien tutkiminen vie 1-3 tuntia riippuen tutkimisen syvyydestä.

> **Nopea aloitus**

1. Forkkaa tämä repositorio GitHub-tilillesi
2. Klikkaa **Code** → **Codespaces** -välilehti → **...** → **Uusi vaihtoehdoilla...**
3. Käytä oletusasetuksia – tämä valitsee tämän kurssin kehityssäiliön
4. Klikkaa **Luo codespace**
5. Odota ~2 minuuttia, että ympäristö on valmis
6. Siirry suoraan kohtaan [Luku 2: Provision Azure AI Foundry](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## Monikielinen tuki

### Tuki GitHub Actionin kautta (automatisoitu ja aina ajan tasalla)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabia](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgaria](../bg/README.md) | [Burma (Myanmar)](../my/README.md) | [Kiina (yksinkertaistettu)](../zh-CN/README.md) | [Kiina (perinteinen, Hong Kong)](../zh-HK/README.md) | [Kiina (perinteinen, Macau)](../zh-MO/README.md) | [Kiina (perinteinen, Taiwan)](../zh-TW/README.md) | [Kroatia](../hr/README.md) | [Tšekki](../cs/README.md) | [Tanska](../da/README.md) | [Hollanti](../nl/README.md) | [Viro](../et/README.md) | [Suomi](./README.md) | [Ranska](../fr/README.md) | [Saksa](../de/README.md) | [Kreikka](../el/README.md) | [Heprea](../he/README.md) | [Hindi](../hi/README.md) | [Unkari](../hu/README.md) | [Indonesia](../id/README.md) | [Italia](../it/README.md) | [Japani](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Korea](../ko/README.md) | [Liettua](../lt/README.md) | [Malaiji](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian pidgin](../pcm/README.md) | [Norja](../no/README.md) | [Persia (Farsi)](../fa/README.md) | [Puola](../pl/README.md) | [Portugali (Brasilia)](../pt-BR/README.md) | [Portugali (Portugali)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romania](../ro/README.md) | [Venäjä](../ru/README.md) | [Serbia (kyrillinen)](../sr/README.md) | [Slovakki](../sk/README.md) | [Slovenia](../sl/README.md) | [Espanja](../es/README.md) | [Swahili](../sw/README.md) | [Ruotsi](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamili](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkki](../tr/README.md) | [Ukraina](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnami](../vi/README.md)

> **Haluatko mieluummin kloonata paikallisesti?**
>
> Tässä repositoriossa on yli 50 kielen käännöksiä, mikä kasvattaa merkittävästi latauskokoa. Jos haluat kloonata ilman käännöksiä, käytä sparse checkoutia:
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
> Saat kaiken tarvitsemasi kurssin suorittamiseen nopeammalla latauksella.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kurssin rakenne ja oppimispolku

### **Luku 1: Johdatus generatiiviseen tekoälyyn**
- **Keskeiset käsitteet**: Suurten kielimallien, tokenien, upotusten ja tekoälyn kyvykkyyksien ymmärtäminen
- **Java AI -ekosysteemi**: Yleiskatsaus Spring AI:hin ja OpenAI SDK:ihin
- **Mallin kontekstiprotokolla**: Johdatus MCP:hen ja sen rooli tekoälyagenttien viestinnässä
- **Käytännön sovellukset**: Todelliset skenaariot, mukaan lukien chatbotit ja sisällön generointi
- **[→ Aloita luku 1](./01-IntroToGenAI/README.md)**

### **Luku 2: Kehitysympäristön perustaminen**
- **Azure AI Foundry**: GPT-5.6 Luna chat ja text-embedding-3-small embeddingit Bicepillä ja Azure Developer CLI:llä (azd)
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: Opiskele `ChatClient`-luokan avulla, virallisen OpenAI Java SDK:n ja Azure OpenAI v1:n tukemana
- **Avainvapaa todennus**: Turvallinen yhteys Microsoft Entra ID:llä — ei API-avainten hallintaa
- **Kehitystyökalut**: Docker-kontit, VS Code ja GitHub Codespaces -konfiguraatio
- **[→ Aloita luku 2](./02-SetupDevEnvironment/README.md)**

### **Luku 3: Keskeiset generatiiviset tekoälytekniikat**
- **Virallinen OpenAI Java SDK**: Kutsu Azure OpenAI v1:stä suoraan avainvapaalla todennuksella
- **Prompt-engineering**: Tekniikat optimaalisiin tekoälymallin vastauksiin
- **Upotukset ja vektorioperaatiot**: Semanttisen haun ja samankaltaisuuden toteutus
- **Hakua täydentävä generointi (RAG)**: Yhdistä tekoäly omiin tietolähteisiisi
- **Funktiokutsut**: Laajenna tekoälyn kykyjä räätälöidyillä työkaluilla ja lisäosilla
- **[→ Aloita luku 3](./03-CoreGenerativeAITechniques/README.md)**

### **Luku 4: Käytännön sovellukset ja projektit**
- **Lemmikkitarinageneraattori** (`petstory/`): Luova sisällön generointi Azure AI Foundryn kanssa
- **Foundry Local -demo** (`foundrylocal/`): Paikallinen tekoälymallin integraatio OpenAI Java SDK:n kanssa
- **MCP-laskinpalvelu** (`calculator/`): Perus Model Context Protocol -toteutus Spring AI:lla
- **[→ Aloita luku 4](./04-PracticalSamples/README.md)**

### **Luku 5: Vastuullinen tekoälykehitys**
- **Azure AI Foundryn sisällön turvallisuus**: Testaa sisäänrakennetut sisältöjen suodatus- ja turvallisuusmekanismit (kovat estot ja pehmeät kieltäytymiset)
- **Vastuullisen tekoälyn demo**: Käytännön esimerkki siitä, miten modernit tekoälyn turvallisuusjärjestelmät toimivat
- **Parhaat käytännöt**: Olennaiset ohjeistukset eettiseen tekoälykehitykseen ja käyttöönottoon
- **[→ Aloita luku 5](./05-ResponsibleGenAI/README.md)**

## Lisäresurssit

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j aloittelijoille](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js aloittelijoille](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain aloittelijoille](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agentit
[![AZD aloittelijoille](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI aloittelijoille](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP aloittelijoille](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI-agentit aloittelijoille](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatiivinen tekoäly -sarja
[![Generatiivinen tekoäly aloittelijoille](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatiivinen tekoäly (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatiivinen tekoäly (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatiivinen tekoäly (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Ydinopetus
[![ML aloittelijoille](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science aloittelijoille](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![Tekoäly aloittelijoille](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kyberturvallisuus aloittelijoille](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Verkkokehitys aloittelijoille](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT aloittelijoille](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR-kehitys aloittelijoille](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot-sarja
[![Copilot tekoälyn pariohjelmointiin](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot C#/.NET:lle](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot-seikkailu](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Apua saatavilla

Jos jumitut tai sinulla on kysyttävää tekoälysovellusten rakentamisesta, liity muiden oppijoiden ja kokeneiden kehittäjien keskusteluihin MCP:stä. Se on kannustava yhteisö, jossa kysymykset ovat tervetulleita ja tieto jaetaan vapaasti.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Jos sinulla on palautetta tuotteesta tai kohtaat virheitä rakentaessasi, käy:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->