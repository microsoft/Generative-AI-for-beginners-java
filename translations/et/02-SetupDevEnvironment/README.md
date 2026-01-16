<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "4bdff5070d182c64143dfe5a581d0ec7",
  "translation_date": "2025-10-11T10:44:57+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "et"
}
-->
# Generatiivse tehisintellekti arenduskeskkonna seadistamine Java jaoks

> **Kiire algus**: Koodi pilves 2 minutiga – liigu [GitHub Codespaces'i seadistamise](../../../02-SetupDevEnvironment) juurde – ei vaja kohalikku installimist ja kasutab GitHubi mudeleid!

> **Huvitatud Azure OpenAI-st?** Vaata meie [Azure OpenAI seadistusjuhendit](getting-started-azure-openai.md), kus on sammud uue Azure OpenAI ressursi loomiseks.

## Mida õpid

- Seadistada Java arenduskeskkonda AI rakenduste jaoks
- Valida ja konfigureerida oma eelistatud arenduskeskkonda (pilvepõhine Codespaces, kohalik dev-container või täielik kohalik seadistus)
- Testida oma seadistust, ühendades GitHubi mudelitega

## Sisukord

- [Mida õpid](../../../02-SetupDevEnvironment)
- [Sissejuhatus](../../../02-SetupDevEnvironment)
- [1. samm: Arenduskeskkonna seadistamine](../../../02-SetupDevEnvironment)
  - [Valik A: GitHub Codespaces (soovitatav)](../../../02-SetupDevEnvironment)
  - [Valik B: Kohalik dev-container](../../../02-SetupDevEnvironment)
  - [Valik C: Olemasoleva kohaliku installatsiooni kasutamine](../../../02-SetupDevEnvironment)
- [2. samm: GitHubi isikliku juurdepääsutunnuse loomine](../../../02-SetupDevEnvironment)
- [3. samm: Seadistuse testimine](../../../02-SetupDevEnvironment)
- [Tõrkeotsing](../../../02-SetupDevEnvironment)
- [Kokkuvõte](../../../02-SetupDevEnvironment)
- [Järgmised sammud](../../../02-SetupDevEnvironment)

## Sissejuhatus

See peatükk juhendab sind arenduskeskkonna seadistamisel. Kasutame **GitHubi mudeleid** peamise näitena, kuna see on tasuta, lihtne seadistada ainult GitHubi kontoga, ei vaja krediitkaarti ja pakub mitmeid mudeleid katsetamiseks.

**Kohalikku seadistust pole vaja!** Saad kohe koodi kirjutama hakata, kasutades GitHub Codespaces'i, mis pakub täielikku arenduskeskkonda otse sinu brauseris.

<img src="./images/models.webp" alt="Ekraanipilt: GitHubi mudelid" width="50%">

Soovitame kasutada [**GitHubi mudeleid**](https://github.com/marketplace?type=models) selle kursuse jaoks, kuna see on:
- **Tasuta** alustamiseks
- **Lihtne** seadistada ainult GitHubi kontoga
- **Ei vaja krediitkaarti**
- **Mitmed mudelid** katsetamiseks

> **Märkus**: Selle koolituse GitHubi mudelitel on järgmised tasuta piirangud:
> - 15 päringut minutis (150 päevas)
> - ~8000 sõna sisend, ~4000 sõna väljund päringu kohta
> - 5 samaaegset päringut
> 
> Tootmiskasutuseks uuenda Azure AI Foundry mudelitele oma Azure'i kontoga. Sinu kood ei vaja muutmist. Vaata [Azure AI Foundry dokumentatsiooni](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## 1. samm: Arenduskeskkonna seadistamine

<a name="quick-start-cloud"></a>

Oleme loonud eelkonfigureeritud arenduscontaineri, et minimeerida seadistusaega ja tagada, et sul on kõik vajalikud tööriistad selle generatiivse tehisintellekti Java kursuse jaoks. Vali oma eelistatud arendusviis:

### Keskkonna seadistamise valikud:

#### Valik A: GitHub Codespaces (soovitatav)

**Alusta koodi kirjutamist 2 minutiga – kohalikku seadistust pole vaja!**

1. Forki see repositoorium oma GitHubi kontole
   > **Märkus**: Kui soovid muuta põhikonfiguratsiooni, vaata [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Klõpsa **Code** → **Codespaces** vahekaart → **...** → **New with options...**
3. Kasuta vaikeseadeid – see valib **Dev container configuration**: **Generative AI Java Development Environment** kohandatud devcontaineri, mis on loodud selle kursuse jaoks
4. Klõpsa **Create codespace**
5. Oota ~2 minutit, kuni keskkond on valmis
6. Jätka [2. sammuga: GitHubi tunnuse loomine](../../../02-SetupDevEnvironment)

<img src="../../../translated_images/et/codespaces.9945ded8ceb431a5.webp" alt="Ekraanipilt: Codespaces'i alammenüü" width="50%">

<img src="../../../translated_images/et/image.833552b62eee7766.webp" alt="Ekraanipilt: New with options" width="50%">

<img src="../../../translated_images/et/codespaces-create.b44a36f728660ab7.webp" alt="Ekraanipilt: Codespace'i loomise valikud" width="50%">

> **Codespaces'i eelised**:
> - Kohalikku installimist pole vaja
> - Töötab igas seadmes, millel on brauser
> - Eelkonfigureeritud kõigi tööriistade ja sõltuvustega
> - Tasuta 60 tundi kuus isikliku konto jaoks
> - Ühtne keskkond kõigile õppijatele

#### Valik B: Kohalik dev-container

**Arendajatele, kes eelistavad kohalikku arendust Dockeriga**

1. Forki ja klooni see repositoorium oma kohalikku masinasse
   > **Märkus**: Kui soovid muuta põhikonfiguratsiooni, vaata [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Installi [Docker Desktop](https://www.docker.com/products/docker-desktop/) ja [VS Code](https://code.visualstudio.com/)
3. Installi [Dev Containers laiendus](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) VS Code'is
4. Ava repositooriumi kaust VS Code'is
5. Kui küsitakse, klõpsa **Reopen in Container** (või kasuta `Ctrl+Shift+P` → "Dev Containers: Reopen in Container")
6. Oota, kuni container ehitatakse ja käivitatakse
7. Jätka [2. sammuga: GitHubi tunnuse loomine](../../../02-SetupDevEnvironment)

<img src="../../../translated_images/et/devcontainer.21126c9d6de64494.webp" alt="Ekraanipilt: Dev-containeri seadistamine" width="50%">

<img src="../../../translated_images/et/image-3.bf93d533bbc84268.webp" alt="Ekraanipilt: Dev-containeri ehitamine lõpetatud" width="50%">

#### Valik C: Olemasoleva kohaliku installatsiooni kasutamine

**Arendajatele, kellel on olemasolev Java keskkond**

Eeltingimused:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) või sinu eelistatud IDE

Sammud:
1. Klooni see repositoorium oma kohalikku masinasse
2. Ava projekt oma IDE-s
3. Jätka [2. sammuga: GitHubi tunnuse loomine](../../../02-SetupDevEnvironment)

> **Pro nõuanne**: Kui sul on madala spetsifikatsiooniga masin, kuid soovid VS Code'i kohapeal, kasuta GitHub Codespaces'i! Sa saad ühendada oma kohaliku VS Code'i pilvehostitud Codespace'iga, et saada mõlemast maailmast parim.

<img src="../../../translated_images/et/image-2.fc0da29a6e4d2aff.webp" alt="Ekraanipilt: loodud kohalik dev-containeri instants" width="50%">

## 2. samm: GitHubi isikliku juurdepääsutunnuse loomine

1. Navigeeri [GitHubi seadistustesse](https://github.com/settings/profile) ja vali **Settings** oma profiilimenüüst.
2. Vasakpoolses külgribas klõpsa **Developer settings** (tavaliselt allosas).
3. **Personal access tokens** all klõpsa **Fine-grained tokens** (või kasuta seda otselinki: [link](https://github.com/settings/personal-access-tokens)).
4. Klõpsa **Generate new token**.
5. "Token name" all anna kirjeldav nimi (nt `GenAI-Java-Course-Token`).
6. Määra aegumiskuupäev (soovitatav: 7 päeva turvapraktikate jaoks).
7. "Resource owner" all vali oma kasutajakonto.
8. "Repository access" all vali repositooriumid, mida soovid kasutada GitHubi mudelitega (või "All repositories", kui vaja).
9. "Account permissions" all leia **Models** ja määra see **Read-only**.
10. Klõpsa **Generate token**.
11. **Kopeeri ja salvesta oma tunnus kohe** – sa ei näe seda enam!

> **Turvanõuanne**: Kasuta minimaalselt vajalikku ulatust ja lühimat praktilist aegumisaega oma juurdepääsutunnuste jaoks.

## 3. samm: Seadistuse testimine GitHubi mudelite näitega

Kui sinu arenduskeskkond on valmis, testime GitHubi mudelite integreerimist meie näiterakendusega [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models).

1. Ava terminal oma arenduskeskkonnas.
2. Navigeeri GitHubi mudelite näite juurde:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Määra oma GitHubi tunnus keskkonnamuutujana:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Käivita rakendus:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

Sa peaksid nägema väljundit, mis on sarnane:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### Näitekoodi mõistmine

Esmalt mõistame, mida me just käivitasime. Näide `examples/github-models` kaustas kasutab OpenAI Java SDK-d, et ühendada GitHubi mudelitega:

**Mida see kood teeb:**
- **Ühendub** GitHubi mudelitega, kasutades sinu isiklikku juurdepääsutunnust
- **Saadab** lihtsa sõnumi "Say Hello World!" AI mudelile
- **Võtab vastu** ja kuvab AI vastuse
- **Kinnitab**, et sinu seadistus töötab õigesti

**Peamine sõltuvus** (failis `pom.xml`):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Põhikood** (`App.java`):
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

## Kokkuvõte

Suurepärane! Sul on nüüd kõik seadistatud:

- Loodud GitHubi isiklik juurdepääsutunnus õigete õigustega AI mudelitele juurdepääsuks
- Käivitatud Java arenduskeskkond (olgu see Codespaces, dev-container või kohalik)
- Ühendatud GitHubi mudelitega, kasutades OpenAI Java SDK-d tasuta AI arenduseks
- Testitud, et kõik töötab lihtsa näitega, mis suhtleb AI mudelitega

## Järgmised sammud

[3. peatükk: Generatiivse tehisintellekti põhitehnikad](../03-CoreGenerativeAITechniques/README.md)

## Tõrkeotsing

Probleemid? Siin on levinud probleemid ja lahendused:

- **Tunnus ei tööta?** 
  - Veendu, et kopeerisid kogu tunnuse ilma lisatühikuteta
  - Kontrolli, et tunnus on õigesti keskkonnamuutujana määratud
  - Veendu, et tunnusel on õiged õigused (Models: Read and write)

- **Maveni puudumine?** 
  - Kui kasutad dev-containerit/Codespaces'i, peaks Maven olema eelinstallitud
  - Kohaliku seadistuse jaoks veendu, et Java 21+ ja Maven 3.9+ on installitud
  - Proovi `mvn --version`, et kontrollida installatsiooni

- **Ühenduse probleemid?** 
  - Kontrolli oma internetiühendust
  - Veendu, et GitHub on sinu võrgust kättesaadav
  - Kontrolli, et tulemüür ei blokeeri GitHubi mudelite lõpp-punkti

- **Dev-container ei käivitu?** 
  - Veendu, et Docker Desktop töötab (kohaliku arenduse jaoks)
  - Proovi containerit uuesti ehitada: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **Rakenduse kompileerimisvead?**
  - Veendu, et oled õiges kataloogis: `02-SetupDevEnvironment/examples/github-models`
  - Proovi puhastada ja uuesti ehitada: `mvn clean compile`

> **Abi vaja?**: Ikka probleeme? Ava repositooriumis probleem ja me aitame sind.

---

**Lahtiütlus**:  
See dokument on tõlgitud AI tõlketeenuse [Co-op Translator](https://github.com/Azure/co-op-translator) abil. Kuigi püüame tagada täpsust, palume arvestada, et automaatsed tõlked võivad sisaldada vigu või ebatäpsusi. Algne dokument selle algses keeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitame kasutada professionaalset inimtõlget. Me ei vastuta selle tõlke kasutamisest tulenevate arusaamatuste või valesti tõlgenduste eest.