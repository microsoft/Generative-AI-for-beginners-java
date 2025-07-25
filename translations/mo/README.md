<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "a49b35508745c032a0033d914df7901b",
  "translation_date": "2025-07-25T10:53:45+00:00",
  "source_file": "README.md",
  "language_code": "mo"
}
-->
# 初學者生成式人工智慧 - Java 版
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![初學者生成式人工智慧 - Java 版](../../translated_images/beg-genai-series.61edc4a6b2cc54284fa2d70eda26dc0ca2669e26e49655b842ea799cd6e16d2a.mo.png)

**所需時間**：整個工作坊可在線完成，無需本地設置。如果您想執行範例，環境設置僅需 2 分鐘，探索範例則需 1-3 小時，具體取決於探索深度。

> **快速開始**

1. 將此倉庫 Fork 到您的 GitHub 帳戶
2. 點擊 **Code** → **Codespaces** 標籤 → **...** → **New with options...**
3. 使用預設值 – 這將選擇為本課程創建的開發容器
4. 點擊 **Create codespace**
5. 等待約 2 分鐘，環境準備完成
6. 直接跳到 [創建您的 GitHub 模型 Token](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## 多語言支持

### 通過 GitHub Action 支持（自動化且始終保持最新）

[法語](../fr/README.md) | [西班牙語](../es/README.md) | [德語](../de/README.md) | [俄語](../ru/README.md) | [阿拉伯語](../ar/README.md) | [波斯語（法爾西語）](../fa/README.md) | [烏爾都語](../ur/README.md) | [中文（簡體）](../zh/README.md) | [中文（繁體，澳門）](./README.md) | [中文（繁體，香港）](../hk/README.md) | [中文（繁體，台灣）](../tw/README.md) | [日語](../ja/README.md) | [韓語](../ko/README.md) | [印地語](../hi/README.md) | [孟加拉語](../bn/README.md) | [馬拉地語](../mr/README.md) | [尼泊爾語](../ne/README.md) | [旁遮普語（古木基文）](../pa/README.md) | [葡萄牙語（葡萄牙）](../pt/README.md) | [葡萄牙語（巴西）](../br/README.md) | [意大利語](../it/README.md) | [波蘭語](../pl/README.md) | [土耳其語](../tr/README.md) | [希臘語](../el/README.md) | [泰語](../th/README.md) | [瑞典語](../sv/README.md) | [丹麥語](../da/README.md) | [挪威語](../no/README.md) | [芬蘭語](../fi/README.md) | [荷蘭語](../nl/README.md) | [希伯來語](../he/README.md) | [越南語](../vi/README.md) | [印尼語](../id/README.md) | [馬來語](../ms/README.md) | [塔加洛語（菲律賓語）](../tl/README.md) | [斯瓦希里語](../sw/README.md) | [匈牙利語](../hu/README.md) | [捷克語](../cs/README.md) | [斯洛伐克語](../sk/README.md) | [羅馬尼亞語](../ro/README.md) | [保加利亞語](../bg/README.md) | [塞爾維亞語（西里爾文）](../sr/README.md) | [克羅地亞語](../hr/README.md) | [斯洛文尼亞語](../sl/README.md) | [烏克蘭語](../uk/README.md) | [緬甸語（緬甸）](../my/README.md)

## 課程結構與學習路徑

### **第一章：生成式人工智慧簡介**
- **核心概念**：了解大型語言模型、tokens、嵌入以及 AI 的能力
- **Java AI 生態系統**：Spring AI 和 OpenAI SDK 的概述
- **模型上下文協議**：介紹 MCP 及其在 AI 代理通信中的角色
- **實際應用**：包括聊天機器人和內容生成的真實場景
- **[→ 開始第一章](./01-IntroToGenAI/README.md)**

### **第二章：開發環境設置**
- **多供應商配置**：設置 GitHub Models、Azure OpenAI 和 OpenAI Java SDK 的整合
- **Spring Boot + Spring AI**：企業 AI 應用開發的最佳實踐
- **GitHub Models**：免費的 AI 模型訪問，用於原型設計和學習（無需信用卡）
- **開發工具**：Docker 容器、VS Code 和 GitHub Codespaces 配置
- **[→ 開始第二章](./02-SetupDevEnvironment/README.md)**

### **第三章：核心生成式人工智慧技術**
- **提示工程**：優化 AI 模型回應的技術
- **嵌入與向量操作**：實現語義搜索和相似性匹配
- **檢索增強生成（RAG）**：將 AI 與您自己的數據源結合
- **函數調用**：使用自定義工具和插件擴展 AI 的能力
- **[→ 開始第三章](./03-CoreGenerativeAITechniques/README.md)**

### **第四章：實際應用與項目**
- **寵物故事生成器** (`petstory/`)：使用 GitHub Models 創造性內容生成
- **Foundry 本地演示** (`foundrylocal/`)：使用 OpenAI Java SDK 的本地 AI 模型整合
- **MCP 計算器服務** (`mcp/calculator/`)：使用 Spring AI 實現基本模型上下文協議
- **[→ 開始第四章](./04-PracticalSamples/README.md)**

### **第五章：負責任的人工智慧開發**
- **GitHub Models 安全性**：測試內建的內容過濾和安全機制
- **負責任的 AI 演示**：展示 AI 安全過濾器如何在實際中運作的動手範例
- **最佳實踐**：倫理 AI 開發和部署的基本指南
- **[→ 開始第五章](./05-ResponsibleGenAI/README.md)**

## 其他資源 

- [初學者 AI 代理](https://github.com/microsoft/ai-agents-for-beginners)
- [使用 .NET 的初學者生成式人工智慧](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [使用 JavaScript 的初學者生成式人工智慧](https://github.com/microsoft/generative-ai-with-javascript)
- [初學者生成式人工智慧](https://github.com/microsoft/generative-ai-for-beginners)
- [初學者機器學習](https://aka.ms/ml-beginners)
- [初學者數據科學](https://aka.ms/datascience-beginners)
- [初學者人工智慧](https://aka.ms/ai-beginners)
- [初學者網絡安全](https://github.com/microsoft/Security-101)
- [初學者網頁開發](https://aka.ms/webdev-beginners)
- [初學者物聯網](https://aka.ms/iot-beginners)
- [初學者 XR 開發](https://github.com/microsoft/xr-development-for-beginners)
- [精通 GitHub Copilot 用於 AI 配對編程](https://aka.ms/GitHubCopilotAI)
- [精通 GitHub Copilot 用於 C#/.NET 開發者](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [選擇您的 Copilot 冒險](https://github.com/microsoft/CopilotAdventures)
- [使用 Azure AI 服務的 RAG 聊天應用](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**免責聲明**：  
本文檔使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。儘管我們努力確保準確性，但請注意，自動翻譯可能包含錯誤或不準確之處。原始語言的文件應被視為權威來源。對於關鍵信息，建議使用專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或誤釋不承擔責任。