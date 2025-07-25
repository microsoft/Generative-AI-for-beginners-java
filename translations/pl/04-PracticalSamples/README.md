<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T11:27:05+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "pl"
}
-->
# Praktyczne Zastosowania i Projekty

## Czego się nauczysz
W tej sekcji zaprezentujemy trzy praktyczne aplikacje, które pokazują wzorce rozwoju generatywnej AI z użyciem Java:
- Tworzenie wielomodalnego Generatora Opowieści o Zwierzętach, łączącego AI po stronie klienta i serwera
- Implementacja integracji lokalnego modelu AI z użyciem demonstracji Foundry Local Spring Boot
- Rozwój usługi Model Context Protocol (MCP) na przykładzie Kalkulatora

## Spis Treści

- [Wprowadzenie](../../../04-PracticalSamples)
  - [Demonstracja Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Generator Opowieści o Zwierzętach](../../../04-PracticalSamples)
  - [Usługa MCP Kalkulator (Przyjazna dla Początkujących Demonstracja MCP)](../../../04-PracticalSamples)
- [Postęp w nauce](../../../04-PracticalSamples)
- [Podsumowanie](../../../04-PracticalSamples)
- [Kolejne kroki](../../../04-PracticalSamples)

## Wprowadzenie

Ten rozdział prezentuje **przykładowe projekty**, które demonstrują wzorce rozwoju generatywnej AI z użyciem Java. Każdy projekt jest w pełni funkcjonalny i pokazuje konkretne technologie AI, wzorce architektoniczne oraz najlepsze praktyki, które możesz dostosować do własnych aplikacji.

### Demonstracja Foundry Local Spring Boot

**[Foundry Local Spring Boot Demo](foundrylocal/README.md)** pokazuje, jak zintegrować lokalne modele AI z użyciem **OpenAI Java SDK**. Demonstruje połączenie z modelem **Phi-3.5-mini** działającym na Foundry Local, umożliwiając uruchamianie aplikacji AI bez konieczności korzystania z usług w chmurze.

### Generator Opowieści o Zwierzętach

**[Generator Opowieści o Zwierzętach](petstory/README.md)** to angażująca aplikacja webowa Spring Boot, która demonstruje **wielomodalne przetwarzanie AI** w celu generowania kreatywnych opowieści o zwierzętach. Łączy możliwości AI po stronie klienta i serwera, wykorzystując transformer.js do interakcji AI w przeglądarce oraz OpenAI SDK do przetwarzania po stronie serwera.

### Usługa MCP Kalkulator (Przyjazna dla Początkujących Demonstracja MCP)

**[Usługa MCP Kalkulator](mcp/calculator/README.md)** to prosta demonstracja **Model Context Protocol (MCP)** z użyciem Spring AI. Zapewnia przyjazne dla początkujących wprowadzenie do koncepcji MCP, pokazując, jak stworzyć podstawowy serwer MCP, który współdziała z klientami MCP.

## Postęp w nauce

Te projekty zostały zaprojektowane tak, aby budować na koncepcjach z poprzednich rozdziałów:

1. **Zacznij od podstaw**: Rozpocznij od demonstracji Foundry Local Spring Boot, aby zrozumieć podstawową integrację AI z lokalnymi modelami
2. **Dodaj interaktywność**: Przejdź do Generatora Opowieści o Zwierzętach, aby poznać wielomodalne AI i interakcje webowe
3. **Poznaj podstawy MCP**: Wypróbuj Usługę MCP Kalkulator, aby zrozumieć fundamenty Model Context Protocol

## Podsumowanie

**Gratulacje!** Udało Ci się:

- **Stworzyć wielomodalne doświadczenia AI**, łącząc przetwarzanie AI po stronie klienta i serwera
- **Zaimplementować integrację lokalnych modeli AI** z użyciem nowoczesnych frameworków i SDK dla Java
- **Rozwinąć usługi Model Context Protocol**, demonstrując wzorce integracji narzędzi

## Kolejne kroki

[Rozdział 5: Odpowiedzialna Generatywna AI](../05-ResponsibleGenAI/README.md)

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby zapewnić precyzję, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego rodzimym języku powinien być uznawany za autorytatywne źródło. W przypadku informacji o kluczowym znaczeniu zaleca się skorzystanie z profesjonalnego tłumaczenia przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z korzystania z tego tłumaczenia.