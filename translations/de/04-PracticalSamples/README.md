<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "14c0a61ecc1cd2012a9c129236dfdf71",
  "translation_date": "2025-07-29T14:19:11+00:00",
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

Dieses Kapitel präsentiert **Beispielprojekte**, die Entwicklungsmuster für generative KI mit Java demonstrieren. Jedes Projekt ist voll funktionsfähig und zeigt spezifische KI-Technologien, Architekturansätze und Best Practices, die Sie für Ihre eigenen Anwendungen übernehmen können.

### Foundry Local Spring Boot Demo

Das **[Foundry Local Spring Boot Demo](foundrylocal/README.md)** zeigt, wie man lokale KI-Modelle mit dem **OpenAI Java SDK** integriert. Es demonstriert die Verbindung mit dem **Phi-3.5-mini** Modell, das auf Foundry Local läuft, und ermöglicht es Ihnen, KI-Anwendungen ohne Abhängigkeit von Cloud-Diensten auszuführen.

### Pet Story Generator

Der **[Pet Story Generator](petstory/README.md)** ist eine unterhaltsame Spring Boot Webanwendung, die **multi-modale KI-Verarbeitung** nutzt, um kreative Haustiergeschichten zu generieren. Sie kombiniert clientseitige und serverseitige KI-Funktionen, indem transformer.js für browserbasierte KI-Interaktionen und das OpenAI SDK für serverseitige Verarbeitung verwendet werden.

### MCP Calculator Service (Einsteigerfreundliches MCP-Demo)

Der **[MCP Calculator Service](calculator/README.md)** ist eine einfache Demonstration des **Model Context Protocol (MCP)** mit Spring AI. Er bietet eine einsteigerfreundliche Einführung in MCP-Konzepte und zeigt, wie man einen grundlegenden MCP-Server erstellt, der mit MCP-Clients interagiert.

## Lernfortschritt

Diese Projekte bauen auf Konzepten aus den vorherigen Kapiteln auf:

1. **Einfach anfangen**: Beginnen Sie mit dem Foundry Local Spring Boot Demo, um die grundlegende Integration von KI mit lokalen Modellen zu verstehen.
2. **Interaktivität hinzufügen**: Machen Sie mit dem Pet Story Generator weiter, um multi-modale KI und webbasierte Interaktionen zu erkunden.
3. **Grundlagen von MCP lernen**: Probieren Sie den MCP Calculator Service aus, um die Grundlagen des Model Context Protocols zu verstehen.

## Zusammenfassung

Gut gemacht! Sie haben nun einige reale Anwendungen erkundet:

- Multi-modale KI-Erfahrungen, die sowohl im Browser als auch auf dem Server funktionieren
- Integration lokaler KI-Modelle mit modernen Java-Frameworks und SDKs
- Ihren ersten Model Context Protocol Dienst, um zu sehen, wie Tools mit KI integriert werden

## Nächste Schritte

[Kapitel 5: Verantwortungsvolle Generative KI](../05-ResponsibleGenAI/README.md)

**Haftungsausschluss**:  
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner ursprünglichen Sprache sollte als maßgebliche Quelle betrachtet werden. Für kritische Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die sich aus der Nutzung dieser Übersetzung ergeben.