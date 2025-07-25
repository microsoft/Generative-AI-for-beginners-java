<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:26:31+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "pl"
}
-->
# Samouczek Podstawowych Technik Generatywnej Sztucznej Inteligencji

## Spis Treści

- [Wymagania wstępne](../../../03-CoreGenerativeAITechniques)
- [Pierwsze kroki](../../../03-CoreGenerativeAITechniques)
  - [Krok 1: Ustaw zmienną środowiskową](../../../03-CoreGenerativeAITechniques)
  - [Krok 2: Przejdź do katalogu z przykładami](../../../03-CoreGenerativeAITechniques)
- [Samouczek 1: Uzupełnienia i czat LLM](../../../03-CoreGenerativeAITechniques)
- [Samouczek 2: Wywoływanie funkcji](../../../03-CoreGenerativeAITechniques)
- [Samouczek 3: RAG (Generowanie wspomagane wyszukiwaniem)](../../../03-CoreGenerativeAITechniques)
- [Samouczek 4: Odpowiedzialna SI](../../../03-CoreGenerativeAITechniques)
- [Wspólne wzorce w przykładach](../../../03-CoreGenerativeAITechniques)
- [Kolejne kroki](../../../03-CoreGenerativeAITechniques)
- [Rozwiązywanie problemów](../../../03-CoreGenerativeAITechniques)
  - [Typowe problemy](../../../03-CoreGenerativeAITechniques)

## Przegląd

Ten samouczek zawiera praktyczne przykłady podstawowych technik generatywnej sztucznej inteligencji z wykorzystaniem Javy i modeli GitHub. Nauczysz się, jak pracować z dużymi modelami językowymi (LLM), implementować wywoływanie funkcji, korzystać z generowania wspomaganego wyszukiwaniem (RAG) oraz stosować praktyki odpowiedzialnej SI.

## Wymagania wstępne

Przed rozpoczęciem upewnij się, że masz:
- Zainstalowaną Javę w wersji 21 lub wyższej
- Maven do zarządzania zależnościami
- Konto GitHub z osobistym tokenem dostępu (PAT)

## Pierwsze kroki

### Krok 1: Ustaw zmienną środowiskową

Najpierw musisz ustawić swój token GitHub jako zmienną środowiskową. Token ten umożliwia dostęp do modeli GitHub za darmo.

**Windows (Command Prompt):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Krok 2: Przejdź do katalogu z przykładami

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Samouczek 1: Uzupełnienia i czat LLM

**Plik:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Czego uczy ten przykład

Ten przykład pokazuje podstawowe mechanizmy interakcji z dużymi modelami językowymi (LLM) za pomocą API OpenAI, w tym inicjalizację klienta z modelami GitHub, wzorce struktury wiadomości dla systemowych i użytkowych podpowiedzi, zarządzanie stanem rozmowy poprzez akumulację historii wiadomości oraz dostrajanie parametrów w celu kontrolowania długości odpowiedzi i poziomu kreatywności.

### Kluczowe koncepcje kodu

#### 1. Konfiguracja klienta
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Tworzy połączenie z modelami GitHub przy użyciu Twojego tokena.

#### 2. Proste uzupełnienie
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Pamięć rozmowy
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

SI pamięta poprzednie wiadomości tylko wtedy, gdy uwzględnisz je w kolejnych żądaniach.

### Uruchomienie przykładu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Co się dzieje po uruchomieniu

1. **Proste uzupełnienie**: SI odpowiada na pytanie dotyczące Javy, korzystając z podpowiedzi systemowych
2. **Wielokrotne pytania**: SI zachowuje kontekst w trakcie kilku pytań
3. **Interaktywny czat**: Możesz prowadzić prawdziwą rozmowę z SI

## Samouczek 2: Wywoływanie funkcji

**Plik:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Czego uczy ten przykład

Wywoływanie funkcji umożliwia modelom SI żądanie wykonania zewnętrznych narzędzi i API za pomocą ustrukturyzowanego protokołu, w którym model analizuje żądania w języku naturalnym, określa wymagane wywołania funkcji z odpowiednimi parametrami przy użyciu definicji JSON Schema i przetwarza zwrócone wyniki, aby generować kontekstowe odpowiedzi, podczas gdy rzeczywiste wykonanie funkcji pozostaje pod kontrolą programisty dla zapewnienia bezpieczeństwa i niezawodności.

### Kluczowe koncepcje kodu

#### 1. Definicja funkcji
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Określa, jakie funkcje są dostępne i jak z nich korzystać.

#### 2. Przepływ wykonania funkcji
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Implementacja funkcji
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Uruchomienie przykładu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Co się dzieje po uruchomieniu

1. **Funkcja pogodowa**: SI żąda danych pogodowych dla Seattle, Ty je dostarczasz, SI formatuje odpowiedź
2. **Funkcja kalkulatora**: SI żąda obliczenia (15% z 240), Ty je wykonujesz, SI wyjaśnia wynik

## Samouczek 3: RAG (Generowanie wspomagane wyszukiwaniem)

**Plik:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Czego uczy ten przykład

Generowanie wspomagane wyszukiwaniem (RAG) łączy wyszukiwanie informacji z generowaniem języka, wstrzykując kontekst z zewnętrznych dokumentów do podpowiedzi SI. Dzięki temu modele mogą dostarczać dokładne odpowiedzi na podstawie określonych źródeł wiedzy, zamiast polegać na potencjalnie nieaktualnych lub niedokładnych danych treningowych, jednocześnie zachowując wyraźne granice między zapytaniami użytkownika a autorytatywnymi źródłami informacji poprzez strategiczne projektowanie podpowiedzi.

### Kluczowe koncepcje kodu

#### 1. Ładowanie dokumentów
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Wstrzykiwanie kontekstu
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

Potrójne cudzysłowy pomagają SI odróżnić kontekst od pytania.

#### 3. Bezpieczne przetwarzanie odpowiedzi
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Zawsze weryfikuj odpowiedzi API, aby zapobiec awariom.

### Uruchomienie przykładu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Co się dzieje po uruchomieniu

1. Program ładuje `document.txt` (zawiera informacje o modelach GitHub)
2. Zadajesz pytanie dotyczące dokumentu
3. SI odpowiada wyłącznie na podstawie treści dokumentu, a nie swojej ogólnej wiedzy

Spróbuj zapytać: "Czym są modele GitHub?" vs "Jaka jest pogoda?"

## Samouczek 4: Odpowiedzialna SI

**Plik:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Czego uczy ten przykład

Przykład odpowiedzialnej SI pokazuje, jak ważne jest wdrażanie środków bezpieczeństwa w aplikacjach SI. Demonstruje filtry bezpieczeństwa wykrywające kategorie szkodliwych treści, takie jak mowa nienawiści, nękanie, samookaleczenia, treści seksualne i przemoc, pokazując, jak produkcyjne aplikacje SI powinny obsługiwać naruszenia polityki treści poprzez odpowiednie obsługiwanie wyjątków, mechanizmy informacji zwrotnej dla użytkownika i strategie odpowiedzi awaryjnych.

### Kluczowe koncepcje kodu

#### 1. Ramy testowania bezpieczeństwa
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Testowane kategorie bezpieczeństwa
- Przemoc/instrukcje szkodliwe
- Mowa nienawiści
- Naruszenia prywatności
- Dezinformacja medyczna
- Działania nielegalne

### Uruchomienie przykładu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Co się dzieje po uruchomieniu

Program testuje różne szkodliwe podpowiedzi i pokazuje, jak system bezpieczeństwa SI:
1. **Blokuje niebezpieczne żądania** za pomocą błędów HTTP 400
2. **Pozwala na generowanie bezpiecznych treści** w normalny sposób
3. **Chroni użytkowników** przed szkodliwymi wynikami SI

## Wspólne wzorce w przykładach

### Wzorzec uwierzytelniania
Wszystkie przykłady używają tego wzorca do uwierzytelniania z modelami GitHub:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Wzorzec obsługi błędów
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Wzorzec struktury wiadomości
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Kolejne kroki

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## Rozwiązywanie problemów

### Typowe problemy

**"GITHUB_TOKEN not set"**
- Upewnij się, że ustawiłeś zmienną środowiskową
- Zweryfikuj, czy Twój token ma zakres `models:read`

**"No response from API"**
- Sprawdź swoje połączenie z internetem
- Zweryfikuj, czy Twój token jest ważny
- Sprawdź, czy nie przekroczyłeś limitów zapytań

**Błędy kompilacji Maven**
- Upewnij się, że masz Javę w wersji 21 lub wyższej
- Uruchom `mvn clean compile`, aby odświeżyć zależności

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby zapewnić poprawność tłumaczenia, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego rodzimym języku powinien być uznawany za autorytatywne źródło. W przypadku informacji krytycznych zaleca się skorzystanie z profesjonalnego tłumaczenia przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.