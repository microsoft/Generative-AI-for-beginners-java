<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:33:50+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "sv"
}
-->
# Ställa in utvecklingsmiljön för Generativ AI för Java

> **Snabbstart**: Koda i molnet på 2 minuter - Hoppa till [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) - ingen lokal installation krävs och använder GitHub-modeller!

> **Intresserad av Azure OpenAI?**, se vår [Azure OpenAI Setup Guide](getting-started-azure-openai.md) med steg för att skapa en ny Azure OpenAI-resurs.

## Vad du kommer att lära dig

- Ställa in en Java-utvecklingsmiljö för AI-applikationer
- Välja och konfigurera din föredragna utvecklingsmiljö (molnbaserad med Codespaces, lokal utvecklingscontainer eller fullständig lokal installation)
- Testa din installation genom att ansluta till GitHub-modeller

## Innehållsförteckning

- [Vad du kommer att lära dig](../../../02-SetupDevEnvironment)
- [Introduktion](../../../02-SetupDevEnvironment)
- [Steg 1: Ställ in din utvecklingsmiljö](../../../02-SetupDevEnvironment)
  - [Alternativ A: GitHub Codespaces (Rekommenderas)](../../../02-SetupDevEnvironment)
  - [Alternativ B: Lokal utvecklingscontainer](../../../02-SetupDevEnvironment)
  - [Alternativ C: Använd din befintliga lokala installation](../../../02-SetupDevEnvironment)
- [Steg 2: Skapa GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Steg 3: Testa din installation](../../../02-SetupDevEnvironment)
- [Felsökning](../../../02-SetupDevEnvironment)
- [Sammanfattning](../../../02-SetupDevEnvironment)
- [Nästa steg](../../../02-SetupDevEnvironment)

## Introduktion

Detta kapitel guidar dig genom att ställa in en utvecklingsmiljö. Vi kommer att använda **GitHub-modeller** som vårt primära exempel eftersom det är gratis, enkelt att ställa in med bara ett GitHub-konto, kräver inget kreditkort och ger tillgång till flera modeller för experimentering.

**Ingen lokal installation krävs!** Du kan börja koda direkt med GitHub Codespaces, som erbjuder en komplett utvecklingsmiljö i din webbläsare.

<img src="./images/models.webp" alt="Skärmdump: GitHub-modeller" width="50%">

Vi rekommenderar att använda [**GitHub-modeller**](https://github.com/marketplace?type=models) för denna kurs eftersom det är:
- **Gratis** att komma igång
- **Enkelt** att ställa in med bara ett GitHub-konto
- **Inget kreditkort** krävs
- **Flera modeller** tillgängliga för experimentering

> **Notera**: GitHub-modellerna som används i denna utbildning har följande gratisgränser:
> - 15 förfrågningar per minut (150 per dag)
> - ~8 000 ord in, ~4 000 ord ut per förfrågan
> - 5 samtidiga förfrågningar
> 
> För produktionsanvändning, uppgradera till Azure AI Foundry-modeller med ditt Azure-konto. Din kod behöver inte ändras. Se [Azure AI Foundry-dokumentationen](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Steg 1: Ställ in din utvecklingsmiljö

<a name="quick-start-cloud"></a>

Vi har skapat en förkonfigurerad utvecklingscontainer för att minimera installationstiden och säkerställa att du har alla nödvändiga verktyg för denna kurs om Generativ AI för Java. Välj din föredragna utvecklingsmetod:

### Alternativ för miljöinställning:

#### Alternativ A: GitHub Codespaces (Rekommenderas)

**Börja koda på 2 minuter - ingen lokal installation krävs!**

1. Forka detta repository till ditt GitHub-konto
   > **Notera**: Om du vill redigera den grundläggande konfigurationen, se [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Klicka på **Code** → fliken **Codespaces** → **...** → **New with options...**
3. Använd standardinställningarna – detta kommer att välja **Dev container configuration**: **Generative AI Java Development Environment**, en anpassad devcontainer skapad för denna kurs
4. Klicka på **Create codespace**
5. Vänta ~2 minuter tills miljön är redo
6. Fortsätt till [Steg 2: Skapa GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Skärmdump: Codespaces undermeny" width="50%">

<img src="./images/image.png" alt="Skärmdump: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Skärmdump: Create codespace options" width="50%">

> **Fördelar med Codespaces**:
> - Ingen lokal installation krävs
> - Fungerar på vilken enhet som helst med en webbläsare
> - Förkonfigurerad med alla verktyg och beroenden
> - Gratis 60 timmar per månad för personliga konton
> - Konsistent miljö för alla deltagare

#### Alternativ B: Lokal utvecklingscontainer

**För utvecklare som föredrar lokal utveckling med Docker**

1. Forka och klona detta repository till din lokala dator
   > **Notera**: Om du vill redigera den grundläggande konfigurationen, se [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Installera [Docker Desktop](https://www.docker.com/products/docker-desktop/) och [VS Code](https://code.visualstudio.com/)
3. Installera [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) i VS Code
4. Öppna repository-mappen i VS Code
5. När du blir tillfrågad, klicka på **Reopen in Container** (eller använd `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Vänta tills containern byggs och startar
7. Fortsätt till [Steg 2: Skapa GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Skärmdump: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="Skärmdump: Dev container build complete" width="50%">

#### Alternativ C: Använd din befintliga lokala installation

**För utvecklare med befintliga Java-miljöer**

Förutsättningar:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) eller din föredragna IDE

Steg:
1. Klona detta repository till din lokala dator
2. Öppna projektet i din IDE
3. Fortsätt till [Steg 2: Skapa GitHub Token](../../../02-SetupDevEnvironment)

> **Proffstips**: Om du har en dator med låg prestanda men vill använda VS Code lokalt, använd GitHub Codespaces! Du kan ansluta din lokala VS Code till en molnbaserad Codespace för det bästa av två världar.

<img src="./images/image-2.png" alt="Skärmdump: skapad lokal devcontainer-instans" width="50%">

## Steg 2: Skapa en GitHub Personal Access Token

1. Navigera till [GitHub Settings](https://github.com/settings/profile) och välj **Settings** från din profilmeny.
2. I vänstra sidofältet, klicka på **Developer settings** (vanligtvis längst ner).
3. Under **Personal access tokens**, klicka på **Fine-grained tokens** (eller följ denna direkta [länk](https://github.com/settings/personal-access-tokens)).
4. Klicka på **Generate new token**.
5. Under "Token name", ge ett beskrivande namn (t.ex. `GenAI-Java-Course-Token`).
6. Ställ in ett utgångsdatum (rekommenderat: 7 dagar för bästa säkerhetspraxis).
7. Under "Resource owner", välj ditt användarkonto.
8. Under "Repository access", välj de repositories du vill använda med GitHub-modeller (eller "All repositories" om det behövs).
9. Under "Repository permissions", hitta **Models** och ställ in det på **Read and write**.
10. Klicka på **Generate token**.
11. **Kopiera och spara din token nu** – du kommer inte att se den igen!

> **Säkerhetstips**: Använd den minsta nödvändiga omfattningen och kortaste praktiska utgångstiden för dina åtkomsttokens.

## Steg 3: Testa din installation med GitHub-modeller-exemplet

När din utvecklingsmiljö är redo, låt oss testa GitHub-modeller-integrationen med vårt exempelprogram i [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Öppna terminalen i din utvecklingsmiljö.
2. Navigera till GitHub-modeller-exemplet:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Ställ in din GitHub-token som en miljövariabel:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Kör programmet:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Du bör se en utdata som liknar:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Förstå exempelprogrammet

Låt oss först förstå vad vi just körde. Exemplet under `examples/github-models` använder OpenAI Java SDK för att ansluta till GitHub-modeller:

**Vad denna kod gör:**
- **Ansluter** till GitHub-modeller med din personliga åtkomsttoken
- **Skickar** ett enkelt "Say Hello World!"-meddelande till AI-modellen
- **Tar emot** och visar AI:s svar
- **Validerar** att din installation fungerar korrekt

**Viktigt beroende** (i `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Huvudkod** (`App.java`):
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

## Sammanfattning

Bra jobbat! Du har nu allt uppsatt:

- Skapat en GitHub Personal Access Token med rätt behörigheter för AI-modellåtkomst
- Fått din Java-utvecklingsmiljö att fungera (oavsett om det är Codespaces, utvecklingscontainers eller lokalt)
- Anslutit till GitHub-modeller med OpenAI Java SDK för gratis AI-utveckling
- Testat att allt fungerar med ett enkelt exempel som pratar med AI-modeller

## Nästa steg

[Kapitel 3: Grundläggande tekniker för generativ AI](../03-CoreGenerativeAITechniques/README.md)

## Felsökning

Har du problem? Här är vanliga problem och lösningar:

- **Token fungerar inte?** 
  - Kontrollera att du kopierade hela token utan extra mellanslag
  - Verifiera att token är korrekt inställd som en miljövariabel
  - Kontrollera att din token har rätt behörigheter (Models: Read and write)

- **Maven hittas inte?** 
  - Om du använder utvecklingscontainers/Codespaces, bör Maven vara förinstallerat
  - För lokal installation, säkerställ att Java 21+ och Maven 3.9+ är installerade
  - Testa `mvn --version` för att verifiera installationen

- **Anslutningsproblem?** 
  - Kontrollera din internetanslutning
  - Verifiera att GitHub är tillgängligt från ditt nätverk
  - Säkerställ att du inte är bakom en brandvägg som blockerar GitHub-modeller-slutpunkten

- **Utvecklingscontainer startar inte?** 
  - Kontrollera att Docker Desktop körs (för lokal utveckling)
  - Testa att bygga om containern: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Kompileringsfel i applikationen?**
  - Kontrollera att du är i rätt katalog: `02-SetupDevEnvironment/examples/github-models`
  - Testa att rensa och bygga om: `mvn clean compile`

> **Behöver hjälp?**: Har du fortfarande problem? Öppna ett ärende i repositoryt så hjälper vi dig.

**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiserade översättningar kan innehålla fel eller felaktigheter. Det ursprungliga dokumentet på dess originalspråk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.