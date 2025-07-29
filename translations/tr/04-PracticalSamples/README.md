<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T15:23:55+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "tr"
}
-->
# Pratik Uygulamalar ve Projeler

## Öğrenecekleriniz
Bu bölümde, Java ile üretken yapay zeka geliştirme kalıplarını sergileyen üç pratik uygulamayı göstereceğiz:
- İstemci tarafı ve sunucu tarafı yapay zekayı birleştiren çok modlu bir Evcil Hayvan Hikaye Üreticisi oluşturma
- Foundry Local Spring Boot demosu ile yerel yapay zeka modeli entegrasyonunu uygulama
- Hesap Makinesi örneğiyle bir Model Context Protocol (MCP) hizmeti geliştirme

## İçindekiler

- [Giriş](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Evcil Hayvan Hikaye Üreticisi](../../../04-PracticalSamples)
  - [MCP Hesap Makinesi Hizmeti (Başlangıç Seviyesi MCP Demosu)](../../../04-PracticalSamples)
- [Öğrenme Süreci](../../../04-PracticalSamples)
- [Özet](../../../04-PracticalSamples)
- [Sonraki Adımlar](../../../04-PracticalSamples)

## Giriş

Bu bölüm, Java ile üretken yapay zeka geliştirme kalıplarını sergileyen **örnek projeler** sunar. Her proje tamamen işlevseldir ve kendi uygulamalarınıza uyarlayabileceğiniz belirli yapay zeka teknolojilerini, mimari kalıpları ve en iyi uygulamaları gösterir.

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)**, **OpenAI Java SDK** kullanarak yerel yapay zeka modelleriyle nasıl entegre olunacağını gösterir. Bu demo, Foundry Local üzerinde çalışan **Phi-3.5-mini** modeline bağlanmayı sergiler ve bulut hizmetlerine bağımlı olmadan yapay zeka uygulamaları çalıştırmanıza olanak tanır.

### Evcil Hayvan Hikaye Üreticisi

**[Evcil Hayvan Hikaye Üreticisi](petstory/README.md)**, yaratıcı evcil hayvan hikayeleri üretmek için **çok modlu yapay zeka işleme** sergileyen eğlenceli bir Spring Boot web uygulamasıdır. Bu proje, tarayıcı tabanlı yapay zeka etkileşimleri için transformer.js'i ve sunucu tarafı işleme için OpenAI SDK'sını birleştirir.

### MCP Hesap Makinesi Hizmeti (Başlangıç Seviyesi MCP Demosu)

**[MCP Hesap Makinesi Hizmeti](calculator/README.md)**, Spring AI kullanarak **Model Context Protocol (MCP)**'nin basit bir gösterimidir. MCP kavramlarına başlangıç seviyesinde bir giriş sunar ve MCP istemcileriyle etkileşim kuran temel bir MCP Sunucusu oluşturmayı gösterir.

## Öğrenme Süreci

Bu projeler, önceki bölümlerdeki kavramların üzerine inşa edilmek üzere tasarlanmıştır:

1. **Basit Başlayın**: Yerel modellerle temel yapay zeka entegrasyonunu anlamak için Foundry Local Spring Boot Demo ile başlayın
2. **Etkileşim Ekleyin**: Çok modlu yapay zeka ve web tabanlı etkileşimler için Evcil Hayvan Hikaye Üreticisi'ne geçin
3. **MCP Temellerini Öğrenin**: Model Context Protocol temel bilgilerini anlamak için MCP Hesap Makinesi Hizmeti'ni deneyin

## Özet

Tebrikler! Artık bazı gerçek uygulamaları keşfettiniz:

- Hem tarayıcıda hem de sunucuda çalışan çok modlu yapay zeka deneyimleri
- Modern Java çerçeveleri ve SDK'ları kullanarak yerel yapay zeka modeli entegrasyonu
- Yapay zeka ile araçların nasıl entegre olduğunu görmek için ilk Model Context Protocol hizmetiniz

## Sonraki Adımlar

[5. Bölüm: Sorumlu Üretken Yapay Zeka](../05-ResponsibleGenAI/README.md)

**Feragatname**:  
Bu belge, AI çeviri hizmeti [Co-op Translator](https://github.com/Azure/co-op-translator) kullanılarak çevrilmiştir. Doğruluk için çaba göstersek de, otomatik çevirilerin hata veya yanlışlık içerebileceğini lütfen unutmayın. Belgenin orijinal dili, yetkili kaynak olarak kabul edilmelidir. Kritik bilgiler için profesyonel insan çevirisi önerilir. Bu çevirinin kullanımından kaynaklanan yanlış anlamalar veya yanlış yorumlamalar için sorumluluk kabul etmiyoruz.