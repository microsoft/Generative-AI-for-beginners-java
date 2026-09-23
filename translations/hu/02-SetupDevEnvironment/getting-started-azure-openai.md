# Fejlesztői környezet beállítása az Azure AI Foundry-hoz

> Ez az útmutató beállítja az **Azure AI Foundry** modelleket a jelen tanfolyam Java AI alkalmazásaihoz, **kulcs nélküli** hitelesítést használva (Microsoft Entra ID) — nincs API-kulcs, amit kezelni kell. Új vagy az eszközökkel? Kezdd a [fejlesztői környezet útmutatóval](./README.md).

Ez az útmutató beállítja az **Azure AI Foundry** modelleket a jelen tanfolyam Java AI alkalmazásaihoz. Két út áll rendelkezésedre:

- **A lehetőség — Telepítés `azd` + Bicep használatával (ajánlott):** egy parancs telepíti a Foundry fiókot és a modelleket kódként. Nem szükséges portálon kattintani.
- **B lehetőség — Erőforrások kézi létrehozása** az Azure AI Foundry portálon.

Mindkét út **kulcs nélküli hitelesítést** (Microsoft Entra ID) használ — nincs API-kulcs, amit másolni vagy véletlenül kiszivárogtatni kellene.

## Tartalomjegyzék

- [Mi jön létre](#mi-jön-létre)
- [Előfeltételek](#előfeltételek)
- [A lehetőség: Telepítés az azd + Bicep segítségével (Ajánlott)](#option-a-provision-with-azd--bicep-recommended)
- [B lehetőség: Erőforrások kézi létrehozása](#b-lehetőség-erőforrások-kézi-létrehozása)
- [Környezet beállítása](#környezet-beállítása)
- [Teszteld a beállítást](#teszteld-a-beállítást)
- [Mi a következő?](#mi-a-következő)
- [Erőforrások](#erőforrások)
- [További erőforrások](#további-erőforrások)

## Mi jön létre

Az [`infra/`](../../../02-SetupDevEnvironment/infra) Bicep sablonok előállítják:

- Egy **Azure AI Foundry** fiókot (`Microsoft.CognitiveServices/accounts`, `AIServices` típus) egy projekttel
- Egy **chat** telepítést - GPT-5.6 Luna (`gpt-5.6-luna`), `2026-07-09` verzióval, `GlobalStandard` kapacitás `10` (10 kérés/perc és 10,000 token/perc a modellhez)
- Egy **embedding** telepítést - `text-embedding-3-small`, `1` verzió (későbbi fejezetekben használva)
- Egy **kulcs nélküli szerepkör hozzárendelést** (`Cognitive Services OpenAI User`), így az `az login` használatával jelentkezel be kulcsok kezelése nélkül

## Előfeltételek

- Egy [Azure előfizetés](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) és [Maven 3.9+](https://maven.apache.org/download.cgi)

## A lehetőség: Telepítés az azd + Bicep segítségével (Ajánlott)

A `02-SetupDevEnvironment` mappából:

```bash
cd 02-SetupDevEnvironment

# Bejelentkezés (mindkét eszköz)
azd auth login
az login

# A Foundry fiók és modelltelepítések előkészítése
azd up
```

Az `azd` egy **környezeti nevet** kér be (például `genai-java`), **előfizetést** és **régiót**. Válassz saját előfizetést és egy régiót, ahol a `gpt-5.6-luna` és a `text-embedding-3-small` elérhető, például `eastus2`. Győződj meg arról, hogy az előfizetés elegendő kvótával rendelkezik az adott régióban a modell és a telepítés típusához; a rendelkezésre állás és kvóta előfizetéstől függően változik.

Amikor a telepítés befejeződik, az azd:

1. Telepíti az összes, a [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) fájlban definiált erőforrást.
2. Lefuttat egy utólagos hook-ot, ami létrehozza a [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) fájlt az endpoint és a telepítési nevek megadásával (titkok nélkül).

> **Tipp:** Futtasd újra bármikor az `azd up` parancsot a változtatások alkalmazásához. Az `azd down` törli az összes erőforrást és megállítja a költségeket.

A generált beállítások megtekintéséhez:

```bash
azd env get-values
```

Most ugorj a [Teszteld a beállítást](#teszteld-a-beállítást) részhez.

## B lehetőség: Erőforrások kézi létrehozása

Ha inkább a portált használod, hozd létre az erőforrásokat kézzel:

1. Menj az [Azure AI Foundry portálra](https://ai.azure.com/) és jelentkezz be.
2. **Hozz létre egy projektet** (ez létrehoz egy AI Foundry erőforrást is). Adj neki nevet, például `GenAIJava`.
3. A projektben nyisd meg a **Modellek + végpontok** → **Modell telepítése** → **Alapmodell telepítése** menüpontot.
4. Telepítsd a **GPT-5.6 Luna** modellt és telepítést (`gpt-5.6-luna` névvel, `2026-07-09` verzió), **Global Standard** kapacitással `10`. Ismételd meg a **text-embedding-3-small** (`1` verzió) esetén, ha az embedding példákat is használni szeretnéd.
5. Az **Áttekintés** nézetből másold ki a **végpont URL-jét** (például `https://<resource>.openai.azure.com/`).
6. Adj magadnak kulcs nélküli hozzáférést: az erőforrásnál nyisd meg a **Hozzáférés-kezelés (IAM)** → **Szerepkör hozzárendelés hozzáadása** → rendeld hozzá a **Cognitive Services OpenAI User** szerepkört a fiókodhoz.

> **Még mindig gond van?** Nézd meg az [Azure AI Foundry dokumentációját](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects).

## Környezet beállítása

**Ha az A lehetőséget választottad (`azd up`),** a beállítási fájlod már elkészült — nincs mit konfigurálni. Ugorj a [Teszteld a beállítást](#teszteld-a-beállítást) részhez.

**Ha a B lehetőséget választottad (kézi),** hozd létre magadnak az example `.env` fájlját:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

Szerkeszd az `.env` fájlt a végpontoddal (nincs kulcs — a hitelesítés kulcs nélküli):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

Használd az erőforrás Azure OpenAI végpontját, ne projekt URL-t. A basic-chat app ezt `/openai/v1`-re fordítja le, és egy explicit bearer-token klienst konfigurál; API-kulcs nem szükséges.

> **Biztonsági megjegyzés:** Nincs API-kulcs tárolni való. A hitelesítés Microsoft Entra ID-n keresztül történik az `az login` (helyi) vagy egy kezelt identitás (Azure-ban) segítségével. Az `.env` fájl csak nem titkos beállításokat tartalmaz, és már szerepel a `.gitignore`-ban.

## Teszteld a beállítást

Győződj meg róla, hogy be vagy jelentkezve, hogy a kulcs nélküli hitelesítés tokenhez jusson, majd futtasd az példát:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # ha még nem jelentkeztél be
mvn clean spring-boot:run
```

Választ kell kapnod a `gpt-5.6-luna` modelltől. Futtasd az példákat egymás után, hogy a kis alapkvótán belül maradj; ha HTTP 429 választ kapsz, várd meg a retry intervallumot, mielőtt újra próbálkozol.

> **VS Code felhasználók:** Nyomd meg az `F5` gombot a futtatáshoz. Az app automatikusan betölti az `.env` fájlt.

> **Teljes példa:** Részletekért és hibakeresésért lásd a [Basic Chat az Azure AI Foundry-val példát](./examples/basic-chat-azure/README.md).

## Mi a következő?

A telepítés és az példa sikeres futtatása után rendelkezel:
- Azure AI Foundry-val, melyen a `gpt-5.6-luna` és `text-embedding-3-small` telepítve van
- Kulcs nélküli hitelesítéssel (Microsoft Entra ID) — nincs kulcs, amit kezelni kellene
- Egy helyi `.env` fájllal, amely tartalmazza a végpont és a telepítési neveket
- Egy Java fejlesztői környezettel, ami készen áll a használatra

**Folytasd a** [3. fejezettel: Alapvető generatív AI technikák](../03-CoreGenerativeAITechniques/README.md), hogy elkezd az AI alkalmazások fejlesztését!

## Erőforrások

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Kulcs nélküli hitelesítés Microsoft Entra ID használatával](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry dokumentáció](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK átállás](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Hivatalos OpenAI Java SDK Azure OpenAI v1-hez](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## További erőforrások

- [VS Code letöltése](https://code.visualstudio.com/Download)
- [Docker Desktop beszerzése](https://www.docker.com/products/docker-desktop)
- [Dev Container konfiguráció](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->