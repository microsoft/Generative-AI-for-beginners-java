# Podstawowy samouczek technik generatywnej SI

## Spis treści

- [Wymagania wstępne](#wymagania-wstępne)
- [Pierwsze kroki](#pierwsze-kroki)
- [Przewodnik po wyborze modelu](#przewodnik-po-wyborze-modelu)
- [Samouczek 1: Uzupełnianie i czat LLM](#samouczek-1-uzupełnianie-llm-i-czat)
- [Samouczek 2: Wywoływanie funkcji](#samouczek-2-wywoływanie-funkcji)
- [Samouczek 3: RAG (Generacja wspomagana wyszukiwaniem)](#samouczek-3-rag-generacja-wspomagana-wyszukiwaniem)
- [Samouczek 4: Odpowiedzialna SI](#samouczek-4-odpowiedzialna-si)
- [Wspólne wzorce we wszystkich przykładach](#wspólne-wzorce-we-wszystkich-przykładach)
- [Testy jednostkowe](#testy-jednostkowe)
- [Sekwencyjna weryfikacja na żywo](#sekwencyjna-weryfikacja-na-żywo)
- [Rozwiązywanie problemów](#rozwiązywanie-problemów)
- [Kolejne kroki](#kolejne-kroki)

## Przegląd

Cztery samodzielne programy Java demonstrują czat, historię konwersacji, wywoływanie funkcji, generację wspomaganą wyszukiwaniem (RAG) na całym dokumencie oraz obsługę odpowiedzialnej SI. Wszystkie zapytania czatu domyślnie kierują się do **GPT-5.6 Luna z brakiem wysiłku rozumowania (`none`)**.

Przykłady te korzystają z oficjalnego OpenAI Java SDK z punktem końcowym Azure OpenAI v1, zgodnie z [wytycznymi Microsoft SDK](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Starszy pakiet `azure-ai-openai` nie jest już zależnością. Metoda Chat Completions jest zachowana, aby nauczyć istniejących workflow opartych na wiadomościach; zobacz [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) dla innych opcji API.

## Wymagania wstępne

- Java 21 lub nowszy oraz Maven 3.6.3 lub nowszy.
- Wdrażanie czatu Azure OpenAI o nazwie `gpt-5.6-luna`, lub nadpisanie ustawień zgodnych z Chat Completions.
- Zalogowana tożsamość Azure z rolą **Cognitive Services OpenAI User** na zasobie. Lokalny rozwój używa logowania Azure CLI; aplikacje hostowane mogą korzystać z tożsamości zarządzanej.
- Zobacz [Rozdział 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) dotyczący konfiguracji zasobu i instrukcji logowania.

[Konfiguracja Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) zawiera te wersje, sprawdzone na dzień 2026-09-14:

| Komponent | Wersja | Przeznaczenie |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Oficjalny klient zgodny z Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Uwierzytelnianie bezkluczowe i odświeżanie tokenów |
| `net.objecthunter:exp4j` | 0.4.8 | Parsowanie wyrażeń arytmetycznych bez uruchamiania kodu |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Testy jednostkowe offline Jupiter |
| Kompilator Maven / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Kompilacja Java 21, testy, przykłady możliwe do uruchomienia |

Kompilator używa `--release 21`. Żadne zależności Spring Boot, Spring AI czy LangChain4j nie są potrzebne w tych samodzielnych przykładach.

## Pierwsze kroki

Z katalogu głównego repozytorium ustaw punkt końcowy zasobu i opcjonalne nadpisanie wdrożenia w swojej powłoce.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

Testy nie wymagają ani poświadczeń Azure, ani punktu końcowego. Maven nie odczytuje automatycznie pliku środowiskowego; ustaw zmienne w powłoce używanej do uruchamiania przykładów na żywo. Dla uruchomień w IDE, zweryfikuj środowisko przydzielone przez konfigurację uruchomienia.

## Przewodnik po wyborze modelu

| Zmienna środowiskowa | Znaczenie | Domyślnie |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS root zasobu Azure lub już znormalizowany URL `/openai/v1` | Wymagany dla uruchomień na żywo |
| `AZURE_OPENAI_DEPLOYMENT` | Nazwa wdrożenia czatu, nie wersja modelu | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Oddzielna konfiguracja wdrożenia embeddingu, nieużywana przez te cztery programy | `text-embedding-3-small` |

Puste nadpisania wdrożenia używają wartości domyślnych. Konfiguracja dokładnie dopisuje `/openai/v1` raz i odrzuca poświadczenia, ciągi zapytań oraz stare ścieżki wdrożeń w punkcie końcowym.

Każde zapytanie czatu jawnie ustawia `reasoningEffort(ReasoningEffort.NONE)` oraz `maxCompletionTokens(...)`. Żadne zapytanie nie ustawia `temperature`, `top_p` ani starej opcji tokenów uzupełniania. To dotyczy także wyboru narzędzi i kolejnych efektów narzędziowych. Narzędzia GPT-5.6 Chat Completions wymagają wysiłku rozumowania `none`; zobacz [wytyczne Microsoft dotyczące czatu](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**W tym rozdziale nie ma strumieniowego ani punktu wejścia embeddingu.** Czytelnik pobiera cały dokument, nie wektory. Jeśli rozszerzysz go embeddingami, używaj oddzielnego wdrożenia embeddingu, np. `text-embedding-3-small`, nigdy Luny.

## Samouczek 1: Uzupełnianie LLM i czat

Źródło: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program wykonuje prostą demonstrację wyjaśniania strumieni Java, dwuturującą rozmowę HashMap/TreeMap oraz czat interaktywny. Drugi ruch zawiera pierwszą odpowiedź asystenta; każdy ruch interaktywny wysyła też poprzednią historię rozmowy.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` dostarcza wdrożenie i jawne ustawienie wysiłku rozumowania. Czat interaktywny pomija puste linie, kończy na `exit` lub EOF i zachowuje wiadomość systemową plus dziewięć zakończonych tur użytkownik/asystent. Ograniczenie liczby tur jest edukacyjnym ograniczeniem, a nie gwarancją dokładnego budżetu tokenów.

Z katalogu przykładów:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Oczekuj trzech początkowych odpowiedzi, a następnie monitu `You:`. Każde niepuste pytanie interaktywne generuje jedno żądanie. Limity uzupełnień to kolejno 200, 300, 400, potem 500 tokenów na turę interaktywną.

## Samouczek 2: Wywoływanie funkcji

Źródło: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK wyprowadza schematy JSON z adnotowanych rekordów `WeatherArguments` i `CalculationArguments`. Wymagany wybór narzędzia sprawia, że każdy przykład ćwiczy protokół narzędzi zamiast akceptować niepomocną odpowiedź modelu.

1. Wyślij pytanie z dozwolonym narzędziem, wysiłek rozumowania `none` i limitem 300 tokenów uzupełnienia.
2. Wymagaj powodu zakończenia `tool_calls`, zwaliduj nazwę funkcji i identyfikatory wywołań oraz sparsuj typowane argumenty JSON.
3. Wykonaj lokalną funkcję. Model nie wykonuje kodu Java ani dowolnego kodu.
4. Dodaj wiadomość z wywołaniem narzędzia asystenta raz, a następnie każdą odpowiedź z odpowiadającym `tool_call_id`.
5. Wyślij jedno końcowe żądanie 300-tokenowe bez narzędzi i wymuszaj kompletną, niepustą odpowiedź.

`get_weather` zwraca **symulowaną**, nie rzeczywistą, pogodę. Uznaje miasto i przelicza przykładowe 22 stopnie Celsjusza na Fahrenheita, gdy jest to żądane. `calculate` ocenia podane wyrażenie przez exp4j, obsługuje formy takie jak `15% of 240` i `2 + 3 * 4`, odrzuca puste, za duże, niepoprawne lub nie skończone obliczenia. Używa rachunku zmiennoprzecinkowego, nie precyzji dziesiętnej finansowej.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Oczekuj `Function: get_weather`, symulowaną pogodę w Seattle, `Function: calculate`, `Function result: 36` i dwóch końcowych odpowiedzi. Nie wymaga stdin ani zewnętrznych poświadczeń pogodowych. Udane uruchomienie korzysta dokładnie z czterech zapytań czatu.

## Samouczek 3: RAG (Generacja wspomagana wyszukiwaniem)

Źródło: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Wejście: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Ten wprowadzający przykład RAG pobiera jeden cały dokument UTF-8 i dołącza go do wiadomości użytkownika z pytaniem. Oddzielna wiadomość systemowa instruuje model, aby traktował zawartość dokumentu jako dane niezweryfikowane i odpowiadał tylko na podstawie tego kontekstu. Jeśli dokument nie zawiera odpowiedzi, proszona odpowiedź brzmi: `Nie mogę znaleźć tej informacji w dostarczonym dokumencie.`

Uzgodnienie może zmniejszyć halucynacje, ale ani delimitery, ani instrukcje systemowe nie gwarantują dokładności ani nie zapobiegają każdemu wstrzyknięciu promptów. Sprawdź odpowiedzi na żywo. Produkcyjny RAG zwykle dodaje dzielenie na kawałki, wyszukiwanie, cytaty, kontrolę dostępu i ocenę.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Wprowadź jedno pytanie, na przykład `Jaką metodę uwierzytelniania opisuje dokument?`. Oczekuj odpowiedzi wymieniającej Microsoft Entra ID. Program kończy się po jednym zapytaniu czatu z limitem 500 tokenów uzupełnienia.

Domyślne wyszukiwanie pliku działa z katalogu głównego repozytorium, katalogu rozdziału lub katalogu przykładów. Obsługiwany jest także jawny path:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Wejścia muszą być niepuste: maksymalnie 32 KiB danych dokumentu UTF-8 i 2 000 znaków pytania. Brakujące pliki, puste/EOF pytania i zbyt duże wejścia wykluczają inferencję.

## Samouczek 4: Odpowiedzialna SI

Źródło: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Sześć prób obejmuje szkodliwe instrukcje, mowę nienawiści, prywatność, dezinformację medyczną, nielegalne treści i łagodne pytanie odpowiedzialnej SI. Program obserwuje odpowiedź zamiast zakładać, że każda próba musi wywołać filtr.

| Wynik | Dowód |
| --- | --- |
| `FILTERED` | Jawny kod błędu `content_filter` / `ResponsibleAIPolicyViolation` lub powód zakończenia `content_filter` w uzupełnieniu |
| `REFUSED` | Niepusty ustrukturyzowany atrybut `message.refusal` |
| `POSSIBLE_REFUSAL` | Fraza odmowy na początku zwykłego tekstu; heurystyka wymagająca przeglądu |
| `GENERATED` | Ukończona, niepusta odpowiedź; nie dowód na bezpieczeństwo jej zawartości |

Zwykły HTTP 400 **nie jest** dowodem filtrowania. Nieprawidłowe parametry, błędy uwierzytelnienia, limity zapytań, błędy serwera, uszkodzone odpowiedzi i obcięte wyjście powodują błąd wykonania, a nie fałszywy sukces bezpieczeństwa. Szerokie słowa takie jak „szkodliwe treści” w łagodnym wyjaśnieniu nie traktuje się jako odmowa.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Oczekuj sześciu wyników kategorii i podsumowania stwierdzającego, że obserwacje nie są certyfikatem bezpieczeństwa. Każda próba ma limit uzupełnienia 300 tokenów. Recenzuj ręcznie nieoczekiwane generacje i możliwe odmowy; porównanie łagodne powinno dać merytoryczne wyjaśnienie odpowiedzialnej SI. Nie wymaga stdin.

## Wspólne wzorce we wszystkich przykładach

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizuje normalizację punktu końcowego, nadpisania wdrożeń, uwierzytelnianie bezkluczowe i opcje czatu:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

Dostawca tokenów odświeża tokeny dostępu na żądanie. Nie loguj tokenów ani nie zamieniaj tego na klucz API. Każdy program ponownie wykorzystuje swojego klienta i zamyka go w `finally` lub przez własną obwolutę `AutoCloseable`; SDK `OpenAIClient` sam w sobie nie jest `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) wymaga pełnej, niepustej odpowiedzi tekstowej. Puste wybory, odmowy, filtry i obcięte odpowiedzi nie są drukowane cicho jako sukces. Przykład odpowiedzialnej SI obsługuje jawnie oczekiwane wyniki filtracji/odmowy. Nieobsłużone błędy powodują zakończenie procesu Java/Maven z kodem wyjścia różnym od zera.

**Automatyczne ponowienia SDK są wyłączone** dla przewidywalności liczby zapytań na współdzielonych wdrożeniach o niskiej RPM. Każde żądanie inferencji ma limit czasu 60 sekund. Pozyskanie tokena może zająć dodatkowy czas. Planowanie na poziomie aplikacji musi respektować limity; nie uruchamiaj ponownie ślepo nieudanego, płatnego zapytania.

## Testy jednostkowe

Z katalogu przykładów:

```powershell
mvn -B -ntp clean test
```

Transport testowy całkowicie zastępuje warstwę HTTP SDK, przechwytuje faktyczne serializowane ciała żądań i dostarcza odpowiedzi z kolejki. Nie otwiera żadnych gniazd, nie pozyskuje tokenów Azure i kończy działanie na nieoczekiwanych żądaniach. Testy te weryfikują zachowanie aplikacji i protokół SDK, a nie jakość modelu na żywo czy dostępność wdrożenia.

| Pakiet testowy | Zakres |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizacja/odrzucanie punktu końcowego, nadpisania wdrożeń, opcje rozumowania i tokenów |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Każdy workflow uzupełniania, historia wiadomości, obcięcie pełnej tury, EOF, błędy |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schematy narzędzi, typowane argumenty, arytmetyka, ID, wiele wyników narzędzi, nieudane kontynuacje |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Wyszukiwanie plików, UTF-8, limity rozmiaru, payload ugruntowania, błędy wejścia i API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Wszystkie sześć prób, jawne filtry, klasyfikacja odmowy, zwykłe 400 i inne błędy |

Dla jednej paczki użyj `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Wspólne fixtures są w [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekwencyjna weryfikacja na żywo

Wywołania na żywo są odrębne od testów jednostkowych. Używaj poniższych poleceń **pojedynczo**, z katalogu głównego repozytorium, dopiero gdy poświadczenia i dostęp do wdrożenia są gotowe. Nie są potrzebne usługi ani procesy trwałe.

Dla współdzielonego wdrożenia **10 zapytań/minutę** zarezerwuj wystarczającą kwotę na cały następny program przed jego uruchomieniem: 5, 4, 1, potem 6 zapytań. Same procesy sekwencyjne nie gwarantują przestrzegania limitu zapytań. Koordynuj minuty kołowe ze wszystkimi innymi wywołującymi; nie wklejaj czterech wywołań jako niekontrolowanej partii.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Uzupełnienia, wieloturystyczne i dwie tury interaktywne:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Sprawdź wszystkie trzy nagłówki sekcji, pięć odpowiedzi, ostateczną interaktywną odpowiedź przypominającą Adę, `Do widzenia!` oraz kod wyjścia 0. Budżet: **5 żądań, maksymalnie 1900 tokenów odpowiedzi**. Dla mniejszego przebiegu wywołaj tylko `exit`: 3 żądania / 900 tokenów, ale to nie testuje interaktywnego wnioskowania.

**2. Oba workflowy wywoływania funkcji:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Sprawdź oba nazwy funkcji, symulowaną pogodę w Seattle, wyliczony wynik 36, dwie ostateczne odpowiedzi i kod wyjścia 0. Budżet: **4 żądania, maksymalnie 1200 tokenów odpowiedzi**.

**3. Odpowiedź oparta na dokumencie:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Sprawdź ścieżkę dokumentu, odpowiedź wspominającą Microsoft Entra ID oraz kod wyjścia 0. Budżet: **1 żądanie, maksymalnie 500 tokenów odpowiedzi**. Istniejący plik [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) jest jedynym wymaganym plikiem wejściowym. Opcjonalne drugie uruchomienie z pytaniem o nieobecny temat powinno powstrzymać się i dodaje jedno żądanie / 500 tokenów.

**4. Obserwacje dotyczące Odpowiedzialnej Sztucznej Inteligencji:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Sprawdź sześć kategorii i podsumowanie obserwacji, przejrzyj wygenerowaną zawartość i wymagaj kodu wyjścia 0 dla technicznego zakończenia. Pomyślne zakończenie procesu nie potwierdza bezpieczeństwa modelu. Budżet: **6 żądań, maksymalnie 1800 tokenów odpowiedzi**.

**Razem dla czterech poleceń: 16 zapytań do czatu i maksymalnie 5400 tokenów odpowiedzi**, plus tokeny wejściowe (w tym powtarzająca się rozmowa i schemat/narracja narzędzia). Nie ma żadnych żądań embeddingów. Rzeczywiste zużycie tokenów zależy od modelu i może być mniejsze, zwłaszcza przy filtrowanych promptach. Koszt w dolarach zależy od wyceny wdrożenia; nie podano stałej wyceny pieniężnej. Wszystkie limity żądań zakładają brak ręcznych ponownych uruchomień. Sprawdź `$LASTEXITCODE` natychmiast po każdym poleceniu; niezerowa wartość oznacza niepowodzenie wykonania.

## Rozwiązywanie problemów

- **Brak punktu końcowego / 401 / 403:** Ustaw punkt końcowy w procesie uruchamiania, zweryfikuj lokalne zalogowanie Azure i rolę ograniczoną do zasobu, oraz sprawdź, czy nie ma niezamierzonych nadpisów środowiska tożsamości.
- **400 / 404:** Potwierdź, że wdrożenie istnieje i obsługuje Chat Completions z wysiłkiem rozumowania `none`. Używaj HTTPS jako root zasobu lub adresu `/openai/v1`, a nie adresu wdrożenia legacy. Zwykłe błędy 400 to usterki techniczne, a nie blokady bezpieczeństwa.
- **429:** Skonsultuj współdzielony RPM i limit tokenów przed ponowną próbą. Przykłady celowo nie retryują automatycznie.
- **`Niepełna odpowiedź czatu: długość`:** Wyjście osiągnęło limit długości. Przejrzyj odpowiedź i prompt przed zwiększeniem limitu i jego dokumentowanego budżetu; nie zapisuj skróconego przebiegu jako pomyślnego.
- **Błędy pliku lub stdin:** Uruchom z obsługiwanego katalogu lub podaj wyraźną ścieżkę do dokumentu. Podaj niepustą treść pytania. Ukończenia mogą zakończyć się normalnie na EOF lub `exit`.
- **Błędy kompilacji:** Zweryfikuj Java 21 lub nowszą, następnie uruchom `mvn -B -ntp clean test`. W PowerShellu zacytuj cały argument Mavena zawierający kropkowaną właściwość, np. `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Kolejne kroki

Kontynuuj do [Rozdział 4: Praktyczne przykłady](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym należy uznawać za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->