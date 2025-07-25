<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:45:08+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "fi"
}
-->
# Käytännön sovellukset ja projektit

## Mitä opit
Tässä osiossa esittelemme kolme käytännön sovellusta, jotka havainnollistavat generatiivisen tekoälyn kehitysmalleja Javalla:
- Luodaan monimodaalinen lemmikkitarinageneraattori, joka yhdistää asiakas- ja palvelinpuolen tekoälyn
- Toteutetaan paikallisen tekoälymallin integrointi Foundry Local Spring Boot -demon avulla
- Kehitetään Model Context Protocol (MCP) -palvelu laskinesimerkin avulla

## Sisällysluettelo

- [Johdanto](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot -demo](../../../04-PracticalSamples)
  - [Lemmikkitarinageneraattori](../../../04-PracticalSamples)
  - [MCP-laskinpalvelu (aloittelijaystävällinen MCP-demo)](../../../04-PracticalSamples)
- [Oppimispolku](../../../04-PracticalSamples)
- [Yhteenveto](../../../04-PracticalSamples)
- [Seuraavat askeleet](../../../04-PracticalSamples)

## Johdanto

Tässä luvussa esitellään **esimerkkiprojekteja**, jotka havainnollistavat generatiivisen tekoälyn kehitysmalleja Javalla. Jokainen projekti on täysin toimiva ja esittelee tiettyjä tekoälyteknologioita, arkkitehtuurimalleja ja parhaita käytäntöjä, joita voit soveltaa omiin sovelluksiisi.

### Foundry Local Spring Boot -demo

**[Foundry Local Spring Boot -demo](foundrylocal/README.md)** näyttää, kuinka paikallisia tekoälymalleja voidaan integroida **OpenAI Java SDK:n** avulla. Se esittelee yhteyden **Phi-3.5-mini**-malliin, joka toimii Foundry Local -ympäristössä, mahdollistaen tekoälysovellusten käytön ilman pilvipalveluita.

### Lemmikkitarinageneraattori

**[Lemmikkitarinageneraattori](petstory/README.md)** on mukaansatempaava Spring Boot -verkkosovellus, joka demonstroi **monimodaalista tekoälyn käsittelyä** luovien lemmikkitarinoiden tuottamiseksi. Se yhdistää asiakas- ja palvelinpuolen tekoälyominaisuudet käyttämällä transformer.js-kirjastoa selaimessa tapahtuvaan tekoälyvuorovaikutukseen ja OpenAI SDK:ta palvelinpuolen käsittelyyn.

### MCP-laskinpalvelu (aloittelijaystävällinen MCP-demo)

**[MCP-laskinpalvelu](mcp/calculator/README.md)** on yksinkertainen esimerkki **Model Context Protocol (MCP)** -protokollasta Spring AI:n avulla. Se tarjoaa aloittelijaystävällisen johdatuksen MCP-konsepteihin ja näyttää, kuinka luodaan perus-MCP-palvelin, joka kommunikoi MCP-asiakkaiden kanssa.

## Oppimispolku

Nämä projektit on suunniteltu rakentumaan aiempien lukujen käsitteiden päälle:

1. **Aloita yksinkertaisesti**: Aloita Foundry Local Spring Boot -demosta ymmärtääksesi paikallisten mallien perusintegraation
2. **Lisää vuorovaikutteisuutta**: Siirry Lemmikkitarinageneraattoriin oppiaksesi monimodaalista tekoälyä ja verkkopohjaisia vuorovaikutuksia
3. **Opi MCP:n perusteet**: Kokeile MCP-laskinpalvelua ymmärtääksesi Model Context Protocolin peruskäsitteet

## Yhteenveto

**Onnittelut!** Olet onnistuneesti:

- **Luonut monimodaalisia tekoälykokemuksia**, jotka yhdistävät asiakas- ja palvelinpuolen tekoälyn käsittelyn
- **Toteuttanut paikallisen tekoälymallin integroinnin** nykyaikaisilla Java-kehyksillä ja SDK:illa
- **Kehittänyt Model Context Protocol -palveluita**, jotka havainnollistavat työkalujen integrointimalleja

## Seuraavat askeleet

[5. luku: Vastuullinen generatiivinen tekoäly](../05-ResponsibleGenAI/README.md)

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä AI-käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäisellä kielellä tulisi pitää ensisijaisena lähteenä. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.