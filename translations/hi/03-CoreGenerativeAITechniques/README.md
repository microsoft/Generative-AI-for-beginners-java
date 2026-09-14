# कोर जेनेरेटिव AI तकनीक ट्यूटोरियल

## सामग्री तालिका

- [पूर्वापेक्षाएँ](#पूर्वापेक्षाएँ)
- [शुरुआत करना](#शुरुआत-करना)
- [मॉडल चयन मार्गदर्शिका](#मॉडल-चयन-मार्गदर्शिका)
- [ट्यूटोरियल 1: LLM समापन और चैट](#ट्यूटोरियल-1-llm-समापन-और-चैट)
- [ट्यूटोरियल 2: फंक्शन कॉलिंग](#ट्यूटोरियल-2-फंक्शन-कॉलिंग)
- [ट्यूटोरियल 3: RAG (रिट्रीवल-ऑगमेंटेड जनरेशन)](#ट्यूटोरियल-3-rag-रिट्रीवल-ऑगमेंटेड-जनरेशन)
- [ट्यूटोरियल 4: जिम्मेदार AI](#ट्यूटोरियल-4-जिम्मेदार-ai)
- [उदाहरणों में सामान्य पैटर्न](#उदाहरणों-में-सामान्य-पैटर्न)
- [यूनिट परीक्षण](#यूनिट-परीक्षण)
- [क्रमिक लाइव सत्यापन](#क्रमिक-लाइव-सत्यापन)
- [समस्याओं का समाधान](#समस्या-निवारण)
- [अगले कदम](#अगले-कदम)

## अवलोकन

चार स्वतंत्र जावा प्रोग्राम चैट, बातचीत इतिहास, फंक्शन कॉलिंग, पूरे दस्तावेज़ के रिट्रीवल-ऑगमेंटेड जनरेशन (RAG), और जिम्मेदार-AI प्रतिक्रिया हैंडलिंग प्रदर्शित करते हैं। सभी चैट अनुरोध डिफ़ॉल्ट रूप से **GPT-5.6 Luna जिसमें reasoning effort `none` है** को लक्षित करते हैं।

ये उदाहरण आधिकारिक OpenAI जावा SDK का उपयोग करते हैं जिसमें Azure OpenAI का v1 endpoint होता है, [Microsoft के SDK मार्गदर्शन](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) का पालन करते हुए। पुराना `azure-ai-openai` पैकेज अब निर्भरता नहीं है। चैट समापन बनाए रखा गया है ताकि मौजूदा संदेश-आधारित वर्कफ़्लो सिखाए जा सकें; अन्य API विकल्पों के लिए देखें [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure)।

## पूर्वापेक्षाएँ

- जावा 21 या बाद का संस्करण और Maven 3.6.3 या बाद का संस्करण।
- एक Azure OpenAI चैट परिनियोजन जिसका नाम `gpt-5.6-luna` हो, या एक संगत चैट समापन सेटिंग्स के साथ ओवरराइड।
- एक साइन-इन किया हुआ Azure पहचान जिसमें संसाधन पर **Cognitive Services OpenAI User** भूमिका हो। स्थानीय विकास में आपके Azure CLI साइन-इन का उपयोग होता है; होस्टेड एप्लिकेशन प्रबंधित पहचान का उपयोग कर सकते हैं।
- संसाधन सेटअप और साइन-इन निर्देशों के लिए देखें [अध्याय 2](../02-SetupDevEnvironment/getting-started-azure-openai.md)।

[Maven कॉन्फ़िगरेशन](../../../03-CoreGenerativeAITechniques/examples/pom.xml) इन संस्करणों को पिन करता है, जो 2026-09-14 को जांचे गए हैं:

| घटक | संस्करण | उद्देश्य |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | आधिकारिक Azure v1-संगत क्लाइंट |
| `com.azure:azure-identity` | 1.18.6 | बिना कुंजी प्रमाणीकरण और टोकन नवीनीकरण |
| `net.objecthunter:exp4j` | 0.4.8 | गणितीय अभिव्यक्ति पार्सिंग बिना कोड मूल्यांकन के |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | ऑफ़लाइन ज्यूपिटर यूनिट परीक्षण |
| Maven कंपाइलर / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | जावा 21 संकलन, परीक्षण, चलने योग्य उदाहरण |

कंपाइलर `--release 21` उपयोग करता है। इन स्वतंत्र उदाहरणों के लिए किसी स्प्रिंग बूट, स्प्रिंग AI, या LangChain4j निर्भरता की आवश्यकता नहीं है।

## शुरुआत करना

रिपॉजिटरी की रूट से, अपने शेल में संसाधन एंडपॉइंट और वैकल्पिक परिनियोजन ओवरराइड सेट करें।

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

परीक्षणों के लिए Azure प्रमाण-पत्र या एंडपॉइंट की आवश्यकता नहीं होती। Maven स्वचालित रूप से वातावरण फ़ाइल को पढ़ता नहीं है; लाइव उदाहरणों को लॉन्च करने के लिए उपयोग किए गए शेल में वेरिएबल सेट करें। IDE लॉन्च के लिए, अपने लॉन्च कॉन्फ़िगरेशन द्वारा आपूर्ति किए गए वातावरण की पुष्टि करें।

## मॉडल चयन मार्गदर्शिका

| वातावरण वेरिएबल | अर्थ | डिफ़ॉल्ट |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure संसाधन मूल या पहले से सामान्यीकृत `/openai/v1` URL | लाइव रन के लिए आवश्यक |
| `AZURE_OPENAI_DEPLOYMENT` | चैट परिनियोजन नाम, मॉडल संस्करण नहीं | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | अलग एम्बेडिंग परिनियोजन कॉन्फ़िगरेशन, इन चार प्रोग्रामों के द्वारा उपयोग न किया गया | `text-embedding-3-small` |

खाली परिनियोजन ओवरराइड डिफ़ॉल्ट का उपयोग करते हैं। कॉन्फ़िगरेशन `/openai/v1` को ठीक एक बार जोड़ता है और एंडपॉइंट में प्रमाण-पत्र, क्वेरी स्ट्रिंग्स, और पुरानी परिनियोजन पथों को अस्वीकार करता है।

हर चैट अनुरोध स्पष्ट रूप से `reasoningEffort(ReasoningEffort.NONE)` और `maxCompletionTokens(...)` सेट करता है। कोई भी अनुरोध `temperature`, `top_p` या पुरानी समापन-टोकन विकल्प सेट नहीं करता। इसमें टूल चयन और टूल परिणाम फॉलो-अप शामिल हैं। GPT-5.6 चैट समापन फंक्शन टूल्स को reasoning effort `none` चाहिए; देखें [Microsoft की चैट मार्गदर्शिका](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt)।

**इस अध्याय में कोई स्ट्रीमिंग या एम्बेडिंग प्रवेश बिंदु नहीं है।** पाठक अपने पूरे दस्तावेज़ को पुनः प्राप्त करता है, न कि वेक्टर। यदि आप एम्बेडिंग के साथ इसे बढ़ाते हैं, तो `text-embedding-3-small` जैसे अलग एम्बेडिंग परिनियोजन का उपयोग करें, कभी Luna नहीं।

## ट्यूटोरियल 1: LLM समापन और चैट

स्रोत: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)।

प्रोग्राम एक सरल जावा स्ट्रीम व्याख्या, एक दो-चरण का HashMap/TreeMap बातचीत, और इंटरैक्टिव चैट चलाता है। दूसरा चरण पहला सहायक उत्तर शामिल करता है; प्रत्येक इंटरैक्टिव चरण भी अपनी पिछली बातचीत भेजता है।

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` परिनियोजन और स्पष्ट reasoning सेटिंग प्रदान करता है। इंटरैक्टिव चैट खाली पंक्तियों को छोड़ देता है, `exit` या EOF पर समाप्त होता है, और सिस्टम संदेश के साथ-साथ नौ पूर्ण उपयोगकर्ता/सहायक चरण बनाए रखता है। चरण-संख्या ट्रिमिंग एक शैक्षिक सीमा है, सटीक टोकन-बजट गारंटी नहीं।

उदाहरण निर्देशिका से:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

तीन प्रारंभिक उत्तरों की अपेक्षा करें, फिर `आप:` प्रॉम्प्ट। प्रत्येक गैर-खाली इंटरैक्टिव प्रश्न एक अनुरोध जोड़ता है। पूर्णता सीमाएं 200, 300, 400, फिर प्रति इंटरैक्टिव चरण 500 टोकन हैं।

## ट्यूटोरियल 2: फंक्शन कॉलिंग

स्रोत: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)।

SDK एनोटेटेड `WeatherArguments` और `CalculationArguments` रिकॉर्ड से JSON स्कीमाएं व्युत्पन्न करता है। एक आवश्यक टूल चयन प्रत्येक उदाहरण को मॉडल के बिना उत्तर स्वीकार करने के बजाय टूल प्रोटोकॉल का अभ्यास कराता है।

1. एक प्रश्न भेजें जिसमें अनुमति प्राप्त टूल हो, reasoning effort `none` हो, और 300 टोकन की पूर्णता सीमा हो।
2. `tool_calls` समाप्ति कारण आवश्यक है, फंक्शन नाम और कॉल आईडीस को सत्यापित करें, और टाइप किए गए JSON तर्क पार्स करें।
3. स्थानीय फंक्शन निष्पादित करें। मॉडल जावा या मनमाना कोड नहीं चलाता।
4. सहायक टूल-कॉल संदेश एक बार जोड़ें, फिर हर परिणाम उसके मिलते-जुलते `tool_call_id` के साथ भेजें।
5. एक अंतिम 300-टोकन अनुरोध बिना टूल के भेजें और एक पूर्ण, गैर-खाली उत्तर अपेक्षित करें।

`get_weather` **सिम्युलेटेड** मौसम प्रदान करता है, लाइव नहीं। यह शहर का सम्मान करता है और अनुरोध पर नमूना 22 डिग्री सेल्सियस को फ़ारेनहाइट में बदलता है। `calculate` exp4j के माध्यम से प्रदान की गई अभिव्यक्ति का मूल्यांकन करता है, जैसे `15% of 240` और `2 + 3 * 4` के रूपों का समर्थन करता है, और खाली, अधिकतम, अमान्य या गैर-सीमित गणनाओं को अस्वीकार करता है। यह फ्लोटिंग-पॉइंट अंकगणित का उपयोग करता है, वित्तीय दशमलव परिशुद्धता नहीं।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

अपेक्षा करें `Function: get_weather`, सिम्युलेटेड सिएटल मौसम, `Function: calculate`, `Function result: 36`, और दो अंतिम उत्तर। कोई stdin या बाहरी मौसम प्रमाण-पत्र की आवश्यकता नहीं। सफल रन ठीक चार चैट अनुरोधों का उपयोग करता है।

## ट्यूटोरियल 3: RAG (रिट्रीवल-ऑगमेंटेड जनरेशन)

स्रोत: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)। इनपुट: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)।

यह परिचयात्मक RAG उदाहरण एक पूरे UTF-8 दस्तावेज़ को पुनः प्राप्त करता है और प्रश्न के साथ उपयोगकर्ता संदेश में शामिल करता है। एक अलग सिस्टम संदेश मॉडल को दस्तावेज़ सामग्री को अविश्वसनीय डेटा के रूप में मानने और केवल उस संदर्भ से उत्तर देने का निर्देश देता है। अगर दस्तावेज़ में उत्तर नहीं है, तो अनुरोधित उत्तर होता है: `मैं इस प्रदान किए गए दस्तावेज़ में जानकारी नहीं पा सकता।`

ग्राउंडिंग भ्रमों को कम कर सकता है, लेकिन सीमा चिह्न या सिस्टम निर्देश सटीकता की गारंटी या हर प्रॉम्प्ट इंजेक्शन को रोक नहीं पाते। लाइव उत्तरों की समीक्षा करें। उत्पादन RAG सामान्यतः छंटाई, पुनः प्राप्ति, संदर्भ, पहुंच नियंत्रण, और मूल्यांकन जोड़ता है।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

एक प्रश्न दर्ज करें, जैसे `दस्तावेज़ किस प्रमाणीकरण विधि का वर्णन करता है?`। Microsoft Entra ID का उल्लेख करने वाला उत्तर अपेक्षित है। प्रोग्राम एक चैट अनुरोध के बाद 500-टोकन पूर्णता सीमा के साथ बाहर निकलता है।

डिफ़ॉल्ट फ़ाइल खोज रिपॉजिटरी रूट, अध्याय निर्देशिका, या उदाहरण निर्देशिका से काम करती है। स्पष्ट पथ भी समर्थित है:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

इनपुट बिना रिक्त होना चाहिए: अधिकतम 32 KiB का UTF-8 दस्तावेज़ डेटा और 2,000 प्रश्न वर्ण। अनुपलब्ध फ़ाइलों, खाली/EOF प्रश्नों, और अधिकतम इनपुट से पहले असफल होते हैं।

## ट्यूटोरियल 4: जिम्मेदार AI

स्रोत: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)।

छह जांचें हानिकारक निर्देश, घृणा भाषण, गोपनीयता, चिकित्सा गलत सूचना, अवैध सामग्री, और एक सौम्य जिम्मेदार-AI प्रश्न को कवर करती हैं। प्रोग्राम प्रतिक्रिया को देखता है बजाय इस धारणा के कि हर जांच को फिल्टर को ट्रिगर करना चाहिए।

| परिणाम | प्रमाण |
| --- | --- |
| `FILTERED` | एक स्पष्ट `content_filter` / `ResponsibleAIPolicyViolation` त्रुटि कोड, या एक पूर्ण `content_filter` समाप्ति कारण |
| `REFUSED` | एक गैर-खाली संरचित `message.refusal` फ़ील्ड |
| `POSSIBLE_REFUSAL` | सामान्य पाठ में एक उद्घाटन अस्वीकार वाक्यांश; समीक्षा की आवश्यकता वाली एक परिकल्पना |
| `GENERATED` | एक पूर्ण गैर-खाली प्रतिक्रिया; इसका प्रमाण नहीं कि सामग्री सुरक्षित है |

सामान्य HTTP 400 **फिल्टरिंग का सबूत नहीं** है। अमान्य पैरामीटर, प्रमाणीकरण विफलता, रेट सीमाएं, सर्वर त्रुटियाँ, गलत प्रतिक्रियाएं, और कटे हुए आउटपुट रन को विफल करते हैं न कि गलत सुरक्षा सफलता पैदा करते हैं। "हानिकारक सामग्री" जैसे व्यापक शब्द एक सौम्य व्याख्या में अस्वीकार के रूप में नहीं गिने जाते।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

छह श्रेणी परिणाम और एक सारांश अपेक्षित करें जो बताता है कि निरीक्षण सुरक्षा प्रमाणन नहीं है। प्रत्येक जांच की 300-टोकन समापन सीमा है। अप्रत्याशित उत्पादन और संभावित अस्वीकृतियों की मैनुअल समीक्षा करें; सौम्य तुलना एक महत्वपूर्ण जिम्मेदार-AI व्याख्या उत्पन्न करें। कोई stdin आवश्यक नहीं।

## उदाहरणों में सामान्य पैटर्न

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) एन्डपॉइंट सामान्यीकरण, परिनियोजन ओवरराइड, बिना कुंजी प्रमाणीकरण, और चैट विकल्प केंद्रीयकृत करता है:

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

टोकन सप्लायर आवश्यकतानुसार एक्सेस टोकन नवीनीकरण करता है। टोकन लॉग न करें और इसे API कुंजी से न बदलें। प्रत्येक प्रोग्राम अपना क्लाइंट पुनः उपयोग करता है और `finally` में या अपने `AutoCloseable` रैपर के माध्यम से बंद करता है; SDK का `OpenAIClient` स्वयं `AutoCloseable` नहीं है।

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) एक पूर्ण, गैर-खाली पाठात्मक उत्तर मांगता है। खाली विकल्प, अस्वीकृतियां, फिल्टर, और कटे हुए उत्तर सफलता के रूप में चुपचाप मुद्रित नहीं होते। जिम्मेदार-AI उदाहरण अपेक्षित फिल्टर/अस्वीकृति परिणामों को स्पष्ट रूप से संभालता है। असंबद्ध विफलताएं जावा/Maven प्रक्रिया को गैर-शून्य निकास कोड देती हैं।

**स्वचालित SDK पुनः प्रयास अक्षम हैं** ताकि साझा कम RPM परिनियोजनों पर अनुरोध संख्या अनुमानित रहे। प्रत्येक इनफेरेंस अनुरोध की 60 सेकंड की टाइमआउट होती है। टोकन अधिग्रहण में अतिरिक्त समय लग सकता है। एप्लिकेशन-स्तर शेड्यूलिंग को कोटा का सम्मान करना चाहिए; अंधाधुंध असफल भुगतान अनुरोध को फिर से न चलाएं।

## यूनिट परीक्षण

उदाहरण निर्देशिका से:

```powershell
mvn -B -ntp clean test
```

परीक्षण ट्रांसपोर्ट SDK HTTP स्तर को पूरी तरह प्रतिस्थापित करता है, वास्तविक सीरियलाइज़्ड अनुरोध बॉडी कैप्चर करता है, और कतारबद्ध प्रतिक्रियाएं प्रदान करता है। यह कोई सॉकेट नहीं खोलता, Azure टोकन प्राप्त नहीं करता, और अप्रत्याशित अनुरोधों पर विफल होता है। ये परीक्षण एप्लिकेशन व्यवहार और SDK प्रोटोकॉल को मान्य करते हैं, न कि लाइव मॉडल की गुणवत्ता या परिनियोजन उपलब्धता।

| टेस्ट सूट | कवरेज |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | एन्डपॉइंट सामान्यीकरण/अस्वीकृति, परिनियोजन ओवरराइड, reasoning और टोकन विकल्प |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | हर समापन वर्कफ़्लो, संदेश इतिहास, पूर्ण-चरण ट्रिमिंग, EOF, विफलताएं |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | टूल स्कीमाएं, टाइप किए गए तर्क, अंकगणित, आईडी, कई टूल परिणाम, विफल फॉलो-अप |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | फ़ाइल खोज, UTF-8, आकार सीमाएं, ग्राउंडिंग पेलोड, इनपुट और API त्रुटियां |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | सभी छह जांचें, स्पष्ट फिल्टर, अस्वीकृति वर्गीकरण, सामान्य 400 और अन्य विफलताएं |

एक सूट के लिए उपयोग करें `mvn -B -ntp test "-Dtest=FunctionsAppTest"`। साझा फिक्स्चर [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) में रहते हैं।

## क्रमिक लाइव सत्यापन

लाइव कॉल यूनिट परीक्षणों से अलग हैं। निम्नलिखित आदेश **अलग-अलग** प्रयोग करें, रिपॉजिटरी रूट से, केवल प्रमाण-पत्र और परिनियोजन पहुँच तैयार होने के बाद। किसी सेवा या स्थायी प्रक्रियाओं की आवश्यकता नहीं।

साझा **10 अनुरोध/मिनट** परिनियोजन के लिए, अगला प्रोग्राम लॉन्च करने से पहले पूरे के लिए पर्याप्त कोटा आरक्षित करें: 5, 4, 1, फिर 6 अनुरोध। क्रमिक प्रक्रियाएं अकेले रेट-लिमिट अनुपालन की गारंटी नहीं देतीं। रोलिंग मिनट का समन्वय सभी अन्य कॉलर के साथ करें; चार अनुरोधों को बिना सामंजस्य के बैच के रूप में न चिपकाएं।

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. समापन, मल्टी-टर्न, और दो इंटरैक्टिव टर्न:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

सभी तीन अनुभाग शीर्षकों, पाँच उत्तरों, अंतिम इंटरैक्टिव उत्तर जिसमें Ada को याद किया गया है, `Goodbye!`, और निकास कोड 0 की जांच करें। बजट: **5 अनुरोध, अधिकतम 1,900 पूर्णता टोकन**। एक छोटे रन के लिए केवल `exit` पाइप करें: 3 अनुरोध / 900 टोकन, लेकिन वह इंटरैक्टिव इनफेरेंस का अभ्यास नहीं करता।

**2. दोनों फ़ंक्शन-कॉलिंग वर्कफ़्लो:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

दोनों फ़ंक्शन नाम, सिम्युलेटेड सिएटल मौसम, गणना परिणाम 36, दो अंतिम उत्तर, और निकास कोड 0 की जांच करें। बजट: **4 अनुरोध, अधिकतम 1,200 पूर्णता टोकन**।

**3. दस्तावेज़-आधारित उत्तर:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

दस्तावेज़ पथ, Microsoft Entra ID का उल्लेख करता हुआ उत्तर, और निकास कोड 0 की जांच करें। बजट: **1 अनुरोध, अधिकतम 500 पूर्णता टोकन**। मौजूदा [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) केवल आवश्यक इनपुट फ़ाइल है। अनुपस्थित विषय के बारे में पूछने वाला एक वैकल्पिक दूसरा रन परहेज करेगा और एक अनुरोध / 500 टोकन जोड़ता है।

**4. जिम्मेदार-एआई अवलोकन:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

छह श्रेणियों और अवलोकनात्मक सारांश की जांच करें, उत्पादित सामग्री की समीक्षा करें, और तकनीकी पूर्णता के लिए निकास कोड 0 आवश्यक है। एक सफल प्रक्रिया निकास मॉडल सुरक्षा प्रमाणित नहीं करता। बजट: **6 अनुरोध, अधिकतम 1,800 पूर्णता टोकन**।

**चार आदेशों के लिए कुल: 16 चैट अनुरोध और अधिकतम 5,400 पूर्णता टोकन**, साथ ही इनपुट टोकन (जिसमें पुनरावृत्त वार्तालाप और टूल स्कीमा/इतिहास शामिल हैं)। किसी भी एम्बेडिंग अनुरोध नहीं हैं। वास्तविक टोकन उपयोग मॉडल पर निर्भर करता है और कम भी हो सकता है, खासकर फ़िल्टर्ड प्रॉम्प्ट के लिए। डॉलर लागत परिनियोजन मूल्य निर्धारण पर निर्भर करती है; कोई निश्चित मौद्रिक अनुमान नहीं है। सभी अनुरोध सीमाएँ कोई मैनुअल पुनरावृत्ति नहीं मानती हैं। प्रत्येक कमांड के तुरंत बाद `$LASTEXITCODE` जांचें; गैर-शून्य होने पर रन सफलतापूर्वक पूरा नहीं हुआ।

## समस्या निवारण

- **एंडपॉइंट गायब / 401 / 403:** लॉन्चिंग प्रक्रिया में एंडपॉइंट सेट करें, अपनी स्थानीय Azure साइन-इन और संसाधन-आधारित भूमिका सत्यापित करें, और अनजाने पहचान पर्यावरण अधिलेखन की जांच करें।
- **400 / 404:** पुष्टि करें कि परिनियोजन मौजूद है और reasoning effort `none` के साथ Chat Completions समर्थित है। HTTPS संसाधन मूल या `/openai/v1` URL का उपयोग करें, न कि पुराना परिनियोजन URL। सामान्य 400 त्रुटियाँ तकनीकी असफलताएं हैं, सुरक्षा ब्लॉक नहीं।
- **429:** साझा RPM और टोकन कोटा समन्वयित करें फिर पुनः प्रयास करें। उदाहरण जानबूझकर स्वत: पुनः प्रयास नहीं करते।
- **`Incomplete chat response: length`:** आउटपुट ने पूर्णता सीमा छू ली। सीमा और उसके प्रलेखित बजट बढ़ाने से पहले प्रतिक्रिया और प्रॉम्प्ट की समीक्षा करें; कटे हुए रन को सफल के रूप में दर्ज न करें।
- **फ़ाइल या stdin त्रुटियाँ:** समर्थित डायरेक्टरी से लॉन्च करें या एक स्पष्ट दस्तावेज़ पथ पास करें। गैर-खाली रीडर प्रश्न प्रदान करें। पूर्णताएँ EOF या `exit` पर सामान्य रूप से समाप्त हो सकती हैं।
- **कंपाइलेशन त्रुटियाँ:** Java 21 या बाद का सत्यापित करें, फिर `mvn -B -ntp clean test` चलाएँ। PowerShell में, Maven के तर्क जिनमें डॉट वाली प्रॉपर्टी होती है, पूरे को उद्धृत करें, उदाहरण के लिए `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`।

## अगले कदम

[Chapter 4: Practical Samples](../04-PracticalSamples/README.md) पर जारी रखें।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में ही प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->