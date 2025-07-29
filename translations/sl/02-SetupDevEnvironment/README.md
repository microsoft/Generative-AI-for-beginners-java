<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:29:57+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "sl"
}
-->
# Nastavitev razvojnega okolja za generativno umetno inteligenco za Javo

> **Hiter začetek**: Koda v oblaku v 2 minutah - skočite na [Nastavitev GitHub Codespaces](../../../02-SetupDevEnvironment) - brez lokalne namestitve in z uporabo GitHub modelov!

> **Zanima vas Azure OpenAI?** Oglejte si naš [Vodnik za nastavitev Azure OpenAI](getting-started-azure-openai.md) s koraki za ustvarjanje novega vira Azure OpenAI.

## Kaj se boste naučili

- Nastaviti razvojno okolje za Javo za aplikacije z umetno inteligenco
- Izbrati in konfigurirati svoje prednostno razvojno okolje (oblak s Codespaces, lokalni razvojni kontejner ali popolnoma lokalna nastavitev)
- Preizkusiti svojo nastavitev z vzpostavitvijo povezave z GitHub modeli

## Kazalo

- [Kaj se boste naučili](../../../02-SetupDevEnvironment)
- [Uvod](../../../02-SetupDevEnvironment)
- [1. korak: Nastavite svoje razvojno okolje](../../../02-SetupDevEnvironment)
  - [Možnost A: GitHub Codespaces (priporočeno)](../../../02-SetupDevEnvironment)
  - [Možnost B: Lokalni razvojni kontejner](../../../02-SetupDevEnvironment)
  - [Možnost C: Uporabite svojo obstoječo lokalno namestitev](../../../02-SetupDevEnvironment)
- [2. korak: Ustvarite osebni dostopni žeton za GitHub](../../../02-SetupDevEnvironment)
- [3. korak: Preizkusite svojo nastavitev](../../../02-SetupDevEnvironment)
- [Odpravljanje težav](../../../02-SetupDevEnvironment)
- [Povzetek](../../../02-SetupDevEnvironment)
- [Naslednji koraki](../../../02-SetupDevEnvironment)

## Uvod

To poglavje vas bo vodilo skozi nastavitev razvojnega okolja. Kot glavni primer bomo uporabili **GitHub modele**, ker so brezplačni, enostavni za nastavitev z le GitHub računom, ne zahtevajo kreditne kartice in omogočajo dostop do več modelov za eksperimentiranje.

**Brez lokalne nastavitve!** Kodiranje lahko začnete takoj z uporabo GitHub Codespaces, ki zagotavlja popolno razvojno okolje v vašem brskalniku.

<img src="./images/models.webp" alt="Posnetek zaslona: GitHub modeli" width="50%">

Priporočamo uporabo [**GitHub modelov**](https://github.com/marketplace?type=models) za ta tečaj, ker so:
- **Brezplačni** za začetek
- **Enostavni** za nastavitev z le GitHub računom
- **Brez kreditne kartice** potrebne
- **Več modelov** na voljo za eksperimentiranje

> **Opomba**: GitHub modeli, uporabljeni v tem usposabljanju, imajo naslednje brezplačne omejitve:
> - 15 zahtevkov na minuto (150 na dan)
> - ~8.000 besed vnos, ~4.000 besed iznos na zahtevo
> - 5 sočasnih zahtevkov
> 
> Za produkcijsko uporabo nadgradite na Azure AI Foundry modele z vašim Azure računom. Vaša koda se ne bo spremenila. Oglejte si [dokumentacijo Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## 1. korak: Nastavite svoje razvojno okolje

<a name="quick-start-cloud"></a>

Ustvarili smo vnaprej konfiguriran razvojni kontejner, da zmanjšamo čas nastavitve in zagotovimo, da imate vsa potrebna orodja za ta tečaj o generativni umetni inteligenci za Javo. Izberite svoj prednostni pristop k razvoju:

### Možnosti nastavitve okolja:

#### Možnost A: GitHub Codespaces (priporočeno)

**Začnite kodirati v 2 minutah - brez lokalne nastavitve!**

1. Forkajte to repozitorij v svoj GitHub račun
   > **Opomba**: Če želite urediti osnovno konfiguracijo, si oglejte [Konfiguracijo razvojnega kontejnerja](../../../.devcontainer/devcontainer.json)
2. Kliknite **Code** → zavihek **Codespaces** → **...** → **New with options...**
3. Uporabite privzete nastavitve – to bo izbralo **Konfiguracijo razvojnega kontejnerja**: **Generative AI Java Development Environment**, prilagojen razvojni kontejner, ustvarjen za ta tečaj
4. Kliknite **Create codespace**
5. Počakajte ~2 minuti, da je okolje pripravljeno
6. Nadaljujte na [2. korak: Ustvarite GitHub žeton](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Posnetek zaslona: podmeni Codespaces" width="50%">

<img src="./images/image.png" alt="Posnetek zaslona: Nova z možnostmi" width="50%">

<img src="./images/codespaces-create.png" alt="Posnetek zaslona: Možnosti ustvarjanja Codespace" width="50%">

> **Prednosti Codespaces**:
> - Brez lokalne namestitve
> - Deluje na katerikoli napravi z brskalnikom
> - Vnaprej konfigurirano z vsemi orodji in odvisnostmi
> - Brezplačno 60 ur na mesec za osebne račune
> - Dosledno okolje za vse udeležence

#### Možnost B: Lokalni razvojni kontejner

**Za razvijalce, ki imajo raje lokalni razvoj z Dockerjem**

1. Forkajte in klonirajte ta repozitorij na svojo lokalno napravo
   > **Opomba**: Če želite urediti osnovno konfiguracijo, si oglejte [Konfiguracijo razvojnega kontejnerja](../../../.devcontainer/devcontainer.json)
2. Namestite [Docker Desktop](https://www.docker.com/products/docker-desktop/) in [VS Code](https://code.visualstudio.com/)
3. Namestite razširitev [Dev Containers](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) v VS Code
4. Odprite mapo repozitorija v VS Code
5. Ko vas pozove, kliknite **Reopen in Container** (ali uporabite `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Počakajte, da se kontejner zgradi in zažene
7. Nadaljujte na [2. korak: Ustvarite GitHub žeton](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Posnetek zaslona: Nastavitev razvojnega kontejnerja" width="50%">

<img src="./images/image-3.png" alt="Posnetek zaslona: Gradnja razvojnega kontejnerja končana" width="50%">

#### Možnost C: Uporabite svojo obstoječo lokalno namestitev

**Za razvijalce z obstoječimi Java okolji**

Predpogoji:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ali vaš prednostni IDE

Koraki:
1. Klonirajte ta repozitorij na svojo lokalno napravo
2. Odprite projekt v svojem IDE
3. Nadaljujte na [2. korak: Ustvarite GitHub žeton](../../../02-SetupDevEnvironment)

> **Nasvet za profesionalce**: Če imate napravo z nizkimi specifikacijami, a želite uporabljati VS Code lokalno, uporabite GitHub Codespaces! Lokalni VS Code lahko povežete z oblačno gostovanim Codespace za najboljše iz obeh svetov.

<img src="./images/image-2.png" alt="Posnetek zaslona: Ustvarjen lokalni razvojni kontejner" width="50%">

## 2. korak: Ustvarite osebni dostopni žeton za GitHub

1. Pojdite na [GitHub Nastavitve](https://github.com/settings/profile) in izberite **Settings** iz menija svojega profila.
2. V levem stranskem meniju kliknite **Developer settings** (običajno na dnu).
3. Pod **Personal access tokens** kliknite **Fine-grained tokens** (ali sledite tej neposredni [povezavi](https://github.com/settings/personal-access-tokens)).
4. Kliknite **Generate new token**.
5. Pod "Token name" vnesite opisno ime (npr. `GenAI-Java-Course-Token`).
6. Nastavite datum poteka (priporočeno: 7 dni za najboljše varnostne prakse).
7. Pod "Resource owner" izberite svoj uporabniški račun.
8. Pod "Repository access" izberite repozitorije, ki jih želite uporabljati z GitHub modeli (ali "All repositories", če je potrebno).
9. Pod "Repository permissions" poiščite **Models** in nastavite na **Read and write**.
10. Kliknite **Generate token**.
11. **Kopirajte in shranite svoj žeton zdaj** – kasneje ga ne boste več videli!

> **Varnostni nasvet**: Uporabite najmanjši potreben obseg in najkrajši praktični čas poteka za svoje dostopne žetone.

## 3. korak: Preizkusite svojo nastavitev z GitHub Models primerom

Ko je vaše razvojno okolje pripravljeno, preizkusimo integracijo GitHub modelov z našo primer aplikacijo v [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Odprite terminal v svojem razvojnem okolju.
2. Pojdite v mapo z GitHub Models primerom:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Nastavite svoj GitHub žeton kot okoljsko spremenljivko:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Zaženite aplikacijo:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Videti bi morali izpis, podoben temu:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Razumevanje primerne kode

Najprej razložimo, kaj smo pravkar zagnali. Primer v `examples/github-models` uporablja OpenAI Java SDK za povezavo z GitHub modeli:

**Kaj ta koda počne:**
- **Vzpostavi povezavo** z GitHub modeli z uporabo vašega osebnega dostopnega žetona
- **Pošlje** preprosto sporočilo "Say Hello World!" modelu umetne inteligence
- **Prejme** in prikaže odgovor umetne inteligence
- **Preveri**, ali vaša nastavitev deluje pravilno

**Ključna odvisnost** (v `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Glavna koda** (`App.java`):
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

## Povzetek

Odlično! Zdaj imate vse nastavljeno:

- Ustvarili ste osebni dostopni žeton za GitHub z ustreznimi dovoljenji za dostop do modelov umetne inteligence
- Zagnali ste svoje razvojno okolje za Javo (naj bo to Codespaces, razvojni kontejnerji ali lokalno)
- Povezali ste se z GitHub modeli z uporabo OpenAI Java SDK za brezplačen razvoj umetne inteligence
- Preizkusili ste, da vse deluje s preprostim primerom, ki komunicira z modeli umetne inteligence

## Naslednji koraki

[Poglavje 3: Osnovne tehnike generativne umetne inteligence](../03-CoreGenerativeAITechniques/README.md)

## Odpravljanje težav

Imate težave? Tukaj so pogoste težave in rešitve:

- **Žeton ne deluje?** 
  - Prepričajte se, da ste kopirali celoten žeton brez dodatnih presledkov
  - Preverite, ali je žeton pravilno nastavljen kot okoljska spremenljivka
  - Preverite, ali ima vaš žeton pravilna dovoljenja (Models: Read and write)

- **Maven ni najden?** 
  - Če uporabljate razvojne kontejnerje/Codespaces, bi moral biti Maven že nameščen
  - Za lokalno nastavitev preverite, ali sta nameščena Java 21+ in Maven 3.9+
  - Poskusite `mvn --version`, da preverite namestitev

- **Težave s povezavo?** 
  - Preverite svojo internetno povezavo
  - Preverite, ali je GitHub dostopen iz vašega omrežja
  - Prepričajte se, da niste za požarnim zidom, ki blokira GitHub Models končno točko

- **Razvojni kontejner se ne zažene?** 
  - Prepričajte se, da Docker Desktop deluje (za lokalni razvoj)
  - Poskusite ponovno zgraditi kontejner: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Napake pri prevajanju aplikacije?**
  - Prepričajte se, da ste v pravilni mapi: `02-SetupDevEnvironment/examples/github-models`
  - Poskusite očistiti in ponovno zgraditi: `mvn clean compile`

> **Potrebujete pomoč?**: Še vedno imate težave? Odprite težavo v repozitoriju in pomagali vam bomo.

**Omejitev odgovornosti**:  
Ta dokument je bil preveden z uporabo storitve AI za prevajanje [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da lahko avtomatizirani prevodi vsebujejo napake ali netočnosti. Izvirni dokument v njegovem maternem jeziku je treba obravnavati kot avtoritativni vir. Za ključne informacije priporočamo profesionalni človeški prevod. Ne prevzemamo odgovornosti za morebitna nesporazumevanja ali napačne razlage, ki bi nastale zaradi uporabe tega prevoda.