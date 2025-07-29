<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:52:51+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "hi"
}
-->
# जेनरेटिव AI के लिए जावा डेवलपमेंट एनवायरनमेंट सेटअप करना

> **त्वरित शुरुआत**: क्लाउड में कोडिंग शुरू करें सिर्फ 2 मिनट में - [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) पर जाएं - कोई लोकल इंस्टॉलेशन की आवश्यकता नहीं और GitHub मॉडल्स का उपयोग करें!

> **Azure OpenAI में रुचि रखते हैं?**, हमारे [Azure OpenAI Setup Guide](getting-started-azure-openai.md) को देखें जिसमें नया Azure OpenAI संसाधन बनाने के चरण दिए गए हैं।

## आप क्या सीखेंगे

- AI एप्लिकेशन के लिए जावा डेवलपमेंट एनवायरनमेंट सेटअप करना
- अपनी पसंद का डेवलपमेंट एनवायरनमेंट चुनना और कॉन्फ़िगर करना (Codespaces के साथ क्लाउड-फर्स्ट, लोकल डेव कंटेनर, या पूरी लोकल सेटअप)
- GitHub मॉडल्स से कनेक्ट करके अपने सेटअप का परीक्षण करना

## सामग्री की तालिका

- [आप क्या सीखेंगे](../../../02-SetupDevEnvironment)
- [परिचय](../../../02-SetupDevEnvironment)
- [चरण 1: अपना डेवलपमेंट एनवायरनमेंट सेटअप करें](../../../02-SetupDevEnvironment)
  - [विकल्प A: GitHub Codespaces (अनुशंसित)](../../../02-SetupDevEnvironment)
  - [विकल्प B: लोकल डेव कंटेनर](../../../02-SetupDevEnvironment)
  - [विकल्प C: अपनी मौजूदा लोकल इंस्टॉलेशन का उपयोग करें](../../../02-SetupDevEnvironment)
- [चरण 2: GitHub पर्सनल एक्सेस टोकन बनाएं](../../../02-SetupDevEnvironment)
- [चरण 3: अपने सेटअप का परीक्षण करें](../../../02-SetupDevEnvironment)
- [समस्या निवारण](../../../02-SetupDevEnvironment)
- [सारांश](../../../02-SetupDevEnvironment)
- [अगले कदम](../../../02-SetupDevEnvironment)

## परिचय

यह अध्याय आपको डेवलपमेंट एनवायरनमेंट सेटअप करने में मार्गदर्शन करेगा। हम **GitHub मॉडल्स** का उपयोग करेंगे क्योंकि यह मुफ़्त है, केवल GitHub अकाउंट के साथ आसानी से सेटअप किया जा सकता है, क्रेडिट कार्ड की आवश्यकता नहीं है, और कई मॉडल्स तक पहुंच प्रदान करता है।

**कोई लोकल सेटअप की आवश्यकता नहीं!** आप तुरंत कोडिंग शुरू कर सकते हैं GitHub Codespaces का उपयोग करके, जो आपके ब्राउज़र में एक पूर्ण डेवलपमेंट एनवायरनमेंट प्रदान करता है।

<img src="./images/models.webp" alt="स्क्रीनशॉट: GitHub मॉडल्स" width="50%">

हम इस कोर्स के लिए [**GitHub मॉडल्स**](https://github.com/marketplace?type=models) का उपयोग करने की अनुशंसा करते हैं क्योंकि यह:
- **मुफ़्त** है शुरू करने के लिए
- **आसान** है केवल GitHub अकाउंट के साथ सेटअप करने के लिए
- **कोई क्रेडिट कार्ड** की आवश्यकता नहीं
- **कई मॉडल्स** प्रयोग के लिए उपलब्ध हैं

> **नोट**: इस ट्रेनिंग में उपयोग किए गए GitHub मॉडल्स के मुफ़्त लिमिट्स:
> - 15 अनुरोध प्रति मिनट (150 प्रति दिन)
> - ~8,000 शब्द इनपुट, ~4,000 शब्द आउटपुट प्रति अनुरोध
> - 5 समवर्ती अनुरोध
> 
> प्रोडक्शन उपयोग के लिए, Azure AI Foundry मॉडल्स में अपग्रेड करें अपने Azure अकाउंट के साथ। आपका कोड बदलने की आवश्यकता नहीं है। [Azure AI Foundry दस्तावेज़](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) देखें।

## चरण 1: अपना डेवलपमेंट एनवायरनमेंट सेटअप करें

<a name="quick-start-cloud"></a>

हमने इस जेनरेटिव AI फॉर जावा कोर्स के लिए आवश्यक सभी टूल्स के साथ एक प्री-कॉन्फ़िगर्ड डेवलपमेंट कंटेनर बनाया है ताकि सेटअप समय कम हो सके। अपनी पसंद का डेवलपमेंट तरीका चुनें:

### एनवायरनमेंट सेटअप विकल्प:

#### विकल्प A: GitHub Codespaces (अनुशंसित)

**2 मिनट में कोडिंग शुरू करें - कोई लोकल सेटअप की आवश्यकता नहीं!**

1. इस रिपॉजिटरी को अपने GitHub अकाउंट में फोर्क करें
   > **नोट**: यदि आप बेसिक कॉन्फ़िगरेशन को एडिट करना चाहते हैं तो [Dev Container Configuration](../../../.devcontainer/devcontainer.json) देखें।
2. **Code** → **Codespaces** टैब → **...** → **New with options...** पर क्लिक करें।
3. डिफ़ॉल्ट्स का उपयोग करें – यह **Dev container configuration** का चयन करेगा: **Generative AI Java Development Environment** इस कोर्स के लिए बनाया गया कस्टम डेवकंटेनर।
4. **Create codespace** पर क्लिक करें।
5. एनवायरनमेंट तैयार होने के लिए ~2 मिनट प्रतीक्षा करें।
6. [चरण 2: GitHub टोकन बनाएं](../../../02-SetupDevEnvironment) पर जाएं।

<img src="./images/codespaces.png" alt="स्क्रीनशॉट: Codespaces सबमेनू" width="50%">

<img src="./images/image.png" alt="स्क्रीनशॉट: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="स्क्रीनशॉट: Create codespace options" width="50%">


> **Codespaces के लाभ**:
> - कोई लोकल इंस्टॉलेशन की आवश्यकता नहीं
> - किसी भी डिवाइस पर ब्राउज़र के साथ काम करता है
> - सभी टूल्स और डिपेंडेंसीज़ के साथ प्री-कॉन्फ़िगर्ड
> - व्यक्तिगत अकाउंट्स के लिए प्रति माह 60 घंटे मुफ़्त
> - सभी शिक्षार्थियों के लिए एक समान एनवायरनमेंट

#### विकल्प B: लोकल डेव कंटेनर

**उन डेवलपर्स के लिए जो Docker के साथ लोकल डेवलपमेंट पसंद करते हैं**

1. इस रिपॉजिटरी को अपने लोकल मशीन पर फोर्क और क्लोन करें।
   > **नोट**: यदि आप बेसिक कॉन्फ़िगरेशन को एडिट करना चाहते हैं तो [Dev Container Configuration](../../../.devcontainer/devcontainer.json) देखें।
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) और [VS Code](https://code.visualstudio.com/) इंस्टॉल करें।
3. VS Code में [Dev Containers एक्सटेंशन](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) इंस्टॉल करें।
4. रिपॉजिटरी फ़ोल्डर को VS Code में खोलें।
5. जब संकेत दिया जाए, **Reopen in Container** पर क्लिक करें (या `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" का उपयोग करें)।
6. कंटेनर के निर्माण और शुरू होने की प्रतीक्षा करें।
7. [चरण 2: GitHub टोकन बनाएं](../../../02-SetupDevEnvironment) पर जाएं।

<img src="./images/devcontainer.png" alt="स्क्रीनशॉट: Dev container सेटअप" width="50%">

<img src="./images/image-3.png" alt="स्क्रीनशॉट: Dev container निर्माण पूरा" width="50%">

#### विकल्प C: अपनी मौजूदा लोकल इंस्टॉलेशन का उपयोग करें

**उन डेवलपर्स के लिए जिनके पास मौजूदा जावा एनवायरनमेंट है**

पूर्वापेक्षाएँ:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) या आपका पसंदीदा IDE

चरण:
1. इस रिपॉजिटरी को अपने लोकल मशीन पर क्लोन करें।
2. प्रोजेक्ट को अपने IDE में खोलें।
3. [चरण 2: GitHub टोकन बनाएं](../../../02-SetupDevEnvironment) पर जाएं।

> **प्रो टिप**: यदि आपके पास लो-स्पेक मशीन है लेकिन आप लोकल VS Code चाहते हैं, तो GitHub Codespaces का उपयोग करें! आप अपने लोकल VS Code को क्लाउड-होस्टेड Codespace से कनेक्ट कर सकते हैं।

<img src="./images/image-2.png" alt="स्क्रीनशॉट: लोकल डेवकंटेनर इंस्टेंस बनाया गया" width="50%">


## चरण 2: GitHub पर्सनल एक्सेस टोकन बनाएं

1. [GitHub Settings](https://github.com/settings/profile) पर जाएं और अपने प्रोफाइल मेनू से **Settings** चुनें।
2. बाएं साइडबार में, **Developer settings** पर क्लिक करें (आमतौर पर नीचे की ओर)।
3. **Personal access tokens** के तहत, **Fine-grained tokens** पर क्लिक करें (या इस [लिंक](https://github.com/settings/personal-access-tokens) का अनुसरण करें)।
4. **Generate new token** पर क्लिक करें।
5. "Token name" के तहत, एक वर्णनात्मक नाम प्रदान करें (जैसे, `GenAI-Java-Course-Token`)।
6. समाप्ति तिथि सेट करें (सुरक्षा सर्वोत्तम प्रथाओं के लिए अनुशंसित: 7 दिन)।
7. "Resource owner" के तहत, अपना उपयोगकर्ता खाता चुनें।
8. "Repository access" के तहत, उन रिपॉजिटरीज़ को चुनें जिन्हें आप GitHub मॉडल्स के साथ उपयोग करना चाहते हैं (या "All repositories" यदि आवश्यक हो)।
9. "Repository permissions" के तहत, **Models** खोजें और इसे **Read and write** पर सेट करें।
10. **Generate token** पर क्लिक करें।
11. **अपना टोकन अभी कॉपी और सेव करें** – आप इसे फिर से नहीं देख पाएंगे!

> **सुरक्षा टिप**: अपने एक्सेस टोकन के लिए न्यूनतम आवश्यक स्कोप और सबसे छोटी व्यावहारिक समाप्ति समय का उपयोग करें।

## चरण 3: GitHub मॉडल्स उदाहरण के साथ अपने सेटअप का परीक्षण करें

एक बार आपका डेवलपमेंट एनवायरनमेंट तैयार हो जाए, तो चलिए GitHub मॉडल्स इंटीग्रेशन को हमारे उदाहरण एप्लिकेशन के साथ परीक्षण करते हैं [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models)।

1. अपने डेवलपमेंट एनवायरनमेंट में टर्मिनल खोलें।
2. GitHub मॉडल्स उदाहरण पर जाएं:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. अपने GitHub टोकन को एक एनवायरनमेंट वेरिएबल के रूप में सेट करें:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. एप्लिकेशन चलाएं:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

आपको इस तरह का आउटपुट दिखाई देना चाहिए:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### उदाहरण कोड को समझना

पहले, चलिए समझते हैं कि हमने अभी क्या चलाया। `examples/github-models` के तहत उदाहरण OpenAI Java SDK का उपयोग करता है GitHub मॉडल्स से कनेक्ट करने के लिए:

**यह कोड क्या करता है:**
- **कनेक्ट** करता है GitHub मॉडल्स से आपके पर्सनल एक्सेस टोकन का उपयोग करके
- **भेजता है** एक साधारण "Say Hello World!" संदेश AI मॉडल को
- **प्राप्त करता है** और AI का उत्तर प्रदर्शित करता है
- **पुष्टि करता है** कि आपका सेटअप सही तरीके से काम कर रहा है

**मुख्य डिपेंडेंसी** (`pom.xml` में):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**मुख्य कोड** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## सारांश

शानदार! अब आपके पास सब कुछ सेटअप हो गया है:

- AI मॉडल एक्सेस के लिए सही अनुमतियों के साथ GitHub पर्सनल एक्सेस टोकन बनाया
- अपना जावा डेवलपमेंट एनवायरनमेंट चालू किया (चाहे वह Codespaces हो, डेव कंटेनर हो, या लोकल)
- OpenAI Java SDK का उपयोग करके GitHub मॉडल्स से कनेक्ट किया मुफ्त AI डेवलपमेंट के लिए
- एक साधारण उदाहरण के साथ परीक्षण किया जो AI मॉडल्स से बात करता है

## अगले कदम

[अध्याय 3: कोर जेनरेटिव AI तकनीकें](../03-CoreGenerativeAITechniques/README.md)

## समस्या निवारण

समस्याएँ आ रही हैं? यहाँ सामान्य समस्याएँ और उनके समाधान दिए गए हैं:

- **टोकन काम नहीं कर रहा?** 
  - सुनिश्चित करें कि आपने पूरा टोकन बिना किसी अतिरिक्त स्पेस के कॉपी किया है।
  - सत्यापित करें कि टोकन सही तरीके से एनवायरनमेंट वेरिएबल के रूप में सेट है।
  - जांचें कि आपके टोकन में सही अनुमतियाँ हैं (Models: Read and write)।

- **Maven नहीं मिला?** 
  - यदि आप डेव कंटेनर/Codespaces का उपयोग कर रहे हैं, तो Maven पहले से इंस्टॉल होना चाहिए।
  - लोकल सेटअप के लिए, सुनिश्चित करें कि Java 21+ और Maven 3.9+ इंस्टॉल हैं।
  - इंस्टॉलेशन सत्यापित करने के लिए `mvn --version` आज़माएं।

- **कनेक्शन समस्याएँ?** 
  - अपना इंटरनेट कनेक्शन जांचें।
  - सत्यापित करें कि GitHub आपके नेटवर्क से सुलभ है।
  - सुनिश्चित करें कि आप किसी फ़ायरवॉल के पीछे नहीं हैं जो GitHub मॉडल्स एंडपॉइंट को ब्लॉक कर रहा है।

- **डेव कंटेनर शुरू नहीं हो रहा?** 
  - सुनिश्चित करें कि Docker Desktop चालू है (लोकल डेवलपमेंट के लिए)।
  - कंटेनर को फिर से बनाने का प्रयास करें: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"।

- **एप्लिकेशन संकलन त्रुटियाँ?**
  - सुनिश्चित करें कि आप सही डायरेक्टरी में हैं: `02-SetupDevEnvironment/examples/github-models`
  - साफ़ करें और फिर से बनाएं: `mvn clean compile`

> **मदद चाहिए?**: अभी भी समस्याएँ आ रही हैं? रिपॉजिटरी में एक इश्यू खोलें और हम आपकी मदद करेंगे।

**अस्वीकरण**:  
यह दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके अनुवादित किया गया है। जबकि हम सटीकता सुनिश्चित करने का प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवाद में त्रुटियां या अशुद्धियां हो सकती हैं। मूल भाषा में उपलब्ध मूल दस्तावेज़ को आधिकारिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।