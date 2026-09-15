# Alapvető chat az Azure AI Foundry-val – Végponttól végpontig példa

Ez a példa egy egyszerű Spring Boot alkalmazás, amely **Azure AI Foundry** modellhez csatlakozik **kulcs nélküli hitelesítéssel** (Microsoft Entra ID) és teszteli a beállítást. A Spring AI `ChatClient`-jét tartja meg, amelyet az **hivatalos OpenAI Java SDK** és az **Azure OpenAI v1** végpont támogat.

A [pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) verziói: Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, és dotenv-java **3.2.0**. A minta használja a `spring-ai-starter-model-openai` csomagot, illetve kifejezetten bejelenti az `openai-java` és `azure-identity` csomagokat; a Spring AI 2 eltávolította a régi Azure OpenAI startert.

## Tartalomjegyzék

- [Előfeltételek](#előfeltételek)
- [Gyors kezdés](#gyors-kezdés)
- [Hogyan működik a hitelesítés](#hogyan-működik-a-hitelesítés)
- [Az alkalmazás futtatása](#az-alkalmazás-futtatása)
  - [Maven használata](#maven-használata)
  - [VS Code használata](#vs-code-használata)
  - [Várt kimenet](#várt-kimenet)
- [Konfigurációs referencia](#konfigurációs-referencia)
  - [Környezeti változók](#környezeti-változók)
  - [Spring konfiguráció](#spring-konfiguráció)
- [Hibaelhárítás](#hibaelhárítás)
  - [Gyakori problémák](#gyakori-problémák)
  - [Hibakeresési mód](#hibakeresési-mód)
- [Következő lépések](#következő-lépések)
- [Erőforrások](#erőforrások)

## Előfeltételek

A példa futtatása előtt győződj meg róla, hogy rendelkezel:

- Egy Azure AI Foundry erőforrással, amely `gpt-5.6-luna` telepítéssel rendelkezik – hozd létre `azd up` parancssal vagy manuálisan a [Azure AI Foundry beállítási útmutatóban](../../getting-started-azure-openai.md)
- Az erőforráson a **Cognitive Services OpenAI User** szerepkörrel (a Bicep sablonok ezt automatikusan beállítják)
- Az [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), bejelentkezve `az login` használatával
- Java 21+ és Maven 3.9+

> **Nem szükséges API kulcs** — a hitelesítés kulcs nélküli, Microsoft Entra ID-n keresztül.

## Gyors kezdés

```bash
# 1. Navigáljon a projekthez
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. Jelentkezzen be, hogy a kulcs nélküli hitelesítés tokenhez jusson
az login

# 3. Állítsa be a végpontot
#    - Ha lefuttatta az `azd up` parancsot, a .env fájl automatikusan létrejött (ezt átugorhatja).
#    - Ellenkező esetben másolja a sablont, és állítsa be az AZURE_OPENAI_ENDPOINT értékét:
cp .env.example .env

# 4. Futtassa az alkalmazást
mvn spring-boot:run
```

## Hogyan működik a hitelesítés

Ez a példa a **Microsoft Entra ID** segítségével hitelesít — nincs API kulcs.

Az alkalmazás explicit módon konfigurálja a hitelesítést a [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) fájlban:

1. Az `azureCredential()` létrehoz egy `BearerTokenCredential`-t az `AuthenticationUtil.getBearerTokenSupplier` segítségével, `DefaultAzureCredential`-lel és a `https://ai.azure.com/.default` jogosultsággal.
2. Az `azureOpenAiClient()` felépít egy `OpenAIClient`-et az `OpenAIOkHttpClient.builder()` használatával, az erőforrás végpontját `/openai/v1` útvonalra állítja, és megadja a bearer hitelesítést `.credential(...)`-vel.
3. Az `azureChatModel()` átadja ezt a klienst a Spring AI `OpenAiChatModel`-jének, amely támogatja a leckében használt `ChatClient`-et.

Ezek az explicit bean-ek megakadályozzák, hogy egy globális `OPENAI_API_KEY` felülírja az Azure hitelesítést. Az API kulcs kihagyása csak a YAML-ból nem jelenti a hitelesítés beállítását. A `DefaultAzureCredential` felhasználhatja az `az login` munkamenetedet lokálisan, vagy egy Azure-ban kezelt identitást; bármely identitás, ami használatban van, rendelkeznie kell a fent említett erőforrás szerepkörrel.

## Az alkalmazás futtatása

### Maven használata

```bash
mvn spring-boot:run
```

### VS Code használata

1. Nyisd meg a projektet VS Code-ban
2. Nyomd meg az `F5` gombot vagy használd a "Futtatás és hibakeresés" panelt
3. Válaszd ki a "Spring Boot-BasicChatApplication" konfigurációt

> **Megjegyzés**: Az alkalmazás a `.env` fájlt a munkakönyvtárából tölti be, beleértve a VS Code-ból való indítást is.

### Várt kimenet

Illusztratív kimenet sikeres futtatás után (a indítási naplók el vannak hagyva; a válasz szövege eltérhet):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## Konfigurációs referencia

### Környezeti változók

| Változó | Leírás | Kötelező | Példa |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) végpont URL | Igen | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | Chat modell telepítésének neve | Nem | `gpt-5.6-luna` (alapértelmezett) |

> Nincs **api kulcs** változó — a hitelesítés kulcs nélküli (Microsoft Entra ID az `az login` által).

### Spring konfiguráció

A [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) beállítások a `spring.ai.openai` előtagot használják, és lapos chat tulajdonságokat (nincs `options` blokk):

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

A `model` az **Azure telepítés neve**. A hitelesítés az explicit bean-ekből származik, nem egy `api-key` beállításból. A példa letiltja a következtetést és korlátozza a kimeneti tokenek számát 500-ra; a `temperature` és a régi `max-tokens` nincs beállítva.

A Microsoft ajánlja az [hivatalos OpenAI SDK használatát Azure OpenAI v1 és a Responses API-val új alkalmazásokhoz](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java). A Chat Completions továbbra is támogatott ebben a meglévő üzenetalapú leckében. A GPT-5.6 esetén a Chat Completions-nél az eszközöket is tartalmazó kéréseknek a `reasoning_effort` értékét `none`-ra kell állítaniuk; ha a következtetést és eszközöket együtt használod, használd a Responses-t. Lásd: [eszközhívás következtető modellekkel](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models).

## Hibaelhárítás

### Gyakori problémák

<details>
<summary><strong>Hiba: 401 / "PermissionDenied" / token hibák</strong></summary>

- Futtasd az `az login` parancsot — a kulcs nélküli hitelesítéshez aktív bejelentkezés szükséges a token megszerzéséhez
- Ellenőrizd, hogy a fiókod rendelkezik a **Cognitive Services OpenAI User** szerepkörrel az erőforráson
- Ha most rendelted hozzá a szerepkört, várj egy percet az érvényesüléshez
- Győződj meg arról, hogy a megfelelő bérlőnél/fióknál vagy (`az account show`)
</details>

<details>
<summary><strong>Hiba: "Az endpoint nem érvényes" / kapcsolódási hibák</strong></summary>

- Győződj meg róla, hogy az `AZURE_OPENAI_ENDPOINT` a teljes alap URL (`https://your-resource.openai.azure.com/`)
- Ellenőrizd a záró perjel konzisztenciáját
- Ellenőrizd, hogy az endpoint megegyezik a telepített erőforráséval (`azd env get-values`)
</details>

<details>
<summary><strong>Hiba: "A telepítés nem található"</strong></summary>

- Ellenőrizd, hogy az `AZURE_OPENAI_DEPLOYMENT` egyezik-e a telepítés nevével az Azure-ban
- Ellenőrizd, hogy a modell sikeresen telepítve és aktív
- Az alapértelmezett telepítés neve `gpt-5.6-luna`
</details>

<details>
<summary><strong>Hiba: 429 / kvóta túllépés</strong></summary>

- Az alapértelmezett GPT-5.6 Luna telepítés Globális Standard kapacitással 10: 10 kérés/perc és 10,000 token/perc
- Futtasd egymás után a példákat, és várd meg a szolgáltatás újrapróbálkozási intervallumát
- Ez az alapvető példa letiltja az automatikus SDK újrapróbálkozásokat, így a hibás kérést azonnal jelzi
</details>

<details>
<summary><strong>VS Code: Környezeti változók nem töltődnek be</strong></summary>

- Győződj meg róla, hogy a `.env` fájl a projekt gyökérkönyvtárában van (ugyanabban a szinten, mint a `pom.xml`)
- Próbáld futtatni az `mvn spring-boot:run` parancsot VS Code integrált termináljából
- Ellenőrizd, hogy a VS Code Java kiterjesztés megfelelően telepítve van
</details>

### Hibakeresési mód

Részletes naplózás engedélyezéséhez töröld a megjegyzés jeleket a következő sorokból a [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) fájlban:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## Következő lépések

**Beállítás kész!** Folytasd tanulmányaid:

[3. fejezet: Alapvető generatív MI technikák](../../../03-CoreGenerativeAITechniques/README.md)

## Erőforrások

- [Spring AI 2 OpenAI Java SDK átállás](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Hivatalos OpenAI Java SDK Azure OpenAI v1-gyel](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Kulcs nélküli hitelesítés Microsoft Entra ID-vel](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry portál](https://ai.azure.com/)
- [Azure AI Foundry dokumentáció](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->