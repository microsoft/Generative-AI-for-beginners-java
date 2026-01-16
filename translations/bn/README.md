<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "459109e7c925f3a7b94440ad61c596a0",
  "translation_date": "2026-01-05T09:05:03+00:00",
  "source_file": "README.md",
  "language_code": "bn"
}
-->
# নবপ্রবীণদের জন্য জেনেরেটিভ এআই - জাভা সংস্করণ
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![নবপ্রবীণদের জন্য জেনেরেটিভ এআই - জাভা সংস্করণ](../../translated_images/bn/beg-genai-series.8b48be9951cc574c.webp)

**সময় বরাদ্দ**: সম্পূর্ণ কর্মশালাটি অনলাইনে স্থানীয় সেটআপ ছাড়াই সম্পন্ন করা যেতে পারে। পরিবেশ সেটআপ করতে ২ মিনিট সময় লাগে, নমুনাগুলি অন্বেষণ করতে ১-৩ ঘণ্টা সময় লাগতে পারে যা অন্বেষণ গভীরতার উপর নির্ভর করে।

> **দ্রুত শুরু**

১. এই রিপোজিটরিটি আপনার গিটহাব অ্যাকাউন্টে ফর্ক করুন  
২. ক্লিক করুন **Code** → **Codespaces** ট্যাব → **...** → **New with options...**  
৩. ডিফল্ট ব্যবহার করুন – এটি এই কোর্সের জন্য তৈরি করা ডেভেলপমেন্ট কন্টেইনার নির্বাচন করবে  
৪. ক্লিক করুন **Create codespace**  
৫. পরিবেশ প্রস্তুত হতে প্রায় ২ মিনিট অপেক্ষা করুন  
৬. সরাসরি লাফ দিন [প্রথম উদাহরণের দিকে](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)  

> **লোকাল-এ ক্লোন করতে পছন্দ করেন?**  
>  
> এই রিপোজিটরিতে ৫০+ ভাষার অনুবাদ অন্তর্ভুক্ত রয়েছে যা ডাউনলোড সাইজ উল্লেখযোগ্যভাবে বৃদ্ধি করে। অনুবাদের ছাড়া ক্লোন করতে sparse checkout ব্যবহার করুন:  
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> এটি আপনাকে কোর্স সম্পন্ন করার জন্য প্রয়োজনীয় সবকিছু ভালো হারে ডাউনলোড করতে দিবে।


## বহু-ভাষার সমর্থন

### গিটহাব অ্যাকশনের মাধ্যমে সমর্থিত (স্বয়ংক্রিয় ও সর্বদা আপ-টু-ডেট)

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[আরবি](../ar/README.md) | [বাংলা](./README.md) | [বুলগেরিয়ান](../bg/README.md) | [বার্মিজ (মায়ানমার)](../my/README.md) | [চীনা (সরলীকৃত)](../zh/README.md) | [চীনা (প্রচলিত, হংকং)](../hk/README.md) | [চীনা (প্রচলিত, মাকাও)](../mo/README.md) | [চীনা (প্রচলিত, তাইওয়ান)](../tw/README.md) | [ক্রোয়েশিয়ান](../hr/README.md) | [চেক](../cs/README.md) | [ডেনিশ](../da/README.md) | [ডাচ](../nl/README.md) | [এস্টোনিয়ান](../et/README.md) | [ফিনিশ](../fi/README.md) | [ফরাসি](../fr/README.md) | [জার্মান](../de/README.md) | [গ্রিক](../el/README.md) | [হিব্রু](../he/README.md) | [হিন্দি](../hi/README.md) | [হাঙ্গেরিয়ান](../hu/README.md) | [ইন্দোনেশিয়ান](../id/README.md) | [ইতালিয়ান](../it/README.md) | [জাপানিস](../ja/README.md) | [কন্নড়](../kn/README.md) | [কোরিয়ান](../ko/README.md) | [লিথুয়ানিয়ান](../lt/README.md) | [মালয়](../ms/README.md) | [মলয়ালম](../ml/README.md) | [মারাঠি](../mr/README.md) | [নেপালী](../ne/README.md) | [নাইজেরিয়ান পিজিন](../pcm/README.md) | [নরওয়েজিয়ান](../no/README.md) | [ফার্সি](../fa/README.md) | [পোলিশ](../pl/README.md) | [পর্তুগিজ (ব্রাজিল)](../br/README.md) | [পর্তুগিজ (পর্তুগাল)](../pt/README.md) | [পাঞ্জাবি (গুরুমুখী)](../pa/README.md) | [রোমানিয়ান](../ro/README.md) | [রাশিয়ান](../ru/README.md) | [সার্বিয়ান (সিরিলিক)](../sr/README.md) | [স্লোভাক](../sk/README.md) | [স্লোভেনিয়ান](../sl/README.md) | [স্প্যানিশ](../es/README.md) | [সোয়াহিলি](../sw/README.md) | [সুইডিশ](../sv/README.md) | [টাগালগ (ফিলিপিনো)](../tl/README.md) | [তামিল](../ta/README.md) | [তেলুগু](../te/README.md) | [থাই](../th/README.md) | [তুর্কি](../tr/README.md) | [ইউক্রেনিয়ান](../uk/README.md) | [উর্দু](../ur/README.md) | [ভিয়েতনামী](../vi/README.md)

> **লোকাল-এ ক্লোন করতে পছন্দ করেন?**  

> এই রিপোজিটরিতে ৫০+ ভাষার অনুবাদ অন্তর্ভুক্ত রয়েছে যা ডাউনলোড সাইজ উল্লেখযোগ্যভাবে বৃদ্ধি করে। অনুবাদের ছাড়া ক্লোন করতে sparse checkout ব্যবহার করুন:  
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> এটি আপনাকে কোর্স সম্পন্ন করার জন্য প্রয়োজনীয় সবকিছু ভালো হারে ডাউনলোড করতে দিবে।  
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## কোর্স কাঠামো ও শেখার পথ

### **অধ্যায় ১: জেনেরেটিভ এআই পরিচিতি**  
- **প্রাথমিক ধারণা**: বড় ভাষা মডেল, টোকেন, এম্বেডিংস এবং এআই সক্ষমতা বোঝা  
- **জাভা এআই ইকোসিস্টেম**: স্প্রিং এআই ও ওপেনএআই এসডিকেগুলোর সংক্ষিপ্ত বিবরণ  
- **মডেল কন্টেক্সট প্রোটোকল**: MCP পরিচিতি এবং এটি কীভাবে এআই এজেন্টের সাথে যোগাযোগ করে  
- **প্রায়োগিক ব্যবহার**: চ্যাটবট ও বিষয়বস্তু সৃষ্টিসহ বাস্তব বিশ্বে প্রয়োগ  
- **[→ অধ্যায় ১ শুরু করুন](./01-IntroToGenAI/README.md)**

### **অধ্যায় ২: ডেভেলপমেন্ট পরিবেশ সেটআপ**  
- **মাল্টি-প্রোভাইডার কনফিগারেশন**: গিটহাব মডেল, আজুর ওপেনএআই, ও ওপেনএআই জাভা এসডিকে সেটআপ  
- **স্প্রিং বুট + স্প্রিং এআই**: এন্টারপ্রাইজ এআই অ্যাপ্লিকেশন ডেভেলপমেন্টের সেরা পদ্ধতি  
- **গিটহাব মডেল**: প্রোটোটাইপিং ও শেখার জন্য ফ্রি এআই মডেল অ্যাক্সেস (কোনো ক্রেডিট কার্ড দরকার নয়)  
- **ডেভেলপমেন্ট টুলস**: ডকার কন্টেইনার, ভিএস কোড, ও গিটহাব কোডস্পেসেস কনফিগারেশন  
- **[→ অধ্যায় ২ শুরু করুন](./02-SetupDevEnvironment/README.md)**

### **অধ্যায় ৩: মূল জেনেরেটিভ এআই কৌশল**  
- **প্রম্পট ইঞ্জিনিয়ারিং**: আদর্শ এআই মডেল প্রতিক্রিয়া পাওয়ার কৌশল  
- **এম্বেডিংস ও ভেক্টর অপারেশন**: সেমান্টিক অনুসন্ধান ও সাদৃশ্য মিলানো কার্যকরী করা  
- **রিট্রিভাল-অগমেন্টেড জেনারেশন (RAG)**: নিজের ডেটা উৎসের সঙ্গে এআই একত্রিত করুন  
- **ফাংশন কলিং**: কাস্টম সরঞ্জাম ও প্লাগইন দিয়ে এআই সক্ষমতা বাড়ানো  
- **[→ অধ্যায় ৩ শুরু করুন](./03-CoreGenerativeAITechniques/README.md)**

### **অধ্যায় ৪: প্রাযুক্তিক ব্যবহার ও প্রকল্পসমূহ**  
- **পেট স্টোরি জেনারেটর** (`petstory/`): গিটহাব মডেলের সাহায্যে সৃজনশীল বিষয়বস্তু সৃষ্টি  
- **ফাউন্ড্রি লোকাল ডেমো** (`foundrylocal/`): লোকাল এআই মডেলের ওপেনএআই জাভা এসডিকে সহযোগিতায় সংযুক্তি  
- **MCP ক্যালকুলেটর সার্ভিস** (`calculator/`): স্প্রিং এআই দিয়ে মডেল কন্টেক্সট প্রোটোকলের মৌলিক বাস্তবায়ন  
- **[→ অধ্যায় ৪ শুরু করুন](./04-PracticalSamples/README.md)**

### **অধ্যায় ৫: দায়িত্বশীল এআই ডেভেলপমেন্ট**  
- **গিটহাব মডেল নিরাপত্তা**: বিল্ট-ইন বিষয়বস্তু ফিল্টারিং ও সেফটি ব্যবস্থা (হার্ড ব্লক ও সফট প্রত্যাখ্যান) পরীক্ষা করা  
- **দায়িত্বশীল এআই ডেমো**: আধুনিক এআই সেফটি সিস্টেমগুলি কীভাবে কাজ করে তা হাতে-কলমে উদাহরণ  
- **সেরা অনুশীলন**: নৈতিক এআই ডেভেলপমেন্ট ও ডেপ্লয়মেন্টের অপরিহার্য নির্দেশিকা  
- **[→ অধ্যায় ৫ শুরু করুন](./05-ResponsibleGenAI/README.md)**

## অতিরিক্ত সম্পদসমূহ

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### ল্যাংচেইন
[![নবপ্রবীণদের জন্য LangChain4j](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)  
[![নবপ্রবীণদের জন্য LangChain.js](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### আজুর / এজ / MCP / এজেন্টস
[![নবপ্রবীণদের জন্য AZD](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![নবপ্রবীণদের জন্য এজ এআই](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![নবপ্রবীণদের জন্য MCP](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![নবপ্রবীণদের জন্য এআই এজেন্টস](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### জেনেরেটিভ এআই সিরিজ
[![নবপ্রবীণদের জন্য জেনেরেটিভ এআই](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)  
[![জেনেরেটিভ এআই (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)  
[![জেনেরেটিভ এআই (জাভা)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)  
[![জেনেরেটিভ এআই (জাভাস্ক্রিপ্ট)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### মূল পাঠ্যক্রম
[![নবপ্রবীণদের জন্য এমএল](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)  
[![নবপ্রবীণদের জন্য ডেটা সায়েন্স](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)  
[![নবপ্রবীণদের জন্য এআই](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)  
[![নবপ্রবীণদের জন্য সাইবারসিকিউরিটি](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)  
[![নবপ্রবীণদের জন্য ওয়েব ডেভেলপমেন্ট](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরু করার জন্য IoT](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![শুরু করার জন্য XR ডেভেলপমেন্ট](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### কপিলট সিরিজ
[![AI জোড়া প্রোগ্রামিংয়ের জন্য কপিলট](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET এর জন্য কপিলট](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![কপিলট অ্যাডভেঞ্চার](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## সাহায্য নেওয়া

যদি আপনি আটকে যান বা AI অ্যাপ তৈরি সম্পর্কে কোনো প্রশ্ন থাকে। MCP সম্পর্কে আলোচনা করার জন্য সহপাঠী শিক্ষার্থী এবং দক্ষ ডেভেলপারদের সঙ্গে যোগ দিন। এটি একটি সমর্থনশীল কমিউনিটি যেখানে প্রশ্ন করাটা স্বাগত এবং জ্ঞানের বিনিময় অবাধ।

[![মাইক্রোসফ্ট ফাউন্ড্রি ডিসকর্ড](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

আপনার যদি পণ্য ফিডব্যাক বা নির্মাণ চলাকালে কোনো ত্রুটি থাকে তবে দেখুন:

[![মাইক্রোসফ্ট ফাউন্ড্রি ডেভেলপার ফোরাম](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**অস্বীকৃতি**:
এই নথিটি AI অনুবাদ সেবা [Co-op Translator](https://github.com/Azure/co-op-translator) ব্যবহার করে অনুবাদ করা হয়েছে। আমরা যথাসাধ্য সঠিকতার চেষ্টা করি, তবে স্বয়ংক্রিয় অনুবাদে ভুল বা অসঙ্গতি থাকতে পারে। মূল নথিটি তার স্বতন্ত্র ভাষায় সর্বোত্তম সূত্র বলে বিবেচিত হওয়া উচিত। গুরুত্বপূর্ণ তথ্যের জন্য পেশাদার মানব অনুবাদ সুপারিশ করা হয়। এই অনুবাদের ব্যবহারে কোনো误ব্যবধান বা ভুল বোঝাবুঝির জন্য আমরা দায়বদ্ধ নই।
<!-- CO-OP TRANSLATOR DISCLAIMER END -->