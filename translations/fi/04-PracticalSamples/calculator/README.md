# MCP-laskinopas aloittelijoille

## Sisällysluettelo

- [Mitä opit](#mitä-opit)
- [Vaatimukset](#vaatimukset)
- [Riippuvuuksien versiot](#riippuvuuksien-versiot)
- [Projektirakenteen ymmärtäminen](#projektirakenteen-ymmärtäminen)
- [Keskeiset osat selitettynä](#keskeiset-osat-selitettynä)
  - [1. Pääsovellus](#1-pääsovellus)
  - [2. Laskinpalvelu](#2-laskinpalvelu)
  - [3. Suora MCP-asiakas](#3-suora-mcp-asiakas)
  - [4. Tekoälyllä toimiva asiakas](#4-tekoälyllä-toimiva-asiakas)
- [Esimerkkien suorittaminen](#esimerkkien-suorittaminen)
- [Offline-testit](#offline-testit)
- [Kuinka kaikki toimii yhdessä](#kuinka-kaikki-toimii-yhdessä)
- [Seuraavat askeleet](#seuraavat-askeleet)

## Mitä opit

Tässä oppaassa selitetään, miten rakennetaan laskinpalvelu Model Context Protocolin (MCP) avulla. Opit:

- Miten luodaan palvelu, jota tekoäly voi käyttää työkaluna
- Miten asetetaan suora yhteys MCP-palveluihin
- Miten tekoälymallit voivat automaattisesti valita käytettävät työkalut
- Ero suoran protokollakutsun ja tekoälyn tukemien vuorovaikutustilanteiden välillä

## Vaatimukset

Ennen aloittamista varmista, että sinulla on:
- Java 21 tai uudempi asennettuna
- Maven riippuvuuksien hallintaan
- Perustiedot Javasta ja Spring Bootista

Vain tekoälyasiakkaat tarvitsevat Azure OpenAI -asennuksen ja todennetun `DefaultAzureCredential`-tunnistautumisen,
kuten paikallisen Azure CLI -kirjautumisen tai Azure-hallinnoidun identiteetin. Identiteetillä tulee olla
Cognitive Services OpenAI User -rooli resurssissa. Katso [Luku 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Palvelin, suora SDK-asiakas ja kaikki automatisoidut testit eivät tarvitse Azure-tiliä tai mallin käyttöoikeutta.

## Riippuvuuksien versiot

Julkaisun riippuvuudet tarkistettu 14.9.2026:

| Riippuvuus | Versio |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (Spring AI -hallinnoitu) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| LangChain4j virallinen OpenAI-adapteri | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (Boot-hallinnoitu) | 6.0.3 |

MCP- ja viralliset OpenAI-adapterit ovat julkaistuja betaversioita Maven Centralissa, eivät snapshotteja.
Niiden versiot eroavat LangChain4j ydinversiosta. Snapshoteja tai milestone-repositoryjä ei tarvita.
Vain asiakasriippuvuuksilla on test scope, koska ajettavat esimerkit ovat `src/test/java`-kansiossa.

## Projektirakenteen ymmärtäminen

Laskinprojektissa on useita tärkeitä tiedostoja:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Keskeiset osat selitettynä

### 1. Pääsovellus

**Tiedosto:** `McpServerApplication.java`

Tämä on laskinpalvelumme käynnistyspiste. Kyseessä on tavallinen Spring Boot -sovellus, johon on lisätty yksi erityispiirre:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Mitä tämä tekee:**
- Käynnistää Spring Boot -webpalvelimen portissa 8080
- Luo `ToolCallbackProvider`-komponentin, joka tekee laskinmenetelmät MCP-työkaluiksi
- `@Bean`-annotaatio kertoo Springille, että tämä on komponentti, jota muut osat voivat käyttää

### 2. Laskinpalvelu

**Tiedosto:** `CalculatorService.java`

Tässä tehdään kaikki laskutoimitukset. Jokainen metodi on merkitty `@Tool`-annotaatiolla, jotta MCP voi kutsua sen:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Lisää laskimen toimintoja...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Keskeiset ominaisuudet:**

1. **`@Tool`-annotaatio**: Tällä kerrotaan MCP:lle, että metodia voivat kutsua ulkoiset asiakkaat
2. **Selkeät kuvaukset**: Jokaisella työkalulla on kuvaus, joka auttaa tekoälymalleja ymmärtämään, milloin sitä käytetään
3. **Yhtenäinen palautusmuoto**: Kaikki operaatiot palauttavat ihmislukuisia merkkijonoja, kuten "5.00 + 3.00 = 8.00"
4. **Virheenkäsittely**: Nollalla jakaminen ja negatiiviset neliöjuuret palauttavat virheilmoituksia

**Saatavilla olevat operaatiot:**
- `add(a, b)` - Laskee luvut yhteen
- `subtract(a, b)` - Vähentää toisen ensimmäisestä
- `multiply(a, b)` - Kertoo luvut keskenään
- `divide(a, b)` - Jakaa ensimmäisen toisella (tarkistaa nollan)
- `power(base, exponent)` - Kohottaa perustan eksponenttiin
- `squareRoot(number)` - Laskee neliöjuuren (tarkistaa negatiivisuuden)
- `modulus(a, b)` - Palauttaa jakojäännöksen
- `absolute(number)` - Palauttaa itseisarvon
- `help()` - Palauttaa tiedot kaikista operaatioista

### 3. Suora MCP-asiakas

Katso [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Tämä asiakas käyttää `HttpClientStreamableHttpTransport`:ia `/mcp`-polussa, alustaa yhteyden,
kokeilee yhteyttä palvelimeen ja seuraa työkalulistan sivutusta. Se tarkistaa kaikkien yhdeksän
odotetun työkalun olemassaolon ja kutsuu ne kaikki, mukaan lukien `modulus` ja `help`, ilman tekoälymallia.

Nykyinen pyyntörakentaja näyttää tältä:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Protokollavirheet aiheuttavat asiakasvirheen, eivät väärän onnistumisen tulostamista. MCP-asiakas suljetaan try-with-resources -lohkon avulla,
myös jos löydön tai työkalukutsun aikana tapahtuu virhe.

### 4. Tekoälyllä toimiva asiakas

Katso [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
ja [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` toteuttaa nykyisen LangChain4j:n `ChatModel`-rajapinnan.
`StreamableHttpMcpTransport` yhdistää sen samaan `/mcp`-pisteeseen kuin SDK-asiakas.
`AiServices` löytää työkalut ja hallinnoi työkalukutsujen / tulosten vuoropuhelua.

Oletusasennus on **GPT-5.6 Luna**, jossa päättely on nimenomaisesti poistettu käytöstä:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Nämä oletukset koskevat jokaista täydennystä, mukaan lukien työkalukäytön jälkeiset jatkokyselyt.
Asiakas käyttää päivitettävää `BearerTokenCredential`-tunnistetta, joka on tuettu `DefaultAzureCredential`-luokalla
ja `https://ai.azure.com/.default`-alueella, ei kertaluonteisella API-avaimena annetulla tokenilla.
Resurssin URL-osoitteet ja URL:t, jotka päättyvät `/openai/v1`, hyväksytään molemmat.

Bottiin tallennetaan rajattu keskusteluhistoria, se tulostaa `Tool executed: ...` todellisella
MCP-tuloksella ja epäonnistuu, jos vastaus ohittaa työkalut. Työkalusilmukat ovat rajoitettu neljään kierrokseen.
Todennus-, malli-, MCP- ja työkaluvikoja välitetään; automaattiset uudelleenyritykset mallille on estetty.
Sekä MCP-siirto/asiakas että virallinen OpenAI-asiakas suljetaan onnistumisen tai virheen jälkeen.

## Esimerkkien suorittaminen

### Vaihe 1: Käynnistä laskinpalvelin

Palvelimella ei tarvita Azure-konfiguraatiota. Komennot suoritetaan tämän esimerkin hakemistosta.
Esimerkki käyttää porttia **18081** välttääkseen päällekkäisyyksiä toisen esimerkin kanssa; oletus on edelleen 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

MCP-päätepiste on `http://localhost:18081/mcp`. Terveystiedot ja löydöt löytyvät osoitteista
`http://localhost:18081/health` ja `http://localhost:18081/info`.
Streamable HTTP korvaa vanhan SSE-transportin; `/sse` ja `/v1/tools` eivät ole päätepisteitä.

### Vaihe 2: Testaa suoralla asiakkaalla

Toisessa PowerShell-terminaalissa:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Syötettä ei tarvita. Kaikkia yhdeksää työkalua käytetään. Odotetut laskutulokset ovat
8, 6, 42, 5, 256, 4, 2 ja 5.5, jonka jälkeen avustusteksti.

### Vaihe 3: Testaa tekoälyasiakkaalla

Todennuksen jälkeen aseta tekoälyasiakas samaan terminaaliin vaatimusten mukaisesti:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Odota riviä `Tool executed: add` arvolla `41.80`, jota seuraa mallin vastaus.
Yhdellä kehotteella toimiva tila poistuu odottamatta syötettä. Alkuperäisen neljän kehotteen demon suorittamiseksi:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demo kutsuu `add`, `squareRoot`, `help` ja ketjutetut `power` ja `divide` -operaatiot.
Odotetut numeeriset vastaukset ovat 41.8, 12 ja 64. Argumenttien poisjättäminen ajaa myös tämän demon.

### Vaihe 4: Suorita interaktiivinen botti

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Kirjoita `Multiply 6 by 7 using the calculator service` ja sen jälkeen `exit` tai `quit`.
Odota todellista `multiply`-työkalun tulosta 42. Tyhjät rivit ohitetaan; EOF päättää istunnon.
Epäinteraktiivinen savutesti tälle käynnistyspisteelle:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Molemmat tekoälykäynnistykset hyväksyvät `--prompt "question"`, `--demo` ja `--interactive`.
Virheelliset asetukset epäonnistuvat ennen yhteyden avaamista. Jokainen Maven `-D...`-argumentti on täysin lainattava
PowerShellissä. Bashissa käytä `export NAME=value` eikä `$env:NAME = "value"`.

**Kiintiö:** Suorita tekoälyesimerkit peräkkäin. Yksinkertainen pyyntö tarvitsee normaalisti kaksi mallipyyntöä;
koko demo tavallisesti yhdeksän, mukaan lukien työkaluvastauksen jälkeiset jatkokyselyt. Jaa samaa 10 RPM
-asennusta, odota uusi kiintiömahdollisuus ennen seuraavaa tekoälyajoa. 429-virhe näkyy eikä automaattisesti toistu;
noudata palvelun retry-after-ohjetta. Todellinen pyyntöluku riippuu mallista.
Offline-testit eivät kuluta kiintiötä eivätkä varmista Luna-palvelimen saatavuutta tai vastausten laatua.

### Asetukset ja sulkeminen

| Asetus | Oletus / toiminta |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; perus-URL ilman `/mcp` |
| `-Dmcp.server.url=...` | Ylikirjoittaa `MCP_SERVER_URL` kaikille asiakkaille |
| `AZURE_OPENAI_ENDPOINT` | Tarvitaan vain tekoälyasiakkaille; resurssin URL tai `/openai/v1`-URL |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; Azuren käyttöönoton nimi |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; positiivinen kokonaisluku |
| Päättely | Aina `none`, myös työkalusilmukan jatkokyselyissä |

Ylikirjoitetun käyttöönoton on tuettava `reasoning_effort=none` ja `max_completion_tokens`.
Asiakkaat eivät lue automaattisesti `.env`-tiedostoa. Lopeta palvelin painamalla `Ctrl+C` testauksen jälkeen.
Asiakkaat palautuvat normaalisti ilman `System.exit`-kutsuja tai sulkemisen viiveitä.

## Offline-testit

```powershell
mvn -B -ntp clean verify
```

Kaikki testit suoritetaan offline-tilassa Azuren suhteen: protokollapino käynnistää Spring-palvelimen ja
OpenAI-yhteensopivan stubin satunnaisilla loopback-porteilla, ja sulkee ne sitten. Maven voi silti tarvita
riippuvuuksien lataamista. Todennustietoja, elävää käyttöönottoa tai olemassa olevaa MCP-palvelinta ei käytetä.

- Laskimen yksikkötestit kattavat kaikki aritmeettiset toiminnot, desimaalitulokset, avun ja domain-virheet.
- MCP-testit kattavat alustuksen, löydön, kaikki yhdeksän työkalukutsua, työkaluvikojen käsittelyn ja terveys-/tietotestit.
- Tekoälyprotokollatestit suorittavat koko demon ja interaktiivisen botin todellista laskinta vastaan,
  varmistavat työkalutulosten syötteen seuraavaan täydennykseen ja tarkastavat jokaisen HTTP-kehon liittyen Lunaan,
  `reasoning_effort: "none"`, ja `max_completion_tokens` ilman perinteistä `max_tokens`.
- Konfigurointi-/syötetestit kattavat käyttöönoton ja päätepisteen ylikirjoitukset, tyhjät rivit, EOF:n,
  exit/quit-komennot, yksittäisen kehotteen tilan, virheelliset asetukset ja virheiden välityksen. Kiintiötestit todistavat, ettei 429-virhettä yritetä uudelleen.

## Kuinka kaikki toimii yhdessä

Tässä kokonaisvirtaus, kun kysyt tekoälyltä "Paljonko on 5 + 3?":

1. **Sinä** kysyt tekoälyltä luonnollisella kielellä
2. **Tekoäly** analysoi pyynnön ja huomaa, että haluat yhteenlaskun
3. **Tekoäly** kutsuu MCP-palvelinta: `add(5.0, 3.0)`
4. **Laskinpalvelu** suorittaa: `5.0 + 3.0 = 8.0`
5. **Laskinpalvelu** palauttaa: `"5.00 + 3.00 = 8.00"`
6. **Tekoäly** saa tuloksen ja muotoilee luonnollisen vastauksen
7. **Sinä** saat: "Lukujen 5 ja 3 summa on 8"

## Seuraavat askeleet

Lisää esimerkkejä löydät kohdasta [Luku 04: Käytännön esimerkit](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vastuuvapauslauseke**:
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, otathan huomioon, että automaattiset käännökset saattavat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäiskielellä on virallinen lähde. Tärkeissä asioissa suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa tämän käännöksen käytöstä aiheutuvista väärinymmärryksistä tai tulkinnoista.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->