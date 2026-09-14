# Postavljanje razvojne okoline za Azure AI Foundry

> Ovaj vodič postavlja modele **Azure AI Foundry** za Java AI aplikacije u ovom tečaju, koristeći **autentifikaciju bez ključeva** (Microsoft Entra ID) — nema API ključeva za upravljanje. Novi ste u alatu? Počnite s [vodičem za razvojno okruženje](./README.md).

Ovaj vodič postavlja modele **Azure AI Foundry** za Java AI aplikacije u ovom tečaju. Imate dva puta:

- **Opcija A — Provisioniranje s `azd` + Bicep (preporučeno):** jedna naredba implementira Foundry račun i modele kao kod. Bez klikanja po portalu.
- **Opcija B — Ručno stvaranje resursa** u Azure AI Foundry portalu.

Oba puta koriste **autentifikaciju bez ključeva** (Microsoft Entra ID) — nema API ključeva za kopiranje ili curenje.

## Sadržaj

- [Što se kreira](#što-se-kreira)
- [Preduvjeti](#preduvjeti)
- [Opcija A: Provisioniranje s azd + Bicep (Preporučeno)](#option-a-provision-with-azd--bicep-recommended)
- [Opcija B: Ručno stvaranje resursa](#opcija-b-ručno-stvaranje-resursa)
- [Konfigurirajte svoje okruženje](#konfigurirajte-svoje-okruženje)
- [Testirajte postavke](#testirajte-postavke)
- [Što slijedi?](#što-slijedi)
- [Resursi](#resursi)
- [Dodatni resursi](#dodatni-resursi)

## Što se kreira

Bicep predlošci u [`infra/`](../../../02-SetupDevEnvironment/infra) osiguravaju:

- **Azure AI Foundry** račun (`Microsoft.CognitiveServices/accounts`, vrsta `AIServices`) s projektom
- **chat** deployment - GPT-5.6 Luna (`gpt-5.6-luna`), verzija `2026-07-09`, s kapacitetom `GlobalStandard` 10 (10 zahtjeva/minuti i 10,000 tokena/minuti za ovaj model)
- **embedding** deployment - `text-embedding-3-small`, verzija `1` (koristi se u kasnijim poglavljima)
- **dodjela uloga bez ključa** (`Cognitive Services OpenAI User`) tako da se prijavljujete s `az login`, bez upravljanja ključevima

## Preduvjeti

- [Azure pretplata](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) i [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opcija A: Provisioniranje s azd + Bicep (Preporučeno)

Iz mape `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Prijavite se (obje alatke)
azd auth login
az login

# Postavljanje Foundry računa + implementacije modela
azd up
```

`azd` će tražiti **naziv okruženja** (npr. `genai-java`), **pretplatu** i **regiju**. Odaberite svoju pretplatu i regiju gdje su dostupni `gpt-5.6-luna` i `text-embedding-3-small`, primjerice `eastus2`. Potvrdite da pretplata ima dovoljan kvot za model i vrstu deploymenta u toj regiji; dostupnost i kvote variraju po pretplati.

Po završetku provisioniranja, azd:

1. Implementira sve definirano u [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Pokreće post-provision hook koji zapisuje [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) s vašom endpoint i imenima deploymenta (bez tajni).

> **Savjet:** Pokrenite `azd up` bilo kada za primjenu promjena. Koristite `azd down` za brisanje svega i zaustavljanje troškova.

Za pregled generiranih postavki:

```bash
azd env get-values
```

Sada preskočite na [Testirajte postavke](#testirajte-postavke).

## Opcija B: Ručno stvaranje resursa

Preferirate portal? Stvorite resurse ručno:

1. Idite na [Azure AI Foundry portal](https://ai.azure.com/) i prijavite se.
2. **Napravite projekt** (to također stvara AI Foundry resurs). Dajte mu naziv poput `GenAIJava`.
3. U projektu otvorite **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Implementirajte **GPT-5.6 Luna** (model i ime deploymenta `gpt-5.6-luna`, verzija `2026-07-09`) s kapacitetom **Global Standard** 10. Ponavljajte za **text-embedding-3-small**, verzija `1`, ako želite primjere za embedding.
5. Iz **Pregleda (Overview)**, kopirajte **endpoint** (npr. `https://<resource>.openai.azure.com/`).
6. Dodijelite si pristup bez ključa: na resursu otvorite **Kontrola pristupa (IAM)** → **Dodaj dodjelu uloge** → dodijelite ulogu **Cognitive Services OpenAI User** svom računu.

> **I dalje imate problema?** Pogledajte [dokumentaciju Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Konfigurirajte svoje okruženje

**Ako koristite Opciju A (`azd up`)**, vaša datoteka s postavkama je već zapisana — nema ništa za konfigurirati. Preskočite na [Testirajte postavke](#testirajte-postavke).

**Ako koristite Opciju B (ručno)**, sami napravite `.env` datoteku za primjer:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Uredite `.env` s vašim endpointom (bez ključa — autentifikacija je bez ključa):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Koristite Azure OpenAI endpoint resursa, ne URL projekta. Basic-chat aplikacija ga prevodi u `/openai/v1` i konfigurira klijenta s eksplicitnim bearer tokenom; API ključ nije potreban.

> **Napomena o sigurnosti:** Nema API ključa za pohranu. Autentificirate se putem Microsoft Entra ID preko `az login` (lokalno) ili managed identity (u Azureu). `.env` datoteka sadrži samo javne postavke i već je zaštićena `.gitignore`.

## Testirajte postavke

Provjerite da ste prijavljeni kako bi autentifikacija bez ključa mogla dobiti token, zatim pokrenite primjer:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ako već niste prijavljeni
mvn clean spring-boot:run
```

Trebali biste vidjeti odgovor modela `gpt-5.6-luna`. Pokrećite primjere redom da ostanete unutar male zadane kvote; ako primite HTTP 429, pričekajte interval pokušaja prije ponovnog pokušaja.

> **VS Code korisnici:** Pritisnite `F5` za pokretanje. Aplikacija automatski učitava vašu `.env`.

> **Potpuni primjer:** Pogledajte [Basic Chat s Azure AI Foundry primjer](./examples/basic-chat-azure/README.md) za detalje i rješavanje problema.

## Što slijedi?

Nakon provisioniranja i uspješnog pokretanja primjera, imat ćete:
- Azure AI Foundry s implementiranim `gpt-5.6-luna` i `text-embedding-3-small`
- Autentifikaciju bez ključeva (Microsoft Entra ID) — nema ključeva za upravljanje
- Lokalnu `.env` s vašim endpoint i imenima deploymenta
- Spremno razvojno okruženje za Javu

**Nastavite na** [Poglavlje 3: Osnovne tehnike generativne AI](../03-CoreGenerativeAITechniques/README.md) da započnete izgradnju AI aplikacija!

## Resursi

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autentifikacija bez ključeva s Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Dokumentacija Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Prijelaz Spring AI 2 na OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Službeni OpenAI Java SDK s Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Dodatni resursi

- [Preuzmite VS Code](https://code.visualstudio.com/Download)
- [Preuzmite Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Konfiguracija Dev Container](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Napomena**:
Ovaj dokument je preveden korištenjem AI prevoditeljskog servisa [Co-op Translator](https://github.com/Azure/co-op-translator). Iako težimo točnosti, imajte na umu da automatski prijevodi mogu sadržavati greške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za važne informacije preporuča se profesionalni ljudski prijevod. Nismo odgovorni za bilo kakva nesporazumevanja ili pogrešne interpretacije koje proizlaze iz korištenja ovog prijevoda.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->