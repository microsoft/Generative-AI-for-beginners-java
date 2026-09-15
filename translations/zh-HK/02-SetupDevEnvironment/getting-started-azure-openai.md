# 設定 Azure AI Foundry 的開發環境

> 本指南設定本課程中 Java AI 應用程式所使用的 **Azure AI Foundry** 模型，採用 <strong>無金鑰</strong> 認證方式（Microsoft Entra ID）——無需管理任何 API 密鑰。還不熟悉這套工具？請先參考 [開發環境指南](./README.md)。

本指南說明如何設定本課程中 Java AI 應用程式使用的 **Azure AI Foundry** 模型。您有兩種路徑可選：

- **選項 A — 使用 `azd` + Bicep 部署（推薦）：** 一條命令即可建置 Foundry 帳戶與模型代碼。無需點擊入口網站。
- **選項 B — 在 Azure AI Foundry 入口網站手動建立資源**。

兩種路徑皆採用 <strong>無金鑰認證</strong>（Microsoft Entra ID）——無需複製或洩漏 API 金鑰。

## 目錄

- [建立哪些內容](#建立哪些內容)
- [前置需求](#前置需求)
- [選項 A：使用 azd + Bicep 部署（推薦）](#option-a-provision-with-azd--bicep-recommended)
- [選項 B：手動建立資源](#選項-b：手動建立資源)
- [設定您的環境](#設定您的環境)
- [測試您的設定](#測試您的設定)
- [接下來做什麼？](#接下來做什麼？)
- [資源](#資源)
- [其他資源](#其他資源)

## 建立哪些內容

[`infra/`](../../../02-SetupDevEnvironment/infra) 中的 Bicep 模板會部署：

- 一個 **Azure AI Foundry** 帳戶（`Microsoft.CognitiveServices/accounts`，類型 `AIServices`）及其專案
- 一個 <strong>聊天</strong> 部署 - GPT-5.6 Luna（`gpt-5.6-luna`），版本 `2026-07-09`，容量為 `GlobalStandard` 10（此模型限制為每分鐘 10 個請求及 10,000 個 token）
- 一個 <strong>嵌入</strong> 部署 - `text-embedding-3-small`，版本 `1`（後續章節會使用）
- 一個 <strong>無金鑰角色指派</strong>（`Cognitive Services OpenAI User`），讓你能使用 `az login` 登入替代管理金鑰

## 前置需求

- 一個 [Azure 訂閱](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) 與 [Maven 3.9+](https://maven.apache.org/download.cgi)

## 選項 A：使用 azd + Bicep 部署（推薦）

於 `02-SetupDevEnvironment` 資料夾中：

```bash
cd 02-SetupDevEnvironment

# 登入（兩個工具）
azd auth login
az login

# 配置 Foundry 帳戶及模型部署
azd up
```

`azd` 會提示輸入 <strong>環境名稱</strong>（例如 `genai-java`）、<strong>訂閱</strong> 與 <strong>區域</strong>。請選擇您自己的訂閱與一個有提供 `gpt-5.6-luna` 和 `text-embedding-3-small` 服務的區域，例如 `eastus2`。確認所選的訂閱在該區域對該模型及部署類型有足夠配額；不同訂閱的可用資源與配額可能有所不同。

部署完成後，azd 將會：

1. 部署 [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) 中定義的所有資源。
2. 執行部署後掛鉤，將您的端點與部署名稱寫入 [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure)（不含任何祕密資訊）。

> **小提示：** 可隨時重新執行 `azd up` 以套用變更，使用 `azd down` 則會刪除所有資源並停止產生費用。

若要查看已生成的設定：

```bash
azd env get-values
```

接著跳至 [測試您的設定](#測試您的設定)。

## 選項 B：手動建立資源

偏好使用入口網站？請手動建立資源：

1. 前往 [Azure AI Foundry 入口網站](https://ai.azure.com/)並登入。
2. <strong>建立專案</strong>（此步也會建立一個 AI Foundry 資源）。命名為 `GenAIJava` 等。
3. 在您的專案中，打開 **Models + endpoints** → **Deploy model** → **Deploy base model**。
4. 部署 **GPT-5.6 Luna**（模型及部署名稱為 `gpt-5.6-luna`，版本 `2026-07-09`），容量設定為 **Global Standard** 10。若需要後續章節的嵌入範例，也請同時部署 **text-embedding-3-small**，版本 `1`。
5. 於 **Overview** 頁面複製 <strong>端點</strong>（例如 `https://<resource>.openai.azure.com/`）。
6. 賦予自己無金鑰存取權限：在資源中開啟 **Access control (IAM)** → **Add role assignment** → 指派 **Cognitive Services OpenAI User** 給您的帳戶。

> **仍有問題？** 請參閱 [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)。

## 設定您的環境

**如果您使用了選項 A (`azd up`)**，設定檔已經建立完成，無需額外操作。請跳至 [測試您的設定](#測試您的設定)。

**如果您使用了選項 B（手動）**，請自行建立範例的 `.env` 檔：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

編輯 `.env` ，填入您的端點（不需金鑰，採用無金鑰驗證）：

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

請使用您的 Azure OpenAI 端點，不是專案 URL。basic-chat 應用會將此 URL 解析至 `/openai/v1` 並設定明確的 bearer-token 用戶端，不需要 API 金鑰。

> **安全說明：** 沒有任何 API 金鑰需要存放。您是透過 `az login`（本機端）或管理身分識別（Azure 內）使用 Microsoft Entra ID 來認證。`.env` 檔只包含非祕密設定，且已被 `.gitignore` 排除。

## 測試您的設定

請確認您已登入，以便無金鑰認證能取得憑證，然後執行範例：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # 如果你未有登錄
mvn clean spring-boot:run
```

您應該會看到 `gpt-5.6-luna` 模型的回應。依序執行範例，避免超出小額預設配額；如果收到 HTTP 429，請等候重試間隔後再試。

> **VS Code 使用者：** 按 `F5` 來執行。應用程式會自動載入您的 `.env`。

> **完整範例：** 查閱 [Azure AI Foundry 基本聊天範例](./examples/basic-chat-azure/README.md) 以獲得詳細資訊與故障排除。

## 接下來做什麼？

成功部署並執行範例後，您將擁有：
- 已部署包含 `gpt-5.6-luna` 與 `text-embedding-3-small` 的 Azure AI Foundry
- 採用無金鑰驗證（Microsoft Entra ID）——無需管理任何密鑰
- 本機 `.env` 檔，包含您的端點與部署名稱
- 準備好的 Java 開發環境

<strong>請繼續參考</strong> [第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md) 開始打造 AI 應用！

## 資源

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [使用 Microsoft Entra ID 進行無金鑰認證](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/)
- [從 Spring AI 2 過渡至 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 與 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## 其他資源

- [下載 VS Code](https://code.visualstudio.com/Download)
- [取得 Docker Desktop](https://www.docker.com/products/docker-desktop)
- [開發容器設定](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議進行專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->