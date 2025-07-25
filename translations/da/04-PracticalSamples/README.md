<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:40:31+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "da"
}
-->
# Praktiske Anvendelser & Projekter

## Hvad Du Vil Lære
I denne sektion demonstrerer vi tre praktiske anvendelser, der viser udviklingsmønstre for generativ AI med Java:
- Opret en multi-modal Pet Story Generator, der kombinerer AI på klient- og server-siden
- Implementer lokal AI-modelintegration med Foundry Local Spring Boot-demoen
- Udvikl en Model Context Protocol (MCP)-tjeneste med eksemplet Calculator

## Indholdsfortegnelse

- [Introduktion](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Pet Story Generator](../../../04-PracticalSamples)
  - [MCP Calculator Service (Begynder-Venlig MCP Demo)](../../../04-PracticalSamples)
- [Læringsprogression](../../../04-PracticalSamples)
- [Opsummering](../../../04-PracticalSamples)
- [Næste Skridt](../../../04-PracticalSamples)

## Introduktion

Dette kapitel præsenterer **eksempelprojekter**, der demonstrerer udviklingsmønstre for generativ AI med Java. Hvert projekt er fuldt funktionelt og viser specifikke AI-teknologier, arkitektoniske mønstre og bedste praksis, som du kan tilpasse til dine egne applikationer.

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** viser, hvordan man integrerer med lokale AI-modeller ved hjælp af **OpenAI Java SDK**. Det demonstrerer forbindelsen til **Phi-3.5-mini**-modellen, der kører på Foundry Local, hvilket giver dig mulighed for at køre AI-applikationer uden afhængighed af cloud-tjenester.

### Pet Story Generator

**[Pet Story Generator](petstory/README.md)** er en engagerende Spring Boot-webapplikation, der demonstrerer **multi-modal AI-behandling** til at generere kreative historier om kæledyr. Den kombinerer AI på klient- og server-siden ved hjælp af transformer.js til browserbaserede AI-interaktioner og OpenAI SDK til server-side behandling.

### MCP Calculator Service (Begynder-Venlig MCP Demo)

**[MCP Calculator Service](mcp/calculator/README.md)** er en simpel demonstration af **Model Context Protocol (MCP)** ved hjælp af Spring AI. Den giver en begynder-venlig introduktion til MCP-konceptet og viser, hvordan man opretter en grundlæggende MCP-server, der interagerer med MCP-klienter.

## Læringsprogression

Disse projekter er designet til at bygge videre på koncepter fra tidligere kapitler:

1. **Start Simpelt**: Begynd med Foundry Local Spring Boot Demo for at forstå grundlæggende AI-integration med lokale modeller
2. **Tilføj Interaktivitet**: Gå videre til Pet Story Generator for multi-modal AI og webbaserede interaktioner
3. **Lær MCP-Grundprincipper**: Prøv MCP Calculator Service for at forstå fundamentet for Model Context Protocol

## Opsummering

**Tillykke!** Du har med succes:

- **Skabt multi-modal AI-oplevelser**, der kombinerer AI-behandling på klient- og server-siden
- **Implementeret lokal AI-modelintegration** ved hjælp af moderne Java-frameworks og SDK'er
- **Udviklet Model Context Protocol-tjenester**, der demonstrerer værktøjsintegrationsmønstre

## Næste Skridt

[Kapitel 5: Ansvarlig Generativ AI](../05-ResponsibleGenAI/README.md)

**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal det bemærkes, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os ikke ansvar for eventuelle misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.