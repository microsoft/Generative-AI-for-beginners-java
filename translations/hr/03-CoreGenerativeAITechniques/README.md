<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T12:19:18+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "hr"
}
-->
# Vodič za Osnovne Tehnike Generativne Umjetne Inteligencije

## Sadržaj

- [Preduvjeti](../../../03-CoreGenerativeAITechniques)
- [Početak](../../../03-CoreGenerativeAITechniques)
  - [Korak 1: Postavite Varijablu Okruženja](../../../03-CoreGenerativeAITechniques)
  - [Korak 2: Navigirajte do Direktorija s Primjerima](../../../03-CoreGenerativeAITechniques)
- [Vodič 1: LLM Dovršavanja i Chat](../../../03-CoreGenerativeAITechniques)
- [Vodič 2: Pozivanje Funkcija](../../../03-CoreGenerativeAITechniques)
- [Vodič 3: RAG (Generacija s Poboljšanim Dohvaćanjem)](../../../03-CoreGenerativeAITechniques)
- [Vodič 4: Odgovorna Umjetna Inteligencija](../../../03-CoreGenerativeAITechniques)
- [Uobičajeni Obrasci u Primjerima](../../../03-CoreGenerativeAITechniques)
- [Sljedeći Koraci](../../../03-CoreGenerativeAITechniques)
- [Rješavanje Problema](../../../03-CoreGenerativeAITechniques)
  - [Uobičajeni Problemi](../../../03-CoreGenerativeAITechniques)

## Pregled

Ovaj vodič pruža praktične primjere osnovnih tehnika generativne umjetne inteligencije koristeći Java i GitHub modele. Naučit ćete kako komunicirati s Velikim Jezičnim Modelima (LLM), implementirati pozivanje funkcija, koristiti generaciju s poboljšanim dohvaćanjem (RAG) i primijeniti prakse odgovorne umjetne inteligencije.

## Preduvjeti

Prije početka, osigurajte da imate:
- Instaliranu Javu 21 ili noviju verziju
- Maven za upravljanje ovisnostima
- GitHub račun s osobnim pristupnim tokenom (PAT)

## Početak

### Korak 1: Postavite Varijablu Okruženja

Prvo, trebate postaviti svoj GitHub token kao varijablu okruženja. Ovaj token omogućuje pristup GitHub modelima besplatno.

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

### Korak 2: Navigirajte do Direktorija s Primjerima

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## Vodič 1: LLM Dovršavanja i Chat

**Datoteka:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### Što Ovaj Primjer Pokazuje

Ovaj primjer demonstrira osnovne mehanike interakcije s Velikim Jezičnim Modelima (LLM) putem OpenAI API-ja, uključujući inicijalizaciju klijenta s GitHub modelima, obrasce strukture poruka za sistemske i korisničke upite, upravljanje stanjem razgovora kroz akumulaciju povijesti poruka te podešavanje parametara za kontrolu duljine odgovora i razine kreativnosti.

### Ključni Koncepti Koda

#### 1. Postavljanje Klijenta
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

Ovo stvara vezu s GitHub modelima koristeći vaš token.

#### 2. Jednostavno Dovršavanje
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

#### 3. Memorija Razgovora
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

AI pamti prethodne poruke samo ako ih uključite u sljedeće zahtjeve.

### Pokretanje Primjera
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### Što Se Događa Kada Pokrenete

1. **Jednostavno Dovršavanje**: AI odgovara na pitanje o Javi uz smjernice sistemskog upita
2. **Višekratni Chat**: AI održava kontekst kroz više pitanja
3. **Interaktivni Chat**: Možete voditi stvarni razgovor s AI-jem

## Vodič 2: Pozivanje Funkcija

**Datoteka:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### Što Ovaj Primjer Pokazuje

Pozivanje funkcija omogućuje AI modelima da zatraže izvršavanje vanjskih alata i API-ja putem strukturiranog protokola gdje model analizira zahtjeve na prirodnom jeziku, određuje potrebne pozive funkcija s odgovarajućim parametrima koristeći JSON sheme i obrađuje vraćene rezultate kako bi generirao kontekstualne odgovore, dok stvarno izvršavanje funkcija ostaje pod kontrolom programera radi sigurnosti i pouzdanosti.

### Ključni Koncepti Koda

#### 1. Definicija Funkcije
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

Ovo govori AI-ju koje su funkcije dostupne i kako ih koristiti.

#### 2. Tok Izvršavanja Funkcije
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

#### 3. Implementacija Funkcije
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

### Pokretanje Primjera
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### Što Se Događa Kada Pokrenete

1. **Funkcija za Vrijeme**: AI traži podatke o vremenu za Seattle, vi ih pružate, AI formatira odgovor
2. **Funkcija Kalkulatora**: AI traži izračun (15% od 240), vi ga izračunate, AI objašnjava rezultat

## Vodič 3: RAG (Generacija s Poboljšanim Dohvaćanjem)

**Datoteka:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### Što Ovaj Primjer Pokazuje

Generacija s Poboljšanim Dohvaćanjem (RAG) kombinira dohvaćanje informacija s generacijom jezika tako što ubacuje kontekst vanjskih dokumenata u AI upite, omogućujući modelima da daju točne odgovore na temelju specifičnih izvora znanja umjesto potencijalno zastarjelih ili netočnih podataka iz obuke, dok jasno razdvajaju korisničke upite i autoritativne izvore informacija kroz strateško oblikovanje upita.

### Ključni Koncepti Koda

#### 1. Učitavanje Dokumenata
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. Ubacivanje Konteksta
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

Trokostruki navodnici pomažu AI-ju razlikovati kontekst od pitanja.

#### 3. Sigurno Rukovanje Odgovorima
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

Uvijek provjerite odgovore API-ja kako biste spriječili padove.

### Pokretanje Primjera
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### Što Se Događa Kada Pokrenete

1. Program učitava `document.txt` (sadrži informacije o GitHub modelima)
2. Postavljate pitanje o dokumentu
3. AI odgovara isključivo na temelju sadržaja dokumenta, a ne svog općeg znanja

Pokušajte pitati: "Što su GitHub modeli?" naspram "Kakvo je vrijeme?"

## Vodič 4: Odgovorna Umjetna Inteligencija

**Datoteka:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### Što Ovaj Primjer Pokazuje

Primjer Odgovorne Umjetne Inteligencije naglašava važnost implementacije sigurnosnih mjera u AI aplikacijama. Pokazuje sigurnosne filtere koji otkrivaju kategorije štetnog sadržaja uključujući govor mržnje, uznemiravanje, samoozljeđivanje, seksualni sadržaj i nasilje, demonstrirajući kako bi AI aplikacije u produkciji trebale graciozno rukovati kršenjima pravila sadržaja kroz pravilno rukovanje iznimkama, mehanizme povratnih informacija korisnika i strategije rezervnih odgovora.

### Ključni Koncepti Koda

#### 1. Okvir za Testiranje Sigurnosti
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

#### 2. Testirane Kategorije Sigurnosti
- Nasilje/Štetne upute
- Govor mržnje
- Kršenje privatnosti
- Medicinske dezinformacije
- Nezakonite aktivnosti

### Pokretanje Primjera
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### Što Se Događa Kada Pokrenete

Program testira razne štetne upite i pokazuje kako AI sigurnosni sustav:
1. **Blokira opasne zahtjeve** s HTTP 400 pogreškama
2. **Dopušta siguran sadržaj** da se generira normalno
3. **Štiti korisnike** od štetnih AI odgovora

## Uobičajeni Obrasci u Primjerima

### Obrazac Autentifikacije
Svi primjeri koriste ovaj obrazac za autentifikaciju s GitHub modelima:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### Obrazac Rukovanja Pogreškama
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### Obrazac Strukture Poruka
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## Sljedeći Koraci

[Četvrto poglavlje: Praktični primjeri](../04-PracticalSamples/README.md)

## Rješavanje Problema

### Uobičajeni Problemi

**"GITHUB_TOKEN nije postavljen"**
- Provjerite jeste li postavili varijablu okruženja
- Provjerite ima li vaš token `models:read` dozvolu

**"Nema odgovora od API-ja"**
- Provjerite internetsku vezu
- Provjerite je li vaš token valjan
- Provjerite jeste li premašili ograničenja zahtjeva

**Pogreške pri Maven kompilaciji**
- Osigurajte da imate Javu 21 ili noviju verziju
- Pokrenite `mvn clean compile` za osvježavanje ovisnosti

**Odricanje od odgovornosti**:  
Ovaj dokument je preveden pomoću AI usluge za prevođenje [Co-op Translator](https://github.com/Azure/co-op-translator). Iako nastojimo osigurati točnost, imajte na umu da automatski prijevodi mogu sadržavati pogreške ili netočnosti. Izvorni dokument na izvornom jeziku treba smatrati autoritativnim izvorom. Za kritične informacije preporučuje se profesionalni prijevod od strane čovjeka. Ne preuzimamo odgovornost za nesporazume ili pogrešna tumačenja koja mogu proizaći iz korištenja ovog prijevoda.