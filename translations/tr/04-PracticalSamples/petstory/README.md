# Acemi için Evcil Hayvan Hikayesi Üreticisi Eğitimi

Bir evcil hayvan fotoğrafı yükleyin, GPT-5.6 Luna ile analiz edin ve ortaya çıkan açıklamadan bir hikaye oluşturun. Her iki model isteği `reasoning_effort: none` kullanır.

| Bileşen | Sürüm |
| --- | --- |
| Java | 21 veya üstü |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## İçindekiler

- [Ön Gereksinimler](#ön-gereksinimler)
- [Proje Yapısını Anlamak](#proje-yapısını-anlamak)
- [Temel Bileşenler Açıklaması](#temel-bileşenler-açıklaması)
  - [1. Ana Uygulama](#1-ana-uygulama)
  - [2. Web Denetleyicisi](#2-web-denetleyicisi)
  - [3. Hikaye Servisi](#3-hikaye-servisi)
  - [4. Web Şablonları](#4-web-şablonları)
  - [5. Yapılandırma](#5-yapılandırma)
- [Uygulamayı Çalıştırmak](#uygulamayı-çalıştırmak)
- [Çevrimdışı Testler](#çevrimdışı-testler)
- [Bunların Birlikte Nasıl Çalıştığı](#bunların-birlikte-nasıl-çalıştığı)
- [Yapay Zeka Entegrasyonunu Anlamak](#yapay-zeka-entegrasyonunu-anlamak)
- [Sonraki Adımlar](#sonraki-adımlar)

## Ön Gereksinimler

Başlamadan önce, şunlara sahip olduğunuzdan emin olun:
- Java 21 veya üstü kurulu
- Bağımlılık yönetimi için Maven
- `gpt-5.6-luna` adlı bir Azure AI Foundry GPT-5.6 Luna dağıtımı veya o dağıtıma işaret eden bir `AZURE_OPENAI_DEPLOYMENT` üst yazısı. Anahtarsız kimlik doğrulaması için `az login` ile oturum açma ve ayrıntılar için [Bölüm 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) 'ye bakın. Dağıtım resim girişini ve `reasoning_effort: none`'i desteklemelidir.
- Java, Spring Boot ve web geliştirme hakkında temel anlayış

## Proje Yapısını Anlamak

Evcil hayvan hikayesi projesinin birkaç önemli dosyası vardır:

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

## Temel Bileşenler Açıklaması

### 1. Ana Uygulama

**Dosya:** `PetStoryApplication.java`

Bu, Spring Boot uygulamamızın giriş noktasıdır:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Yaptığı şey:**
- `@SpringBootApplication` açıklaması otomatik yapılandırma ve bileşen taramayı etkinleştirir
- 8080 portunda gömülü bir web sunucusu (Tomcat) başlatır
- Gerekli tüm Spring beanleri ve servislerini otomatik olarak oluşturur

### 2. Web Denetleyicisi

**Dosya:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Uç Nokta | İstek | Başarılı yanıt |
| --- | --- | --- |
| `GET /` | Gövde yok | CSRF belirteci içeren HTML yükleme formu |
| `POST /analyze-image` | `multipart/form-data`, dosya alanı `image` | JSON: `{"description":"Oynak bir evcil hayvan..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, alan `description` | Açıklama ve oluşturulan hikaye içeren HTML sonuç sayfası |

Her iki POST uç noktası da `GET /`'den alınan oturum çerezi ve CSRF belirteci gerektirir. Yükleme betiği gizli `_csrf` değerini `X-CSRF-TOKEN` başlığında gönderir; hikaye gönderimi `_csrf` form alanı olarak gönderir. API istemcileri çerezi istekler arasında korumalıdır. Bunlar form uç noktalarıdır, JSON istek uç noktaları değildir.

Açıklamalar boş olmamalı ve 1000 karakteri aşmamalıdır. Denetleyici açıklamayı kırpar ve `<`, `>`, çift tırnak, apostrof ve `&` karakterlerini servis katmanına iletmeden önce temizler. Sonuç şablonu model çıktısını `th:text` ile kaçırır.

Resim doğrulama hataları HTTP 400 ile `error` alanı döner; model hataları HTTP 502 ile `error` alanı ve açıklama olmadan döner. Geçersiz hikaye açıklamaları veya model hataları `/` sayfasına görünür bir hata ile yönlendirilir. Gerekli alanlar eksikse HTTP 400, CSRF belirteci eksik veya geçersizse HTTP 403 döner. Başarılı AI sonuçları olarak varsayılan açıklama veya hikaye sunulmaz.

### 3. Hikaye Servisi

**Dosya:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Resmi OpenAI Java SDK 4.63.1, Azure AI Foundry'nin OpenAI uyumlu Chat Completions API çağrılarını yapar. Azure Identity 1.18.6 `DefaultAzureCredential` aracılığıyla Microsoft Entra taşıyıcı jetonu sağlar; API anahtarı gerekmez.

| Operasyon | Girdi | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Yüklenen MIME türü ile base64 veri URL'si olarak kodlanmış resim baytları | 300 |
| `generateStory` | Kullanıcı mesajında evcil hayvan açıklaması | 800 |

Her iki istek de yapılandırılmış dağıtımı kullanır, varsayılan `gpt-5.6-luna` olup, açık şekilde `ReasoningEffort.NONE` (`reasoning_effort: none`) ayarlanır. Hiçbiri `temperature` veya eski `max_tokens` parametresini göndermez.

Görüntü analizi JPEG, PNG, GIF ve WebP kabul eder, boş resimleri ve 10MB üstü dosyaları reddeder ve açıklamayı 1000 karakterle sınırlar. Hikaye istemi aile dostu kısa hikaye talebinde bulunur. Boş seçimler veya boş model içeriği hatadır; hatalar sunucu tarafı tanılama için sebebi saklar. SDK istemcisi uygulama kapandığında kapatılır.

### 4. Web Şablonları

**Dosya:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Yükleme Formu)

Sayfa, bir açıklama metin alanı yerine fotoğraf seçici ile başlar. **Analyze Image** seçilen fotoğrafı önizler ve `/analyze-image` adresine gönderir. Başarılı yanıt açıklamayı gösterir, gizli `description` alanını doldurur ve **Generate Story** butonunu görünür yapar. O buton mevcut formu `/generate-story` adresine gönderir.

Tarayıcıda model indirme veya CDN bağımlılığı yoktur. Resim analizi sunucuda yapılandırılmış Azure dağıtımıyla çalışır. Hatalar görünür kalır ve gerçek olmayan bir açıklama ile hikaye oluşturmayı etkinleştirmez. Farklı dosya seçimi önceki analizi temizler.

**Dosya:** `result.html` (Hikaye Görüntüleme)

Oluşturulan hikayeyi gösterir:

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

**Şablon özellikleri:**

1. **Thymeleaf Entegrasyonu**: Dinamik içerik için `th:` öznitelikleri kullanır
2. **Duyarlı Tasarım**: Mobil ve masaüstü için CSS stil ayarları
3. **Hata İşleme**: Doğrulama hatalarını kullanıcıya gösterir
4. **Yükleme İşleme**: JavaScript fotoğrafı önizler, CSRF korumalı çok parçalı istek gönderir ve dönen açıklamayı gösterir

### 5. Yapılandırma

**Dosya:** `application.properties`

Uygulama için yapılandırma ayarları:

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

**Yapılandırma açıklaması:**

1. **Dosya Yükleme**: Hem dosya hem de tüm çok parçalı istek 10MB ile sınırlandırılmıştır; çok parçalı başlıklar için alan bırakmak üzere fotoğrafları bu limitin altında tutun
2. **Kayıt Tutma**: Çalışma sırasında hangi bilgilerin kaydedileceğini kontrol eder
3. **Azure AI Foundry**: Kullanılacak uç noktayı ve model dağıtımını belirtir (anahtarsız kimlik doğrulama)
4. **Güvenlik**: CSRF koruması etkin kalır; model hataları sunucu tarafında kaydedilir, denetleyici ise genel model hatası mesajları gösterir

## Uygulamayı Çalıştırmak

### Adım 1: Giriş Yap ve Uç Noktanı Ayarla

Kimlik doğrulama anahtarsızdır (Microsoft Entra ID), bu yüzden API anahtarı yoktur. Giriş yapın ve Foundry uç noktanızı belirleyin:

**Windows (Komut İstemi):**
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

**Neden gerekir:**
- Azure AI Foundry, tahmin isteklerini doğrulamak için Microsoft Entra ID kullanır
- Anahtarsız kimlik doğrulama, kaynak kodda veya ortamda gizli bilgi olmaması demektir
- Hesabınızın kaynaktaki **Cognitive Services OpenAI User** rolüne sahip olması gerekir

Varsayılan dağıtım adı `gpt-5.6-luna`'dır. Eğer GPT-5.6 Luna dağıtımınız farklıysa, uygulamayı başlatmadan önce aynı terminalde `AZURE_OPENAI_DEPLOYMENT`'ı ayarlayın. Hem resim analizi hem de hikaye oluşturma bu ayarı kullanır.

### Adım 2: Derle ve Çalıştır

Proje dizinine gidin:
```bash
cd 04-PracticalSamples/petstory
```

Bağımsız çalıştırılabilir JAR'ı derleyin ve tüm çevrimdışı testleri çalıştırın:
```bash
mvn clean package
```

Sunucuyu başlatın:
```bash
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde başlayacaktır.

Alternatif olarak, ücretsiz bir portta paketlenmiş JAR'ı başlatın, örneğin:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Bu komut için `http://localhost:8083/` adresini açın. Aynı `/analyze-image` ve `/generate-story` yolları seçilen portta da kullanılabilir.

### Adım 3: Uygulamayı Test Et

1. Tarayıcınızda `http://localhost:8080` adresini **açın**
2. JPEG, PNG, GIF veya WebP formatında, 10MB altı net bir evcil hayvan fotoğrafı **seçin**
3. **"Analyze Image"** butonuna tıklayın ve evcil hayvan açıklaması gelene kadar bekleyin
4. Başarılı analizden sonra **"Generate Story"** butonuna tıklayın
5. Hikayeyi görüntüleyin ve sonuç sayfasındaki linkle yükleme formuna geri dönün

Başarılı fotoğraftan hikayeye akış, buton başına bir model çağrısı yapar. Canlı çıkarım dağıtımınızın kotasını tüketir ve ücretlendirme getirebilir; oran sınırlı dağıtımı paylaşıyorsanız testleri ardışık çalıştırın. Ana sayfa yüklenmesi model çağrısı yapmaz.

## Çevrimdışı Testler

Örnek dizinden şu komutu çalıştırın:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) gerçek OpenAI SDK isteklerini döngüsel HTTP sabitleyiciyle yakalar. Her iki isteğin dağıtımı, `reasoning_effort: none`, token limitleri, görüntü yükü, giriş doğrulaması, boş yanıtlar ve yukarı akış hataları kontrol edilir.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) Thymeleaf sayfalarını render eden, yükleme sözleşmesini, CSRF'yi, doğrulamayı, çıktı kaçırmayı ve görünür hataları test eden sahte model servisi ile MockMvc kullanır. Bu testler Azure kimlik bilgilerine ihtiyaç duymaz ve ücretli Azure çıkarımını asla çağırmaz. Maven raporları `target/surefire-reports` altında yazar.

## Bunların Birlikte Nasıl Çalıştığı

İşte bir evcil hayvan hikayesi oluşturduğunuzda tam akış:

1. **Fotoğraf Seçimi**: Yükleme formunda bir evcil hayvan resmi seçersiniz
2. **Resim Yükleme**: "Analyze Image", CSRF başlığıyla `/analyze-image` adresine çok parçalı POST gönderir
3. **Resim Analizi**: `StoryService` resmi `none` olarak ayarlanmış mantık ile GPT-5.6 Luna'ya gönderir
4. **Açıklama Gösterimi**: Tarayıcı gelen açıklamayı gösterir ve formda saklar
5. **Hikaye Gönderimi**: "Generate Story" `description` ve `_csrf` ile `/generate-story` adresine gönderir
6. **Hikaye Oluşturma**: Denetleyici açıklamayı doğrular ve aynı dağıtımı `none` mantığı ile çağırır
7. **Şablon İşleme**: Thymeleaf sonucu sayfada açıklamayı ve hikayeyi kaçırarak gösterir

**Hata İşleme Akışı:**
Model başarısız olursa sunucu nedeni kaydeder. Resim analizi HTTP 502 döner ve tarayıcı "Generate Story" görünmeden hatayı gösterir. Hikaye oluşturma formu hata mesajıyla yönlendirir. Hiçbir yol önceden yazılmış sonucu sessizce ikame etmez.

## Yapay Zeka Entegrasyonunu Anlamak

### Azure AI Foundry (anahtarsız)
Hizmet SDK'yı kaynak `/openai/v1/` uç noktanızla yapılandırır. `DefaultAzureCredential` ve `AuthenticationUtil.getBearerTokenSupplier` Microsoft Entra jetonlarını `https://ai.azure.com/.default` için sağlar. Yerel geliştirme Azure CLI oturum açmanızı kullanabilir; Azure barındırmalı uygulama gerekli kaynak izinleri ile yönetilen kimlik kullanabilir.

### İstem Mühendisliği
Görüntü analizi kısa paragrafta gözlemlenebilir evcil hayvan özelliklerini ister ve modele görüntüdeki metni talimat değil veri olarak işlemesini söyler. Hikaye oluşturma dönen açıklamayı ayrı, aile dostu yazma isteğinde kullanır. Hiçbir çağrıda mantık etkin değildir veya sıcaklık değişikliği yapılmaz.

### Yanıt İşleme
Paylaşılan yanıt işleyici, eksik seçenekleri ve boş ya da sadece boşluk içeren içeriği reddeder, geçerli içeriği kırpar ve yukarı akış hatalarını korur. Görüntü açıklamaları sonraki hikaye formuna uyması için 1000 karakterle sınırlandırılır. Orijinal model hatası tanılama için saklanır, kullanıcıya gösterilmez.

## Sonraki Adımlar

Daha fazla örnek için bakınız: [Bölüm 04: Pratik örnekler](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->