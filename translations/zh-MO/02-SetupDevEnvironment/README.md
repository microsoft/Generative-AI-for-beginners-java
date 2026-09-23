# 為 Java 設置生成式 AI 的開發環境

> **快速開始：** 使用 Bicep + `azd` 在幾分鐘內於 **Azure AI Foundry** 上以程式碼部署你的 AI 模型 — 請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)。身份驗證採用 <strong>無金鑰</strong> (Microsoft Entra ID)，因此無需管理 API 金鑰。

## 你將學到什麼

- 為 AI 應用程式設置 Java 開發環境
- 選擇並配置你喜歡的開發環境（以雲端為主的 Codespaces、本地開發容器或完整本地設置）
- 通過連接 Azure AI Foundry 模型來測試你的設置

## 目錄

- [你將學到什麼](#你將學到什麼)
- [介紹](#介紹)
- [步驟 1：設置你的開發環境](#步驟-1：設置你的開發環境)
  - [選項 A：GitHub Codespaces（推薦）](#選項-a：github-codespaces（推薦）)
  - [選項 B：本地開發容器](#選項-b：本地開發容器)
  - [選項 C：使用你現有的本地安裝](#選項-c：使用你現有的本地安裝)
- [步驟 2：部署 Azure AI Foundry](#步驟-2：部署-azure-ai-foundry)
- [步驟 3：測試你的設置](#步驟-3：測試你的設置)
- [故障排除](#故障排除)
- [總結](#總結)
- [後續步驟](#後續步驟)

## 介紹

本章將引導你設置開發環境。在整個課程中，我們將使用 **Azure AI Foundry** 作為模型。你可以使用 Bicep 和 Azure 開發者 CLI (`azd`) 將模型以程式碼方式部署，然後使用 <strong>無金鑰身份驗證</strong> (Microsoft Entra ID) 連接 — 無需複製或洩漏 API 金鑰。

**無需本地設置！** 你可以使用 GitHub Codespaces，在瀏覽器中即擁有完整開發環境，並從那裡部署 Foundry。

之所以為本課程選用 **Azure AI Foundry**，是因為它：
- <strong>以程式碼方式部署</strong> — 一行 `azd up` 即可部署帳戶和模型
- <strong>無金鑰</strong> — 使用你的 Azure 登入或託管身份驗證
- <strong>生產級</strong> — 同一份程式碼可在本地和 Azure 運行
- <strong>靈活</strong> — 只需更改部署名稱即可更換模型，無需更改程式碼

> <strong>注意</strong>：Azure AI Foundry 的部署按代幣計費（按用量付費）。有關部署、區域和成本的詳細資訊，請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)。


## 步驟 1：設置你的開發環境

<a name="quick-start-cloud"></a>

我們已創建預配置的開發容器，以減少設置時間並確保你擁有本生成式 AI for Java 課程所需的所有工具。請選擇你偏好的開發方式：

### 環境設置選項：

#### 選項 A：GitHub Codespaces（推薦）

**2 分鐘內開始編碼—無需本地設置！**

1. 將本存儲庫分岔 (fork) 到你的 GitHub 帳戶
   > <strong>注意</strong>：如需編輯基本配置，請查看 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 點擊 **代碼 (Code)** → **Codespaces** 標籤 → **...** → **New with options...**
3. 使用預設值 — 這會選擇本課程專用的 <strong>開發容器配置</strong>：**生成式 AI Java 開發環境**
4. 點擊 **Create codespace**
5. 等待約 2 分鐘，環境即準備好
6. 繼續進行 [步驟 2：部署 Azure AI Foundry](#步驟-2：部署-azure-ai-foundry)

<img src="../../../translated_images/zh-MO/codespaces.9945ded8ceb431a5.webp" alt="Screenshot: Codespaces submenu" width="50%">

<img src="../../../translated_images/zh-MO/image.833552b62eee7766.webp" alt="Screenshot: New with options" width="50%">

<img src="../../../translated_images/zh-MO/codespaces-create.b44a36f728660ab7.webp" alt="Screenshot: Create codespace options" width="50%">


> **Codespaces 的好處**：
> - 無需本地安裝
> - 可在任何有瀏覽器的裝置上運行
> - 預先配置所有工具和依賴
> - 個人帳戶每月免費 60 小時
> - 為所有學習者提供一致的環境

#### 選項 B：本地開發容器

**適合偏好使用 Docker 進行本地開發的開發者**

1. 將本存儲庫分岔並克隆到你的本地機器
   > <strong>注意</strong>：如需編輯基本配置，請查看 [開發容器配置](../../../.devcontainer/devcontainer.json)
2. 安裝 [Docker Desktop](https://www.docker.com/products/docker-desktop/) 和 [VS Code](https://code.visualstudio.com/)
3. 在 VS Code 中安裝 [Dev Containers 擴充功能](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers)
4. 在 VS Code 中打開存儲庫資料夾
5. 出現提示時，點擊 **Reopen in Container**（或使用 `Ctrl+Shift+P` → "Dev Containers: Reopen in Container"）
6. 等待容器建置並啟動
7. 繼續進行 [步驟 2：部署 Azure AI Foundry](#步驟-2：部署-azure-ai-foundry)

<img src="../../../translated_images/zh-MO/devcontainer.21126c9d6de64494.webp" alt="Screenshot: Dev container setup" width="50%">

<img src="../../../translated_images/zh-MO/image-3.bf93d533bbc84268.webp" alt="Screenshot: Dev container build complete" width="50%">

#### 選項 C：使用你現有的本地安裝

**適合已有 Java 環境的開發者**

前置要求：
- [Java 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) 
- [Maven 3.9+](https://maven.apache.org/download.cgi)
- [VS Code](https://code.visualstudio.com) 或你喜歡的 IDE

步驟：
1. 克隆本存儲庫到你的本地機器
2. 在你的 IDE 中打開該專案
3. 繼續進行 [步驟 2：部署 Azure AI Foundry](#步驟-2：部署-azure-ai-foundry)

> <strong>專業提示</strong>：如果你的電腦配置較低但想使用本地 VS Code，建議使用 GitHub Codespaces！你可以將本地的 VS Code 連接到雲端的 Codespace，兼享兩者優勢。

<img src="../../../translated_images/zh-MO/image-2.fc0da29a6e4d2aff.webp" alt="Screenshot: created local devcontainer instance" width="50%">


## 步驟 2：部署 Azure AI Foundry

將課程的 AI 模型以程式碼方式部署到 Azure AI Foundry。於存儲庫根目錄執行：

```bash
cd 02-SetupDevEnvironment
azd auth login
az login
azd up
```

`azd` 會提示輸入環境名稱、訂閱和區域，部署包含 `gpt-5.6-luna` 和 `text-embedding-3-small` 部署的 Azure AI Foundry 帳戶，並將端點寫入範例的 `.env` 檔案 — 全程採用 <strong>無金鑰</strong> 身份驗證（無需 API 金鑰）。

> **完整流程說明：** 請參閱 [Azure AI Foundry 設置指南](getting-started-azure-openai.md) 了解前置條件、手動（入口網站）替代方案、區域建議以及成本和清理說明。

## 步驟 3：測試你的設置

模型部署完成後，使用範例應用程式 [`02-SetupDevEnvironment/examples/basic-chat-azure`](../../../02-SetupDevEnvironment/examples/basic-chat-azure) 測試連接。

1. 在你的開發環境中開啟終端機。
2. 移至範例目錄：
   ```bash
   cd 02-SetupDevEnvironment/examples/basic-chat-azure
   ```
3. 確認你已登入（無金鑰身份驗證需要令牌）：
   ```bash
   az login
   ```
   > 若你執行過 `azd up`，包含端點的 `.env` 檔案已自動為你寫入。
4. 執行應用程式：
   ```bash
   mvn clean spring-boot:run
   ```

你應該可以看到來自 `gpt-5.6-luna` 模型的回應。

### 了解範例程式碼

[basic-chat 範例](./examples/basic-chat-azure/README.md) 使用 **Spring Boot 4.1.1** 和 **Spring AI 2.0.1**。Spring AI 的 `ChatClient` 由官方 OpenAI Java SDK 支持，並以無金鑰身份驗證連接 Azure OpenAI **v1** 端點。

**此程式碼的功能：**
- 使用你的 Azure 登入 (Microsoft Entra ID) 連接 Azure AI Foundry — 無需 API 金鑰
- 向 `gpt-5.6-luna` 模型發送提示詞
- 接收並顯示 AI 的回應
- 驗證你的設置是否正常運作

<strong>關鍵依賴</strong>（摘自 [pom.xml](../../../02-SetupDevEnvironment/examples/basic-chat-azure/pom.xml)）：
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

POM 明確管理 OpenAI Java **4.63.1**，並顯式指定 Azure Identity **1.18.6**。Spring AI 2 取消了 Azure 特定的啟動器，但憑證 Bean 仍需 Azure Identity。

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

無金鑰身份驗證明確配置於 [BasicChatApplication.java](../../../02-SetupDevEnvironment/examples/basic-chat-azure/src/main/java/com/example/BasicChatApplication.java)，不依賴缺少的 API 金鑰推斷。它的持有人憑證使用帶有 `https://ai.azure.com/.default` 範圍的 `DefaultAzureCredential`，其 `OpenAIClient` 目標為 `/openai/v1`。應用程式將該客戶端提供給 Spring AI 的聊天模型，因此全球 `OPENAI_API_KEY` 無法覆蓋 Azure 認證。

聊天設定直接位於 `spring.ai.openai.chat` 下，無 `options` 區塊。課程保留理性推理等級為 `none` 以及 500 代幣的回應上限；未設定 `temperature` 或 `max-tokens`。請參閱 [範例配置參考](./examples/basic-chat-azure/README.md#spring-configuration) 了解 API 選擇及工具呼叫指引。

## 總結

完成上述步驟後，你將：

- 使用 Bicep + `azd` 以程式碼方式部署 Azure AI Foundry 模型
- 啟動你的 Java 開發環境（無論是 Codespaces、開發容器還是本地）
- 使用無金鑰身份驗證 (Microsoft Entra ID) 連接 Azure AI Foundry — 無需 API 金鑰
- 通過簡單範例測試且成功與模型通訊

## 後續步驟

[第 3 章：核心生成式 AI 技術](../03-CoreGenerativeAITechniques/README.md)

## 故障排除

遇到問題？以下是常見問題及解決方案：

- **認證失敗 (401/403)？**
  - 執行 `az login` — 身份驗證為無金鑰，必須登入
  - 確認你的帳戶在該資源具有 **Cognitive Services OpenAI User** 角色
  - 如剛剛完成部署，請等待一分鐘讓角色指派生效

- **找不到 Maven？**
  - 使用開發容器或 Codespaces 時，Maven 應已預安裝
  - 本地設置時，確保安裝了 Java 21+ 和 Maven 3.9+
  - 嘗試執行 `mvn --version` 驗證安裝

- **找不到 `azd` 或部署失敗？**
  - 安裝 [Azure Developer CLI](https://aka.ms/azure-dev/install) 並執行 `azd auth login`
  - 選擇有 `gpt-5.6-luna` 和 `text-embedding-3-small` 可用的區域（例如 `eastus2`），並確保訂閱中有足夠配額
  - 詳情請見 [Azure AI Foundry 設置指南](getting-started-azure-openai.md)

- **開發容器無法啟動？**
  - 確保 Docker Desktop 正在運行（本地開發）
  - 嘗試重建容器：`Ctrl+Shift+P` → "Dev Containers: Rebuild Container"

- **應用程式編譯錯誤？**
  - 確認你所在目錄為：`02-SetupDevEnvironment/examples/basic-chat-azure`
  - 嘗試清理並重新編譯：`mvn clean compile`

> **需要幫助？**：仍有問題？請在存儲庫中開啟 issue，我們會協助你。

---

<!-- CO-OP TRANSLATOR DISCLAIMER START -->
**免責聲明**：
本文件使用 AI 翻譯服務 [Co-op Translator](https://github.com/Azure/co-op-translator) 進行翻譯。雖然我們力求準確，但請注意，自動翻譯可能包含錯誤或不準確之處。原始文件的母語版本應被視為權威來源。對於重要資訊，建議尋求專業人工翻譯。我們不對因使用本翻譯而引起的任何誤解或曲解承擔責任。
<!-- CO-OP TRANSLATOR DISCLAIMER END -->