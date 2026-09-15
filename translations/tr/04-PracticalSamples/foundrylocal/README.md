# Foundry Yerel Spring Boot Eğitimi

Küçük bir dil modelini kendi makinenizde çalıştırın ve Java konsol uygulamasından onun OpenAI uyumlu
REST uç noktasını çağırın. Hiçbir Azure dağıtımı, Azure giriş işlemi,
bulut API anahtarı veya bulut çıkarımı kullanılmaz. **GPT-5.6 Luna sadece Azure içindir; Foundry Yerel modeli olarak
yapılandırmayın.**

## Sürümler ve önkoşullar

| Bileşen | Sürüm |
| --- | --- |
| Java | 21 veya üzeri |
| Maven | 3.6.3 veya üzeri |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Yerel SDK (yerel REST sunucusu) | 2.0.1 |
| Node.js (yerel REST sunucusu) | 20 veya üzeri |
| Foundry Yerel CLI (isteğe bağlı, ayrı sürüm) | 0.10.3 önizleme |

Spring Boot, Spring Framework, Jackson, JUnit ve Maven eklenti sürümlerini yönetir.
Bu örnek, Spring AI yerine doğrudan OpenAI Java SDK'sını kullanır. Eski ve kullanılmayan
Spring AI kilometre taşı özelliği ve deposu kaldırılmıştır.

Önerilen başlangıç modeli **Qwen 2.5 0.5B**, CPU varyantı
`qwen2.5-0.5b-instruct-generic-cpu:4` (katalogda yaklaşık 822 MB).
GPU yürütme sağlayıcılarını gerektirmemesi açısından avantajlıdır. Diğer desteklenen, önbelleğe alınmış küçük modeller
açıkça seçilebilir. Model ve çalışma zamanı kurulumu ağ erişimi gerektirir;
prompt'lar ve çıkarım yereldir. Foundry Yerel, önemsiz telemetri kapalı olsa bile minimal çalışma zamanı
tanılamalarını verebilir.

Bu örnek dizininden aşağıdaki komutları çalıştırın.

## Java'yı derleyip test etme

```powershell
mvn clean verify
```

HTTP sözleşme testleri geçici bir loopback sunucusu başlatır ve gerçek
OpenAI Java SDK'sını sınar. İstek serileştirme, model keşfi, açık model
seçimi, belirsiz veya hatalı model listeleri, HTTP başarısızlıkları, boş yanıtlar,
sadece yerel URL'ler ve komut satırı hatalarının aktarımını kapsar. Bu testler model veya
ağ erişimi gerektirmez, Maven bağımlılık kurulumu hariç. Canlı test isteğe bağlıdır.

## Yerel modeli başlatma

### Önerilen: sabitlenmiş SDK sunucusu

Yerel Foundry Java SDK yerel değildir. Küçük Node.js yardımcı programı,
resmi SDK'nın REST sunucusunu barındırır; uygulama ve sohbet istekleri Java olarak kalır.

Sabitlenmiş çalışma zamanı bağımlılıklarını yükleyin:

```powershell
npm ci
```

Windows x64 sürümünde SDK'nın yerel kurulumu sırasında NuGet'e erişilemiyorsa, sağlanan
yedek yöntemi kullanın. Bu, uyumlu resmi GitHub çalışma zamanı arşivini indirir, sürümün
SHA-256 özeti doğrular ve DLL'lerini yerel eklentinin yanına koyar. TLS doğrulamasını
devre dışı bırakmaz, yükseltme gerektirmez veya SDK kaynağını değiştirmez.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Bu makinede önbelleğe alınan modelleri listeleyin:

```powershell
npm run start:foundry -- --list
```

İlk çalıştırmada, küçük CPU modelinin indirilmesine açık izin verin:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Sonraki çalıştırmalarda, önbelleğe alınmış model gerektirmek için `--download` parametresini atlayın:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Yardımcı program, eşleşen önbelleğe alınmış modeli tercih eder, takma ad veya tam varyant ID'sini kabul eder
ve `--download` verilmedikçe eksik modeli reddeder. Yalnızca seçilen modelin
yürütme sağlayıcısını gerektiğinde kaydeder. Önbelleğe alınmış GPU varyantları uygun yürütme sağlayıcı paketleri ve sürücüler
gerektirebilir.

Eğer 5273 numaralı port meşgulse, kullanılabilir bir port için `--port 0` parametresini geçin. Yardımcı program
hazır olduğunda `FOUNDRY_LOCAL_BASE_URL`, tam `FOUNDRY_LOCAL_MODEL` kimliğini ve PID'sini yazdırır.
Java'da yazdırılan uç noktayı kullanın. Java çalışırken bu terminali açık bırakın;
**Ctrl+C** REST sunucusunu durdurur ve modeli serbest bırakır.

Varsayılan önbellek `~/.foundry/cache/models` dizinidir. Farklı ve mevcut bir önbellek için `FOUNDRY_LOCAL_CACHE_DIR` ayarlayın.
Günlükler ve yardımcı durumları bu örneğin `target/foundry-local` dizini altında yazılır. `mvn clean` çalıştırmadan önce yardımcıyı durdurun.


### İsteğe Bağlı: Foundry Local CLI

CLI ve SDK bağımsız sürümlere sahiptir: CLI **0.10.3** SDK **1.2.4** paketler;
yukarıdaki yardımcı SDK **2.0.1** kullanır. En son CLI'yı yüklemek en son dil SDK'sını yüklemez.
[CLI sürüm notlarına](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3) bakın.

Windows'da, CLI yoksa kullanıcı başına yükleme komutunu kullanın:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Veya mevcut yüklemeyi yükseltin:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x eski `foundry service` komutlarını `foundry server` ile değiştirir:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` önceden indirilmiş bir modele ihtiyaç duyar. İndirme komutları için `foundry model --help` kontrol edin.
Durum çıktısındaki gerçek uç noktayı kullanın; CLI aksi takdirde
otomatik atanmış bir portu varsayılan olarak kullanır. Aynı portta hem CLI hem SDK yardımcısını başlatmayın.
İşiniz bitince:

```powershell
foundry server stop
```

## Java uygulamasını çalıştırın

İkinci bir terminalde, sunucunuzun yazdırdığı uç noktayı ve tam model kimliğini ayarlayın:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Veya paketlenmiş uygulamayı çalıştırın:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Tek Java giriş noktası `com.example.Application`dır. Seçilen
uç noktayı, gerçek model kimliğini, istemi ve oluşturulan yanıtı yazdırır, ardından Spring
bağlamını ve HTTP istemcisini kapatır. Başarısız çıkarım veya eksik yanıt metni,
başarı biçimli bir yer tutucu yerine bir başarısızlık çıkışı üretir.

### Yapılandırma

| Ortam değişkeni | Varsayılan | Amaç |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Döngüsel HTTP uç noktası, `/v1` dahil |
| `FOUNDRY_LOCAL_MODEL` | Boş | Tam model kimliği; aksi takdirde tek ilan edilen modeli seçer |
| `FOUNDRY_LOCAL_PROMPT` | Yerel modeller hakkında tek cümlelik bir soru | Konsol çalıştırıcısı tarafından gönderilen istem |

Eşdeğer Spring argümanları `--foundry.local.base-url=...`,
`--foundry.local.model=...` ve `--foundry.local.prompt=...`dur.
Sadece döngüsel HTTP uç noktaları kabul edilir. Uzak/bulut uç noktaları, gömülü
kimlik bilgileri, sorgu dizeleri ve `/v1` olmayan yollar reddedilir.

Boş model ayarı yalnızca `/v1/models` tam olarak bir model ilan ederse çalışır.
İlan edilen model illa yüklenmiş değildir. Birden fazla model ilan edildiyse,
katalog sıralamasına güvenmek yerine tam yüklü kimliği ayarlayın.

İstekler `temperature=0`, 150-token çıktı sınırı, 120 saniye zaman aşımı ve
otomatik yeniden deneme olmadan çalışır. `max_tokens` istek alanı kasıtlıdır: o
Foundry Local REST sözleşmesi tarafından desteklenmektedir, ancak OpenAI Java kullanımdan kaldırmaktadır
daha yeni bulut modelleri için o alan. Model kimliği yapılandırmadan veya
keşiften gelir, modelin kendisi hakkında yaptığı iddialardan değil.

## Canlı doğrulama

Yerel sunucu çalışırken, tercihli canlı testi de dahil tüm testleri çalıştırın.
Uç noktanın portunu sunucunuzun yazdırdığı değerle değiştirin. PowerShell'de noktalı
Maven özelliklerini tırnak içine alın:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Canlı test `Application.main`'i çağırır, "Fransa'nın başkenti Paris'tir" bilgisini verir,
şehir sorar ve oluşturulan gerçek metnin `Paris` olduğunu doğrular. Sadece başarılı
bir HTTP durumu değil, anlamsal bir sonucu kontrol eder.

Bu bir entegrasyon kontrolüdür, doğruluk kıyaslaması değildir. Doğrulama sırasında bu
0.5B model Java ve doğrudan REST aracılığıyla ayrı bir "2 + 2" komutuna `3` yanıtını verdi.
Aritmetik veya doğruluk için bağımsız doğrulama olmadan buna güvenmeyin; hesaplamalar için
belirli (deterministik) araçlar kullanın.

## Sorun Giderme

| Belirti | Kontrol |
| --- | --- |
| Bağlantı reddedildi | Hazır mesajını bekleyin; yazdırılan port ve `/v1` yolunu kullanın. |
| Birden fazla model duyuruldu | `FOUNDRY_LOCAL_MODEL` değişkenini yüklenen modelin tam kimliğiyle ayarlayın. |
| Model eksik | `--list` kullanın veya indirebilmek için açıkça `--download` kullanarak izin verin. |
| GPU sağlayıcı başarısız oluyor veya takılıyor | Küçük CPU modelini kullanın. Önbelleğe alınmış GPU modeli hala sağlayıcıya ihtiyaç duyar. |
| CLI `initializing` olarak kalıyor | `foundry server logs --lines 80` okuyun; daemon'u durdurun ve SDK yardımcısını kullanın. |
| NuGet TLS/indirme hatası | Ağ erişimini düzeltin veya yukarıda doğrulanmış Windows x64 yedek çözümü kullanın. TLS'yi devre dışı bırakmayın. |
| Port dolu | `--port 0` kullanın ve yazdırılan uç noktayı Java ile yapılandırın. |
| Seçenek yok veya boş metin | Uygulama bilerek başarısız oluyor; model ve çalışma zamanı günlüklerini inceleyin. |

## Kaynak ve referanslar

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): tek seferlik Spring Boot çalıştırıcısı.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): yazılı keşif ve yerel sohbet tamamlamaları.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): HTTP sözleşmesi, çalıştırıcı ve canlı testler.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): önbelleğe alınmış model seçimi ve temizlik ile resmi SDK REST sunucusu.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): doğrulanmış Windows x64 yerel çalışma zamanı yedek çözümü.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) ve [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): yapılandırma ve bağımlılıklar.
- [Foundry Local REST integration](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Foundry Local 2.0.1 sürüm ve geçiş notları](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Bölüm 04: Pratik örnekler](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->