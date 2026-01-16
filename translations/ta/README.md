<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T10:00:49+00:00",
  "source_file": "README.md",
  "language_code": "ta"
}
-->
# தொடக்கத்தினருக்கான உருவாக்கும் AI - ஜாவா பதிப்பு
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative AI for Beginners - Java Edition](../../translated_images/ta/beg-genai-series.8b48be9951cc574c.webp)

**நேரம் ஒதுக்கீடு**: முழு பணியகம் அடிப்படையில் உள்ளமைக்காமல் ஆன்லைனில் முடிக்க முடியும். சுற்றுச்சூழல் அமைப்புக்கு 2 நிமிடங்கள் பிடிக்கும், மாதிரிகளை ஆராய 1-3 மணி நேரம் தேவைப்படும், ஆராய்ச்சி ஆழத்தின்படி.

> **விரைவான தொடக்கம்** 

1. இந்த நிரல்பெட்டியினை உங்கள் GitHub கணக்கிற்கு Fork செய்யவும்
2. **Code** → **Codespaces** தாவலை → **...** → **New with options...** என்பதை கிளிக் செய்யவும்
3. இயல்புகளை பயன்படுத்தவும் – இது இந்த பாடத்துக்கான Development container ஐ தேர்ந்தெடுக்கும்
4. **Create codespace** คลிக் செய்யவும்
5. சுற்றுச்சூழல் தயார் செய்ய ~2 நிமிடங்கள் காத்திருக்கவும்
6. நேரடியாக [முதற் உதாரணத்துக்கு](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) செல்லவும்

> **உள் கணினியில் கிளோன் செய்ய விரும்புகிறீர்களா?**

> இந்த நிரல்பெட்டி 50+ மொழிபெயர்ப்புகளை உள்ளடக்கியதால் பதிவிறக்கம் அளவு அதிகரிக்கிறது. மொழிபெயர்ப்பு இல்லாமல் கிளோன் செய்ய sparse checkout பயன்படுத்தவும்:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> இது பாடம் முடிக்க வேண்டிய அனைத்தையும் வேகமான பதிவிறக்கத்துடன் வழங்குகிறது.


## பன்மொழி ஆதரவு

### GitHub செயல் மூலம் ஆதரவு (தானாகவும் எப்போதும் புதுப்பிக்கப்படும்)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[அரபி](../ar/README.md) | [பெங்காலி](../bn/README.md) | [பல்கேரியன்](../bg/README.md) | [புர்மீஸ் (மியான்மார்)](../my/README.md) | [சீனம் (சமீபத்திய)](../zh/README.md) | [சீனம் (பாரம்பரிய, ஹாங்காங்)](../hk/README.md) | [சீனம் (பாரம்பரிய, மகாவு)](../mo/README.md) | [சீனம் (பாரம்பரிய, தைவான்)](../tw/README.md) | [குரோஷியன்](../hr/README.md) | [செக்](../cs/README.md) | [டேனிஷ்](../da/README.md) | [டச்சு](../nl/README.md) | [எச்டோனியன்](../et/README.md) | [பின்னிஷ்](../fi/README.md) | [பிரெஞ்சு](../fr/README.md) | [ஜெர்மன்](../de/README.md) | [கிரேக்கு](../el/README.md) | [ஹீப்ரூ](../he/README.md) | [இந்தி](../hi/README.md) | [ஹங்கேரியன்](../hu/README.md) | [இந்தோனேஷியன்](../id/README.md) | [இத்தாலியன்](../it/README.md) | [ஜாப்பனீஸ்](../ja/README.md) | [கன்னடம்](../kn/README.md) | [கொரியன்](../ko/README.md) | [லித்துவேனியன்](../lt/README.md) | [மலாய்](../ms/README.md) | [மலையாளம்](../ml/README.md) | [மராத்தி](../mr/README.md) | [நெபாளி](../ne/README.md) | [நைஜீரியன் பிட்ஜின்](../pcm/README.md) | [நார்வேஜியன்](../no/README.md) | [பெர்சியன் (பார்சி)](../fa/README.md) | [போலிஷ்](../pl/README.md) | [போர்ச்சுகீசு (பிரேசில்)](../br/README.md) | [போர்ச்சுகீசு (போர்ச்சுகல்)](../pt/README.md) | [பஞ்சாபி (குருமுகி)](../pa/README.md) | [ரோமாகியன்](../ro/README.md) | [ரஷ்யன்](../ru/README.md) | [செர்பியன் (சிரிலிக்)](../sr/README.md) | [ஸ்லோவாக்](../sk/README.md) | [ஸ்லோவேனியன்](../sl/README.md) | [ஸ்பானிஷ்](../es/README.md) | [ஸ்வாஹிலி](../sw/README.md) | [ஸ்வீடியன்](../sv/README.md) | [தாகாலோக் (பிலிப்பைனோ)](../tl/README.md) | [தமிழ்](./README.md) | [தெலுங்கு](../te/README.md) | [தை](../th/README.md) | [துருக்கிஷ்](../tr/README.md) | [உக்ரைனியன்](../uk/README.md) | [உருது](../ur/README.md) | [வியட்நாம்](../vi/README.md)

> **உள் கணினியில் கிளோன் செய்ய விரும்புகிறீர்களா?**

> இந்த நிரல்பெட்டி 50+ மொழிபெயர்ப்புகளை உள்ளடக்கியதால் பதிவிறக்கம் அளவு அதிகரிக்கிறது. மொழிபெயர்ப்பு இல்லாமல் கிளோன் செய்ய sparse checkout பயன்படுத்தவும்:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> இது பாடம் முடிக்க வேண்டிய எதையும் வேகமான பதிவிறக்கத்துடன் வழங்குகிறது.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## பாடத் தொகுப்பு மற்றும் கற்றல் பாதை

### **அத்தியாயம் 1: உருவாக்கும் AI அறிமுகம்**
- **மென்பொருள் கருத்துக்கள்**: பெரிய மொழி மாதிரிகள், tokens, embeddings மற்றும் AI திறன்களை புரிந்துகொள்வது
- **ஜாவா AI சூழல்**: Spring AI மற்றும் OpenAI SDK களின் அதிருப்தி
- **மாதிரி உள்ளடக்க நெறிமுறை**: MCP அறிமுகம் மற்றும் AI முகவர் தொடர்பில் அதன் பங்கு
- **பயன்பாட்டு செயலிகள்**: உரையாடலர்கள் மற்றும் உள்ளடக்க உருவாக்கம் உட்பட நடைமுறை சம்பவங்கள்
- **[→ அத்தியாயம் 1 தொடங்கு](./01-IntroToGenAI/README.md)**

### **அத்தியாயம் 2: வளர்ச்சி சூழல் அமைப்பு**
- **பன்முக வழங்குனர் கட்டமைப்பு**: GitHub மாதிரிகள், Azure OpenAI மற்றும் OpenAI Java SDK ஒருங்கிணைப்புகள் அமைத்தல்
- **Spring Boot + Spring AI**: நிறுவன AI பயன்பாட்டு மேம்பாட்டுக்கான சிறந்த நடைமுறைகள்
- **GitHub மாதிரிகள்**: மாதிரிப்பெறல் மற்றும் கற்றலுக்கான இலவச AI மாதிரி அணுகல் (கடன் அட்டை தேவையில்லை)
- ** மேம்பாட்டு கருவிகள்**: டாக்கர் கொண்டாளிகள், VS Code மற்றும் GitHub Codespaces கட்டமைப்பு
- **[→ அத்தியாயம் 2 தொடங்கு](./02-SetupDevEnvironment/README.md)**

### **அத்தியாயம் 3: முக்கிய உருவாக்கும் AI தொழில்நுட்பங்கள்**
- **உதவி விவசாயம்**: சிறந்த AI மாதிரி பதில்களை பெறுவதற்கான தொழில்நுட்பங்கள்
- **Embeddings & வெக்டர் செயலிகள்**: அர்த்தமுள்ள தேடல் மற்றும் ஒத்திசைவு பொருந்துகையை நடைமுறைப்படுத்தல்
- **Retrieval-Augmented Generation (RAG)**: AI யை உங்கள் சொந்த தரவு மூலங்களுடன் இணைத்தல்
- **செயல்பாட்டு அழைப்பு**: தனிப்பயன் கருவிகள் மற்றும் கூடுதல்கள் மூலம் AI திறன்களை விரிவாக்குதல்
- **[→ அத்தியாயம் 3 தொடங்கு](./03-CoreGenerativeAITechniques/README.md)**

### **அத்தியாயம் 4: நடைமுறை பயன்பாடுகள் மற்றும் திட்டங்கள்**
- **விலங்கு கதை உருவாக்கி** (`petstory/`): GitHub மாதிரிகளோடு படைப்பாற்றல் உள்ளடக்கம் உருவாக்கல்
- **Foundry உள்ளூர் டெமோ** (`foundrylocal/`): OpenAI ஜாவா SDK உடன் உள்ளூர் AI மாதிரி ஒருங்கிணைப்பு
- **MCP கணக்கிடுபவர் சேவை** (`calculator/`): Spring AI உடன் அடிப்படை மாதிரி உள்ளடக்க நெறிமுறை செயலாக்கம்
- **[→ அத்தியாயம் 4 தொடங்கு](./04-PracticalSamples/README.md)**

### **அத்தியாயம் 5: பொறுப்பான AI மேம்பாடு**
- **GitHub மாதிரி பாதுகாப்பு**: உள்ளடக்க வடிகட்டல் மற்றும் பாதுகாப்பு செயல்முறைகளை (கடின தடைகள் மற்றும் மெல்லிய மறுத்துக் கொள்கைகள்) சோதனை செய்வது
- **பொறுப்பான AI டெமோ**: நவீன AI பாதுகாப்பு அமைப்புகளின் செயற்பாடுகளை கையில் கற்றல்
- **சிறந்த நடைமுறைகள்**: ஒழுக்கமான AI மேம்பாடு மற்றும் நிறுவல் வழிகாட்டுதல்கள்
- **[→ அத்தியாயம் 5 தொடங்கு](./05-ResponsibleGenAI/README.md)**

## கூடுதலான வளங்கள்

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / முகவர்கள்
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### உருவாக்கும் AI தொடர்
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### முதன்மை கற்றல்
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பிக்கும் IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![ஆரம்பிக்கும் XR Development](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot தொடர்
[![AI ஜோடி திட்டமிடலுக்கான Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET-க்கான Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot சாகசம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவியைப் பெறுதல்

AI பயன்பாடுகளை உருவாக்க ஆயத்தமடையாதீர்கள் அல்லது உங்களுக்குத் தெரியாத ஏதேனும் கேள்விகள் இருந்தால். MCP-ஐப் பற்றிய விவாதங்களில் மற்ற பயில்கையாளர்களும் அனுபவமான டெவலப்பர்களும் சேருங்கள். இது ஒரு ஆதரவான சமூகமாகும், இங்கு கேள்விகள் வரவேற்கப்படுகின்றன மற்றும் அறிவு சுதந்திரமாக பகிரப்படுகின்றது.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

தயாரிப்பு கருத்துகள் அல்லது பிழைகள் இருந்தால் கட்டியெழுதல் போது பார்வையிடவும்:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**கருத்து மறுப்பு**:  
இந்த ஆவணம் AI மொழி மாற்ற சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) பயன்படுத்தி மொழிபெயர்க்கப்பட்டுள்ளது. நாங்கள் துல்லியத்திற்காக முயற்சித்தும் இருந்தாலும், தானியங்கி மொழி மாற்றங்களில் பிழைகள் அல்லது தவறுகள் இருக்கக்கூடும் என்பதை நினைவில் கொள்ளவும். அசல் ஆவணம் அதன் தாய்மொழியில் அதிகாரப்பூர்வமாக கருதப்பட வேண்டும். முக்கியமான தகவலுக்காக, தொழில்முறை மனித மொழிபெயர்ப்பை பரிந்துரைக்கிறோம். இந்த மொழி மாற்றத்தைப் பயன்படுத்தியதன் காரணமாக ஏற்படும் எந்தவொரு தவறான புரிதலுக்கு அல்லது தவறான விளக்கத்திற்கு நாங்கள் பொறுப்பாக இருக்கமாட்டோம்.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->