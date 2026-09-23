# Samouczek kalkulatora MCP dla początkujących

## Spis treści

- [Czego się nauczysz](#czego-się-nauczysz)
- [Wymagania wstępne](#wymagania-wstępne)
- [Wersje zależności](#wersje-zależności)
- [Zrozumienie struktury projektu](#zrozumienie-struktury-projektu)
- [Wyjaśnienie głównych składników](#wyjaśnienie-głównych-składników)
  - [1. Główna aplikacja](#1-główna-aplikacja)
  - [2. Serwis kalkulatora](#2-serwis-kalkulatora)
  - [3. Bezpośredni klient MCP](#3-bezpośredni-klient-mcp)
  - [4. Klient wspierany przez AI](#4-klient-wspierany-przez-ai)
- [Uruchamianie przykładów](#uruchamianie-przykładów)
- [Testy offline](#testy-offline)
- [Jak to wszystko współgra](#jak-to-wszystko-współgra)
- [Kolejne kroki](#kolejne-kroki)

## Czego się nauczysz

Ten samouczek wyjaśnia, jak zbudować serwis kalkulatora używając Model Context Protocol (MCP). Dowiesz się:

- Jak stworzyć serwis, z którego AI może korzystać jako z narzędzia
- Jak skonfigurować bezpośrednią komunikację z usługami MCP
- Jak modele AI mogą automatycznie wybierać, których narzędzi użyć
- Różnica między bezpośrednimi wywołaniami protokołu a interakcjami wspomaganymi przez AI

## Wymagania wstępne

Przed rozpoczęciem upewnij się, że masz:
- Zainstalowaną Javę 21 lub nowszą
- Mavena do zarządzania zależnościami
- Podstawową znajomość Javy i Spring Boot

Tylko klienci AI wymagają wdrożenia Azure OpenAI i uwierzytelnionego `DefaultAzureCredential`,
na przykład przez lokalne zalogowanie się do Azure CLI lub zarządzaną tożsamość w Azure. Tożsamość musi mieć
rolę użytkownika Cognitive Services OpenAI na zasobie. Zobacz [Rozdział 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md).
Serwer, bezpośredni klient SDK oraz wszystkie automatyczne testy nie potrzebują konta Azure ani dostępu do modelu.

## Wersje zależności

Zweryfikowane zależności na dzień 2026-09-14:

| Zależność | Wersja |
| --- | --- |
| Spring Boot | 4.1.1 |
| Spring AI | 2.0.1 |
| MCP Java SDK (zarządzany przez Spring AI) | 2.0.0 |
| LangChain4j / core | 1.20.0 |
| LangChain4j MCP | 1.20.0-beta30 |
| Oficjalny adapter OpenAI LangChain4j | 1.20.0-beta30 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |
| JUnit Jupiter (zarządzany przez Boot) | 6.0.3 |

Adaptery MCP i oficjalny OpenAI są opublikowanymi wersjami beta w Maven Central, nie snapshotami.
Ich wersje różnią się od LangChain4j core. Nie są potrzebne repozytoria snapshot ani milestone.
Zależności tylko dla klienta mają zakres testowy, ponieważ wykonalne przykłady znajdują się pod `src/test/java`.

## Zrozumienie struktury projektu

Projekt kalkulatora zawiera kilka ważnych plików:

```
calculator/
├── src/main/java/com/microsoft/mcp/sample/server/
│   ├── McpServerApplication.java          # Main Spring Boot app
│   └── service/CalculatorService.java     # Calculator operations
└── src/test/java/com/microsoft/mcp/sample/client/
    ├── SDKClient.java                     # Direct MCP communication
    ├── LangChain4jClient.java            # AI-powered client
    └── Bot.java                          # Chat interface and interactive entrypoint
```

## Wyjaśnienie głównych składników

### 1. Główna aplikacja

**Plik:** `McpServerApplication.java`

To punkt startowy naszego serwisu kalkulatora. Jest to standardowa aplikacja Spring Boot z jednym specjalnym dodatkiem:

```java
@SpringBootApplication
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
    
    @Bean
    public ToolCallbackProvider calculatorTools(CalculatorService calculator) {
        return MethodToolCallbackProvider.builder().toolObjects(calculator).build();
    }
}
```

**Co to robi:**
- Uruchamia serwer sieciowy Spring Boot na porcie 8080
- Tworzy `ToolCallbackProvider`, który udostępnia metody kalkulatora jako narzędzia MCP
- Adnotacja `@Bean` mówi Springowi, aby zarządzał tym jako komponentem, z którego mogą korzystać inne części

### 2. Serwis kalkulatora

**Plik:** `CalculatorService.java`

To tutaj odbywają się wszystkie działania matematyczne. Każda metoda jest oznaczona `@Tool`, aby była dostępna przez MCP:

```java
@Service
public class CalculatorService {

    @Tool(description = "Add two numbers together")
    public String add(double a, double b) {
        double result = a + b;
        return formatResult(a, "+", b, result);
    }

    @Tool(description = "Subtract the second number from the first number")
    public String subtract(double a, double b) {
        double result = a - b;
        return formatResult(a, "-", b, result);
    }
    
    // Więcej działań kalkulatora...
    
    private String formatResult(double a, String operator, double b, double result) {
        return String.format(java.util.Locale.ROOT, "%.2f %s %.2f = %.2f", a, operator, b, result);
    }
}
```

**Kluczowe cechy:**

1. **Adnotacja `@Tool`**: Informuje MCP, że ta metoda może być wywołana przez zewnętrznych klientów
2. **Jasne opisy**: Każde narzędzie ma opis pomagający modelom AI zrozumieć, kiedy je używać
3. **Spójny format zwracanych wartości**: Wszystkie operacje zwracają czytelne dla człowieka napisy, np. "5.00 + 3.00 = 8.00"
4. **Obsługa błędów**: Dzielenie przez zero i pierwiastek z liczby ujemnej zwracają komunikaty o błędach

**Dostępne operacje:**
- `add(a, b)` - Dodaje dwie liczby
- `subtract(a, b)` - Odejmuje drugą od pierwszej
- `multiply(a, b)` - Mnoży dwie liczby
- `divide(a, b)` - Dzieli pierwszą przez drugą (z weryfikacją dzielenia przez zero)
- `power(base, exponent)` - Podnosi podstawę do potęgi wykładnika
- `squareRoot(number)` - Oblicza pierwiastek kwadratowy (z weryfikacją na ujemne)
- `modulus(a, b)` - Zwraca resztę z dzielenia
- `absolute(number)` - Zwraca wartość bezwzględną
- `help()` - Zwraca informacje o wszystkich operacjach

### 3. Bezpośredni klient MCP

Zobacz [SDKClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/SDKClient.java).

Ten klient używa `HttpClientStreamableHttpTransport` pod `/mcp`, inicjalizuje połączenie,
wysyła ping do serwera i obsługuje stronicowanie listy narzędzi. Sprawdza, czy istnieje wszystkich dziewięć oczekiwanych narzędzi
i wywołuje każde z nich, w tym `modulus` i `help`, bez modelu AI.

Aktualny kreator zapytań wygląda tak:

```java
var request = CallToolRequest.builder("add")
    .arguments(Map.of("a", 5.0, "b", 3.0))
    .build();
var result = client.callTool(request);
```

Błędy protokołu powodują niepowodzenie klienta, zamiast mylącego komunikatu o sukcesie. Klient MCP
jest zamykany za pomocą try-with-resources, także gdy wykrywanie lub wywołanie narzędzia się nie powiodą.

### 4. Klient wspierany przez AI

Zobacz [LangChain4jClient.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/LangChain4jClient.java)
i [Bot.java](../../../../04-PracticalSamples/calculator/src/test/java/com/microsoft/mcp/sample/client/Bot.java).

`OpenAiOfficialChatModel` implementuje aktualne API LangChain4j `ChatModel`.
`StreamableHttpMcpTransport` łączy go z tym samym endpointem `/mcp`, co klient SDK.
`AiServices` wykrywa narzędzia i zarządza konwersacją wywołań narzędzi oraz wyników.

Domyślne wdrożenie to **GPT-5.6 Luna**, z wyłączonym explicite rozumowaniem:

```java
var parameters = OpenAiOfficialChatRequestParameters.builder()
    .modelName("gpt-5.6-luna")
    .reasoningEffort("none")
    .maxCompletionTokens(1024)
    .parallelToolCalls(false)
    .build();
```

Te domyślne ustawienia mają zastosowanie do każdego zakończenia, w tym follow-upów po wykonaniu narzędzia.
Klient używa odświeżalnego `BearerTokenCredential` opartego na `DefaultAzureCredential`
oraz zakresie `https://ai.azure.com/.default`, a nie jednorazowego tokena podanego jako klucz API.
Akceptowane są zarówno URL zasobu, jak i URL kończące się już na `/openai/v1`.

Bot utrzymuje ograniczoną historię rozmowy, drukuje `Tool executed: ...` z faktycznym
wynikiem MCP i kończy działanie, jeśli odpowiedź pomija narzędzia. Pętle narzędzi są ograniczone do czterech rund.
Błędy uwierzytelniania, modelu, MCP i narzędzi są propagowane; automatyczne powtarzanie dla modelu jest wyłączone.
Zarówno transport/klient MCP, jak i oficjalny klient OpenAI są zamykane przy sukcesie lub porażce.

## Uruchamianie przykładów

### Krok 1: Uruchom serwer kalkulatora

Serwer nie wymaga konfiguracji Azure. Poniższe polecenia uruchom z katalogu tego przykładu.
Przykład używa portu **18081**, aby uniknąć konfliktu z innym przykładem; domyślny pozostaje 8080.

```powershell
cd 04-PracticalSamples/calculator
mvn spring-boot:run "-Dspring-boot.run.arguments=--server.port=18081"
```

Punkt końcowy MCP to `http://localhost:18081/mcp`. Informacje o stanie i wykryciu są pod
`http://localhost:18081/health` i `http://localhost:18081/info`.
Streamable HTTP zastępuje stary transport tylko SSE; `/sse` i `/v1/tools` nie są endpointami.

### Krok 2: Testuj za pomocą klienta bezpośredniego

W kolejnym terminalu PowerShell:

```powershell
cd 04-PracticalSamples/calculator
$env:MCP_SERVER_URL = "http://localhost:18081"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.SDKClient" "-Dexec.classpathScope=test"
```

Nie jest potrzebne dane wejściowe. Przetestowane są wszystkie dziewięć narzędzi. Oczekiwane wyniki arytmetyczne to
8, 6, 42, 5, 256, 4, 2 i 5.5, a następnie tekst pomocy.

### Krok 3: Testuj z klientem AI

Po uwierzytelnieniu opisanym w wymaganiach wstępnych, skonfiguruj klienta AI w tym samym terminalu:

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Calculate the sum of 24.5 and 17.3 using the calculator service'"
```

Spodziewaj się linii `Tool executed: add` z wartością `41.80`, a potem odpowiedzi modelu.
Tryb pojedynczej podpowiedzi wychodzi bez oczekiwania na dane. Aby uruchomić oryginalną demonstrację z czterema podpowiedziami:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.LangChain4jClient" "-Dexec.classpathScope=test" "-Dexec.args=--demo"
```

Demonstracja wywołuje `add`, `squareRoot`, `help` oraz łańcuchową operację `power` a następnie `divide`.
Oczekiwane wyniki numeryczne to 41.8, 12 i 64. Pominięcie argumentów również uruchomi tę demonstrację.

### Krok 4: Uruchom interaktywnego bota

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test"
```

Wpisz `Multiply 6 by 7 using the calculator service`, następnie `exit` lub `quit`.
Spodziewaj się faktycznego wyniku `multiply` równego 42. Puste linie są ignorowane; EOF kończy sesję.
Aby wykonać nieinteraktywny test dymny tego punktu wejścia:

```powershell
mvn test-compile exec:java "-Dexec.mainClass=com.microsoft.mcp.sample.client.Bot" "-Dexec.classpathScope=test" "-Dexec.args=--prompt 'Multiply 6 by 7 using the calculator service'"
```

Oba punkty wejścia AI obsługują `--prompt "question"`, `--demo` oraz `--interactive`.
Nieprawidłowe opcje przerywają działanie przed otwarciem połączenia. Każdy argument Maven `-D...` jest w pełni cytowany
dla PowerShell. W Bash użyj `export NAME=value` zamiast `$env:NAME = "value"`.

**Limit:** Uruchamiaj próbki AI kolejno. Prosta podpowiedź zazwyczaj wymaga dwóch zapytań do modelu;
pełna demonstracja zwykle wymaga dziewięciu, włącznie z follow-upami wyników narzędzi. Przy współdzielonym wdrożeniu na 10 RPM
odczekaj świeże okno limitu przed kolejnym uruchomieniem AI. Błąd 429 kończy się widocznie bez
automatycznych powtórzeń; stosuj się do wytycznych usługi co do retry-after. Faktyczne liczby zapytań zależą od modelu.
Testy offline nie zużywają żadnego limitu i nie ustanawiają dostępności Luna ani jakości odpowiedzi na żywo.

### Konfiguracja i zamknięcie

| Ustawienie | Domyślnie / zachowanie |
| --- | --- |
| `MCP_SERVER_URL` | `http://localhost:8080`; baza URL, bez `/mcp` |
| `-Dmcp.server.url=...` | Nadpisuje `MCP_SERVER_URL` dla wszystkich klientów |
| `AZURE_OPENAI_ENDPOINT` | Wymagane tylko dla klientów AI; URL zasobu lub URL `/openai/v1` |
| `AZURE_OPENAI_DEPLOYMENT` | `gpt-5.6-luna`; nazwa wdrożenia Azure |
| `AZURE_OPENAI_MAX_COMPLETION_TOKENS` | `1024`; dodatnia liczba całkowita |
| Nakład pracy rozumowania | Zawsze `none`, w tym follow-upy pętli narzędzi |

Nadpisanie wdrożenia musi wspierać `reasoning_effort=none` i `max_completion_tokens`.
Klienci nie czytają automatycznie pliku `.env`. Zatrzymaj serwer za pomocą `Ctrl+C` po testach.
Klienci kończą działanie normalnie bez `System.exit` lub opóźnień przy zamykaniu.

## Testy offline

```powershell
mvn -B -ntp clean verify
```

Wszystkie testy są offline względem Azure: pakiet protokołów uruchamia serwer Spring oraz
stub kompatybilny z OpenAI na losowych portach loopback, następnie je zamyka. Maven może nadal potrzebować
pobrać zależności. Nie używa się danych uwierzytelniających, aktywnego wdrożenia ani wcześniej istniejącego serwera MCP.

- Testy jednostkowe kalkulatora obejmują wszystkie operacje arytmetyczne, wyniki dziesiętne, pomoc oraz błędy domenowe.
- Testy MCP obejmują inicjalizację, wykrywanie, wszystkie dziewięć wywołań narzędzi, błędy narzędzi oraz stan/informacje.
- Testy protokołu AI wykonują pełną demonstrację i interaktywnego Bota przeciwko prawdziwemu kalkulatorowi,
  weryfikują, że wyniki narzędzi są przekazywane dalej do kolejnych uzupełnień oraz inspekcjonują każde ciało HTTP pod kątem Luna,
  `reasoning_effort: "none"` i `max_completion_tokens` bez przestarzałego `max_tokens`.
- Testy konfiguracji/wejścia obejmują nadpisania wdrożeń i endpointów, puste linie, EOF, exit/quit,
  tryb pojedynczej podpowiedzi, nieprawidłowe opcje oraz propagację błędów. Testy limitów pokazują, że 429 nie jest powtarzany.

## Jak to wszystko współgra

Oto pełny przebieg zdarzeń, gdy pytasz AI "Ile to 5 + 3?":

1. **Ty** zadajesz AI pytanie w naturalnym języku
2. **AI** analizuje twoją prośbę i rozumie, że chcesz dodać liczby
3. **AI** wywołuje serwer MCP: `add(5.0, 3.0)`
4. **Serwis kalkulatora** wykonuje: `5.0 + 3.0 = 8.0`
5. **Serwis kalkulatora** zwraca: `"5.00 + 3.00 = 8.00"`
6. **AI** otrzymuje wynik i formatuje naturalną odpowiedź
7. **Ty** dostajesz: "Suma 5 i 3 to 8"

## Kolejne kroki

Aby zobaczyć więcej przykładów, zobacz [Rozdział 04: Praktyczne przykłady](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym należy uznawać za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->