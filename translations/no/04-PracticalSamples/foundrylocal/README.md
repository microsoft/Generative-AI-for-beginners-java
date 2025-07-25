<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:43:01+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "no"
}
-->
# Foundry Local Spring Boot Veiledning

## Innholdsfortegnelse

- [Forutsetninger](../../../../04-PracticalSamples/foundrylocal)
- [Prosjektoversikt](../../../../04-PracticalSamples/foundrylocal)
- [Forstå koden](../../../../04-PracticalSamples/foundrylocal)
  - [1. Applikasjonskonfigurasjon (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Hovedapplikasjonsklasse (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI-tjenestelag (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Prosjektavhengigheter (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Hvordan alt fungerer sammen](../../../../04-PracticalSamples/foundrylocal)
- [Sette opp Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Kjøre applikasjonen](../../../../04-PracticalSamples/foundrylocal)
- [Forventet output](../../../../04-PracticalSamples/foundrylocal)
- [Neste steg](../../../../04-PracticalSamples/foundrylocal)
- [Feilsøking](../../../../04-PracticalSamples/foundrylocal)

## Forutsetninger

Før du starter denne veiledningen, sørg for at du har:

- **Java 21 eller nyere** installert på systemet ditt
- **Maven 3.6+** for å bygge prosjektet
- **Foundry Local** installert og kjørende

### **Installer Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Prosjektoversikt

Dette prosjektet består av fire hovedkomponenter:

1. **Application.java** - Hovedinngangspunktet for Spring Boot-applikasjonen
2. **FoundryLocalService.java** - Tjenestelag som håndterer AI-kommunikasjon
3. **application.properties** - Konfigurasjon for Foundry Local-tilkobling
4. **pom.xml** - Maven-avhengigheter og prosjektkonfigurasjon

## Forstå koden

### 1. Applikasjonskonfigurasjon (application.properties)

**Fil:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Hva dette gjør:**
- **base-url**: Angir hvor Foundry Local kjører (standard port 5273)
- **model**: Navngir AI-modellen som skal brukes for tekstgenerering

**Viktig konsept:** Spring Boot laster automatisk inn disse egenskapene og gjør dem tilgjengelige for applikasjonen din ved hjelp av `@Value`-annotasjonen.

### 2. Hovedapplikasjonsklasse (Application.java)

**Fil:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Hva dette gjør:**
- `@SpringBootApplication` aktiverer Spring Boot auto-konfigurasjon
- `WebApplicationType.NONE` forteller Spring at dette er en kommandolinjeapplikasjon, ikke en webserver
- Hovedmetoden starter Spring-applikasjonen

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

**Hva dette gjør:**
- `@Bean` oppretter en komponent som Spring administrerer
- `CommandLineRunner` kjører kode etter at Spring Boot har startet opp
- `foundryLocalService` injiseres automatisk av Spring (avhengighetsinjeksjon)
- Sender en testmelding til AI og viser responsen

### 3. AI-tjenestelag (FoundryLocalService.java)

**Fil:** `src/main/java/com/example/FoundryLocalService.java`

#### Konfigurasjonsinjeksjon:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Hva dette gjør:**
- `@Service` forteller Spring at denne klassen gir forretningslogikk
- `@Value` injiserer konfigurasjonsverdier fra application.properties
- `:default-value`-syntaksen gir fallback-verdier hvis egenskaper ikke er satt

#### Klientinitialisering:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Hva dette gjør:**
- `@PostConstruct` kjører denne metoden etter at Spring har opprettet tjenesten
- Oppretter en OpenAI-klient som peker til din lokale Foundry Local-instans
- `/v1`-stien er nødvendig for OpenAI API-kompatibilitet
- API-nøkkelen er "unused" fordi lokal utvikling ikke krever autentisering

#### Chat-metode:
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

**Hva dette gjør:**
- **ChatCompletionCreateParams**: Konfigurerer AI-forespørselen
  - `model`: Angir hvilken AI-modell som skal brukes
  - `addUserMessage`: Legger til meldingen din i samtalen
  - `maxCompletionTokens`: Begrenser hvor lang responsen kan være (spar ressurser)
  - `temperature`: Kontrollerer tilfeldighet (0.0 = deterministisk, 1.0 = kreativ)
- **API-kall**: Sender forespørselen til Foundry Local
- **Responshåndtering**: Henter AI-ens tekstrespons på en sikker måte
- **Feilhåndtering**: Pakker inn unntak med nyttige feilmeldinger

### 4. Prosjektavhengigheter (pom.xml)

**Viktige avhengigheter:**

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

**Hva disse gjør:**
- **spring-boot-starter**: Gir kjernefunksjonalitet for Spring Boot
- **openai-java**: Offisiell OpenAI Java SDK for API-kommunikasjon
- **jackson-databind**: Håndterer JSON-serialisering/deserialisering for API-kall

## Hvordan alt fungerer sammen

Her er den komplette flyten når du kjører applikasjonen:

1. **Oppstart**: Spring Boot starter og leser `application.properties`
2. **Tjenesteopprettelse**: Spring oppretter `FoundryLocalService` og injiserer konfigurasjonsverdier
3. **Klientoppsett**: `@PostConstruct` initialiserer OpenAI-klienten for å koble til Foundry Local
4. **Demoeksekvering**: `CommandLineRunner` kjører etter oppstart
5. **AI-kall**: Demoen kaller `foundryLocalService.chat()` med en testmelding
6. **API-forespørsel**: Tjenesten bygger og sender en OpenAI-kompatibel forespørsel til Foundry Local
7. **Responsbehandling**: Tjenesten henter og returnerer AI-ens respons
8. **Visning**: Applikasjonen skriver ut responsen og avslutter

## Sette opp Foundry Local

For å sette opp Foundry Local, følg disse trinnene:

1. **Installer Foundry Local** ved å bruke instruksjonene i [Forutsetninger](../../../../04-PracticalSamples/foundrylocal)-seksjonen.
2. **Last ned AI-modellen** du vil bruke, for eksempel `phi-3.5-mini`, med følgende kommando:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfigurer application.properties**-filen for å matche dine Foundry Local-innstillinger, spesielt hvis du bruker en annen port eller modell.

## Kjøre applikasjonen

### Steg 1: Start Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Steg 2: Bygg og kjør applikasjonen
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Forventet output

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

## Neste steg

For flere eksempler, se [Kapittel 04: Praktiske eksempler](../README.md)

## Feilsøking

### Vanlige problemer

**"Connection refused" eller "Service unavailable"**
- Sørg for at Foundry Local kjører: `foundry model list`
- Bekreft at tjenesten er på port 5273: Sjekk `application.properties`
- Prøv å starte Foundry Local på nytt: `foundry model run phi-3.5-mini`

**"Model not found"-feil**
- Sjekk tilgjengelige modeller: `foundry model list`
- Oppdater modellnavnet i `application.properties` til å matche nøyaktig
- Last ned modellen hvis nødvendig: `foundry model run phi-3.5-mini`

**Maven-kompileringsfeil**
- Sørg for Java 21 eller nyere: `java -version`
- Rens og bygg på nytt: `mvn clean compile`
- Sjekk internettforbindelsen for å laste ned avhengigheter

**Applikasjonen starter, men ingen output**
- Bekreft at Foundry Local svarer: Åpne nettleseren til `http://localhost:5273`
- Sjekk applikasjonslogger for spesifikke feilmeldinger
- Sørg for at modellen er fullstendig lastet og klar

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiserte oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.