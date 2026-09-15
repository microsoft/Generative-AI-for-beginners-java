# 為 Java 設置生成式 AI 的開發環境

> **快速開始：** 使用 Bicep + `azd` 在幾分鐘內於 **Azure AI Foundry** 以程式碼方式配置您的 AI 模型 — 請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)。認證採用<strong>無金鑰</strong>（Microsoft Entra ID），因此無需管理 API 金鑰。

## 您將學習到什麼

- 為 AI 應用設置 Java 開發環境
- 選擇並配置您偏好的開發環境（以雲端優先的 Codespaces、本地開發容器或完整本地設置）
- 通過連接至 Azure AI Foundry 模型測試您的設置

## 目錄

- [您將學習到什麼](#您將學習到什麼)
- [介紹](#介紹)
- [步驟 1：設置您的開發環境](#步驟-1：設置您的開發環境)
  - [選項 A：GitHub Codespaces（推薦）](#選項-a：github-codespaces（推薦）)
  - [選項 B：本地開發容器](#選項-b：本地開發容器)
  - [選項 C：使用您現有的本地安裝](#選項-c：使用您現有的本地安裝)
- [步驟 2：配置 Azure AI Foundry](#步驟-2：配置-azure-ai-foundry)
- [步驟 3：測試您的設置](#步驟-3：測試您的設置)
- [故障排除](#故障排除)
- [總結](#總結)
- [下一步](#下一步)

## 介紹

本章將引導您完成設定開發環境。我們將在整個課程中使用 **Azure AI Foundry** 作為模型。您可利用 Bicep 和 Azure 開發者 CLI（`azd`）以程式碼方式配置模型，然後透過<strong>無金鑰認證</strong>（Microsoft Entra ID）連接 — 無需複製或洩露 API 金鑰。

**無需本地設置！** 您可以使用 GitHub Codespaces，該服務提供於瀏覽器中完整的開發環境，並可直接從中配置 Foundry。

我們之所以使用 **Azure AI Foundry**，是因為它：
- <strong>以程式碼方式配置</strong> — 一條 `azd up` 指令即可部署帳戶及模型部署
- <strong>無需金鑰</strong> — 使用您的 Azure 登入或管理身分驗證
- <strong>適合生產環境</strong> — 同一套程式碼可於本地及 Azure 執行
- <strong>具彈性</strong> — 只需更改部署名稱便可替換模型，無需更動程式碼

> <strong>注意</strong>：Azure AI Foundry 的部署按代幣數量收費（隨用隨付）。有關配置、地區及費用詳情，請參考 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)。


## 步驟 1：設置您的開發環境

<a name="quick-start-cloud"></a>

我們已建立預先配置的開發容器，以最小化設置時間並確保您擁有完成此 Java 生成式 AI 課程所需的所有工具。請選擇您偏好的開發方法：

### 環境設置選項：

#### 選項 A：GitHub Codespaces（推薦）

**2 分鐘內開始撰寫程式碼 — 無需本地安裝！**

1. 將此儲存庫 fork 到您的 GitHub 帳戶
   > <strong>注意</strong>：如果您想編輯基礎配置，請參考 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 點選 **Code** → **Codespaces** 索引標籤 → **...** → **New with options...**
3. 使用預設值 — 系統將選擇為本課程定制的 **生成式 AI Java 開發環境** 的 <strong>開發容器配置</strong>
4. 點選 **Create codespace**
5. 等待約 2 分鐘，環境即建立完成
6. 前往 [步驟 2：配置 Azure AI Foundry](#步驟-2：配置-azure-ai-foundry)

<img src="../../../translated_images/zh-HK/codespaces.9945ded8ceb431a5.webp" alt="截圖：Codespaces 子選單" width="50%">

<img src="../../../translated_images/zh-HK/image.833552b62eee7766.webp" alt="截圖：New with options" width="50%">

<img src="../../../translated_images/zh-HK/codespaces-create.b44a36f728660ab7.webp" alt="截圖：Create codespace 選項" width="50%">


> **Codespaces 的優點**：
> - 無需本地安裝
> - 可於任何具瀏覽器的裝置使用
> - 預先配置所有工具與相依性
> - 個人帳戶每月免費 60 小時
> - 為所有學習者提供一致的環境

#### 選項 B：本地開發容器

**適合偏好使用 Docker 進行本地開發的開發者**

1. fork 並將此儲存庫 clone 至您的本地機器
   > <strong>注意</strong>：如果您想編輯基礎配置，請參考 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 安裝 [Docker Desktop](https://www.docker.com/products/docker-desktop/) 和 [VS Code](https://code.visualstudio.com/)
3. 在 VS Code 安裝 [Dev Containers 擴充套件](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
4. 在 VS Code 開啟儲存庫資料夾
5. 出現提示時，點選 **Reopen in Container**（或使用 `Ctrl+Shift+P` →「Dev Containers: Reopen in Container」）
6. 等待容器建立並啟動
7. 前往 [步驟 2：配置 Azure AI Foundry](#步驟-2：配置-azure-ai-foundry)

<img src="../../../translated_images/zh-HK/devcontainer.21126c9d6de64494.webp" alt="截圖：開發容器設置" width="50%">

<img src="../../../translated_images/zh-HK/image-3.bf93d533bbc84268.webp" alt="截圖：開發容器建立完成" width="50%">

#### 選項 C：使用您現有的本地安裝

**適合已有 Java 環境的開發者**

前置條件：
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) 或您偏好的 IDE

步驟：
1. 將此儲存庫 clone 至您的本地機器
2. 在您的 IDE 中開啟專案
3. 前往 [步驟 2：配置 Azure AI Foundry](#步驟-2：配置-azure-ai-foundry)

> <strong>專業提示</strong>：如果您的機器效能較低但想在本地使用 VS Code，請使用 GitHub Codespaces！您可將本地的 VS Code 連接到雲端託管的 Codespace，兩者兼得最佳體驗。

<img src="../../../translated_images/zh-HK/image-2.fc0da29a6e4d2aff.webp" alt="截圖：本地開發容器實例已建立" width="50%">


## 步驟 2：配置 Azure AI Foundry

將課程中的 AI 模型部署至 Azure AI Foundry，方式為程式碼式操作。於儲存庫根目錄執行：

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` 會提示輸入環境名稱、訂閱和地區，配置一個包含 `gpt-5.6-luna` 和 `text-embedding-3-small` 部署的 Azure AI Foundry 帳戶，並將端點寫入示例的 `.env` 檔案 — 全過程採用<strong>無金鑰</strong>認證（無需 API 金鑰）。

> **完整操作流程：** 請參考 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)，了解前置條件、手動（門戶）選項、地區建議及費用/清理備註。

## 步驟 3：測試您的設置

一旦 Foundry 模型配置完成，使用 [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) 中的示例應用測試連接。

1. 在您的開發環境中打開終端機。
2. 切換到示例資料夾：
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. 確保您已登入（無金鑰認證需要令牌）：
   ```bash
   az login
   ```
   > 若您執行過 `azd up`，`.env` 檔案已自動寫入您的端點。
4. 執行應用程式：
   ```bash
   mvn clean spring-boot:run
   ```

您應該能看到來自 `gpt-5.6-luna` 模型的回應。

### 理解範例程式碼

[basic-chat 範例](./examples/basic-chat-azure/README.md) 使用 **Spring Boot 4.1.1** 與 **Spring AI 2.0.1**。Spring AI 的 `ChatClient` 背後是官方 OpenAI Java SDK，並使用無金鑰認證連接至 Azure OpenAI **v1** 端點。

**程式碼功能：**
- 使用您的 Azure 登入（Microsoft Entra ID）連接 Azure AI Foundry — 無需 API 金鑰
- 傳送提示到 `gpt-5.6-luna` 模型
- 接收並顯示 AI 回應
- 驗證您的設置是否正確運作

<strong>關鍵相依性</strong>（摘自 [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)）：
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

POM 明確管理 OpenAI Java **4.63.1** 和 Azure Identity **1.18.6**。Spring AI 2 移除了 Azure 專屬啟動器，但仍需 Azure Identity 作為憑證 Bean。

<strong>配置</strong>（[application.yml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml)）：
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

無金鑰認證在 [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中明確配置，非從缺少 API 金鑰推斷。其攜帶憑證使用帶有 `https://ai.azure.com/.default` 範圍的 `DefaultAzureCredential`，`OpenAIClient` 目標為 `/openai/v1`。應用程式將該客戶端提供給 Spring AI 的聊天模型，因此全域 `OPENAI_API_KEY` 不會覆寫 Azure 認證。

聊天設置直接位於 `spring.ai.openai.chat` 下，無 `options` 區塊。課程保留 Chat Completions，設定為 `reasoning-effort: none` 且完成代幣上限為 500；未設定 `temperature` 或 `max-tokens`。有關 API 選擇和工具呼叫之指引，請參考 [範例配置參考](./examples/basic-chat-azure/README.md#spring-configuration)。

## 總結

完成上述步驟後，您將擁有：

- 以 Bicep + `azd` 以程式碼方式配置的 Azure AI Foundry 模型
- 運行中的 Java 開發環境（可為 Codespaces、開發容器或本地）
- 以無金鑰認證（Microsoft Entra ID）連接至 Azure AI Foundry — 無需 API 金鑰
- 使用簡單示例成功測試所有功能，與模型互動

## 下一步

[第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md)

## 故障排除

遇到問題？以下是常見問題與解決方案：

- **認證失敗（401/403）？** 
  - 執行 `az login` — 認證為無金鑰方式，必須登入
  - 確認您的帳戶在資源上擁有 **認知服務 OpenAI 使用者** 角色
  - 若剛配置，請等待數分鐘以讓角色指派生效

- **找不到 Maven？** 
  - 使用開發容器或 Codespaces 時，Maven 應已預先安裝
  - 本地設置時，請確保安裝 Java 21+ 和 Maven 3.9+
  - 嘗試執行 `mvn --version` 驗證安裝

- **找不到 `azd` 或配置失敗？** 
  - 安裝 [Azure Developer CLI](https://aka.ms/azure-dev/install) 並執行 `azd auth login`
  - 選擇一個支援 `gpt-5.6-luna` 和 `text-embedding-3-small` 的地區（例如 `eastus2`），且訂閱有足夠配額
  - 詳情請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)

- **開發容器無法啟動？** 
  - 確認 Docker Desktop 已啟動（本地開發用）
  - 嘗試重建容器：`Ctrl+Shift+P` → 「Dev Containers: Rebuild Container」

- **應用編譯錯誤？**
  - 確認您位於正確目錄： `02-SetupDevEnvironment/examples/basic-chat-azure`
  - 嘗試清理並編譯：`mvn clean compile`

> **需要幫忙？** 如仍有問題，請在儲存庫開啟議題，我們會協助您。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議進行專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->