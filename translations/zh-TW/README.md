# 生成式 AI 入門 - Java 版本
[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

![生成式 AI 入門 - Java 版本](../../translated_images/zh-TW/beg-genai-series.8b48be9951cc574c.webp)

<strong>時間投入</strong>：整個工作坊可線上完成，無需本地設定。環境設定約 2 分鐘，依探索深度，瀏覽範例需 1-3 小時。

> <strong>快速開始</strong> 

1. 將此儲存庫分支到您的 GitHub 帳號
2. 點擊 **Code** → **Codespaces** 分頁 → **...** → **New with options...**
3. 使用預設設定 – 將自動選擇此課程所建立的開發容器
4. 點擊 **Create codespace**
5. 等待約 2 分鐘，環境準備完成
6. 直接跳至 [第 2 章：配置 Azure AI Foundry](./02-SetupDevEnvironment/README.md#step-2-provision-azure-ai-foundry)

## 多語言支援

### 透過 GitHub Action 支援（自動且持續更新）

<!-- CO-OP TRANSLATOR LANGUAGES TABLE START -->
[阿拉伯文](../ar/README.md) | [孟加拉文](../bn/README.md) | [保加利亞文](../bg/README.md) | [緬甸語（緬甸）](../my/README.md) | [中文（簡體）](../zh-CN/README.md) | [中文（繁體，香港）](../zh-HK/README.md) | [中文（繁體，澳門）](../zh-MO/README.md) | [中文（繁體，台灣）](./README.md) | [克羅埃西亞文](../hr/README.md) | [捷克文](../cs/README.md) | [丹麥文](../da/README.md) | [荷蘭文](../nl/README.md) | [愛沙尼亞文](../et/README.md) | [芬蘭文](../fi/README.md) | [法文](../fr/README.md) | [德文](../de/README.md) | [希臘文](../el/README.md) | [希伯來文](../he/README.md) | [印地文](../hi/README.md) | [匈牙利文](../hu/README.md) | [印尼文](../id/README.md) | [義大利文](../it/README.md) | [日文](../ja/README.md) | [卡納達文](../kn/README.md) | [高棉文](../km/README.md) | [韓文](../ko/README.md) | [立陶宛文](../lt/README.md) | [馬來文](../ms/README.md) | [馬拉雅拉姆文](../ml/README.md) | [馬拉地文](../mr/README.md) | [尼泊爾文](../ne/README.md) | [奈及利亞白話](../pcm/README.md) | [挪威文](../no/README.md) | [波斯文（法爾西）](../fa/README.md) | [波蘭文](../pl/README.md) | [巴西葡萄牙文](../pt-BR/README.md) | [葡萄牙文（葡萄牙）](../pt-PT/README.md) | [旁遮普文（古魯穆奇）](../pa/README.md) | [羅馬尼亞文](../ro/README.md) | [俄文](../ru/README.md) | [塞爾維亞文（西里爾）](../sr/README.md) | [斯洛伐克文](../sk/README.md) | [斯洛文尼亞文](../sl/README.md) | [西班牙文](../es/README.md) | [斯瓦希里文](../sw/README.md) | [瑞典文](../sv/README.md) | [塔加洛語（菲律賓）](../tl/README.md) | [坦米爾文](../ta/README.md) | [泰盧固文](../te/README.md) | [泰文](../th/README.md) | [土耳其文](../tr/README.md) | [烏克蘭文](../uk/README.md) | [烏爾都文](../ur/README.md) | [越南文](../vi/README.md)

> **偏好本機複製？**
>
> 此儲存庫包含超過 50 種語言的翻譯，使下載大小顯著增加。若想不含翻譯內容複製，請使用稀疏簽出：
>
> **Bash / macOS / Linux:** 
> ```bash
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone '/*' '!translations' '!translated_images'
> ```
>
> **CMD (Windows):** 
> ```cmd
> git clone --filter=blob:none --sparse https://github.com/microsoft/Generative-AI-for-beginners-java.git
> cd Generative-AI-for-beginners-java
> git sparse-checkout set --no-cone "/*" "!translations" "!translated_images"
> ```
>
> 這樣您即可快速下載並擁有完成課程所需的所有資源。
<!-- CO-OP TRANSLATOR LANGUAGES TABLE END -->

## 課程架構與學習路徑

### **第 1 章：生成式 AI 簡介**
- <strong>核心概念</strong>：理解大型語言模型、tokens、embeddings 與 AI 能力
- **Java AI 生態系統**：Spring AI 和 OpenAI SDK 簡介
- <strong>模型上下文協定</strong>：MCP 簡介及其在 AI 代理溝通中的角色
- <strong>實務應用</strong>：實際案例包括聊天機器人與內容生成
- **[→ 開始第 1 章](./01-IntroToGenAI/README.md)**

### **第 2 章：開發環境設定**
- **Azure AI Foundry**：使用 Bicep 和 Azure Developer CLI (azd) 配置 GPT-5.6 Luna 聊天及 text-embedding-3-small embeddings
- **Spring Boot 4.1.1 + Spring AI 2.0.1**：使用官方 OpenAI Java SDK 與 Azure OpenAI v1，學習 `ChatClient`
- <strong>無鑰匙認證</strong>：透過 Microsoft Entra ID 安全連接，無需管理 API 金鑰
- <strong>開發工具</strong>：Docker 容器、VS Code 與 GitHub Codespaces 配置
- **[→ 開始第 2 章](./02-SetupDevEnvironment/README.md)**

### **第 3 章：生成式 AI 核心技術**
- **官方 OpenAI Java SDK**：直接使用無鑰匙認證呼叫 Azure OpenAI v1
- <strong>提示工程</strong>：達成最佳 AI 模型回應的技巧
- **Embeddings 與向量運算**：實作語意搜尋與相似度比對
- **檢索增強生成（RAG）**：將 AI 與您自己的資料源結合
- <strong>函式呼叫</strong>：使用自訂工具與插件擴展 AI 功能
- **[→ 開始第 3 章](./03-CoreGenerativeAITechniques/README.md)**

### **第 4 章：實務應用與專案**
- <strong>寵物故事生成器</strong> (`petstory/`)：使用 Azure AI Foundry 進行創意內容生成
- **Foundry 本地演示** (`foundrylocal/`)：本地 AI 模型整合 OpenAI Java SDK
- **MCP 計算器服務** (`calculator/`)：基礎模型上下文協定於 Spring AI 的實作
- **[→ 開始第 4 章](./04-PracticalSamples/README.md)**

### **第 5 章：負責任的 AI 開發**
- **Azure AI Foundry 內容安全**：測試內建內容過濾與安全機制（硬性封鎖與軟性拒絕）
- **負責任 AI 演示**：實作示範現代 AI 安全系統的運作
- <strong>最佳實務</strong>：倫理 AI 開發與部署重要指引
- **[→ 開始第 5 章](./05-ResponsibleGenAI/README.md)**

## 額外資源

<!-- CO-OP TRANSLATOR OTHER COURSES START -->
### LangChain
[![LangChain4j 初學者](https://img.shields.io/badge/LangChain4j%20for%20Beginners-22C55E?style=for-the-badge&&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchain4j-for-beginners)
[![LangChain.js 初學者](https://img.shields.io/badge/LangChain.js%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://aka.ms/langchainjs-for-beginners?WT.mc_id=m365-94501-dwahlin)
[![LangChain 初學者](https://img.shields.io/badge/LangChain%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=0553D6)](https://github.com/microsoft/langchain-for-beginners?WT.mc_id=m365-94501-dwahlin)
---

### Azure / Edge / MCP / 代理
[![AZD 初學者](https://img.shields.io/badge/AZD%20for%20Beginners-0078D4?style=for-the-badge&labelColor=E5E7EB&color=0078D4)](https://github.com/microsoft/AZD-for-beginners?WT.mc_id=academic-105485-koreyst)
[![Edge AI 初學者](https://img.shields.io/badge/Edge%20AI%20for%20Beginners-00B8E4?style=for-the-badge&labelColor=E5E7EB&color=00B8E4)](https://github.com/microsoft/edgeai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![MCP 初學者](https://img.shields.io/badge/MCP%20for%20Beginners-009688?style=for-the-badge&labelColor=E5E7EB&color=009688)](https://github.com/microsoft/mcp-for-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 代理初學者](https://img.shields.io/badge/AI%20Agents%20for%20Beginners-00C49A?style=for-the-badge&labelColor=E5E7EB&color=00C49A)](https://github.com/microsoft/ai-agents-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### 生成式 AI 系列
[![生成式 AI 初學者](https://img.shields.io/badge/Generative%20AI%20for%20Beginners-8B5CF6?style=for-the-badge&labelColor=E5E7EB&color=8B5CF6)](https://github.com/microsoft/generative-ai-for-beginners?WT.mc_id=academic-105485-koreyst)
[![生成式 AI (.NET)](https://img.shields.io/badge/Generative%20AI%20(.NET)-9333EA?style=for-the-badge&labelColor=E5E7EB&color=9333EA)](https://github.com/microsoft/Generative-AI-for-beginners-dotnet?WT.mc_id=academic-105485-koreyst)
[![生成式 AI (Java)](https://img.shields.io/badge/Generative%20AI%20(Java)-C084FC?style=for-the-badge&labelColor=E5E7EB&color=C084FC)](https://github.com/microsoft/generative-ai-for-beginners-java?WT.mc_id=academic-105485-koreyst)
[![生成式 AI (JavaScript)](https://img.shields.io/badge/Generative%20AI%20(JavaScript)-E879F9?style=for-the-badge&labelColor=E5E7EB&color=E879F9)](https://github.com/microsoft/generative-ai-with-javascript?WT.mc_id=academic-105485-koreyst)

---
 
### 核心學習
[![機器學習初學者](https://img.shields.io/badge/ML%20for%20Beginners-22C55E?style=for-the-badge&labelColor=E5E7EB&color=22C55E)](https://aka.ms/ml-beginners?WT.mc_id=academic-105485-koreyst)
[![資料科學初學者](https://img.shields.io/badge/Data%20Science%20for%20Beginners-84CC16?style=for-the-badge&labelColor=E5E7EB&color=84CC16)](https://aka.ms/datascience-beginners?WT.mc_id=academic-105485-koreyst)
[![AI 初學者](https://img.shields.io/badge/AI%20for%20Beginners-A3E635?style=for-the-badge&labelColor=E5E7EB&color=A3E635)](https://aka.ms/ai-beginners?WT.mc_id=academic-105485-koreyst)
[![資安初學者](https://img.shields.io/badge/Cybersecurity%20for%20Beginners-F97316?style=for-the-badge&labelColor=E5E7EB&color=F97316)](https://github.com/microsoft/Security-101?WT.mc_id=academic-96948-sayoung)
[![網頁開發初學者](https://img.shields.io/badge/Web%20Dev%20for%20Beginners-EC4899?style=for-the-badge&labelColor=E5E7EB&color=EC4899)](https://aka.ms/webdev-beginners?WT.mc_id=academic-105485-koreyst)
[![物聯網初學者](https://img.shields.io/badge/IoT%20for%20Beginners-14B8A6?style=for-the-badge&labelColor=E5E7EB&color=14B8A6)](https://aka.ms/iot-beginners?WT.mc_id=academic-105485-koreyst)
[![XR 開發初學者](https://img.shields.io/badge/XR%20Development%20for%20Beginners-38BDF8?style=for-the-badge&labelColor=E5E7EB&color=38BDF8)](https://github.com/microsoft/xr-development-for-beginners?WT.mc_id=academic-105485-koreyst)

---
 
### Copilot 系列
[![AI 配對程式設計 Copilot](https://img.shields.io/badge/Copilot%20for%20AI%20Paired%20Programming-FACC15?style=for-the-badge&labelColor=E5E7EB&color=FACC15)](https://aka.ms/GitHubCopilotAI?WT.mc_id=academic-105485-koreyst)
[![C#/.NET Copilot](https://img.shields.io/badge/Copilot%20for%20C%23/.NET-FBBF24?style=for-the-badge&labelColor=E5E7EB&color=FBBF24)](https://github.com/microsoft/mastering-github-copilot-for-dotnet-csharp-developers?WT.mc_id=academic-105485-koreyst)
[![Copilot 冒險](https://img.shields.io/badge/Copilot%20Adventure-FDE68A?style=for-the-badge&labelColor=E5E7EB&color=FDE68A)](https://github.com/microsoft/CopilotAdventures?WT.mc_id=academic-105485-koreyst)
<!-- CO-OP TRANSLATOR OTHER COURSES END -->

## 尋求協助

如果您在建置 AI 應用時遇到困難或有任何問題，歡迎加入學習者與經驗豐富的開發者討論 MCP。這是一個支持性的社群，熱烈歡迎提問並自由分享知識。

[![Microsoft Foundry Discord](https://dcbadge.limes.pink/api/server/nTYy5BXMWG)](https://discord.gg/nTYy5BXMWG)

如果您有產品回饋或在建置時發生錯誤，請造訪：

[![Microsoft Foundry Developer Forum](https://img.shields.io/badge/GitHub-Microsoft_Foundry_Developer_Forum-blue?style=for-the-badge&logo=github&color=000000&logoColor=fff)](https://aka.ms/foundry/forum)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->