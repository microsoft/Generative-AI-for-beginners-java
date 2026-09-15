# Peruschat Azure AI Foundryn kanssa – End-to-End-esimerkki

Tämä esimerkki on yksinkertainen Spring Boot -sovellus, joka yhdistää **Azure AI Foundry** -malliin käyttäen **avaimetonta todennusta** (Microsoft Entra ID) ja testaa asennuksesi. Se käyttää Spring AI:n `ChatClient`-luokkaa, jota tukevat **virallinen OpenAI Java SDK** ja **Azure OpenAI v1** -päätepiste.

Versiot tiedostossa [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) ovat Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** ja dotenv-java **3.2.0**. Näyte käyttää `spring-ai-starter-model-openai`-kirjastoa ja määrittelee erikseen `openai-java`- ja `azure-identity`-kirjastot; Spring AI 2 poisti vanhan Azure OpenAI -starterin.

## Sisällysluettelo

- [Esivaatimukset](#esivaatimukset)
- [Nopea aloitus](#nopea-aloitus)
- [Kuinka todennus toimii](#kuinka-todennus-toimii)
- [Sovelluksen ajaminen](#sovelluksen-ajaminen)
  - [Mavenin käyttäminen](#mavenin-käyttö)
  - [VS Code -käyttö](#vs-code-käyttö)
  - [Odotettu tuloste](#odotettu-tuloste)
- [Konfiguroinnin viite](#konfiguraation-viite)
  - [Ympäristömuuttujat](#ympäristömuuttujat)
  - [Spring-konfigurointi](#spring-konfiguraatio)
- [Vianmääritys](#vianmääritys)
  - [Yleiset ongelmat](#yleiset-ongelmat)
  - [Debug-tila](#debug-tila)
- [Seuraavat vaiheet](#seuraavat-vaiheet)
- [Resurssit](#resurssit)

## Esivaatimukset

Ennen tämän esimerkin suorittamista varmista, että sinulla on:

- Azure AI Foundry -resurssi, jossa on `gpt-5.6-luna`-käyttöönotto – ota se käyttöön komennolla `azd up` tai manuaalisesti [Azure AI Foundryn asennusopas](../../getting-started-azure-openai.md)
- Resurssin **Cognitive Services OpenAI User** -rooli (Bicep-mallit määrittävät tämän puolestasi)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), jossa olet kirjautunut sisään komennolla `az login`
- Java 21+ ja Maven 3.9+

> **API-avainta ei tarvita** — todennus on avaimetonta Microsoft Entra ID:n kautta.

## Nopea aloitus

```bash
# 1. Siirry projektiin
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Kirjaudu sisään, jotta avaimetonta todennusta varten voidaan saada token
az login

# 3. Määritä päätepiste
#    - Jos suoritat `azd up`, .env tiedosto kirjoitettiin puolestasi (ohita tämä).
#    - Muussa tapauksessa kopioi malli ja aseta AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Suorita sovellus
mvn spring-boot:run
```

## Kuinka todennus toimii

Tämä esimerkki käyttää todennukseen **Microsoft Entra ID:tä** — API-avainta ei ole.

Sovellus määrittää todennuksen eksplisiittisesti tiedostossa [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` luo `BearerTokenCredential`-olion käyttäen `AuthenticationUtil.getBearerTokenSupplier`-metodia, joka hyödyntää `DefaultAzureCredential`ia ja laajuutta `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` rakentaa `OpenAIClient`-olion käyttäen `OpenAIOkHttpClient.builder()`:a, määrittelee resurssin päätepisteen `/openai/v1`:ksi ja antaa tunnisteen `.credential(...)`-metodilla.
3. `azureChatModel()` antaa tämän asiakkaan Spring AI:n `OpenAiChatModel`-luokalle, joka tukee oppitunnin `ChatClient`-oliota.

Nämä eksplisiittiset bean-määritelmät estävät globaalin `OPENAI_API_KEY`-avaimen korvaamasta Azure-todennusta. Pelkkä API-avaimen jättäminen pois YAML-tiedostosta ei riitä todennuksen määrittämiseen. `DefaultAzureCredential` voi käyttää paikallista `az login` -istuntoasi tai hallittua identiteettiä Azurella; kumpi tahansa valittu identiteetti tarvitsee yllä mainitun resurssiroolin.

## Sovelluksen ajaminen

### Mavenin käyttö

```bash
mvn spring-boot:run
```

### VS Code -käyttö

1. Avaa projekti VS Codessa
2. Paina `F5` tai käytä "Run and Debug" -paneelia
3. Valitse "Spring Boot-BasicChatApplication" -kokoonpano

> **Huom**: Sovellus lataa `.env`-tiedoston nykyisestä hakemistosta, myös kun se käynnistetään VS Codesta.

### Odotettu tuloste

Esimerkinomainen tuloste onnistuneen suorituksen jälkeen (käynnistyslokeja ei näytetä; vastausten muotoilu vaihtelee):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Konfiguraation viite

### Ympäristömuuttujat

| Muuttuja | Kuvaus | Pakollinen | Esimerkki |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundryn (Azure OpenAI) päätepiste-URL | Kyllä | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Chat-mallin käyttöönoton nimi | Ei | `gpt-5.6-luna` (oletus) |

> API-avaintakaan ei ole — todennus on avaimetonta (Microsoft Entra ID käyttäen `az login`).

### Spring-konfiguraatio

Asetukset tiedostossa [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) käyttävät etuliitettä `spring.ai.openai` ja tasoitettuja chat-ominaisuuksia (ei `options`-lohkoa):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` on **Azure-käyttöönoton nimi**. Todennus tulee yllä kuvatuista eksplisiittisistä bean-määrittelyistä, ei `api-key`-asetuksesta. Oppitunti poistaa päättelytoiminnon käytöstä ja rajoittaa täydennysten token-määrän 500:aan; `temperature` ja perinteinen `max-tokens` jätetään määrittämättä.

Microsoft suosittelee [virallista OpenAI SDK:ta Azure OpenAI v1:n ja Responses API:n kanssa uusissa sovelluksissa](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions -toimintoa tuetaan edelleen tässä olemassa olevassa viestipohjaisessa opetusohjelmassa. GPT-5.6:n kohdalla pyynnöt, joissa käytetään työkaluja Chat Completionsissa, on asetettava `reasoning_effort` arvoksi `none`; päättelyn yhdistäminen työkaluin kannattaa tehdä Responses API:n kautta. Katso [työkalukutsut päättelymalleissa](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Vianmääritys

### Yleiset ongelmat

<details>
<summary><strong>Virhe: 401 / "PermissionDenied" / token-virheitä</strong></summary>

- Suorita `az login` — avaimeton todennus tarvitsee aktiivisen kirjautumisen tokenin saamiseksi
- Varmista, että tililläsi on **Cognitive Services OpenAI User** -rooli resurssilla
- Jos rooli on juuri myönnetty, odota minuutti, että muutos rekisteröityy
- Varmista, että olet oikeassa vuokraajassa/tilauksessa (`az account show`)
</details>

<details>
<summary><strong>Virhe: "The endpoint is not valid" / yhteysvirheitä</strong></summary>

- Varmista, että `AZURE_OPENAI_ENDPOINT` on täydellinen perus-URL (esim. `https://your-resource.openai.azure.com/`)
- Tarkista loppuviivan yhdenmukaisuus
- Varmista, että päätepiste vastaa provisioimaasi resurssia (`azd env get-values`)
</details>

<details>
<summary><strong>Virhe: "The deployment was not found"</strong></summary>

- Varmista, että `AZURE_OPENAI_DEPLOYMENT` vastaa Azureen manuaalisesti tehtyä käyttöönoton nimeä
- Varmista, että malli on oikein otettu käyttöön ja aktiivinen
- Oletuskäyttöönoton nimi on `gpt-5.6-luna`
</details>

<details>
<summary><strong>Virhe: 429 / rajanopeuden ylitys</strong></summary>

- Oletus GPT-5.6 Luna -käyttöönotossa on kapasiteetti Global Standard 10: 10 pyyntöä/min ja 10 000 tokenia/min
- Suorita esimerkit peräkkäin ja odota palvelun uudelleenyritysväli ennen uusintaa
- Tämä perusesimerkki poistaa automaattiset SDK-yritykset, joten epäonnistuneet pyynnöt raportoidaan heti
</details>

<details>
<summary><strong>VS Code: Ympäristömuuttujat eivät lataudu</strong></summary>

- Varmista, että `.env`-tiedosto on projektin juurihakemistossa (samalla tasolla kuin `pom.xml`)
- Kokeile ajaa `mvn spring-boot:run` VS Coden integroidussa terminaalissa
- Tarkista, että VS Code Java -laajennus on asennettu oikein
</details>

### Debug-tila

Tarkempaa lokitusta varten poista kommentti näiltä riveiltä tiedostossa [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Seuraavat vaiheet

**Asennus valmis!** Jatka oppimismatkaasi:

[Luku 3: Keskeiset Generatiivisen AI:n tekniikat](../../../03-CoreGenerativeAITechniques/README.md)

## Resurssit

- [Spring AI 2 OpenAI Java SDK -siirtymä](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Virallinen OpenAI Java SDK Azure OpenAI v1:n kanssa](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Avaimeton todennus Microsoft Entra ID:llä](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry -portaali](https://ai.azure.com/)
- [Azure AI Foundry -dokumentaatio](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->