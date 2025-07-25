<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:11:53+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "ro"
}
-->
# Tutorial Foundry Local Spring Boot

## Cuprins

- [Cerințe preliminare](../../../../04-PracticalSamples/foundrylocal)
- [Prezentare generală a proiectului](../../../../04-PracticalSamples/foundrylocal)
- [Înțelegerea codului](../../../../04-PracticalSamples/foundrylocal)
  - [1. Configurarea aplicației (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Clasa principală a aplicației (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Stratul de servicii AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Dependențele proiectului (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Cum funcționează totul împreună](../../../../04-PracticalSamples/foundrylocal)
- [Configurarea Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Rularea aplicației](../../../../04-PracticalSamples/foundrylocal)
- [Rezultatul așteptat](../../../../04-PracticalSamples/foundrylocal)
- [Pași următori](../../../../04-PracticalSamples/foundrylocal)
- [Depanare](../../../../04-PracticalSamples/foundrylocal)

## Cerințe preliminare

Înainte de a începe acest tutorial, asigură-te că ai:

- **Java 21 sau o versiune mai recentă** instalată pe sistemul tău
- **Maven 3.6+** pentru a construi proiectul
- **Foundry Local** instalat și funcțional

### **Instalare Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Prezentare generală a proiectului

Acest proiect este format din patru componente principale:

1. **Application.java** - Punctul de intrare principal al aplicației Spring Boot
2. **FoundryLocalService.java** - Stratul de servicii care gestionează comunicarea AI
3. **application.properties** - Configurarea conexiunii pentru Foundry Local
4. **pom.xml** - Dependențele Maven și configurația proiectului

## Înțelegerea codului

### 1. Configurarea aplicației (application.properties)

**Fișier:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Ce face acest lucru:**
- **base-url**: Specifică unde rulează Foundry Local (portul implicit este 5273)
- **model**: Numește modelul AI utilizat pentru generarea textului

**Concept cheie:** Spring Boot încarcă automat aceste proprietăți și le face disponibile aplicației tale folosind adnotarea `@Value`.

### 2. Clasa principală a aplicației (Application.java)

**Fișier:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Ce face acest lucru:**
- `@SpringBootApplication` activează configurarea automată a Spring Boot
- `WebApplicationType.NONE` indică faptul că aceasta este o aplicație de linie de comandă, nu un server web
- Metoda principală pornește aplicația Spring

**Runner-ul Demo:**
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

**Ce face acest lucru:**
- `@Bean` creează o componentă gestionată de Spring
- `CommandLineRunner` rulează cod după ce Spring Boot pornește
- `foundryLocalService` este injectat automat de Spring (injecție de dependență)
- Trimite un mesaj de test către AI și afișează răspunsul

### 3. Stratul de servicii AI (FoundryLocalService.java)

**Fișier:** `src/main/java/com/example/FoundryLocalService.java`

#### Injecția configurației:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Ce face acest lucru:**
- `@Service` indică faptul că această clasă oferă logică de afaceri
- `@Value` injectează valorile de configurare din application.properties
- Sintaxa `:default-value` oferă valori implicite dacă proprietățile nu sunt setate

#### Inițializarea clientului:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Ce face acest lucru:**
- `@PostConstruct` rulează această metodă după ce Spring creează serviciul
- Creează un client OpenAI care indică instanța locală Foundry Local
- Calea `/v1` este necesară pentru compatibilitatea cu API-ul OpenAI
- Cheia API este "unused" deoarece dezvoltarea locală nu necesită autentificare

#### Metoda Chat:
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

**Ce face acest lucru:**
- **ChatCompletionCreateParams**: Configurează cererea către AI
  - `model`: Specifică modelul AI utilizat
  - `addUserMessage`: Adaugă mesajul tău la conversație
  - `maxCompletionTokens`: Limitează lungimea răspunsului (economisește resurse)
  - `temperature`: Controlează aleatorietatea (0.0 = determinist, 1.0 = creativ)
- **Apel API**: Trimite cererea către Foundry Local
- **Gestionarea răspunsului**: Extrage în siguranță răspunsul text al AI-ului
- **Gestionarea erorilor**: Împachetează excepțiile cu mesaje de eroare utile

### 4. Dependențele proiectului (pom.xml)

**Dependențe cheie:**

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

**Ce fac acestea:**
- **spring-boot-starter**: Oferă funcționalitatea de bază a Spring Boot
- **openai-java**: SDK-ul oficial OpenAI pentru comunicarea cu API-ul
- **jackson-databind**: Gestionează serializarea/deserializarea JSON pentru apelurile API

## Cum funcționează totul împreună

Iată fluxul complet atunci când rulezi aplicația:

1. **Pornire**: Spring Boot pornește și citește `application.properties`
2. **Crearea serviciului**: Spring creează `FoundryLocalService` și injectează valorile de configurare
3. **Configurarea clientului**: `@PostConstruct` inițializează clientul OpenAI pentru a se conecta la Foundry Local
4. **Executarea demo-ului**: `CommandLineRunner` se execută după pornire
5. **Apel AI**: Demo-ul apelează `foundryLocalService.chat()` cu un mesaj de test
6. **Cerere API**: Serviciul construiește și trimite o cerere compatibilă cu OpenAI către Foundry Local
7. **Procesarea răspunsului**: Serviciul extrage și returnează răspunsul AI-ului
8. **Afișare**: Aplicația afișează răspunsul și se închide

## Configurarea Foundry Local

Pentru a configura Foundry Local, urmează acești pași:

1. **Instalează Foundry Local** folosind instrucțiunile din secțiunea [Cerințe preliminare](../../../../04-PracticalSamples/foundrylocal).
2. **Descarcă modelul AI** pe care dorești să-l utilizezi, de exemplu, `phi-3.5-mini`, cu următoarea comandă:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Configurează fișierul application.properties** pentru a se potrivi cu setările Foundry Local, mai ales dacă folosești un port sau un model diferit.

## Rularea aplicației

### Pasul 1: Pornește Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Pasul 2: Construiește și rulează aplicația
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Rezultatul așteptat

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

## Pași următori

Pentru mai multe exemple, vezi [Capitolul 04: Exemple practice](../README.md)

## Depanare

### Probleme comune

**"Connection refused" sau "Service unavailable"**
- Asigură-te că Foundry Local rulează: `foundry model list`
- Verifică dacă serviciul este pe portul 5273: Consultă `application.properties`
- Încearcă să repornești Foundry Local: `foundry model run phi-3.5-mini`

**Erori "Model not found"**
- Verifică modelele disponibile: `foundry model list`
- Actualizează numele modelului în `application.properties` pentru a se potrivi exact
- Descarcă modelul dacă este necesar: `foundry model run phi-3.5-mini`

**Erori de compilare Maven**
- Asigură-te că ai Java 21 sau mai recent: `java -version`
- Curăță și reconstruiește: `mvn clean compile`
- Verifică conexiunea la internet pentru descărcarea dependențelor

**Aplicația pornește, dar nu afișează nimic**
- Verifică dacă Foundry Local răspunde: Deschide browserul la `http://localhost:5273`
- Consultă jurnalele aplicației pentru mesaje de eroare specifice
- Asigură-te că modelul este complet încărcat și gata

**Declinare de responsabilitate**:  
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). Deși ne străduim să asigurăm acuratețea, vă rugăm să fiți conștienți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa natală ar trebui considerat sursa autoritară. Pentru informații critice, se recomandă traducerea profesională realizată de un specialist uman. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care pot apărea din utilizarea acestei traduceri.