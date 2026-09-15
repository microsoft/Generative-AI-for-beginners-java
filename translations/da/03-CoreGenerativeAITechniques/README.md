# Grundlæggende Generativ AI Teknikker Tutorial

## Indholdsfortegnelse

- [Forudsætninger](#forudsætninger)
- [Kom godt i gang](#kom-godt-i-gang)
- [Vejledning til valg af model](#vejledning-til-valg-af-model)
- [Tutorial 1: LLM Fuldførelser og Chat](#tutorial-1-llm-fuldførelser-og-chat)
- [Tutorial 2: Funktionsopkald](#tutorial-2-funktionsopkald)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: Ansvarlig AI](#tutorial-4-ansvarlig-ai)
- [Fælles mønstre på tværs af eksempler](#fælles-mønstre-på-tværs-af-eksempler)
- [Unittest](#unittest)
- [Sekventiel live verifikation](#sekventiel-live-verifikation)
- [Fejlfinding](#fejlfinding)
- [Næste trin](#næste-skridt)

## Oversigt

Fire selvstændige Java-programmer demonstrerer chat, samtalehistorik, funktionsopkald, hel-dokument retrieval-augmented generation (RAG) og ansvarlig AI responsbehandling. Alle chat-forespørgsler målretter **GPT-5.6 Luna med ræsonnementsindsats `none`** som standard.

Disse eksempler bruger den officielle OpenAI Java SDK med Azure OpenAI's v1-endpoint, i overensstemmelse med [Microsofts SDK-vejledning](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Den ældre `azure-ai-openai` pakke er ikke længere en afhængighed. Chat Fuldførelser bibeholdes for at undervise i eksisterende beskedbaserede workflows; se [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) for andre API-muligheder.

## Forudsætninger

- Java 21 eller nyere og Maven 3.6.3 eller nyere.
- En Azure OpenAI chat-udrulning navngivet `gpt-5.6-luna`, eller en override med kompatible Chat Fuldførelser-indstillinger.
- En tilmeldt Azure-identitet med rollen **Cognitive Services OpenAI User** på ressourcen. Lokal udvikling bruger din Azure CLI-login; hostede applikationer kan bruge managed identity.
- Se [Kapitel 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) for opsætning af ressourcer og login-instruktioner.

[Maven-konfigurationen](../../../03-CoreGenerativeAITechniques/examples/pom.xml) fastlåser disse versioner, bekræftet pr. 2026-09-14:

| Komponent | Version | Formål |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Officiel Azure v1-kompatibel klient |
| `com.azure:azure-identity` | 1.18.6 | Nøglefri autentificering og token-opdatering |
| `net.objecthunter:exp4j` | 0.4.8 | Parsing af aritmetiske udtryk uden kodeevaluering |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter unit tests |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 kompilering, tests, kørbare eksempler |

Kompilatoren benytter `--release 21`. Ingen Spring Boot, Spring AI eller LangChain4j afhængighed er nødvendig for disse selvstændige eksempler.

## Kom godt i gang

Fra repository-roden, sæt resource-endpoint og valgfrie udrulnings-overrides i dit shell.

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

Tests kræver hverken Azure legitimationsoplysninger eller et endpoint. Maven læser ikke automatisk en miljøfil; sæt variabler i det shell, der bruges til at starte live eksempler. For IDE-kørsler, tjek at det leverede miljø i launch-konfigurationen er korrekt.

## Vejledning til valg af model

| Miljøvariabel | Betydning | Standard |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure resource root eller allerede-normaliseret `/openai/v1` URL | Påkrævet for live kørsel |
| `AZURE_OPENAI_DEPLOYMENT` | Chat-udrulningsnavn, ikke en modelversion | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Separate embedding udrulningskonfiguration, ikke brugt af disse fire programmer | `text-embedding-3-small` |

Tomme udrulnings-overrides bruger standarderne. Konfigurationen tilføjer `/openai/v1` præcis én gang og afviser legitimationsoplysninger, forespørgselsstrenge og ældre udrulningsstier i endpointet.

Alle chat-forespørgsler sætter eksplicit `reasoningEffort(ReasoningEffort.NONE)` og `maxCompletionTokens(...)`. Ingen anmodning sætter `temperature`, `top_p` eller den ældre fuldførelses-token option. Dette inkluderer værktøjsvalg og efterfølgende værktøjsresultater. GPT-5.6 Chat Fuldførelser funktionsværktøjer kræver ræsonnementsindsats `none`; se [Microsofts chat-vejledning](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Der findes hverken streaming eller embedding-entrépunkt i dette kapitel.** Læseren henter hele sit dokument, ikke vektorer. Hvis du udvider med embeddings, brug da en separat embedding-udrulning som `text-embedding-3-small`, aldrig Luna.

## Tutorial 1: LLM Fuldførelser og Chat

Kilde: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programmet kører en enkel forklaring på Java streams, en tohjulet HashMap/TreeMap-samtale og interaktiv chat. Den anden samtaleudveksling indeholder det første assistent-svar; hver interaktiv udveksling sender også sin tidligere samtale.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` leverer udrulningen og den eksplicitte ræsonneringsindstilling. Interaktiv chat springer blanke linjer over, slutter ved `exit` eller EOF og fastholder systembeskeden plus ni gennemførte bruger-/assistent-udvekslinger. Udvekslingsbegrænsning er en læringsmæssig grænse, ikke en nøjagtig token-budget garanti.

Fra eksempelmappen:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Forvent tre indledende svar, derefter en `You:` prompt. Hvert ikke-blankt interaktivt spørgsmål tilføjer en forespørgsel. Maksimum for fuldførelser er 200, 300, 400 og til sidst 500 tokens pr. interaktive udveksling.

## Tutorial 2: Funktionsopkald

Kilde: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK udleder JSON-skemaer fra de annoterede `WeatherArguments` og `CalculationArguments` records. Et påkrævet værktøjsvalg får hvert eksempel til at bruge værktøjsprotokollen i stedet for at acceptere en models eget svar.

1. Send et spørgsmål med det tilladte værktøj, ræsonnementsindsats `none` og en 300-token fuldførelsesgrænse.
2. Kræv en `tool_calls` finish-årsag, valider funktionsnavn og kald-ID'er og parse typed JSON-argumenter.
3. Kør den lokale funktion. Modellen udfører ikke Java eller vilkårlig kode.
4. Tilføj assistentens værktøjsopkaldsbesked én gang, efterfulgt af hvert resultat med det matchende `tool_call_id`.
5. Send én endelig 300-token forespørgsel uden værktøjer og kræv et fuldstændigt, ikke-tomt svar.

`get_weather` returnerer **simuleret**, ikke live, vejr. Den respekterer byen og konverterer de eksemplariske 22 grader Celsius til Fahrenheit ved forespørgsel. `calculate` evaluerer den angivne udtryk via exp4j, understøtter former som `15% af 240` og `2 + 3 * 4`, og afviser tomme, for store, ugyldige eller ikke-endelige beregninger. Den bruger flydende decimal aritmetik, ikke finansiel decimal præcision.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Forvent `Function: get_weather`, simuleret vejr i Seattle, `Function: calculate`, `Function result: 36`, og de to endelige svar. Ingen stdin eller eksterne vejr-legitimationsoplysninger kræves. En succesfuld kørsel bruger præcis fire chat-forespørgsler.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Kilde: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Input: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Dette introducerende RAG-eksempel henter ét helt UTF-8 dokument og inkluderer det i brugermeldingen sammen med spørgsmålet. En separat systembesked instruerer modellen i at opfatte dokumentindhold som upålidelige data og kun svare ud fra denne kontekst. Hvis dokumentet ikke indeholder svaret, skal den anmodede respons være: `I cannot find that information in the provided document.`

Grundlæggelse kan reducere hallucinationer, men hverken afgrænsere eller systeminstruktioner garanterer nøjagtighed eller forhindrer alle prompt-injektioner. Gennemgå altid live-svar. Produktion RAG tilføjer normalt chunking, retrieval, kildeangivelser, adgangskontrol og evaluering.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Indtast ét spørgsmål, for eksempel `Which authentication method does the document describe?`. Forvent et svar der nævner Microsoft Entra ID. Programmet afslutter efter én chat-forespørgsel med 500-token fuldførelsesgrænse.

Standard fil-opslag virker fra repository-roden, kapitel-mappen eller eksempelmappen. En eksplicit sti understøttes også:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Input skal være ikke-tomme: højst 32 KiB UTF-8 dokumentdata og 2.000 tegn i spørgsmål. Manglende filer, tomme/EOF spørgsmål og for store input fejler før inferens.

## Tutorial 4: Ansvarlig AI

Kilde: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

De seks prøver dækker skadelige instruktioner, hadtale, privatliv, medicinsk misinformation, ulovligt indhold og et uskyldigt ansvarlig-AI spørgsmål. Programmet observerer svaret frem for at antage at hver prøve må aktivere et filter.

| Resultat | Bevis |
| --- | --- |
| `FILTERED` | En eksplicit `content_filter` / `ResponsibleAIPolicyViolation` fejlkode, eller en fuldførelse med `content_filter` afslutningsårsag |
| `REFUSED` | Et ikke-tomt struktureret `message.refusal` felt |
| `POSSIBLE_REFUSAL` | En indledende afvisningssætning i almindelig tekst; en heuristik der kræver gennemgang |
| `GENERATED` | Et fuldført ikke-tomt svar; ikke bevis for, at indholdet er sikkert |

En almindelig HTTP 400 er **ikke** bevis på filtrering. Ugyldige parametre, autentificeringsfejl, grænser for forespørgsler, serverfejl, fejlformaterede svar og afkortet output fejler kørslen i stedet for at give en falsk sikkerhedssucces. Bredt formulerede ord såsom "skadeligt indhold" i en uskyldig forklaring tæller ikke som afvisning.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Forvent seks kategorieresultater og et overblik der fastslår, at observationerne ikke er en sikkerhedscertificering. Hver prøve har en 300-token fuldførelsesgrænse. Gennemgå uventede generationer og mulige afvisninger manuelt; den uskyldige sammenligning bør producere en substantiel ansvarlig-AI forklaring. Ingen stdin kræves.

## Fælles mønstre på tværs af eksempler

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centraliserer endpoint-normalisering, udrulnings-overrides, nøglefri autentificering og chat-indstillinger:

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

Token-leverandøren opdaterer adgangstokens efter behov. Log ikke tokens og erstat ikke dette med en API-nøgle. Hvert program genbruger sin klient og lukker den i `finally` eller via sin egen `AutoCloseable` wrapper; SDK's `OpenAIClient` er ikke `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) kræver et fuldstændigt, ikke-tomt tekstsvar. Tomme valg, afvisninger, filtre og afkortede svar udskrives ikke tavst som succes. Eksemplet om ansvarlig AI håndterer forventede filter-/afvisningsresultater eksplicit. Uhåndterede fejl giver Java/Maven processen en ikke-nul exitkode.

**Automatiske SDK-genforsøg er deaktiveret** for at holde forespørgselsantal forudsigelige på delte lav-RPM udrulninger. Hver inferens-forespørgsel har en 60-sekunders timeout. Token ervervelse kan tage yderligere tid. Applikationsniveau planlægning skal respektere kvoter; kør ikke blindt en fejlet betalt forespørgsel igen.

## Unittest

Fra eksempelmappen:

```powershell
mvn -B -ntp clean test
```

Testtransporten erstatter SDK HTTP-laget fuldstændigt, fanger faktiske serialiserede forespørgselskroppen og leverer køede svar. Den åbner ingen sokler, anskaffer ingen Azure tokens og fejler ved uventede forespørgsler. Disse tests validerer applikationsadfærd og SDK-protokol, ikke live-modelkvalitet eller udrulnings-tilgængelighed.

| Testsuite | Dækning |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Endpoint-normalisering/afvisning, udrulnings-overrides, ræsonnerings- og token-indstillinger |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Hver fuldførelsesworkflow, beskedhistorik, trimming af komplette vendinger, EOF, fejl |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Værktøjs-skemaer, typed argumenter, aritmetik, ID'er, flere værktøjsresultater, fejlede opfølgninger |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Filopslag, UTF-8, størrelsesgrænser, grundlæggelses-payload, input- og API-fejl |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Alle seks prøver, eksplicitte filtre, afvisningsklassificering, almindelig 400 og andre fejl |

For én suite, brug `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Delte testdata findes i [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekventiel live verifikation

Live-kald er adskilt fra unittests. Brug følgende kommandoer **enkeltvis**, fra repository-roden, først når legitimationsoplysninger og udrulningsadgang er klar. Ingen tjenester eller vedvarende processer kræves.

For en delt **10 forespørgsler/minut** udrulning, reserver nok kvote til næste program i sin helhed før start: 5, 4, 1, og til sidst 6 forespørgsler. Sekventielle processer alene garanterer ikke overholdelse af hastighedsgrænse. Koordiner det rullende minut med alle andre kaldere; indsæt ikke de fire kald som et ubrudt batch.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Fuldførelser, multi-vending og to interaktive vendinger:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Tjek alle tre sektionoverskrifter, fem svar, et afsluttende interaktivt svar, der nævner Ada, `Goodbye!`, og afslutningskode 0. Budget: **5 forespørgsler, højst 1.900 færdiggørelsestokens**. For en mindre kørsel kan du kun pipe `exit`: 3 forespørgsler / 900 tokens, men det aktiverer ikke interaktiv inferens.

**2. Begge funktionskald-workflows:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Tjek begge funktionsnavne, simuleret vejr i Seattle, beregnet resultat 36, to afsluttende svar og afslutningskode 0. Budget: **4 forespørgsler, højst 1.200 færdiggørelsestokens**.

**3. Dokumentbaseret svar:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Tjek dokumentstien, et svar der nævner Microsoft Entra ID, og afslutningskode 0. Budget: **1 forespørgsel, højst 500 færdiggørelsestokens**. Den eksisterende [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) er den eneste nødvendige inputfil. En valgfri anden kørsel, der spørger om et fraværende emne, bør afstå og tilføjer en forespørgsel / 500 tokens.

**4. Ansvarlig-AI observationer:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Tjek seks kategorier og observationsopsummeringen, gennemgå det genererede indhold og kræv afslutningskode 0 for teknisk færdiggørelse. En vellykket procesafslutning garanterer ikke modelsikkerhed. Budget: **6 forespørgsler, højst 1.800 færdiggørelsestokens**.

**I alt for de fire kommandoer: 16 chatforespørgsler og højst 5.400 færdiggørelsestokens**, plus inputtokens (inklusive gentaget samtale og værktøjsskema/historik). Der er nul embeddingsforespørgsler. Faktisk tokenforbrug afhænger af model og kan være lavere, især for filtrerede prompts. Dollaromkostning afhænger af prisfastsættelse på deployment; ingen fast monetær vurdering er impliceret. Alle forespørgselsgrænser forudsætter ingen manuelle genkørsler. Inspicer `$LASTEXITCODE` straks efter hver kommando; ikke-nul betyder, at kørslen ikke afsluttede succesfuldt.

## Fejlfinding

- **Manglende endpoint / 401 / 403:** Angiv endpoint i opstartsprocessen, verificer din lokale Azure-login og ressourcerolle, og tjek for utilsigtede identitetsmiljøoverstyringer.
- **400 / 404:** Bekræft at deployment findes og understøtter Chat Completions med reasoning effort `none`. Brug HTTPS-ressourceroden eller `/openai/v1` URL, ikke en gammel deployments-URL. Almindelige 400-fejl er tekniske fejl, ikke sikkerhedsblokeringer.
- **429:** Koordiner den delte RPM og tokenkvote før genforsøg. Eksemplerne foretager ikke automatisk genforsøg.
- **`Ufuldstændigt chat-svar: længde`:** Output nåede grænsen for færdiggørelse. Gennemgå svaret og prompten, før du øger grænsen og dens dokumenterede budget; registrer ikke en afkortet kørsel som succesfuld.
- **Fil- eller stdin-fejl:** Start fra en understøttet mappe eller angiv en eksplicit dokumentsti. Giv et ikke-tomt læserspørgsmål. Færdiggørelser kan afsluttes normalt ved EOF eller `exit`.
- **Kompileringsfejl:** Bekræft Java 21 eller nyere, og kør derefter `mvn -B -ntp clean test`. I PowerShell, sæt anførselstegn omkring hele Maven-argumentet, der indeholder en punktsepareret egenskab, fx `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Næste Skridt

Fortsæt til [Kapitel 4: Praktiske Eksempler](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi påtager os intet ansvar for misforståelser eller fejltolkninger, der opstår som følge af brugen af denne oversættelse.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->