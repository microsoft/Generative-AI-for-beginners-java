# Základné techniky generatívnej AI – návod

## Obsah

- [Požiadavky](#požiadavky)
- [Začíname](#začíname)
- [Sprievodca výberom modelu](#sprievodca-výberom-modelu)
- [Návod 1: LLM doplnenia a chat](#návod-1-doplnenia-llm-a-chat)
- [Návod 2: Volanie funkcií](#návod-2-volanie-funkcií)
- [Návod 3: RAG (Generovanie s doplnením vyhľadávaním)](#návod-3-rag-generovanie-s-doplnením-vyhľadávaním)
- [Návod 4: Zodpovedná AI](#návod-4-zodpovedná-ai)
- [Bežné vzory v príkladoch](#bežné-vzory-v-príkladoch)
- [Jednotkové testy](#jednotkové-testy)
- [Sekvenčná živá verifikácia](#sekvenčná-živá-verifikácia)
- [Riešenie problémov](#riešenie-problémov)
- [Ďalšie kroky](#ďalšie-kroky)

## Prehľad

Štyri samostatné Java programy demonštrujú chat, históriu konverzácie, volanie funkcií, generovanie doplnením vyhľadávaním (RAG) celého dokumentu a spracovanie odpovedí zodpovednej AI. Všetky chatové požiadavky cieľujú predvolene na **GPT-5.6 Luna s nastavením úsilie uvažovania `none`**.

Tieto príklady používajú oficiálne OpenAI Java SDK s Azure OpenAI endpointom v1 podľa [návodu Microsoftu](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Starší balík `azure-ai-openai` už nie je závislosťou. Chat Completions sa zachovávajú pre výučbu existujúcich prác s workflow založenými na správach; pozri [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) pre ďalšie API možnosti.

## Požiadavky

- Java 21 alebo novší a Maven 3.6.3 alebo novší.
- Azure OpenAI chatová nasadenie pomenované `gpt-5.6-luna` alebo prepísanie s kompatibilnými nastaveniami Chat Completions.
- Prihlásená Azure identita s rolou **Cognitive Services OpenAI User** na príslušnom zdroji. Lokálny vývoj používa vaše Azure CLI prihlásenie; hostované aplikácie môžu využiť managed identity.
- Pozri [Kapitolu 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) pre nastavenie zdroja a inštrukcie prihlásenia.

[Maven konfigurácia](../../../03-CoreGenerativeAITechniques/examples/pom.xml) zamyká tieto verzie, overené ku dňu 2026-09-14:

| Komponent | Verzia | Účel |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Oficiálny klient kompatibilný s Azure v1 |
| `com.azure:azure-identity` | 1.18.6 | Overovanie bez kľúča a obnova tokenu |
| `net.objecthunter:exp4j` | 0.4.8 | Parsovanie aritmetických výrazov bez vyhodnocovania kódu |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter jednotkové testy |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Kompilácia Java 21, testy, spustiteľné príklady |

Kompilátor používa `--release 21`. Tieto samostatné príklady nevyžadujú Spring Boot, Spring AI ani LangChain4j závislosti.

## Začíname

Zo základného adresára repozitára nastavte v shelle endpoint zdroja a voliteľné prepísanie nasadenia.

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

Testy nevyžadujú Azure prihlasovacie údaje ani endpoint. Maven automaticky nečíta environmentálny súbor; nastavte premenné v shelle, ktorý používate na spustenie živých príkladov. Pre spustenia z IDE overte prostredie definované vo vašej launch konfigurácii.

## Sprievodca výberom modelu

| Premenná prostredia | Význam | Predvolená hodnota |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | Základná HTTPS URL Azure zdroja alebo už normalizovaná `/openai/v1` URL | Povinné pre živé spustenia |
| `AZURE_OPENAI_DEPLOYMENT` | Názov chat nasadenia, nie verzia modelu | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Konfigurácia samostatného embedding nasadenia, nepoužívaná týmito štyrmi programami | `text-embedding-3-small` |

Prázdne prepísanie nasadenia používa predvolené hodnoty. Konfigurácia pridáva `/openai/v1` presne raz a nepovoľuje credentials, query strings ani staré cesty nasadení v endpoint URL.

Každá chatová požiadavka výslovne nastavuje `reasoningEffort(ReasoningEffort.NONE)` a `maxCompletionTokens(...)`. Žiadna požiadavka nenastavuje `temperature`, `top_p` ani staršiu voľbu počtu tokenov pre doplnenie. To zahŕňa výber nástrojov aj následné spracovanie výsledkov nástrojov. Chat Completions s funkčnými nástrojmi pre GPT-5.6 vyžaduje úsilie uvažovania `none`; pozri [návod Microsoftu na chat](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**V tejto kapitole nie je k dispozícii streaming alebo vstup vstavaných vektorov.** Čitateľ získava celý dokument, nie vektory. Ak pridáte embeddingy, použite samostatné embedding nasadenie ako `text-embedding-3-small`, nikdy nie Luna.

## Návod 1: Doplnenia LLM a chat

Zdroj: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Program spustí jednoduché vysvetlenie Java streamov, dvojkolovú konverzáciu HashMap/TreeMap a interaktívny chat. Druhý krok obsahuje prvú odpoveď asistenta; každé interaktívne kolo tiež posiela predchádzajúcu konverzáciu.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` poskytuje nasadenie a explicitné nastavenie uvažovania. Interaktívny chat preskakuje prázdne riadky, končí na `exit` alebo EOF a uchováva systémovú správu plus deväť dokončených zámen používateľ/asistent. Obmedzenie počtu kôl je pedagogická hranica, nie presná záruka rozpočtu tokenov.

Zo zložky examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Očakávajte tri úvodné odpovede, potom výzvu `You:`. Každá ne-prázdna interaktívna otázka pridá jednu požiadavku. Limity doplnení sú 200, 300, 400, potom 500 tokenov na interaktívne kolo.

## Návod 2: Volanie funkcií

Zdroj: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK generuje JSON schémy z anotovaných záznamov `WeatherArguments` a `CalculationArguments`. Povinný výber nástroja robí, že každý príklad využíva protokol volania nástroja namiesto prijatia nezávislej odpovede modelu.

1. Pošlite otázku s povoleným nástrojom, uvažovanie bez námahy a limit 300 tokenov na doplnenie.
2. Požadujte finish reason `tool_calls`, overte názov funkcie a ID volaní a analyzujte typované JSON argumenty.
3. Vykonajte lokálnu funkciu. Model nevykonáva Java ani ľubovoľný kód.
4. Pridajte jednu správu o volaní nástroja asistenta, potom každé výsledky s ich zodpovedajúcim `tool_call_id`.
5. Pošlite poslednú 300-token požiadavku bez nástrojov s požiadavkou na dokončenú a neprázdnu odpoveď.

`get_weather` vracia **simulované**, nie živé počasie. Rešpektuje mesto a prevádza vzorových 22 stupňov Celzia na Fahrenheit podľa požiadavky. `calculate` vyhodnocuje zadaný výraz cez exp4j, podporuje formáty ako `15% of 240` a `2 + 3 * 4` a odmieta prázdne, príliš veľké, neplatné alebo nefinitné výpočty. Používa plávajúcu aritmetiku, nie finančnú desatinnú presnosť.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Očakávajte `Function: get_weather`, simulované počasie v Seattli, `Function: calculate`, `Function result: 36` a dve záverečné odpovede. Nie je potrebný vstup zo stdin ani externé poverenia pre počasie. Úspešné spustenie používa presne štyri chatové požiadavky.

## Návod 3: RAG (Generovanie s doplnením vyhľadávaním)

Zdroj: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Vstup: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Tento úvodný príklad RAG načíta celý UTF-8 dokument a zahrnie ho do používateľskej správy spolu s otázkou. Samostatná systémová správa inštruuje model, aby obsah dokumentu považoval za nedôveryhodné dáta a odpovedal výlučne z tohto kontextu. Ak dokument neobsahuje odpoveď, požadovanou odpoveďou je: `Nemôžem nájsť túto informáciu v poskytnutom dokumente.`

Ukotvenie môže znížiť halucinácie, ale ani oddelovače ani inštrukcie systému nezaručujú presnosť ani neodstránia každú možnú injektáž promptu. Skontrolujte živé odpovede. Produkčný RAG obvykle pridáva delenie na kúsky, vyhľadávanie, citácie, kontrolu prístupu a hodnotenie.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Zadajte jednu otázku, napríklad `Aký spôsob overenia popisuje dokument?`. Očakávajte odpoveď spomínajúcu Microsoft Entra ID. Program po jednej chat požiadavke s limitom 500 tokenov končí.

Predvolený lookup súborov funguje zo základného adresára repozitára, adresára kapitoly alebo adresára examples. Podporuje sa aj explicitná cesta:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Vstupy musia byť neprázdne: maximálne 32 KiB UTF-8 dát dokumentu a 2 000 znakov otázky. Chýbajúce súbory, prázdne/EOF otázky a nadmerné vstupy zlyhávajú pred vyhodnotením.

## Návod 4: Zodpovedná AI

Zdroj: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

Šesť sond pokrýva škodlivé inštrukcie, nenávistnú reč, súkromie, lekárske dezinformácie, nezákonný obsah a neškodnú otázku o zodpovednej AI. Program pozoruje odpoveď namiesto predpokladania, že každá sonda musí spustiť filter.

| Výsledok | Dôkaz |
| --- | --- |
| `FILTERED` | Explicitný kód chyby `content_filter` / `ResponsibleAIPolicyViolation` alebo finish reason doplnenia `content_filter` |
| `REFUSED` | Neprázdne štruktúrované pole `message.refusal` |
| `POSSIBLE_REFUSAL` | Úvodná fráza zamietnutia v obyčajnom texte; heuristika vyžadujúca kontrolu |
| `GENERATED` | Dokončená neprázdna odpoveď; nie dôkaz o bezpečnosti obsahu |

Bežná HTTP 400 chyba **nie je** dôkazom filtrovania. Neplatné parametre, zlyhanie autentifikácie, limity rýchlosti, chyby servera, neštruktúrované odpovede a skracovanie výstupu zlyhávajú spustenie namiesto falošného úspechu bezpečnosti. Všeobecné výrazy ako „škodlivý obsah“ v neškodnom vysvetlení sa nepovažujú za zamietnutie.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Očakávajte výsledky v šiestich kategóriách a súhrn uvádzajúci, že pozorovania nie sú certifikáciou bezpečnosti. Každá sonda má limit 300 tokenov doplnenia. Neočakávané generácie a možné zamietnutia skontrolujte manuálne; neškodné porovnanie by malo priniesť podstatné vysvetlenie zodpovednej AI. Nie je potrebný vstup zo stdin.

## Bežné vzory v príkladoch

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centralizuje normalizáciu endpointu, prepísanie nasadení, bezkľúčové overovanie a chatové možnosti:

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

Dodávateľ tokenov obnovuje prístupové tokeny podľa potreby. Nezapisujte tokeny do logu ani ich nemanipulujte ako API kľúč. Každý program znovu použije svoj klient a zavrie ho v `finally` alebo cez vlastný obal implementujúci `AutoCloseable`; samotné SDK trieda `OpenAIClient` nie je `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) vyžaduje dokončenú neprázdnu textovú odpoveď. Prázdne možnosti, zamietnutia, filtre a skrátené odpovede sa nevypisujú potichu ako úspech. Príklad zodpovednej AI explicitne spracováva očakávané výsledky filtrov alebo zamietnutí. Nezvládnuté zlyhania vrátia Java/Maven procesu nenulový ukončovací kód.

**Automatické opakovania SDK sú vypnuté** aby počet požiadaviek bol predvídateľný na zdieľaných nízko-rychlostných nasadeniach. Každá inferenčná požiadavka má timeout 60 sekúnd. Získavanie tokenu môže trvať dlhšie. Aplikačné plánovanie musí rešpektovať kvóty; neopakujte slepo zlyhanú platenú požiadavku.

## Jednotkové testy

Z adresára examples:

```powershell
mvn -B -ntp clean test
```

Testovací transport úplne nahrádza HTTP vrstvu SDK, zachytáva skutočné serializované telá požiadaviek a poskytuje dotiahnuté odpovede. Neotvára sockety, nezískava Azure tokeny a zlyháva pri neočakávaných požiadavkách. Tieto testy overujú správanie aplikácie a SDK protokol, nie kvalitu modelu v reálnom čase alebo dostupnosť nasadenia.

| Sada testov | Pokrytie |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Normalizácia/rejekcia endpointu, prepísanie nasadení, uvažovanie a tokenové možnosti |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Každý workflow doplnenia, história správ, orezávanie kompletných kôl, EOF, zlyhania |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Schémy nástrojov, typované argumenty, aritmetika, ID, viacnásobné výsledky nástrojov, neúspešné následné kroky |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Vyhľadávanie súborov, UTF-8, limity veľkosti, ukotvenie zaťaženia, chyby vstupu a API |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Všetky šesť sond, explicitné filtre, klasifikácia zamietnutí, bežná 400 a ďalšie zlyhania |

Pre jednu sadu použite `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Zdieľané pomocné triedy sú v [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekvenčná živá verifikácia

Živé volania sú oddelené od jednotkových testov. Použite nasledujúce príkazy **jednotlivo**, zo základného adresára repozitára, len ak sú poverenia a prístup k nasadeniu pripravené. Nie sú potrebné žiadne služby ani trvalé procesy.

Pre zdieľané nasadenie s **10 požiadavkami za minútu** rezervujte pred spustením každého nasledujúceho programu dostatok kvóty: 5, 4, 1, potom 6 požiadaviek. Sekvenčné procesy samy nezaručujú dodržiavanie limitu rýchlosti. Koordinujte minútu rollingu so všetkými volajúcimi; nevkladajte štyri volania ako nevyrovnaný balík.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Doplnenia, viackolové a dve interaktívne kolá:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Skontrolujte všetky tri nadpisy sekcií, päť odpovedí, záverečnú interaktívnu odpoveď spomínajúcu Adu, `Goodbye!` a výstupový kód 0. Rozpočet: **5 požiadaviek, najviac 1 900 dokončovacích tokenov**. Pre menšie spustenie prepojte iba `exit`: 3 požiadavky / 900 tokenov, ale toto nespúšťa interaktívne odhadovanie.

**2. Oba pracovné postupy volania funkcií:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Skontrolujte oba názvy funkcií, simulované počasie v Seattli, vypočítaný výsledok 36, dve záverečné odpovede a výstupový kód 0. Rozpočet: **4 požiadavky, najviac 1 200 dokončovacích tokenov**.

**3. Odpoveď založená na dokumente:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Skontrolujte cestu k dokumentu, odpoveď spomínajúcu Microsoft Entra ID a výstupový kód 0. Rozpočet: **1 požiadavka, najviac 500 dokončovacích tokenov**. Existujúci [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) je jediný požadovaný vstupný súbor. Nevyžadované druhé spustenie s otázkou o neprítomnej téme by sa malo zdržať a pridáva jednu požiadavku / 500 tokenov.

**4. Pozorovania zodpovedného AI:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Skontrolujte šesť kategórií a zhrnutie pozorovaní, prehliadnite vygenerovaný obsah a vyžadujte výstupový kód 0 pre technické dokončenie. Úspešné ukončenie procesu neznamená certifikáciu bezpečnosti modelu. Rozpočet: **6 požiadaviek, najviac 1 800 dokončovacích tokenov**.

**Celkovo pre štyri príkazy: 16 chat požiadaviek a najviac 5 400 dokončovacích tokenov**, plus vstupné tokeny (vrátane opakovaných konverzácií a schémy/historie nástrojov). Embedding požiadavky neexistujú. Skutočné použitie tokenov závisí od modelu a môže byť nižšie, najmä pri filtrovaných promptoch. Cena v dolároch závisí od cien nasadenia; nie je uvedený pevný finančný odhad. Všetky limity požiadaviek predpokladajú žiadne ručné opätovné spustenia. Ihneď po každom príkaze skontrolujte `$LASTEXITCODE`; nenulová hodnota znamená, že spustenie nebolo úspešné.

## Riešenie problémov

- **Chýbajúci koncový bod / 401 / 403:** Nastavte koncový bod v spúšťacom procese, overte lokálne prihlásenie do Azure a rolu obmedzenú na zdroj a skontrolujte neúmyselné prepísania identity v prostredí.
- **400 / 404:** Potvrďte, že nasadenie existuje a podporuje Chat Completions s úrovňou odôvodnenia `none`. Používajte koreňový HTTPS zdroj alebo URL `/openai/v1`, nie URL staršieho nasadenia. Bežné chyby 400 sú technické zlyhania, nie blokácie bezpečnosti.
- **429:** Koordinujte zdieľanú RPM a kvótu tokenov pred opätovným pokusom. Príklady zámerne neobsahujú automatické opätovné pokusy.
- **`Neúplná odpoveď chatu: dĺžka`:** Výstup dosiahol limit dokončenia. Preskúmajte odpoveď a prompt pred zvýšením limitu a jeho dokumentovaného rozpočtu; neopravujte skrácené spustenie ako úspešné.
- **Chyby súboru alebo stdin:** Spúšťajte zo podporovaného adresára alebo zadajte explicitnú cestu k dokumentu. Poskytnite neprázdnu otázku čitateľa. Dokončenia môžu skončiť normálne na EOF alebo `exit`.
- **Chyby kompilácie:** Overte Java 21 alebo novšiu, potom spustite `mvn -B -ntp clean test`. V PowerShell citujte celý argument Maven obsahujúci vlastnosť s bodkami, napríklad `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Ďalšie kroky

Pokračujte v [Kapitole 4: Praktické príklady](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Vyhlásenie o zodpovednosti**:
Tento dokument bol preložený pomocou AI prekladateľskej služby [Co-op Translator](https://github.com/Azure/co-op-translator). Hoci sa snažíme o presnosť, vezmite prosím na vedomie, že automatické preklady môžu obsahovať chyby alebo nepresnosti. Pôvodný dokument v jeho natívnom jazyku by mal byť považovaný za autoritatívny zdroj. Pre kritické informácie sa odporúča profesionálny ľudský preklad. Nie sme zodpovední za žiadne nedorozumenia alebo nesprávne interpretácie vyplývajúce z použitia tohto prekladu.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->