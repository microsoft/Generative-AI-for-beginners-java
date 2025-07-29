<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "63b6426b88f6f56398ca3f1fbfc30889",
  "translation_date": "2025-07-29T15:38:39+00:00",
  "source_file": "README.md",
  "language_code": "no"
}
-->
# Generativ AI for Nybegynnere - Java Edition
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generativ AI for Nybegynnere - Java Edition](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.no.png)

**Tidsforpliktelse**: Hele workshoppen kan fullføres online uten lokal oppsett. Oppsett av miljøet tar 2 minutter, og utforsking av eksemplene tar 1-3 timer avhengig av hvor dypt du ønsker å gå.

> **Rask Start** 

1. Fork dette repositoriet til din GitHub-konto
2. Klikk **Code** → **Codespaces**-fanen → **...** → **New with options...**
3. Bruk standardinnstillingene – dette vil velge utviklingscontaineren som er laget for dette kurset
4. Klikk **Create codespace**
5. Vent ~2 minutter til miljøet er klart
6. Gå direkte til [Det første eksempelet](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Støtte for Flere Språk

### Støttet via GitHub Action (Automatisk og Alltid Oppdatert)

[Fransk](../fr/README.md) | [Spansk](../es/README.md) | [Tysk](../de/README.md) | [Russisk](../ru/README.md) | [Arabisk](../ar/README.md) | [Persisk (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Kinesisk (Forenklet)](../zh/README.md) | [Kinesisk (Tradisjonell, Macau)](../mo/README.md) | [Kinesisk (Tradisjonell, Hong Kong)](../hk/README.md) | [Kinesisk (Tradisjonell, Taiwan)](../tw/README.md) | [Japansk](../ja/README.md) | [Koreansk](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](../bn/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portugisisk (Portugal)](../pt/README.md) | [Portugisisk (Brasil)](../br/README.md) | [Italiensk](../it/README.md) | [Polsk](../pl/README.md) | [Tyrkisk](../tr/README.md) | [Gresk](../el/README.md) | [Thai](../th/README.md) | [Svensk](../sv/README.md) | [Dansk](../da/README.md) | [Norsk](./README.md) | [Finsk](../fi/README.md) | [Nederlandsk](../nl/README.md) | [Hebraisk](../he/README.md) | [Vietnamesisk](../vi/README.md) | [Indonesisk](../id/README.md) | [Malayisk](../ms/README.md) | [Tagalog (Filippinsk)](../tl/README.md) | [Swahili](../sw/README.md) | [Ungarsk](../hu/README.md) | [Tsjekkisk](../cs/README.md) | [Slovakisk](../sk/README.md) | [Rumensk](../ro/README.md) | [Bulgarsk](../bg/README.md) | [Serbisk (Kyrillisk)](../sr/README.md) | [Kroatisk](../hr/README.md) | [Slovensk](../sl/README.md) | [Ukrainsk](../uk/README.md) | [Burmesisk (Myanmar)](../my/README.md)

## Kursstruktur og Læringssti

### **Kapittel 1: Introduksjon til Generativ AI**
- **Kjernebegreper**: Forståelse av store språkmodeller, tokens, embeddings og AI-kapasiteter
- **Java AI-Økosystem**: Oversikt over Spring AI og OpenAI SDK-er
- **Model Context Protocol**: Introduksjon til MCP og dens rolle i AI-agentkommunikasjon
- **Praktiske Anvendelser**: Virkelige scenarier som chatboter og innholdsgenerering
- **[→ Start Kapittel 1](./01-IntroToGenAI/README.md)**

### **Kapittel 2: Oppsett av Utviklingsmiljø**
- **Konfigurasjon for Flere Leverandører**: Sett opp GitHub-modeller, Azure OpenAI og OpenAI Java SDK-integrasjoner
- **Spring Boot + Spring AI**: Beste praksis for utvikling av AI-applikasjoner for bedrifter
- **GitHub-modeller**: Gratis tilgang til AI-modeller for prototyping og læring (ingen kredittkort nødvendig)
- **Utviklingsverktøy**: Konfigurasjon av Docker-containere, VS Code og GitHub Codespaces
- **[→ Start Kapittel 2](./02-SetupDevEnvironment/README.md)**

### **Kapittel 3: Kjerne Teknikkene i Generativ AI**
- **Prompt Engineering**: Teknikker for optimale AI-modellsvar
- **Embeddings og Vektoroperasjoner**: Implementer semantisk søk og likhetsmatching
- **Retrieval-Augmented Generation (RAG)**: Kombiner AI med dine egne datakilder
- **Funksjonskall**: Utvid AI-kapasiteter med egendefinerte verktøy og plugins
- **[→ Start Kapittel 3](./03-CoreGenerativeAITechniques/README.md)**

### **Kapittel 4: Praktiske Anvendelser og Prosjekter**
- **Pet Story Generator** (`petstory/`): Kreativ innholdsgenerering med GitHub-modeller
- **Foundry Local Demo** (`foundrylocal/`): Lokal AI-modellintegrasjon med OpenAI Java SDK
- **MCP Kalkulator Tjeneste** (`calculator/`): Grunnleggende implementering av Model Context Protocol med Spring AI
- **[→ Start Kapittel 4](./04-PracticalSamples/README.md)**

### **Kapittel 5: Ansvarlig AI-utvikling**
- **GitHub-modellers Sikkerhet**: Test innebygde innholdsfiltrerings- og sikkerhetsmekanismer (harde blokker og myke avslag)
- **Ansvarlig AI-demo**: Praktisk eksempel som viser hvordan moderne AI-sikkerhetssystemer fungerer i praksis
- **Beste Praksis**: Essensielle retningslinjer for etisk AI-utvikling og distribusjon
- **[→ Start Kapittel 5](./05-ResponsibleGenAI/README.md)**

## Ekstra Ressurser 

- [AI-agenter for Nybegynnere](https://github.com/microsoft/ai-agents-for-beginners)
- [Generativ AI for Nybegynnere med .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generativ AI for Nybegynnere med JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generativ AI for Nybegynnere](https://github.com/microsoft/generative-ai-for-beginners)
- [ML for Nybegynnere](https://aka.ms/ml-beginners)
- [Data Science for Nybegynnere](https://aka.ms/datascience-beginners)
- [AI for Nybegynnere](https://aka.ms/ai-beginners)
- [Cybersikkerhet for Nybegynnere](https://github.com/microsoft/Security-101)
- [Webutvikling for Nybegynnere](https://aka.ms/webdev-beginners)
- [IoT for Nybegynnere](https://aka.ms/iot-beginners)
- [XR-utvikling for Nybegynnere](https://github.com/microsoft/xr-development-for-beginners)
- [Mestre GitHub Copilot for AI-parprogrammering](https://aka.ms/GitHubCopilotAI)
- [Mestre GitHub Copilot for C#/.NET-utviklere](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Velg Ditt Eget Copilot-eventyr](https://github.com/microsoft/CopilotAdventures)
- [RAG Chat App med Azure AI-tjenester](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.