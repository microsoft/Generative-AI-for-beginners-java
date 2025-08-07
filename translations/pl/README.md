<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:11:25+00:00",
  "source_file": "README.md",
  "language_code": "pl"
}
-->
# Generatywna AI dla Początkujących - Edycja Java
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generatywna AI dla Początkujących - Edycja Java](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.pl.png)

**Czas trwania**: Cały warsztat można ukończyć online bez potrzeby lokalnej konfiguracji. Przygotowanie środowiska zajmuje 2 minuty, a eksploracja przykładów od 1 do 3 godzin, w zależności od głębokości analizy.

> **Szybki Start**

1. Sforkuj to repozytorium na swoje konto GitHub
2. Kliknij **Code** → zakładka **Codespaces** → **...** → **New with options...**
3. Użyj domyślnych ustawień – zostanie wybrany kontener deweloperski stworzony dla tego kursu
4. Kliknij **Create codespace**
5. Poczekaj około 2 minut na przygotowanie środowiska
6. Przejdź bezpośrednio do [Pierwszego przykładu](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Obsługa Wielu Języków

### Obsługiwane przez GitHub Action (Automatyczne i Zawsze Aktualne)

[Francuski](../fr/README.md) | [Hiszpański](../es/README.md) | [Niemiecki](../de/README.md) | [Rosyjski](../ru/README.md) | [Arabski](../ar/README.md) | [Perski (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chiński (Uproszczony)](../zh/README.md) | [Chiński (Tradycyjny, Makau)](../mo/README.md) | [Chiński (Tradycyjny, Hongkong)](../hk/README.md) | [Chiński (Tradycyjny, Tajwan)](../tw/README.md) | [Japoński](../ja/README.md) | [Koreański](../ko/README.md) | [Hindi](../hi/README.md) | [Bengalski](../bn/README.md) | [Marathi](../mr/README.md) | [Nepalski](../ne/README.md) | [Pendżabski (Gurmukhi)](../pa/README.md) | [Portugalski (Portugalia)](../pt/README.md) | [Portugalski (Brazylia)](../br/README.md) | [Włoski](../it/README.md) | [Polski](./README.md) | [Turecki](../tr/README.md) | [Grecki](../el/README.md) | [Tajski](../th/README.md) | [Szwedzki](../sv/README.md) | [Duński](../da/README.md) | [Norweski](../no/README.md) | [Fiński](../fi/README.md) | [Holenderski](../nl/README.md) | [Hebrajski](../he/README.md) | [Wietnamski](../vi/README.md) | [Indonezyjski](../id/README.md) | [Malajski](../ms/README.md) | [Tagalog (Filipiński)](../tl/README.md) | [Suahili](../sw/README.md) | [Węgierski](../hu/README.md) | [Czeski](../cs/README.md) | [Słowacki](../sk/README.md) | [Rumuński](../ro/README.md) | [Bułgarski](../bg/README.md) | [Serbski (Cyrylica)](../sr/README.md) | [Chorwacki](../hr/README.md) | [Słoweński](../sl/README.md) | [Ukraiński](../uk/README.md) | [Birmański (Myanmar)](../my/README.md)

## Struktura Kursu i Ścieżka Nauki

### **Rozdział 1: Wprowadzenie do Generatywnej AI**
- **Podstawowe Pojęcia**: Zrozumienie dużych modeli językowych, tokenów, osadzeń i możliwości AI
- **Ekosystem AI w Javie**: Przegląd Spring AI i OpenAI SDK
- **Protokół Kontekstu Modelu**: Wprowadzenie do MCP i jego roli w komunikacji agentów AI
- **Praktyczne Zastosowania**: Scenariusze z życia wzięte, w tym chatboty i generowanie treści
- **[→ Rozpocznij Rozdział 1](./01-IntroToGenAI/README.md)**

### **Rozdział 2: Konfiguracja Środowiska Deweloperskiego**
- **Konfiguracja Wielu Dostawców**: Integracja GitHub Models, Azure OpenAI i OpenAI Java SDK
- **Spring Boot + Spring AI**: Najlepsze praktyki w tworzeniu aplikacji AI dla przedsiębiorstw
- **GitHub Models**: Darmowy dostęp do modeli AI do prototypowania i nauki (bez potrzeby karty kredytowej)
- **Narzędzia Deweloperskie**: Konfiguracja kontenerów Dockera, VS Code i GitHub Codespaces
- **[→ Rozpocznij Rozdział 2](./02-SetupDevEnvironment/README.md)**

### **Rozdział 3: Kluczowe Techniki Generatywnej AI**
- **Inżynieria Podpowiedzi**: Techniki optymalizacji odpowiedzi modeli AI
- **Osadzenia i Operacje na Wektorach**: Implementacja wyszukiwania semantycznego i dopasowywania podobieństw
- **Generowanie Wspomagane Pobieraniem (RAG)**: Łączenie AI z własnymi źródłami danych
- **Wywoływanie Funkcji**: Rozszerzanie możliwości AI za pomocą niestandardowych narzędzi i wtyczek
- **[→ Rozpocznij Rozdział 3](./03-CoreGenerativeAITechniques/README.md)**

### **Rozdział 4: Praktyczne Zastosowania i Projekty**
- **Generator Historii o Zwierzętach** (`petstory/`): Tworzenie kreatywnych treści z GitHub Models
- **Lokalna Demo Foundry** (`foundrylocal/`): Integracja lokalnych modeli AI z OpenAI Java SDK
- **Usługa Kalkulatora MCP** (`calculator/`): Podstawowa implementacja Protokółu Kontekstu Modelu z Spring AI
- **[→ Rozpocznij Rozdział 4](./04-PracticalSamples/README.md)**

### **Rozdział 5: Odpowiedzialny Rozwój AI**
- **Bezpieczeństwo GitHub Models**: Testowanie wbudowanego filtrowania treści i mechanizmów bezpieczeństwa (twarde blokady i miękkie odmowy)
- **Demo Odpowiedzialnego AI**: Praktyczny przykład pokazujący, jak działają nowoczesne systemy bezpieczeństwa AI
- **Najlepsze Praktyki**: Kluczowe wytyczne dotyczące etycznego rozwoju i wdrażania AI
- **[→ Rozpocznij Rozdział 5](./05-ResponsibleGenAI/README.md)**

## Dodatkowe Zasoby

- [MCP dla Początkujących](https://github.com/microsoft/mcp-for-beginners)
- [Agenci AI dla Początkujących](https://github.com/microsoft/ai-agents-for-beginners)
- [Generatywna AI dla Początkujących z .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generatywna AI dla Początkujących z JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generatywna AI dla Początkujących](https://github.com/microsoft/generative-ai-for-beginners)
- [ML dla Początkujących](https://aka.ms/ml-beginners)
- [Data Science dla Początkujących](https://aka.ms/datascience-beginners)
- [AI dla Początkujących](https://aka.ms/ai-beginners)
- [Cyberbezpieczeństwo dla Początkujących](https://github.com/microsoft/Security-101)
- [Web Dev dla Początkujących](https://aka.ms/webdev-beginners)
- [IoT dla Początkujących](https://aka.ms/iot-beginners)
- [XR Development dla Początkujących](https://github.com/microsoft/xr-development-for-beginners)
- [Opanowanie GitHub Copilot dla Programowania w Parze z AI](https://aka.ms/GitHubCopilotAI)
- [Opanowanie GitHub Copilot dla Programistów C#/.NET](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Wybierz Swoją Własną Przygody z Copilotem](https://github.com/microsoft/CopilotAdventures)
- [Aplikacja Chat RAG z Usługami Azure AI](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczenia AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby tłumaczenie było precyzyjne, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego języku źródłowym powinien być uznawany za wiarygodne źródło. W przypadku informacji o kluczowym znaczeniu zaleca się skorzystanie z profesjonalnego tłumaczenia przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.