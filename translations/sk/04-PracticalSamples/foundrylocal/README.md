<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:09:25+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "sk"
}
-->
# Foundry Local Spring Boot Tutoriál

## Obsah

- [Predpoklady](../../../../04-PracticalSamples/foundrylocal)
- [Prehľad projektu](../../../../04-PracticalSamples/foundrylocal)
- [Porozumenie kódu](../../../../04-PracticalSamples/foundrylocal)
  - [1. Konfigurácia aplikácie (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Hlavná trieda aplikácie (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI Servisná vrstva (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Závislosti projektu (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Ako to všetko spolu funguje](../../../../04-PracticalSamples/foundrylocal)
- [Nastavenie Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Spustenie aplikácie](../../../../04-PracticalSamples/foundrylocal)
- [Očakávaný výstup](../../../../04-PracticalSamples/foundrylocal)
- [Ďalšie kroky](../../../../04-PracticalSamples/foundrylocal)
- [Riešenie problémov](../../../../04-PracticalSamples/foundrylocal)

## Predpoklady

Pred začiatkom tohto tutoriálu sa uistite, že máte:

- **Java 21 alebo vyššiu** nainštalovanú na vašom systéme
- **Maven 3.6+** na zostavenie projektu
- **Foundry Local** nainštalovaný a spustený

### **Inštalácia Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Prehľad projektu

Tento projekt pozostáva zo štyroch hlavných komponentov:

1. **Application.java** - Hlavný vstupný bod Spring Boot aplikácie
2. **FoundryLocalService.java** - Servisná vrstva, ktorá spracováva komunikáciu s AI
3. **application.properties** - Konfigurácia pre pripojenie k Foundry Local
4. **pom.xml** - Maven závislosti a konfigurácia projektu

## Porozumenie kódu

### 1. Konfigurácia aplikácie (application.properties)

**Súbor:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Čo to robí:**
- **base-url**: Určuje, kde beží Foundry Local (predvolený port 5273)
- **model**: Názov AI modelu, ktorý sa použije na generovanie textu

**Kľúčový koncept:** Spring Boot automaticky načíta tieto vlastnosti a sprístupní ich aplikácii pomocou anotácie `@Value`.

### 2. Hlavná trieda aplikácie (Application.java)

**Súbor:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Čo to robí:**
- `@SpringBootApplication` umožňuje automatickú konfiguráciu Spring Boot
- `WebApplicationType.NONE` informuje Spring, že ide o aplikáciu príkazového riadku, nie webový server
- Hlavná metóda spúšťa Spring aplikáciu

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

**Čo to robí:**
- `@Bean` vytvára komponent, ktorý spravuje Spring
- `CommandLineRunner` spúšťa kód po štarte Spring Boot
- `foundryLocalService` je automaticky injektovaný Springom (dependency injection)
- Posiela testovaciu správu AI a zobrazuje odpoveď

### 3. AI Servisná vrstva (FoundryLocalService.java)

**Súbor:** `src/main/java/com/example/FoundryLocalService.java`

#### Injekcia konfigurácie:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Čo to robí:**
- `@Service` informuje Spring, že táto trieda poskytuje obchodnú logiku
- `@Value` injektuje konfiguračné hodnoty z application.properties
- Syntax `:default-value` poskytuje predvolené hodnoty, ak vlastnosti nie sú nastavené

#### Inicializácia klienta:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Čo to robí:**
- `@PostConstruct` spúšťa túto metódu po vytvorení služby Springom
- Vytvára OpenAI klienta, ktorý smeruje na vašu lokálnu inštanciu Foundry Local
- Cesta `/v1` je potrebná pre kompatibilitu s OpenAI API
- API kľúč je "nepoužitý", pretože lokálny vývoj nevyžaduje autentifikáciu

#### Metóda Chat:
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

**Čo to robí:**
- **ChatCompletionCreateParams**: Konfiguruje AI požiadavku
  - `model`: Určuje, ktorý AI model sa použije
  - `addUserMessage`: Pridáva vašu správu do konverzácie
  - `maxCompletionTokens`: Obmedzuje dĺžku odpovede (šetrenie zdrojov)
  - `temperature`: Riadi náhodnosť (0.0 = deterministické, 1.0 = kreatívne)
- **API volanie**: Posiela požiadavku na Foundry Local
- **Spracovanie odpovede**: Bezpečne extrahuje textovú odpoveď AI
- **Riešenie chýb**: Zabalí výnimky do užitočných chybových správ

### 4. Závislosti projektu (pom.xml)

**Kľúčové závislosti:**

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

**Čo robia:**
- **spring-boot-starter**: Poskytuje základnú funkcionalitu Spring Boot
- **openai-java**: Oficiálne OpenAI Java SDK pre komunikáciu s API
- **jackson-databind**: Spracováva JSON serializáciu/deserializáciu pre API volania

## Ako to všetko spolu funguje

Tu je kompletný tok, keď spustíte aplikáciu:

1. **Štart**: Spring Boot sa spustí a načíta `application.properties`
2. **Vytvorenie služby**: Spring vytvorí `FoundryLocalService` a injektuje konfiguračné hodnoty
3. **Nastavenie klienta**: `@PostConstruct` inicializuje OpenAI klienta na pripojenie k Foundry Local
4. **Spustenie dema**: `CommandLineRunner` sa vykoná po štarte
5. **AI volanie**: Demo volá `foundryLocalService.chat()` s testovacou správou
6. **API požiadavka**: Služba zostaví a pošle požiadavku kompatibilnú s OpenAI na Foundry Local
7. **Spracovanie odpovede**: Služba extrahuje a vráti odpoveď AI
8. **Zobrazenie**: Aplikácia vytlačí odpoveď a ukončí sa

## Nastavenie Foundry Local

Na nastavenie Foundry Local postupujte podľa týchto krokov:

1. **Nainštalujte Foundry Local** podľa pokynov v sekcii [Predpoklady](../../../../04-PracticalSamples/foundrylocal).
2. **Stiahnite AI model**, ktorý chcete použiť, napríklad `phi-3.5-mini`, pomocou nasledujúceho príkazu:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfigurujte súbor application.properties**, aby zodpovedal nastaveniam vášho Foundry Local, najmä ak používate iný port alebo model.

## Spustenie aplikácie

### Krok 1: Spustite Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Krok 2: Zostavte a spustite aplikáciu
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Očakávaný výstup

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

## Ďalšie kroky

Pre viac príkladov si pozrite [Kapitolu 04: Praktické ukážky](../README.md)

## Riešenie problémov

### Bežné problémy

**"Connection refused" alebo "Service unavailable"**
- Uistite sa, že Foundry Local beží: `foundry model list`
- Overte, že služba je na porte 5273: Skontrolujte `application.properties`
- Skúste reštartovať Foundry Local: `foundry model run phi-3.5-mini`

**Chyby "Model not found"**
- Skontrolujte dostupné modely: `foundry model list`
- Aktualizujte názov modelu v `application.properties`, aby presne sedel
- Stiahnite model, ak je to potrebné: `foundry model run phi-3.5-mini`

**Chyby pri kompilácii Maven**
- Uistite sa, že máte Java 21 alebo vyššiu: `java -version`
- Vyčistite a znovu zostavte: `mvn clean compile`
- Skontrolujte internetové pripojenie pre stiahnutie závislostí

**Aplikácia sa spustí, ale neprodukuje výstup**
- Overte, že Foundry Local odpovedá: Otvorte prehliadač na `http://localhost:5273`
- Skontrolujte logy aplikácie pre konkrétne chybové správy
- Uistite sa, že model je plne načítaný a pripravený

**Upozornenie**:  
Tento dokument bol preložený pomocou služby na automatický preklad [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, upozorňujeme, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho pôvodnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nenesieme zodpovednosť za akékoľvek nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.