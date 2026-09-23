# Temel Üretici AI Teknikleri Eğitimi

## İçindekiler

- [Gereksinimler](#gereksinimler)
- [Başlarken](#başlarken)
- [Model Seçim Kılavuzu](#model-seçim-kılavuzu)
- [Eğitim 1: LLM Tamamlamalar ve Sohbet](#eğitim-1-llm-tamamlamalar-ve-sohbet)
- [Eğitim 2: Fonksiyon Çağrısı](#eğitim-2-fonksiyon-çağrısı)
- [Eğitim 3: RAG (Arama Destekli Üretim)](#eğitim-3-rag-arama-destekli-üretim)
- [Eğitim 4: Sorumlu AI](#eğitim-4-sorumlu-ai)
- [Örnekler Arasındaki Yaygın Kalıplar](#örnekler-arasındaki-yaygın-kalıplar)
- [Birim Testleri](#birim-testleri)
- [Ardışık Canlı Doğrulama](#ardışık-canlı-doğrulama)
- [Sorun Giderme](#sorun-giderme)
- [Sonraki Adımlar](#sonraki-adımlar)

## Genel Bakış

Dört bağımsız Java programı, sohbet, konuşma geçmişi, fonksiyon çağrısı, tüm belgeyi içeren arama destekli üretim (RAG) ve sorumlu AI yanıt işleme özelliklerini göstermektedir. Tüm sohbet talepleri varsayılan olarak **reasoning effort 'none' olan GPT-5.6 Luna** hedef alınır.

Bu örnekler resmi OpenAI Java SDK ile Azure OpenAI'ın v1 uç noktasını kullanır ve [Microsoft'un SDK kılavuzunu](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) takip eder. Eski `azure-ai-openai` paketi artık bağımlılık değildir. Mevcut mesaj tabanlı iş akışlarını öğretmek için Sohbet Tamamlamaları korunmuştur; diğer API seçenekleri için [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) sayfasına bakınız.

## Gereksinimler

- Java 21 veya daha güncel sürüm ve Maven 3.6.3 veya daha güncel sürüm.
- `gpt-5.6-luna` adında bir Azure OpenAI sohbet dağıtımı veya uyumlu Sohbet Tamamlamaları ayarlarıyla geçersiz kılma.
- Kaynak üzerinde **Cognitive Services OpenAI User** rolüne sahip imzalanmış bir Azure kimliği. Yerel geliştirme Azure CLI oturumu kullanır; barındırılan uygulamalar yönetilen kimlik kullanabilir.
- Kaynak kurulumu ve oturum açma talimatları için [Bölüm 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) bakınız.

[Maven yapılandırması](../../../03-CoreGenerativeAITechniques/examples/pom.xml) bu sürümleri sabitler; 2026-09-14 tarihinde kontrol edilmiştir:

| Bileşen | Sürüm | Amaç |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Resmi Azure v1 uyumlu istemci |
| `com.azure:azure-identity` | 1.18.6 | Anahtarsız kimlik doğrulama ve token yenileme |
| `net.objecthunter:exp4j` | 0.4.8 | Kod değerlendirmesi olmadan aritmetik ifade çözümleme |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Çevrimdışı Jupiter birim testleri |
| Maven Derleyici / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 derlemesi, testler, çalıştırılabilir örnekler |

Derleyici `--release 21` kullanır. Bu bağımsız örnekler için Spring Boot, Spring AI veya LangChain4j bağımlılığı gerekmez.

## Başlarken

Depo kökünden, kabuğunuzda kaynak uç noktasını ve isteğe bağlı dağıtım geçersiz kılmayı ayarlayın.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Testler Azure kimlik bilgisi veya uç noktası gerektirmez. Maven çevresel dosyayı otomatik okumaz; değişkenleri canlı örnekleri başlatan kabukta ayarlayın. IDE başlatmalarında, başlatma yapılandırmanız tarafından sağlanan ortamı doğrulayın.

## Model Seçim Kılavuzu

| Ortam değişkeni | Anlamı | Varsayılan |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure kaynak kökü veya önceden normalleştirilmiş `/openai/v1` URL'si | Canlı çalıştırmalar için gereklidir |
| `AZURE_OPENAI_DEPLOYMENT` | Sohbet dağıtım adı, model sürümü değil | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Ayrı gömme dağıtım yapılandırması, bu dört program tarafından kullanılmaz | `text-embedding-3-small` |

Boş dağıtım geçersiz kılmaları varsayılanları kullanır. Yapılandırma `/openai/v1` ekler tam olarak bir kez ve uç noktada kimlik bilgileri, sorgu dizeleri ve eski dağıtım yollarını kabul etmez.

Her sohbet talebi açıkça `reasoningEffort(ReasoningEffort.NONE)` ve `maxCompletionTokens(...)` ayarlar. Hiçbir talep `temperature`, `top_p` veya eski tamamlayıcı token seçeneğini ayarlamaz. Bu, araç seçme ve araç sonuç takibine de dahil. GPT-5.6 Sohbet Tamamlamaları işlev araçları reasoning effort `none` gerektirir; bkz. [Microsoft'un sohbet kılavuzu](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Bu bölümde akış veya gömme giriş noktası yoktur.** Okuyucu tüm belgesini alır, vektörleri değil. Gömme ile genişletirseniz, Luna yerine `text-embedding-3-small` gibi ayrı gömme dağıtımı kullanın.

## Eğitim 1: LLM Tamamlamalar ve Sohbet

Kaynak: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program basit bir Java streams açıklaması, iki tur HashMap/TreeMap sohbeti ve etkileşimli sohbet çalıştırır. İkinci tur ilk asistan yanıtını içerir; her etkileşimli tur önceki sohbeti de gönderir.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` dağıtımı ve açık reasoning ayarını sağlar. Etkileşimli sohbette boş satırlar atlanır, `exit` veya EOF ile biter ve sistem mesajı ile dokuz tamamlanmış kullanıcı/asistan turu saklanır. Tur sayısı sınırlandırması eğitseldir, kesin token bütçesi garantisi değildir.

Örnekler dizininden:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Üç başlangıç yanıtı bekleyin, sonra `You:` istemi gelir. Her boş olmayan etkileşimli soru bir istek ekler. Tamamlama limitleri sırasıyla 200, 300, 400 ve 500 tokendir.

## Eğitim 2: Fonksiyon Çağrısı

Kaynak: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK, açıklamalı `WeatherArguments` ve `CalculationArguments` kayıtlarından JSON şemaları türetir. Zorunlu araç seçimi, her örnek egzersizi modelin kendi yanıtını kabul etmek yerine araç protokolünü uygular.

1. İzinli araç, reasoning effort `none` ve 300 token tamamlama limiti ile soru gönderin.
2. `tool_calls` bitiş nedeni bekleyin, fonksiyon adını ve çağrı kimliklerini doğrulayın, yazılı JSON argümanları ayrıştırın.
3. Yerel fonksiyonu çalıştırın. Model Java veya rastgele kod çalıştırmaz.
4. Asistan araç çağrısı mesajını bir kez ekleyin, ardından her sonuç kendi `tool_call_id` ile takip edin.
5. Araç olmadan 300 tokenlık son bir istek gönderin ve tamamlanmış, boş olmayan yanıt isteyin.

`get_weather` **canlı değil**, simüle edilmiş hava durumu döndürür. Şehri dikkate alır ve istenirse örnek 22 santigradı Fahrenheit'a dönüştürür. `calculate` sağlanan ifadeyi exp4j ile değerlendirir, `15% of 240` ve `2 + 3 * 4` gibi biçimleri destekler, boş, aşırı büyük, geçersiz veya belirsiz hesaplamaları reddeder. Finansal ondalık duyarlılık değil, kayan nokta aritmetiği kullanır.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

`Function: get_weather`, simüle edilmiş Seattle havası, `Function: calculate`, `Function result: 36` ve iki son yanıt bekleyin. Stdin veya harici hava durumu kimlik bilgisi gerekmez. Başarılı bir çalıştırma tam olarak dört sohbet isteği kullanır.

## Eğitim 3: RAG (Arama Destekli Üretim)

Kaynak: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Girdi: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Bu giriş düzeyi RAG örneği, bir UTF-8 belgenin tamamını getirir ve soruyla birlikte kullanıcı mesajına ekler. Ayrı bir sistem mesajı, modelin belge içeriğini güvenilmeyen veri olarak kabul etmesini ve sadece bu bağlamdan yanıt vermesini sağlar. Eğer belge yanıtı içermiyorsa, istenen yanıt: `I cannot find that information in the provided document.`

Temellendirme halüsinasyonları azaltabilir, ancak sınırlayıcılar veya sistem talimatları doğruluğu garanti etmez veya her prompt enjeksiyonunu önleyemez. Canlı yanıtları gözden geçirin. Üretim ortamında RAG genellikle parçalamas, arama, kaynak gösterme, erişim kontrolü ve değerlendirme ekler.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Bir soru girin, örneğin `Document ne tür bir kimlik doğrulama yöntemini anlatıyor?`. Microsoft Entra ID'den bahseden bir yanıt bekleyin. Program, 500 token tamamlama limiti ile bir sohbet isteği sonrası çıkar.

Varsayılan dosya araması, depo kökü, bölüm dizini veya örnekler dizininden çalışır. Açık bir yol da desteklenir:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Girdiler boş olmayan olmalıdır: en fazla 32 KiB UTF-8 belge verisi ve 2.000 karakter soru. Eksik dosyalar, boş/EOF soruları ve aşırı büyük girdiler çıkarma öncesi başarısız olur.

## Eğitim 4: Sorumlu AI

Kaynak: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Altı test; zararlı talimatlar, nefret söylemi, gizlilik, tıbbi yanlış bilgi, yasadışı içerik ve zararsız sorumlu AI sorusu içerir. Program, yanıtı gözlemler, her testin filtre tetiklemesi gerektiğini varsaymaz.

| Sonuç | Kanıt |
| --- | --- |
| `FILTERED` | Açık `content_filter` / `ResponsibleAIPolicyViolation` hata kodu veya bir tamamlamada `content_filter` bitiş nedeni |
| `REFUSED` | Boş olmayan yapılandırılmış `message.refusal` alanı |
| `POSSIBLE_REFUSAL` | Normal metinde açılış reddi ifadesi; gözden geçirme gerektiren bir kestirim |
| `GENERATED` | Tamamlanmış boş olmayan yanıt; içeriğin güvenli olduğunun kanıtı değil |

Normal bir HTTP 400 hatası filtreleme kanıtı **değildir**. Geçersiz parametreler, kimlik doğrulama hataları, oran limitleri, sunucu hataları, biçimsiz yanıtlar ve kesik çıktı başarısızlıkla sonuçlanır, yanlış başarılı güvenlik olarak değil. Zararsız bir açıklamadaki "zararlı içerik" gibi geniş kelimeler reddetme sayılmaz.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Altı kategori sonucu ve gözlemlerin güvenlik sertifikası olmadığını bildiren bir özet bekleyin. Her test için 300 token tamamlama limiti vardır. Beklenmeyen üretimleri ve olası reddetmeleri manuel kontrol edin; zararsız karşılaştırma, önemli bir sorumlu AI açıklaması üretmelidir. Stdin gerekmez.

## Örnekler Arasındaki Yaygın Kalıplar

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) uç nokta normalizasyonu, dağıtım geçersiz kılmaları, anahtarsız kimlik doğrulama ve sohbet seçeneklerini merkezileştirir:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Token tedarikçisi gerektiğinde erişim tokenlarını yeniler. Tokenları kaydetmeyin veya bunu bir API anahtarı ile değiştirmeyin. Her program kendi istemcisini yeniden kullanır ve `finally` bloğunda veya kendi `AutoCloseable` sarmalayıcısıyla kapatır; SDK'nın `OpenAIClient` kendisi `AutoCloseable` değildir.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) tamamlanmış, boş olmayan metinsel bir yanıt gerektirir. Boş seçimler, reddetmeler, filtreler ve eksik yanıtlar sessizce başarı olarak yazdırılmaz. Sorumlu AI örneği beklenen filtre/reddetme sonuçlarını açıkça işler. İşlenmemiş hatalar Java/Maven işleminin sıfır olmayan çıkış kodu vermesine neden olur.

**Otomatik SDK yeniden denemeleri devre dışı bırakılmıştır**; bu, paylaşılan düşük RPM dağıtımlarında istek sayısını tahmin edilebilir tutar. Her çıkarım isteği için 60 saniyelik zaman aşımı vardır. Token edinimi daha fazla zaman alabilir. Uygulama düzeyi zamanlama kotalara saygı göstermelidir; başarısız ödemeli isteği körü körüne yeniden çalıştırmayın.

## Birim Testleri

Örnekler dizininden:

```powershell
mvn -B -ntp clean test
```

Test taşıması SDK HTTP katmanını tamamen değiştirir, gerçek serileştirilmiş istek gövdelerini yakalar ve sıraya alınmış yanıtlar sağlar. Soket açmaz, Azure tokenları almaz ve beklenmeyen isteklerde başarısız olur. Bu testler uygulama davranışını ve SDK protokolünü doğrular; canlı model kalitesi veya dağıtım erişilebilirliğini değil.

| Test paketi | Kapsam |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Uç nokta normalizasyonu/red, dağıtım geçersiz kılmaları, reasoning ve token seçenekleri |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Her tamamlayıcı iş akışı, mesaj geçmişi, tam tur kırpma, EOF, hatalar |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Araç şemaları, yazılı argümanlar, aritmetik, kimlikler, birden çok araç sonucu, başarısız takipler |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Dosya arama, UTF-8, boyut sınırları, temel yük, girdi ve API hataları |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Altı test, açık filtreler, red sınıflandırması, sıradan 400 ve diğer hatalar |

Bir test paketi için `mvn -B -ntp test "-Dtest=FunctionsAppTest"` kullanın. Paylaşılan araçlar [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) dosyasında bulunur.

## Ardışık Canlı Doğrulama

Canlı çağrılar birim testlerinden ayrı yapılır. İzinler ve dağıtım erişimi hazır olduktan sonra, depo kökünden **bireysel olarak** aşağıdaki komutları kullanın. Hizmetler veya kalıcı süreçler gerekli değildir.

Paylaşılan **10 istek/dakika** dağıtımı için, sıradaki programın tamamı için yeterli kota ayırın: 5, 4, 1, sonra 6 istek. Ardışık süreçler tek başına oran sınırına uyumu garanti etmez. Dakikayı diğer tüm çağıranlarla koordine edin; dört çağrıyı zamansız bir toplu olarak yapıştırmayın.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Tamamlamalar, çok turlu ve iki etkileşimli tur:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Üç bölüm başlığının tamamını, beş cevabı, Ada'yı hatırlatan son etkileşimli cevabı, `Hoşça kal!` ifadesini ve çıkış kodu 0'ı kontrol edin. Bütçe: **5 istek, en fazla 1.900 tamamlayıcı token**. Daha küçük bir çalışma için yalnızca `exit` kullanabilirsiniz: 3 istek / 900 token, ancak bu etkileşimli çıkarımı kullanmaz.

**2. Her iki fonksiyon çağırma iş akışı:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Her iki fonksiyon adını, simüle edilmiş Seattle havasını, hesaplanan 36 sonucunu, iki son cevabı ve çıkış kodu 0'ı kontrol edin. Bütçe: **4 istek, en fazla 1.200 tamamlayıcı token**.

**3. Belgeye dayalı cevap:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Belge yolunu, Microsoft Entra ID'den bahseden bir cevabı ve çıkış kodu 0'ı kontrol edin. Bütçe: **1 istek, en fazla 500 tamamlayıcı token**. Mevcut [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) tek gereken giriş dosyasıdır. Mevcut olmayan bir konu hakkında istekte bulunan isteğe bağlı ikinci çalışma kaçınmalı ve 1 istek / 500 token ekler.

**4. Sorumlu YZ gözlemleri:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Altı kategoriyi ve gözlemsel özeti kontrol edin, oluşturulan içeriği inceleyin ve teknik tamamlanma için çıkış kodu 0 talep edin. Başarılı bir süreç çıkışı model güvenliğini garanti etmez. Bütçe: **6 istek, en fazla 1.800 tamamlayıcı token**.

**Dört komutun toplamı: 16 sohbet isteği ve en fazla 5.400 tamamlayıcı token**, ayrıca giriş tokenları (tekrarlanan konuşmalar ve araç şeması/geçmiş dahil). Sıfır gömme isteği var. Gerçek token kullanımı modele bağlıdır ve özellikle filtrelenmiş istemlerde daha düşük olabilir. Dolar maliyeti dağıtım fiyatlandırmasına bağlıdır; sabit bir parasal tahmin ima edilmez. Tüm istek limitleri elle tekrar çalıştırma yapılmaması varsayımıyla. Her komuttan hemen sonra `$LASTEXITCODE` değerini kontrol edin; sıfır olmayan değer çalışma işleminin başarıyla tamamlanmadığını gösterir.

## Sorun Giderme

- **Uç nokta eksik / 401 / 403:** Başlatma sürecinde uç noktayı ayarlayın, yerel Azure oturum açmanızı ve kaynak kapsamlı rolünüzü doğrulayın, istenmeyen kimlik ortamı geçersiz kılmalarını kontrol edin.
- **400 / 404:** Dağıtımın var olduğundan ve Chat Tamamlamalarını desteklediğinden emin olun, gerekirse mantıksal çaba `none` ile. HTTPS kaynak kökü veya `/openai/v1` URL'sini kullanın, eski bir dağıtım URL'si değil. Normal 400 hataları teknik başarısızlıklardır, güvenlik engeli değildir.
- **429:** Paylaşılan RPM ve token kotasını koordine edin, sonra tekrar deneyin. Örnekler kasıtlı olarak otomatik tekrar denemiyor.
- **`Eksik sohbet yanıtı: uzunluk`:** Çıktı tamamlanma sınırına ulaştı. Yanıtı ve istemi inceleyin, sınırı ve belgelenmiş bütçesini artırmadan önce; kesik bir çalışmayı başarılı olarak kaydetmeyin.
- **Dosya veya stdin hataları:** Desteklenen bir dizinden başlatın veya açıkça bir belge yolu verin. Boş olmayan bir okuyucu sorusu sağlayın. Tamamlamalar normalde EOF veya `exit` ile sonlanabilir.
- **Derleme hataları:** Java 21 veya daha sonrası kurulu olduğundan emin olun, sonra `mvn -B -ntp clean test` çalıştırın. PowerShell'de noktalı özellik içeren tüm Maven argümanını tırnak içine alın, örneğin `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Sonraki Adımlar

[Bölüm 4: Pratik Örnekler](../04-PracticalSamples/README.md) kısmına devam edin.

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->