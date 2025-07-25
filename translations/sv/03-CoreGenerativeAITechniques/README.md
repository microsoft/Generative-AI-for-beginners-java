<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:37:55+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "sv"
}
-->
# Core Generative AI Techniques Tutorial

## Innehållsförteckning

- [Förutsättningar](../../../03-CoreGenerativeAITechniques)
- [Komma igång](../../../03-CoreGenerativeAITechniques)
  - [Steg 1: Ställ in din miljövariabel](../../../03-CoreGenerativeAITechniques)
  - [Steg 2: Navigera till exempelmappen](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: LLM-kompletteringar och chatt](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Funktionsanrop](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: Ansvarsfull AI](../../../03-CoreGenerativeAITechniques)
- [Vanliga mönster i exemplen](../../../03-CoreGenerativeAITechniques)
- [Nästa steg](../../../03-CoreGenerativeAITechniques)
- [Felsökning](../../../03-CoreGenerativeAITechniques)
  - [Vanliga problem](../../../03-CoreGenerativeAITechniques)

## Översikt

Den här guiden ger praktiska exempel på kärntekniker inom generativ AI med Java och GitHub Models. Du kommer att lära dig att interagera med Large Language Models (LLMs), implementera funktionsanrop, använda retrieval-augmented generation (RAG) och tillämpa ansvarsfulla AI-principer.

## Förutsättningar

Innan du börjar, se till att du har:
- Java 21 eller senare installerat
- Maven för att hantera beroenden
- Ett GitHub-konto med en personlig åtkomsttoken (PAT)

## Komma igång

### Steg 1: Ställ in din miljövariabel

Först måste du ställa in din GitHub-token som en miljövariabel. Denna token ger dig tillgång till GitHub Models gratis.

**Windows (Kommandotolken):**
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

### Steg 2: Navigera till exempelmappen

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: LLM-kompletteringar och chatt

**Fil:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Vad detta exempel lär ut

Detta exempel visar de grundläggande mekanismerna för interaktion med Large Language Models (LLMs) via OpenAI API, inklusive klientinitialisering med GitHub Models, mönster för meddelandestruktur för system- och användaruppmaningar, hantering av konversationshistorik genom ackumulering av meddelanden och parameterjustering för att kontrollera svarslängd och kreativitet.

### Viktiga kodkoncept

#### 1. Klientinställning
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Detta skapar en anslutning till GitHub Models med din token.

#### 2. Enkel komplettering
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

#### 3. Konversationsminne
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI kommer ihåg tidigare meddelanden endast om du inkluderar dem i efterföljande förfrågningar.

### Kör exemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Vad händer när du kör det

1. **Enkel komplettering**: AI svarar på en Java-fråga med vägledning från systemuppmaningen.
2. **Fleromgångschatt**: AI behåller kontexten över flera frågor.
3. **Interaktiv chatt**: Du kan ha en riktig konversation med AI.

## Tutorial 2: Funktionsanrop

**Fil:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Vad detta exempel lär ut

Funktionsanrop gör det möjligt för AI-modeller att begära exekvering av externa verktyg och API:er genom ett strukturerat protokoll där modellen analyserar naturliga språkförfrågningar, bestämmer nödvändiga funktionsanrop med lämpliga parametrar med hjälp av JSON Schema-definitioner och bearbetar returnerade resultat för att generera kontextuella svar, medan den faktiska funktionskörningen förblir under utvecklarens kontroll för säkerhet och tillförlitlighet.

### Viktiga kodkoncept

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

Detta berättar för AI vilka funktioner som är tillgängliga och hur de ska användas.

#### 2. Flöde för funktionskörning
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

### Kör exemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Vad händer när du kör det

1. **Väderfunktion**: AI begär väderdata för Seattle, du tillhandahåller det, AI formaterar ett svar.
2. **Kalkylatorfunktion**: AI begär en beräkning (15 % av 240), du utför den, AI förklarar resultatet.

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**Fil:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Vad detta exempel lär ut

Retrieval-Augmented Generation (RAG) kombinerar informationssökning med språkgenerering genom att injicera extern dokumentkontext i AI-uppmaningar, vilket gör det möjligt för modeller att ge korrekta svar baserade på specifika kunskapskällor snarare än potentiellt föråldrad eller felaktig träningsdata, samtidigt som tydliga gränser mellan användarfrågor och auktoritativa informationskällor upprätthålls genom strategisk uppmaningsdesign.

### Viktiga kodkoncept

#### 1. Dokumentladdning
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Kontextinjektion
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

De tre citattecknen hjälper AI att skilja mellan kontext och fråga.

#### 3. Säker hantering av svar
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Validera alltid API-svar för att undvika krascher.

### Kör exemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Vad händer när du kör det

1. Programmet laddar `document.txt` (innehåller information om GitHub Models).
2. Du ställer en fråga om dokumentet.
3. AI svarar endast baserat på dokumentets innehåll, inte sin allmänna kunskap.

Prova att fråga: "Vad är GitHub Models?" jämfört med "Hur är vädret?"

## Tutorial 4: Ansvarsfull AI

**Fil:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Vad detta exempel lär ut

Exemplet Ansvarsfull AI visar vikten av att implementera säkerhetsåtgärder i AI-applikationer. Det demonstrerar säkerhetsfilter som upptäcker skadliga innehållskategorier inklusive hatpropaganda, trakasserier, självskada, sexuellt innehåll och våld, och visar hur produktions-AI-applikationer bör hantera överträdelser av innehållspolicy på ett smidigt sätt genom korrekt undantagshantering, användarfeedbackmekanismer och fallback-strategier.

### Viktiga kodkoncept

#### 1. Ramverk för säkerhetstestning
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

#### 2. Testade säkerhetskategorier
- Våld/skadliga instruktioner
- Hatpropaganda
- Integritetskränkningar
- Medicinsk desinformation
- Olagliga aktiviteter

### Kör exemplet
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Vad händer när du kör det

Programmet testar olika skadliga uppmaningar och visar hur AI-säkerhetssystemet:
1. **Blockerar farliga förfrågningar** med HTTP 400-fel.
2. **Tillåter säkert innehåll** att genereras normalt.
3. **Skyddar användare** från skadliga AI-utdata.

## Vanliga mönster i exemplen

### Autentiseringsmönster
Alla exempel använder detta mönster för att autentisera med GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Felhanteringsmönster
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Meddelandestruktursmönster
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Nästa steg

[Kapitel 04: Praktiska exempel](../04-PracticalSamples/README.md)

## Felsökning

### Vanliga problem

**"GITHUB_TOKEN not set"**
- Se till att du har ställt in miljövariabeln.
- Kontrollera att din token har `models:read`-behörighet.

**"No response from API"**
- Kontrollera din internetanslutning.
- Verifiera att din token är giltig.
- Kontrollera om du har nått gränsen för API-anrop.

**Maven-kompilationsfel**
- Se till att du har Java 21 eller senare.
- Kör `mvn clean compile` för att uppdatera beroenden.

**Ansvarsfriskrivning**:  
Detta dokument har översatts med hjälp av AI-översättningstjänsten [Co-op Translator](https://github.com/Azure/co-op-translator). Även om vi strävar efter noggrannhet, bör du vara medveten om att automatiserade översättningar kan innehålla fel eller felaktigheter. Det ursprungliga dokumentet på dess originalspråk bör betraktas som den auktoritativa källan. För kritisk information rekommenderas professionell mänsklig översättning. Vi ansvarar inte för eventuella missförstånd eller feltolkningar som uppstår vid användning av denna översättning.