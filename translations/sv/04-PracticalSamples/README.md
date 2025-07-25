<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:38:21+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "sv"
}
-->
# Praktiska Tillämpningar & Projekt

## Vad Du Kommer Lära Dig
I den här sektionen demonstrerar vi tre praktiska tillämpningar som visar utvecklingsmönster för generativ AI med Java:
- Skapa en multimodal berättelsegenerator för husdjur som kombinerar AI på klient- och serversidan
- Implementera integration av lokala AI-modeller med Foundry Local Spring Boot-demo
- Utveckla en Model Context Protocol (MCP)-tjänst med hjälp av kalkylatorexemplet

## Innehållsförteckning

- [Introduktion](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot-demo](../../../04-PracticalSamples)
  - [Berättelsegenerator för husdjur](../../../04-PracticalSamples)
  - [MCP Kalkylatortjänst (Nybörjarvänlig MCP-demo)](../../../04-PracticalSamples)
- [Lärandets Progression](../../../04-PracticalSamples)
- [Sammanfattning](../../../04-PracticalSamples)
- [Nästa Steg](../../../04-PracticalSamples)

## Introduktion

Det här kapitlet presenterar **exempelprojekt** som visar utvecklingsmönster för generativ AI med Java. Varje projekt är fullt fungerande och demonstrerar specifika AI-teknologier, arkitekturmönster och bästa praxis som du kan anpassa till dina egna applikationer.

### Foundry Local Spring Boot-demo

**[Foundry Local Spring Boot-demo](foundrylocal/README.md)** visar hur man integrerar med lokala AI-modeller med hjälp av **OpenAI Java SDK**. Den demonstrerar anslutning till **Phi-3.5-mini**-modellen som körs på Foundry Local, vilket gör det möjligt att köra AI-applikationer utan att förlita sig på molntjänster.

### Berättelsegenerator för husdjur

**[Berättelsegenerator för husdjur](petstory/README.md)** är en engagerande Spring Boot-webbapplikation som demonstrerar **multimodal AI-bearbetning** för att skapa kreativa berättelser om husdjur. Den kombinerar AI-funktioner på klientsidan och serversidan med hjälp av transformer.js för AI-interaktioner i webbläsaren och OpenAI SDK för bearbetning på serversidan.

### MCP Kalkylatortjänst (Nybörjarvänlig MCP-demo)

**[MCP Kalkylatortjänst](mcp/calculator/README.md)** är en enkel demonstration av **Model Context Protocol (MCP)** med hjälp av Spring AI. Den ger en nybörjarvänlig introduktion till MCP-koncept och visar hur man skapar en grundläggande MCP-server som interagerar med MCP-klienter.

## Lärandets Progression

Dessa projekt är designade för att bygga vidare på koncept från tidigare kapitel:

1. **Börja Enkelt**: Börja med Foundry Local Spring Boot-demo för att förstå grundläggande AI-integration med lokala modeller
2. **Lägg till Interaktivitet**: Gå vidare till Berättelsegenerator för husdjur för multimodal AI och webbaserade interaktioner
3. **Lär Dig MCP-grunderna**: Testa MCP Kalkylatortjänst för att förstå grunderna i Model Context Protocol

## Sammanfattning

**Grattis!** Du har framgångsrikt:

- **Skapat multimodala AI-upplevelser** som kombinerar AI-bearbetning på klient- och serversidan
- **Implementerat integration av lokala AI-modeller** med moderna Java-ramverk och SDK:er
- **Utvecklat Model Context Protocol-tjänster** som demonstrerar integrationsmönster för verktyg

## Nästa Steg

[Kapitel 5: Ansvarsfull Generativ AI](../05-ResponsibleGenAI/README.md)

**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiserade översättningar kan innehålla fel eller felaktigheter. Det ursprungliga dokumentet på dess ursprungliga språk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.