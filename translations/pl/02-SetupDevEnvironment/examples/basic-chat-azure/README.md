# Podstawowy czat z Azure AI Foundry - przykład end-to-end

Ten przykład to prosta aplikacja Spring Boot, która łączy się z modelem **Azure AI Foundry** używając **uwierzytelniania bezkluczowego** (Microsoft Entra ID) i testuje Twoją konfigurację. Używa `ChatClient` z Spring AI, opartego na **oficjalnym OpenAI Java SDK** oraz punkcie końcowym **Azure OpenAI v1**.

Wersje w [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) to Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6** oraz dotenv-java **3.2.0**. Przykład używa `spring-ai-starter-model-openai` i jawnie deklaruje `openai-java` oraz `azure-identity`; Spring AI 2 usunął stary starter Azure OpenAI.

## Spis treści

- [Wymagania wstępne](#wymagania-wstępne)
- [Szybki start](#szybki-start)
- [Jak działa uwierzytelnianie](#jak-działa-uwierzytelnianie)
- [Uruchamianie aplikacji](#uruchamianie-aplikacji)
  - [Użycie Maven](#użycie-maven)
  - [Użycie VS Code](#użycie-vs-code)
  - [Oczekiwany wynik](#oczekiwany-wynik)
- [Referencje konfiguracji](#referencje-konfiguracji)
  - [Zmienne środowiskowe](#zmienne-środowiskowe)
  - [Konfiguracja Spring](#konfiguracja-spring)
- [Rozwiązywanie problemów](#rozwiązywanie-problemów)
  - [Typowe problemy](#typowe-problemy)
  - [Tryb debugowania](#tryb-debugowania)
- [Kolejne kroki](#kolejne-kroki)
- [Zasoby](#zasoby)

## Wymagania wstępne

Przed uruchomieniem tego przykładu upewnij się, że posiadasz:

- Zasób Azure AI Foundry z wdrożeniem `gpt-5.6-luna` - utwórz go poleceniem `azd up` lub ręcznie za pomocą [przewodnika konfiguracji Azure AI Foundry](../../getting-started-azure-openai.md)
- Rolę **Cognitive Services OpenAI User** na tym zasobie (szablony Bicep przypisują ją automatycznie)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), zalogowany przez `az login`
- Java 21+ i Maven 3.9+

> **Nie wymaga klucza API** — uwierzytelnianie odbywa się bezkluczowo przez Microsoft Entra ID.

## Szybki start

```bash
# 1. Przejdź do projektu
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Zaloguj się, aby uwierzytelnianie bezkluczowe mogło uzyskać token
az login

# 3. Skonfiguruj punkt końcowy
#    - Jeśli uruchomiłeś `azd up`, plik .env został dla Ciebie utworzony (pomiń ten krok).
#    - W przeciwnym razie skopiuj szablon i ustaw AZURE_OPENAI_ENDPOINT:
cp .env.example .env

# 4. Uruchom aplikację
mvn spring-boot:run
```

## Jak działa uwierzytelnianie

Ten przykład uwierzytelnia się za pomocą **Microsoft Entra ID** — nie ma klucza API.

Aplikacja konfiguruje uwierzytelnianie jawnie w pliku [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java):

1. `azureCredential()` tworzy `BearerTokenCredential` używając `AuthenticationUtil.getBearerTokenSupplier` z `DefaultAzureCredential` i zakresem `https://ai.azure.com/.default`.
2. `azureOpenAiClient()` buduje `OpenAIClient` za pomocą `OpenAIOkHttpClient.builder()`, ustawia punkt końcowy zasobu na `/openai/v1` oraz dostarcza poświadczenia tokenu za pomocą `.credential(...)`.
3. `azureChatModel()` przekazuje tego klienta do modelu Spring AI `OpenAiChatModel`, który używa `ChatClient` lekcji.

Te jawne beany zapobiegają nadpisaniu uwierzytelniania Azure przez globalną zmienną `OPENAI_API_KEY`. Brak klucza API w YAML sam w sobie nie tworzy uwierzytelniania. `DefaultAzureCredential` może używać lokalnej sesji `az login` lub tożsamości zarządzanej w Azure; wybrana tożsamość musi posiadać powyższą rolę zasobu.

## Uruchamianie aplikacji

### Użycie Maven

```bash
mvn spring-boot:run
```

### Użycie VS Code

1. Otwórz projekt w VS Code
2. Naciśnij `F5` lub użyj panelu „Uruchom i debuguj”
3. Wybierz konfigurację „Spring Boot-BasicChatApplication”

> **Uwaga**: Aplikacja ładuje `.env` z katalogu roboczego, także podczas uruchamiania z VS Code.

### Oczekiwany wynik

Przykładowe wyjście po udanym uruchomieniu (pominięto logi uruchomienia; treść odpowiedzi może się różnić):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Referencje konfiguracji

### Zmienne środowiskowe

| Zmienna | Opis | Wymagana | Przykład |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Punkt końcowy Foundry (Azure OpenAI) | Tak | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Nazwa wdrożenia modelu czatu | Nie | `gpt-5.6-luna` (domyślna) |

> Nie ma zmiennej klucza API — uwierzytelnianie jest bezkluczowe (Microsoft Entra ID przez `az login`).

### Konfiguracja Spring

Ustawienia w [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) używają prefiksu `spring.ai.openai` i spłaszczonych właściwości czatu (bez bloku `options`):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` to **nazwa wdrożenia Azure**. Uwierzytelnianie pochodzi z jawnych beanów opisanych powyżej, a nie z ustawienia `api-key`. Lekcja wyłącza rozumowanie i ogranicza tokeny odpowiedzi do 500; pozostawia `temperature` i starsze `max-tokens` nieustawione.

Microsoft zaleca [oficjalne OpenAI SDK z Azure OpenAI v1 i API Responses do nowych aplikacji](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). Chat Completions nadal jest wspierane dla tej istniejącej lekcji opartej na wiadomościach. Dla GPT-5.6 żądania z narzędziami w Chat Completions muszą ustawić `reasoning_effort` na `none`; przy łączeniu rozumowania i narzędzi należy używać Responses. Zobacz [wywoływanie narzędzi z modelami rozumującymi](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Rozwiązywanie problemów

### Typowe problemy

<details>
<summary><strong>Błąd: 401 / "PermissionDenied" / błędy tokenu</strong></summary>

- Uruchom `az login` — uwierzytelnianie bezkluczowe wymaga aktywnego zalogowania, aby uzyskać token
- Sprawdź, czy Twoje konto ma rolę **Cognitive Services OpenAI User** na zasobie
- Jeśli właśnie przypisałeś rolę, odczekaj chwilę na propagację
- Potwierdź, że jesteś w odpowiednim dzierżawcy/subskrypcji (`az account show`)
</details>

<details>
<summary><strong>Błąd: "The endpoint is not valid" / błędy połączenia</strong></summary>

- Upewnij się, że `AZURE_OPENAI_ENDPOINT` to pełny adres URL bazy (np. `https://your-resource.openai.azure.com/`)
- Sprawdź spójność ukośników końcowych
- Zweryfikuj, czy punkt końcowy odpowiada Twojemu zasobowi (`azd env get-values`)
</details>

<details>
<summary><strong>Błąd: "The deployment was not found"</strong></summary>

- Sprawdź, czy `AZURE_OPENAI_DEPLOYMENT` odpowiada nazwie wdrożenia w Azure
- Upewnij się, że model jest poprawnie wdrożony i aktywny
- Domyślna nazwa wdrożenia to `gpt-5.6-luna`
</details>

<details>
<summary><strong>Błąd: 429 / przekroczono limit zapytań</strong></summary>

- Domyślne wdrożenie GPT-5.6 Luna ma globalną standardową pojemność 10: 10 żądań/minutę i 10 000 tokenów/minutę
- Uruchamiaj przykłady sekwencyjnie i odczekaj okres ponawiania usługi przed kolejną próbą
- Ten podstawowy przykład wyłącza automatyczne ponawianie w SDK, więc błąd zostanie zgłoszony bezpośrednio
</details>

<details>
<summary><strong>VS Code: zmienne środowiskowe się nie ładują</strong></summary>

- Upewnij się, że plik `.env` znajduje się w katalogu głównym projektu (na tym samym poziomie co `pom.xml`)
- Spróbuj uruchomić `mvn spring-boot:run` w zintegrowanym terminalu VS Code
- Sprawdź, czy rozszerzenie Java dla VS Code jest poprawnie zainstalowane
</details>

### Tryb debugowania

Aby włączyć szczegółowe logowanie, odkomentuj te linie w pliku [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml):

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Kolejne kroki

**Konfiguracja zakończona!** Kontynuuj swoją naukę:

[Rozdział 3: Podstawowe techniki Generatywnej AI](../../../03-CoreGenerativeAITechniques/README.md)

## Zasoby

- [Przejście Spring AI 2 do OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Oficjalne OpenAI Java SDK z Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Uwierzytelnianie bezkluczowe z Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Portal Azure AI Foundry](https://ai.azure.com/)
- [Dokumentacja Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Zastrzeżenie**:
Niniejszy dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Choć dążymy do dokładności, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub niedokładności. Oryginalny dokument w jego języku źródłowym należy uznawać za autorytatywne źródło. W przypadku informacji krytycznych zalecane jest skorzystanie z profesjonalnego tłumaczenia wykonanego przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->