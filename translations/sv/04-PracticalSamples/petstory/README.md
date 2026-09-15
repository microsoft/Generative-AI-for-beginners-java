# Handledning för Pet Story Generator för nybörjare

Ladda upp ett husdjursfoto, analysera det med GPT-5.6 Luna och generera en berättelse från den resulterande beskrivningen. Båda modellförfrågningarna använder `reasoning_effort: none`.

| Komponent | Version |
| --- | --- |
| Java | 21 eller högre |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Innehållsförteckning

- [Förutsättningar](#förutsättningar)
- [Förstå projektstrukturen](#förstå-projektstrukturen)
- [Kärnkomponenter förklarade](#kärnkomponenter-förklarade)
  - [1. Huvudapplikation](#1-huvudapplikation)
  - [2. Webbkontroller](#2-webbkontroller)
  - [3. Berättelsetjänst](#3-berättelsetjänst)
  - [4. Webbmallar](#4-webbmallar)
  - [5. Konfiguration](#5-konfiguration)
- [Köra applikationen](#köra-applikationen)
- [Offline-tester](#offline-tester)
- [Hur allt fungerar tillsammans](#hur-allt-fungerar-tillsammans)
- [Förstå AI-integrationen](#förstå-ai-integrationen)
- [Nästa steg](#nästa-steg)

## Förutsättningar

Innan du börjar, se till att du har:
- Java 21 eller högre installerat
- Maven för beroendehantering
- En Azure AI Foundry-distribution av GPT-5.6 Luna med namnet `gpt-5.6-luna`, eller en `AZURE_OPENAI_DEPLOYMENT`-överskrivning som pekar på den distributionen. Se [Kapitel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) för provisionering och logga in med `az login` för nyckellös autentisering. Distributionen måste stödja bildinmatning och `reasoning_effort: none`.
- Grundläggande förståelse för Java, Spring Boot och webbutveckling

## Förstå projektstrukturen

Pet story-projektet har flera viktiga filer:

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

## Kärnkomponenter förklarade

### 1. Huvudapplikation

**Fil:** `PetStoryApplication.java`

Detta är ingångspunkten för vår Spring Boot-applikation:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Vad detta gör:**
- `@SpringBootApplication`-annoteringen möjliggör automatisk konfiguration och komponentgenomsökning
- Startar en inbäddad webbserver (Tomcat) på port 8080
- Skapar alla nödvändiga Spring beans och tjänster automatiskt

### 2. Webbkontroller

**Fil:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Förfrågan | Lyckat svar |
| --- | --- | --- |
| `GET /` | Ingen kropp | HTML-uppladdningsformulär med CSRF-token |
| `POST /analyze-image` | `multipart/form-data`, fält för fil `image` | JSON: `{"description":"Ett lekfullt husdjur..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, fält `description` | HTML-resultatsida med beskrivningen och den genererade berättelsen |

Båda POST-endpointarna kräver sessionskakan och CSRF-token som erhållits från `GET /`. Uppladdningsskriptet skickar det dolda `_csrf`-värdet i `X-CSRF-TOKEN`-huvudet; berättelseinlämningen skickar det som `_csrf`-formfält. API-klienter måste behålla kakan mellan förfrågningar. Dessa är formulär-endpoints, inte JSON-förfrågningsendpoints.

Beskrivningar måste vara icke tomma och högst 1000 tecken långa. Kontrollen trimmar beskrivningen och tar bort `<`, `>`, dubbla citattecken, apostrofer och `&` innan den skickas till tjänsten. Resultatmallen flyr också modellens utdata med `th:text`.

Bildvalideringsfel returnerar HTTP 400 med ett `error`-fält; modellfel returnerar HTTP 502 med ett `error`-fält och ingen `description`. Ogiltiga berättelsebeskrivningar eller modellfel omdirigerar till `/` med ett synligt felmeddelande. Saknade obligatoriska fält returnerar HTTP 400 och saknade eller ogiltiga CSRF-token returnerar HTTP 403. Inga reservbeskrivningar eller berättelser presenteras som framgångsrika AI-resultat.

### 3. Berättelsetjänst

**Fil:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Det officiella OpenAI Java SDK 4.63.1 anropar Azure AI Foundrys OpenAI-kompatibla Chat Completions API. Azure Identity 1.18.6 levererar en Microsoft Entra-bärartoken via `DefaultAzureCredential`; ingen API-nyckel krävs.

| Operation | Inmatning | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bildbytes kodade som en base64 data-URL med den uppladdade MIME-typen | 300 |
| `generateStory` | En husdjursbeskrivning i ett användarmeddelande | 800 |

Båda förfrågningarna använder den konfigurerade distributionen, som standard `gpt-5.6-luna`, och sätter uttryckligen `ReasoningEffort.NONE` (`reasoning_effort: none`). Ingen av förfrågningarna skickar `temperature` eller den gamla `max_tokens`-parametern.

Bildanalys accepterar JPEG, PNG, GIF och WebP, avvisar tomma bilder och filer över 10MB, och begränsar den resulterande beskrivningen till 1000 tecken. Berättelseprompten begär en familjevänlig kort berättelse. Tomma val eller blankt modellinnehåll är fel, och fel bevarar den ursprungliga orsaken för serverdiagnostik. SDK-klienten stängs när applikationen stängs av.

### 4. Webbmallar

**Fil:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Uppladdningsformulär)

Sidan börjar med en fotoväljare, inte ett textfält för beskrivning. **Analys av bild** förhandsgranskar det valda fotot och postar till `/analyze-image`. Ett lyckat svar visar beskrivningen, fyller det dolda `description`-fältet och visar **Generera berättelse**. Den knappen skickar det befintliga formuläret till `/generate-story`.

Det finns ingen modellnedladdning i webbläsaren eller beroende av CDN. Bildanalys körs på servern via den konfigurerade Azure-distributionen. Fel förblir synliga och möjliggör inte berättelsegenerering med en påhittad beskrivning. Att välja en annan fil rensar den tidigare analysen.

**Fil:** `result.html` (Berättelsevisning)

Visar den genererade berättelsen:

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

**Mallens funktioner:**

1. **Thymeleaf-integration**: Använder `th:`-attribut för dynamiskt innehåll
2. **Responsiv design**: CSS-styling för mobil och desktop
3. **Felhantering**: Visar valideringsfel för användare
4. **Uppladdningshantering**: JavaScript förhandsgranskar fotot, skickar en CSRF-skyddad multipartförfrågan och visar den returnerade beskrivningen

### 5. Konfiguration

**Fil:** `application.properties`

Konfigurationsinställningar för applikationen:

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

**Konfiguration förklarad:**

1. **Filuppladdning**: Både filen och hela multipartförfrågan är begränsade till 10MB; håll foton under den gränsen för att lämna plats för multiparthuvuden
2. **Loggning**: Styr vilken information som loggas under körning
3. **Azure AI Foundry**: Anger endpoint och modelldistribution som ska användas (nyckellös autentisering)
4. **Säkerhet**: CSRF-skyddet är aktiverat; modelldiagonostik loggas på servern, medan kontrollern visar generiska modellfelmeddelanden

## Köra applikationen

### Steg 1: Logga in och ange din endpoint

Autentiseringen är nyckellös (Microsoft Entra ID), så det finns ingen API-nyckel. Logga in och sätt din Foundry-endpoint:

**Windows (Kommandotolk):**
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

**Varför detta behövs:**
- Azure AI Foundry använder Microsoft Entra ID för att autentisera inferensförfrågningar
- Nyckellös autentisering betyder inga hemligheter i din källkod eller miljö
- Ditt konto behöver rollen **Cognitive Services OpenAI User** på resursen

Standardnamnet för distribution är `gpt-5.6-luna`. Om din GPT-5.6 Luna-distribution har ett annat namn, sätt `AZURE_OPENAI_DEPLOYMENT` i samma terminal innan du startar applikationen. Både bildanalys och berättelsegenerering använder denna inställning.

### Steg 2: Bygg och kör

Navigera till projektmappen:
```bash
cd 04-PracticalSamples/petstory
```

Bygg den fristående exekverbara JAR-filen och kör alla offline-tester:
```bash
mvn clean package
```

Starta servern:
```bash
mvn spring-boot:run
```

Applikationen startar på `http://localhost:8080`.

Alternativt, starta den paketerade JAR-filen på en ledig port, till exempel:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

För det kommandot, öppna `http://localhost:8083/`. Samma `/analyze-image` och `/generate-story`-vägar är tillgängliga på den valda porten.

### Steg 3: Testa applikationen

1. **Öppna** `http://localhost:8080` i din webbläsare
2. **Välj** ett tydligt husdjursfoto i JPEG-, PNG-, GIF- eller WebP-format, under 10MB
3. **Klicka** på "Analyze Image" och vänta på husdjursbeskrivningen
4. **Klicka** på "Generate Story" efter en lyckad analys
5. **Titta på** berättelsen och använd länken på resultatsidan för att återgå till uppladdningsformuläret

Det framgångsrika bild-till-berättelse-flödet gör två modellanrop, ett per knapp. Live-inferens förbrukar din distributions kvot och kan medföra kostnader; kör röktester sekventiellt när en rate-begränsad distribution delas. Att ladda hemsidan anropar inte modellen.

## Offline-tester

Från provmappen, kör:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) fångar riktiga OpenAI SDK-förfrågningar med en loopback HTTP-fixtur. Den kontrollerar båda förfrågningarnas distribution, `reasoning_effort: none`, tokenbegränsningar, bildpayload, inmatningsvalidering, tomma svar och upstream-fel.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) använder MockMvc med en mockad modelltjänst för att testa de renderade Thymeleaf-sidorna, uppladdningskontraktet, CSRF, validering, utdataescaper och synliga fel. Dessa tester behöver inte Azure-referenser och gör aldrig betalade Azure-inferensanrop. Maven skriver Surefire-rapporter under `target/surefire-reports`.

## Hur allt fungerar tillsammans

Här är det kompletta flödet när du genererar en husdjursberättelse:

1. **Foto val**: Du väljer en husdjursbild i uppladdningsformuläret
2. **Bilduppladdning**: "Analyze Image" skickar en multipart POST till `/analyze-image` med CSRF-huvudet
3. **Bildanalys**: `StoryService` skickar bilden till GPT-5.6 Luna med reasoning satt till `none`
4. **Beskrivningsvisning**: Webbläsaren visar den returnerade beskrivningen och sparar den i formuläret
5. **Berättelseinlämning**: "Generate Story" postar `description` och `_csrf` till `/generate-story`
6. **Berättelsegenerering**: Kontrollern validerar beskrivningen och anropar samma distribution med reasoning satt till `none`
7. **Mallrendering**: Thymeleaf flyr och visar beskrivningen och berättelsen på resultatsidan

**Felhanteringsflöde:**
Om modellen misslyckas loggar servern orsaken. Bildanalys returnerar HTTP 502 och webbläsaren visar felet utan att visa "Generate Story". Berättelsegenerering omdirigerar till formuläret med ett felmeddelande. Ingen av vägarna ersätter tyst ett förskrivet resultat.

## Förstå AI-integrationen

### Azure AI Foundry (nyckellös)
Tjänsten konfigurerar SDK med din resurs `/openai/v1/` endpoint. `DefaultAzureCredential` och `AuthenticationUtil.getBearerTokenSupplier` levererar Microsoft Entra-tokens för `https://ai.azure.com/.default`. Lokal utveckling kan använda din Azure CLI-inloggning; en Azure-hostad app kan använda en hanterad identitet med nödvändiga resursbehörigheter.

### Prompt-engineering
Bildanalysen begär observerbara husdjursdrag i ett kort stycke och instruerar modellen att behandla text i bilden som data, inte instruktioner. Berättelsegenereringen använder den returnerade beskrivningen i en separat familjevänlig skrivförfrågan. Ingen av anropen möjliggör reasoning eller sätter temperaturöverskrivning.

### Svarshantering
Den delade svarshanteraren avvisar saknade val och tomt eller enbart blankstegsinnehåll, trimmar giltigt innehåll och bevarar upstream-fel. Bildbeskrivningar begränsas till 1000 tecken för att passa det efterföljande berättelseformuläret. Den ursprungliga modellfelet bevaras för diagnostik men visas inte för användaren.

## Nästa steg

För fler exempel, se [Kapitel 04: Praktiska exempel](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->