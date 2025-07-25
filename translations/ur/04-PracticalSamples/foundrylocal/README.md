<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T10:49:16+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "ur"
}
-->
# فاؤنڈری لوکل اسپرنگ بوٹ ٹیوٹوریل

## مواد کی فہرست

- [ضروریات](../../../../04-PracticalSamples/foundrylocal)
- [پروجیکٹ کا جائزہ](../../../../04-PracticalSamples/foundrylocal)
- [کوڈ کو سمجھنا](../../../../04-PracticalSamples/foundrylocal)
  - [1. ایپلیکیشن کنفیگریشن (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. مین ایپلیکیشن کلاس (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. اے آئی سروس لیئر (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. پروجیکٹ ڈیپینڈنسیز (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [یہ سب کیسے کام کرتا ہے](../../../../04-PracticalSamples/foundrylocal)
- [فاؤنڈری لوکل سیٹ اپ کرنا](../../../../04-PracticalSamples/foundrylocal)
- [ایپلیکیشن چلانا](../../../../04-PracticalSamples/foundrylocal)
- [متوقع نتائج](../../../../04-PracticalSamples/foundrylocal)
- [اگلے مراحل](../../../../04-PracticalSamples/foundrylocal)
- [مسائل کا حل](../../../../04-PracticalSamples/foundrylocal)

## ضروریات

اس ٹیوٹوریل کو شروع کرنے سے پہلے، یقینی بنائیں کہ آپ کے پاس درج ذیل موجود ہیں:

- **جاوا 21 یا اس سے زیادہ** آپ کے سسٹم پر انسٹال ہو
- **ماؤن 3.6+** پروجیکٹ کو بنانے کے لیے
- **فاؤنڈری لوکل** انسٹال اور چل رہا ہو

### **فاؤنڈری لوکل انسٹال کریں:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## پروجیکٹ کا جائزہ

یہ پروجیکٹ چار اہم اجزاء پر مشتمل ہے:

1. **Application.java** - اسپرنگ بوٹ ایپلیکیشن کا مرکزی انٹری پوائنٹ
2. **FoundryLocalService.java** - سروس لیئر جو اے آئی کمیونیکیشن کو ہینڈل کرتی ہے
3. **application.properties** - فاؤنڈری لوکل کنکشن کے لیے کنفیگریشن
4. **pom.xml** - ماؤن ڈیپینڈنسیز اور پروجیکٹ کنفیگریشن

## کوڈ کو سمجھنا

### 1. ایپلیکیشن کنفیگریشن (application.properties)

**فائل:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**یہ کیا کرتا ہے:**
- **base-url**: بتاتا ہے کہ فاؤنڈری لوکل کہاں چل رہا ہے (ڈیفالٹ پورٹ 5273)
- **model**: اس اے آئی ماڈل کا نام جو ٹیکسٹ جنریشن کے لیے استعمال ہوگا

**اہم تصور:** اسپرنگ بوٹ خود بخود ان پراپرٹیز کو لوڈ کرتا ہے اور انہیں آپ کی ایپلیکیشن میں `@Value` اینوٹیشن کے ذریعے دستیاب بناتا ہے۔

### 2. مین ایپلیکیشن کلاس (Application.java)

**فائل:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**یہ کیا کرتا ہے:**
- `@SpringBootApplication` اسپرنگ بوٹ آٹو کنفیگریشن کو فعال کرتا ہے
- `WebApplicationType.NONE` اسپرنگ کو بتاتا ہے کہ یہ کمانڈ لائن ایپ ہے، ویب سرور نہیں
- مین میتھڈ اسپرنگ ایپلیکیشن کو شروع کرتا ہے

**ڈیمو رنر:**
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

**یہ کیا کرتا ہے:**
- `@Bean` ایک کمپوننٹ بناتا ہے جسے اسپرنگ مینیج کرتا ہے
- `CommandLineRunner` اسپرنگ بوٹ کے شروع ہونے کے بعد کوڈ چلاتا ہے
- `foundryLocalService` اسپرنگ کے ذریعے خود بخود انجیکٹ ہوتا ہے (ڈیپینڈنسی انجیکشن)
- اے آئی کو ایک ٹیسٹ میسج بھیجتا ہے اور جواب دکھاتا ہے

### 3. اے آئی سروس لیئر (FoundryLocalService.java)

**فائل:** `src/main/java/com/example/FoundryLocalService.java`

#### کنفیگریشن انجیکشن:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**یہ کیا کرتا ہے:**
- `@Service` اسپرنگ کو بتاتا ہے کہ یہ کلاس بزنس لاجک فراہم کرتی ہے
- `@Value` کنفیگریشن ویلیوز کو application.properties سے انجیکٹ کرتا ہے
- `:default-value` سینٹیکس پراپرٹیز نہ ہونے کی صورت میں فال بیک ویلیوز فراہم کرتا ہے

#### کلائنٹ انیشلائزیشن:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**یہ کیا کرتا ہے:**
- `@PostConstruct` اس میتھڈ کو اسپرنگ کے سروس بنانے کے بعد چلاتا ہے
- ایک اوپن اے آئی کلائنٹ بناتا ہے جو آپ کے لوکل فاؤنڈری لوکل انسٹینس کی طرف اشارہ کرتا ہے
- `/v1` راستہ اوپن اے آئی API مطابقت کے لیے ضروری ہے
- API کی "unused" کلید کیونکہ لوکل ڈیولپمنٹ میں تصدیق کی ضرورت نہیں ہوتی

#### چیٹ میتھڈ:
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

**یہ کیا کرتا ہے:**
- **ChatCompletionCreateParams**: اے آئی درخواست کو کنفیگر کرتا ہے
  - `model`: بتاتا ہے کہ کون سا اے آئی ماڈل استعمال کرنا ہے
  - `addUserMessage`: آپ کا میسج گفتگو میں شامل کرتا ہے
  - `maxCompletionTokens`: جواب کی لمبائی محدود کرتا ہے (وسائل بچانے کے لیے)
  - `temperature`: بے ترتیبی کو کنٹرول کرتا ہے (0.0 = متعین، 1.0 = تخلیقی)
- **API کال**: درخواست فاؤنڈری لوکل کو بھیجتا ہے
- **جواب ہینڈلنگ**: اے آئی کے ٹیکسٹ جواب کو محفوظ طریقے سے نکالتا ہے
- **غلطی ہینڈلنگ**: مددگار غلطی پیغامات کے ساتھ ایکسیپشنز کو لپیٹتا ہے

### 4. پروجیکٹ ڈیپینڈنسیز (pom.xml)

**اہم ڈیپینڈنسیز:**

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

**یہ کیا کرتے ہیں:**
- **spring-boot-starter**: اسپرنگ بوٹ کی بنیادی فعالیت فراہم کرتا ہے
- **openai-java**: اوپن اے آئی جاوا SDK API کمیونیکیشن کے لیے
- **jackson-databind**: API کالز کے لیے JSON سیریلائزیشن/ڈی سیریلائزیشن کو ہینڈل کرتا ہے

## یہ سب کیسے کام کرتا ہے

جب آپ ایپلیکیشن چلاتے ہیں تو مکمل فلو یہ ہے:

1. **اسٹارٹ اپ**: اسپرنگ بوٹ شروع ہوتا ہے اور `application.properties` کو پڑھتا ہے
2. **سروس تخلیق**: اسپرنگ `FoundryLocalService` بناتا ہے اور کنفیگریشن ویلیوز انجیکٹ کرتا ہے
3. **کلائنٹ سیٹ اپ**: `@PostConstruct` اوپن اے آئی کلائنٹ کو فاؤنڈری لوکل سے کنیکٹ کرنے کے لیے انیشلائز کرتا ہے
4. **ڈیمو ایکزیکیوشن**: `CommandLineRunner` اسٹارٹ اپ کے بعد ایکزیکیوٹ ہوتا ہے
5. **اے آئی کال**: ڈیمو `foundryLocalService.chat()` کو ایک ٹیسٹ میسج کے ساتھ کال کرتا ہے
6. **API درخواست**: سروس اوپن اے آئی مطابقت پذیر درخواست فاؤنڈری لوکل کو بھیجتی ہے
7. **جواب پروسیسنگ**: سروس اے آئی کے جواب کو نکالتی اور واپس کرتی ہے
8. **ڈسپلے**: ایپلیکیشن جواب پرنٹ کرتی ہے اور بند ہو جاتی ہے

## فاؤنڈری لوکل سیٹ اپ کرنا

فاؤنڈری لوکل سیٹ اپ کرنے کے لیے، درج ذیل مراحل پر عمل کریں:

1. **فاؤنڈری لوکل انسٹال کریں** جیسا کہ [ضروریات](../../../../04-PracticalSamples/foundrylocal) سیکشن میں بتایا گیا ہے۔
2. **اے آئی ماڈل ڈاؤنلوڈ کریں** جو آپ استعمال کرنا چاہتے ہیں، مثلاً `phi-3.5-mini`، درج ذیل کمانڈ کے ساتھ:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties فائل کو کنفیگر کریں** تاکہ آپ کے فاؤنڈری لوکل سیٹنگز سے میل کھائے، خاص طور پر اگر آپ مختلف پورٹ یا ماڈل استعمال کر رہے ہیں۔

## ایپلیکیشن چلانا

### مرحلہ 1: فاؤنڈری لوکل شروع کریں
```bash
foundry model run phi-3.5-mini
```

### مرحلہ 2: ایپلیکیشن کو بنائیں اور چلائیں
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## متوقع نتائج

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

## اگلے مراحل

مزید مثالوں کے لیے، دیکھیں [باب 04: عملی نمونے](../README.md)

## مسائل کا حل

### عام مسائل

**"کنکشن ریفیوزڈ" یا "سروس دستیاب نہیں"**
- یقینی بنائیں کہ فاؤنڈری لوکل چل رہا ہے: `foundry model list`
- تصدیق کریں کہ سروس پورٹ 5273 پر ہے: `application.properties` چیک کریں
- فاؤنڈری لوکل کو دوبارہ شروع کرنے کی کوشش کریں: `foundry model run phi-3.5-mini`

**"ماڈل نہیں ملا" کی غلطیاں**
- دستیاب ماڈلز چیک کریں: `foundry model list`
- `application.properties` میں ماڈل کا نام بالکل درست اپڈیٹ کریں
- اگر ضرورت ہو تو ماڈل ڈاؤنلوڈ کریں: `foundry model run phi-3.5-mini`

**ماؤن کمپائلیشن کی غلطیاں**
- جاوا 21 یا اس سے زیادہ یقینی بنائیں: `java -version`
- صاف کریں اور دوبارہ بنائیں: `mvn clean compile`
- ڈیپینڈنسی ڈاؤنلوڈ کے لیے انٹرنیٹ کنکشن چیک کریں

**ایپلیکیشن شروع ہوتی ہے لیکن کوئی آؤٹ پٹ نہیں**
- تصدیق کریں کہ فاؤنڈری لوکل جواب دے رہا ہے: براؤزر میں `http://localhost:5273` کھولیں
- مخصوص غلطی پیغامات کے لیے ایپلیکیشن لاگز چیک کریں
- یقینی بنائیں کہ ماڈل مکمل طور پر لوڈ ہو چکا ہے اور تیار ہے

**ڈسکلیمر**:  
یہ دستاویز AI ترجمہ سروس [Co-op Translator](https://github.com/Azure/co-op-translator) کا استعمال کرتے ہوئے ترجمہ کی گئی ہے۔ ہم درستگی کے لیے کوشش کرتے ہیں، لیکن براہ کرم آگاہ رہیں کہ خودکار ترجمے میں غلطیاں یا غیر درستیاں ہو سکتی ہیں۔ اصل دستاویز کو اس کی اصل زبان میں مستند ذریعہ سمجھا جانا چاہیے۔ اہم معلومات کے لیے، پیشہ ور انسانی ترجمہ کی سفارش کی جاتی ہے۔ ہم اس ترجمے کے استعمال سے پیدا ہونے والی کسی بھی غلط فہمی یا غلط تشریح کے ذمہ دار نہیں ہیں۔