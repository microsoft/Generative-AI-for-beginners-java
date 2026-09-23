# Başlangıç Seviyesi için Üretken Yapay Zeka - Java Sürümü
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Başlangıç Seviyesi için Üretken Yapay Zeka - Java Sürümü](../../translated_images/tr/beg-genai-series.8b48be9951cc574c.webp)

**Zaman Taahhüdü**: Tüm atölye yerel kurulum olmadan çevrimiçi tamamlanabilir. Ortam kurulumu 2 dakika sürer, örnekleri keşfetmek 1-3 saat arasında keşfetme derinliğine bağlı olarak değişir.

> **Hızlı Başlangıç** 

1. Bu depoyu GitHub hesabınıza çatallayın
2. **Code** → **Codespaces** sekmesine tıklayın → **...** → **New with options...**
3. Varsayılanları kullanın – bu kurs için oluşturulmuş Geliştirme kapsayıcısını seçecektir
4. **Create codespace** seçeneğine tıklayın
5. Ortamın hazır olması için yaklaşık 2 dakika bekleyin
6. Doğrudan [Bölüm 2: Azure AI Foundry Sağlama](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry) bölümüne geçin

## Çok Dilli Destek

### GitHub Action ile Desteklenir (Otomatik ve Her Zaman Güncel)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arapça](../ar/README.md) | [Bengalce](../bn/README.md) | [Bulgarca](../bg/README.md) | [Burma Dili (Myanmar)](../my/README.md) | [Çince (Basitleştirilmiş)](../zh-CN/README.md) | [Çince (Geleneksel, Hong Kong)](../zh-HK/README.md) | [Çince (Geleneksel, Makao)](../zh-MO/README.md) | [Çince (Geleneksel, Tayvan)](../zh-TW/README.md) | [Hırvatça](../hr/README.md) | [Çekçe](../cs/README.md) | [Danca](../da/README.md) | [Flemenkçe](../nl/README.md) | [Estonca](../et/README.md) | [Fince](../fi/README.md) | [Fransızca](../fr/README.md) | [Almanca](../de/README.md) | [Yunanca](../el/README.md) | [İbranice](../he/README.md) | [Hintçe](../hi/README.md) | [Macarca](../hu/README.md) | [Endonezce](../id/README.md) | [İtalyanca](../it/README.md) | [Japonca](../ja/README.md) | [Kannada](../kn/README.md) | [Kmerce](../km/README.md) | [Korece](../ko/README.md) | [Litvanca](../lt/README.md) | [Malayca](../ms/README.md) | [Malayalamca](../ml/README.md) | [Marathi](../mr/README.md) | [Nepalce](../ne/README.md) | [Nijerya Pidgin](../pcm/README.md) | [Norveççe](../no/README.md) | [Farsça (Fars)](../fa/README.md) | [Lehçe](../pl/README.md) | [Portekizce (Brezilya)](../pt-BR/README.md) | [Portekizce (Portekiz)](../pt-PT/README.md) | [Pencapça (Gurmukhi)](../pa/README.md) | [Rumence](../ro/README.md) | [Rusça](../ru/README.md) | [Sırpça (Kiril)](../sr/README.md) | [Slovakça](../sk/README.md) | [Slovence](../sl/README.md) | [İspanyolca](../es/README.md) | [Svahili](../sw/README.md) | [İsveççe](../sv/README.md) | [Tagalogca (Filipince)](../tl/README.md) | [Tamilce](../ta/README.md) | [Teluguca](../te/README.md) | [Tayca](../th/README.md) | [Türkçe](./README.md) | [Ukraynaca](../uk/README.md) | [Urduca](../ur/README.md) | [Vietnamca](../vi/README.md)

> **Yerelde Çoğaltmayı mı Tercih Edersiniz?**
>
> Bu depo 50+ dil çevirisini içerir, bu da indirme boyutunu önemli ölçüde artırır. Çeviriler olmadan klonlamak için seyrek kontrol kullanın:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> Bu, kursu tamamlamak için ihtiyacınız olan her şeyi çok daha hızlı bir indirme ile sağlar.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kurs Yapısı ve Öğrenme Yolu

### **Bölüm 1: Üretken Yapay Zekaya Giriş**
- **Temel Kavramlar**: Büyük Dil Modellerini, tokenleri, gömme (embedding) yöntemlerini ve yapay zeka yeteneklerini anlama
- **Java AI Ekosistemi**: Spring AI ve OpenAI SDK'larının genel görünümü
- **Model Konteks Protokolü**: MCP'ye giriş ve AI ajan iletişimindeki rolü
- **Pratik Uygulamalar**: Sohbet botları ve içerik oluşturma dahil gerçek dünya senaryoları
- **[→ Bölüm 1'e Başla](./01-IntroToGenAI/README.md)**

### **Bölüm 2: Geliştirme Ortamı Kurulumu**
- **Azure AI Foundry**: Bicep ve Azure Developer CLI (azd) ile GPT-5.6 Luna sohbet ve text-embedding-3-small gömme sağlama
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: Resmi OpenAI Java SDK ve Azure OpenAI v1 destekli `ChatClient` ile öğrenme
- **Anahtarsız Doğrulama**: Microsoft Entra ID ile güvenli bağlantı — yönetilecek API anahtarı yok
- **Geliştirme Araçları**: Docker kapsayıcıları, VS Code ve GitHub Codespaces yapılandırması
- **[→ Bölüm 2'ye Başla](./02-SetupDevEnvironment/README.md)**

### **Bölüm 3: Temel Üretken Yapay Zeka Teknikleri**
- **Resmi OpenAI Java SDK**: Anahtarsız doğrulama ile Azure OpenAI v1'i doğrudan çağırma
- **Prompt Mühendisliği**: AI modelinden en iyi yanıtı almak için teknikler
- **Embeddingler ve Vektör İşlemleri**: Anlamsal arama ve benzerlik eşleştirme uygulama
- **Retrieval-Augmented Generation (RAG)**: AI'yi kendi veri kaynaklarınızla birleştirme
- **Fonksiyon Çağırma**: AI yeteneklerini özel araçlar ve eklentilerle genişletme
- **[→ Bölüm 3'e Başla](./03-CoreGenerativeAITechniques/README.md)**

### **Bölüm 4: Pratik Uygulamalar ve Projeler**
- **Evcil Hayvan Hikayesi Üreticisi** (`petstory/`): Azure AI Foundry ile yaratıcı içerik üretimi
- **Foundry Yerel Demo** (`foundrylocal/`): OpenAI Java SDK ile yerel AI modeli entegrasyonu
- **MCP Hesaplayıcı Servisi** (`calculator/`): Spring AI ile temel Model Konteks Protokolü uygulaması
- **[→ Bölüm 4'e Başla](./04-PracticalSamples/README.md)**

### **Bölüm 5: Sorumlu AI Geliştirme**
- **Azure AI Foundry İçerik Güvenliği**: Dahili içerik filtreleme ve güvenlik mekanizmalarını test etme (sert engellemeler ve yumuşak reddetmeler)
- **Sorumlu AI Demo**: Modern AI güvenlik sistemlerinin pratikte nasıl çalıştığını gösteren uygulamalı örnek
- **En İyi Uygulamalar**: Etik AI geliştirme ve dağıtımı için temel rehberler
- **[→ Bölüm 5'e Başla](./05-ResponsibleGenAI/README.md)**

## Ek Kaynaklar

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![Başlangıç için LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![Başlangıç için LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![Başlangıç için LangChain](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Ajanlar
[![Başlangıç için AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için Edge AI](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için AI Ajanları](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Üretken Yapay Zeka Serisi
[![Başlangıç için Üretken Yapay Zeka](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Üretken Yapay Zeka (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Üretken Yapay Zeka (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Üretken Yapay Zeka (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Temel Öğrenme
[![Başlangıç için Makine Öğrenimi](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için Veri Bilimi](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için Yapay Zeka](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için Siber Güvenlik](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Başlangıç için Web Geliştirme](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![Başlangıç için XR Geliştirme](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot Serisi
[![AI ile Eşli Programlama için Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET için Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Macerası](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Yardım Alma

AI uygulamaları geliştirirken takılırsanız veya herhangi bir sorunuz olursa, MCP hakkında tartışmalara katılmak için diğer öğrenenler ve deneyimli geliştiricilerle buluşun. Soruların hoş karşılandığı ve bilginin özgürce paylaşıldığı destekleyici bir topluluktur.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Ürün geri bildiriminiz veya oluştururken hatalarınız varsa ziyaret edin:

[![Microsoft Foundry Geliştirici Forumu](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Feragatname**:
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba sarf etsek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayınız. Orijinal belge, kendi dilinde yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımı sonucu ortaya çıkabilecek yanlış anlamalardan veya yanlış yorumlamalardan sorumlu değiliz.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->