<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:29:12+00:00",
  "source_file": "README.md",
  "language_code": "fi"
}
-->
# Generatiivinen tekoäly aloittelijoille - Java Edition
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generatiivinen tekoäly aloittelijoille - Java Edition](../../translated_images/fi/beg-genai-series.8b48be9951cc574c.webp)

**Ajan tarve**: Koko työpajan voi suorittaa verkossa ilman paikallista asennusta. Ympäristön asetukset vievät 2 minuuttia, ja esimerkkien tutkimiseen menee 1-3 tuntia riippuen tutkimisen syvyydestä.

> **Pika-aloitus**

1. Tee forkki tästä repositoriosta GitHub-tilillesi
2. Valitse **Code** → **Codespaces** -välilehti → **...** → **Uusi vaihtoehdoilla...**
3. Käytä oletuksia – tämä valitsee tähän kurssiin luodun kehityssäiliön
4. Klikkaa **Luo codespace**
5. Odota noin 2 minuuttia, että ympäristö on valmis
6. Siirry suoraan kohtaan [Ensimmäinen esimerkki](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **Haluatko mieluummin kloonata paikallisesti?**
>
> Tämä repositorio sisältää yli 50 kielen käännökset, jotka merkittävästi suurentavat lataustiedoston kokoa. Kloonaa ilman käännöksiä käyttämällä sparse checkoutia:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tämä antaa kaiken tarvittavan kurssin suorittamiseen huomattavasti nopeammalla latauksella.


## Monikielinen tuki

### Tuettu GitHub Actionin kautta (automaattinen ja aina ajan tasalla)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabia](../ar/README.md) | [Bengali](../bn/README.md) | [Bulgaria](../bg/README.md) | [Burma (Myanmar)](../my/README.md) | [Kiina (yksinkertaistettu)](../zh/README.md) | [Kiina (perinteinen, Hong Kong)](../hk/README.md) | [Kiina (perinteinen, Macao)](../mo/README.md) | [Kiina (perinteinen, Taiwan)](../tw/README.md) | [Kroatia](../hr/README.md) | [Tšekki](../cs/README.md) | [Tanska](../da/README.md) | [Hollanti](../nl/README.md) | [Viro](../et/README.md) | [Suomi](./README.md) | [Ranska](../fr/README.md) | [Saksa](../de/README.md) | [Kreikka](../el/README.md) | [Heprea](../he/README.md) | [Hindi](../hi/README.md) | [Unkari](../hu/README.md) | [Indonesia](../id/README.md) | [Italia](../it/README.md) | [Japani](../ja/README.md) | [Kannada](../kn/README.md) | [Korea](../ko/README.md) | [Liettua](../lt/README.md) | [Malaiji](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norja](../no/README.md) | [Persia (Farsi)](../fa/README.md) | [Puola](../pl/README.md) | [Portugali (Brasilia)](../br/README.md) | [Portugali (Portugali)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romania](../ro/README.md) | [Venäjä](../ru/README.md) | [Serbia (kyrillinen)](../sr/README.md) | [Slovakki](../sk/README.md) | [Slovenia](../sl/README.md) | [Espanja](../es/README.md) | [Swaheli](../sw/README.md) | [Ruotsi](../sv/README.md) | [Tagalog (filipino)](../tl/README.md) | [Tamili](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkki](../tr/README.md) | [Ukraina](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnam](../vi/README.md)

> **Haluatko mieluummin kloonata paikallisesti?**

> Tämä repositorio sisältää yli 50 kielen käännöksiä, mikä lisää merkittävästi lataustiedoston kokoa. Kloonaa ilman käännöksiä käyttämällä sparse checkoutia:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> Tämä antaa kaiken tarvitsemasi kurssin suorittamiseen paljon nopeammalla latauksella.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kurssin rakenne ja oppimispolku

### **Luku 1: Johdatus generatiiviseen tekoälyyn**
- **Peruskäsitteet**: Suurten kielimallien, tokenien, upotusten ja tekoälyn kyvykkyyksien ymmärtäminen
- **Java AI -ekosysteemi**: Yleiskatsaus Spring AI:hin ja OpenAI SDK:hin
- **Mallin kontekstiprotokolla**: Johdatus MCP:hen ja sen rooliin tekoälyagenttien kommunikaatiossa
- **Käytännön sovellukset**: Reaalimaailman skenaariot, mukaan lukien chatbotit ja sisällöntuotanto
- **[→ Aloita luku 1](./01-IntroToGenAI/README.md)**

### **Luku 2: Kehitysympäristön asennus**
- **Monitoimittajakonfiguraatio**: GitHub-mallit, Azure OpenAI ja OpenAI Java SDK -integraatiot
- **Spring Boot + Spring AI**: Parhaat käytännöt yritystason tekoälysovelluskehitykseen
- **GitHub-mallit**: Ilmainen pääsy tekoälymalleihin prototypointiin ja oppimiseen (ei luottokorttia vaadita)
- **Kehitystyökalut**: Docker-kontit, VS Code ja GitHub Codespaces -asetukset
- **[→ Aloita luku 2](./02-SetupDevEnvironment/README.md)**

### **Luku 3: Keskeiset generatiivisen tekoälyn tekniikat**
- **Prompt-tekniikat**: Tekniikat parhaiden vastausten saamiseksi tekoälymalleilta
- **Upotukset ja vektoriopeet**: Semanttisen haun ja samankaltaisuuden sovittamisen toteutus
- **Hakuavusteinen generointi (RAG)**: Yhdistä tekoäly omaan tietolähteeseesi
- **Funktioiden kutsuminen**: Laajenna tekoälyn ominaisuuksia omilla työkaluilla ja laajennuksilla
- **[→ Aloita luku 3](./03-CoreGenerativeAITechniques/README.md)**

### **Luku 4: Käytännön sovellukset ja projektit**
- **Lemmikkitarinan generaattori** (`petstory/`): Luova sisällöntuotanto GitHub-malleilla
- **Foundryn paikallinen demo** (`foundrylocal/`): Paikallinen tekoälymallien integraatio OpenAI Java SDK:lla
- **MCP-laskinpalvelu** (`calculator/`): Perusmallikonseptiprotokollan toteutus Spring AI:lla
- **[→ Aloita luku 4](./04-PracticalSamples/README.md)**

### **Luku 5: Vastuullinen tekoälykehitys**
- **GitHub-mallien turvallisuus**: Kokeile sisäänrakennettuja sisällönsuodatuksia ja turvamekanismeja (kovat estot ja pehmeät kieltäytymiset)
- **Vastuullisen tekoälyn demo**: Käytännön esimerkki nykyaikaisten tekoälyn turvajärjestelmien toiminnasta
- **Parhaat käytännöt**: Keskeiset ohjeet eettiseen tekoälykehitykseen ja käyttöönottoon
- **[→ Aloita luku 5](./05-ResponsibleGenAI/README.md)**

## Lisäresurssit

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j aloittelijoille](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js aloittelijoille](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agentit
[![AZD aloittelijoille](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI aloittelijoille](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP aloittelijoille](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI-agentit aloittelijoille](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generatiivinen tekoälysarja
[![Generatiivinen tekoäly aloittelijoille](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generatiivinen tekoäly (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generatiivinen tekoäly (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generatiivinen tekoäly (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Perusopetus
[![ML aloittelijoille](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science aloittelijoille](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![Tekoäly aloittelijoille](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Kyberturvallisuus aloittelijoille](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web-kehitys aloittelijoille](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT for Beginners](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR Development for Beginners](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot -sarja
[![Copilot for AI Paired Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot for C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Adventure](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Apua saamaan

Jos jäät jumiin tai sinulla on kysyttävää tekoälysovellusten rakentamisesta, liity muiden oppijoiden ja kokeneiden kehittäjien keskusteluihin MCP:stä. Se on tukevainen yhteisö, jossa kysymykset ovat tervetulleita ja tietoa jaetaan vapaasti.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Jos sinulla on tuotearvioita tai kohtaat virheitä rakentamisen aikana, käy täällä:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttäen tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Pyrimme tarkkuuteen, mutta huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen omalla kielellä on luotettava lähde. Tärkeiden tietojen osalta suositellaan ammattilaisten tekemää ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai virhetulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->