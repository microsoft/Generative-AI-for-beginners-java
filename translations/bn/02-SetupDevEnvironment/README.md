<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "c2a244c959e00da1ae1613d2ebfdac65",
  "translation_date": "2025-07-29T14:56:07+00:00",
  "source_file": "02-SetupDevEnvironment/README.md",
  "language_code": "bn"
}
-->
# জেনারেটিভ এআই-এর জন্য জাভা ডেভেলপমেন্ট এনভায়রনমেন্ট সেটআপ

> **দ্রুত শুরু**: ক্লাউডে কোডিং শুরু করুন মাত্র ২ মিনিটে - [GitHub Codespaces Setup](../../../02-SetupDevEnvironment)-এ যান - কোনো লোকাল ইনস্টলেশন প্রয়োজন নেই এবং এটি গিটহাব মডেল ব্যবহার করে!

> **Azure OpenAI-এ আগ্রহী?**, আমাদের [Azure OpenAI Setup Guide](getting-started-azure-openai.md) দেখুন যেখানে নতুন Azure OpenAI রিসোর্স তৈরির ধাপগুলো উল্লেখ করা হয়েছে।

## আপনি কী শিখবেন

- এআই অ্যাপ্লিকেশনের জন্য একটি জাভা ডেভেলপমেন্ট এনভায়রনমেন্ট সেটআপ করবেন
- আপনার পছন্দের ডেভেলপমেন্ট এনভায়রনমেন্ট নির্বাচন এবং কনফিগার করবেন (Codespaces দিয়ে ক্লাউড-ফার্স্ট, লোকাল ডেভ কন্টেইনার, অথবা সম্পূর্ণ লোকাল সেটআপ)
- গিটহাব মডেলের সাথে সংযোগ স্থাপন করে আপনার সেটআপ পরীক্ষা করবেন

## বিষয়সূচি

- [আপনি কী শিখবেন](../../../02-SetupDevEnvironment)
- [ভূমিকা](../../../02-SetupDevEnvironment)
- [ধাপ ১: আপনার ডেভেলপমেন্ট এনভায়রনমেন্ট সেটআপ করুন](../../../02-SetupDevEnvironment)
  - [বিকল্প A: GitHub Codespaces (প্রস্তাবিত)](../../../02-SetupDevEnvironment)
  - [বিকল্প B: লোকাল ডেভ কন্টেইনার](../../../02-SetupDevEnvironment)
  - [বিকল্প C: আপনার বিদ্যমান লোকাল ইনস্টলেশন ব্যবহার করুন](../../../02-SetupDevEnvironment)
- [ধাপ ২: GitHub Personal Access Token তৈরি করুন](../../../02-SetupDevEnvironment)
- [ধাপ ৩: আপনার সেটআপ পরীক্ষা করুন](../../../02-SetupDevEnvironment)
- [সমস্যা সমাধান](../../../02-SetupDevEnvironment)
- [সারাংশ](../../../02-SetupDevEnvironment)
- [পরবর্তী ধাপ](../../../02-SetupDevEnvironment)

## ভূমিকা

এই অধ্যায়টি আপনাকে একটি ডেভেলপমেন্ট এনভায়রনমেন্ট সেটআপ করতে সাহায্য করবে। আমরা **GitHub Models**-কে আমাদের প্রধান উদাহরণ হিসেবে ব্যবহার করব কারণ এটি বিনামূল্যে, শুধুমাত্র একটি গিটহাব অ্যাকাউন্ট দিয়ে সহজে সেটআপ করা যায়, কোনো ক্রেডিট কার্ড প্রয়োজন হয় না, এবং বিভিন্ন মডেলের সাথে পরীক্ষা করার সুযোগ দেয়।

**লোকাল সেটআপের প্রয়োজন নেই!** আপনি সরাসরি GitHub Codespaces ব্যবহার করে ব্রাউজারে কোডিং শুরু করতে পারেন, যা একটি পূর্ণাঙ্গ ডেভেলপমেন্ট এনভায়রনমেন্ট প্রদান করে।

<img src="./images/models.webp" alt="Screenshot: GitHub Models" width="50%">

আমরা এই কোর্সের জন্য [**GitHub Models**](https://github.com/marketplace?type=models) ব্যবহার করার সুপারিশ করছি কারণ এটি:
- **বিনামূল্যে** শুরু করার জন্য
- **সহজ** সেটআপের জন্য, শুধুমাত্র একটি গিটহাব অ্যাকাউন্ট প্রয়োজন
- **কোনো ক্রেডিট কার্ড** প্রয়োজন নেই
- **বিভিন্ন মডেল** পরীক্ষার জন্য উপলব্ধ

> **নোট**: এই প্রশিক্ষণে ব্যবহৃত GitHub Models-এর বিনামূল্যে সীমাবদ্ধতাগুলি হল:
> - প্রতি মিনিটে ১৫টি অনুরোধ (প্রতিদিন ১৫০টি)
> - প্রতি অনুরোধে ~৮,০০০ শব্দ ইনপুট এবং ~৪,০০০ শব্দ আউটপুট
> - ৫টি একযোগে অনুরোধ
> 
> প্রোডাকশন ব্যবহারের জন্য, আপনার Azure অ্যাকাউন্ট দিয়ে Azure AI Foundry Models-এ আপগ্রেড করুন। আপনার কোড পরিবর্তন করার প্রয়োজন নেই। [Azure AI Foundry ডকুমেন্টেশন](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/quickstart-github-models) দেখুন।

## ধাপ ১: আপনার ডেভেলপমেন্ট এনভায়রনমেন্ট সেটআপ করুন

<a name="quick-start-cloud"></a>

আমরা একটি প্রি-কনফিগারড ডেভেলপমেন্ট কন্টেইনার তৈরি করেছি যাতে সেটআপের সময় কমানো যায় এবং এই জেনারেটিভ এআই ফর জাভা কোর্সের জন্য প্রয়োজনীয় সমস্ত টুল অন্তর্ভুক্ত থাকে। আপনার পছন্দের ডেভেলপমেন্ট পদ্ধতি নির্বাচন করুন:

### এনভায়রনমেন্ট সেটআপের বিকল্পসমূহ:

#### বিকল্প A: GitHub Codespaces (প্রস্তাবিত)

**লোকাল সেটআপ ছাড়াই ২ মিনিটে কোডিং শুরু করুন!**

1. এই রিপোজিটরিটি আপনার গিটহাব অ্যাকাউন্টে ফর্ক করুন
   > **নোট**: যদি আপনি বেসিক কনফিগ সম্পাদনা করতে চান, দয়া করে [Dev Container Configuration](../../../.devcontainer/devcontainer.json) দেখুন
2. **Code** → **Codespaces** ট্যাব → **...** → **New with options...**-এ ক্লিক করুন
3. ডিফল্ট সেটিংস ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি **Generative AI Java Development Environment** কাস্টম ডেভকন্টেইনার নির্বাচন করবে
4. **Create codespace**-এ ক্লিক করুন
5. এনভায়রনমেন্ট প্রস্তুত হতে প্রায় ২ মিনিট অপেক্ষা করুন
6. [ধাপ ২: GitHub Token তৈরি করুন](../../../02-SetupDevEnvironment)-এ এগিয়ে যান

<img src="./images/codespaces.png" alt="Screenshot: Codespaces submenu" width="50%">

<img src="./images/image.png" alt="Screenshot: New with options" width="50%">

<img src="./images/codespaces-create.png" alt="Screenshot: Create codespace options" width="50%">

> **Codespaces-এর সুবিধাসমূহ**:
> - লোকাল ইনস্টলেশনের প্রয়োজন নেই
> - যেকোনো ব্রাউজারযুক্ত ডিভাইসে কাজ করে
> - সমস্ত টুল এবং ডিপেন্ডেন্সি প্রি-কনফিগারড
> - ব্যক্তিগত অ্যাকাউন্টের জন্য প্রতি মাসে ৬০ ঘণ্টা বিনামূল্যে
> - সকল শিক্ষার্থীর জন্য একরকম পরিবেশ

#### বিকল্প B: লোকাল ডেভ কন্টেইনার

**ডেভেলপারদের জন্য যারা ডকার দিয়ে লোকাল ডেভেলপমেন্ট পছন্দ করেন**

1. এই রিপোজিটরিটি আপনার লোকাল মেশিনে ফর্ক এবং ক্লোন করুন
   > **নোট**: যদি আপনি বেসিক কনফিগ সম্পাদনা করতে চান, দয়া করে [Dev Container Configuration](../../../.devcontainer/devcontainer.json) দেখুন
2. [Docker Desktop](https://www.docker.com/products/docker-desktop/) এবং [VS Code](https://code.visualstudio.com/) ইনস্টল করুন
3. VS Code-এ [Dev Containers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) ইনস্টল করুন
4. রিপোজিটরি ফোল্ডারটি VS Code-এ খুলুন
5. প্রম্পট আসলে, **Reopen in Container**-এ ক্লিক করুন (অথবা `Ctrl+Shift+P` → "Dev Containers: Reopen in Container" ব্যবহার করুন)
6. কন্টেইনারটি বিল্ড এবং শুরু হওয়ার জন্য অপেক্ষা করুন
7. [ধাপ ২: GitHub Token তৈরি করুন](../../../02-SetupDevEnvironment)-এ এগিয়ে যান

<img src="./images/devcontainer.png" alt="Screenshot: Dev container setup" width="50%">

<img src="./images/image-3.png" alt="Screenshot: Dev container build complete" width="50%">

#### বিকল্প C: আপনার বিদ্যমান লোকাল ইনস্টলেশন ব্যবহার করুন

**যারা বিদ্যমান জাভা এনভায়রনমেন্ট ব্যবহার করেন তাদের জন্য**

প্রয়োজনীয়তা:
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) অথবা আপনার পছন্দের IDE

ধাপসমূহ:
1. এই রিপোজিটরিটি আপনার লোকাল মেশিনে ক্লোন করুন
2. আপনার IDE-তে প্রজেক্টটি খুলুন
3. [ধাপ ২: GitHub Token তৈরি করুন](../../../02-SetupDevEnvironment)-এ এগিয়ে যান

> **প্রো টিপ**: যদি আপনার মেশিনের স্পেসিফিকেশন কম হয় কিন্তু লোকাল VS Code ব্যবহার করতে চান, তাহলে GitHub Codespaces ব্যবহার করুন! আপনি আপনার লোকাল VS Code-কে ক্লাউড-হোস্টেড Codespace-এর সাথে সংযুক্ত করতে পারেন, যা উভয়ের সেরা সুবিধা প্রদান করে।

<img src="./images/image-2.png" alt="Screenshot: created local devcontainer instance" width="50%">

## ধাপ ২: GitHub Personal Access Token তৈরি করুন

1. [GitHub Settings](https://github.com/settings/profile)-এ যান এবং আপনার প্রোফাইল মেনু থেকে **Settings** নির্বাচন করুন।
2. বাম সাইডবারে, **Developer settings**-এ ক্লিক করুন (সাধারণত নিচের দিকে থাকে)।
3. **Personal access tokens**-এর অধীনে, **Fine-grained tokens**-এ ক্লিক করুন (অথবা এই সরাসরি [লিঙ্ক](https://github.com/settings/personal-access-tokens) অনুসরণ করুন)।
4. **Generate new token**-এ ক্লিক করুন।
5. "Token name"-এর অধীনে একটি বর্ণনামূলক নাম দিন (যেমন, `GenAI-Java-Course-Token`)।
6. একটি মেয়াদ শেষ হওয়ার তারিখ নির্ধারণ করুন (নিরাপত্তার জন্য প্রস্তাবিত: ৭ দিন)।
7. "Resource owner"-এর অধীনে আপনার ব্যবহারকারী অ্যাকাউন্ট নির্বাচন করুন।
8. "Repository access"-এর অধীনে, আপনি যে রিপোজিটরিগুলি GitHub Models-এর সাথে ব্যবহার করতে চান সেগুলি নির্বাচন করুন (অথবা প্রয়োজন হলে "All repositories")।
9. "Repository permissions"-এর অধীনে **Models** খুঁজুন এবং এটি **Read and write**-এ সেট করুন।
10. **Generate token**-এ ক্লিক করুন।
11. **এখনই আপনার টোকেনটি কপি এবং সংরক্ষণ করুন** – এটি আপনি আর দেখতে পাবেন না!

> **নিরাপত্তা টিপ**: আপনার অ্যাক্সেস টোকেনের জন্য সর্বনিম্ন প্রয়োজনীয় স্কোপ এবং সবচেয়ে কম মেয়াদ শেষ হওয়ার সময় ব্যবহার করুন।

## ধাপ ৩: GitHub Models উদাহরণ দিয়ে আপনার সেটআপ পরীক্ষা করুন

আপনার ডেভেলপমেন্ট এনভায়রনমেন্ট প্রস্তুত হলে, আমাদের উদাহরণ অ্যাপ্লিকেশনটি [`02-SetupDevEnvironment/examples/github-models`](../../../02-SetupDevEnvironment/examples/github-models) থেকে GitHub Models ইন্টিগ্রেশন পরীক্ষা করুন।

1. আপনার ডেভেলপমেন্ট এনভায়রনমেন্টে টার্মিনাল খুলুন।
2. GitHub Models উদাহরণে যান:
   ```bash
   cd 02-SetupDevEnvironment/examples/github-models
   ```
3. আপনার GitHub টোকেনটি একটি এনভায়রনমেন্ট ভেরিয়েবল হিসেবে সেট করুন:
   ```bash
   # macOS/Linux
   export GITHUB_TOKEN=your_token_here
   
   # Windows (Command Prompt)
   set GITHUB_TOKEN=your_token_here
   
   # Windows (PowerShell)
   $env:GITHUB_TOKEN="your_token_here"
   ```

4. অ্যাপ্লিকেশনটি চালান:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.example.githubmodels.App"
   ```

আপনার আউটপুটটি এরকম হওয়া উচিত:
```text
Using model: gpt-4.1-nano
Sending request to GitHub Models...
Response: Hello World!
```

### উদাহরণ কোডটি বোঝা

প্রথমে, আমরা যা চালিয়েছি তা বুঝে নিই। `examples/github-models`-এর অধীনে উদাহরণটি OpenAI Java SDK ব্যবহার করে GitHub Models-এর সাথে সংযোগ স্থাপন করে:

**এই কোডটি যা করে:**
- আপনার ব্যক্তিগত অ্যাক্সেস টোকেন ব্যবহার করে GitHub Models-এর সাথে **সংযোগ স্থাপন করে**
- এআই মডেলকে একটি সাধারণ "Say Hello World!" বার্তা **পাঠায়**
- এআই-এর প্রতিক্রিয়া **গ্রহণ করে** এবং প্রদর্শন করে
- আপনার সেটআপ সঠিকভাবে কাজ করছে কিনা তা **যাচাই করে**

**মূল নির্ভরতা** (`pom.xml`-এ):
```xml
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
    <version>2.12.0</version>
</dependency>
```

**প্রধান কোড** (`App.java`):
```java
// Connect to GitHub Models using OpenAI Java SDK
OpenAIClient client = OpenAIOkHttpClient.builder()
    .apiKey(pat)
    .baseUrl("https://models.inference.ai.azure.com")
    .build();

// Create chat completion request
ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
    .model(modelId)
    .addSystemMessage("You are a concise assistant.")
    .addUserMessage("Say Hello World!")
    .build();

// Get AI response
ChatCompletion response = client.chat().completions().create(params);
System.out.println("Response: " + response.choices().get(0).message().content().orElse("No response content"));
```

## সারাংশ

অভিনন্দন! আপনি এখন সবকিছু সেটআপ করেছেন:

- AI মডেল অ্যাক্সেসের জন্য সঠিক অনুমতিসহ একটি GitHub Personal Access Token তৈরি করেছেন
- আপনার জাভা ডেভেলপমেন্ট এনভায়রনমেন্ট চালু করেছেন (Codespaces, ডেভ কন্টেইনার, অথবা লোকাল)
- OpenAI Java SDK ব্যবহার করে GitHub Models-এর সাথে সংযোগ স্থাপন করেছেন
- একটি সাধারণ উদাহরণ চালিয়ে নিশ্চিত করেছেন যে সবকিছু সঠিকভাবে কাজ করছে

## পরবর্তী ধাপ

[অধ্যায় ৩: কোর জেনারেটিভ এআই টেকনিকস](../03-CoreGenerativeAITechniques/README.md)

## সমস্যার সমাধান

সমস্যা হচ্ছে? এখানে সাধারণ সমস্যাগুলি এবং সমাধান দেওয়া হলো:

- **টোকেন কাজ করছে না?** 
  - নিশ্চিত করুন যে আপনি পুরো টোকেনটি কপি করেছেন, কোনো অতিরিক্ত স্পেস নেই
  - টোকেনটি সঠিকভাবে একটি এনভায়রনমেন্ট ভেরিয়েবল হিসেবে সেট করা হয়েছে কিনা যাচাই করুন
  - আপনার টোকেনের সঠিক অনুমতি আছে কিনা (Models: Read and write) তা নিশ্চিত করুন

- **Maven পাওয়া যাচ্ছে না?** 
  - যদি ডেভ কন্টেইনার/Codespaces ব্যবহার করেন, Maven প্রি-ইনস্টলড থাকা উচিত
  - লোকাল সেটআপের জন্য, নিশ্চিত করুন যে Java 21+ এবং Maven 3.9+ ইনস্টল করা আছে
  - ইনস্টলেশন যাচাই করতে `mvn --version` চেষ্টা করুন

- **সংযোগ সমস্যা?** 
  - আপনার ইন্টারনেট সংযোগ পরীক্ষা করুন
  - নিশ্চিত করুন যে GitHub আপনার নেটওয়ার্ক থেকে অ্যাক্সেসযোগ্য
  - নিশ্চিত করুন যে আপনি এমন কোনো ফায়ারওয়ালের পেছনে নেই যা GitHub Models এন্ডপয়েন্ট ব্লক করছে

- **ডেভ কন্টেইনার শুরু হচ্ছে না?** 
  - নিশ্চিত করুন যে Docker Desktop চালু আছে (লোকাল ডেভেলপমেন্টের জন্য)
  - কন্টেইনারটি পুনরায় বিল্ড করার চেষ্টা করুন: `Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **অ্যাপ্লিকেশন কম্পাইলেশন ত্রুটি?**
  - নিশ্চিত করুন যে আপনি সঠিক ডিরেক্টরিতে আছেন: `02-SetupDevEnvironment/examples/github-models`
  - ক্লিন এবং রিবিল্ড করার চেষ্টা করুন: `mvn clean compile`

> **সাহায্য প্রয়োজন?**: এখনও সমস্যা হচ্ছে? রিপোজিটরিতে একটি ইস্যু খুলুন এবং আমরা আপনাকে সাহায্য করব।

**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসম্ভব সঠিকতার জন্য চেষ্টা করি, তবে অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল ভাষায় থাকা নথিটিকে প্রামাণিক উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য, পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদ ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যা হলে আমরা তার জন্য দায়ী থাকব না।