<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:45:25+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "fi"
}
-->
# Foundry Local Spring Boot -opas

## Sisällysluettelo

- [Edellytykset](../../../../04-PracticalSamples/foundrylocal)
- [Projektin yleiskuvaus](../../../../04-PracticalSamples/foundrylocal)
- [Koodin ymmärtäminen](../../../../04-PracticalSamples/foundrylocal)
  - [1. Sovelluksen konfiguraatio (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Pääsovellusluokka (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI-palvelukerros (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Projektin riippuvuudet (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Kuinka kaikki toimii yhdessä](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Localin asennus](../../../../04-PracticalSamples/foundrylocal)
- [Sovelluksen suorittaminen](../../../../04-PracticalSamples/foundrylocal)
- [Odotettu tulos](../../../../04-PracticalSamples/foundrylocal)
- [Seuraavat askeleet](../../../../04-PracticalSamples/foundrylocal)
- [Vianmääritys](../../../../04-PracticalSamples/foundrylocal)

## Edellytykset

Ennen kuin aloitat tämän oppaan, varmista että sinulla on:

- **Java 21 tai uudempi** asennettuna järjestelmääsi
- **Maven 3.6+** projektin rakentamiseen
- **Foundry Local** asennettuna ja käynnissä

### **Asenna Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Projektin yleiskuvaus

Tämä projekti koostuu neljästä pääkomponentista:

1. **Application.java** - Spring Boot -sovelluksen pääpiste
2. **FoundryLocalService.java** - Palvelukerros, joka käsittelee AI-kommunikaatiota
3. **application.properties** - Konfiguraatio Foundry Local -yhteyttä varten
4. **pom.xml** - Maven-riippuvuudet ja projektin asetukset

## Koodin ymmärtäminen

### 1. Sovelluksen konfiguraatio (application.properties)

**Tiedosto:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Mitä tämä tekee:**
- **base-url**: Määrittää, missä Foundry Local toimii (oletusportti 5273)
- **model**: Nimeää AI-mallin, jota käytetään tekstin generointiin

**Keskeinen käsite:** Spring Boot lataa nämä asetukset automaattisesti ja tekee ne saataville sovellukselle `@Value`-annotaation avulla.

### 2. Pääsovellusluokka (Application.java)

**Tiedosto:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Mitä tämä tekee:**
- `@SpringBootApplication` mahdollistaa Spring Bootin automaattisen konfiguraation
- `WebApplicationType.NONE` kertoo Springille, että kyseessä on komentorivisovellus, ei verkkopalvelin
- Päämetodi käynnistää Spring-sovelluksen

**Demo Runner:**
```java
@Bean
public CommandLineRunner foundryLocalRunner(FoundryLocalService foundryLocalService) {
    return args -> {
        System.out.println("=== Foundry Local Demo ===");
        
        String testMessage = "Hello! Can you tell me what you are and what model you're running?";
        System.out.println("Sending message: " + testMessage);
        
        String response = foundryLocalService.chat(testMessage);
        System.out.println("Response from Foundry Local:");
        System.out.println(response);
    };
}
```

**Mitä tämä tekee:**
- `@Bean` luo komponentin, jota Spring hallinnoi
- `CommandLineRunner` suorittaa koodia Spring Bootin käynnistymisen jälkeen
- `foundryLocalService` injektoidaan automaattisesti Springin toimesta (riippuvuuksien injektio)
- Lähettää testiviestin AI:lle ja näyttää vastauksen

### 3. AI-palvelukerros (FoundryLocalService.java)

**Tiedosto:** `src/main/java/com/example/FoundryLocalService.java`

#### Konfiguraation injektio:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Mitä tämä tekee:**
- `@Service` kertoo Springille, että tämä luokka tarjoaa liiketoimintalogiikkaa
- `@Value` injektoi konfiguraatioarvot application.properties-tiedostosta
- `:default-value`-syntaksi tarjoaa varmuusarvot, jos asetuksia ei ole määritetty

#### Asiakkaan alustaminen:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Mitä tämä tekee:**
- `@PostConstruct` suorittaa tämän metodin sen jälkeen, kun Spring on luonut palvelun
- Luo OpenAI-asiakkaan, joka osoittaa paikalliseen Foundry Local -instanssiin
- `/v1`-polku vaaditaan OpenAI API -yhteensopivuuden vuoksi
- API-avain on "unused", koska paikallinen kehitys ei vaadi autentikointia

#### Chat-metodi:
```java
public String chat(String message) {
    try {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(model)                    // Which AI model to use
                .addUserMessage(message)         // Your question/prompt
                .maxCompletionTokens(150)        // Limit response length
                .temperature(0.7)                // Control creativity (0.0-1.0)
                .build();
        
        ChatCompletion chatCompletion = openAIClient.chat().completions().create(params);
        
        // Extract the AI's response from the API result
        if (chatCompletion.choices() != null && !chatCompletion.choices().isEmpty()) {
            return chatCompletion.choices().get(0).message().content().orElse("No response found");
        }
        
        return "No response content found";
    } catch (Exception e) {
        throw new RuntimeException("Error calling chat completion: " + e.getMessage(), e);
    }
}
```

**Mitä tämä tekee:**
- **ChatCompletionCreateParams**: Konfiguroi AI-pyynnön
  - `model`: Määrittää, mitä AI-mallia käytetään
  - `addUserMessage`: Lisää viestisi keskusteluun
  - `maxCompletionTokens`: Rajoittaa vastauksen pituutta (säästää resursseja)
  - `temperature`: Kontrolloi satunnaisuutta (0.0 = deterministinen, 1.0 = luova)
- **API-kutsu**: Lähettää pyynnön Foundry Localille
- **Vastauksen käsittely**: Poimii AI:n tekstivastauksen turvallisesti
- **Virheenkäsittely**: Käärii poikkeukset hyödyllisiin virheilmoituksiin

### 4. Projektin riippuvuudet (pom.xml)

**Keskeiset riippuvuudet:**

```xml
<!-- Spring Boot - Application framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>${spring-boot.version}</version>
</dependency>

<!-- OpenAI Java SDK - For AI API calls -->
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>

<!-- Jackson - JSON processing -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Mitä nämä tekevät:**
- **spring-boot-starter**: Tarjoaa Spring Bootin ydintoiminnallisuuden
- **openai-java**: Virallinen OpenAI Java SDK API-kommunikaatiota varten
- **jackson-databind**: Käsittelee JSON-sarjoitusta/desarjoitusta API-kutsuille

## Kuinka kaikki toimii yhdessä

Näin sovellus toimii, kun se suoritetaan:

1. **Käynnistys**: Spring Boot käynnistyy ja lukee `application.properties`-tiedoston
2. **Palvelun luonti**: Spring luo `FoundryLocalService`-palvelun ja injektoi konfiguraatioarvot
3. **Asiakkaan asetukset**: `@PostConstruct` alustaa OpenAI-asiakkaan yhteyden Foundry Localiin
4. **Demon suoritus**: `CommandLineRunner` suoritetaan käynnistyksen jälkeen
5. **AI-kutsu**: Demo kutsuu `foundryLocalService.chat()`-metodia testiviestillä
6. **API-pyyntö**: Palvelu rakentaa ja lähettää OpenAI-yhteensopivan pyynnön Foundry Localille
7. **Vastauksen käsittely**: Palvelu poimii ja palauttaa AI:n vastauksen
8. **Näyttö**: Sovellus tulostaa vastauksen ja sulkeutuu

## Foundry Localin asennus

Seuraa näitä vaiheita asentaaksesi Foundry Localin:

1. **Asenna Foundry Local** käyttämällä ohjeita [Edellytykset](../../../../04-PracticalSamples/foundrylocal)-osiossa.
2. **Lataa AI-malli**, esimerkiksi `phi-3.5-mini`, seuraavalla komennolla:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfiguroi application.properties**-tiedosto vastaamaan Foundry Local -asetuksiasi, erityisesti jos käytät eri porttia tai mallia.

## Sovelluksen suorittaminen

### Vaihe 1: Käynnistä Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Vaihe 2: Rakenna ja suorita sovellus
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Odotettu tulos

```
=== Foundry Local Demo ===
Calling Foundry Local service...
Sending message: Hello! Can you tell me what you are and what model you're running?
Response from Foundry Local:
Hello! I'm Phi-3.5, a small language model created by Microsoft. I'm currently running 
as the Phi-3.5-mini-instruct model, which is designed to be helpful, harmless, and honest 
in my interactions. I can assist with a wide variety of tasks including answering 
questions, helping with analysis, creative writing, coding, and general conversation. 
Is there something specific you'd like help with today?
=========================
```

## Seuraavat askeleet

Lisää esimerkkejä löydät [Luku 04: Käytännön esimerkit](../README.md)

## Vianmääritys

### Yleiset ongelmat

**"Yhteys kielletty" tai "Palvelu ei saatavilla"**
- Varmista, että Foundry Local on käynnissä: `foundry model list`
- Tarkista, että palvelu toimii portissa 5273: Katso `application.properties`
- Kokeile käynnistää Foundry Local uudelleen: `foundry model run phi-3.5-mini`

**"Mallia ei löydy" -virheet**
- Tarkista saatavilla olevat mallit: `foundry model list`
- Päivitä mallin nimi `application.properties`-tiedostossa vastaamaan tarkasti
- Lataa malli tarvittaessa: `foundry model run phi-3.5-mini`

**Maven-kääntövirheet**
- Varmista, että käytössäsi on Java 21 tai uudempi: `java -version`
- Puhdista ja rakenna uudelleen: `mvn clean compile`
- Tarkista internet-yhteys riippuvuuksien lataamista varten

**Sovellus käynnistyy mutta ei tuota tulosta**
- Varmista, että Foundry Local vastaa: Avaa selain osoitteeseen `http://localhost:5273`
- Tarkista sovelluksen lokit tarkempien virheilmoitusten varalta
- Varmista, että malli on täysin ladattu ja valmis

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäisellä kielellä tulisi katsoa ensisijaiseksi lähteeksi. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.