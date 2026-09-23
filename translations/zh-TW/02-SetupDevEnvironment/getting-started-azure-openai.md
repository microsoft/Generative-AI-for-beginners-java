# 設定 Azure AI Foundry 的開發環境

> 本指南為本課程的 Java AI 應用程式，設定 **Azure AI Foundry** 模型，採用 <strong>無金鑰</strong> 認證（Microsoft Entra ID）— 無需管理 API 金鑰。工具使用新手？請先參考 [開發環境指南](./README.md)。

本指南為本課程的 Java AI 應用程式，設定 **Azure AI Foundry** 模型。您有兩種方式：

- **選項 A — 使用 `azd` + Bicep 部署 (建議)：** 一個指令即可完成 Foundry 帳戶和模型的程式碼部署。無需點擊入口網站。
- **選項 B — 在 Azure AI Foundry 入口網站手動建立資源**。

兩者皆使用 <strong>無金鑰認證</strong>（Microsoft Entra ID）— 無需複製或外洩 API 金鑰。

## 目錄

- [建立了什麼](#建立了什麼)
- [事前準備](#事前準備)
- [選項 A：使用 azd + Bicep 部署（建議）](#option-a-provision-with-azd--bicep-recommended)
- [選項 B：手動建立資源](#選項-b：手動建立資源)
- [設定您的環境](#設定您的環境)
- [測試您的設定](#測試您的設定)
- [接下來做什麼？](#接下來做什麼？)
- [資源](#資源)
- [額外資源](#額外資源)

## 建立了什麼

[`infra/`](../../../02-SetupDevEnvironment/infra) 中的 Bicep 模板會部署：

- 一個 **Azure AI Foundry** 帳戶（`Microsoft.CognitiveServices/accounts`，種類為 `AIServices`）及一個專案
- 一個 <strong>聊天</strong> 部署 - GPT-5.6 Luna（`gpt-5.6-luna`）、版本 `2026-07-09`，具備 `GlobalStandard` 容量 10（本模型每分鐘 10 請求及 10,000 代幣）
- 一個 **Embedding** 部署 - `text-embedding-3-small`，版本 `1`（於後續章節使用）
- 一個 <strong>無金鑰角色指派</strong>（`Cognitive Services OpenAI User`），讓您可以使用 `az login` 登入而無需管理金鑰

## 事前準備

- [Azure 訂閱](https://azure.microsoft.com/free/)
- [Azure Developer CLI (`azd`)](https://aka.ms/azure-dev/install)
- [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)
- [Java 21+](https://learn.microsoft.com/java/openjdk/download) 及 [Maven 3.9+](https://maven.apache.org/download.cgi)

## 選項 A：使用 azd + Bicep 部署（建議）

在 `02-SetupDevEnvironment` 資料夾中執行：

```bash
cd 02-SetupDevEnvironment

# 登入（兩個工具）
azd auth login
az login

# 設置 Foundry 帳戶和模型部署
azd up
```

`azd` 會提示您輸入 <strong>環境名稱</strong>（例如 `genai-java`）、<strong>訂閱</strong> 和 <strong>區域</strong>。請選擇您擁有的訂閱以及 `gpt-5.6-luna` 和 `text-embedding-3-small` 支援的區域，例如 `eastus2`。請確認該訂閱在該區域對模型及部署類型有足夠的配額；可用性和配額會依訂閱有所不同。

部署完成後，azd 會：

1. 部署 [`infra/main.bicep`](../../../02-SetupDevEnvironment/infra/main.bicep) 中定義的所有資源。
2. 執行後置部署鉤子，將端點和部署名稱寫入 [`examples/basic-chat-azure/.env`](../../../02-SetupDevEnvironment/examples/basic-chat-azure)（不包含任何秘密）。

> **小提示：** 您可以隨時重新執行 `azd up` 以套用變更。執行 `azd down` 則會刪除所有資源並停止產生費用。

查看產生的設定：

```bash
azd env get-values
```

現在可以跳轉到 [測試您的設定](#測試您的設定)。

## 選項 B：手動建立資源

偏好使用入口網站？請手動建立資源：

1. 前往 [Azure AI Foundry 入口網站](https://ai.azure.com/)並登入。
2. <strong>建立專案</strong>（同時也建立 AI Foundry 資源）。取名為 `GenAIJava` 等。
3. 在專案中，開啟 **Models + endpoints** → **Deploy model** → **Deploy base model**。
4. 部署 **GPT-5.6 Luna**（模型和部署名稱為 `gpt-5.6-luna`、版本 `2026-07-09`）且容量為 **Global Standard** 10。若需要 embedding 範例，亦重複部署 **text-embedding-3-small**，版本 `1`。
5. 在 **Overview** 頁面複製 **endpoint**（例如 `https://<resource>.openai.azure.com/`）。
6. 給自己無金鑰存取權：在資源上開啟 **Access control (IAM)** → **Add role assignment** → 指派 **Cognitive Services OpenAI User** 角色給自己的帳戶。

> **仍有問題？** 請參考 [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/how-to/create-projects)。

## 設定您的環境

**如果您使用選項 A (`azd up`)**，您的設定檔已經建立 — 不需額外設定。可以直接前往 [測試您的設定](#測試您的設定)。

**如果使用選項 B（手動）**，請自行建立範例的 `.env` 檔案：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure
cp .env.example .env
```

編輯 `.env` 檔案，填入您的端點（無金鑰，採用無金鑰認證方式）：

```bash
AZURE_OPENAI_ENDPOINT=https://<your-resource>.openai.azure.com/
AZURE_OPENAI_DEPLOYMENT=gpt-5.6-luna
```

使用資源的 Azure OpenAI 端點，而非專案 URL。basic-chat 應用會自動解析成 `/openai/v1`，並使用明確的 bearer-token 用戶端；不需要 API 金鑰。

> **安全提醒：** 不需要儲存 API 金鑰。您透過 `az login`（本機）或受管身分（在 Azure）使用 Microsoft Entra ID 做認證。`.env` 檔案只包含非機密設定，且已有 `.gitignore` 覆蓋。

## 測試您的設定

請先確認已登入，使無金鑰認證能取得令牌，接著執行範例：

```bash
cd 02-SetupDevEnvironment/examples/basic-chat-azure

az login          # 如果你尚未登入
mvn clean spring-boot:run
```

您應該會看到來自 `gpt-5.6-luna` 模型的回應。請依序執行範例以避免超出預設的小配額；若遇到 HTTP 429，請等候重試間隔後再試。

> **VS Code 使用者：** 按 `F5` 即可執行。應用程式會自動載入您的 `.env`。

> **完整範例：** 詳細資料與故障排除，請參見 [使用 Azure AI Foundry 的基本聊天範例](./examples/basic-chat-azure/README.md)。

## 接下來做什麼？

部署完成並成功執行範例後，您將擁有：
- 已部署包含 `gpt-5.6-luna` 與 `text-embedding-3-small` 的 Azure AI Foundry
- 無金鑰認證（Microsoft Entra ID）— 無需管理金鑰
- 包含您的端點與部署名稱的本地 `.env`
- 可立即使用的 Java 開發環境

<strong>繼續閱讀</strong> [第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md)，開始打造 AI 應用程式吧！

## 資源

- [Azure Developer CLI (azd)](https://aka.ms/azure-dev/install)
- [使用 Microsoft Entra ID 的無金鑰認證](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/)
- [Spring AI 2 OpenAI Java SDK 過渡指南](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 及 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)

## 額外資源

- [下載 VS Code](https://code.visualstudio.com/Download)
- [取得 Docker Desktop](https://www.docker.com/products/docker-desktop)
- [開發容器設定](../../../.devcontainer/devcontainer.json)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->