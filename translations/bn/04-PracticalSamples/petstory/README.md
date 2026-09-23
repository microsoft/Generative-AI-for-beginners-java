# নবীনদের জন্য পোষ্য গল্প সরকারী টিউটোরিয়াল

একটি পোষ্যাকৃতির ছবি আপলোড করুন, GPT-5.6 Luna দিয়ে বিশ্লেষণ করুন, এবং প্রাপ্ত বর্ণনা থেকে একটি গল্প তৈরি করুন। উভয় মডেল অনুরোধে `reasoning_effort: none` ব্যবহার করা হয়।

| কম্পোনেন্ট | সংস্করণ |
| --- | --- |
| Java | ২১ বা তার বেশি |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## বিষয়বস্তু সূচি

- [প্রয়োজনীয় শর্তাবলী](#প্রয়োজনীয়-শর্তাবলী)
- [প্রকল্পের কাঠামো বোঝা](#প্রকল্পের-কাঠামো-বোঝা)
- [কোর কম্পোনেন্ট ব্যাখ্যা](#কোর-কম্পোনেন্ট-ব্যাখ্যা)
  - [১. প্রধান অ্যাপ্লিকেশন](#১-প্রধান-অ্যাপ্লিকেশন)
  - [২. ওয়েব কন্ট্রোলার](#২-ওয়েব-কন্ট্রোলার)
  - [৩. গল্প সার্ভিস](#৩-গল্প-সার্ভিস)
  - [৪. ওয়েব টেমপ্লেটস](#৪-ওয়েব-টেমপ্লেটস)
  - [৫. কনফিগারেশন](#৫-কনফিগারেশন)
- [অ্যাপ্লিকেশন চালানো](#অ্যাপ্লিকেশন-চালানো)
- [অফলাইন পরীক্ষা](#অফলাইন-পরীক্ষা)
- [সবকিছু কিভাবে একসাথে কাজ করে](#সবকিছু-কিভাবে-একসাথে-কাজ-করে)
- [AI ইন্টিগ্রেশন বোঝা](#ai-ইন্টিগ্রেশন-বোঝা)
- [পরবর্তী ধাপগুলি](#পরবর্তী-ধাপ)

## প্রয়োজনীয় শর্তাবলী

শুরুর আগে নিশ্চিত করুন:
- Java ২১ বা তার বেশি ইনস্টল করা আছে
- ডিপেনডেন্সি ব্যবস্থাপনার জন্য Maven
- GPT-5.6 Luna নামক একটি Azure AI Foundry ডিপ্লয়মেন্ট, অথবা সেটি নির্দেশকারী `AZURE_OPENAI_DEPLOYMENT` ওভাররাইড। প্রোভিশনিং এর জন্য [অধ্যায় ২](../../02-SetupDevEnvironment/getting-started-azure-openai.md) দেখুন এবং কীবিহীন প্রমাণীকরণের জন্য `az login` দিয়ে সাইন ইন করুন। ডিপ্লয়মেন্ট ইমেজ ইনপুট এবং `reasoning_effort: none` সমর্থন করতে হবে।
- Java, Spring Boot, এবং ওয়েব ডেভেলপমেন্ট এর মৌলিক ধারণা

## প্রকল্পের কাঠামো বোঝা

পোষ্য গল্প প্রকল্পে কয়েকটি গুরুত্বপূর্ণ ফাইল রয়েছে:

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

## কোর কম্পোনেন্ট ব্যাখ্যা

### ১. প্রধান অ্যাপ্লিকেশন

**ফাইল:** `PetStoryApplication.java`

এটি আমাদের Spring Boot অ্যাপ্লিকেশনের প্রবেশদ্বার:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**এর কার্যক্রম:**
- `@SpringBootApplication` অ্যানোটেশন অটো-কনফিগারেশন এবং কম্পোনেন্ট স্ক্যানিং সক্ষম করে
- পোর্ট ৮০৮০ এ এম্বেডেড ওয়েব সার্ভার (Tomcat) চালু করে
- প্রয়োজনীয় সমস্ত Spring বিন এবং সার্ভিস স্বয়ংক্রিয় ভাবে তৈরি করে

### ২. ওয়েব কন্ট্রোলার

**ফাইল:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| এন্ডপয়েন্ট | অনুরোধ | সফল প্রতিক্রিয়া |
| --- | --- | --- |
| `GET /` | কোন বডি নয় | CSRF টোকেন সহ HTML আপলোড ফর্ম |
| `POST /analyze-image` | `multipart/form-data`, ফাইল ক্ষেত্র `image` | JSON: `{"description":"একটি খেলাধূলার পোষ্য..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, ক্ষেত্র `description` | বর্ণনা এবং তৈরি গল্পসহ HTML ফলাফল পৃষ্ঠা |

উভয় POST এন্ডপয়েন্ট সেশন কুকি এবং CSRF টোকেন প্রয়োজন যা `GET /` থেকে পাওয়া যায়। আপলোড স্ক্রিপ্ট লুকানো `_csrf` মান `X-CSRF-TOKEN` হেডারে প্রেরণ করে; গল্প জমা `_csrf` ফর্ম ক্ষেত্র হিসেবে পাঠায়। API ক্লায়েন্টকে অনুরোধের মধ্যে কুকি সংরক্ষণ করতে হবে। এগুলি ফর্ম এন্ডপয়েন্ট, JSON অনুরোধ নয়।

বর্ণনা অবশ্যই খালি নয় এবং ১০০০ অক্ষরের বেশি নয়। কন্ট্রোলার বর্ণনা ট্রিম করে এবং `<`, `>`, ডাবল কোটেশন, অ্যাপোস্ট্রফি এবং `&` অপসারণ করে সেবাতে পাঠায়। ফলাফল টেমপ্লেট মডেল আউটপুটকে `th:text` দিয়ে এস্কেপ করে।

ইমেজ যাচাই ব্যর্থ হলে HTTP 400 এবং `error` ফিল্ড ফেরত দেয়; মডেল ব্যর্থতা হলে HTTP 502 এবং `error` ফিল্ড ফেরত দেয় কিন্তু কোন `description` থাকে না। অবৈধ গল্প বর্ণনা বা মডেল ব্যর্থতা হলে `/` এ পুনঃনির্দেশ করে দৃশ্যমান ত্রুটি দেখায়। প্রয়োজনীয় ক্ষেত্র গায়েব হলে HTTP 400 এবং CSRF টোকেন অনুপস্থিত বা অবৈধ হলে HTTP 403 ফেরত দেয়। সফল AI ফলাফলে কোনো বিকল্প বর্ণনা বা গল্প দেয়া হয় না।

### ৩. গল্প সার্ভিস

**ফাইল:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

অফিসিয়াল OpenAI Java SDK 4.63.1 Azure AI Foundry এর OpenAI-সঙ্গত চ্যাট কমপ্লিশন API কল করে। Azure Identity 1.18.6 `DefaultAzureCredential` এর মাধ্যমে Microsoft Entra বেয়ার টোকেন সরবরাহ করে; কোনও API কী প্রয়োজন হয় না।

| অপারেশন | ইনপুট | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | আপলোড করা MIME টাইপ সহ বেস64 ডেটা URL এ এনকোড করা ইমেজ বাইট | ৩০০ |
| `generateStory` | একটি ব্যবহারকারীর মেসেজে পোষ্য বর্ণনা | ৮০০ |

উভয় অনুরোধ নির্ধারিত ডিপ্লয়মেন্ট ব্যবহার করে, ডিফল্ট `gpt-5.6-luna`, এবং স্পষ্টতই `ReasoningEffort.NONE` (`reasoning_effort: none`) সেট করে। উভয় অনুরোধে `temperature` বা পুরনো `max_tokens` প্যারামিটার পাঠানো হয় না।

ইমেজ বিশ্লেষণ JPEG, PNG, GIF, এবং WebP গ্রহণ করে, খালি ছবি বা ১০MB এর বেশি ফাইল প্রত্যাখ্যান করে, এবং প্রাপ্ত বর্ণনাকে ১০০০ অক্ষরে সীমাবদ্ধ করে। গল্পের প্রম্পট একটি পরিবারের উপযোগী সংক্ষিপ্ত গল্প চায়। খালি পছন্দ বা খালি মডেল কন্টেন্ট ত্রুটি এবং ব্যর্থতা মূল কারণ সংরক্ষণ করে সার্ভার-সাইড ডায়াগনস্টিকের জন্য। অ্যাপ্লিকেশন বন্ধ হলে SDK ক্লায়েন্ট বন্ধ হয়।

### ৪. ওয়েব টেমপ্লেটস

**ফাইল:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (আপলোড ফর্ম)

পৃষ্ঠা শুরু হয় একটি ছবি নির্বাচকের মাধ্যমে, বর্ণনা লেখার ক্ষেত্র নয়। **Analyze Image** নির্বাচন করা ছবি প্রিভিউ করে এবং এটি `/analyze-image` এ পোস্ট করে। সফল প্রতিক্রিয়া বর্ণনা প্রদর্শন করে, লুকানো `description` ক্ষেত্র পূরণ করে, এবং **Generate Story** দৃশ্যমান করে। ওই বোতাম বিদ্যমান ফর্মটি `/generate-story` এ পাঠায়।

ব্রাউজারে কোনো মডেল ডাউনলোড বা CDN নির্ভরশীলতা নেই। ইমেজ বিশ্লেষণ সার্ভারে Azure ডিপ্লয়মেন্টের মাধ্যমে চলে। ব্যর্থতা দৃশ্যমান থাকে এবং বিড়ম্বনাপূর্ণ বর্ণনা দিয়ে গল্প তৈরি করার অনুমতি দেয় না। অন্য ফাইল নির্বাচন আগের বিশ্লেষণ মুছে দেয়।

**ফাইল:** `result.html` (গল্প প্রদর্শন)

তৈরি করা গল্প দেখায়:

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

**টেমপ্লেট বৈশিষ্ট্য:**

১. **Thymeleaf ইন্টিগ্রেশন**: `th:` অ্যাট্রিবিউট ব্যবহার করে গতিশীল কন্টেন্ট
২. **রেসপন্সিভ ডিজাইন**: মোবাইল এবং ডেস্কটপ জন্য CSS স্টাইলিং
৩. **ত্রুটি পরিচালনা**: ব্যবহারকারীদের জন্য যাচাই ত্রুটি প্রদর্শন
৪. **আপলোড পরিচালনা**: JavaScript ছবি প্রিভিউ করে, CSRF সুরক্ষিত মাল্টিপার্ট অনুরোধ পাঠায়, এবং ফেরত বর্ণনা প্রদর্শন করে

### ৫. কনফিগারেশন

**ফাইল:** `application.properties`

অ্যাপ্লিকেশনের জন্য কনফিগারেশন সেটিংস:

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

**কনফিগারেশন ব্যাখ্যা:**

১. **ফাইল আপলোড**: ফাইল এবং পুরো মাল্টিপার্ট অনুরোধ ১০MB এ সীমাবদ্ধ; হেডারগুলোর জন্য স্থান রাখতে ছবিগুলো এই সীমার নিচে রাখুন
২. **লগিং**: এক্সিকিউশনের সময় কোন তথ্য লগ হবে তা নিয়ন্ত্রণ করে
৩. **Azure AI Foundry**: ব্যবহারের জন্য এন্ডপয়েন্ট এবং মডেল ডিপ্লয়মেন্ট নির্দিষ্ট করে (কী বিহীন প্রমাণীকরণ)
৪. **নিরাপত্তা**: CSRF সুরক্ষা সক্রিয় থাকে; মডেল ডায়াগনস্টিক সার্ভারে লগ হয়, এবং কন্ট্রোলার সাধারণ মডেল-ব্যর্থতা বার্তা দেখায়

## অ্যাপ্লিকেশন চালানো

### ধাপ ১: সাইন ইন এবং আপনার এন্ডপয়েন্ট সেট করুন

প্রমাণীকরণ কী বিহীন (Microsoft Entra ID), তাই কোনো API কী নেই। সাইন ইন করুন এবং আপনার Foundry এন্ডপয়েন্ট সেট করুন:

**উইন্ডোজ (কমান্ড প্রম্পট):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**উইন্ডোজ (পাওয়ারশেল):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**লিনাক্স/ম্যাকওএস:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**কেন এটি প্রয়োজন:**
- Azure AI Foundry Microsoft Entra ID ব্যবহার করে ইনফেরেন্স অনুরোধ প্রমাণীকরণ করে
- কী বিহীন প্রমাণীকরণ মানে আপনার সোর্স কোড বা পরিবেশে কোনো গোপনীয়তা থাকে না
- আপনার অ্যাকাউন্টে এই রিসোর্সের উপর **Cognitive Services OpenAI User** ভূমিকা থাকতে হবে

ডিফল্ট ডিপ্লয়মেন্ট নাম `gpt-5.6-luna`। আপনার GPT-5.6 Luna ডিপ্লয়মেন্টের অন্য নাম থাকলে, অ্যাপ্লিকেশন শুরু করার আগে একই টার্মিনালে `AZURE_OPENAI_DEPLOYMENT` সেট করুন। ইমেজ বিশ্লেষণ এবং গল্প তৈরি উভয়ই এই সেটিং ব্যবহার করে।

### ধাপ ২: নির্মাণ এবং চালান

প্রকল্প ডিরেক্টরিতে যান:
```bash
cd 04-PracticalSamples/petstory
```

স্ট্যান্ডঅ্যালোন এক্সিকিউটেবল JAR তৈরি করুন এবং সব অফলাইন পরীক্ষা চালান:
```bash
mvn clean package
```

সার্ভার শুরু করুন:
```bash
mvn spring-boot:run
```

অ্যাপ্লিকেশনটি `http://localhost:8080` এ শুরু হবে।

বিকল্পভাবে, একটি ফ্রি পোর্টে প্যাকেজড JAR শুরু করুন, উদাহরণস্বরূপ:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

ওই কমান্ডের জন্য, `http://localhost:8083/` খুলুন। একই `/analyze-image` এবং `/generate-story` রুট নির্বাচিত পোর্টে উপলব্ধ।

### ধাপ ৩: অ্যাপ্লিকেশন পরীক্ষা

১. **ওপেন করুন** `http://localhost:8080` আপনার ব্রাউজারে
২. **নির্বাচন করুন** ১০MB এর নিচে JPEG, PNG, GIF, বা WebP ফরম্যাটে একটি স্পষ্ট পোষ্য ছবি
৩. **ক্লিক করুন** "Analyze Image" এবং পোষ্য বর্ণনার জন্য অপেক্ষা করুন
৪. **ক্লিক করুন** সফল বিশ্লেষণের পর "Generate Story"
৫. **দেখুন** গল্পটি এবং ফলাফল পৃষ্ঠার লিঙ্ক ব্যবহার করে আপলোড ফর্মে ফিরে যান

সফল ছবি-থেকে-গল্প প্রবাহ দুটি মডেল কল করে, প্রতিটি বোতামের জন্য একটিঃ লাইভ ইনফারেন্স আপনার ডিপ্লয়মেন্টের কোটা ব্যবহার করে এবং চার্জ হতে পারে; রেট-সীমিত ডিপ্লয়মেন্ট শেয়ার করলে স্মোক পরীক্ষা সিরিয়ালি চালান। হোম পৃষ্ঠা লোড করা মডেল কল করে না।

## অফলাইন পরীক্ষা

স্যাম্পল ডিরেক্টরি থেকে চালান:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) লাইভ OpenAI SDK অনুরোধগুলি লুপব্যাক HTTP ফিক্সচারের মাধ্যমে ক্যাপচার করে। এটি উভয় অনুরোধের ডিপ্লয়মেন্ট, `reasoning_effort: none`, টোকেন সীমা, ইমেজ পেলোড, ইনপুট যাচাই, খালি প্রতিক্রিয়া এবং আপস্ট্রিম ত্রুটিগুলো পরীক্ষা করে।

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) MockMvc ব্যবহার করে মকড মডেল সার্ভিস দিয়ে Thymeleaf পৃষ্ঠা, আপলোড চুক্তি, CSRF, যাচাই, আউটপুট এস্কেপিং, এবং দৃশ্যমান ব্যর্থতাগুলো পরীক্ষা করে। এই পরীক্ষাগুলোতে Azure ক্রেডেনশিয়াল দরকার হয় না এবং কখনো পেইড Azure ইনফেরেন্স কল হয় না। Maven `target/surefire-reports` এর অধীনে Surefire রিপোর্ট লেখে।

## সবকিছু কিভাবে একসাথে কাজ করে

পোষ্য গল্প তৈরি করার সম্পূর্ণ প্রবাহ:

১. **ছবি নির্বাচন:** আপলোড ফর্মে একটি পোষ্য ছবি নির্বাচন করুন
২. **ছবি আপলোড:** "Analyze Image" CSRF হেডারসহ `/analyze-image` এ মাল্টিপার্ট POST পাঠায়
৩. **ছবি বিশ্লেষণ:** `StoryService` ইমেজটি GPT-5.6 Luna তে reasoning `none` সহ পাঠায়
৪. **বর্ণনা প্রদর্শন:** ব্রাউজার প্রাপ্ত বর্ণনা দেখায় এবং ফর্মে সংরক্ষণ করে
৫. **গল্প জমা:** "Generate Story" `description` এবং `_csrf` `/generate-story` এ পোস্ট করে
৬. **গল্প তৈরি:** কন্ট্রোলার বর্ণনা যাচাই করে এবং একই ডিপ্লয়মেন্ট reasoning `none` সহ কল করে
৭. **টেমপ্লেট রেন্ডারিং:** Thymeleaf বর্ণনা এবং গল্প এস্কেপ করে ফলাফল পৃষ্ঠায় দেখায়

**ত্রুটি পরিচালনা প্রবাহ:**
মডেল ব্যর্থ হলে সার্ভার কারণ লগ করে। ছবি বিশ্লেষণ HTTP 502 ফেরত দেয় এবং ব্রাউজার ত্রুটি দেখায়, "Generate Story" দেখায় না। গল্প তৈরি ফর্মে ত্রুটি বার্তা সহ পুনঃনির্দেশ করে। কোনো পথ পূর্বনির্ধারিত ফলাফল গোপনে প্রতিস্থাপন করে না।

## AI ইন্টিগ্রেশন বোঝা

### Azure AI Foundry (কী বিহীন)
সার্ভিস SDK কে আপনার রিসোর্সের `/openai/v1/` এন্ডপয়েন্ট দিয়ে কনফিগার করে। `DefaultAzureCredential` এবং `AuthenticationUtil.getBearerTokenSupplier` Microsoft Entra টোকেন সরবরাহ করে `https://ai.azure.com/.default` এর জন্য। লোকাল ডেভেলপমেন্ট Azure CLI সাইন-ইন ব্যবহার করতে পারে; Azure-হোস্টেড অ্যাপ ম্যানেজড আইডেন্টিটি ব্যবহার করতে পারে প্রয়োজনীয় রিসোর্স পারমিশন সহ।

### প্রম্পট ইঞ্জিনিয়ারিং
ইমেজ বিশ্লেষণ সংক্ষিপ্ত প্যারাগ্রাফে লক্ষণীয় পোষ্য বৈশিষ্ট্য অনুরোধ করে এবং মডেলকে বলে বিন্যাসের টেক্সটকে নির্দেশনা নয়, ডেটা হিসেবে বিবেচনা করতে। গল্প তৈরি ফিরে আসা বর্ণনা একটি আলাদা, পরিবারের উপযোগী লেখন অনুরোধে ব্যবহার করে। কোনো কলেই reasoning সক্রিয় বা তাপমাত্রার ওভাররাইড সেট করা হয় না।

### প্রতিক্রিয়া প্রক্রিয়াকরণ
শেয়ার করা প্রতিক্রিয়া হ্যান্ডলার অনুপস্থিত পছন্দ এবং খালি বা শুধুমাত্র স্পেস সহ কন্টেন্ট প্রত্যাখ্যান করে, বৈধ কন্টেন্ট ট্রিম করে, এবং আপস্ট্রিম ব্যর্থতা সংরক্ষণ করে। ইমেজ বর্ণনা পরবর্তী গল্প ফর্মে ফিট করার জন্য ১০০০ অক্ষরে সীমাবদ্ধ। আসল মডেল ব্যর্থতা ডায়াগনস্টিকের জন্য রাখা হয় কিন্তু ব্যবহারকারীকে দেখানো হয় না।

## পরবর্তী ধাপ

আরও উদাহরণের জন্য দেখুন [অধ্যায় ০৪: ব্যবহারিক নমুনা](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা শুদ্ধতার জন্য চেষ্টা করি, অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার স্বভাষায় কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে প্রয়োজনীয় ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->