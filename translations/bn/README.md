<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:23:16+00:00",
  "source_file": "README.md",
  "language_code": "bn"
}
-->
# শুরুকারীদের জন্য জেনারেটিভ AI - জাভা সংস্করণ
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![শুরুকারীদের জন্য জেনারেটিভ AI - জাভা সংস্করণ](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.bn.png)

**সময়ের প্রয়োজন**: সম্পূর্ণ কর্মশালাটি অনলাইনে লোকাল সেটআপ ছাড়া সম্পন্ন করা যায়। পরিবেশ সেটআপে ২ মিনিট লাগে, নমুনাগুলি অন্বেষণের জন্য ১-৩ ঘন্টা লাগতে পারে অন্বেষণের গভরের উপর নির্ভর করে।

> **দ্রুত শুরু** 

1. এই রিপোজিটরিটি আপনার GitHub অ্যাকাউন্টে Fork করুন
2. ক্লিক করুন **Code** → **Codespaces** ট্যাব → **...** → **New with options...**
3. ডিফল্ট সেটিংস ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি Development container নির্বাচন করবে
4. ক্লিক করুন **Create codespace**
5. পরিবেশ প্রস্তুত হতে প্রায় ২ মিনিট অপেক্ষা করুন
6. সরাসরি যান [প্রথম উদাহরণ](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **স্থানীয়ভাবে ক্লোন করতে চান?**
>
> এই রিপোজিটরিতে ৫০+ ভাষার অনুবাদ রয়েছে যা ডাউনলোডের আকার উল্লেখযোগ্যভাবে বৃদ্ধি করে। অনুবাদ ছাড়া ক্লোন করতে sparse checkout ব্যবহার করুন:
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> এটি আপনাকে কোর্সটি সম্পন্ন করার জন্য প্রয়োজনীয় সবকিছু দেবে এবং ডাউনলোড অনেক দ্রুত হবে।

## বহু-ভাষা সমর্থন

### GitHub Action দ্বারা সমর্থিত (স্বয়ংক্রিয় ও সবসময় আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[Arabic](../ar/README.md) | [Bengali](./README.md) | [Bulgarian](../bg/README.md) | [Burmese (Myanmar)](../my/README.md) | [Chinese (Simplified)](../zh/README.md) | [Chinese (Traditional, Hong Kong)](../hk/README.md) | [Chinese (Traditional, Macau)](../mo/README.md) | [Chinese (Traditional, Taiwan)](../tw/README.md) | [Croatian](../hr/README.md) | [Czech](../cs/README.md) | [Danish](../da/README.md) | [Dutch](../nl/README.md) | [Estonian](../et/README.md) | [Finnish](../fi/README.md) | [French](../fr/README.md) | [German](../de/README.md) | [Greek](../el/README.md) | [Hebrew](../he/README.md) | [Hindi](../hi/README.md) | [Hungarian](../hu/README.md) | [Indonesian](../id/README.md) | [Italian](../it/README.md) | [Japanese](../ja/README.md) | [Kannada](../kn/README.md) | [Korean](../ko/README.md) | [Lithuanian](../lt/README.md) | [Malay](../ms/README.md) | [Malayalam](../ml/README.md) | [Marathi](../mr/README.md) | [Nepali](../ne/README.md) | [Nigerian Pidgin](../pcm/README.md) | [Norwegian](../no/README.md) | [Persian (Farsi)](../fa/README.md) | [Polish](../pl/README.md) | [Portuguese (Brazil)](../br/README.md) | [Portuguese (Portugal)](../pt/README.md) | [Punjabi (Gurmukhi)](../pa/README.md) | [Romanian](../ro/README.md) | [Russian](../ru/README.md) | [Serbian (Cyrillic)](../sr/README.md) | [Slovak](../sk/README.md) | [Slovenian](../sl/README.md) | [Spanish](../es/README.md) | [Swahili](../sw/README.md) | [Swedish](../sv/README.md) | [Tagalog (Filipino)](../tl/README.md) | [Tamil](../ta/README.md) | [Telugu](../te/README.md) | [Thai](../th/README.md) | [Turkish](../tr/README.md) | [Ukrainian](../uk/README.md) | [Urdu](../ur/README.md) | [Vietnamese](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## কোর্স কাঠামো ও শেখার পথ

### **অধ্যায় ১: জেনারেটিভ AI পরিচিতি**
- **মূল ধারণা**: বৃহৎ ভাষা মডেল, টোকেন, এমবেডিংস, এবং এআই-এর ক্ষমতা সম্পর্কে বোঝাপড়া
- **জাভা এআই ইকোসিস্টেম**: Spring AI এবং OpenAI SDKs-এর পরিচিতি
- **মডেল কনটেক্সট প্রটোকল**: MCP-এর পরিচিতি এবং এআই এজেন্টদের মধ্যে যোগাযোগে এর ভূমিকা
- **প্রায়োগিক অ্যাপ্লিকেশন**: চ্যাটবট এবং কনটেন্ট জেনারেশনসহ বাস্তবজগতের উদাহরণ
- **[→ অধ্যায় ১ শুরু করুন](./01-IntroToGenAI/README.md)**

### **অধ্যায় ২: ডেভেলপমেন্ট পরিবেশ সেটআপ**
- **বহু-প্রোভাইডার কনফিগারেশন**: GitHub Models, Azure OpenAI, এবং OpenAI Java SDK ইন্টিগ্রেশন সেটআপ
- **Spring Boot + Spring AI**: এন্টারপ্রাইজ AI অ্যাপ্লিকেশন ανάπτυনের সেরা অনুশীলনসমূহ
- **GitHub Models**: প্রোটোটাইপিং ও শেখার জন্য ফ্রি AI মডেল অ্যাক্সেস (ক্রেডিট কার্ড দরকার নেই)
- **ডেভেলপমেন্ট টুলস**: Docker কন্টেইনার, VS Code, এবং GitHub Codespaces কনফিগারেশন
- **[→ অধ্যায় ২ শুরু করুন](./02-SetupDevEnvironment/README.md)**

### **অধ্যায় ৩: মৌলিক জেনারেটিভ AI কৌশলসমূহ**
- **প্রম্পট ইঞ্জিনিয়ারিং**: AI মডেল থেকে সর্বোত্তম প্রতিক্রিয়া পেতে কৌশলসমূহ
- **এমবেডিংস ও ভেক্টর অপারেশন**: সেমান্টিক সার্চ ও সাদৃশ্য মিলানোর বাস্তবায়ন
- **রিট্রিভাল-অগমেন্টেড জেনারেশন (RAG)**: আপনার নিজস্ব ডেটা সোর্সগুলোর সাথে AI একত্রিত করা
- **ফাংশন কলিং**: কাস্টম টুলস ও প্লাগইন দিয়ে AI ক্ষমতা বাড়ানো
- **[→ অধ্যায় ৩ শুরু করুন](./03-CoreGenerativeAITechniques/README.md)**

### **অধ্যায় ৪: প্রায়োগিক অ্যাপ্লিকেশন ও প্রকল্পসমূহ**
- **পেট স্টোরি জেনারেটর** (`petstory/`): GitHub Models ব্যবহার করে সৃজনশীল কনটেন্ট জেনারেশন
- **Foundry Local ডেমো** (`foundrylocal/`): OpenAI Java SDK-এর সাথে লোকাল AI মডেল ইন্টিগ্রেশন
- **MCP ক্যালকুলেটর সার্ভিস** (`calculator/`): Spring AI দিয়ে বেসিক Model Context Protocol বাস্তবায়ন
- **[→ অধ্যায় ৪ শুরু করুন](./04-PracticalSamples/README.md)**

### **অধ্যায় ৫: দায়িত্বশীল AI উন্নয়ন**
- **GitHub Models নিরাপত্তা**: বিল্ট-ইন কনটেন্ট ফিল্টারিং ও সেফটি মেকানিজম (হার্ড ব্লক ও সফট রিফিউজাল) পরীক্ষা করুন
- **দায়িত্বশীল AI ডেমো**: আধুনিক AI সেফটি সিস্টেম কিভাবে বাস্তবে কাজ করে তা প্রদর্শন
- **সেরা অনুশীলনসমূহ**: নৈতিক AI উন্নয়ন ও ডেপ্লয়মেন্টের অপরিহার্য নির্দেশিকা
- **[→ অধ্যায় ৫ শুরু করুন](./05-ResponsibleGenAI/README.md)**

## অতিরিক্ত সম্পদ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j for Beginners](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js for Beginners](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD for Beginners](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI for Beginners](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP for Beginners](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI Agents for Beginners](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### জেনারেটিভ AI সিরিজ
[![Generative AI for Beginners](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Generative AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![Generative AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![Generative AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### মূল শিক্ষণ
[![ML for Beginners](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![Data Science for Beginners](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI for Beginners](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![Cybersecurity for Beginners](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![Web Dev for Beginners](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![নবীনদের জন্য IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![নবীনদের জন্য XR ডেভেলপমেন্ট](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### কপাইলট সিরিজ
[![AI যুগল প্রোগ্রামিংয়ের জন্য Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET-এর জন্য Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot অ্যাডভেঞ্চার](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সহায়তা

আপনি যদি আটকে যান বা AI অ্যাপ তৈরি করার বিষয়ে কোনো প্রশ্ন থাকে। MCP সম্পর্কিত আলোচনায় সহশিক্ষার্থী এবং অভিজ্ঞ ডেভেলপারদের সাথে যোগ দিন। এটি একটি সহায়ক সম্প্রদায় যেখানে প্রশ্নকে স্বাগত জানানো হয় এবং জ্ঞান মুক্তভাবে ভাগ করা হয়।

[![Microsoft Foundry ডিসকর্ড](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

If you have product feedback or errors while building visit:

[![Microsoft Foundry ডেভেলপার ফোরাম](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
অস্বীকৃতি:
এই দলিলটি AI অনুবাদ পরিষেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনূদিত। যদিও আমরা যথাসাধ্য সঠিকতা নিশ্চিত করার চেষ্টা করি, অনুগ্রহ করে লক্ষ্য করুন যে স্বয়ংক্রিয় অনুবাদে ত্রুটি বা অসঙ্গতি থাকতে পারে। মূল ভাষায় থাকা মূল দলিলটিকেই প্রামাণ্য উৎস হিসেবে বিবেচনা করা উচিত। গুরুত্বপূর্ণ তথ্যের ক্ষেত্রে পেশাদার মানব অনুবাদ প্রয়োজন। এই অনুবাদ ব্যবহারের ফলে যে কোনও ভুল বোঝাবুঝি বা ভুল ব্যাখ্যার জন্য আমরা দায়ী নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->