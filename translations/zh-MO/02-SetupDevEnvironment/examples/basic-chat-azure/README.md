# 基本使用 Azure AI Foundry 的聊天範例 - 端對端示例

這個範例是一個簡單的 Spring Boot 應用程式，使用 **無 API 金鑰驗證**（Microsoft Entra ID）連接到 **Azure AI Foundry** 模型並測試您的設置。它保留了 Spring AI 的 `ChatClient`，該客戶端由 **官方 OpenAI Java SDK** 和 **Azure OpenAI v1** 端點支援。

[pom.xml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml) 中的版本為 Spring Boot **4.1.1**、Spring AI **2.0.1**、OpenAI Java **4.63.1**、Azure Identity **1.18.6** 以及 dotenv-java **3.2.0**。此範例使用 `spring-ai-starter-model-openai` 並明確聲明了 `openai-java` 和 `azure-identity`；Spring AI 2 移除了舊版 Azure OpenAI starter。

## 目錄

- [前置需求](#前置需求)
- [快速開始](#快速開始)
- [驗證機制如何運作](#驗證機制如何運作)
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

## 前置需求

在執行此範例前，請確保您已：

- 擁有一個具有 `gpt-5.6-luna` 部署的 Azure AI Foundry 資源 - 可使用 `azd up` 或手動依照 [Azure AI Foundry 設定指南](../../getting-started-azure-openai.md) 來佈署
- 該資源擁有 **認知服務 OpenAI 用戶** 角色（Bicep 模板會自動分配給您）
- 安裝並使用[Azure CLI (`az`)](https://learn.microsoft.com/cli/azure/install-azure-cli)，且已使用 `az login` 登入
- 使用 Java 21+ 和 Maven 3.9+

> **無需 API 金鑰** — 驗證透過 Microsoft Entra ID 採用無金鑰方式。

## 快速開始

```bash
# 1. 導航至專案
cd 02-SetupDevEnvironment/examples/basic-chat-azure

# 2. 登入以便無鑰匙認證可以取得令牌
az login

# 3. 配置端點
#    - 如果你執行了 `azd up`，.env 檔案已為你寫好（可跳過這步）。
#    - 否則請複製範本並設定 AZURE_OPENAI_ENDPOINT：
cp .env.example .env

# 4. 執行應用程式
mvn spring-boot:run
```

## 驗證機制如何運作

本範例使用 **Microsoft Entra ID** 進行驗證 — 無需 API 金鑰。

應用程式在 [BasicChatApplication.java](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java) 中明確配置了驗證：

1. `azureCredential()` 使用 `AuthenticationUtil.getBearerTokenSupplier` 以 `DefaultAzureCredential` 和 `https://ai.azure.com/.default` 範圍建立 `BearerTokenCredential`。
2. `azureOpenAiClient()` 使用 `OpenAIOkHttpClient.builder()` 建立 `OpenAIClient`，將資源端點解析為 `/openai/v1`，並以 `.credential(...)` 提供承載憑證。
3. `azureChatModel()` 將此客戶端注入 Spring AI 的 `OpenAiChatModel`，支援本教學的 `ChatClient`。

這些明確的 Bean 可防止全域 `OPENAI_API_KEY` 覆蓋 Azure 驗證。只在 YAML 中省略 API 金鑰並不是完整的驗證設定。`DefaultAzureCredential` 在本地端可以使用您的 `az login` 工作階段，也可以在 Azure 中使用託管身份；選擇的身份必須擁有上述的資源角色。

## 執行應用程式

### 使用 Maven

```bash
mvn spring-boot:run
```

### 使用 VS Code

1. 在 VS Code 打開專案
2. 按 `F5` 或使用「執行與除錯」面板
3. 選擇「Spring Boot-BasicChatApplication」配置

> <strong>注意</strong>：應用程式會從其工作目錄載入 `.env`，包括從 VS Code 啟動時。

### 預期輸出

成功執行後的示範輸出（已略過啟動日誌；回應文字會有所不同）：

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

| 變數 | 描述 | 必填 | 範例 |
|----------|-------------|----------|---------|
| `AZURE_OPENAI_ENDPOINT` | Foundry（Azure OpenAI）端點 URL | 是 | `https://my-resource.openai.azure.com/` |
| `AZURE_OPENAI_DEPLOYMENT` | 聊天模型部署名稱 | 否 | `gpt-5.6-luna` （預設） |

> 沒有 **API 金鑰** 變數 — 驗證採用無金鑰方式（透過 `az login` 使用 Microsoft Entra ID）。

### Spring 設定

[application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 中使用 `spring.ai.openai` 前綴及平鋪的聊天屬性（無 `options` 區塊）：

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

`model` 是 **Azure 部署名稱**。驗證來自上述明確的 Beans，而非 `api-key` 設定。本教學禁用了推理並將完成的 token 數上限設為 500；`temperature` 和傳統的 `max-tokens` 則未設置。

Microsoft 建議 [新專案使用官方 OpenAI SDK 搭配 Azure OpenAI v1 與 Responses API](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)。本教學基於訊息模式，仍支援 Chat Completions。對 GPT-5.6 而言，若在 Chat Completions 中使用工具請將 `reasoning_effort` 設為 `none`；當需要結合推理與工具時使用 Responses API。請參閱 [推理模型的工具呼叫](https://learn.microsoft.com/azure/foundry/openai/how-to/reasoning#tool-calling-with-reasoning-models)。

## 疑難排解

### 常見問題

<details>
<summary><strong>錯誤：401 / “PermissionDenied” / 令牌錯誤</strong></summary>

- 執行 `az login` — 無金鑰驗證需有效登入才能取得令牌
- 確認帳號在資源中擁有 **認知服務 OpenAI 用戶** 角色
- 如剛分配角色，等待數分鐘以讓設定生效
- 確認您所使用的承租戶/訂閱正確（`az account show`）
</details>

<details>
<summary><strong>錯誤：“端點無效” / 連線錯誤</strong></summary>

- 確認 `AZURE_OPENAI_ENDPOINT` 是完整的基本 URL（例如 `https://your-resource.openai.azure.com/`）
- 檢查尾端斜線是否一致
- 確認端點與您實際佈署的資源相符（使用 `azd env get-values`）
</details>

<details>
<summary><strong>錯誤：“找不到部署”</strong></summary>

- 確認 `AZURE_OPENAI_DEPLOYMENT` 與 Azure 中的部署名稱一致
- 確認模型已成功部署且處於啟用狀態
- 預設部署名稱為 `gpt-5.6-luna`
</details>

<details>
<summary><strong>錯誤：429 / 超出速率限制</strong></summary>

- 預設 GPT-5.6 Luna 部署具有全球標準容量 10：10 請求/分鐘與 10,000 token/分鐘限制
- 依序執行範例並在重試前等待服務設定的重試間隔
- 此基本範例禁用了 SDK 的自動重試，因此失敗的請求會直接回報
</details>

<details>
<summary><strong>VS Code：環境變數未載入</strong></summary>

- 確保您的 `.env` 檔案位於專案根目錄（與 `pom.xml` 同層）
- 嘗試在 VS Code 內建終端機執行 `mvn spring-boot:run`
- 確認 VS Code 的 Java 擴充已正確安裝
</details>

### 除錯模式

若要啟用詳細日誌，可解除註解 [application.yml](../../../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/resources/application.yml) 中以下行：

```yaml
logging:
  level:
    "[org.springframework.ai]": DEBUG
    "[com.azure]": DEBUG
```

## 後續步驟

**設定完成！** 繼續您的學習旅程：

[第 3 章：生成式 AI 核心技巧](../../../03-CoreGenerativeAITechniques/README.md)

## 資源

- [Spring AI 2 OpenAI Java SDK 過渡指南](https://docs.spring.io/spring-ai/reference/upgrade-notes.html#_openai_java_sdk_transition)
- [官方 OpenAI Java SDK 搭配 Azure OpenAI v1](https://learn.microsoft.com/azure/foundry/openai/supported-languages?pivots=programming-language-java)
- [Microsoft Entra ID 的無金鑰驗證](https://learn.microsoft.com/azure/ai-foundry/foundry-models/how-to/configure-entra-id)
- [Azure AI Foundry 入口網站](https://ai.azure.com/)
- [Azure AI Foundry 文件](https://learn.microsoft.com/azure/ai-foundry/)

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->