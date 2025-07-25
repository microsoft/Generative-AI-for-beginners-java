<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:29:17+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "tr"
}
-->
# Temel Üretken Yapay Zeka Teknikleri Eğitimi

## İçindekiler

- [Ön Koşullar](../../../03-CoreGenerativeAITechniques)
- [Başlarken](../../../03-CoreGenerativeAITechniques)
  - [Adım 1: Ortam Değişkeninizi Ayarlayın](../../../03-CoreGenerativeAITechniques)
  - [Adım 2: Örnekler Dizinine Geçin](../../../03-CoreGenerativeAITechniques)
- [Eğitim 1: LLM Tamamlama ve Sohbet](../../../03-CoreGenerativeAITechniques)
- [Eğitim 2: Fonksiyon Çağırma](../../../03-CoreGenerativeAITechniques)
- [Eğitim 3: RAG (Bilgi Getirme Destekli Üretim)](../../../03-CoreGenerativeAITechniques)
- [Eğitim 4: Sorumlu Yapay Zeka](../../../03-CoreGenerativeAITechniques)
- [Örnekler Arasında Ortak Desenler](../../../03-CoreGenerativeAITechniques)
- [Sonraki Adımlar](../../../03-CoreGenerativeAITechniques)
- [Sorun Giderme](../../../03-CoreGenerativeAITechniques)
  - [Yaygın Sorunlar](../../../03-CoreGenerativeAITechniques)

## Genel Bakış

Bu eğitim, Java ve GitHub Modelleri kullanarak temel üretken yapay zeka tekniklerini uygulamalı örneklerle sunar. Büyük Dil Modelleri (LLM) ile nasıl etkileşim kuracağınızı, fonksiyon çağırmayı nasıl uygulayacağınızı, bilgi getirme destekli üretimi (RAG) nasıl kullanacağınızı ve sorumlu yapay zeka uygulamalarını nasıl hayata geçireceğinizi öğreneceksiniz.

## Ön Koşullar

Başlamadan önce aşağıdakilere sahip olduğunuzdan emin olun:
- Java 21 veya daha üstü sürüm yüklü
- Bağımlılık yönetimi için Maven
- Kişisel erişim belirteci (PAT) olan bir GitHub hesabı

## Başlarken

### Adım 1: Ortam Değişkeninizi Ayarlayın

Öncelikle, GitHub belirtecinizi bir ortam değişkeni olarak ayarlamanız gerekir. Bu belirteç, GitHub Modellerine ücretsiz erişim sağlar.

**Windows (Komut İstemi):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Adım 2: Örnekler Dizinine Geçin

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Eğitim 1: LLM Tamamlama ve Sohbet

**Dosya:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Bu Örnek Ne Öğretiyor?

Bu örnek, OpenAI API'si aracılığıyla Büyük Dil Modeli (LLM) etkileşiminin temel mekaniklerini gösterir. GitHub Modelleri ile istemci başlatma, sistem ve kullanıcı istemleri için mesaj yapısı desenleri, mesaj geçmişi birikimi yoluyla konuşma durumu yönetimi ve yanıt uzunluğu ile yaratıcılık seviyelerini kontrol etmek için parametre ayarlama gibi konuları kapsar.

### Temel Kod Kavramları

#### 1. İstemci Kurulumu
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Bu, belirtecinizle GitHub Modellerine bir bağlantı oluşturur.

#### 2. Basit Tamamlama
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Konuşma Hafızası
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Yapay zeka, önceki mesajları yalnızca sonraki isteklerde dahil ederseniz hatırlar.

### Örneği Çalıştırın
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Çalıştırdığınızda Ne Olur?

1. **Basit Tamamlama**: Yapay zeka, sistem istemi rehberliğiyle bir Java sorusunu yanıtlar.
2. **Çoklu Dönüş Sohbet**: Yapay zeka, birden fazla soru boyunca bağlamı korur.
3. **Etkileşimli Sohbet**: Yapay zeka ile gerçek bir konuşma yapabilirsiniz.

## Eğitim 2: Fonksiyon Çağırma

**Dosya:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Bu Örnek Ne Öğretiyor?

Fonksiyon çağırma, yapay zeka modellerinin doğal dil isteklerini analiz ederek, JSON Şeması tanımları kullanarak gerekli fonksiyon çağrılarını uygun parametrelerle belirlediği ve döndürülen sonuçları bağlamsal yanıtlar oluşturmak için işlediği bir protokol aracılığıyla harici araçlar ve API'lerin çalıştırılmasını talep etmesini sağlar. Gerçek fonksiyon yürütmesi, güvenlik ve güvenilirlik için geliştiricinin kontrolü altında kalır.

### Temel Kod Kavramları

#### 1. Fonksiyon Tanımı
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Bu, yapay zekaya hangi fonksiyonların mevcut olduğunu ve nasıl kullanılacağını söyler.

#### 2. Fonksiyon Yürütme Akışı
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Fonksiyon Uygulaması
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Örneği Çalıştırın
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Çalıştırdığınızda Ne Olur?

1. **Hava Durumu Fonksiyonu**: Yapay zeka Seattle için hava durumu verisi ister, siz sağlarsınız, yapay zeka bir yanıt oluşturur.
2. **Hesap Makinesi Fonksiyonu**: Yapay zeka bir hesaplama ister (240'ın %15'i), siz hesaplar ve sonucu açıklarsınız.

## Eğitim 3: RAG (Bilgi Getirme Destekli Üretim)

**Dosya:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Bu Örnek Ne Öğretiyor?

Bilgi Getirme Destekli Üretim (RAG), bilgi getirme ile dil üretimini birleştirerek, yapay zeka istemlerine harici belge bağlamı ekler. Bu, modellerin genel bilgi yerine belirli bilgi kaynaklarına dayalı doğru yanıtlar sağlamasını sağlar. Ayrıca, stratejik istem mühendisliği yoluyla kullanıcı sorguları ile otoriter bilgi kaynakları arasında net sınırlar korur.

### Temel Kod Kavramları

#### 1. Belge Yükleme
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Bağlam Enjeksiyonu
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Üçlü tırnak işaretleri, yapay zekanın bağlam ile soruyu ayırt etmesine yardımcı olur.

#### 3. Güvenli Yanıt İşleme
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API yanıtlarını doğrulayarak çökme riskini önleyin.

### Örneği Çalıştırın
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Çalıştırdığınızda Ne Olur?

1. Program `document.txt` dosyasını yükler (GitHub Modelleri hakkında bilgi içerir).
2. Belgeyle ilgili bir soru sorarsınız.
3. Yapay zeka, yalnızca belge içeriğine dayanarak yanıt verir, genel bilgisine değil.

Şunu deneyin: "GitHub Modelleri nedir?" ve "Hava durumu nasıl?" sorularını sorun.

## Eğitim 4: Sorumlu Yapay Zeka

**Dosya:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Bu Örnek Ne Öğretiyor?

Sorumlu Yapay Zeka örneği, yapay zeka uygulamalarında güvenlik önlemlerinin uygulanmasının önemini vurgular. Nefret söylemi, taciz, kendine zarar verme, cinsel içerik ve şiddet gibi zararlı içerik kategorilerini tespit eden güvenlik filtrelerini gösterir. Üretim yapay zeka uygulamalarının içerik politikası ihlallerini uygun istisna işleme, kullanıcı geri bildirim mekanizmaları ve yedek yanıt stratejileriyle nasıl ele alması gerektiğini açıklar.

### Temel Kod Kavramları

#### 1. Güvenlik Test Çerçevesi
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Test Edilen Güvenlik Kategorileri
- Şiddet/Zarar talimatları
- Nefret söylemi
- Gizlilik ihlalleri
- Tıbbi yanlış bilgi
- Yasadışı faaliyetler

### Örneği Çalıştırın
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Çalıştırdığınızda Ne Olur?

Program çeşitli zararlı istemleri test eder ve yapay zeka güvenlik sistemi şu şekilde çalışır:
1. **Tehlikeli istekleri engeller** (HTTP 400 hataları ile).
2. **Güvenli içeriklerin** normal şekilde üretilmesine izin verir.
3. **Kullanıcıları korur** zararlı yapay zeka çıktılarından.

## Örnekler Arasında Ortak Desenler

### Kimlik Doğrulama Deseni
Tüm örnekler, GitHub Modelleri ile kimlik doğrulamak için bu deseni kullanır:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Hata İşleme Deseni
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Mesaj Yapısı Deseni
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Sonraki Adımlar

[4. Bölüm: Uygulamalı Örnekler](../04-PracticalSamples/README.md)

## Sorun Giderme

### Yaygın Sorunlar

**"GITHUB_TOKEN ayarlanmadı"**
- Ortam değişkenini ayarladığınızdan emin olun.
- Belirtecinizin `models:read` kapsamına sahip olduğunu doğrulayın.

**"API'den yanıt yok"**
- İnternet bağlantınızı kontrol edin.
- Belirtecinizin geçerli olduğunu doğrulayın.
- Kota sınırlarını aşıp aşmadığınızı kontrol edin.

**Maven derleme hataları**
- Java 21 veya daha üstü bir sürüm kullandığınızdan emin olun.
- `mvn clean compile` komutunu çalıştırarak bağımlılıkları yenileyin.

**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlıklar içerebileceğini lütfen unutmayın. Belgenin orijinal dilindeki hali, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yanlış yorumlamalar için sorumluluk kabul etmiyoruz.