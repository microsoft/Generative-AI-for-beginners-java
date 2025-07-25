<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "df269f529a172a0197ef28460bf1da9f",
  "translation_date": "2025-07-25T10:39:14+00:00",
  "source_file": "04-PracticalSamples/README.md",
  "language_code": "de"
}
-->
# Praktische Anwendungen & Projekte

## Was Sie lernen werden
In diesem Abschnitt zeigen wir drei praktische Anwendungen, die Entwicklungsmuster für generative KI mit Java demonstrieren:
- Erstellen eines Multi-Modal Pet Story Generators, der clientseitige und serverseitige KI kombiniert
- Implementierung der Integration lokaler KI-Modelle mit dem Foundry Local Spring Boot Demo
- Entwicklung eines Model Context Protocol (MCP) Dienstes anhand des Taschenrechner-Beispiels

## Inhaltsverzeichnis

- [Einleitung](../../../04-PracticalSamples)
  - [Foundry Local Spring Boot Demo](../../../04-PracticalSamples)
  - [Pet Story Generator](../../../04-PracticalSamples)
  - [MCP Calculator Service (Einsteigerfreundliches MCP-Demo)](../../../04-PracticalSamples)
- [Lernfortschritt](../../../04-PracticalSamples)
- [Zusammenfassung](../../../04-PracticalSamples)
- [Nächste Schritte](../../../04-PracticalSamples)

## Einleitung

Dieses Kapitel präsentiert **Beispielfprojekte**, die Entwicklungsmuster für generative KI mit Java veranschaulichen. Jedes Projekt ist vollständig funktionsfähig und zeigt spezifische KI-Technologien, Architekturansätze und Best Practices, die Sie für Ihre eigenen Anwendungen übernehmen können.

### Foundry Local Spring Boot Demo

Das **[Foundry Local Spring Boot Demo](foundrylocal/README.md)** zeigt, wie man lokale KI-Modelle mit dem **OpenAI Java SDK** integriert. Es demonstriert die Verbindung mit dem **Phi-3.5-mini** Modell, das auf Foundry Local läuft, und ermöglicht es Ihnen, KI-Anwendungen ohne Cloud-Dienste auszuführen.

### Pet Story Generator

Der **[Pet Story Generator](petstory/README.md)** ist eine unterhaltsame Spring Boot Webanwendung, die **Multi-Modal KI-Verarbeitung** nutzt, um kreative Haustiergeschichten zu generieren. Sie kombiniert clientseitige und serverseitige KI-Funktionen, indem transformer.js für browserbasierte KI-Interaktionen und das OpenAI SDK für serverseitige Verarbeitung verwendet werden.

### MCP Calculator Service (Einsteigerfreundliches MCP-Demo)

Der **[MCP Calculator Service](mcp/calculator/README.md)** ist eine einfache Demonstration des **Model Context Protocol (MCP)** mit Spring AI. Es bietet eine einsteigerfreundliche Einführung in MCP-Konzepte und zeigt, wie man einen grundlegenden MCP-Server erstellt, der mit MCP-Clients interagiert.

## Lernfortschritt

Diese Projekte bauen auf Konzepten aus den vorherigen Kapiteln auf:

1. **Einfach anfangen**: Beginnen Sie mit dem Foundry Local Spring Boot Demo, um die grundlegende KI-Integration mit lokalen Modellen zu verstehen
2. **Interaktivität hinzufügen**: Machen Sie mit dem Pet Story Generator weiter, um Multi-Modal KI und webbasierte Interaktionen zu erkunden
3. **MCP-Grundlagen lernen**: Probieren Sie den MCP Calculator Service aus, um die Grundlagen des Model Context Protocols zu verstehen

## Zusammenfassung

**Herzlichen Glückwunsch!** Sie haben erfolgreich:

- **Multi-Modal KI-Erfahrungen erstellt**, die clientseitige und serverseitige KI-Verarbeitung kombinieren
- **Lokale KI-Modellintegration implementiert** mit modernen Java-Frameworks und SDKs
- **Model Context Protocol Dienste entwickelt**, die Muster für Tool-Integration demonstrieren

## Nächste Schritte

[Kapitel 5: Verantwortungsvolle Generative KI](../05-ResponsibleGenAI/README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.