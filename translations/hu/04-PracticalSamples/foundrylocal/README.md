<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T12:04:14+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "hu"
}
-->
# Foundry Local Spring Boot Oktatóanyag

## Tartalomjegyzék

- [Előfeltételek](../../../../04-PracticalSamples/foundrylocal)
- [Projekt Áttekintés](../../../../04-PracticalSamples/foundrylocal)
- [A Kód Megértése](../../../../04-PracticalSamples/foundrylocal)
  - [1. Alkalmazás Konfiguráció (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Fő Alkalmazás Osztály (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. AI Szolgáltatási Réteg (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Projekt Függőségek (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Hogyan Működik Együtt Minden](../../../../04-PracticalSamples/foundrylocal)
- [Foundry Local Beállítása](../../../../04-PracticalSamples/foundrylocal)
- [Az Alkalmazás Futtatása](../../../../04-PracticalSamples/foundrylocal)
- [Várható Kimenet](../../../../04-PracticalSamples/foundrylocal)
- [Következő Lépések](../../../../04-PracticalSamples/foundrylocal)
- [Hibaelhárítás](../../../../04-PracticalSamples/foundrylocal)

## Előfeltételek

Mielőtt elkezdenéd ezt az oktatóanyagot, győződj meg róla, hogy rendelkezel:

- **Java 21 vagy újabb** telepítve a rendszereden
- **Maven 3.6+** a projekt felépítéséhez
- **Foundry Local** telepítve és futtatva

### **Foundry Local Telepítése:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Projekt Áttekintés

Ez a projekt négy fő komponensből áll:

1. **Application.java** - A fő Spring Boot alkalmazás belépési pontja
2. **FoundryLocalService.java** - Szolgáltatási réteg, amely az AI kommunikációt kezeli
3. **application.properties** - Konfiguráció a Foundry Local kapcsolat számára
4. **pom.xml** - Maven függőségek és projekt konfiguráció

## A Kód Megértése

### 1. Alkalmazás Konfiguráció (application.properties)

**Fájl:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Mit csinál ez:**
- **base-url**: Meghatározza, hol fut a Foundry Local (alapértelmezett port: 5273)
- **model**: Megnevezi az AI modellt, amelyet szöveg generálásra használunk

**Kulcsfogalom:** A Spring Boot automatikusan betölti ezeket a tulajdonságokat, és elérhetővé teszi az alkalmazás számára az `@Value` annotációval.

### 2. Fő Alkalmazás Osztály (Application.java)

**Fájl:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Mit csinál ez:**
- `@SpringBootApplication` engedélyezi a Spring Boot automatikus konfigurációját
- `WebApplicationType.NONE` jelzi a Spring számára, hogy ez egy parancssori alkalmazás, nem egy webszerver
- A fő metódus elindítja a Spring alkalmazást

**A Demo Futtató:**
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

**Mit csinál ez:**
- `@Bean` létrehoz egy komponenst, amelyet a Spring kezel
- `CommandLineRunner` kódot futtat, miután a Spring Boot elindult
- A `foundryLocalService` automatikusan injektálva van a Spring által (függőség injektálás)
- Küld egy tesztüzenetet az AI-nak, és megjeleníti a választ

### 3. AI Szolgáltatási Réteg (FoundryLocalService.java)

**Fájl:** `src/main/java/com/example/FoundryLocalService.java`

#### Konfiguráció Injektálása:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Mit csinál ez:**
- `@Service` jelzi a Spring számára, hogy ez az osztály üzleti logikát biztosít
- `@Value` injektálja a konfigurációs értékeket az application.properties fájlból
- A `:default-value` szintaxis tartalék értékeket biztosít, ha a tulajdonságok nincsenek beállítva

#### Ügyfél Inicializálása:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Mit csinál ez:**
- `@PostConstruct` futtatja ezt a metódust, miután a Spring létrehozta a szolgáltatást
- Létrehoz egy OpenAI ügyfelet, amely a helyi Foundry Local példányra mutat
- A `/v1` útvonal szükséges az OpenAI API kompatibilitáshoz
- Az API kulcs "unused", mivel a helyi fejlesztés nem igényel hitelesítést

#### Chat Metódus:
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

**Mit csinál ez:**
- **ChatCompletionCreateParams**: Konfigurálja az AI kérést
  - `model`: Meghatározza, melyik AI modellt használjuk
  - `addUserMessage`: Hozzáadja az üzenetedet a beszélgetéshez
  - `maxCompletionTokens`: Korlátozza a válasz hosszát (erőforrásokat takarít meg)
  - `temperature`: Szabályozza a véletlenszerűséget (0.0 = determinisztikus, 1.0 = kreatív)
- **API Hívás**: Elküldi a kérést a Foundry Localnak
- **Válasz Feldolgozása**: Biztonságosan kinyeri az AI szöveges válaszát
- **Hibakezelés**: Hasznos hibaüzenetekkel csomagolja az esetleges kivételeket

### 4. Projekt Függőségek (pom.xml)

**Kulcsfontosságú Függőségek:**

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

**Mit csinálnak ezek:**
- **spring-boot-starter**: Alapvető Spring Boot funkciókat biztosít
- **openai-java**: Hivatalos OpenAI Java SDK az API kommunikációhoz
- **jackson-databind**: JSON szerializációt/deszerializációt kezel az API hívásokhoz

## Hogyan Működik Együtt Minden

Íme a teljes folyamat, amikor futtatod az alkalmazást:

1. **Indítás**: A Spring Boot elindul, és beolvassa az `application.properties` fájlt
2. **Szolgáltatás Létrehozása**: A Spring létrehozza a `FoundryLocalService`-t, és injektálja a konfigurációs értékeket
3. **Ügyfél Beállítása**: A `@PostConstruct` inicializálja az OpenAI ügyfelet, hogy csatlakozzon a Foundry Localhoz
4. **Demo Végrehajtása**: A `CommandLineRunner` futtatja az indítás után
5. **AI Hívás**: A demo meghívja a `foundryLocalService.chat()` metódust egy tesztüzenettel
6. **API Kérés**: A szolgáltatás OpenAI-kompatibilis kérést épít és küld a Foundry Localnak
7. **Válasz Feldolgozása**: A szolgáltatás kinyeri és visszaadja az AI válaszát
8. **Megjelenítés**: Az alkalmazás kiírja a választ, majd kilép

## Foundry Local Beállítása

A Foundry Local beállításához kövesd az alábbi lépéseket:

1. **Foundry Local Telepítése** az [Előfeltételek](../../../../04-PracticalSamples/foundrylocal) szakaszban található utasítások szerint.
2. **Töltsd le az AI modellt**, például `phi-3.5-mini`, az alábbi parancs segítségével:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Konfiguráld az application.properties** fájlt, hogy illeszkedjen a Foundry Local beállításaidhoz, különösen, ha másik portot vagy modellt használsz.

## Az Alkalmazás Futtatása

### 1. Lépés: Indítsd el a Foundry Local-t
```bash
foundry model run phi-3.5-mini
```

### 2. Lépés: Építsd fel és futtasd az alkalmazást
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Várható Kimenet

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

## Következő Lépések

További példákért lásd: [4. fejezet: Gyakorlati minták](../README.md)

## Hibaelhárítás

### Gyakori Problémák

**"Kapcsolat megtagadva" vagy "Szolgáltatás nem elérhető"**
- Győződj meg róla, hogy a Foundry Local fut: `foundry model list`
- Ellenőrizd, hogy a szolgáltatás a 5273-as porton van: Nézd meg az `application.properties` fájlt
- Próbáld újraindítani a Foundry Local-t: `foundry model run phi-3.5-mini`

**"Modell nem található" hibák**
- Ellenőrizd az elérhető modelleket: `foundry model list`
- Frissítsd a modell nevét az `application.properties` fájlban, hogy pontosan egyezzen
- Töltsd le a modellt, ha szükséges: `foundry model run phi-3.5-mini`

**Maven fordítási hibák**
- Győződj meg róla, hogy Java 21 vagy újabb van telepítve: `java -version`
- Tisztítsd meg és építsd újra: `mvn clean compile`
- Ellenőrizd az internetkapcsolatot a függőségek letöltéséhez

**Az alkalmazás elindul, de nincs kimenet**
- Ellenőrizd, hogy a Foundry Local válaszol-e: Nyisd meg a böngészőt a `http://localhost:5273` címen
- Nézd meg az alkalmazás naplóit konkrét hibaüzenetekért
- Győződj meg róla, hogy a modell teljesen betöltődött és készen áll

**Felelősségkizárás**:  
Ez a dokumentum az [Co-op Translator](https://github.com/Azure/co-op-translator) AI fordítási szolgáltatás segítségével készült. Bár törekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelvén tekintendő hiteles forrásnak. Kritikus információk esetén javasolt professzionális, emberi fordítást igénybe venni. Nem vállalunk felelősséget a fordítás használatából eredő félreértésekért vagy téves értelmezésekért.