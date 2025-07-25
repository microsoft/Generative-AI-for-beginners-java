<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "2284c54d2a98090a37df0dbef1633ebf",
  "translation_date": "2025-07-25T11:27:18+00:00",
  "source_file": "04-PracticalSamples/foundrylocal/README.md",
  "language_code": "pl"
}
-->
# Samouczek Foundry Local z Spring Boot

## Spis Treści

- [Wymagania wstępne](../../../../04-PracticalSamples/foundrylocal)
- [Przegląd projektu](../../../../04-PracticalSamples/foundrylocal)
- [Zrozumienie kodu](../../../../04-PracticalSamples/foundrylocal)
  - [1. Konfiguracja aplikacji (application.properties)](../../../../04-PracticalSamples/foundrylocal)
  - [2. Główna klasa aplikacji (Application.java)](../../../../04-PracticalSamples/foundrylocal)
  - [3. Warstwa serwisowa AI (FoundryLocalService.java)](../../../../04-PracticalSamples/foundrylocal)
  - [4. Zależności projektu (pom.xml)](../../../../04-PracticalSamples/foundrylocal)
- [Jak to wszystko działa razem](../../../../04-PracticalSamples/foundrylocal)
- [Konfigurowanie Foundry Local](../../../../04-PracticalSamples/foundrylocal)
- [Uruchamianie aplikacji](../../../../04-PracticalSamples/foundrylocal)
- [Oczekiwany wynik](../../../../04-PracticalSamples/foundrylocal)
- [Kolejne kroki](../../../../04-PracticalSamples/foundrylocal)
- [Rozwiązywanie problemów](../../../../04-PracticalSamples/foundrylocal)

## Wymagania wstępne

Przed rozpoczęciem tego samouczka upewnij się, że masz:

- **Java 21 lub nowsza** zainstalowana na Twoim systemie
- **Maven 3.6+** do budowania projektu
- **Foundry Local** zainstalowany i uruchomiony

### **Instalacja Foundry Local:**

```bash
# Windows
winget install Microsoft.FoundryLocal

# macOS (after installing)
foundry model run phi-3.5-mini
```

## Przegląd projektu

Ten projekt składa się z czterech głównych komponentów:

1. **Application.java** - Główna klasa uruchomieniowa aplikacji Spring Boot
2. **FoundryLocalService.java** - Warstwa serwisowa obsługująca komunikację z AI
3. **application.properties** - Konfiguracja połączenia z Foundry Local
4. **pom.xml** - Zależności Maven i konfiguracja projektu

## Zrozumienie kodu

### 1. Konfiguracja aplikacji (application.properties)

**Plik:** `src/main/resources/application.properties`

```properties
foundry.local.base-url=http://localhost:5273
foundry.local.model=Phi-3.5-mini-instruct-cuda-gpu
```

**Co to robi:**
- **base-url**: Określa, gdzie działa Foundry Local (domyślny port 5273)
- **model**: Nazwa modelu AI używanego do generowania tekstu

**Kluczowa koncepcja:** Spring Boot automatycznie ładuje te właściwości i udostępnia je aplikacji za pomocą adnotacji `@Value`.

### 2. Główna klasa aplikacji (Application.java)

**Plik:** `src/main/java/com/example/Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);  // No web server needed
        app.run(args);
    }
```

**Co to robi:**
- `@SpringBootApplication` włącza automatyczną konfigurację Spring Boot
- `WebApplicationType.NONE` informuje Spring, że to aplikacja konsolowa, a nie serwer webowy
- Metoda main uruchamia aplikację Spring

**Runner demonstracyjny:**
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

**Co to robi:**
- `@Bean` tworzy komponent zarządzany przez Spring
- `CommandLineRunner` uruchamia kod po starcie Spring Boot
- `foundryLocalService` jest automatycznie wstrzykiwany przez Spring (wstrzykiwanie zależności)
- Wysyła wiadomość testową do AI i wyświetla odpowiedź

### 3. Warstwa serwisowa AI (FoundryLocalService.java)

**Plik:** `src/main/java/com/example/FoundryLocalService.java`

#### Wstrzykiwanie konfiguracji:
```java
@Service
public class FoundryLocalService {
    
    @Value("${foundry.local.base-url:http://localhost:5273}")
    private String baseUrl;
    
    @Value("${foundry.local.model:Phi-3.5-mini-instruct-cuda-gpu}")
    private String model;
```

**Co to robi:**
- `@Service` informuje Spring, że ta klasa dostarcza logikę biznesową
- `@Value` wstrzykuje wartości konfiguracyjne z application.properties
- Składnia `:default-value` zapewnia wartości domyślne, jeśli właściwości nie są ustawione

#### Inicjalizacja klienta:
```java
@PostConstruct
public void init() {
    this.openAIClient = OpenAIOkHttpClient.builder()
            .baseUrl(baseUrl + "/v1")        // Foundry Local uses OpenAI-compatible API
            .apiKey("unused")                 // Local server doesn't need real API key
            .build();
}
```

**Co to robi:**
- `@PostConstruct` uruchamia tę metodę po utworzeniu serwisu przez Spring
- Tworzy klienta OpenAI wskazującego na lokalną instancję Foundry Local
- Ścieżka `/v1` jest wymagana dla kompatybilności z API OpenAI
- Klucz API to "unused", ponieważ lokalny rozwój nie wymaga uwierzytelniania

#### Metoda czatu:
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

**Co to robi:**
- **ChatCompletionCreateParams**: Konfiguruje żądanie do AI
  - `model`: Określa, który model AI ma być użyty
  - `addUserMessage`: Dodaje wiadomość użytkownika do konwersacji
  - `maxCompletionTokens`: Ogranicza długość odpowiedzi (oszczędza zasoby)
  - `temperature`: Kontroluje losowość (0.0 = deterministyczne, 1.0 = kreatywne)
- **Wywołanie API**: Wysyła żądanie do Foundry Local
- **Obsługa odpowiedzi**: Bezpiecznie wyodrębnia tekstową odpowiedź AI
- **Obsługa błędów**: Opakowuje wyjątki w pomocne komunikaty o błędach

### 4. Zależności projektu (pom.xml)

**Kluczowe zależności:**

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

**Co robią:**
- **spring-boot-starter**: Dostarcza podstawową funkcjonalność Spring Boot
- **openai-java**: Oficjalne SDK OpenAI dla komunikacji z API
- **jackson-databind**: Obsługuje serializację/deserializację JSON dla wywołań API

## Jak to wszystko działa razem

Oto pełny przepływ działania aplikacji:

1. **Start**: Spring Boot uruchamia się i odczytuje `application.properties`
2. **Tworzenie serwisu**: Spring tworzy `FoundryLocalService` i wstrzykuje wartości konfiguracyjne
3. **Konfiguracja klienta**: `@PostConstruct` inicjalizuje klienta OpenAI do połączenia z Foundry Local
4. **Wykonanie demo**: `CommandLineRunner` uruchamia się po starcie
5. **Wywołanie AI**: Demo wywołuje `foundryLocalService.chat()` z wiadomością testową
6. **Żądanie API**: Serwis buduje i wysyła żądanie kompatybilne z OpenAI do Foundry Local
7. **Przetwarzanie odpowiedzi**: Serwis wyodrębnia i zwraca odpowiedź AI
8. **Wyświetlanie**: Aplikacja drukuje odpowiedź i kończy działanie

## Konfigurowanie Foundry Local

Aby skonfigurować Foundry Local, wykonaj następujące kroki:

1. **Zainstaluj Foundry Local** zgodnie z instrukcjami w sekcji [Wymagania wstępne](../../../../04-PracticalSamples/foundrylocal).
2. **Pobierz model AI**, którego chcesz używać, na przykład `phi-3.5-mini`, za pomocą następującego polecenia:
   ```bash
   foundry model run phi-3.5-mini
   ```
3. **Skonfiguruj plik application.properties**, aby dopasować ustawienia Foundry Local, szczególnie jeśli używasz innego portu lub modelu.

## Uruchamianie aplikacji

### Krok 1: Uruchom Foundry Local
```bash
foundry model run phi-3.5-mini
```

### Krok 2: Zbuduj i uruchom aplikację
```bash
mvn clean package
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

## Oczekiwany wynik

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

## Kolejne kroki

Więcej przykładów znajdziesz w [Rozdziale 04: Praktyczne przykłady](../README.md)

## Rozwiązywanie problemów

### Typowe problemy

**"Connection refused" lub "Service unavailable"**
- Upewnij się, że Foundry Local działa: `foundry model list`
- Zweryfikuj, czy usługa działa na porcie 5273: Sprawdź `application.properties`
- Spróbuj ponownie uruchomić Foundry Local: `foundry model run phi-3.5-mini`

**Błędy "Model not found"**
- Sprawdź dostępne modele: `foundry model list`
- Zaktualizuj nazwę modelu w `application.properties`, aby dokładnie pasowała
- Pobierz model, jeśli to konieczne: `foundry model run phi-3.5-mini`

**Błędy kompilacji Maven**
- Upewnij się, że masz Java 21 lub nowszą: `java -version`
- Wyczyść i zbuduj ponownie: `mvn clean compile`
- Sprawdź połączenie internetowe dla pobierania zależności

**Aplikacja uruchamia się, ale brak wyników**
- Zweryfikuj, czy Foundry Local odpowiada: Otwórz przeglądarkę na `http://localhost:5273`
- Sprawdź logi aplikacji w poszukiwaniu konkretnych komunikatów o błędach
- Upewnij się, że model jest w pełni załadowany i gotowy

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby tłumaczenie było precyzyjne, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego rodzimym języku powinien być uznawany za autorytatywne źródło. W przypadku informacji o kluczowym znaczeniu zaleca się skorzystanie z profesjonalnego tłumaczenia przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.