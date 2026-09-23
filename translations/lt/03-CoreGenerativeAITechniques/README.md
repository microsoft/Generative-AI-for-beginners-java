# Pagrindinis generatyvios AI mokymo vadovas

## Turinys

- [Išankstiniai reikalavimai](#išankstiniai-reikalavimai)
- [Pradžia](#pradžia)
- [Modelio pasirinkimo vadovas](#modelio-pasirinkimo-vadovas)
- [Pamoka 1: LLM užbaigimai ir pokalbis](#pamoka-1-llm-užbaigimai-ir-pokalbis)
- [Pamoka 2: Funkcijų kvietimas](#pamoka-2-funkcijų-kvietimas)
- [Pamoka 3: RAG (Retrieval-Augmented Generation)](#pamoka-3-rag-retrieval-augmented-generation)
- [Pamoka 4: Atsakingas AI](#pamoka-4-atsakingas-ai)
- [Bendri raštai pavyzdžiuose](#bendri-raštai-pavyzdžiuose)
- [Vienetiniai testai](#vienetiniai-testai)
- [Sekveninė tiesioginė patikra](#sekveninė-tiesioginė-patikra)
- [Gedimų šalinimas](#gedimų-šalinimas)
- [Tolimesni žingsniai](#sekantys-žingsniai)

## Apžvalga

Keturi atskiri Java programų pavyzdžiai demonstruoja pokalbius, pokalbio istoriją, funkcijų kvietimą, viso dokumento paiešką naudojant papildytą generavimą (RAG) ir atsakingo AI atsakymų tvarkymą. Visos pokalbių užklausos pagal numatytuosius parametrus skirtos **GPT-5.6 Luna su protavimo lygiu `none`**.

Šie pavyzdžiai naudoja oficialią OpenAI Java SDK su Azure OpenAI v1 galiniu tašku, vadovaujantis [Microsoft SDK gairėmis](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Senesnis `azure-ai-openai` paketas nebėra priklausomybė. Veiklos užbaigimai išsaugoti mokymui apie esamus žinučių pagrindu veikiančius darbo srautus; kitoms API parinktims žr. [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure).

## Išankstiniai reikalavimai

- Java 21 arba naujesnė versija ir Maven 3.6.3 arba naujesnis.
- Azure OpenAI pokalbių diegimas pavadinimu `gpt-5.6-luna` arba suderinamas pertvarkymas su Chat Completions nustatymais.
- Prisijungęs Azure tapatybės vartotojas su **Cognitive Services OpenAI User** vaidmeniu ištekliui. Vietiniam kūrimui naudojamas Azure CLI prisijungimas; talpinamos programos gali naudojasi valdomąja tapatybe.
- Žr. [2 skyrių](../02-SetupDevEnvironment/getting-started-azure-openai.md) išteklių nustatymui ir prisijungimui.

[Maven konfigūracija](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fiksuoja šias versijas, patikrinta 2026-09-14:

| Komponentas | Versija | Paskirtis |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Oficialus Azure v1 suderinamas klientas |
| `com.azure:azure-identity` | 1.18.6 | Autentifikacija be raktų ir žetonų atnaujinimas |
| `net.objecthunter:exp4j` | 0.4.8 | Aritmetinių išraiškų analizė be kodo vykdymo |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter vienetiniai testai |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 kompiliavimas, testai, paleidžiami pavyzdžiai |

Kompiliatorius naudoja `--release 21`. Šie atskiri pavyzdžiai nereikalauja Spring Boot, Spring AI ar LangChain4j priklausomybių.

## Pradžia

Iš šakninių katalogų nustatykite išteklių galinį tašką ir pasirenkamą diegimo pertvarkymą savo aplinkos komandoje.

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

Testams nereikia Azure kredencialų ar galinio taško. Maven automatiškai neskaito aplinkos failo; nustatykite kintamuosius toje pačioje aplinkoje, kurioje paleidžiami gyvieji pavyzdžiai. IDE paleidimams patikrinkite paleidimo konfigūracijos suteiktą aplinką.

## Modelio pasirinkimo vadovas

| Aplinkos kintamasis | Reikšmė | Numatytoji |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure išteklių šaknis arba jau normalizuotas `/openai/v1` URL | Būtina gyvai vykdant |
| `AZURE_OPENAI_DEPLOYMENT` | Pokalbių diegimo pavadinimas, ne modelio versija | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Atskirai naudojama įterpimų diegimo konfigūracija, nenaudojama šiuose keturiuose programose | `text-embedding-3-small` |

Tušti pertvarkymai naudoja numatytuosius nustatymus. Konfigūracija tiksliai prideda `/openai/v1` vieną kartą ir nepriima kredencialų, užklausos eilučių bei seni diegimo kelių gale.

Kiekviena pokalbio užklausa tiesiogiai nustato `reasoningEffort(ReasoningEffort.NONE)` ir `maxCompletionTokens(...)`. Nei viena užklausa nenustato `temperature`, `top_p` ar seno užbaigimų tokenų parametro. Tai taikoma ir įrankių pasirinkimui bei rezultatų peržiūrai. GPT-5.6 pokalbių užbaigimuose įrankiai reikalauja protavimo lygio `none`; žr. [Microsoft pokalbių gaires](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Šiame skyriuje nėra tiesioginės srautinės transliacijos ar įterpimų įėjimo taško.** Skaitytojas gauna visą dokumentą, o ne vektorius. Jei prailginate su įterpimais, naudokite atskirą diegimą, pavyzdžiui, `text-embedding-3-small`, bet ne Luna.

## Pamoka 1: LLM užbaigimai ir pokalbis

Šaltinis: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programa paleidžia paprastą Java srautų paaiškinimą, dviejų apsikeitimų HashMap/TreeMap pokalbį ir interaktyvų pokalbį. Antras apsikeitimas apima pirmą pagalbininko atsakymą; kiekvienas interaktyvus apsikeitimas taip pat siunčia ankstesnį pokalbį.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` suteikia diegimo ir aiškaus protavimo parametrus. Interaktyvus pokalbis praleidžia tuščias eilutes, baigiasi komandą `exit` arba EOF, ir palaiko sistemos žinutę bei devynis užbaigtus vartotojo/pagalbininko apsikeitimus. Apsikeitimų skaičiaus ribojimas yra mokslinis apribojimas, o ne tikslus tokenų biudžeto garantas.

Iš examples katalogo:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Laukite trijų pradinių atsakymų, tada `You:` užklausos. Kiekviena ne tuščia interaktyvi užklausa prideda vieną užklausą. Užbaigimo ribos yra 200, 300, 400, tada 500 tokenų kiekvienam interaktyviam apsikeitimui.

## Pamoka 2: Funkcijų kvietimas

Šaltinis: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK iš anotuotų `WeatherArguments` ir `CalculationArguments` įrašų sukuria JSON schemas. Privalomas įrankio pasirinkimas leidžia kiekvienam pavyzdžiui naudoti įrankių protokolą, o ne priimti nepadedamą modelio atsakymą.

1. Siųsti klausimą su leidžiamu įrankiu, protavimo lygiu `none` ir 300 tokenų užbaigimo riba.
2. Reikalauti `tool_calls` užbaigimo priežasties, patikrinti funkcijos pavadinimą ir kvietimo ID, bei išanalizuoti tipizuotus JSON argumentus.
3. Vykdyti vietinę funkciją. Modelis nevykdo Java ar jokio kodo.
4. Pridėti pagalbininko įrankio kvietimo žinutę vieną kartą, o po to visus rezultatus su atitinkamu `tool_call_id`.
5. Siųsti galutinę 300 tokenų užklausą be įrankių ir reikalauti užbaigto, ne tuščio atsakymo.

`get_weather` grąžina **simuliuotą**, o ne realią, orų informaciją. Jis gerbia miestą ir konvertuoja pavyzdinę 22 laipsnių Celsijaus temperatūrą į Farenheitus, jei prašoma. `calculate` įvertina pateiktą išraišką per exp4j, palaiko formas, pvz., `15% iš 240` ir `2 + 3 * 4`, bei atmeta tuščias, per dideles, neteisingas ar netiesines skaičiavimus. Naudoja slankiojo kablelio aritmetiką, o ne finansinį dešimtainį tikslumą.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Laukite `Function: get_weather`, simuliuotus Sietlo orus, `Function: calculate`, `Function result: 36` ir du galutinius atsakymus. Nekviečia stdin ar išorinių orų kredencialų. Sėkmingai vykdant naudojamos tik keturios pokalbių užklausos.

## Pamoka 3: RAG (Retrieval-Augmented Generation)

Šaltinis: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Įvestis: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Šis įvedamasis RAG pavyzdys gauna vieną visą UTF-8 dokumentą ir įtraukia jį į vartotojo žinutę su klausimu. Atskirta sistemos žinutė nurodo modeliui laikyti dokumentą nepatikimu šaltiniu ir atsakyti tik remiantis tos informacijos kontekstu. Jei dokumente nerandama atsakymo, atsakymas yra: `Negaliu rasti šios informacijos pateiktame dokumente.`

Grįžtamasis ryšys gali sumažinti haliucinacijas, tačiau nei ribojančios žymos, nei sistemos instrukcijos negarantuoja tikslumo ar neapsaugo nuo kiekvieno užklausos injekcijos. Peržiūrėkite gyvus atsakymus. Produkcijoje RAG dažniausiai prideda dalijimą, paiešką, citatas, prieigos kontrolę ir vertinimą.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Įveskite vieną klausimą, pavyzdžiui, `Kokį autentifikavimo metodą aprašo dokumentas?`. Laukite atsakymo, pamininčio Microsoft Entra ID. Programa baigs darbą po vienos pokalbių užklausos su 500 tokenų užbaigimo limitu.

Numatytoji failų paieška veikia iš šakninių, skyriaus ar examples katalogų. Taip pat palaikomas konkretus kelias:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Įvestys turi būti ne tuščios: ne daugiau kaip 32 KiB UTF-8 dokumento duomenų ir 2000 klausimo simbolių. Trūkstami failai, tušti / EOF klausimai ir per didelės įvestys neveikia prieš darant vertinimą.

## Pamoka 4: Atsakingas AI

Šaltinis: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Šeši bandymai apima žalingas instrukcijas, neapykantos kalbą, privatumo problemas, medicininę dezinformaciją, neteisėtą turinį ir benigną atsakingo AI klausimą. Programa stebi atsakymą, o ne remiasi kiekvienu bandymu aktyvuoti filtrą.

| Rezultatas | Įrodymai |
| --- | --- |
| `FILTERED` | Aiškus `content_filter` / `ResponsibleAIPolicyViolation` klaidos kodas arba užbaigimo `content_filter` priežastis |
| `REFUSED` | Ne tuščias struktūrizuotas `message.refusal` laukas |
| `POSSIBLE_REFUSAL` | Pradinis atsisakymo frazės paminėjimas paprastame tekste; heuristika reikalaujanti peržiūros |
| `GENERATED` | Užbaigtas ne tuščias atsakymas; nereiškia, kad turinys saugus |

Įprastas HTTP 400 nėra filtro įrodymas. Netinkami parametrai, autentifikacijos klaidos, srauto ribojimai, serverio klaidos, netaisyklingi atsakymai ir sutrumpintas išvestis skatina veikimo klaidą, o ne klaidingą saugos sėkmę. Plati išraiška „žalingas turinys“ benigname paaiškinime nelaikoma atsisakymu.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Laukite šešių kategorijų rezultatus ir santrauką, kurioje nurodoma, kad pastebėjimai nėra saugos sertifikatas. Kiekvienam bandymui skirta 300 tokenų užbaigimo riba. Rankiniu būdu peržiūrėkite netikėtus generavimus ir galimus atsisakymus; benignas pavyzdys turėtų pateikti pagrįstą atsakingo AI paaiškinimą. Stdin nereikia.

## Bendri raštai pavyzdžiuose

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizuoja galinių taškų normalizavimą, pertvarkymus, autentifikaciją be raktų ir pokalbių parinktis:

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

Tokenų teikėjas atnaujina prieigos žetonus pagal poreikį. Neloginkite žetonų ir nepakeiskite jų API raktu. Kiekviena programa naudoja savo klientą ir uždaro jį per `finally` arba per savo `AutoCloseable` įvyniojimą; SDK `OpenAIClient` nėra `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) reikalauja užbaigto, ne tuščio tekstinio atsakymo. Tušti pasirinkimai, atsisakymai, filtrai ir sutrumpinti atsakymai nėra tyliai interpretuojami kaip sėkmė. Atsakingo AI pavyzdyje aiškiai tvarkomi filtravimų/atsisakymų rezultatai. Netvarkomi klaidų atvejai grąžina Java/Maven procesui nenulinį išeities kodą.

**Automatiniai SDK pakartotiniai bandymai yra išjungti**, kad užklausų skaičius būtų prognozuojamas bendrinamuose žemo RPM diegimuose. Kiekviena inferencijos užklausa turi 60 sekundžių laikmatį. Tokenų gavimas gali užtrukti papildomai. Programos lygmens paskirstymas turi gerbti kvotas; nekartokite prarastos mokamos užklausos be priežasties.

## Vienetiniai testai

Iš examples katalogo:

```powershell
mvn -B -ntp clean test
```

Testo transportas visiškai pakeičia SDK HTTP sluoksnį, fiksuoja faktinius serijinius užklausų turinius ir tiekia eilės atsakymus. Neatidaro lizdų, negauti Azure žetonų ir nesėkmingai baigia netikėtas užklausas. Šie testai tikrina programos elgseną ir SDK protokolą, ne tiesioginę modelio kokybę ar diegimo prieinamumą.

| Testų paketas | Apimtis |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Galinio taško normalizavimas/atmetimas, pertvarkymai, protavimo ir tokenų parinktys |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Kiekvienas užbaigimo darbo srautas, žinučių istorija, apsikeitimų ribojimas, EOF, klaidos |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Įrankių schemos, tipizuoti argumentai, aritmetika, ID, keli įrankių rezultatai, nesėkmingi tęsiniai |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Failų paieška, UTF-8, dydžio ribos, grįžtamoji apkrova, įvesties ir API klaidos |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Visi šeši bandymai, aiškūs filtrai, atsisakymo klasifikacija, įprastas 400 ir kitos klaidos |

Vienam rinkiniui naudokite `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Bendri ištekliai saugomi [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekveninė tiesioginė patikra

Gyvi kvietimai yra atskirti nuo vienetinių testų. Naudokite žemiau pateiktas komandas **atskirai**, iš saugyklos šaknies, tik kai kredencialai ir diegimo prieiga paruošti. Nereikia jokių paslaugų ar nuolatinių procesų.

Bendrinamam **10 užklausų/minutę** diegimui rezervuokite pakankamai kvotų visai kitai programai prieš ją paleisdami: 5, 4, 1, tada 6 užklausos. Sekveniniai procesai savaime negarantuoja srauto ribų laikymosi. Koordinuokite per valandą visų kitų vartotojų kvietimus; nekopijuokite keturių kvietimų kaip nemoduliuotos partijos.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Užbaigimai, keli apsikeitimai ir du interaktyvūs apsikeitimai:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Patikrinkite visus tris skirsnių pavadinimus, penkis atsakymus, galutinį interaktyvų atsakymą, kuriame prisimenama Ada, „Viso gero!“, ir išėjimo kodą 0. Biudžetas: **5 užklausos, ne daugiau kaip 1 900 baigimo žetonų**. Mažesniam paleidimui, įveskite tik `exit`: 3 užklausos / 900 žetonų, tačiau tai neaktyvuoja interaktyvios inferencijos.

**2. Abi funkcijų kvietimo darbotvarkės:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Patikrinkite abiejų funkcijų pavadinimus, simuliuotą Sietlo orą, apskaičiuotą rezultatą 36, du galutinius atsakymus ir išėjimo kodą 0. Biudžetas: **4 užklausos, ne daugiau kaip 1 200 baigimo žetonų**.

**3. Atsakymas, pagrįstas dokumentu:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Patikrinkite dokumento kelią, atsakymą, kuriame minimas Microsoft Entra ID, ir išėjimo kodą 0. Biudžetas: **1 užklausa, ne daugiau kaip 500 baigimo žetonų**. Esamas [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) yra vienintelis būtinasis įvesties failas. Pasirinktinio antro paleidimo metu, kai užduodamas klausimas apie neegzistuojančią temą, reikia susilaikyti ir pridėti vieną užklausą / 500 žetonų.

**4. Atsakingo DI pastebėjimai:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Patikrinkite šešias kategorijas ir stebėjimų santrauką, peržiūrėkite sugeneruotą turinį ir reikalaukite išėjimo kodo 0 techniniam baigimui. Sėkmingas proceso išėjimas negarantuoja modelio saugumo. Biudžetas: **6 užklausos, ne daugiau kaip 1 800 baigimo žetonų**.

**Iš viso keturiems komandų rinkinams: 16 pokalbių užklausų ir ne daugiau kaip 5 400 baigimo žetonų**, plius įvesties žetonai (įskaitant pasikartojančią pokalbių istoriją ir įrankių schemą/istoriją). Nėra jokių embedded užklausų. Faktinis žetonų naudojimas priklauso nuo modelio ir gali būti mažesnis, ypač filtruotų užklausų atveju. Dolerinė kaina priklauso nuo diegimo kainodaros; nėra fiksuoto piniginio įvertinimo. Visos užklausų ribos numato, kad nėra rankinių pakartojimų. Nedelsdami po kiekvienos komandos patikrinkite `$LASTEXITCODE`; ne nulis reiškia, kad paleidimas nesėkmingas.

## Gedimų šalinimas

- **Nerastas galinis taškas / 401 / 403:** Nustatykite galinį tašką paleidimo procese, patikrinkite vietinį Azure prisijungimą ir ištekliams priklausančią rolę, taip pat patikrinkite netyčinius identiteto aplinkos perrašymus.
- **400 / 404:** Įsitikinkite, kad diegimas egzistuoja ir palaiko pokalbių užbaigimus su loginio mąstymo pastangomis `none`. Naudokite HTTPS išteklių šaknį arba `/openai/v1` URL, o ne seną diegimo URL. Paprasti 400 klaidų pranešimai yra techniniai gedimai, o ne saugumo blokai.
- **429:** Suderinkite bendrą RPM ir žetonų kvotą prieš bandydami iš naujo. Pavyzdžiai tyčia neturi automatinio pakartojimo.
- **`Nepilnas pokalbio atsakymas: ilgis`:** Išvestis pasiekė baigimo ribą. Peržiūrėkite atsakymą ir užklausą prieš didindami ribą bei jos dokumentuotą biudžetą; neskelbkite sutrumpinto paleidimo kaip sėkmingo.
- **Failų ar stdin klaidos:** Paleiskite iš palaikomos direktorijos arba nurodykite aiškų dokumento kelią. Pateikite ne tuščią skaitytojo klausimą. Užbaigimai gali baigtis normaliai EOF arba `exit`.
- **Kompiliavimo klaidos:** Patikrinkite, ar Java 21 ar naujesnė versija, tada paleiskite `mvn -B -ntp clean test`. PowerShell aplinkoje įtraukite visą Maven argumentą su tašku į kabutes, pvz., `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Sekantys žingsniai

Tęskite [4 skyrių: Praktiniai pavyzdžiai](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Atsakomybės apribojimas**:
Šis dokumentas buvo išverstas naudojant dirbtinio intelekto vertimo paslaugą [Co-op Translator](https://github.com/Azure/co-op-translator). Nors siekiame tikslumo, prašome atkreipti dėmesį, kad automatiniai vertimai gali turėti klaidų ar netikslumų. Originalus dokumentas jo gimtąja kalba laikomas autoritetingu šaltiniu. Svarbiai informacijai rekomenduojama naudoti profesionalų žmogiškąjį vertimą. Mes neatsakome už jokius nesusipratimus ar neteisingą interpretaciją, kilusią naudojantis šiuo vertimu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->