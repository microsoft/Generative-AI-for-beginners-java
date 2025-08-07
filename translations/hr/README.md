<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:22:32+00:00",
  "source_file": "README.md",
  "language_code": "hr"
}
-->
# Generativna umjetna inteligencija za početnike - Java izdanje
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![Generativna umjetna inteligencija za početnike - Java izdanje](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.hr.png)

**Vrijeme potrebno**: Cijela radionica može se završiti online bez lokalne instalacije. Postavljanje okruženja traje 2 minute, dok istraživanje primjera zahtijeva 1-3 sata, ovisno o dubini istraživanja.

> **Brzi početak**

1. Forkajte ovaj repozitorij na svoj GitHub račun
2. Kliknite **Code** → kartica **Codespaces** → **...** → **New with options...**
3. Koristite zadane postavke – ovo će odabrati razvojni kontejner kreiran za ovaj tečaj
4. Kliknite **Create codespace**
5. Pričekajte ~2 minute da okruženje bude spremno
6. Odmah krenite na [Prvi primjer](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## Podrška za više jezika

### Podržano putem GitHub Action (Automatski i uvijek ažurirano)

[Francuski](../fr/README.md) | [Španjolski](../es/README.md) | [Njemački](../de/README.md) | [Ruski](../ru/README.md) | [Arapski](../ar/README.md) | [Perzijski (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Kineski (pojednostavljeni)](../zh/README.md) | [Kineski (tradicionalni, Makao)](../mo/README.md) | [Kineski (tradicionalni, Hong Kong)](../hk/README.md) | [Kineski (tradicionalni, Tajvan)](../tw/README.md) | [Japanski](../ja/README.md) | [Korejski](../ko/README.md) | [Hindski](../hi/README.md) | [Bengalski](../bn/README.md) | [Marathi](../mr/README.md) | [Nepalski](../ne/README.md) | [Pandžapski (Gurmukhi)](../pa/README.md) | [Portugalski (Portugal)](../pt/README.md) | [Portugalski (Brazil)](../br/README.md) | [Talijanski](../it/README.md) | [Poljski](../pl/README.md) | [Turski](../tr/README.md) | [Grčki](../el/README.md) | [Tajlandski](../th/README.md) | [Švedski](../sv/README.md) | [Danski](../da/README.md) | [Norveški](../no/README.md) | [Finski](../fi/README.md) | [Nizozemski](../nl/README.md) | [Hebrejski](../he/README.md) | [Vijetnamski](../vi/README.md) | [Indonezijski](../id/README.md) | [Malajski](../ms/README.md) | [Tagalog (Filipinski)](../tl/README.md) | [Svahili](../sw/README.md) | [Mađarski](../hu/README.md) | [Češki](../cs/README.md) | [Slovački](../sk/README.md) | [Rumunjski](../ro/README.md) | [Bugarski](../bg/README.md) | [Srpski (ćirilica)](../sr/README.md) | [Hrvatski](./README.md) | [Slovenski](../sl/README.md) | [Ukrajinski](../uk/README.md) | [Burmanski (Mjanmar)](../my/README.md)

## Struktura tečaja i put učenja

### **Poglavlje 1: Uvod u generativnu umjetnu inteligenciju**
- **Osnovni pojmovi**: Razumijevanje velikih jezičnih modela, tokena, ugrađivanja i AI sposobnosti
- **Java AI ekosustav**: Pregled Spring AI i OpenAI SDK-ova
- **Protokol konteksta modela**: Uvod u MCP i njegovu ulogu u komunikaciji AI agenata
- **Praktične primjene**: Scenariji iz stvarnog svijeta, uključujući chatbotove i generiranje sadržaja
- **[→ Započnite Poglavlje 1](./01-IntroToGenAI/README.md)**

### **Poglavlje 2: Postavljanje razvojnog okruženja**
- **Konfiguracija za više pružatelja usluga**: Postavite GitHub modele, Azure OpenAI i OpenAI Java SDK integracije
- **Spring Boot + Spring AI**: Najbolje prakse za razvoj AI aplikacija u poduzećima
- **GitHub modeli**: Besplatan pristup AI modelima za prototipiranje i učenje (nije potrebna kreditna kartica)
- **Razvojni alati**: Konfiguracija Docker kontejnera, VS Code-a i GitHub Codespaces-a
- **[→ Započnite Poglavlje 2](./02-SetupDevEnvironment/README.md)**

### **Poglavlje 3: Osnovne tehnike generativne umjetne inteligencije**
- **Inženjering upita**: Tehnike za optimalne odgovore AI modela
- **Ugrađivanja i vektorske operacije**: Implementacija semantičkog pretraživanja i podudaranja sličnosti
- **Generacija uz pomoć pretraživanja (RAG)**: Kombiniranje AI-a s vlastitim izvorima podataka
- **Pozivanje funkcija**: Proširenje AI sposobnosti prilagođenim alatima i dodacima
- **[→ Započnite Poglavlje 3](./03-CoreGenerativeAITechniques/README.md)**

### **Poglavlje 4: Praktične primjene i projekti**
- **Generator priča o kućnim ljubimcima** (`petstory/`): Kreativno generiranje sadržaja s GitHub modelima
- **Foundry lokalna demonstracija** (`foundrylocal/`): Integracija lokalnih AI modela s OpenAI Java SDK-om
- **MCP kalkulator usluga** (`calculator/`): Osnovna implementacija protokola konteksta modela sa Spring AI
- **[→ Započnite Poglavlje 4](./04-PracticalSamples/README.md)**

### **Poglavlje 5: Razvoj odgovorne umjetne inteligencije**
- **Sigurnost GitHub modela**: Testiranje ugrađenog filtriranja sadržaja i sigurnosnih mehanizama (čvrste blokade i mekana odbijanja)
- **Demonstracija odgovorne AI**: Praktični primjer koji pokazuje kako moderni AI sigurnosni sustavi funkcioniraju
- **Najbolje prakse**: Ključne smjernice za etički razvoj i implementaciju AI-a
- **[→ Započnite Poglavlje 5](./05-ResponsibleGenAI/README.md)**

## Dodatni resursi

- [MCP za početnike](https://github.com/microsoft/mcp-for-beginners)
- [AI agenti za početnike](https://github.com/microsoft/ai-agents-for-beginners)
- [Generativna umjetna inteligencija za početnike koristeći .NET](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [Generativna umjetna inteligencija za početnike koristeći JavaScript](https://github.com/microsoft/generative-ai-with-javascript)
- [Generativna umjetna inteligencija za početnike](https://github.com/microsoft/generative-ai-for-beginners)
- [Strojno učenje za početnike](https://aka.ms/ml-beginners)
- [Data Science za početnike](https://aka.ms/datascience-beginners)
- [Umjetna inteligencija za početnike](https://aka.ms/ai-beginners)
- [Kibernetička sigurnost za početnike](https://github.com/microsoft/Security-101)
- [Web razvoj za početnike](https://aka.ms/webdev-beginners)
- [IoT za početnike](https://aka.ms/iot-beginners)
- [XR razvoj za početnike](https://github.com/microsoft/xr-development-for-beginners)
- [Ovladavanje GitHub Copilotom za AI programiranje u paru](https://aka.ms/GitHubCopilotAI)
- [Ovladavanje GitHub Copilotom za C#/.NET programere](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [Odaberite vlastitu Copilot avanturu](https://github.com/microsoft/CopilotAdventures)
- [RAG aplikacija za chat s Azure AI uslugama](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za ključne informacije preporučuje se profesionalni prijevod od strane čovjeka. Ne preuzimamo odgovornost za nesporazume ili pogrešna tumačenja koja mogu proizaći iz korištenja ovog prijevoda.