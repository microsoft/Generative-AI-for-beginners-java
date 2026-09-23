# 為 Java 生成式 AI 設置開發環境

> **快速開始：** 使用 Bicep + `azd` 在幾分鐘內於 **Azure AI Foundry** 上以程式碼方式佈建您的 AI 模型 — 請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)。認證為 <strong>無金鑰</strong>（Microsoft Entra ID），因此不需要管理任何 API 金鑰。

## 您將學到什麼

- 設置 Java AI 應用程式開發環境
- 選擇並設定您偏好的開發環境（以雲端為先的 Codespaces、本地開發容器或完整本地設置）
- 通過連接到 Azure AI Foundry 模型測試您的設置

## 目錄

- [您將學到什麼](#您將學到什麼)
- [介紹](#介紹)
- [步驟 1：設置您的開發環境](#步驟-1：設置您的開發環境)
  - [選項 A：GitHub Codespaces（推薦）](#選項-a：github-codespaces（推薦）)
  - [選項 B：本地開發容器](#選項-b：本地開發容器)
  - [選項 C：使用您現有的本地安裝環境](#選項-c：使用您現有的本地安裝環境)
- [步驟 2：佈建 Azure AI Foundry](#步驟-2：佈建-azure-ai-foundry)
- [步驟 3：測試您的設置](#步驟-3：測試您的設置)
- [故障排除](#故障排除)
- [總結](#總結)
- [下一步](#下一步)

## 介紹

本章將指導您完成開發環境的設置。我們會在整個課程中使用 **Azure AI Foundry** 作為模型。您可使用 Bicep 和 Azure Developer CLI (`azd`) 以程式碼方式佈建模型，並使用 <strong>無金鑰認證</strong>（Microsoft Entra ID）連接 — 不需複製或洩漏 API 金鑰。

**不需要本地設置！** 您可以使用 GitHub Codespaces，這會在您的瀏覽器中提供完整的開發環境，並可從那裡佈建 Foundry。

我們使用 **Azure AI Foundry** 是因為它：
- <strong>以程式碼佈建</strong> — 一個 `azd up` 可部署帳戶和模型佈建
- <strong>無金鑰</strong> — 使用您的 Azure 登入或受管理身份驗證
- <strong>適合生產</strong> — 同樣的程式碼可在本地與 Azure 運行
- <strong>彈性靈活</strong> — 只需更改部署名稱即可替換模型，而非修改程式碼

> <strong>注意</strong>：Azure AI Foundry 部署按代幣計費（隨用隨付）。有關佈建、地區及費用詳情，請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)。


## 步驟 1：設置您的開發環境

<a name="quick-start-cloud"></a>

我們已創建預配置開發容器以縮短設置時間，並確保您擁有本 Java 生成式 AI 課程所需的所有工具。請選擇您偏好的開發方式：

### 環境設置選項：

#### 選項 A：GitHub Codespaces（推薦）

**兩分鐘開始編碼 - 無需本地設置！**

1. 將此存儲庫分支（Fork）到您的 GitHub 帳號
   > <strong>注意</strong>：如果想編輯基本配置，請參閱 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 點擊 **Code** → **Codespaces** 標籤 → **...** → **New with options...**
3. 使用預設配置 — 這將選擇課程專屬的 **生成式 AI Java 開發環境** 自訂 devcontainer 配置
4. 點擊 **Create codespace**
5. 等待約 2 分鐘完成環境準備
6. 繼續執行 [步驟 2：佈建 Azure AI Foundry](#步驟-2：佈建-azure-ai-foundry)

<img src="../../../translated_images/zh-TW/codespaces.9945ded8ceb431a5.webp" alt="截圖：Codespaces 子選單" width="50%">

<img src="../../../translated_images/zh-TW/image.833552b62eee7766.webp" alt="截圖：New with options" width="50%">

<img src="../../../translated_images/zh-TW/codespaces-create.b44a36f728660ab7.webp" alt="截圖：建立 codespace 選項" width="50%">


> **Codespaces 優勢**：
> - 不需本地安裝
> - 支援任何有瀏覽器的裝置
> - 預先配置所有工具及相依性
> - 個人帳戶每月免費 60 小時
> - 為所有學員提供一致環境

#### 選項 B：本地開發容器

**適合偏好使用 Docker 的本地開發者**

1. 分支（Fork）並克隆此存儲庫至本地機器
   > <strong>注意</strong>：如果想編輯基本配置，請參閱 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 安裝 [Docker Desktop](https://www.docker.com/products/docker-desktop/) 與 [VS Code](https://code.visualstudio.com/)
3. 在 VS Code 中安裝 [開發容器擴展](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
4. 在 VS Code 中打開存儲庫資料夾
5. 出現提示時，點擊 **Reopen in Container**（或使用 `Ctrl+Shift+P` → 輸入 "Dev Containers: Reopen in Container"）
6. 等待容器建置及啟動完成
7. 繼續執行 [步驟 2：佈建 Azure AI Foundry](#步驟-2：佈建-azure-ai-foundry)

<img src="../../../translated_images/zh-TW/devcontainer.21126c9d6de64494.webp" alt="截圖：開發容器設置" width="50%">

<img src="../../../translated_images/zh-TW/image-3.bf93d533bbc84268.webp" alt="截圖：開發容器建置完成" width="50%">

#### 選項 C：使用您現有的本地安裝環境

**適合已有 Java 開發環境的開發者**

前置需求：
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) 或您偏好的 IDE

步驟：
1. 將此存儲庫克隆至本地機器
2. 在您的 IDE 中打開專案
3. 繼續執行 [步驟 2：佈建 Azure AI Foundry](#步驟-2：佈建-azure-ai-foundry)

> <strong>專業提示</strong>：如果您的機器效能較低，但想在本地使用 VS Code，請使用 GitHub Codespaces！您可以將本地 VS Code 連接到雲端代管的 Codespace，達成兩者兼具的最佳使用體驗。

<img src="../../../translated_images/zh-TW/image-2.fc0da29a6e4d2aff.webp" alt="截圖：已建立本地開發容器實例" width="50%">


## 步驟 2：佈建 Azure AI Foundry

將本課程的 AI 模型以程式碼方式佈建到 Azure AI Foundry。於存儲庫根目錄：

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` 會提示輸入環境名稱、訂閱與地區，然後以 <strong>無金鑰</strong> 認證（不需 API 金鑰）佈建含 `gpt-5.6-luna` 和 `text-embedding-3-small` 部署的 Azure AI Foundry 帳戶，並將端點寫入範例的 `.env` 檔中。

> **完整操作說明：** 請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md) 以了解前置條件、手動（入口網站）佈建替代方案、地區建議及費用／清理說明。

## 步驟 3：測試您的設置

佈建好 Foundry 模型後，使用 [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) 的範例應用程式測試連接。

1. 在您的開發環境打開終端機。
2. 移動至範例資料夾：
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. 確認已登入（無金鑰認證需要令牌）：
   ```bash
   az login
   ```
   > 若您已執行 `azd up`，包含端點的 `.env` 文件已自動為您寫入。
4. 執行應用程式：
   ```bash
   mvn clean spring-boot:run
   ```

您應該會看到來自 `gpt-5.6-luna` 模型的回應。

### 理解範例程式碼

[basic-chat 範例](./examples/basic-chat-azure/README.md) 使用 **Spring Boot 4.1.1** 和 **Spring AI 2.0.1**。Spring AI 的 `ChatClient` 基於官方 OpenAI Java SDK，透過無金鑰認證連接 Azure OpenAI **v1** 端點。

**此程式碼做了什麼：**
- 使用您的 Azure 登入（Microsoft Entra ID）<strong>連接</strong>到 Azure AI Foundry — 無需 API 金鑰
- 向 `gpt-5.6-luna` 模型<strong>傳送</strong>提示
- <strong>接收</strong>並顯示 AI 回應
- <strong>驗證</strong>您的設置運作正常

<strong>主要相依性</strong>（摘自 [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)）：
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-openai</artifactId>
</dependency>
<dependency>
    <groupId>com.openai</groupId>
    <artifactId>openai-java</artifactId>
</dependency>
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-identity</artifactId>
    <version>${azure-identity.version}</version>
</dependency>
```

POM 管理 OpenAI Java **4.63.1**，並明確設定 Azure Identity **1.18.6**。Spring AI 2 移除了 Azure 專屬 starter，Azure Identity 仍用于認證 bean。

<strong>設定</strong>（[application.yml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)）：
```yaml
spring:
  ai:
    openai:
      base-url: ${AZURE_OPENAI_ENDPOINT}
      microsoft-foundry: true
      chat:
        model: ${AZURE_OPENAI_DEPLOYMENT:gpt-5.6-luna}
        reasoning-effort: none
        max-completion-tokens: 500
```

無金鑰認證在 [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中明確配置，不是因為缺少 API 金鑰而推斷。其承載憑證採用 `DefaultAzureCredential`，範圍為 `https://ai.azure.com/.default`，`OpenAIClient` 目標 `/openai/v1`。應用程式將該客戶端提供給 Spring AI 的聊天模型，因此全局 `OPENAI_API_KEY` 不會覆蓋 Azure 認證。

聊天設定直接位於 `spring.ai.openai.chat` 下，沒有 `options` 塊。本課保留了帶有 `reasoning-effort: none` 和 500 代幣上限的 Chat Completions，沒有設定 `temperature` 或 `max-tokens`。請參閱 [範例設定參考](./examples/basic-chat-azure/README.md#spring-configuration) 以了解 API 選擇和工具調用指導。

## 總結

完成上述步驟後，您將擁有：

- 使用 Bicep + `azd` 以程式碼方式佈建 Azure AI Foundry 模型
- 啟動您的 Java 開發環境（不論是 Codespaces、開發容器還是本地）
- 使用無金鑰認證（Microsoft Entra ID）連接 Azure AI Foundry — 不需 API 金鑰
- 透過簡單範例測試並成功連接模型

## 下一步

[第三章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md)

## 故障排除

遇到問題？這裡提供常見問題及解決方案：

- **認證失敗（401/403）？** 
  - 執行 `az login` — 認證為無金鑰，您必須登入
  - 確認您的帳號對相關資源具備 **智能服務 OpenAI 使用者** 角色
  - 若剛佈建，請稍候一分鐘等待角色指派生效

- **找不到 Maven？** 
  - 若使用開發容器或 Codespaces，Maven 應已預裝
  - 本地設置需確保已安裝 Java 21+ 和 Maven 3.9+
  - 嘗試執行 `mvn --version` 檢查安裝狀態

- **找不到 `azd` 或佈建失敗？** 
  - 安裝 [Azure Developer CLI](https://aka.ms/azure-dev/install) 並執行 `azd auth login`
  - 選擇有 `gpt-5.6-luna` 和 `text-embedding-3-small` 可用之區域（如 `eastus2`），且您選擇的訂閱有足夠配額
  - 詳情請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)

- **開發容器無法啟動？** 
  - 確定 Docker Desktop 正在運行（本地開發）
  - 嘗試重新建置容器：`Ctrl+Shift+P` → 輸入 "Dev Containers: Rebuild Container"

- **應用程式編譯錯誤？**
  - 確認您在正確目錄：`02-SetupDevEnvironment/examples/basic-chat-azure`
  - 嘗試清理與編譯：`mvn clean compile`

> **需要協助？**：若仍有問題，請於存儲庫開啟議題，我們將協助您。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->