<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:26:56+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "hr"
}
-->
# Postavljanje razvojnog okruženja za Generativnu AI za Javu

> **Brzi početak**: Kodirajte u oblaku za 2 minute - prijeđite na [Postavljanje GitHub Codespaces](../../../02-SetupDevEnvironment) - nije potrebna lokalna instalacija i koristi GitHub modele!

> **Zanima vas Azure OpenAI?**, pogledajte naš [Vodič za postavljanje Azure OpenAI](getting-started-azure-openai.md) sa koracima za kreiranje novog Azure OpenAI resursa.

## Što ćete naučiti

- Postaviti razvojno okruženje za AI aplikacije u Javi
- Odabrati i konfigurirati preferirano razvojno okruženje (pristup oblaku s Codespaces, lokalni razvojni kontejner ili potpuno lokalno postavljanje)
- Testirati postavke povezivanjem s GitHub modelima

## Sadržaj

- [Što ćete naučiti](../../../02-SetupDevEnvironment)
- [Uvod](../../../02-SetupDevEnvironment)
- [Korak 1: Postavljanje razvojnog okruženja](../../../02-SetupDevEnvironment)
  - [Opcija A: GitHub Codespaces (Preporučeno)](../../../02-SetupDevEnvironment)
  - [Opcija B: Lokalni razvojni kontejner](../../../02-SetupDevEnvironment)
  - [Opcija C: Koristite postojeću lokalnu instalaciju](../../../02-SetupDevEnvironment)
- [Korak 2: Kreiranje GitHub osobnog pristupnog tokena](../../../02-SetupDevEnvironment)
- [Korak 3: Testiranje postavki](../../../02-SetupDevEnvironment)
- [Rješavanje problema](../../../02-SetupDevEnvironment)
- [Sažetak](../../../02-SetupDevEnvironment)
- [Sljedeći koraci](../../../02-SetupDevEnvironment)

## Uvod

Ovo poglavlje će vas voditi kroz postavljanje razvojnog okruženja. Koristit ćemo **GitHub modele** kao primarni primjer jer su besplatni, jednostavni za postavljanje samo s GitHub računom, ne zahtijevaju kreditnu karticu i omogućuju pristup raznim modelima za eksperimentiranje.

**Nije potrebna lokalna instalacija!** Možete odmah početi kodirati koristeći GitHub Codespaces, koji pruža potpuno razvojno okruženje u vašem pregledniku.

<img src="./images/models.webp" alt="Snimka zaslona: GitHub modeli" width="50%">

Preporučujemo korištenje [**GitHub modela**](https://github.com/marketplace?type=models) za ovaj tečaj jer su:
- **Besplatni** za početak
- **Jednostavni** za postavljanje samo s GitHub računom
- **Bez kreditne kartice** potrebne
- **Više modela** dostupno za eksperimentiranje

> **Napomena**: GitHub modeli korišteni u ovom treningu imaju sljedeće besplatne limite:
> - 15 zahtjeva po minuti (150 dnevno)
> - ~8,000 riječi ulaz, ~4,000 riječi izlaz po zahtjevu
> - 5 istovremenih zahtjeva
> 
> Za produkcijsku upotrebu, nadogradite na Azure AI Foundry modele sa svojim Azure računom. Vaš kod ne treba mijenjati. Pogledajte [dokumentaciju Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## Korak 1: Postavljanje razvojnog okruženja

<a name="quick-start-cloud"></a>

Kreirali smo unaprijed konfigurirani razvojni kontejner kako bismo minimizirali vrijeme postavljanja i osigurali da imate sve potrebne alate za ovaj tečaj Generativne AI za Javu. Odaberite preferirani pristup razvoju:

### Opcije postavljanja okruženja:

#### Opcija A: GitHub Codespaces (Preporučeno)

**Počnite kodirati za 2 minute - nije potrebna lokalna instalacija!**

1. Forkajte ovaj repozitorij na svoj GitHub račun
   > **Napomena**: Ako želite urediti osnovnu konfiguraciju, pogledajte [Konfiguraciju razvojnog kontejnera](../../../.devcontainer/devcontainer.json)
2. Kliknite **Code** → kartica **Codespaces** → **...** → **New with options...**
3. Koristite zadane postavke – ovo će odabrati **Konfiguraciju razvojnog kontejnera**: **Razvojno okruženje za Generativnu AI u Javi** prilagođeni devcontainer kreiran za ovaj tečaj
4. Kliknite **Create codespace**
5. Pričekajte ~2 minute da okruženje bude spremno
6. Nastavite na [Korak 2: Kreiranje GitHub tokena](../../../02-SetupDevEnvironment)

<img src="./images/codespaces.png" alt="Snimka zaslona: Codespaces podizbornik" width="50%">

<img src="./images/image.png" alt="Snimka zaslona: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Snimka zaslona: Opcije za kreiranje Codespace-a" width="50%">


> **Prednosti Codespaces-a**:
> - Nije potrebna lokalna instalacija
> - Radi na bilo kojem uređaju s preglednikom
> - Unaprijed konfigurirano sa svim alatima i ovisnostima
> - Besplatno 60 sati mjesečno za osobne račune
> - Dosljedno okruženje za sve polaznike

#### Opcija B: Lokalni razvojni kontejner

**Za developere koji preferiraju lokalni razvoj s Dockerom**

1. Forkajte i klonirajte ovaj repozitorij na svoj lokalni uređaj
   > **Napomena**: Ako želite urediti osnovnu konfiguraciju, pogledajte [Konfiguraciju razvojnog kontejnera](../../../.devcontainer/devcontainer.json)
2. Instalirajte [Docker Desktop](https://www.docker.com/products/docker-desktop/) i [VS Code](https://code.visualstudio.com/)
3. Instalirajte [Dev Containers ekstenziju](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) u VS Code
4. Otvorite mapu repozitorija u VS Code
5. Kada se pojavi upit, kliknite **Reopen in Container** (ili koristite `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Pričekajte da se kontejner izgradi i pokrene
7. Nastavite na [Korak 2: Kreiranje GitHub tokena](../../../02-SetupDevEnvironment)

<img src="./images/devcontainer.png" alt="Snimka zaslona: Postavljanje razvojnog kontejnera" width="50%">

<img src="./images/image-3.png" alt="Snimka zaslona: Završetak izgradnje razvojnog kontejnera" width="50%">

#### Opcija C: Koristite postojeću lokalnu instalaciju

**Za developere s postojećim Java okruženjem**

Preduvjeti:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) ili vaš preferirani IDE

Koraci:
1. Klonirajte ovaj repozitorij na svoj lokalni uređaj
2. Otvorite projekt u svom IDE-u
3. Nastavite na [Korak 2: Kreiranje GitHub tokena](../../../02-SetupDevEnvironment)

> **Savjet za profesionalce**: Ako imate uređaj slabijih specifikacija, ali želite lokalni VS Code, koristite GitHub Codespaces! Možete povezati svoj lokalni VS Code s cloud-hostiranim Codespace-om za najbolje od oba svijeta.

<img src="./images/image-2.png" alt="Snimka zaslona: Kreirana lokalna instanca razvojnog kontejnera" width="50%">


## Korak 2: Kreiranje GitHub osobnog pristupnog tokena

1. Idite na [GitHub postavke](https://github.com/settings/profile) i odaberite **Settings** iz izbornika profila.
2. U lijevom izborniku kliknite **Developer settings** (obično na dnu).
3. Pod **Personal access tokens**, kliknite **Fine-grained tokens** (ili slijedite ovaj direktni [link](https://github.com/settings/personal-access-tokens)).
4. Kliknite **Generate new token**.
5. Pod "Token name", unesite opisno ime (npr., `GenAI-Java-Course-Token`).
6. Postavite datum isteka (preporučeno: 7 dana radi sigurnosnih najboljih praksi).
7. Pod "Resource owner", odaberite svoj korisnički račun.
8. Pod "Repository access", odaberite repozitorije koje želite koristiti s GitHub modelima (ili "All repositories" ako je potrebno).
9. Pod "Repository permissions", pronađite **Models** i postavite na **Read and write**.
10. Kliknite **Generate token**.
11. **Kopirajte i spremite svoj token sada** – nećete ga ponovno vidjeti!

> **Sigurnosni savjet**: Koristite minimalno potrebne dozvole i najkraće praktično vrijeme isteka za svoje pristupne tokene.

## Korak 3: Testiranje postavki s GitHub Models primjerom

Kada je vaše razvojno okruženje spremno, testirajmo integraciju GitHub modela s našom primjer aplikacijom u [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Otvorite terminal u svom razvojnog okruženju.
2. Idite na primjer GitHub modela:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Postavite svoj GitHub token kao varijablu okruženja:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Pokrenite aplikaciju:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Trebali biste vidjeti izlaz sličan:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Razumijevanje primjer koda

Prvo, razumijmo što smo upravo pokrenuli. Primjer pod `examples/github-models` koristi OpenAI Java SDK za povezivanje s GitHub modelima:

**Što ovaj kod radi:**
- **Povezuje se** s GitHub modelima koristeći vaš osobni pristupni token
- **Šalje** jednostavnu poruku "Say Hello World!" AI modelu
- **Prima** i prikazuje odgovor AI-a
- **Provjerava** da vaše postavke ispravno rade

**Ključna ovisnost** (u `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Glavni kod** (`App.java`):
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

## Sažetak

Odlično! Sada imate sve postavljeno:

- Kreirali ste GitHub osobni pristupni token s odgovarajućim dozvolama za pristup AI modelima
- Pokrenuli ste svoje Java razvojno okruženje (bilo da je to Codespaces, razvojni kontejneri ili lokalno)
- Povezali ste se s GitHub modelima koristeći OpenAI Java SDK za besplatni AI razvoj
- Testirali ste da sve radi s jednostavnim primjerom koji komunicira s AI modelima

## Sljedeći koraci

[Poglavlje 3: Osnovne tehnike generativne AI](../03-CoreGenerativeAITechniques/README.md)

## Rješavanje problema

Imate problema? Evo uobičajenih problema i rješenja:

- **Token ne radi?** 
  - Provjerite jeste li kopirali cijeli token bez dodatnih razmaka
  - Provjerite je li token ispravno postavljen kao varijabla okruženja
  - Provjerite da vaš token ima ispravne dozvole (Models: Read and write)

- **Maven nije pronađen?** 
  - Ako koristite razvojne kontejnere/Codespaces, Maven bi trebao biti unaprijed instaliran
  - Za lokalno postavljanje, provjerite jesu li Java 21+ i Maven 3.9+ instalirani
  - Pokušajte `mvn --version` za provjeru instalacije

- **Problemi s povezivanjem?** 
  - Provjerite svoju internetsku vezu
  - Provjerite je li GitHub dostupan s vaše mreže
  - Provjerite da niste iza vatrozida koji blokira GitHub Models endpoint

- **Razvojni kontejner se ne pokreće?** 
  - Provjerite je li Docker Desktop pokrenut (za lokalni razvoj)
  - Pokušajte ponovno izgraditi kontejner: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Greške pri kompilaciji aplikacije?**
  - Provjerite jeste li u ispravnom direktoriju: `02-SetupDevEnvironment/examples/github-models`
  - Pokušajte očistiti i ponovno izgraditi: `mvn clean compile`

> **Trebate pomoć?**: Još uvijek imate problema? Otvorite problem u repozitoriju i pomoći ćemo vam.

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati mjerodavnim izvorom. Za ključne informacije preporučuje se profesionalni prijevod od strane stručnjaka. Ne preuzimamo odgovornost za bilo kakve nesporazume ili pogrešne interpretacije proizašle iz korištenja ovog prijevoda.