# Foundry Local Spring Boot Tutorial

Uruchom mały model językowy na własnym komputerze i wywołuj jego kompatybilny z OpenAI
punkt końcowy REST z aplikacji konsolowej w Javie. Nie używa się wdrożenia w Azure, logowania Azure,
klucza API w chmurze ani inferencji w chmurze. **GPT-5.6 Luna jest dostępny wyłącznie w Azure;
nie konfiguruj go jako modelu Foundry Local.**

## Wersje i wymagania wstępne

| Komponent | Wersja |
| --- | --- |
| Java | 21 lub nowsza |
| Maven | 3.6.3 lub nowsza |
| Spring Boot | 4.1.1 |
| OpenAI Java SDK | 4.63.1 |
| Foundry Local SDK (lokalny serwer REST) | 2.0.1 |
| Node.js (lokalny serwer REST) | 20 lub nowsza |
| Foundry Local CLI (opcjonalnie, osobne wydanie) | 0.10.3 preview |

Spring Boot zarządza wersjami Spring Framework, Jackson, JUnit oraz wtyczki Maven.
Ten przykład używa bezpośrednio OpenAI Java SDK, a nie Spring AI. Stary, nieużywany
punkt kontrolny Spring AI oraz repozytorium zostały usunięte.

Zalecanym modelem startowym jest **Qwen 2.5 0.5B**, wariant CPU
`qwen2.5-0.5b-instruct-generic-cpu:4` (około 822 MB w katalogu).
Unika to konieczności korzystania z dostawców wykonania GPU. Inne obsługiwane, zbuforowane
małe modele można wybrać jawnie. Instalacja modelu i środowiska wykonawczego wymaga dostępu do sieci;
zapytania i inferencja pozostają lokalne. Foundry Local może mimo to emitować minimalne
diagnostyki środowiska wykonawczego nawet przy wyłączonej nieistotnej telemetrii.

Uruchom poniższe polecenia z tego katalogu przykładowego.

## Budowa i testowanie w Javie

```powershell
mvn clean verify
```

Testy kontraktu HTTP uruchamiają ulotny serwer loopback i sprawdzają faktyczny
OpenAI Java SDK. Obejmują serializację zapytań, wykrywanie modeli, jawny wybór modelu,
niejednoznaczne lub nieprawidłowe listy modeli, błędy HTTP, puste odpowiedzi,
adresy URL ograniczone do lokalnego użytku oraz propagację błędów w wierszu poleceń. Nie wymagają modelu ani
dostępu do sieci poza instalacją zależności Maven. Test na żywo jest opcjonalny.

## Uruchom lokalny model

### Zalecane: przypięty serwer SDK

Nie istnieje natywne Foundry Local Java SDK. Mały pomocnik Node.js udostępnia
oficjalny serwer REST SDK; aplikacja i żądanie czatu pozostają w Javie.

Zainstaluj przypięte zależności środowiska wykonawczego:

```powershell
npm ci
```

Jeśli Windows x64 nie może uzyskać dostępu do NuGet podczas natywnej instalacji SDK, użyj dostarczonego
zapasowego rozwiązania. Pobiera ono pasujący oficjalny archiwum środowiska wykonawczego z GitHub, sprawdza
sha-256 wydania i umieszcza jego DLL obok natywnego dodatku. Nie wyłącza
walidacji TLS, nie wymaga uprawnień administratora ani nie modyfikuje kodu SDK.

```powershell
npm ci --ignore-scripts
pwsh -File ./scripts/install-foundry-runtime.ps1
```

Wyświetl modele już buforowane na tej maszynie:

```powershell
npm run start:foundry -- --list
```

Przy pierwszym uruchomieniu jawnie zezwól na pobranie małego modelu CPU:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --download --port 5273
```

Przy kolejnych uruchomieniach pomiń `--download`, co wymusi użycie modelu z cache:

```powershell
npm run start:foundry -- --model qwen2.5-0.5b-instruct-generic-cpu:4 --port 5273
```

Pomocnik preferuje dopasowany model z cache, akceptuje alias lub dokładny identyfikator wariantu,
i odmawia modelu nieobecnego, chyba że podano `--download`. Rejestruje tylko
wykonawcę wybranego modelu, jeśli jest wymagany. Zbuforowane warianty GPU
wciąż mogą wymagać kompatybilnych pakietów i sterowników wykonawczych.

Jeśli port 5273 jest zajęty, podaj `--port 0` dla dostępnego portu. Pomocnik wyświetla
`FOUNDRY_LOCAL_BASE_URL`, dokładny identyfikator modelu `FOUNDRY_LOCAL_MODEL` oraz jego PID, gdy jest gotowy.
Użyj wyświetlonego punktu końcowego w Javie. Pozostaw ten terminal otwarty podczas działania Javy;
**Ctrl+C** zatrzymuje serwer REST i zwalnia model.

Domyślny cache to `~/.foundry/cache/models`. Ustaw `FOUNDRY_LOCAL_CACHE_DIR` dla
innego istniejącego cache. Logi i stan pomocnika są zapisywane pod katalogiem
`target/foundry-local` w tym przykładzie. Zatrzymaj pomocnika przed uruchomieniem `mvn clean`.

### Opcjonalnie: Foundry Local CLI

CLI i SDK mają niezależne wydania: CLI **0.10.3** zawiera SDK **1.2.4**;
powyższy pomocnik korzysta z SDK **2.0.1**. Instalacja najnowszego CLI nie instaluje
najnowszego językowego SDK. Zobacz [uwagi do wydania CLI](https://github.com/microsoft/Foundry-Local/releases/tag/cli-preview-0.10.3).

Na Windows użyj polecenia instalacji dla użytkownika, jeśli CLI nie jest zainstalowane:

```powershell
winget install --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
```

Lub uaktualnij istniejącą instalację:

```powershell
winget upgrade --id Microsoft.FoundryLocal --exact --source winget --scope user --silent --accept-package-agreements --accept-source-agreements --disable-interactivity
foundry --version
```

CLI 0.10.x zastępuje stare polecenia `foundry service` poleceniem `foundry server`:

```powershell
foundry server start --port 5273
foundry cache list
foundry model load qwen2.5-0.5b-instruct-generic-cpu:4
foundry server status --output json
```

`model load` wymaga już pobranego modelu. Sprawdź polecenia pobierania w `foundry model --help`.
Użyj rzeczywistego punktu końcowego z wyjścia statusu; w przeciwnym razie CLI
domyślnie używa automatycznie przydzielonego portu. Nie uruchamiaj CLI i pomocnika SDK
na tym samym porcie. Po zakończeniu:

```powershell
foundry server stop
```

## Uruchom aplikację Java

W drugim terminalu ustaw punkt końcowy i dokładny identyfikator modelu wyświetlone przez serwer:

```powershell
$env:FOUNDRY_LOCAL_BASE_URL = "http://127.0.0.1:5273/v1"
$env:FOUNDRY_LOCAL_MODEL = "qwen2.5-0.5b-instruct-generic-cpu:4"
mvn spring-boot:run
```

Lub uruchom zapakowaną aplikację:

```powershell
java -jar target/foundry-local-spring-boot-0.0.1-SNAPSHOT.jar
```

Jedynym punktem wejścia w Javie jest `com.example.Application`. Wyświetla wybrany
punkt końcowy, rzeczywisty identyfikator modelu, zapytanie i wygenerowaną odpowiedź, a następnie
zamyka kontekst Spring i klienta HTTP. Nieudana inferencja lub brak tekstu odpowiedzi powodują
wyjście z błędem zamiast zastępczego wyniku sukcesu.

### Konfiguracja

| Zmienna środowiskowa | Domyślna wartość | Cel |
| --- | --- | --- |
| `FOUNDRY_LOCAL_BASE_URL` | `http://127.0.0.1:5273/v1` | Punkt końcowy HTTP loopback, włącznie z `/v1` |
| `FOUNDRY_LOCAL_MODEL` | Puste | Dokładny identyfikator modelu; w przeciwnym razie wybierz jeden reklamowany model |
| `FOUNDRY_LOCAL_PROMPT` | Jednozdaniowe pytanie o lokalne modele | Zapytanie wysyłane przez konsolę |

Równoważne argumenty Spring to `--foundry.local.base-url=...`,
`--foundry.local.model=...` oraz `--foundry.local.prompt=...`.
Akceptowane są tylko punkty końcowe HTTP loopback. Odrzucane są zdalne/punkt końcowy w chmurze,
wbudowane poświadczenia, ciągi zapytań oraz ścieżki bez `/v1`.

Puste ustawienie modelu działa tylko, jeśli `/v1/models` reklamuje dokładnie jeden model.
Model reklamowany niekoniecznie jest załadowany. Jeśli reklamowanych jest wiele modeli,
ustaw dokładny załadowany identyfikator zamiast polegać na kolejności w katalogu.

Zapytania używają `temperature=0`, limitu wynikowego 150 tokenów, limitu czasu 120 sekund oraz
braku automatycznych ponowień. Pole `max_tokens` jest zamierzone: jest
obsługiwane przez kontrakt Foundry Local REST, mimo że OpenAI Java deprecjonuje
to pole dla nowszych modeli chmurowych. Tożsamość modelu pochodzi z konfiguracji lub
wykrywania, a nie z deklaracji modelu o sobie samym.

## Walidacja na żywo

Gdy lokalny serwer działa, uruchom wszystkie testy łącznie z opcjonalnym testem na żywo.
Zamień port punktu końcowego na wartość wyświetloną przez Twój serwer. Cytuj właściwości
Maven z kropkami w PowerShell:

```powershell
mvn "-Dfoundry.local.live=true" "-Dfoundry.local.base-url=http://127.0.0.1:5273/v1" "-Dfoundry.local.model=qwen2.5-0.5b-instruct-generic-cpu:4" verify
```

Test na żywo wywołuje `Application.main`, podaje fakt "Stolicą Francji jest Paryż,"
zadaje pytanie o miasto i sprawdza, czy faktycznie wygenerowany tekst to
`Paryż`. Sprawdza wynik semantyczny, nie tylko status HTTP sukcesu.

To jest kontrola integracyjna, a nie benchmark dokładności. Podczas walidacji,
model 0.5B udzielił na oddzielne pytanie "2 + 2" odpowiedzi `3` zarówno przez Javę, jak i bezpośrednio
REST. Nie polegaj na nim w zakresie arytmetyki czy dokładności faktów bez niezależnej
weryfikacji; do obliczeń używaj narzędzi deterministycznych.

## Rozwiązywanie problemów

| Objaw | Sprawdź |
| --- | --- |
| Połączenie odrzucone | Poczekaj na komunikat o gotowości; użyj wydrukowanego portu i ścieżki `/v1`. |
| Reklamowane wiele modeli | Ustaw `FOUNDRY_LOCAL_MODEL` na dokładny identyfikator załadowanego modelu. |
| Model niedostępny | Użyj `--list` lub jawnie zezwól na pobranie przez `--download`. |
| Dostawca GPU zawodzi lub się zawiesza | Użyj małego modelu CPU. Buforowany model GPU wciąż wymaga dostawcy. |
| CLI pozostaje w stanie `initializing` | Przeczytaj `foundry server logs --lines 80`; zatrzymaj demona i użyj pomocnika SDK. |
| Problem z TLS/download NuGet | Napraw dostęp do sieci lub użyj zweryfikowanego zapasowego rozwiązania Windows x64 powyżej. Nie wyłączaj TLS. |
| Port zajęty | Użyj `--port 0` i skonfiguruj Javę przy użyciu wydrukowanego punktu końcowego. |
| Brak wyborów lub pusty tekst | Aplikacja celowo kończy się niepowodzeniem; sprawdź logi modelu i środowiska wykonawczego. |

## Źródła i odniesienia

- [Application.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/Application.java): jednorazowy runner Spring Boot.
- [FoundryLocalService.java](../../../../04-PracticalSamples/foundrylocal/src/main/java/com/example/FoundryLocalService.java): typowane wykrywanie i lokalne uzupełnianie czatu.
- [FoundryLocalServiceTest.java](../../../../04-PracticalSamples/foundrylocal/src/test/java/com/example/FoundryLocalServiceTest.java): kontrakt HTTP, runner i testy na żywo.
- [start-foundry.mjs](../../../../04-PracticalSamples/foundrylocal/scripts/start-foundry.mjs): oficjalny serwer REST SDK z wyborem modelu z cache i sprzątaniem.
- [install-foundry-runtime.ps1](../../../../04-PracticalSamples/foundrylocal/scripts/install-foundry-runtime.ps1): zweryfikowane zapasowe środowisko natywne Windows x64.
- [application.properties](../../../../04-PracticalSamples/foundrylocal/src/main/resources/application.properties), [pom.xml](../../../../04-PracticalSamples/foundrylocal/pom.xml) oraz [package.json](../../../../04-PracticalSamples/foundrylocal/package.json): konfiguracja i zależności.
- [Integracja Foundry Local REST](https://learn.microsoft.com/azure/foundry-local/how-to/how-to-integrate-with-inference-sdks).
- [Wydanie Foundry Local 2.0.1 i uwagi o migracji](https://github.com/microsoft/Foundry-Local/releases/tag/v2.0.1).
- [Rozdział 04: Praktyczne przykłady](../README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym należy uznawać za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->