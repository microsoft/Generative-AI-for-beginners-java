# Foundry लोकल स्प्रिंग बूट ट्यूटोरियल

अपने स्वयं के कंप्यूटर पर एक छोटा भाषा मॉडल चलाएं और इसके OpenAI-संगत
REST एंडपॉइंट को एक जावा कंसोल एप्लिकेशन से कॉल करें। कोई Azure डिप्लॉयमेंट, Azure साइन-इन,
क्लाउड API की, या क्लाउड इनफेरेंस का उपयोग नहीं किया गया है। **GPT-5.6 Luna केवल Azure के लिए है; इसे
Foundry लोकल मॉडल के रूप में कॉन्फ़िगर न करें।**

## संस्करण और पूर्वापेक्षाएँ

| घटक | संस्करण |
| --- | --- |
| जावा | 21 या बाद का |
| मेवन | 3.6.3 या बाद का |
| स्प्रिंग बूट | 4.1.1 |
| OpenAI जावा SDK | 4.63.1 |
| Foundry लोकल SDK (लोकल REST सर्वर) | 2.0.1 |
| Node.js (लोकल REST सर्वर) | 20 या बाद का |
| Foundry लोकल CLI (वैकल्पिक, अलग रिलीज) | 0.10.3 प्रीव्यू |

स्प्रिंग बूट स्प्रिंग फ्रेमवर्क, जैक्सन, JUnit, और मेवन प्लगइन संस्करणों को प्रबंधित करता है।
यह उदाहरण सीधे OpenAI जावा SDK का उपयोग करता है, न कि स्प्रिंग AI। पुरानी अप्रयुक्त
स्प्रिंग AI माइलस्टोन प्रॉपर्टी और रिपोजिटरी हटा दी गई हैं।

सिफारिश किया गया स्टार्टर मॉडल है **Qwen 2.5 0.5B**, CPU संस्करण
`qwen2.5-0.5b-instruct-generic-cpu:4` (कैटलॉग में लगभग 822 एमबी)।
यह GPU execution providers की आवश्यकता से बचता है। अन्य समर्थित, कैश किए गए छोटे मॉडल
को स्पष्ट रूप से चुना जा सकता है। मॉडल और रनटाइम इंस्टॉलेशन के लिए नेटवर्क एक्सेस आवश्यक है;
प्रॉम्प्ट और इनफेरेंस स्थानीय रहते हैं। Foundry लोकल तब भी न्यूनतम रनटाइम
डायग्नोस्टिक्स जारी कर सकता है जब गैर-आवश्यक टेलीमेट्री अक्षम हो।

इस सैंपल डायरेक्टरी से निम्नलिखित कमांड चलाएं।

## जावा बनाएं और परीक्षण करें

```powershell
mvn clean verify
```

HTTP कॉन्ट्रैक्ट परीक्षण एक अल्पकालिक लूपबैक सर्वर शुरू करता है और वास्तविक
OpenAI जावा SDK का अभ्यास करता है। ये अनुरोध सीरियलाइजेशन, मॉडल खोज,
स्पष्ट मॉडल चयन, अस्पष्ट या गलत मॉडल सूचियाँ, HTTP विफलताएं, खाली प्रतिक्रियाएँ,
केवल स्थानीय URL, और कमांड-लाइन विफलता प्रसार को कवर करते हैं। इन्हें मॉडल या
नेटवर्क एक्सेस की जरूरत नहीं होती, सिवाय मेवन डिपेंडेंसी इंस्टॉलेशन के। लाइव टेस्ट इच्छानुसार है।

## लोकल मॉडल शुरू करें

### सिफारिश: पिन्ड SDK सर्वर

कोई नेटिव Foundry लोकल जावा SDK नहीं है। छोटा Node.js हेल्पर
आधिकारिक SDK के REST सर्वर को होस्ट करता है; एप्लिकेशन और चैट अनुरोध जावा ही रहते हैं।

पिन्ड रनटाइम डिपेंडेंसी इंस्टॉल करें:

```powershell
npm ci
```

यदि विंडोज़ x64 SDK के नेटिव इंस्टॉल के दौरान NuGet तक नहीं पहुंच पाता है, तो दिया गया
फॉलबैक उपयोग करें। यह मिलान वाला आधिकारिक GitHub रनटाइम आर्काइव डाउनलोड करता है, रिलीज़ का SHA-256
डाइजेस्ट जांचता है, और इसके DLL को नेटिव एडऑन के पास स्टेज करता है। यह TLS वैलिडेशन को अक्षम नहीं करता,
अधिकार नहीं माँगता, या SDK सोर्स को संशोधित नहीं करता।

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

इस मशीन पर पहले से कैश किए गए मॉडल सूचीबद्ध करें:

```powershell
npm run start:foundry -- --list
```

पहली बार चलाते समय, छोटे CPU मॉडल डाउनलोड की स्पष्ट अनुमति दें:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

बाद के चलाने में, `--download` हटाएं ताकि कैश्ड मॉडल की आवश्यकता हो:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

हेल्पर मिलान वाले कैश्ड मॉडल को प्राथमिकता देता है, उपनाम या सटीक संस्करण ID स्वीकार करता है,
और एक अनुपलब्ध मॉडल को `--download` दिए बिना अस्वीकार कर देता है। यह केवल चयनित
मॉडल के execution provider को पंजीकृत करता है जब आवश्यकता हो। कैश्ड GPU संस्करणों को अभी भी
अनुकूल execution-provider पैकेज और ड्राइवर्स की आवश्यकता हो सकती है।

यदि पोर्ट 5273 व्यस्त है, तो `--port 0` पास करें ताकि उपलब्ध पोर्ट मिले। हेल्पर तैयार होने पर
`FOUNDRY_LOCAL_BASE_URL`, सटीक `FOUNDRY_LOCAL_MODEL` ID, और इसका PID प्रिंट करता है।
जावा में प्रिंट किए गए एंडपॉइंट का उपयोग करें। जावा चलाते समय यह टर्मिनल खुला रखें;
**Ctrl+C** REST सर्वर को रोकता है और मॉडल को रिहा करता है।

डिफ़ॉल्ट कैश `~/.foundry/cache/models` है। अलग मौजूदा कैश के लिए `FOUNDRY_LOCAL_CACHE_DIR` सेट करें।
लॉग और हेल्पर की स्थिति इस सैंपल की `target/foundry-local` डायरेक्टरी में लिखी जाती है।
`mvn clean` चलाने से पहले हेल्पर को रोक दें।

### वैकल्पिक: Foundry लोकल CLI

CLI और SDK के स्वतंत्र रिलीज़ होते हैं: CLI **0.10.3** SDK **1.2.4** को बंडल करता है;
ऊपर दिया गया हेल्पर SDK **2.0.1** का उपयोग करता है। नवीनतम CLI स्थापित करने से
नवीनतम भाषा SDK इंस्टॉल नहीं होता। देखें [CLI रिलीज़ नोट्स](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3)।

विंडोज़ पर, यदि CLI अनुपस्थित हो तो प्रति-उपयोगकर्ता इंस्टॉलर कमांड का उपयोग करें:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

या मौजूदा इंस्टॉलेशन को अपग्रेड करें:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x पुराने `foundry service` कमांड को `foundry server` से बदलता है:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` को पहले से डाउनलोड किया गया मॉडल चाहिए। डाउनलोड कमांड के लिए `foundry model --help` देखें।
स्थिति आउटपुट का वास्तविक एंडपॉइंट उपयोग करें; CLI अन्यथा
स्वचालित रूप से असाइन किए गए पोर्ट का उपयोग करता है। CLI और SDK हेल्पर को
एक ही पोर्ट पर शुरू न करें। समाप्त होने पर:

```powershell
foundry server stop
```

## जावा एप्लिकेशन चलाएँ

दूसरे टर्मिनल में, अपने सर्वर द्वारा प्रिंट किए गए एंडपॉइंट और सटीक मॉडल ID सेट करें:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

या पैकेज्ड एप्लिकेशन चलाएं:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

एकमात्र जावा एंट्रीपॉइंट `com.example.Application` है। यह चयनित
एंडपॉइंट, वास्तविक मॉडल ID, प्रॉम्प्ट, और उत्पन्न प्रतिक्रिया प्रिंट करता है,
फिर अपना स्प्रिंग कंटेक्स्ट और HTTP क्लाइंट बंद कर देता है। विफल इनफेरेंस या अप्राप्य प्रतिक्रिया
टेक्स्ट से सफलता के स्थानापन्न के बजाय विफलता निकास होता है।

### कॉन्फ़िगरेशन

| परिवेश चर | डिफ़ॉल्ट | उद्देश्य |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | लूपबैक HTTP एंडपॉइंट, `/v1` सहित |
| `FOUNDRY_LOCAL_MODEL` | खाली | सटीक मॉडल ID; अन्यथा एकल विज्ञापित मॉडल चुना जाता है |
| `FOUNDRY_LOCAL_PROMPT` | स्थानीय मॉडलों के बारे में एक वाक्य का प्रश्न | कंसोल रनर द्वारा भेजा गया प्रॉम्प्ट |

समतुल्य स्प्रिंग तर्क हैं `--foundry.local.base-url=...`,
`--foundry.local.model=...`, और `--foundry.local.prompt=...`।
केवल लूपबैक HTTP एंडपॉइंट स्वीकार किए जाते हैं। रिमोट/क्लाउड एंडपॉइंट,
एम्बेडेड क्रेडेंशियल्स, क्वेरी स्ट्रिंग्स, और बिना `/v1` के पथ अस्वीकार किए जाते हैं।

खाली मॉडल सेटिंग केवल तभी काम करती है जब `/v1/models` ठीक एक मॉडल विज्ञापित करता है।
विज्ञापित मॉडल जरूरी नहीं कि लोड हो। यदि कई मॉडल विज्ञापित हैं,
तो कैटलॉग क्रम पर निर्भर न रहें, बल्कि सटीक लोड की गई ID सेट करें।

अनुरोध `temperature=0`, 150-टोकन आउटपुट सीमा, 120-सेकंड टाइमआउट, और
कोई स्वचालित पुन: प्रयास नहीं करते। `max_tokens` अनुरोध फ़ील्ड जानबूझकर है:
यह Foundry लोकल REST कॉन्ट्रैक्ट द्वारा समर्थित है, जबकि OpenAI जावा अवांछित करता है।
नए क्लाउड मॉडलों के लिए वह फ़ील्ड। मॉडल की पहचान कॉन्फ़िगरेशन या
डिस्कवरी से आती है, न कि मॉडल के अपने बारे में दावों से।

## लाइव सत्यापन

स्थानीय सर्वर चलने के साथ, सभी परीक्षण चलाएं, जिनमें ऑप्ट-इन लाइव परीक्षण भी शामिल है।
एंडपॉइंट के पोर्ट को उस मान से बदल दें जो आपके सर्वर द्वारा प्रिंट किया गया है। पॉवरशेल में डॉटेड
Maven प्रॉपर्टीज को उद्धृत करें:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

लाइव परीक्षण `Application.main` को बुलाता है, तथ्य "फ़्रांस की राजधानी पेरिस है," देता है,
शहर पूछता है, और पुष्टि करता है कि वास्तविक उत्पन्न पाठ `Paris` है। यह केवल सफल HTTP स्थिति नहीं, बल्कि एक
सैद्धांतिक परिणाम की जाँच करता है।

यह एक एकीकरण जांच है, सटीकता मापदंड नहीं। सत्यापन के दौरान,
0.5B मॉडल ने Java और डायरेक्ट REST दोनों के माध्यम से अलग "2 + 2" प्रॉम्प्ट का उत्तर `3` दिया।
गणितीय या तथ्यात्मक सटीकता के लिए इस पर स्वतंत्र सत्यापन के बिना निर्भर न हों;
गणनाओं के लिए निश्चित उपकरणों का उपयोग करें।

## समस्या निवारण

| लक्षण | जांच करें |
| --- | --- |
| कनेक्शन अस्वीकार किया गया | तैयार संदेश का इंतजार करें; प्रिंट किए गए पोर्ट और `/v1` पथ का उपयोग करें। |
| कई मॉडल विज्ञापनित | `FOUNDRY_LOCAL_MODEL` को लोड किए गए मॉडल के सटीक आईडी पर सेट करें। |
| मॉडल गायब है | `--list` का उपयोग करें, या स्पष्ट रूप से डाउनलोड की अनुमति देने के लिए `--download` का उपयोग करें। |
| GPU प्रदाता विफल या अटक गया | छोटे CPU मॉडल का उपयोग करें। कैश्ड GPU मॉडल को अभी भी उसके प्रदाता की आवश्यकता होती है। |
| CLI `initializing` रहता है | `foundry server logs --lines 80` पढ़ें; डेमन को रोकें और SDK हेल्पर का उपयोग करें। |
| NuGet TLS/डाउनलोड विफलता | नेटवर्क एक्सेस ठीक करें या ऊपर वाले सत्यापित Windows x64 फॉलबैक का उपयोग करें। TLS अक्षम न करें। |
| पोर्ट व्यस्त है | `--port 0` का उपयोग करें और प्रिंट किए गए एंडपॉइंट के साथ Java को कॉन्फ़िगर करें। |
| कोई विकल्प नहीं या खाली टेक्स्ट | ऐप जानबूझकर विफल होता है; मॉडल और रनटाइम लॉग की जांच करें। |

## स्रोत और संदर्भ

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): वन-शॉट स्प्रिंग बूट रनर।
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): टाइप्ड डिस्कवरी और स्थानीय चैट पूर्णता।
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP कॉन्ट्रैक्ट, रनर, और लाइव परीक्षण।
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): आधिकारिक SDK REST सर्वर कैश्ड-मॉडल चयन और सफाई के साथ।
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): सत्यापित Windows x64 नेटिव-रनटाइम फॉलबैक।
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), और [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): कॉन्फ़िगरेशन और निर्भरता।
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks)।
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1)।
- [Chapter 04: Practical samples](../README.md)।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
इस दस्तावेज़ का अनुवाद AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) का उपयोग करके किया गया है। जबकि हम सटीकता के लिए प्रयास करते हैं, कृपया ध्यान दें कि स्वचालित अनुवादों में त्रुटियाँ या अशुद्धियाँ हो सकती हैं। मूल दस्तावेज़ अपनी मूल भाषा में ही प्रामाणिक स्रोत माना जाना चाहिए। महत्वपूर्ण जानकारी के लिए, पेशेवर मानव अनुवाद की सिफारिश की जाती है। इस अनुवाद के उपयोग से उत्पन्न किसी भी गलतफहमी या गलत व्याख्या के लिए हम उत्तरदायी नहीं हैं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->