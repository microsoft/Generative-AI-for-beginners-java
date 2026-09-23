# Pet Story Generator Tutorial for Beginners

Upload et kæledyrsbillede, analyser det med GPT-5.6 Luna, og generer en historie ud fra den resulterende beskrivelse. Begge modelanmodninger bruger `reasoning_effort: none`.

| Komponent | Version |
| --- | --- |
| Java | 21 eller højere |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Indholdsfortegnelse

- [Forudsætninger](#forudsætninger)
- [Forståelse af projektstrukturen](#forståelse-af-projektstrukturen)
- [Kernekomponenter forklaret](#kernekomponenter-forklaret)
  - [1. Hovedapplikation](#1-hovedapplikation)
  - [2. Web Controller](#2-web-controller)
  - [3. Story Service](#3-story-service)
  - [4. Webskabeloner](#4-webskabeloner)
  - [5. Konfiguration](#5-konfiguration)
- [Kørsel af applikationen](#kørsel-af-applikationen)
- [Offline tests](#offline-tests)
- [Hvordan det hele hænger sammen](#hvordan-det-hele-hænger-sammen)
- [Forståelse af AI-integration](#forståelse-af-ai-integration)
- [Næste skridt](#næste-skridt)

## Forudsætninger

Før du starter, skal du sikre dig, at du har:
- Java 21 eller højere installeret
- Maven til afhængighedsstyring
- En Azure AI Foundry-udrulning af GPT-5.6 Luna navngivet `gpt-5.6-luna`, eller en `AZURE_OPENAI_DEPLOYMENT` overskrivelse, der peger på denne udrulning. Se [Kapitel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) for provisioning og log ind med `az login` for nøglefri autentifikation. Udrulningen skal understøtte billedinput og `reasoning_effort: none`.
- Grundlæggende kendskab til Java, Spring Boot og webudvikling

## Forståelse af projektstrukturen

Pet story-projektet har flere vigtige filer:

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

## Kernekomponenter forklaret

### 1. Hovedapplikation

**Fil:** `PetStoryApplication.java`

Dette er indgangspunktet for vores Spring Boot-applikation:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Hvad dette gør:**
- `@SpringBootApplication` annotationen aktiverer auto-konfiguration og komponent-scanning
- Starter en indlejret webserver (Tomcat) på port 8080
- Opretter automatisk alle nødvendige Spring beans og services

### 2. Web Controller

**Fil:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Anmodning | Succesfuldt svar |
| --- | --- | --- |
| `GET /` | Ingen body | HTML-uploadformular med en CSRF-token |
| `POST /analyze-image` | `multipart/form-data`, filfelt `image` | JSON: `{"description":"Et legesygt kæledyr..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, felt `description` | HTML-resultatside med beskrivelsen og genereret historie |

Begge POST-endpoints kræver sessionscookie og CSRF-token opnået fra `GET /`. Upload-skriptet sender den skjulte `_csrf` værdi i `X-CSRF-TOKEN` headeren; historieindsendelse sender den som `_csrf` formfelt. API-klienter skal bevare cookien mellem anmodninger. Dette er formlike endpoints, ikke JSON-anmodninger.

Beskrivelser skal være ikke-tomme og højst 1000 tegn lange. Controlleren trimmer beskrivelsen og fjerner `<`, `>`, dobbelte citationstegn, apostrofer og `&` før den videregives til servicen. Resultatskabelonen undslipper også modeloutput med `th:text`.

Fejl ved billedvalidering returnerer HTTP 400 med et `error` felt; modellfejl returnerer HTTP 502 med et `error` felt og ingen `description`. Ugyldige historiebeskrivelser eller modellfejl omdirigerer til `/` med en synlig fejl. Manglende nødvendige felter returnerer HTTP 400, og manglende eller ugyldige CSRF-tokens returnerer HTTP 403. Der præsenteres ingen fallback-beskrivelser eller historier som succesfulde AI-resultater.

### 3. Story Service

**Fil:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Den officielle OpenAI Java SDK 4.63.1 kalder Azure AI Foundrys OpenAI-kompatible Chat Completions API. Azure Identity 1.18.6 leverer en Microsoft Entra bearer token via `DefaultAzureCredential`; ingen API-nøgle er påkrævet.

| Operation | Input | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Billedbytes kodet som en base64 data-URL med den uploadede MIME-type | 300 |
| `generateStory` | En kæledyrsbeskrivelse i en brugermeddelelse | 800 |

Begge anmodninger bruger den konfigurerede udrulning, som standard `gpt-5.6-luna`, og sætter eksplicit `ReasoningEffort.NONE` (`reasoning_effort: none`). Ingen af anmodningerne sender `temperature` eller den gamle `max_tokens` parameter.

Billedanalyse accepterer JPEG, PNG, GIF og WebP, afviser tomme billeder og filer over 10MB, og begrænser den resulterende beskrivelse til 1000 tegn. Historieprompten anmoder om en familievenlig kort historie. Tomme valg eller tomt modelindhold er fejl, og fejl bevarer den oprindelige årsag til serverdiagnostik. SDK-klienten lukkes når applikationen lukkes ned.

### 4. Webskabeloner

**Fil:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Uploadformular)

Siden starter med en foto-vælger, ikke et beskrivelsesfelt. **Analyze Image** forhåndsviser det valgte foto og sender det til `/analyze-image`. En succesfuld respons viser beskrivelsen, udfylder det skjulte `description` felt og afslører **Generate Story**. Den knap sender den eksisterende formular til `/generate-story`.

Der er ingen browser modeldownload eller CDN-afhængighed. Billedanalysen kører på serveren via den konfigurerede Azure-udrulning. Fejl forbliver synlige og tillader ikke historiegenerering med en fabrikeret beskrivelse. Valg af en anden fil rydder den tidligere analyse.

**Fil:** `result.html` (Historievisning)

Viser den genererede historie:

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

**Skabelonfunktioner:**

1. **Thymeleaf-integration**: Bruger `th:` attributter til dynamisk indhold
2. **Responsivt design**: CSS-styling til mobil og desktop
3. **Fejlhåndtering**: Viser valideringsfejl for brugere
4. **Uploadhåndtering**: JavaScript forhåndsviser billedet, sender en CSRF-beskyttet multipart-anmodning, og viser den returnerede beskrivelse

### 5. Konfiguration

**Fil:** `application.properties`

Konfigurationsindstillinger for applikationen:

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

**Konfiguration forklaret:**

1. **Fil-upload**: Både filen og hele multipart-anmodningen er begrænset til 10MB; hold fotos under denne grænse for at give plads til multipart-headere
2. **Logning**: Styrer hvad der logges under udførelsen
3. **Azure AI Foundry**: Angiver endpoint og modeludrulning til brug (nøglefri autentifikation)
4. **Sikkerhed**: CSRF-beskyttelse forbliver aktiveret; model-diagnostik logges på serveren, mens controlleren viser generiske fejlbeskeder ved modellfejl

## Kørsel af applikationen

### Trin 1: Log ind og indstil dit endpoint

Autentifikation er nøglefri (Microsoft Entra ID), så der er ingen API-nøgle. Log ind og indstil dit Foundry-endpoint:

**Windows (Kommandoprompt):**
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

**Hvorfor dette er nødvendigt:**
- Azure AI Foundry bruger Microsoft Entra ID til at autentificere inferensanmodninger
- Nøglefri autentifikation betyder ingen hemmeligheder i din kildekode eller miljø
- Din konto skal have rollen **Cognitive Services OpenAI User** på ressourcen

Standardudrulningsnavnet er `gpt-5.6-luna`. Hvis din GPT-5.6 Luna-udrulning har et andet navn, sæt `AZURE_OPENAI_DEPLOYMENT` i samme terminal før du starter applikationen. Både billedanalyse og historiegenerering bruger denne indstilling.

### Trin 2: Byg og kør

Naviger til projektmappen:
```bash
cd 04-PracticalSamples/petstory
```

Byg den standalone eksekverbare JAR og kør alle offline tests:
```bash
mvn clean package
```

Start serveren:
```bash
mvn spring-boot:run
```

Applikationen starter på `http://localhost:8080`.

Alternativt start den pakkede JAR på en ledig port, for eksempel:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

For denne kommando, åbn `http://localhost:8083/`. Samme `/analyze-image` og `/generate-story` ruter er tilgængelige på den valgte port.

### Trin 3: Test applikationen

1. **Åbn** `http://localhost:8080` i din browser
2. **Vælg** et klart kæledyrsfoto i JPEG, PNG, GIF eller WebP format, under 10MB
3. **Klik** på "Analyze Image" og vent på kæledyrsbeskrivelsen
4. **Klik** på "Generate Story" efter succesfuld analyse
5. **Se** historien og brug linket på resultatsiden for at vende tilbage til uploadformularen

Den succesfulde foto-til-historie proces foretager to modelkald, ét per knap. Live inferens bruger din udrulnings kvote og kan medføre omkostninger; kør smoke-tests sekventielt når kvote er begrænset og deles. Indlæsning af startsiden kalder ikke modellen.

## Offline tests

Fra sample-mappen, kør:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) optager virkelige OpenAI SDK-anmodninger med en loopback HTTP fixture. Den tjekker begge anmodningers udrulning, `reasoning_effort: none`, token-grænser, billedpayload, inputvalidering, tomme svar og upstream-fejl.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) bruger MockMvc med en mocket modelservice til at teste de gengivne Thymeleaf-sider, uploadkontrakt, CSRF, validering, output-escapning og synlige fejl. Disse tests har ikke brug for Azure legitimationsoplysninger og kalder aldrig betalt Azure inferens. Maven skriver Surefire-rapporter under `target/surefire-reports`.

## Hvordan det hele hænger sammen

Her er den komplette flow, når du generer en kæledyrshistorie:

1. **Fotovalg**: Du vælger et kæledyrsbillede i uploadformularen
2. **Billedupload**: "Analyze Image" sender en multipart POST til `/analyze-image` med CSRF-headeren
3. **Billedanalyse**: `StoryService` sender billedet til GPT-5.6 Luna med reasoning sat til `none`
4. **Beskrivelsesvisning**: Browseren viser den returnerede beskrivelse og gemmer den i formularen
5. **Historieindsendelse**: "Generate Story" poster `description` og `_csrf` til `/generate-story`
6. **Historiegenerering**: Controlleren validerer beskrivelsen og kalder samme udrulning med reasoning sat til `none`
7. **Skabelonrendring**: Thymeleaf escape'er og viser beskrivelsen og historien på resultatsiden

**Fejlhåndteringsflow:**
Hvis modellen fejler, logger serveren årsagen. Billedanalyse returnerer HTTP 502, og browseren viser fejlen uden at afsløre "Generate Story". Historiegenerering omdirigerer til formularen med en fejlbesked. Ingen af vejene erstatter lydløst et forudskrevet resultat.

## Forståelse af AI-integration

### Azure AI Foundry (nøglefri)
Servicen konfigurerer SDK med din ressourcers `/openai/v1/` endpoint. `DefaultAzureCredential` og `AuthenticationUtil.getBearerTokenSupplier` leverer Microsoft Entra tokens til `https://ai.azure.com/.default`. Lokal udvikling kan bruge dit Azure CLI-login; en Azure-hostet app kan bruge en managed identity med nødvendige ressource-tilladelser.

### Prompt Engineering
Billedanalyse anmoder om observerbare kæledyrsdetaljer i et kort afsnit og instruerer modellen til at behandle teksten i billedet som data, ikke instruktioner. Historiegenerering bruger den returnerede beskrivelse i en separat, familievenlig skriveanmodning. Ingen af kald tillader reasoning eller sætter temperatur-override.

### Responsbehandling
Den delte responshåndtering afviser manglende valg og tomt eller kun whitespace-indhold, trimmer gyldigt indhold og bevarer upstream-fejl. Billedbeskrivelser begrænses til 1000 tegn for at passe til den efterfølgende historiformular. Den oprindelige modellfejl fastholdes til diagnostik, men gengives ikke for brugeren.

## Næste skridt

For flere eksempler, se [Kapitel 04: Praktiske prøver](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->