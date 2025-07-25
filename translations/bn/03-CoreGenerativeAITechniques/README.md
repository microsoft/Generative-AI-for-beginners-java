<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T11:08:15+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "bn"
}
-->
# কোর জেনারেটিভ এআই টেকনিকস টিউটোরিয়াল

## বিষয়সূচি

- [প্রয়োজনীয়তা](../../../03-CoreGenerativeAITechniques)
- [শুরু করা](../../../03-CoreGenerativeAITechniques)
  - [ধাপ ১: আপনার এনভায়রনমেন্ট ভ্যারিয়েবল সেট করুন](../../../03-CoreGenerativeAITechniques)
  - [ধাপ ২: উদাহরণ ডিরেক্টরিতে যান](../../../03-CoreGenerativeAITechniques)
- [টিউটোরিয়াল ১: LLM কমপ্লিশনস এবং চ্যাট](../../../03-CoreGenerativeAITechniques)
- [টিউটোরিয়াল ২: ফাংশন কলিং](../../../03-CoreGenerativeAITechniques)
- [টিউটোরিয়াল ৩: RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](../../../03-CoreGenerativeAITechniques)
- [টিউটোরিয়াল ৪: দায়িত্বশীল এআই](../../../03-CoreGenerativeAITechniques)
- [উদাহরণগুলির সাধারণ প্যাটার্ন](../../../03-CoreGenerativeAITechniques)
- [পরবর্তী ধাপ](../../../03-CoreGenerativeAITechniques)
- [সমস্যা সমাধান](../../../03-CoreGenerativeAITechniques)
  - [সাধারণ সমস্যা](../../../03-CoreGenerativeAITechniques)

## ওভারভিউ

এই টিউটোরিয়ালে জাভা এবং গিটহাব মডেল ব্যবহার করে কোর জেনারেটিভ এআই টেকনিকসের হাতে-কলমে উদাহরণ দেওয়া হয়েছে। আপনি শিখবেন কীভাবে লার্জ ল্যাঙ্গুয়েজ মডেল (LLM) এর সাথে ইন্টারঅ্যাক্ট করবেন, ফাংশন কলিং প্রয়োগ করবেন, রিট্রিভাল-অগমেন্টেড জেনারেশন (RAG) ব্যবহার করবেন এবং দায়িত্বশীল এআই অনুশীলন প্রয়োগ করবেন।

## প্রয়োজনীয়তা

শুরু করার আগে নিশ্চিত করুন যে আপনার কাছে রয়েছে:
- জাভা ২১ বা তার বেশি সংস্করণ ইনস্টল করা
- ডিপেনডেন্সি ম্যানেজমেন্টের জন্য Maven
- একটি গিটহাব অ্যাকাউন্ট এবং একটি পার্সোনাল অ্যাক্সেস টোকেন (PAT)

## শুরু করা

### ধাপ ১: আপনার এনভায়রনমেন্ট ভ্যারিয়েবল সেট করুন

প্রথমে, আপনার গিটহাব টোকেনটি একটি এনভায়রনমেন্ট ভ্যারিয়েবল হিসেবে সেট করতে হবে। এই টোকেনটি আপনাকে গিটহাব মডেল বিনামূল্যে অ্যাক্সেস করতে দেবে।

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

### ধাপ ২: উদাহরণ ডিরেক্টরিতে যান

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## টিউটোরিয়াল ১: LLM কমপ্লিশনস এবং চ্যাট

**ফাইল:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### এই উদাহরণটি কী শেখায়

এই উদাহরণটি OpenAI API ব্যবহার করে লার্জ ল্যাঙ্গুয়েজ মডেলের (LLM) সাথে ইন্টারঅ্যাকশনের মূল বিষয়গুলি প্রদর্শন করে। এতে রয়েছে:
- গিটহাব মডেল দিয়ে ক্লায়েন্ট ইনিশিয়ালাইজেশন
- সিস্টেম এবং ব্যবহারকারীর প্রম্পটের জন্য মেসেজ স্ট্রাকচার প্যাটার্ন
- মেসেজ হিস্টোরি সংরক্ষণ করে কথোপকথনের অবস্থা পরিচালনা
- রেসপন্সের দৈর্ঘ্য এবং সৃজনশীলতার মাত্রা নিয়ন্ত্রণের জন্য প্যারামিটার টিউনিং

### মূল কোড ধারণা

#### ১. ক্লায়েন্ট সেটআপ
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

এটি আপনার টোকেন ব্যবহার করে গিটহাব মডেলের সাথে সংযোগ স্থাপন করে।

#### ২. সহজ কমপ্লিশন
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

#### ৩. কথোপকথনের মেমরি
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

এআই পূর্ববর্তী মেসেজগুলি মনে রাখে শুধুমাত্র যদি আপনি সেগুলি পরবর্তী অনুরোধে অন্তর্ভুক্ত করেন।

### উদাহরণটি চালান
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### এটি চালানোর সময় কী ঘটে

1. **সহজ কমপ্লিশন**: এআই একটি জাভা প্রশ্নের উত্তর দেয় সিস্টেম প্রম্পটের নির্দেশনা অনুসারে
2. **মাল্টি-টার্ন চ্যাট**: এআই একাধিক প্রশ্নের মধ্যে প্রসঙ্গ ধরে রাখে
3. **ইন্টারঅ্যাকটিভ চ্যাট**: আপনি এআই এর সাথে একটি বাস্তব কথোপকথন করতে পারেন

## টিউটোরিয়াল ২: ফাংশন কলিং

**ফাইল:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### এই উদাহরণটি কী শেখায়

ফাংশন কলিং এআই মডেলকে প্রোটোকল ব্যবহার করে বাইরের টুল এবং API চালানোর অনুরোধ করতে সক্ষম করে। মডেলটি প্রাকৃতিক ভাষার অনুরোধ বিশ্লেষণ করে, JSON Schema ডেফিনিশন ব্যবহার করে প্রয়োজনীয় ফাংশন কল এবং প্যারামিটার নির্ধারণ করে এবং প্রাপ্ত ফলাফল প্রক্রিয়া করে প্রসঙ্গময় রেসপন্স তৈরি করে। তবে, ফাংশনটি বাস্তবায়ন এবং নিরাপত্তা নিশ্চিত করার দায়িত্ব ডেভেলপারের উপর থাকে।

### মূল কোড ধারণা

#### ১. ফাংশন ডেফিনিশন
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

এটি এআই কে জানায় কোন ফাংশনগুলি উপলব্ধ এবং কীভাবে সেগুলি ব্যবহার করতে হবে।

#### ২. ফাংশন এক্সিকিউশন ফ্লো
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

#### ৩. ফাংশন ইমপ্লিমেন্টেশন
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

### উদাহরণটি চালান
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### এটি চালানোর সময় কী ঘটে

1. **ওয়েদার ফাংশন**: এআই সিয়াটলের আবহাওয়ার তথ্য চায়, আপনি এটি সরবরাহ করেন, এআই একটি রেসপন্স ফরম্যাট করে
2. **ক্যালকুলেটর ফাংশন**: এআই একটি গণনা (১৫% এর ২৪০) চায়, আপনি এটি গণনা করেন, এআই ফলাফল ব্যাখ্যা করে

## টিউটোরিয়াল ৩: RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)

**ফাইল:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### এই উদাহরণটি কী শেখায়

রিট্রিভাল-অগমেন্টেড জেনারেশন (RAG) তথ্য পুনরুদ্ধার এবং ভাষা প্রজন্মকে একত্রিত করে। এটি এআই প্রম্পটে বাইরের ডকুমেন্টের প্রসঙ্গ যোগ করে, মডেলকে নির্দিষ্ট জ্ঞান উৎসের উপর ভিত্তি করে সঠিক উত্তর দিতে সক্ষম করে। এটি স্ট্র্যাটেজিক প্রম্পট ইঞ্জিনিয়ারিংয়ের মাধ্যমে ব্যবহারকারীর প্রশ্ন এবং নির্ভরযোগ্য তথ্য উৎসের মধ্যে স্পষ্ট সীমারেখা বজায় রাখে।

### মূল কোড ধারণা

#### ১. ডকুমেন্ট লোডিং
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### ২. প্রসঙ্গ ইনজেকশন
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

ট্রিপল কোট এআই কে প্রসঙ্গ এবং প্রশ্নের মধ্যে পার্থক্য করতে সাহায্য করে।

#### ৩. নিরাপদ রেসপন্স হ্যান্ডলিং
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

API রেসপন্স যাচাই করুন যাতে ক্র্যাশ এড়ানো যায়।

### উদাহরণটি চালান
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### এটি চালানোর সময় কী ঘটে

1. প্রোগ্রামটি `document.txt` লোড করে (যেখানে গিটহাব মডেল সম্পর্কিত তথ্য রয়েছে)
2. আপনি ডকুমেন্ট সম্পর্কে একটি প্রশ্ন করেন
3. এআই শুধুমাত্র ডকুমেন্টের বিষয়বস্তুর উপর ভিত্তি করে উত্তর দেয়, তার সাধারণ জ্ঞানের উপর নয়

প্রশ্ন করুন: "GitHub Models কী?" বনাম "আবহাওয়া কেমন?"

## টিউটোরিয়াল ৪: দায়িত্বশীল এআই

**ফাইল:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### এই উদাহরণটি কী শেখায়

দায়িত্বশীল এআই উদাহরণটি এআই অ্যাপ্লিকেশনগুলিতে নিরাপত্তা ব্যবস্থা বাস্তবায়নের গুরুত্ব তুলে ধরে। এটি এমন সেফটি ফিল্টার প্রদর্শন করে যা ঘৃণামূলক বক্তব্য, হয়রানি, আত্ম-ক্ষতি, যৌন বিষয়বস্তু এবং সহিংসতা সহ ক্ষতিকারক বিষয়বস্তু শনাক্ত করে। এটি দেখায় কীভাবে প্রোডাকশন এআই অ্যাপ্লিকেশনগুলি কনটেন্ট পলিসি লঙ্ঘনগুলি সঠিকভাবে পরিচালনা করতে পারে, যেমন এক্সসেপশন হ্যান্ডলিং, ব্যবহারকারীর প্রতিক্রিয়া ব্যবস্থা এবং বিকল্প রেসপন্স কৌশল।

### মূল কোড ধারণা

#### ১. সেফটি টেস্টিং ফ্রেমওয়ার্ক
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

#### ২. পরীক্ষিত সেফটি ক্যাটাগরি
- সহিংসতা/ক্ষতির নির্দেশনা
- ঘৃণামূলক বক্তব্য
- গোপনীয়তা লঙ্ঘন
- চিকিৎসা সংক্রান্ত ভুল তথ্য
- অবৈধ কার্যকলাপ

### উদাহরণটি চালান
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### এটি চালানোর সময় কী ঘটে

প্রোগ্রামটি বিভিন্ন ক্ষতিকারক প্রম্পট পরীক্ষা করে এবং দেখায় কীভাবে এআই সেফটি সিস্টেম:
1. **বিপজ্জনক অনুরোধ ব্লক করে** HTTP 400 ত্রুটির মাধ্যমে
2. **নিরাপদ বিষয়বস্তু** স্বাভাবিকভাবে তৈরি করতে দেয়
3. **ব্যবহারকারীদের রক্ষা করে** ক্ষতিকারক এআই আউটপুট থেকে

## উদাহরণগুলির সাধারণ প্যাটার্ন

### অথেনটিকেশন প্যাটার্ন
সব উদাহরণ এই প্যাটার্ন ব্যবহার করে গিটহাব মডেলের সাথে অথেনটিকেট করতে:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### ত্রুটি হ্যান্ডলিং প্যাটার্ন
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### মেসেজ স্ট্রাকচার প্যাটার্ন
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## পরবর্তী ধাপ

[চ্যাপ্টার ০৪: ব্যবহারিক নমুনা](../04-PracticalSamples/README.md)

## সমস্যার সমাধান

### সাধারণ সমস্যা

**"GITHUB_TOKEN সেট করা হয়নি"**
- নিশ্চিত করুন যে আপনি এনভায়রনমেন্ট ভ্যারিয়েবল সেট করেছেন
- আপনার টোকেনের `models:read` স্কোপ আছে কিনা যাচাই করুন

**"API থেকে কোনো রেসপন্স নেই"**
- আপনার ইন্টারনেট সংযোগ পরীক্ষা করুন
- আপনার টোকেন বৈধ কিনা যাচাই করুন
- চেক করুন আপনি রেট লিমিট অতিক্রম করেছেন কিনা

**Maven কম্পাইলেশন ত্রুটি**
- নিশ্চিত করুন যে আপনার কাছে জাভা ২১ বা তার বেশি সংস্করণ রয়েছে
- ডিপেনডেন্সি রিফ্রেশ করতে `mvn clean compile` চালান

**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসম্ভব সঠিকতার জন্য চেষ্টা করি, তবে অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। এর মূল ভাষায় থাকা নথিটিকে প্রামাণিক উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য, পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদ ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যা হলে আমরা দায়বদ্ধ থাকব না।