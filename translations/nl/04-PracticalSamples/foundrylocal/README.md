<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:47:41+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "nl"
}
-->
# Foundry Local Spring Boot Tutorial

## Inhoudsopgave

- [Vereisten](../../../../04-PracticalSamples/foundrylocal)
- [Projectoverzicht](../../../../04-PracticalSamples/foundrylocal)
- [De code begrijpen](../../../../04-PracticalSamples/foundrylocal)
  - [1. Applicatieconfiguratie (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Hoofdapplicatieklasse (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI-servicelaag (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Projectafhankelijkheden (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Hoe alles samenwerkt](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local instellen](../../../../04-PracticalSamples/foundrylocal)
- [De applicatie uitvoeren](../../../../04-PracticalSamples/foundrylocal)
- [Verwachte output](../../../../04-PracticalSamples/foundrylocal)
- [Volgende stappen](../../../../04-PracticalSamples/foundrylocal)
- [Problemen oplossen](../../../../04-PracticalSamples/foundrylocal)

## Vereisten

Voordat je met deze tutorial begint, zorg ervoor dat je het volgende hebt:

- **Java 21 of hoger** geïnstalleerd op je systeem
- **Maven 3.6+** om het project te bouwen
- **Foundry Local** geïnstalleerd en actief

### **Foundry Local installeren:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Projectoverzicht

Dit project bestaat uit vier hoofdcomponenten:

1. **Application.java** - Het startpunt van de Spring Boot-applicatie
2. **FoundryLocalService.java** - Servicelaag die AI-communicatie afhandelt
3. **application.properties** - Configuratie voor de verbinding met Foundry Local
4. **pom.xml** - Maven-afhankelijkheden en projectconfiguratie

## De code begrijpen

### 1. Applicatieconfiguratie (application.properties)

**Bestand:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Wat dit doet:**
- **base-url**: Geeft aan waar Foundry Local actief is (standaardpoort 5273)
- **model**: Geeft de naam van het AI-model dat wordt gebruikt voor tekstgeneratie

**Belangrijk concept:** Spring Boot laadt deze eigenschappen automatisch en maakt ze beschikbaar voor je applicatie via de `@Value`-annotatie.

### 2. Hoofdapplicatieklasse (Application.java)

**Bestand:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Wat dit doet:**
- `@SpringBootApplication` schakelt Spring Boot-autoconfiguratie in
- `WebApplicationType.NONE` geeft aan dat dit een command-line applicatie is, geen webserver
- De main-methode start de Spring-applicatie

**De Demo Runner:**
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

**Wat dit doet:**
- `@Bean` maakt een component dat door Spring wordt beheerd
- `CommandLineRunner` voert code uit nadat Spring Boot is gestart
- `foundryLocalService` wordt automatisch geïnjecteerd door Spring (dependency injection)
- Stuurt een testbericht naar de AI en toont de reactie

### 3. AI-servicelaag (FoundryLocalService.java)

**Bestand:** `src/main/java/com/example/FoundryLocalService.java`

#### Configuratie-injectie:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Wat dit doet:**
- `@Service` geeft aan dat deze klasse bedrijfslogica biedt
- `@Value` injecteert configuratiewaarden uit application.properties
- De `:default-value`-syntaxis biedt standaardwaarden als eigenschappen niet zijn ingesteld

#### Client-initialisatie:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Wat dit doet:**
- `@PostConstruct` voert deze methode uit nadat Spring de service heeft gemaakt
- Maakt een OpenAI-client die verwijst naar je lokale Foundry Local-instantie
- Het `/v1`-pad is vereist voor OpenAI API-compatibiliteit
- API-sleutel is "unused" omdat lokale ontwikkeling geen authenticatie vereist

#### Chat-methode:
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

**Wat dit doet:**
- **ChatCompletionCreateParams**: Configureert het AI-verzoek
  - `model`: Geeft aan welk AI-model wordt gebruikt
  - `addUserMessage`: Voegt je bericht toe aan het gesprek
  - `maxCompletionTokens`: Beperkt de lengte van de reactie (bespaart middelen)
  - `temperature`: Bepaalt willekeurigheid (0.0 = deterministisch, 1.0 = creatief)
- **API-aanroep**: Stuurt het verzoek naar Foundry Local
- **Reactieverwerking**: Haalt de tekstreactie van de AI veilig op
- **Foutafhandeling**: Omvat uitzonderingen met nuttige foutmeldingen

### 4. Projectafhankelijkheden (pom.xml)

**Belangrijke afhankelijkheden:**

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

**Wat deze doen:**
- **spring-boot-starter**: Biedt kernfunctionaliteit van Spring Boot
- **openai-java**: Officiële OpenAI Java SDK voor API-communicatie
- **jackson-databind**: Verzorgt JSON-serialisatie/deserialisatie voor API-aanroepen

## Hoe alles samenwerkt

Hier is de volledige flow wanneer je de applicatie uitvoert:

1. **Opstarten**: Spring Boot start en leest `application.properties`
2. **Servicecreatie**: Spring maakt `FoundryLocalService` en injecteert configuratiewaarden
3. **Clientinstelling**: `@PostConstruct` initialiseert de OpenAI-client om verbinding te maken met Foundry Local
4. **Demo-uitvoering**: `CommandLineRunner` wordt uitgevoerd na opstarten
5. **AI-aanroep**: De demo roept `foundryLocalService.chat()` aan met een testbericht
6. **API-verzoek**: Service bouwt en stuurt een OpenAI-compatibel verzoek naar Foundry Local
7. **Reactieverwerking**: Service haalt de reactie van de AI op en retourneert deze
8. **Weergave**: Applicatie toont de reactie en sluit af

## Foundry Local instellen

Volg deze stappen om Foundry Local in te stellen:

1. **Installeer Foundry Local** volgens de instructies in de sectie [Vereisten](../../../../04-PracticalSamples/foundrylocal).
2. **Download het AI-model** dat je wilt gebruiken, bijvoorbeeld `phi-3.5-mini`, met het volgende commando:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configureer het application.properties-bestand** om overeen te komen met je Foundry Local-instellingen, vooral als je een andere poort of model gebruikt.

## De applicatie uitvoeren

### Stap 1: Start Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Stap 2: Bouw en voer de applicatie uit
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Verwachte output

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

## Volgende stappen

Voor meer voorbeelden, zie [Hoofdstuk 04: Praktische voorbeelden](../README.md)

## Problemen oplossen

### Veelvoorkomende problemen

**"Connection refused" of "Service unavailable"**
- Zorg ervoor dat Foundry Local actief is: `foundry model list`
- Controleer of de service op poort 5273 draait: Controleer `application.properties`
- Probeer Foundry Local opnieuw te starten: `foundry model run phi-3.5-mini`

**"Model not found"-fouten**
- Controleer beschikbare modellen: `foundry model list`
- Werk de modelnaam in `application.properties` bij zodat deze exact overeenkomt
- Download het model indien nodig: `foundry model run phi-3.5-mini`

**Maven-compilatiefouten**
- Zorg ervoor dat Java 21 of hoger is geïnstalleerd: `java -version`
- Maak schoon en bouw opnieuw: `mvn clean compile`
- Controleer de internetverbinding voor het downloaden van afhankelijkheden

**Applicatie start maar geen output**
- Controleer of Foundry Local reageert: Open een browser naar `http://localhost:5273`
- Controleer applicatielogs op specifieke foutmeldingen
- Zorg ervoor dat het model volledig is geladen en klaar is

**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.