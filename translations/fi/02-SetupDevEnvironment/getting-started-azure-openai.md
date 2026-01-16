<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-07-27T13:15:22+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "fi"
}
-->
# Kehitysympäristön asennus Azure OpenAI:lle

> **Pika-aloitus**: Tämä opas on tarkoitettu Azure OpenAI -asennukseen. Jos haluat aloittaa nopeasti ilmaisilla malleilla, käytä [GitHub-malleja Codespacesin kanssa](./README.md#quick-start-cloud).

Tämä opas auttaa sinua asentamaan Azure AI Foundry -mallit Java AI -sovelluksiasi varten tässä kurssissa.

## Sisällysluettelo

- [Pika-asennuksen yleiskatsaus](../../../02-SetupDevEnvironment)
- [Vaihe 1: Luo Azure AI Foundry -resurssit](../../../02-SetupDevEnvironment)
  - [Luo Hub ja Projekti](../../../02-SetupDevEnvironment)
  - [Ota käyttöön GPT-4o-mini-malli](../../../02-SetupDevEnvironment)
- [Vaihe 2: Luo Codespace](../../../02-SetupDevEnvironment)
- [Vaihe 3: Määritä ympäristösi](../../../02-SetupDevEnvironment)
- [Vaihe 4: Testaa asennuksesi](../../../02-SetupDevEnvironment)
- [Mitä seuraavaksi?](../../../02-SetupDevEnvironment)
- [Resurssit](../../../02-SetupDevEnvironment)
- [Lisäresurssit](../../../02-SetupDevEnvironment)

## Pika-asennuksen yleiskatsaus

1. Luo Azure AI Foundry -resurssit (Hub, Projekti, Malli)
2. Luo Codespace Java-kehityskontilla
3. Määritä .env-tiedosto Azure OpenAI -tunnuksilla
4. Testaa asennuksesi esimerkkiprojektilla

## Vaihe 1: Luo Azure AI Foundry -resurssit

### Luo Hub ja Projekti

1. Siirry [Azure AI Foundry -portaaliin](https://ai.azure.com/) ja kirjaudu sisään
2. Klikkaa **+ Create** → **New hub** (tai siirry **Management** → **All hubs** → **+ New hub**)
3. Määritä hub:
   - **Hub name**: esim. "MyAIHub"
   - **Subscription**: Valitse Azure-tilauksesi
   - **Resource group**: Luo uusi tai valitse olemassa oleva
   - **Location**: Valitse lähin sijainti
   - **Storage account**: Käytä oletusta tai määritä mukautettu
   - **Key vault**: Käytä oletusta tai määritä mukautettu
   - Klikkaa **Next** → **Review + create** → **Create**
4. Kun hub on luotu, klikkaa **+ New project** (tai **Create project** hubin yleiskatsauksesta)
   - **Project name**: esim. "GenAIJava"
   - Klikkaa **Create**

### Ota käyttöön GPT-4o-mini-malli

1. Projektissasi siirry kohtaan **Model catalog** ja etsi **gpt-4o-mini**
   - *Vaihtoehto: Siirry kohtaan **Deployments** → **+ Create deployment***
2. Klikkaa **Deploy** gpt-4o-mini-mallin kortilla
3. Määritä käyttöönotto:
   - **Deployment name**: "gpt-4o-mini"
   - **Model version**: Käytä uusinta
   - **Deployment type**: Standard
4. Klikkaa **Deploy**
5. Kun käyttöönotto on valmis, siirry **Deployments**-välilehdelle ja kopioi seuraavat tiedot:
   - **Deployment name** (esim. "gpt-4o-mini")
   - **Target URI** (esim. `https://your-hub-name.openai.azure.com/`)  
      > **Tärkeää**: Kopioi vain perus-URL (esim. `https://myhub.openai.azure.com/`) äläkä koko päätepisteen polkua.
   - **Key** (Keys and Endpoint -osiosta)

> **Onko ongelmia?** Vieraile virallisessa [Azure AI Foundry -dokumentaatiossa](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)

## Vaihe 2: Luo Codespace

1. Haarauta tämä arkisto GitHub-tilillesi
   > **Huom**: Jos haluat muokata perusasetuksia, katso [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. Haarautetussa arkistossasi klikkaa **Code** → **Codespaces**-välilehti
3. Klikkaa **...** → **New with options...**
![codespace-vaihtoehtojen luominen](../../../translated_images/fi/codespaces.9945ded8ceb431a5.webp)
4. Valitse **Dev container configuration**: 
   - **Generative AI Java Development Environment**
5. Klikkaa **Create codespace**

## Vaihe 3: Määritä ympäristösi

Kun Codespace on valmis, määritä Azure OpenAI -tunnuksesi:

1. **Siirry esimerkkiprojektiin arkiston juuresta:**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **Luo .env-tiedosto:**
   ```bash
   cp .env.example .env
   ```

3. **Muokkaa .env-tiedostoa Azure OpenAI -tunnuksillasi:**
   ```bash
   # Your Azure OpenAI API key (from Azure AI Foundry portal)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # Your Azure OpenAI endpoint URL (e.g., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **Tietoturvahuomio**: 
   > - Älä koskaan lisää `.env`-tiedostoa versionhallintaan
   > - `.env`-tiedosto on jo lisätty `.gitignore`-tiedostoon
   > - Pidä API-avaimesi turvassa ja vaihda ne säännöllisesti

## Vaihe 4: Testaa asennuksesi

Aja esimerkkisovellus testataksesi Azure OpenAI -yhteyttäsi:

```bash
mvn clean spring-boot:run
```

Näet vastauksen GPT-4o-mini-mallilta!

> **VS Code -käyttäjät**: Voit myös painaa `F5` VS Codessa ajaaksesi sovelluksen. Käynnistyskonfiguraatio on jo asetettu lataamaan `.env`-tiedosto automaattisesti.

> **Täydellinen esimerkki**: Katso [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) saadaksesi yksityiskohtaiset ohjeet ja vianmäärityksen.

## Mitä seuraavaksi?

**Asennus valmis!** Sinulla on nyt:
- Azure OpenAI ja gpt-4o-mini otettu käyttöön
- Paikallinen .env-tiedosto määritetty
- Java-kehitysympäristö valmiina

**Jatka** [Lukuun 3: Generatiivisen tekoälyn ydintekniikat](../03-CoreGenerativeAITechniques/README.md) aloittaaksesi tekoälysovellusten rakentamisen!

## Resurssit

- [Azure AI Foundry -dokumentaatio](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI -dokumentaatio](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## Lisäresurssit

- [Lataa VS Code](https://code.visualstudio.com/Download)
- [Hanki Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

**Vastuuvapauslauseke**:  
Tämä asiakirja on käännetty käyttämällä tekoälypohjaista käännöspalvelua [Co-op Translator](https://github.com/Azure/co-op-translator). Vaikka pyrimme tarkkuuteen, huomioithan, että automaattiset käännökset voivat sisältää virheitä tai epätarkkuuksia. Alkuperäinen asiakirja sen alkuperäisellä kielellä tulisi pitää ensisijaisena lähteenä. Kriittisen tiedon osalta suositellaan ammattimaista ihmiskäännöstä. Emme ole vastuussa väärinkäsityksistä tai virhetulkinnoista, jotka johtuvat tämän käännöksen käytöstä.