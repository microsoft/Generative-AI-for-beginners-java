# Başlangıç Seviyesi MCP Hesaplayıcı Eğitimi

## İçindekiler

- [Öğrenecekleriniz](#öğrenecekleriniz)
- [Ön Koşullar](#ön-koşullar)
- [Bağımlılık Sürümleri](#bağımlılık-sürümleri)
- [Proje Yapısını Anlama](#proje-yapısını-anlama)
- [Temel Bileşenlerin Açıklaması](#temel-bileşenlerin-açıklaması)
  - [1. Ana Uygulama](#1-ana-uygulama)
  - [2. Hesaplayıcı Servisi](#2-hesaplayıcı-servisi)
  - [3. Doğrudan MCP İstemcisi](#3-doğrudan-mcp-i̇stemcisi)
  - [4. Yapay Zekâ Destekli İstemci](#4-yapay-zekâ-destekli-i̇stemci)
- [Örneklerin Çalıştırılması](#örneklerin-çalıştırılması)
- [Çevrimdışı Testler](#çevrimdışı-testler)
- [Hepsinin Birlikte Nasıl Çalıştığı](#tümünün-birlikte-nasıl-çalıştığı)
- [Sonraki Adımlar](#sonraki-adımlar)

## Öğrenecekleriniz

Bu eğitim, Model Context Protocol (MCP) kullanarak bir hesaplayıcı servisi nasıl oluşturulacağını açıklar. Şunları anlayacaksınız:

- Yapay zekânın bir araç olarak kullanabileceği bir servis nasıl oluşturulur
- MCP servisleri ile doğrudan iletişim nasıl kurulur
- Yapay zeka modellerinin hangi araçları otomatik olarak seçeceği
- Doğrudan protokol çağrıları ile yapay zekâ destekli etkileşimler arasındaki fark

## Ön Koşullar

Başlamadan önce, şunlara sahip olduğunuzdan emin olun:
- Java 21 veya daha yenisi kurulu
- Bağımlılık yönetimi için Maven
- Java ve Spring Boot hakkında temel bilgi

Sadece yapay zekâ istemcileri Azure OpenAI dağıtımı ve kimlik doğrulanmış `DefaultAzureCredential` gerektirir,
örneğin yerel bir Azure CLI oturum açma veya Azure'da yönetilen kimlik gibi. Kimlik,
kaynaktaki Cognitive Services OpenAI Kullanıcı rolüne sahip olmalıdır. Bkz. [Bölüm 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Sunucu, doğrudan SDK istemcisi ve tüm otomatik testler için Azure hesabı veya model erişimi gerekmez.

## Bağımlılık Sürümleri

Yayın bağımlılıkları 2026-09-14 tarihinde doğrulanmıştır:

| Bağımlılık | Sürüm |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI tarafından yönetilen) | 2.0.0 |
| LangChain4j / çekirdek | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j resmi OpenAI adaptörü | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot tarafından yönetilen) | 6.0.3 |

MCP ve resmi OpenAI adaptörleri Maven Central'da yayınlanmış beta sürümlerdir, snapshot değil.
Sürümleri LangChain4j çekirdeğinden farklıdır. Snapshot veya milestone depolarına gerek yoktur.
Sadece istemci bağımlılıkları test kapsamındadır çünkü çalıştırılabilir örnekler `src/test/java` altında yer alır.

## Proje Yapısını Anlama

Hesaplayıcı projesinde birkaç önemli dosya vardır:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Temel Bileşenlerin Açıklaması

### 1. Ana Uygulama

**Dosya:** `McpServerApplication.java`

Bu, hesaplayıcı servisimizin giriş noktasıdır. Standart bir Spring Boot uygulaması olup, özel bir ekleme içerir:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Bunu yapar:**
- 8080 portunda bir Spring Boot web sunucusu başlatır
- Hesaplayıcı yöntemlerimizi MCP araçları olarak kullanılabilir kılan bir `ToolCallbackProvider` oluşturur
- `@Bean` anotasyonu, Spring'in bunu diğer bölümlerin kullanabileceği bir bileşen olarak yönetmesini belirtir

### 2. Hesaplayıcı Servisi

**Dosya:** `CalculatorService.java`

Tüm matematik işlemlerinin gerçekleştiği yerdir. Her yöntem `@Tool` ile işaretlenmiş olup MCP üzerinden erişilebilir:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Daha fazla hesap makinesi işlemi...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Önemli özellikler:**

1. **`@Tool` Anotasyonu**: MCP'ye bu yöntemin harici istemcilerce çağrılabileceğini bildirir
2. **Açık Açıklamalar**: Her aracın ne zaman kullanılacağını yapay zeka modellerinin anlamasını kolaylaştıran açıklaması vardır
3. **Tutarlı Dönüş Formatı**: Tüm işlemler "5.00 + 3.00 = 8.00" gibi insan tarafından okunabilir metin döner
4. **Hata Yönetimi**: Sıfıra bölme ve negatif karekök durumunda hata mesajları döner

**Mevcut İşlemler:**
- `add(a, b)` - İki sayıyı toplar
- `subtract(a, b)` - İkinciden birincisini çıkarır
- `multiply(a, b)` - İki sayıyı çarpar
- `divide(a, b)` - Birincisini ikincisine böler (sıfır kontrolü ile)
- `power(base, exponent)` - Tabanı üs kuvvetine yükseltir
- `squareRoot(number)` - Karekök hesaplar (negatif kontrolü ile)
- `modulus(a, b)` - Bölümden kalan değeri döner
- `absolute(number)` - Mutlak değeri döner
- `help()` - Tüm işlemler hakkında bilgi verir

### 3. Doğrudan MCP İstemcisi

Bkz. [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Bu istemci `/mcp` üzerinde `HttpClientStreamableHttpTransport` kullanır, bağlantıyı başlatır,
sunucuyu pingler ve araç-listesi sayfalandırmasını takip eder. Dokuz beklenen aracın hepsinin
varlığını kontrol eder ve yapay zeka modeli olmadan `modulus` ve `help` dahil her birini çağırır.

Mevcut istek oluşturucu şöyle görünür:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokol hataları istemciyi yanıltıcı bir başarı yerine başarısız kılar. MCP istemcisi,
keşif veya araç çağrısı başarısız olduğunda da dahil olmak üzere try-with-resources ile kapatılır.

### 4. Yapay Zekâ Destekli İstemci

Bkz. [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
ve [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` mevcut LangChain4j `ChatModel` API'sini uygular.
`StreamableHttpMcpTransport` bunu SDK istemcisi ile aynı `/mcp` uç noktasına bağlar.
`AiServices` araçları keşfeder ve araç çağrısı/sonuç sohbetini yönetir.

Varsayılan dağıtım, akıl yürütmenin açıkça devre dışı bırakıldığı **GPT-5.6 Luna** dır:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Bu varsayılanlar, araç yürütmesinden sonra takipler dahil tüm tamamlamalar için geçerlidir.
İstemci, `DefaultAzureCredential` tarafından desteklenen yenilenebilir bir `BearerTokenCredential`
ve `https://ai.azure.com/.default` kapsamı kullanır, API anahtarı olarak geçirilen tek seferlik bir jeton değil.
Kaynak URL'leri ve `/openai/v1` ile biten URL'ler her ikisi de kabul edilir.

Bot sınırlandırılmış bir konuşma geçmişi tutar, gerçek MCP sonucu ile birlikte `Tool executed: ...` yazdırır
ve eğer yanıt araçları atlar ise başarısız olur. Araç döngüleri dört tur ile sınırlıdır.
Kimlik doğrulama, model, MCP ve araç hataları yayılır; otomatik model tekrar denemeleri devre dışıdır.
MCP taşıyıcı/istemci ve resmi OpenAI istemci başarı veya başarısızlıkta kapatılır.

## Örneklerin Çalıştırılması

### Adım 1: Hesaplayıcı Sunucusunu Başlatın

Sunucu için Azure yapılandırması gerekmez. Aşağıdaki komutlar bu örneğin dizininden çalıştırılır.
Örnek, başka bir örnekle çakışmayı önlemek için **18081** portunu kullanır; varsayılan 8080 olarak kalır.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP uç noktası `http://localhost:18081/mcp` adresindedir. Sağlık ve keşif bilgileri ise
`http://localhost:18081/health` ve `http://localhost:18081/info` adreslerinde bulunur.
Akışa uygun HTTP, eski sadece SSE taşımasını değiştirir; `/sse` ve `/v1/tools` uç noktalar değildir.

### Adım 2: Doğrudan İstemci ile Test Edin

Başka bir PowerShell terminalinde:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Girdi gerekmez. Tüm dokuz araç çalıştırılır. Beklenen aritmetik sonuçlar arasında
8, 6, 42, 5, 256, 4, 2 ve 5.5 bulunur, ardından yardım metni gelir.

### Adım 3: AI İstemcisi ile Test Edin

Ön koşullarda açıklandığı gibi kimlik doğrulamasından sonra, aynı terminalde AI istemcisini yapılandırın:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

`Tool executed: add` satırını `41.80` değeriyle ve ardından modelin cevabını bekleyin.
Tek istemlik mod, giriş beklemeden çıkar. Orijinal dört istemlik demoyu çalıştırmak için:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo `add`, `squareRoot`, `help` ve zincirlenmiş `power` sonra `divide` işlemlerini çağırır.
Beklenen sayısal yanıtlar 41.8, 12 ve 64'tür. Argümanlar atlanarak da bu demo çalışır.

### Adım 4: Etkileşimli Botu Çalıştırın

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

`Multiply 6 by 7 using the calculator service` yazın, ardından `exit` veya `quit` komutunu verin.
Gerçek bir `multiply` araç sonucu olarak 42 bekleyin. Boş satırlar yoksayılır; EOF da oturumu sona erdirir.
Bu giriş noktası için etkileşimsiz hızlı bir test yapmak üzere:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Her iki AI giriş noktası da `--prompt "question"`, `--demo` ve `--interactive` seçeneklerini kabul eder.
Geçersiz seçenekler bağlantı açılmadan önce başarısız olur. Her Maven `-D...` argümanı PowerShell için tam olarak tırnaklanmıştır.
Bash kullanıyorsanız, `$env:NAME = "value"` yerine `export NAME=value` kullanın.

**Kotaya Dikkat:** AI örneklerini sırayla çalıştırın. Basit bir istem normalde iki model isteği gerektirir;
tam demo ise dokuz istek gerektirir, araç-sonuç takipleri dahil. Paylaşılan 10 RPM
dağıtımla, bir sonraki AI çalıştırması için taze bir kota penceresi bekleyin. 429 hatası otomatik yenileme olmadan görünür şekilde başarısız olur;
servis tarafından verilen retry-after talimatlarını takip edin. Gerçek istek sayıları modele bağlıdır.
Çevrimdışı testler hiçbir kota tüketmez ve canlı Luna kullanılabilirliği veya yanıt kalitesi sağlamaz.

### Yapılandırma ve Kapatma

| Ayar | Varsayılan / davranış |
| --- | --- |
| `MCP_SERVER_URL` | `/mcp` olmadan baz URL, `http://localhost:8080` |
| `-Dmcp.server.url=...` | Tüm istemciler için `MCP_SERVER_URL` değerini geçersiz kılar |
| `AZURE_OPENAI_ENDPOINT` | Yalnızca AI istemcileri için gerekir; kaynak URL'si veya `/openai/v1` URL'si |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; bir Azure dağıtım adı |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; pozitif tam sayı |
| Akıl yürütme çabası | Araç döngüsü takipleri dahil her zaman `none` |

Geçersiz kılınan dağıtımın `reasoning_effort=none` ve `max_completion_tokens` değerlerini desteklemesi gerekir.
İstemciler `.env` dosyasını otomatik okumaz. Testten sonra sunucuyu `Ctrl+C` ile durdurun.
İstemciler `System.exit` veya kapanış beklemeleri olmadan normal şekilde döner.

## Çevrimdışı Testler

```powershell
mvn -B -ntp clean verify
```

Tüm testler Azure ile bağlantısızdır: protokol paketi rastgele loopback portlarında çalışan bir Spring sunucu
ve OpenAI uyumlu bir stub başlatır ve ardından kapatır. Maven yine de bağımlılıkları indirebilir.
Kimlik bilgisi, canlı dağıtım veya önceden var olan MCP sunucusu kullanılmaz.

- Hesaplayıcı birim testleri tüm aritmetik işlemleri, ondalık sonuçları, yardımı ve alan hatalarını kapsar.
- MCP testleri başlatma, keşif, dokuz aracı çağırma, araç hataları ve sağlık/bilgi durumlarını kapsar.
- AI protokol testleri gerçek hesaplayıcı ile tam demoyu ve etkileşimli Botu çalıştırır,
  araç sonuçlarının sonraki tamamlamaya beslenmesini doğrular ve her HTTP gövdesini Luna,
  `reasoning_effort: "none"` ve kalıtımsız `max_completion_tokens` için denetler.
- Yapılandırma/girdi testleri dağıtım ve uç nokta geçersizliklerini, boş satırları, EOF, çıkış/quit,
  tek istemlik modu, geçersiz seçenekleri ve hata yayılımını kapsar. Kota testleri 429 hatasının yeniden denenmediğini kanıtlar.

## Tümünün Birlikte Nasıl Çalıştığı

AI'ye "5 + 3 nedir?" diye sorduğunuzda tam süreç şöyledir:

1. **Siz** AI'ye doğal dilde sorarsınız
2. **AI** isteğinizi analiz eder ve toplama istediğinizi anlar
3. **AI** MCP sunucusunu çağırır: `add(5.0, 3.0)`
4. **Hesaplayıcı Servisi** çalışır: `5.0 + 3.0 = 8.0`
5. **Hesaplayıcı Servisi** şunu döner: `"5.00 + 3.00 = 8.00"`
6. **AI** sonucu alır ve doğal bir yanıt formatlar
7. **Siz** şu yanıtı alırsınız: "5 ile 3'ün toplamı 8'dir"

## Sonraki Adımlar

Daha fazla örnek için bkz. [Bölüm 04: Pratik örnekler](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->