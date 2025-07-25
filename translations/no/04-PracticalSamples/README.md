<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:42:48+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "no"
}
-->
# Praktiske Applikasjoner og Prosjekter

## Hva Du Vil Lære
I denne delen vil vi demonstrere tre praktiske applikasjoner som viser utviklingsmønstre for generativ AI med Java:
- Lage en multimodal Historiegenerator for Kjæledyr som kombinerer AI på klient- og serversiden
- Implementere lokal AI-modellintegrasjon med Foundry Local Spring Boot-demoen
- Utvikle en Model Context Protocol (MCP)-tjeneste med Kalkulator-eksempelet

## Innholdsfortegnelse

- [Introduksjon](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Historiegenerator for Kjæledyr](../../../04-PracticalSamples)
  - [MCP Kalkulator-tjeneste (Nybegynnervennlig MCP-demo)](../../../04-PracticalSamples)
- [Læringsprogresjon](../../../04-PracticalSamples)
- [Oppsummering](../../../04-PracticalSamples)
- [Neste Steg](../../../04-PracticalSamples)

## Introduksjon

Dette kapittelet viser **eksempelprosjekter** som demonstrerer utviklingsmønstre for generativ AI med Java. Hvert prosjekt er fullt funksjonelt og viser spesifikke AI-teknologier, arkitektoniske mønstre og beste praksis som du kan tilpasse til dine egne applikasjoner.

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** demonstrerer hvordan man integrerer med lokale AI-modeller ved hjelp av **OpenAI Java SDK**. Den viser hvordan man kobler til **Phi-3.5-mini**-modellen som kjører på Foundry Local, slik at du kan kjøre AI-applikasjoner uten å være avhengig av skytjenester.

### Historiegenerator for Kjæledyr

**[Historiegenerator for Kjæledyr](petstory/README.md)** er en engasjerende Spring Boot-webapplikasjon som demonstrerer **multimodal AI-prosessering** for å generere kreative historier om kjæledyr. Den kombinerer AI-funksjoner på klient- og serversiden ved hjelp av transformer.js for AI-interaksjoner i nettleseren og OpenAI SDK for prosessering på serversiden.

### MCP Kalkulator-tjeneste (Nybegynnervennlig MCP-demo)

**[MCP Kalkulator-tjeneste](mcp/calculator/README.md)** er en enkel demonstrasjon av **Model Context Protocol (MCP)** ved bruk av Spring AI. Den gir en nybegynnervennlig introduksjon til MCP-konsepter og viser hvordan man lager en grunnleggende MCP-server som samhandler med MCP-klienter.

## Læringsprogresjon

Disse prosjektene er designet for å bygge videre på konsepter fra tidligere kapitler:

1. **Start Enkelt**: Begynn med Foundry Local Spring Boot Demo for å forstå grunnleggende AI-integrasjon med lokale modeller
2. **Legg til Interaktivitet**: Gå videre til Historiegenerator for Kjæledyr for multimodal AI og nettbaserte interaksjoner
3. **Lær MCP-grunnleggende**: Prøv MCP Kalkulator-tjenesten for å forstå grunnleggende prinsipper for Model Context Protocol

## Oppsummering

**Gratulerer!** Du har med suksess:

- **Laget multimodale AI-opplevelser** som kombinerer AI-prosessering på klient- og serversiden
- **Implementert lokal AI-modellintegrasjon** ved bruk av moderne Java-rammeverk og SDK-er
- **Utviklet Model Context Protocol-tjenester** som demonstrerer integrasjonsmønstre for verktøy

## Neste Steg

[Kapittel 5: Ansvarlig Generativ AI](../05-ResponsibleGenAI/README.md)

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.