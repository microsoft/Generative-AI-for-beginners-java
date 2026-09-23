# Kjerneopplæring i generative KI-teknikker

## Innholdsfortegnelse

- [Forutsetninger](#forutsetninger)
- [Komme i gang](#komme-i-gang)
- [Veiledning for modellvalg](#veiledning-for-modellvalg)
- [Opplæring 1: LLM fullføringer og chat](#opplæring-1-llm-fullføringer-og-chat)
- [Opplæring 2: Funksjonsanrop](#opplæring-2-funksjonsanrop)
- [Opplæring 3: RAG (Retrieval-Augmented Generation)](#opplæring-3-rag-retrieval-augmented-generation)
- [Opplæring 4: Ansvarlig AI](#opplæring-4-ansvarlig-ki)
- [Vanlige mønstre på tvers av eksempler](#vanlige-mønstre-på-tvers-av-eksempler)
- [Enhetstester](#enhetstester)
- [Sekvensiell sanntidsverifisering](#sekvensiell-sanntidsverifisering)
- [Feilsøking](#feilsøking)
- [Neste steg](#neste-steg)

## Oversikt

Fire frittstående Java-programmer demonstrerer chat, samtalehistorikk, funksjonsanrop, hel-dokument hent-basert generering (RAG) og ansvarlig KI-responsbehandling. Alle chat-forespørsler retter seg som standard mot **GPT-5.6 Luna med årsaksinnsats `none`**.

Disse eksemplene bruker den offisielle OpenAI Java SDK med Azure OpenAI sin v1-endepunkt, i tråd med [Microsofts SDK-veiledning](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages). Det eldre `azure-ai-openai`-pakken er ikke lenger en avhengighet. Chat Completions beholdes for å undervise eksisterende meldingsbaserte arbeidsflyter; se [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure) for andre API-alternativer.

## Forutsetninger

- Java 21 eller nyere og Maven 3.6.3 eller nyere.
- En Azure OpenAI chat-utplassering kalt `gpt-5.6-luna`, eller en overstyring med kompatible Chat Completions-innstillinger.
- En innlogget Azure-identitet med rollen **Cognitive Services OpenAI User** på ressursen. Lokal utvikling bruker din Azure CLI-innlogging; hostede applikasjoner kan bruke administrert identitet.
- Se [Kapittel 2](../02-SetupDevEnvironment/getting-started-azure-openai.md) for ressursoppsett og innloggingsinstruksjoner.

[Maven-konfigurasjonen](../../../03-CoreGenerativeAITechniques/examples/pom.xml) låser disse versjonene, kontrollert 2026-09-14:

| Komponent | Versjon | Formål |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | Offisielt Azure v1-kompatibelt klientbibliotek |
| `com.azure:azure-identity` | 1.18.6 | Nøkkelfri autentisering og token-oppfriskning |
| `net.objecthunter:exp4j` | 0.4.8 | Parsning av aritmetiske uttrykk uten kodeutførelse |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | Offline Jupiter enhetstester |
| Maven Compiler / Surefire / Exec | 3.16.0 / 3.6.0 / 3.6.4 | Java 21 kompilering, tester, kjørbare eksempler |

Kompilatoren bruker `--release 21`. Ingen Spring Boot, Spring AI eller LangChain4j-avhengighet er nødvendig for disse frittstående eksemplene.

## Komme i gang

Fra repo-roten, sett ressursendepunkt og valgfri utplasseringsoverstyring i din shell.

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

Tester krever verken Azure-legitimasjon eller et endepunkt. Maven leser ikke automatisk en miljøfil; sett variabler i shell som brukes til å starte sanntidseksempler. For IDE-kjøringer, verifiser miljøet gitt av din oppstartskonfigurasjon.

## Veiledning for modellvalg

| Miljøvariabel | Betydning | Standard |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure-ressursrot eller allerede normalisert `/openai/v1` URL | Påkrevd for sanntidskjøringer |
| `AZURE_OPENAI_DEPLOYMENT` | Chat-utplassering navn, ikke en modellversjon | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | Separat embedding-utplassering, ikke brukt av disse fire programmene | `text-embedding-3-small` |

Blank utplasseringsoverstyring bruker standardverdiene. Konfigurasjonen legger til `/openai/v1` nøyaktig én gang og avviser legitimasjoner, spørringsstrenger og eldre utplasseringsstier i endepunktet.

Hver chat-forespørsel setter eksplisitt `reasoningEffort(ReasoningEffort.NONE)` og `maxCompletionTokens(...)`. Ingen forespørsel setter `temperature`, `top_p`, eller den eldre completion-token-opsjonen. Dette inkluderer verktøyvalg og verktøyresultatetterfølgende. GPT-5.6 Chat Completions-funksjonsverktøy krever årsaksinnsats `none`; se [Microsofts chat-veiledning](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**Det finnes ikke streaming eller embedding-inngang i dette kapittelet.** Leser henter hele dokumentet, ikke vektorer. Hvis du utvider med embeddings, bruk en separat embedding-utplassering som `text-embedding-3-small`, aldri Luna.

## Opplæring 1: LLM fullføringer og chat

Kilde: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java).

Programmet kjører en enkel Java streams forklaring, en to-runde HashMap/TreeMap-samtale og interaktiv chat. Andre runde inkluderer første assistentrespons; hver interaktiv runde sender også sin tidligere samtale.

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` leverer utplassering og eksplisitt årsaksinnstilling. Interaktiv chat hopper over tomme linjer, avsluttes på `exit` eller EOF, og beholder systemmeldingen pluss ni fullførte bruker-/assistentrunder. Antall runder-trimming er et pedagogisk begrensning, ikke en eksakt token-budsjettgaranti.

Fra eksempelkatalogen:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Forvent tre første svar, deretter en `You:`-prompt. Hvert ikke-tomt interaktivt spørsmål legger til én forespørsel. Fullføringsgrenser er 200, 300, 400 og deretter 500 tokens per interaktiv runde.

## Opplæring 2: Funksjonsanrop

Kilde: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java).

SDK-en utleder JSON-skjemaer fra de annoterte `WeatherArguments` og `CalculationArguments` rekordene. Et påkrevd verktøyvalg gjør at hvert eksempel trener verktøyprotokollen i stedet for å akseptere en modells uhjelpne svar.

1. Send et spørsmål med tillatt verktøy, årsaksinnsats `none` og 300-tokens fullføringsgrense.
2. Krev `tool_calls` som sluttårsak, valider funksjonsnavn og anrops-IDer, og parse typet JSON-argumenter.
3. Utfør lokal funksjon. Modellen kjører ikke Java eller vilkårlig kode.
4. Legg til assistentens verktøysanropmelding én gang, etterfulgt av hvert resultat med samsvarende `tool_call_id`.
5. Send én siste 300-token forespørsel uten verktøy og krev et fullført, ikke-tomt svar.

`get_weather` returnerer **simulert**, ikke reell, værdata. Den respekterer byen og konverterer eksempelverdien 22 grader Celsius til Fahrenheit når forespurt. `calculate` evaluerer det gitte uttrykket via exp4j, støtter formater som `15% av 240` og `2 + 3 * 4`, og avviser tomme, for store, ugyldige eller ikke-finitte kalkulasjoner. Den bruker flyttallsaritmetikk, ikke økonomisk desimalpresisjon.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Forvent `Function: get_weather`, simulert Seattle-vær, `Function: calculate`, `Function result: 36` og de to siste svarene. Ingen stdin eller eksterne vær-legitimasjoner kreves. En vellykket kjøring bruker nøyaktig fire chat-forespørsler.

## Opplæring 3: RAG (Retrieval-Augmented Generation)

Kilde: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java). Inndata: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt).

Dette introduksjons-RAG-eksempelet henter ett helt UTF-8-dokument og inkluderer det i brukermeldingen med spørsmålet. En separat systemmelding instruerer modellen om å betrakte dokumentinnhold som upålitelig data og svare kun fra denne konteksten. Hvis dokumentet ikke inneholder svaret, er forespurt respons: `Jeg kan ikke finne den informasjonen i det oppgitte dokumentet.`

Forankring kan redusere hallusinasjoner, men verken avgrensere eller systeminstruksjoner garanterer nøyaktighet eller forhindrer alle promptinjeksjoner. Gjennomgå svarene i sanntid. Produksjons-RAG legger normalt til oppdeling, henting, sitater, tilgangskontroll og evaluering.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

Skriv inn ett spørsmål, for eksempel `Hvilken autentiseringsmetode beskriver dokumentet?`. Forvent et svar som nevner Microsoft Entra ID. Programmet avslutter etter én chat-forespørsel med 500-tokens fullføringsgrense.

Standard filoppslag fungerer fra repo-rot, kapittelkatalog eller eksempelkatalog. En eksplisitt sti støttes også:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

Inndata må ikke være tomme: maksimum 32 KiB UTF-8 dokumentdata og 2 000 spørsmålstegn. Manglende filer, tomme/EOF-spørsmål og for store inndata feiler før inferens.

## Opplæring 4: Ansvarlig KI

Kilde: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java).

De seks prøvene dekker skadelige instruksjoner, hatefulle ytringer, personvern, medisinsk feilinformasjon, ulovlig innhold og et uskyldig ansvarlig KI-spørsmål. Programmet observerer responsen i stedet for å anta at hver prøve må utløse et filter.

| Utfallet | Bevis |
| --- | --- |
| `FILTERED` | En eksplisitt `content_filter` / `ResponsibleAIPolicyViolation` feilkode, eller en fullførings `content_filter` sluttårsak |
| `REFUSED` | Et ikke-tomt strukturert `message.refusal` felt |
| `POSSIBLE_REFUSAL` | En åpnende nektelsesfrase i vanlig tekst; en heuristikk som krever gjennomgang |
| `GENERATED` | Et fullført ikke-tomt svar; ikke bevis på at innholdet er trygt |

En vanlig HTTP 400 er **ikke** bevis for filtrering. Ugyldige parametere, autentiseringsfeil, grenseverdier, serverfeil, feilformede svar og avkortet utdata feiler kjøringen i stedet for å gi falsk sikkerhetssuksess. Vide begreper som «skadelig innhold» i en uskyldig forklaring teller ikke som nektelse.

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Forvent seks kategoriutfall og et sammendrag som sier at observasjonene ikke utgjør en sikkerhetssertifisering. Hver prøve har 300-tokens fullføringsgrense. Gjennomgå uventede genereringer og mulige nektelser manuelt; den uskyldige sammenligningen bør gi en substansiell ansvarlig KI-forklaring. Ingen stdin kreves.

## Vanlige mønstre på tvers av eksempler

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) sentraliserer endepunktsnormalisering, utplasseringsoverstyringer, nøkkelfri autentisering og chat-alternativer:

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

Token-leverandøren oppfrisker tilgangstokener ved behov. Logg ikke tokens eller erstatt dette med en API-nøkkel. Hvert program gjenbruker sin klient og lukker den i `finally` eller gjennom sin egen `AutoCloseable` wrapper; SDK-ens `OpenAIClient` er selv ikke `AutoCloseable`.

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) krever et fullført, ikke-tomt tekstsvar. Tomme valg, nektelser, filtre og avkortede svar printes ikke stille som suksess. Ansvarlig-KI eksempelet håndterer forventede filter-/nektelsesutfall eksplisitt. Uhåndterte feil gir Java/Maven prosessen en ulik null avslutningskode.

**Automatiske SDK-omprøver er deaktivert** for å holde antall forespørsler forutsigbart på delte lav-RPM-utplasseringer. Hver inferensforespørsel har 60 sekunders tidsavbrudd. Token-innhenting kan ta ekstra tid. Applikasjonsnivå planlegging må følge kvoter; ikke kjør blint en mislykket betalt forespørsel på nytt.

## Enhetstester

Fra eksempelkatalogen:

```powershell
mvn -B -ntp clean test
```

Testtransporten erstatter SDK HTTP-laget fullstendig, fanger faktiske serialiserte forespørselskropper, og leverer køede responser. Den åpner ingen sokler, skaffer ingen Azure-tokener, og feiler på uventede forespørsler. Disse testene validerer applikasjonsatferd og SDK-protokoll, ikke live-modellkvalitet eller utplasserings tilgjengelighet.

| Testpakke | Dekning |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | Endepunktsnormalisering/avvisning, utplasseringsoverstyringer, årsaks- og token-opsjoner |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | Hver fullførings arbeidsflyt, meldingshistorikk, komplett-rundek trimming, EOF, feil |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | Verktøyskjemaer, typete argumenter, aritmetikk, IDer, flere verktøyresultater, mislykkede etterfølger |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | Filoppslag, UTF-8, størrelsesbegrensninger, forankringsdata, inndata- og API-feil |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | Alle seks prøver, eksplisitte filtre, nektelsesklassifisering, vanlig 400 og andre feil |

For en pakke, bruk `mvn -B -ntp test "-Dtest=FunctionsAppTest"`. Delte fiksere bor i [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java).

## Sekvensiell sanntidsverifisering

Sanntidskall er adskilt fra enhetstester. Bruk følgende kommandoer **enkeltvis**, fra repo-roten, først etter at legitimasjon og utplasseringsadgang er klar. Ingen tjenester eller vedvarende prosesser er nødvendig.

For en delt **10 forespørsler/minutt** utplassering, reserver nok kvote for hele neste program før du starter det: 5, 4, 1, og deretter 6 forespørsler. Sekvensielle prosesser alene garanterer ikke overholdelse av ratebegrensning. Koordiner rullende minutt med alle andre brukere; lim ikke inn de fire kallene som en ubalansert batch.

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**1. Fullføringer, fler-runde og to interaktive runder:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

Sjekk alle tre seksjonstitlene, fem svar, et siste interaktivt svar som minner om Ada, `Goodbye!`, og exit-kode 0. Budsjett: **5 forespørsler, maksimalt 1 900 fullføringstokener**. For en mindre kjøring, pipe kun `exit`: 3 forespørsler / 900 tokens, men det tester ikke interaktiv inferens.

**2. Begge funksjons-kall arbeidsflyter:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

Sjekk begge funksjonsnavnene, simulert Seattle-vær, beregnet resultat 36, to endelige svar, og exit-kode 0. Budsjett: **4 forespørsler, maksimalt 1 200 fullføringstokener**.

**3. Dokumentbasert svar:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

Sjekk dokumentstien, et svar som nevner Microsoft Entra ID, og exit-kode 0. Budsjett: **1 forespørsel, maksimalt 500 fullføringstokener**. Den eksisterende [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) er den eneste nødvendige inndatafilen. En valgfri andre kjøring som spør om et fraværende tema bør avstå og legger til én forespørsel / 500 tokens.

**4. Observasjoner om ansvarlig KI:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

Sjekk seks kategorier og observasjonssammendraget, gjennomgå generert innhold, og krev exit-kode 0 for teknisk fullføring. En vellykket prosess-avslutning bekrefter ikke modellens sikkerhet. Budsjett: **6 forespørsler, maksimalt 1 800 fullføringstokener**.

**Totalt for de fire kommandoene: 16 chat-forespørsler og maksimalt 5 400 fullføringstokener**, pluss inndatatokens (inkludert gjentatt samtale og verktøyskjema/historikk). Det er null embedding-forespørsler. Faktisk tokenbruk avhenger av modellen og kan være lavere, spesielt for filtrerte prompt. Kostnaden i dollar avhenger av distribusjonsprising; ingen fast pengeberegning er antydet. Alle forespørselsgrenser forutsetter ingen manuelle gjenkjøringer. Inspiser `$LASTEXITCODE` umiddelbart etter hver kommando; et annet enn null betyr at kjøringen ikke fullførte vellykket.

## Feilsøking

- **Manglende endepunkt / 401 / 403:** Sett endepunktet i oppstartsprosessen, verifiser din lokale Azure-pålogging og ressursavgrensede rolle, og sjekk for utilsiktede overstyringer av identitetsmiljø.
- **400 / 404:** Bekreft at distribusjonen eksisterer og støtter Chat Completions med resonneringsinnsats `none`. Bruk HTTPS ressursrot eller `/openai/v1`-URL, ikke en eldre distribusjons-URL. Vanlige 400-feil er tekniske feil, ikke sikkerhetsblokker.
- **429:** Koordiner den delte RPM- og tokenkvoten før du prøver igjen. Eksemplene gjør med vilje ikke automatisk gjenforsøk.
- **`Ufullstendig chat-respons: lengde`:** Utdata nådde fullføringsgrensen. Gå gjennom svaret og prompt før du øker grensen og det dokumenterte budsjettet; ikke registrer en forkortet kjøring som vellykket.
- **Fil- eller stdin-feil:** Start fra en støttet mappe eller oppgi en eksplisitt dokumentsti. Gi et ikke-tomt leserspørsmål. Fullføringer kan avsluttes normalt på EOF eller `exit`.
- **Kompileringsfeil:** Verifiser Java 21 eller nyere, kjør deretter `mvn -B -ntp clean test`. I PowerShell, sett hermetegn rundt hele Maven-argumentet som inneholder en punktum-egenskap, for eksempel `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`.

## Neste steg

Fortsett til [Kapittel 4: Praktiske eksempler](../04-PracticalSamples/README.md).

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**Ansvarsfraskrivelse**:
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det opprinnelige dokumentet på originalspråket skal betraktes som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.
<!-- CO-OP TRANSLATOR DISCLAIMER END -->