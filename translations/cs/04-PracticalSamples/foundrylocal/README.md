<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:06:58+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "cs"
}
-->
# Foundry Local Spring Boot Tutorial

## Obsah

- [Předpoklady](../../../../04-PracticalSamples/foundrylocal)
- [Přehled projektu](../../../../04-PracticalSamples/foundrylocal)
- [Porozumění kódu](../../../../04-PracticalSamples/foundrylocal)
  - [1. Konfigurace aplikace (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Hlavní třída aplikace (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Vrstva AI služby (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Závislosti projektu (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Jak vše funguje dohromady](../../../../04-PracticalSamples/foundrylocal)
- [Nastavení Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Spuštění aplikace](../../../../04-PracticalSamples/foundrylocal)
- [Očekávaný výstup](../../../../04-PracticalSamples/foundrylocal)
- [Další kroky](../../../../04-PracticalSamples/foundrylocal)
- [Řešení problémů](../../../../04-PracticalSamples/foundrylocal)

## Předpoklady

Než začnete s tímto tutoriálem, ujistěte se, že máte:

- **Java 21 nebo vyšší** nainstalovanou na vašem systému
- **Maven 3.6+** pro sestavení projektu
- **Foundry Local** nainstalovaný a spuštěný

### **Instalace Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Přehled projektu

Tento projekt se skládá ze čtyř hlavních komponent:

1. **Application.java** - Hlavní vstupní bod aplikace Spring Boot
2. **FoundryLocalService.java** - Vrstva služby, která zajišťuje komunikaci s AI
3. **application.properties** - Konfigurace pro připojení k Foundry Local
4. **pom.xml** - Závislosti Maven a konfigurace projektu

## Porozumění kódu

### 1. Konfigurace aplikace (application.properties)

**Soubor:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Co to dělá:**
- **base-url**: Určuje, kde běží Foundry Local (výchozí port 5273)
- **model**: Název AI modelu, který se použije pro generování textu

**Klíčový koncept:** Spring Boot automaticky načte tyto vlastnosti a zpřístupní je vaší aplikaci pomocí anotace `@Value`.

### 2. Hlavní třída aplikace (Application.java)

**Soubor:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Co to dělá:**
- `@SpringBootApplication` povoluje automatickou konfiguraci Spring Boot
- `WebApplicationType.NONE` říká Springu, že se jedná o aplikaci příkazového řádku, nikoli webový server
- Hlavní metoda spouští aplikaci Spring

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

**Co to dělá:**
- `@Bean` vytváří komponentu, kterou Spring spravuje
- `CommandLineRunner` spouští kód po spuštění Spring Boot
- `foundryLocalService` je automaticky injektována Springem (dependency injection)
- Posílá testovací zprávu AI a zobrazuje odpověď

### 3. Vrstva AI služby (FoundryLocalService.java)

**Soubor:** `src/main/java/com/example/FoundryLocalService.java`

#### Injektování konfigurace:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Co to dělá:**
- `@Service` říká Springu, že tato třída poskytuje obchodní logiku
- `@Value` injektuje hodnoty konfigurace z application.properties
- Syntaxe `:default-value` poskytuje výchozí hodnoty, pokud vlastnosti nejsou nastaveny

#### Inicializace klienta:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Co to dělá:**
- `@PostConstruct` spouští tuto metodu po vytvoření služby Springem
- Vytváří OpenAI klienta, který se připojuje k vaší lokální instanci Foundry Local
- Cesta `/v1` je vyžadována pro kompatibilitu s OpenAI API
- API klíč je "nepoužitý", protože lokální vývoj nevyžaduje autentizaci

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

**Co to dělá:**
- **ChatCompletionCreateParams**: Konfiguruje požadavek na AI
  - `model`: Určuje, který AI model se použije
  - `addUserMessage`: Přidává vaši zprávu do konverzace
  - `maxCompletionTokens`: Omezuje délku odpovědi (šetří zdroje)
  - `temperature`: Řídí náhodnost (0.0 = deterministické, 1.0 = kreativní)
- **API volání**: Posílá požadavek na Foundry Local
- **Zpracování odpovědi**: Bezpečně extrahuje textovou odpověď AI
- **Zpracování chyb**: Obaluje výjimky s užitečnými chybovými zprávami

### 4. Závislosti projektu (pom.xml)

**Klíčové závislosti:**

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

**Co dělají:**
- **spring-boot-starter**: Poskytuje základní funkce Spring Boot
- **openai-java**: Oficiální OpenAI Java SDK pro komunikaci s API
- **jackson-databind**: Zajišťuje serializaci/deserializaci JSON pro API volání

## Jak vše funguje dohromady

Zde je kompletní tok, když spustíte aplikaci:

1. **Spuštění**: Spring Boot se spustí a načte `application.properties`
2. **Vytvoření služby**: Spring vytvoří `FoundryLocalService` a injektuje konfigurační hodnoty
3. **Nastavení klienta**: `@PostConstruct` inicializuje OpenAI klienta pro připojení k Foundry Local
4. **Spuštění dema**: `CommandLineRunner` se spustí po startu
5. **Volání AI**: Demo volá `foundryLocalService.chat()` s testovací zprávou
6. **API požadavek**: Služba sestaví a pošle požadavek kompatibilní s OpenAI na Foundry Local
7. **Zpracování odpovědi**: Služba extrahuje a vrátí odpověď AI
8. **Zobrazení**: Aplikace vytiskne odpověď a ukončí se

## Nastavení Foundry Local

Pro nastavení Foundry Local postupujte podle těchto kroků:

1. **Nainstalujte Foundry Local** podle pokynů v sekci [Předpoklady](../../../../04-PracticalSamples/foundrylocal).
2. **Stáhněte AI model**, který chcete použít, například `phi-3.5-mini`, pomocí následujícího příkazu:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfigurujte soubor application.properties**, aby odpovídal vašemu nastavení Foundry Local, zejména pokud používáte jiný port nebo model.

## Spuštění aplikace

### Krok 1: Spusťte Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Krok 2: Sestavte a spusťte aplikaci
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Očekávaný výstup

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

## Další kroky

Pro více příkladů si přečtěte [Kapitolu 04: Praktické ukázky](../README.md)

## Řešení problémů

### Běžné problémy

**"Connection refused" nebo "Service unavailable"**
- Ujistěte se, že Foundry Local běží: `foundry model list`
- Ověřte, že služba je na portu 5273: Zkontrolujte `application.properties`
- Zkuste restartovat Foundry Local: `foundry model run phi-3.5-mini`

**"Model not found" chyby**
- Zkontrolujte dostupné modely: `foundry model list`
- Aktualizujte název modelu v `application.properties`, aby přesně odpovídal
- Stáhněte model, pokud je potřeba: `foundry model run phi-3.5-mini`

**Chyby při kompilaci Maven**
- Ujistěte se, že máte Java 21 nebo vyšší: `java -version`
- Vyčistěte a znovu sestavte: `mvn clean compile`
- Zkontrolujte internetové připojení pro stažení závislostí

**Aplikace se spustí, ale žádný výstup**
- Ověřte, že Foundry Local odpovídá: Otevřete prohlížeč na `http://localhost:5273`
- Zkontrolujte logy aplikace pro konkrétní chybové zprávy
- Ujistěte se, že model je plně načtený a připravený

**Prohlášení:**  
Tento dokument byl přeložen pomocí služby pro automatický překlad [Co-op Translator](https://github.com/Azure/co-op-translator). Ačkoli se snažíme o přesnost, mějte prosím na paměti, že automatické překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace se doporučuje profesionální lidský překlad. Nenese odpovědnost za žádné nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.