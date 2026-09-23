# অ্যাজুর AI ফাউন্ড্রির সাথে মৌলিক চ্যাট - শুরু থেকে শেষ উদাহরণ

এই উদাহরণটি একটি সহজ Spring Boot অ্যাপ্লিকেশন যা **কী ছাড়া প্রমাণীকরণ** (Microsoft Entra ID) ব্যবহার করে একটি **অ্যাজুর AI ফাউন্ড্রি** মডেলের সাথে সংযুক্ত হয় এবং আপনার সেটআপ পরীক্ষা করে। এটি Spring AI এর `ChatClient` রাখে, যা **সরকারি OpenAI Java SDK** এবং **অ্যাজুর OpenAI v1** এন্ডপয়েন্ট দ্বারা সমর্থিত।

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) এর সংস্করণগুলি হল Spring Boot **4.1.1**, Spring AI **2.0.1**, OpenAI Java **4.63.1**, Azure Identity **1.18.6**, এবং dotenv-java **3.2.0**। নমুনাটি `spring-ai-starter-model-openai` ব্যবহার করে এবং স্পষ্টভাবে `openai-java` এবং `azure-identity` ঘোষণা করে; Spring AI 2 পুরনো Azure OpenAI স্টার্টারটি সরিয়ে ফেলেছে।

## বিষয়সূচি

- [প্রয়োজনীয়তা](#প্রয়োজনীয়তা)
- [দ্রুত শুরু](#দ্রুত-শুরু)
- [প্রমাণীকরণ কিভাবে কাজ করে](#প্রমাণীকরণ-কিভাবে-কাজ-করে)
- [অ্যাপ্লিকেশন চালানো](#অ্যাপ্লিকেশন-চালানো)
  - [মেভেন ব্যবহার করে](#মেভেন-ব্যবহার-করে)
  - [VS কোড ব্যবহার করে](#vs-কোড-ব্যবহার-করে)
  - [আশিত আউটপুট](#প্রত্যাশিত-আউটপুট)
- [কনফিগারেশন রেফারেন্স](#কনফিগারেশন-রেফারেন্স)
  - [পরিবেশ ভেরিয়েবল](#পরিবেশ-ভেরিয়েবলসমূহ)
  - [Spring কনফিগারেশন](#spring-কনফিগারেশন)
- [সমস্যা সমাধান](#সমস্যা-সমাধান)
  - [সাধারণ সমস্যা](#সাধারণ-সমস্যা)
  - [ডিবাগ মোড](#ডিবাগ-মোড)
- [পরবর্তী পদক্ষেপ](#পরবর্তী-ধাপগুলো)
- [সম্পদসমূহ](#রিসোর্সসমূহ)

## প্রয়োজনীয়তা

এই উদাহরণ চালানোর পূর্বে নিশ্চিত করুন:

- একটি Azure AI Foundry সম্পদ যার মধ্যে `gpt-5.6-luna` মোতায়েন রয়েছে - এটি `azd up` দ্বারা স্বয়ংক্রিয় অথবা [Azure AI Foundry সেটআপ নির্দেশিকা](../../getting-started-azure-openai.md) থেকে ম্যানুয়ালি তৈরি করা যাবে।
- ঐ সম্পদে **Cognitive Services OpenAI User** ভূমিকা রয়েছে (Bicep টেমপ্লেটগুলি এটি স্বয়ংক্রিয়ভাবে দেয়)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli), `az login` দিয়ে সাইন ইন করা আছে
- Java 21+ এবং Maven 3.9+

> **কোন API কী প্রয়োজন নেই** — Microsoft Entra ID এর মাধ্যমে কী ছাড়া প্রমাণীকরণ হয়।

## দ্রুত শুরু

```bash
# 1. প্রকল্পে নেভিগেট করুন
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. সাইন ইন করুন যাতে কীবিহীন প্রমাণীকরণ টোকেন পেতে পারে
az login

# 3. এন্ডপয়েন্ট কনফিগার করুন
#    - যদি আপনি `azd up` চালিয়েছেন, .env আপনার জন্য লেখা হয়েছে (এটি স্কিপ করুন)।
#    - অন্যথায় টেমপ্লেট কপি করুন এবং AZURE_OPENAI_ENDPOINT সেট করুন:
cp .env.example .env

# 4. অ্যাপ্লিকেশন চালান
mvn spring-boot:run
```

## প্রমাণীকরণ কিভাবে কাজ করে

এই উদাহরণটি **Microsoft Entra ID** দিয়ে প্রমাণীকরণ করে — কোনো API কী নেই।

অ্যাপ্লিকেশনটি স্পষ্টভাবে [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) তে প্রমাণীকরণ কনফিগার করে:

1. `azureCredential()` `AuthenticationUtil.getBearerTokenSupplier` ব্যবহার করে `DefaultAzureCredential` এবং `https://ai.azure.com/.default` স্কোপ দিয়ে একটি `BearerTokenCredential` তৈরি করে।
2. `azureOpenAiClient()` `OpenAIOkHttpClient.builder()` দিয়ে একটি `OpenAIClient` গড়ে তোলে, রিসোর্স এন্ডপয়েন্ট `/openai/v1` এ রিজলভ করে, এবং `.credential(...)` দিয়ে বেয়ার ক্রেডেনশিয়াল সরবরাহ করে।
3. `azureChatModel()` ঐ ক্লায়েন্টটি Spring AI এর `OpenAiChatModel` এ সরবরাহ করে, যা লেসনের `ChatClient` কে ব্যাক করে।

এই স্পষ্ট বীনগুলো একটি গ্লোবাল `OPENAI_API_KEY` কে Azure প্রমাণীকরণ ওভাররাইড করাকে রোধ করে। শুধু YAML থেকে API কী বাদ দেওয়া মানে প্রমাণীকরণ সেটআপ নয়। `DefaultAzureCredential` আপনার লোকাল `az login` সেশন অথবা Azure এর ম্যানেজড আইডেন্টিটি ব্যবহার করতে পারে; যেকোনো আইডেন্টিটি বেছে নেওয়া হোক, তার উপরে উল্লেখিত রিসোর্স রোল থাকতে হবে।

## অ্যাপ্লিকেশন চালানো

### মেভেন ব্যবহার করে

```bash
mvn spring-boot:run
```

### VS কোড ব্যবহার করে

1. প্রকল্পটি VS কোডে খুলুন
2. `F5` চাপুন অথবা "Run and Debug" প্যানেল ব্যবহার করুন
3. "Spring Boot-BasicChatApplication" কনফিগারেশন নির্বাচিত করুন

> **বি.দ্র.:** অ্যাপ্লিকেশনটি তার ওয়ার্কিং ডিরেক্টরি থেকে `.env` লোড করে, VS কোড থেকে চালালে ও।

### প্রত্যাশিত আউটপুট

সফলভাবে চালানোর পর উদাহরণস্বরূপ আউটপুট (স্টার্টআপ লগ বাদ দেওয়া হয়েছে; প্রতিক্রিয়ার শব্দভাণ্ডার পরিবর্তিত হতে পারে):

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## কনফিগারেশন রেফারেন্স

### পরিবেশ ভেরিয়েবলসমূহ

| ভেরিয়েবল | বর্ণনা | আবশ্যক | উদাহরণ |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry (Azure OpenAI) এন্ডপয়েন্ট URL | হ্যাঁ | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | চ্যাট মডেল মোতায়েনের নাম | না | `gpt-5.6-luna` (ডিফল্ট) |

> কোনো API কী ভেরিয়েবল নেই — কী ছাড়া প্রমাণীকরণ (Microsoft Entra ID `az login` এর মাধ্যমে)।

### Spring কনফিগারেশন

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) সেটিংসগুলো `spring.ai.openai` প্রিফিক্স এবং ফ্ল্যাটেন্ড চ্যাট প্রপার্টিজ (কোনো `options` ব্লক নেই) ব্যবহার করে:

```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

`model` হল **Azure মোতায়েনের নাম**। প্রমাণীকরণ স্পষ্ট বীনগুলোর মাধ্যমে হয়, `api-key` সেটিং নয়। লেসনে রিজনারিং নিষ্ক্রিয় করা এবং ৫০০ টোকেন সর্বোচ্চ সীমা দেয়া হয়েছে; `temperature` এবং পুরাতন `max-tokens` খালি রেখেছে।

মাইক্রোসফট সুপারিশ করে [নতুন অ্যাপ্লিকেশনগুলোর জন্য অফিসিয়াল OpenAI SDK সহ Azure OpenAI v1 ও Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)। চ্যাট কমপ্লিশন এই মেসেজ-ভিত্তিক লেসনের জন্য এখনও সমর্থিত। GPT-5.6 এর জন্য, চ্যাট কমপ্লিশনে টুল যুক্ত রিকুয়েস্টগুলোকে `reasoning_effort` `none` নির্ধারণ করতে হবে; টুলের সাথে রিজনারিং এর জন্য Responses ব্যবহার করুন। দেখুন [রিজনারিং মডেলের টুল কলিং](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)।

## সমস্যা সমাধান

### সাধারণ সমস্যা

<details>
<summary><strong>ত্রুটি: ৪০১ / "PermissionDenied" / টোকেন ত্রুটি</strong></summary>

- `az login` চালান — কী ছাড়া প্রমাণীকরণ সক্রিয় সাইন-ইন থেকে টোকেন পেতে চায়
- নিশ্চিত করুন আপনার অ্যাকাউন্টে ঐ রিসোর্সের **Cognitive Services OpenAI User** ভূমিকা রয়েছে
- আপনি যদি নতুন ভূমিকা প্রদান করে থাকেন, তার ছড়িয়ে পড়ার জন্য এক মিনিট অপেক্ষা করুন
- নিশ্চিত করুন আপনি সঠিক ভাড়াটে/সাবস্ক্রিপশনে আছেন (`az account show`)
</details>

<details>
<summary><strong>ত্রুটি: "এন্ডপয়েন্ট বৈধ নয়" / সংযোগ ত্রুটি</strong></summary>

- নিশ্চিত করুন `AZURE_OPENAI_ENDPOINT` পুরো বেস URL (যেমন, `https://your-resource.openai.azure.com/`)
- ট্রেইলিং স্ল্যাশ এর সামঞ্জস্য পরীক্ষা করুন
- নিশ্চিত করুন এন্ডপয়েন্ট আপনার প্রোভিশন্ড রিসোর্সের সাথে মেলে (`azd env get-values`)
</details>

<details>
<summary><strong>ত্রুটি: "মোতায়েন পাওয়া যায়নি"</strong></summary>

- নিশ্চিত করুন `AZURE_OPENAI_DEPLOYMENT` Azure এর কোনো মোতায়েন নামের সাথে মেলে
- চেক করুন মডেল সফলভাবে মোতায়েন এবং সক্রিয় আছে কিনা
- ডিফল্ট ডেপ্লয়মেন্ট নাম হলো `gpt-5.6-luna`
</details>

<details>
<summary><strong>ত্রুটি: ৪২৯ / রেট লিমিট অতিক্রান্ত</strong></summary>

- ডিফল্ট GPT-5.6 Luna ডেপ্লয়মেন্টের গ্লোবাল স্ট্যান্ডার্ড ক্যাপাসিটি ১০: ১০ অনুরোধ/মিনিট এবং ১০,০০০ টোকেন/মিনিট
- উদাহরণগুলো ধারাবাহিকভাবে চালান এবং পুনরায় চেষ্টা করার আগে সার্ভিসটির রিট্রি ইন্টারভালের জন্য অপেক্ষা করুন
- এই মৌলিক উদাহরণ স্বয়ংক্রিয় SDK রিট্রি নিষ্ক্রিয় করে, তাই একটি ব্যর্থ অনুরোধ সরাসরি রিপোর্ট করা হয়
</details>

<details>
<summary><strong>VS কোড: পরিবেশ ভেরিয়েবল লোড হচ্ছে না</strong></summary>

- নিশ্চিত করুন আপনার `.env` ফাইল প্রকল্পের রুট ডিরেক্টরিতে আছে (pom.xml এর সমতল)
- VS কোডের ইন্টিগ্রেটেড টার্মিনালে `mvn spring-boot:run` চালানোর চেষ্টা করুন
- যাচাই করুন যে VS কোড জাভা এক্সটেনশন সঠিকভাবে ইনস্টল হয়েছে
</details>

### ডিবাগ মোড

বিস্তারিত লগিং সক্ষম করতে, [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) এ এই লাইনগুলো আনকমেন্ট করুন:

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## পরবর্তী ধাপগুলো

**সেটআপ সম্পন্ন!** আপনার শেখার যাত্রা চালিয়ে যান:

[অধ্যায় ৩: 핵심 জেনারেটিভ AI কৌশলসমূহ](../../../03-CoreGenerativeAITechniques/README.md)

## রিসোর্সসমূহ

- [Spring AI 2 OpenAI Java SDK রূপান্তর](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 সহ অফিসিয়াল OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID সহ কীলেস অথেন্টিকেশন](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry পোর্টাল](https://ai.azure.com/)
- [Azure AI Foundry ডকুমেন্টেশন](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা শুদ্ধতার জন্য চেষ্টা করি, অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার স্বভাষায় কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে প্রয়োজনীয় ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->