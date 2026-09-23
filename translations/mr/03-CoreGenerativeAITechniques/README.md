# मुख्य जनरेटिव्ह AI तंत्रज्ञान ट्युटोरियल

## आशयसूची

- [पूर्वशर्ती](#पूर्वशर्ती)
- [सुरू करणे](#सुरुवात)
- [मॉडेल निवड मार्गदर्शक](#मॉडेल-निवड-मार्गदर्शक)
- [ट्युटोरियल 1: LLM पूर्णता आणि चॅट](#ट्युटोरियल-1-llm-पूर्णता-आणि-चॅट)
- [ट्युटोरियल 2: फंक्शन कॉलिंग](#ट्युटोरियल-2-फंक्शन-कॉलिंग)
- [ट्युटोरियल 3: RAG (रिट्रीवल-अग्मेंटेड जनरेशन)](#ट्युटोरियल-3-rag-रिट्रीवल-अग्मेंटेड-जनरेशन)
- [ट्युटोरियल 4: जबाबदार AI](#ट्युटोरियल-4-जबाबदार-ai)
- [सामान्य नमुने उदाहरणांमध्ये](#उदाहरणांमधील-सामान्य-नमुने)
- [युनिट चाचण्या](#युनिट-चाचण्या)
- [संपर्कानुसार थेट पडताळणी](#सलग-थेट-पडताळणी)
- [समस्या निवारण](#समस्यांचे-निराकरण)
- [पुढील पायऱ्या](#पुढील-टप्पे)

## आढावा

चार स्वतंत्र जावा प्रोग्राम्स चॅट, संभाषण इतिहास, फंक्शन कॉलिंग, संपूर्ण दस्तऐवज पुनर्प्राप्ती-अग्मेंटेड जनरेशन (RAG), आणि जबाबदार-AI प्रतिसाद हाताळणी दाखवतात. सर्व चॅट विनंत्या मूळतः **GPT-5.6 Luna सोबत `none` कारण करण्याच्या प्रयत्नासह** लक्ष्य करतात.

हे उदाहरणे अधिकृत OpenAI जावा SDK वापरतात ज्यामध्ये Azure OpenAI ची v1 एन्डपॉईंट वापरीली आहे, [Microsoft चे SDK मार्गदर्शन](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) अनुसरत. जुना `azure-ai-openai` पॅकेज आता अवलंबन नाही. चॅट पूर्णता साठी विद्यमान संदेश-आधारित कार्यप्रवाह शिकविण्यास ठेवले आहे; इतर API पर्यायांसाठी [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) पहा.

## पूर्वशर्ती

- जावा 21 किंवा नंतरचे व Maven 3.6.3 किंवा नंतरचे.
- `gpt-5.6-luna` नावाचे Azure OpenAI चॅट डिप्लॉयमेंट, किंवा सुसंगत Chat Completions सेटिंग्जसह ओव्हरराईड.
- रिसोर्सवर **Cognitive Services OpenAI User** भूमिका असलेले साइन-इन केलेले Azure ओळखपत्र. स्थानिक विकासासाठी तुमचा Azure CLI साइन-इन वापरला जातो; होस्ट केलेल्या अनुप्रयोगांसाठी व्यवस्थापित ओळख वापरू शकता.
- रिसोर्स सेटअप आणि साइन-इन सूचना साठी [Chapter 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) पहा.

[Maven संरचना](../../../03-CoreGenerativeAITechniques/examples/pom.xml) या आवृत्त्या ठरवते, २०२६-०९-१४ रोजी तपासलेले:

| घटक | आवृत्ती | उद्देश |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | अधिकृत Azure v1-सुसंगत ग्राहक |
| `com.azure:azure-identity` | 1.18.6 | कीलेस प्रमाणीकरण आणि टोकन रिफ्रेश |
| `net.objecthunter:exp4j` | 0.4.8 | कोड मूल्यांकनाशिवाय अंकगणिती व्याख्या पार्सिंग |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | ऑफलाइन जुपिटर युनिट चाचण्या |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | जावा 21 संकलन, चाचण्या, चालणारी उदाहरणे |

संकलक `--release 21` वापरतो. या स्वतंत्र उदाहरणांसाठी Spring Boot, Spring AI, किंवा LangChain4j अवलंबन आवश्यक नाही.

## सुरुवात

रिपॉझिटरी मूळातून तुमच्या शेलमध्ये रिसोर्स एन्डपॉईंट आणि ऐच्छिक डिप्लॉयमेंट ओव्हरराईड सेट करा.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

चाचण्यांना Azure क्रेडेन्शियल्स किंवा एन्डपॉईंट आवश्यक नाहीत. Maven स्वयंचलितपणे पर्यावरण फाइल वाचत नाही; थेट उदाहरणे चालवण्यासाठी वापरलेले शेल मध्ये व्हेरिएबल सेट करा. IDE लाँचसाठी, तुम्ही वापरत असलेल्या लाँच संरचनेत पर्यावरण तपासा.

## मॉडेल निवड मार्गदर्शक

| पर्यावरण व्हेरिएबल | अर्थ | डिफॉल्ट |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure रिसोर्स मूळ किंवा आधीच सामान्यीकृत `/openai/v1` URL | थेट चालवण्यासाठी आवश्यक |
| `AZURE_OPENAI_DEPLOYMENT` | चॅट डिप्लॉयमेंटचे नाव, मॉडेल आवृत्ती नाही | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | स्वतंत्र एम्बेडिंग डिप्लॉयमेंट कॉन्फिगरेशन, या चार प्रोग्रामद्वारे वापरला जात नाही | `text-embedding-3-small` |

रिक्त डिप्लॉयमेंट ओव्हरराईड डिफॉल्ट वापरतो. कॉन्फिगरेशन `/openai/v1` अचूकपणे एकदाच जोडते आणि क्रेडेन्शियल्स, क्वेरी स्ट्रिंग्ज, आणि जुने डिप्लॉयमेंट पाथ एन्डपॉईंटमध्ये नाकारते.

प्रत्येक चॅट विनंती स्पष्टपणे `reasoningEffort(ReasoningEffort.NONE)` आणि `maxCompletionTokens(...)` सेट करते. कोणतीही विनंती `temperature`, `top_p`, किंवा जुना पूर्णता-टोकन पर्याय सेट करत नाही. यात टूल-निवड आणि टूल-परिणाम फॉलोअॅप्स समाविष्ट आहेत. GPT-5.6 चॅट पूर्णता फंक्शन टूल्ससाठी कारण करण्याचा प्रयत्न `none` लागतो; पाहा [Microsoft चा चॅट मार्गदर्शन](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**या भागात स्ट्रिमिंग किंवा एम्बेडिंगचे एन्ट्रीपॉईंट नाहीत.** वाचक संपूर्ण दस्तऐवज प्राप्त करतो, व्हेक्टर नाही. जर तुम्ही एम्बेडिंग वाढवत असाल, तर वेगळा एम्बेडिंग डिप्लॉयमेंट वापरा जसे की `text-embedding-3-small`, कधीच Luna नाही.

## ट्युटोरियल 1: LLM पूर्णता आणि चॅट

स्रोत: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

हा प्रोग्राम सोप्या जावा स्ट्रीम्स स्पष्टीकरण, दोन टप्प्यांचा HashMap/TreeMap संभाषण, आणि संवादात्मक चॅट चालवतो. दुसऱ्या टप्प्यात पहिला सहाय्यक प्रतिसाद असतो; प्रत्येक संवादात्मक टप्पा आपला मागील संभाषण पाठवतो.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` डिप्लॉयमेंट आणि स्पष्ट कारणकारक सेटिंग पुरवतो. संवादात्मक चॅट रिकाम्या ओळी टाळतो, `exit` किंवा EOF वर समाप्त करतो, आणि सिस्टम संदेश आणि नऊ पूर्ण झालेल्या वापरकर्ता/सहाय्यक टप्पे ठेवतो. टप्पा-गणना मर्यादा शैक्षणिक आहे, अचूक टोकन बजेट हमी नाही.

उदाहरण संचिका निर्देशिका मधून:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

अपेक्षा करा तीन प्रारंभिक उत्तर, नंतर `You:` प्रॉम्प्ट. प्रत्येक गैररिकामी संवादात्मक प्रश्न एक विनंती वाढवतो. पूर्णतेच्या मर्यादा २००, ३००, ४००, नंतर ५०० टोकन प्रतेक संवादात्मक टप्प्यासाठी.

## ट्युटोरियल 2: फंक्शन कॉलिंग

स्रोत: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK `WeatherArguments` आणि `CalculationArguments` रेकॉर्ड्समधून JSON स्कीमाज व्युत्पन्न करते. आवश्यक टूल निवड प्रत्येक उदाहरणाला टूल प्रोटोकॉल वापरायला लावते, मॉडेलचे मदत न केलेले उत्तर स्वीकारण्याऐवजी.

1. परवानगी दिलेला टूल, कारणकारक प्रयत्न `none`, आणि ३००-टोकन पूर्णता मर्यादा सह प्रश्न पाठवा.
2. `tool_calls` समाप्ती कारण आवश्यक, फंक्शन नाव आणि कॉल आयडी तपासा, टंकित JSON तर्क पार्स करा.
3. स्थानिक फंक्शन चालवा. मॉडेल जावा किंवा अज्ञात कोड चालवत नाही.
4. सहाय्यक टूल-कॉल संदेश एकदाच जोडा, नंतर प्रत्येक निकाल त्याच्या जुळणाऱ्या `tool_call_id` सह जोडा.
5. एक अंतिम ३००-टोकन विनंती टूलशिवाय पाठवा आणि पूर्ण, रिकाम्या नसलेले उत्तर आवश्यक करा.

`get_weather` **अनुकरण केलेली**, स्थिर नसलेली हवामान देते. हे शहराचा आदर करते आणि दिलेली २२ डिग्री सेल्सियस मागणीवर फॅरेन्हाइट मध्ये रूपांतर करते. `calculate` exp4j द्वारे दिलेल्या अभिव्यक्तीचे मूल्यांकन करते, `१५% ऑफ २४०` आणि `२ + ३ * ४` सारख्या स्वरूपांना समर्थन देते आणि रिकामी, फार मोठी, अवैध, किंवा अमर्यादित गणना नाकारते. हे फ्लोटिंग-पॉइंट अंकगणित वापरते, आर्थिक दशांश अचूकता नाही.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

अपेक्षा करा `Function: get_weather`, अनुकरण केलेले सिएटलचे हवामान, `Function: calculate`, `Function result: 36` आणि दोन अंतिम उत्तर. stdin किंवा बाह्य हवामान क्रेडेन्शियल आवश्यक नाहीत. यशस्वी रनला नेमके चार चॅट विनंत्या लागतात.

## ट्युटोरियल 3: RAG (रिट्रीवल-अग्मेंटेड जनरेशन)

स्रोत: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). इनपुट: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

या परिचयात्मक RAG उदाहरणात एक संपूर्ण UTF-8 दस्तऐवज पुनर्प्राप्त केला जातो आणि प्रश्नासह वापरकर्ता संदेशात समाविष्ट केला जातो. स्वतंत्र प्रणाली संदेश मॉडेलला निर्देशित करतो की दस्तऐवज सामग्री अविश्वसनीय डेटा म्हणून विचार करा आणि फक्त त्या संदर्भातूनच उत्तर द्या. जर दस्तऐवजात उत्तर नसेल, तर विनंती केलेली प्रतिक्रिया: `माझ्याकडे दिलेल्या दस्तऐवजात ती माहिती सापडू शकली नाही.`

आधारभूतता भ्रम कमी करू शकते, पण डेलिमीटर्स किंवा प्रणाली सूचना कठोर अचूकता हमी देत नाही किंवा प्रत्येक प्रॉम्प्ट इंजेक्शन रोखत नाहीत. थेट उत्तरे तपासा. उत्पादन RAG सामान्यतः चंकिन्ग, पुनर्प्राप्ती, संदर्भ, प्रवेश नियंत्रण, आणि मूल्यमापन जोडतो.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

एक प्रश्न प्रविष्ट करा, उदाहरणार्थ `दस्तऐवज कोणती प्रमाणीकरण पद्धत वर्णन करतो?`. उत्तरात Microsoft Entra ID चा उल्लेख अपेक्षा करा. प्रोग्राम एका चॅट विनंतीनंतर 500-टोकन पूर्णता मर्यादेसह बाहेर पडतो.

डिफॉल्ट फाइल शोध रिपॉझिटरी मूळ, अध्याय निर्देशिका, किंवा उदाहरणे निर्देशिकेतून काम करतो. स्पष्ट पथ देखील समर्थित आहे:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

इनपुट्स रिकामे नसावे: जास्तीत जास्त 32 KiB UTF-8 दस्तऐवज डेटा आणि 2,000 प्रश्न अक्षरे. गहाळ फाइल्स, रिकामे/EOF प्रश्न, आणि विहितेपेक्षा मोठे इनपुट्स अनुमानपूर्वी अयशस्वी होतात.

## ट्युटोरियल 4: जबाबदार AI

स्रोत: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

सहा तपासणी नुकसानकारक सूचनां, द्वेष भाषण, गोपनीयता, वैद्यकीय चुकीची माहिती, बेकायदेशीर सामग्री, आणि एक सुखदायक जबाबदार-AI प्रश्न कव्हर करतात. प्रोग्राम उत्तर निरीक्षण करतो, प्रत्येक तपासणीवर एक फिल्टर लागेल असं गृहीत धरत नाही.

| निकाल | पुरावा |
| --- | --- |
| `FILTERED` | स्पष्ट `content_filter` / `ResponsibleAIPolicyViolation` त्रुटी कोड, किंवा पूर्णता `content_filter` समाप्ती कारण |
| `REFUSED` | रिकामी नसलेली संरचित `message.refusal` फील्ड |
| `POSSIBLE_REFUSAL` | साधारण मजकूरात सुरूवातीचा नकारात्मक वाक्प्रचार; पुनरावलोकनासाठी एक नियम |
| `GENERATED` | पूर्ण झालेले रिकामे नसलेले उत्तर; त्याची सामग्री सुरक्षित असल्याचा पुरावा नाही |

सामान्य HTTP 400 हे फिल्टरिंगचे पुरावा नाही. अवैध पॅरामीटर्स, प्रमाणीकरण अयशस्वी, दर मर्यादा, सर्व्हर त्रुटी, चुकीचा प्रतिसाद, आणि कापलेला आउटपुट रन अयशस्वी करतात, खोटे सुरक्षितता यश निर्माण करत नाही. "हानिकारक सामग्री" सारखे व्यापक शब्द सुखद स्वरूपाच्या स्पष्टीकरणात नकार म्हणून घेतले जात नाहीत.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

सहा श्रेणी निकाल आणि निरीक्षण सुरक्षितता प्रमाणपत्र नाही असे एक सारांश अपेक्षा करा. प्रत्येक तपासणीला ३००-टोकन पूर्णता मर्यादा आहे. अपेक्षित नसलेले निर्माण आणि शक्य नकार हस्तचालितपणे तपासा; सुखद अंतर्भाग एक अर्थपूर्ण जबाबदार-AI स्पष्टीकरण तयार करेल. stdin आवश्यक नाही.

## उदाहरणांमधील सामान्य नमुने

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) एन्डपॉईंट सामान्यीकरण, डिप्लॉयमेंट ओव्हरराईड, कीलेस प्रमाणीकरण, आणि चॅट पर्याय केंद्रीत करते:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

टोकन पुरवठादार आवश्यक तेव्हा प्रवेश टोकन ताजेतवाने करतो. टोकन लॉग करू नका किंवा हे API किल्लीने बदला नका. प्रत्येक प्रोग्राम आपला क्लायंट पुन्हा वापरतो आणि `finally` मध्ये किंवा स्वतःच्या `AutoCloseable` wrappers मधून बंद करतो; SDK चा `OpenAIClient` स्वतः `AutoCloseable` नाही.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) पूर्ण, रिकामे नसलेले मजकूरात्मक उत्तर अपेक्षीत करतो. रिकाम्या निवडी, नकार, फिल्टर्स, आणि कापलेली उत्तरे गुपचूप यश म्हणून छापली जात नाहीत. जबाबदार-AI उदाहरण अपेक्षित फिल्टर/नकार निकाल स्पष्ट हाताळतो. हाताळले न गेलेले अयशस्वी जावा/maven प्रक्रियेला नॉनझिरो एग्झिट कोड देते.

**स्वयंचलित SDK रीट्राय बंद आहेत** त्यामुळे शेअर्ड कमी-RPM डिप्लॉयमेंटवर विनंती संख्या अंदाजे राहतील. प्रत्येक अनुमान विनंतीला 60-सेकंदाची टाइमआउट आहे. टोकन मिळवणे अधिक वेळ घेऊ शकते. अनुप्रयोग-स्तरीय अनुसूची किमान मर्यादा आदरली पाहिजे; चुकलेले पेड प्रश्न चुकून पुन्हा चालवू नका.

## युनिट चाचण्या

उदाहरण संचिका निर्देशिका मधून:

```powershell
mvn -B -ntp clean test
```

चाचणी परिवहन SDK HTTP थर संपूर्ण बदलतो, प्रत्यक्ष सिरीयलाइज्ड विनंती बॉडी टिपतो, आणि उत्तर दिलेल्या क्व्युएंना पुरवतो. हे कोणतेही सॉकेट उघडत नाही, Azure टोकन मिळवत नाही, आणि अनपेक्षित विनंत्यांवर अयशस्वी होते. या चाचण्या अनुप्रयोग वर्तन आणि SDK प्रोटोकॉल वैध करतात, थेट मॉडेल गुणवत्ता किंवा डिप्लॉयमेंट उपलब्धता नाही.

| चाचणी संच | कव्हरेज |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | एन्डपॉईंट सामान्यीकरण/त्याग, डिप्लॉयमेंट ओव्हरराईड, कारणकारक आणि टोकन पर्याय |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | प्रत्येक पूर्णता कार्यप्रवाह, संदेश इतिहास, पूर्ण टप्पा छाटणी, EOF, अयशस्वी |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | टूल स्कीमाज, टंकित तर्क, अंकगणित, आयडी, अनेक टूल निकाल, अयशस्वी फॉलोअप्स |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | फाइल शोध, UTF-8, आकार मर्यादा, भू-आधार पेलोड, इनपुट आणि API त्रुटी |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | सर्व सहा तपासण्या, स्पष्ट फिल्टर्स, नकार वर्गीकरण, सामान्य 400 आणि इतर अयशस्वी |

एक संचासाठी, वापरा `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. सामायिक फिक्स्चर [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) मध्ये आहेत.

## सलग थेट पडताळणी

थेट कॉल्स युनिट चाचण्यांपासून वेगळे आहेत. पुढील आदेश **वैयक्तिकरित्या** वापरा, रिपॉझिटरी मूळातून, फक्त क्रेडेन्शियल्स आणि डिप्लॉयमेंट प्रवेश तयार झाल्यानंतर. कोणतेही सेवा किंवा कायमस्वरूपी प्रक्रिया आवश्यक नाहीत.

एक सामायिक **10 विनंत्या/मिनिट** डिप्लॉयमेंटसाठी, संपूर्ण पुढील प्रोग्रामसाठी पुरेसा कोटा राखून ठेवा: 5, 4, 1, नंतर 6 विनंत्या. सलग प्रक्रियाकडे दर मर्यादा पालनाची हमी नाही. सर्व कॉलर्सशी समन्वय करा; चार कॉल्स एकाचवेळी पेस्ट करू नका.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. पूर्णता, बहु-टप्पा, आणि दोन संवादात्मक टप्पे:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

सर्व तीन विभागाच्या शीर्षकांची, पाच उत्तरांची, एका अंतिम संवादात्मक उत्तराची जी अदा, `Goodbye!` आणि exit कोड 0 वळण तपासा. बजेट: **5 विनंत्या, जास्तीत जास्त 1,900 पूर्णता टोकन्स**. छोट्या रनसाठी, फक्त `exit` पास करा: 3 विनंत्या / 900 टोकन्स, पण त्याने संवादात्मक अनुमान चालवले जात नाही.

**2. दोन्ही कार्य-आह्वान कार्यप्रवाह:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

दोन्ही कार्यांची नावे तपासा, सिम्युलेटेड सियाटल हवामान, गणना केलेले परिणाम 36, दोन अंतिम उत्तरं आणि exit कोड 0. बजेट: **4 विनंत्या, जास्तीत जास्त 1,200 पूर्णता टोकन्स**.

**3. दस्तऐवज-आधारित उत्तर:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

दस्तऐवजाचा मार्ग तपासा, मायक्रोसॉफ्ट एंट्रा आयडीचा उल्लेख करणारे उत्तर आणि exit कोड 0. बजेट: **1 विनंती, जास्तीत जास्त 500 पूर्णता टोकन्स**. विद्यमान [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) हा एकमेव आवश्यक इनपुट फाईल आहे. गहाळ विषयाबाबत विचारणा करणाऱ्या ऐच्छिक दुसऱ्या रनला टाळावे आणि यात 1 विनंती / 500 टोकन्स अधिक लागतील.

**4. जबाबदार-AI निरीक्षणे:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

सहा वर्गीकरणे आणि निरीक्षणात्मक सारांश तपासा, तयार केलेल्या सामग्रीचे पुनरावलोकन करा, आणि तांत्रिक पूर्णतेसाठी exit कोड 0 आवश्यक आहे. यशस्वी प्रक्रिया निर्गमन मॉडल सुरक्षितता प्रमाणित करत नाही. बजेट: **6 विनंत्या, जास्तीत जास्त 1,800 पूर्णता टोकन्स**.

**चार आदेशांसाठी एकूण: 16 चॅट विनंत्या आणि जास्तीत जास्त 5,400 पूर्णता टोकन्स**, तसेच इनपुट टोकन्स (परत आलेला संवाद आणि टूल योजना/इतिहास यांसहित). कोणतीही एम्बेडिंग विनंती नाहीत. वास्तविक टोकन वापर मॉडेलवर अवलंबून आहे आणि कमी असू शकतो, विशेषतः फिल्टर केलेल्या प्रॉम्प्टसाठी. डॉलर खर्च तैनाती किंमतीवर अवलंबून आहे; कोणतीही ठराविक आर्थिक किंमत म्हटलेली नाही. सर्व विनंती मर्यादा हाताने पुनरावृत्ती न करता मान्य आहेत. प्रत्येक आदेशानंतर त्वरित `$LASTEXITCODE` तपासा; शून्य नसल्यास रन यशस्वी झाला नाही.

## समस्यांचे निराकरण

- **एंडपॉइंट मिसिंग / 401 / 403:** लॉन्च करताना एंडपॉइंट सेट करा, स्थानिक Azure साइन-इन आणि रिसोर्स-केंद्रित भूमिका तपासा, आणि अनपेक्षित ओळख पर्यावरण अधिलेखांबाबत तपासा.
- **400 / 404:** तैनात केलेले अस्तित्वात आहे का आणि चॅट पूर्णता (reasoning effort `none`) समर्थित आहे का याची पुष्टी करा. HTTPS रिसोर्स रूट किंवा `/openai/v1` URL वापरा, जुना तैनाती URL नाही. सामान्य 400 त्रुटी तांत्रिक अयशस्वी होणे आहेत, सुरक्षा प्रतिबंध नाहीत.
- **429:** सामायिक RPM आणि टोकन कोटा समन्वय करा त्यानंतर पुन्हा प्रयत्न करा. उदाहरणांमध्ये स्वयंचलित पुन्हा प्रयत्न नाहीत.
- **`अपूर्ण चॅट प्रतिसाद: लांबी`:** आउटपुटने पूर्णता मर्यादा गाठली. मर्यादा वाढवण्याआधी प्रतिसाद आणि प्रॉम्प्ट पुनरावलोकन करा आणि त्याच्या दस्तऐवज बजेटची पाहणी करा; त्रुटीने पूर्ण होणारा रन यशस्वी नाही असा नोंदू नका.
- **फाईल किंवा stdin त्रुटी:** समर्थित डिरेक्टरीतून लॉन्च करा किंवा स्पष्ट दस्तऐवज मार्ग द्या. रिक्त नसलेला वाचक प्रश्न द्या. पूर्णता EOF किंवा `exit` वर सामान्यपणे संपु शकते.
- **कंपाइल त्रुटी:** Java 21 किंवा नंतरची आवृत्ती तपासा, नंतर `mvn -B -ntp clean test` चालवा. पॉवरशेलमध्ये Mavenतील डॉट असलेले गुणधर्म संपूर्ण कोटात ठेवा, उदाहरणार्थ `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## पुढील टप्पे

[Chapter 4: Practical Samples](../04-PracticalSamples/README.md) कडे पुढे जा.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. जरी आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थलावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->