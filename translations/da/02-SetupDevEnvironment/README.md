<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:37:05+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "da"
}
-->
# Opsætning af udviklingsmiljø til Generativ AI for Java

> **Hurtig start**: Kod i skyen på 2 minutter - Gå til [GitHub Codespaces Opsætning](../../../02-SetupDevEnvironment) - ingen lokal installation nødvendig og bruger GitHub-modeller!

> **Interesseret i Azure OpenAI?**, se vores [Azure OpenAI Opsætningsguide](getting-started-azure-openai.md) med trin til at oprette en ny Azure OpenAI-ressource.

## Hvad Du Vil Lære

- Opsæt et Java-udviklingsmiljø til AI-applikationer
- Vælg og konfigurer dit foretrukne udviklingsmiljø (cloud-først med Codespaces, lokal dev-container eller fuld lokal opsætning)
- Test din opsætning ved at forbinde til GitHub-modeller

## Indholdsfortegnelse

- [Hvad Du Vil Lære](../../../02-SetupDevEnvironment)
- [Introduktion](../../../02-SetupDevEnvironment)
- [Trin 1: Opsæt Dit Udviklingsmiljø](../../../02-SetupDevEnvironment)
  - [Mulighed A: GitHub Codespaces (Anbefalet)](../../../02-SetupDevEnvironment)
  - [Mulighed B: Lokal Dev-Container](../../../02-SetupDevEnvironment)
  - [Mulighed C: Brug Din Eksisterende Lokale Installation](../../../02-SetupDevEnvironment)
- [Trin 2: Opret GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Trin 3: Test Din Opsætning](../../../02-SetupDevEnvironment)
- [Fejlfinding](../../../02-SetupDevEnvironment)
- [Opsummering](../../../02-SetupDevEnvironment)
- [Næste Skridt](../../../02-SetupDevEnvironment)

## Introduktion

Dette kapitel guider dig gennem opsætningen af et udviklingsmiljø. Vi bruger **GitHub-modeller** som vores primære eksempel, fordi det er gratis, nemt at opsætte med kun en GitHub-konto, kræver ikke et kreditkort og giver adgang til flere modeller til eksperimentering.

**Ingen lokal opsætning nødvendig!** Du kan starte med at kode med det samme ved at bruge GitHub Codespaces, som giver et fuldt udviklingsmiljø i din browser.

<img src="./images/models.webp" alt="Skærmbillede: GitHub-modeller" width="50%">

Vi anbefaler at bruge [**GitHub-modeller**](https://github.com/marketplace?type=models) til dette kursus, fordi det er:
- **Gratis** at komme i gang
- **Nemt** at opsætte med kun en GitHub-konto
- **Ingen kreditkort** kræves
- **Flere modeller** tilgængelige til eksperimentering

> **Bemærk**: GitHub-modellerne, der bruges i denne træning, har følgende gratis grænser:
> - 15 forespørgsler pr. minut (150 pr. dag)
> - ~8.000 ord ind, ~4.000 ord ud pr. forespørgsel
> - 5 samtidige forespørgsler
> 
> Til produktionsbrug, opgrader til Azure AI Foundry-modeller med din Azure-konto. Din kode behøver ikke ændres. Se [Azure AI Foundry-dokumentationen](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Trin 1: Opsæt Dit Udviklingsmiljø

<a name="quick-start-cloud"></a>

Vi har oprettet en forudkonfigureret udviklingscontainer for at minimere opsætningstiden og sikre, at du har alle nødvendige værktøjer til dette Generativ AI for Java-kursus. Vælg din foretrukne udviklingsmetode:

### Miljøopsætningsmuligheder:

#### Mulighed A: GitHub Codespaces (Anbefalet)

**Start med at kode på 2 minutter - ingen lokal opsætning nødvendig!**

1. Fork denne repository til din GitHub-konto
   > **Bemærk**: Hvis du vil redigere den grundlæggende konfiguration, kan du kigge på [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Klik på **Code** → **Codespaces**-fanen → **...** → **New with options...**
3. Brug standardindstillingerne – dette vælger **Dev container configuration**: **Generative AI Java Development Environment**, en brugerdefineret devcontainer oprettet til dette kursus
4. Klik på **Create codespace**
5. Vent ~2 minutter på, at miljøet er klar
6. Fortsæt til [Trin 2: Opret GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Skærmbillede: Codespaces undermenu" width="50%">

<img src="./images/image.png" alt="Skærmbillede: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Skærmbillede: Opret codespace muligheder" width="50%">

> **Fordele ved Codespaces**:
> - Ingen lokal installation nødvendig
> - Fungerer på enhver enhed med en browser
> - Forudkonfigureret med alle værktøjer og afhængigheder
> - Gratis 60 timer pr. måned for personlige konti
> - Konsistent miljø for alle deltagere

#### Mulighed B: Lokal Dev-Container

**For udviklere, der foretrækker lokal udvikling med Docker**

1. Fork og klon denne repository til din lokale maskine
   > **Bemærk**: Hvis du vil redigere den grundlæggende konfiguration, kan du kigge på [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Installer [Docker Desktop](https://www.docker.com/products/docker-desktop/) og [VS Code](https://code.visualstudio.com/)
3. Installer [Dev Containers-udvidelsen](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) i VS Code
4. Åbn repository-mappen i VS Code
5. Når du bliver bedt om det, klik på **Reopen in Container** (eller brug `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Vent på, at containeren bygges og starter
7. Fortsæt til [Trin 2: Opret GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Skærmbillede: Dev container opsætning" width="50%">

<img src="./images/image-3.png" alt="Skærmbillede: Dev container bygning færdig" width="50%">

#### Mulighed C: Brug Din Eksisterende Lokale Installation

**For udviklere med eksisterende Java-miljøer**

Forudsætninger:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) eller din foretrukne IDE

Trin:
1. Klon denne repository til din lokale maskine
2. Åbn projektet i din IDE
3. Fortsæt til [Trin 2: Opret GitHub Token](../../../02-SetupDevEnvironment)

> **Pro Tip**: Hvis du har en maskine med lav ydeevne, men ønsker VS Code lokalt, brug GitHub Codespaces! Du kan forbinde din lokale VS Code til en cloud-hostet Codespace for det bedste fra begge verdener.

<img src="./images/image-2.png" alt="Skærmbillede: oprettet lokal devcontainer-instans" width="50%">

## Trin 2: Opret GitHub Personal Access Token

1. Gå til [GitHub Indstillinger](https://github.com/settings/profile) og vælg **Settings** fra din profilmenu.
2. I venstre sidebjælke, klik på **Developer settings** (normalt nederst).
3. Under **Personal access tokens**, klik på **Fine-grained tokens** (eller følg dette direkte [link](https://github.com/settings/personal-access-tokens)).
4. Klik på **Generate new token**.
5. Under "Token name", angiv et beskrivende navn (f.eks. `GenAI-Java-Course-Token`).
6. Angiv en udløbsdato (anbefalet: 7 dage for bedste sikkerhedspraksis).
7. Under "Resource owner", vælg din brugerkonto.
8. Under "Repository access", vælg de repositories, du vil bruge med GitHub-modeller (eller "All repositories", hvis nødvendigt).
9. Under "Repository permissions", find **Models** og sæt det til **Read and write**.
10. Klik på **Generate token**.
11. **Kopiér og gem dit token nu** – du vil ikke kunne se det igen!

> **Sikkerhedstip**: Brug det minimum nødvendige omfang og den kortest praktiske udløbstid for dine adgangstokens.

## Trin 3: Test Din Opsætning med GitHub-modeller Eksemplet

Når dit udviklingsmiljø er klar, lad os teste GitHub-modeller integrationen med vores eksempelapplikation i [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Åbn terminalen i dit udviklingsmiljø.
2. Naviger til GitHub-modeller eksemplet:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Sæt dit GitHub-token som en miljøvariabel:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Kør applikationen:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Du bør se output, der ligner:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Forstå Eksempelkoden

Lad os først forstå, hvad vi lige har kørt. Eksemplet under `examples/github-models` bruger OpenAI Java SDK til at forbinde til GitHub-modeller:

**Hvad denne kode gør:**
- **Forbinder** til GitHub-modeller ved hjælp af dit personlige adgangstoken
- **Sender** en simpel "Say Hello World!" besked til AI-modellen
- **Modtager** og viser AI'ens svar
- **Validerer**, at din opsætning fungerer korrekt

**Nøgleafhængighed** (i `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Hovedkode** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## Opsummering

Fantastisk! Du har nu alt opsat:

- Oprettet et GitHub Personal Access Token med de rette tilladelser til AI-modeladgang
- Fået dit Java-udviklingsmiljø op at køre (uanset om det er Codespaces, dev-containere eller lokalt)
- Forbundet til GitHub-modeller ved hjælp af OpenAI Java SDK til gratis AI-udvikling
- Testet, at det hele fungerer med et simpelt eksempel, der kommunikerer med AI-modeller

## Næste Skridt

[Kapitel 3: Kerne Generative AI Teknikker](../03-CoreGenerativeAITechniques/README.md)

## Fejlfinding

Har du problemer? Her er almindelige problemer og løsninger:

- **Token virker ikke?** 
  - Sørg for, at du kopierede hele tokenet uden ekstra mellemrum
  - Bekræft, at tokenet er korrekt sat som en miljøvariabel
  - Tjek, at dit token har de korrekte tilladelser (Models: Read and write)

- **Maven ikke fundet?** 
  - Hvis du bruger dev-containere/Codespaces, bør Maven være forudinstalleret
  - Til lokal opsætning, sørg for, at Java 21+ og Maven 3.9+ er installeret
  - Prøv `mvn --version` for at bekræfte installationen

- **Forbindelsesproblemer?** 
  - Tjek din internetforbindelse
  - Bekræft, at GitHub er tilgængelig fra dit netværk
  - Sørg for, at du ikke er bag en firewall, der blokerer GitHub-modeller endpoint

- **Dev-container starter ikke?** 
  - Sørg for, at Docker Desktop kører (til lokal udvikling)
  - Prøv at genopbygge containeren: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Applikationskompileringsfejl?**
  - Sørg for, at du er i den korrekte mappe: `02-SetupDevEnvironment/examples/github-models`
  - Prøv at rense og genopbygge: `mvn clean compile`

> **Brug for hjælp?**: Har du stadig problemer? Opret en issue i repositoryet, så hjælper vi dig.

**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os ikke ansvar for eventuelle misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.