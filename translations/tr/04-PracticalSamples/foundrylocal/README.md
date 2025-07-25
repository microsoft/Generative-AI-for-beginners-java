<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:30:07+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "tr"
}
-->
# Foundry Local Spring Boot Eğitimi

## İçindekiler

- [Ön Gereksinimler](../../../../04-PracticalSamples/foundrylocal)
- [Proje Genel Bakış](../../../../04-PracticalSamples/foundrylocal)
- [Kodu Anlamak](../../../../04-PracticalSamples/foundrylocal)
  - [1. Uygulama Konfigürasyonu (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Ana Uygulama Sınıfı (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI Servis Katmanı (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Proje Bağımlılıkları (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Her Şey Nasıl Birlikte Çalışır](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local Kurulumu](../../../../04-PracticalSamples/foundrylocal)
- [Uygulamayı Çalıştırma](../../../../04-PracticalSamples/foundrylocal)
- [Beklenen Çıktı](../../../../04-PracticalSamples/foundrylocal)
- [Sonraki Adımlar](../../../../04-PracticalSamples/foundrylocal)
- [Sorun Giderme](../../../../04-PracticalSamples/foundrylocal)

## Ön Gereksinimler

Bu eğitime başlamadan önce aşağıdakilere sahip olduğunuzdan emin olun:

- Sisteminizde kurulu **Java 21 veya daha üstü**
- Projeyi derlemek için **Maven 3.6+**
- Kurulu ve çalışan bir **Foundry Local**

### **Foundry Local Kurulumu:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Proje Genel Bakış

Bu proje dört ana bileşenden oluşur:

1. **Application.java** - Spring Boot uygulamasının ana giriş noktası
2. **FoundryLocalService.java** - AI iletişimini yöneten servis katmanı
3. **application.properties** - Foundry Local bağlantısı için yapılandırma
4. **pom.xml** - Maven bağımlılıkları ve proje yapılandırması

## Kodu Anlamak

### 1. Uygulama Konfigürasyonu (application.properties)

**Dosya:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Bu ne yapar:**
- **base-url**: Foundry Local'ın çalıştığı yeri belirtir (varsayılan port 5273)
- **model**: Metin üretimi için kullanılacak AI modelinin adını belirtir

**Anahtar kavram:** Spring Boot bu özellikleri otomatik olarak yükler ve `@Value` anotasyonu kullanılarak uygulamanızda kullanılabilir hale getirir.

### 2. Ana Uygulama Sınıfı (Application.java)

**Dosya:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Bu ne yapar:**
- `@SpringBootApplication` Spring Boot otomatik yapılandırmasını etkinleştirir
- `WebApplicationType.NONE` Spring'e bunun bir komut satırı uygulaması olduğunu, bir web sunucusu olmadığını söyler
- Ana metot Spring uygulamasını başlatır

**Demo Çalıştırıcısı:**
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

**Bu ne yapar:**
- `@Bean` Spring tarafından yönetilen bir bileşen oluşturur
- `CommandLineRunner` Spring Boot başlatıldıktan sonra kod çalıştırır
- `foundryLocalService` Spring tarafından otomatik olarak enjekte edilir (bağımlılık enjeksiyonu)
- AI'ya bir test mesajı gönderir ve yanıtı görüntüler

### 3. AI Servis Katmanı (FoundryLocalService.java)

**Dosya:** `src/main/java/com/example/FoundryLocalService.java`

#### Konfigürasyon Enjeksiyonu:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Bu ne yapar:**
- `@Service` Spring'e bu sınıfın iş mantığı sağladığını söyler
- `@Value` application.properties dosyasından yapılandırma değerlerini enjekte eder
- `:default-value` sözdizimi, özellikler ayarlanmadığında yedek değerler sağlar

#### İstemci Başlatma:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Bu ne yapar:**
- `@PostConstruct` bu metodu Spring servisi oluşturduktan sonra çalıştırır
- Yerel Foundry Local örneğinize işaret eden bir OpenAI istemcisi oluşturur
- `/v1` yolu, OpenAI API uyumluluğu için gereklidir
- API anahtarı "kullanılmıyor" çünkü yerel geliştirme kimlik doğrulama gerektirmez

#### Sohbet Metodu:
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

**Bu ne yapar:**
- **ChatCompletionCreateParams**: AI isteğini yapılandırır
  - `model`: Kullanılacak AI modelini belirtir
  - `addUserMessage`: Mesajınızı konuşmaya ekler
  - `maxCompletionTokens`: Yanıtın uzunluğunu sınırlar (kaynakları korur)
  - `temperature`: Rastgeleliği kontrol eder (0.0 = deterministik, 1.0 = yaratıcı)
- **API Çağrısı**: İsteği Foundry Local'a gönderir
- **Yanıt İşleme**: AI'nın metin yanıtını güvenli bir şekilde çıkarır
- **Hata İşleme**: Hataları açıklayıcı hata mesajlarıyla sarar

### 4. Proje Bağımlılıkları (pom.xml)

**Ana Bağımlılıklar:**

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

**Bunlar ne yapar:**
- **spring-boot-starter**: Temel Spring Boot işlevselliğini sağlar
- **openai-java**: API iletişimi için resmi OpenAI Java SDK'sı
- **jackson-databind**: API çağrıları için JSON serileştirme/deserileştirme işlemlerini yönetir

## Her Şey Nasıl Birlikte Çalışır

Uygulamayı çalıştırdığınızda iş akışı şu şekildedir:

1. **Başlangıç**: Spring Boot başlar ve `application.properties` dosyasını okur
2. **Servis Oluşturma**: Spring, `FoundryLocalService` oluşturur ve yapılandırma değerlerini enjekte eder
3. **İstemci Kurulumu**: `@PostConstruct`, OpenAI istemcisini Foundry Local'a bağlanacak şekilde başlatır
4. **Demo Çalıştırma**: `CommandLineRunner` başlangıçtan sonra çalışır
5. **AI Çağrısı**: Demo, `foundryLocalService.chat()` ile bir test mesajı gönderir
6. **API İsteği**: Servis, OpenAI uyumlu isteği Foundry Local'a gönderir
7. **Yanıt İşleme**: Servis, AI'nın yanıtını çıkarır ve döndürür
8. **Görüntüleme**: Uygulama yanıtı yazdırır ve çıkar

## Foundry Local Kurulumu

Foundry Local'ı kurmak için şu adımları izleyin:

1. **Foundry Local'ı Kurun**, [Ön Gereksinimler](../../../../04-PracticalSamples/foundrylocal) bölümündeki talimatları izleyerek.
2. Kullanmak istediğiniz AI modelini indirin, örneğin `phi-3.5-mini`, şu komutla:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **application.properties** dosyasını Foundry Local ayarlarınıza uyacak şekilde yapılandırın, özellikle farklı bir port veya model kullanıyorsanız.

## Uygulamayı Çalıştırma

### Adım 1: Foundry Local'ı Başlatın
```bash
foundry model run phi-3.5-mini
```

### Adım 2: Uygulamayı Derleyin ve Çalıştırın
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Beklenen Çıktı

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

## Sonraki Adımlar

Daha fazla örnek için [Bölüm 04: Pratik örnekler](../README.md) bölümüne bakın.

## Sorun Giderme

### Yaygın Sorunlar

**"Bağlantı reddedildi" veya "Hizmet kullanılamıyor"**
- Foundry Local'ın çalıştığından emin olun: `foundry model list`
- Hizmetin 5273 portunda olduğundan emin olun: `application.properties` dosyasını kontrol edin
- Foundry Local'ı yeniden başlatmayı deneyin: `foundry model run phi-3.5-mini`

**"Model bulunamadı" hataları**
- Mevcut modelleri kontrol edin: `foundry model list`
- `application.properties` dosyasındaki model adını tam olarak eşleşecek şekilde güncelleyin
- Gerekirse modeli indirin: `foundry model run phi-3.5-mini`

**Maven derleme hataları**
- Java 21 veya daha üstü olduğundan emin olun: `java -version`
- Temizleyin ve yeniden derleyin: `mvn clean compile`
- Bağımlılık indirmeleri için internet bağlantısını kontrol edin

**Uygulama başlıyor ama çıktı yok**
- Foundry Local'ın yanıt verdiğinden emin olun: Tarayıcıda `http://localhost:5273` adresini açın
- Belirli hata mesajları için uygulama günlüklerini kontrol edin
- Modelin tamamen yüklendiğinden ve hazır olduğundan emin olun

**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Belgenin orijinal dili, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yanlış yorumlamalar için sorumluluk kabul etmiyoruz.