<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:40:11+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "no"
}
-->
# Sette opp utviklingsmiljø for Generativ AI for Java

> **Rask start**: Kode i skyen på 2 minutter - Gå til [GitHub Codespaces-oppsett](../../../02-SetupDevEnvironment) - ingen lokal installasjon nødvendig og bruker GitHub-modeller!

> **Interessert i Azure OpenAI?** Se vår [Azure OpenAI-oppsettguide](getting-started-azure-openai.md) med trinn for å opprette en ny Azure OpenAI-ressurs.

## Hva du vil lære

- Sette opp et Java-utviklingsmiljø for AI-applikasjoner
- Velge og konfigurere ditt foretrukne utviklingsmiljø (skybasert med Codespaces, lokal dev-container eller full lokal installasjon)
- Teste oppsettet ditt ved å koble til GitHub-modeller

## Innholdsfortegnelse

- [Hva du vil lære](../../../02-SetupDevEnvironment)
- [Introduksjon](../../../02-SetupDevEnvironment)
- [Trinn 1: Sett opp utviklingsmiljøet ditt](../../../02-SetupDevEnvironment)
  - [Alternativ A: GitHub Codespaces (Anbefalt)](../../../02-SetupDevEnvironment)
  - [Alternativ B: Lokal dev-container](../../../02-SetupDevEnvironment)
  - [Alternativ C: Bruk din eksisterende lokale installasjon](../../../02-SetupDevEnvironment)
- [Trinn 2: Opprett GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Trinn 3: Test oppsettet ditt](../../../02-SetupDevEnvironment)
- [Feilsøking](../../../02-SetupDevEnvironment)
- [Oppsummering](../../../02-SetupDevEnvironment)
- [Neste steg](../../../02-SetupDevEnvironment)

## Introduksjon

Dette kapittelet veileder deg gjennom oppsettet av et utviklingsmiljø. Vi bruker **GitHub-modeller** som vårt primære eksempel fordi det er gratis, enkelt å sette opp med bare en GitHub-konto, krever ingen kredittkort og gir tilgang til flere modeller for eksperimentering.

**Ingen lokal installasjon nødvendig!** Du kan begynne å kode umiddelbart ved å bruke GitHub Codespaces, som gir et fullt utviklingsmiljø i nettleseren din.

<img src="./images/models.webp" alt="Skjermbilde: GitHub-modeller" width="50%">

Vi anbefaler å bruke [**GitHub-modeller**](https://github.com/marketplace?type=models) for dette kurset fordi det er:
- **Gratis** å komme i gang
- **Enkelt** å sette opp med bare en GitHub-konto
- **Ingen kredittkort** nødvendig
- **Flere modeller** tilgjengelig for eksperimentering

> **Merk**: GitHub-modellene som brukes i denne opplæringen har følgende gratis begrensninger:
> - 15 forespørsler per minutt (150 per dag)
> - ~8 000 ord inn, ~4 000 ord ut per forespørsel
> - 5 samtidige forespørsler
> 
> For produksjonsbruk, oppgrader til Azure AI Foundry-modeller med din Azure-konto. Koden din trenger ikke å endres. Se [Azure AI Foundry-dokumentasjonen](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Trinn 1: Sett opp utviklingsmiljøet ditt

<a name="quick-start-cloud"></a>

Vi har laget en forhåndskonfigurert utviklingscontainer for å minimere oppsettstiden og sikre at du har alle nødvendige verktøy for dette Generativ AI for Java-kurset. Velg din foretrukne utviklingsmetode:

### Miljøoppsettalternativer:

#### Alternativ A: GitHub Codespaces (Anbefalt)

**Begynn å kode på 2 minutter - ingen lokal installasjon nødvendig!**

1. Fork dette repositoriet til din GitHub-konto
   > **Merk**: Hvis du vil redigere grunnleggende konfigurasjon, se [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Klikk **Code** → **Codespaces**-fanen → **...** → **New with options...**
3. Bruk standardinnstillingene – dette vil velge **Dev container configuration**: **Generative AI Java Development Environment**, en tilpasset devcontainer laget for dette kurset
4. Klikk **Create codespace**
5. Vent ~2 minutter til miljøet er klart
6. Gå videre til [Trinn 2: Opprett GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Skjermbilde: Codespaces undermeny" width="50%">

<img src="./images/image.png" alt="Skjermbilde: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Skjermbilde: Create codespace options" width="50%">

> **Fordeler med Codespaces**:
> - Ingen lokal installasjon nødvendig
> - Fungerer på alle enheter med en nettleser
> - Forhåndskonfigurert med alle verktøy og avhengigheter
> - Gratis 60 timer per måned for personlige kontoer
> - Konsistent miljø for alle deltakere

#### Alternativ B: Lokal dev-container

**For utviklere som foretrekker lokal utvikling med Docker**

1. Fork og klon dette repositoriet til din lokale maskin
   > **Merk**: Hvis du vil redigere grunnleggende konfigurasjon, se [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Installer [Docker Desktop](https://www.docker.com/products/docker-desktop/) og [VS Code](https://code.visualstudio.com/)
3. Installer [Dev Containers-utvidelsen](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) i VS Code
4. Åpne repositoriets mappe i VS Code
5. Når du blir spurt, klikk **Reopen in Container** (eller bruk `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Vent til containeren bygges og starter
7. Gå videre til [Trinn 2: Opprett GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Skjermbilde: Dev container-oppsett" width="50%">

<img src="./images/image-3.png" alt="Skjermbilde: Dev container fullført bygging" width="50%">

#### Alternativ C: Bruk din eksisterende lokale installasjon

**For utviklere med eksisterende Java-miljøer**

Forutsetninger:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) eller din foretrukne IDE

Trinn:
1. Klon dette repositoriet til din lokale maskin
2. Åpne prosjektet i din IDE
3. Gå videre til [Trinn 2: Opprett GitHub Token](../../../02-SetupDevEnvironment)

> **Proff-tips**: Hvis du har en maskin med lav ytelse, men ønsker VS Code lokalt, bruk GitHub Codespaces! Du kan koble din lokale VS Code til en skybasert Codespace for det beste fra begge verdener.

<img src="./images/image-2.png" alt="Skjermbilde: opprettet lokal devcontainer-instans" width="50%">

## Trinn 2: Opprett GitHub Personal Access Token

1. Gå til [GitHub-innstillinger](https://github.com/settings/profile) og velg **Settings** fra profilmenyen.
2. I venstre sidefelt, klikk **Developer settings** (vanligvis nederst).
3. Under **Personal access tokens**, klikk **Fine-grained tokens** (eller følg denne direkte [lenken](https://github.com/settings/personal-access-tokens)).
4. Klikk **Generate new token**.
5. Under "Token name", gi et beskrivende navn (f.eks. `GenAI-Java-Course-Token`).
6. Sett en utløpsdato (anbefalt: 7 dager for sikkerhetsmessige beste praksiser).
7. Under "Resource owner", velg brukerkontoen din.
8. Under "Repository access", velg repositoriene du vil bruke med GitHub-modeller (eller "All repositories" hvis nødvendig).
9. Under "Repository permissions", finn **Models** og sett det til **Read and write**.
10. Klikk **Generate token**.
11. **Kopier og lagre tokenet ditt nå** – du vil ikke se det igjen!

> **Sikkerhetstips**: Bruk minimum nødvendig omfang og kortest mulig utløpstid for tilgangstokenene dine.

## Trinn 3: Test oppsettet ditt med GitHub-modell-eksempelet

Når utviklingsmiljøet ditt er klart, la oss teste GitHub-modellintegrasjonen med vårt eksempelprogram i [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Åpne terminalen i utviklingsmiljøet ditt.
2. Naviger til GitHub-modell-eksempelet:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Sett GitHub-tokenet ditt som en miljøvariabel:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Kjør programmet:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Du bør se utdata som ligner på:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Forstå eksempelkoden

La oss først forstå hva vi nettopp kjørte. Eksempelet under `examples/github-models` bruker OpenAI Java SDK for å koble til GitHub-modeller:

**Hva denne koden gjør:**
- **Kobler til** GitHub-modeller ved hjelp av ditt personlige tilgangstoken
- **Sender** en enkel "Say Hello World!"-melding til AI-modellen
- **Mottar** og viser AI-ens svar
- **Validerer** at oppsettet ditt fungerer som det skal

**Nøkkelavhengighet** (i `pom.xml`):
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

## Oppsummering

Flott! Du har nå alt satt opp:

- Opprettet et GitHub Personal Access Token med riktige tillatelser for AI-modelltilgang
- Fått Java-utviklingsmiljøet ditt i gang (enten det er Codespaces, dev-containere eller lokalt)
- Knyttet til GitHub-modeller ved hjelp av OpenAI Java SDK for gratis AI-utvikling
- Testet at alt fungerer med et enkelt eksempel som kommuniserer med AI-modeller

## Neste steg

[Kapittel 3: Kjerne-teknikker for Generativ AI](../03-CoreGenerativeAITechniques/README.md)

## Feilsøking

Har du problemer? Her er vanlige problemer og løsninger:

- **Token fungerer ikke?** 
  - Sørg for at du kopierte hele tokenet uten ekstra mellomrom
  - Verifiser at tokenet er riktig satt som en miljøvariabel
  - Sjekk at tokenet ditt har riktige tillatelser (Models: Read and write)

- **Maven ikke funnet?** 
  - Hvis du bruker dev-containere/Codespaces, bør Maven være forhåndsinstallert
  - For lokal installasjon, sørg for at Java 21+ og Maven 3.9+ er installert
  - Prøv `mvn --version` for å verifisere installasjonen

- **Tilkoblingsproblemer?** 
  - Sjekk internettforbindelsen din
  - Verifiser at GitHub er tilgjengelig fra nettverket ditt
  - Sørg for at du ikke er bak en brannmur som blokkerer GitHub-modellens endepunkt

- **Dev-container starter ikke?** 
  - Sørg for at Docker Desktop kjører (for lokal utvikling)
  - Prøv å bygge containeren på nytt: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Kompileringsfeil i applikasjonen?**
  - Sørg for at du er i riktig mappe: `02-SetupDevEnvironment/examples/github-models`
  - Prøv å rense og bygge på nytt: `mvn clean compile`

> **Trenger hjelp?**: Har du fortsatt problemer? Åpne en issue i repositoriet, så hjelper vi deg.

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi tilstreber nøyaktighet, vennligst vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.