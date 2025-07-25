<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:47:06+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "nl"
}
-->
# Core Generatieve AI Technieken Tutorial

## Inhoudsopgave

- [Vereisten](../../../03-CoreGenerativeAITechniques)
- [Aan de slag](../../../03-CoreGenerativeAITechniques)
  - [Stap 1: Stel je omgevingsvariabele in](../../../03-CoreGenerativeAITechniques)
  - [Stap 2: Navigeer naar de voorbeeldenmap](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: LLM-aanvullingen en chat](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Functieaanroepen](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: Verantwoordelijke AI](../../../03-CoreGenerativeAITechniques)
- [Algemene patronen in voorbeelden](../../../03-CoreGenerativeAITechniques)
- [Volgende stappen](../../../03-CoreGenerativeAITechniques)
- [Problemen oplossen](../../../03-CoreGenerativeAITechniques)
  - [Veelvoorkomende problemen](../../../03-CoreGenerativeAITechniques)

## Overzicht

Deze tutorial biedt praktische voorbeelden van kerntechnieken voor generatieve AI met behulp van Java en GitHub Models. Je leert hoe je kunt communiceren met Large Language Models (LLMs), functieaanroepen kunt implementeren, retrieval-augmented generation (RAG) kunt gebruiken en verantwoordelijke AI-praktijken kunt toepassen.

## Vereisten

Voordat je begint, zorg ervoor dat je:
- Java 21 of hoger hebt geïnstalleerd
- Maven gebruikt voor afhankelijkheidsbeheer
- Een GitHub-account hebt met een persoonlijke toegangstoken (PAT)

## Aan de slag

### Stap 1: Stel je omgevingsvariabele in

Eerst moet je je GitHub-token instellen als een omgevingsvariabele. Dit token geeft je toegang tot GitHub Models gratis.

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

### Stap 2: Navigeer naar de voorbeeldenmap

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: LLM-aanvullingen en chat

**Bestand:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Wat je leert in dit voorbeeld

Dit voorbeeld laat de kernmechanismen zien van interactie met Large Language Models (LLM) via de OpenAI API, inclusief het initialiseren van de client met GitHub Models, patroonstructuren voor systeem- en gebruikersprompts, het beheren van gespreksgeschiedenis door berichten op te slaan, en het afstemmen van parameters om de lengte en creativiteit van antwoorden te controleren.

### Belangrijke codeconcepten

#### 1. Clientconfiguratie
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Dit maakt een verbinding met GitHub Models met behulp van je token.

#### 2. Eenvoudige aanvulling
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

#### 3. Gespreksgeheugen
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

De AI onthoudt eerdere berichten alleen als je ze opneemt in volgende verzoeken.

### Voer het voorbeeld uit
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Wat gebeurt er als je het uitvoert

1. **Eenvoudige aanvulling**: AI beantwoordt een Java-vraag met richtlijnen van de systeemprompt.
2. **Meerdere beurten chat**: AI behoudt context over meerdere vragen.
3. **Interactieve chat**: Je kunt een echt gesprek voeren met de AI.

## Tutorial 2: Functieaanroepen

**Bestand:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Wat je leert in dit voorbeeld

Functieaanroepen stellen AI-modellen in staat om externe tools en API's aan te roepen via een gestructureerd protocol waarbij het model natuurlijke taalverzoeken analyseert, benodigde functieaanroepen bepaalt met de juiste parameters via JSON Schema-definities, en de geretourneerde resultaten verwerkt om contextuele antwoorden te genereren, terwijl de daadwerkelijke uitvoering van functies onder controle van de ontwikkelaar blijft voor veiligheid en betrouwbaarheid.

### Belangrijke codeconcepten

#### 1. Functiedefinitie
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

Dit vertelt de AI welke functies beschikbaar zijn en hoe ze te gebruiken.

#### 2. Functie-uitvoeringsstroom
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

#### 3. Functie-implementatie
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

### Voer het voorbeeld uit
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Wat gebeurt er als je het uitvoert

1. **Weerfunctie**: AI vraagt weergegevens voor Seattle, jij levert ze, AI formatteert een antwoord.
2. **Rekenfunctie**: AI vraagt een berekening (15% van 240), jij voert deze uit, AI legt het resultaat uit.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**Bestand:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Wat je leert in dit voorbeeld

Retrieval-Augmented Generation (RAG) combineert informatieophaling met taalgeneratie door externe documentcontext in AI-prompts te injecteren, waardoor modellen nauwkeurige antwoorden kunnen geven op basis van specifieke kennisbronnen in plaats van mogelijk verouderde of onnauwkeurige trainingsgegevens, terwijl duidelijke grenzen worden gehandhaafd tussen gebruikersvragen en gezaghebbende informatiebronnen via strategische prompt-engineering.

### Belangrijke codeconcepten

#### 1. Document laden
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Contextinjectie
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

De triple quotes helpen AI onderscheid te maken tussen context en vraag.

#### 3. Veilige responsverwerking
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Valideer altijd API-responses om crashes te voorkomen.

### Voer het voorbeeld uit
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Wat gebeurt er als je het uitvoert

1. Het programma laadt `document.txt` (bevat informatie over GitHub Models).
2. Je stelt een vraag over het document.
3. AI antwoordt alleen op basis van de inhoud van het document, niet op basis van algemene kennis.

Probeer te vragen: "Wat zijn GitHub Models?" versus "Hoe is het weer?"

## Tutorial 4: Verantwoordelijke AI

**Bestand:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Wat je leert in dit voorbeeld

Het voorbeeld van Verantwoordelijke AI benadrukt het belang van het implementeren van veiligheidsmaatregelen in AI-toepassingen. Het demonstreert veiligheidsfilters die schadelijke inhoudscategorieën detecteren, waaronder haatspraak, intimidatie, zelfbeschadiging, seksuele inhoud en geweld, en laat zien hoe productie-AI-toepassingen inhoudsbeleidsovertredingen op een elegante manier moeten afhandelen via correcte foutafhandeling, gebruikersfeedbackmechanismen en fallback-responsstrategieën.

### Belangrijke codeconcepten

#### 1. Veiligheidstestframework
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

#### 2. Geteste veiligheidscategorieën
- Geweld/instructies voor schade
- Haatspraak
- Privacyovertredingen
- Medische desinformatie
- Illegale activiteiten

### Voer het voorbeeld uit
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Wat gebeurt er als je het uitvoert

Het programma test verschillende schadelijke prompts en laat zien hoe het AI-veiligheidssysteem:
1. **Gevaarlijke verzoeken blokkeert** met HTTP 400-fouten.
2. **Veilige inhoud toestaat** om normaal te worden gegenereerd.
3. **Gebruikers beschermt** tegen schadelijke AI-uitvoer.

## Algemene patronen in voorbeelden

### Authenticatiepatroon
Alle voorbeelden gebruiken dit patroon om te authenticeren met GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Foutafhandelingspatroon
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Berichtstructuurpatroon
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Volgende stappen

[Hoofdstuk 04: Praktische voorbeelden](../04-PracticalSamples/README.md)

## Problemen oplossen

### Veelvoorkomende problemen

**"GITHUB_TOKEN niet ingesteld"**
- Zorg ervoor dat je de omgevingsvariabele hebt ingesteld.
- Controleer of je token de `models:read` scope heeft.

**"Geen reactie van API"**
- Controleer je internetverbinding.
- Controleer of je token geldig is.
- Controleer of je de limieten hebt bereikt.

**Maven compilatiefouten**
- Zorg ervoor dat je Java 21 of hoger hebt.
- Voer `mvn clean compile` uit om afhankelijkheden te vernieuwen.

**Disclaimer**:  
Dit document is vertaald met behulp van de AI-vertalingsservice [Co-op Translator](https://github.com/Azure/co-op-translator). Hoewel we streven naar nauwkeurigheid, dient u zich ervan bewust te zijn dat geautomatiseerde vertalingen fouten of onnauwkeurigheden kunnen bevatten. Het originele document in de oorspronkelijke taal moet worden beschouwd als de gezaghebbende bron. Voor cruciale informatie wordt professionele menselijke vertaling aanbevolen. Wij zijn niet aansprakelijk voor eventuele misverstanden of verkeerde interpretaties die voortvloeien uit het gebruik van deze vertaling.