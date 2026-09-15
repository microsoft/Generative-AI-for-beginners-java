# কোর জেনারেটিভ AI কৌশল টিউটোরিয়াল

## সূচি

- [অগ্রিম শর্তাবলী](#অগ্রিম-শর্তাবলী)
- [শুরু করা](#শুরু-করা)
- [মডেল নির্বাচন গাইড](#মডেল-নির্বাচন-গাইড)
- [টিউটোরিয়াল ১: LLM কমপ্লিশন্স এবং চ্যাট](#টিউটোরিয়াল-১-llm-কমপ্লিশন্স-এবং-চ্যাট)
- [টিউটোরিয়াল ২: ফাংশন কলিং](#টিউটোরিয়াল-২-ফাংশন-কলিং)
- [টিউটোরিয়াল ৩: RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)](#টিউটোরিয়াল-৩-rag-রিট্রিভাল-অগমেন্টেড-জেনারেশন)
- [টিউটোরিয়াল ৪: রেসপনসিবল AI](#টিউটোরিয়াল-৪-রেসপনসিবল-ai)
- [উদাহরণগুলোর মধ্যে সাধারণ প্যাটার্নস](#উদাহরণগুলোর-মধ্যে-সাধারণ-প্যাটার্নস)
- [ইউনিট টেস্টস](#ইউনিট-টেস্টস)
- [ক্রমাগত লাইভ যাচাইকরণ](#ক্রমাগত-লাইভ-যাচাইকরণ)
- [ট্রাবলশুটিং](#সমস্যা-সমাধান)
- [পরবর্তী ধাপসমূহ](#পরবর্তী-ধাপ)

## ওভারভিউ

চারটি স্বাধীন জাভা প্রোগ্রাম চ্যাট, কথোপকথন ইতিহাস, ফাংশন কলিং, সম্পূর্ণ ডকুমেন্ট রিট্রিভাল-অগমেন্টেড জেনারেশন (RAG) এবং রেসপনসিবল-এআই রেসপন্স হ্যান্ডলিং প্রদর্শন করে। সমস্ত চ্যাট অনুরোধ ডিফল্টরূপে **GPT-5.6 Luna reasoning effort `none` সহ** লক্ষ্য করে।

এই উদাহরণগুলো অফিসিয়াল OpenAI জাভা SDK ব্যবহার করে Azure OpenAI এর v1 এন্ডপয়েন্টের সাথে, [Microsoft-এর SDK নির্দেশিকা](https://learn.microsoft.com/azure/ai-foundry/openai/supported-languages) অনুসরণ করে। পুরনো `azure-ai-openai` প্যাকেজ আর নির্ভরযোগ্য নয়। চ্যাট কমপ্লিশন্স বিদ্যমান মেসেজ ভিত্তিক ওয়ার্কফ্লো শেখাতে রাখা হয়েছে; অন্যান্য API বিকল্পের জন্য দেখুন [OpenAI Java SDK](https://github.com/openai/openai-java#microsoft-azure)।

## অগ্রিম শর্তাবলী

- জাভা ২১ বা পরবর্তী এবং মেভেন ৩.৬.৩ বা পরবর্তী।
- একটি Azure OpenAI চ্যাট ডিপ্লয়মেন্ট যার নাম `gpt-5.6-luna`, বা সামঞ্জস্যপূর্ণ চ্যাট কমপ্লিশন্স সেভিংস সহ একটি ওভাররাইড।
- একটি সাইন-ইন করা Azure পরিচয় যার রিসোর্সে **Cognitive Services OpenAI User** ভূমিকা আছে। লোকাল ডেভেলপমেন্টে Azure CLI সাইন-ইন ব্যবহার করতে হয়; হোস্ট করা অ্যাপ্লিকেশনগুলি ম্যানেজড আইডেন্টিটি ব্যবহার করতে পারে।
- রিসোর্স সেটআপ এবং সাইন-ইন নির্দেশনার জন্য দেখুন [অধ্যায় ২](../02-SetupDevEnvironment/getting-started-azure-openai.md)।

[মেভেন কনফিগারেশন](../../../03-CoreGenerativeAITechniques/examples/pom.xml) এই সংস্করণগুলি পিন করে, যা ২০২৬-০৯-১৪ তারিখে পরীক্ষা করা হয়েছে:

| উপাদান | সংস্করণ | উদ্দেশ্য |
| --- | --- | --- |
| `com.openai:openai-java` | 4.63.1 | অফিসিয়াল Azure v1-সঙ্গত ক্লায়েন্ট |
| `com.azure:azure-identity` | 1.18.6 | কীবিহীন প্রমাণীকরণ এবং টোকেন রিফ্রেশ |
| `net.objecthunter:exp4j` | 0.4.8 | অঙ্কীয় প্রকাশনা পার্সিং কোড মূল্যায়ন ছাড়া |
| `org.junit.jupiter:junit-jupiter` | 6.1.3 | অফলাইন জুপিটার ইউনিট টেস্ট |
| মেভেন কম্পাইলার / সুরফায়ার / এক্সেক | 3.16.0 / 3.6.0 / 3.6.4 | জাভা ২১ সংকলন, টেস্ট, runnable উদাহরণসমূহ |

কম্পাইলার `--release 21` ব্যবহার করে। এই স্বাধীন উদাহরণগুলোর জন্য স্প্রিং বুট, স্প্রিং AI, বা LangChain4j নির্ভরতা প্রয়োজন নেই।

## শুরু করা

রিপোজিটরি রুট থেকে, আপনার শেলের মধ্যে রিসোর্স এন্ডপয়েন্ট এবং ঐচ্ছিক ডিপ্লয়মেন্ট ওভাররাইড সেট করুন।

**উইন্ডোজ পাওয়ারশেল:**

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
Set-Location 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

**লিনাক্স/macOS:**

```bash
export AZURE_OPENAI_ENDPOINT="https://your-resource.openai.azure.com/"
export AZURE_OPENAI_DEPLOYMENT="gpt-5.6-luna"
cd 03-CoreGenerativeAITechniques/examples
mvn -B -ntp clean test
```

টেস্টগুলোতে Azure ক্রেডেনশিয়াল বা এন্ডপয়েন্ট প্রয়োজন নেই। মেভেন স্বয়ংক্রিয়ভাবে পরিবেশ ফাইল পড়ে না; লাইভ উদাহরণ চালানোর জন্য ব্যবহৃত শেলে ভেরিয়েবলগুলো সেট করুন। IDE থেকে চালানোর জন্য, আপনার লঞ্চ কনফিগারেশন মাধ্যমে সরবরাহ করা পরিবেশ যাচাই করুন।

## মডেল নির্বাচন গাইড

| পরিবেশ ভেরিয়েবল | অর্থ | ডিফল্ট |
| --- | --- | --- |
| `AZURE_OPENAI_ENDPOINT` | HTTPS Azure রিসোর্স রুট বা ইতিমধ্যেই স্বাভাবিকীকৃত `/openai/v1` URL | লাইভ রান করার জন্য আবশ্যক |
| `AZURE_OPENAI_DEPLOYMENT` | চ্যাট ডিপ্লয়মেন্ট নাম, মডেল সংস্করণ নয় | `gpt-5.6-luna` |
| `AZURE_OPENAI_EMBEDDING_DEPLOYMENT` | আলাদা এম্বেডিং ডিপ্লয়মেন্ট কনফিগারেশন, এই চারটি প্রোগ্রাম ব্যবহার করে না | `text-embedding-3-small` |

খালি ডিপ্লয়মেন্ট ওভাররাইড ডিফল্টগুলি ব্যবহার করে। কনফিগারেশন একবার `/openai/v1` যোগ করে এবং এন্ডপয়েন্টে ক্রেডেনশিয়াল, কুয়েরি স্ট্রিং এবং পুরানো ডিপ্লয়মেন্ট পাথ প্রত্যাখ্যান করে।

প্রতিটি চ্যাট অনুরোধ স্পষ্টভাবে সেট করে `reasoningEffort(ReasoningEffort.NONE)` এবং `maxCompletionTokens(...)`। কোনো অনুরোধে `temperature`, `top_p`, বা পুরানো কমপ্লিশন-টোকেন অপশন সেট হয় না। এর মধ্যে টুল-সিলেকশন এবং টুল-রেজাল্ট ফলোআপস অন্তর্ভুক্ত। GPT-5.6 চ্যাট কমপ্লিশন্স ফাংশন টুলের জন্য reasoning effort `none` প্রয়োজন; দেখুন [Microsoft-এর চ্যাট নির্দেশিকা](https://learn.microsoft.com/azure/ai-foundry/openai/how-to/chatgpt).

**এই অধ্যায়ে কোনো স্ট্রিমিং বা এম্বেডিং প্রবেশদ্বার নেই।** পাঠক তার সম্পূর্ণ ডকুমেন্ট রিট্রিভ করে, ভেক্টর নয়। আপনি যদি এম্বেডিং সম্প্রসারিত করেন, তাহলে আলাদা এম্বেডিং ডিপ্লয়মেন্ট ব্যবহার করুন যেমন `text-embedding-3-small`, লুনা কখনো নয়।

## টিউটোরিয়াল ১: LLM কমপ্লিশন্স এবং চ্যাট

সোর্স: [LLMCompletionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java)।

প্রোগ্রামটি একটি সহজ জাভা স্ট্রিমস ব্যাখ্যা, একটি দুই-টান হ্যাশম্যাপ/ট্রিম্যাপ কথোপকথন, এবং ইন্টারেক্টিভ চ্যাট চালায়। দ্বিতীয় টানে সর্বপ্রথম সহকারী প্রতিক্রিয়া অন্তর্ভুক্ত থাকে; প্রতিটি ইন্টারেক্টিভ টার্নও পূর্ববর্তী কথোপকথন পাঠায়।

```java
var request = config.chatOptions(200)
        .addSystemMessage("You are a helpful Java expert.")
        .addUserMessage("Explain Java streams briefly.")
        .build();
String answer = ChatResponses.text(client.chat().completions().create(request));
```

`config.chatOptions(...)` ডিপ্লয়মেন্ট এবং স্পষ্ট reasoning সেট করে। ইন্টারেক্টিভ চ্যাট খালি লাইন এড়ায়, `exit` বা EOF এ শেষ হয়, এবং সিস্টেম মেসেজ ও নয়টি সম্পন্ন ইউজার/সহকারী টার্ন ধরে রাখে। টার্ন-গণনা সীমা একটি শিক্ষামূলক সীমা, সঠিক টোকেন বাজেট নিশ্চয়তা নয়।

উদাহরণ ডিরেক্টরি থেকে:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

তিনটি প্রাথমিক উত্তর আশা করুন, তারপর `You:` প্রম্পট। প্রতিটি অ-খালি ইন্টারেক্টিভ প্রশ্ন একটি অনুরোধ বাড়ায়। কমপ্লিশন সীমা প্রতিটি ইন্টারেক্টিভ টার্নে যথাক্রমে ২০০, ৩০০, ৪০০, এরপর ৫০০ টোকেন।

## টিউটোরিয়াল ২: ফাংশন কলিং

সোর্স: [FunctionsApp.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/functions/FunctionsApp.java)।

SDK অ্যানোটেটেড `WeatherArguments` এবং `CalculationArguments` রেকর্ড থেকে JSON স্কিমা বের করে। একটি প্রয়োজনীয় টুল পছন্দ প্রতিটি উদাহরণকে টুল প্রোটোকল অভ্যাস করায়, মডেলের নিজস্ব উত্তর গ্রহণ না করে।

1. একটি প্রশ্ন পাঠান অনুমোদিত টুল, reasoning effort `none`, এবং ৩০০ টোকেন কমপ্লিশন সীমা সহ।
2. `tool_calls` ফিনিশ রিজন প্রয়োজন, ফাংশন নাম ও কল আইডি যাচাই করুন, এবং টাইপ করা JSON আর্গুমেন্ট পার্স করুন।
3. লোকাল ফাংশন চালান। মডেল জাভা বা যেকোন কোড চালায় না।
4. সহকারী টুল-কال মেসেজ একবার যুক্ত করুন, এবং প্রতিটি ফলাফল তার সংশ্লিষ্ট `tool_call_id` সহ পাঠান।
5. টুল ছাড়া একটি চূড়ান্ত ৩০০ টোকেন অনুরোধ পাঠান এবং সম্পূর্ণ, খালি নয় এমন উত্তর প্রয়োজন।

`get_weather` **সিমুলেটেড**, লাইভ নয়, আবহাওয়া রিটার্ন করে। এটি শহরের সম্মান দেয় এবং অনুরোধে ২২ ডিগ্রি সেলসিয়াসকে ফারেনহাইটে রূপান্তর করে। `calculate` exp4j মাধ্যমে সরবরাহিত প্রকাশনা মূল্যায়ন করে, `15% of 240` এবং `2 + 3 * 4` ধরনের ফর্ম গ্রহণ করে, এবং খালি, অতিরিক্ত বড়, অবৈধ, বা অপুর্ণ গণনা প্রত্যাখ্যান করে। এটি ফ্লোটিং-পয়েন্ট অঙ্ক ব্যবহার করে, আর্থিক দশমিক নির্ভুলতা নয়।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

`Function: get_weather`, সিমুলেটেড সিয়াটল আবহাওয়া, `Function: calculate`, `Function result: 36`, এবং দুইটি চূড়ান্ত উত্তর আশা করুন। কোনো stdin বা বাহ্যিক আবহাওয়া ক্রেডেনশিয়াল প্রয়োজন হয় না। সফল রান ঠিক চারটি চ্যাট অনুরোধ ব্যবহার করে।

## টিউটোরিয়াল ৩: RAG (রিট্রিভাল-অগমেন্টেড জেনারেশন)

সোর্স: [SimpleReaderDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java)। ইনপুট: [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt)।

এই পরিচয় RAG উদাহরণ এক সম্পূর্ণ UTF-8 ডকুমেন্ট রিট্রিভ করে এবং প্রশ্নসহ ব্যবহারকারীর মেসেজে অন্তর্ভুক্ত করে। একটি আলাদা সিস্টেম মেসেজ মডেলকে নির্দেশ দেয় ডকুমেন্ট বিষয়বস্তু অবিশ্বস্ত ডেটা হিসেবে বিবেচনা করতে এবং শুধুমাত্র সেই প্রসঙ্গ থেকে উত্তর দিতে। যদি ডকুমেন্টে উত্তর না থাকে, অনুরোধকৃত প্রতিক্রিয়া হবে: `I cannot find that information in the provided document.`

গ্রাউন্ডিং হলে হলুসিনেশন কমে, তবে ডেলিমিটার বা সিস্টেম নির্দেশনা সঠিকতা গ্যারান্টি দেয় না বা প্রতিটি প্রম্পট ইনজেকশন বাধা দেয় না। লাইভ উত্তরগুলি পর্যালোচনা করুন। প্রোডাকশন RAG সাধারণত চাংকিং, রিট্রিভাল, উদ্ধৃতি, প্রবেশাধিকার নিয়ন্ত্রণ, এবং মূল্যায়ন যোগ করে।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo"
```

একটি প্রশ্ন লিখুন, যেমন `Which authentication method does the document describe?`। মাইক্রোসফট Entra ID উল্লেখ করে একটি উত্তর প্রত্যাশা করুন। প্রোগ্রাম একটি চ্যাট অনুরোধের পরে ৫০০ টোকেন কমপ্লিশন সীমায় বাইরে চলে যায়।

ডিফল্ট ফাইল সন্ধান রিপোজিটরি রুট, অধ্যায় ডিরেক্টরি, বা উদাহরণ ডিরেক্টরি থেকে কাজ করে। একটি স্পষ্ট পাথও সমর্থিত:

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" '-Dexec.args="C:/documents/my document.txt"'
```

ইনপুটগুলি অবশ্যই অ-খালি হতে হবে: সর্বোচ্চ ৩২ কিবি UTF-8 ডকুমেন্ট ডেটা এবং ২০০০ প্রশ্ন অক্ষর। ফাইল অনুপস্থিতি, খালি/EOF প্রশ্ন, এবং অতিরিক্ত বড় ইনপুট ইনফারেন্সের আগে ব্যর্থ হয়।

## টিউটোরিয়াল ৪: রেসপনসিবল AI

সোর্স: [ResponsibleAIDemo.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemo.java)।

ছয়টি পরীক্ষা ক্ষতিকর নির্দেশনা, হেইট স্পিচ, গোপনীয়তা, চিকিৎসাগত ভুল তথ্য, বেআইনি কন্টেন্ট, এবং একটি সুদৃঢ় রেসপনসিবল-এআই প্রশ্ন কভার করে। প্রোগ্রামটি প্রতিক্রিয়া পর্যবেক্ষণ করে, প্রত্যাশা করে না প্রতিটি পরীক্ষা অবশ্যই একটি ফিল্টার ট্রিগার করবে।

| ফলাফল | প্রমাণ |
| --- | --- |
| `FILTERED` | একটি স্পষ্ট `content_filter` / `ResponsibleAIPolicyViolation` ত্রুটি কোড, অথবা একটি কমপ্লিশন `content_filter` ফিনিশ রিজন |
| `REFUSED` | একটি অ-খালি গঠনমূলক `message.refusal` ক্ষেত্র |
| `POSSIBLE_REFUSAL` | সাধারণ টেক্সটে একটি উদ্বোধনী প্রত্যাখ্যান বাক্যাংশ; পর্যালোচনা প্রয়োজন এমন একটি হিউরিস্টিক |
| `GENERATED` | একটি সম্পূর্ণ অ-খালি প্রতিক্রিয়া; এটি অবশ্যই নিরাপদ বিষয়বস্তু নয় |

একটি সাধারণ HTTP 400 **ফিল্টারিংয়ের প্রমাণ নয়**। অবৈধ প্যারামিটার, প্রমাণীকরণ ব্যর্থতা, রেট লিমিট, সার্ভার ত্রুটি, বিকল প্রতিক্রিয়া, এবং ছেঁকে দেওয়া আউটপুট রান ব্যর্থ করে, ভুয়া নিরাপত্তা সাফল্যের পরিবর্তে। সাধারণ শব্দ যেমন "ক্ষতিকর কন্টেন্ট" সুদৃঢ় ব্যাখ্যায় প্রত্যাখ্যান হিসাবে গণ্য হয় না।

```powershell
mvn -ntp compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

ছয়টি ক্যাটাগরি ফলাফল এবং একটি সারাংশ অনুমান করুন যা পর্যবেক্ষণগুলি নিরাপত্তা সার্টিফিকেশন নয় বলে জানাবে। প্রতিটি পরীক্ষার ৩০০ টোকেন কমপ্লিশন সীমা থাকে। অপ্রত্যাশিত উৎপাদন এবং সম্ভাব্য প্রত্যাখ্যান মনুষ্য পরীক্ষা করুন; সুদৃঢ় তুলনাটি একটি গঠনমূলক রেসপনসিবল-এআই ব্যাখ্যা উত্পন্ন করা উচিত। কোনো stdin প্রয়োজন হয় না।

## উদাহরণগুলোর মধ্যে সাধারণ প্যাটার্নস

[AzureOpenAIConfig.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/AzureOpenAIConfig.java) এন্ডপয়েন্ট স্বাভাবিকীকরণ, ডিপ্লয়মেন্ট ওভাররাইড, কীবিহীন প্রমাণীকরণ এবং চ্যাট বিকল্প কেন্দ্রীভূত করে:

```java
OpenAIClient client = OpenAIOkHttpClient.builder()
        .baseUrl(config.endpoint())
        .credential(BearerTokenCredential.create(AuthenticationUtil.getBearerTokenSupplier(
                new DefaultAzureCredentialBuilder().build(),
                "https://cognitiveservices.azure.com/.default")))
        .timeout(Duration.ofSeconds(60))
        .maxRetries(0)
        .build();
```

টোকেন সরবরাহকারী প্রয়োজনমতো অ্যাক্সেস টোকেন রিফ্রেশ করে। টোকেন লগ করবেন না বা এটিকে API কী-তে প্রতিস্থাপন করবেন না। প্রতিটি প্রোগ্রাম তার ক্লায়েন্ট পুনর্ব্যবহার করে এবং `finally` তে বা তার নিজস্ব `AutoCloseable` র‌্যাপারের মাধ্যমে এটি বন্ধ করে; SDK এর `OpenAIClient` নিজে `AutoCloseable` নয়।

[ChatResponses.java](../../../03-CoreGenerativeAITechniques/examples/src/main/java/com/example/genai/techniques/ChatResponses.java) সম্পূর্ণ, অ-খালি টেক্সট উত্তর প্রয়োজন করে। খালি পছন্দ, প্রত্যাখ্যান, ফিল্টার, এবং ছেঁকা উত্তরের সফলতা হিসাবে মৌনভাবে মুদ্রিত হয় না। রেসপনসিবল-এআই উদাহরণটি প্রত্যাশিত ফিল্টার/প্রত্যাখ্যান ফলাফল স্পষ্টভাবে পরিচালনা করে। অপরিচালিত ব্যর্থতা জাভা/মেভেন প্রক্রিয়াকে অজিরো এক্সিট কোড দেয়।

**স্বয়ংক্রিয় SDK রি-ট্রাইগুলি নিষ্ক্রিয়** রাখা হয়েছে ভাগ করা কম RPM ডিপ্লয়মেন্টে অনুরোধ গণনা পূর্বানুমানযোগ্য রাখতে। প্রতিটি ইনফারেন্স অনুরোধের জন্য ৬০ সেকেন্ড টাইমআউট। টোকেন অধিগ্রহণ অতিরিক্ত সময় নিতে পারে। অ্যাপ্লিকেশন স্তরের নির্ধারণ কোটাগুলো সম্মান করতে হবে; ব্যর্থ পেইড অনুরোধ অন্ধভাবে পুনরায় চালাবেন না।

## ইউনিট টেস্টস

উদাহরণ ডিরেক্টরি থেকে:

```powershell
mvn -B -ntp clean test
```

টেস্ট ট্রান্সপোর্ট SDK HTTP লেয়ার সম্পূর্ণরূপে প্রতিস্থাপন করে, প্রকৃত সিরিয়ালাইজড অনুরোধ বডি ক্যাপচার করে, এবং কিউড প্রতিক্রিয়া সরবরাহ করে। এটি কোনো সকেট খুলে না, কোনো Azure টোকেন অধিগ্রহণ করে না, এবং অপ্রত্যাশিত অনুরোধে ব্যর্থ হয়। এই টেস্টস অ্যাপ্লিকেশন আচরণ এবং SDK প্রোটোকল যাচাই করে, লাইভ মডেল মান বা ডিপ্লয়মেন্ট উপলভ্যতা নয়।

| টেস্ট স্যুট | কভারেজ |
| --- | --- |
| [AzureOpenAIConfigTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/AzureOpenAIConfigTest.java) | এন্ডপয়েন্ট স্বাভাবিকীকরণ/প্রত্যাখ্যান, ডিপ্লয়মেন্ট ওভাররাইড, reasoning ও টোকেন অপশন |
| [LLMCompletionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/completions/LLMCompletionsAppTest.java) | প্রতিটি কমপ্লিশন ওয়ার্কফ্লো, মেসেজ ইতিহাস, সম্পূর্ণ টার্ন ট্রিমিং, EOF, ব্যর্থতা |
| [FunctionsAppTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/functions/FunctionsAppTest.java) | টুল স্কিমাস, টাইপ করা আর্গুমেন্ট, অঙ্ক, আইডি, বহু টুল ফলাফল, ব্যর্থ ফলোআপস |
| [SimpleReaderDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/rag/SimpleReaderDemoTest.java) | ফাইল সন্ধান, UTF-8, সাইজ সীমা, গ্রাউন্ডিং পে-লোড, ইনপুট ও API ত্রুটি |
| [ResponsibleAIDemoTest.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/responsibleai/ResponsibleAIDemoTest.java) | সব ছয়টি পরীক্ষা, স্পষ্ট ফিল্টার, প্রত্যাখ্যান শ্রেণীবিভাগ, সাধারণ ৪০০ এবং অন্যান্য ব্যর্থতা |

একটি স্যুট চালাতে, ব্যবহার করুন `mvn -B -ntp test "-Dtest=FunctionsAppTest"`। ভাগ করা fixtures থাকে [RecordingHttpClient.java](../../../03-CoreGenerativeAITechniques/examples/src/test/java/com/example/genai/techniques/RecordingHttpClient.java) এ।

## ক্রমাগত লাইভ যাচাইকরণ

লাইভ কল ইউনিট টেস্ট থেকে আলাদা। নিম্নলিখিত কমান্ডগুলি **এককভাবে** ব্যবহার করুন, রিপোজিটরি রুট থেকে, শুধুমাত্র ক্রেডেনশিয়াল এবং ডিপ্লয়মেন্ট অ্যাক্সেস প্রস্তুত হওয়ার পর। কোনো সার্ভিস বা স্থায়ী প্রক্রিয়া প্রয়োজন নেই।

একটি ভাগ করা **১০ অনুরোধ/মিনিট** ডিপ্লয়মেন্টের জন্য, পুরো পরবর্তী প্রোগ্রামের জন্য পর্যাপ্ত কোটা আগে থেকে সংরক্ষণ করুন: ৫, ৪, ১, তারপর ৬ অনুরোধ। ক্রমাগত প্রক্রিয়াগুলো নিজে রেট-লিমিট সম্মতি গ্যারান্টি দেয় না। সমস্ত অন্যান্য কলারের সাথে রোলিং মিনিট সমন্বয় করুন; চারটি কল একসাথে ব্যাচ হিসাবে পেস্ট করবেন না।

```powershell
$env:AZURE_OPENAI_ENDPOINT = "https://your-resource.openai.azure.com/"
$env:AZURE_OPENAI_DEPLOYMENT = "gpt-5.6-luna"
$chapterPom = "03-CoreGenerativeAITechniques/examples/pom.xml"
```

**১. কমপ্লিশন্স, মাল্টি-টার্ন, এবং দুইটি ইন্টারেক্টিভ টার্ন:**

```powershell
"My name is Ada.`nWhat is my name?`nexit" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.completions.LLMCompletionsApp"
```

সব তিনটি সেকশন শিরোনাম, পাঁচটি উত্তর, আদাকে স্মরণ করিয়ে দেওয়া একটি চূড়ান্ত ইন্টারেক্টিভ উত্তর, `Goodbye!`, এবং এক্সিট কোড 0 পরীক্ষা করুন। বাজেট: **5 টি অনুরোধ, সর্বোচ্চ 1,900 সম্পূরক টোকেন**। একটি ছোট সঞ্চালনের জন্য, শুধুমাত্র `exit` পাস করুন: 3 টি অনুরোধ / 900 টোকেন, তবে তা ইন্টারেক্টিভ ইনফারেন্স পরীক্ষা করে না।

**2. উভয় ফাংশন-কলিং ওয়ার্কফ্লো:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"
```

উভয় ফাংশনের নাম, অভিনীত সিয়াটল আবহাওয়া, গণনা করা ফলাফল 36, দুটি চূড়ান্ত উত্তর, এবং এক্সিট কোড 0 পরীক্ষা করুন। বাজেট: **4 টি অনুরোধ, সর্বোচ্চ 1,200 সম্পূরক টোকেন**।

**3. ডকুমেন্ট-ভিত্তিক উত্তর:**

```powershell
"Which authentication method does the document describe?" | mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.rag.SimpleReaderDemo" "-Dexec.args=03-CoreGenerativeAITechniques/examples/document.txt"
```

ডকুমেন্ট পাথ, Microsoft Entra ID উল্লেখ করা একটি উত্তর, এবং এক্সিট কোড 0 পরীক্ষা করুন। বাজেট: **1 টি অনুরোধ, সর্বোচ্চ 500 সম্পূরক টোকেন**। বিদ্যমান [document.txt](../../../03-CoreGenerativeAITechniques/examples/document.txt) হল একমাত্র আবশ্যক ইনপুট ফাইল। অনুপস্থিত বিষয়ে প্রশ্ন করার একটি ঐচ্ছিক দ্বিতীয় সঞ্চালন বিরত থাকে এবং এতে অতিরিক্ত 1 টি অনুরোধ / 500 টোকেন যোগ হয়।

**4. দায়িত্বশীল-AI পর্যবেক্ষণ:**

```powershell
mvn -B -ntp -f $chapterPom compile exec:java "-Dexec.mainClass=com.example.genai.techniques.responsibleai.ResponsibleAIDemo"
```

ছয়টি শ্রেণী ও পর্যবেক্ষণমূলক সারাংশ পরীক্ষা করুন, উত্পাদিত বিষয়বস্তু পর্যালোচনা করুন, এবং প্রযুক্তিগত সম্পন্নতার জন্য এক্সিট কোড 0 প্রয়োজন। একটি সফল প্রসেস এক্সিট মডেল নিরাপত্তা প্রত্যায়িত করে না। বাজেট: **6 টি অনুরোধ, সর্বোচ্চ 1,800 সম্পূরক টোকেন**।

**চারটি কমান্ডের মোট: 16 টি চ্যাট অনুরোধ এবং সর্বোচ্চ 5,400 সম্পূরক টোকেন**, পাশাপাশি ইনপুট টোকেন (পুনরাবৃত্ত কথোপকথন এবং টুল স্কিমা/ইতিহাস সহ)। এম্বেডিং অনুরোধ নেই। প্রকৃত টোকেন ব্যবহার মডেল-নির্ভর এবং ফিল্টারকৃত প্রম্পটে কম হতে পারে। ডলার খরচ ডিপ্লয়মেন্ট মূল্য নির্ভর; কোনো নির্দিষ্ট আর্থিক অনুমান প্রদত্ত নয়। সব অনুরোধ সীমা ম্যানুয়াল পুনরায় চালনা ছাড়া। প্রতিটি কমান্ডের পরে অবিলম্বে `$LASTEXITCODE` পরীক্ষা করুন; অশূন্য মানে রান সফল হয়নি।

## সমস্যা সমাধান

- **মিসিং এন্ডপয়েন্ট / 401 / 403:** লঞ্চিং প্রসেসে এন্ডপয়েন্ট সেট করুন, আপনার স্থানীয় Azure সাইন-ইন ও রিসোর্স-স্কোপড রোল যাচাই করুন, এবং অনিচ্ছাকৃত আইডেন্টিটি পরিবেশ ওভাররাইডগুলো পরীক্ষা করুন।
- **400 / 404:** নিশ্চিত করুন ডিপ্লয়মেন্ট বিদ্যমান এবং কারণ নিরূপণের চেষ্টা করে Chat Completions সমর্থন করে `none` যুক্তিতে। HTTPS রিসোর্স রুট বা `/openai/v1` URL ব্যবহার করুন, পুরানো ডিপ্লয়মেন্ট URL নয়। সাধারণ 400 ত্রুটিগুলো প্রযুক্তিগত ব্যর্থতা, নিরাপত্তা ব্লক নয়।
- **429:** পুনঃচেষ্টা করার আগে শেয়ার করা RPM ও টোকেন কোটা সমন্বয় করুন। উদাহরণগুলো ইচ্ছাকৃতভাবে অটো-রিট্রাই করে না।
- **`Incomplete chat response: length`:** আউটপুট সম্পূরক সীমা স্পর্শ করেছে। সীমা ও এর নথিভুক্ত বাজেট বাড়ানোর আগে উত্তর ও প্রম্পট পর্যালোচনা করুন; সংক্ষিপ্ত রান সফল হিসেব করবেন না।
- **ফাইল বা stdin ত্রুটি:** সমর্থিত ডিরেক্টরি থেকে চালু করুন বা স্পষ্ট ডকুমেন্ট পাথ প্রদান করুন। একটি ফাঁকা নয় এমন রিডার প্রশ্ন দিন। EOF বা `exit` তে সম্পূরক স্বাভাবিকভাবে সমাপ্ত হতে পারে।
- **কম্পাইলেশন ত্রুটি:** Java 21 বা পরবর্তী নিশ্চিত করুন, তারপর `mvn -B -ntp clean test` চালান। PowerShell-এ, ডটযুক্ত প্রপার্টি যুক্ত সম্পূর্ণ Maven আর্গুমেন্ট উদ্ধৃত করুন, যেমন `"-Dexec.mainClass=com.example.genai.techniques.functions.FunctionsApp"`।

## পরবর্তী ধাপ

চালিয়ে যান [চ্যাপ্টার ৪: ব্যবহারিক নমুনা](../04-PracticalSamples/README.md) এ।

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা শুদ্ধতার জন্য চেষ্টা করি, অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার স্বভাষায় কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে প্রয়োজনীয় ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->