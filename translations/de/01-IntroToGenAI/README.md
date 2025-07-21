<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "6d8b4a0d774dc2a1e97c95859a6d6e4b",
  "translation_date": "2025-07-21T15:36:04+00:00",
  "source_file": "01-IntroToGenAI/README.md",
  "language_code": "de"
}
-->
# Einführung in Generative KI - Java Edition

## Was Sie lernen werden

- **Grundlagen der generativen KI**, einschließlich LLMs, Prompt-Engineering, Tokens, Embeddings und Vektordatenbanken
- **Vergleich von Java-Entwicklungstools für KI**, einschließlich Azure OpenAI SDK, Spring AI und OpenAI Java SDK
- **Entdeckung des Model Context Protocol** und dessen Rolle in der Kommunikation von KI-Agenten

## Inhaltsverzeichnis

- [Einleitung](../../../01-IntroToGenAI)
- [Ein kurzer Überblick über Konzepte der generativen KI](../../../01-IntroToGenAI)
- [Überblick über Prompt-Engineering](../../../01-IntroToGenAI)
- [Tokens, Embeddings und Agenten](../../../01-IntroToGenAI)
- [Entwicklungstools und Bibliotheken für KI in Java](../../../01-IntroToGenAI)
  - [OpenAI Java SDK](../../../01-IntroToGenAI)
  - [Spring AI](../../../01-IntroToGenAI)
  - [Azure OpenAI Java SDK](../../../01-IntroToGenAI)
- [Zusammenfassung](../../../01-IntroToGenAI)
- [Nächste Schritte](../../../01-IntroToGenAI)

## Einleitung

Willkommen zum ersten Kapitel von Generative KI für Anfänger - Java Edition! Diese grundlegende Lektion führt Sie in die Kernkonzepte der generativen KI ein und zeigt Ihnen, wie Sie mit Java damit arbeiten können. Sie lernen die wesentlichen Bausteine von KI-Anwendungen kennen, darunter Large Language Models (LLMs), Tokens, Embeddings und KI-Agenten. Außerdem erkunden wir die wichtigsten Java-Tools, die Sie im Verlauf dieses Kurses verwenden werden.

### Ein kurzer Überblick über Konzepte der generativen KI

Generative KI ist eine Art von künstlicher Intelligenz, die neue Inhalte wie Texte, Bilder oder Code basierend auf Mustern und Beziehungen erstellt, die aus Daten gelernt wurden. Generative KI-Modelle können menschenähnliche Antworten generieren, Kontext verstehen und manchmal sogar Inhalte erstellen, die menschlich wirken.

Während Sie Ihre Java-KI-Anwendungen entwickeln, arbeiten Sie mit **generativen KI-Modellen**, um Inhalte zu erstellen. Einige Fähigkeiten von generativen KI-Modellen umfassen:

- **Textgenerierung**: Erstellen von menschenähnlichem Text für Chatbots, Inhalte und Textvervollständigung.
- **Bildgenerierung und -analyse**: Erstellen realistischer Bilder, Verbessern von Fotos und Erkennen von Objekten.
- **Codegenerierung**: Schreiben von Code-Snippets oder Skripten.

Es gibt spezifische Modelltypen, die für verschiedene Aufgaben optimiert sind. Zum Beispiel können sowohl **Small Language Models (SLMs)** als auch **Large Language Models (LLMs)** Text generieren, wobei LLMs in der Regel eine bessere Leistung bei komplexen Aufgaben bieten. Für bildbezogene Aufgaben würden Sie spezialisierte Vision-Modelle oder multimodale Modelle verwenden.

![Abbildung: Typen von generativen KI-Modellen und Anwendungsfälle.](../../../translated_images/llms.225ca2b8a0d344738419defc5ae14bba2fd3388b94f09fd4e8be8ce2a720ae51.de.png)

Natürlich sind die Antworten dieser Modelle nicht immer perfekt. Sie haben wahrscheinlich schon gehört, dass Modelle "halluzinieren" oder falsche Informationen auf autoritäre Weise generieren können. Aber Sie können das Modell durch klare Anweisungen und Kontext dazu bringen, bessere Antworten zu generieren. Hier kommt das **Prompt-Engineering** ins Spiel.

#### Überblick über Prompt-Engineering

Prompt-Engineering ist die Praxis, effektive Eingaben zu entwerfen, um KI-Modelle zu gewünschten Ausgaben zu führen. Es umfasst:

- **Klarheit**: Klare und eindeutige Anweisungen geben.
- **Kontext**: Notwendige Hintergrundinformationen bereitstellen.
- **Einschränkungen**: Begrenzungen oder Formate spezifizieren.

Einige bewährte Praktiken im Prompt-Engineering umfassen das Design von Prompts, klare Anweisungen, Aufgabenzerlegung, One-Shot- und Few-Shot-Lernen sowie Prompt-Tuning. Das Testen verschiedener Prompts ist entscheidend, um herauszufinden, was für Ihren spezifischen Anwendungsfall am besten funktioniert.

Bei der Entwicklung von Anwendungen arbeiten Sie mit verschiedenen Prompt-Typen:
- **System-Prompts**: Legen die Grundregeln und den Kontext für das Verhalten des Modells fest.
- **Benutzer-Prompts**: Die Eingabedaten Ihrer Anwendungsbenutzer.
- **Assistenten-Prompts**: Die Antworten des Modells basierend auf System- und Benutzer-Prompts.

> **Erfahren Sie mehr**: Erfahren Sie mehr über Prompt-Engineering im [Kapitel Prompt-Engineering des GenAI für Anfänger-Kurses](https://github.com/microsoft/generative-ai-for-beginners/tree/main/04-prompt-engineering-fundamentals)

#### Tokens, Embeddings und Agenten

Beim Arbeiten mit generativen KI-Modellen stoßen Sie auf Begriffe wie **Tokens**, **Embeddings**, **Agenten** und **Model Context Protocol (MCP)**. Hier ist eine detaillierte Übersicht dieser Konzepte:

- **Tokens**: Tokens sind die kleinste Einheit von Text in einem Modell. Sie können Wörter, Zeichen oder Teilwörter sein. Tokens werden verwendet, um Textdaten in einem Format darzustellen, das das Modell verstehen kann. Zum Beispiel könnte der Satz "The quick brown fox jumped over the lazy dog" als ["The", " quick", " brown", " fox", " jumped", " over", " the", " lazy", " dog"] oder ["The", " qu", "ick", " br", "own", " fox", " jump", "ed", " over", " the", " la", "zy", " dog"] tokenisiert werden, je nach Tokenisierungsstrategie.

![Abbildung: Beispiel für generative KI-Tokens, die Wörter in Tokens zerlegen](../../../01-IntroToGenAI/images/tokens.webp)

Die Tokenisierung ist der Prozess des Zerlegens von Text in diese kleineren Einheiten. Dies ist entscheidend, da Modelle mit Tokens und nicht mit Rohtext arbeiten. Die Anzahl der Tokens in einem Prompt beeinflusst die Länge und Qualität der Modellantwort, da Modelle Token-Limits für ihr Kontextfenster haben (z. B. 128K Tokens für GPT-4o's Gesamtkontext, einschließlich Eingabe und Ausgabe).

  In Java können Sie Bibliotheken wie das OpenAI SDK verwenden, um die Tokenisierung automatisch zu handhaben, wenn Sie Anfragen an KI-Modelle senden.

- **Embeddings**: Embeddings sind Vektorrepräsentationen von Tokens, die semantische Bedeutungen erfassen. Sie sind numerische Darstellungen (typischerweise Arrays von Gleitkommazahlen), die es Modellen ermöglichen, Beziehungen zwischen Wörtern zu verstehen und kontextuell relevante Antworten zu generieren. Ähnliche Wörter haben ähnliche Embeddings, wodurch das Modell Konzepte wie Synonyme und semantische Beziehungen verstehen kann.

![Abbildung: Embeddings](../../../translated_images/embedding.398e50802c0037f931c725fd0113747831ea7776434d2b3ba3eb2e7a1a20ab1f.de.png)

  In Java können Sie Embeddings mit dem OpenAI SDK oder anderen Bibliotheken generieren, die die Erstellung von Embeddings unterstützen. Diese Embeddings sind entscheidend für Aufgaben wie semantische Suche, bei der Sie ähnliche Inhalte basierend auf Bedeutung und nicht auf exakten Textübereinstimmungen finden möchten.

- **Vektordatenbanken**: Vektordatenbanken sind spezialisierte Speichersysteme, die für Embeddings optimiert sind. Sie ermöglichen eine effiziente Ähnlichkeitssuche und sind entscheidend für Retrieval-Augmented Generation (RAG)-Muster, bei denen Sie relevante Informationen aus großen Datensätzen basierend auf semantischer Ähnlichkeit und nicht auf exakten Übereinstimmungen finden müssen.

![Abbildung: Architektur einer Vektordatenbank, die zeigt, wie Embeddings für Ähnlichkeitssuche gespeichert und abgerufen werden.](../../../translated_images/vector.f12f114934e223dff971b01ca371e85a41a540f3af2ffdd49fb3acec6c6652f2.de.png)

> **Hinweis**: In diesem Kurs werden wir Vektordatenbanken nicht behandeln, aber sie sind erwähnenswert, da sie häufig in realen Anwendungen verwendet werden.

- **Agenten & MCP**: KI-Komponenten, die autonom mit Modellen, Tools und externen Systemen interagieren. Das Model Context Protocol (MCP) bietet eine standardisierte Möglichkeit für Agenten, sicher auf externe Datenquellen und Tools zuzugreifen. Erfahren Sie mehr in unserem [MCP für Anfänger](https://github.com/microsoft/mcp-for-beginners)-Kurs.

In Java-KI-Anwendungen verwenden Sie Tokens für die Textverarbeitung, Embeddings für semantische Suche und RAG, Vektordatenbanken für die Datenabfrage und Agenten mit MCP, um intelligente, toolnutzende Systeme zu erstellen.

![Abbildung: Wie ein Prompt zu einer Antwort wird—Tokens, Vektoren, optionales RAG-Lookup, LLM-Denken und ein MCP-Agent alles in einem schnellen Ablauf.](../../../translated_images/flow.f4ef62c3052d12a88b1d216eb2cd0e2ea3293c806d0defa7921dd1786dcb8516.de.png)

### Entwicklungstools und Bibliotheken für KI in Java

Java bietet hervorragende Tools für die KI-Entwicklung. Es gibt drei Hauptbibliotheken, die wir im Verlauf dieses Kurses erkunden werden - OpenAI Java SDK, Azure OpenAI SDK und Spring AI.

Hier ist eine schnelle Referenztabelle, die zeigt, welches SDK in den Beispielen der einzelnen Kapitel verwendet wird:

| Kapitel | Beispiel | SDK |
|---------|----------|-----|
| 02-SetupDevEnvironment | src/github-models/ | OpenAI Java SDK |
| 02-SetupDevEnvironment | src/basic-chat-azure/ | Spring AI Azure OpenAI |
| 03-CoreGenerativeAITechniques | examples/ | Azure OpenAI SDK |
| 04-PracticalSamples | petstory/ | OpenAI Java SDK |
| 04-PracticalSamples | foundrylocal/ | OpenAI Java SDK |
| 04-PracticalSamples | mcp/calculator/ | Spring AI MCP SDK + LangChain4j |

**SDK-Dokumentationslinks:**
- [Azure OpenAI Java SDK](https://github.com/Azure/azure-sdk-for-java/tree/azure-ai-openai_1.0.0-beta.16/sdk/openai/azure-ai-openai)
- [Spring AI](https://docs.spring.io/spring-ai/reference/)
- [OpenAI Java SDK](https://github.com/openai/openai-java)
- [LangChain4j](https://docs.langchain4j.dev/)

#### OpenAI Java SDK

Das OpenAI SDK ist die offizielle Java-Bibliothek für die OpenAI-API. Es bietet eine einfache und konsistente Schnittstelle für die Interaktion mit den Modellen von OpenAI und erleichtert die Integration von KI-Funktionen in Java-Anwendungen. Kapitel 2's GitHub Models Beispiel, Kapitel 4's Pet Story Anwendung und Foundry Local Beispiel demonstrieren den Ansatz des OpenAI SDK.

#### Spring AI

Spring AI ist ein umfassendes Framework, das KI-Funktionen in Spring-Anwendungen bringt und eine konsistente Abstraktionsebene über verschiedene KI-Anbieter bietet. Es integriert sich nahtlos in das Spring-Ökosystem und ist die ideale Wahl für Unternehmens-Java-Anwendungen, die KI-Funktionen benötigen.

Die Stärke von Spring AI liegt in seiner nahtlosen Integration in das Spring-Ökosystem, wodurch es einfach ist, produktionsreife KI-Anwendungen mit vertrauten Spring-Mustern wie Dependency Injection, Konfigurationsmanagement und Testframeworks zu erstellen. Sie werden Spring AI in Kapitel 2 und 4 verwenden, um Anwendungen zu erstellen, die sowohl OpenAI als auch die Model Context Protocol (MCP) Spring AI-Bibliotheken nutzen.

##### Model Context Protocol (MCP)

Das [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) ist ein aufkommender Standard, der es KI-Anwendungen ermöglicht, sicher mit externen Datenquellen und Tools zu interagieren. MCP bietet eine standardisierte Möglichkeit für KI-Modelle, auf kontextuelle Informationen zuzugreifen und Aktionen in Ihren Anwendungen auszuführen.

In Kapitel 4 erstellen Sie einen einfachen MCP-Rechnerdienst, der die Grundlagen des Model Context Protocol mit Spring AI demonstriert und zeigt, wie man grundlegende Tool-Integrationen und Service-Architekturen erstellt.

#### Azure OpenAI Java SDK

Die Azure OpenAI-Clientbibliothek für Java ist eine Anpassung der REST-APIs von OpenAI, die eine idiomatische Schnittstelle und Integration mit dem Rest des Azure SDK-Ökosystems bietet. In Kapitel 3 erstellen Sie Anwendungen mit dem Azure OpenAI SDK, einschließlich Chat-Anwendungen, Funktionsaufrufen und RAG (Retrieval-Augmented Generation)-Mustern.

> Hinweis: Das Azure OpenAI SDK hinkt dem OpenAI Java SDK in Bezug auf Funktionen hinterher, daher sollten Sie für zukünftige Projekte das OpenAI Java SDK in Betracht ziehen.

## Zusammenfassung

**Herzlichen Glückwunsch!** Sie haben erfolgreich:

- **Die Grundlagen der generativen KI gelernt**, einschließlich LLMs, Prompt-Engineering, Tokens, Embeddings und Vektordatenbanken
- **Java-KI-Entwicklungstools verglichen**, einschließlich Azure OpenAI SDK, Spring AI und OpenAI Java SDK
- **Das Model Context Protocol entdeckt** und dessen Rolle in der Kommunikation von KI-Agenten

## Nächste Schritte

[Kapitel 2: Einrichten der Entwicklungsumgebung](../02-SetupDevEnvironment/README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.