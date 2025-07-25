<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:40:44+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "da"
}
-->
# Foundry Local Spring Boot Vejledning

## Indholdsfortegnelse

- [Forudsætninger](../../../../04-PracticalSamples/foundrylocal)
- [Projektoversigt](../../../../04-PracticalSamples/foundrylocal)
- [Forstå Koden](../../../../04-PracticalSamples/foundrylocal)
  - [1. Applikationskonfiguration (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Hovedapplikationsklasse (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI Servicelag (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Projektafhængigheder (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Hvordan Det Hele Hænger Sammen](../../../../04-PracticalSamples/foundrylocal)
- [Opsætning af Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Kørsel af Applikationen](../../../../04-PracticalSamples/foundrylocal)
- [Forventet Output](../../../../04-PracticalSamples/foundrylocal)
- [Næste Skridt](../../../../04-PracticalSamples/foundrylocal)
- [Fejlfinding](../../../../04-PracticalSamples/foundrylocal)

## Forudsætninger

Før du starter denne vejledning, skal du sikre dig, at du har:

- **Java 21 eller nyere** installeret på dit system
- **Maven 3.6+** til at bygge projektet
- **Foundry Local** installeret og kørende

### **Installer Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Projektoversigt

Dette projekt består af fire hovedkomponenter:

1. **Application.java** - Hovedindgangspunktet for Spring Boot-applikationen
2. **FoundryLocalService.java** - Servicelag, der håndterer AI-kommunikation
3. **application.properties** - Konfiguration til Foundry Local-forbindelsen
4. **pom.xml** - Maven-afhængigheder og projektkonfiguration

## Forstå Koden

### 1. Applikationskonfiguration (application.properties)

**Fil:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Hvad dette gør:**
- **base-url**: Angiver, hvor Foundry Local kører (standardport 5273)
- **model**: Navngiver den AI-model, der skal bruges til tekstgenerering

**Nøglekoncept:** Spring Boot indlæser automatisk disse egenskaber og gør dem tilgængelige for din applikation ved hjælp af `@Value`-annotationen.

### 2. Hovedapplikationsklasse (Application.java)

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

**Hvad dette gør:**
- `@SpringBootApplication` aktiverer Spring Boot auto-konfiguration
- `WebApplicationType.NONE` fortæller Spring, at dette er en kommandolinjeapplikation, ikke en webserver
- Main-metoden starter Spring-applikationen

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

**Hvad dette gør:**
- `@Bean` opretter en komponent, som Spring administrerer
- `CommandLineRunner` kører kode efter, at Spring Boot er startet
- `foundryLocalService` injiceres automatisk af Spring (afhængighedsinjektion)
- Sender en testbesked til AI'en og viser svaret

### 3. AI Servicelag (FoundryLocalService.java)

**Fil:** `src/main/java/com/example/FoundryLocalService.java`

#### Konfigurationsinjektion:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Hvad dette gør:**
- `@Service` fortæller Spring, at denne klasse leverer forretningslogik
- `@Value` injicerer konfigurationsværdier fra application.properties
- `:default-value`-syntaksen giver standardværdier, hvis egenskaber ikke er sat

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

**Hvad dette gør:**
- `@PostConstruct` kører denne metode, efter at Spring har oprettet servicen
- Opretter en OpenAI-klient, der peger på din lokale Foundry Local-instans
- `/v1`-stien er nødvendig for OpenAI API-kompatibilitet
- API-nøglen er "unused", da lokal udvikling ikke kræver autentifikation

#### Chatmetode:
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

**Hvad dette gør:**
- **ChatCompletionCreateParams**: Konfigurerer AI-forespørgslen
  - `model`: Angiver, hvilken AI-model der skal bruges
  - `addUserMessage`: Tilføjer din besked til samtalen
  - `maxCompletionTokens`: Begrænser længden af svaret (sparer ressourcer)
  - `temperature`: Styrer tilfældighed (0.0 = deterministisk, 1.0 = kreativ)
- **API-kald**: Sender forespørgslen til Foundry Local
- **Svarhåndtering**: Ekstraherer AI'ens tekstsvar sikkert
- **Fejlhåndtering**: Indpakker undtagelser med nyttige fejlmeddelelser

### 4. Projektafhængigheder (pom.xml)

**Vigtige afhængigheder:**

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

**Hvad disse gør:**
- **spring-boot-starter**: Giver kernefunktionalitet til Spring Boot
- **openai-java**: Officiel OpenAI Java SDK til API-kommunikation
- **jackson-databind**: Håndterer JSON-serialisering/deserialisering til API-kald

## Hvordan Det Hele Hænger Sammen

Her er det komplette flow, når du kører applikationen:

1. **Opstart**: Spring Boot starter og læser `application.properties`
2. **Serviceoprettelse**: Spring opretter `FoundryLocalService` og injicerer konfigurationsværdier
3. **Klientopsætning**: `@PostConstruct` initialiserer OpenAI-klienten til at forbinde til Foundry Local
4. **Demoeksekvering**: `CommandLineRunner` kører efter opstart
5. **AI-kald**: Demoen kalder `foundryLocalService.chat()` med en testbesked
6. **API-forespørgsel**: Servicen bygger og sender en OpenAI-kompatibel forespørgsel til Foundry Local
7. **Svarbehandling**: Servicen ekstraherer og returnerer AI'ens svar
8. **Visning**: Applikationen udskriver svaret og afslutter

## Opsætning af Foundry Local

For at opsætte Foundry Local skal du følge disse trin:

1. **Installer Foundry Local** ved at følge instruktionerne i [Forudsætninger](../../../../04-PracticalSamples/foundrylocal)-sektionen.
2. **Download AI-modellen**, du vil bruge, for eksempel `phi-3.5-mini`, med følgende kommando:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfigurer application.properties**-filen, så den matcher dine Foundry Local-indstillinger, især hvis du bruger en anden port eller model.

## Kørsel af Applikationen

### Trin 1: Start Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Trin 2: Byg og kør applikationen
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Forventet Output

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

## Næste Skridt

For flere eksempler, se [Kapitel 04: Praktiske eksempler](../README.md)

## Fejlfinding

### Almindelige Problemer

**"Connection refused" eller "Service unavailable"**
- Sørg for, at Foundry Local kører: `foundry model list`
- Bekræft, at servicen er på port 5273: Tjek `application.properties`
- Prøv at genstarte Foundry Local: `foundry model run phi-3.5-mini`

**"Model not found"-fejl**
- Tjek tilgængelige modeller: `foundry model list`
- Opdater modelnavnet i `application.properties`, så det matcher præcist
- Download modellen, hvis nødvendigt: `foundry model run phi-3.5-mini`

**Maven-kompilationsfejl**
- Sørg for, at Java 21 eller nyere er installeret: `java -version`
- Rens og genopbyg: `mvn clean compile`
- Tjek internetforbindelsen for afhængighedsdownloads

**Applikationen starter, men der er ingen output**
- Bekræft, at Foundry Local svarer: Åbn browseren på `http://localhost:5273`
- Tjek applikationslogfiler for specifikke fejlmeddelelser
- Sørg for, at modellen er fuldt indlæst og klar

**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal det bemærkes, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os ikke ansvar for eventuelle misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.