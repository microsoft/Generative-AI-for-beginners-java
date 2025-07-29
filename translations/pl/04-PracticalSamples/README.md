<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T15:20:39+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "pl"
}
-->
# Praktyczne Zastosowania i Projekty

## Czego się nauczysz
W tej sekcji zaprezentujemy trzy praktyczne aplikacje, które pokazują wzorce rozwoju generatywnej sztucznej inteligencji z użyciem Javy:
- Tworzenie wielomodalnego Generatora Opowieści o Zwierzakach, łączącego AI po stronie klienta i serwera
- Wdrażanie integracji z lokalnym modelem AI za pomocą demonstracji Foundry Local Spring Boot
- Rozwijanie usługi Model Context Protocol (MCP) na przykładzie Kalkulatora

## Spis treści

- [Wprowadzenie](../../../04-PracticalSamples)
  - [Demonstracja Foundry Local Spring Boot](../../../04-PracticalSamples)
  - [Generator Opowieści o Zwierzakach](../../../04-PracticalSamples)
  - [Usługa MCP Kalkulator (Przyjazna dla Początkujących Demonstracja MCP)](../../../04-PracticalSamples)
- [Postęp w nauce](../../../04-PracticalSamples)
- [Podsumowanie](../../../04-PracticalSamples)
- [Kolejne kroki](../../../04-PracticalSamples)

## Wprowadzenie

Ten rozdział przedstawia **przykładowe projekty**, które demonstrują wzorce rozwoju generatywnej sztucznej inteligencji z użyciem Javy. Każdy projekt jest w pełni funkcjonalny i ilustruje konkretne technologie AI, wzorce architektoniczne oraz najlepsze praktyki, które możesz zaadaptować do własnych aplikacji.

### Demonstracja Foundry Local Spring Boot

**[Demonstracja Foundry Local Spring Boot](foundrylocal/README.md)** pokazuje, jak zintegrować się z lokalnymi modelami AI za pomocą **OpenAI Java SDK**. Prezentuje połączenie z modelem **Phi-3.5-mini**, działającym na Foundry Local, co pozwala na uruchamianie aplikacji AI bez konieczności korzystania z usług w chmurze.

### Generator Opowieści o Zwierzakach

**[Generator Opowieści o Zwierzakach](petstory/README.md)** to angażująca aplikacja webowa oparta na Spring Boot, która demonstruje **wielomodalne przetwarzanie AI** do tworzenia kreatywnych opowieści o zwierzakach. Łączy możliwości AI po stronie klienta i serwera, wykorzystując transformer.js do interakcji w przeglądarce oraz OpenAI SDK do przetwarzania po stronie serwera.

### Usługa MCP Kalkulator (Przyjazna dla Początkujących Demonstracja MCP)

**[Usługa MCP Kalkulator](calculator/README.md)** to prosta demonstracja **Model Context Protocol (MCP)** z użyciem Spring AI. Zapewnia przyjazne dla początkujących wprowadzenie do koncepcji MCP, pokazując, jak stworzyć podstawowy serwer MCP, który współpracuje z klientami MCP.

## Postęp w nauce

Te projekty zostały zaprojektowane tak, aby budować wiedzę krok po kroku:

1. **Zacznij od podstaw**: Rozpocznij od Demonstracji Foundry Local Spring Boot, aby zrozumieć podstawową integrację AI z lokalnymi modelami
2. **Dodaj interaktywność**: Przejdź do Generatora Opowieści o Zwierzakach, aby poznać wielomodalne AI i interakcje webowe
3. **Poznaj podstawy MCP**: Wypróbuj Usługę MCP Kalkulator, aby zrozumieć fundamenty Model Context Protocol

## Podsumowanie

Świetna robota! Poznałeś teraz kilka rzeczywistych zastosowań:

- Wielomodalne doświadczenia AI, które działają zarówno w przeglądarce, jak i na serwerze
- Integrację z lokalnymi modelami AI przy użyciu nowoczesnych frameworków i SDK w Javie
- Swoją pierwszą usługę Model Context Protocol, aby zobaczyć, jak narzędzia współpracują z AI

## Kolejne kroki

[Rozdział 5: Odpowiedzialna Generatywna Sztuczna Inteligencja](../05-ResponsibleGenAI/README.md)

**Zastrzeżenie**:  
Ten dokument został przetłumaczony za pomocą usługi tłumaczeniowej AI [Co-op Translator](https://github.com/Azure/co-op-translator). Chociaż dokładamy wszelkich starań, aby zapewnić poprawność tłumaczenia, prosimy pamiętać, że automatyczne tłumaczenia mogą zawierać błędy lub nieścisłości. Oryginalny dokument w jego rodzimym języku powinien być uznawany za wiarygodne źródło. W przypadku informacji o kluczowym znaczeniu zaleca się skorzystanie z profesjonalnego tłumaczenia przez człowieka. Nie ponosimy odpowiedzialności za jakiekolwiek nieporozumienia lub błędne interpretacje wynikające z użycia tego tłumaczenia.