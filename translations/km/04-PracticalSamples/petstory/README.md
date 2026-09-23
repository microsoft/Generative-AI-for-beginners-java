# មេរៀនបង្កើតរឿងសត្វចិញ្ចឹមសម្រាប់អ្នកចាប់ផ្ដើម

ផ្ទុករូបភាពសត្វចិញ្ចឹមឡើងវិញ វិភាគវាជាមួយ GPT-5.6 Luna ហើយបង្កើតរឿងពីការពិពណ៌នាដែលបានទទួល។ ការស្នើសុំម៉ូដែលទាំងពីរប្រើ `reasoning_effort: none`។

| ផ្នែក | កំណែ |
| --- | --- |
| Java | 21 ឬ ខ្ពស់ជាងនេះ |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## តារាងមាតិកា

- [តម្រូវការមុន](#តម្រូវការមុន)
- [ការយល់ដឹងអំពីរចនាសម្ព័ន្ធគម្រោង](#ការយល់ដឹងអំពីរចនាសម្ព័ន្ធគម្រោង)
- [ការបង្ហាញផ្នែកស្នូល](#ការបង្ហាញផ្នែកស្នូល)
  - [1. កម្មវិធីសំខាន់](#1-កម្មវិធីសំខាន់)
  - [2. គ្រប់គ្រងវែប](#2-គ្រប់គ្រងវែប)
  - [3. សេវារឿង](#3-សេវារឿង)
  - [4. គំរូវែប](#4-គំរូវែប)
  - [5. ការកំណត់រចនា](#5-ការកំណត់រចនា)
- [ការរត់កម្មវិធី](#ការរត់កម្មវិធី)
- [ការធ្វើតេស្តអនឡាញនៅក្រៅបណ្តាញ](#ការធ្វើតេស្តអនឡាញនៅក្រៅបណ្តាញ)
- [របៀបដែលវាធ្វើការរួមគ្នា](#របៀបដែលវាដំណើរការរួមគ្នាទាំងមូល)
- [ការយល់ដឹងអំពីការបញ្ចូល AI](#ការយល់ដឹងអំពីការបញ្ចូល-ai)
- [ជំហានបន្ទាប់](#ជំហានបន្ទាប់)

## តម្រូវការមុន

មុនចាប់ផ្ដើម សូមប្រាកដថាអ្នកមាន:
- Java 21 ឬ ខ្ពស់ជាងនេះបានដំឡើង
- Maven សម្រាប់ការគ្រប់គ្រងភាពត្រូវការ
- ការតំឡើង Azure AI Foundry មួយសម្រាប់ GPT-5.6 Luna ឈ្មោះ `gpt-5.6-luna` ឬ ការកំណត់បម្លែង `AZURE_OPENAI_DEPLOYMENT` ទៅកាន់ការតំឡើងនោះ។ មើល [ជំពូកទី 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) សម្រាប់ការរៀបចំនិងចុះឈ្មោះជាមួយ `az login` សម្រាប់ការផ្ទៀងផ្ទាត់ឥតគន្លងក្តៅ។ ការតំឡើងត្រូវគាំទ្រការបញ្ចូលរូបភាព និង `reasoning_effort: none`។
- មានការយល់ដឹងមូលដ្ធានអំពី Java, Spring Boot និងការអភិវឌ្ឍវែប

## ការយល់ដឹងអំពីរចនាសម្ព័ន្ធគម្រោង

គម្រោងរឿងសត្វមានឯកសារសំខាន់ជាច្រើន:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## ការបង្ហាញផ្នែកស្នូល

### 1. កម្មវិធីសំខាន់

**ឯកសារ:** `PetStoryApplication.java`

នេះគឺជាចំណុចចូលសម្រាប់កម្មវិធី Spring Boot របស់យើង៖

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**អ្វីដែលវាធ្វើ:**
- រមួង `@SpringBootApplication` អនុញ្ញាតឲ្យបើកកំណត់រចនា�Automatic និងស្វែងរកផ្នែកផ្សេងៗ 
- បើកម៉ាស៊ីនមេវែបចូលរួម (Tomcat) នៅលើច្រក 8080
- បង្កើត Spring Beans និងសេវាកម្មទាំងអស់ដោយស្វ័យប្រវត្តិ

### 2. គ្រប់គ្រងវែប

**ឯកសារ:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| ផ្លូវចូល | ពាក្យស្នើ | ពីលទ្ធផលជោគជ័យ |
| --- | --- | --- |
| `GET /` | គ្មានខ្លឹមសារ | បែបបទ HTML សម្រាប់ផ្ទុកឡើងជាមួយសញ្ញា CSRF |
| `POST /analyze-image` | `multipart/form-data`, វាលឯកសារ `image` | JSON: `{"description":"សត្វលេងល្បិច..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, វាល `description` | ទំព័រលទ្ធផល HTML ជាមួយការពិពណ៌នានិងរឿងដែលបានបង្កើត |

ការចូល POST ទាំងពីរត្រូវការគូគ៊ីសម្រាប់សម័យនិងសញ្ញា CSRF ដែលបានទទួលពី `GET /`។ ស្គ្រីបផ្ទុកឡើងផ្ញើតម្លៃលាក់ `_csrf` នៅក្នុងក្បាល `X-CSRF-TOKEN`; ការដាក់ស្នើរឿងផ្ញើវាជាវាល `_csrf` របស់បែបបទ។ អតិថិជន API ត្រូវរក្សាគូគ៊ីរវាងសំណើ។ ទាំងនេះជាប៉ុមបែបបទ មិនមែនជាសំណើ JSON ទេ។

ការពិពណ៌នាត្រូវមិនទទេហើយមិនលើស 1000 តួអក្សរ។ គ្រប់គ្រងកាត់បន្ថយការពិពណ៌នានិងដក `<`, `>`, សញ្ញាពីរ, សញ្ញាអប៉ូស្ទ្រូភេ និង `&` មុនផ្ញើទៅសេវា។ គំរូលទ្ធផលក៏បំលែងលទ្ធផលម៉ូដែលជាអត្ថបទសម្រាប់ `th:text` ដែរ។

ការពិនិត្យរូបភាពដែលទទួលបរាជ័យត្រឡប់ HTTP 400 ជាមួយវាល `error`; ការបរាជ័យម៉ូដែលត្រឡប់ HTTP 502 ជាមួយវាល `error` ហើយគ្មាន `description`។ ការពិពណ៌នារឿងមិនត្រឹមត្រូវ ឬ បរាជ័យម៉ូដែល បញ្ជូនបញ្ជាក់ទៅ `/` ជាមួយកំហុសដែលអាចមើលឃើញបាន។ វាលដែលត្រូវការខកខានត្រឡប់ HTTP 400 ហើយសញ្ញា CSRF ខកខាន ឬ មិនត្រឹមត្រូវត្រឡប់ HTTP 403។ មិនមានការបង្ហាញការពិពណ៌នាដោយជំនួស ឬ រឿងជោគជ័យពី AI ឡើយ។

### 3. សេវារឿង

**ឯកសារ:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

OpenAI Java SDK ផ្លូវការកំណែ 4.63.1 ហៅ API Chat Completions ត្រូវនឹង OpenAI របស់ Azure AI Foundry។ Azure Identity 1.18.6 ផ្ដល់សញ្ញាប័ណ្ណ Microsoft Entra តាម `DefaultAzureCredential`; មិនចាំបាច់មានកូនសោ API ទេ។

| ប្រតិបត្ដិការ | បញ្ចូល | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | បៃតទ្រង់ទ្រាយរូបភាព编码ជាទៅbase64 ជាមួយប្រភេទ MIME ដែលផ្ទុកឡើង | 300 |
| `generateStory` | ការពិពណ៌នាសត្វនៅក្នុងសារអ្នកប្រើ | 800 |

ការស្នើសុំទាំងពីរប្រើការតំឡើងដែលបានកំណត់លំនាំដើមជា `gpt-5.6-luna` ហើយដាក់សញ្ញា `ReasoningEffort.NONE` (`reasoning_effort: none`) បញ្ជាក់។ គ្មានស្នើសុំពី `temperature` ឬប៉ារ៉ាម៉ែត្រតែមួយ `max_tokens` នៃមួយម៉ូដែលចាស់។

ការវិភាគរូបភាពទទួលបាន JPEG, PNG, GIF និង WebP បដិសេធរូបភាពទទេ និងឯកសារលើស 10MB ហើយកំណត់ការពិពណ៌នារួចទទួល 1000 តួអក្សរ។ ការស្នើសុំរឿងសុំរឿងខ្លីសម្រួលមកអ្នកគ្រួសារ។ ការជ្រើសរើសទទេ ឬ មាតិកាម៉ូដែលទទេគឺជាកំហុស ហើយបរាជ័យរក្សាដើមកំណាលជាការសម្គាល់មើលក្នុងម៉ាស៊ីនមេ។ អតិថិជន SDK ត្រូវបានបិទនៅពេលកម្មវិធីបិទ។

### 4. គំរូវែប

**ឯកសារ:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (បែបបទផ្ទុកឡើង)

ទំព័រចាប់ផ្ដើមជាមួយកម្មវិធីជ្រើសរូបភាព មិនមែនតំបន់អត្ថបទពិពណ៌នា។ **វិភាគរូបភាព** បង្ហាញរូបដែលបានជ្រើសរើស និងបញ្ចូនវាទៅ `/analyze-image`។ លទ្ធផលជោគជ័យបង្ហាញការពិពណ៌នា បំពេញវាលពាក្យដាក់ខ្មោច `description` ហើយបង្ហាញប៊ូតុង **បង្កើតរឿង**។ ប៊ូតុងនោះបញ្ចូនបែបបទទៅ `/generate-story`។

គ្មានការទាញយកម៉ូដែលពីកម្មវិធីរុករក ឬការពឹងផ្អែកទៅលើ CDN។ ការវិភាគរូបភាពប្រតិបត្តិលើម៉ាស៊ីនមេតាមការតំឡើង Azure ដែលបានកំណត់។ ករណីបរាជ័យនៅតែបង្ហាញ ហើយមិនអនុញ្ញាតឲ្យបង្កើតរឿងជាមួយការពិពណ៌នាបង្កើតឡើង។ ជ្រើសឯកសារផ្សេងទៀតនឹងលុបការវិភាគមុន។

**ឯកសារ:** `result.html` (បង្ហាញរឿង)

បង្ហាញរឿងដែលបានបង្កើត៖

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**លក្ខណៈពិសេសនៃគំរូ៖**

1. **ការបញ្ចូល Thymeleaf**: ប្រើ `th:` attributes សម្រាប់មាតិកាដោយឌីណាមិច
2. **រចនាបទឆ្លាតវៃ**: ស្ទីល CSS សម្រាប់ទូរស័ព្ទ និងកុំព្យូទ័រប្រភេទដេសក្តុប
3. **ការគ្រប់គ្រងកំហុស**: បង្ហាញកំហុសផ្ទៀងផ្ទាត់ទៅអ្នកប្រើ
4. **ការគ្រប់គ្រងការផ្ទុក**: JavaScript បង្ហាញរូបកាន់តែច្បាស់ ផ្ញើសំណើ multipart ដែលបានការពារដោយ CSRF ហើយបង្ហាញពាក្យពិពណ៌នាដែលបានបញ្ជូនវិញ

### 5. ការកំណត់រចនា

**ឯកសារ:** `application.properties`

ការកំណត់សម្រាប់កម្មវិធី៖

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**ការបង្ហាញការកំណត់៖**

1. **ការផ្ទុកឡើងឯកសារ**: ទាំងឯកសារនិងសំណើ multipart ទាំងមូលមានកំណត់នៅ 10MB; សូមរក្សារូបថតខ្លះក្រោមដែនកំណត់ដើម្បីទុកកន្លែងសម្រាប់ក្បាល multipart
2. **កំណត់ហេតុ**: គ្រប់គ្រងព័ត៌មានដែលបញ្ចូលក្នុងកំណត់ហេតុពេលដំណើរការ
3. **Azure AI Foundry**: បញ្ជាក់ចំណុចចូលនិងការតំឡើងម៉ូដែលដែលត្រូវប្រើ (ការផ្ទៀងផ្ទាត់គ្មានកូនសោ)
4. **សំណុំសុវត្ថិភាព**: ការការពារពី CSRF នៅតែបើក។ ការរីកចម្រើនម៉ូដែលត្រូវបានកំណត់ក្នុងម៉ាស៊ីនមេ ខណៈពេលគ្រប់គ្រងបង្ហាញសារបង្ហាញកំហុសម៉ូដែលទូទៅ។

## ការរត់កម្មវិធី

### ជំហាន 1៖ ចូលប្រើ និងកំណត់ចំណុចចូលរបស់អ្នក

ការផ្ទៀងផ្ទាត់គ្មានកូនសោ (Microsoft Entra ID) ដូច្នេះគ្មានកូនសោ API។ ចូលប្រើហើយកំណត់ចំណុចចូល Foundry របស់អ្នក៖

**Windows (Command Prompt):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**ហេតុអ្វីបានជា ត្រូវការនេះ:**
- Azure AI Foundry ប្រើ Microsoft Entra ID ដើម្បីផ្ទៀងផ្ទាត់សំណើ inference
- ការផ្ទៀងផ្ទាត់គ្មានកូនសោ គឺគ្មានល្បឿនសំងាត់នៅក្នុងកូដចូល ឬបរិយាកាស
- គណនីរបស់អ្នកត្រូវការតួនាទី **Cognitive Services OpenAI User** លើធនធាន

ឈ្មោះការតំឡើងលំនាំដើមគឺ `gpt-5.6-luna`។ ប្រសិនបើការតំឡើង GPT-5.6 Luna របស់អ្នកមានឈ្មោះផ្សេង សូមកំណត់ `AZURE_OPENAI_DEPLOYMENT` នៅក្នុងទំព័របញ្ជាលើសម្ងាត់មុនចាប់ផ្ដើមកម្មវិធី។ ការវិភាគរូបភាព និងការបង្កើតរឿងទាំងអស់ប្រើការកំណត់នេះ។

### ជំហាន 2៖ បង្កើត និង រត់

ទៅកាន់ថតគម្រោង៖
```bash
cd 04-PracticalSamples/petstory
```

បង្កើត JAR ប្រតិបត្ដិការផ្តាច់មុខ និងបញ្ចូលការធ្វើតេស្តអនឡាញទាំងអស់៖
```bash
mvn clean package
```

ចាប់ផ្ដើមម៉ាស៊ីនមេ៖
```bash
mvn spring-boot:run
```

កម្មវិធីនឹងចាប់ផ្ដើមនៅ `http://localhost:8080`។

ជំនួសដូច្នេះ ចាប់ផ្ដើម JAR កញ្ចប់នៅលើច្រកចេញមួយដោយឥតគិតថ្លៃ ឧទាហរណ៍:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

សម្រាប់ពាក្យបញ្ជានេះ បើក `http://localhost:8083/`។ ផ្លូវ `/analyze-image` និង `/generate-story` នៅតែអាចប្រើបាននៅលើច្រកដែលបានជ្រើសរើស។

### ជំហាន 3៖ សាកតេស្តកម្មវិធី

1. **បើក** `http://localhost:8080` នៅក្នុងកម្មវិធីរុករករបស់អ្នក
2. **ជ្រើសរើស** រូបភាពសត្វចំលែក JPEG, PNG, GIF, ឬ WebP ខាងក្រោម 10MB
3. **ចុច** "Analyze Image" ហើយរង់ចាំការពិពណ៌នាសត្វ
4. **ចុច** "Generate Story" បន្ទាប់ពីវិភាគជោគជ័យ
5. **មើល** រឿង និងប្រើតំណភ្ជាប់នៅលើទំព័រលទ្ធផលដើម្បីត្រឡប់ទៅបែបបទផ្ទុកឡើង

ជម្រើសសុទ្ធរបស់រូបភាពទៅរឿងធ្វើការហៅម៉ូដែលពីរដងមួយសម្រាប់ប៊ូតុងមួយ។ ការប្រើប្រាស់ច្នៃប្រឌិតក្នុងពេលពិតបរិមាណលើ quota ការតំឡើងនិងអាចបង្ករឥណទាន។ សូមរត់ការធ្វើតេស្តម្ហូបពេលតែម្ដងនៅពេលចែករំលែកការតំឡើងមានកំណត់អត្រា។ ការផ្ទុកទំព័រដើមមិនហៅម៉ូដែលទេ។

## ការធ្វើតេស្តអនឡាញនៅក្រៅបណ្តាញ

ពីថតផ្ទាល់ខ្លួន សូមរត់៖

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) ចាប់យកសំណើ SDK OpenAI ពិតប្រាកដជាមួយកំណត់ HTTP loopback fixture។ វាត្រួតពិនិត្យទាំងពីរសំណើការតំឡើង, `reasoning_effort: none`, កំណត់សញ្ញាតួអក្សរ, បន្ទុករូបភាព, ការត្រួតពិនិត្យបញ្ចូល, ពីលទ្ធផលទទេ, និងកំហុស upstream។

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) ប្រើ MockMvc ជាមួយសេវារូបមន្តម៉ូដែលចម្លងសម្រាប់តេស្តទំព័រ Thymeleaf ដែលបានបង្ហាញ, កញ្ចប់ផ្ទុកឡើង, CSRF, ការត្រួតពិនិត្យ, ការការពារបញ្ចូល, និងករណីបរាជ័យដែលអាចមើលឃើញ។ ការធ្វើតេស្តទាំងនេះមិនចាំបាច់មានលិខិតបញ្ជាក់ Azure និងមិនហៅករណីសំរភារ Azure សម្រាប់ការប្រាក់។ Maven សរសេរបរិយាយ Surefire ក្រោម `target/surefire-reports`។

## របៀបដែលវាដំណើរការរួមគ្នាទាំងមូល

នេះជាដំណើរការពេញលេញនៅពេលអ្នកបង្កើតរឿងសត្វ៖

1. **ជ្រើសរូបភាព**: អ្នកជ្រើសរើសរូបភាពសត្វនៅក្នុងបែបបទផ្ទុកឡើង
2. **ផ្ទុកឡើងរូបភាព**: "Analyze Image" ផ្ញើសំណើ multipart POST ទៅ `/analyze-image` ជាមួយក្បាល CSRF
3. **វិភាគរូបភាព**: `StoryService` ផ្ញើរូបភាពទៅ GPT-5.6 Luna ជាមួយការកំណត់ reasoning ទៅ `none`
4. **បង្ហាញការពិពណ៌នា**: កម្មវិធីរុករកបង្ហាញការពិពណ៌នាដែលបានបញ្ជូនវិញនិងផ្ទុកវាទៅក្នុងបែបបទ
5. **ដាក់ស្នើរឿង**: "Generate Story" ផ្ញើ `description` និង `_csrf` ទៅ `/generate-story`
6. **បង្កើតរឿង**: គ្រប់គ្រងផ្ទៀងផ្ទាត់ការពិពណ៌នានិងហៅការតំឡើងដូចគ្នាជាមួយការកំណត់ reasoning ទៅ `none`
7. **បង្ហាញគំរូ**: Thymeleaf ការពារ និងបង្ហាញការពិពណ៌នានិងរឿងនៅលើទំព័រលទ្ធផល

**ដំណើរការគ្រប់គ្រងកំហុស:**
ប្រសិនបើម៉ូដែលបរាជ័យ ម៉ាស៊ីនមេកំណត់ហេតុបញ្ហាដើម។ ការវិភាគរូបភាពត្រឡប់ HTTP 502 ហើយកម្មវិធីរុករកបង្ហាញកំហុសដោយគ្មានបង្ហាញ "Generate Story"។ ការបង្កើតរឿងបញ្ជូនបញ្ជាក់ទៅបែបបទជាមួយសារ​កំហុស។ មិនមានផ្លូវខាងណាម្នាក់ជំនួសលទ្ធផលដែលបានសរសេរមុនឡើយ។

## ការយល់ដឹងអំពីការបញ្ចូល AI

### Azure AI Foundry (គ្មានកូនសោ)
សេវាអនុញ្ញាត​ការកំណត់ SDK ជាមួយចំណុចចូល `/openai/v1/` របស់ធនធានរបស់អ្នក។ `DefaultAzureCredential` និង `AuthenticationUtil.getBearerTokenSupplier` ផ្ដល់សញ្ញាប័ណ្ណ Microsoft Entra សម្រាប់ `https://ai.azure.com/.default`។ ការអភិវឌ្ឍក្នុងតំបន់អាចប្រើការចូល CLI Azure របស់អ្នក។ កម្មវិធីនៅលើ Azure អាចប្រើសម្គាល់គ្រប់គ្រងជាមួយសិទ្ធិធនធានត្រូវការ។

### ការបង្រៀនបញ្ចូល
ការវិភាគរូបភាពសុំបង្ហាញលក្ខណៈសត្វដែលអាចមើលឃើញក្នុងបទបញ្ចោតខ្លី ហើយប្រាប់ម៉ូដែលឲ្យដំណើរការបង្ហាញអត្ថបទក្នុងរូបភាពជាទិន្នន័យ មិនមែនជាសេចក្តីណែនាំ។ ការបង្កើតរឿងប្រើការពិពណ៌នាដែលបានបញ្ជូនវិញក្នុងសំណើសរសេរដែលសមរម្យសម្រាប់គ្រួសារ។ ការហៅទាំងពីរមិនបានអនុញ្ញាត reasoning ឬកំណត់ temperature។

### ការបន្តគ្រប់គ្រងចម្លើយ
អ្នកដំណើរការចម្លើយរួម មិនទទួលយកជម្រើសខ្វះ និងមាតិកាទទេ ឬមានតែទាំងហ្វ្រេសបWhite space ប៉ុណ្ណោះ។ កាត់បន្ថយមាតិកាត្រឹមត្រូវ និងរក្សាកំហុស upstream។ ការពិពណ៌នារូបភាពត្រូវបានកំណត់នៅ 1000 តួអក្សរ ដើម្បីឲ្យសមរម្យសម្រាប់បែបបទរឿងក្រោយ។ កំហុសម៉ូដែលដើមត្រូវរក្សាទុកសម្រាប់វាយតម្លៃ ប៉ុន្តែមិនត្រូវបង្ហាញអ្នកប្រើទេ។

## ជំហានបន្ទាប់

សម្រាប់ឧទាហរណ៍បន្ថែម សូមមើល [ជំពូក 04៖ ឧទាហរណ៍អនុវត្ត](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**ការបដិសេធ**:
ឯកសារនេះត្រូវបានបម្លែងភាសា ដោយប្រើសេវាបម្លែងភាសា AI [Co-op Translator](https://github.com/Azure/co-op-translator)។ ទោះយើងខ្ញុំមានក្តីប្រាថ្នាឱ្យបានច្បាស់លាស់ តែសូមយល់ដឹងថាការបម្លែងដោយស្វ័យប្រវត្តិក៏អាចមានកំហុសឬភាពមិនត្រឹមត្រូវ។ ឯកសារដើមជាភាសាទីតាំងគួរត្រូវបានគេប្រើជាប្រភពច្បាស់លាស់។ សម្រាប់ព័ត៌មានសំខាន់ៗ សូមណែនាំឱ្យប្រើប្រាស់ការប្រែដោយមនុស្សជំនាញ។ យើងខ្ញុំមិនទទួលខុសត្រូវចំពោះការយល់ច្រឡំ ឬការបកស្រាយខុសបន្ទាប់ពីការប្រើប្រាស់ការបម្លែងនេះនោះទេ។
<!-- CO-OP TRANSLATOR DISCLAIMER END -->