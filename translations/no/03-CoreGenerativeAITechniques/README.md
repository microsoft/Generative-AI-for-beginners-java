<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:42:18+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "no"
}
-->
# Kjerne Generativ AI Teknikker Veiledning

## Innholdsfortegnelse

- [Forutsetninger](../../../03-CoreGenerativeAITechniques)
- [Komme i Gang](../../../03-CoreGenerativeAITechniques)
  - [Trinn 1: Sett Miljøvariabelen](../../../03-CoreGenerativeAITechniques)
  - [Trinn 2: Naviger til Eksempelmappen](../../../03-CoreGenerativeAITechniques)
- [Veiledning 1: LLM Fullføringer og Chat](../../../03-CoreGenerativeAITechniques)
- [Veiledning 2: Funksjonskall](../../../03-CoreGenerativeAITechniques)
- [Veiledning 3: RAG (Hentingsforsterket Generering)](../../../03-CoreGenerativeAITechniques)
- [Veiledning 4: Ansvarlig AI](../../../03-CoreGenerativeAITechniques)
- [Felles Mønstre på Tvers av Eksempler](../../../03-CoreGenerativeAITechniques)
- [Neste Steg](../../../03-CoreGenerativeAITechniques)
- [Feilsøking](../../../03-CoreGenerativeAITechniques)
  - [Vanlige Problemer](../../../03-CoreGenerativeAITechniques)

## Oversikt

Denne veiledningen gir praktiske eksempler på kjerne generativ AI-teknikker ved bruk av Java og GitHub-modeller. Du vil lære hvordan du interagerer med Store Språkmodeller (LLMs), implementerer funksjonskall, bruker hentingsforsterket generering (RAG), og anvender ansvarlige AI-praksiser.

## Forutsetninger

Før du starter, sørg for at du har:
- Java 21 eller nyere installert
- Maven for avhengighetsstyring
- En GitHub-konto med en personlig tilgangstoken (PAT)

## Komme i Gang

### Trinn 1: Sett Miljøvariabelen

Først må du sette GitHub-tokenet ditt som en miljøvariabel. Dette tokenet gir deg tilgang til GitHub-modeller gratis.

**Windows (Kommandolinje):**
```cmd
set GITHUB_TOKEN=your_github_token_here
```

**Windows (PowerShell):**
```powershell
$env:GITHUB_TOKEN="your_github_token_here"
```

**Linux/macOS:**
```bash
export GITHUB_TOKEN=your_github_token_here
```

### Trinn 2: Naviger til Eksempelmappen

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Veiledning 1: LLM Fullføringer og Chat

**Fil:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Hva Dette Eksemplet Lærer Deg

Dette eksemplet viser de grunnleggende mekanismene for interaksjon med Store Språkmodeller (LLM) via OpenAI API, inkludert klientinitialisering med GitHub-modeller, mønstre for meldingsstruktur for system- og brukerforespørsler, håndtering av samtalestatus gjennom akkumulering av meldingshistorikk, og parameterjustering for å kontrollere svarlengde og kreativitet.

### Viktige Kodekonsepter

#### 1. Klientoppsett
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Dette oppretter en tilkobling til GitHub-modeller ved bruk av tokenet ditt.

#### 2. Enkel Fullføring
```java
List<ChatRequestMessage> messages = List.of(
    // System message sets AI behavior
    new ChatRequestSystemMessage("You are a helpful Java expert."),
    // User message contains the actual question
    new ChatRequestUserMessage("Explain Java streams briefly.")
);

ChatCompletionsOptions options = new ChatCompletionsOptions(messages)
    .setModel("gpt-4o-mini")
    .setMaxTokens(200)      // Limit response length
    .setTemperature(0.7);   // Control creativity (0.0-1.0)
```

#### 3. Samtaleminne
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI husker tidligere meldinger bare hvis du inkluderer dem i påfølgende forespørsler.

### Kjør Eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Hva Skjer Når Du Kjører Det

1. **Enkel Fullføring**: AI svarer på et Java-spørsmål med veiledning fra systemprompt
2. **Flertrinns Chat**: AI opprettholder kontekst på tvers av flere spørsmål
3. **Interaktiv Chat**: Du kan ha en ekte samtale med AI

## Veiledning 2: Funksjonskall

**Fil:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Hva Dette Eksemplet Lærer Deg

Funksjonskall gjør det mulig for AI-modeller å be om utførelse av eksterne verktøy og API-er gjennom en strukturert protokoll der modellen analyserer forespørsler i naturlig språk, bestemmer nødvendige funksjonskall med riktige parametere ved bruk av JSON-skjemaer, og behandler returnerte resultater for å generere kontekstuelle svar, mens selve funksjonsutførelsen forblir under utviklerens kontroll for sikkerhet og pålitelighet.

### Viktige Kodekonsepter

#### 1. Funksjonsdefinisjon
```java
ChatCompletionsFunctionToolDefinitionFunction weatherFunction = 
    new ChatCompletionsFunctionToolDefinitionFunction("get_weather");
weatherFunction.setDescription("Get current weather information for a city");

// Define parameters using JSON Schema
weatherFunction.setParameters(BinaryData.fromString("""
    {
        "type": "object",
        "properties": {
            "city": {
                "type": "string",
                "description": "The city name"
            }
        },
        "required": ["city"]
    }
    """));
```

Dette forteller AI hvilke funksjoner som er tilgjengelige og hvordan de skal brukes.

#### 2. Flyt for Funksjonsutførelse
```java
// 1. AI requests a function call
if (choice.getFinishReason() == CompletionsFinishReason.TOOL_CALLS) {
    ChatCompletionsFunctionToolCall functionCall = ...;
    
    // 2. You execute the function
    String result = simulateWeatherFunction(functionCall.getFunction().getArguments());
    
    // 3. You give the result back to AI
    messages.add(new ChatRequestToolMessage(result, toolCall.getId()));
    
    // 4. AI provides final response with function result
    ChatCompletions finalResponse = client.getChatCompletions(MODEL, options);
}
```

#### 3. Funksjonsimplementasjon
```java
private static String simulateWeatherFunction(String arguments) {
    // Parse arguments and call real weather API
    // For demo, we return mock data
    return """
        {
            "city": "Seattle",
            "temperature": "22",
            "condition": "partly cloudy"
        }
        """;
}
```

### Kjør Eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Hva Skjer Når Du Kjører Det

1. **Værfunksjon**: AI ber om værdata for Seattle, du gir det, AI formaterer et svar
2. **Kalkulatorfunksjon**: AI ber om en beregning (15 % av 240), du utfører den, AI forklarer resultatet

## Veiledning 3: RAG (Hentingsforsterket Generering)

**Fil:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Hva Dette Eksemplet Lærer Deg

Hentingsforsterket Generering (RAG) kombinerer informasjonsinnhenting med språkgenerering ved å injisere kontekst fra eksterne dokumenter i AI-prompter, slik at modellene kan gi nøyaktige svar basert på spesifikke kunnskapskilder i stedet for potensielt utdaterte eller unøyaktige treningsdata, samtidig som klare grenser opprettholdes mellom brukerforespørsler og autoritative informasjonskilder gjennom strategisk promptutforming.

### Viktige Kodekonsepter

#### 1. Dokumentlasting
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Kontekstinjeksjon
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage(
        "Use only the CONTEXT to answer. If not in context, say you cannot find it."
    ),
    new ChatRequestUserMessage(
        "CONTEXT:\n\"\"\"\n" + doc + "\n\"\"\"\n\nQUESTION:\n" + question
    )
);
```

De triple anførselstegnene hjelper AI med å skille mellom kontekst og spørsmål.

#### 3. Sikker Håndtering av Svar
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Valider alltid API-svar for å unngå krasj.

### Kjør Eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Hva Skjer Når Du Kjører Det

1. Programmet laster inn `document.txt` (inneholder info om GitHub-modeller)
2. Du stiller et spørsmål om dokumentet
3. AI svarer kun basert på dokumentinnholdet, ikke generell kunnskap

Prøv å spørre: "Hva er GitHub-modeller?" vs "Hvordan er været?"

## Veiledning 4: Ansvarlig AI

**Fil:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Hva Dette Eksemplet Lærer Deg

Eksemplet på Ansvarlig AI viser viktigheten av å implementere sikkerhetstiltak i AI-applikasjoner. Det demonstrerer sikkerhetsfiltre som oppdager skadelige innholdskategorier, inkludert hatprat, trakassering, selvskading, seksuelt innhold og vold, og viser hvordan produksjons-AI-applikasjoner bør håndtere brudd på innholdspolicyer gjennom riktig unntakshåndtering, tilbakemeldingsmekanismer for brukere og strategier for alternative svar.

### Viktige Kodekonsepter

#### 1. Rammeverk for Sikkerhetstesting
```java
private void testPromptSafety(String prompt, String category) {
    try {
        // Attempt to get AI response
        ChatCompletions response = client.getChatCompletions(modelId, options);
        System.out.println("Response generated (content appears safe)");
        
    } catch (HttpResponseException e) {
        if (e.getResponse().getStatusCode() == 400) {
            System.out.println("[BLOCKED BY SAFETY FILTER]");
            System.out.println("This is GOOD - safety system working!");
        }
    }
}
```

#### 2. Testede Sikkerhetskategorier
- Vold/Skadeinstruksjoner
- Hatprat
- Brudd på personvern
- Medisinsk feilinformasjon
- Ulovlige aktiviteter

### Kjør Eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Hva Skjer Når Du Kjører Det

Programmet tester ulike skadelige forespørsler og viser hvordan AI-sikkerhetssystemet:
1. **Blokkerer farlige forespørsler** med HTTP 400-feil
2. **Tillater trygt innhold** å genereres normalt
3. **Beskytter brukere** mot skadelige AI-utganger

## Felles Mønstre på Tvers av Eksempler

### Autentiseringsmønster
Alle eksempler bruker dette mønsteret for å autentisere med GitHub-modeller:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Feilhåndteringsmønster
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Mønster for Meldingsstruktur
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Neste Steg

[Kapittel 04: Praktiske eksempler](../04-PracticalSamples/README.md)

## Feilsøking

### Vanlige Problemer

**"GITHUB_TOKEN ikke satt"**
- Sørg for at du har satt miljøvariabelen
- Verifiser at tokenet ditt har `models:read`-tillatelse

**"Ingen svar fra API"**
- Sjekk internettforbindelsen din
- Verifiser at tokenet ditt er gyldig
- Sjekk om du har nådd grenseverdier

**Maven-kompileringsfeil**
- Sørg for at du har Java 21 eller nyere
- Kjør `mvn clean compile` for å oppdatere avhengigheter

**Ansvarsfraskrivelse**:  
Dette dokumentet er oversatt ved hjelp av AI-oversettelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selv om vi streber etter nøyaktighet, vær oppmerksom på at automatiske oversettelser kan inneholde feil eller unøyaktigheter. Det originale dokumentet på sitt opprinnelige språk bør anses som den autoritative kilden. For kritisk informasjon anbefales profesjonell menneskelig oversettelse. Vi er ikke ansvarlige for eventuelle misforståelser eller feiltolkninger som oppstår ved bruk av denne oversettelsen.