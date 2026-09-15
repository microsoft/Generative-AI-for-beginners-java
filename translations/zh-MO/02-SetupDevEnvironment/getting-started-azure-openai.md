# 為 Azure AI Foundry 設置開發環境

> 本指南為本課程中的 Java AI 應用程式設定 **Azure AI Foundry** 模型，使用 <strong>無金鑰</strong> 認證（Microsoft Entra ID）— 無需管理 API 金鑰。工具使用新手？請先參閱[開發環境指南](./README.md)。

本指南為本課程中的 Java AI 應用程式設定 **Azure AI Foundry** 模型。您有兩種方式：

- **選項 A — 使用 `azd` + Bicep 一鍵部署（推薦）：** 使用一條指令以程式碼方式部署 Foundry 帳戶和模型，無需在入口網站中點擊。
- **選項 B — 在 Azure AI Foundry 入口網站中手動創建資源。**

兩種方式都使用 <strong>無金鑰認證</strong>（Microsoft Entra ID）— 無需複製或洩漏 API 金鑰。

## 目錄

- [建立了什麼](#建立了什麼)
- [先決條件](#先決條件)
- [選項 A：使用 azd + Bicep 部署（推薦）](#option-a-provision-with-azd--bicep-recommended)
- [選項 B：手動建立資源](#選項-b：手動建立資源)
- [配置您的環境](#配置您的環境)
- [測試您的設定](#測試您的設定)
- [接下來呢？](#接下來呢？)
- [資源](#資源)
- [附加資源](#附加資源)

## 建立了什麼

[`infra/`](../../../02-SetupDevEnvironment/infra) 中的 Bicep 模板會建立：

- 一個 **Azure AI Foundry** 帳戶（`Microsoft.CognitiveServices/accounts`，類型 `AIServices`）及其專案
- 一個 <strong>聊天</strong> 部署 — GPT-5.6 Luna (`gpt-5.6-luna`)，版本 `2026-07-09`，`GlobalStandard` 容量 10（此模型每分鐘 10 次請求和 10,000 個令牌）
- 一個 <strong>嵌入</strong> 部署 — `text-embedding-3-small` 版本 `1`（稍後章節中使用）
- 一個 <strong>無金鑰角色分配</strong>（`Cognitive Services OpenAI User`），讓您可以使用 `az login` 登入，而無需管理金鑰

## 先決條件

- 一個 [Azure 訂閱](https://azure.microsoft.com/free/)
- [Azure 開發者 CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) 和 [Maven 3.9+](https://maven.apache.org/download.cgi)

## 選項 A：使用 azd + Bicep 部署（推薦）

在 `02-SetupDevEnvironment` 資料夾中：

```bash
cd 02-SetupDevEnvironment

# 登入（兩個工具）
azd auth login
az login

# 配置 Foundry 帳戶及模型部署
azd up
```

`azd` 會提示輸入 <strong>環境名稱</strong>（例如 `genai-java`）、<strong>訂閱</strong> 以及 <strong>地區</strong>。請選擇您自己的訂閱和一個支援 `gpt-5.6-luna` 及 `text-embedding-3-small` 的地區，例如 `eastus2`。確認該訂閱在該地區有足夠的配額來使用模型和部署類型；配額和可用性依訂閱而異。

部署完成後，azd 將會：

1. 部署 [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) 中定義的所有內容。
2. 執行一個部署後掛鉤，將帶有您的端點和部署名稱（無秘密資訊）的 [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) 寫入。

> **提示：** 您可以隨時重新執行 `azd up` 以套用更改。執行 `azd down` 來刪除所有資源並停止產生費用。

查看產生的設定：

```bash
azd env get-values
```

現在請跳轉至[測試您的設定](#測試您的設定)。

## 選項 B：手動建立資源

喜歡使用入口網站？請手動建立資源：

1. 前往 [Azure AI Foundry 入口網站](https://ai.azure.com/) 並登入。
2. <strong>建立專案</strong>（這也會創建 AI Foundry 資源）。命名為 `GenAIJava` 或其他名稱。
3. 在專案中，開啟 **Models + endpoints** → **Deploy model** → **Deploy base model**。
4. 部署 **GPT-5.6 Luna**（模型和部署名稱為 `gpt-5.6-luna`，版本 `2026-07-09`），採用 **Global Standard** 容量 10。若需要嵌入示例，請重複部署 **text-embedding-3-small**，版本 `1`。
5. 從 **Overview** 複製 **endpoint**（例如 `https://<resource>.openai.azure.com/`）。
6. 授予自已無金鑰存取權：在資源上，開啟 **存取控制 (IAM)** → <strong>新增角色分配</strong> → 指派 **Cognitive Services OpenAI User** 角色給您的帳號。

> **仍有問題？** 請參考 [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)。

## 配置您的環境

**如果您使用選項 A (`azd up`)**，您的設定檔已自動建立 — 無需手動配置。直接跳轉至[測試您的設定](#測試您的設定)。

**如果您使用選項 B（手動）**，請自行建立示例的 `.env` 檔案：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

使用您的端點編輯 `.env`（無需金鑰 — 授權採用無金鑰方式）：

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

使用資源的 Azure OpenAI 端點，不是專案網址。basic-chat 應用程式會將其解析至 `/openai/v1`，並設定明確的 bearer-token 客戶端；不需要 API 金鑰。

> **安全提示：** 無需儲存 API 金鑰。您透過 `az login`（本地）或管理身份（Azure 中）使用 Microsoft Entra ID 認證。`.env` 檔只包含非秘密設定，且已被 `.gitignore` 保護。

## 測試您的設定

確保您已登入使無金鑰認證獲取令牌，然後執行示例：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # 如果你尚未登入
mvn clean spring-boot:run
```

您應能收到 `gpt-5.6-luna` 模型的回應。請依序運行示例以維持在預設的小配額內；若收到 HTTP 429，請等待重試間隔後再試。

> **VS Code 使用者：** 按下 `F5` 即可執行。應用程式會自動載入您的 `.env`。

> **完整範例：** 請參考 [使用 Azure AI Foundry 的基本聊天示例](./examples/basic-chat-azure/README.md) 了解詳情和疑難排解。

## 接下來呢？

部署及成功執行示例後，您將擁有：
- 部署好的 Azure AI Foundry，包含 `gpt-5.6-luna` 和 `text-embedding-3-small`
- 無金鑰認證（Microsoft Entra ID）— 無需管理金鑰
- 包含端點和部署名稱的本地 `.env`
- 已準備好的 Java 開發環境

<strong>繼續閱讀</strong> [第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md)，開始打造 AI 應用程式！

## 資源

- [Azure 開發者 CLI (azd)](https://aka.ms/azure-dev/install)
- [使用 Microsoft Entra ID 的無金鑰認證](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK 過渡](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 支援 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## 附加資源

- [下載 VS Code](https://code.visualstudio.com/Download)
- [下載 Docker Desktop](https://www.docker.com/products/docker-desktop)
- [開發容器配置](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->