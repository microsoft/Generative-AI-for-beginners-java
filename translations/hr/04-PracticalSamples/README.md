<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T12:19:52+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "hr"
}
-->
# Praktične Primjene i Projekti

## Što ćete naučiti
U ovom dijelu prikazat ćemo tri praktične primjene koje demonstriraju obrasce razvoja generativne umjetne inteligencije s Javom:
- Izrada višemodalnog generatora priča o kućnim ljubimcima koji kombinira AI na strani klijenta i poslužitelja
- Implementacija integracije lokalnog AI modela s Foundry Local Spring Boot demonstracijom
- Razvoj usluge Model Context Protocol (MCP) s primjerom Kalkulatora

## Sadržaj

- [Uvod](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Generator priča o kućnim ljubimcima](../../../04-PracticalSamples)
  - [MCP Kalkulator usluga (Demo MCP-a za početnike)](../../../04-PracticalSamples)
- [Napredak u učenju](../../../04-PracticalSamples)
- [Sažetak](../../../04-PracticalSamples)
- [Sljedeći koraci](../../../04-PracticalSamples)

## Uvod

Ovo poglavlje prikazuje **primjere projekata** koji demonstriraju obrasce razvoja generativne umjetne inteligencije s Javom. Svaki projekt je potpuno funkcionalan i prikazuje specifične AI tehnologije, arhitektonske obrasce i najbolje prakse koje možete prilagoditi za vlastite aplikacije.

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** demonstrira kako integrirati lokalne AI modele koristeći **OpenAI Java SDK**. Prikazuje povezivanje s modelom **Phi-3.5-mini** koji radi na Foundry Local, omogućujući pokretanje AI aplikacija bez oslanjanja na cloud usluge.

### Generator priča o kućnim ljubimcima

**[Generator priča o kućnim ljubimcima](petstory/README.md)** je zanimljiva Spring Boot web aplikacija koja demonstrira **višemodalnu AI obradu** za generiranje kreativnih priča o kućnim ljubimcima. Kombinira AI mogućnosti na strani klijenta i poslužitelja koristeći transformer.js za AI interakcije u pregledniku i OpenAI SDK za obradu na strani poslužitelja.

### MCP Kalkulator usluga (Demo MCP-a za početnike)

**[MCP Kalkulator usluga](mcp/calculator/README.md)** je jednostavna demonstracija **Model Context Protocol (MCP)** koristeći Spring AI. Pruža uvod prilagođen početnicima u MCP koncepte, pokazujući kako stvoriti osnovni MCP poslužitelj koji komunicira s MCP klijentima.

## Napredak u učenju

Ovi projekti su osmišljeni tako da se nadovezuju na koncepte iz prethodnih poglavlja:

1. **Počnite jednostavno**: Započnite s Foundry Local Spring Boot Demo kako biste razumjeli osnovnu AI integraciju s lokalnim modelima
2. **Dodajte interaktivnost**: Nastavite s Generatorom priča o kućnim ljubimcima za višemodalnu AI obradu i web interakcije
3. **Naučite osnove MCP-a**: Isprobajte MCP Kalkulator uslugu kako biste razumjeli osnove Model Context Protocol-a

## Sažetak

**Čestitamo!** Uspješno ste:

- **Stvorili višemodalna AI iskustva** kombinirajući AI obradu na strani klijenta i poslužitelja
- **Implementirali integraciju lokalnih AI modela** koristeći moderne Java okvire i SDK-ove
- **Razvili usluge Model Context Protocol-a** koje demonstriraju obrasce integracije alata

## Sljedeći koraci

[5. Poglavlje: Odgovorna generativna umjetna inteligencija](../05-ResponsibleGenAI/README.md)

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni prijevod od strane čovjeka. Ne preuzimamo odgovornost za nesporazume ili pogrešna tumačenja koja mogu proizaći iz korištenja ovog prijevoda.