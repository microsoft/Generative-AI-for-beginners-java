<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:44:38+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "fi"
}
-->
# Generatiivisen tekoälyn keskeiset tekniikat - opas

## Sisällysluettelo

- [Edellytykset](../../../03-CoreGenerativeAITechniques)
- [Aloittaminen](../../../03-CoreGenerativeAITechniques)
  - [Vaihe 1: Aseta ympäristömuuttuja](../../../03-CoreGenerativeAITechniques)
  - [Vaihe 2: Siirry esimerkkikansioon](../../../03-CoreGenerativeAITechniques)
- [Opetus 1: LLM-vastaukset ja keskustelu](../../../03-CoreGenerativeAITechniques)
- [Opetus 2: Funktioiden kutsuminen](../../../03-CoreGenerativeAITechniques)
- [Opetus 3: RAG (hakuun perustuva generointi)](../../../03-CoreGenerativeAITechniques)
- [Opetus 4: Vastuullinen tekoäly](../../../03-CoreGenerativeAITechniques)
- [Yhteiset toimintamallit esimerkeissä](../../../03-CoreGenerativeAITechniques)
- [Seuraavat askeleet](../../../03-CoreGenerativeAITechniques)
- [Vianmääritys](../../../03-CoreGenerativeAITechniques)
  - [Yleiset ongelmat](../../../03-CoreGenerativeAITechniques)

## Yleiskatsaus

Tämä opas tarjoaa käytännön esimerkkejä generatiivisen tekoälyn keskeisistä tekniikoista Javaa ja GitHub-malleja käyttäen. Opit, kuinka vuorovaikutat suurten kielimallien (LLM) kanssa, toteutat funktioiden kutsumista, käytät hakuun perustuvaa generointia (RAG) ja sovellat vastuullisen tekoälyn käytäntöjä.

## Edellytykset

Ennen aloittamista varmista, että sinulla on:
- Java 21 tai uudempi asennettuna
- Maven riippuvuuksien hallintaan
- GitHub-tili ja henkilökohtainen käyttöoikeustunnus (PAT)

## Aloittaminen

### Vaihe 1: Aseta ympäristömuuttuja

Ensiksi sinun tulee asettaa GitHub-tunnuksesi ympäristömuuttujaksi. Tämä tunnus mahdollistaa GitHub-mallien käytön ilmaiseksi.

**Windows (Komentokehote):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Vaihe 2: Siirry esimerkkikansioon

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Opetus 1: LLM-vastaukset ja keskustelu

**Tiedosto:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Mitä tämä esimerkki opettaa

Tämä esimerkki havainnollistaa suurten kielimallien (LLM) vuorovaikutuksen perusmekanismeja OpenAI:n API:n kautta, mukaan lukien asiakasohjelman alustaminen GitHub-malleilla, viestirakenteiden mallit järjestelmä- ja käyttäjäkehotteille, keskustelutilan hallinta viestihistorian avulla sekä parametrien säätö vastausten pituuden ja luovuuden hallitsemiseksi.

### Keskeiset koodikonseptit

#### 1. Asiakasohjelman alustaminen
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Tämä luo yhteyden GitHub-malleihin käyttämällä tunnustasi.

#### 2. Yksinkertainen vastaus
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Keskustelumuisti
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI muistaa aiemmat viestit vain, jos sisällytät ne seuraaviin pyyntöihin.

### Suorita esimerkki
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Mitä tapahtuu, kun suoritat sen

1. **Yksinkertainen vastaus**: AI vastaa Java-kysymykseen järjestelmäkehotteen ohjauksella
2. **Monivaiheinen keskustelu**: AI säilyttää kontekstin useiden kysymysten välillä
3. **Vuorovaikutteinen keskustelu**: Voit käydä todellisen keskustelun AI:n kanssa

## Opetus 2: Funktioiden kutsuminen

**Tiedosto:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Mitä tämä esimerkki opettaa

Funktioiden kutsuminen mahdollistaa AI-mallien ulkoisten työkalujen ja API:iden käytön jäsennellyn protokollan kautta, jossa malli analysoi luonnollisen kielen pyynnöt, määrittää tarvittavat funktiokutsut sopivilla parametreilla JSON Schema -määritelmien avulla ja käsittelee palautetut tulokset tuottaakseen kontekstuaalisia vastauksia, samalla kun funktioiden suoritus pysyy kehittäjän hallinnassa turvallisuuden ja luotettavuuden varmistamiseksi.

### Keskeiset koodikonseptit

#### 1. Funktion määrittely
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Tämä kertoo AI:lle, mitkä funktiot ovat käytettävissä ja miten niitä käytetään.

#### 2. Funktion suoritusvirta
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Funktion toteutus
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Suorita esimerkki
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Mitä tapahtuu, kun suoritat sen

1. **Sääfunktio**: AI pyytää säätietoja Seattlesta, sinä toimitat ne, AI muotoilee vastauksen
2. **Laskinfunktio**: AI pyytää laskutoimitusta (15 % 240:stä), sinä lasket sen, AI selittää tuloksen

## Opetus 3: RAG (hakuun perustuva generointi)

**Tiedosto:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Mitä tämä esimerkki opettaa

Hakuun perustuva generointi (RAG) yhdistää tiedon haun ja kielen generoinnin lisäämällä ulkoisten dokumenttien kontekstin AI-kehotteisiin, mikä mahdollistaa mallien tarkat vastaukset tiettyjen tietolähteiden perusteella sen sijaan, että ne luottaisivat mahdollisesti vanhentuneisiin tai virheellisiin koulutustietoihin, samalla kun käyttäjän kysymysten ja auktoritatiivisten tietolähteiden välillä säilytetään selkeät rajat strategisen kehotteiden suunnittelun avulla.

### Keskeiset koodikonseptit

#### 1. Dokumenttien lataaminen
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Kontekstin lisääminen
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Kolmoislainausmerkit auttavat AI:ta erottamaan kontekstin ja kysymyksen.

#### 3. Turvallinen vastausten käsittely
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Varmista aina API-vastausten validointi estääksesi kaatumiset.

### Suorita esimerkki
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Mitä tapahtuu, kun suoritat sen

1. Ohjelma lataa `document.txt`-tiedoston (sisältää tietoa GitHub-malleista)
2. Kysyt kysymyksen dokumentista
3. AI vastaa vain dokumentin sisällön perusteella, ei yleisen tietämyksensä mukaan

Kokeile kysyä: "Mitä ovat GitHub-mallit?" vs "Millainen sää on?"

## Opetus 4: Vastuullinen tekoäly

**Tiedosto:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Mitä tämä esimerkki opettaa

Vastuullisen tekoälyn esimerkki korostaa turvallisuustoimenpiteiden toteuttamisen tärkeyttä tekoälysovelluksissa. Se havainnollistaa turvallisuussuodattimia, jotka tunnistavat haitalliset sisältökategoriat, kuten vihapuheen, häirinnän, itsensä vahingoittamisen, seksuaalisen sisällön ja väkivallan, ja osoittaa, kuinka tuotantokäytössä olevien tekoälysovellusten tulisi käsitellä sisältöpolitiikan rikkomuksia sujuvasti asianmukaisen virheenkäsittelyn, käyttäjäpalautemekanismien ja varavastausstrategioiden avulla.

### Keskeiset koodikonseptit

#### 1. Turvallisuustestauksen kehys
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Testatut turvallisuuskategoriat
- Väkivalta/vahingoittamisohjeet
- Vihapuhe
- Yksityisyyden loukkaukset
- Lääketieteellinen väärä tieto
- Laittomat toiminnot

### Suorita esimerkki
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Mitä tapahtuu, kun suoritat sen

Ohjelma testaa erilaisia haitallisia kehotteita ja näyttää, kuinka AI-turvajärjestelmä:
1. **Estää vaaralliset pyynnöt** HTTP 400 -virheillä
2. **Sallii turvallisen sisällön** normaalin generoinnin
3. **Suojaa käyttäjiä** haitallisilta AI-tuotoksilta

## Yhteiset toimintamallit esimerkeissä

### Autentikointimalli
Kaikki esimerkit käyttävät tätä mallia autentikoitumiseen GitHub-malleihin:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Virheenkäsittelymalli
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Viestirakennemalli
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Seuraavat askeleet

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## Vianmääritys

### Yleiset ongelmat

**"GITHUB_TOKEN not set"**
- Varmista, että olet asettanut ympäristömuuttujan
- Tarkista, että tunnuksellasi on `models:read`-oikeus

**"No response from API"**
- Tarkista internet-yhteytesi
- Varmista, että tunnuksesi on voimassa
- Tarkista, oletko ylittänyt käyttörajoitukset

**Maven-kääntövirheet**
- Varmista, että sinulla on Java 21 tai uudempi
- Suorita `mvn clean compile` päivittääksesi riippuvuudet

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäisellä kielellä tulisi pitää ensisijaisena lähteenä. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.