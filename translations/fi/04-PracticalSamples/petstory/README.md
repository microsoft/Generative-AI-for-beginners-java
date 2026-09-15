# Lemmikkitarinageneraattorin opas aloittelijoille

Lataa lemmikkikuva, analysoi se GPT-5.6 Luna -mallilla ja luo tarina saatujen kuvausten perusteella. Molemmissa mallipyyntöissä käytetään `reasoning_effort: none`.

| Komponentti | Versio |
| --- | --- |
| Java | Versio 21 tai uudempi |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Sisällys

- [Esivaatimukset](#esivaatimukset)
- [Projektin rakenteen ymmärtäminen](#projektin-rakenteen-ymmärtäminen)
- [Ydinkomponentit selitettynä](#ydinkomponentit-selitettynä)
  - [1. Pääsovellus](#1-pääsovellus)
  - [2. Web-kontrolleri](#2-web-kontrolleri)
  - [3. Tarinapalvelu](#3-tarinapalvelu)
  - [4. Web-pohjat](#4-web-pohjat)
  - [5. Konfiguraatio](#5-konfiguraatio)
- [Sovelluksen käynnistäminen](#sovelluksen-käynnistäminen)
- [Offline-testit](#offline-testit)
- [Miten kaikki toimii yhteen](#miten-kaikki-toimii-yhteen)
- [AI-integraation ymmärtäminen](#ai-integraation-ymmärtäminen)
- [Seuraavat askeleet](#seuraavat-askeleet)

## Esivaatimukset

Ennen aloittamista varmista, että sinulla on:
- Asennettuna Java versio 21 tai uudempi
- Maven riippuvuuksien hallintaan
- Azure AI Foundryn käyttöönotto GPT-5.6 Luna nimellä `gpt-5.6-luna`, tai `AZURE_OPENAI_DEPLOYMENT` -ylikirjoitus osoittamaan kyseiseen käyttöönottoon. Katso [Luku 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) käyttöönotosta ja kirjaudu sisään `az login`-komennolla avaimettomaan todennukseen. Käyttöönoton täytyy tukea kuvan syötettä ja `reasoning_effort: none`.
- Perustiedot Javasta, Spring Bootista ja web-kehityksestä

## Projektin rakenteen ymmärtäminen

Lemmikkitarinaprojektissa on useita tärkeitä tiedostoja:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Ydinkomponentit selitettynä

### 1. Pääsovellus

**Tiedosto:** `PetStoryApplication.java`

Tämä on Spring Boot -sovelluksemme sisäänkäyntipiste:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Mitä tämä tekee:**
- `@SpringBootApplication`-annotaatio mahdollistaa automaattisen konfiguroinnin ja komponenttien skannauksen
- Käynnistää upotetun web-palvelimen (Tomcat) portissa 8080
- Luo automaattisesti kaikki tarvittavat Spring beanit ja palvelut

### 2. Web-kontrolleri

**Tiedosto:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Päätepiste | Pyyntö | Onnistunut vastaus |
| --- | --- | --- |
| `GET /` | Ei sisältöä | HTML-latauslomake CSRF-tokenilla |
| `POST /analyze-image` | `multipart/form-data`, tiedostokenttä `image` | JSON: `{"description":"Leikkisä lemmikki..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, kenttä `description` | HTML-tulos-sivu kuvauksen ja generoidun tarinan kanssa |

Molemmat POST-päätepisteet vaativat istuntokakun ja CSRF-tokenin, jotka saadaan `GET /` -kutsusta. Lähetysskripti lähettää piilotetun `_csrf`-arvon `X-CSRF-TOKEN`-otsikossa; tarinan lähetys lähettää sen `_csrf`-lomakekenttänä. API-asiakkaiden on säilytettävä kakku pyyntöjen välillä. Nämä ovat lomakepäätepisteitä, eivät JSON-pyyntöpisteitä.

Kuvaukset eivät saa olla tyhjiä ja pisin sallittu pituus on 1000 merkkiä. Kontrolleri trimmää kuvauksen ja poistaa ennen palvelulle siirtoa merkit `<`, `>`, lainausmerkit, heittomerkit ja `&`. Tulosmalli myös suodattaa mallin tuottaman sisällön `th:text`-attribuutilla.

Kuvan validointivirheet palauttavat HTTP 400 -virhekoodin `error`-kentällä; mallivirheet palauttavat HTTP 502 `error`-kentällä ilman `description`-kenttää. Virheelliset tarinakuvaustiedot tai mallivirheet ohjaavat takaisin osoitteeseen `/` näkyvän virheilmoituksen kanssa. Puuttuvat vaaditut kentät palauttavat HTTP 400, ja puuttuvat tai virheelliset CSRF-tokenit palauttavat HTTP 403. Palautetta ei anneta oletuskuvauksina tai tarinoina onnistuneena AI-tuloksena.

### 3. Tarinapalvelu

**Tiedosto:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Virallinen OpenAI Java SDK 4.63.1 kutsuu Azure AI Foundryn OpenAI-yhteensopivaa Chat Completions APIa. Azure Identity 1.18.6 toimittaa Microsoft Entra -paikantunnuksen `DefaultAzureCredential`in kautta; API-avainta ei tarvita.

| Toiminto | Syöte | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Kuvan tavut base64-koodattuna data-URL-muodossa ladatun MIME-tyypin kanssa | 300 |
| `generateStory` | Lemmikin kuvaus käyttäjän viestissä | 800 |

Molemmissa pyynnöissä käytetään määriteltyä käyttöönottoa, oletuksena `gpt-5.6-luna`, ja asetetaan selkeästi `ReasoningEffort.NONE` (`reasoning_effort: none`). Kumpikaan pyyntö ei lähetä `temperature`- tai vanhaa `max_tokens`-parametria.

Kuvan analyysi hyväksyy JPEG-, PNG-, GIF- ja WebP-muodot, hylkää tyhjät kuvat ja yli 10 Mt tiedostot, ja rajoittaa tuloksena olevan kuvauksen 1000 merkkiin. Tarinakehotteessa pyydetään perheystävällistä lyhyttä tarinaa. Tyhjät vaihtoehdot tai tyhjä mallisisältö ovat virheitä, ja virheet säilyttävät alkuperäisen syyn palvelinpuolen diagnostiikkaa varten. SDK-asiakas suljetaan, kun sovellus sammutetaan.

### 4. Web-pohjat

**Tiedosto:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Latauslomake)

Sivulla aloitetaan kuvavalitsimella, ei kuvauskentällä. **Analyze Image** näyttää valitun kuvan esikatselun ja lähettää sen `/analyze-image`-osoitteeseen. Onnistunut vastaus näyttää kuvauksen, täyttää piilotetun `description`-kentän ja paljastaa **Generate Story** -napin. Tuo painike lähettää olemassa olevan lomakkeen osoitteeseen `/generate-story`.

Selaimessa ei ole mallin latausta tai CDN-riippuvuutta. Kuvan analyysi suoritetaan palvelimella määritellyn Azure-käyttöönoton kautta. Virheet näkyvät eivätkä mahdollista tarinageneraatiota keksityllä kuvauksella. Toisen tiedoston valinta tyhjentää edellisen analyysin.

**Tiedosto:** `result.html` (Tarinan näyttö)

Näyttää generoidun tarinan:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Mallin ominaisuudet:**

1. **Thymeleaf-integraatio**: Käyttää `th:`-attribuutteja dynaamiseen sisältöön
2. **Responsiivinen suunnittelu**: CSS-tyylittely mobiili- ja työpöytäkatselua varten
3. **Virheiden käsittely**: Näyttää validointivirheet käyttäjille
4. **Latauksen käsittely**: JavaScript näyttää kuvan esikatselun, lähettää CSRF-suojatun multipart-pyynnön ja näyttää palautetun kuvauksen

### 5. Konfiguraatio

**Tiedosto:** `application.properties`

Sovelluksen konfigurointiasetukset:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Konfiguraation selitys:**

1. **Tiedoston lataus**: Sekä tiedoston että koko multipart-pyynnön koko on rajattu 10Mt; pidä valokuvat tämän rajan alapuolella multipart-otsikoiden varalle
2. **Lokitus**: Hallitsee suorituksen aikana tallennettavaa tietoa
3. **Azure AI Foundry**: Määrittää käytettävän endpointin ja mallin käyttöönoton (avainvapaa todennus)
4. **Tietoturva**: CSRF-suojaus on päällä; mallin diagnostiikka kirjataan palvelimella, kun kontrolleri näyttää geneeriset mallivirheviestit

## Sovelluksen käynnistäminen

### Vaihe 1: Kirjaudu sisään ja määritä endpoint

Todennus on avaimetonta (Microsoft Entra ID), joten API-avainta ei ole. Kirjaudu sisään ja aseta Foundryn endpoint:

**Windows (Komentokehote):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Miksi tämä on tarpeen:**
- Azure AI Foundry käyttää Microsoft Entraa todennukseen inferenssipyyntöihin
- Avaimeton todennus tarkoittaa, ettei salaisuuksia ole lähdekoodissa tai ympäristössä
- Tililläsi tulee olla **Cognitive Services OpenAI User** -rooli resurssissa

Oletuskäyttöönottotunnus on `gpt-5.6-luna`. Jos GPT-5.6 Luna -käyttöönotollasi on eri nimi, aseta `AZURE_OPENAI_DEPLOYMENT` samassa terminaalissa ennen sovelluksen käynnistämistä. Sekä kuvan analyysi että tarinagenerointi käyttävät tätä asetusta.

### Vaihe 2: Käännä ja käynnistä

Siirry projektin hakemistoon:
```bash
cd 04-PracticalSamples/petstory
```

Käännä itsenäinen suoritettava JAR ja aja kaikki offline-testit:
```bash
mvn clean package
```

Käynnistä palvelin:
```bash
mvn spring-boot:run
```

Sovellus käynnistyy osoitteessa `http://localhost:8080`.

Vaihtoehtoisesti käynnistä pakattu JAR vapaalla portilla, esimerkiksi:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Tällöin avaa `http://localhost:8083/`. Samat reitit `/analyze-image` ja `/generate-story` ovat käytettävissä valitulla portilla.

### Vaihe 3: Testaa sovellus

1. **Avaa** `http://localhost:8080` selaimessa
2. **Valitse** selkeä lemmikkikuva JPEG-, PNG-, GIF- tai WebP-muodossa, alle 10 Mt
3. **Napsauta** "Analyze Image" ja odota kuvauksen valmistumista
4. **Napsauta** "Generate Story" onnistuneen analyysin jälkeen
5. **Katsele** tarinaa ja käytä tulossivun linkkiä palataksesi latauslomakkeeseen

Onnistunut kuva-tarina -prosessi tekee kaksi mallikutsua, yhden per painike. Live-inferenssi kuluttaa käyttöönoton määrärahaa ja voi aiheuttaa kuluja; suorita savutestit sarjassa jaaessasi käyttöönottoa, jolla on rajattu pyyntömäärä. Kotisivun lataus ei kutsu mallia.

## Offline-testit

Näytteiden hakemistosta suorita:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) tallentaa oikeat OpenAI SDK -pyynnöt kierto-HTTP-fixtuurilla. Se tarkistaa molempien pyyntöjen käyttöönoton, `reasoning_effort: none`, token-rajoitukset, kuvalatauksen, syötteen validoinnin, tyhjät vastaukset ja ylöspäin menevät virheet.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) käyttää MockMvcä mallipalvelun simulointiin Thymeleaf-sivujen, lataussopimuksen, CSRF:n, validoinnin, lähdön suodatuksen ja näkyvien virheiden testaamiseen. Näissä testeissä ei tarvita Azure-tunnuksia, eikä ne kutsu maksettua Azure-inferenssiä. Maven kirjoittaa Surefire-raportit kansioon `target/surefire-reports`.

## Miten kaikki toimii yhteen

Tässä on koko prosessi lemmikkitarinan generoinnissa:

1. **Kuvan valinta**: Valitset lemmikkikuvan latauslomakkeessa
2. **Kuvan lataus**: "Analyze Image" lähettää multipart-POST-pyynnön `/analyze-image` -osoitteeseen CSRF-otsikolla
3. **Kuvan analysointi**: `StoryService` lähettää kuvan GPT-5.6 Lunalta saadulle mallille, päättely asetettu `none`
4. **Kuvauksen näyttö**: Selain näyttää palautetun kuvauksen ja tallentaa sen lomakkeeseen
5. **Tarinan lähetys**: "Generate Story" lähettää `description`- ja `_csrf`-kentät `/generate-story` -osoitteeseen
6. **Tarinan luonti**: Kontrolleri validoi kuvauksen ja kutsuu samaa käyttöönottoa, päättelyllä `none`
7. **Mallin renderöinti**: Thymeleaf suodattaa ja näyttää kuvauksen sekä tarinan tulossivulla

**Virheenkäsittely:**
Jos malli epäonnistuu, palvelin kirjaa syyn. Kuvan analyysi palauttaa HTTP 502 ja selain näyttää virheen piilottaen "Generate Story" -napin. Tarinagenerointi ohjaa lomakkeelle virheilmoituksen kanssa. Kumpikaan polku ei hiljaisesti korvaa tulosta esikirjoitetulla.

## AI-integraation ymmärtäminen

### Azure AI Foundry (avaimeton)
Palvelu konfiguroi SDK:n resurssisi `/openai/v1/` endpointille. `DefaultAzureCredential` ja `AuthenticationUtil.getBearerTokenSupplier` toimittavat Microsoft Entra -tunnuksia `https://ai.azure.com/.default` -kohteeseen. Paikallisessa kehityksessä voidaan käyttää Azure CLI:n kirjautumista; Azure-isännöity sovellus voi käyttää hallittua tunnusta tarvittavilla oikeuksilla.

### Promptin suunnittelu
Kuvan analyysi pyytää havaittavia lemmikkiominaisuuksia lyhyessä kappaleessa ja ohjeistaa mallia käsittelemään kuvan tekstiä datana, ei ohjeina. Tarinagenerointi käyttää palautettua kuvausta erillisessä, perheystävällisessä kirjoituspyynnössä. Kumpikaan kutsu ei aktivoi päättelyä tai aseta lämpötilan ylitystä.

### Vastauksen käsittely
Yhteinen vastauskäsittelijä hylkää puuttuvat vaihtoehdot ja tyhjän tai vain välilyöntejä sisältävän sisällön, trimaa validin sisällön ja säilyttää ylöspäin menevät virheet. Kuvakuvaukset rajataan 1000 merkkiin, jotta ne soveltuvat seuraavaan tarinalomakkeeseen. Alkuperäinen mallivirhe säilytetään diagnostisia tarkoituksia varten, mutta sitä ei näytetä käyttäjälle.

## Seuraavat askeleet

Lisäesimerkkejä löydät osoitteesta [Luku 04: Käytännön esimerkit](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->