<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "da1b6d87b8a73306b29f9a1bdd681221",
  "translation_date": "2025-07-21T15:20:47+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "de"
}
-->
# Praktische Anwendungen & Projekte

> Hinweis: Jedes Beispiel enthält auch eine **TUTORIAL.md**, die Sie durch die Ausführung der Anwendung führt.

## Was Sie lernen werden
In diesem Abschnitt präsentieren wir drei praktische Anwendungen, die Entwicklungsansätze für generative KI mit Java demonstrieren:
- Erstellen eines Multi-Modalen Pet Story Generators, der clientseitige und serverseitige KI kombiniert
- Implementieren der Integration lokaler KI-Modelle mit dem Foundry Local Spring Boot Demo
- Entwickeln eines Model Context Protocol (MCP)-Dienstes mit dem Calculator-Beispiel

## Inhaltsverzeichnis

- [Einleitung](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Pet Story Generator](../../../04-PracticalSamples)
  - [MCP Calculator Service (Einsteigerfreundliches MCP-Demo)](../../../04-PracticalSamples)
- [Lernfortschritt](../../../04-PracticalSamples)
- [Zusammenfassung](../../../04-PracticalSamples)
- [Nächste Schritte](../../../04-PracticalSamples)

## Einleitung

Dieses Kapitel präsentiert **Beispielprojekte**, die Entwicklungsansätze für generative KI mit Java demonstrieren. Jedes Projekt ist vollständig funktionsfähig und zeigt spezifische KI-Technologien, Architekturansätze und bewährte Praktiken, die Sie für Ihre eigenen Anwendungen übernehmen können.

### Foundry Local Spring Boot Demo

Das **[Foundry Local Spring Boot Demo](foundrylocal/README.md)** zeigt, wie man lokale KI-Modelle mit dem **OpenAI Java SDK** integriert. Es demonstriert die Verbindung zum **Phi-3.5-mini**-Modell, das auf Foundry Local läuft, und ermöglicht die Ausführung von KI-Anwendungen ohne Cloud-Dienste.

### Pet Story Generator

Der **[Pet Story Generator](petstory/README.md)** ist eine unterhaltsame Spring Boot-Webanwendung, die **Multi-Modale KI-Verarbeitung** demonstriert, um kreative Geschichten über Haustiere zu generieren. Sie kombiniert clientseitige und serverseitige KI-Funktionen mit transformer.js für browserbasierte KI-Interaktionen und dem OpenAI SDK für serverseitige Verarbeitung.

### MCP Calculator Service (Einsteigerfreundliches MCP-Demo)

Der **[MCP Calculator Service](mcp/calculator/README.md)** ist eine einfache Demonstration des **Model Context Protocol (MCP)** mit Spring AI. Es bietet eine einsteigerfreundliche Einführung in MCP-Konzepte und zeigt, wie man einen grundlegenden MCP-Server erstellt, der mit MCP-Clients interagiert.

## Lernfortschritt

Diese Projekte sind darauf ausgelegt, auf Konzepten aus vorherigen Kapiteln aufzubauen:

1. **Einfach anfangen**: Beginnen Sie mit dem Foundry Local Spring Boot Demo, um die grundlegende Integration von KI mit lokalen Modellen zu verstehen.
2. **Interaktivität hinzufügen**: Machen Sie weiter mit dem Pet Story Generator, um Multi-Modale KI und webbasierte Interaktionen zu erkunden.
3. **MCP-Grundlagen lernen**: Probieren Sie den MCP Calculator Service aus, um die Grundlagen des Model Context Protocols zu verstehen.

## Zusammenfassung

**Herzlichen Glückwunsch!** Sie haben erfolgreich:

- **Multi-Modale KI-Erfahrungen erstellt**, die clientseitige und serverseitige KI-Verarbeitung kombinieren
- **Lokale KI-Modellintegration implementiert** mit modernen Java-Frameworks und SDKs
- **Model Context Protocol-Dienste entwickelt**, die Integrationsansätze demonstrieren

## Nächste Schritte

[Kapitel 5: Verantwortungsvolle Generative KI](../05-ResponsibleGenAI/README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.