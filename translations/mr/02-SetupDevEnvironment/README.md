<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:00:09+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "mr"
}
-->
# जावा साठी जनरेटिव्ह AI विकास वातावरण सेटअप करणे

> **जलद सुरुवात**: क्लाउडमध्ये 2 मिनिटांत कोड करा - [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) येथे जा - स्थानिक इंस्टॉलेशनची गरज नाही आणि GitHub मॉडेल्स वापरते!

> **Azure OpenAI मध्ये रस आहे?**, आमचे [Azure OpenAI Setup Guide](getting-started-azure-openai.md) पहा ज्यामध्ये नवीन Azure OpenAI संसाधन तयार करण्याची पावले दिली आहेत.

## तुम्ही काय शिकाल

- AI अॅप्लिकेशन्ससाठी जावा विकास वातावरण सेट अप करा
- तुमचे प्राधान्य विकास वातावरण निवडा आणि कॉन्फिगर करा (Codespaces सह क्लाउड-फर्स्ट, स्थानिक dev कंटेनर, किंवा पूर्ण स्थानिक सेटअप)
- GitHub मॉडेल्सशी कनेक्ट करून तुमचे सेटअप तपासा

## विषय सूची

- [तुम्ही काय शिकाल](../../../02-SetupDevEnvironment)
- [परिचय](../../../02-SetupDevEnvironment)
- [पायरी 1: तुमचे विकास वातावरण सेट अप करा](../../../02-SetupDevEnvironment)
  - [पर्याय A: GitHub Codespaces (शिफारस केलेले)](../../../02-SetupDevEnvironment)
  - [पर्याय B: स्थानिक Dev कंटेनर](../../../02-SetupDevEnvironment)
  - [पर्याय C: तुमची विद्यमान स्थानिक इंस्टॉलेशन वापरा](../../../02-SetupDevEnvironment)
- [पायरी 2: GitHub वैयक्तिक प्रवेश टोकन तयार करा](../../../02-SetupDevEnvironment)
- [पायरी 3: तुमचे सेटअप तपासा](../../../02-SetupDevEnvironment)
- [समस्या निवारण](../../../02-SetupDevEnvironment)
- [सारांश](../../../02-SetupDevEnvironment)
- [पुढील पावले](../../../02-SetupDevEnvironment)

## परिचय

हे प्रकरण तुम्हाला विकास वातावरण सेट अप करण्यासाठी मार्गदर्शन करेल. आम्ही **GitHub मॉडेल्स** मुख्य उदाहरण म्हणून वापरणार आहोत कारण ते विनामूल्य आहे, फक्त GitHub खात्याने सेट अप करणे सोपे आहे, क्रेडिट कार्डची आवश्यकता नाही आणि प्रयोगासाठी अनेक मॉडेल्स उपलब्ध करून देते.

**स्थानिक सेटअपची गरज नाही!** तुम्ही GitHub Codespaces वापरून त्वरित कोडिंग सुरू करू शकता, जे तुमच्या ब्राउझरमध्ये पूर्ण विकास वातावरण प्रदान करते.

<img src="./images/models.webp" alt="स्क्रीनशॉट: GitHub मॉडेल्स" width="50%">

आम्ही या कोर्ससाठी [**GitHub मॉडेल्स**](https://github.com/marketplace?type=models) वापरण्याची शिफारस करतो कारण:
- **विनामूल्य** सुरुवात करण्यासाठी
- **सोपे** फक्त GitHub खात्याने सेट अप करणे
- **क्रेडिट कार्डची आवश्यकता नाही**
- **अनेक मॉडेल्स** प्रयोगासाठी उपलब्ध

> **टीप**: या प्रशिक्षणात वापरलेले GitHub मॉडेल्सचे विनामूल्य मर्यादा:
> - 15 विनंत्या प्रति मिनिट (150 प्रति दिवस)
> - ~8,000 शब्द इनपुट, ~4,000 शब्द आउटपुट प्रति विनंती
> - 5 एकत्रित विनंत्या
> 
> उत्पादनासाठी, Azure AI Foundry मॉडेल्समध्ये अपग्रेड करा तुमच्या Azure खात्यासह. तुमचा कोड बदलण्याची गरज नाही. [Azure AI Foundry दस्तऐवज](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) पहा.

## पायरी 1: तुमचे विकास वातावरण सेट अप करा

<a name="quick-start-cloud"></a>

आम्ही या जनरेटिव्ह AI साठी जावा कोर्ससाठी आवश्यक सर्व साधने आणि साधनांसह प्री-कॉन्फिगर केलेला विकास कंटेनर तयार केला आहे. तुमचा प्राधान्य विकास दृष्टिकोन निवडा:

### पर्याय:

#### पर्याय A: GitHub Codespaces (शिफारस केलेले)

**2 मिनिटांत कोडिंग सुरू करा - स्थानिक सेटअपची गरज नाही!**

1. हे रिपॉझिटरी तुमच्या GitHub खात्यावर फोर्क करा
   > **टीप**: जर तुम्हाला बेसिक कॉन्फिग संपादित करायचे असेल तर [Dev Container Configuration](../../../.devcontainer/devcontainer.json) पहा
2. **Code** → **Codespaces** टॅब → **...** → **New with options...** वर क्लिक करा
3. डीफॉल्ट्स वापरा – हे **Dev कंटेनर कॉन्फिगरेशन** निवडेल: **Generative AI Java Development Environment** या कोर्ससाठी तयार केलेला कस्टम devcontainer
4. **Create codespace** वर क्लिक करा
5. वातावरण तयार होण्यासाठी ~2 मिनिटे प्रतीक्षा करा
6. [पायरी 2: GitHub टोकन तयार करा](../../../02-SetupDevEnvironment) वर जा

<img src="./images/codespaces.png" alt="स्क्रीनशॉट: Codespaces सबमेनू" width="50%">

<img src="./images/image.png" alt="स्क्रीनशॉट: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="स्क्रीनशॉट: Create codespace options" width="50%">


> **Codespaces चे फायदे**:
> - स्थानिक इंस्टॉलेशनची गरज नाही
> - कोणत्याही ब्राउझर असलेल्या डिव्हाइसवर कार्य करते
> - सर्व साधने आणि dependencies सह प्री-कॉन्फिगर केलेले
> - वैयक्तिक खात्यांसाठी दरमहा 60 तास विनामूल्य
> - सर्व शिकणाऱ्यांसाठी सुसंगत वातावरण

#### पर्याय B: स्थानिक Dev कंटेनर

**स्थानिक विकासाला प्राधान्य देणाऱ्या विकसकांसाठी**

1. हे रिपॉझिटरी तुमच्या स्थानिक मशीनवर फोर्क आणि क्लोन करा
   > **टीप**: जर तुम्हाला बेसिक कॉन्फिग संपादित करायचे असेल तर [Dev Container Configuration](../../../.devcontainer/devcontainer.json) पहा
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) आणि [VS Code](https://code.visualstudio.com/) इंस्टॉल करा
3. VS Code मध्ये [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) इंस्टॉल करा
4. VS Code मध्ये रिपॉझिटरी फोल्डर उघडा
5. विचारले गेल्यास, **Reopen in Container** वर क्लिक करा (किंवा `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" वापरा)
6. कंटेनर तयार होण्यासाठी आणि सुरू होण्यासाठी प्रतीक्षा करा
7. [पायरी 2: GitHub टोकन तयार करा](../../../02-SetupDevEnvironment) वर जा

<img src="./images/devcontainer.png" alt="स्क्रीनशॉट: Dev कंटेनर सेटअप" width="50%">

<img src="./images/image-3.png" alt="स्क्रीनशॉट: Dev कंटेनर तयार पूर्ण" width="50%">

#### पर्याय C: तुमची विद्यमान स्थानिक इंस्टॉलेशन वापरा

**ज्यांच्याकडे विद्यमान जावा वातावरण आहे अशा विकसकांसाठी**

पूर्वअटी:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) किंवा तुमचा प्राधान्य IDE

पावले:
1. हे रिपॉझिटरी तुमच्या स्थानिक मशीनवर क्लोन करा
2. प्रकल्प तुमच्या IDE मध्ये उघडा
3. [पायरी 2: GitHub टोकन तयार करा](../../../02-SetupDevEnvironment) वर जा

> **प्रो टिप**: जर तुमच्याकडे कमी-स्पेक मशीन असेल पण तुम्हाला VS Code स्थानिक हवे असेल, तर GitHub Codespaces वापरा! तुम्ही तुमच्या स्थानिक VS Code ला क्लाउड-होस्टेड Codespace शी कनेक्ट करू शकता.

<img src="./images/image-2.png" alt="स्क्रीनशॉट: स्थानिक devcontainer instance तयार केले" width="50%">


## पायरी 2: GitHub वैयक्तिक प्रवेश टोकन तयार करा

1. [GitHub Settings](https://github.com/settings/profile) वर जा आणि तुमच्या प्रोफाइल मेनूमधून **Settings** निवडा.
2. डाव्या साइडबारमध्ये, **Developer settings** (सामान्यतः तळाशी) क्लिक करा.
3. **Personal access tokens** अंतर्गत, **Fine-grained tokens** क्लिक करा (किंवा हा थेट [लिंक](https://github.com/settings/personal-access-tokens) वापरा).
4. **Generate new token** क्लिक करा.
5. "Token name" अंतर्गत, वर्णनात्मक नाव द्या (उदा., `GenAI-Java-Course-Token`).
6. समाप्ती तारीख सेट करा (सुरक्षिततेसाठी शिफारस: 7 दिवस).
7. "Resource owner" अंतर्गत, तुमचे वापरकर्ता खाते निवडा.
8. "Repository access" अंतर्गत, तुम्ही GitHub मॉडेल्ससह वापरू इच्छित रिपॉझिटरी निवडा (किंवा आवश्यक असल्यास "All repositories").
9. "Repository permissions" अंतर्गत, **Models** शोधा आणि **Read and write** सेट करा.
10. **Generate token** क्लिक करा.
11. **तुमचे टोकन आता कॉपी आणि सेव्ह करा** – तुम्ही ते पुन्हा पाहू शकणार नाही!

> **सुरक्षा टिप**: तुमच्या प्रवेश टोकनसाठी आवश्यक किमान स्कोप आणि सर्वात कमी व्यावहारिक समाप्ती वेळ वापरा.

## पायरी 3: GitHub मॉडेल्स उदाहरणासह तुमचे सेटअप तपासा

तुमचे विकास वातावरण तयार झाल्यावर, GitHub मॉडेल्ससह आमच्या उदाहरण अॅप्लिकेशनची चाचणी करूया [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) मध्ये.

1. तुमच्या विकास वातावरणात टर्मिनल उघडा.
2. GitHub मॉडेल्स उदाहरणावर जा:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. तुमचे GitHub टोकन पर्यावरणीय व्हेरिएबल म्हणून सेट करा:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. अॅप्लिकेशन चालवा:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

तुम्हाला खालीलप्रमाणे आउटपुट दिसेल:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### उदाहरण कोड समजून घेणे

आधी, आपण काय चालवले ते समजून घेऊया. `examples/github-models` अंतर्गत उदाहरण OpenAI Java SDK वापरून GitHub मॉडेल्सशी कनेक्ट करते:

**हा कोड काय करतो:**
- तुमच्या वैयक्तिक प्रवेश टोकनचा वापर करून GitHub मॉडेल्सशी **कनेक्ट** करतो
- AI मॉडेलला "Say Hello World!" संदेश **पाठवतो**
- AI चा प्रतिसाद **प्राप्त** करतो आणि प्रदर्शित करतो
- तुमचे सेटअप योग्यरित्या कार्य करत आहे हे **सत्यापित** करतो

**मुख्य Dependency** (`pom.xml` मध्ये):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**मुख्य कोड** (`App.java` मध्ये):
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

छान! तुम्ही आता सर्व सेट अप केले आहे:

- AI मॉडेल्ससाठी योग्य परवानग्यांसह GitHub वैयक्तिक प्रवेश टोकन तयार केले
- तुमचे जावा विकास वातावरण चालू केले (Codespaces, dev कंटेनर, किंवा स्थानिक)
- OpenAI Java SDK वापरून GitHub मॉडेल्सशी कनेक्ट केले
- AI मॉडेल्सशी संवाद साधणारे साधे उदाहरण चालवून सर्व काही तपासले

## पुढील पावले

[अध्याय 3: Core Generative AI Techniques](../03-CoreGenerativeAITechniques/README.md)

## समस्या निवारण

समस्या येत आहे? येथे सामान्य समस्या आणि उपाय आहेत:

- **टोकन कार्य करत नाही?** 
  - तुम्ही संपूर्ण टोकन कोणत्याही अतिरिक्त जागांशिवाय कॉपी केले आहे याची खात्री करा
  - टोकन योग्यरित्या पर्यावरणीय व्हेरिएबल म्हणून सेट केले आहे याची पुष्टी करा
  - तुमच्या टोकनला योग्य परवानग्या आहेत (Models: Read and write) याची खात्री करा

- **Maven सापडत नाही?** 
  - dev कंटेनर/Codespaces वापरत असल्यास, Maven प्री-इंस्टॉल केलेले असावे
  - स्थानिक सेटअपसाठी, Java 21+ आणि Maven 3.9+ इंस्टॉल केले आहेत याची खात्री करा
  - `mvn --version` वापरून इंस्टॉलेशन सत्यापित करा

- **कनेक्शन समस्या?** 
  - तुमचे इंटरनेट कनेक्शन तपासा
  - GitHub तुमच्या नेटवर्कवरून प्रवेशयोग्य आहे याची पुष्टी करा
  - GitHub मॉडेल्स एंडपॉइंट ब्लॉक करणाऱ्या फायरवॉलच्या मागे तुम्ही नाही याची खात्री करा

- **Dev कंटेनर सुरू होत नाही?** 
  - Docker Desktop चालू आहे याची खात्री करा (स्थानिक विकासासाठी)
  - कंटेनर पुन्हा तयार करण्याचा प्रयत्न करा: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **अॅप्लिकेशन संकलन त्रुटी?**
  - तुम्ही योग्य डिरेक्टरीमध्ये आहात याची खात्री करा: `02-SetupDevEnvironment/examples/github-models`
  - साफसफाई आणि पुन्हा तयार करण्याचा प्रयत्न करा: `mvn clean compile`

> **मदतीची गरज आहे?**: अजूनही समस्या येत आहे? रिपॉझिटरीमध्ये एक समस्या उघडा आणि आम्ही तुम्हाला मदत करू.

**अस्वीकरण**:  
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) वापरून भाषांतरित करण्यात आला आहे. आम्ही अचूकतेसाठी प्रयत्नशील असलो तरी कृपया लक्षात ठेवा की स्वयंचलित भाषांतरे त्रुटी किंवा अचूकतेच्या अभावाने ग्रस्त असू शकतात. मूळ भाषेतील दस्तऐवज हा अधिकृत स्रोत मानला जावा. महत्त्वाच्या माहितीसाठी व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराचा वापर करून उद्भवलेल्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थासाठी आम्ही जबाबदार राहणार नाही.