<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T15:46:42+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "nl"
}
-->
# Het Instellen van de Ontwikkelomgeving voor Generatieve AI met Java

> **Snelle Start**: Codeer in de cloud in 2 minuten - Ga naar [GitHub Codespaces Setup](../../../02-SetupDevEnvironment) - geen lokale installatie nodig en maakt gebruik van GitHub-modellen!

> **Geïnteresseerd in Azure OpenAI?** Bekijk onze [Azure OpenAI Setup Gids](getting-started-azure-openai.md) met stappen om een nieuwe Azure OpenAI-resource te maken.

## Wat Je Gaat Leren

- Een Java-ontwikkelomgeving instellen voor AI-toepassingen
- Je voorkeursontwikkelomgeving kiezen en configureren (cloud-first met Codespaces, lokale dev-container, of volledige lokale setup)
- Je setup testen door verbinding te maken met GitHub-modellen

## Inhoudsopgave

- [Wat Je Gaat Leren](../../../02-SetupDevEnvironment)
- [Introductie](../../../02-SetupDevEnvironment)
- [Stap 1: Stel Je Ontwikkelomgeving In](../../../02-SetupDevEnvironment)
  - [Optie A: GitHub Codespaces (Aanbevolen)](../../../02-SetupDevEnvironment)
  - [Optie B: Lokale Dev-container](../../../02-SetupDevEnvironment)
  - [Optie C: Gebruik Je Bestaande Lokale Installatie](../../../02-SetupDevEnvironment)
- [Stap 2: Maak een GitHub Personal Access Token](../../../02-SetupDevEnvironment)
- [Stap 3: Test Je Setup](../../../02-SetupDevEnvironment)
- [Problemen Oplossen](../../../02-SetupDevEnvironment)
- [Samenvatting](../../../02-SetupDevEnvironment)
- [Volgende Stappen](../../../02-SetupDevEnvironment)

## Introductie

In dit hoofdstuk begeleiden we je bij het instellen van een ontwikkelomgeving. We gebruiken **GitHub-modellen** als ons primaire voorbeeld omdat het gratis is, eenvoudig in te stellen met alleen een GitHub-account, geen creditcard vereist, en toegang biedt tot meerdere modellen om mee te experimenteren.

**Geen lokale setup nodig!** Je kunt direct beginnen met coderen via GitHub Codespaces, dat een volledige ontwikkelomgeving in je browser biedt.

<img src="./images/models.webp" alt="Screenshot: GitHub-modellen" width="50%">

We raden aan om [**GitHub-modellen**](https://github.com/marketplace?type=models) te gebruiken voor deze cursus omdat het:
- **Gratis** is om te starten
- **Eenvoudig** is in te stellen met alleen een GitHub-account
- **Geen creditcard** vereist
- **Meerdere modellen** biedt om mee te experimenteren

> **Opmerking**: De GitHub-modellen die in deze training worden gebruikt, hebben de volgende gratis limieten:
> - 15 verzoeken per minuut (150 per dag)
> - ~8.000 woorden in, ~4.000 woorden uit per verzoek
> - 5 gelijktijdige verzoeken
> 
> Voor productiegebruik kun je upgraden naar Azure AI Foundry-modellen met je Azure-account. Je code hoeft niet te worden aangepast. Zie de [Azure AI Foundry-documentatie](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Stap 1: Stel Je Ontwikkelomgeving In

<a name="quick-start-cloud"></a>

We hebben een vooraf geconfigureerde ontwikkelcontainer gemaakt om de installatietijd te minimaliseren en ervoor te zorgen dat je alle benodigde tools hebt voor deze Generatieve AI-cursus met Java. Kies je voorkeursontwikkelbenadering:

### Opties voor Omgevingsinstelling:

#### Optie A: GitHub Codespaces (Aanbevolen)

**Begin met coderen in 2 minuten - geen lokale setup nodig!**

1. Fork deze repository naar je GitHub-account  
   > **Opmerking**: Als je de basisconfiguratie wilt aanpassen, bekijk dan de [Dev Container Configuratie](../../../.devcontainer/devcontainer.json)
2. Klik op **Code** → tabblad **Codespaces** → **...** → **Nieuw met opties...**
3. Gebruik de standaardinstellingen – dit selecteert de **Dev-containerconfiguratie**: **Generative AI Java Development Environment**, een aangepaste devcontainer gemaakt voor deze cursus
4. Klik op **Codespace maken**
5. Wacht ~2 minuten tot de omgeving klaar is
6. Ga verder naar [Stap 2: Maak GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Screenshot: Codespaces submenu" width="50%">

<img src="./images/image.png" alt="Screenshot: Nieuw met opties" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Codespace maken opties" width="50%">

> **Voordelen van Codespaces**:
> - Geen lokale installatie nodig
> - Werkt op elk apparaat met een browser
> - Vooraf geconfigureerd met alle tools en afhankelijkheden
> - Gratis 60 uur per maand voor persoonlijke accounts
> - Consistente omgeving voor alle deelnemers

#### Optie B: Lokale Dev-container

**Voor ontwikkelaars die de voorkeur geven aan lokale ontwikkeling met Docker**

1. Fork en clone deze repository naar je lokale machine  
   > **Opmerking**: Als je de basisconfiguratie wilt aanpassen, bekijk dan de [Dev Container Configuratie](../../../.devcontainer/devcontainer.json)
2. Installeer [Docker Desktop](https://www.docker.com/products/docker-desktop/) en [VS Code](https://code.visualstudio.com/)
3. Installeer de [Dev Containers-extensie](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) in VS Code
4. Open de repositorymap in VS Code
5. Wanneer daarom wordt gevraagd, klik op **Opnieuw openen in Container** (of gebruik `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Wacht tot de container is gebouwd en gestart
7. Ga verder naar [Stap 2: Maak GitHub Token](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Screenshot: Dev-container setup" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev-container build voltooid" width="50%">

#### Optie C: Gebruik Je Bestaande Lokale Installatie

**Voor ontwikkelaars met bestaande Java-omgevingen**

Vereisten:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) of je favoriete IDE

Stappen:
1. Clone deze repository naar je lokale machine
2. Open het project in je IDE
3. Ga verder naar [Stap 2: Maak GitHub Token](../../../02-SetupDevEnvironment)

> **Pro Tip**: Als je een computer met lage specificaties hebt maar toch lokaal VS Code wilt gebruiken, gebruik dan GitHub Codespaces! Je kunt je lokale VS Code verbinden met een cloud-hosted Codespace voor het beste van beide werelden.

<img src="./images/image-2.png" alt="Screenshot: lokaal devcontainer exemplaar gemaakt" width="50%">

## Stap 2: Maak een GitHub Personal Access Token

1. Ga naar [GitHub Instellingen](https://github.com/settings/profile) en selecteer **Instellingen** in je profielmenu.
2. Klik in de linkerzijbalk op **Developer settings** (meestal onderaan).
3. Onder **Personal access tokens**, klik op **Fine-grained tokens** (of volg deze directe [link](https://github.com/settings/personal-access-tokens)).
4. Klik op **Genereer nieuw token**.
5. Geef onder "Token name" een beschrijvende naam op (bijv. `GenAI-Java-Course-Token`).
6. Stel een vervaldatum in (aanbevolen: 7 dagen voor beveiligingsbest practices).
7. Onder "Resource owner", selecteer je gebruikersaccount.
8. Onder "Repository access", selecteer de repositories die je wilt gebruiken met GitHub-modellen (of "Alle repositories" indien nodig).
9. Onder "Repository permissions", zoek **Models** en stel deze in op **Lezen en schrijven**.
10. Klik op **Genereer token**.
11. **Kopieer en sla je token nu op** – je ziet het niet opnieuw!

> **Beveiligingstip**: Gebruik de minimaal vereiste scope en de kortst mogelijke vervaltijd voor je toegangstokens.

## Stap 3: Test Je Setup met het GitHub-modellen Voorbeeld

Zodra je ontwikkelomgeving klaar is, testen we de GitHub-modellenintegratie met onze voorbeeldapplicatie in [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Open de terminal in je ontwikkelomgeving.
2. Navigeer naar het GitHub-modellenvoorbeeld:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Stel je GitHub-token in als een omgevingsvariabele:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Voer de applicatie uit:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Je zou een uitvoer moeten zien die lijkt op:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Het Voorbeeld Begrijpen

Laten we eerst begrijpen wat we zojuist hebben uitgevoerd. Het voorbeeld in `examples/github-models` gebruikt de OpenAI Java SDK om verbinding te maken met GitHub-modellen:

**Wat deze code doet:**
- **Maakt verbinding** met GitHub-modellen met behulp van je persoonlijke toegangstoken
- **Stuurt** een eenvoudige "Say Hello World!"-boodschap naar het AI-model
- **Ontvangt** en toont de reactie van de AI
- **Valideert** dat je setup correct werkt

**Belangrijke Afhankelijkheid** (in `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Hoofdcode** (`App.java`):
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

## Samenvatting

Goed gedaan! Je hebt nu alles ingesteld:

- Een GitHub Personal Access Token gemaakt met de juiste rechten voor AI-modeltoegang
- Je Java-ontwikkelomgeving draaiend gekregen (of dat nu Codespaces, dev-containers of lokaal is)
- Verbinding gemaakt met GitHub-modellen met behulp van de OpenAI Java SDK voor gratis AI-ontwikkeling
- Alles getest met een eenvoudig voorbeeld dat communiceert met AI-modellen

## Volgende Stappen

[Hoofdstuk 3: Kerntechnieken voor Generatieve AI](../03-CoreGenerativeAITechniques/README.md)

## Problemen Oplossen

Heb je problemen? Hier zijn veelvoorkomende problemen en oplossingen:

- **Token werkt niet?** 
  - Zorg ervoor dat je het volledige token hebt gekopieerd zonder extra spaties
  - Controleer of het token correct is ingesteld als een omgevingsvariabele
  - Controleer of je token de juiste rechten heeft (Models: Lezen en schrijven)

- **Maven niet gevonden?** 
  - Als je dev-containers/Codespaces gebruikt, zou Maven vooraf geïnstalleerd moeten zijn
  - Voor lokale setup, zorg ervoor dat Java 21+ en Maven 3.9+ zijn geïnstalleerd
  - Probeer `mvn --version` om de installatie te verifiëren

- **Verbindingsproblemen?** 
  - Controleer je internetverbinding
  - Controleer of GitHub toegankelijk is vanaf je netwerk
  - Zorg ervoor dat je niet achter een firewall zit die het GitHub-modellen eindpunt blokkeert

- **Dev-container start niet?** 
  - Zorg ervoor dat Docker Desktop draait (voor lokale ontwikkeling)
  - Probeer de container opnieuw te bouwen: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Applicatie compileerfouten?**
  - Zorg ervoor dat je in de juiste map zit: `02-SetupDevEnvironment/examples/github-models`
  - Probeer schoon te maken en opnieuw te bouwen: `mvn clean compile`

> **Hulp nodig?**: Nog steeds problemen? Open een issue in de repository en we helpen je verder.

**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, willen we u erop wijzen dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.