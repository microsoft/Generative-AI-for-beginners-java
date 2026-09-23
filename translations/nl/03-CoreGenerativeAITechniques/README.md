# Kerntechnieken van Generatieve AI Tutorial

## Inhoudsopgave

- [Vereisten](#vereisten)
- [Aan de slag](#aan-de-slag)
- [Gids voor modelkeuze](#gids-voor-modelkeuze)
- [Tutorial 1: LLM-completies en chat](#tutorial-1-llm-completies-en-chat)
- [Tutorial 2: Functieaanroepen](#tutorial-2-functieaanroepen)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](#tutorial-3-rag-retrieval-augmented-generation)
- [Tutorial 4: Verantwoorde AI](#tutorial-4-verantwoorde-ai)
- [Veelvoorkomende patronen in voorbeelden](#veelvoorkomende-patronen-in-voorbeelden)
- [Unit-tests](#unit-tests)
- [Sequentiële live verificatie](#sequentiële-live-verificatie)
- [Probleemoplossing](#problemen-oplossen)
- [Volgende stappen](#volgende-stappen)

## Overzicht

Vier zelfstandige Java-programma's demonstreren chat, gespreksgeschiedenis, functieaanroepen, whole-document retrieval-augmented generation (RAG) en verantwoord AI-reactiebeheer. Alle chatverzoeken zijn standaard gericht op **GPT-5.6 Luna met redeneerinspanning `none`**.

Deze voorbeelden gebruiken de officiële OpenAI Java SDK met Azure OpenAI's v1-eindpunt, volgens de [SDK-richtlijnen van Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Het oudere `azure-ai-openai`-pakket is geen afhankelijkheid meer. Chat Completions wordt behouden om de bestaande op berichten gebaseerde workflows te onderwijzen; zie de [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) voor andere API-opties.

## Vereisten

- Java 21 of later en Maven 3.6.3 of later.
- Een Azure OpenAI-chatimplementatie genaamd `gpt-5.6-luna`, of een override met compatibele Chat Completions-instellingen.
- Een aangemelde Azure-identiteit met de rol **Cognitive Services OpenAI User** op de resource. Lokale ontwikkeling gebruikt je Azure CLI-aanmelding; gehoste applicaties kunnen managed identity gebruiken.
- Zie [Hoofdstuk 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) voor resource-instelling en aanmeldingsinstructies.

De [Maven-configuratie](../../../03-CoreGenerativeAITechniques/examples/pom.xml) sluit deze versies vast, gecontroleerd op 2026-09-14:

| Component | Versie | Doel |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Officiële Azure v1-compatibele client |
| `com.azure:azure-identity` | 1.18.6 | Sleutelloze authenticatie en tokenverversing |
| `net.objecthunter:exp4j` | 0.4.8 | Parseren van rekenkundige uitdrukkingen zonder code-uitvoering |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter-unit-tests |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 compilatie, tests, uitvoerbare voorbeelden |

De compiler gebruikt `--release 21`. Geen Spring Boot, Spring AI of LangChain4j-afhankelijkheid is nodig voor deze zelfstandige voorbeelden.

## Aan de slag

Stel vanuit de repository-root het resource-eindpunt en optionele implementatie-override in in je shell.

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

Tests vereisen geen Azure-referenties noch een eindpunt. Maven leest niet automatisch een omgevingsbestand; stel variabelen in de shell in die je gebruikt om live voorbeelden te starten. Voor IDE-launches, controleer de omgeving die door je launchconfiguratie wordt geleverd.

## Gids voor modelkeuze

| Omgevingsvariabele | Betekenis | Standaardwaarde |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure resource root of al-genormaliseerde `/openai/v1` URL | Vereist voor live uitvoeringen |
| `AZURE_OPENAI_DEPLOYMENT` | Naam van chat-implementatie, geen modelversie | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Configuratie voor aparte embedding-implementatie, ongebruikt door deze vier programma's | `text-embedding-3-small` |

Lege implementatie-overrides gebruiken de standaardwaarden. De configuratie voegt `/openai/v1` precies één keer toe en weigert referenties, querystrings en legacy-implementatiepaden in het eindpunt.

Elk chatverzoek stelt expliciet `reasoningEffort(ReasoningEffort.NONE)` en `maxCompletionTokens(...)` in. Geen enkel verzoek stelt `temperature`, `top_p` of de legacy optie voor completion-tokens in. Dit omvat tool-selecties en tool-resultaat follow-ups. GPT-5.6 Chat Completions functietools vereisen redeneerinspanning `none`; zie de [chatrichtlijnen van Microsoft](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Er is geen streaming- of embedding-invoerpunt in dit hoofdstuk.** De lezer haalt het gehele document op, niet vectoren. Als je uitbreidt met embeddings, gebruik dan een aparte embedding-implementatie zoals `text-embedding-3-small`, nooit Luna.

## Tutorial 1: LLM-completies en chat

Bron: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Het programma voert een eenvoudige uitleg van Java streams uit, een gesprek van twee beurten met HashMap/TreeMap en interactieve chat. De tweede beurt bevat de eerste assistent-respons; elke interactieve beurt stuurt ook het voorgaande gesprek.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` levert de implementatie en expliciete redeneerinspanning. Interactieve chat slaat lege regels over, eindigt bij `exit` of EOF, en bewaart het systeembericht plus negen voltooide gebruiker/assistent beurten. Het beperken van aantal beurten is een educatieve grens, geen garantie op exact tokenbudget.

Vanuit de map examples:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Verwacht drie initiële antwoorden, daarna een `Jij:` prompt. Elke niet-lege interactieve vraag voegt één verzoek toe. Limieten voor completions zijn 200, 300, 400 en daarna 500 tokens per interactieve beurt.

## Tutorial 2: Functieaanroepen

Bron: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

De SDK genereert JSON-schema's uit de geannoteerde records `WeatherArguments` en `CalculationArguments`. Een verplichte toolkeuze laat elk voorbeeld de tool-protocol gebruiken in plaats van een onondersteund antwoord van het model.

1. Verstuur een vraag met de toegestane tool, redeneerinspanning `none` en een limiet van 300 tokens voor completion.
2. Vereis een `tool_calls` finish reden, valideer de functienaam en call ID's, en parseer getypte JSON-argumenten.
3. Voer de lokale functie uit. Het model voert geen Java- of willekeurige code uit.
4. Voeg het assistent-tool-call bericht één keer toe, gevolgd door elk resultaat met bijpassende `tool_call_id`.
5. Verstuur een laatste verzoek zonder tools van 300 tokens en vereis een voltooid, niet-leeg antwoord.

`get_weather` geeft **geanimeerd**, geen live weer. Het respecteert de stad en zet voorbeeld 22 graden Celsius om naar Fahrenheit als dat wordt gevraagd. `calculate` evalueert de opgegeven expressie via exp4j, ondersteunt vormen zoals `15% van 240` en `2 + 3 * 4`, en weigert lege, te grote, ongeldige of niet-eindige berekeningen. Het maakt gebruik van drijvende-komma rekenkunde, geen financiële decimale precisie.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Verwacht `Function: get_weather`, gesimuleerd weer van Seattle, `Function: calculate`, `Function result: 36` en de twee laatste antwoorden. Geen stdin of externe weerreferenties zijn vereist. Een geslaagde run gebruikt precies vier chatverzoeken.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

Bron: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Invoer: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Dit voorlopige RAG-voorbeeld haalt één volledig UTF-8-document op en voegt het toe aan het gebruikersbericht met de vraag. Een apart systeembericht instrueert het model om documentinhoud als niet-vertrouwde data te behandelen en alleen uit die context te antwoorden. Als het document het antwoord niet bevat, luidt de gevraagde respons: `Ik kan deze informatie niet vinden in het aangeleverde document.`

Grondslag kan hallucinaties verminderen, maar geen enkele delimiter of systeeminstructie garandeert nauwkeurigheid of voorkomt elke promptinjectie. Controleer live antwoorden. Productie-RAG voegt normaal chunking, retrieval, citaties, toegangscontrole en evaluatie toe.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Voer één vraag in, bijvoorbeeld `Welke authenticatiemethode beschrijft het document?`. Verwacht een antwoord waarbij Microsoft Entra ID wordt genoemd. Het programma sluit af na één chatverzoek met een limiet van 500 tokens.

De standaard bestandsopzoeking werkt vanaf de repository-root, het hoofdstuk-map of de examples-map. Een expliciet pad wordt ook ondersteund:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Invoer moet niet leeg zijn: maximaal 32 KiB UTF-8 documentdata en 2.000 teken vraag. Ontbrekende bestanden, lege/EOF vragen en te grote invoer falen voor het inference proces.

## Tutorial 4: Verantwoorde AI

Bron: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

De zes probes behandelen schadelijke instructies, haatzaaien, privacy, medische verkeerde informatie, illegale inhoud, en een onschadelijke verantwoord AI-vraag. Het programma observeert de respons in plaats van aan te nemen dat elke probe een filter moet activeren.

| Uitkomst | Bewijs |
| --- | --- |
| `FILTERED` | Een expliciete `content_filter` / `ResponsibleAIPolicyViolation` foutcode, of een completion `content_filter` finish reden |
| `REFUSED` | Een niet-lege gestructureerde `message.refusal` veld |
| `POSSIBLE_REFUSAL` | Een openingstekst met weigering in gewone tekst; een heuristiek die review vereist |
| `GENERATED` | Een voltooid niet-leeg antwoord; geen bewijs dat de inhoud veilig is |

Een gewone HTTP 400 is **geen** bewijs van filtering. Ongeldige parameters, authenticatiefouten, snelheidslimieten, serverfouten, onjuiste antwoorden en afgekapt resultaat falen de run in plaats van een valse veiligheidssucces te geven. Brede woorden zoals "schadelijke inhoud" in een onschadelijke verklaring tellen niet als weigering.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Verwacht zes categorieresultaten en een samenvatting dat de observaties geen veiligheidscertificering zijn. Elke probe heeft een limiet van 300 tokens. Beoordeel onverwachte generaties en mogelijke weigeringen handmatig; de onschadelijke vergelijking moet een substantiële verantwoord AI-verklaring opleveren. Geen stdin vereist.

## Veelvoorkomende patronen in voorbeelden

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) centraliseert eindpuntnormalisatie, implementatie-overrides, sleutelloze authenticatie en chatopties:

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

De tokenleverancier ververst toegangstokens naar behoefte. Log geen tokens en vervang dit niet door een API-sleutel. Elk programma hergebruikt zijn client en sluit die af in `finally` of via een eigen `AutoCloseable`-wrapper; de SDK's `OpenAIClient` zelf is niet `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) vereist een afgerond, niet-leeg tekstueel antwoord. Lege keuzes, weigeringen, filters en afgekapt antwoorden worden niet stilzwijgend als succes weergegeven. Het verantwoord-AI voorbeeld behandelt verwachte filter-/weigeringuitkomsten expliciet. Ongedefinieerde fouten geven de Java/Maven-proces een niet-nul exitcode.

**Automatische SDK-herhalingen zijn uitgeschakeld** om het aantal verzoeken voorspelbaar te houden op gedeelde lage RPM-implementaties. Elke inference-request heeft een timeout van 60 seconden. Token-acquisitie kan extra tijd kosten. Applicatieniveau planning moet quota respecteren; voer een mislukt betaald verzoek niet blindelings opnieuw uit.

## Unit-tests

Vanuit de examples-map:

```powershell
mvn -B -ntp clean test
```

De testtransportlaag vervangt volledig de SDK HTTP-laag, vangt daadwerkelijke geserialiseerde request bodies op en levert geplaatste antwoorden. Het opent geen sockets, verkrijgt geen Azure-tokens en faalt bij onverwachte verzoeken. Deze tests valideren applicatiegedrag en SDK-protocol, niet live modelkwaliteit of implementatiebeschikbaarheid.

| Test suite | Dekking |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Eindpuntnormalisatie/-afwijzing, implementatie-overrides, redenerings- en tokenopties |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Elke completieworkflow, berichtgeschiedenis, trimmen van complete beurten, EOF, mislukkingen |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Tool-schema's, getypte argumenten, rekenkunde, ID's, meerdere tool-resultaten, mislukte follow-ups |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Bestandsopzoeking, UTF-8, limieten qua grootte, grounding payload, invoer- en API-fouten |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Alle zes probes, expliciete filters, weigeringclassificatie, gewone 400- en andere fouten |

Voor een suite, gebruik `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Gedeelde fixtures bevinden zich in [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sequentiële live verificatie

Live calls zijn apart van unit-tests. Gebruik de volgende commando's **individueel**, vanaf de repository-root, alleen nadat referenties en implementatietoegang klaar zijn. Geen services of persistente processen nodig.

Voor een gedeelde **10 verzoeken/minuut** implementatie, reserveer voldoende quota voor het hele volgende programma vóór de start: 5, 4, 1 en dan 6 verzoeken. Sequentiële processen alleen garanderen geen naleving van snelheidslimiet. Coördineer de rollende minuut met alle andere aanvragers; plak de vier aanroepen niet als ongereguleerde batch.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Completions, meer beurten, en twee interactieve beurten:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Controleer alle drie de sectiekoppen, vijf antwoorden, een laatste interactieve antwoord waarin Ada wordt genoemd, `Goodbye!`, en exitcode 0. Budget: **5 verzoeken, maximaal 1.900 voltooiingstokens**. Voor een kleinere run, pipe alleen `exit`: 3 verzoeken / 900 tokens, maar dat oefent de interactieve inferentie niet.

**2. Beide workflows voor functie-aanroepen:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Controleer beide functienamen, gesimuleerde Seattle-weer, berekend resultaat 36, twee laatste antwoorden, en exitcode 0. Budget: **4 verzoeken, maximaal 1.200 voltooiingstokens**.

**3. Document-gebaseerd antwoord:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Controleer het documentpad, een antwoord waarin Microsoft Entra ID wordt genoemd, en exitcode 0. Budget: **1 verzoek, maximaal 500 voltooiingstokens**. Het bestaande [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) is het enige vereiste invoerbestand. Een optionele tweede run die vraagt over een afwezig onderwerp moet afzien en voegt één verzoek / 500 tokens toe.

**4. Verantwoord-AI observaties:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Controleer zes categorieën en de observatiesamenvatting, beoordeel de gegenereerde inhoud, en eis exitcode 0 voor technische voltooiing. Een succesvolle procesafsluiting garandeert geen modelveiligheid. Budget: **6 verzoeken, maximaal 1.800 voltooiingstokens**.

**Totaal voor de vier opdrachten: 16 chatverzoeken en maximaal 5.400 voltooiingstokens**, plus invoertokens (inclusief herhaalde gesprekken en toolschema/geschiedenis). Er zijn nul embeddingsverzoeken. Werkelijk tokengebruik is modelafhankelijk en kan lager zijn, vooral bij gefilterde prompts. Dollar-kosten hangen af van de prijsstelling van de uitrol; er wordt geen vaste monetaire schatting gegeven. Alle verzoeklimieten veronderstellen geen handmatige herhalingen. Controleer `$LASTEXITCODE` onmiddellijk na elk commando; een niet-nul waarde betekent dat de run niet succesvol is voltooid.

## Problemen oplossen

- **Ontbrekend eindpunt / 401 / 403:** Stel het eindpunt in het startproces in, verifieer uw lokale Azure-aanmelding en resource-gebonden rol, en controleer op onbedoelde omgevingsoverschrijvingen van identiteit.
- **400 / 404:** Bevestig dat de uitrol bestaat en Chat Completions ondersteunt met redeneervermogen `none`. Gebruik de HTTPS resource root of `/openai/v1` URL, niet een legacy uitrol-URL. Gewone 400 fouten zijn technische fouten, geen veiligheidsblokkades.
- **429:** Coördineer de gedeelde RPM en tokenquota voordat u opnieuw probeert. De voorbeelden proberen bewust niet automatisch opnieuw.
- **`Incomplete chat response: length`:** De output heeft het voltooiingslimiet bereikt. Beoordeel het antwoord en prompt voordat u het limiet en het gedocumenteerde budget verhoogt; registreer een afgeknotte run niet als succesvol.
- **Bestand- of stdin-fouten:** Start vanuit een ondersteunde map of geef een expliciet documentpad op. Zorg voor een niet-lege lezer-vraag. Completions kunnen normaal eindigen bij EOF of `exit`.
- **Compilatiefouten:** Verifieer Java 21 of later, en voer dan `mvn -B -ntp clean test` uit. In PowerShell moet het gehele Maven-argument met een punt-eigenschap worden geciteerd, bijvoorbeeld `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Volgende stappen

Ga verder naar [Hoofdstuk 4: Praktische voorbeeldoefeningen](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Disclaimer**:
Dit document is vertaald met behulp van de AI vertaaldienst [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u er rekening mee te houden dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor kritieke informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->