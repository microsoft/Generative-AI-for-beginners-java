# Mafunzo ya Mbinu Muhimu za AI ya Kizazi

## Jedwali la Maudhui

- [Mahitaji ya Awali](#mahitaji-ya-awali)
- [Kuanzisha](#kuanzia)
- [Mwongozo wa Uchaguzi wa Mfano](#mwongozo-wa-uchaguzi-wa-mfano)
- [Mafunzo 1: Kukamilisha LLM na Mazungumzo](#mafunzo-1-kukamilisha-llm-na-mazungumzo)
- [Mafunzo 2: Kupiga Simu ya Kazi](#mafunzo-2-kupiga-simu-ya-kazi)
- [Mafunzo 3: RAG (Uzalishaji Ulioboreshwa kwa Kupata Taarifa)](#mafunzo-3-rag-uzalishaji-ulioboreshwa-kwa-kupata-taarifa)
- [Mafunzo 4: AI Inayowajibika](#mafunzo-4-ai-inayowajibika)
- [Mifumo Miori Katika Mifano](#mifumo-miori-katika-mifano)
- [Majaribio ya Kitengo](#majaribio-ya-kitengo)
- [Uhakiki wa Moja kwa Moja Mfululizo](#uhakiki-wa-moja-kwa-moja-mfululizo)
- [Kutatua Matatizo](#utatuzi-wa-matatizo)
- [Hatua Zifuatazo](#hatua-zifuatazo)

## Muhtasari

Programu nne za kujitegemea za Java zinaonyesha mazungumzo, historia ya mazungumzo, kupiga simu za kazi, uzalishaji ulioboreshwa kwa kupata hati nzima (RAG), na kushughulikia majibu ya AI inayowajibika. Matumizi yote ya mazungumzo yanamalizika kwa **GPT-5.6 Luna kwa juhudi za kufikiri `none`** kwa chaguo-msingi.

Mifano hii inatumia SDK rasmi ya OpenAI Java na kituo cha Azure OpenAI cha v1, ikifuata [miongozo ya SDK ya Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Kifurushi kilichotumika awali `azure-ai-openai` hakitumiki tena. Chat Completions inahifadhiwa kufundisha taratibu za kazi za ujumbe; ona [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) kwa chaguzi zingine za API.

## Mahitaji ya Awali

- Java 21 au zaidi na Maven 3.6.3 au zaidi.
- Utekelezaji wa mazungumzo wa Azure OpenAI uitwao `gpt-5.6-luna`, au mbadala wenye mipangilio inayolingana ya Chat Completions.
- Utambulisho uliojisajili kwenye Azure na jukumu la **Mtumiaji wa Cognitive Services OpenAI** kwenye rasilimali. Maendeleo ya ndani hutumia kuingia kwa Azure CLI yako; programu zilizo hifadhiwa zinaweza kutumia utambulisho uliodhibitiwa.
- Ona [Sura ya 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) kwa maagizo ya usanidi wa rasilimali na kuingia.

[Mpangilio wa Maven](../../../03-CoreGenerativeAITechniques/examples/pom.xml) umetia muhuri matoleo haya, yalikaguliwa tarehe 2026-09-14:

| Sehemu | Toleo | Kusudi |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Mteja rasmi wa Azure anayolingana na v1 |
| `com.azure:azure-identity` | 1.18.6 | Uthibitishaji usiotumia funguo na upya tokeni |
| `net.objecthunter:exp4j` | 0.4.8 | Kuchambua misemo ya hisabati bila kutekeleza msimbo |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Majaribio ya kitengo ya offline ya Jupiter |
| Kompua za Maven / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Ukusanyaji wa Java 21, majaribio, mifano inayoweza kutekelezwa |

Kompua hutumia `--release 21`. Hakuna utegemezi wa Spring Boot, Spring AI, au LangChain4j unahitajika kwa mifano hii ya kujitegemea.

## Kuanzia

Kuanzia mizizi ya hazina, weka kituo cha rasilimali na mbadala wa utekelezaji unapohitajika kwenye shell yako.

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

Majaribio haya hayahitaji sifa za Azure wala kituo. Maven husoma faili ya mazingira sio moja kwa moja; weka misemo kwenye shell inayotumika kuanzisha mifano ya moja kwa moja. Kwa uzinduzi wa IDE, hakikisha mazingira yanayopatikana kwenye usanidi wako wa uzinduzi.

## Mwongozo wa Uchaguzi wa Mfano

| Kibadilishaji cha Mazingira | Maana | Kawaida |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Mzizi wa rasilimali ya Azure kupitia HTTPS au URL iliyosawazishwa tayari ya `/openai/v1` | Inahitajika kwa utekelezaji wa moja kwa moja |
| `AZURE_OPENAI_DEPLOYMENT` | Jina la utekelezaji wa mazungumzo, si toleo la mfano | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Mpangilio wa utekelezaji wa kuingiza, haujatumika na programu hizi nne | `text-embedding-3-small` |

Utekelezaji wa mbadala usiokuwa na maudhui hutumia mipangilio ya kawaida. Mpangilio huongeza `/openai/v1` mara moja kwa usahihi na huruhusu sheria za sifa, nambari za kuulizia, na njia za zamani za utekelezaji kwenye kituo.

Kila ombi la mazungumzo linaweka wazi `reasoningEffort(ReasoningEffort.NONE)` na `maxCompletionTokens(...)`. Hakuna ombi linaloweka `temperature`, `top_p`, au chaguo la zamani la tokeni za kukamilisha. Hii ni pamoja na uteuzi wa zana na matokeo ya zana. Zana za Chat Completions za GPT-5.6 zinahitaji juhudi za kufikiri `none`; ona [miongozo ya mazungumzo ya Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Hakuna njia ya mtiririko au kuingiza katika sura hii.** Msomaji huchukua hati nzima, sio beche. Ikiwa utaongeza kuingiza, tumia utekelezaji tofauti wa kuingiza kama `text-embedding-3-small`, usitumie Luna.

## Mafunzo 1: Kukamilisha LLM na Mazungumzo

Chanzo: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programu hii hufanya maelezo rahisi ya milia ya Java, mazungumzo ya mzunguko miwili ya HashMap/TreeMap, na mazungumzo ya kushirikiana. Mzunguko wa pili unajumuisha jibu la msaidizi la kwanza; kila mzunguko wa kushirikiana pia hutuma mazungumzo yaliyopita.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` hutoa utekelezaji na mpangilio wazi wa kufikiri. Mazungumzo ya kushirikiana huzua laini tupu, huisha kwa `exit` au EOF, na huhifadhi ujumbe wa mfumo pamoja na mizunguko tisa ya mtumiaji/msaidizi iliyokamilika. Kukata miongoro ni kikomo cha elimu, si dhamana halisi ya bajeti ya tokeni.

Kutoka kwenye saraka ya mifano:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Tarajia majibu matatu ya awali, kisha mwito wa `Wewe:`. Kila swali lisilo tupu la kushirikiana linaongeza ombi moja. Mipaka ya kukamilisha ni 200, 300, 400, kisha 500 tokeni kwa kila mzunguko wa kushirikiana.

## Mafunzo 2: Kupiga Simu ya Kazi

Chanzo: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK hutumia skimu za JSON kutoka kwa rekodi zilizoandikwa `WeatherArguments` na `CalculationArguments`. Uchaguzi wa zana unaotakiwa hufanya kila mfano kufanya mazoezi ya itifaki ya zana badala ya kupokea jibu la mfano bila msaada.

1. Tuma swali pamoja na zana inayoruhusiwa, juhudi ya kufikiri `none`, na kikomo cha kukamilisha tokeni 300.
2. Hitaji sababu ya kumaliza `tool_calls`, thibitisha jina la kazi na vitambulisho vya simu, na changanua hoja za JSON zilizoandikwa.
3. Tekeleza kazi ya ndani. Mfano hauutekelezi msimbo wa Java au msimbo wowote.
4. Ongeza ujumbe wa simu ya kazi mara moja, ikifuatiwa na kila matokeo na `tool_call_id` inayolingana.
5. Tuma ombi la mwisho la tokeni 300 bila zana na liweke sharti jibu lililokamilika, lisilo tupu.

`get_weather` hurudisha hali ya hewa **iliyogharamiwa**, si ya moja kwa moja. Inaheshimu jiji na hubadilisha mfano wa nyuzi joto 22 Celsius hadi Fahrenheit inapoulizwa. `calculate` hutathmini kauli iliyotolewa kupitia exp4j, inasaidia fomu kama `15% ya 240` na `2 + 3 * 4`, na hukataa hesabu tupu, kubwa sana, batili, au zisizo na mwisho. Inatumia hesabu ya pointi za kuzunguka, si usahihi wa biashara wa desimali.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Tarajia `Function: get_weather`, hali ya hewa ya kuigiza Seattle, `Function: calculate`, `Function result: 36`, na majibu mawili ya mwisho. Hakuna stdin au sifa ya hali ya hewa ya nje inahitajika. Mkono mzuri hutumia ombi nne za mazungumzo.

## Mafunzo 3: RAG (Uzalishaji Ulioboreshwa kwa Kupata Taarifa)

Chanzo: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Ingizo: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Mfano huu wa awali wa RAG huchukua hati moja nzima ya UTF-8 na kuiingiza kwenye ujumbe wa mtumiaji pamoja na swali. Ujumbe wa mfumo wa tofauti unamwelekeza mfano kutambua maudhui ya hati kama data isiyoaminika na kujibu kutoka katika muktadha huo tu. Ikiwa hati haina jibu, jibu linalotakiwa ni: `Siwezi kupata taarifa hiyo katika hati iliyotolewa.`

Kuinua muktadha kunaweza kupunguza mawazo potofu, lakini hakuna mipaka wala maagizo ya mfumo yanayohakikisha usahihi au kuzuia kila sehemu ya kuingilia. Kagua majibu moja kwa moja. RAG wa uzalishaji mara nyingi huongeza kugawanya vipande, upatikanaji, rejea, udhibiti wa upatikanaji, na tathmini.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Ingiza swali moja, kwa mfano `Je, hati inaelezea njia gani ya uthibitishaji?`. Tarajia jibu likitajwa Microsoft Entra ID. Programu inatoka baada ya ombi la mazungumzo moja na kikomo cha tokeni 500.

Tafuta faili kwa kawaida kutoka mizizi ya hazina, saraka ya sura, au saraka ya mifano. Njia wazi pia inasaidiwa:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Vingizo lazima visivyokuwa tupu: si zaidi ya 32 KiB ya data ya hati ya UTF-8 na herufi 2,000 za swali. Faili zisizopatikana, maswali tupu/EOF, na vingizo vikubwa huitwa kabla ya uamuzi.

## Mafunzo 4: AI Inayowajibika

Chanzo: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Jaribio sita hulenga maagizo hatarishi, hotuba ya chuki, faragha, habari za matibabu zisizo sahihi, maudhui haramu, na swali la AI inayowajibika la asili. Programu inatazama jibu badala ya kutoa dhana ya kuchochea chujio.

| Matokeo | Shahidi |
| --- | --- |
| `FILTERED` | Msimbo wazi wa kosa wa `content_filter` / `ResponsibleAIPolicyViolation`, au sababu ya kumaliza ya `content_filter` |
| `REFUSED` | Sehemu ya `message.refusal` iliyojazwa isiyokuwa tupu |
| `POSSIBLE_REFUSAL` | Maneno ya awali ya kukataa katika maandishi ya kawaida; kanuni ya ukaguzi |
| `GENERATED` | Jibu lililokamilika na lisilo tupu; si uthibitisho wa usalama wake |

HTTP 400 ya kawaida si ushahidi wa uchujaji. Vigezo batili, kushindwa kuthibitisha, mipaka ya kasi, makosa ya seva, majibu yaliyoharibika, na pato lililosimamishwa hushindwa badala ya kutoa mafanikio ya usalama ya uongo. Maneno mapana kama "maudhui hatari" katika maelezo ya asili hayatambuliki kama kukataa.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Tarajia matokeo sita ya aina na muhtasari unaosema kuwa maoni sio uthibitisho wa usalama. Kila jaribio lina kikomo cha tokeni 300. Kagua uundaji usiotarajiwa na kukataa kwa mikono; kulinganisha la asili kunapaswa kutoa ufafanuzi wa AI inayowajibika. Hakuna stdin inayohitajika.

## Mifumo Miori Katika Mifano

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) hukusanya usawa wa kituo, mbadala za utekelezaji, uthibitishaji bila funguo, na chaguzi za mazungumzo:

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

Mhudumu wa tokeni huongeza tokeni za kufikia inapohitajika. Usirekodi tokeni wala usibadilishe hili kuwa funguo ya API. Kila programu inatumia mteja wake tena na kuufunga katika `finally` au kupitia kifuniko chake cha `AutoCloseable`; `OpenAIClient` ya SDK si `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) inahitaji jibu lililokamilika, lisilo tupu la maandishi. Chaguzi tupu, kukataa, vichujio, na majibu yaliyokatwa hayachapishiwi kimya kama mafanikio. Mfano wa AI inayowajibika hushughulikia matokeo ya chujio/kukataa kwa wazi. Makosa yasiyotazamwa hupewa msimbo wa kutoka wa Java/Maven usio sawa.

**Jaribio la otomatiki la SDK limesitishwa** ili kuweka idadi ya maombi iwe ya kutabirika kwenye utekelezaji wa RPM wa chini uliogawanywa. Kila ombi la uamuzi lina muda wa sekunde 60. Kupata tokeni kunaweza kuchukua muda zaidi. Ratiba ya ngazi ya programu lazima izingatie kigezo; usirudishe ombi lililoshindikana bila kufahamu.

## Majaribio ya Kitengo

Kutoka kwenye saraka ya mifano:

```powershell
mvn -B -ntp clean test
```

Usafirishaji wa jaribio hubadilisha tabaka la HTTP la SDK kabisa, hukamata miili halisi ya maombi yaliyosasishwa, na huwasilisha majibu yaliyopangwa. Haufungui soketi, hupata tokeni za Azure, na hushindwa kwa maombi yasiyotegemewa. Majaribio haya hupima tabia ya programu na itifaki ya SDK, si ubora wa mfano wa moja kwa moja au upatikanaji wa utekelezaji.

| Suite ya Jaribio | Makomo |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Usawazishaji/kukataa kituo, mbadala za utekelezaji, chaguzi za kufikiri na tokeni |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Kila mtiririko wa kukamilisha, historia ya ujumbe, kukata mzunguko kamili, EOF, makosa |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Skimu za zana, hoja zilizoandikwa, hesabu, vitambulisho, matokeo mengi ya zana, kushindwa kufuatilia |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Tafuta faili, UTF-8, mipaka ya ukubwa, mzigo wa kutegemea, makosa ya ingizo na API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Jaribio sita zote, vichujio wazi, uainishaji wa kukataa, kawaida 400 na makosa mengine |

Kwa suite moja, tumia `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Vifaa vinavyotumika wote viko katika [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Uhakiki wa Moja kwa Moja Mfululizo

Mito ya moja kwa moja ni tofauti na majaribio ya kitengo. Tumia maagizo haya **kila moja kwa moja**, kutoka mizizi ya hazina, tu baada ya sifa na ufikiaji wa utekelezaji kuwa tayari. Hakuna huduma au michakato inayodumu inahitajika.

Kwa utekelezaji wa **maombi 10 kwa dakika** uliogawanywa, hifadhi kategoria ya kutosha kwa programu yote ijayo kabla ya kuianzia: maombi 5, 4, 1, kisha 6. Michakato mfululizo pekee haina dhihirisho la kufuata kikomo cha kasi. Ratibu dakika inayorandaranda na waomba waliopo wengine; usipakishe miito minne kama kundi lisilo na ratiba.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Kukamilisha, mizunguko mingi, na mizunguko miwili ya kushirikiana:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Angalia vichwa vya sehemu tatu zote, majibu matano, jibu la mwishoni la kuingiliana linalomkumbuka Ada, `Kwa heri!`, na msimbo wa kutoka 0. Bajeti: **maombi 5, tokeni zisizozidi 1,900 za kukamilisha**. Kwa utekelezaji mdogo, tumia pipe tu `exit`: maombi 3 / tokeni 900, lakini hiyo haisaidii uchambuzi wa kuingiliana.

**2. Michakato yote miwili ya kuitisha kazi:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Angalia majina ya kazi yote mawili, hali ya hewa ya kuigizwa Seattle, matokeo yaliyohesabiwa 36, majibu mawili ya mwisho, na msimbo wa kutoka 0. Bajeti: **maombi 4, tokeni zisizozidi 1,200 za kukamilisha**.

**3. Jibu lililoegemea hati:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Angalia njia ya hati, jibu linalotaja Microsoft Entra ID, na msimbo wa kutoka 0. Bajeti: **ombi 1, tokeni zisizozidi 500 za kukamilisha**. Faili la [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) linalopo ndilo faili pekee la kuingiza linalohitajika. Utekelezaji wa hiari wa pili unaouliza kuhusu mada isiyopo unapaswa kujiepusha na unaongeza ombi moja / tokeni 500.

**4. Maoni ya AI inayojali uwajibikaji:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Angalia makundi sita na muhtasari wa maoni, pitia maudhui yaliyotengenezwa, na omba msimbo wa kutoka 0 kwa kukamilika kwa kiufundi. Kutoka kwa mchakato kwa mafanikio hakuthibitishi usalama wa mfano. Bajeti: **maombi 6, tokeni zisizozidi 1,800 za kukamilisha**.

**Jumla ya amri nne: maombi 16 ya mazungumzo na tokeni zisizozidi 5,400 za kukamilisha**, pamoja na tokeni za kuingiza (pamoja na mazungumzo yaliyorudiwa na mpangilio wa zana/rekodi). Hakuna maombi ya kuingiza kumbukumbu. Matumizi halisi ya tokeni yanategemea mfano na yanaweza kuwa chini, hasa kwa vidokezo vilivyochujwa. Gharama ya dola inategemea bei ya usanidi; hakuna makadirio ya pesa ya moja kwa moja yanayothibitishwa. Vikomo vyote vya ombi vinadhani kuwa hakuna kurudiwa kwa mkono. Angalia haraka `$LASTEXITCODE` baada ya kila amri; isiyo sifuri ina maana utekelezaji haujakamilika kwa mafanikio.

## Utatuzi wa Matatizo

- **Helezo haipo / 401 / 403:** Weka helezo katika mchakato wa kuanzisha, hakikisha umeingia Azure kwa mkoa wako na ugavi wa rasilimali, na angalia kuwekwa kwa mazingira ya utambulisho bila kusudia.
- **400 / 404:** Thibitisha kuwa usanidi upo na unaunga mkono Kukamilishwa kwa Mazungumzo kwa juhudi ya mawazo `none`. Tumia mzizi wa rasilimali wa HTTPS au URL ya `/openai/v1`, sio URL ya usanidi wa zamani. Makosa ya kawaida ya 400 ni matatizo ya kiufundi, si vizuizi vya usalama.
- **429:** Ratibu RPM na mgawo wa tokeni wa pamoja kabla ya kujaribu tena. Mifano haijajaribu tena kiotomatiki kwa makusudi.
- **`Jibu la mazungumzo halijakamilika: urefu`:** Matokeo yaliifikia kikomo cha kukamilika. Pitia jibu na kidokezo kabla ya kuongeza kikomo na bajeti yake iliyoandikwa; usirekodi utekelezaji uliofupishwa kama mafanikio.
- **Makosa ya faili au stdin:** Anzisha kutoka saraka inayounga mkono au toa njia ya wazi ya hati. Toa swali kwa msomaji usioeusi. Kukamilisha kunaweza kumalizika kawaida katika EOF au `exit`.
- **Makosa ya kukusanya:** Hakikisha Java 21 au baadaye, kisha endesha `mvn -B -ntp clean test`. Katika PowerShell, weka nukuu hoja nzima ya Maven inayojumuisha mali yenye doa, kwa mfano `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Hatua Zifuatazo

Endelea na [Sura ya 4: Sampuli za Kivitendo](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Kionyozo**:
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kupata usahihi, tafadhali fahamu kwamba tafsiri za kiotomatiki zinaweza kuwa na makosa au upungufu wa usahihi. Hati ya asili katika lugha yake halisi inapaswa kuchukuliwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu inayofanywa na binadamu inapendekezwa. Hatutojibu kwa kuelewa vibaya au tafsiri potofu zinazotokea kutokana na matumizi ya tafsiri hii.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->