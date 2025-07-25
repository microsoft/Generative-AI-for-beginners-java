<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "0cbf68d605615a1e602c832a24616859",
  "translation_date": "2025-07-25T11:46:16+00:00",
  "source_file": "04-PracticalSamples/petstory/README.md",
  "language_code": "fi"
}
-->
# Lemmikkitarinan luontiohje aloittelijoille

## Sisällysluettelo

- [Edellytykset](../../../../04-PracticalSamples/petstory)
- [Projektirakenteen ymmärtäminen](../../../../04-PracticalSamples/petstory)
- [Keskeisten komponenttien selitys](../../../../04-PracticalSamples/petstory)
  - [1. Pääsovellus](../../../../04-PracticalSamples/petstory)
  - [2. Verkkokontrolleri](../../../../04-PracticalSamples/petstory)
  - [3. Tarinapalvelu](../../../../04-PracticalSamples/petstory)
  - [4. Verkkopohjat](../../../../04-PracticalSamples/petstory)
  - [5. Konfiguraatio](../../../../04-PracticalSamples/petstory)
- [Sovelluksen käynnistäminen](../../../../04-PracticalSamples/petstory)
- [Miten kaikki toimii yhdessä](../../../../04-PracticalSamples/petstory)
- [AI-integraation ymmärtäminen](../../../../04-PracticalSamples/petstory)
- [Seuraavat askeleet](../../../../04-PracticalSamples/petstory)

## Edellytykset

Ennen aloittamista varmista, että sinulla on:
- Java 21 tai uudempi asennettuna
- Maven riippuvuuksien hallintaan
- GitHub-tili, jossa on henkilökohtainen käyttöoikeustunnus (PAT) `models:read` -oikeuksilla
- Perustiedot Javasta, Spring Bootista ja verkkokehityksestä

## Projektirakenteen ymmärtäminen

Lemmikkitarinaprojektissa on useita tärkeitä tiedostoja:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI story generation service
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Keskeisten komponenttien selitys

### 1. Pääsovellus

**Tiedosto:** `PetStoryApplication.java`

Tämä on Spring Boot -sovelluksen aloituspiste:

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
- Käynnistää sisäänrakennetun verkkopalvelimen (Tomcat) portissa 8080
- Luo kaikki tarvittavat Spring-beansit ja -palvelut automaattisesti

### 2. Verkkokontrolleri

**Tiedosto:** `PetController.java`

Tämä käsittelee kaikki verkkopyynnöt ja käyttäjän vuorovaikutukset:

```java
@Controller
public class PetController {
    
    private final StoryService storyService;
    
    public PetController(StoryService storyService) {
        this.storyService = storyService;
    }
    
    @GetMapping("/")
    public String index() {
        return "index";  // Returns index.html template
    }
    
    @PostMapping("/generate-story")
    public String generateStory(@RequestParam("description") String description, 
                               Model model, 
                               RedirectAttributes redirectAttributes) {
        
        // Input validation
        if (description.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please provide a description.");
            return "redirect:/";
        }
        
        // Sanitize input for security
        String sanitizedDescription = sanitizeInput(description);
        
        // Generate story with error handling
        try {
            String story = storyService.generateStory(sanitizedDescription);
            model.addAttribute("caption", sanitizedDescription);
            model.addAttribute("story", story);
            return "result";  // Returns result.html template
            
        } catch (Exception e) {
            // Use fallback story if AI fails
            String fallbackStory = generateFallbackStory(sanitizedDescription);
            model.addAttribute("story", fallbackStory);
            return "result";
        }
    }
    
    private String sanitizeInput(String input) {
        return input.replaceAll("[<>\"'&]", "")  // Remove dangerous characters
                   .trim()
                   .substring(0, Math.min(input.length(), 500));  // Limit length
    }
}
```

**Keskeiset ominaisuudet:**

1. **Reittien käsittely**: `@GetMapping("/")` näyttää latauslomakkeen, `@PostMapping("/generate-story")` käsittelee lähetykset
2. **Syötteen validointi**: Tarkistaa tyhjät kuvaukset ja pituusrajat
3. **Turvallisuus**: Puhdistaa käyttäjän syötteen XSS-hyökkäysten estämiseksi
4. **Virheenkäsittely**: Tarjoaa varatarinoita, kun AI-palvelu epäonnistuu
5. **Mallin sidonta**: Välittää dataa HTML-pohjiin Springin `Model`-luokan avulla

**Varajärjestelmä:**
Kontrolleri sisältää valmiiksi kirjoitettuja tarinapohjia, joita käytetään, kun AI-palvelu ei ole käytettävissä:

```java
private String generateFallbackStory(String description) {
    String[] storyTemplates = {
        "Meet the most wonderful pet in the world – a furry ball of energy...",
        "Once upon a time, there lived a remarkable pet whose heart was as big...",
        "In a cozy home filled with love, there lived an extraordinary pet..."
    };
    
    // Use description hash for consistent responses
    int index = Math.abs(description.hashCode() % storyTemplates.length);
    return storyTemplates[index];
}
```

### 3. Tarinapalvelu

**Tiedosto:** `StoryService.java`

Tämä palvelu kommunikoi GitHub Models -palvelun kanssa tarinoiden luomiseksi:

```java
@Service
public class StoryService {
    
    private final OpenAIClient openAIClient;
    private final String modelName;
    
    public StoryService(@Value("${github.models.endpoint}") String endpoint,
                       @Value("${github.models.model}") String modelName) {
        
        String githubToken = System.getenv("GITHUB_TOKEN");
        if (githubToken == null || githubToken.isBlank()) {
            throw new IllegalStateException("GITHUB_TOKEN environment variable must be set");
        }
        
        // Create OpenAI client configured for GitHub Models
        this.openAIClient = OpenAIOkHttpClient.builder()
                .baseUrl(endpoint)
                .apiKey(githubToken)
                .build();
    }
    
    public String generateStory(String description) {
        String systemPrompt = "You are a creative storyteller who writes fun, " +
                             "family-friendly short stories about pets. " +
                             "Keep stories under 500 words and appropriate for all ages.";
        
        String userPrompt = "Write a fun short story about a pet described as: " + description;
        
        // Configure the AI request
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(modelName)
                .addSystemMessage(systemPrompt)
                .addUserMessage(userPrompt)
                .maxCompletionTokens(500)  // Limit response length
                .temperature(0.8)          // Control creativity (0.0-1.0)
                .build();
        
        // Send request and get response
        ChatCompletion response = openAIClient.chat().completions().create(params);
        
        return response.choices().get(0).message().content().orElse("");
    }
}
```

**Keskeiset komponentit:**

1. **OpenAI-asiakas**: Käyttää virallista OpenAI Java SDK:ta, joka on konfiguroitu GitHub Models -palvelulle
2. **Järjestelmäkehotus**: Asettaa AI:n käyttäytymisen kirjoittamaan perheystävällisiä lemmikkitarinoita
3. **Käyttäjäkehotus**: Kertoo AI:lle tarkalleen, millainen tarina kirjoitetaan kuvauksen perusteella
4. **Parametrit**: Kontrolloi tarinan pituutta ja luovuuden tasoa
5. **Virheenkäsittely**: Heittää poikkeuksia, jotka kontrolleri ottaa kiinni ja käsittelee

### 4. Verkkopohjat

**Tiedosto:** `index.html` (Latauslomake)

Pääsivu, jossa käyttäjät kuvaavat lemmikkejään:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Generator</title>
    <!-- CSS styling -->
</head>
<body>
    <div class="container">
        <h1>Pet Story Generator</h1>
        <p>Describe your pet and we'll create a fun story about them!</p>
        
        <!-- Error message display -->
        <div th:if="${error}" class="error" th:text="${error}"></div>
        
        <!-- Story generation form -->
        <form action="/generate-story" method="post">
            <div class="form-group">
                <label for="description">Describe your pet:</label>
                <textarea id="description" name="description" 
                         placeholder="Tell us about your pet - what they look like, their personality, favorite activities..."
                         maxlength="1000" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Generate Story</button>
        </form>
        
        <!-- Image upload section with client-side processing -->
        <div class="upload-section">
            <h2>Or Upload a Photo</h2>
            <input type="file" id="imageInput" accept="image/*" />
            <button onclick="analyzeImage()" class="upload-btn">Analyze Image</button>
        </div>
        
        <script>
            // Client-side image analysis using Transformers.js
            async function analyzeImage() {
                // Image processing code here
                // Generates description automatically from uploaded image
            }
        </script>
    </div>
</body>
</html>
```

**Tiedosto:** `result.html` (Tarinan näyttö)

Näyttää luodun tarinan:

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

**Pohjan ominaisuudet:**

1. **Thymeleaf-integraatio**: Käyttää `th:`-attribuutteja dynaamiseen sisältöön
2. **Responsiivinen suunnittelu**: CSS-tyylit mobiilille ja työpöydälle
3. **Virheenkäsittely**: Näyttää validointivirheet käyttäjille
4. **Asiakaspään käsittely**: JavaScript-kuvan analysointiin (Transformers.js:n avulla)

### 5. Konfiguraatio

**Tiedosto:** `application.properties`

Sovelluksen asetukset:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# GitHub Models configuration
github.models.endpoint=https://models.github.ai/inference
github.models.model=openai/gpt-4.1-nano
```

**Konfiguraation selitys:**

1. **Tiedoston lataus**: Sallii enintään 10 MB:n kuvat
2. **Lokitus**: Kontrolloi, mitä tietoja kirjataan suorituksen aikana
3. **GitHub Models**: Määrittää, mitä AI-mallia ja päätepistettä käytetään
4. **Turvallisuus**: Virheenkäsittelyasetukset, jotka estävät arkaluontoisten tietojen paljastamisen

## Sovelluksen käynnistäminen

### Vaihe 1: Aseta GitHub-tunnuksesi

Ensiksi sinun täytyy asettaa GitHub-tunnuksesi ympäristömuuttujaksi:

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

**Miksi tämä on tarpeen:**
- GitHub Models vaatii autentikoinnin AI-mallien käyttöön
- Ympäristömuuttujien käyttö pitää arkaluontoiset tunnukset poissa lähdekoodista
- `models:read`-oikeus antaa pääsyn AI-päätelmiin

### Vaihe 2: Kokoa ja käynnistä

Siirry projektihakemistoon:
```bash
cd 04-PracticalSamples/petstory
```

Kokoa sovellus:
```bash
mvn clean compile
```

Käynnistä palvelin:
```bash
mvn spring-boot:run
```

Sovellus käynnistyy osoitteessa `http://localhost:8080`.

### Vaihe 3: Testaa sovellusta

1. **Avaa** `http://localhost:8080` selaimessasi
2. **Kuvaile** lemmikkisi tekstialueella (esim. "Leikkisä kultainennoutaja, joka rakastaa noutamista")
3. **Klikkaa** "Luo tarina" saadaksesi AI:n luoman tarinan
4. **Vaihtoehtoisesti**, lataa lemmikkikuva automaattisen kuvauksen luomiseksi
5. **Näe** luova tarina lemmikkisi kuvauksen perusteella

## Miten kaikki toimii yhdessä

Näin koko prosessi etenee, kun luot lemmikkitarinan:

1. **Käyttäjän syöte**: Kuvaat lemmikkisi verkkolomakkeella
2. **Lomakkeen lähetys**: Selaimesi lähettää POST-pyynnön osoitteeseen `/generate-story`
3. **Kontrollerin käsittely**: `PetController` validoi ja puhdistaa syötteen
4. **AI-palvelun kutsu**: `StoryService` lähettää pyynnön GitHub Models API:lle
5. **Tarinan luonti**: AI luo luovan tarinan kuvauksen perusteella
6. **Vastauksen käsittely**: Kontrolleri vastaanottaa tarinan ja lisää sen malliin
7. **Pohjan renderöinti**: Thymeleaf renderöi `result.html`-sivun tarinan kanssa
8. **Näyttö**: Käyttäjä näkee luodun tarinan selaimessaan

**Virheenkäsittelyn kulku:**
Jos AI-palvelu epäonnistuu:
1. Kontrolleri ottaa poikkeuksen kiinni
2. Luo varatarinan valmiiksi kirjoitetuista pohjista
3. Näyttää varatarinan ja ilmoituksen AI:n saatavuudesta
4. Käyttäjä saa silti tarinan, mikä varmistaa hyvän käyttökokemuksen

## AI-integraation ymmärtäminen

### GitHub Models API
Sovellus käyttää GitHub Models -palvelua, joka tarjoaa ilmaisen pääsyn erilaisiin AI-malleihin:

```java
// Authentication with GitHub token
this.openAIClient = OpenAIOkHttpClient.builder()
    .baseUrl("https://models.github.ai/inference")
    .apiKey(githubToken)
    .build();
```

### Kehotusmuotoilu
Palvelu käyttää huolellisesti suunniteltuja kehotuksia hyvien tulosten saamiseksi:

```java
String systemPrompt = "You are a creative storyteller who writes fun, " +
                     "family-friendly short stories about pets. " +
                     "Keep stories under 500 words and appropriate for all ages.";
```

### Vastauksen käsittely
AI:n vastaus otetaan talteen ja validoidaan:

```java
ChatCompletion response = openAIClient.chat().completions().create(params);
String story = response.choices().get(0).message().content().orElse("");
```

## Seuraavat askeleet

Lisää esimerkkejä löydät kohdasta [Luku 04: Käytännön esimerkit](../README.md)

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäistä asiakirjaa sen alkuperäisellä kielellä tulisi pitää ensisijaisena lähteenä. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.