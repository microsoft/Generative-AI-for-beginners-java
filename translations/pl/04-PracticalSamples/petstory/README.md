# Samouczek Generatora Opowieści o Zwierzakach dla Początkujących

Prześlij zdjęcie zwierzaka, przeanalizuj je za pomocą GPT-5.6 Luna i wygeneruj opowieść na podstawie uzyskanego opisu. Oba zapytania do modelu używają `reasoning_effort: none`.

| Komponent | Wersja |
| --- | --- |
| Java | 21 lub wyższa |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Azure Identity | 1.18.6 |

## Spis Treści

- [Wymagania wstępne](#wymagania-wstępne)
- [Zrozumienie struktury projektu](#zrozumienie-struktury-projektu)
- [Wyjaśnienie kluczowych komponentów](#wyjaśnienie-kluczowych-komponentów)
  - [1. Główna aplikacja](#1-główna-aplikacja)
  - [2. Kontroler sieciowy](#2-kontroler-sieciowy)
  - [3. Serwis opowieści](#3-serwis-opowieści)
  - [4. Szablony sieciowe](#4-szablony-www)
  - [5. Konfiguracja](#5-konfiguracja)
- [Uruchamianie aplikacji](#uruchamianie-aplikacji)
- [Testy offline](#testy-offline)
- [Jak to wszystko działa razem](#jak-to-wszystko-działa-razem)
- [Zrozumienie integracji AI](#zrozumienie-integracji-ai)
- [Kolejne kroki](#kolejne-kroki)

## Wymagania wstępne

Przed rozpoczęciem upewnij się, że masz:
- Zainstalowaną Javę 21 lub wyższą
- Maven do zarządzania zależnościami
- Wdrożenie Azure AI Foundry GPT-5.6 Luna o nazwie `gpt-5.6-luna` lub nadpisanie `AZURE_OPENAI_DEPLOYMENT` wskazujące na to wdrożenie. Zobacz [Rozdział 2](../../02-SetupDevEnvironment/getting-started-azure-openai.md) w celu provisioning i zaloguj się za pomocą `az login` do uwierzytelniania bez klucza. Wdrożenie musi obsługiwać wejście obrazu i `reasoning_effort: none`.
- Podstawową znajomość Javy, Spring Boot i tworzenia stron WWW

## Zrozumienie struktury projektu

Projekt opowieści o zwierzaku zawiera kilka ważnych plików:

```
petstory/
├── src/main/java/com/example/petstory/
│   ├── PetStoryApplication.java       # Main Spring Boot application
│   ├── PetController.java             # Web request handler
│   ├── StoryService.java              # AI image analysis and story generation
│   └── SecurityConfig.java            # Security configuration
├── src/main/resources/
│   ├── application.properties         # App configuration
│   └── templates/
│       ├── index.html                 # Upload form page
│       └── result.html               # Story display page
└── pom.xml                           # Maven dependencies
```

## Wyjaśnienie kluczowych komponentów

### 1. Główna aplikacja

**Plik:** `PetStoryApplication.java`

To jest punkt wejścia naszej aplikacji Spring Boot:

```java
@SpringBootApplication
public class PetStoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetStoryApplication.class, args);
    }
}
```

**Co to robi:**
- Adnotacja `@SpringBootApplication` umożliwia automatyczną konfigurację i skanowanie komponentów
- Uruchamia wbudowany serwer WWW (Tomcat) na porcie 8080
- Tworzy automatycznie wszystkie wymagane beany i serwisy Spring

### 2. Kontroler sieciowy

**Plik:** [PetController.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/PetController.java)

| Endpoint | Żądanie | Odpowiedź sukcesu |
| --- | --- | --- |
| `GET /` | Brak ciała | Formularz HTML do przesłania pliku z tokenem CSRF |
| `POST /analyze-image` | `multipart/form-data`, pole pliku `image` | JSON: `{"description":"Zabawny zwierzak..."}` |
| `POST /generate-story` | `application/x-www-form-urlencoded`, pole `description` | Strona HTML z wynikiem zawierającym opis i wygenerowaną opowieść |

Oba punkty końcowe POST wymagają ciasteczka sesji i tokena CSRF pobranego z `GET /`. Skrypt przesyłający wysyła ukrytą wartość `_csrf` w nagłówku `X-CSRF-TOKEN`; przesłanie historii wysyła ją jako pole formularza `_csrf`. Klienci API muszą zachować ciasteczko między żądaniami. To są punkty końcowe formularzy, nie żądania JSON.

Opisy muszą być niepuste i nie dłuższe niż 1000 znaków. Kontroler przycina opis i usuwa `<`, `>`, podwójne cudzysłowy, apostrofy i `&` przed przekazaniem do serwisu. Szablon wynikowy także ucieka dane modelu za pomocą `th:text`.

Niepowodzenia walidacji obrazu zwracają HTTP 400 z polem `error`; błędy modelu zwracają HTTP 502 z polem `error` i bez `description`. Nieprawidłowe opisy historii lub błędy modelu przekierowują na `/` z widocznym błędem. Brak wymaganych pól zwraca HTTP 400, a brak lub niewłaściwe tokeny CSRF HTTP 403. Brak jest podawania alternatywnych opisów lub historii jako wyników AI.

### 3. Serwis opowieści

**Plik:** [StoryService.java](../../../../04-PracticalSamples/petstory/src/main/java/com/example/petstory/StoryService.java)

Oficjalny OpenAI Java SDK 4.63.1 wywołuje kompatybilne z OpenAI API usługi Chat Completions w Azure AI Foundry. Azure Identity 1.18.6 dostarcza token nosiciela Microsoft Entra przez `DefaultAzureCredential`; nie jest wymagany klucz API.

| Operacja | Wejście | `max_completion_tokens` |
| --- | --- | --- |
| `analyzeImage` | Bajty obrazu zakodowane jako URL danych base64 z przesłanym typem MIME | 300 |
| `generateStory` | Opis zwierzaka w wiadomości użytkownika | 800 |

Oba zapytania używają skonfigurowanego wdrożenia, domyślnie `gpt-5.6-luna`, i jawnie ustawiają `ReasoningEffort.NONE` (`reasoning_effort: none`). Żadne z zapytań nie przesyła parametru `temperature` ani przestarzałego `max_tokens`.

Analiza obrazu akceptuje JPEG, PNG, GIF i WebP, odrzuca puste obrazy i pliki powyżej 10MB, a opis wynikowy ogranicza do 1000 znaków. Tekst żądania historii wymaga krótkiej opowieści przyjaznej rodzinie. Puste wybory lub brak zawartości modelu są błędami, a błędy zachowują oryginalną przyczynę do diagnostyki po stronie serwera. Klient SDK jest zamykany podczas zamykania aplikacji.

### 4. Szablony WWW

**Plik:** [index.html](../../../../04-PracticalSamples/petstory/src/main/resources/templates/index.html) (Formularz przesyłania)

Strona rozpoczyna się od wybierania zdjęcia, a nie pola tekstowego opisu. **Analizuj obraz** podgląda wybrane zdjęcie i wysyła je do `/analyze-image`. Udana odpowiedź wyświetla opis, uzupełnia ukryte pole `description` i odsłania **Generuj historię**. Ten przycisk wysyła istniejący formularz do `/generate-story`.

Nie ma pobierania modelu w przeglądarce ani zależności od CDN. Analiza obrazu odbywa się na serwerze przez skonfigurowane wdrożenie Azure. Błędy pozostają widoczne i nie pozwalają na generowanie historii z wymyślonym opisem. Wybranie innego pliku czyści poprzednią analizę.

**Plik:** `result.html` (Wyświetlanie opowieści)

Wyświetla wygenerowaną historię:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Pet Story Result</title>
</head>
<body>
    <div class="container">
        <h1>Your Pet's Story</h1>
        
        <div class="result-section">
            <div class="result-label">Pet Description:</div>
            <div class="result-content" th:text="${caption}"></div>
        </div>
        
        <div class="result-section">
            <div class="result-label">Generated Story:</div>
            <div class="result-content" th:text="${story}"></div>
        </div>
        
        <div class="result-section" th:if="${analysisType}">
            <div class="result-label">Analysis Type:</div>
            <div class="result-content" th:text="${analysisType}"></div>
        </div>
        
        <a href="/" class="back-link">Generate Another Story</a>
    </div>
</body>
</html>
```

**Cechy szablonu:**

1. **Integracja Thymeleaf**: Używa atrybutów `th:` do dynamicznej zawartości
2. **Responsywny design**: Style CSS dla urządzeń mobilnych i stacjonarnych
3. **Obsługa błędów**: Wyświetla błędy walidacji użytkownikom
4. **Obsługa przesyłania plików**: JavaScript podgląda zdjęcie, wysyła żądanie multipart z ochroną CSRF i wyświetla zwrócony opis

### 5. Konfiguracja

**Plik:** `application.properties`

Ustawienia konfiguracyjne aplikacji:

```properties
spring.application.name=pet-story-app

# File upload limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Logging configuration
logging.level.com.example.petstory=INFO

# Azure AI Foundry (keyless) configuration
azure.openai.endpoint=${AZURE_OPENAI_ENDPOINT:}
azure.openai.deployment=${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
```

**Wyjaśnienie konfiguracji:**

1. **Przesyłanie plików**: Rozmiar zarówno pliku, jak i całego żądania multipart jest ograniczony do 10MB; trzymaj zdjęcia poniżej tego limitu, by zostawić miejsce na nagłówki multipart
2. **Logowanie**: Kontroluje jakie informacje są logowane podczas działania
3. **Azure AI Foundry**: Określa punkt końcowy i wdrożenie modelu do użycia (uwierzytelnianie bez klucza)
4. **Bezpieczeństwo**: Ochrona CSRF pozostaje włączona; diagnostyka modelu jest logowana po stronie serwera, podczas gdy kontroler pokazuje ogólne komunikaty o błędzie modelu

## Uruchamianie aplikacji

### Krok 1: Zaloguj się i ustaw punkt końcowy

Uwierzytelnianie jest bezkluczowe (Microsoft Entra ID), więc nie ma klucza API. Zaloguj się i ustaw punkt końcowy Foundry:

**Windows (Wiersz polecenia):**
```cmd
az login
set AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Windows (PowerShell):**
```powershell
az login
$env:AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
```

**Linux/macOS:**
```bash
az login
export AZURE_OPENAI_ENDPOINT=https://your-resource.openai.azure.com/
```

**Dlaczego to jest potrzebne:**
- Azure AI Foundry używa Microsoft Entra ID do uwierzytelniania żądań inferencyjnych
- Uwierzytelnianie bez klucza oznacza brak tajemnic w kodzie źródłowym lub środowisku
- Twoje konto musi mieć rolę **Cognitive Services OpenAI User** na zasobie

Domyślna nazwa wdrożenia to `gpt-5.6-luna`. Jeśli Twoje wdrożenie GPT-5.6 Luna ma inną nazwę, ustaw `AZURE_OPENAI_DEPLOYMENT` w tym samym terminalu przed uruchomieniem aplikacji. Zarówno analiza obrazów, jak i generowanie historii używają tego ustawienia.

### Krok 2: Zbuduj i uruchom

Przejdź do katalogu projektu:
```bash
cd 04-PracticalSamples/petstory
```

Zbuduj samodzielny plik JAR i uruchom wszystkie testy offline:
```bash
mvn clean package
```

Uruchom serwer:
```bash
mvn spring-boot:run
```

Aplikacja uruchomi się pod adresem `http://localhost:8080`.

Alternatywnie uruchom paczkowany JAR na wolnym porcie, np.:

```bash
java -jar target/pet-story-app-0.0.1-SNAPSHOT.jar --server.port=8083
```

Dla tego polecenia otwórz `http://localhost:8083/`. Te same trasy `/analyze-image` i `/generate-story` są dostępne na wybranym porcie.

### Krok 3: Przetestuj aplikację

1. **Otwórz** `http://localhost:8080` w swojej przeglądarce
2. **Wybierz** wyraźne zdjęcie zwierzaka w formacie JPEG, PNG, GIF lub WebP poniżej 10MB
3. **Kliknij** „Analizuj obraz” i poczekaj na opis zwierzaka
4. **Kliknij** „Generuj historię” po udanej analizie
5. **Przejrzyj** historię i użyj linku na stronie wyników, aby wrócić do formularza przesyłania

Udany przepływ od zdjęcia do opowieści powoduje dwa wywołania modelu, po jednym na przycisk. Inferencja na żywo zużywa przydział Twojego wdrożenia i może generować opłaty; wykonuj testy seryjnie, gdy dzielisz limitowane wdrożenie. Ładowanie strony głównej nie wywołuje modelu.

## Testy offline

Z katalogu sample uruchom:

```bash
mvn test
```

[StoryServiceTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/StoryServiceTest.java) przechwytuje rzeczywiste żądania OpenAI SDK za pomocą lokalnego fixture HTTP loopback. Sprawdza wdrożenie obu żądań, `reasoning_effort: none`, limity tokenów, ładunek obrazu, walidację wejścia, puste odpowiedzi i błędy upstream.

[PetControllerTest.java](../../../../04-PracticalSamples/petstory/src/test/java/com/example/petstory/PetControllerTest.java) używa MockMvc z zamockowanym serwisem modelu do testowania wyrenderowanych stron Thymeleaf, kontraktu przesyłania, CSRF, walidacji, uciekania wyjścia i widocznych błędów. Te testy nie potrzebują poświadczeń Azure i nigdy nie wywołują płatnego inferowania Azure. Maven zapisuje raporty Surefire w `target/surefire-reports`.

## Jak to wszystko działa razem

Oto pełny przepływ podczas generowania opowieści o zwierzaku:

1. **Wybór zdjęcia**: Wybierasz obraz zwierzaka w formularzu przesyłania
2. **Przesłanie obrazu**: „Analizuj obraz” wysyła multipart POST do `/analyze-image` z nagłówkiem CSRF
3. **Analiza obrazu**: `StoryService` wysyła obraz do GPT-5.6 Luna z ustawieniem reasoning na `none`
4. **Wyświetlenie opisu**: Przeglądarka pokazuje zwrócony opis i zapisuje go w formularzu
5. **Przesłanie historii**: „Generuj historię” wysyła `description` i `_csrf` do `/generate-story`
6. **Generowanie historii**: Kontroler waliduje opis i wywołuje to samo wdrożenie z reasoning ustawionym na `none`
7. **Renderowanie szablonu**: Thymeleaf ucieka i wyświetla opis oraz historię na stronie wynikowej

**Przepływ obsługi błędów:**
Jeśli model zawiedzie, serwer loguje przyczynę. Analiza obrazu zwraca HTTP 502, a przeglądarka pokazuje błąd bez ujawniania „Generuj historię”. Generowanie historii przekierowuje do formularza z komunikatem o błędzie. Żadna ścieżka nie zastępuje cicho gotowym wynikiem.

## Zrozumienie integracji AI

### Azure AI Foundry (bezkluczowe)
Serwis konfiguruje SDK z użyciem punktu końcowego `/openai/v1/` Twojego zasobu. `DefaultAzureCredential` i `AuthenticationUtil.getBearerTokenSupplier` dostarczają tokeny Microsoft Entra dla `https://ai.azure.com/.default`. Lokalny rozwój może używać logowania Azure CLI; aplikacja hostowana w Azure może używać tożsamości zarządzanej z odpowiednimi uprawnieniami do zasobów.

### Projektowanie promptów
Analiza obrazu prosi o widoczne cechy zwierzaka w krótkim akapicie i instruuje model, by traktował tekst na obrazie jako dane, a nie instrukcje. Generowanie historii używa zwróconego opisu w osobnym, przyjaznym rodzinie zapytaniu twórczym. Żadne wywołanie nie włącza reasoning lub nie ustawia temperaturowych nadpisań.

### Przetwarzanie odpowiedzi
Wspólny handler odpowiedzi odrzuca brakujące wybory i puste lub zawierające tylko spacje treści, przycina ważną treść i zachowuje błędy upstream. Opisy obrazów są ograniczone do 1000 znaków, aby pasowały do kolejnego formularza historii. Oryginalna awaria modelu jest zachowywana do diagnostyki, ale nie jest pokazywana użytkownikowi.

## Kolejne kroki

Po więcej przykładów zobacz [Rozdział 04: Praktyczne przykłady](../README.md)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym należy uznawać za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->