# Azure AI Foundry için Geliştirme Ortamının Kurulması

> Bu kılavuz, bu derste Java AI uygulamaları için **Azure AI Foundry** modellerini **anahtarsız** kimlik doğrulama (Microsoft Entra ID) kullanarak kurar — yönetilecek API anahtarı yoktur. Araçlara yeni misiniz? [Geliştirme ortamı kılavuzuna](./README.md) başlayın.

Bu kılavuz, bu derste Java AI uygulamaları için **Azure AI Foundry** modellerini kurar. İki yolunuz var:

- **Seçenek A — `azd` + Bicep ile sağlama (önerilen):** tek bir komut, Foundry hesabını ve modelleri kod olarak dağıtır. Portalda tıklama gerekmez.
- **Seçenek B — Kaynakları manuel olarak oluşturun** Azure AI Foundry portalında.

Her iki yol da **anahtarsız kimlik doğrulama** (Microsoft Entra ID) kullanır — kopyalanacak veya sızdırılacak API anahtarı yoktur.

## İçindekiler

- [Neler Oluşturulur](#neler-oluşturulur)
- [Önkoşullar](#önkoşullar)
- [Seçenek A: azd + Bicep ile Sağlama (Önerilen)](#option-a-provision-with-azd--bicep-recommended)
- [Seçenek B: Kaynakları Manuel Oluşturma](#seçenek-b-kaynakları-manuel-oluşturma)
- [Ortamınızı Yapılandırma](#ortamınızı-yapılandırın)
- [Kurulumunuzu Test Edin](#kurulumunuzu-test-edin)
- [Sonra Ne Gelir?](#sonra-ne-gelir)
- [Kaynaklar](#kaynaklar)
- [Ek Kaynaklar](#ek-kaynaklar)

## Neler Oluşturulur

[`infra/`](../../../02-SetupDevEnvironment/infra) içindeki Bicep şablonları şunları sağlar:

- Bir **Azure AI Foundry** hesabı (`Microsoft.CognitiveServices/accounts`, tür `AIServices`) ve bir proje
- Bir **chat** dağıtımı - GPT-5.6 Luna (`gpt-5.6-luna`), sürüm `2026-07-09`, `GlobalStandard` kapasitesi `10` (bu model için dakikada 10 istek ve dakikada 10.000 token)
- Bir **embedding** dağıtımı - `text-embedding-3-small`, sürüm `1` (ilerleyen bölümlerde kullanılır)
- Anahtar yönetmeden sizi `az login` ile oturum açmanızı sağlayan **anahtarsız rol ataması** (`Cognitive Services OpenAI User`)

## Önkoşullar

- Bir [Azure aboneliği](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) ve [Maven 3.9+](https://maven.apache.org/download.cgi)

## Seçenek A: azd + Bicep ile Sağlama (Önerilen)

`02-SetupDevEnvironment` klasöründen:

```bash
cd 02-SetupDevEnvironment

# Oturum aç (her iki araç için)
azd auth login
az login

# Foundry hesabını + model dağıtımlarını sağla
azd up
```

`azd` sizden bir **ortam adı** isteyecektir (örneğin `genai-java`), **abonelik** ve **bölge**. Kendinize ait bir abonelik ve `gpt-5.6-luna` ile `text-embedding-3-small` modellerinin kullanılabilir olduğu bir bölge seçin, örneğin `eastus2`. Aboneliğin o bölgedeki model ve dağıtım türü için yeterli kotaya sahip olduğunu onaylayın; kullanılabilirlik ve kota aboneliğe göre değişir.

Sağlama tamamlandığında azd:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) içinde tanımlanan her şeyi dağıtır.
2. Uç noktanız ve dağıtım isimlerinizle gizli olmayan [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) dosyasını yazan bir post sağlayıcı kancasını çalıştırır.

> **İpucu:** Değişiklikleri uygulamak için istediğiniz zaman `azd up` komutunu yeniden çalıştırın. Her şeyi silmek ve maliyeti durdurmak için `azd down` komutunu çalıştırın.

Oluşturulan ayarları görmek için:

```bash
azd env get-values
```

Şimdi [Kurulumunuzu Test Edin](#kurulumunuzu-test-edin) bölümüne atlayın.

## Seçenek B: Kaynakları Manuel Oluşturma

Portalı tercih mi ediyorsunuz? Kaynakları elle oluşturun:

1. [Azure AI Foundry portalına](https://ai.azure.com/) gidin ve oturum açın.
2. **Bir proje oluşturun** (bu aynı zamanda bir AI Foundry kaynağı da oluşturur). Örneğin `GenAIJava` gibi bir isim verin.
3. Projenizde **Modeller + uç noktalar** → **Model dağıtımı yap** → **Temel modeli dağıt** seçeneğini açın.
4. **GPT-5.6 Luna**yu (model ve dağıtım adı `gpt-5.6-luna`, sürüm `2026-07-09`) **Global Standard** kapasitesi `10` ile dağıtın. Embedding örneklerini istiyorsanız **text-embedding-3-small**, sürüm `1` için de tekrarlayın.
5. **Genel bakış**’tan **uç noktayı** kopyalayın (örneğin `https://<resource>.openai.azure.com/`).
6. Kendinize anahtarsız erişim yetkisi verin: kısayolda **Erişim kontrolü (IAM)** → **Rol ataması ekle** → hesabınıza **Cognitive Services OpenAI User** rolünü verin.

> **Hâlâ sorun mu yaşıyorsunuz?** [Azure AI Foundry dokümantasyonuna](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects) bakın.

## Ortamınızı Yapılandırın

**Seçenek A (`azd up`) kullandıysanız**, ayar dosyanız zaten yazıldı — yapılandırmanız gereken bir şey yoktur. [Kurulumunuzu Test Edin](#kurulumunuzu-test-edin) bölümüne atlayın.

**Seçenek B (manuel) kullandıysanız**, örneğin `.env` dosyasını kendiniz oluşturun:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` dosyasını uç noktanızla düzenleyin (anahtar yok — kimlik doğrulama anahtarsızdır):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Projenin URL’si değil, kaynağın Azure OpenAI uç noktasını kullanın. Basic-chat uygulaması onu `/openai/v1` olarak çözümler ve açık bir bearer-token istemci yapılandırır; API anahtarı gerekmez.

> **Güvenlik notu:** Saklanacak API anahtarı yoktur. Microsoft Entra ID ile `az login` (yerelde) veya bir yönetilen kimlik (Azure içinde) ile kimlik doğrulaması yaparsınız. `.env` dosyası yalnızca gizli olmayan ayarları içerir ve zaten `.gitignore` tarafından korunmaktadır.

## Kurulumunuzu Test Edin

Anahtarsız kimlik doğrulamanın bir token alabilmesi için oturum açtığınızdan emin olun, ardından örneği çalıştırın:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # zaten oturum açmadıysanız
mvn clean spring-boot:run
```

`gpt-5.6-luna` modelinden bir yanıt görmelisiniz. Varsayılan küçük kota içinde kalmak için örnekleri sırayla çalıştırın; HTTP 429 alırsanız, tekrar denemeden önce bekleme süresini bekleyin.

> **VS Code kullanıcıları:** Çalıştırmak için `F5` tuşuna basın. Uygulama `.env` dosyanızı otomatik olarak yükler.

> **Tam örnek:** Detaylar ve sorun giderme için [Azure AI Foundry ile Temel Sohbet örneğine](./examples/basic-chat-azure/README.md) bakın.

## Sonra Ne Gelir?

Sağlama yapıp örneği başarıyla çalıştırdıktan sonra şunlara sahip olacaksınız:
- `gpt-5.6-luna` ve `text-embedding-3-small` dağıtılmış Azure AI Foundry
- Anahtarsız kimlik doğrulama (Microsoft Entra ID) — yönetilecek anahtar yok
- Uç nokta ve dağıtım isimlerinizle yerel `.env` dosyası
- Hazır bir Java geliştirme ortamı

**Devam etmek için** [3. Bölüm: Çekirdek Üretken AI Teknikleri](../03-CoreGenerativeAITechniques/README.md) bölümüne giderek AI uygulamaları geliştirmeye başlayın!

## Kaynaklar

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID ile Anahtarsız Kimlik Doğrulama](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry Dokümantasyonu](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK geçişi](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 ile Resmi OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Ek Kaynaklar

- [VS Code İndir](https://code.visualstudio.com/Download)
- [Docker Desktop Al](https://www.docker.com/products/docker-desktop)
- [Geliştirici Konteyner Yapılandırması](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->