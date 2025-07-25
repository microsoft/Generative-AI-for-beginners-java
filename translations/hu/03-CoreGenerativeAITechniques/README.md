<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:03:12+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "hu"
}
-->
# Generatív AI Alaptechnikák Oktatóanyag

## Tartalomjegyzék

- [Előfeltételek](../../../03-CoreGenerativeAITechniques)
- [Első lépések](../../../03-CoreGenerativeAITechniques)
  - [1. lépés: Állítsd be a környezeti változót](../../../03-CoreGenerativeAITechniques)
  - [2. lépés: Navigálj az Examples könyvtárba](../../../03-CoreGenerativeAITechniques)
- [1. oktatóanyag: LLM kiegészítések és chat](../../../03-CoreGenerativeAITechniques)
- [2. oktatóanyag: Funkcióhívás](../../../03-CoreGenerativeAITechniques)
- [3. oktatóanyag: RAG (Retrieval-Augmented Generation)](../../../03-CoreGenerativeAITechniques)
- [4. oktatóanyag: Felelős AI](../../../03-CoreGenerativeAITechniques)
- [Gyakori minták az példákban](../../../03-CoreGenerativeAITechniques)
- [Következő lépések](../../../03-CoreGenerativeAITechniques)
- [Hibakeresés](../../../03-CoreGenerativeAITechniques)
  - [Gyakori problémák](../../../03-CoreGenerativeAITechniques)

## Áttekintés

Ez az oktatóanyag gyakorlati példákon keresztül mutatja be a generatív AI alaptechnikáit Java és GitHub Modellek használatával. Megtanulhatod, hogyan lépj kapcsolatba Nagy Nyelvi Modellekkel (LLM-ekkel), hogyan valósíts meg funkcióhívásokat, hogyan használd a retrieval-augmented generation (RAG) technikát, és hogyan alkalmazz felelős AI gyakorlatokat.

## Előfeltételek

Mielőtt elkezdenéd, győződj meg róla, hogy rendelkezel az alábbiakkal:
- Telepített Java 21 vagy újabb verzió
- Maven a függőségkezeléshez
- GitHub fiók személyes hozzáférési tokennel (PAT)

## Első lépések

### 1. lépés: Állítsd be a környezeti változót

Először állítsd be a GitHub tokenedet környezeti változóként. Ez a token lehetővé teszi, hogy ingyenesen hozzáférj a GitHub Modellekhez.

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

### 2. lépés: Navigálj az Examples könyvtárba

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## 1. oktatóanyag: LLM kiegészítések és chat

**Fájl:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Mit tanít ez a példa?

Ez a példa bemutatja a Nagy Nyelvi Modellek (LLM-ek) interakciójának alapvető mechanikáját az OpenAI API-n keresztül, beleértve az ügyfél inicializálását GitHub Modellek használatával, üzenetstruktúrák mintázatait rendszer- és felhasználói promptokhoz, a beszélgetési állapot kezelését üzenetelőzmények felhalmozásával, valamint a válasz hosszának és kreativitási szintjének szabályozását paraméterek segítségével.

### Kulcsfontosságú kód koncepciók

#### 1. Ügyfél beállítása
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Ez létrehoz egy kapcsolatot a GitHub Modellekhez a tokened használatával.

#### 2. Egyszerű kiegészítés
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

#### 3. Beszélgetési memória
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

Az AI csak akkor emlékszik a korábbi üzenetekre, ha azokat belefoglalod a következő kérésekbe.

### Futtasd a példát
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Mi történik, amikor futtatod?

1. **Egyszerű kiegészítés**: Az AI válaszol egy Java kérdésre rendszer prompt iránymutatással.
2. **Többfordulós chat**: Az AI megőrzi a kontextust több kérdés során.
3. **Interaktív chat**: Valódi beszélgetést folytathatsz az AI-val.

## 2. oktatóanyag: Funkcióhívás

**Fájl:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Mit tanít ez a példa?

A funkcióhívás lehetővé teszi az AI modellek számára, hogy külső eszközök és API-k végrehajtását kérjék egy strukturált protokollon keresztül, ahol a modell elemzi a természetes nyelvű kéréseket, meghatározza a szükséges funkcióhívásokat megfelelő paraméterekkel JSON Schema definíciók alapján, és feldolgozza a visszakapott eredményeket, hogy kontextuális válaszokat generáljon, miközben a tényleges funkcióvégrehajtás a fejlesztő irányítása alatt marad a biztonság és megbízhatóság érdekében.

### Kulcsfontosságú kód koncepciók

#### 1. Funkció definíció
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

Ez megmondja az AI-nak, hogy milyen funkciók érhetők el és hogyan használhatja őket.

#### 2. Funkció végrehajtási folyamat
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

#### 3. Funkció implementáció
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

### Futtasd a példát
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Mi történik, amikor futtatod?

1. **Időjárás funkció**: Az AI időjárási adatokat kér Seattle-ről, te biztosítod, az AI formázza a választ.
2. **Számológép funkció**: Az AI számítást kér (240 15%-a), te elvégzed, az AI elmagyarázza az eredményt.

## 3. oktatóanyag: RAG (Retrieval-Augmented Generation)

**Fájl:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Mit tanít ez a példa?

A Retrieval-Augmented Generation (RAG) kombinálja az információkeresést a nyelvi generálással azáltal, hogy külső dokumentum kontextust injektál az AI promptokba, lehetővé téve a modellek számára, hogy pontos válaszokat adjanak specifikus tudásforrások alapján, ahelyett, hogy potenciálisan elavult vagy pontatlan tanítási adatokra támaszkodnának, miközben világos határokat tartanak fenn a felhasználói kérdések és az autoritatív információforrások között stratégiai prompttervezés révén.

### Kulcsfontosságú kód koncepciók

#### 1. Dokumentum betöltése
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Kontextus injektálása
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

A három idézőjel segít az AI-nak megkülönböztetni a kontextust és a kérdést.

#### 3. Biztonságos válaszkezelés
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Mindig ellenőrizd az API válaszokat, hogy elkerüld az összeomlásokat.

### Futtasd a példát
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Mi történik, amikor futtatod?

1. A program betölti a `document.txt` fájlt (GitHub Modellek információit tartalmazza).
2. Felteszel egy kérdést a dokumentummal kapcsolatban.
3. Az AI csak a dokumentum tartalma alapján válaszol, nem az általános tudása alapján.

Próbáld megkérdezni: "Mi az a GitHub Modellek?" vs "Milyen az időjárás?"

## 4. oktatóanyag: Felelős AI

**Fájl:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Mit tanít ez a példa?

A Felelős AI példa bemutatja, milyen fontos biztonsági intézkedéseket alkalmazni az AI alkalmazásokban. Bemutatja azokat a biztonsági szűrőket, amelyek káros tartalomkategóriákat észlelnek, például gyűlöletbeszédet, zaklatást, önkárosítást, szexuális tartalmat és erőszakot, megmutatva, hogyan kell a termelési AI alkalmazásoknak megfelelően kezelni a tartalompolitikai megsértéseket helyes kivételkezeléssel, felhasználói visszajelzési mechanizmusokkal és alternatív válaszstratégiákkal.

### Kulcsfontosságú kód koncepciók

#### 1. Biztonsági tesztelési keretrendszer
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

#### 2. Tesztelt biztonsági kategóriák
- Erőszak/ártalom utasítások
- Gyűlöletbeszéd
- Magánélet megsértése
- Orvosi félretájékoztatás
- Illegális tevékenységek

### Futtasd a példát
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Mi történik, amikor futtatod?

A program különböző káros promptokat tesztel, és megmutatja, hogyan működik az AI biztonsági rendszere:
1. **Blokkolja a veszélyes kéréseket** HTTP 400 hibákkal.
2. **Engedi a biztonságos tartalmat** normál generálásra.
3. **Védi a felhasználókat** a káros AI kimenetektől.

## Gyakori minták az példákban

### Hitelesítési minta
Minden példa ezt a mintát használja a GitHub Modellek hitelesítéséhez:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Hibakezelési minta
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Üzenetstruktúra minta
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Következő lépések

[4. fejezet: Gyakorlati példák](../04-PracticalSamples/README.md)

## Hibakeresés

### Gyakori problémák

**"GITHUB_TOKEN nincs beállítva"**
- Győződj meg róla, hogy beállítottad a környezeti változót.
- Ellenőrizd, hogy a tokened rendelkezik `models:read` jogosultsággal.

**"Nincs válasz az API-tól"**
- Ellenőrizd az internetkapcsolatodat.
- Győződj meg róla, hogy a tokened érvényes.
- Ellenőrizd, hogy nem lépted-e túl a sebességkorlátokat.

**Maven fordítási hibák**
- Győződj meg róla, hogy Java 21 vagy újabb verziót használsz.
- Futtasd a `mvn clean compile` parancsot a függőségek frissítéséhez.

**Felelősség kizárása**:  
Ez a dokumentum az AI fordítási szolgáltatás, a [Co-op Translator](https://github.com/Azure/co-op-translator) segítségével lett lefordítva. Bár törekszünk a pontosságra, kérjük, vegye figyelembe, hogy az automatikus fordítások hibákat vagy pontatlanságokat tartalmazhatnak. Az eredeti dokumentum az eredeti nyelvén tekintendő hiteles forrásnak. Fontos információk esetén javasolt professzionális emberi fordítást igénybe venni. Nem vállalunk felelősséget semmilyen félreértésért vagy téves értelmezésért, amely a fordítás használatából eredhet.