<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T11:07:46+00:00",
  "source_file": "README.md",
  "language_code": "bn"
}
-->
# জেনারেটিভ এআই শুরু করার জন্য - জাভা সংস্করণ
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![জেনারেটিভ এআই শুরু করার জন্য - জাভা সংস্করণ](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.bn.png)

**সময় প্রয়োজন**: পুরো ওয়ার্কশপটি অনলাইনে সম্পন্ন করা যায়, স্থানীয় সেটআপ ছাড়াই। নমুনাগুলি চালাতে চাইলে পরিবেশ সেটআপে ২ মিনিট সময় লাগে, এবং নমুনাগুলি অন্বেষণ করতে ১-৩ ঘণ্টা সময় লাগতে পারে, অন্বেষণের গভীরতার উপর নির্ভর করে।

> **দ্রুত শুরু করুন**

1. এই রিপোজিটরিটি আপনার GitHub অ্যাকাউন্টে ফর্ক করুন
2. **Code** → **Codespaces** ট্যাব → **...** → **New with options...** এ ক্লিক করুন
3. ডিফল্ট সেটিংস ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি ডেভেলপমেন্ট কন্টেইনার নির্বাচন করবে
4. **Create codespace** এ ক্লিক করুন
5. পরিবেশ প্রস্তুত হতে প্রায় ২ মিনিট অপেক্ষা করুন
6. সরাসরি [আপনার GitHub মডেল টোকেন তৈরি করা](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token) এ যান

## বহু-ভাষার সমর্থন

### GitHub Action এর মাধ্যমে সমর্থিত (স্বয়ংক্রিয় এবং সর্বদা আপডেটেড)

[French](../fr/README.md) | [Spanish](../es/README.md) | [German](../de/README.md) | [Russian](../ru/README.md) | [Arabic](../ar/README.md) | [Persian (Farsi)](../fa/README.md) | [Urdu](../ur/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Japanese](../ja/README.md) | [Korean](../ko/README.md) | [Hindi](../hi/README.md) | [Bengali](./README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Portuguese (Brazil)](../br/README.md) | [Italian](../it/README.md) | [Polish](../pl/README.md) | [Turkish](../tr/README.md) | [Greek](../el/README.md) | [Thai](../th/README.md) | [Swedish](../sv/README.md) | [Danish](../da/README.md) | [Norwegian](../no/README.md) | [Finnish](../fi/README.md) | [Dutch](../nl/README.md) | [Hebrew](../he/README.md) | [Vietnamese](../vi/README.md) | [Indonesian](../id/README.md) | [Malay](../ms/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Swahili](../sw/README.md) | [Hungarian](../hu/README.md) | [Czech](../cs/README.md) | [Slovak](../sk/README.md) | [Romanian](../ro/README.md) | [Bulgarian](../bg/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Croatian](../hr/README.md) | [Slovenian](../sl/README.md) | [Ukrainian](../uk/README.md) | [Burmese (Myanmar)](../my/README.md)

## কোর্সের কাঠামো এবং শেখার পথ

### **অধ্যায় ১: জেনারেটিভ এআই এর পরিচিতি**
- **মূল ধারণা**: বড় ভাষার মডেল, টোকেন, এমবেডিং এবং এআই এর সক্ষমতা বোঝা
- **জাভা এআই ইকোসিস্টেম**: Spring AI এবং OpenAI SDKs এর সংক্ষিপ্ত বিবরণ
- **মডেল কনটেক্সট প্রোটোকল**: MCP এবং এআই এজেন্ট যোগাযোগে এর ভূমিকা
- **ব্যবহারিক প্রয়োগ**: বাস্তব জীবনের উদাহরণ যেমন চ্যাটবট এবং কন্টেন্ট জেনারেশন
- **[→ অধ্যায় ১ শুরু করুন](./01-IntroToGenAI/README.md)**

### **অধ্যায় ২: ডেভেলপমেন্ট পরিবেশ সেটআপ**
- **বহু-প্রোভাইডার কনফিগারেশন**: GitHub Models, Azure OpenAI এবং OpenAI Java SDK ইন্টিগ্রেশন সেটআপ করুন
- **Spring Boot + Spring AI**: এন্টারপ্রাইজ এআই অ্যাপ্লিকেশন ডেভেলপমেন্টের সেরা পদ্ধতি
- **GitHub Models**: প্রোটোটাইপিং এবং শেখার জন্য বিনামূল্যে এআই মডেল অ্যাক্সেস (ক্রেডিট কার্ড প্রয়োজন নেই)
- **ডেভেলপমেন্ট টুলস**: Docker কন্টেইনার, VS Code এবং GitHub Codespaces কনফিগারেশন
- **[→ অধ্যায় ২ শুরু করুন](./02-SetupDevEnvironment/README.md)**

### **অধ্যায় ৩: মূল জেনারেটিভ এআই কৌশল**
- **প্রম্পট ইঞ্জিনিয়ারিং**: এআই মডেলের সেরা প্রতিক্রিয়ার জন্য কৌশল
- **এমবেডিং এবং ভেক্টর অপারেশন**: সেমান্টিক সার্চ এবং সাদৃশ্য মিলানোর বাস্তবায়ন
- **রিট্রিভাল-অগমেন্টেড জেনারেশন (RAG)**: আপনার নিজস্ব ডেটা সোর্সের সাথে এআই সংযুক্ত করুন
- **ফাংশন কলিং**: কাস্টম টুল এবং প্লাগইন দিয়ে এআই এর সক্ষমতা বাড়ান
- **[→ অধ্যায় ৩ শুরু করুন](./03-CoreGenerativeAITechniques/README.md)**

### **অধ্যায় ৪: ব্যবহারিক প্রয়োগ এবং প্রকল্প**
- **পোষা প্রাণীর গল্প জেনারেটর** (`petstory/`): GitHub Models দিয়ে সৃজনশীল কন্টেন্ট জেনারেশন
- **Foundry Local Demo** (`foundrylocal/`): OpenAI Java SDK দিয়ে স্থানীয় এআই মডেল ইন্টিগ্রেশন
- **MCP ক্যালকুলেটর সার্ভিস** (`mcp/calculator/`): Spring AI দিয়ে মৌলিক মডেল কনটেক্সট প্রোটোকল বাস্তবায়ন
- **[→ অধ্যায় ৪ শুরু করুন](./04-PracticalSamples/README.md)**

### **অধ্যায় ৫: দায়িত্বশীল এআই ডেভেলপমেন্ট**
- **GitHub Models নিরাপত্তা**: অন্তর্নির্মিত কন্টেন্ট ফিল্টারিং এবং নিরাপত্তা ব্যবস্থা পরীক্ষা করুন
- **দায়িত্বশীল এআই ডেমো**: হাতে-কলমে উদাহরণ যা দেখায় কিভাবে এআই নিরাপত্তা ফিল্টার কাজ করে
- **সেরা পদ্ধতি**: নৈতিক এআই ডেভেলপমেন্ট এবং ডিপ্লয়মেন্টের জন্য অপরিহার্য নির্দেশিকা
- **[→ অধ্যায় ৫ শুরু করুন](./05-ResponsibleGenAI/README.md)**

## অতিরিক্ত সম্পদ

- [শুরু করার জন্য এআই এজেন্ট](https://github.com/microsoft/ai-agents-for-beginners)
- [জেনারেটিভ এআই শুরু করার জন্য .NET ব্যবহার করে](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [জেনারেটিভ এআই শুরু করার জন্য জাভাস্ক্রিপ্ট ব্যবহার করে](https://github.com/microsoft/generative-ai-with-javascript)
- [জেনারেটিভ এআই শুরু করার জন্য](https://github.com/microsoft/generative-ai-for-beginners)
- [শুরু করার জন্য মেশিন লার্নিং](https://aka.ms/ml-beginners)
- [শুরু করার জন্য ডেটা সায়েন্স](https://aka.ms/datascience-beginners)
- [শুরু করার জন্য এআই](https://aka.ms/ai-beginners)
- [শুরু করার জন্য সাইবারসিকিউরিটি](https://github.com/microsoft/Security-101)
- [শুরু করার জন্য ওয়েব ডেভেলপমেন্ট](https://aka.ms/webdev-beginners)
- [শুরু করার জন্য IoT](https://aka.ms/iot-beginners)
- [শুরু করার জন্য XR ডেভেলপমেন্ট](https://github.com/microsoft/xr-development-for-beginners)
- [GitHub Copilot এর মাধ্যমে এআই পেয়ারড প্রোগ্রামিং আয়ত্ত করা](https://aka.ms/GitHubCopilotAI)
- [C#/.NET ডেভেলপারদের জন্য GitHub Copilot আয়ত্ত করা](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [আপনার নিজস্ব Copilot অ্যাডভেঞ্চার নির্বাচন করুন](https://github.com/microsoft/CopilotAdventures)
- [Azure AI Services দিয়ে RAG চ্যাট অ্যাপ](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**অস্বীকৃতি**:  
এই নথিটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসম্ভব সঠিক অনুবাদ প্রদানের চেষ্টা করি, তবে অনুগ্রহ করে মনে রাখবেন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল ভাষায় থাকা নথিটিকে প্রামাণিক উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের জন্য, পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদ ব্যবহারের ফলে কোনো ভুল বোঝাবুঝি বা ভুল ব্যাখ্যা হলে আমরা তার জন্য দায়ী থাকব না।