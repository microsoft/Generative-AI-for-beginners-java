<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "64843fea021344d8e889dae94f46a9be",
  "translation_date": "2025-12-25T05:12:16+00:00",
  "source_file": "README.md",
  "language_code": "tw"
}
-->
# 生成式 AI 初學者指南 - Java 版
[![Microsoft Foundry 的 Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![生成式 AI 初學者指南 - Java 版](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.tw.png)

**時間投入**：整個工作坊可在線上完成，無需本機設定。環境設定約需 2 分鐘，瀏覽範例視探索深度需要 1–3 小時。

> **快速開始** 

1. 將此儲存庫 Fork 到你的 GitHub 帳戶
2. 點選 **Code** → **Codespaces** 標籤 → **...** → **New with options...**
3. 使用預設值 — 這會選擇為本課程建立的開發容器
4. 點選 **Create codespace**
5. 等待約 2 分鐘，環境即會就緒
6. 直接前往 [→ 開始第2章](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

> **想要在本機 Clone？**
>
> 此儲存庫包含 50+ 種語言的翻譯，會大幅增加下載大小。若要在沒有翻譯檔案的情況下 clone，請使用 sparse checkout：
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
> 這會提供完成課程所需的一切，且下載速度更快。


## 多語言支援

### 透過 GitHub Action 支援（自動化且隨時更新）

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[阿拉伯語](../ar/README.md) | [孟加拉語](../bn/README.md) | [保加利亞語](../bg/README.md) | [緬甸語（緬甸）](../my/README.md) | [中文（簡體）](../zh/README.md) | [中文（繁體，香港）](../hk/README.md) | [中文（繁體，澳門）](../mo/README.md) | [中文（繁體，台灣）](./README.md) | [克羅埃西亞語](../hr/README.md) | [捷克語](../cs/README.md) | [丹麥語](../da/README.md) | [荷蘭語](../nl/README.md) | [愛沙尼亞語](../et/README.md) | [芬蘭語](../fi/README.md) | [法語](../fr/README.md) | [德語](../de/README.md) | [希臘語](../el/README.md) | [希伯來語](../he/README.md) | [印地語](../hi/README.md) | [匈牙利語](../hu/README.md) | [印尼語](../id/README.md) | [義大利語](../it/README.md) | [日語](../ja/README.md) | [坎那達語](../kn/README.md) | [韓語](../ko/README.md) | [立陶宛語](../lt/README.md) | [馬來語](../ms/README.md) | [馬拉雅拉姆語](../ml/README.md) | [馬拉地語](../mr/README.md) | [尼泊爾語](../ne/README.md) | [尼日利亞皮欽語](../pcm/README.md) | [挪威語](../no/README.md) | [波斯語（法爾西）](../fa/README.md) | [波蘭語](../pl/README.md) | [葡萄牙語（巴西）](../br/README.md) | [葡萄牙語（葡萄牙）](../pt/README.md) | [旁遮普語（Gurmukhi）](../pa/README.md) | [羅馬尼亞語](../ro/README.md) | [俄語](../ru/README.md) | [塞爾維亞語（西里爾文）](../sr/README.md) | [斯洛伐克語](../sk/README.md) | [斯洛維尼亞語](../sl/README.md) | [西班牙語](../es/README.md) | [斯瓦希里語](../sw/README.md) | [瑞典語](../sv/README.md) | [他加祿語（菲律賓語）](../tl/README.md) | [泰米爾語](../ta/README.md) | [泰盧固語](../te/README.md) | [泰語](../th/README.md) | [土耳其語](../tr/README.md) | [烏克蘭語](../uk/README.md) | [烏爾都語](../ur/README.md) | [越南語](../vi/README.md)
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## 課程結構與學習路徑

### **第1章：生成式 AI 介紹**
- **核心概念**：了解大型語言模型、tokens、嵌入（embeddings）及 AI 能力
- **Java AI 生態系**：Spring AI 與 OpenAI SDKs 概覽
- **模型上下文協定**：介紹 MCP 及其在 AI 代理通訊中的角色
- **實務應用**：實際情境，例如聊天機器人與內容生成
- **[→ 開始第1章](./01-IntroToGenAI/README.md)**

### **第2章：開發環境設定**
- **多供應者設定**：設定 GitHub Models、Azure OpenAI 與 OpenAI Java SDK 整合
- **Spring Boot + Spring AI**：企業級 AI 應用開發最佳實務
- **GitHub Models**：用於原型與學習的免費 AI 模型（不需信用卡）
- **開發工具**：Docker 容器、VS Code 與 GitHub Codespaces 設定
- **[→ 開始第2章](./02-SetupDevEnvironment/README.md)**

### **第3章：生成式 AI 核心技術**
- **提示工程**：取得最佳 AI 模型回應的技巧
- **嵌入與向量運算**：實作語意搜尋與相似度比對
- **檢索增強生成（RAG）**：將 AI 與你的資料來源結合
- **函式呼叫**：透過自訂工具與外掛擴充 AI 能力
- **[→ 開始第3章](./03-CoreGenerativeAITechniques/README.md)**

### **第4章：實務應用與專案**
- **Pet Story Generator** (`petstory/`)：使用 GitHub Models 的創意內容生成
- **Foundry Local Demo** (`foundrylocal/`)：使用 OpenAI Java SDK 的本地 AI 模型整合範例
- **MCP Calculator Service** (`calculator/`)：使用 Spring AI 的基本 Model Context Protocol 實作
- **[→ 開始第4章](./04-PracticalSamples/README.md)**

### **第5章：負責任的 AI 開發**
- **GitHub Models 的安全性**：測試內建內容過濾與安全機制（硬性封鎖與軟性拒絕）
- **負責任 AI 示範**：實務範例展示現代 AI 安全系統如何運作
- **最佳實務**：倫理 AI 開發與部署的必要指引
- **[→ 開始第5章](./05-ResponsibleGenAI/README.md)**

## 其他資源

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j 初學者入門](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js 初學者入門](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)

---

### Azure / Edge / MCP / Agents
[![AZD 初學者入門](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI 初學者入門](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP 初學者入門](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 代理初學者入門](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 生成式 AI 系列
[![生成式 AI 初學者](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![生成式 AI（.NET）](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![生成式 AI（Java）](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![生成式 AI（JavaScript）](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 核心學習
[![機器學習初學者](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![資料科學初學者](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 初學者](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![資安初學者](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![網頁開發初學者](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![物聯網入門](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR 開發入門](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot 系列
[![Copilot：AI 配對程式設計](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![Copilot：C#/.NET](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot 冒險](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## 取得協助

如果你遇到困難或對建立 AI 應用有任何問題，請加入與其他學習者及資深開發人員的討論。這是一個互助的社群，歡迎提問並自由分享知識。

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

如果您在開發過程中有產品回饋或遇到錯誤，請造訪：

[![Microsoft Foundry 開發者論壇](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
免責聲明：
本文件係使用 AI 翻譯服務 Co‑op Translator（https://github.com/Azure/co-op-translator）進行翻譯。儘管我們力求準確，但請注意，自動翻譯可能包含錯誤或不精確之處。原始文件之母語版本應被視為具權威性的版本。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用本翻譯所導致的任何誤解或誤釋承擔任何責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->