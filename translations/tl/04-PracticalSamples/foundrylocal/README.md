<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:59:04+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "tl"
}
-->
# Foundry Local Spring Boot Tutorial

## Talaan ng Nilalaman

- [Mga Kinakailangan](../../../../04-PracticalSamples/foundrylocal)
- [Pangkalahatang-ideya ng Proyekto](../../../../04-PracticalSamples/foundrylocal)
- [Pag-unawa sa Code](../../../../04-PracticalSamples/foundrylocal)
  - [1. Konfigurasyon ng Aplikasyon (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Pangunahing Klase ng Aplikasyon (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI Service Layer (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Mga Dependency ng Proyekto (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Paano Nagtutulungan ang Lahat](../../../../04-PracticalSamples/foundrylocal)
- [Pag-set Up ng Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Pagpapatakbo ng Aplikasyon](../../../../04-PracticalSamples/foundrylocal)
- [Inaasahang Output](../../../../04-PracticalSamples/foundrylocal)
- [Susunod na Hakbang](../../../../04-PracticalSamples/foundrylocal)
- [Pag-aayos ng Problema](../../../../04-PracticalSamples/foundrylocal)

## Mga Kinakailangan

Bago simulan ang tutorial na ito, tiyaking mayroon ka ng:

- **Java 21 o mas mataas** na naka-install sa iyong sistema
- **Maven 3.6+** para sa pagbuo ng proyekto
- **Foundry Local** na naka-install at tumatakbo

### **I-install ang Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Pangkalahatang-ideya ng Proyekto

Ang proyektong ito ay binubuo ng apat na pangunahing bahagi:

1. **Application.java** - Ang pangunahing entry point ng Spring Boot application
2. **FoundryLocalService.java** - Service layer na humahawak sa komunikasyon ng AI
3. **application.properties** - Konfigurasyon para sa koneksyon sa Foundry Local
4. **pom.xml** - Mga dependency ng Maven at konfigurasyon ng proyekto

## Pag-unawa sa Code

### 1. Konfigurasyon ng Aplikasyon (application.properties)

**File:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Ano ang ginagawa nito:**
- **base-url**: Tinutukoy kung saan tumatakbo ang Foundry Local (default port 5273)
- **model**: Pinapangalanan ang AI model na gagamitin para sa text generation

**Pangunahing konsepto:** Awtomatikong ina-load ng Spring Boot ang mga properties na ito at ginagawang available sa iyong application gamit ang `@Value` annotation.

### 2. Pangunahing Klase ng Aplikasyon (Application.java)

**File:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Ano ang ginagawa nito:**
- `@SpringBootApplication` nagbibigay-daan sa auto-configuration ng Spring Boot
- `WebApplicationType.NONE` sinasabi sa Spring na ito ay isang command-line app, hindi isang web server
- Ang pangunahing method ay nagsisimula sa Spring application

**Ang Demo Runner:**
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

**Ano ang ginagawa nito:**
- `@Bean` lumilikha ng component na pinamamahalaan ng Spring
- `CommandLineRunner` nagpapatakbo ng code pagkatapos magsimula ang Spring Boot
- `foundryLocalService` awtomatikong ini-inject ng Spring (dependency injection)
- Nagpapadala ng test message sa AI at ipinapakita ang sagot

### 3. AI Service Layer (FoundryLocalService.java)

**File:** `src/main/java/com/example/FoundryLocalService.java`

#### Configuration Injection:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Ano ang ginagawa nito:**
- `@Service` sinasabi sa Spring na ang klase na ito ay nagbibigay ng business logic
- `@Value` ini-inject ang mga configuration values mula sa application.properties
- Ang syntax na `:default-value` nagbibigay ng fallback values kung hindi naka-set ang properties

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

**Ano ang ginagawa nito:**
- `@PostConstruct` nagpapatakbo ng method na ito pagkatapos likhain ng Spring ang service
- Lumilikha ng OpenAI client na tumutukoy sa iyong lokal na Foundry Local instance
- Ang `/v1` path ay kinakailangan para sa OpenAI API compatibility
- Ang API key ay "unused" dahil hindi kailangan ng authentication sa lokal na development

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

**Ano ang ginagawa nito:**
- **ChatCompletionCreateParams**: Kinokonfigura ang AI request
  - `model`: Tinutukoy kung aling AI model ang gagamitin
  - `addUserMessage`: Idinadagdag ang iyong mensahe sa usapan
  - `maxCompletionTokens`: Nililimitahan ang haba ng sagot (nakakatipid ng resources)
  - `temperature`: Kinokontrol ang randomness (0.0 = deterministic, 1.0 = creative)
- **API Call**: Nagpapadala ng request sa Foundry Local
- **Response Handling**: Ligtas na kinukuha ang text response ng AI
- **Error Handling**: Binabalot ang exceptions ng mga kapaki-pakinabang na error messages

### 4. Mga Dependency ng Proyekto (pom.xml)

**Mga Pangunahing Dependency:**

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

**Ano ang ginagawa ng mga ito:**
- **spring-boot-starter**: Nagbibigay ng pangunahing functionality ng Spring Boot
- **openai-java**: Opisyal na OpenAI Java SDK para sa API communication
- **jackson-databind**: Humahawak sa JSON serialization/deserialization para sa API calls

## Paano Nagtutulungan ang Lahat

Narito ang kumpletong daloy kapag pinatakbo mo ang application:

1. **Startup**: Sinisimulan ng Spring Boot at binabasa ang `application.properties`
2. **Service Creation**: Lumilikha ang Spring ng `FoundryLocalService` at ini-inject ang mga configuration values
3. **Client Setup**: Ini-initialize ng `@PostConstruct` ang OpenAI client para kumonekta sa Foundry Local
4. **Demo Execution**: Ang `CommandLineRunner` ay nagpapatakbo pagkatapos ng startup
5. **AI Call**: Ang demo ay tumatawag sa `foundryLocalService.chat()` gamit ang test message
6. **API Request**: Ang service ay bumubuo at nagpapadala ng OpenAI-compatible request sa Foundry Local
7. **Response Processing**: Kinukuha ng service ang sagot ng AI at ibinabalik ito
8. **Display**: Ipinapakita ng application ang sagot at tumitigil

## Pag-set Up ng Foundry Local

Para i-set up ang Foundry Local, sundin ang mga hakbang na ito:

1. **I-install ang Foundry Local** gamit ang mga instruksyon sa [Mga Kinakailangan](../../../../04-PracticalSamples/foundrylocal) na seksyon.
2. **I-download ang AI model** na nais mong gamitin, halimbawa, `phi-3.5-mini`, gamit ang sumusunod na command:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **I-configure ang application.properties** file upang tumugma sa iyong Foundry Local settings, lalo na kung gumagamit ka ng ibang port o model.

## Pagpapatakbo ng Aplikasyon

### Hakbang 1: Simulan ang Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Hakbang 2: I-build at Patakbuhin ang Aplikasyon
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Inaasahang Output

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

## Susunod na Hakbang

Para sa higit pang mga halimbawa, tingnan ang [Chapter 04: Practical samples](../README.md)

## Pag-aayos ng Problema

### Karaniwang Mga Isyu

**"Connection refused" o "Service unavailable"**
- Tiyaking tumatakbo ang Foundry Local: `foundry model list`
- Siguraduhing nasa port 5273 ang service: Suriin ang `application.properties`
- Subukang i-restart ang Foundry Local: `foundry model run phi-3.5-mini`

**"Model not found" errors**
- Suriin ang mga available na models: `foundry model list`
- I-update ang pangalan ng model sa `application.properties` upang eksaktong tumugma
- I-download ang model kung kinakailangan: `foundry model run phi-3.5-mini`

**Mga error sa Maven compilation**
- Siguraduhing Java 21 o mas mataas: `java -version`
- Linisin at i-rebuild: `mvn clean compile`
- Suriin ang koneksyon sa internet para sa pag-download ng mga dependency

**Nagsimula ang application pero walang output**
- Siguraduhing tumutugon ang Foundry Local: Buksan ang browser sa `http://localhost:5273`
- Suriin ang application logs para sa mga partikular na error messages
- Tiyaking ang model ay ganap na na-load at handa

**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't sinisikap naming maging tumpak, tandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa kanyang katutubong wika ang dapat ituring na opisyal na pinagmulan. Para sa mahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na maaaring magmula sa paggamit ng pagsasaling ito.