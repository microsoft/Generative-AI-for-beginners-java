<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:06:16+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "cs"
}
-->
# Výukový program základních technik generativní AI

## Obsah

- [Předpoklady](../../../03-CoreGenerativeAITechniques)
- [Začínáme](../../../03-CoreGenerativeAITechniques)
  - [Krok 1: Nastavte proměnnou prostředí](../../../03-CoreGenerativeAITechniques)
  - [Krok 2: Přejděte do adresáře s příklady](../../../03-CoreGenerativeAITechniques)
- [Výukový program 1: LLM dokončování a chat](../../../03-CoreGenerativeAITechniques)
- [Výukový program 2: Volání funkcí](../../../03-CoreGenerativeAITechniques)
- [Výukový program 3: RAG (Generování s podporou vyhledávání)](../../../03-CoreGenerativeAITechniques)
- [Výukový program 4: Odpovědná AI](../../../03-CoreGenerativeAITechniques)
- [Společné vzory napříč příklady](../../../03-CoreGenerativeAITechniques)
- [Další kroky](../../../03-CoreGenerativeAITechniques)
- [Řešení problémů](../../../03-CoreGenerativeAITechniques)
  - [Časté problémy](../../../03-CoreGenerativeAITechniques)

## Přehled

Tento výukový program poskytuje praktické příklady základních technik generativní AI pomocí Javy a GitHub Models. Naučíte se, jak pracovat s velkými jazykovými modely (LLM), implementovat volání funkcí, používat generování s podporou vyhledávání (RAG) a aplikovat zásady odpovědné AI.

## Předpoklady

Než začnete, ujistěte se, že máte:
- Nainstalovanou Javu 21 nebo vyšší
- Maven pro správu závislostí
- GitHub účet s osobním přístupovým tokenem (PAT)

## Začínáme

### Krok 1: Nastavte proměnnou prostředí

Nejprve musíte nastavit svůj GitHub token jako proměnnou prostředí. Tento token vám umožní přístup k GitHub Models zdarma.

**Windows (Příkazový řádek):**
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

### Krok 2: Přejděte do adresáře s příklady

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Výukový program 1: LLM dokončování a chat

**Soubor:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Co vás tento příklad naučí

Tento příklad ukazuje základní principy interakce s velkými jazykovými modely (LLM) prostřednictvím OpenAI API, včetně inicializace klienta s GitHub Models, vzorů struktury zpráv pro systémové a uživatelské výzvy, správy stavu konverzace pomocí akumulace historie zpráv a ladění parametrů pro kontrolu délky odpovědí a úrovně kreativity.

### Klíčové koncepty kódu

#### 1. Nastavení klienta
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Tím vytvoříte připojení k GitHub Models pomocí vašeho tokenu.

#### 2. Jednoduché dokončení
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

#### 3. Paměť konverzace
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI si pamatuje předchozí zprávy pouze tehdy, pokud je zahrnete do následných požadavků.

### Spuštění příkladu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Co se stane, když jej spustíte

1. **Jednoduché dokončení**: AI odpoví na otázku o Javě s pomocí systémové výzvy
2. **Víceotáčkový chat**: AI udržuje kontext napříč více otázkami
3. **Interaktivní chat**: Můžete vést skutečnou konverzaci s AI

## Výukový program 2: Volání funkcí

**Soubor:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Co vás tento příklad naučí

Volání funkcí umožňuje modelům AI požadovat provedení externích nástrojů a API prostřednictvím strukturovaného protokolu, kde model analyzuje požadavky v přirozeném jazyce, určuje potřebné volání funkcí s odpovídajícími parametry pomocí definic JSON Schema a zpracovává vrácené výsledky pro generování kontextových odpovědí, přičemž samotné provedení funkcí zůstává pod kontrolou vývojáře kvůli bezpečnosti a spolehlivosti.

### Klíčové koncepty kódu

#### 1. Definice funkcí
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

Tím AI sdělíte, jaké funkce jsou dostupné a jak je používat.

#### 2. Tok provádění funkcí
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

#### 3. Implementace funkcí
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

### Spuštění příkladu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Co se stane, když jej spustíte

1. **Funkce počasí**: AI požádá o data o počasí v Seattlu, vy je poskytnete, AI vytvoří odpověď
2. **Funkce kalkulačky**: AI požádá o výpočet (15 % z 240), vy jej provedete, AI vysvětlí výsledek

## Výukový program 3: RAG (Generování s podporou vyhledávání)

**Soubor:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Co vás tento příklad naučí

Generování s podporou vyhledávání (RAG) kombinuje vyhledávání informací s generováním textu tím, že do výzev AI vkládá kontext z externích dokumentů, což modelům umožňuje poskytovat přesné odpovědi na základě konkrétních zdrojů znalostí, spíše než na potenciálně zastaralých nebo nepřesných tréninkových datech, přičemž udržuje jasné hranice mezi dotazy uživatele a autoritativními zdroji informací prostřednictvím strategického návrhu výzev.

### Klíčové koncepty kódu

#### 1. Načítání dokumentů
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Vkládání kontextu
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

Trojnásobné uvozovky pomáhají AI rozlišit mezi kontextem a otázkou.

#### 3. Bezpečné zpracování odpovědí
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Vždy ověřujte odpovědi API, abyste předešli pádům programu.

### Spuštění příkladu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Co se stane, když jej spustíte

1. Program načte `document.txt` (obsahuje informace o GitHub Models)
2. Položíte otázku týkající se dokumentu
3. AI odpoví pouze na základě obsahu dokumentu, nikoli na základě svých obecných znalostí

Zkuste se zeptat: "Co jsou GitHub Models?" vs "Jaké je počasí?"

## Výukový program 4: Odpovědná AI

**Soubor:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Co vás tento příklad naučí

Příklad odpovědné AI ukazuje důležitost implementace bezpečnostních opatření v aplikacích AI. Demonstruje bezpečnostní filtry, které detekují škodlivé kategorie obsahu, včetně nenávistných projevů, obtěžování, sebepoškozování, sexuálního obsahu a násilí, a ukazuje, jak by produkční aplikace AI měly elegantně zvládat porušení zásad obsahu prostřednictvím správného zpracování výjimek, mechanismů zpětné vazby uživatelů a strategií náhradních odpovědí.

### Klíčové koncepty kódu

#### 1. Rámec pro testování bezpečnosti
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

#### 2. Testované kategorie bezpečnosti
- Pokyny k násilí/ublížení
- Nenávistné projevy
- Porušení soukromí
- Lékařské dezinformace
- Nelegální aktivity

### Spuštění příkladu
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Co se stane, když jej spustíte

Program testuje různé škodlivé výzvy a ukazuje, jak systém bezpečnosti AI:
1. **Blokuje nebezpečné požadavky** pomocí chyb HTTP 400
2. **Povoluje bezpečný obsah** k normálnímu generování
3. **Chrání uživatele** před škodlivými výstupy AI

## Společné vzory napříč příklady

### Vzor autentizace
Všechny příklady používají tento vzor pro autentizaci s GitHub Models:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Vzor zpracování chyb
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Vzor struktury zpráv
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Další kroky

[Kap. 04: Praktické příklady](../04-PracticalSamples/README.md)

## Řešení problémů

### Časté problémy

**"GITHUB_TOKEN není nastaven"**
- Ujistěte se, že jste nastavili proměnnou prostředí
- Ověřte, že váš token má rozsah `models:read`

**"Žádná odpověď od API"**
- Zkontrolujte své internetové připojení
- Ověřte, že váš token je platný
- Zkontrolujte, zda jste nepřekročili limity

**Chyby při kompilaci Maven**
- Ujistěte se, že máte Javu 21 nebo vyšší
- Spusťte `mvn clean compile` pro obnovení závislostí

**Prohlášení:**  
Tento dokument byl přeložen pomocí služby pro automatizovaný překlad [Co-op Translator](https://github.com/Azure/co-op-translator). I když se snažíme o přesnost, mějte prosím na paměti, že automatizované překlady mohou obsahovat chyby nebo nepřesnosti. Původní dokument v jeho původním jazyce by měl být považován za autoritativní zdroj. Pro důležité informace doporučujeme profesionální lidský překlad. Neodpovídáme za žádná nedorozumění nebo nesprávné interpretace vyplývající z použití tohoto překladu.