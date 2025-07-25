<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:40:05+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "da"
}
-->
# Core Generative AI Techniques Tutorial

## Indholdsfortegnelse

- [Forudsætninger](../../../03-CoreGenerativeAITechniques)
- [Kom godt i gang](../../../03-CoreGenerativeAITechniques)
  - [Trin 1: Sæt din miljøvariabel](../../../03-CoreGenerativeAITechniques)
  - [Trin 2: Naviger til eksempelmappen](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: LLM-fuldførelser og chat](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Funktionskald](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: Ansvarlig AI](../../../03-CoreGenerativeAITechniques)
- [Fælles mønstre på tværs af eksempler](../../../03-CoreGenerativeAITechniques)
- [Næste skridt](../../../03-CoreGenerativeAITechniques)
- [Fejlfinding](../../../03-CoreGenerativeAITechniques)
  - [Almindelige problemer](../../../03-CoreGenerativeAITechniques)

## Oversigt

Denne tutorial giver praktiske eksempler på centrale generative AI-teknikker ved brug af Java og GitHub Models. Du vil lære at interagere med Large Language Models (LLMs), implementere funktionskald, bruge retrieval-augmented generation (RAG) og anvende ansvarlige AI-praksisser.

## Forudsætninger

Før du starter, skal du sikre dig, at du har:
- Java 21 eller nyere installeret
- Maven til afhængighedsstyring
- En GitHub-konto med en personlig adgangstoken (PAT)

## Kom godt i gang

### Trin 1: Sæt din miljøvariabel

Først skal du sætte din GitHub-token som en miljøvariabel. Denne token giver dig adgang til GitHub Models gratis.

**Windows (Command Prompt):**
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

### Trin 2: Naviger til eksempelmappen

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: LLM-fuldførelser og chat

**Fil:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Hvad dette eksempel lærer dig

Dette eksempel demonstrerer de grundlæggende mekanismer for interaktion med Large Language Models (LLMs) via OpenAI API, herunder klientinitialisering med GitHub Models, mønstre for meddelelsesstruktur til system- og brugerprompter, styring af samtalestatus gennem akkumulering af meddelelseshistorik og parameterjustering for at kontrollere svarlængde og kreativitet.

### Centrale kodekoncepter

#### 1. Klientopsætning
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Dette opretter en forbindelse til GitHub Models ved hjælp af din token.

#### 2. Enkel fuldførelse
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

#### 3. Samtalememorering
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI husker tidligere meddelelser, hvis du inkluderer dem i efterfølgende forespørgsler.

### Kør eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Hvad der sker, når du kører det

1. **Enkel fuldførelse**: AI besvarer et Java-spørgsmål med vejledning fra systemprompten.
2. **Flertrinschat**: AI opretholder konteksten på tværs af flere spørgsmål.
3. **Interaktiv chat**: Du kan have en reel samtale med AI'en.

## Tutorial 2: Funktionskald

**Fil:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Hvad dette eksempel lærer dig

Funktionskald gør det muligt for AI-modeller at anmode om eksekvering af eksterne værktøjer og API'er via en struktureret protokol, hvor modellen analyserer naturlige sprogforespørgsler, bestemmer nødvendige funktionskald med passende parametre ved hjælp af JSON Schema-definitioner og behandler returnerede resultater for at generere kontekstuelle svar, mens den faktiske funktionseksekvering forbliver under udviklerens kontrol for sikkerhed og pålidelighed.

### Centrale kodekoncepter

#### 1. Funktionsdefinition
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

Dette fortæller AI, hvilke funktioner der er tilgængelige, og hvordan de skal bruges.

#### 2. Funktionseksekveringsflow
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

#### 3. Funktionsimplementering
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

### Kør eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Hvad der sker, når du kører det

1. **Vejrfunktion**: AI anmoder om vejroplysninger for Seattle, du leverer dem, AI formaterer et svar.
2. **Lommeregnerfunktion**: AI anmoder om en beregning (15% af 240), du udfører den, AI forklarer resultatet.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**Fil:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Hvad dette eksempel lærer dig

Retrieval-Augmented Generation (RAG) kombinerer informationssøgning med sprogproduktion ved at injicere eksternt dokumentindhold i AI-prompter, hvilket gør det muligt for modeller at give præcise svar baseret på specifikke videnskilder frem for potentielt forældede eller unøjagtige træningsdata, samtidig med at klare grænser mellem brugerforespørgsler og autoritative informationskilder opretholdes gennem strategisk promptdesign.

### Centrale kodekoncepter

#### 1. Dokumentindlæsning
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Kontekstindsprøjtning
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

De tre anførselstegn hjælper AI med at skelne mellem kontekst og spørgsmål.

#### 3. Sikker håndtering af svar
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Valider altid API-svar for at forhindre nedbrud.

### Kør eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Hvad der sker, når du kører det

1. Programmet indlæser `document.txt` (indeholder info om GitHub Models).
2. Du stiller et spørgsmål om dokumentet.
3. AI svarer kun baseret på dokumentets indhold, ikke dens generelle viden.

Prøv at spørge: "Hvad er GitHub Models?" vs "Hvordan er vejret?"

## Tutorial 4: Ansvarlig AI

**Fil:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Hvad dette eksempel lærer dig

Eksemplet på ansvarlig AI viser vigtigheden af at implementere sikkerhedsforanstaltninger i AI-applikationer. Det demonstrerer sikkerhedsfiltre, der opdager skadelige indholdskategorier, herunder hadefuld tale, chikane, selvskade, seksuelt indhold og vold, og viser, hvordan produktions-AI-applikationer bør håndtere overtrædelser af indholdspolitikker på en hensigtsmæssig måde gennem korrekt undtagelseshåndtering, brugerfeedbackmekanismer og fallback-svarstrategier.

### Centrale kodekoncepter

#### 1. Testramme for sikkerhed
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

#### 2. Testede sikkerhedskategorier
- Vold/skadeinstruktioner
- Hadefuld tale
- Krænkelse af privatliv
- Medicinsk misinformation
- Ulovlige aktiviteter

### Kør eksemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Hvad der sker, når du kører det

Programmet tester forskellige skadelige prompter og viser, hvordan AI-sikkerhedssystemet:
1. **Blokerer farlige forespørgsler** med HTTP 400-fejl.
2. **Tillader sikkert indhold** at blive genereret normalt.
3. **Beskytter brugere** mod skadelige AI-output.

## Fælles mønstre på tværs af eksempler

### Autentificeringsmønster
Alle eksempler bruger dette mønster til at autentificere med GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Fejlhåndteringsmønster
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Meddelelsesstrukturmønster
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Næste skridt

[Kapitel 04: Praktiske eksempler](../04-PracticalSamples/README.md)

## Fejlfinding

### Almindelige problemer

**"GITHUB_TOKEN ikke sat"**
- Sørg for, at du har sat miljøvariablen.
- Bekræft, at din token har `models:read`-tilladelse.

**"Ingen svar fra API"**
- Tjek din internetforbindelse.
- Bekræft, at din token er gyldig.
- Tjek, om du har nået grænserne for forespørgsler.

**Maven-kompilationsfejl**
- Sørg for, at du har Java 21 eller nyere.
- Kør `mvn clean compile` for at opdatere afhængigheder.

**Ansvarsfraskrivelse**:  
Dette dokument er blevet oversat ved hjælp af AI-oversættelsestjenesten [Co-op Translator](https://github.com/Azure/co-op-translator). Selvom vi bestræber os på nøjagtighed, skal du være opmærksom på, at automatiserede oversættelser kan indeholde fejl eller unøjagtigheder. Det originale dokument på dets oprindelige sprog bør betragtes som den autoritative kilde. For kritisk information anbefales professionel menneskelig oversættelse. Vi er ikke ansvarlige for eventuelle misforståelser eller fejltolkninger, der måtte opstå som følge af brugen af denne oversættelse.