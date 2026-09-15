# Výukový kurz základních technik generativní AI

## Obsah

- [Požadavky](#požadavky)
- [Začínáme](#začínáme)
- [Průvodce výběrem modelu](#průvodce-výběrem-modelu)
- [Tutoriál 1: Dokončování LLM a chat](#tutoriál-1-dokončování-llm-a-chat)
- [Tutoriál 2: Volání funkcí](#tutoriál-2-volání-funkcí)
- [Tutoriál 3: RAG (Generování s doplněním vyhledáváním)](#tutoriál-3-rag-generování-s-doplněním-vyhledáváním)
- [Tutoriál 4: Odpovědná AI](#tutoriál-4-odpovědná-ai)
- [Běžné vzory napříč příklady](#běžné-vzory-napříč-příklady)
- [Jednotkové testy](#jednotkové-testy)
- [Sekvenční živá verifikace](#sekvenční-živá-verifikace)
- [Řešení problémů](#řešení-problémů)
- [Další kroky](#další-kroky)

## Přehled

Čtyři samostatné Java programy demonstrují chat, historii konverzace, volání funkcí, generování s doplněním vyhledáváním celých dokumentů (RAG) a zpracování odpovědí podle principů odpovědné AI. Všechny chatovací požadavky ve výchozím nastavení cílí na **GPT-5.6 Luna s nulovým úsilím v odůvodnění (`none`)**.

Tyto příklady používají oficiální Java SDK OpenAI s Azure OpenAI v1 endpointem podle [návodu Microsoftu](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Starší balíček `azure-ai-openai` již není závislostí. Chat Completions je zachováno pro výuku existujících workflow založených na zprávách; jiné možnosti API jsou popsány v [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure).

## Požadavky

- Java 21 nebo novější a Maven 3.6.3 nebo novější.
- Azure OpenAI chat deployment s názvem `gpt-5.6-luna` nebo přepsání s kompatibilním nastavením Chat Completions.
- Přihlášená Azure identita s rolí **Cognitive Services OpenAI User** na zdroji. Lokální vývoj využívá přihlášení přes Azure CLI; hostované aplikace mohou používat spravovanou identitu.
- Viz [Kapitola 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) pro nastavení zdroje a přihlášení.

[Maven konfigurace](../../../03-CoreGenerativeAITechniques/examples/pom.xml) specifikuje tyto verze, kontrolováno k 2026-09-14:

| Komponenta | Verze | Účel |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Oficiální klient kompatibilní s Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Autentizace bez klíče a obnova tokenu |
| `net.objecthunter:exp4j` | 0.4.8 | Parsování aritmetických výrazů bez vyhodnocení kódu |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline jednotkové testy Jupiter |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Kompilace Java 21, testy, spustitelné příklady |

Kompilátor používá `--release 21`. Tyto samostatné příklady nevyžadují Spring Boot, Spring AI ani závislost LangChain4j.

## Začínáme

Ze základního adresáře repozitáře nastavte endpoint zdroje a případné přepsání deploymentu v shellu.

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

Testy nevyžadují Azure přihlašovací údaje ani endpoint. Maven automaticky nečte soubory prostředí; proměnné nastavte v shellu, kterým spouštíte živé příklady. Pro spuštění v IDE ověřte prostředí nastavené v konfiguraci spuštění.

## Průvodce výběrem modelu

| Proměnná prostředí | Význam | Výchozí hodnota |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS root Azure zdroje nebo již normalizovaná URL `/openai/v1` | Povinné pro živé spuštění |
| `AZURE_OPENAI_DEPLOYMENT` | Název chat deploymentu, ne verze modelu | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Konfigurace samostatného embedding deploymentu, nepoužívaná v těchto čtyřech programech | `text-embedding-3-small` |

Prázdná přepsání deploymentu použijí výchozí hodnoty. Konfigurace připojuje `/openai/v1` přesně jednou a odmítá přihlašovací údaje, query stringy a staré deployment cesty v endpointu.

Každý chatovací požadavek nastavuje explicitně `reasoningEffort(ReasoningEffort.NONE)` a `maxCompletionTokens(...)`. Žádný požadavek nenastavuje `temperature`, `top_p` ani starší možnost dokončovacích tokenů. To zahrnuje i výběr nástrojů a následné výsledky nástrojů. Nástroje GPT-5.6 Chat Completions vyžadují úsilí v odůvodnění `none`; viz [Microsoftův chat průvodce](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**V této kapitole nejsou vstupy pro streamování ani embedding.** Čtenář získává celý dokument, nikoliv vektory. Pokud to rozšíříte o embeddingy, použijte samostatný embedding deployment, například `text-embedding-3-small`, nikdy Luna.

## Tutoriál 1: Dokončování LLM a chat

Zdroj: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program provádí jednoduché vysvětlení Java streamů, dvoutahovou konverzaci HashMap/TreeMap a interaktivní chat. Druhý tah zahrnuje první asistentovu odpověď; každý interaktivní tah rovněž posílá předchozí konverzaci.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` poskytuje deployment a explicitní nastavení odůvodnění. Interaktivní chat přeskočí prázdné řádky, končí na `exit` nebo EOF a uchovává systémovou zprávu plus devět dokončených tahů uživatele/asistenta. Ořezávání počtu tahů je vzdělávací limit, nikoliv přesná záruka tokenového rozpočtu.

Z adresáře examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Očekávejte tři úvodní odpovědi, pak výzvu `You:`. Každá neprázdná interaktivní otázka přidá jeden požadavek. Limity dokončení jsou 200, 300, 400, pak 500 tokenů na interaktivní tah.

## Tutoriál 2: Volání funkcí

Zdroj: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK odvozuje JSON schémata z anotovaných záznamů `WeatherArguments` a `CalculationArguments`. Povinný výběr nástroje způsobí, že každý příklad využije protokol nástroje místo přijetí nepodpořené odpovědi modelu.

1. Odešlete otázku s povoleným nástrojem, úsilím v odůvodnění `none` a limitem dokončení 300 tokenů.
2. Požadujte důvod dokončení `tool_calls`, ověřte název funkce a ID volání, a analyzujte typované JSON argumenty.
3. Proveďte lokální funkci. Model neprovádí Java ani libovolný kód.
4. Přidejte asistentovu zprávu volání nástroje jednou, následovanou každým výsledkem se shodujícím `tool_call_id`.
5. Odešlete jeden finální 300-tokenový požadavek bez nástrojů a požadujte dokončenou, ne prázdnou odpověď.

`get_weather` vrací **simulované**, nikoliv živé počasí. Respektuje město a převede vzorových 22 stupňů Celsia na Fahrenheit, pokud je požadováno. `calculate` vyhodnocuje zadaný výraz přes exp4j, podporuje formy jako `15% of 240` a `2 + 3 * 4` a odmítá prázdné, nadměrné, neplatné či nevýpočtové výrazy. Používá plovoucí desetinnou čárku, nikoliv finanční přesnost.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Očekávejte `Function: get_weather`, simulované počasí v Seattle, `Function: calculate`, `Function result: 36` a dvě závěrečné odpovědi. Nejsou vyžadovány vstupy ze stdin ani externí pověření počasí. Úspěšné spuštění použije přesně čtyři chatovací požadavky.

## Tutoriál 3: RAG (Generování s doplněním vyhledáváním)

Zdroj: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Vstup: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Tento úvodní příklad RAG načte jeden celý UTF-8 dokument a zařadí jej do uživatelské zprávy spolu s otázkou. Samostatná systémová zpráva instruuje model, aby s obsahem dokumentu zacházel jako s nedůvěryhodnými daty a odpovídal pouze na základě tohoto kontextu. Pokud dokument neobsahuje odpověď, požadovaná odpověď je: `V tomto poskytnutém dokumentu nelze tuto informaci nalézt.`

Ukotvení (grounding) může snížit halucinace, ale ani oddělovače, ani systémové instrukce nezaručí přesnost nebo neodstraní všechny možnosti injektáže promptu. Přezkoumejte živé odpovědi. Produkční RAG obvykle přidává dělení na části, vyhledávání, citace, kontrolu přístupu a vyhodnocování.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Zadejte jednu otázku, například `Kterou metodu autentizace dokument popisuje?`. Očekávejte odpověď zmiňující Microsoft Entra ID. Program skončí po jednom chatovacím požadavku s limitem 500 tokenů.

Výchozí vyhledávání souborů funguje z kořenového adresáře repozitáře, adresáře kapitoly nebo adresáře příkladů. Podporována je i explicitní cesta:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Vstupy musí být neprázdné: maximálně 32 KiB UTF-8 dat dokumentu a 2 000 znaků otázky. Chybějící soubory, prázdné/EOF otázky a nadměrné vstupy selhávají před inferencí.

## Tutoriál 4: Odpovědná AI

Zdroj: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Šest testů pokrývá škodlivé instrukce, nenávistnou řeč, soukromí, dezinformace v lékařství, nelegální obsah a benigní otázku vztahující se k odpovědné AI. Program sleduje odpověď, nevyžaduje, aby každý test vyvolal filtr.

| Výsledek | Důkaz |
| --- | --- |
| `FILTERED` | Explicitní kód chyby `content_filter` / `ResponsibleAIPolicyViolation` nebo důvod dokončení `content_filter` |
| `REFUSED` | Neprázdné strukturované pole `message.refusal` |
| `POSSIBLE_REFUSAL` | Úvodní fráze odmítnutí v běžném textu; heuristika vyžadující kontrolu |
| `GENERATED` | Dokončená nenulová odpověď; není důkaz, že obsah je bezpečný |

Běžný HTTP 400 není důkazem filtrování. Neplatné parametry, chyby autentizace, limity rychlosti, chyby serveru, chybné odpovědi a oříznuté výstupy selhání běh místo falešného bezpečnostního úspěchu. Obecná slova jako „škodlivý obsah“ v benigním vysvětlení neznamenají odmítnutí.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Očekávejte výsledky ve šesti kategoriích a shrnutí, že pozorování nejsou bezpečnostní certifikací. Každý test má limit dokončení 300 tokenů. Neočekávané generace a možné odmítnutí zkontrolujte ručně; benigní srovnání by mělo poskytnout podstatné vysvětlení odpovědné AI. Vstup ze stdin není vyžadován.

## Běžné vzory napříč příklady

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizuje normalizaci endpointu, přepsání deploymentů, autentizaci bez klíče a chatovací možnosti:

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

Dodavatel tokenů obnovuje přístupové tokeny podle potřeby. Nezapisujte tokeny do logu ani je nenahrazujte API klíčem. Každý program znovu používá svého klienta a zavírá jej v `finally` nebo přes vlastní obal `AutoCloseable`; SDK `OpenAIClient` sám není `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) vyžaduje dokončenou, nenulovou textovou odpověď. Prázdné volby, odmítnutí, filtry a oříznuté odpovědi se nezobrazují tiše jako úspěch. Příklad odpovědné AI zpracovává očekávané výsledky filtru/odmítnutí explicitně. Nezpracované chyby dávají Java/Maven procesu nenulový návratový kód.

**Automatické opakování SDK je vypnuto** pro předvídatelnost počtu požadavků na sdílených nasazeních s nízkým počtem požadavků za minutu. Každý požadavek inference má timeout 60 sekund. Získání tokenů může trvat déle. Plánování na úrovni aplikace musí respektovat kvóty; neopakujte neuváženě neúspěšný placený požadavek.

## Jednotkové testy

Z adresáře examples:

```powershell
mvn -B -ntp clean test
```

Testovací transport kompletně nahrazuje HTTP vrstvu SDK, zachycuje skutečná serializovaná těla požadavků a dodává frontované odpovědi. Neotevírá žádné sockety, nezískává Azure tokeny a selhává na neočekávané požadavky. Tyto testy validují chování aplikace a SDK protokolu, ne kvalitu živých modelů nebo dostupnost deploymentu.

| Testovací sada | Pokrytí |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizace/odmítnutí endpointu, přepsání deploymentů, možnosti odůvodnění a tokenů |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Každý workflow dokončení, historie zpráv, ořezání kompletních tahů, EOF, chyby |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schémata nástrojů, typované argumenty, aritmetika, ID, více výsledků nástroje, neúspěšné následky |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Vyhledání souboru, UTF-8, limity velikosti, grounding payload, vstupní a API chyby |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Všechny šest testů, explicitní filtry, klasifikace odmítnutí, běžné 400 a jiné chyby |

Pro jednu sadu použijte `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Sdílené fixture jsou v [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekvenční živá verifikace

Živá volání jsou oddělena od jednotkových testů. Použijte následující příkazy **jednotlivě**, ze základního adresáře repozitáře, až poté, co jsou připraveny přihlašovací údaje a přístup k deploymentu. Nepotřebujete žádné služby ani trvalé procesy.

Pro sdílený deployment s **10 požadavky za minutu** rezervujte dostatečnou kvótu pro celý následující program před jeho spuštěním: 5, 4, 1, pak 6 požadavků. Sekvenční procesy samy o sobě nezaručují dodržení limitu rychlosti. Koordinujte s ostatními volajícími; nevkládejte čtyři volání najednou bez časového odstupu.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Dokončování, více tahů a dva interaktivní tahy:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Zkontrolujte všechny tři oddíly nadpisů, pět odpovědí, závěrečnou interaktivní odpověď připomínající Adu, `Sbohem!` a výstupní kód 0. Rozpočet: **5 požadavků, maximálně 1 900 dokončovacích tokenů**. Pro menší spuštění přesměrujte pouze `exit`: 3 požadavky / 900 tokenů, ale to nevyužije interaktivní inferenci.

**2. Oba pracovní postupy volání funkcí:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Zkontrolujte oba názvy funkcí, simulované počasí v Seattlu, vypočítaný výsledek 36, dvě závěrečné odpovědi a výstupní kód 0. Rozpočet: **4 požadavky, maximálně 1 200 dokončovacích tokenů**.

**3. Odpověď založená na dokumentu:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Zkontrolujte cestu k dokumentu, odpověď zmiňující Microsoft Entra ID a výstupní kód 0. Rozpočet: **1 požadavek, maximálně 500 dokončovacích tokenů**. Existující [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) je jediný požadovaný vstupní soubor. Nepovinné druhé spuštění dotazující se na chybějící téma by mělo zůstat bez odpovědi a přidává jeden požadavek / 500 tokenů.

**4. Pozorování zodpovědné AI:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Zkontrolujte šest kategorií a shrnutí pozorování, zkontrolujte vytvořený obsah a vyžadujte výstupní kód 0 pro technické dokončení. Úspěšné ukončení procesu neznamená certifikaci bezpečnosti modelu. Rozpočet: **6 požadavků, maximálně 1 800 dokončovacích tokenů**.

**Celkem pro čtyři příkazy: 16 chatovacích požadavků a maximálně 5 400 dokončovacích tokenů**, plus vstupní tokeny (včetně opakované konverzace a schématu/následné historie nástroje). Požadavků na embedding není vůbec. Skutečné využití tokenů závisí na modelu a může být nižší, zejména pro filtrováné promptové vstupy. Cena v dolarech závisí na nasazení; není zde uváděn žádný pevný finanční odhad. Všechny limity požadavků předpokládají žádná manuální opakování. Ihned po každém příkazu zkontrolujte `$LASTEXITCODE`; nenulová hodnota znamená, že běh nebyl úspěšně dokončen.

## Řešení problémů

- **Chybějící koncový bod / 401 / 403:** Nastavte koncový bod ve spouštěcím procesu, ověřte běžné přihlášení k Azure a roli s omezením na zdroj a zkontrolujte, zda nejsou nechtěné přepsání identity v prostředí.
- **400 / 404:** Potvrďte, že nasazení existuje a podporuje Chat Completions s režimem usilování o zdůvodnění `none`. Používejte HTTPS zdrojový kořen nebo URL `/openai/v1`, nikoli staré URL nasazení. Běžné chyby 400 jsou technické závady, ne bezpečnostní bloky.
- **429:** Koordinujte sdílené RPM a kvótu tokenů před dalším pokusem. Příklady záměrně neprovádějí automatické opakování.
- **`Incomplete chat response: length`:** Výstup dosáhl limit dokončení. Prohlédněte si odpověď a prompt před zvýšením limitu a jeho dokumentovaného rozpočtu; neoznačujte zkrácený běh jako úspěšný.
- **Chyby souboru nebo stdin:** Spouštějte ze podporovaného adresáře nebo předávejte explicitní cestu k dokumentu. Poskytněte otázku pro čtecí vstup, která není prázdná. Dokončení může normálně končit na EOF nebo na `exit`.
- **Chyby kompilace:** Ověřte Java 21 nebo novější, poté spusťte `mvn -B -ntp clean test`. V PowerShellu uveďte celý argument Maven obsahující vlastnost s tečkou do uvozovek, například `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Další kroky

Pokračujte na [kapitolu 4: Praktické ukázky](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Prohlášení o omezení odpovědnosti**:
Tento dokument byl přeložen pomocí AI překladatelské služby [Co-op Translator](https://github.com/Azure/co-op-translator). Přestože usilujeme o co největší přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Originální dokument v jeho mateřském jazyce by měl být považován za autoritativní zdroj. Pro kritické informace se doporučuje profesionální lidský překlad. Nejsme odpovědní za jakékoli nedorozumění nebo nesprávné interpretace vzniklé použitím tohoto překladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->