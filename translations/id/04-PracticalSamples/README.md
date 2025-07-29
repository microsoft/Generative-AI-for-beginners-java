<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T15:56:07+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "id"
}
-->
# Aplikasi Praktis & Proyek

## Apa yang Akan Anda Pelajari
Di bagian ini, kami akan mendemonstrasikan tiga aplikasi praktis yang menampilkan pola pengembangan AI generatif dengan Java:
- Membuat Generator Cerita Hewan Peliharaan multi-modal yang menggabungkan AI sisi klien dan sisi server
- Mengimplementasikan integrasi model AI lokal dengan demo Foundry Local Spring Boot
- Mengembangkan layanan Model Context Protocol (MCP) dengan contoh Kalkulator

## Daftar Isi

- [Pendahuluan](../../../04-PracticalSamples)
  - [Demo Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Generator Cerita Hewan Peliharaan](../../../04-PracticalSamples)
  - [Layanan MCP Kalkulator (Demo MCP Ramah Pemula)](../../../04-PracticalSamples)
- [Progres Pembelajaran](../../../04-PracticalSamples)
- [Ringkasan](../../../04-PracticalSamples)
- [Langkah Selanjutnya](../../../04-PracticalSamples)

## Pendahuluan

Bab ini menampilkan **proyek contoh** yang mendemonstrasikan pola pengembangan AI generatif dengan Java. Setiap proyek sepenuhnya fungsional dan menunjukkan teknologi AI tertentu, pola arsitektur, serta praktik terbaik yang dapat Anda adaptasi untuk aplikasi Anda sendiri.

### Demo Foundry Local Spring Boot

**[Demo Foundry Local Spring Boot](foundrylocal/README.md)** menunjukkan cara berintegrasi dengan model AI lokal menggunakan **OpenAI Java SDK**. Demo ini menampilkan koneksi ke model **Phi-3.5-mini** yang berjalan di Foundry Local, memungkinkan Anda menjalankan aplikasi AI tanpa bergantung pada layanan cloud.

### Generator Cerita Hewan Peliharaan

**[Generator Cerita Hewan Peliharaan](petstory/README.md)** adalah aplikasi web Spring Boot yang menarik, yang mendemonstrasikan **pemrosesan AI multi-modal** untuk menghasilkan cerita kreatif tentang hewan peliharaan. Aplikasi ini menggabungkan kemampuan AI sisi klien dan sisi server menggunakan transformer.js untuk interaksi AI berbasis browser dan OpenAI SDK untuk pemrosesan sisi server.

### Layanan MCP Kalkulator (Demo MCP Ramah Pemula)

**[Layanan MCP Kalkulator](calculator/README.md)** adalah demonstrasi sederhana dari **Model Context Protocol (MCP)** menggunakan Spring AI. Proyek ini memberikan pengenalan yang ramah pemula tentang konsep MCP, menunjukkan cara membuat Server MCP dasar yang berinteraksi dengan klien MCP.

## Progres Pembelajaran

Proyek-proyek ini dirancang untuk membangun konsep dari bab sebelumnya:

1. **Mulai dengan Sederhana**: Mulailah dengan Demo Foundry Local Spring Boot untuk memahami integrasi AI dasar dengan model lokal
2. **Tambahkan Interaktivitas**: Lanjutkan ke Generator Cerita Hewan Peliharaan untuk AI multi-modal dan interaksi berbasis web
3. **Pelajari Dasar-Dasar MCP**: Coba Layanan MCP Kalkulator untuk memahami dasar-dasar Model Context Protocol

## Ringkasan

Kerja bagus! Anda telah menjelajahi beberapa aplikasi nyata:

- Pengalaman AI multi-modal yang bekerja baik di browser maupun di server
- Integrasi model AI lokal menggunakan kerangka kerja dan SDK Java modern
- Layanan Model Context Protocol pertama Anda untuk melihat bagaimana alat berintegrasi dengan AI

## Langkah Selanjutnya

[Bab 5: AI Generatif yang Bertanggung Jawab](../05-ResponsibleGenAI/README.md)

**Penafian**:  
Dokumen ini telah diterjemahkan menggunakan layanan penerjemahan AI [Co-op Translator](https://github.com/Azure/co-op-translator). Meskipun kami berusaha untuk memberikan hasil yang akurat, harap diketahui bahwa terjemahan otomatis mungkin mengandung kesalahan atau ketidakakuratan. Dokumen asli dalam bahasa aslinya harus dianggap sebagai sumber yang otoritatif. Untuk informasi yang bersifat kritis, disarankan menggunakan jasa penerjemahan profesional oleh manusia. Kami tidak bertanggung jawab atas kesalahpahaman atau penafsiran yang keliru yang timbul dari penggunaan terjemahan ini.