# Foundry Local Spring Boot ट्युटोरियल

तपाईँको आफ्नै मेसिनमा सानो भाषा मोडल चलाउनुहोस् र यसको OpenAI-संगत
REST अन्त बिन्दु एक Java कन्सोल अनुप्रयोगबाट कल गर्नुहोस्। कुनै Azure परिनियोजन, Azure साइन-इन,
क्लाउड API कुञ्जी, वा क्लाउड इन्फेरेन्स प्रयोग गरिँदैन। **GPT-5.6 Luna Azure-मात्र हो; यसलाई Foundry Local मोडलको रूपमा कन्फिगर नगर्नुस्।**


## संस्करणहरू र पूर्वशर्तहरू

| कम्पोनेन्ट | संस्करण |
| --- | --- |
| Java | 21 वा पछिल्लो |
| Maven | 3.6.3 वा पछिल्लो |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (स्थानीय REST सर्भर) | 2.0.1 |
| Node.js (स्थानीय REST सर्भर) | 20 वा पछिल्लो |
| Foundry Local CLI (वैकल्पिक, अलग रिलीज) | 0.10.3 पूर्वावलोकन |

Spring Boot ले Spring Framework, Jackson, JUnit, र Maven प्लगइन संस्करणहरू व्यवस्थापन गर्दछ।
यो उदाहरणले प्रत्यक्ष रूपमा OpenAI Java SDK प्रयोग गर्दछ, Spring AI होइन। पुरानो अप्रयोग गरिने
Spring AI माइलस्टोन प्रोपर्टी र रिपोजिटरी हटाइएको छ।

सिफारिस गरिएको स्टार्टर मोडल **Qwen 2.5 0.5B** हो, CPU भेरियन्ट
`qwen2.5-0.5b-instruct-generic-cpu:4` (क्याटलगमा लगभग ८२२ MB)।
यसले GPU कार्यान्वयन प्रदायकहरू आवश्यक हुँदैन। अन्य समर्थित, क्यास गरिएको साना मोडलहरू
स्पष्ट रूपमा चयन गर्न सकिन्छ। मोडल र रनटाइम स्थापना नेटवर्क पहुँच आवश्यक पर्छ;
प्रॉम्प्टहरू र इन्फेरेन्स स्थानीय रहन्छ। Foundry Local ले पनि न्यूनतम रनटाइम
डायग्नोस्टिक्स जारी गर्न सक्छ, गैर-आवश्यक टेलिमेट्री अक्षम गर्दा पनि।

तलका कमाण्डहरू यस नमुना डाइरेक्टरीबाट चलाउनुहोस्।

## Java निर्माण र परीक्षण

```powershell
mvn clean verify
```

HTTP अनुबंध परीक्षणहरूले एक क्षणिक लूपब्याक सर्भर सुरु गर्छन् र वास्तविक
OpenAI Java SDK को अभ्यास गर्छन्। तिनीहरूले अनुरोध सिरियलाइजेसन, मोडल पत्ता लगाउने, स्पष्ट मोडल
चयन, अस्पष्ट वा गलत मोडल सूचीहरू, HTTP असफलताहरू, खाली प्रतिक्रिया,
स्थानीय-मात्र URL हरू, र कमाण्ड-लाइन असफलता प्रसारण कभर गर्छन्। तिनीहरूलाई मोडल वा
Maven निर्भरता स्थापना बाहेक नेटवर्क पहुँच आवश्यक पर्दैन। लाइभ परीक्षण विकल्पसहित हो।

## स्थानीय मोडल सुरु गर्नुहोस्

### सिफारिस गरिएको: पिन गरिएको SDK सर्भर

Foundry Local Java SDK मूलभूत रूपमा छैन। सानो Node.js सहायकले
आधिकारिक SDK को REST सर्भर होस्ट गर्दछ; अनुप्रयोग र च्याट अनुरोध Java मा रहन्छ।

पिन गरिएको रनटाइम निर्भरता स्थापना गर्नुहोस्:

```powershell
npm ci
```

यदि Windows x64 SDK को मूल स्थापना गर्दा NuGet पहुँच्न सक्दैन भने, दिइएका
फालब्याक प्रयोग गर्नुहोस्। यसले मेल खाने आधिकारिक GitHub रनटाइम आर्काइभ डाउनलोड गर्छ, प्रकाशनको
SHA-256 डाइजेस्ट जाँच गर्छ, र यसको DLL हरू मूल addon सँगै स्टेज गर्छ। यसले TLS प्रमाणीकरण नअक्षम पार्दैन,
अधिकार आवश्यक पर्दैन, वा SDK स्रोत संशोधन गर्दैन।

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

यस मेसिनमा पहिले नै क्यास गरिएको मोडलहरू सूचीबद्ध गर्नुहोस्:

```powershell
npm run start:foundry -- --list
```

पहिलो पटक चलाउँदा सानो CPU मोडल डाउनलोड स्पष्ट रूपमा अनुमति दिनुहोस्:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

पछि चलाउँदा, `--download` हटाउनुहोस् जसले क्यास गरिएको मोडल आवश्यक पार्छ:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

सहायकले मेल खाने क्यास गरिएको मोडललाई प्राथमिकता दिन्छ, उपनाम वा ठ्याक्कै भेरियन्ट ID स्वीकार्छ,
र मोडल अनुपस्थित भएमा `--download` नदिइएमा अस्वीकार गर्छ। आवश्यक पर्दा मात्र चयनित मोडलको
कार्यान्वयन प्रदायक दर्ता गरिन्छ। क्यास गरिएको GPU भेरियन्टहरूले अझै पनि उपयुक्त
कार्यान्वयन-प्रदायक प्याकेज र ड्राइभर चाहिन्छ।

यदि पोर्ट 5273 प्रयोगमा छ भने, उपलब्ध पोर्टका लागि `--port 0` पास गर्नुहोस्। सहायकले मुद्रण गर्दछ
`FOUNDRY_LOCAL_BASE_URL`, ठ्याक्कै `FOUNDRY_LOCAL_MODEL` ID, र यसको PID तयार हुँदा।
Java मा मुद्रित अन्तविंदु प्रयोग गर्नुहोस्। Java चलाउँदा यो टर्मिनल खुल्ला राख्नुहोस्;
**Ctrl+C** ले REST सर्भर रोक्छ र मोडल रिलिज गर्छ।

पूर्वनिर्धारित क्यास `~/.foundry/cache/models` हो। फरक अवस्थित क्यासको लागि `FOUNDRY_LOCAL_CACHE_DIR` सेट गर्नुहोस्।
लगहरू र सहायक अवस्थाको लेखन यो नमूना `target/foundry-local` डाइरेक्टरी भित्र गरिन्छ। `mvn clean` चलाउनु अघि सहायक रोक्नुहोस्।


### वैकल्पिक: Foundry Local CLI

CLI र SDK स्वतन्त्र रिलीज छन्: CLI **0.10.3** मा SDK **1.2.4** समावेश गरिएको छ;
माथिको सहायक SDK **2.0.1** प्रयोग गर्छ। नवीनतम CLI स्थापना गर्दा
नवीनतम भाषा SDK स्थापना हुँदैन। [CLI रिलिज नोटहरू](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3) हेर्नुहोस्।

Windows मा, CLI नहुँदा प्रति-प्रयोगकर्ता इन्स्टल आदेश प्रयोग गर्नुहोस्:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

वा पहिले इन्स्टल गरिएको संस्करण अपडेट गर्नुहोस्:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x ले पुराना `foundry service` आदेशहरूलाई `foundry server` सँग प्रतिस्थापन गर्छ:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` लाई पहिले नै डाउनलोड गरिएको मोडल चाहिन्छ। डाउनलोड आदेशहरूको लागि `foundry model --help` जाँच गर्नुहोस्।
स्थिति आउटपुटको वास्तविक अन्तविंदु प्रयोग गर्नुहोस्; CLI ले अन्यथा
स्वचालित रूपमा तोकिएको पोर्टमा डिफल्ट गर्छ। CLI र SDK सहायकलाई
एउटै पोर्टमा सुरु नगर्नुहोस्। समाप्त हुँदा:

```powershell
foundry server stop
```

## Java अनुप्रयोग चलाउनुहोस्

दोस्रो टर्मिनलमा, सर्भरले मुद्रण गरेको अन्तविंदु र ठ्याक्कै मोडल ID सेट गर्नुहोस्:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

वा प्याकेज्ड अनुप्रयोग चलाउनुहोस्:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

एकल Java प्रवेश बिन्दु `com.example.Application` हो। यसले चयनित
अन्तविंदु, वास्तविक मोडल ID, प्रश्न, र उत्पन्न प्रतिक्रिया मुद्रण गर्छ, त्यसपछि यसको Spring
सन्दर्भ र HTTP क्लाइन्ट बन्द गर्छ। असफल अनुमान वा प्रतिक्रिया पाठ नहुँदा
सफल ढाँचाको सट्टा असफलता निकास उत्पादन हुन्छ।

### कन्फिगरेसन

| वातावरण चर | पूर्वनिर्धारित | उद्देश्य |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | लूपब्याक HTTP अन्तविंदु, `/v1` सहित |
| `FOUNDRY_LOCAL_MODEL` | खाली | ठ्याक्कै मोडल ID; अन्यथा एकमात्र विज्ञापन गरिएको मोडल चयन गर्दै |
| `FOUNDRY_LOCAL_PROMPT` | स्थानीय मोडलहरूको बारेमा एक वाक्यको प्रश्न | कन्सोल रनरले पठाएको प्रॉम्प्ट |

बराबर Spring तर्कहरू हुन् `--foundry.local.base-url=...`,
`--foundry.local.model=...`, र `--foundry.local.prompt=...`।
केवल लूपब्याक HTTP अन्तविंदुहरू स्वीकार्य छन्। रिमोट/क्लाउड अन्तविंदुहरू, एम्बेडेड
प्रमाणपत्रहरू, क्वेरी स्ट्रिङहरू, र `/v1` बिना पथहरू अस्वीकृत छन्।

खाली मोडल सेटिङ भनेको त्यति बेला मात्र काम गर्छ जब `/v1/models` ले ठीक एक मोडल विज्ञापन गर्छ।
विज्ञापन गरिएको मोडल आवश्यक नभएको हुन सक्छ। यदि धेरै मोडलहरू विज्ञापन छन्,
ठ्याक्कै लोड गरिएको ID सेट गर्नुहोस्, क्याटालग क्रममा भर पर्नु हुँदैन।

अनुरोधहरूले `temperature=0`, 150-टोकन आउटपुट सीमा, 120-दोस्रो टाइमआउट, र
स्वतः पुनः प्रयास नगर्ने। `max_tokens` अनुरोध क्षेत्र जानाजानी प्रयोग गरिएको हो:
Foundry Local REST सम्झौताले समर्थन गरेको छ, यद्यपि OpenAI Java ले पुरानो बनाएको छ
त्यो क्षेत्र नयाँ क्लाउड मोडेलहरूका लागि हो। मोडेल पहिचान कन्फिगरेसन वा
पत्ता लगाइबाट आउँछ, मोडेलका आफ्नै दाबीहरूबाट होइन।

## प्रत्यक्ष प्रमाणीकरण

स्थानीय सर्भर चलिरहेको अवस्थामा, अप्ट-इन प्रत्यक्ष परीक्षण सहित सबै परीक्षणहरू चलाउनुहोस्।
अन्त्य बिन्दुको पोर्टलाई तपाईंको सर्भरले मुद्रित गरेको मानले प्रतिस्थापन गर्नुहोस्। PowerShell मा डटेड
Maven गुणहरू उद्धरण गर्नुहोस्:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

प्रत्यक्ष परीक्षणले `Application.main`लाई कल गर्छ, तथ्य "फ्रान्सको राजधानी पेरिस हो," प्रदान गर्छ,
शहरको माग गर्छ, र वास्तविक उत्पन्न पाठ `पेरिस` हो भनी सुनिश्चित गर्छ। यसले सफल HTTP स्थिति मात्र होइन,
अर्थपूर्ण परिणाम जाँच गर्दछ।

यो एक एकीकरण जाँच हो, सटीकता मापदण्ड होइन। प्रमाणीकरणको क्रममा, यो
0.5B मोडेलले अलग "2 + 2" प्रॉम्प्टमा Java र सिधा REST दुबै मार्फत `3` जवाफ दियो।
स्वतन्त्र प्रमाणीकरण बिना गणित वा तथ्यात्मक सटीकतामा यसमा भरोसा नगर्नुहोस्; गणनाका लागि निर्धारक उपकरणहरू प्रयोग गर्नुहोस्।


## समस्या समाधान

| लक्षण | जांच गर्नुहोस् |
| --- | --- |
| कनेक्शन अस्वीकृत | तयार सन्देशको प्रतीक्षा गर्नुहोस्; मुद्रित पोर्ट र `/v1` मार्ग प्रयोग गर्नुहोस्। |
| धेरै मोडेलहरू विज्ञापन गरिएको | लोड गरिएको मोडेलको सही ID सँग `FOUNDRY_LOCAL_MODEL` सेट गर्नुहोस्। |
| मोडेल हरायो | `--list` प्रयोग गर्नुहोस्, वा स्पष्ट रूपमा `--download` सँग डाउनलोड अनुमति दिनुहोस्। |
| GPU प्रदायक असफल वा रोकिएको | सानो CPU मोडेल प्रयोग गर्नुहोस्। क्याच गरिएको GPU मोडेललाई यसको प्रदायक अझै आवश्यक पर्छ। |
| CLI `initializing` मा टित्रिरहन्छ | `foundry server logs --lines 80` पढ्नुहोस्; डिमन रोक्नुहोस् र SDK सहायक प्रयोग गर्नुहोस्। |
| NuGet TLS/डाउनलोड विफल | नेटवर्क पहुँच सुधार्नुहोस् वा माथि प्रमाणित Windows x64 फॉलबैक प्रयोग गर्नुहोस्। TLS अक्षम नगर्नुहोस्। |
| पोर्ट कब्जा गरिएको | `--port 0` प्रयोग गर्नुहोस् र मुद्रित अन्त्य बिन्दु सँग Java कन्फिगर गर्नुहोस्। |
| विकल्पहरू छैनन् वा खाली पाठ | एप जानाजानी असफल हुन्छ; मोडेल र रनटाइम लगहरू जाँच गर्नुहोस्। |

## स्रोत र सन्दर्भहरू

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): एक पटकको Spring Boot रनर।
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): टाइप गरिएको पत्ता लगाउने र स्थानीय च्याट पूर्णताहरू।
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP सम्झौता, रनर, र प्रत्यक्ष परीक्षणहरू।
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): आधिकारिक SDK REST सर्भर क्याच गरिएको मोडेल चयन र सफाईसहित।
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): प्रमाणित Windows x64 नेटिभ-रनटाइम फॉलब्याक।
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), र [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): कन्फिगरेसन र निर्भरताहरू।
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks)।
- [Foundry Local 2.0.1 release and migration notes](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1)।
- [Chapter 04: Practical samples](../README.md)।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
यो दस्तावेज़ AI अनुवाद सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) प्रयोग गरेर अनुवाद गरिएको हो। हामी सही हुन प्रयास गर्छौं, तर कृपया जानकार हुनुस् कि स्वचालित अनुवादमा त्रुटिहरू वा अशुद्धताहरू हुन सक्छन्। मूल दस्तावेज़ यसको मूल भाषामा आधिकारिक स्रोत मानिनुपर्छ। महत्वपूर्ण जानकारीका लागि व्यावसायिक मानव अनुवाद सिफारिस गरिन्छ। यस अनुवादको प्रयोगबाट उत्पन्न कुनै पनि गलत बुझाइ वा त्रुटिको लागि हामी जिम्मेवार छैनौं।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->