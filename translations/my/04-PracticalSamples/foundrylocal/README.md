<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:25:00+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "my"
}
-->
# Foundry Local Spring Boot သင်ခန်းစာ

## အကြောင်းအရာများ

- [လိုအပ်ချက်များ](../../../../04-PracticalSamples/foundrylocal)
- [ပရောဂျက်အကျဉ်း](../../../../04-PracticalSamples/foundrylocal)
- [ကုဒ်ကိုနားလည်ခြင်း](../../../../04-PracticalSamples/foundrylocal)
  - [1. အပလီကေးရှင်းဖိုင် (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. အဓိကအပလီကေးရှင်းကလပ် (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI ဝန်ဆောင်မှုအလွှာ (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. ပရောဂျက်အခြေခံလိုအပ်ချက်များ (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [ဘယ်လိုအလုပ်လုပ်တယ်ဆိုတာ](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local ကိုတပ်ဆင်ခြင်း](../../../../04-PracticalSamples/foundrylocal)
- [အပလီကေးရှင်းကို run လုပ်ခြင်း](../../../../04-PracticalSamples/foundrylocal)
- [မျှော်မှန်းထားသောရလဒ်](../../../../04-PracticalSamples/foundrylocal)
- [နောက်ထပ်အဆင့်များ](../../../../04-PracticalSamples/foundrylocal)
- [ပြဿနာရှာဖွေဖြေရှင်းခြင်း](../../../../04-PracticalSamples/foundrylocal)

## လိုအပ်ချက်များ

ဒီသင်ခန်းစာကိုစတင်မတိုင်မီ သင့်မှာအောက်ပါအရာတွေရှိရမယ်-

- **Java 21 သို့မဟုတ် အထက်** ကို သင့်စနစ်မှာတပ်ဆင်ထားရမယ်
- **Maven 3.6+** ကို ပရောဂျက်ကို build လုပ်ဖို့လိုအပ်တယ်
- **Foundry Local** ကိုတပ်ဆင်ပြီး run လုပ်ထားရမယ်

### **Foundry Local ကိုတပ်ဆင်ပါ:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## ပရောဂျက်အကျဉ်း

ဒီပရောဂျက်မှာ အဓိကအစိတ်အပိုင်း ၄ ခုပါဝင်ပါတယ်-

1. **Application.java** - Spring Boot အပလီကေးရှင်းရဲ့ အဓိက entry point
2. **FoundryLocalService.java** - AI ဆက်သွယ်မှုကို စီမံခန့်ခွဲတဲ့ ဝန်ဆောင်မှုအလွှာ
3. **application.properties** - Foundry Local ဆက်သွယ်မှုအတွက် configuration
4. **pom.xml** - Maven အခြေခံလိုအပ်ချက်များနှင့် ပရောဂျက် configuration

## ကုဒ်ကိုနားလည်ခြင်း

### 1. အပလီကေးရှင်းဖိုင် (application.properties)

**ဖိုင်:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**ဒီဖိုင်ဘာလုပ်သလဲ:**
- **base-url**: Foundry Local ရဲ့ run လုပ်နေတဲ့နေရာ (default port 5273)
- **model**: Text generation အတွက် အသုံးပြုမယ့် AI မော်ဒယ်နာမည်

**အဓိကအကြောင်းအရာ:** Spring Boot က ဒီ properties တွေကို အလိုအလျောက် load လုပ်ပြီး `@Value` annotation ကိုသုံးပြီး သင့်အပလီကေးရှင်းမှာ အသုံးပြုနိုင်အောင်လုပ်ပေးတယ်။

### 2. အဓိကအပလီကေးရှင်းကလပ် (Application.java)

**ဖိုင်:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**ဒီဖိုင်ဘာလုပ်သလဲ:**
- `@SpringBootApplication` က Spring Boot ရဲ့ auto-configuration ကို enable လုပ်တယ်
- `WebApplicationType.NONE` က command-line app ဖြစ်ပြီး web server မဟုတ်တာကို ပြောပြတယ်
- Main method က Spring application ကို start လုပ်တယ်

**Demo Runner:**
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

**ဒီဖိုင်ဘာလုပ်သလဲ:**
- `@Bean` က Spring ကစီမံခန့်ခွဲမယ့် component တစ်ခုကိုဖန်တီးတယ်
- `CommandLineRunner` က Spring Boot start လုပ်ပြီးတာနောက် code ကို run လုပ်တယ်
- `foundryLocalService` ကို Spring ကအလိုအလျောက် inject လုပ်တယ် (dependency injection)
- AI ကို စမ်းသပ်မက်ဆေ့ချ်တစ်ခုပို့ပြီး ပြန်လာတဲ့အဖြေကိုပြတယ်

### 3. AI ဝန်ဆောင်မှုအလွှာ (FoundryLocalService.java)

**ဖိုင်:** `src/main/java/com/example/FoundryLocalService.java`

#### Configuration Injection:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**ဒီဖိုင်ဘာလုပ်သလဲ:**
- `@Service` က ဒီ class က business logic ပေးတဲ့ class ဖြစ်တယ်ဆိုတာ Spring ကိုပြောတယ်
- `@Value` က application.properties ထဲက configuration values တွေကို inject လုပ်တယ်
- `:default-value` syntax က properties မရှိရင် fallback value ကိုပေးတယ်

#### Client Initialization:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**ဒီဖိုင်ဘာလုပ်သလဲ:**
- `@PostConstruct` က Spring က service ကိုဖန်တီးပြီးတာနောက် ဒီ method ကို run လုပ်တယ်
- Foundry Local instance ကို point လုပ်တဲ့ OpenAI client တစ်ခုကိုဖန်တီးတယ်
- `/v1` path က OpenAI API နဲ့သဟဇာတဖြစ်ဖို့လိုအပ်တယ်
- API key က "unused" ဖြစ်တယ်၊ ဒါကြောင့် local development မှာ authentication မလိုအပ်ဘူး

#### Chat Method:
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

**ဒီဖိုင်ဘာလုပ်သလဲ:**
- **ChatCompletionCreateParams**: AI request ကို configure လုပ်တယ်
  - `model`: အသုံးပြုမယ့် AI မော်ဒယ်ကိုသတ်မှတ်တယ်
  - `addUserMessage`: သင့်မက်ဆေ့ချ်ကို conversation ထဲထည့်တယ်
  - `maxCompletionTokens`: Response ရဲ့အရှည်ကိုကန့်သတ်တယ် (resource တွေသက်သာစေတယ်)
  - `temperature`: Randomness ကိုထိန်းချုပ်တယ် (0.0 = အတိအကျ, 1.0 = ဖန်တီးမှုများ)
- **API Call**: Foundry Local ကို request ပို့တယ်
- **Response Handling**: AI ရဲ့ text response ကို extract လုပ်တယ်
- **Error Handling**: အမှားတွေကို အကျိုးရှိတဲ့ error message နဲ့ wrap လုပ်တယ်

### 4. ပရောဂျက်အခြေခံလိုအပ်ချက်များ (pom.xml)

**အဓိကလိုအပ်ချက်များ:**

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

**ဒီလိုအပ်ချက်တွေဘာလုပ်သလဲ:**
- **spring-boot-starter**: Spring Boot ရဲ့ အဓိက functionality ကိုပေးတယ်
- **openai-java**: OpenAI Java SDK ကို API ဆက်သွယ်မှုအတွက်အသုံးပြုတယ်
- **jackson-databind**: API calls အတွက် JSON serialization/deserialization ကို handle လုပ်တယ်

## ဘယ်လိုအလုပ်လုပ်တယ်ဆိုတာ

အပလီကေးရှင်းကို run လုပ်တဲ့အခါ အလုပ်လုပ်ပုံစနစ်ကဒီလိုဖြစ်တယ်-

1. **Startup**: Spring Boot က start လုပ်ပြီး `application.properties` ကိုဖတ်တယ်
2. **Service Creation**: Spring က `FoundryLocalService` ကိုဖန်တီးပြီး configuration values တွေကို inject လုပ်တယ်
3. **Client Setup**: `@PostConstruct` က OpenAI client ကို initialize လုပ်တယ်
4. **Demo Execution**: `CommandLineRunner` က startup ပြီးတာနောက် run လုပ်တယ်
5. **AI Call**: Demo က `foundryLocalService.chat()` ကို test message နဲ့ခေါ်တယ်
6. **API Request**: Service က OpenAI-compatible request ကို Foundry Local ကိုပို့တယ်
7. **Response Processing**: Service က AI ရဲ့ response ကို extract လုပ်တယ်
8. **Display**: Application က response ကိုပြပြီးထွက်သွားတယ်

## Foundry Local ကိုတပ်ဆင်ခြင်း

Foundry Local ကိုတပ်ဆင်ဖို့ အောက်ပါအဆင့်တွေကိုလိုက်နာပါ-

1. **Foundry Local ကိုတပ်ဆင်ပါ** - [လိုအပ်ချက်များ](../../../../04-PracticalSamples/foundrylocal) အပိုင်းမှာပေးထားတဲ့ညွှန်ကြားချက်တွေကိုလိုက်နာပါ။
2. **အသုံးပြုမယ့် AI မော်ဒယ်ကိုဒေါင်းလုပ်ဆွဲပါ** - ဥပမာ `phi-3.5-mini` ကို အောက်ပါ command နဲ့:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties ဖိုင်ကို configure လုပ်ပါ** - သင့် Foundry Local settings နဲ့ကိုက်ညီအောင် especially port သို့မဟုတ် model ကိုပြောင်းပါ။

## အပလီကေးရှင်းကို run လုပ်ခြင်း

### အဆင့် 1: Foundry Local ကို Start လုပ်ပါ
```bash
foundry model run phi-3.5-mini
```

### အဆင့် 2: အပလီကေးရှင်းကို Build လုပ်ပြီး Run လုပ်ပါ
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## မျှော်မှန်းထားသောရလဒ်

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

## နောက်ထပ်အဆင့်များ

နောက်ထပ်ဥပမာတွေကိုကြည့်ရန် [Chapter 04: Practical samples](../README.md) ကိုကြည့်ပါ။

## ပြဿနာရှာဖွေဖြေရှင်းခြင်း

### အများဆုံးတွေ့ရတဲ့ပြဿနာများ

**"Connection refused" သို့မဟုတ် "Service unavailable"**
- Foundry Local run လုပ်နေမလားစစ်ပါ: `foundry model list`
- Service က port 5273 မှာရှိမလားစစ်ပါ: `application.properties` ကိုစစ်ပါ
- Foundry Local ကိုပြန်စတင်ပါ: `foundry model run phi-3.5-mini`

**"Model not found" errors**
- ရနိုင်တဲ့မော်ဒယ်တွေကိုစစ်ပါ: `foundry model list`
- application.properties ထဲမှာ model နာမည်ကိုတိတိကျကျပြောင်းပါ
- မော်ဒယ်ကိုဒေါင်းလုပ်ဆွဲပါ: `foundry model run phi-3.5-mini`

**Maven compilation errors**
- Java 21 သို့မဟုတ် အထက်ရှိမရှိစစ်ပါ: `java -version`
- Clean လုပ်ပြီးပြန် build လုပ်ပါ: `mvn clean compile`
- Dependency downloads အတွက် အင်တာနက်ချိတ်ဆက်မှုစစ်ပါ

**Application start လုပ်ပေမယ့် output မရှိ**
- Foundry Local ကတုံ့ပြန်နေမလားစစ်ပါ: Browser မှာ `http://localhost:5273` ကိုဖွင့်ပါ
- Application logs ထဲမှာ error messages ရှိမရှိစစ်ပါ
- မော်ဒယ်က load ပြီးပြီးဖြစ်မဖြစ်စစ်ပါ

**အကြောင်းကြားချက်**:  
ဤစာရွက်စာတမ်းကို AI ဘာသာပြန်ဝန်ဆောင်မှု [Co-op Translator](https://github.com/Azure/co-op-translator) ကို အသုံးပြု၍ ဘာသာပြန်ထားပါသည်။ ကျွန်ုပ်တို့သည် တိကျမှုအတွက် ကြိုးစားနေပါသော်လည်း၊ အလိုအလျောက် ဘာသာပြန်မှုများတွင် အမှားများ သို့မဟုတ် မမှန်ကန်မှုများ ပါဝင်နိုင်သည်ကို သတိပြုပါ။ မူရင်းစာရွက်စာတမ်းကို ၎င်း၏ မူလဘာသာစကားဖြင့် အာဏာတရ အရင်းအမြစ်အဖြစ် သတ်မှတ်သင့်ပါသည်။ အရေးကြီးသော အချက်အလက်များအတွက် လူက ဘာသာပြန်မှုကို အသုံးပြုရန် အကြံပြုပါသည်။ ဤဘာသာပြန်မှုကို အသုံးပြုခြင်းမှ ဖြစ်ပေါ်လာသော အလွဲအလွတ်များ သို့မဟုတ် အနားလွဲမှုများအတွက် ကျွန်ုပ်တို့သည် တာဝန်မယူပါ။