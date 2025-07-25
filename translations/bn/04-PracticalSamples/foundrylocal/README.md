<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:09:03+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "bn"
}
-->
# Foundry Local Spring Boot টিউটোরিয়াল

## সূচিপত্র

- [প্রয়োজনীয়তা](../../../../04-PracticalSamples/foundrylocal)
- [প্রকল্পের সংক্ষিপ্ত বিবরণ](../../../../04-PracticalSamples/foundrylocal)
- [কোড বোঝা](../../../../04-PracticalSamples/foundrylocal)
  - [১. অ্যাপ্লিকেশন কনফিগারেশন (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [২. প্রধান অ্যাপ্লিকেশন ক্লাস (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [৩. এআই সার্ভিস লেয়ার (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [৪. প্রকল্পের নির্ভরতা (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [সবকিছু কীভাবে একসাথে কাজ করে](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local সেটআপ করা](../../../../04-PracticalSamples/foundrylocal)
- [অ্যাপ্লিকেশন চালানো](../../../../04-PracticalSamples/foundrylocal)
- [প্রত্যাশিত আউটপুট](../../../../04-PracticalSamples/foundrylocal)
- [পরবর্তী পদক্ষেপ](../../../../04-PracticalSamples/foundrylocal)
- [সমস্যা সমাধান](../../../../04-PracticalSamples/foundrylocal)

## প্রয়োজনীয়তা

এই টিউটোরিয়াল শুরু করার আগে নিশ্চিত করুন যে আপনার কাছে রয়েছে:

- **Java 21 বা তার বেশি** আপনার সিস্টেমে ইনস্টল করা
- **Maven 3.6+** প্রকল্পটি তৈরি করার জন্য
- **Foundry Local** ইনস্টল করা এবং চালু রয়েছে

### **Foundry Local ইনস্টল করুন:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## প্রকল্পের সংক্ষিপ্ত বিবরণ

এই প্রকল্পটি চারটি প্রধান উপাদান নিয়ে গঠিত:

1. **Application.java** - প্রধান Spring Boot অ্যাপ্লিকেশনের এন্ট্রি পয়েন্ট
2. **FoundryLocalService.java** - সার্ভিস লেয়ার যা এআই যোগাযোগ পরিচালনা করে
3. **application.properties** - Foundry Local সংযোগের জন্য কনফিগারেশন
4. **pom.xml** - Maven নির্ভরতা এবং প্রকল্পের কনফিগারেশন

## কোড বোঝা

### ১. অ্যাপ্লিকেশন কনফিগারেশন (application.properties)

**ফাইল:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**এটি কী করে:**
- **base-url**: Foundry Local কোথায় চলছে তা নির্দিষ্ট করে (ডিফল্ট পোর্ট ৫২৭৩)
- **model**: টেক্সট জেনারেশনের জন্য ব্যবহৃত এআই মডেলের নাম নির্ধারণ করে

**মূল ধারণা:** Spring Boot স্বয়ংক্রিয়ভাবে এই প্রপার্টিগুলি লোড করে এবং `@Value` অ্যানোটেশনের মাধ্যমে আপনার অ্যাপ্লিকেশনে উপলব্ধ করে।

### ২. প্রধান অ্যাপ্লিকেশন ক্লাস (Application.java)

**ফাইল:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**এটি কী করে:**
- `@SpringBootApplication` Spring Boot এর অটো-কনফিগারেশন সক্ষম করে
- `WebApplicationType.NONE` Spring-কে জানায় এটি একটি কমান্ড-লাইন অ্যাপ, ওয়েব সার্ভার নয়
- প্রধান মেথড Spring অ্যাপ্লিকেশন শুরু করে

**ডেমো রানার:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**এটি কী করে:**
- `@Bean` একটি কম্পোনেন্ট তৈরি করে যা Spring পরিচালনা করে
- `CommandLineRunner` Spring Boot শুরু হওয়ার পরে কোড চালায়
- `foundryLocalService` Spring দ্বারা স্বয়ংক্রিয়ভাবে ইনজেক্ট করা হয় (ডিপেনডেন্সি ইনজেকশন)
- একটি টেস্ট মেসেজ এআই-তে পাঠায় এবং প্রতিক্রিয়া প্রদর্শন করে

### ৩. এআই সার্ভিস লেয়ার (FoundryLocalService.java)

**ফাইল:** `src/main/java/com/example/FoundryLocalService.java`

#### কনফিগারেশন ইনজেকশন:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**এটি কী করে:**
- `@Service` Spring-কে জানায় এই ক্লাসটি ব্যবসায়িক লজিক প্রদান করে
- `@Value` application.properties থেকে কনফিগারেশন মান ইনজেক্ট করে
- `:default-value` সিনট্যাক্স প্রপার্টি সেট না থাকলে ডিফল্ট মান প্রদান করে

#### ক্লায়েন্ট ইনিশিয়ালাইজেশন:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**এটি কী করে:**
- `@PostConstruct` Spring সার্ভিস তৈরি করার পরে এই মেথড চালায়
- একটি OpenAI ক্লায়েন্ট তৈরি করে যা আপনার স্থানীয় Foundry Local ইনস্ট্যান্সে নির্দেশ করে
- `/v1` পাথ OpenAI API সামঞ্জস্যের জন্য প্রয়োজন
- API কী "unused" কারণ স্থানীয় ডেভেলপমেন্টে প্রমাণীকরণ প্রয়োজন হয় না

#### চ্যাট মেথড:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**এটি কী করে:**
- **ChatCompletionCreateParams**: এআই অনুরোধ কনফিগার করে
  - `model`: কোন এআই মডেল ব্যবহার করতে হবে তা নির্ধারণ করে
  - `addUserMessage`: আপনার মেসেজ কথোপকথনে যোগ করে
  - `maxCompletionTokens`: প্রতিক্রিয়া কতটা দীর্ঘ হতে পারে তা সীমাবদ্ধ করে (সম্পদ সংরক্ষণ করে)
  - `temperature`: র্যান্ডমনেস নিয়ন্ত্রণ করে (০.০ = নির্ধারিত, ১.০ = সৃজনশীল)
- **API কল**: অনুরোধটি Foundry Local-এ পাঠায়
- **প্রতিক্রিয়া পরিচালনা**: এআই-এর টেক্সট প্রতিক্রিয়া নিরাপদে বের করে
- **ত্রুটি পরিচালনা**: সহায়ক ত্রুটি বার্তাগুলির সাথে এক্সসেপশন মোড়ায়

### ৪. প্রকল্পের নির্ভরতা (pom.xml)

**মূল নির্ভরতা:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**এগুলি কী করে:**
- **spring-boot-starter**: Spring Boot এর মূল কার্যকারিতা প্রদান করে
- **openai-java**: OpenAI Java SDK API যোগাযোগের জন্য
- **jackson-databind**: API কলের জন্য JSON সিরিয়ালাইজেশন/ডিসিরিয়ালাইজেশন পরিচালনা করে

## সবকিছু কীভাবে একসাথে কাজ করে

যখন আপনি অ্যাপ্লিকেশন চালান, তখন সম্পূর্ণ প্রবাহটি এভাবে হয়:

1. **স্টার্টআপ**: Spring Boot শুরু হয় এবং `application.properties` পড়ে
2. **সার্ভিস তৈরি**: Spring `FoundryLocalService` তৈরি করে এবং কনফিগারেশন মান ইনজেক্ট করে
3. **ক্লায়েন্ট সেটআপ**: `@PostConstruct` OpenAI ক্লায়েন্ট ইনিশিয়ালাইজ করে Foundry Local-এ সংযোগ করতে
4. **ডেমো এক্সিকিউশন**: `CommandLineRunner` স্টার্টআপের পরে চালায়
5. **এআই কল**: ডেমো একটি টেস্ট মেসেজ সহ `foundryLocalService.chat()` কল করে
6. **API অনুরোধ**: সার্ভিস OpenAI-সামঞ্জস্যপূর্ণ অনুরোধ তৈরি করে এবং Foundry Local-এ পাঠায়
7. **প্রতিক্রিয়া প্রক্রিয়াকরণ**: সার্ভিস এআই-এর প্রতিক্রিয়া বের করে এবং ফেরত দেয়
8. **প্রদর্শন**: অ্যাপ্লিকেশন প্রতিক্রিয়া প্রিন্ট করে এবং বেরিয়ে যায়

## Foundry Local সেটআপ করা

Foundry Local সেটআপ করতে, এই ধাপগুলি অনুসরণ করুন:

1. **Foundry Local ইনস্টল করুন** [প্রয়োজনীয়তা](../../../../04-PracticalSamples/foundrylocal) সেকশনের নির্দেশনা অনুসারে।
2. **এআই মডেল ডাউনলোড করুন** যা আপনি ব্যবহার করতে চান, যেমন `phi-3.5-mini`, নিম্নলিখিত কমান্ড দিয়ে:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties** ফাইলটি আপনার Foundry Local সেটিংসের সাথে মেলাতে কনফিগার করুন, বিশেষ করে যদি আপনি ভিন্ন পোর্ট বা মডেল ব্যবহার করেন।

## অ্যাপ্লিকেশন চালানো

### ধাপ ১: Foundry Local শুরু করুন
```bash
foundry model run phi-3.5-mini
```

### ধাপ ২: অ্যাপ্লিকেশন তৈরি এবং চালান
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## প্রত্যাশিত আউটপুট

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## পরবর্তী পদক্ষেপ

আরও উদাহরণের জন্য দেখুন [Chapter 04: Practical samples](../README.md)

## সমস্যার সমাধান

### সাধারণ সমস্যা

**"Connection refused" বা "Service unavailable"**
- নিশ্চিত করুন Foundry Local চলছে: `foundry model list`
- সার্ভিসটি পোর্ট ৫২৭৩-এ রয়েছে কিনা যাচাই করুন: `application.properties` পরীক্ষা করুন
- Foundry Local পুনরায় চালানোর চেষ্টা করুন: `foundry model run phi-3.5-mini`

**"Model not found" ত্রুটি**
- উপলব্ধ মডেলগুলি পরীক্ষা করুন: `foundry model list`
- `application.properties`-এ মডেলের নাম সঠিকভাবে আপডেট করুন
- প্রয়োজন হলে মডেলটি ডাউনলোড করুন: `foundry model run phi-3.5-mini`

**Maven কম্পাইলেশন ত্রুটি**
- নিশ্চিত করুন Java 21 বা তার বেশি: `java -version`
- ক্লিন এবং পুনরায় তৈরি করুন: `mvn clean compile`
- নির্ভরতা ডাউনলোডের জন্য ইন্টারনেট সংযোগ পরীক্ষা করুন

**অ্যাপ্লিকেশন শুরু হয় কিন্তু কোনো আউটপুট নেই**
- নিশ্চিত করুন Foundry Local প্রতিক্রিয়া দিচ্ছে: ব্রাউজারে `http://localhost:5273` খুলুন
- নির্দিষ্ট ত্রুটি বার্তার জন্য অ্যাপ্লিকেশন লগ পরীক্ষা করুন
- নিশ্চিত করুন মডেলটি সম্পূর্ণরূপে লোড হয়েছে এবং প্রস্তুত

**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসম্ভব সঠিকতার জন্য চেষ্টা করি, তবে অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। এর মূল ভাষায় থাকা নথিটিকে প্রামাণিক উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য, পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদ ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যা হলে আমরা দায়বদ্ধ থাকব না।