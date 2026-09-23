# मुख्य जेनेरेटिभ एआई प्रविधिहरू ट्यूटोरियल

## सामग्री तालिका

- [पूर्वआवश्यकताहरू](#पूर्वआवश्यकताहरू)
- [सुरु गर्दै](#सुरु-गर्दै)
- [मोडल चयन मार्गदर्शन](#मोडल-चयन-मार्गदर्शन)
- [ट्यूटोरियल १: LLM पुरा र कुराकानी](#ट्यूटोरियल-१-llm-कम्प्लेशन्स-र-कुराकानी)
- [ट्यूटोरियल २: फंक्शन कलिङ](#ट्यूटोरियल-२-फंक्शन-कलिङ)
- [ट्यूटोरियल ३: RAG (रिकभरी-अगुमेन्टेड जेनेरेशन)](#ट्यूटोरियल-३-rag-रिकभरी-अगुमेन्टेड-जेनेरेशन)
- [ट्यूटोरियल ४: जिम्मेवार एआई](#ट्यूटोरियल-४-जिम्मेवार-एआई)
- [उदाहरणहरूमा साझा ढाँचा](#साझा-ढाँचा-उदाहरणहरूमा)
- [यूनिट टेस्टहरू](#यूनिट-टेस्टहरू)
- [क्रमवार प्रत्यक्ष प्रमाणीकरण](#क्रमवार-प्रत्यक्ष-प्रमाणीकरण)
- [समस्या समाधान](#समस्या-समाधान)
- [अगाडिका कदमहरू](#अर्को-चरणहरू)

## अवलोकन

चार स्वतन्त्र जाभा प्रोग्रामहरूले कुराकानी, संवाद इतिहास, फङ्क्सन कलिङ, सम्पूर्ण कागजात रिकभरी-अगुमेन्टेड जेनेरेशन (RAG), र जिम्मेवार-एआई प्रतिक्रिया व्यवस्थापन देखाउँछन्। सबै कुराकानी अनुरोधहरू पूर्वनिर्धारित रूपमा **GPT-5.6 Luna reasoning effort `none` संग** लक्षित छन्।

यी उदाहरणहरूले आधिकारिक OpenAI Java SDK प्रयोग गर्छन् Azure OpenAI को v1 endpoint सँग, [Microsoft को SDK मार्गदर्शन](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) अनुसार। पुरानो `azure-ai-openai` प्याकेज अब निर्भरता होइन। म्यासेज-आधारित कार्यप्रवाह सिकाउन Chat Completions राखिएको छ; अन्य API विकल्पहरूका लागि [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) हेर्नुहोस्।

## पूर्वआवश्यकताहरू

- जाभा २१ वा पछि र Maven ३.६.३ वा पछि।
- `gpt-5.6-luna` नामको Azure OpenAI च्याट डिप्लोयमेन्ट, वा उपयुक्त Chat Completions सेटिङहरू सहितको ओभरराइड।
- स्रोतमा **Cognitive Services OpenAI User** भूमिका भएको Azure आइडेन्टिटीमा साइन-इन गरिएको। स्थानीय विकासले तपाईंको Azure CLI साइन-इन प्रयोग गर्दछ; होस्ट गरिएको अनुप्रयोगहरूले managed identity प्रयोग गर्न सक्छन्।
- स्रोत सेटअप र साइन-इन निर्देशनहरूको लागि [अध्याय २](../02-SetupDevEnvironment/getting-started-azure-openai.md) हेर्नुहोस्।

[Maven कन्फिगरेसन](../../../03-CoreGenerativeAITechniques/examples/pom.xml) ले यी भर्सनहरू निश्चित गर्दछ, २०२६-०९-१४ मा जाँच गरिएको:

| कम्पोनेन्ट | संस्करण | उद्देश्य |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | आधिकारिक Azure v1-समर्थित क्लाइन्ट |
| `com.azure:azure-identity` | 1.18.6 | कीलेस प्रमाणिकरण र टोकन रिफ्रेस |
| `net.objecthunter:exp4j` | 0.4.8 | कोड मूल्याङ्कन बिना अंकगणित अभिव्यक्ति पार्सिङ |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | अफलाइन जुपिटर यूनिट टेस्टहरू |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | जाभा २१ कम्पाइल, टेस्ट, चलाउन योग्य उदाहरणहरू |

कम्पाइलरले `--release 21` प्रयोग गर्छ। यी स्वतन्त्र उदाहरणहरूले Spring Boot, Spring AI, वा LangChain4j निर्भरता आवश्यक पर्दैन।

## सुरु गर्दै

रिपोजिटरीको मूलबाट साधन_endpoint र वैकल्पिक डिप्लोयमेन्ट ओभरराइड आफ्नो शेलमा सेट गर्नुहोस्।

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

टेस्टहरूले न त Azure क्रेडेन्सियल्स चाहिन्छ न त endpoint। Maven स्वतः वातावरण फाइल पढ्दैन; जीवन्त उदाहरण सुरु गर्न प्रयोग गरिने शेलमा भेरिएबलहरू सेट गर्नुहोस्। IDE लेन्चेसका लागि आफ्नो लेन्च कन्फिगरेसनले वातावरण सुनिश्चित गर्नुहोस्।

## मोडल चयन मार्गदर्शन

| वातावरण भेरिएबल | अर्थ | पूर्वनिर्धारित |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure स्रोत मूल वा पहिल्यै सामान्यीकृत `/openai/v1` URL | जीवन्त रनहरूको लागि आवश्यक |
| `AZURE_OPENAI_DEPLOYMENT` | च्याट डिप्लोयमेन्ट नाम, मोडल संस्करण होइन | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | अलग एम्बेडिङ डिप्लोयमेन्ट कन्फिगरेसन, यी चार प्रोग्रामहरूले प्रयोग गर्दैनन् | `text-embedding-3-small` |

खाली डिप्लोयमेन्ट ओभरराइडहरूले पूर्वनिर्धारितहरू प्रयोग गर्छन्। कन्फिगरेसनले `/openai/v1` ठीक एक पटक थप्छ र endpoint मा क्रेडेन्सियल्स, क्वेरी स्ट्रिङहरू, र पुराना डिप्लोयमेन्ट पथहरू अस्विकार गर्छ।

प्रत्येक च्याट अनुरोधले स्पष्ट रूपमा `reasoningEffort(ReasoningEffort.NONE)` र `maxCompletionTokens(...)` सेट गर्छ। कुनै पनि अनुरोधले `temperature`, `top_p`, वा पुरानो completion-token विकल्प सेट गर्दैन। यसमा उपकरण चयन र उपकरण परिणाम पछि फलो-अपहरू समावेश छन्। GPT-5.6 च्याट कम्प्लेशन्स उपकरणहरूले reasoning effort `none` आवश्यक गर्छ; [Microsoft को च्याट मार्गदर्शन](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt) हेर्नुहोस्।

**यस अध्यायमा कुनै स्ट्रिमिङ वा एम्बेडिङ प्रवेश बिन्दु छैन।** पाठकले यसको सम्पूर्ण कागजात पुनःप्राप्त गर्छ, वेक्टर होइन। यदि तपाईं यसलाई एम्बेडिङहरू सहित विस्तार गर्नुहुन्छ भने, `text-embedding-3-small` जस्तो अलग एम्बेडिङ डिप्लोयमेन्ट प्रयोग गर्नुहोस्, कहिल्यै Luna होइन।

## ट्यूटोरियल १: LLM कम्प्लेशन्स र कुराकानी

स्रोत: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)।

प्रोग्रामले सजिलो जाभा स्ट्रिम्स व्याख्या, दुई-टर्न HashMap/TreeMap संवाद, र अन्तरक्रियात्मक च्याट चलाउँछ। दोस्रो टर्नमा पहिलो सहायक जवाफ समावेश छ; प्रत्येक अन्तरक्रियात्मक टर्नले आफ्नो पूर्व संवाद पनि पठाउँछ।

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` ले डिप्लोयमेन्ट र स्पष्ट reasoning सेटिंग्स आपूर्त गर्दछ। अन्तरक्रियात्मक च्याटले खाली लाइनहरू छोड्छ, `exit` वा EOF मा अन्त्य गर्छ, र सिस्टम संदेश plus नौ पूरा भएका प्रयोगकर्ता/सहायक टर्नहरू राख्छ। टर्न-काउन्ट ट्रिमिङ शैक्षिक सीमा हो, टोकन बजेट ग्यारेन्टी होइन।

उदाहरण डाइरेक्टरीबाट:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

तीन आरम्भिक उत्तरहरू अपेक्षा गर्नुहोस्, त्यसपछि `You:` प्रॉम्प्ट। प्रत्येक गैर-खाली अन्तरक्रियात्मक प्रश्न एक अनुरोध थप्छ। प्रतिएक अन्तरक्रियात्मक टर्नको कम्प्लेशन सीमाहरू २००, ३००, ४००, त्यसपछि ५०० टोकन हुन्।

## ट्यूटोरियल २: फंक्शन कलिङ

स्रोत: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)।

SDK ले एनोटेटेड `WeatherArguments` र `CalculationArguments` रेकर्डहरूबाट JSON स्कीमाहरू निकाल्छ। आवश्यक उपकरण छनोटले प्रत्येक उदाहरणलाई उपकरण प्रोटोकल अभ्यास गराउँछ, मोडलको स्वतन्त्र जवाफ स्वीकार नगरी।

१. अनुमति प्राप्त उपकरण, reasoning effort `none`, र ३००-टोकन कम्प्लेशन सीमा सहित प्रश्न पठाउनुहोस्।
२. `tool_calls` समाप्ति कारण आवश्यक पर्छ, फङ्क्सन नाम र कल IDs मान्य गर्नुहोस्, र टाइप गरिएका JSON आर्गुमेन्टहरू पार्स गर्नुहोस्।
३. स्थानीय फंक्शन कार्यान्वयन गर्नुहोस्। मोडलले जाभा वा मनमानी कोड चलाउँदैन।
४. सहायक उपकरण-कल सन्देश एक पटक थप्नुहोस्, त्यसपछि प्रत्येक परिणाम आफ्नो मिल्दोजुल्दो `tool_call_id` सहित।
५. उपकरण बिना अन्तिम ३००-टोकन अनुरोध पठाउनुहोस् र पूरा, खाली नभएको उत्तर आवश्यक पर्नुहोस्।

`get_weather` ले **नक्कली** मौसम फर्काउँछ, प्रत्यक्ष मौसम होइन। यो सहरको आदर गर्छ र मागिएमा २२ डिग्री सेल्सियसलाई फरेनहाइटमा रूपान्तरण गर्छ। `calculate` ले exp4j मार्फत दिइएको अभिव्यक्ति मूल्याङ्कन गर्छ, जस्तै `१५% of २४०` र `२ + ३ * ४` समावेश गर्छ, र खाली, अत्यधिक ठूला, अमान्य, वा अमर्यादित गणनाहरू अस्वीकृत गर्छ। यो फ्लोटिङ पोइन्ट अंकगणित प्रयोग गर्छ, वित्तीय दशमलव सटीकताका लागि होइन।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

अपेक्षा गर्नुहोस् `Function: get_weather`, नक्कली सिएटल मौसम, `Function: calculate`, `Function result: 36`, र दुई अन्तिम उत्तरहरू। कुनै stdin वा बाह्य मौसम प्रमाणपत्र आवश्यक छैन। सफल रनले ठीक चार च्याट अनुरोध प्रयोग गर्छ।

## ट्यूटोरियल ३: RAG (रिकभरी-अगुमेन्टेड जेनेरेशन)

स्रोत: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)। इनपुट: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)।

यो परिचयात्मक RAG उदाहरणले एउटा पुरै UTF-8 कागजात पुनः प्राप्त गर्छ र सोधिएको प्रश्नसँग प्रयोगकर्ता सन्देशमा समावेश गर्छ। एउटा अलग सिस्टम सन्देशले मोडललाई कागजात सामग्री अविश्वसनीय डाटा मान्न र त्यस सन्दर्भबाट मात्र जवाफ दिन निर्देशन दिन्छ। यदि कागजातमा उत्तर छैन भने, अनुरोधित जवाफ हुन्छ: `मैले प्रदान गरिएको कागजातमा त्यो जानकारी फेला पार्न सकिनँ।`

ग्राउण्डिङले भ्रामकता घटाउन सक्छ, तर डेलिमिटरहरू वा सिस्टम निर्देशनहरूले शुद्धता सुनिश्चित गर्दैनन् र प्रत्येक प्रॉम्प्ट इन्जेक्शन रोक्दैन। प्रत्यक्ष उत्तरहरू समीक्षा गर्नुहोस्। उत्पादन RAG सामान्यतया टुक्र्याइने, पुनःप्राप्ति, उद्धरण, पहुँच नियन्त्रण, र मूल्यांकन थप्छ।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

एउटा प्रश्न प्रविष्ट गर्नुहोस्, जस्तै `कागजातले कुन प्रमाणीकरण विधि वर्णन गर्छ?` अपेक्षा गर्नुहोस् Microsoft Entra ID को उल्लेख भएको उत्तर। प्रोग्रामले ५००-टोकन कम्प्लेशन सीमा सहित एक च्याट अनुरोधपछि बन्द हुन्छ।

पूर्वनिर्धारित फाइल खोज रिपोजिटरी मूल, अध्याय डायरेक्टरी, वा उदाहरण डायरेक्टरीबाट हुन्छ। स्पष्ट पथ पनि समर्थन गरिएको छ:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

इनपुटहरू गैर-खाली हुनुपर्छ: अधिकतम ३२ KiB UTF-8 कागजात डाटा र २००० प्रश्न वर्णहरू। हराएका फाइलहरू, खाली/EOF प्रश्नहरू, र अत्याधिक ठूला इनपुटहरू inference अघि असफल हुन्छन्।

## ट्यूटोरियल ४: जिम्मेवार एआई

स्रोत: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)।

छ वटा परीक्षणहरूले हानिकारक निर्देशनहरू, घृणा भाषण, गोपनीयता, चिकित्सकीय गलत सूचना, गैरकानूनी सामग्री, र सौम्य जिम्मेवार-एआई प्रश्न समेट्छ। प्रोग्रामले फिल्टर ट्रिगर हुने अनुमान नगरी प्रतिक्रिया निरीक्षण गर्छ।

| परिणाम | प्रमाण |
| --- | --- |
| `FILTERED` | स्पष्ट `content_filter` / `ResponsibleAIPolicyViolation` त्रुटि कोड, वा कम्प्लेशन `content_filter` समाप्ति कारण |
| `REFUSED` | गैर-खाली संरचित `message.refusal` क्षेत्र |
| `POSSIBLE_REFUSAL` | सामान्य पाठमा खुलेको अस्वीकृति वाक्यांश; समीक्षा आवश्यक heuristics |
| `GENERATED` | पूरा भएको गैर-खाली प्रतिक्रिया; यसको सामग्री सुरक्षित भएको प्रमाण होइन |

साधारण HTTP ४०० **छैन** फिल्टर प्रमाण। अवैध प्यारामिटरहरू, प्रमाणीकरण विफलता, दर सीमा, सर्भर त्रुटिहरू, बिग्रिएका प्रतिक्रिया, र कटौती गरिएको आउटपुट रन विफल गर्छ, गलत सुरक्षा सफलताको सट्टा। सौम्य व्याख्यामा "हानिकारक सामग्री" जस्ता बिषयहरू अस्वीकृतिमा गनिदैनन्।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

छ वटा वर्गीकरण परिणामहरू र अवलोकनहरू सुरक्षा प्रमाणपत्र होइन भनी सारांश अपेक्षा गर्नुहोस्। प्रत्येक परीक्षणमा ३००-टोकन कम्प्लेशन सीमा छ। अनपेक्षित सिर्जना र सम्भावित अस्वीकृतिहरू म्यानुअली समीक्षा गर्नुहोस्; सौम्य तुलना एक ठोस जिम्मेवार-एआई व्याख्या उत्पादन गर्नुपर्छ। कुनै stdin आवश्यक छैन।

## साझा ढाँचा उदाहरणहरूमा

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) ले endpoint सामान्यीकरण, डिप्लोयमेन्ट ओभरराइडहरू, कीलेस प्रमाणिकरण, र च्याट विकल्पहरू केन्द्रीकृत गर्छ:

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

टोकन आपूर्तिकर्ताले आवश्यक अनुसार पहुँच टोकन रिफ्रेस गर्छ। टोकनहरू लग नगर्नुहोस् वा यसलाई API कुञ्जीले प्रतिस्थापन नगर्नुहोस्। प्रत्येक प्रोग्रामले आफ्नो क्लाइन्ट पुनः प्रयोग गर्छ र `finally` वा आफ्नै `AutoCloseable` wrapper मार्फत बन्द गर्छ; SDK को `OpenAIClient` खुद `AutoCloseable` होइन।

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) ले पूरा भएको, गैर-खाली पाठ उत्तर आवश्यक पार्दछ। खाली विकल्पहरू, अस्वीकृतिहरू, फिल्टरहरू, र कटौती गरिएको उत्तरहरू सफलताका रूपमा मौन रूपमा मुद्रित हुँदैनन्। जिम्मेवार-एआई उदाहरणले अपेक्षित फिल्टर/अस्वीकृति परिणामहरू स्पष्ट रूपमा व्यवस्थापन गर्छ। असफलताले Java/Maven प्रक्रियालाई गैर-शून्य निकास कोड दिन्छ।

**स्वचालित SDK पुन: प्रयासहरू अक्षम छन्** साझा कम-RPM डिप्लोयमेन्टहरूमा अनुरोध गणनाहरू पूर्वानुमानयोग्य राख्न। प्रत्येक inference अनुरोधको ६० सेकेण्ड टाइमआउट छ। टोकन प्राप्तिमा थप समय लाग्न सक्छ। अनुप्रयोग स्तरको तालिका मात्र कोटा सम्मान गर्नुपर्छ; असफल शुल्क लाग्ने अनुरोधलाई अन्धाधुंध पुन: चलाउनु हुँदैन।

## यूनिट टेस्टहरू

उदाहरण डाइरेक्टरीबाट:

```powershell
mvn -B -ntp clean test
```

टेस्ट ट्रान्सपोर्टले SDK HTTP तहलाई पूर्ण रूपमा प्रतिस्थापन गर्छ, वास्तविक क्रमबद्ध अनुरोध बॉडीहरू समात्छ, र कतारबद्ध प्रतिक्रिया आपूर्ति गर्छ। यसले कुनै सॉकेट खोल्दैन, Azure टोकनहरू प्राप्त गर्दैन, र अनपेक्षित अनुरोधहरूमा असफल हुन्छ। यी टेस्टहरूले अनुप्रयोग व्यवहार र SDK प्रोटोकल मान्य गर्छन्, प्रत्यक्ष मोडल गुणस्तर वा डिप्लोयमेन्ट उपलब्धता होइन।

| टेस्ट सूट | कभरज |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Endpoint सामान्यीकरण/अस्वीकार, डिप्लोयमेन्ट ओभरराइडहरू, reasoning र टोकन विकल्पहरू |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | प्रत्येक कम्प्लेशन कार्यप्रवाह, सन्देश इतिहास, पूरा टर्न ट्रिमिङ, EOF, असफलता |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | उपकरण स्कीमा, टाइप गरिएका आर्गुमेन्टहरू, अंकगणित, IDs, बहुविध उपकरण परिणामहरू, असफल फलो-अपहरू |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | फाइल खोज, UTF-8, साइज सीमा, ग्राउण्डिङ पेलोड, इनपुट र API त्रुटिहरू |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | सबै छ वटा परीक्षणहरू, स्पष्ट फिल्टरहरू, अस्वीकृति वर्गीकरण, साधारण ४०० र अन्य असफलताहरू |

एउटा सूटका लागि, `mvn -B -ntp test "-Dtest=FunctionsAppTest"` प्रयोग गर्नुहोस्। साझा फिक्स्चरहरू [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) मा जीवन्त छन्।

## क्रमवार प्रत्यक्ष प्रमाणीकरण

प्रत्यक्ष कलहरू यूनिट टेस्टहरूबाट अलग छन्। निम्न आदेशहरू **एक-एक गरी**, रिपोजिटरी मूलबाट, क्रेडेन्सियल्स र डिप्लोयमेन्ट पहुँच तयार भएपछि मात्र चलाउनुहोस्। कुनै सेवाहरू वा दीर्घकालिक प्रक्रिया आवश्यक छैन।

साझा **१० अनुरोध/मिनट** डिप्लोयमेन्टका लागि, सम्पूर्ण अर्को प्रोग्राम अघि पर्याप्त कोटा आरक्षित गर्नुहोस्: ५, ४, १, त्यसपछि ६ अनुरोध। क्रमिक प्रक्रियाहरू मात्र दर-सीमा पालन ग्यारेन्टी गर्दैनन्। सबै कलकर्तासँग रोलिङ मिनेट समन्वय गर्नुहोस्; चार कलहरू एकैपटक नपठाउनुहोस्।

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**१. कम्प्लेशन्स, बहु-टर्न, र दुई अन्तरक्रियात्मक टर्नहरू:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

सबै तीन अनुभाग शीर्षक, पाँच उत्तरहरू, अन्तिम अन्तरक्रियात्मक उत्तर जसले एडा सम्झिन्छ, `Goodbye!`, र निकास कोड 0 जाँच गर्नुहोस्। बजेट: **५ अनुरोधहरू, अधिकतम १,९०० पूर्णता टोकनहरू**। सानो रनको लागि, केवल `exit` पाइप गर्नुहोस्: ३ अनुरोधहरू / ९०० टोकनहरू, तर त्यसले अन्तरक्रियात्मक अनुमानहरू अभ्यास गर्दैन।

**२. दुवै फंक्शन-कलिङ वर्कफलोहरु:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

दुवै फंक्शन नामहरू, सिमुलेट गरिएको सिएटल मौसम, गणना गरिएको परिणाम ३६, दुई अन्तिम उत्तरहरू, र निकास कोड 0 जाँच गर्नुहोस्। बजेट: **४ अनुरोधहरू, अधिकतम १,२०० पूर्णता टोकनहरू**।

**३. कागजात-आधारित उत्तर:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

कागजात पथ, माइक्रोसफ्ट इन्ट्रा आइडी उल्लेख गर्ने उत्तर, र निकास कोड 0 जाँच गर्नुहोस्। बजेट: **१ अनुरोध, अधिकतम ५०० पूर्णता टोकनहरू**। विद्यमान [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) मात्र आवश्यक इनपुट फाइल हो। अनुपस्थित विषयबस्तुको बारेमा सोध्ने वैकल्पिक दोस्रो रनले पास गर्नु पर्छ र १ अनुरोध / ५०० टोकनहरू थप गर्छ।

**४. जिम्मेवार-एआई अवलोकनहरू:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

छ वटा वर्गहरू र अवलोकनात्मक सारांश जाँच गर्नुहोस्, सिर्जना गरिएको सामग्रीको समीक्षा गर्नुहोस्, र प्राविधिक पूर्णताका लागि निकास कोड 0 आवश्यक छ। सफल प्रक्रिया निकासले मोडेल सुरक्षा प्रमाणित गर्दैन। बजेट: **६ अनुरोधहरू, अधिकतम १,८०० पूर्णता टोकनहरू**।

**चार आदेशहरूको जम्मा: १६ च्याट अनुरोधहरू र अधिकतम ५,४०० पूर्णता टोकनहरू**, साथै इनपुट टोकनहरू (दोहोरो वार्तालाप र उपकरण स्कीमा/इतिहास सहित)। शून्य एम्बेड्डिङ अनुरोधहरू छन्। वास्तविक टोकन प्रयोग मोडेलमा निर्भर हुन्छ र विशेष गरी फिल्टर्ड प्रॉम्प्टहरूको लागि कम हुन सक्छ। डलर खर्च वितरण मूल्य निर्धारणमा निर्भर हुन्छ; कुनै निश्चित मौद्रिक अनुमान छैन। सबै अनुरोध सीमाहरू बिना हाते पुन:रन मानिन्छ। प्रत्येक कमाण्ड पछि तुरुन्तै `$LASTEXITCODE` जाँच गर्नुहोस्; शून्य नभए सफल रूपमा रन पूरा भएको हुँदैन।

## समस्या समाधान

- **एन्डप्वाइन्ट गायब / ४०१ / ४०३:** लन्च प्रक्रियामा एन्डप्वाइन्ट सेट गर्नुहोस्, आफ्नो स्थानीय Azure साइन-इन र स्रोत-स्कोप गरिएको रोल जाँच गर्नुस्, र अनजानेमा पहिचान वातावरण ओभरराइडहरूको खोजी गर्नुस्।
- **४०० / ४०४:** पुष्टि गर्नुहोस् कि डिप्लोयमेन्ट अवस्थित छ र reasoning effort `none` सहितको Chat Completions समर्थित छ। HTTPS स्रोत मूल वा `/openai/v1` URL प्रयोग गर्नुहोस्, पुरानो डिप्लोयमेन्ट URL होइन। सामान्य ४०० त्रुटिहरू प्राविधिक असफलताहरू हुन्, सुरक्षा ब्लकहरू होइनन्।
- **४२९:** साझा RPM र टोकन कोटा समन्वय गरी पुन: प्रयास गर्नुहोस्। उदाहरणहरूले जानू-अटो-रिट्राइ गर्दैन।
- **`अपूर्ण च्याट प्रतिक्रिया: लम्बाई`:** आउटपुटले पूर्णता सीमा छुन पुगेको छ। सीमा र यसको दर्ता बजेट बढाउनु अघि प्रतिक्रिया र प्रॉम्प्ट जाँच गर्नुहोस्; एक ट्रंकेट गरिएको रनलाई सफलको रूपमा रेकर्ड नगर्नुहोस्।
- **फाइल वा stdin त्रुटिहरू:** समर्थित डाइरेक्टरीबाट लन्च गर्नुहोस् वा स्पष्ट कागजात पथ पास गर्नुहोस्। खाली नभएको पाठक प्रश्न प्रदान गर्नुहोस्। पूर्णता सामान्यतया EOF वा `exit` मा समाप्त हुन सक्छ।
- **कम्पाइलेशन त्रुटिहरू:** Java 21 वा पछि भेरिफाइ गर्नुहोस्, त्यसपछि `mvn -B -ntp clean test` चलाउनुहोस्। PowerShell मा, डट प्रोपर्टी समावेश गरिएको सम्पूर्ण Maven आर्गुमेन्ट उद्धृत गर्नुहोस्, जस्तै `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`।

## अर्को चरणहरू

[अध्याय ४: व्यवहारिक नमूनाहरू](../04-PracticalSamples/README.md) तिर जारी राख्नुहोस्।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी सही हुन प्रयास गर्छौं, तर कृपया जानकार हुनुस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छन्। मूल दस्तावेज़ यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा त्रुटिको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->