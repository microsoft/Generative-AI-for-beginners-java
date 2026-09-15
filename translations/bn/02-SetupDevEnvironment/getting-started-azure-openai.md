# Azure AI Foundry এর জন্য ডেভেলপমেন্ট পরিবেশ সেট আপ করা

> এই নির্দেশিকাটি এই কোর্সের জাভা AI অ্যাপগুলির জন্য **Azure AI Foundry** মডেলগুলি **keyless** প্রমাণীকরণ (Microsoft Entra ID) ব্যবহার করে সেট আপ করে — কোনো API কী ম্যানেজ করতে হবে না। টুলিং-এ নতুন? শুরু করুন [ডেভেলপমেন্ট পরিবেশ গাইড](./README.md) থেকে।

এই গাইডটি এই কোর্সের জাভা AI অ্যাপগুলির জন্য **Azure AI Foundry** মডেলগুলি সেট আপ করে। আপনার দুটি পথ আছে:

- **অপশন A — `azd` + Bicep দিয়ে প্রোভিশন করুন (প্রশংসিত):** একটি কমান্ড দিয়ে Foundry অ্যাকাউন্ট এবং মডেলগুলি কোড হিসেবে ডিপ্লয় করে। কোনো পোর্টাল ক্লিক করতে হবে না।
- **অপশন B — Azure AI Foundry পোর্টালে ম্যানুয়ালি রিসোর্স তৈরি করুন।**

উভয় পথেই ব্যবহার করা হয় **keyless authentication** (Microsoft Entra ID) — কোনো API কী কপি বা লিক করার প্রয়োজন নেই।

## বিষয়বস্তু

- [কি তৈরি হয়](#কি-তৈরি-হয়)
- [প্রয়োজনীয়তা](#প্রয়োজনীয়তা)
- [অপশন A: azd + Bicep দিয়ে প্রোভিশন করা (প্রশংসিত)](#option-a-provision-with-azd--bicep-recommended)
- [অপশন B: ম্যানুয়ালি রিসোর্স তৈরি করুন](#অপশন-b-ম্যানুয়ালি-রিসোর্স-তৈরি-করুন)
- [আপনার পরিবেশ কনফিগার করুন](#আপনার-পরিবেশ-কনফিগার-করুন)
- [আপনার সেটআপ পরীক্ষা করুন](#আপনার-সেটআপ-পরীক্ষা-করুন)
- [পরবর্তী ধাপ কি?](#পরবর্তী-ধাপ-কি)
- [রিসোর্স](#রিসোর্স)
- [অতিরিক্ত রিসোর্স](#অতিরিক্ত-রিসোর্স)

## কি তৈরি হয়

[`infra/`](../../../02-SetupDevEnvironment/infra) এর Bicep টেমপ্লেটগুলি প্রোভিশন করে:

- একটি **Azure AI Foundry** অ্যাকাউন্ট (`Microsoft.CognitiveServices/accounts`, ধরণ `AIServices`) একটি প্রজেক্টসহ
- একটি **চার্ট** ডিপ্লয়মেন্ট - GPT-5.6 Luna (`gpt-5.6-luna`), সংস্করণ `2026-07-09`, `GlobalStandard` ক্ষমতা `10` (এই মডেলের জন্য ১০টি অনুরোধ/মিনিট এবং ১০,০০০ টোকেন/মিনিট)
- একটি **এম্বেডিং** ডিপ্লয়মেন্ট - `text-embedding-3-small`, সংস্করণ `1` (পরবর্তীতে অধ্যায়ে ব্যবহৃত)
- একটি **keyless রোল অ্যাসাইনমেন্ট** (`Cognitive Services OpenAI User`) যাতে আপনি কী ব্যবস্থাপনা না করে `az login` দিয়ে সাইন ইন করতে পারেন

## প্রয়োজনীয়তা

- একটি [Azure সাবস্ক্রিপশন](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java ২১+](https://learn.microsoft.com/java/openjdk/download) এবং [Maven ৩.৯+](https://maven.apache.org/download.cgi)

## অপশন A: azd + Bicep দিয়ে প্রোভিশন করুন (প্রশংসিত)

`02-SetupDevEnvironment` ফোল্ডার থেকে:

```bash
cd 02-SetupDevEnvironment

# সাইন ইন করুন (উভয় সরঞ্জাম)
azd auth login
az login

# Foundry অ্যাকাউন্ট + মডেল ডিপ্লয়মেন্ট প্রদান করুন
azd up
```

`azd` একটি **পরিবেশের নাম** (উদাহরণস্বরূপ `genai-java`), **সাবস্ক্রিপশন**, এবং **অঞ্চল** এর জন্য প্রম্পট দেয়। আপনার নিজের সাবস্ক্রিপশন এবং এমন একটি অঞ্চল নির্বাচন করুন যেখানে `gpt-5.6-luna` এবং `text-embedding-3-small` উপলব্ধ, যেমন `eastus2`। নিশ্চিত করুন যে সেই এলাকায় মডেল এবং ডিপ্লয়মেন্ট ধরনের জন্য সাবস্ক্রিপশনে পর্যাপ্ত কোটা আছে; উপলব্ধতা এবং কোটা সাবস্ক্রিপশন অনুসারে পরিবর্তিত হতে পারে।

প্রোভিশনিং শেষ হলে, azd:

1. [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) এ সংজ্ঞায়িত সবকিছু ডিপ্লয় করে।
2. একটি পোস্টপ্রোভিশন হুক চালায় যা আপনার এন্ডপয়েন্ট এবং ডিপ্লয়মেন্ট নাম সহ [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) ফাইল লিখে (কোনো গোপনীয় তথ্য নয়)।

> **পরামর্শ:** পরিবর্তন প্রয়োগ করতে যেকোন সময় `azd up` পুনরায় চালান। সবকিছু মুছে ফেলতে এবং খরচ বন্ধ করতে `azd down` চালান।

তৈরি সেটিংস দেখতে:

```bash
azd env get-values
```

এখন [আপনার সেটআপ পরীক্ষা করুন](#আপনার-সেটআপ-পরীক্ষা-করুন) এ যান।

## অপশন B: ম্যানুয়ালি রিসোর্স তৈরি করুন

পোর্টাল পছন্দ করেন? হাতে হাতে রিসোর্স তৈরি করুন:

1. [Azure AI Foundry পোর্টালে](https://ai.azure.com/) যান এবং সাইন ইন করুন।
2. **একটি প্রজেক্ট তৈরি করুন** (এটি একটি AI Foundry রিসোর্সও তৈরি করে)। একটি নাম দিন যেমন `GenAIJava`।
3. আপনার প্রজেক্টের মধ্যে, **Models + endpoints** → **Deploy model** → **Deploy base model** খুলুন।
4. **GPT-5.6 Luna** ডিপ্লয় করুন (মডেল এবং ডিপ্লয়মেন্ট নাম `gpt-5.6-luna`, সংস্করণ `2026-07-09`) **Global Standard** ক্ষমতা `10` সহ। এম্বেডিং উদাহরণ চাইলে **text-embedding-3-small**, সংস্করণ `1` এর জন্যও পুনরাবৃত্তি করুন।
5. **Overview** থেকে **এন্ডপয়েন্ট** কপি করুন (উদাহরণস্বরূপ `https://<resource>.openai.azure.com/`)।
6. নিজেকে keyless অ্যাক্সেস প্রদান করুন: রিসোর্সে গিয়ে **Access control (IAM)** → **Add role assignment** → আপনার অ্যাকাউন্টে **Cognitive Services OpenAI User** রোল অ্যাসাইন করুন।

> **কোনো সমস্যা হচ্ছে?** দেখুন [Azure AI Foundry ডকুমেন্টেশন](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)।

## আপনার পরিবেশ কনফিগার করুন

**অপশন A (`azd up`) ব্যবহার করলে**, আপনার সেটিংস ফাইল ইতিমধ্যেই লেখা আছে — কোনো কনফিগারেশন প্রয়োজন নেই। [আপনার সেটআপ পরীক্ষা করুন](#আপনার-সেটআপ-পরীক্ষা-করুন) এ যান।

**অপশন B (ম্যানুয়ালি) ব্যবহার করলে**, উদাহরণের `.env` ফাইলটি নিজে তৈরি করুন:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

`.env` ফাইল এডিট করুন আপনার এন্ডপয়েন্ট দিয়ে (কোনো কী নয় — প্রমাণীকরণ keyless):

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

রিসোর্সের Azure OpenAI এন্ডপয়েন্ট ব্যবহার করুন, প্রজেক্ট URL নয়। basic-chat অ্যাপ এটি `/openai/v1` হিসেবে রেজলভ করে এবং একটি স্পেসিফিক বেয়ারার-টোকেন ক্লায়েন্ট কনফিগার করে; API কী প্রয়োজন নেই।

> **সুরক্ষা নোট:** সংরক্ষণ করার জন্য কোনো API কী নেই। আপনি Microsoft Entra ID ব্যবহার করে `az login` (লোকালি) অথবা ম্যানেজড আইডেন্টিটি (Azure এ) এর মাধ্যমে প্রমাণীকৃত হন। `.env` ফাইল শুধুমাত্র নন-সিক্রেট সেটিংস ধারণ করে এবং `.gitignore` দ্বারা ইতিমধ্যে কাভার করা হয়েছে।

## আপনার সেটআপ পরীক্ষা করুন

নিশ্চিত করুন আপনি সাইন ইন করেছেন যাতে keyless auth টোকেন পেতে পারে, তারপর উদাহরণ চালান:

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # যদি আপনি ইতিমধ্যেই সাইন ইন না করে থাকেন
mvn clean spring-boot:run
```

আপনি `gpt-5.6-luna` মডেল থেকে একটি উত্তর দেখতে পাবেন। ডিফল্ট ছোট কোটা মধ্যে থাকার জন্য উদাহরণগুলো ধারাবাহিকভাবে চালান; যদি HTTP 429 পান, আবার চেষ্টা করার আগে পুনরায় চেষ্টা করার জন্য অপেক্ষা করুন।

> **VS কোড ব্যবহারকারীগণ:** চালানোর জন্য `F5` চাপুন। অ্যাপ স্বয়ংক্রিয়ভাবে আপনার `.env` লোড করে।

> **পূর্ণ উদাহরণ:** বিস্তারিত এবং সমস্যা সমাধানের জন্য [Azure AI Foundry এর সাথে Basic Chat উদাহরণ](./examples/basic-chat-azure/README.md) দেখুন।

## পরবর্তী ধাপ কি?

প্রোভিশনিং এবং উদাহরণ সফলভাবে চালানোর পরে, আপনার কাছে থাকবে:
- Azure AI Foundry তে `gpt-5.6-luna` এবং `text-embedding-3-small` ডিপ্লয় হয়েছে
- Keyless প্রমাণীকরণ (Microsoft Entra ID) — কোনো কী ব্যবস্থাপনা নেই
- একটি লোকাল `.env` আপনার এন্ডপয়েন্ট এবং ডিপ্লয়মেন্ট নামের সঙ্গে
- একটি জাভা ডেভেলপমেন্ট পরিবেশ প্রস্তুত

**চালিয়ে যান** [অধ্যায় ৩: কোর জেনেরেটিভ AI কৌশলসমূহ](../03-CoreGenerativeAITechniques/README.md) এ, AI অ্যাপ্লিকেশন তৈরি শুরু করতে!

## রিসোর্স

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [Microsoft Entra ID এর সাথে Keyless প্রমাণীকরণ](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry ডকুমেন্টেশন](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK রূপান্তর](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [Azure OpenAI v1 সহ অফিসিয়াল OpenAI Java SDK](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## অতিরিক্ত রিসোর্স

- [VS কোড ডাউনলোড করুন](https://code.visualstudio.com/Download)
- [ডকার ডেস্কটপ পান](https://www.docker.com/products/docker-desktop)
- [ডেভ কন্টেনার কনফিগারেশন](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত হয়েছে। যদিও আমরা শুদ্ধতার জন্য চেষ্টা করি, অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার স্বভাষায় কর্তৃত্বপূর্ণ উৎস হিসেবে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে প্রয়োজনীয় ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->