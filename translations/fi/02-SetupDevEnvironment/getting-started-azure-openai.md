# Azure AI Foundryn kehitysympäristön pystyttäminen

> Tämä opas määrittää **Azure AI Foundry** -mallit tämän kurssin Java AI -sovelluksiin käyttäen **avainvapaata** tunnistautumista (Microsoft Entra ID) — ei API-avaimien hallintaa. Uusi työkaluissa? Aloita [kehitysympäristön oppaasta](./README.md).

Tämä opas määrittää **Azure AI Foundry** -mallit tämän kurssin Java AI -sovelluksiin. Sinulla on kaksi vaihtoehtoa:

- **Vaihtoehto A — Ota käyttöön `azd` + Bicepillä (suositus):** yhdellä komennolla Foundry-tili ja mallit koodina. Ei portaalin klikkailua.
- **Vaihtoehto B — Luo resurssit manuaalisesti** Azure AI Foundry -portaalissa.

Molemmat polut käyttävät **avainvapaata tunnistautumista** (Microsoft Entra ID) — ei kopioitavia tai vuotavia API-avaimia.

## Sisällysluettelo

- [Mitä luodaan](#mitä-luodaan)
- [Esivaatimukset](#esivaatimukset)
- [Vaihtoehto A: Käytä azd + Bicep (suositus)](#option-a-provision-with-azd--bicep-recommended)
- [Vaihtoehto B: Luo resurssit manuaalisesti](#vaihtoehto-b-luo-resurssit-manuaalisesti)
- [Määritä ympäristösi](#määritä-ympäristösi)
- [Testaa asennus](#testaa-asennus)
- [Mitä seuraavaksi?](#mitä-seuraavaksi)
- [Resurssit](#resurssit)
- [Lisäresurssit](#lisäresurssit)

## Mitä luodaan

[`infra/`](../../../02-SetupDevEnvironment/infra) Bicep-mallit määrittävät:

- **Azure AI Foundry** -tilin (`Microsoft.CognitiveServices/accounts`, tyyppi `AIServices`) projektin kanssa
- Chat-käyttöönoton - GPT-5.6 Luna (`gpt-5.6-luna`), versio `2026-07-09`, kapasiteetti `GlobalStandard` `10` (10 pyyntöä/minuutti ja 10 000 tokenia/minuutti tälle mallille)
- Upotuskäyttöönoton - `text-embedding-3-small`, versio `1` (käytössä myöhemmissä luvuissa)
- Avainvapaa rooliasetus (`Cognitive Services OpenAI User`), jotta kirjaudut sisään `az login` -komennolla ilman avaimien hallintaa

## Esivaatimukset

- [Azure-tilaus](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) ja [Maven 3.9+](https://maven.apache.org/download.cgi)

## Vaihtoehto A: Käytä azd + Bicep (suositus)

Siirry `02-SetupDevEnvironment`-kansioon:

```bash
cd 02-SetupDevEnvironment

# Kirjaudu sisään (molemmat työkalut)
azd auth login
az login

# Tarjoa Foundry-tili + mallien käyttöönotot
azd up
```

`azd` pyytää **ympäristön nimeä** (esimerkiksi `genai-java`), **tilausta** ja **aluetta**. Valitse oma tilauksesi ja alue, josta löytyvät `gpt-5.6-luna` ja `text-embedding-3-small`, esimerkiksi `eastus2`. Varmista, että tilauksella on riittävä kiintiö mallille ja käyttöönotolle kyseisellä alueella; saatavuus ja kiintiöt vaihtelevat tilauksen mukaan.

Kun käyttöönotto on valmis, azd:

1. Ottaa käyttöön kaiken, mikä on määritelty tiedostossa [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Suorittaa jälkikäyttöönoton hookin, joka kirjoittaa tiedoston [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) sisältämään endpointisi ja käyttöönoton nimet (ei salaisuuksia).

> **Vinkki:** Suorita `azd up` uudelleen milloin tahansa muutosten soveltamiseksi. Suorita `azd down` poistaaksesi kaiken ja lopettaaksesi kulut.

Katso luotuja asetuksia:

```bash
azd env get-values
```

Siirry nyt kohtaan [Testaa asennus](#testaa-asennus).

## Vaihtoehto B: Luo resurssit manuaalisesti

Haluatko käyttää portaalin kautta? Luo resurssit käsi kädessä:

1. Mene [Azure AI Foundry -portaaliin](https://ai.azure.com/) ja kirjaudu sisään.
2. **Luo projekti** (tämä luo myös AI Foundry -resurssin). Anna sille nimi kuten `GenAIJava`.
3. Avaa projektissasi **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Ota käyttöön **GPT-5.6 Luna** (mallin ja käyttöönoton nimi `gpt-5.6-luna`, versio `2026-07-09`) kapasiteetilla **Global Standard** `10`. Toista toimenpide **text-embedding-3-small** -mallille, versio `1`, jos haluat upotus-esimerkit.
5. **Overview**-näkymästä kopioi **endpoint** (esimerkiksi `https://<resource>.openai.azure.com/`).
6. Anna itsellesi avainvapaa pääsy: avaa resurssissa **Access control (IAM)** → **Add role assignment** → määritä rooli **Cognitive Services OpenAI User** tilillesi.

> **Onko ongelmia?** Tutustu [Azure AI Foundryn dokumentaatioon](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Määritä ympäristösi

**Jos käytit Vaihtoehto A:ta (`azd up`)**, asetustiedosto on jo kirjoitettu — mitään ei tarvitse konfiguroida. Siirry kohtaan [Testaa asennus](#testaa-asennus).

**Jos käytit Vaihtoehto B:tä (manuaalinen)**, luo esimerkin `.env`-tiedosto itse:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Muokkaa `.env`-tiedostoa omalla endpointillasi (ei avainta — tunnistautuminen on avainvapaata):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Käytä resurssin Azure OpenAI -endpointia, ei projektin URL-osoitetta. basic-chat-sovellus käyttää sitä `/openai/v1`-polkuun ja määrittää eksplisiittisen bearer-token clientin; API-avainta ei tarvita.

> **Turvallisuusmuistutus:** API-avainta ei säilytetä. Tunnistaudut Microsoft Entra ID:n kautta `az login` -komennolla (paikallisesti) tai hallitulla identiteetillä (Azuren sisällä). `.env`-tiedosto sisältää vain ei-salaisia asetuksia ja on jo suojattu `.gitignore`-tiedostolla.

## Testaa asennus

Varmista, että olet kirjautunut sisään, jotta avainvapaa tunnistus voi hakea tokenin, ja suorita esimerkki:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # jos et ole vielä kirjautunut sisään
mvn clean spring-boot:run
```

Näet vastauksen `gpt-5.6-luna` -mallilta. Suorita esimerkit peräkkäin pysyäksesi pienemmän oletuskiintiön sisällä; jos saat HTTP 429 -virheen, odota uudelleenyrittämisen aikaväli ennen kuin yrität uudelleen.

> **VS Code -käyttäjille:** Paina `F5` käynnistääksesi. Sovellus lataa automaattisesti `.env`-tiedostosi.

> **Täysi esimerkki:** Katso [Basic Chat with Azure AI Foundry -esimerkki](./examples/basic-chat-azure/README.md) lisätietoja ja vianmääritystä varten.

## Mitä seuraavaksi?

Kun käyttöönotto on valmis ja esimerkki toimii, sinulla on:
- Azure AI Foundry ja `gpt-5.6-luna` ja `text-embedding-3-small` käyttöön otettuna
- Avainvapaa tunnistautuminen (Microsoft Entra ID) — ilman avaimien hallintaa
- Paikallinen `.env`-tiedosto endpointilla ja käyttöönottojen nimillä
- Java-kehitysympäristö valmiina käyttöön

**Jatka lukemista luvusta** [Luku 3: Keskeiset generatiivisen tekoälyn tekniikat](../03-CoreGenerativeAITechniques/README.md) aloittaaksesi tekoälysovellusten rakentamisen!

## Resurssit

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Avainvapaa tunnistus Microsoft Entra ID:llä](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundryn dokumentaatio](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK:n siirtymä](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Virallinen OpenAI Java SDK Azure OpenAI v1:llä](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Lisäresurssit

- [Lataa VS Code](https://code.visualstudio.com/Download)
- [Hanki Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container -konfiguraatio](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->