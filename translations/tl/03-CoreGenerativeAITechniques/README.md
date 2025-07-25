<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:58:24+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "tl"
}
-->
# Core Generative AI Techniques Tutorial

## Talaan ng Nilalaman

- [Mga Kinakailangan](../../../03-CoreGenerativeAITechniques)
- [Pagsisimula](../../../03-CoreGenerativeAITechniques)
  - [Hakbang 1: Itakda ang Iyong Environment Variable](../../../03-CoreGenerativeAITechniques)
  - [Hakbang 2: Pumunta sa Direktoryo ng Mga Halimbawa](../../../03-CoreGenerativeAITechniques)
- [Tutorial 1: LLM Completions at Chat](../../../03-CoreGenerativeAITechniques)
- [Tutorial 2: Function Calling](../../../03-CoreGenerativeAITechniques)
- [Tutorial 3: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [Tutorial 4: Responsible AI](../../../03-CoreGenerativeAITechniques)
- [Karaniwang Pattern sa Mga Halimbawa](../../../03-CoreGenerativeAITechniques)
- [Susunod na Mga Hakbang](../../../03-CoreGenerativeAITechniques)
- [Pag-aayos ng Problema](../../../03-CoreGenerativeAITechniques)
  - [Karaniwang Isyu](../../../03-CoreGenerativeAITechniques)

## Pangkalahatang-ideya

Ang tutorial na ito ay nagbibigay ng mga praktikal na halimbawa ng mga pangunahing teknolohiya sa generative AI gamit ang Java at GitHub Models. Matututuhan mo kung paano makipag-ugnayan sa Large Language Models (LLMs), magpatupad ng function calling, gumamit ng retrieval-augmented generation (RAG), at mag-aplay ng mga responsableng kasanayan sa AI.

## Mga Kinakailangan

Bago magsimula, tiyakin na mayroon ka:
- Java 21 o mas mataas na naka-install
- Maven para sa pamamahala ng mga dependency
- Isang GitHub account na may personal access token (PAT)

## Pagsisimula

### Hakbang 1: Itakda ang Iyong Environment Variable

Una, kailangan mong itakda ang iyong GitHub token bilang isang environment variable. Ang token na ito ay nagbibigay-daan sa iyo na ma-access ang GitHub Models nang libre.

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

### Hakbang 2: Pumunta sa Direktoryo ng Mga Halimbawa

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Tutorial 1: LLM Completions at Chat

**File:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Ano ang Itinuturo ng Halimbawang Ito

Ipinapakita ng halimbawang ito ang mga pangunahing mekanismo ng pakikipag-ugnayan sa Large Language Model (LLM) gamit ang OpenAI API, kabilang ang pag-initialize ng client gamit ang GitHub Models, mga pattern ng istruktura ng mensahe para sa mga system at user prompt, pamamahala ng estado ng pag-uusap sa pamamagitan ng pag-iipon ng kasaysayan ng mensahe, at pag-tune ng mga parameter para kontrolin ang haba ng tugon at antas ng pagkamalikhain.

### Mga Pangunahing Konsepto sa Code

#### 1. Setup ng Client
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Nagbibigay ito ng koneksyon sa GitHub Models gamit ang iyong token.

#### 2. Simpleng Completion
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

#### 3. Memorya ng Pag-uusap
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Naalala ng AI ang mga nakaraang mensahe kung isasama mo ang mga ito sa mga susunod na kahilingan.

### Patakbuhin ang Halimbawa
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Ano ang Nangyayari Kapag Pinatakbo Mo Ito

1. **Simpleng Completion**: Sinasagot ng AI ang tanong sa Java gamit ang gabay ng system prompt
2. **Multi-turn Chat**: Pinapanatili ng AI ang konteksto sa maraming tanong
3. **Interactive Chat**: Maaari kang makipag-usap nang direkta sa AI

## Tutorial 2: Function Calling

**File:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Ano ang Itinuturo ng Halimbawang Ito

Ang function calling ay nagbibigay-daan sa mga AI model na humiling ng pagpapatupad ng mga panlabas na tool at API sa pamamagitan ng isang structured protocol kung saan sinusuri ng model ang mga natural language request, tinutukoy ang mga kinakailangang function call gamit ang tamang mga parameter sa pamamagitan ng JSON Schema definitions, at pinoproseso ang mga resulta upang makabuo ng mga tugon na may kaugnayan, habang ang aktwal na pagpapatupad ng function ay nananatili sa kontrol ng developer para sa seguridad at pagiging maaasahan.

### Mga Pangunahing Konsepto sa Code

#### 1. Kahulugan ng Function
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

Ipinapakita nito sa AI kung anong mga function ang magagamit at kung paano gamitin ang mga ito.

#### 2. Daloy ng Pagpapatupad ng Function
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

#### 3. Implementasyon ng Function
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

### Patakbuhin ang Halimbawa
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Ano ang Nangyayari Kapag Pinatakbo Mo Ito

1. **Weather Function**: Humihiling ang AI ng data ng panahon para sa Seattle, ibinibigay mo ito, at inaayos ng AI ang tugon
2. **Calculator Function**: Humihiling ang AI ng kalkulasyon (15% ng 240), kinukwenta mo ito, at ipinaliliwanag ng AI ang resulta

## Tutorial 3: RAG (Retrieval-Augmented Generation)

**File:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Ano ang Itinuturo ng Halimbawang Ito

Ang Retrieval-Augmented Generation (RAG) ay pinagsasama ang pagkuha ng impormasyon sa pagbuo ng wika sa pamamagitan ng pag-inject ng konteksto ng panlabas na dokumento sa mga AI prompt, na nagbibigay-daan sa mga model na magbigay ng tumpak na sagot batay sa mga partikular na mapagkukunan ng kaalaman sa halip na sa posibleng luma o hindi tamang data ng pagsasanay, habang pinapanatili ang malinaw na hangganan sa pagitan ng mga tanong ng user at mga mapagkakatiwalaang mapagkukunan ng impormasyon sa pamamagitan ng estratehikong prompt engineering.

### Mga Pangunahing Konsepto sa Code

#### 1. Pag-load ng Dokumento
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Pag-inject ng Konteksto
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

Ang triple quotes ay tumutulong sa AI na makilala ang konteksto mula sa tanong.

#### 3. Ligtas na Paghawak ng Tugon
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Palaging i-validate ang mga tugon ng API upang maiwasan ang pag-crash.

### Patakbuhin ang Halimbawa
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Ano ang Nangyayari Kapag Pinatakbo Mo Ito

1. Naglo-load ang programa ng `document.txt` (naglalaman ng impormasyon tungkol sa GitHub Models)
2. Magtatanong ka tungkol sa dokumento
3. Sasagot ang AI batay lamang sa nilalaman ng dokumento, hindi sa pangkalahatang kaalaman nito

Subukang magtanong: "Ano ang GitHub Models?" kumpara sa "Ano ang lagay ng panahon?"

## Tutorial 4: Responsible AI

**File:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Ano ang Itinuturo ng Halimbawang Ito

Ipinapakita ng Responsible AI na halimbawa ang kahalagahan ng pagpapatupad ng mga hakbang sa kaligtasan sa mga aplikasyon ng AI. Ipinapakita nito ang mga safety filter na nakakakita ng mga kategorya ng mapanganib na nilalaman kabilang ang hate speech, harassment, self-harm, sexual content, at violence, na nagpapakita kung paano dapat maayos na hawakan ng mga production AI application ang mga paglabag sa content policy sa pamamagitan ng tamang exception handling, mekanismo ng feedback ng user, at mga estratehiya sa fallback response.

### Mga Pangunahing Konsepto sa Code

#### 1. Framework para sa Pagsubok ng Kaligtasan
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

#### 2. Mga Kategorya ng Kaligtasan na Sinusubukan
- Mga tagubilin sa karahasan/pinsala
- Hate speech
- Paglabag sa privacy
- Maling impormasyon sa medikal
- Ilegal na aktibidad

### Patakbuhin ang Halimbawa
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Ano ang Nangyayari Kapag Pinatakbo Mo Ito

Ipinapakita ng programa ang iba't ibang mapanganib na prompt at kung paano:
1. **Binablock ang mapanganib na mga kahilingan** gamit ang HTTP 400 errors
2. **Pinapayagan ang ligtas na nilalaman** na mabuo nang normal
3. **Pinoprotektahan ang mga user** mula sa mapanganib na output ng AI

## Karaniwang Pattern sa Mga Halimbawa

### Pattern ng Authentication
Ginagamit ng lahat ng halimbawa ang pattern na ito para mag-authenticate sa GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Pattern ng Pag-aayos ng Error
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Pattern ng Istruktura ng Mensahe
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Susunod na Mga Hakbang

[Chapter 04: Practical samples](../04-PracticalSamples/README.md)

## Pag-aayos ng Problema

### Karaniwang Isyu

**"GITHUB_TOKEN not set"**
- Tiyaking naitakda mo ang environment variable
- Siguraduhing ang iyong token ay may `models:read` scope

**"No response from API"**
- Suriin ang iyong koneksyon sa internet
- Siguraduhing valid ang iyong token
- Tingnan kung naabot mo na ang rate limits

**Mga error sa Maven compilation**
- Siguraduhing mayroon kang Java 21 o mas mataas
- Patakbuhin ang `mvn clean compile` upang i-refresh ang mga dependency

**Paunawa**:  
Ang dokumentong ito ay isinalin gamit ang AI translation service na [Co-op Translator](https://github.com/Azure/co-op-translator). Bagama't sinisikap naming maging tumpak, pakitandaan na ang mga awtomatikong pagsasalin ay maaaring maglaman ng mga pagkakamali o hindi pagkakatugma. Ang orihinal na dokumento sa kanyang katutubong wika ang dapat ituring na opisyal na sanggunian. Para sa mahalagang impormasyon, inirerekomenda ang propesyonal na pagsasalin ng tao. Hindi kami mananagot sa anumang hindi pagkakaunawaan o maling interpretasyon na dulot ng paggamit ng pagsasaling ito.