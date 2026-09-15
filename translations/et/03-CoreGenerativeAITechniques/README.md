# Põhilised generatiivse tehisintellekti tehnikate õpetus

## Sisukord

- [Eeldused](#eeldused)
- [Alustamine](#alustamine)
- [Mudeli valiku juhend](#mudeli-valiku-juhend)
- [Õpetus 1: LLM täiendused ja vestlus](#õpetus-1-llm-täiendused-ja-vestlus)
- [Õpetus 2: funktsiooni kutsumine](#õpetus-2-funktsiooni-kutsumine)
- [Õpetus 3: RAG (otsingupõhine genereerimine)](#õpetus-3-rag-otsingupõhine-genereerimine)
- [Õpetus 4: Vastutustundlik tehisintellekt](#õpetus-4-vastutustundlik-ai)
- [Tavalised mustrid näidete seas](#tavalised-mustrid-näidete-seas)
- [Ühiktestid](#ühiktestid)
- [Järjestikune reaalajas kontroll](#järjestikune-reaalajas-kontroll)
- [Tõrkeotsing](#probleemide-lahendamine)
- [Järgmised sammud](#järgmised-sammud)

## Ülevaade

Neli iseseisvat Java programmi demonstreerivad vestlust, vestluse ajalugu, funktsiooni kutsumist, kogu dokumendi põhist otsingupõhist genereerimist (RAG) ja vastutustundliku AI vastuste töötlemist. Kõik vestluspäringud suunatakse vaikimisi **GPT-5.6 Luna mõtlemiskatsestuseta (`none`)** mudelile.

Need näited kasutavad ametlikku OpenAI Java SDK-d Azure OpenAI v1 API lõpp-punkti kaudu, järgides [Microsofti SDK juhiseid](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Vanem `azure-ai-openai` pakett ei ole enam sõltuvus. Chat Completions hoitakse õpetamise eesmärgil olemasolevate sõnumipõhiste töövoogude jaoks; vaata täpsemaid API valikuid [OpenAI Java SDK-st](https://github.com/openai/openai-java#microsoft-azure).

## Eeldused

- Java 21 või uuem ning Maven 3.6.3 või uuem.
- Azure OpenAI vestluse juurutus nimega `gpt-5.6-luna` või ülekate sobivate Chat Completions seadistustega.
- Azure identiteet, mis on sisse logitud ja omab ressursil rolli **Cognitive Services OpenAI User**. Kohalik arendus kasutab Azure CLI sisselogimist; majutatud rakendused saavad kasutada hallatud identiteeti.
- Ressurssi seadistuse ja sisselogimise juhised leiad [2. peatükist](../02-SetupDevEnvironment/getting-started-azure-openai.md).

[Maven konfiguratsioon](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fikseerib järgmised versioonid, kontrollitud 2026-09-14:

| Komponent | Versioon | Eesmärk |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Ametlik Azure v1-ühilduv klient |
| `com.azure:azure-identity` | 1.18.6 | Võtmeteta autentimine ja tokeni uuendamine |
| `net.objecthunter:exp4j` | 0.4.8 | Aritmeetiliste avaldiste parserdamine ilma koodi käivitamiseta |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter ühikutestid |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 kompileerimine, testid, käivitatavad näited |

Kompilaator kasutab `--release 21`. Nendeks iseseisvateks näideteks ei ole vaja Spring Booti, Spring AI-d ega LangChain4j sõltuvust.

## Alustamine

Seadista ressursi lõpp-punkt ja valikuline juurutuse ülekate oma terminalis hoidla juurest.

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

Testid ei vaja Azure mandaate ega lõpp-punkti. Maven ei loe automaatselt keskkonnamuutuja faili; määra muutujad shellis, mis käivitab reaalaegsed näited. IDE-st käivitamisel kontrolli, et käivituskonfiguratsioon edastaks õiged keskkonnamuutujad.

## Mudeli valiku juhend

| Keskkonnamuutuja | Tähendus | Vaiketund |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure ressursi juur või juba normaliseeritud `/openai/v1` URL | Vajalik reaalaegseks käivituseks |
| `AZURE_OPENAI_DEPLOYMENT` | Vestluse juurutuse nimi, mitte mudeli versioon | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Eraldi manustamise juurutuse seadistus, mida need neli programmi ei kasuta | `text-embedding-3-small` |

Tühjad juurutuse ülekanded kasutavad vaikimisi sätteid. Konfiguratsioon lisab `/openai/v1` täpselt korra ja nõuab, et lõpp-punkt ei sisaldaks mandaate, päringuread ega vanu juurutusviise.

Iga vestluspäring seab selgesõnaliselt `reasoningEffort(ReasoningEffort.NONE)` ja `maxCompletionTokens(...)`. Ükski päring ei määra `temperature`, `top_p` ega vana täiendustokeni valikut. See hõlmab ka tööriistade valikut ja tulemuste järeldusi. GPT-5.6 Chat Completions funktsioonide tööriistad nõuavad mõtlemiskatsestust `none`; vaata [Microsofti vestlusjuhiseid](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Selles peatükis puudub voogedastus ja manustamise sisendpunkt.** Lugeja hangib kogu dokumendi, mitte vektoreid. Kui laiendad manustustega, kasuta eraldi manustamise juurutust nagu `text-embedding-3-small`, mitte Luna.

## Õpetus 1: LLM täiendused ja vestlus

Allikas: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programm jookseb lihtsa Java streams seletuse, kahekäigulise HashMap/TreeMap vestluse ja interaktiivse vestluse. Teine käik sisaldab esimest assistendi vastust; iga interaktiivne käik saadab ka eelneva vestluse.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` määrab juurutuse ja otsese mõtlemise sätte. Interaktiivne vestlus vahele jätab tühjad read, lõpetab `exit` või EOF korral ning hoiab süsteemisõnumit ja üheksa täidetud kasutaja/assistendi käiku. Käikude arvu piiramine on hariduslik, mitte täpne tokeni eelarve garantii.

Näidete kataloogist:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Oota kolme esmast vastust, seejärel `You:` sisselülitust. Iga mitte-tühi interaktiivne küsimus lisab ühe päringu. Täienduse piirangud on 200, 300, 400 ja lõpuks 500 tokenit iga pöörde kohta.

## Õpetus 2: funktsiooni kutsumine

Allikas: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK tuletab JSON skeemid tähistatud `WeatherArguments` ja `CalculationArguments` kirjetest. Vajaliku tööriista valik tagab, et iga näide harjutab tööriista protokolli, mitte ei aktsepteeri mudeli iseseisvat vastust.

1. Saada küsimus lubatud tööriistaga, mõtlemiskatsestusega `none` ja 300 tokeni täienduse piiranguga.
2. Nõua `tool_calls` lõpetamise põhjust, valideeri funktsiooni nimi ja kõne ID-d ning loo tüübitud JSON parameetrite analüüs.
3. Käivita kohalik funktsioon. Mudel ei käivita Java või suvalist koodi.
4. Lisa assistendi tööriista kõne sõnum üks kord, seejärel iga tulemusega sobiv `tool_call_id`.
5. Saada üks lõplik 300 tokeni päring ilma tööriistadeta ja nõua lõpetatud ning mitte-tühja vastust.

`get_weather` tagastab **simuleeritud**, mitte reaalajas ilmaandmed. See arvestab linna ning teisendab 22 kraadi Celsiuse näidispõhjal Fahrenheitiks, kui nõutud. `calculate` hindab sisestatud avaldist exp4j abil, toetab vorme nagu `15% of 240` ja `2 + 3 * 4`, ning lükkab tagasi tühjad, liiga suured, vigased või mitte-lõplikud arvutused. See kasutab liitmurruaritmeetikat, mitte finantsilist kümnendkoha täpsustust.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Oota `Function: get_weather`, simuleeritud Seattle ilm, `Function: calculate`, `Function result: 36` ja kahte lõplikku vastust. Ei ole vaja stdin'i ega väliseid ilma mandaate. Edukas jooks kasutab täpselt nelja vestlus päringut.

## Õpetus 3: RAG (otsingupõhine genereerimine)

Allikas: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Sisend: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

See algeline RAG näide hangib ühe kogu UTF-8 dokumendi ja lisab selle kasutaja sõnumisse koos küsimusega. Eraldi süsteemisõnum õpetab mudelit käsitlema dokumendi sisu usaldamata andmetena ning vastama ainult selle konteksti põhjal. Kui dokument ei sisalda vastust, tuleb vastusena: `I cannot find that information in the provided document.`

Põhjendus võib vähendada hallutsinatsioone, kuid ei piira täpsust ega kogu võimalikke prompt-süstimisi. Vaata reaalajas vastuseid. Tootmine RAG lisab tavaliselt tükkimise, otsingu, viited, ligipääsu kontrolli ja hindamise.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Sisesta üks küsimus, näiteks `Which authentication method does the document describe?`. Oota vastust, mis mainib Microsoft Entra ID-d. Programm lõpetab pärast ühte vestlus päringut 500 tokeni täienduse piiril.

Vaikimisi failide otsing toimib hoidla juures, peatüki või näidete kataloogis. Toetatud on ka täpne tee:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Sisendid peavad olema mittekhüljatud: kuni 32 KiB UTF-8 dokumendi andmeid ja 2000 küsimuse tähemärki. Puuduvad failid, tühjad/EOF küsimused ja ülemõõdulised sisendid ebaõnnestuvad enne järeldamist.

## Õpetus 4: vastutustundlik AI

Allikas: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Kuus proovi puudutavad kahjulikke juhiseid, vihakõnet, privaatsust, meditsiinilisi väärinfot, ebaseaduslikku sisu ja heatahtlikku vastutustundliku AI küsimust. Programm vaatleb vastust, mitte ei eelda, et iga proov peab aktiveerima filtri.

| Tulemus | Tõend |
| --- | --- |
| `FILTERED` | Selgelt märgitud `content_filter` / `ResponsibleAIPolicyViolation` veakood või täienduse `content_filter` lõpetamise põhjus |
| `REFUSED` | Mitte-tühi struktureeritud `message.refusal` väli |
| `POSSIBLE_REFUSAL` | Tavalises tekstis algusvastuse fraas; heuristiline, mis vajab ülevaatust |
| `GENERATED` | Täielik mitte-tühi vastus; mitte tõend selle sisu ohutuse kohta |

Tavaline HTTP 400 ei ole filtreerimise tõend. Vead parameetrites, autentimises, määrade piirangud, serverivead, vigased vastused ja lühendatud väljund ebaõnnestuvad ning ei anna valet turvastegurit. Laialt kasutatavad sõnad nagu "kahjulik sisu" heatahtlikus selgituses ei loeta keeldumiseks.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Oota kuut kategooria tulemust ja kokkuvõtet, mis ütleb, et vaatlused ei ole turvasertifikaadi tõend. Iga proov on 300 tokeni täienduse piiranguga. Käsitsi loo ülevaade ootamatutest vastustest ja võimalikest keeldumistest; heatahtlik võrdlus peaks tooma vea kirjutamise vastutustundliku AI selgituse. Stdin ei ole vajalik.

## Tavalised mustrid näidete seas

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) tsentraliseerib lõpp-punkti normaliseerimise, juurutuse ülekanded, võtmepõhise autentimise ja vestluse valikud:

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

Tokenite hankija uuendab juurdepääsulubasid vastavalt vajadusele. Ära logi tokeneid ega asenda neid API võtmega. Iga programm kasutab oma klienti uuesti ja sulgeb selle `finally` või `AutoCloseable` mähise kaudu; SDK `OpenAIClient` ise ei ole `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) nõuab lõpetatud, mitte-tühja tekstivastust. Tühjad valikud, keeldumised, filtrid ja lühendatud vastused ei prindi vaikides edukat tulemust. Vastutustundliku-AI õpetus käsitleb selgelt oodatud filter/keeldumise olukordi. Käideldamata vead annavad Java/Maveni protsessile mittetühja väljundi koodi.

**SDK automaatsed kordused on keelatud**, et hoida päringute arv jagatud madala RPM juurutuste puhul prognoositavana. Igal järelduspäringul on 60-sekundiline ajoaeg. Tokeni hankimine võib võtta lisa aega. Rakenduse taseme ajastamine peab austama kvotasid; ära suvaliselt korda ebaõnnestunud tasulist päringut.

## Ühiktestid

Näidete kataloogist:

```powershell
mvn -B -ntp clean test
```

Test transpordikiht asendab täielikult SDK HTTP kihi, jäädvustab päringukehade tegeliku serialiseerimise ja tarnib järjekordset vastuseid. See ei ava socket'e, ei haara Azure tokeneid ega ebaõnnestu ootamatute päringute korral. Need testid valideerivad rakenduse käitumist ja SDK protokolli, mitte elava mudeli kvaliteeti ega juurutuse kättesaadavust.

| Testikomplekt | Katvus |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Lõpp-punkti normaliseerimine/keeldumine, juurutuse ülekanded, mõtlemise ja tokeni valikud |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Kõik täiendamise töövood, sõnumi ajalugu, täis-käikude kärpimine, EOF, vead |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Tööriistade skeemid, tüübitud argumendid, aritmeetika, ID-d, mitmed tööriista tulemused, ebaõnnestunud järeldused |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Faili otsing, UTF-8, suuruse piirangud, põhjenduse koormus, sisendi ja API vead |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Kuus proovi, selged filtrid, keeldumise klassifikatsioon, tavaline 400 ja muud vead |

Ühe komplekti jooksutamiseks kasuta `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Jagatud konfigureeritud ning tehtud kood on [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Järjestikune reaalajas kontroll

Reaalajas kõned on eraldi ühikutestidest. Kasuta järgmisi käske **üksikult**, hoidla juurest ja alles pärast mandaadi ja juurutuse ligipääsu valmimist. Teenuseid või püsivaid protsesse pole vaja.

Jagatud **10 päringut minutis** juurutuse korral broneeri järgmise programmi tarbeks piisavalt kvotat enne käivitamist: 5, 4, 1 ja 6 päringut. Järjestikused protsessid üksi ei taga määra piiri järgimist. Koordineeri rulluvat minutit kõigi kutsujatega; ära kleebi kõiki nelja kutset järjest ilma pausita.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Täiendused, mitmekäiguline ja kaks interaktiivset käiku:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Kontrolli kõiki kolme jaotise pealkirju, viit vastust, lõplikku interaktiivset vastust Ada meenutamisega, `Head aega!` ja väljundkoodi 0. Eelarve: **5 päringut, maksimaalselt 1900 täiendustokenit**. Väiksemaks käivitamiseks kasuta ainult `exit`: 3 päringut / 900 täiendustokenit, kuid see ei hõlma interaktiivset järeldamist.

**2. Mõlemad funktsiooni-kutsumise töövood:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Kontrolli mõlema funktsiooni nime, simuleeritud Seattle ilmastikku, arvutatud tulemust 36, kahte lõplikku vastust ja väljundkoodi 0. Eelarve: **4 päringut, maksimaalselt 1200 täiendustokenit**.

**3. Dokumendipõhine vastus:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Kontrolli dokumendi teed, vastust, mis mainib Microsoft Entra ID-d, ja väljundkoodi 0. Eelarve: **1 päring, maksimaalselt 500 täiendustokenit**. Olemasolev [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) on ainus nõutav sisendfail. Valikuline teine käivitamine, mis küsib puuduv teema kohta, peaks hoiduma ja lisab ühe päringu / 500 tokenit.

**4. Vastutustundliku tehisintellekti tähelepanekud:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Kontrolli kuut kategooriat ja tähelepanekute kokkuvõtet, ülevaata genereeritud sisu ning nõua tehniliseks lõpetamiseks väljundkoodi 0. Edukas protsessi väljumine ei taga mudeli ohutust. Eelarve: **6 päringut, maksimaalselt 1800 täiendustokenit**.

**Kokku nelja käsu jaoks: 16 vestluse päringut ja maksimaalselt 5400 täiendustokenit**, pluss sisendtokenid (sh korduv vestlus ja tööriista skeem/ajalugu). Manustamispäringuid ei ole. Tegelik tokenite kasutus sõltub mudelist ja võib olla madalam, eriti filtreeritud promptide puhul. Dollarikulu sõltub juurutuse hinnastamisest; fikseeritud rahaline hinnang puudub. Kõik päringu limiidid eeldavad käsitsi kordamisel mitteosalemist. Kontrolli sisse kohe pärast iga käsku `$LASTEXITCODE`; nullist erinev tähendab, et töö ei lõpetanud edukalt.

## Probleemide lahendamine

- **Puuduv lõpp-punkt / 401 / 403:** Sea lõpp-punkt käivitusprotsessis, kontrolli oma kohalikku Azure'i sisselogimist ja ressursi ulatuses rolli ning vaata, et poleks tahtmatuid identiteedikonteksti ülekirjutusi.
- **400 / 404:** Veendu, et juurutus on olemas ja toetab Chat Completions funktsiooni ilma järeldamiseta (`none`). Kasuta HTTPS ressursi juurt või `/openai/v1` URL-i, mitte vana juurutuse URL-i. Tavapärased 400 vead on tehnilised vead, mitte ohutuse blokeeringud.
- **429:** Koordineeri jagatud RPM ja tokenite limiidid enne uuesti katsetamist. Näited ei tee automaatset kordust.
- **`Ebatäielik vestluse vastus: pikkus`:** Vastus jõudis täienduste limiidini. Kontrolli vastust ja prompti enne limiidi ning selle dokumenteeritud eelarve tõstmist; ära kirjuta lühendatud käivitust edukaks.
- **Faili või stdin-vead:** Käivita toetatud kataloogist või anna selge dokumendi tee. Esita mitte-tühi lugejaküsimus. Täiendused lõppevad korralikult EOF või `exit` korral.
- **Kompileerimisvead:** Veendu, et kasutad Java 21 või uuemat, siis käivita `mvn -B -ntp clean test`. PowerShellis pane kogu Maveniga seotud argument jutumärkidesse, kui seal on punktidega omadused, nt `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Järgmised sammud

Jätka peatükiga [4. peatükk: Praktilised näited](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Lahtiütlus**:
See dokument on tõlgitud kasutades AI tõlketeenust [Co-op Translator](https://github.com/Azure/co-op-translator). Kuigi me püüdleme täpsuse poole, palun pange tähele, et automatiseeritud tõlgetes võib esineda vigu või ebatäpsusi. Originaaldokument selle emakeeles tuleks pidada autoriteetseks allikaks. Olulise teabe puhul soovitatakse kasutada professionaalset inimtõlget. Me ei vastuta selle tõlkega seotud eksimustest või valesti mõistmistest.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->