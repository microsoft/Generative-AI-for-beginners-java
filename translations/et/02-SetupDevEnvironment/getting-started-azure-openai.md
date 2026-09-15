# Arenduskeskkonna seadistamine Azure AI Foundry jaoks

> See juhend seadistab selle kursuse Java AI rakenduste jaoks **Azure AI Foundry** mudelid, kasutades **võtmepõhist** autentimist (Microsoft Entra ID) — pole vaja hallata API võtmestikke. Oled tööriistadega uus? Alusta [arenduskeskkonna juhendist](./README.md).

See juhend seadistab selle kursuse Java AI rakenduste jaoks **Azure AI Foundry** mudelid. Sul on kaks rada:

- **Variant A — Provisionimine `azd` + Bicep abil (soovitatav):** üks käsklus paigutab Foundry konto ja mudelid koodina. Pole porterite klõpsimist.
- **Variant B — Loo ressursid käsitsi** Azure AI Foundry portaalis.

Mõlemad rajad kasutavad **võtmepõhist autentimist** (Microsoft Entra ID) — API võtmestikke pole vaja kopeerida ega lekkida.

## Sisukord

- [Mis luuakse](#mis-luuakse)
- [Eeltingimused](#eeltingimused)
- [Variant A: Provisionimine azd + Bicep abil (Soovitatav)](#option-a-provision-with-azd--bicep-recommended)
- [Variant B: Ressursside käsitsi loomine](#variant-b-ressursside-käsitsi-loomine)
- [Seadista oma keskkond](#seadista-oma-keskkond)
- [Testi oma seadistust](#testi-oma-seadistust)
- [Mis järgmiseks?](#mis-järgmiseks)
- [Ressursid](#ressursid)
- [Lisamaterjalid](#lisamaterjalid)

## Mis luuakse

Kaustas [`infra/`](../../../02-SetupDevEnvironment/infra) olevad Bicep mallid paigutavad:

- **Azure AI Foundry** konto (`Microsoft.CognitiveServices/accounts`, tüüp `AIServices`) koos projektiga
- **vestluse** lansseerimine - GPT-5.6 Luna (`gpt-5.6-luna`), versioon `2026-07-09`, koos `GlobalStandard` mahtuvusega `10` (10 päringut/minut ja 10 000 märksõna/minut selle mudeli jaoks)
- **embedding** lansseerimine - `text-embedding-3-small`, versioon `1` (kasutatakse hilisemates peatükkides)
- **võtmepõhine rolli määramine** (`Cognitive Services OpenAI User`), nii et logid sisse `az login` abil ilma võtmete haldamiseta

## Eeltingimused

- [Azure tellimus](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) ja [Maven 3.9+](https://maven.apache.org/download.cgi)

## Variant A: Provisionimine azd + Bicep abil (Soovitatav)

Kaustast `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Logi sisse (mõlema tööriista puhul)
azd auth login
az login

# Tarkvaraarendusplatvormi konto + mudelite juurutuste loomine
azd up
```

`azd` küsib **keskkonna nime** (näiteks `genai-java`), **tellimust** ja **piirkonda**. Vali oma tellimus ja piirkond, kus on saadaval `gpt-5.6-luna` ja `text-embedding-3-small`, näiteks `eastus2`. Kinnita, et tellimusel on piisavalt limiiti mudeli ja lansseerimise tüübi jaoks selles piirkonnas; saadavus ja limiidid varieeruvad tellimuse kaupa.

Kui provisioning lõpeb, teeb azd järgmist:

1. Paigutab kõik, mis on määratletud failis [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Käivitab postprovision hooki, mis kirjutab faili [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) sinu lõpp-punkti ja lansseerimise nimedega (ilma saladusteta).

> **Näpunäide:** Käivita `azd up` igal ajal uuesti, et muudatusi rakendada. Käivita `azd down`, et kustutada kõik ja lõpetada kulutamine.

Genereeritud seadete nägemiseks:

```bash
azd env get-values
```

Nüüd liigu edasi ja vaata [Testi oma seadistust](#testi-oma-seadistust).

## Variant B: Ressursside käsitsi loomine

Eelistad portaali? Loo ressursid käsitsi:

1. Mine [Azure AI Foundry portaalile](https://ai.azure.com/) ja logi sisse.
2. **Loo projekt** (see loob samas ka AI Foundry ressursi). Anna talle nimi, näiteks `GenAIJava`.
3. Ava projektis **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Paiguta **GPT-5.6 Luna** (mudeli ja lansseerimise nimi `gpt-5.6-luna`, versioon `2026-07-09`) koos **Global Standard** mahu `10`. Korda sama **text-embedding-3-small** (versioon `1`) jaoks, kui tahad embedding näiteid.
5. Kopeeri lehelt **Overview** **endpoint** (näiteks `https://<resource>.openai.azure.com/`).
6. Anna endale võtmepõhine juurdepääs: ava ressursil **Access control (IAM)** → **Add role assignment** → määra oma kontole roll **Cognitive Services OpenAI User**.

> **Kas sul on endiselt raskusi?** Vaata [Azure AI Foundry dokumentatsiooni](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Seadista oma keskkond

**Kui kasutasid Variant A (`azd up`)**, siis sinu seaded on juba kirjas — midagi pole vaja seadistada. Liigu edasi [Testi oma seadistust](#testi-oma-seadistust).

**Kui kasutasid Variant B (käsitsi)**, loo ise näite `.env` fail:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Muuda `.env` faili ja lisa oma lõpp-punkt (võtmeta — autentimine on võtmepõhine):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Kasuta ressursi Azure OpenAI lõpp-punkti, mitte projekti URL-i. Basic-chat rakendus suunab selle `/openai/v1` ja seadistab otsese bearer-token kliendi; API võti pole vajalik.

> **Turvalisuse märkus:** API võtit ei ole vaja salvestada. Autentid Microsoft Entra ID kaudu `az login` (kohapeal) või hallatud identiteedi kaudu (Azure'is). `.env` fail sisaldab ainult saladusteta seadeid ja on juba `.gitignore`-ga kaetud.

## Testi oma seadistust

Veendu, et oled sisse logitud, et võtmepõhine autentimine saaks tokeni, seejärel käivita näide:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # kui sa pole veel sisse logitud
mvn clean spring-boot:run
```

Sa peaksid nägema vastust `gpt-5.6-luna` mudelilt. Käivita näiteid järjest, et püsida väikese vaikimisi limiidi piires; kui saad HTTP 429, oota palun uuesti proovimise intervalli.

> **VS Code kasutajad:** Vajuta `F5`, et käivitada. Rakendus laadib automaatselt sinu `.env` faili.

> **Täisnäide:** Vaata [Basic Chat Azure AI Foundry näidet](./examples/basic-chat-azure/README.md) detailide ja tõrkeotsingu jaoks.

## Mis järgmiseks?

Pärast provisioningut ja näite edukat käivitamist on sul:
- Azure AI Foundry koos `gpt-5.6-luna` ja `text-embedding-3-small` paigaldatud
- Võtmepõhine autentimine (Microsoft Entra ID) — võtmeid pole vaja hallata
- Kohalik `.env` fail koos sinu lõpp-punkti ja lansseerimise nimedega
- Java arenduskeskkond valmis kasutamiseks

**Jätka** [3. peatükiga: Põhilised generatiivse tehisintellekti tehnikad](../03-CoreGenerativeAITechniques/README.md), et alustada AI rakenduste loomist!

## Ressursid

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Võtmepõhine autentimine Microsoft Entra ID-ga](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry dokumentatsioon](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK üleminemine](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Ametlik OpenAI Java SDK Azure OpenAI v1-ga](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Lisamaterjalid

- [Laadi alla VS Code](https://code.visualstudio.com/Download)
- [Hangi Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev konteineri seadistus](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades AI tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun pange tähele, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega seotud eksimustest või valesti mõistmistest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->