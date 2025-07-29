<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T15:44:21+00:00",
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
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Lemmikkitarinageneraattori](../../../04-PracticalSamples)
  - [MCP-laskinpalvelu (aloittelijaystävällinen MCP-demo)](../../../04-PracticalSamples)
- [Oppimispolku](../../../04-PracticalSamples)
- [Yhteenveto](../../../04-PracticalSamples)
- [Seuraavat askeleet](../../../04-PracticalSamples)

## Johdanto

Tässä luvussa esitellään **esimerkkiprojekteja**, jotka havainnollistavat generatiivisen tekoälyn kehitysmalleja Javalla. Jokainen projekti on täysin toimiva ja esittelee tiettyjä tekoälyteknologioita, arkkitehtuurimalleja ja parhaita käytäntöjä, joita voit soveltaa omiin sovelluksiisi.

### Foundry Local Spring Boot Demo

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** havainnollistaa, kuinka paikallisia tekoälymalleja voidaan integroida **OpenAI Java SDK:n** avulla. Se esittelee yhteyden **Phi-3.5-mini**-malliin, joka toimii Foundry Local -ympäristössä, mahdollistaen tekoälysovellusten käytön ilman pilvipalveluita.

### Lemmikkitarinageneraattori

**[Lemmikkitarinageneraattori](petstory/README.md)** on mukaansatempaava Spring Boot -verkkosovellus, joka demonstroi **monimodaalista tekoälyn käsittelyä** luovien lemmikkitarinoiden tuottamiseksi. Se yhdistää asiakaspuolen ja palvelinpuolen tekoälyominaisuudet käyttämällä transformer.js-kirjastoa selaimessa tapahtuvaan tekoälyvuorovaikutukseen ja OpenAI SDK:ta palvelinpuolen käsittelyyn.

### MCP-laskinpalvelu (aloittelijaystävällinen MCP-demo)

**[MCP-laskinpalvelu](calculator/README.md)** on yksinkertainen esimerkki **Model Context Protocol (MCP)** -protokollasta Spring AI:n avulla. Se tarjoaa aloittelijaystävällisen johdannon MCP-konsepteihin ja näyttää, kuinka luodaan perus-MCP-palvelin, joka vuorovaikuttaa MCP-asiakkaiden kanssa.

## Oppimispolku

Nämä projektit on suunniteltu rakentumaan aiempien lukujen käsitteiden päälle:

1. **Aloita yksinkertaisesti**: Aloita Foundry Local Spring Boot -demosta ymmärtääksesi paikallisten mallien perusintegraation
2. **Lisää vuorovaikutteisuutta**: Siirry lemmikkitarinageneraattoriin oppiaksesi monimodaalista tekoälyä ja verkkopohjaista vuorovaikutusta
3. **Opi MCP:n perusteet**: Kokeile MCP-laskinpalvelua ymmärtääksesi Model Context Protocol -protokollan perusperiaatteet

## Yhteenveto

Hienoa työtä! Olet nyt tutustunut muutamiin todellisiin sovelluksiin:

- Monimodaalisiin tekoälykokemuksiin, jotka toimivat sekä selaimessa että palvelimella
- Paikallisten tekoälymallien integrointiin modernien Java-kehysten ja SDK:iden avulla
- Ensimmäiseen Model Context Protocol -palveluusi, joka näyttää, kuinka työkalut integroituvat tekoälyyn

## Seuraavat askeleet

[5. luku: Vastuullinen generatiivinen tekoäly](../05-ResponsibleGenAI/README.md)

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäisellä kielellä tulisi katsoa ensisijaiseksi lähteeksi. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.