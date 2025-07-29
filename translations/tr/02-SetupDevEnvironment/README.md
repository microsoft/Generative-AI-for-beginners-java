<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:23:18+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "tr"
}
-->
# Java için Üretken Yapay Zeka Geliştirme Ortamını Kurma

> **Hızlı Başlangıç**: Bulutta 2 dakikada kod yazmaya başlayın - [GitHub Codespaces Kurulumu](../../../02-SetupDevEnvironment) bölümüne atlayın - yerel kurulum gerekmez ve GitHub modellerini kullanır!

> **Azure OpenAI ile ilgileniyor musunuz?** [Azure OpenAI Kurulum Kılavuzumuza](getting-started-azure-openai.md) göz atın ve yeni bir Azure OpenAI kaynağı oluşturma adımlarını öğrenin.

## Öğrenecekleriniz

- Yapay zeka uygulamaları için bir Java geliştirme ortamı kurmayı öğrenin
- Tercih ettiğiniz geliştirme ortamını seçin ve yapılandırın (Codespaces ile bulut öncelikli, yerel geliştirme konteyneri veya tam yerel kurulum)
- GitHub Modellerine bağlanarak kurulumunuzu test edin

## İçindekiler

- [Öğrenecekleriniz](../../../02-SetupDevEnvironment)
- [Giriş](../../../02-SetupDevEnvironment)
- [Adım 1: Geliştirme Ortamınızı Kurun](../../../02-SetupDevEnvironment)
  - [Seçenek A: GitHub Codespaces (Önerilen)](../../../02-SetupDevEnvironment)
  - [Seçenek B: Yerel Geliştirme Konteyneri](../../../02-SetupDevEnvironment)
  - [Seçenek C: Mevcut Yerel Kurulumunuzu Kullanın](../../../02-SetupDevEnvironment)
- [Adım 2: GitHub Kişisel Erişim Jetonu Oluşturun](../../../02-SetupDevEnvironment)
- [Adım 3: Kurulumunuzu Test Edin](../../../02-SetupDevEnvironment)
- [Sorun Giderme](../../../02-SetupDevEnvironment)
- [Özet](../../../02-SetupDevEnvironment)
- [Sonraki Adımlar](../../../02-SetupDevEnvironment)

## Giriş

Bu bölüm, geliştirme ortamınızı kurmanıza rehberlik edecek. **GitHub Modellerini** ana örneğimiz olarak kullanacağız çünkü sadece bir GitHub hesabı ile ücretsizdir, kolayca kurulabilir, kredi kartı gerektirmez ve deney yapmak için birden fazla modele erişim sağlar.

**Yerel kurulum gerekmez!** GitHub Codespaces kullanarak tarayıcınızda tam bir geliştirme ortamı ile hemen kod yazmaya başlayabilirsiniz.

<img src="./images/models.webp" alt="Ekran Görüntüsü: GitHub Modelleri" width="50%">

Bu kurs için [**GitHub Modellerini**](https://github.com/marketplace?type=models) kullanmanızı öneriyoruz çünkü:
- **Ücretsiz** başlamak için
- **Kolay** bir şekilde sadece bir GitHub hesabı ile kurulabilir
- **Kredi kartı** gerektirmez
- **Birden fazla model** deney yapmak için kullanılabilir

> **Not**: Bu eğitimde kullanılan GitHub Modellerinin ücretsiz limitleri şunlardır:
> - Dakikada 15 istek (günde 150 istek)
> - İstek başına ~8.000 kelime giriş, ~4.000 kelime çıkış
> - 5 eşzamanlı istek
> 
> Üretim kullanımı için, Azure hesabınızla Azure AI Foundry Modellerine yükseltin. Kodunuzu değiştirmenize gerek yok. [Azure AI Foundry belgelerine](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) göz atın.

## Adım 1: Geliştirme Ortamınızı Kurun

<a name="quick-start-cloud"></a>

Bu Java için Üretken Yapay Zeka kursu için gerekli tüm araçları içeren önceden yapılandırılmış bir geliştirme konteyneri oluşturduk. Tercih ettiğiniz geliştirme yaklaşımını seçin:

### Ortam Kurulum Seçenekleri:

#### Seçenek A: GitHub Codespaces (Önerilen)

**Yerel kurulum gerekmeden 2 dakikada kod yazmaya başlayın!**

1. Bu depoyu GitHub hesabınıza çatallayın
   > **Not**: Temel yapılandırmayı düzenlemek isterseniz [Geliştirme Konteyneri Yapılandırması](../../../.devcontainer/devcontainer.json) bölümüne göz atın.
2. **Code** → **Codespaces** sekmesi → **...** → **New with options...** seçeneğine tıklayın
3. Varsayılanları kullanın – bu, bu kurs için özel olarak oluşturulmuş **Generative AI Java Development Environment** geliştirme konteyneri yapılandırmasını seçecektir
4. **Create codespace** seçeneğine tıklayın
5. Ortamın hazır olması için ~2 dakika bekleyin
6. [Adım 2: GitHub Jetonu Oluşturun](../../../02-SetupDevEnvironment) bölümüne geçin

<img src="./images/codespaces.png" alt="Ekran Görüntüsü: Codespaces alt menüsü" width="50%">

<img src="./images/image.png" alt="Ekran Görüntüsü: Yeni seçeneklerle" width="50%">

<img src="./images/codespaces-create.png" alt="Ekran Görüntüsü: Codespace oluşturma seçenekleri" width="50%">

> **Codespaces'in Avantajları**:
> - Yerel kurulum gerekmez
> - Tarayıcıya sahip herhangi bir cihazda çalışır
> - Tüm araçlar ve bağımlılıklar önceden yapılandırılmıştır
> - Kişisel hesaplar için ayda 60 saat ücretsiz
> - Tüm öğrenenler için tutarlı bir ortam

#### Seçenek B: Yerel Geliştirme Konteyneri

**Docker ile yerel geliştirmeyi tercih eden geliştiriciler için**

1. Bu depoyu yerel makinenize çatallayın ve klonlayın
   > **Not**: Temel yapılandırmayı düzenlemek isterseniz [Geliştirme Konteyneri Yapılandırması](../../../.devcontainer/devcontainer.json) bölümüne göz atın.
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) ve [VS Code](https://code.visualstudio.com/) yükleyin
3. VS Code'da [Geliştirme Konteynerleri eklentisini](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) yükleyin
4. Depo klasörünü VS Code'da açın
5. İstendiğinde, **Konteynerde Yeniden Aç** seçeneğine tıklayın (veya `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" seçeneğini kullanın)
6. Konteynerin oluşturulması ve başlatılması için bekleyin
7. [Adım 2: GitHub Jetonu Oluşturun](../../../02-SetupDevEnvironment) bölümüne geçin

<img src="./images/devcontainer.png" alt="Ekran Görüntüsü: Geliştirme konteyneri kurulumu" width="50%">

<img src="./images/image-3.png" alt="Ekran Görüntüsü: Geliştirme konteyneri oluşturma tamamlandı" width="50%">

#### Seçenek C: Mevcut Yerel Kurulumunuzu Kullanın

**Mevcut Java ortamlarına sahip geliştiriciler için**

Önkoşullar:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) veya tercih ettiğiniz IDE

Adımlar:
1. Bu depoyu yerel makinenize klonlayın
2. Projeyi IDE'nizde açın
3. [Adım 2: GitHub Jetonu Oluşturun](../../../02-SetupDevEnvironment) bölümüne geçin

> **Profesyonel İpucu**: Düşük özellikli bir makineniz varsa ancak VS Code'u yerel olarak kullanmak istiyorsanız, GitHub Codespaces'i kullanın! Yerel VS Code'unuzu bulutta barındırılan bir Codespace'e bağlayarak her iki dünyanın en iyisini elde edebilirsiniz.

<img src="./images/image-2.png" alt="Ekran Görüntüsü: Yerel geliştirme konteyneri örneği oluşturuldu" width="50%">

## Adım 2: GitHub Kişisel Erişim Jetonu Oluşturun

1. [GitHub Ayarları](https://github.com/settings/profile) sayfasına gidin ve profil menüsünden **Settings** seçeneğini seçin.
2. Sol kenar çubuğunda, **Developer settings** seçeneğine tıklayın (genellikle en altta).
3. **Personal access tokens** altında, **Fine-grained tokens** seçeneğine tıklayın (veya bu doğrudan [bağlantıyı](https://github.com/settings/personal-access-tokens) takip edin).
4. **Generate new token** seçeneğine tıklayın.
5. "Token name" alanına açıklayıcı bir ad verin (ör. `GenAI-Java-Course-Token`).
6. Bir son kullanma tarihi belirleyin (güvenlik en iyi uygulamaları için önerilen: 7 gün).
7. "Resource owner" altında, kullanıcı hesabınızı seçin.
8. "Repository access" altında, GitHub Modelleri ile kullanmak istediğiniz depoları seçin (veya gerekirse "All repositories").
9. "Repository permissions" altında **Models** seçeneğini bulun ve **Read and write** olarak ayarlayın.
10. **Generate token** seçeneğine tıklayın.
11. **Jetonunuzu şimdi kopyalayıp kaydedin** – daha sonra göremeyeceksiniz!

> **Güvenlik İpucu**: Erişim jetonlarınız için minimum gerekli kapsamı ve en kısa pratik son kullanma süresini kullanın.

## Adım 3: GitHub Modelleri Örneği ile Kurulumunuzu Test Edin

Geliştirme ortamınız hazır olduğunda, GitHub Modelleri entegrasyonunu [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) altındaki örnek uygulamamızla test edelim.

1. Geliştirme ortamınızda terminali açın.
2. GitHub Modelleri örneğine gidin:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. GitHub jetonunuzu bir ortam değişkeni olarak ayarlayın:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Uygulamayı çalıştırın:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Benzer bir çıktı görmelisiniz:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Örnek Kodun Anlaşılması

Öncelikle, çalıştırdığımız şeyi anlayalım. `examples/github-models` altındaki örnek, GitHub Modellerine bağlanmak için OpenAI Java SDK'sını kullanır:

**Bu kodun yaptığı şey:**
- GitHub Modellerine kişisel erişim jetonunuzla **bağlanır**
- AI modeline basit bir "Say Hello World!" mesajı **gönderir**
- AI'nın yanıtını **alır** ve görüntüler
- Kurulumunuzun doğru çalıştığını **doğrular**

**Ana Bağımlılık** (`pom.xml` içinde):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Ana Kod** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Özet

Harika! Artık her şey kurulu:

- AI model erişimi için doğru izinlere sahip bir GitHub Kişisel Erişim Jetonu oluşturdunuz
- Java geliştirme ortamınızı çalıştırdınız (Codespaces, geliştirme konteynerleri veya yerel kurulum)
- OpenAI Java SDK'sını kullanarak GitHub Modellerine bağlandınız ve ücretsiz AI geliştirme için test ettiniz
- AI modelleriyle konuşan basit bir örnekle her şeyin çalıştığını doğruladınız

## Sonraki Adımlar

[3. Bölüm: Temel Üretken Yapay Zeka Teknikleri](../03-CoreGenerativeAITechniques/README.md)

## Sorun Giderme

Sorun mu yaşıyorsunuz? İşte yaygın problemler ve çözümleri:

- **Jeton çalışmıyor mu?** 
  - Jetonun tamamını ekstra boşluklar olmadan kopyaladığınızdan emin olun
  - Jetonun doğru bir şekilde ortam değişkeni olarak ayarlandığını doğrulayın
  - Jetonunuzun doğru izinlere sahip olduğundan emin olun (Models: Read and write)

- **Maven bulunamadı mı?** 
  - Geliştirme konteynerleri/Codespaces kullanıyorsanız, Maven önceden yüklenmiş olmalıdır
  - Yerel kurulum için Java 21+ ve Maven 3.9+ yüklü olduğundan emin olun
  - Kurulumu doğrulamak için `mvn --version` komutunu deneyin

- **Bağlantı sorunları mı?** 
  - İnternet bağlantınızı kontrol edin
  - GitHub'ın ağınızdan erişilebilir olduğundan emin olun
  - GitHub Modelleri uç noktasını engelleyen bir güvenlik duvarı olmadığından emin olun

- **Geliştirme konteyneri başlamıyor mu?** 
  - Docker Desktop'ın çalıştığından emin olun (yerel geliştirme için)
  - Konteyneri yeniden oluşturmayı deneyin: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Uygulama derleme hataları mı?**
  - Doğru dizinde olduğunuzdan emin olun: `02-SetupDevEnvironment/examples/github-models`
  - Temizleyip yeniden derlemeyi deneyin: `mvn clean compile`

> **Yardım mı gerekiyor?**: Hala sorun yaşıyorsanız, depoda bir sorun açın ve size yardımcı olalım.

**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Belgenin orijinal dili, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yanlış yorumlamalar için sorumluluk kabul etmiyoruz.