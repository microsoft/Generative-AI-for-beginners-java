# Configurarea mediului de dezvoltare pentru Azure AI Foundry

> Acest ghid configurează modelele **Azure AI Foundry** pentru aplicațiile Java AI din acest curs, folosind autentificare **fără cheie** (Microsoft Entra ID) — fără chei API de gestionat. Ești nou în acestel instrumente? Începe cu [ghidul mediului de dezvoltare](./README.md).

Acest ghid configurează modelele **Azure AI Foundry** pentru aplicațiile Java AI din acest curs. Ai două variante:

- **Opțiunea A — Provisionare cu `azd` + Bicep (recomandat):** o singură comandă deployează contul Foundry și modelele ca cod. Fără click-uri în portal.
- **Opțiunea B — Creează resursele manual** din portalul Azure AI Foundry.

Ambele opțiuni folosesc **autentificare fără cheie** (Microsoft Entra ID) — nu există chei API de copiat sau scurs.

## Cuprins

- [Ce se creează](#ce-se-creează)
- [Prerechizite](#prerechizite)
- [Opțiunea A: Provisionare cu azd + Bicep (Recomandat)](#option-a-provision-with-azd--bicep-recommended)
- [Opțiunea B: Creează resurse manual](#opțiunea-b-creează-resurse-manual)
- [Configurează-ți mediul](#configurează-ți-mediul)
- [Testează configurația](#testează-configurația)
- [Ce urmează?](#ce-urmează)
- [Resurse](#resurse)
- [Resurse suplimentare](#resurse-suplimentare)

## Ce se creează

Șabloanele Bicep din [`infra/`](../../../02-SetupDevEnvironment/infra) fac provision pentru:

- Un cont **Azure AI Foundry** (`Microsoft.CognitiveServices/accounts`, tip `AIServices`) cu un proiect
- O implementare **chat** - GPT-5.6 Luna (`gpt-5.6-luna`), versiunea `2026-07-09`, cu capacitate `GlobalStandard` `10` (10 cereri/minut și 10.000 de tokeni/minut pentru acest model)
- O implementare **embedding** - `text-embedding-3-small`, versiunea `1` (folosit în capitolele următoare)
- O **atribuire de rol fără cheie** (`Cognitive Services OpenAI User`) astfel încât te conectezi cu `az login` în loc să gestionezi chei

## Prerechizite

- Un [abonament Azure](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) și [Maven 3.9+](https://maven.apache.org/download.cgi)

## Opțiunea A: Provisionare cu azd + Bicep (Recomandat)

Din dosarul `02-SetupDevEnvironment`:

```bash
cd 02-SetupDevEnvironment

# Autentificare (ambele unelte)
azd auth login
az login

# Provisionare cont Foundry + implementări modele
azd up
```

`azd` îți cere un **nume de mediu** (de exemplu `genai-java`), **abonament** și **regiune**. Alege abonamentul tău și o regiune unde `gpt-5.6-luna` și `text-embedding-3-small` sunt disponibile, de exemplu `eastus2`. Confirmă că abonamentul are cotă suficientă pentru model și tipul implementării în acea regiune; disponibilitatea și cota variază în funcție de abonament.

Când provisionarea se termină, azd:

1. Deployează tot ce este definit în [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep).
2. Rulează un hook post-provisionare care scrie [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) cu endpoint-ul și numele implementărilor tale (fără secrete).

> **Sfat:** Rulează din nou `azd up` oricând pentru a aplica modificări. Rulează `azd down` pentru a șterge tot și a opri costurile.

Pentru a vedea setările generate:

```bash
azd env get-values
```

Acum sari la [Testează configurația](#testează-configurația).

## Opțiunea B: Creează resurse manual

Preferi portalul? Creează resursele manual:

1. Accesează [portalul Azure AI Foundry](https://ai.azure.com/) și conectează-te.
2. **Creează un proiect** (asta creează și o resursă AI Foundry). Dă-i un nume ca `GenAIJava`.
3. În proiectul tău, deschide **Models + endpoints** → **Deploy model** → **Deploy base model**.
4. Deployează **GPT-5.6 Luna** (model și nume implementare `gpt-5.6-luna`, versiunea `2026-07-09`) cu capacitatea **Global Standard** `10`. Repetă pentru **text-embedding-3-small**, versiunea `1`, dacă vrei exemplele embedding.
5. Din **Overview**, copiază **endpoint-ul** (de exemplu `https://<resource>.openai.azure.com/`).
6. Acordă-ți acces fără cheie: pe resursă, deschide **Access control (IAM)** → **Add role assignment** → atribuie **Cognitive Services OpenAI User** contului tău.

> **Încă ai probleme?** Vezi [documentația Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Configurează-ți mediul

**Dacă ai folosit Opțiunea A (`azd up`)**, fișierul tău de setări este deja scris — nu ai nevoie să configurezi nimic. Sari la [Testează configurația](#testează-configurația).

**Dacă ai folosit Opțiunea B (manual)**, creează singur fișierul `.env` pentru exemplu:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Editează `.env` cu endpoint-ul tău (fără cheie — autentificarea e fără cheie):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Folosește endpoint-ul Azure OpenAI al resursei, nu un URL al proiectului. Aplicația basic-chat îl rezolvă ca `/openai/v1` și configurează un client explicit cu token bearer; o cheie API nu este necesară.

> **Notă de securitate:** Nu există o cheie API de stocat. Te autentifici cu Microsoft Entra ID prin `az login` (local) sau identitate gestionată (în Azure). Fișierul `.env` conține doar setări ne-confidențiale și este deja exclus din `.gitignore`.

## Testează configurația

Asigură-te că ești conectat pentru ca autentificarea fără cheie să obțină un token, apoi rulează exemplul:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # dacă nu sunteți deja autentificat
mvn clean spring-boot:run
```

Ar trebui să vezi un răspuns de la modelul `gpt-5.6-luna`. Rulează exemplele în ordine pentru a rămâne în cota mică implicită; dacă primești HTTP 429, așteaptă intervalul de retry înainte să încerci din nou.

> **Utilizatori VS Code:** Apasă `F5` pentru a rula. Aplicația încarcă automat fișierul `.env`.

> **Exemplu complet:** Vezi [Exemplul Basic Chat cu Azure AI Foundry](./examples/basic-chat-azure/README.md) pentru detalii și depanenare.

## Ce urmează?

După provisionare și rularea reușită a exemplului, vei avea:
- Azure AI Foundry cu `gpt-5.6-luna` și `text-embedding-3-small` implementate
- Autentificare fără cheie (Microsoft Entra ID) — fără chei de gestionat
- Un `.env` local cu endpoint-ul și numele implementărilor
- Un mediu de dezvoltare Java gata de utilizat

**Continuă cu** [Capitolul 3: Tehnici Generative AI de bază](../03-CoreGenerativeAITechniques/README.md) pentru a începe să construiești aplicații AI!

## Resurse

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Autentificare fără cheie cu Microsoft Entra ID](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Documentația Azure AI Foundry](https://learn.microsoft.com/azure/ai-foundry/)
- [Tranziția Spring AI 2 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [SDK OpenAI Java oficial cu Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## Resurse suplimentare

- [Descarcă VS Code](https://code.visualstudio.com/Download)
- [Obține Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Configurare container de dezvoltare](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Declinare a responsabilității**:
Acest document a fost tradus folosind serviciul de traducere AI [Co-op Translator](https://github.com/Azure/co-op-translator). În timp ce ne străduim pentru acuratețe, vă rugăm să rețineți că traducerile automate pot conține erori sau inexactități. Documentul original în limba sa nativă trebuie considerat sursa autorizată. Pentru informații critice, se recomandă traducerea profesională realizată de un om. Nu ne asumăm responsabilitatea pentru eventualele neînțelegeri sau interpretări greșite care decurg din utilizarea acestei traduceri.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->