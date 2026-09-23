# Nastavitev razvojnega okolja za Azure AI Foundry

> Ta vodič nastavi modele **Azure AI Foundry** za Java AI aplikacije v tem tečaju, z uporabo **avtentikacije brez ključev** (Microsoft Entra ID) — brez upravljanja API ključev. Nov v orodju? Začni z [vodnikom za razvojno okolje](./README.md).

Ta vodič nastavi modele **Azure AI Foundry** za Java AI aplikacije v tem tečaju. Na voljo imata dve poti:

- **Možnost A — Provision z `azd` + Bicep (priporočeno):** ena ukazna vrstica za namestitev Foundry računa in modelov kot koda. Brez klikov po portalu.
- **Možnost B — Ročno ustvarjanje virov** v portalu Azure AI Foundry.

Obe poti uporabljata **avtentikacijo brez ključev** (Microsoft Entra ID) — ni treba kopirati ali razkriti API ključev.

## Kazalo

- [Kaj se ustvari](#kaj-se-ustvari)
- [Pogoji](#pogoji)
- [Možnost A: Provision z azd + Bicep (priporočeno)](#option-a-provision-with-azd--bicep-recommended)
- [Možnost B: Ročno ustvarjanje virov](#možnost-b-ročno-ustvarjanje-virov)
- [Konfigurirajte svoje okolje](#konfigurirajte-svoje-okolje)
- [Preizkusite svojo namestitev](#preizkusi-svojo-namestitev)
- [Kaj sledi?](#kaj-sledi)
- [Viri](#viri)
- [Dodatni viri](#dodatni-viri)

## Kaj se ustvari

Bicep predloge v [`infra/`](../../../02-SetupDevEnvironment/infra) zagotovijo:

- Račun **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, vrsta `AIServices`) s projektom
- Postavitev pogovora - GPT-5.6 Luna (`gpt-5.6-luna`), različica `2026-07-09`, s kapaciteto `GlobalStandard` `10` (10 zahtev na minuto in 10.000 žetonov na minuto za ta model)
- Postavitev vdelave - `text-embedding-3-small`, različica `1` (uporablja se v kasnejših poglavjih)
- Dodelitev brezključne vloge (`Cognitive Services OpenAI User`), da se prijavite z `az login`, namesto da upravljate ključe

## Pogoji

- [Azure naročnina](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) in [Maven 3.9+](https://maven.apache.org/download.cgi)

## Možnost A: Provision z azd + Bicep (priporočeno)

Iz mape `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Prijava (obe orodji)
azd auth login
az login

# Priprava Foundry računa + namestitve modelov
azd up
```

`azd` bo zahteval **ime okolja** (na primer `genai-java`), **naročnino** in **regijo**. Izberi svojo naročnino in regijo, kjer sta na voljo `gpt-5.6-luna` in `text-embedding-3-small`, na primer `eastus2`. Preveri, ali ima naročnina dovolj kvote za model in vrsto postavitve v tej regiji; razpoložljivost in kvote se razlikujejo glede na naročnino.

Ko je provision končan, azd:

1. Namesti vse, kar je definirano v [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Izvede post-provision hook, ki zapiše [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) z vašim končnim točko in imeni namestitev (brez skrivnosti).

> **Namig:** Za uporabo sprememb kadar koli ponovno zaženi `azd up`. Za brisanje vsega in ustavitev stroškov poženite `azd down`.

Za ogled ustvarjenih nastavitev:

```bash
azd env get-values
```

Zdaj preskoči na [Preizkusi svojo namestitev](#preizkusi-svojo-namestitev).

## Možnost B: Ročno ustvarjanje virov

Raje uporabljaš portal? Ustvari vire ročno:

1. Obišči [Azure AI Foundry portal](https://ai.azure.com/) in se prijavi.
2. **Ustvari projekt** (s tem se ustvari tudi vir AI Foundry). Poimenuj ga na primer `GenAIJava`.
3. V svojem projektu odpri **Modeli + končne točke** → **Namesti model** → **Namesti osnovni model**.
4. Namesti **GPT-5.6 Luna** (ime modela in namestitve `gpt-5.6-luna`, različica `2026-07-09`) s kapaciteto **Global Standard** `10`. Ponovi za **text-embedding-3-small**, različica `1`, če želiš primere vdelav.
5. Iz **Pregleda** kopiraj **končno točko** (na primer `https://<resource>.openai.azure.com/`).
6. Dodeli si brezključen dostop: na viru odpri **Upravljanje dostopa (IAM)** → **Dodaj dodelitev vloge** → dodeli vlogo **Cognitive Services OpenAI User** svojemu računu.

> **Še vedno imaš težave?** Oglej si [dokumentacijo Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurirajte svoje okolje

**Če si uporabil možnost A (`azd up`)**, je datoteka z nastavitvami že ustvarjena — ni ničesar za konfigurirati. Preskoči na [Preizkusi svojo namestitev](#preizkusi-svojo-namestitev).

**Če si uporabil možnost B (ročna)**, sam ustvari `.env` datoteko za primer:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Uredi `.env` s svojo končno točko (brez ključa — avtentikacija je brez ključev):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Uporabi Azure OpenAI končno točko vira, ne URL projekta. Aplikacija basic-chat jo prevede na `/openai/v1` in nastavi eksplicitnega odjemalca z nosilnim žetonom; API ključ ni potreben.

> **Varnostna opomba:** Ni API ključa za shranjevanje. Avtenticiraš se z Microsoft Entra ID prek `az login` (lokalno) ali upravljano identiteto (v Azure). `.env` datoteka vsebuje samo nesečne nastavitve in je že vključena v `.gitignore`.

## Preizkusi svojo namestitev

Poskrbi, da si prijavljen, da lahko avtentikacija brez ključev pridobi žeton, nato zaženi primer:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # če še niste prijavljeni
mvn clean spring-boot:run
```

Moral bi videti odziv iz modela `gpt-5.6-luna`. Zaženi primere zaporedno, da ostaneš znotraj majhne privzete kvote; če prejmeš HTTP 429, počakaj na ponovni poskus pred ponovnim poizkusom.

> **Uporabniki VS Code:** Pritisni `F5` za zagon. Aplikacija samodejno naloži tvojo `.env`.

> **Celoten primer:** Oglej si [Primer osnovnega klepeta z Azure AI Foundry](./examples/basic-chat-azure/README.md) za podrobnosti in odpravljanje težav.

## Kaj sledi?

Po uspešnem provisionu in zagonu primera boš imel:
- Azure AI Foundry z nameščenim `gpt-5.6-luna` in `text-embedding-3-small`
- Avtentikacijo brez ključev (Microsoft Entra ID) — brez upravljanja ključev
- Lokalno `.env` z imeni tvoje končne točke in namestitev
- Pripravljen Java razvojni okolje

**Nadaljuj na** [Poglavje 3: Osnovne generativne AI tehnike](../03-CoreGenerativeAITechniques/README.md) za začetek gradnje AI aplikacij!

## Viri

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Avtentikacija brez ključev z Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentacija Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Prehod Spring AI 2 na OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Uradni OpenAI Java SDK z Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Dodatni viri

- [Prenesi VS Code](https://code.visualstudio.com/Download)
- [Pridobi Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfiguracija razvojnega kontejnerja](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Omejitev odgovornosti**:
Ta dokument je bil preveden z uporabo AI prevajalske storitve [Co-op Translator](https://github.com/Azure/co-op-translator). Čeprav si prizadevamo za natančnost, vas prosimo, da upoštevate, da avtomatizirani prevodi lahko vsebujejo napake ali netočnosti. Izvirni dokument v njegovem izvirnem jeziku je treba obravnavati kot avtoritativni vir. Za kritične informacije je priporočljiv strokovni človeški prevod. Ne odgovarjamo za morebitna nesporazume ali napačne interpretacije, ki izhajajo iz uporabe tega prevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->