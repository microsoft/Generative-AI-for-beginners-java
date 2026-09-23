# Grundläggande tekniker för generativ AI - Tutorial

## Innehållsförteckning

- [Förkunskaper](#förkunskaper)
- [Komma igång](#komma-igång)
- [Guide för val av modell](#guide-för-val-av-modell)
- [Tutorial 1: LLM-kompletteringar och chatt](#tutorial-1-llm-kompletteringar-och-chatt)
- [Tutorial 2: Funktionsanrop](#tutorial-2-funktionsanrop)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: Ansvarsfull AI](#tutorial-4-ansvarsfull-ai)
- [Vanliga mönster över exempel](#vanliga-mönster-över-exempel)
- [Enhetstester](#enhetstester)
- [Sekventiell liveverifiering](#sekventiell-liveverifiering)
- [Felsökning](#felsökning)
- [Nästa steg](#nästa-steg)

## Översikt

Fyra fristående Java-program demonstrerar chatt, konversationshistorik, funktionsanrop, hel-dokument Retrieval-Augmented Generation (RAG) och hantering av ansvarsfulla AI-svar. Alla chattförfrågningar riktas som standard till **GPT-5.6 Luna med resonemangsenhet `none`**.

Dessa exempel använder det officiella OpenAI Java SDK med Azure OpenAIs v1-endpoint, enligt [Microsofts SDK-riktlinjer](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Det äldre `azure-ai-openai`-paketet är inte längre en beroende. Chat Completions behålls för att lära ut de befintliga meddelandebaserade arbetsflödena; se [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) för andra API-alternativ.

## Förkunskaper

- Java 21 eller senare och Maven 3.6.3 eller senare.
- En Azure OpenAI-chatdistribution med namnet `gpt-5.6-luna` eller en överskridning med kompatibla inställningar för Chat Completions.
- En inloggad Azure-identitet med rollen **Cognitive Services OpenAI User** på resursen. Lokal utveckling använder din inloggning i Azure CLI; hostade applikationer kan använda hanterad identitet.
- Se [Kapitel 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) för resursuppsättning och inloggningsinstruktioner.

[Maven-konfigurationen](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fastställer dessa versioner, kontrollerade 2026-09-14:

| Komponent | Version | Syfte |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Officiell Azure v1-kompatibel klient |
| `com.azure:azure-identity` | 1.18.6 | Autentisering utan nyckel och tokenförnyelse |
| `net.objecthunter:exp4j` | 0.4.8 | Parsering av aritmetiska uttryck utan kodutvärdering |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter-enhetstester |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21-kompilering, tester, körbara exempel |

Kompilatorn använder `--release 21`. Ingen beroende av Spring Boot, Spring AI eller LangChain4j krävs för dessa fristående exempel.

## Komma igång

Från repositorierot, ange resurs-endpoint och eventuell distributionsöverskridning i din shell.

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

Tester kräver varken Azure-uppgifter eller endpoint. Maven läser inte automatiskt en miljöfil; sätt variabler i den shell som används för att starta liveexempel. Vid start i IDE, verifiera miljön som tillhandahålls av din startkonfiguration.

## Guide för val av modell

| Miljövariabel | Betydelse | Standard |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure-resursrot eller redan-normaliserad `/openai/v1` URL | Obligatorisk för livekörningar |
| `AZURE_OPENAI_DEPLOYMENT` | Namn på chattdistribution, inte en modellversion | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Separat embedding-distribution, oanvänd av dessa fyra program | `text-embedding-3-small` |

Tomma distributionsöverskridningar använder standardvärden. Konfigurationen lägger till `/openai/v1` exakt en gång och avvisar autentisering, frågesträngar och legacy-distributionsvägar i endpointen.

Varje chattförfrågan anger uttryckligen `reasoningEffort(ReasoningEffort.NONE)` och `maxCompletionTokens(...)`. Ingen förfrågan sätter `temperature`, `top_p` eller det legacy completion-token-alternativet. Detta inkluderar verktygsval och uppföljningar av verktygsresultat. GPT-5.6 Chat Completions-funktionsverktyg kräver resonemangsenhet `none`; se [Microsofts chattriktlinjer](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Det finns ingen streaming- eller embedding-ingång i detta kapitel.** Läsaren hämtar hela dokumentet, inte vektorer. Om du utökar med embeddings, använd en separat embedding-distribution som `text-embedding-3-small`, aldrig Luna.

## Tutorial 1: LLM-kompletteringar och chatt

Källa: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programmet kör en enkel förklaring av Java streams, en två-turs HashMap/TreeMap-konversation och interaktiv chatt. Andra turen inkluderar det första assistent-svaret; varje interaktiv tur skickar även föregående konversation.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` tillhandahåller distribution och uttrycklig resonemangsinställning. Interaktiv chatt hoppar över tomma rader, slutar vid `exit` eller EOF och behåller systemmeddelandet plus nio kompletta användar-/assistentturer. Trimmningen av turantal är en pedagogisk begränsning, inte en exakt tokenbudgetsgaranti.

Från examples-mappen:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Förvänta dig tre initiala svar, sedan en `Du:` prompt. Varje icke-tom interaktiv fråga lägger till en förfrågan. Kompletteringsgränser är 200, 300, 400 och sedan 500 tokens per interaktiv tur.

## Tutorial 2: Funktionsanrop

Källa: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK härleder JSON-scheman från de annoterade `WeatherArguments` och `CalculationArguments` posterna. Ett obligatoriskt verktygsval gör att varje exempel utövar verktygsprotokollet istället för att acceptera modellens obesuttna svar.

1. Skicka en fråga med tillåtet verktyg, resonemangsenhet `none` och en limit på 300 tokens för komplettering.
2. Kräv `tool_calls` som avslutningsorsak, bekräfta funktionsnamn och samtals-ID:n, och parsa typade JSON-argument.
3. Kör den lokala funktionen. Modellen kör inte Java eller godtycklig kod.
4. Lägg till assistentens verktygsanropsmeddelande en gång, följt av varje resultat med dess matchande `tool_call_id`.
5. Skicka en sista 300-token förfrågan utan verktyg och kräva ett fullständigt, icke-tomt svar.

`get_weather` returnerar **simulerad**, inte live, väderdata. Den respekterar staden och konverterar exempelvis 22 grader Celsius till Fahrenheit när det efterfrågas. `calculate` utvärderar det angivna uttrycket via exp4j, stödjer former som `15% of 240` och `2 + 3 * 4`, och avvisar tomma, för stora, ogiltiga eller icke-finitära beräkningar. Den använder flyttalsaritmetik, inte finansiell decimalprecision.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Förvänta dig `Function: get_weather`, simulerat väder för Seattle, `Function: calculate`, `Function result: 36`, och de två slutliga svaren. Ingen stdin eller externa väderuppgifter krävs. En lyckad körning använder exakt fyra chattförfrågningar.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Källa: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Indata: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Detta introduktions-RAG-exempel hämtar ett helt UTF-8-dokument och inkluderar det i användarmeddelandet tillsammans med frågan. Ett separat systemmeddelande instruerar modellen att behandla dokumentinnehållet som opålitlig data och endast svara från det sammanhanget. Om dokumentet inte innehåller svaret, är det begärda svaret: `Jag kan inte hitta den informationen i det angivna dokumentet.`

Grundläggning kan minska hallucinationer, men varken avgränsare eller systeminstruktioner garanterar noggrannhet eller förhindrar varje promptinjektion. Granska live-svar. Produktion av RAG lägger normalt till chunkning, hämtning, referenser, åtkomstkontroll och utvärdering.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Ange en fråga, t.ex. `Vilken autentiseringsmetod beskriver dokumentet?`. Förvänta ett svar som nämner Microsoft Entra ID. Programmet avslutas efter en chattförfrågan med en 500-token begränsning.

Standard filuppslagning fungerar från repositorierot, kapitelkatalog eller examples-katalog. En explicit sökväg stöds också:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Indatat måste vara icke-tomt: högst 32 KiB UTF-8 dokumentdata och 2 000 frågetecken. Saknade filer, tomma/EOF-frågor och för stora indata misslyckas innan inferens.

## Tutorial 4: Ansvarsfull AI

Källa: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

De sex testerna täcker skadliga instruktioner, hatpropaganda, integritet, medicinsk felinformation, olagligt innehåll och en benign ansvarsfull AI-fråga. Programmet observerar svaret istället för att anta att varje test måste trigga ett filter.

| Utfall | Bevis |
| --- | --- |
| `FILTERED` | En explicit `content_filter` / `ResponsibleAIPolicyViolation` felkod, eller en `content_filter`-avslutningsorsak för komplettering |
| `REFUSED` | Ett icke-tomt strukturerat `message.refusal` fält |
| `POSSIBLE_REFUSAL` | En inledande avvisningsfras i vanlig text; en heuristik som kräver granskning |
| `GENERATED` | Ett komplett icke-tomt svar; inte bevis för att innehållet är säkert |

Ett vanligt HTTP 400 är **inte** bevis på filtrering. Ogiltiga parametrar, autentiseringsfel, hastighetsbegränsningar, serverfel, felaktiga svar och avklippt utdata misslyckas i körningen istället för att producera en falsk säkerhetssuccé. Bredda ord som "skadligt innehåll" i en benign förklaring räknas inte som en avvisning.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Förvänta sex kategorisvar och en sammanfattning som säger att observationerna inte är en säkerhetscertifiering. Varje test har en 300-token gräns för komplettering. Granska oväntade genereringar och eventuella avvisningar manuellt; den benigna jämförelsen bör ge en substantiell ansvarsfull AI-förklaring. Ingen stdin krävs.

## Vanliga mönster över exempel

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centraliserar endpoint-normalisering, distributionsöverskridningar, autentisering utan nyckel och chattalternativ:

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

Tokensleverantören förnyar åtkomsttokens vid behov. Logga inte tokens eller ersätt detta med en API-nyckel. Varje program återanvänder sin klient och stänger den i `finally` eller via sin egen `AutoCloseable` wrapper; SDK:ns `OpenAIClient` är inte `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) kräver ett komplett, icke-tomt textbaserat svar. Tomma val, avvisningar, filter och avklippta svar skrivs inte tyst ut som framgång. Exemplet för ansvarsfull AI hanterar förväntade filter-/avvisningsresultat explicit. Ohanterade fel ger Java/Maven-processen en icke-noll exitcode.

**Automatiska SDK-omförsök är inaktiverade** för att hålla förfrågningsantalet förutsägbart på delade låg-RPM-distributioner. Varje inferensförfrågan har en 60-sekunders timeout. Tokenförvärv kan ta extra tid. Schemaläggning på applikationsnivå måste respektera kvoter; kör inte blint om en misslyckad betald förfrågan.

## Enhetstester

Från examples-katalogen:

```powershell
mvn -B -ntp clean test
```

Testtransporten ersätter SDK HTTP-lagret helt, fångar faktiska serialiserade förfrågningskroppar och levererar köade svar. Den öppnar inga sockets, erhåller inga Azure-tokens och misslyckas vid oväntade förfrågningar. Dessa tester validerar applikationsbeteende och SDK-protokoll, inte levande modellers kvalitet eller distributionsbarhet.

| Testsvit | Täckning |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Endpoint-normalisering/avvisning, distributionsöverskridningar, resonemangs- och tokenalternativ |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Alla kompletteringsarbetsflöden, meddelandehistorik, trimning av kompletta turer, EOF, fel |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Verktygsscheman, typade argument, aritmetik, ID:n, flera verktygsresultat, misslyckade uppföljningar |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Filuppslagning, UTF-8, storleksgränser, grundningspayload, indata- och API-fel |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Alla sex tester, explicita filter, avvisningsklassificering, vanliga 400 och andra fel |

För en svit, använd `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Delade fixeringsfiler finns i [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekventiell liveverifiering

Liveanrop är separata från enhetstester. Använd följande kommandon **var för sig**, från repositorierot, endast efter att behörigheter och distributionsåtkomst är klara. Inga tjänster eller bestående processer krävs.

För en delad **10 förfrågningar/minut** distribution, reservera tillräcklig kvot för hela nästa program innan du startar: 5, 4, 1, sedan 6 förfrågningar. Sekventiella processer garanterar inte ensamt att hastighetsgränsen följs. Koordinera den rullande minuten med alla andra användare; klistra inte in de fyra anropen som en okontrollerad batch.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Kompletteringar, multi-turn och två interaktiva turer:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Kontrollera alla tre avsnittsrubriker, fem svar, ett slutgiltigt interaktivt svar som nämner Ada, `Goodbye!` och avslutningskod 0. Budget: **5 förfrågningar, högst 1 900 kompletteringstoken**. För en mindre körning, skicka endast `exit`: 3 förfrågningar / 900 token, men det tränar inte interaktiv inferens.

**2. Båda funktion-anropsarbetsflödena:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Kontrollera båda funktionsnamnen, simulerat Seattle-väder, beräknat resultat 36, två slutgiltiga svar och avslutningskod 0. Budget: **4 förfrågningar, högst 1 200 kompletteringstoken**.

**3. Dokumentgranskat svar:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Kontrollera dokumentvägen, ett svar som nämner Microsoft Entra ID och avslutningskod 0. Budget: **1 förfrågan, högst 500 kompletteringstoken**. Den befintliga [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) är den enda nödvändiga indatfilen. En valfri andra körning som frågar om ett frånvarande ämne bör avstå och tillför en förfrågan / 500 token.

**4. Ansvarsfull AI-observationer:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Kontrollera sex kategorier och observationssammanfattningen, granska det genererade innehållet och kräva avslutningskod 0 för teknisk slutförande. En lyckad processavslutning garanterar inte modellsäkerhet. Budget: **6 förfrågningar, högst 1 800 kompletteringstoken**.

**Totalt för de fyra kommandona: 16 chattförfrågningar och högst 5 400 kompletteringstoken**, plus indatatoken (inklusive upprepad konversation och verktygsschema/historik). Det finns noll inbäddningsförfrågningar. Faktisk tokenanvändning beror på modellen och kan vara lägre, särskilt för filtrerade prompts. Kostnaden i dollar beror på distributionsprissättning; ingen fast penninguppskattning impliceras. Alla förfrågningsgränser förutsätter inga manuella omkörningar. Kontrollera `$LASTEXITCODE` omedelbart efter varje kommando; icke-noll betyder att körningen inte slutfördes framgångsrikt.

## Felsökning

- **Saknat slutpunkt / 401 / 403:** Ange slutpunkten i startprocessen, kontrollera din lokala Azure-inloggning och resursbegränsade roll, och kontrollera oavsiktliga identitetsmiljööverskrivningar.
- **400 / 404:** Bekräfta att distributionen finns och stödjer chattkompletteringar med resoneringsinsats `none`. Använd HTTPS-resursrot eller `/openai/v1` URL, inte en äldre distributions-URL. Vanliga 400-fel är tekniska fel, inte säkerhetshinder.
- **429:** Koordinera den delade RPM och tokenkvotan innan du försöker igen. Exempelvis försöker inte automatiskt igen.
- **`Ofullständigt chatt-svar: längd`:** Utdata nådde gränsen för komplettering. Granska svaret och prompten innan du ökar gränsen och dess dokumenterade budget; registrera inte en avklippt körning som framgångsrik.
- **Fil- eller stdin-fel:** Starta från en stödd katalog eller ange en explicit dokumentväg. Ge en icke-tom läsarfråga. Kompletteringar kan sluta normalt vid EOF eller `exit`.
- **Kompileringsfel:** Verifiera Java 21 eller senare, kör sedan `mvn -B -ntp clean test`. I PowerShell, citera hela Maven-argumentet som innehåller en punktseparerad egenskap, till exempel `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Nästa steg

Fortsätt till [Kapitel 4: Praktiska Exempel](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfriskrivning**:
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, var vänlig notera att automatiska översättningar kan innehålla fel eller brister. Det ursprungliga dokumentet på dess modersmål bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för några missförstånd eller feltolkningar som uppstår till följd av användningen av denna översättning.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->