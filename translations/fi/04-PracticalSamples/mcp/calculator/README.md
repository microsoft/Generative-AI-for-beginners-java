<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "8c6c7e9008b114540677f7a65aa9ddad",
  "translation_date": "2025-07-25T11:45:52+00:00",
  "source_file": "04-PracticalSamples/mcp/calculator/README.md",
  "language_code": "fi"
}
-->
# MCP-laskimen opas aloittelijoille

## Sisällysluettelo

- [Mitä opit](../../../../../04-PracticalSamples/mcp/calculator)
- [Edellytykset](../../../../../04-PracticalSamples/mcp/calculator)
- [Projektirakenteen ymmärtäminen](../../../../../04-PracticalSamples/mcp/calculator)
- [Keskeiset komponentit selitettynä](../../../../../04-PracticalSamples/mcp/calculator)
  - [1. Pääsovellus](../../../../../04-PracticalSamples/mcp/calculator)
  - [2. Laskinpalvelu](../../../../../04-PracticalSamples/mcp/calculator)
  - [3. Suora MCP-asiakas](../../../../../04-PracticalSamples/mcp/calculator)
  - [4. AI-pohjainen asiakas](../../../../../04-PracticalSamples/mcp/calculator)
- [Esimerkkien suorittaminen](../../../../../04-PracticalSamples/mcp/calculator)
- [Kuinka kaikki toimii yhdessä](../../../../../04-PracticalSamples/mcp/calculator)
- [Seuraavat askeleet](../../../../../04-PracticalSamples/mcp/calculator)

## Mitä opit

Tämä opas selittää, kuinka rakentaa laskinpalvelu Model Context Protocolin (MCP) avulla. Opit:

- Kuinka luoda palvelu, jota tekoäly voi käyttää työkaluna
- Kuinka asettaa suora yhteys MCP-palveluihin
- Kuinka tekoälymallit voivat automaattisesti valita käytettävät työkalut
- Ero suoran protokollakutsun ja tekoälyavusteisen vuorovaikutuksen välillä

## Edellytykset

Ennen aloittamista varmista, että sinulla on:
- Java 21 tai uudempi asennettuna
- Maven riippuvuuksien hallintaan
- GitHub-tili ja henkilökohtainen käyttöoikeustunnus (PAT)
- Perustiedot Javasta ja Spring Bootista

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
    └── Bot.java                          # Simple chat interface
```

## Keskeiset komponentit selitettynä

### 1. Pääsovellus

**Tiedosto:** `McpServerApplication.java`

Tämä on laskinpalvelumme aloituspiste. Se on tavallinen Spring Boot -sovellus, jossa on yksi erityinen lisäys:

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
- Käynnistää Spring Boot -verkkopalvelimen portissa 8080
- Luo `ToolCallbackProvider`-komponentin, joka tekee laskinmetodimme saatavilla MCP-työkaluina
- `@Bean`-annotaatio kertoo Springille, että tämä komponentti on käytettävissä muille osille

### 2. Laskinpalvelu

**Tiedosto:** `CalculatorService.java`

Täällä tapahtuu kaikki laskenta. Jokainen metodi on merkitty `@Tool`-annotaatiolla, jotta se on saatavilla MCP:n kautta:

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
    
    // More calculator operations...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format("%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Keskeiset ominaisuudet:**

1. **`@Tool`-annotaatio**: Tämä kertoo MCP:lle, että ulkoiset asiakkaat voivat kutsua tätä metodia
2. **Selkeät kuvaukset**: Jokaisella työkalulla on kuvaus, joka auttaa tekoälymalleja ymmärtämään, milloin sitä käytetään
3. **Johdonmukainen palautusmuoto**: Kaikki operaatiot palauttavat helposti luettavia merkkijonoja, kuten "5.00 + 3.00 = 8.00"
4. **Virheenkäsittely**: Nollalla jakaminen ja negatiiviset neliöjuuret palauttavat virheilmoituksia

**Saatavilla olevat operaatiot:**
- `add(a, b)` - Laskee kahden luvun summan
- `subtract(a, b)` - Vähentää toisen luvun ensimmäisestä
- `multiply(a, b)` - Kertoo kaksi lukua
- `divide(a, b)` - Jakaa ensimmäisen luvun toisella (nollatarkistus)
- `power(base, exponent)` - Korottaa luvun potenssiin
- `squareRoot(number)` - Laskee neliöjuuren (negatiivisten tarkistus)
- `modulus(a, b)` - Palauttaa jakolaskun jakojäännöksen
- `absolute(number)` - Palauttaa luvun itseisarvon
- `help()` - Palauttaa tietoa kaikista operaatioista

### 3. Suora MCP-asiakas

**Tiedosto:** `SDKClient.java`

Tämä asiakas kommunikoi suoraan MCP-palvelimen kanssa ilman tekoälyä. Se kutsuu laskimen toimintoja manuaalisesti:

```java
public class SDKClient {
    
    public static void main(String[] args) {
        var transport = new WebFluxSseClientTransport(
            WebClient.builder().baseUrl("http://localhost:8080")
        );
        new SDKClient(transport).run();
    }
    
    public void run() {
        var client = McpClient.sync(this.transport).build();
        client.initialize();
        
        // List available tools
        ListToolsResult toolsList = client.listTools();
        System.out.println("Available Tools = " + toolsList);
        
        // Call specific calculator functions
        CallToolResult resultAdd = client.callTool(
            new CallToolRequest("add", Map.of("a", 5.0, "b", 3.0))
        );
        System.out.println("Add Result = " + resultAdd);
        
        CallToolResult resultSqrt = client.callTool(
            new CallToolRequest("squareRoot", Map.of("number", 16.0))
        );
        System.out.println("Square Root Result = " + resultSqrt);
        
        client.closeGracefully();
    }
}
```

**Mitä tämä tekee:**
1. **Yhdistää** laskinpalvelimeen osoitteessa `http://localhost:8080`
2. **Listaa** kaikki saatavilla olevat työkalut (laskimen toiminnot)
3. **Kutsuu** tiettyjä toimintoja tarkoin parametrein
4. **Tulostaa** tulokset suoraan

**Milloin käyttää tätä:** Kun tiedät tarkalleen, minkä laskutoimituksen haluat suorittaa ja haluat kutsua sen ohjelmallisesti.

### 4. AI-pohjainen asiakas

**Tiedosto:** `LangChain4jClient.java`

Tämä asiakas käyttää tekoälymallia (GPT-4o-mini), joka voi automaattisesti päättää, mitä laskimen työkaluja käyttää:

```java
public class LangChain4jClient {
    
    public static void main(String[] args) throws Exception {
        // Set up the AI model (using GitHub Models)
        ChatLanguageModel model = OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .apiKey(System.getenv("GITHUB_TOKEN"))
                .modelName("gpt-4o-mini")
                .build();

        // Connect to our calculator MCP server
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("http://localhost:8080/sse")
                .logRequests(true)  // Shows what the AI is doing
                .logResponses(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        // Give the AI access to our calculator tools
        ToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();

        // Create an AI bot that can use our calculator
        Bot bot = AiServices.builder(Bot.class)
                .chatLanguageModel(model)
                .toolProvider(toolProvider)
                .build();

        // Now we can ask the AI to do calculations in natural language
        String response = bot.chat("Calculate the sum of 24.5 and 17.3 using the calculator service");
        System.out.println(response);

        response = bot.chat("What's the square root of 144?");
        System.out.println(response);
    }
}
```

**Mitä tämä tekee:**
1. **Luo** yhteyden tekoälymalliin GitHub-tunnuksesi avulla
2. **Yhdistää** tekoälyn laskinpalvelimeen MCP:n kautta
3. **Antaa** tekoälylle pääsyn kaikkiin laskimen työkaluihin
4. **Mahdollistaa** luonnollisen kielen pyynnöt, kuten "Laske 24.5 ja 17.3 summa"

**Tekoäly automaattisesti:**
- Ymmärtää, että haluat laskea summan
- Valitsee `add`-työkalun
- Kutsuu `add(24.5, 17.3)`
- Palauttaa tuloksen luonnollisessa vastauksessa

## Esimerkkien suorittaminen

### Vaihe 1: Käynnistä laskinpalvelin

Aseta ensin GitHub-tunnuksesi (tarvitaan tekoälyasiakkaalle):

**Windows:**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

Käynnistä palvelin:
```bash
cd 04-PracticalSamples/mcp/calculator
mvn spring-boot:run
```

Palvelin käynnistyy osoitteessa `http://localhost:8080`. Näet:
```
Started McpServerApplication in X.XXX seconds
```

### Vaihe 2: Testaa suoralla asiakkaalla

Avaa uusi pääte:
```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.SDKClient"
```

Näet tulosteen, kuten:
```
Available Tools = [add, subtract, multiply, divide, power, squareRoot, modulus, absolute, help]
Add Result = 5.00 + 3.00 = 8.00
Square Root Result = √16.00 = 4.00
```

### Vaihe 3: Testaa tekoälyasiakkaalla

```bash
mvn test-compile exec:java -Dexec.mainClass="com.microsoft.mcp.sample.client.LangChain4jClient"
```

Näet tekoälyn käyttävän työkaluja automaattisesti:
```
The sum of 24.5 and 17.3 is 41.8.
The square root of 144 is 12.
```

## Kuinka kaikki toimii yhdessä

Näin koko prosessi etenee, kun kysyt tekoälyltä "Mikä on 5 + 3?":

1. **Sinä** kysyt tekoälyltä luonnollisella kielellä
2. **Tekoäly** analysoi pyyntösi ja ymmärtää, että haluat laskea summan
3. **Tekoäly** kutsuu MCP-palvelinta: `add(5.0, 3.0)`
4. **Laskinpalvelu** suorittaa: `5.0 + 3.0 = 8.0`
5. **Laskinpalvelu** palauttaa: `"5.00 + 3.00 = 8.00"`
6. **Tekoäly** vastaanottaa tuloksen ja muotoilee luonnollisen vastauksen
7. **Sinä** saat: "Lukujen 5 ja 3 summa on 8"

## Seuraavat askeleet

Lisää esimerkkejä löydät kohdasta [Luku 04: Käytännön esimerkit](../../README.md)

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäisellä kielellä tulisi katsoa ensisijaiseksi lähteeksi. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.