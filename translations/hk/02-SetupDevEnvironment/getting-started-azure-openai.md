<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-07-27T12:56:26+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "hk"
}
-->
# 設置 Azure OpenAI 的開發環境

> **快速開始**：本指南適用於 Azure OpenAI 的設置。如果想快速使用免費模型，請參考 [GitHub Models with Codespaces](./README.md#quick-start-cloud)。

本指南將幫助你為本課程中的 Java AI 應用設置 Azure AI Foundry 模型。

## 目錄

- [快速設置概覽](../../../02-SetupDevEnvironment)
- [步驟 1：創建 Azure AI Foundry 資源](../../../02-SetupDevEnvironment)
  - [創建 Hub 和項目](../../../02-SetupDevEnvironment)
  - [部署 GPT-4o-mini 模型](../../../02-SetupDevEnvironment)
- [步驟 2：創建你的 Codespace](../../../02-SetupDevEnvironment)
- [步驟 3：配置你的環境](../../../02-SetupDevEnvironment)
- [步驟 4：測試你的設置](../../../02-SetupDevEnvironment)
- [接下來是什麼？](../../../02-SetupDevEnvironment)
- [資源](../../../02-SetupDevEnvironment)
- [其他資源](../../../02-SetupDevEnvironment)

## 快速設置概覽

1. 創建 Azure AI Foundry 資源（Hub、項目、模型）
2. 使用 Java 開發容器創建 Codespace
3. 使用 Azure OpenAI 憑據配置 `.env` 文件
4. 使用示例項目測試你的設置

## 步驟 1：創建 Azure AI Foundry 資源

### 創建 Hub 和項目

1. 前往 [Azure AI Foundry Portal](https://ai.azure.com/) 並登錄
2. 點擊 **+ Create** → **New hub**（或導航到 **Management** → **All hubs** → **+ New hub**）
3. 配置你的 Hub：
   - **Hub 名稱**：例如 "MyAIHub"
   - **訂閱**：選擇你的 Azure 訂閱
   - **資源組**：創建新的或選擇現有的
   - **位置**：選擇離你最近的
   - **存儲帳戶**：使用默認或自定義配置
   - **密鑰保管庫**：使用默認或自定義配置
   - 點擊 **Next** → **Review + create** → **Create**
4. 創建完成後，點擊 **+ New project**（或從 Hub 概覽中選擇 **Create project**）
   - **項目名稱**：例如 "GenAIJava"
   - 點擊 **Create**

### 部署 GPT-4o-mini 模型

1. 在你的項目中，前往 **Model catalog** 並搜索 **gpt-4o-mini**
   - *替代方法：前往 **Deployments** → **+ Create deployment***
2. 在 gpt-4o-mini 模型卡片上點擊 **Deploy**
3. 配置部署：
   - **部署名稱**："gpt-4o-mini"
   - **模型版本**：使用最新版本
   - **部署類型**：標準
4. 點擊 **Deploy**
5. 部署完成後，前往 **Deployments** 標籤並複製以下值：
   - **部署名稱**（例如 "gpt-4o-mini"）
   - **目標 URI**（例如 `https://your-hub-name.openai.azure.com/`）  
      > **重要**：僅複製基礎 URL（例如 `https://myhub.openai.azure.com/`），不要包含完整的端點路徑。
   - **密鑰**（從密鑰和端點部分獲取）

> **仍有問題？** 請參考官方 [Azure AI Foundry 文檔](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)

## 步驟 2：創建你的 Codespace

1. 將此倉庫 Fork 到你的 GitHub 帳戶
   > **注意**：如果你想編輯基本配置，請查看 [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. 在你的 Fork 倉庫中，點擊 **Code** → **Codespaces** 標籤
3. 點擊 **...** → **New with options...**  
![創建帶選項的 Codespace](../../../translated_images/hk/codespaces.9945ded8ceb431a5.webp)
4. 選擇 **Dev container configuration**： 
   - **Generative AI Java Development Environment**
5. 點擊 **Create codespace**

## 步驟 3：配置你的環境

當你的 Codespace 準備好後，設置你的 Azure OpenAI 憑據：

1. **從倉庫根目錄導航到示例項目：**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **創建你的 .env 文件：**
   ```bash
   cp .env.example .env
   ```

3. **編輯 .env 文件，填入你的 Azure OpenAI 憑據：**
   ```bash
   # Your Azure OpenAI API key (from Azure AI Foundry portal)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # Your Azure OpenAI endpoint URL (e.g., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **安全提示**： 
   > - 切勿將 `.env` 文件提交到版本控制
   > - `.env` 文件已包含在 `.gitignore` 中
   > - 確保你的 API 密鑰安全，並定期更換

## 步驟 4：測試你的設置

運行示例應用程序以測試你的 Azure OpenAI 連接：

```bash
mvn clean spring-boot:run
```

你應該會看到來自 GPT-4o-mini 模型的響應！

> **VS Code 用戶**：你也可以在 VS Code 中按 `F5` 運行應用程序。啟動配置已設置為自動加載你的 `.env` 文件。

> **完整示例**：請參考 [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) 獲取詳細說明和故障排除。

## 接下來是什麼？

**設置完成！** 你現在已經：
- 部署了 gpt-4o-mini 模型的 Azure OpenAI
- 配置了本地 `.env` 文件
- 準備好了 Java 開發環境

**繼續前往** [第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md) 開始構建 AI 應用！

## 資源

- [Azure AI Foundry 文檔](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI 文檔](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## 其他資源

- [下載 VS Code](https://code.visualstudio.com/Download)
- [獲取 Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

**免責聲明**：  
本文件已使用人工智能翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。我們致力於提供準確的翻譯，但請注意，自動翻譯可能包含錯誤或不準確之處。應以原文文件作為權威來源。如涉及關鍵資訊，建議尋求專業人工翻譯。我們對因使用此翻譯而引起的任何誤解或誤釋概不負責。