# Alapvető Generatív AI Technikák Bemutatója

## Tartalomjegyzék

- [Előfeltételek](#előfeltételek)
- [Első lépések](#első-lépések)
- [Modellválasztási Útmutató](#modellválasztási-útmutató)
- [Bemutató 1: LLM kitöltések és csevegés](#bemutató-1-llm-kitöltések-és-csevegés)
- [Bemutató 2: Funkció hívás](#bemutató-2-funkció-hívás)
- [Bemutató 3: RAG (Retrieval-Augmented Generation)](#bemutató-3-rag-retrieval-augmented-generation)
- [Bemutató 4: Felelős AI](#bemutató-4-felelős-ai)
- [Gyakori Minták a Példákban](#gyakori-minták-a-példákban)
- [Egységtesztek](#egységtesztek)
- [Folyamatos Élő Ellenőrzés](#folyamatos-élő-ellenőrzés)
- [Hibaelhárítás](#hibakeresés)
- [Következő lépések](#következő-lépések)

## Áttekintés

Négy különálló Java program mutat be csevegést, beszélgetés történetet, funkció hívást, egész dokumentumos visszakereséses generálást (RAG), és felelős AI válaszkezelést. Minden csevegési kérés alapértelmezés szerint a **GPT-5.6 Luna-ra irányul, `none` érvelési erőfeszítéssel**.

Ezek a példák az Azure OpenAI v1 végpontjával az OpenAI hivatalos Java SDK-ját használják, a [Microsoft SDK útmutatása](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) szerint. A régebbi `azure-ai-openai` csomag már nem függőség. A Chat Completions megmaradt az üzenetalapú munkafolyamatok bemutatására; további API lehetőségekért lásd az [OpenAI Java SDK-t](https://github.com/openai/openai-java#microsoft-azure).

## Előfeltételek

- Java 21 vagy újabb és Maven 3.6.3 vagy újabb.
- Egy Azure OpenAI csevegési telepítés `gpt-5.6-luna` névvel, vagy ehhez kompatibilis Chat Completions beállításokkal rendelkező helyettesítés.
- Egy bejelentkezett Azure identitás, amely rendelkezik a **Cognitive Services OpenAI User** szerepkörrel az erőforráson. Helyi fejlesztéshez az Azure CLI bejelentkezést használja; hosztolt alkalmazások menedzselt identitást használhatnak.
- Lásd a [2. fejezetet](../02-SetupDevEnvironment/getting-started-azure-openai.md) az erőforrás beállításához és bejelentkezési utasításokhoz.

A [Maven konfiguráció](../../../03-CoreGenerativeAITechniques/examples/pom.xml) rögzíti ezeket a verziókat, 2026-09-14-i ellenőrzéssel:

| Összetevő | Verzió | Cél |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Hivatalos Azure v1-kompatibilis kliens |
| `com.azure:azure-identity` | 1.18.6 | Kulcs nélküli hitelesítés és token frissítés |
| `net.objecthunter:exp4j` | 0.4.8 | Arimatikai kifejezés elemzés kódértékelés nélkül |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter egységtesztek |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 fordítás, tesztek, futtatható példák |

A fordító `--release 21`-et használ. Ezekhez a különálló példákhoz nincs szükség Spring Boot, Spring AI vagy LangChain4j függőségre.

## Első lépések

A repository gyökeréből állítsa be az erőforrás végpontját és opcionális telepítési helyettesítést a shell-ben.

**Windows PowerShell:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**Linux/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

A tesztekhez nem szükséges Azure hitelesítés vagy végpont. A Maven nem olvas automatikusan környezeti fájlt; állítsa be a változókat abban a shell-ben, amelyből az élő példákat indítja. IDE-ből indítva ellenőrizze az indítási konfigurációból adott környezetet.

## Modellválasztási Útmutató

| Környezeti változó | Jelentése | Alapértelmezett |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure erőforrás gyökér vagy már normalizált `/openai/v1` URL | Kötelező élő futtatáshoz |
| `AZURE_OPENAI_DEPLOYMENT` | Csevegési telepítés neve, nem modell verzió | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Külön beágyazási telepítés konfiguráció, ezt a négy program nem használja | `text-embedding-3-small` |

Üres telepítési felülírások az alapértelmezett értékeket használják. A konfiguráció pontosan egyszer hozzáfűzi a `/openai/v1` végpontot, és elutasítja a hitelesítő adatokat, lekérdezési karakterláncokat, valamint régi telepítési útvonalakat a végpontban.

Minden csevegési kérés explicit módon beállítja a `reasoningEffort(ReasoningEffort.NONE)` és `maxCompletionTokens(...)` értékeket. Egyetlen kérés sem állít be `temperature`, `top_p`, vagy a régi completion-token opciót. Ez érvényes az eszközválasztásra és eszköz eredménykövetésre is. A GPT-5.6 Chat Completions funkcióeszközök érvelési erőfeszítése `none`; lásd [Microsoft chat útmutatását](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Ebben a fejezetben nincs streaming vagy beágyazási belépési pont.** Az olvasó a teljes dokumentumot lekéri, nem vektorokat. Ha beágyazásokkal bővíti, használjon külön beágyazási telepítést, például `text-embedding-3-small`-t, soha ne Lunát.

## Bemutató 1: LLM kitöltések és csevegés

Forrás: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

A program futtat egy egyszerű Java streams magyarázatot, egy kétfordulós HashMap/TreeMap beszélgetést és interaktív csevegést. A második forduló az első asszisztens választ tartalmazza; minden interaktív forduló elküldi az előző beszélgetést is.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

A `config.chatOptions(...)` szolgáltatja a telepítést és az explicit érvelési beállítást. Az interaktív csevegés kihagyja az üres sorokat, `exit`-re vagy EOF-re ér véget, és megtartja a rendszerüzenetet plusz kilenc befejezett felhasználó/asszisztens fordulót. A fordulószám korlátozás oktatási célú, nem pontos tokenkeret-garancia.

A példakönyvtárból:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Várható három kezdeti válasz, majd a `Te:` prompt. Minden nem üres interaktív kérdés egy kérést ad hozzá. A kitöltési limitek 200, 300, 400, majd 500 token fordulónként.

## Bemutató 2: Funkció hívás

Forrás: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

Az SDK JSON sémákat származtat a jelölt `WeatherArguments` és `CalculationArguments` rekordokból. Egy kötelező eszköz választás minden példánál az eszköz protokoll használatát eredményezi a modell önálló válasza helyett.

1. Kérdés elküldése az engedélyezett eszközzel, érvelési erőfeszítéssel `none` és 300-token kitöltési korláttal.
2. Követelje a `tool_calls` befejezési okot, validálja a funkcióneveket és hívás azonosítókat, majd elemezze a típusos JSON argumentumokat.
3. Végezze el a helyi funkciót. A modell nem futtat Java vagy tetszőleges kódot.
4. Adja hozzá az asszisztens eszköz-hívás üzenetét egyszer, majd minden eredményt a hozzá tartozó `tool_call_id`-val.
5. Küldjön egy utolsó 300-token kérdést eszközök nélkül, és igényeljen egy befejezett, nem üres választ.

A `get_weather` **szimulált**, nem élő időjárást ad vissza. Tiszteletben tartja a várost és a bemutató 22 Celsius-fokot Fahrenheit-be konvertálja, ha kéri. A `calculate` az exp4j-val értékeli ki a megadott kifejezést, támogatja az olyan formákat, mint `15% of 240` és `2 + 3 * 4`, és elutasítja az üres, túlméretes, érvénytelen vagy nem véges számításokat. Lebegőpontos aritmetikát használ, nem pénzügyi decimális pontosságot.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Várható a `Function: get_weather`, szimulált Seattle időjárás, `Function: calculate`, `Function result: 36`, és a két utolsó válasz. Nem szükséges stdin vagy külső időjárási hitelesítés. Egy sikeres futás pontosan négy csevegési kérdést használ.

## Bemutató 3: RAG (Retrieval-Augmented Generation)

Forrás: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Bemenet: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Ez a bevezető RAG példa egy teljes UTF-8 dokumentumot kér le, és azt a felhasználói üzenettel küldi el a kérdéssel együtt. Egy külön rendszerüzenet utasítja a modellt, hogy a dokumentum tartalmát megbízhatatlan adatként kezelje, és csak ebből a kontextusból válaszoljon. Ha a dokumentum nem tartalmazza a választ, a kért válasz: `Nem találom ezt az információt a megadott dokumentumban.`

A lekötés csökkentheti a téveszméket, de sem a határolók, sem a rendszerutasítások nem garantálják a pontosságot vagy nem akadályozzák meg az összes promptbeszúrást. Ellenőrizze az élő válaszokat. A gyártási RAG általában tartalmaz darabolást, visszakeresést, hivatkozásokat, hozzáférés-ellenőrzést és értékelést.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Írjon be egy kérdést, például `Mely hitelesítési módszert írja le a dokumentum?`. Várjon választ, amely említi a Microsoft Entra ID-t. A program egy csevegési kérés után bezár 500-token kitöltési korlát mellett.

Az alapértelmezett fájlkeresés a repository gyökérből, a fejezet könyvtárából vagy a példakönyvtárból működik. Egy explicit útvonal is támogatott:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

A bemenetek nem lehetnek üresek: legfeljebb 32 KiB UTF-8 dokumentumadat és 2,000 kérdéskarakter. Hiányzó fájlok, üres/EOF kérdések, túl nagy bemenetek sikertelenek az inferencia előtt.

## Bemutató 4: Felelős AI

Forrás: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

A hat próba a káros utasításokat, gyűlöletbeszédet, adatvédelmet, orvosi félretájékoztatást, illegális tartalmat és egy jóindulatú felelős AI kérdést fedi le. A program a választ figyeli meg, nem feltételezi, hogy minden próba szűrőt vált ki.

| Eredmény | Bizonyíték |
| --- | --- |
| `SZŰRÖZVE` | Egy explicit `content_filter` / `ResponsibleAIPolicyViolation` hiba kód vagy kitöltést befejező `content_filter` ok |
| `ELUTASÍTVA` | Egy nem üres strukturált `message.refusal` mező |
| `LEHETSÉGES_ELUTASÍTÁS` | Egy nyitó elutasító kifejezés hétköznapi szövegben; egy heurisztika, amely további vizsgálatot igényel |
| `GENERÁLT` | Egy befejezett, nem üres válasz; nem bizonyíték a tartalom biztonságosságára |

Egy átlagos HTTP 400 **nem** bizonyíték szűrésre. Érvénytelen paraméterek, hitelesítési hibák, sebességkorlátok, szerverhibák, hibás válaszok, és rövidített kimenetek a futást sikertelenné teszik hamis biztonsági siker helyett. Általános kifejezések, mint a "káros tartalom" egy jóindulatú magyarázatban nem minősülnek elutasításnak.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Várható hat kategória eredmény és egy összegzés, amely leszögezi, hogy a megfigyelések nem biztonsági tanúsítványok. Minden próba 300-token kitöltési korlátot tartalmaz. Ellenőrizze kézzel a váratlan generációkat és lehetséges elutasításokat; a jóindulatú összehasonlításnak érdemi felelős AI magyarázatot kell adnia. Nem szükséges stdin.

## Gyakori Minták a Példákban

Az [AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) központosítja a végpont normalizálását, telepítési felülírásokat, kulcs nélküli hitelesítést és csevegési beállításokat:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

A token szolgáltató szükség szerint frissíti a hozzáférési tokeneket. Ne naplózza a tokeneket és ne cserélje le API kulcsra. Minden program újrahasználja kliensét és lezárja azt `finally`-ban vagy saját `AutoCloseable` burkolón keresztül; az SDK `OpenAIClient` önmagában nem `AutoCloseable`.

A [ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) befejezett, nem üres szöveges választ kér. Üres választási lehetőségek, elutasítások, szűrők és legrövidített válaszok nem jelennek meg sikerként csendben. A felelős AI példa kifejezetten kezeli a várt szűrő/lelkesülési eredményeket. A nem kezelt hibák nem nulla kilépési kóddal zárják a Java/Maven folyamatot.

**Az automatikus SDK újrapróbálkozások ki vannak kapcsolva**, hogy a kérés-számok kiszámíthatók legyenek megosztott alacsony RPM-es telepítéseken. Minden inferencia kérésnek 60 másodperces időkorlátja van. A token beszerzés további időt vehet igénybe. Az alkalmazásszintű ütemezésnek tiszteletben kell tartania a kvótákat; ne futtasson vakon ismételten egy sikertelen fizetős kérdést.

## Egységtesztek

A példakönyvtárból:

```powershell
mvn -B -ntp clean test
```

A teszt szállítás teljesen helyettesíti az SDK HTTP réteget, rögzíti a tényleges szerializált kéréstörzseket, és szolgáltatott sorban álló válaszokat. Nem nyit socketeket, nem szerez Azure tokeneket, és hibát jelez váratlan kéréseknél. Ezek a tesztek az alkalmazás viselkedését és az SDK protokollt validálják, nem az élő modell minőségét vagy a telepítés elérhetőségét.

| Teszt csomag | Lefedettség |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Végpont normalizálás/elutasítás, telepítési felülírások, érvelési és token opciók |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Minden kitöltési munkafolyamat, üzenettörténet, teljes forduló korrekció, EOF, hibák |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Eszközsémák, típusos argumentumok, aritmetika, azonosítók, több eszközeredmény, sikertelen utókövetések |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Fájlkeresés, UTF-8, méretkorlátok, talajfeltétel csomag, bemeneti és API hibák |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Mind a hat próba, explicit szűrők, elutasítás osztályozás, hétköznapi 400 és egyéb hibák |

Egy csomag futtatásához használja a `mvn -B -ntp test "-Dtest=FunctionsAppTest"` parancsot. Megosztott segédeszközök találhatók a [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java)-ben.

## Folyamatos Élő Ellenőrzés

Az élő hívások elkülönülnek az egységtesztektől. Használja az alábbi parancsokat **egyenként**, a repository gyökeréből, csak akkor, ha a hitelesítési adatok és a telepítési hozzáférés készen áll. Nincsenek szükséges szolgáltatások vagy állandó folyamatok.

Megosztott **10 kérés/perc** telepítéshez előre foglaljon elég kvótát az egész következő programhoz, mielőtt elindítja: 5, 4, 1, majd 6 kérés. A szekvenciális folyamatok önmagukban nem garantálják a sebességkorlát betartását. Egyeztessen a többi hívóval az egyperces ablakról; ne illessze be a négy hívást tempó nélküli csomagként.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Kitöltések, többfordulós és két interaktív forduló:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Ellenőrizze mindhárom szakaszcím, öt válasz, egy végső interaktív válasz, amely Ada említését tartalmazza, a `Goodbye!` kifejezést és a 0 kilépési kódot. Költségvetés: **5 kérés, legfeljebb 1900 befejezési token**. Egy kisebb futtatáshoz csak az `exit` parancsot használja csővezetékként: 3 kérés / 900 token, de így nem történik interaktív következtetés.

**2. Mindkét függvényhívó munkafolyamat:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Ellenőrizze mindkét függvénynév helyességét, a szimulált seattle-i időjárást, a kiszámított 36-os eredményt, a két végső választ és a 0-s kilépési kódot. Költségvetés: **4 kérés, legfeljebb 1200 befejezési token**.

**3. Dokumentum-alapú válasz:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Ellenőrizze a dokumentum elérési útját, egy Microsoft Entra ID-t megemlítő választ, és a 0 kilépési kódot. Költségvetés: **1 kérés, legfeljebb 500 befejezési token**. A meglévő [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) az egyetlen szükséges bemeneti fájl. Egy opcionális második futtatás, amely hiányzó témáról kérdez, tartózkodjon a válaszadástól, és ez plusz egy kérés / 500 token.

**4. Felelős mesterséges intelligencia megfigyelések:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Ellenőrizze a hat kategóriát és a megfigyelési összefoglalót, tekintse át a generált tartalmat, és követelje meg a 0 kilépési kódot a technikai befejezéshez. Egy sikeres folyamat kilépés nem tanúsítja a modell biztonságosságát. Költségvetés: **6 kérés, legfeljebb 1800 befejezési token**.

**Összesen a négy parancsra: 16 chat kérés és legfeljebb 5400 befejezési token**, plusz bemeneti tokenek (beleértve az ismételt beszélgetést és az eszköz sémát/történetet). Nincs beágyazási kérés. A tényleges tokenhasználat modelltől függ, és lehet alacsonyabb, különösen szűrt promptoknál. A dollárköltség a kiépítés árképzésétől függ; nem nyújt fix pénzbeli becslést. Minden kéréskorlát feltételezi, hogy nincs manuális újrafuttatás. Ellenőrizze a `$LASTEXITCODE` értékét az egyes parancsok után; ha nem nulla, a futás nem fejeződött be sikeresen.

## Hibakeresés

- **Hiányzó végpont / 401 / 403:** Állítsa be a végpontot a futtatási folyamatban, ellenőrizze helyi Azure bejelentkezését és az erőforráshoz kötött szerepkört, valamint a nem szándékolt identitás-környezet felülírásokat.
- **400 / 404:** Erősítse meg, hogy a telepítés létezik és támogatja a Chat Completion-t `none` érvelési erőfeszítéssel. Használjon HTTPS erőforrás gyökér vagy `/openai/v1` URL-t, ne legacy telepítési URL-t. A szokásos 400-as hibák technikai hibák, nem biztonsági blokkok.
- **429:** Egyeztessen a megosztott RPM és token kvótával, mielőtt újrapróbálkozna. A példák szándékosan nem próbálkoznak automatikusan újra.
- **`Incomplete chat response: length`:** A kimenet elérte a befejezési korlátot. Tekintse át a választ és a promptot mielőtt növelné a limitet és annak dokumentált költségvetését; ne rögzítsen sikeresként egy megnyesett futást.
- **Fájl vagy stdin hibák:** Indítsa támogatott könyvtárból vagy adjon meg egy explicit dokumentum útvonalat. Adjon meg nem üres olvasói kérdést. A befejezések normálisan zárulhatnak EOF-n vagy az `exit`-en.
- **Fordítási hibák:** Ellenőrizze a Java 21 vagy újabb verziót, majd futtassa a `mvn -B -ntp clean test` parancsot. PowerShellben idézze az egész Maven argumentumot, amely pontozott tulajdonságot tartalmaz, például `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Következő lépések

Folytassa a [4. fejezettel: Gyakorlati példák](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Jogi nyilatkozat**:
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével készült. Bár az pontosságra törekszünk, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az anyanyelvén tekintendő hiteles forrásnak. Fontos információk esetén professzionális emberi fordítást javasolunk. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely ebből a fordításból ered.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->