# Generative KI für Einsteiger - Java Edition
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![Generative KI für Einsteiger - Java Edition](../../translated_images/de/beg-genai-series.8b48be9951cc574c.webp)

**Zeitaufwand**: Der gesamte Workshop kann online ohne lokale Einrichtung abgeschlossen werden. Die Einrichtung der Umgebung dauert 2 Minuten, das Erkunden der Beispiele erfordert je nach Tiefe der Erkundung 1-3 Stunden.

> **Schnellstart**

1. Forken Sie dieses Repository in Ihrem GitHub-Konto
2. Klicken Sie auf **Code** → **Codespaces** Tab → **...** → **Neu mit Optionen...**
3. Verwenden Sie die Standardeinstellungen – dies wählt den für diesen Kurs erstellten Entwicklungscontainer aus
4. Klicken Sie auf **Codespace erstellen**
5. Warten Sie ca. 2 Minuten, bis die Umgebung bereit ist
6. Springen Sie direkt zu [Kapitel 2: Azure AI Foundry bereitstellen](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## Mehrsprachige Unterstützung

### Unterstützt via GitHub Action (Automatisiert & Immer aktuell)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabisch](../ar/README.md) | [Bengalisch](../bn/README.md) | [Bulgarisch](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinesisch (vereinfacht)](../zh-CN/README.md) | [Chinesisch (traditionell, Hongkong)](../zh-HK/README.md) | [Chinesisch (traditionell, Macau)](../zh-MO/README.md) | [Chinesisch (traditionell, Taiwan)](../zh-TW/README.md) | [Kroatisch](../hr/README.md) | [Tschechisch](../cs/README.md) | [Dänisch](../da/README.md) | [Niederländisch](../nl/README.md) | [Estnisch](../et/README.md) | [Finnisch](../fi/README.md) | [Französisch](../fr/README.md) | [Deutsch](./README.md) | [Griechisch](../el/README.md) | [Hebräisch](../he/README.md) | [Hindi](../hi/README.md) | [Ungarisch](../hu/README.md) | [Indonesisch](../id/README.md) | [Italienisch](../it/README.md) | [Japanisch](../ja/README.md) | [Kannada](../kn/README.md) | [Khmer](../km/README.md) | [Koreanisch](../ko/README.md) | [Litauisch](../lt/README.md) | [Malaiisch](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepalesisch](../ne/README.md) | [Nigerianisches Pidgin](../pcm/README.md) | [Norwegisch](../no/README.md) | [Persisch (Farsi)](../fa/README.md) | [Polnisch](../pl/README.md) | [Portugiesisch (Brasilien)](../pt-BR/README.md) | [Portugiesisch (Portugal)](../pt-PT/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Rumänisch](../ro/README.md) | [Russisch](../ru/README.md) | [Serbisch (Kyrillisch)](../sr/README.md) | [Slowakisch](../sk/README.md) | [Slowenisch](../sl/README.md) | [Spanisch](../es/README.md) | [Suaheli](../sw/README.md) | [Schwedisch](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamilisch](../ta/README.md) | [Telugu](../te/README.md) | [Thailändisch](../th/README.md) | [Türkisch](../tr/README.md) | [Ukrainisch](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamesisch](../vi/README.md)

> **Bevorzugen Sie das lokale Klonen?**
>
> Dieses Repository enthält mehr als 50 Sprachübersetzungen, die die Download-Größe erheblich erhöhen. Zum Klonen ohne Übersetzungen verwenden Sie Sparse Checkout:
>
> **Bash / macOS / Linux:**
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):**
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> Dies gibt Ihnen alles, was Sie benötigen, um den Kurs mit deutlich schnellerem Download abzuschließen.
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## Kursstruktur & Lernpfad

### **Kapitel 1: Einführung in Generative KI**
- **Kernkonzepte**: Verständnis von großen Sprachmodellen, Tokens, Embeddings und KI-Fähigkeiten
- **Java KI-Ökosystem**: Überblick über Spring AI und OpenAI SDKs
- **Model Context Protocol**: Einführung in MCP und seine Rolle in der KI-Agenten-Kommunikation
- **Praktische Anwendungen**: Realwelt-Szenarien einschließlich Chatbots und Inhaltserstellung
- **[→ Kapitel 1 starten](./01-IntroToGenAI/README.md)**

### **Kapitel 2: Einrichtung der Entwicklungsumgebung**
- **Azure AI Foundry**: Bereitstellung von GPT-5.6 Luna chat und text-embedding-3-small embeddings mit Bicep und der Azure Developer CLI (azd)
- **Spring Boot 4.1.1 + Spring AI 2.0.1**: Lernen mit `ChatClient`, unterstützt vom offiziellen OpenAI Java SDK und Azure OpenAI v1
- **Schlüssellose Authentifizierung**: Sichere Verbindung mit Microsoft Entra ID — keine Verwaltung von API-Schlüsseln notwendig
- **Entwicklungstools**: Docker-Container, VS Code und GitHub Codespaces-Konfiguration
- **[→ Kapitel 2 starten](./02-SetupDevEnvironment/README.md)**

### **Kapitel 3: Kerntechniken der Generativen KI**
- **Offizielles OpenAI Java SDK**: Direkter Aufruf von Azure OpenAI v1 mit schlüsselloser Authentifizierung
- **Prompt Engineering**: Techniken für optimale KI-Modellantworten
- **Embeddings & Vektoroperationen**: Implementierung von semantischer Suche und Ähnlichkeitsabgleich
- **Retrieval-Augmented Generation (RAG)**: Kombination von KI mit eigenen Datenquellen
- **Funktionsaufrufe**: Erweiterung der KI-Fähigkeiten mit benutzerdefinierten Tools und Plugins
- **[→ Kapitel 3 starten](./03-CoreGenerativeAITechniques/README.md)**

### **Kapitel 4: Praktische Anwendungen & Projekte**
- **Haustier-Geschichtengenerator** (`petstory/`): Kreative Inhaltserstellung mit Azure AI Foundry
- **Foundry Local Demo** (`foundrylocal/`): Lokale KI-Modellintegration mit OpenAI Java SDK
- **MCP Rechner-Service** (`calculator/`): Basale Implementierung des Model Context Protocol mit Spring AI
- **[→ Kapitel 4 starten](./04-PracticalSamples/README.md)**

### **Kapitel 5: Verantwortungsbewusste KI-Entwicklung**
- **Azure AI Foundry Inhalts-Sicherheit**: Testen von integrierten Inhaltsfiltern und Sicherheitsmechanismen (harte Blockaden und sanfte Ablehnungen)
- **Verantwortungsbewusste KI-Demo**: Praxisbeispiel, das zeigt, wie moderne KI-Sicherheitssysteme funktionieren
- **Best Practices**: Wesentliche Richtlinien für ethische KI-Entwicklung und -Bereitstellung
- **[→ Kapitel 5 starten](./05-ResponsibleGenAI/README.md)**

## Weitere Ressourcen

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j für Einsteiger](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js für Einsteiger](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain für Einsteiger](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / Agents
[![AZD für Einsteiger](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge KI für Einsteiger](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP für Einsteiger](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![KI-Agenten für Einsteiger](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Generative KI-Serie
[![Generative KI für Einsteiger](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative KI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative KI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative KI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### Kernlernen
[![ML für Einsteiger](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Datenwissenschaft für Einsteiger](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![KI für Einsteiger](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersicherheit für Einsteiger](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web-Entwicklung für Einsteiger](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![IoT für Einsteiger](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR-Entwicklung für Einsteiger](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot-Serie
[![Copilot für KI-Paired-Programming](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot für C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot Abenteuer](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## Hilfe erhalten

Wenn Sie nicht weiterkommen oder Fragen zum Erstellen von KI-Anwendungen haben. Treten Sie anderen Lernenden und erfahrenen Entwicklern in Diskussionen über MCP bei. Es ist eine unterstützende Gemeinschaft, in der Fragen willkommen sind und Wissen frei geteilt wird.

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

Wenn Sie Produktfeedback oder Fehler beim Bauen haben, besuchen Sie:

[![Microsoft Foundry Entwicklerforum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Haftungsausschluss**:
Dieses Dokument wurde mit dem KI-Übersetzungsdienst [Co-op Translator](https://github.com/Azure/co-op-translator) übersetzt. Obwohl wir uns um Genauigkeit bemühen, beachten Sie bitte, dass automatisierte Übersetzungen Fehler oder Ungenauigkeiten enthalten können. Das Originaldokument in seiner Ursprungssprache gilt als maßgebliche Quelle. Bei kritischen Informationen wird eine professionelle menschliche Übersetzung empfohlen. Wir übernehmen keine Haftung für Missverständnisse oder Fehlinterpretationen, die aus der Verwendung dieser Übersetzung entstehen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->