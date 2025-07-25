<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:00:41+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "sw"
}
-->
# Mafunzo ya Mbinu za Msingi za AI ya Kizazi

## Jedwali la Yaliyomo

- [Mahitaji ya Awali](../../../03-CoreGenerativeAITechniques)
- [Kuanza](../../../03-CoreGenerativeAITechniques)
  - [Hatua ya 1: Weka Kigezo cha Mazingira](../../../03-CoreGenerativeAITechniques)
  - [Hatua ya 2: Elekea kwenye Saraka ya Mifano](../../../03-CoreGenerativeAITechniques)
- [Mafunzo 1: Kukamilisha LLM na Gumzo](../../../03-CoreGenerativeAITechniques)
- [Mafunzo 2: Kuita Kazi](../../../03-CoreGenerativeAITechniques)
- [Mafunzo 3: RAG (Kizazi Kilichoongezewa Urejeshaji)](../../../03-CoreGenerativeAITechniques)
- [Mafunzo 4: AI ya Uwajibikaji](../../../03-CoreGenerativeAITechniques)
- [Mifumo ya Kawaida Kwenye Mifano](../../../03-CoreGenerativeAITechniques)
- [Hatua Zifuatazo](../../../03-CoreGenerativeAITechniques)
- [Kutatua Tatizo](../../../03-CoreGenerativeAITechniques)
  - [Masuala ya Kawaida](../../../03-CoreGenerativeAITechniques)

## Muhtasari

Mafunzo haya yanatoa mifano ya vitendo ya mbinu za msingi za AI ya kizazi kwa kutumia Java na GitHub Models. Utajifunza jinsi ya kuingiliana na Large Language Models (LLMs), kutekeleza kuita kazi, kutumia kizazi kilichoongezewa urejeshaji (RAG), na kutumia mazoea ya AI ya uwajibikaji.

## Mahitaji ya Awali

Kabla ya kuanza, hakikisha una:
- Java 21 au toleo la juu zaidi lililowekwa
- Maven kwa usimamizi wa utegemezi
- Akaunti ya GitHub yenye tokeni ya ufikiaji wa kibinafsi (PAT)

## Kuanza

### Hatua ya 1: Weka Kigezo cha Mazingira

Kwanza, unahitaji kuweka tokeni yako ya GitHub kama kigezo cha mazingira. Tokeni hii inakuwezesha kufikia GitHub Models bila malipo.

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

### Hatua ya 2: Elekea kwenye Saraka ya Mifano

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Mafunzo 1: Kukamilisha LLM na Gumzo

**Faili:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Kile Mafunzo Haya Yanakufundisha

Mfano huu unaonyesha mbinu za msingi za kuingiliana na Large Language Model (LLM) kupitia OpenAI API, ikiwa ni pamoja na usanidi wa mteja kwa kutumia GitHub Models, mifumo ya muundo wa ujumbe kwa maelekezo ya mfumo na mtumiaji, usimamizi wa hali ya mazungumzo kupitia mkusanyiko wa historia ya ujumbe, na kurekebisha vigezo ili kudhibiti urefu wa majibu na viwango vya ubunifu.

### Dhana Muhimu za Msimbo

#### 1. Usanidi wa Mteja
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Hii inaunda muunganisho na GitHub Models kwa kutumia tokeni yako.

#### 2. Kukamilisha Rahisi
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

#### 3. Kumbukumbu ya Mazungumzo
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI inakumbuka ujumbe wa awali tu ikiwa utajumuisha katika maombi yanayofuata.

### Endesha Mfano
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Kile Kinachotokea Unapokiendesha

1. **Kukamilisha Rahisi**: AI inajibu swali la Java kwa mwongozo wa maelekezo ya mfumo
2. **Gumzo la Zamu Nyingi**: AI inahifadhi muktadha katika maswali mengi
3. **Gumzo la Kuingiliana**: Unaweza kuwa na mazungumzo halisi na AI

## Mafunzo 2: Kuita Kazi

**Faili:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Kile Mafunzo Haya Yanakufundisha

Kuita kazi kunaruhusu mifano ya AI kuomba utekelezaji wa zana za nje na API kupitia itifaki iliyopangwa ambapo mfano unachambua maombi ya lugha asilia, kuamua kazi zinazohitajika na vigezo sahihi kwa kutumia ufafanuzi wa JSON Schema, na kuchakata matokeo yaliyorejeshwa ili kutoa majibu ya muktadha, huku utekelezaji halisi wa kazi ukibaki chini ya udhibiti wa msanidi programu kwa usalama na uaminifu.

### Dhana Muhimu za Msimbo

#### 1. Ufafanuzi wa Kazi
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

Hii inaiambia AI kazi zipi zinapatikana na jinsi ya kuzitumia.

#### 2. Mtiririko wa Utekelezaji wa Kazi
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

#### 3. Utekelezaji wa Kazi
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

### Endesha Mfano
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Kile Kinachotokea Unapokiendesha

1. **Kazi ya Hali ya Hewa**: AI inaomba data ya hali ya hewa ya Seattle, unaitoa, AI inaweka majibu
2. **Kazi ya Kikokotoo**: AI inaomba hesabu (15% ya 240), unaihesabu, AI inaelezea matokeo

## Mafunzo 3: RAG (Kizazi Kilichoongezewa Urejeshaji)

**Faili:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Kile Mafunzo Haya Yanakufundisha

Kizazi Kilichoongezewa Urejeshaji (RAG) kinachanganya urejeshaji wa taarifa na kizazi cha lugha kwa kuingiza muktadha wa hati za nje katika maelekezo ya AI, kuwezesha mifano kutoa majibu sahihi kulingana na vyanzo maalum vya maarifa badala ya data ya mafunzo ambayo inaweza kuwa ya zamani au isiyo sahihi, huku ikidumisha mipaka wazi kati ya maswali ya mtumiaji na vyanzo vya taarifa vya mamlaka kupitia uhandisi wa maelekezo wa kimkakati.

### Dhana Muhimu za Msimbo

#### 1. Upakiaji wa Hati
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Uingizaji wa Muktadha
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

Alama za nukuu tatu zinasaidia AI kutofautisha kati ya muktadha na swali.

#### 3. Ushughulikiaji Salama wa Majibu
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Daima thibitisha majibu ya API ili kuzuia hitilafu.

### Endesha Mfano
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Kile Kinachotokea Unapokiendesha

1. Programu inapakia `document.txt` (inayo taarifa kuhusu GitHub Models)
2. Unauliza swali kuhusu hati hiyo
3. AI inajibu kwa kutumia tu yaliyomo kwenye hati, si maarifa yake ya jumla

Jaribu kuuliza: "GitHub Models ni nini?" dhidi ya "Hali ya hewa ikoje?"

## Mafunzo 4: AI ya Uwajibikaji

**Faili:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Kile Mafunzo Haya Yanakufundisha

Mfano wa AI ya Uwajibikaji unaonyesha umuhimu wa kutekeleza hatua za usalama katika programu za AI. Unaonyesha vichujio vya usalama vinavyotambua makundi ya maudhui hatarishi ikiwa ni pamoja na hotuba ya chuki, unyanyasaji, kujidhuru, maudhui ya kingono, na vurugu, ikionyesha jinsi programu za AI za uzalishaji zinavyopaswa kushughulikia ukiukaji wa sera za maudhui kwa ustadi kupitia ushughulikiaji sahihi wa hitilafu, mifumo ya maoni ya mtumiaji, na mikakati ya majibu mbadala.

### Dhana Muhimu za Msimbo

#### 1. Mfumo wa Kupima Usalama
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

#### 2. Makundi ya Usalama Yanayopimwa
- Maelekezo ya vurugu/kuumiza
- Hotuba ya chuki
- Ukiukaji wa faragha
- Taarifa za uongo za matibabu
- Shughuli zisizo halali

### Endesha Mfano
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Kile Kinachotokea Unapokiendesha

Programu inajaribu maelekezo mbalimbali hatarishi na inaonyesha jinsi mfumo wa usalama wa AI:
1. **Unazuiya maombi hatarishi** kwa makosa ya HTTP 400
2. **Unaruhusu maudhui salama** kuzalishwa kawaida
3. **Unawalinda watumiaji** dhidi ya matokeo hatarishi ya AI

## Mifumo ya Kawaida Kwenye Mifano

### Mfumo wa Uthibitishaji
Mifano zote zinatumia mfumo huu kuthibitisha na GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Mfumo wa Ushughulikiaji wa Hitilafu
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Mfumo wa Muundo wa Ujumbe
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Hatua Zifuatazo

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## Kutatua Tatizo

### Masuala ya Kawaida

**"GITHUB_TOKEN not set"**
- Hakikisha umeweka kigezo cha mazingira
- Thibitisha tokeni yako ina `models:read` scope

**"No response from API"**
- Angalia muunganisho wako wa intaneti
- Thibitisha tokeni yako ni halali
- Angalia kama umefikia mipaka ya kiwango

**Hitilafu za mkusanyiko wa Maven**
- Hakikisha una Java 21 au toleo la juu zaidi
- Endesha `mvn clean compile` ili kusasisha utegemezi

**Kanusho**:  
Hati hii imetafsiriwa kwa kutumia huduma ya tafsiri ya AI [Co-op Translator](https://github.com/Azure/co-op-translator). Ingawa tunajitahidi kuhakikisha usahihi, tafadhali fahamu kuwa tafsiri za kiotomatiki zinaweza kuwa na makosa au kutokuwa sahihi. Hati ya asili katika lugha yake ya awali inapaswa kuzingatiwa kama chanzo cha mamlaka. Kwa taarifa muhimu, tafsiri ya kitaalamu ya binadamu inapendekezwa. Hatutawajibika kwa kutoelewana au tafsiri zisizo sahihi zinazotokana na matumizi ya tafsiri hii.