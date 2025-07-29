<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T16:07:18+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "hu"
}
-->
# A Java generatív AI fejlesztői környezet beállítása

> **Gyors kezdés**: Kódolás a felhőben 2 perc alatt - ugorj a [GitHub Codespaces beállítás](../../../02-SetupDevEnvironment) részhez - nincs szükség helyi telepítésre, és GitHub modelleket használ!

> **Érdekel az Azure OpenAI?** Lásd az [Azure OpenAI beállítási útmutatót](getting-started-azure-openai.md), amely lépésről lépésre bemutatja, hogyan hozhatsz létre egy új Azure OpenAI erőforrást.

## Amit megtanulsz

- Java fejlesztői környezet beállítása AI alkalmazásokhoz
- A preferált fejlesztői környezet kiválasztása és konfigurálása (felhőalapú Codespaces, helyi fejlesztői konténer vagy teljes helyi beállítás)
- A beállítás tesztelése GitHub modellekhez való csatlakozással

## Tartalomjegyzék

- [Amit megtanulsz](../../../02-SetupDevEnvironment)
- [Bevezetés](../../../02-SetupDevEnvironment)
- [1. lépés: A fejlesztői környezet beállítása](../../../02-SetupDevEnvironment)
  - [Opció A: GitHub Codespaces (Ajánlott)](../../../02-SetupDevEnvironment)
  - [Opció B: Helyi fejlesztői konténer](../../../02-SetupDevEnvironment)
  - [Opció C: Meglévő helyi telepítés használata](../../../02-SetupDevEnvironment)
- [2. lépés: GitHub személyes hozzáférési token létrehozása](../../../02-SetupDevEnvironment)
- [3. lépés: A beállítás tesztelése](../../../02-SetupDevEnvironment)
- [Hibaelhárítás](../../../02-SetupDevEnvironment)
- [Összefoglalás](../../../02-SetupDevEnvironment)
- [Következő lépések](../../../02-SetupDevEnvironment)

## Bevezetés

Ez a fejezet végigvezet a fejlesztői környezet beállításán. A **GitHub modelleket** fogjuk használni elsődleges példaként, mert ingyenes, egyszerűen beállítható egy GitHub fiókkal, nem igényel hitelkártyát, és több modellhez is hozzáférést biztosít kísérletezés céljából.

**Nincs szükség helyi beállításra!** Azonnal elkezdhetsz kódolni a GitHub Codespaces használatával, amely teljes fejlesztői környezetet biztosít a böngésződben.

<img src="./images/models.webp" alt="Képernyőkép: GitHub modellek" width="50%">

A [**GitHub modellek**](https://github.com/marketplace?type=models) használatát javasoljuk ehhez a kurzushoz, mert:
- **Ingyenes** a kezdéshez
- **Egyszerű** beállítani egy GitHub fiókkal
- **Nincs szükség hitelkártyára**
- **Több modell** érhető el kísérletezéshez

> **Megjegyzés**: Az ebben a képzésben használt GitHub modellek ingyenes korlátai:
> - 15 kérés percenként (150 naponta)
> - ~8,000 szó bemenet, ~4,000 szó kimenet kérésenként
> - 5 egyidejű kérés
> 
> Termelési használathoz frissíts az Azure AI Foundry modellekre az Azure fiókoddal. A kódod nem igényel változtatást. Lásd az [Azure AI Foundry dokumentációt](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models).

## 1. lépés: A fejlesztői környezet beállítása

<a name="quick-start-cloud"></a>

Előre konfigurált fejlesztői konténert hoztunk létre, hogy minimalizáljuk a beállítási időt, és biztosítsuk, hogy minden szükséges eszköz rendelkezésre álljon ehhez a Java generatív AI kurzushoz. Válaszd ki a preferált fejlesztési megközelítést:

### Környezetbeállítási opciók:

#### Opció A: GitHub Codespaces (Ajánlott)

**Kódolás 2 perc alatt - nincs szükség helyi beállításra!**

1. Forkold ezt a repót a GitHub fiókodba  
   > **Megjegyzés**: Ha módosítani szeretnéd az alapkonfigurációt, nézd meg a [Dev Container Configuration](../../../.devcontainer/devcontainer.json) fájlt.
2. Kattints a **Code** → **Codespaces** fülre → **...** → **New with options...**
3. Használd az alapértelmezett beállításokat – ez kiválasztja a **Dev container configuration**: **Generative AI Java Development Environment** egyedi devcontainer-t, amelyet ehhez a kurzushoz hoztunk létre.
4. Kattints a **Create codespace** gombra.
5. Várj ~2 percet, amíg a környezet elkészül.
6. Folytasd a [2. lépés: GitHub token létrehozása](../../../02-SetupDevEnvironment) résszel.

<img src="./images/codespaces.png" alt="Képernyőkép: Codespaces almenü" width="50%">

<img src="./images/image.png" alt="Képernyőkép: Új opciókkal" width="50%">

<img src="./images/codespaces-create.png" alt="Képernyőkép: Codespace létrehozási opciók" width="50%">

> **A Codespaces előnyei**:
> - Nincs szükség helyi telepítésre
> - Bármilyen böngészővel rendelkező eszközön működik
> - Előre konfigurált minden eszközzel és függőséggel
> - Havi 60 óra ingyenes személyes fiókokhoz
> - Konzisztens környezet minden tanuló számára

#### Opció B: Helyi fejlesztői konténer

**Fejlesztőknek, akik a helyi fejlesztést preferálják Dockerrel**

1. Forkold és klónozd ezt a repót a helyi gépedre  
   > **Megjegyzés**: Ha módosítani szeretnéd az alapkonfigurációt, nézd meg a [Dev Container Configuration](../../../.devcontainer/devcontainer.json) fájlt.
2. Telepítsd a [Docker Desktop](https://www.docker.com/products/docker-desktop/) és [VS Code](https://code.visualstudio.com/) alkalmazásokat.
3. Telepítsd a [Dev Containers bővítményt](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) a VS Code-ban.
4. Nyisd meg a repó mappáját a VS Code-ban.
5. Amikor megjelenik a kérdés, kattints a **Reopen in Container** gombra (vagy használd a `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" parancsot).
6. Várj, amíg a konténer felépül és elindul.
7. Folytasd a [2. lépés: GitHub token létrehozása](../../../02-SetupDevEnvironment) résszel.

<img src="./images/devcontainer.png" alt="Képernyőkép: Dev konténer beállítás" width="50%">

<img src="./images/image-3.png" alt="Képernyőkép: Dev konténer építés befejezve" width="50%">

#### Opció C: Meglévő helyi telepítés használata

**Fejlesztőknek, akik már rendelkeznek Java környezettel**

Előfeltételek:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) vagy a preferált IDE-d

Lépések:
1. Klónozd ezt a repót a helyi gépedre.
2. Nyisd meg a projektet az IDE-dben.
3. Folytasd a [2. lépés: GitHub token létrehozása](../../../02-SetupDevEnvironment) résszel.

> **Profi tipp**: Ha alacsony teljesítményű géped van, de helyben szeretnéd használni a VS Code-ot, próbáld ki a GitHub Codespaces-t! Csatlakoztathatod a helyi VS Code-ot egy felhőben futó Codespace-hez, így a legjobbat kapod mindkét világból.

<img src="./images/image-2.png" alt="Képernyőkép: helyi devcontainer példány létrehozva" width="50%">

## 2. lépés: GitHub személyes hozzáférési token létrehozása

1. Nyisd meg a [GitHub beállításokat](https://github.com/settings/profile), és válaszd a **Settings** lehetőséget a profilmenüben.
2. A bal oldali sávban kattints a **Developer settings** lehetőségre (általában alul).
3. A **Personal access tokens** alatt kattints a **Fine-grained tokens** lehetőségre (vagy használd ezt a közvetlen [linket](https://github.com/settings/personal-access-tokens)).
4. Kattints a **Generate new token** gombra.
5. A "Token name" mezőbe adj meg egy leíró nevet (pl. `GenAI-Java-Course-Token`).
6. Állíts be egy lejárati dátumot (ajánlott: 7 nap a biztonsági legjobb gyakorlatok érdekében).
7. A "Resource owner" alatt válaszd ki a felhasználói fiókodat.
8. A "Repository access" alatt válaszd ki azokat a repókat, amelyeket használni szeretnél a GitHub modellekkel (vagy "All repositories", ha szükséges).
9. A "Repository permissions" alatt keresd meg a **Models** opciót, és állítsd **Read and write** értékre.
10. Kattints a **Generate token** gombra.
11. **Másold és mentsd el a tokened most** – később nem fogod látni!

> **Biztonsági tipp**: Használj minimális szükséges jogosultságokat és a lehető legrövidebb lejárati időt a hozzáférési tokenekhez.

## 3. lépés: A beállítás tesztelése a GitHub modellek példával

Miután a fejlesztői környezeted készen áll, teszteljük a GitHub modellek integrációját a [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) mappában található példával.

1. Nyisd meg a terminált a fejlesztői környezetedben.
2. Navigálj a GitHub modellek példához:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. Állítsd be a GitHub tokened környezeti változóként:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. Futtasd az alkalmazást:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

A következőhöz hasonló kimenetet kell látnod:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### A példa kód megértése

Először értsük meg, mit futtattunk. Az `examples/github-models` mappában található példa az OpenAI Java SDK-t használja a GitHub modellekhez való csatlakozáshoz:

**Mit csinál ez a kód:**
- **Csatlakozik** a GitHub modellekhez a személyes hozzáférési tokened használatával
- **Küld** egy egyszerű "Say Hello World!" üzenetet az AI modellnek
- **Fogadja** és megjeleníti az AI válaszát
- **Ellenőrzi**, hogy a beállításod megfelelően működik-e

**Fő függőség** (a `pom.xml` fájlban):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**Fő kód** (`App.java`):
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

## Összefoglalás

Gratulálunk! Most már minden készen áll:

- Létrehoztál egy GitHub személyes hozzáférési tokent a megfelelő jogosultságokkal az AI modellek eléréséhez.
- Beállítottad a Java fejlesztői környezetedet (legyen az Codespaces, fejlesztői konténer vagy helyi).
- Csatlakoztál a GitHub modellekhez az OpenAI Java SDK segítségével az ingyenes AI fejlesztéshez.
- Tesztelted, hogy minden működik egy egyszerű példával, amely AI modellekkel kommunikál.

## Következő lépések

[3. fejezet: Alapvető generatív AI technikák](../03-CoreGenerativeAITechniques/README.md)

## Hibaelhárítás

Problémákba ütköztél? Íme néhány gyakori probléma és megoldás:

- **A token nem működik?** 
  - Győződj meg róla, hogy a teljes tokent másoltad, extra szóközök nélkül.
  - Ellenőrizd, hogy a token helyesen van-e beállítva környezeti változóként.
  - Ellenőrizd, hogy a tokened rendelkezik-e a megfelelő jogosultságokkal (Models: Read and write).

- **Maven nem található?** 
  - Ha fejlesztői konténereket/Codespaces-t használsz, a Maven előre telepítve van.
  - Helyi beállítás esetén győződj meg róla, hogy a Java 21+ és Maven 3.9+ telepítve van.
  - Próbáld ki a `mvn --version` parancsot a telepítés ellenőrzéséhez.

- **Kapcsolódási problémák?** 
  - Ellenőrizd az internetkapcsolatodat.
  - Győződj meg róla, hogy a GitHub elérhető a hálózatodból.
  - Ellenőrizd, hogy nem vagy-e olyan tűzfal mögött, amely blokkolja a GitHub modellek végpontját.

- **A fejlesztői konténer nem indul el?** 
  - Győződj meg róla, hogy a Docker Desktop fut (helyi fejlesztés esetén).
  - Próbáld újraépíteni a konténert: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container".

- **Az alkalmazás fordítási hibákat ad?**
  - Győződj meg róla, hogy a megfelelő könyvtárban vagy: `02-SetupDevEnvironment/examples/github-models`
  - Próbáld meg tisztítani és újraépíteni: `mvn clean compile`

> **Segítségre van szükséged?**: Még mindig problémáid vannak? Nyiss egy hibajegyet a repóban, és segítünk.

**Felelősség kizárása**:  
Ez a dokumentum az AI fordítási szolgáltatás [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével lett lefordítva. Bár törekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelvén tekintendő hiteles forrásnak. Fontos információk esetén javasolt professzionális emberi fordítást igénybe venni. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely a fordítás használatából eredhet.