# Azure AI Foundry ile Temel Sohbet - Baştan Sona Örnek

Bu örnek, **keyless authentication** (Microsoft Entra ID) kullanarak **Azure AI Foundry** modeline bağlanan ve kurulumunuzu test eden basit bir Spring Boot uygulamasıdır. Resmi OpenAI Java SDK'sı ve **Azure OpenAI v1** uç noktası tarafından desteklenen Spring AI'nın `ChatClient`'ını kullanır.

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) içindeki sürümler Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** ve dotenv-java **3.2.0**'dır. Örnek, `spring-ai-starter-model-openai` kullanır ve `openai-java` ile `azure-identity` yapılandırmasını açıkça belirtir; Spring AI 2 eski Azure OpenAI starter'ını kaldırmıştır.

## İçindekiler

- [Önkoşullar](#önkoşullar)
- [Hızlı Başlangıç](#hızlı-başlangıç)
- [Kimlik Doğrulama Nasıl Çalışır](#kimlik-doğrulama-nasıl-çalışır)
- [Uygulamayı Çalıştırma](#uygulamayı-çalıştırma)
  - [Maven ile](#maven-ile)
  - [VS Code ile](#vs-code-ile)
  - [Beklenen Çıktı](#beklenen-çıktı)
- [Yapılandırma Referansı](#yapılandırma-referansı)
  - [Ortam Değişkenleri](#ortam-değişkenleri)
  - [Spring Konfigürasyonu](#spring-konfigürasyonu)
- [Sorun Giderme](#sorun-giderme)
  - [Yaygın Sorunlar](#yaygın-sorunlar)
  - [Hata Ayıklama Modu](#hata-ayıklama-modu)
- [Sonraki Adımlar](#sonraki-adımlar)
- [Kaynaklar](#kaynaklar)

## Önkoşullar

Bu örneği çalıştırmadan önce:

- `gpt-5.6-luna` dağıtımı içeren bir Azure AI Foundry kaynağı - `azd up` ile ya da el ile [Azure AI Foundry kurulum kılavuzu](../../getting-started-azure-openai.md) üzerinden sağlanmalı
- Kaynak üzerindeki **Cognitive Services OpenAI User** rolü (Bicep şablonları bunu sizin için atar)
- `az login` ile oturum açmış olarak [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- Java 21+ ve Maven 3.9+

> **API anahtarı gerekmez** — kimlik doğrulama Microsoft Entra ID üzerinden keyless yapılır.

## Hızlı Başlangıç

```bash
# 1. Projeye gidin
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Anahtarsız kimlik doğrulama bir token alabilmesi için giriş yapın
az login

# 3. Uç noktayı yapılandırın
#    - Eğer `azd up` komutunu çalıştırdıysanız, .env sizin için yazıldı (bunu atlayın).
#    - Aksi takdirde şablonu kopyalayıp AZURE_OPENAI_ENDPOINT ayarlayın:
cp .env.example .env

# 4. Uygulamayı çalıştırın
mvn spring-boot:run
```

## Kimlik Doğrulama Nasıl Çalışır

Bu örnekte kimlik doğrulama **Microsoft Entra ID** ile yapılır — API anahtarı yoktur.

Uygulama kimlik doğrulamayı açıkça [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) içinde yapılandırır:

1. `azureCredential()` `DefaultAzureCredential` ve `https://ai.azure.com/.default` kapsamı ile `AuthenticationUtil.getBearerTokenSupplier` kullanarak bir `BearerTokenCredential` oluşturur.
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()` ile bir `OpenAIClient` oluşturur, kaynak uç noktasını `/openai/v1` olarak çözer ve `.credential(...)` ile taşıyıcı kimlik bilgisi sağlar.
3. `azureChatModel()` bu istemciyi Spring AI'nın `OpenAiChatModel`'ine sağlar; bu da dersin `ChatClient`'ına destek verir.

Bu açık bileşenler, genel bir `OPENAI_API_KEY`'nin Azure kimlik doğrulamasını geçersiz kılmasını engeller. YAML'den yalnızca bir API anahtarı omitting kimlik doğrulama yapılandırması değildir. `DefaultAzureCredential`, yerelde `az login` oturumunuzu veya Azure'daki yönetilen bir kimliği kullanabilir; seçilen kimliğin yukarıdaki kaynak rolüne sahip olması gerekir.

## Uygulamayı Çalıştırma

### Maven ile

```bash
mvn spring-boot:run
```

### VS Code ile

1. Projeyi VS Code'da açın
2. `F5` tuşuna basın veya "Çalıştır ve Hata Ayıkla" panelini kullanın
3. "Spring Boot-BasicChatApplication" konfigürasyonunu seçin

> **Not**: Uygulama çalışma dizininden, VS Code'dan başlatılsa bile, `.env` dosyasını yükler.

### Beklenen Çıktı

Başarılı bir çalıştırmadan sonra örnek çıktı (başlangıç kayıtları atlanmıştır; yanıt ifadeleri değişebilir):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Yapılandırma Referansı

### Ortam Değişkenleri

| Değişken | Açıklama | Zorunlu | Örnek |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) uç nokta URL'si | Evet | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Sohbet modeli dağıtım adı | Hayır | `gpt-5.6-luna` (varsayılan) |

> **API anahtarı değişkeni yok** — kimlik doğrulama keyless (Microsoft Entra ID, `az login` ile).

### Spring Konfigürasyonu

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) ayarları `spring.ai.openai` önekini ve düzleştirilmiş sohbet özelliklerini kullanır (`options` bloğu yoktur):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` **Azure dağıtım adıdır**. Kimlik doğrulama yukarıda açıklanan açık bileşenlerden gelir; `api-key` ayarı kullanılmaz. Ders, muhakemeyi devre dışı bırakır ve tamamlama token sayısını 500 ile sınırlar; `temperature` ve eski `max-tokens` ayarları boş bırakılmıştır.

Microsoft, [yeni uygulamalar için resmi OpenAI SDK'yı Azure OpenAI v1 ve Responses API ile kullanmayı önerir](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Bu mevcut mesaj tabanlı ders için Chat Completions desteklenmeye devam etmektedir. GPT-5.6 için, Chat Completions üzerinde araç içeren istekler `reasoning_effort`'i `none` olarak ayarlamalıdır; araçlarla birlikte muhakeme yaparken Responses API kullanılmalıdır. Ayrıntılar için [muhakeme modelleriyle araç çağrısı](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models) adresine bakınız.

## Sorun Giderme

### Yaygın Sorunlar

<details>
<summary><strong>Hata: 401 / "PermissionDenied" / token hataları</strong></summary>

- `az login` komutunu çalıştırın — anahtarsız kimlik doğrulama için aktif oturum gereklidir
- Hesabınızın kaynak üzerinde **Cognitive Services OpenAI User** rolüne sahip olduğundan emin olun
- Rolü yeni atadıysanız, yayılması için birkaç dakika bekleyin
- Doğru kiracı/abone içinde olduğunuzu kontrol edin (`az account show`)
</details>

<details>
<summary><strong>Hata: "The endpoint is not valid" / bağlantı hataları</strong></summary>

- `AZURE_OPENAI_ENDPOINT` tam temel URL olmalıdır (örn. `https://your-resource.openai.azure.com/`)
- Sonundaki eğik çizgi tutarlılığını kontrol edin
- Uç noktanın sağlanan kaynakla eşleştiğini doğrulayın (`azd env get-values`)
</details>

<details>
<summary><strong>Hata: "The deployment was not found"</strong></summary>

- `AZURE_OPENAI_DEPLOYMENT` dağıtım adıyla uyumlu olmalı
- Modelin başarıyla dağıtıldığını ve aktif olduğunu kontrol edin
- Varsayılan dağıtım adı `gpt-5.6-luna`dır
</details>

<details>
<summary><strong>Hata: 429 / kota aşımı</strong></summary>

- Varsayılan GPT-5.6 Luna dağıtımı Global Standard kapasitesine sahiptir: dakikada 10 istek ve dakikada 10.000 token
- Örnekleri sırayla çalıştırın ve yeniden denemeden önce hizmetin yeniden deneme aralığını bekleyin
- Bu temel örnek otomatik SDK yeniden denemelerini devre dışı bırakır, bu yüzden başarısız istek doğrudan raporlanır
</details>

<details>
<summary><strong>VS Code: Ortam değişkenleri yüklenmiyor</strong></summary>

- `.env` dosyanızın proje ana dizininde olduğundan emin olun (`pom.xml` ile aynı seviye)
- VS Code'un entegre terminalinde `mvn spring-boot:run` komutunu çalıştırmayı deneyin
- VS Code Java uzantısının düzgün kurulmuş olduğunu kontrol edin
</details>

### Hata Ayıklama Modu

Daha ayrıntılı günlükleme için [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) içindeki bu satırların yorumunu kaldırın:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Sonraki Adımlar

**Kurulum Tamamlandı!** Öğrenme yolculuğunuza devam edin:

[Bölüm 3: Temel Üretken Yapay Zeka Teknikleri](../../../03-CoreGenerativeAITechniques/README.md)

## Kaynaklar

- [Spring AI 2 OpenAI Java SDK geçişi](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 ile resmi OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID ile anahtarsız kimlik doğrulama](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Portalı](https://ai.azure.com/)
- [Azure AI Foundry Belgeleri](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->