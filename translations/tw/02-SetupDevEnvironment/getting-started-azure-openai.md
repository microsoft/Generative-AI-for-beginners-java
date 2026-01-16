<!--
CO_OP_TRANSLATOR_METADATA:
{
  "original_hash": "bfdb4b4eadbee3a59ef742439f58326a",
  "translation_date": "2025-07-27T12:57:32+00:00",
  "source_file": "02-SetupDevEnvironment/getting-started-azure-openai.md",
  "language_code": "tw"
}
-->
# 設置 Azure OpenAI 開發環境

> **快速開始**：本指南適用於 Azure OpenAI 的設置。如果想立即使用免費模型，請參考 [GitHub Models with Codespaces](./README.md#quick-start-cloud)。

本指南將幫助您在本課程中為 Java AI 應用設置 Azure AI Foundry 模型。

## 目錄

- [快速設置概述](../../../02-SetupDevEnvironment)
- [步驟 1：創建 Azure AI Foundry 資源](../../../02-SetupDevEnvironment)
  - [創建 Hub 和 Project](../../../02-SetupDevEnvironment)
  - [部署 GPT-4o-mini 模型](../../../02-SetupDevEnvironment)
- [步驟 2：創建 Codespace](../../../02-SetupDevEnvironment)
- [步驟 3：配置環境](../../../02-SetupDevEnvironment)
- [步驟 4：測試設置](../../../02-SetupDevEnvironment)
- [下一步是什麼？](../../../02-SetupDevEnvironment)
- [資源](../../../02-SetupDevEnvironment)
- [其他資源](../../../02-SetupDevEnvironment)

## 快速設置概述

1. 創建 Azure AI Foundry 資源（Hub、Project、Model）
2. 使用 Java 開發容器創建 Codespace
3. 使用 Azure OpenAI 憑證配置 `.env` 文件
4. 使用示例項目測試設置

## 步驟 1：創建 Azure AI Foundry 資源

### 創建 Hub 和 Project

1. 前往 [Azure AI Foundry Portal](https://ai.azure.com/) 並登錄
2. 點擊 **+ Create** → **New hub**（或導航至 **Management** → **All hubs** → **+ New hub**）
3. 配置您的 Hub：
   - **Hub 名稱**：例如 "MyAIHub"
   - **訂閱**：選擇您的 Azure 訂閱
   - **資源組**：創建新的或選擇現有的
   - **位置**：選擇離您最近的地點
   - **存儲帳戶**：使用默認或自定義配置
   - **密鑰保管庫**：使用默認或自定義配置
   - 點擊 **Next** → **Review + create** → **Create**
4. 創建完成後，點擊 **+ New project**（或從 Hub 概覽中點擊 **Create project**）
   - **Project 名稱**：例如 "GenAIJava"
   - 點擊 **Create**

### 部署 GPT-4o-mini 模型

1. 在您的 Project 中，前往 **Model catalog** 並搜索 **gpt-4o-mini**
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
   - **密鑰**（從 Keys and Endpoint 部分獲取）

> **仍有疑問？** 請參考官方 [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects?tabs=ai-foundry&pivots=hub-project)

## 步驟 2：創建 Codespace

1. 將此存儲庫 Fork 到您的 GitHub 帳戶
   > **注意**：如果您想編輯基本配置，請查看 [Dev Container Configuration](../../../.devcontainer/devcontainer.json)
2. 在您的 Fork 存儲庫中，點擊 **Code** → **Codespaces** 標籤
3. 點擊 **...** → **New with options...**
![使用選項創建 Codespace](../../../translated_images/tw/codespaces.9945ded8ceb431a5.webp)
4. 選擇 **Dev container configuration**： 
   - **Generative AI Java Development Environment**
5. 點擊 **Create codespace**

## 步驟 3：配置環境

當您的 Codespace 準備好後，設置您的 Azure OpenAI 憑證：

1. **從存儲庫根目錄導航到示例項目：**
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```

2. **創建 `.env` 文件：**
   ```bash
   cp .env.example .env
   ```

3. **使用您的 Azure OpenAI 憑證編輯 `.env` 文件：**
   ```bash
   # Your Azure OpenAI API key (from Azure AI Foundry portal)
   AZURE_AI_KEY=your-actual-api-key-here
   
   # Your Azure OpenAI endpoint URL (e.g., https://myhub.openai.azure.com/)
   AZURE_AI_ENDPOINT=https://your-hub-name.openai.azure.com/
   ```

   > **安全提示**： 
   > - 切勿將 `.env` 文件提交到版本控制
   > - `.env` 文件已包含在 `.gitignore` 中
   > - 保護您的 API 密鑰並定期更換

## 步驟 4：測試設置

運行示例應用程序以測試您的 Azure OpenAI 連接：

```bash
mvn clean spring-boot:run
```

您應該能看到來自 GPT-4o-mini 模型的響應！

> **VS Code 使用者**：您也可以在 VS Code 中按 `F5` 運行應用程序。啟動配置已設置為自動加載您的 `.env` 文件。

> **完整示例**：請參考 [End-to-End Azure OpenAI Example](./examples/basic-chat-azure/README.md) 獲取詳細指導和故障排除。

## 下一步是什麼？

**設置完成！** 您現在擁有：
- 部署了 gpt-4o-mini 的 Azure OpenAI
- 本地 `.env` 文件配置
- 準備好的 Java 開發環境

**繼續前往** [第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md) 開始構建 AI 應用！

## 資源

- [Azure AI Foundry Documentation](https://learn.microsoft.com/azure/ai-services/)
- [Spring AI Azure OpenAI Documentation](https://docs.spring.io/spring-ai/reference/api/clients/azure-openai-chat.html)
- [Azure OpenAI Java SDK](https://learn.microsoft.com/java/api/overview/azure/ai-openai-readme)

## 其他資源

- [下載 VS Code](https://code.visualstudio.com/Download)
- [獲取 Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Dev Container Configuration](../../../.devcontainer/devcontainer.json)

**免責聲明**：  
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們致力於提供準確的翻譯，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於關鍵資訊，建議使用專業人工翻譯。我們對因使用此翻譯而產生的任何誤解或錯誤解釋不承擔責任。