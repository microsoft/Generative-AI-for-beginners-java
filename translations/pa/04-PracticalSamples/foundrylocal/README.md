<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:17:23+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "pa"
}
-->
# Foundry Local Spring Boot ਟਿਊਟੋਰਿਅਲ

## ਸੂਚੀ

- [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../../04-PracticalSamples/foundrylocal)
- [ਪ੍ਰੋਜੈਕਟ ਝਲਕ](../../../../04-PracticalSamples/foundrylocal)
- [ਕੋਡ ਦੀ ਸਮਝ](../../../../04-PracticalSamples/foundrylocal)
  - [1. ਐਪਲੀਕੇਸ਼ਨ ਕਨਫਿਗਰੇਸ਼ਨ (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ ਕਲਾਸ (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI ਸੇਵਾ ਲੇਅਰ (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. ਪ੍ਰੋਜੈਕਟ ਡਿਪੈਂਡੈਂਸੀਜ਼ (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [ਇਹ ਸਭ ਕਿਵੇਂ ਕੰਮ ਕਰਦਾ ਹੈ](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local ਸੈਟਅੱਪ ਕਰਨਾ](../../../../04-PracticalSamples/foundrylocal)
- [ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣਾ](../../../../04-PracticalSamples/foundrylocal)
- [ਉਮੀਦਵਾਰ ਨਤੀਜਾ](../../../../04-PracticalSamples/foundrylocal)
- [ਅਗਲੇ ਕਦਮ](../../../../04-PracticalSamples/foundrylocal)
- [ਮਸਲੇ ਹੱਲ ਕਰਨਾ](../../../../04-PracticalSamples/foundrylocal)

## ਪੂਰਵ ਸ਼ਰਤਾਂ

ਇਸ ਟਿਊਟੋਰਿਅਲ ਨੂੰ ਸ਼ੁਰੂ ਕਰਨ ਤੋਂ ਪਹਿਲਾਂ, ਇਹ ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਤੁਹਾਡੇ ਕੋਲ:

- **Java 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ** ਤੁਹਾਡੇ ਸਿਸਟਮ 'ਤੇ ਇੰਸਟਾਲ ਹੈ
- **Maven 3.6+** ਪ੍ਰੋਜੈਕਟ ਬਣਾਉਣ ਲਈ
- **Foundry Local** ਇੰਸਟਾਲ ਅਤੇ ਚਲ ਰਹੀ ਹੈ

### **Foundry Local ਇੰਸਟਾਲ ਕਰੋ:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## ਪ੍ਰੋਜੈਕਟ ਝਲਕ

ਇਸ ਪ੍ਰੋਜੈਕਟ ਵਿੱਚ ਚਾਰ ਮੁੱਖ ਹਿੱਸੇ ਹਨ:

1. **Application.java** - ਮੁੱਖ Spring Boot ਐਪਲੀਕੇਸ਼ਨ ਐਂਟਰੀ ਪੌਇੰਟ
2. **FoundryLocalService.java** - ਸੇਵਾ ਲੇਅਰ ਜੋ AI ਸੰਚਾਰ ਨੂੰ ਸੰਭਾਲਦਾ ਹੈ
3. **application.properties** - Foundry Local ਕਨੈਕਸ਼ਨ ਲਈ ਕਨਫਿਗਰੇਸ਼ਨ
4. **pom.xml** - Maven ਡਿਪੈਂਡੈਂਸੀਜ਼ ਅਤੇ ਪ੍ਰੋਜੈਕਟ ਕਨਫਿਗਰੇਸ਼ਨ

## ਕੋਡ ਦੀ ਸਮਝ

### 1. ਐਪਲੀਕੇਸ਼ਨ ਕਨਫਿਗਰੇਸ਼ਨ (application.properties)

**ਫਾਇਲ:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- **base-url**: ਦੱਸਦਾ ਹੈ ਕਿ Foundry Local ਕਿੱਥੇ ਚਲ ਰਿਹਾ ਹੈ (ਡਿਫਾਲਟ ਪੋਰਟ 5273)
- **model**: AI ਮਾਡਲ ਦਾ ਨਾਮ ਦਿੰਦਾ ਹੈ ਜੋ ਟੈਕਸਟ ਜਨਰੇਸ਼ਨ ਲਈ ਵਰਤਿਆ ਜਾਵੇਗਾ

**ਮੁੱਖ ਧਾਰਨਾ:** Spring Boot ਆਪਣੇ ਆਪ ਇਹ ਗੁਣ ਲੋਡ ਕਰਦਾ ਹੈ ਅਤੇ `@Value` ਐਨੋਟੇਸ਼ਨ ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਤੁਹਾਡੇ ਐਪਲੀਕੇਸ਼ਨ ਵਿੱਚ ਉਪਲਬਧ ਕਰਵਾਉਂਦਾ ਹੈ।

### 2. ਮੁੱਖ ਐਪਲੀਕੇਸ਼ਨ ਕਲਾਸ (Application.java)

**ਫਾਇਲ:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- `@SpringBootApplication` Spring Boot ਆਟੋ-ਕਨਫਿਗਰੇਸ਼ਨ ਨੂੰ ਐਨਬਲ ਕਰਦਾ ਹੈ
- `WebApplicationType.NONE` Spring ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਇਹ ਇੱਕ ਕਮਾਂਡ-ਲਾਈਨ ਐਪ ਹੈ, ਵੈੱਬ ਸਰਵਰ ਨਹੀਂ
- ਮੁੱਖ ਮੈਥਡ Spring ਐਪਲੀਕੇਸ਼ਨ ਨੂੰ ਸ਼ੁਰੂ ਕਰਦਾ ਹੈ

**ਡੈਮੋ ਰਨਰ:**
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

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- `@Bean` ਇੱਕ ਕੰਪੋਨੈਂਟ ਬਣਾਉਂਦਾ ਹੈ ਜਿਸਨੂੰ Spring ਮੈਨੇਜ ਕਰਦਾ ਹੈ
- `CommandLineRunner` Spring Boot ਸ਼ੁਰੂ ਹੋਣ ਤੋਂ ਬਾਅਦ ਕੋਡ ਚਲਾਉਂਦਾ ਹੈ
- `foundryLocalService` Spring ਦੁਆਰਾ ਆਟੋਮੈਟਿਕਲੀ ਇੰਜੈਕਟ ਕੀਤਾ ਜਾਂਦਾ ਹੈ (ਡਿਪੈਂਡੈਂਸੀ ਇੰਜੈਕਸ਼ਨ)
- AI ਨੂੰ ਇੱਕ ਟੈਸਟ ਸੁਨੇਹਾ ਭੇਜਦਾ ਹੈ ਅਤੇ ਜਵਾਬ ਦਿਖਾਉਂਦਾ ਹੈ

### 3. AI ਸੇਵਾ ਲੇਅਰ (FoundryLocalService.java)

**ਫਾਇਲ:** `src/main/java/com/example/FoundryLocalService.java`

#### ਕਨਫਿਗਰੇਸ਼ਨ ਇੰਜੈਕਸ਼ਨ:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- `@Service` Spring ਨੂੰ ਦੱਸਦਾ ਹੈ ਕਿ ਇਹ ਕਲਾਸ ਬਿਜ਼ਨਸ ਲਾਜਿਕ ਪ੍ਰਦਾਨ ਕਰਦੀ ਹੈ
- `@Value` application.properties ਤੋਂ ਕਨਫਿਗਰੇਸ਼ਨ ਮੁੱਲਾਂ ਨੂੰ ਇੰਜੈਕਟ ਕਰਦਾ ਹੈ
- `:default-value` ਸਿੰਟੈਕਸ ਫਾਲਬੈਕ ਮੁੱਲ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ ਜੇ ਗੁਣ ਸੈਟ ਨਹੀਂ ਹਨ

#### ਕਲਾਇੰਟ ਇਨੀਸ਼ੀਅਲਾਈਜ਼ੇਸ਼ਨ:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- `@PostConstruct` Spring ਦੁਆਰਾ ਸੇਵਾ ਬਣਾਉਣ ਤੋਂ ਬਾਅਦ ਇਹ ਮੈਥਡ ਚਲਾਉਂਦਾ ਹੈ
- ਇੱਕ OpenAI ਕਲਾਇੰਟ ਬਣਾਉਂਦਾ ਹੈ ਜੋ ਤੁਹਾਡੇ ਸਥਾਨਕ Foundry Local ਇੰਸਟੈਂਸ ਨੂੰ ਪੋਇੰਟ ਕਰਦਾ ਹੈ
- `/v1` ਪਾਥ OpenAI API ਅਨੁਕੂਲਤਾ ਲਈ ਲਾਜ਼ਮੀ ਹੈ
- API ਕੁੰਜੀ "unused" ਹੈ ਕਿਉਂਕਿ ਸਥਾਨਕ ਵਿਕਾਸ ਵਿੱਚ ਪ੍ਰਮਾਣਿਕਤਾ ਦੀ ਲੋੜ ਨਹੀਂ ਹੁੰਦੀ

#### ਚੈਟ ਮੈਥਡ:
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

**ਇਹ ਕੀ ਕਰਦਾ ਹੈ:**
- **ChatCompletionCreateParams**: AI ਬੇਨਤੀ ਨੂੰ ਕਨਫਿਗਰ ਕਰਦਾ ਹੈ
  - `model`: ਦੱਸਦਾ ਹੈ ਕਿ ਕਿਹੜਾ AI ਮਾਡਲ ਵਰਤਣਾ ਹੈ
  - `addUserMessage`: ਗੱਲਬਾਤ ਵਿੱਚ ਤੁਹਾਡਾ ਸੁਨੇਹਾ ਸ਼ਾਮਲ ਕਰਦਾ ਹੈ
  - `maxCompletionTokens`: ਜਵਾਬ ਦੀ ਲੰਬਾਈ ਸੀਮਿਤ ਕਰਦਾ ਹੈ (ਸੰਸਾਧਨਾਂ ਦੀ ਬਚਤ ਕਰਦਾ ਹੈ)
  - `temperature`: ਰੈਂਡਮਨੈਸ ਨੂੰ ਕੰਟਰੋਲ ਕਰਦਾ ਹੈ (0.0 = ਨਿਰਧਾਰਿਤ, 1.0 = ਰਚਨਾਤਮਕ)
- **API ਕਾਲ**: ਬੇਨਤੀ Foundry Local ਨੂੰ ਭੇਜਦਾ ਹੈ
- **ਜਵਾਬ ਸੰਭਾਲਣਾ**: AI ਦੇ ਟੈਕਸਟ ਜਵਾਬ ਨੂੰ ਸੁਰੱਖਿਅਤ ਤਰੀਕੇ ਨਾਲ ਕੱਢਦਾ ਹੈ
- **ਗਲਤੀ ਸੰਭਾਲਣਾ**: ਮਦਦਗਾਰ ਗਲਤੀ ਸੁਨੇਹਿਆਂ ਨਾਲ ਅਪਵਾਦਾਂ ਨੂੰ ਲਪੇਟਦਾ ਹੈ

### 4. ਪ੍ਰੋਜੈਕਟ ਡਿਪੈਂਡੈਂਸੀਜ਼ (pom.xml)

**ਮੁੱਖ ਡਿਪੈਂਡੈਂਸੀਜ਼:**

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

**ਇਹ ਕੀ ਕਰਦੇ ਹਨ:**
- **spring-boot-starter**: ਮੁੱਖ Spring Boot ਫੰਕਸ਼ਨਲਿਟੀ ਪ੍ਰਦਾਨ ਕਰਦਾ ਹੈ
- **openai-java**: API ਸੰਚਾਰ ਲਈ OpenAI ਦਾ ਅਧਿਕਾਰਕ Java SDK
- **jackson-databind**: API ਕਾਲਾਂ ਲਈ JSON ਸੀਰੀਅਲਾਈਜ਼ੇਸ਼ਨ/ਡਿਸੀਰੀਅਲਾਈਜ਼ੇਸ਼ਨ ਨੂੰ ਸੰਭਾਲਦਾ ਹੈ

## ਇਹ ਸਭ ਕਿਵੇਂ ਕੰਮ ਕਰਦਾ ਹੈ

ਜਦੋਂ ਤੁਸੀਂ ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਂਦੇ ਹੋ ਤਾਂ ਪੂਰਾ ਪ੍ਰਕਿਰਿਆ ਇਹ ਹੈ:

1. **ਸ਼ੁਰੂਆਤ**: Spring Boot ਸ਼ੁਰੂ ਹੁੰਦਾ ਹੈ ਅਤੇ `application.properties` ਨੂੰ ਪੜ੍ਹਦਾ ਹੈ
2. **ਸੇਵਾ ਬਣਾਉਣਾ**: Spring `FoundryLocalService` ਬਣਾਉਂਦਾ ਹੈ ਅਤੇ ਕਨਫਿਗਰੇਸ਼ਨ ਮੁੱਲਾਂ ਨੂੰ ਇੰਜੈਕਟ ਕਰਦਾ ਹੈ
3. **ਕਲਾਇੰਟ ਸੈਟਅੱਪ**: `@PostConstruct` OpenAI ਕਲਾਇੰਟ ਨੂੰ Foundry Local ਨਾਲ ਕਨੈਕਟ ਕਰਨ ਲਈ ਇਨੀਸ਼ੀਅਲਾਈਜ਼ ਕਰਦਾ ਹੈ
4. **ਡੈਮੋ ਐਗਜ਼ਿਕਿਊਸ਼ਨ**: `CommandLineRunner` ਸ਼ੁਰੂਆਤ ਤੋਂ ਬਾਅਦ ਚਲਦਾ ਹੈ
5. **AI ਕਾਲ**: ਡੈਮੋ `foundryLocalService.chat()` ਨੂੰ ਇੱਕ ਟੈਸਟ ਸੁਨੇਹੇ ਨਾਲ ਕਾਲ ਕਰਦਾ ਹੈ
6. **API ਬੇਨਤੀ**: ਸੇਵਾ OpenAI-ਅਨੁਕੂਲ ਬੇਨਤੀ ਬਣਾਉਂਦੀ ਹੈ ਅਤੇ Foundry Local ਨੂੰ ਭੇਜਦੀ ਹੈ
7. **ਜਵਾਬ ਪ੍ਰਕਿਰਿਆ**: ਸੇਵਾ AI ਦੇ ਜਵਾਬ ਨੂੰ ਕੱਢਦੀ ਹੈ ਅਤੇ ਵਾਪਸ ਕਰਦੀ ਹੈ
8. **ਡਿਸਪਲੇ**: ਐਪਲੀਕੇਸ਼ਨ ਜਵਾਬ ਦਿਖਾਉਂਦਾ ਹੈ ਅਤੇ ਬੰਦ ਹੋ ਜਾਂਦਾ ਹੈ

## Foundry Local ਸੈਟਅੱਪ ਕਰਨਾ

Foundry Local ਸੈਟਅੱਪ ਕਰਨ ਲਈ, ਇਹ ਕਦਮ ਅਨੁਸਰ ਕਰੋ:

1. **Foundry Local ਇੰਸਟਾਲ ਕਰੋ** [ਪੂਰਵ ਸ਼ਰਤਾਂ](../../../../04-PracticalSamples/foundrylocal) ਸੈਕਸ਼ਨ ਵਿੱਚ ਦਿੱਤੀਆਂ ਹਦਾਇਤਾਂ ਦੀ ਵਰਤੋਂ ਕਰਕੇ।
2. **AI ਮਾਡਲ ਡਾਊਨਲੋਡ ਕਰੋ** ਜੋ ਤੁਸੀਂ ਵਰਤਣਾ ਚਾਹੁੰਦੇ ਹੋ, ਉਦਾਹਰਣ ਲਈ, `phi-3.5-mini`, ਹੇਠਾਂ ਦਿੱਤੇ ਕਮਾਂਡ ਨਾਲ:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties ਫਾਇਲ ਕਨਫਿਗਰ ਕਰੋ** Foundry Local ਸੈਟਿੰਗਾਂ ਨਾਲ ਮੇਲ ਖਾਣ ਲਈ, ਖਾਸ ਕਰਕੇ ਜੇ ਤੁਸੀਂ ਵੱਖਰੇ ਪੋਰਟ ਜਾਂ ਮਾਡਲ ਵਰਤ ਰਹੇ ਹੋ।

## ਐਪਲੀਕੇਸ਼ਨ ਚਲਾਉਣਾ

### ਕਦਮ 1: Foundry Local ਸ਼ੁਰੂ ਕਰੋ
```bash
foundry model run phi-3.5-mini
```

### ਕਦਮ 2: ਐਪਲੀਕੇਸ਼ਨ ਬਣਾਓ ਅਤੇ ਚਲਾਓ
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## ਉਮੀਦਵਾਰ ਨਤੀਜਾ

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

## ਅਗਲੇ ਕਦਮ

ਹੋਰ ਉਦਾਹਰਣਾਂ ਲਈ, [Chapter 04: Practical samples](../README.md) ਵੇਖੋ

## ਮਸਲੇ ਹੱਲ ਕਰਨਾ

### ਆਮ ਸਮੱਸਿਆਵਾਂ

**"Connection refused" ਜਾਂ "Service unavailable"**
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ Foundry Local ਚਲ ਰਿਹਾ ਹੈ: `foundry model list`
- ਸੇਵਾ ਪੋਰਟ 5273 'ਤੇ ਹੈ: `application.properties` ਚੈੱਕ ਕਰੋ
- Foundry Local ਨੂੰ ਮੁੜ ਸ਼ੁਰੂ ਕਰਨ ਦੀ ਕੋਸ਼ਿਸ਼ ਕਰੋ: `foundry model run phi-3.5-mini`

**"Model not found" errors**
- ਉਪਲਬਧ ਮਾਡਲਾਂ ਦੀ ਜਾਂਚ ਕਰੋ: `foundry model list`
- `application.properties` ਵਿੱਚ ਮਾਡਲ ਦਾ ਨਾਮ ਸਹੀ ਤਰੀਕੇ ਨਾਲ ਅਪਡੇਟ ਕਰੋ
- ਜੇ ਲੋੜ ਹੋਵੇ ਤਾਂ ਮਾਡਲ ਡਾਊਨਲੋਡ ਕਰੋ: `foundry model run phi-3.5-mini`

**Maven ਕੰਪਾਇਲੇਸ਼ਨ ਸਮੱਸਿਆਵਾਂ**
- Java 21 ਜਾਂ ਇਸ ਤੋਂ ਉੱਚਾ ਯਕੀਨੀ ਬਣਾਓ: `java -version`
- ਸਾਫ਼ ਕਰੋ ਅਤੇ ਮੁੜ ਬਣਾਓ: `mvn clean compile`
- ਡਿਪੈਂਡੈਂਸੀ ਡਾਊਨਲੋਡ ਲਈ ਇੰਟਰਨੈਟ ਕਨੈਕਸ਼ਨ ਚੈੱਕ ਕਰੋ

**ਐਪਲੀਕੇਸ਼ਨ ਸ਼ੁਰੂ ਹੁੰਦੀ ਹੈ ਪਰ ਕੋਈ ਨਤੀਜਾ ਨਹੀਂ**
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ Foundry Local ਜਵਾਬ ਦੇ ਰਿਹਾ ਹੈ: `http://localhost:5273` ਨੂੰ ਬ੍ਰਾਊਜ਼ਰ ਵਿੱਚ ਖੋਲ੍ਹੋ
- ਖਾਸ ਗਲਤੀ ਸੁਨੇਹਿਆਂ ਲਈ ਐਪਲੀਕੇਸ਼ਨ ਲਾਗਾਂ ਦੀ ਜਾਂਚ ਕਰੋ
- ਯਕੀਨੀ ਬਣਾਓ ਕਿ ਮਾਡਲ ਪੂਰੀ ਤਰ੍ਹਾਂ ਲੋਡ ਹੋਇਆ ਹੈ ਅਤੇ ਤਿਆਰ ਹੈ

**ਅਸਵੀਕਾਰਨਾ**:  
ਇਹ ਦਸਤਾਵੇਜ਼ AI ਅਨੁਵਾਦ ਸੇਵਾ [Co-op Translator](https://github.com/Azure/co-op-translator) ਦੀ ਵਰਤੋਂ ਕਰਕੇ ਅਨੁਵਾਦ ਕੀਤਾ ਗਿਆ ਹੈ। ਹਾਲਾਂਕਿ ਅਸੀਂ ਸਹੀਅਤਾ ਲਈ ਯਤਨਸ਼ੀਲ ਹਾਂ, ਕਿਰਪਾ ਕਰਕੇ ਧਿਆਨ ਦਿਓ ਕਿ ਸਵੈਚਾਲਿਤ ਅਨੁਵਾਦਾਂ ਵਿੱਚ ਗਲਤੀਆਂ ਜਾਂ ਅਸੁੱਤੀਆਂ ਹੋ ਸਕਦੀਆਂ ਹਨ। ਮੂਲ ਦਸਤਾਵੇਜ਼ ਨੂੰ ਇਸਦੀ ਮੂਲ ਭਾਸ਼ਾ ਵਿੱਚ ਅਧਿਕਾਰਤ ਸਰੋਤ ਮੰਨਿਆ ਜਾਣਾ ਚਾਹੀਦਾ ਹੈ। ਮਹੱਤਵਪੂਰਨ ਜਾਣਕਾਰੀ ਲਈ, ਪੇਸ਼ੇਵਰ ਮਨੁੱਖੀ ਅਨੁਵਾਦ ਦੀ ਸਿਫਾਰਸ਼ ਕੀਤੀ ਜਾਂਦੀ ਹੈ। ਇਸ ਅਨੁਵਾਦ ਦੀ ਵਰਤੋਂ ਤੋਂ ਪੈਦਾ ਹੋਣ ਵਾਲੇ ਕਿਸੇ ਵੀ ਗਲਤਫਹਿਮੀ ਜਾਂ ਗਲਤ ਵਿਆਖਿਆ ਲਈ ਅਸੀਂ ਜ਼ਿੰਮੇਵਾਰ ਨਹੀਂ ਹਾਂ।