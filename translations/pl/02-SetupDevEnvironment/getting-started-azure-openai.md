# Konfiguracja środowiska programistycznego dla Azure AI Foundry

> Ten przewodnik konfiguruje modele **Azure AI Foundry** dla aplikacji AI w Javie w tym kursie, korzystając z uwierzytelniania **bezkluczowego** (Microsoft Entra ID) — bez konieczności zarządzania kluczami API. Nowy w narzędziach? Zacznij od [przewodnika po środowisku programistycznym](./README.md).

Ten przewodnik konfiguruje modele **Azure AI Foundry** dla aplikacji AI w Javie w tym kursie. Masz dwie ścieżki:

- **Opcja A — Provisioning za pomocą `azd` + Bicep (zalecane):** jedno polecenie wdraża konto Foundry i modele jako kod. Bez klikania w portalu.
- **Opcja B — Tworzenie zasobów ręcznie** w portalu Azure AI Foundry.

Obie ścieżki korzystają z **uwierzytelniania bezkluczowego** (Microsoft Entra ID) — nie trzeba kopiować ani ujawniać kluczy API.

## Spis treści

- [Co zostanie utworzone](#co-zostanie-utworzone)
- [Wymagania wstępne](#wymagania-wstępne)
- [Opcja A: Provisioning z azd + Bicep (zalecane)](#option-a-provision-with-azd--bicep-recommended)
- [Opcja B: Tworzenie zasobów ręcznie](#opcja-b-tworzenie-zasobów-ręcznie)
- [Konfiguracja środowiska](#konfiguracja-środowiska)
- [Testowanie konfiguracji](#testowanie-konfiguracji)
- [Co dalej?](#co-dalej)
- [Zasoby](#zasoby)
- [Dodatkowe zasoby](#dodatkowe-zasoby)

## Co zostanie utworzone

Szablony Bicep w [`infra/`](../../../02-SetupDevEnvironment/infra) provisionują:

- Konto **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, typ `AIServices`) z projektem
- Wdrożenie **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), wersja `2026-07-09`, z pojemnością `GlobalStandard` `10` (10 żądań/minutę i 10 000 tokenów/minutę dla tego modelu)
- Wdrożenie **embedding** - `text-embedding-3-small`, wersja `1` (używane w późniejszych rozdziałach)
- Przydział roli **bezkluczowej** (`Cognitive Services OpenAI User`), abyś logował się poprzez `az login` zamiast zarządzać kluczami

## Wymagania wstępne

- [Subskrypcja Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) oraz [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opcja A: Provisioning z azd + Bicep (zalecane)

Z folderu `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Zaloguj się (oba narzędzia)
azd auth login
az login

# Zapewnij konto Foundry + wdrożenia modeli
azd up
```

`azd` poprosi o **nazwę środowiska** (np. `genai-java`), **subskrypcję** i **region**. Wybierz własną subskrypcję oraz region, w którym dostępne są `gpt-5.6-luna` i `text-embedding-3-small`, np. `eastus2`. Potwierdź, że subskrypcja ma wystarczającą kwotę dla modelu i typu wdrożenia w tym regionie; dostępność i kwoty różnią się w zależności od subskrypcji.

Po zakończeniu provisioning, azd:

1. Wdraża wszystko zdefiniowane w [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Uruchamia hak post-provisioningowy, który zapisuje [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) z twoim endpointem i nazwami wdrożeń (bez sekretów).

> **Wskazówka:** Uruchom `azd up` w dowolnym momencie, aby zastosować zmiany. Uruchom `azd down`, aby usunąć wszystko i przestać generować koszty.

Aby zobaczyć wygenerowane ustawienia:

```bash
azd env get-values
```

Teraz przejdź do [Testowania konfiguracji](#testowanie-konfiguracji).

## Opcja B: Tworzenie zasobów ręcznie

Wolisz portal? Utwórz zasoby ręcznie:

1. Przejdź do [portalu Azure AI Foundry](https://ai.azure.com/) i zaloguj się.
2. **Utwórz projekt** (to również tworzy zasób AI Foundry). Nadaj mu nazwę, np. `GenAIJava`.
3. W projekcie otwórz **Modele + punkty końcowe** → **Wdroż model** → **Wdroż podstawowy model**.
4. Wdróż **GPT-5.6 Luna** (nazwa modelu i wdrożenia `gpt-5.6-luna`, wersja `2026-07-09`) z pojemnością **Global Standard** `10`. Powtórz dla **text-embedding-3-small**, wersja `1`, jeśli chcesz przykłady embeddingów.
5. Z widoku **Przegląd** skopiuj **endpoint** (np. `https://<resource>.openai.azure.com/`).
6. Przyznaj sobie dostęp bezkluczowy: na zasobie otwórz **Kontrola dostępu (IAM)** → **Dodaj przypisanie roli** → przypisz swoją rolę **Cognitive Services OpenAI User**.

> **Wciąż masz problemy?** Zobacz [dokumentację Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfiguracja środowiska

**Jeśli użyłeś opcji A (`azd up`)**, plik ustawień jest już zapisany — nic nie trzeba konfigurować. Przejdź do [Testowania konfiguracji](#testowanie-konfiguracji).

**Jeśli użyłeś opcji B (ręczne), utwórz samodzielnie plik `.env` przykładu:**

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Edytuj `.env` z twoim endpointem (bez klucza — uwierzytelnianie bezkluczowe):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Użyj endpointu Azure OpenAI zasobu, a nie adresu URL projektu. Aplikacja basic-chat konwertuje go na `/openai/v1` i konfiguruje klienta z wyraźnym tokenem typu bearer; klucz API nie jest wymagany.

> **Uwaga bezpieczeństwa:** Nie ma klucza API do przechowywania. Uwierzytelniasz się za pomocą Microsoft Entra ID przez `az login` (lokalnie) lub tożsamość zarządzaną (w Azure). Plik `.env` zawiera tylko ustawienia nienaznaczone jako sekretne i jest już uwzględniony w `.gitignore`.

## Testowanie konfiguracji

Upewnij się, że jesteś zalogowany, aby uwierzytelnianie bezkluczowe mogło pobrać token, następnie uruchom przykład:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # jeśli nie jesteś już zalogowany
mvn clean spring-boot:run
```

Powinieneś zobaczyć odpowiedź z modelu `gpt-5.6-luna`. Uruchamiaj przykłady kolejno, aby pozostać w małej domyślnej kwocie; jeśli pojawi się HTTP 429, poczekaj na odstęp czasu ponowienia przed ponowną próbą.

> **Użytkownicy VS Code:** Naciśnij `F5`, aby uruchomić. Aplikacja automatycznie ładuje twój plik `.env`.

> **Pełny przykład:** Zobacz [Przykład Basic Chat z Azure AI Foundry](./examples/basic-chat-azure/README.md) dla szczegółów i rozwiązywania problemów.

## Co dalej?

Po provisioning i pomyślnym uruchomieniu przykładu będziesz miał:
- Azure AI Foundry z wdrożonymi `gpt-5.6-luna` i `text-embedding-3-small`
- Uwierzytelnianie bezkluczowe (Microsoft Entra ID) – bez kluczy do zarządzania
- Lokalny plik `.env` z twoim endpointem i nazwami wdrożeń
- Gotowe środowisko programistyczne w Javie

**Przejdź do** [Rozdział 3: Podstawowe techniki generatywnej SI](../03-CoreGenerativeAITechniques/README.md), aby zacząć tworzyć aplikacje AI!

## Zasoby

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Uwierzytelnianie bezkluczowe z Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentacja Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Przejście Spring AI 2 do OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficjalne OpenAI Java SDK z Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Dodatkowe zasoby

- [Pobierz VS Code](https://code.visualstudio.com/Download)
- [Pobierz Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfiguracja kontenera deweloperskiego](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym należy uznawać za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->