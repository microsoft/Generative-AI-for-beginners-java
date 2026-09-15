# Veiledningskurs for Generering av Kjæledyrhistorier for Nybegynnere

Last opp et kjæledyrbilde, analyser det med GPT-5.6 Luna, og generer en historie basert på den resulterende beskrivelsen. Begge modellforespørslene bruker `reasoning_effort: none`.

| Komponent | Versjon |
| --- | --- |
| Java | 21 eller nyere |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Innholdsfortegnelse

- [Forutsetninger](#forutsetninger)
- [Forståelse av Prosjektstrukturen](#forståelse-av-prosjektstrukturen)
- [Forklaring av Kjernesystemer](#forklaring-av-kjernesystemer)
  - [1. Hovedapplikasjonen](#1-hovedapplikasjonen)
  - [2. Webkontroller](#2-webkontroller)
  - [3. Historietjeneste](#3-historietjeneste)
  - [4. Webmaler](#4-webmaler)
  - [5. Konfigurasjon](#5-konfigurasjon)
- [Kjøre Applikasjonen](#kjøre-applikasjonen)
- [Offline Tester](#offline-tester)
- [Hvordan Alt Henger Sammen](#hvordan-alt-henger-sammen)
- [Forståelse av AI-integrasjon](#forståelse-av-ai-integrasjon)
- [Neste Steg](#neste-steg)

## Forutsetninger

Før du begynner, sørg for at du har:
- Java 21 eller nyere installert
- Maven for avhengighetsadministrasjon
- En Azure AI Foundry-distribusjon av GPT-5.6 Luna kalt `gpt-5.6-luna`, eller en `AZURE_OPENAI_DEPLOYMENT`-overstyring som peker til denne distribusjonen. Se [Kapittel 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) for provisjonering og logg inn med `az login` for nøkkelfri autentisering. Distribusjonen må støtte bildeinput og `reasoning_effort: none`.
- Grunnleggende forståelse av Java, Spring Boot og webutvikling

## Forståelse av Prosjektstrukturen

Kjæledyrhistorieprosjektet har flere viktige filer:

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

## Forklaring av Kjernesystemer

### 1. Hovedapplikasjonen

**Fil:** `PetStoryApplication.java`

Dette er inngangspunktet for vår Spring Boot-applikasjon:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Dette gjør den:**
- `@SpringBootApplication`-annotasjonen aktiverer automatisk konfigurasjon og komponentskanning
- Starter en innebygd webserver (Tomcat) på port 8080
- Oppretter alle nødvendige Spring-bønner og tjenester automatisk

### 2. Webkontroller

**Fil:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endepunkt | Forespørsel | Vellykket respons |
| --- | --- | --- |
| `GET /` | Ingen kropp | HTML opplastingsskjema med CSRF-token |
| `POST /analyze-image` | `multipart/form-data`, filfelt `image` | JSON: `{"description":"Et lekent kjæledyr..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, felt `description` | HTML resultatside med beskrivelse og generert historie |

Begge POST-endepunktene krever sesjonskake og CSRF-token hentet fra `GET /`. Opplastingsskriptet sender den skjulte `_csrf`-verdien i `X-CSRF-TOKEN`-headeren; historielasting sender den som `_csrf`-skjema-felt. API-klienter må bevare kaken mellom forespørsler. Dette er skjemaendepunkter, ikke JSON-forespørselsendepunkter.

Beskrivelser må være ikke-tomme og ikke lengre enn 1000 tegn. Kontrolleren trimmer beskrivelsen og fjerner `<`, `>`, doble anførselstegn, apostrofer og `&` før den sender til tjenesten. Resultatmalen unngår også modellutdata med `th:text`.

Feil ved bildevalidering returnerer HTTP 400 med et `error`-felt; modellfeil returnerer HTTP 502 med et `error`-felt og ingen `description`. Ugyldige historiebeskrivelser eller modellfeil omdirigerer til `/` med synlig feil. Manglende obligatoriske felt returnerer HTTP 400, og manglende eller ugyldige CSRF-tokener returnerer HTTP 403. Ingen reservebeskrivelser eller historier presenteres som vellykkede AI-resultater.

### 3. Historietjeneste

**Fil:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Den offisielle OpenAI Java SDK 4.63.1 kaller Azure AI Foundrys OpenAI-kompatible Chat Completions API. Azure Identity 1.18.6 leverer et Microsoft Entra-bærertoken via `DefaultAzureCredential`; ingen API-nøkkel er nødvendig.

| Operasjon | Inndata | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bildedata kodet som base64-data-URL med opplastet MIME-type | 300 |
| `generateStory` | En kjæledyrbeskrivelse i en brukermelding | 800 |

Begge forespørslene bruker den konfigurerte distribusjonen, som standard `gpt-5.6-luna`, og setter eksplisitt `ReasoningEffort.NONE` (`reasoning_effort: none`). Ingen av forespørslene sender `temperature` eller den eldre `max_tokens`-parameteren.

Bildeanalyse aksepterer JPEG, PNG, GIF og WebP, forkaster tomme bilder og filer over 10MB, og begrenser den resulterende beskrivelsen til 1000 tegn. Historieprompten ber om en familievennlig kort historie. Tomme valg eller blankt modellinnhold er feil, og feil bevarer den opprinnelige årsaken for serverdiagnostikk. SDK-klienten lukkes når applikasjonen avsluttes.

### 4. Webmaler

**Fil:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Opplastingsskjema)

Siden starter med en fotovelger, ikke et tekstområde for beskrivelse. **Analyser bilde** forhåndsviser det valgte bildet og sender det til `/analyze-image`. Et vellykket svar viser beskrivelsen, fyller det skjulte `description`-feltet, og avslører **Generer historie**. Den knappen sender det eksisterende skjemaet til `/generate-story`.

Det er ingen nedlasting av modell i nettleseren eller CDN-avhengighet. Bildeanalyse kjøres på serveren gjennom den konfigurerte Azure-distribusjonen. Feil er synlige og tillater ikke historiegenerering med fabrikert beskrivelse. Å velge en annen fil fjerner forrige analyse.

**Fil:** `result.html` (Historievennlig visning)

Viser den genererte historien:

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

**Malfunksjoner:**

1. **Thymeleaf-integrasjon**: Bruker `th:`-attributter for dynamisk innhold
2. **Responsivt Design**: CSS-styling for mobil og desktop
3. **Feilhåndtering**: Viser valideringsfeil til brukerne
4. **Opplastingshåndtering**: JavaScript forhåndsviser bildet, sender en CSRF-beskyttet multipart-forespørsel, og viser den returnerte beskrivelsen

### 5. Konfigurasjon

**Fil:** `application.properties`

Konfigurasjonsinnstillinger for applikasjonen:

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

**Konfigurasjon forklart:**

1. **Filopplastning**: Både filen og den komplette multipart-forespørselen er begrenset til 10MB; hold bilder under denne grensen for å gi plass til multipart-headere
2. **Logging**: Styrer hvilken informasjon som logges under kjøring
3. **Azure AI Foundry**: Spesifiserer endepunkt og modell-distribusjon som skal brukes (nøkkelfri autentisering)
4. **Sikkerhet**: CSRF-beskyttelse forblir aktivert; modelldiagnostikk logges på serveren, mens kontrolleren viser generiske modellfeilmeldinger

## Kjøre Applikasjonen

### Trinn 1: Logg Inn og Sett Endepunktet Ditt

Autentisering er nøkkelfri (Microsoft Entra ID), så det finnes ingen API-nøkkel. Logg inn og sett Foundry-endepunktet ditt:

**Windows (Kommandolinje):**
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

**Hvorfor dette er nødvendig:**
- Azure AI Foundry bruker Microsoft Entra ID for autentisering av inferensforespørsler
- Nøkkelfri autentisering betyr ingen hemmeligheter i din kildekode eller miljø
- Kontoen din trenger rollen **Cognitive Services OpenAI User** på ressursen

Standard distribusjonsnavn er `gpt-5.6-luna`. Hvis distribusjonen din for GPT-5.6 Luna har et annet navn, sett `AZURE_OPENAI_DEPLOYMENT` i samme terminal før du starter applikasjonen. Både bildeanalyse og historiegenerering bruker denne innstillingen.

### Trinn 2: Bygg og Kjør

Naviger til prosjektmappen:
```bash
cd 04-PracticalSamples/petstory
```

Bygg den frittstående kjørbare JAR-filen og kjør alle offline-tester:
```bash
mvn clean package
```

Start serveren:
```bash
mvn spring-boot:run
```

Applikasjonen vil starte på `http://localhost:8080`.

Alternativt, start den pakkede JAR-filen på en ledig port, for eksempel:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

For den kommandoen, åpne `http://localhost:8083/`. De samme rutene `/analyze-image` og `/generate-story` er tilgjengelige på den valgte porten.

### Trinn 3: Test Applikasjonen

1. **Åpne** `http://localhost:8080` i nettleseren din
2. **Velg** et klart kjæledyrbilde i JPEG, PNG, GIF eller WebP-format, under 10MB
3. **Klikk** på "Analyze Image" og vent på kjæledyrbeskrivelsen
4. **Klikk** på "Generate Story" etter vellykket analyse
5. **Se** historien og bruk lenken på resultatssiden for å gå tilbake til opplastingsskjemaet

Den vellykkede bilde-til-historie-flyten gjør to modellkall, ett per knapp. Live inferens bruker ditt distribusjonskvote og kan påløpe kostnader; kjør røyktester serielt når du deler en ratebegrenset distribusjon. Å laste hjemmesiden kaller ikke modellen.

## Offline Tester

Fra sample-katalogen, kjør:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) fanger ekte OpenAI SDK-forespørsler med en loopback HTTP-fixture. Den sjekker begge forespørslers distribusjon, `reasoning_effort: none`, tokengrenser, bildeinnhold, inngangsvalidering, tomme svar og oppstrømsfeil.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) bruker MockMvc med en modelltjeneste-mock for å teste de gjengitte Thymeleaf-sidene, opplastingskontrakt, CSRF, validering, utdata-escaper og synlige feil. Disse testene trenger ikke Azure-legitimasjon og kaller aldri betalt Azure-inferens. Maven skriver Surefire-rapporter under `target/surefire-reports`.

## Hvordan Alt Henger Sammen

Her er hele flyten når du genererer en kjæledyrhistorie:

1. **Bildeseleksjon**: Du velger et kjæledyrbilde i opplastingsskjemaet
2. **Bildeopplasting**: "Analyze Image" sender en multipart POST til `/analyze-image` med CSRF-header
3. **Bildeanalyse**: `StoryService` sender bildet til GPT-5.6 Luna med reasoning satt til `none`
4. **Beskrivelsesvisning**: Nettleseren viser den returnerte beskrivelsen og lagrer den i skjemaet
5. **Historieinnsending**: "Generate Story" sender `description` og `_csrf` til `/generate-story`
6. **Historiegenerering**: Kontrolleren validerer beskrivelsen og kaller samme distribusjon med reasoning satt til `none`
7. **Malgjengivelse**: Thymeleaf escaper og viser beskrivelse og historie på resultatssiden

**Feilhåndteringsflyt:**
Hvis modellen feiler, logger serveren årsaken. Bildeanalyse returnerer HTTP 502 og nettleseren viser feilen uten å avsløre "Generate Story". Historiegenerering omdirigerer til skjemaet med en feilmelding. Ingen av veiene erstatter stille en forhåndsskrevet resultat.

## Forståelse av AI-integrasjon

### Azure AI Foundry (nøkkelfri)
Tjenesten konfigurerer SDK med ressursens `/openai/v1/`-endepunkt. `DefaultAzureCredential` og `AuthenticationUtil.getBearerTokenSupplier` leverer Microsoft Entra-tokener for `https://ai.azure.com/.default`. Lokal utvikling kan bruke din Azure CLI-innlogging; en Azure-hostet app kan bruke en administrert identitet med nødvendige ressursrettigheter.

### Prompt engineering
Bildeanalyse ber om observerbare kjæledyrsberegenskaper i et kort avsnitt og forteller modellen å behandle tekst i bildet som data, ikke instruksjoner. Historiegenerering bruker den returnerte beskrivelsen i en separat, familievennlig skriveforespørsel. Ingen av kallene aktiverer resonnering eller setter temperaturoverstyring.

### Responsbehandling
Den delte responsbehandleren forkaster manglende valg og tomt eller bare mellomrom-innhold, trimmer gyldig innhold, og bevarer oppstrømsfeil. Bildebeskrivelser er begrenset til 1000 tegn for å passe etterfølgende historiefelt. Opprinnelig modellfeil beholdes for diagnostikk, men vises ikke til brukeren.

## Neste Steg

For flere eksempler, se [Kapittel 04: Praktiske eksempler](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->