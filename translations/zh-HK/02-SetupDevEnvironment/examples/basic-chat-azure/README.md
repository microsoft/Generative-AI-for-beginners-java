# 基本與 Azure AI Foundry 的聊天 - 從頭到尾範例

此範例是一個簡單的 Spring Boot 應用程式，透過 <strong>無鑰匙驗證</strong>（Microsoft Entra ID）連接到 **Azure AI Foundry** 模型並測試您的設置。它使用由 **官方 OpenAI Java SDK** 和 **Azure OpenAI v1** 端點支持的 Spring AI 的 `ChatClient`。

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) 中的版本為 Spring Boot **4.1.1**、Spring AI **2.0.1**、OpenAI Java **4.63.1**、Azure Identity **1.18.6**、dotenv-java **3.2.0**。此範例使用 `spring-ai-starter-model-openai` 並明確聲明 `openai-java` 及 `azure-identity`；Spring AI 2 移除了舊版的 Azure OpenAI starter。

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
- [疑難排解](#疑難排解)
  - [常見問題](#常見問題)
  - [除錯模式](#除錯模式)
- [後續步驟](#後續步驟)
- [資源](#資源)

## 先決條件

執行此範例前，請確保您已：

- 擁有配置了 `gpt-5.6-luna` 部署的 Azure AI Foundry 資源 — 可使用 `azd up` 自動配置或按 [Azure AI Foundry 設定指南](../../getting-started-azure-openai.md) 手動配置
- 在該資源上具有 **Cognitive Services OpenAI User** 角色（Bicep 範本會為您指派）
- 已登入的 [Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)，使用 `az login`
- 安裝 Java 21+ 與 Maven 3.9+

> **不需要 API 金鑰** — 認證使用無鑰匙方式，透過 Microsoft Entra ID。

## 快速開始

```bash
# 1. 導航至專案
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. 登入以便無金鑰認證可以取得權杖
az login

# 3. 配置端點
#    - 如果你執行了 `azd up`，會為你寫入 .env（可跳過此步驟）。
#    - 否則複製範本並設定 AZURE_OPENAI_ENDPOINT：
cp .env.example .env

# 4. 執行應用程式
mvn spring-boot:run
```

## 驗證機制說明

此範例透過 **Microsoft Entra ID** 進行驗證 — 不使用 API 金鑰。

應用程式在 [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中明確配置驗證：

1. `azureCredential()` 使用 `AuthenticationUtil.getBearerTokenSupplier` 和 `DefaultAzureCredential`，以及 `https://ai.azure.com/.default` 權限範圍，建立 `BearerTokenCredential`。
2. `azureOpenAiClient()` 利用 `OpenAIOkHttpClient.builder()` 建立 `OpenAIClient`，將資源端點轉換成 `/openai/v1`，並通過 `.credential(...)` 提供持有令牌憑證。
3. `azureChatModel()` 將該客戶端提供給 Spring AI 的 `OpenAiChatModel`，支援本課程的 `ChatClient`。

這些明確的 Bean 阻止全局的 `OPENAI_API_KEY` 覆蓋 Azure 驗證。僅在 YAML 中省略 API 金鑰，並不構成完整驗證；`DefaultAzureCredential` 可以使用您本地的 `az login` 會話或 Azure 管理身分；所選的身分需擁有上述資源角色。

## 執行應用程式

### 使用 Maven

```bash
mvn spring-boot:run
```

### 使用 VS Code

1. 在 VS Code 中開啟專案
2. 按 `F5` 或使用「運行與除錯」面板
3. 選擇「Spring Boot-BasicChatApplication」設定

> <strong>注意</strong>：應用程式會從工作目錄載入 `.env`，無論是從 VS Code 啟動也一樣。

### 預期輸出

成功執行後範例輸出（忽略啟動日誌；回應詞句會有所不同）：

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

| 變數 | 說明 | 必填 | 範例 |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry（Azure OpenAI）端點 URL | 是 | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天模型部署名稱 | 否 | `gpt-5.6-luna`（預設） |

> 無 API 金鑰變數 — 認證採用無鑰匙方式（透過 `az login` 的 Microsoft Entra ID）。

### Spring 設定

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 設定使用 `spring.ai.openai` 前綴並平鋪聊天屬性（無 `options` 區塊）：

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

`model` 是 **Azure 部署名稱**。認證來自上述明確的 Bean，而非 `api-key` 設定。本課程禁用 reasoning 並將 completion tokens 限制為 500；未設置 `temperature` 和舊版的 `max-tokens`。

微軟建議 [用官方 OpenAI SDK 搭配 Azure OpenAI v1 與 Responses API 應用於新專案](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)。本課程以訊息為基底，仍支援 Chat Completions。對 GPT-5.6，如請求含有工具於 Chat Completions，必須將 `reasoning_effort` 設為 `none`；如需結合工具與 reasoning，請使用 Responses。詳情見 [帶推理模型的工具調用](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)。

## 疑難排解

### 常見問題

<details>
<summary><strong>錯誤：401 / "PermissionDenied" / 令牌錯誤</strong></summary>

- 執行 `az login` — 無鑰匙認證需要有效登入以取得令牌
- 確認您的帳戶在資源上具有 **Cognitive Services OpenAI User** 角色
- 若剛指派角色，需等候一分鐘讓權限生效
- 確認您身處正確的租戶/訂閱（`az account show`）
</details>

<details>
<summary><strong>錯誤："端點無效" / 連線錯誤</strong></summary>

- 確保 `AZURE_OPENAI_ENDPOINT` 是完整基本 URL（如 `https://your-resource.openai.azure.com/`）
- 檢查尾部斜線是否一致
- 確認端點與您配發的資源相符（`azd env get-values`）
</details>

<details>
<summary><strong>錯誤："找不到部署"</strong></summary>

- 確認 `AZURE_OPENAI_DEPLOYMENT` 與 Azure 內的部署名稱相符
- 檢查模型是否部署成功且處於啟用狀態
- 預設部署名稱為 `gpt-5.6-luna`
</details>

<details>
<summary><strong>錯誤：429 / 超出速率限制</strong></summary>

- 默認的 GPT-5.6 Luna 部署配有 Global Standard 容量 10：每分鐘 10 次請求和 10,000 令牌
- 逐個執行範例並在重試前等待服務的重試間隔
- 此簡單範例禁止 SDK 自動重試，因此失敗請求會直接報告錯誤
</details>

<details>
<summary><strong>VS Code：環境變數無法載入</strong></summary>

- 確保 `.env` 檔案位於專案根目錄（與 `pom.xml` 同層）
- 嘗試在 VS Code 整合終端執行 `mvn spring-boot:run`
- 確認已正確安裝 VS Code 的 Java 擴充套件
</details>

### 除錯模式

若要啟用詳細日誌，請在 [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 中取消註解以下行：

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## 後續步驟

**設定完成！** 繼續您的學習旅程：

[第 3 章：核心生成式 AI 技術](../../../03-CoreGenerativeAITechniques/README.md)

## 資源

- [Spring AI 2 OpenAI Java SDK 過渡](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 與 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID 無鑰匙認證](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 入口網站](https://ai.azure.com/)
- [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件由 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 翻譯而成。雖然我們致力於確保準確性，但請注意，機器自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議進行專業人工翻譯。我們不對因使用本翻譯而產生的任何誤解或誤釋承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->