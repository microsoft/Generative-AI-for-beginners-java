<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T07:08:12+00:00",
  "source_file": "README.md",
  "language_code": "ta"
}
-->
# Generative AI for Beginners - Java பதிப்பு
[![Microsoft Foundry டிஸ்கோர்டு](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java பதிப்பு](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.ta.png)

**காலஅளவு**: முழு வேலைக் குழு உள்ளூரில் அமைக்காமல் ஆன்லைனில் முடிக்கக்கூடியது. சூழல் அமைப்பு 2 நிமிடம் எடுத்துக்கொள்கிறது, மாதிரிகளை ஆராய்சி செய்வதற்கான நேரம் ஆராய்ச்சி ஆழத்தன்மைக்கு ஏற்ப 1-3 மணி நேரம் ஆகும்.

> **விரைவு தொடக்கம்** 

1. இந்த ரெப்போசிடோரி உங்கள் GitHub கணக்குக்கு Fork செய்யவும்
2. **Code** → **Codespaces** தாவலை கிளிக் → **...** → **New with options...** ஐ தேர்ந்தெடுக்கவும்
3. இயல்புகளைக் (<em>defaults</em>) பயன்படுத்தவும் – இது இந்த பாடநெறிக்காக உருவாக்கப்பட்ட Development container ஐ தேர்ந்தெடுக்கும்
4. **Create codespace** ஐ கிளிக் செய்யவும்
5. சூழல் தயார் ஆக ~2 நிமிடம் காத்திருங்கள்
6. நேரடியாக [→ அத்தியாயம் 1 தொடக்கம்](./01-IntroToGenAI/README.md#step-2-create-a-github-personal-access-token) க்கு செல்

> **உள்ளூரில் கிளோன் செய்வதை முன்னுரிமையா கொடுப்பீர்களா?**
>
> இந்த ரெப்போசிடோரியில் 50+ மொழி மொழிபெயர்ப்புகள் உள்ளதால் பதிவிறக்க அளவு பெரிதாகும். மொழிபெயர்ப்புகள் இல்லாமல் கிளோன் செய்ய sparse checkout பயன்படுத்தவும்:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> இது பாடநெறியை முடிக்க உங்களுக்கு தேவையான அனைத்தையும் மிகவும் வேகமான பதிவிறக்கத்துடன் தருகிறது.


## பல மொழி ஆதரவு

### GitHub Action மூலம் ஆதரிக்கப்படுகிறது (தானியங்கி & எப்போதும் புதுப்பிக்கப்படும்)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[அரபிக்](../ar/README.md) | [பெங்காலி](../bn/README.md) | [பல்கேரியன்](../bg/README.md) | [பர்மீஸ் (மியான்மார்)](../my/README.md) | [சீனம் (எளிமைப்படுத்தப்பட்ட)](../zh/README.md) | [சீனம் (சம்பாரம்பரிய, ஹொங் காங்)](../hk/README.md) | [சீனம் (சம்பாரம்பரிய, மாகாவ்)](../mo/README.md) | [சீனம் (சம்பாரம்பரிய, தைவான்)](../tw/README.md) | [குரோஷியன்](../hr/README.md) | [செக்](../cs/README.md) | [டேனிஷ்](../da/README.md) | [டச்சு](../nl/README.md) | [எஸ்டோனியன்](../et/README.md) | [பினிஷ்](../fi/README.md) | [பிரெஞ்சு](../fr/README.md) | [ஜெர்மன்](../de/README.md) | [கிரேக்கு](../el/README.md) | [ஹீப்ரூ](../he/README.md) | [ஹிந்தி](../hi/README.md) | [ஹங்கேரியன்](../hu/README.md) | [இந்தோனேஷியன்](../id/README.md) | [இத்தாலியன்](../it/README.md) | [ஜப்பானீஸ்](../ja/README.md) | [கன்னடம்](../kn/README.md) | [கொரியன்](../ko/README.md) | [லித்துவேனியன்](../lt/README.md) | [மலாய்](../ms/README.md) | [மலையாளம்](../ml/README.md) | [மராத்தி](../mr/README.md) | [நேபாளி](../ne/README.md) | [நைஜீரியன் பிஜின்](../pcm/README.md) | [நார்வேஜியன்](../no/README.md) | [பாரசீக (பார்ஸி)](../fa/README.md) | [போலிஷ்](../pl/README.md) | [போர்த்துகீசிய (பிரேசில்)](../br/README.md) | [போர்த்துகீசிய (போர்ச்சுகல்)](../pt/README.md) | [பஞ்சாபி (குர்முகி)](../pa/README.md) | [ருமேனியன்](../ro/README.md) | [ரஷ்யன்](../ru/README.md) | [செர்பியன் (செரிலிக்)](../sr/README.md) | [ஸ்லோவாக்](../sk/README.md) | [ஸ்லோவேனியன்](../sl/README.md) | [ஸ்பானிஷ்](../es/README.md) | [சுவாஹிலி](../sw/README.md) | [ஸ்வீடிஷ்](../sv/README.md) | [டாகாலொக் (பிலிப்பைன்ஸ்)](../tl/README.md) | [தமிழ்](./README.md) | [తెलుగు](../te/README.md) | [தாய்](../th/README.md) | [துருக்கிஷ்](../tr/README.md) | [உக்ரேனியன்](../uk/README.md) | [உருது](../ur/README.md) | [வியட்நாமீஸ்](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## பாடத்திட்டம் மற்றும் கற்றல் பாதை

### **அத்தியாயம் 1: Generative AI அறிமுகம்**
- **முக்கியக் கோட்பாடுகள்**: பெரிய மொழி மாதிரிகள், டோக்கன்கள், எம்பெடிங்ஸ் மற்றும் ஏஐ திறன்களைப் புரிந்து கொள்வது
- **ஜாவா AI சுற்றுச்சூழல்**: Spring AI மற்றும் OpenAI SDKs பற்றிய சிறுகண்ணோட்டம்
- **Model Context Protocol**: MCP அறிமுகம் மற்றும் ஏஐ ஏஜென்டுகள் இடையிலான தொடர்பில் அதன் பங்கு
- **நிகழ்நிலை பயன்பாடுகள்**: சந்தைப்படுத்தல் மற்றும் உள்ளடக்க உருவாக்கம் உள்ளிட்ட உண்மையான உலக сценарிகள்
- **[→ அத்தியாயம் 1 தொடக்கம்](./01-IntroToGenAI/README.md)**

### **அத்தியாயம் 2: மேம்பாட்டு சூழல் அமைப்பு**
- **பன்முக வழங்குநர் கட்டமைப்பு**: GitHub Models, Azure OpenAI, மற்றும் OpenAI Java SDK ஒருங்கிணைப்புகளை அமைக்கவும்
- **Spring Boot + Spring AI**: நிறுவன அளவிலான ஏஐ பயன்பாட்டு மேம்பாட்டுக்கான சிறந்த நடைமுறைகள்
- **GitHub Models**: Prototype மற்றும் கற்றலுக்கான இலவச AI மாதிரி அணுகல் (கடன் அட்டை தேவையில்லை)
- **மேம்பாட்டு கருவிகள்**: Docker கான்டெய்னர்கள், VS Code, மற்றும் GitHub Codespaces கட்டமைப்பு
- **[→ அத்தியாயம் 2 தொடக்கம்](./02-SetupDevEnvironment/README.md)**

### **அத்தியாயம் 3: பொது Generative AI நுட்பங்கள்**
- **Prompt Engineering**: சிறந்த AI மாதிரி பதில்களுக்கு நுட்பங்கள்
- **Embeddings & வெக்டர் செயல்பாடுகள்**: அர்த்தமுடைய தேடல் மற்றும் ஒத்திசைவை அமல்படுத்துதல்
- **Retrieval-Augmented Generation (RAG)**: உங்கள் சொந்த தரவுத்தளங்களுடன் AI ஐ இணைத்தல்
- **Function Calling**: தனிப்பயன் கருவிகள் மற்றும் பிளக்கின்களுடன் AI திறன்களை विस्तार செய்தல்
- **[→ அத்தியாயம் 3 தொடக்கம்](./03-CoreGenerativeAITechniques/README.md)**

### **அத்தியாயம் 4: நடைமுறை பயன்பாடுகள் & திட்டங்கள்**
- **Pet Story Generator** (`petstory/`): GitHub Models உடன் படைப்பாற்றல் உள்ளடக்க உருவாக்கம்
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK உடன் உள்ளூர் AI மாதிரி ஒருங்கிணைப்பு
- **MCP Calculator Service** (`calculator/`): Spring AI உடன் அடிப்படை Model Context Protocol செயல்பாடு
- **[→ அத்தியாயம் 4 தொடக்கம்](./04-PracticalSamples/README.md)**

### **அத்தியாயம் 5: பொறுப்புள்ள AI மேம்பாடு**
- **GitHub Models பாதுகாப்பு**: கட்டமைக்கப்பட்ட உள்ளடக்க வடிகட்டுதல் மற்றும் பாதுகாப்பு யந்திரங்களை (கடுமையான தடைகள் மற்றும் மென்மையான மறுத்தல்கள்) சோதனை செய்யவும்
- **பொறுப்புள்ள AI டெமோ**: நவீன AI பாதுகாப்பு அமைப்புகள் செயல்படுவதைக் காட்டும் விரைவான கையேடு
- **சிறந்த நடைமுறைகள்**: நெறிமுறைகள், நீதி மற்றும் பாதுகாப்பு சார்ந்த முக்கிய வழிகாட்டுதல்கள்
- **[→ அத்தியாயம் 5 தொடக்கம்](./05-ResponsibleGenAI/README.md)**

## கூடுதல் வளங்கள்

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j தொடக்கத்தினருக்கு](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js தொடக்கத்தினருக்கு](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / ஏஜென்டுகள்
[![AZD தொடக்கத்தினருக்கு](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI தொடக்கத்தினருக்கு](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP தொடக்கத்தினருக்கு](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents தொடக்கத்தினருக்கு](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative AI தொடர்
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### முக்கியக் கற்றல்
[![ML தொடக்கத்தினருக்கு](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![தரவு அறிவியல் தொடக்கத்தினருக்கு](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI தொடக்கத்தினருக்கு](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![உறுப்புணர்வியல் பாதுகாப்பு தொடக்கத்தினருக்கு](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![வெப்உருவாக்கம் தொடக்கத்தினருக்கு](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பகர்களுக்கான IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பகர்களுக்கான XR அபிவிருத்தி](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### கோபைலட் தொடர்
[![AI இணைக்கப்பட்ட ஜோடி நிரலாக்கத்துக்கான Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET க்கான Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot அனுபவம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

If you get stuck or have any questions about building AI apps. Join fellow learners and experienced developers in discussions about MCP. It's a supportive community where questions are welcome and knowledge is shared freely.

[![Microsoft Foundry டிஸ்கோர்ட்](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

If you have product feedback or errors while building visit:

[![Microsoft Foundry டெவலப்பர் மன்றம்](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
மறுப்பு:
இந்த ஆவணம் AI மொழிபெயர்ப்பு சேவையான [Co-op Translator](https://github.com/Azure/co-op-translator) மூலம் மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்திற்காக முயற்சித்தாலும், தானியங்கி மொழிபெயர்ப்புகளில் தவறுகள் அல்லது துல்லியமின்மைகள் இருக்கக்கூடும் என்பதை தயவுசெய்து கவனிக்கவும். மூல ஆவணத்தை அதன் சொந்த மொழியில் அதிகாரப்பூர்வ ஆதாரமாக கருதப்பட வேண்டும். முக்கியமான தகவல்களுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பாளர் மூலம் மொழிபெயர்ப்பு செய்வதை பரிந்துரிக்கிறோம். இந்த மொழிபெயர்ப்பைப் பயன்படுத்துவதனால் ஏற்படும் எந்த தவறான புரிதலும் அல்லது தவறான விளக்கங்களுக்கும் நாங்கள் பொறுப்பேற்கமாட்டோம்.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->