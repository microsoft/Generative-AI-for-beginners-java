<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:29:48+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "tr"
}
-->
# Pratik Uygulamalar ve Projeler

## Öğrenecekleriniz
Bu bölümde, Java ile üretken yapay zeka geliştirme modellerini sergileyen üç pratik uygulamayı göstereceğiz:
- İstemci tarafı ve sunucu tarafı yapay zekayı birleştiren çok modlu bir Evcil Hayvan Hikaye Üreticisi oluşturma
- Foundry Local Spring Boot demosu ile yerel yapay zeka modeli entegrasyonunu uygulama
- Hesap Makinesi örneği ile bir Model Context Protocol (MCP) hizmeti geliştirme

## İçindekiler

- [Giriş](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Evcil Hayvan Hikaye Üreticisi](../../../04-PracticalSamples)
  - [MCP Hesap Makinesi Hizmeti (Yeni Başlayanlar İçin MCP Demo)](../../../04-PracticalSamples)
- [Öğrenme Süreci](../../../04-PracticalSamples)
- [Özet](../../../04-PracticalSamples)
- [Sonraki Adımlar](../../../04-PracticalSamples)

## Giriş

Bu bölüm, Java ile üretken yapay zeka geliştirme modellerini sergileyen **örnek projeleri** sunar. Her proje tamamen işlevseldir ve kendi uygulamalarınıza uyarlayabileceğiniz belirli yapay zeka teknolojilerini, mimari modelleri ve en iyi uygulamaları gösterir.

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)**, **OpenAI Java SDK** kullanarak yerel yapay zeka modelleriyle entegrasyonu nasıl gerçekleştireceğinizi gösterir. Bu demo, Foundry Local üzerinde çalışan **Phi-3.5-mini** modeline bağlanmayı sergiler ve bulut hizmetlerine bağımlı olmadan yapay zeka uygulamaları çalıştırmanıza olanak tanır.

### Evcil Hayvan Hikaye Üreticisi

**[Evcil Hayvan Hikaye Üreticisi](petstory/README.md)**, yaratıcı evcil hayvan hikayeleri üretmek için **çok modlu yapay zeka işleme** sergileyen eğlenceli bir Spring Boot web uygulamasıdır. Bu uygulama, tarayıcı tabanlı yapay zeka etkileşimleri için transformer.js ve sunucu tarafı işleme için OpenAI SDK kullanarak istemci tarafı ve sunucu tarafı yapay zekayı birleştirir.

### MCP Hesap Makinesi Hizmeti (Yeni Başlayanlar İçin MCP Demo)

**[MCP Hesap Makinesi Hizmeti](mcp/calculator/README.md)**, Spring AI kullanarak **Model Context Protocol (MCP)**'nin basit bir gösterimidir. MCP kavramlarına yeni başlayanlar için uygun bir giriş sunar ve MCP istemcileriyle etkileşimde bulunan temel bir MCP Sunucusu oluşturmayı gösterir.

## Öğrenme Süreci

Bu projeler, önceki bölümlerdeki kavramlar üzerine inşa edilmek üzere tasarlanmıştır:

1. **Basit Başlayın**: Yerel modellerle temel yapay zeka entegrasyonunu anlamak için Foundry Local Spring Boot Demo ile başlayın
2. **Etkileşim Ekleyin**: Çok modlu yapay zeka ve web tabanlı etkileşimler için Evcil Hayvan Hikaye Üreticisi'ne geçin
3. **MCP Temellerini Öğrenin**: Model Context Protocol temel bilgilerini anlamak için MCP Hesap Makinesi Hizmeti'ni deneyin

## Özet

**Tebrikler!** Başarıyla:

- **Çok modlu yapay zeka deneyimleri oluşturdunuz**, istemci tarafı ve sunucu tarafı yapay zeka işlemlerini birleştirdiniz
- **Modern Java çerçeveleri ve SDK'lar kullanarak yerel yapay zeka modeli entegrasyonu uyguladınız**
- **Araç entegrasyon modellerini sergileyen Model Context Protocol hizmetleri geliştirdiniz**

## Sonraki Adımlar

[5. Bölüm: Sorumlu Üretken Yapay Zeka](../05-ResponsibleGenAI/README.md)

**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Belgenin orijinal dili, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yanlış yorumlamalar için sorumluluk kabul etmiyoruz.