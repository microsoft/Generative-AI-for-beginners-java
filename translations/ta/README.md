# துவக்கக் கருவிகளுக்கு உருவாக்கும் AI - ஜாவா பதிப்பு
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![துவக்கக் கருவிகளுக்கு உருவாக்கும் AI - ஜாவா பதிப்பு](../../translated_images/ta/beg-genai-series.8b48be9951cc574c.webp)

**நேர ஒதுக்கீடு**: முழு பயிற்சி மையம் உள்ளூர் அமைப்பு இல்லாமல் ஆன்லைனில் முடிக்க முடியும். சுற்றுப்புற அமைப்பு செய்ய 2 நிமிடங்கள் ஆகும், மாதிரிகளை ஆராய 1-3 மணி நேரம் தேவைப்படும், ஆராய்ச்சி ஆழத்துக்கு அமைவாக.

> **விரைவு தொடக்கம்** 

1. இந்த சேமிப்பகத்தை உங்கள் GitHub கணக்கிற்கு வெட்டி எடுக்கவும்
2. **Code** → **Codespaces** தாவலுக்கு கிளிக் செய்யவும் → **...** → **New with options...** பொத்தானை அழுத்தவும்
3. இயல்புக்கள் பயன்படுத்தவும் – இது இந்த பாடத்திற்கான கணினி சுற்றுப்புற அமைப்பை தேர்வு செய்யும்
4. **Create codespace** என்பதில் கிளிக் செய்யவும்
5. சூழல் தயார் செய்ய ~2 நிமிடங்கள் காத்திருங்கள்
6. நேரடியாக [முதலாவது எடுத்துக்காட்டு](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)க்கு செல்லவும்

## பல்மொழி ஆதரவு

### GitHub செயல் மூலம் ஆதரவு (தானியங்கி & எப்போதும் புதுப்பிக்கப்பட்டது)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[அரபு](../ar/README.md) | [பெங்காலி](../bn/README.md) | [பல்கேரியன்](../bg/README.md) | [பர்மீஸ் (மியான்மர்)](../my/README.md) | [சீனம் (எளிமைப்படுத்திய)](../zh-CN/README.md) | [சீனம் (பாரம்பரிய, ஹாங்காங்)](../zh-HK/README.md) | [சீனம் (பாரம்பரிய, மகाऊ)](../zh-MO/README.md) | [சீனம் (பாரம்பரிய, தைவான்)](../zh-TW/README.md) | [குரோஷியன்](../hr/README.md) | [செக்](../cs/README.md) | [டேனிஷ்](../da/README.md) | [டச்சு](../nl/README.md) | [எஸ்டோனியன்](../et/README.md) | [பினிஷ்](../fi/README.md) | [பிரஞ்சு](../fr/README.md) | [ஜெர்மன்](../de/README.md) | [கிரேக்கம்](../el/README.md) | [ஹீப்ரூ](../he/README.md) | [ஹிந்தி](../hi/README.md) | [ஹங்கேரியன்](../hu/README.md) | [இந்தோனேஷியன்](../id/README.md) | [இத்தாலியன்](../it/README.md) | [ஜப்பானீஸ்](../ja/README.md) | [கன்னடம்](../kn/README.md) | [கொரியன்](../ko/README.md) | [லிதுவேனியன்](../lt/README.md) | [மலாய்](../ms/README.md) | [மலையாளம்](../ml/README.md) | [மராத்தி](../mr/README.md) | [நேபாளி](../ne/README.md) | [நைஜீரியன் பிட்ஜின்](../pcm/README.md) | [நார்வேஜியன்](../no/README.md) | [பேரசீயம் (ஃபார்சி)](../fa/README.md) | [போலிஷ்](../pl/README.md) | [போர்ச்சுகீஸ் (பிரேசில்)](../pt-BR/README.md) | [போர்ச்சுகீஸ் (போர்ச்சுகல்)](../pt-PT/README.md) | [பஞ்சாபி (குருமுகி)](../pa/README.md) | [ரோமானியன்](../ro/README.md) | [ரஷியன்](../ru/README.md) | [செர்பியன் (சிரிலிக்)](../sr/README.md) | [ஸ்லோவாக்](../sk/README.md) | [ஸ்லோவேனி](../sl/README.md) | [இஸ்பானிய](../es/README.md) | [ஸ்வாஹிலி](../sw/README.md) | [ஸ்வீடிஷ்](../sv/README.md) | [டாகாலோக் (பிலிப்பைனோ)](../tl/README.md) | [தமிழ்](./README.md) | [तेलுங்கு](../te/README.md) | [தாய்](../th/README.md) | [துருக்கிய](../tr/README.md) | [உக்ரைனியன்](../uk/README.md) | [உருது](../ur/README.md) | [வியட்நாமீஸ்](../vi/README.md)

> **உள்ளூரில் க்ளோன் செய்ய விருப்பமா?**
>
> இந்த சேமிப்பகம் 50+ மொழி மொழிபெயர்ப்புகளைக் கொண்டுள்ளது, இது பதிவிறக்கும் அளவை அதிகரிக்கிறது. மொழிபெயர்புகளை இல்லாமல் க்ளோன் செய்ய sparse checkout பயன்படுத்தவும்:
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
> இதனால் நீங்கள் பாடத்தைக் முடிப்பதற்குத் தேவையான அனைத்தையும் வேகமாகப் பெறலாம்.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## பாட வகுப்பு அமைப்பு & கற்றல் பாதை

### **அத்தியாயம் 1: உருவாக்கும் AI அறிமுகம்**
- **முக்கிய கருத்துக்கள்**: பெரிய மொழி மொழி மாடல்கள், டோக்கன்கள், செருகியல்கள் மற்றும் AI திறன்கள் புரிதல்
- **ஜாவா AI சூழல்**: Spring AI மற்றும் OpenAI SDKகளின் மேற்பார்வை
- **மாடல் கான்டெக்ஸ்ட் ப்ரொட்டோக்கால்**: MCP அறிமுகம் மற்றும் AI ஏஜென்ட் தொடர்பு பாதை
- **நிகழ்ச்சியின் பயன்பாடுகள்**: உரையாடல் பொறிகள் மற்றும் உள்ளடக்க உருவாக்கம் போன்ற வாழ்க்கை நிகழ்வுகள்
- **[→ அத்தியாயம் 1 தொடக்கம்](./01-IntroToGenAI/README.md)**

### **அத்தியாயம் 2: வளர்ச்சியியல் சூழல் அமைத்தல்**
- **பல வழங்குநர் கட்டமைப்பு**: GitHub Models, Azure OpenAI மற்றும் OpenAI Java SDK ஒருங்கிணைப்பு
- **Spring Boot + Spring AI**: நிறுவன AI பயன்பாட்டு மேம்பாட்டு சிறந்த நடைமுறைகள்
- **GitHub Models**: கலைப்படைப்பு மற்றும் கற்றலுக்கு இலவச AI மாடல் அணுகல் (கடன் அட்டை தேவையில்லை)
- **வளர்ச்சி கருவிகள்**: Docker கன்டெய்னர்கள், VS Code, மற்றும் GitHub Codespaces அமைப்பு
- **[→ அத்தியாயம் 2 தொடக்கம்](./02-SetupDevEnvironment/README.md)**

### **அத்தியாயம் 3: முக்கிய உருவாக்கும் AI நுட்பங்கள்**
- **பிராம்ட் பொறியியல்**: AI மாடல் சிறந்த பதில்களுக்கான சுற்றியுடன் வேலை செய்வது
- **செருகியல்கள் & வெக்டர் செயல்பாடுகள்**: அர்த்தமுள்ளத் தேடல் மற்றும் ஒப்பீட்டு பொருத்தல்
- **திரும்ப-திருவி உருவாக்கல் (RAG)**: உங்கள் சொந்த தரவுத்தளங்களுடன் AI ஐ இணைத்தல்
- **செயல் அழைப்பு**: தனிப்பயன் கருவிகள் மற்றும் பிளகின்களுடன் AI திறன்களை விரிவாக்குதல்
- **[→ அத்தியாயம் 3 தொடக்கம்](./03-CoreGenerativeAITechniques/README.md)**

### **அத்தியாயம் 4: நடைமுறை பயன்பாடுகள் & திட்டங்கள்**
- **பேட் கதை உருவாக்கி** (`petstory/`): GitHub Models உடன் படைப்பு உள்ளடக்கம் உருவாக்கல்
- **Foundry உள்ளூர் டெமோ** (`foundrylocal/`): OpenAI Java SDK உடன் உள்ளூர் AI மாடல் ஒருங்கிணைப்பு
- **MCP கணக்கீட்டு சேவை** (`calculator/`): Spring AI ஆல் அடிப்படையான மாடல் கான்டெக்ஸ்ட் ப்ரொட்டோக்கால் நடைமுறை
- **[→ அத்தியாயம் 4 தொடக்கம்](./04-PracticalSamples/README.md)**

### **அத்தியாயம் 5: பொறுப்புடன் AI உருவாக்கல்**
- **GitHub Models பாதுகாப்பு**: உள்ளமைக்கப்பட்ட உள்ளடக்க ஆட்சி மற்றும் பாதுகாப்பு முறைகளை பரிசோதனை (கடுமையான தடைகள் மற்றும் மெல்லிய மறுப்பு)
- **பொறுப்பான AI டெமோ**: இன்றைய AI பாதுகாப்பு முறைகள் நடைமுறையில் எப்படி செயல்படுகின்றன என்பதைக் கையாளும் கையடக்க எடுத்துக்காட்டு
- **சிறந்த நடைமுறைகள்**: ஒழுக்கமான AI மேம்பாடு மற்றும் நடைமுறைக்கு அவசியமான வழிகாட்டிகள்
- **[→ அத்தியாயம் 5 தொடக்கம்](./05-ResponsibleGenAI/README.md)**

## கூடுதல் வளங்கள்

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### லாங்செயின்
[![துவக்கங்களுக்கான LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![துவக்கங்களுக்கான LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![துவக்கங்களுக்கான LangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### அஜ்யூர் / எட்ஜ் / MCP / ஏஜென்ட்ஸ்
[![துவக்கங்களுக்கான AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![துவக்கங்களுக்கான எட்ஜ் AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![துவக்கங்களுக்கான MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![துவக்கங்களுக்கான AI ஏஜென்ட்ஸ்](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### உருவாக்கும் AI தொடர்
[![துவக்கங்களுக்கான உருவாக்கும் AI](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![உருவாக்கும் AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![உருவாக்கும் AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![உருவாக்கும் AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### முக்கிய கற்றல்
[![துவக்கங்களுக்கான ML](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![துவக்கங்களுக்கான தரவு அறிவியல்](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![துவக்கங்களுக்கான AI](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![துவக்கங்களுக்கான சைபர்செக்யூரிட்டி](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![புதியவர்களுக்கு வலைப்பரிகசனம்](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கு IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![புதியவர்களுக்கு XR மேம்பாடு](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---

### கோபைலட் தொடர்
[![AI கூட்டணிப் நிரலாக்கத்திற்கு கோபைலட்](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NETக்கு கோபைலட்](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![கொப்பைலட் சாகசம்](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## உதவி பெறுதல்

AI செயலிகளை உருவாக்கும் பொழுது நீங்கள் தொந்தரவு அடைந்தால் அல்லது கேள்விகள் இருந்தால். MCP பற்றி fellow learners மற்றும் அனுபவமுள்ள டெவலப்பர்கள் உடன் விவாதங்களில் கலந்து கொள்ளுங்கள். அங்கு கேள்விகள் வரவேற்கப்படுகின்றன மற்றும் அறிவு சுதந்திரமாக பகிரப்படுகின்றது என்பது ஆதரவு சமூகம்.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

உற்பத்தியின் கருத்து அல்லது பிழைகள் இருந்தால் இங்கு சென்று பாருங்கள்:

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**எச்சரிக்கை**:
இந்த ஆவணம் AI மொழிபெயர்ப்புச் சேவை [Co-op Translator](https://github.com/Azure/co-op-translator) பயன்படுத்தி மொழிபெயர்க்கப்பட்டது. துல்லியத்திற்காக நாம் முயற்சி செய்தாலும், தானாக உருவாக்கப்பட்ட மொழிபெயர்ப்பில் பிழைகள் அல்லது தவறுகள் இருக்க வாய்ப்பு உள்ளது என்பதை கவனத்தில் கொள்ளவும். அசல் ஆவணம் அதன் தாய்மொழியில் தான் அதிகாரப்பூர்வமான ஆதாரமாக கருதப்பட வேண்டும். முக்கியமான தகவலுக்கு, தொழில்முறை மனித மொழிபெயர்ப்பு பரிந்துரைக்கப்படுகிறது. இந்த மொழிபெயர்ப்பைப் பயன்படுத்துவதால் ஏற்படும் எந்த தவறான புரிதல்கள் அல்லது தவறான விளக்கங்களுக்கும் நாங்கள் பொறுப்பேற்கவில்லை.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->