<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "59454ab4ec36d89840df6fcfe7633cbd",
  "translation_date": "2025-07-25T10:43:27+00:00",
  "source_file": "03-CoreGenerativeAITechniques/README.md",
  "language_code": "ar"
}
-->
# دليل تقنيات الذكاء الاصطناعي التوليدي الأساسية

## جدول المحتويات

- [المتطلبات الأساسية](../../../03-CoreGenerativeAITechniques)
- [البدء](../../../03-CoreGenerativeAITechniques)
  - [الخطوة 1: ضبط متغير البيئة](../../../03-CoreGenerativeAITechniques)
  - [الخطوة 2: الانتقال إلى دليل الأمثلة](../../../03-CoreGenerativeAITechniques)
- [الدرس 1: إكمالات LLM والدردشة](../../../03-CoreGenerativeAITechniques)
- [الدرس 2: استدعاء الوظائف](../../../03-CoreGenerativeAITechniques)
- [الدرس 3: RAG (التوليد المعزز بالاسترجاع)](../../../03-CoreGenerativeAITechniques)
- [الدرس 4: الذكاء الاصطناعي المسؤول](../../../03-CoreGenerativeAITechniques)
- [أنماط شائعة عبر الأمثلة](../../../03-CoreGenerativeAITechniques)
- [الخطوات التالية](../../../03-CoreGenerativeAITechniques)
- [استكشاف الأخطاء وإصلاحها](../../../03-CoreGenerativeAITechniques)
  - [مشاكل شائعة](../../../03-CoreGenerativeAITechniques)

## نظرة عامة

يوفر هذا الدليل أمثلة عملية لتقنيات الذكاء الاصطناعي التوليدي الأساسية باستخدام Java ونماذج GitHub. ستتعلم كيفية التفاعل مع نماذج اللغة الكبيرة (LLMs)، وتنفيذ استدعاء الوظائف، واستخدام التوليد المعزز بالاسترجاع (RAG)، وتطبيق ممارسات الذكاء الاصطناعي المسؤول.

## المتطلبات الأساسية

قبل البدء، تأكد من توفر ما يلي:
- Java 21 أو إصدار أحدث مثبت
- Maven لإدارة التبعيات
- حساب GitHub مع رمز وصول شخصي (PAT)

## البدء

### الخطوة 1: ضبط متغير البيئة

أولاً، تحتاج إلى ضبط رمز GitHub الخاص بك كمتغير بيئة. يتيح لك هذا الرمز الوصول إلى نماذج GitHub مجانًا.

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

### الخطوة 2: الانتقال إلى دليل الأمثلة

```bash
cd 03-CoreGenerativeAITechniques/examples/
```

## الدرس 1: إكمالات LLM والدردشة

**الملف:** `src/main/java/com/example/genai/techniques/completions/LLMCompletionsApp.java`

### ما الذي يقدمه هذا المثال

يوضح هذا المثال الآليات الأساسية للتفاعل مع نماذج اللغة الكبيرة (LLM) من خلال واجهة برمجة تطبيقات OpenAI، بما في ذلك تهيئة العميل باستخدام نماذج GitHub، وأنماط هيكلة الرسائل للمطالبات النظامية والمستخدم، وإدارة حالة المحادثة من خلال تراكم سجل الرسائل، وضبط المعلمات للتحكم في طول الاستجابة ومستوى الإبداع.

### مفاهيم الكود الرئيسية

#### 1. إعداد العميل
```java
// Create the AI client
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(new StaticTokenCredential(pat))
    .buildClient();
```

هذا ينشئ اتصالاً بنماذج GitHub باستخدام الرمز الخاص بك.

#### 2. إكمال بسيط
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

#### 3. ذاكرة المحادثة
```java
// Add AI's response to maintain conversation history
messages.add(new ChatRequestAssistantMessage(aiResponse));
messages.add(new ChatRequestUserMessage("Follow-up question"));
```

يتذكر الذكاء الاصطناعي الرسائل السابقة فقط إذا قمت بتضمينها في الطلبات اللاحقة.

### تشغيل المثال
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.completions.LLMCompletionsApp"
```

### ما يحدث عند تشغيله

1. **إكمال بسيط**: يجيب الذكاء الاصطناعي على سؤال Java مع توجيه من النظام
2. **دردشة متعددة الأدوار**: يحافظ الذكاء الاصطناعي على السياق عبر أسئلة متعددة
3. **دردشة تفاعلية**: يمكنك إجراء محادثة حقيقية مع الذكاء الاصطناعي

## الدرس 2: استدعاء الوظائف

**الملف:** `src/main/java/com/example/genai/techniques/functions/FunctionsApp.java`

### ما الذي يقدمه هذا المثال

يتيح استدعاء الوظائف لنماذج الذكاء الاصطناعي طلب تنفيذ أدوات وواجهات برمجة تطبيقات خارجية من خلال بروتوكول منظم حيث يحلل النموذج الطلبات الطبيعية، ويحدد استدعاءات الوظائف المطلوبة مع المعلمات المناسبة باستخدام تعريفات JSON Schema، ويعالج النتائج المسترجعة لتوليد استجابات سياقية، بينما يظل تنفيذ الوظائف الفعلي تحت سيطرة المطور لضمان الأمان والموثوقية.

### مفاهيم الكود الرئيسية

#### 1. تعريف الوظيفة
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

هذا يخبر الذكاء الاصطناعي بالوظائف المتاحة وكيفية استخدامها.

#### 2. تدفق تنفيذ الوظيفة
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

#### 3. تنفيذ الوظيفة
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

### تشغيل المثال
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.functions.FunctionsApp"
```

### ما يحدث عند تشغيله

1. **وظيفة الطقس**: يطلب الذكاء الاصطناعي بيانات الطقس لمدينة سياتل، تقدمها له، ويقوم بتنسيق استجابة
2. **وظيفة الآلة الحاسبة**: يطلب الذكاء الاصطناعي حساب (15% من 240)، تحسبها له، ويشرح النتيجة

## الدرس 3: RAG (التوليد المعزز بالاسترجاع)

**الملف:** `src/main/java/com/example/genai/techniques/rag/SimpleReaderDemo.java`

### ما الذي يقدمه هذا المثال

يجمع التوليد المعزز بالاسترجاع (RAG) بين استرجاع المعلومات وتوليد اللغة من خلال إدخال سياق مستند خارجي في مطالبات الذكاء الاصطناعي، مما يتيح للنماذج تقديم إجابات دقيقة بناءً على مصادر معرفة محددة بدلاً من بيانات التدريب التي قد تكون قديمة أو غير دقيقة، مع الحفاظ على حدود واضحة بين استفسارات المستخدم والمصادر المعلوماتية الموثوقة من خلال هندسة المطالبات الاستراتيجية.

### مفاهيم الكود الرئيسية

#### 1. تحميل المستند
```java
// Load your knowledge source
String doc = Files.readString(Paths.get("document.txt"));
```

#### 2. إدخال السياق
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

تساعد علامات الاقتباس الثلاثية الذكاء الاصطناعي على التمييز بين السياق والسؤال.

#### 3. التعامل مع الاستجابات بأمان
```java
if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
    String answer = response.getChoices().get(0).getMessage().getContent();
    System.out.println("Assistant: " + answer);
} else {
    System.err.println("Error: No response received from the API.");
}
```

تحقق دائمًا من صحة استجابات واجهة برمجة التطبيقات لتجنب الأعطال.

### تشغيل المثال
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.rag.SimpleReaderDemo"
```

### ما يحدث عند تشغيله

1. يقوم البرنامج بتحميل `document.txt` (يحتوي على معلومات حول نماذج GitHub)
2. تسأل سؤالاً حول المستند
3. يجيب الذكاء الاصطناعي بناءً فقط على محتوى المستند، وليس على معرفته العامة

جرّب أن تسأل: "ما هي نماذج GitHub؟" مقابل "كيف هو الطقس؟"

## الدرس 4: الذكاء الاصطناعي المسؤول

**الملف:** `src/main/java/com/example/genai/techniques/responsibleai/ResponsibleGithubModels.java`

### ما الذي يقدمه هذا المثال

يوضح مثال الذكاء الاصطناعي المسؤول أهمية تنفيذ تدابير السلامة في تطبيقات الذكاء الاصطناعي. يعرض فلاتر السلامة التي تكتشف فئات المحتوى الضار بما في ذلك خطاب الكراهية، والتحرش، وإيذاء النفس، والمحتوى الجنسي، والعنف، مما يوضح كيف يجب أن تتعامل تطبيقات الذكاء الاصطناعي الإنتاجية مع انتهاكات سياسة المحتوى من خلال التعامل الصحيح مع الاستثناءات، وآليات تقديم الملاحظات للمستخدم، واستراتيجيات الاستجابة البديلة.

### مفاهيم الكود الرئيسية

#### 1. إطار اختبار السلامة
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

#### 2. فئات السلامة التي تم اختبارها
- تعليمات العنف/الإيذاء
- خطاب الكراهية
- انتهاكات الخصوصية
- المعلومات الطبية المضللة
- الأنشطة غير القانونية

### تشغيل المثال
```bash
mvn compile exec:java -Dexec.mainClass="com.example.genai.techniques.responsibleai.ResponsibleGithubModels"
```

### ما يحدث عند تشغيله

يختبر البرنامج مطالبات ضارة مختلفة ويظهر كيف يقوم نظام أمان الذكاء الاصطناعي:
1. **بحظر الطلبات الخطرة** مع أخطاء HTTP 400
2. **يسمح بالمحتوى الآمن** ليتم توليده بشكل طبيعي
3. **يحمي المستخدمين** من مخرجات الذكاء الاصطناعي الضارة

## أنماط شائعة عبر الأمثلة

### نمط المصادقة
تستخدم جميع الأمثلة هذا النمط للمصادقة مع نماذج GitHub:

```java
String pat = System.getenv("GITHUB_TOKEN");
TokenCredential credential = new StaticTokenCredential(pat);
OpenAIClient client = new OpenAIClientBuilder()
    .endpoint("https://models.inference.ai.azure.com")
    .credential(credential)
    .buildClient();
```

### نمط التعامل مع الأخطاء
```java
try {
    // AI operation
} catch (HttpResponseException e) {
    // Handle API errors (rate limits, safety filters)
} catch (Exception e) {
    // Handle general errors (network, parsing)
}
```

### نمط هيكلة الرسائل
```java
List<ChatRequestMessage> messages = List.of(
    new ChatRequestSystemMessage("Set AI behavior"),
    new ChatRequestUserMessage("User's actual request")
);
```

## الخطوات التالية

[الفصل 04: أمثلة عملية](../04-PracticalSamples/README.md)

## استكشاف الأخطاء وإصلاحها

### مشاكل شائعة

**"GITHUB_TOKEN غير مضبوط"**
- تأكد من ضبط متغير البيئة
- تحقق من أن الرمز الخاص بك يحتوي على نطاق `models:read`

**"لا توجد استجابة من واجهة برمجة التطبيقات"**
- تحقق من اتصالك بالإنترنت
- تحقق من صلاحية الرمز الخاص بك
- تحقق مما إذا كنت قد تجاوزت حدود المعدل

**أخطاء تجميع Maven**
- تأكد من أن لديك Java 21 أو إصدار أحدث
- قم بتشغيل `mvn clean compile` لتحديث التبعيات

**إخلاء المسؤولية**:  
تم ترجمة هذا المستند باستخدام خدمة الترجمة بالذكاء الاصطناعي [Co-op Translator](https://github.com/Azure/co-op-translator). بينما نسعى لتحقيق الدقة، يرجى العلم أن الترجمات الآلية قد تحتوي على أخطاء أو معلومات غير دقيقة. يجب اعتبار المستند الأصلي بلغته الأصلية المصدر الموثوق. للحصول على معلومات حاسمة، يُوصى بالاستعانة بترجمة بشرية احترافية. نحن غير مسؤولين عن أي سوء فهم أو تفسيرات خاطئة تنشأ عن استخدام هذه الترجمة.