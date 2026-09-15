# Handleiding Pet Story Generator voor Beginners

Upload een huisdierfoto, analyseer deze met GPT-5.6 Luna en genereer een verhaal op basis van de resulterende beschrijving. Beide modelverzoeken gebruiken `reasoning_effort: none`.

| Component | Versie |
| --- | --- |
| Java | 21 of hoger |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Inhoudsopgave

- [Vereisten](#vereisten)
- [Het Projectstructuur Begrijpen](#het-projectstructuur-begrijpen)
- [Kerncomponenten Uitgelegd](#kerncomponenten-uitgelegd)
  - [1. Hoofd Applicatie](#1-hoofd-applicatie)
  - [2. Web Controller](#2-web-controller)
  - [3. Verhaalservice](#3-verhaalservice)
  - [4. Web Sjablonen](#4-web-sjablonen)
  - [5. Configuratie](#5-configuratie)
- [Applicatie Uitvoeren](#applicatie-uitvoeren)
- [Offline Tests](#offline-tests)
- [Hoe Het Helemaal Samen Werkt](#hoe-het-helemaal-samen-werkt)
- [Het AI-integratie Begrijpen](#het-ai-integratie-begrijpen)
- [Volgende Stappen](#volgende-stappen)

## Vereisten

Voordat je begint, zorg dat je hebt:
- Java 21 of hoger geïnstalleerd
- Maven voor dependencybeheer
- Een Azure AI Foundry-implementatie van GPT-5.6 Luna genaamd `gpt-5.6-luna`, of een `AZURE_OPENAI_DEPLOYMENT` override die naar die implementatie verwijst. Zie [Hoofdstuk 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) voor provisioning en meld je aan met `az login` voor keyless authenticatie. De implementatie moet afbeeldinginvoer en `reasoning_effort: none` ondersteunen.
- Basiskennis van Java, Spring Boot en webontwikkeling

## Het Projectstructuur Begrijpen

Het pet story project bevat meerdere belangrijke bestanden:

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

## Kerncomponenten Uitgelegd

### 1. Hoofd Applicatie

**Bestand:** `PetStoryApplication.java`

Dit is het startpunt van onze Spring Boot-applicatie:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Wat dit doet:**
- De `@SpringBootApplication` annotatie schakelt auto-configuratie en component scanning in
- Start een embedded webserver (Tomcat) op poort 8080
- Maakt automatisch alle benodigde Spring beans en services aan

### 2. Web Controller

**Bestand:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Verzoek | Succesvolle respons |
| --- | --- | --- |
| `GET /` | Geen body | HTML-uploadformulier met een CSRF-token |
| `POST /analyze-image` | `multipart/form-data`, bestandsveld `image` | JSON: `{"description":"Een speels huisdier..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, veld `description` | HTML-resultaatpagina met de beschrijving en het gegenereerde verhaal |

Beide POST-endpoints vereisen de sessiecookie en CSRF-token verkregen via `GET /`. Het uploads-script verstuurt de verborgen `_csrf` waarde in de `X-CSRF-TOKEN` header; verhaalindiening verstuurt deze als het `_csrf` formulierveld. API-clients moeten de cookie tussen verzoeken bewaren. Dit zijn formele endpoints, geen JSON-verzoek endpoints.

Beschrijvingen moeten niet leeg zijn en maximaal 1000 tekens bevatten. De controller trimt de beschrijving en verwijdert `<`, `>`, dubbele aanhalingstekens, apostroffen en `&` voordat deze aan de service wordt doorgegeven. De resultaat-sjabloon escapt ook modeloutput met `th:text`.

Afbeeldingsvalidatiefouten geven HTTP 400 terug met een `error` veld; modelfouten geven HTTP 502 terug met een `error` veld en geen `description`. Ongeldige verhaalbeschrijvingen of modelfouten leiden door naar `/` met een zichtbare foutmelding. Ontbrekende verplichte velden geven HTTP 400, en ontbrekende of ongeldige CSRF-tokens geven HTTP 403. Geen fallback-beschrijvingen of verhalen worden als succesvolle AI-resultaten weergegeven.

### 3. Verhaalservice

**Bestand:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

De officiële OpenAI Java SDK 4.63.1 roept de OpenAI-compatibele Chat Completions API van Azure AI Foundry aan. Azure Identity 1.18.6 levert een Microsoft Entra bearer token via `DefaultAzureCredential`; een API-sleutel is niet nodig.

| Operatie | Invoer | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Afbeeldingsbytes gecodeerd als een base64 data-URL met het geüploade MIME-type | 300 |
| `generateStory` | Een huisdierbeschrijving in een gebruikersbericht | 800 |

Beide verzoeken gebruiken de geconfigureerde deployment, standaard `gpt-5.6-luna`, en stellen expliciet `ReasoningEffort.NONE` (`reasoning_effort: none`) in. Geen van beide verzoeken verstuurt `temperature` of de legacy `max_tokens` parameter.

Afbeeldingsanalyse accepteert JPEG, PNG, GIF en WebP, keurt lege afbeeldingen en bestanden groter dan 10MB af, en beperkt de resulterende beschrijving tot 1000 tekens. De verhaalprompt vraagt om een gezinsvriendelijk kort verhaal. Lege keuzes of blanco modelinhoud zijn fouten, en fouten behouden de oorspronkelijke oorzaak voor serverdiagnostiek. De SDK-client wordt gesloten bij het afsluiten van de applicatie.

### 4. Web Sjablonen

**Bestand:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Uploadformulier)

De pagina start met een fotokeuze, niet met een tekstvak voor een beschrijving. **Analyseer Afbeelding** toont een voorbeeld van de geselecteerde foto en verstuurt deze naar `/analyze-image`. Een succesvolle respons toont de beschrijving, vult het verborgen `description` veld en toont **Genereer Verhaal**. Die knop verstuurt het bestaande formulier naar `/generate-story`.

Er is geen modeldownload in de browser of afhankelijkheid van een CDN. Afbeeldingsanalyse vindt plaats op de server via de geconfigureerde Azure-deployment. Fouten blijven zichtbaar en maken geen verhaal-generatie met een gefabriceerde beschrijving mogelijk. Het selecteren van een ander bestand wist de vorige analyse.

**Bestand:** `result.html` (Verhaaltentoonstelling)

Toont het gegenereerde verhaal:

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

**Sjabloonkenmerken:**

1. **Thymeleaf Integratie**: Gebruikt `th:` attributen voor dynamische inhoud
2. **Responsief Ontwerp**: CSS-styling voor mobiel en desktop
3. **Foutafhandeling**: Toont validatiefouten aan gebruikers
4. **Uploadafhandeling**: JavaScript toont een voorbeeld van de foto, verstuurt een CSRF-beschermd multipart-verzoek en toont de teruggegeven beschrijving

### 5. Configuratie

**Bestand:** `application.properties`

Configuratie-instellingen voor de applicatie:

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

**Configuratie uitgelegd:**

1. **Bestandsupload**: Zowel het bestand als het complete multipart-verzoek zijn begrensd op 10MB; houd foto's onder die limiet om ruimte te laten voor multipart-headers
2. **Logging**: Bepaalt welke informatie wordt gelogd tijdens uitvoering
3. **Azure AI Foundry**: Specificeert de endpoint en modeldeployments die gebruikt worden (keyless authenticatie)
4. **Beveiliging**: CSRF-bescherming blijft ingeschakeld; modeldiagnostiek wordt gelogd op de server, terwijl de controller generieke model-foutmeldingen toont

## Applicatie Uitvoeren

### Stap 1: Aanmelden en Jouw Endpoint Instellen

Authenticatie is keyless (Microsoft Entra ID), dus er is geen API-sleutel. Meld je aan en stel je Foundry endpoint in:

**Windows (Command Prompt):**
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

**Waarom dit nodig is:**
- Azure AI Foundry gebruikt Microsoft Entra ID om inference-verzoeken te authenticeren
- Keyless authenticatie betekent geen geheimen in je broncode of omgeving
- Je account moet de rol **Cognitive Services OpenAI User** op de resource hebben

De standaard deploymentnaam is `gpt-5.6-luna`. Als jouw GPT-5.6 Luna deployment een andere naam heeft, stel dan `AZURE_OPENAI_DEPLOYMENT` in dezelfde terminal in voordat je de applicatie start. Zowel afbeeldinganalyse als verhaal generatie gebruiken deze instelling.

### Stap 2: Bouwen en Uitvoeren

Navigeer naar de projectmap:
```bash
cd 04-PracticalSamples/petstory
```

Bouw de standalone uitvoerbare JAR en voer alle offline-tests uit:
```bash
mvn clean package
```

Start de server:
```bash
mvn spring-boot:run
```

De applicatie start op `http://localhost:8080`.

Start alternatief de verpakte JAR op een vrije poort, bijvoorbeeld:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Voor dat commando open je `http://localhost:8083/`. Dezelfde routes `/analyze-image` en `/generate-story` zijn beschikbaar op de geselecteerde poort.

### Stap 3: Test de Applicatie

1. **Open** `http://localhost:8080` in je browser
2. **Selecteer** een duidelijke huisdierfoto in JPEG-, PNG-, GIF- of WebP-formaat, onder 10MB
3. **Klik** op "Analyseer Afbeelding" en wacht op de huisdierbeschrijving
4. **Klik** op "Genereer Verhaal" na succesvolle analyse
5. **Bekijk** het verhaal en gebruik de link op de resultaatpagina om terug te keren naar het uploadformulier

De succesvolle foto-naar-verhaal flow maakt twee modeloproepen, één per knop. Live inference verbruikt de quota van jouw deployment en kan kosten veroorzaken; voer rooktests serieel uit bij het delen van een rate-limited deployment. Het laden van de startpagina roept het model niet aan.

## Offline Tests

Vanuit de voorbeeldmap, voer uit:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) vastlegt echte OpenAI SDK-verzoeken met een loopback HTTP-fixture. Het controleert zowel de deployment van beide verzoeken, `reasoning_effort: none`, tokenlimieten, afbeeldingen payload, invoervalidatie, lege reacties en upstream-fouten.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) gebruikt MockMvc met een gemockte modelservice om de gerenderde Thymeleaf-pagina's, uploadcontract, CSRF, validatie, output-escaping en zichtbare fouten te testen. Deze tests hebben geen Azure-referenties nodig en roepen nooit betaalde Azure-inference aan. Maven schrijft Surefire rapporten onder `target/surefire-reports`.

## Hoe Het Helemaal Samen Werkt

Hier is de volledige flow wanneer je een pet-verhaal genereert:

1. **Foto Selectie**: Je kiest een huisdierafbeelding in het uploadformulier
2. **Afbeelding Uploaden**: "Analyseer Afbeelding" verstuurt een multipart POST naar `/analyze-image` met de CSRF-header
3. **Afbeeldingsanalyse**: `StoryService` verstuurt de afbeelding naar GPT-5.6 Luna met reasoning op `none`
4. **Beschrijving Weergeven**: De browser toont de geretourneerde beschrijving en slaat deze op in het formulier
5. **Verhaal Indienen**: "Genereer Verhaal" verstuurt `description` en `_csrf` naar `/generate-story`
6. **Verhaal Genereren**: De controller valideert de beschrijving en roept dezelfde deployment aan met reasoning op `none`
7. **Sjabloon Renderen**: Thymeleaf escaped en toont de beschrijving en het verhaal op de resultaatpagina

**Foutafhandelingsflow:**
Als het model faalt, logt de server de oorzaak. Afbeeldingsanalyse geeft HTTP 502 terug en de browser toont de fout zonder "Genereer Verhaal" weer te geven. Verhaalgeneratie leidt door naar het formulier met een foutmelding. Geen van beide paden substitueert stilletjes een vooraf geschreven resultaat.

## Het AI-integratie Begrijpen

### Azure AI Foundry (keyless)
De service configureert de SDK met je resource’s `/openai/v1/` endpoint. `DefaultAzureCredential` en `AuthenticationUtil.getBearerTokenSupplier` leveren Microsoft Entra tokens voor `https://ai.azure.com/.default`. Lokale ontwikkeling kan je Azure CLI aanmelding gebruiken; een Azure-gehoste app kan een managed identity gebruiken met de benodigde resource-permissies.

### Prompt Engineering
Afbeeldingsanalyse vraagt om waarneembare huisdierkenmerken in een korte paragraaf en zegt tegen het model om tekst in de afbeelding als data te behandelen, niet als instructies. Verhaalgeneratie gebruikt de geretourneerde beschrijving in een aparte, gezinsvriendelijke schrijfopdracht. Geen van beide oproepen zet reasoning aan of stelt een temperatuur override in.

### Response Processing
De gedeelde response handler verwerpt ontbrekende keuzes en lege of alleen witruimte-inhoud, trimt geldige inhoud en behoudt upstream-fouten. Afbeeldingsbeschrijvingen worden beperkt tot 1000 tekens om in het volgende verhaal-formulier te passen. De oorspronkelijke modelfout wordt bewaard voor diagnostiek maar niet aan de gebruiker getoond.

## Volgende Stappen

Voor meer voorbeelden, zie [Hoofdstuk 04: Praktische voorbeelden](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->