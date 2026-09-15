# 使用 Azure AI Foundry 的基本聊天示範 - 端到端範例

這個範例是使用 **Azure AI Foundry** 模型並透過 <strong>無密鑰驗證</strong>（Microsoft Entra ID）連接的簡單 Spring Boot 應用程式，用於測試您的設定。它使用 Spring AI 的 `ChatClient`，其背後是 **官方 OpenAI Java SDK** 和 **Azure OpenAI v1** 端點。

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) 中的版本為 Spring Boot **4.1.1**、Spring AI **2.0.1**、OpenAI Java **4.63.1**、Azure Identity **1.18.6** 和 dotenv-java **3.2.0**。範例使用 `spring-ai-starter-model-openai`，並明確宣告 `openai-java` 與 `azure-identity`；Spring AI 2 移除了舊的 Azure OpenAI 起始套件。

## 目錄

- [先決條件](#先決條件)
- [快速開始](#快速開始)
- [驗證機制說明](#驗證機制說明)
- [執行應用程式](#執行應用程式)
  - [使用 Maven](#使用-maven)
  - [使用 VS Code](#使用-vs-code)
  - [預期輸出](#預期輸出)
- [設定參考](#設定參考)
  - [環境變數](#環境變數)
  - [Spring 設定](#spring-設定)
- [故障排除](#故障排除)
  - [常見問題](#常見問題)
  - [除錯模式](#除錯模式)
- [後續步驟](#後續步驟)
- [資源](#資源)

## 先決條件

執行此範例前，請確保您擁有：

- 一個具備 `gpt-5.6-luna` 部署的 Azure AI Foundry 資源 — 可透過 `azd up` 指令快建，或參考 [Azure AI Foundry 設定指南](../../getting-started-azure-openai.md) 手動建立
- 該資源上的 **認知服務 OpenAI 使用者** 角色（Bicep 模板會自動指派此角色）
- 已登入的 [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli) (`az login`)
- Java 21+ 與 Maven 3.9+

> **不需 API 金鑰** — 驗證透過 Microsoft Entra ID 無密鑰進行。

## 快速開始

```bash
# 1. 導航至專案
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. 登入，以便無需密鑰的身份驗證可以取得令牌
az login

# 3. 配置端點
#    - 如果你執行過 `azd up`，.env 已為你寫入（可跳過此步驟）。
#    - 否則請複製範本並設定 AZURE_OPENAI_ENDPOINT：
cp .env.example .env

# 4. 執行應用程式
mvn spring-boot:run
```

## 驗證機制說明

此範例使用 **Microsoft Entra ID** 進行驗證 — 無需 API 金鑰。

應用程式在 [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中明確設定驗證：

1. `azureCredential()` 使用 `AuthenticationUtil.getBearerTokenSupplier` 搭配 `DefaultAzureCredential` 和 `https://ai.azure.com/.default` 範圍建立 `BearerTokenCredential`。
2. `azureOpenAiClient()` 使用 `OpenAIOkHttpClient.builder()` 構建 `OpenAIClient`，將資源端點解析為 `/openai/v1`，並以 `.credential(...)` 提供持有者權杖憑證。
3. `azureChatModel()` 將此客戶端參數輸入 Spring AI 的 `OpenAiChatModel`，作為本課程 `ChatClient` 的後端。

這些明確的 bean 可避免全域 `OPENAI_API_KEY` 覆蓋 Azure 驗證。僅從 YAML 中省略 API 金鑰並非完整驗證設定。`DefaultAzureCredential` 可在本地使用您的 `az login` 會話或 Azure 管理身分；所選的身分必須具備上述的資源角色。

## 執行應用程式

### 使用 Maven

```bash
mvn spring-boot:run
```

### 使用 VS Code

1. 在 VS Code 開啟專案
2. 按下 `F5` 或使用「執行與除錯」面板
3. 選擇「Spring Boot-BasicChatApplication」設定

> <strong>注意</strong>：應用程式會從其工作目錄載入 `.env`，包括透過 VS Code 啟動時。

### 預期輸出

執行成功後的示範輸出（已省略啟動日誌；輸出回應文字會變動）：

```text
Starting Basic Chat with Azure OpenAI...
Environment variables loaded from .env file
Endpoint: https://your-resource.openai.azure.com/
Deployment: gpt-5.6-luna
Auth: keyless (Microsoft Entra ID via DefaultAzureCredential)
Connecting to Azure OpenAI...
Sending prompt: What is AI in a short sentence? Max 100 words.

AI Response:
================
AI, or Artificial Intelligence, is the simulation of human intelligence in machines programmed to think and learn like humans.
================

Success! Azure OpenAI connection is working correctly.
```

## 設定參考

### 環境變數

| 變數 | 說明 | 是否必須 | 範例 |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry（Azure OpenAI）端點 URL | 是 | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天模型部署名稱 | 否 | `gpt-5.6-luna` （預設） |

> 不存在 API 金鑰變數 — 驗證是無密鑰的（透過 `az login` 使用 Microsoft Entra ID）。

### Spring 設定

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 使用 `spring.ai.openai` 前綴與扁平化的聊天屬性（無 `options` 區塊）：

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

`model` 是 **Azure 部署名稱**。驗證來自上述明確的 bean，而非 `api-key` 設定。本課程關閉推理並將完成 token 上限設為 500；保留 `temperature` 和傳統 `max-tokens` 未設定。

Microsoft 推薦 [官方 OpenAI SDK 與 Azure OpenAI v1 及 Responses API 用於新應用程式](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)。聊天完成（Chat Completions）仍適用於此基於訊息的既有課程。對 GPT-5.6，包含工具的聊天完成請將 `reasoning_effort` 設為 `none`；如需結合推理與工具請使用 Responses。詳見 [推理模型與工具呼叫](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)。

## 故障排除

### 常見問題

<details>
<summary><strong>錯誤：401 / "PermissionDenied" / 權杖錯誤</strong></summary>

- 執行 `az login` — 無密鑰驗證需有效登入以取得權杖
- 確認帳號在資源上有 **認知服務 OpenAI 使用者** 角色
- 若剛指派角色，請稍待片刻讓它生效
- 確認您在正確的租戶/訂閱中（`az account show`）
</details>

<details>
<summary><strong>錯誤："端點無效" / 連線錯誤</strong></summary>

- 確保 `AZURE_OPENAI_ENDPOINT` 是完整的基本 URL（例如 `https://your-resource.openai.azure.com/`）
- 檢查結尾斜線是否一致
- 驗證端點與已配置資源相符（`azd env get-values`）
</details>

<details>
<summary><strong>錯誤："找不到部署"</strong></summary>

- 確認 `AZURE_OPENAI_DEPLOYMENT` 與 Azure 中的部署名稱吻合
- 檢查模型是否成功部署且處於啟用狀態
- 預設部署名稱為 `gpt-5.6-luna`
</details>

<details>
<summary><strong>錯誤：429 / 超出速率限制</strong></summary>

- 預設的 GPT-5.6 Luna 部署有全球標準容量 10：每分鐘 10 個請求，每分鐘 10,000 個 token
- 請依序執行範例並在服務重試間隔後重試
- 此基本範例關閉自動 SDK 重試，失敗請求會直接回報
</details>

<details>
<summary><strong>VS Code：環境變數未載入</strong></summary>

- 確保 `.env` 檔案位於專案根目錄（與 `pom.xml` 同層級）
- 嘗試在 VS Code 整合終端執行 `mvn spring-boot:run`
- 確認已正確安裝 VS Code Java 擴充套件
</details>

### 除錯模式

要啟用詳細日誌，請取消註解 [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 中以下行：

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## 後續步驟

**設定完成！** 繼續您的學習之旅：

[第三章：核心生成式 AI 技術](../../../03-CoreGenerativeAITechniques/README.md)

## 資源

- [Spring AI 2 過渡到 OpenAI Java SDK](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 與 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [使用 Microsoft Entra ID 無密鑰驗證](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 入口網站](https://ai.azure.com/)
- [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
此文件已使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們努力追求準確性，但請注意自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應視為權威來源。對於關鍵資訊，建議採用專業人工翻譯。我們不對因使用此翻譯所產生的任何誤解或誤譯承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->