# Foundry लोकल स्प्रिंग बूट ट्युटोरियल

तुमच्या स्वतःच्या मशीनवर एक लहान भाषा मॉडेल चालवा आणि त्याचा OpenAI-सुसंगत
REST एंडपॉइंट एक Java कन्सोल अनुप्रयोगातून कॉल करा. कोणतीही Azure डिप्लॉयमेंट, Azure साइन-इन,
क्लाउड API की, किंवा क्लाउड इन्फरेंस वापरली जात नाही. **GPT-5.6 Luna फक्त Azure साठी आहे; ते
Foundry लोकल मॉडेल म्हणून कॉन्फिगर करू नका.**

## आवृत्त्या आणि पूर्वतयारी

| घटक | आवृत्ती |
| --- | --- |
| Java | 21 किंवा नंतरची |
| Maven | 3.6.3 किंवा नंतरची |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry लोकल SDK (लोकल REST सर्व्हर) | 2.0.1 |
| Node.js (लोकल REST सर्व्हर) | 20 किंवा नंतरची |
| Foundry लोकल CLI (ऐच्छिक, स्वतंत्र रिलीज) | 0.10.3 प्रीव्ह्यू |

Spring Boot Spring Framework, Jackson, JUnit, आणि Maven प्लगइन आवृत्त्यांचे व्यवस्थापन करते.
हे उदाहरण थेट OpenAI Java SDK वापरते, Spring AI नाही. जुनी वापरात नसलेली
Spring AI माइलस्टोन प्रॉपर्टी आणि रेपॉजिटरी काढून टाकण्यात आली आहे.

शिफारस केलेले स्टार्टर मॉडेल आहे **Qwen 2.5 0.5B**, CPU प्रकार
`qwen2.5-0.5b-instruct-generic-cpu:4` (कॅटलॉगमध्ये सुमारे 822 MB).
हे GPU अंमलबजावणी प्रदात्यांची गरज टाळते. इतर समर्थित, कॅश केलेली लहान मॉडेल्स
स्पष्टपणे निवडली जाऊ शकतात. मॉडेल आणि रनटाइम इंस्टॉलेशनसाठी नेटवर्क प्रवेश आवश्यक आहे;
प्रॉम्प्ट्स आणि इन्फरन्स लोकल राहतात. Foundry लोकल कधीकधी आवश्यक नसलेली टेलिमेट्री निष्क्रिय असतानाही कमी प्रमाणात रनटाइम
डायग्नोस्टिक्स दर्शवू शकते.

या नमुना डिरेक्टरीतून खालील आदेश चालवा.

## Java तयार करा आणि चाचणी करा

```powershell
mvn clean verify
```

HTTP कांट्रॅक्ट चाचण्या एक अस्थायी लूपबॅक सर्व्हर सुरू करतात आणि प्रत्यक्ष
OpenAI Java SDK वापरतात. त्या विनंती सिरीयलायझेशन, मॉडेल शोध, स्पष्ट मॉडेल निवड,
अस्पष्ट किंवा चुकीच्या मॉडेल सूची, HTTP अयशस्वी, रिक्त प्रतिसाद,
लोकल-केवळ URLs आणि कमांड-लाइन अयशस्वी प्रसार यांचा समावेश करतात. त्यांना कोणत्याही मॉडेल किंवा
Maven अवलंबित्व इंस्टॉलेशन व्यतिरिक्त नेटवर्क प्रवेशाची गरज नाही. लाईव्ह चाचणी ऐच्छिक आहे.

## लोकल मॉडेल सुरू करा

### शिफारस: पिन केलेला SDK सर्व्हर

Foundry लोकल Java SDK मूळतः उपलब्ध नाही. लहान Node.js सहाय्यक अधिकृत
SDK चा REST सर्व्हर होस्ट करतो; अनुप्रयोग आणि चॅट विनंती Java मध्ये राहतात.

पिन केलेल्या रनटाइम अवलंबन इंस्टॉल करा:

```powershell
npm ci
```

जर Windows x64 SDK च्या मूळ इंस्टॉलेशनदरम्यान NuGet ला पोहोचू शकत नसेल, तर पुरवलेला
फॉलबॅक वापरा. हे जुळणारा अधिकृत GitHub रनटाइम आर्काइव्ह डाउनलोड करते, प्रकाशनाचा
SHA-256 डाइजेस्ट तपासतो, आणि त्याचे DLLs मूळ अ‍ॅडऑनच्या जवळ ठेवतो. हे TLS
पडताळणी अक्षम करत नाही, प्राधान्य मागत नाही, किंवा SDK स्त्रोत बदलत नाही.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

या मशीनवर आधीच कॅश केलेले मॉडेल सूचीबद्ध करा:

```powershell
npm run start:foundry -- --list
```

पहिल्या वेळी, लहान CPU मॉडेल डाउनलोडला स्पष्टपणे परवानगी द्या:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

नंतरच्या वेळी, `--download` वगळा जेणेकरून कॅश केलेले मॉडेल आवश्यक राहील:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

सहाय्यक जुळणाऱ्या कॅश मॉडेलला प्राधान्य देतो, उपनाम किंवा अचूक प्रकार ID स्वीकारतो,
आणि अनधिकृत मॉडेल असताना `--download` नसल्यास नाकारतो. जेव्हा आवश्यक असेल तेव्हा फक्त
निवडलेल्या मॉडेलच्या अंमलबजावणी प्रदात्याची नोंदणी करतो. कॅश केलेल्या GPU प्रकारांना
अद्याप सुसंगत अंमलबजावणी-प्रहतनकर्ता पॅकेजेस आणि ड्रायव्हर्स लागतात.

जर पोर्ट 5273 वापरात असेल, तर उपलब्ध पोर्टसाठी `--port 0` द्या. सहाय्यक तयार असताना
`FOUNDRY_LOCAL_BASE_URL`, अचूक `FOUNDRY_LOCAL_MODEL` ID आणि त्याचा PID छापतो.
Java मध्ये छापलेला एंडपॉइंट वापरा. Java चालवताना हे टर्मिनल उघडे ठेवा;
**Ctrl+C** REST सर्व्हर थांबवते आणि मॉडेल मुक्त करते.

डीफॉल्ट कॅश आहे `~/.foundry/cache/models`. वेगळ्या विद्यमान कॅशसाठी `FOUNDRY_LOCAL_CACHE_DIR` सेट करा.
लॉग्स आणि सहाय्यक स्थिती या नमुन्याच्या `target/foundry-local` डिरेक्टरी अंतर्गत लिहिली जातात.
`mvn clean` चालवण्यापूर्वी सहाय्यक थांबवा.

### ऐच्छिक: Foundry लोकल CLI

CLI आणि SDK स्वतंत्र रिलीज आहेत: CLI **0.10.3** मध्ये SDK **1.2.4** समाविष्ट आहे;
वरचा सहाय्यक SDK **2.0.1** वापरतो. नवीनतम CLI इंस्टॉल केल्याने नवीनतम भाषा SDK इंस्टॉल होत नाही.
[CLI रिलीज नोट्स](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3) पहा.

Windows वर, CLI अनुपस्थित असल्यास प्रति-युजर इंस्टॉल आदेश वापरा:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

किंवा विद्यमान इंस्टॉलेशन अपग्रेड करा:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x जुने `foundry service` आदेश `foundry server` ने बदलतो:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` आधी डाउनलोड केलेले मॉडेल आवश्यक आहे. डाउनलोड आदेशांसाठी `foundry model --help` तपासा.
स्थिती आउटपुटमधील अचूक एंडपॉइंट वापरा; CLI अन्यथा
आपोआप नियुक्त केलेल्या पोर्टवर डीफॉल्ट करते. CLI आणि SDK सहाय्यक एकाच पोर्टवर सुरू करू नका.
समाप्ती नंतर:

```powershell
foundry server stop
```

## Java अनुप्रयोग चालवा

दुसऱ्या टर्मिनलमध्ये तुमच्या सर्व्हरने छापलेला एंडपॉइंट आणि अचूक मॉडेल ID सेट करा:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

किंवा संकुचित अनुप्रयोग चालवा:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

एकच Java एंट्रीपॉइंट आहे `com.example.Application`. हे निवडलेला
एंडपॉइंट, अचूक मॉडेल ID, प्रॉम्प्ट, आणि निर्मित प्रतिसाद छापते, नंतर त्याचा Spring
संदर्भ आणि HTTP क्लायंट बंद करते. अयशस्वी इन्फरन्स किंवा प्रतिसाद मजकूर नसेल तर
यशस्वी-आकाराचा प्लेसहोल्डरऐवजी अयशस्वी एक्झिट तयार करते.

### कॉन्फिगरेशन

| पर्यावरण बदलयोग्य | डीफॉल्ट | हेतू |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | लूपबॅक HTTP एंडपॉइंट, `/v1` सह |
| `FOUNDRY_LOCAL_MODEL` | रिक्त | अचूक मॉडेल ID; अन्यथा जाहिरात केलेले एकच मॉडेल निवडा |
| `FOUNDRY_LOCAL_PROMPT` | लोकल मॉडेल्स बद्दल एक वाक्याचा प्रश्न | कन्सोल रनरद्वारे पाठवलेला प्रॉम्प्ट |

समतुल्य Spring आर्ग्युमेंट्स आहेत `--foundry.local.base-url=...`,
`--foundry.local.model=...`, आणि `--foundry.local.prompt=...`.
फक्त लूपबॅक HTTP एंडपॉइंट्स मान्य आहेत. रिमोट/क्लाउड एंडपॉइंट्स, एंबेड केलेली
क्रेडेंशियल्स, क्वेरी स्ट्रिंग्ज, आणि `/v1` वगळता पाथ नाकारले जातात.

रिक्त मॉडेल सेटिंग केवळ तेव्हाच कार्य करते जेव्हा `/v1/models` ने नक्की एकच मॉडेल जाहिरात केले आहे.
जाहिरात केलेले मॉडेल आवश्यकतः लोड केलेले नसते. जर एकाधिक मॉडेल जाहिरात असतील,
कॅटलॉग क्रमवारीवर अवलंबून न राहता अचूक लोड केलेला ID सेट करा.

विनंत्या `temperature=0`, 150-टोकन आउटपुट मर्यादा, 120 सेकंद टाइमआउट, आणि
कोणतीही स्वयंचलित पुनःप्रयत्न नव्हती वापरतात. `max_tokens` विनंती फील्ड उद्देशपूर्ण आहे:
Foundry लोकल REST कांट्रॅक्टने ते समर्थन केले आहे, जरी OpenAI Java
नवीन क्लाउड मॉडेलसाठी त्या फील्डला निषिद्ध करते. मॉडेल ओळख कॉन्फिगरेशन किंवा
शोधामधून येते, मॉडेलच्या स्वतःच्या दावा पासून नाही.

## थेट पडताळणी

लोकल सर्व्हर चालू असताना, opt-in लाईव्ह चाचणीसह सर्व चाचण्या चालवा.
तुमच्या सर्व्हरने छापलेली पोर्टची किंमत एंडपॉइंटमध्ये बदला. PowerShell मध्ये डॉट असलेली
Maven प्रॉपर्टीज कोट करा:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

लाईव्ह चाचणी `Application.main` कॉल करते, "फ्रान्सचे राजधानी पॅरिस आहे"
हे तथ्य देते, शहर विचारते, आणि निर्मित मजकूर खरंच `Paris` आहे का ते तपासते.
ही फक्त यशस्वी HTTP स्थिती नव्हे तर सैद्धांतिक निकाल तपासते.

हे एक एकात्मिक तपासणी आहे, बरोळ मोजमाप नाही. पडताळणी दरम्यान, या
0.5B मॉडेलने स्वतंत्र "2 + 2" प्रॉम्प्टवर Java आणि थेट REST द्वारे दोन्ही ठिकाणी `3` उत्तर दिले.
त्यावर अंकगणित किंवा तथ्यात्मक अचूकतेसाठी स्वतंत्र
पुष्टीकरणाशिवाय अवलंबू नका; गणनेसाठी निश्चित टूल्स वापरा.

## समस्यांचे निराकरण

| लक्षण | तपासा |
| --- | --- |
| कनेक्शन नाकारले | तयार असल्याचा संदेश येईपर्यंत वाट पाहा; छापलेला पोर्ट आणि `/v1` पाथ वापरा. |
| अनेक मॉडेल जाहिरात केलेले | लोड केलेल्या मॉडेलचा अचूक ID `FOUNDRY_LOCAL_MODEL` सेट करा. |
| मॉडेल गहाळ | `--list` वापरा किंवा स्पष्टपणे डाउनलोडसाठी `--download` द्या. |
| GPU प्रदाता अयशस्वी किंवा अडथळा येतो | लहान CPU मॉडेल वापरा. कॅश केलेल्या GPU मॉडेलसाठी त्याचा प्रदाता आवश्यक आहे. |
| CLI अजूनही `initializing` आहे | `foundry server logs --lines 80` वाचा; डेमॉन थांबवा आणि SDK सहाय्यक वापरा. |
| NuGet TLS/डाउनलोड अयशस्वी | नेटवर्क प्रवेश दुरुस्त करा किंवा समोर दिलेला निश्चित Windows x64 फॉलबॅक वापरा. TLS अक्षम करू नका. |
| पोर्ट वापरात आहे | `--port 0` वापरा आणि छापलेला एंडपॉइंट Java मध्ये कॉन्फिगर करा. |
| कोणतेही पर्याय नाहीत किंवा रिक्त मजकूर | अनुप्रयोग जाणूनबुजून अयशस्वी होतो; मॉडेल आणि रनटाइम लॉग तपासा. |

## स्रोत आणि संदर्भ

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): एक-शॉट स्प्रिंग बूट रनर.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): टाइप केलेले शोध आणि लोकल चॅट पूर्णता.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP कांट्रॅक्ट, रनर, आणि लाईव्ह चाचण्या.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): अधिकृत SDK REST सर्व्हर कॅश केलेल्या मॉडेल निवड आणि क्लीनअपसह.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): खात्रीशीर Windows x64 मूळ-रनटाइम फॉलबॅक.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml), आणि [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): कॉन्फिगरेशन आणि अवलंबित्वे.
- [Foundry लोकल REST एकत्रीकरण](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry लोकल 2.0.1 रिलीज आणि स्थलांतर नोट्स](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [अध्याय 04: व्यावहारिक नमुने](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**अस्वीकरण**:
हा दस्तऐवज AI भाषांतर सेवा [Co-op Translator](https://github.com/Azure/co-op-translator) चा वापर करून अनुवादित केला आहे. जरी आम्ही अचूकतेसाठी प्रयत्न करतो, तरी कृपया लक्षात घ्या की स्वयंचलित भाषांतरांमध्ये त्रुटी किंवा अचूकतेची कमतरता असू शकते. मूळ दस्तऐवज त्याच्या मूळ भाषेत अधिकृत स्रोत मानला पाहिजे. महत्त्वाची माहिती असल्यास, व्यावसायिक मानवी भाषांतराची शिफारस केली जाते. या भाषांतराच्या वापरामुळे उद्भवणाऱ्या कोणत्याही गैरसमज किंवा चुकीच्या अर्थलावणीसाठी आम्ही जबाबदार नाही.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->