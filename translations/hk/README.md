<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "90ac762d40c6db51b8081cdb3e49e9db",
  "translation_date": "2025-08-07T11:04:09+00:00",
  "source_file": "README.md",
  "language_code": "hk"
}
-->
# 初學者的生成式 AI - Java 版
[![Microsoft Azure AI Foundry Discord](https://dcbadge.limes.pink/api/server/ByRwuEEgH4)](https://discord.com/invite/ByRwuEEgH4)

![初學者的生成式 AI - Java 版](../../translated_images/beg-genai-series.8b48be9951cc574c25f8a3accba949bfd03c2f008e2c613283a1b47316fbee68.hk.png)

**所需時間**：整個工作坊可在線上完成，無需本地設置。環境設置僅需 2 分鐘，探索範例所需時間視探索深度而定，大約需 1-3 小時。

> **快速開始**

1. 將此儲存庫 Fork 到你的 GitHub 帳戶
2. 點擊 **Code** → **Codespaces** 標籤 → **...** → **New with options...**
3. 使用預設值 – 這將選擇為本課程創建的開發容器
4. 點擊 **Create codespace**
5. 等待約 2 分鐘，直到環境準備就緒
6. 直接跳到 [第一個範例](./02-SetupDevEnvironment/README.md#step-2-create-a-github-personal-access-token)

## 多語言支援

### 通過 GitHub Action 支援（自動化且始終保持最新）

[法文](../fr/README.md) | [西班牙文](../es/README.md) | [德文](../de/README.md) | [俄文](../ru/README.md) | [阿拉伯文](../ar/README.md) | [波斯文（法爾西）](../fa/README.md) | [烏爾都文](../ur/README.md) | [中文（簡體）](../zh/README.md) | [中文（繁體，澳門）](../mo/README.md) | [中文（繁體，香港）](./README.md) | [中文（繁體，台灣）](../tw/README.md) | [日文](../ja/README.md) | [韓文](../ko/README.md) | [印地文](../hi/README.md) | [孟加拉文](../bn/README.md) | [馬拉地文](../mr/README.md) | [尼泊爾文](../ne/README.md) | [旁遮普文（古木基文）](../pa/README.md) | [葡萄牙文（葡萄牙）](../pt/README.md) | [葡萄牙文（巴西）](../br/README.md) | [意大利文](../it/README.md) | [波蘭文](../pl/README.md) | [土耳其文](../tr/README.md) | [希臘文](../el/README.md) | [泰文](../th/README.md) | [瑞典文](../sv/README.md) | [丹麥文](../da/README.md) | [挪威文](../no/README.md) | [芬蘭文](../fi/README.md) | [荷蘭文](../nl/README.md) | [希伯來文](../he/README.md) | [越南文](../vi/README.md) | [印尼文](../id/README.md) | [馬來文](../ms/README.md) | [塔加洛語（菲律賓語）](../tl/README.md) | [斯瓦希里文](../sw/README.md) | [匈牙利文](../hu/README.md) | [捷克文](../cs/README.md) | [斯洛伐克文](../sk/README.md) | [羅馬尼亞文](../ro/README.md) | [保加利亞文](../bg/README.md) | [塞爾維亞文（西里爾字母）](../sr/README.md) | [克羅地亞文](../hr/README.md) | [斯洛文尼亞文](../sl/README.md) | [烏克蘭文](../uk/README.md) | [緬甸文（緬甸）](../my/README.md)

## 課程結構與學習路徑

### **第一章：生成式 AI 簡介**
- **核心概念**：了解大型語言模型、tokens、嵌入和 AI 的能力
- **Java AI 生態系統**：Spring AI 和 OpenAI SDK 的概述
- **模型上下文協議（MCP）**：介紹 MCP 及其在 AI 代理通信中的角色
- **實際應用**：包括聊天機器人和內容生成的真實場景
- **[→ 開始第一章](./01-IntroToGenAI/README.md)**

### **第二章：開發環境設置**
- **多供應商配置**：設置 GitHub Models、Azure OpenAI 和 OpenAI Java SDK 的整合
- **Spring Boot + Spring AI**：企業 AI 應用開發的最佳實踐
- **GitHub Models**：免費的 AI 模型，用於原型設計和學習（無需信用卡）
- **開發工具**：Docker 容器、VS Code 和 GitHub Codespaces 的配置
- **[→ 開始第二章](./02-SetupDevEnvironment/README.md)**

### **第三章：生成式 AI 核心技術**
- **提示工程**：優化 AI 模型回應的技術
- **嵌入與向量操作**：實現語義搜索和相似性匹配
- **檢索增強生成（RAG）**：將 AI 與自有數據源結合
- **函數調用**：使用自定義工具和插件擴展 AI 功能
- **[→ 開始第三章](./03-CoreGenerativeAITechniques/README.md)**

### **第四章：實際應用與項目**
- **寵物故事生成器** (`petstory/`)：使用 GitHub Models 進行創意內容生成
- **Foundry 本地演示** (`foundrylocal/`)：使用 OpenAI Java SDK 的本地 AI 模型整合
- **MCP 計算器服務** (`calculator/`)：使用 Spring AI 實現基本的模型上下文協議
- **[→ 開始第四章](./04-PracticalSamples/README.md)**

### **第五章：負責任的 AI 開發**
- **GitHub Models 安全性**：測試內建的內容過濾和安全機制（硬性阻止和軟性拒絕）
- **負責任的 AI 演示**：展示現代 AI 安全系統如何在實踐中運作
- **最佳實踐**：倫理 AI 開發與部署的基本指南
- **[→ 開始第五章](./05-ResponsibleGenAI/README.md)**

## 其他資源

- [MCP 初學者指南](https://github.com/microsoft/mcp-for-beginners)
- [AI 代理初學者指南](https://github.com/microsoft/ai-agents-for-beginners)
- [使用 .NET 的生成式 AI 初學者指南](https://github.com/microsoft/Generative-AI-for-beginners-dotnet)
- [使用 JavaScript 的生成式 AI 初學者指南](https://github.com/microsoft/generative-ai-with-javascript)
- [生成式 AI 初學者指南](https://github.com/microsoft/generative-ai-for-beginners)
- [機器學習初學者指南](https://aka.ms/ml-beginners)
- [數據科學初學者指南](https://aka.ms/datascience-beginners)
- [AI 初學者指南](https://aka.ms/ai-beginners)
- [網絡安全初學者指南](https://github.com/microsoft/Security-101)
- [Web 開發初學者指南](https://aka.ms/webdev-beginners)
- [物聯網初學者指南](https://aka.ms/iot-beginners)
- [XR 開發初學者指南](https://github.com/microsoft/xr-development-for-beginners)
- [精通 GitHub Copilot 的 AI 配對編程](https://aka.ms/GitHubCopilotAI)
- [精通 GitHub Copilot 的 C#/.NET 開發](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers)
- [選擇你的 Copilot 冒險](https://github.com/microsoft/CopilotAdventures)
- [使用 Azure AI 服務的 RAG 聊天應用](https://github.com/Azure-Samples/azure-search-openai-demo-java)

**免責聲明**：  
本文件使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。我們致力於提供準確的翻譯，但請注意，自動翻譯可能包含錯誤或不準確之處。應以原文文件作為權威來源。對於關鍵資訊，建議尋求專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或誤釋概不負責。